package com.manhhavu.experiments.graphql

import com.google.gson.Gson
import graphql.Scalars
import graphql.schema.GraphQLFieldDefinition.newFieldDefinition
import graphql.schema.GraphQLObjectType.newObject
import graphql.schema.GraphQLSchema
import okhttp3.OkHttpClient
import okhttp3.Request

class WeatherSchema constructor(val appId: String,
                                val httpClient: OkHttpClient,
                                val gson: Gson) {
    val schema: GraphQLSchema

    init {
        val parameters = newObject()
                .name("Parameters")
                .field(newFieldDefinition().name("temp").type(Scalars.GraphQLFloat))
                .field(newFieldDefinition().name("pressure").type(Scalars.GraphQLInt))
                .field(newFieldDefinition().name("humidity").type(Scalars.GraphQLInt))
                .field(newFieldDefinition().name("temp_min").type(Scalars.GraphQLFloat))
                .field(newFieldDefinition().name("temp_max").type(Scalars.GraphQLFloat))

        val weather = newObject()
                .name("Weather")
                .field(newFieldDefinition()
                        .name("id")
                        .type(Scalars.GraphQLFloat))
                .field(newFieldDefinition()
                        .name("name")
                        .type(Scalars.GraphQLString))
                .field(newFieldDefinition()
                        .name("main")
                        .type(parameters))

        val queryType = newObject()
                .name("City")
                .field(newFieldDefinition()
                        .name("weather")
                        .type(weather)
                        .dataFetcher {
                            val request = Request.Builder()
                                    .url("http://api.openweathermap.org/data/2.5/weather?q=London&APPID=$appId")
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
}