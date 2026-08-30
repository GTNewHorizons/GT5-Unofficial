# Test audit verdicts (permanent-test directive)

SCOPE (user, 2026-08-28): ONLY tectech\voidcraft\** tests (uss/ship/multiblock/render). Everything outside Voidcraft/USS is KEPT regardless of verdict.

Rubric: KEEP = pins a real decision (rule / serialized format / error contract / lifecycle invariant) a likely change could break alone. DELETE = scaffolding validating initial implementation, or dominated by another test's assumptions.

## OUT OF SCOPE — KEEP ALL (no changes)
- gregtech\api\factory\StandardFactoryGridTest (14)
- gregtech\api\metatileentity\implementations\MTEMultiBlockBaseTest (0 active)
- gregtech\api\recipe\lookup\GTRecipeLookupBuilderTest (11)
- gregtech\api\recipe\lookup\GTRecipeLookupIngredientTest (7)
- gregtech\api\recipe\lookup\GTRecipeLookupTest (12)
- gregtech\api\recipe\RecipeMapBackendLookupTest (33)
- gregtech\api\util\ArrayExtTest (1)
- gregtech\api\util\GTUtilityTest (17)
- gregtech\api\util\LongRunningAverageTest (5)
- gregtech\api\util\NBTPersistTest (2)
- gregtech\common\blocks\BlockMachinesSubclassTest (1)
- gregtech\common\covers\CoverWirelessControllerTest (6)
- gregtech\common\items\CombTypeTest (4)
- gregtech\globalenergymap\IGlobalWirelessEnergy_UnitTest (4)
- gregtech\overclock\GT_OverclockCalculator_UnitTest (48)
- tectech\mechanics\boseEinsteinCondensate\BECFactoryNetworkTest (22)
- tectech\thing\...\ForgeOfGodsStarColorTest (1)
- tectech\util\TTUtilityTest (1)
(Any earlier per-test deletion notes below these files are RETRACTED.)

## tectech\voidcraft\multiblock\MiningArrayStructureTest (4)
KEEP ALL: structure shape (real decision: 3x3x2=18 cells), cell counts/anchor split, anchor position, findAnchor error contract (IllegalStateException).

## tectech\voidcraft\multiblock\MultiblockAuditTest (10)
KEEP ALL: audit error contract (ERROR_INCOMPLETE / ERROR_OUT_OF_VOLUME, stray casing tolerated, extra casings ok, dedupe, per-component independence).

## tectech\voidcraft\render\RenderVoidcraftBlueprintItemTest (4)
KEEP ALL: fitScale box-fit rule, hasBlueprint grid-tag rule, render-type claiming rule, render-helper selection matrix.

## tectech\voidcraft\render\TileEntityVoidcraftShipTest (4)
KEEP ALL: NBT round-trip (tag names/structure = serialized format), empty round-trip, setter null-skip, setter whole-list replace (lifecycle invariants).

## tectech\voidcraft\render\VoidcraftShipFxTest (4)
KEEP ALL: beam role gate, beam fade envelope (visual spec), beam basis orthonormal + degenerate->null contract, exhaust duty cycle 3/8 rule.

## tectech\voidcraft\ship\VoidcraftBlueprintTest (36)
KEEP 35; DELETE 1: testIntegritySumsComponents (summation rule already pinned by testMinimalHaulerStats 2-part + testCoverStatsContribute 4-part sums)
Rationale: validation error contracts (controller_count/no_engine/no_frame/cover_only/too_small/tier_too_high/thruster_wrong_facing/engine_blocked), speed=thrust/mass unclamped rule, exhaust clearance window 5, base 15-cube rule, parts-list format/order, NBT format, toGridSide mapping tables, mining-array stats-once + tier gate.
NOTE: comment in testMinimalHaulerStats says "pass 18" — user rule: no pass references in comments; clean while editing.

## tectech\voidcraft\ship\VoidcraftComponentTest (10)
KEEP ALL: meta contiguity, grid-value mapping, unique core blocks, placeable set, multiblock catalog stats (tier2/mass25/mining1000/draw200/casing5, meta 11-13, grid 12-14), MTE id = 32058+meta renderer contract, cover-only voidbase stats, registry counts, circuit tier gate table, stats non-negative.
NOTE: rename testPass23PlaceableSet -> testPlaceableSet (no pass references).

## tectech\voidcraft\ship\VoidcraftCoverComponentTest (7)
KEEP ALL: table shape + id=ordinal, mirrored mapping table, only-thruster-thrusts, tier table, energy gen/draw + grid values, grid round-trip, stats shape (400000 buffer decision).

## tectech\voidcraft\ship\VoidcraftRoleTest (4)
KEEP ALL: role bit distinctness, computeRoles mapping, efficiency penalty table, activeRoles.

## tectech\voidcraft\uss\CargoHoldTest (17)
KEEP ALL: unit conversion rules (1 item=1 unit, 100 mB=1 unit, mixed), clamp rules (add/remove/zero/negative capacity), transfer rules (clamp/null no-op/immutability), NBT round-trip + null->empty contract, miner yield fill (0.5 base) + starlifter fill, ship capacity = vc_cargo x CARGO_UNIT_MULTIPLIER.
DELETE 0

## tectech\voidcraft\uss\ItemVoidbaseBlueprintTest (3)
KEEP ALL: item NBT payload format (TAG_UUID/TAG_NAME/TAG_PROGRAM), empty-blueprint detection, wrong-item->null contract.

## tectech\voidcraft\uss\USSBaseAnchorTest (5)
KEEP ALL: factories + invalid-index error contract, NBT round-trip, fromMoveTarget resolution table (incl. null for HOME/SHIP/unresolved/unknown), null->star default, equals/toString display format.

## tectech\voidcraft\uss\USSBasePilotTest (20)
KEEP ALL: base pilot lifecycle — loop/wrap without STOP, base never discarded, instant own-anchor leg, skip rules (other anchor/HOME/SHIP + "unresolvable" log), resolveMoveTarget table, repair poll-to-done + refused-start skip, CONSTRUCT always skipped on base, stat string contract (BASE/PLANET:2/positions/ripples), variable/nextInt seed determinism + bound<=0, attach restores cursor without re-firing (lifecycle), corrupt pilot tag fails safe to COMPLETED, mining leg (mineTicks duration, wrap, no power = no-op, survives NBT, abandoned cleared).
DELETE 0

## tectech\voidcraft\uss\USSBaseSiteTest (5)
KEEP ALL: blueprint parts init, add-credit rules (overflow discarded, unknown/zero credit nothing), NBT round-trip, timed-construct leg (tpi=2000/power, exact deposit count, countdown holds at 0, restart seed, clear), construct leg NBT survival + corrupt-leg failsafe (parts survive).
DELETE 0

## tectech\voidcraft\uss\USSBlueprintProgramTest (8)
KEEP ALL: empty defaults (no NBT / tagless NBT), NBT program read, accepted-edit replace+clear-note contract, rejected-action keep+set-note contract, TAG_PROGRAM round-trip, empty removes tag (format decision), full session round-trip.
DELETE 0

## tectech\voidcraft\uss\USSCapabilitiesTest (6)
KEEP ALL: capability bit table + ALL=63 + high-bit masking, allowsCommand mapping (unknown command allowed), preset requirement table, work-kind bits/isWork/fromCommand/name labels, command labels (incl. CMD13 fallback), starlifter plasma factor/cap/floor rule.
DELETE 0

## tectech\voidcraft\uss\USSCommandTest (20)
KEEP ALL: move/repair/construct start+tick seam lifecycles, failure contracts (missing/unresolvable target, refused construct/repair + log strings), work-kind mappings (mine/scan/siphon each pin their command->kind), write/read variable semantics (slotless->0, var ref, source untouched), wait (0 immediate, 1/tick countdown, STATE_REMAINING tag, MAX_WAIT_TICKS clamp), STOP, command registry (all builtins + unknown->null).
DELETE 0

## tectech\voidcraft\uss\USSConditionTest (13)
KEEP 12; DELETE 1: testAccessors (pure getter validation, no decision pinned)
Kept decisions: op id round-trip + unknown->null, EQ case-sensitive, null sides as empty, LT/GT numeric-not-lexicographic (trim/negatives/equality), unparseable->false contract, NBT round-trip, read-null default, unknown-op->EQ fallback, structural equality.

## tectech\voidcraft\uss\USSFleetOrbitTest (32)
KEEP ALL: hover offset determinism/bounds/spread (uuid + seed), null/empty uuid->0, MAX_SHIPS_PER_USS>=100, planet orbit radius formula matches star renderer, orbit preservation, tilt at t=0, position matches explicit Matrix4f chain (guards JOML radians/mul footgun), target -1 -> star center, shell/dome bounds invariant (fleet never pokes the dome; MAX_DISTANCE 4 inside; HOVER_ABOVE_STAR>1.9), star center contract, anchor math consistency, shell point exactly on sphere + spread + degenerate, nudge determinism/bounds/clouds, gateway edge on dome surface + direction + idempotent + degenerate + sanity, band point on shell + 30-degree band + deterministic + zero-tilt + tracks planet + radius 0.
DELETE 0

## tectech\voidcraft\uss\USSNodeTest (19)
KEEP ALL: node kind contracts (default TARGET_HOME param), null-safe factories, negative cmd id -> 0, defensive param copy, repeat clamp (neg->0, 1e9->65535), body null-entry rejection + unmodifiable, depth/subtreeSize semantics, NBT round-trips (command + nested), and the 5 corruption read contracts (null/unknown type/over budget/over depth/non-compound entry/missing type -> null).
DELETE 0

## tectech\voidcraft\uss\USSPlanetColorTest (4)
KEEP ALL: opaque alpha bit, determinism, visual distinguishability, null -> white fallback.

## tectech\voidcraft\uss\USSPlanetDefinitionTest (19)
KEEP 17; DELETE 2: testOreStoresTypeAmountAndWeight + testDefinitionExposesAllFiveFields (pure accessor/builder wiring validation = scaffolding; the fields are exercised by the contract tests below)
Kept: ore validation contracts (NPE/IAE on null type, _NULL material, negative amount; zero amount allowed; non-positive/NaN/Inf weight), star-type support, sizeInRange rule incl. inclusive boundaries, null-safe allowsStarType, builder rejection contracts (blank id/texture, size range out of bounds/inverted, no allowed star type), empty ores/fluids allowed, unmodifiable collections (ores/fluids/starTypes), toString carries id.

## tectech\voidcraft\uss\USSPlanetRegistryTest (14)
KEEP ALL: register/get/contains, unknown/null -> null, null def NPE, duplicate id IAE, all() registration order, size/clear, pool filters by star type + null pool empty, catalog size 45 + idempotent re-register, catalog pools cover every star (all 45 per type), catalog data (mars/jupiter texture path/tier/ores), placeholder ore amount/weight defaults, size ranges within envelope.
DELETE 0

## tectech\voidcraft\uss\USSPlanetsTest (9)
KEEP ALL: per-star-type planet count range + variability, distinct definitions per cycle, determinism (star type + seed), different seeds differ, shared 45-planet pool across star types, orbital params in range (distance bands, >1.9 clears star, <= shell-4 margin, scale/speed/inclination 0-5 deg), materialsOf union order + null-safe, null star type -> main sequence.
DELETE 0

## tectech\voidcraft\uss\USSPlanetTypeTest (7)
KEEP ALL: 12-type ordered catalog + byId round-trip + out-of-range null, 3 distinct resolvable materials per type, 36 globally distinct materials, lang keys unique + namespaced (tt.voidcraft_uss.planet.), hologram visuals non-empty + unique, pools are complete disjoint 4+4+4 partition, pool null-safe.
DELETE 0

## tectech\voidcraft\uss\USSPositionTest (12)
KEEP ALL: star center at anchor -2, zero origin, distance symmetric Euclidean + null -> 0, lerp endpoints/midpoint + null -> this, add/subtract/scale + null contracts, dot/cross + null, normalize + zero no-op, immutability + value equals/hashCode, NBT round-trip + null -> origin, toString components.
DELETE 0

## tectech\voidcraft\uss\USSProgramDefaultsTest (9)
KEEP ALL: miner/starlifter/explorer chip contracts (target/command/label), chips distinct, chips within caps, chips survive NBT (param key "target" pinned as contract), cover -> chip priority table (scanner > siphon > mining > default; all chips end HOME; exact 3-instruction shape), params round-trip verbatim, constructor chip (3 instructions: MOVE nearest planet, CONSTRUCT no params, MOVE home).
DELETE 0

## tectech\voidcraft\uss\USSProgramEditorTest (38)
KEEP ALL: every editor op (insert/remove/move/copy/setParam/setCount/setConditionSide/setOp/apply) has a positive semantic test + rejection/error-contract tests (error text "body"/"node cap"/"nesting", out-of-capability rejections for SCAN/SIPHON/CONSTRUCT, input never mutated, edited program NBT round-trip). GUI-facing edit API = user contract.
DELETE 0

## tectech\voidcraft\uss\USSProgramExecutorTest (31)
KEEP ALL: program wraps forever (never self-completes), one node step per 20 ticks pacing, wrap without implicit legs, null-context tick safe, IF/WHILE/REPEAT semantics (incl. REPEAT 0 never runs body, tick-budget no runaway), condition reads earlier writes, MOVE/WORK leg lifecycle + re-arm per loop, unresolvable/unknown/failure skips with log strings ("unresolvable", "unknown command 99"), WRITE/READ between slots, STOP terminates rest / inside WHILE, lone command wraps, cursor NBT round-trip mid-WHILE/WAIT/MOVE (no re-run/no restart — persistence invariant), 5 fail-safe read contracts (null cursor, corrupt scope, unknown kind, running w/o scopes, corrupt active record -> COMPLETED).
DELETE 0
NOTE: comments say "decision #6" and "Phase A contract" — clean per no-design-history comment rule.

## tectech\voidcraft\uss\USSProgramSyncTest (26)
KEEP ALL: sync action wire contract — insert/remove/move/copy/param(var+literal+int)/count/cond/condop/apply-preset each with positive + rejection cases, outside-capability rejections, bad actions never throw, rejected actions keep program unchanged.
DELETE 0

## tectech\voidcraft\uss\USSProgramTest (14)
KEEP ALL: NBT cap/drop contracts (256>255 nodes -> whole program dropped, 255 OK; depth 9>8 -> dropped, 8 OK; nested corruption -> dropped; non-compound root -> dropped; 255-char literal truncated at construction), NBT round-trips, size/depth/nodeCount semantics.
DELETE 0

## tectech\voidcraft\uss\USSProgramViewTest (13)
KEEP ALL: view-model wire contract — root order/depth/paths/labels, children indented after parent, deep-nesting paths, null/empty programs, slot display per command (MOVE target, WRITE literal + VAR reference display "VAR 17", READ/WAIT/STOP, CONDITION left/op/right + isOp, REPEAT count), valueDisplay table (literal/VAR/STAT/null->""), row JSON round-trip + malformed->null, rowsJsonList parity with rows.
DELETE 0

## tectech\voidcraft\uss\USSRipplesTest (10)
KEEP ALL: 7x7x7=343 grid geometry, ripple count within star range + seed-variability, determinism (same seed identical), different seeds differ, set size == count, every point on one of the 3 shells (visual invariant), shell radii evenly spaced, radius clamped, null star type -> main sequence.
DELETE 0

## tectech\voidcraft\uss\USSStarDefinitionTest (26)
KEEP 23; DELETE 3: testStarMaterialStoresMaterialAndWeight (pure getter), testStarExposesAllFields (pure builder wiring; its unique pin — color defaults to white 0xFFFFFFFF — preserved as testColorDefaultsToWhite), testNameMethodIsInvocableAndSupplied (supplier-invocation scaffolding)
Kept: material validation contracts (null NPE, _NULL sentinel IAE, non-positive/NaN/Inf weight IAE), color round-trip, color default white, size/planetCount inclusive ranges, evolutionTarget null/set, builder rejections (blank id/null nameMethod/blank type, size range OOB/inverted, luminosity OOB incl. boundaries 0.0/16, planet range OOB/inverted, ripple range OOB/inverted, missing material, blank texture), ripple range exposed + default full span (MIN_RIPPLES/MAX_RIPPLES=128), toString id+type.
NOTE: testStarExposesAllFields replaced by testColorDefaultsToWhite (trimmed, decision pin kept).

## tectech\voidcraft\uss\USSStarRegistryTest (18)
KEEP ALL: register/get/unknown/null, null def NPE, duplicate id IAE, all() order, size/clear, evolution target resolution (registered / absent -> null / unregistered -> null), catalog registers 3 stars + idempotent, catalog preserves the 3 legacy star classes (main_sequence/white_dwarf/supermassive ids), catalog types are display names, catalog materials weighted (content-integrity guard), catalog colors follow the visual spec (0xFFFFD640/0xFFFFFFFF/0xFF5A8CFF), starColor helper resolves + DEFAULT fallback, catalog evolution chain (main_sequence -> white_dwarf, others terminal), catalog bounds within envelope.
DELETE 0

## tectech\voidcraft\uss\USSTargetTest (10)
KEEP ALL: per-kind payload rules (star no payload, planet index, ship per-launch seed), equality (same payload different kind !=), NBT round-trips per kind, NBT null/unknown/missing kind -> star (defensive contract), write null-safe, toString kind name.
DELETE 0

## tectech\voidcraft\uss\USSValueTest (11)
KEEP ALL: literal null -> empty contract, literal 255-char cap (truncated at construction), variable slot clamp (neg->0, 999->255), stat id clamp, kind byId round-trip + unknown->null, NBT round-trip all kinds, read null -> empty literal, read unknown kind -> literal fallback, read missing field -> safe defaults, equality (VAR and STAT with same number are distinct).
DELETE 0

## tectech\voidcraft\uss\USSVariableSpaceTest (13)
KEEP ALL: fresh empty + unwritten, out-of-range reads empty, set = new instance (immutability), set overwrites slot (writtenCount = distinct slots), set null -> empty but written, set out-of-range no-op (same instance), writtenCount distinct-slot semantics, boundary slots 0/255, NBT sparse round-trip (only written slots), NBT list well-formed (i/s tag shapes = serialized format), read null -> fresh, read drops OOB + non-compound entries (corruption contract), equality.
DELETE 0

## tectech\voidcraft\uss\USSVeinMathTest (4)
KEEP ALL: total vein amount (sum + null-safe), junk entries ignored, stone dust x3 multiplier, mining EU cost = amount x unit (zero/negative -> 0).
DELETE 0

## tectech\voidcraft\uss\VoidcraftActiveBaseTest (15)
KEEP ALL: the VOIDBASE integrity lifecycle — launch pulls integrity from payload (sum of components), fallback re-derive from grid, blueprint-item payload full integrity, corrupt payload -> DEFAULT_INTEGRITY floor, decay 1 per 20 ticks, zero decommissions (tickIntegrity returns true at/under 0, stays true), repair refills + clamps at max + no-op at max/zero/negative, NBT round-trip (uuid/name/seed/anchor/integrity mid-countdown/position/cargo marker/payload width; counter resumes, does not restart), energy starts full 400_000 + ticks 2000 EU/t + capped at buffer, setEnergy clamps, repair draw 2000 EU restores 1 integrity per 20 ticks, repair requires energy (no energy/insufficient/max -> no-op), energy persists across round-trip, read rejects corrupt tag (null/missing payload/empty payload -> null).
DELETE 0

## tectech\voidcraft\uss\VoidcraftActiveShipTest (38)
KEEP ALL: launch holds at origin (default origin zero), startLeg arming (OUTBOUND, distance, countdown, leg id increment, position unchanged until done), countdown latches at 0, completion consumed exactly once (side-effect once), zero-length leg completes next tick, hold parks ship mid-leg, leg id increments per leg, setCargo, cargo null until caller produces (ship never fabricates cargo), NBT round-trips (full + without targets + destination/distance + leg-done latch survives and consumes exactly once AFTER reload + body static + target + per-launch seed + work kind), missing leg tags -> safe defaults, read allows HOVERING, read rejects DOCKED (never persisted) + missing/unknown (null), constructor loadout init from payload vc_build_parts, consumeBuildParts clamps + drops empty keys + unknown key consumes nothing, build loadout survives NBT (durable ship state, not payload re-expansion), integrity starts at max (missing -> 0, lost first tick), integrity 1/sec decay, zero loses ship, zero integrity loses immediately, integrity round-trips mid-countdown (resumes, does not restart), mining leg always visible (mineTicks bounds + monotonic), constructTicksPerItem (100 power -> 20 t = 1 part/s, power<1 -> base 20, min 1), construction power read live from payload (missing -> 0), legTicks dispatch table (state/kind; docked/hover/null -> 0), scanTicks bounds + saturation, workTicks kind-aware (MINE/SCAN/SIPHON per-kind, TRAVEL/unknown -> mining table), starliftTicks bounds + saturation, leg work kind survives NBT, travel time grows with distance (monotonic + bounds), travel speed multiplier (15 blocks @ speed 5 -> 120 ticks = SHIP_SPEED_MULTIPLIER 5.0).
DELETE 0

## tectech\voidcraft\uss\USSShipCargoTest (17)
KEEP ALL: weighted ore yield (weight/6 per ore), reserve cap depletes yield, reserve initialized from definition (amount x planetSize^2), depletion across missions (reserve persists), null/empty definition -> empty cargo, starlifter 3 weighted fluids (base x weight x sizeFactor), fluid materials match star, null star -> main sequence, unregistered star -> empty, sample size in range (128 seeds), star render size = 2/3 x sqrt(size) (visual formula), reserve NBT round-trip, reserve clamp at 0, toStacks split into 64s (vanilla cap, lossless), toStacks skips unknown ids, readItems never null, fluid entry resolves to material (+ unknown -> _NULL sentinel).
DELETE 0

## tectech\voidcraft\uss\USSShipPilotTest (18)
KEEP ALL: empty program holds, chip program runs leg -> work -> delivery in order (20-tick pacing, OUTBOUND, leg duration from world seam, leg id increment, destination resolution, delivery to origin, side-effect exactly once, 3 legs), random planet target (same shape; targetPlanet index drives client hover), work leg is a real leg (workTicks + side-effect only on completion), REPEAT loops work leg exactly N times, unresolvable move skipped + program continues ("unresolvable" log), refused leg start skipped (no stuck legs), work side-effect fires once across save/reload mid-work-leg (persistence invariant), HOME resolves to origin, finished program wraps and re-runs (never ends without STOP), STOP ends + holds, variables + stats flow (CARGO_FREE = capacity x 100, live POSITION_X double, RIPPLES_UNSCANNED, unknown stat -> "", nextInt deterministic + range + bound<=0 -> 0), resolve funnel (literal/variable/null), distanceTo live (null -> 0), writeToNbt nests executor + pilot state (vc_exec/vc_leg_work/vc_leg_home/vc_last_kind/vc_origin tags), attach without pilot tag is fresh, attach with corrupt pilot fails safe to COMPLETED, attach restores leg bookkeeping.
DELETE 0

## tectech\voidcraft\uss\USSShipStatTest (4)
KEEP ALL: byId round-trip for every stat, ids distinct, unknown id -> null, registry covers the full stat table (ids 0-11 with names = the stat-id wire contract).
DELETE 0

## tectech\voidcraft\uss\VoidcraftUSSTest (21)
KEEP ALL: cold initial state baseline, ignite uses star tables (lifespan from table, star type + tier + state), ignite clamps tier (neg->0, >MAX->MAX, null star -> MAIN_SEQUENCE), lifespan decrement + burnout (1/tick; immutable copy preserves tier/starType/state; original untouched; cold stays 0), ship registry (dedupe, null no-op), ripple scan state (empty/accumulates/immutable copy-on-write, NBT round-trip + empty -> no tag written = format decision), star type tables (positive lifespan/draw, types differentiated, null fallback), clampTier (0..8 rule), variable space fresh (empty-string reads, writtenCount 0), withVariables immutable + preserved by with-chain (copy-on-write lifecycle), variable NBT sparse (only written slots), fresh space on cold + ignite, fresh space writes no tag, NBT round-trips (cold + ignited incl. ship list + double-pass idempotence), read rejects corrupt (null/empty/bad state/bad tier/bad star/bad lifespan -> null).
DELETE 0

## tectech\voidcraft\render\AssemblerVisualsTest (13)
KEEP 12; DELETE 1: scanAxesAreOrthogonalToEachOtherAndToTheFront (orthogonality implied by the exact-plane scanAxes tests per facing)
Kept: exact-plane axis vectors per facing (visual spec), scan cell world positions (north -Z depth, up XZ plane), cell bounds, preview spin axis + gate, assembler preview origin.
DELETE 1

## EXECUTION LOG (2026-08-28)
Deleted methods (7):
- AssemblerVisualsTest.scanAxesAreOrthogonalToEachOtherAndToTheFront
- VoidcraftBlueprintTest.testIntegritySumsComponents
- USSConditionTest.testAccessors
- USSPlanetDefinitionTest.testOreStoresTypeAmountAndWeight
- USSPlanetDefinitionTest.testDefinitionExposesAllFiveFields
- USSStarDefinitionTest.testStarMaterialStoresMaterialAndWeight
- USSStarDefinitionTest.testNameMethodIsInvocableAndSupplied
Replaced (1): USSStarDefinitionTest.testStarExposesAllFields -> testColorDefaultsToWhite (trimmed to the unique color-default pin)
Renamed (1): VoidcraftComponentTest.testPass23PlaceableSet -> testPlaceableSet
Comment hygiene: removed all pass-N/Phase-N/decision-# design-history references from 14 voidcraft test files (VoidcraftBlueprintTest, USSConditionTest n/a, USSStarDefinitionTest n/a, USSFleetOrbitTest, USSProgramExecutorTest, USSProgramViewTest, USSProgramSyncTest, USSProgramEditorTest, USSShipPilotTest, USSShipStatTest, USSBasePilotTest, VoidcraftUSSTest, VoidcraftActiveShipTest, VoidcraftActiveBaseTest n/a, TileEntityVoidcraftShipTest, VoidcraftShipFxTest, VoidcraftFluidPoolTest, USSCommandTest, USSPlanetsTest, USSPlanetColorTest, USSPlanetTypeTest, USSPlanetDefinitionTest n/a, USSShipCargoTest, CargoHoldTest, FakePilotWorld, FakeUSSContext). Final grep for pass/phase/gate/decision references: clean (only functional "client's phase key" remains).
Net test delta: 7 methods deleted, 1 trimmed in place (replaced). Verified after the pass: 66 suites, 872 tests, 0 failures/errors (`gradlew test`, BUILD SUCCESSFUL). Note: the suite had already grown past the 846 multiblock-record baseline before this audit; the audit itself removed 7 tests.