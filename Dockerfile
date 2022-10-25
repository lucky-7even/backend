FROM adoptopenjdk:11-jdk-hotspot

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} ./

EXPOSE 5000

ENTRYPOINT ["java", "-jar", "/app.jar"]