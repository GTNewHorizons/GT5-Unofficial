package gregtech.api.util.tooltip;

import net.minecraft.util.StatCollector;

/**
 * Contains a series of Lang-key / Tier Parings.
 * Assists in a few methods in {@link gregtech.api.util.MultiblockTooltipBuilder}
 *
 * Keys found under GT5U.MBTT.Tiers.* . Please add your own.
 */
public enum TooltipTier {

    VOLTAGE("GT5U.MBTT.Tiers.Voltage"),
    MACHINE("GT5U.MBTT.Tiers.Machine"),
    COIL("GT5U.MBTT.Tiers.Coil"),
    MACHINE_CASING("GT5U.MBTT.Tiers.MachineCasing"),
    METAL_CASING("GT5U.MBTT.Tiers.MetalMachineCasing"),
    ITEM_PIPE_CASING("GT5U.MBTT.Tiers.ItemPipe"),
    PIPE_CASING("GT5U.MBTT.Tiers.FluidPipe"),
    SOLENOID("GT5U.MBTT.Tiers.Solenoid"),
    TURBINE("GT5U.MBTT.Tiers.Turbine"),
    GLASS("GT5U.MBTT.Tiers.Glass"),
    COMPONENT_ASSEMBLY_LINE_CASING("GT5U.MBTT.Tiers.ComponentAssemblyLineCasing"),

    ;

    public final String key;

    TooltipTier(String key) {
        this.key = key;
    }

    public String getValue() {
        return TooltipHelper.tierText(StatCollector.translateToLocal(this.key));
    }
}
