# Utilisation d'une image JDK légère
FROM quay.io/quarkus/ubi-quarkus-native-image:21.3-java11 as builder
WORKDIR /project
COPY . .
RUN ./gradlew build -Dquarkus.package.type=native

FROM registry.access.redhat.com/ubi8/ubi-minimal
WORKDIR /work/
COPY --from=builder /project/build/*-runner /work/application
EXPOSE 8080
CMD ["./application", "-Dquarkus.http.host=0.0.0.0"]
