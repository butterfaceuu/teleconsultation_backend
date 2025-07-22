# Teleconsultation Backend

This is the backend for the Teleconsultation platform, built with Spring Boot and MySQL. It provides RESTful APIs for patient and practitioner registration, admin management, document upload, and secure authentication with email/SMS verification.

## Features

- Patient and Practitioner registration with document upload (BLOB storage)
- Admin management and authentication
- Email/SMS verification for registration and login
- OTP verification for account activation
- RESTful API endpoints for all major actions

## Technologies Used

- Java 17+
- Spring Boot
- MySQL
- JPA/Hibernate

## Getting Started

### 1. Clone the repository
```sh
git clone https://github.com/butterfaceuu/teleconsultation_backend.git
cd teleconsultation_backend
```

### 2. Configure the database
Edit `src/main/resources/application.properties` with your MySQL username and password.

### 3. Build and run the application
```sh
./mvnw spring-boot:run
```

### 4. API Endpoints
- Registration, login, and verification endpoints are available under `/api`.

## Project Structure
- `entities/` - JPA entities (Patient, Practitioner, Admin, VerificationCode)
- `repositories/` - Spring Data JPA repositories
- `controllers/` - REST controllers
- `services/` - Business logic and service classes
- `dtos/` - Data Transfer Objects

## Contributing
Feel free to fork this repository and submit pull requests.

## License
MIT 