package com.nurkiewicz

import com.codahale.metrics.JmxReporter
import com.codahale.metrics.MetricRegistry
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class Application(metricRegistry: MetricRegistry) {
	init {
		JmxReporter
				.forRegistry(metricRegistry)
				.build()
				.start()
	}
}


fun main(args: Array<String>) {
	SpringApplication.run(Application::class.java, *args)
}
