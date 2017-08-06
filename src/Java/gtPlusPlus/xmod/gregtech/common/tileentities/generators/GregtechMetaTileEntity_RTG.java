package gtPlusPlus.xmod.gregtech.common.tileentities.generators;

import java.lang.reflect.Field;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicGenerator;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.Recipe_GT;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.PollutionUtils;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMetaTileEntity_RTG extends GT_MetaTileEntity_BasicGenerator {
	public int mEfficiency;
	private int mDays;
	private long mTicksToBurnFor;
	private int mVoltage = 0;
	private GT_Recipe mCurrentRecipe;
	private int mDaysRemaining = 0;
	private int mDayTick = 0;

	public int removeDayOfTime(){
		if (this.mDaysRemaining > 0){
			return this.mDaysRemaining--;
		}
		return this.mDaysRemaining;
	}

	//Generates fuel value based on MC days
	public static int convertDaysToTicks(float days){
		int value = 0;
		value = MathUtils.roundToClosestInt(20*86400*days);
		return value;
	}

	public static long getTotalEUGenerated(int ticks, int voltage){
		return ticks*voltage;
	}

	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		super.saveNBTData(aNBT);
		aNBT.setLong("mTicksToBurnFor", this.mTicksToBurnFor);
		aNBT.setInteger("mVoltage", this.mVoltage);
		aNBT.setInteger("mDaysRemaining", this.mDaysRemaining);
		aNBT.setInteger("mDayTick", this.mDayTick);
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		super.loadNBTData(aNBT);
		//this.mMachineBlock = aNBT.getByte("mMachineBlock");
		this.mTicksToBurnFor = aNBT.getLong("mTicksToBurnFor");
		this.mVoltage = aNBT.getInteger("mVoltage");
		this.mDaysRemaining = aNBT.getInteger("mDaysRemaining");
		this.mDayTick = aNBT.getInteger("mDayTick");
	}

	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		if (aBaseMetaTileEntity.isServerSide()){
			if (this.mDayTick < 24000){
				this.mDayTick++;
			}
			else if (this.mDayTick >= 24000){
				this.mDayTick = 0;
				this.mDaysRemaining = this.removeDayOfTime();
			}
		}


		if ((aBaseMetaTileEntity.isServerSide()) && (aBaseMetaTileEntity.isAllowedToWork()) && (aTick % 10L == 0L)) {
			long tProducedEU = 0L;
			//Utils.LOG_INFO("Output Voltage:"+this.getOutputTier()+"eu/t");
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

	@Override
	public String[] getDescription() {
		return new String[]{this.mDescription,
				"Fuel is measured in minecraft days",
				"RTG changes output voltage depending on fuel",
				"Generates power at " + this.getEfficiency() + "% Efficiency per tick",
				"Output Voltage: "+this.getOutputTier()+" EU/t",
				CORE.GT_Tooltip};
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
				new GT_RenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_TOP) };
	}

	public ITexture[] getBack(byte aColor) {
		return new ITexture[] { super.getBack(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_TOP) };
	}

	public ITexture[] getBottom(byte aColor) {
		return new ITexture[] { super.getBottom(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_TOP) };
	}

	public ITexture[] getTop(byte aColor) {
		return new ITexture[] { super.getTop(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_FLUID_SIDE) };
	}

	public ITexture[] getSides(byte aColor) {
		return new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_TOP_ACTIVE) ,gregtech.api.enums.Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[this.mTier]};
	}

	public ITexture[] getFrontActive(byte aColor) {
		return new ITexture[] { super.getFrontActive(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_TOP_ACTIVE) };
	}

	public ITexture[] getBackActive(byte aColor) {
		return new ITexture[] { super.getBackActive(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_TOP_ACTIVE) };
	}

	public ITexture[] getBottomActive(byte aColor) {
		return new ITexture[] { super.getBottomActive(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_TOP_ACTIVE) };
	}

	public ITexture[] getTopActive(byte aColor) {
		return new ITexture[] { super.getTopActive(aColor)[0],
				new GT_RenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_FLUID_SIDE_ACTIVE) };
	}

	public ITexture[] getSidesActive(byte aColor) {
		return new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_TOP_ACTIVE) ,gregtech.api.enums.Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[this.mTier]};
	}

	public int getPollution() {
		return 0;
	}

	public int getFuelValue(ItemStack aStack) {
		if ((GT_Utility.isStackInvalid(aStack)) || (getRecipes() == null))
			return 0;
		GT_Recipe tFuel = getRecipes().findRecipe(getBaseMetaTileEntity(), false, 9223372036854775807L, null,
				new ItemStack[] { aStack });
		if (tFuel != null){
			this.mCurrentRecipe = tFuel;
			int voltage = tFuel.mEUt;
			this.mVoltage = voltage;
			int sfsf = this.mTier;
			this.mDaysRemaining = tFuel.mSpecialValue*365;

			//Do some voodoo.
			int mTier2;
			//mTier2 = ReflectionUtils.getField(this.getClass(), "mTier");
			try {
				if (this.mCurrentRecipe.mInputs[0] == GregtechItemList.Pellet_RTG_AM241.get(1)){
					mTier2 = 1;
				}
				else if (this.mCurrentRecipe.mInputs[0] == GregtechItemList.Pellet_RTG_PO210.get(1)){
					mTier2 = 3;
				}
				else if (this.mCurrentRecipe.mInputs[0] == GregtechItemList.Pellet_RTG_PU238.get(1)){
					mTier2 = 2;
				}
				else if (this.mCurrentRecipe.mInputs[0] == GregtechItemList.Pellet_RTG_SR90.get(1)){
					mTier2 = 1;
				}
				else {
					mTier2 = 0;
				}
				ReflectionUtils.setFieldValue(this.getClass(), "mTier", mTier2);
				//ReflectionUtils.setFinalStatic(mTier2, GT_Values.V[0]);
			} catch (Exception e) {
				Utils.LOG_INFO("Failed setting mTier.");
				e.printStackTrace();
			}

			this.mTicksToBurnFor = getTotalEUGenerated(convertDaysToTicks(tFuel.mSpecialValue*365), voltage);
			if (mTicksToBurnFor >= Integer.MAX_VALUE){
				mTicksToBurnFor = Integer.MAX_VALUE;
				Utils.LOG_INFO("Fuel went over Int limit, setting to MAX_VALUE.");
			}
			return (int) (mTicksToBurnFor * getEfficiency() / 100L);
			//return (int) (tFuel.mSpecialValue * 365L * getEfficiency() / 100L);
			//return tFuel.mEUt;
		}
		return 0;
	}

	@Override
	public long maxEUOutput() {
		return ((getBaseMetaTileEntity().isAllowedToWork()) ? this.mVoltage : 0L);
	}

	@Override
	public long getOutputTier() {
		//Utils.LOG_INFO(""+this.mVoltage + " | " + (this.mCurrentRecipe == null));
		if (this.mCurrentRecipe != null){
			return this.mVoltage = this.mCurrentRecipe.mEUt;
		}
		//Utils.LOG_INFO("x");
		return 0;
	}

	@Override
	public boolean isGivingInformation() {
		return true;
	}

	@Override
	public String[] getInfoData() {
		return new String[] { "RTG", "Active:"+this.getBaseMetaTileEntity().isActive(), "Current Output: " + this.mVoltage + " EU/t",
				"Days of Fuel remaining: "+this.mDaysRemaining*365,
				"Hours of Fuel remaining: "+(this.mDaysRemaining*365/3f),
				"Current Recipe input: "+ this.mCurrentRecipe != null ? this.mCurrentRecipe.mInputs[0].getDisplayName() + " x1" : "NUll"
		};
	}

}