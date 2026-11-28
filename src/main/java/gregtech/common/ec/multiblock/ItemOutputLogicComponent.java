package gregtech.common.ec.multiblock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.api.ec.component.multiblock.IHatchConfigCopyableComponent;
import gregtech.api.ec.component.multiblock.IMultiblockItemOutputLogicComponent;
import gregtech.api.interfaces.IOutputBus;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchOutputBus;
import gregtech.api.metatileentity.implementations.MTEHatchVoidBus;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ItemEjectionHelper;
import gregtech.common.ec.multiblock.internal.BaseHatchContainerComponent;
import gregtech.common.tileentities.machines.outputme.MTEHatchOutputBusME;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchSteamBusOutput;

public class ItemOutputLogicComponent extends BaseHatchContainerComponent
    implements IMultiblockItemOutputLogicComponent, IHatchConfigCopyableComponent {

    private final List<MTEHatchOutputBus> mOutputBusses = new ArrayList<>();

    @Override
    public boolean canDumpItemToME(@NotNull List<GTUtility.ItemId> outputs) {
        List<MTEHatchOutputBusME> filteredBusses = new ArrayList<>();
        for (var bus : getValidItemOutputHatches()) {
            if (bus instanceof MTEHatchOutputBusME meOutput) {
                if (!meOutput.hasPhysicalSpace() || meOutput.getCheckMode()) continue;
                if (!meOutput.isFiltered()) return true;
                filteredBusses.add(meOutput);
            }
        }
        if (filteredBusses.isEmpty()) return false;
        for (GTUtility.ItemId output : outputs) {
            boolean handled = false;
            for (MTEHatchOutputBusME meOutput : filteredBusses) {
                // If the bus is unfiltered or is filtered to this item, we can eject the stack fully
                // We don't care about bus ordering here because we're just checking if it's possible
                if (meOutput.isFilteredToItem(output)) {
                    handled = true;
                    break;
                }
            }
            if (!handled) return false;
        }
        return true;
    }

    @Override
    public @NotNull List<IOutputBus> getOutputBusses() {
        if (mOutputBusses.isEmpty()) return Collections.emptyList();
        List<IOutputBus> totalBusses = new ArrayList<>(mOutputBusses.size());
        for (var bus : mOutputBusses) {
            if (bus == null || !bus.isValid()) continue;
            if (bus instanceof MTEHatchVoidBus voidBus && !voidBus.isLocked()) continue;
            totalBusses.add(bus);
        }
        return totalBusses;
    }

    @Override
    public boolean addOutputAtomic(@NotNull ItemStack stack, boolean protectExcess) {
        if (GTUtility.isStackInvalid(stack)) return false;
        if (stack.stackSize == 0) return true;

        var ejectionHelper = new ItemEjectionHelper(getOutputBusses(), protectExcess);
        var list = Collections.singletonList(GTUtility.copyAmountUnsafe(1, stack));
        int ejected = ejectionHelper.ejectItems(list, stack.stackSize);

        if (stack.stackSize == ejected) {
            ejectionHelper.commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean addOutputAtomic(@NotNull List<ItemStack> stacks, boolean protectExcess) {
        if (stacks.isEmpty()) return true;
        var ejectionHelper = new ItemEjectionHelper(getOutputBusses(), protectExcess);
        int ejected = ejectionHelper.ejectItems(stacks, 1, null);
        if (ejected == 1) {
            ejectionHelper.commit();
            return true;
        }
        return false;
    }

    @Override
    public int addOutputPartial(@NotNull ItemStack stack, boolean protectExcess) {
        if (!GTUtility.isStackValid(stack) || stack.stackSize == 0) return 0;

        var ejectionHelper = new ItemEjectionHelper(getOutputBusses(), protectExcess);
        var list = Collections.singletonList(GTUtility.copyAmount(1, stack));
        int ejected = ejectionHelper.ejectItems(list, stack.stackSize);
        ejectionHelper.commit();
        return ejected;
    }

    @Override
    public boolean addOutputPartial(@NotNull List<ItemStack> stacks, boolean protectExcess,
        @Nullable List<ItemStack> remaining) {
        if (stacks.isEmpty()) return true;
        ItemEjectionHelper ejectionHelper = new ItemEjectionHelper(getOutputBusses(), protectExcess);
        int ejected = ejectionHelper.ejectItems(stacks, 1, remaining);
        ejectionHelper.commit();

        return ejected == 1;
    }

    @Override
    public @NotNull List<MTEHatchOutputBus> getItemOutputHatches() {
        return mOutputBusses;
    }

    @Override
    public boolean tryAttachItemOutput(@NotNull IMetaTileEntity metaTile, int baseCasingIndex) {
        if (metaTile instanceof MTEHatchSteamBusOutput) {
            return false;
        }
        if (metaTile instanceof MTEHatchOutputBus hatch) {
            if (!mOutputBusses.contains(hatch)) {
                onHatchAttached(hatch, baseCasingIndex);
                mOutputBusses.add(hatch);
            }
            return true;
        }
        return false;
    }

    @Override
    public void detachAllHatches() {
        clearCraftingIcons(mOutputBusses);
        mOutputBusses.clear();
    }

    @Override
    public void explodeAllHatches(long power) {
        for (MetaTileEntity tTileEntity : getValidItemOutputHatches()) {
            tTileEntity.getBaseMetaTileEntity()
                .doExplosion(power);
        }
    }

    @Override
    public @NotNull Map<String, @NotNull Collection<? extends MTEHatch>> getCopyableHatches() {
        return Collections.singletonMap("mOutputBusses", mOutputBusses);
    }
}
