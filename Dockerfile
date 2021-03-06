FROM maven:3.6.3-jdk-11-slim AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean compile package

FROM openjdk:11
COPY --from=build /usr/src/app/target/personmanager-1.0-SNAPSHOT.jar /usr/app/personmanager-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-cp","/usr/app/personmanager-1.0-SNAPSHOT.jar", "com.accela.CommandLineProcessor"]