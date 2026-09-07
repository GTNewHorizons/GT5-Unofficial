# Energy batch 8: retain alternate compressed topology

## Scope

This implements item 10, patch 2 of the consolidated plan, following routing-contract commit ac8bcd9414. Alternate physical runs are retained as topology metadata; legacy DFS transfer is still active. No shortest-path preparation, entry cache, new consumer API, alternate-route transfer, new energization pass or separate arithmetic/lifecycle correctness fix is included.

## Representation and compatibility

Node now has a nullable package-private list of alternate edges. Each edge records its two existing endpoint nodes, both endpoint faces and the ordered interior MetaPipeEntity array from first endpoint to second. Endpoint self cables are excluded, matching batch 7's weight contract. An edge object is shared by both endpoints; a self-loop is stored once. Endpoint faces distinguish parallel runs, including a direct edge with an empty interior. Reverse discovery of the same physical run does not allocate a second retained edge.

GenerateNodeMapPower captures a loop closure during its existing degree-two walk, after checking the visited endpoint's reciprocal connection. The protected walk still returns null for an already visited node. The DFS neighbor arrays, node IDs, consumer order, existing paths and locks are unchanged. Only power graph generation retains this metadata. Invalid or replaced interior cable instances are excluded from the new records.

Existing tree paths and self paths remain the sole owners of transfer counters. Alternate records do not construct a PowerNodePath, bind a node/path onto an interior tile, reload its locks, energize it or create a second amp budget. This deliberately preserves the existing scanner/transfer behavior of formerly discarded runs while making their physical topology available to the next stage. The combined tree paths, self paths and alternate records account for each cable once within a generated snapshot, tested from both ends of the loop.

The representative 128 V shortcut case continues to deliver 116 V through the legacy route, even though the retained topology and test oracle identify the future 124 V route. This is the required staging behavior, not completion of shortest-path routing.

## Cleanup and lifecycle

clearNodeMap retains its public signature and existing tree traversal/return-node semantics. A per-clear identity visited set prevents recursive cycles. Clearing an endpoint detaches each alternate edge from both endpoint lists without traversing that edge; this handles self-loops and parallel edges and avoids clearing an excluded parent during a partial clear. Empty metadata lists are released. No alternate path binding is cleared because none was installed.

Tests cover clearing from either endpoint, repeat clearing, partial clearing, a cycle in neighbor arrays, and rebuilding after an invalid cable, missing neighbor, replacement cable or lost reciprocal connection. The missing-neighbor case is a controlled stand-in for removal/unavailability at rebuild time; it is not a live chunk unload test. The pre-existing production chunk-lifecycle and same-tick rebuild tests remain part of validation.

The representation is a snapshot, not an automatically maintained availability cache. Covers and existing locks can change without a topology rebuild, so retained edges must not be treated as traversable solely because a record exists. Future preparation/integration must validate availability and invalidate prepared routes on the prescribed events. The known middle-chunk stale-graph behavior and the existing source promotion/rebuild lifecycle are not silently repaired here. No caller uses alternate records to transfer energy yet.

Cleanup is cycle-safe but still recursive over compressed nodes, preserving the legacy order. Very deep junction trees retain their existing stack-depth limitation; this patch does not claim general iterative graph construction/cleanup.

## Validation and cost

All 33 existing graph tests passed before editing. The eight supporting files under build/energy-improvement-2026-09-07 were copied to a new protected temporary directory before Gradle ran. No clean task was used.

Eight new production-code regression tests cover shared endpoint ownership and unchanged delivery; ordered parallel runs; a degree-two self-loop; direct adjacent loop endpoints; invalid/unavailable/replaced/one-way runs; partial cleanup; explicit cyclic cleanup; and exactly-once physical membership when rebuilding from either entry. The existing shortcut contract, long-run compression, energy accounting, first-voltage, override, re-entry and lifecycle tests remain active.

Final validation: all 293 tests passed with zero failures, errors or skips, including all 41 focused graph tests. The full command was gradlew.bat spotlessApply test spotlessCheck checkstyleMain checkstyleTest --console=plain. Formatting and both Checkstyle tasks passed with zero violations. Final diff review and whitespace checks passed. All eight supporting files still match their protected backup by hash.

Retained memory is proportional to alternate edges and their interior members, plus one nullable reference per node. Only nodes incident to an alternate edge allocate a list. Capturing the records reuses the existing walk and neighbor lookups, with an additional validity scan over each newly retained interior run. Cleanup adds a visited set proportional to the nodes cleared. No live allocation-byte, CPU, MSPT or TPS gain is claimed; this is prerequisite feature work and adds topology storage.

## Next batch

Implement item 10, patch 3: deterministic per-entry shortest-path preparation over the existing tree plus retained alternate edges, with bounded reuse and comparison to the small exhaustive oracle. Keep production transfer on DFS until the separate integration patch. Preparation must explicitly validate nonnegative/checked weights and route availability and define invalidation ownership before caching results. Shared physical amp/overload accounting and the new loop-energization behavior remain integration work, not independent per-entry budgets.

Representative AE2 baseline/candidate CPU/allocation/MSPT profiling remains outstanding. Nothing was pushed.
