"""Lists recipes mu has that master does not, the reverse of gate.py's loss views.

Usage: gains.py <mu census dir> [--out FILE]
A gained recipe is one in a (map, output set) key master lacks ("new-set"), or a surplus canonical recipe in a key
both sides share ("surplus"). Each row carries the master recipe in the same map with the same canonical inputs,
when one exists ("twin"), which marks a renamed or changed output rather than a new recipe. Writes one TSV row per
gained recipe (map, kind, output key, mu line, twin or -) and prints counts per (map, kind, twinned).
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

OUT = re.compile(r" ?out=(\[.*?\]|\S+)")


def inputs_of(line):
    return re.sub(r" fout=\[.*?\]", "", OUT.sub("", line, count=1)).split(" t=")[0]


def main():
    out_file = gate.arg("--out", None)
    mu_name = sys.argv[1]
    master, mu = gate.Side(os.path.join(gate.RESULTS, "census-master")), gate.Side(os.path.join(gate.RESULTS, mu_name))
    canon = gate.canonicalizer(master, mu)
    (om, fm), (ou, fu) = gate.recipe_outputs(master, canon), gate.recipe_outputs(mu, canon)
    by_inputs = collections.defaultdict(dict)
    for (name, _), lines in fm.items():
        for line in lines:
            by_inputs[name].setdefault(inputs_of(line), line)
    rows, counts = [], collections.Counter()
    for key, lines in fu.items():
        name, outs = key
        kind = "surplus" if key in om else "new-set"
        for line in (lines - fm.get(key, collections.Counter())).elements():
            if kind == "surplus" and ou[key] <= om[key]:
                continue
            twin = by_inputs[name].get(inputs_of(line), "-")
            rows.append((name, kind, " ".join(outs), line, twin))
            counts[(name, kind, twin != "-")] += 1
    path = out_file or os.path.join(gate.RESULTS, "gains-%s.tsv" % os.path.basename(mu_name))
    with open(path, "w", encoding="utf8") as fo:
        for r in rows:
            fo.write("\t".join(r) + "\n")
    print("rows:", len(rows), "->", path)
    for (name, kind, twinned), c in counts.most_common(80):
        print("%6d  %-40s %-8s %s" % (c, name, kind, "twinned" if twinned else ""))


if __name__ == "__main__":
    main()
