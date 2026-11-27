"""Clusters a gate report's findings for triage. Usage: gate_clusters.py <gate report> <view> [top N]"""
import collections
import os
import re
import sys

report, view = sys.argv[1], sys.argv[2]
top = int(sys.argv[3]) if len(sys.argv) > 3 else 60
disp = collections.defaultdict(list)
for line in open(os.path.join(os.environ.get("CENSUS_RESULTS", os.path.join(os.path.dirname(os.path.abspath(__file__)), "results")), "census-master", "items.tsv"), encoding="utf8", errors="replace"):
    p = line.rstrip("\n").split("\t")
    if len(p) >= 6:
        disp["~" + "\t".join(p[5:])].append(p[0])
sec = None
counts, examples = collections.Counter(), {}
for line in open(report, encoding="utf8"):
    if line.startswith("== "):
        sec = line[3:].split(":")[0]
        continue
    if sec != view or not line.startswith("  "):
        continue
    name, key = line[2:].split("\t")[:2]
    if key.startswith("~"):
        group = disp.get(key, ["?"])[0].rsplit("@", 1)[0]
    elif key.startswith("="):
        group = "=" + re.match(r"=([a-z]*)", key).group(1)
    else:
        group = key.split(" ")[0].rsplit("@", 1)[0]
    if view == "lost-map-product":
        group = name + " " + group
    counts[group] += 1
    examples.setdefault(group, []).append(key[:60])
for group, n in counts.most_common(top):
    print(n, group, "|", examples[group][:4])
