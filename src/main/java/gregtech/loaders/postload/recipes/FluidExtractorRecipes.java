package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.ModIDs.*;
import static gregtech.api.enums.ModIDs.Railcraft;
import static gregtech.api.util.GT_ModHandler.getModItem;
import static net.minecraftforge.fluids.FluidRegistry.getFluidStack;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_DummyWorld;

public class FluidExtractorRecipes implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.addFluidExtractionRecipe(
                ItemList.Dye_SquidInk.get(1L),
                GT_Values.NI,
                getFluidStack("squidink", 144),
                10000,
                128,
                4);
        GT_Values.RA.addFluidExtractionRecipe(
                ItemList.Dye_Indigo.get(1L),
                GT_Values.NI,
                getFluidStack("indigo", 144),
                10000,
                128,
                4);
        GT_Values.RA.addFluidExtractionRecipe(
                ItemList.Crop_Drop_Indigo.get(1L),
                GT_Values.NI,
                getFluidStack("indigo", 144),
                10000,
                128,
                4);
        GT_Values.RA.addFluidExtractionRecipe(
                ItemList.Crop_Drop_MilkWart.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Milk, 1L),
                GT_ModHandler.getMilk(150L),
                1000,
                128,
                4);
        GT_Values.RA.addFluidExtractionRecipe(
                ItemList.Crop_Drop_OilBerry.get(1L),
                GT_Values.NI,
                Materials.Oil.getFluid(100L),
                10000,
                128,
                4);
        GT_Values.RA.addFluidExtractionRecipe(
                ItemList.Crop_Drop_UUMBerry.get(1L),
                GT_Values.NI,
                Materials.UUMatter.getFluid(4L),
                10000,
                128,
                4);
        GT_Values.RA.addFluidExtractionRecipe(
                ItemList.Crop_Drop_UUABerry.get(1L),
                GT_Values.NI,
                Materials.UUAmplifier.getFluid(4L),
                10000,
                128,
                4);
        GT_Values.RA.addFluidExtractionRecipe(
                new ItemStack(Items.fish, 1, 0),
                GT_Values.NI,
                Materials.FishOil.getFluid(40L),
                10000,
                16,
                4);
        GT_Values.RA.addFluidExtractionRecipe(
                new ItemStack(Items.fish, 1, 1),
                GT_Values.NI,
                Materials.FishOil.getFluid(60L),
                10000,
                16,
                4);
        GT_Values.RA.addFluidExtractionRecipe(
                new ItemStack(Items.fish, 1, 2),
                GT_Values.NI,
                Materials.FishOil.getFluid(70L),
                10000,
                16,
                4);
        GT_Values.RA.addFluidExtractionRecipe(
                new ItemStack(Items.fish, 1, 3),
                GT_Values.NI,
                Materials.FishOil.getFluid(30L),
                10000,
                16,
                4);

        GT_Values.RA.addFluidExtractionRecipe(
                new ItemStack(Items.coal, 1, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 1L),
                Materials.WoodTar.getFluid(100L),
                1000,
                30,
                16);
        GT_Values.RA.addFluidExtractionRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 1L),
                ItemList.IC2_Plantball.get(1L),
                Materials.Creosote.getFluid(5L),
                100,
                16,
                4);
        GT_Values.RA.addFluidExtractionRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.HydratedCoal, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 1L),
                Materials.Water.getFluid(100L),
                10000,
                32,
                4);
        GT_Values.RA.addFluidExtractionRecipe(
                getModItem("Thaumcraft", "ItemResource", 1, 3),
                GT_Values.NI,
                Materials.Mercury.getFluid(1000L),
                10000,
                128,
                4);
        GT_Values.RA.addFluidExtractionRecipe(
                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Mercury, 1L),
                GT_Values.NI,
                Materials.Mercury.getFluid(1000L),
                10000,
                128,
                4);
        GT_Values.RA.addFluidExtractionRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Monazite, 1L),
                GT_Values.NI,
                Materials.Helium.getGas(200L),
                10000,
                64,
                64);

        GT_Values.RA.addFluidExtractionRecipe(
                getModItem("IC2", "blockAlloyGlass", 1L, 0),
                GT_Values.NI,
                Materials.ReinforceGlass.getMolten(144),
                10000,
                100,
                1920);
        GT_Values.RA.addFluidExtractionRecipe(
                getModItem(NewHorizonsCoreMod.modID, "item.ReinforcedGlassPlate", 1L, 0),
                GT_Values.NI,
                Materials.ReinforceGlass.getMolten(72),
                10000,
                50,
                1920);
        GT_Values.RA.addFluidExtractionRecipe(
                getModItem(NewHorizonsCoreMod.modID, "item.ReinforcedGlassLense", 1L, 0),
                GT_Values.NI,
                Materials.ReinforceGlass.getMolten(54),
                10000,
                50,
                1920);

        GT_Values.RA.addFluidExtractionRecipe(
                ItemList.Long_Distance_Pipeline_Fluid.get(1L),
                GT_Values.NI,
                Materials.Steel.getMolten(19 * 144),
                10000,
                400,
                90);

        GT_Values.RA.addFluidExtractionRecipe(
                ItemList.Long_Distance_Pipeline_Item.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Steel, 7L),
                Materials.Tin.getMolten(12 * 144),
                10000,
                400,
                90);

        GT_Values.RA.addFluidExtractionRecipe(
                ItemList.Long_Distance_Pipeline_Fluid_Pipe.get(4L),
                GT_Values.NI,
                Materials.Steel.getMolten(189),
                10000,
                40,
                90);

        GT_Values.RA.addFluidExtractionRecipe(
                ItemList.Long_Distance_Pipeline_Item_Pipe.get(16L),
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Tin, 3L),
                Materials.Steel.getMolten(324),
                10000,
                400,
                90);

        GT_Values.RA.addFluidExtractionRecipe(
                GT_ModHandler.getIC2Item("TritiumCell", 1),
                GT_ModHandler.getIC2Item("fuelRod", 1),
                Materials.Tritium.getGas(32),
                10000,
                16,
                64);

        GT_Values.RA.addFluidExtractionRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Quartzite, 1L),
                null,
                Materials.Glass.getMolten(72),
                10000,
                600,
                28);

        GT_Values.RA.addFluidExtractionRecipe(
                getModItem(Railcraft.modID, "machine.beta", 1L, 0),
                GT_Values.NI,
                Materials.Iron.getMolten(288),
                10000,
                300,
                60);
        GT_Values.RA.addFluidExtractionRecipe(
                getModItem(Railcraft.modID, "machine.beta", 1L, 1),
                GT_Values.NI,
                Materials.Iron.getMolten(144),
                10000,
                300,
                60);
        GT_Values.RA.addFluidExtractionRecipe(
                getModItem(Railcraft.modID, "machine.beta", 1L, 2),
                GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Iron, 6),
                Materials.Bronze.getMolten(1728),
                10000,
                300,
                60);

        GT_Values.RA.addFluidExtractionRecipe(
                getModItem(Railcraft.modID, "machine.beta", 1L, 13),
                GT_Values.NI,
                Materials.Steel.getMolten(288),
                10000,
                400,
                90);
        GT_Values.RA.addFluidExtractionRecipe(
                getModItem(Railcraft.modID, "machine.beta", 1L, 14),
                GT_Values.NI,
                Materials.Steel.getMolten(144),
                10000,
                400,
                90);
        GT_Values.RA.addFluidExtractionRecipe(
                getModItem(Railcraft.modID, "machine.beta", 1L, 15),
                GT_Values.NI,
                Materials.Steel.getMolten(1836),
                10000,
                400,
                90);

        GT_Values.RA.addFluidExtractionRecipe(
                getModItem(Railcraft.modID, "machine.zeta", 1L, 0),
                GT_Values.NI,
                Materials.Aluminium.getMolten(288),
                10000,
                500,
                120);
        GT_Values.RA.addFluidExtractionRecipe(
                getModItem(Railcraft.modID, "machine.zeta", 1L, 1),
                GT_Values.NI,
                Materials.Aluminium.getMolten(144),
                10000,
                500,
                120);
        GT_Values.RA.addFluidExtractionRecipe(
                getModItem(Railcraft.modID, "machine.zeta", 1L, 2),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 12L),
                Materials.Aluminium.getMolten(108L),
                10000,
                500,
                120);

        GT_Values.RA.addFluidExtractionRecipe(
                getModItem(Railcraft.modID, "machine.zeta", 1L, 3),
                GT_Values.NI,
                Materials.StainlessSteel.getMolten(288),
                10000,
                600,
                180);
        GT_Values.RA.addFluidExtractionRecipe(
                getModItem(Railcraft.modID, "machine.zeta", 1L, 4),
                GT_Values.NI,
                Materials.StainlessSteel.getMolten(144),
                10000,
                600,
                180);
        GT_Values.RA.addFluidExtractionRecipe(
                getModItem(Railcraft.modID, "machine.zeta", 1L, 5),
                GT_Values.NI,
                Materials.StainlessSteel.getMolten(1836),
                10000,
                600,
                180);

        GT_Values.RA.addFluidExtractionRecipe(
                getModItem(Railcraft.modID, "machine.zeta", 1L, 6),
                GT_Values.NI,
                Materials.Titanium.getMolten(288),
                10000,
                700,
                240);
        GT_Values.RA.addFluidExtractionRecipe(
                getModItem(Railcraft.modID, "machine.zeta", 1L, 7),
                GT_Values.NI,
                Materials.Titanium.getMolten(144),
                10000,
                700,
                240);
        GT_Values.RA.addFluidExtractionRecipe(
                getModItem(Railcraft.modID, "machine.zeta", 1L, 8),
                GT_Values.NI,
                Materials.Titanium.getMolten(1836),
                10000,
                700,
                240);

        GT_Values.RA.addFluidExtractionRecipe(
                getModItem(Railcraft.modID, "machine.zeta", 1L, 9),
                GT_Values.NI,
                Materials.TungstenSteel.getMolten(288),
                10000,
                800,
                360);
        GT_Values.RA.addFluidExtractionRecipe(
                getModItem(Railcraft.modID, "machine.zeta", 1L, 10),
                GT_Values.NI,
                Materials.TungstenSteel.getMolten(144),
                10000,
                800,
                360);
        GT_Values.RA.addFluidExtractionRecipe(
                getModItem(Railcraft.modID, "machine.zeta", 1L, 11),
                GT_Values.NI,
                Materials.TungstenSteel.getMolten(1836),
                10000,
                800,
                360);

        GT_Values.RA.addFluidExtractionRecipe(
                getModItem(Railcraft.modID, "machine.zeta", 1L, 12),
                GT_Values.NI,
                Materials.Palladium.getMolten(288),
                10000,
                900,
                480);
        GT_Values.RA.addFluidExtractionRecipe(
                getModItem(Railcraft.modID, "machine.zeta", 1L, 13),
                GT_Values.NI,
                Materials.Palladium.getMolten(144),
                10000,
                900,
                480);
        GT_Values.RA.addFluidExtractionRecipe(
                getModItem(Railcraft.modID, "machine.zeta", 1L, 14),
                GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Chrome, 6L),
                Materials.NiobiumTitanium.getMolten(1728),
                10000,
                900,
                480);

        GT_Values.RA.addFluidExtractionRecipe(
                getModItem(Railcraft.modID, "machine.eta", 1L, 0),
                GT_Values.NI,
                Materials.Iridium.getMolten(288),
                10000,
                1000,
                720);
        GT_Values.RA.addFluidExtractionRecipe(
                getModItem(Railcraft.modID, "machine.eta", 1L, 1),
                GT_Values.NI,
                Materials.Iridium.getMolten(144),
                10000,
                1000,
                720);
        GT_Values.RA.addFluidExtractionRecipe(
                getModItem(Railcraft.modID, "machine.eta", 1L, 2),
                GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Iridium, 6L),
                Materials.Enderium.getMolten(1728),
                10000,
                1000,
                720);

        GT_Values.RA.addFluidExtractionRecipe(
                getModItem(Railcraft.modID, "machine.eta", 1L, 3),
                GT_Values.NI,
                Materials.Osmium.getMolten(288),
                10000,
                1100,
                960);
        GT_Values.RA.addFluidExtractionRecipe(
                getModItem(Railcraft.modID, "machine.eta", 1L, 4),
                GT_Values.NI,
                Materials.Osmium.getMolten(144),
                10000,
                1100,
                960);
        GT_Values.RA.addFluidExtractionRecipe(
                getModItem(Railcraft.modID, "machine.eta", 1L, 5),
                GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Osmium, 6L),
                Materials.Naquadah.getMolten(1728),
                10000,
                1100,
                960);

        GT_Values.RA.addFluidExtractionRecipe(
                getModItem(Railcraft.modID, "machine.eta", 1L, 6),
                GT_Values.NI,
                Materials.Neutronium.getMolten(288),
                10000,
                1200,
                1440);
        GT_Values.RA.addFluidExtractionRecipe(
                getModItem(Railcraft.modID, "machine.eta", 1L, 7),
                GT_Values.NI,
                Materials.Neutronium.getMolten(144),
                10000,
                1200,
                1440);
        GT_Values.RA.addFluidExtractionRecipe(
                getModItem(Railcraft.modID, "machine.eta", 1L, 8),
                GT_Values.NI,
                Materials.Neutronium.getMolten(1836),
                10000,
                1200,
                1440);

        GT_Values.RA.addFluidExtractionRecipe(
                new ItemStack(Items.wheat_seeds, 1, 32767),
                GT_Values.NI,
                Materials.SeedOil.getFluid(10),
                10000,
                32,
                2);
        GT_Values.RA.addFluidExtractionRecipe(
                new ItemStack(Items.melon_seeds, 1, 32767),
                GT_Values.NI,
                Materials.SeedOil.getFluid(10),
                10000,
                32,
                2);
        GT_Values.RA.addFluidExtractionRecipe(
                new ItemStack(Items.pumpkin_seeds, 1, 32767),
                GT_Values.NI,
                Materials.SeedOil.getFluid(10),
                10000,
                32,
                2);
        GT_Values.RA.addFluidExtractionRecipe(
                ItemList.Crop_Drop_Rape.get(1),
                null,
                Materials.SeedOil.getFluid(125),
                10000,
                32,
                2);

        GT_Values.RA.addFluidSmelterRecipe(
                new ItemStack(Items.snowball, 1, 0),
                GT_Values.NI,
                Materials.Water.getFluid(250L),
                10000,
                32,
                4);
        GT_Values.RA.addFluidSmelterRecipe(
                new ItemStack(Blocks.snow, 1, 0),
                GT_Values.NI,
                Materials.Water.getFluid(1000L),
                10000,
                128,
                4);
        GT_Values.RA.addFluidSmelterRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ice, 1L),
                GT_Values.NI,
                Materials.Ice.getSolid(1000L),
                10000,
                128,
                4);
        GT_Values.RA.addFluidSmelterRecipe(
                getModItem(Forestry.modID, "phosphor", 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Phosphorus, 1L),
                Materials.Lava.getFluid(800L),
                1000,
                256,
                128);

        try {
            GT_DummyWorld tWorld = (GT_DummyWorld) GT_Values.DW;
            while (tWorld.mRandom.mIterationStep > 0) {
                GT_Values.RA.addFluidExtractionRecipe(
                        GT_Utility.copyAmount(1L, ForgeHooks.getGrassSeed(tWorld)),
                        GT_Values.NI,
                        Materials.SeedOil.getFluid(5L),
                        10000,
                        64,
                        2);
            }
        } catch (Throwable e) {
            GT_Log.out.println(
                    "GT_Mod: failed to iterate somehow, maybe it's your Forge Version causing it. But it's not that important\n");
            e.printStackTrace(GT_Log.err);
        }

        // Beecombs fluid extractor recipes
        if (BartWorks.isModLoaded()) {
            // xenon
            GT_Values.RA.addFluidExtractionRecipe(
                    getModItem("gregtech", "gt.comb", 1L, 134),
                    null,
                    getFluidStack("xenon", 250),
                    100 * 100,
                    50,
                    8192);
            // neon
            GT_Values.RA.addFluidExtractionRecipe(
                    getModItem("gregtech", "gt.comb", 1L, 135),
                    null,
                    getFluidStack("neon", 250),
                    100 * 100,
                    15,
                    8192);
            // krpton
            GT_Values.RA.addFluidExtractionRecipe(
                    getModItem("gregtech", "gt.comb", 1L, 136),
                    null,
                    getFluidStack("krypton", 250),
                    100 * 100,
                    25,
                    8192);
        }
    }
}
