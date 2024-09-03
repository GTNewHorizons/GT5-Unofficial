package gregtech.loaders.oreprocessing;

import static gregtech.api.enums.Mods.IndustrialCraft2;
import static gregtech.api.recipe.RecipeMaps.brewingRecipes;
import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.recipe.RecipeMaps.slicerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class ProcessingCrop implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingCrop() {
        OrePrefixes.crop.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        net.minecraft.item.ItemStack aStack) {
        // Compressor recipes
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(8, aStack))
            .itemOutputs(ItemList.IC2_PlantballCompressed.get(1L))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);

        Fluid[] waterArray;

        if (IndustrialCraft2.isModLoaded()) {
            waterArray = new Fluid[] { FluidRegistry.WATER, GTModHandler.getDistilledWater(1L)
                .getFluid() };
        } else {
            waterArray = new Fluid[] { FluidRegistry.WATER };
        }

        switch (aOreDictName) {
            case "cropTea" -> {
                for (Fluid tFluid : waterArray) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(aStack)
                        .fluidInputs(new FluidStack(tFluid, 750))
                        .fluidOutputs(new FluidStack(FluidRegistry.getFluid("potion.tea"), 750))
                        .duration(6 * SECONDS + 8 * TICKS)
                        .eut(4)
                        .addTo(brewingRecipes);
                }
            }
            case "cropGrape" -> {
                for (Fluid tFluid : waterArray) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(aStack)
                        .fluidInputs(new FluidStack(tFluid, 750))
                        .fluidOutputs(new FluidStack(FluidRegistry.getFluid("potion.grapejuice"), 750))
                        .duration(6 * SECONDS + 8 * TICKS)
                        .eut(4)
                        .addTo(brewingRecipes);
                }
            }
            case "cropChilipepper" -> GTValues.RA.stdBuilder()
                .itemInputs(aStack)
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Chili, 1L))
                .duration(20 * SECONDS)
                .eut(2)
                .addTo(maceratorRecipes);
            case "cropCoffee" -> GTValues.RA.stdBuilder()
                .itemInputs(aStack)
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Coffee, 1L))
                .duration(20 * SECONDS)
                .eut(2)
                .addTo(maceratorRecipes);
            case "cropPotato" -> {
                GTValues.RA.stdBuilder()
                    .itemInputs(aStack, ItemList.Shape_Slicer_Flat.get(0))
                    .itemOutputs(ItemList.Food_Raw_PotatoChips.get(1L))
                    .duration(3 * SECONDS + 4 * TICKS)
                    .eut(4)
                    .addTo(slicerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(aStack, ItemList.Shape_Slicer_Stripes.get(0L))
                    .itemOutputs(ItemList.Food_Raw_Fries.get(1L))
                    .duration(3 * SECONDS + 4 * TICKS)
                    .eut(4)
                    .addTo(slicerRecipes);

                for (Fluid tFluid : waterArray) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(aStack)
                        .fluidInputs(new FluidStack(tFluid, 750))
                        .fluidOutputs(new FluidStack(FluidRegistry.getFluid("potion.potatojuice"), 750))
                        .duration(6 * SECONDS + 8 * TICKS)
                        .eut(4)
                        .addTo(brewingRecipes);
                }
            }
            case "cropLemon" -> {
                GTValues.RA.stdBuilder()
                    .itemInputs(aStack, ItemList.Shape_Slicer_Flat.get(0))
                    .itemOutputs(ItemList.Food_Sliced_Lemon.get(4L))
                    .duration(3 * SECONDS + 4 * TICKS)
                    .eut(4)
                    .addTo(slicerRecipes);

                for (Fluid tFluid : waterArray) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(aStack)
                        .fluidInputs(new FluidStack(tFluid, 750))
                        .fluidOutputs(new FluidStack(FluidRegistry.getFluid("potion.lemonjuice"), 750))
                        .duration(6 * SECONDS + 8 * TICKS)
                        .eut(4)
                        .addTo(brewingRecipes);
                }

                GTValues.RA.stdBuilder()
                    .itemInputs(aStack)
                    .fluidInputs(new FluidStack(FluidRegistry.getFluid("potion.vodka"), 750))
                    .fluidOutputs(new FluidStack(FluidRegistry.getFluid("potion.leninade"), 750))
                    .duration(6 * SECONDS + 8 * TICKS)
                    .eut(4)
                    .addTo(brewingRecipes);
            }
            case "cropTomato" -> GTValues.RA.stdBuilder()
                .itemInputs(aStack, ItemList.Shape_Slicer_Flat.get(0))
                .itemOutputs(ItemList.Food_Sliced_Tomato.get(4L))
                .duration(3 * SECONDS + 4 * TICKS)
                .eut(4)
                .addTo(slicerRecipes);
            case "cropCucumber" -> GTValues.RA.stdBuilder()
                .itemInputs(aStack, ItemList.Shape_Slicer_Flat.get(0))
                .itemOutputs(ItemList.Food_Sliced_Cucumber.get(4L))
                .duration(3 * SECONDS + 4 * TICKS)
                .eut(4)
                .addTo(slicerRecipes);
            case "cropOnion" -> GTValues.RA.stdBuilder()
                .itemInputs(aStack, ItemList.Shape_Slicer_Flat.get(0))
                .itemOutputs(ItemList.Food_Sliced_Onion.get(4L))
                .duration(3 * SECONDS + 4 * TICKS)
                .eut(4)
                .addTo(slicerRecipes);
        }
    }
}
