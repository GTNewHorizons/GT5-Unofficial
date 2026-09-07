# Consolidated energy improvement plan

## Decision

Keep the existing synchronous energy API and cable-path compression. Complete GT's shared optimizations and compatibility tests before rolling out consumer changes. Optimize AE2's internal injection algorithm through its existing GT entry point. Add small accessors or an optional query only when a concrete implementation needs information it cannot safely obtain today. After that initial performance series, implement deterministic lowest-loss cable routing as an explicitly requested behavior improvement, replacing the current DFS-selected routes while retaining compression.

There is no established need for a replacement energy API, a public graph API, a new network manager or a pack-wide migration. GT already offers voltage and multiple amps in one call. Exposing more GT graph internals would not remove AE2's repeated internal simulation and would create invalidation responsibilities for consumers.

This plan consolidates the independent audit, original system audit and its comparison with Sol's older review, AE2 follow-up, and installed-pack consumer census. It supersedes their implementation ordering. All gains below are expected opportunities, not measured speedups. Production code has not been changed.

## Delivery sequence

| Order | Patch/work item | Repository | Expected result | Gate |
|---|---|---|---|---|
| 0 | Baseline and focused transfer regression coverage | GT; AE2 counting scenario | Distinguish scans, routing, rebuilds and consumer work; establish behavior | Required for affected complex changes |
| 1 | Remove unused IC2 traversal-set allocation | GT | Less allocation per supported IC2-to-cable injection | Preserve unknown cable override behavior |
| 2 | Check connection bits before neighbor lookup | GT | Fewer graph-build lookups | Identical graph/order and null-meta handling |
| 3 | Hoist LSC fixed transfer limits | GT | Less repeated BigInteger arithmetic per hatch | Preserve per-hatch limits and deferred net accounting |
| 4 | Allocate consumer selection only when nonempty | GT | Less allocation on blocked native networks | Preserve first voltage, snapshot and predicate order |
| 5 | Iterate linear cable-run construction | GT | Remove linear-run stack-depth exposure | Identical path membership and DFS results |
| 6 | Exact packet-equivalent dynamo filling | GT | Fewer storage calls at high amps | Prove storage, accounting and callback equivalence; retain fallback |
| 7 | Consumer boundary tests and documentation | GT | Stable behavior for existing third-party integrations | Native/IC2/RF/GC and synchronous re-entry coverage |
| 8 | Decide whether a small exposed query is actually needed | GT, with one concrete consumer use case | Minimal stable extension, or no extension | Must eliminate measured work and preserve observable behavior |
| 9 | Remove redundant AE2 energy-provider simulation | AE2 | Less repeated traversal/copying, especially fiber-linked grids | Preserve whole amps, order, buffers, extension providers and mutation |
| 10 | Deterministic lowest-loss cable routing | GT | Shortcuts and cable loops select the minimum-loss available route | Separate feature; validate routing/accounting/lifecycle and precomputation cost |
| 11 | Measured follow-ups | Relevant repository | Address remaining dominant cost | Reprofile first; select individually |

Items 1–4 should be independently reviewable changes. Items 5–6 are separate, more demanding patches. Item 7's coverage can be built alongside item 0; it must be complete before an exposed contract or consumer rollout. Item 8 is a decision gate, not a commitment to add an interface. AE2 does not technically depend on a GT API change, but this ordering satisfies the goal of finishing GT groundwork first.

Item 10 is now in the requested scope; it does not depend on proving a TPS gain. Placing it after the first performance series isolates its changed energy delivery from performance-equivalence measurements. Its design/tests can start once GT boundary coverage exists. AE2 does not need access to its internals, and the feature should not require a second consumer migration. Defer substantial tuning tied to the old DFS router until this feature's design is settled.

## 0. Establish a useful baseline without building a profiling framework

Use the supplied pack on a copied benchmark world, recording actual mod/JVM versions and configuration. The source checkout and installed GT/AE2 JARs have separately recorded baselines: build comparisons from known matching artifacts rather than treating them as interchangeable.

Record CPU and allocation profiles, median/p95/p99 MSPT, ticks over 50 ms, GC pauses and allocation rate. Use the same layouts, loaded chunks and warmup for before/after runs. Repeat enough to distinguish a change from run-to-run noise. Healthy 20 TPS alone cannot show added headroom.

Use existing test infrastructure and narrowly scoped counters where profiler stacks are insufficient. Aggregate counts; do not log each packet or build a permanent telemetry subsystem. Separate:

- Source offers, consumers scanned/selected, compressed path visits and rejected callbacks.
- Graph builds/clears and update-queue work; these are not normal energy injection.
- AE2 SIMULATE/MODULATE entries, provider calls and visited-set copying.
- High-amp dynamo storage calls and LSC hatch count.
- Laser positions, wireless sweep/save time only for the corresponding later investigation.

Start with branching cable networks at several source/consumer counts, blocked and actively draining receivers, high-amp dynamos, hatch-heavy LSCs, and AE2 quartz-fiber chains with capacity at the far end. Add lifecycle and forwarding scenarios to regression coverage. Extend the benchmark world only when a later patch needs it.

Acceptance for a performance patch: explain which work was removed, show that targeted work/allocations decreased, and check that ordinary setups did not regress. A small cleanup may have an unmeasurable whole-server effect; report that honestly instead of assigning it a TPS percentage.

## 1–4. First GT patch series

### Unused IC2 allocation

Change `TileIC2EnergySink` to avoid constructing the traversal set where the dispatched cable implementation is known to ignore it. Retain the public parameter and preserve behavior for unsupported external overrides. Validate returned remainder, accepted amps and overload behavior for GT and GT++ cables.

### Neighbor lookup ordering

In `GenerateNodeMap.generateNextNode`, reject disconnected sides before fetching adjacent tiles. Keep direction enumeration and null-meta behavior. Compare graph consumers, IDs, paths and losses for branches, loops and chunk borders.

### LSC arithmetic

Compute the existing input headroom limit and output balance limit once within the running tick, then reuse them across hatch loops. Keep clamps, ordering, maintenance/wireless interactions and final net-delta application.

Do not decrement a shared budget after each hatch: the current loops use a fixed balance and apply changes later. Changing that would alter energy accounting. Test values beyond `Long.MAX_VALUE`, low balances and simultaneous input/output hatches.

### Empty consumer selection

On the already-energized path in `MTECable.transferElectricity`, scan eligibility once in the current order and allocate the selection array only after the first qualifying consumer. Return without allocating selection/cursor state if none qualifies.

Keep first-use voltage application to all consumers/dead ends. Do not move eligibility into lazy routing or cache an empty result for the tick. A full foreign AE2 endpoint currently still qualifies, so this optimization will not by itself eliminate that allocation case.

For these narrow changes use focused existing tests or small regression cases proportionate to risk; a new general test framework is unnecessary.

## 5–6. Larger local GT changes

### Linear graph construction

Convert only `getNextValidTileEntity`'s linear cable walk to an iterative loop. Test long runs, bends, loops and branch termination. Leave the wider recursive builder/router alone unless stack stress or profiles identify a further problem.

### Dynamo filling

Replace the generic and rocket-engine per-amp storage loop only where an exact equivalent can be proven. Preserve the single packet that can cross capacity, separate remainder handling, offered-versus-stored accounting and exceptional arithmetic behavior. Fall back to the old path outside the supported range or for implementations whose storage callbacks cannot be collapsed safely.

Do not copy the already-batched XL turbine implementation wholesale. Review generic, rocket, XL and legacy turbine paths together to understand their differences, but change only the matching implementations. Keep integer-widening fixes separate.

The existing Python arithmetic model is useful evidence, not sufficient validation. Add production hatch tests for boundary storage, large amps, overflow fallback, mixed hatches and observable hooks. If preserving those hooks removes the benefit, narrow or drop the fast path.

## 7–8. Finish consumer groundwork with the smallest boundary

### What to expose, and what to leave internal

| Need | Smallest adequate solution | Initial decision |
|---|---|---|
| Send several amps | Existing `injectEnergyUnits(side, voltage, amps)` | Already available; no addition |
| Avoid AE2's repeated provider traversal | AE2-local reuse of work during one injection | No GT addition |
| Reject an offer cheaply using endpoint-owned exact state | Early return inside that endpoint's existing injection method | Preferred first solution |
| Let GT use a useful endpoint-owned rejection query | Optional query via an existing extension convention, if one is suitable | Add only with a concrete measured use case |
| Cache rejection across calls | Complete state invalidation owned by the endpoint/network | Defer; a timestamp or telemetry getter is insufficient |
| Skip a downstream GT graph because a P2P output previously failed | Would require more than a safe local getter | Defer; different entries, losses and state changes matter |
| Access GT consumer lists, paths or mutable graph objects | Creates external lifecycle and routing dependencies | Do not expose |

The missing information generally belongs to the receiving mod. GT cannot manufacture exact AE2 capacity by exposing its own graph. Likewise, adding a getter for estimated stored/max power does not establish what a particular packet can accept.

If the consumer can perform an equivalent cheap rejection inside its existing injection method, a separate GT query saves no additional consumer work at that same point. That is a reason to omit the query, not to add one for completeness.

### If a shared query earns its place

Specify semantics before choosing a Java method/interface shape. It answers only whether bypassing this exact attempted injection is observably equivalent to the legacy rejection. The offer includes the actual side, delivered voltage after losses, amperage and selected protocol as needed. A false/unknown answer means normal injection proceeds; it does not promise acceptance.

Required properties:

- Cheap, non-mutating and non-recursive: no downstream graph polling to answer the question.
- Authoritative for the immediate call; no implicit lifetime beyond it and no whole-tick cache.
- Opt-in only, with old implementations continuing unchanged. No new mandatory abstract method on an existing public interface.
- Protocol-specific semantics for multi-interface tiles, respecting GT's current selection precedence. A shared RF/EU capacity assumption is invalid for turret bases.
- No bypass of legacy rejection-side effects. If a rejecting callback updates diagnostics, counters or other visible state, either preserve those effects or leave that implementation on the old path.
- Initially evaluated where endpoint injection already happens, after required GT path voltage effects. Skipping route traversal is a separate, harder optimization.

Inspect existing capability/extension conventions during implementation; choose the smallest compatible mechanism. Do not prebuild a registry of every mod or wrappers that force IC2/RF/GC tiles to implement the native GT energy interface. Expose support only for demonstrated adopters; preserve all other adapters.

Complete GT tests and document this boundary before a small real integration pilot, then settle its shape before wider rollout. If the pilot shows no advantage over an endpoint-local fast path, remove the proposed public addition. The groundwork remains useful without it.

### Compatibility cases that must guide the boundary

| Case | Why it matters |
|---|---|
| Railcraft feeder/loader | Accepts one amp per call and rejects at an exact demand boundary |
| Open Modular Turrets | Whole-batch acceptance can overfill; RF and EU buffers differ; fullness affects connection checks |
| BuildCraft-style receiver | Free storage does not imply remaining per-tick input allowance |
| Multiple sources with a receiver tick/extraction between calls | A rejected endpoint can accept later in the same server tick |
| AE2 GT P2P, RemoteIO, transvector | Calls can synchronously forward and potentially re-enter GT |
| GC/IC2/RF multi-interface endpoint | Preserve adapter precedence, conversion and rejection semantics |
| Full endpoint behind an overvolted cable | Refusal does not automatically cancel cable voltage effects |
| Replacement, retarget, side mode and middle-chunk unload | Endpoint validity alone does not prove route validity |

Assert ordered accepted amps, source debit, affected buffers, packet remainders, path state, burns and relevant diagnostics. For correctness bugs already identified, record current behavior separately from the intended corrected expectation; do not enshrine an energy-duplication bug as a permanent contract.

## 9. AE2 change through the existing integration

First reproduce the source-derived repeated-suffix pattern with quartz-fiber depth 1, 2, 4, 8, 16 and 32, placing useful capacity at the far end. Include an ordinary single grid to catch overhead added to the cheap case.

Then remove redundant simulation inside `EnergyGridCache` using the least invasive approach supported by that call flow. A bounded per-call reuse of acceptance decisions for known AE2 providers is a candidate; it is not permission to build a general transaction framework. Preserve fallback for unknown extension providers and invalidate/re-evaluate decisions when callbacks change relevant state.

Retain `GTPowerSink.injectEnergyUnits` as the public entry. Preserve requester/provider order, `extra` and local buffers, cycle/visited rules, events and the distinction between simulating the whole offer and committing only whole GT amps. Never commit fractional unpaid energy and round the return afterward.

If rejected calls remain costly afterward, examine an authoritative endpoint-local rejection fast path. Existing estimated energy telemetry and storage threshold events are not enough to support exact cross-call caching. Implement no cache until every relevant mutation can invalidate it.

Handle native GT P2P separately from acceptor/controller injection: measure output iteration and downstream GT selection. Preserve entry points, losses, output order and synchronous debit. Do not merge outputs just because they reach the same graph. Legacy IC2 P2P has per-packet/random-buffer behavior and needs its own profile and tests.

No broad consumer migration follows this patch. Ordinary RF/IC2 buffers benefit from shared GT work unchanged. OC, forwarding devices or other consumers get changes only where their own profiles justify them. Railcraft and OMT are valuable compatibility cases, not automatically worthwhile optimization targets.

## 10. Deterministic lowest-loss cable routing

**Required behavior:** for a given cable entry and receiving endpoint, choose an available route with minimum accumulated GT voltage loss. Adding a strictly lower-loss shortcut must lower the selected route loss after the relevant topology update. Different entries into one component can have different optimal routes. This is a game routing rule using GT's existing loss model, not a simulation of electrical current splitting across parallel resistances.

### Algorithm and representation

Dijkstra is the initial algorithm choice (but verify if anything else would work better), provided the represented costs are nonnegative and additive. Use the existing compressed cable runs as weighted edges, but retain loop-closing connections and parallel paths: running Dijkstra on the current DFS tree cannot discover shortcuts already discarded during construction. Include junction/entry/endpoint losses exactly once according to the actual transfer accounting. Check supported addon loss values and overflow rather than assuming every weight is valid.

Compute a shortest-path tree for each active cable entry when needed and reuse it while its routing state remains valid. This means preparation at topology change or first use of a new/invalidated entry, not a new search for every packet. Use one search to cover that entry's reachable consumers; avoid all-pairs precomputation and separately allocated full paths for every source/consumer pair. Bound retained entry caches and measure many-source networks, including P2P entries, before choosing cache ownership.

For a heap-based implementation, the target search complexity is approximately `O((V + E) log V)` per entry, where V/E refer to the compressed graph. Per-entry predecessor data still has a memory cost proportional to V. This is a planning estimate, not a benchmark or a promise that rebuild-heavy networks get faster. Check existing utilities/dependencies before writing a custom queue or graph framework.

Define deterministic equal-loss tie-breaking: prefer fewer physical cable blocks, then stable coordinate/direction ordering rather than discovery/hash iteration order. Handle zero-loss cycles without cyclic predecessor chains. Equal-loss shortcuts need not reduce loss, but repeated rebuilds of unchanged topology must choose the same routes regardless of which machine triggers reconstruction.

### Transfer and lifecycle rules

- Keep synchronous injection, source debit, packet voltage/amp semantics and consumer priority. Shortest paths select routes to consumers; they must not silently sort consumers by distance or available demand.
- Account for shared physical cables across all entry trees. Each entry must not acquire an independent amp budget or overload state for the same cable. Preserve grouped transfer where possible without changing delivery order.
- Retain voltage/amp rating checks on the selected route. Do not silently reroute around an impending overload: overload avoidance and multipath load sharing are outside this feature.
- Invalidate affected routing data on connectivity, cable/loss changes, relevant covers/locks, endpoint/side changes and chunk lifecycle. Covers that block traversal affect which routes are available. Ordinary energy-buffer changes do not invalidate a topology-only route.
- If injection callbacks remove cables or change topology, stale paths must not remain usable. Integrate invalidation with existing update timing, and explicitly test changes during nested forwarding.
- Preserve first-energization/dead-end voltage effects and diagnostic obligations wherever independent of the new chosen routes. Specify how previously discarded loop edges are energized/accounted for before coding the router; path selection alone does not define those effects.

Lower loss intentionally changes delivered voltage, storage acceptance and which cables carry/burn under load. That can expose a receiver to higher voltage. Document these as feature effects. Continue to test energy conservation and all unchanged rules; do not demand identical old/new loss traces on networks whose route intentionally changes.

### Reviewable patch sequence

1. Add feature tests for shortcut selection and establish route-weight/accounting rules, including energization of loop branches.
2. Extend compressed topology to retain alternate edges with lifecycle tests, keeping the legacy transfer behavior until the new routing stage is ready.
3. Implement deterministic per-entry shortest-path preparation and bounded reuse. Compare results to a simple test oracle on small graphs.
4. Integrate transfer through selected routes, shared cable accounting and invalidation. Keep old/new comparison in tests; a permanent user-facing legacy-mode option is not assumed necessary.
5. Benchmark steady-state transfers, many entries and topology churn; document the gameplay change separately from the initial performance results.

### Acceptance scenarios

| Scenario | Required result |
|---|---|
| Loop with short and long same-material routes | Lower-loss route selected, independently of discovery order |
| Longer low-loss cable route versus shorter high-loss route | Loss wins over physical distance |
| Shortcut added, removed, covered or recolored | Updated minimum available route; no stale traversal |
| Equal-loss alternatives and zero-loss loops | Stable acyclic selection across rebuilds |
| Multiple sources and P2P entry points | Correct route from each entry with shared physical amp/overload accounting |
| Several consumers competing for supply | Existing consumer priority and synchronous debit retained |
| Underrated cable on the minimum-loss route | Existing overload rules applied; no automatic load balancing |
| Middle-chunk unload/reload and cable removal during injection | No use of invalid route data |
| First energization, dead ends and full consumers | Explicitly defined voltage effects and diagnostics retained |
| Many entries, large loops and repeated edits | Bounded memory and measured rebuild/steady-state costs |

This phase supersedes the older reports' recommendation to defer shortest-path routing indefinitely under the original performance-only scope. No public graph exposure, asynchronous world mutation or new consumer API is required.

## 11. Follow-ups selected from the new profile

| Remaining cost | Next bounded change | Condition that stops the work |
|---|---|---|
| Nonempty selection allocation | Reuse bounded scratch with nested-call fallback, cleanup and topology handling | Persistent memory/coordination costs outweigh allocation savings |
| Native eligibility checks | Restricted exact state caching for known implementations | Current predicate is cheap or invalidation cannot be complete |
| Update/rebuild churn | Deduplicate cable seeds while preserving updates scheduled during processing | No material duplicate work; do not blindly alter machine seeds |
| Repeated long laser walks | Route cache only with reliable segment invalidation | Validation still walks the whole route or lifecycle cannot be preserved |
| Routine electric-tile work | Same-pass reuse in face/item checks and safe local terminal-state exits | Dynamic covers/items or callbacks make cached state incorrect |
| Wireless save work | Avoid unnecessary dirty marking while preserving map/team/entry creation | No meaningful save cost, or proposal changes persistence semantics |
| Wireless charger status scans | Reuse existing proximity facilities with unchanged notifications/boundaries | Complexity exceeds measured saving |
| LSC statistics memory | Exact lazy allocation only if history behavior can be retained | It shortens or alters diagnostic history |
| Repeated topology/lifecycle failures | Narrow invalidation fix based on a reproduced failure | No reproduced issue; do not add component ownership speculatively |

Do not introduce an event-maintained active-consumer list as an initial optimization. It requires every relevant mutation, including foreign ones, to maintain membership. Do not add a second adjacency cache where the existing cache already solves the problem.

## Separate correctness and behavior-change work

Reproduce these before optimizing the affected subsystem, then give confirmed fixes separate patches and release notes. They need not wait for the entire performance series. Explicitly changed behavior must not be used as evidence that a performance patch is equivalent.

| Priority to investigate | Finding | Required proof/fix scope |
|---|---|---|
| High | RF/GC residual credit can survive an unpaid rejected packet | Rejection, partial acceptance and changed-voltage accounting; credit only energy whose debit/ownership is established |
| High | RF transformer extracts before native acceptance | Demonstrate loss on rejection; preserve actual third-party extraction semantics |
| High | Mirror recursion and possible forwarding cycles | Reproduce reachable cycles; bound recursion while retaining valid routes |
| High | Cross-chunk IC2 lookup condition | Loaded/unloaded neighbor tests with a sink using emitter identity |
| High | Narrowing/overflow across cable, adapters and dynamos | Trace caller chain and supported limits before widening arithmetic |
| Medium | Cable middle-chunk unload and cached microwave/P2P targets | Reproduce stale route/target behavior; targeted invalidation, including reload |
| Medium | AE2 compatibility wrapper retries after arbitrary `Throwable` | Separate API-version dispatch failure from a transfer that may have partially completed |

Keep these optional changes outside the default performance series: staggered wireless arrival, failed-withdrawal backoff, shorter LSC history, changed exotic-hatch scanner flags, revised LSC sequential budgeting, and removing scanner-triggered lock refresh without equivalent invalidation. Each changes timing, accounting or diagnostics. Evaluate only when its measured benefit warrants an explicit behavior decision.

## Completion criteria and scope

The first delivery is GT patches 1–7, their focused checks and a before/after report. The boundary decision is included: either document why existing entry points suffice or deliver the smallest tested extension with a concrete adopter. AE2 follows against that settled boundary. Lowest-loss routing is a separate required feature delivery after the initial performance series, with its own behavior and performance results. The final profile selects later work rather than automatically authorizing every cache in the backlog.

For each delivered patch, record affected workloads, validation, observed work/allocation/time changes and any limitation. Require no unexplained energy, ordering, diagnostic or lifecycle changes in the performance series. Check ordinary setups alongside stress cases and retain external compatibility fallbacks.

Existing audit checks passed during the earlier work, but they are bounded arithmetic/parser checks. No production regression tests or live benchmarks have yet validated these proposed implementations. This document is the implementation plan, not a claim that the improvements are already complete.

Source reports: `energy-audit-independent-notes-2026-09-07.md`, `energy-system-audit-2026-09-07.md` (including the older Sol review comparison), `energy-audit-ae2-followup-2026-09-07.md`, `energy-pack-consumers-2026-09-07.md`, and the generated `energy-pack-inventory-2026-09-07.md`/JSON. The static inventory supplies compatibility cases; its candidate classes are not a measured ranking of server cost.
