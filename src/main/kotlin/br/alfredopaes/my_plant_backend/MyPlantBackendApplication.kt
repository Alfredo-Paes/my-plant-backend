package br.alfredopaes.my_plant_backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class MyPlantBackendApplication

fun main(args: Array<String>) {
	runApplication<MyPlantBackendApplication>(*args)
}
