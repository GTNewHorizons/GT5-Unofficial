package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MaterialUtils;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class ProcessingRawOre implements gregtech.api.interfaces.IOreRecipeRegistrator {

    private final OrePrefixes[] mRawOrePrefixes = { OrePrefixes.rawOre };

    public static ProcessingRawOre INSTANCE;

    public ProcessingRawOre() {
        INSTANCE = this;
        for (OrePrefixes tPrefix : this.mRawOrePrefixes) tPrefix.add(this);
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
        } else if (material == Materials2Materials.Oilsands) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(1, stack))
                .itemOutputs(new ItemStack(net.minecraft.init.Blocks.sand, 1, 0))
                .outputChances(4000)
                .fluidOutputs(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.OilHeavy, Materials2FluidShapes.fluidLiquid, (int) (2_000)))
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
        ItemStack tIngot = GTOreDictUnificator.get(OrePrefixes.ingot, MaterialUtils.directSmelting(material), 1L);
        ItemStack tGem = GTOreDictUnificator.get(OrePrefixes.gem, material, 1L);
        ItemStack tSmeltInto = tIngot == null ? null
            : MaterialUtils.hasFlag(material, GTMaterialFlag.SMELTING_TO_GEM) ? GTOreDictUnificator.get(
                OrePrefixes.gem,
                MaterialUtils.directSmelting(material),
                GTOreDictUnificator.get(
                    OrePrefixes.crystal,
                    MaterialUtils.directSmelting(material),
                    GTOreDictUnificator
                        .get(OrePrefixes.gem, material, GTOreDictUnificator.get(OrePrefixes.crystal, material, 1L), 1L),
                    1L),
                1L) : tIngot;
        ItemStack tDust = GTOreDictUnificator.get(OrePrefixes.dust, material, tGem, 1L);
        ItemStack tCleaned = GTOreDictUnificator.get(OrePrefixes.crushedPurified, material, tDust, 1L);
        ItemStack tCrushed = GTOreDictUnificator
            .get(OrePrefixes.crushed, material, MaterialUtils.oreMultiplier(material));
        ItemStack tPrimaryByProduct = null;

        if (tCrushed == null) {
            tCrushed = GTOreDictUnificator.get(
                OrePrefixes.dustImpure,
                material,
                GTUtility.copyAmount(MaterialUtils.oreMultiplier(material), tCleaned, tDust, tGem),
                MaterialUtils.oreMultiplier(material));
        }

        for (Material tMat : MaterialUtils.oreByProducts(material)) {
            GTOreDictUnificator.get(OrePrefixes.dust, tMat, 1L);
            if (tPrimaryByProduct == null) {
                tPrimaryByMaterial = tMat;
                tPrimaryByProduct = GTOreDictUnificator.get(OrePrefixes.dust, tMat, 1L);
                if (GTOreDictUnificator.get(OrePrefixes.dustSmall, tMat, 1L) == null) GTOreDictUnificator
                    .get(OrePrefixes.dustTiny, tMat, GTOreDictUnificator.get(OrePrefixes.nugget, tMat, 2L), 2L);
            }
            GTOreDictUnificator.get(OrePrefixes.dust, tMat, 1L);
            if (GTOreDictUnificator.get(OrePrefixes.dustSmall, tMat, 1L) == null) GTOreDictUnificator
                .get(OrePrefixes.dustTiny, tMat, GTOreDictUnificator.get(OrePrefixes.nugget, tMat, 2L), 2L);
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
                    GTUtility.copyAmount(MaterialUtils.smeltingMultiplier(material), tSmeltInto));
            }

            if (MaterialUtils.hasFlag(material, GTMaterialFlag.BLASTFURNACE_CALCITE_TRIPLE)) {
                if (MaterialUtils.autoGenerateBlastFurnaceRecipes(material)) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            oreStack,
                            MaterialLibAPI
                                .getStack(Materials2Materials.Calcite, Materials2Shapes.dust, (int) (multiplier)))
                        .itemOutputs(
                            GTUtility.mul(3 * MaterialUtils.smeltingMultiplier(material), tSmeltInto),
                            GTOreDictUnificator.get(OrePrefixes.dust, Materials2Materials.DarkAsh, 1L))
                        .outputChances(10000, 2500)
                        .duration(tSmeltInto.stackSize * 25 * SECONDS)
                        .eut(TierEU.RECIPE_MV)
                        .metadata(COIL_HEAT, 1500)
                        .addTo(blastFurnaceRecipes);
                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            oreStack,
                            MaterialLibAPI
                                .getStack(Materials2Materials.Quicklime, Materials2Shapes.dust, (int) (multiplier)))
                        .itemOutputs(
                            GTUtility.mul(3 * MaterialUtils.smeltingMultiplier(material), tSmeltInto),
                            GTOreDictUnificator.get(OrePrefixes.dust, Materials2Materials.DarkAsh, 1L))
                        .outputChances(10000, 2500)
                        .duration(tSmeltInto.stackSize * 25 * SECONDS)
                        .eut(TierEU.RECIPE_MV)
                        .metadata(COIL_HEAT, 1500)
                        .addTo(blastFurnaceRecipes);
                }
            } else if (MaterialUtils.hasFlag(material, GTMaterialFlag.BLASTFURNACE_CALCITE_DOUBLE)) {
                if (MaterialUtils.autoGenerateBlastFurnaceRecipes(material)) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            oreStack,
                            MaterialLibAPI
                                .getStack(Materials2Materials.Calcite, Materials2Shapes.dust, (int) (multiplier)))
                        .itemOutputs(
                            GTUtility.mul(2 * MaterialUtils.smeltingMultiplier(material), tSmeltInto),
                            GTOreDictUnificator.get(OrePrefixes.dust, Materials2Materials.DarkAsh, 1L))
                        .outputChances(10000, 2500)
                        .duration(tSmeltInto.stackSize * 25 * SECONDS)
                        .eut(TierEU.RECIPE_MV)
                        .metadata(COIL_HEAT, 1500)
                        .addTo(blastFurnaceRecipes);
                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            oreStack,
                            MaterialLibAPI
                                .getStack(Materials2Materials.Quicklime, Materials2Shapes.dust, (int) (multiplier)))
                        .itemOutputs(
                            GTUtility.mul(2 * MaterialUtils.smeltingMultiplier(material), tSmeltInto),
                            GTOreDictUnificator.get(OrePrefixes.dust, Materials2Materials.DarkAsh, 1L))
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
                GTOreDictUnificator.get(
                    OrePrefixes.gem,
                    MaterialUtils.directSmelting(material),
                    Math.max(1, MaterialUtils.smeltingMultiplier(material) / 2)));
        }

        if (tCrushed != null) {
            GTModHandler.addShapelessCraftingRecipe(
                GTUtility.mul(1, tCrushed),
                new Object[] { oreStack, ToolDictNames.craftingToolHardHammer });

            GTValues.RA.stdBuilder()
                .itemInputs(oreStack)
                .itemOutputs(GTUtility.copy(GTUtility.copyAmount(tCrushed.stackSize, tGem), tCrushed))
                .duration(10)
                .eut(TierEU.RECIPE_LV / 2)
                .addTo(hammerRecipes);

            int chanceOre2 = tPrimaryByProduct == null ? 0
                : tPrimaryByProduct.stackSize * 5 * MaterialUtils.byProductMultiplier(material);
            chanceOre2 = 100 * chanceOre2; // converting to the GT format, 100% is 10000
            GTValues.RA.stdBuilder()
                .itemInputs(oreStack)
                .itemOutputs(
                    GTUtility.mul(2, tCrushed),
                    MaterialUtils.hasFlag(material, GTMaterialFlag.PULVERIZING_CINNABAR) ? GTOreDictUnificator.get(
                        OrePrefixes.crystal,
                        Materials2Materials.Cinnabar,
                        GTOreDictUnificator
                            .get(OrePrefixes.gem, tPrimaryByMaterial, GTUtility.copyAmount(1, tPrimaryByProduct), 1L),
                        1L)
                        : GTOreDictUnificator
                            .get(OrePrefixes.gem, tPrimaryByMaterial, GTUtility.copyAmount(1, tPrimaryByProduct), 1L),
                    GTOreDictUnificator.getDust(prefix.mSecondaryMaterial))
                .outputChances(10000, chanceOre2, 5000)
                .duration(20 * SECONDS)
                .eut(2)
                .addTo(maceratorRecipes);
        }
        return true;
    }
}
