package gregtech.api.enums.materials2;

import com.ruling_0.materiallib.api.Family;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.StandardProperties;
import com.ruling_0.materiallib.api.TextureSet;

// One-time output of scripts/mu/gen_shapes.py (RETIRED for this file, see its module docstring);
// hand-maintained from here -- edit this file directly.
/// The shape groups a material joins. A family carries the shapes every member generates, so a member declares
/// only its own exceptions: [Materials] adds a shape the family does not carry with `generateShape`
/// and drops one it does with `removeShape`.
///
/// Grouping follows what a material *is* rather than what its items are called. The capability families
/// ([#dusts], [#metals], [#gems], [#ores], [#toolParts], [#gears]) carry the shapes shared by essentially
/// every member; where a sizeable minority shares a distinct sub-set, that sub-set is its own family
/// ([#multiPlates], [#gemQualities], [#poweredToolParts]) rather than a shape the majority removes.
///
/// The fluid-state families ([#liquids], [#gases], [#moltens], [#plasmas]) each carry a fluid shape and the
/// cell shape that follows from it: every cell-generating material also generates the matching fluid, so cell
/// membership is a consequence of fluid state, not an independent fact.
///
/// [#all] carries no shapes. It exists only to give every material a fallback texture set, and is the one
/// family allowed to declare a property: family property lookup takes the first family in `modid:name` order,
/// and `gregtech:All` sorts ahead of every other, so a second property-carrying family would silently win or
/// lose by name.
public class Materials2Families {

    // spotless:off
    public static Family all;
    public static Family dusts;
    public static Family metals;
    public static Family multiPlates;
    public static Family gems;
    public static Family gemQualities;
    public static Family ores;
    public static Family toolParts;
    public static Family poweredToolParts;
    public static Family gears;
    public static Family liquids;
    public static Family gases;
    public static Family moltens;
    public static Family plasmas;
    // spotless:on

    public static void init() {
        all = MaterialLibAPI.newFamily("gregtech", "All")
            .setProperty(StandardProperties.FALLBACK_TEXTURE_SET, TextureSet.of("gregtech", "NONE"))
            .build();

        dusts = MaterialLibAPI.newFamily("gregtech", "Dusts")
            .generateShapes(Shapes.dust, Shapes.dustSmall, Shapes.dustTiny)
            .build();
        metals = MaterialLibAPI.newFamily("gregtech", "Metals")
            .generateShapes(
                Shapes.bolt,
                Shapes.dust,
                Shapes.dustSmall,
                Shapes.dustTiny,
                Shapes.foil,
                Shapes.ingot,
                Shapes.ingotHot,
                Shapes.itemCasing,
                Shapes.nugget,
                Shapes.plate,
                Shapes.plateDense,
                Shapes.plateDouble,
                Shapes.plateSuperdense,
                Shapes.ring,
                Shapes.round,
                Shapes.screw,
                Shapes.spring,
                Shapes.springSmall,
                Shapes.stick,
                Shapes.stickLong,
                Shapes.wireFine)
            .build();
        multiPlates = MaterialLibAPI.newFamily("gregtech", "MultiPlates")
            .generateShapes(
                Shapes.plateTriple,
                Shapes.plateQuadruple,
                Shapes.plateQuintuple)
            .build();
        gems = MaterialLibAPI.newFamily("gregtech", "Gems")
            .generateShapes(
                Shapes.bolt,
                Shapes.dust,
                Shapes.dustSmall,
                Shapes.dustTiny,
                Shapes.gem,
                Shapes.itemCasing,
                Shapes.lens,
                Shapes.plate,
                Shapes.screw,
                Shapes.stick,
                Shapes.stickLong)
            .build();
        gemQualities = MaterialLibAPI.newFamily("gregtech", "GemQualities")
            .generateShapes(
                Shapes.gemChipped,
                Shapes.gemFlawed,
                Shapes.gemFlawless,
                Shapes.gemExquisite)
            .build();
        ores = MaterialLibAPI.newFamily("gregtech", "Ores")
            .generateShapes(
                Shapes.crushed,
                Shapes.crushedCentrifuged,
                Shapes.crushedPurified,
                Shapes.dust,
                Shapes.dustImpure,
                Shapes.dustPure,
                Shapes.dustSmall,
                Shapes.dustTiny,
                Shapes.rawOre,
                OreShapes.ore,
                OreShapes.oreSmall)
            .build();
        toolParts = MaterialLibAPI.newFamily("gregtech", "ToolParts")
            .generateShapes(
                Shapes.toolHeadHammer,
                Shapes.toolHeadSaw,
                Shapes.toolHeadWrench,
                Shapes.turbineBlade)
            .build();
        poweredToolParts = MaterialLibAPI.newFamily("gregtech", "PoweredToolParts")
            .generateShapes(
                Shapes.toolHeadBuzzSaw,
                Shapes.toolHeadChainsaw,
                Shapes.toolHeadDrill,
                Shapes.toolHeadFile)
            .build();
        gears = MaterialLibAPI.newFamily("gregtech", "Gears")
            .generateShapes(Shapes.gearGt, Shapes.gearGtSmall, Shapes.rotor)
            .build();
        liquids = MaterialLibAPI.newFamily("gregtech", "Liquids")
            .generateShapes(Materials2FluidShapes.fluidLiquid, CellShapes.cell)
            .build();
        gases = MaterialLibAPI.newFamily("gregtech", "Gases")
            .generateShapes(Materials2FluidShapes.fluidGas, CellShapes.cell)
            .build();
        moltens = MaterialLibAPI.newFamily("gregtech", "Moltens")
            .generateShapes(Materials2FluidShapes.fluidMolten, CellShapes.cellMolten)
            .build();
        plasmas = MaterialLibAPI.newFamily("gregtech", "Plasmas")
            .generateShape(Materials2FluidShapes.fluidPlasma)
            .build();
    }

    private Materials2Families() {}
}
