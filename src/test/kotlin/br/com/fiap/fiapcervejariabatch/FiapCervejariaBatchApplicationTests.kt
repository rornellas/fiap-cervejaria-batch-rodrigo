package br.com.fiap.fiapcervejariabatch

import org.awaitility.Awaitility
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.Job
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.concurrent.TimeUnit
import javax.sql.DataSource

@SpringBootTest
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [FiapCervejariaChuckApplication::class, BatchTestConfig::class])
class FiapCervejariaBatchApplicationTests {
	@Autowired
	private lateinit var jobLauncher: JobLauncherTestUtils

	@Autowired
	private lateinit var datasource: DataSource

	@Autowired
	private lateinit var job: Job

	@Test
	@Throws(Exception::class)
	fun testJob() {
		val jobExecution = jobLauncher!!.jobLauncher
				.run(job, jobLauncher.uniqueJobParameters)

		Assertions.assertNotNull(jobExecution)
		Assertions.assertEquals(BatchStatus.COMPLETED, jobExecution.status)

		val resultSet = datasource!!.connection
				.prepareStatement("select count(*) from TB_PESSOA")
				.executeQuery()

		Awaitility.await().atMost(10, TimeUnit.SECONDS)
			.until {
				resultSet.last()
				resultSet.getInt(1) == 6
			}

		Assertions.assertEquals(1, resultSet.getInt(1))
	}
}
