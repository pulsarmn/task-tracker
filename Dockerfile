# Build Stage
FROM eclipse-temurin:25-jdk AS build

WORKDIR /app-build

COPY ./gradle ./gradle
COPY gradlew settings.gradle gradle.properties ./

RUN ./gradlew dependencies --no-daemon

COPY build.gradle ./
COPY src ./src

RUN ./gradlew bootJar -x test --no-daemon

# Execution Stage
FROM eclipse-temurin:25-jre-jammy AS execution

WORKDIR /app
COPY --from=build /app-build/build/libs/*.jar application.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "application.jar"]
CMD ["--spring.profiles.active=prod"]
