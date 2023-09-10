package kekztech;

import java.util.Arrays;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import common.items.MetaItem_CraftingComponent;

import util.Util;

public enum Items {

    // Ceramics
    YSZCeramicDust(9, 1),
    GDCCeramicDust(10, 1),
    YttriaDust(11, 1),
    ZirconiaDust(12, 1),
    CeriaDust(13, 1),
    YSZCeramicPlate(14, 1),
    GDCCeramicPlate(15, 1),
    // Error Item
    Error(0, 1);

    static {
        YttriaDust.setOreDictName("dustYttriumOxide");
        ZirconiaDust.setOreDictName("dustCubicZirconia");
    }

    private final int metaID;
    private final int identifier;

    Items(int metaID, int identifier) {
        this.metaID = metaID;
        this.identifier = identifier;
    }

    public int getMetaID() {
        return metaID;
    }

    String OreDictName;

    private void registerOreDict() {
        OreDictionary.registerOre(getOreDictName(), getNonOreDictedItemStack(1));
    }

    public static void registerOreDictNames() {
        Arrays.stream(Items.values()).filter(e -> e.getOreDictName() != null).forEach(Items::registerOreDict);
    }

    public ItemStack getNonOreDictedItemStack(int amount) {
        return new ItemStack(MetaItem_CraftingComponent.getInstance(), amount, this.getMetaID());
    }

    public ItemStack getOreDictedItemStack(int amount) {
        return this.getOreDictName() != null ? Util.getStackofAmountFromOreDict(this.getOreDictName(), amount)
                : new ItemStack(MetaItem_CraftingComponent.getInstance(), amount, this.getMetaID());
    }

    public String getOreDictName() {
        return OreDictName;
    }

    public void setOreDictName(String oreDictName) {
        OreDictName = oreDictName;
    }
}
