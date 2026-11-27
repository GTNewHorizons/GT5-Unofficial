#!/usr/bin/env bash
# Usage: boot.sh <master|mu> [label]
# Boots server-<side> headless running InterchangeCensus. The master census always lands in census-master (the
# gate baseline); a mu census lands in census-mu-<label>, label defaulting to a timestamp, and is never
# overwritten. latest-mu.txt names the most recent mu census.
source "$(dirname "$0")/env.sh"
S=$CENSUS_WORK/server-$1
LOG=$CENSUS_WORK/logs/boot-$1.log
if [ "$1" = master ]; then OUT=$CENSUS_RESULTS/census-master${2:+-$2}; else OUT=$CENSUS_RESULTS/census-mu-${2:-$(date +%Y%m%d-%H%M%S)}; fi
[ "$1" = mu ] && [ -e "$OUT" ] && { echo "refusing to overwrite $OUT"; exit 1; }
rm -rf "$OUT" "$S/CensusWorld"
cd "$S" || exit 1
"$JAVA25" -Xms8G -Xmx8G -Dfml.readTimeout=180 \
    -Dfml.queryResult=confirm -Duser.language=en -Dhorizonqa.mode=ci -Dhorizonqa.tests=InterchangeCensus \
    "-Dgt.recipeCensus=$(cygpath -w "$OUT")" "-Dhorizonqa.reportDir=$(cygpath -w "$S/horizonqa-reports")" \
    @java9args.txt -jar lwjgl3ify-forgePatches.jar nogui > "$LOG" 2>&1
echo "exit=$? files=$(ls "$OUT" 2>/dev/null | wc -l) dir=$(basename "$OUT")"; grep -h "INTERCHANGE" "$LOG"
[ "$1" = mu ] && basename "$OUT" > "$CENSUS_RESULTS/latest-mu.txt"
true
