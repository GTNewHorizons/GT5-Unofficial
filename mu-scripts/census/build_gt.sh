#!/usr/bin/env bash
# Usage: build_gt.sh <master|mu>
# Builds the census GT jar (gregtech-5.99.99-census.jar) from the master or mu checkout with the probe copied in.
source "$(dirname "$0")/env.sh"
if [ "$1" = master ]; then D=$CENSUS_WORK/src/GT5-Unofficial; A=CensusAdapterMaster; else D=$CENSUS_WORK/src-mu/GT5-Unofficial; A=CensusAdapterMu; fi
LOG=$CENSUS_WORK/logs/GT-$1.log
mkdir -p $D/src/main/java/gttests/census
cp $CENSUS_HOME/probe/InterchangeCensus.java $D/src/main/java/gttests/census/
cp $CENSUS_HOME/probe/$A.java $D/src/main/java/gttests/census/CensusAdapter.java
cd "$D" || exit 1
git log -1 --format="built %H %s" > $LOG
./gradlew --stop > /dev/null 2>&1; VERSION=5.99.99-census ./gradlew -Dorg.gradle.internal.http.socketTimeout=60000 assemble >> $LOG 2>&1
echo "GT-$1: $(grep -E '^BUILD (SUCCESSFUL|FAILED)' $LOG | tail -1)"; grep -m5 'error:' $LOG
grep -q '^BUILD SUCCESSFUL' $LOG
