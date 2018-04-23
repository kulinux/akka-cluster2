FROM openjdk:8u141-jre

ADD ./target/scala-2.12/*.jar /opt/app/
ADD ./lib_managed /opt/app
ADD ./src/main/docker/run.sh /opt/app/bin/run.sh

ENTRYPOINT ["/opt/app/bin/run.sh"]