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
import net.minecraft.world.World;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.common.tools.GT_Tool;

import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtTools;
import gtPlusPlus.xmod.gregtech.common.items.behaviours.Behaviour_Pump;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fluids.FluidStack;

public class TOOL_Gregtech_Pump
extends GT_Tool {

	public static final List<String> mEffectiveList = Arrays.asList(new String[]{EntityIronGolem.class.getName(), "EntityTowerGuardian"});
	private final Behaviour_Pump mBehaviour;
	private FluidStack mStoredFluid;
	
	public TOOL_Gregtech_Pump() {
		mBehaviour = new Behaviour_Pump(100);
		//this.setFluidContainerStats(32000, 16000L, 16L);
	}

	@Override
	public float getNormalDamageAgainstEntity(final float aOriginalDamage, final Entity aEntity, final ItemStack aStack, final EntityPlayer aPlayer) {
		String tName = aEntity.getClass().getName();
		tName = tName.substring(tName.lastIndexOf(".") + 1);
		return (mEffectiveList.contains(tName)) || (tName.contains("Golem")) ? aOriginalDamage * 2.0F : aOriginalDamage;
	}
	
	public Behaviour_Pump getBehaviour() {
		return this.mBehaviour;
	}

	@Override
	public int getToolDamagePerBlockBreak() {
		return 1000;
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
		return 1000;
	}

	@Override
	public int getBaseQuality() {
		return 1;
	}

	@Override
	public float getBaseDamage() {
		return 0.0F;
	}

	@Override
	public float getSpeedMultiplier() {
		return 1.0F;
	}

	@Override
	public float getMaxDurabilityMultiplier() {
		return 1.5F;
	}

	@Override
	public String getCraftingSound() {
		return GregTech_API.sSoundList.get(Integer.valueOf(1));
	}

	@Override
	public String getEntityHitSound() {
		return GregTech_API.sSoundList.get(Integer.valueOf(2));
	}

	@Override
	public String getBreakingSound() {
		return GregTech_API.sSoundList.get(Integer.valueOf(0));
	}

	@Override
	public String getMiningSound() {
		return null;
	}

	@Override
	public boolean canBlock() {
		return false;
	}

	public boolean isWrench(){
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
		return false;
	}

	@Override
	public int convertBlockDrops(final List<ItemStack> aDrops, final ItemStack aStack, final EntityPlayer aPlayer, final Block aBlock, final int aX, final int aY, final int aZ, final byte aMetaData, final int aFortune, final boolean aSilkTouch, final BlockEvent.HarvestDropsEvent aEvent) {
		int rConversions = 0;		
		return rConversions;
	}

	@Override
	public ItemStack getBrokenItem(final ItemStack aStack) {
		return null;
	}

	@Override
	public IIconContainer getIcon(final boolean aIsToolHead, final ItemStack aStack) {
		return TexturesGtTools.PUMP;
	}

	@Override
	public short[] getRGBa(final boolean aIsToolHead, final ItemStack aStack) {
		return GT_MetaGenerated_Tool.getPrimaryMaterial(aStack).mRGBa;
	}

	@Override
	public void onToolCrafted(final ItemStack aStack, final EntityPlayer aPlayer) {
		super.onToolCrafted(aStack, aPlayer);
		aPlayer.triggerAchievement(AchievementList.buildSword);
		try {
			GT_Mod.achievements.issueAchievement(aPlayer, "tools");
			GT_Mod.achievements.issueAchievement(aPlayer, "unitool");
		} catch (final Exception e) {
		}
	}

	@Override
	public IChatComponent getDeathMessage(final EntityLivingBase aPlayer, final EntityLivingBase aEntity) {
		return new ChatComponentText(EnumChatFormatting.RED + aEntity.getCommandSenderName() + EnumChatFormatting.WHITE + " got pumped by " + EnumChatFormatting.GREEN + aPlayer.getCommandSenderName() + EnumChatFormatting.WHITE);
	}

	@Override
	public void onStatsAddedToTool(final GT_MetaGenerated_Tool aItem, final int aID) {
		aItem.addItemBehavior(aID, mBehaviour);
	}

	@Override
	public boolean isGrafter() {
		return false;
	}

	@Override
	public float getMiningSpeed(Block aBlock, byte aMetaData, float aDefault, EntityPlayer aPlayer, World worldObj,
			int aX, int aY, int aZ) {
		return 0f;
	}

	@Override
	public boolean isChainsaw() {
		return false;
	}

	@Override
	public boolean isMiningTool() {
		return false;
	}

	@Override
	public boolean isRangedWeapon() {
		return false;
	}
	
}
