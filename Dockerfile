# Build stage
FROM maven:3.8.5-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .

RUN mvn dependency:go-offline



COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/template-0.0.1-SNAPSHOT.jar template.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","template.jar"]


