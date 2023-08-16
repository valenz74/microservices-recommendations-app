FROM eclipse-temurin:17-jdk-alpine
COPY recomendations*.jar app.jar
ENTRYPOINT [ "java", "-jar", "/app.jar" ]
