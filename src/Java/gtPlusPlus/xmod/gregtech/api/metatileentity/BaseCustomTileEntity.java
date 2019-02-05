package gtPlusPlus.xmod.gregtech.api.metatileentity;

import java.util.ArrayList;
import java.util.Arrays;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Pollution;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.common.Meta_GT_Proxy;
import ic2.api.Direction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class BaseCustomTileEntity extends BaseMetaTileEntity {
		
	protected NBTTagCompound mRecipeStuff2;

	public BaseCustomTileEntity() {
		super();
		Logger.MACHINE_INFO("Created new BaseCustomTileEntity");
	}

	public void writeToNBT(NBTTagCompound aNBT) {
		try {
			super.writeToNBT(aNBT);
		} catch (Throwable arg7) {
			GT_Log.err.println(
					"Encountered CRITICAL ERROR while saving MetaTileEntity, the Chunk whould\'ve been corrupted by now, but I prevented that. Please report immidietly to GregTech Intergalactical!!!");
			arg7.printStackTrace(GT_Log.err);
		}

		try {
			if (!aNBT.hasKey("ModVersion"))
			aNBT.setString("ModVersion", CORE.VERSION);
		} catch (Throwable arg6) {
			GT_Log.err.println(
					"Encountered CRITICAL ERROR while saving MetaTileEntity, the Chunk whould\'ve been corrupted by now, but I prevented that. Please report immidietly to GregTech Intergalactical!!!");
			arg6.printStackTrace(GT_Log.err);
		}
	}

	public void doEnergyExplosion() {
		if (this.getUniversalEnergyCapacity() > 0L
				&& this.getUniversalEnergyStored() >= this.getUniversalEnergyCapacity() / 5L) {
			this.doExplosion(
					this.getOutput() * (long) (this.getUniversalEnergyStored() >= this.getUniversalEnergyCapacity() ? 4
							: (this.getUniversalEnergyStored() >= this.getUniversalEnergyCapacity() / 2L ? 2 : 1)));
			GT_Mod arg9999 = GT_Mod.instance;
			GT_Mod.achievements.issueAchievement(this.getWorldObj().getPlayerEntityByName(this.getOwnerName()),
					"electricproblems");
		}

	}

	public void doExplosion(long aAmount) {
		if (this.canAccessData()) {
			if (GregTech_API.sMachineWireFire && this.mMetaTileEntity.isElectric()) {
				try {
					this.mReleaseEnergy = true;
					Util.emitEnergyToNetwork(GT_Values.V[5], Math.max(1L, this.getStoredEU() / GT_Values.V[5]), this);
				} catch (Exception arg4) {
					;
				}
			}

			this.mReleaseEnergy = false;
			this.mMetaTileEntity.onExplosion();
			int i;
			if (GT_Mod.gregtechproxy.mExplosionItemDrop) {
				for (i = 0; i < this.getSizeInventory(); ++i) {
					ItemStack tItem = this.getStackInSlot(i);
					if (tItem != null && tItem.stackSize > 0 && this.isValidSlot(i)) {
						this.dropItems(tItem);
						this.setInventorySlotContents(i, (ItemStack) null);
					}
				}
			}

			if (this.mRecipeStuff2 != null) {
				for (i = 0; i < 9; ++i) {
					this.dropItems(GT_Utility.loadItem(this.mRecipeStuff2, "Ingredient." + i));
				}
			}

			GT_Pollution.addPollution(this, 100000);
			this.mMetaTileEntity.doExplosion(aAmount);
		}

	}

	public ArrayList<ItemStack> getDrops() {
		ItemStack rStack = new ItemStack(Meta_GT_Proxy.sBlockMachines, 1, this.getMetaTileID());		
		NBTTagCompound aSuperNBT = super.getDrops().get(0).getTagCompound();		
		NBTTagCompound tNBT = aSuperNBT;
		if (this.hasValidMetaTileEntity()) {
			this.mMetaTileEntity.setItemNBT(tNBT);
		}
		if (!tNBT.hasNoTags()) {
			rStack.setTagCompound(tNBT);
		}

		return new ArrayList<ItemStack>(Arrays.asList(new ItemStack[] { rStack }));
	}

	public boolean isTeleporterCompatible(Direction aSide) {
		return this.canAccessData() && this.mMetaTileEntity.isTeleporterCompatible();
	}

}