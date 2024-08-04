package com.github.technus.tectech.thing.item;

import static com.github.technus.tectech.Reference.MODID;
import static com.github.technus.tectech.thing.CustomItemList.enderLinkFluidCover;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.github.technus.tectech.util.CommonValues;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public final class EnderFluidLinkCover extends Item {

    public static EnderFluidLinkCover INSTANCE;

    private EnderFluidLinkCover() {
        setHasSubtypes(true);
        setUnlocalizedName("tm.enderfluidlinkcover");
        setTextureName(MODID + ":itemEnderFluidLinkCover");
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer ep, List<String> aList, boolean boo) {
        aList.add(CommonValues.THETA_MOVEMENT);
        aList.add(translateToLocal("item.tm.enderfluidlinkcover.desc.0")); // Ender-Fluid-Enables Machines!
        aList.add(EnumChatFormatting.BLUE + translateToLocal("item.tm.enderfluidlinkcover.desc.1")); // Use on any side
                                                                                                     // of a fluid tank
                                                                                                     // to link it to
        // the Ender
        aList.add(EnumChatFormatting.BLUE + translateToLocal("item.tm.enderfluidlinkcover.desc.2")); // Ender Tanks so
                                                                                                     // are laggy -Bot
                                                                                                     // from the Chads
                                                                                                     // of NH
    }

    public static void run() {
        INSTANCE = new EnderFluidLinkCover();
        GameRegistry.registerItem(INSTANCE, INSTANCE.getUnlocalizedName());
        enderLinkFluidCover.set(INSTANCE);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon(getIconString());
    }
}
