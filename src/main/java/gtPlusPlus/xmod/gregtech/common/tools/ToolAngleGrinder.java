package gtPlusPlus.xmod.gregtech.common.tools;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import gregtech.GTMod;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.TextureSet;
import gregtech.api.enums.Textures.ItemIcons;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.common.items.behaviors.BehaviourNone;
import gregtech.common.tools.GTTool;

public class ToolAngleGrinder extends GTTool {

    public static final List<String> mEffectiveList = Arrays
        .asList(EntityIronGolem.class.getName(), "EntityTowerGuardian");

    @Override
    public float getNormalDamageAgainstEntity(final float aOriginalDamage, final Entity aEntity, final ItemStack aStack,
        final EntityPlayer aPlayer) {
        String tName = aEntity.getClass()
            .getName();
        tName = tName.substring(tName.lastIndexOf(".") + 1);
        return (mEffectiveList.contains(tName)) || (tName.contains("Golem")) ? aOriginalDamage * 2.0F : aOriginalDamage;
    }

    @Override
    public int getToolDamagePerBlockBreak() {
        return 50;
    }

    @Override
    public int getToolDamagePerContainerCraft() {
        return 400;
    }

    @Override
    public int getToolDamagePerEntityAttack() {
        return 100;
    }

    @Override
    public float getBaseDamage() {
        return 8.0F;
    }

    @Override
    public float getSpeedMultiplier() {
        return 2F;
    }

    @Override
    public float getMaxDurabilityMultiplier() {
        return 1.8F;
    }

    @Override
    public String getCraftingSound() {
        return SoundResource.GTCEU_OP_FILE.toString();
    }

    @Override
    public String getEntityHitSound() {
        return SoundResource.GTCEU_OP_FILE.toString();
    }

    @Override
    public boolean isWeapon() {
        return true;
    }

    @Override
    public boolean isMinableBlock(final Block aBlock, final int aMetaData) {
        final String tTool = aBlock.getHarvestTool(aMetaData);
        return (tTool != null) && (tTool.equals("sword") || tTool.equals("file"));
    }

    @Override
    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead
            ? MetaGeneratedTool.getPrimaryMaterial(aStack).mIconSet.mTextures[TextureSet.INDEX_angleGrinder]
            : ItemIcons.POWER_UNIT_HV;
    }

    @Override
    public short[] getRGBa(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? MetaGeneratedTool.getPrimaryMaterial(aStack).mRGBa : Materials.TungstenSteel.mRGBa;
    }

    @Override
    public void onToolCrafted(final ItemStack aStack, final EntityPlayer aPlayer) {
        super.onToolCrafted(aStack, aPlayer);
        aPlayer.triggerAchievement(AchievementList.buildSword);
        try {
            GTMod.achievements.issueAchievement(aPlayer, "tools");
        } catch (final Exception e) {}
    }

    @Override
    public IChatComponent getDeathMessage(final EntityLivingBase aPlayer, final EntityLivingBase aEntity) {
        return new ChatComponentText(
            EnumChatFormatting.RED + aEntity.getCommandSenderName()
                + EnumChatFormatting.WHITE
                + " has been Ground out of existence by "
                + EnumChatFormatting.GREEN
                + aPlayer.getCommandSenderName()
                + EnumChatFormatting.WHITE);
    }

    @Override
    public void onStatsAddedToTool(final MetaGeneratedTool aItem, final int aID) {
        aItem.addItemBehavior(aID, new BehaviourNone());
    }

}
