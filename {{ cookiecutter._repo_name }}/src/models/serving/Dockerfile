ARG WEBSERVER_CONTAINER=gcr.io/tvlk-data-mlplatform-prod/rm-webserver:latest
FROM $WEBSERVER_CONTAINER

RUN mkdir -p flaskr/serving
COPY . ./flaskr/serving