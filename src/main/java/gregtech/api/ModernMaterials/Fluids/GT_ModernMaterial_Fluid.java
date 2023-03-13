package gregtech.api.ModernMaterials.Fluids;

import gregtech.api.ModernMaterials.ModernMaterial;
import gregtech.api.enums.Materials;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

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

//    @Override
//    public IIcon getFlowingIcon() {
//        return Materials.Iron.mFluid.getFlowingIcon();
//    }

    @Override
    public String getLocalizedName(FluidStack stack) {
        return fluidType.format(associatedMaterial);
    }

//    @Override
//    public IIcon getStillIcon() {
//        return Materials.Iron.mFluid.getStillIcon();
//    }

}
