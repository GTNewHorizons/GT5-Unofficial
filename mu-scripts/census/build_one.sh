#!/usr/bin/env bash
# Usage: build_one.sh <repo>
# Builds the master checkout of one fleet repo for server-master.
source "$(dirname "$0")/env.sh"
r="$1"; cd "$CENSUS_WORK/src/$r" || exit 1
LOG=$CENSUS_WORK/logs/$r.log
extra=""; [ "$r" = Roguelike-Dungeons ] && [ -n "$JAVA21_HOME" ] && extra="-Dorg.gradle.java.home=$JAVA21_HOME"
./gradlew $extra -Dorg.gradle.internal.http.socketTimeout=60000 assemble > $LOG 2>&1
echo "$r: $(grep -E '^BUILD (SUCCESSFUL|FAILED)' $LOG | tail -1)"
