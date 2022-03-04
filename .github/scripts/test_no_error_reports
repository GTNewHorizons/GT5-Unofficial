#!/usr/bin/env bash

# bashsupport disable=BP5006 # Global environment variables
RUNDIR="run" \
  CRASH="crash-reports" \
  SERVERLOG="server.log"

# enable nullglob to get 0 results when no match rather than the pattern
shopt -s nullglob

# store matches in array
crash_reports=("$RUNDIR/$CRASH/crash"*.txt)

# if array not empty there are crash_reports
if [ "${#crash_reports[@]}" -gt 0 ]; then
  # get the latest crash_report from array
  latest_crash_report="${crash_reports[-1]}"
  {
    printf 'Latest crash report detected %s:\n' "${latest_crash_report##*/}"
    cat "$latest_crash_report"
  } >&2
  exit 1
fi

if grep --quiet --fixed-strings 'Fatal errors were detected' "$SERVERLOG"; then
  {
    printf 'Fatal errors detected:\n'
    cat server.log
  } >&2
  exit 1
fi

if grep --quiet --fixed-strings 'The state engine was in incorrect state ERRORED and forced into state SERVER_STOPPED' \
  "$SERVERLOG"; then
  {
    printf 'Server force stopped:'
    cat server.log
  } >&2
  exit 1
fi

if ! grep --quiet --perl-regexp --only-matching '.+Done \(.+\)\! For help, type "help" or "\?"' "$SERVERLOG"; then
  {
    printf 'Server did not finish startup:'
    cat server.log
  } >&2
  exit 1
fi

printf 'No crash reports detected'
exit 0
