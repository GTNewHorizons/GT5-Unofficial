package gtPlusPlus.xmod.gregtech.common.tools;

import gregtech.api.GregTech_API;
import gregtech.api.damagesources.GT_DamageSources;
import gtPlusPlus.xmod.gregtech.api.interfaces.internal.Interface_ToolStats;
import gtPlusPlus.xmod.gregtech.api.items.Gregtech_MetaTool;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.event.world.BlockEvent;

public abstract class TOOL_Gregtech_Base
implements Interface_ToolStats {
	public static final Enchantment[] FORTUNE_ENCHANTMENT = {Enchantment.fortune};
	public static final Enchantment[] LOOTING_ENCHANTMENT = {Enchantment.looting};
	public static final Enchantment[] ZERO_ENCHANTMENTS = new Enchantment[0];
	public static final int[] ZERO_ENCHANTMENT_LEVELS = new int[0];

	@Override
	public int getToolDamagePerBlockBreak() {
		return 100;
	}

	@Override
	public int getToolDamagePerDropConversion() {
		return 100;
	}

	@Override
	public int getToolDamagePerContainerCraft() {
		return 800;
	}

	@Override
	public int getToolDamagePerEntityAttack() {
		return 200;
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
	public int getHurtResistanceTime(int aOriginalHurtResistance, Entity aEntity) {
		return aOriginalHurtResistance;
	}

	@Override
	public String getMiningSound() {
		return null;
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
	public String getBreakingSound() {
		return (String) GregTech_API.sSoundList.get(Integer.valueOf(0));
	}

	@Override
	public int getBaseQuality() {
		return 0;
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
		return true;
	}

	@Override
	public DamageSource getDamageSource(EntityLivingBase aPlayer, Entity aEntity) {
		return GT_DamageSources.getCombatDamage((aPlayer instanceof EntityPlayer) ? "player" : "mob", aPlayer, (aEntity instanceof EntityLivingBase) ? getDeathMessage(aPlayer, (EntityLivingBase) aEntity) : null);
	}

	public IChatComponent getDeathMessage(EntityLivingBase aPlayer, EntityLivingBase aEntity) {
		return new EntityDamageSource((aPlayer instanceof EntityPlayer) ? "player" : "mob", aPlayer).func_151519_b(aEntity);
	}

	@Override
	public int convertBlockDrops(List<ItemStack> aDrops, ItemStack aStack, EntityPlayer aPlayer, Block aBlock, int aX, int aY, int aZ, byte aMetaData, int aFortune, boolean aSilkTouch, BlockEvent.HarvestDropsEvent aEvent) {
		return 0;
	}

	@Override
	public ItemStack getBrokenItem(ItemStack aStack) {
		return null;
	}

	@Override
	public Enchantment[] getEnchantments(ItemStack aStack) {
		return ZERO_ENCHANTMENTS;
	}

	@Override
	public int[] getEnchantmentLevels(ItemStack aStack) {
		return ZERO_ENCHANTMENT_LEVELS;
	}

	@Override
	public void onToolCrafted(ItemStack aStack, EntityPlayer aPlayer) {
		aPlayer.triggerAchievement(AchievementList.openInventory);
		aPlayer.triggerAchievement(AchievementList.mineWood);
		aPlayer.triggerAchievement(AchievementList.buildWorkBench);
	}
	
	@Override
	public void onStatsAddedToTool(Gregtech_MetaTool aItem, int aID) {
	}

	@Override
	public float getNormalDamageAgainstEntity(float aOriginalDamage, Entity aEntity, ItemStack aStack, EntityPlayer aPlayer) {
		return aOriginalDamage;
	}

	@Override
	public float getMagicDamageAgainstEntity(float aOriginalDamage, Entity aEntity, ItemStack aStack, EntityPlayer aPlayer) {
		return aOriginalDamage;
	}
}
