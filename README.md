# wawsup
AWS Opinionated Spring Boot Scaffolding Template

## Getting Started - Running Locally
```
mvn clean compile
mvn spring-boot: run

// Test if API running
http://localhost:8080/
http://localhost:8080/ping

// Full API Documented in Swagger/OpenAPI
http://localhost:8080/swagger-ui.html
```
#### Environments
Environment profiles have been setup. To use the development environment (application-dev.yml)
```
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

