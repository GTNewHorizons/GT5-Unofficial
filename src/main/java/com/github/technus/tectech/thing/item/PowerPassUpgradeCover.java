package com.github.technus.tectech.thing.item;

import static com.github.technus.tectech.Reference.MODID;
import static com.github.technus.tectech.thing.CustomItemList.powerPassUpgradeCover;
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

public final class PowerPassUpgradeCover extends Item {

    public static PowerPassUpgradeCover INSTANCE;

    private PowerPassUpgradeCover() {
        setHasSubtypes(true);
        setUnlocalizedName("tm.powerpassupgradecover");
        setTextureName(MODID + ":itemPowerPassUpgradeCover");
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer ep, List<String> aList, boolean boo) {
        aList.add(CommonValues.THETA_MOVEMENT);
        aList.add(translateToLocal("item.tm.powerpassupgradecover.desc.0")); // Add power pass functionality to TecTech
                                                                             // Multiblocks
        aList.add(EnumChatFormatting.BLUE + translateToLocal("item.tm.powerpassupgradecover.desc.1")); // Active
                                                                                                       // transformer in
                                                                                                       // a can??
        aList.add(EnumChatFormatting.BLUE + translateToLocal("item.tm.powerpassupgradecover.desc.2")); // Chain them up
                                                                                                       // like Christmas
                                                                                                       // lights!
    }

    public static void run() {
        INSTANCE = new PowerPassUpgradeCover();
        GameRegistry.registerItem(INSTANCE, INSTANCE.getUnlocalizedName());
        powerPassUpgradeCover.set(INSTANCE);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon(getIconString());
    }
}
