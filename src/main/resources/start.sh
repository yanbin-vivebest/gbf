#!/bin/sh

JAR_NAME=open-aml-web-1.0-SNAPSHOT.jar
APP_DIR=`pwd`
JAVA_MEM_ORGS="-Xmx100M -XX:MaxPermSize=100M"

export SPRING_PROFILES_ACTIVE=$2
echo \$SPRING_PROFILES_ACTIVE=$SPRING_PROFILES_ACTIVE
echo \$APP_DIR=$APP_DIR

if [ -z "$2" ]; then
    echo "ERROR:SPRING_PROFILES_ACTIVE is not set ，please pass parameter \$2 !"
    exit 1
fi

start(){
    PID=`ps -ef|grep $JAR_NAME|grep -v grep|awk '{print $2}'`
    if [ -n "$PID" ]; then
        echo "$JAR_NAME is running !pid is $PID"
    else
        cd $APP_DIR
        nohup java -jar $JAR_NAME --logging.config=config/log4j2.xml --spring.profiles.active=$SPRING_PROFILES_ACTIVE >/dev/null &
        if [ $? -eq 0 ]; then
            echo "$JAR_NAME is started success !"
        else
            echo "$JAR_NAME is started failed !"
        fi
    fi
}

stop(){
   pkill -f $JAR_NAME
   PID=`ps -ef|grep $JAR_NAME|grep -v grep|awk '{print $2}'`
   kill -9 $PID
   echo "$JAR_NAME is stopped !"
}

case "$1" in
    start)
        start
    ;;
    stop)
        stop
    ;;
    restart)
        stop
        start
    ;;
*)
esac
exit $?