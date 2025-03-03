FROM quay.io/quarkus/ubi-quarkus-native-image:21.3-java11 as builder
WORKDIR /project
COPY . .
RUN chmod +x gradlew
RUN ./gradlew build -Dquarkus.package.type=native
