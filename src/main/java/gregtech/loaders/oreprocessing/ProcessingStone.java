package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.cutterRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

public class ProcessingStone implements IOreRecipeRegistrator {

    public ProcessingStone() {
        OrePrefixes.stone.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        Block aBlock = GT_Utility.getBlockFromStack(aStack);
        switch (aMaterial.mName) {
            case "NULL":
                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(3, aStack), new ItemStack(Blocks.redstone_torch, 2))
                    .itemOutputs(new ItemStack(Items.repeater, 1))
                    .fluidInputs(Materials.Redstone.getMolten(144L))
                    .duration(5 * SECONDS)
                    .eut(48)
                    .addTo(assemblerRecipes);
                break;
            case "Sand":
                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(1, aStack))
                    .itemOutputs(new ItemStack(Blocks.sand, 1, 0))
                    .duration(20 * SECONDS)
                    .eut(2)
                    .addTo(maceratorRecipes);
                break;
            case "Endstone":
                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(1, aStack))
                    .itemOutputs(
                        GT_OreDictUnificator.get(OrePrefixes.dustImpure, Materials.Endstone, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Tungstate, 1L))
                    .outputChances(10000, 500)
                    .duration(20 * SECONDS)
                    .eut(2)
                    .addTo(maceratorRecipes);
                break;
            case "Netherrack":
                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(1, aStack))
                    .itemOutputs(
                        GT_OreDictUnificator.get(OrePrefixes.dustImpure, Materials.Netherrack, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Gold, 1L))
                    .outputChances(10000, 500)
                    .duration(20 * SECONDS)
                    .eut(2)
                    .addTo(maceratorRecipes);
                break;
            case "NetherBrick":
                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(1, aStack), GT_Utility.getIntegratedCircuit(1))
                    .itemOutputs(new ItemStack(Blocks.nether_brick_fence, 1))
                    .duration(5 * SECONDS)
                    .eut(4)
                    .addTo(assemblerRecipes);
                break;
            case "Obsidian":
                if (aBlock != Blocks.air) aBlock.setResistance(20.0F);
                break;
            case "Concrete":
                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(1, aStack))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                    .fluidInputs(Materials.Water.getFluid(Math.max(4, Math.min(1000, 200 * 30 / 320))))
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(cutterRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(1, aStack))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                    .fluidInputs(GT_ModHandler.getDistilledWater(Math.max(3, Math.min(750, 200 * 30 / 426))))
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(cutterRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(1, aStack))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                    .fluidInputs(Materials.Lubricant.getFluid(Math.max(1, Math.min(250, 100 * 30 / 1280))))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(cutterRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(1, aStack))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L))
                    .duration(20 * SECONDS)
                    .eut(2)
                    .addTo(maceratorRecipes);
                break;
            case "Rhyolite":
                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(1, aStack))
                    .itemOutputs(
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.PotassiumFeldspar, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Quartz, 1L))
                    .outputChances(10000, 2000)
                    .duration(20 * SECONDS)
                    .eut(2)
                    .addTo(maceratorRecipes);
                break;
            case "Komatiite":
                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(1, aStack))
                    .itemOutputs(
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Biotite, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Uranium, 1L))
                    .outputChances(10000, 500)
                    .duration(20 * SECONDS)
                    .eut(2)
                    .addTo(maceratorRecipes);
                break;
            case "Dacite":
            case "Andesite":
                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(1, aStack))
                    .itemOutputs(
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Obsidian, 1L))
                    .outputChances(10000, 2000)
                    .duration(20 * SECONDS)
                    .eut(2)
                    .addTo(maceratorRecipes);
                break;
            case "Gabbro":
                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(1, aStack))
                    .itemOutputs(
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.PotassiumFeldspar, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Pyrite, 1L))
                    .outputChances(10000, 2000)
                    .duration(20 * SECONDS)
                    .eut(2)
                    .addTo(maceratorRecipes);
                break;
            case "Eclogite":
                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(1, aStack))
                    .itemOutputs(
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Rutile, 1L))
                    .outputChances(10000, 1000)
                    .duration(20 * SECONDS)
                    .eut(2)
                    .addTo(maceratorRecipes);
                break;
            case "Soapstone":
                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(1, aStack))
                    .itemOutputs(
                        GT_OreDictUnificator.get(OrePrefixes.dustImpure, Materials.Talc, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Chromite, 1L))
                    .outputChances(10000, 1000)
                    .duration(20 * SECONDS)
                    .eut(2)
                    .addTo(maceratorRecipes);
                break;
            case "Greenschist":
            case "Blueschist":
                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(1, aStack))
                    .itemOutputs(
                        GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Glauconite, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Basalt, 1L))
                    .outputChances(10000, 1000)
                    .duration(20 * SECONDS)
                    .eut(2)
                    .addTo(maceratorRecipes);
                break;
            case "Gneiss":
            case "Migmatite":
                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(1, aStack))
                    .itemOutputs(
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dustImpure, Materials.GraniteBlack, 1L))
                    .outputChances(10000, 5000)
                    .duration(20 * SECONDS)
                    .eut(2)
                    .addTo(maceratorRecipes);
                break;
            case "Redrock":
            case "Marble":
                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(1, aStack))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                    .fluidInputs(Materials.Water.getFluid(Math.max(4, Math.min(1000, 400 * 30 / 320))))
                    .duration(20 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(cutterRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(1, aStack))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                    .fluidInputs(GT_ModHandler.getDistilledWater(Math.max(3, Math.min(750, 400 * 30 / 426))))
                    .duration(20 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(cutterRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(1, aStack))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                    .fluidInputs(Materials.Lubricant.getFluid(Math.max(1, Math.min(250, 200 * 30 / 1280))))
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(cutterRecipes);
            case "Basalt":
                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(1, aStack), GT_Utility.getIntegratedCircuit(3))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                    .fluidInputs(Materials.Water.getFluid(Math.max(4, Math.min(1000, 400 * 30 / 320))))
                    .duration(20 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(cutterRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(1, aStack), GT_Utility.getIntegratedCircuit(3))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                    .fluidInputs(GT_ModHandler.getDistilledWater(Math.max(3, Math.min(750, 400 * 30 / 426))))
                    .duration(20 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(cutterRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(1, aStack), GT_Utility.getIntegratedCircuit(3))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                    .fluidInputs(Materials.Lubricant.getFluid(Math.max(1, Math.min(250, 200 * 30 / 1280))))
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(cutterRecipes);
            case "Quartzite":
                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(1, aStack))
                    .itemOutputs(
                        GT_OreDictUnificator.get(OrePrefixes.dustImpure, aMaterial, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L))
                    .outputChances(10000, 1000)
                    .duration(20 * SECONDS)
                    .eut(2)
                    .addTo(maceratorRecipes);
                break;
            case "Flint":
                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(1, aStack))
                    .itemOutputs(
                        GT_OreDictUnificator.get(OrePrefixes.dustImpure, aMaterial, 2L),
                        new ItemStack(Items.flint, 1))
                    .outputChances(10000, 5000)
                    .duration(20 * SECONDS)
                    .eut(2)
                    .addTo(maceratorRecipes);
                break;
            case "GraniteBlack":
                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(1, aStack))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                    .fluidInputs(Materials.Water.getFluid(Math.max(4, Math.min(1000, 400 * 30 / 320))))
                    .duration(20 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(cutterRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(1, aStack))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                    .fluidInputs(GT_ModHandler.getDistilledWater(Math.max(3, Math.min(750, 400 * 30 / 426))))
                    .duration(20 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(cutterRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(1, aStack))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                    .fluidInputs(Materials.Lubricant.getFluid(Math.max(1, Math.min(250, 200 * 30 / 1280))))
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(cutterRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(1, aStack))
                    .itemOutputs(
                        GT_OreDictUnificator.get(OrePrefixes.dustImpure, aMaterial, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Thorium, 1L))
                    .outputChances(10000, 100)
                    .duration(20 * SECONDS)
                    .eut(2)
                    .addTo(maceratorRecipes);
                break;
            case "GraniteRed":
                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(1, aStack))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                    .fluidInputs(Materials.Water.getFluid(Math.max(4, Math.min(1000, 400 * 30 / 320))))
                    .duration(20 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(cutterRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(1, aStack))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                    .fluidInputs(GT_ModHandler.getDistilledWater(Math.max(3, Math.min(750, 400 * 30 / 426))))
                    .duration(20 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(cutterRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(1, aStack))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L))
                    .fluidInputs(Materials.Lubricant.getFluid(Math.max(1, Math.min(250, 200 * 30 / 1280))))
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(cutterRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(1, aStack))
                    .itemOutputs(
                        GT_OreDictUnificator.get(OrePrefixes.dustImpure, aMaterial, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Uranium, 1L))
                    .outputChances(10000, 100)
                    .duration(20 * SECONDS)
                    .eut(2)
                    .addTo(maceratorRecipes);
        }
    }
}
