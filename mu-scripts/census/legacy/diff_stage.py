"""Set diff of two census stages' machine recipes, remapping the base's material indices through the test world's
transition files. Usage: diff_stage.py <base label> <fresh label> [first transition version, default 7]"""
import collections
import json
import os
import re
import sys

HERE = os.path.dirname(os.path.abspath(__file__))
REPO = os.path.abspath(os.path.join(HERE, "..", "..", ".."))
C = os.environ.get("CENSUS_RESULTS", os.path.join(HERE, "..", "results")) + "/"
WORLD = os.environ.get("CENSUS_TEST_WORLD", os.path.join(os.environ.get("CENSUS_WORK", os.path.join(HERE, "..", "work")), "test-server", "World"))
TDIR = os.path.join(WORLD, "materiallib", "transitions")
base, fresh = sys.argv[1], sys.argv[2]
first = int(sys.argv[3]) if len(sys.argv) > 3 else 7

if not os.path.isdir(C + fresh) or not os.listdir(C + fresh):
    print("no %s census" % fresh)
    raise SystemExit(1)

version = lambda n: int(re.match(r"v(\d+)-", n).group(1))
chain = sorted((f for f in os.listdir(TDIR) if version(f) >= first), key=version)
print("transitions applied:", chain)
maps = [{int(k): int(v) for k, v in json.load(open(os.path.join(TDIR, f))).get("moved", {}).items()} for f in chain]


def remap_index(i):
    for m in maps:
        i = m.get(i, i)
    return i


TOK = re.compile(r"(materiallib:[\w|.\-]+@)(\d+)")
remap = lambda line: TOK.sub(lambda m: m.group(1) + str(remap_index(int(m.group(2)))), line)
TOKEN = re.compile(r"[\w|.\-]+:[\w|.\-]+@\d+")

# Stacks that moved off a legacy meta item onto a MaterialLib shape read
# gregtech:gt.metaitem.0N@<1000 * prefix slot + OLD_SUB_ID> in an older census and
# materiallib:<shape>@<material index> in a newer one. Both forms normalize to <shape>:<MaterialName>: the
# legacy metas through the OLD_SUB_ID declarations in Materials.java and the meta item's own prefix slot
# order, the shape metas through the test world's material index store.
GT = os.path.join(REPO, "src/main/java/gregtech/api/enums/materials/Materials.java")
IDS = os.path.join(WORLD, "materiallib", "material-ids.json")
CUTOVER_SLOTS = {("03", 4): "nanite", ("01", 30): "cell"}


def _name_by_sub_id():
    names, current = {}, None
    for line in open(GT, encoding="utf8"):
        declaration = re.match(r"\s*Materials\.(\w+) = MaterialLibAPI\.newMaterial\(", line)
        if declaration:
            current = declaration.group(1)
        sub_id = re.search(r"GTMaterialProperties\.OLD_SUB_ID,\s*(\d+)\)", line)
        if sub_id and current:
            names[int(sub_id.group(1))] = current
    return names


NAME_BY_SUB_ID = _name_by_sub_id()
NAME_BY_INDEX = {i: n for n, i in json.load(open(IDS, encoding="utf8"))["materials"].items()}
LEGACY_META = re.compile(r"gregtech:gt\.metaitem\.(0\d)@(\d+)")
# Several shapes can serve one legacy prefix slot; each normalizes to the slot's name so the two censuses line up.
SHAPE_ALIASES = {"cellSolid": "cell"}
SHAPE_META = re.compile(r"materiallib:(cellSolid|nanite|cell|itemCasing|dustSmall|dustTiny|dust)@(\d+)")

# Foreign and gtpp items that migrate onto a MaterialLib pair normalize to <shape>:<MaterialName>, read off the
# Postea migration that moves them: the IC2 casing and gtpp Thermal dust transformers and GtppItemCutoverTable.
POSTEA = os.path.join(REPO, "src/main/java/gregtech/loaders/postload") + "/"


def _migrated_ids():
    ids = {}
    src = open(POSTEA + "PosteaTransformers.java", encoding="utf8").read()
    casings = re.search(r"registerIC2ItemCasingCutoverTransformers\(\) \{\s*Material\[\] materials = \{([^}]*)\}", src)
    if casings:
        for meta, name in enumerate(re.findall(r"Materials\.(\w+)", casings.group(1))):
            ids["IC2:itemCasing@%d" % meta] = "itemCasing:" + name
    for meta, name in re.findall(r"remapGtppThermalDust\((\d+), Materials\.(\w+)\)", src):
        ids["miscutils:material@" + meta] = "dust:" + name
    table = open(POSTEA + "GtppItemCutoverTable.java", encoding="utf8").read()
    for prefix, name, registry in re.findall(r'new Entry\(OrePrefixes\.(\w+), "(\w+)", "([^"]+)"\)', table):
        ids[registry + "@0"] = SHAPE_ALIASES.get(prefix, prefix) + ":" + name
    return ids


MIGRATED_IDS = _migrated_ids()
MIGRATED_TOKEN = re.compile(r"(?:IC2|miscutils):[\w|.\-]+@\d+")


def _legacy_meta(match):
    slot, sub_id = divmod(int(match.group(2)), 1000)
    shape = CUTOVER_SLOTS.get((match.group(1), slot))
    name = NAME_BY_SUB_ID.get(sub_id) if shape else None
    return shape + ":" + name if name else match.group(0)


def _shape_meta(match):
    name = NAME_BY_INDEX.get(int(match.group(2)))
    shape = SHAPE_ALIASES.get(match.group(1), match.group(1))
    return shape + ":" + name if name else match.group(0)


def normalize_cutover(line):
    line = MIGRATED_TOKEN.sub(lambda m: MIGRATED_IDS.get(m.group(0), m.group(0)), line)
    return SHAPE_META.sub(_shape_meta, LEGACY_META.sub(_legacy_meta, line))


# A crafting ore input lists every registrant; one that migrated duplicates its target once normalized.
ANY = re.compile(r"any\[([^\]]*)\]")
canon_any = lambda line: ANY.sub(lambda m: "any[" + ", ".join(sorted(set(m.group(1).split(", ")))) + "]", line)


SKIP = ("cropBreeder", "eyeofharmony", "centrifuge", "fakeAssembly")


def load(p, f=None):
    if not os.path.isfile(p):
        return set()
    return set((f(l.rstrip("\n")) if f else l.rstrip("\n")) for l in open(p, encoding="utf8"))


tl = tn = 0
out = []
for m in sorted(set(os.listdir(C + base)) | set(os.listdir(C + fresh))):
    if any(s in m for s in SKIP) or m in ("crafting.txt", "unification.txt", "furnace.txt"):
        continue
    nb = load(C + base + "/" + m, lambda l: normalize_cutover(remap(l)))
    nf = load(C + fresh + "/" + m, normalize_cutover)
    lost, new = nb - nf, nf - nb
    tl += len(lost)
    tn += len(new)
    if lost or new:
        a = collections.Counter(x for l in lost for x in TOKEN.findall(l))
        b = collections.Counter(x for l in new for x in TOKEN.findall(l))
        out.append("== %s lost %d new %d" % (m, len(lost), len(new)))
        out.append("   gone-only: %s" % [(x, c) for x, c in a.most_common() if x not in b][:6])
        out.append("   new-only:  %s" % [(x, c) for x, c in b.most_common() if x not in a][:6])
        out.extend("   - " + l[:300] for l in sorted(lost))
        out.extend("   + " + l[:300] for l in sorted(new))
cb = load(C + base + "/crafting.txt", lambda l: canon_any(normalize_cutover(remap(l))))
cf = load(C + fresh + "/crafting.txt", lambda l: canon_any(normalize_cutover(l)))
craft_lost, craft_new = cb - cf, cf - cb
out.append("== crafting.txt (sets, normalized) lost %d new %d" % (len(craft_lost), len(craft_new)))
out.extend("   - " + l[:300] for l in sorted(craft_lost))
out.extend("   + " + l[:300] for l in sorted(craft_new))
open(C + "%s-vs-%s.txt" % (base, fresh), "w", encoding="utf8").write("\n".join(out))
print("%s -> %s machine recipes (indices remapped) distinct lost %d new %d" % (base, fresh, tl, tn))
count = lambda p: sum(1 for _ in open(p, encoding="utf8")) if os.path.isfile(p) else 0
print("crafting.txt lines %d -> %d, normalized sets %d -> %d, lost %d new %d" % (
    count(C + base + "/crafting.txt"), count(C + fresh + "/crafting.txt"), len(cb), len(cf), len(craft_lost),
    len(craft_new)))
for l in out:
    if l.startswith("==") or l.startswith("   gone-only") or l.startswith("   new-only"):
        print(l[:250])
