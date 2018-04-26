#!/bin/bash

cd /opt/app

cp=$(find . -name "*.jar" -exec printf :{} ';')
if [[ -n "$CLASSPATH" ]]; then
    cp="$cp:$CLASSPATH"
fi

java -classpath "./classes:$cp" com.poc.cluster.Main

bash
