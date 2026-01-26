package gregtech.common.tileentities.machines.multi.nanochip.util;

import static gregtech.common.tileentities.machines.multi.nanochip.util.SplitterRule.FilterType.COLOR;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.cleanroommc.modularui.utils.item.LimitingItemStackHandler;
import com.cleanroommc.modularui.utils.serialization.IByteBufAdapter;
import com.google.common.primitives.Bytes;

import gregtech.common.tileentities.machines.multi.nanochip.modules.MTESplitterModule;

public class SplitterRule {

    public static class RedstoneMode {

        // Which redstone channel needs to have the specified signal
        public int channel;
        // The level the redstone channel needs to be at
        public int level;

        public RedstoneMode(int channel, int level) {
            this.channel = channel;
            this.level = level;
        }

        @Override
        public boolean equals(Object obj) {
            return (obj instanceof RedstoneMode mode) && this.channel == mode.channel && this.level == mode.level;
        }
    }

    public enum FilterType {

        COLOR,
        REDSTONE,
        ITEM;

        public static final FilterType[] VALUES = values();

        FilterType() {}
    }

    public List<Byte> inputColors;
    public List<Byte> outputColors;
    // Optional redstone settings for this splitter rule
    public RedstoneMode redstoneMode;
    // Optional handler of filtering items this rule should only apply to.
    public final ItemStackHandler filterStacks;
    // Used by the Splitter UI to determine which widget to show on the input side
    public FilterType enabledWidget;

    public SplitterRule() {
        this(
            new ArrayList<>(),
            new ArrayList<>(),
            new SplitterRule.RedstoneMode(0, 15),
            new LimitingItemStackHandler(6, 1),
            COLOR);
    }

    public SplitterRule(List<Byte> inputs, List<Byte> outputs, RedstoneMode redstoneMode, ItemStackHandler filterStacks,
        FilterType enabledWidget) {
        this.inputColors = inputs;
        this.outputColors = outputs;
        this.redstoneMode = redstoneMode;
        this.filterStacks = filterStacks;
        this.enabledWidget = enabledWidget;
    }

    // True if this rule can apply to the given itemstack
    public boolean appliesTo(Byte color, ItemStack item, MTESplitterModule.RedstoneChannelInfo redstoneState) {
        // Requires the given color to be in the set of input colors
        if (!inputColors.contains(color)) return false;
        // If a set of filter stacks is set and nonempty
        // If no items in the filter set match the given item, do not apply this rule
        if (filterStacks.getStacks()
            .stream()
            .noneMatch(stack -> stack == null || stack.isItemEqual(item))) {
            return false;
        }
        // If a redstone mode is set
        if (redstoneMode != null) {
            // Redstone level in requested channel should be at least the given level
            if (redstoneMode.level < redstoneState.get(redstoneMode.channel)) {
                return false;
            }
        }
        // All checks passed, this rule applies
        return true;
    }

    public NBTTagCompound saveToNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setByteArray("inputs", Bytes.toArray(inputColors));
        compound.setByteArray("outputs", Bytes.toArray(outputColors));
        if (redstoneMode != null) {
            compound.setInteger("channel", redstoneMode.channel);
            compound.setInteger("level", redstoneMode.level);
        }
        compound.setTag("filterStacks", filterStacks.serializeNBT());
        compound.setInteger("enabled", enabledWidget.ordinal());
        return compound;
    }

    public static SplitterRule loadFromNBT(NBTTagCompound compound) {
        SplitterRule rule = new SplitterRule();
        List<Byte> inputs = new ArrayList<>();
        List<Byte> outputs = new ArrayList<>();
        for (byte value : compound.getByteArray("inputs")) inputs.add(value);
        for (byte value : compound.getByteArray("outputs")) outputs.add(value);
        rule.inputColors = inputs;
        rule.outputColors = outputs;

        RedstoneMode redstoneMode = null;
        if (compound.hasKey("channel")) {
            int channel = compound.getInteger("channel");
            int level = compound.getInteger("level");
            redstoneMode = new RedstoneMode(channel, level);
        }
        rule.redstoneMode = redstoneMode;
        rule.filterStacks.deserializeNBT(compound.getCompoundTag("filterStacks"));
        rule.enabledWidget = FilterType.VALUES[compound.getInteger("enabled")];
        return rule;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SplitterRule rule)) return false;
        if (!this.inputColors.equals(rule.inputColors)) return false;
        if (!this.outputColors.equals(rule.outputColors)) return false;
        if (!Objects.equals(this.redstoneMode, rule.redstoneMode)) return false;
        if (this.enabledWidget != rule.enabledWidget) return false;

        for (int i = 0; i < this.filterStacks.getSlots(); i++) {
            ItemStack thisStack = this.filterStacks.getStackInSlot(i);
            ItemStack otherStack = rule.filterStacks.getStackInSlot(i);
            if (!ItemStack.areItemStacksEqual(thisStack, otherStack)) return false;
        }

        return true;
    }

    public static class SplitterRuleAdapter implements IByteBufAdapter<SplitterRule> {

        @Override
        public SplitterRule deserialize(PacketBuffer buffer) throws IOException {
            return SplitterRule.loadFromNBT(buffer.readNBTTagCompoundFromBuffer());
        }

        @Override
        public void serialize(PacketBuffer buffer, SplitterRule u) throws IOException {
            buffer.writeNBTTagCompoundToBuffer(u.saveToNBT());
        }

        @Override
        public boolean areEqual(@NotNull SplitterRule t1, @NotNull SplitterRule t2) {
            return t1.equals(t2);
        }
    }
}
