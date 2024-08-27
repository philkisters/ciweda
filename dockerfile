# Stufe 1: Build mit Maven
FROM eclipse-temurin:21-jdk AS build

# Setze das Arbeitsverzeichnis
WORKDIR /app

# Kopiere die pom.xml und die Maven Wrapper-Dateien ins Arbeitsverzeichnis
COPY mvnw ./
COPY .mvn ./.mvn


# Kopiere die restlichen Projektdateien
COPY pom.xml ./
COPY src ./src

# Stelle sicher, dass mvnw ausführbar ist
RUN chmod +x mvnw

# Führe den Maven-Build aus, um die Anwendung zu bauen
RUN ./mvnw clean package -DskipTests

# Stufe 2: Laufzeitumgebung
FROM eclipse-temurin:21-jre-alpine

# Setze das Arbeitsverzeichnis
WORKDIR /app

# Kopiere das gebaute JAR-File von der ersten Stufe
COPY --from=build /app/target/*.jar app.jar
COPY ./.env .env

# Exponiere den Port 8080
EXPOSE 8080

# Starte die Java-Anwendung
ENTRYPOINT ["java", "-jar", "app.jar"]
