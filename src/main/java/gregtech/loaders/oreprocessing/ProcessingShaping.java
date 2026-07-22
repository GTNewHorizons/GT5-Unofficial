package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.alloySmelterRecipes;
import static gregtech.api.recipe.RecipeMaps.extruderRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.util.GTRecipeBuilder.EIGHTH_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.HALF_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.NUGGETS;
import static gregtech.api.util.GTRecipeBuilder.QUARTER_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MU;
import gregtech.api.recipe.RecipeCategories;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

@SuppressWarnings("RedundantLabeledSwitchRuleCodeBlock")
public class ProcessingShaping implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public static ProcessingShaping INSTANCE;

    public ProcessingShaping() {
        INSTANCE = this;
        OrePrefixes.ingot.add(this);
        OrePrefixes.dust.add(this);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Materials material, String oreDictName, String modName,
        ItemStack stack) {
        registerOre(prefix, MU.material(material), oreDictName, modName, stack);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        Materials legacyMaterial = MU.materialOf(material);
        if (legacyMaterial == null) return;

        // Blacklist materials which are handled by Werkstoff loader
        if (legacyMaterial == Materials.Calcium || legacyMaterial == Materials.Magnesia) return;

        if (((legacyMaterial == Materials.Glass) || (GTOreDictUnificator.get(OrePrefixes.ingot, material, 1L) != null))
            && (!MU.hasFlag(material, GTMaterialFlag.NO_SMELTING))) {
            long materialMass = legacyMaterial.getMass();
            int tAmount = (int) (prefix.getMaterialAmount() / 3628800L);
            if ((tAmount > 0) && (tAmount <= 64) && (prefix.getMaterialAmount() % 3628800L == 0L)) {
                int tVoltageMultiplier = MU.blastFurnaceTemp(material) >= 2800 ? 60 : 15;
                Integer processingTierEU = material.getProperty(GTMaterialProperties.PROCESSING_MATERIAL_TIER_EU);
                int tTrueVoltage = processingTierEU == null ? 0 : processingTierEU;

                if (MU.hasFlag(material, GTMaterialFlag.NO_SMASHING)) {
                    tVoltageMultiplier /= 4;
                } else if (prefix.getName()
                    .startsWith(OrePrefixes.dust.getName())) {
                        return;
                    }

                if (!OrePrefixes.block.isIgnored(MU.smeltInto(legacyMaterial))
                    && (GTOreDictUnificator.get(OrePrefixes.block, MU.smeltInto(material), 1L) != null)
                    && legacyMaterial != Materials.Ichorium
                    && legacyMaterial != Materials.Obsidian) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(9, stack), ItemList.Shape_Extruder_Block.get(0L))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.block, MU.smeltInto(material), tAmount))
                        .duration(materialMass * 9 * TICKS)
                        .eut(calculateRecipeEU(legacyMaterial, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);

                    // Allow creation of alloy smelter recipes for material recycling if < IV tier.
                    if (tTrueVoltage < TierEU.IV) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(9, stack), ItemList.Shape_Mold_Block.get(0L))
                            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.block, MU.smeltInto(material), tAmount))
                            .duration(materialMass * 9 * TICKS)
                            .eut(calculateRecipeEU(legacyMaterial, 4 * tVoltageMultiplier))
                            .recipeCategory(RecipeCategories.alloySmelterMolding)
                            .addTo(alloySmelterRecipes);
                    }
                }
                if ((prefix != OrePrefixes.ingot || material != MU.smeltInto(material))
                    && GTOreDictUnificator.get(OrePrefixes.ingot, MU.smeltInto(material), 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack), ItemList.Shape_Extruder_Ingot.get(0L))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ingot, MU.smeltInto(material), tAmount))
                        .duration(10 * TICKS)
                        .eut(calculateRecipeEU(legacyMaterial, 4 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (GTOreDictUnificator.get(OrePrefixes.pipeTiny, MU.smeltInto(material), 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack), ItemList.Shape_Extruder_Pipe_Tiny.get(0L))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.pipeTiny, MU.smeltInto(material), tAmount * 2))
                        .duration((4 * tAmount) * TICKS)
                        .eut(calculateRecipeEU(legacyMaterial, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (GTOreDictUnificator.get(OrePrefixes.pipeSmall, MU.smeltInto(material), 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack), ItemList.Shape_Extruder_Pipe_Small.get(0L))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.pipeSmall, MU.smeltInto(material), tAmount))
                        .duration((8 * tAmount) * TICKS)
                        .eut(calculateRecipeEU(legacyMaterial, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (GTOreDictUnificator.get(OrePrefixes.pipeMedium, MU.smeltInto(material), 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(3, stack), ItemList.Shape_Extruder_Pipe_Medium.get(0L))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.pipeMedium, MU.smeltInto(material), tAmount))
                        .duration((24 * tAmount) * TICKS)
                        .eut(calculateRecipeEU(legacyMaterial, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (GTOreDictUnificator.get(OrePrefixes.pipeLarge, MU.smeltInto(material), 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(6, stack), ItemList.Shape_Extruder_Pipe_Large.get(0L))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.pipeLarge, MU.smeltInto(material), tAmount))
                        .duration((48 * tAmount) * TICKS)
                        .eut(calculateRecipeEU(legacyMaterial, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (GTOreDictUnificator.get(OrePrefixes.pipeHuge, MU.smeltInto(material), 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(12, stack), ItemList.Shape_Extruder_Pipe_Huge.get(0L))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.pipeHuge, MU.smeltInto(material), tAmount))
                        .duration((96 * tAmount) * TICKS)
                        .eut(calculateRecipeEU(legacyMaterial, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (GTOreDictUnificator.get(OrePrefixes.plate, MU.smeltInto(material), 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack), ItemList.Shape_Extruder_Plate.get(0L))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, MU.smeltInto(material), tAmount))
                        .duration(((int) Math.max(materialMass * tAmount, tAmount)) * TICKS)
                        .eut(calculateRecipeEU(legacyMaterial, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (GTOreDictUnificator.get(OrePrefixes.gearGtSmall, MU.smeltInto(material), 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack), ItemList.Shape_Extruder_Small_Gear.get(0L))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gearGtSmall, MU.smeltInto(material), tAmount))
                        .duration(((int) Math.max(materialMass * tAmount, tAmount)) * TICKS)
                        .eut(calculateRecipeEU(legacyMaterial, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (GTOreDictUnificator.get(OrePrefixes.turbineBlade, MU.smeltInto(material), 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(6, stack), ItemList.Shape_Extruder_Turbine_Blade.get(0L))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.turbineBlade, MU.smeltInto(material), tAmount))
                        .duration(((int) Math.max(materialMass * tAmount, tAmount)) * TICKS)
                        .eut(calculateRecipeEU(legacyMaterial, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }

                if (!(legacyMaterial == Materials.AnnealedCopper || legacyMaterial == Materials.CastIron)
                    && !(MU.hasFlag(material, GTMaterialFlag.NO_SMELTING))
                    && prefix == OrePrefixes.ingot) {
                    if (legacyMaterial.mStandardMoltenFluid != null) {
                        if (GTOreDictUnificator.get(OrePrefixes.ring, material, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Ring.get(0L))
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ring, material, 1L))
                                .fluidInputs(legacyMaterial.getMolten(1 * QUARTER_INGOTS))
                                .duration(5 * SECONDS)
                                .eut(calculateRecipeEU(legacyMaterial, 4 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                        if (GTOreDictUnificator.get(OrePrefixes.screw, material, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Screw.get(0L))
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.screw, material, 1L))
                                .fluidInputs(legacyMaterial.getMolten(1 * EIGHTH_INGOTS))
                                .duration(2 * SECONDS + 10 * TICKS)
                                .eut(calculateRecipeEU(legacyMaterial, 2 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                        if (GTOreDictUnificator.get(OrePrefixes.stick, material, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Rod.get(0L))
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.stick, material, 1L))
                                .fluidInputs(legacyMaterial.getMolten(1 * HALF_INGOTS))
                                .duration(7 * SECONDS + 10 * TICKS)
                                .eut(calculateRecipeEU(legacyMaterial, 8 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                        if (GTOreDictUnificator.get(OrePrefixes.bolt, material, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Bolt.get(0L))
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.bolt, material, 1L))
                                .fluidInputs(legacyMaterial.getMolten(1 * EIGHTH_INGOTS))
                                .duration(2 * SECONDS + 10 * TICKS)
                                .eut(calculateRecipeEU(legacyMaterial, 2 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                        if (GTOreDictUnificator.get(OrePrefixes.round, material, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Round.get(0L))
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.round, material, 1L))
                                .fluidInputs(legacyMaterial.getMolten(1 * NUGGETS))
                                .duration(2 * SECONDS + 10 * TICKS)
                                .eut(calculateRecipeEU(legacyMaterial, 2 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                        if (GTOreDictUnificator.get(OrePrefixes.stickLong, material, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Rod_Long.get(0L))
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.stickLong, material, 1L))
                                .fluidInputs(legacyMaterial.getMolten(1 * INGOTS))
                                .duration(15 * SECONDS)
                                .eut(calculateRecipeEU(legacyMaterial, 8 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                        if (GTOreDictUnificator.get(OrePrefixes.turbineBlade, material, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Turbine_Blade.get(0L))
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.turbineBlade, material, 1L))
                                .fluidInputs(legacyMaterial.getMolten(6 * INGOTS))
                                .duration(20 * SECONDS)
                                .eut(calculateRecipeEU(legacyMaterial, 8 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                        if (GTOreDictUnificator.get(OrePrefixes.pipeTiny, material, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Pipe_Tiny.get(0L))
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.pipeTiny, material, 1L))
                                .fluidInputs(legacyMaterial.getMolten(1 * HALF_INGOTS))
                                .duration(1 * SECONDS)
                                .eut(calculateRecipeEU(legacyMaterial, 8 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                        if (GTOreDictUnificator.get(OrePrefixes.pipeSmall, material, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Pipe_Small.get(0L))
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.pipeSmall, material, 1L))
                                .fluidInputs(legacyMaterial.getMolten(1 * INGOTS))
                                .duration(2 * SECONDS)
                                .eut(calculateRecipeEU(legacyMaterial, 8 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                        if (GTOreDictUnificator.get(OrePrefixes.pipeMedium, material, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Pipe_Medium.get(0L))
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.pipeMedium, material, 1L))
                                .fluidInputs(legacyMaterial.getMolten(3 * INGOTS))
                                .duration(4 * SECONDS)
                                .eut(calculateRecipeEU(legacyMaterial, 8 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                        if (GTOreDictUnificator.get(OrePrefixes.pipeLarge, material, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Pipe_Large.get(0L))
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.pipeLarge, material, 1L))
                                .fluidInputs(legacyMaterial.getMolten(6 * INGOTS))
                                .duration(8 * SECONDS)
                                .eut(calculateRecipeEU(legacyMaterial, 8 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                        if (GTOreDictUnificator.get(OrePrefixes.pipeHuge, material, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Pipe_Huge.get(0L))
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.pipeHuge, material, 1L))
                                .fluidInputs(legacyMaterial.getMolten(1728L))
                                .duration(16 * SECONDS)
                                .eut(calculateRecipeEU(legacyMaterial, 8 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                    }
                }
                if (tAmount * 2 <= 64 && legacyMaterial != Materials.Obsidian) {
                    if (!(legacyMaterial == Materials.Aluminium)) {
                        if (GTOreDictUnificator.get(OrePrefixes.stick, MU.smeltInto(material), 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(1, stack), ItemList.Shape_Extruder_Rod.get(0L))
                                .itemOutputs(
                                    GTOreDictUnificator.get(OrePrefixes.stick, MU.smeltInto(material), tAmount * 2))
                                .duration(((int) Math.max(materialMass * 2L * tAmount, tAmount)) * TICKS)
                                .eut(calculateRecipeEU(legacyMaterial, 6 * tVoltageMultiplier))
                                .addTo(extruderRecipes);
                        }
                    } else {
                        if (GTOreDictUnificator.get(OrePrefixes.stick, MU.smeltInto(material), 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(1, stack), ItemList.Shape_Extruder_Rod.get(0L))
                                .itemOutputs(
                                    GTOreDictUnificator.get(OrePrefixes.stick, MU.smeltInto(material), tAmount * 2))
                                .duration(10 * SECONDS)
                                .eut(calculateRecipeEU(legacyMaterial, 2 * tVoltageMultiplier))
                                .addTo(extruderRecipes);
                        }
                    }
                }
                if (tAmount * 2 <= 64) {
                    if (GTOreDictUnificator.get(OrePrefixes.wireGt01, MU.smeltInto(material), 1L) != null) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, stack), ItemList.Shape_Extruder_Wire.get(0L))
                            .itemOutputs(
                                GTOreDictUnificator.get(OrePrefixes.wireGt01, MU.smeltInto(material), tAmount * 2))
                            .duration(((int) Math.max(materialMass * 2L * tAmount, tAmount)) * TICKS)
                            .eut(calculateRecipeEU(legacyMaterial, 6 * tVoltageMultiplier))
                            .addTo(extruderRecipes);
                    }
                }
                if (tAmount * 8 <= 64) {
                    if (GTOreDictUnificator.get(OrePrefixes.bolt, MU.smeltInto(material), 1L) != null) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, stack), ItemList.Shape_Extruder_Bolt.get(0L))
                            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.bolt, MU.smeltInto(material), tAmount * 8))
                            .duration(((int) Math.max(materialMass * 2L * tAmount, tAmount)) * TICKS)
                            .eut(calculateRecipeEU(legacyMaterial, 8 * tVoltageMultiplier))
                            .addTo(extruderRecipes);
                    }
                }
                if (tAmount * 4 <= 64) {
                    if (GTOreDictUnificator.get(OrePrefixes.ring, MU.smeltInto(material), 1L) != null) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, stack), ItemList.Shape_Extruder_Ring.get(0L))
                            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ring, MU.smeltInto(material), tAmount * 4))
                            .duration(((int) Math.max(materialMass * 2L * tAmount, tAmount)) * TICKS)
                            .eut(calculateRecipeEU(legacyMaterial, 6 * tVoltageMultiplier))
                            .addTo(extruderRecipes);
                    }
                    if (!Boolean.FALSE.equals(material.getProperty(GTMaterialProperties.UNIFIABLE))
                        && (legacyMaterial.mMaterialInto == legacyMaterial)
                        && !MU.hasFlag(material, GTMaterialFlag.NO_SMASHING)) {
                        // If material tier < IV then add manual recipe.
                        Integer ringTierEU = material.getProperty(GTMaterialProperties.PROCESSING_MATERIAL_TIER_EU);
                        if ((ringTierEU == null ? 0 : ringTierEU) < TierEU.IV
                            && GTOreDictUnificator.get(OrePrefixes.ring, material, 1L) != null) {
                            GTModHandler.addCraftingRecipe(
                                GTOreDictUnificator.get(OrePrefixes.ring, material, 1L),
                                GTModHandler.RecipeBits.BITS_STD,
                                new Object[] { "h ", "fX", 'X', MU.craftIngredient(OrePrefixes.stick, material) });
                        }
                    }
                }

                if (GTOreDictUnificator.get(OrePrefixes.toolHeadHammer, MU.smeltInto(material), 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(6, stack), ItemList.Shape_Extruder_Hammer.get(0L))
                        .itemOutputs(
                            GTOreDictUnificator.get(OrePrefixes.toolHeadHammer, MU.smeltInto(material), tAmount))
                        .duration(((int) Math.max(materialMass * 6L * tAmount, tAmount)) * TICKS)
                        .eut(calculateRecipeEU(legacyMaterial, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (GTOreDictUnificator.get(OrePrefixes.toolHeadFile, MU.smeltInto(material), 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(2, stack), ItemList.Shape_Extruder_File.get(0L))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.toolHeadFile, MU.smeltInto(material), tAmount))
                        .duration(((int) Math.max(materialMass * 2L * tAmount, tAmount)) * TICKS)
                        .eut(calculateRecipeEU(legacyMaterial, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (GTOreDictUnificator.get(OrePrefixes.toolHeadSaw, MU.smeltInto(material), 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(2, stack), ItemList.Shape_Extruder_Saw.get(0L))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.toolHeadSaw, MU.smeltInto(material), tAmount))
                        .duration(((int) Math.max(materialMass * 2L * tAmount, tAmount)) * TICKS)
                        .eut(calculateRecipeEU(legacyMaterial, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (GTOreDictUnificator.get(OrePrefixes.gearGt, MU.smeltInto(material), 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(4, stack), ItemList.Shape_Extruder_Gear.get(0L))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gearGt, MU.smeltInto(material), tAmount))
                        .duration(((int) Math.max(materialMass * 5L * tAmount, tAmount)) * TICKS)
                        .eut(calculateRecipeEU(legacyMaterial, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }

                if (!(legacyMaterial == Materials.StyreneButadieneRubber
                    || legacyMaterial == Materials.RubberSilicone)) {
                    Integer plateTierEU = material.getProperty(GTMaterialProperties.PROCESSING_MATERIAL_TIER_EU);
                    if ((plateTierEU == null ? 0 : plateTierEU) < TierEU.IV) {
                        if (GTOreDictUnificator.get(OrePrefixes.plate, MU.smeltInto(material), 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(2, stack), ItemList.Shape_Mold_Plate.get(0L))
                                .itemOutputs(
                                    GTOreDictUnificator.get(OrePrefixes.plate, MU.smeltInto(material), tAmount))
                                .duration(((int) Math.max(materialMass * 2L * tAmount, tAmount)) * TICKS)
                                .eut(calculateRecipeEU(legacyMaterial, 2 * tVoltageMultiplier))
                                .recipeCategory(RecipeCategories.alloySmelterMolding)
                                .addTo(alloySmelterRecipes);
                        }
                    }
                } else {
                    // If tier < IV then add ability to turn ingots into plates via alloy smelter.
                    if (tTrueVoltage < TierEU.IV) {
                        if (GTOreDictUnificator.get(OrePrefixes.plate, MU.smeltInto(material), 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(1, stack), ItemList.Shape_Mold_Plate.get(0L))
                                .itemOutputs(
                                    GTOreDictUnificator.get(OrePrefixes.plate, MU.smeltInto(material), tAmount))
                                .duration(((int) Math.max(materialMass * 2L * tAmount, tAmount)) * TICKS)
                                .eut(calculateRecipeEU(legacyMaterial, 2 * tVoltageMultiplier))
                                .recipeCategory(RecipeCategories.alloySmelterMolding)
                                .addTo(alloySmelterRecipes);
                        }
                    }
                }

                // If tier < IV then add ability to turn ingots into gears via alloy smelter.
                if (tTrueVoltage < TierEU.IV) {
                    if (GTOreDictUnificator.get(OrePrefixes.gearGt, MU.smeltInto(material), 1L) != null) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(8, stack), ItemList.Shape_Mold_Gear.get(0L))
                            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gearGt, MU.smeltInto(material), tAmount))
                            .duration(((int) Math.max(materialMass * 10L * tAmount, tAmount)) * TICKS)
                            .eut(calculateRecipeEU(legacyMaterial, 2 * tVoltageMultiplier))
                            .recipeCategory(RecipeCategories.alloySmelterMolding)
                            .addTo(alloySmelterRecipes);
                    }
                }

                switch (MU.internalName(MU.smeltInto(material))) {
                    case "Glass" -> {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, stack), ItemList.Shape_Extruder_Bottle.get(0L))
                            .itemOutputs(new ItemStack(Items.glass_bottle, 1))
                            .duration((tAmount * 32) * TICKS)
                            .eut(TierEU.RECIPE_LV / 2)
                            .addTo(extruderRecipes);
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, stack), ItemList.Shape_Mold_Bottle.get(0L))
                            .itemOutputs(new ItemStack(Items.glass_bottle, 1))
                            .duration((tAmount * 64) * TICKS)
                            .eut(4)
                            .addTo(alloySmelterRecipes);
                    }
                    case "Steel" -> {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, stack), ItemList.Shape_Extruder_Cell.get(0L))
                            .itemOutputs(ItemList.Cell_Empty.get(tAmount))
                            .duration((tAmount * 128) * TICKS)
                            .eut(TierEU.RECIPE_LV)
                            .addTo(extruderRecipes);
                    }
                    case "Iron", "CastIron" -> {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, stack), ItemList.Shape_Extruder_Cell.get(0L))
                            .itemOutputs(ItemList.IC2_Fuel_Rod_Empty.get(tAmount))
                            .duration((tAmount * 128) * TICKS)
                            .eut(TierEU.RECIPE_LV)
                            .addTo(extruderRecipes);
                        if (tAmount * 31 <= 64) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(31, stack), ItemList.Shape_Mold_Anvil.get(0L))
                                .itemOutputs(new ItemStack(Blocks.anvil, 1, 0))
                                .duration((tAmount * 512) * TICKS)
                                .eut(4 * tVoltageMultiplier)
                                .addTo(alloySmelterRecipes);
                        }
                    }
                    case "Tin" -> {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(2, stack), ItemList.Shape_Extruder_Cell.get(0L))
                            .itemOutputs(ItemList.Cell_Empty.get(tAmount))
                            .duration((tAmount * 128) * TICKS)
                            .eut(TierEU.RECIPE_LV)
                            .addTo(extruderRecipes);
                    }
                    case "Polytetrafluoroethylene" -> {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, stack), ItemList.Shape_Extruder_Cell.get(0L))
                            .itemOutputs(ItemList.Cell_Empty.get(tAmount * 4))
                            .duration((tAmount * 128) * TICKS)
                            .eut(TierEU.RECIPE_LV)
                            .addTo(extruderRecipes);
                    }
                }
            }
        }
    }
}
