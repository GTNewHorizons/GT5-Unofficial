package gregtech.api.modernmaterials.fluids;

import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import gregtech.api.modernmaterials.ModernMaterial;

public enum FluidEnum {

    Molten("molten", "molten.", 3_000, false),
    Gas("gas", "gas.", 1_000, true),
    Plasma("plasma", "plasma.", 10_000, false),
    NoPrefix("none", "", 300, false);

    private final String internalName;
    private final String prefix;
    private final int defaultTemperatureForFluidType;
    private final boolean isGas;
    public IIcon stillIcon;
    public IIcon flowingIcon;

    FluidEnum(final String internalName, String prefix, final int defaultTemperatureForFluidType, final boolean isGas) {
        this.internalName = internalName;
        this.prefix = prefix;
        this.defaultTemperatureForFluidType = defaultTemperatureForFluidType;
        this.isGas = isGas;
    }

    public String getUnlocalizedName(ModernMaterial material) {
        return prefix + material.getMaterialName();
    }

    public String getLocalizedName(ModernMaterial material) {
        return StatCollector.translateToLocalFormatted("gt.part.fluid." + internalName, material.getLocalizedName());
    }

    public int getTemperature() {
        return defaultTemperatureForFluidType;
    }

    public boolean isGas() {
        return isGas;
    }
}
