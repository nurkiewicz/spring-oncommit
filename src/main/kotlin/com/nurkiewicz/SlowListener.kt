package com.nurkiewicz

import org.slf4j.LoggerFactory
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit
import javax.jms.Message

@Component
class SlowListener {

	private val log = LoggerFactory.getLogger(SlowListener::class.java)

	@JmsListener(destination = "queue", concurrency = "1")
	fun onMessage(msg: Message): Unit {
		log.debug("Received: {}", msg)
		TimeUnit.SECONDS.sleep(10)
	}

}