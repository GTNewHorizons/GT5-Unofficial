package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.alloySmelterRecipes;
import static gregtech.api.recipe.RecipeMaps.extruderRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;
import static gregtech.api.util.GT_Utility.calculateRecipeEU;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TierEU;
import gregtech.api.recipe.RecipeCategories;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Proxy;

@SuppressWarnings("RedundantLabeledSwitchRuleCodeBlock")
public class ProcessingShaping implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingShaping() {
        OrePrefixes.ingot.add(this);
        OrePrefixes.dust.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        if (((aMaterial == Materials.Glass) || (GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L) != null))
            && (!aMaterial.contains(SubTag.NO_SMELTING))) {
            long aMaterialMass = aMaterial.getMass();
            int tAmount = (int) (aPrefix.mMaterialAmount / 3628800L);
            if ((tAmount > 0) && (tAmount <= 64) && (aPrefix.mMaterialAmount % 3628800L == 0L)) {
                int tVoltageMultiplier = aMaterial.mBlastFurnaceTemp >= 2800 ? 60 : 15;
                int tTrueVoltage = aMaterial.getProcessingMaterialTierEU();

                if (aMaterial.contains(SubTag.NO_SMASHING)) {
                    tVoltageMultiplier /= 4;
                } else if (aPrefix.name()
                    .startsWith(OrePrefixes.dust.name())) {
                        return;
                    }

                if (!OrePrefixes.block.isIgnored(aMaterial.mSmeltInto)
                    && (GT_OreDictUnificator.get(OrePrefixes.block, aMaterial.mSmeltInto, 1L) != null)
                    && aMaterial != Materials.Ichorium) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(9, aStack), ItemList.Shape_Extruder_Block.get(0L))
                        .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.block, aMaterial.mSmeltInto, tAmount))
                        .duration((10 * tAmount) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);

                    // Allow creation of alloy smelter recipes for material recycling if < IV tier.
                    if (tTrueVoltage < TierEU.IV) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(9, aStack), ItemList.Shape_Mold_Block.get(0L))
                            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.block, aMaterial.mSmeltInto, tAmount))
                            .duration((5 * tAmount) * TICKS)
                            .eut(calculateRecipeEU(aMaterial, 4 * tVoltageMultiplier))
                            .recipeCategory(RecipeCategories.alloySmelterMolding)
                            .addTo(alloySmelterRecipes);
                    }
                }
                if ((aPrefix != OrePrefixes.ingot || aMaterial != aMaterial.mSmeltInto)
                    && GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial.mSmeltInto, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1, aStack), ItemList.Shape_Extruder_Ingot.get(0L))
                        .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial.mSmeltInto, tAmount))
                        .duration(10 * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 4 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (GT_OreDictUnificator.get(OrePrefixes.pipeTiny, aMaterial.mSmeltInto, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1, aStack), ItemList.Shape_Extruder_Pipe_Tiny.get(0L))
                        .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.pipeTiny, aMaterial.mSmeltInto, tAmount * 2))
                        .duration((4 * tAmount) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (GT_OreDictUnificator.get(OrePrefixes.pipeSmall, aMaterial.mSmeltInto, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1, aStack), ItemList.Shape_Extruder_Pipe_Small.get(0L))
                        .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.pipeSmall, aMaterial.mSmeltInto, tAmount))
                        .duration((8 * tAmount) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (GT_OreDictUnificator.get(OrePrefixes.pipeMedium, aMaterial.mSmeltInto, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(3, aStack), ItemList.Shape_Extruder_Pipe_Medium.get(0L))
                        .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.pipeMedium, aMaterial.mSmeltInto, tAmount))
                        .duration((24 * tAmount) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (GT_OreDictUnificator.get(OrePrefixes.pipeLarge, aMaterial.mSmeltInto, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(6, aStack), ItemList.Shape_Extruder_Pipe_Large.get(0L))
                        .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.pipeLarge, aMaterial.mSmeltInto, tAmount))
                        .duration((48 * tAmount) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (GT_OreDictUnificator.get(OrePrefixes.pipeHuge, aMaterial.mSmeltInto, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(12, aStack), ItemList.Shape_Extruder_Pipe_Huge.get(0L))
                        .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.pipeHuge, aMaterial.mSmeltInto, tAmount))
                        .duration((96 * tAmount) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial.mSmeltInto, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1, aStack), ItemList.Shape_Extruder_Plate.get(0L))
                        .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial.mSmeltInto, tAmount))
                        .duration(((int) Math.max(aMaterialMass * tAmount, tAmount)) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, aMaterial.mSmeltInto, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1, aStack), ItemList.Shape_Extruder_Small_Gear.get(0L))
                        .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, aMaterial.mSmeltInto, tAmount))
                        .duration(((int) Math.max(aMaterialMass * tAmount, tAmount)) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (GT_OreDictUnificator.get(OrePrefixes.turbineBlade, aMaterial.mSmeltInto, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(6, aStack), ItemList.Shape_Extruder_Turbine_Blade.get(0L))
                        .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.turbineBlade, aMaterial.mSmeltInto, tAmount))
                        .duration(((int) Math.max(aMaterialMass * tAmount, tAmount)) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }

                if (!(aMaterial == Materials.AnnealedCopper || aMaterial == Materials.WroughtIron)) {
                    if (aMaterial.mStandardMoltenFluid != null) {
                        if (GT_OreDictUnificator.get(OrePrefixes.ring, aMaterial, 1L) != null) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Ring.get(0L))
                                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.ring, aMaterial, 1L))
                                .fluidInputs(aMaterial.getMolten(36L))
                                .duration(5 * SECONDS)
                                .eut(calculateRecipeEU(aMaterial, 4 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                        if (GT_OreDictUnificator.get(OrePrefixes.screw, aMaterial, 1L) != null) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Screw.get(0L))
                                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.screw, aMaterial, 1L))
                                .fluidInputs(aMaterial.getMolten(18L))
                                .duration(2 * SECONDS + 10 * TICKS)
                                .eut(calculateRecipeEU(aMaterial, 2 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                        if (GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial, 1L) != null) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Rod.get(0L))
                                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial, 1L))
                                .fluidInputs(aMaterial.getMolten(72L))
                                .duration(7 * SECONDS + 10 * TICKS)
                                .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                        if (GT_OreDictUnificator.get(OrePrefixes.bolt, aMaterial, 1L) != null) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Bolt.get(0L))
                                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.bolt, aMaterial, 1L))
                                .fluidInputs(aMaterial.getMolten(18L))
                                .duration(2 * SECONDS + 10 * TICKS)
                                .eut(calculateRecipeEU(aMaterial, 2 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                        if (GT_OreDictUnificator.get(OrePrefixes.round, aMaterial, 1L) != null) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Round.get(0L))
                                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.round, aMaterial, 1L))
                                .fluidInputs(aMaterial.getMolten(18L))
                                .duration(2 * SECONDS + 10 * TICKS)
                                .eut(calculateRecipeEU(aMaterial, 2 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                        if (GT_OreDictUnificator.get(OrePrefixes.stickLong, aMaterial, 1L) != null) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Rod_Long.get(0L))
                                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.stickLong, aMaterial, 1L))
                                .fluidInputs(aMaterial.getMolten(144L))
                                .duration(15 * SECONDS)
                                .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                        if (GT_OreDictUnificator.get(OrePrefixes.turbineBlade, aMaterial, 1L) != null) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Turbine_Blade.get(0L))
                                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.turbineBlade, aMaterial, 1L))
                                .fluidInputs(aMaterial.getMolten(864L))
                                .duration(20 * SECONDS)
                                .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                        if (GT_OreDictUnificator.get(OrePrefixes.pipeTiny, aMaterial, 1L) != null) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Pipe_Tiny.get(0L))
                                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.pipeTiny, aMaterial, 1L))
                                .fluidInputs(aMaterial.getMolten(72L))
                                .duration(1 * SECONDS)
                                .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                        if (GT_OreDictUnificator.get(OrePrefixes.pipeSmall, aMaterial, 1L) != null) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Pipe_Small.get(0L))
                                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.pipeSmall, aMaterial, 1L))
                                .fluidInputs(aMaterial.getMolten(144L))
                                .duration(2 * SECONDS)
                                .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                        if (GT_OreDictUnificator.get(OrePrefixes.pipeMedium, aMaterial, 1L) != null) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Pipe_Medium.get(0L))
                                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.pipeMedium, aMaterial, 1L))
                                .fluidInputs(aMaterial.getMolten(432L))
                                .duration(4 * SECONDS)
                                .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                        if (GT_OreDictUnificator.get(OrePrefixes.pipeLarge, aMaterial, 1L) != null) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Pipe_Large.get(0L))
                                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.pipeLarge, aMaterial, 1L))
                                .fluidInputs(aMaterial.getMolten(864L))
                                .duration(8 * SECONDS)
                                .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                        if (GT_OreDictUnificator.get(OrePrefixes.pipeHuge, aMaterial, 1L) != null) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(ItemList.Shape_Mold_Pipe_Huge.get(0L))
                                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.pipeHuge, aMaterial, 1L))
                                .fluidInputs(aMaterial.getMolten(1728L))
                                .duration(16 * SECONDS)
                                .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                                .addTo(fluidSolidifierRecipes);
                        }
                    }
                }
                if (tAmount * 2 <= 64 && aMaterial != Materials.Obsidian) {
                    if (!(aMaterial == Materials.Aluminium)) {
                        if (GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial.mSmeltInto, 1L) != null) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(1, aStack), ItemList.Shape_Extruder_Rod.get(0L))
                                .itemOutputs(
                                    GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial.mSmeltInto, tAmount * 2))
                                .duration(((int) Math.max(aMaterialMass * 2L * tAmount, tAmount)) * TICKS)
                                .eut(calculateRecipeEU(aMaterial, 6 * tVoltageMultiplier))
                                .addTo(extruderRecipes);
                        }
                    } else {
                        if (GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial.mSmeltInto, 1L) != null) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(1, aStack), ItemList.Shape_Extruder_Rod.get(0L))
                                .itemOutputs(
                                    GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial.mSmeltInto, tAmount * 2))
                                .duration(10 * SECONDS)
                                .eut(calculateRecipeEU(aMaterial, 2 * tVoltageMultiplier))
                                .addTo(extruderRecipes);
                        }
                    }
                }
                if (tAmount * 2 <= 64) {
                    if (GT_OreDictUnificator.get(OrePrefixes.wireGt01, aMaterial.mSmeltInto, 1L) != null) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1, aStack), ItemList.Shape_Extruder_Wire.get(0L))
                            .itemOutputs(
                                GT_OreDictUnificator.get(OrePrefixes.wireGt01, aMaterial.mSmeltInto, tAmount * 2))
                            .duration(((int) Math.max(aMaterialMass * 2L * tAmount, tAmount)) * TICKS)
                            .eut(calculateRecipeEU(aMaterial, 6 * tVoltageMultiplier))
                            .addTo(extruderRecipes);
                    }
                }
                if (tAmount * 8 <= 64) {
                    if (GT_OreDictUnificator.get(OrePrefixes.bolt, aMaterial.mSmeltInto, 1L) != null) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1, aStack), ItemList.Shape_Extruder_Bolt.get(0L))
                            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.bolt, aMaterial.mSmeltInto, tAmount * 8))
                            .duration(((int) Math.max(aMaterialMass * 2L * tAmount, tAmount)) * TICKS)
                            .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                            .addTo(extruderRecipes);
                    }
                }
                if (tAmount * 4 <= 64) {
                    if (GT_OreDictUnificator.get(OrePrefixes.ring, aMaterial.mSmeltInto, 1L) != null) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1, aStack), ItemList.Shape_Extruder_Ring.get(0L))
                            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.ring, aMaterial.mSmeltInto, tAmount * 4))
                            .duration(((int) Math.max(aMaterialMass * 2L * tAmount, tAmount)) * TICKS)
                            .eut(calculateRecipeEU(aMaterial, 6 * tVoltageMultiplier))
                            .addTo(extruderRecipes);
                    }
                    if ((aMaterial.mUnificatable) && (aMaterial.mMaterialInto == aMaterial)
                        && !aMaterial.contains(SubTag.NO_SMASHING)) {
                        // If material tier < IV then add manual recipe.
                        if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV
                            && GT_OreDictUnificator.get(OrePrefixes.ring, aMaterial, 1L) != null) {
                            GT_ModHandler.addCraftingRecipe(
                                GT_OreDictUnificator.get(OrePrefixes.ring, aMaterial, 1L),
                                GT_Proxy.tBits,
                                new Object[] { "h ", "fX", 'X', OrePrefixes.stick.get(aMaterial) });
                        }
                    }
                }

                if (GT_OreDictUnificator.get(OrePrefixes.toolHeadSword, aMaterial.mSmeltInto, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(2, aStack), ItemList.Shape_Extruder_Sword.get(0L))
                        .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.toolHeadSword, aMaterial.mSmeltInto, tAmount))
                        .duration(((int) Math.max(aMaterialMass * 2L * tAmount, tAmount)) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (GT_OreDictUnificator.get(OrePrefixes.toolHeadPickaxe, aMaterial.mSmeltInto, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(3, aStack), ItemList.Shape_Extruder_Pickaxe.get(0L))
                        .itemOutputs(
                            GT_OreDictUnificator.get(OrePrefixes.toolHeadPickaxe, aMaterial.mSmeltInto, tAmount))
                        .duration(((int) Math.max(aMaterialMass * 3L * tAmount, tAmount)) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (GT_OreDictUnificator.get(OrePrefixes.toolHeadShovel, aMaterial.mSmeltInto, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1, aStack), ItemList.Shape_Extruder_Shovel.get(0L))
                        .itemOutputs(
                            GT_OreDictUnificator.get(OrePrefixes.toolHeadShovel, aMaterial.mSmeltInto, tAmount))
                        .duration(((int) Math.max(aMaterialMass * 1L * tAmount, tAmount)) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (GT_OreDictUnificator.get(OrePrefixes.toolHeadAxe, aMaterial.mSmeltInto, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(3, aStack), ItemList.Shape_Extruder_Axe.get(0L))
                        .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.toolHeadAxe, aMaterial.mSmeltInto, tAmount))
                        .duration(((int) Math.max(aMaterialMass * 3L * tAmount, tAmount)) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (GT_OreDictUnificator.get(OrePrefixes.toolHeadHoe, aMaterial.mSmeltInto, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(2, aStack), ItemList.Shape_Extruder_Hoe.get(0L))
                        .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.toolHeadHoe, aMaterial.mSmeltInto, tAmount))
                        .duration(((int) Math.max(aMaterialMass * 2L * tAmount, tAmount)) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (GT_OreDictUnificator.get(OrePrefixes.toolHeadHammer, aMaterial.mSmeltInto, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(6, aStack), ItemList.Shape_Extruder_Hammer.get(0L))
                        .itemOutputs(
                            GT_OreDictUnificator.get(OrePrefixes.toolHeadHammer, aMaterial.mSmeltInto, tAmount))
                        .duration(((int) Math.max(aMaterialMass * 6L * tAmount, tAmount)) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (GT_OreDictUnificator.get(OrePrefixes.toolHeadFile, aMaterial.mSmeltInto, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(2, aStack), ItemList.Shape_Extruder_File.get(0L))
                        .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.toolHeadFile, aMaterial.mSmeltInto, tAmount))
                        .duration(((int) Math.max(aMaterialMass * 2L * tAmount, tAmount)) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (GT_OreDictUnificator.get(OrePrefixes.toolHeadSaw, aMaterial.mSmeltInto, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(2, aStack), ItemList.Shape_Extruder_Saw.get(0L))
                        .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.toolHeadSaw, aMaterial.mSmeltInto, tAmount))
                        .duration(((int) Math.max(aMaterialMass * 2L * tAmount, tAmount)) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }
                if (GT_OreDictUnificator.get(OrePrefixes.gearGt, aMaterial.mSmeltInto, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(4, aStack), ItemList.Shape_Extruder_Gear.get(0L))
                        .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.gearGt, aMaterial.mSmeltInto, tAmount))
                        .duration(((int) Math.max(aMaterialMass * 5L * tAmount, tAmount)) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(extruderRecipes);
                }

                if (!(aMaterial == Materials.StyreneButadieneRubber || aMaterial == Materials.Silicone)) {
                    if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                        if (GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial.mSmeltInto, 1L) != null) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(2, aStack), ItemList.Shape_Mold_Plate.get(0L))
                                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial.mSmeltInto, tAmount))
                                .duration(((int) Math.max(aMaterialMass * 2L * tAmount, tAmount)) * TICKS)
                                .eut(calculateRecipeEU(aMaterial, 2 * tVoltageMultiplier))
                                .recipeCategory(RecipeCategories.alloySmelterMolding)
                                .addTo(alloySmelterRecipes);
                        }
                    }
                } else {
                    // If tier < IV then add ability to turn ingots into plates via alloy smelter.
                    if (tTrueVoltage < TierEU.IV) {
                        if (GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial.mSmeltInto, 1L) != null) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(1, aStack), ItemList.Shape_Mold_Plate.get(0L))
                                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial.mSmeltInto, tAmount))
                                .duration(((int) Math.max(aMaterialMass * 2L * tAmount, tAmount)) * TICKS)
                                .eut(calculateRecipeEU(aMaterial, 2 * tVoltageMultiplier))
                                .recipeCategory(RecipeCategories.alloySmelterMolding)
                                .addTo(alloySmelterRecipes);
                        }
                    }
                }

                // If tier < IV then add ability to turn ingots into gears via alloy smelter.
                if (tTrueVoltage < TierEU.IV) {
                    if (GT_OreDictUnificator.get(OrePrefixes.gearGt, aMaterial.mSmeltInto, 1L) != null) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(8, aStack), ItemList.Shape_Mold_Gear.get(0L))
                            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.gearGt, aMaterial.mSmeltInto, tAmount))
                            .duration(((int) Math.max(aMaterialMass * 10L * tAmount, tAmount)) * TICKS)
                            .eut(calculateRecipeEU(aMaterial, 2 * tVoltageMultiplier))
                            .recipeCategory(RecipeCategories.alloySmelterMolding)
                            .addTo(alloySmelterRecipes);
                    }
                }

                switch (aMaterial.mSmeltInto.mName) {
                    case "Glass" -> {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1, aStack), ItemList.Shape_Extruder_Bottle.get(0L))
                            .itemOutputs(new ItemStack(Items.glass_bottle, 1))
                            .duration((tAmount * 32) * TICKS)
                            .eut(16)
                            .addTo(extruderRecipes);
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1, aStack), ItemList.Shape_Mold_Bottle.get(0L))
                            .itemOutputs(new ItemStack(Items.glass_bottle, 1))
                            .duration((tAmount * 64) * TICKS)
                            .eut(4)
                            .addTo(alloySmelterRecipes);
                    }
                    case "Steel" -> {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1, aStack), ItemList.Shape_Extruder_Cell.get(0L))
                            .itemOutputs(ItemList.Cell_Empty.get(tAmount))
                            .duration((tAmount * 128) * TICKS)
                            .eut(TierEU.RECIPE_LV)
                            .addTo(extruderRecipes);
                        if (tAmount * 2 <= 64) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(1, aStack), ItemList.Shape_Extruder_Casing.get(0L))
                                .itemOutputs(GT_ModHandler.getIC2Item("casingadviron", tAmount * 2))
                                .duration((tAmount * 32) * TICKS)
                                .eut(3 * tVoltageMultiplier)
                                .addTo(extruderRecipes);
                        }
                        if (tAmount * 2 <= 64) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(2, aStack), ItemList.Shape_Mold_Casing.get(0L))
                                .itemOutputs(GT_ModHandler.getIC2Item("casingadviron", tAmount * 3))
                                .duration((tAmount * 128) * TICKS)
                                .eut(1 * tVoltageMultiplier)
                                .addTo(alloySmelterRecipes);
                        }
                    }
                    case "Iron", "WroughtIron" -> {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1, aStack), ItemList.Shape_Extruder_Cell.get(0L))
                            .itemOutputs(GT_ModHandler.getIC2Item("fuelRod", tAmount))
                            .duration((tAmount * 128) * TICKS)
                            .eut(TierEU.RECIPE_LV)
                            .addTo(extruderRecipes);
                        if (tAmount * 2 <= 64) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(1, aStack), ItemList.Shape_Extruder_Casing.get(0L))
                                .itemOutputs(GT_ModHandler.getIC2Item("casingiron", tAmount * 2))
                                .duration((tAmount * 32) * TICKS)
                                .eut(3 * tVoltageMultiplier)
                                .addTo(extruderRecipes);
                        }
                        if (tAmount * 2 <= 64) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(2, aStack), ItemList.Shape_Mold_Casing.get(0L))
                                .itemOutputs(GT_ModHandler.getIC2Item("casingiron", tAmount * 3))
                                .duration((tAmount * 128) * TICKS)
                                .eut(1 * tVoltageMultiplier)
                                .addTo(alloySmelterRecipes);
                        }
                        if (tAmount * 31 <= 64) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(31, aStack), ItemList.Shape_Mold_Anvil.get(0L))
                                .itemOutputs(new ItemStack(Blocks.anvil, 1, 0))
                                .duration((tAmount * 512) * TICKS)
                                .eut(4 * tVoltageMultiplier)
                                .addTo(alloySmelterRecipes);
                        }
                    }
                    case "Tin" -> {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(2, aStack), ItemList.Shape_Extruder_Cell.get(0L))
                            .itemOutputs(ItemList.Cell_Empty.get(tAmount))
                            .duration((tAmount * 128) * TICKS)
                            .eut(TierEU.RECIPE_LV)
                            .addTo(extruderRecipes);
                        if (tAmount * 2 <= 64) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(1, aStack), ItemList.Shape_Extruder_Casing.get(0L))
                                .itemOutputs(GT_ModHandler.getIC2Item("casingtin", tAmount * 2))
                                .duration((tAmount * 32) * TICKS)
                                .eut(3 * tVoltageMultiplier)
                                .addTo(extruderRecipes);
                        }
                        if (tAmount * 2 <= 64) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(2, aStack), ItemList.Shape_Mold_Casing.get(0L))
                                .itemOutputs(GT_ModHandler.getIC2Item("casingtin", tAmount * 3))
                                .duration((tAmount * 128) * TICKS)
                                .eut(1 * tVoltageMultiplier)
                                .addTo(alloySmelterRecipes);
                        }
                    }
                    case "Lead" -> {
                        if (tAmount * 2 <= 64) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(1, aStack), ItemList.Shape_Extruder_Casing.get(0L))
                                .itemOutputs(GT_ModHandler.getIC2Item("casinglead", tAmount * 2))
                                .duration((tAmount * 32) * TICKS)
                                .eut(3 * tVoltageMultiplier)
                                .addTo(extruderRecipes);
                        }
                        if (tAmount * 2 <= 64) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(2, aStack), ItemList.Shape_Mold_Casing.get(0L))
                                .itemOutputs(GT_ModHandler.getIC2Item("casinglead", tAmount * 3))
                                .duration((tAmount * 128) * TICKS)
                                .eut(1 * tVoltageMultiplier)
                                .addTo(alloySmelterRecipes);
                        }
                    }
                    case "Copper", "AnnealedCopper" -> {
                        if (tAmount * 2 <= 64) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(1, aStack), ItemList.Shape_Extruder_Casing.get(0L))
                                .itemOutputs(GT_ModHandler.getIC2Item("casingcopper", tAmount * 2))
                                .duration((tAmount * 32) * TICKS)
                                .eut(3 * tVoltageMultiplier)
                                .addTo(extruderRecipes);
                        }
                        if (tAmount * 2 <= 64) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(2, aStack), ItemList.Shape_Mold_Casing.get(0L))
                                .itemOutputs(GT_ModHandler.getIC2Item("casingcopper", tAmount * 3))
                                .duration((tAmount * 128) * TICKS)
                                .eut(1 * tVoltageMultiplier)
                                .addTo(alloySmelterRecipes);
                        }
                    }
                    case "Bronze" -> {
                        if (tAmount * 2 <= 64) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(1, aStack), ItemList.Shape_Extruder_Casing.get(0L))
                                .itemOutputs(GT_ModHandler.getIC2Item("casingbronze", tAmount * 2))
                                .duration((tAmount * 32) * TICKS)
                                .eut(3 * tVoltageMultiplier)
                                .addTo(extruderRecipes);
                        }
                        if (tAmount * 2 <= 64) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(2, aStack), ItemList.Shape_Mold_Casing.get(0L))
                                .itemOutputs(GT_ModHandler.getIC2Item("casingbronze", tAmount * 3))
                                .duration((tAmount * 128) * TICKS)
                                .eut(1 * tVoltageMultiplier)
                                .addTo(alloySmelterRecipes);
                        }
                    }
                    case "Gold" -> {
                        if (tAmount * 2 <= 64) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(1, aStack), ItemList.Shape_Extruder_Casing.get(0L))
                                .itemOutputs(GT_ModHandler.getIC2Item("casinggold", tAmount * 2))
                                .duration((tAmount * 32) * TICKS)
                                .eut(3 * tVoltageMultiplier)
                                .addTo(extruderRecipes);
                        }
                        if (tAmount * 2 <= 64) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(2, aStack), ItemList.Shape_Mold_Casing.get(0L))
                                .itemOutputs(GT_ModHandler.getIC2Item("casinggold", tAmount * 3))
                                .duration((tAmount * 128) * TICKS)
                                .eut(1 * tVoltageMultiplier)
                                .addTo(alloySmelterRecipes);
                        }
                    }
                    case "Polytetrafluoroethylene" -> {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1, aStack), ItemList.Shape_Extruder_Cell.get(0L))
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
