package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.recipe.RecipeMaps.*;
import static gregtech.api.util.GTRecipeBuilder.HALF_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gregtech.api.util.StringUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class GregtechConduits {

    private static long voltageMultiplier(Material material) {
        Long multiplier = material.getProperty(GTMaterialProperties.VOLTAGE_MULTIPLIER);
        return multiplier != null ? multiplier : 16L;
    }

    private static FluidStack fluidStackOf(Material material, long amount) {
        return MU.hasMolten(material) ? MU.molten(material, amount) : MU.fluid(material, amount);
    }

    public static void generatePipeRecipes(final Material material) {
        // generatePipeRecipes multiplies the voltage multiplier by 8 because ??! reasons.
        generatePipeRecipes(material, MU.localName(material), voltageMultiplier(material) / 8);
    }

    public static void generatePipeRecipes(final Material material, final String materialName, final long vMulti) {

        String output = materialName.substring(0, 1)
            .toUpperCase() + materialName.substring(1);
        output = StringUtils.sanitizeString(output);

        if (output.equals("VoidMetal")) {
            output = "Void";
        }

        ItemStack pipeIngot = ItemUtils.getItemStackOfAmountFromOreDict("ingot" + output, 1);
        ItemStack pipePlate = ItemUtils.getItemStackOfAmountFromOreDict("plate" + output, 1);

        if (pipeIngot == null) {
            if (pipePlate != null) {
                pipeIngot = pipePlate;
            }
        }

        int eut = (int) (8 * vMulti);

        if (material != null && voltageMultiplier(material) <= TierEU.RECIPE_IV) {
            // Add the Four Shaped Recipes First
            GTModHandler.addCraftingRecipe(
                ItemUtils.getItemStackOfAmountFromOreDict("pipe" + "Tiny" + output, 8),
                GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "PPP", "h w", "PPP", 'P', pipePlate });

            GTModHandler.addCraftingRecipe(
                ItemUtils.getItemStackOfAmountFromOreDict("pipe" + "Small" + output, 6),
                GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "PwP", "P P", "PhP", 'P', pipePlate });

            GTModHandler.addCraftingRecipe(
                ItemUtils.getItemStackOfAmountFromOreDict("pipe" + "Medium" + output, 2),
                GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "PPP", "w h", "PPP", 'P', pipePlate });

            GTModHandler.addCraftingRecipe(
                ItemUtils.getItemStackOfAmountFromOreDict("pipe" + "Large" + output, 1),
                GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "PhP", "P P", "PwP", 'P', pipePlate });
        }

        if (pipeIngot != null) {
            // 1 Clay Plate = 1 Clay Dust = 2 Clay Ball
            int inputMultiplier = materialName.equals("Clay") ? 2 : 1;
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTUtility.copyAmount(1 * inputMultiplier, pipeIngot),
                    ItemList.Shape_Extruder_Pipe_Tiny.get(0))
                .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDictNoBroken("pipe" + "Tiny" + output, 2))
                .duration(5 * TICKS)
                .eut(eut)
                .addTo(extruderRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTUtility.copyAmount(1 * inputMultiplier, pipeIngot),
                    ItemList.Shape_Extruder_Pipe_Small.get(0))
                .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("pipe" + "Small" + output, 1))
                .duration(10 * TICKS)
                .eut(eut)
                .addTo(extruderRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTUtility.copyAmount(3 * inputMultiplier, pipeIngot),
                    ItemList.Shape_Extruder_Pipe_Medium.get(0))
                .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("pipe" + "Medium" + output, 1))
                .duration(20 * TICKS)
                .eut(eut)
                .addTo(extruderRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTUtility.copyAmount(6 * inputMultiplier, pipeIngot),
                    ItemList.Shape_Extruder_Pipe_Large.get(0))
                .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("pipe" + "Large" + output, 1))
                .duration(2 * SECONDS)
                .eut(eut)
                .addTo(extruderRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTUtility.copyAmount(12 * inputMultiplier, pipeIngot),
                    ItemList.Shape_Extruder_Pipe_Huge.get(0))
                .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("pipe" + "Huge" + output, 1))
                .duration(4 * SECONDS)
                .eut(eut)
                .addTo(extruderRecipes);

        }

        if ((eut < 512) && !output.equals("Void")) {
            ItemStack pipePlateDouble = ItemUtils.getItemStackOfAmountFromOreDict("plateDouble" + output, 1);
            if (pipePlateDouble != null) {
                GTModHandler.addCraftingRecipe(
                    ItemUtils.getItemStackOfAmountFromOreDict("pipe" + "Huge" + output, 1),
                    GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "DhD", "D D", "DwD", 'D', pipePlateDouble.copy() });
            }
        }

        if (material != null && fluidStackOf(material, 1) != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Mold_Pipe_Tiny.get(0L))
                .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDictNoBroken("pipe" + "Tiny" + output, 1))
                .fluidInputs(fluidStackOf(material, 1 * HALF_INGOTS))
                .duration(1 * SECONDS)
                .eut(eut)
                .addTo(fluidSolidifierRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Mold_Pipe_Small.get(0L))
                .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("pipe" + "Small" + output, 1))
                .fluidInputs(fluidStackOf(material, 1 * INGOTS))
                .duration(2 * SECONDS)
                .eut(eut)
                .addTo(fluidSolidifierRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Mold_Pipe_Medium.get(0L))
                .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("pipe" + "Medium" + output, 1))
                .fluidInputs(fluidStackOf(material, 3 * INGOTS))
                .duration(4 * SECONDS)
                .eut(eut)
                .addTo(fluidSolidifierRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Mold_Pipe_Large.get(0L))
                .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("pipe" + "Large" + output, 1))
                .fluidInputs(fluidStackOf(material, 6 * INGOTS))
                .duration(8 * SECONDS)
                .eut(eut)
                .addTo(fluidSolidifierRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Mold_Pipe_Huge.get(0L))
                .itemOutputs(ItemUtils.getItemStackOfAmountFromOreDict("pipe" + "Huge" + output, 1))
                .fluidInputs(fluidStackOf(material, 12 * INGOTS))
                .duration(16 * SECONDS)
                .eut(eut)
                .addTo(fluidSolidifierRecipes);
        }
    }

    public static boolean generateWireRecipes(Material aMaterial) {

        ItemStack aPlate = MU.stack(OrePrefixes.plate, aMaterial, 1);
        ItemStack aIngot = MU.stack(OrePrefixes.ingot, aMaterial, 1);
        ItemStack aRod = MU.stack(OrePrefixes.stick, aMaterial, 1);
        ItemStack aWire01 = MU.stack(OrePrefixes.wireGt01, aMaterial, 1);
        ItemStack aWire02 = MU.stack(OrePrefixes.wireGt02, aMaterial, 1);
        ItemStack aWire04 = MU.stack(OrePrefixes.wireGt04, aMaterial, 1);
        ItemStack aWire08 = MU.stack(OrePrefixes.wireGt08, aMaterial, 1);
        ItemStack aWire12 = MU.stack(OrePrefixes.wireGt12, aMaterial, 1);
        ItemStack aWire16 = MU.stack(OrePrefixes.wireGt16, aMaterial, 1);
        ItemStack aCable01 = MU.stack(OrePrefixes.cableGt01, aMaterial, 1);
        ItemStack aCable02 = MU.stack(OrePrefixes.cableGt02, aMaterial, 1);
        ItemStack aCable04 = MU.stack(OrePrefixes.cableGt04, aMaterial, 1);
        ItemStack aCable08 = MU.stack(OrePrefixes.cableGt08, aMaterial, 1);
        ItemStack aCable12 = MU.stack(OrePrefixes.cableGt12, aMaterial, 1);
        ItemStack aCable16 = MU.stack(OrePrefixes.cableGt16, aMaterial, 1);
        ItemStack aFineWire = MU.stack(OrePrefixes.wireFine, aMaterial, 1);

        // Adds manual crafting recipe
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aPlate, aWire01 })
            && voltageMultiplier(aMaterial) < 7680) {
            GTModHandler.addCraftingRecipe(
                aWire01,
                GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "Px ", "   ", "   ", 'P', aPlate });
        }

        // Wire mill
        if (ItemUtils
            .checkForInvalidItems(new ItemStack[] { aIngot, aWire01, aWire02, aWire04, aWire08, aWire12, aWire16 })) {
            GTValues.RA.stdBuilder()
                .itemInputs(MU.stack(OrePrefixes.ingot, aMaterial, 1))
                .circuit(1)
                .itemOutputs(MU.stack(OrePrefixes.wireGt01, aMaterial, 2))
                .duration(5 * SECONDS)
                .eut(4)
                .addTo(wiremillRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(MU.stack(OrePrefixes.ingot, aMaterial, 1))
                .circuit(2)
                .itemOutputs(MU.stack(OrePrefixes.wireGt02, aMaterial, 1))
                .duration(7 * SECONDS + 10 * TICKS)
                .eut(4)
                .addTo(wiremillRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(MU.stack(OrePrefixes.ingot, aMaterial, 2))
                .circuit(4)
                .itemOutputs(MU.stack(OrePrefixes.wireGt04, aMaterial, 1))
                .duration(10 * SECONDS)
                .eut(4)
                .addTo(wiremillRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(MU.stack(OrePrefixes.ingot, aMaterial, 4))
                .circuit(8)
                .itemOutputs(MU.stack(OrePrefixes.wireGt08, aMaterial, 1))
                .duration(12 * SECONDS + 10 * TICKS)
                .eut(4)
                .addTo(wiremillRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(MU.stack(OrePrefixes.ingot, aMaterial, 6))
                .circuit(12)
                .itemOutputs(MU.stack(OrePrefixes.wireGt12, aMaterial, 1))
                .duration(15 * SECONDS)
                .eut(4)
                .addTo(wiremillRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(MU.stack(OrePrefixes.ingot, aMaterial, 8))
                .circuit(16)
                .itemOutputs(MU.stack(OrePrefixes.wireGt16, aMaterial, 1))
                .duration(17 * SECONDS + 10 * TICKS)
                .eut(4)
                .addTo(wiremillRecipes);

        }

        if (ItemUtils
            .checkForInvalidItems(new ItemStack[] { aRod, aWire01, aWire02, aWire04, aWire08, aWire12, aWire16 })) {
            GTValues.RA.stdBuilder()
                .itemInputs(MU.stack(OrePrefixes.stick, aMaterial, 1))
                .circuit(1)
                .itemOutputs(MU.stack(OrePrefixes.wireGt01, aMaterial, 1))
                .duration(2 * SECONDS + 10 * TICKS)
                .eut(4)
                .addTo(wiremillRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(MU.stack(OrePrefixes.stick, aMaterial, 2))
                .circuit(2)
                .itemOutputs(MU.stack(OrePrefixes.wireGt02, aMaterial, 1))
                .duration(5 * SECONDS)
                .eut(4)
                .addTo(wiremillRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(MU.stack(OrePrefixes.stick, aMaterial, 4))
                .circuit(4)
                .itemOutputs(MU.stack(OrePrefixes.wireGt04, aMaterial, 1))
                .duration(7 * SECONDS + 10 * TICKS)
                .eut(4)
                .addTo(wiremillRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(MU.stack(OrePrefixes.stick, aMaterial, 8))
                .circuit(8)
                .itemOutputs(MU.stack(OrePrefixes.wireGt08, aMaterial, 1))
                .duration(10 * SECONDS)
                .eut(4)
                .addTo(wiremillRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(MU.stack(OrePrefixes.stick, aMaterial, 12))
                .circuit(12)
                .itemOutputs(MU.stack(OrePrefixes.wireGt12, aMaterial, 1))
                .duration(12 * SECONDS + 10 * TICKS)
                .eut(4)
                .addTo(wiremillRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(MU.stack(OrePrefixes.stick, aMaterial, 16))
                .circuit(16)
                .itemOutputs(MU.stack(OrePrefixes.wireGt16, aMaterial, 1))
                .duration(15 * SECONDS)
                .eut(4)
                .addTo(wiremillRecipes);

        }

        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aIngot, aFineWire })) {
            GTValues.RA.stdBuilder()
                .itemInputs(MU.stack(OrePrefixes.ingot, aMaterial, 1))
                .circuit(3)
                .itemOutputs(MU.stack(OrePrefixes.wireFine, aMaterial, 8))
                .duration(5 * SECONDS)
                .eut(4)
                .addTo(wiremillRecipes);

        }

        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aRod, aFineWire })) {
            GTValues.RA.stdBuilder()
                .itemInputs(MU.stack(OrePrefixes.stick, aMaterial, 1))
                .circuit(3)
                .itemOutputs(MU.stack(OrePrefixes.wireFine, aMaterial, 4))
                .duration(2 * SECONDS + 10 * TICKS)
                .eut(4)
                .addTo(wiremillRecipes);

        }

        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire01, aFineWire })) {
            GTValues.RA.stdBuilder()
                .itemInputs(MU.stack(OrePrefixes.wireGt01, aMaterial, 1))
                .circuit(1)
                .itemOutputs(MU.stack(OrePrefixes.wireFine, aMaterial, 4))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(wiremillRecipes);

        }

        // Extruder
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aIngot, aWire01 })) {
            GTValues.RA.stdBuilder()
                .itemInputs(aIngot, ItemList.Shape_Extruder_Wire.get(0))
                .itemOutputs(MU.stack(OrePrefixes.wireGt01, aMaterial, 2))
                .duration(9 * SECONDS + 16 * TICKS)
                .eut(96)
                .addTo(extruderRecipes);
        }

        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aCable01, aWire01 })) {
            GTValues.RA.stdBuilder()
                .itemInputs(aCable01)
                .itemOutputs(aWire01)
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(unpackagerRecipes);
        }

        // Shapeless Down-Crafting
        // 2x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire01, aWire02 })) {
            GTModHandler.addShapelessCraftingRecipe(
                MU.stack(OrePrefixes.wireGt01, aMaterial, 2),
                GTModHandler.RecipeBits.BUFFERED,
                new ItemStack[] { aWire02 });
        }

        // 4x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire01, aWire04 })) {
            GTModHandler.addShapelessCraftingRecipe(
                MU.stack(OrePrefixes.wireGt01, aMaterial, 4),
                GTModHandler.RecipeBits.BUFFERED,
                new ItemStack[] { aWire04 });
        }

        // 8x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire01, aWire08 })) {
            GTModHandler.addShapelessCraftingRecipe(
                MU.stack(OrePrefixes.wireGt01, aMaterial, 8),
                GTModHandler.RecipeBits.BUFFERED,
                new ItemStack[] { aWire08 });
        }

        // 12x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire01, aWire12 })) {
            GTModHandler.addShapelessCraftingRecipe(
                MU.stack(OrePrefixes.wireGt01, aMaterial, 12),
                GTModHandler.RecipeBits.BUFFERED,
                new ItemStack[] { aWire12 });
        }

        // 16x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire01, aWire16 })) {
            GTModHandler.addShapelessCraftingRecipe(
                MU.stack(OrePrefixes.wireGt01, aMaterial, 16),
                GTModHandler.RecipeBits.BUFFERED,
                new ItemStack[] { aWire16 });
        }

        // 1x -> 2x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire01, aWire02 })) {
            GTModHandler.addShapelessCraftingRecipe(
                aWire02,
                GTModHandler.RecipeBits.BUFFERED,
                new ItemStack[] { aWire01, aWire01 });
        }

        // 2x -> 4x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire02, aWire04 })) {
            GTModHandler.addShapelessCraftingRecipe(
                aWire04,
                GTModHandler.RecipeBits.BUFFERED,
                new ItemStack[] { aWire02, aWire02 });
        }

        // 4x -> 8x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire04, aWire08 })) {
            GTModHandler.addShapelessCraftingRecipe(
                aWire08,
                GTModHandler.RecipeBits.BUFFERED,
                new ItemStack[] { aWire04, aWire04 });
        }

        // 8x -> 12x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire04, aWire08, aWire12 })) {
            GTModHandler.addShapelessCraftingRecipe(
                aWire12,
                GTModHandler.RecipeBits.BUFFERED,
                new ItemStack[] { aWire04, aWire08 });
        }

        // 12x -> 16x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire04, aWire12, aWire16 })) {
            GTModHandler.addShapelessCraftingRecipe(
                aWire16,
                GTModHandler.RecipeBits.BUFFERED,
                new ItemStack[] { aWire04, aWire12 });
        }

        // 8x -> 16x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire08, aWire16 })) {
            GTModHandler.addShapelessCraftingRecipe(
                aWire16,
                GTModHandler.RecipeBits.BUFFERED,
                new ItemStack[] { aWire08, aWire08 });
        }

        // 1x -> 4x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire01, aWire04 })) {
            GTModHandler.addShapelessCraftingRecipe(
                aWire04,
                GTModHandler.RecipeBits.BUFFERED,
                new ItemStack[] { aWire01, aWire01, aWire01, aWire01 });
        }

        // 1x -> 8x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire01, aWire08 })) {
            GTModHandler.addShapelessCraftingRecipe(
                aWire08,
                GTModHandler.RecipeBits.BUFFERED,
                new ItemStack[] { aWire01, aWire01, aWire01, aWire01, aWire01, aWire01, aWire01, aWire01 });
        }

        // Wire to Cable
        // 1x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire01, aCable01 })) {
            GTValues.RA.stdBuilder()
                .itemInputs(aWire01)
                .circuit(24)
                .itemOutputs(aCable01)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.Rubber,
                        Materials2FluidShapes.fluidMolten,
                        (int) (1 * INGOTS)))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(assemblerRecipes);

        }

        // 2x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire02, aCable02 })) {
            GTValues.RA.stdBuilder()
                .itemInputs(aWire02)
                .circuit(24)
                .itemOutputs(aCable02)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.Rubber,
                        Materials2FluidShapes.fluidMolten,
                        (int) (1 * INGOTS)))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(assemblerRecipes);

        }

        // 4x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire04, aCable04 })) {
            GTValues.RA.stdBuilder()
                .itemInputs(aWire04)
                .circuit(24)
                .itemOutputs(aCable04)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.Rubber,
                        Materials2FluidShapes.fluidMolten,
                        (int) (2 * INGOTS)))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(assemblerRecipes);

        }

        // 8x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire08, aCable08 })) {
            GTValues.RA.stdBuilder()
                .itemInputs(aWire08)
                .circuit(24)
                .itemOutputs(aCable08)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.Rubber,
                        Materials2FluidShapes.fluidMolten,
                        (int) (3 * INGOTS)))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(assemblerRecipes);

        }

        // 12x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire12, aCable12 })) {
            GTValues.RA.stdBuilder()
                .itemInputs(aWire12)
                .circuit(24)
                .itemOutputs(aCable12)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.Rubber,
                        Materials2FluidShapes.fluidMolten,
                        (int) (4 * INGOTS)))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(assemblerRecipes);

        }

        // 16x
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire16, aCable16 })) {
            GTValues.RA.stdBuilder()
                .itemInputs(aWire16)
                .circuit(24)
                .itemOutputs(aCable16)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.Rubber,
                        Materials2FluidShapes.fluidMolten,
                        (int) (5 * INGOTS)))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(assemblerRecipes);

        }

        // Assemble small wires into bigger wires
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { aWire01, aWire02 })) {
            GTValues.RA.stdBuilder()
                .itemInputs(MU.stack(OrePrefixes.wireGt01, aMaterial, 2))
                .circuit(2)
                .itemOutputs(aWire02)
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(assemblerRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(MU.stack(OrePrefixes.wireGt01, aMaterial, 4))
                .circuit(4)
                .itemOutputs(aWire04)
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(assemblerRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(MU.stack(OrePrefixes.wireGt01, aMaterial, 8))
                .circuit(8)
                .itemOutputs(aWire08)
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(assemblerRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(MU.stack(OrePrefixes.wireGt01, aMaterial, 12))
                .circuit(12)
                .itemOutputs(aWire12)
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(assemblerRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(MU.stack(OrePrefixes.wireGt01, aMaterial, 16))
                .circuit(16)
                .itemOutputs(aWire16)
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(assemblerRecipes);
        }

        return true;
    }
}
