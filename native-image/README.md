# Native Image Build

To build a container with a native image, just run

```bash
docker build -t spring-petclinic-rest:3.3.1-native -f native-image/Dockerfile .
```

This will run the native image build within a Docker container.

*Hint:* Be aware that, for a native image, there are some [hints](../src/main/java/org/springframework/samples/petclinic/graalvm) necessary.

*Hint:* We could also use [Jib GraalVM Native Image Extension](https://github.com/GoogleContainerTools/jib-extensions/tree/master/first-party/jib-native-image-extension-maven)
to build a native image, but this would run the build on the host machine, which means we need
a host architecture that is equal to the runtime architecture.
