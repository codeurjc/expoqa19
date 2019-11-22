#!/bin/sh
while ! nc -z db 33306 ; do
    echo "Waiting for MySQL server to be available"
    sleep 2
done

exec java -jar target/webapp2.jar