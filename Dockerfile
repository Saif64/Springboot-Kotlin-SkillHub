# Stage 1: Build the application with Gradle
# Use a slim JDK image to keep the build stage smaller
FROM openjdk:17-jdk-slim AS build

WORKDIR /app

# Copy Gradle wrapper and build files first to leverage Docker layer caching
COPY gradlew .
COPY gradle gradle/
COPY build.gradle.kts .
COPY settings.gradle.kts .

# Grant execution permission to the Gradle wrapper
RUN chmod +x ./gradlew

# Download dependencies. This layer is cached as long as your build scripts don't change.
# --no-daemon is recommended for CI/CD environments and containers
RUN ./gradlew dependencies --no-daemon

# Copy the source code
COPY src ./src

# Build the application, create the executable JAR, and skip tests
RUN ./gradlew build --no-daemon -x test

# -----------------------------------------------------------------------------

# Stage 2: Create the final, lightweight runtime image
# Use a JRE (Java Runtime Environment) image as it's much smaller than a JDK
FROM openjdk:17-jre-slim

# Create a non-root user for security
RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring

WORKDIR /app

VOLUME /tmp

# Copy the executable JAR from the 'build' stage
# Gradle places artifacts in build/libs
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the port the application runs on
EXPOSE 8080

# The command to run the application
ENTRYPOINT ["java","-jar","/app.jar"]