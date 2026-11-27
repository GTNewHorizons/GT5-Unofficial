"""Classifies gate.py's changed-recipe findings by which recipe fields changed.

Usage: audit_changes.py <mu census dir> [--out FILE]
Pairs master and mu recipes that share a (map, output set) key one-to-one (keys with differing recipe counts
are reported as "count-changed") and names each differing field: inputs, fluid inputs, output amounts, output
chances, fluid outputs, duration, eut, special value, flags, metadata. Writes one TSV row per finding (map, output
key, change kinds, master line, mu line) and prints counts per (map, change kind).
"""
import collections
import importlib.util
import os
import re
import sys

BASE = os.path.dirname(os.path.abspath(__file__))
spec = importlib.util.spec_from_file_location("gate", os.path.join(BASE, "gate.py"))
gate = importlib.util.module_from_spec(spec)
spec.loader.exec_module(gate)

MACHINE = re.compile(r"in=(\[.*?\]) fin=(\[.*?\]) out=(\[.*?\]) fout=(\[.*?\]) t=(\S+) eut=(\S+) sv=(\S+)(.*)")


def fields(line):
    m = MACHINE.match(line)
    if not m:
        return {"raw": line}
    tail = m.group(8)
    flags = re.search(r"flags=(\S+)", tail)
    meta = re.search(r"meta=(\{.*\})", tail)
    return {"in": m.group(1), "fin": m.group(2), "out": m.group(3), "fout": m.group(4), "t": m.group(5),
            "eut": m.group(6), "sv": m.group(7), "flags": flags.group(1) if flags else "",
            "meta": meta.group(1) if meta else ""}


def kinds(a, b):
    if "raw" in a or "raw" in b:
        return ["crafting-or-furnace"]
    out = []
    ain, bin_ = a["in"], b["in"]
    if ain != bin_:
        strip = lambda s: re.sub(r"x\d+", "", s)
        out.append("input-items" if strip(ain) != strip(bin_) else "input-amounts")
    if a["fin"] != b["fin"]:
        strip = lambda s: re.sub(r"x\d+", "", s)
        out.append("fluid-inputs" if strip(a["fin"]) != strip(b["fin"]) else "fluid-input-amounts")
    if a["out"] != b["out"]:
        strip = lambda s: re.sub(r"@\d+(,\d+)*", "", s)
        out.append("output-amounts" if strip(a["out"]) != strip(b["out"]) else "output-chances")
    if a["fout"] != b["fout"]:
        out.append("fluid-outputs")
    for key in ("t", "eut", "sv", "flags", "meta"):
        if a[key] != b[key]:
            out.append({"t": "duration", "eut": "eut", "sv": "special-value", "flags": "flags", "meta": "metadata"}[key])
    return out or ["identical?"]


def main():
    out_file = gate.arg("--out", None)
    mu_name = sys.argv[1]
    master, mu = gate.Side(os.path.join(gate.RESULTS, "census-master")), gate.Side(os.path.join(gate.RESULTS, mu_name))
    canon = gate.canonicalizer(master, mu)
    (om, fm), (ou, fu) = gate.recipe_outputs(master, canon), gate.recipe_outputs(mu, canon)
    rows = []
    counts = collections.Counter()
    for key, n in om.items():
        if key not in ou or fm[key] == fu[key]:
            continue
        only_m = sorted((fm[key] - fu[key]).elements())
        only_u = sorted((fu[key] - fm[key]).elements())
        name, outs = key
        if len(only_m) != len(only_u):
            ks = ["count-changed"]
            rows.append((name, " ".join(outs), ",".join(ks), only_m[0] if only_m else "-", only_u[0] if only_u else "-"))
            counts[(name, "count-changed")] += 1
            continue
        for a, b in zip(only_m, only_u):
            ks = kinds(fields(a), fields(b))
            rows.append((name, " ".join(outs), ",".join(ks), a, b))
            counts[(name, ",".join(ks))] += 1
    path = out_file or os.path.join(gate.RESULTS, "changes-%s.tsv" % os.path.basename(mu_name))
    with open(path, "w", encoding="utf8") as fo:
        for r in rows:
            fo.write("\t".join(r) + "\n")
    print("rows:", len(rows), "->", path)
    for (name, k), c in counts.most_common(60):
        print("%6d  %-40s %s" % (c, name, k))


if __name__ == "__main__":
    main()
