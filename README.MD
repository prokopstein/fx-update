You need:
---------
1. java 8
2. docker desktop

Build user service:
-------------------
1. cd fx-user-service
2. ./gradlew clean build
3. docker build -t user-service:1.0.0 .

Build monitor service:
----------------------
1. cd fx-monitor-service
2. ./gradlew clean build
3. docker build -t monitor-service:1.0.0 .

Run RabbitMQ:
-------------
1.cd infrastructure/rabbitmq
2. docker-compose up

Run user service:
-----------------
1. cd fx-user-service
2. set up fields in .env file: EMAIL_PASSWORD & CLIENT_EMAIL
    - EMAIL_PASSWORD should be received confidentially
    - CLIENT_EMAIL your email where the rates updates will go
3. docker run --net=host --env-file .env user-service:1.0.0

Run monitor service (development):
--------------------
1. cd fx-monitor-service
2. docker run --net=host --env-file .env.dev monitor-service:1.0.0

Run monitor service (production):
--------------------
1. cd fx-monitor-service
2. set up field in .env.prd file: API_KEY
    - API_KEY should be received confidentially
3. docker run --net=host --env-file .env.prd monitor-service:1.0.0