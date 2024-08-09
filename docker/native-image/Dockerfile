# First stage: JDK with GraalVM
ARG BUILDER_IMAGE=ghcr.io/graalvm/graalvm-ce:ol9-java17-maven-22.3.3
FROM ${BUILDER_IMAGE} AS build
WORKDIR /usr/src/app
COPY . .
RUN mvn clean package -Pnative -Dspring.profiles.active=hsqldb,spring-data-jpa -DskipTests

# Second stage: Lightweight debian-slim image
FROM debian:bookworm-slim
WORKDIR /app
COPY --from=build /usr/src/app/target/app /app/app
EXPOSE 9966
ENV CONTEXT_ROOT=/
CMD ["/app/app"]
HEALTHCHECK --interval=5m --timeout=3s CMD curl -f http://localhost:9966/swagger-ui/index.html || exit 1
