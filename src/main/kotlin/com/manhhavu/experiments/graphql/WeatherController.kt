package com.manhhavu.experiments.graphql

import graphql.GraphQL
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class WeatherController @Autowired constructor (val graph: GraphQL) {

    @RequestMapping(value = "/weather",
            method = arrayOf(RequestMethod.POST),
            produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @ResponseBody
    fun run(@RequestParam("query") query: String): ResponseEntity<Map<String, Any>> {
        val result = graph.execute(query)
        val body = if (!result.errors.isEmpty()) {
            mapOf("errors" to result.errors)
        } else {
            mapOf("data" to result.data)
        }

        return ResponseEntity(body, HttpStatus.OK)
    }
}