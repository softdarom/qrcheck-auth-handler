FROM openjdk:11.0.10-jdk

ARG APP_HOME=/opt/auth-handler
ARG APP_JAR=auth-handler.jar

ENV TZ=Europe/Moscow \
    HOME=$APP_HOME \
    JAR=$APP_JAR

WORKDIR $HOME
COPY build/libs/auth-handler.jar $HOME/$JAR
ENTRYPOINT java $JAVA_OPTS -jar $JAR