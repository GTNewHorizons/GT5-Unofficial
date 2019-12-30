package gtPlusPlus.xmod.gregtech.common.tools;

import gregtech.GT_Mod;
import gregtech.api.enums.Textures.ItemIcons;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.common.tools.GT_Tool_WireCutter;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtTools;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class TOOL_Gregtech_ElectricSnips
extends GT_Tool_WireCutter {


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
		return 4.0F;
	}

	@Override
	public float getSpeedMultiplier() {
		return 1F;
	}

	@Override
	public float getMaxDurabilityMultiplier() {
		return 1.4F;
	}

	@Override
	public ItemStack getBrokenItem(final ItemStack aStack) {
		return null;
	}
	
	public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
		return (IIconContainer) (aIsToolHead
				? TexturesGtTools.ELECTRIC_SNIPS
				: ItemIcons.POWER_UNIT_MV);
	}

	public short[] getRGBa(boolean aIsToolHead, ItemStack aStack) {
		return !aIsToolHead
				? GT_MetaGenerated_Tool.getPrimaryMaterial(aStack).mRGBa
				: GT_MetaGenerated_Tool.getPrimaryMaterial(aStack).mRGBa;
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
		return new ChatComponentText(EnumChatFormatting.RED + aEntity.getCommandSenderName() + EnumChatFormatting.WHITE + " has been Snipped out of existence by " + EnumChatFormatting.GREEN + aPlayer.getCommandSenderName() + EnumChatFormatting.WHITE);
	}
	
}
