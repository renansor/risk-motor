#BUILD
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests -U
#RUN
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/risk-motor-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]