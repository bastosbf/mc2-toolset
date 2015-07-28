#!/bin/bash

pid=`cat $CLI_HOME/cli.pid`

for p in $pid
do
    kill -9 $p
done

echo "SINAPAD CLI server stopped..."

