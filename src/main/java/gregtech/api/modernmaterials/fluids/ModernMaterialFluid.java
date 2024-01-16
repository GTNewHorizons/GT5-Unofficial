package gregtech.api.modernmaterials.fluids;

import java.util.function.Supplier;

import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.modernmaterials.ModernMaterial;

final public class ModernMaterialFluid extends Fluid {

    private final ModernMaterial associatedMaterial;
    private final Supplier<String> localizedNameGetter;
    private FluidEnum fluidEnum;

    public ModernMaterialFluid(FluidEnum fluidEnum, ModernMaterial modernMaterial) {
        super(fluidEnum.getUnlocalizedName(modernMaterial));
        this.fluidEnum = fluidEnum;
        this.localizedNameGetter = () -> fluidEnum.getLocalizedName(modernMaterial);
        this.associatedMaterial = modernMaterial;
        this.temperature = fluidEnum.getTemperature();
    }

    public ModernMaterialFluid(String internalName, String prefix, ModernMaterial modernMaterial) {
        super(prefix + modernMaterial.getMaterialName());
        this.localizedNameGetter = () -> StatCollector
            .translateToLocalFormatted("gt.fluid.custom." + internalName, modernMaterial.getLocalizedName());
        this.associatedMaterial = modernMaterial;
    }

    private boolean shouldColourFluid = true;

    public void disableFluidColouring() {
        shouldColourFluid = false;
    }

    @Override
    public int getColor() {

        if (!shouldColourFluid) return 0xFFFFFF;

        return associatedMaterial.getColor()
            .getRGB();
    }

    @Override
    public String getLocalizedName(FluidStack stack) {
        return localizedNameGetter.get();
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
        return fluidName;
    }

    public static class Builder {

        private final String internalName;
        private final String prefix;
        private ModernMaterial associatedMaterial;
        private int temperature = -1;
        private boolean isGas = false;
        private IIcon stillIcon;
        private IIcon flowingIcon;

        public Builder(String internalName, String prefix) {
            this.internalName = internalName;
            this.prefix = prefix;
        }

        public Builder(String internalName) {
            this(internalName, "");
        }

        public ModernMaterialFluid build() {
            return new ModernMaterialFluid(internalName, prefix, associatedMaterial);
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
