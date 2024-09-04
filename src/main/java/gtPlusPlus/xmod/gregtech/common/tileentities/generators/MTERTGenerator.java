package gtPlusPlus.xmod.gregtech.common.tileentities.generators;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.ArrayUtils;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicGenerator;
import gregtech.api.objects.GTRenderedTexture;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.gregtech.PollutionUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class MTERTGenerator extends MTEBasicGenerator {

    public int mEfficiency;
    private long mTicksToBurnFor;
    private int mVoltage = 0;
    private GTRecipe mCurrentRecipe;
    private int mDaysRemaining = 0;
    private int mDayTick = 0;
    private byte mNewTier = 0;

    public int removeDayOfTime() {
        if (this.mDaysRemaining > 0) {
            return this.mDaysRemaining--;
        }
        return this.mDaysRemaining;
    }

    // Generates fuel value based on MC days
    public static int convertDaysToTicks(float days) {
        int value = 0;
        value = MathUtils.roundToClosestInt(20 * 86400 * days);
        return value;
    }

    public static long getTotalEUGenerated(int ticks, int voltage) {
        return ticks * voltage;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setLong("mTicksToBurnFor", this.mTicksToBurnFor);
        aNBT.setInteger("mVoltage", this.mVoltage);
        aNBT.setInteger("mDaysRemaining", this.mDaysRemaining);
        aNBT.setInteger("mDayTick", this.mDayTick);
        aNBT.setByte("mNewTier", this.mNewTier);

        if (this.mCurrentRecipe != null) {
            final NBTTagList list = new NBTTagList();
            final ItemStack stack = this.mCurrentRecipe.mInputs[0];
            if (stack != null) {
                final NBTTagCompound data = new NBTTagCompound();
                stack.writeToNBT(data);
                data.setInteger("mSlot", 0);
                list.appendTag(data);
            }
            aNBT.setTag("mRecipeItem", list);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.mTicksToBurnFor = aNBT.getLong("mTicksToBurnFor");
        this.mVoltage = aNBT.getInteger("mVoltage");
        this.mDaysRemaining = aNBT.getInteger("mDaysRemaining");
        this.mDayTick = aNBT.getInteger("mDayTick");
        this.mNewTier = aNBT.getByte("mNewTier");

        try {
            ReflectionUtils.setByte(this, "mTier", this.mNewTier);
        } catch (Exception e) {
            if (this.getBaseMetaTileEntity() != null) {
                IGregTechTileEntity thisTile = this.getBaseMetaTileEntity();
                if (thisTile.isAllowedToWork() || thisTile.isActive()) {
                    thisTile.setActive(false);
                }
            }
        }

        final NBTTagList list = aNBT.getTagList("mRecipeItem", 10);
        final NBTTagCompound data = list.getCompoundTagAt(0);
        ItemStack lastUsedFuel = ItemStack.loadItemStackFromNBT(data);
        if (lastUsedFuel != null) {
            this.mCurrentRecipe = getRecipeMap().findRecipe(
                getBaseMetaTileEntity(),
                false,
                9223372036854775807L,
                null,
                new ItemStack[] { lastUsedFuel });
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (this.mDayTick < 24000) {
                this.mDayTick++;
            } else if (this.mDayTick >= 24000) {
                this.mDayTick = 0;
                this.mDaysRemaining = this.removeDayOfTime();
            }
        }

        if ((aBaseMetaTileEntity.isServerSide()) && (aBaseMetaTileEntity.isAllowedToWork()) && (aTick % 10L == 0L)) {
            long tProducedEU = 0L;
            if (this.mFluid == null) {
                if (aBaseMetaTileEntity.getUniversalEnergyStored() < maxEUOutput() + getMinimumStoredEU()) {
                    this.mInventory[getStackDisplaySlot()] = null;
                } else {
                    if (this.mInventory[getStackDisplaySlot()] == null)
                        this.mInventory[getStackDisplaySlot()] = new ItemStack(Blocks.fire, 1);
                    this.mInventory[getStackDisplaySlot()].setStackDisplayName(
                        "Generating: "
                            + GTUtility.formatNumbers(
                                aBaseMetaTileEntity.getUniversalEnergyStored() - getMinimumStoredEU())
                            + " EU");
                }
            } else {
                int tFuelValue = getFuelValue(this.mFluid);
                int tConsumed = consumedFluidPerOperation(this.mFluid);
                if ((tFuelValue > 0) && (tConsumed > 0) && (this.mFluid.amount > tConsumed)) {
                    long tFluidAmountToUse = Math.min(
                        this.mFluid.amount / tConsumed,
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
                && (GTUtility.getFluidForFilledItem(this.mInventory[getInputSlot()], true) == null)) {
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
                PollutionUtils
                    .addPollution(aBaseMetaTileEntity, (int) (tProducedEU * getPollution() / 500 * this.mTier + 1L));
            }
        }

        if (aBaseMetaTileEntity.isServerSide()) aBaseMetaTileEntity.setActive(
            (aBaseMetaTileEntity.isAllowedToWork())
                && (aBaseMetaTileEntity.getUniversalEnergyStored() >= maxEUOutput() + getMinimumStoredEU()));
    }

    @Override
    public String[] getDescription() {
        return ArrayUtils.addAll(
            this.mDescriptionArray,
            "Fuel is measured in minecraft days (Check with Scanner)",
            "RTG changes output voltage depending on fuel",
            "Generates power at " + GTUtility.formatNumbers(this.getEfficiency()) + "% Efficiency per tick",
            "Output Voltage: " + GTUtility.formatNumbers(this.getOutputTier()) + " EU/t",
            GTPPCore.GT_Tooltip.get());
    }

    public MTERTGenerator(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, "Requires RTG Pellets", new ITexture[0]);
    }

    private byte getTier() {
        int voltage = this.mVoltage;
        if (voltage >= 512) {
            return 4;
        } else if (voltage >= 128) {
            return 3;
        } else if (voltage >= 32) {
            return 2;
        } else if (voltage >= 8) {
            return 1;
        }
        return 0;
    }

    public MTERTGenerator(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
        return ((side.offsetY == 0) && (side != getBaseMetaTileEntity().getFrontFacing())
            && (side != getBaseMetaTileEntity().getBackFacing()));
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTERTGenerator(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTPPRecipeMaps.rtgFuels;
    }

    @Override
    public int getCapacity() {
        return 0;
    }

    @Override
    public int getEfficiency() {
        return this.mEfficiency = 100;
    }

    @Override
    public ITexture[] getFront(byte aColor) {
        return new ITexture[] { super.getFront(aColor)[0],
            new GTRenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_TOP),
            new GTRenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_MASSFAB) };
    }

    @Override
    public ITexture[] getBack(byte aColor) {
        return new ITexture[] { super.getBack(aColor)[0],
            new GTRenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_TOP) };
    }

    @Override
    public ITexture[] getBottom(byte aColor) {
        return new ITexture[] { super.getBottom(aColor)[0],
            new GTRenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_TOP) };
    }

    @Override
    public ITexture[] getTop(byte aColor) {
        return new ITexture[] { super.getTop(aColor)[0],
            new GTRenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_TOP),
            new GTRenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_FLUID_SIDE) };
    }

    @Override
    public ITexture[] getSides(byte aColor) {
        return new ITexture[] { gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS[this.mTier][(0)],
            new GTRenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_TOP_ACTIVE),
            gregtech.api.enums.Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[getTier()] };
    }

    @Override
    public ITexture[] getFrontActive(byte aColor) {
        return new ITexture[] { super.getFrontActive(aColor)[0],
            new GTRenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_TOP_ACTIVE),
            new GTRenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_MASSFAB_ACTIVE) };
    }

    @Override
    public ITexture[] getBackActive(byte aColor) {
        return new ITexture[] { super.getBackActive(aColor)[0],
            new GTRenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_TOP_ACTIVE) };
    }

    @Override
    public ITexture[] getBottomActive(byte aColor) {
        return new ITexture[] { super.getBottomActive(aColor)[0],
            new GTRenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_TOP_ACTIVE) };
    }

    @Override
    public ITexture[] getTopActive(byte aColor) {
        return new ITexture[] { super.getTopActive(aColor)[0],
            new GTRenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_TOP_ACTIVE),
            new GTRenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_FLUID_SIDE_ACTIVE) };
    }

    @Override
    public ITexture[] getSidesActive(byte aColor) {
        return new ITexture[] { gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS[this.mTier][(0)],
            new GTRenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_TOP_ACTIVE),
            gregtech.api.enums.Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[getTier()] };
    }

    @Override
    public int getPollution() {
        return 0;
    }

    @Override
    public int getFuelValue(ItemStack aStack) {
        if ((GTUtility.isStackInvalid(aStack)) || (getRecipeMap() == null)) return 0;
        GTRecipe tFuel = getRecipeMap()
            .findRecipe(getBaseMetaTileEntity(), false, 9223372036854775807L, null, new ItemStack[] { aStack });
        if (tFuel != null) {
            this.mCurrentRecipe = tFuel;
            int voltage = tFuel.mEUt;
            this.mVoltage = voltage;
            // this.mDaysRemaining = tFuel.mSpecialValue*365;

            // Do some voodoo.
            byte mTier2;
            // mTier2 = ReflectionUtils.getField(this.getClass(), "mTier");
            try {
                if (ItemStack.areItemStacksEqual(tFuel.mInputs[0], GregtechItemList.Pellet_RTG_AM241.get(1))) {
                    mTier2 = 1;
                } else if (ItemStack.areItemStacksEqual(tFuel.mInputs[0], GregtechItemList.Pellet_RTG_PO210.get(1))) {
                    mTier2 = 3;
                } else if (ItemStack.areItemStacksEqual(tFuel.mInputs[0], GregtechItemList.Pellet_RTG_PU238.get(1))) {
                    mTier2 = 2;
                } else if (ItemStack.areItemStacksEqual(tFuel.mInputs[0], GregtechItemList.Pellet_RTG_SR90.get(1))) {
                    mTier2 = 1;
                } else {
                    mTier2 = 0;
                }
                ReflectionUtils.setByte(this, "mTier", mTier2);
                this.mNewTier = mTier2;
            } catch (Exception e) {
                Logger.WARNING("Failed setting mTier.");
                e.printStackTrace();
            }

            this.mTicksToBurnFor = getTotalEUGenerated(convertDaysToTicks(tFuel.mSpecialValue), voltage);
            if (mTicksToBurnFor >= Integer.MAX_VALUE) {
                mTicksToBurnFor = Integer.MAX_VALUE;
                Logger.WARNING("Fuel went over Int limit, setting to MAX_VALUE.");
            }
            this.mDaysRemaining = MathUtils.roundToClosestInt(mTicksToBurnFor / 20 / 60 / 3);
            Logger.WARNING("step | " + (int) (mTicksToBurnFor * getEfficiency() / 100L));
            return (int) (mTicksToBurnFor * getEfficiency() / 100L);
        }
        Logger.WARNING("Not sure");
        return 0;
    }

    @Override
    public long maxEUOutput() {
        return ((getBaseMetaTileEntity().isAllowedToWork()) ? this.mVoltage : 0L);
    }

    @Override
    public long getOutputTier() {
        if (this.mCurrentRecipe != null) {
            return this.mVoltage = this.mCurrentRecipe.mEUt;
        }
        return 0;
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        return new String[] { "RTG - Running at tier " + this.mTier, "Active: " + this.getBaseMetaTileEntity()
            .isActive(), "Current Output: " + GTUtility.formatNumbers(mVoltage) + " EU/t",
            "Days of Fuel remaining: " + GTUtility.formatNumbers(mTicksToBurnFor / 20 / 60 / 20),
            "Hours of Fuel remaining: " + GTUtility.formatNumbers(mTicksToBurnFor / 20 / 60 / 60),
            "Ticks of " + this.mVoltage + "v remaining: " + mTicksToBurnFor,
            this.mCurrentRecipe.mInputs[0].getDisplayName() + " x1" };
    }
}
