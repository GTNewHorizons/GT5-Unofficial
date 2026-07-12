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

    /// Untinted container base for [#cell] and the six cracked-cell shapes, converted from the legacy
    /// `cell_OVERLAY.png` shared by 61 of 66 material icon sets (see `scripts/mu/convert_textures.py`); the
    /// legacy `cellHydroCracked*`/`cellSteamCracked*` `OrePrefixes` all render through the same `CELL` texture
    /// slot as plain `cell` (see `dumps/oreprefixes.json`'s `textureIndex`).
    private static final String CELL_BASE = "gregtech:materials/cell_base";

    /// Untinted container base for [#cellPlasma]/[#cellPlasmaLight]/[#cellMolten], converted from
    /// the legacy `cellPlasma_OVERLAY.png` shared by 13 of 16 sets that have plasma-cell art. `cellMolten`
    /// renders through the same `CELL_PLASMA` texture slot as `cellPlasma` (see `dumps/oreprefixes.json`'s
    /// `textureIndex`), not a slot of its own -- there is no separate legacy `cellMolten` art.
    private static final String CELL_PLASMA_BASE = "gregtech:materials/cell_plasma_base";

    // spotless:off
    public static Shape cell;
    public static Shape cellPlasma;
    public static Shape cellPlasmaLight;
    public static Shape cellMolten;
    public static Shape cellHydroCracked1;
    public static Shape cellHydroCracked2;
    public static Shape cellHydroCracked3;
    public static Shape cellSteamCracked1;
    public static Shape cellSteamCracked2;
    public static Shape cellSteamCracked3;
    // spotless:on

    public static void init() {
        cell = MaterialLibAPI.newFluidInContainerShape("gregtech", "cell")
            .displayName("%s Cell")
            .fluid(Materials2FluidShapes.fluidLiquid, Materials2FluidShapes.fluidGas)
            .emptyContainer(EMPTY_CELL, 0)
            .volume(1000)
            .emptyIcon(CELL_BASE)
            .build();

        cellPlasma = MaterialLibAPI.newFluidInContainerShape("gregtech", "cellPlasma")
            .displayName("%s Plasma Cell")
            .fluid(Materials2FluidShapes.fluidPlasma)
            .emptyContainer(EMPTY_CELL, 0)
            .volume(INGOTS)
            .emptyIcon(CELL_PLASMA_BASE)
            .build();
        cellPlasmaLight = MaterialLibAPI.newFluidInContainerShape("gregtech", "cellPlasmaLight")
            .displayName("%s Plasma Cell")
            .fluid(Materials2FluidShapes.fluidPlasma)
            .emptyContainer(EMPTY_CELL, 0)
            .volume(1000)
            .oreDict("cellPlasma")
            .emptyIcon(CELL_PLASMA_BASE)
            .build();

        cellMolten = MaterialLibAPI.newFluidInContainerShape("gregtech", "cellMolten")
            .displayName("Molten %s Cell")
            .fluid(Materials2FluidShapes.fluidMolten)
            .emptyContainer(EMPTY_CELL, 0)
            .volume(INGOTS)
            .emptyIcon(CELL_PLASMA_BASE)
            .build();

        cellHydroCracked1 = crackedCellShape(
            "cellHydroCracked1",
            "Lightly Hydro-Cracked %s Cell",
            Materials2FluidShapes.fluidHydroCracked1);
        cellHydroCracked2 = crackedCellShape(
            "cellHydroCracked2",
            "Moderately Hydro-Cracked %s Cell",
            Materials2FluidShapes.fluidHydroCracked2);
        cellHydroCracked3 = crackedCellShape(
            "cellHydroCracked3",
            "Severely Hydro-Cracked %s Cell",
            Materials2FluidShapes.fluidHydroCracked3);
        cellSteamCracked1 = crackedCellShape(
            "cellSteamCracked1",
            "Lightly Steam-Cracked %s Cell",
            Materials2FluidShapes.fluidSteamCracked1);
        cellSteamCracked2 = crackedCellShape(
            "cellSteamCracked2",
            "Moderately Steam-Cracked %s Cell",
            Materials2FluidShapes.fluidSteamCracked2);
        cellSteamCracked3 = crackedCellShape(
            "cellSteamCracked3",
            "Severely Steam-Cracked %s Cell",
            Materials2FluidShapes.fluidSteamCracked3);
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
