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

import java.util.ArrayList;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.GTMod;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.ores.OreInfo;
import gregtech.common.ores.OreManager;

public class ProcessingOre implements IOreRecipeRegistrator {

    private final ArrayList<Material> mAlreadyListedOres = new ArrayList<>(1000);

    public static ProcessingOre INSTANCE;

    public ProcessingOre() {
        INSTANCE = this;
        for (OrePrefixes prefix : OrePrefixes.VALUES) {
            final String name = prefix.getName();
            if (!name.startsWith("ore")) continue;
            if (prefix == OrePrefixes.orePoor) continue;
            if (prefix == OrePrefixes.oreSmall) continue;
            if (prefix == OrePrefixes.oreRich) continue;
            if (prefix == OrePrefixes.oreNormal) continue;
            prefix.add(this);
        }
    }

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        if (material == null) return;

        if (MaterialUtils.hasFlag(material, GTMaterialFlag.NO_ORE_PROCESSING)) {
            return;
        }
        if (ProcessingOreMachine.owns(material)) {
            return;
        }

        boolean tIsRich = false;

        // For Sake of god of balance!

        // Dense ore
        if (GTMod.proxy.mRichOreYieldMultiplier) {
            tIsRich = (prefix == OrePrefixes.oreRich) || (prefix == OrePrefixes.oreDense);
        }
        // NetherOre
        if (GTMod.proxy.mNetherOreYieldMultiplier && !tIsRich) {
            tIsRich = (prefix == OrePrefixes.oreNetherrack) || (prefix == OrePrefixes.oreNether);
        }
        // EndOre
        if (GTMod.proxy.mEndOreYieldMultiplier && !tIsRich) {
            tIsRich = (prefix == OrePrefixes.oreEndstone) || (prefix == OrePrefixes.oreEnd);
        }

        if (material == Materials.Oilsands) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(1, stack))
                .itemOutputs(new ItemStack(Blocks.sand, 1, 0))
                .outputChances(tIsRich ? 2000 : 4000)
                .fluidOutputs(
                    MaterialLibAPI
                        .getFluidStack(Materials.OilHeavy, FluidShapes.fluidLiquid, (int) (tIsRich ? 4000 : 2000)))
                .duration(tIsRich ? 30 * SECONDS : 15 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(centrifugeRecipes);
        } else {
            registerStandardOreRecipes(prefix, material, GTUtility.copyAmount(1, stack), tIsRich ? 2 : 1);
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
            .get(OrePrefixes.crushed, material, (long) MaterialUtils.oreMultiplier(material) * multiplier);
        ItemStack tPrimaryByProduct = null;

        if (tCrushed == null) {
            tCrushed = GTOreDictUnificator.get(
                OrePrefixes.dustImpure,
                material,
                GTUtility.copyAmount((long) MaterialUtils.oreMultiplier(material) * multiplier, tCleaned, tDust, tGem),
                (long) MaterialUtils.oreMultiplier(material) * multiplier);
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
                    convertSmelting(
                        material,
                        prefix,
                        GTUtility.copyAmount(multiplier * MaterialUtils.smeltingMultiplier(material), tSmeltInto)));
            }

            if (MaterialUtils.hasFlag(material, GTMaterialFlag.BLASTFURNACE_CALCITE_TRIPLE)) {
                if (MaterialUtils.autoGenerateBlastFurnaceRecipes(material)) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            oreStack,
                            MaterialLibAPI.getStack(Materials.Calcite, Shapes.dust, (int) (multiplier)))
                        .itemOutputs(
                            GTUtility.mul(multiplier * 3 * MaterialUtils.smeltingMultiplier(material), tSmeltInto),
                            GTOreDictUnificator.get(OrePrefixes.dust, Materials.DarkAsh, 1L))
                        .outputChances(10000, 2500)
                        .duration(tSmeltInto.stackSize * 25 * SECONDS)
                        .eut(TierEU.RECIPE_MV)
                        .metadata(COIL_HEAT, 1500)
                        .addTo(blastFurnaceRecipes);
                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            oreStack,
                            MaterialLibAPI.getStack(Materials.Quicklime, Shapes.dust, (int) (multiplier)))
                        .itemOutputs(
                            GTUtility.mul(multiplier * 3 * MaterialUtils.smeltingMultiplier(material), tSmeltInto),
                            GTOreDictUnificator.get(OrePrefixes.dust, Materials.DarkAsh, 1L))
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
                            MaterialLibAPI.getStack(Materials.Calcite, Shapes.dust, (int) (multiplier)))
                        .itemOutputs(
                            GTUtility.mul(multiplier * 2 * MaterialUtils.smeltingMultiplier(material), tSmeltInto),
                            GTOreDictUnificator.get(OrePrefixes.dust, Materials.DarkAsh, 1L))
                        .outputChances(10000, 2500)
                        .duration(tSmeltInto.stackSize * 25 * SECONDS)
                        .eut(TierEU.RECIPE_MV)
                        .metadata(COIL_HEAT, 1500)
                        .addTo(blastFurnaceRecipes);
                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            oreStack,
                            MaterialLibAPI.getStack(Materials.Quicklime, Shapes.dust, (int) (multiplier)))
                        .itemOutputs(
                            GTUtility.mul(multiplier * 2 * MaterialUtils.smeltingMultiplier(material), tSmeltInto),
                            GTOreDictUnificator.get(OrePrefixes.dust, Materials.DarkAsh, 1L))
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
                    GTOreDictUnificator.get(
                        OrePrefixes.gem,
                        MaterialUtils.directSmelting(material),
                        Math.max(1, multiplier * MaterialUtils.smeltingMultiplier(material) / 2))));
        }

        if (tCrushed != null && material != Materials.Knightmetal) {
            GTValues.RA.stdBuilder()
                .itemInputs(oreStack)
                .itemOutputs(
                    convert(material, GTUtility.copy(GTUtility.copyAmount(tCrushed.stackSize, tGem), tCrushed)))
                .duration(10)
                .eut(TierEU.RECIPE_LV / 2)
                .addTo(hammerRecipes);

            ItemStack byproduct = GTOreDictUnificator
                .get(OrePrefixes.gem, tPrimaryByMaterial, GTUtility.copyAmount(1, tPrimaryByProduct), 1L);

            if (MaterialUtils.hasFlag(material, GTMaterialFlag.PULVERIZING_CINNABAR)) {
                byproduct = GTOreDictUnificator.get(OrePrefixes.crystal, Materials.Cinnabar, byproduct, 1L);
            }

            ItemStack stoneDust = null;

            try (OreInfo info = OreManager.getOreInfo(oreStack)) {
                if (info != null) {
                    stoneDust = info.stoneType.getDust(true, 1);
                }
            }

            if (stoneDust == null) {
                stoneDust = GTOreDictUnificator.getDust(prefix.mSecondaryMaterial);
            }

            int chanceOre2 = tPrimaryByProduct == null ? 0
                : tPrimaryByProduct.stackSize * 10 * multiplier * MaterialUtils.byProductMultiplier(material);
            chanceOre2 = 100 * chanceOre2; // converting to the GT format, 100% is 10000
            GTValues.RA.stdBuilder()
                .itemInputs(oreStack)
                .itemOutputs(convertOre(material, GTUtility.mul(2, tCrushed), byproduct, stoneDust))
                .outputChances(10000, chanceOre2, 5000)
                .duration(20 * SECONDS)
                .nbtSensitive()
                .eut(2)
                .addTo(maceratorRecipes);
        }
        return true;
    }
}
