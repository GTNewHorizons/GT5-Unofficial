package gregtech.api.ModernMaterials.Fluids;

import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.ModernMaterials.ModernMaterial;

final public class ModernMaterialFluid extends Fluid {

    private final ModernMaterial associatedMaterial;
    private final String localisedName;
    private FluidEnum fluidEnum;

    public ModernMaterialFluid(FluidEnum fluidEnum, ModernMaterial modernMaterial) {
        super(fluidEnum.format(modernMaterial));
        this.fluidEnum = fluidEnum;
        this.localisedName = fluidEnum.format(modernMaterial);
        this.associatedMaterial = modernMaterial;
        this.temperature = fluidEnum.getTemperature();
    }

    public ModernMaterialFluid(String unformattedString, ModernMaterial modernMaterial) {
        super(unformattedString.replace("%", modernMaterial.getMaterialName()));
        this.localisedName = unformattedString.replace("%", modernMaterial.getMaterialName());
        this.associatedMaterial = modernMaterial;
    }

    @Override
    public int getColor() {
        return associatedMaterial.getColor()
            .getRGB();
    }

    @Override
    public String getLocalizedName(FluidStack stack) {
        return localisedName;
    }

    public void setFluidEnum(FluidEnum fluidEnum) {
        this.fluidEnum = fluidEnum;
    }

    @Override
    public IIcon getStillIcon() {
        if (fluidEnum == null) return stillIcon; // Custom fluid textures.
        return fluidEnum.stillIcon;
    }

    @Override
    public IIcon getFlowingIcon() {
        if (fluidEnum == null) return flowingIcon; // Custom fluid textures.
        return fluidEnum.flowingIcon;
    }

    @Override
    public String toString() {
        return localisedName;
    }

    public static class Builder {

        private ModernMaterial associatedMaterial;
        private int temperature = -1;
        private boolean isGas = false;
        private final String unformattedString;
        private IIcon stillIcon;
        private IIcon flowingIcon;

        // Unformatted string example: "Molten % Stuff" -> "Molten Test Stuff" as fluid name if
        // material is called "Test". You can use no % signs or multiple.
        public Builder(String unformatedString) {
            this.unformattedString = unformatedString;
        }

        public ModernMaterialFluid build() {
            return new ModernMaterialFluid(unformattedString, associatedMaterial);
        }

        public ModernMaterialFluid.Builder setMaterial(ModernMaterial material) {
            this.associatedMaterial = material;
            return this;
        }

        public ModernMaterialFluid.Builder setTemperature(int temperature) {
            this.temperature = temperature;
            return this;
        }

        public ModernMaterialFluid.Builder isGaseous(boolean isGaseous) {
            this.isGas = isGaseous;
            return this;
        }

        public ModernMaterialFluid.Builder setFlowingIcon(IIcon icon) {
            this.flowingIcon = icon;
            return this;
        }

        public ModernMaterialFluid.Builder setStillIcon(IIcon icon) {
            this.stillIcon = icon;
            return this;
        }
    }

}
