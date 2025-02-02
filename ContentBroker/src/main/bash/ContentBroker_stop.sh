#!/bin/bash


OPTION_F=${1:-nonforce}
MAX_TIME=7200
STEP=60

if [ "$OPTION_F" == "force" ]; then
	MAX_TIME=60 
	STEP=5	
	echo "'force' parameter in use -> wait time max $MAX_TIME sec."
fi

if [ "$OPTION_F" != "force"  -a "$OPTION_F" != "nonforce"  ]; then
	echo ""
	echo "Use 'force' parameter to enforce shutdown after 60 sec."
	echo ""	
fi



PID=`ps -aef | grep ContentBroker.jar | grep -v grep | awk '{print $2}'`

if [ "$PID" == "" ]; then
	echo "Application is not running"
else
	kill -15 $PID  # for graceful shutdown
	sleep 10
	CUR_TIME=0
	while [[ $CUR_TIME -lt $MAX_TIME ]]
	do
		if [  `ps --pid $PID | wc -l` -eq 1 ]; then
			break;
		fi
		echo "Wait for gracefully exiting of ContentBroker $CUR_TIME/$MAX_TIME s"
		sleep $STEP
		CUR_TIME=$((CUR_TIME +STEP))
	done
	
	if [  `ps --pid $PID | wc -l` -eq 2 ]; then
		kill -9 $PID
	fi
fi
rm -f /tmp/cb.running
