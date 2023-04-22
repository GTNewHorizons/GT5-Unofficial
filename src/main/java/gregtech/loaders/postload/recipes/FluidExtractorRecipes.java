package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.*;
import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.util.GT_ModHandler.getModItem;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sFluidExtractionRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;
import static net.minecraftforge.fluids.FluidRegistry.getFluidStack;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;

public class FluidExtractorRecipes implements Runnable {

    @Override
    public void run() {

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Dye_SquidInk.get(1L))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(getFluidStack("squidink", 144))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(4)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Dye_Indigo.get(1L))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(getFluidStack("indigo", 144))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(4)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Crop_Drop_Indigo.get(1L))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(getFluidStack("indigo", 144))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(4)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Crop_Drop_MilkWart.get(1L))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Milk, 1L))
            .outputChances(1000)
            .noFluidInputs()
            .fluidOutputs(GT_ModHandler.getMilk(150L))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(4)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Crop_Drop_OilBerry.get(1L))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.Oil.getFluid(100L))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(4)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Crop_Drop_UUMBerry.get(1L))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.UUMatter.getFluid(4L))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(4)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Crop_Drop_UUABerry.get(1L))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.UUAmplifier.getFluid(4L))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(4)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.fish, 1, 0))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.FishOil.getFluid(40L))
            .duration(16 * TICKS)
            .eut(4)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.fish, 1, 1))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.FishOil.getFluid(60L))
            .duration(16 * TICKS)
            .eut(4)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.fish, 1, 2))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.FishOil.getFluid(70L))
            .duration(16 * TICKS)
            .eut(4)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.fish, 1, 3))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.FishOil.getFluid(30L))
            .duration(16 * TICKS)
            .eut(4)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.coal, 1, 1))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 1L))
            .outputChances(1000)
            .noFluidInputs()
            .fluidOutputs(Materials.WoodTar.getFluid(100L))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 1L))
            .itemOutputs(ItemList.IC2_Plantball.get(1L))
            .outputChances(100)
            .noFluidInputs()
            .fluidOutputs(Materials.Creosote.getFluid(5L))
            .duration(16 * TICKS)
            .eut(4)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.HydratedCoal, 1L))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 1L))
            .outputChances(10000)
            .noFluidInputs()
            .fluidOutputs(Materials.Water.getFluid(100L))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(4)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getModItem(Thaumcraft.ID, "ItemResource", 1, 3))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.Mercury.getFluid(1000L))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(4)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Mercury, 1L))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.Mercury.getFluid(1000L))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(4)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Monazite, 1L))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.Helium.getGas(200L))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(64)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getModItem(IndustrialCraft2.ID, "blockAlloyGlass", 1L, 0))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.ReinforceGlass.getMolten(144))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getModItem(NewHorizonsCoreMod.ID, "item.ReinforcedGlassPlate", 1L, 0))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.ReinforceGlass.getMolten(72))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getModItem(NewHorizonsCoreMod.ID, "item.ReinforcedGlassLense", 1L, 0))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.ReinforceGlass.getMolten(54))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Long_Distance_Pipeline_Fluid.get(1L))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.Steel.getMolten(19 * 144))
            .duration(20 * SECONDS)
            .eut(90)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Long_Distance_Pipeline_Item.get(1L))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Steel, 7L))
            .outputChances(10000)
            .noFluidInputs()
            .fluidOutputs(Materials.Tin.getMolten(12 * 144))
            .duration(20 * SECONDS)
            .eut(90)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Long_Distance_Pipeline_Fluid_Pipe.get(4L))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.Steel.getMolten(189))
            .duration(2 * SECONDS)
            .eut(90)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Long_Distance_Pipeline_Item_Pipe.get(16L))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Tin, 3L))
            .outputChances(10000)
            .noFluidInputs()
            .fluidOutputs(Materials.Steel.getMolten(324))
            .duration(20 * SECONDS)
            .eut(90)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_ModHandler.getIC2Item("TritiumCell", 1))
            .itemOutputs(GT_ModHandler.getIC2Item("fuelRod", 1))
            .outputChances(10000)
            .noFluidInputs()
            .fluidOutputs(Materials.Tritium.getGas(32))
            .duration(16 * TICKS)
            .eut(64)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Quartzite, 1L))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.Glass.getMolten(72))
            .duration(30 * SECONDS)
            .eut(28)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 0))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.Iron.getMolten(288))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 1))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.Iron.getMolten(144))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 2))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Iron, 6))
            .outputChances(10000)
            .noFluidInputs()
            .fluidOutputs(Materials.Bronze.getMolten(1728))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 13))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.Steel.getMolten(288))
            .duration(20 * SECONDS)
            .eut(90)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 14))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.Steel.getMolten(144))
            .duration(20 * SECONDS)
            .eut(90)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 15))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.Steel.getMolten(1836))
            .duration(20 * SECONDS)
            .eut(90)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 0))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.Aluminium.getMolten(288))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 1))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.Aluminium.getMolten(144))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 2))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 12L))
            .outputChances(10000)
            .noFluidInputs()
            .fluidOutputs(Materials.Aluminium.getMolten(108L))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 3))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.StainlessSteel.getMolten(288))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 4))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.StainlessSteel.getMolten(144))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 5))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.StainlessSteel.getMolten(1836))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 6))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.Titanium.getMolten(288))
            .duration(35 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 7))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.Titanium.getMolten(144))
            .duration(35 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 8))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.Titanium.getMolten(1836))
            .duration(35 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 9))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.TungstenSteel.getMolten(288))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 10))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.TungstenSteel.getMolten(144))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 11))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.TungstenSteel.getMolten(1836))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 12))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.Palladium.getMolten(288))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 13))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.Palladium.getMolten(144))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 14))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Chrome, 6L))
            .outputChances(10000)
            .noFluidInputs()
            .fluidOutputs(Materials.NiobiumTitanium.getMolten(1728))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 0))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.Iridium.getMolten(288))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 1))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.Iridium.getMolten(144))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 2))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Iridium, 6L))
            .outputChances(10000)
            .noFluidInputs()
            .fluidOutputs(Materials.Enderium.getMolten(1728))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 3))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.Osmium.getMolten(288))
            .duration(55 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 4))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.Osmium.getMolten(144))
            .duration(55 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 5))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Osmium, 6L))
            .outputChances(10000)
            .noFluidInputs()
            .fluidOutputs(Materials.Naquadah.getMolten(1728))
            .duration(55 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 6))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.Neutronium.getMolten(288))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 7))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.Neutronium.getMolten(144))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 8))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.Neutronium.getMolten(1836))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.wheat_seeds, 1, 32767))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.SeedOil.getFluid(10))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(2)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.melon_seeds, 1, 32767))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.SeedOil.getFluid(10))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(2)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.pumpkin_seeds, 1, 32767))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.SeedOil.getFluid(10))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(2)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Crop_Drop_Rape.get(1))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.SeedOil.getFluid(125))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(2)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.snowball, 1, 0))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.Water.getFluid(250L))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(4)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.snow, 1, 0))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.Water.getFluid(1000L))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(4)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ice, 1L))
            .noItemOutputs()
            .noFluidInputs()
            .fluidOutputs(Materials.Ice.getSolid(1000L))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(4)
            .addTo(sFluidExtractionRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(getModItem(Forestry.ID, "phosphor", 1L))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Phosphorus, 1L))
            .outputChances(1000)
            .noFluidInputs()
            .fluidOutputs(Materials.Lava.getFluid(800L))
            .duration(12 * SECONDS + 16 * TICKS)
            .eut(128)
            .addTo(sFluidExtractionRecipes);

        // Beecombs fluid extractor recipes
        if (BartWorks.isModLoaded()) {
            // xenon
            GT_Values.RA.stdBuilder()
                .itemInputs(getModItem(GregTech.ID, "gt.comb", 1L, 134))
                .noItemOutputs()
                .noFluidInputs()
                .fluidOutputs(getFluidStack("xenon", 250))
                .duration(2 * SECONDS + 10 * TICKS)
                .eut(TierEU.RECIPE_IV)
                .addTo(sFluidExtractionRecipes);

            // neon
            GT_Values.RA.stdBuilder()
                .itemInputs(getModItem(GregTech.ID, "gt.comb", 1L, 135))
                .noItemOutputs()
                .noFluidInputs()
                .fluidOutputs(getFluidStack("neon", 250))
                .duration(15 * TICKS)
                .eut(TierEU.RECIPE_IV)
                .addTo(sFluidExtractionRecipes);

            // krpton
            GT_Values.RA.stdBuilder()
                .itemInputs(getModItem(GregTech.ID, "gt.comb", 1L, 136))
                .noItemOutputs()
                .noFluidInputs()
                .fluidOutputs(getFluidStack("krypton", 250))
                .duration(1 * SECONDS + 5 * TICKS)
                .eut(TierEU.RECIPE_IV)
                .addTo(sFluidExtractionRecipes);
        }
    }
}
