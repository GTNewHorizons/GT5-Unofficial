"""Diffs two recipe census directories written by gttests.census.RecipeCensusDump.

Usage: python diff_census.py <baseline dir> <fresh dir> [--samples N] [--out report.md]

Each census file holds one sorted line per recipe of one recipe map (crafting.txt for the crafting table).
The report lists, per map, how many lines only the baseline or only the fresh boot has, a few samples of
each, and the item identities that account for the difference (tokens present on one side only), which is
what an identity move such as materiallib:ingot@625 -> Avaritia:Resource@6 looks like.
"""
import collections
import os
import re
import sys

args = [a for a in sys.argv[1:] if not a.startswith("--")]
base_dir, fresh_dir = args[0], args[1]
samples = int(sys.argv[sys.argv.index("--samples") + 1]) if "--samples" in sys.argv else 6
out = sys.argv[sys.argv.index("--out") + 1] if "--out" in sys.argv else None
TOKEN = re.compile(r"[\w|.\-]+:[\w|.\-]+@\d+")


def load(path):
    if not os.path.isfile(path):
        return collections.Counter()
    with open(path, encoding="utf8") as f:
        return collections.Counter(line.rstrip("\n") for line in f)


report = []
total_removed = total_added = 0
names = sorted(set(os.listdir(base_dir)) | set(os.listdir(fresh_dir)))
for name in names:
    base, fresh = load(os.path.join(base_dir, name)), load(os.path.join(fresh_dir, name))
    removed, added = base - fresh, fresh - base
    if not removed and not added:
        continue
    n_removed, n_added = sum(removed.values()), sum(added.values())
    total_removed += n_removed
    total_added += n_added
    report.append("## %s: %d -> %d recipes, -%d +%d" % (name, sum(base.values()), sum(fresh.values()), n_removed, n_added))
    gone = collections.Counter(t for line in removed for t in TOKEN.findall(line))
    new = collections.Counter(t for line in added for t in TOKEN.findall(line))
    only_gone = [(t, c) for t, c in gone.most_common() if t not in new][:12]
    only_new = [(t, c) for t, c in new.most_common() if t not in gone][:12]
    if only_gone:
        report.append("identities only in removed lines: " + ", ".join("%s (%d)" % tc for tc in only_gone))
    if only_new:
        report.append("identities only in added lines: " + ", ".join("%s (%d)" % tc for tc in only_new))
    for line in list(removed)[:samples]:
        report.append("    - " + line[:400])
    for line in list(added)[:samples]:
        report.append("    + " + line[:400])
    report.append("")

header = "# census diff: %d maps differ, -%d +%d lines" % (sum(1 for r in report if r.startswith("## ")), total_removed, total_added)
text = "\n".join([header, ""] + report)
if out:
    with open(out, "w", encoding="utf8") as f:
        f.write(text + "\n")
print(header)
for r in report:
    if r.startswith("## "):
        print(r)
