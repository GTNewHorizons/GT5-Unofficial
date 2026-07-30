package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.GTValues.M;
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

import gregtech.api.enums.materials2.FluidShapes;
import gregtech.api.enums.materials2.Materials;
import gregtech.api.enums.materials2.Shapes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.common.loaders.ItemRegistry;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

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
            .itemOutputs(GTOreDictUnificator.get("gemMercury", 1L))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Mercury, FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(4)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Ball.get(0L))
            .itemOutputs(new ItemStack(Items.snowball, 1, 0))
            .fluidInputs(GTUtility.getWater(250))
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
            .fluidInputs(GTUtility.getWater(1_000))
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
            .fluidInputs(GTUtility.getLava(1_000))
            .duration(51 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Block.get(0L))
            .itemOutputs(new ItemStack(GregTechAPI.sBlockConcretes, 1, 8))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Concrete, FluidShapes.fluidMolten, (int) (1 * INGOTS)))
            .duration(12 * TICKS)
            .eut(4)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Block.get(0L))
            .itemOutputs(new ItemStack(Blocks.glowstone, 1, 0))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.Glowstone,
                    FluidShapes.fluidMolten,
                    (int) (4 * INGOTS)))
            .duration(12 * TICKS)
            .eut(4)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Block.get(0L))
            .itemOutputs(new ItemStack(Blocks.glass, 1, 0))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Glass, FluidShapes.fluidMolten, (int) (1 * INGOTS)))
            .duration(12 * TICKS)
            .eut(4)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Bottle.get(0L))
            .itemOutputs(ItemList.Bottle_Empty.get(1L))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Glass, FluidShapes.fluidMolten, (int) (1 * INGOTS)))
            .duration(12 * TICKS)
            .eut(4)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Cylinder.get(0L))
            .itemOutputs(ItemList.Food_Cheese.get(1L))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Milk, FluidShapes.fluidLiquid, (int) (250)))
            .duration(51 * SECONDS + 4 * TICKS)
            .eut(4)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Cylinder.get(0L))
            .itemOutputs(ItemList.Food_Cheese.get(1L))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Cheese, FluidShapes.fluidMolten, (int) (1 * INGOTS)))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Anvil.get(0L))
            .itemOutputs(new ItemStack(Blocks.anvil, 1, 0))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Iron, FluidShapes.fluidMolten, (int) (4464)))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Anvil.get(0L))
            .itemOutputs(new ItemStack(Blocks.anvil, 1, 0))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.CastIron, FluidShapes.fluidMolten, (int) (4464)))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Boron, Shapes.dust, (int) (1)))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Boron, FluidShapes.fluidMolten, (int) (1 * INGOTS)))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Cylinder.get(0))
            .itemOutputs(ItemList.Circuit_Parts_PetriDish.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.Polytetrafluoroethylene,
                    FluidShapes.fluidMolten,
                    (int) (1 * QUARTER_INGOTS)))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Cylinder.get(0))
            .itemOutputs(ItemList.Circuit_Parts_PetriDish.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.Polystyrene,
                    FluidShapes.fluidMolten,
                    (int) (1 * QUARTER_INGOTS)))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Cylinder.get(0))
            .itemOutputs(ItemList.Circuit_Parts_PetriDish.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.BorosilicateGlass,
                    FluidShapes.fluidMolten,
                    (int) (1 * HALF_INGOTS)))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Block.get(0L))
            .itemOutputs(ItemList.ReinforcedGlass.get(1L))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.ReinforcedGlass,
                    FluidShapes.fluidMolten,
                    (int) (1 * INGOTS)))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Ball.get(0L))
            .itemOutputs(ItemList.Circuit_Parts_Glass_Tube.get(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Glass, FluidShapes.fluidMolten, (int) (1 * INGOTS)))
            .duration(10 * SECONDS)
            .eut(24)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Ball.get(0L))
            .itemOutputs(ItemList.Circuit_Parts_Reinforced_Glass_Tube.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.ReinforcedGlass,
                    FluidShapes.fluidMolten,
                    (int) (2 * INGOTS)))
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
            .itemOutputs(MaterialLibAPI.getStack(Materials.Copper, Shapes.nugget, (int) (1)))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.AnnealedCopper,
                    FluidShapes.fluidMolten,
                    (int) (1 * NUGGETS)))
            .duration(16 * TICKS)
            .eut(4)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Ingot.get(0L))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Copper, Shapes.ingot, (int) (1)))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.AnnealedCopper,
                    FluidShapes.fluidMolten,
                    (int) (1 * INGOTS)))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Block.get(0L))
            .itemOutputs(GTOreDictUnificator.get("blockCopper", 1L))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.AnnealedCopper,
                    FluidShapes.fluidMolten,
                    (int) (9 * INGOTS)))
            .duration(14 * SECONDS + 8 * TICKS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Nugget.get(0L))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Iron, Shapes.nugget, (int) (1)))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.CastIron,
                    FluidShapes.fluidMolten,
                    (int) (1 * NUGGETS)))
            .duration(16 * TICKS)
            .eut(4)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Ingot.get(0L))
            .itemOutputs(GTOreDictUnificator.get("ingotIron", 1L))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.CastIron, FluidShapes.fluidMolten, (int) (1 * INGOTS)))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Block.get(0L))
            .itemOutputs(GTOreDictUnificator.get("blockIron", 1L))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.CastIron, FluidShapes.fluidMolten, (int) (9 * INGOTS)))
            .duration(14 * SECONDS + 8 * TICKS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Spinneret.get(0L))
            .itemOutputs(ItemList.KevlarFiber.get(8L))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.LiquidCrystalKevlar,
                    FluidShapes.fluidLiquid,
                    (int) (1 * INGOTS)))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Anvil.get(0L))
            .itemOutputs(getModItem(Railcraft.ID, "anvil", 1L, 0))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Steel, FluidShapes.fluidMolten, (int) (4464)))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(fluidSolidifierRecipes);
        // Bartworks Glass Tube
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Rod_Long.get(0L))
            .itemOutputs(new ItemStack(ItemRegistry.PUMPPARTS, 1, 0))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Glass, FluidShapes.fluidMolten, (int) (288)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(fluidSolidifierRecipes);

        // Red Granite Block
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Block.get(0L))
            .itemOutputs(GTOreDictUnificator.get("stoneGraniteRed", 1L))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.GraniteRed,
                    FluidShapes.fluidMolten,
                    (int) (1 * INGOTS)))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(fluidSolidifierRecipes);
        // Black Granite Block
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Block.get(0L))
            .itemOutputs(GTOreDictUnificator.get("stoneGraniteBlack", 1L))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.GraniteBlack,
                    FluidShapes.fluidMolten,
                    (int) (1 * INGOTS)))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(fluidSolidifierRecipes);

        this.HexaniteRecipes();
    }

    private void HexaniteRecipes() {
        this.addHexanitePartRecipe(OrePrefixes.frameGt, 1);
        this.addHexanitePartRecipe(OrePrefixes.ingot, 1);
        this.addHexanitePartRecipe(OrePrefixes.plate, 1);
        this.addHexanitePartRecipe(OrePrefixes.plateDouble, 1);
        this.addHexanitePartRecipe(OrePrefixes.plateDense, 1);
        this.addHexanitePartRecipe(OrePrefixes.stick, 1);
        this.addHexanitePartRecipe(OrePrefixes.round, 1);
        this.addHexanitePartRecipe(OrePrefixes.bolt, 1);
        this.addHexanitePartRecipe(OrePrefixes.screw, 1);
        this.addHexanitePartRecipe(OrePrefixes.ring, 1);
        this.addHexanitePartRecipe(OrePrefixes.foil, 1);
        this.addHexanitePartRecipe(OrePrefixes.gearGtSmall, 1);
        this.addHexanitePartRecipe(OrePrefixes.rotor, 1);
        this.addHexanitePartRecipe(OrePrefixes.stickLong, 1);
        this.addHexanitePartRecipe(OrePrefixes.gearGt, 1);
        this.addHexanitePartRecipe(OrePrefixes.itemCasing, 1);
        this.addHexanitePartRecipe(OrePrefixes.plateSuperdense, 1);
        this.addHexanitePartRecipe(OrePrefixes.block, 1);
    }

    private void addHexanitePartRecipe(OrePrefixes prefix, final int multiplier) {

        final int partFraction = (int) (prefix.getMaterialAmount() * INGOTS / M);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(prefix, Materials.Netherite, multiplier))
            .itemOutputs(GTOreDictUnificator.get(prefix, Materials.Hexanite, multiplier))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.UnformedHexanite,
                    FluidShapes.fluidLiquid,
                    (int) ((long) partFraction * multiplier)))
            .duration((int) (multiplier * (2 * SECONDS * partFraction / (float) INGOTS)))
            .eut(TierEU.RECIPE_UMV)
            .addTo(fluidSolidifierRecipes);

    }
}
