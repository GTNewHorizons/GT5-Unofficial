package gregtech.common.tools;

import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.IToolStats;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import gregtech.common.items.behaviors.Behaviour_Crowbar;

public class GT_Tool_Crowbar extends GT_Tool {

    @Override
    public int getToolDamagePerBlockBreak() {
        return 50;
    }

    @Override
    public int getToolDamagePerDropConversion() {
        return 100;
    }

    @Override
    public int getToolDamagePerContainerCraft() {
        return 100;
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
        return 2.0F;
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
        return SoundResource.RANDOM_BREAK.toString();
    }

    @Override
    public String getEntityHitSound() {
        return SoundResource.RANDOM_BREAK.toString();
    }

    @Override
    public String getMiningSound() {
        return SoundResource.RANDOM_BREAK.toString();
    }

    @Override
    public boolean canBlock() {
        return true;
    }

    @Override
    public boolean isCrowbar() {
        return true;
    }

    @Override
    public boolean isWeapon() {
        return true;
    }

    @Override
    public boolean isMinableBlock(Block aBlock, byte aMetaData) {
        if (aBlock.getMaterial() == Material.circuits) {
            return true;
        }
        String tTool = aBlock.getHarvestTool(aMetaData);
        if ((tTool == null) || (tTool.equals(""))) {
            for (Iterator<IToolStats> i$ = GT_MetaGenerated_Tool_01.INSTANCE.mToolStats.values()
                                                                                       .iterator(); i$.hasNext(); i$.next()) {
                if (((i$ instanceof GT_Tool_Crowbar)) && (!((IToolStats) i$).isMinableBlock(aBlock, aMetaData))) {
                    return false;
                }
            }
            return true;
        }
        return tTool.equals("crowbar");
    }

    @Override
    public ItemStack getBrokenItem(ItemStack aStack) {
        return null;
    }

    @Override
    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? Textures.ItemIcons.CROWBAR : null;
    }

    @Override
    public short[] getRGBa(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? GT_MetaGenerated_Tool.getPrimaryMaterial(aStack).mRGBa : null;
    }

    @Override
    public void onStatsAddedToTool(GT_MetaGenerated_Tool aItem, int aID) {
        aItem.addItemBehavior(aID, new Behaviour_Crowbar(1, 1000));
    }

    @Override
    public IChatComponent getDeathMessage(EntityLivingBase aPlayer, EntityLivingBase aEntity) {
        return new ChatComponentText(
                EnumChatFormatting.RED + aEntity.getCommandSenderName()
                        + EnumChatFormatting.WHITE
                        + " was removed by "
                        + EnumChatFormatting.GREEN
                        + aPlayer.getCommandSenderName()
                        + EnumChatFormatting.WHITE);
    }
}
