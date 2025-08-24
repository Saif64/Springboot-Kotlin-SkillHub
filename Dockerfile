# Stage 1: Build the application using Gradle
FROM gradle:8.7-jdk17 AS build
WORKDIR /app

# Copy Gradle build files
COPY build.gradle.kts settings.gradle.kts ./
COPY gradlew ./
COPY gradle gradle

# Copy source code
COPY src src

# Give permission and build
RUN chmod +x ./gradlew
RUN ./gradlew clean build -x test

# Stage 2: Create the final runtime image
FROM openjdk:17-jdk
WORKDIR /app
VOLUME /tmp

# Copy the fat JAR from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
