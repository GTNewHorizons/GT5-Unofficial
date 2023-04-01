package gregtech.loaders.postload.recipes;

import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sPrinterRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static net.minecraftforge.fluids.FluidRegistry.getFluidStack;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

public class PrinterRecipes implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.stdBuilder().itemInputs(GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Paper, 1L))
                .itemOutputs(ItemList.Paper_Punch_Card_Empty.get(1L)).fluidInputs(getFluidStack("squidink", 36))
                .noFluidOutputs().duration(5 * SECONDS).eut(2).addTo(sPrinterRecipes);

        GT_Values.RA.stdBuilder().itemInputs(ItemList.Paper_Punch_Card_Empty.get(1L))
                .specialItem(ItemList.Tool_DataStick.getWithName(0L, "With Punch Card Data"))
                .itemOutputs(ItemList.Paper_Punch_Card_Encoded.get(1L)).fluidInputs(getFluidStack("squidink", 36))
                .noFluidOutputs().duration(5 * SECONDS).eut(2).addTo(sPrinterRecipes);

        GT_Values.RA.stdBuilder().itemInputs(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Paper, 3L))
                .specialItem(ItemList.Tool_DataStick.getWithName(0L, "With Scanned Book Data"))
                .itemOutputs(ItemList.Paper_Printed_Pages.get(1L)).fluidInputs(getFluidStack("squidink", 144))
                .noFluidOutputs().duration(20 * SECONDS).eut(2).addTo(sPrinterRecipes);

        GT_Values.RA.stdBuilder().itemInputs(new ItemStack(Items.map, 1, 32767))
                .specialItem(ItemList.Tool_DataStick.getWithName(0L, "With Scanned Map Data"))
                .itemOutputs(new ItemStack(Items.filled_map, 1, 0)).fluidInputs(getFluidStack("squidink", 144))
                .noFluidOutputs().duration(20 * SECONDS).eut(2).addTo(sPrinterRecipes);

        GT_Values.RA.stdBuilder().itemInputs(new ItemStack(Items.book, 1, 32767))
                .itemOutputs(GT_Utility.getWrittenBook("Manual_Printer", ItemList.Book_Written_01.get(1L)))
                .fluidInputs(getFluidStack("squidink", 144)).noFluidOutputs().duration(20 * SECONDS).eut(2)
                .addTo(sPrinterRecipes);
    }
}
