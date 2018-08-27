#!/bin/sh
export CROWDIN_API_KEY=`cat ~/.appconfig/eu.thedarken.wldonate/crowdin.key`
alias crowdin-cli='java -jar /usr/local/bin/crowdin-cli.jar'
crowdin-cli "$@"