FROM debian:12.12-slim

RUN apt update -y \
    && apt install openjdk-17-jre locales locales-all -y \
    && locale-gen ru_RU.UTF-8
ENV LANG=ru_RU.UTF-8
ENV LC_ALL=ru_RU.UTF-8


COPY target/diplom-3.5.7.jar /app/diplom-3.5.7.jar

WORKDIR /app

CMD ["java", "-jar", "diplom-3.5.7.jar"]
