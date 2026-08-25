# Voidcraft Implementation Plan (EoH Rework — Voidcraft System)

Companion to [EyeOfHarmony_Rework_Proposal.md](./EyeOfHarmony_Rework_Proposal.md).
Scope: implementing the **Voidcraft system** — digitized Voidcraft ships, Voidcraft
Assembler, Unstable Solar System (USS) main multiblock, Dimensional Gateway,
Voidcraft Storage Bay, Dimensional Extractor, and the USS simulation they run in.

> **Hard constraint (deprecation period):** the existing Eye of Harmony implementation
> is left **completely untouched** — no edits to its classes, recipes, or loaders.
> The new implementation ships as a **separate, parallel machine** that can be built
> and used **alongside** the existing EoH. The old EoH keeps working exactly as it
> does today (planet-block recipes, success-chance/pity RNG, EU output) until it is
> deliberately deprecated and removed in a later, separate change.

> **Implementation order (user decision, 2026-08):** build the **Voidcraft side
> first** — components, stat math, `ItemVoidcraft`, Voidcraft Assembler — because
> none of it depends on the USS. The new USS main multiblock (`MTEUnstableSolarSystem`)
> comes **after** (Phase 2), and the first full ship loop lands in Phase 3.

This document is self-contained for a fresh implementation session: §9 collects every
codebase fact verified so far (files, line numbers, patterns, build commands) so the
implementing session does not have to rediscover them.

---

## 1. Current state (reference only — none of this is modified)

### 1.1 Existing Eye of Harmony (reference implementation; stays byte-for-byte as-is)

| Concern | Current implementation |
| --- | --- |
| Controller MTE | `src/main/java/tectech/thing/metaTileEntity/multi/MTEEyeOfHarmony.java` (1744 lines), extends `TTMultiblockBase` |
| Structure | 33×33×33 spherical multiblock, `IStructureDefinition` in the same file (lines ~147–776); elements `A` (spacetime compression, 9 tiers), `S` (stabilisation, 9 tiers), `E` (time dilation, 9 tiers), `C`/`D` (BA0 casings), `H` (hatch adder on `InfiniteSpacetimeEnergyBoundaryCasing`, line ~754), `~` (controller, front-face center) |
| Trigger | Controller slot holds a "planet block" (`BlockDimensionDisplay`, gtneioreplugin); `EyeOfHarmonyRecipeStorage.recipeLookUp(controllerStack)` picks the recipe (line ~1078) |
| RNG (stays in the old machine) | Success chance, pity, overflow-probability, astral-array parallels — `recipeChanceCalculator()`, `pityChance`, `calculateInputFluidExcessValues()` (lines ~786–846, 1099–1260) |
| Inputs/outputs | Hydrogen + Helium in (2 input hatches, 1 input bus); item + fluid + **EU out** (`addEUToGlobalEnergyMap`, line ~1362); energy hatches explicitly rejected (`checkMachine`, line ~883) |
| Tier source | Spacetime Compression Field Casing meta 0–8 → `spacetimeCompressionFieldMetadata` (lines ~714–731, 1152–1161) |
| Rendering | `TileEntityEyeOfHarmony` + `tectech/rendering/EOH/EOHTileEntitySR.java` draw orbiting `OrbitingObject`s — **reusable as-is** by the new machine (public `setTier(long)`, `setStarSize(double)`, `getOrbitingObjects()`) |

Files that must **not** appear in the change diff for this work: `MTEEyeOfHarmony.java`,
`EyeOfHarmonyRecipe.java`, `EyeOfHarmonyRecipeStorage.java`, `EyeOfHarmonyFrontend.java`,
`TileEntityEyeOfHarmony.java`, `EOHTileEntitySR.java`, `SpacetimeCompressionFieldCasing.java`,
and the EoH recipe loaders (`ResearchStationAssemblyLine.java` EoH entry, `BECRecipes.addEyeOfHarmonyRecipes()`).

### 1.2 Assets that already exist and get reused (read-only reuse)

- **Materials/fluids** (`gregtech/api/enums/Materials.java`, lines ~1074–1077):
  `RawStarMatter` (Stellar Plasma), `WhiteDwarfMatter`, `BlackDwarfMatter`, `Universium`.
  These are the Starlifter / supernova outputs — no new materials needed for v1.
- **Spacetime compression tiers 0–8**: `tectech/thing/casing/SpacetimeCompressionFieldCasing.java`
  + `TTCasingsContainer.SpacetimeCompressionFieldGenerators` (line 18). The proposal's
  "gateway maximum size is determined by the spacetime compression block tiers" maps
  directly onto these 9 metas; the new machine matches the same casing block (no changes to it).
- **Structure channels** `EOH_COMPRESSION / EOH_STABILISATION / EOH_DILATION` in
  `gregtech/common/misc/GTStructureChannels.java` (lines 42–44) — reference for any new channel.
- **Multiblock framework**: `tectech/thing/metaTileEntity/multi/base/TTMultiblockBase.java`
  (2506 lines): `getStructure_EM()`, `checkMachine()`, `checkProcessing_EM()`,
  `outputAfterRecipe_EM()`, `onPreTick()`, `saveNBTData()/loadNBTData()`, `Parameters`,
  `mInputBusses/mInputHatches/mOutputBusses/mOutputHatches`, `useLongPower`, `survivalBuildPiece()`.
  **Default GUI**: `getGuiClass()` already returns `new TTMultiblockBaseGui<>(this)`
  (line ~2112) — new machines get a working multiblock GUI for free.
- **Module-linking precedent**: space elevator modules —
  `gtnhintergalactic/tile/multi/elevatormodules/TileEntityModuleBase.java` (`isConnected` flag,
  links to `TileEntitySpaceElevator`) and `TileEntityModuleManager.java`.
- **Custom item precedent**: `tectech/thing/item/ItemAstralArrayFabricator.java` — plain
  `Item` subclass + `GameRegistry.registerItem` + `CustomItemList.X.set(INSTANCE)`.
  Template for `ItemVoidcraft`.
- **Vein data math** (reference for the USS planet generator): `tectech/recipe/EyeOfHarmonyRecipe.java`
  (`processHelper*`, `validDustGenerator`) already maps planet → ore-vein dust outputs via
  gtneioreplugin dimension data, plus per-tier star-matter / plasma /
  White-Black Dwarf / Universium outputs (`SPECIAL_FLUIDS`, lines 46–56). The USS planet
  generator **re-implements** this math in the new package (copy the formulas; do not call
  into or modify the old classes).
- **Registration path**: `tectech/thing/CustomItemList.java` (enum holder),
  `tectech/loader/thing/MachineLoader.java` (line 2001 = the EoH registration line, the
  pattern to copy) + `ThingsLoader.java` (blocks/items), both `Runnable`s invoked from
  `tectech/loader/MainLoader.java` (lines 34 and 44),
  `gregtech/api/enums/MetaTileEntityIDs.java` (EoH = 15410; **highest ID in use: 32765**).

---

## 2. Target architecture

```
   LEGACY (untouched)                    NEW (this work)
   +----------------------+              +--------------------------------------------------+
   | Existing Eye of      |              |  MTEUnstableSolarSystem (NEW controller MTE)     |
   | Harmony (as today)   |   coexist    |  - mirrors the EoH structure shape (own class)   |
   | recipes + RNG + EU   |<-----------> |  - H2 + He in -> "ignite" USS (tiered, timed)    |
   +----------------------+   same world |  - owns USS state (NBT) + tick engine            |
                                        |  - 5 face anchors for modules                     |
                                        +--+----------+-----------+-----------+------------+
                                           |          |           |           |
                                     [Gateway]   [Storage Bay] [Extractor] [2 spare slots]
                                     (1x1/3x3/   (N ship      (item/fluid/ energy I/O
                                      5x5)       slots, I/O)  energy out)
                                           |          |
                                           +----+-----+
                                                v
                                        +---------------------+
                                        |  USS (data model)   |
                                        |  star + planets +   |
                                        |  ripples + ships +  |
                                        |  structures + pools |
                                        +---------------------+
                                                ^
                                                | launch / return
                                        +---------------------+
                                        | Digitized Voidcraft |  <- built & "digitized" by
                                        | (ItemVoidcraft NBT) |     Voidcraft Assembler
                                        +---------------------+
```

Key decisions:

1. **Coexistence via a separate controller.** The new main multiblock is a **new class**
   `MTEUnstableSolarSystem` (new ID ≥ 32766, new controller item) that **mirrors the
   existing EoH structure** — same sphere shape, same casings and field-generator tiers,
   same face geometry — with its own structure definition (own tier setters) and its own
   controller element. One physical shell can therefore serve either machine depending on
   which controller block is placed; both machines can also run simultaneously in one world.
   Nothing in `MTEEyeOfHarmony` changes.
2. **The USS is a data model, not world blocks.** It lives as NBT on the
   **new controller MTE** and ticks inside that MTE. Ships are entries in `ships[]`, not
   tile entities. This avoids per-ship block placement, chunk-loading and
   persistence problems, and matches the "miniaturized, temporary" fiction.
3. **Voidcraft are item representations** (`ItemVoidcraft`, `maxStackSize = 1`,
   NBT = blueprint + computed stats), produced by the Assembler, moved only through
   the dedicated voidcraft network (Assembler ↔ Bay ↔ Gateway), consumed back by the
   Gateway on launch.
4. **Modules are standalone multiblocks built against a face of the new main
   multiblock**, linking by fixed face offset + MTE lookup (same pattern as
   space-elevator modules). The new MTE tracks linked modules and exposes a USS API.
5. **Determinism**: a per-USS `seed` drives star type, planet set, ripple positions.
   No success-chance RNG in the loop (the old machine keeps its own RNG untouched).
6. **Implementation order**: Voidcraft first (Phases 0–1: components, stat math,
   `ItemVoidcraft`, Assembler — all standalone, no USS dependency, fully testable in
   isolation), then the new USS main multiblock (Phase 2), then the ship loop
   (Phase 3 onward).

### 2.1 USS data model (NBT on the new controller MTE)

`VoidcraftUSS` (new class), serialized under a root NBT tag on `MTEUnstableSolarSystem`:

```
{
  seed: long,               // per ignition
  tier: int,                // 0..8, from Spacetime Compression meta
  phase: int,               // 0 forming, 1 stable, 2 collapsing, 3 supernova, 4 collapsed
  ageTicks: long, lifespanTicks: long,
  star: {
    type: int,              // 0 main-sequence dwarf, 1 white dwarf, 2 supermassive
    energyPool: long,       // Dyson-extractable EU
    whiteDwarfMatter: long, blackDwarfMatter: long,   // Starlifter pools
    stellarPlasma: long,    // RawStarMatter (Stellar Plasma Mixture) pool
    universiumTrace: long   // mid-tier trace leak (see proposal)
  },
  planets: [ { id, orbitRadius, veins: {item->amount}, mined, depleted } ],
  ripples: [ { posVec3, discovered, scanned, bonus } ],
  structures: [ { type: hyperlane|rail|dyson|igniter, progress, active } ],
  ships: [ {
    uuid, role, state,      // state: docked | out | working | returning
    posVec3, progressTicks, cargo: {item->amount, fluid->amount}, energyDrawn
  } ],
  cargoPool: { item->amount, fluid->amount },  // unclaimed output for Extractor
  universiumPool: long, rippleBonus: double
}
```

- **Star type** at ignition: derived from `tier` + `seed` (higher tiers more likely
  supermassive; mid tiers → white dwarf). Only `supermassive` supernovas at
  end-of-life; `white dwarf` can be forced by a built **Star Igniter**.
- **Universium**: no direct production mid-USS except a small `universiumTrace`
  leak at mid tiers; the supernova yields `base(tier) * (1 + rippleBonus)`;
  ripple scans (Explorers) add to `rippleBonus`.
- **Planet veins**: re-implement the existing vein math from `EyeOfHarmonyRecipe`
  (`processHelper*`) into the new package (copy, do not call), seeded per planet;
  quantities scaled by `tier`.

### 2.2 Voidcraft item & stats

`ItemVoidcraft` (new, template = `ItemAstralArrayFabricator`):

- `maxStackSize = 1`; custom `addInformation` tooltip listing role, stats, dims;
  optional custom renderer (Phase 7) for a hologram preview of the block grid.
- NBT: `uuid`, `name`, `dims[w,h,d]`, `grid` (compressed byte array of component
  ids), `stats` (below), `role`, `createdAt`, `formatVersion`.

Computed stats at assembly time (all derived from the component registry):

```
mass, thrust, speed = clamp(thrust/mass), cargoSlots, miningPower,
scanPower, constructionPower, energyBuffer, energyDraw, integrity
```

- **Role** is derived from which capability modules are present
  (`MINER | CONSTRUCTOR | STARLIFTER | EXPLORER`); a **hybrid penalty**
  (efficiency multiplier < 1 per extra active role) enforces "dedicated ships are
  more efficient" from the proposal.
- **Integrity** determines whether a ship survives a collapse if it is docked
  (recoverable) vs. lost mid-space (expendable) — the proposal's central
  recoverable-vs-expendable trade-off.

### 2.3 Component registry

New voidcraft component blocks (one casing block, multiple metas — the
`SpacetimeCompressionFieldCasing` pattern):

| Component | Contributes |
| --- | --- |
| Voidcraft Controller (required ×1) | base mass, enables digitization |
| Voidcraft Engine | thrust, energyDraw |
| Utility / Integrity Block | mass, integrity |
| Cargo Bay | cargoSlots |
| Mining Drone Command Centre | miningPower |
| Star Drill / Starlift Array | starlifter capability (accesses star pools) |
| Spacetime Scanner | scanPower (ripple detection) |
| Construction Arm / Fabricator | constructionPower (builds USS structures) |
| Reactor / Energy Block | energyBuffer |

Registry class `VoidcraftComponentRegistry` maps component id → stat vector +
tier (tech level). Tiers gate which components can be digitized (higher tech =
more options, per the proposal).

### 2.4 Gateway / Bay / Extractor linkage

Each module is a `TTMultiblockBase` that, in `checkMachine`, verifies an
`MTEUnstableSolarSystem` at a fixed offset on one of the 5 module faces, stores its
position, and uses the USS via a public API on the **new** MTE:

```java
// on MTEUnstableSolarSystem (new class — MTEEyeOfHarmony is never touched)
VoidcraftUSS getUSS();
boolean isGatewayLinked(MTEVoidcraftGateway g); // face + tier compatibility
void launchShip(ItemVoidcraft); void returnShip(UUID);
boolean canExtract(ItemStack/FluidStack/EU);
```

- **Gateway tier** = min(built size, `scTier`-derived max). Mapping (balanceable):
  SC tier 0–2 → 1×1 (ships ≤ 1×1×3), 3–5 → 3×3 (≤ 3×3×6), 6–8 → 5×5 (≤ 5×5×10).
  Bigger gateway → faster dock/undock ticks for smaller ships (per proposal).
- **Bay** holds N (e.g. 9) `ItemVoidcraft`, one per slot; input bus receives,
  output bus ejects; auto-launch queue when the USS ignites.
- **Extractor** gates output type on in-USS infrastructure (constructor structures):
  item extraction needs a rail accelerator station, energy extraction needs a Dyson
  Sphere; each tier scales the rate.
- Rendering: the new MTE places the **existing** `TTCasingsContainer.eyeOfHarmonyRenderBlock`
  and drives its `OrbitingObject` list (`setTier`, `setStarSize`,
  `getOrbitingObjects()`) — no changes to the render block or the EoH renderer.

---

## 3. Repository layout (concrete new/changed files)

New package `tectech.voidcraft.*` (sibling to existing `tectech.thing.*`):

```
tectech/voidcraft/
  core/
    VoidcraftUSS.java            // USS data model + NBT (de)serialization
    USSStar.java, USSPlanet.java, USSRipple.java, USSStructure.java
    USSimulationEngine.java      // tick driver: travel/work/return/collapse/supernova
    USSConstants.java            // tier tables, travel/work formulas, star-type roll
                                 // (vein math re-implemented from EyeOfHarmonyRecipe)
  ship/
    VoidcraftBlueprint.java      // parse grid -> stats (pure, unit-testable)
    VoidcraftStats.java
    VoidcraftRole.java           // enum + hybrid penalty
    VoidcraftComponentRegistry.java
  item/
    ItemVoidcraft.java           // custom Item (template: ItemAstralArrayFabricator)
  machine/
    MTEUnstableSolarSystem.java  // NEW main multiblock (mirrors EoH structure;
                                 // own structure definition + tier setters; owns USS;
                                 // module-link registry + USS API; 5 face anchors)
    MTEVoidcraftAssembler.java   // standalone multi: scan + digitize
    MTEVoidcraftGateway.java     // face module (1x1/3x3/5x5)
    MTEVoidcraftStorageBay.java  // face module (N ship slots)
    MTEVoidcraftExtractor.java   // face module (item/fluid/EU out)
  loader/
    VoidcraftLoader.java         // entry point: runs all below (called from MainLoader)
    VoidcraftThingsLoader.java   // component block, ItemVoidcraft, casing wiring
    VoidcraftMachineLoader.java  // 5 new MTEs: CustomItemList + IDs + registration
    VoidcraftRecipeLoader.java   // new machine + component crafting recipes
  util/
    VoidcraftNBT.java            // shared NBT tag helpers
```

Changed existing files — **additive only, and none of them are EoH files**:

- `tectech/thing/CustomItemList.java` — add `Voidcraft`, `UnstableSolarSystem`,
  `VoidcraftAssembler`, `VoidcraftGateway`, `VoidcraftStorageBay`, `VoidcraftExtractor`,
  component items.
- `tectech/loader/MainLoader.java` — one additive line: `new VoidcraftLoader().run();`
  (the existing `ThingsLoader`/`MachineLoader` calls stay as they are).
- `gregtech/api/enums/MetaTileEntityIDs.java` — allocate new IDs **≥ 32766**
  (highest in use is 32765); reserve a contiguous block (e.g. 32766–32772) for the
  5 new machines.
- `gregtech/common/misc/GTStructureChannels.java` — optional new channel
  `VOIDCRAFT_COMPONENT` (only if components are tiered).
- `tectech/loader/ConfigHandler.java` — new `voidcraft` config section
  (enable toggle, tier tables, debug).
- Lang files: `src/main/resources/assets/gregtech/lang/en_US/` (+ `ru_RU`) —
  tooltips, structure info, module names.
- Optional Phase 7: `tectech/rendering/EOH/EOHTileEntitySR.java` **only if** the
  existing orbit renderer proves insufficient for USS planets/ships (target is to
  reuse it unchanged; extend the render block's object list from the new MTE instead).

---

## 4. Core data flow

1. **Build** — player places voidcraft component blocks (controller + engines +
   role modules) adjacent to the Assembler within a 5×5×10 bounding volume.
2. **Digitize** — Assembler action scans the connected component, validates
   (known blocks, controller present, within max dims/count, tier-allowed),
   computes stats, emits one `ItemVoidcraft`, and clears the blocks.
3. **Store** — ship item flows to the Storage Bay (bus) and persists across
   collapses.
4. **Ignite** — `MTEUnstableSolarSystem` (with H2 + He + power) ignites a tiered
   USS from its seed. The legacy EoH can run its own recipes next door, unaffected.
5. **Launch** — Bay → Gateway (tier-checked) → ship instance appears at the USS
   edge (`state = out`).
6. **Travel + work** — ship moves edge → target (planet/star/ripple) at
   `speed`; performs its role (mine vein / tap star pool / scan ripple / build
   structure), accumulating cargo or progress.
7. **Return** — ship returns to the gateway (or docks if a rail station lets it
   dump in place), delivers cargo to `cargoPool`, and (if recoverable) re-emits
   its `ItemVoidcraft` to the Bay; (if expendable) is consumed.
8. **Extract** — Extractor moves `cargoPool` / energy out through hatches,
   gated by the in-USS structures the Constructors built.
9. **Collapse** — at `lifespanTicks` (or forced by Star Igniter on a white dwarf):
   phase → `collapsing`/`supernova`; docked recoverable ships return, mid-space
   ships are lost; supernova emits Universium (`base * (1+rippleBonus)`) into
   `cargoPool`/outputs; USS resets to `forming` for the next ignition.

---

## 5. Phased implementation plan

Order per user decision: **Voidcraft (components → item → Assembler) first**, then
the new USS main multiblock, then the full ship loop. Each phase is independently
shippable and testable. **No phase may touch the existing EoH files** (see §1.1
do-not-modify list).

### Phase 0 — Voidcraft components + stat math  (S)
- Component casing block (multi-meta, `BlockCasingsAbstract` pattern of
  `SpacetimeCompressionFieldCasing`) + `VoidcraftComponentRegistry`
  (component id → stat vector + tier gate).
- `VoidcraftBlueprint` + `VoidcraftStats` + `VoidcraftRole` (pure classes,
  unit-testable; hybrid penalty logic).
- Unit tests under `src/test/java/tectech/voidcraft/` (JUnit, JUnit platform —
  existing tectech tests there: `TTUtilityTest.java`, `BECFactoryNetworkTest.java`).
- **Exit:** given a block grid, produce correct stats/role; unit tests pass
  (`gradlew test`); components craftable and visible in creative tab.

### Phase 1 — ItemVoidcraft + Voidcraft Assembler  (M)
- `ItemVoidcraft` (custom Item per the `ItemAstralArrayFabricator` template:
  `maxStackSize=1`, NBT grid+stats, `addInformation` tooltip, icon registration).
- `MTEVoidcraftAssembler`: `TTMultiblockBase` multiblock (its own small structure),
  default `TTMultiblockBaseGui` works out of the box; action: scan region,
  validate, digitize, clear blocks, emit item; EU + optional per-block cost;
  `ISurvivalConstructable` if desired.
- Register: `CustomItemList`, `VoidcraftMachineLoader`, `MetaTileEntityIDs`
  (≥ 32766), wire `VoidcraftLoader` into `MainLoader`.
- **Exit:** build a 1×1×3 ship out of component blocks, digitize it, inspect
  tooltip/stats; blocks consumed; item non-stackable; assembler survives
  chunk reload (NBT round-trip).

### Phase 2 — New main multiblock + USS foundation  (M) — ✅ DONE (vertical slice)

> **Status:** implemented as the agreed vertical slice ("keep it simple, iterate
> later"). Delivered: `MTEUnstableSolarSystem` (ID 32057, verbatim EoH structure
> mirror, same hatch rules, own state machine: COLD → controller insert → IGNITED
> → lifespan countdown → burnout, render block create/destroy, infodata, NBT
> round-trip), `VoidcraftUSS` model + `USSState`/`USSStarType`/`USSConstants`
> (tier tables)/`USSVeinMath`, `ItemUSSController` (+ placeholder texture),
> `voidcraft.enabled` master switch in `config/voidcraft.cfg`, en_US + ru_RU lang
> keys, 15 new unit tests (NBT round-trip incl. corrupt reads, star tables, vein
> math) — full `tectech.voidcraft.*` suite green, §1.1 zero-diff verified.
> Deferred to later phases (out of slice): module-face anchors + USS API stubs,
> star draw/EU model, any gameplay beyond the ignition loop.

- `MTEUnstableSolarSystem`: new class mirroring the EoH structure (copy the shape
  strings + element mapping from `MTEEyeOfHarmony` lines ~147–776, own tier
  setters, own controller item), hatch rules, render-block placement
  (`TTCasingsContainer.eyeOfHarmonyRenderBlock` — place + drive, don't modify).
- `VoidcraftUSS` + sub-models + NBT (de)serialization; unit-test round-trip.
- `USSConstants` tier tables (star-type roll, lifespan, vein scaling) with vein
  math copied from `EyeOfHarmonyRecipe` into the new package.
- Identify the 5 module-face anchors from the mirrored structure; USS API stubs.
- `voidcraft.enabled` config.
- **Exit:** build the shell with the new controller → USS ignites, persists
  across reload, visible via infodata; **the legacy EoH built in the same world
  still runs its recipes exactly as before**; git diff shows zero changes to the
  §1.1 do-not-modify list.

### Phase 3 — Storage Bay + Gateway + first ship loop  (L)  ← first playable loop — ✅ DONE (vertical slice)

> **Status:** implemented as the agreed vertical slice ("keep it simple, iterate
> later"). Delivered: `MTEVoidcraftGateway` (ID 32054, 3×3×3 BA0-10; right-click
> with a Voidcraft → launch; one ship in flight per USS; tier-check vs the
> ignited USS; recoverable ships re-emit into the gateway slot, expendable ones
> are consumed), `MTEVoidcraftStorageBay` (ID 32055, 5×5×3, 16-slot
> `VoidcraftCargoPool` with bus I/O and NBT persistence across collapses),
> `VoidcraftActiveShip` flight FSM (OUTBOUND → MINING → RETURNING, deterministic
> leg ticks), `USSShipCargo` deterministic cargo, `MTEUnstableSolarSystem`
> launch/tick/complete/discard/rebuild, and the ship model rendered with the
> Amazing Trophies VBO technique (actual ship blocks, `tectech.voidcraft.render.*`).
> **Cargo model (user directive):** while cargo lives on the ship it is abstract
> `{id, Damage, amount}` entries (unbounded ints, no `ItemStack`s); conversion to
> 64-chunked stacks happens exactly once at the delivery boundary
> (`USSShipCargo.toStacks`, called by the bay's `deliver`). Known slice
> limitation: the pool is a small 16×64 buffer — cargo that does not fit is
> dropped at the bay door (never lost silently); continuous extraction comes
> with the Phase 6 Extractor. 297 unit tests green (incl. NBT round-trips, FSM
> loop length, cargo boundary), §1.1 zero-diff re-verified.
> Deferred (out of slice): recipes (creative-only, per standing directive),
> Extractor/energy, GUI/NEI, star death, construction ships.

- `MTEVoidcraftStorageBay` (N slots, I/O, persist across collapses).
- `MTEVoidcraftGateway` (1×1/3×3/5×5, tier-check vs SC meta, dock/undock ticks).
- `USSimulationEngine` travel + Miner work + return + cargo to `cargoPool`.
- **Exit:** full loop — digitize a Miner (Ph. 1), launch, it mines a planet,
  returns cargo, ship item recovers (recoverable) or is consumed (expendable);
  cargo in pool. Legacy EoH coexists throughout.

### Phase 4 — Star + collapse + Universium + Explorer/Starlifter  (L)
- Star pools (White/Black Dwarf, Stellar Plasma, trace Universium) + Starlifter.
- Lifespan → collapse → supernova (supermassive only) → Universium yield
  `base * (1+rippleBonus)`.
- Ripples + `scanPower` Explorer; `rippleBonus` accumulation.
- **Exit:** a Starlifter drains star pools; a supermassive system supernovas and
  yields Universium; Explorers raise the yield.

### Phase 5 — Constructors + USS structures + Star Igniter  (L)
- Construction Arm ships; structures: hyperlane (travel ÷2), rail accelerator
  (miners dump in place), Dyson Sphere (energy), Star Igniter (force white-dwarf
  supernova).
- Structure build/deconstruct progress; benefit application in the engine.
- **Exit:** a Constructor builds a hyperlane and a Star Igniter; travel time
  drops; a white dwarf supernovas early on demand.

### Phase 6 — Dimensional Extractor + energy integration  (M)
- `MTEVoidcraftExtractor` (item/fluid/EU out, gated by in-USS structures, tiered).
- Dyson energy → `energyPool` → extractable; new machine's energy model finalized.
- **Exit:** Extractor pulls cargoPool/energy out through hatches only when the
  required structure exists.

### Phase 7 — Rendering, GUI, NEI, balance, config  (M–L)
- Drive the **existing** orbit render block (`OrbitingObject` list) with USS
  planets/star/ripples/ships from the new MTE; fall back to extending
  `EOHTileEntitySR` only if the object list is insufficient.
- Module GUIs (slot views, ship list, USS state), NEI recipe map for the assembler
  (optional), full localization (en_US + ru_RU), config knobs, balance pass.
- Optional: custom `ItemVoidcraft` hologram renderer; "Matter Manipulator" copy
  convenience (proposal mentions it — scope as optional).
- **Exit:** visual + info polish complete; system feature-complete and ready for
  the (separate, future) legacy-EoH deprecation decision.

---

## 6. Key risks & open questions

1. **Coexistence is a hard requirement, not a nicety** — zero edits to the §1.1
   do-not-modify list; both machines must build and run in the same world with no
   ID conflicts, no shared mutable statics, and no cross-interference (both match
   the same casing blocks; structure claims are per-controller-position, which
   structurelib already isolates). Verify in Phase 2 (and keep the diff check in
   every phase).
2. **Structure mirroring drift** — the new MTE copies the EoH shape strings and
   element mapping. If the legacy structure ever changes, the mirror must be kept
   in sync (or the deprecation removes one copy). Note the dependency explicitly
   in `MTEUnstableSolarSystem` javadoc.
3. **Module-face geometry** — the exact 5 face-center offsets/orientations must be
   derived from the (mirrored) structure definition (one face currently hosts the
   `~` controller). Phase 2 must pin these down before modules can link.
4. **ItemVoidcraft inventory restriction** — the proposal says ships "cannot be
   taken into inventory/storage". v1 recommendation: allow normal item behavior
   (simpler, debuggable) and only route them through the dedicated network by
   convention; add a hard restriction (custom valid-slot checks) later if needed.
5. **Energy model for the new machine** — the legacy EoH is a net **EU producer**;
   decide what the new machine does with EU (consume power for ignition/USS ops
   with Dyson offsetting internal draw, or also produce). Balance call, not a
   code blocker.
6. **Universium supply** — `Universium` is already consumed by other late-game
   chains (BEC nanite tier, `NaniteChain`, etc.). Adding a supernova source means
   rebalancing those consumers; coordinate with the eventual EoH deprecation.
7. **NBT size** — a 5×5×10 = 250-cell grid per ship is fine as a compressed byte
   array, but keep the `cargoPool` and `planets[]` vein maps bounded (dedupe by
   item, cap distinct entries) to avoid bloated saves.
8. **Per-tick cost** — the USS engine runs on the new MTE's tick; keep it
   O(ships + planets) with a 20-tick work budget to avoid lag with many ships,
   especially now that legacy EoH machines can tick alongside it.
9. **Determinism vs. seed** — a per-ignition `seed` drives star/planet/ripple
   layout. Confirm the proposal's "removes EoH RNG" intent: the new system has no
   *success-chance* RNG, only a *layout* seed; the legacy machine keeps its RNG.
   Flag for design sign-off.
10. **Deprecation path (out of scope, planned)** — after the deprecation period:
    decide whether the legacy EoH recipe path is removed, gated, or left forever;
    that change will be a separate PR touching the §1.1 files deliberately.

---

## 7. Verification plan

- **Unit tests** (pure classes, `src/test/java/tectech/voidcraft/`):
  `VoidcraftBlueprint` stat math, `VoidcraftRole` hybrid penalty, `VoidcraftUSS`
  NBT round-trip, star-type roll, supernova yield formula. Run: `gradlew test`.
- **In-game** (`gradlew runClient` / `runServer`, the `functionalTest` mod in
  `src/functionalTest` is available for scripted checks): smallest case —
  Phase 1: digitize a 1×1×3 Miner ship, inspect item. Phase 3: low-tier USS +
  launch → mine → return → extract cycle.
- **Coexistence regression (from Phase 2 on):** legacy EoH built in the same
  world runs its existing recipe path identically (chance/pity/parallel outputs
  unchanged); **git diff confirms zero changes to the §1.1 do-not-modify list**.
- **Save integrity:** unload/reload mid-USS and after a collapse; confirm ships,
  pools, and phase persist on the new machine; legacy EoH save data untouched.

---

## 8. Suggested next step

Start **Phase 0** (Voidcraft components + stat math) and **Phase 1**
(`ItemVoidcraft` + Voidcraft Assembler). Both are standalone — no USS, no EoH
dependency — and they produce the first tangible, testable result (a digitized
ship in hand) early. They also settle the component stat vectors and tier gates
before the USS multiblock (Phase 2) needs to consume them.

---

## 9. Handoff: verified codebase facts for the implementation session

Everything below was verified by reading this repository (session 2026-08-22).
Line numbers are from that read; re-verify before relying on them.

### 9.1 Build / run / test

- Windows repo root: `E:\Programming\Java\GT5-Unofficial` (Gradle wrapper present:
  use `gradlew.bat` on Windows, `gradlew` elsewhere).
- Build system: GTNH convention plugin (`com.gtnewhorizons.gtnhconvention`,
  RetroFuturaGradle) — see `build.gradle.kts`. Checkstyle runs (keep style clean).
- **Unit tests**: JUnit 5 (JUnit platform), sources in `src/test/java/...`,
  task `gradlew test`. Existing tectech examples:
  `src/test/java/tectech/util/TTUtilityTest.java`,
  `src/test/java/tectech/mechanics/boseEinsteinCondensate/BECFactoryNetworkTest.java`.
- **In-game**: `gradlew runClient` / `gradlew runServer`; a functional-test mod
  lives in `src/functionalTest/java/gregtech/test/` (`GT5TestMod.java`), wired
  into both run tasks automatically.
- Minecraft/Forge: 1.7.10-era GT5 (Forge 1.7.10 API: `ItemStack`, `NBTTagCompound`,
  `GameRegistry`, `ForgeDirection`, `IIconRegister` — no modern conveniences).

### 9.2 Exact patterns to copy (with locations)

- **Custom plain Item**: `tectech/thing/item/ItemAstralArrayFabricator.java` —
  private constructor, `setHasSubtypes(false)`, `setUnlocalizedName(...)`,
  `setTextureName(Reference.MODID + ":...")`, `setCreativeTab(TecTech.creativeTabTecTech)`,
  static `run()` doing `GameRegistry.registerItem(INSTANCE, ...)` +
  `CustomItemList.X.set(INSTANCE)`, `addInformation` tooltip override.
  Registered from `tectech/loader/thing/ThingsLoader.java` line 87.
- **Multi-meta casing block**: `tectech/thing/casing/SpacetimeCompressionFieldCasing.java` —
  extends `gregtech.common.blocks.BlockCasingsAbstract` with own
  `ItemCasingsSpacetime` item class; textures on
  `Textures.BlockIcons.casingTexturePages[7][b + 16]` (page 7, offset 16 — the page
  is allocated in `ThingsLoader.java` lines 46–48); per-meta names via
  `GTLanguageManager.addStringLocalization`; channel indicators via
  `GTStructureChannels.EOH_COMPRESSION.registerAsIndicator(new ItemStack(this,1,i), i+1)`;
  `CustomItemList.SpacetimeCompressionFieldGeneratorTierN.set(new ItemStack(this,1,N))`;
  `getSubBlocks` for creative tab. Container: `TTCasingsContainer`
  (`tectech/thing/casing/TTCasingsContainer.java`, EoH field casings at line ~18).
  Tier name strings live in `tectech/util/CommonValues.EOH_TIER_FANCY_NAMES`.
- **Machine registration**: `tectech/loader/thing/MachineLoader.java` line 2001:
  `Machine_Multi_EyeOfHarmony.set(new MTEEyeOfHarmony(EyeofHarmony.ID,
  "multimachine.em.eye_of_harmony", "Eye of Harmony").getStackForm(1L));` with the
  ID from `gregtech/api/enums/MetaTileEntityIDs.java` (EoH = `EyeofHarmony(15410)`).
  New machines: add enum entries **≥ 32766** (32765 is the highest ID in use),
  add `CustomItemList` entries, register in `MachineLoader` (or the new
  `VoidcraftMachineLoader`), item texture + lang entry.
- **Loader wiring**: `tectech/loader/MainLoader.java` — line 34
  `new ThingsLoader().run();`, line 44 `new MachineLoader().run();`.
  The new `VoidcraftLoader` gets one additive line here.
- **Structure definition**: builder pattern `IStructureDefinition.<T>builder()
  .addShape("main", StructureUtility.transpose(new String[][]{...})).addElement('X', ...)
  .build();` — full real example in `MTEEyeOfHarmony` lines ~147–776.
  Hatch adder: `GTStructureUtility.buildHatchAdder(MTE.class, HatchElement.InputBus,
  InputHatch, InputHatch, OutputBus, OutputHatch)` on the `H` element (line ~754),
  hatches attached to `InfiniteSpacetimeEnergyBoundaryCasing`.
  Simpler module structure example: `gtnhintergalactic/tile/multi/elevatormodules/
  TileEntityModuleBase.java` lines 81–95 (`ofHatchAdderOptional`, 2×2 "H" face +
  `~` controller).
- **Module ↔ host linkage precedent**: `TileEntityModuleBase.isConnected` +
  `TileEntitySpaceElevator` lookup — the pattern for Gateway/Bay/Extractor linking
  to `MTEUnstableSolarSystem` by fixed face offset.
- **Multiblock base API** (`TTMultiblockBase`, 2506 lines): constructor takes
  `(int aID, String aName, String aNameRegional)`; implement `getStructure_EM()`,
  `checkMachine()`, `checkProcessing_EM()`, `outputAfterRecipe_EM()` (or override
  `onPostTick`), `onPreTick()` for tick work; `saveNBTData`/`loadNBTData` for
  persistence (see EoH lines ~1594–1727 for big NBT + list-of-ItemStack patterns
  using `ItemStackLong`/`FluidStackLong` from `tectech/util/`);
  `useLongPower = true` for long energy; `getDefaultHasMaintenanceChecks()` → false
  to skip maintenance (EoH line ~1735); `supportsSingleRecipeLocking()` → false;
  activity sound via `getActivitySoundLoop()` (`SoundResource`, e.g.
  `GT_MACHINES_EYE_OF_HARMONY_LOOP` at `SoundResource.java` line 103 — add a new
  entry for the new machine's sound); GUI default = `TTMultiblockBaseGui`
  (line ~2112), subclassable for extra panels (see `gregtech/common/gui/modularui/
  multiblock/base/TTMultiblockBaseGui.java`).
- **EoH structure facts for the Phase 2 mirror** (from `MTEEyeOfHarmony`):
  33×33×33 spherical shell; elements: `A` spacetime compression (metas 0–8),
  `S` stabilisation (metas 0–8), `E` time dilation (metas 0–8), `C`/`D` BA0
  casings (metas 11/10), `H` hatches (1× InputBus, 2× InputHatch, 1× OutputBus,
  1× OutputHatch, **0 energy hatches required — `checkMachine` rejects them**,
  line ~883), `~` controller at front-face center. Tier read from
  `spacetimeCompressionFieldMetadata` (line ~1152). Recipe lookup via
  `EyeOfHarmonyRecipeStorage.recipeLookUp` (line ~1078). EU output via
  `WirelessNetworkManager.addEUToGlobalEnergyMap(userUUID, ...)` (line ~1362).
  Render block: `TTCasingsContainer.eyeOfHarmonyRenderBlock`, placed by
  `createRenderBlock()` (line ~1262) at the back of the controller; driven by
  `TileEntityEyeOfHarmony.setTier/setStarSize/getOrbitingObjects`
  (`OrbitingObject(block, distance, rotationSpeed, orbitSpeed, xAngle, zAngle, scale)`).
- **Data for the USS** (to be re-implemented, not called): `EyeOfHarmonyRecipe`
  `SPECIAL_FLUIDS` (lines 46–56: White/Black Dwarf Matter + Universium molten
  amounts 1_152/4_608/18_432 per tier group), `processHelper*` planet→vein math,
  `validDustGenerator`, `plasmaEnergyMap` (lines 607–619), `VALID_PLASMAS`
  (lines ~560–605), hydrogen/helium requirements per recipe, `miningTimeSeconds`.
  Materials: `gregtech/api/enums/Materials.java` lines ~1074–1077
  (`RawStarMatter`, `WhiteDwarfMatter`, `BlackDwarfMatter`, `Universium`);
  `MaterialsIDMap`: Universium=139, WhiteDwarfMatter=585, BlackDwarfMatter=586.
- **Universium consumers to keep in mind** (risk #6):
  `bartworks/common/loaders/ElectricImplosionCompressorRecipes.java` (lines 95–97:
  nanites of White/Black Dwarf + Universium), `BECRecipes`/`NaniteChain` usage.
- **Existing EoH recipes that stay untouched**: `ResearchStationAssemblyLine.java`
  line 176 (EoH crafting), `BECRecipes.addEyeOfHarmonyRecipes()` (line 408, casings
  line 439).

### 9.3 Decisions log (what is settled)

1. **Legacy EoH untouched** (deprecation period); new system = parallel machine.
   (User decision.)
2. **Implementation order**: Voidcraft (components/stat math → ItemVoidcraft →
   Assembler) **before** the new USS multiblock. (User decision.)
3. USS = pure data model (NBT on the new MTE), ticked server-side; ships are not
   world blocks.
4. Voidcraft ships = non-stackable NBT data items, valid only in the voidcraft
   network (hard restrictions deferred, see risk #4).
5. Gateway size tiers (1×1 / 3×3 / 5×5) gated by Spacetime Compression casing tier
   (0–2 / 3–5 / 6–8 → max ship size; exact mapping balanceable).
6. New machine mirrors the legacy structure (one shell can host either machine,
   chosen by controller block); both can coexist in-world.
7. Star types: main-sequence / white dwarf / supermassive; only supermassive
   supernovas naturally; Star Igniter (constructor-built) forces white dwarfs.
   Universium = supernova byproduct × (1 + rippleBonus) + mid-tier trace leak.
8. Roles: Miner / Constructor / Starlifter / Explorer, hybrid penalty for mixed
   roles; integrity stat decides recoverable-vs-expendable on collapse.
9. IDs: new MTE IDs ≥ 32766. New package: `tectech.voidcraft.*`.
10. Energy model, inventory restrictions, layout-seed sign-off: **open**
    (risks #4, #5, #9) — decide before Phase 2/6.
11. **Phase 1.5 — components are machine-block MTEs, not a casing block.**
    The old `BlockVoidcraftComponents` / `ItemVoidcraftComponents` (page-7 casing
    multi-meta block) are **deleted**; each component is now an
    `MTEVoidcraftComponent` on the standard GT machine block. This gives
    wrench-facing, per-face textures, and — the point — **six cover slots per
    hull block** (GT5U cover system). Consequence: old component blocks already
    placed in dev worlds become air on the upgrade; accepted pre-release.
12. **Covers are the compact parts.** 8 covers
    (`VoidcraftCoverComponent`, one per non-controller component, no controller
    cover) mount via sneak + right-click on a component block
    (`CoverRegistry.registerCover` + `CoverPlacer.onlyPlaceIf` +
    `MTEVoidcraftComponent.allowCoverOnSide` — dual gate: only Voidcraft cover
    items, only on component blocks). Crowbar removes. Covers contribute stats
    to the ship; a thruster cover (`THRUSTER_NOZZLE`) is the compact engine and
    counts toward the "has an engine" requirement.
13. **Thrust is a vector.** Engine exhaust leaves the block's front face
    (wrench to aim); a thruster cover fires out of its mounted face; the ship is
    pushed the **opposite** way (thrust = −faceVector × magnitude).
    `VoidcraftStats.thrust` = best single axis = max(|dx|,|dy|,|dz|); the
    `thrustX/Y/Z` net vector is stored in NBT and shown in the item tooltip.
    New validation key `voidcraft_thrusters_cancelled` when Σ magnitudes > 0
    but net = 0. Facing stored as `ForgeDirection.ordinal + 1` (missing →
    treated as DOWN = placement default).
14. **NBT format v2** (`NBT_FORMAT_VERSION = 2`, strict equality on read):
    `vc_grid` (cells, 0 = empty, else component id+1) + `vc_facing` (cells, 0 or
    ordinal+1) + `vc_covers` (cells×6, 0 or cover id+1, index
    `cell*6+ForgeDirection.ordinal`) + denormalized `vc_thrust` and
    `vc_thrust_x/y/z`. v1 items are rejected, not migrated. Assembler pending
    NBT carries the same three grids.
15. **MTE IDs 32058–32066** (after `UnstableSolarSystem` 32057, inside the
    32737-32760 runtime-reserved gap left clear): Controller 32058, Engine
    32059, Utility 32060, CargoBay 32061, MiningCentre 32062, StarlifterArray
    32063, SpacetimeScanner 32064, ConstructionArm 32065, Reactor 32066.
    Registered in `VoidcraftLoader.load()` (MTE ctor is load-phase only).
    Cover items registered in `preLoad()`; cover *placements* in `load()`.
16. **Hull MTE contract** (verified against this codebase): must override
    `allowPutStack`/`allowPullStack` (false — no inventory),
    `loadNBTData`/`saveNBTData` (no-ops), `getTileEntityBaseType()`
    (`HarvestTool.WrenchLevel2` — the machine-block meta encodes the harvest
    tool; casing-style → wrench tier 2, as `MTEModificationTable` does), and
    `getDescription()` returning pre-translated lines
    (`@IMetaTileEntity.SkipGenerateDescription` shows them verbatim). Item
    icons: override `getIcon(ItemStack,int)` (this codebase's `Item` has no
    1-arg `getIcon(int)`); translation helpers come from
    `net.minecraft.util.StatCollector` (not `GTLanguageManager`).
17. **Directives (user, standing)**: (a) **Backwards compatibility does not
    matter** — no partial implementation reaches user hands, so never maintain
    legacy NBT versions or migration paths (the v2 strict-version check may be
    simplified whenever NBT is touched); (b) **ignore recipes** until explicitly
    asked — creative-mode testing is the main loop for now. The BEC recipe set
    was **removed** (it crashed the game at postLoad); items are creative-only
    for now and any future recipes come in a deliberate pass.
18. **Render NPE bug — found & fixed** (placing a component block crashed with
    `GTIconFlipped.getMaxU` NPE): `Textures.BlockIcons.custom(name)`
    deduplicates `GTCustomBlockIconContainer`s in a name-keyed map that
    `GTClient.onLoadComplete` → `Textures.BlockIcons.cleanup()` **clears**. Any
    lookup after that instant (placed blocks created via `newMetaEntity`,
    covers constructed at mount time) returned a *fresh* container whose icon
    was never registered (the icon load phase had already run) → `getIcon()`
    null → NPE at render. Fix: `tectech.voidcraft.VoidcraftTextures` — resolves
    every component/cover texture once during the load phase
    (`VoidcraftLoader.load()` calls `VoidcraftTextures.resolveAll()` first) and
    caches the `ITexture` in a static `EnumMap` that survives `cleanup()`;
    `MTEVoidcraftComponent` (both ctors, incl. `newMetaEntity`),
    `CoverVoidcraftComponent`, and `VoidcraftLoader.registerCovers` all use the
    cache and never re-resolve by name. Rule for all future voidcraft
    textures: load-phase resolve + static cache only.
19. **Cover item icons** (error texture in inventory): item-atlas icons resolve
    `gregtech:iconsets/<NAME>` → `gregtech:textures/items/iconsets/<NAME>.png`
    (verified via `GTItemIconContainer`: name = `gregtech:iconsets/<NAME>`,
    resource = `textures/items/iconsets/<NAME>.png`) — block icons
    (`textures/blocks/...`) do NOT serve item icons. Fixed by adding
    `assets/gregtech/textures/items/iconsets/EM_DIM_1..8.png` (+animated
    mcmeta), copied from the blocks tree. Note: mounting a cover on the block
    it mirrors (e.g. Thruster Nozzle on Engine) shows the identical icon —
    visually indistinguishable by design; test covers on a *different*
    component block.

20. **Cover item names showed raw lang keys**: vanilla 1.7.10
    `Item.getUnlocalizedName()` *prefixes* `item.` (verified in decompiled
    `Item.java:614`), so a plain-`Item` subtype's display-name key is
    `item.<unlocalized>.<meta>.name` — proven by the working tectech
    precedent `ItemTeslaCoilCover` → `item.tm.teslaCover.0.name`. ItemBlock
    derived items do NOT get the prefix (`ItemBlock` overrides both name
    getters to return `block.getUnlocalizedName()`), which is why GT machine
    names resolve to `gt.blockmachines.<metaName>.name` (self-registered at
    MTE construction). Fixed: lang keys `tt.voidcraft_cover.N.name` renamed to
    `item.tt.voidcraft_cover.N.name` (en_US + ru_RU; zh_CN has no voidcraft
    entries). Related: `item.tm.voidcraft.name` was `Voidcraft %s` — a literal
    `%s` in the name slot (vanilla name path never formats args); now the name
    slot is plain `Voidcraft` and the formatted ship line uses new key
    `item.tm.voidcraft.named=Voidcraft %s` (`ItemVoidcraft:115`).

 21. **Phase 2 slice — implementation notes** (vertical slice, "keep it simple"):
     - Structure block copied **verbatim** from `MTEEyeOfHarmony:146–776`
       (builder chain with 6 `addElement`s incl. the `'H'` hatch adder
       `buildHatchAdder(...).atLeast(InputBus, InputHatch, InputHatch,
       OutputBus, OutputHatch).casingIndex(...).hint(1).buildAndChain(...)`);
       element keys are **chars** (`'A'` not `"A"`); anchor `checkPiece(main,
       16, 16, 0)`; the `~` controller sits in slice #16 dead center.
     - `checkProcessing_EM()` → `CheckRecipeResultRegistry.NO_RECIPE` (not a
       recipe machine; the star lifecycle runs in `onPostTick` server-side,
       gated on `mMachine`). `stopMachine`/`onBlockDestroyed` → star goes cold
       + render block removed (the render block is a live block in the world —
       must be cleared or it becomes orphan geometry).
     - Controller insert mirrors EoH: fill `mInventory[controllerSlotIndex]`
       with `heldItem.copy()` (size 1) + `ItemUtils.depleteStack(heldItem, 1)`
       (`gtPlusPlus.core.util.minecraft.ItemUtils` — `GTUtility` has **no**
       `depleteStack`); burnout consumes the controller from the slot.
     - 1.7.10 gotchas hit: `EnumChatFormatting + number` does not compile —
       EoH's pattern is `"" + YELLOW + (n) + RESET` (leading `""` makes a
       String chain); Forge config is
       `net.minecraftforge.common.config.Configuration` (not
       `...common.Configuration`); config dir before pre-init events =
       `FMLInjectionData.data()[6]` game dir (GGConfigLoader pattern);
       `NBTTagCompound` has no `getSize()` (that's `NBTTagList`) — use a
       `hasKey(i)` loop; PowerShell 5.1 `Set-Content -Encoding UTF8` writes a
       BOM that javac rejects — strip EF BB BF.
     - `voidcraft.enabled` master switch: `VoidcraftConfig.init()` runs at the
       very start of `VoidcraftLoader.preLoad()` (config dir =
       `<game>/config/voidcraft.cfg`); when false **nothing** voidcraft
       registers (items, MTEs, covers) — the legacy EoH is unaffected.
     - Deferred from the slice (by design): module-face anchors + USS API
       stubs, EU/energy model (`USSConstants.starDrawEUt` is a documented
       placeholder), star death effects (Phase 4).

### 9.4 Where to put new work (quick map)

- New code → `src/main/java/tectech/voidcraft/...` (§3 layout).
- Unit tests → `src/test/java/tectech/voidcraft/...`.
- Item/block textures → `src/main/resources/assets/gregtech/textures/items/` and
  `.../textures/blocks/` (16×16 PNGs; casing pages via `casingTexturePages[7]`).
- Lang → `src/main/resources/assets/gregtech/lang/en_US/lang.properties`
  (and `ru_RU/`), tooltip pattern: existing `eye-of-harmony.md` tooltips under
  `assets/gregtech/lang/en_US/tooltip/` show the machine tooltip convention.
- New sound (optional) → `SoundResource` enum entry + sound json under
  `assets/gregtech/sounds/`.
