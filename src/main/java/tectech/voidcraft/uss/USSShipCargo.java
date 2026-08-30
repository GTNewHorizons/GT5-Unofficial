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
 * Deterministic cargo of a completed mission (plan Phase 3; reserve depletion per material):
 *
 * <p>
 * <strong>Miner</strong> ({@link #minePlanet}): the miner works its target planet — cargo is that planet's
 * registered ores, each weighed by its weight and capped by the planet's remaining reserve (ores deplete over the
 * planet's lifetime).
 *
 * <p>
 * <strong>Starlifter</strong> ({@link #siphonStar}): the starlifter works the star — cargo is the star's PRODUCED
 * fluids (1–3 of its three materials), each weighed by its weight, scaled by the star size, and capped by the star's
 * remaining fluid reserve (the fluids deplete over the star's life).
 *
 * <p>
 * <strong>Abstract representation.</strong> While cargo lives on the ship (and in its NBT) it is a list of
 * <em>abstract entries</em> — {@code {id: item id, Damage: meta, amount: int}} for items,
 * {@code {material: GT material name, amount: mB}} for fluids — NOT {@link ItemStack}s / {@code FluidStack}s.
 * Amounts are plain ints/longs (the 1.7.10 NBT {@code Count} byte and GT's 64-item stack cap do not apply), so a
 * mission can carry 10 000 dust or 10 000 000 mB without materializing stacks. The conversion to 64-chunked
 * {@link ItemStack}s happens exactly once, at the delivery boundary ({@link #toStacks(NBTTagList)}, called by the
 * bay's {@code deliver} before the pool merge — the pool is the only 64-slot structure involved).
 *
 * <p>
 * Amounts come from {@link USSConstants#minerOreAmount(long)} (mining) and
 * {@link USSConstants#starlifterPlasmaAmount(long)} (siphoning).
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

    // Starlifter fluid cargo (fluids stay ABSTRACT while on the ship, like the items: the pool and the delivery
    // boundary are the only fluid-aware structures).

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
        public final VoidcraftUSS.MaterialReserve newReserve;

        MinerResult(NBTTagCompound cargo, VoidcraftUSS.MaterialReserve newReserve) {
            this.cargo = cargo;
            this.newReserve = newReserve;
        }
    }

    /**
     * Mine ONE planet (the mechanics pass): the planet's registered ores, weighed by their weights, each capped by
     * the planet's remaining reserve. The reserve is initialized from the planet definition on the first mine
     * ({@code ore.amount × 1_000_000 × planetSize²}, see {@link VoidcraftUSS.MaterialReserve#fromPlanet}) and then
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
        VoidcraftUSS.MaterialReserve currentReserve) {
        if (planet == null || planet.definition == null) {
            return new MinerResult(
                new NBTTagCompound(),
                currentReserve != null ? currentReserve
                    : new VoidcraftUSS.MaterialReserve(new java.util.LinkedHashMap<>()));
        }
        List<USSPlanetOre> ores = planet.definition.getOres();
        if (ores == null || ores.isEmpty()) {
            return new MinerResult(
                new NBTTagCompound(),
                currentReserve != null ? currentReserve
                    : new VoidcraftUSS.MaterialReserve(new java.util.LinkedHashMap<>()));
        }

        // Initialize the reserve from the planet definition (ore.amount × planetSize²) when not present.
        VoidcraftUSS.MaterialReserve reserve = currentReserve != null ? currentReserve
            : VoidcraftUSS.MaterialReserve.fromPlanet(planet.definition, planet.scale);

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
     * The outcome of a Starlifter mission (the starlifter pass): the collected fluid cargo plus the star's reserve
     * after this mission's draw.
     */
    public static final class StarlifterResult {

        /**
         * The cargo compound (a {@link #TAG_FLUIDS} list of abstract fluid entries; empty when the star yields
         * nothing).
         */
        public final NBTTagCompound cargo;

        /** The star's fluid reserve after this mission's draw (never null). */
        public final VoidcraftUSS.MaterialReserve newReserve;

        StarlifterResult(NBTTagCompound cargo, VoidcraftUSS.MaterialReserve newReserve) {
            this.cargo = cargo;
            this.newReserve = newReserve;
        }
    }

    /**
     * Siphon ONE star (the starlifter pass): the star's PRODUCED fluids (a zero-capacity slot produces nothing — a
     * star may produce 1–3 of its three materials), each capped by the star's remaining fluid reserve. The reserve is
     * initialized from the star definition on the first siphon
     * ({@code material.amount × 1_000_000 × starSize²}, see {@link VoidcraftUSS.MaterialReserve#fromStar}) and then
     * decremented by the siphoned amount — so the fluids deplete over the star's life (the amount left later feeds
     * the Dyson swarm output and stellar evolution).
     *
     * <p>
     * Per-mission yield per fluid = {@code min(starlifterPlasmaAmount(siphonPower) × weight × √starSize,
     * reserve.remaining(fluid))}. The weights set the relative split; the star size scales the total. A depleted
     * fluid is skipped (0 → no entry); a fully depleted star yields empty cargo, and the mission completes normally
     * (same as a depleted planet).
     *
     * @param star           the star definition to siphon (null → empty cargo)
     * @param starSize       the star's sampled size (negative clamped to 0)
     * @param siphonPower    the ship's total siphon (starlifter) power
     * @param currentReserve the star's current fluid reserve (null → initialized from the star definition)
     * @return the cargo + the new reserve (both non-null)
     */
    public static StarlifterResult siphonStar(USSStarDefinition star, double starSize, long siphonPower,
        VoidcraftUSS.MaterialReserve currentReserve) {
        VoidcraftUSS.MaterialReserve reserve = currentReserve != null ? currentReserve
            : VoidcraftUSS.MaterialReserve.fromStar(star, starSize);
        if (star == null) {
            return new StarlifterResult(new NBTTagCompound(), reserve);
        }

        long base = USSConstants.starlifterPlasmaAmount(siphonPower);
        double sizeFactor = Math.sqrt(Math.max(0.0, starSize));

        // Per-fluid yield: base × weight × √starSize, capped by the reserve. The reserve decreases by the siphoned
        // amount; a zero-capacity or depleted fluid is skipped.
        NBTTagList fluids = new NBTTagList();
        for (USSStarMaterial material : star.getMaterials()) {
            if (material == null || material.getMaterial() == null
                || material.getMaterial() == Materials._NULL
                || material.getAmount() <= 0L
                || material.getWeight() <= 0.0) {
                continue;
            }
            long share = (long) (base * material.getWeight() * sizeFactor);
            long capped = Math.min(share, reserve.remaining(material.getMaterial()));
            if (capped <= 0L) {
                continue;
            }
            fluids.appendTag(fluidEntry(material.getMaterial(), capped));
            reserve = reserve.mine(material.getMaterial(), capped);
        }

        NBTTagCompound cargo = new NBTTagCompound();
        cargo.setTag(TAG_FLUIDS, fluids);
        return new StarlifterResult(cargo, reserve);
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
