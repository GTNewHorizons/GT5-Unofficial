package gregtech.api.util;

import cpw.mods.fml.common.Loader;
import gregtech.api.enums.OrePrefixes;
import net.bdew.gendustry.api.ApiaryModifiers;
import net.bdew.gendustry.api.items.IApiaryUpgrade;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;

public enum GT_ApiaryUpgrade {
    speed1(32200, 1, 6),
    speed2(32201, 1, 7),
    speed3(32202, 1, 8)
    ;
    private int meta = 0;
    private int maxnumber = 1;
    private int maxspeedmodifier = 0; // formula: maxspeed = modifier

    private ArrayList<ItemStack> additionalGendustryUpgrades = new ArrayList<>();

    GT_ApiaryUpgrade(int meta, int maxnumber, int maxspeedmodifier){
        this.meta = meta;
        this.maxnumber = maxnumber;
        this.maxspeedmodifier = maxspeedmodifier;
    }

    public static GT_ApiaryUpgrade getUpgrade(ItemStack s){
        if(s == null)
            return null;
        if(!OrePrefixes.apiaryUpgrade.contains(s))
            return null;
        return quickLookup.get(s.getItemDamage());
    }

    public boolean isAllowedToWorkWith(ItemStack s){
        for(ItemStack upgrade : additionalGendustryUpgrades){
            if(GT_Utility.areStacksEqual(upgrade, s))
                return false;
        }
        return true;
    }

    public int getMaxNumber(){
        return maxnumber;
    }

    public void applyModifiers(ApiaryModifiers mods, ItemStack s){
        additionalGendustryUpgrades.forEach((u) -> ((IApiaryUpgrade)u.getItem()).applyModifiers(mods, u));
    }

    public int applyMaxSpeedModifier(int maxspeed){
        return Math.max(maxspeed, maxspeedmodifier);
    }

    private static final HashMap<Integer, GT_ApiaryUpgrade> quickLookup = new HashMap<>();

    static{
        if(Loader.isModLoaded("gendustry")) {
            ItemStack a = GT_ModHandler.getModItem("gendustry", "ApiaryUpgrade", 8L, 0);
            speed1.additionalGendustryUpgrades.add(a);
            speed2.additionalGendustryUpgrades.add(a);
            speed3.additionalGendustryUpgrades.add(a);
        }

        EnumSet.allOf(GT_ApiaryUpgrade.class).forEach((u)->quickLookup.put(u.meta, u));
    }
}
