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

    /// Untinted container base for [#shapeCell] and the six cracked-cell shapes, converted from the legacy
    /// `cell_OVERLAY.png` shared by 61 of 66 material icon sets (see `scripts/mu/convert_textures.py`); the
    /// legacy `cellHydroCracked*`/`cellSteamCracked*` `OrePrefixes` all render through the same `CELL` texture
    /// slot as plain `cell` (see `dumps/oreprefixes.json`'s `textureIndex`).
    private static final String CELL_BASE = "gregtech:materials/cell_base";

    /// Untinted container base for [#shapeCellPlasma]/[#shapeCellPlasmaLight]/[#shapeCellMolten], converted from
    /// the legacy `cellPlasma_OVERLAY.png` shared by 13 of 16 sets that have plasma-cell art. `cellMolten`
    /// renders through the same `CELL_PLASMA` texture slot as `cellPlasma` (see `dumps/oreprefixes.json`'s
    /// `textureIndex`), not a slot of its own -- there is no separate legacy `cellMolten` art.
    private static final String CELL_PLASMA_BASE = "gregtech:materials/cell_plasma_base";

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
            .emptyIcon(CELL_BASE)
            .build();

        shapeCellPlasma = MaterialLibAPI.newFluidInContainerShape("gregtech", "cellPlasma")
            .displayName("%s Plasma Cell")
            .fluid(Materials2FluidShapes.shapeFluidPlasma)
            .emptyContainer(EMPTY_CELL, 0)
            .volume(INGOTS)
            .emptyIcon(CELL_PLASMA_BASE)
            .build();
        shapeCellPlasmaLight = MaterialLibAPI.newFluidInContainerShape("gregtech", "cellPlasmaLight")
            .displayName("%s Plasma Cell")
            .fluid(Materials2FluidShapes.shapeFluidPlasma)
            .emptyContainer(EMPTY_CELL, 0)
            .volume(1000)
            .oreDict("cellPlasma")
            .emptyIcon(CELL_PLASMA_BASE)
            .build();

        shapeCellMolten = MaterialLibAPI.newFluidInContainerShape("gregtech", "cellMolten")
            .displayName("Molten %s Cell")
            .fluid(Materials2FluidShapes.shapeFluidMolten)
            .emptyContainer(EMPTY_CELL, 0)
            .volume(INGOTS)
            .emptyIcon(CELL_PLASMA_BASE)
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
            .emptyIcon(CELL_BASE)
            .build();
    }

    private Materials2CellShapes() {}
}
