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
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.BlockShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.enums.materials.TEBlockShapes;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
import gregtech.api.recipe.RecipeCategories;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;

@SuppressWarnings("RedundantLabeledSwitchRuleCodeBlock")
public class ProcessingShaping implements PartRecipeGenerator {

    public static final ProcessingShaping INSTANCE = new ProcessingShaping();

    private ProcessingShaping() {}

    /// Whether this pass shapes `material`'s parts from its ingots, or from its dust for Glass.
    static boolean shapesFromIngots(Material material) {
        return (material == Materials.Glass || MaterialLibAPI.findStack(material, Shapes.ingot, 1) != null)
            && !MaterialUtils.hasFlag(material, GTMaterialFlag.NO_SMELTING);
    }

    @Override
    public void generate(OrePrefixes prefix, Material material, String oreDictName, ItemStack stack) {
        if (shapesFromIngots(material)) {
            long materialMass = MaterialUtils.mass(material);
            int tAmount = (int) (prefix.getMaterialAmount() / 3628800L);
            if ((tAmount > 0) && (tAmount <= 64) && (prefix.getMaterialAmount() % 3628800L == 0L)) {
                int tVoltageMultiplier = MaterialUtils.blastFurnaceTemp(material) >= 2800 ? 60 : 15;
                int tTrueVoltage = MaterialUtils.processingMaterialTierEU(material);

                if (MaterialUtils.hasFlag(material, GTMaterialFlag.NO_SMASHING)) {
                    tVoltageMultiplier /= 4;
                } else if (prefix.getName()
                    .startsWith(OrePrefixes.dust.getName())) {
                        return;
                    }

                if (!OrePrefixes.block.isIgnored(MaterialUtils.smeltInto(material))
                    && (MaterialLibAPI.findStack(MaterialUtils.smeltInto(material), BlockShapes.block, 1) != null)
                    && material != Materials.Ichorium
                    && material != Materials.Obsidian) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(9, stack), ItemList.Shape_Extruder_Block.get(0L))
                        .itemOutputs(
                            MaterialLibAPI.findStack(MaterialUtils.smeltInto(material), BlockShapes.block, tAmount))
                        .duration(materialMass * 9 * TICKS)
                        .eut(calculateRecipeEU(material, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);

                    // Allow creation of alloy smelter recipes for material recycling if < IV tier.
                    if (tTrueVoltage < TierEU.IV) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(9, stack), ItemList.Shape_Mold_Block.get(0L))
                            .itemOutputs(
                                MaterialLibAPI.findStack(MaterialUtils.smeltInto(material), BlockShapes.block, tAmount))
                            .duration(materialMass * 9 * TICKS)
                            .eut(calculateRecipeEU(material, 4 * tVoltageMultiplier))
                            .recipeCategory(RecipeCategories.alloySmelterMolding)
                            .addTo(alloySmelterRecipes);
                    }
                }
                if ((prefix != OrePrefixes.ingot || material != MaterialUtils.smeltInto(material))
                    && MaterialLibAPI.findStack(MaterialUtils.smeltInto(material), Shapes.ingot, 1) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack), ItemList.Shape_Extruder_Ingot.get(0L))
                        .itemOutputs(
                            GTUtility.copyAmount(
                                tAmount,
                                MaterialLibAPI.findStack(MaterialUtils.smeltInto(material), Shapes.ingot, 1)))
                        .duration(10 * TICKS)
                        .eut(calculateRecipeEU(material, 4 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (MaterialParts.stack(OrePrefixes.pipeTiny, MaterialUtils.smeltInto(material), 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack), ItemList.Shape_Extruder_Pipe_Tiny.get(0L))
                        .itemOutputs(
                            GTUtility.copyAmount(
                                tAmount * 2,
                                MaterialParts.stack(OrePrefixes.pipeTiny, MaterialUtils.smeltInto(material), 1)))
                        .duration((4 * tAmount) * TICKS)
                        .eut(calculateRecipeEU(material, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (MaterialParts.stack(OrePrefixes.pipeSmall, MaterialUtils.smeltInto(material), 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack), ItemList.Shape_Extruder_Pipe_Small.get(0L))
                        .itemOutputs(
                            GTUtility.copyAmount(
                                tAmount,
                                MaterialParts.stack(OrePrefixes.pipeSmall, MaterialUtils.smeltInto(material), 1)))
                        .duration((8 * tAmount) * TICKS)
                        .eut(calculateRecipeEU(material, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (MaterialParts.stack(OrePrefixes.pipeMedium, MaterialUtils.smeltInto(material), 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(3, stack), ItemList.Shape_Extruder_Pipe_Medium.get(0L))
                        .itemOutputs(
                            GTUtility.copyAmount(
                                tAmount,
                                MaterialParts.stack(OrePrefixes.pipeMedium, MaterialUtils.smeltInto(material), 1)))
                        .duration((24 * tAmount) * TICKS)
                        .eut(calculateRecipeEU(material, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (MaterialParts.stack(OrePrefixes.pipeLarge, MaterialUtils.smeltInto(material), 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(6, stack), ItemList.Shape_Extruder_Pipe_Large.get(0L))
                        .itemOutputs(
                            GTUtility.copyAmount(
                                tAmount,
                                MaterialParts.stack(OrePrefixes.pipeLarge, MaterialUtils.smeltInto(material), 1)))
                        .duration((48 * tAmount) * TICKS)
                        .eut(calculateRecipeEU(material, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (MaterialParts.stack(OrePrefixes.pipeHuge, MaterialUtils.smeltInto(material), 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(12, stack), ItemList.Shape_Extruder_Pipe_Huge.get(0L))
                        .itemOutputs(
                            GTUtility.copyAmount(
                                tAmount,
                                MaterialParts.stack(OrePrefixes.pipeHuge, MaterialUtils.smeltInto(material), 1)))
                        .duration((96 * tAmount) * TICKS)
                        .eut(calculateRecipeEU(material, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (MaterialLibAPI.findStack(MaterialUtils.smeltInto(material), Shapes.plate, 1) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack), ItemList.Shape_Extruder_Plate.get(0L))
                        .itemOutputs(
                            GTUtility.copyAmount(
                                tAmount,
                                MaterialLibAPI.findStack(MaterialUtils.smeltInto(material), Shapes.plate, 1)))
                        .duration(((int) Math.max(materialMass * tAmount, tAmount)) * TICKS)
                        .eut(calculateRecipeEU(material, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (MaterialLibAPI.findStack(MaterialUtils.smeltInto(material), Shapes.gearGtSmall, 1) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack), ItemList.Shape_Extruder_Small_Gear.get(0L))
                        .itemOutputs(
                            GTUtility.copyAmount(
                                tAmount,
                                MaterialLibAPI.findStack(MaterialUtils.smeltInto(material), Shapes.gearGtSmall, 1)))
                        .duration(((int) Math.max(materialMass * tAmount, tAmount)) * TICKS)
                        .eut(calculateRecipeEU(material, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (MaterialLibAPI.findStack(MaterialUtils.smeltInto(material), Shapes.turbineBlade, 1) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(6, stack), ItemList.Shape_Extruder_Turbine_Blade.get(0L))
                        .itemOutputs(
                            GTUtility.copyAmount(
                                tAmount,
                                MaterialLibAPI.findStack(MaterialUtils.smeltInto(material), Shapes.turbineBlade, 1)))
                        .duration(((int) Math.max(materialMass * tAmount, tAmount)) * TICKS)
                        .eut(calculateRecipeEU(material, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }

                if (!(material == Materials.AnnealedCopper || material == Materials.CastIron)
                    && !(MaterialUtils.hasFlag(material, GTMaterialFlag.NO_SMELTING))
                    && prefix == OrePrefixes.ingot) {
                    if (MaterialUtils.hasMolten(material)) {
                        if (MaterialLibAPI.findStack(material, Shapes.ring, 1) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Ring.get(0L))
                                .itemOutputs(MaterialLibAPI.findStack(material, Shapes.ring, 1))
                                .fluidInputs(MaterialUtils.molten(material, 1 * QUARTER_INGOTS))
                                .duration(5 * SECONDS)
                                .eut(calculateRecipeEU(material, 4 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                        if (MaterialLibAPI.findStack(material, Shapes.screw, 1) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Screw.get(0L))
                                .itemOutputs(MaterialLibAPI.findStack(material, Shapes.screw, 1))
                                .fluidInputs(MaterialUtils.molten(material, 1 * EIGHTH_INGOTS))
                                .duration(2 * SECONDS + 10 * TICKS)
                                .eut(calculateRecipeEU(material, 2 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                        if (MaterialLibAPI.findStack(material, Shapes.stick, 1) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Rod.get(0L))
                                .itemOutputs(MaterialLibAPI.findStack(material, Shapes.stick, 1))
                                .fluidInputs(MaterialUtils.molten(material, 1 * HALF_INGOTS))
                                .duration(7 * SECONDS + 10 * TICKS)
                                .eut(calculateRecipeEU(material, 8 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                        if (MaterialLibAPI.findStack(material, Shapes.bolt, 1) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Bolt.get(0L))
                                .itemOutputs(MaterialLibAPI.findStack(material, Shapes.bolt, 1))
                                .fluidInputs(MaterialUtils.molten(material, 1 * EIGHTH_INGOTS))
                                .duration(2 * SECONDS + 10 * TICKS)
                                .eut(calculateRecipeEU(material, 2 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                        if (MaterialLibAPI.findStack(material, Shapes.round, 1) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Round.get(0L))
                                .itemOutputs(MaterialLibAPI.findStack(material, Shapes.round, 1))
                                .fluidInputs(MaterialUtils.molten(material, 1 * NUGGETS))
                                .duration(2 * SECONDS + 10 * TICKS)
                                .eut(calculateRecipeEU(material, 2 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                        if (MaterialLibAPI.findStack(material, Shapes.stickLong, 1) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Rod_Long.get(0L))
                                .itemOutputs(MaterialLibAPI.findStack(material, Shapes.stickLong, 1))
                                .fluidInputs(MaterialUtils.molten(material, 1 * INGOTS))
                                .duration(15 * SECONDS)
                                .eut(calculateRecipeEU(material, 8 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                        if (MaterialLibAPI.findStack(material, Shapes.turbineBlade, 1) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Turbine_Blade.get(0L))
                                .itemOutputs(MaterialLibAPI.findStack(material, Shapes.turbineBlade, 1))
                                .fluidInputs(MaterialUtils.molten(material, 6 * INGOTS))
                                .duration(20 * SECONDS)
                                .eut(calculateRecipeEU(material, 8 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                        if (MaterialParts.stack(OrePrefixes.pipeTiny, material, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Pipe_Tiny.get(0L))
                                .itemOutputs(MaterialParts.stack(OrePrefixes.pipeTiny, material, 1L))
                                .fluidInputs(MaterialUtils.molten(material, 1 * HALF_INGOTS))
                                .duration(1 * SECONDS)
                                .eut(calculateRecipeEU(material, 8 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                        if (MaterialParts.stack(OrePrefixes.pipeSmall, material, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Pipe_Small.get(0L))
                                .itemOutputs(MaterialParts.stack(OrePrefixes.pipeSmall, material, 1L))
                                .fluidInputs(MaterialUtils.molten(material, 1 * INGOTS))
                                .duration(2 * SECONDS)
                                .eut(calculateRecipeEU(material, 8 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                        if (MaterialParts.stack(OrePrefixes.pipeMedium, material, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Pipe_Medium.get(0L))
                                .itemOutputs(MaterialParts.stack(OrePrefixes.pipeMedium, material, 1L))
                                .fluidInputs(MaterialUtils.molten(material, 3 * INGOTS))
                                .duration(4 * SECONDS)
                                .eut(calculateRecipeEU(material, 8 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                        if (MaterialParts.stack(OrePrefixes.pipeLarge, material, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Pipe_Large.get(0L))
                                .itemOutputs(MaterialParts.stack(OrePrefixes.pipeLarge, material, 1L))
                                .fluidInputs(MaterialUtils.molten(material, 6 * INGOTS))
                                .duration(8 * SECONDS)
                                .eut(calculateRecipeEU(material, 8 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                        if (MaterialParts.stack(OrePrefixes.pipeHuge, material, 1L) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Pipe_Huge.get(0L))
                                .itemOutputs(MaterialParts.stack(OrePrefixes.pipeHuge, material, 1L))
                                .fluidInputs(MaterialUtils.molten(material, 1728L))
                                .duration(16 * SECONDS)
                                .eut(calculateRecipeEU(material, 8 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                    }
                }
                if (tAmount * 2 <= 64 && material != Materials.Obsidian) {
                    if (!(material == Materials.Aluminium)) {
                        if (MaterialLibAPI.findStack(MaterialUtils.smeltInto(material), Shapes.stick, 1) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(1, stack), ItemList.Shape_Extruder_Rod.get(0L))
                                .itemOutputs(
                                    GTUtility.copyAmount(
                                        tAmount * 2,
                                        MaterialLibAPI.findStack(MaterialUtils.smeltInto(material), Shapes.stick, 1)))
                                .duration(((int) Math.max(materialMass * 2L * tAmount, tAmount)) * TICKS)
                                .eut(calculateRecipeEU(material, 6 * tVoltageMultiplier))
                                .addTo(extruderRecipes);
                        }
                    } else {
                        if (MaterialLibAPI.findStack(MaterialUtils.smeltInto(material), Shapes.stick, 1) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(1, stack), ItemList.Shape_Extruder_Rod.get(0L))
                                .itemOutputs(
                                    GTUtility.copyAmount(
                                        tAmount * 2,
                                        MaterialLibAPI.findStack(MaterialUtils.smeltInto(material), Shapes.stick, 1)))
                                .duration(10 * SECONDS)
                                .eut(calculateRecipeEU(material, 2 * tVoltageMultiplier))
                                .addTo(extruderRecipes);
                        }
                    }
                }
                if (tAmount * 2 <= 64) {
                    if (MaterialLibAPI.findStack(MaterialUtils.smeltInto(material), TEBlockShapes.wireGt01, 1)
                        != null) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, stack), ItemList.Shape_Extruder_Wire.get(0L))
                            .itemOutputs(
                                GTUtility.copyAmount(
                                    tAmount * 2,
                                    MaterialLibAPI
                                        .findStack(MaterialUtils.smeltInto(material), TEBlockShapes.wireGt01, 1)))
                            .duration(((int) Math.max(materialMass * 2L * tAmount, tAmount)) * TICKS)
                            .eut(calculateRecipeEU(material, 6 * tVoltageMultiplier))
                            .addTo(extruderRecipes);
                    }
                }
                if (tAmount * 8 <= 64) {
                    if (MaterialLibAPI.findStack(MaterialUtils.smeltInto(material), Shapes.bolt, 1) != null) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, stack), ItemList.Shape_Extruder_Bolt.get(0L))
                            .itemOutputs(
                                GTUtility.copyAmount(
                                    tAmount * 8,
                                    MaterialLibAPI.findStack(MaterialUtils.smeltInto(material), Shapes.bolt, 1)))
                            .duration(((int) Math.max(materialMass * 2L * tAmount, tAmount)) * TICKS)
                            .eut(calculateRecipeEU(material, 8 * tVoltageMultiplier))
                            .addTo(extruderRecipes);
                    }
                }
                if (tAmount * 4 <= 64) {
                    if (MaterialLibAPI.findStack(MaterialUtils.smeltInto(material), Shapes.ring, 1) != null) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, stack), ItemList.Shape_Extruder_Ring.get(0L))
                            .itemOutputs(
                                GTUtility.copyAmount(
                                    tAmount * 4,
                                    MaterialLibAPI.findStack(MaterialUtils.smeltInto(material), Shapes.ring, 1)))
                            .duration(((int) Math.max(materialMass * 2L * tAmount, tAmount)) * TICKS)
                            .eut(calculateRecipeEU(material, 6 * tVoltageMultiplier))
                            .addTo(extruderRecipes);
                    }
                    if (MaterialUtils.unifiable(material)
                        && !MaterialUtils.hasFlag(material, GTMaterialFlag.NO_SMASHING)) {
                        // If material tier < IV then add manual recipe.
                        if (MaterialUtils.processingMaterialTierEU(material) < TierEU.IV
                            && MaterialLibAPI.findStack(material, Shapes.ring, 1) != null) {
                            GTModHandler.addCraftingRecipe(
                                MaterialLibAPI.findStack(material, Shapes.ring, 1),
                                GTModHandler.RecipeBits.BITS_STD,
                                new Object[] { "h ", "fX", 'X', OrePrefixes.stick.ingredient(material) });
                        }
                    }
                }

                if (MaterialLibAPI.findStack(MaterialUtils.smeltInto(material), Shapes.toolHeadHammer, 1) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(6, stack), ItemList.Shape_Extruder_Hammer.get(0L))
                        .itemOutputs(
                            GTUtility.copyAmount(
                                tAmount,
                                MaterialLibAPI.findStack(MaterialUtils.smeltInto(material), Shapes.toolHeadHammer, 1)))
                        .duration(((int) Math.max(materialMass * 6L * tAmount, tAmount)) * TICKS)
                        .eut(calculateRecipeEU(material, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (MaterialLibAPI.findStack(MaterialUtils.smeltInto(material), Shapes.toolHeadFile, 1) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(2, stack), ItemList.Shape_Extruder_File.get(0L))
                        .itemOutputs(
                            GTUtility.copyAmount(
                                tAmount,
                                MaterialLibAPI.findStack(MaterialUtils.smeltInto(material), Shapes.toolHeadFile, 1)))
                        .duration(((int) Math.max(materialMass * 2L * tAmount, tAmount)) * TICKS)
                        .eut(calculateRecipeEU(material, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (MaterialLibAPI.findStack(MaterialUtils.smeltInto(material), Shapes.toolHeadSaw, 1) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(2, stack), ItemList.Shape_Extruder_Saw.get(0L))
                        .itemOutputs(
                            GTUtility.copyAmount(
                                tAmount,
                                MaterialLibAPI.findStack(MaterialUtils.smeltInto(material), Shapes.toolHeadSaw, 1)))
                        .duration(((int) Math.max(materialMass * 2L * tAmount, tAmount)) * TICKS)
                        .eut(calculateRecipeEU(material, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (MaterialLibAPI.findStack(MaterialUtils.smeltInto(material), Shapes.gearGt, 1) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(4, stack), ItemList.Shape_Extruder_Gear.get(0L))
                        .itemOutputs(
                            GTUtility.copyAmount(
                                tAmount,
                                MaterialLibAPI.findStack(MaterialUtils.smeltInto(material), Shapes.gearGt, 1)))
                        .duration(((int) Math.max(materialMass * 5L * tAmount, tAmount)) * TICKS)
                        .eut(calculateRecipeEU(material, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }

                if (!(material == Materials.StyreneButadieneRubber || material == Materials.Silicone)) {
                    if (MaterialUtils.processingMaterialTierEU(material) < TierEU.IV) {
                        if (MaterialLibAPI.findStack(MaterialUtils.smeltInto(material), Shapes.plate, 1) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(2, stack), ItemList.Shape_Mold_Plate.get(0L))
                                .itemOutputs(
                                    GTUtility.copyAmount(
                                        tAmount,
                                        MaterialLibAPI.findStack(MaterialUtils.smeltInto(material), Shapes.plate, 1)))
                                .duration(((int) Math.max(materialMass * 2L * tAmount, tAmount)) * TICKS)
                                .eut(calculateRecipeEU(material, 2 * tVoltageMultiplier))
                                .recipeCategory(RecipeCategories.alloySmelterMolding)
                                .addTo(alloySmelterRecipes);
                        }
                    }
                } else {
                    // If tier < IV then add ability to turn ingots into plates via alloy smelter.
                    if (tTrueVoltage < TierEU.IV) {
                        if (MaterialLibAPI.findStack(MaterialUtils.smeltInto(material), Shapes.plate, 1) != null) {
                            GTValues.RA.stdBuilder()
                                .itemInputs(GTUtility.copyAmount(1, stack), ItemList.Shape_Mold_Plate.get(0L))
                                .itemOutputs(
                                    GTUtility.copyAmount(
                                        tAmount,
                                        MaterialLibAPI.findStack(MaterialUtils.smeltInto(material), Shapes.plate, 1)))
                                .duration(((int) Math.max(materialMass * 2L * tAmount, tAmount)) * TICKS)
                                .eut(calculateRecipeEU(material, 2 * tVoltageMultiplier))
                                .recipeCategory(RecipeCategories.alloySmelterMolding)
                                .addTo(alloySmelterRecipes);
                        }
                    }
                }

                // If tier < IV then add ability to turn ingots into gears via alloy smelter.
                if (tTrueVoltage < TierEU.IV) {
                    if (MaterialLibAPI.findStack(MaterialUtils.smeltInto(material), Shapes.gearGt, 1) != null) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(8, stack), ItemList.Shape_Mold_Gear.get(0L))
                            .itemOutputs(
                                GTUtility.copyAmount(
                                    tAmount,
                                    MaterialLibAPI.findStack(MaterialUtils.smeltInto(material), Shapes.gearGt, 1)))
                            .duration(((int) Math.max(materialMass * 10L * tAmount, tAmount)) * TICKS)
                            .eut(calculateRecipeEU(material, 2 * tVoltageMultiplier))
                            .recipeCategory(RecipeCategories.alloySmelterMolding)
                            .addTo(alloySmelterRecipes);
                    }
                }

                switch (MaterialUtils.internalName(MaterialUtils.smeltInto(material))) {
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
