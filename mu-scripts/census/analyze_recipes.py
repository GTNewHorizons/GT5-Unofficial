import collections, re, os
import os, sys
HERE = os.path.dirname(os.path.abspath(__file__))
RESULTS = os.environ.get("CENSUS_RESULTS", os.path.join(HERE, "results"))
MU_NAME = sys.argv[1] if len(sys.argv) > 1 else open(os.path.join(RESULTS, "latest-mu.txt")).read().strip()
CENSUS_DIRS = (os.path.join(RESULTS, "census-master"), os.path.join(RESULTS, MU_NAME), os.path.join(RESULTS, "compare-" + MU_NAME))
exec(open(os.path.join(HERE, "compare.py")).read().split("report = []")[0].replace("sys.argv[1], sys.argv[2], sys.argv[3]", "CENSUS_DIRS"))
SPECIAL = {"crafting.txt", "furnace.txt", "accept.txt", "shown.txt", "items.tsv", "oredict.tsv"}
maps = sorted((set(os.listdir(M)) | set(os.listdir(U))) - SPECIAL)
FIELD = re.compile(r"in=(\[.*?\]) fin=(\[.*?\]) out=(\[.*?\]) fout=(\[.*?\]) t=(\S+) eut=(\S+) sv=(\S+)")
def parse(l):
    m = FIELD.match(l)
    return m.groups() if m else None
summary = collections.Counter()
per_map = collections.defaultdict(collections.Counter)
examples = collections.defaultdict(list)
for f in maps:
    cm = collections.Counter(norm_line("M", l) for l in lines(M, f))
    cu = collections.Counter(norm_line("U", l) for l in lines(U, f))
    lost, new = cm - cu, cu - cm
    byin_l = collections.defaultdict(list); byin_n = collections.defaultdict(list)
    for l, c in lost.items():
        p = parse(l); byin_l[(p[0], p[1]) if p else l].extend([p or l] * c)
    for l, c in new.items():
        p = parse(l); byin_n[(p[0], p[1]) if p else l].extend([p or l] * c)
    for k in set(byin_l) | set(byin_n):
        a, b = byin_l.get(k, []), byin_n.get(k, [])
        paired = min(len(a), len(b))
        for i in range(paired):
            pa, pb = a[i], b[i]
            what = []
            if pa[2] != pb[2]: what.append("out")
            if pa[3] != pb[3]: what.append("fout")
            if pa[4] != pb[4]: what.append("time")
            if pa[5] != pb[5]: what.append("eut")
            if pa[6] != pb[6]: what.append("sv")
            cls = "changed:" + "+".join(what)
            per_map[f][cls] += 1
            if len(examples[(f, cls)]) < 5: examples[(f, cls)].append("in=%s fin=%s\n        master out=%s fout=%s t=%s eut=%s\n        mu     out=%s fout=%s t=%s eut=%s" % (pa[0][:200], pa[1][:80], pa[2][:200], pa[3][:80], pa[4], pa[5], pb[2][:200], pb[3][:80], pb[4], pb[5]))
        for x in a[paired:]:
            per_map[f]["removed"] += 1
            if len(examples[(f, "removed")]) < 6: examples[(f, "removed")].append(str(x)[:300])
        for x in b[paired:]:
            per_map[f]["added"] += 1
            if len(examples[(f, "added")]) < 6: examples[(f, "added")].append(str(x)[:300])
with open(os.path.join(OUT, "recipes-classified.txt"), "w", encoding="utf8") as fo:
    tot = collections.Counter()
    for f in sorted(per_map, key=lambda f: -sum(per_map[f].values())):
        fo.write("== %s %s\n" % (f, dict(per_map[f])))
        tot.update(per_map[f])
        for cls in per_map[f]:
            for e in examples[(f, cls)]: fo.write("   [%s] %s\n" % (cls, e))
    print(dict(tot))
for f in sorted(per_map, key=lambda f: -sum(per_map[f].values()))[:30]: print(f, dict(per_map[f]))
