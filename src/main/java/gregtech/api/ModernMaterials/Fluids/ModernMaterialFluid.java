package gregtech.api.ModernMaterials.Fluids;

import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.ModernMaterials.ModernMaterial;
import gregtech.api.enums.Materials;

import java.util.HashSet;
import java.util.Set;

final public class ModernMaterialFluid extends Fluid {

    private final ModernMaterial associatedMaterial;
    private final FluidEnum fluidType;

    public ModernMaterialFluid(FluidEnum fluidType, ModernMaterial modernMaterial) {
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
        return this.fluidType.getTemperature();
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
