#Openjdk is not on docker hub any more.
FROM eclipse-temurin:21
COPY build/libs/myApp.jar .
ENTRYPOINT ["java", "-jar", "/myApp.jar"]
