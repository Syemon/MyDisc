FROM openjdk:8-jdk-alpine
VOLUME /tmp
#RUN ./gradlew build
ARG JAR_FILE="MyDisc-0.0.1-SNAPSHOT.jar"
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]