# syntax=docker/dockerfile:1

# ----------------------------------------------------------------------------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY pom.xml ./
COPY mvnw ./
COPY .mvn/ .mvn/
RUN chmod +x ./mvnw
# Warm the local Maven repository for better layer caching.
RUN ./mvnw dependency:go-offline -B -q || true
COPY src ./src
RUN ./mvnw clean package -DskipTests -B

# ----------------------------------------------------------------------------
# Stage 2 — Runtime
# Minimal JRE only. Runs as a non-root user. No shell, no build tools.
# ----------------------------------------------------------------------------
FROM eclipse-temurin:17-jre-jammy

# Run as an unprivileged user (defence in depth).
RUN groupadd -r appuser && useradd -r -g appuser -m appuser

WORKDIR /app

# Copy only the built artifact.
COPY --from=build /workspace/target/*.jar app.jar
RUN chown -R appuser:appuser /app

USER appuser

# Render injects the public $PORT; the app binds server.port=${PORT:8080}.
EXPOSE 8080

# JVM is tuned via JAVA_OPTS (set in render.yaml) so the container memory
# limit is respected instead of the host's physical RAM.
ENTRYPOINT ["java", "-jar", "app.jar"]
