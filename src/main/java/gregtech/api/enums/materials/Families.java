package gregtech.api.enums.materials;

import java.util.List;

import com.ruling_0.materiallib.api.Family;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.StandardProperties;
import com.ruling_0.materiallib.api.TextureSet;

import gregtech.api.material.GTMaterialProperties;

/// Groupings for [Materials]. All materials of a family generate its [Shape]s.
///
/// [#all] and [#superconductors] carry no shapes: `all` exists only to give every material a fallback texture
/// set, and `superconductors` only to carry [GTMaterialProperties#IS_SUPERCONDUCTOR] for its members. The
/// superconductor markers take their wire shapes from [PipeMaterials], like every other wire material.
public class Families {

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
    public static Family superconductors;
    // spotless:on

    public static void init() {
        all = MaterialLibAPI.newFamily("gregtech", "All")
            .setProperty(StandardProperties.FALLBACK_TEXTURE_SETS, List.of(TextureSet.of("gregtech", "NONE")))
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
            .generateShapes(Shapes.plateTriple, Shapes.plateQuadruple, Shapes.plateQuintuple)
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
            .generateShapes(Shapes.gemChipped, Shapes.gemFlawed, Shapes.gemFlawless, Shapes.gemExquisite)
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
            .generateShapes(Shapes.toolHeadHammer, Shapes.toolHeadSaw, Shapes.toolHeadWrench, Shapes.turbineBlade)
            .build();
        poweredToolParts = MaterialLibAPI.newFamily("gregtech", "PoweredToolParts")
            .generateShapes(Shapes.toolHeadBuzzSaw, Shapes.toolHeadChainsaw, Shapes.toolHeadDrill, Shapes.toolHeadFile)
            .build();
        gears = MaterialLibAPI.newFamily("gregtech", "Gears")
            .generateShapes(Shapes.gearGt, Shapes.gearGtSmall, Shapes.rotor)
            .build();
        liquids = MaterialLibAPI.newFamily("gregtech", "Liquids")
            .generateShapes(FluidShapes.fluidLiquid, CellShapes.cell)
            .build();
        gases = MaterialLibAPI.newFamily("gregtech", "Gases")
            .generateShapes(FluidShapes.fluidGas, CellShapes.cell)
            .build();
        moltens = MaterialLibAPI.newFamily("gregtech", "Moltens")
            .generateShapes(FluidShapes.fluidMolten, CellShapes.cellMolten)
            .build();
        plasmas = MaterialLibAPI.newFamily("gregtech", "Plasmas")
            .generateShape(FluidShapes.fluidPlasma)
            .build();
        superconductors = MaterialLibAPI.newFamily("gregtech", "Superconductors")
            .setProperty(GTMaterialProperties.IS_SUPERCONDUCTOR, true)
            .build();
    }

    private Families() {}
}
