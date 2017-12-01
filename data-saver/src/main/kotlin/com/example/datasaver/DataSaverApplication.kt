package com.example.datasaver

import com.example.kafka.EnableDataKafkaConsumers
import com.example.kafka.EnableDataKafkaProducers
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
@EnableDataKafkaProducers
@EnableDataKafkaConsumers
class DataSaverApplication

fun main(args: Array<String>) {
    runApplication<DataSaverApplication>(*args)
}