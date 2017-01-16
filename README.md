# OpenWeatherMap's GraphQL implementation

Demonstrates a GraphQL server proxied to [OpenWeatherMap API](http://openweathermap.org/api)

## How to run

You need to specify a OpenWeatherMap APPID as an environment variable (OWM_ID).

Example request:
```
{  
    weather(city: "London") { 
        id 
        name 
        main { 
            temp 
        } 
    }  
}
```
sent to http://localhost:9090/weather. More parameters can be found in **WeatherSchema** class.

## Credits
* [TodoMVC Relay Java sample](https://github.com/graphql-java/todomvc-relay-java/)