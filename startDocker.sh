#!/bin/bash
export DOCKER_HOST=tcp://192.168.59.103:2376
    export DOCKER_CERT_PATH=/Users/Diana/.boot2docker/certs/boot2docker-vm
    export DOCKER_TLS_VERIFY=1
docker run -d -p 9200:9200 -p 9300:9300 -v /Users/Diana/programacion/ejemplos/gradleDocker/data-dir:/data dockerfile/elasticsearch /elasticsearch/bin/elasticsearch -Des.config=/data/elasticsearch.yml
