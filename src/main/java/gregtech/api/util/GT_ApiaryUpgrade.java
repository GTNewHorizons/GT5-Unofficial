package gregtech.api.util;

import cpw.mods.fml.common.Loader;
import gregtech.api.enums.OrePrefixes;
import gregtech.common.items.GT_MetaGenerated_Item_03;
import net.bdew.gendustry.api.ApiaryModifiers;
import net.bdew.gendustry.api.items.IApiaryUpgrade;
import net.minecraft.item.ItemStack;

import java.util.*;

public enum GT_ApiaryUpgrade {
    speed1(32200, 1, 6),
    speed2(32201, 1, 7),
    speed3(32202, 1, 8)
    ;
    private int meta = 0;
    private int maxnumber = 1;
    private int maxspeedmodifier = 0; // formula: maxspeed = modifier

    private GT_Utility.ItemId id;

    private HashMap<GT_Utility.ItemId, ItemStack> additionalGendustryUpgrades = new HashMap<>();
    private HashSet<GT_Utility.ItemId> blacklistedUpgrades = new HashSet<>(); // additionalGendustryUpgrades are blacklisted by default

    GT_ApiaryUpgrade(int meta, int maxnumber, int maxspeedmodifier){
        this.meta = meta;
        this.maxnumber = maxnumber;
        this.maxspeedmodifier = maxspeedmodifier;
        this.id = GT_Utility.ItemId.createNoCopy(get(1));
    }

    public static GT_ApiaryUpgrade getUpgrade(ItemStack s){
        if(s == null)
            return null;
        if(!OrePrefixes.apiaryUpgrade.contains(s))
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

    public int applyMaxSpeedModifier(int maxspeed){
        return Math.max(maxspeed, maxspeedmodifier);
    }

    private static final HashMap<Integer, GT_ApiaryUpgrade> quickLookup = new HashMap<>();

    static{
        if(Loader.isModLoaded("gendustry")) {
            ItemStack s = GT_ModHandler.getModItem("gendustry", "ApiaryUpgrade", 8L, 0);
            GT_Utility.ItemId a = GT_Utility.ItemId.createNoCopy(s);
            speed1.additionalGendustryUpgrades.put(a, s);
            speed2.additionalGendustryUpgrades.put(a, s);
            speed3.additionalGendustryUpgrades.put(a, s);
            speed1.blacklistedUpgrades.addAll(Arrays.asList(speed2.id, speed3.id));
            speed2.blacklistedUpgrades.addAll(Arrays.asList(speed1.id, speed3.id));
            speed3.blacklistedUpgrades.addAll(Arrays.asList(speed1.id, speed2.id));
        }

        EnumSet.allOf(GT_ApiaryUpgrade.class).forEach((u)->quickLookup.put(u.meta, u));
    }
}
