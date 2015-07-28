#!/bin/bash

rmiregistry -J-classpath -J$CLI_HOME/mc2-cli.jar &
echo $! > $CLI_HOME/cli.pid
java -Djava.security.policy=$CLI_HOME/cli.policy -cp $CLI_HOME/mc2-cli.jar br.lncc.sinapad.cli.server.Server >> $CLI_HOME/cli.log 2> $CLI_HOME/cli.log &
echo $! >> $CLI_HOME/cli.pid
echo "SINAPAD CLI server started..."
