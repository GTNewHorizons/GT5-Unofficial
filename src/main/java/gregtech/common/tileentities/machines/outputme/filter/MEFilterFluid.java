package gregtech.common.tileentities.machines.outputme.filter;

import java.util.HashSet;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fluids.FluidStack;

import appeng.api.storage.data.IAEFluidStack;
import appeng.api.storage.data.IAEStack;
import appeng.util.item.AEFluidStack;
import gregtech.api.util.GTUtility;

public class MEFilterFluid extends MEFilterBase<IAEFluidStack, GTUtility.FluidId, FluidStack> {

    public MEFilterFluid() {
        super(new HashSet<>());
    }

    @Override
    protected GTUtility.FluidId extractElement(FluidStack stack) {
        return GTUtility.FluidId.create(stack);
    }

    @Override
    protected boolean isCorrectType(IAEStack<?> stack) {
        return stack instanceof IAEFluidStack;
    }

    @Override
    public void onLoadNBTData(NBTTagCompound aNBT) {
        NBTBase lockedFluidsTag = aNBT.getTag("lockedFluids");

        if (lockedFluidsTag instanceof NBTTagList lockedFluidsList) {
            for (int i = 0; i < lockedFluidsList.tagCount(); i++) {
                NBTTagCompound fluidTag = lockedFluidsList.getCompoundTagAt(i);
                lockedElements.add(GTUtility.FluidId.create(GTUtility.loadFluid(fluidTag)));
            }
        }
    }

    @Override
    public void onSaveNBTData(NBTTagCompound aNBT) {
        NBTTagList lockedFluidsTag = new NBTTagList();

        for (GTUtility.FluidId fluid : lockedElements) {
            NBTTagCompound fluidTag = new NBTTagCompound();
            fluid.getFluidStack()
                .writeToNBT(fluidTag);
            lockedFluidsTag.appendTag(fluidTag);
        }

        aNBT.setTag("lockedFluids", lockedFluidsTag);
    }

    @Override
    public String getEnableKey() {
        return "GT5U.hatch.fluid.filter.enable";
    }

    @Override
    public String getDisableKey() {
        return "GT5U.hatch.fluid.filter.disable";
    }

    @Override
    public FluidStack toNative(IAEFluidStack aeStack) {
        return aeStack.getFluidStack();
    }

    @Override
    public IAEFluidStack fromNative(FluidStack stack) {
        return AEFluidStack.create(stack);
    }

    public boolean isFilteredToFluid(GTUtility.FluidId id) {
        if (!isFiltered()) {
            return true;
        }
        return isBlacklist ^ lockedElements.contains(id);
    }
}
