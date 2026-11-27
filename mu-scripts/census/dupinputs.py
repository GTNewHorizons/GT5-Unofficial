import collections, re, os
import os, sys
HERE = os.path.dirname(os.path.abspath(__file__))
RESULTS = os.environ.get("CENSUS_RESULTS", os.path.join(HERE, "results"))
MU_NAME = sys.argv[1] if len(sys.argv) > 1 else open(os.path.join(RESULTS, "latest-mu.txt")).read().strip()
CENSUS_DIRS = (os.path.join(RESULTS, "census-master"), os.path.join(RESULTS, MU_NAME), os.path.join(RESULTS, "compare-" + MU_NAME))
exec(open(os.path.join(HERE, "compare.py")).read().split("report = []")[0].replace("sys.argv[1], sys.argv[2], sys.argv[3]", "CENSUS_DIRS"))
SPECIAL = {"crafting.txt", "furnace.txt", "accept.txt", "shown.txt", "items.tsv", "oredict.tsv"}
FIELD = re.compile(r"in=(\[.*?\]) fin=(\[.*?\]) out=(\[.*?\]) fout=(\[.*?\]) t=(\S+) eut=(\S+) sv=(\S+)")
res = {}
for side, d in (("M", M), ("U", U)):
    per = collections.Counter(); ex = collections.defaultdict(list)
    for f in sorted(set(os.listdir(d)) - SPECIAL):
        if "fake" in f or "fuel" in f.lower(): continue
        g = collections.defaultdict(list)
        for l in lines(d, f):
            p = FIELD.match(l)
            if p: g[(p.group(1), p.group(2))].append((p.group(3), p.group(5), p.group(6)))
        for k, v in g.items():
            if len(v) > 1 and len(set(v)) > 1:
                per[f] += 1
                if len(ex[f]) < 3: ex[f].append(norm_line(side, "in=%s fin=%s -> %s" % (k[0], k[1], sorted(set(v)))))
    res[side] = (per, ex)
pm, pu = res["M"][0], res["U"][0]
for f in sorted(set(pm) | set(pu), key=lambda f: -(pu.get(f, 0) - pm.get(f, 0))):
    if pu.get(f, 0) != pm.get(f, 0):
        print("%-40s master=%d mu=%d" % (f, pm.get(f, 0), pu.get(f, 0)))
        for e in res["U"][1].get(f, [])[:2]: print("      mu:", e[:260])
