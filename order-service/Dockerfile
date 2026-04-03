# Use modern Java image
FROM eclipse-temurin:17-jdk

# Copy jar file
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Run app
ENTRYPOINT ["java","-jar","/app.jar"]