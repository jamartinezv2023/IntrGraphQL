FROM maven:3.8.1-openjdk-11 AS build
COPY pom.xml /app/
COPY src /app/src/
WORKDIR /app
RUN mvn clean package -DskipTests

FROM openjdk:11-jre-slim
COPY --from=build /app/target/virtualgr-0.0.1-SNAPSHOT.jar /usr/local/lib/virtualgr.jar
EXPOSE 8088
ENTRYPOINT ["java","-jar", "/usr/local/lib/virtualgr.jar"]