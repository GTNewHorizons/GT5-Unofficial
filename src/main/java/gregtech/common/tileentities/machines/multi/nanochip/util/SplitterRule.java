package gregtech.common.tileentities.machines.multi.nanochip.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;

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

        FilterType() {}
    }

    public List<Byte> inputColors;
    public List<Byte> outputColors;
    // Optional redstone settings for this splitter rule
    public RedstoneMode redstoneMode = null;
    // Optional set of filtering items this rule should only apply to.
    public Set<ItemStack> filterStacks = null;
    // Used by the Splitter UI to determine which widget to show on the input side
    public FilterType enabledWidget = FilterType.COLOR;

    public SplitterRule(List<Byte> inputs, List<Byte> outputs, RedstoneMode redstoneMode, Set<ItemStack> filterStacks,
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
        if (filterStacks != null && !filterStacks.isEmpty()) {
            // If no items in the filter set match the given item, do not apply this rule
            if (filterStacks.stream()
                .noneMatch(stack -> stack.isItemEqual(item))) {
                return false;
            }
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
        compound.setTag("inputs", new NBTTagByteArray(Bytes.toArray(inputColors)));
        compound.setTag("outputs", new NBTTagByteArray(Bytes.toArray(outputColors)));
        if (redstoneMode != null) {
            compound.setInteger("channel", redstoneMode.channel);
            compound.setInteger("level", redstoneMode.level);
        }
        if (filterStacks != null && !filterStacks.isEmpty()) {
            NBTTagList filterStackList = new NBTTagList();
            for (ItemStack stack : filterStacks) {
                filterStackList.appendTag(stack.getTagCompound());
            }
            compound.setTag("filterStacks", filterStackList);
        }
        compound.setTag("enabled", new NBTTagInt(enabledWidget.ordinal()));
        return compound;
    }

    public static SplitterRule loadFromNBT(NBTTagCompound compound) {
        List<Byte> inputs = new ArrayList<>();
        List<Byte> outputs = new ArrayList<>();
        for (byte value : compound.getByteArray("inputs")) inputs.add(value);
        for (byte value : compound.getByteArray("outputs")) outputs.add(value);
        RedstoneMode redstoneMode = null;
        if (compound.hasKey("channel")) {
            int channel = compound.getInteger("channel");
            int level = compound.getInteger("level");
            redstoneMode = new RedstoneMode(channel, level);
        }
        Set<ItemStack> filterStacks = null;
        if (compound.hasKey("filterStacks")) {
            filterStacks = new HashSet<>();
            // Type 10 is NBTTagCompound
            NBTTagList filterStackList = compound.getTagList("filterStacks", new NBTTagCompound().getId());
            for (int i = 0; i < filterStackList.tagCount(); ++i) {
                NBTTagCompound stackNBT = filterStackList.getCompoundTagAt(i);
                ItemStack stack = ItemStack.loadItemStackFromNBT(stackNBT);
                filterStacks.add(stack);
            }
        }
        FilterType enabled = FilterType.values()[compound.getInteger("enabled")];
        return new SplitterRule(inputs, outputs, redstoneMode, filterStacks, enabled);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof SplitterRule rule) && this.inputColors.equals(rule.inputColors)
            && this.outputColors.equals(rule.outputColors)
            && Objects.equals(this.redstoneMode, rule.redstoneMode)
            && Objects.equals(this.filterStacks, rule.filterStacks)
            && this.enabledWidget == rule.enabledWidget;
    }
}
