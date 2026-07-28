package gregtech.api.enums.materials2;

import com.ruling_0.materiallib.api.Family;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.StandardProperties;
import com.ruling_0.materiallib.api.TextureSet;

// One-time output of scripts/mu/gen_shapes.py (RETIRED for this file, see its module docstring);
// hand-maintained from here -- edit this file directly.
/// The shape groups a material joins. A family carries the shapes every member generates, so a member declares
/// only its own exceptions: [Materials2Materials] adds a shape the family does not carry with `generateShape`
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
            .generateShapes(Materials2Shapes.dust, Materials2Shapes.dustSmall, Materials2Shapes.dustTiny)
            .build();
        metals = MaterialLibAPI.newFamily("gregtech", "Metals")
            .generateShapes(
                Materials2Shapes.bolt,
                Materials2Shapes.dust,
                Materials2Shapes.dustSmall,
                Materials2Shapes.dustTiny,
                Materials2Shapes.foil,
                Materials2Shapes.ingot,
                Materials2Shapes.ingotHot,
                Materials2Shapes.itemCasing,
                Materials2Shapes.nugget,
                Materials2Shapes.plate,
                Materials2Shapes.plateDense,
                Materials2Shapes.plateDouble,
                Materials2Shapes.plateSuperdense,
                Materials2Shapes.ring,
                Materials2Shapes.round,
                Materials2Shapes.screw,
                Materials2Shapes.spring,
                Materials2Shapes.springSmall,
                Materials2Shapes.stick,
                Materials2Shapes.stickLong,
                Materials2Shapes.wireFine)
            .build();
        multiPlates = MaterialLibAPI.newFamily("gregtech", "MultiPlates")
            .generateShapes(
                Materials2Shapes.plateTriple,
                Materials2Shapes.plateQuadruple,
                Materials2Shapes.plateQuintuple)
            .build();
        gems = MaterialLibAPI.newFamily("gregtech", "Gems")
            .generateShapes(
                Materials2Shapes.bolt,
                Materials2Shapes.dust,
                Materials2Shapes.dustSmall,
                Materials2Shapes.dustTiny,
                Materials2Shapes.gem,
                Materials2Shapes.itemCasing,
                Materials2Shapes.lens,
                Materials2Shapes.plate,
                Materials2Shapes.screw,
                Materials2Shapes.stick,
                Materials2Shapes.stickLong)
            .build();
        gemQualities = MaterialLibAPI.newFamily("gregtech", "GemQualities")
            .generateShapes(
                Materials2Shapes.gemChipped,
                Materials2Shapes.gemFlawed,
                Materials2Shapes.gemFlawless,
                Materials2Shapes.gemExquisite)
            .build();
        ores = MaterialLibAPI.newFamily("gregtech", "Ores")
            .generateShapes(
                Materials2Shapes.crushed,
                Materials2Shapes.crushedCentrifuged,
                Materials2Shapes.crushedPurified,
                Materials2Shapes.dust,
                Materials2Shapes.dustImpure,
                Materials2Shapes.dustPure,
                Materials2Shapes.dustSmall,
                Materials2Shapes.dustTiny,
                Materials2Shapes.rawOre,
                Materials2OreShapes.ore,
                Materials2OreShapes.oreSmall)
            .build();
        toolParts = MaterialLibAPI.newFamily("gregtech", "ToolParts")
            .generateShapes(
                Materials2Shapes.toolHeadHammer,
                Materials2Shapes.toolHeadSaw,
                Materials2Shapes.toolHeadWrench,
                Materials2Shapes.turbineBlade)
            .build();
        poweredToolParts = MaterialLibAPI.newFamily("gregtech", "PoweredToolParts")
            .generateShapes(
                Materials2Shapes.toolHeadBuzzSaw,
                Materials2Shapes.toolHeadChainsaw,
                Materials2Shapes.toolHeadDrill,
                Materials2Shapes.toolHeadFile)
            .build();
        gears = MaterialLibAPI.newFamily("gregtech", "Gears")
            .generateShapes(Materials2Shapes.gearGt, Materials2Shapes.gearGtSmall, Materials2Shapes.rotor)
            .build();
        liquids = MaterialLibAPI.newFamily("gregtech", "Liquids")
            .generateShapes(Materials2FluidShapes.fluidLiquid, Materials2CellShapes.cell)
            .build();
        gases = MaterialLibAPI.newFamily("gregtech", "Gases")
            .generateShapes(Materials2FluidShapes.fluidGas, Materials2CellShapes.cell)
            .build();
        moltens = MaterialLibAPI.newFamily("gregtech", "Moltens")
            .generateShapes(Materials2FluidShapes.fluidMolten, Materials2CellShapes.cellMolten)
            .build();
        plasmas = MaterialLibAPI.newFamily("gregtech", "Plasmas")
            .generateShape(Materials2FluidShapes.fluidPlasma)
            .build();
    }

    private Materials2Families() {}
}
