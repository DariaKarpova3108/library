FROM eclipse-temurin:21-jdk

ARG GRADLE_VERSION=8.7

RUN apt-get update && apt-get install -yq make unzip

WORKDIR .

COPY . .

RUN ./gradlew installDist

CMD ["java", "-jar", "build/libs/my-app-0.0.1-SNAPSHOT.jar"]

EXPOSE 8080

