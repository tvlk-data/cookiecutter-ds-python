ARG WEBSERVER_CONTAINER=gcr.io/tvlk-data-mlplatform-prod/rm-webserver@sha256:6b85307f25e4ad5f4be7ae0ca833edcecb7821eb8a9f5963dbfe0daf1db682c4
FROM $WEBSERVER_CONTAINER

RUN mkdir -p flaskr/serving
COPY . ./flaskr/serving