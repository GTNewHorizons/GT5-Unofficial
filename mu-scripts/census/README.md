# Recipe census

The census compares every recipe GregTech and the fleet register on `master` with the same pack built from `mu`.
Two headless servers (one per branch) boot with the HorizonQA test `InterchangeCensus`, which dumps every recipe map,
the crafting table, furnace, item registry, ore dictionary and what each recipe input slot accepts. `gate.py` then
keys every recipe by what it produces and reports what `mu` lost.

## Layout

| Path | Contents |
| --- | --- |
| `env.sh` | Workspace, results and Java locations shared by the shell scripts; every one can be overridden from the environment |
| `probe/` | `InterchangeCensus.java` and the per-branch `CensusAdapter` sources `build_gt.sh` copies into a GT checkout |
| `results/` | Written by the scripts and ignored by git: `census-master/`, `census-mu-<label>/`, `gate-<census>.txt`, `gate-stdout-<label>.txt`, `latest-mu.txt` |
| `accepted-drift.tsv` | Accepted findings: view, map glob, key regex, reason |
| `repos.txt` | Fleet repos whose master builds replace the mu jars on `server-master` |
| `legacy/` | The earlier stage-by-stage census (`RecipeCensusDump`), kept for comparing old stage censuses |

## Workspace

`CENSUS_WORK` (default `work/` here, ignored by git) holds what the scripts drive:

```
server-master/   headless GTNH server with the master fleet (lwjgl3ify-forgePatches.jar, java9args.txt, HorizonQA)
server-mu/       the same server with the mu fleet, MaterialLib and Postea
src/<repo>/      master checkouts of GT5-Unofficial and every repo in repos.txt
src-mu/GT5-Unofficial/   a mu checkout (or worktree) census_cmp.sh can detach at any commit
logs/            build and boot logs
```

Both servers boot on Java 25 (`JAVA25`). `JAVA21_HOME` is only needed for Roguelike-Dungeons. `GTNH_ROOT` is the
directory holding the mu fleet checkouts, defaulting to the parent of this GT checkout.

## Usage

All scripts run under Git Bash; the Python scripts need Python 3.

Rebuild the master baseline after a master merge or a probe change:

```
./build_gt.sh master && cp "$CENSUS_WORK/src/GT5-Unofficial/build/libs/gregtech-5.99.99-census.jar" "$CENSUS_WORK/server-master/mods/"
./swap_master_mods.sh          # after building the master fleet with build_one.sh <repo>
./boot.sh master               # writes results/census-master
```

Census one mu commit, gate it and diff it against an earlier mu census:

```
./census_cmp.sh <commit> <label> <baseline label>
```

This writes `results/census-mu-<label>`, `results/gate-census-mu-<label>.txt` and `results/gate-stdout-<label>.txt`,
then prints every census file that changed against `census-mu-<baseline label>` (`noisy` marks maps that vary
between boots of the same build).

The analysis scripts read `results/` (or `CENSUS_RESULTS`) and take a mu census name, defaulting to `latest-mu.txt`:

| Script | Purpose |
| --- | --- |
| `gate.py [census] [--master DIR] [--allow FILE] [--out FILE]` | Output-keyed loss report; exits 1 when a gating view has unexplained findings |
| `gate_clusters.py <report> <view> [top N]` | Groups one view of a gate report for triage |
| `gains.py <census>` | Recipes mu has that master lacks |
| `audit_changes.py <census>` | Classifies changed-recipe rows by the field that differs |
| `norm_cmp.py <file> <file>` | Order-insensitive comparison of one map between two censuses |
| `compare.py <master dir> <mu dir> <out dir>` | Older input-keyed comparison with acceptance and oredict diffs |
| `analyze_accept.py`, `analyze_groups.py`, `analyze_recipes.py`, `dupinputs.py` | Breakdowns of a `compare.py` run; run `compare.py results/census-master results/<census> results/compare-<census>` first |

The gating views are lost-product, lost-map-product, empty-ore-name, ore-name-leaver, duplicate-input, cost-shift and
lost-output-less; lost-output-set, count-drop and changed-recipe are informational. `gate.py`'s docstring defines
each view. A finding the census should accept goes into `accepted-drift.tsv` with the reason; rules prefixed `OPEN`
are undecided.
