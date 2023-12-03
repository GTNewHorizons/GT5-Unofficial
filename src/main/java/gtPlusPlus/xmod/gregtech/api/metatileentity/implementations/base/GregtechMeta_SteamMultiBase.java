package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base;

import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_Utility.filterValidMTEs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.IGT_HatchAdder;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Steam_BusInput;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Steam_BusOutput;

public abstract class GregtechMeta_SteamMultiBase<T extends GregtechMeta_SteamMultiBase<T>>
        extends GregtechMeta_MultiBlockBase<T> {

    public ArrayList<GT_MetaTileEntity_Hatch_Steam_BusInput> mSteamInputs = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch_Steam_BusOutput> mSteamOutputs = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch_CustomFluidBase> mSteamInputFluids = new ArrayList<>();

    protected static final String TT_steaminputbus = StatCollector.translateToLocal("GTPP.MBTT.SteamInputBus");
    protected static final String TT_steamoutputbus = StatCollector.translateToLocal("GTPP.MBTT.SteamOutputBus");
    protected static final String TT_steamhatch = StatCollector.translateToLocal("GTPP.MBTT.SteamHatch");

    public GregtechMeta_SteamMultiBase(String aName) {
        super(aName);
    }

    public GregtechMeta_SteamMultiBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    @Override
    public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final ForgeDirection side,
            final ForgeDirection facing, final int aColorIndex, final boolean aActive, final boolean aRedstone) {
        if (side == facing) {
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(getCasingTextureIndex()),
                    aActive ? getFrontOverlayActive() : getFrontOverlay() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(getCasingTextureIndex()) };
    }

    protected abstract GT_RenderedTexture getFrontOverlay();

    protected abstract GT_RenderedTexture getFrontOverlayActive();

    private int getCasingTextureIndex() {
        return 10;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(V[1]);
        // We need to trick the GT_ParallelHelper we have enough amps for all recipe parallels.
        logic.setAvailableAmperage(getMaxParallelRecipes());
        logic.setAmperageOC(false);
    }

    public ArrayList<FluidStack> getAllSteamStacks() {
        ArrayList<FluidStack> aFluids = new ArrayList<>();
        FluidStack aSteam = FluidUtils.getSteam(1);
        for (FluidStack aFluid : this.getStoredFluids()) {
            if (aFluid.isFluidEqual(aSteam)) {
                aFluids.add(aFluid);
            }
        }
        return aFluids;
    }

    public int getTotalSteamStored() {
        int aSteam = 0;
        for (FluidStack aFluid : getAllSteamStacks()) {
            aSteam += aFluid.amount;
        }
        return aSteam;
    }

    public boolean tryConsumeSteam(int aAmount) {
        if (getTotalSteamStored() <= 0) {
            return false;
        } else {
            return this.depleteInput(FluidUtils.getSteam(aAmount));
        }
    }

    @Override
    public int getMaxEfficiency(ItemStack arg0) {
        return 0;
    }

    @Override
    public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (this.mUpdate == 1 || this.mStartUpCheck == 1) {
                this.mSteamInputs.clear();
                this.mSteamOutputs.clear();
                this.mSteamInputFluids.clear();
            }
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    /**
     * Called every tick the Machine runs
     */
    @Override
    public boolean onRunningTick(ItemStack aStack) {
        fixAllMaintenanceIssue();
        if (lEUt < 0) {
            long aSteamVal = ((-lEUt * 10000) / Math.max(1000, mEfficiency));
            // Logger.INFO("Trying to drain "+aSteamVal+" steam per tick.");
            if (!tryConsumeSteam((int) aSteamVal)) {
                stopMachine();
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addToMachineList(final IGregTechTileEntity aTileEntity, final int aBaseCasingIndex) {
        if (aTileEntity == null) {
            log("Invalid IGregTechTileEntity");
            return false;
        }
        final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            log("Invalid IMetaTileEntity");
            return false;
        }

        // Use this to determine the correct value, then update the hatch texture after.
        boolean aDidAdd = false;

        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_CustomFluidBase) {
            log("Adding Steam Input Hatch");
            aDidAdd = addToMachineListInternal(mSteamInputFluids, aMetaTileEntity, aBaseCasingIndex);
        } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Steam_BusInput) {
            log(
                    "Trying to set recipe map. Type: "
                            + (getRecipeMap() != null ? getRecipeMap().unlocalizedName : "Null"));
            this.resetRecipeMapForHatch(aTileEntity, getRecipeMap());
            log("Adding Steam Input Bus");
            aDidAdd = addToMachineListInternal(mSteamInputs, aMetaTileEntity, aBaseCasingIndex);
        } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Steam_BusOutput) {
            log("Adding Steam Output Bus");
            aDidAdd = addToMachineListInternal(mSteamOutputs, aMetaTileEntity, aBaseCasingIndex);
        }

        return aDidAdd;
    }

    @Override
    public void stopMachine() {
        super.stopMachine();
    }

    /*
     * Handle I/O with custom hatches
     */

    @Override
    public boolean depleteInput(FluidStack aLiquid) {
        if (aLiquid == null) return false;
        for (GT_MetaTileEntity_Hatch_CustomFluidBase tHatch : filterValidMTEs(mSteamInputFluids)) {
            FluidStack tLiquid = tHatch.getFluid();
            if (tLiquid != null && tLiquid.isFluidEqual(aLiquid)) {
                tLiquid = tHatch.drain(aLiquid.amount, false);
                if (tLiquid != null && tLiquid.amount >= aLiquid.amount) {
                    tLiquid = tHatch.drain(aLiquid.amount, true);
                    return tLiquid != null && tLiquid.amount >= aLiquid.amount;
                }
            }
        }
        return false;
    }

    @Override
    public boolean depleteInput(ItemStack aStack) {
        if (GT_Utility.isStackInvalid(aStack)) return false;
        FluidStack aLiquid = GT_Utility.getFluidForFilledItem(aStack, true);
        if (aLiquid != null) return depleteInput(aLiquid);
        for (GT_MetaTileEntity_Hatch_CustomFluidBase tHatch : filterValidMTEs(mSteamInputFluids)) {
            if (GT_Utility.areStacksEqual(aStack, tHatch.getBaseMetaTileEntity().getStackInSlot(0))) {
                if (tHatch.getBaseMetaTileEntity().getStackInSlot(0).stackSize >= aStack.stackSize) {
                    tHatch.getBaseMetaTileEntity().decrStackSize(0, aStack.stackSize);
                    return true;
                }
            }
        }
        for (GT_MetaTileEntity_Hatch_Steam_BusInput tHatch : filterValidMTEs(mSteamInputs)) {
            tHatch.mRecipeMap = getRecipeMap();
            for (int i = tHatch.getBaseMetaTileEntity().getSizeInventory() - 1; i >= 0; i--) {
                if (GT_Utility.areStacksEqual(aStack, tHatch.getBaseMetaTileEntity().getStackInSlot(i))) {
                    if (tHatch.getBaseMetaTileEntity().getStackInSlot(0).stackSize >= aStack.stackSize) {
                        tHatch.getBaseMetaTileEntity().decrStackSize(0, aStack.stackSize);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public ArrayList<FluidStack> getStoredFluids() {
        ArrayList<FluidStack> rList = new ArrayList<>();
        for (GT_MetaTileEntity_Hatch_CustomFluidBase tHatch : filterValidMTEs(mSteamInputFluids)) {
            if (tHatch.getFillableStack() != null) {
                rList.add(tHatch.getFillableStack());
            }
        }
        return rList;
    }

    @Override
    public ArrayList<ItemStack> getStoredInputs() {
        ArrayList<ItemStack> rList = new ArrayList<>();
        for (GT_MetaTileEntity_Hatch_Steam_BusInput tHatch : filterValidMTEs(mSteamInputs)) {
            tHatch.mRecipeMap = getRecipeMap();
            for (int i = tHatch.getBaseMetaTileEntity().getSizeInventory() - 1; i >= 0; i--) {
                if (tHatch.getBaseMetaTileEntity().getStackInSlot(i) != null) {
                    rList.add(tHatch.getBaseMetaTileEntity().getStackInSlot(i));
                }
            }
        }
        return rList;
    }

    @Override
    public boolean addOutput(ItemStack aStack) {
        if (GT_Utility.isStackInvalid(aStack)) return false;
        aStack = GT_Utility.copy(aStack);
        boolean outputSuccess = true;
        while (outputSuccess && aStack.stackSize > 0) {
            outputSuccess = false;
            ItemStack single = aStack.splitStack(1);
            for (GT_MetaTileEntity_Hatch_Steam_BusOutput tHatch : filterValidMTEs(mSteamOutputs)) {
                if (!outputSuccess) {
                    for (int i = tHatch.getSizeInventory() - 1; i >= 0 && !outputSuccess; i--) {
                        if (tHatch.getBaseMetaTileEntity().addStackToSlot(i, single)) outputSuccess = true;
                    }
                }
            }
            for (GT_MetaTileEntity_Hatch_Output tHatch : filterValidMTEs(mOutputHatches)) {
                if (!outputSuccess && tHatch.outputsItems()) {
                    if (tHatch.getBaseMetaTileEntity().addStackToSlot(1, single)) outputSuccess = true;
                }
            }
        }
        return outputSuccess;
    }

    @Override
    public ArrayList<ItemStack> getStoredOutputs() {
        ArrayList<ItemStack> rList = new ArrayList<>();
        for (GT_MetaTileEntity_Hatch_Steam_BusOutput tHatch : filterValidMTEs(mSteamOutputs)) {
            for (int i = tHatch.getBaseMetaTileEntity().getSizeInventory() - 1; i >= 0; i--) {
                rList.add(tHatch.getBaseMetaTileEntity().getStackInSlot(i));
            }
        }
        return rList;
    }

    @Override
    public List<ItemStack> getItemOutputSlots(ItemStack[] toOutput) {
        List<ItemStack> ret = new ArrayList<>();
        for (final GT_MetaTileEntity_Hatch tBus : filterValidMTEs(mSteamOutputs)) {
            final IInventory tBusInv = tBus.getBaseMetaTileEntity();
            for (int i = 0; i < tBusInv.getSizeInventory(); i++) {
                ret.add(tBus.getStackInSlot(i));
            }
        }
        return ret;
    }

    @Override
    public void updateSlots() {
        for (GT_MetaTileEntity_Hatch_CustomFluidBase tHatch : filterValidMTEs(mSteamInputFluids)) tHatch.updateSlots();
        for (GT_MetaTileEntity_Hatch_Steam_BusInput tHatch : filterValidMTEs(mSteamInputs)) tHatch.updateSlots();
    }

    @Override
    public boolean supportsBatchMode() {
        return false;
    }

    @Override
    public void clearHatches() {
        super.clearHatches();
        mSteamInputFluids.clear();
        mSteamInputs.clear();
        mSteamOutputs.clear();
    }

    @Override
    public boolean resetRecipeMapForAllInputHatches(RecipeMap<?> aMap) {
        boolean ret = super.resetRecipeMapForAllInputHatches(aMap);
        for (GT_MetaTileEntity_Hatch_Steam_BusInput hatch : mSteamInputs) {
            if (resetRecipeMapForHatch(hatch, aMap)) {
                ret = true;
            }
        }
        return ret;
    }

    protected static <T extends GregtechMeta_SteamMultiBase<T>> GT_HatchElementBuilder<T> buildSteamInput(
            Class<T> typeToken) {
        return buildHatchAdder(typeToken).adder(GregtechMeta_SteamMultiBase::addToMachineList).hatchIds(31040)
                .shouldReject(t -> !t.mSteamInputFluids.isEmpty());
    }

    protected enum SteamHatchElement implements IHatchElement<GregtechMeta_SteamMultiBase<?>> {

        InputBus_Steam {

            @Override
            public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
                return Collections.singletonList(GT_MetaTileEntity_Hatch_Steam_BusInput.class);
            }

            @Override
            public long count(GregtechMeta_SteamMultiBase<?> t) {
                return t.mSteamInputs.size();
            }
        },
        OutputBus_Steam {

            @Override
            public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
                return Collections.singletonList(GT_MetaTileEntity_Hatch_Steam_BusOutput.class);
            }

            @Override
            public long count(GregtechMeta_SteamMultiBase<?> t) {
                return t.mSteamOutputs.size();
            }
        },;

        @Override
        public IGT_HatchAdder<? super GregtechMeta_SteamMultiBase<?>> adder() {
            return GregtechMeta_SteamMultiBase::addToMachineList;
        }
    }
}
