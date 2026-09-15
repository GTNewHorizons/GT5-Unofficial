package gregtech.api.util;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.ItemList;

public class GTCreativeTab extends CreativeTabs {

    public GTCreativeTab(String aName, String aLocalName) {
        super("GregTech." + aName);
        GTLanguageManager.addStringLocalization("itemGroup.GregTech." + aName, aLocalName);
    }

    @Override
    public ItemStack getIconItemStack() {
        return ItemList.Tool_Cheat.get(1, new ItemStack(Blocks.iron_block, 1));
    }

    @Override
    public Item getTabIconItem() {
        return ItemList.Circuit_Integrated.getItem();
    }
}
