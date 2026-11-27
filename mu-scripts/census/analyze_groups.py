"""Per (map, input association) acceptance sets, master vs mu."""
import collections, re
import os, sys
HERE = os.path.dirname(os.path.abspath(__file__))
RESULTS = os.environ.get("CENSUS_RESULTS", os.path.join(HERE, "results"))
MU_NAME = sys.argv[1] if len(sys.argv) > 1 else open(os.path.join(RESULTS, "latest-mu.txt")).read().strip()
CENSUS_DIRS = (os.path.join(RESULTS, "census-master"), os.path.join(RESULTS, MU_NAME), os.path.join(RESULTS, "compare-" + MU_NAME))
exec(open(os.path.join(HERE, "compare.py")).read().split("report = []")[0].replace("sys.argv[1], sys.argv[2], sys.argv[3]", "CENSUS_DIRS"))

def group_of(items, side, x):
    i = items.get(x)
    if i and i["assoc"] != "-": return "=" + i["assoc"]
    return canon(side, x)

def load(d, side, items):
    g = collections.defaultdict(lambda: collections.defaultdict(lambda: [0, 0, 0]))
    inputs = collections.defaultdict(set)
    for line in lines(d, "accept.txt"):
        m, x, c, *rest = line.split("\t")
        n, L, C, O = (int(p.split("=")[1]) for p in rest[:4])
        key = (m, group_of(items, side, x))
        inputs[key].add(canon(side, x))
        a = g[key][canon(side, c)]
        a[0] += n; a[1] += L; a[2] += C
    return g, inputs

GM, XM = load(M, "M", IM)
GU, XU = load(U, "U", IU)
exists_m = {canon("M", t) for t in IM}
exists_u = {canon("U", t) for t in IU}

def st(a):
    if a is None: return None
    n, L, C = a
    return "all" if C == n and L == n else "none" if C == 0 and L == 0 else "part(L%d C%d /%d)" % (L, C, n)

out = collections.Counter(); ex = collections.defaultdict(list)
for key in sorted(set(GM) | set(GU)):
    m, grp = key
    if key not in GM or key not in GU:
        out[("group only on " + ("master" if key in GM else "mu"),)] += 1
        ex[("group only on " + ("master" if key in GM else "mu"),)].append(f"{m} {grp} inputs={sorted(XM.get(key) or XU.get(key))[:3]}")
        continue
    am, au = GM[key], GU[key]
    # the inputs themselves count as accepted
    accm = {c for c, a in am.items() if st(a) == "all"} | (XM[key] - set(am))
    accu = {c for c, a in au.items() if st(a) == "all"} | (XU[key] - set(au))
    for c in set(am) & set(au):
        a, b = st(am[c]), st(au[c])
        if a != b and "all" not in (a, b) and not (a.startswith("part") and b.startswith("part")):
            k = ("partial changed", "%s -> %s" % (a.split("(")[0], b.split("(")[0]), re.match(r"=?([a-z]+)", grp).group(1) if re.match(r"=?[a-z]+", grp) else grp)
            out[k] += 1; ex[k].append(f"{m} {grp}: {c} master={a} mu={b}")
    for c in accm - accu:
        why = "mu rejects" if c in au or c in XU[key] else ("not in mu pack" if c not in exists_u else "not a candidate on mu")
        k = ("lost", why, re.match(r"=?([a-z]+)", grp).group(1) if re.match(r"=?[a-z]+", grp) else grp)
        out[k] += 1; ex[k].append(f"{m} {grp}: {c} master={st(am.get(c))} mu={st(au.get(c))}")
    for c in accu - accm:
        why = "master rejected" if c in am or c in XM[key] else ("not in master pack" if c not in exists_m else "not a candidate on master")
        k = ("gained", why, re.match(r"=?([a-z]+)", grp).group(1) if re.match(r"=?[a-z]+", grp) else grp)
        out[k] += 1; ex[k].append(f"{m} {grp}: {c} master={st(am.get(c))} mu={st(au.get(c))}")
with open(os.path.join(OUT, "accept-groups.txt"), "w", encoding="utf8") as fo:
    for k, n in sorted(out.items(), key=lambda kv: (kv[0][0], -kv[1])):
        fo.write("%6d %s\n" % (n, " / ".join(k)))
        for e in sorted(ex[k])[:6]: fo.write("         %s\n" % e[:260])
    fo.write("\n\nFULL\n")
    for k in sorted(ex):
        fo.write("== %s\n" % " / ".join(k))
        for e in sorted(ex[k]): fo.write("   %s\n" % e[:300])
print(len(GM), len(GU))
for k, n in sorted(out.items(), key=lambda kv: (kv[0][0], -kv[1])): print("%6d %s" % (n, " / ".join(k)))
