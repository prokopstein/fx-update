FROM adoptopenjdk/openjdk11:alpine-slim

COPY ./build/libs/fx-monitor-service.jar /app/

EXPOSE 2225

CMD java -jar /app/fx-monitor-service.jar