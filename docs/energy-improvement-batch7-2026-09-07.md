# Energy batch 7: routing contract and loss-accounting baseline

## Delivered scope

This is item 10, patch 1 of the approved consolidated plan: executable shortcut/tie/weight examples and an explicit accounting/energization contract. Production routing remains DFS. There is no alternate-edge graph, production shortest-path search, entry cache or new API in this patch.

No Minecraft server/client process was running at inspection. The checkout has devworld and NEI development saves, but no established representative energy benchmark workload. Live AE2 CPU/allocation/MSPT profiling remains outstanding; merely launching a development save would not produce the required comparable workload. This batch proceeds with the next planned code preparation, without claiming a new performance gain.

## Executable examples

EnergyRoutingContractTest uses actual MTECable, GenerateNodeMapPower, PowerNodes and PowerNodePath with controlled physical-neighbor tiles and a native GT receiver. The small test-only oracle enumerates simple physical cable paths independently of the compressed DFS graph. It is deliberately unsuitable for production-sized graphs. No tests are disabled.

The shortcut example has entry loss 1, terminal cable loss 2, three lower-route cables of loss 3 each, and a one-cable shortcut of loss 1. At 128 V:

| Topology/result | Loss | Receiver voltage |
|---|---:|---:|
| Lower route only, current production | 12 | 116 |
| Shortcut added and graph rebuilt, current production | 12 | 116 |
| Required minimum-loss route with shortcut | 4 | 124 |
| Lower entry edge disconnected and rebuilt, current production | 4 | 124 |

The test asserts both the future oracle result and the current production mismatch. The latter is a temporary characterization to replace with equality to the oracle when transfer integration lands, not a requirement to keep DFS routing. Construction currently discards the shortcut's run before it gets a NodePath.

Further examples require a longer low-loss route to beat a shorter high-loss one; different cable entries to select their own route; equal loss to prefer fewer cable blocks; equal loss/length to compare coordinates; and zero-loss cycles to yield simple acyclic paths. Reversing enumeration order must not affect selection.

## Route weights and deterministic ordering

A route contains every physical cable from the injection cable through the final cable adjacent to the receiving endpoint, inclusive, once. A machine endpoint contributes zero cable loss. Received voltage equals offered voltage minus this total, using the existing receiver's behavior at the resulting voltage; this patch does not fix voltage narrowing or nonpositive-packet behavior.

For compressed topology, retain ordered interior cable members and separate node/self cables. Define a directed search transition from cable node u to cable node v as interior-run loss plus v's self loss, with initial entry distance equal to the entry self loss. A transition to a machine consumer contributes only its interior-run loss. This makes entry, junction and terminal cable loss occur exactly once. Do not charge a junction as both an interior member and a self path.

This is an accounting representation for search. Existing transfer calls apply a node's self-path voltage before subtracting its loss, then apply the run voltage and subtract the run loss. Preserve those diagnostic and rating-check conventions when integrating; a different search representation must not silently change transfer-side loss placement.

Order routes by (total loss, number of physical cable blocks, lexicographic sequence of physical cable coordinates x/y/z from entry to endpoint, then receiving face ordinal). Within one dimension, adjacency between different cable coordinates determines direction; use ForgeDirection ordinal for otherwise identical endpoint/edge records. Compare integers without subtraction overflow. Consumer list priority stays independent of this route ordering.

Zero is a valid loss: native registration includes zero-loss insulated cables. Native constructors accept unrestricted long loss values; GT++ forwards those values unchanged, and GoodGenerator registration derives insulated loss with integer division. The production path sum currently wraps on overflow and does not reject negative values. Tests reproduce these facts and require the oracle to reject negative weights and checked-add overflow. They do not repair existing arithmetic.

Dijkstra is appropriate only after topology preparation validates nonnegative additive weights and checked sums. A later router must explicitly fall back to legacy transfer for an unsupported component, rather than clamp weights, treat overflow as a cheap route, or silently reject energy that legacy transfer accepted. Record that fallback as a compatibility limitation. Do not introduce a separate arithmetic correctness fix in this feature patch. A simple PriorityQueue is sufficient for the planned search; no new graph dependency is justified.

## First energization, unused loop edges and accounting

The following rules define the new feature's integration target; they are not implemented by this contract-only patch:

1. Preserve first energization for each entry after routing/topology invalidation, including full receivers and dead ends. The existing first-offer voltage effects must not depend on positive accepted amps. Subsequent offers retain ordinary consumer eligibility/priority rules.
2. First energization also visits retained loop/parallel runs that were discarded by DFS. For each available compressed run, use the greater voltage available at its reachable endpoints after each endpoint's self loss; apply that incoming voltage to the run in that direction. Compute endpoint availability from the entry's minimum-loss distances. Resolve equal voltage by stable endpoint coordinate order. Evaluate each physical run once for this pass, without circulating a pulse around zero-loss cycles.
3. Apply existing PowerNodePath voltage diagnostics and rating checks at that incoming voltage, including the current conservative run-wide voltage check. Do not silently replace that check with per-block attenuated overvoltage checks. Apply a junction self-path at its incoming voltage, and include terminal/dead-end self paths once.
4. Unused runs receive voltage effects but zero transferred amps. Charge accepted amps only to cables on the selected consumer routes, using shared physical accounting across entries and opposite directions. Do not clone amp/overload budgets into entry trees or debit the source for this diagnostic pass.
5. A locked/unavailable run is excluded from search and energization. Do not choose an alternate route merely to avoid an impending overload. If voltage effects or synchronous receiver callbacks burn/remove cables or change topology, stop using invalid routing data and observe the established update lifecycle before another transfer. Tests for this must accompany topology/transfer integration.
6. Retaining loop edges makes formerly unvisited cable voltage diagnostics and possible burns observable. Lower loss also increases delivered voltage and may expose a receiver to overvoltage. These are explicit routing feature effects to document in release notes, not evidence of performance equivalence.

Existing EnergyTransferTest and EnergyConsumerBoundaryTest cover demand-free first voltage/burning, endpoint loss/diagnostics, consumer snapshots and synchronous debit. New shortest-path energization must keep those controls and add actual loop-run, multiple-entry and mutation tests at integration. Current clearNodeMap recursively assumes a tree; simply attaching loop-closing neighbors to it would recurse indefinitely. Patch 2 must provide cycle-safe ownership/clearing before retaining alternate edges and must preserve legacy transfer until patch 4.

## Validation and handoff

Baseline: all 28 graph tests passed before editing. Supporting build reports were copied to a fresh protected temporary directory before Gradle ran; no clean task was used. Final validation: all 285 tests passed with zero failures, errors or skips, including five new routing tests. The full command was: gradlew.bat spotlessApply test spotlessCheck checkstyleMain checkstyleTest --console=plain. Formatting and both Checkstyle tasks passed with zero violations. Final diff review and whitespace checks passed. All eight supporting reports still match the protected backup by hash. The diff contains only this handoff and the new test class; nothing was pushed.

Next code batch is item 10, patch 2: retain alternate compressed edges with ordered physical membership, shared ownership and cycle-safe lifecycle tests while leaving legacy transfer selection active. This contract does not authorize skipping that staging step. Live baseline/candidate AE2 profiling remains a separate outstanding measurement task; no TPS, allocation-byte or CPU gain is claimed here.
