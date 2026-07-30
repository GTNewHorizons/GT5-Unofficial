package gregtech.common.gui.modularui.multiblock.godforge.data;

import gregtech.api.enums.materials2.Materials;
import net.minecraftforge.fluids.FluidStack;

import com.google.common.base.Supplier;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.material.MaterialUtils;
import tectech.thing.metaTileEntity.multi.godforge.util.ForgeOfGodsData;

public enum Fuels {

    RESIDUE(() -> MaterialUtils.fluid(Materials.DimensionallyTranscendentResidue, 1)),
    STELLAR(() -> MaterialLibAPI
        .getFluidStack(Materials.RawStarMatter, Materials2FluidShapes.fluidLiquid, (int) (1))),
    MHDCSM(() -> MaterialUtils.molten(Materials.MagnetohydrodynamicallyConstrainedStarMatter, 1));

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
