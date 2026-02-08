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

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TierEU;
import gregtech.api.recipe.RecipeCategories;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

@SuppressWarnings("RedundantLabeledSwitchRuleCodeBlock")
public class ProcessingShaping implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingShaping() {
        OrePrefixes.ingot.add(this);
        OrePrefixes.dust.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        // Blacklist materials which are handled by Werkstoff loader
        if (aMaterial == Materials.Calcium || aMaterial == Materials.Magnesia) return;

        if (((aMaterial == Materials.Glass) || (GTOreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L) != null))
            && (!aMaterial.contains(SubTag.NO_SMELTING))) {
            long aMaterialMass = aMaterial.getMass();
            int tAmount = (int) (aPrefix.getMaterialAmount() / 3628800L);
            if ((tAmount > 0) && (tAmount <= 64) && (aPrefix.getMaterialAmount() % 3628800L == 0L)) {
                int tVoltageMultiplier = aMaterial.mBlastFurnaceTemp >= 2800 ? 60 : 15;
                int tTrueVoltage = aMaterial.getProcessingMaterialTierEU();

                if (aMaterial.contains(SubTag.NO_SMASHING)) {
                    tVoltageMultiplier /= 4;
                } else if (aPrefix.getName()
                    .startsWith(OrePrefixes.dust.getName())) {
                        return;
                    }

                if (!OrePrefixes.block.isIgnored(aMaterial.mSmeltInto)
                    && (GTOreDictUnificator.get(OrePrefixes.block, aMaterial.mSmeltInto, 1L) != null)
                    && aMaterial != Materials.Ichorium
                    && aMaterial != Materials.Obsidian) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(9, aStack), ItemList.Shape_Extruder_Block.get(0L))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.block, aMaterial.mSmeltInto, tAmount))
                        .duration(aMaterialMass * 9 * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);

                    // Allow creation of alloy smelter recipes for material recycling if < IV tier.
                    if (tTrueVoltage < TierEU.IV) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(9, aStack), ItemList.Shape_Mold_Block.get(0L))
                            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.block, aMaterial.mSmeltInto, tAmount))
                            .duration(aMaterialMass * 9 * TICKS)
                            .eut(calculateRecipeEU(aMaterial, 4 * tVoltageMultiplier))
                            .recipeCategory(RecipeCategories.alloySmelterMolding)
                            .addTo(alloySmelterRecipes);
                    }
                }
                if ((aPrefix != OrePrefixes.ingot || aMaterial != aMaterial.mSmeltInto)
                    && GTOreDictUnificator.get(OrePrefixes.ingot, aMaterial.mSmeltInto, 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, aStack), ItemList.Shape_Extruder_Ingot.get(0L))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ingot, aMaterial.mSmeltInto, tAmount))
                        .duration(10 * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 4 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (GTOreDictUnificator.get(OrePrefixes.pipeTiny, aMaterial.mSmeltInto, 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, aStack), ItemList.Shape_Extruder_Pipe_Tiny.get(0L))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.pipeTiny, aMaterial.mSmeltInto, tAmount * 2))
                        .duration((4 * tAmount) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (GTOreDictUnificator.get(OrePrefixes.pipeSmall, aMaterial.mSmeltInto, 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, aStack), ItemList.Shape_Extruder_Pipe_Small.get(0L))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.pipeSmall, aMaterial.mSmeltInto, tAmount))
                        .duration((8 * tAmount) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (GTOreDictUnificator.get(OrePrefixes.pipeMedium, aMaterial.mSmeltInto, 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(3, aStack), ItemList.Shape_Extruder_Pipe_Medium.get(0L))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.pipeMedium, aMaterial.mSmeltInto, tAmount))
                        .duration((24 * tAmount) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (GTOreDictUnificator.get(OrePrefixes.pipeLarge, aMaterial.mSmeltInto, 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(6, aStack), ItemList.Shape_Extruder_Pipe_Large.get(0L))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.pipeLarge, aMaterial.mSmeltInto, tAmount))
                        .duration((48 * tAmount) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (GTOreDictUnificator.get(OrePrefixes.pipeHuge, aMaterial.mSmeltInto, 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(12, aStack), ItemList.Shape_Extruder_Pipe_Huge.get(0L))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.pipeHuge, aMaterial.mSmeltInto, tAmount))
                        .duration((96 * tAmount) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (GTOreDictUnificator.get(OrePrefixes.plate, aMaterial.mSmeltInto, 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, aStack), ItemList.Shape_Extruder_Plate.get(0L))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial.mSmeltInto, tAmount))
                        .duration(((int) Math.max(aMaterialMass * tAmount, tAmount)) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (GTOreDictUnificator.get(OrePrefixes.gearGtSmall, aMaterial.mSmeltInto, 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, aStack), ItemList.Shape_Extruder_Small_Gear.get(0L))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gearGtSmall, aMaterial.mSmeltInto, tAmount))
                        .duration(((int) Math.max(aMaterialMass * tAmount, tAmount)) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (GTOreDictUnificator.get(OrePrefixes.turbineBlade, aMaterial.mSmeltInto, 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(6, aStack), ItemList.Shape_Extruder_Turbine_Blade.get(0L))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.turbineBlade, aMaterial.mSmeltInto, tAmount))
                        .duration(((int) Math.max(aMaterialMass * tAmount, tAmount)) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }

                if (!(aMaterial == Materials.AnnealedCopper || aMaterial == Materials.WroughtIron)
                    && !(aMaterial.contains(SubTag.NO_SMELTING))
                    && aPrefix == OrePrefixes.ingot) {
                    if (aMaterial.mStandardMoltenFluid != null) {
                        if (GTOreDictUnificator.get(OrePrefixes.ring, aMaterial, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Ring.get(0L))
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ring, aMaterial, 1L))
                                .fluidInputs(aMaterial.getMolten(1 * QUARTER_INGOTS))
                                .duration(5 * SECONDS)
                                .eut(calculateRecipeEU(aMaterial, 4 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                        if (GTOreDictUnificator.get(OrePrefixes.screw, aMaterial, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Screw.get(0L))
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.screw, aMaterial, 1L))
                                .fluidInputs(aMaterial.getMolten(1 * EIGHTH_INGOTS))
                                .duration(2 * SECONDS + 10 * TICKS)
                                .eut(calculateRecipeEU(aMaterial, 2 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                        if (GTOreDictUnificator.get(OrePrefixes.stick, aMaterial, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Rod.get(0L))
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.stick, aMaterial, 1L))
                                .fluidInputs(aMaterial.getMolten(1 * HALF_INGOTS))
                                .duration(7 * SECONDS + 10 * TICKS)
                                .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                        if (GTOreDictUnificator.get(OrePrefixes.bolt, aMaterial, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Bolt.get(0L))
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.bolt, aMaterial, 1L))
                                .fluidInputs(aMaterial.getMolten(1 * EIGHTH_INGOTS))
                                .duration(2 * SECONDS + 10 * TICKS)
                                .eut(calculateRecipeEU(aMaterial, 2 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                        if (GTOreDictUnificator.get(OrePrefixes.round, aMaterial, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Round.get(0L))
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.round, aMaterial, 1L))
                                .fluidInputs(aMaterial.getMolten(1 * NUGGETS))
                                .duration(2 * SECONDS + 10 * TICKS)
                                .eut(calculateRecipeEU(aMaterial, 2 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                        if (GTOreDictUnificator.get(OrePrefixes.stickLong, aMaterial, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Rod_Long.get(0L))
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.stickLong, aMaterial, 1L))
                                .fluidInputs(aMaterial.getMolten(1 * INGOTS))
                                .duration(15 * SECONDS)
                                .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                        if (GTOreDictUnificator.get(OrePrefixes.turbineBlade, aMaterial, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Turbine_Blade.get(0L))
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.turbineBlade, aMaterial, 1L))
                                .fluidInputs(aMaterial.getMolten(6 * INGOTS))
                                .duration(20 * SECONDS)
                                .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                        if (GTOreDictUnificator.get(OrePrefixes.pipeTiny, aMaterial, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Pipe_Tiny.get(0L))
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.pipeTiny, aMaterial, 1L))
                                .fluidInputs(aMaterial.getMolten(1 * HALF_INGOTS))
                                .duration(1 * SECONDS)
                                .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                        if (GTOreDictUnificator.get(OrePrefixes.pipeSmall, aMaterial, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Pipe_Small.get(0L))
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.pipeSmall, aMaterial, 1L))
                                .fluidInputs(aMaterial.getMolten(1 * INGOTS))
                                .duration(2 * SECONDS)
                                .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                        if (GTOreDictUnificator.get(OrePrefixes.pipeMedium, aMaterial, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Pipe_Medium.get(0L))
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.pipeMedium, aMaterial, 1L))
                                .fluidInputs(aMaterial.getMolten(3 * INGOTS))
                                .duration(4 * SECONDS)
                                .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                        if (GTOreDictUnificator.get(OrePrefixes.pipeLarge, aMaterial, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Pipe_Large.get(0L))
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.pipeLarge, aMaterial, 1L))
                                .fluidInputs(aMaterial.getMolten(6 * INGOTS))
                                .duration(8 * SECONDS)
                                .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                        if (GTOreDictUnificator.get(OrePrefixes.pipeHuge, aMaterial, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Pipe_Huge.get(0L))
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.pipeHuge, aMaterial, 1L))
                                .fluidInputs(aMaterial.getMolten(1728L))
                                .duration(16 * SECONDS)
                                .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                    }
                }
                if (tAmount * 2 <= 64 && aMaterial != Materials.Obsidian) {
                    if (!(aMaterial == Materials.Aluminium)) {
                        if (GTOreDictUnificator.get(OrePrefixes.stick, aMaterial.mSmeltInto, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(1, aStack), ItemList.Shape_Extruder_Rod.get(0L))
                                .itemOutputs(
                                    GTOreDictUnificator.get(OrePrefixes.stick, aMaterial.mSmeltInto, tAmount * 2))
                                .duration(((int) Math.max(aMaterialMass * 2L * tAmount, tAmount)) * TICKS)
                                .eut(calculateRecipeEU(aMaterial, 6 * tVoltageMultiplier))
                                .addTo(extruderRecipes);
                        }
                    } else {
                        if (GTOreDictUnificator.get(OrePrefixes.stick, aMaterial.mSmeltInto, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(1, aStack), ItemList.Shape_Extruder_Rod.get(0L))
                                .itemOutputs(
                                    GTOreDictUnificator.get(OrePrefixes.stick, aMaterial.mSmeltInto, tAmount * 2))
                                .duration(10 * SECONDS)
                                .eut(calculateRecipeEU(aMaterial, 2 * tVoltageMultiplier))
                                .addTo(extruderRecipes);
                        }
                    }
                }
                if (tAmount * 2 <= 64) {
                    if (GTOreDictUnificator.get(OrePrefixes.wireGt01, aMaterial.mSmeltInto, 1L) != null) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, aStack), ItemList.Shape_Extruder_Wire.get(0L))
                            .itemOutputs(
                                GTOreDictUnificator.get(OrePrefixes.wireGt01, aMaterial.mSmeltInto, tAmount * 2))
                            .duration(((int) Math.max(aMaterialMass * 2L * tAmount, tAmount)) * TICKS)
                            .eut(calculateRecipeEU(aMaterial, 6 * tVoltageMultiplier))
                            .addTo(extruderRecipes);
                    }
                }
                if (tAmount * 8 <= 64) {
                    if (GTOreDictUnificator.get(OrePrefixes.bolt, aMaterial.mSmeltInto, 1L) != null) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, aStack), ItemList.Shape_Extruder_Bolt.get(0L))
                            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.bolt, aMaterial.mSmeltInto, tAmount * 8))
                            .duration(((int) Math.max(aMaterialMass * 2L * tAmount, tAmount)) * TICKS)
                            .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                            .addTo(extruderRecipes);
                    }
                }
                if (tAmount * 4 <= 64) {
                    if (GTOreDictUnificator.get(OrePrefixes.ring, aMaterial.mSmeltInto, 1L) != null) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, aStack), ItemList.Shape_Extruder_Ring.get(0L))
                            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ring, aMaterial.mSmeltInto, tAmount * 4))
                            .duration(((int) Math.max(aMaterialMass * 2L * tAmount, tAmount)) * TICKS)
                            .eut(calculateRecipeEU(aMaterial, 6 * tVoltageMultiplier))
                            .addTo(extruderRecipes);
                    }
                    if ((aMaterial.mUnifiable) && (aMaterial.mMaterialInto == aMaterial)
                        && !aMaterial.contains(SubTag.NO_SMASHING)) {
                        // If material tier < IV then add manual recipe.
                        if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV
                            && GTOreDictUnificator.get(OrePrefixes.ring, aMaterial, 1L) != null) {
                            GTModHandler.addCraftingRecipe(
                                GTOreDictUnificator.get(OrePrefixes.ring, aMaterial, 1L),
                                GTModHandler.RecipeBits.BITS_STD,
                                new Object[] { "h ", "fX", 'X', OrePrefixes.stick.get(aMaterial) });
                        }
                    }
                }

                if (GTOreDictUnificator.get(OrePrefixes.toolHeadHammer, aMaterial.mSmeltInto, 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(6, aStack), ItemList.Shape_Extruder_Hammer.get(0L))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.toolHeadHammer, aMaterial.mSmeltInto, tAmount))
                        .duration(((int) Math.max(aMaterialMass * 6L * tAmount, tAmount)) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (GTOreDictUnificator.get(OrePrefixes.toolHeadFile, aMaterial.mSmeltInto, 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(2, aStack), ItemList.Shape_Extruder_File.get(0L))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.toolHeadFile, aMaterial.mSmeltInto, tAmount))
                        .duration(((int) Math.max(aMaterialMass * 2L * tAmount, tAmount)) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (GTOreDictUnificator.get(OrePrefixes.toolHeadSaw, aMaterial.mSmeltInto, 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(2, aStack), ItemList.Shape_Extruder_Saw.get(0L))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.toolHeadSaw, aMaterial.mSmeltInto, tAmount))
                        .duration(((int) Math.max(aMaterialMass * 2L * tAmount, tAmount)) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (GTOreDictUnificator.get(OrePrefixes.gearGt, aMaterial.mSmeltInto, 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(4, aStack), ItemList.Shape_Extruder_Gear.get(0L))
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gearGt, aMaterial.mSmeltInto, tAmount))
                        .duration(((int) Math.max(aMaterialMass * 5L * tAmount, tAmount)) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }

                if (!(aMaterial == Materials.StyreneButadieneRubber || aMaterial == Materials.RubberSilicone)) {
                    if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                        if (GTOreDictUnificator.get(OrePrefixes.plate, aMaterial.mSmeltInto, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(2, aStack), ItemList.Shape_Mold_Plate.get(0L))
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial.mSmeltInto, tAmount))
                                .duration(((int) Math.max(aMaterialMass * 2L * tAmount, tAmount)) * TICKS)
                                .eut(calculateRecipeEU(aMaterial, 2 * tVoltageMultiplier))
                                .recipeCategory(RecipeCategories.alloySmelterMolding)
                                .addTo(alloySmelterRecipes);
                        }
                    }
                } else {
                    // If tier < IV then add ability to turn ingots into plates via alloy smelter.
                    if (tTrueVoltage < TierEU.IV) {
                        if (GTOreDictUnificator.get(OrePrefixes.plate, aMaterial.mSmeltInto, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(1, aStack), ItemList.Shape_Mold_Plate.get(0L))
                                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial.mSmeltInto, tAmount))
                                .duration(((int) Math.max(aMaterialMass * 2L * tAmount, tAmount)) * TICKS)
                                .eut(calculateRecipeEU(aMaterial, 2 * tVoltageMultiplier))
                                .recipeCategory(RecipeCategories.alloySmelterMolding)
                                .addTo(alloySmelterRecipes);
                        }
                    }
                }

                // If tier < IV then add ability to turn ingots into gears via alloy smelter.
                if (tTrueVoltage < TierEU.IV) {
                    if (GTOreDictUnificator.get(OrePrefixes.gearGt, aMaterial.mSmeltInto, 1L) != null) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(8, aStack), ItemList.Shape_Mold_Gear.get(0L))
                            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gearGt, aMaterial.mSmeltInto, tAmount))
                            .duration(((int) Math.max(aMaterialMass * 10L * tAmount, tAmount)) * TICKS)
                            .eut(calculateRecipeEU(aMaterial, 2 * tVoltageMultiplier))
                            .recipeCategory(RecipeCategories.alloySmelterMolding)
                            .addTo(alloySmelterRecipes);
                    }
                }

                switch (aMaterial.mSmeltInto.mName) {
                    case "Glass" -> {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, aStack), ItemList.Shape_Extruder_Bottle.get(0L))
                            .itemOutputs(new ItemStack(Items.glass_bottle, 1))
                            .duration((tAmount * 32) * TICKS)
                            .eut(TierEU.RECIPE_LV / 2)
                            .addTo(extruderRecipes);
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, aStack), ItemList.Shape_Mold_Bottle.get(0L))
                            .itemOutputs(new ItemStack(Items.glass_bottle, 1))
                            .duration((tAmount * 64) * TICKS)
                            .eut(4)
                            .addTo(alloySmelterRecipes);
                    }
                    case "Steel" -> {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, aStack), ItemList.Shape_Extruder_Cell.get(0L))
                            .itemOutputs(ItemList.Cell_Empty.get(tAmount))
                            .duration((tAmount * 128) * TICKS)
                            .eut(TierEU.RECIPE_LV)
                            .addTo(extruderRecipes);
                    }
                    case "Iron", "WroughtIron" -> {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, aStack), ItemList.Shape_Extruder_Cell.get(0L))
                            .itemOutputs(ItemList.IC2_Fuel_Rod_Empty.get(tAmount))
                            .duration((tAmount * 128) * TICKS)
                            .eut(TierEU.RECIPE_LV)
                            .addTo(extruderRecipes);
                        if (tAmount * 31 <= 64) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(31, aStack), ItemList.Shape_Mold_Anvil.get(0L))
                                .itemOutputs(new ItemStack(Blocks.anvil, 1, 0))
                                .duration((tAmount * 512) * TICKS)
                                .eut(4 * tVoltageMultiplier)
                                .addTo(alloySmelterRecipes);
                        }
                    }
                    case "Tin" -> {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(2, aStack), ItemList.Shape_Extruder_Cell.get(0L))
                            .itemOutputs(ItemList.Cell_Empty.get(tAmount))
                            .duration((tAmount * 128) * TICKS)
                            .eut(TierEU.RECIPE_LV)
                            .addTo(extruderRecipes);
                    }
                    case "Polytetrafluoroethylene" -> {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, aStack), ItemList.Shape_Extruder_Cell.get(0L))
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
