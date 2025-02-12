FROM java:8
VOLUME ["/tmp","/opt/resource"]
ADD yudao-pay-1.0.0.jar app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 8033
ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -jar /app.jar --spring.profiles.active=$ACTIVE --eureka.username=root --eureka.password=yudao2019
