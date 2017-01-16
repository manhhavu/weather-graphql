package com.manhhavu.experiments.graphql

import graphql.GraphQL
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
open class Application {

    @Bean
    open fun weatherSchema(@Value("\${openweathermap.appid}") appId: String): WeatherSchema {
        return WeatherSchema(appId)
    }

    @Bean
    open fun graph(schema: WeatherSchema): GraphQL {
        return GraphQL(schema.schema)
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}
