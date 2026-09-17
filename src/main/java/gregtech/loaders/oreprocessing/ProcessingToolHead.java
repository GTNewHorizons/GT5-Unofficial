package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.extruderRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.recipe.RecipeMaps.formingPressRecipes;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TCAspects;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.items.IDMetaTool01;
import gregtech.common.items.MetaGeneratedTool01;
import gregtech.common.items.tools.GTToolItems;
import gregtech.common.items.tools.ToolChainsawItem;
import gregtech.common.items.tools.ToolDrillItem;
import gregtech.common.items.tools.ToolFileElectricItem;
import gregtech.common.items.tools.ToolJackHammerItem;
import gregtech.common.items.tools.ToolScrewdriverElectricItem;
import gregtech.common.items.tools.ToolWireCutterElectricItem;
import gregtech.common.items.tools.ToolWrenchElectricItem;

public class ProcessingToolHead implements gregtech.api.interfaces.IOreRecipeRegistrator { // TODO COMPARE WITH OLD TOOL
                                                                                           // HEAD??? generator

    public ProcessingToolHead() {
        OrePrefixes.toolHeadBuzzSaw.add(this);
        OrePrefixes.toolHeadChainsaw.add(this);
        OrePrefixes.toolHeadDrill.add(this);
        OrePrefixes.toolHeadFile.add(this);
        OrePrefixes.toolHeadSaw.add(this);
        OrePrefixes.toolHeadWrench.add(this);
        OrePrefixes.toolHeadHammer.add(this);
        OrePrefixes.turbineBlade.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        boolean aSpecialRecipeReq1 = aMaterial.mUnifiable && (aMaterial.mMaterialInto == aMaterial)
            && !aMaterial.contains(SubTag.NO_SMASHING);
        boolean aSpecialRecipeReq2 = aMaterial.mUnifiable && (aMaterial.mMaterialInto == aMaterial)
            && !aMaterial.contains(SubTag.NO_WORKING);
        boolean aNoWorking = aMaterial.contains(SubTag.NO_WORKING);
        boolean aProducesSoftMallet = aMaterial.contains(SubTag.BOUNCY) || aMaterial.contains(SubTag.WOOD)
            || aMaterial.contains(SubTag.SOFT);
        switch (aPrefix.getName()) {
            case "toolHeadBuzzSaw" -> {
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.BUZZSAW_LV.ID,
                        1,
                        aMaterial,
                        Materials.Steel,
                        new long[] { 100000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PBM", "dXG", "SGP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_LV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Steel), 'P', OrePrefixes.plate.get(Materials.Steel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.Steel), 'B', ItemList.Battery_RE_LV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.BUZZSAW_LV.ID,
                        1,
                        aMaterial,
                        Materials.Steel,
                        new long[] { 75000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PBM", "dXG", "SGP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_LV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Steel), 'P', OrePrefixes.plate.get(Materials.Steel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.Steel), 'B', ItemList.Battery_RE_LV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.BUZZSAW_LV.ID,
                        1,
                        aMaterial,
                        Materials.Steel,
                        new long[] { 50000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PBM", "dXG", "SGP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_LV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Steel), 'P', OrePrefixes.plate.get(Materials.Steel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.Steel), 'B', ItemList.Battery_RE_LV_Sodium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.BUZZSAW_MV.ID,
                        1,
                        aMaterial,
                        Materials.Aluminium,
                        new long[] { 400000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PBM", "dXG", "SGP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_MV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Aluminium), 'P', OrePrefixes.plate.get(Materials.Aluminium),
                        'G', OrePrefixes.gearGtSmall.get(Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.BUZZSAW_MV.ID,
                        1,
                        aMaterial,
                        Materials.Aluminium,
                        new long[] { 300000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PBM", "dXG", "SGP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_MV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Aluminium), 'P', OrePrefixes.plate.get(Materials.Aluminium),
                        'G', OrePrefixes.gearGtSmall.get(Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.BUZZSAW_MV.ID,
                        1,
                        aMaterial,
                        Materials.Aluminium,
                        new long[] { 200000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PBM", "dXG", "SGP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_MV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Aluminium), 'P', OrePrefixes.plate.get(Materials.Aluminium),
                        'G', OrePrefixes.gearGtSmall.get(Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Sodium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.BUZZSAW_HV.ID,
                        1,
                        aMaterial,
                        Materials.StainlessSteel,
                        new long[] { 1600000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PBM", "dXG", "SGP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_HV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.StainlessSteel), 'P',
                        OrePrefixes.plate.get(Materials.StainlessSteel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.BUZZSAW_HV.ID,
                        1,
                        aMaterial,
                        Materials.StainlessSteel,
                        new long[] { 1200000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PBM", "dXG", "SGP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_HV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.StainlessSteel), 'P',
                        OrePrefixes.plate.get(Materials.StainlessSteel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.BUZZSAW_HV.ID,
                        1,
                        aMaterial,
                        Materials.StainlessSteel,
                        new long[] { 800000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PBM", "dXG", "SGP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_HV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.StainlessSteel), 'P',
                        OrePrefixes.plate.get(Materials.StainlessSteel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Sodium.get(1L) });
                if (aSpecialRecipeReq2) GTModHandler.addCraftingRecipe(
                    GTOreDictUnificator.get(OrePrefixes.toolHeadBuzzSaw, aMaterial, 1L),
                    GTModHandler.RecipeBits.BITS_STD,
                    new Object[] { "wXh", "X X", "fXx", 'X', OrePrefixes.plate.get(aMaterial) });
            }
            case "toolHeadChainsaw" -> {
                GTToolItems.CHAINSAW_LV.registerMaterial(
                    aMaterial,
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.METO, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.ARBOR, 2L));
                GTToolItems.CHAINSAW_MV.registerMaterial(
                    aMaterial,
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.METO, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.ARBOR, 2L));
                GTToolItems.CHAINSAW_HV.registerMaterial(
                    aMaterial,
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.METO, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.ARBOR, 2L));
                addElectricChainsawRecipe(
                    GTToolItems.CHAINSAW_LV,
                    aMaterial,
                    100_000L,
                    aOreDictName,
                    Materials.Steel,
                    ItemList.Electric_Motor_LV,
                    ItemList.Battery_RE_LV_Lithium);
                addElectricChainsawRecipe(
                    GTToolItems.CHAINSAW_LV,
                    aMaterial,
                    75_000L,
                    aOreDictName,
                    Materials.Steel,
                    ItemList.Electric_Motor_LV,
                    ItemList.Battery_RE_LV_Cadmium);
                addElectricChainsawRecipe(
                    GTToolItems.CHAINSAW_LV,
                    aMaterial,
                    50_000L,
                    aOreDictName,
                    Materials.Steel,
                    ItemList.Electric_Motor_LV,
                    ItemList.Battery_RE_LV_Sodium);
                addElectricChainsawRecipe(
                    GTToolItems.CHAINSAW_MV,
                    aMaterial,
                    400_000L,
                    aOreDictName,
                    Materials.Aluminium,
                    ItemList.Electric_Motor_MV,
                    ItemList.Battery_RE_MV_Lithium);
                addElectricChainsawRecipe(
                    GTToolItems.CHAINSAW_MV,
                    aMaterial,
                    300_000L,
                    aOreDictName,
                    Materials.Aluminium,
                    ItemList.Electric_Motor_MV,
                    ItemList.Battery_RE_MV_Cadmium);
                addElectricChainsawRecipe(
                    GTToolItems.CHAINSAW_MV,
                    aMaterial,
                    200_000L,
                    aOreDictName,
                    Materials.Aluminium,
                    ItemList.Electric_Motor_MV,
                    ItemList.Battery_RE_MV_Sodium);
                addElectricChainsawRecipe(
                    GTToolItems.CHAINSAW_HV,
                    aMaterial,
                    1_600_000L,
                    aOreDictName,
                    Materials.StainlessSteel,
                    ItemList.Electric_Motor_HV,
                    ItemList.Battery_RE_HV_Lithium);
                addElectricChainsawRecipe(
                    GTToolItems.CHAINSAW_HV,
                    aMaterial,
                    1_200_000L,
                    aOreDictName,
                    Materials.StainlessSteel,
                    ItemList.Electric_Motor_HV,
                    ItemList.Battery_RE_HV_Cadmium);
                addElectricChainsawRecipe(
                    GTToolItems.CHAINSAW_HV,
                    aMaterial,
                    800_000L,
                    aOreDictName,
                    Materials.StainlessSteel,
                    ItemList.Electric_Motor_HV,
                    ItemList.Battery_RE_HV_Sodium);
                if (aSpecialRecipeReq2) GTModHandler.addCraftingRecipe(
                    GTOreDictUnificator.get(OrePrefixes.toolHeadChainsaw, aMaterial, 1L),
                    GTModHandler.RecipeBits.BITS_STD,
                    new Object[] { "SRS", "XhX", "SRS", 'X', OrePrefixes.plate.get(aMaterial), 'S',
                        OrePrefixes.plate.get(Materials.Steel), 'R', OrePrefixes.ring.get(Materials.Steel) });
            }
            case "toolHeadDrill" -> {
                GTToolItems.DRILL_LV.registerMaterial(
                    aMaterial,
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.PERFODIO, 4L));
                GTToolItems.DRILL_MV.registerMaterial(
                    aMaterial,
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.PERFODIO, 4L));
                GTToolItems.DRILL_HV.registerMaterial(
                    aMaterial,
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.PERFODIO, 4L));
                addElectricDrillRecipe(
                    GTToolItems.DRILL_LV,
                    aMaterial,
                    100_000L,
                    aOreDictName,
                    Materials.Steel,
                    ItemList.Electric_Motor_LV,
                    ItemList.Battery_RE_LV_Lithium);
                addElectricDrillRecipe(
                    GTToolItems.DRILL_LV,
                    aMaterial,
                    75_000L,
                    aOreDictName,
                    Materials.Steel,
                    ItemList.Electric_Motor_LV,
                    ItemList.Battery_RE_LV_Cadmium);
                addElectricDrillRecipe(
                    GTToolItems.DRILL_LV,
                    aMaterial,
                    50_000L,
                    aOreDictName,
                    Materials.Steel,
                    ItemList.Electric_Motor_LV,
                    ItemList.Battery_RE_LV_Sodium);
                addElectricDrillRecipe(
                    GTToolItems.DRILL_MV,
                    aMaterial,
                    400_000L,
                    aOreDictName,
                    Materials.Aluminium,
                    ItemList.Electric_Motor_MV,
                    ItemList.Battery_RE_MV_Lithium);
                addElectricDrillRecipe(
                    GTToolItems.DRILL_MV,
                    aMaterial,
                    300_000L,
                    aOreDictName,
                    Materials.Aluminium,
                    ItemList.Electric_Motor_MV,
                    ItemList.Battery_RE_MV_Cadmium);
                addElectricDrillRecipe(
                    GTToolItems.DRILL_MV,
                    aMaterial,
                    200_000L,
                    aOreDictName,
                    Materials.Aluminium,
                    ItemList.Electric_Motor_MV,
                    ItemList.Battery_RE_MV_Sodium);
                addElectricDrillRecipe(
                    GTToolItems.DRILL_HV,
                    aMaterial,
                    1600_000L,
                    aOreDictName,
                    Materials.StainlessSteel,
                    ItemList.Electric_Motor_HV,
                    ItemList.Battery_RE_HV_Lithium);
                addElectricDrillRecipe(
                    GTToolItems.DRILL_HV,
                    aMaterial,
                    1200_000L,
                    aOreDictName,
                    Materials.StainlessSteel,
                    ItemList.Electric_Motor_HV,
                    ItemList.Battery_RE_HV_Cadmium);
                addElectricDrillRecipe(
                    GTToolItems.DRILL_HV,
                    aMaterial,
                    800_000L,
                    aOreDictName,
                    Materials.StainlessSteel,
                    ItemList.Electric_Motor_HV,
                    ItemList.Battery_RE_HV_Sodium);
                // LV Jackhammer
                GTToolItems.JACKHAMMER_LV.registerMaterial(
                    aMaterial,
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.PERFODIO, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.PERDITIO, 2L));
                GTToolItems.JACKHAMMER_MV.registerMaterial(
                    aMaterial,
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.PERFODIO, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.PERDITIO, 2L));
                GTToolItems.JACKHAMMER_HV.registerMaterial(
                    aMaterial,
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.PERFODIO, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.PERDITIO, 2L));
                addElectricJackHammerRecipe(
                    GTToolItems.JACKHAMMER_LV,
                    aMaterial,
                    100_000L,
                    aOreDictName,
                    Materials.Steel,
                    ItemList.Electric_Piston_LV,
                    ItemList.Battery_RE_LV_Lithium);
                addElectricJackHammerRecipe(
                    GTToolItems.JACKHAMMER_LV,
                    aMaterial,
                    75_000L,
                    aOreDictName,
                    Materials.Steel,
                    ItemList.Electric_Piston_LV,
                    ItemList.Battery_RE_LV_Cadmium);
                addElectricJackHammerRecipe(
                    GTToolItems.JACKHAMMER_LV,
                    aMaterial,
                    50_000L,
                    aOreDictName,
                    Materials.Steel,
                    ItemList.Electric_Piston_LV,
                    ItemList.Battery_RE_LV_Sodium);
                addElectricJackHammerRecipe(
                    GTToolItems.JACKHAMMER_MV,
                    aMaterial,
                    400_000L,
                    aOreDictName,
                    Materials.Aluminium,
                    ItemList.Electric_Piston_MV,
                    ItemList.Battery_RE_MV_Lithium);
                addElectricJackHammerRecipe(
                    GTToolItems.JACKHAMMER_MV,
                    aMaterial,
                    300_000L,
                    aOreDictName,
                    Materials.Aluminium,
                    ItemList.Electric_Piston_MV,
                    ItemList.Battery_RE_MV_Cadmium);
                addElectricJackHammerRecipe(
                    GTToolItems.JACKHAMMER_MV,
                    aMaterial,
                    200_000L,
                    aOreDictName,
                    Materials.Aluminium,
                    ItemList.Electric_Piston_MV,
                    ItemList.Battery_RE_MV_Sodium);
                addElectricJackHammerRecipe(
                    GTToolItems.JACKHAMMER_HV,
                    aMaterial,
                    1_600_000L,
                    aOreDictName,
                    Materials.StainlessSteel,
                    ItemList.Electric_Piston_HV,
                    ItemList.Battery_RE_HV_Lithium);
                addElectricJackHammerRecipe(
                    GTToolItems.JACKHAMMER_HV,
                    aMaterial,
                    1_200_000L,
                    aOreDictName,
                    Materials.StainlessSteel,
                    ItemList.Electric_Piston_HV,
                    ItemList.Battery_RE_HV_Cadmium);
                addElectricJackHammerRecipe(
                    GTToolItems.JACKHAMMER_HV,
                    aMaterial,
                    800_000L,
                    aOreDictName,
                    Materials.StainlessSteel,
                    ItemList.Electric_Piston_HV,
                    ItemList.Battery_RE_HV_Sodium);
                if (aSpecialRecipeReq2) {
                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.toolHeadDrill, aMaterial, 1L),
                        GTModHandler.RecipeBits.BITS_STD,
                        new Object[] { "XSX", "XSX", "ShS", 'X', OrePrefixes.plate.get(aMaterial), 'S',
                            OrePrefixes.plate.get(Materials.Steel) });
                    if (aMaterial.mStandardMoltenFluid != null) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(ItemList.Shape_Mold_ToolHeadDrill.get(0))
                            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.toolHeadDrill, aMaterial, 1L))
                            .fluidInputs(aMaterial.getMolten(4 * INGOTS))
                            .duration(5 * SECONDS)
                            .eut(calculateRecipeEU(aMaterial, (int) TierEU.RECIPE_MV))
                            .addTo(fluidSolidifierRecipes);
                    }
                    if (GTOreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L) != null) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(
                                GTOreDictUnificator.get(OrePrefixes.ingot, aMaterial, 4L),
                                ItemList.Shape_Extruder_ToolHeadDrill.get(0))
                            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.toolHeadDrill, aMaterial, 1L))
                            .duration(5 * SECONDS)
                            .eut(calculateRecipeEU(aMaterial, (int) TierEU.RECIPE_MV))
                            .addTo(extruderRecipes);
                    }
                }
            }
            case "toolHeadFile" -> {
                // Each recipe gets its own copy, because addCraftingRecipe can edit the stack it is handed.
                final ItemStack tFile = GTToolItems.FILE.registerMaterial(
                    aMaterial,
                    new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.FABRICO, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L));
                if (tFile != null && aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                    GTModHandler.addShapelessCraftingRecipe(
                        GTUtility.copyAmount(1, tFile),
                        new Object[] { aOreDictName, OrePrefixes.stick.get(aMaterial.mHandleMaterial) });

                    if ((!aMaterial.contains(SubTag.NO_SMASHING)) && (!aMaterial.contains(SubTag.BOUNCY))) {
                        GTModHandler.addCraftingRecipe(
                            GTUtility.copyAmount(1, tFile),
                            GTModHandler.RecipeBits.MIRRORED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                                | GTModHandler.RecipeBits.BUFFERED,
                            new Object[] { "P", "P", "S", 'P', OrePrefixes.plate.get(aMaterial), 'S',
                                OrePrefixes.stick.get(aMaterial.mHandleMaterial) });
                    }
                }
                if (tFile != null
                    && GTOreDictUnificator.get(OrePrefixes.stick, aMaterial.mHandleMaterial, 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            GTOreDictUnificator.get(OrePrefixes.stick, aMaterial.mHandleMaterial, 1L),
                            GTOreDictUnificator.get(OrePrefixes.toolHeadFile, aMaterial, 1L))
                        .circuit(15)
                        .itemOutputs(GTUtility.copyAmount(1, tFile))
                        .duration(10 * SECONDS)
                        .eut(calculateRecipeEU(aMaterial, (int) TierEU.RECIPE_MV))
                        .addTo(assemblerRecipes);
                }
            }
            case "toolHeadSaw" -> {
                final ItemStack tSaw = GTToolItems.SAW.registerMaterial(
                    aMaterial,
                    new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.METO, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.ARBOR, 2L));
                if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {

                    if (tSaw != null) GTModHandler.addShapelessCraftingRecipe(
                        GTUtility.copyAmount(1, tSaw),
                        new Object[] { aOreDictName, OrePrefixes.stick.get(aMaterial.mHandleMaterial) });

                    if (aSpecialRecipeReq1) GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.toolHeadSaw, aMaterial, 1L),
                        GTModHandler.RecipeBits.BITS_STD,
                        new Object[] { "PP ", "fh ", 'P', OrePrefixes.plate.get(aMaterial), 'I',
                            OrePrefixes.ingot.get(aMaterial) });

                    if (!aNoWorking) GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.toolHeadSaw, aMaterial, 1L),
                        GTModHandler.RecipeBits.BITS_STD,
                        new Object[] { "GGf", 'G', OrePrefixes.gem.get(aMaterial) });
                }
                if (tSaw != null && GTOreDictUnificator.get(OrePrefixes.stick, aMaterial.mHandleMaterial, 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            GTOreDictUnificator.get(OrePrefixes.stick, aMaterial.mHandleMaterial, 1L),
                            GTOreDictUnificator.get(OrePrefixes.toolHeadSaw, aMaterial, 1L))
                        .circuit(7)
                        .itemOutputs(GTUtility.copyAmount(1, tSaw))
                        .duration(10 * SECONDS)
                        .eut(calculateRecipeEU(aMaterial, (int) TierEU.RECIPE_MV))
                        .addTo(assemblerRecipes);
                }
            }
            case "toolHeadWrench" -> {
                // The three battery variants of each tier differ only in capacity, which rides along on the stack;
                // the item and its metadata (the head material) are the same, so NEI sees one wrench per material.
                GTToolItems.WRENCH_LV.registerMaterial(
                    aMaterial,
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 4L),
                    new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L));
                GTToolItems.WRENCH_MV.registerMaterial(
                    aMaterial,
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 4L),
                    new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L));
                GTToolItems.WRENCH_HV.registerMaterial(
                    aMaterial,
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 4L),
                    new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L));
                addElectricWrenchRecipe(
                    GTToolItems.WRENCH_LV,
                    aMaterial,
                    100000L,
                    aOreDictName,
                    Materials.Steel,
                    ItemList.Electric_Motor_LV,
                    ItemList.Battery_RE_LV_Lithium);
                addElectricWrenchRecipe(
                    GTToolItems.WRENCH_LV,
                    aMaterial,
                    75000L,
                    aOreDictName,
                    Materials.Steel,
                    ItemList.Electric_Motor_LV,
                    ItemList.Battery_RE_LV_Cadmium);
                addElectricWrenchRecipe(
                    GTToolItems.WRENCH_LV,
                    aMaterial,
                    50000L,
                    aOreDictName,
                    Materials.Steel,
                    ItemList.Electric_Motor_LV,
                    ItemList.Battery_RE_LV_Sodium);
                addElectricWrenchRecipe(
                    GTToolItems.WRENCH_MV,
                    aMaterial,
                    400000L,
                    aOreDictName,
                    Materials.Aluminium,
                    ItemList.Electric_Motor_MV,
                    ItemList.Battery_RE_MV_Lithium);
                addElectricWrenchRecipe(
                    GTToolItems.WRENCH_MV,
                    aMaterial,
                    300000L,
                    aOreDictName,
                    Materials.Aluminium,
                    ItemList.Electric_Motor_MV,
                    ItemList.Battery_RE_MV_Cadmium);
                addElectricWrenchRecipe(
                    GTToolItems.WRENCH_MV,
                    aMaterial,
                    200000L,
                    aOreDictName,
                    Materials.Aluminium,
                    ItemList.Electric_Motor_MV,
                    ItemList.Battery_RE_MV_Sodium);
                addElectricWrenchRecipe(
                    GTToolItems.WRENCH_HV,
                    aMaterial,
                    1600000L,
                    aOreDictName,
                    Materials.StainlessSteel,
                    ItemList.Electric_Motor_HV,
                    ItemList.Battery_RE_HV_Lithium);
                addElectricWrenchRecipe(
                    GTToolItems.WRENCH_HV,
                    aMaterial,
                    1200000L,
                    aOreDictName,
                    Materials.StainlessSteel,
                    ItemList.Electric_Motor_HV,
                    ItemList.Battery_RE_HV_Cadmium);
                addElectricWrenchRecipe(
                    GTToolItems.WRENCH_HV,
                    aMaterial,
                    800000L,
                    aOreDictName,
                    Materials.StainlessSteel,
                    ItemList.Electric_Motor_HV,
                    ItemList.Battery_RE_HV_Sodium);
                GTToolItems.SCREWDRIVER_LV.registerMaterial(
                    aMaterial,
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.FABRICO, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L));
                GTToolItems.SCREWDRIVER_MV.registerMaterial(
                    aMaterial,
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.FABRICO, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L));
                GTToolItems.SCREWDRIVER_HV.registerMaterial(
                    aMaterial,
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.FABRICO, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L));
                addElectricScrewdriverRecipe(
                    GTToolItems.SCREWDRIVER_LV,
                    aMaterial,
                    100000L,
                    Materials.Steel,
                    ItemList.Electric_Motor_LV,
                    ItemList.Battery_RE_LV_Lithium);
                addElectricScrewdriverRecipe(
                    GTToolItems.SCREWDRIVER_LV,
                    aMaterial,
                    75000L,
                    Materials.Steel,
                    ItemList.Electric_Motor_LV,
                    ItemList.Battery_RE_LV_Cadmium);
                addElectricScrewdriverRecipe(
                    GTToolItems.SCREWDRIVER_LV,
                    aMaterial,
                    50000L,
                    Materials.Steel,
                    ItemList.Electric_Motor_LV,
                    ItemList.Battery_RE_LV_Sodium);
                addElectricScrewdriverRecipe(
                    GTToolItems.SCREWDRIVER_MV,
                    aMaterial,
                    400000L,
                    Materials.Aluminium,
                    ItemList.Electric_Motor_MV,
                    ItemList.Battery_RE_MV_Lithium);
                addElectricScrewdriverRecipe(
                    GTToolItems.SCREWDRIVER_MV,
                    aMaterial,
                    300000L,
                    Materials.Aluminium,
                    ItemList.Electric_Motor_MV,
                    ItemList.Battery_RE_MV_Cadmium);
                addElectricScrewdriverRecipe(
                    GTToolItems.SCREWDRIVER_MV,
                    aMaterial,
                    200000L,
                    Materials.Aluminium,
                    ItemList.Electric_Motor_MV,
                    ItemList.Battery_RE_MV_Sodium);
                addElectricScrewdriverRecipe(
                    GTToolItems.SCREWDRIVER_HV,
                    aMaterial,
                    1600000L,
                    Materials.StainlessSteel,
                    ItemList.Electric_Motor_HV,
                    ItemList.Battery_RE_HV_Lithium);
                addElectricScrewdriverRecipe(
                    GTToolItems.SCREWDRIVER_HV,
                    aMaterial,
                    1200000L,
                    Materials.StainlessSteel,
                    ItemList.Electric_Motor_HV,
                    ItemList.Battery_RE_HV_Cadmium);
                addElectricScrewdriverRecipe(
                    GTToolItems.SCREWDRIVER_HV,
                    aMaterial,
                    800000L,
                    Materials.StainlessSteel,
                    ItemList.Electric_Motor_HV,
                    ItemList.Battery_RE_HV_Sodium);
                // Electric wire cutters, built around a hand wire cutter of the same material.
                GTToolItems.WIRE_CUTTER_LV.registerMaterial(
                    aMaterial,
                    new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 6),
                    new TCAspects.TC_AspectStack(TCAspects.FABRICO, 3),
                    new TCAspects.TC_AspectStack(TCAspects.ORDO, 3));
                GTToolItems.WIRE_CUTTER_MV.registerMaterial(
                    aMaterial,
                    new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 6),
                    new TCAspects.TC_AspectStack(TCAspects.FABRICO, 3),
                    new TCAspects.TC_AspectStack(TCAspects.ORDO, 3));
                GTToolItems.WIRE_CUTTER_HV.registerMaterial(
                    aMaterial,
                    new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 6),
                    new TCAspects.TC_AspectStack(TCAspects.FABRICO, 3),
                    new TCAspects.TC_AspectStack(TCAspects.ORDO, 3));
                addElectricWireCutterRecipe(
                    GTToolItems.WIRE_CUTTER_LV,
                    aMaterial,
                    100000L,
                    ItemList.Electric_Motor_LV,
                    ItemList.Battery_RE_LV_Lithium);
                addElectricWireCutterRecipe(
                    GTToolItems.WIRE_CUTTER_LV,
                    aMaterial,
                    75000L,
                    ItemList.Electric_Motor_LV,
                    ItemList.Battery_RE_LV_Cadmium);
                addElectricWireCutterRecipe(
                    GTToolItems.WIRE_CUTTER_LV,
                    aMaterial,
                    50000L,
                    ItemList.Electric_Motor_LV,
                    ItemList.Battery_RE_LV_Sodium);
                addElectricWireCutterRecipe(
                    GTToolItems.WIRE_CUTTER_MV,
                    aMaterial,
                    400000L,
                    ItemList.Electric_Motor_MV,
                    ItemList.Battery_RE_MV_Lithium);
                addElectricWireCutterRecipe(
                    GTToolItems.WIRE_CUTTER_MV,
                    aMaterial,
                    300000L,
                    ItemList.Electric_Motor_MV,
                    ItemList.Battery_RE_MV_Cadmium);
                addElectricWireCutterRecipe(
                    GTToolItems.WIRE_CUTTER_MV,
                    aMaterial,
                    200000L,
                    ItemList.Electric_Motor_MV,
                    ItemList.Battery_RE_MV_Sodium);
                addElectricWireCutterRecipe(
                    GTToolItems.WIRE_CUTTER_HV,
                    aMaterial,
                    1600000L,
                    ItemList.Electric_Motor_HV,
                    ItemList.Battery_RE_HV_Lithium);
                addElectricWireCutterRecipe(
                    GTToolItems.WIRE_CUTTER_HV,
                    aMaterial,
                    1200000L,
                    ItemList.Electric_Motor_HV,
                    ItemList.Battery_RE_HV_Cadmium);
                addElectricWireCutterRecipe(
                    GTToolItems.WIRE_CUTTER_HV,
                    aMaterial,
                    800000L,
                    ItemList.Electric_Motor_HV,
                    ItemList.Battery_RE_HV_Sodium);

                // Electric files.
                GTToolItems.FILE_LV.registerMaterial(
                    aMaterial,
                    new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.FABRICO, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L));
                GTToolItems.FILE_MV.registerMaterial(
                    aMaterial,
                    new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.FABRICO, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L));
                GTToolItems.FILE_HV.registerMaterial(
                    aMaterial,
                    new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.FABRICO, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L));
                addElectricFileRecipe(
                    GTToolItems.FILE_LV,
                    aMaterial,
                    100000L,
                    Materials.Steel,
                    ItemList.Component_Grinder_Diamond,
                    ItemList.Electric_Motor_LV,
                    ItemList.Battery_RE_LV_Lithium);
                addElectricFileRecipe(
                    GTToolItems.FILE_LV,
                    aMaterial,
                    75000L,
                    Materials.Steel,
                    ItemList.Component_Grinder_Diamond,
                    ItemList.Electric_Motor_LV,
                    ItemList.Battery_RE_LV_Cadmium);
                addElectricFileRecipe(
                    GTToolItems.FILE_LV,
                    aMaterial,
                    50000L,
                    Materials.Steel,
                    ItemList.Component_Grinder_Diamond,
                    ItemList.Electric_Motor_LV,
                    ItemList.Battery_RE_LV_Sodium);
                addElectricFileRecipe(
                    GTToolItems.FILE_MV,
                    aMaterial,
                    400000L,
                    Materials.Aluminium,
                    ItemList.Component_Grinder_Diamond,
                    ItemList.Electric_Motor_MV,
                    ItemList.Battery_RE_MV_Lithium);
                addElectricFileRecipe(
                    GTToolItems.FILE_MV,
                    aMaterial,
                    300000L,
                    Materials.Aluminium,
                    ItemList.Component_Grinder_Diamond,
                    ItemList.Electric_Motor_MV,
                    ItemList.Battery_RE_MV_Cadmium);
                addElectricFileRecipe(
                    GTToolItems.FILE_MV,
                    aMaterial,
                    200000L,
                    Materials.Aluminium,
                    ItemList.Component_Grinder_Diamond,
                    ItemList.Electric_Motor_MV,
                    ItemList.Battery_RE_MV_Sodium);
                addElectricFileRecipe(
                    GTToolItems.FILE_HV,
                    aMaterial,
                    1600000L,
                    Materials.StainlessSteel,
                    ItemList.Component_Grinder_Tungsten,
                    ItemList.Electric_Motor_HV,
                    ItemList.Battery_RE_HV_Lithium);
                addElectricFileRecipe(
                    GTToolItems.FILE_HV,
                    aMaterial,
                    1200000L,
                    Materials.StainlessSteel,
                    ItemList.Component_Grinder_Tungsten,
                    ItemList.Electric_Motor_HV,
                    ItemList.Battery_RE_HV_Cadmium);
                addElectricFileRecipe(
                    GTToolItems.FILE_HV,
                    aMaterial,
                    800000L,
                    Materials.StainlessSteel,
                    ItemList.Component_Grinder_Tungsten,
                    ItemList.Electric_Motor_HV,
                    ItemList.Battery_RE_HV_Sodium);

                // Wrench Special Condition
                if (aSpecialRecipeReq2) GTModHandler.addCraftingRecipe(
                    GTOreDictUnificator.get(OrePrefixes.toolHeadWrench, aMaterial, 1L),
                    GTModHandler.RecipeBits.BITS_STD,
                    new Object[] { "hXW", "XRX", "WXd", 'X', OrePrefixes.plate.get(aMaterial), 'S',
                        OrePrefixes.plate.get(Materials.Steel), 'R', OrePrefixes.ring.get(Materials.Steel), 'W',
                        OrePrefixes.screw.get(Materials.Steel) });
            }
            case "toolHeadHammer", "toolHeadMallet" -> {
                // Whether this material makes a soft mallet or a hard hammer, both are now standalone items built by
                // the same four recipes, so resolve the output once. Each recipe gets its own copy, because
                // addCraftingRecipe can edit the stack it is handed. A null output means the tool has no metadata
                // slot for this material, in which case there is no recipe to add.
                final ItemStack tMalletOrHammer = aProducesSoftMallet
                    ? GTToolItems.SOFT_MALLET.registerMaterial(
                        aMaterial,
                        new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
                        new TCAspects.TC_AspectStack(TCAspects.LIMUS, 4L))
                    : GTToolItems.HARD_HAMMER.registerMaterial(
                        aMaterial,
                        new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
                        new TCAspects.TC_AspectStack(TCAspects.FABRICO, 2L),
                        new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L));
                if (tMalletOrHammer != null
                    && GTOreDictUnificator.get(OrePrefixes.stick, aMaterial.mHandleMaterial, 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            GTOreDictUnificator.get(OrePrefixes.stick, aMaterial.mHandleMaterial, 1L),
                            GTOreDictUnificator.get(OrePrefixes.toolHeadHammer, aMaterial, 1L))
                        .circuit(14)
                        .itemOutputs(GTUtility.copyAmount(1, tMalletOrHammer))
                        .duration(10 * SECONDS)
                        .eut(calculateRecipeEU(aMaterial, (int) TierEU.RECIPE_MV))
                        .addTo(assemblerRecipes);
                }
                if (tMalletOrHammer != null && (aMaterial != Materials.Stone) && (aMaterial != Materials.Flint)) {
                    GTModHandler.addShapelessCraftingRecipe(
                        GTUtility.copyAmount(1, tMalletOrHammer),
                        GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                        new Object[] { aOreDictName, OrePrefixes.stick.get(aMaterial.mHandleMaterial) });
                    GTModHandler.addCraftingRecipe(
                        GTUtility.copyAmount(1, tMalletOrHammer),
                        GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                        new Object[] { "XX ", "XXS", "XX ", 'X',
                            aMaterial == Materials.Wood ? OrePrefixes.plank.get(Materials.Wood)
                                : OrePrefixes.ingot.get(aMaterial),
                            'S', OrePrefixes.stick.get(aMaterial.mHandleMaterial) });
                    GTModHandler.addCraftingRecipe(
                        GTUtility.copyAmount(1, tMalletOrHammer),
                        GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                        new Object[] { "XX ", "XXS", "XX ", 'X',
                            aMaterial == Materials.Wood ? OrePrefixes.plank.get(Materials.Wood)
                                : OrePrefixes.gem.get(aMaterial),
                            'S', OrePrefixes.stick.get(aMaterial.mHandleMaterial) });
                }
                if (aPrefix == OrePrefixes.toolHeadHammer) if (aSpecialRecipeReq1) GTModHandler.addCraftingRecipe(
                    GTOreDictUnificator.get(OrePrefixes.toolHeadHammer, aMaterial, 1L),
                    GTModHandler.RecipeBits.BITS_STD,
                    new Object[] { "II ", "IIh", "II ", 'P', OrePrefixes.plate.get(aMaterial), 'I',
                        OrePrefixes.ingot.get(aMaterial) });
            }
            case "turbineBlade" -> {
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        GTOreDictUnificator.get(OrePrefixes.turbineBlade, aMaterial, 4L),
                        GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Magnalium, 1L))
                    .itemOutputs(MetaGeneratedTool01.INSTANCE.getToolWithStats(170, 1, aMaterial, aMaterial, null))
                    .duration(8 * SECONDS)
                    .eut(calculateRecipeEU(aMaterial, 100))
                    .addTo(assemblerRecipes);
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        GTOreDictUnificator.get(OrePrefixes.turbineBlade, aMaterial, 8L),
                        GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Titanium, 1L))
                    .itemOutputs(MetaGeneratedTool01.INSTANCE.getToolWithStats(172, 1, aMaterial, aMaterial, null))
                    .duration(16 * SECONDS)
                    .eut(calculateRecipeEU(aMaterial, 400))
                    .addTo(assemblerRecipes);
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        GTOreDictUnificator.get(OrePrefixes.turbineBlade, aMaterial, 12L),
                        GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.TungstenSteel, 1L))
                    .itemOutputs(MetaGeneratedTool01.INSTANCE.getToolWithStats(174, 1, aMaterial, aMaterial, null))
                    .duration(32 * SECONDS)
                    .eut(calculateRecipeEU(aMaterial, 1600))
                    .addTo(assemblerRecipes);
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        GTOreDictUnificator.get(OrePrefixes.turbineBlade, aMaterial, 16L),
                        GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Americium, 1L))
                    .itemOutputs(MetaGeneratedTool01.INSTANCE.getToolWithStats(176, 1, aMaterial, aMaterial, null))
                    .duration(1 * MINUTES + 4 * SECONDS)
                    .eut(calculateRecipeEU(aMaterial, 6400))
                    .addTo(assemblerRecipes);
                if (aSpecialRecipeReq2) {
                    if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                        GTModHandler.addCraftingRecipe(
                            GTOreDictUnificator.get(OrePrefixes.turbineBlade, aMaterial, 1L),
                            GTModHandler.RecipeBits.BITS_STD,
                            new Object[] { "fPd", "SPS", " P ", 'P',
                                aMaterial == Materials.Wood ? OrePrefixes.plank.get(aMaterial)
                                    : OrePrefixes.plateDouble.get(aMaterial),
                                'R', OrePrefixes.ring.get(aMaterial), 'S', OrePrefixes.screw.get(aMaterial) });
                    }

                    // Turbine blades
                    if (GTOreDictUnificator.get(OrePrefixes.plateDouble, aMaterial, 1L) != null
                        && GTOreDictUnificator.get(OrePrefixes.screw, aMaterial, 1L) != null) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(
                                GTOreDictUnificator.get(OrePrefixes.plateDouble, aMaterial, 3L),
                                GTOreDictUnificator.get(OrePrefixes.screw, aMaterial, 2L))
                            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.turbineBlade, aMaterial, 1L))
                            .duration(10 * SECONDS)
                            .eut(calculateRecipeEU(aMaterial, 60))
                            .addTo(formingPressRecipes);
                    }
                }
            }
            default -> {}
        }
    }

    /**
     * Adds one electric wrench crafting recipe. The nine of them differ only in tier, casing material and battery, so
     * they share this shape rather than being spelled out one by one as they were when each was a separate
     * {@code getToolWithStats} call.
     *
     * @param maxCharge the capacity the battery gives this wrench; recorded on the stack, since all three batteries
     *                  of a tier produce the same item and metadata.
     */
    private static void addElectricWrenchRecipe(ToolWrenchElectricItem wrenchItem, Materials headMaterial,
        long maxCharge, String headOreDictName, Materials casingMaterial, ItemList motor, ItemList battery) {
        ItemStack wrench = wrenchItem.getToolWithMaterial(headMaterial, maxCharge);
        if (wrench == null) return;
        GTModHandler.addCraftingRecipe(
            wrench,
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SXd", "GMG", "PBP", 'X', headOreDictName, 'M', motor.get(1L), 'S',
                OrePrefixes.screw.get(casingMaterial), 'P', OrePrefixes.plate.get(casingMaterial), 'G',
                OrePrefixes.gearGtSmall.get(casingMaterial), 'B', battery.get(1L) });
    }

    /**
     * Adds one electric screwdriver crafting recipe. The layout differs from the wrench's, and the head is a long rod
     * of the head material rather than a tool head, but otherwise this is the same shape as
     * {@link #addElectricWrenchRecipe}.
     */
    private static void addElectricScrewdriverRecipe(ToolScrewdriverElectricItem screwdriverItem,
        Materials headMaterial, long maxCharge, Materials casingMaterial, ItemList motor, ItemList battery) {
        ItemStack screwdriver = screwdriverItem.getToolWithMaterial(headMaterial, maxCharge);
        if (screwdriver == null) return;
        GTModHandler.addCraftingRecipe(
            screwdriver,
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PdX", "MGS", "GBP", 'X', OrePrefixes.stickLong.get(headMaterial), 'M', motor.get(1L), 'S',
                OrePrefixes.screw.get(casingMaterial), 'P', OrePrefixes.plate.get(casingMaterial), 'G',
                OrePrefixes.gearGtSmall.get(casingMaterial), 'B', battery.get(1L) });
    }

    /**
     * Adds one electric wire cutter crafting recipe. Unlike the other electric tools this one is built around a hand
     * wire cutter of the same material rather than a bare tool head, so the ingredient comes from the same metadata
     * mapping as the output.
     */
    private static void addElectricWireCutterRecipe(ToolWireCutterElectricItem wireCutterItem, Materials headMaterial,
        long maxCharge, ItemList motor, ItemList battery) {
        ItemStack wireCutter = wireCutterItem.getToolWithMaterial(headMaterial, maxCharge);
        ItemStack handWireCutter = GTToolItems.WIRE_CUTTER.getToolWithMaterial(headMaterial);
        if (wireCutter == null || handWireCutter == null) return;
        GTModHandler.addCraftingRecipe(
            wireCutter,
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SXS", "GMG", "PBP", 'X', handWireCutter, 'M', motor.get(1L), 'S',
                OrePrefixes.wireFine.get(Materials.Electrum), 'P', OrePrefixes.plate.get(headMaterial), 'G',
                OrePrefixes.gearGt.get(Materials.Steel), 'B', battery.get(1L) });
    }

    /**
     * Adds one electric file crafting recipe. Same layout as the electric wrench's, but the head is a grinder
     * component rather than a tool head.
     */
    private static void addElectricFileRecipe(ToolFileElectricItem fileItem, Materials headMaterial, long maxCharge,
        Materials casingMaterial, ItemList grinder, ItemList motor, ItemList battery) {
        ItemStack file = fileItem.getToolWithMaterial(headMaterial, maxCharge);
        if (file == null) return;
        GTModHandler.addCraftingRecipe(
            file,
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SXL", "GMG", "PBP", 'X', grinder.get(1), 'M', motor.get(1L), 'S',
                OrePrefixes.screw.get(casingMaterial), 'L', OrePrefixes.stickLong.get(headMaterial), 'P',
                OrePrefixes.plate.get(headMaterial), 'G', OrePrefixes.gearGt.get(casingMaterial), 'B',
                battery.get(1L) });
    }

    /**
     * Adds one electric drill crafting recipe. Same layout as the electric wrench's, with a drill head.
     */
    private static void addElectricDrillRecipe(ToolDrillItem drillItem, Materials headMaterial, long maxCharge,
        String headOreDictName, Materials casingMaterial, ItemList motor, ItemList battery) {
        ItemStack drill = drillItem.getToolWithMaterial(headMaterial, maxCharge);
        if (drill == null) return;
        GTModHandler.addCraftingRecipe(
            drill,
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SXd", "GMG", "PBP", 'X', headOreDictName, 'M', motor.get(1L), 'S',
                OrePrefixes.screw.get(casingMaterial), 'P', OrePrefixes.plate.get(casingMaterial), 'G',
                OrePrefixes.gearGtSmall.get(casingMaterial), 'B', battery.get(1L) });
    }

    /**
     * Adds one electric chainsaw crafting recipe.
     */
    private static void addElectricChainsawRecipe(ToolChainsawItem toolItem, Materials headMaterial, long maxCharge,
        String headOreDictName, Materials casingMaterial, ItemList motor, ItemList battery) {
        ItemStack tool = toolItem.getToolWithMaterial(headMaterial, maxCharge);
        if (tool == null) return;
        GTModHandler.addCraftingRecipe(
            tool,
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SXd", "GMG", "PBP", 'X', headOreDictName, 'M', motor.get(1L), 'S',
                OrePrefixes.screw.get(casingMaterial), 'P', OrePrefixes.plate.get(casingMaterial), 'G',
                OrePrefixes.gearGtSmall.get(casingMaterial), 'B', battery.get(1L) });
    }

    /**
     * Adds one jackhammer crafting recipe.
     */
    private static void addElectricJackHammerRecipe(ToolJackHammerItem toolItem, Materials headMaterial, long maxCharge,
        String headOreDictName, Materials casingMaterial, ItemList piston, ItemList battery) {
        ItemStack tool = toolItem.getToolWithMaterial(headMaterial, maxCharge);
        if (tool == null) return;
        GTModHandler.addCraftingRecipe(
            tool,
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SXd", "PRP", "MPB", 'X', OrePrefixes.stickLong.get(headMaterial), 'M', piston.get(1L), 'S',
                OrePrefixes.screw.get(casingMaterial), 'P', OrePrefixes.plate.get(casingMaterial), 'R',
                OrePrefixes.spring.get(casingMaterial), 'B', battery.get(1L) });
    }
}
