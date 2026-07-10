package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.enums.Mods.TinkerConstruct;
import static gregtech.api.recipe.RecipeMaps.fluidExtractionRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.HALF_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.NUGGETS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.loaders.misc.GTBees.combs;
import static net.minecraftforge.fluids.FluidRegistry.getFluidStack;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.recipe.RecipeCategories;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;

@SuppressWarnings({ "PointlessArithmeticExpression" })
public class FluidExtractorRecipes implements Runnable {

    @Override
    public void run() {

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.stone, Materials.GraniteBlack, 1))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.GraniteBlack,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (1 * INGOTS)))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(4)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.stone, Materials.GraniteRed, 1))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.GraniteRed,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (1 * INGOTS)))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(4)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Dye_SquidInk.get(1L))
            .fluidOutputs(getFluidStack("squidink", 1 * INGOTS))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(4)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Dye_Indigo.get(1L))
            .fluidOutputs(getFluidStack("indigo", 1 * INGOTS))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(4)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.fish, 1, 0))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.FishOil, Materials2FluidShapes.shapeFluidLiquid, (int) (40L)))
            .duration(16 * TICKS)
            .eut(4)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.fish, 1, 1))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.FishOil, Materials2FluidShapes.shapeFluidLiquid, (int) (60L)))
            .duration(16 * TICKS)
            .eut(4)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.fish, 1, 2))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.FishOil, Materials2FluidShapes.shapeFluidLiquid, (int) (70L)))
            .duration(16 * TICKS)
            .eut(4)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.fish, 1, 3))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.FishOil, Materials2FluidShapes.shapeFluidLiquid, (int) (30L)))
            .duration(16 * TICKS)
            .eut(4)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.coal, 1, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.shapeDust, (int) (1L)))
            .outputChances(1000)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.WoodTar, Materials2FluidShapes.shapeFluidLiquid, (int) (100L)))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Wood, Materials2Shapes.shapeDust, (int) (1L)))
            .itemOutputs(ItemList.IC2_Plantball.get(1L))
            .outputChances(100)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Creosote, Materials2FluidShapes.shapeFluidLiquid, (int) (5L)))
            .duration(16 * TICKS)
            .eut(4)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.HydratedCoal, Materials2Shapes.shapeDust, (int) (1L)))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Coal, Materials2Shapes.shapeDust, (int) (1L)))
            .outputChances(10000)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Water, Materials2FluidShapes.shapeFluidLiquid, (int) (100L)))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(4)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Mercury, Materials2Shapes.shapeGem, (int) (1L)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Mercury, Materials2FluidShapes.shapeFluidLiquid, (int) (1_000)))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(4)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Monazite, Materials2Shapes.shapeDust, (int) (1L)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Helium, Materials2FluidShapes.shapeFluidGas, (int) (200L)))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.ReinforcedGlass.get(1L))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.ReinforcedGlass,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (1 * INGOTS)))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(NewHorizonsCoreMod.ID, "ReinforcedGlassPlate", 1L, 0))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.ReinforcedGlass,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (1 * HALF_INGOTS)))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(NewHorizonsCoreMod.ID, "ReinforcedGlassLense", 1L, 0))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.ReinforcedGlass,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (54)))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Long_Distance_Pipeline_Fluid.get(1L))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Steel,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (19 * INGOTS)))
            .duration(20 * SECONDS)
            .eut(90)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Long_Distance_Pipeline_Item.get(1L))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.shapeIngot, (int) (7L)))
            .outputChances(10000)
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Tin,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (12 * INGOTS)))
            .duration(20 * SECONDS)
            .eut(90)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Long_Distance_Pipeline_Fluid_Pipe.get(4L))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Steel, Materials2FluidShapes.shapeFluidMolten, (int) (189)))
            .duration(2 * SECONDS)
            .eut(90)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Long_Distance_Pipeline_Item_Pipe.get(16L))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Tin, Materials2Shapes.shapeIngot, (int) (3L)))
            .outputChances(10000)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Steel, Materials2FluidShapes.shapeFluidMolten, (int) (324)))
            .duration(20 * SECONDS)
            .eut(90)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Quartzite, Materials2Shapes.shapeDust, (int) (1L)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Glass,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (1 * HALF_INGOTS)))
            .duration(30 * SECONDS)
            .eut(28)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 0))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Iron,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (2 * INGOTS)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 1))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Iron,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (1 * INGOTS)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 2))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.shapeNugget, (int) (6)))
            .outputChances(10000)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Bronze, Materials2FluidShapes.shapeFluidMolten, (int) (1728)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 13))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Steel,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (2 * INGOTS)))
            .duration(20 * SECONDS)
            .eut(90)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 14))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Steel,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (1 * INGOTS)))
            .duration(20 * SECONDS)
            .eut(90)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 15))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Steel, Materials2FluidShapes.shapeFluidMolten, (int) (1836)))
            .duration(20 * SECONDS)
            .eut(90)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 0))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Aluminium,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (2 * INGOTS)))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 1))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Aluminium,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (1 * INGOTS)))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 2))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.shapeDust, (int) (12L)))
            .outputChances(10000)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Aluminium, Materials2FluidShapes.shapeFluidMolten, (int) (108L)))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 3))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.StainlessSteel,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (2 * INGOTS)))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 4))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.StainlessSteel,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (1 * INGOTS)))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 5))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.StainlessSteel,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (1836)))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 6))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Titanium,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (2 * INGOTS)))
            .duration(35 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 7))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Titanium,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (1 * INGOTS)))
            .duration(35 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 8))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Titanium, Materials2FluidShapes.shapeFluidMolten, (int) (1836)))
            .duration(35 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 9))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.TungstenSteel,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (2 * INGOTS)))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 10))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.TungstenSteel,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (1 * INGOTS)))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 11))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.TungstenSteel,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (1836)))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 12))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Palladium,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (2 * INGOTS)))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 13))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Palladium,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (1 * INGOTS)))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 14))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Chrome, Materials2Shapes.shapeNugget, (int) (6L)))
            .outputChances(10000)
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.NiobiumTitanium,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (1728)))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 0))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Iridium,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (2 * INGOTS)))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 1))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Iridium,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (1 * INGOTS)))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 2))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Iridium, Materials2Shapes.shapeNugget, (int) (6L)))
            .outputChances(10000)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Enderium, Materials2FluidShapes.shapeFluidMolten, (int) (1728)))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 3))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Osmium,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (2 * INGOTS)))
            .duration(55 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 4))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Osmium,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (1 * INGOTS)))
            .duration(55 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 5))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Osmium, Materials2Shapes.shapeNugget, (int) (6L)))
            .outputChances(10000)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Naquadah, Materials2FluidShapes.shapeFluidMolten, (int) (1728)))
            .duration(55 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 6))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Neutronium,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (2 * INGOTS)))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 7))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Neutronium,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (1 * INGOTS)))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 8))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Neutronium,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (1836)))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.wheat_seeds, 1, 32767))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SeedOil, Materials2FluidShapes.shapeFluidLiquid, (int) (10)))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(2)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.melon_seeds, 1, 32767))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SeedOil, Materials2FluidShapes.shapeFluidLiquid, (int) (10)))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(2)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.pumpkin_seeds, 1, 32767))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SeedOil, Materials2FluidShapes.shapeFluidLiquid, (int) (10)))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(2)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.snowball, 1, 0))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Water, Materials2FluidShapes.shapeFluidLiquid, (int) (250)))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(4)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.snow, 1, 0))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Water, Materials2FluidShapes.shapeFluidLiquid, (int) (1_000)))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(4)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Ice, Materials2Shapes.shapeDust, (int) (1L)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Ice, Materials2FluidShapes.shapeFluidSolid, (int) (1000L)))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(4)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Forestry.ID, "phosphor", 1L))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Phosphorus, Materials2Shapes.shapeDust, (int) (1L)))
            .outputChances(1000)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Lava, Materials2FluidShapes.shapeFluidLiquid, (int) (800L)))
            .duration(12 * SECONDS + 16 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getModItem(TinkerConstruct.ID, "oreBerries", 1L, 0))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Iron,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (1 * NUGGETS)))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getModItem(TinkerConstruct.ID, "oreBerries", 1L, 1))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Gold,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (1 * NUGGETS)))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getModItem(TinkerConstruct.ID, "oreBerries", 1L, 2))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Copper,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (1 * NUGGETS)))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getModItem(TinkerConstruct.ID, "oreBerries", 1L, 3))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Tin,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (1 * NUGGETS)))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getModItem(TinkerConstruct.ID, "oreBerries", 1L, 4))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Aluminium,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (1 * NUGGETS)))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(fluidExtractionRecipes);

        if (Forestry.isModLoaded()) {
            // Beecombs fluid extractor recipes
            // xenon
            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(combs, 1, 134))
                .fluidOutputs(getFluidStack("xenon", 250))
                .duration(2 * SECONDS + 10 * TICKS)
                .eut(TierEU.RECIPE_IV)
                .addTo(fluidExtractionRecipes);

            // neon
            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(combs, 1, 135))
                .fluidOutputs(getFluidStack("neon", 250))
                .duration(15 * TICKS)
                .eut(TierEU.RECIPE_IV)
                .addTo(fluidExtractionRecipes);

            // krpton
            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(combs, 1, 136))
                .fluidOutputs(getFluidStack("krypton", 250))
                .duration(1 * SECONDS + 5 * TICKS)
                .eut(TierEU.RECIPE_IV)
                .addTo(fluidExtractionRecipes);
        }
    }
}
