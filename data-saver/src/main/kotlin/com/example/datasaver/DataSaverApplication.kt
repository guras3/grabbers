package com.example.datasaver

import com.example.kafka.EnableDataKafkaConsumers
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableDataKafkaConsumers
class DataSaverApplication

fun main(args: Array<String>) {
    runApplication<DataSaverApplication>(*args)
}