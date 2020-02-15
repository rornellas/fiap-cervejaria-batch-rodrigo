package br.com.fiap.fiapcervejariabatch

import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BatchTestConfig {

    @Bean
    fun jobLauncherTestUtils(): JobLauncherTestUtils {
        return JobLauncherTestUtils()
    }

}