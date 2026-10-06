package gregtech.loaders.oreprocessing;

import static bartworks.system.material.gtenhancement.PlatinumSludgeOutputs.convertSmelting;
import static goodgenerator.util.NaquadahRecipeOutputs.convert;
import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gtnhlanth.util.LanthanidesRecipeOutputs.convertOre;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;

public class ProcessingRawOre implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public static ProcessingRawOre INSTANCE;

    public ProcessingRawOre() {
        INSTANCE = this;
    }

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        if (material == null) return;

        if (MaterialUtils.hasFlag(material, GTMaterialFlag.NO_ORE_PROCESSING)) {
            return;
        }

        if (MaterialUtils.hasFlag(material, GTMaterialFlag.ICE_ORE)) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(1, stack))
                .fluidOutputs(MaterialUtils.gas(material, 1000L * MaterialUtils.oreMultiplier(material)))
                .duration(2 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(RecipeMaps.fluidExtractionRecipes);
        } else if (material == Materials.Oilsands) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(1, stack))
                .itemOutputs(new ItemStack(net.minecraft.init.Blocks.sand, 1, 0))
                .outputChances(4000)
                .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.OilHeavy, FluidShapes.fluidLiquid, 2_000))
                .duration(15 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(centrifugeRecipes);
        } else {
            registerStandardOreRecipes(prefix, material, GTUtility.copyAmount(1, stack), 1);
        }
    }

    private boolean registerStandardOreRecipes(OrePrefixes prefix, Material material, ItemStack oreStack,
        int multiplier) {
        if ((oreStack == null) || (material == null)) return false;
        Material tPrimaryByMaterial = null;
        multiplier = Math.max(1, multiplier);
        oreStack = GTUtility.copyAmount(1, oreStack);
        oreStack.stackSize = 1;
        ItemStack tIngot = MaterialLibAPI.findStack(MaterialUtils.directSmelting(material), Shapes.ingot, 1);
        ItemStack tGem = MaterialLibAPI.findStack(material, Shapes.gem, 1);
        ItemStack tSmeltInto = tIngot == null ? null
            : MaterialUtils.hasFlag(material, GTMaterialFlag.SMELTING_TO_GEM) ? GTUtility
                .copyAmount(1L, MaterialLibAPI.findStack(MaterialUtils.directSmelting(material), Shapes.gem, 1), tGem)
                : tIngot;
        ItemStack tDust = GTUtility.copyAmount(1L, MaterialLibAPI.findStack(material, Shapes.dust, 1), tGem);
        ItemStack tCleaned = GTUtility
            .copyAmount(1L, MaterialLibAPI.findStack(material, Shapes.crushedPurified, 1), tDust);
        ItemStack tCrushed = GTUtility
            .copyAmount(MaterialUtils.oreMultiplier(material), MaterialLibAPI.findStack(material, Shapes.crushed, 1));
        ItemStack tPrimaryByProduct = null;

        if (tCrushed == null) {
            tCrushed = GTUtility.copyAmount(
                MaterialUtils.oreMultiplier(material),
                MaterialLibAPI.findStack(material, Shapes.dustImpure, 1),
                GTUtility.copyAmount(MaterialUtils.oreMultiplier(material), tCleaned, tDust, tGem));
        }

        for (Material tMat : MaterialUtils.oreByProducts(material)) {
            MaterialLibAPI.findStack(tMat, Shapes.dust, 1);
            if (tPrimaryByProduct == null) {
                tPrimaryByMaterial = tMat;
                tPrimaryByProduct = MaterialLibAPI.findStack(tMat, Shapes.dust, 1);
                if (MaterialLibAPI.findStack(tMat, Shapes.dustSmall, 1) == null) GTUtility.copyAmount(
                    2L,
                    MaterialLibAPI.findStack(tMat, Shapes.dustTiny, 1),
                    MaterialLibAPI.findStack(tMat, Shapes.nugget, 2));
            }
            MaterialLibAPI.findStack(tMat, Shapes.dust, 1);
            if (MaterialLibAPI.findStack(tMat, Shapes.dustSmall, 1) == null) GTUtility.copyAmount(
                2L,
                MaterialLibAPI.findStack(tMat, Shapes.dustTiny, 1),
                MaterialLibAPI.findStack(tMat, Shapes.nugget, 2));
        }

        if (tPrimaryByMaterial == null) tPrimaryByMaterial = material;
        if (tPrimaryByProduct == null) tPrimaryByProduct = tDust;
        boolean tHasSmelting = false;

        if (tSmeltInto != null) {
            if (MaterialUtils.blastFurnaceRequired(material)
                || MaterialUtils.blastFurnaceRequired(MaterialUtils.directSmelting(material))) {
                GTModHandler.removeFurnaceSmelting(oreStack);
            } else {
                tHasSmelting = GTModHandler.addSmeltingRecipe(
                    oreStack,
                    convertSmelting(
                        material,
                        prefix,
                        GTUtility.copyAmount(MaterialUtils.smeltingMultiplier(material), tSmeltInto)));
            }

            if (MaterialUtils.hasFlag(material, GTMaterialFlag.BLASTFURNACE_CALCITE_TRIPLE)) {
                if (MaterialUtils.autoGenerateBlastFurnaceRecipes(material)) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(oreStack, MaterialLibAPI.getStack(Materials.Calcite, Shapes.dust, multiplier))
                        .itemOutputs(
                            GTUtility.mul(3 * MaterialUtils.smeltingMultiplier(material), tSmeltInto),
                            MaterialLibAPI.getStack(Materials.DarkAsh, Shapes.dust, 1))
                        .outputChances(10000, 2500)
                        .duration(tSmeltInto.stackSize * 25 * SECONDS)
                        .eut(TierEU.RECIPE_MV)
                        .metadata(COIL_HEAT, 1500)
                        .addTo(blastFurnaceRecipes);
                    GTValues.RA.stdBuilder()
                        .itemInputs(oreStack, MaterialLibAPI.getStack(Materials.Quicklime, Shapes.dust, multiplier))
                        .itemOutputs(
                            GTUtility.mul(3 * MaterialUtils.smeltingMultiplier(material), tSmeltInto),
                            MaterialLibAPI.getStack(Materials.DarkAsh, Shapes.dust, 1))
                        .outputChances(10000, 2500)
                        .duration(tSmeltInto.stackSize * 25 * SECONDS)
                        .eut(TierEU.RECIPE_MV)
                        .metadata(COIL_HEAT, 1500)
                        .addTo(blastFurnaceRecipes);
                }
            } else if (MaterialUtils.hasFlag(material, GTMaterialFlag.BLASTFURNACE_CALCITE_DOUBLE)) {
                if (MaterialUtils.autoGenerateBlastFurnaceRecipes(material)) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(oreStack, MaterialLibAPI.getStack(Materials.Calcite, Shapes.dust, multiplier))
                        .itemOutputs(
                            GTUtility.mul(2 * MaterialUtils.smeltingMultiplier(material), tSmeltInto),
                            MaterialLibAPI.getStack(Materials.DarkAsh, Shapes.dust, 1))
                        .outputChances(10000, 2500)
                        .duration(tSmeltInto.stackSize * 25 * SECONDS)
                        .eut(TierEU.RECIPE_MV)
                        .metadata(COIL_HEAT, 1500)
                        .addTo(blastFurnaceRecipes);
                    GTValues.RA.stdBuilder()
                        .itemInputs(oreStack, MaterialLibAPI.getStack(Materials.Quicklime, Shapes.dust, multiplier))
                        .itemOutputs(
                            GTUtility.mul(2 * MaterialUtils.smeltingMultiplier(material), tSmeltInto),
                            MaterialLibAPI.getStack(Materials.DarkAsh, Shapes.dust, 1))
                        .outputChances(10000, 2500)
                        .duration(tSmeltInto.stackSize * 25 * SECONDS)
                        .eut(TierEU.RECIPE_MV)
                        .metadata(COIL_HEAT, 1500)
                        .addTo(blastFurnaceRecipes);
                }
            }
        }

        if (!tHasSmelting) {
            GTModHandler.addSmeltingRecipe(
                oreStack,
                convertSmelting(
                    material,
                    prefix,
                    GTUtility.copyAmount(
                        Math.max(1, MaterialUtils.smeltingMultiplier(material) / 2),
                        MaterialLibAPI.findStack(MaterialUtils.directSmelting(material), Shapes.gem, 1))));
        }

        if (tCrushed != null) {
            GTModHandler.addShapelessCraftingRecipe(
                GTUtility.mul(1, tCrushed),
                new Object[] { oreStack, ToolDictNames.craftingToolHardHammer });

            GTValues.RA.stdBuilder()
                .itemInputs(oreStack)
                .itemOutputs(
                    convert(material, GTUtility.copy(GTUtility.copyAmount(tCrushed.stackSize, tGem), tCrushed)))
                .duration(10)
                .eut(TierEU.RECIPE_LV / 2)
                .addTo(hammerRecipes);

            int chanceOre2 = tPrimaryByProduct == null ? 0
                : tPrimaryByProduct.stackSize * 5 * MaterialUtils.byProductMultiplier(material);
            chanceOre2 = 100 * chanceOre2; // converting to the GT format, 100% is 10000
            ItemStack secondary = GTUtility.copyAmount(
                1L,
                MaterialLibAPI.findStack(tPrimaryByMaterial, Shapes.gem, 1),
                GTUtility.copyAmount(1, tPrimaryByProduct));
            GTValues.RA.stdBuilder()
                .itemInputs(oreStack)
                .itemOutputs(
                    convertOre(
                        material,
                        GTUtility.mul(2, tCrushed),
                        secondary,
                        MaterialParts.dustOf(prefix.mSecondaryMaterial)))
                .outputChances(10000, chanceOre2, 5000)
                .duration(20 * SECONDS)
                .eut(2)
                .addTo(maceratorRecipes);
        }
        return true;
    }

}
