# OpenWeatherMap's GraphQL implementation

Demonstrates a GraphQL server proxied to [OpenWeatherMap API](http://openweathermap.org/api)

## Features
1. Query [current weather data for one location](https://openweathermap.org/current#one) by specifying city's name or id. 

## How to run

You need to specify a OpenWeatherMap APPID as an environment variable (OWM_ID).

Example request:
```
{  
    location(cityName: "London") { 
        id 
        name 
        main { 
            temp 
            pressure
            humidity
            temp_min
            temp_max
        } 
    }  
}
```
as **query** parameter in a POST (x-www-form-urlencoded) request sent to [http://localhost:9090/weather](http://localhost:9090/weather). More parameters can be found in **WeatherSchema** class.




## Credits
* [TodoMVC Relay Java sample](https://github.com/graphql-java/todomvc-relay-java/)