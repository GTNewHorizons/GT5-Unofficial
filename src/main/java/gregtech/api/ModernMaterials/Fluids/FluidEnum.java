package gregtech.api.ModernMaterials.Fluids;

import gregtech.api.ModernMaterials.ModernMaterial;
import net.minecraft.util.IIcon;

public enum FluidEnum {

    Molten("Molten %", 3_000),
    Gas("% Gas", 1_000),
    Plasma("% Plasma", 10_000),
    NoPrefix("%", 300);

    private final String format;
    private final int defaultTemperatureForFluidType;
    public IIcon stillIcon;
    public IIcon flowingIcon;

    FluidEnum(final String format, final int defaultTemperatureForFluidType) {
        this.format = format;
        this.defaultTemperatureForFluidType = defaultTemperatureForFluidType;
    }

    public String format(ModernMaterial material) {
        return format.replace("%", material.getName());
    }

    public int getDefaultTemperatureForFluidType() {
        return defaultTemperatureForFluidType;
    }
}
