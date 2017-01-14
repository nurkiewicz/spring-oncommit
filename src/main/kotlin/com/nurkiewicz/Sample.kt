package com.nurkiewicz

import com.codahale.metrics.Meter
import com.codahale.metrics.MetricRegistry
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jms.core.JmsOperations
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionSynchronizationAdapter
import org.springframework.transaction.support.TransactionSynchronizationManager
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
import java.util.concurrent.TimeUnit

@RestController
open class Sample(
		private val jms: JmsOperations,
		private val jdbc: JdbcOperations,
		metricRegistry: MetricRegistry) {

	private val meter: Meter = metricRegistry.meter("requests")

	@Transactional
	@RequestMapping(method = arrayOf(GET, POST), value = "/")
	open fun test(): String {
		TransactionSynchronizationManager.registerSynchronization(sendMessageAfterCommit())
		val result = jdbc.queryForObject("SELECT 2 + 2", Int::class.java)
		meter.mark()
		return "OK " + result
	}

	private fun sendMessageAfterCommit(): TransactionSynchronizationAdapter {
		return object : TransactionSynchronizationAdapter() {
			override fun afterCommit() {
				val result = "Hello " + Instant.now()
				TimeUnit.SECONDS.sleep(5)
				jms.send("queue", { it.createTextMessage(result) })
			}
		}
	}

}