package gregtech.api.ModernMaterials.Fluids;

import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.ModernMaterials.ModernMaterial;
import gregtech.api.enums.Materials;

final public class GT_ModernMaterial_Fluid extends Fluid {

    private final ModernMaterial associatedMaterial;
    private final FluidEnum fluidType;

    public GT_ModernMaterial_Fluid(FluidEnum fluidType, ModernMaterial modernMaterial) {
        super(fluidType.format(modernMaterial));
        this.associatedMaterial = modernMaterial;
        this.fluidType = fluidType;
    }

    @Override
    public int getColor() {
        return associatedMaterial.getColor().getRGB();
    }

    // todo fix
    @Override
    public IIcon getFlowingIcon() {
        return Materials.Iron.mPlasma.getStillIcon();
    }

    @Override
    public int getTemperature(FluidStack stack) {
        return this.fluidType.getDefaultTemperatureForFluidType();
    }

    @Override
    public String getLocalizedName(FluidStack stack) {
        return this.fluidType.format(associatedMaterial);
    }

    // todo fix
    @Override
    public IIcon getStillIcon() {
        return Materials.Iron.mPlasma.getStillIcon();
    }

}
