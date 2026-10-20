import collections, re, sys
import os, sys
HERE = os.path.dirname(os.path.abspath(__file__))
RESULTS = os.environ.get("CENSUS_RESULTS", os.path.join(HERE, "results"))
MU_NAME = sys.argv[1] if len(sys.argv) > 1 else open(os.path.join(RESULTS, "latest-mu.txt")).read().strip()
CENSUS_DIRS = (os.path.join(RESULTS, "census-master"), os.path.join(RESULTS, MU_NAME), os.path.join(RESULTS, "compare-" + MU_NAME))
exec(open(os.path.join(HERE, "compare.py")).read().split("report = []")[0].replace("sys.argv[1], sys.argv[2], sys.argv[3]", "CENSUS_DIRS"))
def assoc(items, t):
    i = items.get(t); return i["assoc"] if i else "?"
rows = collections.defaultdict(collections.Counter)
ex = collections.defaultdict(list)
cls = None
for line in open(os.path.join(OUT, "accept-diff.txt"), encoding="utf8"):
    if line.startswith("=="):
        cls = line[3:].rsplit(" (", 1)[0]; continue
    m, x, c, st = [p.strip() for p in line.split(" | ")]
    # association of raw input/candidate on mu (canonical '=name' keys carry the name)
    ax = x[1:] if x.startswith("=") else assoc(IU, x) if "mu" in cls or "changed" in cls else assoc(IM, x)
    ac = c[1:] if c.startswith("=") else assoc(IU, c) if "mu" in cls or "changed" in cls else assoc(IM, c)
    rel = "same-assoc" if ax == ac and ax not in ("-", "?") else "diff-assoc" if ax not in ("-","?") and ac not in ("-","?") else "no-assoc"
    key = (rel, re.match(r"[a-z]+", ax).group(0) if ax not in ("-","?") and re.match(r"[a-z]+", ax) else ax)
    rows[cls][key] += 1
    if len(ex[(cls, key)]) < 4: ex[(cls, key)].append(f"{m} | {x} | {c} | {st}")
for cls in rows:
    print("==", cls, sum(rows[cls].values()))
    for k, n in rows[cls].most_common(12):
        print("  ", n, k)
        for e in ex[(cls, k)][:2]: print("       ", e[:230])
