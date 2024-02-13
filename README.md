# Key value pairs API

An API for editing key value pairs.

Technologies used:
* [ktor](https://github.com/ktorio/ktor) - Kotlin web application framework

## Local development

IntelliJ is the recommended IDE for Ktor development. If you don't have IntelliJ available, you can build and run the project using the following commands:

```bash
./gradlew installDist
cd build/install/com.example.key-value-api/bin
./com.example.key-value-api
```

The API server will be available at http://localhost:8080/
