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
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.common.items.IDMetaTool01;
import gregtech.common.items.MetaGeneratedTool01;

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
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
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
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
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
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
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
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
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
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
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
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
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
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
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
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
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
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
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
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.CHAINSAW_LV.ID,
                        1,
                        aMaterial,
                        Materials.Steel,
                        new long[] { 100000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_LV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Steel), 'P', OrePrefixes.plate.get(Materials.Steel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.Steel), 'B', ItemList.Battery_RE_LV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.CHAINSAW_LV.ID,
                        1,
                        aMaterial,
                        Materials.Steel,
                        new long[] { 75000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_LV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Steel), 'P', OrePrefixes.plate.get(Materials.Steel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.Steel), 'B', ItemList.Battery_RE_LV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.CHAINSAW_LV.ID,
                        1,
                        aMaterial,
                        Materials.Steel,
                        new long[] { 50000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_LV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Steel), 'P', OrePrefixes.plate.get(Materials.Steel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.Steel), 'B', ItemList.Battery_RE_LV_Sodium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.CHAINSAW_MV.ID,
                        1,
                        aMaterial,
                        Materials.Aluminium,
                        new long[] { 400000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_MV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Aluminium), 'P', OrePrefixes.plate.get(Materials.Aluminium),
                        'G', OrePrefixes.gearGtSmall.get(Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.CHAINSAW_MV.ID,
                        1,
                        aMaterial,
                        Materials.Aluminium,
                        new long[] { 300000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_MV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Aluminium), 'P', OrePrefixes.plate.get(Materials.Aluminium),
                        'G', OrePrefixes.gearGtSmall.get(Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.CHAINSAW_MV.ID,
                        1,
                        aMaterial,
                        Materials.Aluminium,
                        new long[] { 200000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_MV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Aluminium), 'P', OrePrefixes.plate.get(Materials.Aluminium),
                        'G', OrePrefixes.gearGtSmall.get(Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Sodium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.CHAINSAW_HV.ID,
                        1,
                        aMaterial,
                        Materials.StainlessSteel,
                        new long[] { 1600000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_HV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.StainlessSteel), 'P',
                        OrePrefixes.plate.get(Materials.StainlessSteel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.CHAINSAW_HV.ID,
                        1,
                        aMaterial,
                        Materials.StainlessSteel,
                        new long[] { 1200000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_HV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.StainlessSteel), 'P',
                        OrePrefixes.plate.get(Materials.StainlessSteel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.CHAINSAW_HV.ID,
                        1,
                        aMaterial,
                        Materials.StainlessSteel,
                        new long[] { 800000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_HV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.StainlessSteel), 'P',
                        OrePrefixes.plate.get(Materials.StainlessSteel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Sodium.get(1L) });
                if (aSpecialRecipeReq2) GTModHandler.addCraftingRecipe(
                    GTOreDictUnificator.get(OrePrefixes.toolHeadChainsaw, aMaterial, 1L),
                    GTModHandler.RecipeBits.BITS_STD,
                    new Object[] { "SRS", "XhX", "SRS", 'X', OrePrefixes.plate.get(aMaterial), 'S',
                        OrePrefixes.plate.get(Materials.Steel), 'R', OrePrefixes.ring.get(Materials.Steel) });
            }
            case "toolHeadDrill" -> {
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.DRILL_LV.ID,
                        1,
                        aMaterial,
                        Materials.Steel,
                        new long[] { 100_000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_LV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Steel), 'P', OrePrefixes.plate.get(Materials.Steel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.Steel), 'B', ItemList.Battery_RE_LV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.DRILL_LV.ID,
                        1,
                        aMaterial,
                        Materials.Steel,
                        new long[] { 75_000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_LV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Steel), 'P', OrePrefixes.plate.get(Materials.Steel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.Steel), 'B', ItemList.Battery_RE_LV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.DRILL_LV.ID,
                        1,
                        aMaterial,
                        Materials.Steel,
                        new long[] { 50_000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_LV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Steel), 'P', OrePrefixes.plate.get(Materials.Steel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.Steel), 'B', ItemList.Battery_RE_LV_Sodium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.DRILL_MV.ID,
                        1,
                        aMaterial,
                        Materials.Aluminium,
                        new long[] { 400_000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_MV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Aluminium), 'P', OrePrefixes.plate.get(Materials.Aluminium),
                        'G', OrePrefixes.gearGtSmall.get(Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.DRILL_MV.ID,
                        1,
                        aMaterial,
                        Materials.Aluminium,
                        new long[] { 300_000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_MV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Aluminium), 'P', OrePrefixes.plate.get(Materials.Aluminium),
                        'G', OrePrefixes.gearGtSmall.get(Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.DRILL_MV.ID,
                        1,
                        aMaterial,
                        Materials.Aluminium,
                        new long[] { 200_000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_MV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Aluminium), 'P', OrePrefixes.plate.get(Materials.Aluminium),
                        'G', OrePrefixes.gearGtSmall.get(Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Sodium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.DRILL_HV.ID,
                        1,
                        aMaterial,
                        Materials.StainlessSteel,
                        new long[] { 1_600_000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_HV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.StainlessSteel), 'P',
                        OrePrefixes.plate.get(Materials.StainlessSteel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.DRILL_HV.ID,
                        1,
                        aMaterial,
                        Materials.StainlessSteel,
                        new long[] { 1_200_000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_HV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.StainlessSteel), 'P',
                        OrePrefixes.plate.get(Materials.StainlessSteel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.DRILL_HV.ID,
                        1,
                        aMaterial,
                        Materials.StainlessSteel,
                        new long[] { 800_000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_HV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.StainlessSteel), 'P',
                        OrePrefixes.plate.get(Materials.StainlessSteel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Sodium.get(1L) });
                // LV Jackhammer
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.JACKHAMMER_LV.ID,
                        1,
                        aMaterial,
                        Materials.Steel,
                        new long[] { 100_000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "PRP", "MPB", 'X', OrePrefixes.stickLong.get(aMaterial), 'M',
                        ItemList.Electric_Piston_LV.get(1L), 'S', OrePrefixes.screw.get(Materials.Steel), 'P',
                        OrePrefixes.plate.get(Materials.Steel), 'R', OrePrefixes.spring.get(Materials.Steel), 'B',
                        ItemList.Battery_RE_LV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.JACKHAMMER_LV.ID,
                        1,
                        aMaterial,
                        Materials.Steel,
                        new long[] { 75_000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "PRP", "MPB", 'X', OrePrefixes.stickLong.get(aMaterial), 'M',
                        ItemList.Electric_Piston_LV.get(1L), 'S', OrePrefixes.screw.get(Materials.Steel), 'P',
                        OrePrefixes.plate.get(Materials.Steel), 'R', OrePrefixes.spring.get(Materials.Steel), 'B',
                        ItemList.Battery_RE_LV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.JACKHAMMER_LV.ID,
                        1,
                        aMaterial,
                        Materials.Steel,
                        new long[] { 50_000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "PRP", "MPB", 'X', OrePrefixes.stickLong.get(aMaterial), 'M',
                        ItemList.Electric_Piston_LV.get(1L), 'S', OrePrefixes.screw.get(Materials.Steel), 'P',
                        OrePrefixes.plate.get(Materials.Steel), 'R', OrePrefixes.spring.get(Materials.Steel), 'B',
                        ItemList.Battery_RE_LV_Sodium.get(1L) });
                // MV Jackhammer
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.JACKHAMMER_MV.ID,
                        1,
                        aMaterial,
                        Materials.Aluminium,
                        new long[] { 400_000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "PRP", "MPB", 'X', OrePrefixes.stickLong.get(aMaterial), 'M',
                        ItemList.Electric_Piston_MV.get(1L), 'S', OrePrefixes.screw.get(Materials.Aluminium), 'P',
                        OrePrefixes.plate.get(Materials.Aluminium), 'R', OrePrefixes.spring.get(Materials.Aluminium),
                        'B', ItemList.Battery_RE_MV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.JACKHAMMER_MV.ID,
                        1,
                        aMaterial,
                        Materials.Aluminium,
                        new long[] { 300_000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "PRP", "MPB", 'X', OrePrefixes.stickLong.get(aMaterial), 'M',
                        ItemList.Electric_Piston_MV.get(1L), 'S', OrePrefixes.screw.get(Materials.Aluminium), 'P',
                        OrePrefixes.plate.get(Materials.Aluminium), 'R', OrePrefixes.spring.get(Materials.Aluminium),
                        'B', ItemList.Battery_RE_MV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.JACKHAMMER_MV.ID,
                        1,
                        aMaterial,
                        Materials.Aluminium,
                        new long[] { 200_000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "PRP", "MPB", 'X', OrePrefixes.stickLong.get(aMaterial), 'M',
                        ItemList.Electric_Piston_MV.get(1L), 'S', OrePrefixes.screw.get(Materials.Aluminium), 'P',
                        OrePrefixes.plate.get(Materials.Aluminium), 'R', OrePrefixes.spring.get(Materials.Aluminium),
                        'B', ItemList.Battery_RE_MV_Sodium.get(1L) });
                // HV Jackhammer
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.JACKHAMMER_HV.ID,
                        1,
                        aMaterial,
                        Materials.StainlessSteel,
                        new long[] { 1_600_000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "PRP", "MPB", 'X', OrePrefixes.stickLong.get(aMaterial), 'M',
                        ItemList.Electric_Piston_HV.get(1L), 'S', OrePrefixes.screw.get(Materials.StainlessSteel), 'P',
                        OrePrefixes.plate.get(Materials.StainlessSteel), 'R',
                        OrePrefixes.spring.get(Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.JACKHAMMER_HV.ID,
                        1,
                        aMaterial,
                        Materials.StainlessSteel,
                        new long[] { 1_200_000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "PRP", "MPB", 'X', OrePrefixes.stickLong.get(aMaterial), 'M',
                        ItemList.Electric_Piston_HV.get(1L), 'S', OrePrefixes.screw.get(Materials.StainlessSteel), 'P',
                        OrePrefixes.plate.get(Materials.StainlessSteel), 'R',
                        OrePrefixes.spring.get(Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.JACKHAMMER_HV.ID,
                        1,
                        aMaterial,
                        Materials.StainlessSteel,
                        new long[] { 800_000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "PRP", "MPB", 'X', OrePrefixes.stickLong.get(aMaterial), 'M',
                        ItemList.Electric_Piston_HV.get(1L), 'S', OrePrefixes.screw.get(Materials.StainlessSteel), 'P',
                        OrePrefixes.plate.get(Materials.StainlessSteel), 'R',
                        OrePrefixes.spring.get(Materials.StainlessSteel), 'B', ItemList.Battery_RE_HV_Sodium.get(1L) });
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
                if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                    GTModHandler.addShapelessCraftingRecipe(
                        MetaGeneratedTool01.INSTANCE
                            .getToolWithStats(IDMetaTool01.FILE.ID, 1, aMaterial, aMaterial.mHandleMaterial, null),
                        new Object[] { aOreDictName, OrePrefixes.stick.get(aMaterial.mHandleMaterial) });

                    if ((!aMaterial.contains(SubTag.NO_SMASHING)) && (!aMaterial.contains(SubTag.BOUNCY))) {
                        if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                            GTModHandler.addCraftingRecipe(
                                MetaGeneratedTool01.INSTANCE.getToolWithStats(
                                    IDMetaTool01.FILE.ID,
                                    1,
                                    aMaterial,
                                    aMaterial.mHandleMaterial,
                                    null),
                                GTModHandler.RecipeBits.MIRRORED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                                    | GTModHandler.RecipeBits.BUFFERED,
                                new Object[] { "P", "P", "S", 'P', OrePrefixes.plate.get(aMaterial), 'S',
                                    OrePrefixes.stick.get(aMaterial.mHandleMaterial) });
                        }
                    }
                }
                if (GTOreDictUnificator.get(OrePrefixes.stick, aMaterial.mHandleMaterial, 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            GTOreDictUnificator.get(OrePrefixes.stick, aMaterial.mHandleMaterial, 1L),
                            GTOreDictUnificator.get(OrePrefixes.toolHeadFile, aMaterial, 1L))
                        .circuit(15)
                        .itemOutputs(
                            MetaGeneratedTool01.INSTANCE
                                .getToolWithStats(IDMetaTool01.FILE.ID, 1, aMaterial, aMaterial.mHandleMaterial, null))
                        .duration(10 * SECONDS)
                        .eut(calculateRecipeEU(aMaterial, (int) TierEU.RECIPE_MV))
                        .addTo(assemblerRecipes);
                }
            }
            case "toolHeadSaw" -> {
                if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {

                    GTModHandler.addShapelessCraftingRecipe(
                        MetaGeneratedTool01.INSTANCE
                            .getToolWithStats(IDMetaTool01.SAW.ID, 1, aMaterial, aMaterial.mHandleMaterial, null),
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
                if (GTOreDictUnificator.get(OrePrefixes.stick, aMaterial.mHandleMaterial, 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            GTOreDictUnificator.get(OrePrefixes.stick, aMaterial.mHandleMaterial, 1L),
                            GTOreDictUnificator.get(OrePrefixes.toolHeadSaw, aMaterial, 1L))
                        .circuit(7)
                        .itemOutputs(
                            MetaGeneratedTool01.INSTANCE
                                .getToolWithStats(IDMetaTool01.SAW.ID, 1, aMaterial, aMaterial.mHandleMaterial, null))
                        .duration(10 * SECONDS)
                        .eut(calculateRecipeEU(aMaterial, (int) TierEU.RECIPE_MV))
                        .addTo(assemblerRecipes);
                }
            }
            case "toolHeadWrench" -> {
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.WRENCH_LV.ID,
                        1,
                        aMaterial,
                        Materials.Steel,
                        new long[] { 100000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_LV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Steel), 'P', OrePrefixes.plate.get(Materials.Steel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.Steel), 'B', ItemList.Battery_RE_LV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.WRENCH_LV.ID,
                        1,
                        aMaterial,
                        Materials.Steel,
                        new long[] { 75000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_LV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Steel), 'P', OrePrefixes.plate.get(Materials.Steel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.Steel), 'B', ItemList.Battery_RE_LV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.WRENCH_LV.ID,
                        1,
                        aMaterial,
                        Materials.Steel,
                        new long[] { 50000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_LV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Steel), 'P', OrePrefixes.plate.get(Materials.Steel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.Steel), 'B', ItemList.Battery_RE_LV_Sodium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.WRENCH_MV.ID,
                        1,
                        aMaterial,
                        Materials.Aluminium,
                        new long[] { 400000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_MV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Aluminium), 'P', OrePrefixes.plate.get(Materials.Aluminium),
                        'G', OrePrefixes.gearGtSmall.get(Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.WRENCH_MV.ID,
                        1,
                        aMaterial,
                        Materials.Aluminium,
                        new long[] { 300000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_MV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Aluminium), 'P', OrePrefixes.plate.get(Materials.Aluminium),
                        'G', OrePrefixes.gearGtSmall.get(Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.WRENCH_MV.ID,
                        1,
                        aMaterial,
                        Materials.Aluminium,
                        new long[] { 200000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_MV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Aluminium), 'P', OrePrefixes.plate.get(Materials.Aluminium),
                        'G', OrePrefixes.gearGtSmall.get(Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Sodium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.WRENCH_HV.ID,
                        1,
                        aMaterial,
                        Materials.StainlessSteel,
                        new long[] { 1600000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_HV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.StainlessSteel), 'P',
                        OrePrefixes.plate.get(Materials.StainlessSteel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.WRENCH_HV.ID,
                        1,
                        aMaterial,
                        Materials.StainlessSteel,
                        new long[] { 1200000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_HV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.StainlessSteel), 'P',
                        OrePrefixes.plate.get(Materials.StainlessSteel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.WRENCH_HV.ID,
                        1,
                        aMaterial,
                        Materials.StainlessSteel,
                        new long[] { 800000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_HV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.StainlessSteel), 'P',
                        OrePrefixes.plate.get(Materials.StainlessSteel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Sodium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SCREWDRIVER_LV.ID,
                        1,
                        aMaterial,
                        Materials.Steel,
                        new long[] { 100000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PdX", "MGS", "GBP", 'X', OrePrefixes.stickLong.get(aMaterial), 'M',
                        ItemList.Electric_Motor_LV.get(1L), 'S', OrePrefixes.screw.get(Materials.Steel), 'P',
                        OrePrefixes.plate.get(Materials.Steel), 'G', OrePrefixes.gearGtSmall.get(Materials.Steel), 'B',
                        ItemList.Battery_RE_LV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SCREWDRIVER_LV.ID,
                        1,
                        aMaterial,
                        Materials.Steel,
                        new long[] { 75000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PdX", "MGS", "GBP", 'X', OrePrefixes.stickLong.get(aMaterial), 'M',
                        ItemList.Electric_Motor_LV.get(1L), 'S', OrePrefixes.screw.get(Materials.Steel), 'P',
                        OrePrefixes.plate.get(Materials.Steel), 'G', OrePrefixes.gearGtSmall.get(Materials.Steel), 'B',
                        ItemList.Battery_RE_LV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SCREWDRIVER_LV.ID,
                        1,
                        aMaterial,
                        Materials.Steel,
                        new long[] { 50000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PdX", "MGS", "GBP", 'X', OrePrefixes.stickLong.get(aMaterial), 'M',
                        ItemList.Electric_Motor_LV.get(1L), 'S', OrePrefixes.screw.get(Materials.Steel), 'P',
                        OrePrefixes.plate.get(Materials.Steel), 'G', OrePrefixes.gearGtSmall.get(Materials.Steel), 'B',
                        ItemList.Battery_RE_LV_Sodium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SCREWDRIVER_MV.ID,
                        1,
                        aMaterial,
                        Materials.Aluminium,
                        new long[] { 400000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PdX", "MGS", "GBP", 'X', OrePrefixes.stickLong.get(aMaterial), 'M',
                        ItemList.Electric_Motor_MV.get(1L), 'S', OrePrefixes.screw.get(Materials.Aluminium), 'P',
                        OrePrefixes.plate.get(Materials.Aluminium), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SCREWDRIVER_MV.ID,
                        1,
                        aMaterial,
                        Materials.Aluminium,
                        new long[] { 300000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PdX", "MGS", "GBP", 'X', OrePrefixes.stickLong.get(aMaterial), 'M',
                        ItemList.Electric_Motor_MV.get(1L), 'S', OrePrefixes.screw.get(Materials.Aluminium), 'P',
                        OrePrefixes.plate.get(Materials.Aluminium), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SCREWDRIVER_MV.ID,
                        1,
                        aMaterial,
                        Materials.Aluminium,
                        new long[] { 200000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PdX", "MGS", "GBP", 'X', OrePrefixes.stickLong.get(aMaterial), 'M',
                        ItemList.Electric_Motor_MV.get(1L), 'S', OrePrefixes.screw.get(Materials.Aluminium), 'P',
                        OrePrefixes.plate.get(Materials.Aluminium), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.Aluminium), 'B', ItemList.Battery_RE_MV_Sodium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SCREWDRIVER_HV.ID,
                        1,
                        aMaterial,
                        Materials.StainlessSteel,
                        new long[] { 1600000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PdX", "MGS", "GBP", 'X', OrePrefixes.stickLong.get(aMaterial), 'M',
                        ItemList.Electric_Motor_HV.get(1L), 'S', OrePrefixes.screw.get(Materials.StainlessSteel), 'P',
                        OrePrefixes.plate.get(Materials.StainlessSteel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SCREWDRIVER_HV.ID,
                        1,
                        aMaterial,
                        Materials.StainlessSteel,
                        new long[] { 1200000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PdX", "MGS", "GBP", 'X', OrePrefixes.stickLong.get(aMaterial), 'M',
                        ItemList.Electric_Motor_HV.get(1L), 'S', OrePrefixes.screw.get(Materials.StainlessSteel), 'P',
                        OrePrefixes.plate.get(Materials.StainlessSteel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SCREWDRIVER_HV.ID,
                        1,
                        aMaterial,
                        Materials.StainlessSteel,
                        new long[] { 800000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PdX", "MGS", "GBP", 'X', OrePrefixes.stickLong.get(aMaterial), 'M',
                        ItemList.Electric_Motor_HV.get(1L), 'S', OrePrefixes.screw.get(Materials.StainlessSteel), 'P',
                        OrePrefixes.plate.get(Materials.StainlessSteel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Sodium.get(1L) });
                // LV Electric Wirecutter
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.WIRECUTTER_LV.ID,
                        1,
                        aMaterial,
                        aMaterial,
                        new long[] { 100000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXS", "GMG", "PBP", 'X',
                        MetaGeneratedTool01.INSTANCE
                            .getToolWithStats(IDMetaTool01.WIRECUTTER.ID, 1, aMaterial, aMaterial, null),
                        'M', ItemList.Electric_Motor_LV.get(1L), 'S', OrePrefixes.wireFine.get(Materials.Electrum), 'P',
                        OrePrefixes.plate.get(aMaterial), 'G', OrePrefixes.gearGt.get(Materials.Steel), 'B',
                        ItemList.Battery_RE_LV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.WIRECUTTER_LV.ID,
                        1,
                        aMaterial,
                        aMaterial,
                        new long[] { 75000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXS", "GMG", "PBP", 'X',
                        MetaGeneratedTool01.INSTANCE
                            .getToolWithStats(IDMetaTool01.WIRECUTTER.ID, 1, aMaterial, aMaterial, null),
                        'M', ItemList.Electric_Motor_LV.get(1L), 'S', OrePrefixes.wireFine.get(Materials.Electrum), 'P',
                        OrePrefixes.plate.get(aMaterial), 'G', OrePrefixes.gearGt.get(Materials.Steel), 'B',
                        ItemList.Battery_RE_LV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.WIRECUTTER_LV.ID,
                        1,
                        aMaterial,
                        aMaterial,
                        new long[] { 50000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXS", "GMG", "PBP", 'X',
                        MetaGeneratedTool01.INSTANCE
                            .getToolWithStats(IDMetaTool01.WIRECUTTER.ID, 1, aMaterial, aMaterial, null),
                        'M', ItemList.Electric_Motor_LV.get(1L), 'S', OrePrefixes.wireFine.get(Materials.Electrum), 'P',
                        OrePrefixes.plate.get(aMaterial), 'G', OrePrefixes.gearGt.get(Materials.Steel), 'B',
                        ItemList.Battery_RE_LV_Sodium.get(1L) });
                // MV Electric Wirecutter
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.WIRECUTTER_MV.ID,
                        1,
                        aMaterial,
                        aMaterial,
                        new long[] { 400000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXS", "GMG", "PBP", 'X',
                        MetaGeneratedTool01.INSTANCE
                            .getToolWithStats(IDMetaTool01.WIRECUTTER.ID, 1, aMaterial, aMaterial, null),
                        'M', ItemList.Electric_Motor_MV.get(1L), 'S', OrePrefixes.wireFine.get(Materials.Electrum), 'P',
                        OrePrefixes.plate.get(aMaterial), 'G', OrePrefixes.gearGt.get(Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.WIRECUTTER_MV.ID,
                        1,
                        aMaterial,
                        aMaterial,
                        new long[] { 300000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXS", "GMG", "PBP", 'X',
                        MetaGeneratedTool01.INSTANCE
                            .getToolWithStats(IDMetaTool01.WIRECUTTER.ID, 1, aMaterial, aMaterial, null),
                        'M', ItemList.Electric_Motor_MV.get(1L), 'S', OrePrefixes.wireFine.get(Materials.Electrum), 'P',
                        OrePrefixes.plate.get(aMaterial), 'G', OrePrefixes.gearGt.get(Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.WIRECUTTER_MV.ID,
                        1,
                        aMaterial,
                        aMaterial,
                        new long[] { 200000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXS", "GMG", "PBP", 'X',
                        MetaGeneratedTool01.INSTANCE
                            .getToolWithStats(IDMetaTool01.WIRECUTTER.ID, 1, aMaterial, aMaterial, null),
                        'M', ItemList.Electric_Motor_MV.get(1L), 'S', OrePrefixes.wireFine.get(Materials.Electrum), 'P',
                        OrePrefixes.plate.get(aMaterial), 'G', OrePrefixes.gearGt.get(Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Sodium.get(1L) });
                // HV Electric Wirecutter
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.WIRECUTTER_HV.ID,
                        1,
                        aMaterial,
                        aMaterial,
                        new long[] { 1600000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXS", "GMG", "PBP", 'X',
                        MetaGeneratedTool01.INSTANCE
                            .getToolWithStats(IDMetaTool01.WIRECUTTER.ID, 1, aMaterial, aMaterial, null),
                        'M', ItemList.Electric_Motor_HV.get(1L), 'S', OrePrefixes.wireFine.get(Materials.Electrum), 'P',
                        OrePrefixes.plate.get(aMaterial), 'G', OrePrefixes.gearGt.get(Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.WIRECUTTER_HV.ID,
                        1,
                        aMaterial,
                        aMaterial,
                        new long[] { 1200000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXS", "GMG", "PBP", 'X',
                        MetaGeneratedTool01.INSTANCE
                            .getToolWithStats(IDMetaTool01.WIRECUTTER.ID, 1, aMaterial, aMaterial, null),
                        'M', ItemList.Electric_Motor_HV.get(1L), 'S', OrePrefixes.wireFine.get(Materials.Electrum), 'P',
                        OrePrefixes.plate.get(aMaterial), 'G', OrePrefixes.gearGt.get(Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.WIRECUTTER_HV.ID,
                        1,
                        aMaterial,
                        aMaterial,
                        new long[] { 800000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXS", "GMG", "PBP", 'X',
                        MetaGeneratedTool01.INSTANCE
                            .getToolWithStats(IDMetaTool01.WIRECUTTER.ID, 1, aMaterial, aMaterial, null),
                        'M', ItemList.Electric_Motor_HV.get(1L), 'S', OrePrefixes.wireFine.get(Materials.Electrum), 'P',
                        OrePrefixes.plate.get(aMaterial), 'G', OrePrefixes.gearGt.get(Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Sodium.get(1L) });

                // LV Electric File
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.FILE_LV.ID,
                        1,
                        aMaterial,
                        Materials.Steel,
                        new long[] { 100000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXL", "GMG", "PBP", 'X', ItemList.Component_Grinder_Diamond.get(1), 'M',
                        ItemList.Electric_Motor_LV.get(1L), 'S', OrePrefixes.screw.get(Materials.Steel), 'L',
                        OrePrefixes.stickLong.get(aMaterial), 'P', OrePrefixes.plate.get(aMaterial), 'G',
                        OrePrefixes.gearGt.get(Materials.Steel), 'B', ItemList.Battery_RE_LV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.FILE_LV.ID,
                        1,
                        aMaterial,
                        Materials.Steel,
                        new long[] { 75000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXL", "GMG", "PBP", 'X', ItemList.Component_Grinder_Diamond.get(1), 'M',
                        ItemList.Electric_Motor_LV.get(1L), 'S', OrePrefixes.screw.get(Materials.Steel), 'L',
                        OrePrefixes.stickLong.get(aMaterial), 'P', OrePrefixes.plate.get(aMaterial), 'G',
                        OrePrefixes.gearGt.get(Materials.Steel), 'B', ItemList.Battery_RE_LV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.FILE_LV.ID,
                        1,
                        aMaterial,
                        Materials.Steel,
                        new long[] { 50000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXL", "GMG", "PBP", 'X', ItemList.Component_Grinder_Diamond.get(1), 'M',
                        ItemList.Electric_Motor_LV.get(1L), 'S', OrePrefixes.screw.get(Materials.Steel), 'L',
                        OrePrefixes.stickLong.get(aMaterial), 'P', OrePrefixes.plate.get(aMaterial), 'G',
                        OrePrefixes.gearGt.get(Materials.Steel), 'B', ItemList.Battery_RE_LV_Sodium.get(1L) });
                // MV Electric File
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.FILE_MV.ID,
                        1,
                        aMaterial,
                        Materials.Aluminium,
                        new long[] { 400000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXL", "GMG", "PBP", 'X', ItemList.Component_Grinder_Diamond.get(1), 'M',
                        ItemList.Electric_Motor_MV.get(1L), 'S', OrePrefixes.screw.get(Materials.Aluminium), 'L',
                        OrePrefixes.stickLong.get(aMaterial), 'P', OrePrefixes.plate.get(aMaterial), 'G',
                        OrePrefixes.gearGt.get(Materials.Aluminium), 'B', ItemList.Battery_RE_MV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.FILE_MV.ID,
                        1,
                        aMaterial,
                        Materials.Aluminium,
                        new long[] { 300000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXL", "GMG", "PBP", 'X', ItemList.Component_Grinder_Diamond.get(1), 'M',
                        ItemList.Electric_Motor_MV.get(1L), 'S', OrePrefixes.screw.get(Materials.Aluminium), 'L',
                        OrePrefixes.stickLong.get(aMaterial), 'P', OrePrefixes.plate.get(aMaterial), 'G',
                        OrePrefixes.gearGt.get(Materials.Aluminium), 'B', ItemList.Battery_RE_MV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.FILE_MV.ID,
                        1,
                        aMaterial,
                        Materials.Aluminium,
                        new long[] { 200000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXL", "GMG", "PBP", 'X', ItemList.Component_Grinder_Diamond.get(1), 'M',
                        ItemList.Electric_Motor_MV.get(1L), 'S', OrePrefixes.screw.get(Materials.Aluminium), 'L',
                        OrePrefixes.stickLong.get(aMaterial), 'P', OrePrefixes.plate.get(aMaterial), 'G',
                        OrePrefixes.gearGt.get(Materials.Aluminium), 'B', ItemList.Battery_RE_MV_Sodium.get(1L) });
                // HV Electric File
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.FILE_HV.ID,
                        1,
                        aMaterial,
                        Materials.StainlessSteel,
                        new long[] { 1600000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXL", "GMG", "PBP", 'X', ItemList.Component_Grinder_Tungsten.get(1), 'M',
                        ItemList.Electric_Motor_HV.get(1L), 'S', OrePrefixes.screw.get(Materials.StainlessSteel), 'L',
                        OrePrefixes.stickLong.get(aMaterial), 'P', OrePrefixes.plate.get(aMaterial), 'G',
                        OrePrefixes.gearGt.get(Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.FILE_HV.ID,
                        1,
                        aMaterial,
                        Materials.StainlessSteel,
                        new long[] { 1200000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXL", "GMG", "PBP", 'X', ItemList.Component_Grinder_Tungsten.get(1), 'M',
                        ItemList.Electric_Motor_HV.get(1L), 'S', OrePrefixes.screw.get(Materials.StainlessSteel), 'L',
                        OrePrefixes.stickLong.get(aMaterial), 'P', OrePrefixes.plate.get(aMaterial), 'G',
                        OrePrefixes.gearGt.get(Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.FILE_HV.ID,
                        1,
                        aMaterial,
                        Materials.StainlessSteel,
                        new long[] { 800000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXL", "GMG", "PBP", 'X', ItemList.Component_Grinder_Tungsten.get(1), 'M',
                        ItemList.Electric_Motor_HV.get(1L), 'S', OrePrefixes.screw.get(Materials.StainlessSteel), 'L',
                        OrePrefixes.stickLong.get(aMaterial), 'P', OrePrefixes.plate.get(aMaterial), 'G',
                        OrePrefixes.gearGt.get(Materials.StainlessSteel), 'B', ItemList.Battery_RE_HV_Sodium.get(1L) });

                // Wrench Special Condition
                if (aSpecialRecipeReq2) GTModHandler.addCraftingRecipe(
                    GTOreDictUnificator.get(OrePrefixes.toolHeadWrench, aMaterial, 1L),
                    GTModHandler.RecipeBits.BITS_STD,
                    new Object[] { "hXW", "XRX", "WXd", 'X', OrePrefixes.plate.get(aMaterial), 'S',
                        OrePrefixes.plate.get(Materials.Steel), 'R', OrePrefixes.ring.get(Materials.Steel), 'W',
                        OrePrefixes.screw.get(Materials.Steel) });
            }
            case "toolHeadHammer", "toolHeadMallet" -> {
                if (GTOreDictUnificator.get(OrePrefixes.stick, aMaterial.mHandleMaterial, 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            GTOreDictUnificator.get(OrePrefixes.stick, aMaterial.mHandleMaterial, 1L),
                            GTOreDictUnificator.get(OrePrefixes.toolHeadHammer, aMaterial, 1L))
                        .circuit(14)
                        .itemOutputs(
                            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                                aProducesSoftMallet ? IDMetaTool01.SOFTMALLET.ID : IDMetaTool01.HARDHAMMER.ID,
                                1,
                                aMaterial,
                                aMaterial.mHandleMaterial,
                                null))
                        .duration(10 * SECONDS)
                        .eut(calculateRecipeEU(aMaterial, (int) TierEU.RECIPE_MV))
                        .addTo(assemblerRecipes);
                }
                if ((aMaterial != Materials.Stone) && (aMaterial != Materials.Flint)) {
                    GTModHandler.addShapelessCraftingRecipe(
                        MetaGeneratedTool01.INSTANCE.getToolWithStats(
                            aProducesSoftMallet ? IDMetaTool01.SOFTMALLET.ID : IDMetaTool01.HARDHAMMER.ID,
                            1,
                            aMaterial,
                            aMaterial.mHandleMaterial,
                            null),
                        GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                        new Object[] { aOreDictName, OrePrefixes.stick.get(aMaterial.mHandleMaterial) });
                    GTModHandler.addCraftingRecipe(
                        MetaGeneratedTool01.INSTANCE.getToolWithStats(
                            aProducesSoftMallet ? IDMetaTool01.SOFTMALLET.ID : IDMetaTool01.HARDHAMMER.ID,
                            1,
                            aMaterial,
                            aMaterial.mHandleMaterial,
                            null),
                        GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                        new Object[] { "XX ", "XXS", "XX ", 'X',
                            aMaterial == Materials.Wood ? OrePrefixes.plank.get(Materials.Wood)
                                : OrePrefixes.ingot.get(aMaterial),
                            'S', OrePrefixes.stick.get(aMaterial.mHandleMaterial) });
                    GTModHandler.addCraftingRecipe(
                        MetaGeneratedTool01.INSTANCE.getToolWithStats(
                            aProducesSoftMallet ? IDMetaTool01.SOFTMALLET.ID : IDMetaTool01.HARDHAMMER.ID,
                            1,
                            aMaterial,
                            aMaterial.mHandleMaterial,
                            null),
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
}
