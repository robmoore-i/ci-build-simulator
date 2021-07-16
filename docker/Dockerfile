FROM openjdk:11
COPY . /ci-build-simulator
WORKDIR /ci-build-simulator
RUN apt update \
    && apt install git \
    && ./gradlew :sleeper:test
