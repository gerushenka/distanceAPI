FROM openjdk:17-alpine
WORKDIR /app
ENV PORT 8080
EXPOSE 8080
COPY build/libs/*.jar app.jar
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar

