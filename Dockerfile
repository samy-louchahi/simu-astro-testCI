# Étape 1 : Construction du projet avec Gradle et JDK 21
FROM gradle:8.2.1-jdk21 AS builder
WORKDIR /home/gradle/project
COPY . .
RUN gradle clean build --no-daemon

# Étape 2 : Image finale avec OpenJDK 21 (version slim)
FROM openjdk:21-slim
WORKDIR /app
COPY --from=builder /home/gradle/project/build/libs/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
