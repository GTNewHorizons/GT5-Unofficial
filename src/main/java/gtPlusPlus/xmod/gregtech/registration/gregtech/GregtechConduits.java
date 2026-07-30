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
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.PipeMaterials;
import gregtech.api.enums.materials.PipeShapes;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gregtech.api.util.StringUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class GregtechConduits {

    private static FluidStack fluidStackOf(Material material, long amount) {
        return MaterialUtils.hasMolten(material) ? MaterialUtils.molten(material, amount)
            : MaterialUtils.fluid(material, amount);
    }

    public static void generatePipeRecipes(final Material material) {
        // generatePipeRecipes multiplies the voltage multiplier by 8 because ??! reasons.
        generatePipeRecipes(material, MaterialUtils.localName(material), MaterialUtils.voltageMultiplier(material) / 8);
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

        if (material != null && MaterialUtils.voltageMultiplier(material) <= TierEU.RECIPE_IV) {
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

    /// Whether `material` carries the whole `wireGt01`..`wireGt16` ladder [PipeMaterials] grants as
    /// one unit -- the precondition every wire block in [#generateWireRecipes] shares.
    private static boolean hasWireLadder(Material material) {
        for (Shape shape : PipeMaterials.wireShapes()) {
            if (!material.hasShape(shape)) return false;
        }
        return true;
    }

    /// The wire recipes gtPlusPlus generated for its own conduit materials: the wiremill fine-wire pair, and
    /// -- for a material carrying the wire ladder -- the wiremill wire ladders, the wire extruder recipe, the
    /// shapeless size conversions, and the assembler wire-combining ladder.
    ///
    /// Fine wire is drawn straight from the ingot and the rod, so those two recipes depend only on
    /// `ingot`/`stick`/`wireFine`; most callers carry none of the wire ladder and reach only those two.
    /// Everything else needs the ladder, whose membership comes from [PipeMaterials]: it grants
    /// `wireGt01`..`wireGt16` together, so [#hasWireLadder] covers every wire size below it. Cables are absent
    /// by the same table -- it grants `cableGt01`..`cableGt16` only to its `WireCable` rows, and no caller here
    /// is one. [gregtech.loaders.oreprocessing.ProcessingWire] owns wire-to-cable for the materials that have
    /// cables.
    public static void generateWireRecipes(Material material) {
        boolean hasIngot = material.hasShape(Shapes.ingot);
        boolean hasStick = material.hasShape(Shapes.stick);
        boolean hasFineWire = material.hasShape(Shapes.wireFine);

        if (hasIngot && hasFineWire) {
            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(material, Shapes.ingot, 1))
                .circuit(3)
                .itemOutputs(MaterialLibAPI.getStack(material, Shapes.wireFine, 8))
                .duration(5 * SECONDS)
                .eut(4)
                .addTo(wiremillRecipes);
        }

        if (hasStick && hasFineWire) {
            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(material, Shapes.stick, 1))
                .circuit(3)
                .itemOutputs(MaterialLibAPI.getStack(material, Shapes.wireFine, 4))
                .duration(2 * SECONDS + 10 * TICKS)
                .eut(4)
                .addTo(wiremillRecipes);
        }

        if (!hasWireLadder(material)) return;

        ItemStack wire01 = MaterialLibAPI.getStack(material, PipeShapes.wireGt01, 1);
        ItemStack wire02 = MaterialLibAPI.getStack(material, PipeShapes.wireGt02, 1);
        ItemStack wire04 = MaterialLibAPI.getStack(material, PipeShapes.wireGt04, 1);
        ItemStack wire08 = MaterialLibAPI.getStack(material, PipeShapes.wireGt08, 1);
        ItemStack wire12 = MaterialLibAPI.getStack(material, PipeShapes.wireGt12, 1);
        ItemStack wire16 = MaterialLibAPI.getStack(material, PipeShapes.wireGt16, 1);

        // Adds manual crafting recipe
        if (material.hasShape(Shapes.plate) && MaterialUtils.voltageMultiplier(material) < 7680) {
            GTModHandler.addCraftingRecipe(
                wire01,
                GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "Px ", "   ", "   ", 'P', MaterialLibAPI.getStack(material, Shapes.plate, 1) });
        }

        // Wire mill
        if (hasIngot) {
            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(material, Shapes.ingot, 1))
                .circuit(1)
                .itemOutputs(MaterialLibAPI.getStack(material, PipeShapes.wireGt01, 2))
                .duration(5 * SECONDS)
                .eut(4)
                .addTo(wiremillRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(material, Shapes.ingot, 1))
                .circuit(2)
                .itemOutputs(wire02)
                .duration(7 * SECONDS + 10 * TICKS)
                .eut(4)
                .addTo(wiremillRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(material, Shapes.ingot, 2))
                .circuit(4)
                .itemOutputs(wire04)
                .duration(10 * SECONDS)
                .eut(4)
                .addTo(wiremillRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(material, Shapes.ingot, 4))
                .circuit(8)
                .itemOutputs(wire08)
                .duration(12 * SECONDS + 10 * TICKS)
                .eut(4)
                .addTo(wiremillRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(material, Shapes.ingot, 6))
                .circuit(12)
                .itemOutputs(wire12)
                .duration(15 * SECONDS)
                .eut(4)
                .addTo(wiremillRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(material, Shapes.ingot, 8))
                .circuit(16)
                .itemOutputs(wire16)
                .duration(17 * SECONDS + 10 * TICKS)
                .eut(4)
                .addTo(wiremillRecipes);
        }

        if (hasStick) {
            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(material, Shapes.stick, 1))
                .circuit(1)
                .itemOutputs(wire01)
                .duration(2 * SECONDS + 10 * TICKS)
                .eut(4)
                .addTo(wiremillRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(material, Shapes.stick, 2))
                .circuit(2)
                .itemOutputs(wire02)
                .duration(5 * SECONDS)
                .eut(4)
                .addTo(wiremillRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(material, Shapes.stick, 4))
                .circuit(4)
                .itemOutputs(wire04)
                .duration(7 * SECONDS + 10 * TICKS)
                .eut(4)
                .addTo(wiremillRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(material, Shapes.stick, 8))
                .circuit(8)
                .itemOutputs(wire08)
                .duration(10 * SECONDS)
                .eut(4)
                .addTo(wiremillRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(material, Shapes.stick, 12))
                .circuit(12)
                .itemOutputs(wire12)
                .duration(12 * SECONDS + 10 * TICKS)
                .eut(4)
                .addTo(wiremillRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(material, Shapes.stick, 16))
                .circuit(16)
                .itemOutputs(wire16)
                .duration(15 * SECONDS)
                .eut(4)
                .addTo(wiremillRecipes);
        }

        if (hasFineWire) {
            GTValues.RA.stdBuilder()
                .itemInputs(wire01)
                .circuit(1)
                .itemOutputs(MaterialLibAPI.getStack(material, Shapes.wireFine, 4))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(wiremillRecipes);
        }

        // Extruder
        if (hasIngot) {
            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(material, Shapes.ingot, 1), ItemList.Shape_Extruder_Wire.get(0))
                .itemOutputs(MaterialLibAPI.getStack(material, PipeShapes.wireGt01, 2))
                .duration(9 * SECONDS + 16 * TICKS)
                .eut(96)
                .addTo(extruderRecipes);
        }

        // Shapeless down-crafting
        GTModHandler.addShapelessCraftingRecipe(
            MaterialLibAPI.getStack(material, PipeShapes.wireGt01, 2),
            GTModHandler.RecipeBits.BUFFERED,
            new ItemStack[] { wire02 });
        GTModHandler.addShapelessCraftingRecipe(
            MaterialLibAPI.getStack(material, PipeShapes.wireGt01, 4),
            GTModHandler.RecipeBits.BUFFERED,
            new ItemStack[] { wire04 });
        GTModHandler.addShapelessCraftingRecipe(
            MaterialLibAPI.getStack(material, PipeShapes.wireGt01, 8),
            GTModHandler.RecipeBits.BUFFERED,
            new ItemStack[] { wire08 });
        GTModHandler.addShapelessCraftingRecipe(
            MaterialLibAPI.getStack(material, PipeShapes.wireGt01, 12),
            GTModHandler.RecipeBits.BUFFERED,
            new ItemStack[] { wire12 });
        GTModHandler.addShapelessCraftingRecipe(
            MaterialLibAPI.getStack(material, PipeShapes.wireGt01, 16),
            GTModHandler.RecipeBits.BUFFERED,
            new ItemStack[] { wire16 });

        // Shapeless up-crafting
        GTModHandler
            .addShapelessCraftingRecipe(wire02, GTModHandler.RecipeBits.BUFFERED, new ItemStack[] { wire01, wire01 });
        GTModHandler
            .addShapelessCraftingRecipe(wire04, GTModHandler.RecipeBits.BUFFERED, new ItemStack[] { wire02, wire02 });
        GTModHandler
            .addShapelessCraftingRecipe(wire08, GTModHandler.RecipeBits.BUFFERED, new ItemStack[] { wire04, wire04 });
        GTModHandler
            .addShapelessCraftingRecipe(wire12, GTModHandler.RecipeBits.BUFFERED, new ItemStack[] { wire04, wire08 });
        GTModHandler
            .addShapelessCraftingRecipe(wire16, GTModHandler.RecipeBits.BUFFERED, new ItemStack[] { wire04, wire12 });
        GTModHandler
            .addShapelessCraftingRecipe(wire16, GTModHandler.RecipeBits.BUFFERED, new ItemStack[] { wire08, wire08 });
        GTModHandler.addShapelessCraftingRecipe(
            wire04,
            GTModHandler.RecipeBits.BUFFERED,
            new ItemStack[] { wire01, wire01, wire01, wire01 });
        GTModHandler.addShapelessCraftingRecipe(
            wire08,
            GTModHandler.RecipeBits.BUFFERED,
            new ItemStack[] { wire01, wire01, wire01, wire01, wire01, wire01, wire01, wire01 });

        // Assemble small wires into bigger wires
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(material, PipeShapes.wireGt01, 2))
            .circuit(2)
            .itemOutputs(wire02)
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(material, PipeShapes.wireGt01, 4))
            .circuit(4)
            .itemOutputs(wire04)
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(material, PipeShapes.wireGt01, 8))
            .circuit(8)
            .itemOutputs(wire08)
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(material, PipeShapes.wireGt01, 12))
            .circuit(12)
            .itemOutputs(wire12)
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(material, PipeShapes.wireGt01, 16))
            .circuit(16)
            .itemOutputs(wire16)
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(assemblerRecipes);
    }
}
