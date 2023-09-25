package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMap.sAssemblerRecipes;
import static gregtech.api.recipe.RecipeMap.sExtruderRecipes;
import static gregtech.api.recipe.RecipeMap.sFluidSolidficationRecipes;
import static gregtech.api.recipe.RecipeMap.sPressRecipes;
import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;
import static gregtech.api.util.GT_Utility.calculateRecipeEU;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Proxy;
import gregtech.common.items.GT_MetaGenerated_Tool_01;

public class ProcessingToolHead implements gregtech.api.interfaces.IOreRecipeRegistrator { // TODO COMPARE WITH OLD TOOL
                                                                                           // HEAD??? generator

    public ProcessingToolHead() {
        OrePrefixes.toolHeadArrow.add(this);
        OrePrefixes.toolHeadAxe.add(this);
        OrePrefixes.toolHeadBuzzSaw.add(this);
        OrePrefixes.toolHeadChainsaw.add(this);
        OrePrefixes.toolHeadDrill.add(this);
        OrePrefixes.toolHeadFile.add(this);
        OrePrefixes.toolHeadHoe.add(this);
        OrePrefixes.toolHeadPickaxe.add(this);
        OrePrefixes.toolHeadPlow.add(this);
        OrePrefixes.toolHeadSaw.add(this);
        OrePrefixes.toolHeadSense.add(this);
        OrePrefixes.toolHeadShovel.add(this);
        OrePrefixes.toolHeadSword.add(this);
        OrePrefixes.toolHeadUniversalSpade.add(this);
        OrePrefixes.toolHeadWrench.add(this);
        OrePrefixes.toolHeadHammer.add(this);
        OrePrefixes.turbineBlade.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        boolean aSpecialRecipeReq1 = aMaterial.mUnificatable && (aMaterial.mMaterialInto == aMaterial)
            && !aMaterial.contains(SubTag.NO_SMASHING);
        boolean aSpecialRecipeReq2 = aMaterial.mUnificatable && (aMaterial.mMaterialInto == aMaterial)
            && !aMaterial.contains(SubTag.NO_WORKING);
        boolean aNoWorking = aMaterial.contains(SubTag.NO_WORKING);
        boolean aProducesSoftMallet = aMaterial.contains(SubTag.BOUNCY) || aMaterial.contains(SubTag.WOOD)
            || aMaterial.contains(SubTag.SOFT);
        switch (aPrefix) {
            case toolHeadArrow -> {
                if (aMaterial.mStandardMoltenFluid != null)
                    if (!(aMaterial == Materials.AnnealedCopper || aMaterial == Materials.WroughtIron)) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(ItemList.Shape_Mold_Arrow.get(0L))
                            .itemOutputs(GT_Utility.copyAmount(1L, aStack))
                            .fluidInputs(aMaterial.getMolten(36L))
                            .duration(16 * TICKS)
                            .eut(8)
                            .addTo(sFluidSolidficationRecipes);
                    }
                if (aSpecialRecipeReq2) {
                    GT_ModHandler.addCraftingRecipe(
                        GT_OreDictUnificator.get(OrePrefixes.toolHeadArrow, aMaterial, 1L),
                        GT_Proxy.tBits,
                        new Object[] { "Xf", 'X', OrePrefixes.gemChipped.get(aMaterial) });
                    GT_ModHandler.addCraftingRecipe(
                        GT_OreDictUnificator.get(OrePrefixes.toolHeadArrow, aMaterial, 3L),
                        GT_Proxy.tBits,
                        new Object[] { (aMaterial.contains(SubTag.WOOD) ? 115 : 'x') + "Pf", 'P',
                            OrePrefixes.plate.get(aMaterial) });
                }
            }
            case toolHeadAxe -> {
                GT_ModHandler.addShapelessCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE
                        .getToolWithStats(GT_MetaGenerated_Tool_01.AXE, 1, aMaterial, aMaterial.mHandleMaterial, null),
                    new Object[] { aOreDictName, OrePrefixes.stick.get(aMaterial.mHandleMaterial) });
                if (GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial.mHandleMaterial, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(
                            GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial.mHandleMaterial, 1L),
                            GT_OreDictUnificator.get(OrePrefixes.toolHeadAxe, aMaterial, 1L),
                            GT_Utility.getIntegratedCircuit(2))
                        .itemOutputs(
                            GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                                GT_MetaGenerated_Tool_01.AXE,
                                1,
                                aMaterial,
                                aMaterial.mHandleMaterial,
                                null))
                        .duration(10 * SECONDS)
                        .eut(TierEU.RECIPE_MV)
                        .addTo(sAssemblerRecipes);
                }
                if (aSpecialRecipeReq1) GT_ModHandler.addCraftingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.toolHeadAxe, aMaterial, 1L),
                    GT_Proxy.tBits,
                    new Object[] { "PIh", "P  ", "f  ", 'P', OrePrefixes.plate.get(aMaterial), 'I',
                        OrePrefixes.ingot.get(aMaterial) });
                if (!aNoWorking) GT_ModHandler.addCraftingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.toolHeadAxe, aMaterial, 1L),
                    GT_Proxy.tBits,
                    new Object[] { "GG ", "G  ", "f  ", 'G', OrePrefixes.gem.get(aMaterial) });
            }
            case toolHeadBuzzSaw -> {
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.BUZZSAW_LV,
                        1,
                        aMaterial,
                        Materials.Steel,
                        new long[] { 100000L, 32L, 1L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PBM", "dXG", "SGP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_LV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Steel), 'P', OrePrefixes.plate.get(Materials.Steel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.Steel), 'B', ItemList.Battery_RE_LV_Lithium.get(1L) });
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.BUZZSAW_LV,
                        1,
                        aMaterial,
                        Materials.Steel,
                        new long[] { 75000L, 32L, 1L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PBM", "dXG", "SGP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_LV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Steel), 'P', OrePrefixes.plate.get(Materials.Steel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.Steel), 'B', ItemList.Battery_RE_LV_Cadmium.get(1L) });
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.BUZZSAW_LV,
                        1,
                        aMaterial,
                        Materials.Steel,
                        new long[] { 50000L, 32L, 1L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PBM", "dXG", "SGP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_LV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Steel), 'P', OrePrefixes.plate.get(Materials.Steel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.Steel), 'B', ItemList.Battery_RE_LV_Sodium.get(1L) });
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.BUZZSAW_MV,
                        1,
                        aMaterial,
                        Materials.Aluminium,
                        new long[] { 400000L, 128L, 2L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PBM", "dXG", "SGP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_MV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Aluminium), 'P', OrePrefixes.plate.get(Materials.Aluminium),
                        'G', OrePrefixes.gearGtSmall.get(Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Lithium.get(1L) });
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.BUZZSAW_MV,
                        1,
                        aMaterial,
                        Materials.Aluminium,
                        new long[] { 300000L, 128L, 2L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PBM", "dXG", "SGP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_MV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Aluminium), 'P', OrePrefixes.plate.get(Materials.Aluminium),
                        'G', OrePrefixes.gearGtSmall.get(Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Cadmium.get(1L) });
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.BUZZSAW_MV,
                        1,
                        aMaterial,
                        Materials.Aluminium,
                        new long[] { 200000L, 128L, 2L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PBM", "dXG", "SGP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_MV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Aluminium), 'P', OrePrefixes.plate.get(Materials.Aluminium),
                        'G', OrePrefixes.gearGtSmall.get(Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Sodium.get(1L) });
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.BUZZSAW_HV,
                        1,
                        aMaterial,
                        Materials.StainlessSteel,
                        new long[] { 1600000L, 512L, 3L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PBM", "dXG", "SGP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_HV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.StainlessSteel), 'P',
                        OrePrefixes.plate.get(Materials.StainlessSteel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Lithium.get(1L) });
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.BUZZSAW_HV,
                        1,
                        aMaterial,
                        Materials.StainlessSteel,
                        new long[] { 1200000L, 512L, 3L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PBM", "dXG", "SGP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_HV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.StainlessSteel), 'P',
                        OrePrefixes.plate.get(Materials.StainlessSteel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Cadmium.get(1L) });
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.BUZZSAW_HV,
                        1,
                        aMaterial,
                        Materials.StainlessSteel,
                        new long[] { 800000L, 512L, 3L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PBM", "dXG", "SGP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_HV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.StainlessSteel), 'P',
                        OrePrefixes.plate.get(Materials.StainlessSteel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Sodium.get(1L) });
                if (aSpecialRecipeReq2) GT_ModHandler.addCraftingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.toolHeadBuzzSaw, aMaterial, 1L),
                    GT_Proxy.tBits,
                    new Object[] { "wXh", "X X", "fXx", 'X', OrePrefixes.plate.get(aMaterial) });
            }
            case toolHeadChainsaw -> {
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.CHAINSAW_LV,
                        1,
                        aMaterial,
                        Materials.Steel,
                        new long[] { 100000L, 32L, 1L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_LV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Steel), 'P', OrePrefixes.plate.get(Materials.Steel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.Steel), 'B', ItemList.Battery_RE_LV_Lithium.get(1L) });
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.CHAINSAW_LV,
                        1,
                        aMaterial,
                        Materials.Steel,
                        new long[] { 75000L, 32L, 1L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_LV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Steel), 'P', OrePrefixes.plate.get(Materials.Steel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.Steel), 'B', ItemList.Battery_RE_LV_Cadmium.get(1L) });
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.CHAINSAW_LV,
                        1,
                        aMaterial,
                        Materials.Steel,
                        new long[] { 50000L, 32L, 1L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_LV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Steel), 'P', OrePrefixes.plate.get(Materials.Steel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.Steel), 'B', ItemList.Battery_RE_LV_Sodium.get(1L) });
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.CHAINSAW_MV,
                        1,
                        aMaterial,
                        Materials.Aluminium,
                        new long[] { 400000L, 128L, 2L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_MV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Aluminium), 'P', OrePrefixes.plate.get(Materials.Aluminium),
                        'G', OrePrefixes.gearGtSmall.get(Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Lithium.get(1L) });
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.CHAINSAW_MV,
                        1,
                        aMaterial,
                        Materials.Aluminium,
                        new long[] { 300000L, 128L, 2L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_MV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Aluminium), 'P', OrePrefixes.plate.get(Materials.Aluminium),
                        'G', OrePrefixes.gearGtSmall.get(Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Cadmium.get(1L) });
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.CHAINSAW_MV,
                        1,
                        aMaterial,
                        Materials.Aluminium,
                        new long[] { 200000L, 128L, 2L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_MV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Aluminium), 'P', OrePrefixes.plate.get(Materials.Aluminium),
                        'G', OrePrefixes.gearGtSmall.get(Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Sodium.get(1L) });
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.CHAINSAW_HV,
                        1,
                        aMaterial,
                        Materials.StainlessSteel,
                        new long[] { 1600000L, 512L, 3L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_HV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.StainlessSteel), 'P',
                        OrePrefixes.plate.get(Materials.StainlessSteel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Lithium.get(1L) });
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.CHAINSAW_HV,
                        1,
                        aMaterial,
                        Materials.StainlessSteel,
                        new long[] { 1200000L, 512L, 3L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_HV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.StainlessSteel), 'P',
                        OrePrefixes.plate.get(Materials.StainlessSteel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Cadmium.get(1L) });
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.CHAINSAW_HV,
                        1,
                        aMaterial,
                        Materials.StainlessSteel,
                        new long[] { 800000L, 512L, 3L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_HV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.StainlessSteel), 'P',
                        OrePrefixes.plate.get(Materials.StainlessSteel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Sodium.get(1L) });
                if (aSpecialRecipeReq2) GT_ModHandler.addCraftingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.toolHeadChainsaw, aMaterial, 1L),
                    GT_Proxy.tBits,
                    new Object[] { "SRS", "XhX", "SRS", 'X', OrePrefixes.plate.get(aMaterial), 'S',
                        OrePrefixes.plate.get(Materials.Steel), 'R', OrePrefixes.ring.get(Materials.Steel) });
            }
            case toolHeadDrill -> {
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.DRILL_LV,
                        1,
                        aMaterial,
                        Materials.Steel,
                        new long[] { 100_000L, 32L, 1L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_LV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Steel), 'P', OrePrefixes.plate.get(Materials.Steel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.Steel), 'B', ItemList.Battery_RE_LV_Lithium.get(1L) });
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.DRILL_LV,
                        1,
                        aMaterial,
                        Materials.Steel,
                        new long[] { 75_000L, 32L, 1L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_LV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Steel), 'P', OrePrefixes.plate.get(Materials.Steel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.Steel), 'B', ItemList.Battery_RE_LV_Cadmium.get(1L) });
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.DRILL_LV,
                        1,
                        aMaterial,
                        Materials.Steel,
                        new long[] { 50_000L, 32L, 1L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_LV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Steel), 'P', OrePrefixes.plate.get(Materials.Steel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.Steel), 'B', ItemList.Battery_RE_LV_Sodium.get(1L) });
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.DRILL_MV,
                        1,
                        aMaterial,
                        Materials.Aluminium,
                        new long[] { 400_000L, 128L, 2L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_MV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Aluminium), 'P', OrePrefixes.plate.get(Materials.Aluminium),
                        'G', OrePrefixes.gearGtSmall.get(Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Lithium.get(1L) });
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.DRILL_MV,
                        1,
                        aMaterial,
                        Materials.Aluminium,
                        new long[] { 300_000L, 128L, 2L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_MV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Aluminium), 'P', OrePrefixes.plate.get(Materials.Aluminium),
                        'G', OrePrefixes.gearGtSmall.get(Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Cadmium.get(1L) });
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.DRILL_MV,
                        1,
                        aMaterial,
                        Materials.Aluminium,
                        new long[] { 200_000L, 128L, 2L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_MV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Aluminium), 'P', OrePrefixes.plate.get(Materials.Aluminium),
                        'G', OrePrefixes.gearGtSmall.get(Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Sodium.get(1L) });
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.DRILL_HV,
                        1,
                        aMaterial,
                        Materials.StainlessSteel,
                        new long[] { 1_600_000L, 512L, 3L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_HV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.StainlessSteel), 'P',
                        OrePrefixes.plate.get(Materials.StainlessSteel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Lithium.get(1L) });
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.DRILL_HV,
                        1,
                        aMaterial,
                        Materials.StainlessSteel,
                        new long[] { 1_200_000L, 512L, 3L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_HV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.StainlessSteel), 'P',
                        OrePrefixes.plate.get(Materials.StainlessSteel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Cadmium.get(1L) });
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.DRILL_HV,
                        1,
                        aMaterial,
                        Materials.StainlessSteel,
                        new long[] { 800_000L, 512L, 3L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_HV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.StainlessSteel), 'P',
                        OrePrefixes.plate.get(Materials.StainlessSteel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Sodium.get(1L) });
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.JACKHAMMER,
                        1,
                        aMaterial,
                        Materials.StainlessSteel,
                        new long[] { 1_600_000L, 512L, 3L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "PRP", "MPB", 'X', OrePrefixes.stickLong.get(aMaterial), 'M',
                        ItemList.Electric_Piston_HV.get(1L), 'S', OrePrefixes.screw.get(Materials.StainlessSteel), 'P',
                        OrePrefixes.plate.get(Materials.StainlessSteel), 'R',
                        OrePrefixes.spring.get(Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Lithium.get(1L) });
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.JACKHAMMER,
                        1,
                        aMaterial,
                        Materials.StainlessSteel,
                        new long[] { 1_200_000L, 512L, 3L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "PRP", "MPB", 'X', OrePrefixes.stickLong.get(aMaterial), 'M',
                        ItemList.Electric_Piston_HV.get(1L), 'S', OrePrefixes.screw.get(Materials.StainlessSteel), 'P',
                        OrePrefixes.plate.get(Materials.StainlessSteel), 'R',
                        OrePrefixes.spring.get(Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Cadmium.get(1L) });
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.JACKHAMMER,
                        1,
                        aMaterial,
                        Materials.StainlessSteel,
                        new long[] { 800_000L, 512L, 3L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "PRP", "MPB", 'X', OrePrefixes.stickLong.get(aMaterial), 'M',
                        ItemList.Electric_Piston_HV.get(1L), 'S', OrePrefixes.screw.get(Materials.StainlessSteel), 'P',
                        OrePrefixes.plate.get(Materials.StainlessSteel), 'R',
                        OrePrefixes.spring.get(Materials.StainlessSteel), 'B', ItemList.Battery_RE_HV_Sodium.get(1L) });
                if (aSpecialRecipeReq2) {
                    GT_ModHandler.addCraftingRecipe(
                        GT_OreDictUnificator.get(OrePrefixes.toolHeadDrill, aMaterial, 1L),
                        GT_Proxy.tBits,
                        new Object[] { "XSX", "XSX", "ShS", 'X', OrePrefixes.plate.get(aMaterial), 'S',
                            OrePrefixes.plate.get(Materials.Steel) });
                    if (aMaterial.mStandardMoltenFluid != null) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(ItemList.Shape_Mold_ToolHeadDrill.get(0))
                            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.toolHeadDrill, aMaterial, 1L))
                            .fluidInputs(aMaterial.getMolten(144 * 4))
                            .duration(5 * SECONDS)
                            .eut(calculateRecipeEU(aMaterial, (int) TierEU.RECIPE_MV))
                            .addTo(sFluidSolidficationRecipes);
                    }
                    if (GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L) != null) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(
                                GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial, 4L),
                                ItemList.Shape_Extruder_ToolHeadDrill.get(0))
                            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.toolHeadDrill, aMaterial, 1L))
                            .duration(5 * SECONDS)
                            .eut(calculateRecipeEU(aMaterial, (int) TierEU.RECIPE_MV))
                            .addTo(sExtruderRecipes);
                    }
                }
            }
            case toolHeadFile -> {
                if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                    GT_ModHandler.addShapelessCraftingRecipe(
                        GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                            GT_MetaGenerated_Tool_01.FILE,
                            1,
                            aMaterial,
                            aMaterial.mHandleMaterial,
                            null),
                        new Object[] { aOreDictName, OrePrefixes.stick.get(aMaterial.mHandleMaterial) });

                    if ((!aMaterial.contains(SubTag.NO_SMASHING)) && (!aMaterial.contains(SubTag.BOUNCY))) {
                        if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                            GT_ModHandler.addCraftingRecipe(
                                GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                                    GT_MetaGenerated_Tool_01.FILE,
                                    1,
                                    aMaterial,
                                    aMaterial.mHandleMaterial,
                                    null),
                                GT_ModHandler.RecipeBits.MIRRORED | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                                    | GT_ModHandler.RecipeBits.BUFFERED,
                                new Object[] { "P", "P", "S", 'P', OrePrefixes.plate.get(aMaterial), 'S',
                                    OrePrefixes.stick.get(aMaterial.mHandleMaterial) });
                        }
                    }
                }
                if (GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial.mHandleMaterial, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(
                            GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial.mHandleMaterial, 1L),
                            GT_OreDictUnificator.get(OrePrefixes.toolHeadFile, aMaterial, 1L),
                            GT_Utility.getIntegratedCircuit(15))
                        .itemOutputs(
                            GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                                GT_MetaGenerated_Tool_01.FILE,
                                1,
                                aMaterial,
                                aMaterial.mHandleMaterial,
                                null))
                        .duration(10 * SECONDS)
                        .eut(calculateRecipeEU(aMaterial, (int) TierEU.RECIPE_MV))
                        .addTo(sAssemblerRecipes);
                }
            }
            case toolHeadHoe -> {
                if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                    GT_ModHandler.addShapelessCraftingRecipe(
                        GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                            GT_MetaGenerated_Tool_01.HOE,
                            1,
                            aMaterial,
                            aMaterial.mHandleMaterial,
                            null),
                        new Object[] { aOreDictName, OrePrefixes.stick.get(aMaterial.mHandleMaterial) });
                }
                if (GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial.mHandleMaterial, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(
                            GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial.mHandleMaterial, 1L),
                            GT_OreDictUnificator.get(OrePrefixes.toolHeadHoe, aMaterial, 1L),
                            GT_Utility.getIntegratedCircuit(16))
                        .itemOutputs(
                            GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                                GT_MetaGenerated_Tool_01.HOE,
                                1,
                                aMaterial,
                                aMaterial.mHandleMaterial,
                                null))
                        .duration(10 * SECONDS)
                        .eut(calculateRecipeEU(aMaterial, (int) TierEU.RECIPE_MV))
                        .addTo(sAssemblerRecipes);
                }
                if (aSpecialRecipeReq1) GT_ModHandler.addCraftingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.toolHeadHoe, aMaterial, 1L),
                    GT_Proxy.tBits,
                    new Object[] { "PIh", "f  ", 'P', OrePrefixes.plate.get(aMaterial), 'I',
                        OrePrefixes.ingot.get(aMaterial) });
                if (!aNoWorking) GT_ModHandler.addCraftingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.toolHeadHoe, aMaterial, 1L),
                    GT_Proxy.tBits,
                    new Object[] { "GG ", "f  ", "   ", 'G', OrePrefixes.gem.get(aMaterial) });
            }
            case toolHeadPickaxe -> {
                if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {

                    GT_ModHandler.addShapelessCraftingRecipe(
                        GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                            GT_MetaGenerated_Tool_01.PICKAXE,
                            1,
                            aMaterial,
                            aMaterial.mHandleMaterial,
                            null),
                        new Object[] { aOreDictName, OrePrefixes.stick.get(aMaterial.mHandleMaterial) });

                    if (aSpecialRecipeReq1) GT_ModHandler.addCraftingRecipe(
                        GT_OreDictUnificator.get(OrePrefixes.toolHeadPickaxe, aMaterial, 1L),
                        GT_Proxy.tBits,
                        new Object[] { "PII", "f h", 'P', OrePrefixes.plate.get(aMaterial), 'I',
                            OrePrefixes.ingot.get(aMaterial) });

                    if (!aNoWorking) GT_ModHandler.addCraftingRecipe(
                        GT_OreDictUnificator.get(OrePrefixes.toolHeadPickaxe, aMaterial, 1L),
                        GT_Proxy.tBits,
                        new Object[] { "GGG", "f  ", 'G', OrePrefixes.gem.get(aMaterial) });
                }
                if (GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial.mHandleMaterial, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(
                            GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial.mHandleMaterial, 1L),
                            GT_OreDictUnificator.get(OrePrefixes.toolHeadPickaxe, aMaterial, 1L),
                            GT_Utility.getIntegratedCircuit(5))
                        .itemOutputs(
                            GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                                GT_MetaGenerated_Tool_01.PICKAXE,
                                1,
                                aMaterial,
                                aMaterial.mHandleMaterial,
                                null))
                        .duration(10 * SECONDS)
                        .eut(calculateRecipeEU(aMaterial, (int) TierEU.RECIPE_MV))
                        .addTo(sAssemblerRecipes);
                }
            }
            case toolHeadPlow -> {
                if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {

                    GT_ModHandler.addShapelessCraftingRecipe(
                        GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                            GT_MetaGenerated_Tool_01.PLOW,
                            1,
                            aMaterial,
                            aMaterial.mHandleMaterial,
                            null),
                        new Object[] { aOreDictName, OrePrefixes.stick.get(aMaterial.mHandleMaterial) });

                    if (aSpecialRecipeReq1) GT_ModHandler.addCraftingRecipe(
                        GT_OreDictUnificator.get(OrePrefixes.toolHeadPlow, aMaterial, 1L),
                        GT_Proxy.tBits,
                        new Object[] { "PP", "PP", "hf", 'P', OrePrefixes.plate.get(aMaterial) });

                    if (!aNoWorking) GT_ModHandler.addCraftingRecipe(
                        GT_OreDictUnificator.get(OrePrefixes.toolHeadPlow, aMaterial, 1L),
                        GT_Proxy.tBits,
                        new Object[] { "GG", "GG", " f", 'G', OrePrefixes.gem.get(aMaterial) });
                }
                if (GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial.mHandleMaterial, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(
                            GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial.mHandleMaterial, 1L),
                            GT_OreDictUnificator.get(OrePrefixes.toolHeadPlow, aMaterial, 1L),
                            GT_Utility.getIntegratedCircuit(6))
                        .itemOutputs(
                            GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                                GT_MetaGenerated_Tool_01.PLOW,
                                1,
                                aMaterial,
                                aMaterial.mHandleMaterial,
                                null))
                        .duration(10 * SECONDS)
                        .eut(calculateRecipeEU(aMaterial, (int) TierEU.RECIPE_MV))
                        .addTo(sAssemblerRecipes);
                }
            }
            case toolHeadSaw -> {
                if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {

                    GT_ModHandler.addShapelessCraftingRecipe(
                        GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                            GT_MetaGenerated_Tool_01.SAW,
                            1,
                            aMaterial,
                            aMaterial.mHandleMaterial,
                            null),
                        new Object[] { aOreDictName, OrePrefixes.stick.get(aMaterial.mHandleMaterial) });

                    if (aSpecialRecipeReq1) GT_ModHandler.addCraftingRecipe(
                        GT_OreDictUnificator.get(OrePrefixes.toolHeadSaw, aMaterial, 1L),
                        GT_Proxy.tBits,
                        new Object[] { "PP ", "fh ", 'P', OrePrefixes.plate.get(aMaterial), 'I',
                            OrePrefixes.ingot.get(aMaterial) });

                    if (!aNoWorking) GT_ModHandler.addCraftingRecipe(
                        GT_OreDictUnificator.get(OrePrefixes.toolHeadSaw, aMaterial, 1L),
                        GT_Proxy.tBits,
                        new Object[] { "GGf", 'G', OrePrefixes.gem.get(aMaterial) });
                }
                if (GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial.mHandleMaterial, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(
                            GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial.mHandleMaterial, 1L),
                            GT_OreDictUnificator.get(OrePrefixes.toolHeadSaw, aMaterial, 1L),
                            GT_Utility.getIntegratedCircuit(7))
                        .itemOutputs(
                            GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                                GT_MetaGenerated_Tool_01.SAW,
                                1,
                                aMaterial,
                                aMaterial.mHandleMaterial,
                                null))
                        .duration(10 * SECONDS)
                        .eut(calculateRecipeEU(aMaterial, (int) TierEU.RECIPE_MV))
                        .addTo(sAssemblerRecipes);
                }
            }
            case toolHeadSense -> {
                if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {

                    GT_ModHandler.addShapelessCraftingRecipe(
                        GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                            GT_MetaGenerated_Tool_01.SENSE,
                            1,
                            aMaterial,
                            aMaterial.mHandleMaterial,
                            null),
                        new Object[] { aOreDictName, OrePrefixes.stick.get(aMaterial.mHandleMaterial) });

                    if (aSpecialRecipeReq1) GT_ModHandler.addCraftingRecipe(
                        GT_OreDictUnificator.get(OrePrefixes.toolHeadSense, aMaterial, 1L),
                        GT_Proxy.tBits,
                        new Object[] { "PPI", "hf ", 'P', OrePrefixes.plate.get(aMaterial), 'I',
                            OrePrefixes.ingot.get(aMaterial) });

                    if (!aNoWorking) GT_ModHandler.addCraftingRecipe(
                        GT_OreDictUnificator.get(OrePrefixes.toolHeadSense, aMaterial, 1L),
                        GT_Proxy.tBits,
                        new Object[] { "GGG", " f ", "   ", 'G', OrePrefixes.gem.get(aMaterial) });
                }
                if (GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial.mHandleMaterial, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(
                            GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial.mHandleMaterial, 1L),
                            GT_OreDictUnificator.get(OrePrefixes.toolHeadSense, aMaterial, 1L),
                            GT_Utility.getIntegratedCircuit(8))
                        .itemOutputs(
                            GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                                GT_MetaGenerated_Tool_01.SENSE,
                                1,
                                aMaterial,
                                aMaterial.mHandleMaterial,
                                null))
                        .duration(10 * SECONDS)
                        .eut(calculateRecipeEU(aMaterial, (int) TierEU.RECIPE_MV))
                        .addTo(sAssemblerRecipes);
                }
            }
            case toolHeadShovel -> {
                GT_ModHandler.addShapelessCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.SHOVEL,
                        1,
                        aMaterial,
                        aMaterial.mHandleMaterial,
                        null),
                    new Object[] { aOreDictName, OrePrefixes.stick.get(aMaterial.mHandleMaterial) });
                if (GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial.mHandleMaterial, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(
                            GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial.mHandleMaterial, 1L),
                            GT_OreDictUnificator.get(OrePrefixes.toolHeadShovel, aMaterial, 1L),
                            GT_Utility.getIntegratedCircuit(9))
                        .itemOutputs(
                            GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                                GT_MetaGenerated_Tool_01.SHOVEL,
                                1,
                                aMaterial,
                                aMaterial.mHandleMaterial,
                                null))
                        .duration(10 * SECONDS)
                        .eut(calculateRecipeEU(aMaterial, (int) TierEU.RECIPE_MV))
                        .addTo(sAssemblerRecipes);
                }
                if (aSpecialRecipeReq1) GT_ModHandler.addCraftingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.toolHeadShovel, aMaterial, 1L),
                    GT_Proxy.tBits,
                    new Object[] { "fPh", 'P', OrePrefixes.plate.get(aMaterial), 'I',
                        OrePrefixes.ingot.get(aMaterial) });
                if (!aNoWorking) GT_ModHandler.addCraftingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.toolHeadShovel, aMaterial, 1L),
                    GT_Proxy.tBits,
                    new Object[] { "fG", 'G', OrePrefixes.gem.get(aMaterial) });
            }
            case toolHeadSword -> {
                if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {

                    GT_ModHandler.addShapelessCraftingRecipe(
                        GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                            GT_MetaGenerated_Tool_01.SWORD,
                            1,
                            aMaterial,
                            aMaterial.mHandleMaterial,
                            null),
                        new Object[] { aOreDictName, OrePrefixes.stick.get(aMaterial.mHandleMaterial) });

                    if (aSpecialRecipeReq1) GT_ModHandler.addCraftingRecipe(
                        GT_OreDictUnificator.get(OrePrefixes.toolHeadSword, aMaterial, 1L),
                        GT_Proxy.tBits,
                        new Object[] { " P ", "fPh", 'P', OrePrefixes.plate.get(aMaterial), 'I',
                            OrePrefixes.ingot.get(aMaterial) });

                    if (!aNoWorking) GT_ModHandler.addCraftingRecipe(
                        GT_OreDictUnificator.get(OrePrefixes.toolHeadSword, aMaterial, 1L),
                        GT_Proxy.tBits,
                        new Object[] { " G", "fG", 'G', OrePrefixes.gem.get(aMaterial) });
                }
                if (GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial.mHandleMaterial, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(
                            GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial.mHandleMaterial, 1L),
                            GT_OreDictUnificator.get(OrePrefixes.toolHeadSword, aMaterial, 1L),
                            GT_Utility.getIntegratedCircuit(10))
                        .itemOutputs(
                            GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                                GT_MetaGenerated_Tool_01.SWORD,
                                1,
                                aMaterial,
                                aMaterial.mHandleMaterial,
                                null))
                        .duration(10 * SECONDS)
                        .eut(calculateRecipeEU(aMaterial, (int) TierEU.RECIPE_MV))
                        .addTo(sAssemblerRecipes);
                }
            }
            case toolHeadUniversalSpade -> {
                GT_ModHandler.addShapelessCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE
                        .getToolWithStats(GT_MetaGenerated_Tool_01.UNIVERSALSPADE, 1, aMaterial, aMaterial, null),
                    new Object[] { aOreDictName, OrePrefixes.stick.get(aMaterial), OrePrefixes.screw.get(aMaterial),
                        ToolDictNames.craftingToolScrewdriver });
                if (GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial.mHandleMaterial, 1L) != null
                    && GT_OreDictUnificator.get(OrePrefixes.screw, aMaterial, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(
                            GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial.mHandleMaterial, 1L),
                            GT_OreDictUnificator.get(OrePrefixes.screw, aMaterial, 1L),
                            GT_OreDictUnificator.get(OrePrefixes.toolHeadUniversalSpade, aMaterial, 1L),
                            GT_Utility.getIntegratedCircuit(11))
                        .itemOutputs(
                            GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                                GT_MetaGenerated_Tool_01.UNIVERSALSPADE,
                                1,
                                aMaterial,
                                aMaterial.mHandleMaterial,
                                null))
                        .duration(10 * SECONDS)
                        .eut(calculateRecipeEU(aMaterial, (int) TierEU.RECIPE_MV))
                        .addTo(sAssemblerRecipes);
                }
                if (aSpecialRecipeReq2) GT_ModHandler.addCraftingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.toolHeadUniversalSpade, aMaterial, 1L),
                    GT_Proxy.tBits,
                    new Object[] { "fX", 'X', OrePrefixes.toolHeadShovel.get(aMaterial) });
            }
            case toolHeadWrench -> {
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.WRENCH_LV,
                        1,
                        aMaterial,
                        Materials.Steel,
                        new long[] { 100000L, 32L, 1L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_LV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Steel), 'P', OrePrefixes.plate.get(Materials.Steel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.Steel), 'B', ItemList.Battery_RE_LV_Lithium.get(1L) });
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.WRENCH_LV,
                        1,
                        aMaterial,
                        Materials.Steel,
                        new long[] { 75000L, 32L, 1L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_LV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Steel), 'P', OrePrefixes.plate.get(Materials.Steel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.Steel), 'B', ItemList.Battery_RE_LV_Cadmium.get(1L) });
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.WRENCH_LV,
                        1,
                        aMaterial,
                        Materials.Steel,
                        new long[] { 50000L, 32L, 1L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_LV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Steel), 'P', OrePrefixes.plate.get(Materials.Steel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.Steel), 'B', ItemList.Battery_RE_LV_Sodium.get(1L) });
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.WRENCH_MV,
                        1,
                        aMaterial,
                        Materials.Aluminium,
                        new long[] { 400000L, 128L, 2L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_MV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Aluminium), 'P', OrePrefixes.plate.get(Materials.Aluminium),
                        'G', OrePrefixes.gearGtSmall.get(Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Lithium.get(1L) });
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.WRENCH_MV,
                        1,
                        aMaterial,
                        Materials.Aluminium,
                        new long[] { 300000L, 128L, 2L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_MV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Aluminium), 'P', OrePrefixes.plate.get(Materials.Aluminium),
                        'G', OrePrefixes.gearGtSmall.get(Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Cadmium.get(1L) });
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.WRENCH_MV,
                        1,
                        aMaterial,
                        Materials.Aluminium,
                        new long[] { 200000L, 128L, 2L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_MV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.Aluminium), 'P', OrePrefixes.plate.get(Materials.Aluminium),
                        'G', OrePrefixes.gearGtSmall.get(Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Sodium.get(1L) });
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.WRENCH_HV,
                        1,
                        aMaterial,
                        Materials.StainlessSteel,
                        new long[] { 1600000L, 512L, 3L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_HV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.StainlessSteel), 'P',
                        OrePrefixes.plate.get(Materials.StainlessSteel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Lithium.get(1L) });
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.WRENCH_HV,
                        1,
                        aMaterial,
                        Materials.StainlessSteel,
                        new long[] { 1200000L, 512L, 3L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_HV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.StainlessSteel), 'P',
                        OrePrefixes.plate.get(Materials.StainlessSteel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Cadmium.get(1L) });
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.WRENCH_HV,
                        1,
                        aMaterial,
                        Materials.StainlessSteel,
                        new long[] { 800000L, 512L, 3L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', aOreDictName, 'M', ItemList.Electric_Motor_HV.get(1L), 'S',
                        OrePrefixes.screw.get(Materials.StainlessSteel), 'P',
                        OrePrefixes.plate.get(Materials.StainlessSteel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Sodium.get(1L) });
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.SCREWDRIVER_LV,
                        1,
                        aMaterial,
                        Materials.Steel,
                        new long[] { 100000L, 32L, 1L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PdX", "MGS", "GBP", 'X', OrePrefixes.stickLong.get(aMaterial), 'M',
                        ItemList.Electric_Motor_LV.get(1L), 'S', OrePrefixes.screw.get(Materials.Steel), 'P',
                        OrePrefixes.plate.get(Materials.Steel), 'G', OrePrefixes.gearGtSmall.get(Materials.Steel), 'B',
                        ItemList.Battery_RE_LV_Lithium.get(1L) });
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.SCREWDRIVER_LV,
                        1,
                        aMaterial,
                        Materials.Steel,
                        new long[] { 75000L, 32L, 1L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PdX", "MGS", "GBP", 'X', OrePrefixes.stickLong.get(aMaterial), 'M',
                        ItemList.Electric_Motor_LV.get(1L), 'S', OrePrefixes.screw.get(Materials.Steel), 'P',
                        OrePrefixes.plate.get(Materials.Steel), 'G', OrePrefixes.gearGtSmall.get(Materials.Steel), 'B',
                        ItemList.Battery_RE_LV_Cadmium.get(1L) });
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.SCREWDRIVER_LV,
                        1,
                        aMaterial,
                        Materials.Steel,
                        new long[] { 50000L, 32L, 1L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PdX", "MGS", "GBP", 'X', OrePrefixes.stickLong.get(aMaterial), 'M',
                        ItemList.Electric_Motor_LV.get(1L), 'S', OrePrefixes.screw.get(Materials.Steel), 'P',
                        OrePrefixes.plate.get(Materials.Steel), 'G', OrePrefixes.gearGtSmall.get(Materials.Steel), 'B',
                        ItemList.Battery_RE_LV_Sodium.get(1L) });
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.SCREWDRIVER_MV,
                        1,
                        aMaterial,
                        Materials.Aluminium,
                        new long[] { 400000L, 128L, 2L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PdX", "MGS", "GBP", 'X', OrePrefixes.stickLong.get(aMaterial), 'M',
                        ItemList.Electric_Motor_MV.get(1L), 'S', OrePrefixes.screw.get(Materials.Aluminium), 'P',
                        OrePrefixes.plate.get(Materials.Aluminium), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Lithium.get(1L) });
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.SCREWDRIVER_MV,
                        1,
                        aMaterial,
                        Materials.Aluminium,
                        new long[] { 300000L, 128L, 2L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PdX", "MGS", "GBP", 'X', OrePrefixes.stickLong.get(aMaterial), 'M',
                        ItemList.Electric_Motor_MV.get(1L), 'S', OrePrefixes.screw.get(Materials.Aluminium), 'P',
                        OrePrefixes.plate.get(Materials.Aluminium), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Cadmium.get(1L) });
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.SCREWDRIVER_MV,
                        1,
                        aMaterial,
                        Materials.Aluminium,
                        new long[] { 200000L, 128L, 2L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PdX", "MGS", "GBP", 'X', OrePrefixes.stickLong.get(aMaterial), 'M',
                        ItemList.Electric_Motor_MV.get(1L), 'S', OrePrefixes.screw.get(Materials.Aluminium), 'P',
                        OrePrefixes.plate.get(Materials.Aluminium), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.Aluminium), 'B', ItemList.Battery_RE_MV_Sodium.get(1L) });
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.SCREWDRIVER_HV,
                        1,
                        aMaterial,
                        Materials.StainlessSteel,
                        new long[] { 1600000L, 512L, 3L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PdX", "MGS", "GBP", 'X', OrePrefixes.stickLong.get(aMaterial), 'M',
                        ItemList.Electric_Motor_HV.get(1L), 'S', OrePrefixes.screw.get(Materials.StainlessSteel), 'P',
                        OrePrefixes.plate.get(Materials.StainlessSteel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Lithium.get(1L) });
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.SCREWDRIVER_HV,
                        1,
                        aMaterial,
                        Materials.StainlessSteel,
                        new long[] { 1200000L, 512L, 3L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PdX", "MGS", "GBP", 'X', OrePrefixes.stickLong.get(aMaterial), 'M',
                        ItemList.Electric_Motor_HV.get(1L), 'S', OrePrefixes.screw.get(Materials.StainlessSteel), 'P',
                        OrePrefixes.plate.get(Materials.StainlessSteel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Cadmium.get(1L) });
                GT_ModHandler.addCraftingRecipe(
                    GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.SCREWDRIVER_HV,
                        1,
                        aMaterial,
                        Materials.StainlessSteel,
                        new long[] { 800000L, 512L, 3L, -1L }),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                        | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PdX", "MGS", "GBP", 'X', OrePrefixes.stickLong.get(aMaterial), 'M',
                        ItemList.Electric_Motor_HV.get(1L), 'S', OrePrefixes.screw.get(Materials.StainlessSteel), 'P',
                        OrePrefixes.plate.get(Materials.StainlessSteel), 'G',
                        OrePrefixes.gearGtSmall.get(Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Sodium.get(1L) });
                if (aSpecialRecipeReq2) GT_ModHandler.addCraftingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.toolHeadWrench, aMaterial, 1L),
                    GT_Proxy.tBits,
                    new Object[] { "hXW", "XRX", "WXd", 'X', OrePrefixes.plate.get(aMaterial), 'S',
                        OrePrefixes.plate.get(Materials.Steel), 'R', OrePrefixes.ring.get(Materials.Steel), 'W',
                        OrePrefixes.screw.get(Materials.Steel) });
            }
            case toolHeadHammer, toolHeadMallet -> {
                if (GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial.mHandleMaterial, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(
                            GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial.mHandleMaterial, 1L),
                            GT_OreDictUnificator.get(OrePrefixes.toolHeadHammer, aMaterial, 1L),
                            GT_Utility.getIntegratedCircuit(14))
                        .itemOutputs(
                            GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                                aProducesSoftMallet ? GT_MetaGenerated_Tool_01.SOFTMALLET
                                    : GT_MetaGenerated_Tool_01.HARDHAMMER,
                                1,
                                aMaterial,
                                aMaterial.mHandleMaterial,
                                null))
                        .duration(10 * SECONDS)
                        .eut(calculateRecipeEU(aMaterial, (int) TierEU.RECIPE_MV))
                        .addTo(sAssemblerRecipes);
                }
                if ((aMaterial != Materials.Stone) && (aMaterial != Materials.Flint)) {
                    GT_ModHandler.addShapelessCraftingRecipe(
                        GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                            aProducesSoftMallet ? GT_MetaGenerated_Tool_01.SOFTMALLET
                                : GT_MetaGenerated_Tool_01.HARDHAMMER,
                            1,
                            aMaterial,
                            aMaterial.mHandleMaterial,
                            null),
                        GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED,
                        new Object[] { aOreDictName, OrePrefixes.stick.get(aMaterial.mHandleMaterial) });
                    GT_ModHandler.addCraftingRecipe(
                        GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                            aProducesSoftMallet ? GT_MetaGenerated_Tool_01.SOFTMALLET
                                : GT_MetaGenerated_Tool_01.HARDHAMMER,
                            1,
                            aMaterial,
                            aMaterial.mHandleMaterial,
                            null),
                        GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED,
                        new Object[] { "XX ", "XXS", "XX ", 'X',
                            aMaterial == Materials.Wood ? OrePrefixes.plank.get(Materials.Wood)
                                : OrePrefixes.ingot.get(aMaterial),
                            'S', OrePrefixes.stick.get(aMaterial.mHandleMaterial) });
                    GT_ModHandler.addCraftingRecipe(
                        GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                            aProducesSoftMallet ? GT_MetaGenerated_Tool_01.SOFTMALLET
                                : GT_MetaGenerated_Tool_01.HARDHAMMER,
                            1,
                            aMaterial,
                            aMaterial.mHandleMaterial,
                            null),
                        GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED,
                        new Object[] { "XX ", "XXS", "XX ", 'X',
                            aMaterial == Materials.Wood ? OrePrefixes.plank.get(Materials.Wood)
                                : OrePrefixes.gem.get(aMaterial),
                            'S', OrePrefixes.stick.get(aMaterial.mHandleMaterial) });
                }
                if (aPrefix == OrePrefixes.toolHeadHammer) if (aSpecialRecipeReq1) GT_ModHandler.addCraftingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.toolHeadHammer, aMaterial, 1L),
                    GT_Proxy.tBits,
                    new Object[] { "II ", "IIh", "II ", 'P', OrePrefixes.plate.get(aMaterial), 'I',
                        OrePrefixes.ingot.get(aMaterial) });
            }
            case turbineBlade -> {
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.turbineBlade, aMaterial, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Magnalium, 1L))
                    .itemOutputs(GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(170, 1, aMaterial, aMaterial, null))
                    .duration(8 * SECONDS)
                    .eut(calculateRecipeEU(aMaterial, 100))
                    .addTo(sAssemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.turbineBlade, aMaterial, 8L),
                        GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Titanium, 1L))
                    .itemOutputs(GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(172, 1, aMaterial, aMaterial, null))
                    .duration(16 * SECONDS)
                    .eut(calculateRecipeEU(aMaterial, 400))
                    .addTo(sAssemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.turbineBlade, aMaterial, 12L),
                        GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.TungstenSteel, 1L))
                    .itemOutputs(GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(174, 1, aMaterial, aMaterial, null))
                    .duration(32 * SECONDS)
                    .eut(calculateRecipeEU(aMaterial, 1600))
                    .addTo(sAssemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.turbineBlade, aMaterial, 16L),
                        GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Americium, 1L))
                    .itemOutputs(GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(176, 1, aMaterial, aMaterial, null))
                    .duration(1 * MINUTES + 4 * SECONDS)
                    .eut(calculateRecipeEU(aMaterial, 6400))
                    .addTo(sAssemblerRecipes);
                if (aSpecialRecipeReq2) {
                    if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                        GT_ModHandler.addCraftingRecipe(
                            GT_OreDictUnificator.get(OrePrefixes.turbineBlade, aMaterial, 1L),
                            GT_Proxy.tBits,
                            new Object[] { "fPd", "SPS", " P ", 'P',
                                aMaterial == Materials.Wood ? OrePrefixes.plank.get(aMaterial)
                                    : OrePrefixes.plateDouble.get(aMaterial),
                                'R', OrePrefixes.ring.get(aMaterial), 'S', OrePrefixes.screw.get(aMaterial) });
                    }

                    // Turbine blades
                    if (GT_OreDictUnificator.get(OrePrefixes.plateDouble, aMaterial, 1L) != null
                        && GT_OreDictUnificator.get(OrePrefixes.screw, aMaterial, 1L) != null) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(
                                GT_OreDictUnificator.get(OrePrefixes.plateDouble, aMaterial, 3L),
                                GT_OreDictUnificator.get(OrePrefixes.screw, aMaterial, 2L))
                            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.turbineBlade, aMaterial, 1L))
                            .duration(10 * SECONDS)
                            .eut(calculateRecipeEU(aMaterial, 60))
                            .addTo(sPressRecipes);
                    }
                }
            }
            default -> {}
        }
    }
}
