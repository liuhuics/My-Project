#!/bin/bash
######################################################
# /home/deploys/admin/admin.jar 放置 jar
# /home/logs/start/admin.log admin模块启动的日志 
# /home/sh/admin/pid.file	pid文件
# /home/sh/admin.sh	sh文件
# /home/logs/admin/	admin模块相关日志

######### PARAM ######################################

JAVA_OPT=-Xmx1024m
JARDIR=/home/deploys/admin
JARFILE=`ls -1r /home/deploys/admin 2>/dev/null | head -n 1`
PID_FILE=adminPid.file
RUNNING=N
PWD=`pwd`
LOGFILE=/home/logs/start/admin.out

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
