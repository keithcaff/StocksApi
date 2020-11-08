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
POST | http://localhost:8080/stocks/folllow | [ { "symbol": "NKE","name": "NIKE Inc.","region": "United States","currency": "USD"}] | Creates a new list of 'followed' stocks for the authorized user |

## Okta
To access the protected server, you need a valid JSON Web Token.
Details on how to set up your own Okta Authorization server and get a JWT token can be found [here](https://developer.okta.com/blog/2019/12/26/java-mongodb-crud#create-an-oidc-application-for-your-java--mongodb-app)
