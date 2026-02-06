package gregtech.common.tools;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import gregtech.GTMod;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.util.GTToolHarvestHelper;

public class ToolDrillLV extends GTTool {

    @Override
    public int getToolDamagePerBlockBreak() {
        return 50;
    }

    @Override
    public int getToolDamagePerContainerCraft() {
        return 100;
    }

    @Override
    public float getBaseDamage() {
        return 2.0F;
    }

    @Override
    public float getSpeedMultiplier() {
        return 3.0F;
    }

    @Override
    public String getCraftingSound() {
        return SoundResource.IC2_TOOLS_DRILL_DRILL_SOFT.toString();
    }

    @Override
    public String getEntityHitSound() {
        return SoundResource.IC2_TOOLS_DRILL_DRILL_SOFT.toString();
    }

    @Override
    public String getBreakingSound() {
        return SoundResource.IC2_TOOLS_DRILL_DRILL_SOFT.toString();
    }

    @Override
    public String getMiningSound() {
        return SoundResource.IC2_TOOLS_DRILL_DRILL_SOFT.toString();
    }

    @Override
    public boolean isMinableBlock(Block aBlock, int aMetaData) {
        return GTToolHarvestHelper.isAppropriateTool(aBlock, aMetaData, "pickaxe", "shovel")
            || GTToolHarvestHelper.isAppropriateMaterial(
                aBlock,
                Material.rock,
                Material.iron,
                Material.anvil,
                Material.sand,
                Material.grass,
                Material.ground,
                Material.snow,
                Material.clay,
                Material.glass);
    }

    @Override
    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? MetaGeneratedTool.getPrimaryMaterial(aStack).mIconSet.mTextures[OrePrefixes.toolHeadDrill
            .getTextureIndex()] : Textures.ItemIcons.POWER_UNIT_LV;
    }

    @Override
    public short[] getRGBa(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? MetaGeneratedTool.getPrimaryMaterial(aStack).mRGBa : Materials.Steel.mRGBa;
    }

    @Override
    public void onToolCrafted(ItemStack aStack, EntityPlayer aPlayer) {
        super.onToolCrafted(aStack, aPlayer);
        aPlayer.triggerAchievement(AchievementList.buildPickaxe);
        aPlayer.triggerAchievement(AchievementList.buildBetterPickaxe);
        try {
            GTMod.achievements.issueAchievement(aPlayer, "driltime");
            GTMod.achievements.issueAchievement(aPlayer, "buildDrill");
        } catch (Exception ignored) {}
    }

    @Override
    public IChatComponent getDeathMessage(EntityLivingBase aPlayer, EntityLivingBase aEntity) {
        return new ChatComponentText(
            EnumChatFormatting.RED + aEntity.getCommandSenderName()
                + EnumChatFormatting.WHITE
                + " got the Drill! (by "
                + EnumChatFormatting.GREEN
                + aPlayer.getCommandSenderName()
                + EnumChatFormatting.WHITE
                + ")");
    }
}
