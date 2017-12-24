package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.generators;

import static gregtech.api.enums.GT_Values.V;

import java.util.Collection;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_DeluxeTank;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public abstract class GregtechDoubleFuelGeneratorBase extends GT_MetaTileEntity_DeluxeTank {

	private boolean useFuel = false;

	public GregtechDoubleFuelGeneratorBase(final int aID, final String aName, final String aNameRegional, final int aTier, final String aDescription, final ITexture... aTextures) {
		super(aID, aName, aNameRegional, aTier, 4, aDescription, aTextures);
	}

	public GregtechDoubleFuelGeneratorBase(final String aName, final int aTier, final String aDescription, final ITexture[][][] aTextures) {
		super(aName, aTier, 4, aDescription, aTextures);
	}

	@Override
	public ITexture[][][] getTextureSet(final ITexture[] aTextures) {
		final ITexture[][][] rTextures = new ITexture[10][17][];
		for (byte i = -1; i < 16; i++) {
			rTextures[0][i + 1] = this.getFront(i);
			rTextures[1][i + 1] = this.getBack(i);
			rTextures[2][i + 1] = this.getBottom(i);
			rTextures[3][i + 1] = this.getTop(i);
			rTextures[4][i + 1] = this.getSides(i);
			rTextures[5][i + 1] = this.getFrontActive(i);
			rTextures[6][i + 1] = this.getBackActive(i);
			rTextures[7][i + 1] = this.getBottomActive(i);
			rTextures[8][i + 1] = this.getTopActive(i);
			rTextures[9][i + 1] = this.getSidesActive(i);
		}
		return rTextures;
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		return this.mTextures[(aActive ? 5 : 0) + (aSide == aFacing ? 0 : aSide == GT_Utility.getOppositeSide(aFacing) ? 1 : aSide == 0 ? 2 : aSide == 1 ? 3 : 4)][aColorIndex + 1];
	}


	@Override
	public String[] getDescription() {
		return new String[]{this.mDescription, "Fuel Efficiency: " + this.getEfficiency() + "%", CORE.GT_Tooltip};
	}


	/* @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) return true;
        aBaseMetaTileEntity.openGUI(aPlayer);
        return true;
    }*/

	@Override
	public boolean onRightclick(final IGregTechTileEntity aBaseMetaTileEntity, final EntityPlayer aPlayer) {
		if (aBaseMetaTileEntity.isClientSide()){
			Logger.WARNING("Entity is Client side, simply returning true");
			return true;
		}
		Logger.WARNING("Entity is not Client side, opening entity Container and by extension, it's GUI, then returning true");
		aBaseMetaTileEntity.openGUI(aPlayer);
		return true;
	}

	public ITexture[] getFront(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1]};
	}

	public ITexture[] getBack(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1]};
	}

	public ITexture[] getBottom(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1]};
	}

	public ITexture[] getTop(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1]};
	}

	public ITexture[] getSides(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1]};
	}

	public ITexture[] getFrontActive(final byte aColor) {
		return this.getFront(aColor);
	}

	public ITexture[] getBackActive(final byte aColor) {
		return this.getBack(aColor);
	}

	public ITexture[] getBottomActive(final byte aColor) {
		return this.getBottom(aColor);
	}

	public ITexture[] getTopActive(final byte aColor) {
		return this.getTop(aColor);
	}

	public ITexture[] getSidesActive(final byte aColor) {
		return this.getSides(aColor);
	}

	@Override
	public boolean isFacingValid(final byte aSide) {
		return aSide > 1;
	}

	@Override
	public boolean isSimpleMachine() {
		return false;
	}

	@Override
	public boolean isValidSlot(final int aIndex) {
		return aIndex < 2;
	}

	@Override
	public boolean isEnetOutput() {
		return true;
	}

	@Override
	public boolean isOutputFacing(final byte aSide) {
		return true;
	}

	@Override
	public boolean isAccessAllowed(final EntityPlayer aPlayer) {
		return true;
	}

	@Override
	public long maxEUOutput() {
		return this.getBaseMetaTileEntity().isAllowedToWork() ? V[this.mTier] : 0;
	}

	@Override
	public long maxEUStore() {
		return Math.max(this.getEUVar(), (V[this.mTier] * 115) + this.getMinimumStoredEU());
	}

	@Override
	public boolean doesFillContainers() {
		return this.getBaseMetaTileEntity().isAllowedToWork();
	}

	@Override
	public boolean doesEmptyContainers() {
		return this.getBaseMetaTileEntity().isAllowedToWork();
	}

	@Override
	public boolean canTankBeFilled() {
		return this.getBaseMetaTileEntity().isAllowedToWork();
	}

	@Override
	public boolean canTankBeEmptied() {
		return this.getBaseMetaTileEntity().isAllowedToWork();
	}

	@Override
	public boolean displaysItemStack() {
		return true;
	}

	@Override
	public boolean displaysStackSize() {
		return false;
	}

	@Override
	public boolean isFluidInputAllowed(final FluidStack aFluid) {
		return this.getFuelValue(aFluid) > 0;
	}

	@Override
	public long getMinimumStoredEU() {
		return 512;
	}

	@Override
	public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
		if (aBaseMetaTileEntity.isServerSide() && aBaseMetaTileEntity.isAllowedToWork() && ((aTick % 10) == 0)) {
			if (this.mFluid == null) {
				if (aBaseMetaTileEntity.getUniversalEnergyStored() < (this.maxEUOutput() + this.getMinimumStoredEU())) {
					this.mInventory[this.getStackDisplaySlot()] = null;
				} else {
					if (this.mInventory[this.getStackDisplaySlot()] == null) {
						this.mInventory[this.getStackDisplaySlot()] = new ItemStack(Blocks.fire, 1);
					}
					this.mInventory[this.getStackDisplaySlot()].setStackDisplayName("Generating: " + (aBaseMetaTileEntity.getUniversalEnergyStored() - this.getMinimumStoredEU()) + " EU");
				}
			} else {
				if ((this.mFluid != null) && (this.mFluid2 != null)){
					final int tFuelValue = this.getFuelValue(this.mFluid), tConsumed = this.consumedFluidPerOperation(this.mFluid);
					final int tFuelValue2 = this.getFuelValue(this.mFluid2), tConsumed2 = this.consumedFluidPerOperation(this.mFluid2);
					if (((tFuelValue > 0) && (tConsumed > 0) && (this.mFluid.amount > tConsumed))/* && (tFuelValue2 > 0 && tConsumed2 > 0 && mFluid2.amount > tConsumed2)*/) {

						Logger.WARNING("tFuelValue: "+tFuelValue);
						Logger.WARNING("tConsumed: "+tConsumed);
						Logger.WARNING("mFluid.name: "+this.mFluid.getFluid().getName());
						Logger.WARNING("mFluid.amount: "+this.mFluid.amount);
						Logger.WARNING("mFluid.amount > tConsumed: "+(this.mFluid.amount > tConsumed));

						Logger.WARNING("=========================================================");

						Logger.WARNING("tFuelValue2: "+tFuelValue2);
						Logger.WARNING("tConsumed2: "+tConsumed2);
						Logger.WARNING("mFluid2.name: "+this.mFluid2.getFluid().getName());
						Logger.WARNING("mFluid2.amount: "+this.mFluid2.amount);
						Logger.WARNING("mFluid2.amount > tConsumed2: "+(this.mFluid2.amount > tConsumed2));
						long tFluidAmountToUse = Math.min(this.mFluid.amount / tConsumed, (((this.maxEUOutput() * 30) + this.getMinimumStoredEU()) - aBaseMetaTileEntity.getUniversalEnergyStored()) / tFuelValue);
						long tFluidAmountToUse2 = Math.min(this.mFluid2.amount / tConsumed2, (((this.maxEUOutput() * 30) + this.getMinimumStoredEU()) - aBaseMetaTileEntity.getUniversalEnergyStored()) / tFuelValue2);

						if (tFluidAmountToUse <= 0){
							/*if ((mFluid.amount / tConsumed) == getCapacity()){
								tFluidAmountToUse = 1;
							}*/

							if (aBaseMetaTileEntity.getUniversalEnergyStored() <= (aBaseMetaTileEntity.getEUCapacity()-aBaseMetaTileEntity.getUniversalEnergyStored())){
								tFluidAmountToUse = 1;
								Logger.WARNING("=========================================================");
								Logger.WARNING("tFluidAmountToUse - Updated: "+tFluidAmountToUse);
								Logger.WARNING("=========================================================");
							}
						}

						if (tFluidAmountToUse2 <= 0){
							/*if ((mFluid2.amount / tConsumed) == getCapacity()){
								tFluidAmountToUse2 = 1;
							}*/
							if (aBaseMetaTileEntity.getUniversalEnergyStored() <= (aBaseMetaTileEntity.getEUCapacity()-aBaseMetaTileEntity.getUniversalEnergyStored())){
								tFluidAmountToUse2 = 1;
								Logger.WARNING("=========================================================");
								Logger.WARNING("tFluidAmountToUse2 - Updated: "+tFluidAmountToUse2);
								Logger.WARNING("=========================================================");
							}
						}

						Logger.WARNING("=========================================================");
						Logger.WARNING("tFluidAmountToUse: "+tFluidAmountToUse);
						Logger.WARNING("=========================================================");

						/*Utils.LOG_WARNING("mFluid.amount / tConsumed: "+("fluidAmount:"+mFluid.amount)+(" tConsumed:"+tConsumed)+" | "+(mFluid.amount / tConsumed));
						Utils.LOG_WARNING("maxEUOutput() * 20 + getMinimumStoredEU(): "+(maxEUOutput() * 30 + getMinimumStoredEU()));
						Utils.LOG_WARNING("maxEUOutput(): "+maxEUOutput());
						Utils.LOG_WARNING("maxEUOutput() * 20: "+(maxEUOutput() * 30));
						Utils.LOG_WARNING("getMinimumStoredEU(): "+(getMinimumStoredEU()));
						Utils.LOG_WARNING("aBaseMetaTileEntity.getUniversalEnergyStored(): "+(aBaseMetaTileEntity.getUniversalEnergyStored()));
						Utils.LOG_WARNING("(maxEUOutput() * 20 + getMinimumStoredEU() - aBaseMetaTileEntity.getUniversalEnergyStored()): "+((maxEUOutput() * 30 + getMinimumStoredEU() - aBaseMetaTileEntity.getUniversalEnergyStored())));
						Utils.LOG_WARNING("tFuelValue: "+(tFuelValue));
						Utils.LOG_WARNING("(maxEUOutput() * 20 + getMinimumStoredEU() - aBaseMetaTileEntity.getUniversalEnergyStored()) / tFuelValue): "+((maxEUOutput() * 30 + getMinimumStoredEU() - aBaseMetaTileEntity.getUniversalEnergyStored()) / tFuelValue));
						 */

						Logger.WARNING("=========================================================");
						Logger.WARNING("tFluidAmountToUse2: "+tFluidAmountToUse2);
						Logger.WARNING("=========================================================");

						/*Utils.LOG_WARNING("mFluid2.amount / tConsumed2: "+("fluidAmount2:"+mFluid2.amount)+(" tConsumed2:"+tConsumed2)+" | "+(mFluid2.amount / tConsumed2));
						Utils.LOG_WARNING("maxEUOutput() * 20 + getMinimumStoredEU(): "+(maxEUOutput() * 30 + getMinimumStoredEU()));
						Utils.LOG_WARNING("maxEUOutput(): "+maxEUOutput());
						Utils.LOG_WARNING("maxEUOutput() * 20: "+(maxEUOutput() * 30));
						Utils.LOG_WARNING("getMinimumStoredEU(): "+(getMinimumStoredEU()));
						Utils.LOG_WARNING("aBaseMetaTileEntity.getUniversalEnergyStored(): "+(aBaseMetaTileEntity.getUniversalEnergyStored()));
						Utils.LOG_WARNING("(maxEUOutput() * 20 + getMinimumStoredEU() - aBaseMetaTileEntity.getUniversalEnergyStored()): "+((maxEUOutput() * 30 + getMinimumStoredEU() - aBaseMetaTileEntity.getUniversalEnergyStored())));
						Utils.LOG_WARNING("tFuelValue2: "+(tFuelValue2));
						Utils.LOG_WARNING("(maxEUOutput() * 20 + getMinimumStoredEU() - aBaseMetaTileEntity.getUniversalEnergyStored()) / tFuelValue2): "+((maxEUOutput() * 30 + getMinimumStoredEU() - aBaseMetaTileEntity.getUniversalEnergyStored()) / tFuelValue2));
						 */
						if (((tFluidAmountToUse > 0) && aBaseMetaTileEntity.increaseStoredEnergyUnits(tFluidAmountToUse * tFuelValue, true)) && ((tFluidAmountToUse2 > 0) && aBaseMetaTileEntity.increaseStoredEnergyUnits(tFluidAmountToUse2 * tFuelValue2, true))){

							Logger.WARNING("tFuelValue: "+tFuelValue);
							Logger.WARNING("tConsumed: "+tConsumed);
							Logger.WARNING("mFluid.name: "+this.mFluid.getFluid().getName());
							Logger.WARNING("mFluid.amount: "+this.mFluid.amount);
							Logger.WARNING("mFluid.amount > tConsumed: "+(this.mFluid.amount > tConsumed));

							Logger.WARNING("=========================================================");

							Logger.WARNING("tFuelValue2: "+tFuelValue2);
							Logger.WARNING("tConsumed2: "+tConsumed2);
							Logger.WARNING("mFluid2.name: "+this.mFluid2.getFluid().getName());
							Logger.WARNING("mFluid2.amount: "+this.mFluid2.amount);
							Logger.WARNING("mFluid2.amount > tConsumed2: "+(this.mFluid2.amount > tConsumed2));

							if (this.useFuel){
								this.mFluid.amount -= tFluidAmountToUse * tConsumed;
								this.mFluid2.amount -= tFluidAmountToUse2 * tConsumed2;
								this.useFuel = false;
							}
							else {
								this.useFuel = true;
							}

						}
						else {
							Logger.WARNING("=========================================================");
							Logger.WARNING("Either tFluidAmountToUse1 <= 0, power cannot be increased of tFluidAmountToUse2 <= 0");
							Logger.WARNING("tFluidAmountToUse1: "+tFluidAmountToUse);
							Logger.WARNING("tFluidAmountToUse2: "+tFluidAmountToUse2);
						}
					}
					else {
						/*Utils.LOG_WARNING("(tFuelValue > 0 && tConsumed > 0 && mFluid.amount > tConsumed) && (tFuelValue2 > 0 && tConsumed2 > 0 && mFluid2.amount > tConsumed2)");
                		Utils.LOG_WARNING("tFuelValue: "+tFuelValue);
                		Utils.LOG_WARNING("tConsumed: "+tConsumed);
                		Utils.LOG_WARNING("mFluid.amount: "+mFluid.amount);
                		Utils.LOG_WARNING("mFluid.amount > tConsumed: "+(mFluid.amount > tConsumed));

                		Utils.LOG_WARNING("=========================================================");

                		Utils.LOG_WARNING("tFuelValue2: "+tFuelValue2);
                		Utils.LOG_WARNING("tConsumed2: "+tConsumed2);
                		Utils.LOG_WARNING("mFluid2.amount: "+mFluid2.amount);
                		Utils.LOG_WARNING("mFluid2.amount > tConsumed2: "+(mFluid2.amount > tConsumed2)); */
					}
				}
				else {
					Logger.WARNING("One mFluid is null");
					if (this.mFluid != null) {
						Logger.WARNING("mFluid1 is not null");
					}
					if (this.mFluid2 != null) {
						Logger.WARNING("mFluid2 is not null");
					}
				}
			}
			if ((this.mInventory[this.getInputSlot()] != null) && (aBaseMetaTileEntity.getUniversalEnergyStored() < ((this.maxEUOutput() * 20) + this.getMinimumStoredEU())) && (GT_Utility.getFluidForFilledItem(this.mInventory[this.getInputSlot()], true) == null)) {
				final int tFuelValue = this.getFuelValue(this.mInventory[this.getInputSlot()]);
				if (tFuelValue > 0) {
					final ItemStack tEmptyContainer = this.getEmptyContainer(this.mInventory[this.getInputSlot()]);
					if (aBaseMetaTileEntity.addStackToSlot(this.getOutputSlot(), tEmptyContainer)) {
						aBaseMetaTileEntity.increaseStoredEnergyUnits(tFuelValue, true);
						aBaseMetaTileEntity.decrStackSize(this.getInputSlot(), 1);
					}
				}
			}
		}

		if (aBaseMetaTileEntity.isServerSide()) {
			aBaseMetaTileEntity.setActive(aBaseMetaTileEntity.isAllowedToWork() && (aBaseMetaTileEntity.getUniversalEnergyStored() >= (this.maxEUOutput() + this.getMinimumStoredEU())));
		}
	}

	public abstract GT_Recipe_Map getRecipes();

	public abstract int getEfficiency();

	public int consumedFluidPerOperation(final FluidStack aLiquid) {
		return 1;
	}

	public int getFuelValue(final FluidStack aLiquid) {
		if ((aLiquid == null) || (this.getRecipes() == null)) {
			return 0;
		}
		FluidStack tLiquid;
		final Collection<GT_Recipe> tRecipeList = this.getRecipes().mRecipeList;
		if (tRecipeList != null) {
			for (final GT_Recipe tFuel : tRecipeList) {
				if ((tLiquid = GT_Utility.getFluidForFilledItem(tFuel.getRepresentativeInput(0), true)) != null) {
					if (aLiquid.isFluidEqual(tLiquid)) {
						return (int) (((long) tFuel.mSpecialValue * this.getEfficiency() * this.consumedFluidPerOperation(tLiquid)) / 100);
					}
				}
			}
		}
		return 0;
	}

	public int getFuelValue(final ItemStack aStack) {
		if (GT_Utility.isStackInvalid(aStack) || (this.getRecipes() == null)) {
			return 0;
		}
		final GT_Recipe tFuel = this.getRecipes().findRecipe(this.getBaseMetaTileEntity(), false, Long.MAX_VALUE, null, aStack);
		if (tFuel != null) {
			return (int) ((tFuel.mSpecialValue * 1000L * this.getEfficiency()) / 100);
		}
		return 0;
	}

	public ItemStack getEmptyContainer(final ItemStack aStack) {
		if (GT_Utility.isStackInvalid(aStack) || (this.getRecipes() == null)) {
			return null;
		}
		final GT_Recipe tFuel = this.getRecipes().findRecipe(this.getBaseMetaTileEntity(), false, Long.MAX_VALUE, null, aStack);
		if (tFuel != null) {
			return GT_Utility.copy(tFuel.getOutput(0));
		}
		return GT_Utility.getContainerItem(aStack, true);
	}

	@Override
	public boolean allowPutStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex, final byte aSide, final ItemStack aStack) {
		return super.allowPutStack(aBaseMetaTileEntity, aIndex, aSide, aStack) && ((this.getFuelValue(aStack) > 0) || (this.getFuelValue(GT_Utility.getFluidForFilledItem(aStack, true)) > 0));
	}

	@Override
	public int getCapacity() {
		return 32000;
	}

	@Override
	public int getTankPressure() {
		return -100;
	}
}
