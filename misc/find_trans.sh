#!/usr/bin/env bash
cd $(dirname $0)/../src/main
git grep -horE 'trans\( *\"[^"]*\" *\, *\"[^"]*\" *\)' -- *.java | sort -u