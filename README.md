# wawsup
AWS Opinionated Spring Boot Scaffolding Template

## Getting Started - Running Locally
```aidl
mvn clean compile
mvn spring-boot: run
```
Environment profiles have been setup. To use the development environment (application-dev.yml)
```aidl
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```