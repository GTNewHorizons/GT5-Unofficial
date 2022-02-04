#!/usr/bin/env bash
cd $(dirname $0)/../src/main
git grep -horE 'GT_Utility.trans\( *\"[^"]*\" *\, *\"[^"]*\" *\)' -- *.java | sort -u