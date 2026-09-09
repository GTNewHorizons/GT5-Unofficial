#!/usr/bin/env python3
"""Generate the useless recipe removal callsite report from its log."""

import argparse
import re
from collections import Counter, defaultdict
from pathlib import Path


ROOT = Path(__file__).resolve().parent
DEFAULT_LOG = ROOT / "UselessRecipeRemovals.log"
DEFAULT_OUTPUT = ROOT / "UselessRecipeRemovals.md"

TARGET = "No existing removable crafting recipe matched these queued inputs; removal call site follows"
ADD_METHODS = {"addCraftingRecipe", "addShapelessCraftingRecipe"}
MACHINE_METHOD = "addMachineCraftingRecipe"
FRAME = re.compile(
    r"^\s*at\s+(?:(?:[^/\s]+)//)?(?P<class>[A-Za-z0-9_.$]+)\."
    r"(?P<method>[^\s(]+)\((?P<source>[^)]*)\)"
)
SOURCE_LINE = re.compile(r":(?P<line>\d+)$")


def classify(frames):
    removal = next((i for i, frame in enumerate(frames) if frame[1] == "removeRecipeDelayed"), None)
    if removal is None:
        return "unresolved", None

    caller = removal + 1
    while caller < len(frames) and frames[caller][1] == "removeRecipeDelayed":
        caller += 1
    if caller >= len(frames):
        return "unresolved", None

    if frames[caller][1] in ADD_METHODS:
        while caller < len(frames) and frames[caller][1] in ADD_METHODS:
            caller += 1
        while caller < len(frames) and frames[caller][1] == MACHINE_METHOD:
            caller += 1
        return ("crafting", frames[caller]) if caller < len(frames) else ("unresolved", None)

    return "direct", frames[caller]


def record(callsites, frame):
    source_line = SOURCE_LINE.search(frame[2])
    if not source_line:
        return False
    callsites[frame[0]][int(source_line.group("line"))] += 1
    return True


def parse(log_path):
    callsites = {
        "crafting": defaultdict(Counter),
        "direct": defaultdict(Counter),
    }
    counts = Counter()
    active = False
    frames = []

    def finish_entry():
        nonlocal frames
        if active:
            category, frame = classify(frames)
            if frame is None or category not in callsites or not record(callsites[category], frame):
                counts["unresolved"] += 1
            else:
                counts[category] += 1
        frames = []

    with log_path.open("r", encoding="utf-8", errors="replace") as log:
        for raw_line in log:
            if TARGET in raw_line:
                finish_entry()
                counts["target"] += 1
                active = True
                continue
            if raw_line.startswith("["):
                finish_entry()
                active = False
                continue
            if not active:
                continue
            frame = FRAME.match(raw_line)
            if frame:
                frames.append((frame.group("class"), frame.group("method"), frame.group("source")))
    finish_entry()

    if counts["target"] != counts["crafting"] + counts["direct"] + counts["unresolved"]:
        raise RuntimeError("entry accounting mismatch")
    return callsites, counts


def append_section(lines, title, callsites):
    class_totals = {class_name: sum(entries.values()) for class_name, entries in callsites.items()}
    total = sum(class_totals.values())
    label = "entry" if total == 1 else "entries"
    lines.extend((f"## {title} ({total:,} {label})", ""))

    for class_name in sorted(callsites, key=lambda name: (-class_totals[name], name)):
        class_total = class_totals[class_name]
        class_label = "entry" if class_total == 1 else "entries"
        lines.extend((f"### `{class_name}` ({class_total:,} {class_label})", ""))
        for line, occurrences in sorted(callsites[class_name].items()):
            # Report convention: duplicate count means total occurrences for the callsite.
            suffix = f" ({occurrences} duplicates)" if occurrences > 1 else ""
            lines.append(f"- Line `{line}`{suffix}")
        lines.append("")
    return total


def write_report(output_path, callsites):
    lines = ["# Useless Recipe Removal Callsites", ""]
    crafting_total = append_section(
        lines,
        "Crafting Recipe Calls via `addCraftingRecipe` / `addShapelessCraftingRecipe`",
        callsites["crafting"],
    )
    direct_total = append_section(lines, "Direct `removeRecipeDelayed` Calls", callsites["direct"])
    output_path.write_text("\n".join(lines), encoding="utf-8", newline="\n")
    return crafting_total, direct_total


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("log", nargs="?", type=Path, default=DEFAULT_LOG)
    parser.add_argument("output", nargs="?", type=Path, default=DEFAULT_OUTPUT)
    args = parser.parse_args()

    callsites, counts = parse(args.log)
    crafting_total, direct_total = write_report(args.output, callsites)
    if (crafting_total, direct_total) != (counts["crafting"], counts["direct"]):
        raise RuntimeError("report total mismatch")

    print(f"Wrote {args.output}")
    print(f"Target entries: {counts['target']:,}")
    print(f"Crafting entries: {crafting_total:,}")
    print(f"Direct entries: {direct_total:,}")
    print(f"Unresolved entries: {counts['unresolved']:,}")


if __name__ == "__main__":
    main()
