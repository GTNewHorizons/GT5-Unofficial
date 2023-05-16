package gregtech.loaders.oreprocessing;

import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sAlloySmelterRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sExtruderRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sFluidSolidficationRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;
import static gregtech.api.util.GT_Utility.calculateRecipeEU;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.*;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Proxy;

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
                    && (GT_OreDictUnificator.get(OrePrefixes.block, aMaterial.mSmeltInto, 1L) != null)) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(9L, aStack), ItemList.Shape_Extruder_Block.get(0L))
                        .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.block, aMaterial.mSmeltInto, tAmount))
                        .noFluidInputs()
                        .noFluidOutputs()
                        .duration((10 * tAmount) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(sExtruderRecipes);

                    // Allow creation of alloy smelter recipes for material recycling if < IV tier.
                    if (tTrueVoltage < TierEU.IV) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(9L, aStack), ItemList.Shape_Mold_Block.get(0L))
                            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.block, aMaterial.mSmeltInto, tAmount))
                            .noFluidInputs()
                            .noFluidOutputs()
                            .duration((5 * tAmount) * TICKS)
                            .eut(calculateRecipeEU(aMaterial, 4 * tVoltageMultiplier))
                            .addTo(sAlloySmelterRecipes);
                    }
                }
                if ((aPrefix != OrePrefixes.ingot || aMaterial != aMaterial.mSmeltInto)
                    && GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial.mSmeltInto, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1L, aStack), ItemList.Shape_Extruder_Ingot.get(0L))
                        .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial.mSmeltInto, tAmount))
                        .noFluidInputs()
                        .noFluidOutputs()
                        .duration(10 * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 4 * tVoltageMultiplier))
                        .addTo(sExtruderRecipes);
                }
                if (GT_OreDictUnificator.get(OrePrefixes.pipeTiny, aMaterial.mSmeltInto, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1L, aStack), ItemList.Shape_Extruder_Pipe_Tiny.get(0L))
                        .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.pipeTiny, aMaterial.mSmeltInto, tAmount * 2))
                        .noFluidInputs()
                        .noFluidOutputs()
                        .duration((4 * tAmount) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(sExtruderRecipes);
                }
                if (GT_OreDictUnificator.get(OrePrefixes.pipeSmall, aMaterial.mSmeltInto, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1L, aStack), ItemList.Shape_Extruder_Pipe_Small.get(0L))
                        .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.pipeSmall, aMaterial.mSmeltInto, tAmount))
                        .noFluidInputs()
                        .noFluidOutputs()
                        .duration((8 * tAmount) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(sExtruderRecipes);
                }
                if (GT_OreDictUnificator.get(OrePrefixes.pipeMedium, aMaterial.mSmeltInto, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(3L, aStack), ItemList.Shape_Extruder_Pipe_Medium.get(0L))
                        .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.pipeMedium, aMaterial.mSmeltInto, tAmount))
                        .noFluidInputs()
                        .noFluidOutputs()
                        .duration((24 * tAmount) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(sExtruderRecipes);
                }
                if (GT_OreDictUnificator.get(OrePrefixes.pipeLarge, aMaterial.mSmeltInto, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(6L, aStack), ItemList.Shape_Extruder_Pipe_Large.get(0L))
                        .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.pipeLarge, aMaterial.mSmeltInto, tAmount))
                        .noFluidInputs()
                        .noFluidOutputs()
                        .duration((48 * tAmount) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(sExtruderRecipes);
                }
                if (GT_OreDictUnificator.get(OrePrefixes.pipeHuge, aMaterial.mSmeltInto, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(12L, aStack), ItemList.Shape_Extruder_Pipe_Huge.get(0L))
                        .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.pipeHuge, aMaterial.mSmeltInto, tAmount))
                        .noFluidInputs()
                        .noFluidOutputs()
                        .duration((96 * tAmount) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(sExtruderRecipes);
                }
                if (GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial.mSmeltInto, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1L, aStack), ItemList.Shape_Extruder_Plate.get(0L))
                        .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial.mSmeltInto, tAmount))
                        .noFluidInputs()
                        .noFluidOutputs()
                        .duration(((int) Math.max(aMaterialMass * tAmount, tAmount)) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(sExtruderRecipes);
                }
                if (GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, aMaterial.mSmeltInto, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1L, aStack), ItemList.Shape_Extruder_Small_Gear.get(0L))
                        .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, aMaterial.mSmeltInto, tAmount))
                        .noFluidInputs()
                        .noFluidOutputs()
                        .duration(((int) Math.max(aMaterialMass * tAmount, tAmount)) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(sExtruderRecipes);
                }
                if (GT_OreDictUnificator.get(OrePrefixes.turbineBlade, aMaterial.mSmeltInto, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(6L, aStack), ItemList.Shape_Extruder_Turbine_Blade.get(0L))
                        .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.turbineBlade, aMaterial.mSmeltInto, tAmount))
                        .noFluidInputs()
                        .noFluidOutputs()
                        .duration(((int) Math.max(aMaterialMass * tAmount, tAmount)) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(sExtruderRecipes);
                }

                if (!(aMaterial == Materials.AnnealedCopper || aMaterial == Materials.WroughtIron)) {
                    if (GT_OreDictUnificator.get(OrePrefixes.ring, aMaterial, 1L) != null) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(ItemList.Shape_Mold_Ring.get(0L))
                            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.ring, aMaterial, 1L))
                            .fluidInputs(aMaterial.getMolten(36L))
                            .noFluidOutputs()
                            .duration(5 * SECONDS)
                            .eut(calculateRecipeEU(aMaterial, 4 * tVoltageMultiplier))
                            .addTo(sFluidSolidficationRecipes);
                    }
                    if (GT_OreDictUnificator.get(OrePrefixes.screw, aMaterial, 1L) != null) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(ItemList.Shape_Mold_Screw.get(0L))
                            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.screw, aMaterial, 1L))
                            .fluidInputs(aMaterial.getMolten(18L))
                            .noFluidOutputs()
                            .duration(2 * SECONDS + 10 * TICKS)
                            .eut(calculateRecipeEU(aMaterial, 2 * tVoltageMultiplier))
                            .addTo(sFluidSolidficationRecipes);
                    }
                    if (GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial, 1L) != null) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(ItemList.Shape_Mold_Rod.get(0L))
                            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial, 1L))
                            .fluidInputs(aMaterial.getMolten(72L))
                            .noFluidOutputs()
                            .duration(7 * SECONDS + 10 * TICKS)
                            .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                            .addTo(sFluidSolidficationRecipes);
                    }
                    if (GT_OreDictUnificator.get(OrePrefixes.bolt, aMaterial, 1L) != null) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(ItemList.Shape_Mold_Bolt.get(0L))
                            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.bolt, aMaterial, 1L))
                            .fluidInputs(aMaterial.getMolten(18L))
                            .noFluidOutputs()
                            .duration(2 * SECONDS + 10 * TICKS)
                            .eut(calculateRecipeEU(aMaterial, 2 * tVoltageMultiplier))
                            .addTo(sFluidSolidficationRecipes);
                    }
                    if (GT_OreDictUnificator.get(OrePrefixes.round, aMaterial, 1L) != null) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(ItemList.Shape_Mold_Round.get(0L))
                            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.round, aMaterial, 1L))
                            .fluidInputs(aMaterial.getMolten(18L))
                            .noFluidOutputs()
                            .duration(2 * SECONDS + 10 * TICKS)
                            .eut(calculateRecipeEU(aMaterial, 2 * tVoltageMultiplier))
                            .addTo(sFluidSolidficationRecipes);
                    }
                    if (GT_OreDictUnificator.get(OrePrefixes.stickLong, aMaterial, 1L) != null) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(ItemList.Shape_Mold_Rod_Long.get(0L))
                            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.stickLong, aMaterial, 1L))
                            .fluidInputs(aMaterial.getMolten(144L))
                            .noFluidOutputs()
                            .duration(15 * SECONDS)
                            .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                            .addTo(sFluidSolidficationRecipes);
                    }
                    if (GT_OreDictUnificator.get(OrePrefixes.turbineBlade, aMaterial, 1L) != null) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(ItemList.Shape_Mold_Turbine_Blade.get(0L))
                            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.turbineBlade, aMaterial, 1L))
                            .fluidInputs(aMaterial.getMolten(864L))
                            .noFluidOutputs()
                            .duration(20 * SECONDS)
                            .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                            .addTo(sFluidSolidficationRecipes);
                    }
                    if (GT_OreDictUnificator.get(OrePrefixes.pipeTiny, aMaterial, 1L) != null) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(ItemList.Shape_Mold_Pipe_Tiny.get(0L))
                            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.pipeTiny, aMaterial, 1L))
                            .fluidInputs(aMaterial.getMolten(72L))
                            .noFluidOutputs()
                            .duration(1 * SECONDS)
                            .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                            .addTo(sFluidSolidficationRecipes);
                    }
                    if (GT_OreDictUnificator.get(OrePrefixes.pipeSmall, aMaterial, 1L) != null) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(ItemList.Shape_Mold_Pipe_Small.get(0L))
                            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.pipeSmall, aMaterial, 1L))
                            .fluidInputs(aMaterial.getMolten(144L))
                            .noFluidOutputs()
                            .duration(2 * SECONDS)
                            .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                            .addTo(sFluidSolidficationRecipes);
                    }
                    if (GT_OreDictUnificator.get(OrePrefixes.pipeMedium, aMaterial, 1L) != null) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(ItemList.Shape_Mold_Pipe_Medium.get(0L))
                            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.pipeMedium, aMaterial, 1L))
                            .fluidInputs(aMaterial.getMolten(432L))
                            .noFluidOutputs()
                            .duration(4 * SECONDS)
                            .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                            .addTo(sFluidSolidficationRecipes);
                    }
                    if (GT_OreDictUnificator.get(OrePrefixes.pipeLarge, aMaterial, 1L) != null) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(ItemList.Shape_Mold_Pipe_Large.get(0L))
                            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.pipeLarge, aMaterial, 1L))
                            .fluidInputs(aMaterial.getMolten(864L))
                            .noFluidOutputs()
                            .duration(8 * SECONDS)
                            .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                            .addTo(sFluidSolidficationRecipes);
                    }
                    if (GT_OreDictUnificator.get(OrePrefixes.pipeHuge, aMaterial, 1L) != null) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(ItemList.Shape_Mold_Pipe_Huge.get(0L))
                            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.pipeHuge, aMaterial, 1L))
                            .fluidInputs(aMaterial.getMolten(1728L))
                            .noFluidOutputs()
                            .duration(16 * SECONDS)
                            .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                            .addTo(sFluidSolidficationRecipes);
                    }
                }
                if (tAmount * 2 <= 64) {
                    if (!(aMaterial == Materials.Aluminium)) {
                        if (GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial.mSmeltInto, 1L) != null) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(1L, aStack), ItemList.Shape_Extruder_Rod.get(0L))
                                .itemOutputs(
                                    GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial.mSmeltInto, tAmount * 2))
                                .noFluidInputs()
                                .noFluidOutputs()
                                .duration(((int) Math.max(aMaterialMass * 2L * tAmount, tAmount)) * TICKS)
                                .eut(calculateRecipeEU(aMaterial, 6 * tVoltageMultiplier))
                                .addTo(sExtruderRecipes);
                        }
                    } else {
                        if (GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial.mSmeltInto, 1L) != null) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(1L, aStack), ItemList.Shape_Extruder_Rod.get(0L))
                                .itemOutputs(
                                    GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial.mSmeltInto, tAmount * 2))
                                .noFluidInputs()
                                .noFluidOutputs()
                                .duration(10 * SECONDS)
                                .eut(calculateRecipeEU(aMaterial, 2 * tVoltageMultiplier))
                                .addTo(sExtruderRecipes);
                        }
                    }
                }
                if (tAmount * 2 <= 64) {
                    if (GT_OreDictUnificator.get(OrePrefixes.wireGt01, aMaterial.mSmeltInto, 1L) != null) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1L, aStack), ItemList.Shape_Extruder_Wire.get(0L))
                            .itemOutputs(
                                GT_OreDictUnificator.get(OrePrefixes.wireGt01, aMaterial.mSmeltInto, tAmount * 2))
                            .noFluidInputs()
                            .noFluidOutputs()
                            .duration(((int) Math.max(aMaterialMass * 2L * tAmount, tAmount)) * TICKS)
                            .eut(calculateRecipeEU(aMaterial, 6 * tVoltageMultiplier))
                            .addTo(sExtruderRecipes);
                    }
                }
                if (tAmount * 8 <= 64) {
                    if (GT_OreDictUnificator.get(OrePrefixes.bolt, aMaterial.mSmeltInto, 1L) != null) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1L, aStack), ItemList.Shape_Extruder_Bolt.get(0L))
                            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.bolt, aMaterial.mSmeltInto, tAmount * 8))
                            .noFluidInputs()
                            .noFluidOutputs()
                            .duration(((int) Math.max(aMaterialMass * 2L * tAmount, tAmount)) * TICKS)
                            .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                            .addTo(sExtruderRecipes);
                    }
                }
                if (tAmount * 4 <= 64) {
                    if (GT_OreDictUnificator.get(OrePrefixes.ring, aMaterial.mSmeltInto, 1L) != null) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1L, aStack), ItemList.Shape_Extruder_Ring.get(0L))
                            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.ring, aMaterial.mSmeltInto, tAmount * 4))
                            .noFluidInputs()
                            .noFluidOutputs()
                            .duration(((int) Math.max(aMaterialMass * 2L * tAmount, tAmount)) * TICKS)
                            .eut(calculateRecipeEU(aMaterial, 6 * tVoltageMultiplier))
                            .addTo(sExtruderRecipes);
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
                        .itemInputs(GT_Utility.copyAmount(2L, aStack), ItemList.Shape_Extruder_Sword.get(0L))
                        .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.toolHeadSword, aMaterial.mSmeltInto, tAmount))
                        .noFluidInputs()
                        .noFluidOutputs()
                        .duration(((int) Math.max(aMaterialMass * 2L * tAmount, tAmount)) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(sExtruderRecipes);
                }
                if (GT_OreDictUnificator.get(OrePrefixes.toolHeadPickaxe, aMaterial.mSmeltInto, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(3L, aStack), ItemList.Shape_Extruder_Pickaxe.get(0L))
                        .itemOutputs(
                            GT_OreDictUnificator.get(OrePrefixes.toolHeadPickaxe, aMaterial.mSmeltInto, tAmount))
                        .noFluidInputs()
                        .noFluidOutputs()
                        .duration(((int) Math.max(aMaterialMass * 3L * tAmount, tAmount)) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(sExtruderRecipes);
                }
                if (GT_OreDictUnificator.get(OrePrefixes.toolHeadShovel, aMaterial.mSmeltInto, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1L, aStack), ItemList.Shape_Extruder_Shovel.get(0L))
                        .itemOutputs(
                            GT_OreDictUnificator.get(OrePrefixes.toolHeadShovel, aMaterial.mSmeltInto, tAmount))
                        .noFluidInputs()
                        .noFluidOutputs()
                        .duration(((int) Math.max(aMaterialMass * 1L * tAmount, tAmount)) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(sExtruderRecipes);
                }
                if (GT_OreDictUnificator.get(OrePrefixes.toolHeadAxe, aMaterial.mSmeltInto, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(3L, aStack), ItemList.Shape_Extruder_Axe.get(0L))
                        .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.toolHeadAxe, aMaterial.mSmeltInto, tAmount))
                        .noFluidInputs()
                        .noFluidOutputs()
                        .duration(((int) Math.max(aMaterialMass * 3L * tAmount, tAmount)) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(sExtruderRecipes);
                }
                if (GT_OreDictUnificator.get(OrePrefixes.toolHeadHoe, aMaterial.mSmeltInto, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(2L, aStack), ItemList.Shape_Extruder_Hoe.get(0L))
                        .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.toolHeadHoe, aMaterial.mSmeltInto, tAmount))
                        .noFluidInputs()
                        .noFluidOutputs()
                        .duration(((int) Math.max(aMaterialMass * 2L * tAmount, tAmount)) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(sExtruderRecipes);
                }
                if (GT_OreDictUnificator.get(OrePrefixes.toolHeadHammer, aMaterial.mSmeltInto, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(6L, aStack), ItemList.Shape_Extruder_Hammer.get(0L))
                        .itemOutputs(
                            GT_OreDictUnificator.get(OrePrefixes.toolHeadHammer, aMaterial.mSmeltInto, tAmount))
                        .noFluidInputs()
                        .noFluidOutputs()
                        .duration(((int) Math.max(aMaterialMass * 6L * tAmount, tAmount)) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(sExtruderRecipes);
                }
                if (GT_OreDictUnificator.get(OrePrefixes.toolHeadFile, aMaterial.mSmeltInto, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(2L, aStack), ItemList.Shape_Extruder_File.get(0L))
                        .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.toolHeadFile, aMaterial.mSmeltInto, tAmount))
                        .noFluidInputs()
                        .noFluidOutputs()
                        .duration(((int) Math.max(aMaterialMass * 2L * tAmount, tAmount)) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(sExtruderRecipes);
                }
                if (GT_OreDictUnificator.get(OrePrefixes.toolHeadSaw, aMaterial.mSmeltInto, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(2L, aStack), ItemList.Shape_Extruder_Saw.get(0L))
                        .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.toolHeadSaw, aMaterial.mSmeltInto, tAmount))
                        .noFluidInputs()
                        .noFluidOutputs()
                        .duration(((int) Math.max(aMaterialMass * 2L * tAmount, tAmount)) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(sExtruderRecipes);
                }
                if (GT_OreDictUnificator.get(OrePrefixes.gearGt, aMaterial.mSmeltInto, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(4L, aStack), ItemList.Shape_Extruder_Gear.get(0L))
                        .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.gearGt, aMaterial.mSmeltInto, tAmount))
                        .noFluidInputs()
                        .noFluidOutputs()
                        .duration(((int) Math.max(aMaterialMass * 5L * tAmount, tAmount)) * TICKS)
                        .eut(calculateRecipeEU(aMaterial, 8 * tVoltageMultiplier))
                        .addTo(sExtruderRecipes);
                }

                if (!(aMaterial == Materials.StyreneButadieneRubber || aMaterial == Materials.Silicone)) {
                    if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                        if (GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial.mSmeltInto, 1L) != null) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(2L, aStack), ItemList.Shape_Mold_Plate.get(0L))
                                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial.mSmeltInto, tAmount))
                                .noFluidInputs()
                                .noFluidOutputs()
                                .duration(((int) Math.max(aMaterialMass * 2L * tAmount, tAmount)) * TICKS)
                                .eut(calculateRecipeEU(aMaterial, 2 * tVoltageMultiplier))
                                .addTo(sAlloySmelterRecipes);
                        }
                    }
                } else {
                    // If tier < IV then add ability to turn ingots into plates via alloy smelter.
                    if (tTrueVoltage < TierEU.IV) {
                        if (GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial.mSmeltInto, 1L) != null) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(1L, aStack), ItemList.Shape_Mold_Plate.get(0L))
                                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial.mSmeltInto, tAmount))
                                .noFluidInputs()
                                .noFluidOutputs()
                                .duration(((int) Math.max(aMaterialMass * 2L * tAmount, tAmount)) * TICKS)
                                .eut(calculateRecipeEU(aMaterial, 2 * tVoltageMultiplier))
                                .addTo(sAlloySmelterRecipes);
                        }
                    }
                }

                // If tier < IV then add ability to turn ingots into gears via alloy smelter.
                if (tTrueVoltage < TierEU.IV) {
                    if (GT_OreDictUnificator.get(OrePrefixes.gearGt, aMaterial.mSmeltInto, 1L) != null) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(8L, aStack), ItemList.Shape_Mold_Gear.get(0L))
                            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.gearGt, aMaterial.mSmeltInto, tAmount))
                            .noFluidInputs()
                            .noFluidOutputs()
                            .duration(((int) Math.max(aMaterialMass * 10L * tAmount, tAmount)) * TICKS)
                            .eut(calculateRecipeEU(aMaterial, 2 * tVoltageMultiplier))
                            .addTo(sAlloySmelterRecipes);
                    }
                }

                switch (aMaterial.mSmeltInto.mName) {
                    case "Glass" -> {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1L, aStack), ItemList.Shape_Extruder_Bottle.get(0L))
                            .itemOutputs(new ItemStack(Items.glass_bottle, 1))
                            .noFluidInputs()
                            .noFluidOutputs()
                            .duration((tAmount * 32) * TICKS)
                            .eut(16)
                            .addTo(sExtruderRecipes);
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1L, aStack), ItemList.Shape_Mold_Bottle.get(0L))
                            .itemOutputs(new ItemStack(Items.glass_bottle, 1))
                            .noFluidInputs()
                            .noFluidOutputs()
                            .duration((tAmount * 64) * TICKS)
                            .eut(4)
                            .addTo(sAlloySmelterRecipes);
                    }
                    case "Steel" -> {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1L, aStack), ItemList.Shape_Extruder_Cell.get(0L))
                            .itemOutputs(ItemList.Cell_Empty.get(tAmount))
                            .noFluidInputs()
                            .noFluidOutputs()
                            .duration((tAmount * 128) * TICKS)
                            .eut(TierEU.RECIPE_LV)
                            .addTo(sExtruderRecipes);
                        if (tAmount * 2 <= 64) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(1L, aStack), ItemList.Shape_Extruder_Casing.get(0L))
                                .itemOutputs(GT_ModHandler.getIC2Item("casingadviron", tAmount * 2))
                                .noFluidInputs()
                                .noFluidOutputs()
                                .duration((tAmount * 32) * TICKS)
                                .eut(3 * tVoltageMultiplier)
                                .addTo(sExtruderRecipes);
                        }
                        if (tAmount * 2 <= 64) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(2L, aStack), ItemList.Shape_Mold_Casing.get(0L))
                                .itemOutputs(GT_ModHandler.getIC2Item("casingadviron", tAmount * 3))
                                .noFluidInputs()
                                .noFluidOutputs()
                                .duration((tAmount * 128) * TICKS)
                                .eut(1 * tVoltageMultiplier)
                                .addTo(sAlloySmelterRecipes);
                        }
                    }
                    case "Iron", "WroughtIron" -> {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1L, aStack), ItemList.Shape_Extruder_Cell.get(0L))
                            .itemOutputs(GT_ModHandler.getIC2Item("fuelRod", tAmount))
                            .noFluidInputs()
                            .noFluidOutputs()
                            .duration((tAmount * 128) * TICKS)
                            .eut(TierEU.RECIPE_LV)
                            .addTo(sExtruderRecipes);
                        if (tAmount * 2 <= 64) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(1L, aStack), ItemList.Shape_Extruder_Casing.get(0L))
                                .itemOutputs(GT_ModHandler.getIC2Item("casingiron", tAmount * 2))
                                .noFluidInputs()
                                .noFluidOutputs()
                                .duration((tAmount * 32) * TICKS)
                                .eut(3 * tVoltageMultiplier)
                                .addTo(sExtruderRecipes);
                        }
                        if (tAmount * 2 <= 64) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(2L, aStack), ItemList.Shape_Mold_Casing.get(0L))
                                .itemOutputs(GT_ModHandler.getIC2Item("casingiron", tAmount * 3))
                                .noFluidInputs()
                                .noFluidOutputs()
                                .duration((tAmount * 128) * TICKS)
                                .eut(1 * tVoltageMultiplier)
                                .addTo(sAlloySmelterRecipes);
                        }
                        if (tAmount * 31 <= 64) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(31L, aStack), ItemList.Shape_Mold_Anvil.get(0L))
                                .itemOutputs(new ItemStack(Blocks.anvil, 1, 0))
                                .noFluidInputs()
                                .noFluidOutputs()
                                .duration((tAmount * 512) * TICKS)
                                .eut(4 * tVoltageMultiplier)
                                .addTo(sAlloySmelterRecipes);
                        }
                    }
                    case "Tin" -> {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(2L, aStack), ItemList.Shape_Extruder_Cell.get(0L))
                            .itemOutputs(ItemList.Cell_Empty.get(tAmount))
                            .noFluidInputs()
                            .noFluidOutputs()
                            .duration((tAmount * 128) * TICKS)
                            .eut(TierEU.RECIPE_LV)
                            .addTo(sExtruderRecipes);
                        if (tAmount * 2 <= 64) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(1L, aStack), ItemList.Shape_Extruder_Casing.get(0L))
                                .itemOutputs(GT_ModHandler.getIC2Item("casingtin", tAmount * 2))
                                .noFluidInputs()
                                .noFluidOutputs()
                                .duration((tAmount * 32) * TICKS)
                                .eut(3 * tVoltageMultiplier)
                                .addTo(sExtruderRecipes);
                        }
                        if (tAmount * 2 <= 64) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(2L, aStack), ItemList.Shape_Mold_Casing.get(0L))
                                .itemOutputs(GT_ModHandler.getIC2Item("casingtin", tAmount * 3))
                                .noFluidInputs()
                                .noFluidOutputs()
                                .duration((tAmount * 128) * TICKS)
                                .eut(1 * tVoltageMultiplier)
                                .addTo(sAlloySmelterRecipes);
                        }
                    }
                    case "Lead" -> {
                        if (tAmount * 2 <= 64) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(1L, aStack), ItemList.Shape_Extruder_Casing.get(0L))
                                .itemOutputs(GT_ModHandler.getIC2Item("casinglead", tAmount * 2))
                                .noFluidInputs()
                                .noFluidOutputs()
                                .duration((tAmount * 32) * TICKS)
                                .eut(3 * tVoltageMultiplier)
                                .addTo(sExtruderRecipes);
                        }
                        if (tAmount * 2 <= 64) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(2L, aStack), ItemList.Shape_Mold_Casing.get(0L))
                                .itemOutputs(GT_ModHandler.getIC2Item("casinglead", tAmount * 3))
                                .noFluidInputs()
                                .noFluidOutputs()
                                .duration((tAmount * 128) * TICKS)
                                .eut(1 * tVoltageMultiplier)
                                .addTo(sAlloySmelterRecipes);
                        }
                    }
                    case "Copper", "AnnealedCopper" -> {
                        if (tAmount * 2 <= 64) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(1L, aStack), ItemList.Shape_Extruder_Casing.get(0L))
                                .itemOutputs(GT_ModHandler.getIC2Item("casingcopper", tAmount * 2))
                                .noFluidInputs()
                                .noFluidOutputs()
                                .duration((tAmount * 32) * TICKS)
                                .eut(3 * tVoltageMultiplier)
                                .addTo(sExtruderRecipes);
                        }
                        if (tAmount * 2 <= 64) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(2L, aStack), ItemList.Shape_Mold_Casing.get(0L))
                                .itemOutputs(GT_ModHandler.getIC2Item("casingcopper", tAmount * 3))
                                .noFluidInputs()
                                .noFluidOutputs()
                                .duration((tAmount * 128) * TICKS)
                                .eut(1 * tVoltageMultiplier)
                                .addTo(sAlloySmelterRecipes);
                        }
                    }
                    case "Bronze" -> {
                        if (tAmount * 2 <= 64) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(1L, aStack), ItemList.Shape_Extruder_Casing.get(0L))
                                .itemOutputs(GT_ModHandler.getIC2Item("casingbronze", tAmount * 2))
                                .noFluidInputs()
                                .noFluidOutputs()
                                .duration((tAmount * 32) * TICKS)
                                .eut(3 * tVoltageMultiplier)
                                .addTo(sExtruderRecipes);
                        }
                        if (tAmount * 2 <= 64) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(2L, aStack), ItemList.Shape_Mold_Casing.get(0L))
                                .itemOutputs(GT_ModHandler.getIC2Item("casingbronze", tAmount * 3))
                                .noFluidInputs()
                                .noFluidOutputs()
                                .duration((tAmount * 128) * TICKS)
                                .eut(1 * tVoltageMultiplier)
                                .addTo(sAlloySmelterRecipes);
                        }
                    }
                    case "Gold" -> {
                        if (tAmount * 2 <= 64) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(1L, aStack), ItemList.Shape_Extruder_Casing.get(0L))
                                .itemOutputs(GT_ModHandler.getIC2Item("casinggold", tAmount * 2))
                                .noFluidInputs()
                                .noFluidOutputs()
                                .duration((tAmount * 32) * TICKS)
                                .eut(3 * tVoltageMultiplier)
                                .addTo(sExtruderRecipes);
                        }
                        if (tAmount * 2 <= 64) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(GT_Utility.copyAmount(2L, aStack), ItemList.Shape_Mold_Casing.get(0L))
                                .itemOutputs(GT_ModHandler.getIC2Item("casinggold", tAmount * 3))
                                .noFluidInputs()
                                .noFluidOutputs()
                                .duration((tAmount * 128) * TICKS)
                                .eut(1 * tVoltageMultiplier)
                                .addTo(sAlloySmelterRecipes);
                        }
                    }
                    case "Polytetrafluoroethylene" -> {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1L, aStack), ItemList.Shape_Extruder_Cell.get(0L))
                            .itemOutputs(ItemList.Cell_Empty.get(tAmount * 4))
                            .noFluidInputs()
                            .noFluidOutputs()
                            .duration((tAmount * 128) * TICKS)
                            .eut(TierEU.RECIPE_LV)
                            .addTo(sExtruderRecipes);
                    }
                }
            }
        }
    }
}
