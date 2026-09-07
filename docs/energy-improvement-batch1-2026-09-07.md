# Energy improvements: first batch, 2026-09-07

## Scope and baseline

Implemented consolidated-plan items 0–4 only. Source baseline: `2ae269ae497a51fc3d6fe61993b46178fb8cd077`, initially clean. This is different from the earlier audit revision and from the installed-pack GT artifact; neither is used as a substitute for this source baseline.

The eight supporting files in `build/energy-improvement-2026-09-07/` were copied to a temporary backup before Gradle ran. SHA-256 comparisons after implementation confirmed all eight unchanged. No clean task was run. Focused baseline JUnit XML is retained locally in `build/energy-batch-baseline-results/`; build outputs are disposable, so the durable results are recorded here.

Environment: Windows, Temurin OpenJDK 25.0.3+9 LTS, Gradle wrapper 9.4.0, Minecraft 1.7.10 / Forge 10.13.4.1614, repository JVM Downgrader configuration targeting Java 8 with multi-release output. Existing JUnit 5 and Mockito infrastructure was used without additional dependencies or production instrumentation.

Before production edits, the full unit suite passed 242 tests, including the initial 12 focused energy tests. Two additional LSC cases were then added: all six hatch loops and wireless rebalance/maintenance ordering. All 14 focused tests passed against unchanged production code. Fixture development initially encountered final-field assignments, a Mockito stubbing-induced trace entry, and Forge launcher initialization; these were fixed in the tests before production edits. The original Python checks also passed: 1,800 bounded packet-storage cases and the existing counterexamples.

## Independently reviewable changes

| Plan item | Production file | Change and observed work reduction |
|---|---|---|
| 1 | `src/main/java/gregtech/api/metatileentity/TileIC2EnergySink.java` | Pass null only when the currently dispatched object's exact class is `MTECable` or `GTPPMTECable`. Their inherited implementation ignores the set. This removes the seeded traversal-set construction per supported injection; tests inspect the dispatched argument. Unknown subclasses still receive a fresh mutable set containing the base tile, including after a meta replacement. |
| 2 | `src/main/java/gregtech/api/graphs/GenerateNodeMap.java` | Check disconnected sides before neighbor resolution. The one-connected-side fixture goes from **6 lookups to 1**. Direction order, excluded direction and null-meta behavior remain intact. |
| 3 | `src/main/java/kekztech/common/tileentities/MTELapotronicSuperCapacitor.java` | Reuse the existing fixed input/output limit calculations once per running tick with hatches in that direction. The fixture with one input in each of the three input families goes from **3 capacity subtractions to 1**. Output limiting is likewise computed once rather than per output hatch. Hatch wattage calls and deferred net accounting retain their order. |
| 4 | `src/main/java/gregtech/api/metatileentity/implementations/MTECable.java` | On an already-energized node, create the selection array only after the first eligible consumer. With no eligible consumer, return before constructing the array/cursor or calling the router. The blocked fixture goes from **1 cursor construction to 0**; source inspection confirms the array allocation is also bypassed. The eligibility scan still happens on each offer. |

The production changes occupy separate files and do not depend on one another. No public signatures changed. First-energization selection still includes every consumer and dead end. Nonempty selections remain ordered snapshots completed before callbacks; selection state remains local to each synchronous call.

The LSC's zero input limit explicitly remains zero even for negative overflowed hatch wattage. Replacing the original full-capacity return with an unconditional `min(watts, 0)` would change behavior. Wattage callbacks are still evaluated in that case. This is preservation of an existing boundary, not an overflow fix.

## Focused production regression coverage

Tests live in `EnergyTransferTest`, `EnergyGraphConstructionTest`, and `LSCEnergyTransferTest` under `src/test/java/`.

- Production cable transfer/router/path calls: ordered predicates before injection, accepted amps, partial IC2 remainder, voltage clamping, GT and GT++ overload/fire callbacks, dead-end first energization, later empty selection, nested injection, and later eligibility without a server-tick change.
- Native receiver adapter and production receiver acceptance/reset methods: one accepted amp, rejection on the next offer, and acceptance after the receiver's own electric update without advancing the mocked server clock. Storage/world services are mocked.
- Production graph construction: DFS IDs and consumer order, branches, discarded loop closure, compressed cable membership/loss, graph clearing, a mocked chunk-border layout, disconnected-side lookup count, and null-meta/excluded-side behavior.
- Production LSC running ticks: all six hatch families, simultaneous input/output, empty/full/over-capacity and beyond-`Long.MAX_VALUE` balances, low headroom, per-hatch writes, final balance clamp, transfer diagnostics, wireless rebalance before net delta, maintenance loss, rolling statistics, and full input with overflowed negative wattage.

Work-count expectations were changed only after confirming their old values against baseline production. These tests execute production methods with narrowly mocked surroundings; they are not duplicated Python transfer algorithms or live Minecraft integration tests.

## Validation results

Post-change unit suite: **244 tests passed, zero failures/errors**, including all 14 focused energy cases. `gradlew.bat test spotlessCheck checkstyleMain checkstyleTest --console=plain` completed successfully: production/test compilation, bytecode downgrading, unit tests, formatting checks and both Checkstyle tasks passed.

`py -3 build/energy-improvement-2026-09-07/energy-audit-checks.py` passed again after edits. `git diff --check` passed. Final review checked the four production diffs against the approved scope, the unchanged helpers/router, and the focused regression expectations.

## Limitations and remaining measurements

No copied benchmark world or live server profiling run was performed. There are **no measured TPS/MSPT gains, allocation-byte rates or CPU-time improvements** in this delivery. The counts above establish removed work in controlled fixtures; they do not quantify pack-wide benefit. A full foreign AE2 endpoint still qualifies for selection, so this patch does not eliminate that case's selection storage or AE2 internal work.

Mocked chunk-border neighbors do not prove real chunk unload/reload behavior. Receiver-local testing invokes the production electric-update method with mocked world/storage services, not an entire server tick. The broader foreign protocol/lifecycle matrix from item 7 remains future work. Wireless storage is mocked at its existing boundary while the LSC's rebalance and final accounting run normally.

Remaining benchmark steps:

1. Build matching artifacts from this source baseline and the completed batch. Record artifact hashes, actual GT/AE2/mod/JVM versions and configuration; keep the supplied pack's installed versions separately identified.
2. On identical copies of a benchmark world, hold loaded chunks, warmup and run duration constant. Include a single ordinary network, branching/many-source networks with blocked and draining native receivers, IC2 injections, topology churn, and hatch-heavy LSCs near empty/full with simultaneous input/output. Record rebuild work separately from steady-state injection.
3. Capture repeated CPU/allocation profiles, median/p95/p99 MSPT, ticks over 50 ms, allocation rate and GC pauses. Compare run-to-run variation. Verify the removed set/array/cursor allocations in profiler stacks and count LSC hatches/limit calculations; do not infer headroom from 20 TPS alone.
4. Establish the deferred AE2 counting scenario with quartz-fiber depths 1, 2, 4, 8, 16 and 32 plus a single-grid control, counting SIMULATE/MODULATE/provider calls and set copying. AE2 code remains unchanged in this batch.

## Findings kept outside scope and next batch

The known LSC deferred-budget issue remains: multiple hatches can collectively offer more than the starting balance, followed by the final zero clamp. The all-six-loop fixture records current behavior for performance equivalence, not a permanent correctness contract. A separately approved accounting fix should replace that expectation with its corrected behavior. Hatch wattage overflow also remains untouched; the full-capacity test prevents this optimization from changing its existing zero-input behavior. No additional correctness fix was silently included.

Next recommended implementation batch: item 5, iterate only the linear cable-run walk, with long-run/bend/loop/branch termination stress coverage and identical DFS/path results. Continue the item 7 consumer/lifecycle groundwork alongside it. Item 6 dynamo batching needs its own production-hatch equivalence and callback tests. No AE2 change, new API, shortest-path routing, persistent cache, commit or push is included here.
