[![Maintainability](https://api.codeclimate.com/v1/badges/ff151c01509f83514e84/maintainability)](https://codeclimate.com/github/DariaKarpova3108/library/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/ff151c01509f83514e84/test_coverage)](https://codeclimate.com/github/DariaKarpova3108/library/test_coverage)

# Electronic library


## Description
The Electronic Library is a web application designed to manage a digital library. It allows users to register, search for books, and borrow them using a library card. The application supports administrative functions to manage the library collection.

## Functionality
- **User registration and authorization**: Users can create accounts and log in to access the functionality.
- **Book search**: Conveniently search for books by title, author, genre, and publisher.
- **Management of reader cards**: Users can borrow books and manage their borrowed items.
- **Administrative functions**: Administrators can manage books, genres, authors, publishers, and readers.
- **Email notifications**: Readers receive email reminders 3 days before the due date to either return or renew their borrowed books.

## Features
### Email Notifications
The project includes a scheduled service that sends an email to users reminding them to return or extend their borrowed books three days before the due date. The service works as follows:
1. Every day, the system checks for books that are due for return in 3 days.
2. If such books are found, the system sends an email to the borrower.
3. The email includes a reminder to either return or extend the rental of the book.
4. The notification status is tracked, and the success or failure of each notifi

## Technologies
The following technologies are used in the project:
- **Backend**: Spring Boot
- **Database**: H2 (testing), PostgreSQL (production and development)
- **Authentication and Security**: Spring Security, JWT
- **API Documentation**: Springdoc OpenAPI, Swagger
- **Testing**: JUnit 5, MockWebServer, Datafaker, Instancio
- **Email**: Spring Boot Mail for sending email notifications
- **Scheduled Tasks**: Spring Scheduler
- **Deployment**: Docker

## Project Structure
- **Controllers**: REST controllers for handling HTTP requests.
- **DTO**: Data Transfer Objects for passing data between application tiers.
- **Models**: Entities representing data in the database.
- **Repositories**: Interfaces for working with the database.
- **service**: services containing business logic, including a new `ScheduledService` for email notifications.
- **config**: configuration classes for setting up security, async tasks, and more.
- **mapper**: MapStruct mappers for converting between entities and DTOs.
- **exception**: exception handling and configuration.
- **handler**: global error handling for the application.

API documentation is available at http://localhost:9090/swagger-ui.html after running the application.
