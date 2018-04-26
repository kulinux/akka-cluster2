sbt publishLocal
docker build . -f src/main/docker/Dockerfile -t pako/akka-cluster

echo ' docker run -i -t pako/akka-cluster'
