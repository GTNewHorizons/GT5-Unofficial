package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.ModIDs.NewHorizonsCoreMod;
import static gregtech.api.enums.ModIDs.Railcraft;
import static gregtech.api.util.GT_ModHandler.getModItem;
import static net.minecraftforge.fluids.FluidRegistry.getFluidStack;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.GregTech_API;
import gregtech.api.enums.*;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

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
            if (materialArray[i].mStandardMoltenFluid != null) {
                GT_Values.RA.addFluidSolidifierRecipe(
                        ItemList.Shape_Mold_Casing.get(0L),
                        materialArray[i].getMolten(72L),
                        materialCasing[i],
                        16,
                        8);
            }
        }

        GT_Values.RA.addFluidSolidifierRecipe(
                ItemList.Shape_Mold_Ball.get(0L),
                Materials.Mercury.getFluid(1000L),
                getModItem("Thaumcraft", "ItemResource", 1, 3),
                128,
                4);
        GT_Values.RA.addFluidSolidifierRecipe(
                ItemList.Shape_Mold_Ball.get(0L),
                Materials.Mercury.getFluid(1000L),
                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Mercury, 1L),
                128,
                4);
        GT_Values.RA.addFluidSolidifierRecipe(
                ItemList.Shape_Mold_Ball.get(0L),
                Materials.Water.getFluid(250L),
                new ItemStack(Items.snowball, 1, 0),
                128,
                4);
        GT_Values.RA.addFluidSolidifierRecipe(
                ItemList.Shape_Mold_Ball.get(0L),
                GT_ModHandler.getDistilledWater(250L),
                new ItemStack(Items.snowball, 1, 0),
                128,
                4);
        GT_Values.RA.addFluidSolidifierRecipe(
                ItemList.Shape_Mold_Block.get(0L),
                Materials.Water.getFluid(1000L),
                new ItemStack(Blocks.snow, 1, 0),
                512,
                4);
        GT_Values.RA.addFluidSolidifierRecipe(
                ItemList.Shape_Mold_Block.get(0L),
                GT_ModHandler.getDistilledWater(1000L),
                new ItemStack(Blocks.snow, 1, 0),
                512,
                4);
        GT_Values.RA.addFluidSolidifierRecipe(
                ItemList.Shape_Mold_Block.get(0L),
                Materials.Lava.getFluid(1000L),
                new ItemStack(Blocks.obsidian, 1, 0),
                1024,
                16);
        GT_Values.RA.addFluidSolidifierRecipe(
                ItemList.Shape_Mold_Block.get(0L),
                Materials.Concrete.getMolten(144L),
                new ItemStack(GregTech_API.sBlockConcretes, 1, 8),
                12,
                4);
        GT_Values.RA.addFluidSolidifierRecipe(
                ItemList.Shape_Mold_Block.get(0L),
                Materials.Glowstone.getMolten(576L),
                new ItemStack(Blocks.glowstone, 1, 0),
                12,
                4);
        GT_Values.RA.addFluidSolidifierRecipe(
                ItemList.Shape_Mold_Block.get(0L),
                Materials.Glass.getMolten(144L),
                new ItemStack(Blocks.glass, 1, 0),
                12,
                4);
        GT_Values.RA.addFluidSolidifierRecipe(
                ItemList.Shape_Mold_Plate.get(0L),
                Materials.Glass.getMolten(144L),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Glass, 1L),
                12,
                4);
        GT_Values.RA.addFluidSolidifierRecipe(
                ItemList.Shape_Mold_Bottle.get(0L),
                Materials.Glass.getMolten(144L),
                ItemList.Bottle_Empty.get(1L),
                12,
                4);
        GT_Values.RA.addFluidSolidifierRecipe(
                ItemList.Shape_Mold_Cylinder.get(0L),
                Materials.Milk.getFluid(250L),
                ItemList.Food_Cheese.get(1L),
                1024,
                4);
        GT_Values.RA.addFluidSolidifierRecipe(
                ItemList.Shape_Mold_Cylinder.get(0L),
                Materials.Cheese.getMolten(144L),
                ItemList.Food_Cheese.get(1L),
                64,
                8);
        GT_Values.RA.addFluidSolidifierRecipe(
                ItemList.Shape_Mold_Anvil.get(0L),
                Materials.Iron.getMolten(4464L),
                new ItemStack(Blocks.anvil, 1, 0),
                128,
                16);
        GT_Values.RA.addFluidSolidifierRecipe(
                ItemList.Shape_Mold_Anvil.get(0L),
                Materials.WroughtIron.getMolten(4464L),
                new ItemStack(Blocks.anvil, 1, 0),
                128,
                16);
        GT_Values.RA.addFluidSolidifierRecipe(
                GT_Utility.getIntegratedCircuit(1),
                Materials.Boron.getMolten(144L),
                Materials.Boron.getDust(1),
                30,
                30);

        GT_Values.RA.addFluidSolidifierRecipe(
                ItemList.Shape_Mold_Cylinder.get(0),
                Materials.Polytetrafluoroethylene.getMolten(36),
                ItemList.Circuit_Parts_PetriDish.get(1),
                160,
                16);
        GT_Values.RA.addFluidSolidifierRecipe(
                ItemList.Shape_Mold_Cylinder.get(0),
                Materials.Polystyrene.getMolten(36),
                ItemList.Circuit_Parts_PetriDish.get(1),
                160,
                16);
        GT_Values.RA.addFluidSolidifierRecipe(
                ItemList.Shape_Mold_Cylinder.get(0),
                Materials.BorosilicateGlass.getMolten(72),
                ItemList.Circuit_Parts_PetriDish.get(1),
                160,
                16);

        GT_Values.RA.addFluidSolidifierRecipe(
                ItemList.Shape_Mold_Plate.get(0L),
                Materials.ReinforceGlass.getMolten(72),
                getModItem(NewHorizonsCoreMod.modID, "item.ReinforcedGlassPlate", 1L, 0),
                160,
                1920);
        GT_Values.RA.addFluidSolidifierRecipe(
                ItemList.Shape_Mold_Block.get(0L),
                Materials.ReinforceGlass.getMolten(144),
                getModItem("IC2", "blockAlloyGlass", 1L),
                160,
                1920);

        GT_Values.RA.addFluidSolidifierRecipe(
                ItemList.Shape_Mold_Ball.get(0L),
                Materials.Glass.getMolten(144),
                ItemList.Circuit_Parts_Glass_Tube.get(1),
                200,
                24);
        GT_Values.RA.addFluidSolidifierRecipe(
                ItemList.Shape_Mold_Ball.get(0L),
                Materials.ReinforceGlass.getMolten(288),
                ItemList.Circuit_Parts_Reinforced_Glass_Tube.get(1),
                200,
                240);
        GT_Values.RA.addFluidSolidifierRecipe(
                ItemList.Shape_Mold_Ball.get(0L),
                getFluidStack("glass.molten", 1000),
                ItemList.Circuit_Parts_Glass_Tube.get(1),
                200,
                24);
        GT_Values.RA.addFluidSolidifierRecipe(
                ItemList.Shape_Mold_Ball.get(0L),
                new FluidStack(ItemList.sToluene, 100),
                ItemList.GelledToluene.get(1),
                100,
                16);

        GT_Values.RA.addFluidSolidifierRecipe(
                ItemList.Shape_Mold_Nugget.get(0L),
                Materials.AnnealedCopper.getMolten(16),
                GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Copper, 1L),
                16,
                4);
        GT_Values.RA.addFluidSolidifierRecipe(
                ItemList.Shape_Mold_Ingot.get(0L),
                Materials.AnnealedCopper.getMolten(144),
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Copper, 1L),
                32,
                8);
        GT_Values.RA.addFluidSolidifierRecipe(
                ItemList.Shape_Mold_Block.get(0L),
                Materials.AnnealedCopper.getMolten(1296),
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Copper, 1L),
                288,
                8);

        GT_Values.RA.addFluidSolidifierRecipe(
                ItemList.Shape_Mold_Nugget.get(0L),
                Materials.WroughtIron.getMolten(16),
                GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Iron, 1L),
                16,
                4);
        GT_Values.RA.addFluidSolidifierRecipe(
                ItemList.Shape_Mold_Ingot.get(0L),
                Materials.WroughtIron.getMolten(144),
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, 1L),
                32,
                8);
        GT_Values.RA.addFluidSolidifierRecipe(
                ItemList.Shape_Mold_Block.get(0L),
                Materials.WroughtIron.getMolten(1296),
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Iron, 1L),
                288,
                8);

        GT_Values.RA.addFluidSolidifierRecipe(
                ItemList.Spinneret.get(0L),
                MaterialsKevlar.LiquidCrystalKevlar.getFluid(144L),
                ItemList.KevlarFiber.get(8L),
                800,
                1920);

        GT_Values.RA.addFluidSolidifierRecipe(
                ItemList.Shape_Mold_Anvil.get(0L),
                Materials.Steel.getMolten(4464L),
                getModItem(Railcraft.modID, "tile.railcraft.anvil", 1L, 0),
                128,
                16);

        final int whiteDwarfShapeSolidifierTime = 10 * 20;
        final int fluidPerShapeSolidifierRecipe = 576;
        {
            GT_Values.RA.addFluidSolidifierRecipe(
                    new ItemStack[] { ItemList.Shape_Extruder_Bottle.get(1) },
                    new FluidStack[] { Materials.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe) },
                    new ItemStack[] { ItemList.White_Dwarf_Shape_Extruder_Bottle.get(1) },
                    new FluidStack[] { GT_Values.NF },
                    (int) TierEU.RECIPE_UMV,
                    whiteDwarfShapeSolidifierTime);

            GT_Values.RA.addFluidSolidifierRecipe(
                    new ItemStack[] { ItemList.Shape_Extruder_Plate.get(1) },
                    new FluidStack[] { Materials.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe) },
                    new ItemStack[] { ItemList.White_Dwarf_Shape_Extruder_Plate.get(1) },
                    new FluidStack[] { GT_Values.NF },
                    (int) TierEU.RECIPE_UMV,
                    whiteDwarfShapeSolidifierTime);

            GT_Values.RA.addFluidSolidifierRecipe(
                    new ItemStack[] { ItemList.Shape_Extruder_Cell.get(1) },
                    new FluidStack[] { Materials.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe) },
                    new ItemStack[] { ItemList.White_Dwarf_Shape_Extruder_Cell.get(1) },
                    new FluidStack[] { GT_Values.NF },
                    (int) TierEU.RECIPE_UMV,
                    whiteDwarfShapeSolidifierTime);

            GT_Values.RA.addFluidSolidifierRecipe(
                    new ItemStack[] { ItemList.Shape_Extruder_Ring.get(1) },
                    new FluidStack[] { Materials.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe) },
                    new ItemStack[] { ItemList.White_Dwarf_Shape_Extruder_Ring.get(1) },
                    new FluidStack[] { GT_Values.NF },
                    (int) TierEU.RECIPE_UMV,
                    whiteDwarfShapeSolidifierTime);

            GT_Values.RA.addFluidSolidifierRecipe(
                    new ItemStack[] { ItemList.Shape_Extruder_Rod.get(1) },
                    new FluidStack[] { Materials.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe) },
                    new ItemStack[] { ItemList.White_Dwarf_Shape_Extruder_Rod.get(1) },
                    new FluidStack[] { GT_Values.NF },
                    (int) TierEU.RECIPE_UMV,
                    whiteDwarfShapeSolidifierTime);

            GT_Values.RA.addFluidSolidifierRecipe(
                    new ItemStack[] { ItemList.Shape_Extruder_Bolt.get(1) },
                    new FluidStack[] { Materials.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe) },
                    new ItemStack[] { ItemList.White_Dwarf_Shape_Extruder_Bolt.get(1) },
                    new FluidStack[] { GT_Values.NF },
                    (int) TierEU.RECIPE_UMV,
                    whiteDwarfShapeSolidifierTime);

            GT_Values.RA.addFluidSolidifierRecipe(
                    new ItemStack[] { ItemList.Shape_Extruder_Ingot.get(1) },
                    new FluidStack[] { Materials.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe) },
                    new ItemStack[] { ItemList.White_Dwarf_Shape_Extruder_Ingot.get(1) },
                    new FluidStack[] { GT_Values.NF },
                    (int) TierEU.RECIPE_UMV,
                    whiteDwarfShapeSolidifierTime);

            GT_Values.RA.addFluidSolidifierRecipe(
                    new ItemStack[] { ItemList.Shape_Extruder_Wire.get(1) },
                    new FluidStack[] { Materials.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe) },
                    new ItemStack[] { ItemList.White_Dwarf_Shape_Extruder_Wire.get(1) },
                    new FluidStack[] { GT_Values.NF },
                    (int) TierEU.RECIPE_UMV,
                    whiteDwarfShapeSolidifierTime);

            GT_Values.RA.addFluidSolidifierRecipe(
                    new ItemStack[] { ItemList.Shape_Extruder_Casing.get(1) },
                    new FluidStack[] { Materials.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe) },
                    new ItemStack[] { ItemList.White_Dwarf_Shape_Extruder_Casing.get(1) },
                    new FluidStack[] { GT_Values.NF },
                    (int) TierEU.RECIPE_UMV,
                    whiteDwarfShapeSolidifierTime);

            GT_Values.RA.addFluidSolidifierRecipe(
                    new ItemStack[] { ItemList.Shape_Extruder_Pipe_Tiny.get(1) },
                    new FluidStack[] { Materials.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe) },
                    new ItemStack[] { ItemList.White_Dwarf_Shape_Extruder_Pipe_Tiny.get(1) },
                    new FluidStack[] { GT_Values.NF },
                    (int) TierEU.RECIPE_UMV,
                    whiteDwarfShapeSolidifierTime);

            GT_Values.RA.addFluidSolidifierRecipe(
                    new ItemStack[] { ItemList.Shape_Extruder_Pipe_Small.get(1) },
                    new FluidStack[] { Materials.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe) },
                    new ItemStack[] { ItemList.White_Dwarf_Shape_Extruder_Pipe_Small.get(1) },
                    new FluidStack[] { GT_Values.NF },
                    (int) TierEU.RECIPE_UMV,
                    whiteDwarfShapeSolidifierTime);

            GT_Values.RA.addFluidSolidifierRecipe(
                    new ItemStack[] { ItemList.Shape_Extruder_Pipe_Medium.get(1) },
                    new FluidStack[] { Materials.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe) },
                    new ItemStack[] { ItemList.White_Dwarf_Shape_Extruder_Pipe_Medium.get(1) },
                    new FluidStack[] { GT_Values.NF },
                    (int) TierEU.RECIPE_UMV,
                    whiteDwarfShapeSolidifierTime);

            GT_Values.RA.addFluidSolidifierRecipe(
                    new ItemStack[] { ItemList.Shape_Extruder_Pipe_Large.get(1) },
                    new FluidStack[] { Materials.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe) },
                    new ItemStack[] { ItemList.White_Dwarf_Shape_Extruder_Pipe_Large.get(1) },
                    new FluidStack[] { GT_Values.NF },
                    (int) TierEU.RECIPE_UMV,
                    whiteDwarfShapeSolidifierTime);

            GT_Values.RA.addFluidSolidifierRecipe(
                    new ItemStack[] { ItemList.Shape_Extruder_Pipe_Huge.get(1) },
                    new FluidStack[] { Materials.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe) },
                    new ItemStack[] { ItemList.White_Dwarf_Shape_Extruder_Pipe_Huge.get(1) },
                    new FluidStack[] { GT_Values.NF },
                    (int) TierEU.RECIPE_UMV,
                    whiteDwarfShapeSolidifierTime);

            GT_Values.RA.addFluidSolidifierRecipe(
                    new ItemStack[] { ItemList.Shape_Extruder_Block.get(1) },
                    new FluidStack[] { Materials.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe) },
                    new ItemStack[] { ItemList.White_Dwarf_Shape_Extruder_Block.get(1) },
                    new FluidStack[] { GT_Values.NF },
                    (int) TierEU.RECIPE_UMV,
                    whiteDwarfShapeSolidifierTime);

            GT_Values.RA.addFluidSolidifierRecipe(
                    new ItemStack[] { ItemList.Shape_Extruder_Sword.get(1) },
                    new FluidStack[] { Materials.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe) },
                    new ItemStack[] { ItemList.White_Dwarf_Shape_Extruder_Sword.get(1) },
                    new FluidStack[] { GT_Values.NF },
                    (int) TierEU.RECIPE_UMV,
                    whiteDwarfShapeSolidifierTime);

            GT_Values.RA.addFluidSolidifierRecipe(
                    new ItemStack[] { ItemList.Shape_Extruder_Pickaxe.get(1) },
                    new FluidStack[] { Materials.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe) },
                    new ItemStack[] { ItemList.White_Dwarf_Shape_Extruder_Pickaxe.get(1) },
                    new FluidStack[] { GT_Values.NF },
                    (int) TierEU.RECIPE_UMV,
                    whiteDwarfShapeSolidifierTime);

            GT_Values.RA.addFluidSolidifierRecipe(
                    new ItemStack[] { ItemList.Shape_Extruder_Shovel.get(1) },
                    new FluidStack[] { Materials.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe) },
                    new ItemStack[] { ItemList.White_Dwarf_Shape_Extruder_Shovel.get(1) },
                    new FluidStack[] { GT_Values.NF },
                    (int) TierEU.RECIPE_UMV,
                    whiteDwarfShapeSolidifierTime);

            GT_Values.RA.addFluidSolidifierRecipe(
                    new ItemStack[] { ItemList.Shape_Extruder_Axe.get(1) },
                    new FluidStack[] { Materials.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe) },
                    new ItemStack[] { ItemList.White_Dwarf_Shape_Extruder_Axe.get(1) },
                    new FluidStack[] { GT_Values.NF },
                    (int) TierEU.RECIPE_UMV,
                    whiteDwarfShapeSolidifierTime);

            GT_Values.RA.addFluidSolidifierRecipe(
                    new ItemStack[] { ItemList.Shape_Extruder_Hoe.get(1) },
                    new FluidStack[] { Materials.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe) },
                    new ItemStack[] { ItemList.White_Dwarf_Shape_Extruder_Hoe.get(1) },
                    new FluidStack[] { GT_Values.NF },
                    (int) TierEU.RECIPE_UMV,
                    whiteDwarfShapeSolidifierTime);

            GT_Values.RA.addFluidSolidifierRecipe(
                    new ItemStack[] { ItemList.Shape_Extruder_Hammer.get(1) },
                    new FluidStack[] { Materials.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe) },
                    new ItemStack[] { ItemList.White_Dwarf_Shape_Extruder_Hammer.get(1) },
                    new FluidStack[] { GT_Values.NF },
                    (int) TierEU.RECIPE_UMV,
                    whiteDwarfShapeSolidifierTime);

            GT_Values.RA.addFluidSolidifierRecipe(
                    new ItemStack[] { ItemList.Shape_Extruder_File.get(1) },
                    new FluidStack[] { Materials.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe) },
                    new ItemStack[] { ItemList.White_Dwarf_Shape_Extruder_File.get(1) },
                    new FluidStack[] { GT_Values.NF },
                    (int) TierEU.RECIPE_UMV,
                    whiteDwarfShapeSolidifierTime);

            GT_Values.RA.addFluidSolidifierRecipe(
                    new ItemStack[] { ItemList.Shape_Extruder_Saw.get(1) },
                    new FluidStack[] { Materials.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe) },
                    new ItemStack[] { ItemList.White_Dwarf_Shape_Extruder_Saw.get(1) },
                    new FluidStack[] { GT_Values.NF },
                    (int) TierEU.RECIPE_UMV,
                    whiteDwarfShapeSolidifierTime);

            GT_Values.RA.addFluidSolidifierRecipe(
                    new ItemStack[] { ItemList.Shape_Extruder_Gear.get(1) },
                    new FluidStack[] { Materials.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe) },
                    new ItemStack[] { ItemList.White_Dwarf_Shape_Extruder_Gear.get(1) },
                    new FluidStack[] { GT_Values.NF },
                    (int) TierEU.RECIPE_UMV,
                    whiteDwarfShapeSolidifierTime);

            GT_Values.RA.addFluidSolidifierRecipe(
                    new ItemStack[] { ItemList.Shape_Extruder_Rotor.get(1) },
                    new FluidStack[] { Materials.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe) },
                    new ItemStack[] { ItemList.White_Dwarf_Shape_Extruder_Rotor.get(1) },
                    new FluidStack[] { GT_Values.NF },
                    (int) TierEU.RECIPE_UMV,
                    whiteDwarfShapeSolidifierTime);

            GT_Values.RA.addFluidSolidifierRecipe(
                    new ItemStack[] { ItemList.Shape_Extruder_Turbine_Blade.get(1) },
                    new FluidStack[] { Materials.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe) },
                    new ItemStack[] { ItemList.White_Dwarf_Shape_Extruder_Turbine_Blade.get(1) },
                    new FluidStack[] { GT_Values.NF },
                    (int) TierEU.RECIPE_UMV,
                    whiteDwarfShapeSolidifierTime);

            GT_Values.RA.addFluidSolidifierRecipe(
                    new ItemStack[] { ItemList.Shape_Extruder_Small_Gear.get(1) },
                    new FluidStack[] { Materials.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe) },
                    new ItemStack[] { ItemList.White_Dwarf_Shape_Extruder_Small_Gear.get(1) },
                    new FluidStack[] { GT_Values.NF },
                    (int) TierEU.RECIPE_UMV,
                    whiteDwarfShapeSolidifierTime);

            GT_Values.RA.addFluidSolidifierRecipe(
                    new ItemStack[] { ItemList.Shape_Extruder_ToolHeadDrill.get(1) },
                    new FluidStack[] { Materials.WhiteDwarfMatter.getMolten(fluidPerShapeSolidifierRecipe) },
                    new ItemStack[] { ItemList.White_Dwarf_Shape_Extruder_ToolHeadDrill.get(1) },
                    new FluidStack[] { GT_Values.NF },
                    (int) TierEU.RECIPE_UMV,
                    whiteDwarfShapeSolidifierTime);

        }
    }
}
