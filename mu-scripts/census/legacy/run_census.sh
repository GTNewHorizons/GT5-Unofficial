#!/usr/bin/env bash
# Boots working/test-server headless with HorizonQA running only the recipe census dump.
# Usage: run_census.sh <label> <gregtech jar> <materiallib jar> <ProjRed jar>
# The three jars replace whatever gregtech/materiallib/ProjRed jars sit in the server's mods directory; the census
# lands in $CENSUS_RESULTS/<label>/. Exits non-zero when the boot wrote no fresh census.
set -uo pipefail
source "$(dirname "$0")/../env.sh"
SERVER="$CENSUS_WORK/test-server"
LABEL="$1"
OUT="$CENSUS_RESULTS/$LABEL"
LOG="$CENSUS_WORK/logs/$LABEL.log"
rm -rf "$OUT"
rm -f "$SERVER"/mods/gregtech-*.jar "$SERVER"/mods/materiallib-*.jar "$SERVER"/mods/ProjRed-*.jar
cp "$2" "$3" "$4" "$SERVER/mods/"
cd "$SERVER"
"$JAVA25" -Xms6G -Xmx6G -Dfml.readTimeout=180 \
    -Dfml.queryResult=confirm -Duser.language=en -Dhorizonqa.mode=ci -Dhorizonqa.tests=RecipeCensusDump \
    "-Dgt.recipeCensus=$(cygpath -w "$OUT")" \
    "-Dhorizonqa.reportDir=$(cygpath -w "$SERVER/horizonqa-reports")" \
    @java9args.txt -jar lwjgl3ify-forgePatches.jar nogui > "$LOG" 2>&1
echo "exit=$? files=$(ls "$OUT" 2>/dev/null | wc -l) lines=$(cat "$OUT"/*.txt 2>/dev/null | wc -l)"
[[ -f "$OUT/crafting.txt" ]]
