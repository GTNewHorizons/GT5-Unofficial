package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.cutterRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class ProcessingStone implements IOreRecipeRegistrator {

    public ProcessingStone() {
        OrePrefixes.stone.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        Block aBlock = GTUtility.getBlockFromStack(aStack);
        switch (aMaterial.mName) {
            case "NULL":
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(3, aStack), new ItemStack(Blocks.redstone_torch, 2))
                    .itemOutputs(new ItemStack(Items.repeater, 1))
                    .fluidInputs(Materials.Redstone.getMolten(1 * INGOTS))
                    .duration(5 * SECONDS)
                    .eut(48)
                    .addTo(assemblerRecipes);
                break;
            case "Sand":
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .itemOutputs(new ItemStack(Blocks.sand, 1, 0))
                    .duration(20 * SECONDS)
                    .eut(2)
                    .addTo(maceratorRecipes);
                break;
            case "Endstone":
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .itemOutputs(
                        GTOreDictUnificator.get(OrePrefixes.dustImpure, Materials.Endstone, 1L),
                        GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Tungstate, 1L))
                    .outputChances(10000, 500)
                    .duration(20 * SECONDS)
                    .eut(2)
                    .addTo(maceratorRecipes);
                break;
            case "Netherrack":
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .itemOutputs(
                        GTOreDictUnificator.get(OrePrefixes.dustImpure, Materials.Netherrack, 1L),
                        GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Gold, 1L))
                    .outputChances(10000, 500)
                    .duration(20 * SECONDS)
                    .eut(2)
                    .addTo(maceratorRecipes);
                break;
            case "NetherBrick":
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .circuit(1)
                    .itemOutputs(new ItemStack(Blocks.nether_brick_fence, 1))
                    .duration(5 * SECONDS)
                    .eut(4)
                    .addTo(assemblerRecipes);
                break;
            case "Obsidian":
                if (aBlock != Blocks.air) aBlock.setResistance(20.0F);
                break;
            case "Concrete":
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                    .fluidInputs(Materials.Water.getFluid(Math.max(4, Math.min(1000, 200 * 30 / 320))))
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(cutterRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                    .fluidInputs(GTModHandler.getDistilledWater(Math.max(3, Math.min(750, 200 * 30 / 426))))
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(cutterRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                    .fluidInputs(Materials.Lubricant.getFluid(Math.max(1, Math.min(250, 100 * 30 / 1280))))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(cutterRecipes);
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                    .fluidInputs(
                        Materials.DimensionallyShiftedSuperfluid.getFluid(Math.max(1, Math.min(10, 200 * 30 / 4000))))
                    .duration(4 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(cutterRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L))
                    .duration(20 * SECONDS)
                    .eut(2)
                    .addTo(maceratorRecipes);
                break;
            case "Andesite":
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .itemOutputs(
                        GTOreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 1L),
                        GTOreDictUnificator.get(OrePrefixes.dust, Materials.Obsidian, 1L))
                    .outputChances(10000, 2000)
                    .duration(20 * SECONDS)
                    .eut(2)
                    .addTo(maceratorRecipes);
                break;
            case "Soapstone":
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .itemOutputs(
                        GTOreDictUnificator.get(OrePrefixes.dustImpure, Materials.Talc, 1L),
                        GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Chromite, 1L))
                    .outputChances(10000, 1000)
                    .duration(20 * SECONDS)
                    .eut(2)
                    .addTo(maceratorRecipes);
                break;
            case "Migmatite":
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .itemOutputs(
                        GTOreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 1L),
                        GTOreDictUnificator.get(OrePrefixes.dustImpure, Materials.GraniteBlack, 1L))
                    .outputChances(10000, 5000)
                    .duration(20 * SECONDS)
                    .eut(2)
                    .addTo(maceratorRecipes);
                break;
            case "Redrock":
            case "Marble":
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                    .fluidInputs(Materials.Water.getFluid(Math.max(4, Math.min(1000, 400 * 30 / 320))))
                    .duration(20 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(cutterRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                    .fluidInputs(GTModHandler.getDistilledWater(Math.max(3, Math.min(750, 400 * 30 / 426))))
                    .duration(20 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(cutterRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                    .fluidInputs(Materials.Lubricant.getFluid(Math.max(1, Math.min(250, 200 * 30 / 1280))))
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(cutterRecipes);
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                    .fluidInputs(
                        Materials.DimensionallyShiftedSuperfluid.getFluid(Math.max(1, Math.min(10, 200 * 30 / 4000))))
                    .duration(4 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(cutterRecipes);
                break;
            case "Basalt":
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .circuit(3)
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                    .fluidInputs(Materials.Water.getFluid(Math.max(4, Math.min(1000, 400 * 30 / 320))))
                    .duration(20 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(cutterRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .circuit(3)
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                    .fluidInputs(GTModHandler.getDistilledWater(Math.max(3, Math.min(750, 400 * 30 / 426))))
                    .duration(20 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(cutterRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .circuit(3)
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                    .fluidInputs(Materials.Lubricant.getFluid(Math.max(1, Math.min(250, 200 * 30 / 1280))))
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(cutterRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .circuit(3)
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                    .fluidInputs(
                        Materials.DimensionallyShiftedSuperfluid.getFluid(Math.max(1, Math.min(10, 200 * 30 / 4000))))
                    .duration(4 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(cutterRecipes);

            case "Quartzite":
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .itemOutputs(
                        GTOreDictUnificator.get(OrePrefixes.dustImpure, aMaterial, 1L),
                        GTOreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L))
                    .outputChances(10000, 1000)
                    .duration(20 * SECONDS)
                    .eut(2)
                    .addTo(maceratorRecipes);
                break;
            case "Flint":
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .itemOutputs(
                        GTOreDictUnificator.get(OrePrefixes.dustImpure, aMaterial, 2L),
                        new ItemStack(Items.flint, 1))
                    .outputChances(10000, 5000)
                    .duration(20 * SECONDS)
                    .eut(2)
                    .addTo(maceratorRecipes);
                break;
            case "GraniteBlack":
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                    .fluidInputs(Materials.Water.getFluid(Math.max(4, Math.min(1000, 400 * 30 / 320))))
                    .duration(20 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(cutterRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                    .fluidInputs(GTModHandler.getDistilledWater(Math.max(3, Math.min(750, 400 * 30 / 426))))
                    .duration(20 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(cutterRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                    .fluidInputs(Materials.Lubricant.getFluid(Math.max(1, Math.min(250, 200 * 30 / 1280))))
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(cutterRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                    .fluidInputs(
                        Materials.DimensionallyShiftedSuperfluid.getFluid(Math.max(1, Math.min(10, 200 * 30 / 4000))))
                    .duration(4 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(cutterRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .itemOutputs(
                        GTOreDictUnificator.get(OrePrefixes.dustImpure, aMaterial, 1L),
                        GTOreDictUnificator.get(OrePrefixes.dust, Materials.Thorium, 1L))
                    .outputChances(10000, 100)
                    .duration(20 * SECONDS)
                    .eut(2)
                    .addTo(maceratorRecipes);
                break;
            case "GraniteRed":
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                    .fluidInputs(Materials.Water.getFluid(Math.max(4, Math.min(1000, 400 * 30 / 320))))
                    .duration(20 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(cutterRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                    .fluidInputs(GTModHandler.getDistilledWater(Math.max(3, Math.min(750, 400 * 30 / 426))))
                    .duration(20 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(cutterRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                    .fluidInputs(Materials.Lubricant.getFluid(Math.max(1, Math.min(250, 200 * 30 / 1280))))
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(cutterRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                    .fluidInputs(
                        Materials.DimensionallyShiftedSuperfluid.getFluid(Math.max(1, Math.min(10, 200 * 30 / 4000))))
                    .duration(4 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(cutterRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .itemOutputs(
                        GTOreDictUnificator.get(OrePrefixes.dustImpure, aMaterial, 1L),
                        GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Uranium, 1L))
                    .outputChances(10000, 100)
                    .duration(20 * SECONDS)
                    .eut(2)
                    .addTo(maceratorRecipes);
        }
    }
}
