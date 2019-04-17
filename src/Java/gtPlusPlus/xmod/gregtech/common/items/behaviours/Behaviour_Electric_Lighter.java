package gtPlusPlus.xmod.gregtech.common.items.behaviours;

import java.util.List;

import codechicken.lib.math.MathHelper;
import gregtech.api.GregTech_API;
import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.GT_Utility.ItemNBT;
import gregtech.common.items.behaviors.Behaviour_None;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.common.helpers.ChargingHelper;
import gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedGregtechTools;
import ic2.api.item.IElectricItemManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class Behaviour_Electric_Lighter extends Behaviour_None {

	private final ItemStack mLighter;

	private final long mFuelAmount;

	private final String mTooltip = GT_LanguageManager.addStringLocalization("gt.behaviour.lighter.tooltip",
			"Can light things on Fire");
	private final String mTooltipUses = GT_LanguageManager.addStringLocalization("gt.behaviour.lighter.uses",
			"Remaining Uses:");
	private final String mTooltipUnstackable = GT_LanguageManager.addStringLocalization("gt.behaviour.unstackable",
			"Not usable when stacked!");

	public Behaviour_Electric_Lighter(ItemStack aFullLighter, long aFuelAmount) {
		this.mLighter = aFullLighter;
		this.mFuelAmount = aFuelAmount;
	}

	public boolean onLeftClickEntity(GT_MetaBase_Item aItem, ItemStack aStack, EntityPlayer aPlayer, Entity aEntity) {
		if (!aPlayer.worldObj.isRemote && aStack.stackSize == 1) {
			boolean rOutput = false;
			if (aEntity instanceof EntityCreeper) {
				if (this.prepare(aStack) || aPlayer.capabilities.isCreativeMode) {
					GT_Utility.sendSoundToPlayers(aPlayer.worldObj, (String) GregTech_API.sSoundList.get(6), 1.0F, 1.0F,
							MathHelper.floor_double(aEntity.posX), MathHelper.floor_double(aEntity.posY),
							MathHelper.floor_double(aEntity.posZ));
					((EntityCreeper) aEntity).func_146079_cb();
					rOutput = true;
				}
			}
			return rOutput;
		} else {
			return false;
		}
	}

	public boolean onItemUse(GT_MetaBase_Item aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX,
			int aY, int aZ, int aSide, float hitX, float hitY, float hitZ) {
		return false;
	}

	public boolean onItemUseFirst(GT_MetaBase_Item aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX,
			int aY, int aZ, int aSide, float hitX, float hitY, float hitZ) {
		if (!aWorld.isRemote && aStack.stackSize == 1) {
			Logger.WARNING("Preparing Lighter a");
			boolean rOutput = false;
			ForgeDirection tDirection = ForgeDirection.getOrientation(aSide);
			aX += tDirection.offsetX;
			aY += tDirection.offsetY;
			aZ += tDirection.offsetZ;
			if (GT_Utility.isBlockAir(aWorld, aX, aY, aZ) && aPlayer.canPlayerEdit(aX, aY, aZ, aSide, aStack)) {
				Logger.WARNING("Preparing Lighter b");
				if (this.prepare(aStack) || aPlayer.capabilities.isCreativeMode) {
					Logger.WARNING("Preparing Lighter c");
					GT_Utility.sendSoundToPlayers(aWorld, (String) GregTech_API.sSoundList.get(6), 1.0F, 1.0F, aX, aY,
							aZ);
					aWorld.setBlock(aX, aY, aZ, Blocks.fire);
					rOutput = true;
					// ItemNBT.setLighterFuel(aStack, tFuelAmount);
					return rOutput;
				}
			}
		}
		Logger.WARNING("Preparing Lighter z");
		return false;
	}

	private boolean prepare(ItemStack aStack) {
		if (aStack != null) {
			Logger.WARNING("Preparing Lighter 1");
			if (aStack.getItem() instanceof MetaGeneratedGregtechTools) {
				Logger.WARNING("Preparing Lighter 2");
				if (ChargingHelper.isItemValid(aStack)) {
					Logger.WARNING("Preparing Lighter 3");
					if (aStack.getItem() instanceof IElectricItemManager) {
						Logger.WARNING("Preparing Lighter 4");
						IElectricItemManager aItemElec = (IElectricItemManager) aStack.getItem();
						double aCharge = aItemElec.getCharge(aStack);
						long aEuCost = 4096 * 2;
						if (aCharge >= aEuCost) {
							Logger.WARNING("Preparing Lighter 5");
							aItemElec.discharge(aStack, aEuCost, 3, true, true, false);
							return true;
						}
					}
				}
			}
		}
		Logger.WARNING("Preparing Lighter 0");
		return false;
	}

	private void useUp(ItemStack aStack) {

	}

	public List<String> getAdditionalToolTips(GT_MetaBase_Item aItem, List<String> aList, ItemStack aStack) {
		aList.add(this.mTooltip);
		int aUses = 0;

		if (aStack != null) {
			if (aStack.getItem() instanceof MetaGeneratedGregtechTools) {
				if (ChargingHelper.isItemValid(aStack)) {
					if (aStack.getItem() instanceof IElectricItemManager) {
						IElectricItemManager aItemElec = (IElectricItemManager) aStack.getItem();
						double aCharge = aItemElec.getCharge(aStack);
						long aEuCost = 4096 * 2;
						aUses = (int) (aCharge / aEuCost);
					}
				}
			}
		}

		NBTTagCompound tNBT = aStack.getTagCompound();
		aList.add(this.mTooltipUses + " " + aUses);
		aList.add(this.mTooltipUnstackable);
		return aList;
	}
}