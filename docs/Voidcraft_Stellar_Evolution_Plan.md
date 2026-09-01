# Voidcraft Stellar Evolution — Phased Implementation Plan

Branch: `eoh-rework`. Working loop: creative mode + `gradle runClient`; correctness gates: `gradlew compileJava` + full test suite (baseline ~1000 tests 0F). No backwards compatibility (standing directive) — new NBT tags need no migration paths. Recipes out of scope until asked.

**Rev 3** — incorporates the user's architecture corrections: Voidbases are Voidships (wiped by acceleration); the Injector/Stabilizer/Lens are true *infrastructure* built via new multiblock components (Dyson-Swarm-like, not base components); the Matrix is a Voidbase component whose activation is a new ship-program command; D1 auto-ignite, D2 injector-fed, D6 7×7×12 lens, D8 proportional orbit speedup confirmed. **Rev 3**: stellar evolution (and terminal death) wipes ALL infrastructure too; the new system recreates voidcraft slots, planets, star and all ripples from scratch.

**Rev 4 (user correction)** — the infrastructure model was over-built as world behaviour: the new multiblocks are plain **Voidcraft Multiblock Components, exactly like the Satellite Rail Launcher** — dormant in-world structures (no-op MTEs, structure check only) that the assemblers digitize into Voidbase blueprints. The infrastructure itself (injector / stabilizer / lens / matrix *capability*) is an **internal of the USS** (state + system-view rendering, like ships and gateways): NO separate functional MTE exists in-world. Consequences: the "world-only" classification is gone — all four components are **station-only** (rejected in ship builds with `voidcraft_launcher_station_only`, allowed + digitized in Voidbase builds); the self-registration scan / USS position map / MUI registration panel / `USSInfraKind` / ripple-binding helper built in the first Phase-2 pass were removed; MTE IDs are **32085–32092** (renderer contract `32058 + meta` — the rev-3 "32076+" was stale, Frame2/3/4 occupy 32076–32078). Wipes now target USS internal state only (no world-block destruction).

**Rev 5 (user correction — infrastructure is BUILT, like the Dyson)** — the three shell infrastructures are no longer mere blueprint-read capabilities: their components (Stellar Injector / Continuum Stabilizer / Stellar Gravitational Lens) are **infrastructure builders**, exactly like the Satellite Rail Launcher. A Voidbase standing at a target (its `USSBaseAnchor`: the star, or ripple `i`) runs its builder components (the USS machine tick), which consume the builder's **component item** (new: three items, `ItemVoidcraftInfraComponent` — special cargo pulled by the gateway, delivered to the build site + finished base hold) ONE UNIT per `INFRA_BUILD_INTERVAL` (10 ticks) into the target's shell — progress tracked in the existing `USSInfrastructure` string-key ledger under typed keys (`injector:star` / `injector:ripple:<i>` / `stabilizer:ripple:<i>` / `lens:star`), with CAPACITY = the shell's triangle count (pure geometry, `USSInfraShell` — the server's capacity and the client's mesh share the one formula). **Exclusivity (one structure per target)**: the star hosts at most one of Dyson Swarm / Injector / Lens (a built Dyson blocks the builders, a built injector/lens blocks the Satellite Rail Launchers); a ripple at most one of Stabilizer / Injector. **Functional = fully built** (count = capacity): partial shells render (triangle shells fill fractionally — light-gray/orange injector around the star, dark-gray/light-green lens around the star, small gray/dark-purple stabilizer around the ripple point) but have no effect until complete. The **Matrix remains the only dormant component** (no builder; N from the blueprint count, 0 at expiry until the STABILIZE legs land in Phase 5). The decay pass (Dyson-only now — the new shells never decay) and the expiry reads (stabilizer active = any fully-built stabilizer on a scanned ripple; lens present = the star's lens shell fully built) were reworked accordingly.

## 1. Spec (resolved into formulas)

### 1.1 Stellar acceleration (USS controller)
- Every 20 ticks (1 s): drain **all** Molten Spacetime (`Materials.SpaceTime.getMolten()`) from the two input hatches.
- Lifetime reduction = `max(1, (long) sqrt(consumedMB))` machine ticks ("cannot be zero").
- **All Voidships — flying ships AND Voidbases — are wiped** on that tick when `consumed > 0` (the existing full `discardAllShips()`; no selective variant). Only true infrastructure survives: the Dyson Swarm and the Stellar Injector / Continuum Stabilizer / Gravitational Lens (USS internal state, section 2.2).
- Orbits run faster while accelerating: the USS virtual orbit clock (section 2.4) advances **proportionally** to the consumption (D8): during an active second, per-tick advance = `1 + sqrt(consumedMB) / ORBIT_SPEEDUP_DIVISOR` (placeholder constant, e.g. 200 → 10 000 mB/s ≈ ×10 orbit speed).

### 1.2 Star expiry (replaces the current `starBurnsOut()`)
1. **Spacetime yield**: the USS outputs `Y = USSConstants.spacetimeYieldForType(type)` mB of Molten Spacetime (new per-star placeholder table) to the output hatch/buses.
2. **Universium conversion** (from the yielded Spacetime, using the expiring system's scanned ripples):
   - `R` = count of ripple points that are **both** scanned and actual ripples (`uss.isRippleScanned(i) && rippleField.isRipple(i)`).
   - Denominator `D = 200_000` mB/litre if any Continuum Stabilizer structure is **fully built (count = capacity) at expiry** (a partial shell has no effect), else `10_000_000` (50×).
   - `L = (Y / D) × R` litres of Universium; Spacetime consumed `= L × 100_000` mB (the 100000:1 ratio).
   - Matrix multiplier: `N` = sum of effective weights of **active STABILIZE legs at the exact expiry tick** (UMV-fed = 1, UXV-fed = 2, section 2.5); multiplier `= fib(N+2)` with `fib(1)=fib(2)=1` → N=0: ×1, N=1: ×2 (F3), N=2: ×3, N=3: ×5, N=4: ×8, …
   - Final: output `(Y − L×100_000)` mB Spacetime + `(L × multiplier) × 1000` mB Molten Universium (`Materials.Universium.getMolten()`).
   - **Pinned example**: Y = 100 000 000 mB, R = 8, no stabilizer → L = 80 → out: 92 000 000 mB Spacetime + 80 000 mB Universium.
3. **Evolution outcome** (section 1.3). If an outcome exists, a **new star system auto-ignites** in the same controller (D1); otherwise the system terminates (current burnout behaviour). **Both paths wipe the entire system**: all Voidships (full `discardAllShips()`), all infrastructure (USS internal state cleared; injector buffer cargo lost), and star, planets and all ripples are recreated as fresh system data (new seed, fresh field, Dyson count reset).

### 1.3 Evolution outcome table
`F` = primary-material remaining fraction (0…1; unsiphoned reserve = 1.0). `sizeFactor` = current star size / original sampled size (sampled at ignition, ≥ 1.0 — the Stellar Injector only grows the star, up to the 1.5 cap). `atMax` = sizeFactor ≥ 1.5 (ε). `fed` = sizeFactor > 1.0 (ε). `depleted` = F == 0.

| Star (id) | Outcome | Condition |
|---|---|---|
| red_dwarf, yellow_dwarf, red_giant, white_dwarf (dwarf chain / main sequence — "cool down") | catalog target (white_dwarf / red_giant / white_dwarf / black_dwarf) | `roll < (1 − F)` (0% at full, 100% at empty) |
| blue_giant / blue_supergiant ("explode") | red_supergiant / hypernova | `atMax && roll < (F > 0.75 ? 1.0 : F/0.75)` (100% above 75% full → 0% at empty) |
| red_supergiant | supernova | deterministic |
| supernova | neutron_star if **fed** (size boosted above original by the injector — D2 confirmed), else black_hole | — |
| hypernova | neutron_star | deterministic |
| neutron_star | gravastar if a Stellar Gravitational Lens infrastructure stands around it (priority) · magnetar if `depleted && atMax` · quark_star if `depleted` · terminal otherwise | — |
| black_dwarf, black_hole, quasi_star, magnetar, gravastar, quark_star | terminal | — |

The catalog's `evolutionTarget` field stays as nominal-chain data (pinned by `testCatalogEvolutionTable`); the resolver is the mechanic's source of truth.

### 1.4 Infrastructure model (rev 5)
**True infrastructure** — survives the acceleration wipe, wiped by evolution/terminal death (Dyson-Swarm-like lifecycle). The infrastructure is an **internal of the USS** (state + system-view rendering). The three shell infrastructures are **BUILT** by their station-only components (the Satellite-Rail-Launcher pattern — section 2.2); the Matrix is the only dormant one. **No separate functional MTE exists in-world** (the four in-world structures stay dormant no-ops — structure check only).

| Infrastructure | Component (station-only) | Target | Effect |
|---|---|---|---|
| Stellar Injector | Infrastructure Builder (7×7×12 in-world, dormant) | the star — or a spacetime ripple point (the base's anchor) | star scale-boost / matter-feed (D2) |
| Spacetime Continuum Stabilizer | Infrastructure Builder (5×5×7 in-world, dormant) | the base's anchor ripple point | passive; **active** iff its shell is fully built; 50× Universium rate at expiry (applies once, any active stabilizer) |
| Stellar Gravitational Lens | Infrastructure Builder (7×7×12 in-world, dormant) | the star | passive; at neutron-star expiry the outcome is gravastar (overrides depletion rules) |
| Hyperdimensional Stabilization Matrix | Voidbase component (7×7×10 in-world, dormant; the ONLY one with no builder) | the base carrying it | "does nothing on its own": enables the **STABILIZE** ship command (section 2.5); weight counts at expiry |

- **Construction**: a base standing at a target (its `USSBaseAnchor`: star, or ripple `i`; a planet anchor builds nothing) runs its builder components in the USS machine tick — one structure unit per builder component per `INFRA_BUILD_INTERVAL` (10 ticks), drawing the builder's **component item** from the base's hold. The component items (three new items, `ItemVoidcraftInfraComponent`) are special cargo: the gateway pulls them from the input buses (like Power Satellites), the constructor delivers them to the build-site cargo, the finished base's hold receives them at spawn. Progress = the target's key in `USSInfrastructure` (`injector:star` / `injector:ripple:<i>` / `stabilizer:ripple:<i>` / `lens:star`); CAPACITY = the shell's triangle count (`USSInfraShell` — pure geometry shared by the server's capacity and the client's mesh).
- **Exclusivity (one structure per target)**: the star's shell slot hosts at most one of Dyson Swarm / Injector / Lens — built counts occupy the slot (even a partial shell), the Satellite Rail Launchers stop while an injector/lens shell exists; a ripple hosts at most one of Stabilizer / Injector.
- **Functional = fully built**: partial shells render (the triangle shell fills fractionally as units join — light-gray panels / orange cores for the injector, dark gray / light green for the lens, a small gray / dark-purple shell at the ripple for the stabilizer) but have no effect until count = capacity.
- **All four components are station-only**: rejected from ship blueprints (`voidcraft_launcher_station_only`); digitized by the Voidbase Assembler (15×15×15 scan volume) into a Voidbase blueprint. The in-world structures are no-ops — built via survival construct / creative, validated by their own structure check, digitized like the launcher.
- Sensor cargo rides the hold **special** axis (new keys `sensor_umv` / `sensor_uxv`, pattern: `USSInfra.KEY_POWER_SATELLITE`); the Gateway pulls sensor cargo for matrix-carrying bases (extend the launcher cargo pull at `MTEVoidcraftGateway` ~L489).
- **Lifecycle**: infrastructure is per-star-system. It survives stellar acceleration (ships/bases are wiped; the USS internal state persists — the built shells keep their counts). On stellar evolution or terminal death the entire system is wiped — the internal state (shell counts, stabilizer bindings, lens presence, injector buffer cargo) is cleared and the new system starts with no infrastructure (D12/D13 resolved). The dormant in-world component blocks are ordinary blocks — neither wiped nor recreated by evolution.

## 2. Architecture decisions (grounded in the codebase)

### 2.1 Wipe semantics
Two distinct wipes:
- **Acceleration** uses the **existing full** `discardAllShips()` (ships + bases + sites + transfers + countdowns). Infrastructure SURVIVES because it is USS data on `VoidcraftUSS` (star size, Dyson count, injector buffer, the internal infrastructure state) — none of which `discardAllShips()` touches. (The in-world component structures are ordinary blocks and are untouched as well.)
- **Evolution / terminal death** wipes EVERYTHING: `discardAllShips()` + the fresh system model — the model swap IS the wipe (`ignite` on the evolution path, `toCold()` on the terminal; the injector buffer, star size, Dyson count and scan state all reset with the old system; there are NO world blocks to destroy). Star, planets, ripples (fresh `USSRipples.generate(type, ignitedAt)`) all reset as fresh system data.

### 2.2 Components → built infrastructure (rev 5)
The four new components are plain dormant multiblock components (the launcher pattern: `MTEVoidcraftMultiblockBase` subclass — own structure check, no hatches/energy, rechecked ~50 ticks). Built in-world, digitized by the Voidbase Assembler into a Voidbase blueprint; the in-world structure holds its shape for the assembler and does nothing else. The three shell components are also **infrastructure builders** — exactly like the Satellite Rail Launcher (the machine tick on a standing base consumes the builder's cargo from the base's hold, one unit per interval, into the USS infrastructure ledger). The Matrix is the only component that builds nothing.

The constructed shells are **internal state of the USS** (progress + rendering in the system view, never separate world MTEs) — the existing `USSInfrastructure` string-key ledger, extended with typed keys (`USSInfraBuild`): `injector:star` / `injector:ripple:<i>` / `stabilizer:ripple:<i>` / `lens:star` (the Dyson's `dyson_swarm:star` unchanged). Builder target = the base's `USSBaseAnchor` (star anchor → the star's shell slot; ripple anchor `i` → ripple `i`; planet anchor → nothing). Build capacity = the shell's triangle count (`USSInfraShell` pure geometry — the client bakes the same mesh it renders, so count/capacity = the rendered fill).

Per-kind state (USS data; the infrastructure ledger rides `VoidcraftUSS`'s NBT):
- **Injector**: the built shell on the star (or a ripple) + the star's cargo buffer (`CargoHold`, `vc_uss_injector_buffer`) + star size (section 2.3).
- **Stabilizer**: the built shell on its anchor ripple; **active** at expiry iff fully built (drives the 50× denominator).
- **Lens**: the built shell on the star (neutron-star expiry → gravastar when fully built).
- **Matrix**: count (STABILIZE eligibility + expiry weights via the live legs) — no builder.
- **Wipe**: the expiry pipeline's model swap (`ignite` / `toCold`) clears all of the above (no world destruction; the new system is a fresh model).
- **Rendering**: the star's shells (Dyson / injector / lens — exclusive, at most one) ride the star render TE (`TileEntityEyeOfHarmony`: `setDysonSwarm` + `setInfraShell(type, count, capacity)`) and draw in `EOHTileEntitySR` via the shared triangle-shell path (`EOHRenderingUtils.renderUSSInfraShell`, edge + tints parameterized per type); the ripple shells ride the fleet TE (`TileEntityVoidcraftShip.setRippleInfraShells`, one `[x, y, z, count, capacity]` per built stabilizer on a revealed ripple) and draw in `RenderVoidcraftShip` at the ripple positions. The decay pass (`tickStarInfrastructure`) touches the Dyson key ONLY — the new shells never decay.

### 2.3 New data on `VoidcraftUSS`
- `starSize` (double): set from `USSPlanets.sampleStarSize(type, ignitedAt)` at ignition; the injector raises it up to 1.5× the original. Replaces the static `starSizeFor(type, ignitedAt)` call sites (render TE, fleet TE, orbits, satellite capacity) — they read `uss.getStarSize()`. New NBT tag `vc_uss_star_size`.
- `virtualTime` (long): the USS orbit clock. Advances by 1 per machine tick; by `1 + sqrt(consumedMB)/ORBIT_SPEEDUP_DIVISOR` during an active accelerating second (D8). Server orbit math (`worldTimeTicks()` → `ussOrbitTime()`) and client render both use it (they currently both use `getTotalWorldTime()` — see `worldTimeTicks()` javadoc). New NBT tag `vc_uss_virtual_time`.
- `injectorBuffer` (`CargoHold`): the star injector's cargo buffer. New NBT tag `vc_uss_injector_buffer`. (Fate at evolution: D12.)
- `USSInfrastructure` keys (rev 5, landed): the existing string-key ledger gains the typed builder keys (`USSInfraBuild`): `injector:star` / `injector:ripple:<i>` / `stabilizer:ripple:<i>` / `lens:star` alongside `dyson_swarm:star`. The infrastructure NBT round-trips as before — the ledger is generic over keys; only the Dyson key is decayed (`tickStarInfrastructure`).
- NBT format version bump (no migration).

### 2.4 Acceleration tick (in `MTEUnstableSolarSystem.onPostTick`)
Per machine tick while ignited:
1. `accTicks++`; at 20: drain all Molten Spacetime from `mInputHatches` (hatch pattern: Gateway `pullLaunchFuel`);
2. if `consumed > 0`: `uss = uss.withLifespan(remaining − max(1, sqrt(consumed)))`; `discardAllShips()` (full wipe); virtual clock runs at the proportional rate for that second;
3. normal `-1/tick` lifespan continues; `lifespan ≤ 0` → expiry pipeline (1.2).

Client: `TileEntityEyeOfHarmony` gains `ussOrbitTime` (0 = legacy world time) riding the description packet; `EOHTileEntitySR` + ship-hologram hover math use `syncedTime + partialTicks` when set. Sync points: existing `syncStarRenderBlock` / `syncFleetRenderBlock`.

### 2.5 The STABILIZE command (D4 — "a new command — the user needs to synchronize bases to expiration")
New work action in the programming framework: `USSWorkKind.STABILIZE` + `USSCommandStabilize` + registry/MUI-editor/preset entries + USS handler (`stabilizeTick(ship)`, pattern `constructTick`).
- **Eligibility (else SKIP)**: the ship's blueprint contains the STABILIZATION_MATRIX component; the ship is a base anchored to a ripple (anchor kind RIPPLE); the anchor ripple is scanned and an actual ripple; an internal Stabilizer instance is active and bound to that ripple.
- **Leg**: fixed duration from the node argument (the user times the window against the expected expiry — acceleration wipes the base, so the window must land on the *natural* expiry). While the leg is active (server ticks):
  - energy: drain from the base's energy buffer per tick (stall-on-shortfall, travel semantics);
  - sensors: every 2000 ticks consume 1 sensor from the hold special axis (UXV preferred over UMV); the leg's **weight** = tier of the last sensor consumed (UMV = 1, UXV = 2); a leg that has no sensor available cannot start (SKIP).
- **At expiry**: `N` = Σ weights over currently active STABILIZE legs; no persistent matrix state needed (query live ships).
- Multiple ripple-anchored bases can run STABILIZE concurrently (each needs its own bound stabilizer); weights sum (D10).

### 2.6 Injector behaviour
USS tick (while the system has a FULLY BUILT star injector shell — rev 5: the built count, not a blueprint read): drain `injectorBuffer` at a fixed pace (units per interval, placeholder constant), cost per size step `= INJECTOR_COST_PER_SIZE_UNIT × currentSize² × Δs` (monotone in size — "material required depends on the size of the star"), raising `uss.starSize` toward the 1.5× cap; render + fleet TE sync on change. No energy (the injector is USS-internal state — cargo + time only).

**Cargo delivery — `SEND`/`TAKE` to the star** (the "in-system mining logistics / external supply" axis):
- New target constant `USSProgramDefaults.TARGET_STAR` for SEND/TAKE: the destination is not a ship's hold but the **injector buffer** (`uss.getInjectorBuffer()`), resolved in `cargoTransferStart` (new branch before the fleet-target resolution; preconditions: a formed injector is registered, else the leg SKIPs with a log line — pattern of the existing `cargoTransferStart` error paths).
- The transfer leg is the existing Hold→Hold engine (`USSCargoTransfer.arm(filter, amount, transferTicksPerUnit(power))`, one cargo unit per `transferTicksPerUnit` per logistics power, `amount` default = ALL, `filter` default = `*`): `leg.tick(sourceShip.getHold(), injectorBuffer)` — the injector buffer is a `CargoHold`, so no new transfer code, only the destination resolution.
- **Location rule**: SEND/TAKE require a SHARED location between the two endpoints (`sharesLocation`); the star is already a first-class location (`USSLocation.shared` — planet orbit, the star, a ripple site). A ship hovers at the star via the existing `MOVE target=star` and then `SEND target=star` delivers; `TAKE target=star` retrieves from the buffer (symmetric, lets a ship reclaim over-delivered cargo before evolution wipes it).
- **In-system mining logistics**: a mining ship extracts on a planet (`EXTRACTION` → hold), travels to the star, hovers, `SEND target=star` (optional `filter`/`amount` to pick which ores feed the star).
- **External supply**: the player pipes material into the Gateway input hatches; a launched ship carries it in its hold (existing launch-loadout path) and delivers the same way.
- The injector buffer is USS state — it is wiped on evolution/terminal death (section 2.2) and survives acceleration (D12 resolved: cargo wiped with the system).

### 2.7 Component catalog
New `VoidcraftComponent` entries appended at the END (meta==ordinal invariant): `STELLAR_INJECTOR` + `_CASING`, `CONTINUUM_STABILIZER` + `_CASING`, `STELLAR_LENS` + `_CASING`, `STABILIZATION_MATRIX` + `_CASING` (metas 27–34). Four structure shape classes (`SatelliteLauncherStructure` pattern) + four DORMANT MTEs (`MTEVoidcraftMultiblockBase` subclasses, launcher pattern — no behaviour) + `MetaTileEntityIDs` **32085–32092** (renderer contract `32058 + meta`) + `CustomItemList` + `VoidcraftLoader` registration (lang, icons, `COMPONENT_ENTRIES`). Validation (rev 4): all four are **station-only** — rejected in SHIP blueprints (`voidcraft_launcher_station_only`), allowed + digitized in Voidbase builds by the 15×15×15 voidbase assembler (the rev-3 "world-only" classification and its error key are gone).

## 3. Core math (bare-JVM, unit-tested first)

1. **`USSStellarEvolution`** — `resolve(USSStarType starType, double primaryFraction, double sizeFactor, boolean lensPresent, java.util.Random rng) → Optional<USSStarType>` implementing the 1.3 table. Seeded `Random` → deterministic tests.
2. **Expiry yield / Universium** (pure helpers): yield table `spacetimeYieldForType`, `universiumLiters(yield, denominator, ripples)`, `spacetimeConsumed(liters)`, `fibonacci(int n)`, `matrixMultiplier(int weightSum)`.
3. **Acceleration** (pure): `lifespanReductionPerSecond(consumedMB) = max(1, (long) sqrt(consumedMB))`; `orbitAdvancePerTick(consumedMB) = 1 + sqrt(consumedMB)/ORBIT_SPEEDUP_DIVISOR` (D8).
4. **Injector** (pure): `sizeCap(original) = 1.5 × original`; `cargoUnitsForSizeDelta(currentSize, delta)`.
5. **Ripple counting / binding** (pure over `USSRippleField` + scanned set): `activeScannedRipples(field, scanned)`; nearest-ripple binding for the stabilizer.

## 4. Phases

Each phase ends at a gate: `gradlew compileJava` + full suite green; phases 1, 2, 3, 4, 5 additionally end with a user playtest via `gradle runClient` (playtest results reflect the current source).

### Phase 0 — Core data + math (bare JVM, no machine code)
- `USSStellarEvolution` + tests: outcome table; chance boundaries (cooling 0%@full/100%@empty; explosion 100%≥75%/0%@empty; seeded rolls); priority (lens > magnetar > quark); deterministic rows; terminals.
- Yield/Universium/Fibonacci helpers + tests: **pinned example** (100M, 8 ripples → 80 L, 92M remaining); 50× rate; multiplier table (N=0…6 → 1,2,3,5,8,13,21).
- Acceleration helpers + tests (1 mB→1 tick; 10 000 mB→100; orbit advance proportional).
- Injector size/cost helpers + tests (cap 1.5×; monotone cost).
- `VoidcraftUSS`: `starSize`, `virtualTime`, `injectorBuffer` + NBT roundtrip tests.
- `USSConstants`: new tunables (yield table, ratios, `ORBIT_SPEEDUP_DIVISOR`, intervals, costs).
- **Gate:** compile + suite green.

### Phase 1 — Stellar acceleration (machine + client)
- `MTEUnstableSolarSystem`: per-second spacetime drain, sqrt lifespan reduction, **full** `discardAllShips()` wipe (all Voidships incl. bases), virtual clock advance (proportional), fleet/render sync.
- Client: `TileEntityEyeOfHarmony.ussOrbitTime` + packet; `EOHTileEntitySR` + ship hover use the synced clock.
- MUI: acceleration panel (spacetime rate, ticks/s reduction, orbit rate) + ship-loss log line.
- **Tests:** clock advance while active/inactive (pure portion); MTE wiring by compile + playtest.
- **Gate:** suite green + playtest (feed spacetime: lifespan falls by √, orbits speed up proportionally, ships AND bases wiped, Dyson swarm count + star persist).

### Phase 2 — Infrastructure components: catalog, structures, validation
- 8 new `VoidcraftComponent` metas (27–34) + station-only classification; 4 structure shape classes (7×7×12 injector, 5×5×7 stabilizer, 7×7×12 lens, 7×7×10 matrix); 4 DORMANT MTEs (launcher pattern — own structure check, no behaviour) + MTE IDs **32085–32092** + `CustomItemList` + loader registration (lang, icons); all four **station-only** like the launcher (ship builds rejected with `voidcraft_launcher_station_only`; digitized by the Voidbase Assembler).
- The in-world structures stay dormant no-ops (structure check only); the three shell components are **infrastructure builders** — their build behaviour lives in the USS machine tick (rev 5, section 2.2), like the launcher's.
- **Tests:** catalog invariants (meta==ordinal, placeable/multiblock, station-only classification); the four shape invariants (footprints, cell counts, anchors); the validation rules pinned.
- **Gate:** suite green + playtest (creative tab: the 8 blocks + icons; the four structures build + audit + stay dormant; all four digitize into a base blueprint; a ship build containing any of them is rejected).

### Phase 3 — Expiry pipeline
- `starBurnsOut()` → `starExpires()` (landed): yield + Universium outputs (output hatch/buses, `addFluidOutputs`); outcome resolution incl. infrastructure reads (rev 5: stabilizer active = any FULLY BUILT stabilizer shell on a scanned ripple; lens present = the star's lens shell FULLY BUILT — both read from the `USSInfrastructure` ledger BEFORE `discardAllShips()`; matrix N = 0 until the STABILIZE legs land in Phase 5); **the fresh-system model swap** (ignite / toCold) on BOTH evolution and terminal paths, after `discardAllShips()`; auto-ignite + controller UPGRADE (D1 — the controller item's meta becomes the outcome's class; only terminal death consumes it); terminal path unchanged (controller consumed); log line. (Base re-anchors are moot: the evolution wipes the bases with the fleet — D5 retired.)
- **Tests:** expiry math integration (pinned example; 50×; Fibonacci via weights; per-star yield table); the model reads (primary fraction unsiphoned=1.0 / depleted=0; size factor 1.0→1.5 at the cap); the blueprint component-count record.
- **Gate:** suite green + playtest (feed Molten Spacetime to force the expiry; outputs + evolved system with fresh planets/ripples; controller upgraded in-slot; new system starts with no infrastructure).

### Phase 3.5 — Infrastructure-builder pass (rev 5, landed)
The rev-4 "read the base's blueprint" design for the three shells is superseded: the shells are BUILT (section 2.2). Landed:
- 3 new component items (`ItemVoidcraftInfraComponent` — `VC_ITEM_INFRA_INJECTOR/STABILIZER/LENS` icons), special cargo keys `injector_component` / `stabilizer_component` / `lens_component` + `item.` loadout keys; gateway pulls the components for builder-carrying blueprints (one pull block, like the satellites).
- `USSInfraBuild` (types/targets/keys/capacity/exclusivity/built) + `USSInfraShell` (triangle-shell geometry — server capacity and client mesh share it); `MTEUnstableSolarSystem.tickInfrastructureBuilder` (per-base per-type countdown, one unit / `INFRA_BUILD_INTERVAL` = 10 ticks, hold draw, ledger update, render sync every unit); exclusivity guards in BOTH directions (builders skip an occupied target; the Satellite Rail Launchers stop while an injector/lens shell exists on the star).
- The decay pass is gated to the Dyson key (the new shells never decay).
- Render: the shared triangle-shell pass (`EOHRenderingUtils.renderUSSInfraShell` — edge + tints per type; the Dyson delegates to it), star shells on `TileEntityEyeOfHarmony.setInfraShell` (star TE, `EOHTileEntitySR`), ripple shells on `TileEntityVoidcraftShip.setRippleInfraShells` (fleet TE, `RenderVoidcraftShip` at the ripple positions — revealed ripples only).
- **Tests (permanent):** `USSInfraShellTest` (geometry pins/monotonicity), `USSInfraBuildTest` (keys, target matrix, exclusivity, built checks, builder mapping), `USSInfraTest` (component key/loadout contract).
- **Gate:** suite green (1031 tests 0F/0E/0S) + playtest (build a star-anchored base with an injector or lens builder + a ripple-anchored base with a stabilizer builder; feed the component items; the shells fill fractionally around the star / at the ripple; exclusivity blocks a second shell on the same target AND the Dyson launcher; a fully built shell is picked up by the expiry reads — see the Phase 3 playtest, which now runs after this).

### Phase 4 — Injector behaviour (LANDED, gate 1035/0/0/0)
The injector's SHELL is built in Phase 3.5 (rev 5). Landed:
- **Injector tick** (`MTEUnstableSolarSystem.tickInjector`, called from `onPostTick` while ignited): active = the star's Injector shell FULLY BUILT; on the `INJECTOR_STEP_INTERVAL_TICKS` = 1000-tick pace one size step leaves the injector buffer — cost `cargoUnitsForSizeDelta(size, INJECTOR_SIZE_STEP)` = ceil(`INJECTOR_COST_PER_SIZE_UNIT` × size² × step), size +`INJECTOR_SIZE_STEP` = 0.01, capped at `sizeCap` = 1.5× the ORIGINAL sampled size; a step the buffer cannot pay is skipped (no partial consumption), a step that would cross the cap is not started; step logs to the console + `syncStarRenderBlock`.
- **Star-shell geometry pinned to the ignition size** (`starShellRenderSize()`): the Injector/Lens shell capacity + render geometry + expiry lens read + builder pace + debug items all use the ORIGINAL star's render size — the star grows up to 1.5× behind the built shell, and a live-size capacity would read the finished shell as incomplete. The Dyson Swarm keeps the LIVE size (the swarm tracks the star). The star size now also rides `syncStarRenderBlock` (`te.setStarSize`) so the star TE grows client-side.
- **`TARGET_STAR` SEND/TAKE**: a branch in `cargoTransferStart` resolves the star to the Stellar Injector's cargo buffer (the same `USSCargoTransfer` hold→hold leg — SEND: ship hold → buffer, TAKE: buffer → ship hold; energy stall model + filter/limit as ship-to-ship). Guards: ignited star, FULLY BUILT star Injector shell (else "not fully built - skipping"), ship settled (not in transit), ship's location IS the star (`USSLocation.Kind.STAR`). Persists through NBT (`vc_tr_star` flag, empty target uuid) + a chunk reload resumes the leg; the fleet beam ends at the star center (`TAG_TRANSFER_STAR`, `USSFleetOrbit.STAR_CENTER_Y`).
- **Display**: `getInfoData` "Stellar Injector" section (star size current/cap, buffer used/capacity, shell count/capacity + ACTIVE/under-construction) — `tt.voidcraft_uss.injector.*` lang keys.
- **Tests (permanent):** `CargoHold.removeUnits` pin (items-first insertion order, then full 100 mB fluid units, clamped; sub-unit remainders stay; 0/negative no-op) — the injector's drain primitive. The cost formula + cap were already pinned (`USSStellarEvolutionTest`). MTE tick/resolution glue is server-only (not bare-JVM testable).
- **Gate:** suite green (1035 tests 0F/0E/0S) + playtest owed (mining ship SENDs to the star; size grows to cap; the max-size explosion path is reachable).

### Phase 5 — STABILIZE command
- `USSWorkKind.STABILIZE` + `USSCommandStabilize` + registry/MUI editor/presets + `stabilizeTick` handler; eligibility (matrix in blueprint, ripple-anchored base, scanned active ripple, bound stabilizer → else SKIP); duration from node argument; per-tick energy (stall), 2000-tick sensor consumption (UXV-over-UMV), weight tracking; expiry weight readout (already in Phase 3 — wired to live legs); Gateway sensor-cargo pull for matrix bases.
- **Tests:** SKIP conditions pinned; consumption pace (1 sensor/2000 ticks); tier/weight rules (UMV=1, UXV=2, last-consumed tier); Fibonacci integration.
- **Gate:** suite green + playtest (base carrying a matrix: STABILIZE window timed to expiry; Universium multiplied; base wiped if acceleration runs over it).

### Phase 6 — Polish, debug tooling, handoff
- Debug effects: scan N random ripples, set star to max size, set lifespan (s), force expiry. Landed (rev 5): three infrastructure-shell debug items (`ItemVoidcraftDebugInfraShell`, the Dyson debug-item pattern — item → effect registry, 10% of the shell's capacity per click, no resource cost): the Injector / Lens ones build the star's shell, the Stabilizer one reveals (scans) the first ripple whose shell is not fully built and builds on it (a full shell advances to the next ripple — `USSInfraBuild.firstIncompleteStabilizerRipple`).
- Log/tooltip polish (component blocks: station-only semantics in the tooltip; matrix: command requirements).
- Final playtest pass; Mnemon record of the mechanic decisions; git handoff commands (user commits).

## 5. Decisions (confirmed / chosen defaults — flag if wrong)

| # | Decision | Status / default |
|---|---|---|
| D1 | Evolution auto-ignites; controller slot upgraded in place to the outcome-type controller item (consumed only at terminal death) | **confirmed** |
| D2 | Supernova "fed enough matter" = star size boosted above original by the injector | **confirmed** |
| D3 | The three shell infrastructures are BUILT by their station-only builder components (Satellite-Rail-Launcher pattern: component item consumed by the standing base into the `USSInfrastructure` ledger, one unit / interval, capacity = shell triangle count); the Matrix is the only dormant component; shells = USS-internal state + system-view render — no functional world MTE; one structure per target (star: Dyson XOR Injector XOR Lens; ripple: Stabilizer XOR Injector); functional = fully built (rev 5, user correction) | **revised per user** |
| D4 | Matrix activation = new STABILIZE ship-program command (duration node argument); the user synchronizes the base's window to the natural expiry (acceleration wipes the base) | **revised per user** |
| D5 | Ripple-anchored base after evolution: re-resolve anchor index in the new ripple field; if not a ripple there, re-anchor to the star | default |
| D6 | Lens footprint 7×7×12 | **confirmed** |
| D7 | Explosion chance ramp: `F > 0.75 → 100%`, else linear `F/0.75` | default |
| D8 | Orbit speedup proportional to √consumed: per-tick advance `1 + sqrt(consumedMB)/ORBIT_SPEEDUP_DIVISOR` (placeholder divisor 200) | **confirmed (proportional)** |
| D9 | Injector vs star fluid reserve: reserve stays fixed at first-siphon initialization; size boost is a separate axis | default |
| D10 | Stacking: stabilizer rate applies once (any active stabilizer); matrix weights sum across bases | default |
| D11 | Energy: injector none (world structure — cargo + time); matrix placeholder ~5000 EU/t while the leg runs (balance pass later) | default |
| D12 | Injector buffer (and all infrastructure internal state) at evolution | **wiped** — the whole system resets: internal state cleared, buffer cargo lost (per user; rev 4: no world structures to destroy) |
| D13 | Stabilizer target = the base's ANCHOR ripple (the builder builds on the anchor; the shell is active at expiry iff fully built) — supersedes the rev-4 "nearest scanned ripple binding" (moot: evolution wipes the system) | **revised per user (rev 5)** |
| D14 | Matrix weight at expiry = tier of the last sensor consumed by each active leg (UMV = 1, UXV = 2); a leg with no sensor available cannot start (SKIP) | default |
| D15 | Acceleration wipe = the existing full `discardAllShips()` (all Voidships incl. bases + sites); infrastructure (USS internal state) survives | **confirmed per user** |
| D16 | Injector cargo delivery = new SEND/TAKE target `TARGET_STAR` resolving to the injector buffer; the star is a first-class shared location so `MOVE target=star` → `SEND target=star` delivers; `TAKE target=star` retrieves | default |

## 6. Test strategy (standing directive)
- **Permanent:** `USSStellarEvolutionTest` (outcome table + chance boundaries — the evolution decision itself); expiry math tests (pinned numeric example + ratio + Fibonacci — contract values); acceleration reduction + orbit advance (the √/min-1 + proportional rules); injector cap/cost (the 1.5× + size-cost rule); the two-wipe lifecycle (acceleration preserves infrastructure; evolution/terminal death wipes the internal state + buffer — the lifecycle invariant); NBT roundtrip for the new `VoidcraftUSS` fields (serialized format); component catalog + station-only validation (invariants); STABILIZE SKIP conditions + consumption pace (the command's error contract + pacing rule); **`USSInfraShellTest`** (the shell-geometry formula the server capacity and the client mesh share — pinned values + monotonicity); **`USSInfraBuildTest`** (typed keys/targets, one-structure-per-target exclusivity, functional=fully-built, builder→component mapping — the builder + expiry-read contract); **`USSInfraTest`** (the component item loadout→cargo key routing, serialized keys).
- **Scaffolding (delete on landing):** tests that only validate a phase's wiring against its own implementation — playtest + compile cover those.

## 7. File map (expected)
- New: `uss/USSStellarEvolution.java` (landed), `uss/USSCommandStabilize.java` (+ `USSWorkKind` entry, `USSCommandRegistry`/`USSProgramDefaults`/MUI editor entries, Phase 5), `multiblock/{StellarInjectorStructure,ContinuumStabilizerStructure,StellarLensStructure,StabilizationMatrixStructure,MTEVoidcraftStellarInjector,MTEVoidcraftContinuumStabilizer,MTEVoidcraftStellarLens,MTEVoidcraftStabilizationMatrix}.java` (all landed, Phase 2 — dormant, launcher pattern), `uss/USSInfraShell.java` (shell geometry — server + client share it, rev 5), `uss/USSInfraBuild.java` (types/targets/keys/capacity/exclusivity/built, rev 5), `item/ItemVoidcraftInfraComponent.java` (the three builder component items, rev 5)
- Modified: `uss/{VoidcraftUSS,USSConstants,MTEUnstableSolarSystem (builder pass + launcher exclusivity + render sync + decay gate),USSInfrastructure (keys),USSInfra (component keys + mapping)}.java`, `ship/VoidcraftComponent.java`, `ship/VoidcraftBlueprint.java` (station-only validation), `machine/{MTEVoidcraftGateway (component + sensor pull),MTEVoidbaseAssembler}.java`, `loader/VoidcraftLoader.java`, `gregtech/api/enums/MetaTileEntityIDs.java`, CustomItemList, `thing/block/TileEntityEyeOfHarmony.java` (star shell state, rev 5), `render/TileEntityVoidcraftShip.java` (ripple shell state, rev 5), `render/EOH/EOHRenderingUtils.java` (parameterized triangle-shell pass, rev 5), `render/EOH/EOHTileEntitySR.java` (star shell draw, rev 5), `render/RenderVoidcraftShip.java` (ripple shell draw, rev 5), MUI sections of `MTEUnstableSolarSystem`
- Tests: `uss/USSStellarEvolutionTest.java` (landed), expiry-math tests (landed), `uss/VoidcraftUSSTest.java` (extended, landed), `ship/VoidcraftComponentTest.java` (extended, landed), `ship/VoidcraftBlueprintTest.java` (extended, landed), `multiblock/StarInfrastructureStructureTest.java` (landed, Phase 2), `uss/USSInfraShellTest.java` + `uss/USSInfraBuildTest.java` + `uss/USSInfraTest.java` (rev 5, landed), STABILIZE command tests (new, Phase 5)