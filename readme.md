# Docker Container Images and OpenShift 

[![CI-Build to DockerHub](https://github.com/ueberfuhr-samples/docker-ocp-sample/actions/workflows/ci.yml/badge.svg)](https://github.com/ueberfuhr-samples/docker-ocp-sample/actions/workflows/ci.yml)
[![Native Image Build to DockerHub](https://github.com/ueberfuhr-samples/docker-ocp-sample/actions/workflows/native-image.yml/badge.svg)](https://github.com/ueberfuhr-samples/docker-ocp-sample/actions/workflows/native-image.yml)

This project is a detached fork of [Spring's pet-clinic sample](https://github.com/spring-petclinic/spring-petclinic-rest).

## Run the app in the pre-built container images

We can find the pre-built container images on DockerHub:
- [Application Container](https://hub.docker.com/repository/docker/ralfueberfuhr/spring-petclinic-rest).
- [Database Container](https://hub.docker.com/repository/docker/ralfueberfuhr/spring-petclinic-db).

We just have to pull them and run a container.

```bash
# we could also use ralfueberfuhr/spring-petclinic-rest:latest-native which runs faster
docker pull ralfueberfuhr/spring-petclinic-rest:latest
docker run --rm -p 8080:8080 ralfueberfuhr/spring-petclinic-rest:latest
# open http://localhost:8080/pet-clinic
```

## Build container locally

### JVM Image Build

To build the container, we have to run the Maven and the Docker build:

```bash
# build the application JAR
mvn clean package
# build the image
docker build -t spring-petclinic-rest:latest -f docker/Dockerfile .
# run the container
docker run --rm -p 8080:8080 spring-petclinic-rest:latest
# open http://localhost:8080/pet-clinic
```

### Native Image Build

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
docker run --rm -p 8080:8080 spring-petclinic-rest:latest-native
# open http://localhost:8080/pet-clinic
```

*Hint:* We need to be aware that, for a native image, there are some [hints](src/main/java/org/springframework/samples/petclinic/graalvm) necessary.

*Hint:* We could also use [Jib GraalVM Native Image Extension](https://github.com/GoogleContainerTools/jib-extensions/tree/master/first-party/jib-native-image-extension-maven)
to build a native image, but this would run the build on the host machine, which means we need
a host architecture that is equal to the runtime architecture.

## Deploy to K8s / OpenShift

To deploy Kubernetes resources manually, see the instructions [here](openshift/app/README.md).

Alternatively, we can use [Helm](helm/README.md), e.g.
```bash
# install into OpenShift Local Container (CRC) - see https://github.com/crc-org/crc
helm install petclinic oci://registry-1.docker.io/ralfueberfuhr/spring-petclinic-oci --values=ENV_LOCAL_CRC.yaml -n "<openshift-project>"
```

## ArgoCD

> [!WARNING]
> ArgoCD runs slowly in the local CRC on a developer's machine. So it might be easier to switch to
> Minikube. We can find details [here](argocd-minikube/README.md).

To install ArgoCD into the project, we need to follow the steps described in [this tutorial](https://docs.openshift.com/container-platform/4.10/cicd/gitops/setting-up-argocd-instance.html).
The ArgoCD UI is then available under `https://argocd-server-<openshift-project>.apps-crc.testing/`.

> [!IMPORTANT]
> The ArgoCD containers request disk-size and memory that the OpenShift node does not provide by default.
> We need to increase disk-size and memory to get the pods form ArgoCD start successfully:

```bash
# default: 10752 (MB)
crc config set memory 16128
# default: 31 (GB)
crc config set disk-size 62
```

## Terraform

We could also use Terraform. We can find details [here](terraform/README.md).
