"""One-time generator of Materials2Formulas.java (RETIRED after its single run; the generated file is
hand-maintained from then on -- see the note atop Materials2Formulas.java).

Bakes GTMaterialProperties.FORMULA declaration entries for every ported gregtech material whose legacy
`Materials#mChemicalFormula` was a real formula, closing the LegacyMaterials "known gap" (mChemicalFormula
overrides) and giving the render/NEI side (MaterialFormulas) a bridge-independent source:

- The ~170 explicit `MaterialBuilder#setChemicalFormula` overrides are extracted from the deleted
  MaterialsInit's source (read from the master branch via `git show`) and emitted verbatim -- CustomGlyphs
  and EnumChatFormatting references intact, the 6 localized overrides also emitting FORMULA_LOCALIZED.
  Overrides that referenced OTHER materials' formulas at declaration time
  (`Materials.X.getChemicalFormula()` concatenations) are flattened to the literal string the legacy loader
  computed: every such reference resolved to an already-declared material, so its final dumped formula is
  exactly the value the concatenation saw.
- Every other ported material takes its constructor-derived formula from a fresh
  `-Dgt.dumpMaterialData=true` boot dump (gt-materials.json, `chemicalFormula` field), skipping `"?"`/empty
  (no legacy tooltip line) and skipping werkstoff-composite materials outright (BridgeMaterialsLoader
  overwrote their gregtech-side formula from the werkstoff unconditionally, so WerkstoffData.formula is the
  authoritative declaration source for them and baking a translated-at-dump-time copy would both duplicate
  and freeze it).

PUA glyph characters (U+E000..U+E020) in flattened/derived strings are re-emitted as their CustomGlyphs
constant references so the generated source stays readable and matches the legacy declarations' own style.

Usage: python scripts/mu/add_chemical_formulas.py [dump-dir]
    dump-dir defaults to run/server/material-dump. Run from the repo root.
"""

import json
import re
import subprocess
import sys
from pathlib import Path

REPO = Path(__file__).resolve().parents[2]
M2M = REPO / "src/main/java/gregtech/api/enums/materials2/Materials2Materials.java"
OUT = REPO / "src/main/java/gregtech/api/enums/materials2/Materials2Formulas.java"
DUMP_DIR = Path(sys.argv[1]) if len(sys.argv) > 1 else REPO / "run/server/material-dump"

CHUNK = 60

GLYPHS = {
    "\ue000": "CustomGlyphs.AIR", "\ue001": "CustomGlyphs.EARTH", "\ue002": "CustomGlyphs.FIRE",
    "\ue003": "CustomGlyphs.WATER", "\ue004": "CustomGlyphs.CHAOS", "\ue005": "CustomGlyphs.ORDER",
    "\ue006": "CustomGlyphs.CIRCLE_STAR", "\ue007": "CustomGlyphs.PICKAXE",
    "\ue008": "CustomGlyphs.ARROW_CORNER_SOUTH_EAST", "\ue009": "CustomGlyphs.ARROW_CORNER_NORTH_WEST",
    "\ue00a": "CustomGlyphs.HIGH_VOLTAGE", "\ue00b": "CustomGlyphs.STAR", "\ue00c": "CustomGlyphs.GALAXY",
    "\ue00d": "CustomGlyphs.SPARKLES", "\ue00e": "CustomGlyphs.MAGNET", "\ue00f": "CustomGlyphs.EMPTY_SET",
    "\ue010": "CustomGlyphs.SUPERSCRIPT0", "\ue011": "CustomGlyphs.SUPERSCRIPT1",
    "\ue012": "CustomGlyphs.SUPERSCRIPT2", "\ue013": "CustomGlyphs.SUPERSCRIPT3",
    "\ue014": "CustomGlyphs.SUPERSCRIPT4", "\ue015": "CustomGlyphs.SUPERSCRIPT5",
    "\ue016": "CustomGlyphs.SUPERSCRIPT6", "\ue017": "CustomGlyphs.SUPERSCRIPT7",
    "\ue018": "CustomGlyphs.SUPERSCRIPT8", "\ue019": "CustomGlyphs.SUPERSCRIPT9",
    "\ue01a": "CustomGlyphs.SUBSCRIPT0", "\ue01b": "CustomGlyphs.ALEPH", "\ue01c": "CustomGlyphs.OMEGA",
    "\ue01d": "CustomGlyphs.FIXED_JAPANESE_OPENING_QUOTE", "\ue01e": "CustomGlyphs.BRIMSTONE",
    "\ue01f": "CustomGlyphs.CIRCLE_CROSS", "\ue020": "CustomGlyphs.SUBSCRIPT_QUESTION_MARK",
}
GLYPH_VALUES = {v: k for k, v in GLYPHS.items()}


def materialsinit_source():
    return subprocess.run(
        ["git", "show", "master:src/main/java/gregtech/loaders/materials/MaterialsInit.java"],
        cwd=REPO, capture_output=True, text=True, encoding="utf-8", check=True).stdout


def extract_balanced(s, start_idx):
    depth = 0
    i = start_idx
    while i < len(s):
        c = s[i]
        if c == "(":
            depth += 1
        elif c == ")":
            depth -= 1
            if depth == 0:
                return s[start_idx + 1:i]
        i += 1
    raise ValueError("Unbalanced parens")


def split_top_level_comma(arg_text):
    depth = 0
    in_string = False
    for i, c in enumerate(arg_text):
        if c == '"' and (i == 0 or arg_text[i - 1] != "\\"):
            in_string = not in_string
        elif not in_string:
            if c in "([":
                depth += 1
            elif c in ")]":
                depth -= 1
            elif c == "," and depth == 0:
                return arg_text[:i].strip(), arg_text[i + 1:].strip()
    return arg_text.strip(), None


def extract_overrides(src):
    """Every legacy setChemicalFormula call: material name -> (expression, localized, load method name)."""
    overrides = {}
    starts = [m.start() for m in re.finditer(r"\n    private static Materials load\w*\(", src)]
    starts.append(len(src))
    for a, b in zip(starts, starts[1:]):
        chunk = src[a:b]
        method = re.search(r"private static Materials (load\w*)\(", chunk)
        name = re.search(r'\.setName\(\s*"([^"]+)"\s*\)', chunk)
        if not name:
            continue
        call = re.search(r"\.setChemicalFormula\(", chunk)
        if not call:
            continue
        arg = extract_balanced(chunk, call.end() - 1)
        expr, localized_expr = split_top_level_comma(arg)
        expr = re.sub(r"\s+", " ", expr)
        overrides[name.group(1)] = (expr, localized_expr == "true", method.group(1))
    return overrides


def assignment_plan(src):
    """The MaterialsInit load order as (field, load method) pairs, plus field -> declared material name."""
    assigns = re.findall(r"Materials\.(\w+) = (load\w+)\(\);", src)
    method_names = {}
    starts = [m.start() for m in re.finditer(r"\n    private static Materials load\w*\(", src)]
    starts.append(len(src))
    for a, b in zip(starts, starts[1:]):
        chunk = src[a:b]
        method = re.search(r"private static Materials (load\w*)\(", chunk)
        name = re.search(r'\.setName\(\s*"([^"]+)"\s*\)', chunk)
        if method and name:
            method_names[method.group(1)] = name.group(1)
    field_to_name = {field: method_names[method] for field, method in assigns if method in method_names}
    ordered_names = [method_names[method] for _, method in assigns if method in method_names]
    return field_to_name, ordered_names


def java_unescape_string(lit):
    """The Python value of a Java string literal body (no surrounding quotes)."""
    out = []
    i = 0
    while i < len(lit):
        c = lit[i]
        if c == "\\":
            n = lit[i + 1]
            if n == "u":
                out.append(chr(int(lit[i + 2:i + 6], 16)))
                i += 6
                continue
            out.append({"n": "\n", "t": "\t", "\\": "\\", '"': '"', "'": "'"}.get(n, n))
            i += 2
            continue
        out.append(c)
        i += 1
    return "".join(out)


def evaluate_expr(expr, formulas, field_to_name):
    """The runtime value of a formula concatenation expression, or None when it uses constructs this
    flattener does not handle (EnumChatFormatting, kept verbatim instead). `formulas` must already carry
    the final value of every referenced material -- references only ever point at materials declared
    earlier in the legacy load order, so an in-order pass over that order guarantees it."""
    if "EnumChatFormatting" in expr:
        return None
    parts = []
    for token in split_concat(expr):
        if token.startswith('"'):
            parts.append(java_unescape_string(token[1:-1]))
        elif token in GLYPH_VALUES:
            parts.append(GLYPH_VALUES[token])
        else:
            ref = re.fullmatch(r"Materials\.(\w+)\.getChemicalFormula\(\)", token)
            if not ref:
                raise ValueError(f"Unhandled token in formula expression: {token}")
            parts.append(formulas[field_to_name[ref.group(1)]])
    return "".join(parts)


def split_concat(expr):
    tokens = []
    depth = 0
    in_string = False
    current = []
    i = 0
    while i < len(expr):
        c = expr[i]
        if c == '"' and (i == 0 or expr[i - 1] != "\\"):
            in_string = not in_string
            current.append(c)
        elif not in_string and c == "(":
            depth += 1
            current.append(c)
        elif not in_string and c == ")":
            depth -= 1
            current.append(c)
        elif not in_string and depth == 0 and c == "+":
            tokens.append("".join(current).strip())
            current = []
        else:
            current.append(c)
        i += 1
    tokens.append("".join(current).strip())
    return tokens


def java_string_literal(value):
    """A Java expression (string literal, possibly concatenated with CustomGlyphs refs) for `value`."""
    parts = []
    buf = []

    def flush():
        if buf:
            parts.append('"' + "".join(buf) + '"')
            buf.clear()

    for c in value:
        if c in GLYPHS:
            flush()
            parts.append(GLYPHS[c])
        elif c == '"':
            buf.append('\\"')
        elif c == "\\":
            buf.append("\\\\")
        elif c == "§" or 0xE000 <= ord(c) <= 0xF8FF:
            buf.append(f"\\u{ord(c):04X}")
        else:
            buf.append(c)
    flush()
    return " + ".join(parts) if parts else '""'


def m2m_blocks():
    """Per-material declaration info from Materials2Materials.java, in declaration order:
    (field, regname, legacy_name, has_werkstoff)."""
    text = M2M.read_text(encoding="utf-8")
    stmts = list(
        re.finditer(r'Materials2Materials\.(\w+) = MaterialLibAPI\.newMaterial\("gregtech", "((?:[^"\\]|\\.)*)"', text))
    blocks = []
    for i, m in enumerate(stmts):
        end = stmts[i + 1].start() if i + 1 < len(stmts) else len(text)
        body = text[m.start():end]
        legacy = re.search(r'LEGACY_NAME, "((?:[^"\\]|\\.)*)"', body)
        blocks.append({
            "field": m.group(1),
            "regname": m.group(2),
            "legacy_name": java_unescape_string(legacy.group(1)) if legacy else java_unescape_string(m.group(2)),
            "werkstoff": "GTMaterialProperties.WERKSTOFF" in body,
        })
    return blocks


def main():
    dump = json.loads((DUMP_DIR / "gt-materials.json").read_text(encoding="utf-8"))
    dump_formulas = {e["name"]: e.get("chemicalFormula") for e in dump}

    src = materialsinit_source()
    overrides = extract_overrides(src)
    field_to_name, ordered_names = assignment_plan(src)
    blocks = m2m_blocks()

    # Replay every evaluable override in the legacy load order so a cross-material reference always sees
    # the exact value the legacy concatenation saw, including references to materials whose own formula
    # was itself an override (e.g. the proto-halkonite chain).
    formulas = dict(dump_formulas)
    flattened = {}
    for name in ordered_names:
        if name not in overrides:
            continue
        expr, _, _ = overrides[name]
        value = evaluate_expr(expr, formulas, field_to_name)
        if value is not None:
            formulas[name] = value
            if "Materials." in expr and ".getChemicalFormula()" in expr:
                flattened[name] = value

    entries = []  # (regname, java_expr, localized)
    stats = {"override_verbatim": 0, "override_flattened": 0, "derived": 0,
             "skipped_placeholder": 0, "skipped_werkstoff": 0, "skipped_not_dumped": 0}
    uses_glyphs = False
    uses_ecf = False

    for block in blocks:
        legacy_name = block["legacy_name"]
        if block["werkstoff"]:
            stats["skipped_werkstoff"] += 1
            continue
        if legacy_name in overrides:
            expr, localized, _ = overrides[legacy_name]
            if legacy_name in flattened:
                entries.append((block["regname"], java_string_literal(flattened[legacy_name]), localized))
                stats["override_flattened"] += 1
            else:
                entries.append((block["regname"], expr, localized))
                stats["override_verbatim"] += 1
        else:
            formula = dump_formulas.get(legacy_name)
            if formula is None:
                stats["skipped_not_dumped"] += 1
                continue
            if formula in ("", "?"):
                stats["skipped_placeholder"] += 1
                continue
            entries.append((block["regname"], java_string_literal(formula), False))
            stats["derived"] += 1

    for _, expr, _ in entries:
        if "CustomGlyphs." in expr:
            uses_glyphs = True
        if "EnumChatFormatting." in expr:
            uses_ecf = True

    lines = []
    lines.append("package gregtech.api.enums.materials2;")
    lines.append("")
    if uses_ecf:
        lines.append("import net.minecraft.util.EnumChatFormatting;")
        lines.append("")
    lines.append("import com.ruling_0.materiallib.api.MaterialLibAPI;")
    lines.append("")
    lines.append("import gregtech.api.material.GTMaterialProperties;")
    if uses_glyphs:
        lines.append("import gregtech.api.util.CustomGlyphs;")
    lines.append("")
    lines.append(
        "// One-time output of scripts/mu/add_chemical_formulas.py (RETIRED, see its module docstring); hand-maintained")
    lines.append("// from here -- edit this file directly.")
    lines.append(
        "/// The chemical-formula maintenance surface: one [GTMaterialProperties#FORMULA] entry per ported gregtech")
    lines.append(
        "/// material whose legacy `Materials#mChemicalFormula` was a real formula -- the explicit `MaterialsInit`")
    lines.append(
        "/// overrides verbatim (cross-material concatenations flattened to the literal the legacy loader computed,")
    lines.append(
        "/// [GTMaterialProperties#FORMULA_LOCALIZED] added where the legacy call used its localized overload), every")
    lines.append(
        "/// other material's constructor-derived string as pinned data. Werkstoff-composite materials carry no entry:")
    lines.append(
        "/// their formula lives in `WerkstoffData#formula` (see `MaterialFormulas`). Kept separate from")
    lines.append(
        "/// [Materials2Materials] so its hand-maintained declaration blocks stay uncluttered by ~1200 formula lines.")
    lines.append("public class Materials2Formulas {")
    lines.append("")
    lines.append("    // spotless:off")

    n_parts = (len(entries) + CHUNK - 1) // CHUNK
    lines.append("    public static void init() {")
    for i in range(n_parts):
        lines.append(f"        initPart{i + 1}();")
    lines.append("    }")
    lines.append("")

    for part in range(n_parts):
        chunk = entries[part * CHUNK:(part + 1) * CHUNK]
        lines.append(f"    private static void initPart{part + 1}() {{")
        for regname, expr, localized in chunk:
            escaped = regname.replace("\\", "\\\\").replace('"', '\\"')
            lines.append(f'        MaterialLibAPI.editMaterial("gregtech", "{escaped}")')
            lines.append(f"            .setProperty(GTMaterialProperties.FORMULA, {expr})")
            if localized:
                lines.append("            .setProperty(GTMaterialProperties.FORMULA_LOCALIZED, true)")
            lines[-1] = lines[-1] + ";"
        lines.append("    }")
        lines.append("")

    lines.append("    // spotless:on")
    lines.append("")
    lines.append("    private Materials2Formulas() {}")
    lines.append("}")

    OUT.write_text("\n".join(lines) + "\n", encoding="utf-8")
    print(f"Wrote {OUT}")
    print(f"Entries: {len(entries)} across {n_parts} parts; stats: {stats}")


if __name__ == "__main__":
    main()
