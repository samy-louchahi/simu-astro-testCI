# Étape 1 : Construction du projet avec Gradle et JDK 21
FROM gradle:8.2.1-jdk21 AS builder
WORKDIR /home/gradle/project
# Copier l'intégralité du contexte du dépôt dans le conteneur
COPY . .
# Lancer la compilation du projet avec Gradle
RUN gradle clean build --no-daemon

# Étape 2 : Image finale avec OpenJDK 21
FROM openjdk:21-jdk-alpine
WORKDIR /app
# Copier le JAR généré dans l'étape précédente (vérifiez le chemin du JAR selon votre projet)
COPY --from=builder /home/gradle/project/build/libs/*.jar app.jar
# Exposer le port sur lequel votre application écoute (à adapter si nécessaire)
EXPOSE 8080
# Commande de lancement de l'application
CMD ["java", "-jar", "app.jar"]
