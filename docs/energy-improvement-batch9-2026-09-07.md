# Energy batch 9: deterministic per-entry routing preparation

## Delivered scope

This is item 10, patch 3 of the consolidated plan, following alternate-topology commit d815742e97. PowerRoutePlanner is an internal preparation engine over the existing DFS paths and retained alternate edges. Production transfer still uses PowerNodes/DFS; no runtime caller invokes this planner in this staging patch. There is no new public consumer API, graph exposure, packet transfer, source debit, amp accounting, energization pass or AE2 change.

The planner must be owned once per freshly generated component by the later integration layer. It is not a global static cache or a planner per packet. Its root anchors the generated snapshot; the integration owner must discard it after invalidation and create a replacement only after the component has been regenerated. Constructor/prepare calls against arbitrarily edited, unreconstructed graph data are not an alternative to that lifecycle.

## Preparation and ordering

A standard PriorityQueue runs Dijkstra over the existing neighbor/path arrays plus alternate edges. There is no second persistent adjacency representation. Each best label retains its predecessor and the selected compressed run/orientation; full source-to-consumer cable paths are reconstructed only for a tied comparison or explicit inspection, not retained separately for every pair.

The initial cost includes the entry self cable. Each transition adds interior cable loss and the destination self cable, if any. A machine endpoint adds no cable loss and is never used as a bridge to another consumer. Routes are ordered by total loss, physical cable count, lexicographic physical cable coordinates x/y/z, then receiving face ordinal. Node IDs, consumer priority and hash iteration order do not choose the route. Zero-loss cycles cannot improve the cable-count tie-break. The receiving face must match the last cable's physical position.

Preparation validates nonnegative cable weights and checked arithmetic. It conservatively rejects a component if the sum of its unique represented cable losses overflows long, even if a particular short route could fit. This keeps unsupported weights on the explicit legacy fallback rather than inventing clamped costs. Ratings are not optimization weights: overload avoidance is not introduced.

A null prepare result means unsupported or stale preparation and requires the integration layer's legacy fallback; it is not a zero-demand answer. A current tree can separately report an unreachable endpoint. Access through a stale tree is rejected. No endpoint demand or capacity query is called while selecting paths.

## Bounded reuse and invalidation

Each owner retains at most eight entry trees in access-order LRU order. Preparation is also bounded to 4,096 compressed nodes/consumers and 65,536 unique cables, with a per-run length guard. These are conservative staging ceilings, not measured optimum settings. Oversized or unsupported snapshots fall back. The nine-entry grid test exercises eviction and verifies repeated preparation reuses the same tree while valid.

Because graph arrays and covers are mutable without a complete routing revision protocol, cache reuse validates exact captured object identities and scalar state instead of trusting a tick or hash. Captured state includes graph paths/members, alternate records, node ownership, endpoint faces, consumer membership/order, cable/meta identity, connections, neighbor identities, colors, coordinates, world tile identity and loaded/invalid/dead state.

Structural changes invalidate the entire owner and its entries. Covers and lock-state changes clear entry preparations and allow recomputation on the same topology. Cover replacement with equivalent energy passability does not invalidate a route merely due to object identity. Ordinary receiver capacity/demand changes do not invalidate topology-only preparation.

Connected cover faces must permit both energy directions, matching the existing cable lock convention. Tree path locks remain unavailable, and alternate runs have their faces checked without manufacturing transfer locks or counters. Neighbor/world lookups test blockExists before accessing a tile, independent of the native tile lookup's ignore-unloaded-chunks setting.

Results are validated again before publication/reuse, and tree currency checks do the same. A cover callback that changes topology during scanning cannot publish the tested old state. Nested preparation during a validation callback returns fallback instead of recursively entering another search. Validation is synchronous and does not introduce background work.

This is deliberately conservative validation, not event-driven availability caching. The later integration must check currency after receiver callbacks/topology effects and before further transfer, and must never replay a partially debited transfer merely because preparation became stale. Existing middle-chunk stale-DFS behavior is not silently fixed by this staging patch.

## Verification

The 41 existing graph tests passed before editing. The eight supporting files under build/energy-improvement-2026-09-07 were copied to a new protected temporary directory before Gradle ran. No clean task was used.

Ten new tests exercise production preparation:

- Shortcut and mixed-loss routes in both entry directions, checked against the independent physical simple-path oracle.
- Equal loss/length coordinate ties and zero-loss cycles.
- All entry/endpoint pairs in six fixed-seed 3-by-3 grids, plus nine-entry LRU eviction.
- Cover/lock closure and reopening, with unchanged receiver capacity leaving the cached tree valid.
- Connection changes, recoloring, graph clearing and replacement component owners.
- Loaded-world replacement and unload, including proof that an unloaded neighbor is not queried.
- Negative/overflowing loss and oversized consumer-snapshot fallback.
- Mutation during initial and cached preparation, and synchronous nested preparation fallback.

Preparation still chooses the four-volt-loss shortcut while the unchanged production transfer delivers 116 V from a 128 V offer through the twelve-volt-loss DFS route. The test explicitly verifies preparation invokes neither injection nor receiver capacity predicates.

An intermediate full run found a test setup error where Mockito invoked the old callback while replacing its stub; the fixture now uses doAnswer. The final `gradlew.bat spotlessApply test spotlessCheck checkstyleMain checkstyleTest --console=plain` run succeeded: 303 tests passed, with zero failures, errors or skips, including all ten new planner tests. Main and test Checkstyle reports contain zero errors. Final diff review confirmed only the planner, its regression tests and this handoff were added; existing production transfer code is unchanged. The eight protected supporting reports retain matching SHA-256 hashes.

## Costs, limitations and next batch

A cache hit skips Dijkstra but still performs bounded full-component validation scans and allocates comparison state. Currency/endpoint access also validates. This can outweigh the saved search in small or frequently changing networks. The basic heap search has the expected graph-search work, plus physical-run inspection and transient path reconstruction for ties; tie-heavy networks can be substantially more expensive than a simple O((V+E) log V) label comparison model. No CPU, allocation-byte, MSPT or TPS improvement is claimed.

Retained trees use predecessor data, with one shared validation snapshot per owner. Profile the provisional bounds and validation/tie costs before broadening them. Headless world/cover fixtures establish bounded behavior, not real modpack lifecycle/performance. Native GT P2P/many-source entry promotion and active owner placement must be tested during integration; these tests do not establish simultaneous live source ownership.

Next is item 10, patch 4: integrate selected routes with existing consumer priority, synchronous debit, shared physical cable amp/overload state, first/dead-end/loop energization and callback-safe invalidation. Wire one owner to a completed component generation and release it on lifecycle changes. Keep old/new comparisons in tests, document the intentional delivered-voltage changes, and preserve the unsupported-component fallback. Representative AE2 and routing CPU/allocation/MSPT profiling remains outstanding. Nothing was pushed.
