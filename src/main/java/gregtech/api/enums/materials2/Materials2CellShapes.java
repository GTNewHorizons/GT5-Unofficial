package gregtech.api.enums.materials2;

import static gregtech.api.util.GTRecipeBuilder.INGOTS;

import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

/// Hand-maintained fluid-in-container [Shape] declarations for GT's cells (unlike [Materials2Shapes], not
/// `gen_shapes.py`-generated: cells are containers, which that generator excludes). Empty container is IC2's
/// cell (`IC2:itemCellEmpty`), resolved once at MaterialLib's init -- after IC2's own preInit has registered it
/// -- via the deferred-by-name mechanism, since IC2 loads and registers items independently of MaterialLib's
/// own preInit-time resolve.
///
/// `cellPlasma` needs two shapes sharing one oredict prefix: legacy plasma cells hold [GTRecipeBuilder#INGOTS]
/// (144 mB) when the material also has a molten fluid (metal plasmas), or 1000 mB otherwise (gas/element
/// plasmas) -- a per-material volume MaterialLib's container shape does not support directly. [MU] resolves the
/// two back to one prefix.
public class Materials2CellShapes {

    private static final String EMPTY_CELL = "IC2:itemCellEmpty";

    // spotless:off
    public static Shape shapeCell;
    public static Shape shapeCellPlasma;
    public static Shape shapeCellPlasmaLight;
    public static Shape shapeCellMolten;
    public static Shape shapeCellHydroCracked1;
    public static Shape shapeCellHydroCracked2;
    public static Shape shapeCellHydroCracked3;
    public static Shape shapeCellSteamCracked1;
    public static Shape shapeCellSteamCracked2;
    public static Shape shapeCellSteamCracked3;
    // spotless:on

    public static void init() {
        shapeCell = MaterialLibAPI.newFluidInContainerShape("gregtech", "cell")
            .displayName("%s Cell")
            .fluid(Materials2FluidShapes.shapeFluidLiquid, Materials2FluidShapes.shapeFluidGas)
            .emptyContainer(EMPTY_CELL, 0)
            .volume(1000)
            .build();

        shapeCellPlasma = MaterialLibAPI.newFluidInContainerShape("gregtech", "cellPlasma")
            .displayName("%s Plasma Cell")
            .fluid(Materials2FluidShapes.shapeFluidPlasma)
            .emptyContainer(EMPTY_CELL, 0)
            .volume(INGOTS)
            .build();
        shapeCellPlasmaLight = MaterialLibAPI.newFluidInContainerShape("gregtech", "cellPlasmaLight")
            .displayName("%s Plasma Cell")
            .fluid(Materials2FluidShapes.shapeFluidPlasma)
            .emptyContainer(EMPTY_CELL, 0)
            .volume(1000)
            .oreDict("cellPlasma")
            .build();

        shapeCellMolten = MaterialLibAPI.newFluidInContainerShape("gregtech", "cellMolten")
            .displayName("Molten %s Cell")
            .fluid(Materials2FluidShapes.shapeFluidMolten)
            .emptyContainer(EMPTY_CELL, 0)
            .volume(INGOTS)
            .build();

        shapeCellHydroCracked1 = crackedCellShape(
            "cellHydroCracked1",
            "Lightly Hydro-Cracked %s Cell",
            Materials2FluidShapes.shapeFluidHydroCracked1);
        shapeCellHydroCracked2 = crackedCellShape(
            "cellHydroCracked2",
            "Moderately Hydro-Cracked %s Cell",
            Materials2FluidShapes.shapeFluidHydroCracked2);
        shapeCellHydroCracked3 = crackedCellShape(
            "cellHydroCracked3",
            "Severely Hydro-Cracked %s Cell",
            Materials2FluidShapes.shapeFluidHydroCracked3);
        shapeCellSteamCracked1 = crackedCellShape(
            "cellSteamCracked1",
            "Lightly Steam-Cracked %s Cell",
            Materials2FluidShapes.shapeFluidSteamCracked1);
        shapeCellSteamCracked2 = crackedCellShape(
            "cellSteamCracked2",
            "Moderately Steam-Cracked %s Cell",
            Materials2FluidShapes.shapeFluidSteamCracked2);
        shapeCellSteamCracked3 = crackedCellShape(
            "cellSteamCracked3",
            "Severely Steam-Cracked %s Cell",
            Materials2FluidShapes.shapeFluidSteamCracked3);
    }

    private static Shape crackedCellShape(String name, String displayFormat, Shape fluidShape) {
        return MaterialLibAPI.newFluidInContainerShape("gregtech", name)
            .displayName(displayFormat)
            .fluid(fluidShape)
            .emptyContainer(EMPTY_CELL, 0)
            .volume(1000)
            .build();
    }

    private Materials2CellShapes() {}
}
