package com.github.technus.tectech.thing.item;

import com.github.technus.tectech.CommonValues;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

import static com.github.technus.tectech.Reference.MODID;


public final class TeslaCoilCover extends Item {
    public static TeslaCoilCover INSTANCE;

    public TeslaCoilCover() {
        setUnlocalizedName("tm.teslaCoilCover");
        setTextureName(MODID + ":itemTeslaCover");
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer ep, List aList, boolean boo) {
        aList.add(CommonValues.BASS_MARK);
        aList.add("Tesla-Enables Machines!");
        aList.add(EnumChatFormatting.BLUE + "Use on top of a machine to enable Tesla capabilities");
        aList.add(EnumChatFormatting.BLUE + "Who the hell uses cables anyway?");
    }

    public static void run() {
        INSTANCE = new TeslaCoilCover();
        GameRegistry.registerItem(INSTANCE, INSTANCE.getUnlocalizedName());
    }
}
