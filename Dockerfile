FROM java:8
USER root
RUN echo "deb [check-valid-until=no] http://archive.debian.org/debian jessie-backports main" > /etc/apt/sources.list.d/jessie-backports.list \
    && sed -i '/deb http:\/\/deb.debian.org\/debian jessie-updates main/d' /etc/apt/sources.list \
    && apt-get -o Acquire::Check-Valid-Until=false update \
    && apt install -y ffmpeg \
    && mkdir /data \
    && mkdir /data/files \
    && mkdir /root/OneDrive \
    && mkdir /root/OneDrive/data

COPY target/springMybatis-0.0.1-SNAPSHOT.jar /opt/service-jar/
WORKDIR /opt/service-jar
CMD java -jar springMybatis-0.0.1-SNAPSHOT.jar