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


public final class TeslaCoilCapacitor extends Item {
    public static TeslaCoilCapacitor INSTANCE;
    public static IIcon LVicon, MVicon, HVicon, EVicon, IVicon, LuVicon, ZPMicon, UVicon;

    public TeslaCoilCapacitor() {
        setUnlocalizedName("tm.teslaCoilCapacitor");
        setTextureName(MODID + ":itemParametrizerMemoryCardUnlocked");

        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "LV Tesla Capacitor");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".1.name", "MV Tesla Capacitor");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".2.name", "HV Tesla Capacitor");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".3.name", "EV Tesla Capacitor");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".4.name", "IV Tesla Capacitor");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".5.name", "LuV Tesla Capacitor");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".6.name", "ZPM Tesla Capacitor");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".7.name", "UV Tesla Capacitor");
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer ep, List aList, boolean boo) {
        aList.add(CommonValues.BASS_MARK);
        switch (aStack.getItemDamage()) {
            case 0://"LV"
                aList.add("Stores energy for tesla towers! (LV)");
                break;
            case 1://"MV"
                aList.add("Stores energy for tesla towers! (MV)");
                break;
            case 2://"HV"
                aList.add("Stores energy for tesla towers! (HV)");
                break;
            case 3://"EV"
                aList.add("Stores energy for tesla towers! (EV)");
                break;
            case 4://"IV"
                aList.add("Stores energy for tesla towers! (IV)");
                break;
            case 5://"LuV"
                aList.add("Stores energy for tesla towers! (LuV)");
                break;
            case 6://"ZPM"
                aList.add("Stores energy for tesla towers! (ZPM)");
                break;
            case 7://"UV"
                aList.add("Stores energy for tesla towers! (UV)");
                break;
            default://
                aList.add("Yeet this broken item into some spicy water!");
                break;
        }
        aList.add(EnumChatFormatting.BLUE + "Insert into a Capacitor hatch of a Tesla Tower");
        aList.add(EnumChatFormatting.BLUE + "Capacitors are the same thing as batteries, right?");
    }

    @Override
    public String getUnlocalizedName(ItemStack aStack) {
        return getUnlocalizedName() + "." + getDamage(aStack);
    }

    public static void run() {
        INSTANCE = new TeslaCoilCapacitor();
        GameRegistry.registerItem(INSTANCE, INSTANCE.getUnlocalizedName());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        LVicon = itemIcon = iconRegister.registerIcon(getIconString());
        MVicon = iconRegister.registerIcon(MODID + ":itemParametrizerMemoryCardLocked");
        HVicon = iconRegister.registerIcon(MODID + ":itemParametrizerMemoryCardLocked");
        EVicon = iconRegister.registerIcon(MODID + ":itemParametrizerMemoryCardLocked");
        IVicon = iconRegister.registerIcon(MODID + ":itemParametrizerMemoryCardLocked");
        LuVicon = iconRegister.registerIcon(MODID + ":itemParametrizerMemoryCardLocked");
        ZPMicon = iconRegister.registerIcon(MODID + ":itemParametrizerMemoryCardLocked");
        UVicon = iconRegister.registerIcon(MODID + ":itemParametrizerMemoryCardLocked");
    }

    @Override
    public IIcon getIconFromDamage(int damage) {
        switch (damage) {
            case 1:
                return MVicon;
            case 2:
                return HVicon;
            case 3:
                return EVicon;
            case 4:
                return IVicon;
            case 5:
                return LuVicon;
            case 6:
                return ZPMicon;
            case 7:
                return UVicon;
            default:
                return LVicon;
        }
    }

    public void getSubItems(Item aItem, CreativeTabs par2CreativeTabs, List aList) {
        for (int i = 0; i <= 7; i++) {
            aList.add(new ItemStack(aItem, 1, i));
        }
    }
}
