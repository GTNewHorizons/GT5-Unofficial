package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.IndustrialCraft2;
import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.HALF_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.NUGGETS;
import static gregtech.api.util.GTRecipeBuilder.QUARTER_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static net.minecraftforge.fluids.FluidRegistry.getFluidStack;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import bartworks.common.loaders.ItemRegistry;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;

@SuppressWarnings({ "PointlessArithmeticExpression" })
public class FluidSolidifierRecipes implements Runnable {

    @Override
    public void run() {

        ItemStack flask = ItemList.VOLUMETRIC_FLASK.get(1);
        NBTTagCompound nbtFlask = new NBTTagCompound();
        nbtFlask.setInteger("Capacity", 1000);
        flask.setTagCompound(nbtFlask);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Ball.get(0))
            .itemOutputs(flask)
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("molten.borosilicateglass"), 1 * INGOTS))
            .duration(2 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Ball.get(0L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Mercury, 1L))
            .fluidInputs(Materials.Mercury.getFluid(1_000))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(4)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Ball.get(0L))
            .itemOutputs(new ItemStack(Items.snowball, 1, 0))
            .fluidInputs(Materials.Water.getFluid(250))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(4)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Ball.get(0L))
            .itemOutputs(new ItemStack(Items.snowball, 1, 0))
            .fluidInputs(GTModHandler.getDistilledWater(250L))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(4)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Block.get(0L))
            .itemOutputs(new ItemStack(Blocks.snow, 1, 0))
            .fluidInputs(Materials.Water.getFluid(1_000))
            .duration(25 * SECONDS + 12 * TICKS)
            .eut(4)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Block.get(0L))
            .itemOutputs(new ItemStack(Blocks.snow, 1, 0))
            .fluidInputs(GTModHandler.getDistilledWater(1_000))
            .duration(25 * SECONDS + 12 * TICKS)
            .eut(4)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Block.get(0L))
            .itemOutputs(new ItemStack(Blocks.obsidian, 1, 0))
            .fluidInputs(Materials.Lava.getFluid(1_000))
            .duration(51 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Block.get(0L))
            .itemOutputs(new ItemStack(GregTechAPI.sBlockConcretes, 1, 8))
            .fluidInputs(Materials.Concrete.getMolten(1 * INGOTS))
            .duration(12 * TICKS)
            .eut(4)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Block.get(0L))
            .itemOutputs(new ItemStack(Blocks.glowstone, 1, 0))
            .fluidInputs(Materials.Glowstone.getMolten(4 * INGOTS))
            .duration(12 * TICKS)
            .eut(4)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Block.get(0L))
            .itemOutputs(new ItemStack(Blocks.glass, 1, 0))
            .fluidInputs(Materials.Glass.getMolten(1 * INGOTS))
            .duration(12 * TICKS)
            .eut(4)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Bottle.get(0L))
            .itemOutputs(ItemList.Bottle_Empty.get(1L))
            .fluidInputs(Materials.Glass.getMolten(1 * INGOTS))
            .duration(12 * TICKS)
            .eut(4)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Cylinder.get(0L))
            .itemOutputs(ItemList.Food_Cheese.get(1L))
            .fluidInputs(Materials.Milk.getFluid(250))
            .duration(51 * SECONDS + 4 * TICKS)
            .eut(4)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Cylinder.get(0L))
            .itemOutputs(ItemList.Food_Cheese.get(1L))
            .fluidInputs(Materials.Cheese.getMolten(1 * INGOTS))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(8)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Anvil.get(0L))
            .itemOutputs(new ItemStack(Blocks.anvil, 1, 0))
            .fluidInputs(Materials.Iron.getMolten(4464L))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Anvil.get(0L))
            .itemOutputs(new ItemStack(Blocks.anvil, 1, 0))
            .fluidInputs(Materials.WroughtIron.getMolten(4464L))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(Materials.Boron.getDust(1))
            .fluidInputs(Materials.Boron.getMolten(1 * INGOTS))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Cylinder.get(0))
            .itemOutputs(ItemList.Circuit_Parts_PetriDish.get(1))
            .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(1 * QUARTER_INGOTS))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Cylinder.get(0))
            .itemOutputs(ItemList.Circuit_Parts_PetriDish.get(1))
            .fluidInputs(Materials.Polystyrene.getMolten(1 * QUARTER_INGOTS))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Cylinder.get(0))
            .itemOutputs(ItemList.Circuit_Parts_PetriDish.get(1))
            .fluidInputs(Materials.BorosilicateGlass.getMolten(1 * HALF_INGOTS))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Block.get(0L))
            .itemOutputs(getModItem(IndustrialCraft2.ID, "blockAlloyGlass", 1L))
            .fluidInputs(Materials.ReinforcedGlass.getMolten(1 * INGOTS))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Ball.get(0L))
            .itemOutputs(ItemList.Circuit_Parts_Glass_Tube.get(1))
            .fluidInputs(Materials.Glass.getMolten(1 * INGOTS))
            .duration(10 * SECONDS)
            .eut(24)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Ball.get(0L))
            .itemOutputs(ItemList.Circuit_Parts_Reinforced_Glass_Tube.get(1))
            .fluidInputs(Materials.ReinforcedGlass.getMolten(2 * INGOTS))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Ball.get(0L))
            .itemOutputs(ItemList.Circuit_Parts_Glass_Tube.get(1))
            .fluidInputs(getFluidStack("glass.molten", 1000))
            .duration(10 * SECONDS)
            .eut(24)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Ball.get(0L))
            .itemOutputs(ItemList.GelledToluene.get(1))
            .fluidInputs(new FluidStack(ItemList.sToluene, 100))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Nugget.get(0L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Copper, 1L))
            .fluidInputs(Materials.AnnealedCopper.getMolten(1 * NUGGETS))
            .duration(16 * TICKS)
            .eut(4)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Ingot.get(0L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Copper, 1L))
            .fluidInputs(Materials.AnnealedCopper.getMolten(1 * INGOTS))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(8)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Block.get(0L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.block, Materials.Copper, 1L))
            .fluidInputs(Materials.AnnealedCopper.getMolten(9 * INGOTS))
            .duration(14 * SECONDS + 8 * TICKS)
            .eut(8)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Nugget.get(0L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Iron, 1L))
            .fluidInputs(Materials.WroughtIron.getMolten(1 * NUGGETS))
            .duration(16 * TICKS)
            .eut(4)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Ingot.get(0L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, 1L))
            .fluidInputs(Materials.WroughtIron.getMolten(1 * INGOTS))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(8)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Block.get(0L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.block, Materials.Iron, 1L))
            .fluidInputs(Materials.WroughtIron.getMolten(9 * INGOTS))
            .duration(14 * SECONDS + 8 * TICKS)
            .eut(8)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Anvil.get(0L))
            .itemOutputs(getModItem(Railcraft.ID, "anvil", 1L, 0))
            .fluidInputs(Materials.Steel.getMolten(4464L))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(fluidSolidifierRecipes);
        // Bartworks Glass Tube
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Rod_Long.get(0L))
            .itemOutputs(new ItemStack(ItemRegistry.PUMPPARTS, 1, 0))
            .fluidInputs(Materials.Glass.getMolten(288L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(fluidSolidifierRecipes);

        // Red Granite Block
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Block.get(0L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.stone, Materials.GraniteRed, 1L))
            .fluidInputs(Materials.GraniteRed.getMolten(1 * INGOTS))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(fluidSolidifierRecipes);
        // Black Granite Block
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Block.get(0L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.stone, Materials.GraniteBlack, 1L))
            .fluidInputs(Materials.GraniteBlack.getMolten(1 * INGOTS))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(fluidSolidifierRecipes);

    }
}
