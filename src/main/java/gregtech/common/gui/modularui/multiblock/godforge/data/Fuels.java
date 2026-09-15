package gregtech.common.gui.modularui.multiblock.godforge.data;

import net.minecraftforge.fluids.FluidStack;

import com.google.common.base.Supplier;

import gregtech.api.enums.Materials;
import tectech.thing.metaTileEntity.multi.godforge.util.ForgeOfGodsData;

public enum Fuels {

    RESIDUE(() -> Materials.DTR.getFluid(1)),
    STELLAR(() -> Materials.RawStarMatter.getFluid(1)),
    MHDCSM(() -> Materials.MHDCSM.getMolten(1));

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
