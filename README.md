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
GET | http://localhost:8080/stocks/search?keywords=apple | - | Search for stocks using keywords. Returns the best-matching symbols and market information based on keywords of your choice. |
POST | http://localhost:8080/user/stocks | [ { "symbol": "NKE","name": "NIKE Inc.","region": "United States","currency": "USD"}] | Creates a new set of 'followed' stocks for the authorized user |
GET | http://localhost:8080/user/stocks |  | Gets a set of 'followed' stocks for the authorized user |
PUT | http://localhost:8080/user/stocks/{userStockId} |  | Updates the set of 'followed' stocks using the userStocksId |

## Okta
To access the protected server, you need a valid JSON Web Token.
Details on how to set up your own Okta Authorization server and get a JWT token can be found [here](https://developer.okta.com/blog/2019/12/26/java-mongodb-crud#create-an-oidc-application-for-your-java--mongodb-app)

## To run the unit and integration tests
From root of the project 
gradle:
```
./gradlew clean build
```

## To run the application
### Dependencies
MongoDB - The mongo daemon process needs to be running on localhost:27017. Steps on how to install with homebrew are outlined [here](https://treehouse.github.io/installation-guides/mac/mongo-mac.html)
### Config
Ensure you have set Okta oauth config values in application.yml file. Populate 'issuer', 'client-id' and 'client-secret' properties
```
okta:
  oauth2:
    issuer: https://dev-8153053.okta.com/oauth2/default
    client-id: 1234567688
    client-secret: 1234567688
```
 
2. Run the following gradle command from the root project directory:
```
./gradlew bootRun
```
3. You can navigate to localhost:8080/ to view the home screen where you can login/create a user
4. If you just want to invoke the rest endpoints you can get a valid token using [OIDCDebugger](https://oidcdebugger.com/). Details on how to get token from OIDC Debugger are outlined in this Okta blog entry [here](https://developer.okta.com/blog/2020/01/09/java-rest-api-showdown#generate-tokens-using-openid-connect-debugger)
