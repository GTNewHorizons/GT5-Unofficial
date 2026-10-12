package gregtech.common.gui.modularui.multiblock.godforge.data;

import net.minecraftforge.fluids.FluidStack;

import com.google.common.base.Supplier;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import tectech.thing.metaTileEntity.multi.godforge.util.ForgeOfGodsData;

public enum Fuels {

    RESIDUE(() -> MaterialLibAPI.getFluidStack(Materials.DimensionallyTranscendentResidue, FluidShapes.fluidLiquid, 1)),
    STELLAR(() -> MaterialLibAPI.getFluidStack(Materials.RawStarMatter, FluidShapes.fluidLiquid, 1)),
    MHDCSM(() -> MaterialLibAPI
        .getFluidStack(Materials.MagnetohydrodynamicallyConstrainedStarMatter, FluidShapes.fluidMolten, 1));

    public static final Fuels[] VALUES = values();

    private final Supplier<FluidStack> fluidSupplier;

    Fuels(Supplier<FluidStack> fluidSupplier) {
        this.fluidSupplier = fluidSupplier;
    }

    public FluidStack getFluid() {
        return fluidSupplier.get();
    }

    public void select(ForgeOfGodsData data) {
        data.setSelectedFuelType(ordinal());
    }

    public static Fuels getFromData(ForgeOfGodsData data) {
        return VALUES[data.getSelectedFuelType()];
    }
}
