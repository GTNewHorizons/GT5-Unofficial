package gtPlusPlus.xmod.gregtech.common.tools;

import java.util.Arrays;
import java.util.List;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures.ItemIcons;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.common.items.behaviors.Behaviour_None;
import gregtech.common.tools.GT_Tool;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtTools;
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

public class TOOL_Gregtech_ElectricButcherKnife
extends GT_Tool {

	public static final List<String> mEffectiveList = Arrays.asList(new String[]{EntityIronGolem.class.getName(), "EntityTowerGuardian"});


	@Override
	public float getNormalDamageAgainstEntity(final float aOriginalDamage, final Entity aEntity, final ItemStack aStack, final EntityPlayer aPlayer) {
		String tName = aEntity.getClass().getName();
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
		final String tTool = aBlock.getHarvestTool(aMetaData);
		return (tTool != null) && (tTool.equals("sword") || tTool.equals("file")); 
	}
	

	@Override
	public int convertBlockDrops(final List<ItemStack> aDrops, final ItemStack aStack, final EntityPlayer aPlayer, final Block aBlock, final int aX, final int aY, final int aZ, final byte aMetaData, final int aFortune, final boolean aSilkTouch, final BlockEvent.HarvestDropsEvent aEvent) {
		int rConversions = 0;
		/*GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sHammerRecipes.findRecipe(null, true, 2147483647L, null, new ItemStack[]{new ItemStack(aBlock, 1, aMetaData)});
		if ((tRecipe == null) || (aBlock.hasTileEntity(aMetaData))) {
			for (final ItemStack tDrop : aDrops) {
				tRecipe = GT_Recipe.GT_Recipe_Map.sHammerRecipes.findRecipe(null, true, 2147483647L, null, new ItemStack[]{GT_Utility.copyAmount(1L, new Object[]{tDrop})});
				if (tRecipe != null) {
					final ItemStack tHammeringOutput = tRecipe.getOutput(0);
					if (tHammeringOutput != null) {
						rConversions += tDrop.stackSize;
						tDrop.stackSize *= tHammeringOutput.stackSize;
						tHammeringOutput.stackSize = tDrop.stackSize;
						GT_Utility.setStack(tDrop, tHammeringOutput);
					}
				}
			}
		} else {
			aDrops.clear();
			aDrops.add(tRecipe.getOutput(0));
			rConversions++;
		}*/
		return rConversions;
	}

	@Override
	public ItemStack getBrokenItem(final ItemStack aStack) {
		return null;
	}
	
	public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
		return (IIconContainer) (aIsToolHead
				? TexturesGtTools.ELECTRIC_BUTCHER_KNIFE
				: ItemIcons.POWER_UNIT_HV);
	}

	public short[] getRGBa(boolean aIsToolHead, ItemStack aStack) {
		return aIsToolHead
				? GT_MetaGenerated_Tool.getPrimaryMaterial(aStack).mRGBa
				: Materials.RedSteel.mRGBa;
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
		return new ChatComponentText(EnumChatFormatting.RED + aEntity.getCommandSenderName() + EnumChatFormatting.WHITE + " has been Sliced out of existence by " + EnumChatFormatting.GREEN + aPlayer.getCommandSenderName() + EnumChatFormatting.WHITE);
	}

	@Override
	public void onStatsAddedToTool(final GT_MetaGenerated_Tool aItem, final int aID) {
		aItem.addItemBehavior(aID, new Behaviour_None());
	}

	@Override
	public boolean isGrafter() {
		return false;
	}
	
}
