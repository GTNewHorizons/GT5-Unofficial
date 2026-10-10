package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.GTMod;
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
import gregtech.api.material.ProcessingSubstitutes;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTUtility;
import gregtech.common.ores.OreInfo;
import gregtech.common.ores.OreManager;

public class ProcessingOre implements PartRecipeGenerator {

    public static final ProcessingOre INSTANCE = new ProcessingOre();

    private ProcessingOre() {}

    @Override
    public void generate(OrePrefixes prefix, Material material, Shape shape, ItemStack stack) {
        if (MaterialUtils.hasFlag(material, GTMaterialFlag.NO_ORE_PROCESSING)) {
            return;
        }

        boolean rich = prefix == OrePrefixes.oreNetherrack && GTMod.proxy.mNetherOreYieldMultiplier
            || prefix == OrePrefixes.oreEndstone && GTMod.proxy.mEndOreYieldMultiplier;

        if (material == Materials.Oilsands) {
            addOilsandsRecipe(stack, rich);
        } else {
            addOreRecipes(prefix, material, stack, rich ? 2 : 1, false);
        }
    }

    /// Centrifuges Oilsands ore into heavy oil and sand; a rich ore yields twice the oil over twice the time.
    static void addOilsandsRecipe(ItemStack ore, boolean rich) {
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, ore))
            .itemOutputs(new ItemStack(Blocks.sand, 1, 0))
            .outputChances(rich ? 2000 : 4000)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.OilHeavy, FluidShapes.fluidLiquid, rich ? 4000 : 2000))
            .duration(rich ? 30 * SECONDS : 15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(centrifugeRecipes);
    }

    /// Adds the smelting, blast furnace, forge hammer and macerator recipes of an ore block, or of a raw ore when
    /// `raw`, yielding `multiplier` times the usual amounts. A raw ore also gets a hammer crafting recipe and half the
    /// macerator byproduct chance.
    static void addOreRecipes(OrePrefixes prefix, Material material, ItemStack oreStack, int multiplier, boolean raw) {
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
        ItemStack tCrushed = GTUtility.copyAmount(
            (long) MaterialUtils.oreMultiplier(material) * multiplier,
            MaterialLibAPI.findStack(material, Shapes.crushed, 1));
        ItemStack tPrimaryByProduct = null;

        if (tCrushed == null) {
            tCrushed = GTUtility.copyAmount(
                (long) MaterialUtils.oreMultiplier(material) * multiplier,
                MaterialLibAPI.findStack(material, Shapes.dustImpure, 1),
                GTUtility.copyAmount((long) MaterialUtils.oreMultiplier(material) * multiplier, tCleaned, tDust, tGem));
        }

        for (Material tMat : MaterialUtils.oreByProducts(material)) {
            if (tPrimaryByProduct != null) break;
            tPrimaryByMaterial = tMat;
            tPrimaryByProduct = MaterialLibAPI.findStack(tMat, Shapes.dust, 1);
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
                    ProcessingSubstitutes.smelting(
                        material,
                        prefix,
                        GTUtility.copyAmount(multiplier * MaterialUtils.smeltingMultiplier(material), tSmeltInto)));
            }

            if (MaterialUtils.hasFlag(material, GTMaterialFlag.BLASTFURNACE_CALCITE_TRIPLE)) {
                if (MaterialUtils.autoGenerateBlastFurnaceRecipes(material)) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(oreStack, MaterialLibAPI.getStack(Materials.Calcite, Shapes.dust, multiplier))
                        .itemOutputs(
                            GTUtility.mul(multiplier * 3 * MaterialUtils.smeltingMultiplier(material), tSmeltInto),
                            MaterialLibAPI.getStack(Materials.DarkAsh, Shapes.dust, 1))
                        .outputChances(10000, 2500)
                        .duration(tSmeltInto.stackSize * 25 * SECONDS)
                        .eut(TierEU.RECIPE_MV)
                        .metadata(COIL_HEAT, 1500)
                        .addTo(blastFurnaceRecipes);
                    GTValues.RA.stdBuilder()
                        .itemInputs(oreStack, MaterialLibAPI.getStack(Materials.Quicklime, Shapes.dust, multiplier))
                        .itemOutputs(
                            GTUtility.mul(multiplier * 3 * MaterialUtils.smeltingMultiplier(material), tSmeltInto),
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
                            GTUtility.mul(multiplier * 2 * MaterialUtils.smeltingMultiplier(material), tSmeltInto),
                            MaterialLibAPI.getStack(Materials.DarkAsh, Shapes.dust, 1))
                        .outputChances(10000, 2500)
                        .duration(tSmeltInto.stackSize * 25 * SECONDS)
                        .eut(TierEU.RECIPE_MV)
                        .metadata(COIL_HEAT, 1500)
                        .addTo(blastFurnaceRecipes);
                    GTValues.RA.stdBuilder()
                        .itemInputs(oreStack, MaterialLibAPI.getStack(Materials.Quicklime, Shapes.dust, multiplier))
                        .itemOutputs(
                            GTUtility.mul(multiplier * 2 * MaterialUtils.smeltingMultiplier(material), tSmeltInto),
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
                ProcessingSubstitutes.smelting(
                    material,
                    prefix,
                    GTUtility.copyAmount(
                        Math.max(1, multiplier * MaterialUtils.smeltingMultiplier(material) / 2),
                        MaterialLibAPI.findStack(MaterialUtils.directSmelting(material), Shapes.gem, 1))));
        }

        if (tCrushed != null && (raw || material != Materials.Knightmetal)) {
            if (raw) {
                GTModHandler.addShapelessCraftingRecipe(
                    GTUtility.mul(1, tCrushed),
                    new Object[] { oreStack, ToolDictNames.craftingToolHardHammer });
            }

            GTValues.RA.stdBuilder()
                .itemInputs(oreStack)
                .itemOutputs(
                    ProcessingSubstitutes
                        .ore(material, GTUtility.copy(GTUtility.copyAmount(tCrushed.stackSize, tGem), tCrushed)))
                .duration(10)
                .eut(TierEU.RECIPE_LV / 2)
                .addTo(hammerRecipes);

            ItemStack byproduct = GTUtility.copyAmount(
                1L,
                MaterialLibAPI.findStack(tPrimaryByMaterial, Shapes.gem, 1),
                GTUtility.copyAmount(1, tPrimaryByProduct));

            ItemStack stoneDust = null;
            if (!raw) {
                try (OreInfo info = OreManager.getOreInfo(oreStack)) {
                    if (info != null) {
                        stoneDust = info.stoneType.getDust(true, 1);
                    }
                }
            }
            if (stoneDust == null) {
                stoneDust = MaterialParts.dustOf(prefix.mSecondaryMaterial);
            }

            int chanceOre2 = tPrimaryByProduct == null ? 0
                : tPrimaryByProduct.stackSize * (raw ? 5 : 10)
                    * multiplier
                    * MaterialUtils.byProductMultiplier(material);
            chanceOre2 = 100 * chanceOre2; // converting to the GT format, 100% is 10000
            GTRecipeBuilder macerator = GTValues.RA.stdBuilder()
                .itemInputs(oreStack)
                .itemOutputs(ProcessingSubstitutes.ore(material, GTUtility.mul(2, tCrushed), byproduct, stoneDust))
                .outputChances(10000, chanceOre2, 5000)
                .duration(20 * SECONDS)
                .eut(2);
            if (!raw) macerator.nbtSensitive();
            macerator.addTo(maceratorRecipes);
        }
    }
}
