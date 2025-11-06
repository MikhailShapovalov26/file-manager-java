FROM debian:12.12-slim AS build

RUN apt update -y \
    && apt install openjdk-17-jdk  tar gzip -y \
    && echo "export JAVA_HOME=$(dirname $(dirname $(readlink -f $(type -P java))))" > /etc/profile.d/javahome.sh

ENV PATH=$JAVA_HOME/bin:$PATH

WORKDIR /app

COPY . ./

RUN ./mvnw dependency:go-offline -B \
    && ./mvnw clean package -DskipTests -Ddockerfile.skip=true

FROM debian:12.12-slim

RUN apt update -y \
    && apt install openjdk-17-jre locales locales-all -y \
    && locale-gen ru_RU.UTF-8
ENV LANG=ru_RU.UTF-8
ENV LC_ALL=ru_RU.UTF-8


COPY --from=build /app/target/diplom-3.5.7.jar /app/diplom-3.5.7.jar

WORKDIR /app

CMD ["java", "-jar", "diplom-3.5.7.jar"]
