package com.github.technus.tectech.thing.item;

import static com.github.technus.tectech.Reference.MODID;
import static com.github.technus.tectech.thing.CustomItemList.teslaCover;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;

import com.github.technus.tectech.util.CommonValues;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public final class TeslaCoilCover extends Item {

    public static TeslaCoilCover INSTANCE;
    private static IIcon ultItemIcon;

    private TeslaCoilCover() {
        setHasSubtypes(true);
        setUnlocalizedName("tm.teslaCover");
        setTextureName(MODID + ":itemTeslaCover");
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer ep, List<String> aList, boolean boo) {
        aList.add(CommonValues.THETA_MOVEMENT);
        switch (aStack.getItemDamage()) {
            case 0:
                aList.add(translateToLocal("item.tm.teslaCover.desc.0")); // Tesla-Enables Machines!
                break;
            case 1:
                aList.add(translateToLocal("item.tm.teslaCover.desc.1")); // Tesla-Enables Machines! (BUT LOUDER!!)
                break;
            default:
                aList.add(translateToLocal("item.tm.teslaCover.desc.2")); // Yeet this broken item into some spicy
                                                                          // water!
                break;
        }
        aList.add(EnumChatFormatting.BLUE + translateToLocal("item.tm.teslaCover.desc.3")); // Use on top of a machine
                                                                                            // to enable Tesla
                                                                                            // capabilities
        aList.add(EnumChatFormatting.BLUE + translateToLocal("item.tm.teslaCover.desc.4")); // Who the hell uses cables
                                                                                            // anyway?
    }

    @Override
    public String getUnlocalizedName(ItemStack aStack) {
        return getUnlocalizedName() + "." + getDamage(aStack);
    }

    public static void run() {
        INSTANCE = new TeslaCoilCover();
        GameRegistry.registerItem(INSTANCE, INSTANCE.getUnlocalizedName());
        teslaCover.set(INSTANCE);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon(getIconString());
        ultItemIcon = iconRegister.registerIcon(MODID + ":itemTeslaCoverUltimate");
    }

    @Override
    public IIcon getIconFromDamage(int damage) {
        if (damage == 1) {
            return ultItemIcon;
        }
        return itemIcon;
    }

    @Override
    public void getSubItems(Item aItem, CreativeTabs par2CreativeTabs, List<ItemStack> aList) {
        aList.add(new ItemStack(aItem, 1, 0));
        aList.add(new ItemStack(aItem, 1, 1));
    }
}
