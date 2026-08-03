package gregtech.api.enums.materials;

import static gregtech.api.enums.GTValues.M;
import static gregtech.api.enums.StackSizeLimits.ORE_STACK_SIZE;
import static gregtech.api.enums.StackSizeLimits.OTHER_STACK_SIZE;
import static gregtech.api.material.GTMaterialGenerationFlag.CELL;
import static gregtech.api.material.GTMaterialGenerationFlag.DUST;
import static gregtech.api.material.GTMaterialGenerationFlag.EMPTY;
import static gregtech.api.material.GTMaterialGenerationFlag.GEAR;
import static gregtech.api.material.GTMaterialGenerationFlag.GEM;
import static gregtech.api.material.GTMaterialGenerationFlag.METAL;
import static gregtech.api.material.GTMaterialGenerationFlag.ORE;
import static gregtech.api.material.GTMaterialGenerationFlag.PLASMA;
import static gregtech.api.material.GTMaterialGenerationFlag.TOOL_HEAD;

import java.util.List;
import java.util.Set;

import gregtech.api.enums.TCAspects;
import gregtech.api.enums.TCAspects.TC_AspectStack;
import gregtech.api.material.GTShapeProperties;
import gregtech.api.objects.MaterialStack;

/// The per-form data GregTech attaches to its MaterialLib shapes: how much material one item of the shape
/// holds, how it unifies, which texture slot it draws, and the rest of what a form knows about itself
/// independently of the material taking it. This is where those values are declared; the shape-backed
/// [gregtech.api.enums.OrePrefixes] take them from here at load.
///
/// Set on the shape objects directly rather than through
/// `com.ruling_0.materiallib.api.MaterialLibAPI#editShape`: these are GregTech's own shapes, so this
/// declares their data. The edit path addresses a shape by name and applies after unification, which is
/// what another mod changing someone else's shape needs and would wrongly let this beat a competing
/// owner's declaration. Runs inside `com.ruling_0.materiallib.api.MaterialRegistrationEvent`, after the
/// shape declarations and before shapes resolve, while the shapes are still mutable.
///
/// A value equal to the [GTShapeProperties] key's default is omitted.
public final class ShapeData {

    private ShapeData() {}

    public static void init() {
        BlockShapes.block.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 9)
            .setProperty(GTShapeProperties.DEFAULT_STACK_SIZE, OTHER_STACK_SIZE)
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Block of %s")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true);
        BlockShapes.blockCasing.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 9)
            .setProperty(GTShapeProperties.DEFAULT_STACK_SIZE, OTHER_STACK_SIZE)
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Bolted %s Casing")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.SELF_REFERENCING, true)
            .setProperty(GTShapeProperties.CONTAINER, true);
        BlockShapes.blockCasingAdvanced.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 9)
            .setProperty(GTShapeProperties.DEFAULT_STACK_SIZE, OTHER_STACK_SIZE)
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Rebolted %s Casing")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.SELF_REFERENCING, true)
            .setProperty(GTShapeProperties.CONTAINER, true);
        Shapes.bolt.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M / 8)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(METAL, GEM))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "%s Bolt")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true);
        TEBlockShapes.cableGt01.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M / 2)
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "1x %s Cable")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.ELECTRUM, 1L)));
        TEBlockShapes.cableGt02.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 1)
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "2x %s Cable")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.ELECTRUM, 1L)));
        TEBlockShapes.cableGt04.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 2)
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "4x %s Cable")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.ELECTRUM, 1L)));
        TEBlockShapes.cableGt08.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 4)
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "8x %s Cable")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.ELECTRUM, 1L)));
        TEBlockShapes.cableGt12.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 6)
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "12x %s Cable")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.ELECTRUM, 1L)));
        TEBlockShapes.cableGt16.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 8)
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "16x %s Cable")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.ELECTRUM, 1L)));
        CellShapes.cell.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 1)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(CELL, EMPTY))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "%s Cell")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.SELF_REFERENCING, true)
            .setProperty(GTShapeProperties.CONTAINER, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.SECONDARY_MATERIAL, new MaterialStack(Materials.Tin, M * 2));
        CellShapes.cellHydroCracked1.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 1)
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Lightly Hydro-Cracked %s Cell")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.SELF_REFERENCING, true)
            .setProperty(GTShapeProperties.CONTAINER, true)
            .setProperty(GTShapeProperties.SECONDARY_MATERIAL, new MaterialStack(Materials.Tin, M * 2));
        CellShapes.cellHydroCracked2.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 1)
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Moderately Hydro-Cracked %s Cell")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.SELF_REFERENCING, true)
            .setProperty(GTShapeProperties.CONTAINER, true)
            .setProperty(GTShapeProperties.SECONDARY_MATERIAL, new MaterialStack(Materials.Tin, M * 2));
        CellShapes.cellHydroCracked3.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 1)
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Severely Hydro-Cracked %s Cell")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.SELF_REFERENCING, true)
            .setProperty(GTShapeProperties.CONTAINER, true)
            .setProperty(GTShapeProperties.SECONDARY_MATERIAL, new MaterialStack(Materials.Tin, M * 2));
        CellShapes.cellMolten.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 1)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(TOOL_HEAD))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Molten %s Cell")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.SELF_REFERENCING, true)
            .setProperty(GTShapeProperties.CONTAINER, true)
            .setProperty(GTShapeProperties.HEAT_DAMAGE, 3.0F)
            .setProperty(GTShapeProperties.SECONDARY_MATERIAL, new MaterialStack(Materials.Tin, M * 2));
        CellShapes.cellPlasma.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 1)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(PLASMA))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "%s Plasma Cell")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.SELF_REFERENCING, true)
            .setProperty(GTShapeProperties.CONTAINER, true)
            .setProperty(GTShapeProperties.HEAT_DAMAGE, 6.0F)
            .setProperty(GTShapeProperties.SECONDARY_MATERIAL, new MaterialStack(Materials.Tin, M * 2));
        CellShapes.cellPlasmaLight // from prefix cellPlasma
            .setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 1)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(PLASMA))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "%s Plasma Cell")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.SELF_REFERENCING, true)
            .setProperty(GTShapeProperties.CONTAINER, true)
            .setProperty(GTShapeProperties.HEAT_DAMAGE, 6.0F)
            .setProperty(GTShapeProperties.SECONDARY_MATERIAL, new MaterialStack(Materials.Tin, M * 2));
        CellShapes.cellSteamCracked1.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 1)
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Lightly Steam-Cracked %s Cell")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.SELF_REFERENCING, true)
            .setProperty(GTShapeProperties.CONTAINER, true)
            .setProperty(GTShapeProperties.SECONDARY_MATERIAL, new MaterialStack(Materials.Tin, M * 2));
        CellShapes.cellSteamCracked2.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 1)
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Moderately Steam-Cracked %s Cell")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.SELF_REFERENCING, true)
            .setProperty(GTShapeProperties.CONTAINER, true)
            .setProperty(GTShapeProperties.SECONDARY_MATERIAL, new MaterialStack(Materials.Tin, M * 2));
        CellShapes.cellSteamCracked3.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 1)
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Severely Steam-Cracked %s Cell")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.SELF_REFERENCING, true)
            .setProperty(GTShapeProperties.CONTAINER, true)
            .setProperty(GTShapeProperties.SECONDARY_MATERIAL, new MaterialStack(Materials.Tin, M * 2));
        Shapes.crushed.setProperty(GTShapeProperties.DEFAULT_STACK_SIZE, ORE_STACK_SIZE)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(ORE))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Crushed %s Ore")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.SECONDARY_MATERIAL, new MaterialStack(Materials.Stone, M * 1))
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.PERFODIO, 1L)));
        Shapes.crushedCentrifuged.setProperty(GTShapeProperties.DEFAULT_STACK_SIZE, ORE_STACK_SIZE)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(ORE))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Centrifuged %s Ore")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.PERFODIO, 1L)));
        Shapes.crushedPurified.setProperty(GTShapeProperties.DEFAULT_STACK_SIZE, ORE_STACK_SIZE)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(ORE))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Purified %s Ore")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.PERFODIO, 1L)));
        Shapes.dust.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 1)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(DUST, METAL, GEM, ORE))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "%s Dust")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.PERDITIO, 1L)));
        Shapes.dustImpure.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 1)
            .setProperty(GTShapeProperties.DEFAULT_STACK_SIZE, ORE_STACK_SIZE)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(ORE))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Impure Pile of %s Dust")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.PERDITIO, 1L)));
        Shapes.dustPure.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 1)
            .setProperty(GTShapeProperties.DEFAULT_STACK_SIZE, ORE_STACK_SIZE)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(ORE))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Purified Pile of %s Dust")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.PERDITIO, 1L)));
        Shapes.dustSmall.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M / 4)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(DUST, METAL, GEM, ORE))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Small Pile of %s Dust")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.PERDITIO, 1L)));
        Shapes.dustTiny.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M / 9)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(DUST, METAL, GEM, ORE))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Tiny Pile of %s Dust")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.PERDITIO, 1L)));
        Shapes.foil.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M / 4)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(METAL))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "%s Foil")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true);
        TEBlockShapes.frameGt.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 2)
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "%s Frame Box")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.SKIP_ACTIVE_UNIFICATION, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.FABRICO, 1L)));
        Shapes.gearGt.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 4)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(GEAR))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "%s Gear")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(
                GTShapeProperties.ASPECTS,
                List.of(new TC_AspectStack(TCAspects.MOTUS, 1L), new TC_AspectStack(TCAspects.MACHINA, 1L)));
        Shapes.gearGtSmall.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 1)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(GEAR))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Small %s Gear")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(
                GTShapeProperties.ASPECTS,
                List.of(new TC_AspectStack(TCAspects.MOTUS, 1L), new TC_AspectStack(TCAspects.MACHINA, 1L)));
        Shapes.gem.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 1)
            .setProperty(GTShapeProperties.DEFAULT_STACK_SIZE, OTHER_STACK_SIZE)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(GEM))
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.SELF_REFERENCING, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.VITREUS, 1L)));
        Shapes.gemChipped.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M / 4)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(GEM))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Chipped %s")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.SELF_REFERENCING, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.VITREUS, 1L)));
        Shapes.gemExquisite.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 4)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(GEM))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Exquisite %s")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.SELF_REFERENCING, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.VITREUS, 1L)));
        Shapes.gemFlawed.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M / 2)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(GEM))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Flawed %s")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.SELF_REFERENCING, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.VITREUS, 1L)));
        Shapes.gemFlawless.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 2)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(GEM))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Flawless %s")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.SELF_REFERENCING, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.VITREUS, 1L)));
        Shapes.ingot.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 1)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(METAL))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "%s Ingot")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.METALLUM, 1L)));
        Shapes.ingotHot.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 1)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(METAL))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Hot %s Ingot")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.HEAT_DAMAGE, 3.0F)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.METALLUM, 1L)));
        Shapes.itemCasing.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M / 2)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(METAL, GEM))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "%s Casing")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true);
        TEBlockShapes.itemPipeHuge // from prefix pipeHuge
            .setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 12)
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Huge %s Pipe")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.SKIP_ACTIVE_UNIFICATION, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.ITER, 1L)));
        TEBlockShapes.itemPipeLarge // from prefix pipeLarge
            .setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 6)
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Large %s Pipe")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.SKIP_ACTIVE_UNIFICATION, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.ITER, 1L)));
        TEBlockShapes.itemPipeMedium // from prefix pipeMedium
            .setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 3)
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Medium %s Pipe")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.SKIP_ACTIVE_UNIFICATION, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.ITER, 1L)));
        TEBlockShapes.itemPipeRestrictiveHuge // from prefix pipeRestrictiveHuge
            .setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 12)
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Huge Restrictive %s Pipe")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.SKIP_ACTIVE_UNIFICATION, true)
            .setProperty(GTShapeProperties.SECONDARY_MATERIAL, new MaterialStack(Materials.Steel, M / 4 * 5))
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.ITER, 1L)));
        TEBlockShapes.itemPipeRestrictiveLarge // from prefix pipeRestrictiveLarge
            .setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 6)
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Large Restrictive %s Pipe")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.SKIP_ACTIVE_UNIFICATION, true)
            .setProperty(GTShapeProperties.SECONDARY_MATERIAL, new MaterialStack(Materials.Steel, M / 4 * 4))
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.ITER, 1L)));
        TEBlockShapes.itemPipeRestrictiveMedium // from prefix pipeRestrictiveMedium
            .setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 3)
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Medium Restrictive %s Pipe")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.SKIP_ACTIVE_UNIFICATION, true)
            .setProperty(GTShapeProperties.SECONDARY_MATERIAL, new MaterialStack(Materials.Steel, M / 4 * 3))
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.ITER, 1L)));
        TEBlockShapes.itemPipeRestrictiveSmall // from prefix pipeRestrictiveSmall
            .setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 1)
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Small Restrictive %s Pipe")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.SKIP_ACTIVE_UNIFICATION, true)
            .setProperty(GTShapeProperties.SECONDARY_MATERIAL, new MaterialStack(Materials.Steel, M / 4 * 2))
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.ITER, 1L)));
        TEBlockShapes.itemPipeRestrictiveTiny // from prefix pipeRestrictiveTiny
            .setProperty(GTShapeProperties.MATERIAL_AMOUNT, M / 2)
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Tiny Restrictive %s Pipe")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.SKIP_ACTIVE_UNIFICATION, true)
            .setProperty(GTShapeProperties.SECONDARY_MATERIAL, new MaterialStack(Materials.Steel, M / 4))
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.ITER, 1L)));
        TEBlockShapes.itemPipeSmall // from prefix pipeSmall
            .setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 1)
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Small %s Pipe")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.SKIP_ACTIVE_UNIFICATION, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.ITER, 1L)));
        TEBlockShapes.itemPipeTiny // from prefix pipeTiny
            .setProperty(GTShapeProperties.MATERIAL_AMOUNT, M / 2)
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Tiny %s Pipe")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.SKIP_ACTIVE_UNIFICATION, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.ITER, 1L)));
        Shapes.lens.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 3 / 4)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(GEM))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "%s Lens")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.VITREUS, 1L)));
        Shapes.milled.setProperty(GTShapeProperties.DEFAULT_STACK_SIZE, ORE_STACK_SIZE)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(ORE))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Milled %s Ore")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true);
        Shapes.nugget.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M / 9)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(METAL))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "%s Nugget")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.METALLUM, 1L)));
        OreShapes.ore.setProperty(GTShapeProperties.DEFAULT_STACK_SIZE, ORE_STACK_SIZE)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(ORE))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "%s Ore")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.SECONDARY_MATERIAL, new MaterialStack(Materials.Stone, M * 1))
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.TERRA, 1L)));
        OreShapes.oreSmall.setProperty(GTShapeProperties.DEFAULT_STACK_SIZE, ORE_STACK_SIZE)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(ORE))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Small %s Ore")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.SECONDARY_MATERIAL, new MaterialStack(Materials.Stone, M * 2))
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.TERRA, 1L)));
        TEBlockShapes.pipeHuge.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 12)
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Huge %s Pipe")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.SKIP_ACTIVE_UNIFICATION, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.ITER, 1L)));
        TEBlockShapes.pipeLarge.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 6)
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Large %s Pipe")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.SKIP_ACTIVE_UNIFICATION, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.ITER, 1L)));
        TEBlockShapes.pipeMedium.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 3)
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Medium %s Pipe")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.SKIP_ACTIVE_UNIFICATION, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.ITER, 1L)));
        TEBlockShapes.pipeNonuple.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 9)
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Nonuple %s Pipe")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.SKIP_ACTIVE_UNIFICATION, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.ITER, 1L)));
        TEBlockShapes.pipeQuadruple.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 12)
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Quadruple %s Pipe")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.SKIP_ACTIVE_UNIFICATION, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.ITER, 1L)));
        TEBlockShapes.pipeSmall.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 1)
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Small %s Pipe")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.SKIP_ACTIVE_UNIFICATION, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.ITER, 1L)));
        TEBlockShapes.pipeTiny.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M / 2)
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Tiny %s Pipe")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.SKIP_ACTIVE_UNIFICATION, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.ITER, 1L)));
        Shapes.plate.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 1)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(METAL, GEM))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "%s Plate")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.FABRICO, 1L)));
        Shapes.plateDense.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 9)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(METAL))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Dense %s Plate")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.FABRICO, 1L)));
        Shapes.plateDouble.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 2)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(METAL))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Double %s Plate")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.FABRICO, 1L)));
        Shapes.plateQuadruple.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 4)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(METAL))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Quadruple %s Plate")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.FABRICO, 1L)));
        Shapes.plateQuintuple.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 5)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(METAL))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Quintuple %s Plate")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.FABRICO, 1L)));
        Shapes.plateSuperdense.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 64)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(METAL))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Superdense %s Plate")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.FABRICO, 1L)));
        Shapes.plateTriple.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 3)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(METAL))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Triple %s Plate")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.FABRICO, 1L)));
        Shapes.rawOre.setProperty(GTShapeProperties.DEFAULT_STACK_SIZE, ORE_STACK_SIZE)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(ORE))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Raw %s Ore")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.SECONDARY_MATERIAL, new MaterialStack(Materials.Stone, M * 1));
        Shapes.ring.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M / 4)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(METAL))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "%s Ring")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true);
        Shapes.rotor.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 4 + M / 4)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(GEAR))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "%s Rotor")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true);
        Shapes.round.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M / 9)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(METAL))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "%s Round")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true);
        Shapes.screw.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M / 8)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(METAL, GEM))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "%s Screw")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true);
        BlockShapes.sheetmetal.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 2)
            .setProperty(GTShapeProperties.DEFAULT_STACK_SIZE, OTHER_STACK_SIZE)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(METAL))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "%s Sheetmetal")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true);
        Shapes.spring.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 1)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(METAL))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "%s Spring")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true);
        Shapes.springSmall.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M / 4)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(METAL))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Small %s Spring")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true);
        Shapes.stick.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M / 2)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(METAL, GEM))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "%s Rod")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true);
        Shapes.stickLong.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 1)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(METAL, GEM))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Long %s Rod")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true);
        Shapes.toolHeadBuzzSaw.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 4)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(TOOL_HEAD))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "%s Buzzsaw Blade")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.INSTRUMENTUM, 2L)));
        Shapes.toolHeadChainsaw.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 2)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(TOOL_HEAD))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "%s Chainsaw Tip")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.SECONDARY_MATERIAL, new MaterialStack(Materials.Steel, M * 4 + M / 4 * 2))
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.INSTRUMENTUM, 2L)));
        Shapes.toolHeadDrill.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 4)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(TOOL_HEAD))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "%s Drill Tip")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.INSTRUMENTUM, 2L)));
        Shapes.toolHeadFile.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 2)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(TOOL_HEAD))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "%s File Head")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.INSTRUMENTUM, 2L)));
        Shapes.toolHeadHammer.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 6)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(TOOL_HEAD))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "%s Hammer Head")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.INSTRUMENTUM, 2L)));
        Shapes.toolHeadSaw.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 2)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(TOOL_HEAD))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "%s Saw Blade")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.INSTRUMENTUM, 2L)));
        Shapes.toolHeadWrench.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 4)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(TOOL_HEAD))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "%s Wrench Tip")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.SECONDARY_MATERIAL, new MaterialStack(Materials.Steel, M / 4 + M / 8 * 2))
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.INSTRUMENTUM, 2L)));
        Shapes.turbineBlade.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 6)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(TOOL_HEAD))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "%s Turbine Blade")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true);
        Shapes.wireFine.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M / 8)
            .setProperty(GTShapeProperties.GENERATION_FLAGS, Set.of(METAL))
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "Fine %s Wire")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.ELECTRUM, 1L)));
        TEBlockShapes.wireGt01.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M / 2)
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "1x %s Wire")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.ELECTRUM, 1L)));
        TEBlockShapes.wireGt02.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 1)
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "2x %s Wire")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.ELECTRUM, 1L)));
        TEBlockShapes.wireGt04.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 2)
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "4x %s Wire")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.ELECTRUM, 1L)));
        TEBlockShapes.wireGt08.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 4)
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "8x %s Wire")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.ELECTRUM, 1L)));
        TEBlockShapes.wireGt12.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 6)
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "12x %s Wire")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.ELECTRUM, 1L)));
        TEBlockShapes.wireGt16.setProperty(GTShapeProperties.MATERIAL_AMOUNT, M * 8)
            .setProperty(GTShapeProperties.LOCAL_NAME_FORMAT, "16x %s Wire")
            .setProperty(GTShapeProperties.UNIFIABLE, true)
            .setProperty(GTShapeProperties.MATERIAL_BASED, true)
            .setProperty(GTShapeProperties.RECYCLABLE, true)
            .setProperty(GTShapeProperties.ASPECTS, List.of(new TC_AspectStack(TCAspects.ELECTRUM, 1L)));
    }
}
