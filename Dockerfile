FROM gradle:jdk21-graal-jammy

WORKDIR /app

COPY . .

RUN apt update -y
RUN apt install zlib1g-dev -y
RUN ./gradlew nativeCompile

CMD ["./gradlew", "nativeRun"]
