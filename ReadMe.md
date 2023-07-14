# Spring Boot, Java, PostgreSQL Case Study

This is a Spring Boot application that showcases the Midas case study. It provides endpoints to retrieve and sync instrument and market data from the Robinhood API.

### Testing the Application
A Postman collection is provided along with this documentation, which contains sample requests for testing the application's endpoints. Import the Postman collection and execute the requests to interact with the application. For sync requests, please refer to the database to verify that the data has been synced or check the console for the log.

## Entities

The application consists of the following entities:

### Instrument

- Represents an instrument with its symbol, name, custom name, and associated market.
- The fields include:
    - `id`: Unique identifier for the instrument.
    - `symbol`: Unique symbol of the instrument.
    - `name`: Name of the instrument.
    - `customName`: Custom name for the instrument.
    - `market`: Associated market for the instrument.

### InstrumentDTO

- Represents a DTO (Data Transfer Object) for the Instrument entity.
- The fields include:
    - `id`: Unique identifier for the instrument.
    - `symbol`: Symbol of the instrument.
    - `name`: Name of the instrument.
    - `customName`: Custom name for the instrument.
    - `marketId`: ID of the associated market.

### Market

- Represents a market with its code, symbol, name, country, and website.
- The fields include:
    - `MarketId`: Unique identifier for the market.
    - `Code`: Unique code of the market.
    - `Symbol`: Symbol of the market.
    - `Name`: Name of the market.
    - `Country`: Country of the market.
    - `Website`: Website of the market.

## Repositories

The application includes the following repositories:

### InstrumentRepository

- Provides CRUD operations for the Instrument entity.
- Includes the following methods:
    - `findBySymbol(symbol)`: Retrieves an instrument by its symbol.
    - `findByMarket(market)`: Retrieves instruments associated with a specific market.

### MarketRepository

- Provides CRUD operations for the Market entity.

## Services

The application includes the following services:

### InstrumentService

- Handles business logic related to instruments.
- Includes the following methods:
    - `syncInstruments()`: Synchronizes instruments by retrieving data from the Robinhood API and saving it to the database.
    - `getAllInstruments()`: Retrieves all instruments from the database.
    - `getInstrumentBySymbol(symbol)`: Retrieves an instrument by its symbol using cache for improved performance.

### MarketService

- Handles business logic related to markets.
- Includes the following methods:
    - `syncMarkets()`: Synchronizes markets by retrieving data from the Robinhood API and saving it to the database.

## Controllers

The application includes the following controllers:

### InstrumentController

- Exposes endpoints related to instruments.
- Includes the following routes:
    - `GET /instruments/sync`: Syncs instruments by calling the `syncInstruments()` method of the `InstrumentService`.
    - `GET /instruments/getall`: Retrieves all instruments by calling the `getAllInstruments()` method of the `InstrumentService`.
    - `GET /instruments/{symbol}`: Retrieves an instrument by its symbol by calling the `getInstrumentBySymbol(symbol)` method of the `InstrumentService`.

### MarketController

- Exposes endpoints related to markets.
- Includes the following route:
    - `GET /markets/sync`: Syncs markets by calling the `syncMarkets()` method of the `MarketService`.

## Configuration

The application includes the following configuration:

### CacheConfig

- Configures caching for instrument retrieval.
- Includes a cache manager bean that utilizes the `SimpleCacheManager` and sets up a cache named "instruments" with a cache expiration time of 1 hour.

## Application Properties

The application properties include the configuration for the database connection:

```
spring.datasource.url=jdbc:postgresql://localhost:5436/midas
spring.datasource.username=postgres
spring.datasource.password=123
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.hibernate.show-sql=true
```

Please ensure that you have a PostgreSQL database running on `localhost:5436` with the specified credentials and database name.

## How to Run

To run the Spring Boot application, follow these steps:

1. Ensure that you have a PostgreSQL database running on `localhost:5436` with the specified credentials and database name.
2. Import the data on seed.sql into your empty database.
3. Build the application using Maven: `mvn clean install`.
4. Run the application: `java -jar target/springmidas-0.0.1-SNAPSHOT.jar`.
5. The application will be accessible at `http://localhost:8080`.
6. Import the Postman collection and execute the requests to interact with the application.

Note: In order to use instrument sync endpoint, you have to insert the seed.sql into the database. The seed.sql file is located in the main folder. This is due to sync endpoint fetching only instruments available in the database. This is due to Robinhood API restrictions. Since the API does not provide a way to fetch all instruments, we have to fetch them one by one. This is why we have to insert the seed.sql into the database to get instrument symbols one by one.

## API Documentation

- `GET /instruments/sync`: Synchronizes instruments by retrieving data from the Robinhood API and saving it to the database.
- `GET /instruments/getall`: Retrieves all instruments.
- `GET /instruments/{symbol}`: Retrieves an instrument by its symbol.
- `GET /markets/sync`: Synchronizes markets by retrieving data from the Robinhood API and saving it to the database.

## Dependencies

The application uses the following dependencies:

- Spring Boot Starter Data JPA
- Spring Boot Starter Web
- Spring Boot Starter Webflux
- PostgreSQL Driver
- Lombok
- Spring Boot Starter Test
- Jakarta Persistence API
- Spring Boot Starter Cache

Make sure to include these dependencies in your `pom.xml` file.

## Test Coverage

The application includes unit tests to ensure the correctness of the implemented functionality. The tests cover various scenarios, including repository, service, and controller tests.

### Tests
The application includes unit tests to ensure the correctness of repository methods and service functionality:

- InstrumentRepositoryTest: Tests the repository methods for retrieving instruments.
- MarketRepositoryTest: Tests the repository methods for retrieving markets.
- InstrumentServiceTest: Tests the service methods for retrieving instruments.
- MarketServiceTest: Tests the service methods for syncing markets.

## Issue with Dockerization

**Please note that the Dockerization of this application was attempted, but encountered an error when the application container tried to connect to the PostgreSQL database. As a result, it was not possible to fully test the functionality of the Dockerized application.**

### Docker Commands (Not Fully Tested)

To build and run the Dockerized application, use the following commands:
```
docker build -t springmidasv8 .
docker run -d -p 8080:8080 --name=testapp_con springmidasv8
```
---

Enjoy exploring the Midas case study with this Spring Boot application! If you have any questions or need further assistance, please don't hesitate to reach out me ykircova@gmail.com
