package gregtech.loaders.oreprocessing;

import static gregtech.api.enums.Mods.IndustrialCraft2;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sBrewingRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sCompressorRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sMaceratorRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sSlicerRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;

public class ProcessingCrop implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingCrop() {
        OrePrefixes.crop.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        net.minecraft.item.ItemStack aStack) {
        // Compressor recipes
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(gregtech.api.util.GT_Utility.copyAmount(8L, aStack))
                .itemOutputs(ItemList.IC2_PlantballCompressed.get(1L))
                .duration(15 * SECONDS)
                .eut(2)
                .addTo(sCompressorRecipes);
        }

        Fluid[] waterArray;

        if (IndustrialCraft2.isModLoaded()) {
            waterArray = new Fluid[] { FluidRegistry.WATER, GT_ModHandler.getDistilledWater(1L)
                .getFluid() };
        } else {
            waterArray = new Fluid[] { FluidRegistry.WATER };
        }

        switch (aOreDictName) {
            case "cropTea" -> {
                for (Fluid tFluid : waterArray) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(aStack)
                        .fluidInputs(new FluidStack(tFluid, 750))
                        .fluidOutputs(new FluidStack(FluidRegistry.getFluid("potion.tea"), 750))
                        .duration(6 * SECONDS + 8 * TICKS)
                        .eut(4)
                        .addTo(sBrewingRecipes);
                }
            }
            case "cropGrape" -> {
                for (Fluid tFluid : waterArray) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(aStack)
                        .fluidInputs(new FluidStack(tFluid, 750))
                        .fluidOutputs(new FluidStack(FluidRegistry.getFluid("potion.grapejuice"), 750))
                        .duration(6 * SECONDS + 8 * TICKS)
                        .eut(4)
                        .addTo(sBrewingRecipes);
                }
            }
            case "cropChilipepper" -> GT_Values.RA.stdBuilder()
                .itemInputs(aStack)
                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chili, 1L))
                .duration(20 * SECONDS)
                .eut(2)
                .addTo(sMaceratorRecipes);
            case "cropCoffee" -> GT_Values.RA.stdBuilder()
                .itemInputs(aStack)
                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coffee, 1L))
                .duration(20 * SECONDS)
                .eut(2)
                .addTo(sMaceratorRecipes);
            case "cropPotato" -> {
                GT_Values.RA.stdBuilder()
                    .itemInputs(aStack, ItemList.Shape_Slicer_Flat.get(0))
                    .itemOutputs(ItemList.Food_Raw_PotatoChips.get(1L))
                    .duration(3 * SECONDS + 4 * TICKS)
                    .eut(4)
                    .addTo(sSlicerRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(aStack, ItemList.Shape_Slicer_Stripes.get(0L))
                    .itemOutputs(ItemList.Food_Raw_Fries.get(1L))
                    .duration(3 * SECONDS + 4 * TICKS)
                    .eut(4)
                    .addTo(sSlicerRecipes);

                for (Fluid tFluid : waterArray) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(aStack)
                        .fluidInputs(new FluidStack(tFluid, 750))
                        .fluidOutputs(new FluidStack(FluidRegistry.getFluid("potion.potatojuice"), 750))
                        .duration(6 * SECONDS + 8 * TICKS)
                        .eut(4)
                        .addTo(sBrewingRecipes);
                }
            }
            case "cropLemon" -> {
                GT_Values.RA.stdBuilder()
                    .itemInputs(aStack, ItemList.Shape_Slicer_Flat.get(0))
                    .itemOutputs(ItemList.Food_Sliced_Lemon.get(4L))
                    .duration(3 * SECONDS + 4 * TICKS)
                    .eut(4)
                    .addTo(sSlicerRecipes);

                for (Fluid tFluid : waterArray) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(aStack)
                        .fluidInputs(new FluidStack(tFluid, 750))
                        .fluidOutputs(new FluidStack(FluidRegistry.getFluid("potion.lemonjuice"), 750))
                        .duration(6 * SECONDS + 8 * TICKS)
                        .eut(4)
                        .addTo(sBrewingRecipes);
                }

                GT_Values.RA.stdBuilder()
                    .itemInputs(aStack)
                    .fluidInputs(new FluidStack(FluidRegistry.getFluid("potion.vodka"), 750))
                    .fluidOutputs(new FluidStack(FluidRegistry.getFluid("potion.leninade"), 750))
                    .duration(6 * SECONDS + 8 * TICKS)
                    .eut(4)
                    .addTo(sBrewingRecipes);
            }
            case "cropTomato" -> GT_Values.RA.stdBuilder()
                .itemInputs(aStack, ItemList.Shape_Slicer_Flat.get(0))
                .itemOutputs(ItemList.Food_Sliced_Tomato.get(4L))
                .duration(3 * SECONDS + 4 * TICKS)
                .eut(4)
                .addTo(sSlicerRecipes);
            case "cropCucumber" -> GT_Values.RA.stdBuilder()
                .itemInputs(aStack, ItemList.Shape_Slicer_Flat.get(0))
                .itemOutputs(ItemList.Food_Sliced_Cucumber.get(4L))
                .duration(3 * SECONDS + 4 * TICKS)
                .eut(4)
                .addTo(sSlicerRecipes);
            case "cropOnion" -> GT_Values.RA.stdBuilder()
                .itemInputs(aStack, ItemList.Shape_Slicer_Flat.get(0))
                .itemOutputs(ItemList.Food_Sliced_Onion.get(4L))
                .duration(3 * SECONDS + 4 * TICKS)
                .eut(4)
                .addTo(sSlicerRecipes);
        }
    }
}
