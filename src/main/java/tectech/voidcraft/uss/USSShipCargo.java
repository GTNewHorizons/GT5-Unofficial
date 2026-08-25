package tectech.voidcraft.uss;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import gregtech.api.enums.Materials;

/**
 * Deterministic cargo of a completed mission (plan Phase 3; Phase 4 pass 1 adds Starlifter cargo; Phase 4 pass 3
 * switches the miner to the star's planets):
 *
 * <p>
 * <strong>Miner</strong> (Phase 4 pass 3): the miner works the star's <em>planets</em> — cargo is the union
 * of the planets' ore materials (deduplicated, {@link USSPlanets#materialsOf(List)}) plus stone dust, all as dust.
 * The system's planets come from the star's TYPE (its planet pool — {@link USSPlanetType}) — "the different
 * planet types determine what can be mined from the system".
 *
 * <p>
 * <strong>Abstract representation.</strong> While cargo lives on the ship (and in its NBT) it is a list of
 * <em>abstract entries</em> — {@code {id: item id, Damage: meta, amount: int}} — NOT {@link ItemStack}s. Amounts are
 * plain ints (the 1.7.10 NBT {@code Count} byte and GT's 64-item stack cap do not apply), so a mission can carry
 * 10 000 dust without ever materializing 157 stacks. The conversion to 64-chunked {@link ItemStack}s happens exactly
 * once, at the delivery boundary ({@link #toStacks(NBTTagList)}, called by the bay's
 * {@code deliver} before the pool merge — the pool is the only 64-slot structure involved).
 *
 * <p>
 * Amounts come from {@link USSConstants#minerOreAmount(long)} (per ore) and
 * {@link USSConstants#minerStoneDustAmount(long)} (stone dust).
 */
public final class USSShipCargo {

    /** Tag under which the item list lives in a cargo compound. */
    public static final String TAG_ITEMS = "vc_items";

    /** Abstract entry: item id (1.7.10 item registry id). */
    public static final String ENTRY_ID = "id";

    /** Abstract entry: item meta / damage. */
    public static final String ENTRY_DAMAGE = "Damage";

    /** Abstract entry: total amount (unbounded int — no stack cap while the cargo is abstract). */
    public static final String ENTRY_AMOUNT = "amount";

    /**
     * Abstract item entry (optional): the GT material name the dust resolves from — written by Constructor loadouts
     * so the applying side can credit the right material without a reverse item lookup (mining cargo omits it).
     */
    public static final String ITEM_ENTRY_MATERIAL = "material";

    // Phase 4 pass 1 — Starlifter fluid cargo (fluids stay ABSTRACT while on the ship, like the items: the pool and
    // the delivery boundary are the only fluid-aware structures).

    /** Tag under which the fluid list lives in a cargo compound. */
    public static final String TAG_FLUIDS = "vc_fluids";

    /** Abstract fluid entry: the GT material name the fluid resolves from (stable across builds; NOT a registry id). */
    public static final String FLUID_ENTRY_MATERIAL = "material";

    /** Abstract fluid entry: amount in millibuckets (unbounded long — no capacity while the cargo is abstract). */
    public static final String FLUID_ENTRY_AMOUNT = "amount";

    private USSShipCargo() {
        throw new AssertionError("Static helpers");
    }

    /**
     * The result of mining one planet: the cargo (dust entries) + the new reserve (after this mission's draw).
     */
    public static final class MinerResult {

        /**
         * The cargo compound (a {@link #TAG_ITEMS} list of abstract dust entries; empty when the planet yields
         * nothing).
         */
        public final NBTTagCompound cargo;

        /** The planet's reserve after this mission's draw (never null). */
        public final VoidcraftUSS.PlanetReserve newReserve;

        MinerResult(NBTTagCompound cargo, VoidcraftUSS.PlanetReserve newReserve) {
            this.cargo = cargo;
            this.newReserve = newReserve;
        }
    }

    /**
     * Mine ONE planet (the mechanics pass): the planet's registered ores, weighed by their weights, each capped by
     * the planet's remaining reserve. The reserve is initialized from the planet definition on the first mine
     * ({@code ore.amount × 1_000_000 × planetSize²}, see {@link VoidcraftUSS.PlanetReserve#fromPlanet}) and then
     * decremented by the mined amount — so ores deplete over the planet's lifetime.
     *
     * <p>
     * Per-mission yield per ore = {@code min(minerOreAmount(miningPower) × (weight / Σweight),
     * reserve.remaining(ore))}. The total (when the reserve is sufficient) is {@code minerOreAmount(miningPower)} —
     * the ship's cargo amount for the mission — split across the ores by their weights.
     *
     * @param planet         the planet to mine (null or empty-ore → empty cargo, reserve unchanged)
     * @param miningPower    the ship's total mining power (the per-mission base amount)
     * @param currentReserve the planet's current reserve (null → initialized from the planet definition)
     * @return the cargo + the new reserve (both non-null)
     */
    public static MinerResult minePlanet(USSPlanets.USSPlanet planet, long miningPower,
        VoidcraftUSS.PlanetReserve currentReserve) {
        if (planet == null || planet.definition == null) {
            return new MinerResult(
                new NBTTagCompound(),
                currentReserve != null ? currentReserve
                    : new VoidcraftUSS.PlanetReserve(new java.util.LinkedHashMap<>()));
        }
        List<USSPlanetOre> ores = planet.definition.getOres();
        if (ores == null || ores.isEmpty()) {
            return new MinerResult(
                new NBTTagCompound(),
                currentReserve != null ? currentReserve
                    : new VoidcraftUSS.PlanetReserve(new java.util.LinkedHashMap<>()));
        }

        // Initialize the reserve from the planet definition (ore.amount × planetSize²) when not present.
        VoidcraftUSS.PlanetReserve reserve = currentReserve != null ? currentReserve
            : VoidcraftUSS.PlanetReserve.fromPlanet(planet.definition, planet.scale);

        long base = USSConstants.minerOreAmount(miningPower);
        double totalWeight = 0.0;
        for (USSPlanetOre ore : ores) {
            totalWeight += Math.max(0.0, ore.getWeight());
        }
        if (totalWeight <= 0.0) {
            return new MinerResult(new NBTTagCompound(), reserve);
        }

        // Per-ore yield: base × (weight / Σweight), capped by the reserve. The reserve decreases by the mined amount.
        NBTTagList items = new NBTTagList();
        for (USSPlanetOre ore : ores) {
            Materials material = ore.getOreType();
            if (material == null || material == Materials._NULL || ore.getWeight() <= 0.0) {
                continue;
            }
            long share = (long) (base * (ore.getWeight() / totalWeight));
            long capped = Math.min(share, reserve.remaining(material));
            if (capped <= 0L) {
                continue;
            }
            NBTTagCompound entry = abstractEntry(material, capped);
            if (entry != null) {
                items.appendTag(entry);
            }
            reserve = reserve.mine(material, capped);
        }

        NBTTagCompound cargo = new NBTTagCompound();
        cargo.setTag(TAG_ITEMS, items);
        return new MinerResult(cargo, reserve);
    }

    /**
     * Build the cargo of a completed Starlifter mission (the mechanics pass): the star's three registered
     * materials, each as a FLUID entry (they are "primarily fluids"), with amount =
     * {@code starlifterPlasmaAmount(miningPower) × weight × √(star size)}. The weights set the relative split;
     * the star size (sampled from the star's size range, a pure function of the star type + seed) scales the total.
     *
     * <p>
     * Deterministic: the star size is derived from the seed (no RNG outside {@link USSPlanets#sampleStarSize}).
     *
     * @param starType    the star the ship mined (null → main sequence, defensive).
     * @param miningPower the ship's total mining power.
     * @param seed        the star's ignition timestamp (the seed for the star-size draw — same as
     *                    {@link USSPlanets#sampleStarSize(USSStarType, long)}).
     * @return a cargo compound with a {@link #TAG_FLUIDS} list of the star's 3 materials (defensive: empty when the
     *         star is unregistered).
     */
    public static NBTTagCompound buildForStarlifter(USSStarType starType, long miningPower, long seed) {
        if (starType == null) {
            starType = USSStarType.MAIN_SEQUENCE;
        }
        USSStarDefinition star = USSStarRegistry.byType(starType);
        if (star == null) {
            return new NBTTagCompound(); // defensive: no registered star → empty cargo
        }
        long base = USSConstants.starlifterPlasmaAmount(miningPower);
        double sizeFactor = Math.sqrt(USSPlanets.sampleStarSize(starType, seed));

        NBTTagCompound cargo = new NBTTagCompound();
        NBTTagList fluids = new NBTTagList();
        fluids.appendTag(
            fluidEntry(
                star.getMain()
                    .getMaterial(),
                (long) (base * star.getMain()
                    .getWeight() * sizeFactor)));
        fluids.appendTag(
            fluidEntry(
                star.getSecondary()
                    .getMaterial(),
                (long) (base * star.getSecondary()
                    .getWeight() * sizeFactor)));
        fluids.appendTag(
            fluidEntry(
                star.getTertiary()
                    .getMaterial(),
                (long) (base * star.getTertiary()
                    .getWeight() * sizeFactor)));
        cargo.setTag(TAG_FLUIDS, fluids);
        return cargo;
    }

    /**
     * One abstract FLUID cargo entry: material name + mB amount (the Fluid itself is only resolved at the
     * delivery boundary — the bay's {@code deliverFluids} pump, see {@link #TAG_FLUIDS}).
     */
    private static NBTTagCompound fluidEntry(Materials material, long amountMiliBuckets) {
        NBTTagCompound entry = new NBTTagCompound();
        entry.setString(FLUID_ENTRY_MATERIAL, material.getName());
        entry.setLong(FLUID_ENTRY_AMOUNT, Math.max(0L, amountMiliBuckets));
        return entry;
    }

    /**
     * One abstract cargo entry: the material's unified dust resolved at amount 1 (we need the item + meta; the
     * amount is carried separately and is therefore never clamped), with the full {@code amount}.
     */
    private static NBTTagCompound abstractEntry(Materials material, long amount) {
        ItemStack one = material.getDust(1);
        if (one == null) {
            return null;
        }
        NBTTagCompound entry = new NBTTagCompound();
        entry.setShort(ENTRY_ID, (short) Item.getIdFromItem(one.getItem()));
        entry.setShort(ENTRY_DAMAGE, (short) one.getItemDamage());
        entry.setInteger(ENTRY_AMOUNT, (int) Math.max(0L, amount));
        // The material name — so the cargo hold (the cargo-capacity pass) can resolve the material without a
        // reverse item lookup (and so a future ship-to-ship transfer keeps the material, not just the item id).
        entry.setString(ITEM_ENTRY_MATERIAL, material.getName());
        return entry;
    }

    /**
     * Fill a cargo hold with a cargo (the abstract items + fluids), clamped by the hold's capacity (the
     * cargo-capacity pass: "mining fills their internal cargo capacity, and they cannot mine if it is full").
     *
     * <p>
     * The hold is the ship's internal cargo — this method adds the (clamped) cargo to it. Materials are resolved
     * from each entry's material name (the {@link #ITEM_ENTRY_MATERIAL} / {@link #FLUID_ENTRY_MATERIAL} tags).
     *
     * @param hold  the hold to fill (null → null; the caller keeps no cargo)
     * @param cargo the cargo to add (the abstract items + fluids; null → hold unchanged)
     * @return the updated hold (never null when the input hold is non-null)
     */
    public static CargoHold fillHold(CargoHold hold, NBTTagCompound cargo) {
        if (hold == null || cargo == null) {
            return hold;
        }
        CargoHold next = hold;
        // Items: resolve the material from the entry's material name (the abstractEntry writes it).
        NBTTagList items = readItems(cargo);
        for (int i = 0; i < items.tagCount(); i++) {
            NBTTagCompound entry = items.getCompoundTagAt(i);
            if (entry == null) {
                continue;
            }
            long amount = Math.max(0L, entry.getInteger(ENTRY_AMOUNT));
            Materials material = Materials.get(entry.getString(ITEM_ENTRY_MATERIAL));
            if (amount <= 0L || material == null || material == Materials._NULL) {
                continue;
            }
            next = next.addItems(material, amount);
        }
        // Fluids: resolve the material from the entry's material name.
        NBTTagList fluids = readFluids(cargo);
        for (int i = 0; i < fluids.tagCount(); i++) {
            NBTTagCompound entry = fluids.getCompoundTagAt(i);
            if (entry == null) {
                continue;
            }
            long amount = Math.max(0L, entry.getLong(FLUID_ENTRY_AMOUNT));
            Materials material = Materials.get(entry.getString(FLUID_ENTRY_MATERIAL));
            if (amount <= 0L || material == null || material == Materials._NULL) {
                continue;
            }
            next = next.addFluids(material, amount);
        }
        return next;
    }

    /**
     * Derive a cargo (the abstract items + fluids) from a cargo hold — the delivery-boundary conversion (the hold
     * is the source of truth; this is what the bay receives).
     *
     * @param hold the hold to convert (null → an empty cargo compound)
     * @return a cargo compound with the hold's items ({@link #TAG_ITEMS}) + fluids ({@link #TAG_FLUIDS})
     */
    public static NBTTagCompound cargoFromHold(CargoHold hold) {
        NBTTagCompound cargo = new NBTTagCompound();
        if (hold == null) {
            return cargo;
        }
        NBTTagList items = new NBTTagList();
        for (Map.Entry<Materials, Long> e : hold.getItems()
            .entrySet()) {
            NBTTagCompound entry = abstractEntry(e.getKey(), e.getValue());
            if (entry != null) {
                items.appendTag(entry);
            }
        }
        NBTTagList fluids = new NBTTagList();
        for (Map.Entry<Materials, Long> e : hold.getFluids()
            .entrySet()) {
            fluids.appendTag(fluidEntry(e.getKey(), e.getValue()));
        }
        cargo.setTag(TAG_ITEMS, items);
        cargo.setTag(TAG_FLUIDS, fluids);
        return cargo;
    }

    /**
     * Read the item list of a cargo compound.
     *
     * @param cargo cargo compound (may be null or lack the tag)
     * @return the item list (never null; empty when absent)
     */
    public static NBTTagList readItems(NBTTagCompound cargo) {
        if (cargo == null || !cargo.hasKey(TAG_ITEMS)) {
            return new NBTTagList();
        }
        return cargo.getTagList(TAG_ITEMS, 10);
    }

    /**
     * <strong>Delivery boundary:</strong> convert abstract entries into 64-chunked {@link ItemStack}s — the only
     * place the cargo becomes item stacks.
     *
     * @param entries an abstract entry list ({@link #TAG_ITEMS} format; may be empty)
     * @return 64-chunked stacks (never null; entries with unresolvable ids are skipped)
     */
    public static List<ItemStack> toStacks(NBTTagList entries) {
        List<ItemStack> stacks = new ArrayList<>();
        if (entries == null) {
            return stacks;
        }
        for (int i = 0; i < entries.tagCount(); i++) {
            NBTTagCompound entry = entries.getCompoundTagAt(i);
            if (entry == null) {
                continue;
            }
            Item item = Item.getItemById(entry.getShort(ENTRY_ID));
            if (item == null) {
                continue; // unknown item id (e.g. from another mod removed) — skip rather than corrupt the rest
            }
            int damage = entry.getShort(ENTRY_DAMAGE);
            int amount = Math.max(0, entry.getInteger(ENTRY_AMOUNT));
            while (amount > 0) {
                int take = Math.min(64, amount);
                stacks.add(new ItemStack(item, take, damage));
                amount -= take;
            }
        }
        return stacks;
    }

    /**
     * Read the fluid list of a cargo compound.
     *
     * @param cargo cargo compound (may be null or lack the tag)
     * @return the fluid list (never null; empty when absent)
     */
    public static NBTTagList readFluids(NBTTagCompound cargo) {
        if (cargo == null || !cargo.hasKey(TAG_FLUIDS)) {
            return new NBTTagList();
        }
        return cargo.getTagList(TAG_FLUIDS, 10);
    }

    // Note: the fluid DELIVERY boundary is the bay's deliverFluids (it iterates the abstract entries and resolves
    // name → material → fluid per entry, keeping the pool abstract). There is deliberately no FluidStack list
    // helper here: Forge fluids are a runtime detail and must not leak into the cargo model (a bare JVM cannot even
    // construct a FluidStack — its ctor triggers FluidRegistry's static init).
}
