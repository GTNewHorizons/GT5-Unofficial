#!/usr/bin/env python3
"""Checks the two family invariants that nothing else enforces.

1. Every `removeShape` in `Materials2Materials` targets a shape one of that material's families
   actually contributes. MaterialLib cannot warn about this: `Material.removeShape` unconditionally
   records the mask, so a removal whose shape no family supplies is a silent no-op. Harmless on the day
   it is written, and a landmine the day that shape joins a family the material belongs to.

2. Exactly one `gregtech` family declares properties. Family property lookup takes the first family in
   `modid:name` order, so a second property-carrying family would win or lose by name rather than by
   intent. `All` sorts ahead of every other gregtech family, which is only correct while it is alone.

Usage: python scripts/mu/check_families.py
Exits non-zero on a violation.
"""

import re
import sys
from pathlib import Path

REPO_ROOT = Path(__file__).resolve().parent.parent.parent
MATERIALS2 = REPO_ROOT / "src/main/java/gregtech/api/enums/materials2"


def family_shapes():
    """Each family's declared shape set, parsed from the `newFamily(...)...build()` chains."""
    text = (MATERIALS2 / "Materials2Families.java").read_text(encoding="utf-8")
    families, properties = {}, []
    for chain in re.findall(r'newFamily\("gregtech", "(\w+)"\)(.*?)\.build\(\);', text, re.S):
        name, body = chain
        field = name[0].lower() + name[1:]
        families[field] = set(re.findall(r"Materials2\w+\.(\w+)", body))
        if ".setProperty(" in body:
            properties.append(name)
    return families, properties


def declarations():
    """Each material's family list and removeShape list."""
    text = (MATERIALS2 / "Materials2Materials.java").read_text(encoding="utf-8")
    out = []
    for chain in re.findall(r'newMaterial\("gregtech", "([^"]+)"(.*?)\.build\(\);', text, re.S):
        name, body = chain
        out.append(
            (
                name,
                re.findall(r"\.addToFamily\(Materials2Families\.(\w+)\)", body),
                re.findall(r"\.removeShape\(Materials2\w+\.(\w+)\)", body),
            )
        )
    return out


def main():
    families, property_carriers = family_shapes()
    failures = []

    if len(property_carriers) != 1:
        failures.append(
            f"expected exactly one property-carrying gregtech family, found {len(property_carriers)}: "
            f"{property_carriers}"
        )

    unreachable = []
    for name, fams, removals in declarations():
        contributed = set()
        for f in fams:
            contributed |= families.get(f, set())
        for shape in removals:
            if shape not in contributed:
                unreachable.append((name, shape))
    if unreachable:
        failures.append(f"{len(unreachable)} removeShape calls target a shape no family contributes")

    print(f"families: {len(families)}   property carriers: {property_carriers}")
    print(f"materials checked: {len(declarations())}")
    print(f"unreachable removeShape calls: {len(unreachable)}")
    for u in unreachable[:25]:
        print(f"   {u[0]}: {u[1]}")
    if len(unreachable) > 25:
        print(f"   ... and {len(unreachable) - 25} more")

    if failures:
        print("\nFAILED:")
        for f in failures:
            print(f"  {f}")
        return 1
    print("\nOK")
    return 0


if __name__ == "__main__":
    sys.exit(main())
