package detrav.items.tools;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;

import detrav.enums.Textures01;
import detrav.items.behaviours.BehaviourDetravToolElectricProspector;
import gregtech.api.GregTechAPI;
import gregtech.api.damagesources.GTDamageSources;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.IToolStats;
import gregtech.api.items.MetaGeneratedTool;

/**
 * Created by wital_000 on 19.03.2016. modified by bartimaeusnek on 05.06.2018
 */
public class DetravToolElectricProspectorBase implements IToolStats {

    public int getToolDamagePerBlockBreak() {
        return 100;
    }

    public int getToolDamagePerDropConversion() {
        return 100;
    }

    public int getToolDamagePerContainerCraft() {
        return 100;
    }

    public int getToolDamagePerEntityAttack() {
        return 2000;
    }

    public int getBaseQuality() {
        return 0;
    }

    public float getBaseDamage() {
        return 1.0F;
    }

    @Override
    public int getHurtResistanceTime(int i, Entity entity) {
        return i;
    }

    public float getSpeedMultiplier() {
        return 1.0F;
    }

    public float getMaxDurabilityMultiplier() {
        return 1.0F;
    }

    @Override
    public DamageSource getDamageSource(EntityLivingBase aPlayer, Entity aEntity) {
        return GTDamageSources.getCombatDamage(
            (aPlayer instanceof EntityPlayer) ? "player" : "mob",
            aPlayer,
            (aEntity instanceof EntityLivingBase) ? getDeathMessage(aPlayer, (EntityLivingBase) aEntity) : null);
    }

    public String getCraftingSound() {
        return null;
    }

    public String getEntityHitSound() {
        return null;
    }

    public String getBreakingSound() {
        return (String) GregTechAPI.sSoundList.get(0);
    }

    @Override
    public Enchantment[] getEnchantments(ItemStack itemStack) {
        return new Enchantment[0];
    }

    @Override
    public int[] getEnchantmentLevels(ItemStack itemStack) {
        return new int[0];
    }

    public String getMiningSound() {
        return null;
    }

    public boolean canBlock() {
        return false;
    }

    public boolean isCrowbar() {
        return false;
    }

    @Override
    public boolean isGrafter() {
        return false;
    }

    @Override
    public boolean isChainsaw() {
        return false;
    }

    @Override
    public boolean isWrench() {
        return false;
    }

    @Override
    public boolean isWeapon() {
        return false;
    }

    @Override
    public boolean isRangedWeapon() {
        return false;
    }

    @Override
    public boolean isMiningTool() {
        return false;
    }

    public boolean isMinableBlock(Block aBlock, byte aMetaData) {

        return false;
    }

    @Override
    public int convertBlockDrops(List<ItemStack> list, ItemStack itemStack, EntityPlayer entityPlayer, Block block,
        int i, int i1, int i2, byte b, int i3, boolean b1, BlockEvent.HarvestDropsEvent harvestDropsEvent) {
        return 0;
    }

    public ItemStack getBrokenItem(ItemStack aStack) {
        return null;
    }

    @Override
    public float getNormalDamageAgainstEntity(float v, Entity entity, ItemStack itemStack, EntityPlayer entityPlayer) {
        return v;
    }

    @Override
    public float getMagicDamageAgainstEntity(float v, Entity entity, ItemStack itemStack, EntityPlayer entityPlayer) {
        return v;
    }

    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
        return Textures01.mTextures[0];
    }

    public short[] getRGBa(boolean aIsToolHead, ItemStack aStack) {
        return aIsToolHead ? MetaGeneratedTool.getPrimaryMaterial(aStack).mRGBa
            : MetaGeneratedTool.getSecondaryMaterial(aStack).mRGBa;
    }

    public void onStatsAddedToTool(MetaGeneratedTool aItem, int aID) {
        aItem.addItemBehavior(aID, new BehaviourDetravToolElectricProspector(getToolDamagePerBlockBreak()));
    }

    public void onToolCrafted(ItemStack aStack, EntityPlayer aPlayer) {

        aPlayer.triggerAchievement(AchievementList.openInventory);
        aPlayer.triggerAchievement(AchievementList.mineWood);
        aPlayer.triggerAchievement(AchievementList.buildWorkBench);
    }

    public IChatComponent getDeathMessage(EntityLivingBase aPlayer, EntityLivingBase aEntity) {
        return new ChatComponentText(
            EnumChatFormatting.RED + aEntity.getCommandSenderName()
                + EnumChatFormatting.WHITE
                + " got Pick Up'ed by "
                + EnumChatFormatting.GREEN
                + aPlayer.getCommandSenderName()
                + EnumChatFormatting.WHITE);
    }

    public float getMiningSpeed(Block aBlock, byte aMetaData, float aDefault, EntityPlayer aPlayer, World aWorld,
        int aX, int aY, int aZ) {
        return aDefault;
    }
}
