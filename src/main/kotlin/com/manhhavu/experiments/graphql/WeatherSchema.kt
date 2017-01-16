package com.manhhavu.experiments.graphql

import com.mashape.unirest.http.Unirest
import graphql.Scalars.*
import graphql.schema.GraphQLFieldDefinition.newFieldDefinition
import graphql.schema.GraphQLObjectType
import graphql.schema.GraphQLObjectType.newObject
import graphql.schema.GraphQLSchema

import java.util.*

class WeatherSchema constructor(val appId: String) {
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

                            Unirest.get("http://api.openweathermap.org/data/2.5/weather?$query&APPID=$appId")
                                    .header("accept", "application/json")
                                    .asJson().body.`object`.asMap()
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