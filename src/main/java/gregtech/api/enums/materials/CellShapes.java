package gregtech.api.enums.materials;

import static gregtech.api.enums.materials.GTShapeStore.reg;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;

import com.ruling_0.materiallib.api.EmptyContainerHandle;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;
import com.ruling_0.materiallib.api.StandardProperties;

import gregtech.common.items.GTSolidCellShapeItem;

/// Cell [Shape] declarations. Every cell drains to [#emptyCell].
///
/// Two oredict prefixes span several shapes, since a MaterialLib container has one volume and binds one fluid
/// shape:
/// - `cellPlasma` holds 144 mB for most metal plasmas. Materials whose plasma cell holds 1000 mB declare
/// [#cellPlasmaLight] instead.
/// - `cell` covers [#cell] for liquids, [#cellGas] for gases and [#cellSolid] for materials with no fluid.
///
/// [gregtech.api.material.MaterialParts] resolves each prefix to its group of shapes.
public class CellShapes {

    /// The empty cell every cell shape drains to, bound to `ItemList.Cell_Empty`.
    public static EmptyContainerHandle emptyCell;

    /// Untinted container base for [#emptyCell], [#cell], [#cellGas], [#cellSolid] and the six cracked-cell shapes.
    private static final String CELL_BASE = "gregtech:materials/cell_base";

    /// Untinted container base for [#cellPlasma], [#cellPlasmaLight] and [#cellMolten]. There is no separate
    /// `cellMolten` art.
    private static final String CELL_PLASMA_BASE = "gregtech:materials/cell_plasma_base";

    // spotless:off
    public static Shape cell;
    public static Shape cellGas;
    public static Shape cellSolid;
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

        cell = reg(
            MaterialLibAPI.newFluidInContainerShape("gregtech", "cell")
                .displayNameKey("gt.oreprefix.material_cell")
                .fluid(FluidShapes.fluidLiquid)
                .emptyContainer(emptyCell)
                .volume(1000)
                .emptyIcon(CELL_BASE)
                .build());
        cellGas = reg(
            MaterialLibAPI.newFluidInContainerShape("gregtech", "cellGas")
                .displayNameKey("gt.oreprefix.material_cell")
                .fluid(FluidShapes.fluidGas)
                .emptyContainer(emptyCell)
                .volume(1000)
                .oreDict("cell")
                .emptyIcon(CELL_BASE)
                .build());
        cellSolid = reg(
            MaterialLibAPI.registerItemShape(new GTSolidCellShapeItem("gregtech", "cellSolid", CELL_BASE, "cell"))
                .setProperty(StandardProperties.DISPLAY_NAME_FORMAT_KEY, "gt.oreprefix.material_cell"));

        cellPlasma = reg(
            MaterialLibAPI.newFluidInContainerShape("gregtech", "cellPlasma")
                .displayNameKey("gt.oreprefix.material_plasma_cell")
                .fluid(FluidShapes.fluidPlasma)
                .emptyContainer(emptyCell)
                .volume(INGOTS)
                .emptyIcon(CELL_PLASMA_BASE)
                .build());
        cellPlasmaLight = reg(
            MaterialLibAPI.newFluidInContainerShape("gregtech", "cellPlasmaLight")
                .displayNameKey("gt.oreprefix.material_plasma_cell")
                .fluid(FluidShapes.fluidPlasma)
                .emptyContainer(emptyCell)
                .volume(1000)
                .oreDict("cellPlasma")
                .emptyIcon(CELL_PLASMA_BASE)
                .build());

        cellMolten = reg(
            MaterialLibAPI.newFluidInContainerShape("gregtech", "cellMolten")
                .displayNameKey("gt.oreprefix.molten_material_cell")
                .fluid(FluidShapes.fluidMolten)
                .emptyContainer(emptyCell)
                .volume(INGOTS)
                .emptyIcon(CELL_PLASMA_BASE)
                .build());

        cellHydroCracked1 = crackedCellShape(
            "cellHydroCracked1",
            "gt.oreprefix.lightly_hydro-cracked_material_cell",
            FluidShapes.fluidHydroCracked1);
        cellHydroCracked2 = crackedCellShape(
            "cellHydroCracked2",
            "gt.oreprefix.moderately_hydro-cracked_material_cell",
            FluidShapes.fluidHydroCracked2);
        cellHydroCracked3 = crackedCellShape(
            "cellHydroCracked3",
            "gt.oreprefix.severely_hydro-cracked_material_cell",
            FluidShapes.fluidHydroCracked3);
        cellSteamCracked1 = crackedCellShape(
            "cellSteamCracked1",
            "gt.oreprefix.lightly_steam-cracked_material_cell",
            FluidShapes.fluidSteamCracked1);
        cellSteamCracked2 = crackedCellShape(
            "cellSteamCracked2",
            "gt.oreprefix.moderately_steam-cracked_material_cell",
            FluidShapes.fluidSteamCracked2);
        cellSteamCracked3 = crackedCellShape(
            "cellSteamCracked3",
            "gt.oreprefix.severely_steam-cracked_material_cell",
            FluidShapes.fluidSteamCracked3);
    }

    private static Shape crackedCellShape(String name, String displayFormatKey, Shape fluidShape) {
        return reg(
            MaterialLibAPI.newFluidInContainerShape("gregtech", name)
                .displayNameKey(displayFormatKey)
                .fluid(fluidShape)
                .emptyContainer(emptyCell)
                .volume(1000)
                .emptyIcon(CELL_BASE)
                .build());
    }

    private CellShapes() {}
}
