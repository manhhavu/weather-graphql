# OpenWeatherMap's GraphQL implementation

Demonstrates a GraphQL server proxied to [OpenWeatherMap API](http://openweathermap.org/api)

## Features
1. Query following services:
    * [current weather data for a location](https://openweathermap.org/current#one)
    * [5 day/3 hour forecast for a location](https://openweathermap.org/forecast5) 
2. Combine multiple services in a same GraphQL query, e.g. we can have current weather and forecasts in a same response.

## How to run

You need to specify a OpenWeatherMap APPID as an environment variable (OWM_ID).

Example request:
```
{ 
	location(cityName: "London") { 
		id 
		name  
		now {
		    main {
			    temp 
			    humidity
			 }
		} 
		forecasts  { 
			dt 
			dt_txt 
			main { 
				temp 
				humidity 
			} 
			weather { 
				main 
				description 
			} 
		}  
	} 
}
```
as **query** parameter in a POST (x-www-form-urlencoded) request sent to [http://localhost:9090/location](http://localhost:9090/location). More technicalIndicators can be found in **WeatherSchema** class.


## Credits
* [TodoMVC Relay Java sample](https://github.com/graphql-java/todomvc-relay-java/)