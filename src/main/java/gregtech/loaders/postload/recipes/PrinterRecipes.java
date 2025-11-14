package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.printerRecipes;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static net.minecraftforge.fluids.FluidRegistry.getFluidStack;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

@SuppressWarnings({ "PointlessArithmeticExpression" })
public class PrinterRecipes implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.Paper, 1L))
            .itemOutputs(ItemList.Paper_Punch_Card_Empty.get(1L))
            .fluidInputs(getFluidStack("squidink", 36))
            .duration(5 * SECONDS)
            .eut(2)
            .addTo(printerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Paper_Punch_Card_Empty.get(1L))
            .special(ItemList.Tool_DataStick.getWithName(0L, "With Punch Card Data"))
            .itemOutputs(ItemList.Paper_Punch_Card_Encoded.get(1L))
            .fluidInputs(getFluidStack("squidink", 36))
            .duration(5 * SECONDS)
            .eut(2)
            .addTo(printerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.plate, Materials.Paper, 3L))
            .special(ItemList.Tool_DataStick.getWithName(0L, "With Scanned Book Data"))
            .itemOutputs(ItemList.Paper_Printed_Pages.get(1L))
            .fluidInputs(getFluidStack("squidink", 1 * INGOTS))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(printerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.map, 1, 32767))
            .special(ItemList.Tool_DataStick.getWithName(0L, "With Scanned Map Data"))
            .itemOutputs(new ItemStack(Items.filled_map, 1, 0))
            .fluidInputs(getFluidStack("squidink", 1 * INGOTS))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(printerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.book, 1, 32767))
            .itemOutputs(GTUtility.getWrittenBook("Manual_Printer", ItemList.Book_Written_01.get(1L)))
            .fluidInputs(getFluidStack("squidink", 1 * INGOTS))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(printerRecipes);
    }
}
