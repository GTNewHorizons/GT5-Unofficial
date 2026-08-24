package tectech.voidcraft.uss;

import java.util.ArrayList;
import java.util.List;

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
     * Build the cargo of a completed MINING mission (Phase 4 pass 3 — the miner works the star's planets): one dust
     * entry per ore material offered by the system's planets (deduplicated, first-seen order) plus one universal
     * stone dust, each carrying its full amount.
     *
     * @param planets     the star's planet system (see {@link USSPlanets#generate(USSStarType, long)}; null or empty →
     *                    stone dust only, defensive)
     * @param miningPower the ship's total mining power
     * @return a cargo compound holding a {@link #TAG_ITEMS} list of abstract entries
     */
    public static NBTTagCompound buildForMiner(List<USSPlanets.USSPlanet> planets, long miningPower) {
        long oreAmount = USSConstants.minerOreAmount(miningPower);
        long stoneAmount = USSConstants.minerStoneDustAmount(miningPower);

        NBTTagList items = new NBTTagList();
        for (Materials ore : USSPlanets.materialsOf(planets)) {
            NBTTagCompound entry = abstractEntry(ore, oreAmount);
            if (entry != null) {
                items.appendTag(entry);
            }
        }
        NBTTagCompound stone = abstractEntry(Materials.Stone, stoneAmount);
        if (stone != null) {
            items.appendTag(stone);
        }

        NBTTagCompound cargo = new NBTTagCompound();
        cargo.setTag(TAG_ITEMS, items);
        return cargo;
    }

    /**
     * Build the cargo of a completed Starlifter mission (Phase 4 pass 1 — fluid production on top of the miner's
     * item cargo), by the star's TYPE:
     * <ul>
     * <li>{@link USSStarType#MAIN_SEQUENCE}: Stellar Plasma ({@code RawStarMatter} fluid) only.</li>
     * <li>{@link USSStarType#WHITE_DWARF}: White Dwarf Matter dust + Stellar Plasma.</li>
     * <li>{@link USSStarType#SUPERMASSIVE}: Black Dwarf Matter dust + Stellar Plasma.</li>
     * </ul>
     * Deterministic, no RNG (same design contract as the miner cargo).
     *
     * @param starType    the star the ship mined (null → main sequence, defensive).
     * @param miningPower the ship's total mining power.
     * @return a cargo compound with a {@link #TAG_FLUIDS} list (Stellar Plasma) and, for dwarf-class stars, a
     *         {@link #TAG_ITEMS} list with one dwarf-matter dust entry.
     */
    public static NBTTagCompound buildForStarlifter(USSStarType starType, long miningPower) {
        if (starType == null) {
            starType = USSStarType.MAIN_SEQUENCE;
        }
        long plasma = USSConstants.starlifterPlasmaAmount(miningPower);
        long matter = USSConstants.starlifterMatterAmount(miningPower);

        NBTTagCompound cargo = new NBTTagCompound();

        NBTTagList fluids = new NBTTagList();
        fluids.appendTag(fluidEntry(Materials.RawStarMatter, plasma));
        cargo.setTag(TAG_FLUIDS, fluids);

        if (starType != USSStarType.MAIN_SEQUENCE) {
            Materials matterMaterial = starType == USSStarType.WHITE_DWARF ? Materials.WhiteDwarfMatter
                : Materials.BlackDwarfMatter;
            NBTTagCompound dust = abstractEntry(matterMaterial, matter);
            if (dust != null) {
                NBTTagList items = new NBTTagList();
                items.appendTag(dust);
                cargo.setTag(TAG_ITEMS, items);
            }
        }
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
        return entry;
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
