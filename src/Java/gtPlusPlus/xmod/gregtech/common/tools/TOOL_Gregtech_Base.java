package gtPlusPlus.xmod.gregtech.common.tools;

import java.util.List;

import gregtech.api.GregTech_API;
import gregtech.api.damagesources.GT_DamageSources;
import gtPlusPlus.xmod.gregtech.api.interfaces.internal.Interface_ToolStats;
import gtPlusPlus.xmod.gregtech.api.items.Gregtech_MetaTool;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.*;
import net.minecraftforge.event.world.BlockEvent;

public abstract class TOOL_Gregtech_Base implements Interface_ToolStats {
	public static final Enchantment[]	FORTUNE_ENCHANTMENT		= {
			Enchantment.fortune
	};
	public static final Enchantment[]	LOOTING_ENCHANTMENT		= {
			Enchantment.looting
	};
	public static final Enchantment[]	ZERO_ENCHANTMENTS		= new Enchantment[0];
	public static final int[]			ZERO_ENCHANTMENT_LEVELS	= new int[0];

	@Override
	public boolean canBlock() {
		return false;
	}

	@Override
	public int convertBlockDrops(final List<ItemStack> aDrops, final ItemStack aStack, final EntityPlayer aPlayer,
			final Block aBlock, final int aX, final int aY, final int aZ, final byte aMetaData, final int aFortune,
			final boolean aSilkTouch, final BlockEvent.HarvestDropsEvent aEvent) {
		return 0;
	}

	@Override
	public int getBaseQuality() {
		return 0;
	}

	@Override
	public String getBreakingSound() {
		return GregTech_API.sSoundList.get(Integer.valueOf(0));
	}

	@Override
	public ItemStack getBrokenItem(final ItemStack aStack) {
		return null;
	}

	@Override
	public String getCraftingSound() {
		return null;
	}

	@Override
	public DamageSource getDamageSource(final EntityLivingBase aPlayer, final Entity aEntity) {
		return GT_DamageSources.getCombatDamage(aPlayer instanceof EntityPlayer ? "player" : "mob", aPlayer,
				aEntity instanceof EntityLivingBase ? this.getDeathMessage(aPlayer, (EntityLivingBase) aEntity) : null);
	}

	public IChatComponent getDeathMessage(final EntityLivingBase aPlayer, final EntityLivingBase aEntity) {
		return new EntityDamageSource(aPlayer instanceof EntityPlayer ? "player" : "mob", aPlayer)
				.func_151519_b(aEntity);
	}

	@Override
	public int[] getEnchantmentLevels(final ItemStack aStack) {
		return TOOL_Gregtech_Base.ZERO_ENCHANTMENT_LEVELS;
	}

	@Override
	public Enchantment[] getEnchantments(final ItemStack aStack) {
		return TOOL_Gregtech_Base.ZERO_ENCHANTMENTS;
	}

	@Override
	public String getEntityHitSound() {
		return null;
	}

	@Override
	public int getHurtResistanceTime(final int aOriginalHurtResistance, final Entity aEntity) {
		return aOriginalHurtResistance;
	}

	@Override
	public float getMagicDamageAgainstEntity(final float aOriginalDamage, final Entity aEntity, final ItemStack aStack,
			final EntityPlayer aPlayer) {
		return aOriginalDamage;
	}

	@Override
	public float getMaxDurabilityMultiplier() {
		return 1.0F;
	}

	@Override
	public String getMiningSound() {
		return null;
	}

	@Override
	public float getNormalDamageAgainstEntity(final float aOriginalDamage, final Entity aEntity, final ItemStack aStack,
			final EntityPlayer aPlayer) {
		return aOriginalDamage;
	}

	@Override
	public float getSpeedMultiplier() {
		return 1.0F;
	}

	@Override
	public int getToolDamagePerBlockBreak() {
		return 100;
	}

	@Override
	public int getToolDamagePerContainerCraft() {
		return 800;
	}

	@Override
	public int getToolDamagePerDropConversion() {
		return 100;
	}

	@Override
	public int getToolDamagePerEntityAttack() {
		return 200;
	}

	public boolean isChainsaw() {
		return false;
	}

	@Override
	public boolean isCrowbar() {
		return false;
	}

	@Override
	public boolean isGrafter() {
		return false;
	}

	@Override
	public boolean isMiningTool() {
		return true;
	}

	@Override
	public boolean isRangedWeapon() {
		return false;
	}

	@Override
	public boolean isWeapon() {
		return false;
	}

	@Override
	public boolean isWrench() {
		return false;
	}

	@Override
	public void onStatsAddedToTool(final Gregtech_MetaTool aItem, final int aID) {
	}

	@Override
	public void onToolCrafted(final ItemStack aStack, final EntityPlayer aPlayer) {
		aPlayer.triggerAchievement(AchievementList.openInventory);
		aPlayer.triggerAchievement(AchievementList.mineWood);
		aPlayer.triggerAchievement(AchievementList.buildWorkBench);
	}
}
