[![Maintainability](https://api.codeclimate.com/v1/badges/ff151c01509f83514e84/maintainability)](https://codeclimate.com/github/DariaKarpova3108/library/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/ff151c01509f83514e84/test_coverage)](https://codeclimate.com/github/DariaKarpova3108/library/test_coverage)

Electronic library

## Description
Electronic library is a web application that allows users to register, search for books, and borrow them using a library card. The application supports administrative functions for managing the library collection.

## Functionality
- **User registration and authorization**: users can create accounts and log in to access the functionality.
- **Book search**: conveniently search for books by title, author, genre, and publisher.
- **Management of reader cards**: users can borrow books.
- **Administrative functions**: administrators can manage books, genres, authors, publishers, and readers.

## Technologies
The project is built on the following technologies:
- **Backend**: Spring Boot
- **Database**: H2 (for development), PostgreSQL (for production and development)
- **Authentication and security**: Spring Security, JWT
- **API documentation**: Springdoc OpenAPI, Swagger
- **Testing**: JUnit 5, MockWebServer, Datafaker
- **Deployment**: Docker

## Project structure
- **controllers**: REST controllers for handling HTTP requests.
- **dto**: Data Transfer Objects for transferring data between application layers.
- **models**: Entities representing data in the database.
- **repositories**: Interfaces for working with the database.
- **service**: Services containing business logic.
- **config**: Configuration classes for setting up security and other aspects of the application.
- **mapper**: MapStruct mappers for converting between entities and DTOs.
- **exception**: Handling and customizing exceptions.
- **handler**: Global error handling in the application.

API documentation is available at http://localhost:9090/swagger-ui.html after running the application.
