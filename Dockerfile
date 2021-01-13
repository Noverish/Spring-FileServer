FROM openjdk:8-jdk-alpine

RUN apk add --no-cache tzdata
ENV TZ='Asia/Seoul'

ENV USER=cicd \
    UID=1001

RUN adduser --disabled-password  --gecos "" --no-create-home --uid $UID $USER \
 && mkdir /app \
 && chown -R $USER /app

USER $USER

WORKDIR /app

COPY --chown=$USER build/libs/file*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]