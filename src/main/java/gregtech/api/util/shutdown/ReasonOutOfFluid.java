package gregtech.api.util.shutdown;


import java.util.Objects;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import gregtech.api.enums.Materials;

public class ReasonOutOfFluid implements ShutDownReason {

    private FluidStack requiredFluid;

    ReasonOutOfFluid(@NotNull FluidStack requiredFluid) {
        this.requiredFluid = requiredFluid;
    }

    @NotNull
    @Override
    public String getID() {
        return "out_of_fluid";
    }

    @NotNull
    @Override
    public String getDisplayString() {
        return Objects.requireNonNull(
            StatCollector.translateToLocalFormatted(
                "GT5U.gui.text.out_of_fluid",
                requiredFluid.getLocalizedName(),
                formatNumbers(requiredFluid.amount)));
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound tag) {
        return requiredFluid.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(@NotNull NBTTagCompound tag) {
        requiredFluid = FluidStack.loadFluidStackFromNBT(tag);
    }

    @NotNull
    @Override
    public ShutDownReason newInstance() {
        return new ReasonOutOfFluid(Materials.Water.getFluid(0));
    }

    @Override
    public void encode(@NotNull PacketBuffer buffer) {
        buffer.writeInt(requiredFluid.getFluidID());
        buffer.writeInt(requiredFluid.amount);
    }

    @Override
    public void decode(PacketBuffer buffer) {
        int fluidID = buffer.readInt();
        Fluid fluid = FluidRegistry.getFluid(fluidID);
        requiredFluid = new FluidStack(fluid, buffer.readInt());
    }

    @Override
    public boolean wasCritical() {
        return true;
    }

    @Override
    public String getKey() {
        return getID();
    }
}
