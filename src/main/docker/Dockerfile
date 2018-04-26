FROM openjdk:8u141-jre

ADD ./target/scala-2.12/*.jar /opt/app/
ADD ./lib_managed /opt/app
ADD ./src/main/resources/application_docker.conf /opt/app/classes/application.conf
ADD ./src/main/docker/run.sh /opt/app/run.sh

ENTRYPOINT ["/opt/app/run.sh"]
#ENTRYPOINT ["/bin/sleep",  "10000"]
