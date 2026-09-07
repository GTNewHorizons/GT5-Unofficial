# Energy improvements: consumer boundary, 2026-09-07

## Scope and API decision

This batch completes the GT-side coverage and boundary decision in consolidated-plan items 7–8. Baseline: `88cca8df0e`. It adds tests, test-runtime configuration and documentation only. No production adapter, API, AE2 implementation, routing algorithm or correctness fix changes.

**Keep the existing energy entry points; add no rejection query.** `IEnergyConnected.injectEnergyUnits(side, voltage, amperage)` already supports multi-amp offers and synchronous forwarding. Its documentation explicitly leaves injection-side validation to the endpoint. The existing `inputEnergyFrom` overloads are connection checks, not an authoritative demand contract. `IGregTechTileEntity` already extends GTNHLib's `CapabilityProvider`, and `BaseMetaTileEntity.getCapability` delegates to its meta tile, but an extension mechanism alone does not justify another capability.

The strict one-amp and whole-batch fixtures provide concrete contrasting consumer cases: a strict endpoint rejects at an exact voltage boundary while a batch endpoint can overfill its EU buffer and has unrelated RF storage. Each can decide locally within its existing injection callback. A separate query at that point would not eliminate more endpoint work. No expensive adopter was demonstrated that benefits from a new query. AE2's repeated provider traversal remains work for AE2 to remove behind its existing GT entry. There is no interface to pilot or roll out from this batch.

Consumer lists, paths, locks and mutable graph objects remain internal. Do not infer native acceptance from RF telemetry, call demand getters speculatively, or cache rejection for a server tick. A future opt-in query requires an actual adopter, measured incremental savings over endpoint-local rejection, and the immediate-call, protocol, side-effect and invalidation semantics specified in the consolidated plan.

## Runnable coverage and evidence

The existing 17 focused energy/graph/LSC cases passed before edits. Their XML was preserved in `build/energy-batch4-baseline-results/`. Supporting reports were backed up outside `build/`; no clean task was run. Tests use the existing JUnit/Mockito infrastructure and real production adapter, graph, cable, path and source-output methods. Galacticraft 3.4.33-GTNH, already a compile dependency, was added to test runtime without transitive dependencies so the real GC interfaces and conversion code can execute. Endpoint callbacks and world/chunk availability are controlled fixtures, not executions of the installed Railcraft, OMT, BuildCraft, AE2 or forwarding-mod implementations.

| Boundary | What is checked |
|---|---|
| Adapter choice | GT base, native GT, GC, IC2, then RF precedence; refusal in a higher-priority protocol does not fall through; RF output flag |
| Strict native endpoint | One amp per callback, exact-boundary refusal, correct receiving side and reopened demand in the same tick |
| Whole-batch native endpoint | Entire offered batch accepted across capacity, later refusal, fullness-sensitive connection predicate, no RF callback |
| IC2 | Full and partial packet acceptance, whole-amp accounting, full remainder refusal, demand/invalidity selection |
| RF | Simulation before receipt, partial receipt and retained remainder, refusal with per-call input allowance exhausted; no capacity telemetry substituted for receipt |
| Galacticraft | Conversion and capacity gate, retained GJ and receipt after capacity changes |
| Forwarding | Real native adapter callbacks synchronously enter another GT cable graph; ordered downstream/tail receipt, remaining offered amps and changed forwarding behavior between calls |
| Source and loss accounting | Production `handleEUOutput` debits accepted amps including source output loss; cable self/run losses reach the endpoint and both paths record accepted amps and voltage diagnostics |
| Refusal and overload | A rejecting native endpoint still gets path voltage effects and cable burning, with zero accepted-amp diagnostics |
| Side, replacement and locks | Injection-side refusal before rebuild, endpoint replacement on rebuild, invalid selection, cover lock rejection/reopening without replacing the graph |
| Lifecycle | Actual cable unload hook, cross-border missing/reloaded neighbor and graph clearing/reconstruction; production source graph generation reuses same-tick graph and rebuilds on a later tick |

Earlier tests remain the coverage for receiver-local amp resets, IC2-to-GT/GT++ ingress remainder/overload, unsupported cable overrides, selection snapshot/order and same-graph synchronous re-entry, first-energization/dead-end burns, and LSC deferred accounting. This batch does not claim a new optimization or a new allocation/time reduction.

## Known issues reproduced, not fixed

These tests record current behavior for the performance series. Their assertions are not a permanent energy-conservation or lifecycle contract; change them with the separately reviewed fixes.

- **RF unpaid remainder:** a 32 EU offer at the test's 4 RF/EU ratio creates 128 RF. After accepting 20 RF and paying one amp, a second rejected offer adds another 128 RF and returns zero. A later call delivers the retained 236 RF for zero new amps. The extra 128 RF was never paid for.
- **GC unpaid remainder:** a rejected offer creates one packet of retained GJ. Increasing capacity then delivers that packet while returning zero amps. The intended corrected behavior must ensure all delivered energy is paid for.
- **IC2 emitter identity across chunks:** at a loaded adjacent chunk, the current graph builder passes null to `acceptsEnergyFrom`; when `blockExists` is false, it performs the tile lookup and passes that result. The future fix must avoid looking into unloaded chunks and preserve the real adjacent emitter when loaded.
- **Middle-chunk unload:** the real `onChunkUnload` hook marks a cable dead but leaves its compressed path and the root graph attached. The controlled scenario still transfers through that retained route until graph clear/rebuild. Explicit rebuild with an unavailable neighbor removes the endpoint; reload and rebuild restores it. This does not validate real server chunk-event ordering.
- **Same-tick graph reuse:** `generatePowerNodes` does not reconstruct an existing graph created in the current server tick. The next-tick invocation does reconstruct it. Forwarding/topology changes during a call therefore need explicit consideration in any future routing/invalidation change.

The first four reproduce issues already identified by the approved plan. The same-tick guard is a concrete scheduling limitation relevant to that backlog. No fix or new cache was introduced under the guise of coverage.

## Validation and next batch

All **280 tests passed with zero failures or skips**, including 15 new boundary/lifecycle cases. `gradlew.bat test spotlessCheck checkstyleMain checkstyleTest --console=plain` completed successfully: compilation, bytecode downgrading, the full unit suite, formatting and both Checkstyle tasks passed. Initial fixture/classpath issues and a test import-style violation were corrected before this final run. `git diff --check` passed. All eight supporting reports match their protected backup. Final review confirms that changes are limited to two new test classes, package-visible reuse of two existing test helpers, one test-runtime dependency declaration and this handoff. The batch is committed separately; production source is unchanged.

Live profiling remains unavailable. No TPS, MSPT, CPU-time or allocation-byte gain is claimed for this tests/documentation batch. Before wider integration changes, replay these cases against the actual mod versions and real chunk/event sequencing, in addition to the benchmark-world steps in batches 1–3.

Next recommended batch is item 9 in the AE2 repository: first count provider/simulation work at quartz-fiber depths 1, 2, 4, 8, 16 and 32 plus a single-grid control, then choose the smallest safe per-call reuse/removal of redundant simulation. Preserve whole paid GT amps, provider order, buffers, mutation and external-provider fallbacks through the existing GT entry. This GT change does not authorize a correctness fix or start the separately sequenced routing feature. Nothing was pushed.
