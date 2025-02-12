FROM java:8
VOLUME ["/tmp","/opt/resource"]
ADD demo-1.0.0.jar app.jar
RUN bash -c 'touch /app.jar'
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo 'Asia/Shanghai' >/etc/timezone
EXPOSE 8002
ENTRYPOINT java -Duser.timezone=GMT+08 -Djava.security.egd=file:/dev/./urandom -jar /app.jar
