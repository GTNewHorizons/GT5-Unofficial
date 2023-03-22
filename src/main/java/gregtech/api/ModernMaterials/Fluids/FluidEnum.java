package gregtech.api.ModernMaterials.Fluids;

import net.minecraft.util.IIcon;

import gregtech.api.ModernMaterials.ModernMaterial;

public enum FluidEnum {

    Molten("Molten %", 3_000, false),
    Gas("% Gas", 1_000, true),
    Plasma("% Plasma", 10_000, false),
    NoPrefix("%", 300, false);

    private final String format;
    private final int defaultTemperatureForFluidType;
    private final boolean isGas;
    public IIcon stillIcon;
    public IIcon flowingIcon;

    FluidEnum(final String format, final int defaultTemperatureForFluidType, final boolean isGas) {
        this.format = format;
        this.defaultTemperatureForFluidType = defaultTemperatureForFluidType;
        this.isGas = isGas;
    }

    public String format(ModernMaterial material) {
        return format.replace("%", material.getMaterialName());
    }

    public int getTemperature() {
        return defaultTemperatureForFluidType;
    }

    public boolean isGas() {
        return isGas;
    }
}
