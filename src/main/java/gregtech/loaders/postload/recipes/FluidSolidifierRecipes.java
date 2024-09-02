package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.IndustrialCraft2;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static net.minecraftforge.fluids.FluidRegistry.getFluidStack;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsKevlar;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class FluidSolidifierRecipes implements Runnable {

    @Override
    public void run() {
        Materials[] materialArray = new Materials[] { Materials.Iron, Materials.WroughtIron, Materials.Gold,
            Materials.Bronze, Materials.Copper, Materials.AnnealedCopper, Materials.Tin, Materials.Lead,
            Materials.Steel };

        ItemStack[] materialCasing = new ItemStack[] { ItemList.IC2_Item_Casing_Iron.get(1L),
            ItemList.IC2_Item_Casing_Iron.get(1L), ItemList.IC2_Item_Casing_Gold.get(1L),
            ItemList.IC2_Item_Casing_Bronze.get(1L), ItemList.IC2_Item_Casing_Copper.get(1L),
            ItemList.IC2_Item_Casing_Copper.get(1L), ItemList.IC2_Item_Casing_Tin.get(1L),
            ItemList.IC2_Item_Casing_Lead.get(1L), ItemList.IC2_Item_Casing_Steel.get(1L) };

        for (int i = 0; i < materialArray.length; i++) {
            if (materialArray[i].mStandardMoltenFluid == null) {
                continue;
            }

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Mold_Casing.get(0L))
                .itemOutputs(materialCasing[i])
                .fluidInputs(materialArray[i].getMolten(72L))
                .duration(16 * TICKS)
                .eut(8)
                .addTo(fluidSolidifierRecipes);
        }

        {
            ItemStack flask = ItemList.VOLUMETRIC_FLASK.get(1);
            NBTTagCompound nbtFlask = new NBTTagCompound();
            nbtFlask.setInteger("Capacity", 1000);
            flask.setTagCompound(nbtFlask);
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Mold_Ball.get(0))
                .itemOutputs(flask)
                .fluidInputs(new FluidStack(FluidRegistry.getFluid("molten.borosilicateglass"), 144))
                .duration(2 * SECONDS + 4 * TICKS)
                .eut(TierEU.RECIPE_LV)
                .addTo(fluidSolidifierRecipes);
        }

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Ball.get(0L))
            .itemOutputs(getModItem(Thaumcraft.ID, "ItemResource", 1, 3))
            .fluidInputs(Materials.Mercury.getFluid(1000L))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(4)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Ball.get(0L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Mercury, 1L))
            .fluidInputs(Materials.Mercury.getFluid(1000L))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(4)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Ball.get(0L))
            .itemOutputs(new ItemStack(Items.snowball, 1, 0))
            .fluidInputs(Materials.Water.getFluid(250L))
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
            .fluidInputs(Materials.Water.getFluid(1000L))
            .duration(25 * SECONDS + 12 * TICKS)
            .eut(4)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Block.get(0L))
            .itemOutputs(new ItemStack(Blocks.snow, 1, 0))
            .fluidInputs(GTModHandler.getDistilledWater(1000L))
            .duration(25 * SECONDS + 12 * TICKS)
            .eut(4)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Block.get(0L))
            .itemOutputs(new ItemStack(Blocks.obsidian, 1, 0))
            .fluidInputs(Materials.Lava.getFluid(1000L))
            .duration(51 * SECONDS + 4 * TICKS)
            .eut(16)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Block.get(0L))
            .itemOutputs(new ItemStack(GregTechAPI.sBlockConcretes, 1, 8))
            .fluidInputs(Materials.Concrete.getMolten(144L))
            .duration(12 * TICKS)
            .eut(4)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Block.get(0L))
            .itemOutputs(new ItemStack(Blocks.glowstone, 1, 0))
            .fluidInputs(Materials.Glowstone.getMolten(576L))
            .duration(12 * TICKS)
            .eut(4)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Block.get(0L))
            .itemOutputs(new ItemStack(Blocks.glass, 1, 0))
            .fluidInputs(Materials.Glass.getMolten(144L))
            .duration(12 * TICKS)
            .eut(4)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Plate.get(0L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, Materials.Glass, 1L))
            .fluidInputs(Materials.Glass.getMolten(144L))
            .duration(12 * TICKS)
            .eut(4)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Bottle.get(0L))
            .itemOutputs(ItemList.Bottle_Empty.get(1L))
            .fluidInputs(Materials.Glass.getMolten(144L))
            .duration(12 * TICKS)
            .eut(4)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Cylinder.get(0L))
            .itemOutputs(ItemList.Food_Cheese.get(1L))
            .fluidInputs(Materials.Milk.getFluid(250L))
            .duration(51 * SECONDS + 4 * TICKS)
            .eut(4)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Cylinder.get(0L))
            .itemOutputs(ItemList.Food_Cheese.get(1L))
            .fluidInputs(Materials.Cheese.getMolten(144L))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(8)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Anvil.get(0L))
            .itemOutputs(new ItemStack(Blocks.anvil, 1, 0))
            .fluidInputs(Materials.Iron.getMolten(4464L))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(16)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Anvil.get(0L))
            .itemOutputs(new ItemStack(Blocks.anvil, 1, 0))
            .fluidInputs(Materials.WroughtIron.getMolten(4464L))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(16)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .itemOutputs(Materials.Boron.getDust(1))
            .fluidInputs(Materials.Boron.getMolten(144L))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(30)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Cylinder.get(0))
            .itemOutputs(ItemList.Circuit_Parts_PetriDish.get(1))
            .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(36))
            .duration(8 * SECONDS)
            .eut(16)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Cylinder.get(0))
            .itemOutputs(ItemList.Circuit_Parts_PetriDish.get(1))
            .fluidInputs(Materials.Polystyrene.getMolten(36))
            .duration(8 * SECONDS)
            .eut(16)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Cylinder.get(0))
            .itemOutputs(ItemList.Circuit_Parts_PetriDish.get(1))
            .fluidInputs(Materials.BorosilicateGlass.getMolten(72))
            .duration(8 * SECONDS)
            .eut(16)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Plate.get(0L))
            .itemOutputs(getModItem(NewHorizonsCoreMod.ID, "item.ReinforcedGlassPlate", 1L, 0))
            .fluidInputs(Materials.ReinforceGlass.getMolten(72))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Block.get(0L))
            .itemOutputs(getModItem(IndustrialCraft2.ID, "blockAlloyGlass", 1L))
            .fluidInputs(Materials.ReinforceGlass.getMolten(144))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Ball.get(0L))
            .itemOutputs(ItemList.Circuit_Parts_Glass_Tube.get(1))
            .fluidInputs(Materials.Glass.getMolten(144))
            .duration(10 * SECONDS)
            .eut(24)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Ball.get(0L))
            .itemOutputs(ItemList.Circuit_Parts_Reinforced_Glass_Tube.get(1))
            .fluidInputs(Materials.ReinforceGlass.getMolten(288))
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
            .eut(16)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Nugget.get(0L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Copper, 1L))
            .fluidInputs(Materials.AnnealedCopper.getMolten(16))
            .duration(16 * TICKS)
            .eut(4)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Ingot.get(0L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Copper, 1L))
            .fluidInputs(Materials.AnnealedCopper.getMolten(144))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(8)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Block.get(0L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.block, Materials.Copper, 1L))
            .fluidInputs(Materials.AnnealedCopper.getMolten(1296))
            .duration(14 * SECONDS + 8 * TICKS)
            .eut(8)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Nugget.get(0L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Iron, 1L))
            .fluidInputs(Materials.WroughtIron.getMolten(16))
            .duration(16 * TICKS)
            .eut(4)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Ingot.get(0L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, 1L))
            .fluidInputs(Materials.WroughtIron.getMolten(144))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(8)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Block.get(0L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.block, Materials.Iron, 1L))
            .fluidInputs(Materials.WroughtIron.getMolten(1296))
            .duration(14 * SECONDS + 8 * TICKS)
            .eut(8)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Spinneret.get(0L))
            .itemOutputs(ItemList.KevlarFiber.get(8L))
            .fluidInputs(MaterialsKevlar.LiquidCrystalKevlar.getFluid(144L))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Anvil.get(0L))
            .itemOutputs(getModItem(Railcraft.ID, "anvil", 1L, 0))
            .fluidInputs(Materials.Steel.getMolten(4464L))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(16)
            .addTo(fluidSolidifierRecipes);

        final int whiteDwarfShapeSolidifierTime = 10 * SECONDS;
        final int fluidPerShapeSolidifierRecipe = 4 * INGOTS;
        {

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Extruder_Bottle.get(1))
                .itemOutputs(ItemList.White_Dwarf_Shape_Extruder_Bottle.get(1))
                .fluidInputs(MaterialsUEVplus.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe))
                .duration(whiteDwarfShapeSolidifierTime)
                .eut(TierEU.RECIPE_UMV)
                .addTo(fluidSolidifierRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Extruder_Plate.get(1))
                .itemOutputs(ItemList.White_Dwarf_Shape_Extruder_Plate.get(1))
                .fluidInputs(MaterialsUEVplus.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe))
                .duration(whiteDwarfShapeSolidifierTime)
                .eut(TierEU.RECIPE_UMV)
                .addTo(fluidSolidifierRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Extruder_Cell.get(1))
                .itemOutputs(ItemList.White_Dwarf_Shape_Extruder_Cell.get(1))
                .fluidInputs(MaterialsUEVplus.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe))
                .duration(whiteDwarfShapeSolidifierTime)
                .eut(TierEU.RECIPE_UMV)
                .addTo(fluidSolidifierRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Extruder_Ring.get(1))
                .itemOutputs(ItemList.White_Dwarf_Shape_Extruder_Ring.get(1))
                .fluidInputs(MaterialsUEVplus.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe))
                .duration(whiteDwarfShapeSolidifierTime)
                .eut(TierEU.RECIPE_UMV)
                .addTo(fluidSolidifierRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Extruder_Rod.get(1))
                .itemOutputs(ItemList.White_Dwarf_Shape_Extruder_Rod.get(1))
                .fluidInputs(MaterialsUEVplus.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe))
                .duration(whiteDwarfShapeSolidifierTime)
                .eut(TierEU.RECIPE_UMV)
                .addTo(fluidSolidifierRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Extruder_Bolt.get(1))
                .itemOutputs(ItemList.White_Dwarf_Shape_Extruder_Bolt.get(1))
                .fluidInputs(MaterialsUEVplus.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe))
                .duration(whiteDwarfShapeSolidifierTime)
                .eut(TierEU.RECIPE_UMV)
                .addTo(fluidSolidifierRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Extruder_Ingot.get(1))
                .itemOutputs(ItemList.White_Dwarf_Shape_Extruder_Ingot.get(1))
                .fluidInputs(MaterialsUEVplus.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe))
                .duration(whiteDwarfShapeSolidifierTime)
                .eut(TierEU.RECIPE_UMV)
                .addTo(fluidSolidifierRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Extruder_Wire.get(1))
                .itemOutputs(ItemList.White_Dwarf_Shape_Extruder_Wire.get(1))
                .fluidInputs(MaterialsUEVplus.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe))
                .duration(whiteDwarfShapeSolidifierTime)
                .eut(TierEU.RECIPE_UMV)
                .addTo(fluidSolidifierRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Extruder_Casing.get(1))
                .itemOutputs(ItemList.White_Dwarf_Shape_Extruder_Casing.get(1))
                .fluidInputs(MaterialsUEVplus.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe))
                .duration(whiteDwarfShapeSolidifierTime)
                .eut(TierEU.RECIPE_UMV)
                .addTo(fluidSolidifierRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Extruder_Pipe_Tiny.get(1))
                .itemOutputs(ItemList.White_Dwarf_Shape_Extruder_Pipe_Tiny.get(1))
                .fluidInputs(MaterialsUEVplus.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe))
                .duration(whiteDwarfShapeSolidifierTime)
                .eut(TierEU.RECIPE_UMV)
                .addTo(fluidSolidifierRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Extruder_Pipe_Small.get(1))
                .itemOutputs(ItemList.White_Dwarf_Shape_Extruder_Pipe_Small.get(1))
                .fluidInputs(MaterialsUEVplus.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe))
                .duration(whiteDwarfShapeSolidifierTime)
                .eut(TierEU.RECIPE_UMV)
                .addTo(fluidSolidifierRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Extruder_Pipe_Medium.get(1))
                .itemOutputs(ItemList.White_Dwarf_Shape_Extruder_Pipe_Medium.get(1))
                .fluidInputs(MaterialsUEVplus.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe))
                .duration(whiteDwarfShapeSolidifierTime)
                .eut(TierEU.RECIPE_UMV)
                .addTo(fluidSolidifierRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Extruder_Pipe_Large.get(1))
                .itemOutputs(ItemList.White_Dwarf_Shape_Extruder_Pipe_Large.get(1))
                .fluidInputs(MaterialsUEVplus.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe))
                .duration(whiteDwarfShapeSolidifierTime)
                .eut(TierEU.RECIPE_UMV)
                .addTo(fluidSolidifierRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Extruder_Pipe_Huge.get(1))
                .itemOutputs(ItemList.White_Dwarf_Shape_Extruder_Pipe_Huge.get(1))
                .fluidInputs(MaterialsUEVplus.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe))
                .duration(whiteDwarfShapeSolidifierTime)
                .eut(TierEU.RECIPE_UMV)
                .addTo(fluidSolidifierRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Extruder_Block.get(1))
                .itemOutputs(ItemList.White_Dwarf_Shape_Extruder_Block.get(1))
                .fluidInputs(MaterialsUEVplus.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe))
                .duration(whiteDwarfShapeSolidifierTime)
                .eut(TierEU.RECIPE_UMV)
                .addTo(fluidSolidifierRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Extruder_Sword.get(1))
                .itemOutputs(ItemList.White_Dwarf_Shape_Extruder_Sword.get(1))
                .fluidInputs(MaterialsUEVplus.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe))
                .duration(whiteDwarfShapeSolidifierTime)
                .eut(TierEU.RECIPE_UMV)
                .addTo(fluidSolidifierRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Extruder_Pickaxe.get(1))
                .itemOutputs(ItemList.White_Dwarf_Shape_Extruder_Pickaxe.get(1))
                .fluidInputs(MaterialsUEVplus.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe))
                .duration(whiteDwarfShapeSolidifierTime)
                .eut(TierEU.RECIPE_UMV)
                .addTo(fluidSolidifierRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Extruder_Shovel.get(1))
                .itemOutputs(ItemList.White_Dwarf_Shape_Extruder_Shovel.get(1))
                .fluidInputs(MaterialsUEVplus.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe))
                .duration(whiteDwarfShapeSolidifierTime)
                .eut(TierEU.RECIPE_UMV)
                .addTo(fluidSolidifierRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Extruder_Axe.get(1))
                .itemOutputs(ItemList.White_Dwarf_Shape_Extruder_Axe.get(1))
                .fluidInputs(MaterialsUEVplus.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe))
                .duration(whiteDwarfShapeSolidifierTime)
                .eut(TierEU.RECIPE_UMV)
                .addTo(fluidSolidifierRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Extruder_Hoe.get(1))
                .itemOutputs(ItemList.White_Dwarf_Shape_Extruder_Hoe.get(1))
                .fluidInputs(MaterialsUEVplus.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe))
                .duration(whiteDwarfShapeSolidifierTime)
                .eut(TierEU.RECIPE_UMV)
                .addTo(fluidSolidifierRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Extruder_Hammer.get(1))
                .itemOutputs(ItemList.White_Dwarf_Shape_Extruder_Hammer.get(1))
                .fluidInputs(MaterialsUEVplus.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe))
                .duration(whiteDwarfShapeSolidifierTime)
                .eut(TierEU.RECIPE_UMV)
                .addTo(fluidSolidifierRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Extruder_File.get(1))
                .itemOutputs(ItemList.White_Dwarf_Shape_Extruder_File.get(1))
                .fluidInputs(MaterialsUEVplus.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe))
                .duration(whiteDwarfShapeSolidifierTime)
                .eut(TierEU.RECIPE_UMV)
                .addTo(fluidSolidifierRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Extruder_Saw.get(1))
                .itemOutputs(ItemList.White_Dwarf_Shape_Extruder_Saw.get(1))
                .fluidInputs(MaterialsUEVplus.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe))
                .duration(whiteDwarfShapeSolidifierTime)
                .eut(TierEU.RECIPE_UMV)
                .addTo(fluidSolidifierRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Extruder_Gear.get(1))
                .itemOutputs(ItemList.White_Dwarf_Shape_Extruder_Gear.get(1))
                .fluidInputs(MaterialsUEVplus.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe))
                .duration(whiteDwarfShapeSolidifierTime)
                .eut(TierEU.RECIPE_UMV)
                .addTo(fluidSolidifierRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Extruder_Rotor.get(1))
                .itemOutputs(ItemList.White_Dwarf_Shape_Extruder_Rotor.get(1))
                .fluidInputs(MaterialsUEVplus.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe))
                .duration(whiteDwarfShapeSolidifierTime)
                .eut(TierEU.RECIPE_UMV)
                .addTo(fluidSolidifierRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Extruder_Turbine_Blade.get(1))
                .itemOutputs(ItemList.White_Dwarf_Shape_Extruder_Turbine_Blade.get(1))
                .fluidInputs(MaterialsUEVplus.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe))
                .duration(whiteDwarfShapeSolidifierTime)
                .eut(TierEU.RECIPE_UMV)
                .addTo(fluidSolidifierRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Extruder_Small_Gear.get(1))
                .itemOutputs(ItemList.White_Dwarf_Shape_Extruder_Small_Gear.get(1))
                .fluidInputs(MaterialsUEVplus.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe))
                .duration(whiteDwarfShapeSolidifierTime)
                .eut(TierEU.RECIPE_UMV)
                .addTo(fluidSolidifierRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Extruder_ToolHeadDrill.get(1))
                .itemOutputs(ItemList.White_Dwarf_Shape_Extruder_ToolHeadDrill.get(1))
                .fluidInputs(MaterialsUEVplus.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe))
                .duration(whiteDwarfShapeSolidifierTime)
                .eut(TierEU.RECIPE_UMV)
                .addTo(fluidSolidifierRecipes);

        }
    }
}
