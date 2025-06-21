FROM openjdk:17-slim AS build
WORKDIR /workspace/app

COPY gradlew build.gradle settings.gradle ./
COPY gradle gradle
COPY src src

RUN chmod +x ./gradlew

RUN ./gradlew build -x test

FROM openjdk:17-slim
ENV TZ=Asia/Seoul
COPY --from=build /workspace/app/build/libs/*.jar yoajung-server.jar
ENTRYPOINT ["java","-jar","/yoajung-server.jar"]

