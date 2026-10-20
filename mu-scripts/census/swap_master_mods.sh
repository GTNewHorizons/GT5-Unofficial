#!/usr/bin/env bash
# Replaces the mu fleet jars in server-master/mods with the master checkout builds of the repos in repos.txt.
source "$(dirname "$0")/env.sh"
S=$CENSUS_WORK/server-master/mods
REMOVED=$CENSUS_WORK/removed-mu-jars
newest() { ls -t "$1"/build/libs/*.jar 2>/dev/null | grep -Ev -- '-(dev|sources|api|javadoc|predowngrade|preshadow)\.jar$' | head -1; }
mkdir -p "$REMOVED"
for r in $(cat "$CENSUS_HOME/repos.txt"); do
  for j in $(ls "$GTNH_ROOT/$r/build/libs" 2>/dev/null); do [ -f "$S/$j" ] && mv "$S/$j" "$REMOVED/" && echo "removed $j"; done
  n=$(newest "$CENSUS_WORK/src/$r"); [ -n "$n" ] && cp "$n" "$S/" && echo "added $(basename "$n")" || echo "MISSING master jar for $r"
done
mv "$S"/materiallib-*.jar "$S"/gregtech-*.jar "$REMOVED/" 2>/dev/null
n=$(newest "$CENSUS_WORK/src/GT5-Unofficial"); cp "$n" "$S/" && echo "added $(basename "$n")"
