FROM gradle:jdk21 as build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle clean bootJar

FROM openjdk:21 as create_image

WORKDIR /app

COPY --from=build /home/gradle/src/build/libs/reachyourgoal-*.jar /app/app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]