"""Compares an InterchangeCensus dump from a master pack with one from a mu pack.

Item tokens are compared raw when the same registry id and damage names the same item in both packs (same display
name or same association). Otherwise they normalize to their association (=ingotCopper), else their first ore name
(=name), else their display name (~Name), so a GT, bartworks or GT++ material part lines up with its MaterialLib
successor. Usage: compare.py <master dir> <mu dir> <out dir>"""
import collections
import os
import re
import sys

M, U, OUT = sys.argv[1], sys.argv[2], sys.argv[3]
os.makedirs(OUT, exist_ok=True)


def load_items(d):
    items = {}
    for line in open(os.path.join(d, "items.tsv"), encoding="utf8"):
        parts = line.rstrip("\n").split("\t")
        if len(parts) < 6:
            continue
        tok, assoc, ores, bl, copy, display = parts[0], parts[1], parts[2], parts[3], parts[4], "\t".join(parts[5:])
        items[tok] = dict(assoc=assoc, ores=[o for o in ores.split(",") if o], bl=bl == "bl=true",
                          copy=copy == "copy=true", display=display)
    return items


IM, IU = load_items(M), load_items(U)


def shared(t):
    a, b = IM.get(t), IU.get(t)
    if not a or not b:
        return False
    return a["display"] == b["display"] or (a["assoc"] != "-" and a["assoc"] == b["assoc"])


def canon_of(items, t):
    if shared(t):
        return t
    i = items.get(t)
    if not i:
        return t
    if i["assoc"] != "-":
        return "=" + i["assoc"]
    if i["ores"]:
        return "=" + i["ores"][0]
    if i["display"] and i["display"] != "?":
        return "~" + i["display"]
    return t


_cache = {}


def canon(side, t):
    k = (side, t)
    if k not in _cache:
        _cache[k] = canon_of(IM if side == "M" else IU, t)
    return _cache[k]


TOKEN = re.compile(r"([\w|.\-]+:[\w|.\-]+@\d+)")


def norm_line(side, line):
    return TOKEN.sub(lambda m: canon(side, m.group(1)), line)


ANY = re.compile(r"any\[([^\]]*)\]")


def canon_any(line):
    return ANY.sub(lambda m: "any[" + ", ".join(sorted(set(m.group(1).split(", ")))) + "]", line)


def lines(d, f):
    p = os.path.join(d, f)
    return [l.rstrip("\n") for l in open(p, encoding="utf8")] if os.path.isfile(p) else []


report = []
SPECIAL = {"crafting.txt", "furnace.txt", "accept.txt", "shown.txt", "items.tsv", "oredict.tsv"}

# Recipes
maps = sorted((set(os.listdir(M)) | set(os.listdir(U))) - SPECIAL)
recipe_rows = []
tot = collections.Counter()
detail = []
for f in maps + ["crafting.txt", "furnace.txt"]:
    lm, lu = lines(M, f), lines(U, f)
    post = canon_any if f == "crafting.txt" else (lambda x: x)
    cm = collections.Counter(post(norm_line("M", l)) for l in lm)
    cu = collections.Counter(post(norm_line("U", l)) for l in lu)
    raw_same = collections.Counter(lm) & collections.Counter(lu)
    lost, new = cm - cu, cu - cm
    nl, nn = sum(lost.values()), sum(new.values())
    tot["master"] += len(lm)
    tot["mu"] += len(lu)
    tot["lost"] += nl
    tot["new"] += nn
    tot["raw_same"] += sum(raw_same.values())
    if nl or nn or len(lm) != len(lu):
        recipe_rows.append((f, len(lm), len(lu), sum(raw_same.values()), nl, nn))
        detail.append("== %s master=%d mu=%d raw-identical=%d lost=%d new=%d" % (f, len(lm), len(lu),
                                                                             sum(raw_same.values()), nl, nn))
        detail.extend("   - %s%s" % ("" if c == 1 else "(x%d) " % c, l[:400]) for l, c in sorted(lost.items()))
        detail.extend("   + %s%s" % ("" if c == 1 else "(x%d) " % c, l[:400]) for l, c in sorted(new.items()))
open(os.path.join(OUT, "recipes-diff.txt"), "w", encoding="utf8").write("\n".join(detail))


# Acceptance
def load_accept(d, side):
    acc = collections.defaultdict(lambda: [0, 0, 0, 0])
    raw = {}
    for line in lines(d, "accept.txt"):
        parts = line.split("\t")
        m, x, c = parts[0], parts[1], parts[2]
        n, L, C, O = (int(p.split("=")[1]) for p in parts[3:7])
        key = (m, canon(side, x), canon(side, c))
        a = acc[key]
        a[0] += n
        a[1] += L
        a[2] += C
        a[3] += O
        raw[(m, x, c)] = (n, L, C, O)
    return acc, raw


AM, rawM = load_accept(M, "M")
AU, rawU = load_accept(U, "U")


def status(a):
    n, L, C, O = a
    if n == 0:
        return "none"
    s = "L" + ("all" if L == n else "0" if L == 0 else "part") + "/C" + ("all" if C == n else "0" if C == 0 else "part")
    return s


def accepts(a):
    return a[2] > 0


def prefix_of(k):
    m = re.match(r"=([a-z]+)", k)
    return m.group(1) if m else ("item:" + k.split(":")[0] if ":" in k else k[:12])


acc_rows = collections.Counter()
acc_detail = collections.defaultdict(list)
for key in set(AM) | set(AU):
    a, b = AM.get(key), AU.get(key)
    x = key[1]
    p = prefix_of(x)
    if a and b:
        sa, sb = status(a), status(b)
        if sa == sb:
            acc_rows[(p, "same:" + sa)] += 1
            continue
        cls = "changed %s -> %s" % (sa, sb)
    elif a:
        cls = "master-only " + status(a)
    else:
        cls = "mu-only " + status(b)
    acc_rows[(p, cls)] += 1
    acc_detail[cls].append((key, a, b))

with open(os.path.join(OUT, "accept-diff.txt"), "w", encoding="utf8") as fo:
    for cls in sorted(acc_detail):
        rows = sorted(acc_detail[cls])
        fo.write("== %s (%d)\n" % (cls, len(rows)))
        for key, a, b in rows:
            fo.write("   %s | %s | %s | master=%s mu=%s\n" % (key[0], key[1], key[2], a, b))
with open(os.path.join(OUT, "accept-summary.txt"), "w", encoding="utf8") as fo:
    for (p, cls), n in sorted(acc_rows.items()):
        fo.write("%-24s %-40s %d\n" % (p, cls, n))


# Lookup/consume disagreement per side
def disagreements(raw):
    out = collections.Counter()
    for (m, x, c), (n, L, C, O) in raw.items():
        if L != C:
            out[("lookup>consume" if L > C else "consume>lookup", m)] += 1
    return out


dm, du = disagreements(rawM), disagreements(rawU)

# NEI alternatives
def load_shown(d, side):
    s = collections.defaultdict(set)
    for line in lines(d, "shown.txt"):
        m, x, alts = line.split("\t")
        alts = [a for a in alts.strip("[]").split(", ") if a]
        s[(m, canon(side, x))] |= {canon(side, a) for a in alts}
    return s


SM, SU = load_shown(M, "M"), load_shown(U, "U")
shown_rows = collections.Counter()
shown_detail = []
for key in sorted(set(SM) & set(SU)):
    a, b = SM[key] - {key[1]}, SU[key] - {key[1]}
    if a == b:
        shown_rows["same"] += 1
        continue
    shown_rows["differs"] += 1
    shown_detail.append("%s | %s | master-only=%s mu-only=%s" % (key[0], key[1], sorted(a - b), sorted(b - a)))
open(os.path.join(OUT, "shown-diff.txt"), "w", encoding="utf8").write("\n".join(shown_detail))


# Unified copies and ore names
def copies(items):
    return {t for t, i in items.items() if i["copy"]}


def load_ore(d, side):
    o = collections.defaultdict(set)
    for line in lines(d, "oredict.tsv"):
        name, st = line.split("\t")
        tok = st.split("x")[0] if "x" in st else st
        tok = re.sub(r"x\d+(#\w+)?$", "", st)
        o[name].add(canon(side, tok))
    return o


OM, OU = load_ore(M, "M"), load_ore(U, "U")
ore_detail = []
ore_rows = collections.Counter()
for name in sorted(set(OM) | set(OU)):
    a, b = OM.get(name, set()), OU.get(name, set())
    if a == b:
        ore_rows["same"] += 1
    elif not a:
        ore_rows["mu-only name"] += 1
        ore_detail.append("+name %s %s" % (name, sorted(b)[:8]))
    elif not b:
        ore_rows["master-only name"] += 1
        ore_detail.append("-name %s %s" % (name, sorted(a)[:8]))
    else:
        ore_rows["members differ"] += 1
        ore_detail.append("~ %s master-only=%s mu-only=%s" % (name, sorted(a - b)[:8], sorted(b - a)[:8]))
open(os.path.join(OUT, "oredict-diff.txt"), "w", encoding="utf8").write("\n".join(ore_detail))

with open(os.path.join(OUT, "summary.txt"), "w", encoding="utf8") as fo:
    fo.write("items: master=%d mu=%d shared-raw=%d\n" % (len(IM), len(IU), sum(1 for t in IM if shared(t))))
    fo.write("unified copies: master=%d mu=%d\n" % (len(copies(IM)), len(copies(IU))))
    fo.write("recipes: master=%d mu=%d raw-identical=%d normalized lost=%d new=%d\n" % (
        tot["master"], tot["mu"], tot["raw_same"], tot["lost"], tot["new"]))
    for r in sorted(recipe_rows, key=lambda r: -(r[4] + r[5])):
        fo.write("   %-50s master=%-7d mu=%-7d raw-same=%-7d lost=%-6d new=%-6d\n" % r)
    fo.write("acceptance keys: master=%d mu=%d\n" % (len(AM), len(AU)))
    agg = collections.Counter()
    for (p, cls), n in acc_rows.items():
        agg[cls.split(" ")[0] if not cls.startswith("same") else "same"] += n
    for k, v in sorted(agg.items()):
        fo.write("   %s %d\n" % (k, v))
    fo.write("lookup/consume disagreements master=%d mu=%d\n" % (sum(dm.values()), sum(du.values())))
    for side, d in (("master", dm), ("mu", du)):
        for (k, m), n in sorted(d.items(), key=lambda kv: -kv[1])[:15]:
            fo.write("   %s %s %s %d\n" % (side, k, m, n))
    fo.write("NEI shown alternatives: %s\n" % dict(shown_rows))
    fo.write("ore names: %s\n" % dict(ore_rows))
print(open(os.path.join(OUT, "summary.txt"), encoding="utf8").read())
