package com.manhhavu.experiments.graphql

import com.mashape.unirest.http.Unirest
import graphql.Scalars.*
import graphql.schema.GraphQLFieldDefinition.newFieldDefinition
import graphql.schema.GraphQLList
import graphql.schema.GraphQLObjectType
import graphql.schema.GraphQLObjectType.newObject
import graphql.schema.GraphQLSchema

import java.util.*

class WeatherSchema constructor(val appId: String) {
    private val endpoint = "http://api.openweathermap.org/data/2.5"

    val schema: GraphQLSchema

    init {
        val queryType = newObject()
                .name("Query")
                .field(newFieldDefinition()
                        .name("location")
                        .type(location())
                        .argument { it.name("cityName").type(GraphQLString).defaultValue("London") }
                        .argument { it.name("id").type(GraphQLLong) }
                        .dataFetcher {
                            val query = Optional.ofNullable(it.arguments["id"])
                                    .map { "id=$it" }
                                    .orElse("q=${it.arguments["cityName"]}")
                            val response = Unirest.get("$endpoint/weather?$query&APPID=$appId")
                                    .header("accept", "application/json")
                                    .asJson().body.`object`.asMap()

                            mapOf("id"   to response["id"],
                                  "name" to response["name"],
                                  "now"  to mapOf("main"    to response["main"],
                                                  "weather" to response["weather"]))
                        })
                .build()
        schema = GraphQLSchema.newSchema()
                .query(queryType)
                .build()
    }

    private fun now(): GraphQLObjectType.Builder {
        return newObject()
                .name("Now")
                .field(newFieldDefinition().name("main").type(technicalIndicators()))
                .field(newFieldDefinition().name("weather").type(GraphQLList(humanlyReadableIndicators().build())))
    }

    private fun location(): GraphQLObjectType.Builder {
        return newObject()
                .name("Location")
                .field(newFieldDefinition().name("id").type(GraphQLFloat))
                .field(newFieldDefinition().name("name").type(GraphQLString))
                .field(newFieldDefinition().name("now").type(now()))
                .field(newFieldDefinition()
                        .name("forecasts")
                        .type(GraphQLList(dayForecast().build()))
                        .dataFetcher {
                            val query = "q=London"
                            val response = Unirest.get("$endpoint/forecast?$query&APPID=$appId")
                                    .header("accept", "application/json")
                                    .asJson().body.`object`.asMap()

                            response["list"]
                        })
    }

    private fun technicalIndicators(): GraphQLObjectType.Builder {
        return newObject()
                .name("TechnicalIndicators")
                .field(newFieldDefinition().name("temp").type(GraphQLFloat))
                .field(newFieldDefinition().name("pressure").type(GraphQLFloat))
                .field(newFieldDefinition().name("humidity").type(GraphQLFloat))
                .field(newFieldDefinition().name("temp_min").type(GraphQLFloat))
                .field(newFieldDefinition().name("temp_max").type(GraphQLFloat))
    }

    private fun humanlyReadableIndicators() : GraphQLObjectType.Builder {
        return newObject()
                .name("HumanlyReadableIndicators")
                .field(newFieldDefinition().name("main").type(GraphQLString))
                .field(newFieldDefinition().name("description").type(GraphQLString))
                .field(newFieldDefinition().name("icon").type(GraphQLString))
    }

    private fun dayForecast() : GraphQLObjectType.Builder {
        return newObject()
                .name("DayForecast")
                .field(newFieldDefinition().name("dt").type(GraphQLString))
                .field(newFieldDefinition().name("dt_txt").type(GraphQLString))
                .field(newFieldDefinition().name("main").type(technicalIndicators()))
                .field(newFieldDefinition().name("weather").type(GraphQLList(humanlyReadableIndicators().build())))
    }

}