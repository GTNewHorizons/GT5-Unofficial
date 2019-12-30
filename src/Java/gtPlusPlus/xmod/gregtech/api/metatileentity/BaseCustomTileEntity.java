package gtPlusPlus.xmod.gregtech.api.metatileentity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.gregtech.PollutionUtils;
import gtPlusPlus.xmod.gregtech.common.Meta_GT_Proxy;
import gtPlusPlus.xmod.gregtech.common.StaticFields59;
import ic2.api.Direction;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class BaseCustomTileEntity extends BaseMetaTileEntity {
		
	protected NBTTagCompound mRecipeStuff2;
	private static final Field ENTITY_ITEM_HEALTH_FIELD_2;
	
	static {
		Field f = null;
		try {
			f = EntityItem.class.getDeclaredField("field_70291_e");
			f.setAccessible(true);
		} catch (Exception var4) {
			try {
				f = EntityItem.class.getDeclaredField("health");
				f.setAccessible(true);
			} catch (Exception var3) {
				var4.printStackTrace();
				var3.printStackTrace();
			}
		}

		ENTITY_ITEM_HEALTH_FIELD_2 = f;
	}

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
			
			boolean aExplosionDropItem = false;
			Object aProxyField = StaticFields59.getFieldFromGregtechProxy(false, "mExplosionItemDrop");
			if (boolean.class.isInstance(aProxyField) || Boolean.class.isInstance(aProxyField)) {
				aExplosionDropItem = (boolean) aProxyField;
			}
			
			if (aExplosionDropItem) {
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

			PollutionUtils.addPollution(this, 100000);
			this.mMetaTileEntity.doExplosion(aAmount);
		}

	}
	
	
	public void dropItems(ItemStack tItem) {
		if (tItem != null) {
			Random tRandom = new Random();
			EntityItem tItemEntity = new EntityItem(this.worldObj,
					(double) ((float) this.xCoord + tRandom.nextFloat() * 0.8F + 0.1F),
					(double) ((float) this.yCoord + tRandom.nextFloat() * 0.8F + 0.1F),
					(double) ((float) this.zCoord + tRandom.nextFloat() * 0.8F + 0.1F),
					new ItemStack(tItem.getItem(), tItem.stackSize, tItem.getItemDamage()));
			if (tItem.hasTagCompound()) {
				tItemEntity.getEntityItem().setTagCompound((NBTTagCompound) tItem.getTagCompound().copy());
			}

			tItemEntity.motionX = tRandom.nextGaussian() * 0.0500000007450581D;
			tItemEntity.motionY = tRandom.nextGaussian() * 0.0500000007450581D + 0.2000000029802322D;
			tItemEntity.motionZ = tRandom.nextGaussian() * 0.0500000007450581D;
			tItemEntity.hurtResistantTime = 999999;
			tItemEntity.lifespan = 60000;

			try {
				if (ENTITY_ITEM_HEALTH_FIELD_2 != null) {
					ENTITY_ITEM_HEALTH_FIELD_2.setInt(tItemEntity, 99999999);
				}
			} catch (Exception var5) {
				;
			}

			this.worldObj.spawnEntityInWorld(tItemEntity);
			tItem.stackSize = 0;
		}
	}

	public ArrayList<ItemStack> getDrops() {
		ArrayList<ItemStack> aDrops = new ArrayList<ItemStack>();
		ItemStack rStack = new ItemStack(GregTech_API.sBlockMachines, 1, this.getMetaTileID());
		// Currently not using my custom block.
		// ItemStack rStack = new ItemStack(Meta_GT_Proxy.sBlockMachines, 1,
		// this.getMetaTileID());
		boolean fail = true;

		ArrayList<ItemStack> aSuperDrops = super.getDrops();
		if (aSuperDrops != null && !aSuperDrops.isEmpty()) {
			ItemStack aSuperStack = super.getDrops().get(0);
			if (aSuperStack != null && aSuperStack.hasTagCompound()) {
				NBTTagCompound aSuperNBT = aSuperStack.getTagCompound();
				if (aSuperNBT != null && !aSuperNBT.hasNoTags()) {
					NBTTagCompound tNBT = (NBTTagCompound) aSuperNBT.copy();
					if (tNBT != null && !tNBT.hasNoTags()) {
						if (this.hasValidMetaTileEntity()) {
							this.mMetaTileEntity.setItemNBT(tNBT);
							rStack.setTagCompound(tNBT);
							fail = false;
			                aDrops.add(rStack);
						}
					}
				}

			}
		}
		if (fail) {
                aDrops.add(rStack);
		}
		return aDrops;
	}

	public boolean isTeleporterCompatible(Direction aSide) {
		return this.canAccessData() && this.mMetaTileEntity.isTeleporterCompatible();
	}

}