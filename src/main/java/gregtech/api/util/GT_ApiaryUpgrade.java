package gregtech.api.util;

import cpw.mods.fml.common.Loader;
import gregtech.api.enums.OrePrefixes;
import gregtech.common.items.GT_MetaGenerated_Item_03;
import net.bdew.gendustry.api.ApiaryModifiers;
import net.bdew.gendustry.api.items.IApiaryUpgrade;
import net.minecraft.item.ItemStack;

import java.util.*;
import java.util.function.Consumer;

public enum GT_ApiaryUpgrade {
    speed1(UNIQUE_INDEX.SPEED_UPGRADE, 32200, 1, 1),
    speed2(UNIQUE_INDEX.SPEED_UPGRADE, 32201, 1, 2),
    speed3(UNIQUE_INDEX.SPEED_UPGRADE, 32202, 1, 3),
    speed4(UNIQUE_INDEX.SPEED_UPGRADE, 32203, 1, 4),
    speed5(UNIQUE_INDEX.SPEED_UPGRADE, 32204, 1, 5),
    speed6(UNIQUE_INDEX.SPEED_UPGRADE, 32205, 1, 6),
    speed7(UNIQUE_INDEX.SPEED_UPGRADE, 32206, 1, 7),
    speed8(UNIQUE_INDEX.SPEED_UPGRADE, 32207, 1, 8),
    ;

    private enum UNIQUE_INDEX{
        SPEED_UPGRADE,
        ;
        void apply(Consumer<GT_ApiaryUpgrade> fn){
            UNIQUE_UPGRADE_LIST.get(this).forEach(fn);
        }
    }

    private static final EnumMap<UNIQUE_INDEX, ArrayList<GT_ApiaryUpgrade>> UNIQUE_UPGRADE_LIST = new EnumMap<>(UNIQUE_INDEX.class);

    private int meta = 0;
    private int maxnumber = 1;
    private int maxspeedmodifier = 0; // formula: maxspeed = modifier

    private final GT_Utility.ItemId id;
    private final UNIQUE_INDEX unique_index;

    private final HashMap<GT_Utility.ItemId, ItemStack> additionalGendustryUpgrades = new HashMap<>();
    private final HashSet<GT_Utility.ItemId> blacklistedUpgrades = new HashSet<>(); // additionalGendustryUpgrades are blacklisted by default

    GT_ApiaryUpgrade(UNIQUE_INDEX unique_index, int meta, int maxnumber, int maxspeedmodifier){
        this.unique_index = unique_index;
        this.meta = meta;
        this.maxnumber = maxnumber;
        this.maxspeedmodifier = maxspeedmodifier;
        this.id = GT_Utility.ItemId.createNoCopy(get(1));
    }

    private void setup_static_variables(){
        quickLookup.put(this.meta, this);
        ArrayList<GT_ApiaryUpgrade> un = UNIQUE_UPGRADE_LIST.get(this.unique_index);
        if(un != null)
            un.forEach((u) -> { u.blacklistedUpgrades.add(this.id); this.blacklistedUpgrades.add(u.id); });
        else {
            un = new ArrayList<>(1);
            UNIQUE_UPGRADE_LIST.put(this.unique_index, un);
        }
        un.add(this);
    }

    public static GT_ApiaryUpgrade getUpgrade(ItemStack s){
        if(s == null)
            return null;
        if(!isUpgrade(s))
            return null;
        return quickLookup.get(s.getItemDamage());
    }

    public boolean isAllowedToWorkWith(ItemStack s){
        GT_Utility.ItemId id = GT_Utility.ItemId.createNoCopy(s);
        return !additionalGendustryUpgrades.containsKey(id) && !blacklistedUpgrades.contains(id);
    }

    public int getMaxNumber(){
        return maxnumber;
    }

    public void applyModifiers(ApiaryModifiers mods, ItemStack s){
        additionalGendustryUpgrades.forEach((k, u) -> ((IApiaryUpgrade)u.getItem()).applyModifiers(mods, u));
    }

    public ItemStack get(int count){
        return new ItemStack(GT_MetaGenerated_Item_03.INSTANCE, count, meta);
    }

    public static boolean isUpgrade(ItemStack s){
        return OrePrefixes.apiaryUpgrade.contains(s);
    }

    public int applyMaxSpeedModifier(int maxspeed){
        return Math.max(maxspeed, maxspeedmodifier);
    }

    private static final HashMap<Integer, GT_ApiaryUpgrade> quickLookup = new HashMap<>();

    static{
        EnumSet.allOf(GT_ApiaryUpgrade.class).forEach(GT_ApiaryUpgrade::setup_static_variables);

        if(Loader.isModLoaded("gendustry")) {
            ItemStack s = GT_ModHandler.getModItem("gendustry", "ApiaryUpgrade", 8L, 0);
            GT_Utility.ItemId a = GT_Utility.ItemId.createNoCopy(s);
            UNIQUE_INDEX.SPEED_UPGRADE.apply((u) -> u.additionalGendustryUpgrades.put(a, s));
        }
    }
}
