package gregtech.api.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fluids.FluidStack;

import com.github.bsideup.jabel.Desugar;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;

/// One fluid output of a running recipe before its chance is rolled: the fluid identity (amount ignored), the amount
/// for the current parallels, and the effective chance on the 10000 scale, where 10000 is 100% and larger values are
/// allowed. Equality compares fluid identity, amount and chance rather than `FluidStack` reference identity.
@Desugar
public record ExpectedFluidOutput(FluidStack stack, long amount, int chance) {

    public double expected() {
        return (double) amount * chance / 10000.0;
    }

    public boolean isChanced() {
        return chance % 10000 != 0;
    }

    public static void write(PacketBuffer buffer, ExpectedFluidOutput output) {
        NetworkUtils.writeFluidStack(buffer, output.stack);
        buffer.writeLong(output.amount);
        buffer.writeInt(output.chance);
    }

    public static ExpectedFluidOutput read(PacketBuffer buffer) {
        FluidStack stack = NetworkUtils.readFluidStack(buffer);
        return new ExpectedFluidOutput(stack, buffer.readLong(), buffer.readInt());
    }

    public NBTTagCompound writeToNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        if (stack != null) tag.setTag("stack", stack.writeToNBT(new NBTTagCompound()));
        tag.setLong("amount", amount);
        tag.setInteger("chance", chance);
        return tag;
    }

    @Nullable
    public static ExpectedFluidOutput readFromNBT(NBTTagCompound tag) {
        FluidStack stack = FluidStack.loadFluidStackFromNBT(tag.getCompoundTag("stack"));
        if (stack == null) return null;
        return new ExpectedFluidOutput(stack, tag.getLong("amount"), tag.getInteger("chance"));
    }

    public static NBTTagList writeList(List<ExpectedFluidOutput> outputs) {
        NBTTagList list = new NBTTagList();
        for (ExpectedFluidOutput output : outputs) {
            list.appendTag(output.writeToNBT());
        }
        return list;
    }

    public static List<ExpectedFluidOutput> readList(NBTTagList list) {
        List<ExpectedFluidOutput> outputs = new ArrayList<>(list.tagCount());
        for (int i = 0; i < list.tagCount(); i++) {
            ExpectedFluidOutput output = readFromNBT(list.getCompoundTagAt(i));
            if (output != null) outputs.add(output);
        }
        return outputs;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof ExpectedFluidOutput output)) return false;
        if (amount != output.amount || chance != output.chance) return false;
        if (stack == null || output.stack == null) return stack == output.stack;
        return stack.isFluidEqual(output.stack);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stack == null ? null : stack.getFluid(), amount, chance);
    }
}
