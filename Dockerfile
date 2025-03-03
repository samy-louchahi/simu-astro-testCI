# Étape de build : Compilation de l'application avec Gradle
FROM openjdk:21-slim AS builder
WORKDIR /usr/src/app

# Copier l'ensemble du code source dans l'image
COPY . .

RUN ls -la

# Rendre le wrapper Gradle exécutable et compiler le projet
RUN chmod +x gradlew && ./gradlew clean build --no-daemon

# Étape finale : Création de l'image d'exécution
FROM openjdk:21-slim
WORKDIR /app

# Copier le fichier JAR généré depuis l'étape builder
COPY --from=builder /usr/src/app/build/libs/*.jar app.jar

# Exposer le port sur lequel l'application écoute
EXPOSE 8080

# Définir la commande de lancement de l'application
CMD ["java", "-jar", "app.jar"]
