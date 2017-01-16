package com.manhhavu.experiments.graphql

import com.google.gson.Gson
import graphql.Scalars.*
import graphql.schema.GraphQLFieldDefinition.newFieldDefinition
import graphql.schema.GraphQLObjectType
import graphql.schema.GraphQLObjectType.newObject
import graphql.schema.GraphQLSchema
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.*

class WeatherSchema constructor(val appId: String,
                                val httpClient: OkHttpClient,
                                val gson: Gson) {
    val schema: GraphQLSchema

    init {
        val queryType = newObject()
                .name("Query")
                .field(newFieldDefinition()
                        .name("location")
                        .type(weather(parameters()))
                        .argument { it.name("cityName").type(GraphQLString).defaultValue("London") }
                        .argument { it.name("id").type(GraphQLLong) }
                        .dataFetcher {
                            val query = Optional.ofNullable(it.arguments["id"])
                                    .map { "id=$it" }
                                    .orElse("q=${it.arguments["cityName"]}")

                            val request = Request.Builder()
                                    .url("http://api.openweathermap.org/data/2.5/weather?$query&APPID=$appId")
                                    .get().build()

                            val response = httpClient.newCall(request).execute().body().string()
                            val json = gson.fromJson(response, Map::class.java)

                            mapOf("id" to json["id"],
                                  "name" to json["name"],
                                  "main" to json["main"])
                        })
                .build()
        schema = GraphQLSchema.newSchema()
                .query(queryType)
                .build()
    }

    private fun parameters(): GraphQLObjectType.Builder? {
        return newObject()
                .name("Parameters")
                .field(newFieldDefinition().name("temp").type(GraphQLFloat))
                .field(newFieldDefinition().name("pressure").type(GraphQLFloat))
                .field(newFieldDefinition().name("humidity").type(GraphQLFloat))
                .field(newFieldDefinition().name("temp_min").type(GraphQLFloat))
                .field(newFieldDefinition().name("temp_max").type(GraphQLFloat))
    }

    private fun weather(parameters: GraphQLObjectType.Builder?): GraphQLObjectType.Builder? {
        return newObject()
                .name("Weather")
                .field(newFieldDefinition()
                        .name("id")
                        .type(GraphQLFloat))
                .field(newFieldDefinition()
                        .name("name")
                        .type(GraphQLString))
                .field(newFieldDefinition()
                        .name("main")
                        .type(parameters))
    }
}