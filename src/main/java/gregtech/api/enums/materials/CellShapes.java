package gregtech.api.enums.materials;

import static gregtech.api.util.GTRecipeBuilder.INGOTS;

import com.ruling_0.materiallib.api.EmptyContainerHandle;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

/// Fluid-in-container [Shape] declarations for cells. Every cell shape drains to [#emptyCell], a gregtech-owned
/// standalone empty container item registered through MaterialLib alongside the shapes themselves.
///
/// `cellPlasma` needs two shapes sharing one oredict prefix: legacy plasma cells hold [GTRecipeBuilder#INGOTS] (144 mB)
/// when the material also has a molten fluid (metal plasmas), or 1000 mB otherwise (gas/element plasmas) -- a
/// per-material volume MaterialLib's container shape does not support directly. [MaterialParts] resolves the two back
/// to one prefix.
public class CellShapes {

    /// The empty cell every cell shape drains to, rebound onto `ItemList.Cell_Empty` during GTProxy's preInit.
    /// Renders through the same [#CELL_BASE] art as the filled cells.
    public static EmptyContainerHandle emptyCell;

    /// Untinted container base for [#cell] and the six cracked-cell shapes, converted from the legacy
    /// `cell_OVERLAY.png` shared by 61 of 66 material icon sets. The `cellHydroCracked*`/`cellSteamCracked*`
    /// `OrePrefixes` all render through the same `CELL` texture slot as plain `cell`.
    private static final String CELL_BASE = "gregtech:materials/cell_base";

    /// Untinted container base for [#cellPlasma]/[#cellPlasmaLight]/[#cellMolten], converted from
    /// the legacy `cellPlasma_OVERLAY.png` shared by 13 of 16 sets that have plasma-cell art. `cellMolten`
    /// renders through the same `CELL_PLASMA` texture slot as `cellPlasma`, not one of its own: there is no
    /// separate `cellMolten` art.
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
        emptyCell = MaterialLibAPI.registerEmptyContainer("gregtech", "cellEmpty", CELL_BASE);

        cell = MaterialLibAPI.newFluidInContainerShape("gregtech", "cell")
            .displayName("%s Cell")
            .fluid(FluidShapes.fluidLiquid, FluidShapes.fluidGas)
            .emptyContainer(emptyCell)
            .volume(1000)
            .emptyIcon(CELL_BASE)
            .build();

        cellPlasma = MaterialLibAPI.newFluidInContainerShape("gregtech", "cellPlasma")
            .displayName("%s Plasma Cell")
            .fluid(FluidShapes.fluidPlasma)
            .emptyContainer(emptyCell)
            .volume(INGOTS)
            .emptyIcon(CELL_PLASMA_BASE)
            .build();
        cellPlasmaLight = MaterialLibAPI.newFluidInContainerShape("gregtech", "cellPlasmaLight")
            .displayName("%s Plasma Cell")
            .fluid(FluidShapes.fluidPlasma)
            .emptyContainer(emptyCell)
            .volume(1000)
            .oreDict("cellPlasma")
            .emptyIcon(CELL_PLASMA_BASE)
            .build();

        cellMolten = MaterialLibAPI.newFluidInContainerShape("gregtech", "cellMolten")
            .displayName("Molten %s Cell")
            .fluid(FluidShapes.fluidMolten)
            .emptyContainer(emptyCell)
            .volume(INGOTS)
            .emptyIcon(CELL_PLASMA_BASE)
            .build();

        cellHydroCracked1 = crackedCellShape(
            "cellHydroCracked1",
            "Lightly Hydro-Cracked %s Cell",
            FluidShapes.fluidHydroCracked1);
        cellHydroCracked2 = crackedCellShape(
            "cellHydroCracked2",
            "Moderately Hydro-Cracked %s Cell",
            FluidShapes.fluidHydroCracked2);
        cellHydroCracked3 = crackedCellShape(
            "cellHydroCracked3",
            "Severely Hydro-Cracked %s Cell",
            FluidShapes.fluidHydroCracked3);
        cellSteamCracked1 = crackedCellShape(
            "cellSteamCracked1",
            "Lightly Steam-Cracked %s Cell",
            FluidShapes.fluidSteamCracked1);
        cellSteamCracked2 = crackedCellShape(
            "cellSteamCracked2",
            "Moderately Steam-Cracked %s Cell",
            FluidShapes.fluidSteamCracked2);
        cellSteamCracked3 = crackedCellShape(
            "cellSteamCracked3",
            "Severely Steam-Cracked %s Cell",
            FluidShapes.fluidSteamCracked3);
    }

    private static Shape crackedCellShape(String name, String displayFormat, Shape fluidShape) {
        return MaterialLibAPI.newFluidInContainerShape("gregtech", name)
            .displayName(displayFormat)
            .fluid(fluidShape)
            .emptyContainer(emptyCell)
            .volume(1000)
            .emptyIcon(CELL_BASE)
            .build();
    }

    private CellShapes() {}
}
