# Étape 1 : Builder – Installer Gradle dans une image OpenJDK 21
FROM openjdk:21-slim AS builder

# Installer les dépendances nécessaires (wget, unzip)
RUN apt-get update && apt-get install -y wget unzip && rm -rf /var/lib/apt/lists/*

# Définir la version de Gradle à installer
ENV GRADLE_VERSION=8.2.1

# Télécharger et installer Gradle
RUN wget https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip \
    && unzip gradle-${GRADLE_VERSION}-bin.zip -d /opt \
    && rm gradle-${GRADLE_VERSION}-bin.zip

# Ajouter Gradle au PATH
ENV PATH="/opt/gradle-${GRADLE_VERSION}/bin:${PATH}"

# Définir le répertoire de travail et copier le code source
WORKDIR /project
COPY . .

# Construire le projet Gradle (ajustez les options si nécessaire)
RUN gradle clean build --no-daemon

# Étape 2 : Image finale – Utiliser OpenJDK 21 pour exécuter l'application
FROM openjdk:21-slim
WORKDIR /app

# Copier le JAR généré depuis l'étape builder
COPY --from=builder /project/build/libs/*.jar app.jar

# Exposer le port sur lequel l'application écoute
EXPOSE 8080

# Commande de lancement
CMD ["java", "-jar", "app.jar"]
