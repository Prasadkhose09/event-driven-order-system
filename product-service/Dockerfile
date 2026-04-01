# Use lightweight Java image
FROM openjdk:17-jdk-slim

# Copy jar file
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Run application
ENTRYPOINT ["java","-jar","/app.jar"]