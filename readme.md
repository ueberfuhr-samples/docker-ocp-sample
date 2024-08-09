# Docker Container Images and OpenShift 

[![CI-Build to DockerHub](https://github.com/ueberfuhr-samples/docker-ocp-sample/actions/workflows/ci.yml/badge.svg)](https://github.com/ueberfuhr-samples/docker-ocp-sample/actions/workflows/ci.yml)

This project is a detached fork of [Spring's pet-clinic sample](https://github.com/spring-petclinic/spring-petclinic-rest).

## JVM Image Build

To build the container, just run

```bash
# build the application JAR
mvn clean package
# build the image
docker build -t spring-petclinic-rest:latest -f docker/Dockerfile .
# run the container
docker run --rm -p 8080:8080 -it spring-petclinic-rest:latest
# open http://localhost:8080
```

## Native Image Build

The native image build consists of 3 steps in 2 Dockerfiles:

1. Prepare the build container image. (only once, can be done manually) - see [`Dockerfile-BuilderImage`](docker/native-image/Dockerfile-BuilderImage)
2. Use the build container to build the native image of the application  - see [`Dockerfile`](docker/native-image/Dockerfile)
3. Build the runtime container image that executes the native image of the application - see [`Dockerfile`](docker/native-image/Dockerfile)

All together, we need the following commands:

```bash
# we need a repository and tag for the build container image, e.g.
export BUILDER_IMAGE=ghcr.io/graalvm/graalvm-ce:ol9-java17-maven-22.3.3
# step1: prepare the base image (only once)
docker build -t $BUILDER_IMAGE -f docker/native-image/Dockerfile-BuilderImage --no-cache .
# step2+3:  to build the application image
docker build --build-arg BUILDER_IMAGE -t spring-petclinic-rest:latest-native -f docker/native-image/Dockerfile .
# run the container
docker run --rm -p 8080:8080 -it spring-petclinic-rest:latest-native
# open http://localhost:8080
```

*Hint:* Be aware that, for a native image, there are some [hints](src/main/java/org/springframework/samples/petclinic/graalvm) necessary.

*Hint:* We could also use [Jib GraalVM Native Image Extension](https://github.com/GoogleContainerTools/jib-extensions/tree/master/first-party/jib-native-image-extension-maven)
to build a native image, but this would run the build on the host machine, which means we need
a host architecture that is equal to the runtime architecture.
