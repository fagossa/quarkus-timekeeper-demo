####
# This is a Dockerfile to deploy this application to Qovery https://www.qovery.com/
#
# Execute this file with
# docker build -t quarkus/timekeeper-jvm -f Dockerfile .
#
# Quarkus 1.2.0 final is required - It is normally set in pom.xml
#########################################################################################################

## Stage 1 : build with maven builder image with native capabilities
# see https://github.com/quarkusio/quarkus-images
# see also https://quay.io/repository/quarkus/centos-quarkus-maven?tab=tags
# Thanks to Loïc Mathieu from Zenika https://blog.zenika.com/2019/04/23/zoom-sur-quarkus/
# I updated its multi-stage build
FROM quay.io/quarkus/centos-quarkus-maven:19.2.1 AS build
COPY src /project
COPY pom.xml /project
# Please note that you need a large amount of memory here => check your Docker machine settings if you compile from Mac OS
# If you get an exit error with 137 : OutOfMemory then change your docker machine settings from the desktop app
RUN mvn -e -f /project/pom.xml -DskipTests -Pnative clean package

## Stage 2 : create the docker final image form a distroless image !
FROM cescoffier/native-base
# we copy from the previous build the artifacts
COPY --from=build /project/target/*-runner /application
EXPOSE 8080
ENTRYPOINT ["./application", "-Dquarkus.http.host=0.0.0.0" , "-Dquarkus.log.level=FINEST", "-Dquarkus.log.console.level=FINEST", "-Dquarkus.datasource.url=${QOVERY_DATABASE_MY_POSTGRESQL_6132005_URI}"]
