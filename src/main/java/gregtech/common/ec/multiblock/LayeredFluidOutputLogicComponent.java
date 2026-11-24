package gregtech.common.ec.multiblock;

import static gregtech.api.util.GTUtility.filterValidMTEs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.api.ec.component.multiblock.IHatchConfigCopyableComponent;
import gregtech.api.ec.component.multiblock.ILayeredFluidOutputLogicComponent;
import gregtech.api.interfaces.IOutputHatch;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.util.FluidEjectionHelper;
import gregtech.api.util.GTUtility;
import gregtech.api.util.OutputHatchWrapper;
import gregtech.common.ec.multiblock.internal.BaseHatchContainerComponent;
import gregtech.common.tileentities.machines.outputme.MTEHatchOutputME;

public class LayeredFluidOutputLogicComponent extends BaseHatchContainerComponent
    implements ILayeredFluidOutputLogicComponent, IHatchConfigCopyableComponent {

    private final List<List<MTEHatchOutput>> mOutputHatchesByLayer = new ArrayList<>();
    private final List<MTEHatchOutput> mAllOutputHatches = new ArrayList<>();

    @Override
    public boolean tryAttachFluidOutput(@NotNull IMetaTileEntity metaTile, int baseCasingIndex, int layerIndex) {
        if (layerIndex < 0) return false;
        if (metaTile instanceof MTEHatchOutput hatch) {
            while (mOutputHatchesByLayer.size() <= layerIndex) {
                mOutputHatchesByLayer.add(new ArrayList<>());
            }
            List<MTEHatchOutput> layerHatches = mOutputHatchesByLayer.get(layerIndex);
            if (!layerHatches.contains(hatch)) {
                onHatchAttached(hatch, baseCasingIndex);
                layerHatches.add(hatch);
                if (!mAllOutputHatches.contains(hatch)) {
                    mAllOutputHatches.add(hatch);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int getLayerHatchCount(int layerIndex) {
        if (layerIndex < 0 || layerIndex >= mOutputHatchesByLayer.size()) return 0;
        return mOutputHatchesByLayer.get(layerIndex)
            .size();
    }

    @Override
    public @NotNull List<List<MTEHatchOutput>> getFluidOutputHatchesByLayers() {
        return mOutputHatchesByLayer;
    }

    @Override
    public @NotNull List<MTEHatchOutput> getFluidOutputHatches() {
        return mAllOutputHatches;
    }

    @Override
    public boolean canDumpFluidToME(@NotNull List<GTUtility.FluidId> outputs) {
        for (int i = 0; i < outputs.size(); i++) {
            if (i >= mOutputHatchesByLayer.size()) {
                // Less layer than recipe size
                return false;
            }
            List<MTEHatchOutputME> hatches = GTUtility
                .getMTEsOfType(mOutputHatchesByLayer.get(i), MTEHatchOutputME.class);
            GTUtility.FluidId output = outputs.get(i);
            boolean handled = false;

            for (MTEHatchOutputME hatch : hatches) {
                if (!hatch.hasPhysicalSpace() || hatch.getCheckMode()) continue;
                if (!hatch.isFiltered() || hatch.isFilteredToFluid(output)) {
                    handled = true;
                    break;
                }
            }
            if (!handled) {
                return false;
            }
        }

        return true;
    }

    @Override
    public @NotNull List<IOutputHatch> getOutputHatches(@NotNull List<FluidStack> toOutput) {
        List<IOutputHatch> ret = new ArrayList<>();
        int layerIndex = 0;
        for (int i = 0; i < toOutput.size(); i++) {
            FluidStack fluidOutputForLayer = toOutput.get(i);
            if (layerIndex >= mOutputHatchesByLayer.size()) {
                break;
            }
            for (MTEHatchOutput hatch : mOutputHatchesByLayer.get(layerIndex)) {
                if (!hatch.isValid()) continue;
                if (fluidOutputForLayer != null) {
                    ret.add(new OutputHatchWrapper(hatch, f -> f.matches(fluidOutputForLayer)));
                } else {
                    ret.add(hatch);
                }
            }
            layerIndex++;
        }
        return ret;
    }

    @Override
    public boolean addOutputAtomic(@NotNull FluidStack stack, boolean protectExcess) {
        if (!GTUtility.isStackValid(stack)) return false;
        long amount = GTUtility.getFluidAmount(stack);
        if (amount == 0) return true;

        FluidEjectionHelper ejectionHelper = new FluidEjectionHelper(
            getOutputHatches(Collections.singletonList(stack)),
            protectExcess);
        var list = Collections.singletonList(GTUtility.copyAmount(1, stack));
        long ejected = ejectionHelper.ejectFluids(list, amount);

        if (amount == ejected) {
            ejectionHelper.commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean addOutputAtomic(@NotNull List<FluidStack> stacks, boolean protectExcess) {
        if (stacks.isEmpty()) return true;
        var ejectionHelper = new FluidEjectionHelper(getOutputHatches(stacks), protectExcess);
        long ejected = ejectionHelper.ejectFluids(stacks, 1, null);
        if (ejected == 1) {
            ejectionHelper.commit();
            return true;
        }
        return false;
    }

    @Override
    public long addOutputPartial(@NotNull FluidStack stack, boolean protectExcess) {
        if (!GTUtility.isStackValid(stack)) return 0;
        long amount = GTUtility.getFluidAmount(stack);
        if (amount == 0) return 0;

        FluidEjectionHelper ejectionHelper = new FluidEjectionHelper(getOutputHatches(List.of(stack)), protectExcess);
        var list = Collections.singletonList(GTUtility.copyAmount(1, stack));
        long ejected = ejectionHelper.ejectFluids(list, amount);
        ejectionHelper.commit();
        return ejected;
    }

    @Override
    public boolean addOutputPartial(@NotNull List<FluidStack> outputFluids, boolean protectExcess,
        @Nullable List<FluidStack> remaining) {
        if (outputFluids.isEmpty()) return true;
        List<FluidStack> layerRemaining = remaining != null ? new ArrayList<>() : null;
        int index = 0;
        boolean succeed = true;

        for (FluidStack stack : outputFluids) {
            if (!GTUtility.isStackValid(stack)) {
                if (remaining != null) remaining.add(null);
            } else if (index >= mOutputHatchesByLayer.size()) {
                if (remaining != null) remaining.add(stack.copy());
                succeed = false;
            } else {
                if (layerRemaining != null) layerRemaining.clear();
                List<MTEHatchOutput> hatches = filterValidMTEs(mOutputHatchesByLayer.get(index));
                FluidEjectionHelper ejectionHelper = new FluidEjectionHelper(hatches, protectExcess);
                succeed &= ejectionHelper.ejectFluids(Collections.singletonList(stack), 1, layerRemaining) == 1;
                ejectionHelper.commit();
                if (remaining != null) {
                    if (layerRemaining.isEmpty()) remaining.add(null);
                    else remaining.addAll(layerRemaining);
                }
            }
            index++;
        }
        if (remaining != null) {
            GTUtility.removeTailingNulls(remaining);
        }
        return succeed;
    }

    @Override
    public void detachAllHatches() {
        clearCraftingIcons(mAllOutputHatches);
        mAllOutputHatches.clear();
        for (var hatches : mOutputHatchesByLayer) {
            hatches.clear();
        }
        mOutputHatchesByLayer.clear();
    }

    @Override
    public void explodeAllHatches(long power) {
        for (MetaTileEntity tTileEntity : getValidFluidOutputHatches()) {
            tTileEntity.getBaseMetaTileEntity()
                .doExplosion(power);
        }
    }

    @Override
    public @NotNull Map<String, @NotNull Collection<? extends MTEHatch>> getCopyableHatches() {
        return Collections.singletonMap("mOutputHatches", mAllOutputHatches);
    }
}
