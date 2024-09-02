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
import net.minecraftforge.event.world.BlockEvent;

import gregtech.GTMod;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures.ItemIcons;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.common.items.behaviors.BehaviourNone;
import gregtech.common.tools.GTTool;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtTools;

public class ToolAngleGrinder extends GTTool {

    public static final List<String> mEffectiveList = Arrays
        .asList(new String[] { EntityIronGolem.class.getName(), "EntityTowerGuardian" });

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
    public int getToolDamagePerDropConversion() {
        return 100;
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
    public int getBaseQuality() {
        return 0;
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
        return SoundResource.RANDOM_ANVIL_USE.toString();
    }

    @Override
    public String getEntityHitSound() {
        return SoundResource.RANDOM_ANVIL_BREAK.toString();
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
    public boolean isWrench() {
        return false;
    }

    @Override
    public boolean isCrowbar() {
        return false;
    }

    @Override
    public boolean isWeapon() {
        return true;
    }

    @Override
    public boolean isMinableBlock(final Block aBlock, final byte aMetaData) {
        final String tTool = aBlock.getHarvestTool(aMetaData);
        return (tTool != null) && (tTool.equals("sword") || tTool.equals("file"));
    }

    @Override
    public int convertBlockDrops(final List<ItemStack> aDrops, final ItemStack aStack, final EntityPlayer aPlayer,
        final Block aBlock, final int aX, final int aY, final int aZ, final byte aMetaData, final int aFortune,
        final boolean aSilkTouch, final BlockEvent.HarvestDropsEvent aEvent) {
        return 0;
    }

    @Override
    public ItemStack getBrokenItem(final ItemStack aStack) {
        return null;
    }

    @Override
    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
        return (IIconContainer) (aIsToolHead ? TexturesGtTools.ANGLE_GRINDER : ItemIcons.POWER_UNIT_HV);
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
            GTMod.achievements.issueAchievement(aPlayer, "unitool");
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

    @Override
    public boolean isGrafter() {
        return false;
    }
}
