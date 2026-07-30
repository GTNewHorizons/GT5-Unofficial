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

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.common.items.IDMetaTool01;
import gregtech.common.items.MetaGeneratedTool01;

public class ProcessingToolHead implements gregtech.api.interfaces.IOreRecipeRegistrator { // TODO COMPARE WITH OLD TOOL
                                                                                           // HEAD??? generator

    public static ProcessingToolHead INSTANCE;

    public ProcessingToolHead() {
        INSTANCE = this;
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
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        boolean unifiable = !Boolean.FALSE.equals(material.getProperty(GTMaterialProperties.UNIFIABLE));
        boolean specialRecipeReq1 = unifiable && !MaterialUtils.hasFlag(material, GTMaterialFlag.NO_SMASHING);
        boolean specialRecipeReq2 = unifiable && !MaterialUtils.hasFlag(material, GTMaterialFlag.NO_WORKING);
        boolean noWorking = MaterialUtils.hasFlag(material, GTMaterialFlag.NO_WORKING);
        boolean producesSoftMallet = MaterialUtils.hasFlag(material, GTMaterialFlag.BOUNCY)
            || MaterialUtils.hasFlag(material, GTMaterialFlag.WOOD)
            || MaterialUtils.hasFlag(material, GTMaterialFlag.SOFT);
        Integer processingTierEUProp = material.getProperty(GTMaterialProperties.PROCESSING_MATERIAL_TIER_EU);
        boolean belowProcessingTierIV = (processingTierEUProp == null ? 0 : processingTierEUProp) < TierEU.IV;
        switch (prefix.getName()) {
            case "toolHeadBuzzSaw" -> {
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.BUZZSAW_LV.ID,
                        1,
                        material,
                        Materials.Steel,
                        new long[] { 100000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PBM", "dXG", "SGP", 'X', oreDictName, 'M', ItemList.Electric_Motor_LV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.Steel), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.Steel), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGtSmall, Materials.Steel), 'B',
                        ItemList.Battery_RE_LV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.BUZZSAW_LV.ID,
                        1,
                        material,
                        Materials.Steel,
                        new long[] { 75000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PBM", "dXG", "SGP", 'X', oreDictName, 'M', ItemList.Electric_Motor_LV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.Steel), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.Steel), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGtSmall, Materials.Steel), 'B',
                        ItemList.Battery_RE_LV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.BUZZSAW_LV.ID,
                        1,
                        material,
                        Materials.Steel,
                        new long[] { 50000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PBM", "dXG", "SGP", 'X', oreDictName, 'M', ItemList.Electric_Motor_LV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.Steel), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.Steel), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGtSmall, Materials.Steel), 'B',
                        ItemList.Battery_RE_LV_Sodium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.BUZZSAW_MV.ID,
                        1,
                        material,
                        Materials.Aluminium,
                        new long[] { 400000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PBM", "dXG", "SGP", 'X', oreDictName, 'M', ItemList.Electric_Motor_MV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.Aluminium), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.Aluminium), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGtSmall, Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.BUZZSAW_MV.ID,
                        1,
                        material,
                        Materials.Aluminium,
                        new long[] { 300000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PBM", "dXG", "SGP", 'X', oreDictName, 'M', ItemList.Electric_Motor_MV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.Aluminium), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.Aluminium), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGtSmall, Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.BUZZSAW_MV.ID,
                        1,
                        material,
                        Materials.Aluminium,
                        new long[] { 200000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PBM", "dXG", "SGP", 'X', oreDictName, 'M', ItemList.Electric_Motor_MV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.Aluminium), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.Aluminium), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGtSmall, Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Sodium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.BUZZSAW_HV.ID,
                        1,
                        material,
                        Materials.StainlessSteel,
                        new long[] { 1600000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PBM", "dXG", "SGP", 'X', oreDictName, 'M', ItemList.Electric_Motor_HV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.StainlessSteel), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.StainlessSteel), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGtSmall, Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.BUZZSAW_HV.ID,
                        1,
                        material,
                        Materials.StainlessSteel,
                        new long[] { 1200000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PBM", "dXG", "SGP", 'X', oreDictName, 'M', ItemList.Electric_Motor_HV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.StainlessSteel), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.StainlessSteel), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGtSmall, Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.BUZZSAW_HV.ID,
                        1,
                        material,
                        Materials.StainlessSteel,
                        new long[] { 800000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PBM", "dXG", "SGP", 'X', oreDictName, 'M', ItemList.Electric_Motor_HV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.StainlessSteel), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.StainlessSteel), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGtSmall, Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Sodium.get(1L) });
                if (specialRecipeReq2) GTModHandler.addCraftingRecipe(
                    GTOreDictUnificator.get(OrePrefixes.toolHeadBuzzSaw, material, 1L),
                    GTModHandler.RecipeBits.BITS_STD,
                    new Object[] { "wXh", "X X", "fXx", 'X',
                        MaterialParts.craftIngredient(OrePrefixes.plate, material) });
            }
            case "toolHeadChainsaw" -> {
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.CHAINSAW_LV.ID,
                        1,
                        material,
                        Materials.Steel,
                        new long[] { 100000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', oreDictName, 'M', ItemList.Electric_Motor_LV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.Steel), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.Steel), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGtSmall, Materials.Steel), 'B',
                        ItemList.Battery_RE_LV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.CHAINSAW_LV.ID,
                        1,
                        material,
                        Materials.Steel,
                        new long[] { 75000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', oreDictName, 'M', ItemList.Electric_Motor_LV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.Steel), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.Steel), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGtSmall, Materials.Steel), 'B',
                        ItemList.Battery_RE_LV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.CHAINSAW_LV.ID,
                        1,
                        material,
                        Materials.Steel,
                        new long[] { 50000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', oreDictName, 'M', ItemList.Electric_Motor_LV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.Steel), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.Steel), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGtSmall, Materials.Steel), 'B',
                        ItemList.Battery_RE_LV_Sodium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.CHAINSAW_MV.ID,
                        1,
                        material,
                        Materials.Aluminium,
                        new long[] { 400000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', oreDictName, 'M', ItemList.Electric_Motor_MV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.Aluminium), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.Aluminium), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGtSmall, Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.CHAINSAW_MV.ID,
                        1,
                        material,
                        Materials.Aluminium,
                        new long[] { 300000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', oreDictName, 'M', ItemList.Electric_Motor_MV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.Aluminium), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.Aluminium), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGtSmall, Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.CHAINSAW_MV.ID,
                        1,
                        material,
                        Materials.Aluminium,
                        new long[] { 200000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', oreDictName, 'M', ItemList.Electric_Motor_MV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.Aluminium), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.Aluminium), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGtSmall, Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Sodium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.CHAINSAW_HV.ID,
                        1,
                        material,
                        Materials.StainlessSteel,
                        new long[] { 1600000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', oreDictName, 'M', ItemList.Electric_Motor_HV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.StainlessSteel), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.StainlessSteel), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGtSmall, Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.CHAINSAW_HV.ID,
                        1,
                        material,
                        Materials.StainlessSteel,
                        new long[] { 1200000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', oreDictName, 'M', ItemList.Electric_Motor_HV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.StainlessSteel), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.StainlessSteel), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGtSmall, Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.CHAINSAW_HV.ID,
                        1,
                        material,
                        Materials.StainlessSteel,
                        new long[] { 800000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', oreDictName, 'M', ItemList.Electric_Motor_HV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.StainlessSteel), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.StainlessSteel), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGtSmall, Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Sodium.get(1L) });
                if (specialRecipeReq2) GTModHandler.addCraftingRecipe(
                    GTOreDictUnificator.get(OrePrefixes.toolHeadChainsaw, material, 1L),
                    GTModHandler.RecipeBits.BITS_STD,
                    new Object[] { "SRS", "XhX", "SRS", 'X', MaterialParts.craftIngredient(OrePrefixes.plate, material),
                        'S', MaterialParts.craftIngredient(OrePrefixes.plate, Materials.Steel), 'R',
                        MaterialParts.craftIngredient(OrePrefixes.ring, Materials.Steel) });
            }
            case "toolHeadDrill" -> {
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.DRILL_LV.ID,
                        1,
                        material,
                        Materials.Steel,
                        new long[] { 100_000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', oreDictName, 'M', ItemList.Electric_Motor_LV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.Steel), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.Steel), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGtSmall, Materials.Steel), 'B',
                        ItemList.Battery_RE_LV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.DRILL_LV.ID,
                        1,
                        material,
                        Materials.Steel,
                        new long[] { 75_000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', oreDictName, 'M', ItemList.Electric_Motor_LV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.Steel), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.Steel), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGtSmall, Materials.Steel), 'B',
                        ItemList.Battery_RE_LV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.DRILL_LV.ID,
                        1,
                        material,
                        Materials.Steel,
                        new long[] { 50_000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', oreDictName, 'M', ItemList.Electric_Motor_LV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.Steel), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.Steel), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGtSmall, Materials.Steel), 'B',
                        ItemList.Battery_RE_LV_Sodium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.DRILL_MV.ID,
                        1,
                        material,
                        Materials.Aluminium,
                        new long[] { 400_000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', oreDictName, 'M', ItemList.Electric_Motor_MV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.Aluminium), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.Aluminium), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGtSmall, Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.DRILL_MV.ID,
                        1,
                        material,
                        Materials.Aluminium,
                        new long[] { 300_000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', oreDictName, 'M', ItemList.Electric_Motor_MV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.Aluminium), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.Aluminium), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGtSmall, Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.DRILL_MV.ID,
                        1,
                        material,
                        Materials.Aluminium,
                        new long[] { 200_000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', oreDictName, 'M', ItemList.Electric_Motor_MV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.Aluminium), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.Aluminium), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGtSmall, Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Sodium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.DRILL_HV.ID,
                        1,
                        material,
                        Materials.StainlessSteel,
                        new long[] { 1_600_000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', oreDictName, 'M', ItemList.Electric_Motor_HV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.StainlessSteel), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.StainlessSteel), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGtSmall, Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.DRILL_HV.ID,
                        1,
                        material,
                        Materials.StainlessSteel,
                        new long[] { 1_200_000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', oreDictName, 'M', ItemList.Electric_Motor_HV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.StainlessSteel), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.StainlessSteel), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGtSmall, Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.DRILL_HV.ID,
                        1,
                        material,
                        Materials.StainlessSteel,
                        new long[] { 800_000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', oreDictName, 'M', ItemList.Electric_Motor_HV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.StainlessSteel), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.StainlessSteel), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGtSmall, Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Sodium.get(1L) });
                // LV Jackhammer
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.JACKHAMMER_LV.ID,
                        1,
                        material,
                        Materials.Steel,
                        new long[] { 100_000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "PRP", "MPB", 'X',
                        MaterialParts.craftIngredient(OrePrefixes.stickLong, material), 'M',
                        ItemList.Electric_Piston_LV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.Steel), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.Steel), 'R',
                        MaterialParts.craftIngredient(OrePrefixes.spring, Materials.Steel), 'B',
                        ItemList.Battery_RE_LV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.JACKHAMMER_LV.ID,
                        1,
                        material,
                        Materials.Steel,
                        new long[] { 75_000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "PRP", "MPB", 'X',
                        MaterialParts.craftIngredient(OrePrefixes.stickLong, material), 'M',
                        ItemList.Electric_Piston_LV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.Steel), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.Steel), 'R',
                        MaterialParts.craftIngredient(OrePrefixes.spring, Materials.Steel), 'B',
                        ItemList.Battery_RE_LV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.JACKHAMMER_LV.ID,
                        1,
                        material,
                        Materials.Steel,
                        new long[] { 50_000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "PRP", "MPB", 'X',
                        MaterialParts.craftIngredient(OrePrefixes.stickLong, material), 'M',
                        ItemList.Electric_Piston_LV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.Steel), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.Steel), 'R',
                        MaterialParts.craftIngredient(OrePrefixes.spring, Materials.Steel), 'B',
                        ItemList.Battery_RE_LV_Sodium.get(1L) });
                // MV Jackhammer
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.JACKHAMMER_MV.ID,
                        1,
                        material,
                        Materials.Aluminium,
                        new long[] { 400_000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "PRP", "MPB", 'X',
                        MaterialParts.craftIngredient(OrePrefixes.stickLong, material), 'M',
                        ItemList.Electric_Piston_MV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.Aluminium), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.Aluminium), 'R',
                        MaterialParts.craftIngredient(OrePrefixes.spring, Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.JACKHAMMER_MV.ID,
                        1,
                        material,
                        Materials.Aluminium,
                        new long[] { 300_000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "PRP", "MPB", 'X',
                        MaterialParts.craftIngredient(OrePrefixes.stickLong, material), 'M',
                        ItemList.Electric_Piston_MV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.Aluminium), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.Aluminium), 'R',
                        MaterialParts.craftIngredient(OrePrefixes.spring, Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.JACKHAMMER_MV.ID,
                        1,
                        material,
                        Materials.Aluminium,
                        new long[] { 200_000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "PRP", "MPB", 'X',
                        MaterialParts.craftIngredient(OrePrefixes.stickLong, material), 'M',
                        ItemList.Electric_Piston_MV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.Aluminium), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.Aluminium), 'R',
                        MaterialParts.craftIngredient(OrePrefixes.spring, Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Sodium.get(1L) });
                // HV Jackhammer
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.JACKHAMMER_HV.ID,
                        1,
                        material,
                        Materials.StainlessSteel,
                        new long[] { 1_600_000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "PRP", "MPB", 'X',
                        MaterialParts.craftIngredient(OrePrefixes.stickLong, material), 'M',
                        ItemList.Electric_Piston_HV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.StainlessSteel), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.StainlessSteel), 'R',
                        MaterialParts.craftIngredient(OrePrefixes.spring, Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.JACKHAMMER_HV.ID,
                        1,
                        material,
                        Materials.StainlessSteel,
                        new long[] { 1_200_000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "PRP", "MPB", 'X',
                        MaterialParts.craftIngredient(OrePrefixes.stickLong, material), 'M',
                        ItemList.Electric_Piston_HV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.StainlessSteel), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.StainlessSteel), 'R',
                        MaterialParts.craftIngredient(OrePrefixes.spring, Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.JACKHAMMER_HV.ID,
                        1,
                        material,
                        Materials.StainlessSteel,
                        new long[] { 800_000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "PRP", "MPB", 'X',
                        MaterialParts.craftIngredient(OrePrefixes.stickLong, material), 'M',
                        ItemList.Electric_Piston_HV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.StainlessSteel), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.StainlessSteel), 'R',
                        MaterialParts.craftIngredient(OrePrefixes.spring, Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Sodium.get(1L) });
                if (specialRecipeReq2) {
                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.toolHeadDrill, material, 1L),
                        GTModHandler.RecipeBits.BITS_STD,
                        new Object[] { "XSX", "XSX", "ShS", 'X',
                            MaterialParts.craftIngredient(OrePrefixes.plate, material), 'S',
                            MaterialParts.craftIngredient(OrePrefixes.plate, Materials.Steel) });
                    if (MaterialUtils.hasMolten(material)) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(ItemList.Shape_Mold_ToolHeadDrill.get(0))
                            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.toolHeadDrill, material, 1L))
                            .fluidInputs(MaterialUtils.molten(material, 4 * INGOTS))
                            .duration(5 * SECONDS)
                            .eut(calculateRecipeEU(material, (int) TierEU.RECIPE_MV))
                            .addTo(fluidSolidifierRecipes);
                    }
                    if (GTOreDictUnificator.get(OrePrefixes.ingot, material, 1L) != null) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(
                                GTOreDictUnificator.get(OrePrefixes.ingot, material, 4L),
                                ItemList.Shape_Extruder_ToolHeadDrill.get(0))
                            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.toolHeadDrill, material, 1L))
                            .duration(5 * SECONDS)
                            .eut(calculateRecipeEU(material, (int) TierEU.RECIPE_MV))
                            .addTo(extruderRecipes);
                    }
                }
            }
            case "toolHeadFile" -> {
                if (belowProcessingTierIV) {
                    GTModHandler.addShapelessCraftingRecipe(
                        MetaGeneratedTool01.INSTANCE.getToolWithStats(
                            IDMetaTool01.FILE.ID,
                            1,
                            material,
                            MaterialUtils.handleMaterial(material),
                            null),
                        new Object[] { oreDictName,
                            OrePrefixes.stick.ingredient(MaterialUtils.handleMaterial(material)) });

                    if ((!MaterialUtils.hasFlag(material, GTMaterialFlag.NO_SMASHING))
                        && (!MaterialUtils.hasFlag(material, GTMaterialFlag.BOUNCY))) {
                        if (belowProcessingTierIV) {
                            GTModHandler.addCraftingRecipe(
                                MetaGeneratedTool01.INSTANCE.getToolWithStats(
                                    IDMetaTool01.FILE.ID,
                                    1,
                                    material,
                                    MaterialUtils.handleMaterial(material),
                                    null),
                                GTModHandler.RecipeBits.MIRRORED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                                    | GTModHandler.RecipeBits.BUFFERED,
                                new Object[] { "P", "P", "S", 'P',
                                    MaterialParts.craftIngredient(OrePrefixes.plate, material), 'S',
                                    OrePrefixes.stick.ingredient(MaterialUtils.handleMaterial(material)) });
                        }
                    }
                }
                if (GTOreDictUnificator.get(OrePrefixes.stick, MaterialUtils.handleMaterial(material), 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            GTOreDictUnificator.get(OrePrefixes.stick, MaterialUtils.handleMaterial(material), 1L),
                            GTOreDictUnificator.get(OrePrefixes.toolHeadFile, material, 1L))
                        .circuit(15)
                        .itemOutputs(
                            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                                IDMetaTool01.FILE.ID,
                                1,
                                material,
                                MaterialUtils.handleMaterial(material),
                                null))
                        .duration(10 * SECONDS)
                        .eut(calculateRecipeEU(material, (int) TierEU.RECIPE_MV))
                        .addTo(assemblerRecipes);
                }
            }
            case "toolHeadSaw" -> {
                if (belowProcessingTierIV) {

                    GTModHandler.addShapelessCraftingRecipe(
                        MetaGeneratedTool01.INSTANCE.getToolWithStats(
                            IDMetaTool01.SAW.ID,
                            1,
                            material,
                            MaterialUtils.handleMaterial(material),
                            null),
                        new Object[] { oreDictName,
                            OrePrefixes.stick.ingredient(MaterialUtils.handleMaterial(material)) });

                    if (specialRecipeReq1) GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.toolHeadSaw, material, 1L),
                        GTModHandler.RecipeBits.BITS_STD,
                        new Object[] { "PP ", "fh ", 'P', MaterialParts.craftIngredient(OrePrefixes.plate, material),
                            'I', MaterialParts.craftIngredient(OrePrefixes.ingot, material) });

                    if (!noWorking) GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.toolHeadSaw, material, 1L),
                        GTModHandler.RecipeBits.BITS_STD,
                        new Object[] { "GGf", 'G', MaterialParts.craftIngredient(OrePrefixes.gem, material) });
                }
                if (GTOreDictUnificator.get(OrePrefixes.stick, MaterialUtils.handleMaterial(material), 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            GTOreDictUnificator.get(OrePrefixes.stick, MaterialUtils.handleMaterial(material), 1L),
                            GTOreDictUnificator.get(OrePrefixes.toolHeadSaw, material, 1L))
                        .circuit(7)
                        .itemOutputs(
                            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                                IDMetaTool01.SAW.ID,
                                1,
                                material,
                                MaterialUtils.handleMaterial(material),
                                null))
                        .duration(10 * SECONDS)
                        .eut(calculateRecipeEU(material, (int) TierEU.RECIPE_MV))
                        .addTo(assemblerRecipes);
                }
            }
            case "toolHeadWrench" -> {
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.WRENCH_LV.ID,
                        1,
                        material,
                        Materials.Steel,
                        new long[] { 100000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', oreDictName, 'M', ItemList.Electric_Motor_LV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.Steel), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.Steel), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGtSmall, Materials.Steel), 'B',
                        ItemList.Battery_RE_LV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.WRENCH_LV.ID,
                        1,
                        material,
                        Materials.Steel,
                        new long[] { 75000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', oreDictName, 'M', ItemList.Electric_Motor_LV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.Steel), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.Steel), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGtSmall, Materials.Steel), 'B',
                        ItemList.Battery_RE_LV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.WRENCH_LV.ID,
                        1,
                        material,
                        Materials.Steel,
                        new long[] { 50000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', oreDictName, 'M', ItemList.Electric_Motor_LV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.Steel), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.Steel), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGtSmall, Materials.Steel), 'B',
                        ItemList.Battery_RE_LV_Sodium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.WRENCH_MV.ID,
                        1,
                        material,
                        Materials.Aluminium,
                        new long[] { 400000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', oreDictName, 'M', ItemList.Electric_Motor_MV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.Aluminium), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.Aluminium), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGtSmall, Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.WRENCH_MV.ID,
                        1,
                        material,
                        Materials.Aluminium,
                        new long[] { 300000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', oreDictName, 'M', ItemList.Electric_Motor_MV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.Aluminium), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.Aluminium), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGtSmall, Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.WRENCH_MV.ID,
                        1,
                        material,
                        Materials.Aluminium,
                        new long[] { 200000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', oreDictName, 'M', ItemList.Electric_Motor_MV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.Aluminium), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.Aluminium), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGtSmall, Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Sodium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.WRENCH_HV.ID,
                        1,
                        material,
                        Materials.StainlessSteel,
                        new long[] { 1600000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', oreDictName, 'M', ItemList.Electric_Motor_HV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.StainlessSteel), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.StainlessSteel), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGtSmall, Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.WRENCH_HV.ID,
                        1,
                        material,
                        Materials.StainlessSteel,
                        new long[] { 1200000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', oreDictName, 'M', ItemList.Electric_Motor_HV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.StainlessSteel), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.StainlessSteel), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGtSmall, Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.WRENCH_HV.ID,
                        1,
                        material,
                        Materials.StainlessSteel,
                        new long[] { 800000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXd", "GMG", "PBP", 'X', oreDictName, 'M', ItemList.Electric_Motor_HV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.StainlessSteel), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.StainlessSteel), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGtSmall, Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Sodium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SCREWDRIVER_LV.ID,
                        1,
                        material,
                        Materials.Steel,
                        new long[] { 100000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PdX", "MGS", "GBP", 'X',
                        MaterialParts.craftIngredient(OrePrefixes.stickLong, material), 'M',
                        ItemList.Electric_Motor_LV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.Steel), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.Steel), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGtSmall, Materials.Steel), 'B',
                        ItemList.Battery_RE_LV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SCREWDRIVER_LV.ID,
                        1,
                        material,
                        Materials.Steel,
                        new long[] { 75000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PdX", "MGS", "GBP", 'X',
                        MaterialParts.craftIngredient(OrePrefixes.stickLong, material), 'M',
                        ItemList.Electric_Motor_LV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.Steel), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.Steel), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGtSmall, Materials.Steel), 'B',
                        ItemList.Battery_RE_LV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SCREWDRIVER_LV.ID,
                        1,
                        material,
                        Materials.Steel,
                        new long[] { 50000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PdX", "MGS", "GBP", 'X',
                        MaterialParts.craftIngredient(OrePrefixes.stickLong, material), 'M',
                        ItemList.Electric_Motor_LV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.Steel), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.Steel), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGtSmall, Materials.Steel), 'B',
                        ItemList.Battery_RE_LV_Sodium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SCREWDRIVER_MV.ID,
                        1,
                        material,
                        Materials.Aluminium,
                        new long[] { 400000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PdX", "MGS", "GBP", 'X',
                        MaterialParts.craftIngredient(OrePrefixes.stickLong, material), 'M',
                        ItemList.Electric_Motor_MV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.Aluminium), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.Aluminium), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGtSmall, Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SCREWDRIVER_MV.ID,
                        1,
                        material,
                        Materials.Aluminium,
                        new long[] { 300000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PdX", "MGS", "GBP", 'X',
                        MaterialParts.craftIngredient(OrePrefixes.stickLong, material), 'M',
                        ItemList.Electric_Motor_MV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.Aluminium), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.Aluminium), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGtSmall, Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SCREWDRIVER_MV.ID,
                        1,
                        material,
                        Materials.Aluminium,
                        new long[] { 200000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PdX", "MGS", "GBP", 'X',
                        MaterialParts.craftIngredient(OrePrefixes.stickLong, material), 'M',
                        ItemList.Electric_Motor_MV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.Aluminium), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.Aluminium), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGtSmall, Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Sodium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SCREWDRIVER_HV.ID,
                        1,
                        material,
                        Materials.StainlessSteel,
                        new long[] { 1600000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PdX", "MGS", "GBP", 'X',
                        MaterialParts.craftIngredient(OrePrefixes.stickLong, material), 'M',
                        ItemList.Electric_Motor_HV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.StainlessSteel), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.StainlessSteel), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGtSmall, Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SCREWDRIVER_HV.ID,
                        1,
                        material,
                        Materials.StainlessSteel,
                        new long[] { 1200000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PdX", "MGS", "GBP", 'X',
                        MaterialParts.craftIngredient(OrePrefixes.stickLong, material), 'M',
                        ItemList.Electric_Motor_HV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.StainlessSteel), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.StainlessSteel), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGtSmall, Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.SCREWDRIVER_HV.ID,
                        1,
                        material,
                        Materials.StainlessSteel,
                        new long[] { 800000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PdX", "MGS", "GBP", 'X',
                        MaterialParts.craftIngredient(OrePrefixes.stickLong, material), 'M',
                        ItemList.Electric_Motor_HV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.StainlessSteel), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, Materials.StainlessSteel), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGtSmall, Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Sodium.get(1L) });
                // LV Electric Wirecutter
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.WIRECUTTER_LV.ID,
                        1,
                        material,
                        material,
                        new long[] { 100000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXS", "GMG", "PBP", 'X',
                        MetaGeneratedTool01.INSTANCE
                            .getToolWithStats(IDMetaTool01.WIRECUTTER.ID, 1, material, material, null),
                        'M', ItemList.Electric_Motor_LV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.wireFine, Materials.Electrum), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, material), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGt, Materials.Steel), 'B',
                        ItemList.Battery_RE_LV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.WIRECUTTER_LV.ID,
                        1,
                        material,
                        material,
                        new long[] { 75000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXS", "GMG", "PBP", 'X',
                        MetaGeneratedTool01.INSTANCE
                            .getToolWithStats(IDMetaTool01.WIRECUTTER.ID, 1, material, material, null),
                        'M', ItemList.Electric_Motor_LV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.wireFine, Materials.Electrum), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, material), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGt, Materials.Steel), 'B',
                        ItemList.Battery_RE_LV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.WIRECUTTER_LV.ID,
                        1,
                        material,
                        material,
                        new long[] { 50000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXS", "GMG", "PBP", 'X',
                        MetaGeneratedTool01.INSTANCE
                            .getToolWithStats(IDMetaTool01.WIRECUTTER.ID, 1, material, material, null),
                        'M', ItemList.Electric_Motor_LV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.wireFine, Materials.Electrum), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, material), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGt, Materials.Steel), 'B',
                        ItemList.Battery_RE_LV_Sodium.get(1L) });
                // MV Electric Wirecutter
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.WIRECUTTER_MV.ID,
                        1,
                        material,
                        material,
                        new long[] { 400000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXS", "GMG", "PBP", 'X',
                        MetaGeneratedTool01.INSTANCE
                            .getToolWithStats(IDMetaTool01.WIRECUTTER.ID, 1, material, material, null),
                        'M', ItemList.Electric_Motor_MV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.wireFine, Materials.Electrum), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, material), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGt, Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.WIRECUTTER_MV.ID,
                        1,
                        material,
                        material,
                        new long[] { 300000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXS", "GMG", "PBP", 'X',
                        MetaGeneratedTool01.INSTANCE
                            .getToolWithStats(IDMetaTool01.WIRECUTTER.ID, 1, material, material, null),
                        'M', ItemList.Electric_Motor_MV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.wireFine, Materials.Electrum), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, material), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGt, Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.WIRECUTTER_MV.ID,
                        1,
                        material,
                        material,
                        new long[] { 200000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXS", "GMG", "PBP", 'X',
                        MetaGeneratedTool01.INSTANCE
                            .getToolWithStats(IDMetaTool01.WIRECUTTER.ID, 1, material, material, null),
                        'M', ItemList.Electric_Motor_MV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.wireFine, Materials.Electrum), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, material), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGt, Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Sodium.get(1L) });
                // HV Electric Wirecutter
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.WIRECUTTER_HV.ID,
                        1,
                        material,
                        material,
                        new long[] { 1600000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXS", "GMG", "PBP", 'X',
                        MetaGeneratedTool01.INSTANCE
                            .getToolWithStats(IDMetaTool01.WIRECUTTER.ID, 1, material, material, null),
                        'M', ItemList.Electric_Motor_HV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.wireFine, Materials.Electrum), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, material), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGt, Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.WIRECUTTER_HV.ID,
                        1,
                        material,
                        material,
                        new long[] { 1200000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXS", "GMG", "PBP", 'X',
                        MetaGeneratedTool01.INSTANCE
                            .getToolWithStats(IDMetaTool01.WIRECUTTER.ID, 1, material, material, null),
                        'M', ItemList.Electric_Motor_HV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.wireFine, Materials.Electrum), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, material), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGt, Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.WIRECUTTER_HV.ID,
                        1,
                        material,
                        material,
                        new long[] { 800000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXS", "GMG", "PBP", 'X',
                        MetaGeneratedTool01.INSTANCE
                            .getToolWithStats(IDMetaTool01.WIRECUTTER.ID, 1, material, material, null),
                        'M', ItemList.Electric_Motor_HV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.wireFine, Materials.Electrum), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, material), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGt, Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Sodium.get(1L) });

                // LV Electric File
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.FILE_LV.ID,
                        1,
                        material,
                        Materials.Steel,
                        new long[] { 100000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXL", "GMG", "PBP", 'X', ItemList.Component_Grinder_Diamond.get(1), 'M',
                        ItemList.Electric_Motor_LV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.Steel), 'L',
                        MaterialParts.craftIngredient(OrePrefixes.stickLong, material), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, material), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGt, Materials.Steel), 'B',
                        ItemList.Battery_RE_LV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.FILE_LV.ID,
                        1,
                        material,
                        Materials.Steel,
                        new long[] { 75000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXL", "GMG", "PBP", 'X', ItemList.Component_Grinder_Diamond.get(1), 'M',
                        ItemList.Electric_Motor_LV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.Steel), 'L',
                        MaterialParts.craftIngredient(OrePrefixes.stickLong, material), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, material), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGt, Materials.Steel), 'B',
                        ItemList.Battery_RE_LV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.FILE_LV.ID,
                        1,
                        material,
                        Materials.Steel,
                        new long[] { 50000L, 32L, 1L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXL", "GMG", "PBP", 'X', ItemList.Component_Grinder_Diamond.get(1), 'M',
                        ItemList.Electric_Motor_LV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.Steel), 'L',
                        MaterialParts.craftIngredient(OrePrefixes.stickLong, material), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, material), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGt, Materials.Steel), 'B',
                        ItemList.Battery_RE_LV_Sodium.get(1L) });
                // MV Electric File
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.FILE_MV.ID,
                        1,
                        material,
                        Materials.Aluminium,
                        new long[] { 400000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXL", "GMG", "PBP", 'X', ItemList.Component_Grinder_Diamond.get(1), 'M',
                        ItemList.Electric_Motor_MV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.Aluminium), 'L',
                        MaterialParts.craftIngredient(OrePrefixes.stickLong, material), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, material), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGt, Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.FILE_MV.ID,
                        1,
                        material,
                        Materials.Aluminium,
                        new long[] { 300000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXL", "GMG", "PBP", 'X', ItemList.Component_Grinder_Diamond.get(1), 'M',
                        ItemList.Electric_Motor_MV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.Aluminium), 'L',
                        MaterialParts.craftIngredient(OrePrefixes.stickLong, material), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, material), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGt, Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.FILE_MV.ID,
                        1,
                        material,
                        Materials.Aluminium,
                        new long[] { 200000L, 128L, 2L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXL", "GMG", "PBP", 'X', ItemList.Component_Grinder_Diamond.get(1), 'M',
                        ItemList.Electric_Motor_MV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.Aluminium), 'L',
                        MaterialParts.craftIngredient(OrePrefixes.stickLong, material), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, material), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGt, Materials.Aluminium), 'B',
                        ItemList.Battery_RE_MV_Sodium.get(1L) });
                // HV Electric File
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.FILE_HV.ID,
                        1,
                        material,
                        Materials.StainlessSteel,
                        new long[] { 1600000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXL", "GMG", "PBP", 'X', ItemList.Component_Grinder_Tungsten.get(1), 'M',
                        ItemList.Electric_Motor_HV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.StainlessSteel), 'L',
                        MaterialParts.craftIngredient(OrePrefixes.stickLong, material), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, material), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGt, Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Lithium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.FILE_HV.ID,
                        1,
                        material,
                        Materials.StainlessSteel,
                        new long[] { 1200000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXL", "GMG", "PBP", 'X', ItemList.Component_Grinder_Tungsten.get(1), 'M',
                        ItemList.Electric_Motor_HV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.StainlessSteel), 'L',
                        MaterialParts.craftIngredient(OrePrefixes.stickLong, material), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, material), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGt, Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Cadmium.get(1L) });
                GTModHandler.addCraftingRecipe(
                    MetaGeneratedTool01.INSTANCE.getToolWithStats(
                        IDMetaTool01.FILE_HV.ID,
                        1,
                        material,
                        Materials.StainlessSteel,
                        new long[] { 800000L, 512L, 3L, -1L }),
                    GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "SXL", "GMG", "PBP", 'X', ItemList.Component_Grinder_Tungsten.get(1), 'M',
                        ItemList.Electric_Motor_HV.get(1L), 'S',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.StainlessSteel), 'L',
                        MaterialParts.craftIngredient(OrePrefixes.stickLong, material), 'P',
                        MaterialParts.craftIngredient(OrePrefixes.plate, material), 'G',
                        MaterialParts.craftIngredient(OrePrefixes.gearGt, Materials.StainlessSteel), 'B',
                        ItemList.Battery_RE_HV_Sodium.get(1L) });

                // Wrench Special Condition
                if (specialRecipeReq2) GTModHandler.addCraftingRecipe(
                    GTOreDictUnificator.get(OrePrefixes.toolHeadWrench, material, 1L),
                    GTModHandler.RecipeBits.BITS_STD,
                    new Object[] { "hXW", "XRX", "WXd", 'X', MaterialParts.craftIngredient(OrePrefixes.plate, material),
                        'S', MaterialParts.craftIngredient(OrePrefixes.plate, Materials.Steel), 'R',
                        MaterialParts.craftIngredient(OrePrefixes.ring, Materials.Steel), 'W',
                        MaterialParts.craftIngredient(OrePrefixes.screw, Materials.Steel) });
            }
            case "toolHeadHammer", "toolHeadMallet" -> {
                if (GTOreDictUnificator.get(OrePrefixes.stick, MaterialUtils.handleMaterial(material), 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            GTOreDictUnificator.get(OrePrefixes.stick, MaterialUtils.handleMaterial(material), 1L),
                            GTOreDictUnificator.get(OrePrefixes.toolHeadHammer, material, 1L))
                        .circuit(14)
                        .itemOutputs(
                            MetaGeneratedTool01.INSTANCE.getToolWithStats(
                                producesSoftMallet ? IDMetaTool01.SOFTMALLET.ID : IDMetaTool01.HARDHAMMER.ID,
                                1,
                                material,
                                MaterialUtils.handleMaterial(material),
                                null))
                        .duration(10 * SECONDS)
                        .eut(calculateRecipeEU(material, (int) TierEU.RECIPE_MV))
                        .addTo(assemblerRecipes);
                }
                if ((material != Materials.Stone) && (material != Materials.Flint)) {
                    GTModHandler.addShapelessCraftingRecipe(
                        MetaGeneratedTool01.INSTANCE.getToolWithStats(
                            producesSoftMallet ? IDMetaTool01.SOFTMALLET.ID : IDMetaTool01.HARDHAMMER.ID,
                            1,
                            material,
                            MaterialUtils.handleMaterial(material),
                            null),
                        GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                        new Object[] { oreDictName,
                            OrePrefixes.stick.ingredient(MaterialUtils.handleMaterial(material)) });
                    GTModHandler.addCraftingRecipe(
                        MetaGeneratedTool01.INSTANCE.getToolWithStats(
                            producesSoftMallet ? IDMetaTool01.SOFTMALLET.ID : IDMetaTool01.HARDHAMMER.ID,
                            1,
                            material,
                            MaterialUtils.handleMaterial(material),
                            null),
                        GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                        new Object[] { "XX ", "XXS", "XX ", 'X',
                            material == Materials.Wood
                                ? MaterialParts.craftIngredient(OrePrefixes.plank, Materials.Wood)
                                : MaterialParts.craftIngredient(OrePrefixes.ingot, material),
                            'S', OrePrefixes.stick.ingredient(MaterialUtils.handleMaterial(material)) });
                    GTModHandler.addCraftingRecipe(
                        MetaGeneratedTool01.INSTANCE.getToolWithStats(
                            producesSoftMallet ? IDMetaTool01.SOFTMALLET.ID : IDMetaTool01.HARDHAMMER.ID,
                            1,
                            material,
                            MaterialUtils.handleMaterial(material),
                            null),
                        GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                        new Object[] { "XX ", "XXS", "XX ", 'X',
                            material == Materials.Wood
                                ? MaterialParts.craftIngredient(OrePrefixes.plank, Materials.Wood)
                                : MaterialParts.craftIngredient(OrePrefixes.gem, material),
                            'S', OrePrefixes.stick.ingredient(MaterialUtils.handleMaterial(material)) });
                }
                if (prefix == OrePrefixes.toolHeadHammer)
                    if (specialRecipeReq1) GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.toolHeadHammer, material, 1L),
                        GTModHandler.RecipeBits.BITS_STD,
                        new Object[] { "II ", "IIh", "II ", 'P',
                            MaterialParts.craftIngredient(OrePrefixes.plate, material), 'I',
                            MaterialParts.craftIngredient(OrePrefixes.ingot, material) });
            }
            case "turbineBlade" -> {
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        GTOreDictUnificator.get(OrePrefixes.turbineBlade, material, 4L),
                        MaterialLibAPI.getStack(Materials.Magnalium, Shapes.stickLong, 1))
                    .itemOutputs(MetaGeneratedTool01.INSTANCE.getToolWithStats(170, 1, material, material, null))
                    .duration(8 * SECONDS)
                    .eut(calculateRecipeEU(material, 100))
                    .addTo(assemblerRecipes);
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        GTOreDictUnificator.get(OrePrefixes.turbineBlade, material, 8L),
                        MaterialLibAPI.getStack(Materials.Titanium, Shapes.stickLong, 1))
                    .itemOutputs(MetaGeneratedTool01.INSTANCE.getToolWithStats(172, 1, material, material, null))
                    .duration(16 * SECONDS)
                    .eut(calculateRecipeEU(material, 400))
                    .addTo(assemblerRecipes);
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        GTOreDictUnificator.get(OrePrefixes.turbineBlade, material, 12L),
                        MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.stickLong, 1))
                    .itemOutputs(MetaGeneratedTool01.INSTANCE.getToolWithStats(174, 1, material, material, null))
                    .duration(32 * SECONDS)
                    .eut(calculateRecipeEU(material, 1600))
                    .addTo(assemblerRecipes);
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        GTOreDictUnificator.get(OrePrefixes.turbineBlade, material, 16L),
                        MaterialLibAPI.getStack(Materials.Americium, Shapes.stickLong, 1))
                    .itemOutputs(MetaGeneratedTool01.INSTANCE.getToolWithStats(176, 1, material, material, null))
                    .duration(1 * MINUTES + 4 * SECONDS)
                    .eut(calculateRecipeEU(material, 6400))
                    .addTo(assemblerRecipes);
                if (specialRecipeReq2) {
                    if (belowProcessingTierIV) {
                        GTModHandler.addCraftingRecipe(
                            GTOreDictUnificator.get(OrePrefixes.turbineBlade, material, 1L),
                            GTModHandler.RecipeBits.BITS_STD,
                            new Object[] { "fPd", "SPS", " P ", 'P',
                                material == Materials.Wood ? MaterialParts.craftIngredient(OrePrefixes.plank, material)
                                    : MaterialParts.craftIngredient(OrePrefixes.plateDouble, material),
                                'R', MaterialParts.craftIngredient(OrePrefixes.ring, material), 'S',
                                MaterialParts.craftIngredient(OrePrefixes.screw, material) });
                    }

                    // Turbine blades
                    if (GTOreDictUnificator.get(OrePrefixes.plateDouble, material, 1L) != null
                        && GTOreDictUnificator.get(OrePrefixes.screw, material, 1L) != null) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(
                                GTOreDictUnificator.get(OrePrefixes.plateDouble, material, 3L),
                                GTOreDictUnificator.get(OrePrefixes.screw, material, 2L))
                            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.turbineBlade, material, 1L))
                            .duration(10 * SECONDS)
                            .eut(calculateRecipeEU(material, 60))
                            .addTo(formingPressRecipes);
                    }
                }
            }
            default -> {}
        }
    }
}
