FROM openjdk:17-jdk-slim
VOLUME /tmp
ARG JAR_FILE=target/position-service-*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"] 
EXPOSE 9116 
