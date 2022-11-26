FROM openjdk:8
USER root
RUN  apt-get -o Acquire::Check-Valid-Until=false update \
    && apt install -y ffmpeg \
    && mkdir /data \
    && mkdir /data/files \
    && mkdir /root/OneDrive \
    && mkdir /root/OneDrive/data

COPY target/MyCloud-1.0.jar /opt/service-jar/
WORKDIR /opt/service-jar

VOLUME ["/data","/root/OneDrive/data"]

EXPOSE 8081

CMD java -jar MyCloud-1.0.jar