package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.GT_Values.V;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.IFluidHandler;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GT_UIInfos;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.maps.FuelBackend;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Pollution;

public abstract class GT_MetaTileEntity_BasicGenerator extends GT_MetaTileEntity_BasicTank {

    public GT_MetaTileEntity_BasicGenerator(int aID, String aName, String aNameRegional, int aTier, String aDescription,
        ITexture... aTextures) {
        super(aID, aName, aNameRegional, aTier, 3, aDescription, aTextures);
    }

    public GT_MetaTileEntity_BasicGenerator(int aID, String aName, String aNameRegional, int aTier,
        String[] aDescription, ITexture... aTextures) {
        super(aID, aName, aNameRegional, aTier, 3, aDescription, aTextures);
    }

    public GT_MetaTileEntity_BasicGenerator(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 3, aDescription, aTextures);
    }

    public GT_MetaTileEntity_BasicGenerator(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 3, aDescription, aTextures);
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[10][17][];
        for (byte i = -1; i < 16; i++) {
            rTextures[0][i + 1] = getFront(i);
            rTextures[1][i + 1] = getBack(i);
            rTextures[2][i + 1] = getBottom(i);
            rTextures[3][i + 1] = getTop(i);
            rTextures[4][i + 1] = getSides(i);
            rTextures[5][i + 1] = getFrontActive(i);
            rTextures[6][i + 1] = getBackActive(i);
            rTextures[7][i + 1] = getBottomActive(i);
            rTextures[8][i + 1] = getTopActive(i);
            rTextures[9][i + 1] = getSidesActive(i);
        }
        return rTextures;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        return mTextures[(active ? 5 : 0) + (side == facingDirection ? 0
            : side == facingDirection.getOpposite() ? 1
                : side == ForgeDirection.DOWN ? 2 : side == ForgeDirection.UP ? 3 : 4)][colorIndex + 1];
    }

    @Override
    public String[] getDescription() {
        String[] desc = new String[mDescriptionArray.length + 1];
        System.arraycopy(mDescriptionArray, 0, desc, 0, mDescriptionArray.length);
        desc[mDescriptionArray.length] = "Fuel Efficiency: " + getEfficiency() + "%";
        return desc;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        GT_UIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
        return true;
    }

    public ITexture[] getFront(byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][aColor + 1] };
    }

    public ITexture[] getBack(byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][aColor + 1] };
    }

    public ITexture[] getBottom(byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][aColor + 1] };
    }

    public ITexture[] getTop(byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][aColor + 1] };
    }

    public ITexture[] getSides(byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][aColor + 1] };
    }

    public ITexture[] getFrontActive(byte aColor) {
        return getFront(aColor);
    }

    public ITexture[] getBackActive(byte aColor) {
        return getBack(aColor);
    }

    public ITexture[] getBottomActive(byte aColor) {
        return getBottom(aColor);
    }

    public ITexture[] getTopActive(byte aColor) {
        return getTop(aColor);
    }

    public ITexture[] getSidesActive(byte aColor) {
        return getSides(aColor);
    }

    @Override
    public boolean isFacingValid(ForgeDirection side) {
        return true;
    }

    @Override
    public boolean isSimpleMachine() {
        return false;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return aIndex < 2;
    }

    @Override
    public boolean isEnetOutput() {
        return true;
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public long maxEUOutput() {
        return getBaseMetaTileEntity().isAllowedToWork() ? V[mTier] : 0L;
    }

    @Override
    public long maxEUStore() {
        return Math.max(getEUVar(), V[mTier] * 80L + getMinimumStoredEU());
    }

    @Override
    public boolean doesFillContainers() {
        return getBaseMetaTileEntity().isAllowedToWork();
        // return false;
    }

    @Override
    public boolean doesEmptyContainers() {
        return getBaseMetaTileEntity().isAllowedToWork();
    }

    @Override
    public boolean canTankBeFilled() {
        return getBaseMetaTileEntity().isAllowedToWork();
    }

    @Override
    public boolean canTankBeEmptied() {
        return getBaseMetaTileEntity().isAllowedToWork();
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
    public boolean isFluidInputAllowed(FluidStack aFluid) {
        return getFuelValue(aFluid) > 0;
    }

    @Override
    public boolean isLiquidOutput(ForgeDirection side) {
        // return super.isLiquidOutput(aSide);
        return false;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide() && aBaseMetaTileEntity.isAllowedToWork() && aTick % 10 == 0) {
            if (mFluid != null) {
                long tFuelValue = getFuelValue(mFluid), tConsumed = consumedFluidPerOperation(mFluid);
                if (tFuelValue > 0 && tConsumed > 0 && mFluid.amount >= tConsumed) {
                    long tFluidAmountToUse = Math.min(
                        mFluid.amount / tConsumed,
                        (maxEUStore() - aBaseMetaTileEntity.getUniversalEnergyStored()) / tFuelValue);
                    if (tFluidAmountToUse > 0
                        && aBaseMetaTileEntity.increaseStoredEnergyUnits(tFluidAmountToUse * tFuelValue, true)) {
                        // divided by two because this is called every 10 ticks, not 20
                        GT_Pollution.addPollution(getBaseMetaTileEntity(), getPollution() / 2);
                        mFluid.amount -= tFluidAmountToUse * tConsumed;
                    }
                }
            }

            if (mInventory[getInputSlot()] != null
                && aBaseMetaTileEntity.getUniversalEnergyStored() < (maxEUOutput() * 20 + getMinimumStoredEU())
                && ((GT_Utility.getFluidForFilledItem(mInventory[getInputSlot()], true) != null)
                    || solidFuelOverride(mInventory[getInputSlot()]))) {
                long tFuelValue = getFuelValue(mInventory[getInputSlot()]);
                if (tFuelValue <= 0) tFuelValue = getFuelValue(mInventory[getInputSlot()], true);
                // System.out.println(" tFuelValue : " + tFuelValue );
                if (tFuelValue > 0) {
                    ItemStack tEmptyContainer = getEmptyContainer(mInventory[getInputSlot()]);
                    if (aBaseMetaTileEntity.addStackToSlot(getOutputSlot(), tEmptyContainer)) {
                        aBaseMetaTileEntity.increaseStoredEnergyUnits(tFuelValue, true);
                        aBaseMetaTileEntity.decrStackSize(getInputSlot(), 1);
                        // divided by two because this is called every 10 ticks, not 20
                        GT_Pollution.addPollution(getBaseMetaTileEntity(), getPollution() / 2);
                    }
                }
            }
        }

        if (aBaseMetaTileEntity.isServerSide()) aBaseMetaTileEntity.setActive(
            aBaseMetaTileEntity.isAllowedToWork()
                && aBaseMetaTileEntity.getUniversalEnergyStored() >= maxEUOutput() + getMinimumStoredEU());
    }

    /**
     * @param stack the fuel stack
     * @return if the stack is a solid fuel
     */
    public boolean solidFuelOverride(ItemStack stack) {
        // this could be used for a coal generator for example aswell...
        ItemData association = GT_OreDictUnificator.getAssociation(stack);
        // if it is a gregtech Item, make sure its not a VOLUMETRIC_FLASK or any type of cell, else do vanilla checks
        if (association != null) {
            return !OrePrefixes.CELL_TYPES.contains(association.mPrefix)
                && !GT_Utility.areStacksEqual(ItemList.VOLUMETRIC_FLASK.get(1L), stack, true);
        } else {
            return stack != null && // when the stack is null its not a solid
                stack.getItem() != null && // when the item in the stack is null its not a solid
                !(stack.getItem() instanceof IFluidContainerItem) && // when the item is a fluid container its not a
                                                                     // solid...
                !(stack.getItem() instanceof IFluidHandler) && // when the item is a fluid handler its not a
                                                               // solid...
                !stack.getItem()
                    .getUnlocalizedName()
                    .contains("bucket"); // since we cant really check for
                                         // buckets...
        }
    }

    public abstract int getPollution();

    public abstract RecipeMap<?> getRecipes();

    public abstract int getEfficiency();

    public int consumedFluidPerOperation(FluidStack aLiquid) {
        return 1;
    }

    public int getFuelValue(FluidStack aLiquid) {
        long value = getFuelValue(aLiquid, true);
        return (value > Integer.MAX_VALUE) ? 0 : (int) value;
    }

    public long getFuelValue(FluidStack aLiquid, boolean aLong) {
        RecipeMap<?> tRecipes = getRecipes();
        if (aLiquid == null || !(tRecipes.getBackend() instanceof FuelBackend tFuels)) return 0;
        GT_Recipe tFuel = tFuels.findFuel(aLiquid);
        if (tFuel == null) return 0;

        return (long) tFuel.mSpecialValue * getEfficiency() * consumedFluidPerOperation(aLiquid) / 100;
    }

    public int getFuelValue(ItemStack aStack) {
        long value = getFuelValue(aStack, true);
        return (value > Integer.MAX_VALUE) ? 0 : (int) value;
    }

    public long getFuelValue(ItemStack aStack, boolean aLong) {
        if (GT_Utility.isStackInvalid(aStack) || getRecipes() == null) return 0;
        GT_Recipe tFuel = getRecipes().findRecipe(getBaseMetaTileEntity(), false, Long.MAX_VALUE, null, aStack);
        if (tFuel == null) return 0;

        long liters = 10L; // 1000mb/100
        return (long) tFuel.mSpecialValue * liters * getEfficiency();
    }

    public ItemStack getEmptyContainer(ItemStack aStack) {
        if (GT_Utility.isStackInvalid(aStack) || getRecipes() == null) return null;
        GT_Recipe tFuel = getRecipes().findRecipe(getBaseMetaTileEntity(), false, Long.MAX_VALUE, null, aStack);
        if (tFuel != null) return GT_Utility.copyOrNull(tFuel.getOutput(0));
        return GT_Utility.getContainerItem(aStack, true);
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return super.allowPutStack(aBaseMetaTileEntity, aIndex, side, aStack) && (getFuelValue(aStack, true) > 0
            || getFuelValue(GT_Utility.getFluidForFilledItem(aStack, true), true) > 0);
    }

    @Override
    public int getCapacity() {
        return 16000;
    }

    @Override
    public int getTankPressure() {
        return -100;
    }

    @Override
    public boolean useModularUI() {
        return true;
    }
}
