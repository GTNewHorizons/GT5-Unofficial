package gregtech.api.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;

import com.github.bsideup.jabel.Desugar;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;

/// One output of a running recipe before its chance is rolled: the stack identity (stack size ignored), the amount for
/// the current parallels, and the effective chance on the 10000 scale, where 10000 is 100% and larger values are
/// allowed. Equality compares stack identity, amount and chance rather than `ItemStack` reference identity.
@Desugar
public record ExpectedItemOutput(ItemStack stack, long amount, int chance) {

    public double expected() {
        return (double) amount * chance / 10000.0;
    }

    public boolean isChanced() {
        return chance % 10000 != 0;
    }

    public static void write(PacketBuffer buffer, ExpectedItemOutput output) {
        NetworkUtils.writeItemStack(buffer, output.stack);
        buffer.writeLong(output.amount);
        buffer.writeInt(output.chance);
    }

    public static ExpectedItemOutput read(PacketBuffer buffer) {
        ItemStack stack = NetworkUtils.readItemStack(buffer);
        return new ExpectedItemOutput(stack, buffer.readLong(), buffer.readInt());
    }

    public NBTTagCompound writeToNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        GTUtility.saveItem(tag, "stack", stack);
        tag.setLong("amount", amount);
        tag.setInteger("chance", chance);
        return tag;
    }

    @Nullable
    public static ExpectedItemOutput readFromNBT(NBTTagCompound tag) {
        ItemStack stack = GTUtility.loadItem(tag, "stack");
        if (stack == null) return null;
        return new ExpectedItemOutput(stack, tag.getLong("amount"), tag.getInteger("chance"));
    }

    public static NBTTagList writeList(List<ExpectedItemOutput> outputs) {
        NBTTagList list = new NBTTagList();
        for (ExpectedItemOutput output : outputs) {
            list.appendTag(output.writeToNBT());
        }
        return list;
    }

    public static List<ExpectedItemOutput> readList(NBTTagList list) {
        List<ExpectedItemOutput> outputs = new ArrayList<>(list.tagCount());
        for (int i = 0; i < list.tagCount(); i++) {
            ExpectedItemOutput output = readFromNBT(list.getCompoundTagAt(i));
            if (output != null) outputs.add(output);
        }
        return outputs;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof ExpectedItemOutput output)) return false;
        if (amount != output.amount || chance != output.chance) return false;
        if (stack == null || output.stack == null) return stack == output.stack;
        return GTUtility.areStacksEqual(stack, output.stack, false);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stack == null ? null : stack.getItem(), amount, chance);
    }
}
