package gregtech.loaders.oreprocessing;

import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sAssemblerRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sCutterRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sLatheRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

public class ProcessingPlank implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingPlank() {
        OrePrefixes.plank.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        if (aOreDictName.startsWith("plankWood")) {
            GT_Values.RA.stdBuilder()
                .itemInputs(GT_Utility.copyAmount(1L, aStack))
                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(10 * TICKS)
                .eut(8)
                .addTo(sLatheRecipes);
            // todo: not actually in the game. removed somewhere? better remove here then.
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(8L, aStack),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L))
                .itemOutputs(new ItemStack(Blocks.noteblock, 1))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(10 * SECONDS)
                .eut(4)
                .addTo(sAssemblerRecipes);
            // todo: not actually in the game. removed somewhere? better remove here then.
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(8L, aStack),
                    GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Diamond, 1L))
                .itemOutputs(new ItemStack(Blocks.jukebox, 1))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(20 * SECONDS)
                .eut(4)
                .addTo(sAssemblerRecipes);
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(1L, aStack),
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Iron, 1L))
                .itemOutputs(ItemList.Crate_Empty.get(1L))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(10 * SECONDS)
                .eut(1)
                .addTo(sAssemblerRecipes);
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(1L, aStack),
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.WroughtIron, 1L))
                .itemOutputs(ItemList.Crate_Empty.get(1L))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(10 * SECONDS)
                .eut(1)
                .addTo(sAssemblerRecipes);
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(1L, aStack),
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Steel, 1L))
                .itemOutputs(ItemList.Crate_Empty.get(1L))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(10 * SECONDS)
                .eut(1)
                .addTo(sAssemblerRecipes);
            // todo: not actually in the game. removed somewhere? better remove here then.
            GT_Values.RA.stdBuilder()
                .itemInputs(GT_Utility.copyAmount(1L, aStack), GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(new ItemStack(Blocks.wooden_button, 1))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(5 * SECONDS)
                .eut(4)
                .addTo(sAssemblerRecipes);
            // todo: not actually in the game. removed somewhere? better remove here then.
            GT_Values.RA.stdBuilder()
                .itemInputs(GT_Utility.copyAmount(2L, aStack), GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(new ItemStack(Blocks.wooden_pressure_plate, 1))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(5 * SECONDS)
                .eut(4)
                .addTo(sAssemblerRecipes);
            // todo: not actually in the game. removed somewhere? better remove here then.
            GT_Values.RA.stdBuilder()
                .itemInputs(GT_Utility.copyAmount(3L, aStack), GT_Utility.getIntegratedCircuit(3))
                .itemOutputs(new ItemStack(Blocks.trapdoor, 1))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(15 * SECONDS)
                .eut(4)
                .addTo(sAssemblerRecipes);
            // todo: not actually in the game. removed somewhere? better remove here then.
            GT_Values.RA.stdBuilder()
                .itemInputs(GT_Utility.copyAmount(4L, aStack), GT_Utility.getIntegratedCircuit(4))
                .itemOutputs(new ItemStack(Blocks.crafting_table, 1))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(15 * SECONDS)
                .eut(4)
                .addTo(sAssemblerRecipes);
            // todo: not actually in the game. removed somewhere? better remove here then.
            GT_Values.RA.stdBuilder()
                .itemInputs(GT_Utility.copyAmount(6L, aStack), GT_Utility.getIntegratedCircuit(6))
                .itemOutputs(new ItemStack(Items.wooden_door, 1))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(30 * SECONDS)
                .eut(4)
                .addTo(sAssemblerRecipes);
            GT_Values.RA.stdBuilder()
                .itemInputs(GT_Utility.copyAmount(8L, aStack), GT_Utility.getIntegratedCircuit(8))
                .itemOutputs(new ItemStack(Blocks.chest, 1))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(40 * SECONDS)
                .eut(4)
                .addTo(sAssemblerRecipes);
            GT_Values.RA.stdBuilder()
                .itemInputs(GT_Utility.copyAmount(6L, aStack), new ItemStack(Items.book, 3))
                .itemOutputs(new ItemStack(Blocks.bookshelf, 1))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(20 * SECONDS)
                .eut(4)
                .addTo(sAssemblerRecipes);

            if (aStack.getItemDamage() == 32767) {
                for (byte i = 0; i < 64; i = (byte) (i + 1)) {
                    ItemStack tStack = GT_Utility.copyMetaData(i, aStack);
                    // Get Recipe and Output, add recipe to delayed removal
                    ItemStack tOutput = GT_ModHandler.getRecipeOutput(tStack, tStack, tStack);
                    if ((tOutput != null) && (tOutput.stackSize >= 3)) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1L, tStack))
                            .itemOutputs(GT_Utility.copyAmount(tOutput.stackSize / 3, tOutput))
                            .fluidInputs(Materials.Water.getFluid(4))
                            .noFluidOutputs()
                            .duration(2 * 25 * TICKS)
                            .eut(4)
                            .addTo(sCutterRecipes);
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1L, tStack))
                            .itemOutputs(GT_Utility.copyAmount(tOutput.stackSize / 3, tOutput))
                            .fluidInputs(GT_ModHandler.getDistilledWater(3))
                            .noFluidOutputs()
                            .duration(2 * 25 * TICKS)
                            .eut(4)
                            .addTo(sCutterRecipes);
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1L, tStack))
                            .itemOutputs(GT_Utility.copyAmount(tOutput.stackSize / 3, tOutput))
                            .fluidInputs(Materials.Lubricant.getFluid(1))
                            .noFluidOutputs()
                            .duration(25 * TICKS)
                            .eut(4)
                            .addTo(sCutterRecipes);
                        GT_ModHandler.removeRecipeDelayed(tStack, tStack, tStack);
                        GT_ModHandler.addCraftingRecipe(
                            GT_Utility.copyAmount(tOutput.stackSize / 3, tOutput),
                            GT_ModHandler.RecipeBits.BUFFERED,
                            new Object[] { "sP", 'P', tStack });
                    }
                    if ((tStack == null) && (i >= 16)) break;
                }
            } else {
                ItemStack tOutput = !aModName.equalsIgnoreCase("thaumcraft")
                    ? GT_ModHandler.getRecipeOutput(aStack, aStack, aStack)
                    : GT_ModHandler.getRecipeOutputNoOreDict(aStack, aStack, aStack);
                if ((tOutput != null) && (tOutput.stackSize >= 3)) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1L, aStack))
                        .itemOutputs(GT_Utility.copyAmount(tOutput.stackSize / 3, tOutput))
                        .fluidInputs(Materials.Water.getFluid(4))
                        .noFluidOutputs()
                        .duration(2 * 25)
                        .eut(4)
                        .addTo(sCutterRecipes);
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1L, aStack))
                        .itemOutputs(GT_Utility.copyAmount(tOutput.stackSize / 3, tOutput))
                        .fluidInputs(GT_ModHandler.getDistilledWater(3))
                        .noFluidOutputs()
                        .duration(2 * 25)
                        .eut(4)
                        .addTo(sCutterRecipes);
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1L, aStack))
                        .itemOutputs(GT_Utility.copyAmount(tOutput.stackSize / 3, tOutput))
                        .fluidInputs(Materials.Lubricant.getFluid(1))
                        .noFluidOutputs()
                        .duration(25)
                        .eut(4)
                        .addTo(sCutterRecipes);
                    GT_ModHandler.removeRecipeDelayed(aStack, aStack, aStack);
                    GT_ModHandler.addCraftingRecipe(
                        GT_Utility.copyAmount(tOutput.stackSize / 3, tOutput),
                        GT_ModHandler.RecipeBits.BUFFERED,
                        new Object[] { "sP", 'P', aStack });
                }
            }
        }
    }
}
