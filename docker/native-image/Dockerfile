# First stage: JDK with GraalVM
ARG BUILDER_IMAGE=ghcr.io/graalvm/graalvm-ce:ol9-java17-maven-22.3.3
FROM ${BUILDER_IMAGE} AS build
WORKDIR /usr/src/app
COPY . .
CMD ["/bin/bash"]
RUN mvn clean package -Pnative -Dspring.profiles.active=hsqldb,spring-data-jpa -DskipTests

# Second stage: Lightweight debian-slim image
FROM debian:bookworm-slim
WORKDIR /app
COPY --from=build /usr/src/app/target/spring-petclinic-rest /app/spring-petclinic-rest
EXPOSE 8080
ENV CONTEXT_ROOT=/
CMD ["/app/spring-petclinic-rest"]
HEALTHCHECK --interval=5m --timeout=3s CMD curl -f http://localhost:8080/swagger-ui/index.html || exit 1

