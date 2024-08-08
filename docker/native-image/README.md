# Native Image Build

To build and run a container with a native image, just run

```bash
# run only once to prepare the base image
docker build -t ghcr.io/graalvm/graalvm-ce:ol9-java17-maven-22.3.3 -f native-image/Dockerfile-BaseImage --no-cache .
# run to build the application image
docker build -t spring-petclinic-rest:3.3.1-native -f native-image/Dockerfile .
docker run --rm -p 8080:9966 -it spring-petclinic-rest:3.3.1-native
# open http://localhost:8080
```

This will run the native image build within a Docker container.

*Hint:* Be aware that, for a native image, there are some [hints](../../src/main/java/org/springframework/samples/petclinic/graalvm) necessary.

*Hint:* We could also use [Jib GraalVM Native Image Extension](https://github.com/GoogleContainerTools/jib-extensions/tree/master/first-party/jib-native-image-extension-maven)
to build a native image, but this would run the build on the host machine, which means we need
a host architecture that is equal to the runtime architecture.
