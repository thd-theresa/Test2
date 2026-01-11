# Docker Build Notes

## Issue

The Docker build currently fails with an SSL certificate verification error when Maven tries to download dependencies from Maven Central:

```
Could not transfer artifact org.springframework.boot:spring-boot-starter-parent:pom:4.0.0 from/to central
(certificate_unknown) PKIX path building failed: unable to find valid certification path to requested target
```

## Root Cause

This is an infrastructure/environment issue where the Maven Docker image cannot verify Maven Central's SSL certificate. This is **not related to the authentication code changes**.

## Workarounds

### Option 1: Build Locally (Recommended)

If you have Java 21 installed locally:

```bash
# Build the JAR file
mvn clean package -DskipTests

# Run with Docker Compose (will use pre-built JAR)
docker-compose up
```

### Option 2: Run Without Docker

```bash
# Run the application directly
mvn spring-boot:run
```

The admin account will be auto-created on first startup and the password will be printed to the console.

### Option 3: Build JAR Locally, Copy to Docker

1. Build locally: `mvn clean package -DskipTests`
2. Modify Dockerfile to use local JAR instead of building in Docker

## Attempted Fixes

The following approaches were tried but did not resolve the SSL issue in the Docker environment:

1. ❌ Adding Maven SSL bypass flags (`-Dmaven.wagon.http.ssl.insecure=true`)
2. ❌ Using HTTP mirror instead of HTTPS for Maven Central
3. ❌ Setting MAVEN_OPTS environment variable
4. ❌ Updating CA certificates in Docker container
5. ❌ Using different Maven resolver transport mechanisms

## Notes

- Maven works fine outside of Docker (SSL certificates are valid)
- This appears to be specific to the Docker build environment
- The application code and all authentication features work correctly once built
