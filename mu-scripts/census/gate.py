"""Output-keyed master-vs-mu census gate.

Usage: gate.py [mu census dir] [--master DIR] [--allow FILE] [--out FILE]
  Census dirs resolve against CENSUS_RESULTS (default: results/ beside this script). The mu census dir defaults to
  the one named in latest-mu.txt; master defaults to census-master; the report lands beside them.

Every recipe is keyed by what it produces, never by its inputs, so a changed input (a lens group gaining
members, an item moving to MaterialLib) cannot hide a lost product. Item tokens normalize the same way on both
sides: an item with an association becomes =<association>; an item without one whose ore names include a name
that is some item's association in either pack becomes =<that name>; an item with no ore names whose display name
belongs to exactly one associated item becomes =<that association>; otherwise a raw token present in both packs with the
same display name (or an unreadable one on either side) stays raw; otherwise it becomes ~<display name>. Amounts and NBT hashes are dropped and
outputs are sorted, which removes the run-to-run ordering noise.

Views (the first four gate; the rest inform):
  lost-product     an item some recipe makes on master that no recipe makes on mu
  lost-map-product (map, item) pairs: master's map makes the item, mu's same map does not
  empty-ore-name   an ore name with members on master and none on mu
  ore-name-leaver  a raw item still registered on mu that left an ore name it had on master
  lost-output-set  (map, full output set) with recipes on master and none on mu
  count-drop       (map, output set) whose mu recipe count fell below half of master's (master >= 4)
  duplicate-input  (map, canonical inputs) with more mu recipes than master recipes, when mu has more than one:
                   machines can reach only one recipe of such a group
  cost-shift       a machine recipe paired across the packs by map, output set and canonical inputs (or, failing
                   that, by the only recipe with that output set, or the only one with those inputs regardless of
                   amounts) whose EU/t or duration moved by more than a factor of 4
  lost-output-less (map, canonical inputs) of a recipe without item or fluid outputs (fuels, radiation hatch, ...)
                   on master and none on mu
  changed-recipe   (map, output set) on both sides whose recipes differ once canonicalized: amounts, chances,
                   inputs, duration, EU/t, flags or metadata; maps in UNSTABLE are left out

accepted-drift.tsv lines: <view> TAB <map glob> TAB <key regex> TAB <reason>. A finding whose view matches, whose
map matches the glob (fnmatch; use * for views without a map) and whose key is matched by the regex (re.search)
is listed as accepted instead of unexplained. Exit status is 1 when a gating view has unexplained findings.
"""
import collections
import fnmatch
import os
import re
import sys

BASE = os.path.dirname(os.path.abspath(__file__))
RESULTS = os.environ.get("CENSUS_RESULTS", os.path.join(BASE, "results"))
SPECIAL = {"crafting.txt", "furnace.txt", "accept.txt", "shown.txt", "items.tsv", "oredict.tsv"}
GATING = ("lost-product", "lost-map-product", "empty-ore-name", "ore-name-leaver", "duplicate-input", "cost-shift",
          "lost-output-less")
COST = re.compile(r" t=(-?\d+) eut=(-?\d+)")
SHIFT = 4
# Maps whose recipes differ between two boots of the same build (ore-list member order, random ore variants).
UNSTABLE = {"cropsnh.recipes.cropBreeder", "gt.recipe.fakeAssemblylineProcess"}
AMOUNT = re.compile(r"x\d+(?=['\"@])")
TOKEN = re.compile(r"([^\s\[\],=]+:[^\s\[\],]+@\d+)(?:x\d+)?(?:#[0-9a-f]+)?")
MACHINE = re.compile(r"in=(\[.*?\]) fin=(\[.*?\]) out=(\[.*?\]) fout=(\[.*?\]) t=")
CRAFT = re.compile(r"out=(\S+) ")
FURNACE = re.compile(r"in=(\S+) out=(\S+)")


def arg(flag, default):
    if flag in sys.argv:
        i = sys.argv.index(flag)
        value = sys.argv[i + 1]
        del sys.argv[i:i + 2]
        return value
    return default


def load_items(d):
    items = {}
    for line in open(os.path.join(d, "items.tsv"), encoding="utf8", errors="replace"):
        p = line.rstrip("\n").split("\t")
        if len(p) >= 6:
            items[p[0]] = (p[1], "\t".join(p[5:]), [o for o in p[2].split(",") if o])
    return items


def load_oredict(d):
    names = collections.defaultdict(set)
    path = os.path.join(d, "oredict.tsv")
    if os.path.isfile(path):
        for line in open(path, encoding="utf8", errors="replace"):
            name, _, stack = line.rstrip("\n").partition("\t")
            m = TOKEN.match(stack)
            if m:
                names[name].add(m.group(1))
    return names


class Side:

    def __init__(self, d):
        self.dir = d
        self.items = load_items(d)
        self.ores = load_oredict(d)
        self.known = set(self.items) | {t for members in self.ores.values() for t in members}


def canonicalizer(master, mu):
    cache = {}
    associations = {i[0] for side in (master, mu) for i in side.items.values() if i[0] != "-"}
    by_display = collections.defaultdict(set)
    for side in (master, mu):
        for i in side.items.values():
            if i[0] != "-" and i[1] not in ("", "?"):
                by_display[i[1]].add(i[0])

    def canon(side, token):
        key = (id(side), token)
        if key in cache:
            return cache[key]
        info = side.items.get(token)
        a, b = master.items.get(token), mu.items.get(token)
        adopted = sorted(o for o in info[2] if o in associations) if info else []
        if info and info[0] != "-":
            result = "=" + info[0]
        elif adopted:
            result = "=" + adopted[0]
        elif a and b and (a[1] == b[1] or "?" in (a[1], b[1])):
            result = token
        elif info and not info[2] and len(by_display.get(info[1], ())) == 1:
            result = "=" + next(iter(by_display[info[1]]))
        elif info and info[1] not in ("", "?"):
            result = "~" + info[1]
        else:
            result = token
        cache[key] = result
        return result

    return canon


def outputs(side, canon, text):
    return sorted({canon(side, m.group(1)) for m in TOKEN.finditer(text)})


def fluids(text):
    return sorted({f.rsplit("x", 1)[0] for f in text.strip("[]").split(", ") if f and f != "-"})


LIST = re.compile(r"(\w+)=(\[.*?\])")


def split_list(text):
    inner = text.strip()[1:-1]
    return [x for x in inner.split(", ") if x] if inner else []


def canon_stack(side, canon, text):
    m = TOKEN.match(text)
    if not m:
        return text
    amount = re.search(r"x(\d+)", text[m.end(1):])
    return canon(side, m.group(1)) + ("x" + amount.group(1) if amount else "")


def canon_machine(side, canon, line):
    """The recipe with every item canonicalized and each list sorted, chances kept beside their stack."""
    fields = dict(LIST.findall(line))
    def paired(stacks, chances, is_item):
        items = split_list(stacks)
        rates = chances.strip()[1:-1].split(",") if chances else []
        out = []
        for i, x in enumerate(items):
            key = canon_stack(side, canon, x) if is_item else x
            out.append(key + ("@" + rates[i] if i < len(rates) and rates[i] != "10000" else ""))
        return sorted(out)
    tail = re.sub(r" (ic|oc|foc)=\[[^\]]*\]", "", line[line.index(" t="):]).strip()
    tail = re.sub(r"RecipeMetadataKey\{[^}]*_identifier=([\w]+)'?\}", r"\1", tail)
    tail = re.sub(r"(;?)variant_group=[^;}]*(;?)", lambda v: ";" if v.group(1) and v.group(2) else "", tail)
    tail = tail.replace(" meta={}", "")
    tail = re.sub(r"(special=\S+?)#[0-9a-f]+", r"\1", tail)
    tail = tail.replace("<Materials>", "<Material>")
    return "in=%s fin=%s out=%s fout=%s %s" % (
        paired(fields.get("in", "[]"), fields.get("ic"), True), sorted(split_list(fields.get("fin", "[]"))),
        paired(fields.get("out", "[]"), fields.get("oc"), True),
        paired(fields.get("fout", "[]"), fields.get("foc"), False), tail)


ANY = re.compile(r"any\[([^\]]*)\]")
CRAFT_INPUTS = re.compile(r" in=\[(.*)\]$")


def canon_crafting(side, canon, line):
    """The crafting recipe with its inputs as a sorted multiset: each alternative list canonicalized, deduplicated
    and sorted (a one-member list collapses to its member), empty slots dropped."""
    m = CRAFT_INPUTS.search(line)
    if not m:
        return TOKEN.sub(lambda t: canon(side, t.group(1)), line)
    def one(text):
        m2 = TOKEN.match(text)
        return canon(side, m2.group(1)) if m2 else text
    def alternatives(a):
        members = sorted({one(x) for x in a.group(1).split(", ") if x})
        return members[0] if len(members) == 1 else "any[" + "|".join(members) + "]"
    slots = ANY.sub(alternatives, m.group(1)).split(", ")
    head = TOKEN.sub(lambda t: canon(side, t.group(1)), line[:m.start()])
    return head + " in=" + str(sorted(one(x) for x in slots if x and x != "-"))


def recipe_outputs(side, canon):
    """(map, output set tuple) -> recipe count, and -> Counter of canonical full recipe lines, over every machine
    map plus crafting and furnace."""
    result = collections.Counter()
    full = collections.defaultdict(collections.Counter)
    for f in sorted(os.listdir(side.dir)):
        path = os.path.join(side.dir, f)
        if not f.endswith(".txt") or f in ("accept.txt", "shown.txt"):
            continue
        name = f[:-4]
        for line in open(path, encoding="utf8", errors="replace"):
            line = line.rstrip("\n")
            if f == "crafting.txt":
                m = CRAFT.match(line)
                outs = tuple(outputs(side, canon, m.group(1))) if m else ()
                text = canon_crafting(side, canon, line)
            elif f == "furnace.txt":
                m = FURNACE.match(line)
                outs = tuple(outputs(side, canon, m.group(2))) if m else ()
                text = TOKEN.sub(lambda t: canon(side, t.group(1)), line)
            else:
                m = MACHINE.match(line)
                if not m:
                    continue
                outs = tuple(outputs(side, canon, m.group(3)) + ["fluid:" + x for x in fluids(m.group(4))])
                text = canon_machine(side, canon, line)
            if outs:
                result[(name, outs)] += 1
                full[(name, outs)][text] += 1
    return result, full


def inputs_of(text):
    """The canonical input half (items and fluids with amounts) of a canonical machine recipe line."""
    return text.split(" out=", 1)[0]


def machine_recipes(side, canon):
    """map -> list of (canonical inputs, output set, duration, EU/t) over every machine map, including recipes
    without outputs."""
    recipes = collections.defaultdict(list)
    for f in sorted(os.listdir(side.dir)):
        if not f.endswith(".txt") or f in SPECIAL:
            continue
        for line in open(os.path.join(side.dir, f), encoding="utf8", errors="replace"):
            line = line.rstrip("\n")
            m = MACHINE.match(line)
            if not m:
                continue
            outs = tuple(outputs(side, canon, m.group(3)) + ["fluid:" + x for x in fluids(m.group(4))])
            cost = COST.search(line)
            recipes[f[:-4]].append((inputs_of(canon_machine(side, canon, line)), outs, int(cost.group(1)),
                                    int(cost.group(2))))
    return recipes


def ratio_outside(a, b):
    if a <= 0 or b <= 0:
        return a != b and max(a, b) > 1
    return b > a * SHIFT or a > b * SHIFT


def load_allow(path):
    rules = []
    if os.path.isfile(path):
        for n, line in enumerate(open(path, encoding="utf8"), 1):
            if not line.strip() or line.startswith("#"):
                continue
            p = line.rstrip("\n").split("\t")
            if len(p) < 4:
                sys.exit("%s:%d: expected view<TAB>map glob<TAB>key regex<TAB>reason" % (path, n))
            rules.append((p[0], p[1], re.compile(p[2]), p[3], n))
    return rules


def main():
    master_dir = os.path.join(RESULTS, arg("--master", "census-master"))
    allow_path = os.path.join(BASE, arg("--allow", "accepted-drift.tsv"))
    out_arg = arg("--out", None)
    mu_name = sys.argv[1] if len(sys.argv) > 1 else open(os.path.join(RESULTS, "latest-mu.txt")).read().strip()
    mu_dir = os.path.join(RESULTS, mu_name)
    master, mu = Side(master_dir), Side(mu_dir)
    canon = canonicalizer(master, mu)
    (om, fm), (ou, fu) = recipe_outputs(master, canon), recipe_outputs(mu, canon)

    findings = []  # (view, map, key, detail)
    made_m = collections.defaultdict(set)
    made_u = collections.defaultdict(set)
    for (name, outs), n in om.items():
        for o in outs:
            made_m[o].add(name)
    for (name, outs), n in ou.items():
        for o in outs:
            made_u[o].add(name)
    for item in sorted(set(made_m) - set(made_u)):
        findings.append(("lost-product", "*", item, "made on master by " + ",".join(sorted(made_m[item]))))
    for item in sorted(set(made_m) & set(made_u)):
        for name in sorted(made_m[item] - made_u[item]):
            findings.append(("lost-map-product", name, item, "mu makes it only in " + ",".join(sorted(made_u[item]))))

    for name in sorted(master.ores):
        if master.ores[name] and not mu.ores.get(name):
            findings.append(("empty-ore-name", "*", name, "master members: " + ",".join(sorted(master.ores[name])[:4])))
    for name in sorted(set(master.ores) & set(mu.ores)):
        for token in sorted(master.ores[name] - mu.ores[name]):
            if token in mu.known:
                findings.append(("ore-name-leaver", "*", name + " " + token, ""))

    by_map_m = collections.Counter()
    for (name, outs), n in om.items():
        if (name, outs) not in ou:
            findings.append(("lost-output-set", name, " ".join(outs), "%d recipe(s) on master" % n))
        elif n >= 4 and ou[(name, outs)] * 2 < n:
            findings.append(("count-drop", name, " ".join(outs), "master %d -> mu %d" % (n, ou[(name, outs)])))
        elif fm[(name, outs)] != fu[(name, outs)] and name not in UNSTABLE:
            only_m = sorted((fm[(name, outs)] - fu[(name, outs)]).elements())
            only_u = sorted((fu[(name, outs)] - fm[(name, outs)]).elements())
            findings.append(("changed-recipe", name, " ".join(outs), "master: %s | mu: %s" % (
                only_m[0][:300] if only_m else "-", only_u[0][:300] if only_u else "-")))

    rm, ru = machine_recipes(master, canon), machine_recipes(mu, canon)
    for name in sorted(set(rm) | set(ru)):
        count_m = collections.Counter(r[0] for r in rm.get(name, ()))
        count_u = collections.Counter(r[0] for r in ru.get(name, ()))
        for ins, n in sorted(count_u.items()):
            if n > 1 and n > count_m[ins]:
                outs = sorted({" ".join(r[1]) or "-" for r in ru[name] if r[0] == ins})
                findings.append(("duplicate-input", name, ins, "master %d -> mu %d; outputs: %s" % (
                    count_m[ins], n, " | ".join(outs)[:300])))
        no_out_m = {r[0] for r in rm.get(name, ()) if not r[1]}
        no_out_u = {r[0] for r in ru.get(name, ()) if not r[1]}
        for ins in sorted(no_out_m - no_out_u):
            findings.append(("lost-output-less", name, ins, ""))
        paired_m = collections.defaultdict(list)
        for ins, outs, t, eut in rm.get(name, ()):
            paired_m[(ins, outs)].append((t, eut))
        paired_u = collections.defaultdict(list)
        for ins, outs, t, eut in ru.get(name, ()):
            paired_u[(ins, outs)].append((t, eut))
        pairs = [(key, paired_m[key][0], paired_u[key][0]) for key in sorted(set(paired_m) & set(paired_u))
                 if len(paired_m[key]) == 1 and len(paired_u[key]) == 1]
        by_outs_m = collections.defaultdict(list)
        for ins, outs, t, eut in rm.get(name, ()):
            by_outs_m[outs].append((ins, t, eut))
        by_outs_u = collections.defaultdict(list)
        for ins, outs, t, eut in ru.get(name, ()):
            by_outs_u[outs].append((ins, t, eut))
        for outs in sorted(set(by_outs_m) & set(by_outs_u)):
            if outs and len(by_outs_m[outs]) == 1 and len(by_outs_u[outs]) == 1 \
                    and by_outs_m[outs][0][0] != by_outs_u[outs][0][0]:
                (ins_m, tm, em), (ins_u, tu, eu) = by_outs_m[outs][0], by_outs_u[outs][0]
                pairs.append(((ins_u, outs), (tm, em), (tu, eu)))
        by_bare_m = collections.defaultdict(list)
        for ins, outs, t, eut in rm.get(name, ()):
            by_bare_m[(AMOUNT.sub("", ins), outs)].append((ins, t, eut))
        by_bare_u = collections.defaultdict(list)
        for ins, outs, t, eut in ru.get(name, ()):
            by_bare_u[(AMOUNT.sub("", ins), outs)].append((ins, t, eut))
        paired_keys = {key for key, _, _ in pairs}
        for bare in sorted(set(by_bare_m) & set(by_bare_u)):
            if len(by_bare_m[bare]) == 1 and len(by_bare_u[bare]) == 1:
                (ins_m, tm, em), (ins_u, tu, eu) = by_bare_m[bare][0], by_bare_u[bare][0]
                if (ins_u, bare[1]) not in paired_keys:
                    pairs.append(((ins_u, bare[1]), (tm, em), (tu, eu)))
        for key, (tm, em), (tu, eu) in pairs:
            if ratio_outside(tm, tu) or ratio_outside(em, eu):
                findings.append(("cost-shift", name, " ".join(key[1]) + " <- " + key[0],
                                 "master t=%d eut=%d | mu t=%d eut=%d" % (tm, em, tu, eu)))

    rules = load_allow(allow_path)
    used = collections.Counter()
    accepted, unexplained = [], []
    for f in findings:
        view, name, key, detail = f
        for rule in rules:
            if rule[0] == view and fnmatch.fnmatch(name, rule[1]) and rule[2].search(key):
                accepted.append(f + (rule[3],))
                used[rule[4]] += 1
                break
        else:
            unexplained.append(f)

    report = out_arg or os.path.join(RESULTS, "gate-%s.txt" % os.path.basename(mu_name))
    views = ("lost-product", "lost-map-product", "empty-ore-name", "ore-name-leaver", "duplicate-input", "cost-shift",
             "lost-output-less", "lost-output-set", "count-drop", "changed-recipe")
    with open(report, "w", encoding="utf8") as fo:
        fo.write("master=%s mu=%s allow=%s\n\n" % (os.path.basename(master_dir), mu_name, os.path.basename(allow_path)))
        for view in views:
            rows = [f for f in unexplained if f[0] == view]
            fo.write("== %s: %d unexplained%s\n" % (view, len(rows), "" if view in GATING else " (informational)"))
            for _, name, key, detail in rows:
                fo.write("  %s\t%s\t%s\n" % (name, key, detail))
            fo.write("\n")
        fo.write("== accepted\n")
        for view, name, key, detail, reason in accepted:
            fo.write("  %s\t%s\t%s\t%s\n" % (view, name, key, reason))
        stale = [r for r in rules if not used[r[4]]]
        if stale:
            fo.write("\n== allowlist rules that matched nothing\n")
            for r in stale:
                fo.write("  line %d: %s %s %s\n" % (r[4], r[0], r[1], r[2].pattern))
    counts = collections.Counter(f[0] for f in unexplained)
    print("report:", report)
    for view in views:
        print("  %-17s %5d unexplained%s" % (view, counts[view], "" if view in GATING else "  (info)"))
    print("  accepted          %5d" % len(accepted))
    sys.exit(1 if any(counts[v] for v in GATING) else 0)


if __name__ == "__main__":
    main()
