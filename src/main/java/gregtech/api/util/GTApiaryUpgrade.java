package gregtech.api.util;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeGenBase;

import gregtech.api.enums.OrePrefixes;
import gregtech.common.items.MetaGeneratedItem03;

/**
 * Actual items are defined in {@link MetaGeneratedItem03}
 */
public enum GTApiaryUpgrade {

    speed1(UNIQUE_INDEX.SPEED_UPGRADE, 32200, 1, (mods, n) -> mods.maxSpeed = 1),
    speed2(UNIQUE_INDEX.SPEED_UPGRADE, 32201, 1, (mods, n) -> mods.maxSpeed = 2),
    speed3(UNIQUE_INDEX.SPEED_UPGRADE, 32202, 1, (mods, n) -> mods.maxSpeed = 3),
    speed4(UNIQUE_INDEX.SPEED_UPGRADE, 32203, 1, (mods, n) -> mods.maxSpeed = 4),
    speed5(UNIQUE_INDEX.SPEED_UPGRADE, 32204, 1, (mods, n) -> mods.maxSpeed = 5),
    speed6(UNIQUE_INDEX.SPEED_UPGRADE, 32205, 1, (mods, n) -> mods.maxSpeed = 6),
    speed7(UNIQUE_INDEX.SPEED_UPGRADE, 32206, 1, (mods, n) -> mods.maxSpeed = 7),
    speed8(UNIQUE_INDEX.SPEED_UPGRADE, 32207, 1, (mods, n) -> mods.maxSpeed = 8),
    speed8upgraded(UNIQUE_INDEX.SPEED_UPGRADE, 32208, 1, (mods, n) -> {
        mods.maxSpeed = 8;
        mods.production = 17.19926784f;
        mods.energy *= 14.75;
    }),
    production(UNIQUE_INDEX.PRODUCTION_UPGRADE, 32209, 8, (mods, n) -> {
        mods.production = 4.f * (float) Math.pow(1.2d, n);
        mods.energy *= Math.pow(1.4f, n);
    }),
    plains(UNIQUE_INDEX.PLAINS_UPGRADE, 32210, 1, (mods, n) -> {
        mods.biomeOverride = BiomeGenBase.plains;
        mods.energy *= 1.2f;
    }),
    light(UNIQUE_INDEX.LIGHT_UPGRADE, 32211, 1, (mods, n) -> {
        mods.isSelfLighted = true;
        mods.energy *= 1.05f;
    }),
    flowering(UNIQUE_INDEX.FLOWERING_UPGRADE, 32212, 8, (mods, n) -> {
        mods.flowering *= Math.pow(1.2f, n);
        mods.energy *= Math.pow(1.1f, n);
    }),
    winter(UNIQUE_INDEX.WINTER_UPGRADE, 32213, 1, (mods, n) -> {
        mods.biomeOverride = BiomeGenBase.taiga;
        mods.energy *= 1.5f;
    }),
    dryer(UNIQUE_INDEX.DRYER_UPGRADE, 32214, 16, (mods, n) -> {
        mods.humidity -= 0.125f * n;
        mods.energy *= Math.pow(1.025f, n);
    }),
    automation(UNIQUE_INDEX.AUTOMATION_UPGRADE, 32215, 1, (mods, n) -> {
        mods.isAutomated = true;
        mods.energy *= 1.1f;
    }),
    humidifier(UNIQUE_INDEX.HUMIDIFIER_UPGRADE, 32216, 16, (mods, n) -> {
        mods.humidity += 0.125f * n;
        mods.energy *= Math.pow(1.05f, n);
    }),
    hell(UNIQUE_INDEX.HELL_UPGRADE, 32217, 1, (mods, n) -> {
        mods.biomeOverride = BiomeGenBase.hell;
        mods.energy *= 1.5f;
    }),
    pollen(UNIQUE_INDEX.POLLEN_UPGRADE, 32218, 1, (mods, n) -> {
        mods.flowering = 0f;
        mods.energy *= 1.3f;
    }),
    desert(UNIQUE_INDEX.DESERT_UPGRADE, 32219, 1, (mods, n) -> {
        mods.biomeOverride = BiomeGenBase.desert;
        mods.energy *= 1.2f;
    }),
    cooler(UNIQUE_INDEX.COOLER_UPGRADE, 32220, 16, (mods, n) -> {
        mods.temperature -= 0.125f * n;
        mods.energy *= Math.pow(1.025f, n);
    }),
    lifespan(UNIQUE_INDEX.LIFESPAN_UPGRADE, 32221, 4, (mods, n) -> {
        mods.lifespan /= Math.pow(1.5f, n);
        mods.energy *= Math.pow(1.05f, n);
    }),
    seal(UNIQUE_INDEX.SEAL_UPGRADE, 32222, 1, (mods, n) -> {
        mods.isSealed = true;
        mods.energy *= 1.05f;
    }),
    stabilizer(UNIQUE_INDEX.STABILIZER_UPGRADE, 32223, 1, (mods, n) -> {
        mods.geneticDecay = 0f;
        mods.energy *= 2.50f;
    }),
    jungle(UNIQUE_INDEX.JUNGLE_UPGRADE, 32224, 1, (mods, n) -> {
        mods.biomeOverride = BiomeGenBase.jungle;
        mods.energy *= 1.20f;
    }),
    territory(UNIQUE_INDEX.TERRITORY_UPGRADE, 32225, 4, (mods, n) -> {
        mods.territory *= Math.pow(1.5f, n);
        mods.energy *= Math.pow(1.05f, n);
    }),
    ocean(UNIQUE_INDEX.OCEAN_UPGRADE, 32226, 1, (mods, n) -> {
        mods.biomeOverride = BiomeGenBase.ocean;
        mods.energy *= 1.20f;
    }),
    sky(UNIQUE_INDEX.SKY_UPGRADE, 32227, 1, (mods, n) -> {
        mods.isSunlightSimulated = true;
        mods.energy *= 1.05f;
    }),
    heater(UNIQUE_INDEX.HEATER_UPGRADE, 32228, 16, (mods, n) -> {
        mods.temperature += 0.125f * n;
        mods.energy *= Math.pow(1.025f, n);
    }),
    sieve(UNIQUE_INDEX.SIEVE_UPGRADE, 32229, 1, (mods, n) -> {
        mods.isCollectingPollen = true;
        mods.energy *= 1.05f;
    }),
    unlight(UNIQUE_INDEX.LIGHT_UPGRADE, 32231, 1, (mods, n) -> {
        mods.isSelfUnlighted = true;
        mods.energy *= 1.05f;
    }),;

    private enum UNIQUE_INDEX {

        SPEED_UPGRADE,
        PRODUCTION_UPGRADE,
        PLAINS_UPGRADE,
        LIGHT_UPGRADE, // also unlight
        FLOWERING_UPGRADE,
        WINTER_UPGRADE,
        DRYER_UPGRADE,
        AUTOMATION_UPGRADE,
        HUMIDIFIER_UPGRADE,
        HELL_UPGRADE,
        POLLEN_UPGRADE,
        DESERT_UPGRADE,
        COOLER_UPGRADE,
        LIFESPAN_UPGRADE,
        SEAL_UPGRADE,
        STABILIZER_UPGRADE,
        JUNGLE_UPGRADE,
        TERRITORY_UPGRADE,
        OCEAN_UPGRADE,
        SKY_UPGRADE,
        HEATER_UPGRADE,
        SIEVE_UPGRADE,;

        void apply(Consumer<GTApiaryUpgrade> fn) {
            UNIQUE_UPGRADE_LIST.get(this)
                .forEach(fn);
        }
    }

    private static final EnumMap<UNIQUE_INDEX, ArrayList<GTApiaryUpgrade>> UNIQUE_UPGRADE_LIST = new EnumMap<>(
        UNIQUE_INDEX.class);

    private int meta = 0;
    private int maxnumber = 1;

    private final GTUtility.ItemId id;
    private final UNIQUE_INDEX unique_index;
    private final BiConsumer<GTApiaryModifier, Integer> applier;

    private final HashSet<GTUtility.ItemId> blacklistedUpgrades = new HashSet<>();

    GTApiaryUpgrade(UNIQUE_INDEX unique_index, int meta, int maxnumber, BiConsumer<GTApiaryModifier, Integer> applier) {
        this.unique_index = unique_index;
        this.meta = meta;
        this.maxnumber = maxnumber;
        this.applier = applier;
        this.id = GTUtility.ItemId.createNoCopy(get(1));
    }

    private void setup_static_variables() {
        quickLookup.put(this.meta, this);
        ArrayList<GTApiaryUpgrade> un = UNIQUE_UPGRADE_LIST.get(this.unique_index);
        if (un != null) un.forEach((u) -> {
            u.blacklistedUpgrades.add(this.id);
            this.blacklistedUpgrades.add(u.id);
        });
        else {
            un = new ArrayList<>(1);
            UNIQUE_UPGRADE_LIST.put(this.unique_index, un);
        }
        un.add(this);
    }

    public static GTApiaryUpgrade getUpgrade(ItemStack s) {
        if (s == null) return null;
        if (!isUpgrade(s)) return null;
        return quickLookup.get(s.getItemDamage());
    }

    public boolean isAllowedToWorkWith(ItemStack s) {
        GTUtility.ItemId id = GTUtility.ItemId.createNoCopy(s);
        return !blacklistedUpgrades.contains(id);
    }

    public int getMaxNumber() {
        return maxnumber;
    }

    public void applyModifiers(GTApiaryModifier mods, ItemStack stack) {
        if (applier != null) applier.accept(mods, stack.stackSize);
    }

    public ItemStack get(int count) {
        return new ItemStack(MetaGeneratedItem03.INSTANCE, count, meta);
    }

    public static boolean isUpgrade(ItemStack s) {
        return OrePrefixes.apiaryUpgrade.contains(s);
    }

    private static final HashMap<Integer, GTApiaryUpgrade> quickLookup = new HashMap<>();

    static {
        EnumSet.allOf(GTApiaryUpgrade.class)
            .forEach(GTApiaryUpgrade::setup_static_variables);
        speed8upgraded.blacklistedUpgrades.add(production.id);
        production.blacklistedUpgrades.add(speed8upgraded.id);
    }
}
