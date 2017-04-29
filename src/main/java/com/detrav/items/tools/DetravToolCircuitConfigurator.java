package com.detrav.items.tools;



import com.detrav.enums.Textures01;
import com.detrav.items.behaviours.BehaviourDetravConfigurator;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.common.tools.GT_Tool;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.List;

/**
 * Created by Detrav on 29.04.2017.
 */
public class DetravToolCircuitConfigurator extends GT_Tool {
    public float getBaseDamage() {
        return 0.25F;
    }

    public float getSpeedMultiplier() {
        return 0.3F;
    }

    public float getMaxDurabilityMultiplier() {
        return 0.50F;
    }

    public String getCraftingSound() {
        return (String) GregTech_API.sSoundList.get(Integer.valueOf(101));
    }

    public String getEntityHitSound() {
        return (String) GregTech_API.sSoundList.get(Integer.valueOf(101));
    }

    public String getBreakingSound() {
        return (String) GregTech_API.sSoundList.get(Integer.valueOf(0));
    }

    public String getMiningSound() {
        return (String) GregTech_API.sSoundList.get(Integer.valueOf(101));
    }

    public boolean isMinableBlock(Block aBlock, byte aMetaData) {
        return false;
    }

    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? Textures01.mTextures[8] : null;
    }

    public short[] getRGBa(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? GT_MetaGenerated_Tool.getPrimaryMaterial(aStack).mRGBa : GT_MetaGenerated_Tool.getSecondaryMaterial(aStack).mRGBa;
    }

    public void onStatsAddedToTool(GT_MetaGenerated_Tool aItem, int aID) {
        aItem.addItemBehavior(aID, new BehaviourDetravConfigurator());
    }

    public IChatComponent getDeathMessage(EntityLivingBase aPlayer, EntityLivingBase aEntity) {
        return new ChatComponentText(EnumChatFormatting.RED + aEntity.getCommandSenderName() + EnumChatFormatting.WHITE + " got stuck trying to escape through a Pipe while fighting " + EnumChatFormatting.GREEN + aPlayer.getCommandSenderName() + EnumChatFormatting.WHITE);
    }
}
