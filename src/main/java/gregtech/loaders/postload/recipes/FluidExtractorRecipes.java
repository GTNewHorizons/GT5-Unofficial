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
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.recipe.RecipeCategories;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

@SuppressWarnings({ "PointlessArithmeticExpression" })
public class FluidExtractorRecipes implements Runnable {

    @Override
    public void run() {

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.stone, Materials.GraniteBlack, 1))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.GraniteBlack, FluidShapes.fluidMolten, INGOTS))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(4)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.stone, Materials.GraniteRed, 1))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.GraniteRed, FluidShapes.fluidMolten, INGOTS))
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
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.FishOil, FluidShapes.fluidLiquid, 40))
            .duration(16 * TICKS)
            .eut(4)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.fish, 1, 1))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.FishOil, FluidShapes.fluidLiquid, 60))
            .duration(16 * TICKS)
            .eut(4)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.fish, 1, 2))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.FishOil, FluidShapes.fluidLiquid, 70))
            .duration(16 * TICKS)
            .eut(4)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.fish, 1, 3))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.FishOil, FluidShapes.fluidLiquid, 30))
            .duration(16 * TICKS)
            .eut(4)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.coal, 1, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, 1))
            .outputChances(1000)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.WoodTar, FluidShapes.fluidLiquid, 100))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Wood, Shapes.dust, 1))
            .itemOutputs(ItemList.IC2_Plantball.get(1L))
            .outputChances(100)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Creosote, FluidShapes.fluidLiquid, 5))
            .duration(16 * TICKS)
            .eut(4)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.HydratedCoal, Shapes.dust, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Coal, Shapes.dust, 1))
            .outputChances(10000)
            .fluidOutputs(GTUtility.getWater(100L))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(4)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Mercury, 1L))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Mercury, FluidShapes.fluidLiquid, 1_000))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(4)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Monazite, Shapes.dust, 1))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidGas, 200))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.ReinforcedGlass.get(1L))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.ReinforcedGlass, FluidShapes.fluidMolten, INGOTS))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(NewHorizonsCoreMod.ID, "ReinforcedGlassPlate", 1L, 0))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.ReinforcedGlass, FluidShapes.fluidMolten, HALF_INGOTS))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(NewHorizonsCoreMod.ID, "ReinforcedGlassLense", 1L, 0))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.ReinforcedGlass, FluidShapes.fluidMolten, 54))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Long_Distance_Pipeline_Fluid.get(1L))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Steel, FluidShapes.fluidMolten, 19 * INGOTS))
            .duration(20 * SECONDS)
            .eut(90)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Long_Distance_Pipeline_Item.get(1L))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Steel, Shapes.ingot, 7))
            .outputChances(10000)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Tin, FluidShapes.fluidMolten, 12 * INGOTS))
            .duration(20 * SECONDS)
            .eut(90)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Long_Distance_Pipeline_Fluid_Pipe.get(4L))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Steel, FluidShapes.fluidMolten, 189))
            .duration(2 * SECONDS)
            .eut(90)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Long_Distance_Pipeline_Item_Pipe.get(16L))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Tin, Shapes.ingot, 3))
            .outputChances(10000)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Steel, FluidShapes.fluidMolten, 324))
            .duration(20 * SECONDS)
            .eut(90)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Quartzite, Shapes.dust, 1))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Glass, FluidShapes.fluidMolten, HALF_INGOTS))
            .duration(30 * SECONDS)
            .eut(28)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 0))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Iron, FluidShapes.fluidMolten, 2 * INGOTS))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 1))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Iron, FluidShapes.fluidMolten, INGOTS))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 2))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Iron, Shapes.nugget, 6))
            .outputChances(10000)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Bronze, FluidShapes.fluidMolten, 1728))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 13))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Steel, FluidShapes.fluidMolten, 2 * INGOTS))
            .duration(20 * SECONDS)
            .eut(90)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 14))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Steel, FluidShapes.fluidMolten, INGOTS))
            .duration(20 * SECONDS)
            .eut(90)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 15))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Steel, FluidShapes.fluidMolten, 1836))
            .duration(20 * SECONDS)
            .eut(90)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 0))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Aluminium, FluidShapes.fluidMolten, 2 * INGOTS))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 1))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Aluminium, FluidShapes.fluidMolten, INGOTS))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 2))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, 12))
            .outputChances(10000)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Aluminium, FluidShapes.fluidMolten, 108))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 3))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.StainlessSteel, FluidShapes.fluidMolten, 2 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 4))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.StainlessSteel, FluidShapes.fluidMolten, INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 5))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.StainlessSteel, FluidShapes.fluidMolten, 1836))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 6))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Titanium, FluidShapes.fluidMolten, 2 * INGOTS))
            .duration(35 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 7))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Titanium, FluidShapes.fluidMolten, INGOTS))
            .duration(35 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 8))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Titanium, FluidShapes.fluidMolten, 1836))
            .duration(35 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 9))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.TungstenSteel, FluidShapes.fluidMolten, 2 * INGOTS))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 10))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.TungstenSteel, FluidShapes.fluidMolten, INGOTS))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 11))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.TungstenSteel, FluidShapes.fluidMolten, 1836))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 12))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Palladium, FluidShapes.fluidMolten, 2 * INGOTS))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 13))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Palladium, FluidShapes.fluidMolten, INGOTS))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 14))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Chrome, Shapes.nugget, 6))
            .outputChances(10000)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.NiobiumTitanium, FluidShapes.fluidMolten, 1728))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 0))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Iridium, FluidShapes.fluidMolten, 2 * INGOTS))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 1))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Iridium, FluidShapes.fluidMolten, INGOTS))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 2))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Iridium, Shapes.nugget, 6))
            .outputChances(10000)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Enderium, FluidShapes.fluidMolten, 1728))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 3))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Osmium, FluidShapes.fluidMolten, 2 * INGOTS))
            .duration(55 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 4))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Osmium, FluidShapes.fluidMolten, INGOTS))
            .duration(55 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 5))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Osmium, Shapes.nugget, 6))
            .outputChances(10000)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Naquadah, FluidShapes.fluidMolten, 1728))
            .duration(55 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 6))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Neutronium, FluidShapes.fluidMolten, 2 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 7))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Neutronium, FluidShapes.fluidMolten, INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 8))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Neutronium, FluidShapes.fluidMolten, 1836))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.wheat_seeds, 1, 32767))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SeedOil, FluidShapes.fluidLiquid, 10))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(2)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.melon_seeds, 1, 32767))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SeedOil, FluidShapes.fluidLiquid, 10))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(2)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.pumpkin_seeds, 1, 32767))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SeedOil, FluidShapes.fluidLiquid, 10))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(2)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.snowball, 1, 0))
            .fluidOutputs(GTUtility.getWater(250))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(4)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.snow, 1, 0))
            .fluidOutputs(GTUtility.getWater(1_000))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(4)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Ice, Shapes.dust, 1))
            .fluidOutputs(GTUtility.getIceSolid(1000L))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(4)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Forestry.ID, "phosphor", 1L))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Phosphorus, Shapes.dust, 1))
            .outputChances(1000)
            .fluidOutputs(GTUtility.getLava(800L))
            .duration(12 * SECONDS + 16 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getModItem(TinkerConstruct.ID, "oreBerries", 1L, 0))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Iron, FluidShapes.fluidMolten, NUGGETS))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getModItem(TinkerConstruct.ID, "oreBerries", 1L, 1))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Gold, FluidShapes.fluidMolten, NUGGETS))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getModItem(TinkerConstruct.ID, "oreBerries", 1L, 2))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Copper, FluidShapes.fluidMolten, NUGGETS))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getModItem(TinkerConstruct.ID, "oreBerries", 1L, 3))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Tin, FluidShapes.fluidMolten, NUGGETS))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(fluidExtractionRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getModItem(TinkerConstruct.ID, "oreBerries", 1L, 4))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Aluminium, FluidShapes.fluidMolten, NUGGETS))
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
