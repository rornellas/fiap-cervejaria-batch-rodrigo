package br.com.fiap.fiapcervejariabatch

import br.com.fiap.fiapcervejariabatch.entity.Pessoa
import org.slf4j.LoggerFactory
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import javax.sql.DataSource
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.beans.factory.annotation.Qualifier

import org.springframework.batch.core.Job
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.core.io.FileSystemResource

@SpringBootApplication
@EnableBatchProcessing
class FiapCervejariaChuckApplication {
    var logger = LoggerFactory.getLogger(FiapCervejariaChuckApplication::class.java)

    @Bean
    fun itemReader(@Value("\${file.input}") resource: FileSystemResource): FlatFileItemReader<Pessoa> {
        return FlatFileItemReaderBuilder<Pessoa>()
                .delimited().delimiter(";").names("name", "cpf")
                .resource(resource)
                .targetType(Pessoa::class.java)
                .name("File Item Reader")
                .build()
    }

    @Bean
    fun itemProcessor(): ItemProcessor<Pessoa, Pessoa> {
        return ItemProcessor<Pessoa, Pessoa> { pessoa ->
            pessoa.nome = pessoa.nome!!.toUpperCase()
            pessoa.cpf = pessoa.cpf!!
                    .replace("\\.".toRegex(), "")
                    .replace("-", "")
            pessoa
        }
    }

    @Bean
    fun itemWriter(dataSource: DataSource): ItemWriter<Pessoa> {
        return JdbcBatchItemWriterBuilder<Pessoa>()
                .beanMapped()
                .dataSource(dataSource)
                .sql("insert into tb_pessoa (nome,cpf) values (:nome,:cpf)")
                .build()
    }

    @Bean
    @Qualifier("stepchuck")
    fun step(stepBuilderFactory: StepBuilderFactory,
             itemReader: ItemReader<Pessoa>,
             itemProcessor: ItemProcessor<Pessoa, Pessoa>,
             itemWriter: ItemWriter<Pessoa>): Step {
        return stepBuilderFactory.get("step processar pessoa")
                .chunk<Pessoa, Pessoa>(2)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build()
    }

    @Bean
    @Qualifier("jobchuck")
    fun job(jobBuilderFactory: JobBuilderFactory, @Qualifier("stepchuck") step: Step): Job {
        return jobBuilderFactory.get("job processar pessoa")
                .start(step)
                .build()
    }
}

fun main(args: Array<String>) {
    runApplication<FiapCervejariaChuckApplication>(*args)
}