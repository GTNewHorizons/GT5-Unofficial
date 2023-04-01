package gregtech.common.tools;

import static gregtech.api.enums.ModIDs.Forestry;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.util.GT_ToolHarvestHelper;
import gregtech.common.items.behaviors.Behaviour_None;
import gregtech.common.items.behaviors.Behaviour_Scoop;

public class GT_Tool_Scoop extends GT_Tool {

    public static Material sBeeHiveMaterial;

    @Override
    public int getToolDamagePerBlockBreak() {
        return 200;
    }

    @Override
    public int getToolDamagePerDropConversion() {
        return 100;
    }

    @Override
    public int getToolDamagePerContainerCraft() {
        return 200;
    }

    @Override
    public int getToolDamagePerEntityAttack() {
        return 200;
    }

    @Override
    public int getBaseQuality() {
        return 0;
    }

    @Override
    public float getBaseDamage() {
        return 1.0F;
    }

    @Override
    public float getSpeedMultiplier() {
        return 1.0F;
    }

    @Override
    public float getMaxDurabilityMultiplier() {
        return 1.0F;
    }

    @Override
    public String getCraftingSound() {
        return null;
    }

    @Override
    public String getEntityHitSound() {
        return null;
    }

    @Override
    public String getMiningSound() {
        return null;
    }

    @Override
    public boolean canBlock() {
        return false;
    }

    @Override
    public boolean isCrowbar() {
        return false;
    }

    @Override
    public boolean isMinableBlock(Block aBlock, byte aMetaData) {
        return GT_ToolHarvestHelper.isAppropriateTool(aBlock, aMetaData, "scoop")
                || GT_ToolHarvestHelper.isAppropriateMaterial(aBlock, sBeeHiveMaterial);
    }

    @Override
    public ItemStack getBrokenItem(ItemStack aStack) {
        return null;
    }

    @Override
    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? Textures.ItemIcons.SCOOP : null;
    }

    @Override
    public short[] getRGBa(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? GT_MetaGenerated_Tool.getPrimaryMaterial(aStack).mRGBa
                : GT_MetaGenerated_Tool.getSecondaryMaterial(aStack).mRGBa;
    }

    @Override
    public void onStatsAddedToTool(GT_MetaGenerated_Tool aItem, int aID) {
        if (Forestry.isModLoaded()) {
            aItem.addItemBehavior(aID, new Behaviour_Scoop(200));
        } else {
            aItem.addItemBehavior(aID, new Behaviour_None());
        }
    }

    @Override
    public IChatComponent getDeathMessage(EntityLivingBase aPlayer, EntityLivingBase aEntity) {
        return new ChatComponentText(
                EnumChatFormatting.RED + aEntity.getCommandSenderName()
                        + EnumChatFormatting.WHITE
                        + " got scooped up by "
                        + EnumChatFormatting.GREEN
                        + aPlayer.getCommandSenderName()
                        + EnumChatFormatting.WHITE);
    }
}
