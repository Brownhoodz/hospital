FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY HospitalServer.java .

RUN javac HospitalServer.java

CMD ["java", "HospitalServer"]
