package gtPlusPlus.xmod.gregtech.api.metatileentity;

import java.util.ArrayList;
import java.util.Arrays;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Pollution;
import gtPlusPlus.xmod.gregtech.common.Meta_GT_Proxy;
import ic2.api.Direction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class BaseCustomTileEntity extends BaseMetaTileEntity {
	
	protected boolean mHasEnoughEnergy;	
	
	protected short mID;
	protected long oOutput;
	
	protected long mAcceptedAmperes;
	protected NBTTagCompound mRecipeStuff;
	
	

	protected boolean[] mActiveEUInputs;
	protected boolean[] mActiveEUOutputs;

	public BaseCustomTileEntity() {
		super();
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
			aNBT.setInteger("nbtVersion", GT_Mod.TOTAL_VERSION);
		} catch (Throwable arg6) {
			GT_Log.err.println(
					"Encountered CRITICAL ERROR while saving MetaTileEntity, the Chunk whould\'ve been corrupted by now, but I prevented that. Please report immidietly to GregTech Intergalactical!!!");
			arg6.printStackTrace(GT_Log.err);
		}

	}

	public void readFromNBT(NBTTagCompound aNBT) {
		super.readFromNBT(aNBT);
		this.setInitialValuesAsNBT(aNBT, (short) 0);
	}

	public void updateStatus() {
	}

	public void chargeItem(ItemStack aStack) {
		this.decreaseStoredEU(
				(long) GT_ModHandler.chargeElectricItem(aStack, (int) Math.min(2147483647L, this.getStoredEU()),
						(int) Math.min(2147483647L, this.mMetaTileEntity.getOutputTier()), false, false),
				true);
	}

	public void dischargeItem(ItemStack aStack) {
		this.increaseStoredEnergyUnits((long) GT_ModHandler.dischargeElectricItem(aStack,
				(int) Math.min(2147483647L, this.getEUCapacity() - this.getStoredEU()),
				(int) Math.min(2147483647L, this.mMetaTileEntity.getInputTier()), false, false, false), true);
	}

	public boolean decreaseStoredEnergyUnits(long aEnergy, boolean aIgnoreTooLessEnergy) {
		return !this.canAccessData() ? false
				: (this.mHasEnoughEnergy = this.decreaseStoredEU(aEnergy, aIgnoreTooLessEnergy)
						|| this.decreaseStoredSteam(aEnergy, false)
						|| aIgnoreTooLessEnergy && this.decreaseStoredSteam(aEnergy, true));
	}

	public boolean increaseStoredEnergyUnits(long aEnergy, boolean aIgnoreTooMuchEnergy) {
		if (!this.canAccessData()) {
			return false;
		} else if (this.getStoredEU() >= this.getEUCapacity() && !aIgnoreTooMuchEnergy) {
			return false;
		} else {
			this.setStoredEU(this.mMetaTileEntity.getEUVar() + aEnergy);
			return true;
		}
	}

	public boolean inputEnergyFrom(byte aSide) {
		return aSide == 6 ? true
				: (!this.isServerSide() ? this.isEnergyInputSide(aSide)
						: aSide >= 0 && aSide < 6 && this.mActiveEUInputs[aSide] && !this.mReleaseEnergy);
	}

	public boolean outputsEnergyTo(byte aSide) {
		return aSide == 6 ? true
				: (!this.isServerSide() ? this.isEnergyOutputSide(aSide)
						: aSide >= 0 && aSide < 6 && this.mActiveEUOutputs[aSide] || this.mReleaseEnergy);
	}

	public long getOutputAmperage() {
		return this.canAccessData() && this.mMetaTileEntity.isElectric() ? this.mMetaTileEntity.maxAmperesOut() : 0L;
	}

	public long getOutputVoltage() {
		return this.canAccessData() && this.mMetaTileEntity.isElectric() && this.mMetaTileEntity.isEnetOutput()
				? this.mMetaTileEntity.maxEUOutput()
				: 0L;
	}

	public long getInputAmperage() {
		return this.canAccessData() && this.mMetaTileEntity.isElectric() ? this.mMetaTileEntity.maxAmperesIn() : 0L;
	}

	public long getInputVoltage() {
		return this.canAccessData() && this.mMetaTileEntity.isElectric() ? this.mMetaTileEntity.maxEUInput()
				: 2147483647L;
	}

	public boolean increaseStoredSteam(long aEnergy, boolean aIgnoreTooMuchEnergy) {
		if (!this.canAccessData()) {
			return false;
		} else if (this.mMetaTileEntity.getSteamVar() >= this.getSteamCapacity() && !aIgnoreTooMuchEnergy) {
			return false;
		} else {
			this.setStoredSteam(this.mMetaTileEntity.getSteamVar() + aEnergy);
			return true;
		}
	}

	public long getUniversalEnergyStored() {
		return Math.max(this.getStoredEU(), this.getStoredSteam());
	}

	public long getUniversalEnergyCapacity() {
		return Math.max(this.getEUCapacity(), this.getSteamCapacity());
	}

	public long getStoredEU() {
		return this.canAccessData() ? Math.min(this.mMetaTileEntity.getEUVar(), this.getEUCapacity()) : 0L;
	}

	public long getEUCapacity() {
		return this.canAccessData() ? this.mMetaTileEntity.maxEUStore() : 0L;
	}

	private boolean isEnergyInputSide(byte aSide) {
		if (aSide >= 0 && aSide < 6) {
			if (!this.getCoverBehaviorAtSide(aSide).letsEnergyIn(aSide, this.getCoverIDAtSide(aSide),
					this.getCoverDataAtSide(aSide), this)) {
				return false;
			}

			if (this.isInvalid() || this.mReleaseEnergy) {
				return false;
			}

			if (this.canAccessData() && this.mMetaTileEntity.isElectric() && this.mMetaTileEntity.isEnetInput()) {
				return this.mMetaTileEntity.isInputFacing(aSide);
			}
		}

		return false;
	}

	private boolean isEnergyOutputSide(byte aSide) {
		if (aSide >= 0 && aSide < 6) {
			if (!this.getCoverBehaviorAtSide(aSide).letsEnergyOut(aSide, this.getCoverIDAtSide(aSide),
					this.getCoverDataAtSide(aSide), this)) {
				return false;
			}

			if (this.isInvalid() || this.mReleaseEnergy) {
				return this.mReleaseEnergy;
			}

			if (this.canAccessData() && this.mMetaTileEntity.isElectric() && this.mMetaTileEntity.isEnetOutput()) {
				return this.mMetaTileEntity.isOutputFacing(aSide);
			}
		}

		return false;
	}

	public boolean setStoredEU(long aEnergy) {
		if (!this.canAccessData()) {
			return false;
		} else {
			if (aEnergy < 0L) {
				aEnergy = 0L;
			}

			this.mMetaTileEntity.setEUVar(aEnergy);
			return true;
		}
	}

	public boolean decreaseStoredEU(long aEnergy, boolean aIgnoreTooLessEnergy) {
		if (!this.canAccessData()) {
			return false;
		} else if (this.mMetaTileEntity.getEUVar() - aEnergy < 0L && !aIgnoreTooLessEnergy) {
			return false;
		} else {
			this.setStoredEU(this.mMetaTileEntity.getEUVar() - aEnergy);
			if (this.mMetaTileEntity.getEUVar() < 0L) {
				this.setStoredEU(0L);
				return false;
			} else {
				return true;
			}
		}
	}

	public void doEnergyExplosion() {
		if (this.getUniversalEnergyCapacity() > 0L
				&& this.getUniversalEnergyStored() >= this.getUniversalEnergyCapacity() / 5L) {
			this.doExplosion(
					this.oOutput * (long) (this.getUniversalEnergyStored() >= this.getUniversalEnergyCapacity() ? 4
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

			if (this.mRecipeStuff != null) {
				for (i = 0; i < 9; ++i) {
					if (this.getRandomNumber(100) < 50) {
						this.dropItems(GT_Utility.loadItem(this.mRecipeStuff, "Ingredient." + i));
					}
				}
			}

			GT_Pollution.addPollution(this, 100000);
			this.mMetaTileEntity.doExplosion(aAmount);
		}

	}

	public ArrayList<ItemStack> getDrops() {
		ItemStack rStack = new ItemStack(Meta_GT_Proxy.sBlockMachines, 1, this.mID);		
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

	public long getAverageElectricInput() {
		int rEU = 0;

		for (int i = 0; i < this.mAverageEUInput.length; ++i) {
			if (i != this.mAverageEUInputIndex) {
				rEU += this.mAverageEUInput[i];
			}
		}

		return (long) (rEU / (this.mAverageEUInput.length - 1));
	}

	public long getAverageElectricOutput() {
		int rEU = 0;

		for (int i = 0; i < this.mAverageEUOutput.length; ++i) {
			if (i != this.mAverageEUOutputIndex) {
				rEU += this.mAverageEUOutput[i];
			}
		}

		return (long) (rEU / (this.mAverageEUOutput.length - 1));
	}

	public long injectEnergyUnits(byte aSide, long aVoltage, long aAmperage) {
		if (this.canAccessData() && this.mMetaTileEntity.isElectric() && this.inputEnergyFrom(aSide) && aAmperage > 0L
				&& aVoltage > 0L && this.getStoredEU() < this.getEUCapacity()
				&& this.mMetaTileEntity.maxAmperesIn() > this.mAcceptedAmperes) {
			if (aVoltage > this.getInputVoltage()) {
				this.doExplosion(aVoltage);
				return 0L;
			} else if (this
					.increaseStoredEnergyUnits(
							aVoltage * (aAmperage = Math
									.min(aAmperage,
											Math.min(this.mMetaTileEntity.maxAmperesIn() - this.mAcceptedAmperes,
													1L + (this.getEUCapacity() - this.getStoredEU()) / aVoltage))),
							true)) {
				this.mAverageEUInput[this.mAverageEUInputIndex] = (int) ((long) this.mAverageEUInput[this.mAverageEUInputIndex]
						+ aVoltage * aAmperage);
				this.mAcceptedAmperes += aAmperage;
				return aAmperage;
			} else {
				return 0L;
			}
		} else {
			return 0L;
		}
	}

	public boolean drainEnergyUnits(byte aSide, long aVoltage, long aAmperage) {
		if (this.canAccessData() && this.mMetaTileEntity.isElectric() && this.outputsEnergyTo(aSide)
				&& this.getStoredEU() - aVoltage * aAmperage >= this.mMetaTileEntity.getMinimumStoredEU()) {
			if (this.decreaseStoredEU(aVoltage * aAmperage, false)) {
				this.mAverageEUOutput[this.mAverageEUOutputIndex] = (int) ((long) this.mAverageEUOutput[this.mAverageEUOutputIndex]
						+ aVoltage * aAmperage);
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean acceptsRotationalEnergy(byte aSide) {
		return this.canAccessData() && this.getCoverIDAtSide(aSide) == 0
				? this.mMetaTileEntity.acceptsRotationalEnergy(aSide)
				: false;
	}

	public boolean injectRotationalEnergy(byte aSide, long aSpeed, long aEnergy) {
		return this.canAccessData() && this.getCoverIDAtSide(aSide) == 0
				? this.mMetaTileEntity.injectRotationalEnergy(aSide, aSpeed, aEnergy)
				: false;
	}

	public double getOutputEnergyUnitsPerTick() {
		return (double) this.oOutput;
	}

	public double demandedEnergyUnits() {
		return !this.mReleaseEnergy && this.canAccessData() && this.mMetaTileEntity.isEnetInput()
				? (double) (this.getEUCapacity() - this.getStoredEU())
				: 0.0D;
	}

	public double injectEnergyUnits(ForgeDirection aDirection, double aAmount) {
		return this.injectEnergyUnits((byte) aDirection.ordinal(), (long) ((int) aAmount), 1L) > 0L ? 0.0D : aAmount;
	}

	public boolean acceptsEnergyFrom(TileEntity aEmitter, ForgeDirection aDirection) {
		return this.inputEnergyFrom((byte) aDirection.ordinal());
	}

	public boolean emitsEnergyTo(TileEntity aReceiver, ForgeDirection aDirection) {
		return this.outputsEnergyTo((byte) aDirection.ordinal());
	}

	public double getOfferedEnergy() {
		return this.canAccessData() && this.getStoredEU() - this.mMetaTileEntity.getMinimumStoredEU() >= this.oOutput
				? (double) Math.max(0L, this.oOutput)
				: 0.0D;
	}

	public void drawEnergy(double amount) {
		this.mAverageEUOutput[this.mAverageEUOutputIndex] = (int) ((double) this.mAverageEUOutput[this.mAverageEUOutputIndex]
				+ amount);
		this.decreaseStoredEU((long) ((int) amount), true);
	}

	public int injectEnergy(ForgeDirection aForgeDirection, int aAmount) {
		return this.injectEnergyUnits((byte) aForgeDirection.ordinal(), (long) aAmount, 1L) > 0L ? 0 : aAmount;
	}

	public int addEnergy(int aEnergy) {
		if (!this.canAccessData()) {
			return 0;
		} else {
			if (aEnergy > 0) {
				this.increaseStoredEnergyUnits((long) aEnergy, true);
			} else {
				this.decreaseStoredEU((long) (-aEnergy), true);
			}

			return (int) Math.min(2147483647L, this.mMetaTileEntity.getEUVar());
		}
	}

	public boolean isAddedToEnergyNet() {
		return false;
	}

	public int demandsEnergy() {
		return !this.mReleaseEnergy && this.canAccessData() && this.mMetaTileEntity.isEnetInput()
				? this.getCapacity() - this.getStored()
				: 0;
	}

	public int getCapacity() {
		return (int) Math.min(2147483647L, this.getEUCapacity());
	}

	public int getStored() {
		return (int) Math.min(2147483647L, Math.min(this.getStoredEU(), (long) this.getCapacity()));
	}

	public void setStored(int aEU) {
		if (this.canAccessData()) {
			this.setStoredEU((long) aEU);
		}

	}

	public int getMaxSafeInput() {
		return (int) Math.min(2147483647L, this.getInputVoltage());
	}

	public int getMaxEnergyOutput() {
		return this.mReleaseEnergy ? Integer.MAX_VALUE : this.getOutput();
	}

	public int getOutput() {
		return (int) Math.min(2147483647L, this.oOutput);
	}

	public int injectEnergy(Direction aDirection, int aAmount) {
		return this.injectEnergyUnits((byte) aDirection.toSideValue(), (long) aAmount, 1L) > 0L ? 0 : aAmount;
	}

	public boolean isTeleporterCompatible(Direction aSide) {
		return this.canAccessData() && this.mMetaTileEntity.isTeleporterCompatible();
	}

	public boolean acceptsEnergyFrom(TileEntity aReceiver, Direction aDirection) {
		return this.inputEnergyFrom((byte) aDirection.toSideValue());
	}

	public boolean emitsEnergyTo(TileEntity aReceiver, Direction aDirection) {
		return this.outputsEnergyTo((byte) aDirection.toSideValue());
	}

	public boolean isUniversalEnergyStored(long aEnergyAmount) {
		if (this.getUniversalEnergyStored() >= aEnergyAmount) {
			return true;
		} else {
			this.mHasEnoughEnergy = false;
			return false;
		}
	}

}