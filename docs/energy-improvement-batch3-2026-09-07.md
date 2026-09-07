# Energy improvements: full dynamo batches, 2026-09-07

## Scope and decision

Batch 2 was committed as `9cb9431452` (`Iterate compressed cable runs without linear stack growth`). This batch addresses consolidated-plan item 6, narrowed to skipping rejected packets on already-full native multi-amp dynamos. The plan explicitly permits narrowing the fast path when preserving storage hooks prevents accepted-packet batching.

Source baseline: `9cb9431452`. The existing JUnit/Mockito/JVM Downgrader infrastructure, Temurin 25.0.3 and Gradle 9.4.0 were used. All eight supporting reports were copied outside `build/` before Gradle ran. No clean task was used.

## Why accepted packets remain individual

The generic, rocket-engine, XL and legacy turbine implementations were traced against current storage code. `BaseMetaTileEntity.increaseStoredEnergyUnits` accepts a packet when storage is below capacity before that packet; the packet can cross capacity. Its `setStoredEU` clamps a negative overflowed result to zero. `MetaTileEntity.setEUVar` marks the chunk dirty **before** assigning the new balance. World/chunk overrides can observe the old balance and change state synchronously.

The new production regression demonstrates that a dirty callback changing `Amperes` from four to two causes different existing results: the generic controller still offers four precomputed packets, while the rocket loop stops after two. Combining accepted packets would change both observations and behavior. Those loops remain intact, including the rocket's zero-remainder call with a spare amp. There is no new storage API or attempt to copy XL/legacy batching.

## Implemented change

The generic controller's normal and exotic hatch loops skip full-voltage storage calls only for exact `MTEHatchDynamoMulti` instances attached to exact `BaseMetaTileEntity` instances whose current meta binding matches. The generic remainder call and offered-energy accounting remain unchanged. The rocket engine uses equivalent offered-energy arithmetic only on this full-storage path, retaining its original loop for exceptional amp arithmetic and all other implementations.

Rejected calls on these exact native implementations have no callbacks or writes. A per-invocation capacity check is sufficient; no state is cached. External hatch/base subclasses, ordinary one-amp dynamos and accepted transfers retain packet calls. Tunnel, wireless and buffer subclasses do not qualify. Accepted transfers that become full mid-loop also retain their subsequent rejected calls.

## Baseline and checks

Before production edits, all **28 focused cases passed**, including 18 new dynamo cases. Baseline XML is retained locally in `build/energy-batch3-baseline-results/`. The storage-call assertions were changed from the original count to zero only after that passing baseline.

The fixtures invoke actual controller methods, native hatch storage, capacity checks and dirty handling, with mocked world/chunk objects and spies for call counts. They cover 80 boundary combinations across the two controllers, packet overshoot, remainder and zero offers, accepted dirty callbacks, external getters, an external base reopening storage after rejection, mixed/invalid/exotic hatch lists, large offers, and arithmetic fallback.

| Full native hatch, complete full-voltage offer | Baseline storage calls | Optimized calls |
|---|---:|---:|
| 4 amps, each controller | 4 | 0 |
| 16 amps, each controller | 16 | 0 |
| 64 amps, each controller | 64 | 0 |
| 4,096 amps, each controller (stress case) | 4,096 | 0 |
| External hatch/base, 4 amps | 4 | 4 |

Balances, offered return values and dirty traces are checked alongside these counts. One-packet offers keep the original loop. Eligible accepted multi-packet offers incur an extra constant-time native capacity check; their packet work is unchanged. This overhead has not been timed.

The initial fixture needed headless texture initialization corrected. A 65,536-amp spy fixture exhausted the existing test heap through invocation recording; it was reduced to 4,096 rather than changing test infrastructure. A duplicated patch block was caught by compilation/diff review and removed before final validation.

Final validation: **265 tests passed, zero failures or skips**. `gradlew.bat spotlessApply test spotlessCheck checkstyleMain checkstyleTest --console=plain` completed successfully, including compilation, bytecode downgrading, all unit tests, formatting and both Checkstyle tasks. `git diff --check` passed. All eight supporting reports match their protected backup. Final diff review confirms two scoped production files, the focused dynamo tests and this handoff; no XL/legacy turbine, consumer, AE2 or routing code changed. This batch is committed separately from batch 2.

## Separate observations and remaining work

Existing `int injected` narrowing can return false for a large offered batch despite traversing it, and the rocket's `aAmpsToInject + 1` overflows at `Integer.MAX_VALUE`, skipping transfer. Tests record current behavior for this performance patch, not a permanent correctness contract. These are concrete instances of the plan's existing arithmetic-correctness backlog; neither is fixed here. Per-packet storage overflow/clamping is likewise preserved.

There was no live game server or representative benchmark world available. The table is verified production-code work reduction, not a measurement of CPU time, allocation bytes, MSPT or TPS. Before claiming live gains, compare source-built baseline and optimized artifacts with identical JVM/pack/world settings; warm up and repeat ordinary one-amp, active and full 4/16/64-amp hatch workloads. Record storage-call counts, CPU samples, allocation and MSPT distributions, and the proportion of dynamos already full. Check accepted workloads for the cost of the added guard. Retain the broader cable/LSC benchmark steps from batch 1.

Next recommended batch: finish item 7's native/IC2/RF/GC consumer, forwarding/re-entry and lifecycle coverage, then record item 8's boundary decision using a concrete consumer case. Existing injection entry points remain the default; no new query is justified by this dynamo change. AE2, routing and separate correctness fixes remain outside this batch. Nothing was pushed.
