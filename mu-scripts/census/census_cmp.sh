#!/usr/bin/env bash
# Usage: census_cmp.sh <commit> <label> <baseline-label>
# Builds the census GT jar at <commit>, boots it, gates it, and lists every census file that differs from the
# baseline census (file name and changed-line count).
source "$(dirname "$0")/env.sh"
cd "$CENSUS_WORK" || exit 1
git -C src-mu/GT5-Unofficial checkout -q --detach "$1" || exit 1
"$CENSUS_HOME/build_gt.sh" mu || exit 1
cp src-mu/GT5-Unofficial/build/libs/gregtech-5.99.99-census.jar server-mu/mods/gregtech-5.99.99-census.jar
"$CENSUS_HOME/boot.sh" mu "$2" > /dev/null
R=$CENSUS_RESULTS
[ -f "$R/census-mu-$2/crafting.txt" ] || { echo "NO CENSUS"; grep -m3 'Exception\|Error:' logs/boot-mu.log | cut -c1-200; exit 1; }
python "$CENSUS_HOME/gate.py" "census-mu-$2" > "$R/gate-stdout-$2.txt" 2>&1; echo "gate exit=$?"
grep -E 'unexplained' "$R/gate-stdout-$2.txt" | grep -v ' 0 unexplained'
for f in $(ls "$R/census-mu-$3"); do
    cmp -s "$R/census-mu-$3/$f" "$R/census-mu-$2/$f" && continue
    case "$f" in cropsnh.recipes.cropBreeder.txt|gt.recipe.fakeAssemblylineProcess.txt|accept.txt|shown.txt|items.tsv)
        echo "noisy $f $(diff "$R/census-mu-$3/$f" "$R/census-mu-$2/$f" | grep -c '^[<>]')"; continue;; esac
    python "$CENSUS_HOME/norm_cmp.py" "$R/census-mu-$3/$f" "$R/census-mu-$2/$f" | grep -v identical
done
grep -h 'shape consumers' logs/boot-mu.log | cut -c1-200
