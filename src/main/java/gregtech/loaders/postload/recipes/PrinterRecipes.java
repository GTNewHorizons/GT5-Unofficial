package gregtech.loaders.postload.recipes;

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
        GT_Values.RA.addPrinterRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Paper, 1L),
                getFluidStack("squidink", 36),
                GT_Values.NI,
                ItemList.Paper_Punch_Card_Empty.get(1L),
                100,
                2);
        GT_Values.RA.addPrinterRecipe(
                ItemList.Paper_Punch_Card_Empty.get(1L),
                getFluidStack("squidink", 36),
                ItemList.Tool_DataStick.getWithName(0L, "With Punch Card Data"),
                ItemList.Paper_Punch_Card_Encoded.get(1L),
                100,
                2);
        GT_Values.RA.addPrinterRecipe(
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Paper, 3L),
                getFluidStack("squidink", 144),
                ItemList.Tool_DataStick.getWithName(0L, "With Scanned Book Data"),
                ItemList.Paper_Printed_Pages.get(1L),
                400,
                2);
        GT_Values.RA.addPrinterRecipe(
                new ItemStack(Items.map, 1, 32767),
                getFluidStack("squidink", 144),
                ItemList.Tool_DataStick.getWithName(0L, "With Scanned Map Data"),
                new ItemStack(Items.filled_map, 1, 0),
                400,
                2);
        GT_Values.RA.addPrinterRecipe(
                new ItemStack(Items.book, 1, 32767),
                getFluidStack("squidink", 144),
                GT_Values.NI,
                GT_Utility.getWrittenBook("Manual_Printer", ItemList.Book_Written_01.get(1L)),
                400,
                2);
    }
}
