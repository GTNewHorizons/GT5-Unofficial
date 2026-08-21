package gtPlusPlus.core.item.general;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import gtPlusPlus.core.item.base.BaseItemColourable;
import gtPlusPlus.core.util.Utils;

public class ItemHalfCompleteCasings extends BaseItemColourable {

    public ItemHalfCompleteCasings(String unlocalizedName, CreativeTabs creativeTab, int stackSize, int maxDmg,
        String description, EnumRarity regRarity, EnumChatFormatting colour, boolean Effect, int rgb) {
        super(unlocalizedName, creativeTab, stackSize, maxDmg, description, regRarity, colour, Effect, rgb);
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
    }

    @Override
    public String getItemStackDisplayName(final ItemStack tItem) {
        String prefix = super.getItemStackDisplayName(tItem);
        String casingType = "";
        if (tItem.getItemDamage() == 0) {
            casingType = " I";
        } else if (tItem.getItemDamage() == 1) {
            casingType = " II";
        }
        return (prefix + casingType);
    }

    @Override
    public int getColorFromItemStack(ItemStack stack, int HEX_OxFFFFFF) {
        if (this.getDamage(stack) == 0) {
            return Utils.rgbtoHexValue(52, 52, 52);
        } else {
            return Utils.rgbtoHexValue(80, 90, 222);
        }
    }
}
