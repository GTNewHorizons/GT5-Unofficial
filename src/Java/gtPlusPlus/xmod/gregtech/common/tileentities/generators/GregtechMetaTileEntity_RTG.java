package gtPlusPlus.xmod.gregtech.common.tileentities.generators;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicGenerator;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.Recipe_GT;
import gregtech.common.GT_Pollution;
import gtPlusPlus.core.util.PollutionUtils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.ChunkPosition;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMetaTileEntity_RTG extends GT_MetaTileEntity_BasicGenerator {
	public int mEfficiency;
	private int mDays;
	private long mTicksToBurnFor;

	//Generates fuel value based on MC days
	public static int convertDaysToTicks(float days){
		int value = 0;
		value = MathUtils.roundToClosestInt(20*86400*days);
		return value;
	}
	
	
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		if ((aBaseMetaTileEntity.isServerSide()) && (aBaseMetaTileEntity.isAllowedToWork()) && (aTick % 10L == 0L)) {
			long tProducedEU = 0L;
			if (this.mFluid == null) {
				if (aBaseMetaTileEntity.getUniversalEnergyStored() < maxEUOutput() + getMinimumStoredEU()) {
					this.mInventory[getStackDisplaySlot()] = null;
				} else {
					if (this.mInventory[getStackDisplaySlot()] == null)
						this.mInventory[getStackDisplaySlot()] = new ItemStack(Blocks.fire, 1);
					this.mInventory[getStackDisplaySlot()].setStackDisplayName("Generating: "
							+ (aBaseMetaTileEntity.getUniversalEnergyStored() - getMinimumStoredEU()) + " EU");
				}
			} else {
				int tFuelValue = getFuelValue(this.mFluid);
				int tConsumed = consumedFluidPerOperation(this.mFluid);
				if ((tFuelValue > 0) && (tConsumed > 0) && (this.mFluid.amount > tConsumed)) {
					long tFluidAmountToUse = Math.min(this.mFluid.amount / tConsumed,
							(maxEUStore() - aBaseMetaTileEntity.getUniversalEnergyStored()) / tFuelValue);
					if ((tFluidAmountToUse > 0L)
							&& (aBaseMetaTileEntity.increaseStoredEnergyUnits(tFluidAmountToUse * tFuelValue, true))) {
						tProducedEU = tFluidAmountToUse * tFuelValue;
						FluidStack tmp260_257 = this.mFluid;
						tmp260_257.amount = (int) (tmp260_257.amount - (tFluidAmountToUse * tConsumed));
					}
				}
			}
			if ((this.mInventory[getInputSlot()] != null)
					&& (aBaseMetaTileEntity.getUniversalEnergyStored() < maxEUOutput() * 20L + getMinimumStoredEU())
					&& (GT_Utility.getFluidForFilledItem(this.mInventory[getInputSlot()], true) == null)) {
				int tFuelValue = getFuelValue(this.mInventory[getInputSlot()]);
				if (tFuelValue > 0) {
					ItemStack tEmptyContainer = getEmptyContainer(this.mInventory[getInputSlot()]);
					if (aBaseMetaTileEntity.addStackToSlot(getOutputSlot(), tEmptyContainer)) {
						aBaseMetaTileEntity.increaseStoredEnergyUnits(tFuelValue, true);
						aBaseMetaTileEntity.decrStackSize(getInputSlot(), 1);
						tProducedEU = tFuelValue;
					}
				}
			}
			if ((tProducedEU > 0L) && (getPollution() > 0)) {
				PollutionUtils.addPollution(aBaseMetaTileEntity, (int) (tProducedEU * getPollution() / 500 * this.mTier + 1L));
			}
		}

		if (aBaseMetaTileEntity.isServerSide())
			aBaseMetaTileEntity.setActive((aBaseMetaTileEntity.isAllowedToWork())
					&& (aBaseMetaTileEntity.getUniversalEnergyStored() >= maxEUOutput() + getMinimumStoredEU()));
	}


	public GregtechMetaTileEntity_RTG(int aID, String aName, String aNameRegional, int aTier) {
		super(aID, aName, aNameRegional, aTier, "Requires RTG Pellets", new ITexture[0]);
	}

	public GregtechMetaTileEntity_RTG(String aName, int aTier, String aDescription,
			ITexture[][][] aTextures) {
		super(aName, aTier, aDescription, aTextures);
	}

	public boolean isOutputFacing(byte aSide) {
		return ((aSide > 1) && (aSide != getBaseMetaTileEntity().getFrontFacing())
				&& (aSide != getBaseMetaTileEntity().getBackFacing()));
	}

	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_RTG(this.mName, this.mTier, this.mDescription, this.mTextures);
	}

	public GT_Recipe.GT_Recipe_Map getRecipes() {
		return Recipe_GT.Gregtech_Recipe_Map.sRTGFuels;
	}

	public int getCapacity() {
		return 0;
	}

	public int getEfficiency() {
		return this.mEfficiency = 100;
	}

	public ITexture[] getFront(byte aColor) {
		return new ITexture[] { super.getFront(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_FRONT) };
	}

	public ITexture[] getBack(byte aColor) {
		return new ITexture[] { super.getBack(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_BACK) };
	}

	public ITexture[] getBottom(byte aColor) {
		return new ITexture[] { super.getBottom(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_BOTTOM) };
	}

	public ITexture[] getTop(byte aColor) {
		return new ITexture[] { super.getTop(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_TOP) };
	}

	public ITexture[] getSides(byte aColor) {
		return new ITexture[] { super.getSides(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_SIDE) };
	}

	public ITexture[] getFrontActive(byte aColor) {
		return new ITexture[] { super.getFrontActive(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_FRONT_ACTIVE) };
	}

	public ITexture[] getBackActive(byte aColor) {
		return new ITexture[] { super.getBackActive(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_BACK_ACTIVE) };
	}

	public ITexture[] getBottomActive(byte aColor) {
		return new ITexture[] { super.getBottomActive(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_BOTTOM_ACTIVE) };
	}

	public ITexture[] getTopActive(byte aColor) {
		return new ITexture[] { super.getTopActive(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_TOP_ACTIVE) };
	}

	public ITexture[] getSidesActive(byte aColor) {
		return new ITexture[] { super.getSidesActive(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_SIDE_ACTIVE) };
	}

	public int getPollution() {
		return 0;
	}
	
	public int getFuelValue(ItemStack aStack) {
		if ((GT_Utility.isStackInvalid(aStack)) || (getRecipes() == null))
			return 0;
		GT_Recipe tFuel = getRecipes().findRecipe(getBaseMetaTileEntity(), false, 9223372036854775807L, null,
				new ItemStack[] { aStack });
		if (tFuel != null)
			//return (int) (tFuel.mSpecialValue * 365L * getEfficiency() / 100L);
			return tFuel.mEUt;
		return 0;
	}
}