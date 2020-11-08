## StocksApi

## About
StocksAPI allows users to
- login to their portfolio
- search for stocks
- follow stocks they are interested in 
- unfollow stocks they are no longer interested in

### Tech Stack:
* [Spring Boot](http://spring.io/projects/spring-boot) for creating the RESTful Web Services
* [Okta](https://developer.okta.com/) as the user management store and OAuth2 authorization server
* [MongoDB](https://www.mongodb.com/) as StocksApi database
* [Gradle](https://gradle.org/) for managing the project's build

## Endpoints
Request Method | URI | Body (JSON) | Description |  
:---: | :--- | :---: | :--- |
GET | http://localhost:8080/stocks/search?{keywords} | - | Search for stocks using keywords. Returns the best-matching symbols and market information based on keywords of your choice. |