package gregtech.api.util;

import gregtech.api.enums.OrePrefixes;
import gregtech.common.items.GT_MetaGenerated_Item_03;
import java.util.*;
import java.util.function.Consumer;
import net.bdew.gendustry.api.ApiaryModifiers;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeGenBase;

public enum GT_ApiaryUpgrade {
    speed1(UNIQUE_INDEX.SPEED_UPGRADE, 32200, 1, 1),
    speed2(UNIQUE_INDEX.SPEED_UPGRADE, 32201, 1, 2),
    speed3(UNIQUE_INDEX.SPEED_UPGRADE, 32202, 1, 3),
    speed4(UNIQUE_INDEX.SPEED_UPGRADE, 32203, 1, 4),
    speed5(UNIQUE_INDEX.SPEED_UPGRADE, 32204, 1, 5),
    speed6(UNIQUE_INDEX.SPEED_UPGRADE, 32205, 1, 6),
    speed7(UNIQUE_INDEX.SPEED_UPGRADE, 32206, 1, 7),
    speed8(UNIQUE_INDEX.SPEED_UPGRADE, 32207, 1, 8),
    speed8upgraded(UNIQUE_INDEX.SPEED_UPGRADE, 32208, 1, 8, (mods) -> {
        mods.production = 2f;
        mods.energy *= 14.75;
    }),
    production(UNIQUE_INDEX.PRODUCTION_UPGRADE, 32209, 8, (mods) -> {
        mods.production += 0.25f;
        mods.energy *= 1.4f;
    }),
    plains(UNIQUE_INDEX.PLAINS_UPGRADE, 32210, 1, (mods) -> {
        mods.biomeOverride = BiomeGenBase.plains;
        mods.energy *= 1.2f;
    }),
    light(UNIQUE_INDEX.LIGHT_UPGRADE, 32211, 1, (mods) -> {
        mods.isSelfLighted = true;
        mods.energy *= 1.05f;
    }),
    flowering(UNIQUE_INDEX.FLOWERING_UPGRADE, 32212, 8, (mods) -> {
        mods.flowering *= 1.2f;
        mods.energy *= 1.1f;
    }),
    winter(UNIQUE_INDEX.WINTER_UPGRADE, 32213, 1, (mods) -> {
        mods.biomeOverride = BiomeGenBase.taiga;
        mods.energy *= 1.5f;
    }),
    dryer(UNIQUE_INDEX.DRYER_UPGRADE, 32214, 8, (mods) -> {
        mods.humidity -= 0.25f;
        mods.energy *= 1.05f;
    }),
    automation(UNIQUE_INDEX.AUTOMATION_UPGRADE, 32215, 1, (mods) -> {
        mods.isAutomated = true;
        mods.energy *= 1.1f;
    }),
    humidifier(UNIQUE_INDEX.HUMIDIFIER_UPGRADE, 32216, 8, (mods) -> {
        mods.humidity += 0.25f;
        mods.energy *= 1.1f;
    }),
    hell(UNIQUE_INDEX.HELL_UPGRADE, 32217, 1, (mods) -> {
        mods.biomeOverride = BiomeGenBase.hell;
        mods.energy *= 1.5f;
    }),
    pollen(UNIQUE_INDEX.POLLEN_UPGRADE, 32218, 1, (mods) -> {
        mods.flowering = 0f;
        mods.energy *= 1.3f;
    }),
    desert(UNIQUE_INDEX.DESERT_UPGRADE, 32219, 1, (mods) -> {
        mods.biomeOverride = BiomeGenBase.desert;
        mods.energy *= 1.2f;
    }),
    cooler(UNIQUE_INDEX.COOLER_UPGRADE, 32220, 8, (mods) -> {
        mods.temperature -= 0.25f;
        mods.energy *= 1.05f;
    }),
    lifespan(UNIQUE_INDEX.LIFESPAN_UPGRADE, 32221, 4, (mods) -> {
        mods.lifespan *= 0.67f;
        mods.energy *= 1.05f;
    }),
    seal(UNIQUE_INDEX.SEAL_UPGRADE, 32222, 1, (mods) -> {
        mods.isSealed = true;
        mods.energy *= 1.05f;
    }),
    stabilizer(UNIQUE_INDEX.STABILIZER_UPGRADE, 32223, 1, (mods) -> {
        mods.geneticDecay = 0f;
        mods.energy *= 2.50f;
    }),
    jungle(UNIQUE_INDEX.JUNGLE_UPGRADE, 32224, 1, (mods) -> {
        mods.biomeOverride = BiomeGenBase.jungle;
        mods.energy *= 1.20f;
    }),
    territory(UNIQUE_INDEX.TERRITORY_UPGRADE, 32225, 4, (mods) -> {
        mods.territory *= 1.5f;
        mods.energy *= 1.05f;
    }),
    ocean(UNIQUE_INDEX.OCEAN_UPGRADE, 32226, 1, (mods) -> {
        mods.biomeOverride = BiomeGenBase.ocean;
        mods.energy *= 1.20f;
    }),
    sky(UNIQUE_INDEX.SKY_UPGRADE, 32227, 1, (mods) -> {
        mods.isSunlightSimulated = true;
        mods.energy *= 1.05f;
    }),
    heater(UNIQUE_INDEX.HEATER_UPGRADE, 32228, 8, (mods) -> {
        mods.temperature += 0.25f;
        mods.energy *= 1.05f;
    }),
    sieve(UNIQUE_INDEX.SIEVE_UPGRADE, 32229, 1, (mods) -> {
        mods.isCollectingPollen = true;
        mods.energy *= 1.05f;
    }),
    ;

    private enum UNIQUE_INDEX {
        SPEED_UPGRADE,
        PRODUCTION_UPGRADE,
        PLAINS_UPGRADE,
        LIGHT_UPGRADE,
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
        SIEVE_UPGRADE,
        ;

        void apply(Consumer<GT_ApiaryUpgrade> fn) {
            UNIQUE_UPGRADE_LIST.get(this).forEach(fn);
        }
    }

    private static final EnumMap<UNIQUE_INDEX, ArrayList<GT_ApiaryUpgrade>> UNIQUE_UPGRADE_LIST =
            new EnumMap<>(UNIQUE_INDEX.class);

    private int meta = 0;
    private int maxnumber = 1;
    private int maxspeedmodifier = 0; // formula: maxspeed = modifier

    private final GT_Utility.ItemId id;
    private final UNIQUE_INDEX unique_index;
    private final Consumer<ApiaryModifiers> applier;

    private final HashSet<GT_Utility.ItemId> blacklistedUpgrades =
            new HashSet<>(); // additionalGendustryUpgrades are blacklisted by default

    GT_ApiaryUpgrade(UNIQUE_INDEX unique_index, int meta, int maxnumber, Consumer<ApiaryModifiers> applier) {
        this.unique_index = unique_index;
        this.meta = meta;
        this.maxnumber = maxnumber;
        this.maxspeedmodifier = 0;
        this.applier = applier;
        this.id = GT_Utility.ItemId.createNoCopy(get(1));
    }

    GT_ApiaryUpgrade(UNIQUE_INDEX unique_index, int meta, int maxnumber, int maxspeedmodifier) {
        this.unique_index = unique_index;
        this.meta = meta;
        this.maxnumber = maxnumber;
        this.maxspeedmodifier = maxspeedmodifier;
        applier = null;
        this.id = GT_Utility.ItemId.createNoCopy(get(1));
    }

    GT_ApiaryUpgrade(
            UNIQUE_INDEX unique_index,
            int meta,
            int maxnumber,
            int maxspeedmodifier,
            Consumer<ApiaryModifiers> applier) {
        this.unique_index = unique_index;
        this.meta = meta;
        this.maxnumber = maxnumber;
        this.maxspeedmodifier = maxspeedmodifier;
        this.applier = applier;
        this.id = GT_Utility.ItemId.createNoCopy(get(1));
    }

    private void setup_static_variables() {
        quickLookup.put(this.meta, this);
        ArrayList<GT_ApiaryUpgrade> un = UNIQUE_UPGRADE_LIST.get(this.unique_index);
        if (un != null)
            un.forEach((u) -> {
                u.blacklistedUpgrades.add(this.id);
                this.blacklistedUpgrades.add(u.id);
            });
        else {
            un = new ArrayList<>(1);
            UNIQUE_UPGRADE_LIST.put(this.unique_index, un);
        }
        un.add(this);
    }

    public static GT_ApiaryUpgrade getUpgrade(ItemStack s) {
        if (s == null) return null;
        if (!isUpgrade(s)) return null;
        return quickLookup.get(s.getItemDamage());
    }

    public boolean isAllowedToWorkWith(ItemStack s) {
        GT_Utility.ItemId id = GT_Utility.ItemId.createNoCopy(s);
        return !blacklistedUpgrades.contains(id);
    }

    public int getMaxNumber() {
        return maxnumber;
    }

    public void applyModifiers(ApiaryModifiers mods, ItemStack s) {
        if (applier != null) applier.accept(mods);
    }

    public ItemStack get(int count) {
        return new ItemStack(GT_MetaGenerated_Item_03.INSTANCE, count, meta);
    }

    public static boolean isUpgrade(ItemStack s) {
        return OrePrefixes.apiaryUpgrade.contains(s);
    }

    public int applyMaxSpeedModifier(int maxspeed) {
        return Math.max(maxspeed, maxspeedmodifier);
    }

    private static final HashMap<Integer, GT_ApiaryUpgrade> quickLookup = new HashMap<>();

    static {
        EnumSet.allOf(GT_ApiaryUpgrade.class).forEach(GT_ApiaryUpgrade::setup_static_variables);
        speed8upgraded.blacklistedUpgrades.add(production.id);
    }
}
