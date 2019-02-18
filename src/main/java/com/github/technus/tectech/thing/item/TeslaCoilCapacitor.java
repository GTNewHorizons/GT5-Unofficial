package com.github.technus.tectech.thing.item;

import com.github.technus.tectech.CommonValues;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

import static com.github.technus.tectech.CommonValues.VN;
import static com.github.technus.tectech.Reference.MODID;


public final class TeslaCoilCapacitor extends Item {
    public static TeslaCoilCapacitor INSTANCE;

    public TeslaCoilCapacitor(int capTier) {
        setUnlocalizedName("tm.teslaCoilCapacitor_" + capTier);
        setTextureName(MODID + ":itemParametrizerMemoryCardUnlocked");
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer ep, List aList, boolean boo) {
        aList.add(CommonValues.BASS_MARK);
        aList.add("I IDENTIFY AS A: " + VN[Integer.parseInt(getUnlocalizedName(aStack).replaceAll("[^0-9]", ""))]);
        aList.add("Stores energy for tesla towers!");
        aList.add(EnumChatFormatting.BLUE + "Insert into a Capacitor hatch of a Tesla Tower");
        aList.add(EnumChatFormatting.BLUE + "Capacitors are the same thing as batteries, right?");
    }

    public static void run(int capTier) {
        INSTANCE = new TeslaCoilCapacitor(capTier);
        GameRegistry.registerItem(INSTANCE, INSTANCE.getUnlocalizedName());
    }
}
