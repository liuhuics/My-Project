#!/bin/bash
######################################################
# /home/deploys/business/business.jar 放置 jar
# /home/logs/start/business.log business模块启动的日志 
# /home/sh/business/pid.file	pid文件
# /home/sh/business.sh	sh文件
# /home/logs/business/	business模块相关日志
# workerId 不同的服务器设置不同的值
######### PARAM ######################################

JAVA_OPT=-Xmx1024m -DworkerId=3
JARDIR=/home/deploys/business
JARFILE=`ls -1r /home/deploys/business 2>/dev/null | head -n 1`
PID_FILE=businessPid.file
RUNNING=N
PWD=`pwd`
LOGFILE=/home/logs/start/business.out

######### DO NOT MODIFY ########

if [ -f $PID_FILE ]; then
        PID=`cat $PID_FILE`
        if [ ! -z "$PID" ] && kill -0 $PID 2>/dev/null; then
                RUNNING=Y
        fi
fi

start()
{
        if [ $RUNNING == "Y" ]; then
                echo "Application already started"
        else
                if [ -z "$JARFILE" ]
                then
                        echo "ERROR: jar file not found"
                else
                        nohup java  $JAVA_OPT -jar $JARDIR/$JARFILE > $LOGFILE 2>&1  &
                        echo $! > $PID_FILE
                        echo "Application $JARFILE starting..."
                        tail -f $LOGFILE
                fi
        fi
}

stop()
{
        if [ $RUNNING == "Y" ]; then
                kill -9 $PID
                rm -f $PID_FILE
                echo "Application stopped"
        else
                echo "Application not running"
        fi
}

restart()
{
        stop
        start
}

case "$1" in

        'start')
                start
                ;;

        'stop')
                stop
                ;;

        'restart')
                restart
                ;;

        *)
                echo "Usage: $0 {  start | stop | restart  }"
                exit 1
                ;;
esac
exit 0
