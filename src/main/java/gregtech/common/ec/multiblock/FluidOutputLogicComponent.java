package gregtech.common.ec.multiblock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.api.ec.component.multiblock.IHatchConfigCopyableComponent;
import gregtech.api.ec.component.multiblock.IMultiblockFluidOutputLogicComponent;
import gregtech.api.interfaces.IOutputHatch;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.metatileentity.implementations.MTEHatchVoid;
import gregtech.api.util.FluidEjectionHelper;
import gregtech.api.util.GTUtility;
import gregtech.common.ec.multiblock.internal.BaseHatchContainerComponent;
import gregtech.common.tileentities.machines.outputme.MTEHatchOutputME;

public class FluidOutputLogicComponent extends BaseHatchContainerComponent
    implements IMultiblockFluidOutputLogicComponent, IHatchConfigCopyableComponent {

    private final List<MTEHatchOutput> mOutputHatches = new ArrayList<>();

    @Override
    public boolean canDumpFluidToME(@NotNull List<GTUtility.FluidId> outputs) {
        List<MTEHatchOutputME> filteredHatches = new ArrayList<>();
        for (var hatch : getValidFluidOutputHatches()) {
            if (hatch instanceof MTEHatchOutputME meOutput) {
                if (!meOutput.hasPhysicalSpace() || meOutput.getCheckMode()) continue;
                if (!meOutput.isFiltered()) return true;
                filteredHatches.add(meOutput);
            }
        }
        if (filteredHatches.isEmpty()) return false;
        for (GTUtility.FluidId output : outputs) {
            boolean handled = false;
            for (MTEHatchOutputME meOutput : filteredHatches) {
                if (meOutput.isFilteredToFluid(output)) {
                    handled = true;
                    break;
                }
            }
            if (!handled) return false;
        }
        return true;
    }

    @Override
    public @NotNull List<IOutputHatch> getOutputHatches(@NotNull List<FluidStack> toOutput) {
        if (mOutputHatches.isEmpty()) return Collections.emptyList();
        List<IOutputHatch> totalHatches = new ArrayList<>(mOutputHatches.size());
        for (var hatch : mOutputHatches) {
            if (hatch == null || !hatch.isValid()) continue;
            if (hatch instanceof MTEHatchVoid voidHatch && !voidHatch.isFluidLocked()) continue;
            totalHatches.add(hatch);
        }
        return totalHatches;
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
    public boolean addOutputPartial(@NotNull List<FluidStack> stacks, boolean protectExcess,
        @Nullable List<@Nullable FluidStack> remaining) {
        if (stacks.isEmpty()) return true;
        FluidEjectionHelper ejectionHelper = new FluidEjectionHelper(getOutputHatches(stacks), protectExcess);
        long ejected = ejectionHelper.ejectFluids(stacks, 1, remaining);
        ejectionHelper.commit();

        return ejected == 1;
    }

    @Override
    public @NotNull List<MTEHatchOutput> getFluidOutputHatches() {
        return mOutputHatches;
    }

    @Override
    public boolean tryAttachFluidOutput(@NotNull IMetaTileEntity metaTile, int baseCasingIndex) {
        if (metaTile instanceof MTEHatchOutput hatch) {
            if (!mOutputHatches.contains(hatch)) {
                onHatchAttached(hatch, baseCasingIndex);
                mOutputHatches.add(hatch);
            }
            return true;
        }
        return false;
    }

    @Override
    public void detachAllHatches() {
        clearCraftingIcons(mOutputHatches);
        mOutputHatches.clear();
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
        return Collections.singletonMap("mOutputHatches", mOutputHatches);
    }
}
