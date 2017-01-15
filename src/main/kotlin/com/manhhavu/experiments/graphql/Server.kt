package com.manhhavu.experiments.graphql

import com.google.gson.Gson
import graphql.GraphQL
import okhttp3.OkHttpClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
open class Application {

    @Bean
    open fun weatherSchema(@Value("\${openweathermap.appid}") appId: String): WeatherSchema {
        return WeatherSchema(appId, OkHttpClient(), Gson())
    }

    @Bean
    open fun graph(schema: WeatherSchema): GraphQL {
        return GraphQL(schema.schema)
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}
