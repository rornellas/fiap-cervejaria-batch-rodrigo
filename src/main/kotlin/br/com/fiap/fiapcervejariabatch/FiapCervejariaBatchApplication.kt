//package br.com.fiap.fiapcervejariabatch
//
//import org.slf4j.LoggerFactory
//import org.springframework.batch.core.Job
//import org.springframework.batch.core.Step
//import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
//import org.springframework.batch.core.step.tasklet.Tasklet
//import org.springframework.batch.repeat.RepeatStatus
//import org.springframework.beans.factory.annotation.Qualifier
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.boot.autoconfigure.SpringBootApplication
//import org.springframework.boot.runApplication
//import org.springframework.context.annotation.Bean
//import java.nio.file.Paths
//
//@SpringBootApplication
//@EnableBatchProcessing
//class CervejariaBatchApplication {
//
//	internal var logger = LoggerFactory.getLogger(CervejariaBatchApplication::class.java)
//
//	@Bean
//	fun tasklet(@Value("\${file.path}") path: String): Tasklet {
//		return Tasklet { contribution, chunkContext ->
//			val file = Paths.get(path).toFile()
//			if (file.delete()) {
//				logger.info("Arquivo deletado")
//			} else {
//				logger.info("não foi possivel deletar o arquivo")
//			}
//			RepeatStatus.FINISHED
//		}
//	}
//
//	@Bean
//	@Qualifier("stepfile")
//	fun step(stepBuilderFactory: StepBuilderFactory, tasklet: Tasklet): Step {
//		return stepBuilderFactory.get("Delete File Step")
//				.tasklet(tasklet)
//				.build()
//	}
//
//	@Bean
//	@Qualifier("jobdelete")
//	fun job(jobBuilderFactory: JobBuilderFactory, @Qualifier("stepfile") step: Step): Job {
//		return jobBuilderFactory.get("delete file")
//				.start(step)
//				.build()
//	}
//
//}
//
//fun main(args: Array<String>) {
//	runApplication<CervejariaBatchApplication>(*args)
//}