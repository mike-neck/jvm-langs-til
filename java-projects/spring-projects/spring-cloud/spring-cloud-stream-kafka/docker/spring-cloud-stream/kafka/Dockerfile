FROM openjdk:8u111-jdk-alpine

ENV KAFKA_VERSION=0.10.1.0 SCALA_VERSION=2.11
ENV KAFKA_HOME /opt/kafka_${SCALA_VERSION}-${KAFKA_VERSION}

RUN apk --no-cache add curl bash supervisor && \
  mkdir opt && \
  curl -s -L http://ftp.riken.jp/net/apache/kafka/${KAFKA_VERSION}/kafka_${SCALA_VERSION}-${KAFKA_VERSION}.tgz | \
    tar -zxf - -C /opt && \
  mkdir -p /etc/supervisor.d && \
  apk --no-cache del curl

ADD supervisor/kafka.ini supervisor/zookeeper.ini /etc/supervisor.d/

EXPOSE 2181 9092

ENTRYPOINT ["supervisord", "-n"]
