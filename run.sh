#!/usr/bin/env bash

export MAVEN_OPTS="-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8000"

mvn jetty::run
