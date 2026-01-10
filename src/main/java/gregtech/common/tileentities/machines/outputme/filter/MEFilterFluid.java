package gregtech.common.tileentities.machines.outputme.filter;

import java.util.ArrayList;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fluids.FluidStack;

import appeng.api.storage.data.IAEFluidStack;
import appeng.api.storage.data.IAEStack;
import appeng.util.item.AEFluidStack;

public class MEFilterFluid extends MEFilterBase<IAEFluidStack, String, FluidStack> {

    public MEFilterFluid() {
        super(new ArrayList<String>());
    }

    @Override
    protected String extractElement(FluidStack stack) {
        return stack.getFluid()
            .getName();
    }

    @Override
    protected boolean isCorrectType(IAEStack<?> stack) {
        return stack instanceof IAEFluidStack;
    }

    @Override
    protected String getDisplayName(IAEFluidStack stack) {
        return stack.getFluidStack()
            .getLocalizedName();
    }

    @Override
    public void onLoadNBTData(NBTTagCompound aNBT) {
        NBTBase lockedFluidsTag = aNBT.getTag("lockedFluids");

        if (lockedFluidsTag instanceof NBTTagList lockedFluidsList) {
            for (int i = 0; i < lockedFluidsList.tagCount(); i++) {
                NBTTagCompound fluidTag = lockedFluidsList.getCompoundTagAt(i);
                lockedElements.add(fluidTag.getString("fluid"));
            }
        }
    }

    @Override
    public void onSaveNBTData(NBTTagCompound aNBT) {
        NBTTagList lockedFluidsTag = new NBTTagList();

        for (String fluid : lockedElements) {
            NBTTagCompound fluidTag = new NBTTagCompound();
            fluidTag.setString("fluid", fluid);
            lockedFluidsTag.appendTag(fluidTag);
        }

        aNBT.setTag("lockedFluids", lockedFluidsTag);
    }

    @Override
    public String getEnableKey() {
        return "GT5U.hatch.item.filter.enable";
    }

    @Override
    public String getDisableKey() {
        return "GT5U.hatch.item.filter.disable";
    }

    @Override
    public FluidStack toNative(IAEFluidStack aeStack) {
        return aeStack.getFluidStack();
    }

    @Override
    public IAEFluidStack fromNative(FluidStack stack) {
        return AEFluidStack.create(stack);
    }
}
