# First stage: JDK with GraalVM
FROM ghcr.io/graalvm/graalvm-ce:ol9-java17-maven-22.3.3 AS build
WORKDIR /usr/src/app
COPY .. .
RUN mvn clean package -Pnative -Dspring.profiles.active=hsqldb,spring-data-jpa -DskipTests

# Second stage: Lightweight debian-slim image
FROM debian:bookworm-slim
WORKDIR /app
COPY --from=build /usr/src/app/target/spring-petclinic-rest /app/spring-petclinic-rest
EXPOSE 9966
ENV CONTEXT_ROOT=/
CMD ["/app/spring-petclinic-rest"]
