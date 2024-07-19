# First stage: JDK with GraalVM
FROM ghcr.io/graalvm/graalvm-ce:ol9-java17-22.3.3 AS build

# Update package lists and Install Maven
#RUN microdnf update -y
#RUN microdnf install -y gcc glibc-devel zlib-devel libstdc++-devel gcc-c++
RUN microdnf install -y maven && microdnf clean all

WORKDIR /usr/src/app

# Copy pom.xml and download dependencies
COPY ../pom.xml .
#RUN mvn dependency:go-offline

COPY .. .

#RUN mvn -version
RUN mvn clean package -Pnative -Dspring.profiles.active=hsqldb,spring-data-jpa -DskipTests

#CMD ["/bin/bash"]

# Second stage: Lightweight debian-slim image
FROM debian:bookworm-slim

WORKDIR /app

# Copy the native binary from the build stage
COPY --from=build /usr/src/app/target/spring-petclinic-rest /app/spring-petclinic-rest

# Run the application
CMD ["/app/spring-petclinic-rest"]
