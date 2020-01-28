# Time Keeper project

This project uses Quarkus 1.1.1, the Supersonic Subatomic Java Framework.
If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

You can have a look at this project and use it as a simple tutorial, if you want to evaluate Quarkus.

# List of Quarkus features 



## For Lunatech employees only

A full documentation about this Lunatech project is avalaible on [Lunatech Confluence web site](https://lunatech.atlassian.net/wiki/spaces/INTRANET/pages/1609695253/Timekeeper)

## Database with Docker

First we will need a PostgreSQL database; you can launch one easily if you have Docker installed:

```
eval $(docker-machine env default)
```

then
```
docker run --ulimit memlock=-1:-1 -it --rm=true --memory-swappiness=0 --name timekeeper-db -e POSTGRES_USER=quarkus_test -e POSTGRES_PASSWORD=quarkus_test -e POSTGRES_DB=timekeeper -p 5434:5432 postgres:10.5
```

Alternatively you can setup a PostgreSQL instance in any another way.

The connection properties of the Agroal datasource are configured in the standard Quarkus configuration file, which you will find in
`src/main/resources/application.properties`.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
./mvnw quarkus:dev
```

## Packaging and running the application

The application is packageable using `./mvnw package`.
It produces the executable `timekeeper-1.0.0-SNAPSHOT-runner.jar` file in `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

The application is now runnable using `java -jar target/timekeeper-1.0.0-SNAPSHOT-runner.jar`.

## Creating a native executable

If GraalVM is configured on your server, you can create a native executable using: `./mvnw package -Pnative`.

Or you can use Docker to build the native executable using: `./mvnw package -Pnative -Dquarkus.native.container-build=true`.

You can then execute your binary: `./target/timekeeper-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/building-native-image-guide .


# Developement

List of documentations you should read : 

- [Qute template engine](https://quarkus.io/guides/qute) used to build HTML page with templating
- [RESTEasy documentation](https://docs.jboss.org/resteasy/docs/4.4.2.Final/userguide/html_single/index.html#Using_Path)


## Resources

We use Ionic for icons. See [the full documentation](https://ionicons.com/usage)
List of icons : [https://ionicons.com/](https://ionicons.com/)

# Tips

If you want to add a new Quarkus extension : 

> ./mvnw quarkus:add-extension -Dextensions="openapi"


# GraalVM on Mac OS

- Go to https://github.com/oracle/graal/releases 
- Download version 19.2 [here](https://github.com/oracle/graal/releases)
- unzip
- move the `graalvm-ce-19.2.1` folder to ` /Library/Java/JavaVirtualMachines`
- use `/usr/libexec/java_home -V` to show a list of JVM
- use `/usr/libexec/java_home -v 1.8.0_231` to set the JVM to GraalVM

You can also use `jenv` utility.
> jenv add /Library/Java/JavaVirtualMachines/graalvm-ce-19.2.1/Contents/Home

Check that Graal VM is configured 

```
> nicolas:quarkus-timekeeper-demo nmartignole$ jenv local 1.8.0.232
> nicolas:quarkus-timekeeper-demo nmartignole$ jenv local
1.8.0.232
> nicolas:quarkus-timekeeper-demo nmartignole$ java -version
openjdk version "1.8.0_232"
OpenJDK Runtime Environment (build 1.8.0_232-20191009173705.graal.jdk8u-src-tar-gz-b07)
OpenJDK 64-Bit GraalVM CE 19.2.1 (build 25.232-b07-jvmci-19.2-b03, mixed mode)

```

You should then be able to install 'native-image'

`gu install native-image`

Once i

See also this blog article [by Software Mill](https://blog.softwaremill.com/graalvm-installation-and-setup-on-macos-294dd1d23ca2)

