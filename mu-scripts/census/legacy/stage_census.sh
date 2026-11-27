#!/usr/bin/env bash
# Builds a census jar from the GT5-Unofficial working tree, boots the test server with it and diffs the machine
# recipes against an earlier stage, remapping material indices through the world's transition files.
# Usage: stage_census.sh <label> <base label> [first transition version to apply, default 7]
set -uo pipefail
source "$(dirname "$0")/../env.sh"
L=$CENSUS_HOME/legacy
GT=${GT_DIR:-$(cd "$CENSUS_HOME/../.." && pwd)}
LOGS=$CENSUS_WORK/logs
LABEL="$1"; BASE="$2"; FROM="${3:-7}"
cd "$GT"
mkdir -p src/main/java/gttests/census
cp $L/RecipeCensusDump.java src/main/java/gttests/census/
VERSION=5.99.99-census ./gradlew --offline build -x test -x spotlessCheck -x spotlessJavaCheck > $LOGS/gt-build-census-$LABEL.log 2>&1
echo "census build: $(grep -E 'BUILD (SUCCESSFUL|FAILED)|error:' $LOGS/gt-build-census-$LABEL.log | head -3)"
if ! grep -q 'BUILD SUCCESSFUL' $LOGS/gt-build-census-$LABEL.log; then
    rm -rf src/main/java/gttests/census
    echo "census build failed; not booting a stale jar"
    exit 1
fi
J=$(ls -t build/libs/gregtech-*.jar | grep -Ev -- '-(dev|sources|api|javadoc|predowngrade|preshadow)\.jar$' | head -1)
cp "$J" $CENSUS_WORK/gregtech-$LABEL-census.jar
rm -rf src/main/java/gttests/census
./gradlew --stop >/dev/null 2>&1
ML=$(ls -t ../MaterialLib/build/libs/materiallib-*.jar | grep -Ev -- '-(dev|sources|predowngrade)\.jar$' | head -1)
PR=$(ls -t ../ProjectRed/build/libs/ProjRed-*.jar | grep -Ev -- '-(dev|sources)\.jar$' | head -1)
bash $L/run_census.sh "$LABEL" "$CENSUS_WORK/gregtech-$LABEL-census.jar" "$GT/$ML" "$GT/$PR"
grep -n "Caught exception from\|names no registered\|Caused by" $LOGS/$LABEL.log | head -8 | cut -c1-220
python $L/diff_stage.py "$BASE" "$LABEL" "$FROM"
