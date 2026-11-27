#!/usr/bin/env bash
# Shared locations for the census scripts. Each variable can be set in the environment to override its default.
#
#   CENSUS_WORK     Workspace holding the census servers and checkouts:
#                     server-master/, server-mu/   headless GTNH servers (lwjgl3ify, java9args.txt, HorizonQA)
#                     src/<repo>/                  master checkouts of GT5-Unofficial and the fleet repos
#                     src-mu/GT5-Unofficial/       the GT5-Unofficial mu checkout census_cmp.sh builds
#                     logs/                        build and boot logs
#   CENSUS_RESULTS  Census dumps, gate reports and latest-mu.txt.
#   JAVA25          Java 25 executable the servers boot with.
#   JAVA21_HOME     JDK 21 home for repos whose build needs it (Roguelike-Dungeons).
#   GTNH_ROOT       Directory holding the mu fleet checkouts, whose build/libs jars swap_master_mods.sh removes
#                   from server-master.
CENSUS_HOME="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
: "${CENSUS_WORK:=$CENSUS_HOME/work}"
: "${CENSUS_RESULTS:=$CENSUS_HOME/results}"
: "${JAVA25:=java}"
: "${JAVA21_HOME:=}"
: "${GTNH_ROOT:=$(cd "$CENSUS_HOME/../../.." && pwd)}"
export CENSUS_HOME CENSUS_WORK CENSUS_RESULTS JAVA25 JAVA21_HOME GTNH_ROOT
mkdir -p "$CENSUS_WORK/logs" "$CENSUS_RESULTS"
