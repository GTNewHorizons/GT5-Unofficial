package gregtech.api.enums;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public enum WrenchOutputConfig {

    ITEM(0, "GT5U.machines.wrench_output_config.item"),
    FLUID(1, "GT5U.machines.wrench_output_config.fluid"),
    BOTH(2, "GT5U.machines.wrench_output_config.both");

    private static final String NBT_KEY = "OutputConfig";

    private final byte value;
    private final String langKey;

    WrenchOutputConfig(int value, String langKey) {
        this.value = (byte) value;
        this.langKey = langKey;
    }

    public byte get() {
        return value;
    }

    public String getLangKey() {
        return langKey;
    }

    public static WrenchOutputConfig fromOrdinal(int ordinal) {
        WrenchOutputConfig[] values = values();
        if (ordinal < 0 || ordinal >= values.length) return BOTH;
        return values[ordinal];
    }

    /** Defaults to {@link #BOTH} when unset. */
    public static WrenchOutputConfig get(ItemStack toolStack) {
        if (toolStack == null) return BOTH;
        NBTTagCompound stats = getToolStats(toolStack);
        if (stats == null || !stats.hasKey(NBT_KEY)) return BOTH;
        return fromOrdinal(stats.getByte(NBT_KEY));
    }

    public static WrenchOutputConfig cycle(ItemStack toolStack) {
        if (toolStack == null) return BOTH;
        WrenchOutputConfig next = fromOrdinal((get(toolStack).ordinal() + 1) % values().length);
        NBTTagCompound stats = getOrCreateToolStats(toolStack);
        if (stats != null) {
            stats.setByte(NBT_KEY, next.value);
        }
        return next;
    }

    private static NBTTagCompound getToolStats(ItemStack stack) {
        NBTTagCompound root = stack.getTagCompound();
        if (root == null) return null;
        return root.getCompoundTag("GT.ToolStats");
    }

    private static NBTTagCompound getOrCreateToolStats(ItemStack stack) {
        NBTTagCompound root = stack.getTagCompound();
        if (root == null || !root.hasKey("GT.ToolStats")) {
            // Crafted tools always have GT.ToolStats; refuse to invent a bare tag on random stacks.
            return null;
        }
        return root.getCompoundTag("GT.ToolStats");
    }
}
