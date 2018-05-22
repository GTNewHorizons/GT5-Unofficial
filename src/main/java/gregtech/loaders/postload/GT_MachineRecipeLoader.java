package gregtech.loaders.postload;

import codechicken.nei.api.API;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.*;
import gregtech.api.objects.MaterialStack;
import gregtech.api.util.*;
import gregtech.common.GT_DummyWorld;
import gregtech.common.items.GT_MetaGenerated_Item_03;
import ic2.api.recipe.ILiquidHeatExchangerManager.HeatExchangeProperty;
import ic2.api.recipe.Recipes;
import mods.railcraft.common.blocks.aesthetics.cube.EnumCube;
import mods.railcraft.common.items.RailcraftToolItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

public class GT_MachineRecipeLoader implements Runnable {
    private final MaterialStack[][] mAlloySmelterList = {
            {new MaterialStack(Materials.Tetrahedrite, 3L), new MaterialStack(Materials.Tin, 1L), new MaterialStack(Materials.Bronze, 3L)},
            {new MaterialStack(Materials.Tetrahedrite, 3L), new MaterialStack(Materials.Zinc, 1L), new MaterialStack(Materials.Brass, 3L)},
            {new MaterialStack(Materials.Copper, 3L), new MaterialStack(Materials.Tin, 1L), new MaterialStack(Materials.Bronze, 4L)},
            {new MaterialStack(Materials.Copper, 3L), new MaterialStack(Materials.Zinc, 1L), new MaterialStack(Materials.Brass, 4L)},
            {new MaterialStack(Materials.Copper, 1L), new MaterialStack(Materials.Nickel, 1L), new MaterialStack(Materials.Cupronickel, 2L)},
            {new MaterialStack(Materials.Copper, 1L), new MaterialStack(Materials.Redstone, 4L), new MaterialStack(Materials.RedAlloy, 1L)},
            {new MaterialStack(Materials.AnnealedCopper, 3L), new MaterialStack(Materials.Tin, 1L), new MaterialStack(Materials.Bronze, 4L)},
            {new MaterialStack(Materials.AnnealedCopper, 3L), new MaterialStack(Materials.Zinc, 1L), new MaterialStack(Materials.Brass, 4L)},
            {new MaterialStack(Materials.AnnealedCopper, 1L), new MaterialStack(Materials.Nickel, 1L), new MaterialStack(Materials.Cupronickel, 2L)},
            {new MaterialStack(Materials.AnnealedCopper, 1L), new MaterialStack(Materials.Redstone, 4L), new MaterialStack(Materials.RedAlloy, 1L)},
            {new MaterialStack(Materials.Iron, 1L), new MaterialStack(Materials.Tin, 1L), new MaterialStack(Materials.TinAlloy, 2L)},
            {new MaterialStack(Materials.WroughtIron, 1L), new MaterialStack(Materials.Tin, 1L), new MaterialStack(Materials.TinAlloy, 2L)},
            {new MaterialStack(Materials.Iron, 2L), new MaterialStack(Materials.Nickel, 1L), new MaterialStack(Materials.Invar, 3L)},
            {new MaterialStack(Materials.WroughtIron, 2L), new MaterialStack(Materials.Nickel, 1L), new MaterialStack(Materials.Invar, 3L)},
            {new MaterialStack(Materials.Tin, 9L), new MaterialStack(Materials.Antimony, 1L), new MaterialStack(Materials.SolderingAlloy, 10L)},
            {new MaterialStack(Materials.Lead, 4L), new MaterialStack(Materials.Antimony, 1L), new MaterialStack(Materials.BatteryAlloy, 5L)},
            {new MaterialStack(Materials.Gold, 1L), new MaterialStack(Materials.Silver, 1L), new MaterialStack(Materials.Electrum, 2L)},
            {new MaterialStack(Materials.Magnesium, 1L), new MaterialStack(Materials.Aluminium, 2L), new MaterialStack(Materials.Magnalium, 3L)},
            {new MaterialStack(Materials.Silver, 1L), new MaterialStack(Materials.Nikolite, 4L), new MaterialStack(Materials.BlueAlloy, 1L)},
            {new MaterialStack(Materials.Boron, 1L), new MaterialStack(Materials.Glass, 7L), new MaterialStack(Materials.BorosilicateGlass, 8L)}};
    private final static String aTextAE = "appliedenergistics2"; private final static String aTextAEMM = "item.ItemMultiMaterial"; private final static String aTextForestry = "Forestry";
    private final static String aTextEBXL = "ExtrabiomesXL"; private final static String aTextTCGTPage = "gt.research.page.1.";
    private final static Boolean isNEILoaded = Loader.isModLoaded("NotEnoughItems");
    
    public void run() {
        GT_Log.out.println("GT_Mod: Adding non-OreDict Machine Recipes.");
        try {
            GT_Utility.removeSimpleIC2MachineRecipe(GT_Values.NI, ic2.api.recipe.Recipes.metalformerExtruding.getRecipes(), ItemList.Cell_Empty.get(3L, new Object[0]));
            GT_Utility.removeSimpleIC2MachineRecipe(ItemList.IC2_Energium_Dust.get(1L, new Object[0]), ic2.api.recipe.Recipes.compressor.getRecipes(), GT_Values.NI);
            GT_Utility.removeSimpleIC2MachineRecipe(new ItemStack(Items.gunpowder), ic2.api.recipe.Recipes.extractor.getRecipes(), GT_Values.NI);
            GT_Utility.removeSimpleIC2MachineRecipe(new ItemStack(Blocks.wool, 1, 32767), ic2.api.recipe.Recipes.extractor.getRecipes(), GT_Values.NI);
			GT_Utility.removeSimpleIC2MachineRecipe(new ItemStack(Blocks.gravel), ic2.api.recipe.Recipes.oreWashing.getRecipes(), GT_Values.NI);
            } catch (Throwable e) {
        }
        GT_Utility.removeIC2BottleRecipe(GT_ModHandler.getIC2Item("fuelRod", 1), GT_ModHandler.getIC2Item("UranFuel", 1), ic2.api.recipe.Recipes.cannerBottle.getRecipes(), GT_ModHandler.getIC2Item("reactorUraniumSimple", 1, 1));
        GT_Utility.removeIC2BottleRecipe(GT_ModHandler.getIC2Item("fuelRod", 1), GT_ModHandler.getIC2Item("MOXFuel", 1), ic2.api.recipe.Recipes.cannerBottle.getRecipes(), GT_ModHandler.getIC2Item("reactorMOXSimple", 1, 1));
        GT_Values.RA.addFluidExtractionRecipe(new ItemStack(Items.wheat_seeds, 1, 32767), GT_Values.NI, Materials.SeedOil.getFluid(10), 10000, 32, 2);
        GT_Values.RA.addFluidExtractionRecipe(new ItemStack(Items.melon_seeds, 1, 32767), GT_Values.NI, Materials.SeedOil.getFluid(10), 10000, 32, 2);
        GT_Values.RA.addFluidExtractionRecipe(new ItemStack(Items.pumpkin_seeds, 1, 32767), GT_Values.NI, Materials.SeedOil.getFluid(10), 10000, 32, 2);
        GT_Values.RA.addFluidExtractionRecipe(ItemList.Crop_Drop_Rape.get(1, new Object[0]), null, Materials.SeedOil.getFluid(50), 10000, 32, 2);
        try {
            GT_DummyWorld tWorld = (GT_DummyWorld) GT_Values.DW;
            while (tWorld.mRandom.mIterationStep > 0) {
                GT_Values.RA.addFluidExtractionRecipe(GT_Utility.copyAmount(1L, new Object[]{ForgeHooks.getGrassSeed(tWorld)}), GT_Values.NI, Materials.SeedOil.getFluid(5L), 10000, 64, 2);
            }
        } catch (Throwable e) {
            GT_Log.out.println("GT_Mod: failed to iterate somehow, maybe it's your Forge Version causing it. But it's not that important\n");
            e.printStackTrace(GT_Log.err);
        }

//        GT_Values.RA.addArcFurnaceRecipe(ItemList.Block_BronzePlate.get(1, new Object[]{}), new ItemStack[]{ GT_OreDictUnificator.get(OrePrefixes.ingot,Materials.Bronze,4), GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Stone,1)}, null, 160, 96);
//        GT_Values.RA.addArcFurnaceRecipe(ItemList.Block_IridiumTungstensteel.get(1, new Object[]{}), new ItemStack[]{ GT_OreDictUnificator.get(OrePrefixes.ingot,Materials.Bronze,4), GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Stone,1)}, null, 160, 96);
        GT_Values.RA.addArcFurnaceRecipe(ItemList.Block_TungstenSteelReinforced.get(1, new Object[]{}), new ItemStack[]{ GT_OreDictUnificator.get(OrePrefixes.ingot,Materials.TungstenSteel,2), GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Concrete,1)}, null, 160, 96);

        //Temporary until circuit overhaul
//        GT_Values.RA.addAlloySmelterRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Rubber, 2), GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Copper, 1), GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Copper, 1), 100, 16);

        GT_Values.RA.addPrinterRecipe(GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Paper, 1L), FluidRegistry.getFluidStack("squidink", 36), GT_Values.NI, ItemList.Paper_Punch_Card_Empty.get(1L, new Object[0]), 100, 2);
        GT_Values.RA.addPrinterRecipe(ItemList.Paper_Punch_Card_Empty.get(1L, new Object[0]), FluidRegistry.getFluidStack("squidink", 36), ItemList.Tool_DataStick.getWithName(0L, "With Punch Card Data", new Object[0]), ItemList.Paper_Punch_Card_Encoded.get(1L, new Object[0]), 100, 2);
        GT_Values.RA.addPrinterRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Paper, 3L), FluidRegistry.getFluidStack("squidink", 144), ItemList.Tool_DataStick.getWithName(0L, "With Scanned Book Data", new Object[0]), ItemList.Paper_Printed_Pages.get(1L, new Object[0]), 400, 2);
        GT_Values.RA.addPrinterRecipe(new ItemStack(Items.map, 1, 32767), FluidRegistry.getFluidStack("squidink", 144), ItemList.Tool_DataStick.getWithName(0L, "With Scanned Map Data", new Object[0]), new ItemStack(Items.filled_map, 1, 0), 400, 2);
        GT_Values.RA.addPrinterRecipe(new ItemStack(Items.book, 1, 32767), FluidRegistry.getFluidStack("squidink", 144), GT_Values.NI, GT_Utility.getWrittenBook("Manual_Printer", ItemList.Book_Written_01.get(1L, new Object[0])), 400, 2);
        for (OrePrefixes tPrefix : Arrays.asList(new OrePrefixes[]{OrePrefixes.dust, OrePrefixes.dustSmall, OrePrefixes.dustTiny})) {
            GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(tPrefix, Materials.EnderPearl, 1L), GT_OreDictUnificator.get(tPrefix, Materials.Blaze, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.EnderEye, 1L * tPrefix.mMaterialAmount), (int) (100L * tPrefix.mMaterialAmount / 3628800L), 48);
            GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(tPrefix, Materials.Gold, 1L), GT_OreDictUnificator.get(tPrefix, Materials.Silver, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.Electrum, 2L * tPrefix.mMaterialAmount), (int) (200L * tPrefix.mMaterialAmount / 3628800L), 8);
            GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(tPrefix, Materials.Iron, 2L), GT_OreDictUnificator.get(tPrefix, Materials.Nickel, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.Invar, 3L * tPrefix.mMaterialAmount), (int) (300L * tPrefix.mMaterialAmount / 3628800L), 8);
            GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(tPrefix, Materials.Iron, 4L), GT_OreDictUnificator.get(tPrefix, Materials.Invar, 3L), GT_OreDictUnificator.get(tPrefix, Materials.Manganese, 1L), GT_OreDictUnificator.get(tPrefix, Materials.Chrome, 1L), GT_Values.NI, GT_Utility.getIntegratedCircuit(1), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.StainlessSteel, 9L * tPrefix.mMaterialAmount), (int) (900L * tPrefix.mMaterialAmount / 3628800L), 120);
            GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(tPrefix, Materials.Iron, 1L), GT_OreDictUnificator.get(tPrefix, Materials.Aluminium, 1L), GT_OreDictUnificator.get(tPrefix, Materials.Chrome, 1L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.Kanthal, 3L * tPrefix.mMaterialAmount), (int) (300L * tPrefix.mMaterialAmount / 3628800L), 120);
            //GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(tPrefix, Materials.Copper, 3L), GT_OreDictUnificator.get(tPrefix, Materials.Barium, 2L), GT_OreDictUnificator.get(tPrefix, Materials.Yttrium, 1L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.YttriumBariumCuprate, 6L * tPrefix.mMaterialAmount), (int) (600L * tPrefix.mMaterialAmount / 3628800L), 8);
            GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(tPrefix, Materials.Copper, 3L), GT_OreDictUnificator.get(tPrefix, Materials.Zinc, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.Brass, 4L * tPrefix.mMaterialAmount), (int) (400L * tPrefix.mMaterialAmount / 3628800L), 8);
            GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(tPrefix, Materials.Copper, 3L), GT_OreDictUnificator.get(tPrefix, Materials.Tin, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.Bronze, 4L * tPrefix.mMaterialAmount), (int) (400L * tPrefix.mMaterialAmount / 3628800L), 8);
            GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(tPrefix, Materials.Copper, 1L), GT_OreDictUnificator.get(tPrefix, Materials.Nickel, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.Cupronickel, 2L * tPrefix.mMaterialAmount), (int) (200L * tPrefix.mMaterialAmount / 3628800L), 24);
            GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(tPrefix, Materials.Copper, 1L), GT_OreDictUnificator.get(tPrefix, Materials.Gold, 4L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.RoseGold, 5L * tPrefix.mMaterialAmount), (int) (500L * tPrefix.mMaterialAmount / 3628800L), 120);
            GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(tPrefix, Materials.Copper, 1L), GT_OreDictUnificator.get(tPrefix, Materials.Silver, 4L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.SterlingSilver, 5L * tPrefix.mMaterialAmount), (int) (500L * tPrefix.mMaterialAmount / 3628800L), 120);
            GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(tPrefix, Materials.Copper, 3L), GT_OreDictUnificator.get(tPrefix, Materials.Electrum, 2L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.BlackBronze, 5L * tPrefix.mMaterialAmount), (int) (500L * tPrefix.mMaterialAmount / 3628800L), 8);
            GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(tPrefix, Materials.Bismuth, 1L), GT_OreDictUnificator.get(tPrefix, Materials.Brass, 4L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.BismuthBronze, 5L * tPrefix.mMaterialAmount), (int) (500L * tPrefix.mMaterialAmount / 3628800L), 8);
            GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(tPrefix, Materials.BlackBronze, 1L), GT_OreDictUnificator.get(tPrefix, Materials.Nickel, 1L), GT_OreDictUnificator.get(tPrefix, Materials.Steel, 3L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.BlackSteel, 5L * tPrefix.mMaterialAmount), (int) (500L * tPrefix.mMaterialAmount / 3628800L), 8);
            GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(tPrefix, Materials.SterlingSilver, 1L), GT_OreDictUnificator.get(tPrefix, Materials.BismuthBronze, 1L), GT_OreDictUnificator.get(tPrefix, Materials.BlackSteel, 4L), GT_OreDictUnificator.get(tPrefix, Materials.Steel, 2L), GT_Values.NI, GT_Utility.getIntegratedCircuit(1), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.RedSteel, 8L * tPrefix.mMaterialAmount), (int) (800L * tPrefix.mMaterialAmount / 3628800L), 8);
            GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(tPrefix, Materials.RoseGold, 1L), GT_OreDictUnificator.get(tPrefix, Materials.Brass, 1L), GT_OreDictUnificator.get(tPrefix, Materials.BlackSteel, 4L), GT_OreDictUnificator.get(tPrefix, Materials.Steel, 2L), GT_Values.NI, GT_Utility.getIntegratedCircuit(1), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.BlueSteel, 8L * tPrefix.mMaterialAmount), (int) (800L * tPrefix.mMaterialAmount / 3628800L), 8);
            GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(tPrefix, Materials.Cobalt, 5L), GT_OreDictUnificator.get(tPrefix, Materials.Chrome, 2L), GT_OreDictUnificator.get(tPrefix, Materials.Nickel, 1L), GT_OreDictUnificator.get(tPrefix, Materials.Molybdenum, 1L), GT_Values.NI, GT_Utility.getIntegratedCircuit(1),GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.Ultimet, 9L * tPrefix.mMaterialAmount), (int) (900L * tPrefix.mMaterialAmount / 3628800L), 500);
            GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(tPrefix, Materials.Brass, 7L), GT_OreDictUnificator.get(tPrefix, Materials.Aluminium, 1L), GT_OreDictUnificator.get(tPrefix, Materials.Cobalt, 1L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.CobaltBrass, 9L * tPrefix.mMaterialAmount), (int) (900L * tPrefix.mMaterialAmount / 3628800L), 8);
            GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(tPrefix, Materials.Saltpeter, 2L), GT_OreDictUnificator.get(tPrefix, Materials.Sulfur, 1L), GT_OreDictUnificator.get(tPrefix, Materials.Coal, 3L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.Gunpowder, 6L * tPrefix.mMaterialAmount), (int) (600L * tPrefix.mMaterialAmount / 3628800L), 8);
            GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(tPrefix, Materials.Saltpeter, 2L), GT_OreDictUnificator.get(tPrefix, Materials.Sulfur, 1L), GT_OreDictUnificator.get(tPrefix, Materials.Charcoal, 3L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.Gunpowder, 6L * tPrefix.mMaterialAmount), (int) (600L * tPrefix.mMaterialAmount / 3628800L), 8);
            GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(tPrefix, Materials.Saltpeter, 2L), GT_OreDictUnificator.get(tPrefix, Materials.Sulfur, 1L), GT_OreDictUnificator.get(tPrefix, Materials.Carbon, 3L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.Gunpowder, 6L * tPrefix.mMaterialAmount), (int) (600L * tPrefix.mMaterialAmount / 3628800L), 8);

            GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(tPrefix, Materials.Indium, 1L), GT_OreDictUnificator.get(tPrefix, Materials.Gallium, 1L), GT_OreDictUnificator.get(tPrefix, Materials.Phosphor, 1L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.IndiumGalliumPhosphide, 3L * tPrefix.mMaterialAmount), (int) (200L * tPrefix.mMaterialAmount / 3628800L), 8);
            GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(tPrefix, Materials.Brick, 1L), GT_OreDictUnificator.get(tPrefix, Materials.Clay, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.Fireclay, 2L * tPrefix.mMaterialAmount), (int) (200L * tPrefix.mMaterialAmount / 3628800L), 8);

            GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(tPrefix, Materials.Nickel, 4L), GT_OreDictUnificator.get(tPrefix, Materials.Chrome, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.Nichrome, 5L * tPrefix.mMaterialAmount), (int) (500L * tPrefix.mMaterialAmount / 3628800L), 120);
            GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(tPrefix, Materials.Osmium, 1L), GT_OreDictUnificator.get(tPrefix, Materials.Iridium, 3L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1),GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.Osmiridium, 4L * tPrefix.mMaterialAmount), (int) (400L * tPrefix.mMaterialAmount / 3628800L), 2000);
            GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(tPrefix, Materials.Niobium, 1L), GT_OreDictUnificator.get(tPrefix, Materials.Titanium, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1),GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.NiobiumTitanium, 2L * tPrefix.mMaterialAmount), (int) (200L * tPrefix.mMaterialAmount / 3628800L), 2000);
            GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(tPrefix, Materials.Vanadium, 3L), GT_OreDictUnificator.get(tPrefix, Materials.Gallium, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1),GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.VanadiumGallium, 4L * tPrefix.mMaterialAmount), (int) (400L * tPrefix.mMaterialAmount / 3628800L), 2000);
            GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(tPrefix, Materials.Tungsten, 1L), GT_OreDictUnificator.get(tPrefix, Materials.Carbon, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1),GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.TungstenCarbide, 2L * tPrefix.mMaterialAmount), (int) (200L * tPrefix.mMaterialAmount / 3628800L), 500);
            GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(tPrefix, Materials.Tungsten, 1L), GT_OreDictUnificator.get(tPrefix, Materials.Steel, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1),GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.TungstenSteel, 2L * tPrefix.mMaterialAmount), (int) (200L * tPrefix.mMaterialAmount / 3628800L), 500);

            GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(tPrefix, Materials.TungstenSteel, 5L), GT_OreDictUnificator.get(tPrefix, Materials.Chrome, 1L), GT_OreDictUnificator.get(tPrefix, Materials.Molybdenum, 2L), GT_OreDictUnificator.get(tPrefix, Materials.Vanadium, 1L), GT_Values.NI, GT_Utility.getIntegratedCircuit(1),GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.HSSG, 9L * tPrefix.mMaterialAmount), (int) (600L * tPrefix.mMaterialAmount / 3628800L), 3500);
            GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(tPrefix, Materials.HSSG, 6L), GT_OreDictUnificator.get(tPrefix, Materials.Cobalt, 1L), GT_OreDictUnificator.get(tPrefix, Materials.Manganese, 1L), GT_OreDictUnificator.get(tPrefix, Materials.Silicon, 1L), GT_Values.NI, GT_Utility.getIntegratedCircuit(1),GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.HSSE, 9L * tPrefix.mMaterialAmount), (int) (700L * tPrefix.mMaterialAmount / 3628800L), 4000);

            GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(tPrefix, Materials.Nickel, 1L), GT_OreDictUnificator.get(tPrefix, Materials.Zinc, 1L), GT_OreDictUnificator.get(tPrefix, Materials.Iron, 4L), GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.FerriteMixture, 6L * tPrefix.mMaterialAmount), (int) (200L * tPrefix.mMaterialAmount / 3628800L), 8);
            GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(tPrefix, Materials.Boron, 1L), GT_OreDictUnificator.get(tPrefix, Materials.Glass, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.BorosilicateGlass, 8L * tPrefix.mMaterialAmount), (int) (200L * tPrefix.mMaterialAmount / 3628800L), 8);

            //InCore
            //GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(tPrefix, Materials.HSSG,6L),GT_OreDictUnificator.get(tPrefix, Materials.Iridium, 2L), GT_OreDictUnificator.get(tPrefix, Materials.Osmium, 1L), GT_Values.NI, GT_Values.NF, GT_Values.NF,  GT_OreDictUnificator.getDust(Materials.HSSS, 9L * tPrefix.mMaterialAmount), (int) (800L * tPrefix.mMaterialAmount / 3628800L), 4500);

            //GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(tPrefix, Materials.Naquadah, 2L), GT_OreDictUnificator.get(tPrefix, Materials.Tritium, 1L), GT_OreDictUnificator.get(tPrefix, Materials.Carbon, 1L), GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.NaquadahAlloy, 4L * tPrefix.mMaterialAmount), (int) (400L * tPrefix.mMaterialAmount / 3628800L), 8000);

            //GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(tPrefix, Materials.Yttrium, 1L), GT_OreDictUnificator.get(tPrefix, Materials.Barium, 2L), GT_OreDictUnificator.get(tPrefix, Materials.AnyCopper, 3L), GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.YttriumBariumCuprate, 6L * tPrefix.mMaterialAmount), (int) (600L * tPrefix.mMaterialAmount / 3628800L), 2000);
            //GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(tPrefix, Materials.Aluminium, 6L), GT_OreDictUnificator.get(tPrefix, Materials.AnyCopper, 1L), GT_OreDictUnificator.get(tPrefix, Materials.Manganese, 1L), GT_OreDictUnificator.get(tPrefix, Materials.Magnesium, 1L), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.getDust(Materials.Duralumin, 9L * tPrefix.mMaterialAmount), (int) (900L * tPrefix.mMaterialAmount / 3628800L), 2000);
        }
        GT_Values.RA.addMixerRecipe(new ItemStack(Items.rotten_flesh, 1, 0), new ItemStack(Items.fermented_spider_eye, 1, 0), ItemList.IC2_Scrap.get(1L, new Object[0]), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.MeatRaw, 1L), FluidRegistry.getFluidStack("potion.purpledrink", 750), FluidRegistry.getFluidStack("sludge", 1000), ItemList.Food_Chum.get(4L, new Object[0]), 128, 24);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wheat, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.Water.getFluid(1000L), GT_Values.NF, ItemList.Food_Dough.get(2L, new Object[0]), 32, 8);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chili, 1L), ItemList.Food_PotatoChips.get(1L, new Object[0]), GT_Values.NI, GT_Values.NI, GT_Values.NF, GT_Values.NF, ItemList.Food_ChiliChips.get(1L, new Object[0]), 32, 8);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Redstone, 5L), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Ruby, 4L), GT_Values.NI, GT_Values.NI, GT_Values.NF, GT_Values.NF, ItemList.IC2_Energium_Dust.get(1L, new Object[0]), 300, 120);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 5L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ruby, 4L), GT_Values.NI, GT_Values.NI, GT_Values.NF, GT_Values.NF, ItemList.IC2_Energium_Dust.get(9L, new Object[0]), 600, 120);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L), new ItemStack(Blocks.brown_mushroom, 1), new ItemStack(Items.spider_eye, 1), GT_Values.NI, GT_Values.NF, GT_Values.NF, new ItemStack(Items.fermented_spider_eye, 1), 100, 8);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Gold, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.LiveRoot, 1L), GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.IronWood, 2L), 100, 8);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gold, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 9L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.LiveRoot, 9L), GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.IronWood, 18L), 900, 8);
        GT_Values.RA.addMixerRecipe(GT_ModHandler.getModItem(aTextAE, aTextAEMM, 1L, 1), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L), GT_OreDictUnificator.get(OrePrefixes.gem, Materials.NetherQuartz, 1L), GT_Values.NI, Materials.Water.getFluid(500L), GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Fluix, 2L), 20, 16);
        GT_Values.RA.addMixerRecipe(GT_ModHandler.getModItem(aTextAE, aTextAEMM, 1L, 1), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L), GT_OreDictUnificator.get(OrePrefixes.gem, Materials.NetherQuartz, 1L), GT_Values.NI, GT_ModHandler.getDistilledWater(500L), GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Fluix, 2L), 20, 16);
        GT_Values.RA.addMixerRecipe(ItemList.IC2_Fertilizer.get(1L, new Object[0]), new ItemStack(Blocks.dirt, 8, 32767), GT_Utility.getIntegratedCircuit(1), GT_Values.NI, Materials.Water.getFluid(1000L), GT_Values.NF, GT_ModHandler.getModItem(aTextForestry, "soil", 8L, 0), 64, 16);
        GT_Values.RA.addMixerRecipe(ItemList.FR_Fertilizer.get(1L, new Object[0]), new ItemStack(Blocks.dirt, 8, 32767),  GT_Utility.getIntegratedCircuit(1), GT_Values.NI, Materials.Water.getFluid(1000L), GT_Values.NF, GT_ModHandler.getModItem(aTextForestry, "soil", 8L, 0), 64, 16);
        GT_Values.RA.addMixerRecipe(ItemList.FR_Compost.get(1L, new Object[0]), new ItemStack(Blocks.dirt, 8, 32767),  GT_Utility.getIntegratedCircuit(1), GT_Values.NI, Materials.Water.getFluid(1000L), GT_Values.NF, GT_ModHandler.getModItem(aTextForestry, "soil", 8L, 0), 64, 16);
        GT_Values.RA.addMixerRecipe(ItemList.FR_Mulch.get(1L, new Object[0]), new ItemStack(Blocks.dirt, 8, 32767),  GT_Utility.getIntegratedCircuit(1), GT_Values.NI, Materials.Water.getFluid(1000L), GT_Values.NF, GT_ModHandler.getModItem(aTextForestry, "soil", 8L, 0), 64, 16);
        GT_Values.RA.addMixerRecipe(new ItemStack(Blocks.sand, 1, 32767), new ItemStack(Blocks.dirt, 1, 32767),  GT_Utility.getIntegratedCircuit(1), GT_Values.NI, Materials.Water.getFluid(250L), GT_Values.NF, GT_ModHandler.getModItem(aTextForestry, "soil", 2L, 1), 16, 16);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.LightFuel, 5L), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.HeavyFuel, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Fuel, 6L), 16, 120);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.LightFuel, 5L), Materials.Empty.getCells(1), GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.HeavyFuel.getFluid(1000L), GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Fuel, 6L), 16, 120);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.HeavyFuel, 1L), Materials.Empty.getCells(5), GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.LightFuel.getFluid(5000L), GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Fuel, 6L), 16, 120);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.LightFuel, 5L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(2), Materials.HeavyFuel.getFluid(1000L), Materials.Fuel.getFluid(6000L), Materials.Empty.getCells(5), 16, 120);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.HeavyFuel, 1L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(2), Materials.LightFuel.getFluid(5000L), Materials.Fuel.getFluid(6000L), Materials.Empty.getCells(1), 16, 120);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Water, 5L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1L), GT_Values.NI, GT_Values.NI, Materials.Lubricant.getFluid(20), new FluidStack(ItemList.sDrillingFluid, 5000), Materials.Empty.getCells(5), 64, 16);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lapis, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.Water.getFluid(125), FluidRegistry.getFluidStack("ic2coolant", 125), GT_Values.NI, 256, 48);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lapis, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_ModHandler.getDistilledWater(1000), FluidRegistry.getFluidStack("ic2coolant", 1000), GT_Values.NI, 256, 48);

        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 4L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.Creosote.getFluid(1000), null, ItemList.SFMixture.get(8, new Object[]{}), 1600, 16);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lithium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 4L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.Creosote.getFluid(1000), null, ItemList.SFMixture.get(8, new Object[]{}), 1600, 16);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Caesium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 4L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.Creosote.getFluid(1000), null, ItemList.SFMixture.get(12, new Object[]{}), 1600, 16);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 4L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.Lubricant.getFluid(300), null, ItemList.SFMixture.get(8, new Object[]{}), 1200, 16);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lithium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 4L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.Lubricant.getFluid(300), null, ItemList.SFMixture.get(8, new Object[]{}), 1200, 16);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Caesium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 4L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.Lubricant.getFluid(300), null, ItemList.SFMixture.get(12, new Object[]{}), 1200, 16);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 4L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.Glue.getFluid(333), null, ItemList.SFMixture.get(8, new Object[]{}), 800, 16);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lithium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 4L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.Glue.getFluid(333), null, ItemList.SFMixture.get(8, new Object[]{}), 800, 16);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Caesium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 4L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.Glue.getFluid(333), null, ItemList.SFMixture.get(12, new Object[]{}), 800, 16);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 4L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.McGuffium239.getFluid(10), null, ItemList.SFMixture.get(64, new Object[]{}), 400, 16);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lithium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 4L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.McGuffium239.getFluid(10), null, ItemList.SFMixture.get(64, new Object[]{}), 400, 16);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Caesium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 4L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.McGuffium239.getFluid(10), null, ItemList.SFMixture.get(64, new Object[]{}), 400, 16);


        GT_Values.RA.addMixerRecipe(ItemList.SFMixture.get(2, new Object[]{}), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.EnderEye, 1L), null, null, Materials.Mercury.getFluid(50), null, ItemList.MSFMixture.get(2, new Object[]{}), 100, 64);
        GT_Values.RA.addMixerRecipe(ItemList.SFMixture.get(2, new Object[]{}), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Blaze, 1L), null, null, Materials.Mercury.getFluid(50), null, ItemList.MSFMixture.get(2, new Object[]{}), 100, 64);
        
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Lignite,1), ItemList.MSFMixture.get(6, new Object[]{}),  GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Diamond, 1L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.NitroFuel.getFluid(1000), null, ItemList.Block_MSSFUEL.get(1, new Object[]{}), 120, 96);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Charcoal,1), ItemList.MSFMixture.get(4, new Object[]{}), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Diamond, 1L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.NitroFuel.getFluid(800), null, ItemList.Block_MSSFUEL.get(1, new Object[]{}), 120, 96);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Coal,1), ItemList.MSFMixture.get(2, new Object[]{}),     GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Diamond, 1L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.NitroFuel.getFluid(500), null, ItemList.Block_MSSFUEL.get(1, new Object[]{}), 120, 96);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Lignite,1), ItemList.MSFMixture.get(6, new Object[]{}),  GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Emerald, 1L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.NitroFuel.getFluid(1000), null, ItemList.Block_MSSFUEL.get(1, new Object[]{}), 120, 96);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Charcoal,1), ItemList.MSFMixture.get(4, new Object[]{}), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Emerald, 1L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.NitroFuel.getFluid(800), null, ItemList.Block_MSSFUEL.get(1, new Object[]{}), 120, 96);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Coal,1), ItemList.MSFMixture.get(2, new Object[]{}),     GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Emerald, 1L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.NitroFuel.getFluid(500), null, ItemList.Block_MSSFUEL.get(1, new Object[]{}), 120, 96);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Lignite,1), ItemList.MSFMixture.get(6, new Object[]{}),  GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Sapphire, 1L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.NitroFuel.getFluid(1000), null, ItemList.Block_MSSFUEL.get(1, new Object[]{}), 120, 96);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Charcoal,1), ItemList.MSFMixture.get(4, new Object[]{}), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Sapphire, 1L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.NitroFuel.getFluid(800), null, ItemList.Block_MSSFUEL.get(1, new Object[]{}), 120, 96);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Coal,1), ItemList.MSFMixture.get(2, new Object[]{}),     GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Sapphire, 1L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.NitroFuel.getFluid(500), null, ItemList.Block_MSSFUEL.get(1, new Object[]{}), 120, 96);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Lignite,1), ItemList.MSFMixture.get(6, new Object[]{}),  GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.GreenSapphire, 1L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.NitroFuel.getFluid(1000), null, ItemList.Block_MSSFUEL.get(1, new Object[]{}), 120, 96);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Charcoal,1), ItemList.MSFMixture.get(4, new Object[]{}), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.GreenSapphire, 1L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.NitroFuel.getFluid(800), null, ItemList.Block_MSSFUEL.get(1, new Object[]{}), 120, 96);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Coal,1), ItemList.MSFMixture.get(2, new Object[]{}),     GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.GreenSapphire, 1L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.NitroFuel.getFluid(500), null, ItemList.Block_MSSFUEL.get(1, new Object[]{}), 120, 96);

        
        if(Loader.isModLoaded("Thaumcraft")){
        GT_Values.RA.addMixerRecipe(ItemList.SFMixture.get(4, new Object[]{}), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.InfusedAir, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.FierySteel.getFluid(10), null, ItemList.MSFMixture.get(4, new Object[]{}), 100, 64);
        GT_Values.RA.addMixerRecipe(ItemList.SFMixture.get(4, new Object[]{}), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.InfusedEarth, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.FierySteel.getFluid(10), null, ItemList.MSFMixture.get(4, new Object[]{}), 100, 64);
        GT_Values.RA.addMixerRecipe(ItemList.SFMixture.get(4, new Object[]{}), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.InfusedEntropy, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.FierySteel.getFluid(10), null, ItemList.MSFMixture.get(4, new Object[]{}), 100, 64);
        GT_Values.RA.addMixerRecipe(ItemList.SFMixture.get(4, new Object[]{}), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.InfusedFire, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.FierySteel.getFluid(10), null, ItemList.MSFMixture.get(4, new Object[]{}), 100, 64);
        GT_Values.RA.addMixerRecipe(ItemList.SFMixture.get(4, new Object[]{}), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.InfusedOrder, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.FierySteel.getFluid(10), null, ItemList.MSFMixture.get(4, new Object[]{}), 100, 64);
        GT_Values.RA.addMixerRecipe(ItemList.SFMixture.get(4, new Object[]{}), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.InfusedWater, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.FierySteel.getFluid(10), null, ItemList.MSFMixture.get(4, new Object[]{}), 100, 64);

        GT_Values.RA.addMixerRecipe(ItemList.SFMixture.get(2, new Object[]{}), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.InfusedAir, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.Mercury.getFluid(50), null, ItemList.MSFMixture.get(2, new Object[]{}), 100, 64);
        GT_Values.RA.addMixerRecipe(ItemList.SFMixture.get(2, new Object[]{}), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.InfusedEarth, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.Mercury.getFluid(50), null, ItemList.MSFMixture.get(2, new Object[]{}), 100, 64);
        GT_Values.RA.addMixerRecipe(ItemList.SFMixture.get(2, new Object[]{}), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.InfusedEntropy, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.Mercury.getFluid(50), null, ItemList.MSFMixture.get(2, new Object[]{}), 100, 64);
        GT_Values.RA.addMixerRecipe(ItemList.SFMixture.get(2, new Object[]{}), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.InfusedFire, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.Mercury.getFluid(50), null, ItemList.MSFMixture.get(2, new Object[]{}), 100, 64);
        GT_Values.RA.addMixerRecipe(ItemList.SFMixture.get(2, new Object[]{}), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.InfusedOrder, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.Mercury.getFluid(50), null, ItemList.MSFMixture.get(2, new Object[]{}), 100, 64);
        GT_Values.RA.addMixerRecipe(ItemList.SFMixture.get(2, new Object[]{}), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.InfusedWater, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.Mercury.getFluid(50), null, ItemList.MSFMixture.get(2, new Object[]{}), 100, 64);

            FluidStack tFD = FluidRegistry.getFluidStack("fluiddeath", 10);
            if (tFD != null && tFD.getFluid() != null && tFD.amount > 0) {
                GT_Values.RA.addMixerRecipe(ItemList.SFMixture.get(8, new Object[]{}), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.InfusedAir, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), tFD, null, ItemList.MSFMixture.get(8, new Object[]{}), 100, 64);
                GT_Values.RA.addMixerRecipe(ItemList.SFMixture.get(8, new Object[]{}), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.InfusedEarth, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), tFD, null, ItemList.MSFMixture.get(8, new Object[]{}), 100, 64);
                GT_Values.RA.addMixerRecipe(ItemList.SFMixture.get(8, new Object[]{}), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.InfusedEntropy, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), tFD, null, ItemList.MSFMixture.get(8, new Object[]{}), 100, 64);
                GT_Values.RA.addMixerRecipe(ItemList.SFMixture.get(8, new Object[]{}), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.InfusedFire, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), tFD, null, ItemList.MSFMixture.get(8, new Object[]{}), 100, 64);
                GT_Values.RA.addMixerRecipe(ItemList.SFMixture.get(8, new Object[]{}), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.InfusedOrder, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), tFD, null, ItemList.MSFMixture.get(8, new Object[]{}), 100, 64);
                GT_Values.RA.addMixerRecipe(ItemList.SFMixture.get(8, new Object[]{}), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.InfusedWater, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), tFD, null, ItemList.MSFMixture.get(8, new Object[]{}), 100, 64);

        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Lignite,1), ItemList.MSFMixture.get(6, new Object[]{}), GT_ModHandler.getModItem("Thaumcraft", "ItemResource", 4), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.NitroFuel.getFluid(1000), null, ItemList.Block_MSSFUEL.get(1, new Object[]{}), 120, 96);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Charcoal,1), ItemList.MSFMixture.get(4, new Object[]{}), GT_ModHandler.getModItem("Thaumcraft", "ItemResource", 4), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.NitroFuel.getFluid(800), null, ItemList.Block_MSSFUEL.get(1, new Object[]{}), 120, 96);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Coal,1), ItemList.MSFMixture.get(2, new Object[]{}), GT_ModHandler.getModItem("Thaumcraft", "ItemResource", 4), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.NitroFuel.getFluid(500), null, ItemList.Block_MSSFUEL.get(1, new Object[]{}), 120, 96);

                GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block, Materials.Lignite, 1), ItemList.MSFMixture.get(6, new Object[]{}), GT_ModHandler.getModItem("Thaumcraft", "ItemResource", 4), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.HeavyFuel.getFluid(1500), null, ItemList.Block_MSSFUEL.get(1, new Object[]{}), 120, 96);
                GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block, Materials.Charcoal, 1), ItemList.MSFMixture.get(4, new Object[]{}), GT_ModHandler.getModItem("Thaumcraft", "ItemResource", 4), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.HeavyFuel.getFluid(1200), null, ItemList.Block_MSSFUEL.get(1, new Object[]{}), 120, 96);
                GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block, Materials.Coal, 1), ItemList.MSFMixture.get(2, new Object[]{}), GT_ModHandler.getModItem("Thaumcraft", "ItemResource", 4), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.HeavyFuel.getFluid(750), null, ItemList.Block_MSSFUEL.get(1, new Object[]{}), 120, 96);
				//todo CHECK ADDED X3
				GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Lignite,1), ItemList.SFMixture.get(6, new Object[]{}), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.LPG.getFluid(1500), null, ItemList.Block_SSFUEL.get(1, new Object[]{}), 120, 96);
        		GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Charcoal,1), ItemList.SFMixture.get(4, new Object[]{}), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.LPG.getFluid(1200), null, ItemList.Block_SSFUEL.get(1, new Object[]{}), 120, 96);
        		GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Coal,1), ItemList.SFMixture.get(2, new Object[]{}), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.LPG.getFluid(750), null, ItemList.Block_SSFUEL.get(1, new Object[]{}), 120, 96);

        		GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Lignite,1), ItemList.MSFMixture.get(6, new Object[]{}), GT_ModHandler.getModItem("Thaumcraft", "ItemResource", 4), null, Materials.LPG.getFluid(1500), null, ItemList.Block_MSSFUEL.get(1, new Object[]{}), 120, 96);
                GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Charcoal,1), ItemList.MSFMixture.get(4, new Object[]{}), GT_ModHandler.getModItem("Thaumcraft", "ItemResource", 4), null, Materials.LPG.getFluid(1200), null, ItemList.Block_MSSFUEL.get(1, new Object[]{}), 120, 96);
                GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Coal,1), ItemList.MSFMixture.get(2, new Object[]{}), GT_ModHandler.getModItem("Thaumcraft", "ItemResource", 4), null, Materials.LPG.getFluid(750), null, ItemList.Block_MSSFUEL.get(1, new Object[]{}), 120, 96);


            }
        }

        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Lignite,1), ItemList.SFMixture.get(6, new Object[]{}), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.NitroFuel.getFluid(1000), null, ItemList.Block_SSFUEL.get(1, new Object[]{}), 120, 96);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Charcoal,1), ItemList.SFMixture.get(4, new Object[]{}), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.NitroFuel.getFluid(800), null, ItemList.Block_SSFUEL.get(1, new Object[]{}), 120, 96);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Coal,1), ItemList.SFMixture.get(2, new Object[]{}), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.NitroFuel.getFluid(500), null, ItemList.Block_SSFUEL.get(1, new Object[]{}), 120, 96);

        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block, Materials.Lignite, 1), ItemList.SFMixture.get(6, new Object[]{}), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.HeavyFuel.getFluid(1500), null, ItemList.Block_SSFUEL.get(1, new Object[]{}), 120, 96);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block, Materials.Charcoal, 1), ItemList.SFMixture.get(4, new Object[]{}), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.HeavyFuel.getFluid(1200), null, ItemList.Block_SSFUEL.get(1, new Object[]{}), 120, 96);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block, Materials.Coal, 1), ItemList.SFMixture.get(2, new Object[]{}), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.HeavyFuel.getFluid(750), null, ItemList.Block_SSFUEL.get(1, new Object[]{}), 120, 96);
		
		//todo CHECK ADDED X3
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Lignite,1), ItemList.SFMixture.get(6, new Object[]{}), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.LPG.getFluid(1500), null, ItemList.Block_SSFUEL.get(1, new Object[]{}), 120, 96);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Charcoal,1), ItemList.SFMixture.get(4, new Object[]{}), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.LPG.getFluid(1200), null, ItemList.Block_SSFUEL.get(1, new Object[]{}), 120, 96);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Coal,1), ItemList.SFMixture.get(2, new Object[]{}), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.LPG.getFluid(750), null, ItemList.Block_SSFUEL.get(1, new Object[]{}), 120, 96);

        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Lignite,1), ItemList.SFMixture.get(6, new Object[]{}), null, null, Materials.LPG.getFluid(1500), null, ItemList.Block_SSFUEL.get(1, new Object[]{}), 120, 96);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Charcoal,1), ItemList.SFMixture.get(4, new Object[]{}), null, null, Materials.LPG.getFluid(1200), null, ItemList.Block_SSFUEL.get(1, new Object[]{}), 120, 96);
        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.block,Materials.Coal,1), ItemList.SFMixture.get(2, new Object[]{}), null, null, Materials.LPG.getFluid(750), null, ItemList.Block_SSFUEL.get(1, new Object[]{}), 120, 96);

        if (Loader.isModLoaded("Railcraft")) {
            GT_Values.RA.addMixerRecipe(EnumCube.COKE_BLOCK.getItem(), ItemList.SFMixture.get(1, new Object[]{}), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.NitroFuel.getFluid(250), null, ItemList.Block_SSFUEL.get(1, new Object[]{}), 120, 96);
            GT_Values.RA.addMixerRecipe(EnumCube.COKE_BLOCK.getItem(), ItemList.SFMixture.get(1, new Object[]{}), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.HeavyFuel.getFluid(375), null, ItemList.Block_SSFUEL.get(1, new Object[]{}), 120, 96);
            GT_Values.RA.addMixerRecipe(EnumCube.COKE_BLOCK.getItem(), ItemList.SFMixture.get(1, new Object[]{}), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.LPG.getFluid(375), null, ItemList.Block_SSFUEL.get(1, new Object[]{}), 120, 96);
            GT_Values.RA.addMixerRecipe(EnumCube.COKE_BLOCK.getItem(), ItemList.SFMixture.get(1, new Object[]{}), null, null, Materials.LPG.getFluid(375), null, ItemList.Block_SSFUEL.get(1, new Object[]{}), 120, 96);

            if (Loader.isModLoaded("Thaumcraft")) {
                GT_Values.RA.addMixerRecipe(EnumCube.COKE_BLOCK.getItem(), ItemList.MSFMixture.get(1, new Object[]{}), GT_ModHandler.getModItem("Thaumcraft", "ItemResource", 4), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.NitroFuel.getFluid(250), null, ItemList.Block_MSSFUEL.get(1, new Object[]{}), 120, 96);
                GT_Values.RA.addMixerRecipe(EnumCube.COKE_BLOCK.getItem(), ItemList.MSFMixture.get(1, new Object[]{}), GT_ModHandler.getModItem("Thaumcraft", "ItemResource", 4), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.HeavyFuel.getFluid(375), null, ItemList.Block_MSSFUEL.get(1, new Object[]{}), 120, 96);
                GT_Values.RA.addMixerRecipe(EnumCube.COKE_BLOCK.getItem(), ItemList.MSFMixture.get(1, new Object[]{}), GT_ModHandler.getModItem("Thaumcraft", "ItemResource", 4), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), Materials.LPG.getFluid(375), null, ItemList.Block_MSSFUEL.get(1, new Object[]{}), 120, 96);
                GT_Values.RA.addMixerRecipe(EnumCube.COKE_BLOCK.getItem(), ItemList.MSFMixture.get(1, new Object[]{}), GT_ModHandler.getModItem("Thaumcraft", "ItemResource", 4), null, Materials.LPG.getFluid(375), null, ItemList.Block_MSSFUEL.get(1, new Object[]{}), 120, 96);
            }
        }

        GT_Values.RA.addExtruderRecipe(ItemList.FR_Wax.get(1L, new Object[0]), ItemList.Shape_Extruder_Cell.get(0L, new Object[0]), ItemList.FR_WaxCapsule.get(1L, new Object[0]), 64, 16);
        GT_Values.RA.addExtruderRecipe(ItemList.FR_RefractoryWax.get(1L, new Object[0]), ItemList.Shape_Extruder_Cell.get(0L, new Object[0]), ItemList.FR_RefractoryCapsule.get(1L, new Object[0]), 128, 16);

        GT_Values.RA.addFluidCannerRecipe(ItemList.Battery_Hull_LV.get(1L, new Object[0]), ItemList.IC2_ReBattery.get(1L, new Object[0]), Materials.Redstone.getMolten(288L), GT_Values.NF);
        GT_Values.RA.addFluidCannerRecipe(ItemList.Battery_Hull_LV.get(1L, new Object[0]), ItemList.Battery_SU_LV_Mercury.getWithCharge(1L, Integer.MAX_VALUE, new Object[0]), Materials.Mercury.getFluid(1000L), GT_Values.NF);
        GT_Values.RA.addFluidCannerRecipe(ItemList.Battery_Hull_MV.get(1L, new Object[0]), ItemList.Battery_SU_MV_Mercury.getWithCharge(1L, Integer.MAX_VALUE, new Object[0]), Materials.Mercury.getFluid(4000L), GT_Values.NF);
        GT_Values.RA.addFluidCannerRecipe(ItemList.Battery_Hull_HV.get(1L, new Object[0]), ItemList.Battery_SU_HV_Mercury.getWithCharge(1L, Integer.MAX_VALUE, new Object[0]), Materials.Mercury.getFluid(16000L), GT_Values.NF);
        GT_Values.RA.addFluidCannerRecipe(ItemList.Battery_Hull_LV.get(1L, new Object[0]), ItemList.Battery_SU_LV_SulfuricAcid.getWithCharge(1L, Integer.MAX_VALUE, new Object[0]), Materials.SulfuricAcid.getFluid(1000L), GT_Values.NF);
        GT_Values.RA.addFluidCannerRecipe(ItemList.Battery_Hull_MV.get(1L, new Object[0]), ItemList.Battery_SU_MV_SulfuricAcid.getWithCharge(1L, Integer.MAX_VALUE, new Object[0]), Materials.SulfuricAcid.getFluid(4000L), GT_Values.NF);
        GT_Values.RA.addFluidCannerRecipe(ItemList.Battery_Hull_HV.get(1L, new Object[0]), ItemList.Battery_SU_HV_SulfuricAcid.getWithCharge(1L, Integer.MAX_VALUE, new Object[0]), Materials.SulfuricAcid.getFluid(16000L), GT_Values.NF);
        GT_Values.RA.addFluidCannerRecipe(ItemList.TF_Vial_FieryTears.get(1L, new Object[0]), ItemList.Bottle_Empty.get(1L, new Object[0]), GT_Values.NF, Materials.FierySteel.getFluid(250L));

        GT_Values.RA.addFluidCannerRecipe(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Concrete, 1L), ItemList.Cell_Empty.get(1L), GT_Values.NF, Materials.Concrete.getMolten(144L));
        GT_Values.RA.addFluidCannerRecipe(ItemList.Cell_Empty.get(1L), (GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Concrete, 1L)), Materials.Concrete.getMolten(144L), GT_Values.NF);

        GT_Values.RA.addFluidCannerRecipe(ItemList.Cell_Empty.get(1L), (GT_OreDictUnificator.get(OrePrefixes.cellMolten, Materials.Redstone, 1L)), Materials.Redstone.getMolten(144L), GT_Values.NF);
        GT_Values.RA.addFluidCannerRecipe(ItemList.Cell_Empty.get(1L), (GT_OreDictUnificator.get(OrePrefixes.cellMolten, Materials.Glass, 1L)), Materials.Glass.getMolten(144L), GT_Values.NF);
        GT_Values.RA.addFluidCannerRecipe(ItemList.Cell_Empty.get(1L), (GT_OreDictUnificator.get(OrePrefixes.cellMolten, Materials.Blaze, 1L)), Materials.Blaze.getMolten(144L), GT_Values.NF);
        GT_Values.RA.addFluidCannerRecipe(GT_OreDictUnificator.get(OrePrefixes.cellMolten, Materials.Redstone, 1L), ItemList.Cell_Empty.get(1L), GT_Values.NF, Materials.Redstone.getMolten(144L));
        GT_Values.RA.addFluidCannerRecipe(GT_OreDictUnificator.get(OrePrefixes.cellMolten, Materials.Glass, 1L), ItemList.Cell_Empty.get(1L), GT_Values.NF, Materials.Glass.getMolten(144L));
        GT_Values.RA.addFluidCannerRecipe(GT_OreDictUnificator.get(OrePrefixes.cellMolten, Materials.Blaze, 1L), ItemList.Cell_Empty.get(1L), GT_Values.NF, Materials.Blaze.getMolten(144L));

        Materials tMaterial = Materials.Iron;
        if (tMaterial.mStandardMoltenFluid != null) {
            GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Casing.get(0L, new Object[0]), tMaterial.getMolten(72L), ItemList.IC2_Item_Casing_Iron.get(1L, new Object[0]), 16, 8);
        }
        tMaterial = Materials.WroughtIron;
        if (tMaterial.mStandardMoltenFluid != null) {
            GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Casing.get(0L, new Object[0]), tMaterial.getMolten(72L), ItemList.IC2_Item_Casing_Iron.get(1L, new Object[0]), 16, 8);
        }
        tMaterial = Materials.Gold;
        if (tMaterial.mStandardMoltenFluid != null) {
            GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Casing.get(0L, new Object[0]), tMaterial.getMolten(72L), ItemList.IC2_Item_Casing_Gold.get(1L, new Object[0]), 16, 8);
        }
        tMaterial = Materials.Bronze;
        if (tMaterial.mStandardMoltenFluid != null) {
            GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Casing.get(0L, new Object[0]), tMaterial.getMolten(72L), ItemList.IC2_Item_Casing_Bronze.get(1L, new Object[0]), 16, 8);
        }
        tMaterial = Materials.Copper;
        if (tMaterial.mStandardMoltenFluid != null) {
            GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Casing.get(0L, new Object[0]), tMaterial.getMolten(72L), ItemList.IC2_Item_Casing_Copper.get(1L, new Object[0]), 16, 8);
        }
        tMaterial = Materials.AnnealedCopper;
        if (tMaterial.mStandardMoltenFluid != null) {
            GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Casing.get(0L, new Object[0]), tMaterial.getMolten(72L), ItemList.IC2_Item_Casing_Copper.get(1L, new Object[0]), 16, 8);
        }
        tMaterial = Materials.Tin;
        if (tMaterial.mStandardMoltenFluid != null) {
            GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Casing.get(0L, new Object[0]), tMaterial.getMolten(72L), ItemList.IC2_Item_Casing_Tin.get(1L, new Object[0]), 16, 8);
        }
        tMaterial = Materials.Lead;
        if (tMaterial.mStandardMoltenFluid != null) {
            GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Casing.get(0L, new Object[0]), tMaterial.getMolten(72L), ItemList.IC2_Item_Casing_Lead.get(1L, new Object[0]), 16, 8);
        }
        tMaterial = Materials.Steel;
        if (tMaterial.mStandardMoltenFluid != null) {
            GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Casing.get(0L, new Object[0]), tMaterial.getMolten(72L), ItemList.IC2_Item_Casing_Steel.get(1L, new Object[0]), 16, 8);
        }
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Ball.get(0L, new Object[0]), Materials.Mercury.getFluid(1000L), GT_ModHandler.getModItem("Thaumcraft", "ItemResource", 1, 3), 128, 4);
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Ball.get(0L, new Object[0]), Materials.Mercury.getFluid(1000L), GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Mercury, 1L), 128, 4);
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Ball.get(0L, new Object[0]), Materials.Water.getFluid(250L), new ItemStack(Items.snowball, 1, 0), 128, 4);
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Ball.get(0L, new Object[0]), GT_ModHandler.getDistilledWater(250L), new ItemStack(Items.snowball, 1, 0), 128, 4);
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Block.get(0L, new Object[0]), Materials.Water.getFluid(1000L), new ItemStack(Blocks.snow, 1, 0), 512, 4);
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Block.get(0L, new Object[0]), GT_ModHandler.getDistilledWater(1000L), new ItemStack(Blocks.snow, 1, 0), 512, 4);
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Block.get(0L, new Object[0]), Materials.Lava.getFluid(1000L), new ItemStack(Blocks.obsidian, 1, 0), 1024, 16);
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Block.get(0L, new Object[0]), Materials.Concrete.getMolten(144L), new ItemStack(GregTech_API.sBlockConcretes, 1, 8), 12, 4);
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Block.get(0L, new Object[0]), Materials.Glowstone.getMolten(576L), new ItemStack(Blocks.glowstone, 1, 0), 12, 4);
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Block.get(0L, new Object[0]), Materials.Glass.getMolten(144L), new ItemStack(Blocks.glass, 1, 0), 12, 4);
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Plate.get(0L, new Object[0]), Materials.Glass.getMolten(144L), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Glass, 1L), 12, 4);
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Bottle.get(0L, new Object[0]), Materials.Glass.getMolten(144L), ItemList.Bottle_Empty.get(1L, new Object[0]), 12, 4);
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Cylinder.get(0L, new Object[0]), Materials.Milk.getFluid(250L), ItemList.Food_Cheese.get(1L, new Object[0]), 1024, 4);
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Cylinder.get(0L, new Object[0]), Materials.Cheese.getMolten(144L), ItemList.Food_Cheese.get(1L, new Object[0]), 64, 8);
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Anvil.get(0L, new Object[0]), Materials.Iron.getMolten(4464L), new ItemStack(Blocks.anvil, 1, 0), 128, 16);
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Anvil.get(0L, new Object[0]), Materials.WroughtIron.getMolten(4464L), new ItemStack(Blocks.anvil, 1, 0), 128, 16);
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Anvil.get(0L, new Object[0]), Materials.Steel.getMolten(4464L), GT_ModHandler.getModItem("Railcraft", "tile.railcraft.anvil", 1L, 0), 128, 16);

        GT_Values.RA.addChemicalBathRecipe(ItemList.Food_Raw_Fries.get(1L, new Object[0]), Materials.FryingOilHot.getFluid(10L), ItemList.Food_Fries.get(1L, new Object[0]), GT_Values.NI, GT_Values.NI, null, 16, 4);
        GT_Values.RA.addChemicalBathRecipe(GT_ModHandler.getIC2Item("dynamite", 1L), Materials.Glue.getFluid(10L), GT_ModHandler.getIC2Item("stickyDynamite", 1L), GT_Values.NI, GT_Values.NI, null, 16, 4);
        GT_Values.RA.addChemicalRecipe(new ItemStack(Items.paper, 1), new ItemStack(Items.string, 1), Materials.Glyceryl.getFluid(500), GT_Values.NF, GT_ModHandler.getIC2Item("dynamite", 1L), 160, 4);
        GT_Values.RA.addChemicalBathRecipe(GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Steel, 1L), Materials.Concrete.getMolten(144L), GT_ModHandler.getIC2Item("reinforcedStone", 1L), GT_Values.NI, GT_Values.NI, null, 200, 4);
        GT_Values.RA.addChemicalBathRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 1L), Materials.Water.getFluid(125L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.HydratedCoal, 1L), GT_Values.NI, GT_Values.NI, null, 12, 4);
        GT_Values.RA.addChemicalBathRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 1L), Materials.Water.getFluid(100L), new ItemStack(Items.paper, 1, 0), GT_Values.NI, GT_Values.NI, null, 200, 4);
        GT_Values.RA.addChemicalBathRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Paper, 1L), Materials.Water.getFluid(100L), new ItemStack(Items.paper, 1, 0), GT_Values.NI, GT_Values.NI, null, 100, 4);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Items.reeds, 1, 32767), Materials.Water.getFluid(100L), new ItemStack(Items.paper, 1, 0), GT_Values.NI, GT_Values.NI, null, 100, 8);
        GT_Values.RA.addChemicalBathRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 1L), GT_ModHandler.getDistilledWater(125L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.HydratedCoal, 1L), GT_Values.NI, GT_Values.NI, null, 12, 4);
        GT_Values.RA.addChemicalBathRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 1L), GT_ModHandler.getDistilledWater(100L), new ItemStack(Items.paper, 1, 0), GT_Values.NI, GT_Values.NI, null, 200, 4);
        GT_Values.RA.addChemicalBathRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Paper, 1L), GT_ModHandler.getDistilledWater(100L), new ItemStack(Items.paper, 1, 0), GT_Values.NI, GT_Values.NI, null, 100, 4);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Items.reeds, 1, 32767), GT_ModHandler.getDistilledWater(100L), new ItemStack(Items.paper, 1, 0), GT_Values.NI, GT_Values.NI, null, 100, 8);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.wool, 1, 1), Materials.Chlorine.getGas(50L), new ItemStack(Blocks.wool, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.wool, 1, 2), Materials.Chlorine.getGas(50L), new ItemStack(Blocks.wool, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.wool, 1, 3), Materials.Chlorine.getGas(50L), new ItemStack(Blocks.wool, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.wool, 1, 4), Materials.Chlorine.getGas(50L), new ItemStack(Blocks.wool, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.wool, 1, 5), Materials.Chlorine.getGas(50L), new ItemStack(Blocks.wool, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.wool, 1, 6), Materials.Chlorine.getGas(50L), new ItemStack(Blocks.wool, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.wool, 1, 7), Materials.Chlorine.getGas(50L), new ItemStack(Blocks.wool, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.wool, 1, 8), Materials.Chlorine.getGas(50L), new ItemStack(Blocks.wool, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.wool, 1, 9), Materials.Chlorine.getGas(50L), new ItemStack(Blocks.wool, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.wool, 1, 10), Materials.Chlorine.getGas(50L), new ItemStack(Blocks.wool, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.wool, 1, 11), Materials.Chlorine.getGas(50L), new ItemStack(Blocks.wool, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.wool, 1, 12), Materials.Chlorine.getGas(50L), new ItemStack(Blocks.wool, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.wool, 1, 13), Materials.Chlorine.getGas(50L), new ItemStack(Blocks.wool, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.wool, 1, 14), Materials.Chlorine.getGas(50L), new ItemStack(Blocks.wool, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.wool, 1, 15), Materials.Chlorine.getGas(50L), new ItemStack(Blocks.wool, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.carpet, 1, 1), Materials.Chlorine.getGas(25L), new ItemStack(Blocks.carpet, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.carpet, 1, 2), Materials.Chlorine.getGas(25L), new ItemStack(Blocks.carpet, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.carpet, 1, 3), Materials.Chlorine.getGas(25L), new ItemStack(Blocks.carpet, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.carpet, 1, 4), Materials.Chlorine.getGas(25L), new ItemStack(Blocks.carpet, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.carpet, 1, 5), Materials.Chlorine.getGas(25L), new ItemStack(Blocks.carpet, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.carpet, 1, 6), Materials.Chlorine.getGas(25L), new ItemStack(Blocks.carpet, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.carpet, 1, 7), Materials.Chlorine.getGas(25L), new ItemStack(Blocks.carpet, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.carpet, 1, 8), Materials.Chlorine.getGas(25L), new ItemStack(Blocks.carpet, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.carpet, 1, 9), Materials.Chlorine.getGas(25L), new ItemStack(Blocks.carpet, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.carpet, 1, 10), Materials.Chlorine.getGas(25L), new ItemStack(Blocks.carpet, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.carpet, 1, 11), Materials.Chlorine.getGas(25L), new ItemStack(Blocks.carpet, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.carpet, 1, 12), Materials.Chlorine.getGas(25L), new ItemStack(Blocks.carpet, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.carpet, 1, 13), Materials.Chlorine.getGas(25L), new ItemStack(Blocks.carpet, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.carpet, 1, 14), Materials.Chlorine.getGas(25L), new ItemStack(Blocks.carpet, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.carpet, 1, 15), Materials.Chlorine.getGas(25L), new ItemStack(Blocks.carpet, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.stained_hardened_clay, 1, 32767), Materials.Chlorine.getGas(50L), new ItemStack(Blocks.hardened_clay, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.stained_glass, 1, 32767), Materials.Chlorine.getGas(50L), new ItemStack(Blocks.glass, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.stained_glass_pane, 1, 32767), Materials.Chlorine.getGas(20L), new ItemStack(Blocks.glass_pane, 1, 0), GT_Values.NI, GT_Values.NI, null, 400, 2);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(GregTech_API.sBlockConcretes, 1, 8), Materials.Water.getFluid(250L), new ItemStack(GregTech_API.sBlockConcretes, 1, 0), GT_Values.NI, GT_Values.NI, null, 200, 4);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(GregTech_API.sBlockConcretes, 1, 9), Materials.Water.getFluid(250L), new ItemStack(GregTech_API.sBlockConcretes, 1, 1), GT_Values.NI, GT_Values.NI, null, 200, 4);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(GregTech_API.sBlockConcretes, 1, 10), Materials.Water.getFluid(250L), new ItemStack(GregTech_API.sBlockConcretes, 1, 2), GT_Values.NI, GT_Values.NI, null, 200, 4);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(GregTech_API.sBlockConcretes, 1, 11), Materials.Water.getFluid(250L), new ItemStack(GregTech_API.sBlockConcretes, 1, 3), GT_Values.NI, GT_Values.NI, null, 200, 4);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(GregTech_API.sBlockConcretes, 1, 12), Materials.Water.getFluid(250L), new ItemStack(GregTech_API.sBlockConcretes, 1, 4), GT_Values.NI, GT_Values.NI, null, 200, 4);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(GregTech_API.sBlockConcretes, 1, 13), Materials.Water.getFluid(250L), new ItemStack(GregTech_API.sBlockConcretes, 1, 5), GT_Values.NI, GT_Values.NI, null, 200, 4);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(GregTech_API.sBlockConcretes, 1, 14), Materials.Water.getFluid(250L), new ItemStack(GregTech_API.sBlockConcretes, 1, 6), GT_Values.NI, GT_Values.NI, null, 200, 4);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(GregTech_API.sBlockConcretes, 1, 15), Materials.Water.getFluid(250L), new ItemStack(GregTech_API.sBlockConcretes, 1, 7), GT_Values.NI, GT_Values.NI, null, 200, 4);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(GregTech_API.sBlockConcretes, 1, 8), GT_ModHandler.getDistilledWater(250L), new ItemStack(GregTech_API.sBlockConcretes, 1, 0), GT_Values.NI, GT_Values.NI, null, 200, 4);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(GregTech_API.sBlockConcretes, 1, 9), GT_ModHandler.getDistilledWater(250L), new ItemStack(GregTech_API.sBlockConcretes, 1, 1), GT_Values.NI, GT_Values.NI, null, 200, 4);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(GregTech_API.sBlockConcretes, 1, 10), GT_ModHandler.getDistilledWater(250L), new ItemStack(GregTech_API.sBlockConcretes, 1, 2), GT_Values.NI, GT_Values.NI, null, 200, 4);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(GregTech_API.sBlockConcretes, 1, 11), GT_ModHandler.getDistilledWater(250L), new ItemStack(GregTech_API.sBlockConcretes, 1, 3), GT_Values.NI, GT_Values.NI, null, 200, 4);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(GregTech_API.sBlockConcretes, 1, 12), GT_ModHandler.getDistilledWater(250L), new ItemStack(GregTech_API.sBlockConcretes, 1, 4), GT_Values.NI, GT_Values.NI, null, 200, 4);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(GregTech_API.sBlockConcretes, 1, 13), GT_ModHandler.getDistilledWater(250L), new ItemStack(GregTech_API.sBlockConcretes, 1, 5), GT_Values.NI, GT_Values.NI, null, 200, 4);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(GregTech_API.sBlockConcretes, 1, 14), GT_ModHandler.getDistilledWater(250L), new ItemStack(GregTech_API.sBlockConcretes, 1, 6), GT_Values.NI, GT_Values.NI, null, 200, 4);
        GT_Values.RA.addChemicalBathRecipe(new ItemStack(GregTech_API.sBlockConcretes, 1, 15), GT_ModHandler.getDistilledWater(250L), new ItemStack(GregTech_API.sBlockConcretes, 1, 7), GT_Values.NI, GT_Values.NI, null, 200, 4);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.BlackSteel, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Plastic, 1L), Materials.Concrete.getMolten(144L), ItemList.Block_Plascrete.get(1L, new Object[0]), 200, 48);
        GT_Values.RA.addChemicalBathRecipe(GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.TungstenSteel, 1L), Materials.Concrete.getMolten(144L), ItemList.Block_TungstenSteelReinforced.get(1L, new Object[0]), GT_Values.NI, GT_Values.NI, null, 200, 4);
        //GT_Values.RA.addAssemblerRecipe(ItemList.Casing_Coil_Superconductor.get(1L), ItemList.Circuit_Integrated.getWithDamage(0L, 8L), GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Superconductor, 1L), 200, 7000);

        GT_Values.RA.addAssemblerRecipe(ItemList.Casing_Coil_Cupronickel.get(1L), ItemList.Circuit_Integrated.getWithDamage(0L, 8L), GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Cupronickel, 1L), 200, 30);
        GT_Values.RA.addAssemblerRecipe(ItemList.Casing_Coil_Kanthal.get(1L), ItemList.Circuit_Integrated.getWithDamage(0L, 8L), GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Kanthal, 1L), 200, 120);
        GT_Values.RA.addAssemblerRecipe(ItemList.Casing_Coil_Nichrome.get(1L), ItemList.Circuit_Integrated.getWithDamage(0L, 8L), GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Nichrome, 1L), 200, 120);
        GT_Values.RA.addAssemblerRecipe(ItemList.Casing_Coil_TungstenSteel.get(1L), ItemList.Circuit_Integrated.getWithDamage(0L, 8L), GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.TungstenSteel, 1L), 200, 500);
        GT_Values.RA.addAssemblerRecipe(ItemList.Casing_Coil_HSSG.get(1L), ItemList.Circuit_Integrated.getWithDamage(0L, 8L), GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.HSSG, 1L), 200, 500);
        GT_Values.RA.addAssemblerRecipe(ItemList.Casing_Coil_Naquadah.get(1L), ItemList.Circuit_Integrated.getWithDamage(0L, 8L), GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Naquadah, 1L), 200, 2000);
        GT_Values.RA.addAssemblerRecipe(ItemList.Casing_Coil_NaquadahAlloy.get(1L), ItemList.Circuit_Integrated.getWithDamage(0L, 8L), GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.NaquadahAlloy, 1L), 200, 2000);

        GT_Values.RA.addChemicalBathRecipe(GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.TungstenSteel, 1L), Materials.Concrete.getMolten(144L), ItemList.Block_TungstenSteelReinforced.get(1L), GT_Values.NI, GT_Values.NI, null, 200, 4);
        GT_Values.RA.addCentrifugeRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfusedGold, 1L), GT_Values.NI, Materials.Mercury.getFluid(200L), GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gold, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gold, 1L), GT_ModHandler.getModItem("Thaumcraft", "ItemResource", 2L, 14), GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{10000, 10000, 9000}, 400, 120);

        //GT_Values.RA.addChemicalBathRecipe(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Galena, 1L), Materials.Mercury.getFluid(1000L), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Galena, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silver, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1L), new int[]{10000, 10000, 7500}, 800, 8);

        for (int j = 0; j < Dyes.dyeRed.getSizeOfFluidList(); j++) {
            GT_Values.RA.addChemicalBathRecipe(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.RedAlloy, 1L), Dyes.dyeRed.getFluidDye(j, 72L), GT_ModHandler.getModItem("BuildCraft|Transport", "pipeWire", 4L, 0), GT_Values.NI, GT_Values.NI, null, 32, 16);
        }
        for (int j = 0; j < Dyes.dyeBlue.getSizeOfFluidList(); j++) {
            GT_Values.RA.addChemicalBathRecipe(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.RedAlloy, 1L), Dyes.dyeBlue.getFluidDye(j, 72L), GT_ModHandler.getModItem("BuildCraft|Transport", "pipeWire", 4L, 1), GT_Values.NI, GT_Values.NI, null, 32, 16);
        }
        for (int j = 0; j < Dyes.dyeGreen.getSizeOfFluidList(); j++) {
            GT_Values.RA.addChemicalBathRecipe(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.RedAlloy, 1L), Dyes.dyeGreen.getFluidDye(j, 72L), GT_ModHandler.getModItem("BuildCraft|Transport", "pipeWire", 4L, 2), GT_Values.NI, GT_Values.NI, null, 32, 16);
        }
        for (int j = 0; j < Dyes.dyeYellow.getSizeOfFluidList(); j++) {
            GT_Values.RA.addChemicalBathRecipe(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.RedAlloy, 1L), Dyes.dyeYellow.getFluidDye(j, 72L), GT_ModHandler.getModItem("BuildCraft|Transport", "pipeWire", 4L, 3), GT_Values.NI, GT_Values.NI, null, 32, 16);
        }
        for (byte i = 0; i < 16; i = (byte) (i + 1)) {
            for (int j = 0; j < Dyes.VALUES[i].getSizeOfFluidList(); j++) {
                if (i != 15) {
                    GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.wool, 1, 0), Dyes.VALUES[i].getFluidDye(j, 72L), new ItemStack(Blocks.wool, 1, 15 - i), GT_Values.NI, GT_Values.NI, null, 64, 2);
                }
                GT_Values.RA.addAssemblerRecipe(new ItemStack(Items.string, 3), ItemList.Circuit_Integrated.getWithDamage(0L, 3L, new Object[0]), Dyes.VALUES[i].getFluidDye(j, 24L), new ItemStack(Blocks.carpet, 2, 15 - i), 128, 5);
                GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.glass, 1, 0), Dyes.VALUES[i].getFluidDye(j, 18L), new ItemStack(Blocks.stained_glass, 1, 15 - i), GT_Values.NI, GT_Values.NI, null, 64, 2);
                GT_Values.RA.addChemicalBathRecipe(new ItemStack(Blocks.hardened_clay, 1, 0), Dyes.VALUES[i].getFluidDye(j, 18L), new ItemStack(Blocks.stained_hardened_clay, 1, 15 - i), GT_Values.NI, GT_Values.NI, null, 64, 2);
            }
        }
        GT_Values.RA.addFluidExtractionRecipe(ItemList.Dye_SquidInk.get(1L, new Object[0]), GT_Values.NI, FluidRegistry.getFluidStack("squidink", 144), 10000, 128, 4);
        GT_Values.RA.addFluidExtractionRecipe(ItemList.Dye_Indigo.get(1L, new Object[0]), GT_Values.NI, FluidRegistry.getFluidStack("indigo", 144), 10000, 128, 4);
        GT_Values.RA.addFluidExtractionRecipe(ItemList.Crop_Drop_Indigo.get(1L, new Object[0]), GT_Values.NI, FluidRegistry.getFluidStack("indigo", 144), 10000, 128, 4);
        GT_Values.RA.addFluidExtractionRecipe(ItemList.Crop_Drop_MilkWart.get(1L, new Object[0]), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Milk, 1L), GT_ModHandler.getMilk(150L), 1000, 128, 4);
        GT_Values.RA.addFluidExtractionRecipe(ItemList.Crop_Drop_OilBerry.get(1L, new Object[0]), GT_Values.NI, Materials.Oil.getFluid(100L), 10000, 128, 4);
        GT_Values.RA.addFluidExtractionRecipe(ItemList.Crop_Drop_UUMBerry.get(1L, new Object[0]), GT_Values.NI, Materials.UUMatter.getFluid(4L), 10000, 128, 4);
        GT_Values.RA.addFluidExtractionRecipe(ItemList.Crop_Drop_UUABerry.get(1L, new Object[0]), GT_Values.NI, Materials.UUAmplifier.getFluid(4L), 10000, 128, 4);
        GT_Values.RA.addFluidExtractionRecipe(new ItemStack(Items.fish, 1, 0), GT_Values.NI, Materials.FishOil.getFluid(40L), 10000, 16, 4);
        GT_Values.RA.addFluidExtractionRecipe(new ItemStack(Items.fish, 1, 1), GT_Values.NI, Materials.FishOil.getFluid(60L), 10000, 16, 4);
        GT_Values.RA.addFluidExtractionRecipe(new ItemStack(Items.fish, 1, 2), GT_Values.NI, Materials.FishOil.getFluid(70L), 10000, 16, 4);
        GT_Values.RA.addFluidExtractionRecipe(new ItemStack(Items.fish, 1, 3), GT_Values.NI, Materials.FishOil.getFluid(30L), 10000, 16, 4);

        if (Loader.isModLoaded("harvestcraft")) {
            GT_Values.RA.addFluidExtractionRecipe(GT_ModHandler.getModItem("harvestcraft", "catfishrawItem", 1, 0), GT_Values.NI, Materials.FishOil.getFluid(50L), 10000, 16, 4);
            GT_Values.RA.addFluidExtractionRecipe(GT_ModHandler.getModItem("harvestcraft", "crayfishrawItem", 1, 0), GT_Values.NI, Materials.FishOil.getFluid(50L), 10000, 16, 4);
            GT_Values.RA.addFluidExtractionRecipe(GT_ModHandler.getModItem("harvestcraft", "jellyfishrawItem", 1, 0), GT_Values.NI, Materials.FishOil.getFluid(50L), 10000, 16, 4);
            GT_Values.RA.addFluidExtractionRecipe(GT_ModHandler.getModItem("harvestcraft", "mudfishrawItem", 1, 0), GT_Values.NI, Materials.FishOil.getFluid(50L), 10000, 16, 4);
            GT_Values.RA.addFluidExtractionRecipe(GT_ModHandler.getModItem("harvestcraft", "greenheartfishItem", 1, 0), GT_Values.NI, Materials.FishOil.getFluid(50L), 10000, 16, 4);
            GT_Values.RA.addFluidExtractionRecipe(GT_ModHandler.getModItem("harvestcraft", "catfishrawItem", 1, 0), GT_Values.NI, Materials.FishOil.getFluid(50L), 10000, 16, 4);
            GT_Values.RA.addFluidExtractionRecipe(GT_ModHandler.getModItem("harvestcraft", "anchovyrawItem", 1, 0), GT_Values.NI, Materials.FishOil.getFluid(50L), 10000, 16, 4);
            GT_Values.RA.addFluidExtractionRecipe(GT_ModHandler.getModItem("harvestcraft", "bassrawItem", 1, 0), GT_Values.NI, Materials.FishOil.getFluid(50L), 10000, 16, 4);
            GT_Values.RA.addFluidExtractionRecipe(GT_ModHandler.getModItem("harvestcraft", "carprawItem", 1, 0), GT_Values.NI, Materials.FishOil.getFluid(50L), 10000, 16, 4);
            GT_Values.RA.addFluidExtractionRecipe(GT_ModHandler.getModItem("harvestcraft", "octopusrawItem", 1, 0), GT_Values.NI, Materials.FishOil.getFluid(50L), 10000, 16, 4);
            GT_Values.RA.addFluidExtractionRecipe(GT_ModHandler.getModItem("harvestcraft", "charrrawItem", 1, 0), GT_Values.NI, Materials.FishOil.getFluid(50L), 10000, 16, 4);
            GT_Values.RA.addFluidExtractionRecipe(GT_ModHandler.getModItem("harvestcraft", "clamrawItem", 1, 0), GT_Values.NI, Materials.FishOil.getFluid(50L), 10000, 16, 4);
            GT_Values.RA.addFluidExtractionRecipe(GT_ModHandler.getModItem("harvestcraft", "crabrawItem", 1, 0), GT_Values.NI, Materials.FishOil.getFluid(50L), 10000, 16, 4);
            GT_Values.RA.addFluidExtractionRecipe(GT_ModHandler.getModItem("harvestcraft", "crayfishrawItem", 1, 0), GT_Values.NI, Materials.FishOil.getFluid(50L), 10000, 16, 4);
            GT_Values.RA.addFluidExtractionRecipe(GT_ModHandler.getModItem("harvestcraft", "eelrawItem", 1, 0), GT_Values.NI, Materials.FishOil.getFluid(50L), 10000, 16, 4);
            GT_Values.RA.addFluidExtractionRecipe(GT_ModHandler.getModItem("harvestcraft", "frograwItem", 1, 0), GT_Values.NI, Materials.FishOil.getFluid(50L), 10000, 16, 4);
            GT_Values.RA.addFluidExtractionRecipe(GT_ModHandler.getModItem("harvestcraft", "grouperrawItem", 1, 0), GT_Values.NI, Materials.FishOil.getFluid(50L), 10000, 16, 4);
            GT_Values.RA.addFluidExtractionRecipe(GT_ModHandler.getModItem("harvestcraft", "herringrawItem", 1, 0), GT_Values.NI, Materials.FishOil.getFluid(50L), 10000, 16, 4);
            GT_Values.RA.addFluidExtractionRecipe(GT_ModHandler.getModItem("harvestcraft", "perchrawItem", 1, 0), GT_Values.NI, Materials.FishOil.getFluid(50L), 10000, 16, 4);
            GT_Values.RA.addFluidExtractionRecipe(GT_ModHandler.getModItem("harvestcraft", "scalloprawItem", 1, 0), GT_Values.NI, Materials.FishOil.getFluid(50L), 10000, 16, 4);
            GT_Values.RA.addFluidExtractionRecipe(GT_ModHandler.getModItem("harvestcraft", "shrimprawItem", 1, 0), GT_Values.NI, Materials.FishOil.getFluid(50L), 10000, 16, 4);
            GT_Values.RA.addFluidExtractionRecipe(GT_ModHandler.getModItem("harvestcraft", "snailrawItem", 1, 0), GT_Values.NI, Materials.FishOil.getFluid(50L), 10000, 16, 4);
            GT_Values.RA.addFluidExtractionRecipe(GT_ModHandler.getModItem("harvestcraft", "snapperrawItem", 1, 0), GT_Values.NI, Materials.FishOil.getFluid(50L), 10000, 16, 4);
            GT_Values.RA.addFluidExtractionRecipe(GT_ModHandler.getModItem("harvestcraft", "tilapiarawItem", 1, 0), GT_Values.NI, Materials.FishOil.getFluid(50L), 10000, 16, 4);
            GT_Values.RA.addFluidExtractionRecipe(GT_ModHandler.getModItem("harvestcraft", "troutrawItem", 1, 0), GT_Values.NI, Materials.FishOil.getFluid(50L), 10000, 16, 4);
            GT_Values.RA.addFluidExtractionRecipe(GT_ModHandler.getModItem("harvestcraft", "tunarawItem", 1, 0), GT_Values.NI, Materials.FishOil.getFluid(50L), 10000, 16, 4);
            GT_Values.RA.addFluidExtractionRecipe(GT_ModHandler.getModItem("harvestcraft", "turtlerawItem", 1, 0), GT_Values.NI, Materials.FishOil.getFluid(50L), 10000, 16, 4);
            GT_Values.RA.addFluidExtractionRecipe(GT_ModHandler.getModItem("harvestcraft", "walleyerawItem", 1, 0), GT_Values.NI, Materials.FishOil.getFluid(50L), 10000, 16, 4);
        }
        GT_Values.RA.addFluidExtractionRecipe(new ItemStack(Items.coal, 1, 1), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 1L), Materials.WoodTar.getFluid(100L), 1000, 128, 4);
        GT_Values.RA.addFluidExtractionRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 1L), ItemList.IC2_Plantball.get(1L, new Object[0]), Materials.Creosote.getFluid(5L), 100, 16, 4);
        GT_Values.RA.addFluidExtractionRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.HydratedCoal, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 1L), Materials.Water.getFluid(100L), 10000, 32, 4);
        GT_Values.RA.addFluidExtractionRecipe(GT_ModHandler.getModItem("Thaumcraft", "ItemResource", 1, 3), GT_Values.NI, Materials.Mercury.getFluid(1000L), 10000, 128, 4);
        GT_Values.RA.addFluidExtractionRecipe(GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Mercury, 1L), GT_Values.NI, Materials.Mercury.getFluid(1000L), 10000, 128, 4);
        GT_Values.RA.addFluidExtractionRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Monazite, 1L), GT_Values.NI, Materials.Helium.getGas(200L), 10000, 64, 64);

        GT_Values.RA.addFluidSmelterRecipe(new ItemStack(Items.snowball, 1, 0), GT_Values.NI, Materials.Water.getFluid(250L), 10000, 32, 4);
        GT_Values.RA.addFluidSmelterRecipe(new ItemStack(Blocks.snow, 1, 0), GT_Values.NI, Materials.Water.getFluid(1000L), 10000, 128, 4);
        GT_Values.RA.addFluidSmelterRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ice, 1L), GT_Values.NI, Materials.Ice.getSolid(1000L), 10000, 128, 4);
        GT_Values.RA.addFluidSmelterRecipe(GT_ModHandler.getModItem(aTextForestry, "phosphor", 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Phosphor, 1L), Materials.Lava.getFluid(800L), 1000, 256, 128);

        GT_Values.RA.addAutoclaveRecipe(ItemList.IC2_Energium_Dust.get(9L), Materials.EnergeticAlloy.getMolten(288), ItemList.IC2_EnergyCrystal.get(1L), 10000, 600, 256);
        GT_Values.RA.addAutoclaveRecipe(GT_ModHandler.getModItem(aTextAE, "item.ItemCrystalSeed", 1L, 0), Materials.Water.getFluid(200L), GT_ModHandler.getModItem(aTextAE, aTextAEMM, 1L, 10), 10000, 2000, 24);
        GT_Values.RA.addAutoclaveRecipe(GT_ModHandler.getModItem(aTextAE, "item.ItemCrystalSeed", 1L, 600), Materials.Water.getFluid(200L), GT_ModHandler.getModItem(aTextAE, aTextAEMM, 1L, 11), 10000, 2000, 24);
        GT_Values.RA.addAutoclaveRecipe(GT_ModHandler.getModItem(aTextAE, "item.ItemCrystalSeed", 1L, 1200), Materials.Water.getFluid(200L), GT_ModHandler.getModItem(aTextAE, aTextAEMM, 1L, 12), 10000, 2000, 24);
        GT_Values.RA.addAutoclaveRecipe(ItemList.IC2_Energium_Dust.get(9L), Materials.ConductiveIron.getMolten(576), ItemList.IC2_EnergyCrystal.get(1L), 10000, 1200, 256);
        GT_Values.RA.addAutoclaveRecipe(GT_ModHandler.getModItem(aTextAE, "item.ItemCrystalSeed", 1L, 0), GT_ModHandler.getDistilledWater(200L), GT_ModHandler.getModItem(aTextAE, aTextAEMM, 1L, 10), 10000, 1000, 24);
        GT_Values.RA.addAutoclaveRecipe(GT_ModHandler.getModItem(aTextAE, "item.ItemCrystalSeed", 1L, 600), GT_ModHandler.getDistilledWater(200L), GT_ModHandler.getModItem(aTextAE, aTextAEMM, 1L, 11), 10000, 1000, 24);
        GT_Values.RA.addAutoclaveRecipe(GT_ModHandler.getModItem(aTextAE, "item.ItemCrystalSeed", 1L, 1200), GT_ModHandler.getDistilledWater(200L), GT_ModHandler.getModItem(aTextAE, aTextAEMM, 1L, 12), 10000, 1000, 24);
        GT_Values.RA.addAutoclaveRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 64), Materials.Epoxid.getMolten(144), GT_ModHandler.getIC2Item("carbonFiber", 64L), 10000, 300, 480);
        GT_Values.RA.addAutoclaveRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 64), Materials.Polytetrafluoroethylene.getMolten(288), GT_ModHandler.getIC2Item("carbonFiber", 32L), 10000, 400, 120);
        GT_Values.RA.addAutoclaveRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 64), Materials.Plastic.getMolten(576), GT_ModHandler.getIC2Item("carbonFiber", 16L), 10000, 600, 30);
        GT_Values.RA.addAutoclaveRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.NetherStar, 1), Materials.UUMatter.getFluid(576), GT_OreDictUnificator.get(OrePrefixes.gem, Materials.NetherStar, 1), 3333, 72000, 480);

        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.EnderPearl, 1), GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 4), Materials.RedSteel.getMolten(576), ItemList.Field_Generator_LV.get(1), 1800, 30);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.EnderEye, 1), GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Data, 4), Materials.TungstenSteel.getMolten(576), ItemList.Field_Generator_MV.get(1), 1800, 120);
        GT_Values.RA.addAssemblerRecipe(ItemList.QuantumEye.get(1), GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Elite, 4), Materials.NiobiumTitanium.getMolten(1152), ItemList.Field_Generator_HV.get(1), 1800, 480);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.gem, Materials.NetherStar, 1), GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Master, 4), Materials.HSSG.getMolten(1152), ItemList.Field_Generator_EV.get(1), 1800, 1920);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{GT_ModHandler.getIC2Item("carbonMesh", 4L), GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Zinc, 16L),  GT_Utility.getIntegratedCircuit(1)}, GT_Values.NF, ItemList.Component_Filter.get(1L, new Object[0]), 1600, 30);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Steel, 64), GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Zinc, 16L),  GT_Utility.getIntegratedCircuit(1)}, Materials.Plastic.getFluid(144), ItemList.Component_Filter.get(1), 1600, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Graphite, 8), GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Silicon, 1), Materials.Glue.getFluid(250L), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Graphene, 1), 480, 240);
        GT_Values.RA.addAssemblerRecipe(ItemList.Electric_Pump_LV.get(1L, new Object[0]), OrePrefixes.circuit.get(Materials.Basic), 2, GT_Values.NF, ItemList.FluidRegulator_LV.get(1L, new Object[0]), 800, 4);//TODO Check
        GT_Values.RA.addAssemblerRecipe(ItemList.Electric_Pump_MV.get(1L, new Object[0]), OrePrefixes.circuit.get(Materials.Good), 2, GT_Values.NF, ItemList.FluidRegulator_MV.get(1L, new Object[0]), 800, 8);//TODO Check
        GT_Values.RA.addAssemblerRecipe(ItemList.Electric_Pump_HV.get(1L, new Object[0]), OrePrefixes.circuit.get(Materials.Advanced), 2, GT_Values.NF, ItemList.FluidRegulator_HV.get(1L, new Object[0]), 800, 16);//TODO Check
        GT_Values.RA.addAssemblerRecipe(ItemList.Electric_Pump_EV.get(1L, new Object[0]), OrePrefixes.circuit.get(Materials.Data), 2, GT_Values.NF, ItemList.FluidRegulator_EV.get(1L, new Object[0]), 800, 32);//TODO Check
        GT_Values.RA.addAssemblerRecipe(ItemList.Electric_Pump_IV.get(1L, new Object[0]), OrePrefixes.circuit.get(Materials.Elite), 2, GT_Values.NF, ItemList.FluidRegulator_IV.get(1L, new Object[0]), 800, 64);//TODO Check
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.StainlessSteel, 2L), OrePrefixes.circuit.get(Materials.Good), 4, GT_Values.NF, ItemList.Schematic.get(1L, new Object[0]), 3200, 4);//TODO Check
        GT_Values.RA.addAssemblerRecipe(ItemList.Cover_Shutter.get(1L, new Object[0]), OrePrefixes.circuit.get(Materials.Advanced), 2, GT_Values.NF, ItemList.FluidFilter.get(1L, new Object[0]), 800, 4);//TODO Check
        //TODO CHECK RECIPES BELOW
        GT_Values.RA.addCentrifugeRecipe(ItemList.Cell_Empty.get(1, new Object[0]), null, Materials.Air.getGas(10000), Materials.Nitrogen.getGas(3900), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 1), null, null, null, null, null, null, 1600, 8);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Galena, 3), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Sphalerite, 1), Materials.SulfuricAcid.getFluid(4000), new FluidStack(ItemList.sIndiumConcentrate, 8000), null, 60, 150);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 4), GT_Utility.getIntegratedCircuit(1), new FluidStack(ItemList.sIndiumConcentrate, 8000), new FluidStack(ItemList.sLeadZincSolution, 8000), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Indium, 1), 50, 600);
        GT_Values.RA.addElectrolyzerRecipe(null, null, new FluidStack(ItemList.sLeadZincSolution, 8000), Materials.Water.getFluid(2000), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lead, 1), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silver, 1), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Zinc, 1), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 3), null, null, null, 300, 192);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Pentlandite, 1), GT_Utility.getIntegratedCircuit(1), new FluidStack(ItemList.sNitricAcid, 1000), new FluidStack(ItemList.sNickelSulfate, 2000), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.PlatinumGroupSludge, 1), 50, 30);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Chalcopyrite, 1), GT_Utility.getIntegratedCircuit(1), new FluidStack(ItemList.sNitricAcid, 1000), new FluidStack(ItemList.sBlueVitriol, 2000), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.PlatinumGroupSludge, 1), 50, 30);
        GT_Values.RA.addElectrolyzerRecipe(ItemList.Cell_Empty.get(1, new Object[0]), null, new FluidStack(ItemList.sBlueVitriol, 2000), Materials.SulfuricAcid.getFluid(1000), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 1), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 1), null, null, null, null, null, 900, 30);
        GT_Values.RA.addElectrolyzerRecipe(ItemList.Cell_Empty.get(1, new Object[0]), null, new FluidStack(ItemList.sNickelSulfate, 2000), Materials.SulfuricAcid.getFluid(1000), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 1), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 1), null, null, null, null, null, 900, 30);
        GT_Values.RA.addCentrifugeRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.PlatinumGroupSludge, 1), null, null, null, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 1), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Gold, 1), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Platinum, 1), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Palladium, 1), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Iridium, 1), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Osmium, 1), new int[]{10000, 10000, 10000, 8000, 6000, 6000}, 900, 30);

        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 2), GT_Utility.getIntegratedCircuit(1),  Materials.HydrochloricAcid.getFluid(1000), new FluidStack(ItemList.sIron3Chloride, 2000), null, 400, 30);
        //GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Salt, 1), null, Materials.SulfuricAcid.getFluid(1000), new FluidStack(ItemList.sHydrochloricAcid, 1000), null, 200, 30);

        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfusedGold, 8L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 8L), new FluidStack(FluidRegistry.getFluid("ic2coolant"), 1000), GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Thaumium, 16L), 400, 480);

        GT_Values.RA.addSlicerRecipe(ItemList.Food_Dough_Chocolate.get(1L, new Object[0]), ItemList.Shape_Slicer_Flat.get(0L, new Object[0]), ItemList.Food_Raw_Cookie.get(4L, new Object[0]), 128, 4);
        GT_Values.RA.addSlicerRecipe(ItemList.Food_Baked_Bun.get(1L, new Object[0]), ItemList.Shape_Slicer_Flat.get(0L, new Object[0]), ItemList.Food_Sliced_Bun.get(2L, new Object[0]), 128, 4);
        GT_Values.RA.addSlicerRecipe(ItemList.Food_Baked_Bread.get(1L, new Object[0]), ItemList.Shape_Slicer_Flat.get(0L, new Object[0]), ItemList.Food_Sliced_Bread.get(2L, new Object[0]), 128, 4);
        GT_Values.RA.addSlicerRecipe(ItemList.Food_Baked_Baguette.get(1L, new Object[0]), ItemList.Shape_Slicer_Flat.get(0L, new Object[0]), ItemList.Food_Sliced_Baguette.get(2L, new Object[0]), 128, 4);
        //Moved to Core Mod
        Object[] o = new Object[0];
        //GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Board_Coated.get(3, o), new Object[]{"RRR", "PPP", "RRR", 'P', GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Wood, 1), 'R', ItemList.IC2_Resin.get(1, o)});
        //GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Board_Coated_Basic.get(1, o), new Object[]{"FFF", "FCF", "FFF", 'C', ItemList.Circuit_Board_Coated.get(1, o), 'F', GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Copper, 1)});
        //GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Wood, 8), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Copper, 32), Materials.Glue.getFluid(576), ItemList.Circuit_Board_Coated_Basic.get(8, o), 1600, 8);
        //GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 8), ItemList.Circuit_Integrated.get(0, o), Materials.Glue.getFluid(288), ItemList.Circuit_Board_Phenolic.get(8, o), 2400, 16);
        //GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Board_Phenolic_Good.get(1, o), new Object[]{"FFF", "FCF", "FFF", 'C', ItemList.Circuit_Board_Phenolic.get(1, o), 'F', GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Gold, 1)});
        //GT_Values.RA.addChemicalRecipe(ItemList.Circuit_Board_Phenolic.get(1, o), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Gold, 4), new FluidStack(ItemList.sIron3Chloride, 100), null, ItemList.Circuit_Board_Phenolic_Good.get(1, o), 600, 30);
        //GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Epoxid, 1), GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Gold, 8), Materials.SulfuricAcid.getFluid(500), null, ItemList.Circuit_Board_Epoxy.get(1, o), 600, 30);
        //GT_Values.RA.addChemicalRecipe(ItemList.Circuit_Board_Epoxy.get(1, o), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Electrum, 8), new FluidStack(ItemList.sIron3Chloride, 500), null, ItemList.Circuit_Board_Epoxy_Advanced.get(1, o), 1200, 30);
        //GT_Values.RA.addBlastRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glass, 32), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 16), Materials.Electrum.getMolten(288), null, ItemList.Circuit_Board_Fiberglass.get(2, o), null, 1200, 480, 2600);
        //GT_Values.RA.addChemicalRecipe(ItemList.Circuit_Board_Fiberglass.get(1, o), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.AnnealedCopper, 16), new FluidStack(ItemList.sIron3Chloride, 1000), null, ItemList.Circuit_Board_Fiberglass_Advanced.get(1, o), 1800, 30, true);
        //GT_Values.RA.addChemicalRecipe(ItemList.Circuit_Board_Fiberglass.get(2, o), GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Platinum, 32), Materials.SulfuricAcid.getFluid(500), null, ItemList.Circuit_Board_Multifiberglass.get(1, o), 600, 480, true);
        //GT_Values.RA.addChemicalRecipe(ItemList.Circuit_Board_Multifiberglass.get(1, o), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Platinum, 32), new FluidStack(ItemList.sIron3Chloride, 2000), null, ItemList.Circuit_Board_Multifiberglass_Elite.get(1, o), 2400, 120, true);
        //GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Board_Wetware.get(1, o), new Object[]{"DLD", "PBP", "TPT", 'T', GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Polytetrafluoroethylene, 1), 'L', ItemList.Electric_Pump_LV.get(1, o), 'P', GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Silicone, 1), 'D', ItemList.Bottle_Dragon_Blood.get(1, o), 'B', ItemList.Circuit_Board_Multifiberglass.get(1, o)});
        //GT_Values.RA.addChemicalRecipe(ItemList.Circuit_Board_Wetware.get(1, o), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.NiobiumTitanium, 64), new FluidStack(ItemList.sIron3Chloride, 5000), null, ItemList.Circuit_Board_Wetware_Extreme.get(1, o), 3000, 480, true);

        //GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Parts_Resistor.get(1, o), new Object[]{"RPR", "FCF", " P ", 'F', OrePrefixes.wireGt01.get(Materials.Copper), 'P', OrePrefixes.wireFine.get(Materials.Copper), 'C', OrePrefixes.dust.get(Materials.Coal), 'R', ItemList.IC2_Resin.get(1, o)});
        //GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Parts_Resistor.get(3, o), new Object[]{" P ", "FCF", " P ", 'P', new ItemStack(Items.paper), 'F', OrePrefixes.wireFine.get(Materials.Copper), 'C', OrePrefixes.dust.get(Materials.Coal)});
        //GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 1), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Copper, 4), Materials.Glue.getFluid(288), ItemList.Circuit_Parts_Resistor.get(4, o), 320, 16);
        //GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 1), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Electrum, 4), Materials.Plastic.getMolten(288), ItemList.Circuit_Parts_ResistorSMD.get(8, o), 320, 96, true);
        //GT_Values.RA.addAlloySmelterRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glass, 1), ItemList.Shape_Mold_Ball.get(0, o), ItemList.Circuit_Parts_Glass_Tube.get(1, o), 240, 16);
        //GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Parts_Vacuum_Tube.get(1, o), new Object[]{"WWW", "FGF", "SAS", 'G', ItemList.Circuit_Parts_Glass_Tube.get(1, o), 'F', OrePrefixes.wireFine.get(Materials.Copper), 'W', OrePrefixes.wireGt01.get(Materials.Copper), 'S', OrePrefixes.stickLong.get(Materials.Steel), 'A', OrePrefixes.bolt.get(Materials.RedAlloy)});
        //GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Parts_Vacuum_Tube.get(1, o), new Object[]{"PSP","FGF",'G',ItemList.Circuit_Parts_Glass_Tube,'P',new ItemStack(Items.paper),'W',OrePrefixes.wireGt01.get(Materials.Copper)});
        //GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Parts_Diode.get(1,o), new Object[]{"BG ","WDW","BG ",'B',OrePrefixes.dye.get(Materials.Black),'G',new ItemStack(Blocks.glass_pane),'D',ItemList.Circuit_Silicon_Wafer.get(1, new Object[0]),'W',OrePrefixes.wireGt01.get(Materials.Tin)});
        //GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Parts_Diode.get(1,o), new Object[]{"BG ","WDW","BG ",'B',OrePrefixes.dye.get(Materials.Black),'G',new ItemStack(Blocks.glass_pane),'D',ItemList.Circuit_Silicon_Wafer.get(1, new Object[0]),'W',OrePrefixes.wireFine.get(Materials.Tin)});
        //GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Parts_Diode.get(4,o), new Object[]{"BG ","WDW","BG ",'B',OrePrefixes.dye.get(Materials.Black),'G',new ItemStack(Blocks.glass_pane),'D',GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Gallium, 1),'W',OrePrefixes.wireGt01.get(Materials.Tin)});
        //GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Parts_Diode.get(4,o), new Object[]{"BG ","WDW","BG ",'B',OrePrefixes.dye.get(Materials.Black),'G',new ItemStack(Blocks.glass_pane),'D',GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Gallium, 1),'W',OrePrefixes.wireFine.get(Materials.Tin)});
        //GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Tin, 8), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.GalliumArsenide, 1), Materials.Glass.getMolten(288), ItemList.Circuit_Parts_Diode.get(8, o), 600, 30);
        //GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Tin, 4), ItemList.Circuit_Silicon_Wafer.get(1, o), Materials.Glass.getMolten(288), ItemList.Circuit_Parts_Diode.get(4, o), 600, 30);
        //GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Platinum, 8), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.GalliumArsenide, 1), Materials.Glass.getMolten(288), ItemList.Circuit_Parts_DiodeSMD.get(16, o), 600, 192, true);
        //GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Parts_Coil.get(1, o), new Object[]{"WWW", "WDW", "WWW", 'G', new ItemStack(Blocks.glass_pane), 'D', ItemList.IC2_Item_Casing_Steel.get(1, o), 'W', OrePrefixes.wireFine.get(Materials.Copper)});
        //GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.IronMagnetic, 1), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Copper, 16), Materials.Plastic.getFluid(144), ItemList.Circuit_Parts_Coil.get(4, o), 320, 30);
        //GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Silicon, 1), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Tin, 8), Materials.Plastic.getMolten(144), ItemList.Circuit_Parts_Transistor.get(8, o), 320, 30);
        //GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Gallium, 1), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.AnnealedCopper, 8), Materials.Plastic.getMolten(288), ItemList.Circuit_Parts_TransistorSMD.get(16, o), 320, 120, true);
        //GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Plastic, 2), GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 4), Materials.Plastic.getMolten(288), ItemList.Circuit_Parts_Capacitor.get(4, o), 320, 120);
        //GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Silicone, 2), GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 4), Materials.Plastic.getMolten(288), ItemList.Circuit_Parts_CapacitorSMD.get(8, o), 320, 480, true);

        //GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer2.get(1, new Object[0]), GT_Utility.copyAmount(0, GT_OreDictUnificator.get(OrePrefixes.lens, Materials.EnderPearl, 1)), ItemList.Circuit_Wafer_NAND.get(1, new Object[0]), 900, 480, true);
        //GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer3.get(1, new Object[0]), GT_Utility.copyAmount(0, GT_OreDictUnificator.get(OrePrefixes.lens, Materials.EnderPearl, 1)), ItemList.Circuit_Wafer_NAND.get(4, new Object[0]), 600, 1920, true);
        //GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer2.get(1, new Object[0]), GT_Utility.copyAmount(0, GT_OreDictUnificator.get(OrePrefixes.lens, Materials.EnderEye, 1)), ItemList.Circuit_Wafer_NOR.get(1, new Object[0]), 900, 480, true);
        //GT_Values.RA.addLaserEngraverRecipe(ItemList.Circuit_Silicon_Wafer3.get(1, new Object[0]), GT_Utility.copyAmount(0, GT_OreDictUnificator.get(OrePrefixes.lens, Materials.EnderEye, 1)), ItemList.Circuit_Wafer_NOR.get(4, new Object[0]), 600, 1920, true);
        //GT_Values.RA.addCutterRecipe(ItemList.Circuit_Wafer_ILC.get(1, o), ItemList.Circuit_Chip_ILC.get(8, o), null, 900, 64);
        //GT_Values.RA.addCutterRecipe(ItemList.Circuit_Wafer_Ram.get(1, o), ItemList.Circuit_Chip_Ram.get(32, o), null, 900, 96);
        //GT_Values.RA.addCutterRecipe(ItemList.Circuit_Wafer_NAND.get(1, o), ItemList.Circuit_Chip_NAND.get(32, o), null, 900, 192, true);
        //GT_Values.RA.addCutterRecipe(ItemList.Circuit_Wafer_NOR.get(1, o), ItemList.Circuit_Chip_NOR.get(16, o), null, 900, 192, true);
        //GT_Values.RA.addCutterRecipe(ItemList.Circuit_Wafer_CPU.get(1, o), ItemList.Circuit_Chip_CPU.get(8, o), null, 900, 120, true);
        //GT_Values.RA.addCutterRecipe(ItemList.Circuit_Wafer_SoC.get(1, o), ItemList.Circuit_Chip_SoC.get(6, o), null, 900, 480, true);
        //GT_Values.RA.addCutterRecipe(ItemList.Circuit_Wafer_SoC2.get(1, o), ItemList.Circuit_Chip_SoC2.get(6, o), null, 900, 1024, true);
        //GT_Values.RA.addCutterRecipe(ItemList.Circuit_Wafer_PIC.get(1, o), ItemList.Circuit_Chip_PIC.get(4, o), null, 900, 1024, true);
        //GT_Values.RA.addCutterRecipe(ItemList.Circuit_Wafer_HPIC.get(1, o), ItemList.Circuit_Chip_HPIC.get(2, o), null, 900, 4096, true);
        //GT_Values.RA.addCutterRecipe(ItemList.Circuit_Wafer_NanoCPU.get(1, o), ItemList.Circuit_Chip_NanoCPU.get(7, o), null, 900, 480, true);
        //GT_Values.RA.addCutterRecipe(ItemList.Circuit_Wafer_QuantumCPU.get(1, o), ItemList.Circuit_Chip_QuantumCPU.get(5, o), null, 900, 1920, true);
        //GT_Values.RA.addChemicalRecipe(ItemList.Circuit_Wafer_PIC.get(1, o), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.IndiumGalliumPhosphide, 2), Materials.RedstoneAlloy.getMolten(288), null, ItemList.Circuit_Wafer_HPIC.get(1, o), 1200, 1920, true);
        //GT_Values.RA.addChemicalRecipe(ItemList.Circuit_Wafer_CPU.get(1, o), GT_Utility.copyAmount(16, ic2.core.Ic2Items.carbonFiber), Materials.Glowstone.getMolten(576), null, ItemList.Circuit_Wafer_NanoCPU.get(1, o), 1200, 1920, true);
        //GT_Values.RA.addChemicalRecipe(ItemList.Circuit_Wafer_NanoCPU.get(1, o), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.IndiumGalliumPhosphide, 1), Materials.Radon.getGas(50), null, ItemList.Circuit_Wafer_QuantumCPU.get(1, o), 1200, 1920, true);
        //GT_Values.RA.addChemicalRecipe(ItemList.Circuit_Wafer_NanoCPU.get(1, o), ItemList.QuantumEye.get(2, o), Materials.GalliumArsenide.getMolten(288), null, ItemList.Circuit_Wafer_QuantumCPU.get(1, o), 900, 1920, true);

        //GT_Values.RA.addAutoclaveRecipe(GT_OreDictUnificator.get(OrePrefixes.gemExquisite, Materials.Emerald, 1), Materials.Europium.getMolten(16), ItemList.Circuit_Parts_RawCrystalChip.get(1, o), 1000, 12000, 320, true);
        //GT_Values.RA.addAutoclaveRecipe(GT_OreDictUnificator.get(OrePrefixes.gemExquisite, Materials.Olivine, 1), Materials.Europium.getMolten(16), ItemList.Circuit_Parts_RawCrystalChip.get(1, o), 1000, 12000, 320, true);
        //GT_Values.RA.addAutoclaveRecipe(GT_OreDictUnificator.get(OrePrefixes.gemExquisite, Materials.Emerald, 12), Materials.UUMatter.getFluid(250), ItemList.Tool_DataOrb.get(1, o), 10000, 12000, 960, true);
        //GT_Values.RA.addAutoclaveRecipe(GT_OreDictUnificator.get(OrePrefixes.gemExquisite, Materials.Olivine, 12), Materials.UUMatter.getFluid(250), ItemList.Tool_DataOrb.get(1, o), 10000, 12000, 960, true);
        //GT_ModHandler.addShapelessCraftingRecipe(ItemList.Circuit_Parts_RawCrystalChip.get(9, o), new Object[]{ItemList.Circuit_Chip_CrystalCPU.get(1, o)});
        //GT_Values.RA.addBlastRecipe(ItemList.Circuit_Parts_RawCrystalChip.get(1, o), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Emerald, 1), Materials.Helium.getGas(1000), null, ItemList.Circuit_Parts_Crystal_Chip_Elite.get(1, o), null, 900, 480, 5000);
        //GT_Values.RA.addBlastRecipe(ItemList.Circuit_Parts_RawCrystalChip.get(1, o), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Olivine, 1), Materials.Helium.getGas(1000), null, ItemList.Circuit_Parts_Crystal_Chip_Elite.get(1, o), null, 900, 480, 5000);

        //GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Good.get(1, o), new Object[]{"PAP", "CBC", "DCD", 'D', ItemList.Circuit_Parts_Diode.get(1, o), 'C', Ic2Items.electronicCircuit, 'A', CustomItemList.AluminiumItemCasing.get(1, o), 'P', GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Plastic, 1), 'B', ItemList.Circuit_Board_Phenolic_Good.get(1, o),});
        //Core Mod End
        //OLD RECIPES
        //GT_Values.RA.addFormingPressRecipe(ItemList.Empty_Board_Basic.get(1), ItemList.Circuit_Parts_Wiring_Basic.get(4L), ItemList.Circuit_Board_Basic.get(1L), 32, 16);
        //GT_Values.RA.addFormingPressRecipe(ItemList.Empty_Board_Basic.get(1), ItemList.Circuit_Parts_Wiring_Advanced.get(4L), ItemList.Circuit_Board_Advanced.get(1L), 32, 64);
        //GT_Values.RA.addFormingPressRecipe(ItemList.Empty_Board_Elite.get(1), ItemList.Circuit_Parts_Wiring_Elite.get(4L), ItemList.Circuit_Board_Elite.get(1L), 32, 256);
        //GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Lapis, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glowstone, 1L), ItemList.Circuit_Parts_Advanced.get(2L), 32, 64);
        //GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Lazurite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glowstone, 1L), ItemList.Circuit_Parts_Advanced.get(2L), 32, 64);

        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Cylinder.get(0, o), Materials.Polytetrafluoroethylene.getMolten(36), ItemList.Circuit_Parts_PetriDish.get(1, o), 160, 16);
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Cylinder.get(0, o), Materials.Polystyrene.getMolten(36), ItemList.Circuit_Parts_PetriDish.get(1, o), 160, 16);
        GT_Values.RA.addMixerRecipe(Materials.Sugar.getDust(4), Materials.MeatRaw.getDust(1), Materials.Salt.getDustTiny(1), GT_Values.NI, GT_ModHandler.getDistilledWater(4000), Materials.GrowthMediumRaw.getFluid(4000), GT_Values.NI, 160, 16);
        GT_Values.RA.addFluidHeaterRecipe(GT_Utility.getIntegratedCircuit(1), Materials.GrowthMediumRaw.getFluid(1000), Materials.GrowthMediumSterilized.getFluid(1000), 60, 24);
        GT_Values.RA.addFluidHeaterRecipe(GT_Utility.getIntegratedCircuit(1), FluidRegistry.getFluidStack("potion.dragonblood", 1000), Materials.GrowthMediumSterilized.getFluid(1000), 60, 24);

        GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 1L), GT_ModHandler.getModItem("BuildCraft|Silicon", "redstoneChipset", 1L, 0), GT_ModHandler.getModItem("BuildCraft|Silicon", "redstoneChipset", 1L, 1), 100, 120);
        GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 1L), GT_ModHandler.getModItem("BuildCraft|Silicon", "redstoneChipset", 1L, 0), GT_ModHandler.getModItem("BuildCraft|Silicon", "redstoneChipset", 1L, 1), 100, 120);
        GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Gold, 1L), GT_ModHandler.getModItem("BuildCraft|Silicon", "redstoneChipset", 1L, 0), GT_ModHandler.getModItem("BuildCraft|Silicon", "redstoneChipset", 1L, 2), 200, 120);
        GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Diamond, 1L), GT_ModHandler.getModItem("BuildCraft|Silicon", "redstoneChipset", 1L, 0), GT_ModHandler.getModItem("BuildCraft|Silicon", "redstoneChipset", 1L, 3), 100, 480);
        GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.gem, Materials.EnderPearl, 1L), GT_ModHandler.getModItem("BuildCraft|Silicon", "redstoneChipset", 1L, 0), GT_ModHandler.getModItem("BuildCraft|Silicon", "redstoneChipset", 2L, 4), 200, 120);
        GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NetherQuartz, 1L), GT_ModHandler.getModItem("BuildCraft|Silicon", "redstoneChipset", 1L, 0), GT_ModHandler.getModItem("BuildCraft|Silicon", "redstoneChipset", 1L, 5), 300, 120);
        GT_Values.RA.addFormingPressRecipe(new ItemStack(Items.comparator, 1, 32767), GT_ModHandler.getModItem("BuildCraft|Silicon", "redstoneChipset", 1L, 0), GT_ModHandler.getModItem("BuildCraft|Silicon", "redstoneChipset", 1L, 6), 300, 120);
        GT_Values.RA.addFormingPressRecipe(GT_ModHandler.getModItem(aTextAE, aTextAEMM, 1L, 10), GT_ModHandler.getModItem(aTextAE, aTextAEMM, 0L, 13), GT_ModHandler.getModItem(aTextAE, aTextAEMM, 1L, 16), 200, 16);
        GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.CertusQuartz, 1L), GT_ModHandler.getModItem(aTextAE, aTextAEMM, 0L, 13), GT_ModHandler.getModItem(aTextAE, aTextAEMM, 1L, 16), 200, 16);
        GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Diamond, 1L), GT_ModHandler.getModItem(aTextAE, aTextAEMM, 0L, 14), GT_ModHandler.getModItem(aTextAE, aTextAEMM, 1L, 17), 200, 16);
        GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Gold, 1L), GT_ModHandler.getModItem(aTextAE, aTextAEMM, 0L, 15), GT_ModHandler.getModItem(aTextAE, aTextAEMM, 1L, 18), 200, 16);
        GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Silicon, 1L), GT_ModHandler.getModItem(aTextAE, aTextAEMM, 0L, 19), GT_ModHandler.getModItem(aTextAE, aTextAEMM, 1L, 20), 200, 16);
        run2();
        GT_Values.RA.addFormingPressRecipe(ItemList.Food_Dough_Sugar.get(4L, new Object[0]), ItemList.Shape_Mold_Cylinder.get(0L, new Object[0]), ItemList.Food_Raw_Cake.get(1L, new Object[0]), 384, 4);
        GT_Values.RA.addFormingPressRecipe(new ItemStack(Blocks.glass, 1, 32767), ItemList.Shape_Mold_Arrow.get(0L, new Object[0]), ItemList.Arrow_Head_Glass_Emtpy.get(1L, new Object[0]), 64, 4);
        for (Materials tMat : Materials.values()) {
            if (tMat.mStandardMoltenFluid != null && tMat.contains(SubTag.SOLDERING_MATERIAL) && !(GregTech_API.mUseOnlyGoodSolderingMaterials && !tMat.contains(SubTag.SOLDERING_MATERIAL_GOOD))) {
                int tMultiplier = tMat.contains(SubTag.SOLDERING_MATERIAL_GOOD) ? 1 : tMat.contains(SubTag.SOLDERING_MATERIAL_BAD) ? 4 : 2;
                //Circuit soldering
                //Integraded Circuits //TODO CHECK THERE ARE CHANGES IN THE CIRCUIT ASSEMBLER RECIPES, DREAM adjust and comment out again i guess, MOSTLY AMOUNT CHANGES
                //GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{ItemList.Circuit_Board_Phenolic.get(1,o),ItemList.Circuit_Chip_ILC.get(1,o),ItemList.Circuit_Parts_Resistor.get(2,o),GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Copper, 4)},tMat.getMolten(144L * tMultiplier / 2L), ItemList.Circuit_Basic.get(1,o), 200, 8);
                //GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{ItemList.Circuit_Board_Phenolic.get(1,o),ItemList.Circuit_Chip_ILC.get(1,o),ItemList.Circuit_Parts_ResistorSMD.get(2,o),GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Copper, 4)},tMat.getMolten(144L * tMultiplier / 2L), ItemList.Circuit_Basic.get(1,o), 200, 8);
                //GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{ItemList.Circuit_Board_Phenolic.get(1,o),ItemList.Circuit_Basic.get(3,o),ItemList.Circuit_Parts_Resistor.get(4,o),GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Gold, 8)},tMat.getMolten(144L * tMultiplier / 2L), ItemList.Circuit_Integrated_Good.get(1,o), 400, 16);
                //GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{ItemList.Circuit_Board_Phenolic.get(1,o),ItemList.Circuit_Basic.get(3,o),ItemList.Circuit_Parts_ResistorSMD.get(4,o),GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Gold, 8)},tMat.getMolten(144L * tMultiplier / 2L), ItemList.Circuit_Integrated_Good.get(1,o), 400, 16);

                //Advanced circuit
                //GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{ItemList.Circuit_Integrated_Good.get(2,new Object[0]),ItemList.Circuit_Chip_ILC.get(3,new Object[0]),ItemList.Circuit_Chip_Ram.get(1,new Object[0]),ItemList.Circuit_Parts_Transistor.get(4,new Object[0]),GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Electrum, 16)},tMat.getMolten(144L * tMultiplier / 2L), GT_ModHandler.getIC2Item("advancedCircuit", 1L), 800, 28);
                //GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{ItemList.Circuit_Integrated_Good.get(2,new Object[0]),ItemList.Circuit_Chip_ILC.get(3,new Object[0]),ItemList.Circuit_Chip_Ram.get(1,new Object[0]),ItemList.Circuit_Parts_TransistorSMD.get(4,new Object[0]),GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Electrum, 16)},tMat.getMolten(144L * tMultiplier / 2L), GT_ModHandler.getIC2Item("advancedCircuit", 1L), 800, 28);

                //Highly Integrated Circuits
                //GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{ItemList.Circuit_Board_Epoxy.get(1,o),ItemList.Circuit_Chip_CPU.get(1,o),ItemList.Circuit_Parts_Resistor.get(4,o),ItemList.Circuit_Parts_Capacitor.get(4,o),ItemList.Circuit_Parts_Transistor.get(4,o),GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.RedAlloy, 4)},tMat.getMolten(144L * tMultiplier / 2L), ItemList.Circuit_Processor.get(1,o), 200, 60);
                //GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{ItemList.Circuit_Board_Epoxy.get(1,o),ItemList.Circuit_Processor.get(3,o),ItemList.Circuit_Parts_Coil.get(4,o),ItemList.Circuit_Parts_Capacitor.get(4,o),ItemList.Circuit_Chip_Ram.get(4,o),GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.RedAlloy, 12)},tMat.getMolten(144L * tMultiplier), ItemList.Circuit_Computer.get(1,o), 400, 90);
                //GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{ItemList.Circuit_Board_Epoxy.get(1,o),ItemList.Circuit_Processor.get(1,o),ItemList.Circuit_Chip_NAND.get(32,o),ItemList.Circuit_Chip_Ram.get(4,o),GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.RedAlloy, 8),GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Plastic, 4)},tMat.getMolten(144L * tMultiplier), ItemList.Tool_DataStick.get(1,o), 400, 90);
                //GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{ItemList.Circuit_Board_Epoxy.get(2,o),ItemList.Circuit_Advanced.get(2,o),ItemList.Circuit_Parts_Diode.get(4,o),ItemList.Circuit_Chip_Ram.get(4,o),GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Electrum, 6)},tMat.getMolten(144L * tMultiplier), ItemList.Circuit_Data.get(1,o), 400, 90);
                //GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Aluminium, 1),ItemList.Circuit_Data.get(4,o),ItemList.Circuit_Parts_Coil.get(4,o),ItemList.Circuit_Parts_Capacitor.get(24,o),ItemList.Circuit_Chip_Ram.get(16,o),GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.AnnealedCopper, 12)},tMat.getMolten(144L * tMultiplier*2), ItemList.Circuit_Elite.get(1,o), 1600, 480);
                //GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{ItemList.Circuit_Board_Epoxy.get(1,o),ItemList.Circuit_Chip_CPU.get(1,o),ItemList.Circuit_Parts_ResistorSMD.get(4,o),ItemList.Circuit_Parts_CapacitorSMD.get(4,o),ItemList.Circuit_Parts_TransistorSMD.get(4,o),GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.RedAlloy, 4)},tMat.getMolten(144L * tMultiplier / 2L), ItemList.Circuit_Processor.get(1,o), 200, 60);
                //GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{ItemList.Circuit_Board_Epoxy.get(1,o),ItemList.Circuit_Processor.get(3,o),ItemList.Circuit_Parts_Coil.get(4,o),ItemList.Circuit_Parts_CapacitorSMD.get(4,o),ItemList.Circuit_Chip_Ram.get(4,o),GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.RedAlloy, 12)},tMat.getMolten(144L * tMultiplier), ItemList.Circuit_Computer.get(1,o), 400, 80);
                //GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{ItemList.Circuit_Board_Epoxy.get(2,o),ItemList.Circuit_Advanced.get(2,o),ItemList.Circuit_Parts_DiodeSMD.get(4,o),ItemList.Circuit_Chip_Ram.get(4,o),GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Electrum, 6)},tMat.getMolten(144L * tMultiplier), ItemList.Circuit_Data.get(1,o), 400, 90);
                //GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Aluminium, 1),ItemList.Circuit_Data.get(4,o),ItemList.Circuit_Parts_Coil.get(4,o),ItemList.Circuit_Parts_CapacitorSMD.get(24,o),ItemList.Circuit_Chip_Ram.get(16,o),GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.AnnealedCopper, 12)},tMat.getMolten(144L * tMultiplier*2), ItemList.Circuit_Elite.get(1,o), 1600, 480);
                //Nanotech Circuits
                //GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{ItemList.Circuit_Board_Fiberglass.get(1,o),ItemList.Circuit_Chip_NanoCPU.get(1,o),ItemList.Circuit_Parts_ResistorSMD.get(4,o),ItemList.Circuit_Parts_CapacitorSMD.get(4,o),ItemList.Circuit_Parts_TransistorSMD.get(4,o),GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Electrum, 2)},tMat.getMolten(144L * tMultiplier / 2L), ItemList.Circuit_Nanoprocessor.get(1,o), 200, 600);
                //GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{ItemList.Circuit_Board_Fiberglass.get(1,o),ItemList.Circuit_Nanoprocessor.get(3,o),ItemList.Circuit_Parts_Coil.get(4,o),ItemList.Circuit_Parts_CapacitorSMD.get(4,o),ItemList.Circuit_Chip_Ram.get(4,o),GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Electrum, 6)},tMat.getMolten(144L * tMultiplier), ItemList.Circuit_Nanocomputer.get(1,o), 400, 600);
                //GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{ItemList.Circuit_Board_Fiberglass.get(1,o),ItemList.Circuit_Nanoprocessor.get(1,o),ItemList.Circuit_Chip_Ram.get(4,o),ItemList.Circuit_Chip_NOR.get(32,o),ItemList.Circuit_Chip_NAND.get(64,o),GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Platinum, 32)},tMat.getMolten(144L * tMultiplier), ItemList.Tool_DataOrb.get(1,o), 400, 1200);
       //NEW?   //GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{ItemList.Circuit_Board_Fiberglass.get(2,o),ItemList.Circuit_Nanocomputer.get(2,o),ItemList.Circuit_Parts_DiodeSMD.get(4,o),ItemList.Circuit_Chip_NOR.get(4,o),ItemList.Circuit_Chip_Ram.get(4,o),GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Electrum, 6)},tMat.getMolten(144L * tMultiplier), ItemList.Circuit_Elitenanocomputer.get(1,o), 400, 600);
                //GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Aluminium, 1),ItemList.Circuit_Elitenanocomputer.get(4,o),ItemList.Circuit_Parts_Coil.get(4,o),ItemList.Circuit_Parts_CapacitorSMD.get(24,o),ItemList.Circuit_Chip_Ram.get(16,o),GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.AnnealedCopper, 12)},tMat.getMolten(144L * tMultiplier*2), ItemList.Circuit_Master.get(1,o), 1600, 1920);            
                //Quantum Circuits
                //GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{ItemList.Circuit_Board_Multifiberglass.get(1,o),ItemList.Circuit_Chip_QuantumCPU.get(1,o),ItemList.Circuit_Chip_NanoCPU.get(1,o),ItemList.Circuit_Parts_CapacitorSMD.get(4,o),ItemList.Circuit_Parts_TransistorSMD.get(4,o),GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Platinum, 2)},tMat.getMolten(144L * tMultiplier / 2L), ItemList.Circuit_Quantumprocessor.get(1,o), 200, 2400);
                //GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{ItemList.Circuit_Board_Multifiberglass.get(1,o),ItemList.Circuit_Quantumprocessor.get(3,o),ItemList.Circuit_Parts_Coil.get(4,o),ItemList.Circuit_Parts_CapacitorSMD.get(4,o),ItemList.Circuit_Chip_Ram.get(4,o),GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Platinum, 6)},tMat.getMolten(144L * tMultiplier), ItemList.Circuit_Quantumcomputer.get(1,o), 400, 2400);
                //GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{ItemList.Circuit_Board_Multifiberglass.get(2,o),ItemList.Circuit_Quantumcomputer.get(2,o),ItemList.Circuit_Parts_DiodeSMD.get(4,o),ItemList.Circuit_Chip_NOR.get(4,o),ItemList.Circuit_Chip_Ram.get(4,o),GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Platinum, 6)},tMat.getMolten(144L * tMultiplier), ItemList.Circuit_Masterquantumcomputer.get(1,o), 400, 2400);
                //GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Aluminium, 1),ItemList.Circuit_Masterquantumcomputer.get(4,o),ItemList.Circuit_Parts_Coil.get(4,o),ItemList.Circuit_Parts_CapacitorSMD.get(24,o),ItemList.Circuit_Chip_Ram.get(16,o),GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.AnnealedCopper, 12)},tMat.getMolten(144L * tMultiplier*2), ItemList.Circuit_Quantummainframe.get(1,o), 1600, 7680);            
                //Crystallized Circuits
                //GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{ItemList.Circuit_Board_Multifiberglass.get(1,o),ItemList.Circuit_Chip_CrystalCPU.get(1,o),ItemList.Circuit_Chip_NanoCPU.get(1,o),ItemList.Circuit_Parts_CapacitorSMD.get(4,o),ItemList.Circuit_Parts_TransistorSMD.get(4,o),GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.NiobiumTitanium, 2)},tMat.getMolten(144L * tMultiplier / 2L), ItemList.Circuit_Crystalprocessor.get(1,o), 200, 9600);
                //GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{ItemList.Circuit_Board_Multifiberglass.get(1,o),ItemList.Circuit_Crystalprocessor.get(3,o),ItemList.Circuit_Parts_Coil.get(4,o),ItemList.Circuit_Parts_CapacitorSMD.get(4,o),ItemList.Circuit_Chip_Ram.get(4,o),GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.NiobiumTitanium, 6)},tMat.getMolten(144L * tMultiplier), ItemList.Circuit_Crystalcomputer.get(1,o), 400, 9600);
                //GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{ItemList.Circuit_Board_Multifiberglass.get(2,o),ItemList.Circuit_Crystalcomputer.get(2,o),ItemList.Circuit_Parts_DiodeSMD.get(4,o),ItemList.Circuit_Chip_NOR.get(4,o),ItemList.Circuit_Chip_Ram.get(4,o),GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.NiobiumTitanium, 6)},tMat.getMolten(144L * tMultiplier), ItemList.Circuit_Ultimatecrystalcomputer.get(1,o), 400, 9600);
                //GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Aluminium, 1),ItemList.Circuit_Ultimatecrystalcomputer.get(4,o),ItemList.Circuit_Parts_Coil.get(4,o),ItemList.Circuit_Parts_CapacitorSMD.get(24,o),ItemList.Circuit_Chip_Ram.get(16,o),GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Superconductor, 12)},tMat.getMolten(144L * tMultiplier*2), ItemList.Circuit_Crystalmainframe.get(1,o), 1600, 30720);
                //Wetware Circuits
                //GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{ItemList.Circuit_Board_Wetware.get(1,o),ItemList.Circuit_Chip_NeuroCPU.get(1,o),ItemList.Circuit_Chip_CrystalCPU.get(1,o),ItemList.Circuit_Parts_CapacitorSMD.get(4,o),ItemList.Circuit_Parts_TransistorSMD.get(4,o),GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.YttriumBariumCuprate, 2)},tMat.getMolten(144L * tMultiplier / 2L), ItemList.Circuit_Neuroprocessor.get(1,o), 200, 38400);
                //GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{ItemList.Circuit_Board_Wetware.get(1,o),ItemList.Circuit_Neuroprocessor.get(3,o),ItemList.Circuit_Parts_Coil.get(4,o),ItemList.Circuit_Parts_CapacitorSMD.get(4,o),ItemList.Circuit_Chip_Ram.get(4,o),GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.YttriumBariumCuprate, 6)},tMat.getMolten(144L * tMultiplier), ItemList.Circuit_Wetwarecomputer.get(1,o), 400, 38400);
                //GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{ItemList.Circuit_Board_Wetware.get(2,o),ItemList.Circuit_Wetwarecomputer.get(2,o),ItemList.Circuit_Parts_DiodeSMD.get(4,o),ItemList.Circuit_Chip_NOR.get(4,o),ItemList.Circuit_Chip_Ram.get(4,o),GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.YttriumBariumCuprate, 6)},tMat.getMolten(144L * tMultiplier), ItemList.Circuit_Wetwaresupercomputer.get(1,o), 400, 38400);
               
                //SoC
                //GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{ItemList.Circuit_Board_Epoxy.get(1,o),ItemList.Circuit_Chip_SoC.get(1,o),GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.RedAlloy, 4)},tMat.getMolten(144L * tMultiplier / 2L), ItemList.Circuit_Basic.get(1,o), 50, 600);
                //GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{ItemList.Circuit_Board_Epoxy.get(1,o),ItemList.Circuit_Chip_SoC.get(1,o),GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Electrum, 4)},tMat.getMolten(144L * tMultiplier / 2L), ItemList.Circuit_Processor.get(1,o), 50, 2400);
                //GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{ItemList.Circuit_Board_Fiberglass.get(1,o),ItemList.Circuit_Chip_SoC2.get(1,o),GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Platinum, 4)},tMat.getMolten(144L * tMultiplier / 2L), ItemList.Circuit_Nanoprocessor.get(1,o), 50, 9600);
                //GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{ItemList.Circuit_Board_Fiberglass.get(1,o),ItemList.Circuit_Chip_SoC2.get(1,o),GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.NiobiumTitanium, 4)},tMat.getMolten(144L * tMultiplier / 2L), ItemList.Circuit_Quantumprocessor.get(1,o), 50, 38400);
                //GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{ItemList.Circuit_Board_Multifiberglass.get(1,o),ItemList.Circuit_Chip_CrystalSoC.get(1,o),GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.YttriumBariumCuprate, 4)},tMat.getMolten(144L * tMultiplier / 2L), ItemList.Circuit_Crystalprocessor.get(1,o), 50, 153600);
            
                //Lapoorbs
                //GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{ItemList.Circuit_Board_Multifiberglass.get(1,o),ItemList.Circuit_Chip_PIC.get(4,o), ItemList.Circuit_Parts_Crystal_Chip_Master.get(18L,o),ItemList.Circuit_Chip_NanoCPU.get(1,o), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Platinum, 16)},tMat.getMolten(144L * tMultiplier), ItemList.Energy_LapotronicOrb.get(1,o), 512, 1024);
                //GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{ItemList.Circuit_Board_Multifiberglass.get(1,o),ItemList.Circuit_Chip_HPIC.get(4,o), ItemList.Energy_LapotronicOrb.get(8L,o),ItemList.Circuit_Chip_QuantumCPU.get(1,o), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Platinum, 16),GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Europium, 4L)},tMat.getMolten(144L * tMultiplier), ItemList.Energy_LapotronicOrb2.get(1,o), 1024, 4096);
                
				//Wetware Board
                //GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{ItemList.Circuit_Board_Multifiberglass.get(1, o), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Polytetrafluoroethylene, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Silicone, 2), ItemList.Electric_Pump_IV.get(1, o), ItemList.Bottle_Dragon_Blood.get(4, o), GT_OreDictUnificator.get(OrePrefixes.foil, Materials.NiobiumTitanium, 16)}, tMat.getMolten(144L * tMultiplier / 2L), ItemList.Circuit_Board_Wetware.get(1, o), 600, 1920, true);

                //OLD RECIPES
                //GT_Values.RA.addAssemblerRecipe(ItemList.IC2_Item_Casing_Steel.get(1L), GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.RedAlloy, 2L), tMat.getMolten(144L * tMultiplier / 8L), ItemList.Circuit_Primitive.get(1L), 16, 8);
                //GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Plastic, 1L), GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.RedAlloy, 1L), tMat.getMolten(144L * tMultiplier / 8L), ItemList.Circuit_Primitive.get(1L), 16, 8);
                //GT_Values.RA.addAssemblerRecipe(ItemList.Circuit_Board_Basic.get(1L), ItemList.Circuit_Primitive.get(2L), tMat.getMolten(144L * tMultiplier / 4L), ItemList.Circuit_Basic.get(1L), 32, 16);
                //GT_Values.RA.addAssemblerRecipe(ItemList.Circuit_Board_Basic.get(1L), ItemList.Circuit_Basic.get(1L), tMat.getMolten(144L * tMultiplier / 4L), ItemList.Circuit_Good.get(1L), 32, 16);
                //GT_Values.RA.addAssemblerRecipe(ItemList.Circuit_Board_Advanced.get(1L), ItemList.Circuit_Parts_Advanced.get(2L), tMat.getMolten(144L * tMultiplier / 2L), ItemList.Circuit_Advanced.get(1L), 32, 64);
                //GT_Values.RA.addAssemblerRecipe(ItemList.Circuit_Board_Advanced.get(1L), ItemList.Circuit_Parts_Crystal_Chip_Elite.get(1L), tMat.getMolten(144L * tMultiplier / 2L), ItemList.Circuit_Data.get(1L), 32, 64);
                //GT_Values.RA.addAssemblerRecipe(ItemList.Circuit_Board_Elite.get(1L), ItemList.Circuit_Data.get(3L), tMat.getMolten(144L * tMultiplier / 1L), ItemList.Circuit_Elite.get(1L), 32, 256);
                //GT_Values.RA.addAssemblerRecipe(ItemList.Circuit_Board_Elite.get(1L), ItemList.Circuit_Parts_Crystal_Chip_Master.get(3L), tMat.getMolten(144L * tMultiplier / 1L), ItemList.Circuit_Master.get(1L), 32, 256);
                //GT_Values.RA.addAssemblerRecipe(ItemList.Circuit_Data.get(1L), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Plastic, 2L), tMat.getMolten(144L * tMultiplier / 2L), ItemList.Tool_DataStick.get(1L), 128, 64);

                for (ItemStack tPlate : new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 1L), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 1L), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Aluminium, 1L)}) {
                    GT_Values.RA.addAssemblerRecipe(new ItemStack(Blocks.lever, 1, 32767), tPlate, tMat.getMolten(144L * tMultiplier / 2L), ItemList.Cover_Controller.get(1L, new Object[0]), 800, 16);
                    GT_Values.RA.addAssemblerRecipe(new ItemStack(Blocks.redstone_torch, 1, 32767), tPlate, tMat.getMolten(144L * tMultiplier / 2L), ItemList.Cover_ActivityDetector.get(1L, new Object[0]), 800, 16);
                    GT_Values.RA.addAssemblerRecipe(new ItemStack(Blocks.heavy_weighted_pressure_plate, 1, 32767), tPlate, tMat.getMolten(144L * tMultiplier / 2L), ItemList.Cover_FluidDetector.get(1L, new Object[0]), 800, 16);
                    GT_Values.RA.addAssemblerRecipe(new ItemStack(Blocks.light_weighted_pressure_plate, 1, 32767), tPlate, tMat.getMolten(144L * tMultiplier / 2L), ItemList.Cover_ItemDetector.get(1L, new Object[0]), 800, 16);
                    GT_Values.RA.addAssemblerRecipe(GT_ModHandler.getIC2Item("ecMeter", 1L), tPlate, tMat.getMolten(144L * tMultiplier / 2L), ItemList.Cover_EnergyDetector.get(1L, new Object[0]), 800, 16);
                }
            }
        }
        GT_Values.RA.addBlastRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 32), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.GalliumArsenide, 1), null, null, ItemList.Circuit_Silicon_Ingot.get(1, new Object[0]), null, 9000, 120, 1784);
        GT_Values.RA.addCutterRecipe(ItemList.Circuit_Silicon_Ingot.get(1, new Object[0]), ItemList.Circuit_Silicon_Wafer.get(16, new Object[0]), null, 400, 30);
        GT_Values.RA.addBlastRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 64), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glowstone, 8), Materials.Nitrogen.getGas(8000), null, ItemList.Circuit_Silicon_Ingot2.get(1, new Object[0]), null, 12000, 480, 2484);
        GT_Values.RA.addCutterRecipe(ItemList.Circuit_Silicon_Ingot2.get(1, new Object[0]), ItemList.Circuit_Silicon_Wafer2.get(32, new Object[0]), null, 800, 120, true);
        GT_Values.RA.addBlastRecipe(GT_OreDictUnificator.get(OrePrefixes.block, Materials.Silicon, 16), GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Naquadah, 1), Materials.Argon.getGas(8000), null, ItemList.Circuit_Silicon_Ingot3.get(1, new Object[0]), null, 15000, 1920, 4484);
        GT_Values.RA.addCutterRecipe(ItemList.Circuit_Silicon_Ingot3.get(1, new Object[0]), ItemList.Circuit_Silicon_Wafer3.get(64, new Object[0]), null, 1600, 480, true);

        GT_Values.RA.addAssemblerRecipe(new ItemStack(Blocks.redstone_torch, 2, 32767), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L), Materials.Concrete.getMolten(144L), new ItemStack(Items.repeater, 1, 0), 80, 10);
        GT_Values.RA.addAssemblerRecipe(new ItemStack(Items.leather, 1, 32767), new ItemStack(Items.lead, 1, 32767), Materials.Glue.getFluid(50L), new ItemStack(Items.name_tag, 1, 0), 100, 8);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Paper, 8L), new ItemStack(Items.compass, 1, 32767), GT_Values.NF, new ItemStack(Items.map, 1, 0), 100, 8);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Tantalum, 1L), GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Manganese, 1L), Materials.Plastic.getMolten(144L), ItemList.Battery_RE_ULV_Tantalum.get(8L, new Object[0]), 100, 4);
        //GT_Values.RA.addAssemblerRecipe(ItemList.Circuit_Elite.get(2L), ItemList.Circuit_Parts_Crystal_Chip_Elite.get(18L), GT_Values.NF, ItemList.Tool_DataOrb.get(1L), 512, 256);
        //GT_Values.RA.addAssemblerRecipe(ItemList.Circuit_Master.get(2L), ItemList.Circuit_Parts_Crystal_Chip_Master.get(18L), GT_Values.NF, ItemList.Energy_LapotronicOrb.get(1L), 512, 1024);
        //GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Europium, 4L), ItemList.Energy_LapotronicOrb.get(8L), GT_Values.NF, ItemList.Energy_LapotronicOrb2.get(1L), 2048, 4096);
        //GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 16L), ItemList.Energy_LapotronicOrb2.get(8L), GT_Values.NF, ItemList.ZPM2.get(1L), 32768, 4096);
        GT_Values.RA.addAssemblerRecipe(GT_ModHandler.getModItem("TwilightForest", "item.charmOfLife1", 4L, 0), ItemList.Circuit_Integrated.getWithDamage(0L, 4L, new Object[0]), GT_Values.NF, GT_ModHandler.getModItem("TwilightForest", "item.charmOfLife2", 1L, 0), 100, 8);
        GT_Values.RA.addAssemblerRecipe(GT_ModHandler.getModItem("TwilightForest", "item.charmOfKeeping1", 4L, 0), ItemList.Circuit_Integrated.getWithDamage(0L, 4L, new Object[0]), GT_Values.NF, GT_ModHandler.getModItem("TwilightForest", "item.charmOfKeeping2", 1L, 0), 100, 8);
        GT_Values.RA.addAssemblerRecipe(GT_ModHandler.getModItem("TwilightForest", "item.charmOfKeeping2", 4L, 0), ItemList.Circuit_Integrated.getWithDamage(0L, 4L, new Object[0]), GT_Values.NF, GT_ModHandler.getModItem("TwilightForest", "item.charmOfKeeping3", 1L, 0), 100, 8);
        GT_Values.RA.addAssemblerRecipe(GT_ModHandler.getModItem("TwilightForest", "item.charmOfLife2", 1L, 0), ItemList.Circuit_Integrated.getWithDamage(0L, 1L, new Object[0]), GT_Values.NF, GT_ModHandler.getModItem("TwilightForest", "item.charmOfLife1", 4L, 0), 100, 8);
        GT_Values.RA.addAssemblerRecipe(GT_ModHandler.getModItem("TwilightForest", "item.charmOfKeeping2", 1L, 0), ItemList.Circuit_Integrated.getWithDamage(0L, 1L, new Object[0]), GT_Values.NF, GT_ModHandler.getModItem("TwilightForest", "item.charmOfKeeping1", 4L, 0), 100, 8);
        GT_Values.RA.addAssemblerRecipe(GT_ModHandler.getModItem("TwilightForest", "item.charmOfKeeping3", 1L, 0), ItemList.Circuit_Integrated.getWithDamage(0L, 1L, new Object[0]), GT_Values.NF, GT_ModHandler.getModItem("TwilightForest", "item.charmOfKeeping2", 4L, 0), 100, 8);
        GT_Values.RA.addAssemblerRecipe(GT_ModHandler.getModItem(aTextAE, aTextAEMM, 1L, 16), GT_ModHandler.getModItem(aTextAE, aTextAEMM, 1L, 20), Materials.Redstone.getMolten(144L), GT_ModHandler.getModItem(aTextAE, aTextAEMM, 1L, 23), 64, 30);
        GT_Values.RA.addAssemblerRecipe(GT_ModHandler.getModItem(aTextAE, aTextAEMM, 1L, 17), GT_ModHandler.getModItem(aTextAE, aTextAEMM, 1L, 20), Materials.Redstone.getMolten(144L), GT_ModHandler.getModItem(aTextAE, aTextAEMM, 1L, 24), 64, 30);
        GT_Values.RA.addAssemblerRecipe(GT_ModHandler.getModItem(aTextAE, aTextAEMM, 1L, 18), GT_ModHandler.getModItem(aTextAE, aTextAEMM, 1L, 20), Materials.Redstone.getMolten(144L), GT_ModHandler.getModItem(aTextAE, aTextAEMM, 1L, 22), 64, 30);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.CertusQuartz, 1L), new ItemStack(Blocks.sand, 1, 32767), GT_Values.NF, GT_ModHandler.getModItem(aTextAE, "item.ItemCrystalSeed", 2L, 0), 64, 8);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.NetherQuartz, 1L), new ItemStack(Blocks.sand, 1, 32767), GT_Values.NF, GT_ModHandler.getModItem(aTextAE, "item.ItemCrystalSeed", 2L, 600), 64, 8);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Fluix, 1L), new ItemStack(Blocks.sand, 1, 32767), GT_Values.NF, GT_ModHandler.getModItem(aTextAE, "item.ItemCrystalSeed", 2L, 1200), 64, 8);
        GT_Values.RA.addAssemblerRecipe(ItemList.FR_Wax.get(6L, new Object[0]), new ItemStack(Items.string, 1, 32767), Materials.Water.getFluid(600L), GT_ModHandler.getModItem(aTextForestry, "candle", 24L, 0), 64, 8);
        GT_Values.RA.addAssemblerRecipe(ItemList.FR_Wax.get(2L, new Object[0]), ItemList.FR_Silk.get(1L, new Object[0]), Materials.Water.getFluid(200L), GT_ModHandler.getModItem(aTextForestry, "candle", 8L, 0), 16, 8);
        GT_Values.RA.addAssemblerRecipe(ItemList.FR_Silk.get(9L, new Object[0]), ItemList.Circuit_Integrated.getWithDamage(0L, 9L, new Object[0]), Materials.Water.getFluid(500L), GT_ModHandler.getModItem(aTextForestry, "craftingMaterial", 1L, 3), 64, 8);
        GT_Values.RA.addAssemblerRecipe(GT_ModHandler.getModItem(aTextForestry, "propolis", 5L, 2), ItemList.Circuit_Integrated.getWithDamage(0L, 5L, new Object[0]), GT_Values.NF, GT_ModHandler.getModItem(aTextForestry, "craftingMaterial", 1L, 1), 16, 8);
        GT_Values.RA.addAssemblerRecipe(GT_ModHandler.getModItem(aTextForestry, "sturdyMachine", 1L, 0), GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Diamond, 4L), Materials.Water.getFluid(5000L), ItemList.FR_Casing_Hardened.get(1L, new Object[0]), 64, 32);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Bronze, 8L), ItemList.Circuit_Integrated.getWithDamage(0L, 8L, new Object[0]), GT_Values.NF, ItemList.FR_Casing_Sturdy.get(1L, new Object[0]), 32, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Tin, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 6L), Materials.Water.getFluid(1000L), GT_ModHandler.getModItem(aTextForestry, "chipsets", 1L, 0), 16, 8);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Bronze, 3L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 6L), Materials.Water.getFluid(1000L), GT_ModHandler.getModItem(aTextForestry, "chipsets", 1L, 1), 32, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, 3L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 6L), Materials.Water.getFluid(1000L), GT_ModHandler.getModItem(aTextForestry, "chipsets", 1L, 2), 48, 24);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.WroughtIron, 3L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 6L), Materials.Water.getFluid(1000L), GT_ModHandler.getModItem(aTextForestry, "chipsets", 1L, 2), 48, 24);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Gold, 3L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 6L), Materials.Water.getFluid(1000L), GT_ModHandler.getModItem(aTextForestry, "chipsets", 1L, 3), 64, 32);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1L), new ItemStack(Blocks.wool, 1, 32767), Materials.Creosote.getFluid(1000L), new ItemStack(Blocks.torch, 6, 0), 400, 1);
        GT_Values.RA.addAssemblerRecipe(GT_ModHandler.getModItem(aTextForestry, "craftingMaterial", 5L, 1), ItemList.Circuit_Integrated.getWithDamage(0L, 5L, new Object[0]), GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.gem, Materials.EnderPearl, 1L), 64, 8);
        GT_Values.RA.addAssemblerRecipe(new ItemStack(Blocks.piston, 1, 32767), new ItemStack(Items.slime_ball, 1, 32767), GT_Values.NF, new ItemStack(Blocks.sticky_piston, 1, 0), 100, 4);
        GT_Values.RA.addAssemblerRecipe(new ItemStack(Blocks.piston, 1, 32767), ItemList.IC2_Resin.get(1L, new Object[0]), GT_Values.NF, new ItemStack(Blocks.sticky_piston, 1, 0), 100, 4);
        GT_Values.RA.addAssemblerRecipe(new ItemStack(Blocks.piston, 1, 32767), ItemList.Circuit_Integrated.getWithDamage(0L, 1L, new Object[0]), Materials.Glue.getFluid(100L), new ItemStack(Blocks.sticky_piston, 1, 0), 100, 4);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Rubber, 3L), GT_ModHandler.getIC2Item("carbonMesh", 3L), Materials.Glue.getFluid(300L), ItemList.Duct_Tape.get(1L, new Object[0]), 100, 64);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Paper, 3L), new ItemStack(Items.leather, 1, 32767), Materials.Glue.getFluid(20L), new ItemStack(Items.book, 1, 0), 32, 8);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Paper, 3L), GT_OreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.Paper, 1L), Materials.Glue.getFluid(20L), new ItemStack(Items.book, 1, 0), 32, 8);
        GT_Values.RA.addAssemblerRecipe(ItemList.Paper_Printed_Pages.get(1L, new Object[0]), new ItemStack(Items.leather, 1, 32767), Materials.Glue.getFluid(20L), new ItemStack(Items.written_book, 1, 0), 32, 8);
        GT_Values.RA.addAssemblerRecipe(ItemList.IC2_Item_Casing_Tin.get(4L, new Object[0]), new ItemStack(Blocks.glass_pane, 1, 32767), GT_Values.NF, ItemList.Cell_Universal_Fluid.get(1L, new Object[0]), 128, 8);
        GT_Values.RA.addAssemblerRecipe(ItemList.Food_Baked_Cake.get(1L, new Object[0]), new ItemStack(Items.egg, 1, 0), Materials.Milk.getFluid(3000L), new ItemStack(Items.cake, 1, 0), 100, 8);
        GT_Values.RA.addAssemblerRecipe(ItemList.Food_Sliced_Bun.get(2L, new Object[0]), ItemList.Circuit_Integrated.getWithDamage(0L, 2L, new Object[0]), GT_Values.NF, ItemList.Food_Sliced_Buns.get(1L, new Object[0]), 100, 4);
        GT_Values.RA.addAssemblerRecipe(ItemList.Food_Sliced_Bread.get(2L, new Object[0]), ItemList.Circuit_Integrated.getWithDamage(0L, 2L, new Object[0]), GT_Values.NF, ItemList.Food_Sliced_Breads.get(1L, new Object[0]), 100, 4);
        GT_Values.RA.addAssemblerRecipe(ItemList.Food_Sliced_Baguette.get(2L, new Object[0]), ItemList.Circuit_Integrated.getWithDamage(0L, 2L, new Object[0]), GT_Values.NF, ItemList.Food_Sliced_Baguettes.get(1L, new Object[0]), 100, 4);
        GT_Values.RA.addAssemblerRecipe(ItemList.Food_Sliced_Buns.get(1L, new Object[0]), ItemList.Circuit_Integrated.getWithDamage(0L, 1L, new Object[0]), GT_Values.NF, ItemList.Food_Sliced_Bun.get(2L, new Object[0]), 100, 4);
        GT_Values.RA.addAssemblerRecipe(ItemList.Food_Sliced_Breads.get(1L, new Object[0]), ItemList.Circuit_Integrated.getWithDamage(0L, 1L, new Object[0]), GT_Values.NF, ItemList.Food_Sliced_Bread.get(2L, new Object[0]), 100, 4);
        GT_Values.RA.addAssemblerRecipe(ItemList.Food_Sliced_Baguettes.get(1L, new Object[0]), ItemList.Circuit_Integrated.getWithDamage(0L, 1L, new Object[0]), GT_Values.NF, ItemList.Food_Sliced_Baguette.get(2L, new Object[0]), 100, 4);
        GT_Values.RA.addAssemblerRecipe(ItemList.Food_Sliced_Bun.get(2L, new Object[0]), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.MeatCooked, 1L), GT_Values.NF, ItemList.Food_Burger_Meat.get(1L, new Object[0]), 100, 4);
        GT_Values.RA.addAssemblerRecipe(ItemList.Food_Sliced_Buns.get(1L, new Object[0]), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.MeatCooked, 1L), GT_Values.NF, ItemList.Food_Burger_Meat.get(1L, new Object[0]), 100, 4);
        GT_Values.RA.addAssemblerRecipe(ItemList.Food_Sliced_Bun.get(2L, new Object[0]), ItemList.Food_Chum.get(1L, new Object[0]), GT_Values.NF, ItemList.Food_Burger_Chum.get(1L, new Object[0]), 100, 4);
        GT_Values.RA.addAssemblerRecipe(ItemList.Food_Sliced_Buns.get(1L, new Object[0]), ItemList.Food_Chum.get(1L, new Object[0]), GT_Values.NF, ItemList.Food_Burger_Chum.get(1L, new Object[0]), 100, 4);
        GT_Values.RA.addAssemblerRecipe(ItemList.Food_Sliced_Bun.get(2L, new Object[0]), ItemList.Food_Sliced_Cheese.get(3L, new Object[0]), GT_Values.NF, ItemList.Food_Burger_Cheese.get(1L, new Object[0]), 100, 4);
        GT_Values.RA.addAssemblerRecipe(ItemList.Food_Sliced_Buns.get(1L, new Object[0]), ItemList.Food_Sliced_Cheese.get(3L, new Object[0]), GT_Values.NF, ItemList.Food_Burger_Cheese.get(1L, new Object[0]), 100, 4);
        GT_Values.RA.addAssemblerRecipe(ItemList.Food_Flat_Dough.get(1L, new Object[0]), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.MeatCooked, 1L), GT_Values.NF, ItemList.Food_Raw_Pizza_Meat.get(1L, new Object[0]), 100, 4);
        GT_Values.RA.addAssemblerRecipe(ItemList.Food_Flat_Dough.get(1L, new Object[0]), ItemList.Food_Sliced_Cheese.get(3L, new Object[0]), GT_Values.NF, ItemList.Food_Raw_Pizza_Cheese.get(1L, new Object[0]), 100, 4);

        GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.plate, Materials.RedAlloy, 1L), GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Gold, 2L), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Copper, 2L), GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Copper, 2L)}, Materials.Glass.getMolten(576L), GT_ModHandler.getModItem(aTextForestry, "thermionicTubes", 4L, 0), 200, 480);
        GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.plate, Materials.RedAlloy, 1L), GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Gold, 2L), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Copper, 2L), GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.AnnealedCopper, 2L)}, Materials.Glass.getMolten(576L), GT_ModHandler.getModItem(aTextForestry, "thermionicTubes", 4L, 0), 480, 480);
        GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.plate, Materials.RedAlloy, 1L), GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Gold, 2L), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Copper, 2L), GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Tin, 2L)}, Materials.Glass.getMolten(576L), GT_ModHandler.getModItem(aTextForestry, "thermionicTubes", 4L, 1), 200, 480);
        GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.plate, Materials.RedAlloy, 1L), GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Gold, 2L), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Copper, 2L), GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Bronze, 2L)}, Materials.Glass.getMolten(576L), GT_ModHandler.getModItem(aTextForestry, "thermionicTubes", 4L, 2), 200, 480);
        GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.plate, Materials.RedAlloy, 1L), GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Gold, 2L), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Copper, 2L), GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Iron, 2L)}, Materials.Glass.getMolten(576L), GT_ModHandler.getModItem(aTextForestry, "thermionicTubes", 4L, 3), 200, 120);
        GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.plate, Materials.RedAlloy, 1L), GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Gold, 2L), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Copper, 2L), GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.WroughtIron, 2L)}, Materials.Glass.getMolten(576L), GT_ModHandler.getModItem(aTextForestry, "thermionicTubes", 4L, 3), 200, 480);
        GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.plate, Materials.RedAlloy, 1L), GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Gold, 2L), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Copper, 2L), GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Gold, 2L)}, Materials.Glass.getMolten(576L), GT_ModHandler.getModItem(aTextForestry, "thermionicTubes", 4L, 4), 200, 480);
        GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.plate, Materials.RedAlloy, 1L), GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Gold, 2L), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Copper, 2L), GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Diamond, 2L)}, Materials.Glass.getMolten(576L), GT_ModHandler.getModItem(aTextForestry, "thermionicTubes", 4L, 5), 200, 480);
        GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.plate, Materials.RedAlloy, 1L), GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Gold, 2L), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Copper, 2L), GT_ModHandler.getModItem("dreamcraft", "item.LongObsidianRod", 2L, 0)}, Materials.Glass.getMolten(576L), GT_ModHandler.getModItem(aTextForestry, "thermionicTubes", 4L, 6), 200, 480);
        GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.plate, Materials.RedAlloy, 1L), GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Gold, 2L), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Copper, 2L), GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Blaze, 2L)}, Materials.Glass.getMolten(576L), GT_ModHandler.getModItem(aTextForestry, "thermionicTubes", 4L, 7), 200, 480);
        GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.plate, Materials.RedAlloy, 1L), GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Gold, 2L), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Copper, 2L), GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Rubber, 2L)}, Materials.Glass.getMolten(576L), GT_ModHandler.getModItem(aTextForestry, "thermionicTubes", 4L, 8), 200, 480);
        GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.plate, Materials.RedAlloy, 1L), GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Gold, 2L), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Copper, 2L), GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Emerald, 2L)}, Materials.Glass.getMolten(576L), GT_ModHandler.getModItem(aTextForestry, "thermionicTubes", 4L, 9), 200, 480);
        GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.plate, Materials.RedAlloy, 1L), GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Gold, 2L), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Copper, 2L), GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Apatite, 2L)}, Materials.Glass.getMolten(576L), GT_ModHandler.getModItem(aTextForestry, "thermionicTubes", 4L, 10), 200, 480);
        GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.plate, Materials.RedAlloy, 1L), GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Gold, 2L), GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Copper, 2L), GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Lapis, 2L)}, Materials.Glass.getMolten(576L), GT_ModHandler.getModItem(aTextForestry, "thermionicTubes", 4L, 11), 200, 480);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Aluminium, 2L), new ItemStack(Items.iron_door, 1), ItemList.Cover_Shutter.get(2L, new Object[0]), 800, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 2L), new ItemStack(Items.iron_door, 1), ItemList.Cover_Shutter.get(2L, new Object[0]), 800, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 2L), new ItemStack(Items.iron_door, 1), ItemList.Cover_Shutter.get(2L, new Object[0]), 800, 16);

        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Pentacadmiummagnesiumhexaoxid, 3L), GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.StainlessSteel, 2L), ItemList.Electric_Pump_HV.get(2L, new Object[0]), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Nitrogen, 1L), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Helium, 1L), GT_Utility.getIntegratedCircuit(9)}, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorMV, 3L), 300, 120);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Titaniumonabariumdecacoppereikosaoxid, 6L), GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Titanium, 4L), ItemList.Electric_Pump_EV.get(2L, new Object[0]), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Nitrogen, 2L), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Helium, 2L), GT_Utility.getIntegratedCircuit(9)}, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorHV, 6L), 400, 256);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Uraniumtriplatinid, 9L), GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.TungstenSteel, 6L), ItemList.Electric_Pump_IV.get(2L, new Object[0]), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Nitrogen, 3L), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Helium, 3L), GT_Utility.getIntegratedCircuit(9)}, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorEV, 9L), 500, 480);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Vanadiumtriindinid, 12L), GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.NiobiumTitanium, 8L), ItemList.Electric_Pump_IV.get(2L, new Object[0]), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Nitrogen, 4L), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Helium, 4L), GT_Utility.getIntegratedCircuit(9)}, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorIV, 12L), 600, 1920);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid, 16L), GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Enderium, 10L), ItemList.Electric_Pump_LuV.get(2L, new Object[0]), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Nitrogen, 6L), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Helium, 6L), GT_Utility.getIntegratedCircuit(9)}, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 16L), 700, 7680);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Tetranaquadahdiindiumhexaplatiumosminid, 32L), GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Naquadah, 12L), ItemList.Electric_Pump_ZPM.get(2L, new Object[0]), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Nitrogen, 8L), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Helium, 8L), GT_Utility.getIntegratedCircuit(9)}, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 32L), 800, 30720);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 64L), GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Neutronium, 14L), ItemList.Electric_Pump_ZPM.get(2L, new Object[0]), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Nitrogen, 10L), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Helium, 10L), GT_Utility.getIntegratedCircuit(9)}, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Superconductor, 64L), 1000, 122880);

        GT_Values.RA.addUniversalDistillationRecipe(Materials.OilLight.getFluid(150), new FluidStack[]{ Materials.SulfuricHeavyFuel.getFluid(10),  Materials.SulfuricLightFuel.getFluid(20), Materials.SulfuricNaphtha.getFluid(30), Materials.SulfuricGas.getGas(240)}, null, 20, 96);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.OilMedium.getFluid(100), new FluidStack[]{Materials.SulfuricHeavyFuel.getFluid(15),  Materials.SulfuricLightFuel.getFluid(50), Materials.SulfuricNaphtha.getFluid(20), Materials.SulfuricGas.getGas(60)}, null, 20, 96);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Oil.getFluid(50L), new FluidStack[]{      Materials.SulfuricHeavyFuel.getFluid(15),  Materials.SulfuricLightFuel.getFluid(50), Materials.SulfuricNaphtha.getFluid(20), Materials.SulfuricGas.getGas(60)}, null, 20, 96);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.OilHeavy.getFluid(100), new FluidStack[]{ Materials.SulfuricHeavyFuel.getFluid(250), Materials.SulfuricLightFuel.getFluid(45), Materials.SulfuricNaphtha.getFluid(15), Materials.SulfuricGas.getGas(60)}, null, 20, 288);

        //TODO ADDED?
        GT_Values.RA.addUniversalDistillationRecipe(Materials.OilLight.getFluid(150), new FluidStack[]{Materials.SulfuricGas.getGas(240), Materials.SulfuricNaphtha.getFluid(30), Materials.SulfuricLightFuel.getFluid(20), Materials.SulfuricHeavyFuel.getFluid(10)}, null, 32, 64);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.OilMedium.getFluid(100), new FluidStack[]{Materials.SulfuricGas.getGas(60), Materials.SulfuricNaphtha.getFluid(20), Materials.SulfuricLightFuel.getFluid(50), Materials.SulfuricHeavyFuel.getFluid(15)}, null, 32, 64);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Oil.getFluid(50L), new FluidStack[]{Materials.SulfuricGas.getGas(60), Materials.SulfuricNaphtha.getFluid(20), Materials.SulfuricLightFuel.getFluid(50), Materials.SulfuricHeavyFuel.getFluid(15)}, null, 32, 64);

         if (GregTech_API.sSpecialFile.get("general", "EnableLagencyOilGalactiCraft", false) && FluidRegistry.getFluid("oilgc") != null) {
            GT_Values.RA.addUniversalDistillationRecipe(new FluidStack(FluidRegistry.getFluid("oilgc"), 50), new FluidStack[]{Materials.SulfuricHeavyFuel.getFluid(15), Materials.SulfuricLightFuel.getFluid(50), Materials.SulfuricNaphtha.getFluid(20), Materials.SulfuricGas.getGas(60)}, null, 20, 96);
        }
        GT_Values.RA.addDistilleryRecipe(ItemList.Circuit_Integrated.getWithDamage(0L, 1L, new Object[0]), new FluidStack(ItemList.sOilExtraHeavy,10), Materials.OilHeavy.getFluid(15), 16, 24, false);
        GT_Values.RA.addDistilleryRecipe(ItemList.Circuit_Integrated.getWithDamage(0L, 1L, new Object[0]), Materials.HeavyFuel.getFluid(10L), new FluidStack(ItemList.sToluene,4), 16, 24, false);
        GT_Values.RA.addDistilleryRecipe(ItemList.Circuit_Integrated.getWithDamage(0L, 1L, new Object[0]), new FluidStack(ItemList.sToluene,30), Materials.LightFuel.getFluid(30L), 16, 24, false);
        GT_Values.RA.addChemicalRecipe(ItemList.GelledToluene.get(4, new Object[0]), GT_Utility.getIntegratedCircuit(1), Materials.SulfuricAcid.getFluid(250), GT_Values.NF, new ItemStack(Blocks.tnt, 1), 200, 24);

        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Ball.get(0L, new Object[0]), Materials.Glass.getMolten(144), ItemList.Circuit_Parts_Glass_Tube.get(1, new Object[0]), 200, 24);
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Ball.get(0L, new Object[0]), FluidRegistry.getFluidStack("glass.molten", 1000), ItemList.Circuit_Parts_Glass_Tube.get(1, new Object[0]), 200, 24);
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Ball.get(0L, new Object[0]), new FluidStack(ItemList.sToluene, 100), ItemList.GelledToluene.get(1, new Object[0]), 100, 16);

        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Nugget.get(0L, new Object[0]), Materials.AnnealedCopper.getMolten(16), GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Copper, 1L), 16, 4);
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Ingot.get(0L, new Object[0]), Materials.AnnealedCopper.getMolten(144), GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Copper, 1L), 32, 8);
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Block.get(0L, new Object[0]), Materials.AnnealedCopper.getMolten(1296), GT_OreDictUnificator.get(OrePrefixes.block, Materials.Copper, 1L), 288, 8);

        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Nugget.get(0L, new Object[0]), Materials.WroughtIron.getMolten(16), GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Iron, 1L), 16, 4);
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Ingot.get(0L, new Object[0]), Materials.WroughtIron.getMolten(144), GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, 1L), 32, 8);
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Block.get(0L, new Object[0]), Materials.WroughtIron.getMolten(1296), GT_OreDictUnificator.get(OrePrefixes.block, Materials.Iron, 1L), 288, 8);

        GT_Values.RA.addChemicalRecipe(new ItemStack(Items.sugar), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Plastic, 1), new FluidStack(ItemList.sToluene, 133), GT_Values.NF, ItemList.GelledToluene.get(2, new Object[0]), 140, 192);

        GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.cell,Materials.SulfuricAcid, 1),  GT_Values.NI,  GT_Values.NI,  GT_Values.NI,  GT_Values.NI, GT_Utility.getIntegratedCircuit(1), new FluidStack(ItemList.sNitricAcid,1000), new FluidStack(ItemList.sNitrationMixture, 2000), ItemList.Cell_Empty.get(1, new Object[0]), 480, 2);
        GT_Values.RA.addChemicalRecipe(ItemList.GelledToluene.get(4, new Object[0]), GT_Utility.getIntegratedCircuit(1), new FluidStack(ItemList.sNitrationMixture,200), Materials.DilutedSulfuricAcid.getFluid(200), GT_ModHandler.getIC2Item("industrialTnt", 1L), 80, 480);

        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 2L),           GT_Utility.getIntegratedCircuit(4), Materials.NatruralGas.getGas(16000),         Materials.Gas.getGas(16000),          GT_OreDictUnificator.get(OrePrefixes.cell, Materials.HydricSulfide, 1L), Materials.Empty.getCells(1), 160);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.NatruralGas, 16L),       GT_Utility.getIntegratedCircuit(4), Materials.Hydrogen.getGas(2000),             Materials.HydricSulfide.getGas(1000), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Gas, 16L), 160);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 2L),           GT_Utility.getIntegratedCircuit(4), Materials.SulfuricGas.getGas(16000),         Materials.Gas.getGas(16000),          GT_OreDictUnificator.get(OrePrefixes.cell, Materials.HydricSulfide, 1L), Materials.Empty.getCells(1), 160);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.SulfuricGas, 16L),       GT_Utility.getIntegratedCircuit(4), Materials.Hydrogen.getGas(2000),             Materials.HydricSulfide.getGas(1000), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Gas, 16L), 160);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 2L),           GT_Utility.getIntegratedCircuit(4), Materials.SulfuricNaphtha.getFluid(12000),   Materials.Naphtha.getFluid(12000),    GT_OreDictUnificator.get(OrePrefixes.cell, Materials.HydricSulfide, 1L), Materials.Empty.getCells(1), 160);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.SulfuricNaphtha, 12L),   GT_Utility.getIntegratedCircuit(4), Materials.Hydrogen.getGas(2000),             Materials.HydricSulfide.getGas(1000), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Naphtha, 12L), 160);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 2L),           GT_Utility.getIntegratedCircuit(4), Materials.SulfuricLightFuel.getFluid(12000), Materials.LightFuel.getFluid(12000),  GT_OreDictUnificator.get(OrePrefixes.cell, Materials.HydricSulfide, 1L), Materials.Empty.getCells(1), 160);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.SulfuricLightFuel, 12L), GT_Utility.getIntegratedCircuit(4), Materials.Hydrogen.getGas(2000),             Materials.HydricSulfide.getGas(1000), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.LightFuel, 12L), 160);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 2L),           GT_Utility.getIntegratedCircuit(4), Materials.SulfuricHeavyFuel.getFluid(8000),  Materials.HeavyFuel.getFluid(8000),   GT_OreDictUnificator.get(OrePrefixes.cell, Materials.HydricSulfide, 1L), Materials.Empty.getCells(1), 160);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.SulfuricHeavyFuel, 8L),  GT_Utility.getIntegratedCircuit(4), Materials.Hydrogen.getGas(2000),             Materials.HydricSulfide.getGas(1000), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.HeavyFuel, 8L), 160);

        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Saltpeter, 1L), GT_Utility.getIntegratedCircuit(1), Materials.Naphtha.getFluid(576), Materials.Polycaprolactam.getMolten(1296), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Potassium, 1), 640);
        GT_Values.RA.addWiremillRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Polycaprolactam, 1L), new ItemStack(Items.string, 32), 80, 48);

        GT_Values.RA.addWiremillRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.RedstoneAlloy, 1L), GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.RedstoneAlloy, 2L), 100, 4);
        GT_Values.RA.addWiremillRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.RedstoneAlloy, 1L), GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.RedstoneAlloy, 1L),  50, 4);

        GT_Values.RA.addDistilleryRecipe(ItemList.Circuit_Integrated.getWithDamage(0L, 4L, new Object[0]), Materials.Creosote.getFluid(100L), Materials.Lubricant.getFluid(32L), 240, 30, false);
        GT_Values.RA.addDistilleryRecipe(ItemList.Circuit_Integrated.getWithDamage(0L, 4L, new Object[0]), Materials.SeedOil.getFluid(32L), Materials.Lubricant.getFluid(8L), 80, 30, false);
        GT_Values.RA.addDistilleryRecipe(ItemList.Circuit_Integrated.getWithDamage(0L, 4L, new Object[0]), Materials.FishOil.getFluid(32L), Materials.Lubricant.getFluid(8L), 80, 30, false);
        GT_Values.RA.addDistilleryRecipe(ItemList.Circuit_Integrated.getWithDamage(0L, 4L, new Object[0]), Materials.Oil.getFluid(120L), Materials.Lubricant.getFluid(60L), 160, 30, false);
        GT_Values.RA.addDistilleryRecipe(ItemList.Circuit_Integrated.getWithDamage(0L, 1L, new Object[0]), Materials.Biomass.getFluid(40L), Materials.Ethanol.getFluid(12L), 16, 24, false);
        GT_Values.RA.addDistilleryRecipe(ItemList.Circuit_Integrated.getWithDamage(0L, 5L, new Object[0]), Materials.Biomass.getFluid(40L), Materials.Water.getFluid(12L), 16, 24, false);
        GT_Values.RA.addDistilleryRecipe(ItemList.Circuit_Integrated.getWithDamage(0L, 5L, new Object[0]), Materials.Water.getFluid(5L), GT_ModHandler.getDistilledWater(5L), 16, 10, false);
        GT_Values.RA.addDistilleryRecipe(ItemList.Circuit_Integrated.getWithDamage(0L, 1L, new Object[0]), FluidRegistry.getFluidStack("potion.potatojuice", 2), FluidRegistry.getFluidStack("potion.vodka", 1), 16, 16, true);
        GT_Values.RA.addDistilleryRecipe(ItemList.Circuit_Integrated.getWithDamage(0L, 1L, new Object[0]), FluidRegistry.getFluidStack("potion.lemonade", 2), FluidRegistry.getFluidStack("potion.alcopops", 1), 16, 16, true);

        GT_Values.RA.addDistilleryRecipe(ItemList.Circuit_Integrated.getWithDamage(0L, 4L, new Object[0]), Materials.OilLight.getFluid(300L), Materials.Oil.getFluid(100L), 16, 24, false);
        GT_Values.RA.addDistilleryRecipe(ItemList.Circuit_Integrated.getWithDamage(0L, 4L, new Object[0]), Materials.OilMedium.getFluid(200L), Materials.Oil.getFluid(100L), 16, 24, false);
        GT_Values.RA.addDistilleryRecipe(ItemList.Circuit_Integrated.getWithDamage(0L, 4L, new Object[0]), Materials.OilHeavy.getFluid(100L), Materials.Oil.getFluid(100L), 16, 24, false);

        if (Loader.isModLoaded("TConstruct")) {
            GT_Values.RA.addDistilleryRecipe(ItemList.Circuit_Integrated.getWithDamage(0L, 1L), Materials.Glue.getFluid(8L), FluidRegistry.getFluidStack("glue", 8), 1, 24, false);
            GT_Values.RA.addDistilleryRecipe(ItemList.Circuit_Integrated.getWithDamage(0L, 1L), FluidRegistry.getFluidStack("glue", 8), Materials.Glue.getFluid(4L), 1, 24, false);
        }

        GT_Values.RA.addFluidHeaterRecipe(ItemList.Circuit_Integrated.getWithDamage(0L, 1L, new Object[0]), Materials.Water.getFluid(6L), Materials.Water.getGas(960L), 30, 30);
        GT_Values.RA.addFluidHeaterRecipe(ItemList.Circuit_Integrated.getWithDamage(0L, 1L, new Object[0]), GT_ModHandler.getDistilledWater(6L), Materials.Water.getGas(960L), 30, 30);
        GT_Values.RA.addFluidHeaterRecipe(ItemList.Circuit_Integrated.getWithDamage(0L, 1L, new Object[0]), Materials.SeedOil.getFluid(16L), Materials.FryingOilHot.getFluid(16L), 16, 30);
        GT_Values.RA.addFluidHeaterRecipe(ItemList.Circuit_Integrated.getWithDamage(0L, 1L, new Object[0]), Materials.FishOil.getFluid(16L), Materials.FryingOilHot.getFluid(16L), 16, 30);

        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Talc, 1L), FluidRegistry.getFluid("oil"), FluidRegistry.getFluid("lubricant"), false);
        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Soapstone, 1L), FluidRegistry.getFluid("oil"), FluidRegistry.getFluid("lubricant"), false);
        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L), FluidRegistry.getFluid("oil"), FluidRegistry.getFluid("lubricant"), false);
        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Talc, 1L), FluidRegistry.getFluid("creosote"), FluidRegistry.getFluid("lubricant"), false);
        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Soapstone, 1L), FluidRegistry.getFluid("creosote"), FluidRegistry.getFluid("lubricant"), false);
        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L), FluidRegistry.getFluid("creosote"), FluidRegistry.getFluid("lubricant"), false);
        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Talc, 1L), FluidRegistry.getFluid("seedoil"), FluidRegistry.getFluid("lubricant"), false);
        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Soapstone, 1L), FluidRegistry.getFluid("seedoil"), FluidRegistry.getFluid("lubricant"), false);
        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L), FluidRegistry.getFluid("seedoil"), FluidRegistry.getFluid("lubricant"), false);
        for (Fluid tFluid : new Fluid[]{FluidRegistry.WATER, GT_ModHandler.getDistilledWater(1L).getFluid()}) {
            GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Milk, 1L), tFluid, FluidRegistry.getFluid("milk"), false);
            GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wheat, 1L), tFluid, FluidRegistry.getFluid("potion.wheatyjuice"), false);
            GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Potassium, 1L), tFluid, FluidRegistry.getFluid("potion.mineralwater"), false);
            GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L), tFluid, FluidRegistry.getFluid("potion.mineralwater"), false);
            GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 1L), tFluid, FluidRegistry.getFluid("potion.mineralwater"), false);
            GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnesium, 1L), tFluid, FluidRegistry.getFluid("potion.mineralwater"), false);
            //Disabled in favor of a different type of Salt Water
//            GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Salt, 1L), tFluid, FluidRegistry.getFluid("potion.saltywater"), true);
//            GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RockSalt, 1L), tFluid, FluidRegistry.getFluid("potion.saltywater"), true);
            GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glowstone, 1L), tFluid, FluidRegistry.getFluid("potion.thick"), false);
            GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L), tFluid, FluidRegistry.getFluid("potion.mundane"), false);
            GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L), tFluid, FluidRegistry.getFluid("potion.mundane"), false);
            GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Blaze, 1L), tFluid, FluidRegistry.getFluid("potion.mundane"), false);
            GT_Values.RA.addBrewingRecipe(new ItemStack(Items.magma_cream, 1, 0), tFluid, FluidRegistry.getFluid("potion.mundane"), false);
            GT_Values.RA.addBrewingRecipe(new ItemStack(Items.fermented_spider_eye, 1, 0), tFluid, FluidRegistry.getFluid("potion.mundane"), false);
            GT_Values.RA.addBrewingRecipe(new ItemStack(Items.spider_eye, 1, 0), tFluid, FluidRegistry.getFluid("potion.mundane"), false);
            GT_Values.RA.addBrewingRecipe(new ItemStack(Items.speckled_melon, 1, 0), tFluid, FluidRegistry.getFluid("potion.mundane"), false);
            GT_Values.RA.addBrewingRecipe(new ItemStack(Items.ghast_tear, 1, 0), tFluid, FluidRegistry.getFluid("potion.mundane"), false);
            GT_Values.RA.addBrewingRecipe(new ItemStack(Items.nether_wart, 1, 0), tFluid, FluidRegistry.getFluid("potion.awkward"), false);
            GT_Values.RA.addBrewingRecipe(new ItemStack(Blocks.red_mushroom, 1, 0), tFluid, FluidRegistry.getFluid("potion.poison"), false);
            GT_Values.RA.addBrewingRecipe(new ItemStack(Items.fish, 1, 3), tFluid, FluidRegistry.getFluid("potion.poison.strong"), true);
            GT_Values.RA.addBrewingRecipe(ItemList.IC2_Grin_Powder.get(1L, new Object[0]), tFluid, FluidRegistry.getFluid("potion.poison.strong"), false);
            GT_Values.RA.addBrewingRecipe(new ItemStack(Items.reeds, 1, 0), tFluid, FluidRegistry.getFluid("potion.reedwater"), false);
            GT_Values.RA.addBrewingRecipe(new ItemStack(Items.apple, 1, 0), tFluid, FluidRegistry.getFluid("potion.applejuice"), false);
            GT_Values.RA.addBrewingRecipe(new ItemStack(Items.golden_apple, 1, 0), tFluid, FluidRegistry.getFluid("potion.goldenapplejuice"), true);
            GT_Values.RA.addBrewingRecipe(new ItemStack(Items.golden_apple, 1, 1), tFluid, FluidRegistry.getFluid("potion.idunsapplejuice"), true);
            GT_Values.RA.addBrewingRecipe(ItemList.IC2_Hops.get(1L, new Object[0]), tFluid, FluidRegistry.getFluid("potion.hopsjuice"), false);
            GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coffee, 1L), tFluid, FluidRegistry.getFluid("potion.darkcoffee"), false);
            GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chili, 1L), tFluid, FluidRegistry.getFluid("potion.chillysauce"), false);

            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(2L, new Object[0]), 200);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Phosphorus, 1L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(3L, new Object[0]), 300);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Phosphate, 1L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(2L, new Object[0]), 200);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 3L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(1L, new Object[0]), 100);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.DarkAsh, 1L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(1L, new Object[0]), 100);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(3L, new Object[0]), 300);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Phosphorus, 1L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(4L, new Object[0]), 400);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Phosphate, 1L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(3L, new Object[0]), 300);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 3L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(2L, new Object[0]), 200);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.DarkAsh, 1L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(2L, new Object[0]), 200);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Apatite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(3L, new Object[0]), 300);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Apatite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Phosphorus, 1L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(4L, new Object[0]), 400);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Apatite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Phosphate, 1L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(3L, new Object[0]), 300);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Apatite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 3L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(2L, new Object[0]), 200);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Apatite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.DarkAsh, 1L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(2L, new Object[0]), 200);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glauconite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(3L, new Object[0]), 300);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glauconite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Phosphorus, 1L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(4L, new Object[0]), 400);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glauconite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Phosphate, 1L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(3L, new Object[0]), 300);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glauconite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 3L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(2L, new Object[0]), 200);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glauconite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.DarkAsh, 1L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(2L, new Object[0]), 200);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.GlauconiteSand, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(3L, new Object[0]), 300);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.GlauconiteSand, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Phosphorus, 1L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(4L, new Object[0]), 400);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.GlauconiteSand, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Phosphate, 1L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(3L, new Object[0]), 300);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.GlauconiteSand, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 3L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(2L, new Object[0]), 200);
            GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.GlauconiteSand, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.DarkAsh, 1L), new FluidStack(tFluid, 1000), GT_Values.NF, ItemList.IC2_Fertilizer.get(2L, new Object[0]), 200);
        }
        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chili, 1L), FluidRegistry.getFluid("potion.chillysauce"), FluidRegistry.getFluid("potion.hotsauce"), false);
        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chili, 1L), FluidRegistry.getFluid("potion.hotsauce"), FluidRegistry.getFluid("potion.diabolosauce"), true);
        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chili, 1L), FluidRegistry.getFluid("potion.diabolosauce"), FluidRegistry.getFluid("potion.diablosauce"), true);
        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coffee, 1L), FluidRegistry.getFluid("milk"), FluidRegistry.getFluid("potion.coffee"), false);
        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Cocoa, 1L), FluidRegistry.getFluid("milk"), FluidRegistry.getFluid("potion.darkchocolatemilk"), false);
        GT_Values.RA.addBrewingRecipe(ItemList.IC2_Hops.get(1L, new Object[0]), FluidRegistry.getFluid("potion.wheatyjuice"), FluidRegistry.getFluid("potion.wheatyhopsjuice"), false);
        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wheat, 1L), FluidRegistry.getFluid("potion.hopsjuice"), FluidRegistry.getFluid("potion.wheatyhopsjuice"), false);
        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L), FluidRegistry.getFluid("potion.tea"), FluidRegistry.getFluid("potion.sweettea"), true);
        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L), FluidRegistry.getFluid("potion.coffee"), FluidRegistry.getFluid("potion.cafeaulait"), false);
        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L), FluidRegistry.getFluid("potion.cafeaulait"), FluidRegistry.getFluid("potion.laitaucafe"), true);
        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L), FluidRegistry.getFluid("potion.lemonjuice"), FluidRegistry.getFluid("potion.lemonade"), false);
        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L), FluidRegistry.getFluid("potion.darkcoffee"), FluidRegistry.getFluid("potion.darkcafeaulait"), true);
        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L), FluidRegistry.getFluid("potion.darkchocolatemilk"), FluidRegistry.getFluid("potion.chocolatemilk"), false);
        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ice, 1L), FluidRegistry.getFluid("potion.tea"), FluidRegistry.getFluid("potion.icetea"), false);
        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gunpowder, 1L), FluidRegistry.getFluid("potion.lemonade"), FluidRegistry.getFluid("potion.cavejohnsonsgrenadejuice"), true);
        GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L), FluidRegistry.getFluid("potion.mundane"), FluidRegistry.getFluid("potion.purpledrink"), true);
        GT_Values.RA.addBrewingRecipe(new ItemStack(Items.fermented_spider_eye, 1, 0), FluidRegistry.getFluid("potion.mundane"), FluidRegistry.getFluid("potion.weakness"), false);
        GT_Values.RA.addBrewingRecipe(new ItemStack(Items.fermented_spider_eye, 1, 0), FluidRegistry.getFluid("potion.thick"), FluidRegistry.getFluid("potion.weakness"), false);

        GT_Values.RA.addBrewingRecipe(GT_ModHandler.getModItem(aTextForestry, "fertilizerBio", 4L, 0), FluidRegistry.WATER, FluidRegistry.getFluid("biomass"), false);
        GT_Values.RA.addBrewingRecipe(GT_ModHandler.getModItem(aTextForestry, "mulch", 2L, 0), GT_ModHandler.getDistilledWater(750L).getFluid(), FluidRegistry.getFluid("biomass"), false);
        GT_Values.RA.addBrewingRecipeCustom(GT_ModHandler.getModItem(aTextForestry, "mulch", 1L, 0), FluidRegistry.getFluidStack("juice", 500), FluidRegistry.getFluidStack("biomass", 750), 128, 4, false);
       
        GT_Values.RA.addBrewingRecipe(GT_ModHandler.getIC2Item("biochaff", 1), FluidRegistry.WATER, FluidRegistry.getFluid("ic2biomass"), false);
        GT_Values.RA.addBrewingRecipe(GT_ModHandler.getIC2Item("biochaff", 1), GT_ModHandler.getDistilledWater(750L).getFluid(), FluidRegistry.getFluid("ic2biomass"), false);

        GT_Values.RA.addMixerRecipe(new ItemStack(Blocks.dirt, 1, 32767), new ItemStack(Items.wheat, 4, 32767), GT_Utility.getIntegratedCircuit(2),  GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.Water.getFluid(100), null, GT_ModHandler.getModItem("Forestry", "fertilizerBio", 1L, 0), 200, 16);
        GT_Values.RA.addMixerRecipe(new ItemStack(Blocks.dirt, 1, 2), new ItemStack(Items.wheat, 4, 32767), GT_Utility.getIntegratedCircuit(2), GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.Water.getFluid(100), GT_Values.NF, GT_ModHandler.getModItem("Forestry", "fertilizerBio", 1L, 0), 200, 16);
        GT_Values.RA.addMixerRecipe(new ItemStack(Blocks.dirt, 1, 32767), GT_ModHandler.getModItem("BiomesOPlenty", "plants", 4, 6), GT_Utility.getIntegratedCircuit(2),  GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.Water.getFluid(100), GT_Values.NF, GT_ModHandler.getModItem("Forestry", "fertilizerBio", 1L, 0), 200, 16);
        GT_Values.RA.addMixerRecipe(new ItemStack(Blocks.dirt, 1, 2), GT_ModHandler.getModItem("BiomesOPlenty", "plants", 4, 6), GT_Utility.getIntegratedCircuit(2),  GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.Water.getFluid(100), GT_Values.NF, GT_ModHandler.getModItem("Forestry", "fertilizerBio", 1L, 0), 200, 16);
        GT_Values.RA.addMixerRecipe(new ItemStack(Blocks.dirt, 1, 32767), GT_ModHandler.getModItem("harvestcraft", "oatsItem", 4), GT_Utility.getIntegratedCircuit(2),  GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.Water.getFluid(100), GT_Values.NF, GT_ModHandler.getModItem("Forestry", "fertilizerBio", 1L, 0), 200, 16);
        GT_Values.RA.addMixerRecipe(new ItemStack(Blocks.dirt, 1, 2), GT_ModHandler.getModItem("harvestcraft", "oatsItem", 4), GT_Utility.getIntegratedCircuit(2),  GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.Water.getFluid(100), GT_Values.NF, GT_ModHandler.getModItem("Forestry", "fertilizerBio", 1L, 0), 200, 16);
        GT_Values.RA.addMixerRecipe(new ItemStack(Blocks.dirt, 1, 32767), GT_ModHandler.getModItem("harvestcraft", "ryeItem", 4), GT_Utility.getIntegratedCircuit(2),  GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.Water.getFluid(100), GT_Values.NF, GT_ModHandler.getModItem("Forestry", "fertilizerBio", 1L, 0), 200, 16);
        GT_Values.RA.addMixerRecipe(new ItemStack(Blocks.dirt, 1, 2), GT_ModHandler.getModItem("harvestcraft", "ryeItem", 4), GT_Utility.getIntegratedCircuit(2),  GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.Water.getFluid(100), GT_Values.NF, GT_ModHandler.getModItem("Forestry", "fertilizerBio", 1L, 0), 200, 16);
        GT_Values.RA.addMixerRecipe(new ItemStack(Blocks.dirt, 1, 32767), GT_ModHandler.getModItem("harvestcraft", "barleyItem", 4), GT_Utility.getIntegratedCircuit(2),  GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.Water.getFluid(100), GT_Values.NF, GT_ModHandler.getModItem("Forestry", "fertilizerBio", 1L, 0), 200, 16);
        GT_Values.RA.addMixerRecipe(new ItemStack(Blocks.dirt, 1, 2), GT_ModHandler.getModItem("harvestcraft", "barleyItem", 4, 6), GT_Utility.getIntegratedCircuit(2),  GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.Water.getFluid(100), GT_Values.NF, GT_ModHandler.getModItem("Forestry", "fertilizerBio", 1L, 0), 200, 16);
        GT_Values.RA.addMixerRecipe(new ItemStack(Blocks.dirt, 1, 32767), GT_ModHandler.getModItem("Natura", "barleyFood", 4), GT_Utility.getIntegratedCircuit(2),  GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.Water.getFluid(100), GT_Values.NF, GT_ModHandler.getModItem("Forestry", "fertilizerBio", 1L, 0), 200, 16);
        GT_Values.RA.addMixerRecipe(new ItemStack(Blocks.dirt, 1, 2), GT_ModHandler.getModItem("Natura", "barleyFood", 4), GT_Utility.getIntegratedCircuit(2),  GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.Water.getFluid(100), GT_Values.NF, GT_ModHandler.getModItem("Forestry", "fertilizerBio", 1L, 0), 200, 16);
        GT_Values.RA.addMixerRecipe(new ItemStack(Blocks.dirt, 1, 32767), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 4L), GT_Utility.getIntegratedCircuit(3),  GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.Water.getFluid(100), GT_Values.NF, GT_ModHandler.getModItem("Forestry", "fertilizerBio", 1L, 0), 200, 16);
        GT_Values.RA.addMixerRecipe(new ItemStack(Blocks.dirt, 1, 2), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 4L), GT_Utility.getIntegratedCircuit(3),  GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.Water.getFluid(100), GT_Values.NF, GT_ModHandler.getModItem("Forestry", "fertilizerBio", 1L, 0), 200, 16);

        addPotionRecipes("waterbreathing", new ItemStack(Items.fish, 1, 3));
        addPotionRecipes("fireresistance", new ItemStack(Items.magma_cream, 1, 0));
        addPotionRecipes("nightvision", new ItemStack(Items.golden_carrot, 1, 0));
        addPotionRecipes("weakness", new ItemStack(Items.fermented_spider_eye, 1, 0));
        addPotionRecipes("poison", new ItemStack(Items.spider_eye, 1, 0));
        addPotionRecipes("health", new ItemStack(Items.speckled_melon, 1, 0));
        addPotionRecipes("regen", new ItemStack(Items.ghast_tear, 1, 0));
        addPotionRecipes("speed", GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L));
        addPotionRecipes("strength", GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Blaze, 1L));
        //TODO CHECK OLD RECIPES???
        //GT_Values.RA.addBrewingRecipe(new ItemStack(Items.fish, 1, 3), FluidRegistry.getFluid("potion.awkward"), FluidRegistry.getFluid("potion.waterbreathing"), false);
        //GT_Values.RA.addBrewingRecipe(new ItemStack(Items.magma_cream, 1, 0), FluidRegistry.getFluid("potion.awkward"), FluidRegistry.getFluid("potion.fireresistance"), false);
        //GT_Values.RA.addBrewingRecipe(new ItemStack(Items.golden_carrot, 1, 0), FluidRegistry.getFluid("potion.awkward"), FluidRegistry.getFluid("potion.nightvision"), false);
        //GT_Values.RA.addBrewingRecipe(new ItemStack(Items.fermented_spider_eye, 1, 0), FluidRegistry.getFluid("potion.awkward"), FluidRegistry.getFluid("potion.weakness"), false);
        //GT_Values.RA.addBrewingRecipe(new ItemStack(Items.spider_eye, 1, 0), FluidRegistry.getFluid("potion.awkward"), FluidRegistry.getFluid("potion.poison"), false);
        //GT_Values.RA.addBrewingRecipe(new ItemStack(Items.speckled_melon, 1, 0), FluidRegistry.getFluid("potion.awkward"), FluidRegistry.getFluid("potion.health"), false);
        //GT_Values.RA.addBrewingRecipe(new ItemStack(Items.ghast_tear, 1, 0), FluidRegistry.getFluid("potion.awkward"), FluidRegistry.getFluid("potion.regen"), false);
        //GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L), FluidRegistry.getFluid("potion.awkward"), FluidRegistry.getFluid("potion.speed"), false);
        //GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Blaze, 1L), FluidRegistry.getFluid("potion.awkward"), FluidRegistry.getFluid("potion.strength"), false);
        //GT_Values.RA.addBrewingRecipe(new ItemStack(Items.spider_eye, 1, 0), FluidRegistry.getFluid("potion.thick"), FluidRegistry.getFluid("potion.poison.strong"), false);
        //GT_Values.RA.addBrewingRecipe(new ItemStack(Items.speckled_melon, 1, 0), FluidRegistry.getFluid("potion.thick"), FluidRegistry.getFluid("potion.health.strong"), false);
        //GT_Values.RA.addBrewingRecipe(new ItemStack(Items.ghast_tear, 1, 0), FluidRegistry.getFluid("potion.thick"), FluidRegistry.getFluid("potion.regen.strong"), false);
        //GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L), FluidRegistry.getFluid("potion.thick"), FluidRegistry.getFluid("potion.speed.strong"), false);
        //GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Blaze, 1L), FluidRegistry.getFluid("potion.thick"), FluidRegistry.getFluid("potion.strength.strong"), false);

        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("milk", 50), FluidRegistry.getFluidStack("potion.mundane", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.lemonjuice", 50), FluidRegistry.getFluidStack("potion.limoncello", 25), 1024, true);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.applejuice", 50), FluidRegistry.getFluidStack("potion.cider", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.goldenapplejuice", 50), FluidRegistry.getFluidStack("potion.goldencider", 25), 1024, true);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.idunsapplejuice", 50), FluidRegistry.getFluidStack("potion.notchesbrew", 25), 1024, true);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.reedwater", 50), FluidRegistry.getFluidStack("potion.rum", 25), 1024, true);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.rum", 50), FluidRegistry.getFluidStack("potion.piratebrew", 10), 2048, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.grapejuice", 50), FluidRegistry.getFluidStack("potion.wine", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.wine", 50), FluidRegistry.getFluidStack("potion.vinegar", 10), 2048, true);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.wheatyjuice", 50), FluidRegistry.getFluidStack("potion.scotch", 25), 1024, true);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.scotch", 50), FluidRegistry.getFluidStack("potion.glenmckenner", 10), 2048, true);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.wheatyhopsjuice", 50), FluidRegistry.getFluidStack("potion.beer", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.hopsjuice", 50), FluidRegistry.getFluidStack("potion.darkbeer", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.darkbeer", 50), FluidRegistry.getFluidStack("potion.dragonblood", 10), 2048, true);

        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.beer", 75), FluidRegistry.getFluidStack("potion.vinegar", 50), 2048, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.cider", 75), FluidRegistry.getFluidStack("potion.vinegar", 50), 2048, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.goldencider", 75), FluidRegistry.getFluidStack("potion.vinegar", 50), 2048, true);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.rum", 75), FluidRegistry.getFluidStack("potion.vinegar", 50), 2048, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.wine", 75), FluidRegistry.getFluidStack("potion.vinegar", 50), 2048, false);

        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.awkward", 50), FluidRegistry.getFluidStack("potion.weakness", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.mundane", 50), FluidRegistry.getFluidStack("potion.weakness", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.thick", 50), FluidRegistry.getFluidStack("potion.weakness", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.poison", 50), FluidRegistry.getFluidStack("potion.damage", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.health", 50), FluidRegistry.getFluidStack("potion.damage", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.waterbreathing", 50), FluidRegistry.getFluidStack("potion.damage", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.nightvision", 50), FluidRegistry.getFluidStack("potion.invisibility", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.fireresistance", 50), FluidRegistry.getFluidStack("potion.slowness", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.speed", 50), FluidRegistry.getFluidStack("potion.slowness", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.strength", 50), FluidRegistry.getFluidStack("potion.weakness", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.regen", 50), FluidRegistry.getFluidStack("potion.poison", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.poison.strong", 50), FluidRegistry.getFluidStack("potion.damage.strong", 10), 2048, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.health.strong", 50), FluidRegistry.getFluidStack("potion.damage.strong", 10), 2048, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.speed.strong", 50), FluidRegistry.getFluidStack("potion.slowness.strong", 10), 2048, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.strength.strong", 50), FluidRegistry.getFluidStack("potion.weakness.strong", 10), 2048, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.nightvision.long", 50), FluidRegistry.getFluidStack("potion.invisibility.long", 10), 2048, false);        
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.regen.strong", 50), FluidRegistry.getFluidStack("potion.poison.strong", 10), 2048, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.poison.long", 50), FluidRegistry.getFluidStack("potion.damage.long", 10), 2048, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.waterbreathing.long", 50), FluidRegistry.getFluidStack("potion.damage.long", 10), 2048, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.fireresistance.long", 50), FluidRegistry.getFluidStack("potion.slowness.long", 10), 2048, false);        
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.speed.long", 50), FluidRegistry.getFluidStack("potion.slowness.long", 10), 2048, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.strength.long", 50), FluidRegistry.getFluidStack("potion.weakness.long", 10), 2048, false);
        GT_Values.RA.addFermentingRecipe(FluidRegistry.getFluidStack("potion.regen.long", 50), FluidRegistry.getFluidStack("potion.poison.long", 10), 2048, false);

        GT_ModHandler.addSmeltingRecipe(ItemList.Food_Raw_PotatoChips.get(1L, new Object[0]), ItemList.Food_PotatoChips.get(1L, new Object[0]));
        GT_ModHandler.addSmeltingRecipe(ItemList.Food_Potato_On_Stick.get(1L, new Object[0]), ItemList.Food_Potato_On_Stick_Roasted.get(1L, new Object[0]));
        GT_ModHandler.addSmeltingRecipe(ItemList.Food_Raw_Bun.get(1L, new Object[0]), ItemList.Food_Baked_Bun.get(1L, new Object[0]));
        GT_ModHandler.addSmeltingRecipe(ItemList.Food_Raw_Bread.get(1L, new Object[0]), ItemList.Food_Baked_Bread.get(1L, new Object[0]));
        GT_ModHandler.addSmeltingRecipe(ItemList.Food_Raw_Baguette.get(1L, new Object[0]), ItemList.Food_Baked_Baguette.get(1L, new Object[0]));
        GT_ModHandler.addSmeltingRecipe(ItemList.Food_Raw_Pizza_Veggie.get(1L, new Object[0]), ItemList.Food_Baked_Pizza_Veggie.get(1L, new Object[0]));
        GT_ModHandler.addSmeltingRecipe(ItemList.Food_Raw_Pizza_Cheese.get(1L, new Object[0]), ItemList.Food_Baked_Pizza_Cheese.get(1L, new Object[0]));
        GT_ModHandler.addSmeltingRecipe(ItemList.Food_Raw_Pizza_Meat.get(1L, new Object[0]), ItemList.Food_Baked_Pizza_Meat.get(1L, new Object[0]));
        GT_ModHandler.addSmeltingRecipe(ItemList.Food_Raw_Baguette.get(1L, new Object[0]), ItemList.Food_Baked_Baguette.get(1L, new Object[0]));
        GT_ModHandler.addSmeltingRecipe(ItemList.Food_Raw_Cake.get(1L, new Object[0]), ItemList.Food_Baked_Cake.get(1L, new Object[0]));
        GT_ModHandler.addSmeltingRecipe(ItemList.Food_Raw_Cookie.get(1L, new Object[0]), new ItemStack(Items.cookie, 1));
        GT_ModHandler.addSmeltingRecipe(new ItemStack(Items.slime_ball, 1), ItemList.IC2_Resin.get(1L, new Object[0]));

        GT_ModHandler.addExtractionRecipe(new ItemStack(Blocks.bookshelf, 1, 32767), new ItemStack(Items.book, 3, 0));
        GT_ModHandler.addExtractionRecipe(new ItemStack(Items.slime_ball, 1), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RawRubber, 2L));
        GT_ModHandler.addExtractionRecipe(ItemList.IC2_Resin.get(1L, new Object[0]), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RawRubber, 3L));
        GT_ModHandler.addExtractionRecipe(GT_ModHandler.getIC2Item("rubberSapling", 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RawRubber, 1L));
        GT_ModHandler.addExtractionRecipe(GT_ModHandler.getIC2Item("rubberLeaves", 16L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RawRubber, 1L));
        GT_ModHandler.addExtractionRecipe(ItemList.Cell_Air.get(1L, new Object[0]), ItemList.Cell_Empty.get(1L, new Object[0]));
        if (Loader.isModLoaded(aTextEBXL)) {
            GT_ModHandler.addExtractionRecipe(GT_ModHandler.getModItem(aTextEBXL, "waterplant1", 1, 0), new ItemStack(Items.dye, 4, 2));
            GT_ModHandler.addExtractionRecipe(GT_ModHandler.getModItem(aTextEBXL, "vines", 1, 0), new ItemStack(Items.dye, 4, 1));
            GT_ModHandler.addExtractionRecipe(GT_ModHandler.getModItem(aTextEBXL, "flower3", 1, 11), new ItemStack(Items.dye, 4, 11));
            GT_ModHandler.addExtractionRecipe(GT_ModHandler.getModItem(aTextEBXL, "flower3", 1, 10), new ItemStack(Items.dye, 4, 5));
            GT_ModHandler.addExtractionRecipe(GT_ModHandler.getModItem(aTextEBXL, "flower3", 1, 9), new ItemStack(Items.dye, 4, 14));
            GT_ModHandler.addExtractionRecipe(GT_ModHandler.getModItem(aTextEBXL, "flower3", 1, 8), new ItemStack(Items.dye, 4, 14));
            GT_ModHandler.addExtractionRecipe(GT_ModHandler.getModItem(aTextEBXL, "flower3", 1, 7), new ItemStack(Items.dye, 4, 1));
            GT_ModHandler.addExtractionRecipe(GT_ModHandler.getModItem(aTextEBXL, "flower3", 1, 6), new ItemStack(Items.dye, 4, 1));
            GT_ModHandler.addExtractionRecipe(GT_ModHandler.getModItem(aTextEBXL, "flower3", 1, 5), new ItemStack(Items.dye, 4, 11));
            GT_ModHandler.addExtractionRecipe(GT_ModHandler.getModItem(aTextEBXL, "flower3", 1, 0), new ItemStack(Items.dye, 4, 9));
            GT_ModHandler.addExtractionRecipe(GT_ModHandler.getModItem(aTextEBXL, "flower3", 1, 4), new ItemStack(Items.dye, 4, 11));
            GT_ModHandler.addExtractionRecipe(GT_ModHandler.getModItem(aTextEBXL, "flower3", 1, 3), new ItemStack(Items.dye, 4, 13));
            GT_ModHandler.addExtractionRecipe(GT_ModHandler.getModItem(aTextEBXL, "flower1", 1, 3), new ItemStack(Items.dye, 4, 5));
            GT_ModHandler.addExtractionRecipe(GT_ModHandler.getModItem(aTextEBXL, "flower3", 1, 2), new ItemStack(Items.dye, 4, 5));
            GT_ModHandler.addExtractionRecipe(GT_ModHandler.getModItem(aTextEBXL, "flower1", 1, 1), new ItemStack(Items.dye, 4, 12));
            GT_ModHandler.addExtractionRecipe(GT_ModHandler.getModItem(aTextEBXL, "flower2", 1, 15), new ItemStack(Items.dye, 4, 11));
            GT_ModHandler.addExtractionRecipe(GT_ModHandler.getModItem(aTextEBXL, "flower2", 1, 14), new ItemStack(Items.dye, 4, 1));
            GT_ModHandler.addExtractionRecipe(GT_ModHandler.getModItem(aTextEBXL, "flower2", 1, 13), new ItemStack(Items.dye, 4, 9));
            GT_ModHandler.addExtractionRecipe(GT_ModHandler.getModItem(aTextEBXL, "flower2", 1, 12), new ItemStack(Items.dye, 4, 14));
            GT_ModHandler.addExtractionRecipe(GT_ModHandler.getModItem(aTextEBXL, "flower2", 1, 11), new ItemStack(Items.dye, 4, 7));
            GT_ModHandler.addExtractionRecipe(GT_ModHandler.getModItem(aTextEBXL, "flower1", 1, 7), new ItemStack(Items.dye, 4, 7));
            GT_ModHandler.addExtractionRecipe(GT_ModHandler.getModItem(aTextEBXL, "flower1", 1, 2), new ItemStack(Items.dye, 4, 11));
            GT_ModHandler.addExtractionRecipe(GT_ModHandler.getModItem(aTextEBXL, "flower3", 1, 13), new ItemStack(Items.dye, 4, 6));
            GT_ModHandler.addExtractionRecipe(GT_ModHandler.getModItem(aTextEBXL, "flower2", 1, 6), new ItemStack(Items.dye, 4, 12));
            GT_ModHandler.addExtractionRecipe(GT_ModHandler.getModItem(aTextEBXL, "flower2", 1, 5), new ItemStack(Items.dye, 4, 10));
            GT_ModHandler.addExtractionRecipe(GT_ModHandler.getModItem(aTextEBXL, "flower2", 1, 2), new ItemStack(Items.dye, 4, 1));
            GT_ModHandler.addExtractionRecipe(GT_ModHandler.getModItem(aTextEBXL, "flower2", 1, 1), new ItemStack(Items.dye, 4, 9));
            GT_ModHandler.addExtractionRecipe(GT_ModHandler.getModItem(aTextEBXL, "flower2", 1, 0), new ItemStack(Items.dye, 4, 13));

            GT_ModHandler.addExtractionRecipe(GT_ModHandler.getModItem(aTextEBXL, "flower2", 1, 7), GT_ModHandler.getModItem(aTextEBXL, "extrabiomes.dye", 1, 0));
            GT_ModHandler.addExtractionRecipe(GT_ModHandler.getModItem(aTextEBXL, "flower3", 1, 1), GT_ModHandler.getModItem(aTextEBXL, "extrabiomes.dye", 1, 1));
            GT_ModHandler.addExtractionRecipe(GT_ModHandler.getModItem(aTextEBXL, "flower3", 1,12), GT_ModHandler.getModItem(aTextEBXL, "extrabiomes.dye", 1, 1));
            GT_ModHandler.addExtractionRecipe(GT_ModHandler.getModItem(aTextEBXL, "flower2", 1, 4), GT_ModHandler.getModItem(aTextEBXL, "extrabiomes.dye", 1, 1));
            GT_ModHandler.addExtractionRecipe(GT_ModHandler.getModItem(aTextEBXL, "flower1", 1, 6), GT_ModHandler.getModItem(aTextEBXL, "extrabiomes.dye", 1, 2));
            GT_ModHandler.addExtractionRecipe(GT_ModHandler.getModItem(aTextEBXL, "flower2", 1, 8), GT_ModHandler.getModItem(aTextEBXL, "extrabiomes.dye", 1, 3));
            GT_ModHandler.addExtractionRecipe(GT_ModHandler.getModItem(aTextEBXL, "flower2", 1, 3), GT_ModHandler.getModItem(aTextEBXL, "extrabiomes.dye", 1, 3));

            GT_ModHandler.addCompressionRecipe(GT_ModHandler.getModItem(aTextEBXL, "saplings_1", 4, 0), ItemList.IC2_Plantball.get(1, new Object[0]));
            GT_ModHandler.addCompressionRecipe(GT_ModHandler.getModItem(aTextEBXL, "saplings_1", 4, 1), ItemList.IC2_Plantball.get(1, new Object[0]));
            GT_ModHandler.addCompressionRecipe(GT_ModHandler.getModItem(aTextEBXL, "saplings_1", 4, 2), ItemList.IC2_Plantball.get(1, new Object[0]));
            GT_ModHandler.addCompressionRecipe(GT_ModHandler.getModItem(aTextEBXL, "saplings_1", 4, 3), ItemList.IC2_Plantball.get(1, new Object[0]));
            GT_ModHandler.addCompressionRecipe(GT_ModHandler.getModItem(aTextEBXL, "saplings_1", 4, 4), ItemList.IC2_Plantball.get(1, new Object[0]));
            GT_ModHandler.addCompressionRecipe(GT_ModHandler.getModItem(aTextEBXL, "saplings_1", 4, 5), ItemList.IC2_Plantball.get(1, new Object[0]));
            GT_ModHandler.addCompressionRecipe(GT_ModHandler.getModItem(aTextEBXL, "saplings_1", 4, 6), ItemList.IC2_Plantball.get(1, new Object[0]));
            GT_ModHandler.addCompressionRecipe(GT_ModHandler.getModItem(aTextEBXL, "saplings_1", 4, 7), ItemList.IC2_Plantball.get(1, new Object[0]));
            GT_ModHandler.addCompressionRecipe(GT_ModHandler.getModItem(aTextEBXL, "saplings_2", 4, 0), ItemList.IC2_Plantball.get(1, new Object[0]));
            GT_ModHandler.addCompressionRecipe(GT_ModHandler.getModItem(aTextEBXL, "saplings_2", 4, 1), ItemList.IC2_Plantball.get(1, new Object[0]));
            GT_ModHandler.addCompressionRecipe(GT_ModHandler.getModItem(aTextEBXL, "saplings_2", 4, 2), ItemList.IC2_Plantball.get(1, new Object[0]));
            GT_ModHandler.addCompressionRecipe(GT_ModHandler.getModItem(aTextEBXL, "saplings_2", 4, 3), ItemList.IC2_Plantball.get(1, new Object[0]));
            GT_ModHandler.addCompressionRecipe(GT_ModHandler.getModItem(aTextEBXL, "saplings_2", 4, 4), ItemList.IC2_Plantball.get(1, new Object[0]));

        }
        GT_ModHandler.addCompressionRecipe(GT_ModHandler.getModItem("miscutils", "blockRainforestOakSapling", 8, 0), ItemList.IC2_Plantball.get(1, new Object[0]));

        GT_ModHandler.addCompressionRecipe(ItemList.IC2_Compressed_Coal_Chunk.get(1L, new Object[0]), ItemList.IC2_Industrial_Diamond.get(1L, new Object[0]));
        GT_ModHandler.addCompressionRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Uranium, 1L), GT_ModHandler.getIC2Item("Uran238", 1L));
        GT_ModHandler.addCompressionRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Uranium235, 1L), GT_ModHandler.getIC2Item("Uran235", 1L));
        GT_ModHandler.addCompressionRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Plutonium, 1L), GT_ModHandler.getIC2Item("Plutonium", 1L));
        GT_ModHandler.addCompressionRecipe(GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Uranium235, 1L), GT_ModHandler.getIC2Item("smallUran235", 1L));
        GT_ModHandler.addCompressionRecipe(GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Plutonium, 1L), GT_ModHandler.getIC2Item("smallPlutonium", 1L));
        GT_ModHandler.addCompressionRecipe(new ItemStack(Blocks.ice, 2, 32767), new ItemStack(Blocks.packed_ice, 1, 0));
        GT_ModHandler.addCompressionRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ice, 1L), new ItemStack(Blocks.ice, 1, 0));
        GT_ModHandler.addCompressionRecipe(GT_OreDictUnificator.get(OrePrefixes.gem, Materials.CertusQuartz, 4L), GT_ModHandler.getModItem(aTextAE, "tile.BlockQuartz", 1L));
        GT_ModHandler.addCompressionRecipe(GT_ModHandler.getModItem(aTextAE, aTextAEMM, 8L, 10), GT_ModHandler.getModItem(aTextAE, "tile.BlockQuartz", 1L));
        GT_ModHandler.addCompressionRecipe(GT_ModHandler.getModItem(aTextAE, aTextAEMM, 8L, 11), new ItemStack(Blocks.quartz_block, 1, 0));
        GT_ModHandler.addCompressionRecipe(GT_ModHandler.getModItem(aTextAE, aTextAEMM, 8L, 12), GT_ModHandler.getModItem(aTextAE, "tile.BlockFluix", 1L));
        GT_ModHandler.addCompressionRecipe(new ItemStack(Items.quartz, 4, 0), new ItemStack(Blocks.quartz_block, 1, 0));
        //GT_ModHandler.addCompressionRecipe(new ItemStack(Items.wheat, 9, 0), new ItemStack(Blocks.hay_block, 1, 0));
        GT_ModHandler.addCompressionRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glowstone, 4L), new ItemStack(Blocks.glowstone, 1));

        GT_Values.RA.addCompressorRecipe(Materials.Fireclay.getDust(1), ItemList.CompressedFireclay.get(1), 80, 4);
        GameRegistry.addSmelting(ItemList.CompressedFireclay.get(1, new Object[0]), ItemList.Firebrick.get(1, new Object[0]), 0);

        GT_Values.RA.addCutterRecipe(GT_OreDictUnificator.get(OrePrefixes.block, Materials.Graphite, 1L), GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Graphite, 9L), GT_Values.NI, 500, 48);
        GT_ModHandler.removeFurnaceSmelting(GT_OreDictUnificator.get(OrePrefixes.ore, Materials.Graphite, 1L));
        GT_ModHandler.addSmeltingRecipe(GT_OreDictUnificator.get(OrePrefixes.ore, Materials.Graphite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Graphite, 1L));
        GT_ModHandler.removeFurnaceSmelting(GT_OreDictUnificator.get(OrePrefixes.oreBlackgranite, Materials.Graphite, 1L));
        GT_ModHandler.addSmeltingRecipe(GT_OreDictUnificator.get(OrePrefixes.oreBlackgranite, Materials.Graphite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Graphite, 1L));
        GT_ModHandler.removeFurnaceSmelting(GT_OreDictUnificator.get(OrePrefixes.oreEndstone, Materials.Graphite, 1L));
        GT_ModHandler.addSmeltingRecipe(GT_OreDictUnificator.get(OrePrefixes.oreEndstone, Materials.Graphite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Graphite, 1L));
        GT_ModHandler.removeFurnaceSmelting(GT_OreDictUnificator.get(OrePrefixes.oreNetherrack, Materials.Graphite, 1L));
        GT_ModHandler.addSmeltingRecipe(GT_OreDictUnificator.get(OrePrefixes.oreNetherrack, Materials.Graphite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Graphite, 1L));
        GT_ModHandler.removeFurnaceSmelting(GT_OreDictUnificator.get(OrePrefixes.oreRedgranite, Materials.Graphite, 1L));
        GT_ModHandler.addSmeltingRecipe(GT_OreDictUnificator.get(OrePrefixes.oreRedgranite, Materials.Graphite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Graphite, 1L));

        GT_ModHandler.addPulverisationRecipe(GT_ModHandler.getModItem(aTextAE, "tile.BlockSkyStone", 1L, 32767), GT_ModHandler.getModItem(aTextAE, aTextAEMM, 1L, 45), GT_Values.NI, 0, false);
        GT_ModHandler.addPulverisationRecipe(GT_ModHandler.getModItem(aTextAE, "tile.BlockSkyChest", 1L, 32767), GT_ModHandler.getModItem(aTextAE, aTextAEMM, 8L, 45), GT_Values.NI, 0, false);
        GT_ModHandler.addPulverisationRecipe(new ItemStack(Items.blaze_rod, 1), new ItemStack(Items.blaze_powder, 3), new ItemStack(Items.blaze_powder, 1), 50, false);
        GT_ModHandler.addPulverisationRecipe(GT_ModHandler.getModItem("Railcraft", "cube.crushed.obsidian", 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Obsidian, 1L), GT_Values.NI, 0, true);
        GT_ModHandler.addPulverisationRecipe(new ItemStack(Items.flint, 1, 32767), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Flint, 4L), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Flint, 1L), 40, true);
        GT_ModHandler.addPulverisationRecipe(new ItemStack(Blocks.red_mushroom, 1, 32767), ItemList.IC2_Grin_Powder.get(1L, new Object[0]));
        GT_ModHandler.addPulverisationRecipe(new ItemStack(Items.item_frame, 1, 32767), new ItemStack(Items.leather, 1), GT_OreDictUnificator.getDust(Materials.Wood, OrePrefixes.stick.mMaterialAmount * 4L), 95, false);
        GT_ModHandler.addPulverisationRecipe(new ItemStack(Items.bow, 1, 0), new ItemStack(Items.string, 3), GT_OreDictUnificator.getDust(Materials.Wood, OrePrefixes.stick.mMaterialAmount * 3L), 95, false);

        GT_Values.RA.addForgeHammerRecipe(new ItemStack(Blocks.stonebrick, 1, 0), new ItemStack(Blocks.stonebrick, 1, 2), 10, 16);
        GT_Values.RA.addForgeHammerRecipe(new ItemStack(Blocks.stone, 1, 0), new ItemStack(Blocks.cobblestone, 1, 0), 10, 16);
        GT_Values.RA.addForgeHammerRecipe(new ItemStack(Blocks.cobblestone, 1, 0), new ItemStack(Blocks.gravel, 1, 0), 10, 16);
        GT_Values.RA.addForgeHammerRecipe(new ItemStack(Blocks.gravel, 1, 0), new ItemStack(Blocks.sand, 1, 0), 10, 16);
		GT_Values.RA.addSifterRecipe(new ItemStack(Blocks.gravel, 1, 0), new ItemStack[] {new ItemStack(Items.flint, 1, 0), new ItemStack(Items.flint, 1, 0), new ItemStack(Items.flint, 1, 0), new ItemStack(Items.flint, 1, 0), new ItemStack(Items.flint, 1, 0), new ItemStack(Items.flint, 1, 0)}, new int[] {10000, 9000, 8000, 6000, 3300, 2500}, 600, 16);
        GT_Values.RA.addForgeHammerRecipe(new ItemStack(Blocks.sandstone, 1, 32767), new ItemStack(Blocks.sand, 1, 0), 10, 16);
        GT_Values.RA.addForgeHammerRecipe(new ItemStack(Blocks.ice, 1, 0), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ice, 1L), 10, 16);
        GT_Values.RA.addForgeHammerRecipe(new ItemStack(Blocks.packed_ice, 1, 0), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ice, 2L), 10, 16);
        //GT_Values.RA.addForgeHammerRecipe(new ItemStack(Blocks.hardened_clay, 1, 0), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Clay, 1L), 16, 10);
        //GT_Values.RA.addForgeHammerRecipe(new ItemStack(Blocks.stained_hardened_clay, 1, 32767), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Clay, 1L), 16, 10);
        GT_Values.RA.addForgeHammerRecipe(new ItemStack(Blocks.brick_block, 1, 0), new ItemStack(Items.brick, 3, 0), 10, 16);
        GT_Values.RA.addForgeHammerRecipe(new ItemStack(Blocks.nether_brick, 1, 0), new ItemStack(Items.netherbrick, 3, 0), 10, 16);
        GT_Values.RA.addForgeHammerRecipe(new ItemStack(Blocks.stained_glass, 1, 32767), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glass, 1L), 10, 16);
        GT_Values.RA.addForgeHammerRecipe(new ItemStack(Blocks.glass, 1, 32767), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glass, 1L), 10, 10);
        GT_Values.RA.addForgeHammerRecipe(new ItemStack(Blocks.stained_glass_pane, 1, 32767), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3L), 10, 16);
        GT_Values.RA.addForgeHammerRecipe(new ItemStack(Blocks.glass_pane, 1, 32767), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3L), 10, 16);

        GT_Values.RA.addForgeHammerRecipe(Materials.Brick.getIngots(1), Materials.Brick.getDustSmall(1), 10, 16);
        GT_Values.RA.addForgeHammerRecipe(ItemList.Firebrick.get(1, new Object[0]), Materials.Brick.getDust(1), 10, 16);
        GT_Values.RA.addForgeHammerRecipe(ItemList.Casing_Firebricks.get(1, new Object[0]), ItemList.Firebrick.get(3, new Object[0]), 10, 16);

        GT_ModHandler.addPulverisationRecipe(Materials.Brick.getIngots(1), Materials.Brick.getDustSmall(1));
        GT_ModHandler.addPulverisationRecipe(ItemList.CompressedFireclay.get(1, new Object[0]), Materials.Fireclay.getDustSmall(1));
        GT_ModHandler.addPulverisationRecipe(ItemList.Firebrick.get(1, new Object[0]), Materials.Brick.getDust(1));
        GT_ModHandler.addPulverisationRecipe(ItemList.Casing_Firebricks.get(1, new Object[0]), Materials.Brick.getDust(4));
        GT_ModHandler.addPulverisationRecipe(ItemList.Machine_Bricked_BlastFurnace.get(1, new Object[0]), Materials.Brick.getDust(8), Materials.Iron.getDust(1), true);

        GT_Values.RA.addForgeHammerRecipe(GT_ModHandler.getModItem("HardcoreEnderExpansion", "endium_ore", 1), GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.HeeEndium, 1), 16, 10);
        GT_ModHandler.addPulverisationRecipe(GT_ModHandler.getModItem("HardcoreEnderExpansion", "endium_ore", 1), GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.HeeEndium, 2), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Endstone, 1), 50, GT_Values.NI, 0, true);
        GT_OreDictUnificator.set(OrePrefixes.ingot, Materials.HeeEndium, GT_ModHandler.getModItem("HardcoreEnderExpansion", "endium_ingot", 1), true, true);

        GT_Values.RA.addAmplifier(ItemList.IC2_Scrap.get(9L, new Object[0]), 180, 1);
        GT_Values.RA.addAmplifier(ItemList.IC2_Scrapbox.get(1L, new Object[0]), 180, 1);

        GT_Values.RA.addBoxingRecipe(ItemList.IC2_Scrap.get(9L, new Object[0]), ItemList.Schematic_3by3.get(0L, new Object[0]), ItemList.IC2_Scrapbox.get(1L, new Object[0]), 16, 1);
        GT_Values.RA.addBoxingRecipe(ItemList.Food_Fries.get(1L, new Object[0]), GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Paper, 1L), ItemList.Food_Packaged_Fries.get(1L, new Object[0]), 64, 16);
        GT_Values.RA.addBoxingRecipe(ItemList.Food_PotatoChips.get(1L, new Object[0]), GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 1L), ItemList.Food_Packaged_PotatoChips.get(1L, new Object[0]), 64, 16);
        GT_Values.RA.addBoxingRecipe(ItemList.Food_ChiliChips.get(1L, new Object[0]), GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 1L), ItemList.Food_Packaged_ChiliChips.get(1L, new Object[0]), 64, 16);
        //TODO CHECK!!!!
        //GT_Values.RA.addBlastRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Steel, 3L), GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Silicon, 1L), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.ElectricalSteel, 4L), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.DarkAsh, 2L), (int) Math.max(Materials.ElectricalSteel.getMass() / 40L, 1L) * Materials.ElectricalSteel.mBlastFurnaceTemp, 120, Materials.ElectricalSteel.mBlastFurnaceTemp);
        //GT_Values.RA.addBlastRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Gold, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glowstone, 1L), Materials.Redstone.getMolten(144), GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.EnergeticAlloy, 1L), null, Materials.EnergeticAlloy.mBlastFurnaceTemp / 10, 120, Materials.EnergeticAlloy.mBlastFurnaceTemp);
        //GT_Values.RA.addBlastRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.EnergeticAlloy, 1L), GT_OreDictUnificator.get(OrePrefixes.gem, Materials.EnderPearl, 1L), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.PhasedGold, 1L), null, Materials.VibrantAlloy.mBlastFurnaceTemp / 10, 480, Materials.VibrantAlloy.mBlastFurnaceTemp);
        ////GT_Values.RA.addAlloySmelterRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L), GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Silicon, 1L), GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.RedstoneAlloy, 1L), 400, 24);
        ////GT_Values.RA.addAlloySmelterRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L), GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, 1L), GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.ConductiveIron, 1L), 400, 24);
        //GT_Values.RA.addBlastRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, 1L), GT_OreDictUnificator.get(OrePrefixes.gem, Materials.EnderPearl, 1L), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.PhasedIron, 1L), null, Materials.PulsatingIron.mBlastFurnaceTemp / 10, 120, Materials.PulsatingIron.mBlastFurnaceTemp);
        ////GT_Values.RA.addAlloySmelterRecipe(new ItemStack(Blocks.soul_sand), GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Gold, 1L), GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Soularium, 1L), 400, 24);

        //GT_Values.RA.addBlastRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Tungsten, 1L), GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Steel, 1L), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.TungstenSteel, 2L), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.DarkAsh, 1L), (int) Math.max(Materials.TungstenSteel.getMass() / 80L, 1L) * Materials.TungstenSteel.mBlastFurnaceTemp, 480, Materials.TungstenSteel.mBlastFurnaceTemp);
        //GT_Values.RA.addBlastRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Tungsten, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 1L), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.TungstenCarbide, 1L), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Ash, 2L), (int) Math.max(Materials.TungstenCarbide.getMass() / 40L, 1L) * Materials.TungstenCarbide.mBlastFurnaceTemp, 480, Materials.TungstenCarbide.mBlastFurnaceTemp);
        //GT_Values.RA.addBlastRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Vanadium, 3L), GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Gallium, 1L), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.VanadiumGallium, 4L), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.DarkAsh, 2L), (int) Math.max(Materials.VanadiumGallium.getMass() / 40L, 1L) * Materials.VanadiumGallium.mBlastFurnaceTemp, 480, Materials.VanadiumGallium.mBlastFurnaceTemp);
        //GT_Values.RA.addBlastRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Niobium, 1L), GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Titanium, 1L), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.NiobiumTitanium, 2L), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.DarkAsh, 1L), (int) Math.max(Materials.NiobiumTitanium.getMass() / 80L, 1L) * Materials.NiobiumTitanium.mBlastFurnaceTemp, 480, Materials.NiobiumTitanium.mBlastFurnaceTemp);
        //GT_Values.RA.addBlastRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Nickel, 4L), GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Chrome, 1L), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.Nichrome, 5L), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.DarkAsh, 2L), (int) Math.max(Materials.Nichrome.getMass() / 32L, 1L) * Materials.Nichrome.mBlastFurnaceTemp, 480, Materials.Nichrome.mBlastFurnaceTemp);
        //GT_Values.RA.addBlastRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ruby, 1L), GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Aluminium, 3L), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 1L), 400, 100, 1200);
        //GT_Values.RA.addBlastRecipe(GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Ruby, 1L), GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Aluminium, 3L), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 1L), 320, 100, 1200);
        //GT_Values.RA.addBlastRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.GreenSapphire, 1L), GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Aluminium, 3L), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 1L), 400, 100, 1200);
        //GT_Values.RA.addBlastRecipe(GT_OreDictUnificator.get(OrePrefixes.gem, Materials.GreenSapphire, 1L), GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Aluminium, 3L), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 1L), 320, 100, 1200);
        //GT_Values.RA.addBlastRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sapphire, 1L), GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Aluminium, 3L), GT_Values.NI, 400, 100, 1200);
        //GT_Values.RA.addBlastRecipe(GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Sapphire, 1L), GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Aluminium, 3L), GT_Values.NI, 320, 100, 1200);
        //GT_Values.RA.addBlastRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ilmenite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 1L), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.WroughtIron, 4L), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Rutile, 4L), 800, 500, 1700);
        //GT_Values.RA.addBlastRecipe(GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Ilmenite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 1L), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.WroughtIron, 4L), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Rutile, 4L), 800, 500, 1700);
        //GT_Values.RA.addBlastRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnesium, 2L), GT_Values.NI, Materials.Titaniumtetrachloride.getFluid(1000L), GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.Titanium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnesiumchloride, 2L), 800, 480, Materials.Titanium.mBlastFurnaceTemp + 200);
        //GT_Values.RA.addBlastRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Galena, 1L), GT_Values.NI, Materials.Oxygen.getGas(2000L), GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Silver, 4L), GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Lead, 4L), 400, 500, 1500);
        //GT_Values.RA.addBlastRecipe(GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Galena, 1L), GT_Values.NI, Materials.Oxygen.getGas(2000L), GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Silver, 5L), GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Lead, 5L), 320, 500, 1500);
        //GT_Values.RA.addBlastRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnetite, 1L), GT_Values.NI, Materials.Oxygen.getGas(2000L), GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.WroughtIron, 4L), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.DarkAsh, 1L), 400, 500, 1000);
        //GT_Values.RA.addBlastRecipe(GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Magnetite, 1L), GT_Values.NI, Materials.Oxygen.getGas(2000L), GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.WroughtIron, 5L), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Ash, 1L), 320, 500, 1000);
        //GT_Values.RA.addBlastRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, 1L), GT_Values.NI, Materials.Oxygen.getGas(1000L), GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Steel, 1L), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.DarkAsh, 1L), 500, 120, 1000);
        //GT_Values.RA.addBlastRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.PigIron, 1L), GT_Values.NI, Materials.Oxygen.getGas(1000L), GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Steel, 1L), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.DarkAsh, 1L), 100, 120, 1000);
        //GT_Values.RA.addBlastRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.WroughtIron, 1L), GT_Values.NI, Materials.Oxygen.getGas(1000L), GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Steel, 1L), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.DarkAsh, 1L), 100, 120, 1000);
        //GT_Values.RA.addBlastRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.ShadowIron, 1L), GT_Values.NI, Materials.Oxygen.getGas(1000L), GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.ShadowSteel, 1L), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.DarkAsh, 1L), 500, 120, 1100);
        //GT_Values.RA.addBlastRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.MeteoricIron, 1L), GT_Values.NI, Materials.Oxygen.getGas(1000L), GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.MeteoricSteel, 1L), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.DarkAsh, 1L), 500, 120, 1200);
        //GT_Values.RA.addBlastRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 1L), GT_Values.NI, Materials.Oxygen.getGas(1000L), GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.AnnealedCopper, 1L), GT_Values.NI, 500, 120, 1200);
        //GT_Values.RA.addBlastRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Copper, 1L), GT_Values.NI, Materials.Oxygen.getGas(1000L), GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.AnnealedCopper, 1L), GT_Values.NI, 500, 120, 1200);
        //GT_Values.RA.addBlastRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.ElectricalSteel, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Obsidian, 1L), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.DarkSteel, 1L), GT_Values.NI, 4000, 120, 2000);
        //GT_Values.RA.addBlastRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Iridium, 3L), GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Osmium, 1L), Materials.Helium.getGas(1000), null, GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.Osmiridium, 4L), null, 500, 1920, 2900);
        //GT_Values.RA.addBlastRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Naquadah, 1L), GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Osmiridium, 1L), Materials.Argon.getGas(1000), null, GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.NaquadahAlloy, 2L), null, 500, 30720, Materials.NaquadahAlloy.mBlastFurnaceTemp);
        //GT_Values.RA.addBlastRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gallium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Arsenic, 1L), null, null, GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.GalliumArsenide, 2L), null, 600, 120, Materials.GalliumArsenide.mBlastFurnaceTemp);


        if (!GregTech_API.mIC2Classic) {
            GT_Values.RA.addCannerRecipe(GT_ModHandler.getIC2Item("fuelRod", 1), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Lithium, 1L), GT_ModHandler.getIC2Item("reactorLithiumCell", 1, 1), null, 16, 64);
            GT_Values.RA.addFluidExtractionRecipe(GT_ModHandler.getIC2Item("TritiumCell", 1), GT_ModHandler.getIC2Item("fuelRod", 1), Materials.Tritium.getGas(32), 10000, 16, 64);
            GT_Values.RA.addCannerRecipe(GT_ModHandler.getIC2Item("fuelRod", 1), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Thorium, 3), ItemList.ThoriumCell_1.get(1L, new Object[0]), null, 30, 16);
            GT_Values.RA.addCannerRecipe(ItemList.Large_Fluid_Cell_TungstenSteel.get(1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.NaquadahEnriched, 3), ItemList.NaquadahCell_1.get(1L), null, 30, 16);
            GT_Values.RA.addCannerRecipe(ItemList.Large_Fluid_Cell_TungstenSteel.get(1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadria, 3), ItemList.MNqCell_1.get(1L), null, 30, 16);
            GT_Values.RA.addCannerRecipe(GT_ModHandler.getIC2Item("fuelRod", 1), GT_ModHandler.getIC2Item("UranFuel", 1), ItemList.Uraniumcell_1.get(1, new Object[0]), null, 30, 16);
            GT_Values.RA.addCannerRecipe(GT_ModHandler.getIC2Item("fuelRod", 1), GT_ModHandler.getIC2Item("MOXFuel", 1), ItemList.Moxcell_1.get(1, new Object[0]), null, 30, 16);
        }
        //Fusion tiering -T1 32768EU/t -T2 65536EU/t - T3 131073EU/t
        //Fusion with margin 32700         65450          131000
        //Startup  max       160M EU       320M EU        640M EU
        //Fluid input,Fluid input,Fluid output,ticks,EU/t,Startup
        //FT1, FT2, FT3 - fusion tier required, + - requires different startup recipe (startup cost bigger than available on the tier)
        GT_Values.RA.addFusionReactorRecipe(Materials.Lithium.getMolten(16), Materials.Tungsten.getMolten(16), Materials.Iridium.getMolten(16), 64, 32700, 300000000); //FT1+ - utility
        GT_Values.RA.addFusionReactorRecipe(Materials.Deuterium.getGas(125), Materials.Tritium.getGas(125), Materials.Helium.getPlasma(125), 16, 4096, 40000000);  //FT1 Cheap - farmable
        GT_Values.RA.addFusionReactorRecipe(Materials.Deuterium.getGas(125), Materials.Helium_3.getGas(125), Materials.Helium.getPlasma(125), 16, 2048, 60000000); //FT1 Expensive //
        GT_Values.RA.addFusionReactorRecipe(Materials.Carbon.getMolten(125), Materials.Helium_3.getGas(125), Materials.Oxygen.getPlasma(125), 32, 4096, 80000000); //FT1 Expensive //
        GT_Values.RA.addFusionReactorRecipe(Materials.Aluminium.getMolten(16), Materials.Lithium.getMolten(16), Materials.Sulfur.getPlasma(144), 32, 10240, 240000000); //FT1+ Cheap
        GT_Values.RA.addFusionReactorRecipe(Materials.Beryllium.getMolten(16), Materials.Deuterium.getGas(375), Materials.Nitrogen.getPlasma(125), 16, 16384, 180000000); //FT1+ Expensive //
        GT_Values.RA.addFusionReactorRecipe(Materials.Silicon.getMolten(16), Materials.Magnesium.getMolten(16), Materials.Iron.getPlasma(144), 32, 8192, 360000000); //FT1++ Cheap //
        GT_Values.RA.addFusionReactorRecipe(Materials.Potassium.getMolten(16), Materials.Fluorine.getGas(144), Materials.Nickel.getPlasma(144), 16, 32700, 480000000); //FT1++ Expensive //
        GT_Values.RA.addFusionReactorRecipe(Materials.Beryllium.getMolten(16), Materials.Tungsten.getMolten(16), Materials.Platinum.getMolten(16), 32, 32700, 150000000); //FT1 - utility
        GT_Values.RA.addFusionReactorRecipe(Materials.Neodymium.getMolten(16), Materials.Hydrogen.getGas(48), Materials.Europium.getMolten(16), 64, 24576, 150000000); //FT1 - utility
        GT_Values.RA.addFusionReactorRecipe(Materials.Lutetium.getMolten(16), Materials.Chrome.getMolten(16), Materials.Americium.getMolten(16), 96, 49152, 200000000); //FT2 - utility
        GT_Values.RA.addFusionReactorRecipe(Materials.Plutonium.getMolten(16), Materials.Thorium.getMolten(16), Materials.Naquadah.getMolten(16), 64, 32700, 300000000); //FT1+ - utility
        GT_Values.RA.addFusionReactorRecipe(Materials.Americium.getMolten(16), Materials.Naquadria.getMolten(16), Materials.Neutronium.getMolten(1), 1200, 98304, 600000000); //FT3 - utility

        GT_Values.RA.addFusionReactorRecipe(Materials.Tungsten.getMolten(16), Materials.Helium.getGas(16), Materials.Osmium.getMolten(16), 256, 24578, 150000000); //FT1 - utility
        GT_Values.RA.addFusionReactorRecipe(Materials.Manganese.getMolten(16), Materials.Hydrogen.getGas(16), Materials.Iron.getMolten(16), 64, 8192, 120000000); //FT1 - utility
        GT_Values.RA.addFusionReactorRecipe(Materials.Mercury.getFluid(16), Materials.Magnesium.getMolten(16), Materials.Uranium.getMolten(16), 64, 49152, 240000000); //FT2 - utility
        GT_Values.RA.addFusionReactorRecipe(Materials.Gold.getMolten(16), Materials.Aluminium.getMolten(16), Materials.Uranium.getMolten(16), 64, 49152, 240000000); //FT2 - utility
        GT_Values.RA.addFusionReactorRecipe(Materials.Uranium.getMolten(16), Materials.Helium.getGas(16), Materials.Plutonium.getMolten(16), 128, 49152, 480000000); //FT2+ - utility
        GT_Values.RA.addFusionReactorRecipe(Materials.Vanadium.getMolten(16), Materials.Hydrogen.getGas(125), Materials.Chrome.getMolten(16), 64, 24576, 140000000); //FT1 - utility

        GT_Values.RA.addFusionReactorRecipe(Materials.Gallium.getMolten(16), Materials.Radon.getGas(125), Materials.Duranium.getMolten(16), 64, 16384, 140000000);
        GT_Values.RA.addFusionReactorRecipe(Materials.Titanium.getMolten(48), Materials.Duranium.getMolten(32), Materials.Tritanium.getMolten(16), 64, 32700, 200000000);
        GT_Values.RA.addFusionReactorRecipe(Materials.Tantalum.getMolten(16), Materials.Tritium.getGas(16), Materials.Tungsten.getMolten(16), 16, 24576, 200000000); //
        GT_Values.RA.addFusionReactorRecipe(Materials.Silver.getMolten(16), Materials.Lithium.getMolten(16), Materials.Indium.getMolten(16), 32, 24576, 380000000); //

        //NEW RECIPES FOR FUSION
        GT_Values.RA.addFusionReactorRecipe(Materials.Magnesium.getMolten(144), Materials.Carbon.getMolten(144), Materials.Argon.getPlasma(125), 32, 24576, 180000000);//FT1+ - utility

        GT_Values.RA.addFusionReactorRecipe(Materials.Copper.getMolten(72), Materials.Tritium.getGas(250), Materials.Zinc.getPlasma(72), 16, 49152, 180000000);//FT2 - farmable
        GT_Values.RA.addFusionReactorRecipe(Materials.Cobalt.getMolten(144), Materials.Silicon.getMolten(144), Materials.Niobium.getPlasma(144), 16, 49152, 200000000);//FT2 - utility
        GT_Values.RA.addFusionReactorRecipe(Materials.Gold.getMolten(144), Materials.Arsenic.getMolten(144), Materials.Silver.getPlasma(144), 16, 49152, 350000000);//FT2+
        GT_Values.RA.addFusionReactorRecipe(Materials.Silver.getMolten(144), Materials.Helium_3.getGas(375), Materials.Tin.getPlasma(144), 16, 49152, 280000000);//FT2
        GT_Values.RA.addFusionReactorRecipe(Materials.Tungsten.getMolten(144), Materials.Carbon.getMolten(144), Materials.Mercury.getPlasma(144), 16, 49152, 300000000);//FT2

        GT_Values.RA.addFusionReactorRecipe(Materials.Tantalum.getMolten(144), Materials.Zinc.getPlasma(72), Materials.Bismuth.getPlasma(144), 16, 98304, 350000000);//FT3 - farmable
        GT_Values.RA.addFusionReactorRecipe(Materials.Iridium.getMolten(144), Materials.Fluorine.getGas(500), Materials.Radon.getPlasma(144), 32, 98304, 450000000);//FT3 - utility
        GT_Values.RA.addFusionReactorRecipe(Materials.Plutonium241.getMolten(144), Materials.Hydrogen.getGas(2000), Materials.Americium.getPlasma(144), 64, 98304, 500000000);//FT3
        //GT_Values.RA.addFusionReactorRecipe(Materials.Neutronium.getMolten(144), Materials.Neutronium.getMolten(144), Materials.Neutronium.getPlasma(72), 64, 130000, 640000000);//FT3+ - yes it is a bit troll XD

        GT_ModHandler.removeRecipeByOutput(ItemList.IC2_Fertilizer.get(1L, new Object[0]));
        GT_Values.RA.addImplosionRecipe(ItemList.IC2_Compressed_Coal_Chunk.get(1L, new Object[0]), 8, ItemList.IC2_Industrial_Diamond.get(1L, new Object[0]), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 4L));
        GT_Values.RA.addImplosionRecipe(ItemList.Ingot_IridiumAlloy.get(1L, new Object[0]), 8, GT_OreDictUnificator.get(OrePrefixes.plateAlloy, Materials.Iridium, 1L), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 4L));

        if (Loader.isModLoaded("GalacticraftMars")) {
            //GT_ModHandler.addCraftingRecipe(ItemList.Ingot_Heavy1.get(1L, new Object[0]), GT_ModHandler.RecipeBits.NOT_REMOVABLE, new Object[]{"BhB", "CAS", "B B", 'B', OrePrefixes.bolt.get(Materials.StainlessSteel), 'C', OrePrefixes.compressed.get(Materials.Bronze), 'A', OrePrefixes.compressed.get(Materials.Aluminium), 'S', OrePrefixes.compressed.get(Materials.Steel)});
            //GT_ModHandler.addCraftingRecipe(ItemList.Ingot_Heavy2.get(1L, new Object[0]), GT_ModHandler.RecipeBits.NOT_REMOVABLE, new Object[]{" BB", "hPC", " BB", 'B', OrePrefixes.bolt.get(Materials.Tungsten), 'C', OrePrefixes.compressed.get(Materials.MeteoricIron), 'P', GT_ModHandler.getModItem("GalacticraftCore", "item.heavyPlating", 1L)});
            //GT_ModHandler.addCraftingRecipe(ItemList.Ingot_Heavy3.get(1L, new Object[0]), GT_ModHandler.RecipeBits.NOT_REMOVABLE, new Object[]{" BB", "hPC", " BB", 'B', OrePrefixes.bolt.get(Materials.TungstenSteel), 'C', OrePrefixes.compressed.get(Materials.Desh), 'P', GT_ModHandler.getModItem("GalacticraftMars", "item.null", 1L, 3)});
            //-->Duplicates x3
            ////GT_Values.RA.addImplosionRecipe(ItemList.Ingot_Heavy1.get(1L, new Object[0]), 8, GT_ModHandler.getModItem("GalacticraftCore", "item.heavyPlating", 1L), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.StainlessSteel, 2L));
            ////GT_Values.RA.addImplosionRecipe(ItemList.Ingot_Heavy2.get(1L, new Object[0]), 8, GT_ModHandler.getModItem("GalacticraftMars", "item.null", 1L, 3), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Tungsten, 2L));
            ////GT_Values.RA.addImplosionRecipe(ItemList.Ingot_Heavy3.get(1L, new Object[0]), 8, GT_ModHandler.getModItem("GalacticraftMars", "item.itemBasicAsteroids", 1L), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.TungstenSteel, 2L));

            //GT_Values.RA.addCentrifugeRecipe(GT_ModHandler.getModItem("GalacticraftCore", "tile.moonBlock", 1L, 5), null, null, Materials.Helium_3.getGas(33), new ItemStack(Blocks.sand,1), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 1), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcite, 1), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 1), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnesium, 1), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Rutile, 1), new int[]{5000,400,400,100,100,100}, 400, 8);
            //GT_Values.RA.addPulveriserRecipe(GT_ModHandler.getModItem("GalacticraftCore", "tile.moonBlock", 1L, 4), new ItemStack[]{GT_ModHandler.getModItem("GalacticraftCore", "tile.moonBlock", 1L, 5)}, null, 400, 2);
            //GT_Values.RA.addFluidExtractionRecipe(GT_ModHandler.getModItem("GalacticraftMars", "tile.mars", 1L, 9), new ItemStack(Blocks.stone, 1), Materials.Iron.getMolten(50), 10000, 250, 16);
            //GT_Values.RA.addPulveriserRecipe(GT_ModHandler.getModItem("GalacticraftMars", "tile.asteroidsBlock", 1L, 1), new ItemStack[]{GT_ModHandler.getModItem("GalacticraftMars", "tile.asteroidsBlock", 1L, 0)}, null, 400, 2);
            //GT_Values.RA.addPulveriserRecipe(GT_ModHandler.getModItem("GalacticraftMars", "tile.asteroidsBlock", 1L, 2), new ItemStack[]{GT_ModHandler.getModItem("GalacticraftMars", "tile.asteroidsBlock", 1L, 0)}, null, 400, 2);
            //GT_Values.RA.addCentrifugeRecipe(GT_ModHandler.getModItem("GalacticraftMars", "tile.asteroidsBlock", 1L, 0), null, null, Materials.Nitrogen.getGas(33), new ItemStack(Blocks.sand,1), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 1), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 1), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 1), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gallium, 1), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Platinum, 1), new int[]{5000,400,400,100,100,100}, 400, 8);

            GT_Values.RA.addAssemblerRecipe(GT_ModHandler.getModItem("GalacticraftCore", "item.heavyPlating", 1L), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.MeteoricIron, 2L), Materials.Titanium.getMolten(72L), ItemList.Ingot_Heavy2.get(1L), 600, 256);
            GT_Values.RA.addAssemblerRecipe(GT_ModHandler.getModItem("GalacticraftMars", "item.null", 1L, 3), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Desh, 4L), Materials.TungstenSteel.getMolten(144L), ItemList.Ingot_Heavy3.get(1L), 1200, 480);
            GT_Values.RA.addImplosionRecipe(ItemList.Ingot_Heavy1.get(1L), 8, GT_ModHandler.getModItem("GalacticraftCore", "item.heavyPlating", 1L), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.StainlessSteel, 2L));
            GT_Values.RA.addImplosionRecipe(ItemList.Ingot_Heavy2.get(1L), 16, GT_ModHandler.getModItem("GalacticraftMars", "item.null", 1L, 3), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Titanium, 4L));
            GT_Values.RA.addImplosionRecipe(ItemList.Ingot_Heavy3.get(1L), 24, GT_ModHandler.getModItem("GalacticraftMars", "item.itemBasicAsteroids", 1L), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.TungstenSteel, 6L));
        }

        GT_Values.RA.addFluidExtractionRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Quartzite, 1L), null, Materials.Glass.getMolten(72), 10000, 600, 28);//(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.SiliconDioxide,1L), GT_OreDictUnificator.get(OrePrefixes.dust,Materials.SiliconDioxide,2L),GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Glass,1L)/** GT_Utility.fillFluidContainer(Materials.Glass.getMolten(1000), ItemList.Cell_Empty.get(1, new Object[0]), true, true)**/, 600, 16);

        GT_Values.RA.addDistillationTowerRecipe(Materials.Creosote.getFluid(1000L), new FluidStack[]{Materials.Lubricant.getFluid(500L)}, null, 400, 120);
        GT_Values.RA.addDistillationTowerRecipe(Materials.SeedOil.getFluid(1400L), new FluidStack[]{Materials.Lubricant.getFluid(500L)}, null, 400, 120);
        GT_Values.RA.addDistillationTowerRecipe(Materials.FishOil.getFluid(1200L), new FluidStack[]{Materials.Lubricant.getFluid(500L)}, null, 400, 120);
        GT_Values.RA.addDistillationTowerRecipe(Materials.Biomass.getFluid(600L), new FluidStack[]{Materials.Ethanol.getFluid(240L), Materials.Water.getFluid(240L)}, GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Wood, 1L), 16, 400);
        GT_Values.RA.addDistillationTowerRecipe(Materials.Water.getFluid(288L), new FluidStack[]{GT_ModHandler.getDistilledWater(260L)}, null, 16, 64);

        if(!GregTech_API.mIC2Classic){
            GT_Values.RA.addDistillationTowerRecipe(new FluidStack(FluidRegistry.getFluid("ic2biomass"), 3000), new FluidStack[]{new FluidStack(FluidRegistry.getFluid("ic2biogas"), 8000), Materials.Water.getFluid(125L)}, ItemList.IC2_Fertilizer.get(1, new Object[0]), 250, 480);
            GT_Values.RA.addFuel(GT_ModHandler.getIC2Item("biogasCell", 1L), null, 40, 1);

            GT_Values.RA.addDistilleryRecipe(ItemList.Circuit_Integrated.getWithDamage(0L, 1L, new Object[0]), new FluidStack(FluidRegistry.getFluid("ic2biomass"), 20), new FluidStack(FluidRegistry.getFluid("ic2biogas"), 32), 40, 16, false);
            GT_Values.RA.addDistilleryRecipe(ItemList.Circuit_Integrated.getWithDamage(0L, 2L, new Object[0]), new FluidStack(FluidRegistry.getFluid("ic2biomass"), 4), Materials.Water.getFluid(2), 80, 30, false);
        }

        GT_Values.RA.addFuel(new ItemStack(Items.golden_apple, 1, 1), new ItemStack(Items.apple, 1), 6400, 5);
        GT_Values.RA.addFuel(GT_ModHandler.getModItem("Thaumcraft", "ItemShard", 1L, 6), null, 720, 5);
        GT_Values.RA.addFuel(GT_ModHandler.getModItem("ForbiddenMagic", "GluttonyShard", 1L), null, 720, 5);
        GT_Values.RA.addFuel(GT_ModHandler.getModItem("ForbiddenMagic", "FMResource", 1L, 3), null, 720, 5);
        GT_Values.RA.addFuel(GT_ModHandler.getModItem("ForbiddenMagic", "NetherShard", 1L), null, 720, 5);
        GT_Values.RA.addFuel(GT_ModHandler.getModItem("ForbiddenMagic", "NetherShard", 1L, 1), null, 720, 5);
        GT_Values.RA.addFuel(GT_ModHandler.getModItem("ForbiddenMagic", "NetherShard", 1L, 2), null, 720, 5);
        GT_Values.RA.addFuel(GT_ModHandler.getModItem("ForbiddenMagic", "NetherShard", 1L, 3), null, 720, 5);
        GT_Values.RA.addFuel(GT_ModHandler.getModItem("ForbiddenMagic", "NetherShard", 1L, 4), null, 720, 5);
        GT_Values.RA.addFuel(GT_ModHandler.getModItem("ForbiddenMagic", "NetherShard", 1L, 5), null, 720, 5);
        GT_Values.RA.addFuel(GT_ModHandler.getModItem("ForbiddenMagic", "NetherShard", 1L, 6), null, 720, 5);
        GT_Values.RA.addFuel(GT_ModHandler.getModItem("TaintedMagic", "WarpedShard", 1L), null, 720, 5);
        GT_Values.RA.addFuel(GT_ModHandler.getModItem("TaintedMagic", "FluxShard", 1L), null, 720, 5);
        GT_Values.RA.addFuel(GT_ModHandler.getModItem("TaintedMagic", "EldritchShard", 1L), null, 720, 5);
        GT_Values.RA.addFuel(GT_ModHandler.getModItem("ThaumicTinkerer", "kamiResource", 1L, 6), null, 720, 5);
        GT_Values.RA.addFuel(GT_ModHandler.getModItem("ThaumicTinkerer", "kamiResource", 1L, 7), null, 720, 5);

        GT_Values.RA.addElectrolyzerRecipe(GT_Utility.getIntegratedCircuit(1), ItemList.Cell_Empty.get(1L, new Object[0]), Materials.Water.getFluid(3000L), Materials.Hydrogen.getGas(2000L), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 2000, 30);
        GT_Values.RA.addElectrolyzerRecipe(GT_Utility.getIntegratedCircuit(2), ItemList.Cell_Empty.get(1L, new Object[0]), GT_ModHandler.getDistilledWater(3000L), Materials.Hydrogen.getGas(2000L), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 2000, 30);
        GT_Values.RA.addElectrolyzerRecipe(GT_Utility.getIntegratedCircuit(3), ItemList.Cell_Empty.get(2L, new Object[0]), Materials.Water.getFluid(3000L), Materials.Oxygen.getGas(1000L), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 2L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 2000, 30);
        GT_Values.RA.addElectrolyzerRecipe(GT_Utility.getIntegratedCircuit(4), ItemList.Cell_Empty.get(2L, new Object[0]), GT_ModHandler.getDistilledWater(3000L), Materials.Oxygen.getGas(1000L), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 2L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 2000, 30);
        GT_Values.RA.addElectrolyzerRecipe(GT_ModHandler.getIC2Item("electrolyzedWaterCell", 3L), 0, GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 2L), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 30, 30);
        GT_Values.RA.addElectrolyzerRecipe(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1L), 0, GT_ModHandler.getIC2Item("electrolyzedWaterCell", 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, 490, 30);
        GT_Values.RA.addElectrolyzerRecipe(ItemList.Dye_Bonemeal.get(3L, new Object[0]), 0, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, 98, 26);
        GT_Values.RA.addElectrolyzerRecipe(new ItemStack(Blocks.sand, 8), 0, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, 500, 25);
        GT_Values.RA.addElectrolyzerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Tungstate, 7L),  GT_Utility.getIntegratedCircuit(1), Materials.Hydrogen.getGas(7000L), Materials.Oxygen.getGas(4000L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Tungsten, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lithium, 2L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{10000, 10000, 0, 0, 0, 0}, 120, 1920);
        GT_Values.RA.addElectrolyzerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Scheelite, 7L),  GT_Utility.getIntegratedCircuit(1), Materials.Hydrogen.getGas(7000L), Materials.Oxygen.getGas(4000L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Tungsten, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 2L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{10000, 10000, 0, 0, 0, 0}, 120, 1920);
        GT_Values.RA.addElectrolyzerRecipe(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.CarbonDioxide, 4),  GT_Utility.getIntegratedCircuit(1), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 3), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 1), ItemList.Cell_Empty.get(3, new Object[0]), GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{10000, 10000, 10000, 0, 0, 0}, 180, 60);
        GT_Values.RA.addElectrolyzerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Graphite, 1), 0, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 4), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, 100, 64);

        GT_Values.RA.addElectrolyzerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sphalerite, 2), GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Zinc, 1), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Gallium, 1), GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{10000, 10000, 2500, 0, 0, 0},200, 30);

        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.NetherQuartz, 3L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L), Materials.Water.getFluid(1000L), GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.gem, Materials.NetherQuartz, 3L), 500);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.CertusQuartz, 3L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L), Materials.Water.getFluid(1000L), GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.gem, Materials.CertusQuartz, 3L), 500);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Quartzite, 3L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L), Materials.Water.getFluid(1000L), GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Quartzite, 3L), 500);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.NetherQuartz, 3L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L), GT_ModHandler.getDistilledWater(1000L), GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.gem, Materials.NetherQuartz, 3L), 500);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.CertusQuartz, 3L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L), GT_ModHandler.getDistilledWater(1000L), GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.gem, Materials.CertusQuartz, 3L), 500);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Quartzite, 3L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L), GT_ModHandler.getDistilledWater(1000L), GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Quartzite, 3L), 500);

        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Uraninite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 1L), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Uranium, 1L), 1000);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Uraninite, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnesium, 1L), GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Uranium, 1L), 1000);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 1L), Materials.Oxygen.getGas(3000L), GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcite, 5L), 500);
        GT_Values.RA.addChemicalRecipe(Materials.Carbon.getDust(1), GT_Utility.getIntegratedCircuit(1),  Materials.Hydrogen.getGas(4000L), Materials.Methane.getGas(1000L), GT_Values.NI, 200);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Carbon.getDust(1), Materials.Empty.getCells(1),         Materials.Hydrogen.getGas(4000L), GT_Values.NF, Materials.Methane.getCells(1), GT_Values.NI, 200, 30);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 1L), GT_Values.NI, Materials.Hydrogen.getGas(2000L), GT_ModHandler.getDistilledWater(1000L), ItemList.Cell_Empty.get(1L, new Object[0]), GT_Values.NI, 10, 30);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 1L), GT_Values.NI, Materials.Oxygen.getGas(500L), GT_ModHandler.getDistilledWater(500L), ItemList.Cell_Empty.get(1L, new Object[0]), GT_Values.NI, 5, 30);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(1)}, new FluidStack[]{Materials.Hydrogen.getGas(2000), Materials.Oxygen.getGas(1000)}, new FluidStack[]{GT_ModHandler.getDistilledWater(1000)}, new ItemStack[]{}, 10, 30);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Tin, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Saltpeter, 1L), Materials.Glass.getMolten(864L), GT_Values.NF, GT_ModHandler.getModItem("Railcraft", "tile.railcraft.glass", 6L), 50);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Rutile, 1L), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Carbon, 2L), Materials.Chlorine.getGas(4000L), Materials.Titaniumtetrachloride.getFluid(1000L), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.CarbonMonoxide, 2L), 500, 480);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Rutile, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 2L), Materials.Chlorine.getGas(4000L), Materials.Titaniumtetrachloride.getFluid(1000L), GT_Values.NI, 500, 480);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnesiumchloride, 2L), GT_Values.NF, Materials.Chlorine.getGas(3000L), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Magnesium, 6L), 300, 240);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RawRubber, 9L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), GT_Values.NF, Materials.Rubber.getMolten(1296L), GT_Values.NI, 600, 16);

        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Gold, 8L), new ItemStack(Items.melon, 1, 32767), new ItemStack(Items.speckled_melon, 1, 0), 50);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Gold, 8L), new ItemStack(Items.carrot, 1, 32767), new ItemStack(Items.golden_carrot, 1, 0), 50);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Gold, 8L), new ItemStack(Items.apple, 1, 32767), new ItemStack(Items.golden_apple, 1, 0), 50);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.block, Materials.Gold, 8L), new ItemStack(Items.apple, 1, 32767), new ItemStack(Items.golden_apple, 1, 1), 50);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Blaze, 1L), GT_OreDictUnificator.get(OrePrefixes.gem, Materials.EnderPearl, 1L), GT_OreDictUnificator.get(OrePrefixes.gem, Materials.EnderEye, 1L), 200, 480);
        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Blaze, 1L), new ItemStack(Items.slime_ball, 1, 32767), new ItemStack(Items.magma_cream, 1, 0), 50);

        GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Plutonium, 8), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Uranium, 1), Materials.Air.getGas(1000), Materials.Radon.getGas(100), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Plutonium, 8), 12000, 8);
        GT_Values.RA.addChemicalBathRecipe(GT_OreDictUnificator.get(OrePrefixes.gem, Materials.EnderEye, 1), Materials.Radon.getGas(250), ItemList.QuantumEye.get(1L, new Object[0]), null, null, null, 480, 384);
        GT_Values.RA.addChemicalBathRecipe(GT_OreDictUnificator.get(OrePrefixes.gem, Materials.NetherStar, 1), Materials.Radon.getGas(1250), ItemList.QuantumStar.get(1L, new Object[0]), null, null, null, 1920, 384);
        GT_Values.RA.addAutoclaveRecipe(GT_OreDictUnificator.get(ItemList.QuantumStar.get(1L)), Materials.Neutronium.getMolten(288), ItemList.Gravistar.get(1L, new Object[0]), 10000, 480, 7680);

        GT_Values.RA.addBenderRecipe(ItemList.IC2_Mixed_Metal_Ingot.get(1L, new Object[0]), GT_OreDictUnificator.get(OrePrefixes.plateAlloy, Materials.Advanced, 1L), 100, 8);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Aluminium, 6L), ItemList.RC_Rail_Standard.get(2L, new Object[0]), 200, 15);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Iron, 6L), ItemList.RC_Rail_Standard.get(4L, new Object[0]), 400, 15);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.WroughtIron, 6L), ItemList.RC_Rail_Standard.get(5L, new Object[0]), 400, 15);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Bronze, 6L), ItemList.RC_Rail_Standard.get(3L, new Object[0]), 300, 15);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 6L), ItemList.RC_Rail_Standard.get(8L, new Object[0]), 500, 24);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.StainlessSteel, 6L), ItemList.RC_Rail_Standard.get(12L, new Object[0]), 800, 32);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Titanium, 6L), ItemList.RC_Rail_Standard.get(16L, new Object[0]), 1000, 32);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.TungstenSteel, 6L), ItemList.RC_Rail_Standard.get(20L, new Object[0]), 1200, 32);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Iridium, 6L), ItemList.RC_Rail_Standard.get(24L, new Object[0]), 1400, 32);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.StainlessSteel, 12L), ItemList.RC_Rail_Reinforced.get(24L), 1200, 64);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.TungstenSteel, 6L), ItemList.RC_Rail_Reinforced.get(24L, new Object[0]), 900, 64);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Iridium, 3L), ItemList.RC_Rail_Reinforced.get(24L), 600, 64);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Aluminium, 12L), ItemList.RC_Rebar.get(4L, new Object[0]), 200, 15);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Iron, 12L), ItemList.RC_Rebar.get(8L, new Object[0]), 400, 15);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.WroughtIron, 12L), ItemList.RC_Rebar.get(10L, new Object[0]), 400, 15);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Bronze, 12L), ItemList.RC_Rebar.get(8L, new Object[0]), 400, 15);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 12L), ItemList.RC_Rebar.get(16L, new Object[0]), 800, 15);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.StainlessSteel, 12L), ItemList.RC_Rebar.get(24L, new Object[0]), 1200, 15);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Titanium, 12L), ItemList.RC_Rebar.get(32L, new Object[0]), 1600, 15);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.TungstenSteel, 12L), ItemList.RC_Rebar.get(48L, new Object[0]), 2400, 15);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Iridium, 8L), ItemList.RC_Rebar.get(64L), 2400, 15);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Tin, 12L), ItemList.Cell_Empty.get(6L, new Object[0]), 1200, 8);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 12L), ItemList.Cell_Empty.get(6L, new Object[0]), 1200, 8);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.SteelMagnetic, 12L), ItemList.Cell_Empty.get(6L, new Object[0]), 1200, 8);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Polytetrafluoroethylene, 12L), ItemList.Cell_Empty.get(6L, new Object[0]), 1200, 8);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 12L), new ItemStack(Items.bucket, 4, 0), 800, 4);
        GT_Values.RA.addBenderRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 12L), new ItemStack(Items.bucket, 4, 0), 800, 4);
        GT_Values.RA.addBenderRecipe(ItemList.IC2_Item_Casing_Iron.get(2L, new Object[0]), GT_ModHandler.getIC2Item("fuelRod", 1L), 100, 8);
        GT_Values.RA.addBenderRecipe(ItemList.IC2_Item_Casing_Tin.get(1L, new Object[0]), ItemList.IC2_Food_Can_Empty.get(1L, new Object[0]), 100, 8);
        GT_Values.RA.addPulveriserRecipe(GT_OreDictUnificator.get(OrePrefixes.block, Materials.Marble, 1L), new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Marble, 1L)}, null, 160, 4);

        GT_Values.RA.addVacuumFreezerRecipe(GT_ModHandler.getIC2Item("reactorHeatSwitch", 1L, 32767), GT_ModHandler.getIC2Item("reactorHeatSwitch", 1L, 1), 100);
        GT_Values.RA.addVacuumFreezerRecipe(GT_ModHandler.getIC2Item("reactorHeatSwitchCore", 1L, 32767), GT_ModHandler.getIC2Item("reactorHeatSwitchCore", 1L, 1), 100);
        GT_Values.RA.addVacuumFreezerRecipe(GT_ModHandler.getIC2Item("reactorHeatSwitchSpread", 1L, 32767), GT_ModHandler.getIC2Item("reactorHeatSwitchSpread", 1L, 1), 100);
        GT_Values.RA.addVacuumFreezerRecipe(GT_ModHandler.getIC2Item("reactorHeatSwitchDiamond", 1L, 32767), GT_ModHandler.getIC2Item("reactorHeatSwitchDiamond", 1L, 1), 100);

        GT_Values.RA.addVacuumFreezerRecipe(GT_ModHandler.getIC2Item("reactorVent", 1L, 32767), GT_ModHandler.getIC2Item("reactorVent", 1L, 1), 100);
        GT_Values.RA.addVacuumFreezerRecipe(GT_ModHandler.getIC2Item("reactorVentCore", 1L, 32767), GT_ModHandler.getIC2Item("reactorVentCore", 1L, 1), 100);
        GT_Values.RA.addVacuumFreezerRecipe(GT_ModHandler.getIC2Item("reactorVentGold", 1L, 32767), GT_ModHandler.getIC2Item("reactorVentGold", 1L, 1), 100);
        GT_Values.RA.addVacuumFreezerRecipe(GT_ModHandler.getIC2Item("reactorVentDiamond", 1L, 32767), GT_ModHandler.getIC2Item("reactorVentDiamond", 1L, 1), 100);

        GT_Values.RA.addVacuumFreezerRecipe(GT_ModHandler.getIC2Item("reactorVentSpread", 1L, 32767), GT_ModHandler.getIC2Item("reactorVentSpread", 1L, 0), 100);

        GT_Values.RA.addVacuumFreezerRecipe(GT_ModHandler.getIC2Item("reactorCoolantSimple", 1L, 32767), GT_ModHandler.getIC2Item("reactorCoolantSimple", 1L, 1), 100);
        GT_Values.RA.addVacuumFreezerRecipe(GT_ModHandler.getIC2Item("reactorCoolantTriple", 1L, 32767), GT_ModHandler.getIC2Item("reactorCoolantTriple", 1L, 1), 300);
        GT_Values.RA.addVacuumFreezerRecipe(GT_ModHandler.getIC2Item("reactorCoolantSix", 1L, 32767), GT_ModHandler.getIC2Item("reactorCoolantSix", 1L, 1), 600);
        GT_Values.RA.addVacuumFreezerRecipe(ItemList.Reactor_Coolant_He_1.getWildcard(1L, new Object[0]), ItemList.Reactor_Coolant_He_1.get(1L, new Object[0]), 600);
        GT_Values.RA.addVacuumFreezerRecipe(ItemList.Reactor_Coolant_He_3.getWildcard(1L, new Object[0]), ItemList.Reactor_Coolant_He_3.get(1L, new Object[0]), 1800);
        GT_Values.RA.addVacuumFreezerRecipe(ItemList.Reactor_Coolant_He_6.getWildcard(1L, new Object[0]), ItemList.Reactor_Coolant_He_6.get(1L, new Object[0]), 3600);
        GT_Values.RA.addVacuumFreezerRecipe(ItemList.Reactor_Coolant_NaK_1.getWildcard(1L, new Object[0]), ItemList.Reactor_Coolant_NaK_1.get(1L, new Object[0]), 600);
        GT_Values.RA.addVacuumFreezerRecipe(ItemList.Reactor_Coolant_NaK_3.getWildcard(1L, new Object[0]), ItemList.Reactor_Coolant_NaK_3.get(1L, new Object[0]), 1800);
        GT_Values.RA.addVacuumFreezerRecipe(ItemList.Reactor_Coolant_NaK_6.getWildcard(1L, new Object[0]), ItemList.Reactor_Coolant_NaK_6.get(1L, new Object[0]), 3600);
        GT_Values.RA.addVacuumFreezerRecipe(ItemList.neutroniumHeatCapacitor.getWildcard(1L), ItemList.neutroniumHeatCapacitor.get(1L), 10000000);
        GT_Values.RA.addVacuumFreezerRecipe(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1L), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Ice, 1L), 50);
        GT_Values.RA.addVacuumFreezerRecipe(GT_ModHandler.getIC2Item("airCell", 1L), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.LiquidAir, 1L), 25);

        GT_Values.RA.addVacuumFreezerRecipe(GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.Pentacadmiummagnesiumhexaoxid, 1L), GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Pentacadmiummagnesiumhexaoxid, 1L), 9000, 480);
        GT_Values.RA.addVacuumFreezerRecipe(GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.Titaniumonabariumdecacoppereikosaoxid, 1L), GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Titaniumonabariumdecacoppereikosaoxid, 1L), 9000, 480);
        GT_Values.RA.addVacuumFreezerRecipe(GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.Uraniumtriplatinid, 1L), GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Uraniumtriplatinid, 1L), 12000, 480);
        GT_Values.RA.addVacuumFreezerRecipe(GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.Vanadiumtriindinid, 1L), GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Vanadiumtriindinid, 1L), 12000, 480);
        GT_Values.RA.addVacuumFreezerRecipe(GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid, 1L), GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid, 1L), 15000, 480);
        GT_Values.RA.addVacuumFreezerRecipe(GT_OreDictUnificator.get(OrePrefixes.ingotHot, Materials.Tetranaquadahdiindiumhexaplatiumosminid, 1L), GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Tetranaquadahdiindiumhexaplatiumosminid, 1L), 15000, 480);

        GT_Values.RA.addAlloySmelterRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lead, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Obsidian, 2L), ItemList.TE_Hardened_Glass.get(2L, new Object[0]), 200, 16);
        GT_Values.RA.addAlloySmelterRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Lead, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Obsidian, 2L), ItemList.TE_Hardened_Glass.get(2L, new Object[0]), 200, 16);
        GT_Values.RA.addAlloySmelterRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RawRubber, 3L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Rubber, 1L), 200, 8);//We use rubber

        GT_Values.RA.addCutterRecipe(GT_ModHandler.getModItem("BuildCraft|Transport", "item.buildcraftPipe.pipestructurecobblestone", 1L, 0), GT_ModHandler.getModItem("BuildCraft|Transport", "pipePlug", 8L, 0), GT_Values.NI, 32, 16);
        for (int i = 0; i < 16; i++) {
            GT_Values.RA.addCutterRecipe(new ItemStack(Blocks.stained_glass, 3, i), new ItemStack(Blocks.stained_glass_pane, 8, i), GT_Values.NI, 50, 8);
        }
        GT_Values.RA.addCutterRecipe(new ItemStack(Blocks.glass, 3, 0), new ItemStack(Blocks.glass_pane, 8, 0), GT_Values.NI, 50, 8);
        GT_Values.RA.addCutterRecipe(new ItemStack(Blocks.stone, 1, 0), new ItemStack(Blocks.stone_slab, 2, 0), GT_Values.NI, 25, 8);
        GT_Values.RA.addCutterRecipe(new ItemStack(Blocks.sandstone, 1, 0), new ItemStack(Blocks.stone_slab, 2, 1), GT_Values.NI, 25, 8);
        GT_Values.RA.addCutterRecipe(new ItemStack(Blocks.cobblestone, 1, 0), new ItemStack(Blocks.stone_slab, 2, 3), GT_Values.NI, 25, 8);
        GT_Values.RA.addCutterRecipe(new ItemStack(Blocks.brick_block, 1, 0), new ItemStack(Blocks.stone_slab, 2, 4), GT_Values.NI, 25, 8);
        GT_Values.RA.addCutterRecipe(new ItemStack(Blocks.stonebrick, 1, 0), new ItemStack(Blocks.stone_slab, 2, 5), GT_Values.NI, 25, 8);
        GT_Values.RA.addCutterRecipe(new ItemStack(Blocks.nether_brick, 1, 0), new ItemStack(Blocks.stone_slab, 2, 6), GT_Values.NI, 25, 8);
        GT_Values.RA.addCutterRecipe(new ItemStack(Blocks.quartz_block, 1, 32767), new ItemStack(Blocks.stone_slab, 2, 7), GT_Values.NI, 25, 8);
        GT_Values.RA.addCutterRecipe(new ItemStack(Blocks.glowstone, 1, 0), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Glowstone, 4L), GT_Values.NI, 100, 16);

        GT_Values.RA.addCutterRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 1L), ItemList.IC2_Item_Casing_Iron.get(2L, new Object[0]), GT_Values.NI, 50, 16);
        GT_Values.RA.addCutterRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 1L), ItemList.IC2_Item_Casing_Iron.get(2L, new Object[0]), GT_Values.NI, 50, 16);
        GT_Values.RA.addCutterRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Gold, 1L), ItemList.IC2_Item_Casing_Gold.get(2L, new Object[0]), GT_Values.NI, 50, 16);
        GT_Values.RA.addCutterRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Bronze, 1L), ItemList.IC2_Item_Casing_Bronze.get(2L, new Object[0]), GT_Values.NI, 50, 16);
        GT_Values.RA.addCutterRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Copper, 1L), ItemList.IC2_Item_Casing_Copper.get(2L, new Object[0]), GT_Values.NI, 50, 16);
        GT_Values.RA.addCutterRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.AnnealedCopper, 1L), ItemList.IC2_Item_Casing_Copper.get(2L, new Object[0]), GT_Values.NI, 50, 16);
        GT_Values.RA.addCutterRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Tin, 1L), ItemList.IC2_Item_Casing_Tin.get(2L, new Object[0]), GT_Values.NI, 50, 16);
        GT_Values.RA.addCutterRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Lead, 1L), ItemList.IC2_Item_Casing_Lead.get(2L, new Object[0]), GT_Values.NI, 50, 16);
        GT_Values.RA.addCutterRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 1L), ItemList.IC2_Item_Casing_Steel.get(2L, new Object[0]), GT_Values.NI, 50, 16);
        for (byte i = 0; i < 16; i = (byte) (i + 1)) {
            GT_Values.RA.addCutterRecipe(new ItemStack(Blocks.wool, 1, i), new ItemStack(Blocks.carpet, 2, i), GT_Values.NI, 50, 8);
        }
        //GT_Values.RA.addCutterRecipe(new ItemStack(Blocks.wooden_slab, 1, 0), new ItemStack(Blocks.wooden_pressure_plate, 2, 0), GT_Values.NI, 50, 8);
        GT_Values.RA.addCutterRecipe(new ItemStack(Blocks.wooden_slab, 1, 0), ItemList.Plank_Oak.get(2L, new Object[0]), GT_Values.NI, 50, 8);
        GT_Values.RA.addCutterRecipe(new ItemStack(Blocks.wooden_slab, 1, 1), ItemList.Plank_Spruce.get(2L, new Object[0]), GT_Values.NI, 50, 8);
        GT_Values.RA.addCutterRecipe(new ItemStack(Blocks.wooden_slab, 1, 2), ItemList.Plank_Birch.get(2L, new Object[0]), GT_Values.NI, 50, 8);
        GT_Values.RA.addCutterRecipe(new ItemStack(Blocks.wooden_slab, 1, 3), ItemList.Plank_Jungle.get(2L, new Object[0]), GT_Values.NI, 50, 8);
        GT_Values.RA.addCutterRecipe(new ItemStack(Blocks.wooden_slab, 1, 4), ItemList.Plank_Acacia.get(2L, new Object[0]), GT_Values.NI, 50, 8);
        GT_Values.RA.addCutterRecipe(new ItemStack(Blocks.wooden_slab, 1, 5), ItemList.Plank_DarkOak.get(2L, new Object[0]), GT_Values.NI, 50, 8);
        boolean loaded = Loader.isModLoaded(aTextForestry);//TODO OW YEAH NEW PLANK GEN CODE!!!
        ItemStack[] coverIDs = new ItemStack[]{
        		ItemList.Plank_Larch.get(2L, new Object[0]), 
        		ItemList.Plank_Teak.get(2L, new Object[0]), 
        		ItemList.Plank_Acacia_Green.get(2L, new Object[0]), 
        		ItemList.Plank_Lime.get(2L, new Object[0]), 
        		ItemList.Plank_Chestnut.get(2L, new Object[0]), 
        		ItemList.Plank_Wenge.get(2L, new Object[0]), 
        		ItemList.Plank_Baobab.get(2L, new Object[0]), 
        		ItemList.Plank_Sequoia.get(2L, new Object[0]),              
        		ItemList.Plank_Kapok.get(2L, new Object[0]), 
        		ItemList.Plank_Ebony.get(2L, new Object[0]), 
        		ItemList.Plank_Mahagony.get(2L, new Object[0]), 
        		ItemList.Plank_Balsa.get(2L, new Object[0]), 
        		ItemList.Plank_Willow.get(2L, new Object[0]), 
        		ItemList.Plank_Walnut.get(2L, new Object[0]), 
        		ItemList.Plank_Greenheart.get(2L, new Object[0]), 
        		ItemList.Plank_Cherry.get(2L, new Object[0]), 
        		ItemList.Plank_Mahoe.get(2L, new Object[0]), 
        		ItemList.Plank_Poplar.get(2L, new Object[0]), 
        		ItemList.Plank_Palm.get(2L, new Object[0]), 
        		ItemList.Plank_Papaya.get(2L, new Object[0]), 
        		ItemList.Plank_Pine.get(2L, new Object[0]), 
        		ItemList.Plank_Plum.get(2L, new Object[0]), 
        		ItemList.Plank_Maple.get(2L, new Object[0]), 
        		ItemList.Plank_Citrus.get(2L, new Object[0])};
        int i = 0;
        for (ItemStack cover : coverIDs) {
            if (loaded) {
                ItemStack slabWood = GT_ModHandler.getModItem(aTextForestry, "slabs", 1, i);
                ItemStack slabWoodFireproof = GT_ModHandler.getModItem(aTextForestry, "slabsFireproof", 1, i);
                GT_ModHandler.addCraftingRecipe(cover, GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"s ", " P", 'P', slabWood});
                GT_ModHandler.addCraftingRecipe(cover, GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"s ", " P", 'P', slabWoodFireproof});
                GT_Values.RA.addCutterRecipe(slabWood, cover, null, 40, 8);
                GT_Values.RA.addCutterRecipe(slabWoodFireproof, cover, null, 40, 8);
            } else if (isNEILoaded) {
                API.hideItem(cover);
            }
            i++;
        }
        for (int g = 0; g < 16; g++) {
            if (!isNEILoaded) {
                break;
            }
            API.hideItem(new ItemStack(GT_MetaGenerated_Item_03.INSTANCE, 1, g));
        }
    }
    public void run2() {

        GT_Values.RA.addLatheRecipe(new ItemStack(Blocks.wooden_slab, 1, GT_Values.W), new ItemStack(Items.bowl,1), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Wood, 1), 50, 8);
        GT_Values.RA.addLatheRecipe(GT_ModHandler.getModItem(aTextForestry, "slabs", 1L, GT_Values.W), new ItemStack(Items.bowl,1), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Wood, 1), 50, 8);
        GT_Values.RA.addLatheRecipe(GT_ModHandler.getModItem(aTextEBXL, "woodslab", 1L, GT_Values.W), new ItemStack(Items.bowl,1), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Wood, 1), 50, 8);

        GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Cupronickel, 1L), ItemList.Shape_Mold_Credit.get(0L, new Object[0]), ItemList.Credit_Greg_Cupronickel.get(4L, new Object[0]), 100, 16);
        GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Brass, 1L), ItemList.Shape_Mold_Credit.get(0L, new Object[0]), ItemList.Coin_Doge.get(4L, new Object[0]), 100, 16);
        GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 1L), ItemList.Shape_Mold_Credit.get(0L, new Object[0]), ItemList.Credit_Iron.get(4L, new Object[0]), 100, 16);
        GT_Values.RA.addFormingPressRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 1L), ItemList.Shape_Mold_Credit.get(0L, new Object[0]), ItemList.Credit_Iron.get(4L, new Object[0]), 100, 16);

        if (!GT_Mod.gregtechproxy.mDisableIC2Cables) {
            GT_Values.RA.addWiremillRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Copper, 1L), GT_ModHandler.getIC2Item("copperCableItem", 3L), 100, 2);
            GT_Values.RA.addWiremillRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.AnnealedCopper, 1L), GT_ModHandler.getIC2Item("copperCableItem", 3L), 100, 2);
            GT_Values.RA.addWiremillRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Tin, 1L), GT_ModHandler.getIC2Item("tinCableItem", 4L), 150, 1);
            GT_Values.RA.addWiremillRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 1L), GT_ModHandler.getIC2Item("ironCableItem", 6L), 200, 2);
            GT_Values.RA.addWiremillRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 1L), GT_ModHandler.getIC2Item("ironCableItem", 6L), 200, 2);
            GT_Values.RA.addWiremillRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Gold, 1L), GT_ModHandler.getIC2Item("goldCableItem", 6L), 200, 1);
        }
        GT_Values.RA.addWiremillRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Graphene, 1L), GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Graphene, 1L), 400, 2);
        if (!GregTech_API.sRecipeFile.get(ConfigCategories.Recipes.disabledrecipes, "torchesFromCoal", false)) {
            GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1L), new ItemStack(Items.coal, 1, 32767), new ItemStack(Blocks.torch, 4), 400, 1);
        }
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Gold, 2L), GT_OreDictUnificator.get(OrePrefixes.spring, Materials.Steel, 1L), new ItemStack(Blocks.light_weighted_pressure_plate, 1), 200, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 2L), GT_OreDictUnificator.get(OrePrefixes.spring, Materials.Steel, 1L), new ItemStack(Blocks.heavy_weighted_pressure_plate, 1), 200, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 6L), ItemList.Circuit_Integrated.getWithDamage(0L, 6L, new Object[0]), new ItemStack(Items.iron_door, 1), 600, 4);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 7L), ItemList.Circuit_Integrated.getWithDamage(0L, 7L, new Object[0]), new ItemStack(Items.cauldron, 1), 700, 4);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Iron, 1L), ItemList.Circuit_Integrated.getWithDamage(0L, 1L, new Object[0]), GT_ModHandler.getIC2Item("ironFence", 1L), 100, 4);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Iron, 3L), ItemList.Circuit_Integrated.getWithDamage(0L, 3L, new Object[0]), new ItemStack(Blocks.iron_bars, 4), 300, 4);
        //GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 2L), ItemList.Circuit_Integrated.getWithDamage(0L, 2L, new Object[0]), new ItemStack(Blocks.heavy_weighted_pressure_plate, 1), 200, 4);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 6L), ItemList.Circuit_Integrated.getWithDamage(0L, 6L, new Object[0]), new ItemStack(Items.iron_door, 1), 600, 4);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 7L), ItemList.Circuit_Integrated.getWithDamage(0L, 7L, new Object[0]), new ItemStack(Items.cauldron, 1), 700, 4);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.WroughtIron, 1L), ItemList.Circuit_Integrated.getWithDamage(0L, 1L, new Object[0]), GT_ModHandler.getIC2Item("ironFence", 1L), 100, 4);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.WroughtIron, 3L), ItemList.Circuit_Integrated.getWithDamage(0L, 3L, new Object[0]), new ItemStack(Blocks.iron_bars, 4), 300, 4);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 3L), ItemList.Circuit_Integrated.getWithDamage(0L, 3L, new Object[0]), new ItemStack(Blocks.fence, 1), 300, 4);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L), GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Iron, 2L), new ItemStack(Blocks.tripwire_hook, 1), 400, 4);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L), GT_OreDictUnificator.get(OrePrefixes.ring, Materials.WroughtIron, 2L), new ItemStack(Blocks.tripwire_hook, 1), 400, 4);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 3L), new ItemStack(Items.string, 3, 32767), new ItemStack(Items.bow, 1), 400, 4);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 3L), ItemList.Component_Minecart_Wheels_Iron.get(2L, new Object[0]), new ItemStack(Items.minecart, 1), 500, 2);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 3L), ItemList.Component_Minecart_Wheels_Iron.get(2L, new Object[0]), new ItemStack(Items.minecart, 1), 400, 2);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 3L), ItemList.Component_Minecart_Wheels_Steel.get(2L, new Object[0]), new ItemStack(Items.minecart, 1), 300, 2);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Iron, 1L), GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Iron, 2L), ItemList.Component_Minecart_Wheels_Iron.get(1L, new Object[0]), 500, 2);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.WroughtIron, 1L), GT_OreDictUnificator.get(OrePrefixes.ring, Materials.WroughtIron, 2L), ItemList.Component_Minecart_Wheels_Iron.get(1L, new Object[0]), 400, 2);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 1L), GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Steel, 2L), ItemList.Component_Minecart_Wheels_Steel.get(1L, new Object[0]), 300, 2);
        GT_Values.RA.addAssemblerRecipe(new ItemStack(Items.minecart, 1), new ItemStack(Blocks.hopper, 1, 32767), new ItemStack(Items.hopper_minecart, 1), 400, 4);
        GT_Values.RA.addAssemblerRecipe(new ItemStack(Items.minecart, 1), new ItemStack(Blocks.tnt, 1, 32767), new ItemStack(Items.tnt_minecart, 1), 400, 4);
        GT_Values.RA.addAssemblerRecipe(new ItemStack(Items.minecart, 1), new ItemStack(Blocks.chest, 1, 32767), new ItemStack(Items.chest_minecart, 1), 400, 4);
        GT_Values.RA.addAssemblerRecipe(new ItemStack(Items.minecart, 1), new ItemStack(Blocks.trapped_chest, 1, 32767), new ItemStack(Items.chest_minecart, 1), 400, 4);
        GT_Values.RA.addAssemblerRecipe(new ItemStack(Items.minecart, 1), new ItemStack(Blocks.furnace, 1, 32767), new ItemStack(Items.furnace_minecart, 1), 400, 4);
        GT_Values.RA.addAssemblerRecipe(new ItemStack(Blocks.tripwire_hook, 1), new ItemStack(Blocks.chest, 1, 32767), new ItemStack(Blocks.trapped_chest, 1), 200, 4);
        GT_Values.RA.addAssemblerRecipe(new ItemStack(Blocks.stone, 1, 0), ItemList.Circuit_Integrated.getWithDamage(0L, 4L, new Object[0]), new ItemStack(Blocks.stonebrick, 1, 0), 50, 4);
        GT_Values.RA.addAssemblerRecipe(new ItemStack(Blocks.sandstone, 1, 0), ItemList.Circuit_Integrated.getWithDamage(0L, 1L, new Object[0]), new ItemStack(Blocks.sandstone, 1, 2), 50, 4);
        GT_Values.RA.addAssemblerRecipe(new ItemStack(Blocks.sandstone, 1, 1), ItemList.Circuit_Integrated.getWithDamage(0L, 1L, new Object[0]), new ItemStack(Blocks.sandstone, 1, 0), 50, 4);
        GT_Values.RA.addAssemblerRecipe(new ItemStack(Blocks.sandstone, 1, 2), ItemList.Circuit_Integrated.getWithDamage(0L, 1L, new Object[0]), new ItemStack(Blocks.sandstone, 1, 0), 50, 4);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 8L), ItemList.Circuit_Integrated.getWithDamage(0L, 8L, new Object[0]), GT_ModHandler.getIC2Item("machine", 1L), 25, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 8L), ItemList.Circuit_Integrated.getWithDamage(0L, 8L, new Object[0]), ItemList.Casing_ULV.get(1L, new Object[0]), 25, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 8L), ItemList.Circuit_Integrated.getWithDamage(0L, 8L, new Object[0]), ItemList.Casing_LV.get(1L, new Object[0]), 50, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Aluminium, 8L), ItemList.Circuit_Integrated.getWithDamage(0L, 8L, new Object[0]), ItemList.Casing_MV.get(1L, new Object[0]), 50, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.StainlessSteel, 8L), ItemList.Circuit_Integrated.getWithDamage(0L, 8L, new Object[0]), ItemList.Casing_HV.get(1L, new Object[0]), 50, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 8L), ItemList.Circuit_Integrated.getWithDamage(0L, 8L, new Object[0]), ItemList.Casing_EV.get(1L, new Object[0]), 50, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 8L), ItemList.Circuit_Integrated.getWithDamage(0L, 8L, new Object[0]), ItemList.Casing_IV.get(1L, new Object[0]), 50, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Chrome, 8L), ItemList.Circuit_Integrated.getWithDamage(0L, 8L, new Object[0]), ItemList.Casing_LuV.get(1L, new Object[0]), 50, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 8L), ItemList.Circuit_Integrated.getWithDamage(0L, 8L, new Object[0]), ItemList.Casing_ZPM.get(1L, new Object[0]), 50, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 8L), ItemList.Circuit_Integrated.getWithDamage(0L, 8L, new Object[0]), ItemList.Casing_UV.get(1L, new Object[0]), 50, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 8L), ItemList.Circuit_Integrated.getWithDamage(0L, 8L, new Object[0]), ItemList.Casing_MAX.get(1L, new Object[0]), 50, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Invar, 6L), GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Invar, 1L), ItemList.Casing_HeatProof.get(1L, new Object[0]), 50, 16);
        //GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Cupronickel, 8L), ItemList.Circuit_Integrated.getWithDamage(0L, 8L, new Object[0]), ItemList.Casing_Coil_Cupronickel.get(1L, new Object[0]), 50, 16);
        //GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Kanthal, 8L), ItemList.Circuit_Integrated.getWithDamage(0L, 8L, new Object[0]), ItemList.Casing_Coil_Kanthal.get(1L, new Object[0]), 50, 16);
        //GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Nichrome, 8L), ItemList.Circuit_Integrated.getWithDamage(0L, 8L, new Object[0]), ItemList.Casing_Coil_Nichrome.get(1L, new Object[0]), 50, 16);
        //GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.TungstenSteel, 8L), ItemList.Circuit_Integrated.getWithDamage(0L, 8L, new Object[0]), ItemList.Casing_Coil_TungstenSteel.get(1L, new Object[0]), 50, 16);
        //GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.HSSG, 8L), ItemList.Circuit_Integrated.getWithDamage(0L, 8L, new Object[0]), ItemList.Casing_Coil_HSSG.get(1L, new Object[0]), 50, 16);
        //GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Naquadah, 8L), ItemList.Circuit_Integrated.getWithDamage(0L, 8L, new Object[0]), ItemList.Casing_Coil_Naquadah.get(1L, new Object[0]), 50, 16);
        //GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.NaquadahAlloy, 8L), ItemList.Circuit_Integrated.getWithDamage(0L, 8L, new Object[0]), ItemList.Casing_Coil_NaquadahAlloy.get(1L, new Object[0]), 50, 16);
        //GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Superconductor, 8L), ItemList.Circuit_Integrated.getWithDamage(0L, 8L, new Object[0]), ItemList.Casing_Coil_Superconductor.get(1L, new Object[0]), 50, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 6L), GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Steel, 1L), ItemList.Casing_SolidSteel.get(1L, new Object[0]), 50, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Aluminium, 6L), GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Aluminium, 1L), ItemList.Casing_FrostProof.get(1L, new Object[0]), 50, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 6L), GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.TungstenSteel, 1L), ItemList.Casing_RobustTungstenSteel.get(1L, new Object[0]), 50, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.StainlessSteel, 6L), GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.StainlessSteel, 1L), ItemList.Casing_CleanStainlessSteel.get(1L, new Object[0]), 50, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 6L), GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Titanium, 1L), ItemList.Casing_StableTitanium.get(1L, new Object[0]), 50, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmiridium, 6L), GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Osmiridium, 1L), ItemList.Casing_MiningOsmiridium.get(1L, new Object[0]), 50, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.HSSS, 4L), ItemList.Casing_LuV.get(1L),  Materials.HSSG.getMolten(288), ItemList.Casing_Fusion.get(1L), 100, 7680);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Americium, 4L), ItemList.Casing_Fusion.get(1L),  Materials.NaquadahAlloy.getMolten(288), ItemList.Casing_Fusion2.get(1L), 200, 30720);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Magnalium, 6L), GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.BlueSteel, 1L), ItemList.Casing_Turbine.get(1L, new Object[0]), 50, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.StainlessSteel, 6L), ItemList.Casing_Turbine.get(1L, new Object[0]), ItemList.Casing_Turbine1.get(1L, new Object[0]), 50, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 6L), ItemList.Casing_Turbine.get(1L, new Object[0]), ItemList.Casing_Turbine2.get(1L, new Object[0]), 50, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 6L), ItemList.Casing_Turbine.get(1L, new Object[0]), ItemList.Casing_Turbine3.get(1L, new Object[0]), 50, 16);
        GT_Values.RA.addAssemblerRecipe(ItemList.Casing_SolidSteel.get(1, new Object[0]), GT_Utility.getIntegratedCircuit(6), Materials.Polytetrafluoroethylene.getMolten(216), ItemList.Casing_Chemically_Inert.get(1, new Object[0]), 50, 16);

        if (GT_Mod.gregtechproxy.mHardMachineCasings) {
            GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Lead, 2L), ItemList.Casing_ULV.get(1L, new Object[0]), Materials.Plastic.getMolten(288), ItemList.Hull_ULV.get(1L, new Object[0]), 25, 16);
            GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Tin, 2L), ItemList.Casing_LV.get(1L, new Object[0]), Materials.Plastic.getMolten(288), ItemList.Hull_LV.get(1L, new Object[0]), 50, 16);
            GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Copper, 2L), ItemList.Casing_MV.get(1L, new Object[0]), Materials.Plastic.getMolten(288), ItemList.Hull_MV.get(1L, new Object[0]), 50, 16);
            GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.AnnealedCopper, 2L), ItemList.Casing_MV.get(1L, new Object[0]), Materials.Plastic.getMolten(288), ItemList.Hull_MV.get(1L, new Object[0]), 50, 16);
            GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Gold, 2L), ItemList.Casing_HV.get(1L, new Object[0]), Materials.Plastic.getMolten(288), ItemList.Hull_HV.get(1L, new Object[0]), 50, 16);
            GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Aluminium, 2L), ItemList.Casing_EV.get(1L, new Object[0]), Materials.Plastic.getMolten(288), ItemList.Hull_EV.get(1L, new Object[0]), 50, 16);
            GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Tungsten, 2L), ItemList.Casing_IV.get(1L, new Object[0]), Materials.Plastic.getMolten(288), ItemList.Hull_IV.get(1L, new Object[0]), 50, 16);
            GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.VanadiumGallium, 2L), ItemList.Casing_LuV.get(1L, new Object[0]), Materials.Plastic.getMolten(288), ItemList.Hull_LuV.get(1L, new Object[0]), 50, 16);
            GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Naquadah, 2L), ItemList.Casing_ZPM.get(1L, new Object[0]), Materials.Polytetrafluoroethylene.getMolten(288), ItemList.Hull_ZPM.get(1L, new Object[0]), 50, 16);
            GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 2L), ItemList.Casing_UV.get(1L, new Object[0]), Materials.Polytetrafluoroethylene.getMolten(288), ItemList.Hull_UV.get(1L, new Object[0]), 50, 16);
            GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Superconductor, 2L), ItemList.Casing_MAX.get(1L, new Object[0]), Materials.Polytetrafluoroethylene.getMolten(288), ItemList.Hull_MAX.get(1L, new Object[0]), 50, 16);
        } else {
            GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Lead, 2L), ItemList.Casing_ULV.get(1L, new Object[0]),ItemList.Hull_ULV.get(1L, new Object[0]), 25, 16);
            GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Tin, 2L), ItemList.Casing_LV.get(1L, new Object[0]),ItemList.Hull_LV.get(1L, new Object[0]), 50, 16);
            GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Copper, 2L), ItemList.Casing_MV.get(1L, new Object[0]),ItemList.Hull_MV.get(1L, new Object[0]), 50, 16);
            GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.AnnealedCopper, 2L), ItemList.Casing_MV.get(1L, new Object[0]), ItemList.Hull_MV.get(1L, new Object[0]), 50, 16);
            GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Gold, 2L), ItemList.Casing_HV.get(1L, new Object[0]), ItemList.Hull_HV.get(1L, new Object[0]), 50, 16);
            GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Aluminium, 2L), ItemList.Casing_EV.get(1L, new Object[0]), ItemList.Hull_EV.get(1L, new Object[0]), 50, 16);
            GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Tungsten, 2L), ItemList.Casing_IV.get(1L, new Object[0]), ItemList.Hull_IV.get(1L, new Object[0]), 50, 16);
            GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.VanadiumGallium, 2L), ItemList.Casing_LuV.get(1L, new Object[0]), ItemList.Hull_LuV.get(1L, new Object[0]), 50, 16);
            GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Naquadah, 2L), ItemList.Casing_ZPM.get(1L, new Object[0]), ItemList.Hull_ZPM.get(1L, new Object[0]), 50, 16);
            GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 2L), ItemList.Casing_UV.get(1L, new Object[0]), ItemList.Hull_UV.get(1L, new Object[0]), 50, 16);
            GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Superconductor, 2L), ItemList.Casing_MAX.get(1L, new Object[0]), ItemList.Hull_MAX.get(1L, new Object[0]), 50, 16);
        }
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Tin, 1L), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.BatteryAlloy, 1L), Materials.Plastic.getMolten(144), ItemList.Battery_Hull_LV.get(1L, new Object[0]), 800, 1);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Copper, 2L), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.BatteryAlloy, 3L), Materials.Plastic.getMolten(432), ItemList.Battery_Hull_MV.get(1L, new Object[0]), 1600, 2);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.AnnealedCopper, 2L), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.BatteryAlloy, 3L), Materials.Plastic.getMolten(432), ItemList.Battery_Hull_MV.get(1L, new Object[0]), 1600, 2);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Gold, 4L), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.BatteryAlloy, 9L), Materials.Plastic.getMolten(1296), ItemList.Battery_Hull_HV.get(1L, new Object[0]), 3200, 4);

        GT_Values.RA.addAssemblerRecipe(new ItemStack(Items.string, 4, 32767), new ItemStack(Items.slime_ball, 1, 32767), new ItemStack(Items.lead, 2), 200, 2);
        GT_Values.RA.addAssemblerRecipe(ItemList.IC2_Compressed_Coal_Ball.get(8L, new Object[0]), new ItemStack(Blocks.brick_block, 1), ItemList.IC2_Compressed_Coal_Chunk.get(1L, new Object[0]), 400, 4);

        GT_Values.RA.addAssemblerRecipe(GT_ModHandler.getIC2Item("waterMill", 2L), ItemList.Circuit_Integrated.getWithDamage(0L, 2L, new Object[0]), GT_ModHandler.getIC2Item("generator", 1L), 6400, 8);
        GT_Values.RA.addAssemblerRecipe(GT_ModHandler.getIC2Item("batPack", 1L, 32767), ItemList.Circuit_Integrated.getWithDamage(0L, 1L, new Object[0]), ItemList.IC2_ReBattery.get(6L, new Object[0]), 800, 4);
        GT_Values.RA.addAssemblerRecipe(new ItemStack(Blocks.stone_slab, 3, 0), ItemList.RC_Rebar.get(1L, new Object[0]), ItemList.RC_Tie_Stone.get(1L, new Object[0]), 128, 8);
        GT_Values.RA.addAssemblerRecipe(new ItemStack(Blocks.stone_slab, 3, 7), ItemList.RC_Rebar.get(1L, new Object[0]), ItemList.RC_Tie_Stone.get(1L, new Object[0]), 128, 8);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Copper, 9L), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Lead, 2L), GT_Values.NF, ItemList.RC_ShuntingWire.get(4L, new Object[0]), 1600, 4);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.AnnealedCopper, 9L), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Lead, 2L), GT_Values.NF, ItemList.RC_ShuntingWire.get(4L, new Object[0]), 1600, 4);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 3L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Electrum, 3L), Materials.Blaze.getMolten(216L), ItemList.RC_Rail_HS.get(8L, new Object[0]), 400, 4);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 3L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Electrum, 3L), Materials.ConductiveIron.getMolten(432L), ItemList.RC_Rail_HS.get(8L), 400, 4);
        GT_Values.RA.addAssemblerRecipe(ItemList.RC_Rail_Standard.get(3L, new Object[0]), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Electrum, 3L), Materials.Redstone.getMolten(216L), ItemList.RC_Rail_Adv.get(8L, new Object[0]), 400, 4);
        GT_Values.RA.addAssemblerRecipe(ItemList.RC_Rail_Standard.get(1L, new Object[0]), GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Copper, 1L), ItemList.RC_Rail_Electric.get(1L, new Object[0]), 50, 4);
        GT_Values.RA.addAssemblerRecipe(ItemList.RC_Rail_Standard.get(1L, new Object[0]), GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.AnnealedCopper, 1L), ItemList.RC_Rail_Electric.get(1L, new Object[0]), 50, 4);
        GT_Values.RA.addAssemblerRecipe(ItemList.RC_Tie_Wood.get(3L, new Object[0]), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Iron, 3L), ItemList.RC_Rail_Wooden.get(12L, new Object[0]), 400, 4);
        GT_Values.RA.addAssemblerRecipe(ItemList.RC_Tie_Wood.get(3L, new Object[0]), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.WroughtIron, 3L), ItemList.RC_Rail_Wooden.get(12L, new Object[0]), 400, 4);
        GT_Values.RA.addAssemblerRecipe(ItemList.RC_Tie_Wood.get(4L, new Object[0]), ItemList.Circuit_Integrated.getWithDamage(0L, 4L, new Object[0]), ItemList.RC_Bed_Wood.get(1L, new Object[0]), 200, 4);
        GT_Values.RA.addAssemblerRecipe(ItemList.RC_Tie_Stone.get(4L, new Object[0]), ItemList.Circuit_Integrated.getWithDamage(0L, 4L, new Object[0]), ItemList.RC_Bed_Stone.get(1L, new Object[0]), 200, 4);
        for (ItemStack tRail : new ItemStack[]{ItemList.RC_Rail_Standard.get(6L, new Object[0]), ItemList.RC_Rail_Adv.get(6L, new Object[0]), ItemList.RC_Rail_Reinforced.get(6L, new Object[0]), ItemList.RC_Rail_Electric.get(6L, new Object[0]), ItemList.RC_Rail_HS.get(6L, new Object[0]), ItemList.RC_Rail_Wooden.get(6L, new Object[0])}) {
            for (ItemStack tBed : new ItemStack[]{ItemList.RC_Bed_Wood.get(1L, new Object[0]), ItemList.RC_Bed_Stone.get(1L, new Object[0])}) {
                GT_Values.RA.addAssemblerRecipe(tBed, tRail, GT_ModHandler.getRecipeOutput(new ItemStack[]{tRail, GT_Values.NI, tRail, tRail, tBed, tRail, tRail, GT_Values.NI, tRail}), 400, 4);
                GT_Values.RA.addAssemblerRecipe(tBed, tRail, Materials.Redstone.getMolten(144L), GT_ModHandler.getRecipeOutput(new ItemStack[]{tRail, GT_Values.NI, tRail, tRail, tBed, tRail, tRail, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L), tRail}), 400, 4);
                GT_Values.RA.addAssemblerRecipe(tBed, tRail, Materials.Redstone.getMolten(288L), GT_ModHandler.getRecipeOutput(new ItemStack[]{tRail, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L), tRail, tRail, tBed, tRail, tRail, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L), tRail}), 400, 4);
            }
        }
        GT_Values.RA.addAssemblerRecipe(GT_ModHandler.getIC2Item("carbonFiber", 2L), ItemList.Circuit_Integrated.getWithDamage(0L, 2L, new Object[0]), GT_ModHandler.getIC2Item("carbonMesh", 1L), 800, 2);

        GT_Values.RA.addAssemblerRecipe(ItemList.NC_SensorCard.getWildcard(1L, new Object[0]), ItemList.Circuit_Integrated.getWithDamage(0L, 1L, new Object[0]), GT_ModHandler.getIC2Item("itemPartCircuit", 3L), 1600, 2);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Aluminium, 4L), GT_ModHandler.getIC2Item("generator", 1L), GT_ModHandler.getIC2Item("waterMill", 2L), 6400, 8);

        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 5L), new ItemStack(Blocks.chest, 1, 32767), new ItemStack(Blocks.hopper), 800, 2);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 5L), new ItemStack(Blocks.trapped_chest, 1, 32767), new ItemStack(Blocks.hopper), 800, 2);

        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 5L), new ItemStack(Blocks.chest, 1, 32767), new ItemStack(Blocks.hopper), 800, 2);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 5L), new ItemStack(Blocks.trapped_chest, 1, 32767), new ItemStack(Blocks.hopper), 800, 2);

        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Magnalium, 2L), GT_ModHandler.getIC2Item("generator", 1L), GT_ModHandler.getIC2Item("windMill", 1L), 6400, 8);

        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.gem, Materials.EnderPearl, 1L), new ItemStack(Items.blaze_powder, 1, 0), new ItemStack(Items.ender_eye, 1, 0), 400, 2);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.gem, Materials.EnderPearl, 6L), new ItemStack(Items.blaze_rod, 1, 0), new ItemStack(Items.ender_eye, 6, 0), 2500, 2);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.gear, Materials.CobaltBrass, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Diamond, 1L), ItemList.Component_Sawblade_Diamond.get(1L, new Object[0]), 1600, 2);
//        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Flint, 5L), new ItemStack(Blocks.tnt, 3, 32767), GT_ModHandler.getIC2Item("industrialTnt", 5L), 800, 2);
//        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gunpowder, 4L), new ItemStack(Blocks.sand, 4, 32767), new ItemStack(Blocks.tnt, 1), 400, 1);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 4L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glowstone, 4L), new ItemStack(Blocks.redstone_lamp, 1), 400, 1);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1L), new ItemStack(Blocks.redstone_torch, 1), 400, 1);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 4L), new ItemStack(Items.compass, 1), 400, 4);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 4L), new ItemStack(Items.compass, 1), 400, 4);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Gold, 4L), new ItemStack(Items.clock, 1), 400, 4);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), new ItemStack(Blocks.torch, 2), 400, 1);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Phosphorus, 1L), new ItemStack(Blocks.torch, 6), 400, 1);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1L), ItemList.IC2_Resin.get(1L, new Object[0]), new ItemStack(Blocks.torch, 6), 400, 1);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 8L), new ItemStack(Items.flint, 1), ItemList.IC2_Compressed_Coal_Ball.get(1L, new Object[0]), 400, 4);

        if(Loader.isModLoaded("IC2NuclearControl")) {
            GT_Values.RA.addAssemblerRecipe(ItemList.NC_SensorCard.get(1L, new Object[0]), ItemList.NC_SensorCard.get(1L, new Object[0]), GT_ModHandler.getIC2Item("electronicCircuit", 4L), 200, 30);
            GT_Values.RA.addAssemblerRecipe(GT_ModHandler.getModItem("IC2NuclearControl", "ItemMultipleSensorLocationCard", 1L, 0), GT_ModHandler.getModItem("IC2NuclearControl", "ItemMultipleSensorLocationCard", 1L, 0),  GT_ModHandler.getIC2Item("electronicCircuit", 1L), 200, 30);
            GT_Values.RA.addAssemblerRecipe(GT_ModHandler.getModItem("IC2NuclearControl", "ItemMultipleSensorLocationCard", 1L, 1), GT_ModHandler.getModItem("IC2NuclearControl", "ItemMultipleSensorLocationCard", 1L, 1),  GT_ModHandler.getIC2Item("electronicCircuit", 1L), 200, 30);
            GT_Values.RA.addAssemblerRecipe(GT_ModHandler.getModItem("IC2NuclearControl", "ItemMultipleSensorLocationCard", 1L, 2), GT_ModHandler.getModItem("IC2NuclearControl", "ItemMultipleSensorLocationCard", 1L, 2),  GT_ModHandler.getIC2Item("electronicCircuit", 1L), 200, 30);
            GT_Values.RA.addAssemblerRecipe(GT_ModHandler.getModItem("IC2NuclearControl", "RFSensorCard", 1L, 0), GT_ModHandler.getModItem("IC2NuclearControl", "RFSensorCard", 1L, 0),  GT_ModHandler.getIC2Item("electronicCircuit", 1L), 200, 30);
        }

        if (!GT_Mod.gregtechproxy.mDisableIC2Cables) {
            GT_Values.RA.addAssemblerRecipe(GT_ModHandler.getIC2Item("tinCableItem", 1L), GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Rubber, 1L), GT_ModHandler.getIC2Item("insulatedTinCableItem", 1L), 100, 2);
            GT_Values.RA.addAssemblerRecipe(GT_ModHandler.getIC2Item("copperCableItem", 1L), GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Rubber, 1L), GT_ModHandler.getIC2Item("insulatedCopperCableItem", 1L), 100, 2);
            GT_Values.RA.addAssemblerRecipe(GT_ModHandler.getIC2Item("goldCableItem", 1L), GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Rubber, 2L), GT_ModHandler.getIC2Item("insulatedGoldCableItem", 1L), 200, 2);
            GT_Values.RA.addAssemblerRecipe(GT_ModHandler.getIC2Item("ironCableItem", 1L), GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Rubber, 3L), GT_ModHandler.getIC2Item("insulatedIronCableItem", 1L), 300, 2);
        }
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.toolHeadSword, Materials.Wood, 1L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1L), new ItemStack(Items.wooden_sword, 1), 100, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.toolHeadSword, Materials.Stone, 1L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1L), new ItemStack(Items.stone_sword, 1), 100, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.toolHeadSword, Materials.Iron, 1L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1L), new ItemStack(Items.iron_sword, 1), 100, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.toolHeadSword, Materials.Gold, 1L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1L), new ItemStack(Items.golden_sword, 1), 100, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.toolHeadSword, Materials.Diamond, 1L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1L), new ItemStack(Items.diamond_sword, 1), 100, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.toolHeadSword, Materials.Bronze, 1L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1L), ItemList.Tool_Sword_Bronze.getUndamaged(1L, new Object[0]), 100, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.toolHeadSword, Materials.Steel, 1L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1L), ItemList.Tool_Sword_Steel.getUndamaged(1L, new Object[0]), 100, 16);

        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.toolHeadPickaxe, Materials.Wood, 1L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L), new ItemStack(Items.wooden_pickaxe, 1), 100, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.toolHeadPickaxe, Materials.Stone, 1L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L), new ItemStack(Items.stone_pickaxe, 1), 100, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.toolHeadPickaxe, Materials.Iron, 1L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L), new ItemStack(Items.iron_pickaxe, 1), 100, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.toolHeadPickaxe, Materials.Gold, 1L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L), new ItemStack(Items.golden_pickaxe, 1), 100, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.toolHeadPickaxe, Materials.Diamond, 1L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L), new ItemStack(Items.diamond_pickaxe, 1), 100, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.toolHeadPickaxe, Materials.Bronze, 1L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L), ItemList.Tool_Pickaxe_Bronze.getUndamaged(1L, new Object[0]), 100, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.toolHeadPickaxe, Materials.Steel, 1L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L), ItemList.Tool_Pickaxe_Steel.getUndamaged(1L, new Object[0]), 100, 16);

        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.toolHeadShovel, Materials.Wood, 1L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L), new ItemStack(Items.wooden_shovel, 1), 100, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.toolHeadShovel, Materials.Stone, 1L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L), new ItemStack(Items.stone_shovel, 1), 100, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.toolHeadShovel, Materials.Iron, 1L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L), new ItemStack(Items.iron_shovel, 1), 100, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.toolHeadShovel, Materials.Gold, 1L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L), new ItemStack(Items.golden_shovel, 1), 100, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.toolHeadShovel, Materials.Diamond, 1L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L), new ItemStack(Items.diamond_shovel, 1), 100, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.toolHeadShovel, Materials.Bronze, 1L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L), ItemList.Tool_Shovel_Bronze.getUndamaged(1L, new Object[0]), 100, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.toolHeadShovel, Materials.Steel, 1L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L), ItemList.Tool_Shovel_Steel.getUndamaged(1L, new Object[0]), 100, 16);

        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.toolHeadAxe, Materials.Wood, 1L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L), new ItemStack(Items.wooden_axe, 1), 100, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.toolHeadAxe, Materials.Stone, 1L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L), new ItemStack(Items.stone_axe, 1), 100, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.toolHeadAxe, Materials.Iron, 1L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L), new ItemStack(Items.iron_axe, 1), 100, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.toolHeadAxe, Materials.Gold, 1L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L), new ItemStack(Items.golden_axe, 1), 100, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.toolHeadAxe, Materials.Diamond, 1L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L), new ItemStack(Items.diamond_axe, 1), 100, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.toolHeadAxe, Materials.Bronze, 1L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L), ItemList.Tool_Axe_Bronze.getUndamaged(1L, new Object[0]), 100, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.toolHeadAxe, Materials.Steel, 1L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L), ItemList.Tool_Axe_Steel.getUndamaged(1L, new Object[0]), 100, 16);

        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.toolHeadHoe, Materials.Wood, 1L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L), new ItemStack(Items.wooden_hoe, 1), 100, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.toolHeadHoe, Materials.Stone, 1L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L), new ItemStack(Items.stone_hoe, 1), 100, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.toolHeadHoe, Materials.Iron, 1L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L), new ItemStack(Items.iron_hoe, 1), 100, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.toolHeadHoe, Materials.Gold, 1L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L), new ItemStack(Items.golden_hoe, 1), 100, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.toolHeadHoe, Materials.Diamond, 1L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L), new ItemStack(Items.diamond_hoe, 1), 100, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.toolHeadHoe, Materials.Bronze, 1L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L), ItemList.Tool_Hoe_Bronze.getUndamaged(1L, new Object[0]), 100, 16);
        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.toolHeadHoe, Materials.Steel, 1L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 2L), ItemList.Tool_Hoe_Steel.getUndamaged(1L, new Object[0]), 100, 16);

        GT_Values.RA.addAssemblerRecipe(ItemList.NaquadahCell_1.get(2L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.TungstenSteel, 4L), ItemList.NaquadahCell_2.get(1L), 100, 400);
        GT_Values.RA.addAssemblerRecipe(ItemList.NaquadahCell_1.get(4L), GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.TungstenSteel, 6L), ItemList.NaquadahCell_4.get(1L), 150, 400);
        GT_Values.RA.addAssemblerRecipe(ItemList.NaquadahCell_2.get(2L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.TungstenSteel, 4L), ItemList.NaquadahCell_4.get(1L), 100, 400);
        GT_Values.RA.addAssemblerRecipe(ItemList.MNqCell_1.get(2L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.TungstenSteel, 4L), ItemList.MNqCell_2.get(1L), 100, 400);
        GT_Values.RA.addAssemblerRecipe(ItemList.MNqCell_1.get(4L), GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.TungstenSteel, 6L), ItemList.MNqCell_4.get(1L), 150, 400);
        GT_Values.RA.addAssemblerRecipe(ItemList.MNqCell_2.get(2L), GT_OreDictUnificator.get(OrePrefixes.stick, Materials.TungstenSteel, 4L), ItemList.MNqCell_4.get(1L), 100, 400);

        GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Neutronium, 8L), GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Iridium, 4L), ItemList.neutroniumHeatCapacitor.get(1L), 100, 120000);
		
        GT_ModHandler.removeRecipe(new ItemStack[]{new ItemStack(Items.lava_bucket), ItemList.Cell_Empty.get(1L, new Object[0])});
        GT_ModHandler.removeRecipe(new ItemStack[]{new ItemStack(Items.water_bucket), ItemList.Cell_Empty.get(1L, new Object[0])});

        GT_ModHandler.removeFurnaceSmelting(ItemList.IC2_Resin.get(1L, new Object[0]));
        if(!GregTech_API.mIC2Classic)
        GT_Values.RA.addPyrolyseRecipe(GT_ModHandler.getIC2Item("biochaff", 4), Materials.Water.getFluid(4000), 1, null, new FluidStack(FluidRegistry.getFluid("ic2biomass"), 5000), 900, 10);
        if (Loader.isModLoaded("Railcraft")) {
            GT_Values.RA.addPyrolyseRecipe(GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 16), null, 1, RailcraftToolItems.getCoalCoke(16), Materials.Creosote.getFluid(8000), 640, 64);
            GT_Values.RA.addPyrolyseRecipe(GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 16), Materials.Nitrogen.getGas(1000), 2, RailcraftToolItems.getCoalCoke(16), Materials.Creosote.getFluid(8000), 320, 96);
            GT_Values.RA.addPyrolyseRecipe(GT_OreDictUnificator.get(OrePrefixes.block, Materials.Coal, 8), null, 1, EnumCube.COKE_BLOCK.getItem(8), Materials.Creosote.getFluid(32000), 2560, 64);
            GT_Values.RA.addPyrolyseRecipe(GT_OreDictUnificator.get(OrePrefixes.block, Materials.Coal, 8), Materials.Nitrogen.getGas(1000), 2, EnumCube.COKE_BLOCK.getItem(8), Materials.Creosote.getFluid(32000), 1280, 96);
        }
        if(Loader.isModLoaded("Forestry")) {
            GT_Values.RA.addPyrolyseRecipe(GT_ModHandler.getModItem(aTextForestry, "fertilizerBio", 4L), Materials.Water.getFluid(4000), 1, null, Materials.Biomass.getFluid(5000), 900, 10);
            GT_Values.RA.addPyrolyseRecipe(GT_ModHandler.getModItem(aTextForestry, "mulch", 4L), Materials.Water.getFluid(4000), 1, null, Materials.Biomass.getFluid(5000), 900, 10);
        }

        GT_Values.RA.addCentrifugeRecipe(new ItemStack(Items.golden_apple, 1, 1), GT_Values.NI, GT_Values.NF, Materials.Methane.getGas(4608L), new ItemStack(Items.gold_ingot, 64), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 9216, 5);
        GT_Values.RA.addCentrifugeRecipe(new ItemStack(Items.golden_apple, 1, 0), GT_Values.NI, GT_Values.NF, Materials.Methane.getGas(576L), new ItemStack(Items.gold_ingot, 7), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 9216, 5);
        GT_Values.RA.addCentrifugeRecipe(new ItemStack(Items.golden_carrot, 1, 0), GT_Values.NI, GT_Values.NF, Materials.Methane.getGas(576L), new ItemStack(Items.gold_nugget, 6), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 9216, 5);
        GT_Values.RA.addCentrifugeRecipe(new ItemStack(Items.speckled_melon, 1, 0), GT_Values.NI, GT_Values.NF, Materials.Methane.getGas(576L), new ItemStack(Items.gold_nugget, 6), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 9216, 5);
        GT_Values.RA.addCentrifugeRecipe(new ItemStack(Items.mushroom_stew, 16, 0), GT_Values.NI, GT_Values.NF, Materials.Methane.getGas(576L), new ItemStack(Items.bowl, 16, 0), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 4608, 5);
        GT_Values.RA.addCentrifugeRecipe(new ItemStack(Items.apple, 32, 0), GT_Values.NI, GT_Values.NF, Materials.Methane.getGas(576L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 4608, 5);
        GT_Values.RA.addCentrifugeRecipe(new ItemStack(Items.bread, 64, 0), GT_Values.NI, GT_Values.NF, Materials.Methane.getGas(576L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 4608, 5);
        GT_Values.RA.addCentrifugeRecipe(new ItemStack(Items.porkchop, 12, 0), GT_Values.NI, GT_Values.NF, Materials.Methane.getGas(576L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 4608, 5);
        GT_Values.RA.addCentrifugeRecipe(new ItemStack(Items.cooked_porkchop, 16, 0), GT_Values.NI, GT_Values.NF, Materials.Methane.getGas(576L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 4608, 5);
        GT_Values.RA.addCentrifugeRecipe(new ItemStack(Items.beef, 12, 0), GT_Values.NI, GT_Values.NF, Materials.Methane.getGas(576L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 4608, 5);
        GT_Values.RA.addCentrifugeRecipe(new ItemStack(Items.cooked_beef, 16, 0), GT_Values.NI, GT_Values.NF, Materials.Methane.getGas(576L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 4608, 5);
        GT_Values.RA.addCentrifugeRecipe(new ItemStack(Items.fish, 12, 32767), GT_Values.NI, GT_Values.NF, Materials.Methane.getGas(576L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 4608, 5);
        GT_Values.RA.addCentrifugeRecipe(new ItemStack(Items.cooked_fished, 16, 32767), GT_Values.NI, GT_Values.NF, Materials.Methane.getGas(576L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 4608, 5);
        GT_Values.RA.addCentrifugeRecipe(new ItemStack(Items.chicken, 12, 0), GT_Values.NI, GT_Values.NF, Materials.Methane.getGas(576L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 4608, 5);
        GT_Values.RA.addCentrifugeRecipe(new ItemStack(Items.cooked_chicken, 16, 0), GT_Values.NI, GT_Values.NF, Materials.Methane.getGas(576L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 4608, 5);
        GT_Values.RA.addCentrifugeRecipe(new ItemStack(Items.melon, 64, 0), GT_Values.NI, GT_Values.NF, Materials.Methane.getGas(576L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 4608, 5);
        GT_Values.RA.addCentrifugeRecipe(new ItemStack(Blocks.pumpkin, 16, 0), GT_Values.NI, GT_Values.NF, Materials.Methane.getGas(576L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 4608, 5);
        GT_Values.RA.addCentrifugeRecipe(new ItemStack(Items.rotten_flesh, 16, 0), GT_Values.NI, GT_Values.NF, Materials.Methane.getGas(576L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 4608, 5);
        GT_Values.RA.addCentrifugeRecipe(new ItemStack(Items.spider_eye, 32, 0), GT_Values.NI, GT_Values.NF, Materials.Methane.getGas(576L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 4608, 5);
        GT_Values.RA.addCentrifugeRecipe(new ItemStack(Items.carrot, 16, 0), GT_Values.NI, GT_Values.NF, Materials.Methane.getGas(576L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 4608, 5);
        GT_Values.RA.addCentrifugeRecipe(ItemList.Food_Raw_Potato.get(16L, new Object[0]), GT_Values.NI, GT_Values.NF, Materials.Methane.getGas(576L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 4608, 5);
        GT_Values.RA.addCentrifugeRecipe(ItemList.Food_Poisonous_Potato.get(12L, new Object[0]), GT_Values.NI, GT_Values.NF, Materials.Methane.getGas(576L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 4608, 5);
        GT_Values.RA.addCentrifugeRecipe(ItemList.Food_Baked_Potato.get(24L, new Object[0]), GT_Values.NI, GT_Values.NF, Materials.Methane.getGas(576L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 4608, 5);
        GT_Values.RA.addCentrifugeRecipe(new ItemStack(Items.cookie, 64, 0), GT_Values.NI, GT_Values.NF, Materials.Methane.getGas(576L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 4608, 5);
        GT_Values.RA.addCentrifugeRecipe(new ItemStack(Items.cake, 8, 0), GT_Values.NI, GT_Values.NF, Materials.Methane.getGas(576L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 4608, 5);
        GT_Values.RA.addCentrifugeRecipe(new ItemStack(Blocks.brown_mushroom_block, 12, 32767), GT_Values.NI, GT_Values.NF, Materials.Methane.getGas(576L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 4608, 5);
        GT_Values.RA.addCentrifugeRecipe(new ItemStack(Blocks.red_mushroom_block, 12, 32767), GT_Values.NI, GT_Values.NF, Materials.Methane.getGas(576L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 4608, 5);
        GT_Values.RA.addCentrifugeRecipe(new ItemStack(Blocks.brown_mushroom, 32, 32767), GT_Values.NI, GT_Values.NF, Materials.Methane.getGas(576L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 4608, 5);
        GT_Values.RA.addCentrifugeRecipe(new ItemStack(Blocks.red_mushroom, 32, 32767), GT_Values.NI, GT_Values.NF, Materials.Methane.getGas(576L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 4608, 5);
        GT_Values.RA.addCentrifugeRecipe(new ItemStack(Items.nether_wart, 32, 32767), GT_Values.NI, GT_Values.NF, Materials.Methane.getGas(576L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 4608, 5);
        GT_Values.RA.addCentrifugeRecipe(GT_ModHandler.getIC2Item("terraWart", 16L), GT_Values.NI, GT_Values.NF, Materials.Methane.getGas(576L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 4608, 5);
        GT_Values.RA.addCentrifugeRecipe(GT_ModHandler.getModItem("TwilightForest", "item.meefRaw", 12L, 32767), GT_Values.NI, GT_Values.NF, Materials.Methane.getGas(576L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 4608, 5);
        GT_Values.RA.addCentrifugeRecipe(GT_ModHandler.getModItem("TwilightForest", "item.meefSteak", 16L, 32767), GT_Values.NI, GT_Values.NF, Materials.Methane.getGas(576L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 4608, 5);
        GT_Values.RA.addCentrifugeRecipe(GT_ModHandler.getModItem("TwilightForest", "item.venisonRaw", 12L, 32767), GT_Values.NI, GT_Values.NF, Materials.Methane.getGas(576L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 4608, 5);
        GT_Values.RA.addCentrifugeRecipe(GT_ModHandler.getModItem("TwilightForest", "item.venisonCooked", 16L, 32767), GT_Values.NI, GT_Values.NF, Materials.Methane.getGas(576L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 4608, 5);

        GT_Values.RA.addCentrifugeRecipe(GT_OreDictUnificator.get(OrePrefixes.log, Materials.Wood, 1L), null, null, Materials.Methane.getGas(60L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 200, 20);

        GT_Values.RA.addCentrifugeRecipe(new ItemStack(Blocks.sand, 1, 1), GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 1L), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Diamond, 1L), new ItemStack(Blocks.sand, 1), GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{5000, 100, 5000}, 600, 120);
        GT_Values.RA.addCentrifugeRecipe(new ItemStack(Blocks.dirt, 1, 32767), GT_Values.NI, GT_Values.NF, GT_Values.NF, ItemList.IC2_Plantball.get(1L, new Object[0]), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Clay, 1L), new ItemStack(Blocks.sand, 1), GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{1250, 5000, 5000}, 250, 30);
        GT_Values.RA.addCentrifugeRecipe(new ItemStack(Blocks.grass, 1, 32767), GT_Values.NI, GT_Values.NF, GT_Values.NF, ItemList.IC2_Plantball.get(1L, new Object[0]), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Clay, 1L), new ItemStack(Blocks.sand, 1), GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{2500, 5000, 5000}, 250, 30);
        GT_Values.RA.addCentrifugeRecipe(new ItemStack(Blocks.mycelium, 1, 32767), GT_Values.NI, GT_Values.NF, GT_Values.NF, new ItemStack(Blocks.brown_mushroom, 1), new ItemStack(Blocks.red_mushroom, 1), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Clay, 1L), new ItemStack(Blocks.sand, 1), GT_Values.NI, GT_Values.NI, new int[]{2500, 2500, 5000, 5000}, 650, 30);
        GT_Values.RA.addCentrifugeRecipe(ItemList.IC2_Resin.get(1L, new Object[0]), GT_Values.NI, GT_Values.NF, Materials.Glue.getFluid(100L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RawRubber, 3L), ItemList.IC2_Plantball.get(1L, new Object[0]), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{10000, 1000}, 300, 5);
        GT_Values.RA.addCentrifugeRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.DarkAsh, 1), 0, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 1L), ItemList.TE_Slag.get(1L, new Object[]{GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 1L)}), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, 250);
        GT_Values.RA.addCentrifugeRecipe(new ItemStack(Items.magma_cream, 1), 0, new ItemStack(Items.blaze_powder, 1), new ItemStack(Items.slime_ball, 1), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, 500);
        GT_Values.RA.addCentrifugeRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Uranium, 1L), GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Uranium235, 1L), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Plutonium, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{2000, 200}, 800, 320);
        GT_Values.RA.addCentrifugeRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Plutonium, 1L), GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Plutonium241, 1L), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Uranium, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{2000, 3000}, 1600, 320);
        GT_Values.RA.addCentrifugeRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 1L), GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.NaquadahEnriched, 1L), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Naquadria, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{5000, 1000}, 3200, 320);
        GT_Values.RA.addCentrifugeRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.NaquadahEnriched, 1L), GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Naquadria, 1L), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Naquadah, 1L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{2000, 3000}, 6400, 640);
        GT_Values.RA.addCentrifugeRecipe(GT_Values.NI, GT_Values.NI, Materials.Hydrogen.getGas(160L), Materials.Deuterium.getGas(40L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 160, 20);
        GT_Values.RA.addCentrifugeRecipe(GT_Values.NI, GT_Values.NI, Materials.Deuterium.getGas(160L), Materials.Tritium.getGas(40L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 160, 80);
        GT_Values.RA.addCentrifugeRecipe(GT_Values.NI, GT_Values.NI, Materials.Helium.getGas(80L), Materials.Helium_3.getGas(5L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 160, 80);
        GT_Values.RA.addCentrifugeRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glowstone, 1L), GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Redstone, 2L), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Gold, 2L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 488, 80);
        GT_Values.RA.addCentrifugeRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Endstone, 1L), GT_Values.NI, GT_Values.NF, Materials.Helium.getGas(120L), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Tungstate, 1L), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Platinum, 1L), new ItemStack(Blocks.sand, 1), GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{1250, 625, 9000, 0, 0, 0}, 320, 20);
        GT_Values.RA.addCentrifugeRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Netherrack, 1L), GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Redstone, 1L), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Sulfur, 1L), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Coal, 1L), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Gold, 1L), GT_Values.NI, GT_Values.NI, new int[]{5625, 9900, 5625, 625, 0, 0}, 160, 20);
        GT_Values.RA.addCentrifugeRecipe(new ItemStack(Blocks.soul_sand, 1), GT_Values.NI, GT_Values.NF, Materials.Oil.getFluid(80L), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Saltpeter, 1L), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Coal, 1L), new ItemStack(Blocks.sand, 1), GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{8000, 2000, 9000, 0, 0, 0}, 200, 80);
        GT_Values.RA.addCentrifugeRecipe(GT_Values.NI, GT_Values.NI, Materials.Lava.getFluid(100L), GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Copper, 1L), GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Tin, 1L), GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Gold, 1L), GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Silver, 1L), GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Tantalum, 1L), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Tungstate, 1L), new int[]{2000, 1000, 250, 250, 250, 250}, 80, 80);
        GT_Values.RA.addCentrifugeRecipe(GT_Values.NI, GT_Values.NI, FluidRegistry.getFluidStack("ic2pahoehoelava", 100), GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Copper, 1L), GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Tin, 1L), GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Gold, 1L), GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Silver, 1L), GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Tantalum, 1L), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Tungstate, 1L), new int[]{2000, 1000, 250, 250, 250, 250}, 40, 80);

        GT_Values.RA.addCentrifugeRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RareEarth, 1L), GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Neodymium, 1L), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Yttrium, 1L), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Lanthanum, 1L), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Cerium, 1L), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Cadmium, 1L), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Caesium, 1L), new int[]{2500, 2500, 2500, 2500, 2500, 2500}, 64, 20);
        GT_Values.RA.addCentrifugeRecipe(GT_ModHandler.getModItem(aTextAE, aTextAEMM, 1L, 45), GT_Values.NI, GT_Values.NF, GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.BasalticMineralSand, 1L), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Olivine, 1L), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Obsidian, 1L), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Basalt, 1L), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Flint, 1L), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.RareEarth, 1L), new int[]{2000, 2000, 2000, 2000, 2000, 2000}, 64, 20);

        run3();

        GT_Utility.removeSimpleIC2MachineRecipe(new ItemStack(Blocks.cobblestone), GT_ModHandler.getMaceratorRecipeList(), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1L));
        GT_Utility.removeSimpleIC2MachineRecipe(GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Lapis, 1L), GT_ModHandler.getMaceratorRecipeList(), ItemList.IC2_Plantball.get(1L, new Object[0]));
        GT_Utility.removeSimpleIC2MachineRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L), GT_ModHandler.getMaceratorRecipeList(), ItemList.IC2_Plantball.get(1L, new Object[0]));
        GT_Utility.removeSimpleIC2MachineRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glowstone, 1L), GT_ModHandler.getMaceratorRecipeList(), ItemList.IC2_Plantball.get(1L, new Object[0]));
        
		GT_Utility.removeSimpleIC2MachineRecipe(GT_Values.NI, GT_ModHandler.getMaceratorRecipeList(), GT_ModHandler.getModItem("IC2", "itemBiochaff", 1L));
        GT_Utility.removeSimpleIC2MachineRecipe(new ItemStack(Blocks.cactus, 8, 0), GT_ModHandler.getCompressorRecipeList(), GT_ModHandler.getModItem("IC2", "itemFuelPlantBall", 1L));
        GT_Utility.removeSimpleIC2MachineRecipe(GT_ModHandler.getModItem("ExtraTrees", "food", 8L, 24), GT_ModHandler.getCompressorRecipeList(), GT_ModHandler.getModItem("IC2", "itemFuelPlantBall", 1L));
		
        if(GregTech_API.mMagneticraft && GT_Mod.gregtechproxy.mMagneticraftRecipes){
        	GT_ModHandler.removeRecipeByOutput(GT_ModHandler.getModItem("Magneticraft", "item.ingotCarbide", 8));
        	GT_Values.RA.addAlloySmelterRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 8), GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.WroughtIron, 1), GT_ModHandler.getModItem("Magneticraft", "item.ingotCarbide", 1), 600, 24);
        	GT_Values.RA.addBlastRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 8), GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.TungstenCarbide, 1), null, null, GT_ModHandler.getModItem("Magneticraft", "item.ingotCarbide", 8), null, 100, 120, 2600);
        	GT_ModHandler.removeFurnaceSmelting(GT_ModHandler.getModItem("Magneticraft", "item.chunks", 1, 4));
        	GT_ModHandler.removeFurnaceSmelting(GT_ModHandler.getModItem("Magneticraft", "item.pebbles", 1, 4));
        	GT_ModHandler.removeFurnaceSmelting(GT_ModHandler.getModItem("Magneticraft", "item.rubble", 1, 4));
        	GT_ModHandler.removeFurnaceSmelting(GT_ModHandler.getModItem("Magneticraft", "item.chunks", 1, 13));
        	GT_ModHandler.removeFurnaceSmelting(GT_ModHandler.getModItem("Magneticraft", "item.pebbles", 1, 13));
        	GT_ModHandler.removeFurnaceSmelting(GT_ModHandler.getModItem("Magneticraft", "item.rubble", 1, 13));
        	GT_ModHandler.removeFurnaceSmelting(GT_ModHandler.getModItem("Magneticraft", "item.chunks", 1, 15));
        	GT_ModHandler.removeFurnaceSmelting(GT_ModHandler.getModItem("Magneticraft", "item.pebbles", 1, 15));
        	GT_ModHandler.removeFurnaceSmelting(GT_ModHandler.getModItem("Magneticraft", "item.rubble", 1, 15));
        	GT_ModHandler.removeFurnaceSmelting(GT_ModHandler.getModItem("Magneticraft", "item.chunks", 1, 16));
        	GT_ModHandler.removeFurnaceSmelting(GT_ModHandler.getModItem("Magneticraft", "item.pebbles", 1, 16));
        	GT_ModHandler.removeFurnaceSmelting(GT_ModHandler.getModItem("Magneticraft", "item.rubble", 1, 16));
        	GT_ModHandler.removeFurnaceSmelting(GT_ModHandler.getModItem("Magneticraft", "item.chunks", 1, 21));
        	GT_ModHandler.removeFurnaceSmelting(GT_ModHandler.getModItem("Magneticraft", "item.pebbles", 1, 21));
        	GT_ModHandler.removeFurnaceSmelting(GT_ModHandler.getModItem("Magneticraft", "item.rubble", 1, 21));
        }

        for (MaterialStack[] tMats : this.mAlloySmelterList) {
            ItemStack tDust1 = GT_OreDictUnificator.get(OrePrefixes.dust, tMats[0].mMaterial, tMats[0].mAmount);
            ItemStack tDust2 = GT_OreDictUnificator.get(OrePrefixes.dust, tMats[1].mMaterial, tMats[1].mAmount);
            ItemStack tIngot1 = GT_OreDictUnificator.get(OrePrefixes.ingot, tMats[0].mMaterial, tMats[0].mAmount);
            ItemStack tIngot2 = GT_OreDictUnificator.get(OrePrefixes.ingot, tMats[1].mMaterial, tMats[1].mAmount);
            ItemStack tOutputIngot = GT_OreDictUnificator.get(OrePrefixes.ingot, tMats[2].mMaterial, tMats[2].mAmount);
            if (tOutputIngot != GT_Values.NI) {
                GT_ModHandler.addAlloySmelterRecipe(tIngot1, tDust2, tOutputIngot, (int) tMats[2].mAmount * 50, 16, false);
                GT_ModHandler.addAlloySmelterRecipe(tIngot1, tIngot2, tOutputIngot, (int) tMats[2].mAmount * 50, 16, false);
                GT_ModHandler.addAlloySmelterRecipe(tDust1, tIngot2, tOutputIngot, (int) tMats[2].mAmount * 50, 16, false);
                GT_ModHandler.addAlloySmelterRecipe(tDust1, tDust2, tOutputIngot, (int) tMats[2].mAmount * 50, 16, false);
            }
        }

        if(!GregTech_API.mIC2Classic){
            try {
                Map<String, HeatExchangeProperty> tLiqExchange = ic2.api.recipe.Recipes.liquidCooldownManager.getHeatExchangeProperties();
                Iterator<Map.Entry<String, HeatExchangeProperty>> tIterator = tLiqExchange.entrySet().iterator();
                while (tIterator.hasNext()) {
                    Map.Entry<String, HeatExchangeProperty> tEntry = tIterator.next();
                    if(tEntry.getKey().equals("ic2hotcoolant")){
                    	tIterator.remove();
                    	Recipes.liquidCooldownManager.addFluid("ic2hotcoolant", "ic2coolant", 100);
                    }
                }
            } catch (Throwable e) {/*Do nothing*/}

            try {
                Map<String, HeatExchangeProperty> tLiqExchange = ic2.api.recipe.Recipes.liquidHeatupManager.getHeatExchangeProperties();
                Iterator<Map.Entry<String, HeatExchangeProperty>> tIterator = tLiqExchange.entrySet().iterator();
                while (tIterator.hasNext()) {
                    Map.Entry<String, HeatExchangeProperty> tEntry = tIterator.next();
                    if(tEntry.getKey().equals("ic2coolant")){
                    	tIterator.remove();
                    	Recipes.liquidHeatupManager.addFluid("ic2coolant", "ic2hotcoolant", 100);
                    }
                }
            } catch (Throwable e) {/*Do nothing*/}
        }
        GT_Utility.removeSimpleIC2MachineRecipe(ItemList.Crop_Drop_BobsYerUncleRanks.get(1L, new Object[0]), GT_ModHandler.getExtractorRecipeList(), null);
        GT_Utility.removeSimpleIC2MachineRecipe(ItemList.Crop_Drop_Ferru.get(1L, new Object[0]), GT_ModHandler.getExtractorRecipeList(), null);
        GT_Utility.removeSimpleIC2MachineRecipe(ItemList.Crop_Drop_Aurelia.get(1L, new Object[0]), GT_ModHandler.getExtractorRecipeList(), null);

		ItemStack tCrop;
	    // Metals Line
		tCrop = ItemList.Crop_Drop_Coppon.get(1, new Object[0]);
		addProcess(tCrop, Materials.Copper, 100, true);
		addProcess(tCrop, Materials.Tetrahedrite, 100, false);
		addProcess(tCrop, Materials.Chalcopyrite, 100, false);
		addProcess(tCrop, Materials.Malachite, 100, false);
		addProcess(tCrop, Materials.Pyrite, 100, false);
		addProcess(tCrop, Materials.Stibnite, 100, false);
		tCrop = ItemList.Crop_Drop_Tine.get(1, new Object[0]);
		addProcess(tCrop, Materials.Tin, 100, true);
		addProcess(tCrop, Materials.Cassiterite, 100, false);
		tCrop = ItemList.Crop_Drop_Plumbilia.get(1, new Object[0]);
		addProcess(tCrop, Materials.Lead, 100, true);
		tCrop = ItemList.Crop_Drop_Ferru.get(1, new Object[0]);
		addProcess(tCrop, Materials.Iron, 100, true);
		addProcess(tCrop, Materials.Magnetite, 100, false);
		addProcess(tCrop, Materials.BrownLimonite, 100, false);
		addProcess(tCrop, Materials.YellowLimonite, 100, false);
		addProcess(tCrop, Materials.VanadiumMagnetite, 100, false);
		addProcess(tCrop, Materials.BandedIron, 100, false);
		addProcess(tCrop, Materials.Pyrite, 100, false);
		addProcess(tCrop, Materials.MeteoricIron, 100, false);
		tCrop = ItemList.Crop_Drop_Nickel.get(1, new Object[0]);
		addProcess(tCrop, Materials.Nickel, 100, true);
		addProcess(tCrop, Materials.Garnierite, 100, false);
		addProcess(tCrop, Materials.Pentlandite, 100, false);
		addProcess(tCrop, Materials.Cobaltite, 100, false);
		addProcess(tCrop, Materials.Wulfenite, 100, false);
		addProcess(tCrop, Materials.Powellite, 100, false);
		tCrop = ItemList.Crop_Drop_Zinc.get(1, new Object[0]);
		addProcess(tCrop, Materials.Zinc, 100, true);
		addProcess(tCrop, Materials.Sphalerite, 100, false);
		addProcess(tCrop, Materials.Sulfur, 100, false);
		tCrop = ItemList.Crop_Drop_Argentia.get(1, new Object[0]);
		addProcess(tCrop, Materials.Silver, 100, true);
		addProcess(tCrop, Materials.Galena, 100, false);
		tCrop = ItemList.Crop_Drop_Aurelia.get(1, new Object[0]);
		addProcess(tCrop, Materials.Gold, 100, true);
		addProcess(tCrop, Materials.Magnetite, Materials.Gold, 100, false);

	    // Rare Metals Line
		tCrop = ItemList.Crop_Drop_Bauxite.get(1, new Object[0]);
		addProcess(tCrop,Materials.Aluminium,60, true);
		addProcess(tCrop,Materials.Bauxite,100, false);
		tCrop = ItemList.Crop_Drop_Manganese.get(1, new Object[0]);
		addProcess(tCrop,Materials.Manganese,30, true);
		addProcess(tCrop,Materials.Grossular,100, false);
		addProcess(tCrop,Materials.Spessartine,100, false);
		addProcess(tCrop,Materials.Pyrolusite,100, false);
		addProcess(tCrop,Materials.Tantalite,100, false);
		tCrop = ItemList.Crop_Drop_Ilmenite.get(1, new Object[0]);
		addProcess(tCrop,Materials.Titanium,100, true);
		addProcess(tCrop,Materials.Ilmenite,100, false);
		addProcess(tCrop,Materials.Bauxite,100, false);
		tCrop = ItemList.Crop_Drop_Scheelite.get(1, new Object[0]);
		addProcess(tCrop,Materials.Scheelite,100, true);
		addProcess(tCrop,Materials.Tungstate,100, false);
		addProcess(tCrop,Materials.Lithium,100, false);
		tCrop = ItemList.Crop_Drop_Platinum.get(1, new Object[0]);
		addProcess(tCrop,Materials.Platinum,40, true);
		addProcess(tCrop,Materials.Cooperite,40, false);
		addProcess(tCrop,Materials.Palladium,40, false);
		addProcess(tCrop, Materials.Neodymium, 100, false);
		addProcess(tCrop, Materials.Bastnasite, 100, false);
		tCrop = ItemList.Crop_Drop_Iridium.get(1, new Object[0]);
		addProcess(tCrop,Materials.Iridium,20, true);
		tCrop = ItemList.Crop_Drop_Osmium.get(1, new Object[0]);
		addProcess(tCrop,Materials.Osmium,20, true);

	    // Radioactive Line
        tCrop = ItemList.Crop_Drop_Pitchblende.get(1, new Object[0]);
        addProcess(tCrop,Materials.Pitchblende,50, true);
		tCrop = ItemList.Crop_Drop_Uraninite.get(1, new Object[0]);
		addProcess(tCrop,Materials.Uraninite,50, false);
		addProcess(tCrop,Materials.Uranium,50, true);
		addProcess(tCrop,Materials.Pitchblende,50, false);
		addProcess(tCrop,Materials.Uranium235,50, false);
		tCrop = ItemList.Crop_Drop_Thorium.get(1, new Object[0]);
		addProcess(tCrop,Materials.Thorium,50, true);
		tCrop = ItemList.Crop_Drop_Naquadah.get(1, new Object[0]);
		addProcess(tCrop,Materials.Naquadah,10, true);
		addProcess(tCrop,Materials.NaquadahEnriched,10, false);
		addProcess(tCrop,Materials.Naquadria,10, false);

		//Gem Line
		tCrop = ItemList.Crop_Drop_BobsYerUncleRanks.get(1, new Object[0]);
		addProcess(tCrop, Materials.Emerald, 100, true);
		addProcess(tCrop, Materials.Beryllium, 100, false);

        addRecipesApril2017ChemistryUpdate();
        //if (!GT_Mod.gregtechproxy.mDisableOldChemicalRecipes) {
          //  addOldChemicalRecipes();
        //} else {
            GT_Values.RA.addChemicalRecipe(Materials.Sodium.getDust(2), Materials.Sulfur.getDust(1), Materials.SodiumSulfide.getDust(1), 60);
            GT_Values.RA.addChemicalRecipe(Materials.HydricSulfide.getCells(1), GT_Values.NI, Materials.Water.getFluid(1000), Materials.DilutedSulfuricAcid.getFluid(750), Materials.Empty.getCells(1), 60);
            GT_Values.RA.addChemicalRecipe(Materials.Water.getCells(1), GT_Values.NI, Materials.HydricSulfide.getGas(1000), Materials.DilutedSulfuricAcid.getFluid(750), Materials.Empty.getCells(1), 60);
       //}
        GT_Values.RA.addAutoclaveRecipe(Materials.SiliconDioxide.getDust(1), Materials.Water.getFluid(200L), 		Materials.Quartzite.getGems(1), 750,  2000, 24);
        GT_Values.RA.addAutoclaveRecipe(Materials.SiliconDioxide.getDust(1), GT_ModHandler.getDistilledWater(200L), Materials.Quartzite.getGems(1), 1000, 1500, 24);

        addRecipesMay2017OilRefining();
        addPyrometallurgicalRecipes();
    }

    public void addProcess(ItemStack tCrop, Materials aMaterial, int chance, boolean aMainOutput) {
    	if(tCrop==null||aMaterial==null||GT_OreDictUnificator.get(OrePrefixes.crushed, aMaterial,1)==null)return;
        if (GT_Mod.gregtechproxy.mNerfedCrops) {
            GT_Values.RA.addChemicalRecipe(GT_Utility.copyAmount(9, tCrop), GT_OreDictUnificator.get(OrePrefixes.crushed, aMaterial, 1), Materials.Water.getFluid(1000), aMaterial.mOreByProducts.isEmpty() ? null : aMaterial.mOreByProducts.get(0).getMolten(144), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, 4), 96, 24);
            GT_Values.RA.addAutoclaveRecipe(GT_Utility.copyAmount(16, tCrop), Materials.UUMatter.getFluid(Math.max(1, ((aMaterial.getMass()+9)/10))), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, 1), 10000, (int) (aMaterial.getMass() * 128), 384);
        } else {
            if (aMainOutput) GT_ModHandler.addExtractionRecipe(tCrop, GT_OreDictUnificator.get(OrePrefixes.dustTiny, aMaterial, 1));
        }
    }

    public void addProcess(ItemStack tCrop, Materials aMaterial, int chance){
    	if(tCrop==null||aMaterial==null||GT_OreDictUnificator.get(OrePrefixes.crushed, aMaterial,1)==null)return;
        if (GT_Mod.gregtechproxy.mNerfedCrops) {
            GT_Values.RA.addChemicalRecipe(GT_Utility.copyAmount(9, tCrop), GT_OreDictUnificator.get(OrePrefixes.crushed, aMaterial, 1), Materials.Water.getFluid(1000), aMaterial.mOreByProducts.isEmpty() ? null : aMaterial.mOreByProducts.get(0).getMolten(144), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, 4), 96, 24);
            GT_Values.RA.addAutoclaveRecipe(GT_Utility.copyAmount(16, tCrop), Materials.UUMatter.getFluid(Math.max(1, ((aMaterial.getMass()+9)/10))), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, 1), 10000, (int) (aMaterial.getMass() * 128), 384);
        } else {
            GT_ModHandler.addExtractionRecipe(tCrop, GT_OreDictUnificator.get(OrePrefixes.dustTiny, aMaterial, 1));
        }
    }

    public void addProcess(ItemStack tCrop, Materials aMaterial, Materials aMaterialOut, int chance, boolean aMainOutput){
    	if(tCrop==null||aMaterial==null||GT_OreDictUnificator.get(OrePrefixes.crushed, aMaterial,1)==null)return;
        if (GT_Mod.gregtechproxy.mNerfedCrops) {
            GT_Values.RA.addChemicalRecipe(GT_Utility.copyAmount(9, tCrop), GT_OreDictUnificator.get(OrePrefixes.crushed, aMaterial, 1), Materials.Water.getFluid(1000), aMaterialOut.mOreByProducts.isEmpty() ? null : aMaterialOut.mOreByProducts.get(0).getMolten(144), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterialOut, 4), 96, 24);
            GT_Values.RA.addAutoclaveRecipe(GT_Utility.copyAmount(16, tCrop), Materials.UUMatter.getFluid(Math.max(1, ((aMaterial.getMass()+9)/10))), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, 1), 10000, (int) (aMaterial.getMass() * 128), 384);
        } else {
            if (aMainOutput) GT_ModHandler.addExtractionRecipe(tCrop, GT_OreDictUnificator.get(OrePrefixes.dustTiny, aMaterial, 1));
        }
    }

    public void addProcess(ItemStack tCrop, Materials aMaterial, Materials aMaterialOut, int chance){
    	if(tCrop==null||aMaterial==null||GT_OreDictUnificator.get(OrePrefixes.crushed, aMaterial,1)==null)return;
        if (GT_Mod.gregtechproxy.mNerfedCrops) {
            GT_Values.RA.addChemicalRecipe(GT_Utility.copyAmount(9, tCrop), GT_OreDictUnificator.get(OrePrefixes.crushed, aMaterial, 1), Materials.Water.getFluid(1000), aMaterialOut.mOreByProducts.isEmpty() ? null : aMaterialOut.mOreByProducts.get(0).getMolten(144), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterialOut, 4), 96, 24);
            GT_Values.RA.addAutoclaveRecipe(GT_Utility.copyAmount(16, tCrop), Materials.UUMatter.getFluid(Math.max(1, ((aMaterial.getMass()+9)/10))), GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, 1), 10000, (int) (aMaterial.getMass() * 128), 384);
        } else {
            GT_ModHandler.addExtractionRecipe(tCrop, GT_OreDictUnificator.get(OrePrefixes.dustTiny, aMaterial, 1));
        }
    }

	private void run3(){//TODO CHECK assline recipes
        //LUV   Palladium Plat, VanadiumGallium, topazes alduorite, chrome Naquadah
        //ZPM   iridium niobiumTitanium jade vinteum amethyst infused gold vulcanite Ceruclase mithril  Naquadah Alloy
        //UV    osmium  Deep Iron, Shadow Iron Orihalcum rubracium draconium Electrum Flux
        //UHV/(UV emmiter/sensor/forcefield)  Neutronium Europium lots of (niobium yttrium gallium)  adamantium draconium Yellow/red Garnets - meh
        //(UHV emmiter/sensor/forcefield) also use Black Plutonium Crysolite Realgar Yellow/red Garnets - good quality

        //recipe len:
        //LUV 6         72000   600   32k
        //ZPM 9         144000  1200  125k
        //UV- 12        288000  1800  500k
        //UV+/UHV- 14   360000  2100  2000k
        //UHV+ 16       576000  2400  4000k

        //addAssemblylineRecipe(ItemStack aResearchItem, int aResearchTime, ItemStack[] aInputs, FluidStack[] aFluidInputs, ItemStack aOutput1, int aDuration, int aEUt);

//      Motor

        GT_Values.RA.addAssemblylineRecipe(ItemList.Electric_Motor_IV.get(1, new Object(){}),144000,new ItemStack[]{
        		GT_OreDictUnificator.get(OrePrefixes.stick, Materials.SamariumMagnetic, 1L),
        		GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.HSSG, 2L),
        		GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.AnnealedCopper, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.AnnealedCopper, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.AnnealedCopper, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.AnnealedCopper, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.YttriumBariumCuprate, 2L)}, new FluidStack[]{
        		Materials.SolderingAlloy.getMolten(144),
        		Materials.Lubricant.getFluid(250)}, ItemList.Electric_Motor_LuV.get(1, new Object[]{}), 600, 6000);

        GT_Values.RA.addAssemblylineRecipe(ItemList.Electric_Motor_LuV.get(1, new Object(){}),144000,new ItemStack[]{
        		GT_OreDictUnificator.get(OrePrefixes.stick, Materials.SamariumMagnetic, 2L),
        		GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.HSSE, 4L),
        		GT_OreDictUnificator.get(OrePrefixes.ring, Materials.HSSE, 4L),
        		GT_OreDictUnificator.get(OrePrefixes.round, Materials.HSSE, 16L),
        		GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Platinum, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Platinum, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Platinum, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Platinum, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.VanadiumGallium, 2L)}, new FluidStack[]{
        		Materials.SolderingAlloy.getMolten(288),
        		Materials.Lubricant.getFluid(750)}, ItemList.Electric_Motor_ZPM.get(1, new Object[]{}), 600, 24000);

        GT_Values.RA.addAssemblylineRecipe(ItemList.Electric_Motor_ZPM.get(1, new Object(){}),288000,new ItemStack[]{
        		GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.SamariumMagnetic, 2L),
        		GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Neutronium, 4L),
        		GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Neutronium, 4L),
        		GT_OreDictUnificator.get(OrePrefixes.round, Materials.Neutronium, 16L),
        		GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Superconductor, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.NaquadahAlloy, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.NaquadahAlloy, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.NaquadahAlloy, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.NaquadahAlloy, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NaquadahAlloy, 2L)}, new FluidStack[]{
                Materials.Naquadria.getMolten(1296),
        		Materials.SolderingAlloy.getMolten(1296),
        		Materials.Lubricant.getFluid(2000)}, ItemList.Electric_Motor_UV.get(1, new Object[]{}), 600, 100000);

        GT_Values.RA.addAssemblylineRecipe(ItemList.Electric_Motor_UV.get(1, new Object(){}),576000,new ItemStack[]{
                GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.SamariumMagnetic, 4L),
                GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Neutronium, 8L),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Neutronium, 8L),
                GT_OreDictUnificator.get(OrePrefixes.round, Materials.Neutronium, 32L),
                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Superconductor, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Europium, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Europium, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Bedrockium, 2L)}, new FluidStack[]{
                Materials.Naquadria.getMolten(2592),
                Materials.SolderingAlloy.getMolten(2592),
                Materials.Lubricant.getFluid(4000)}, ItemList.Electric_Motor_UHV.get(1, new Object[]{}), 800, 200000);



//      Pump
        //LUV   Palladium Plat, VanadiumGallium, topazes alduorite, chrome Naquadah
        //ZPM   iridium niobiumTitanium jade vinteum amethyst infused gold vulcanite Ceruclase mithril  Naquadah Alloy
        //UV    osmium  Deep Iron, Shadow Iron Orihalcum rubracium draconium Electrum Flux
        //UHV/(UV emmiter/sensor/forcefield)  Neutronium Europium lots of (niobium yttrium gallium)  adamantium draconium Yellow/red Garnets - meh
        //(UHV emmiter/sensor/forcefield) also use Black Plutonium Crysolite Realgar Yellow/red Garnets - good quality

        GT_Values.RA.addAssemblylineRecipe(ItemList.Electric_Pump_IV.get(1, new Object(){}),144000,new ItemStack[]{
        		ItemList.Electric_Motor_LuV.get(1, new Object(){}),
        		GT_OreDictUnificator.get(OrePrefixes.pipeSmall, Materials.NiobiumTitanium, 2L),
        		GT_OreDictUnificator.get(OrePrefixes.plate, Materials.HSSG, 2L),
        		GT_OreDictUnificator.get(OrePrefixes.screw, Materials.HSSG, 8L),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.AnySyntheticRubber, 4L),
        		GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.HSSG, 2L),
        		GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.YttriumBariumCuprate, 2L)}, new FluidStack[]{
        		Materials.SolderingAlloy.getMolten(144),
        		Materials.Lubricant.getFluid(250)}, ItemList.Electric_Pump_LuV.get(1, new Object[]{}), 600, 6000);

        GT_Values.RA.addAssemblylineRecipe(ItemList.Electric_Pump_LuV.get(1, new Object(){}),144000,new ItemStack[]{
        		ItemList.Electric_Motor_ZPM.get(1, new Object(){}),
        		GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Enderium, 2L),
        		GT_OreDictUnificator.get(OrePrefixes.plate, Materials.HSSE, 2L),
        		GT_OreDictUnificator.get(OrePrefixes.screw, Materials.HSSE, 8L),
        		GT_OreDictUnificator.get(OrePrefixes.ring, Materials.AnySyntheticRubber, 16L),
        		GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.HSSE, 2L),
        		GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.VanadiumGallium, 2L)}, new FluidStack[]{
        		Materials.SolderingAlloy.getMolten(288),
        		Materials.Lubricant.getFluid(750)}, ItemList.Electric_Pump_ZPM.get(1, new Object[]{}), 600, 24000);

        GT_Values.RA.addAssemblylineRecipe(ItemList.Electric_Pump_ZPM.get(1, new Object(){}),288000,new ItemStack[]{
        		ItemList.Electric_Motor_UV.get(1, new Object(){}),
        		GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Naquadah, 2L),
        		GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 2L),
        		GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Neutronium, 8L),
        		GT_OreDictUnificator.get(OrePrefixes.ring, Materials.AnySyntheticRubber, 32L),
        		GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Neutronium, 2L),
        		GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NaquadahAlloy, 2L)}, new FluidStack[]{
                Materials.Naquadria.getMolten(1296),
        		Materials.SolderingAlloy.getMolten(1296),
        		Materials.Lubricant.getFluid(2000)}, ItemList.Electric_Pump_UV.get(1, new Object[]{}), 600, 100000);

        GT_Values.RA.addAssemblylineRecipe(ItemList.Electric_Pump_UV.get(1, new Object(){}),576000,new ItemStack[]{
                ItemList.Electric_Motor_UHV.get(1, new Object(){}),
                GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Neutronium, 2L),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Bedrockium, 4L),
                GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Neutronium, 16L),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.AnySyntheticRubber, 32L),
                GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Neutronium, 4L),
                GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Bedrockium, 2L)}, new FluidStack[]{
                Materials.Naquadria.getMolten(2592),
                Materials.SolderingAlloy.getMolten(2592),
                Materials.Lubricant.getFluid(4000)}, ItemList.Electric_Pump_UHV.get(1, new Object[]{}), 800, 200000);

//        Conveyor

        GT_Values.RA.addAssemblylineRecipe(ItemList.Conveyor_Module_IV.get(1, new Object(){}),144000,new ItemStack[]{
        		ItemList.Electric_Motor_LuV.get(2, new Object(){}),
        		GT_OreDictUnificator.get(OrePrefixes.plate, Materials.HSSG, 2L),
        		GT_OreDictUnificator.get(OrePrefixes.ring, Materials.HSSG, 4L),
        		GT_OreDictUnificator.get(OrePrefixes.round, Materials.HSSG, 32L),
        		GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.YttriumBariumCuprate, 2L)}, new FluidStack[]{
                Materials.SolderingAlloy.getMolten(144),
        		Materials.Lubricant.getFluid(250),
                Materials.StyreneButadieneRubber.getMolten(1440)},ItemList.Conveyor_Module_LuV.get(1, new Object[]{}), 600, 6000);

        GT_Values.RA.addAssemblylineRecipe(ItemList.Conveyor_Module_LuV.get(1, new Object(){}),144000,new ItemStack[]{
        		ItemList.Electric_Motor_ZPM.get(2, new Object(){}),
        		GT_OreDictUnificator.get(OrePrefixes.plate, Materials.HSSE, 2L),
        		GT_OreDictUnificator.get(OrePrefixes.ring, Materials.HSSE, 4L),
        		GT_OreDictUnificator.get(OrePrefixes.round, Materials.HSSE, 32L),
        		GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.VanadiumGallium, 2L)}, new FluidStack[]{
                Materials.SolderingAlloy.getMolten(288),
        		Materials.Lubricant.getFluid(750),
                Materials.StyreneButadieneRubber.getMolten(2880)}, ItemList.Conveyor_Module_ZPM.get(1, new Object[]{}), 600, 24000);

        GT_Values.RA.addAssemblylineRecipe(ItemList.Conveyor_Module_ZPM.get(1, new Object(){}),288000,new ItemStack[]{
        		ItemList.Electric_Motor_UV.get(2, new Object(){}),
        		GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 2L),
        		GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Neutronium, 4L),
        		GT_OreDictUnificator.get(OrePrefixes.round, Materials.Neutronium, 32L),
        		GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NaquadahAlloy, 2L)}, new FluidStack[]{
                Materials.Naquadria.getMolten(1296),
                Materials.SolderingAlloy.getMolten(1296),
        		Materials.Lubricant.getFluid(2000),
                Materials.StyreneButadieneRubber.getMolten(5760)}, ItemList.Conveyor_Module_UV.get(1, new Object[]{}), 600, 100000);

        GT_Values.RA.addAssemblylineRecipe(ItemList.Conveyor_Module_UV.get(1, new Object(){}),576000,new ItemStack[]{
                ItemList.Electric_Motor_UHV.get(2, new Object(){}),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Bedrockium, 2L),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Neutronium, 8L),
                GT_OreDictUnificator.get(OrePrefixes.round, Materials.Neutronium, 64L),
                GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Bedrockium, 2L)}, new FluidStack[]{
                Materials.Naquadria.getMolten(2592),
                Materials.SolderingAlloy.getMolten(2592),
                Materials.Lubricant.getFluid(4000),
                Materials.StyreneButadieneRubber.getMolten(11520)}, ItemList.Conveyor_Module_UHV.get(1, new Object[]{}), 800, 200000);

//        Piston

        GT_Values.RA.addAssemblylineRecipe(ItemList.Electric_Piston_IV.get(1, new Object(){}),144000,new ItemStack[]{
        		ItemList.Electric_Motor_LuV.get(1, new Object(){}),
        		GT_OreDictUnificator.get(OrePrefixes.plate, Materials.HSSG, 6L),
        		GT_OreDictUnificator.get(OrePrefixes.ring, Materials.HSSG, 4L),
        		GT_OreDictUnificator.get(OrePrefixes.round, Materials.HSSG, 32L),
        		GT_OreDictUnificator.get(OrePrefixes.stick, Materials.HSSG, 4L),
        		GT_OreDictUnificator.get(OrePrefixes.gear, Materials.HSSG, 1L),
        		GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.HSSG, 2L),
        		GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.YttriumBariumCuprate, 4L)}, new FluidStack[]{
        		Materials.SolderingAlloy.getMolten(144),
        		Materials.Lubricant.getFluid(250)}, ItemList.Electric_Piston_LuV.get(1, new Object[]{}), 600, 6000);


        GT_Values.RA.addAssemblylineRecipe(ItemList.Electric_Piston_LuV.get(1, new Object(){}),144000,new ItemStack[]{
        		ItemList.Electric_Motor_ZPM.get(1, new Object(){}),
        		GT_OreDictUnificator.get(OrePrefixes.plate, Materials.HSSE, 6L),
        		GT_OreDictUnificator.get(OrePrefixes.ring, Materials.HSSE, 4L),
        		GT_OreDictUnificator.get(OrePrefixes.round, Materials.HSSE, 32L),
        		GT_OreDictUnificator.get(OrePrefixes.stick, Materials.HSSE, 4L),
        		GT_OreDictUnificator.get(OrePrefixes.gear, Materials.HSSE, 1L),
        		GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.HSSE, 2L),
        		GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.VanadiumGallium, 4L)}, new FluidStack[]{
        		Materials.SolderingAlloy.getMolten(288),
        		Materials.Lubricant.getFluid(750)}, ItemList.Electric_Piston_ZPM.get(1, new Object[]{}), 600, 24000);

         GT_Values.RA.addAssemblylineRecipe(ItemList.Electric_Piston_ZPM.get(1, new Object(){}),288000,new ItemStack[]{
                ItemList.Electric_Motor_UV.get(1, new Object(){}),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 6L),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Neutronium, 4L),
                GT_OreDictUnificator.get(OrePrefixes.round, Materials.Neutronium, 32L),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Neutronium, 4L),
                GT_OreDictUnificator.get(OrePrefixes.gear, Materials.Neutronium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Neutronium, 2L),
                GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NaquadahAlloy, 4L)}, new FluidStack[]{
                Materials.Naquadria.getMolten(1296),
                Materials.SolderingAlloy.getMolten(1296),
                Materials.Lubricant.getFluid(2000)}, ItemList.Electric_Piston_UV.get(1, new Object[]{}), 600, 100000);

        GT_Values.RA.addAssemblylineRecipe(ItemList.Electric_Piston_UV.get(1, new Object(){}),576000,new ItemStack[]{
                ItemList.Electric_Motor_UV.get(1, new Object(){}),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Bedrockium, 6L),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Neutronium, 8L),
                GT_OreDictUnificator.get(OrePrefixes.round, Materials.Neutronium, 64L),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Neutronium, 8L),
                GT_OreDictUnificator.get(OrePrefixes.gear, Materials.Neutronium, 2L),
                GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Neutronium, 4L),
                GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Bedrockium, 4L)}, new FluidStack[]{
                Materials.Naquadria.getMolten(2592),
                Materials.SolderingAlloy.getMolten(2592),
                Materials.Lubricant.getFluid(4000)}, ItemList.Electric_Piston_UHV.get(1, new Object[]{}), 800, 200000);

//        RobotArm


        GT_Values.RA.addAssemblylineRecipe(ItemList.Robot_Arm_IV.get(1, new Object(){}),144000,new Object[]{
        		GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.HSSG, 4L),
        		GT_OreDictUnificator.get(OrePrefixes.gear, Materials.HSSG, 1L),
        		GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.HSSG, 3L),
        		ItemList.Electric_Motor_LuV.get(2, new Object(){}),
        		ItemList.Electric_Piston_LuV.get(1, new Object(){}),
                new Object[]{OrePrefixes.circuit.get(Materials.Master), 2},
                new Object[]{OrePrefixes.circuit.get(Materials.Elite), 4},
                new Object[]{OrePrefixes.circuit.get(Materials.Data), 8},
        		GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.YttriumBariumCuprate, 6L)}, new FluidStack[]{
        		Materials.SolderingAlloy.getMolten(576),
        		Materials.Lubricant.getFluid(250)}, ItemList.Robot_Arm_LuV.get(1, new Object[]{}), 600, 6000);

        GT_Values.RA.addAssemblylineRecipe(ItemList.Robot_Arm_LuV.get(1, new Object(){}),144000,new Object[]{
        		GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.HSSE, 4L),
        		GT_OreDictUnificator.get(OrePrefixes.gear, Materials.HSSE, 1L),
        		GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.HSSE, 3L),
        		ItemList.Electric_Motor_ZPM.get(2, new Object(){}),
        		ItemList.Electric_Piston_ZPM.get(1, new Object(){}),
                new Object[]{OrePrefixes.circuit.get(Materials.Ultimate), 2},
                new Object[]{OrePrefixes.circuit.get(Materials.Master), 4},
                new Object[]{OrePrefixes.circuit.get(Materials.Elite), 8},
        		GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.VanadiumGallium, 6L)}, new FluidStack[]{
        		Materials.SolderingAlloy.getMolten(1152),
        		Materials.Lubricant.getFluid(750)}, ItemList.Robot_Arm_ZPM.get(1, new Object[]{}), 600, 24000);

        GT_Values.RA.addAssemblylineRecipe(ItemList.Robot_Arm_ZPM.get(1, new Object(){}),288000,new Object[]{
        		GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Neutronium, 4L),
        		GT_OreDictUnificator.get(OrePrefixes.gear, Materials.Neutronium, 1L),
        		GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Neutronium, 3L),
        		ItemList.Electric_Motor_UV.get(2, new Object(){}),
        		ItemList.Electric_Piston_UV.get(1, new Object(){}),
                new Object[]{OrePrefixes.circuit.get(Materials.Superconductor), 2},
                new Object[]{OrePrefixes.circuit.get(Materials.Ultimate), 4},
                new Object[]{OrePrefixes.circuit.get(Materials.Master), 8},
        		GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NaquadahAlloy, 6L)}, new FluidStack[]{
                Materials.Naquadria.getMolten(1296),
        		Materials.SolderingAlloy.getMolten(2304),
        		Materials.Lubricant.getFluid(2000)}, ItemList.Robot_Arm_UV.get(1, new Object[]{}), 600, 100000);

        GT_Values.RA.addAssemblylineRecipe(ItemList.Robot_Arm_UV.get(1, new Object(){}),576000,new Object[]{
                GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Neutronium, 8L),
                GT_OreDictUnificator.get(OrePrefixes.gear, Materials.Neutronium, 2L),
                GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Neutronium, 6L),
                ItemList.Electric_Motor_UHV.get(2, new Object(){}),
                ItemList.Electric_Piston_UHV.get(1, new Object(){}),
                new Object[]{OrePrefixes.circuit.get(Materials.Infinite), 2},
                new Object[]{OrePrefixes.circuit.get(Materials.Superconductor), 4},
                new Object[]{OrePrefixes.circuit.get(Materials.Ultimate), 8},
                GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Bedrockium, 6L)}, new FluidStack[]{
                Materials.Naquadria.getMolten(2592),
                Materials.SolderingAlloy.getMolten(4608),
                Materials.Lubricant.getFluid(4000)}, ItemList.Robot_Arm_UHV.get(1, new Object[]{}), 800, 200000);

//        Emitter
        //LUV   Palladium Plat, VanadiumGallium, topazes alduorite, chrome Naquadah
        //ZPM   iridium niobiumTitanium jade vinteum amethyst infused gold vulcanite Ceruclase mithril  Naquadah Alloy
        //UV    osmium  Deep Iron, Shadow Iron Orihalcum rubracium draconium Electrum Flux
        //UHV/(UV emmiter/sensor/forcefield)  Neutronium Europium lots of (niobium yttrium gallium)  adamantium draconium Yellow/red Garnets - meh
        //(UHV emmiter/sensor/forcefield) also use Black Plutonium Crysolite Realgar Yellow/red Garnets - good quality

        GT_Values.RA.addAssemblylineRecipe(ItemList.Emitter_IV.get(1, new Object(){}),144000,new Object[]{
        		GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.HSSG, 1L),
        		ItemList.Electric_Motor_LuV.get(1, new Object(){}),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Osmium, 8L),
        		ItemList.QuantumStar.get(1, new Object(){}),
        		new Object[]{OrePrefixes.circuit.get(Materials.Master), 4},
        		GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Electrum, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Electrum, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Electrum, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.YttriumBariumCuprate, 7L)}, new FluidStack[]{
        		Materials.SolderingAlloy.getMolten(576)},
        		ItemList.Emitter_LuV.get(1, new Object[]{}), 600, 6000);

        GT_Values.RA.addAssemblylineRecipe(ItemList.Emitter_LuV.get(1, new Object(){}),144000,new Object[]{
        		GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.HSSE, 1L),
                ItemList.Electric_Motor_ZPM.get(1, new Object(){}),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Osmiridium, 8L),
                ItemList.QuantumStar.get(2, new Object(){}),
                new Object[]{OrePrefixes.circuit.get(Materials.Ultimate), 4},
        		GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Platinum, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Platinum, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Platinum, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.VanadiumGallium, 7L)}, new FluidStack[]{
        		Materials.SolderingAlloy.getMolten(1152)},
        		ItemList.Emitter_ZPM.get(1, new Object[]{}), 600, 24000);

        GT_Values.RA.addAssemblylineRecipe(ItemList.Emitter_ZPM.get(1, new Object(){}),288000,new Object[]{
        		GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 1L),
                ItemList.Electric_Motor_UV.get(1, new Object(){}),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Neutronium, 8L),
                ItemList.Gravistar.get(4, new Object(){}),
                new Object[]{OrePrefixes.circuit.get(Materials.Superconductor), 4},
        		GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Osmiridium, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Osmiridium, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Osmiridium, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NaquadahAlloy, 7L)}, new FluidStack[]{
                Materials.Naquadria.getMolten(1296),
        		Materials.SolderingAlloy.getMolten(2304)},
        		ItemList.Emitter_UV.get(1, new Object[]{}), 600, 100000);

        GT_Values.RA.addAssemblylineRecipe(ItemList.Emitter_UV.get(1, new Object(){}),576000,new Object[]{
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Europium, 1L),
                        ItemList.Electric_Motor_UV.get(1, new Object(){}),
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Adamantium, 8L),
                        ItemList.Gravistar.get(8, new Object(){}),
                        new Object[]{OrePrefixes.circuit.get(Materials.Infinite), 4},
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Adamantium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Adamantium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Neutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Neutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Bedrockium, 7L)}, new FluidStack[]{
                        Materials.Naquadria.getMolten(2592),
                        Materials.SolderingAlloy.getMolten(4608)},
                ItemList.Emitter_UHV.get(1, new Object[]{}), 800, 200000);

//        Sensor

        GT_Values.RA.addAssemblylineRecipe(ItemList.Sensor_IV.get(1, new Object(){}),144000,new Object[]{
        		GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.HSSG, 1L),
                ItemList.Electric_Motor_LuV.get(1, new Object(){}),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 8L),
                ItemList.QuantumStar.get(1, new Object(){}),
                new Object[]{OrePrefixes.circuit.get(Materials.Master), 4},
        		GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Electrum, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Electrum, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Electrum, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.YttriumBariumCuprate, 7L)}, new FluidStack[]{
        		Materials.SolderingAlloy.getMolten(576)},
        		ItemList.Sensor_LuV.get(1, new Object[]{}), 600, 6000);

        GT_Values.RA.addAssemblylineRecipe(ItemList.Sensor_LuV.get(1, new Object(){}),144000,new Object[]{
        		GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.HSSE, 1L),
                ItemList.Electric_Motor_ZPM.get(1, new Object(){}),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmiridium, 8L),
                ItemList.QuantumStar.get(2, new Object(){}),
                new Object[]{OrePrefixes.circuit.get(Materials.Ultimate), 4},
        		GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Platinum, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Platinum, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Platinum, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.VanadiumGallium, 7L)}, new FluidStack[]{
        		Materials.SolderingAlloy.getMolten(1152)},
        		ItemList.Sensor_ZPM.get(1, new Object[]{}), 600, 24000);

        GT_Values.RA.addAssemblylineRecipe(ItemList.Sensor_ZPM.get(1, new Object(){}),288000,new Object[]{
        		GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 1L),
                ItemList.Electric_Motor_UV.get(1, new Object(){}),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 8L),
                ItemList.Gravistar.get(4, new Object(){}),
                new Object[]{OrePrefixes.circuit.get(Materials.Superconductor), 4},
        		GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Osmiridium, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Osmiridium, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Osmiridium, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NaquadahAlloy, 7L)}, new FluidStack[]{
                Materials.Naquadria.getMolten(1296),
        		Materials.SolderingAlloy.getMolten(2304)},
        		ItemList.Sensor_UV.get(1, new Object[]{}), 600, 100000);

        GT_Values.RA.addAssemblylineRecipe(ItemList.Sensor_UV.get(1, new Object(){}),576000,new Object[]{
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Europium, 1L),
                        ItemList.Electric_Motor_UHV.get(1, new Object(){}),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Bedrockium, 8L),
                        ItemList.Gravistar.get(8, new Object(){}),
                        new Object[]{OrePrefixes.circuit.get(Materials.Infinite), 4},
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Adamantium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Adamantium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Neutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Neutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Bedrockium, 7L)}, new FluidStack[]{
                        Materials.Naquadria.getMolten(2592),
                        Materials.SolderingAlloy.getMolten(4608)},
                ItemList.Sensor_UHV.get(1, new Object[]{}), 800, 200000);

//        Field Generator

        GT_Values.RA.addAssemblylineRecipe(ItemList.Field_Generator_IV.get(1, new Object(){}),144000,new Object[]{
        		GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.HSSG, 1L),
        		GT_OreDictUnificator.get(OrePrefixes.plate, Materials.HSSG, 6L),
        		ItemList.QuantumStar.get(2, new Object(){}),
        		ItemList.Emitter_LuV.get(4, new Object(){}),
        		new ItemStack[]{ItemList.Circuit_Crystalmainframe.get(4, new Object(){}), ItemList.Circuit_Wetwaresupercomputer.get(4, new Object(){})},
        		GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Osmium, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Osmium, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Osmium, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Osmium, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.YttriumBariumCuprate, 8L)}, new FluidStack[]{
        		Materials.SolderingAlloy.getMolten(576)},
        		ItemList.Field_Generator_LuV.get(1, new Object[]{}), 600, 6000);

        GT_Values.RA.addAssemblylineRecipe(ItemList.Field_Generator_LuV.get(1, new Object(){}),144000,new Object[]{
        		GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.HSSE, 1L),
        		GT_OreDictUnificator.get(OrePrefixes.plate, Materials.HSSE, 6L),
        		ItemList.QuantumStar.get(1, new Object(){}),
        		ItemList.Emitter_ZPM.get(4, new Object(){}),
        		new ItemStack[]{ItemList.Circuit_Crystalmainframe.get(8, new Object(){}), ItemList.Circuit_Wetwaresupercomputer.get(8, new Object(){})},
        		GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Osmiridium, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Osmiridium, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Osmiridium, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Osmiridium, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Osmiridium, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Osmiridium, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Osmiridium, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Osmiridium, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.VanadiumGallium, 8L)}, new FluidStack[]{
        		Materials.SolderingAlloy.getMolten(1152)},
        		ItemList.Field_Generator_ZPM.get(1, new Object[]{}), 600, 24000);

        GT_Values.RA.addAssemblylineRecipe(ItemList.Field_Generator_ZPM.get(1, new Object(){}),288000,new Object[]{
        		GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 1L),
        		GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 6L),
        		ItemList.Gravistar.get(2, new Object(){}),
        		ItemList.Emitter_UV.get(4, new Object(){}),
        		new ItemStack[]{ItemList.Circuit_Crystalmainframe.get(16, new Object(){}), ItemList.Circuit_Wetwaresupercomputer.get(16, new Object(){})},
        		GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Naquadah, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Naquadah, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Naquadah, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Naquadah, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Naquadah, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Naquadah, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Naquadah, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Naquadah, 64L),
        		GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NaquadahAlloy, 8L)},
                new FluidStack[]{
                Materials.Naquadria.getMolten(1296),
        		Materials.SolderingAlloy.getMolten(2304)},
        		ItemList.Field_Generator_UV.get(1, new Object[]{}), 600, 100000);

        GT_Values.RA.addAssemblylineRecipe(ItemList.Field_Generator_UV.get(1, new Object(){}),576000,new Object[]{
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Europium, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Bedrockium, 6L),
                        ItemList.Gravistar.get(4, new Object(){}),
                        ItemList.Emitter_UHV.get(4, new Object(){}),
                        new ItemStack[]{ItemList.Circuit_Crystalmainframe.get(16, new Object(){}), ItemList.Circuit_Wetwaresupercomputer.get(16, new Object(){})},
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Adamantium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Adamantium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Adamantium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Adamantium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Bedrockium, 8L)},
                new FluidStack[]{
                        Materials.Naquadria.getMolten(2592),
                        Materials.SolderingAlloy.getMolten(4608)},
                ItemList.Field_Generator_UHV.get(1, new Object[]{}), 800, 200000);

        Object o = new Object[0];

        GT_Values.RA.addAssemblylineRecipe(ItemList.Energy_LapotronicOrb2.get(1, o), 288000, new ItemStack[]{
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Europium, 16L),
                ItemList.Circuit_Wetwarecomputer.get(1, o),
                ItemList.Circuit_Wetwarecomputer.get(1, o),
                ItemList.Circuit_Wetwarecomputer.get(1, o),
                ItemList.Circuit_Wetwarecomputer.get(1, o),
                ItemList.Energy_LapotronicOrb2.get(8L, new Object[0]),
                ItemList.Field_Generator_LuV.get(2, o),
                ItemList.Circuit_Wafer_SoC2.get(64, o),
                ItemList.Circuit_Wafer_SoC2.get(64, o),
                ItemList.Circuit_Parts_DiodeSMD.get(8, o),
                GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Naquadah, 32),},
                new FluidStack[]{
                Materials.SolderingAlloy.getMolten(2880), GregTech_API.mIC2Classic ? Materials.Water.getFluid(8000) : new FluidStack(FluidRegistry.getFluid("ic2coolant"), 16000)},
                ItemList.Energy_Module.get(1, o), 2000, 100000);

        GT_Values.RA.addAssemblylineRecipe(ItemList.Energy_Module.get(1, o), 288000, new ItemStack[]{
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Americium, 32L),
                ItemList.Circuit_Wetwaresupercomputer.get(1, o),
                ItemList.Circuit_Wetwaresupercomputer.get(1, o),
                ItemList.Circuit_Wetwaresupercomputer.get(1, o),
                ItemList.Circuit_Wetwaresupercomputer.get(1, o),
                ItemList.Energy_Module.get(8L, new Object[0]),
                ItemList.Field_Generator_ZPM.get(2, o),
                ItemList.Circuit_Wafer_HPIC.get(64, o),
                ItemList.Circuit_Wafer_HPIC.get(64, o),
                ItemList.Circuit_Parts_DiodeSMD.get(16, o),
                GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.NaquadahAlloy, 32),},
                new FluidStack[]{
                Materials.SolderingAlloy.getMolten(2880),
                GregTech_API.mIC2Classic ? Materials.Water.getFluid(16000) : new FluidStack(FluidRegistry.getFluid("ic2coolant"), 16000)},
                ItemList.Energy_Cluster.get(1, o), 2000, 200000);

        GT_Values.RA.addAssemblylineRecipe(ItemList.Energy_Cluster.get(1,o), 288000, new ItemStack[]{
            		GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 64L),
            		ItemList.Circuit_Wetwaremainframe.get(1,o),
            		ItemList.Circuit_Wetwaremainframe.get(1,o),
            		ItemList.Circuit_Wetwaremainframe.get(1,o),
            		ItemList.Circuit_Wetwaremainframe.get(1,o),
            		ItemList.Energy_LapotronicOrb2.get(8L, new Object[0]),
            		ItemList.Field_Generator_UV.get(2,o),
            		ItemList.Circuit_Wafer_HPIC.get(64,o),
            		ItemList.Circuit_Wafer_HPIC.get(64,o),
            		ItemList.Circuit_Parts_DiodeSMD.get(64,o),
            		GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Superconductor, 64),
            }, new FluidStack[]{
            		Materials.SolderingAlloy.getMolten(2880),
            		GregTech_API.mIC2Classic ? Materials.Water.getFluid(16000) : new FluidStack(FluidRegistry.getFluid("ic2coolant"), 16000)
            }, ItemList.ZPM2.get(1,o), 2000, 300000);

        GT_Values.RA.addAssemblylineRecipe(ItemList.ZPM2.get(1,o), 576000, new ItemStack[]{
                GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Infinity, 4L),
                ItemList.Circuit_Wetwaremainframe.get(1,o),
                ItemList.Circuit_Wetwaremainframe.get(1,o),
                ItemList.Circuit_Wetwaremainframe.get(1,o),
                ItemList.Circuit_Wetwaremainframe.get(1,o),
                ItemList.ZPM2.get(8,o),
                ItemList.Field_Generator_UV.get(4,o),
                ItemList.Circuit_Wafer_UHPIC.get(64,o),
                ItemList.Circuit_Wafer_UHPIC.get(64,o),
                ItemList.Circuit_Wafer_SoC2.get(32,o),
                ItemList.Circuit_Parts_DiodeSMD.get(64,o),
                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Superconductor, 64),
        }, new FluidStack[]{
                Materials.SolderingAlloy.getMolten(3760),
                Materials.Naquadria.getMolten(9000),
                new FluidStack(FluidRegistry.getFluid("ic2coolant"), 32000)
        }, ItemList.ZPM3.get(1,o), 4000, 450000);
            
            GT_Values.RA.addAssemblylineRecipe(GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 1), 144000, new Object[]{
            		ItemList.Casing_Fusion_Coil.get(1,o),
            		new Object[]{OrePrefixes.circuit.get(Materials.Ultimate), 1},
            		new Object[]{OrePrefixes.circuit.get(Materials.Ultimate), 1},
            		new Object[]{OrePrefixes.circuit.get(Materials.Ultimate), 1},
            		new Object[]{OrePrefixes.circuit.get(Materials.Ultimate), 1},
            		GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Plutonium241, 1L),
            		GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NetherStar, 1L),
            		ItemList.Field_Generator_IV.get(2,o),
            		ItemList.Circuit_Wafer_HPIC.get(32,o),
            		GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 32),
            }, new FluidStack[]{
            		Materials.SolderingAlloy.getMolten(2880),
            }, ItemList.FusionComputer_LuV.get(1,o), 1000, 30000);
            
            GT_Values.RA.addAssemblylineRecipe(GT_OreDictUnificator.get(OrePrefixes.block, Materials.Europium, 1), 288000, new Object[]{
            		ItemList.Casing_Fusion_Coil.get(1,o),
            		new Object[]{OrePrefixes.circuit.get(Materials.Superconductor), 1},
            		new Object[]{OrePrefixes.circuit.get(Materials.Superconductor), 1},
            		new Object[]{OrePrefixes.circuit.get(Materials.Superconductor), 1},
            		new Object[]{OrePrefixes.circuit.get(Materials.Superconductor), 1},
            		GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Europium, 4L),
            		ItemList.Field_Generator_LuV.get(2,o),
            		ItemList.Circuit_Wafer_HPIC.get(48,o),
            		GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorZPM, 32),
            }, new FluidStack[]{
            		Materials.SolderingAlloy.getMolten(2880),
            }, ItemList.FusionComputer_ZPMV.get(1,o), 1000, 60000);
            
            GT_Values.RA.addAssemblylineRecipe(GT_OreDictUnificator.get(OrePrefixes.block, Materials.Americium, 1), 432000, new ItemStack[]{
            		ItemList.Casing_Fusion_Coil.get(1,o),
            		ItemList.Circuit_Wetwaresupercomputer.get(1,o),
            		ItemList.Circuit_Wetwaresupercomputer.get(1,o),
            		ItemList.Circuit_Wetwaresupercomputer.get(1,o),
            		ItemList.Circuit_Wetwaresupercomputer.get(1,o),
            		GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Americium, 4L),
            		ItemList.Field_Generator_ZPM.get(2,o),
            		ItemList.Circuit_Wafer_HPIC.get(64,o),
            		GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Superconductor, 32),
            }, new FluidStack[]{
            		Materials.SolderingAlloy.getMolten(2880),
            }, ItemList.FusionComputer_UV.get(1,o), 1000, 90000);

            if (GregTech_API.sThaumcraftCompat != null) {
                String tKey = "GT_WOOD_TO_CHARCOAL";
                GT_LanguageManager.addStringLocalization(aTextTCGTPage + tKey, "You have discovered a way of making charcoal magically instead of using regular ovens for this purpose.<BR><BR>To create charcoal from wood you first need an air-free environment, some vacuus essentia is needed for that, then you need to incinerate the wood using ignis essentia and wait until all the water inside the wood is burned away.<BR><BR>This method however doesn't create creosote oil as byproduct.");

                GregTech_API.sThaumcraftCompat.addResearch(tKey, "Charcoal Transmutation", "Turning wood into charcoal", new String[]{"ALUMENTUM"}, "ALCHEMY", GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 1L), 2, 0, 13, 5, Arrays.asList(new TC_Aspects.TC_AspectStack[]{new TC_Aspects.TC_AspectStack(TC_Aspects.ARBOR, 10L), new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 8L), new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 8L)}), null, new Object[]{aTextTCGTPage + tKey, GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, OrePrefixes.log.get(Materials.Wood), GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 1L), Arrays.asList(new TC_Aspects.TC_AspectStack[]{new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 1L)}))});

                tKey = "GT_FILL_WATER_BUCKET";
                GT_LanguageManager.addStringLocalization(aTextTCGTPage + tKey, "You have discovered a way of filling a bucket with aqua essentia in order to simply get water.");
                GregTech_API.sThaumcraftCompat.addResearch(tKey, "Water Transmutation", "Filling buckets with water", null, "ALCHEMY", GT_OreDictUnificator.get(OrePrefixes.bucket, Materials.Water, 1L), 2, 0, 16, 5, Arrays.asList(new TC_Aspects.TC_AspectStack[]{new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 4L), new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 4L)}), null, new Object[]{aTextTCGTPage + tKey, GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, GT_OreDictUnificator.get(OrePrefixes.bucket, Materials.Empty, 1L), GT_OreDictUnificator.get(OrePrefixes.bucket, Materials.Water, 1L), Arrays.asList(new TC_Aspects.TC_AspectStack[]{new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 4L)})), GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, GT_OreDictUnificator.get(OrePrefixes.capsule, Materials.Empty, 1L), GT_OreDictUnificator.get(OrePrefixes.capsule, Materials.Water, 1L), Arrays.asList(new TC_Aspects.TC_AspectStack[]{new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 4L)})), GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1L), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1L), Arrays.asList(new TC_Aspects.TC_AspectStack[]{new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 4L)}))});

                tKey = "GT_TRANSZINC";
                GT_LanguageManager.addStringLocalization(aTextTCGTPage + tKey, "You have discovered a way to multiply zinc by steeping zinc nuggets in metallum harvested from other metals.");
                GregTech_API.sThaumcraftCompat.addResearch(tKey, "Zinc Transmutation", "Transformation of metals into zinc", new String[]{"TRANSTIN"}, "ALCHEMY", GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Zinc, 1L), 2, 1, 9, 13, Arrays.asList(new TC_Aspects.TC_AspectStack[]{new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L), new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L), new TC_Aspects.TC_AspectStack(TC_Aspects.SANO, 3L)}), null, new Object[]{aTextTCGTPage + tKey, GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, OrePrefixes.nugget.get(Materials.Zinc), GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Zinc, 3L), Arrays.asList(new TC_Aspects.TC_AspectStack[]{new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.SANO, 1L)}))});

                tKey = "GT_TRANSANTIMONY";
                GT_LanguageManager.addStringLocalization(aTextTCGTPage + tKey, "You have discovered a way to multiply antimony by steeping antimony nuggets in metallum harvested from other metals.");
                GregTech_API.sThaumcraftCompat.addResearch(tKey, "Antimony Transmutation", "Transformation of metals into antimony", new String[]{"GT_TRANSZINC", "TRANSLEAD"}, "ALCHEMY", GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Antimony, 1L), 2, 1, 9, 14, Arrays.asList(new TC_Aspects.TC_AspectStack[]{new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L), new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L), new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 3L)}), null, new Object[]{aTextTCGTPage + tKey, GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, OrePrefixes.nugget.get(Materials.Antimony), GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Antimony, 3L), Arrays.asList(new TC_Aspects.TC_AspectStack[]{new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 1L)}))});

                tKey = "GT_TRANSNICKEL";
                GT_LanguageManager.addStringLocalization(aTextTCGTPage + tKey, "You have discovered a way to multiply nickel by steeping nickel nuggets in metallum harvested from other metals.");
                GregTech_API.sThaumcraftCompat.addResearch(tKey, "Nickel Transmutation", "Transformation of metals into nickel", new String[]{"TRANSLEAD"}, "ALCHEMY", GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Nickel, 1L), 2, 1, 9, 15, Arrays.asList(new TC_Aspects.TC_AspectStack[]{new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L), new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L), new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 3L)}), null, new Object[]{aTextTCGTPage + tKey, GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, OrePrefixes.nugget.get(Materials.Nickel), GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Nickel, 3L), Arrays.asList(new TC_Aspects.TC_AspectStack[]{new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 1L)}))});

                tKey = "GT_TRANSCOBALT";
                GT_LanguageManager.addStringLocalization(aTextTCGTPage + tKey, "You have discovered a way to multiply cobalt by steeping cobalt nuggets in metallum harvested from other metals.");
                GregTech_API.sThaumcraftCompat.addResearch(tKey, "Cobalt Transmutation", "Transformation of metals into cobalt", new String[]{"GT_TRANSNICKEL"}, "ALCHEMY", GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Cobalt, 1L), 2, 1, 9, 16, Arrays.asList(new TC_Aspects.TC_AspectStack[]{new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L), new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L), new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 3L)}), null, new Object[]{aTextTCGTPage + tKey, GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, OrePrefixes.nugget.get(Materials.Cobalt), GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Cobalt, 3L), Arrays.asList(new TC_Aspects.TC_AspectStack[]{new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 1L)}))});

                tKey = "GT_TRANSBISMUTH";
                GT_LanguageManager.addStringLocalization(aTextTCGTPage + tKey, "You have discovered a way to multiply bismuth by steeping bismuth nuggets in metallum harvested from other metals.");
                GregTech_API.sThaumcraftCompat.addResearch(tKey, "Bismuth Transmutation", "Transformation of metals into bismuth", new String[]{"GT_TRANSCOBALT"}, "ALCHEMY", GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Bismuth, 1L), 2, 1, 11, 17, Arrays.asList(new TC_Aspects.TC_AspectStack[]{new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L), new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L), new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 3L)}), null, new Object[]{aTextTCGTPage + tKey, GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, OrePrefixes.nugget.get(Materials.Bismuth), GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Bismuth, 3L), Arrays.asList(new TC_Aspects.TC_AspectStack[]{new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 1L)}))});

                tKey = "GT_IRON_TO_STEEL";
                GT_LanguageManager.addStringLocalization(aTextTCGTPage + tKey, "You have discovered a way of making Iron harder by just re-ordering its components.<BR><BR>This Method can be used to create a Material called Steel, which is used in many non-Thaumaturgic applications.");
                GregTech_API.sThaumcraftCompat.addResearch(tKey, "Steel Transmutation", "Transforming iron to steel", new String[]{"TRANSIRON", "GT_WOOD_TO_CHARCOAL"}, "ALCHEMY", GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Steel, 1L), 3, 0, 13, 8, Arrays.asList(new TC_Aspects.TC_AspectStack[]{new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L), new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L), new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 3L)}), null, new Object[]{aTextTCGTPage + tKey, GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, OrePrefixes.nugget.get(Materials.Iron), GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Steel, 1L), Arrays.asList(new TC_Aspects.TC_AspectStack[]{new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 1L)}))});

                tKey = "GT_TRANSBRONZE";
                GT_LanguageManager.addStringLocalization(aTextTCGTPage + tKey, "You have discovered a way of creating Alloys using the already known transmutations of Copper and Tin.<BR><BR>This Method can be used to create a Bronze directly without having to go through an alloying process.");
                GregTech_API.sThaumcraftCompat.addResearch(tKey, "Bronze Transmutation", "Transformation of metals into bronze", new String[]{"TRANSTIN", "TRANSCOPPER"}, "ALCHEMY", GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Bronze, 1L), 2, 0, 13, 11, Arrays.asList(new TC_Aspects.TC_AspectStack[]{new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L), new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L), new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 3L)}), null, new Object[]{aTextTCGTPage + tKey, GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, OrePrefixes.nugget.get(Materials.Bronze), GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Bronze, 3L), Arrays.asList(new TC_Aspects.TC_AspectStack[]{new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 1L)}))});

                tKey = "GT_TRANSELECTRUM";
                GT_LanguageManager.addStringLocalization(aTextTCGTPage + tKey, "Your discovery of Bronze Transmutation has lead you to the conclusion it works with other Alloys such as Electrum as well.");
                GregTech_API.sThaumcraftCompat.addResearch(tKey, "Electrum Transmutation", "Transformation of metals into electrum", new String[]{"GT_TRANSBRONZE", "TRANSGOLD", "TRANSSILVER"}, "ALCHEMY", GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Electrum, 1L), 2, 1, 11, 11, Arrays.asList(new TC_Aspects.TC_AspectStack[]{new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L), new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L), new TC_Aspects.TC_AspectStack(TC_Aspects.LUCRUM, 3L)}), null, new Object[]{aTextTCGTPage + tKey, GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, OrePrefixes.nugget.get(Materials.Electrum), GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Electrum, 3L), Arrays.asList(new TC_Aspects.TC_AspectStack[]{new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.LUCRUM, 1L)}))});

                tKey = "GT_TRANSBRASS";
                GT_LanguageManager.addStringLocalization(aTextTCGTPage + tKey, "Your discovery of Bronze Transmutation has lead you to the conclusion it works with other Alloys such as Brass as well.");
                GregTech_API.sThaumcraftCompat.addResearch(tKey, "Brass Transmutation", "Transformation of metals into brass", new String[]{"GT_TRANSBRONZE", "GT_TRANSZINC"}, "ALCHEMY", GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Brass, 1L), 2, 1, 11, 12, Arrays.asList(new TC_Aspects.TC_AspectStack[]{new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L), new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L), new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 3L)}), null, new Object[]{aTextTCGTPage + tKey, GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, OrePrefixes.nugget.get(Materials.Brass), GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Brass, 3L), Arrays.asList(new TC_Aspects.TC_AspectStack[]{new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 1L)}))});

                tKey = "GT_TRANSINVAR";
                GT_LanguageManager.addStringLocalization(aTextTCGTPage + tKey, "Your discovery of Bronze Transmutation has lead you to the conclusion it works with other Alloys such as Invar as well.");
                GregTech_API.sThaumcraftCompat.addResearch(tKey, "Invar Transmutation", "Transformation of metals into invar", new String[]{"GT_TRANSBRONZE", "GT_TRANSNICKEL"}, "ALCHEMY", GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Invar, 1L), 2, 1, 11, 15, Arrays.asList(new TC_Aspects.TC_AspectStack[]{new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L), new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L), new TC_Aspects.TC_AspectStack(TC_Aspects.GELUM, 3L)}), null, new Object[]{aTextTCGTPage + tKey, GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, OrePrefixes.nugget.get(Materials.Invar), GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Invar, 3L), Arrays.asList(new TC_Aspects.TC_AspectStack[]{new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.GELUM, 1L)}))});

                tKey = "GT_TRANSCUPRONICKEL";
                GT_LanguageManager.addStringLocalization(aTextTCGTPage + tKey, "Your discovery of Bronze Transmutation has lead you to the conclusion it works with other Alloys such as Cupronickel as well.");
                GregTech_API.sThaumcraftCompat.addResearch(tKey, "Cupronickel Transmutation", "Transformation of metals into cupronickel", new String[]{"GT_TRANSBRONZE", "GT_TRANSNICKEL"}, "ALCHEMY", GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Cupronickel, 1L), 2, 1, 11, 16, Arrays.asList(new TC_Aspects.TC_AspectStack[]{new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L), new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L), new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L), new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 3L)}), null, new Object[]{aTextTCGTPage + tKey, GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, OrePrefixes.nugget.get(Materials.Cupronickel), GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Cupronickel, 3L), Arrays.asList(new TC_Aspects.TC_AspectStack[]{new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 1L), new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 1L)}))});

                tKey = "GT_TRANSBATTERYALLOY";
                GT_LanguageManager.addStringLocalization(aTextTCGTPage + tKey, "Your discovery of Bronze Transmutation has lead you to the conclusion it works with other Alloys such as Battery Alloy as well.");
                GregTech_API.sThaumcraftCompat.addResearch(tKey, "Battery Alloy Transmutation", "Transformation of metals into battery alloy", new String[]{"GT_TRANSBRONZE", "GT_TRANSANTIMONY"}, "ALCHEMY", GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.BatteryAlloy, 1L), 2, 1, 11, 13, Arrays.asList(new TC_Aspects.TC_AspectStack[]{new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L), new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L), new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L), new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 3L)}), null, new Object[]{aTextTCGTPage + tKey, GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, OrePrefixes.nugget.get(Materials.BatteryAlloy), GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.BatteryAlloy, 3L), Arrays.asList(new TC_Aspects.TC_AspectStack[]{new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 1L), new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 1L)}))});

                tKey = "GT_TRANSSOLDERINGALLOY";
                GT_LanguageManager.addStringLocalization(aTextTCGTPage + tKey, "Your discovery of Bronze Transmutation has lead you to the conclusion it works with other Alloys such as Soldering Alloy as well.");
                GregTech_API.sThaumcraftCompat.addResearch(tKey, "Soldering Alloy Transmutation", "Transformation of metals into soldering alloy", new String[]{"GT_TRANSBRONZE", "GT_TRANSANTIMONY"}, "ALCHEMY", GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.SolderingAlloy, 1L), 2, 1, 11, 14, Arrays.asList(new TC_Aspects.TC_AspectStack[]{new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L), new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L), new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L), new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 3L)}), null, new Object[]{aTextTCGTPage + tKey, GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, OrePrefixes.nugget.get(Materials.SolderingAlloy), GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.SolderingAlloy, 3L), Arrays.asList(new TC_Aspects.TC_AspectStack[]{new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 1L), new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 1L)}))});

                tKey = "GT_ADVANCEDMETALLURGY";
                GT_LanguageManager.addStringLocalization(aTextTCGTPage + tKey, "Now that you have discovered all the basic metals, you can finally move on to the next Level of magic metallurgy and create more advanced metals");
                GregTech_API.sThaumcraftCompat.addResearch(tKey, "Advanced Metallurgic Transmutation", "Mastering the basic metals", new String[]{"GT_TRANSBISMUTH", "GT_IRON_TO_STEEL", "GT_TRANSSOLDERINGALLOY", "GT_TRANSBATTERYALLOY", "GT_TRANSBRASS", "GT_TRANSELECTRUM", "GT_TRANSCUPRONICKEL", "GT_TRANSINVAR"}, "ALCHEMY", GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, 1L), 3, 0, 16, 14, Arrays.asList(new TC_Aspects.TC_AspectStack[]{new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 50L), new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 20L), new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 20L), new TC_Aspects.TC_AspectStack(TC_Aspects.PRAECANTATIO, 20L), new TC_Aspects.TC_AspectStack(TC_Aspects.NEBRISUM, 20L), new TC_Aspects.TC_AspectStack(TC_Aspects.MAGNETO, 20L)}), null, new Object[]{aTextTCGTPage + tKey});

                tKey = "GT_TRANSALUMINIUM";
                GT_LanguageManager.addStringLocalization(aTextTCGTPage + tKey, "You have discovered a way to multiply aluminium by steeping aluminium nuggets in metallum harvested from other metals.<BR><BR>This transmutation is slightly harder to achieve, because aluminium has special properties, which require more order to achieve the desired result.");
                GregTech_API.sThaumcraftCompat.addResearch(tKey, "Aluminium Transmutation", "Transformation of metals into aluminium", new String[]{"GT_ADVANCEDMETALLURGY"}, "ALCHEMY", GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Aluminium, 1L), 4, 0, 19, 14, Arrays.asList(new TC_Aspects.TC_AspectStack[]{new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L), new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L), new TC_Aspects.TC_AspectStack(TC_Aspects.VOLATUS, 3L), new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 3L), new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 3L)}), null, new Object[]{aTextTCGTPage + tKey, GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, OrePrefixes.nugget.get(Materials.Aluminium), GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Aluminium, 3L), Arrays.asList(new TC_Aspects.TC_AspectStack[]{new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.VOLATUS, 1L), new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 1L), new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 1L)}))});

                tKey = "GT_CRYSTALLISATION";
                GT_LanguageManager.addStringLocalization(aTextTCGTPage + tKey, "Sometimes when processing your Crystal Shards they become a pile of Dust instead of the mostly required Shard.<BR><BR>You have finally found a way to reverse this Process by using Vitreus Essentia for recrystallising the Shards.");
                GregTech_API.sThaumcraftCompat.addResearch(tKey, "Shard Recrystallisation", "Fixing your precious crystals", new String[]{"ALCHEMICALMANUFACTURE"}, "ALCHEMY", GT_OreDictUnificator.get(OrePrefixes.gem, Materials.InfusedOrder, 1L), 3, 0, -11, -3, Arrays.asList(new TC_Aspects.TC_AspectStack[]{new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 5L), new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L), new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 3L)}), null, new Object[]{aTextTCGTPage + tKey, GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, OrePrefixes.dust.get(Materials.Amber), GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Amber, 1L), Arrays.asList(new TC_Aspects.TC_AspectStack[]{new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 4L)})), GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, OrePrefixes.dust.get(Materials.InfusedOrder), GT_OreDictUnificator.get(OrePrefixes.gem, Materials.InfusedOrder, 1L), Arrays.asList(new TC_Aspects.TC_AspectStack[]{new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 4L)})), GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, OrePrefixes.dust.get(Materials.InfusedEntropy), GT_OreDictUnificator.get(OrePrefixes.gem, Materials.InfusedEntropy, 1L), Arrays.asList(new TC_Aspects.TC_AspectStack[]{new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 4L)})), GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, OrePrefixes.dust.get(Materials.InfusedAir), GT_OreDictUnificator.get(OrePrefixes.gem, Materials.InfusedAir, 1L), Arrays.asList(new TC_Aspects.TC_AspectStack[]{new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 4L)})), GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, OrePrefixes.dust.get(Materials.InfusedEarth), GT_OreDictUnificator.get(OrePrefixes.gem, Materials.InfusedEarth, 1L), Arrays.asList(new TC_Aspects.TC_AspectStack[]{new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 4L)})), GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, OrePrefixes.dust.get(Materials.InfusedFire), GT_OreDictUnificator.get(OrePrefixes.gem, Materials.InfusedFire, 1L), Arrays.asList(new TC_Aspects.TC_AspectStack[]{new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 4L)})), GregTech_API.sThaumcraftCompat.addCrucibleRecipe(tKey, OrePrefixes.dust.get(Materials.InfusedWater), GT_OreDictUnificator.get(OrePrefixes.gem, Materials.InfusedWater, 1L), Arrays.asList(new TC_Aspects.TC_AspectStack[]{new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 4L)}))});

                tKey = "GT_MAGICENERGY";
                GT_LanguageManager.addStringLocalization(aTextTCGTPage + tKey,
                        "While trying to find new ways to integrate magic into your industrial factories, you have discovered a way to convert magical energy into electrical power.");
                GregTech_API.sThaumcraftCompat.addResearch(tKey,
                        "Magic Energy Conversion",
                        "Magic to Power",
                        new String[]{"ARCANEBORE"},
                        "ARTIFICE",
                        ItemList.MagicEnergyConverter_LV.get(1L, new Object[0]),
                        3, 0, -3, 10,
                        Arrays.asList(new TC_Aspects.TC_AspectStack[]{
                                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 10L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 10L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 20L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 10L)}),
                        null, new Object[]{aTextTCGTPage + tKey,
                                GregTech_API.sThaumcraftCompat.addInfusionRecipe(tKey,
                                        ItemList.Hull_LV.get(1L, new Object[0]),
                                        new ItemStack[]{
                                                new ItemStack(Blocks.beacon),
                                                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 1L),
                                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Thaumium, 1L),
                                                ItemList.Sensor_MV.get(2L, new Object[0]),
                                                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 1L),
                                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Thaumium, 1L),
                                                ItemList.Sensor_MV.get(2L, new Object[0])
                                        },
                                        ItemList.MagicEnergyConverter_LV.get(1L, new Object[0]),
                                        5,
                                        Arrays.asList(new TC_Aspects.TC_AspectStack[]{
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 32L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 16L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 32L)}))});

                tKey = "GT_MAGICENERGY2";
                GT_LanguageManager.addStringLocalization(aTextTCGTPage + tKey,
                        "Attempts to increase the output of your Magic Energy generators have resulted in significant improvements.");
                GregTech_API.sThaumcraftCompat.addResearch(tKey,
                        "Adept Magic Energy Conversion",
                        "Magic to Power",
                        new String[]{"GT_MAGICENERGY"},
                        "ARTIFICE",
                        ItemList.MagicEnergyConverter_MV.get(1L, new Object[0]),
                        1, 1, -4, 12,
                        Arrays.asList(new TC_Aspects.TC_AspectStack[]{
                                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 10L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 10L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 20L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 10L)}),
                        null, new Object[]{aTextTCGTPage + tKey,
                                GregTech_API.sThaumcraftCompat.addInfusionRecipe(tKey,
                                        ItemList.Hull_MV.get(1L, new Object[0]),
                                        new ItemStack[]{
                                                new ItemStack(Blocks.beacon),
                                                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Data, 1L),
                                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Thaumium, 1L),
                                                ItemList.Sensor_HV.get(2L, new Object[0]),
                                                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Data, 1L),
                                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 1L),
                                                ItemList.Sensor_HV.get(2L, new Object[0])
                                        },
                                        ItemList.MagicEnergyConverter_MV.get(1L, new Object[0]),
                                        6,
                                        Arrays.asList(new TC_Aspects.TC_AspectStack[]{
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 64L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 32L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 64L)}))});

                tKey = "GT_MAGICENERGY3";
                GT_LanguageManager.addStringLocalization(aTextTCGTPage + tKey,
                        "Attempts to further increase the output of your Magic Energy generators have resulted in great improvements.");
                GregTech_API.sThaumcraftCompat.addResearch(tKey,
                        "Master Magic Energy Conversion",
                        "Magic to Power",
                        new String[]{"GT_MAGICENERGY2"},
                        "ARTIFICE",
                        ItemList.MagicEnergyConverter_HV.get(1L, new Object[0]),
                        1, 1, -4, 14,
                        Arrays.asList(new TC_Aspects.TC_AspectStack[]{
                                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 20L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 20L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 40L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 20L)}),
                        null, new Object[]{aTextTCGTPage + tKey,
                                GregTech_API.sThaumcraftCompat.addInfusionRecipe(tKey,
                                        ItemList.Hull_HV.get(1L, new Object[0]),
                                        new ItemStack[]{
                                                new ItemStack(Blocks.beacon),
                                                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Elite, 1L),
                                                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Thaumium, 1L),
                                                ItemList.Field_Generator_MV.get(1L, new Object[0]),
                                                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Elite, 1L),
                                                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Osmium, 1L),
                                                ItemList.Field_Generator_MV.get(1L, new Object[0])
                                        },
                                        ItemList.MagicEnergyConverter_HV.get(1L, new Object[0]),
                                        8,
                                        Arrays.asList(new TC_Aspects.TC_AspectStack[]{
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 128L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 128L)}))});


                tKey = "GT_MAGICABSORB";
                GT_LanguageManager.addStringLocalization(aTextTCGTPage + tKey,
                        "Research into magical energy conversion methods has identified a way to convert surrounding energies into electrical power.");
                GregTech_API.sThaumcraftCompat.addResearch(tKey,
                        "Magic Energy Absorption",
                        "Harvesting Magic",
                        new String[]{"GT_MAGICENERGY"},
                        "ARTIFICE",
                        ItemList.MagicEnergyAbsorber_LV.get(1L, new Object[0]),
                        3, 0, -2, 12,
                        Arrays.asList(new TC_Aspects.TC_AspectStack[]{
                                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 10L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 10L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 20L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 10L)}),
                        null, new Object[]{aTextTCGTPage + tKey,
                                GregTech_API.sThaumcraftCompat.addInfusionRecipe(tKey,
                                        ItemList.Hull_LV.get(1L, new Object[0]),
                                        new ItemStack[]{
                                                ItemList.MagicEnergyConverter_LV.get(1L, new Object[0]),
                                                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 1L),
                                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Thaumium, 1L),
                                                ItemList.Sensor_MV.get(2L, new Object[0])
                                        },
                                        ItemList.MagicEnergyAbsorber_LV.get(1L, new Object[0]),
                                        6,
                                        Arrays.asList(new TC_Aspects.TC_AspectStack[]{
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 32L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 16L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 32L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 16L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 32L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.STRONTIO, 4L)}))});

                tKey = "GT_MAGICABSORB2";
                GT_LanguageManager.addStringLocalization(aTextTCGTPage + tKey,
                        "Moar output! Drain all the Magic!");
                GregTech_API.sThaumcraftCompat.addResearch(tKey,
                        "Improved Magic Energy Absorption",
                        "Harvesting Magic",
                        new String[]{"GT_MAGICABSORB"},
                        "ARTIFICE",
                        ItemList.MagicEnergyAbsorber_EV.get(1L, new Object[0]),
                        3, 1, -2, 14,
                        Arrays.asList(new TC_Aspects.TC_AspectStack[]{
                                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 10L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 10L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 20L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 10L)}),
                        null, new Object[]{aTextTCGTPage + tKey,
                                GregTech_API.sThaumcraftCompat.addInfusionRecipe(tKey,
                                        ItemList.Hull_MV.get(1L, new Object[0]),
                                        new ItemStack[]{
                                                ItemList.MagicEnergyConverter_MV.get(1L, new Object[0]),
                                                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 1L),
                                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Thaumium, 1L),
                                                ItemList.Sensor_HV.get(2L, new Object[0]),
                                                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 1L),
                                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Thaumium, 1L)
                                        },
                                        ItemList.MagicEnergyAbsorber_MV.get(1L, new Object[0]),
                                        6,
                                        Arrays.asList(new TC_Aspects.TC_AspectStack[]{
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 64L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 32L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 64L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 32L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 64L),
                                                new TC_Aspects.TC_AspectStack(TC_Aspects.STRONTIO, 8L)}))


                                , GregTech_API.sThaumcraftCompat.addInfusionRecipe(tKey,
                                ItemList.Hull_HV.get(1L, new Object[0]),
                                new ItemStack[]{
                                        ItemList.MagicEnergyConverter_MV.get(1L, new Object[0]),
                                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Elite, 1L),
                                        GT_ModHandler.getModItem("Thaumcraft", "ItemResource", 1, 16),
                                        ItemList.Field_Generator_MV.get(1L, new Object[0]),
                                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Elite, 1L),
                                        GT_ModHandler.getModItem("Thaumcraft", "ItemResource", 1, 16)
                                },
                                ItemList.MagicEnergyAbsorber_HV.get(1L, new Object[0]),
                                8,
                                Arrays.asList(new TC_Aspects.TC_AspectStack[]{
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 128L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 128L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 64L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 128L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.STRONTIO, 16L)}))


                                , GregTech_API.sThaumcraftCompat.addInfusionRecipe(tKey,
                                ItemList.Hull_EV.get(1L, new Object[0]),
                                new ItemStack[]{
                                        ItemList.MagicEnergyConverter_HV.get(1L, new Object[0]),
                                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Master, 1L),
                                        GT_ModHandler.getModItem("Thaumcraft", "ItemResource", 1, 16),
                                        ItemList.Field_Generator_HV.get(1L, new Object[0]),
                                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Master, 1L),
                                        GT_ModHandler.getModItem("Thaumcraft", "ItemResource", 1, 16)
                                },
                                ItemList.MagicEnergyAbsorber_EV.get(1L, new Object[0]),
                                10,
                                Arrays.asList(new TC_Aspects.TC_AspectStack[]{
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 256L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 128L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 256L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 128L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 256L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.STRONTIO, 64L)}))
                        });
            }

	}
	
	/**
	 * Adds recipes related to the processing of Charcoal Byproducts, Fermented Biomass
	 * Adds recipes related to the production of Glue, Gunpowder, Polyvinyl Chloride
	 * Adds replacement recipes for Epoxy Resin, Nitric Acid, Polyethylene, Polydimethylsiloxane (Silicone), Polytetrafluoroethylene, Rocket Fuel, Sulfuric Acid
	 * Instrumental materials are not mentioned here.
	 */
	private void addRecipesApril2017ChemistryUpdate(){
        GT_Values.RA.addElectrolyzerRecipe(GT_Utility.getIntegratedCircuit(1),  GT_Values.NI,                Materials.CarbonDioxide.getGas(1000), Materials.Oxygen.getGas(2000), Materials.Carbon.getDust(1), GT_Values.NI,                 GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 300, 120);
        GT_Values.RA.addElectrolyzerRecipe(GT_Utility.getIntegratedCircuit(11), Materials.Empty.getCells(2), Materials.CarbonDioxide.getGas(1000), GT_Values.NF,                  Materials.Carbon.getDust(1), Materials.Oxygen.getCells(2), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 300, 120);
        GT_Values.RA.addElectrolyzerRecipe(GT_Utility.getIntegratedCircuit(1),  GT_Values.NI,                Materials.SulfurDioxide.getGas(1000), Materials.Oxygen.getGas(2000), Materials.Sulfur.getDust(1), GT_Values.NI,                 GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 300, 120);
        GT_Values.RA.addElectrolyzerRecipe(GT_Utility.getIntegratedCircuit(11), Materials.Empty.getCells(2), Materials.SulfurDioxide.getGas(1000), GT_Values.NF,                  Materials.Sulfur.getDust(1), Materials.Oxygen.getCells(2), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 300, 120);
        GT_Values.RA.addElectrolyzerRecipe(Materials.Salt.getDust(2),   GT_Values.NI,                        GT_Values.NF,                                     Materials.Chlorine.getGas(1000), Materials.Sodium.getDust(1),          GT_Values.NI,                   GT_Values.NI, GT_Values.NI,GT_Values.NI, GT_Values.NI, null, 320, 30);
		GT_Values.RA.addElectrolyzerRecipe(Materials.Empty.getCells(1), GT_Utility.getIntegratedCircuit(1),  Materials.SaltWater.getFluid(2000),               Materials.Chlorine.getGas(1000), Materials.SodiumHydroxide.getDust(1), Materials.Hydrogen.getCells(1), GT_Values.NI, GT_Values.NI,GT_Values.NI, GT_Values.NI, null, 720, 30);
		GT_Values.RA.addElectrolyzerRecipe(Materials.Empty.getCells(1), GT_Utility.getIntegratedCircuit(11), Materials.SaltWater.getFluid(2000),               Materials.Hydrogen.getGas(1000), Materials.SodiumHydroxide.getDust(1), Materials.Chlorine.getCells(1), GT_Values.NI, GT_Values.NI,GT_Values.NI, GT_Values.NI, null, 720, 30);
		GT_Values.RA.addElectrolyzerRecipe(Materials.Empty.getCells(1), GT_Utility.getIntegratedCircuit(1),  Materials.HydrochloricAcid.getFluid(1000),        Materials.Chlorine.getGas(1000), Materials.Hydrogen.getCells(1),       GT_Values.NI,                   GT_Values.NI, GT_Values.NI,GT_Values.NI, GT_Values.NI, null, 720, 30);
		GT_Values.RA.addElectrolyzerRecipe(Materials.Empty.getCells(1), GT_Utility.getIntegratedCircuit(11), Materials.HydrochloricAcid.getFluid(1000),        Materials.Hydrogen.getGas(1000), Materials.Chlorine.getCells(1),       GT_Values.NI,                   GT_Values.NI, GT_Values.NI,GT_Values.NI, GT_Values.NI, null, 720, 30);
        GT_Values.RA.addElectrolyzerRecipe(Materials.HydrochloricAcid.getCells(1), GT_Utility.getIntegratedCircuit(1),  GT_Values.NF,        Materials.Chlorine.getGas(1000), Materials.Hydrogen.getCells(1),       GT_Values.NI,                   GT_Values.NI, GT_Values.NI,GT_Values.NI, GT_Values.NI, null, 720, 30);
        GT_Values.RA.addElectrolyzerRecipe(Materials.HydrochloricAcid.getCells(1), GT_Utility.getIntegratedCircuit(11), GT_Values.NF,        Materials.Hydrogen.getGas(1000), Materials.Chlorine.getCells(1),       GT_Values.NI,                   GT_Values.NI, GT_Values.NI,GT_Values.NI, GT_Values.NI, null, 720, 30);

        GT_Values.RA.addUniversalDistillationRecipe(Materials.DilutedHydrochloricAcid.getFluid(2000), new FluidStack[]{Materials.Water.getFluid(1000), Materials.HydrochloricAcid.getFluid(1000)}, GT_Values.NI, 600, 64);
		
		GT_Values.RA.addChemicalRecipe(Materials.Potassium.getDust(1), Materials.Oxygen.getCells(3), Materials.Nitrogen.getGas(1000), GT_Values.NF, Materials.Saltpeter.getDust(1), Materials.Empty.getCells(3), 180);
		GT_Values.RA.addChemicalRecipe(Materials.Potassium.getDust(1), Materials.Nitrogen.getCells(1), Materials.Oxygen.getGas(3000), GT_Values.NF, Materials.Saltpeter.getDust(1), Materials.Empty.getCells(1), 180);
		
		GT_Values.RA.addMixerRecipe(Materials.Salt.getDust(1), GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.Water.getFluid(1000), Materials.SaltWater.getFluid(2000), GT_Values.NI, 40, 8);
		GT_Values.RA.addDistilleryRecipe(1, Materials.SaltWater.getFluid(2000), GT_ModHandler.getDistilledWater(1000), Materials.Salt.getDust(1), 3200, 16, false);

        GT_Values.RA.addUniversalDistillationRecipe(FluidRegistry.getFluidStack("potion.vinegar", 40), new FluidStack[]{Materials.AceticAcid.getFluid(5), Materials.Water.getFluid(35)}, GT_Values.NI, 20, 64);
        GT_Values.RA.addMixerRecipe(Materials.Calcite.getDust(1),        GT_Utility.getIntegratedCircuit(1),  GT_Values.NI, GT_Values.NI, Materials.AceticAcid.getFluid(2000), Materials.CalciumAcetateSolution.getFluid(1000), GT_Values.NI, 240, 16);
        GT_Values.RA.addMixerRecipe(Materials.Calcium.getDust(1),        GT_Utility.getIntegratedCircuit(1),  GT_Values.NI, GT_Values.NI, Materials.AceticAcid.getFluid(2000), Materials.CalciumAcetateSolution.getFluid(1000), GT_Values.NI, 80, 16);
        GT_Values.RA.addMixerRecipe(Materials.Quicklime.getDust(1),      GT_Utility.getIntegratedCircuit(1),  GT_Values.NI, GT_Values.NI, Materials.AceticAcid.getFluid(2000), Materials.CalciumAcetateSolution.getFluid(1000), GT_Values.NI, 80, 16);
        GT_Values.RA.addMixerRecipe(Materials.Quicklime.getDustSmall(4), GT_Utility.getIntegratedCircuit(1),  GT_Values.NI, GT_Values.NI, Materials.AceticAcid.getFluid(2000), Materials.CalciumAcetateSolution.getFluid(1000), GT_Values.NI, 80, 16);
        GT_Values.RA.addMixerRecipe(Materials.Calcite.getDust(1),        Materials.Empty.getCells(1), GT_Utility.getIntegratedCircuit(11), GT_Values.NI, Materials.AceticAcid.getFluid(2000), GT_Values.NF, Materials.CalciumAcetateSolution.getCells(1), 240, 16);
        GT_Values.RA.addMixerRecipe(Materials.Calcium.getDust(1),        Materials.Empty.getCells(1), GT_Utility.getIntegratedCircuit(11), GT_Values.NI, Materials.AceticAcid.getFluid(2000), GT_Values.NF, Materials.CalciumAcetateSolution.getCells(1), 80, 16);
        GT_Values.RA.addMixerRecipe(Materials.Quicklime.getDust(1),      Materials.Empty.getCells(1), GT_Utility.getIntegratedCircuit(11), GT_Values.NI, Materials.AceticAcid.getFluid(2000), GT_Values.NF, Materials.CalciumAcetateSolution.getCells(1), 80, 16);
        GT_Values.RA.addMixerRecipe(Materials.Quicklime.getDustSmall(4), Materials.Empty.getCells(1), GT_Utility.getIntegratedCircuit(11), GT_Values.NI, Materials.AceticAcid.getFluid(2000), GT_Values.NF, Materials.CalciumAcetateSolution.getCells(1), 80, 16);
        //GameRegistry.addSmelting(Materials.CalciumAcetateSolution.getCells(1), Materials.Acetone.getCells(1), 0);
        GT_Values.RA.addFluidHeaterRecipe(GT_Utility.getIntegratedCircuit(1), Materials.CalciumAcetateSolution.getFluid(1000), Materials.Acetone.getFluid(1000), 80, 30);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.CalciumAcetateSolution.getFluid(1000), new FluidStack[]{Materials.Acetone.getFluid(1000), Materials.CarbonDioxide.getGas(1000)}, Materials.Quicklime.getDustSmall(3), 80, 480);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{Materials.Calcite.getDust(1),   GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.AceticAcid.getFluid(4000)}, new FluidStack[]{Materials.Acetone.getFluid(4000), Materials.CarbonDioxide.getGas(4000)}, null, 400, 480);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{Materials.Calcium.getDust(1),   GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.AceticAcid.getFluid(4000)}, new FluidStack[]{Materials.Acetone.getFluid(4000), Materials.CarbonDioxide.getGas(4000)}, null, 400, 480);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{Materials.Quicklime.getDust(1), GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.AceticAcid.getFluid(4000)}, new FluidStack[]{Materials.Acetone.getFluid(4000), Materials.CarbonDioxide.getGas(4000)}, null, 400, 480);

        GT_Values.RA.addChemicalRecipe(Materials.AceticAcid.getCells(1), GT_Utility.getIntegratedCircuit(1),  Materials.Methanol.getFluid(1000),   Materials.MethylAcetate.getFluid(1000), Materials.Water.getCells(1), 240);
        GT_Values.RA.addChemicalRecipe(Materials.Methanol.getCells(1),   GT_Utility.getIntegratedCircuit(1),  Materials.AceticAcid.getFluid(1000), Materials.MethylAcetate.getFluid(1000), Materials.Water.getCells(1), 240);
        GT_Values.RA.addChemicalRecipe(Materials.AceticAcid.getCells(1), GT_Utility.getIntegratedCircuit(2),  Materials.Methanol.getFluid(1000),   Materials.MethylAcetate.getFluid(1000), Materials.Empty.getCells(1), 240);
        GT_Values.RA.addChemicalRecipe(Materials.Methanol.getCells(1),   GT_Utility.getIntegratedCircuit(2),  Materials.AceticAcid.getFluid(1000), Materials.MethylAcetate.getFluid(1000), Materials.Empty.getCells(1), 240);
        GT_Values.RA.addChemicalRecipe(Materials.AceticAcid.getCells(1), GT_Utility.getIntegratedCircuit(11), Materials.Methanol.getFluid(1000),   Materials.Water.getFluid(1000),         Materials.MethylAcetate.getCells(1), 240);
        GT_Values.RA.addChemicalRecipe(Materials.Methanol.getCells(1),   GT_Utility.getIntegratedCircuit(11), Materials.AceticAcid.getFluid(1000), Materials.Water.getFluid(1000),         Materials.MethylAcetate.getCells(1), 240);
        GT_Values.RA.addChemicalRecipe(Materials.AceticAcid.getCells(1), GT_Utility.getIntegratedCircuit(12), Materials.Methanol.getFluid(1000),   GT_Values.NF,                           Materials.MethylAcetate.getCells(1), 240);
        GT_Values.RA.addChemicalRecipe(Materials.Methanol.getCells(1),   GT_Utility.getIntegratedCircuit(12), Materials.AceticAcid.getFluid(1000), GT_Values.NF,                           Materials.MethylAcetate.getCells(1), 240);

        GT_Values.RA.addMixerRecipe(Materials.Acetone.getCells(3), GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.PolyvinylAcetate.getFluid(2000), Materials.Glue.getFluid(5000), Materials.Empty.getCells(3), 100, 8);
        GT_Values.RA.addMixerRecipe(Materials.PolyvinylAcetate.getCells(2), GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.Acetone.getFluid(3000), Materials.Glue.getFluid(5000), Materials.Empty.getCells(2), 100, 8);
        GT_Values.RA.addMixerRecipe(Materials.MethylAcetate.getCells(3), GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.PolyvinylAcetate.getFluid(2000), Materials.Glue.getFluid(5000), Materials.Empty.getCells(3), 100, 8);
        GT_Values.RA.addMixerRecipe(Materials.PolyvinylAcetate.getCells(2), GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.MethylAcetate.getFluid(3000), Materials.Glue.getFluid(5000), Materials.Empty.getCells(2), 100, 8);

        GT_Values.RA.addChemicalRecipe(Materials.Carbon.getDust(1),   GT_Utility.getIntegratedCircuit(1), Materials.Oxygen.getGas(1000), Materials.CarbonMonoxide.getGas(1000), GT_Values.NI, 40, 8);
        GT_Values.RA.addChemicalRecipe(Materials.Coal.getGems(1),     GT_Utility.getIntegratedCircuit(1), Materials.Oxygen.getGas(1000), Materials.CarbonMonoxide.getGas(1000), Materials.Ash.getDustTiny(1), 80, 8);
        GT_Values.RA.addChemicalRecipe(Materials.Coal.getDust(1),     GT_Utility.getIntegratedCircuit(1), Materials.Oxygen.getGas(1000), Materials.CarbonMonoxide.getGas(1000), Materials.Ash.getDustTiny(1), 80, 8);
        GT_Values.RA.addChemicalRecipe(Materials.Charcoal.getGems(1), GT_Utility.getIntegratedCircuit(1), Materials.Oxygen.getGas(1000), Materials.CarbonMonoxide.getGas(1000), Materials.Ash.getDustTiny(1), 80, 8);
        GT_Values.RA.addChemicalRecipe(Materials.Charcoal.getDust(1), GT_Utility.getIntegratedCircuit(1), Materials.Oxygen.getGas(1000), Materials.CarbonMonoxide.getGas(1000), Materials.Ash.getDustTiny(1), 80, 8);
        GT_Values.RA.addChemicalRecipe(Materials.Carbon.getDust(1),   GT_Utility.getIntegratedCircuit(2), Materials.Oxygen.getGas(2000), Materials.CarbonDioxide.getGas(1000), GT_Values.NI, 40, 8);
        GT_Values.RA.addChemicalRecipe(Materials.Coal.getGems(1),     GT_Utility.getIntegratedCircuit(2), Materials.Oxygen.getGas(2000), Materials.CarbonDioxide.getGas(1000), Materials.Ash.getDustTiny(1), 40, 8);
        GT_Values.RA.addChemicalRecipe(Materials.Coal.getDust(1),     GT_Utility.getIntegratedCircuit(2), Materials.Oxygen.getGas(2000), Materials.CarbonDioxide.getGas(1000), Materials.Ash.getDustTiny(1), 40, 8);
        GT_Values.RA.addChemicalRecipe(Materials.Charcoal.getGems(1), GT_Utility.getIntegratedCircuit(2), Materials.Oxygen.getGas(2000), Materials.CarbonDioxide.getGas(1000), Materials.Ash.getDustTiny(1), 40, 8);
        GT_Values.RA.addChemicalRecipe(Materials.Charcoal.getDust(1), GT_Utility.getIntegratedCircuit(2), Materials.Oxygen.getGas(2000), Materials.CarbonDioxide.getGas(1000), Materials.Ash.getDustTiny(1), 40, 8);
        GT_Values.RA.addChemicalRecipe(Materials.Carbon.getDust(1), GT_Values.NI, Materials.CarbonDioxide.getGas(1000), Materials.CarbonMonoxide.getGas(2000), GT_Values.NI, 800);

        GT_Values.RA.addChemicalRecipe(Materials.CarbonMonoxide.getCells(1), GT_Utility.getIntegratedCircuit(1),  Materials.Hydrogen.getGas(4000),       Materials.Methanol.getFluid(1000), Materials.Empty.getCells(1), 120, 96);
        GT_Values.RA.addChemicalRecipe(Materials.Hydrogen.getCells(4),       GT_Utility.getIntegratedCircuit(1),  Materials.CarbonMonoxide.getGas(1000), Materials.Methanol.getFluid(1000), Materials.Empty.getCells(4), 120, 96);
        GT_Values.RA.addChemicalRecipe(Materials.CarbonMonoxide.getCells(1), GT_Utility.getIntegratedCircuit(11), Materials.Hydrogen.getGas(4000),       GT_Values.NF, Materials.Methanol.getCells(1), 120, 96);
        GT_Values.RA.addChemicalRecipe(Materials.Hydrogen.getCells(4),       GT_Utility.getIntegratedCircuit(11), Materials.CarbonMonoxide.getGas(1000), GT_Values.NF, Materials.Methanol.getCells(1), Materials.Empty.getCells(3), 120, 96);
        GT_Values.RA.addChemicalRecipe(Materials.CarbonDioxide.getCells(1),  GT_Utility.getIntegratedCircuit(1),  Materials.Hydrogen.getGas(6000),       Materials.Methanol.getFluid(1000), Materials.Water.getCells(1), 120, 96);
        GT_Values.RA.addChemicalRecipe(Materials.Hydrogen.getCells(6),       GT_Utility.getIntegratedCircuit(1),  Materials.CarbonDioxide.getGas(1000),  Materials.Methanol.getFluid(1000), Materials.Water.getCells(1), Materials.Empty.getCells(3), 120, 96);
        GT_Values.RA.addChemicalRecipe(Materials.CarbonDioxide.getCells(1),  GT_Utility.getIntegratedCircuit(2),  Materials.Hydrogen.getGas(6000),       Materials.Methanol.getFluid(1000), Materials.Empty.getCells(1), 120, 96);
        GT_Values.RA.addChemicalRecipe(Materials.Hydrogen.getCells(6),       GT_Utility.getIntegratedCircuit(2),  Materials.CarbonDioxide.getGas(1000),  Materials.Methanol.getFluid(1000), Materials.Empty.getCells(6), 120, 96);
        GT_Values.RA.addChemicalRecipe(Materials.CarbonDioxide.getCells(1),  GT_Utility.getIntegratedCircuit(12), Materials.Hydrogen.getGas(6000),       GT_Values.NF, Materials.Methanol.getCells(1), 120, 96);
        GT_Values.RA.addChemicalRecipe(Materials.Hydrogen.getCells(6),       GT_Utility.getIntegratedCircuit(12), Materials.CarbonDioxide.getGas(1000),  GT_Values.NF, Materials.Methanol.getCells(1), Materials.Empty.getCells(5), 120, 96);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{Materials.Carbon.getDust(1), GT_Utility.getIntegratedCircuit(23)}, new FluidStack[]{Materials.Hydrogen.getGas(4000), Materials.Oxygen.getGas(1000)}, new FluidStack[]{Materials.Methanol.getFluid(1000)}, null, 320, 96);

        GT_Values.RA.addChemicalRecipe(Materials.Methanol.getCells(1), GT_Utility.getIntegratedCircuit(1), Materials.CarbonMonoxide.getGas(1000), Materials.AceticAcid.getFluid(1000), Materials.Empty.getCells(1), 300);
        GT_Values.RA.addChemicalRecipe(Materials.CarbonMonoxide.getCells(1), GT_Utility.getIntegratedCircuit(1), Materials.Methanol.getFluid(1000), Materials.AceticAcid.getFluid(1000), Materials.Empty.getCells(1), 300);
        GT_Values.RA.addChemicalRecipe(Materials.Methanol.getCells(1), GT_Utility.getIntegratedCircuit(11), Materials.CarbonMonoxide.getGas(1000), GT_Values.NF, Materials.AceticAcid.getCells(1), 300);
        GT_Values.RA.addChemicalRecipe(Materials.CarbonMonoxide.getCells(1), GT_Utility.getIntegratedCircuit(11), Materials.Methanol.getFluid(1000), GT_Values.NF, Materials.AceticAcid.getCells(1), 300);
        GT_Values.RA.addChemicalRecipe(Materials.Ethylene.getCells(1), GT_Utility.getIntegratedCircuit(9), Materials.Oxygen.getGas(2000), Materials.AceticAcid.getFluid(1000), Materials.Empty.getCells(1), 100);
        GT_Values.RA.addChemicalRecipe(Materials.Oxygen.getCells(2), GT_Utility.getIntegratedCircuit(9), Materials.Ethylene.getGas(1000), Materials.AceticAcid.getFluid(1000), Materials.Empty.getCells(2), 100);
        GT_Values.RA.addChemicalRecipe(Materials.Ethylene.getCells(1), GT_Utility.getIntegratedCircuit(19), Materials.Oxygen.getGas(2000), GT_Values.NF, Materials.AceticAcid.getCells(1), 100);
        GT_Values.RA.addChemicalRecipe(Materials.Oxygen.getCells(2), GT_Utility.getIntegratedCircuit(19), Materials.Ethylene.getGas(1000), GT_Values.NF, Materials.AceticAcid.getCells(1), Materials.Empty.getCells(1), 100);
        //This recipe collides with one for Vinyl Chloride
        //GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Oxygen.getCells(2), Materials.Ethylene.getCells(1), GT_Values.NF, Materials.AceticAcid.getFluid(1000), Materials.Empty.getCells(3), GT_Values.NI, 100, 30);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{Materials.Carbon.getDust(2), GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.Hydrogen.getGas(4000), Materials.Oxygen.getGas(2000)}, new FluidStack[]{Materials.AceticAcid.getFluid(1000)}, null, 480, 30);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.CarbonMonoxide.getGas(2000), Materials.Hydrogen.getGas(4000)}, new FluidStack[]{Materials.AceticAcid.getFluid(1000)}, null, 320, 30);

        GT_Values.RA.addFermentingRecipe(Materials.Biomass.getFluid(100), Materials.FermentedBiomass.getFluid(100), 150, false);
        GT_Values.RA.addFermentingRecipe(new FluidStack(FluidRegistry.getFluid("ic2biomass"), 100), Materials.FermentedBiomass.getFluid(100), 150, false);

        GT_Values.RA.addPyrolyseRecipe(GT_ModHandler.getIC2Item("biochaff", 1), Materials.Water.getFluid(1500), 2, GT_Values.NI, Materials.FermentedBiomass.getFluid(1500), 200, 10);
        GT_Values.RA.addPyrolyseRecipe(GT_Values.NI, new FluidStack(FluidRegistry.getFluid("ic2biomass"), 1000), 2, GT_Values.NI, Materials.FermentedBiomass.getFluid(1000), 100, 10);
        GT_Values.RA.addPyrolyseRecipe(GT_Values.NI, Materials.Biomass.getFluid(1000), 2, GT_Values.NI, Materials.FermentedBiomass.getFluid(1000), 100, 10);

        GT_Values.RA.addDistillationTowerRecipe(Materials.FermentedBiomass.getFluid(1000), new FluidStack[]{
                Materials.AceticAcid.getFluid(25), Materials.Water.getFluid(375), Materials.Ethanol.getFluid(150),
                Materials.Methanol.getFluid(150),Materials.Ammonia.getGas(100), Materials.CarbonDioxide.getGas(400),
                Materials.Methane.getGas(600)}, ItemList.IC2_Fertilizer.get(1, new Object[0]), 75, 180);
        GT_Values.RA.addDistilleryRecipe(1, Materials.FermentedBiomass.getFluid(1000), Materials.AceticAcid.getFluid(25),   ItemList.IC2_Fertilizer.get(1, new Object[0]), 1500, 8, false);
        GT_Values.RA.addDistilleryRecipe(2, Materials.FermentedBiomass.getFluid(1000), Materials.Water.getFluid(375),       ItemList.IC2_Fertilizer.get(1, new Object[0]), 1500, 8, false);
        GT_Values.RA.addDistilleryRecipe(3, Materials.FermentedBiomass.getFluid(1000), Materials.Ethanol.getFluid(150),     ItemList.IC2_Fertilizer.get(1, new Object[0]), 1500, 8, false);
        GT_Values.RA.addDistilleryRecipe(4, Materials.FermentedBiomass.getFluid(1000), Materials.Methanol.getFluid(150),    ItemList.IC2_Fertilizer.get(1, new Object[0]), 1500, 8, false);
        GT_Values.RA.addDistilleryRecipe(5, Materials.FermentedBiomass.getFluid(1000), Materials.Ammonia.getGas(100),        ItemList.IC2_Fertilizer.get(1, new Object[0]), 1500, 8, false);
        GT_Values.RA.addDistilleryRecipe(6, Materials.FermentedBiomass.getFluid(1000), Materials.CarbonDioxide.getGas(400), ItemList.IC2_Fertilizer.get(1, new Object[0]), 1500, 8, false);
        GT_Values.RA.addDistilleryRecipe(7, Materials.FermentedBiomass.getFluid(1000), Materials.Methane.getGas(600),       ItemList.IC2_Fertilizer.get(1, new Object[0]), 1500, 8, false);

        GT_Values.RA.addDistilleryRecipe(17, Materials.FermentedBiomass.getFluid(1000), new FluidStack(FluidRegistry.getFluid("ic2biogas"), 1800), ItemList.IC2_Fertilizer.get(1, new Object[0]), 1600, 8, false);
        GT_Values.RA.addDistilleryRecipe(1, Materials.Methane.getGas(1000), new FluidStack(FluidRegistry.getFluid("ic2biogas"), 3000), GT_Values.NI, 160, 8, false);

        GT_Values.RA.addPyrolyseRecipe(Materials.Sugar.getDust(23), 								  GT_Values.NF, 					1, Materials.Charcoal.getDust(12),   Materials.Water.getFluid(1500), 				320, 64);
        GT_Values.RA.addPyrolyseRecipe(Materials.Sugar.getDust(23), 								  Materials.Nitrogen.getGas(500),   2, Materials.Charcoal.getDust(12),   Materials.Water.getFluid(1500), 				160, 96);

        GT_Values.RA.addUniversalDistillationRecipe(Materials.CharcoalByproducts.getGas(1000),
                new FluidStack[]{Materials.WoodTar.getFluid(250), Materials.WoodVinegar.getFluid(500), Materials.WoodGas.getGas(250)},
                Materials.Charcoal.getDustSmall(1),  40, 256);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.WoodGas.getGas(1000),
                new FluidStack[]{Materials.CarbonDioxide.getGas(490), Materials.Ethylene.getGas(20), Materials.Methane.getGas(130), Materials.CarbonMonoxide.getGas(340), Materials.Hydrogen.getGas(20)},
                GT_Values.NI,  40, 256);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.WoodVinegar.getFluid(1000),
                new FluidStack[]{Materials.AceticAcid.getFluid(100), Materials.Water.getFluid(500), Materials.Ethanol.getFluid(10), Materials.Methanol.getFluid(300), Materials.Acetone.getFluid(50), Materials.MethylAcetate.getFluid(10)},
                GT_Values.NI,  40, 256);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.WoodTar.getFluid(1000),
                new FluidStack[]{Materials.Creosote.getFluid(500), Materials.Phenol.getFluid(75), Materials.Benzene.getFluid(350), Materials.Toluene.getFluid(75)},
                GT_Values.NI,  40, 256);

        GT_Values.RA.addChemicalRecipe(Materials.Ethylene.getCells(1), 	Materials.AceticAcid.getCells(1), 	Materials.Oxygen.getGas(1000), 			Materials.VinylAcetate.getFluid(1000),Materials.Water.getCells(1), Materials.Empty.getCells(1), 180);
        GT_Values.RA.addChemicalRecipe(Materials.AceticAcid.getCells(1),Materials.Oxygen.getCells(1), 		Materials.Ethylene.getGas(1000), 		Materials.VinylAcetate.getFluid(1000),Materials.Water.getCells(1), Materials.Empty.getCells(1), 180);
        GT_Values.RA.addChemicalRecipe(Materials.Oxygen.getCells(1), 	Materials.Ethylene.getCells(1), 	Materials.AceticAcid.getFluid(1000), 	Materials.VinylAcetate.getFluid(1000),Materials.Water.getCells(1), Materials.Empty.getCells(1), 180);

    	GT_Values.RA.addDefaultPolymerizationRecipes(Materials.VinylAcetate.mFluid, Materials.VinylAcetate.getCells(1), Materials.PolyvinylAcetate.mFluid);
    	
        GT_Values.RA.addChemicalRecipe(Materials.Ethanol.getCells(1), GT_Utility.getIntegratedCircuit(1), Materials.SulfuricAcid.getFluid(1000), Materials.DilutedSulfuricAcid.getFluid(1000), Materials.Ethylene.getCells(1), 1200, 120);
        GT_Values.RA.addChemicalRecipe(Materials.SulfuricAcid.getCells(1), GT_Utility.getIntegratedCircuit(1), Materials.Ethanol.getFluid(1000), Materials.DilutedSulfuricAcid.getFluid(1000), Materials.Ethylene.getCells(1), 1200, 120);
        GT_Values.RA.addChemicalRecipe(Materials.Ethanol.getCells(1), GT_Utility.getIntegratedCircuit(11), Materials.SulfuricAcid.getFluid(1000), Materials.Ethylene.getGas(1000), Materials.DilutedSulfuricAcid.getCells(1), 1200, 120);
        GT_Values.RA.addChemicalRecipe(Materials.SulfuricAcid.getCells(1), GT_Utility.getIntegratedCircuit(11), Materials.Ethanol.getFluid(1000), Materials.Ethylene.getGas(1000), Materials.DilutedSulfuricAcid.getCells(1), 1200, 120);
    	
    	GT_Values.RA.addDefaultPolymerizationRecipes(Materials.Ethylene.mGas, Materials.Ethylene.getCells(1), Materials.Plastic.mStandardMoltenFluid);        
        
        GT_Values.RA.addChemicalRecipe(Materials.Sodium.getDust(1), GT_Utility.getIntegratedCircuit(1), Materials.Water.getFluid(1000), GT_Values.NF, Materials.SodiumHydroxide.getDust(1), 40, 8);

        GT_Values.RA.addChemicalRecipe(Materials.Chlorine.getCells(1), GT_Utility.getIntegratedCircuit(1),  Materials.Hydrogen.getGas(1000), Materials.HydrochloricAcid.getFluid(1000), Materials.Empty.getCells(1), 60, 8);
        GT_Values.RA.addChemicalRecipe(Materials.Hydrogen.getCells(1), GT_Utility.getIntegratedCircuit(1),  Materials.Chlorine.getGas(1000), Materials.HydrochloricAcid.getFluid(1000), Materials.Empty.getCells(1), 60, 8);
        GT_Values.RA.addChemicalRecipe(Materials.Chlorine.getCells(1), GT_Utility.getIntegratedCircuit(11), Materials.Hydrogen.getGas(1000), GT_Values.NF, Materials.HydrochloricAcid.getCells(1), 60, 8);
        GT_Values.RA.addChemicalRecipe(Materials.Hydrogen.getCells(1), GT_Utility.getIntegratedCircuit(11), Materials.Chlorine.getGas(1000), GT_Values.NF, Materials.HydrochloricAcid.getCells(1), 60, 8);

        GT_Values.RA.addChemicalRecipe(Materials.Chlorine.getCells(2), GT_Utility.getIntegratedCircuit(1),  Materials.Propene.getGas(1000),  Materials.AllylChloride.getFluid(1000),    Materials.HydrochloricAcid.getCells(1), Materials.Empty.getCells(1), 160);
        GT_Values.RA.addChemicalRecipe(Materials.Propene.getCells(1),  GT_Utility.getIntegratedCircuit(1),  Materials.Chlorine.getGas(2000), Materials.AllylChloride.getFluid(1000),    Materials.HydrochloricAcid.getCells(1), 160);
        GT_Values.RA.addChemicalRecipe(Materials.Chlorine.getCells(2), GT_Utility.getIntegratedCircuit(11), Materials.Propene.getGas(1000),  Materials.HydrochloricAcid.getFluid(1000), Materials.AllylChloride.getCells(1), Materials.Empty.getCells(1), 160);
        GT_Values.RA.addChemicalRecipe(Materials.Propene.getCells(1),  GT_Utility.getIntegratedCircuit(11), Materials.Chlorine.getGas(2000), Materials.HydrochloricAcid.getFluid(1000), Materials.AllylChloride.getCells(1), 160);

        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Chlorine.getCells(10),  Materials.Mercury.getCells(1), 	Materials.Water.getFluid(10000), 	Materials.HypochlorousAcid.getFluid(10000), Materials.Empty.getCells(11), GT_Values.NI, 600, 8);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Water.getCells(10), 	Materials.Mercury.getCells(1), 	Materials.Chlorine.getGas(10000), 	Materials.HypochlorousAcid.getFluid(10000), Materials.Empty.getCells(11), GT_Values.NI, 600, 8);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Chlorine.getCells(1),	Materials.Water.getCells(1), 	Materials.Mercury.getFluid(100), 	Materials.HypochlorousAcid.getFluid(1000),  Materials.Empty.getCells(2),  GT_Values.NI,  60, 8);
    	GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(2)}, new FluidStack[]{Materials.Chlorine.getGas(10000), Materials.Water.getFluid(10000), Materials.Mercury.getFluid(1000)}, new FluidStack[]{Materials.HypochlorousAcid.getFluid(10000)}, null, 600, 8);
        
        GT_Values.RA.addChemicalRecipe(Materials.Chlorine.getCells(2), GT_Utility.getIntegratedCircuit(1),  Materials.Water.getFluid(1000),  Materials.HypochlorousAcid.getFluid(1000),        Materials.DilutedHydrochloricAcid.getCells(1), Materials.Empty.getCells(1), 120);
        GT_Values.RA.addChemicalRecipe(Materials.Water.getCells(1),    GT_Utility.getIntegratedCircuit(1),  Materials.Chlorine.getGas(2000), Materials.HypochlorousAcid.getFluid(1000),        Materials.DilutedHydrochloricAcid.getCells(1), GT_Values.NI, 120);
        GT_Values.RA.addChemicalRecipe(Materials.Chlorine.getCells(2), GT_Utility.getIntegratedCircuit(11), Materials.Water.getFluid(1000),  Materials.DilutedHydrochloricAcid.getFluid(1000), Materials.HypochlorousAcid.getCells(1),        Materials.Empty.getCells(1), 120);
        GT_Values.RA.addChemicalRecipe(Materials.Water.getCells(1),    GT_Utility.getIntegratedCircuit(11), Materials.Chlorine.getGas(2000), Materials.DilutedHydrochloricAcid.getFluid(1000), Materials.HypochlorousAcid.getCells(1),        GT_Values.NI, 120);
        
        GT_Values.RA.addChemicalRecipe(                   Materials.HypochlorousAcid.getCells(1), 	Materials.SodiumHydroxide.getDust(1), 	Materials.AllylChloride.getFluid(1000), 	Materials.Epichlorohydrin.getFluid(1000), Materials.SaltWater.getCells(1), 480);
        GT_Values.RA.addChemicalRecipe(                   Materials.SodiumHydroxide.getDust(1), 	Materials.AllylChloride.getCells(1), 	Materials.HypochlorousAcid.getFluid(1000), 	Materials.Epichlorohydrin.getFluid(1000), Materials.SaltWater.getCells(1), 480);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.HydrochloricAcid.getCells(1), 	Materials.Glycerol.getCells(1), 		GT_Values.NF, 								Materials.Epichlorohydrin.getFluid(1000), Materials.Water.getCells(2), GT_Values.NI, 480, 30);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{Materials.SodiumHydroxide.getDust(1), GT_Utility.getIntegratedCircuit(23)}, new FluidStack[]{Materials.Propene.getGas(1000), Materials.Chlorine.getGas(4000), Materials.Water.getFluid(1000)},                                  new FluidStack[]{Materials.Epichlorohydrin.getFluid(1000), Materials.SaltWater.getFluid(1000), Materials.HydrochloricAcid.getFluid(2000)}, null, 640, 30);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{Materials.SodiumHydroxide.getDust(1), GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.Propene.getGas(1000), Materials.Chlorine.getGas(3000), Materials.Water.getFluid(1000), Materials.Mercury.getFluid(100)}, new FluidStack[]{Materials.Epichlorohydrin.getFluid(1000), Materials.SaltWater.getFluid(1000), Materials.HydrochloricAcid.getFluid(1000)}, null, 640, 30);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{Materials.SodiumHydroxide.getDust(1), GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.Propene.getGas(1000), Materials.Chlorine.getGas(2000), Materials.HypochlorousAcid.getFluid(1000)},                       new FluidStack[]{Materials.Epichlorohydrin.getFluid(1000), Materials.SaltWater.getFluid(1000), Materials.HydrochloricAcid.getFluid(1000)}, null, 640, 30);

        GT_Values.RA.addChemicalRecipe(                   Materials.HydrochloricAcid.getCells(1), 	Materials.Empty.getCells(1), 			Materials.Glycerol.getFluid(1000), 			Materials.Epichlorohydrin.getFluid(1000), Materials.Water.getCells(2), 480);
        GT_Values.RA.addChemicalRecipe(                   Materials.Glycerol.getCells(1), 			Materials.Empty.getCells(1), 			Materials.HydrochloricAcid.getFluid(1000), 	Materials.Epichlorohydrin.getFluid(1000), Materials.Water.getCells(2), 480);
        GT_Values.RA.addChemicalRecipe(                   Materials.HydrochloricAcid.getCells(1), 	GT_Utility.getIntegratedCircuit(11), 	Materials.Glycerol.getFluid(1000), 			Materials.Water.getFluid(2000), 		  Materials.Epichlorohydrin.getCells(1), 480);
        GT_Values.RA.addChemicalRecipe(                   Materials.Glycerol.getCells(1), 			GT_Utility.getIntegratedCircuit(11), 	Materials.HydrochloricAcid.getFluid(1000), 	Materials.Water.getFluid(2000), 		  Materials.Epichlorohydrin.getCells(1), 480);
        GT_Values.RA.addChemicalRecipe(                   Materials.HydrochloricAcid.getCells(1), 	GT_Utility.getIntegratedCircuit(2), 	Materials.Glycerol.getFluid(1000), 			Materials.Epichlorohydrin.getFluid(1000), Materials.Empty.getCells(1), 480);
        GT_Values.RA.addChemicalRecipe(                   Materials.Glycerol.getCells(1), 			GT_Utility.getIntegratedCircuit(2), 	Materials.HydrochloricAcid.getFluid(1000), 	Materials.Epichlorohydrin.getFluid(1000), Materials.Empty.getCells(1), 480);
        GT_Values.RA.addChemicalRecipe(                   Materials.HydrochloricAcid.getCells(1), 	GT_Utility.getIntegratedCircuit(12), 	Materials.Glycerol.getFluid(1000), 			GT_Values.NF, 		  					  Materials.Epichlorohydrin.getCells(1), 480);
        GT_Values.RA.addChemicalRecipe(                   Materials.Glycerol.getCells(1), 			GT_Utility.getIntegratedCircuit(12), 	Materials.HydrochloricAcid.getFluid(1000), 	GT_Values.NF, 		  					  Materials.Epichlorohydrin.getCells(1), 480);

        GT_Values.RA.addDistilleryRecipe(2, Materials.HeavyFuel.getFluid(100), Materials.Benzene.getFluid(40), 160, 24, false);
        GT_Values.RA.addDistilleryRecipe(3, Materials.HeavyFuel.getFluid(100), Materials.Phenol.getFluid(25), 160, 24, false);

        GT_Values.RA.addChemicalRecipe(Materials.Apatite.getDust(1), Materials.SulfuricAcid.getCells(5), Materials.Water.getFluid(10000), Materials.PhosphoricAcid.getFluid(3000), Materials.HydrochloricAcid.getCells(1), Materials.Empty.getCells(4), 320);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Phosphor.getDust(4), GT_Values.NI, Materials.Oxygen.getGas(10000), GT_Values.NF, Materials.PhosphorousPentoxide.getDust(1), GT_Values.NI, 40, 30);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{Materials.Phosphor.getDust(4), GT_Utility.getIntegratedCircuit(1)}, new FluidStack[]{Materials.Oxygen.getGas(10000)}, null, new ItemStack[]{Materials.PhosphorousPentoxide.getDust(1)}, 40, 30);
        GT_Values.RA.addChemicalRecipe(Materials.PhosphorousPentoxide.getDust(1), GT_Values.NI, Materials.Water.getFluid(6000), Materials.PhosphoricAcid.getFluid(4000), GT_Values.NI, 40);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{Materials.Phosphor.getDust(1), GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.Oxygen.getGas(2500), Materials.Water.getFluid(1500)}, new FluidStack[]{Materials.PhosphoricAcid.getFluid(1000)}, null, 320, 30);

        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Propene.getCells(8),        Materials.PhosphoricAcid.getCells(1), Materials.Benzene.getFluid(8000),       Materials.Cumene.getFluid(8000), Materials.Empty.getCells(9), GT_Values.NI, 1920, 30);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.PhosphoricAcid.getCells(1), Materials.Benzene.getCells(8),        Materials.Propene.getGas(8000),         Materials.Cumene.getFluid(8000), Materials.Empty.getCells(9), GT_Values.NI, 1920, 30);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Benzene.getCells(1),        Materials.Propene.getCells(1),        Materials.PhosphoricAcid.getFluid(125), Materials.Cumene.getFluid(1000), Materials.Empty.getCells(2), GT_Values.NI, 240 , 30);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(1)}, new FluidStack[]{Materials.Propene.getGas(8000), Materials.Benzene.getFluid(8000), Materials.PhosphoricAcid.getFluid(1000)}, new FluidStack[]{Materials.Cumene.getFluid(8000)}, null, 1920, 30);

        GT_Values.RA.addChemicalRecipe(Materials.Cumene.getCells(1), GT_Utility.getIntegratedCircuit(1), 	Materials.Oxygen.getGas(1000), 	Materials.Acetone.getFluid(1000), 	Materials.Phenol.getCells(1), 	160);
        GT_Values.RA.addChemicalRecipe(Materials.Oxygen.getCells(1), GT_Utility.getIntegratedCircuit(1), 	Materials.Cumene.getFluid(1000),Materials.Acetone.getFluid(1000), 	Materials.Phenol.getCells(1), 	160);
        GT_Values.RA.addChemicalRecipe(Materials.Cumene.getCells(1), GT_Utility.getIntegratedCircuit(11), 	Materials.Oxygen.getGas(1000), 	Materials.Phenol.getFluid(1000), 	Materials.Acetone.getCells(1), 	160);
        GT_Values.RA.addChemicalRecipe(Materials.Oxygen.getCells(1), GT_Utility.getIntegratedCircuit(11), 	Materials.Cumene.getFluid(1000),Materials.Phenol.getFluid(1000), 	Materials.Acetone.getCells(1), 	160);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.Propene.getGas(1000), Materials.Benzene.getFluid(1000), Materials.PhosphoricAcid.getFluid(100), Materials.Oxygen.getGas(1000)}, new FluidStack[]{Materials.Phenol.getFluid(1000), Materials.Acetone.getFluid(1000)}, null, 480, 30);

        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Acetone.getCells(1), 			Materials.Phenol.getCells(2), 			Materials.HydrochloricAcid.getFluid(1000),  Materials.BisphenolA.getFluid(1000), Materials.Water.getCells(1), Materials.Empty.getCells(2), 160, 30);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.HydrochloricAcid.getCells(1), 	Materials.Acetone.getCells(1), 			Materials.Phenol.getFluid(2000), 			Materials.BisphenolA.getFluid(1000), Materials.Water.getCells(1), Materials.Empty.getCells(1), 160, 30);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Phenol.getCells(2), 			Materials.HydrochloricAcid.getCells(1), Materials.Acetone.getFluid(1000), 			Materials.BisphenolA.getFluid(1000), Materials.Water.getCells(1), Materials.Empty.getCells(2), 160, 30);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(1)}, new FluidStack[]{Materials.Acetone.getFluid(1000), Materials.Phenol.getFluid(2000), Materials.HydrochloricAcid.getFluid(1000)}, new FluidStack[]{Materials.BisphenolA.getFluid(1000)}, null, 160, 30);

        GT_Values.RA.addChemicalRecipe(Materials.BisphenolA.getCells(1), 		Materials.SodiumHydroxide.getDust(1), 	Materials.Epichlorohydrin.getFluid(1000), 	Materials.Epoxid.getMolten(1000), Materials.SaltWater.getCells(1), 200);
        GT_Values.RA.addChemicalRecipe(Materials.SodiumHydroxide.getDust(1), 	Materials.Epichlorohydrin.getCells(1), 	Materials.BisphenolA.getFluid(1000), 		Materials.Epoxid.getMolten(1000), Materials.SaltWater.getCells(1), 200);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{Materials.SodiumHydroxide.getDust(1), GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.Acetone.getFluid(1000), Materials.Phenol.getFluid(2000), Materials.HydrochloricAcid.getFluid(1000), Materials.Epichlorohydrin.getFluid(1000)}, new FluidStack[]{Materials.Epoxid.getMolten(1000), Materials.SaltWater.getFluid(1000)}, null, 480, 30);

        GT_Values.RA.addChemicalRecipe(Materials.Methanol.getCells(1),         GT_Utility.getIntegratedCircuit(1),  Materials.HydrochloricAcid.getFluid(1000), Materials.Chloromethane.getGas(1000), Materials.Water.getCells(1), 160);
        GT_Values.RA.addChemicalRecipe(Materials.HydrochloricAcid.getCells(1), GT_Utility.getIntegratedCircuit(1),  Materials.Methanol.getFluid(1000),         Materials.Chloromethane.getGas(1000), Materials.Water.getCells(1), 160);
        GT_Values.RA.addChemicalRecipe(Materials.Methanol.getCells(1),         GT_Utility.getIntegratedCircuit(11), Materials.HydrochloricAcid.getFluid(1000), Materials.Water.getFluid(1000), Materials.Chloromethane.getCells(1), 160);
        GT_Values.RA.addChemicalRecipe(Materials.HydrochloricAcid.getCells(1), GT_Utility.getIntegratedCircuit(11), Materials.Methanol.getFluid(1000),         Materials.Water.getFluid(1000), Materials.Chloromethane.getCells(1), 160);
        GT_Values.RA.addChemicalRecipe(Materials.Methanol.getCells(1),         GT_Utility.getIntegratedCircuit(2),  Materials.HydrochloricAcid.getFluid(1000), Materials.Chloromethane.getGas(1000), Materials.Empty.getCells(1), 160);
        GT_Values.RA.addChemicalRecipe(Materials.HydrochloricAcid.getCells(1), GT_Utility.getIntegratedCircuit(2),  Materials.Methanol.getFluid(1000),         Materials.Chloromethane.getGas(1000), Materials.Empty.getCells(1), 160);
        GT_Values.RA.addChemicalRecipe(Materials.Methanol.getCells(1),         GT_Utility.getIntegratedCircuit(12), Materials.HydrochloricAcid.getFluid(1000), GT_Values.NF,                   Materials.Chloromethane.getCells(1), 160);
        GT_Values.RA.addChemicalRecipe(Materials.HydrochloricAcid.getCells(1), GT_Utility.getIntegratedCircuit(12), Materials.Methanol.getFluid(1000),         GT_Values.NF,                   Materials.Chloromethane.getCells(1), 160);

        GT_Values.RA.addChemicalRecipe(Materials.Chlorine.getCells(2), GT_Utility.getIntegratedCircuit(1),  Materials.Methane.getGas(1000),  Materials.Chloromethane.getGas(1000), 		Materials.HydrochloricAcid.getCells(1), Materials.Empty.getCells(1), 80);
        GT_Values.RA.addChemicalRecipe(Materials.Methane.getCells(1),  GT_Utility.getIntegratedCircuit(1),  Materials.Chlorine.getGas(2000), Materials.Chloromethane.getGas(1000), 		Materials.HydrochloricAcid.getCells(1), 80);
        GT_Values.RA.addChemicalRecipe(Materials.Chlorine.getCells(2), GT_Utility.getIntegratedCircuit(11), Materials.Methane.getGas(1000),  Materials.HydrochloricAcid.getFluid(1000), Materials.Chloromethane.getCells(1), Materials.Empty.getCells(1), 80);
        GT_Values.RA.addChemicalRecipe(Materials.Methane.getCells(1),  GT_Utility.getIntegratedCircuit(11), Materials.Chlorine.getGas(2000), Materials.HydrochloricAcid.getFluid(1000), Materials.Chloromethane.getCells(1), 80);

        GT_Values.RA.addChemicalRecipe(                   Materials.Chlorine.getCells(6), GT_Utility.getIntegratedCircuit(3),  Materials.Methane.getGas(1000),  Materials.Chloroform.getFluid(1000),       Materials.HydrochloricAcid.getCells(3), Materials.Empty.getCells(3), 80);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Methane.getCells(1),  Materials.Empty.getCells(2),         Materials.Chlorine.getGas(6000), Materials.Chloroform.getFluid(1000),       Materials.HydrochloricAcid.getCells(3), GT_Values.NI,  80, 30);
        GT_Values.RA.addChemicalRecipe(                   Materials.Chlorine.getCells(6), GT_Utility.getIntegratedCircuit(13), Materials.Methane.getGas(1000),  Materials.HydrochloricAcid.getFluid(3000), Materials.Chloroform.getCells(1),       Materials.Empty.getCells(5), 80);
        GT_Values.RA.addChemicalRecipe(                   Materials.Methane.getCells(1),  GT_Utility.getIntegratedCircuit(13), Materials.Chlorine.getGas(6000), Materials.HydrochloricAcid.getFluid(3000), Materials.Chloroform.getCells(1),       80);

        GT_Values.RA.addChemicalRecipe(Materials.Fluorine.getCells(1), 	GT_Utility.getIntegratedCircuit(1),  Materials.Hydrogen.getGas(1000), Materials.HydrofluoricAcid.getFluid(1000), Materials.Empty.getCells(1), 60, 8);
        GT_Values.RA.addChemicalRecipe(Materials.Hydrogen.getCells(1), 	GT_Utility.getIntegratedCircuit(1),  Materials.Fluorine.getGas(1000), Materials.HydrofluoricAcid.getFluid(1000), Materials.Empty.getCells(1), 60, 8);
        GT_Values.RA.addChemicalRecipe(Materials.Fluorine.getCells(1), 	GT_Utility.getIntegratedCircuit(11), Materials.Hydrogen.getGas(1000), GT_Values.NF, Materials.HydrofluoricAcid.getCells(1), 60, 8);
        GT_Values.RA.addChemicalRecipe(Materials.Hydrogen.getCells(1), 	GT_Utility.getIntegratedCircuit(11), Materials.Fluorine.getGas(1000), GT_Values.NF, Materials.HydrofluoricAcid.getCells(1), 60, 8);

        GT_Values.RA.addChemicalRecipe(Materials.Chloroform.getCells(2), 		Materials.HydrofluoricAcid.getCells(4), GT_Values.NF,                              Materials.Tetrafluoroethylene.getGas(1000),       Materials.DilutedHydrochloricAcid.getCells(6), 480, 240);
        GT_Values.RA.addChemicalRecipe(Materials.Chloroform.getCells(2), 		Materials.Empty.getCells(4),            Materials.HydrofluoricAcid.getFluid(4000), Materials.Tetrafluoroethylene.getGas(1000),       Materials.DilutedHydrochloricAcid.getCells(6), 480, 240);
        GT_Values.RA.addChemicalRecipe(Materials.HydrofluoricAcid.getCells(4),  Materials.Empty.getCells(2),            Materials.Chloroform.getFluid(2000),       Materials.Tetrafluoroethylene.getGas(1000),       Materials.DilutedHydrochloricAcid.getCells(6), 480, 240);
        GT_Values.RA.addChemicalRecipe(Materials.HydrofluoricAcid.getCells(4),  GT_Utility.getIntegratedCircuit(11),    Materials.Chloroform.getFluid(2000),       Materials.DilutedHydrochloricAcid.getFluid(6000), Materials.Tetrafluoroethylene.getCells(1),     Materials.Empty.getCells(3), 480, 240);
        GT_Values.RA.addChemicalRecipe(Materials.Chloroform.getCells(2),        GT_Utility.getIntegratedCircuit(11),    Materials.HydrofluoricAcid.getFluid(4000), Materials.DilutedHydrochloricAcid.getFluid(6000), Materials.Tetrafluoroethylene.getCells(1),     Materials.Empty.getCells(1), 480, 240);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.HydrofluoricAcid.getFluid(4000), Materials.Methane.getGas(2000), Materials.Chlorine.getGas(12000)}, new FluidStack[]{Materials.Tetrafluoroethylene.getGas(1000), Materials.HydrochloricAcid.getFluid(6000), Materials.DilutedHydrochloricAcid.getFluid(6000)}, null, 540, 240);

        GT_Values.RA.addDefaultPolymerizationRecipes(Materials.Tetrafluoroethylene.mGas, Materials.Tetrafluoroethylene.getCells(1), Materials.Polytetrafluoroethylene.mStandardMoltenFluid);
        
        GT_Values.RA.addChemicalRecipe(                   Materials.Silicon.getDust(1), GT_Utility.getIntegratedCircuit(1), Materials.Chloromethane.getGas(2000), Materials.Dimethyldichlorosilane.getFluid(1000), GT_Values.NI, 240, 96);
        //This recipe is redundant:
        //GT_Values.RA.addChemicalRecipe(                   Materials.Silicon.getDust(1), GT_Utility.getIntegratedCircuit(11), Materials.Chloromethane.getGas(2000), GT_Values.NF, Materials.Dimethyldichlorosilane.getCells(1), 240, 96);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Silicon.getDust(1), Materials.Chloromethane.getCells(2), GT_Values.NF, Materials.Dimethyldichlorosilane.getFluid(1000), Materials.Empty.getCells(2), GT_Values.NI, 240, 96);

        GT_Values.RA.addChemicalRecipe(Materials.Dimethyldichlorosilane.getCells(1), GT_Utility.getIntegratedCircuit(1),  Materials.Water.getFluid(1000),                   Materials.DilutedHydrochloricAcid.getFluid(1000), Materials.Polydimethylsiloxane.getDust(3), Materials.Empty.getCells(1), 240, 96);
        GT_Values.RA.addChemicalRecipe(Materials.Water.getCells(1),                  GT_Utility.getIntegratedCircuit(1),  Materials.Dimethyldichlorosilane.getFluid(1000),  Materials.DilutedHydrochloricAcid.getFluid(1000), Materials.Polydimethylsiloxane.getDust(3), Materials.Empty.getCells(1), 240, 96);
        GT_Values.RA.addChemicalRecipe(Materials.Dimethyldichlorosilane.getCells(1), GT_Utility.getIntegratedCircuit(11), Materials.Water.getFluid(1000),                   GT_Values.NF,                                     Materials.Polydimethylsiloxane.getDust(3), Materials.DilutedHydrochloricAcid.getCells(1), 240, 96);
        GT_Values.RA.addChemicalRecipe(Materials.Water.getCells(1),                  GT_Utility.getIntegratedCircuit(11), Materials.Dimethyldichlorosilane.getFluid(1000),  GT_Values.NF,                                     Materials.Polydimethylsiloxane.getDust(3), Materials.DilutedHydrochloricAcid.getCells(1), 240, 96);
        GT_Values.RA.addChemicalRecipe(Materials.Dimethyldichlorosilane.getCells(1), Materials.Water.getCells(1),         GT_Values.NF,                                     Materials.DilutedHydrochloricAcid.getFluid(1000), Materials.Polydimethylsiloxane.getDust(3), Materials.Empty.getCells(2), 240, 96);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{Materials.Silicon.getDust(1), GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.Methane.getGas(2000), Materials.Chlorine.getGas(4000), Materials.Water.getFluid(1000)}, new FluidStack[]{Materials.HydrochloricAcid.getFluid(2000), Materials.DilutedHydrochloricAcid.getFluid(2000)}, new ItemStack[]{Materials.Polydimethylsiloxane.getDust(3)}, 480, 96);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{Materials.Silicon.getDust(1), GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.Methanol.getFluid(2000), Materials.HydrochloricAcid.getFluid(2000)},                    new FluidStack[]{Materials.DilutedHydrochloricAcid.getFluid(2000)}, new ItemStack[]{Materials.Polydimethylsiloxane.getDust(3)}, 480, 96);

        GT_Values.RA.addChemicalRecipe(Materials.Polydimethylsiloxane.getDust(9), Materials.Sulfur.getDust(1), GT_Values.NF, Materials.Silicone.getMolten(1296), GT_Values.NI, 600);

        GT_Values.RA.addChemicalRecipe(Materials.Nitrogen.getCells(1), GT_Utility.getIntegratedCircuit(1),  Materials.Hydrogen.getGas(3000), Materials.Ammonia.getGas(1000), Materials.Empty.getCells(1), 320, 384);
        GT_Values.RA.addChemicalRecipe(Materials.Hydrogen.getCells(3), GT_Utility.getIntegratedCircuit(1),  Materials.Nitrogen.getGas(1000), Materials.Ammonia.getGas(1000), Materials.Empty.getCells(3), 320, 384);
        GT_Values.RA.addChemicalRecipe(Materials.Nitrogen.getCells(1), GT_Utility.getIntegratedCircuit(11), Materials.Hydrogen.getGas(3000), GT_Values.NF, Materials.Ammonia.getCells(1), 320, 384);
        GT_Values.RA.addChemicalRecipe(Materials.Hydrogen.getCells(3), GT_Utility.getIntegratedCircuit(11), Materials.Nitrogen.getGas(1000), GT_Values.NF, Materials.Ammonia.getCells(1), Materials.Empty.getCells(2), 320, 384);

        GT_Values.RA.addChemicalRecipe(                   Materials.Methanol.getCells(2), GT_Utility.getIntegratedCircuit(1),  Materials.Ammonia.getGas(1000),    Materials.Dimethylamine.getGas(1000), Materials.Water.getCells(2), 240, 120);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Ammonia.getCells(1),  Materials.Empty.getCells(1),         Materials.Methanol.getFluid(2000), Materials.Dimethylamine.getGas(1000), Materials.Water.getCells(2), GT_Values.NI, 240, 120);
        GT_Values.RA.addChemicalRecipe(                   Materials.Methanol.getCells(2), GT_Utility.getIntegratedCircuit(11), Materials.Ammonia.getGas(1000),    Materials.Water.getFluid(1000),       Materials.Dimethylamine.getCells(1), Materials.Empty.getCells(1), 240, 120);
        GT_Values.RA.addChemicalRecipe(                   Materials.Ammonia.getCells(1),  GT_Utility.getIntegratedCircuit(11), Materials.Methanol.getFluid(2000), Materials.Water.getFluid(1000),       Materials.Dimethylamine.getCells(1), 240, 120);
        GT_Values.RA.addChemicalRecipe(                   Materials.Methanol.getCells(2), GT_Utility.getIntegratedCircuit(2),  Materials.Ammonia.getGas(1000),    Materials.Dimethylamine.getGas(1000), Materials.Empty.getCells(2), 240, 120);
        GT_Values.RA.addChemicalRecipe(                   Materials.Methanol.getCells(2), GT_Utility.getIntegratedCircuit(12), Materials.Ammonia.getGas(1000),    GT_Values.NF,                         Materials.Dimethylamine.getCells(1), Materials.Empty.getCells(1), 240, 120);
        GT_Values.RA.addChemicalRecipe(                   Materials.Ammonia.getCells(1),  GT_Utility.getIntegratedCircuit(12), Materials.Methanol.getFluid(2000), GT_Values.NF,                         Materials.Dimethylamine.getCells(1), 240, 120);

        GT_Values.RA.addChemicalRecipe(Materials.Ammonia.getCells(1),          GT_Utility.getIntegratedCircuit(1),  Materials.HypochlorousAcid.getFluid(1000), Materials.Chloramine.getFluid(1000), Materials.Water.getCells(1), 160);
        GT_Values.RA.addChemicalRecipe(Materials.HypochlorousAcid.getCells(1), GT_Utility.getIntegratedCircuit(1),  Materials.Ammonia.getGas(1000),            Materials.Chloramine.getFluid(1000), Materials.Water.getCells(1), 160);
        GT_Values.RA.addChemicalRecipe(Materials.Ammonia.getCells(1),          GT_Utility.getIntegratedCircuit(11), Materials.HypochlorousAcid.getFluid(1000), Materials.Water.getFluid(1000), Materials.Chloramine.getCells(1), 160);
        GT_Values.RA.addChemicalRecipe(Materials.HypochlorousAcid.getCells(1), GT_Utility.getIntegratedCircuit(11), Materials.Ammonia.getGas(1000),            Materials.Water.getFluid(1000), Materials.Chloramine.getCells(1), 160);
        GT_Values.RA.addChemicalRecipe(Materials.Ammonia.getCells(1),          GT_Utility.getIntegratedCircuit(2),  Materials.HypochlorousAcid.getFluid(1000), Materials.Chloramine.getFluid(1000), Materials.Empty.getCells(1), 160);
        GT_Values.RA.addChemicalRecipe(Materials.HypochlorousAcid.getCells(1), GT_Utility.getIntegratedCircuit(2),  Materials.Ammonia.getGas(1000),            Materials.Chloramine.getFluid(1000), Materials.Empty.getCells(1), 160);
        GT_Values.RA.addChemicalRecipe(Materials.Ammonia.getCells(1),          GT_Utility.getIntegratedCircuit(12), Materials.HypochlorousAcid.getFluid(1000), GT_Values.NF,                   Materials.Chloramine.getCells(1), 160);
        GT_Values.RA.addChemicalRecipe(Materials.HypochlorousAcid.getCells(1), GT_Utility.getIntegratedCircuit(12), Materials.Ammonia.getGas(1000),            GT_Values.NF,                   Materials.Chloramine.getCells(1), 160);

        GT_Values.RA.addChemicalRecipe(Materials.Chloramine.getCells(1),    GT_Utility.getIntegratedCircuit(1),  Materials.Dimethylamine.getGas(1000), Materials.DilutedHydrochloricAcid.getFluid(1000),  Materials.Dimethylhydrazine.getCells(1), 960, 480);        
        GT_Values.RA.addChemicalRecipe(Materials.Dimethylamine.getCells(1), GT_Utility.getIntegratedCircuit(1),  Materials.Chloramine.getFluid(1000),  Materials.DilutedHydrochloricAcid.getFluid(1000),  Materials.Dimethylhydrazine.getCells(1), 960, 480);        
        GT_Values.RA.addChemicalRecipe(Materials.Chloramine.getCells(1),    GT_Utility.getIntegratedCircuit(11), Materials.Dimethylamine.getGas(1000), Materials.Dimethylhydrazine.getFluid(1000), Materials.DilutedHydrochloricAcid.getCells(1), 960, 480);        
        GT_Values.RA.addChemicalRecipe(Materials.Dimethylamine.getCells(1), GT_Utility.getIntegratedCircuit(11), Materials.Chloramine.getFluid(1000),  Materials.Dimethylhydrazine.getFluid(1000), Materials.DilutedHydrochloricAcid.getCells(1), 960, 480);        
    	GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.HypochlorousAcid.getFluid(1000), Materials.Ammonia.getGas(1000), Materials.Methanol.getFluid(2000)}, new FluidStack[]{Materials.Dimethylhydrazine.getFluid(1000), Materials.DilutedHydrochloricAcid.getFluid(1000), Materials.Water.getFluid(3000)}, null, 1040, 480);
        
        GT_Values.RA.addChemicalRecipe(GT_Utility.getIntegratedCircuit(2), GT_Values.NI, Materials.NitrogenDioxide.getGas(2000), Materials.DinitrogenTetroxide.getGas(1000), GT_Values.NI, 640);        
        GT_Values.RA.addChemicalRecipe(Materials.NitrogenDioxide.getCells(2), GT_Utility.getIntegratedCircuit(2), GT_Values.NF, Materials.DinitrogenTetroxide.getGas(1000), Materials.Empty.getCells(2), 640);        
        GT_Values.RA.addChemicalRecipe(Materials.NitrogenDioxide.getCells(2), GT_Utility.getIntegratedCircuit(12), GT_Values.NF, GT_Values.NF, Materials.DinitrogenTetroxide.getCells(1), Materials.Empty.getCells(1), 640);        
    	GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(23)}, new FluidStack[]{Materials.Ammonia.getGas(2000), Materials.Oxygen.getGas(7000)},                                   new FluidStack[]{Materials.DinitrogenTetroxide.getGas(1000), Materials.Water.getFluid(3000)}, null, 480, 30);
    	GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(23)}, new FluidStack[]{Materials.Nitrogen.getGas(2000), Materials.Hydrogen.getGas(6000), Materials.Oxygen.getGas(7000)}, new FluidStack[]{Materials.DinitrogenTetroxide.getGas(1000), Materials.Water.getFluid(3000)}, null, 1100, 480);

        GT_Values.RA.addMixerRecipe(Materials.Dimethylhydrazine.getCells(1), GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.DinitrogenTetroxide.getGas(1000), new FluidStack(ItemList.sRocketFuel, 6000), Materials.Empty.getCells(1), 60, 16);
        GT_Values.RA.addMixerRecipe(Materials.DinitrogenTetroxide.getCells(1), GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.Dimethylhydrazine.getFluid(1000), new FluidStack(ItemList.sRocketFuel, 6000), Materials.Empty.getCells(1), 60, 16);
        GT_Values.RA.addMixerRecipe(Materials.Dimethylhydrazine.getCells(1), GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.Oxygen.getGas(1000), new FluidStack(ItemList.sRocketFuel, 3000), Materials.Empty.getCells(1), 60, 16);
        GT_Values.RA.addMixerRecipe(Materials.Oxygen.getCells(1), GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.Dimethylhydrazine.getFluid(1000), new FluidStack(ItemList.sRocketFuel, 3000), Materials.Empty.getCells(1), 60, 16);

        GT_Values.RA.addChemicalRecipe(                   Materials.Ammonia.getCells(4), GT_Utility.getIntegratedCircuit(1),  Materials.Oxygen.getGas(10000), Materials.Water.getFluid(6000), Materials.NitricOxide.getCells(4), 320);
        GT_Values.RA.addChemicalRecipe(                   Materials.Oxygen.getCells(10), GT_Utility.getIntegratedCircuit(1),  Materials.Ammonia.getGas(4000), Materials.Water.getFluid(6000), Materials.NitricOxide.getCells(4), Materials.Empty.getCells(6), 320);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Ammonia.getCells(4), Materials.Empty.getCells(2),         Materials.Oxygen.getGas(10000), Materials.NitricOxide.getGas(4000), Materials.Water.getCells(6), GT_Values.NI, 320, 30);
        GT_Values.RA.addChemicalRecipe(                   Materials.Oxygen.getCells(10), GT_Utility.getIntegratedCircuit(11), Materials.Ammonia.getGas(4000), Materials.NitricOxide.getGas(4000), Materials.Water.getCells(6), Materials.Empty.getCells(4), 320);
        GT_Values.RA.addChemicalRecipe(                   Materials.Ammonia.getCells(4), GT_Utility.getIntegratedCircuit(2),  Materials.Oxygen.getGas(10000), GT_Values.NF, Materials.NitricOxide.getCells(4), 320);
        GT_Values.RA.addChemicalRecipe(                   Materials.Oxygen.getCells(10), GT_Utility.getIntegratedCircuit(2),  Materials.Ammonia.getGas(4000), GT_Values.NF, Materials.NitricOxide.getCells(4), Materials.Empty.getCells(6), 320);
        GT_Values.RA.addChemicalRecipe(                   Materials.Oxygen.getCells(10), GT_Utility.getIntegratedCircuit(12), Materials.Ammonia.getGas(4000), Materials.NitricOxide.getGas(4000), Materials.Empty.getCells(10), 320);


        GT_Values.RA.addChemicalRecipe(Materials.NitricOxide.getCells(1), GT_Utility.getIntegratedCircuit(1),  Materials.Oxygen.getGas(1000),      Materials.NitrogenDioxide.getGas(1000), Materials.Empty.getCells(1), 160);
        GT_Values.RA.addChemicalRecipe(Materials.Oxygen.getCells(1),      GT_Utility.getIntegratedCircuit(1),  Materials.NitricOxide.getGas(1000), Materials.NitrogenDioxide.getGas(1000), Materials.Empty.getCells(1), 160);
        GT_Values.RA.addChemicalRecipe(Materials.NitricOxide.getCells(1), GT_Utility.getIntegratedCircuit(11), Materials.Oxygen.getGas(1000),      GT_Values.NF,                           Materials.NitrogenDioxide.getCells(1), 160);
        GT_Values.RA.addChemicalRecipe(Materials.Oxygen.getCells(1),      GT_Utility.getIntegratedCircuit(11), Materials.NitricOxide.getGas(1000), GT_Values.NF,                           Materials.NitrogenDioxide.getCells(1), 160);

        GT_Values.RA.addChemicalRecipe(                   Materials.Water.getCells(1),           GT_Utility.getIntegratedCircuit(1),  Materials.NitrogenDioxide.getGas(3000), Materials.NitricAcid.getFluid(2000), Materials.NitricOxide.getCells(1), 240);
        GT_Values.RA.addChemicalRecipe(                   Materials.NitrogenDioxide.getCells(3), GT_Utility.getIntegratedCircuit(1),  Materials.Water.getFluid(1000),         Materials.NitricAcid.getFluid(2000), Materials.NitricOxide.getCells(1), Materials.Empty.getCells(2), 240);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Water.getCells(1),           Materials.Empty.getCells(1),         Materials.NitrogenDioxide.getGas(3000), Materials.NitricOxide.getGas(1000),  Materials.NitricAcid.getCells(2), GT_Values.NI, 240, 30);
        GT_Values.RA.addChemicalRecipe(                   Materials.NitrogenDioxide.getCells(3), GT_Utility.getIntegratedCircuit(11), Materials.Water.getFluid(1000),         Materials.NitricOxide.getGas(1000),  Materials.NitricAcid.getCells(2), Materials.Empty.getCells(1), 240);

        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.NitrogenDioxide.getCells(2), 	Materials.Oxygen.getCells(1), 			Materials.Water.getFluid(1000), 		Materials.NitricAcid.getFluid(2000), Materials.Empty.getCells(3), GT_Values.NI,240, 30);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Oxygen.getCells(1), 			Materials.Water.getCells(1), 			Materials.NitrogenDioxide.getGas(2000), Materials.NitricAcid.getFluid(2000), Materials.Empty.getCells(2), GT_Values.NI,240, 30);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Water.getCells(1), 			Materials.NitrogenDioxide.getCells(2), 	Materials.Oxygen.getGas(1000), 			Materials.NitricAcid.getFluid(2000), Materials.Empty.getCells(3), GT_Values.NI,240, 30);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.Hydrogen.getGas(3000), Materials.Nitrogen.getGas(1000), Materials.Oxygen.getGas(4000)}, new FluidStack[]{Materials.NitricAcid.getFluid(1000), Materials.Water.getFluid(1000)}, null, 320, 480);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.Ammonia.getGas(1000), Materials.Oxygen.getGas(4000)}, new FluidStack[]{Materials.NitricAcid.getFluid(1000), Materials.Water.getFluid(1000)}, null, 320, 30);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.NitrogenDioxide.getGas(2000), Materials.Oxygen.getGas(1000), Materials.Water.getFluid(1000)}, new FluidStack[]{Materials.NitricAcid.getFluid(2000)}, null, 320, 30);

        GT_Values.RA.addChemicalRecipe(Materials.Sulfur.getDust(1), GT_Utility.getIntegratedCircuit(1), Materials.Hydrogen.getGas(2000), Materials.HydricSulfide.getGas(1000), GT_Values.NI, 60, 8);
        GT_Values.RA.addChemicalRecipe(Materials.Sulfur.getDust(1), Materials.Empty.getCells(1),        Materials.Hydrogen.getGas(2000), GT_Values.NF, Materials.HydricSulfide.getCells(1), 60, 8);

        GT_Values.RA.addChemicalRecipe(Materials.Sulfur.getDust(1),         GT_Utility.getIntegratedCircuit(2), Materials.Oxygen.getGas(2000), Materials.SulfurDioxide.getGas(1000),  GT_Values.NI, 60, 8);

        GT_Values.RA.addChemicalRecipe(Materials.HydricSulfide.getCells(1), GT_Utility.getIntegratedCircuit(1),  Materials.Oxygen.getGas(3000),        Materials.SulfurDioxide.getGas(1000), Materials.Water.getCells(1), 120);
        GT_Values.RA.addChemicalRecipe(Materials.Oxygen.getCells(3),        GT_Utility.getIntegratedCircuit(1),  Materials.HydricSulfide.getGas(1000), Materials.SulfurDioxide.getGas(1000), Materials.Water.getCells(1), Materials.Empty.getCells(2), 120);
        GT_Values.RA.addChemicalRecipe(Materials.HydricSulfide.getCells(1), GT_Utility.getIntegratedCircuit(11), Materials.Oxygen.getGas(3000),        Materials.Water.getFluid(1000),       Materials.SulfurDioxide.getCells(1), 120);
        GT_Values.RA.addChemicalRecipe(Materials.Oxygen.getCells(3),        GT_Utility.getIntegratedCircuit(11), Materials.HydricSulfide.getGas(1000), Materials.Water.getFluid(1000),       Materials.SulfurDioxide.getCells(1), Materials.Empty.getCells(2), 120);
        GT_Values.RA.addChemicalRecipe(Materials.HydricSulfide.getCells(1), GT_Utility.getIntegratedCircuit(2),  Materials.Oxygen.getGas(3000),        Materials.SulfurDioxide.getGas(1000), Materials.Empty.getCells(1), 120);
        GT_Values.RA.addChemicalRecipe(Materials.Oxygen.getCells(3),        GT_Utility.getIntegratedCircuit(2),  Materials.HydricSulfide.getGas(1000), Materials.SulfurDioxide.getGas(1000), Materials.Empty.getCells(3), 120);
        GT_Values.RA.addChemicalRecipe(Materials.HydricSulfide.getCells(1), GT_Utility.getIntegratedCircuit(12), Materials.Oxygen.getGas(3000),        GT_Values.NF,                         Materials.SulfurDioxide.getCells(1), 120);
        GT_Values.RA.addChemicalRecipe(Materials.Oxygen.getCells(3),        GT_Utility.getIntegratedCircuit(12), Materials.HydricSulfide.getGas(1000), GT_Values.NF,                         Materials.SulfurDioxide.getCells(1), Materials.Empty.getCells(2), 120);

        GT_Values.RA.addChemicalRecipe(Materials.SulfurDioxide.getCells(1), GT_Utility.getIntegratedCircuit(1), Materials.HydricSulfide.getGas(2000), Materials.Water.getFluid(2000), Materials.Sulfur.getDust(3), Materials.Empty.getCells(1), 120);
        GT_Values.RA.addChemicalRecipe(Materials.HydricSulfide.getCells(2), GT_Utility.getIntegratedCircuit(1), Materials.SulfurDioxide.getGas(1000), Materials.Water.getFluid(2000), Materials.Sulfur.getDust(3), Materials.Empty.getCells(2), 120);
        GT_Values.RA.addChemicalRecipe(Materials.SulfurDioxide.getCells(1), GT_Utility.getIntegratedCircuit(2), Materials.HydricSulfide.getGas(2000), GT_Values.NF,                   Materials.Sulfur.getDust(3), Materials.Empty.getCells(1), 120);
        GT_Values.RA.addChemicalRecipe(Materials.HydricSulfide.getCells(2), GT_Utility.getIntegratedCircuit(2), Materials.SulfurDioxide.getGas(1000), GT_Values.NF,                   Materials.Sulfur.getDust(3), Materials.Empty.getCells(2), 120);

        //GT_Values.RA.addChemicalRecipe(Materials.Sulfur.getDust(1),         GT_Utility.getIntegratedCircuit(3),  Materials.Oxygen.getGas(3000), Materials.SulfurTrioxide.getGas(1000), GT_Values.NI, 280);
        //GT_Values.RA.addChemicalRecipe(Materials.Sulfur.getDust(1),         Materials.Empty.getCells(1),         Materials.Oxygen.getGas(3000), GT_Values.NF,                          Materials.SulfurTrioxide.getCells(1), 280);
        GT_Values.RA.addChemicalRecipe(Materials.Oxygen.getCells(1),        GT_Utility.getIntegratedCircuit(1),  Materials.SulfurDioxide.getGas(1000), Materials.SulfurTrioxide.getGas(1000), Materials.Empty.getCells(1), 200);
        GT_Values.RA.addChemicalRecipe(Materials.SulfurDioxide.getCells(1), GT_Utility.getIntegratedCircuit(1),  Materials.Oxygen.getGas(1000),        Materials.SulfurTrioxide.getGas(1000), Materials.Empty.getCells(1), 200);
        GT_Values.RA.addChemicalRecipe(Materials.Oxygen.getCells(1),        GT_Utility.getIntegratedCircuit(11), Materials.SulfurDioxide.getGas(1000), GT_Values.NF,                          Materials.SulfurTrioxide.getCells(1), 200);
        GT_Values.RA.addChemicalRecipe(Materials.SulfurDioxide.getCells(1), GT_Utility.getIntegratedCircuit(11), Materials.Oxygen.getGas(1000),        GT_Values.NF,                          Materials.SulfurTrioxide.getCells(1), 200);

        GT_Values.RA.addChemicalRecipe(Materials.Water.getCells(1),          GT_Utility.getIntegratedCircuit(1),  Materials.SulfurTrioxide.getGas(1000), Materials.SulfuricAcid.getFluid(1000), Materials.Empty.getCells(1), 320, 8);
        GT_Values.RA.addChemicalRecipe(Materials.SulfurTrioxide.getCells(1), GT_Utility.getIntegratedCircuit(1),  Materials.Water.getFluid(1000),        Materials.SulfuricAcid.getFluid(1000), Materials.Empty.getCells(1), 320, 8);
        GT_Values.RA.addChemicalRecipe(Materials.Water.getCells(1),          GT_Utility.getIntegratedCircuit(11), Materials.SulfurTrioxide.getGas(1000), GT_Values.NF,                          Materials.SulfuricAcid.getCells(1), 320, 8);
        GT_Values.RA.addChemicalRecipe(Materials.SulfurTrioxide.getCells(1), GT_Utility.getIntegratedCircuit(11), Materials.Water.getFluid(1000),        GT_Values.NF,                          Materials.SulfuricAcid.getCells(1), 320, 8);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{Materials.Sulfur.getDust(1), GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.Oxygen.getGas(3000),        Materials.Water.getFluid(1000)},                                new FluidStack[]{Materials.SulfuricAcid.getFluid(1000)}, null, 480, 30);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(24)},                              new FluidStack[]{Materials.HydricSulfide.getGas(1000), Materials.Oxygen.getGas(3000)},                                 new FluidStack[]{Materials.SulfuricAcid.getFluid(1000)}, null, 480, 30);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(24)},                              new FluidStack[]{Materials.SulfurDioxide.getGas(1000), Materials.Oxygen.getGas(1000), Materials.Water.getFluid(1000)}, new FluidStack[]{Materials.SulfuricAcid.getFluid(1000)}, null, 480, 30);

        GT_Values.RA.addUniversalDistillationRecipe(Materials.DilutedSulfuricAcid.getFluid(3000), new FluidStack[]{Materials.SulfuricAcid.getFluid(2000), Materials.Water.getFluid(1000)}, GT_Values.NI, 600, 120);

        GT_Values.RA.addChemicalRecipe(Materials.Chlorine.getCells(2), GT_Utility.getIntegratedCircuit(1),  Materials.Ethylene.getGas(1000), Materials.VinylChloride.getGas(1000),      Materials.HydrochloricAcid.getCells(1), Materials.Empty.getCells(1), 160);
        GT_Values.RA.addChemicalRecipe(Materials.Ethylene.getCells(1), GT_Utility.getIntegratedCircuit(1),  Materials.Chlorine.getGas(2000), Materials.VinylChloride.getGas(1000),      Materials.HydrochloricAcid.getCells(1), 160);
        GT_Values.RA.addChemicalRecipe(Materials.Chlorine.getCells(2), GT_Utility.getIntegratedCircuit(11), Materials.Ethylene.getGas(1000), Materials.HydrochloricAcid.getFluid(1000), Materials.VinylChloride.getCells(1), Materials.Empty.getCells(1), 160);
        GT_Values.RA.addChemicalRecipe(Materials.Ethylene.getCells(1), GT_Utility.getIntegratedCircuit(11), Materials.Chlorine.getGas(2000), Materials.HydrochloricAcid.getFluid(1000), Materials.VinylChloride.getCells(1), 160);

        GT_Values.RA.addChemicalRecipe(Materials.Ethylene.getCells(1), 			Materials.HydrochloricAcid.getCells(1), Materials.Oxygen.getGas(1000), 				Materials.VinylChloride.getGas(1000), Materials.Water.getCells(1), Materials.Empty.getCells(1), 160);
        GT_Values.RA.addChemicalRecipe(Materials.HydrochloricAcid.getCells(1), 	Materials.Oxygen.getCells(1), 			Materials.Ethylene.getGas(1000), 			Materials.VinylChloride.getGas(1000), Materials.Water.getCells(1), Materials.Empty.getCells(1), 160);
        GT_Values.RA.addChemicalRecipe(Materials.Oxygen.getCells(1), 			Materials.Ethylene.getCells(1), 		Materials.HydrochloricAcid.getFluid(1000),  Materials.VinylChloride.getGas(1000), Materials.Water.getCells(1), Materials.Empty.getCells(1), 160);

        GT_Values.RA.addDefaultPolymerizationRecipes(Materials.VinylChloride.mGas, Materials.VinylChloride.getCells(1), Materials.PolyvinylChloride.mStandardMoltenFluid);
 
        GT_Values.RA.addMixerRecipe(Materials.Sugar.getDust(4), GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.SulfuricAcid.getFluid(1000), Materials.DilutedSulfuricAcid.getFluid(1000), Materials.Charcoal.getGems(1), 1200, 2);
        GT_Values.RA.addMixerRecipe(Materials.Wood.getDust(4), 	GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.SulfuricAcid.getFluid(1000), Materials.DilutedSulfuricAcid.getFluid(1000), Materials.Charcoal.getGems(1), 1200, 2);

        GT_Values.RA.addUniversalDistillationRecipe(Materials.Acetone.getFluid(1000), new FluidStack[]{Materials.Ethenone.getGas(1000), Materials.Methane.getGas(1000)}, GT_Values.NI, 80, 640);
        GT_Values.RA.addFluidHeaterRecipe(GT_Utility.getIntegratedCircuit(1), Materials.Acetone.getFluid(1000), Materials.Ethenone.getGas(1000), 160, 160);
        //GameRegistry.addSmelting(Materials.Acetone.getCells(1), Materials.Ethenone.getCells(1), 0);
        GT_Values.RA.addChemicalRecipe(Materials.AceticAcid.getCells(1),   GT_Utility.getIntegratedCircuit(1),  Materials.SulfuricAcid.getFluid(1000), Materials.DilutedSulfuricAcid.getFluid(1000), Materials.Ethenone.getCells(1), 160, 120);
        GT_Values.RA.addChemicalRecipe(Materials.SulfuricAcid.getCells(1), GT_Utility.getIntegratedCircuit(1),  Materials.AceticAcid.getFluid(1000),   Materials.DilutedSulfuricAcid.getFluid(1000), Materials.Ethenone.getCells(1), 160, 120);
        GT_Values.RA.addChemicalRecipe(Materials.AceticAcid.getCells(1),   GT_Utility.getIntegratedCircuit(11), Materials.SulfuricAcid.getFluid(1000), Materials.Ethenone.getGas(1000), Materials.DilutedSulfuricAcid.getCells(1), 160, 120);
        GT_Values.RA.addChemicalRecipe(Materials.SulfuricAcid.getCells(1), GT_Utility.getIntegratedCircuit(11), Materials.AceticAcid.getFluid(1000),   Materials.Ethenone.getGas(1000), Materials.DilutedSulfuricAcid.getCells(1), 160, 120);
        
        GT_Values.RA.addChemicalRecipe(Materials.Ethenone.getCells(1),   Materials.Empty.getCells(1),         Materials.NitricAcid.getFluid(8000), Materials.Water.getFluid(9000),             Materials.Tetranitromethane.getCells(2), 480, 120);
        GT_Values.RA.addChemicalRecipe(Materials.Ethenone.getCells(1),   GT_Utility.getIntegratedCircuit(12), Materials.NitricAcid.getFluid(8000), Materials.Tetranitromethane.getFluid(2000), Materials.Empty.getCells(1), 480, 120);
        GT_Values.RA.addChemicalRecipe(Materials.NitricAcid.getCells(8), GT_Utility.getIntegratedCircuit(1),  Materials.Ethenone.getGas(1000),     Materials.Water.getFluid(9000),             Materials.Tetranitromethane.getCells(2), Materials.Empty.getCells(6), 480, 120);
        GT_Values.RA.addChemicalRecipe(Materials.NitricAcid.getCells(8), GT_Utility.getIntegratedCircuit(2),  Materials.Ethenone.getGas(1000),     GT_Values.NF,                               Materials.Tetranitromethane.getCells(2), Materials.Empty.getCells(6), 480, 120);
        GT_Values.RA.addChemicalRecipe(Materials.NitricAcid.getCells(8), GT_Utility.getIntegratedCircuit(12), Materials.Ethenone.getGas(1000),     Materials.Tetranitromethane.getFluid(2000), Materials.Empty.getCells(8), 480, 120);
        GT_Values.RA.addChemicalRecipe(Materials.NitricAcid.getCells(8), Materials.Empty.getCells(1),         Materials.Ethenone.getGas(1000),     Materials.Tetranitromethane.getFluid(2000), Materials.Water.getCells(9), 480, 120);
        GT_Values.RA.addChemicalRecipe(Materials.Ethenone.getCells(1),   Materials.NitricAcid.getCells(8),    GT_Values.NF,                        Materials.Tetranitromethane.getFluid(2000), Materials.Water.getCells(9), 480, 120);

        //GT_Values.RA.addMixerRecipe(Materials.LightFuel.getCells(1), GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.Tetranitromethane.getFluid(20), Materials.NitroFuel.getFluid(1000), Materials.Empty.getCells(1), 80, 8);
        GT_Values.RA.addMixerRecipe(Materials.Fuel.getCells(1),      GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.Tetranitromethane.getFluid(20), Materials.NitroFuel.getFluid(1000), Materials.Empty.getCells(1), 20, 480);
        GT_Values.RA.addMixerRecipe(Materials.BioDiesel.getCells(1), GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.Tetranitromethane.getFluid(40), Materials.NitroFuel.getFluid(750), Materials.Empty.getCells(1), 20, 480);

        GT_Values.RA.addChemicalRecipe(Materials.Propene.getCells(1),  Materials.Empty.getCells(1),         Materials.Ethylene.getGas(1000), Materials.Isoprene.getFluid(1000), Materials.Hydrogen.getCells(2), 120);
        GT_Values.RA.addChemicalRecipe(Materials.Ethylene.getCells(1), Materials.Empty.getCells(1),         Materials.Propene.getGas(1000),  Materials.Isoprene.getFluid(1000), Materials.Hydrogen.getCells(2), 120);
        GT_Values.RA.addChemicalRecipe(Materials.Propene.getCells(1),  GT_Utility.getIntegratedCircuit(1),  Materials.Ethylene.getGas(1000), Materials.Hydrogen.getGas(2000),   Materials.Isoprene.getCells(1), 120);
        GT_Values.RA.addChemicalRecipe(Materials.Ethylene.getCells(1), GT_Utility.getIntegratedCircuit(1),  Materials.Propene.getGas(1000),  Materials.Hydrogen.getGas(2000),   Materials.Isoprene.getCells(1), 120);
        GT_Values.RA.addChemicalRecipe(Materials.Empty.getCells(1),    GT_Utility.getIntegratedCircuit(2),  Materials.Propene.getGas(2000),  Materials.Isoprene.getFluid(1000), Materials.Methane.getCells(1),  120);
        GT_Values.RA.addChemicalRecipe(Materials.Propene.getCells(2),  GT_Utility.getIntegratedCircuit(2),  GT_Values.NF,                    Materials.Isoprene.getFluid(1000), Materials.Methane.getCells(1),  Materials.Empty.getCells(1), 120);
        GT_Values.RA.addChemicalRecipe(Materials.Empty.getCells(1),    GT_Utility.getIntegratedCircuit(12), Materials.Propene.getGas(2000),  Materials.Methane.getGas(1000),    Materials.Isoprene.getCells(1), 120);
        GT_Values.RA.addChemicalRecipe(Materials.Propene.getCells(2),  GT_Utility.getIntegratedCircuit(12), GT_Values.NF,                    Materials.Methane.getGas(1000),    Materials.Isoprene.getCells(1), Materials.Empty.getCells(1), 120);

        GT_Values.RA.addChemicalRecipe(ItemList.Cell_Air.get(1, new Object[0]), GT_Utility.getIntegratedCircuit(1), Materials.Isoprene.getFluid(144),  GT_Values.NF, Materials.RawRubber.getDust(1),  Materials.Empty.getCells(1),  160);
        GT_Values.RA.addChemicalRecipe(Materials.Oxygen.getCells(2),            GT_Utility.getIntegratedCircuit(1), Materials.Isoprene.getFluid(288),  GT_Values.NF, Materials.RawRubber.getDust(3),  Materials.Empty.getCells(2),  320);
        GT_Values.RA.addChemicalRecipe(Materials.Isoprene.getCells(1),          GT_Utility.getIntegratedCircuit(1), Materials.Air.getGas(14000),       GT_Values.NF, Materials.RawRubber.getDust(7),  Materials.Empty.getCells(1),  1120);
        GT_Values.RA.addChemicalRecipe(Materials.Isoprene.getCells(2),          GT_Utility.getIntegratedCircuit(1), Materials.Oxygen.getGas(14000),    GT_Values.NF, Materials.RawRubber.getDust(21),  Materials.Empty.getCells(2), 2240);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(2)}, new FluidStack[]{Materials.Isoprene.getFluid(1728), Materials.Air.getGas(6000), Materials.Titaniumtetrachloride.getFluid(80)}, null, new ItemStack[]{Materials.RawRubber.getDust(18)}, 640, 30);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(2)}, new FluidStack[]{Materials.Isoprene.getFluid(1728), Materials.Oxygen.getGas(6000), Materials.Titaniumtetrachloride.getFluid(80)}, null, new ItemStack[]{Materials.RawRubber.getDust(24)}, 640, 30);

        GT_Values.RA.addChemicalRecipe(Materials.Benzene.getCells(1),  GT_Utility.getIntegratedCircuit(1), Materials.Ethylene.getGas(1000),  Materials.Hydrogen.getGas(2000),  Materials.Styrene.getCells(1), 120);
        GT_Values.RA.addChemicalRecipe(Materials.Ethylene.getCells(1), GT_Utility.getIntegratedCircuit(1), Materials.Benzene.getFluid(1000), Materials.Hydrogen.getGas(2000),  Materials.Styrene.getCells(1), 120);
        GT_Values.RA.addChemicalRecipe(Materials.Benzene.getCells(1),  Materials.Empty.getCells(1),        Materials.Ethylene.getGas(1000),  Materials.Styrene.getFluid(1000), Materials.Hydrogen.getCells(2), 120);
        GT_Values.RA.addChemicalRecipe(Materials.Ethylene.getCells(1), Materials.Empty.getCells(1),        Materials.Benzene.getFluid(1000), Materials.Styrene.getFluid(1000), Materials.Hydrogen.getCells(2), 120);

        //GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(2)}, new FluidStack[]{Materials.Benzene.getFluid(1000),  Materials.Ethylene.getGas(1000)},  new FluidStack[]{Materials.Hydrogen.getGas(2000),  Materials.Styrene.getFluid(1000)}, null,120, 30);

        GT_Values.RA.addDefaultPolymerizationRecipes(Materials.Styrene.mFluid, Materials.Styrene.getCells(1), Materials.Polystyrene.mStandardMoltenFluid);

        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Butadiene.getCells(1), ItemList.Cell_Air.get(5, new Object[0]),  Materials.Styrene.getFluid(350),  GT_Values.NF, Materials.RawStyreneButadieneRubber.getDust(9),  Materials.Empty.getCells(6),  160, 240);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Butadiene.getCells(1), Materials.Oxygen.getCells(5),             Materials.Styrene.getFluid(350),  GT_Values.NF, Materials.RawStyreneButadieneRubber.getDust(13), Materials.Empty.getCells(6),  160, 240);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Styrene.getCells(1),   ItemList.Cell_Air.get(15, new Object[0]), Materials.Butadiene.getGas(3000), GT_Values.NF, Materials.RawStyreneButadieneRubber.getDust(27), Materials.Empty.getCells(16), 480, 240);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Styrene.getCells(1),   Materials.Oxygen.getCells(15),            Materials.Butadiene.getGas(3000), GT_Values.NF, Materials.RawStyreneButadieneRubber.getDust(41), Materials.Empty.getCells(16), 480, 240);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Styrene.getCells(1),   Materials.Butadiene.getCells(3),          Materials.Air.getGas(15000),      GT_Values.NF, Materials.RawStyreneButadieneRubber.getDust(27), Materials.Empty.getCells(4),  480, 240);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Styrene.getCells(1),   Materials.Butadiene.getCells(3),          Materials.Oxygen.getGas(15000),   GT_Values.NF, Materials.RawStyreneButadieneRubber.getDust(41), Materials.Empty.getCells(4),  480, 240);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(1)}, new FluidStack[]{Materials.Ethylene.getGas(1000),  Materials.Benzene.getFluid(1000)}, new FluidStack[]{Materials.Styrene.getFluid(1000), Materials.Hydrogen.getGas(2000)}, null,120, 30);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(3)}, new FluidStack[]{Materials.Styrene.getFluid(36),  Materials.Butadiene.getGas(108), Materials.Air.getGas(2000)},                                                     null, new ItemStack[]{Materials.RawStyreneButadieneRubber.getDust(1)}, 160, 240);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(3)}, new FluidStack[]{Materials.Styrene.getFluid(72),  Materials.Butadiene.getGas(216), Materials.Oxygen.getGas(2000)},                                                  null, new ItemStack[]{Materials.RawStyreneButadieneRubber.getDust(3)}, 160, 240);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(4)}, new FluidStack[]{Materials.Styrene.getFluid(540), Materials.Butadiene.getGas(1620), Materials.Titaniumtetrachloride.getFluid(100), Materials.Air.getGas(15000)},    null, new ItemStack[]{Materials.RawStyreneButadieneRubber.getDust(22), Materials.RawStyreneButadieneRubber.getDustSmall(2)}, 640, 240);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(4)}, new FluidStack[]{Materials.Styrene.getFluid(540), Materials.Butadiene.getGas(1620), Materials.Titaniumtetrachloride.getFluid(100), Materials.Oxygen.getGas(7500)},  null, new ItemStack[]{Materials.RawStyreneButadieneRubber.getDust(30)}, 640, 240);

        GT_Values.RA.addChemicalRecipe(Materials.RawStyreneButadieneRubber.getDust(9), Materials.Sulfur.getDust(1), GT_Values.NF, Materials.StyreneButadieneRubber.getMolten(1296), GT_Values.NI, 600);

        GT_Values.RA.addChemicalRecipe(                   Materials.Benzene.getCells(1),  GT_Utility.getIntegratedCircuit(2),  Materials.Chlorine.getGas(4000),  Materials.HydrochloricAcid.getFluid(2000), Materials.Dichlorobenzene.getCells(1),  240);
        GT_Values.RA.addChemicalRecipe(                   Materials.Chlorine.getCells(4), GT_Utility.getIntegratedCircuit(2),  Materials.Benzene.getFluid(1000), Materials.HydrochloricAcid.getFluid(2000), Materials.Dichlorobenzene.getCells(1),  Materials.Empty.getCells(3), 240);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Benzene.getCells(1),  Materials.Empty.getCells(1),         Materials.Chlorine.getGas(4000),  Materials.Dichlorobenzene.getFluid(1000),  Materials.HydrochloricAcid.getCells(2), GT_Values.NI, 240, 30);
        GT_Values.RA.addChemicalRecipe(                   Materials.Chlorine.getCells(4), GT_Utility.getIntegratedCircuit(12), Materials.Benzene.getFluid(1000), Materials.Dichlorobenzene.getFluid(1000),  Materials.HydrochloricAcid.getCells(2), Materials.Empty.getCells(2), 240);

        GT_Values.RA.addChemicalRecipe(Materials.SodiumSulfide.getDust(1), ItemList.Cell_Air.get(8, new Object[0]), Materials.Dichlorobenzene.getFluid(1000), Materials.PolyphenyleneSulfide.getMolten(1000), Materials.Salt.getDust(2), Materials.Empty.getCells(8), 240, 360);
        GT_Values.RA.addChemicalRecipe(Materials.SodiumSulfide.getDust(1), Materials.Oxygen.getCells(8),            Materials.Dichlorobenzene.getFluid(1000), Materials.PolyphenyleneSulfide.getMolten(1500), Materials.Salt.getDust(2), Materials.Empty.getCells(8), 240, 360);

        GT_Values.RA.addChemicalRecipe(Materials.Salt.getDust(2), GT_Utility.getIntegratedCircuit(1), Materials.SulfuricAcid.getFluid(1000), Materials.HydrochloricAcid.getFluid(1000), Materials.SodiumBisulfate.getDust(1), 60);
        GT_Values.RA.addElectrolyzerRecipe(Materials.SodiumBisulfate.getDust(2), Materials.Empty.getCells(2), null, Materials.SodiumPersulfate.getFluid(1000), Materials.Hydrogen.getCells(2), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 600, 30);

        GT_Values.RA.addChemicalRecipe(Materials.SodiumHydroxide.getDustTiny(1), Materials.Methanol.getCells(1), Materials.SeedOil.getFluid(6000), Materials.BioDiesel.getFluid(6000), Materials.Glycerol.getCells(1), 600);
        GT_Values.RA.addChemicalRecipe(Materials.SodiumHydroxide.getDustTiny(1), Materials.SeedOil.getCells(6), Materials.Methanol.getFluid(1000), Materials.Glycerol.getFluid(1000), Materials.BioDiesel.getCells(6), 600);
        GT_Values.RA.addChemicalRecipe(Materials.SodiumHydroxide.getDustTiny(1), Materials.Methanol.getCells(1), Materials.FishOil.getFluid(6000), Materials.BioDiesel.getFluid(6000), Materials.Glycerol.getCells(1), 600);
        GT_Values.RA.addChemicalRecipe(Materials.SodiumHydroxide.getDustTiny(1), Materials.FishOil.getCells(6), Materials.Methanol.getFluid(1000), Materials.Glycerol.getFluid(1000), Materials.BioDiesel.getCells(6), 600);

        GT_Values.RA.addChemicalRecipe(Materials.SodiumHydroxide.getDustTiny(1), Materials.Ethanol.getCells(1), Materials.SeedOil.getFluid(6000), Materials.BioDiesel.getFluid(6000), Materials.Glycerol.getCells(1), 600);
        GT_Values.RA.addChemicalRecipe(Materials.SodiumHydroxide.getDustTiny(1), Materials.SeedOil.getCells(6), Materials.Ethanol.getFluid(1000), Materials.Glycerol.getFluid(1000), Materials.BioDiesel.getCells(6), 600);
        GT_Values.RA.addChemicalRecipe(Materials.SodiumHydroxide.getDustTiny(1), Materials.Ethanol.getCells(1), Materials.FishOil.getFluid(6000), Materials.BioDiesel.getFluid(6000), Materials.Glycerol.getCells(1), 600);
        GT_Values.RA.addChemicalRecipe(Materials.SodiumHydroxide.getDustTiny(1), Materials.FishOil.getCells(6), Materials.Ethanol.getFluid(1000), Materials.Glycerol.getFluid(1000), Materials.BioDiesel.getCells(6), 600);

        GT_Values.RA.addChemicalRecipe(                   Materials.Glycerol.getCells(1),         GT_Utility.getIntegratedCircuit(1),  Materials.NitrationMixture.getFluid(3000), Materials.DilutedSulfuricAcid.getFluid(3000), Materials.Glyceryl.getCells(1), 180);
        GT_Values.RA.addChemicalRecipe(                   Materials.NitrationMixture.getCells(3), GT_Utility.getIntegratedCircuit(1),  Materials.Glycerol.getFluid(1000),         Materials.DilutedSulfuricAcid.getFluid(3000), Materials.Glyceryl.getCells(1), Materials.Empty.getCells(2), 180);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Glycerol.getCells(1),         Materials.Empty.getCells(2),         Materials.NitrationMixture.getFluid(3000), Materials.Glyceryl.getFluid(1000),            Materials.DilutedSulfuricAcid.getCells(3), GT_Values.NI, 180, 30);
        GT_Values.RA.addChemicalRecipe(                   Materials.NitrationMixture.getCells(3), GT_Utility.getIntegratedCircuit(11), Materials.Glycerol.getFluid(1000),         Materials.Glyceryl.getFluid(1000),            Materials.DilutedSulfuricAcid.getCells(3), 180);

        GT_Values.RA.addChemicalRecipe(Materials.Quicklime.getDust(1), GT_Values.NI,                       Materials.CarbonDioxide.getGas(1000), GT_Values.NF,                         Materials.Calcite.getDust(1), 80);
        GT_Values.RA.addChemicalRecipe(Materials.Calcite.getDust(1),   GT_Utility.getIntegratedCircuit(1), GT_Values.NF,                         Materials.CarbonDioxide.getGas(1000), Materials.Quicklime.getDust(1), 240);
        GT_Values.RA.addChemicalRecipe(Materials.Magnesia.getDust(1),  GT_Values.NI,                       Materials.CarbonDioxide.getGas(1000), GT_Values.NF,                         Materials.Magnesite.getDust(1), 80);
        GT_Values.RA.addChemicalRecipe(Materials.Magnesite.getDust(1), GT_Utility.getIntegratedCircuit(1), GT_Values.NF,                         Materials.CarbonDioxide.getGas(1000), Materials.Magnesia.getDust(1), 240);

        GT_Values.RA.addChemicalRecipe(Materials.Methane.getCells(1), Materials.Empty.getCells(7),         Materials.Water.getFluid(2000), Materials.CarbonDioxide.getGas(1000), Materials.Hydrogen.getCells(8),      150, 480);
        GT_Values.RA.addChemicalRecipe(Materials.Water.getCells(2),   Materials.Empty.getCells(6),         Materials.Methane.getGas(1000), Materials.CarbonDioxide.getGas(1000), Materials.Hydrogen.getCells(8),      150, 480);
        GT_Values.RA.addChemicalRecipe(Materials.Methane.getCells(1), GT_Utility.getIntegratedCircuit(11), Materials.Water.getFluid(2000), Materials.Hydrogen.getGas(8000),      Materials.CarbonDioxide.getCells(1), 150, 480);
        GT_Values.RA.addChemicalRecipe(Materials.Water.getCells(2),   GT_Utility.getIntegratedCircuit(11), Materials.Methane.getGas(1000), Materials.Hydrogen.getGas(8000),      Materials.CarbonDioxide.getCells(1), Materials.Empty.getCells(1), 150, 480);
        GT_Values.RA.addChemicalRecipe(Materials.Methane.getCells(1), GT_Utility.getIntegratedCircuit(12), Materials.Water.getFluid(2000), Materials.Hydrogen.getGas(8000),      Materials.Empty.getCells(1), 150, 480);
        GT_Values.RA.addChemicalRecipe(Materials.Water.getCells(2),   GT_Utility.getIntegratedCircuit(12), Materials.Methane.getGas(1000), Materials.Hydrogen.getGas(8000),      Materials.Empty.getCells(2), 150, 480);

        GT_Values.RA.addChemicalRecipe(Materials.Benzene.getCells(1),  GT_Utility.getIntegratedCircuit(1),  Materials.Chlorine.getGas(2000),  Materials.HydrochloricAcid.getFluid(1000), Materials.Chlorobenzene.getCells(1),    240);
        GT_Values.RA.addChemicalRecipe(Materials.Chlorine.getCells(2), GT_Utility.getIntegratedCircuit(1),  Materials.Benzene.getFluid(1000), Materials.HydrochloricAcid.getFluid(1000), Materials.Chlorobenzene.getCells(1),    Materials.Empty.getCells(1), 240);
        GT_Values.RA.addChemicalRecipe(Materials.Chlorine.getCells(2), GT_Utility.getIntegratedCircuit(11), Materials.Benzene.getFluid(1000), Materials.Chlorobenzene.getFluid(1000),    Materials.HydrochloricAcid.getCells(1), Materials.Empty.getCells(1), 240);

        GT_Values.RA.addChemicalRecipe(Materials.Water.getCells(1),         GT_Utility.getIntegratedCircuit(1),  Materials.Chlorobenzene.getFluid(1000), Materials.Phenol.getFluid(1000),                  Materials.DilutedHydrochloricAcid.getCells(1), 240);
        GT_Values.RA.addChemicalRecipe(Materials.Chlorobenzene.getCells(1), GT_Utility.getIntegratedCircuit(1),  Materials.Water.getFluid(1000),         Materials.Phenol.getFluid(1000),                  Materials.DilutedHydrochloricAcid.getCells(1), 240);
        GT_Values.RA.addChemicalRecipe(Materials.Water.getCells(1),         GT_Utility.getIntegratedCircuit(11), Materials.Chlorobenzene.getFluid(1000), Materials.DilutedHydrochloricAcid.getFluid(1000), Materials.Phenol.getCells(1), 240);
        GT_Values.RA.addChemicalRecipe(Materials.Chlorobenzene.getCells(1), GT_Utility.getIntegratedCircuit(11), Materials.Water.getFluid(1000),         Materials.DilutedHydrochloricAcid.getFluid(1000), Materials.Phenol.getCells(1), 240);

        GT_Values.RA.addChemicalRecipe(                   Materials.SodiumHydroxide.getDust(4), GT_Utility.getIntegratedCircuit(1),  Materials.Chlorobenzene.getFluid(4000), Materials.Phenol.getFluid(4000), Materials.Salt.getDust(6), 960);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.SodiumHydroxide.getDust(4), Materials.Empty.getCells(4),         Materials.Chlorobenzene.getFluid(4000), GT_Values.NF,                    Materials.Salt.getDust(6), Materials.Phenol.getCells(4), 960, 30);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.SodiumHydroxide.getDust(4), Materials.Chlorobenzene.getCells(4), GT_Values.NF,                           GT_Values.NF,                    Materials.Salt.getDust(6), Materials.Phenol.getCells(4), 960, 30);

        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.Benzene.getFluid(1000), Materials.Chlorine.getGas(2000), Materials.Water.getFluid(1000)}, new FluidStack[]{Materials.Phenol.getFluid(1000), Materials.HydrochloricAcid.getFluid(1000), Materials.DilutedHydrochloricAcid.getFluid(1000)}, null, 560, 30);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{Materials.SodiumHydroxide.getDust(2), GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.Benzene.getFluid(2000), Materials.Chlorine.getGas(4000)}, new FluidStack[]{Materials.Phenol.getFluid(2000), Materials.HydrochloricAcid.getFluid(2000)}, new ItemStack[]{Materials.Salt.getDust(3)}, 1120, 30);

        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.LightFuel.getFluid(20000), Materials.HeavyFuel.getFluid(4000)}, new FluidStack[]{Materials.Fuel.getFluid(24000)}, null, 100, 480);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.Fuel.getFluid(10000), Materials.Tetranitromethane.getFluid(200)}, new FluidStack[]{Materials.NitroFuel.getFluid(10000)}, null, 100, 480);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.BioDiesel.getFluid(10000), Materials.Tetranitromethane.getFluid(400)}, new FluidStack[]{Materials.NitroFuel.getFluid(7500)}, null, 100, 480);


    }

    //private void addOldChemicalRecipes() {
        //GT_Values.RA.setIsAddingDeprecatedRecipes(true);
        //GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Nitrogen, 1L), ItemList.Circuit_Integrated.getWithDamage(0, 1, new Object[0]), Materials.Oxygen.getGas(2000L), Materials.NitrogenDioxide.getGas(1000L), ItemList.Cell_Empty.get(1L, new Object[0]), GT_Values.NI,1250, 30);
        //GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 2L), ItemList.Circuit_Integrated.getWithDamage(0, 1, new Object[0]), Materials.Nitrogen.getGas(1000L), Materials.NitrogenDioxide.getGas(1000L), ItemList.Cell_Empty.get(2L, new Object[0]), GT_Values.NI,1250, 30);

        //GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), ItemList.Circuit_Integrated.getWithDamage(0L, 1L, new Object[0]), Materials.Water.getFluid(1000L), Materials.SulfuricAcid.getFluid(1000L), GT_Values.NI, GT_Values.NI, 1150, 30);
        //GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 2L), Materials.Oxygen.getGas(4000L), Materials.SulfuricAcid.getFluid(1000L), ItemList.Cell_Empty.get(2L, new Object[0]), GT_Values.NI, 1150,30);
        //GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 4L), Materials.Hydrogen.getGas(2000L), Materials.SulfuricAcid.getFluid(1000L), ItemList.Cell_Empty.get(4L, new Object[0]), GT_Values.NI, 1150,30);

        //GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.HydricSulfide, 2L), ItemList.Cell_Water.get(2, new Object[0]), null, Materials.SulfuricAcid.getFluid(2000), ItemList.Cell_Empty.get(4, new Object[0]), GT_Values.NI, 320, 30);
        //GT_Values.RA.addChemicalRecipe(ItemList.Cell_Water.get(2, new Object[0]),                               null, new FluidStack(ItemList.sHydricSulfur, 2000), Materials.SulfuricAcid.getFluid(2000), ItemList.Cell_Empty.get(2, new Object[0]), GT_Values.NI, 320, 30);
        //GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.HydricSulfide, 2L), null, Materials.Water.getFluid(2000),         Materials.SulfuricAcid.getFluid(2000), ItemList.Cell_Empty.get(2, new Object[0]), GT_Values.NI, 320, 30);

        //GT_Values.RA.addChemicalRecipe(ItemList.Cell_Air.get(2, new Object[0]), null, Materials.Naphtha.getFluid(288), Materials.Plastic.getMolten(144), ItemList.Cell_Empty.get(2, new Object[0]), GT_Values.NI, 640, 30);
        //GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Titanium, 1L), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 16L), Materials.Naphtha.getFluid(1296), Materials.Plastic.getMolten(1296), ItemList.Cell_Empty.get(16, new Object[0]), GT_Values.NI, 640,30);

        //GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Naphtha, 3L), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.NitrogenDioxide, 1L), new FluidStack(ItemList.sEpichlorhydrin, 144), Materials.Epoxid.getMolten(288), ItemList.Cell_Empty.get(4, new Object[0]), GT_Values.NI, 240, 30);

        //GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 1L), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Chlorine, 1L), Materials.LPG.getFluid(432), new FluidStack(ItemList.sEpichlorhydrin, 432), ItemList.Cell_Empty.get(1, new Object[0]), GT_Values.NI, 480, 30);
        //GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Naphtha, 3L), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Fluorine, 1L), new FluidStack(ItemList.sEpichlorhydrin, 432), Materials.Polytetrafluoroethylene.getMolten(432), ItemList.Cell_Empty.get(4, new Object[0]), GT_Values.NI, 240, 256);
        //GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 1L), null, new FluidStack(ItemList.sEpichlorhydrin, 144), Materials.Silicone.getMolten(144), GT_Values.NI, GT_Values.NI, 240, 96);

        //GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.NitrogenDioxide, 1L), GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 3L), Materials.Air.getGas(500), new FluidStack(ItemList.sRocketFuel,3000), ItemList.Cell_Water.get(4, new Object[0]), GT_Values.NI, 1000, 388);

        //GT_Values.RA.addCentrifugeRecipe(GT_Utility.getIntegratedCircuit(1), GT_Values.NI, Materials.Gas.getGas(8000), Materials.LPG.getFluid(4000), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{10000}, 200, 5);
        //GT_Values.RA.addCentrifugeRecipe(ItemList.Cell_Empty.get(4, new Object[0]), GT_Values.NI, Materials.Gas.getGas(8000), GT_Values.NF, GT_OreDictUnificator.get(OrePrefixes.cell, Materials.LPG, 4), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, new int[]{10000}, 200, 5);

        //GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Fuel, 1L), GT_Values.NI, Materials.Glyceryl.getFluid(250L), Materials.NitroFuel.getFluid(1000L), ItemList.Cell_Empty.get(1L, new Object[0]), GT_Values.NI, 250, 30);
        //GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Glyceryl, 1L), GT_Values.NI, Materials.Fuel.getFluid(4000L), Materials.NitroFuel.getFluid(4000L), ItemList.Cell_Empty.get(1L, new Object[0]), GT_Values.NI, 1000, 30);
        //GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.LightFuel, 1L), GT_Values.NI, Materials.Glyceryl.getFluid(250L), Materials.NitroFuel.getFluid(1250L), ItemList.Cell_Empty.get(1L, new Object[0]), GT_Values.NI, 250, 30);
        //GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Glyceryl, 1L), GT_Values.NI, Materials.LightFuel.getFluid(4000L), Materials.NitroFuel.getFluid(5000L), ItemList.Cell_Empty.get(1L, new Object[0]), GT_Values.NI, 1000, 30);

        //GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L), Materials.Oxygen.getGas(4000L), Materials.SodiumPersulfate.getFluid(6000L), GT_Values.NI, GT_Values.NI, 800, 30);
        //GT_Values.RA.addMixerRecipe(Materials.Sodium.getDust(2), Materials.Sulfur.getDust(1), GT_Utility.getIntegratedCircuit(2), null, null, null, Materials.SodiumSulfide.getDust(1), 60, 30);

        //GT_Values.RA.addChemicalRecipe(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Nitrogen, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 1L), Materials.Water.getFluid(2000L), Materials.Glyceryl.getFluid(4000L), ItemList.Cell_Empty.get(1L, new Object[0]), GT_Values.NI, 2700, 30);
        //GT_Values.RA.setIsAddingDeprecatedRecipes(false);
    //}

        private void addRecipesMay2017OilRefining() {
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Gas.getGas(1000), new FluidStack[]{Materials.Butane.getGas(60), Materials.Propane.getGas(70), Materials.Ethane.getGas(100), Materials.Methane.getGas(750), Materials.Helium.getGas(20)}, GT_Values.NI, 240, 120);

        GT_Values.RA.addCentrifugeRecipe(null, null, Materials.Propane.getGas(320), Materials.LPG.getFluid(290), null, null, null, null, null, null, null, 20, 5);
        GT_Values.RA.addCentrifugeRecipe(null, null, Materials.Butane.getGas(320), Materials.LPG.getFluid(370), null, null, null, null, null, null, null, 20, 5);

        GT_Values.RA.addUniversalDistillationRecipe(Materials.Ethylene.getLightlyHydroCracked(1000), new FluidStack[]{Materials.Ethane.getGas(1000)}, GT_Values.NI, 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Ethylene.getModeratelyHydroCracked(1000), new FluidStack[]{Materials.Methane.getGas(2000)}, null, 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Ethylene.getSeverelyHydroCracked(1000), new FluidStack[]{Materials.Methane.getGas(2000), Materials.Hydrogen.getGas(2000)}, GT_Values.NI, 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Ethylene.getLightlySteamCracked(1000), new FluidStack[]{Materials.Methane.getGas(1000)}, Materials.Carbon.getDust(1), 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Ethylene.getModeratelySteamCracked(1000), new FluidStack[]{Materials.Methane.getGas(1000)}, Materials.Carbon.getDust(1), 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Ethylene.getSeverelySteamCracked(1000), new FluidStack[]{Materials.Methane.getGas(1000)}, Materials.Carbon.getDust(1), 120, 120);

        GT_Values.RA.addUniversalDistillationRecipe(Materials.Ethane.getLightlyHydroCracked(1000), new FluidStack[]{Materials.Methane.getGas(2000)}, GT_Values.NI, 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Ethane.getModeratelyHydroCracked(1000), new FluidStack[]{Materials.Methane.getGas(2000), Materials.Hydrogen.getGas(2000)}, GT_Values.NI, 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Ethane.getSeverelyHydroCracked(1000), new FluidStack[]{Materials.Methane.getGas(2000), Materials.Hydrogen.getGas(4000)}, GT_Values.NI, 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Ethane.getLightlySteamCracked(1000), new FluidStack[]{Materials.Ethylene.getGas(250), Materials.Methane.getGas(1250)}, Materials.Carbon.getDustSmall(1), 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Ethane.getModeratelySteamCracked(1000), new FluidStack[]{Materials.Ethylene.getGas(125), Materials.Methane.getGas(1375)}, Materials.Carbon.getDustTiny(6), 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Ethane.getSeverelySteamCracked(1000), new FluidStack[]{Materials.Methane.getGas(1500)}, Materials.Carbon.getDustSmall(2), 120, 120);

        GT_Values.RA.addUniversalDistillationRecipe(Materials.Propene.getLightlyHydroCracked(1000), new FluidStack[]{Materials.Propane.getGas(500), Materials.Ethylene.getGas(500), Materials.Methane.getGas(500)}, GT_Values.NI, 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Propene.getModeratelyHydroCracked(1000), new FluidStack[]{Materials.Ethane.getGas(1000), Materials.Methane.getGas(1000)}, GT_Values.NI, 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Propene.getSeverelyHydroCracked(1000), new FluidStack[]{Materials.Methane.getGas(3000)}, GT_Values.NI, 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Propene.getLightlySteamCracked(1000), new FluidStack[]{Materials.Ethylene.getGas(1000), Materials.Methane.getGas(500)}, Materials.Carbon.getDustSmall(2), 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Propene.getModeratelySteamCracked(1000), new FluidStack[]{Materials.Ethylene.getGas(750), Materials.Methane.getGas(750)}, Materials.Carbon.getDustSmall(3), 180, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Propene.getSeverelySteamCracked(1000), new FluidStack[]{Materials.Methane.getGas(1500)}, Materials.Carbon.getDustSmall(6), 180, 120);

        GT_Values.RA.addUniversalDistillationRecipe(Materials.Propane.getLightlyHydroCracked(1000), new FluidStack[]{Materials.Ethane.getGas(1000), Materials.Methane.getGas(1000)}, GT_Values.NI, 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Propane.getModeratelyHydroCracked(1000), new FluidStack[]{Materials.Methane.getGas(3000)}, GT_Values.NI, 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Propane.getSeverelyHydroCracked(1000), new FluidStack[]{Materials.Methane.getGas(3000), Materials.Hydrogen.getGas(2000)}, GT_Values.NI, 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Propane.getLightlySteamCracked(1000), new FluidStack[]{Materials.Ethylene.getGas(750), Materials.Methane.getGas(1250)}, Materials.Carbon.getDustTiny(2), 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Propane.getModeratelySteamCracked(1000), new FluidStack[]{Materials.Ethylene.getGas(500), Materials.Methane.getGas(1500)}, Materials.Carbon.getDustSmall(1), 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Propane.getSeverelySteamCracked(1000), new FluidStack[]{Materials.Ethylene.getGas(250), Materials.Methane.getGas(1750)}, Materials.Carbon.getDustTiny(4), 120, 120);

        GT_Values.RA.addUniversalDistillationRecipe(Materials.Butadiene.getLightlyHydroCracked(1000), new FluidStack[]{Materials.Butene.getGas(667), Materials.Ethylene.getGas(667)}, GT_Values.NI, 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Butadiene.getModeratelyHydroCracked(1000), new FluidStack[]{Materials.Butane.getGas(223), Materials.Propene.getGas(223), Materials.Ethane.getGas(400), Materials.Ethylene.getGas(445), Materials.Methane.getGas(223)}, GT_Values.NI, 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Butadiene.getSeverelyHydroCracked(1000), new FluidStack[]{Materials.Propane.getGas(260), Materials.Ethane.getGas(926), Materials.Ethylene.getGas(389), Materials.Methane.getGas(2667)}, GT_Values.NI, 112, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Butadiene.getLightlySteamCracked(1000), new FluidStack[]{Materials.Propene.getGas(750), Materials.Ethylene.getGas(188), Materials.Methane.getGas(188)}, Materials.Carbon.getDustSmall(3), 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Butadiene.getModeratelySteamCracked(1000), new FluidStack[]{Materials.Propene.getGas(125), Materials.Ethylene.getGas(1125), Materials.Methane.getGas(188)}, Materials.Carbon.getDustSmall(3), 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Butadiene.getSeverelySteamCracked(1000), new FluidStack[]{Materials.Propene.getGas(125), Materials.Ethylene.getGas(188), Materials.Methane.getGas(1125)}, Materials.Carbon.getDust(1), 120, 120);

        GT_Values.RA.addUniversalDistillationRecipe(Materials.Butene.getLightlyHydroCracked(1000), new FluidStack[]{Materials.Butane.getGas(334), Materials.Propene.getGas(334), Materials.Ethane.getGas(334), Materials.Ethylene.getGas(334), Materials.Methane.getGas(334)}, GT_Values.NI, 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Butene.getModeratelyHydroCracked(1000), new FluidStack[]{Materials.Propane.getGas(389), Materials.Ethane.getGas(556), Materials.Ethylene.getGas(334), Materials.Methane.getGas(1056)}, GT_Values.NI, 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Butene.getSeverelyHydroCracked(1000), new FluidStack[]{Materials.Ethane.getGas(1000), Materials.Methane.getGas(2000)}, GT_Values.NI, 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Butene.getLightlySteamCracked(1000), new FluidStack[]{Materials.Propene.getGas(750), Materials.Ethylene.getGas(500), Materials.Methane.getGas(250)}, Materials.Carbon.getDustSmall(1), 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Butene.getModeratelySteamCracked(1000), new FluidStack[]{Materials.Propene.getGas(200), Materials.Ethylene.getGas(1300), Materials.Methane.getGas(400)}, Materials.Carbon.getDustSmall(1), 192, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Butene.getSeverelySteamCracked(1000), new FluidStack[]{Materials.Propene.getGas(125), Materials.Ethylene.getGas(313), Materials.Methane.getGas(1500)}, Materials.Carbon.getDustSmall(6), 120, 120);

        GT_Values.RA.addUniversalDistillationRecipe(Materials.Butane.getLightlyHydroCracked(1000), new FluidStack[]{Materials.Propane.getGas(667), Materials.Ethane.getGas(667), Materials.Methane.getGas(667)}, GT_Values.NI, 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Butane.getModeratelyHydroCracked(1000), new FluidStack[]{Materials.Ethane.getGas(1000), Materials.Methane.getGas(2000)}, GT_Values.NI, 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Butane.getSeverelyHydroCracked(1000), new FluidStack[]{Materials.Methane.getGas(1000)}, GT_Values.NI, 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Butane.getLightlySteamCracked(1000), new FluidStack[]{Materials.Propane.getGas(750), Materials.Ethane.getGas(125), Materials.Ethylene.getGas(125), Materials.Methane.getGas(1063)}, Materials.Carbon.getDustTiny(2), 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Butane.getModeratelySteamCracked(1000), new FluidStack[]{Materials.Propane.getGas(125), Materials.Ethane.getGas(750), Materials.Ethylene.getGas(750), Materials.Methane.getGas(438)}, Materials.Carbon.getDustTiny(2), 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Butane.getSeverelySteamCracked(1000), new FluidStack[]{Materials.Propane.getGas(125), Materials.Ethane.getGas(125), Materials.Ethylene.getGas(125), Materials.Methane.getGas(2000)}, Materials.Carbon.getDustTiny(11), 120, 120);

        GT_Values.RA.addUniversalDistillationRecipe(Materials.Gas.getLightlyHydroCracked(1000), new FluidStack[]{Materials.Methane.getGas(1400), Materials.Hydrogen.getGas(1340), Materials.Helium.getGas(20)}, GT_Values.NI, 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Gas.getModeratelyHydroCracked(1000), new FluidStack[]{Materials.Methane.getGas(1400), Materials.Hydrogen.getGas(3340), Materials.Helium.getGas(20)}, GT_Values.NI, 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Gas.getSeverelyHydroCracked(1000), new FluidStack[]{Materials.Methane.getGas(1400), Materials.Hydrogen.getGas(4340), Materials.Helium.getGas(20)}, GT_Values.NI, 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Gas.getLightlySteamCracked(1000), new FluidStack[]{Materials.Propene.getGas(45), Materials.Ethane.getGas(8), Materials.Ethylene.getGas(85), Materials.Methane.getGas(1026), Materials.Helium.getGas(20)}, Materials.Carbon.getDustTiny(1), 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Gas.getModeratelySteamCracked(1000), new FluidStack[]{Materials.Propene.getGas(8), Materials.Ethane.getGas(45), Materials.Ethylene.getGas(92), Materials.Methane.getGas(1018), Materials.Helium.getGas(20)}, Materials.Carbon.getDustTiny(1), 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Gas.getSeverelySteamCracked(1000), new FluidStack[]{Materials.Propene.getGas(8), Materials.Ethane.getGas(8), Materials.Ethylene.getGas(25), Materials.Methane.getGas(1143), Materials.Helium.getGas(20)}, Materials.Carbon.getDustTiny(1), 120, 120);

        GT_Values.RA.addUniversalDistillationRecipe(Materials.Naphtha.getLightlyHydroCracked(1000), new FluidStack[]{Materials.Butane.getGas(800), Materials.Propane.getGas(300), Materials.Ethane.getGas(250), Materials.Methane.getGas(250)}, GT_Values.NI, 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Naphtha.getModeratelyHydroCracked(1000), new FluidStack[]{Materials.Butane.getGas(200), Materials.Propane.getGas(1100), Materials.Ethane.getGas(400), Materials.Methane.getGas(400)}, GT_Values.NI, 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Naphtha.getSeverelyHydroCracked(1000), new FluidStack[]{Materials.Butane.getGas(125), Materials.Propane.getGas(125), Materials.Ethane.getGas(1500), Materials.Methane.getGas(1500)}, GT_Values.NI, 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Naphtha.getLightlySteamCracked(1000), new FluidStack[]{Materials.HeavyFuel.getFluid(75), Materials.LightFuel.getFluid(150), Materials.Toluene.getFluid(40), Materials.Benzene.getFluid(150), Materials.Butene.getGas(80), Materials.Butadiene.getGas(150), Materials.Propane.getGas(15), Materials.Propene.getGas(200), Materials.Ethane.getGas(35), Materials.Ethylene.getGas(200), Materials.Methane.getGas(200)}, Materials.Carbon.getDustTiny(1), 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Naphtha.getModeratelySteamCracked(1000), new FluidStack[]{Materials.HeavyFuel.getFluid(50), Materials.LightFuel.getFluid(100), Materials.Toluene.getFluid(30), Materials.Benzene.getFluid(125), Materials.Butene.getGas(65), Materials.Butadiene.getGas(100), Materials.Propane.getGas(30), Materials.Propene.getGas(400), Materials.Ethane.getGas(50), Materials.Ethylene.getGas(350), Materials.Methane.getGas(350)}, Materials.Carbon.getDustTiny(2), 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.Naphtha.getSeverelySteamCracked(1000), new FluidStack[]{Materials.HeavyFuel.getFluid(25), Materials.LightFuel.getFluid(50), Materials.Toluene.getFluid(20), Materials.Benzene.getFluid(100), Materials.Butene.getGas(50), Materials.Butadiene.getGas(50), Materials.Propane.getGas(15), Materials.Propene.getGas(300), Materials.Ethane.getGas(65), Materials.Ethylene.getGas(500), Materials.Methane.getGas(500)}, Materials.Carbon.getDustTiny(3), 120, 120);

        GT_Values.RA.addUniversalDistillationRecipe(Materials.LightFuel.getLightlyHydroCracked(1000), new FluidStack[]{Materials.Naphtha.getFluid(800), Materials.Octane.getFluid(100), Materials.Butane.getGas(150), Materials.Propane.getGas(200), Materials.Ethane.getGas(125), Materials.Methane.getGas(125)}, GT_Values.NI, 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.LightFuel.getModeratelyHydroCracked(1000), new FluidStack[]{Materials.Naphtha.getFluid(500), Materials.Octane.getFluid(50), Materials.Butane.getGas(200), Materials.Propane.getGas(1100), Materials.Ethane.getGas(400), Materials.Methane.getGas(400)}, GT_Values.NI, 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.LightFuel.getSeverelyHydroCracked(1000), new FluidStack[]{Materials.Naphtha.getFluid(200), Materials.Octane.getFluid(20), Materials.Butane.getGas(125), Materials.Propane.getGas(125), Materials.Ethane.getGas(1500), Materials.Methane.getGas(1500)}, GT_Values.NI, 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.LightFuel.getLightlySteamCracked(1000), new FluidStack[]{Materials.HeavyFuel.getFluid(150), Materials.Naphtha.getFluid(400), Materials.Toluene.getFluid(40), Materials.Benzene.getFluid(200), Materials.Butene.getGas(75), Materials.Butadiene.getGas(60), Materials.Propane.getGas(20), Materials.Propene.getGas(150), Materials.Ethane.getGas(10), Materials.Ethylene.getGas(50), Materials.Methane.getGas(50)}, Materials.Carbon.getDustTiny(1), 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.LightFuel.getModeratelySteamCracked(1000), new FluidStack[]{Materials.HeavyFuel.getFluid(100), Materials.Naphtha.getFluid(250), Materials.Toluene.getFluid(50), Materials.Benzene.getFluid(300), Materials.Butene.getGas(90), Materials.Butadiene.getGas(75), Materials.Propane.getGas(35), Materials.Propene.getGas(200), Materials.Ethane.getGas(30), Materials.Ethylene.getGas(150), Materials.Methane.getGas(150)}, Materials.Carbon.getDustTiny(2), 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.LightFuel.getSeverelySteamCracked(1000), new FluidStack[]{Materials.HeavyFuel.getFluid(50), Materials.Naphtha.getFluid(100), Materials.Toluene.getFluid(30), Materials.Benzene.getFluid(150), Materials.Butene.getGas(65), Materials.Butadiene.getGas(50), Materials.Propane.getGas(50), Materials.Propene.getGas(250), Materials.Ethane.getGas(50), Materials.Ethylene.getGas(250), Materials.Methane.getGas(250)}, Materials.Carbon.getDustTiny(3), 120, 120);

        GT_Values.RA.addUniversalDistillationRecipe(Materials.HeavyFuel.getLightlyHydroCracked(1000), new FluidStack[]{Materials.LightFuel.getFluid(600), Materials.Naphtha.getFluid(100), Materials.Butane.getGas(100), Materials.Propane.getGas(100), Materials.Ethane.getGas(75), Materials.Methane.getGas(75)}, GT_Values.NI, 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.HeavyFuel.getModeratelyHydroCracked(1000), new FluidStack[]{Materials.LightFuel.getFluid(400), Materials.Naphtha.getFluid(400), Materials.Butane.getGas(150), Materials.Propane.getGas(150), Materials.Ethane.getGas(100), Materials.Methane.getGas(100)}, GT_Values.NI, 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.HeavyFuel.getSeverelyHydroCracked(1000), new FluidStack[]{Materials.LightFuel.getFluid(200), Materials.Naphtha.getFluid(250), Materials.Butane.getGas(300), Materials.Propane.getGas(300), Materials.Ethane.getGas(175), Materials.Methane.getGas(175)}, GT_Values.NI, 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.HeavyFuel.getLightlySteamCracked(1000), new FluidStack[]{Materials.LightFuel.getFluid(300), Materials.Naphtha.getFluid(50), Materials.Toluene.getFluid(25), Materials.Benzene.getFluid(125), Materials.Butene.getGas(25), Materials.Butadiene.getGas(15), Materials.Propane.getGas(3), Materials.Propene.getGas(30), Materials.Ethane.getGas(5), Materials.Ethylene.getGas(50), Materials.Methane.getGas(50)}, Materials.Carbon.getDustTiny(1), 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.HeavyFuel.getModeratelySteamCracked(1000), new FluidStack[]{Materials.LightFuel.getFluid(200), Materials.Naphtha.getFluid(200), Materials.Toluene.getFluid(40), Materials.Benzene.getFluid(200), Materials.Butene.getGas(40), Materials.Butadiene.getGas(25), Materials.Propane.getGas(5), Materials.Propene.getGas(50), Materials.Ethane.getGas(7), Materials.Ethylene.getGas(75), Materials.Methane.getGas(75)}, Materials.Carbon.getDustTiny(2), 120, 120);
        GT_Values.RA.addUniversalDistillationRecipe(Materials.HeavyFuel.getSeverelySteamCracked(1000), new FluidStack[]{Materials.LightFuel.getFluid(100), Materials.Naphtha.getFluid(125), Materials.Toluene.getFluid(80), Materials.Benzene.getFluid(400), Materials.Butene.getGas(80), Materials.Butadiene.getGas(50), Materials.Propane.getGas(10), Materials.Propene.getGas(100), Materials.Ethane.getGas(15), Materials.Ethylene.getGas(150), Materials.Methane.getGas(150)}, Materials.Carbon.getDustTiny(3), 120, 120);

        //Recipes for gasoline
        GT_Values.RA.addChemicalRecipe(Materials.Nitrogen.getCells(2), Materials.Oxygen.getCells(1), GT_Values.NF, GT_Values.NF, Materials.NitrousOxide.getCells(3), 200, 30);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Ethanol.getCells(1), Materials.Butene.getCells(1), GT_Values.NF, GT_Values.NF, Materials.AntiKnock.getCells(2), GT_Values.NI, 400, 480);
        GT_Values.RA.addMixerRecipe(Materials.Naphtha.getCells(16), Materials.Gas.getCells(2), Materials.Methanol.getCells(1), Materials.Acetone.getCells(1), GT_Values.NF, GT_Values.NF, Materials.GasolineRaw.getCells(20), 100, 480);
        GT_Values.RA.addChemicalRecipe(Materials.GasolineRaw.getCells(10), Materials.Toluene.getCells(1), GT_Values.NF, GT_Values.NF, Materials.GasolineRegular.getCells(11), 10, 480);
        GT_Values.RA.addMixerRecipe(Materials.GasolineRegular.getCells(20), Materials.Octane.getCells(2), Materials.NitrousOxide.getCells(6), Materials.Toluene.getCells(1), Materials.AntiKnock.getFluid(3000L), Materials.GasolinePremium.getFluid(32000L), Materials.Empty.getCells(29), 50, 1920);

        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.Naphtha.getFluid(16000), Materials.Gas.getGas(2000), Materials.Methanol.getFluid(1000), Materials.Acetone.getFluid(1000)}, new FluidStack[]{ Materials.GasolineRaw.getFluid(20000)}, null, 100, 480);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.GasolineRaw.getFluid(10000), Materials.Toluene.getFluid(1000)}, new FluidStack[]{ Materials.GasolineRegular.getFluid(11000)}, null, 10, 480);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.GasolineRegular.getFluid(20000), Materials.Octane.getFluid(2000), Materials.NitrousOxide.getGas(6000), Materials.Toluene.getFluid(1000), Materials.AntiKnock.getFluid(3000L)},   new FluidStack[]{Materials.GasolinePremium.getFluid(32000L)}, null, 50, 1920);
        GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.Ethanol.getFluid(1000), Materials.Butene.getGas(1000)}, new FluidStack[]{Materials.AntiKnock.getFluid(2000)}, null,400, 480);

        }
	
	public void addPotionRecipes(String aName,ItemStack aItem){
		//normal
		GT_Values.RA.addBrewingRecipe(aItem, FluidRegistry.getFluid("potion.awkward"), FluidRegistry.getFluid("potion."+aName), false);
		//strong
		 GT_Values.RA.addBrewingRecipe(aItem, FluidRegistry.getFluid("potion.thick"), FluidRegistry.getFluid("potion."+aName+".strong"), false);
		//long
		 GT_Values.RA.addBrewingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L), FluidRegistry.getFluid("potion."+aName), FluidRegistry.getFluid("potion."+aName+".long"), false);
		//splash
		 if(!(FluidRegistry.getFluid("potion."+aName)==null||FluidRegistry.getFluid("potion."+aName+".splash")==null))
		 GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gunpowder, 1L), null, null, null, new FluidStack(FluidRegistry.getFluid("potion."+aName),750), new FluidStack(FluidRegistry.getFluid("potion."+aName+".splash"),750), null, 200, 24);
		//splash strong
		 if(!(FluidRegistry.getFluid("potion."+aName+".strong")==null||FluidRegistry.getFluid("potion."+aName+".strong.splash")==null))
		 GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gunpowder, 1L), null, null, null, new FluidStack(FluidRegistry.getFluid("potion."+aName+".strong"),750), new FluidStack(FluidRegistry.getFluid("potion."+aName+".strong.splash"),750), null, 200, 24);
		//splash long
		 if(!(FluidRegistry.getFluid("potion."+aName+".long")==null||FluidRegistry.getFluid("potion."+aName+".long.splash")==null))
		 GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gunpowder, 1L), null, null, null, new FluidStack(FluidRegistry.getFluid("potion."+aName+".long"),750), new FluidStack(FluidRegistry.getFluid("potion."+aName+".long.splash"),750), null, 200, 24);
	}

    /**
     * Adds recipes related to producing Steel in a Primitive Blast Furnace.
 	 * Adds recipes related to roasting sulfuric ores and reducing oxidic ores in the Electric Blast Furnace.
 	 */
        private void addPyrometallurgicalRecipes() {
    	GT_Values.RA.addPrimitiveBlastRecipe(Materials.Iron.getIngots(1), GT_Values.NI, 4, Materials.Steel.getIngots(1), GT_Values.NI, 7200);
    	GT_Values.RA.addPrimitiveBlastRecipe(Materials.Iron.getDust(1), GT_Values.NI, 4, Materials.Steel.getIngots(1), GT_Values.NI, 7200);
    	GT_Values.RA.addPrimitiveBlastRecipe(Materials.Iron.getBlocks(1), GT_Values.NI, 36, Materials.Steel.getIngots(9), GT_Values.NI, 64800);
    	GT_Values.RA.addPrimitiveBlastRecipe(Materials.Steel.getDust(1), GT_Values.NI, 2, Materials.Steel.getIngots(1), GT_Values.NI, 7200);

    	//Roasting

            GT_Values.RA.addBlastRecipe(Materials.Tetrahedrite.getDust(1), GT_Values.NI, Materials.Oxygen.getGas(3000), Materials.SulfurDioxide.getGas(2000), Materials.CupricOxide.getDust(1), Materials.AntimonyTrioxide.getDustTiny(3), 120, 120, 1200);
            GT_Values.RA.addBlastRecipe(Materials.Chalcopyrite.getDust(1), Materials.SiliconDioxide.getDust(1), Materials.Oxygen.getGas(3000), Materials.SulfurDioxide.getGas(2000), Materials.CupricOxide.getDust(1), Materials.Ferrosilite.getDust(1), 120, 120, 1200);

            GT_Values.RA.addBlastRecipe(Materials.Pyrite.getDust(1), GT_Values.NI, Materials.Oxygen.getGas(3000), Materials.SulfurDioxide.getGas(2000), Materials.BandedIron.getDust(1), Materials.Ash.getDustTiny(1), 120, 120, 1200);

            GT_Values.RA.addBlastRecipe(Materials.Pentlandite.getDust(1), GT_Values.NI, Materials.Oxygen.getGas(3000), Materials.SulfurDioxide.getGas(1000), Materials.Garnierite.getDust(1), Materials.Ash.getDustTiny(1), 120, 120, 1200);

            GT_Values.RA.addBlastRecipe(Materials.Sphalerite.getDust(1), GT_Values.NI, Materials.Oxygen.getGas(3000), Materials.SulfurDioxide.getGas(1000), Materials.Zincite.getDust(1), Materials.Ash.getDustTiny(1), 120, 120, 1200);

            GT_Values.RA.addBlastRecipe(Materials.Cobaltite.getDust(1), GT_Values.NI, Materials.Oxygen.getGas(3000), Materials.SulfurDioxide.getGas(1000), Materials.CobaltOxide.getDust(1), Materials.ArsenicTrioxide.getDust(1), 120, 120, 1200);

            GT_Values.RA.addBlastRecipe(Materials.Stibnite.getDust(1), GT_Values.NI, Materials.Oxygen.getGas(3000), Materials.SulfurDioxide.getGas(1500), Materials.AntimonyTrioxide.getDust(1), Materials.Ash.getDustTiny(1), 120, 120, 1200);

            GT_Values.RA.addBlastRecipe(Materials.Galena.getDust(1), GT_Values.NI, Materials.Oxygen.getGas(3000), Materials.SulfurDioxide.getGas(1000), Materials.Massicot.getDust(1), Materials.Ash.getDustTiny(1), 120, 120, 1200);

            //Carbothermic Reduction
            int outputIngotAmount = GT_Mod.gregtechproxy.mMixedOreOnlyYieldsTwoThirdsOfPureOre ? 2 : 3;
            GT_Values.RA.addBlastRecipe(Materials.CupricOxide.getDust(2),         Materials.Carbon.getDust(1),      GT_Values.NF, Materials.CarbonDioxide.getGas(1000),  Materials.Copper.getIngots(outputIngotAmount),   Materials.Ash.getDustTiny(2), 240, 120, 1200);
            GT_Values.RA.addBlastRecipe(Materials.Malachite.getDust(2),           Materials.Carbon.getDust(1),      GT_Values.NF, Materials.CarbonDioxide.getGas(3000),  Materials.Copper.getIngots(outputIngotAmount),   Materials.Ash.getDustTiny(2), 240, 120, 1200);

            GT_Values.RA.addBlastRecipe(Materials.AntimonyTrioxide.getDust(2),    Materials.Carbon.getDust(1),      GT_Values.NF, Materials.CarbonDioxide.getGas(3000),  Materials.Antimony.getIngots(outputIngotAmount), Materials.Ash.getDustTiny(2), 240, 120, 1200);

            GT_Values.RA.addBlastRecipe(Materials.BandedIron.getDust(2),          Materials.Carbon.getDust(1),      GT_Values.NF, Materials.CarbonDioxide.getGas(1000),  Materials.Iron.getIngots(outputIngotAmount),     Materials.Ash.getDustTiny(2), 240, 120, 1200);
            GT_Values.RA.addBlastRecipe(Materials.Magnetite.getDust(2),           Materials.Carbon.getDust(1),      GT_Values.NF, Materials.CarbonDioxide.getGas(1000),  Materials.Iron.getIngots(outputIngotAmount),     Materials.Ash.getDustTiny(2), 240, 120, 1200);
            GT_Values.RA.addBlastRecipe(Materials.YellowLimonite.getDust(2),      Materials.Carbon.getDust(1),      GT_Values.NF, Materials.CarbonDioxide.getGas(1000),  Materials.Iron.getIngots(outputIngotAmount),     Materials.Ash.getDustTiny(2), 240, 120, 1200);
            GT_Values.RA.addBlastRecipe(Materials.BrownLimonite.getDust(2),       Materials.Carbon.getDust(1),      GT_Values.NF, Materials.CarbonDioxide.getGas(1000),  Materials.Iron.getIngots(outputIngotAmount),     Materials.Ash.getDustTiny(2), 240, 120, 1200);
            GT_Values.RA.addBlastRecipe(Materials.BasalticMineralSand.getDust(2), Materials.Carbon.getDust(1),      GT_Values.NF, Materials.CarbonDioxide.getGas(1000),  Materials.Iron.getIngots(outputIngotAmount),     Materials.Ash.getDustTiny(2), 240, 120, 1200);
            GT_Values.RA.addBlastRecipe(Materials.GraniticMineralSand.getDust(2), Materials.Carbon.getDust(1),      GT_Values.NF, Materials.CarbonDioxide.getGas(1000),  Materials.Iron.getIngots(outputIngotAmount),     Materials.Ash.getDustTiny(2), 240, 120, 1200);

            GT_Values.RA.addBlastRecipe(Materials.Cassiterite.getDust(2),         Materials.Carbon.getDust(1),      GT_Values.NF, Materials.CarbonDioxide.getGas(1000),  Materials.Tin.getIngots(outputIngotAmount),      Materials.Ash.getDustTiny(2), 240, 120, 1200);
            GT_Values.RA.addBlastRecipe(Materials.CassiteriteSand.getDust(2),     Materials.Carbon.getDust(1),      GT_Values.NF, Materials.CarbonDioxide.getGas(1000),  Materials.Tin.getIngots(outputIngotAmount),      Materials.Ash.getDustTiny(2), 240, 120, 1200);

            GT_Values.RA.addBlastRecipe(Materials.Garnierite.getDust(2),          Materials.Carbon.getDust(1),      GT_Values.NF, Materials.CarbonDioxide.getGas(1000),  Materials.Nickel.getIngots(outputIngotAmount),   Materials.Ash.getDustTiny(2), 240, 120, 1200);

            GT_Values.RA.addBlastRecipe(Materials.CobaltOxide.getDust(2),         Materials.Carbon.getDust(1),      GT_Values.NF, Materials.CarbonDioxide.getGas(1000),  Materials.Cobalt.getIngots(outputIngotAmount),   Materials.Ash.getDustTiny(2), 240, 120, 1200);

            GT_Values.RA.addBlastRecipe(Materials.ArsenicTrioxide.getDust(2),     Materials.Carbon.getDust(1),      GT_Values.NF, Materials.CarbonDioxide.getGas(1000),  Materials.Arsenic.getIngots(outputIngotAmount),  Materials.Ash.getDustTiny(2), 240, 120, 1200);

            GT_Values.RA.addBlastRecipe(Materials.Massicot.getDust(2),            Materials.Carbon.getDust(1),      GT_Values.NF, Materials.CarbonDioxide.getGas(1000),  Materials.Lead.getIngots(outputIngotAmount),     Materials.Ash.getDustTiny(2), 240, 120, 1200);

            GT_Values.RA.addBlastRecipe(Materials.SiliconDioxide.getDust(1),        Materials.Carbon.getDust(2),      GT_Values.NF, Materials.CarbonMonoxide.getGas(2000), Materials.Silicon.getIngots(1),  Materials.Ash.getDustTiny(1), 240, 120, 1200);

            if (GT_Mod.gregtechproxy.mMixedOreOnlyYieldsTwoThirdsOfPureOre) {
                GT_Values.RA.addBlastRecipe(Materials.CupricOxide.getDust(2),         Materials.Carbon.getDustSmall(4), GT_Values.NF, Materials.CarbonDioxide.getGas(1000),  Materials.Copper.getIngots(outputIngotAmount),   Materials.Ash.getDustTiny(2), 240, 120, 1200);
                GT_Values.RA.addBlastRecipe(Materials.Malachite.getDust(2),           Materials.Carbon.getDustSmall(4), GT_Values.NF, Materials.CarbonDioxide.getGas(3000),  Materials.Copper.getIngots(outputIngotAmount),   Materials.Ash.getDustTiny(2), 240, 120, 1200);
                GT_Values.RA.addBlastRecipe(Materials.AntimonyTrioxide.getDust(2),    Materials.Carbon.getDustSmall(4), GT_Values.NF, Materials.CarbonDioxide.getGas(3000),  Materials.Antimony.getIngots(outputIngotAmount), Materials.Ash.getDustTiny(2), 240, 120, 1200);
                GT_Values.RA.addBlastRecipe(Materials.BandedIron.getDust(2),          Materials.Carbon.getDustSmall(4), GT_Values.NF, Materials.CarbonDioxide.getGas(1000),  Materials.Iron.getIngots(outputIngotAmount),     Materials.Ash.getDustTiny(2), 240, 120, 1200);
                GT_Values.RA.addBlastRecipe(Materials.Magnetite.getDust(2),           Materials.Carbon.getDustSmall(4), GT_Values.NF, Materials.CarbonDioxide.getGas(1000),  Materials.Iron.getIngots(outputIngotAmount),     Materials.Ash.getDustTiny(2), 240, 120, 1200);
                GT_Values.RA.addBlastRecipe(Materials.YellowLimonite.getDust(2),      Materials.Carbon.getDustSmall(4), GT_Values.NF, Materials.CarbonDioxide.getGas(1000),  Materials.Iron.getIngots(outputIngotAmount),     Materials.Ash.getDustTiny(2), 240, 120, 1200);
                GT_Values.RA.addBlastRecipe(Materials.BrownLimonite.getDust(2),       Materials.Carbon.getDustSmall(4), GT_Values.NF, Materials.CarbonDioxide.getGas(1000),  Materials.Iron.getIngots(outputIngotAmount),     Materials.Ash.getDustTiny(2), 240, 120, 1200);
                GT_Values.RA.addBlastRecipe(Materials.BasalticMineralSand.getDust(2), Materials.Carbon.getDustSmall(4), GT_Values.NF, Materials.CarbonDioxide.getGas(1000),  Materials.Iron.getIngots(outputIngotAmount),     Materials.Ash.getDustTiny(2), 240, 120, 1200);
                GT_Values.RA.addBlastRecipe(Materials.GraniticMineralSand.getDust(2), Materials.Carbon.getDustSmall(4), GT_Values.NF, Materials.CarbonDioxide.getGas(1000),  Materials.Iron.getIngots(outputIngotAmount),     Materials.Ash.getDustTiny(2), 240, 120, 1200);
                GT_Values.RA.addBlastRecipe(Materials.Cassiterite.getDust(2),         Materials.Carbon.getDustSmall(4), GT_Values.NF, Materials.CarbonDioxide.getGas(1000),  Materials.Tin.getIngots(outputIngotAmount),      Materials.Ash.getDustTiny(2), 240, 120, 1200);
                GT_Values.RA.addBlastRecipe(Materials.CassiteriteSand.getDust(2),     Materials.Carbon.getDustSmall(4), GT_Values.NF, Materials.CarbonDioxide.getGas(1000),  Materials.Tin.getIngots(outputIngotAmount),      Materials.Ash.getDustTiny(2), 240, 120, 1200);
                GT_Values.RA.addBlastRecipe(Materials.Garnierite.getDust(2),          Materials.Carbon.getDustSmall(4), GT_Values.NF, Materials.CarbonDioxide.getGas(1000),  Materials.Nickel.getIngots(outputIngotAmount),   Materials.Ash.getDustTiny(2), 240, 120, 1200);
                GT_Values.RA.addBlastRecipe(Materials.CobaltOxide.getDust(2),         Materials.Carbon.getDustSmall(4), GT_Values.NF, Materials.CarbonDioxide.getGas(1000),  Materials.Cobalt.getIngots(outputIngotAmount),   Materials.Ash.getDustTiny(2), 240, 120, 1200);
                GT_Values.RA.addBlastRecipe(Materials.ArsenicTrioxide.getDust(2),     Materials.Carbon.getDustSmall(4), GT_Values.NF, Materials.CarbonDioxide.getGas(1000),  Materials.Arsenic.getIngots(outputIngotAmount),  Materials.Ash.getDustTiny(2), 240, 120, 1200);
                GT_Values.RA.addBlastRecipe(Materials.Massicot.getDust(2),            Materials.Carbon.getDustSmall(4), GT_Values.NF, Materials.CarbonDioxide.getGas(1000),  Materials.Lead.getIngots(outputIngotAmount),     Materials.Ash.getDustTiny(2), 240, 120, 1200);
            }
        }
}


