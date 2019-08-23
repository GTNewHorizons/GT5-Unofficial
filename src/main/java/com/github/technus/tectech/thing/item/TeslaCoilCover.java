package com.github.technus.tectech.thing.item;

import com.github.technus.tectech.CommonValues;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.util.GT_LanguageManager;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;

import java.util.List;

import static com.github.technus.tectech.Reference.MODID;
import static com.github.technus.tectech.thing.CustomItemList.teslaCover;


public final class TeslaCoilCover extends Item {
    public static TeslaCoilCover INSTANCE;
    private static IIcon ultItemIcon;

    private TeslaCoilCover() {
        setHasSubtypes(true);
        setUnlocalizedName("tm.teslaCover");
        setTextureName(MODID + ":itemTeslaCover");

        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "Tesla Coil Cover");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".1.name", "Tesla Coil Cover Rich Edition");
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer ep, List aList, boolean boo) {
        aList.add(CommonValues.BASS_MARK);
        switch (aStack.getItemDamage()) {
            case 0:
                aList.add("Tesla-Enables Machines!");
                break;
            case 1:
                aList.add("Tesla-Enables Machines! (BUT LOUDER!!)");
                break;
            default:
                aList.add("Yeet this broken item into some spicy water!");
                break;
        }
        aList.add(EnumChatFormatting.BLUE + "Use on top of a machine to enable Tesla capabilities");
        aList.add(EnumChatFormatting.BLUE + "Who the hell uses cables anyway?");
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
    public void getSubItems(Item aItem, CreativeTabs par2CreativeTabs, List aList) {
        aList.add(new ItemStack(aItem, 1, 0));
        aList.add(new ItemStack(aItem, 1, 1));
    }
}
