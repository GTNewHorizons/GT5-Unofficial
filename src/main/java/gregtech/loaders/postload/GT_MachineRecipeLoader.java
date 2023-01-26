package gregtech.loaders.postload;

import static gregtech.api.GregTech_API.mGTPlusPlus;
import static gregtech.api.enums.GT_Values.*;
import static gregtech.api.util.GT_ModHandler.getModItem;
import static net.minecraftforge.fluids.FluidRegistry.getFluidStack;

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
import gregtech.loaders.postload.chains.GT_BauxiteRefineChain;
import gregtech.loaders.postload.chains.GT_NaniteChain;
import gregtech.loaders.postload.chains.GT_PCBFactoryRecipes;
import ic2.api.recipe.ILiquidHeatExchangerManager;
import ic2.api.recipe.Recipes;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import javafx.beans.binding.BooleanExpression;
import mods.railcraft.common.blocks.aesthetics.cube.EnumCube;
import mods.railcraft.common.items.RailcraftToolItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class GT_MachineRecipeLoader implements Runnable {

    public static final String aTextAE = "appliedenergistics2";
    public static final String aTextAEMM = "item.ItemMultiMaterial";
    public static final String aTextForestry = "Forestry";
    public static final String aTextEBXL = "ExtrabiomesXL";
    public static final String aTextTCGTPage = "gt.research.page.1.";
    public static final Boolean isNEILoaded = Loader.isModLoaded("NotEnoughItems");
    public static final Boolean isThaumcraftLoaded = Loader.isModLoaded("Thaumcraft");
    public static final Boolean isBartWorksLoaded = Loader.isModLoaded("bartworks");
    public static final Boolean isGTNHLanthanidLoaded = Loader.isModLoaded("gtnhlanth");
    public static final Boolean isGTPPLoaded = Loader.isModLoaded(MOD_ID_GTPP);
    public static final Boolean isGalaxySpaceLoaded = Loader.isModLoaded("GalaxySpace");
    public static final Boolean isGalacticraftMarsLoaded = Loader.isModLoaded("GalacticraftMars");
    public static final Boolean isIronChestLoaded = Loader.isModLoaded("IronChest");
    public static final Boolean isCoremodLoaded = Loader.isModLoaded(MOD_ID_DC);
    public static final Boolean isBuildCraftFactoryLoaded = Loader.isModLoaded("BuildCraft|Factory");
    public static final Boolean isIronTankLoaded = Loader.isModLoaded("irontank");
    public static final Boolean isExtraUtilitiesLoaded = Loader.isModLoaded("ExtraUtilities");
    public static final Boolean isEBXLLoaded = Loader.isModLoaded(GT_MachineRecipeLoader.aTextEBXL);
    public static final Boolean isRailcraftLoaded = Loader.isModLoaded(MOD_ID_RC);

    @Override
    public void run() {
        GT_Log.out.println("GT_Mod: Adding non-OreDict Machine Recipes.");
        try {
            GT_Utility.removeSimpleIC2MachineRecipe(
                    GT_Values.NI,
                    ic2.api.recipe.Recipes.metalformerExtruding.getRecipes(),
                    ItemList.Cell_Empty.get(3L));
            GT_Utility.removeSimpleIC2MachineRecipe(
                    ItemList.IC2_Energium_Dust.get(1L), ic2.api.recipe.Recipes.compressor.getRecipes(), GT_Values.NI);
            GT_Utility.removeSimpleIC2MachineRecipe(
                    new ItemStack(Items.gunpowder), ic2.api.recipe.Recipes.extractor.getRecipes(), GT_Values.NI);
            GT_Utility.removeSimpleIC2MachineRecipe(
                    new ItemStack(Blocks.wool, 1, 32767), ic2.api.recipe.Recipes.extractor.getRecipes(), GT_Values.NI);
            GT_Utility.removeSimpleIC2MachineRecipe(
                    new ItemStack(Blocks.gravel), ic2.api.recipe.Recipes.oreWashing.getRecipes(), GT_Values.NI);
        } catch (Throwable ignored) {
        }
        GT_Utility.removeIC2BottleRecipe(
                GT_ModHandler.getIC2Item("fuelRod", 1),
                GT_ModHandler.getIC2Item("UranFuel", 1),
                ic2.api.recipe.Recipes.cannerBottle.getRecipes(),
                GT_ModHandler.getIC2Item("reactorUraniumSimple", 1, 1));
        GT_Utility.removeIC2BottleRecipe(
                GT_ModHandler.getIC2Item("fuelRod", 1),
                GT_ModHandler.getIC2Item("MOXFuel", 1),
                ic2.api.recipe.Recipes.cannerBottle.getRecipes(),
                GT_ModHandler.getIC2Item("reactorMOXSimple", 1, 1));

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

        GT_Values.RA.addFluidSmelterRecipe(
                new ItemStack(Items.snowball, 1, 0), GT_Values.NI, Materials.Water.getFluid(250L), 10000, 32, 4);
        GT_Values.RA.addFluidSmelterRecipe(
                new ItemStack(Blocks.snow, 1, 0), GT_Values.NI, Materials.Water.getFluid(1000L), 10000, 128, 4);
        GT_Values.RA.addFluidSmelterRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ice, 1L),
                GT_Values.NI,
                Materials.Ice.getSolid(1000L),
                10000,
                128,
                4);
        GT_Values.RA.addFluidSmelterRecipe(
                getModItem(GT_MachineRecipeLoader.aTextForestry, "phosphor", 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Phosphorus, 1L),
                Materials.Lava.getFluid(800L),
                1000,
                256,
                128);

        GT_Values.RA.addSimpleArcFurnaceRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 1L),
                Materials.Oxygen.getGas(2000L),
                new ItemStack[] {GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 3)},
                null,
                1200,
                30);

        this.run2();

        GT_Values.RA.addCutterRecipe(
                new ItemStack[] {ItemList.Circuit_Silicon_Ingot.get(1)},
                new ItemStack[] {
                    ItemList.Circuit_Silicon_Wafer.get(16),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconSG, 4L)
                },
                400,
                30,
                false);

        GT_Values.RA.addCutterRecipe(
                new ItemStack[] {ItemList.Circuit_Silicon_Ingot2.get(1)},
                new ItemStack[] {
                    ItemList.Circuit_Silicon_Wafer2.get(32),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconSG, 8L)
                },
                800,
                120,
                true);

        GT_Values.RA.addCutterRecipe(
                new ItemStack[] {ItemList.Circuit_Silicon_Ingot3.get(1)},
                new ItemStack[] {
                    ItemList.Circuit_Silicon_Wafer3.get(64),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconSG, 16L)
                },
                1600,
                480,
                true);

        GT_Values.RA.addCutterRecipe(
                new ItemStack[] {ItemList.Circuit_Silicon_Ingot4.get(1)},
                new ItemStack[] {
                    ItemList.Circuit_Silicon_Wafer4.get(64),
                    ItemList.Circuit_Silicon_Wafer4.get(32),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconSG, 32L)
                },
                2400,
                1920,
                true);

        GT_Values.RA.addCutterRecipe(
                new ItemStack[] {ItemList.Circuit_Silicon_Ingot5.get(1)},
                new ItemStack[] {
                    ItemList.Circuit_Silicon_Wafer5.get(64),
                    ItemList.Circuit_Silicon_Wafer5.get(64),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconSG, 64L)
                },
                3200,
                7680,
                true);

        GT_Values.RA.addUniversalDistillationRecipewithCircuit(
                Materials.OilLight.getFluid(150),
                new ItemStack[] {GT_Utility.getIntegratedCircuit(1)},
                new FluidStack[] {
                    Materials.SulfuricHeavyFuel.getFluid(10),
                    Materials.SulfuricLightFuel.getFluid(20),
                    Materials.SulfuricNaphtha.getFluid(30),
                    Materials.SulfuricGas.getGas(240)
                },
                null,
                20,
                96);
        GT_Values.RA.addUniversalDistillationRecipewithCircuit(
                Materials.OilMedium.getFluid(100),
                new ItemStack[] {GT_Utility.getIntegratedCircuit(1)},
                new FluidStack[] {
                    Materials.SulfuricHeavyFuel.getFluid(10),
                    Materials.SulfuricLightFuel.getFluid(50),
                    Materials.SulfuricNaphtha.getFluid(150),
                    Materials.SulfuricGas.getGas(60)
                },
                null,
                20,
                96);
        GT_Values.RA.addUniversalDistillationRecipewithCircuit(
                Materials.Oil.getFluid(50L),
                new ItemStack[] {GT_Utility.getIntegratedCircuit(1)},
                new FluidStack[] {
                    Materials.SulfuricHeavyFuel.getFluid(15),
                    Materials.SulfuricLightFuel.getFluid(50),
                    Materials.SulfuricNaphtha.getFluid(20),
                    Materials.SulfuricGas.getGas(60)
                },
                null,
                20,
                96);
        GT_Values.RA.addUniversalDistillationRecipewithCircuit(
                Materials.OilHeavy.getFluid(100),
                new ItemStack[] {GT_Utility.getIntegratedCircuit(1)},
                new FluidStack[] {
                    Materials.SulfuricHeavyFuel.getFluid(250),
                    Materials.SulfuricLightFuel.getFluid(45),
                    Materials.SulfuricNaphtha.getFluid(15),
                    Materials.SulfuricGas.getGas(60)
                },
                null,
                20,
                288);

        if (GregTech_API.sSpecialFile.get("general", "EnableLagencyOilGalactiCraft", false)
                && FluidRegistry.getFluid("oilgc") != null)
            GT_Values.RA.addUniversalDistillationRecipe(
                    new FluidStack(FluidRegistry.getFluid("oilgc"), 50),
                    new FluidStack[] {
                        Materials.SulfuricHeavyFuel.getFluid(15),
                        Materials.SulfuricLightFuel.getFluid(50),
                        Materials.SulfuricNaphtha.getFluid(20),
                        Materials.SulfuricGas.getGas(60)
                    },
                    null,
                    20,
                    96);

        GT_Values.RA.addDistilleryRecipe(
                GT_Utility.getIntegratedCircuit(1),
                new FluidStack(ItemList.sOilExtraHeavy, 10),
                Materials.OilHeavy.getFluid(15),
                16,
                24,
                false);
        GT_Values.RA.addDistilleryRecipe(
                GT_Utility.getIntegratedCircuit(1),
                Materials.HeavyFuel.getFluid(10L),
                new FluidStack(ItemList.sToluene, 4),
                16,
                24,
                false);
        GT_Values.RA.addDistilleryRecipe(
                GT_Utility.getIntegratedCircuit(1),
                new FluidStack(ItemList.sToluene, 30),
                Materials.LightFuel.getFluid(30L),
                16,
                24,
                false);





        GT_Values.RA.addWiremillRecipe(
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Polycaprolactam, 1L),
                new ItemStack(Items.string, 32),
                80,
                48);
        GT_RecipeRegistrator.registerWiremillRecipes(Materials.SpaceTime, 400, 32_000);

        GT_Values.RA.addDistilleryRecipe(
                GT_Utility.getIntegratedCircuit(24),
                Materials.Creosote.getFluid(100L),
                Materials.Lubricant.getFluid(32L),
                240,
                30,
                false);
        GT_Values.RA.addDistilleryRecipe(
                GT_Utility.getIntegratedCircuit(24),
                Materials.SeedOil.getFluid(32L),
                Materials.Lubricant.getFluid(8L),
                80,
                30,
                false);
        GT_Values.RA.addDistilleryRecipe(
                GT_Utility.getIntegratedCircuit(24),
                Materials.FishOil.getFluid(32L),
                Materials.Lubricant.getFluid(8L),
                80,
                30,
                false);
        GT_Values.RA.addDistilleryRecipe(
                GT_Utility.getIntegratedCircuit(24),
                Materials.Oil.getFluid(120L),
                Materials.Lubricant.getFluid(60L),
                160,
                30,
                false);
        GT_Values.RA.addDistilleryRecipe(
                GT_Utility.getIntegratedCircuit(24),
                Materials.OilLight.getFluid(120L),
                Materials.Lubricant.getFluid(30L),
                160,
                30,
                false);
        GT_Values.RA.addDistilleryRecipe(
                GT_Utility.getIntegratedCircuit(24),
                Materials.OilMedium.getFluid(120L),
                Materials.Lubricant.getFluid(60L),
                160,
                30,
                false);
        GT_Values.RA.addDistilleryRecipe(
                GT_Utility.getIntegratedCircuit(24),
                Materials.OilHeavy.getFluid(120L),
                Materials.Lubricant.getFluid(90L),
                160,
                30,
                false);
        GT_Values.RA.addDistilleryRecipe(
                GT_Utility.getIntegratedCircuit(1),
                Materials.Biomass.getFluid(40L),
                Materials.Ethanol.getFluid(12L),
                16,
                24,
                false);
        GT_Values.RA.addDistilleryRecipe(
                GT_Utility.getIntegratedCircuit(5),
                Materials.Biomass.getFluid(40L),
                Materials.Water.getFluid(12L),
                16,
                24,
                false);
        GT_Values.RA.addDistilleryRecipe(
                GT_Utility.getIntegratedCircuit(5),
                Materials.Water.getFluid(5L),
                GT_ModHandler.getDistilledWater(5L),
                16,
                10,
                false);
        GT_Values.RA.addDistilleryRecipe(
                GT_Utility.getIntegratedCircuit(1),
                getFluidStack("potion.potatojuice", 2),
                getFluidStack("potion.vodka", 1),
                16,
                16,
                true);
        GT_Values.RA.addDistilleryRecipe(
                GT_Utility.getIntegratedCircuit(1),
                getFluidStack("potion.lemonade", 2),
                getFluidStack("potion.alcopops", 1),
                16,
                16,
                true);

        GT_Values.RA.addDistilleryRecipe(
                GT_Utility.getIntegratedCircuit(4),
                Materials.OilLight.getFluid(300L),
                Materials.Oil.getFluid(100L),
                16,
                24,
                false);
        GT_Values.RA.addDistilleryRecipe(
                GT_Utility.getIntegratedCircuit(4),
                Materials.OilMedium.getFluid(200L),
                Materials.Oil.getFluid(100L),
                16,
                24,
                false);
        GT_Values.RA.addDistilleryRecipe(
                GT_Utility.getIntegratedCircuit(4),
                Materials.OilHeavy.getFluid(100L),
                Materials.Oil.getFluid(100L),
                16,
                24,
                false);

        if (Loader.isModLoaded("TConstruct")) {
            GT_Values.RA.addDistilleryRecipe(
                    GT_Utility.getIntegratedCircuit(1),
                    Materials.Glue.getFluid(8L),
                    getFluidStack("glue", 8),
                    1,
                    24,
                    false);
            GT_Values.RA.addDistilleryRecipe(
                    GT_Utility.getIntegratedCircuit(1),
                    getFluidStack("glue", 8),
                    Materials.Glue.getFluid(4L),
                    1,
                    24,
                    false);
        }



        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Talc, 1L),
                FluidRegistry.getFluid("oil"),
                FluidRegistry.getFluid("lubricant"),
                false);
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Soapstone, 1L),
                FluidRegistry.getFluid("oil"),
                FluidRegistry.getFluid("lubricant"),
                false);
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L),
                FluidRegistry.getFluid("oil"),
                FluidRegistry.getFluid("lubricant"),
                false);
        GT_Values.RA.addBrewingRecipeCustom(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Talc, 1L),
                getFluidStack("liquid_light_oil", 750),
                getFluidStack("lubricant", 500),
                128,
                4,
                false);

        GT_Values.RA.addBrewingRecipeCustom(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Soapstone, 1L),
                getFluidStack("liquid_light_oil", 750),
                getFluidStack("lubricant", 500),
                128,
                4,
                false);
        GT_Values.RA.addBrewingRecipeCustom(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L),
                getFluidStack("liquid_light_oil", 750),
                getFluidStack("lubricant", 500),
                128,
                4,
                false);
        GT_Values.RA.addBrewingRecipeCustom(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Talc, 1L),
                getFluidStack("liquid_medium_oil", 750),
                getFluidStack("lubricant", 750),
                128,
                4,
                false);

        GT_Values.RA.addBrewingRecipeCustom(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Soapstone, 1L),
                getFluidStack("liquid_medium_oil", 750),
                getFluidStack("lubricant", 750),
                128,
                4,
                false);
        GT_Values.RA.addBrewingRecipeCustom(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L),
                getFluidStack("liquid_medium_oil", 750),
                getFluidStack("lubricant", 750),
                128,
                4,
                false);
        GT_Values.RA.addBrewingRecipeCustom(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Talc, 1L),
                getFluidStack("liquid_heavy_oil", 500),
                getFluidStack("lubricant", 750),
                64,
                4,
                false);

        GT_Values.RA.addBrewingRecipeCustom(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Soapstone, 1L),
                getFluidStack("liquid_heavy_oil", 500),
                getFluidStack("lubricant", 750),
                64,
                4,
                false);
        GT_Values.RA.addBrewingRecipeCustom(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L),
                getFluidStack("liquid_heavy_oil", 500),
                getFluidStack("lubricant", 750),
                64,
                4,
                false);
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Talc, 1L),
                FluidRegistry.getFluid("creosote"),
                FluidRegistry.getFluid("lubricant"),
                false);
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Soapstone, 1L),
                FluidRegistry.getFluid("creosote"),
                FluidRegistry.getFluid("lubricant"),
                false);
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L),
                FluidRegistry.getFluid("creosote"),
                FluidRegistry.getFluid("lubricant"),
                false);
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Talc, 1L),
                FluidRegistry.getFluid("seedoil"),
                FluidRegistry.getFluid("lubricant"),
                false);
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Soapstone, 1L),
                FluidRegistry.getFluid("seedoil"),
                FluidRegistry.getFluid("lubricant"),
                false);
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L),
                FluidRegistry.getFluid("seedoil"),
                FluidRegistry.getFluid("lubricant"),
                false);
        for (Fluid tFluid : new Fluid[] {
            FluidRegistry.WATER, GT_ModHandler.getDistilledWater(1L).getFluid()
        }) {
            GT_Values.RA.addBrewingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Milk, 1L),
                    tFluid,
                    FluidRegistry.getFluid("milk"),
                    false);
            GT_Values.RA.addBrewingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wheat, 1L),
                    tFluid,
                    FluidRegistry.getFluid("potion.wheatyjuice"),
                    false);
            GT_Values.RA.addBrewingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Potassium, 1L),
                    tFluid,
                    FluidRegistry.getFluid("potion.mineralwater"),
                    false);
            GT_Values.RA.addBrewingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L),
                    tFluid,
                    FluidRegistry.getFluid("potion.mineralwater"),
                    false);
            GT_Values.RA.addBrewingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 1L),
                    tFluid,
                    FluidRegistry.getFluid("potion.mineralwater"),
                    false);
            GT_Values.RA.addBrewingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnesium, 1L),
                    tFluid,
                    FluidRegistry.getFluid("potion.mineralwater"),
                    false);
            GT_Values.RA.addBrewingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glowstone, 1L),
                    tFluid,
                    FluidRegistry.getFluid("potion.thick"),
                    false);
            GT_Values.RA.addBrewingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L),
                    tFluid,
                    FluidRegistry.getFluid("potion.mundane"),
                    false);
            GT_Values.RA.addBrewingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L),
                    tFluid,
                    FluidRegistry.getFluid("potion.mundane"),
                    false);
            GT_Values.RA.addBrewingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Blaze, 1L),
                    tFluid,
                    FluidRegistry.getFluid("potion.mundane"),
                    false);
            GT_Values.RA.addBrewingRecipe(
                    new ItemStack(Items.magma_cream, 1, 0), tFluid, FluidRegistry.getFluid("potion.mundane"), false);
            GT_Values.RA.addBrewingRecipe(
                    new ItemStack(Items.fermented_spider_eye, 1, 0),
                    tFluid,
                    FluidRegistry.getFluid("potion.mundane"),
                    false);
            GT_Values.RA.addBrewingRecipe(
                    new ItemStack(Items.spider_eye, 1, 0), tFluid, FluidRegistry.getFluid("potion.mundane"), false);
            GT_Values.RA.addBrewingRecipe(
                    new ItemStack(Items.speckled_melon, 1, 0), tFluid, FluidRegistry.getFluid("potion.mundane"), false);
            GT_Values.RA.addBrewingRecipe(
                    new ItemStack(Items.ghast_tear, 1, 0), tFluid, FluidRegistry.getFluid("potion.mundane"), false);
            GT_Values.RA.addBrewingRecipe(
                    new ItemStack(Items.nether_wart, 1, 0), tFluid, FluidRegistry.getFluid("potion.awkward"), false);
            GT_Values.RA.addBrewingRecipe(
                    new ItemStack(Blocks.red_mushroom, 1, 0), tFluid, FluidRegistry.getFluid("potion.poison"), false);
            GT_Values.RA.addBrewingRecipe(
                    new ItemStack(Items.fish, 1, 3), tFluid, FluidRegistry.getFluid("potion.poison.strong"), true);
            GT_Values.RA.addBrewingRecipe(
                    ItemList.IC2_Grin_Powder.get(1L), tFluid, FluidRegistry.getFluid("potion.poison.strong"), false);
            GT_Values.RA.addBrewingRecipe(
                    new ItemStack(Items.reeds, 1, 0), tFluid, FluidRegistry.getFluid("potion.reedwater"), false);
            GT_Values.RA.addBrewingRecipe(
                    new ItemStack(Items.apple, 1, 0), tFluid, FluidRegistry.getFluid("potion.applejuice"), false);
            GT_Values.RA.addBrewingRecipe(
                    new ItemStack(Items.golden_apple, 1, 0),
                    tFluid,
                    FluidRegistry.getFluid("potion.goldenapplejuice"),
                    true);
            GT_Values.RA.addBrewingRecipe(
                    new ItemStack(Items.golden_apple, 1, 1),
                    tFluid,
                    FluidRegistry.getFluid("potion.idunsapplejuice"),
                    true);
            GT_Values.RA.addBrewingRecipe(
                    ItemList.IC2_Hops.get(1L), tFluid, FluidRegistry.getFluid("potion.hopsjuice"), false);
            GT_Values.RA.addBrewingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coffee, 1L),
                    tFluid,
                    FluidRegistry.getFluid("potion.darkcoffee"),
                    false);
            GT_Values.RA.addBrewingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chili, 1L),
                    tFluid,
                    FluidRegistry.getFluid("potion.chillysauce"),
                    false);


        }
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chili, 1L),
                FluidRegistry.getFluid("potion.chillysauce"),
                FluidRegistry.getFluid("potion.hotsauce"),
                false);
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chili, 1L),
                FluidRegistry.getFluid("potion.hotsauce"),
                FluidRegistry.getFluid("potion.diabolosauce"),
                true);
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chili, 1L),
                FluidRegistry.getFluid("potion.diabolosauce"),
                FluidRegistry.getFluid("potion.diablosauce"),
                true);
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coffee, 1L),
                FluidRegistry.getFluid("milk"),
                FluidRegistry.getFluid("potion.coffee"),
                false);
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Cocoa, 1L),
                FluidRegistry.getFluid("milk"),
                FluidRegistry.getFluid("potion.darkchocolatemilk"),
                false);
        GT_Values.RA.addBrewingRecipe(
                ItemList.IC2_Hops.get(1L),
                FluidRegistry.getFluid("potion.wheatyjuice"),
                FluidRegistry.getFluid("potion.wheatyhopsjuice"),
                false);
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wheat, 1L),
                FluidRegistry.getFluid("potion.hopsjuice"),
                FluidRegistry.getFluid("potion.wheatyhopsjuice"),
                false);
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L),
                FluidRegistry.getFluid("potion.tea"),
                FluidRegistry.getFluid("potion.sweettea"),
                true);
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L),
                FluidRegistry.getFluid("potion.coffee"),
                FluidRegistry.getFluid("potion.cafeaulait"),
                false);
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L),
                FluidRegistry.getFluid("potion.cafeaulait"),
                FluidRegistry.getFluid("potion.laitaucafe"),
                true);
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L),
                FluidRegistry.getFluid("potion.lemonjuice"),
                FluidRegistry.getFluid("potion.lemonade"),
                false);
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L),
                FluidRegistry.getFluid("potion.darkcoffee"),
                FluidRegistry.getFluid("potion.darkcafeaulait"),
                true);
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L),
                FluidRegistry.getFluid("potion.darkchocolatemilk"),
                FluidRegistry.getFluid("potion.chocolatemilk"),
                false);
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ice, 1L),
                FluidRegistry.getFluid("potion.tea"),
                FluidRegistry.getFluid("potion.icetea"),
                false);
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gunpowder, 1L),
                FluidRegistry.getFluid("potion.lemonade"),
                FluidRegistry.getFluid("potion.cavejohnsonsgrenadejuice"),
                true);
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L),
                FluidRegistry.getFluid("potion.mundane"),
                FluidRegistry.getFluid("potion.purpledrink"),
                true);
        GT_Values.RA.addBrewingRecipe(
                new ItemStack(Items.fermented_spider_eye, 1, 0),
                FluidRegistry.getFluid("potion.mundane"),
                FluidRegistry.getFluid("potion.weakness"),
                false);
        GT_Values.RA.addBrewingRecipe(
                new ItemStack(Items.fermented_spider_eye, 1, 0),
                FluidRegistry.getFluid("potion.thick"),
                FluidRegistry.getFluid("potion.weakness"),
                false);

        GT_Values.RA.addBrewingRecipe(
                getModItem(GT_MachineRecipeLoader.aTextForestry, "fertilizerBio", 4L, 0),
                FluidRegistry.WATER,
                FluidRegistry.getFluid("biomass"),
                false);
        GT_Values.RA.addBrewingRecipe(
                getModItem(GT_MachineRecipeLoader.aTextForestry, "mulch", 16L, 0),
                GT_ModHandler.getDistilledWater(750L).getFluid(),
                FluidRegistry.getFluid("biomass"),
                false);
        GT_Values.RA.addBrewingRecipeCustom(
                getModItem(GT_MachineRecipeLoader.aTextForestry, "mulch", 8L, 0),
                getFluidStack("juice", 500),
                getFluidStack("biomass", 750),
                128,
                4,
                false);

        GT_Values.RA.addBrewingRecipeCustom(
                GT_ModHandler.getIC2Item("biochaff", 1),
                GT_ModHandler.getWater(1000L),
                getFluidStack("ic2biomass", 1000),
                170,
                4,
                false);
        GT_Values.RA.addBrewingRecipeCustom(
                GT_ModHandler.getIC2Item("biochaff", 1),
                GT_ModHandler.getDistilledWater(500L),
                getFluidStack("ic2biomass", 1000),
                10,
                30,
                false);

        this.addPotionRecipes("waterbreathing", new ItemStack(Items.fish, 1, 3));
        this.addPotionRecipes("fireresistance", new ItemStack(Items.magma_cream, 1, 0));
        this.addPotionRecipes("nightvision", new ItemStack(Items.golden_carrot, 1, 0));
        this.addPotionRecipes("weakness", new ItemStack(Items.fermented_spider_eye, 1, 0));
        this.addPotionRecipes("poison", new ItemStack(Items.spider_eye, 1, 0));
        this.addPotionRecipes("health", new ItemStack(Items.speckled_melon, 1, 0));
        this.addPotionRecipes("regen", new ItemStack(Items.ghast_tear, 1, 0));
        this.addPotionRecipes("speed", GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L));
        this.addPotionRecipes("strength", GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Blaze, 1L));

        GT_Values.RA.addFermentingRecipe(getFluidStack("milk", 50), getFluidStack("potion.mundane", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.lemonjuice", 50), getFluidStack("potion.limoncello", 25), 1024, true);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.applejuice", 50), getFluidStack("potion.cider", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.goldenapplejuice", 50), getFluidStack("potion.goldencider", 25), 1024, true);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.idunsapplejuice", 50), getFluidStack("potion.notchesbrew", 25), 1024, true);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.reedwater", 50), getFluidStack("potion.rum", 25), 1024, true);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.rum", 50), getFluidStack("potion.piratebrew", 10), 2048, false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.grapejuice", 50), getFluidStack("potion.wine", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.wine", 50), getFluidStack("potion.vinegar", 10), 2048, true);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.wheatyjuice", 50), getFluidStack("potion.scotch", 25), 1024, true);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.scotch", 50), getFluidStack("potion.glenmckenner", 10), 2048, true);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.wheatyhopsjuice", 50), getFluidStack("potion.beer", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.hopsjuice", 50), getFluidStack("potion.darkbeer", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.darkbeer", 50), getFluidStack("potion.dragonblood", 10), 2048, true);

        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.beer", 75), getFluidStack("potion.vinegar", 50), 2048, false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.cider", 75), getFluidStack("potion.vinegar", 50), 2048, false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.goldencider", 75), getFluidStack("potion.vinegar", 50), 2048, true);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.rum", 75), getFluidStack("potion.vinegar", 50), 2048, false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.wine", 75), getFluidStack("potion.vinegar", 50), 2048, false);

        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.awkward", 50), getFluidStack("potion.weakness", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.mundane", 50), getFluidStack("potion.weakness", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.thick", 50), getFluidStack("potion.weakness", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.poison", 50), getFluidStack("potion.damage", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.health", 50), getFluidStack("potion.damage", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.waterbreathing", 50), getFluidStack("potion.damage", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.nightvision", 50), getFluidStack("potion.invisibility", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.fireresistance", 50), getFluidStack("potion.slowness", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.speed", 50), getFluidStack("potion.slowness", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.strength", 50), getFluidStack("potion.weakness", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.regen", 50), getFluidStack("potion.poison", 25), 1024, false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.poison.strong", 50), getFluidStack("potion.damage.strong", 10), 2048, false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.health.strong", 50), getFluidStack("potion.damage.strong", 10), 2048, false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.speed.strong", 50), getFluidStack("potion.slowness.strong", 10), 2048, false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.strength.strong", 50), getFluidStack("potion.weakness.strong", 10), 2048, false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.nightvision.long", 50),
                getFluidStack("potion.invisibility.long", 10),
                2048,
                false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.regen.strong", 50), getFluidStack("potion.poison.strong", 10), 2048, false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.poison.long", 50), getFluidStack("potion.damage.long", 10), 2048, false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.waterbreathing.long", 50), getFluidStack("potion.damage.long", 10), 2048, false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.fireresistance.long", 50),
                getFluidStack("potion.slowness.long", 10),
                2048,
                false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.speed.long", 50), getFluidStack("potion.slowness.long", 10), 2048, false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.strength.long", 50), getFluidStack("potion.weakness.long", 10), 2048, false);
        GT_Values.RA.addFermentingRecipe(
                getFluidStack("potion.regen.long", 50), getFluidStack("potion.poison.long", 10), 2048, false);

        GT_ModHandler.addSmeltingRecipe(ItemList.Food_Raw_PotatoChips.get(1L), ItemList.Food_PotatoChips.get(1L));
        GT_ModHandler.addSmeltingRecipe(
                ItemList.Food_Potato_On_Stick.get(1L), ItemList.Food_Potato_On_Stick_Roasted.get(1L));
        GT_ModHandler.addSmeltingRecipe(ItemList.Food_Raw_Bun.get(1L), ItemList.Food_Baked_Bun.get(1L));
        GT_ModHandler.addSmeltingRecipe(ItemList.Food_Raw_Bread.get(1L), ItemList.Food_Baked_Bread.get(1L));
        GT_ModHandler.addSmeltingRecipe(ItemList.Food_Raw_Baguette.get(1L), ItemList.Food_Baked_Baguette.get(1L));
        GT_ModHandler.addSmeltingRecipe(
                ItemList.Food_Raw_Pizza_Veggie.get(1L), ItemList.Food_Baked_Pizza_Veggie.get(1L));
        GT_ModHandler.addSmeltingRecipe(
                ItemList.Food_Raw_Pizza_Cheese.get(1L), ItemList.Food_Baked_Pizza_Cheese.get(1L));
        GT_ModHandler.addSmeltingRecipe(ItemList.Food_Raw_Pizza_Meat.get(1L), ItemList.Food_Baked_Pizza_Meat.get(1L));
        GT_ModHandler.addSmeltingRecipe(ItemList.Food_Raw_Baguette.get(1L), ItemList.Food_Baked_Baguette.get(1L));
        GT_ModHandler.addSmeltingRecipe(ItemList.Food_Raw_Cake.get(1L), ItemList.Food_Baked_Cake.get(1L));
        GT_ModHandler.addSmeltingRecipe(ItemList.Food_Raw_Cookie.get(1L), new ItemStack(Items.cookie, 1));
        GT_ModHandler.addSmeltingRecipe(new ItemStack(Items.slime_ball, 1), ItemList.IC2_Resin.get(1L));

        GT_ModHandler.addExtractionRecipe(new ItemStack(Blocks.bookshelf, 1, 32767), new ItemStack(Items.book, 3, 0));
        GT_ModHandler.addExtractionRecipe(
                new ItemStack(Items.slime_ball, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RawRubber, 2L));
        GT_ModHandler.addExtractionRecipe(
                ItemList.IC2_Resin.get(1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RawRubber, 3L));
        GT_ModHandler.addExtractionRecipe(
                GT_ModHandler.getIC2Item("rubberSapling", 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RawRubber, 1L));
        GT_ModHandler.addExtractionRecipe(
                GT_ModHandler.getIC2Item("rubberLeaves", 16L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RawRubber, 1L));
        GT_ModHandler.addExtractionRecipe(ItemList.Cell_Air.get(1L), ItemList.Cell_Empty.get(1L));
        if (isEBXLLoaded) {
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "waterplant1", 1, 0), new ItemStack(Items.dye, 4, 2));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "vines", 1, 0), new ItemStack(Items.dye, 4, 1));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower3", 1, 11), new ItemStack(Items.dye, 4, 11));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower3", 1, 10), new ItemStack(Items.dye, 4, 5));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower3", 1, 9), new ItemStack(Items.dye, 4, 14));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower3", 1, 8), new ItemStack(Items.dye, 4, 14));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower3", 1, 7), new ItemStack(Items.dye, 4, 1));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower3", 1, 6), new ItemStack(Items.dye, 4, 1));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower3", 1, 5), new ItemStack(Items.dye, 4, 11));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower3", 1, 0), new ItemStack(Items.dye, 4, 9));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower3", 1, 4), new ItemStack(Items.dye, 4, 11));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower3", 1, 3), new ItemStack(Items.dye, 4, 13));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower1", 1, 3), new ItemStack(Items.dye, 4, 5));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower3", 1, 2), new ItemStack(Items.dye, 4, 5));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower1", 1, 1), new ItemStack(Items.dye, 4, 12));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower2", 1, 15), new ItemStack(Items.dye, 4, 11));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower2", 1, 14), new ItemStack(Items.dye, 4, 1));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower2", 1, 13), new ItemStack(Items.dye, 4, 9));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower2", 1, 12), new ItemStack(Items.dye, 4, 14));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower2", 1, 11), new ItemStack(Items.dye, 4, 7));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower1", 1, 7), new ItemStack(Items.dye, 4, 7));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower1", 1, 2), new ItemStack(Items.dye, 4, 11));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower3", 1, 13), new ItemStack(Items.dye, 4, 6));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower2", 1, 6), new ItemStack(Items.dye, 4, 12));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower2", 1, 5), new ItemStack(Items.dye, 4, 10));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower2", 1, 2), new ItemStack(Items.dye, 4, 1));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower2", 1, 1), new ItemStack(Items.dye, 4, 9));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower2", 1, 0), new ItemStack(Items.dye, 4, 13));

            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower2", 1, 7),
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "extrabiomes.dye", 1, 0));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower3", 1, 1),
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "extrabiomes.dye", 1, 1));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower3", 1, 12),
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "extrabiomes.dye", 1, 1));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower2", 1, 4),
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "extrabiomes.dye", 1, 1));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower1", 1, 6),
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "extrabiomes.dye", 1, 2));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower2", 1, 8),
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "extrabiomes.dye", 1, 3));
            GT_ModHandler.addExtractionRecipe(
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "flower2", 1, 3),
                    getModItem(GT_MachineRecipeLoader.aTextEBXL, "extrabiomes.dye", 1, 3));

        }

        GT_Values.RA.addCutterRecipe(
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Graphite, 1L),
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Graphite, 9L),
                GT_Values.NI,
                500,
                48);
        GT_ModHandler.removeFurnaceSmelting(GT_OreDictUnificator.get(OrePrefixes.ore, Materials.Graphite, 1L));
        GT_ModHandler.addSmeltingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.ore, Materials.Graphite, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Graphite, 1L));
        GT_ModHandler.removeFurnaceSmelting(
                GT_OreDictUnificator.get(OrePrefixes.oreBlackgranite, Materials.Graphite, 1L));
        GT_ModHandler.addSmeltingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.oreBlackgranite, Materials.Graphite, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Graphite, 1L));
        GT_ModHandler.removeFurnaceSmelting(GT_OreDictUnificator.get(OrePrefixes.oreEndstone, Materials.Graphite, 1L));
        GT_ModHandler.addSmeltingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.oreEndstone, Materials.Graphite, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Graphite, 1L));
        GT_ModHandler.removeFurnaceSmelting(
                GT_OreDictUnificator.get(OrePrefixes.oreNetherrack, Materials.Graphite, 1L));
        GT_ModHandler.addSmeltingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.oreNetherrack, Materials.Graphite, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Graphite, 1L));
        GT_ModHandler.addSmeltingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Iron, 1L),
                GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.WroughtIron, 1L));
        GT_ModHandler.removeFurnaceSmelting(
                GT_OreDictUnificator.get(OrePrefixes.oreRedgranite, Materials.Graphite, 1L));
        GT_ModHandler.addSmeltingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.oreRedgranite, Materials.Graphite, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Graphite, 1L));

        GT_ModHandler.addPulverisationRecipe(
                getModItem(GT_MachineRecipeLoader.aTextAE, "tile.BlockSkyStone", 1L, 32767),
                getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 1L, 45),
                GT_Values.NI,
                0,
                false);
        GT_ModHandler.addPulverisationRecipe(
                getModItem(GT_MachineRecipeLoader.aTextAE, "tile.BlockSkyChest", 1L, 32767),
                getModItem(GT_MachineRecipeLoader.aTextAE, GT_MachineRecipeLoader.aTextAEMM, 8L, 45),
                GT_Values.NI,
                0,
                false);
        GT_ModHandler.addPulverisationRecipe(
                new ItemStack(Items.blaze_rod, 1),
                new ItemStack(Items.blaze_powder, 3),
                new ItemStack(Items.blaze_powder, 1),
                50,
                false);
        GT_ModHandler.addPulverisationRecipe(
                new ItemStack(Blocks.web, 1, 0),
                new ItemStack(Items.string, 1),
                new ItemStack(Items.string, 1),
                50,
                false);
        GT_ModHandler.addPulverisationRecipe(
                new ItemStack(Blocks.red_mushroom, 1, 32767), ItemList.IC2_Grin_Powder.get(1L));
        GT_ModHandler.addPulverisationRecipe(
                new ItemStack(Items.item_frame, 1, 32767),
                new ItemStack(Items.leather, 1),
                GT_OreDictUnificator.getDust(Materials.Wood, OrePrefixes.stick.mMaterialAmount * 4L),
                95,
                false);
        GT_ModHandler.addPulverisationRecipe(
                new ItemStack(Items.bow, 1, 0),
                new ItemStack(Items.string, 3),
                GT_OreDictUnificator.getDust(Materials.Wood, OrePrefixes.stick.mMaterialAmount * 3L),
                95,
                false);
        GT_ModHandler.addPulverisationRecipe(Materials.Brick.getIngots(1), Materials.Brick.getDustSmall(1));
        GT_ModHandler.addPulverisationRecipe(new ItemStack(Blocks.brick_stairs, 1, 0), Materials.Brick.getDustSmall(6));
        GT_ModHandler.addPulverisationRecipe(ItemList.CompressedFireclay.get(1), Materials.Fireclay.getDustSmall(1));
        GT_ModHandler.addPulverisationRecipe(ItemList.Firebrick.get(1), Materials.Brick.getDust(1));
        GT_ModHandler.addPulverisationRecipe(ItemList.Casing_Firebricks.get(1), Materials.Brick.getDust(4));
        GT_ModHandler.addPulverisationRecipe(
                ItemList.Machine_Bricked_BlastFurnace.get(1),
                Materials.Brick.getDust(8),
                Materials.Iron.getDust(1),
                true);

        GT_Values.RA.addSifterRecipe(
                new ItemStack(Blocks.gravel, 1, 0),
                new ItemStack[] {
                    new ItemStack(Items.flint, 1, 0),
                    new ItemStack(Items.flint, 1, 0),
                    new ItemStack(Items.flint, 1, 0),
                    new ItemStack(Items.flint, 1, 0),
                    new ItemStack(Items.flint, 1, 0),
                    new ItemStack(Items.flint, 1, 0)
                },
                new int[] {10000, 9000, 8000, 6000, 3300, 2500},
                600,
                16);
        GT_Values.RA.addSifterRecipe(
                GT_OreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Coal, 1L),
                new ItemStack[] {
                    new ItemStack(Items.coal, 1, 0),
                    new ItemStack(Items.coal, 1, 0),
                    new ItemStack(Items.coal, 1, 0),
                    new ItemStack(Items.coal, 1, 0),
                    new ItemStack(Items.coal, 1, 0),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 1L)
                },
                new int[] {10000, 9000, 8000, 7000, 6000, 5000},
                600,
                16);

        GT_Values.RA.addForgeHammerRecipe(
                new ItemStack(Blocks.stonebrick, 1, 0), new ItemStack(Blocks.stonebrick, 1, 2), 10, 16);
        GT_Values.RA.addForgeHammerRecipe(
                new ItemStack(Blocks.stone, 1, 0), new ItemStack(Blocks.cobblestone, 1, 0), 10, 16);
        GT_Values.RA.addForgeHammerRecipe(
                new ItemStack(Blocks.cobblestone, 1, 0), new ItemStack(Blocks.gravel, 1, 0), 10, 16);
        GT_Values.RA.addForgeHammerRecipe(new ItemStack(Blocks.gravel, 1, 0), new ItemStack(Blocks.sand, 1, 0), 10, 16);
        GT_Values.RA.addForgeHammerRecipe(
                new ItemStack(Blocks.sandstone, 1, 32767), new ItemStack(Blocks.sand, 1, 0), 10, 16);
        GT_Values.RA.addForgeHammerRecipe(
                new ItemStack(Blocks.ice, 1, 0), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ice, 1L), 10, 16);
        GT_Values.RA.addForgeHammerRecipe(
                new ItemStack(Blocks.packed_ice, 1, 0),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ice, 2L),
                10,
                16);
        GT_Values.RA.addForgeHammerRecipe(
                new ItemStack(Blocks.brick_block, 1, 0), new ItemStack(Items.brick, 3, 0), 10, 16);
        GT_Values.RA.addForgeHammerRecipe(
                new ItemStack(Blocks.nether_brick, 1, 0), new ItemStack(Items.netherbrick, 3, 0), 10, 16);
        GT_Values.RA.addForgeHammerRecipe(
                new ItemStack(Blocks.stained_glass, 1, 32767),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glass, 1L),
                10,
                16);
        GT_Values.RA.addForgeHammerRecipe(
                new ItemStack(Blocks.glass, 1, 32767),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glass, 1L),
                10,
                10);
        GT_Values.RA.addForgeHammerRecipe(
                new ItemStack(Blocks.stained_glass_pane, 1, 32767),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3L),
                10,
                16);
        GT_Values.RA.addForgeHammerRecipe(
                new ItemStack(Blocks.glass_pane, 1, 32767),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3L),
                10,
                16);
        GT_Values.RA.addForgeHammerRecipe(Materials.Brick.getIngots(1), Materials.Brick.getDustSmall(1), 10, 16);
        GT_Values.RA.addForgeHammerRecipe(ItemList.Firebrick.get(1), Materials.Brick.getDust(1), 10, 16);
        GT_Values.RA.addForgeHammerRecipe(ItemList.Casing_Firebricks.get(1), ItemList.Firebrick.get(3), 10, 16);

        GT_Values.RA.addForgeHammerRecipe(
                new ItemStack[] {ItemList.Tesseract.get(1L), getModItem(MOD_ID_GTPP, "MU-metaitem.01", 1, 32105)},
                new FluidStack[] {Materials.SpaceTime.getMolten(2880L)},
                null,
                new FluidStack[] {Materials.Space.getMolten(1440L), Materials.Time.getMolten(1440L)},
                10 * 20,
                (int) Tier.RECIPE_UXV);

        if (Loader.isModLoaded("HardcoreEnderExpansion")) {
            GT_Values.RA.addForgeHammerRecipe(
                    getModItem("HardcoreEnderExpansion", "endium_ore", 1),
                    GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.HeeEndium, 1),
                    16,
                    10);
            GT_ModHandler.addPulverisationRecipe(
                    getModItem("HardcoreEnderExpansion", "endium_ore", 1),
                    GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.HeeEndium, 2),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Endstone, 1),
                    50,
                    GT_Values.NI,
                    0,
                    true);
            GT_OreDictUnificator.set(
                    OrePrefixes.ingot,
                    Materials.HeeEndium,
                    getModItem("HardcoreEnderExpansion", "endium_ingot", 1),
                    true,
                    true);
        }

        // Giga chad trophy.
        GT_Values.RA.addPlasmaForgeRecipe(
                new ItemStack[] {
                    ItemList.Field_Generator_UEV.get(64),
                    ItemList.Field_Generator_UIV.get(64),
                    ItemList.Field_Generator_UMV.get(64)
                },
                new FluidStack[] {
                    Materials.ExcitedDTEC.getFluid(100_000_000), Materials.SpaceTime.getMolten(64 * 2 * 9 * 144)
                },
                new ItemStack[] {ItemList.GigaChad.get(1)},
                new FluidStack[] {GT_Values.NF},
                86400 * 20 * 2,
                2_000_000_000,
                13500);

        // Quantum anomaly recipe bypass for UXV. Avoids RNG.
        GT_Values.RA.addPlasmaForgeRecipe(
                new ItemStack[] {
                    getModItem(MOD_ID_DC, "item.ChromaticLens", 1), getModItem("GoodGenerator", "huiCircuit", 1, 4)
                },
                new FluidStack[] {
                    Materials.WhiteDwarfMatter.getMolten(144),
                    getFluidStack("molten.shirabon", 72),
                    Materials.BlackDwarfMatter.getMolten(144)
                },
                new ItemStack[] {getModItem(MOD_ID_GTPP, "MU-metaitem.01", 1, 32105)},
                new FluidStack[] {NF},
                50 * 20,
                (int) Tier.UXV,
                13_500);

        GT_Values.RA.addAmplifier(ItemList.IC2_Scrap.get(9L), 180, 1);
        GT_Values.RA.addAmplifier(ItemList.IC2_Scrapbox.get(1L), 180, 1);

        GT_Values.RA.addBoxingRecipe(
                ItemList.IC2_Scrap.get(9L), ItemList.Schematic_3by3.get(0L), ItemList.IC2_Scrapbox.get(1L), 16, 1);
        GT_Values.RA.addBoxingRecipe(
                ItemList.Food_Fries.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Paper, 1L),
                ItemList.Food_Packaged_Fries.get(1L),
                64,
                16);
        GT_Values.RA.addBoxingRecipe(
                ItemList.Food_PotatoChips.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 1L),
                ItemList.Food_Packaged_PotatoChips.get(1L),
                64,
                16);
        GT_Values.RA.addBoxingRecipe(
                ItemList.Food_ChiliChips.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 1L),
                ItemList.Food_Packaged_ChiliChips.get(1L),
                64,
                16);

        // fuel rod canner recipes
        GT_Values.RA.addCannerRecipe(
                GT_ModHandler.getIC2Item("fuelRod", 1),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Lithium, 1L),
                GT_ModHandler.getIC2Item("reactorLithiumCell", 1, 1),
                null,
                16,
                64);

        GT_Values.RA.addCannerRecipe(
                GT_ModHandler.getIC2Item("fuelRod", 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Thorium, 3),
                ItemList.ThoriumCell_1.get(1L),
                null,
                30,
                16);
        GT_Values.RA.addCannerRecipe(
                ItemList.Large_Fluid_Cell_TungstenSteel.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.NaquadahEnriched, 3),
                ItemList.NaquadahCell_1.get(1L),
                null,
                30,
                16);
        GT_Values.RA.addCannerRecipe(
                ItemList.Large_Fluid_Cell_TungstenSteel.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Naquadria, 3),
                ItemList.MNqCell_1.get(1L),
                null,
                30,
                16);
        GT_Values.RA.addCannerRecipe(
                GT_ModHandler.getIC2Item("fuelRod", 1),
                GT_ModHandler.getIC2Item("UranFuel", 1),
                ItemList.Uraniumcell_1.get(1),
                null,
                30,
                16);
        GT_Values.RA.addCannerRecipe(
                GT_ModHandler.getIC2Item("fuelRod", 1),
                GT_ModHandler.getIC2Item("MOXFuel", 1),
                ItemList.Moxcell_1.get(1),
                null,
                30,
                16);


        RA.addThermalCentrifugeRecipe(
                ItemList.SunnariumCell.get(1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sunnarium, 1L),
                new ItemStack(Items.glowstone_dust, 2),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 1L),
                500,
                48);
        // Fusion tiering -T1 32768EU/t -T2 65536EU/t - T3 131073EU/t
        // Fusion with margin 32700         65450          131000
        // Startup  max       160M EU       320M EU        640M EU
        // Fluid input,Fluid input,Fluid output,ticks,EU/t,Startup
        // FT1, FT2, FT3 - fusion tier required, + - requires different startup recipe (startup cost bigger than
        // available on the tier)
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Lithium.getMolten(16),
                Materials.Tungsten.getMolten(16),
                Materials.Iridium.getMolten(16),
                64,
                32700,
                300000000); // FT1+ - utility
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Deuterium.getGas(125),
                Materials.Tritium.getGas(125),
                Materials.Helium.getPlasma(125),
                16,
                4096,
                40000000); // FT1 Cheap - farmable
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Deuterium.getGas(125),
                Materials.Helium_3.getGas(125),
                Materials.Helium.getPlasma(125),
                16,
                2048,
                60000000); // FT1 Expensive //
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Carbon.getMolten(125),
                Materials.Helium_3.getGas(125),
                Materials.Oxygen.getPlasma(125),
                32,
                4096,
                80000000); // FT1 Expensive //
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Aluminium.getMolten(16),
                Materials.Lithium.getMolten(16),
                Materials.Sulfur.getPlasma(144),
                32,
                10240,
                240000000); // FT1+ Cheap
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Beryllium.getMolten(16),
                Materials.Deuterium.getGas(375),
                Materials.Nitrogen.getPlasma(125),
                16,
                16384,
                180000000); // FT1+ Expensive //
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Silicon.getMolten(16),
                Materials.Magnesium.getMolten(16),
                Materials.Iron.getPlasma(144),
                32,
                8192,
                360000000); // FT1++ Cheap //
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Potassium.getMolten(16),
                Materials.Fluorine.getGas(144),
                Materials.Nickel.getPlasma(144),
                16,
                32700,
                480000000); // FT1++ Expensive //
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Beryllium.getMolten(16),
                Materials.Tungsten.getMolten(16),
                Materials.Platinum.getMolten(16),
                32,
                32700,
                150000000); // FT1 - utility
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Neodymium.getMolten(16),
                Materials.Hydrogen.getGas(48),
                Materials.Europium.getMolten(16),
                32,
                24576,
                150000000); // FT1 - utility
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Lutetium.getMolten(16),
                Materials.Chrome.getMolten(16),
                Materials.Americium.getMolten(16),
                96,
                49152,
                200000000); // FT2 - utility
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Plutonium.getMolten(16),
                Materials.Thorium.getMolten(16),
                Materials.Naquadah.getMolten(16),
                64,
                32700,
                300000000); // FT1+ - utility
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Americium.getMolten(144),
                Materials.Naquadria.getMolten(144),
                Materials.Neutronium.getMolten(144),
                240,
                122880,
                640000000); // FT3 - utility
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Glowstone.getMolten(16),
                Materials.Helium.getPlasma(4),
                Materials.Sunnarium.getMolten(16),
                32,
                7680,
                40000000); // Mark 1 Expensive //

        GT_Values.RA.addFusionReactorRecipe(
                Materials.Tungsten.getMolten(16),
                Materials.Helium.getGas(16),
                Materials.Osmium.getMolten(16),
                256,
                24578,
                150000000); // FT1 - utility
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Manganese.getMolten(16),
                Materials.Hydrogen.getGas(16),
                Materials.Iron.getMolten(16),
                64,
                8192,
                120000000); // FT1 - utility
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Magnesium.getMolten(128),
                Materials.Oxygen.getGas(128),
                Materials.Calcium.getPlasma(16),
                128,
                8192,
                120000000); //
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Mercury.getFluid(16),
                Materials.Magnesium.getMolten(16),
                Materials.Uranium.getMolten(16),
                64,
                49152,
                240000000); // FT2 - utility
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Gold.getMolten(16),
                Materials.Aluminium.getMolten(16),
                Materials.Uranium.getMolten(16),
                64,
                49152,
                240000000); // FT2 - utility
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Uranium.getMolten(16),
                Materials.Helium.getGas(16),
                Materials.Plutonium.getMolten(16),
                128,
                49152,
                480000000); // FT2+ - utility
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Vanadium.getMolten(16),
                Materials.Hydrogen.getGas(125),
                Materials.Chrome.getMolten(16),
                64,
                24576,
                140000000); // FT1 - utility

        GT_Values.RA.addFusionReactorRecipe(
                Materials.Gallium.getMolten(16),
                Materials.Radon.getGas(125),
                Materials.Duranium.getMolten(16),
                64,
                16384,
                140000000);
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Titanium.getMolten(48),
                Materials.Duranium.getMolten(32),
                Materials.Tritanium.getMolten(16),
                64,
                32700,
                200000000);
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Tantalum.getMolten(16),
                Materials.Tritium.getGas(16),
                Materials.Tungsten.getMolten(16),
                16,
                24576,
                200000000); //
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Silver.getMolten(16),
                Materials.Lithium.getMolten(16),
                Materials.Indium.getMolten(16),
                32,
                24576,
                380000000); //

        // NEW RECIPES FOR FUSION
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Magnesium.getMolten(144),
                Materials.Carbon.getMolten(144),
                Materials.Argon.getPlasma(125),
                32,
                24576,
                180000000); // FT1+ - utility

        GT_Values.RA.addFusionReactorRecipe(
                Materials.Copper.getMolten(72),
                Materials.Tritium.getGas(250),
                Materials.Zinc.getPlasma(72),
                16,
                49152,
                180000000); // FT2 - farmable
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Cobalt.getMolten(144),
                Materials.Silicon.getMolten(144),
                Materials.Niobium.getPlasma(144),
                16,
                49152,
                200000000); // FT2 - utility
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Gold.getMolten(144),
                Materials.Arsenic.getMolten(144),
                Materials.Silver.getPlasma(144),
                16,
                49152,
                350000000); // FT2+
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Silver.getMolten(144),
                Materials.Helium_3.getGas(375),
                Materials.Tin.getPlasma(144),
                16,
                49152,
                280000000); // FT2
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Tungsten.getMolten(144),
                Materials.Carbon.getMolten(144),
                Materials.Mercury.getPlasma(144),
                16,
                49152,
                300000000); // FT2

        GT_Values.RA.addFusionReactorRecipe(
                Materials.Tantalum.getMolten(144),
                Materials.Zinc.getPlasma(72),
                Materials.Bismuth.getPlasma(144),
                16,
                98304,
                350000000); // FT3 - farmable
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Caesium.getMolten(144),
                Materials.Carbon.getMolten(144),
                Materials.Promethium.getMolten(144),
                64,
                49152,
                400000000); // FT3
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Iridium.getMolten(144),
                Materials.Fluorine.getGas(500),
                Materials.Radon.getPlasma(144),
                32,
                98304,
                450000000); // FT3 - utility
        GT_Values.RA.addFusionReactorRecipe(
                Materials.Plutonium241.getMolten(144),
                Materials.Hydrogen.getGas(2000),
                Materials.Americium.getPlasma(144),
                64,
                98304,
                500000000); // FT3
        // GT_Values.RA.addFusionReactorRecipe(Materials.Neutronium.getMolten(144), Materials.Neutronium.getMolten(144),
        // Materials.Neutronium.getPlasma(72), 64, 130000, 640000000);//FT3+ - yes it is a bit troll XD

        GT_ModHandler.removeRecipeByOutput(ItemList.IC2_Fertilizer.get(1L));
        GT_Values.RA.addImplosionRecipe(
                ItemList.IC2_Compressed_Coal_Chunk.get(1L),
                8,
                ItemList.IC2_Industrial_Diamond.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 4L));

        GT_Values.RA.addImplosionRecipe(
                ItemList.Ingot_IridiumAlloy.get(1L),
                8,
                GT_OreDictUnificator.get(OrePrefixes.plateAlloy, Materials.Iridium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 4L));

        if (isGalacticraftMarsLoaded) {

            GT_Values.RA.addImplosionRecipe(
                    ItemList.Ingot_Heavy1.get(1L),
                    8,
                    getModItem("GalacticraftCore", "item.heavyPlating", 1L),
                    GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.StainlessSteel, 1L));
            GT_Values.RA.addImplosionRecipe(
                    ItemList.Ingot_Heavy2.get(1L),
                    16,
                    getModItem("GalacticraftMars", "item.null", 1L, 3),
                    GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.TungstenSteel, 2L));
            GT_Values.RA.addImplosionRecipe(
                    ItemList.Ingot_Heavy3.get(1L),
                    24,
                    getModItem("GalacticraftMars", "item.itemBasicAsteroids", 1L),
                    GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Platinum, 3L));
        }


        GT_Values.RA.addDistillationTowerRecipe(
                Materials.Creosote.getFluid(1000L),
                new ItemStack[] {GT_Utility.getIntegratedCircuit(2)},
                new FluidStack[] {Materials.Lubricant.getFluid(500L)},
                null,
                400,
                120);
        GT_Values.RA.addDistillationTowerRecipe(
                Materials.SeedOil.getFluid(1400L),
                new ItemStack[] {GT_Utility.getIntegratedCircuit(2)},
                new FluidStack[] {Materials.Lubricant.getFluid(500L)},
                null,
                400,
                120);
        GT_Values.RA.addDistillationTowerRecipe(
                Materials.FishOil.getFluid(1200L),
                new ItemStack[] {GT_Utility.getIntegratedCircuit(2)},
                new FluidStack[] {Materials.Lubricant.getFluid(500L)},
                null,
                400,
                120);
        GT_Values.RA.addDistillationTowerRecipe(
                Materials.Biomass.getFluid(1000L),
                new FluidStack[] {Materials.Ethanol.getFluid(600L), Materials.Water.getFluid(300L)},
                GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Wood, 2L),
                32,
                400);
        GT_Values.RA.addDistillationTowerRecipe(
                Materials.Water.getFluid(1000L),
                new FluidStack[] {GT_ModHandler.getDistilledWater(1000L)},
                null,
                32,
                120);
        GT_Values.RA.addDistillationTowerRecipe(
                Materials.OilLight.getFluid(1000L),
                new ItemStack[] {GT_Utility.getIntegratedCircuit(2)},
                new FluidStack[] {Materials.Lubricant.getFluid(250L)},
                null,
                400,
                120);
        GT_Values.RA.addDistillationTowerRecipe(
                Materials.OilMedium.getFluid(1000L),
                new ItemStack[] {GT_Utility.getIntegratedCircuit(2)},
                new FluidStack[] {Materials.Lubricant.getFluid(500L)},
                null,
                400,
                120);
        GT_Values.RA.addDistillationTowerRecipe(
                Materials.OilHeavy.getFluid(1000L),
                new ItemStack[] {GT_Utility.getIntegratedCircuit(2)},
                new FluidStack[] {Materials.Lubricant.getFluid(750L)},
                null,
                400,
                120);

        if (!GregTech_API.mIC2Classic) {
            GT_Values.RA.addDistillationTowerRecipe(
                    new FluidStack(FluidRegistry.getFluid("ic2biomass"), 3000),
                    new FluidStack[] {
                        new FluidStack(FluidRegistry.getFluid("ic2biogas"), 8000), Materials.Water.getFluid(125L)
                    },
                    ItemList.IC2_Fertilizer.get(1),
                    250,
                    480);
            GT_Values.RA.addFuel(GT_ModHandler.getIC2Item("biogasCell", 1L), null, 40, 1);

            GT_Values.RA.addDistilleryRecipe(
                    GT_Utility.getIntegratedCircuit(1),
                    new FluidStack(FluidRegistry.getFluid("ic2biomass"), 20),
                    new FluidStack(FluidRegistry.getFluid("ic2biogas"), 32),
                    40,
                    16,
                    false);
            GT_Values.RA.addDistilleryRecipe(
                    GT_Utility.getIntegratedCircuit(2),
                    new FluidStack(FluidRegistry.getFluid("ic2biomass"), 4),
                    Materials.Water.getFluid(2),
                    80,
                    30,
                    false);
        }

        GT_Values.RA.addFuel(new ItemStack(Items.golden_apple, 1, 1), new ItemStack(Items.apple, 1), 6400, 5);
        GT_Values.RA.addFuel(getModItem("Thaumcraft", "ItemShard", 1L, 6), null, 720, 5);
        GT_Values.RA.addFuel(getModItem("ForbiddenMagic", "GluttonyShard", 1L), null, 720, 5);
        GT_Values.RA.addFuel(getModItem("ForbiddenMagic", "FMResource", 1L, 3), null, 720, 5);
        GT_Values.RA.addFuel(getModItem("ForbiddenMagic", "NetherShard", 1L), null, 720, 5);
        GT_Values.RA.addFuel(getModItem("ForbiddenMagic", "NetherShard", 1L, 1), null, 720, 5);
        GT_Values.RA.addFuel(getModItem("ForbiddenMagic", "NetherShard", 1L, 2), null, 720, 5);
        GT_Values.RA.addFuel(getModItem("ForbiddenMagic", "NetherShard", 1L, 3), null, 720, 5);
        GT_Values.RA.addFuel(getModItem("ForbiddenMagic", "NetherShard", 1L, 4), null, 720, 5);
        GT_Values.RA.addFuel(getModItem("ForbiddenMagic", "NetherShard", 1L, 5), null, 720, 5);
        GT_Values.RA.addFuel(getModItem("ForbiddenMagic", "NetherShard", 1L, 6), null, 720, 5);
        GT_Values.RA.addFuel(getModItem("TaintedMagic", "WarpedShard", 1L), null, 720, 5);
        GT_Values.RA.addFuel(getModItem("TaintedMagic", "FluxShard", 1L), null, 720, 5);
        GT_Values.RA.addFuel(getModItem("TaintedMagic", "EldritchShard", 1L), null, 720, 5);
        GT_Values.RA.addFuel(getModItem("ThaumicTinkerer", "kamiResource", 1L, 6), null, 720, 5);
        GT_Values.RA.addFuel(getModItem("ThaumicTinkerer", "kamiResource", 1L, 7), null, 720, 5);





        if (isThaumcraftLoaded) {


            // Add Recipe for TC Crucible: Salis Mundus to Balanced Shards
            String tKey = "GT_BALANCE_SHARD_RECIPE";
            GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                    "TB.SM",
                    getModItem(MOD_ID_TC, "ItemResource", 1L, 14),
                    getModItem(MOD_ID_TC, "ItemShard", 1L, 6),
                    Arrays.asList(
                            new TC_Aspects.TC_AspectStack(TC_Aspects.PRAECANTATIO, 2L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 1L)));
        }






        GT_Values.RA.addCutterRecipe(
                getModItem("BuildCraft|Transport", "item.buildcraftPipe.pipestructurecobblestone", 1L, 0),
                getModItem("BuildCraft|Transport", "pipePlug", 8L, 0),
                GT_Values.NI,
                32,
                16);
        for (int i = 0; i < 16; i++) {
            GT_Values.RA.addCutterRecipe(
                    new ItemStack(Blocks.stained_glass, 3, i),
                    new ItemStack(Blocks.stained_glass_pane, 8, i),
                    GT_Values.NI,
                    50,
                    8);
        }
        GT_Values.RA.addCutterRecipe(
                new ItemStack(Blocks.glass, 3, 0), new ItemStack(Blocks.glass_pane, 8, 0), GT_Values.NI, 50, 8);
        GT_Values.RA.addCutterRecipe(
                getModItem("TConstruct", "GlassBlock", 3L, 0),
                getModItem("TConstruct", "GlassPane", 8L, 0),
                GT_Values.NI,
                50,
                8);
        GT_Values.RA.addCutterRecipe(
                new ItemStack(Blocks.stone, 1, 0), new ItemStack(Blocks.stone_slab, 2, 0), GT_Values.NI, 25, 8);
        GT_Values.RA.addCutterRecipe(
                new ItemStack(Blocks.sandstone, 1, 0), new ItemStack(Blocks.stone_slab, 2, 1), GT_Values.NI, 25, 8);
        GT_Values.RA.addCutterRecipe(
                new ItemStack(Blocks.cobblestone, 1, 0), new ItemStack(Blocks.stone_slab, 2, 3), GT_Values.NI, 25, 8);
        GT_Values.RA.addCutterRecipe(
                new ItemStack(Blocks.brick_block, 1, 0), new ItemStack(Blocks.stone_slab, 2, 4), GT_Values.NI, 25, 8);
        GT_Values.RA.addCutterRecipe(
                new ItemStack(Blocks.stonebrick, 1, 0), new ItemStack(Blocks.stone_slab, 2, 5), GT_Values.NI, 25, 8);
        GT_Values.RA.addCutterRecipe(
                new ItemStack(Blocks.nether_brick, 1, 0), new ItemStack(Blocks.stone_slab, 2, 6), GT_Values.NI, 25, 8);
        GT_Values.RA.addCutterRecipe(
                new ItemStack(Blocks.quartz_block, 1, 32767),
                new ItemStack(Blocks.stone_slab, 2, 7),
                GT_Values.NI,
                25,
                8);
        GT_Values.RA.addCutterRecipe(
                new ItemStack(Blocks.glowstone, 1, 0),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Glowstone, 4L),
                GT_Values.NI,
                100,
                16);

        for (byte i = 0; i < 16; i = (byte) (i + 1)) {
            GT_Values.RA.addCutterRecipe(
                    new ItemStack(Blocks.wool, 1, i), new ItemStack(Blocks.carpet, 2, i), GT_Values.NI, 50, 8);
        }
        GT_Values.RA.addCutterRecipe(
                new ItemStack(Blocks.wooden_slab, 1, 0), ItemList.Plank_Oak.get(2L), GT_Values.NI, 50, 8);
        GT_Values.RA.addCutterRecipe(
                new ItemStack(Blocks.wooden_slab, 1, 1), ItemList.Plank_Spruce.get(2L), GT_Values.NI, 50, 8);
        GT_Values.RA.addCutterRecipe(
                new ItemStack(Blocks.wooden_slab, 1, 2), ItemList.Plank_Birch.get(2L), GT_Values.NI, 50, 8);
        GT_Values.RA.addCutterRecipe(
                new ItemStack(Blocks.wooden_slab, 1, 3), ItemList.Plank_Jungle.get(2L), GT_Values.NI, 50, 8);
        GT_Values.RA.addCutterRecipe(
                new ItemStack(Blocks.wooden_slab, 1, 4), ItemList.Plank_Acacia.get(2L), GT_Values.NI, 50, 8);
        GT_Values.RA.addCutterRecipe(
                new ItemStack(Blocks.wooden_slab, 1, 5), ItemList.Plank_DarkOak.get(2L), GT_Values.NI, 50, 8);
        boolean loaded = Loader.isModLoaded(GT_MachineRecipeLoader.aTextForestry); // TODO OW YEAH NEW PLANK GEN CODE!!!
        ItemStack[] coverIDs = {
            ItemList.Plank_Larch.get(2L),
            ItemList.Plank_Teak.get(2L),
            ItemList.Plank_Acacia_Green.get(2L),
            ItemList.Plank_Lime.get(2L),
            ItemList.Plank_Chestnut.get(2L),
            ItemList.Plank_Wenge.get(2L),
            ItemList.Plank_Baobab.get(2L),
            ItemList.Plank_Sequoia.get(2L),
            ItemList.Plank_Kapok.get(2L),
            ItemList.Plank_Ebony.get(2L),
            ItemList.Plank_Mahagony.get(2L),
            ItemList.Plank_Balsa.get(2L),
            ItemList.Plank_Willow.get(2L),
            ItemList.Plank_Walnut.get(2L),
            ItemList.Plank_Greenheart.get(2L),
            ItemList.Plank_Cherry.get(2L),
            ItemList.Plank_Mahoe.get(2L),
            ItemList.Plank_Poplar.get(2L),
            ItemList.Plank_Palm.get(2L),
            ItemList.Plank_Papaya.get(2L),
            ItemList.Plank_Pine.get(2L),
            ItemList.Plank_Plum.get(2L),
            ItemList.Plank_Maple.get(2L),
            ItemList.Plank_Citrus.get(2L)
        };
        int i = 0;
        for (ItemStack cover : coverIDs) {
            if (loaded) {
                ItemStack slabWood = getModItem(GT_MachineRecipeLoader.aTextForestry, "slabs", 1, i);
                ItemStack slabWoodFireproof = getModItem(GT_MachineRecipeLoader.aTextForestry, "slabsFireproof", 1, i);
                GT_ModHandler.addCraftingRecipe(
                        cover,
                        GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE,
                        new Object[] {"s ", " P", 'P', slabWood});
                GT_ModHandler.addCraftingRecipe(
                        cover,
                        GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE,
                        new Object[] {"s ", " P", 'P', slabWoodFireproof});
                GT_Values.RA.addCutterRecipe(slabWood, cover, null, 40, 8);
                GT_Values.RA.addCutterRecipe(slabWoodFireproof, cover, null, 40, 8);
            } else if (GT_MachineRecipeLoader.isNEILoaded) {
                API.hideItem(cover);
            }
            i++;
        }
        for (int g = 0; g < 16; g++) {
            if (!GT_MachineRecipeLoader.isNEILoaded) {
                break;
            }
            API.hideItem(new ItemStack(GT_MetaGenerated_Item_03.INSTANCE, 1, g));
        }

        GT_Values.RA.addElectromagneticSeparatorRecipe(
                MaterialsOreAlum.SluiceSand.getDust(1),
                Materials.Iron.getDust(1),
                Materials.Neodymium.getDust(1),
                Materials.Chrome.getDust(1),
                new int[] {4000, 2000, 2000},
                200,
                240);

        GT_Values.RA.addDistilleryRecipe(
                1,
                MaterialsOreAlum.SluiceJuice.getFluid(1000),
                Materials.Water.getFluid(500),
                MaterialsOreAlum.SluiceSand.getDust(1),
                100,
                16,
                false);

        GT_BauxiteRefineChain.run();
    }

    public void run2() {

        GT_Values.RA.addLatheRecipe(
                new ItemStack(Blocks.wooden_slab, 1, GT_Values.W),
                new ItemStack(Items.bowl, 1),
                GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Wood, 1),
                50,
                8);
        GT_Values.RA.addLatheRecipe(
                getModItem(GT_MachineRecipeLoader.aTextForestry, "slabs", 1L, GT_Values.W),
                new ItemStack(Items.bowl, 1),
                GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Wood, 1),
                50,
                8);
        GT_Values.RA.addLatheRecipe(
                getModItem(GT_MachineRecipeLoader.aTextEBXL, "woodslab", 1L, GT_Values.W),
                new ItemStack(Items.bowl, 1),
                GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Wood, 1),
                50,
                8);

        if (!GT_Mod.gregtechproxy.mDisableIC2Cables) {
            GT_Values.RA.addWiremillRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Copper, 1L),
                    GT_ModHandler.getIC2Item("copperCableItem", 3L),
                    100,
                    2);
            GT_Values.RA.addWiremillRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.AnnealedCopper, 1L),
                    GT_ModHandler.getIC2Item("copperCableItem", 3L),
                    100,
                    2);
            GT_Values.RA.addWiremillRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Tin, 1L),
                    GT_ModHandler.getIC2Item("tinCableItem", 4L),
                    150,
                    1);
            GT_Values.RA.addWiremillRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 1L),
                    GT_ModHandler.getIC2Item("ironCableItem", 6L),
                    200,
                    2);
            GT_Values.RA.addWiremillRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 1L),
                    GT_ModHandler.getIC2Item("ironCableItem", 6L),
                    200,
                    2);
            GT_Values.RA.addWiremillRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Gold, 1L),
                    GT_ModHandler.getIC2Item("goldCableItem", 6L),
                    200,
                    1);
        }
        GT_RecipeRegistrator.registerWiremillRecipes(
                Materials.Graphene, 400, 2, OrePrefixes.dust, OrePrefixes.stick, 1);

        GT_ModHandler.removeRecipe(new ItemStack(Items.lava_bucket), ItemList.Cell_Empty.get(1L));
        GT_ModHandler.removeRecipe(new ItemStack(Items.water_bucket), ItemList.Cell_Empty.get(1L));

        GT_ModHandler.removeFurnaceSmelting(ItemList.IC2_Resin.get(1L));

        this.run3();

        GT_Utility.removeSimpleIC2MachineRecipe(
                new ItemStack(Blocks.cobblestone),
                GT_ModHandler.getMaceratorRecipeList(),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1L));
        GT_Utility.removeSimpleIC2MachineRecipe(
                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Lapis, 1L),
                GT_ModHandler.getMaceratorRecipeList(),
                ItemList.IC2_Plantball.get(1L));
        GT_Utility.removeSimpleIC2MachineRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L),
                GT_ModHandler.getMaceratorRecipeList(),
                ItemList.IC2_Plantball.get(1L));
        GT_Utility.removeSimpleIC2MachineRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glowstone, 1L),
                GT_ModHandler.getMaceratorRecipeList(),
                ItemList.IC2_Plantball.get(1L));
        GT_Utility.removeSimpleIC2MachineRecipe(
                GT_Values.NI, GT_ModHandler.getMaceratorRecipeList(), getModItem("IC2", "itemBiochaff", 1L));

        GT_Utility.removeSimpleIC2MachineRecipe(
                new ItemStack(Blocks.cactus, 8, 0),
                GT_ModHandler.getCompressorRecipeList(),
                getModItem("IC2", "itemFuelPlantBall", 1L));
        GT_Utility.removeSimpleIC2MachineRecipe(
                getModItem("ExtraTrees", "food", 8L, 24),
                GT_ModHandler.getCompressorRecipeList(),
                getModItem("IC2", "itemFuelPlantBall", 1L));



        if (!GregTech_API.mIC2Classic) {
            try {
                Map<String, ILiquidHeatExchangerManager.HeatExchangeProperty> tLiqExchange =
                        ic2.api.recipe.Recipes.liquidCooldownManager.getHeatExchangeProperties();
                Iterator<Map.Entry<String, ILiquidHeatExchangerManager.HeatExchangeProperty>> tIterator =
                        tLiqExchange.entrySet().iterator();
                while (tIterator.hasNext()) {
                    Map.Entry<String, ILiquidHeatExchangerManager.HeatExchangeProperty> tEntry = tIterator.next();
                    if (tEntry.getKey().equals("ic2hotcoolant")) {
                        tIterator.remove();
                        Recipes.liquidCooldownManager.addFluid("ic2hotcoolant", "ic2coolant", 100);
                    }
                }
            } catch (Throwable e) {
                /*Do nothing*/
            }

            try {
                Map<String, ILiquidHeatExchangerManager.HeatExchangeProperty> tLiqExchange =
                        ic2.api.recipe.Recipes.liquidHeatupManager.getHeatExchangeProperties();
                Iterator<Map.Entry<String, ILiquidHeatExchangerManager.HeatExchangeProperty>> tIterator =
                        tLiqExchange.entrySet().iterator();
                while (tIterator.hasNext()) {
                    Map.Entry<String, ILiquidHeatExchangerManager.HeatExchangeProperty> tEntry = tIterator.next();
                    if (tEntry.getKey().equals("ic2coolant")) {
                        tIterator.remove();
                        Recipes.liquidHeatupManager.addFluid("ic2coolant", "ic2hotcoolant", 100);
                    }
                }
            } catch (Throwable e) {
                /*Do nothing*/
            }
        }
        GT_Utility.removeSimpleIC2MachineRecipe(
                ItemList.Crop_Drop_BobsYerUncleRanks.get(1L), GT_ModHandler.getExtractorRecipeList(), null);
        GT_Utility.removeSimpleIC2MachineRecipe(
                ItemList.Crop_Drop_Ferru.get(1L), GT_ModHandler.getExtractorRecipeList(), null);
        GT_Utility.removeSimpleIC2MachineRecipe(
                ItemList.Crop_Drop_Aurelia.get(1L), GT_ModHandler.getExtractorRecipeList(), null);

        ItemStack tCrop;
        // Metals Line
        tCrop = ItemList.Crop_Drop_Coppon.get(1);
        this.addProcess(tCrop, Materials.Copper, 100, true);
        this.addProcess(tCrop, Materials.Tetrahedrite, 100, false);
        this.addProcess(tCrop, Materials.Chalcopyrite, 100, false);
        this.addProcess(tCrop, Materials.Malachite, 100, false);
        this.addProcess(tCrop, Materials.Pyrite, 100, false);
        this.addProcess(tCrop, Materials.Stibnite, 100, false);
        tCrop = ItemList.Crop_Drop_Tine.get(1);
        this.addProcess(tCrop, Materials.Tin, 100, true);
        this.addProcess(tCrop, Materials.Cassiterite, 100, false);
        this.addProcess(tCrop, Materials.CassiteriteSand, 100, true);
        tCrop = ItemList.Crop_Drop_Plumbilia.get(1);
        this.addProcess(tCrop, Materials.Lead, 100, true);
        this.addProcess(tCrop, Materials.Galena, 100, false); //
        tCrop = ItemList.Crop_Drop_Ferru.get(1);
        this.addProcess(tCrop, Materials.Iron, 100, true);
        this.addProcess(tCrop, Materials.Magnetite, 100, false);
        this.addProcess(tCrop, Materials.BrownLimonite, 100, false);
        this.addProcess(tCrop, Materials.YellowLimonite, 100, false);
        this.addProcess(tCrop, Materials.VanadiumMagnetite, 100, false);
        this.addProcess(tCrop, Materials.BandedIron, 100, false);
        this.addProcess(tCrop, Materials.Pyrite, 100, false);
        this.addProcess(tCrop, Materials.MeteoricIron, 100, false);
        tCrop = ItemList.Crop_Drop_Nickel.get(1);
        this.addProcess(tCrop, Materials.Nickel, 100, true);
        this.addProcess(tCrop, Materials.Garnierite, 100, false);
        this.addProcess(tCrop, Materials.Pentlandite, 100, false);
        this.addProcess(tCrop, Materials.Cobaltite, 100, false);
        this.addProcess(tCrop, Materials.Wulfenite, 100, false);
        this.addProcess(tCrop, Materials.Powellite, 100, false);
        tCrop = ItemList.Crop_Drop_Zinc.get(1);
        this.addProcess(tCrop, Materials.Zinc, 100, true);
        this.addProcess(tCrop, Materials.Sphalerite, 100, false);
        this.addProcess(tCrop, Materials.Sulfur, 100, false);
        tCrop = ItemList.Crop_Drop_Argentia.get(1);
        this.addProcess(tCrop, Materials.Silver, 100, true);
        this.addProcess(tCrop, Materials.Galena, 100, false);
        tCrop = ItemList.Crop_Drop_Aurelia.get(1);
        this.addProcess(tCrop, Materials.Gold, 100, true);
        this.addProcess(tCrop, Materials.Magnetite, Materials.Gold, 100, false);
        tCrop = ItemList.Crop_Drop_Mica.get(1);
        this.addProcess(tCrop, Materials.Mica, 75, true);

        // Rare Metals Line
        tCrop = ItemList.Crop_Drop_Bauxite.get(1);
        this.addProcess(tCrop, Materials.Aluminium, 60, true);
        this.addProcess(tCrop, Materials.Bauxite, 100, false);
        tCrop = ItemList.Crop_Drop_Manganese.get(1);
        this.addProcess(tCrop, Materials.Manganese, 30, true);
        this.addProcess(tCrop, Materials.Grossular, 100, false);
        this.addProcess(tCrop, Materials.Spessartine, 100, false);
        this.addProcess(tCrop, Materials.Pyrolusite, 100, false);
        this.addProcess(tCrop, Materials.Tantalite, 100, false);
        tCrop = ItemList.Crop_Drop_Ilmenite.get(1);
        this.addProcess(tCrop, Materials.Titanium, 100, true);
        this.addProcess(tCrop, Materials.Ilmenite, 100, false);
        this.addProcess(tCrop, Materials.Bauxite, 100, false);
        this.addProcess(tCrop, Materials.Rutile, 100, false);
        tCrop = ItemList.Crop_Drop_Scheelite.get(1);
        this.addProcess(tCrop, Materials.Scheelite, 100, true);
        this.addProcess(tCrop, Materials.Tungstate, 100, false);
        this.addProcess(tCrop, Materials.Lithium, 100, false);
        this.addProcess(tCrop, Materials.Tungsten, 75, false);
        tCrop = ItemList.Crop_Drop_Platinum.get(1);
        this.addProcess(tCrop, Materials.Platinum, 40, true);
        this.addProcess(tCrop, Materials.Cooperite, 40, false);
        this.addProcess(tCrop, Materials.Palladium, 40, false);
        this.addProcess(tCrop, Materials.Neodymium, 100, false);
        this.addProcess(tCrop, Materials.Bastnasite, 100, false);
        tCrop = ItemList.Crop_Drop_Iridium.get(1);
        this.addProcess(tCrop, Materials.Iridium, 20, true);
        tCrop = ItemList.Crop_Drop_Osmium.get(1);
        this.addProcess(tCrop, Materials.Osmium, 20, true);

        // Radioactive Line
        tCrop = ItemList.Crop_Drop_Pitchblende.get(1);
        this.addProcess(tCrop, Materials.Pitchblende, 50, true);
        tCrop = ItemList.Crop_Drop_Uraninite.get(1);
        this.addProcess(tCrop, Materials.Uraninite, 50, false);
        this.addProcess(tCrop, Materials.Uranium, 50, true);
        this.addProcess(tCrop, Materials.Pitchblende, 50, false);
        this.addProcess(tCrop, Materials.Uranium235, 50, false);
        tCrop = ItemList.Crop_Drop_Thorium.get(1);
        this.addProcess(tCrop, Materials.Thorium, 50, true);
        tCrop = ItemList.Crop_Drop_Naquadah.get(1);
        this.addProcess(tCrop, Materials.Naquadah, 10, true);
        this.addProcess(tCrop, Materials.NaquadahEnriched, 10, false);
        this.addProcess(tCrop, Materials.Naquadria, 10, false);

        // Gem Line
        tCrop = ItemList.Crop_Drop_BobsYerUncleRanks.get(1);
        this.addProcess(tCrop, Materials.Emerald, 100, true);
        this.addProcess(tCrop, Materials.Beryllium, 100, false);

        this.addRecipesApril2017ChemistryUpdate();







        this.addRecipesMay2017OilRefining();
        this.addPolybenzimidazoleRecipes();
        this.addKevlarLineRecipes();

        loadRailcraftRecipes();
    }

    private void addKevlarLineRecipes() {
        // Kevlar Line
        // C15H10N2O2(5HCl) = C15H10N2O2 + 5HCl
        GT_Values.RA.addDistillationTowerRecipe(
                MaterialsKevlar.DiphenylmethaneDiisocyanateMixture.getFluid(1000L),
                new FluidStack[] {Materials.HydrochloricAcid.getFluid(5000L)},
                GT_OreDictUnificator.get(OrePrefixes.dust, MaterialsKevlar.DiphenylmethaneDiisocyanate, 29L),
                2500,
                1920);

        GT_Values.RA.addDistillationTowerRecipe(
                Materials.CharcoalByproducts.getGas(1000),
                new ItemStack[] {GT_Utility.getIntegratedCircuit(2)},
                new FluidStack[] {
                    Materials.WoodTar.getFluid(250),
                    Materials.WoodVinegar.getFluid(400),
                    Materials.WoodGas.getGas(250),
                    MaterialsKevlar.IIIDimethylbenzene.getFluid(100)
                },
                Materials.Charcoal.getDustSmall(1),
                40,
                256);
        GT_Values.RA.addDistillationTowerRecipe(
                Materials.WoodTar.getFluid(1000),
                new ItemStack[] {GT_Utility.getIntegratedCircuit(2)},
                new FluidStack[] {
                    Materials.Creosote.getFluid(250),
                    Materials.Phenol.getFluid(100),
                    Materials.Benzene.getFluid(400),
                    Materials.Toluene.getFluid(100),
                    MaterialsKevlar.IIIDimethylbenzene.getFluid(150)
                },
                GT_Values.NI,
                40,
                256);
        GT_Values.RA.addDistillationTowerRecipe(
                Materials.CharcoalByproducts.getGas(1000),
                new ItemStack[] {GT_Utility.getIntegratedCircuit(3)},
                new FluidStack[] {
                    Materials.WoodTar.getFluid(250),
                    Materials.WoodVinegar.getFluid(400),
                    Materials.WoodGas.getGas(250),
                    MaterialsKevlar.IVDimethylbenzene.getFluid(100)
                },
                Materials.Charcoal.getDustSmall(1),
                40,
                256);
        GT_Values.RA.addUniversalDistillationRecipewithCircuit(
                Materials.WoodTar.getFluid(1000),
                new ItemStack[] {GT_Utility.getIntegratedCircuit(3)},
                new FluidStack[] {
                    Materials.Creosote.getFluid(250),
                    Materials.Phenol.getFluid(100),
                    Materials.Benzene.getFluid(400),
                    Materials.Toluene.getFluid(100),
                    MaterialsKevlar.IVDimethylbenzene.getFluid(150)
                },
                GT_Values.NI,
                40,
                256);
        GT_Values.RA.addDistillationTowerRecipe(
                Materials.CharcoalByproducts.getGas(1000),
                new ItemStack[] {GT_Utility.getIntegratedCircuit(4)},
                new FluidStack[] {
                    Materials.WoodTar.getFluid(250),
                    Materials.WoodVinegar.getFluid(400),
                    Materials.WoodGas.getGas(250),
                    Materials.Dimethylbenzene.getFluid(20),
                    MaterialsKevlar.IIIDimethylbenzene.getFluid(60),
                    MaterialsKevlar.IVDimethylbenzene.getFluid(20)
                },
                Materials.Charcoal.getDustSmall(1),
                40,
                256);
        GT_Values.RA.addDistillationTowerRecipe(
                Materials.WoodTar.getFluid(1000),
                new ItemStack[] {GT_Utility.getIntegratedCircuit(4)},
                new FluidStack[] {
                    Materials.Creosote.getFluid(250),
                    Materials.Phenol.getFluid(100),
                    Materials.Benzene.getFluid(400),
                    Materials.Toluene.getFluid(100),
                    Materials.Dimethylbenzene.getFluid(30),
                    MaterialsKevlar.IIIDimethylbenzene.getFluid(90),
                    MaterialsKevlar.IVDimethylbenzene.getFluid(30)
                },
                GT_Values.NI,
                40,
                256);

        GT_Values.RA.addDistilleryRecipe(
                6, Materials.WoodTar.getFluid(200), MaterialsKevlar.IIIDimethylbenzene.getFluid(30), 16, 64, false);
        GT_Values.RA.addDistilleryRecipe(
                6,
                Materials.CharcoalByproducts.getGas(200),
                MaterialsKevlar.IIIDimethylbenzene.getFluid(20),
                100,
                64,
                false);
        GT_Values.RA.addDistilleryRecipe(
                7, Materials.WoodTar.getFluid(200), MaterialsKevlar.IVDimethylbenzene.getFluid(30), 16, 64, false);
        GT_Values.RA.addDistilleryRecipe(
                7,
                Materials.CharcoalByproducts.getGas(200),
                MaterialsKevlar.IVDimethylbenzene.getFluid(20),
                100,
                64,
                false);




        GT_Values.RA.addDistillationTowerRecipe(
                Materials.OilLight.getFluid(1500),
                new ItemStack[] {GT_Utility.getIntegratedCircuit(9)},
                new FluidStack[] {
                    Materials.SulfuricHeavyFuel.getFluid(100),
                    Materials.SulfuricLightFuel.getFluid(200),
                    Materials.SulfuricNaphtha.getFluid(300),
                    MaterialsKevlar.NaphthenicAcid.getFluid(25),
                    Materials.SulfuricGas.getGas(2400)
                },
                null,
                32,
                480);
        GT_Values.RA.addDistillationTowerRecipe(
                Materials.OilMedium.getFluid(1000),
                new ItemStack[] {GT_Utility.getIntegratedCircuit(9)},
                new FluidStack[] {
                    Materials.SulfuricHeavyFuel.getFluid(100),
                    Materials.SulfuricLightFuel.getFluid(500),
                    Materials.SulfuricNaphtha.getFluid(1500),
                    MaterialsKevlar.NaphthenicAcid.getFluid(25),
                    Materials.SulfuricGas.getGas(600)
                },
                null,
                32,
                480);
        GT_Values.RA.addDistillationTowerRecipe(
                Materials.Oil.getFluid(500L),
                new ItemStack[] {GT_Utility.getIntegratedCircuit(9)},
                new FluidStack[] {
                    Materials.SulfuricHeavyFuel.getFluid(150),
                    Materials.SulfuricLightFuel.getFluid(500),
                    Materials.SulfuricNaphtha.getFluid(200),
                    MaterialsKevlar.NaphthenicAcid.getFluid(25),
                    Materials.SulfuricGas.getGas(600)
                },
                null,
                32,
                480);
        GT_Values.RA.addDistillationTowerRecipe(
                Materials.OilHeavy.getFluid(1000),
                new ItemStack[] {GT_Utility.getIntegratedCircuit(9)},
                new FluidStack[] {
                    Materials.SulfuricHeavyFuel.getFluid(2500),
                    Materials.SulfuricLightFuel.getFluid(450),
                    Materials.SulfuricNaphtha.getFluid(150),
                    MaterialsKevlar.NaphthenicAcid.getFluid(50),
                    Materials.SulfuricGas.getGas(600)
                },
                null,
                100,
                480);



    }

    public void addProcess(ItemStack tCrop, Materials aMaterial, int chance, boolean aMainOutput) {
        if (tCrop == null || aMaterial == null || GT_OreDictUnificator.get(OrePrefixes.crushed, aMaterial, 1) == null)
            return;
        if (GT_Mod.gregtechproxy.mNerfedCrops) {
            GT_Values.RA.addChemicalRecipe(
                    GT_Utility.copyAmount(9, tCrop),
                    GT_OreDictUnificator.get(OrePrefixes.crushed, aMaterial, 1),
                    Materials.Water.getFluid(1000),
                    aMaterial.mOreByProducts.isEmpty()
                            ? null
                            : aMaterial.mOreByProducts.get(0).getMolten(144),
                    GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, 4),
                    96,
                    24);
            GT_Values.RA.addAutoclaveRecipe(
                    GT_Utility.copyAmount(16, tCrop),
                    Materials.UUMatter.getFluid(Math.max(1, ((aMaterial.getMass() + 9) / 10))),
                    GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, 1),
                    10000,
                    (int) (aMaterial.getMass() * 128),
                    384);
        } else {
            if (aMainOutput)
                GT_Values.RA.addExtractorRecipe(
                        GT_Utility.copyAmount(9, tCrop),
                        GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, 1),
                        300,
                        18);
        }
    }

    public void addProcess(ItemStack tCrop, Materials aMaterial, int chance) {
        if (tCrop == null || aMaterial == null || GT_OreDictUnificator.get(OrePrefixes.crushed, aMaterial, 1) == null)
            return;
        if (GT_Mod.gregtechproxy.mNerfedCrops) {
            GT_Values.RA.addChemicalRecipe(
                    GT_Utility.copyAmount(9, tCrop),
                    GT_OreDictUnificator.get(OrePrefixes.crushed, aMaterial, 1),
                    Materials.Water.getFluid(1000),
                    aMaterial.mOreByProducts.isEmpty()
                            ? null
                            : aMaterial.mOreByProducts.get(0).getMolten(144),
                    GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, 4),
                    96,
                    24);
            GT_Values.RA.addAutoclaveRecipe(
                    GT_Utility.copyAmount(16, tCrop),
                    Materials.UUMatter.getFluid(Math.max(1, ((aMaterial.getMass() + 9) / 10))),
                    GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, 1),
                    10000,
                    (int) (aMaterial.getMass() * 128),
                    384);
        } else {
            GT_Values.RA.addExtractorRecipe(
                    GT_Utility.copyAmount(16, tCrop),
                    GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, 1),
                    300,
                    18);
        }
    }

    public void addProcess(
            ItemStack tCrop, Materials aMaterial, Materials aMaterialOut, int chance, boolean aMainOutput) {
        if (tCrop == null || aMaterial == null || GT_OreDictUnificator.get(OrePrefixes.crushed, aMaterial, 1) == null)
            return;
        if (GT_Mod.gregtechproxy.mNerfedCrops) {
            GT_Values.RA.addChemicalRecipe(
                    GT_Utility.copyAmount(9, tCrop),
                    GT_OreDictUnificator.get(OrePrefixes.crushed, aMaterial, 1),
                    Materials.Water.getFluid(1000),
                    aMaterialOut.mOreByProducts.isEmpty()
                            ? null
                            : aMaterialOut.mOreByProducts.get(0).getMolten(144),
                    GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterialOut, 4),
                    96,
                    24);
            GT_Values.RA.addAutoclaveRecipe(
                    GT_Utility.copyAmount(16, tCrop),
                    Materials.UUMatter.getFluid(Math.max(1, ((aMaterial.getMass() + 9) / 10))),
                    GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, 1),
                    10000,
                    (int) (aMaterial.getMass() * 128),
                    384);
        } else {
            if (aMainOutput)
                GT_Values.RA.addExtractorRecipe(
                        GT_Utility.copyAmount(16, tCrop),
                        GT_OreDictUnificator.get(OrePrefixes.dustTiny, aMaterial, 1),
                        300,
                        18);
        }
    }

    public void addProcess(ItemStack tCrop, Materials aMaterial, Materials aMaterialOut, int chance) {
        if (tCrop == null || aMaterial == null || GT_OreDictUnificator.get(OrePrefixes.crushed, aMaterial, 1) == null)
            return;
        if (GT_Mod.gregtechproxy.mNerfedCrops) {
            GT_Values.RA.addChemicalRecipe(
                    GT_Utility.copyAmount(9, tCrop),
                    GT_OreDictUnificator.get(OrePrefixes.crushed, aMaterial, 1),
                    Materials.Water.getFluid(1000),
                    aMaterialOut.mOreByProducts.isEmpty()
                            ? null
                            : aMaterialOut.mOreByProducts.get(0).getMolten(144),
                    GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterialOut, 4),
                    96,
                    24);
            GT_Values.RA.addAutoclaveRecipe(
                    GT_Utility.copyAmount(16, tCrop),
                    Materials.UUMatter.getFluid(Math.max(1, ((aMaterial.getMass() + 9) / 10))),
                    GT_OreDictUnificator.get(OrePrefixes.crushedPurified, aMaterial, 1),
                    10000,
                    (int) (aMaterial.getMass() * 128),
                    384);
        } else {
            GT_Values.RA.addExtractorRecipe(
                    GT_Utility.copyAmount(16, tCrop),
                    GT_OreDictUnificator.get(OrePrefixes.dustTiny, aMaterial, 1),
                    300,
                    18);
        }
    }

    public void run3() {
        // recipe len:
        // LUV 6         72000   600   32k
        // ZPM 9         144000  1200  125k
        // UV- 12        288000  1800  500k
        // UV+/UHV- 14   360000  2100  2000k
        // UHV+ 16       576000  2400  4000k

        // addAssemblylineRecipe(ItemStack aResearchItem, int aResearchTime, ItemStack[] aInputs, FluidStack[]
        // aFluidInputs, ItemStack aOutput1, int aDuration, int aEUt);

        Fluid solderIndalloy = FluidRegistry.getFluid("molten.indalloy140") != null
                ? FluidRegistry.getFluid("molten.indalloy140")
                : FluidRegistry.getFluid("molten.solderingalloy");

        // Motors
        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Electric_Motor_IV.get(1, new Object() {}),
                144000,
                new ItemStack[] {
                    GT_OreDictUnificator.get(OrePrefixes.stick, Materials.SamariumMagnetic, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.HSSS, 2L),
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Osmiridium, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Osmiridium, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.YttriumBariumCuprate, 2L)
                },
                new FluidStack[] {new FluidStack(solderIndalloy, 144), Materials.Lubricant.getFluid(250)},
                ItemList.Electric_Motor_LuV.get(1),
                600,
                6000);

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Electric_Motor_LuV.get(1, new Object() {}),
                144000,
                new ItemStack[] {
                    GT_OreDictUnificator.get(OrePrefixes.stick, Materials.SamariumMagnetic, 2L),
                    GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.NaquadahAlloy, 4L),
                    GT_OreDictUnificator.get(OrePrefixes.ring, Materials.NaquadahAlloy, 4L),
                    GT_OreDictUnificator.get(OrePrefixes.round, Materials.NaquadahAlloy, 16L),
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Europium, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Europium, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Europium, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.VanadiumGallium, 2L)
                },
                new FluidStack[] {new FluidStack(solderIndalloy, 288), Materials.Lubricant.getFluid(750)},
                ItemList.Electric_Motor_ZPM.get(1),
                600,
                24000);

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Electric_Motor_ZPM.get(1, new Object() {}),
                288000,
                new ItemStack[] {
                    GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.SamariumMagnetic, 2L),
                    GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Neutronium, 4L),
                    GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Neutronium, 4L),
                    GT_OreDictUnificator.get(OrePrefixes.round, Materials.Neutronium, 16L),
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NaquadahAlloy, 2L)
                },
                new FluidStack[] {
                    Materials.Naquadria.getMolten(1296),
                    new FluidStack(solderIndalloy, 1296),
                    Materials.Lubricant.getFluid(2000)
                },
                ItemList.Electric_Motor_UV.get(1),
                600,
                100000);

        // Pumps
        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Electric_Pump_IV.get(1, new Object() {}),
                144000,
                new Object[] {
                    ItemList.Electric_Motor_LuV.get(1, new Object() {}),
                    GT_OreDictUnificator.get(OrePrefixes.pipeSmall, Materials.NiobiumTitanium, 2L),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.HSSS, 2L),
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.HSSS, 8L),
                    new Object[] {OrePrefixes.ring.get(Materials.AnySyntheticRubber), 4L},
                    GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.HSSS, 2L),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.YttriumBariumCuprate, 2L)
                },
                new FluidStack[] {new FluidStack(solderIndalloy, 144), Materials.Lubricant.getFluid(250)},
                ItemList.Electric_Pump_LuV.get(1),
                600,
                6000);

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Electric_Pump_LuV.get(1, new Object() {}),
                144000,
                new Object[] {
                    ItemList.Electric_Motor_ZPM.get(1, new Object() {}),
                    GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Enderium, 2L),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NaquadahAlloy, 2L),
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.NaquadahAlloy, 8L),
                    new Object[] {OrePrefixes.ring.get(Materials.AnySyntheticRubber), 8L},
                    GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.NaquadahAlloy, 2L),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.VanadiumGallium, 2L)
                },
                new FluidStack[] {new FluidStack(solderIndalloy, 288), Materials.Lubricant.getFluid(750)},
                ItemList.Electric_Pump_ZPM.get(1),
                600,
                24000);

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Electric_Pump_ZPM.get(1, new Object() {}),
                288000,
                new Object[] {
                    ItemList.Electric_Motor_UV.get(1, new Object() {}),
                    GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Naquadah, 2L),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 2L),
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Neutronium, 8L),
                    new Object[] {OrePrefixes.ring.get(Materials.AnySyntheticRubber), 16L},
                    GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Neutronium, 2L),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NaquadahAlloy, 2L)
                },
                new FluidStack[] {
                    Materials.Naquadria.getMolten(1296),
                    new FluidStack(solderIndalloy, 1296),
                    Materials.Lubricant.getFluid(2000)
                },
                ItemList.Electric_Pump_UV.get(1),
                600,
                100000);

        // Conveyors
        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Conveyor_Module_IV.get(1, new Object() {}),
                144000,
                new Object[] {
                    ItemList.Electric_Motor_LuV.get(2, new Object() {}),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.HSSS, 2L),
                    GT_OreDictUnificator.get(OrePrefixes.ring, Materials.HSSS, 4L),
                    GT_OreDictUnificator.get(OrePrefixes.round, Materials.HSSS, 32L),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.YttriumBariumCuprate, 2L),
                    new Object[] {OrePrefixes.plate.get(Materials.AnySyntheticRubber), 10L},
                },
                new FluidStack[] {new FluidStack(solderIndalloy, 144), Materials.Lubricant.getFluid(250)},
                ItemList.Conveyor_Module_LuV.get(1),
                600,
                6000);

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Conveyor_Module_LuV.get(1, new Object() {}),
                144000,
                new Object[] {
                    ItemList.Electric_Motor_ZPM.get(2, new Object() {}),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NaquadahAlloy, 2L),
                    GT_OreDictUnificator.get(OrePrefixes.ring, Materials.NaquadahAlloy, 4L),
                    GT_OreDictUnificator.get(OrePrefixes.round, Materials.NaquadahAlloy, 32L),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.VanadiumGallium, 2L),
                    new Object[] {OrePrefixes.plate.get(Materials.AnySyntheticRubber), 20L},
                },
                new FluidStack[] {new FluidStack(solderIndalloy, 288), Materials.Lubricant.getFluid(750)},
                ItemList.Conveyor_Module_ZPM.get(1),
                600,
                24000);

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Conveyor_Module_ZPM.get(1, new Object() {}),
                288000,
                new Object[] {
                    ItemList.Electric_Motor_UV.get(2, new Object() {}),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 2L),
                    GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Neutronium, 4L),
                    GT_OreDictUnificator.get(OrePrefixes.round, Materials.Neutronium, 32L),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NaquadahAlloy, 2L),
                    new Object[] {OrePrefixes.plate.get(Materials.AnySyntheticRubber), 40L}
                },
                new FluidStack[] {
                    Materials.Naquadria.getMolten(1296),
                    new FluidStack(solderIndalloy, 1296),
                    Materials.Lubricant.getFluid(2000)
                },
                ItemList.Conveyor_Module_UV.get(1),
                600,
                100000);

        // Pistons
        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Electric_Piston_IV.get(1, new Object() {}),
                144000,
                new ItemStack[] {
                    ItemList.Electric_Motor_LuV.get(1, new Object() {}),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.HSSS, 6L),
                    GT_OreDictUnificator.get(OrePrefixes.ring, Materials.HSSS, 4L),
                    GT_OreDictUnificator.get(OrePrefixes.round, Materials.HSSS, 32L),
                    GT_OreDictUnificator.get(OrePrefixes.stick, Materials.HSSS, 4L),
                    GT_OreDictUnificator.get(OrePrefixes.gear, Materials.HSSS, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.HSSS, 2L),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.YttriumBariumCuprate, 4L)
                },
                new FluidStack[] {new FluidStack(solderIndalloy, 144), Materials.Lubricant.getFluid(250)},
                ItemList.Electric_Piston_LuV.get(1),
                600,
                6000);

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Electric_Piston_LuV.get(1, new Object() {}),
                144000,
                new ItemStack[] {
                    ItemList.Electric_Motor_ZPM.get(1, new Object() {}),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NaquadahAlloy, 6L),
                    GT_OreDictUnificator.get(OrePrefixes.ring, Materials.NaquadahAlloy, 4L),
                    GT_OreDictUnificator.get(OrePrefixes.round, Materials.NaquadahAlloy, 32L),
                    GT_OreDictUnificator.get(OrePrefixes.stick, Materials.NaquadahAlloy, 4L),
                    GT_OreDictUnificator.get(OrePrefixes.gear, Materials.NaquadahAlloy, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.NaquadahAlloy, 2L),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.VanadiumGallium, 4L)
                },
                new FluidStack[] {new FluidStack(solderIndalloy, 288), Materials.Lubricant.getFluid(750)},
                ItemList.Electric_Piston_ZPM.get(1),
                600,
                24000);

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Electric_Piston_ZPM.get(1, new Object() {}),
                288000,
                new ItemStack[] {
                    ItemList.Electric_Motor_UV.get(1, new Object() {}),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 6L),
                    GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Neutronium, 4L),
                    GT_OreDictUnificator.get(OrePrefixes.round, Materials.Neutronium, 32L),
                    GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Neutronium, 4L),
                    GT_OreDictUnificator.get(OrePrefixes.gear, Materials.Neutronium, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Neutronium, 2L),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NaquadahAlloy, 4L)
                },
                new FluidStack[] {
                    Materials.Naquadria.getMolten(1296),
                    new FluidStack(solderIndalloy, 1296),
                    Materials.Lubricant.getFluid(2000)
                },
                ItemList.Electric_Piston_UV.get(1),
                600,
                100000);

        // RobotArms
        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Robot_Arm_IV.get(1, new Object() {}),
                144000,
                new Object[] {
                    GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.HSSS, 4L),
                    GT_OreDictUnificator.get(OrePrefixes.gear, Materials.HSSS, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.HSSS, 3L),
                    ItemList.Electric_Motor_LuV.get(2, new Object() {}),
                    ItemList.Electric_Piston_LuV.get(1, new Object() {}),
                    new Object[] {OrePrefixes.circuit.get(Materials.Master), 2},
                    new Object[] {OrePrefixes.circuit.get(Materials.Elite), 4},
                    new Object[] {OrePrefixes.circuit.get(Materials.Data), 8},
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.YttriumBariumCuprate, 6L)
                },
                new FluidStack[] {new FluidStack(solderIndalloy, 576), Materials.Lubricant.getFluid(250)},
                ItemList.Robot_Arm_LuV.get(1),
                600,
                6000);

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Robot_Arm_LuV.get(1, new Object() {}),
                144000,
                new Object[] {
                    GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.NaquadahAlloy, 4L),
                    GT_OreDictUnificator.get(OrePrefixes.gear, Materials.NaquadahAlloy, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.NaquadahAlloy, 3L),
                    ItemList.Electric_Motor_ZPM.get(2, new Object() {}),
                    ItemList.Electric_Piston_ZPM.get(1, new Object() {}),
                    new Object[] {OrePrefixes.circuit.get(Materials.Ultimate), 2},
                    new Object[] {OrePrefixes.circuit.get(Materials.Master), 4},
                    new Object[] {OrePrefixes.circuit.get(Materials.Elite), 8},
                    GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.VanadiumGallium, 6L)
                },
                new FluidStack[] {new FluidStack(solderIndalloy, 1152), Materials.Lubricant.getFluid(750)},
                ItemList.Robot_Arm_ZPM.get(1),
                600,
                24000);

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Robot_Arm_ZPM.get(1, new Object() {}),
                288000,
                new Object[] {
                    GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Neutronium, 4L),
                    GT_OreDictUnificator.get(OrePrefixes.gear, Materials.Neutronium, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Neutronium, 3L),
                    ItemList.Electric_Motor_UV.get(2, new Object() {}),
                    ItemList.Electric_Piston_UV.get(1, new Object() {}),
                    new Object[] {OrePrefixes.circuit.get(Materials.Superconductor), 2},
                    new Object[] {OrePrefixes.circuit.get(Materials.Ultimate), 4},
                    new Object[] {OrePrefixes.circuit.get(Materials.Master), 8},
                    GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NaquadahAlloy, 6L)
                },
                new FluidStack[] {
                    Materials.Naquadria.getMolten(1296),
                    new FluidStack(solderIndalloy, 2304),
                    Materials.Lubricant.getFluid(2000)
                },
                ItemList.Robot_Arm_UV.get(1),
                600,
                100000);

        // Emitters
        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Emitter_IV.get(1, new Object() {}),
                144000,
                new Object[] {
                    GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.HSSS, 1L),
                    ItemList.Electric_Motor_LuV.get(1, new Object() {}),
                    GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Osmiridium, 8L),
                    ItemList.QuantumStar.get(1, new Object() {}),
                    new Object[] {OrePrefixes.circuit.get(Materials.Master), 4},
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Gallium, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Gallium, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Gallium, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.YttriumBariumCuprate, 7L)
                },
                new FluidStack[] {new FluidStack(solderIndalloy, 576)},
                ItemList.Emitter_LuV.get(1),
                600,
                6000);

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Emitter_LuV.get(1, new Object() {}),
                144000,
                new Object[] {
                    GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.NaquadahAlloy, 1L),
                    ItemList.Electric_Motor_ZPM.get(1, new Object() {}),
                    GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Osmiridium, 8L),
                    ItemList.QuantumStar.get(2, new Object() {}),
                    new Object[] {OrePrefixes.circuit.get(Materials.Ultimate), 4},
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Trinium, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Trinium, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Trinium, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.VanadiumGallium, 7L)
                },
                new FluidStack[] {new FluidStack(solderIndalloy, 1152)},
                ItemList.Emitter_ZPM.get(1),
                600,
                24000);

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Emitter_ZPM.get(1, new Object() {}),
                288000,
                new Object[] {
                    GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 1L),
                    ItemList.Electric_Motor_UV.get(1, new Object() {}),
                    GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Neutronium, 8L),
                    ItemList.Gravistar.get(4, new Object() {}),
                    new Object[] {OrePrefixes.circuit.get(Materials.Superconductor), 4},
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Naquadria, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Naquadria, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Naquadria, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NaquadahAlloy, 7L)
                },
                new FluidStack[] {Materials.Naquadria.getMolten(1296), new FluidStack(solderIndalloy, 2304)},
                ItemList.Emitter_UV.get(1),
                600,
                100000);

        // Sensors
        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Sensor_IV.get(1, new Object() {}),
                144000,
                new Object[] {
                    GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.HSSS, 1L),
                    ItemList.Electric_Motor_LuV.get(1, new Object() {}),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmiridium, 8L),
                    ItemList.QuantumStar.get(1, new Object() {}),
                    new Object[] {OrePrefixes.circuit.get(Materials.Master), 4},
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Gallium, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Gallium, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Gallium, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.YttriumBariumCuprate, 7L)
                },
                new FluidStack[] {new FluidStack(solderIndalloy, 576)},
                ItemList.Sensor_LuV.get(1),
                600,
                6000);

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Sensor_LuV.get(1, new Object() {}),
                144000,
                new Object[] {
                    GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.NaquadahAlloy, 1L),
                    ItemList.Electric_Motor_ZPM.get(1, new Object() {}),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmiridium, 8L),
                    ItemList.QuantumStar.get(2, new Object() {}),
                    new Object[] {OrePrefixes.circuit.get(Materials.Ultimate), 4},
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Trinium, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Trinium, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Trinium, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.VanadiumGallium, 7L)
                },
                new FluidStack[] {new FluidStack(solderIndalloy, 1152)},
                ItemList.Sensor_ZPM.get(1),
                600,
                24000);

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Sensor_ZPM.get(1, new Object() {}),
                288000,
                new Object[] {
                    GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 1L),
                    ItemList.Electric_Motor_UV.get(1, new Object() {}),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 8L),
                    ItemList.Gravistar.get(4, new Object() {}),
                    new Object[] {OrePrefixes.circuit.get(Materials.Superconductor), 4},
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Naquadria, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Naquadria, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Naquadria, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NaquadahAlloy, 7L)
                },
                new FluidStack[] {Materials.Naquadria.getMolten(1296), new FluidStack(solderIndalloy, 2304)},
                ItemList.Sensor_UV.get(1),
                600,
                100000);

        // Field Generators
        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Field_Generator_IV.get(1, new Object() {}),
                144000,
                new Object[] {
                    GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.HSSS, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.HSSS, 6L),
                    ItemList.QuantumStar.get(2, new Object() {}),
                    ItemList.Emitter_LuV.get(4, new Object() {}),
                    new Object[] {OrePrefixes.circuit.get(Materials.Ultimate), 4},
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Osmiridium, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Osmiridium, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Osmiridium, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Osmiridium, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.YttriumBariumCuprate, 8L)
                },
                new FluidStack[] {new FluidStack(solderIndalloy, 576)},
                ItemList.Field_Generator_LuV.get(1),
                600,
                6000);

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Field_Generator_LuV.get(1, new Object() {}),
                144000,
                new Object[] {
                    GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.NaquadahAlloy, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NaquadahAlloy, 6L),
                    ItemList.QuantumStar.get(2, new Object() {}),
                    ItemList.Emitter_ZPM.get(4, new Object() {}),
                    new Object[] {OrePrefixes.circuit.get(Materials.Superconductor), 4},
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Europium, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Europium, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Europium, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Europium, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.VanadiumGallium, 8L)
                },
                new FluidStack[] {new FluidStack(solderIndalloy, 1152)},
                ItemList.Field_Generator_ZPM.get(1),
                600,
                24000);

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Field_Generator_ZPM.get(1, new Object() {}),
                288000,
                new Object[] {
                    GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 6L),
                    ItemList.Gravistar.get(2, new Object() {}),
                    ItemList.Emitter_UV.get(4, new Object() {}),
                    new Object[] {OrePrefixes.circuit.get(Materials.Infinite), 4},
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NaquadahAlloy, 8L)
                },
                new FluidStack[] {Materials.Naquadria.getMolten(1296), new FluidStack(solderIndalloy, 2304)},
                ItemList.Field_Generator_UV.get(1),
                600,
                100000);

        // Energy Hatches Luv-UV
        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Hatch_Energy_IV.get(1, new Object() {}),
                72000,
                new Object[] {
                    ItemList.Hull_LuV.get(1L, new Object() {}),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 2L),
                    ItemList.Circuit_Chip_UHPIC.get(2L, new Object() {}),
                    new Object[] {OrePrefixes.circuit.get(Materials.Master), 2},
                    ItemList.LuV_Coil.get(2L, new Object() {}),
                    new ItemStack[] {
                        ItemList.Reactor_Coolant_He_3.get(1, new Object() {}),
                        ItemList.Reactor_Coolant_NaK_3.get(1, new Object() {}),
                        ItemList.Reactor_Coolant_Sp_1.get(1, new Object() {})
                    },
                    new ItemStack[] {
                        ItemList.Reactor_Coolant_He_3.get(1, new Object() {}),
                        ItemList.Reactor_Coolant_NaK_3.get(1, new Object() {}),
                        ItemList.Reactor_Coolant_Sp_1.get(1, new Object() {})
                    },
                    ItemList.Electric_Pump_LuV.get(1L, new Object() {})
                },
                new FluidStack[] {
                    new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000), new FluidStack(solderIndalloy, 720)
                },
                ItemList.Hatch_Energy_LuV.get(1),
                400,
                30720);

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Hatch_Energy_LuV.get(1, new Object() {}),
                144000,
                new Object[] {
                    ItemList.Hull_ZPM.get(1L, new Object() {}),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorZPM, 2L),
                    ItemList.Circuit_Chip_NPIC.get(2L, new Object() {}),
                    new Object[] {OrePrefixes.circuit.get(Materials.Ultimate), 2},
                    ItemList.ZPM_Coil.get(2L, new Object() {}),
                    new ItemStack[] {
                        ItemList.Reactor_Coolant_He_6.get(1, new Object() {}),
                        ItemList.Reactor_Coolant_NaK_6.get(1, new Object() {}),
                        ItemList.Reactor_Coolant_Sp_2.get(1, new Object() {})
                    },
                    new ItemStack[] {
                        ItemList.Reactor_Coolant_He_6.get(1, new Object() {}),
                        ItemList.Reactor_Coolant_NaK_6.get(1, new Object() {}),
                        ItemList.Reactor_Coolant_Sp_2.get(1, new Object() {})
                    },
                    ItemList.Electric_Pump_ZPM.get(1L, new Object() {})
                },
                new FluidStack[] {
                    new FluidStack(FluidRegistry.getFluid("ic2coolant"), 4000), new FluidStack(solderIndalloy, 1440)
                },
                ItemList.Hatch_Energy_ZPM.get(1),
                600,
                122880);

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Hatch_Energy_ZPM.get(1, new Object() {}),
                288000,
                new Object[] {
                    ItemList.Hull_UV.get(1L, new Object() {}),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUV, 2L),
                    ItemList.Circuit_Chip_PPIC.get(2L, new Object() {}),
                    new Object[] {OrePrefixes.circuit.get(Materials.Superconductor), 2},
                    ItemList.UV_Coil.get(2L, new Object() {}),
                    new ItemStack[] {
                        ItemList.Reactor_Coolant_He_6.get(1, new Object() {}),
                        ItemList.Reactor_Coolant_NaK_6.get(1, new Object() {}),
                        ItemList.Reactor_Coolant_Sp_2.get(1, new Object() {})
                    },
                    new ItemStack[] {
                        ItemList.Reactor_Coolant_He_6.get(1, new Object() {}),
                        ItemList.Reactor_Coolant_NaK_6.get(1, new Object() {}),
                        ItemList.Reactor_Coolant_Sp_2.get(1, new Object() {})
                    },
                    new ItemStack[] {
                        ItemList.Reactor_Coolant_He_6.get(1, new Object() {}),
                        ItemList.Reactor_Coolant_NaK_6.get(1, new Object() {}),
                        ItemList.Reactor_Coolant_Sp_2.get(1, new Object() {})
                    },
                    new ItemStack[] {
                        ItemList.Reactor_Coolant_He_6.get(1, new Object() {}),
                        ItemList.Reactor_Coolant_NaK_6.get(1, new Object() {}),
                        ItemList.Reactor_Coolant_Sp_2.get(1, new Object() {})
                    },
                    ItemList.Electric_Pump_UV.get(1L, new Object() {})
                },
                new FluidStack[] {
                    new FluidStack(FluidRegistry.getFluid("ic2coolant"), 8000), new FluidStack(solderIndalloy, 2880)
                },
                ItemList.Hatch_Energy_UV.get(1),
                800,
                500000);

        // Dynamo Hatches Luv-UV
        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Hatch_Dynamo_IV.get(1, new Object() {}),
                72000,
                new Object[] {
                    ItemList.Hull_LuV.get(1L, new Object() {}),
                    GT_OreDictUnificator.get(
                            OrePrefixes.spring,
                            Materials.Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid,
                            2L),
                    ItemList.Circuit_Chip_UHPIC.get(2L, new Object() {}),
                    new Object[] {OrePrefixes.circuit.get(Materials.Master), 2},
                    ItemList.LuV_Coil.get(2L, new Object() {}),
                    new ItemStack[] {
                        ItemList.Reactor_Coolant_He_3.get(1, new Object() {}),
                        ItemList.Reactor_Coolant_NaK_3.get(1, new Object() {}),
                        ItemList.Reactor_Coolant_Sp_1.get(1, new Object() {})
                    },
                    new ItemStack[] {
                        ItemList.Reactor_Coolant_He_3.get(1, new Object() {}),
                        ItemList.Reactor_Coolant_NaK_3.get(1, new Object() {}),
                        ItemList.Reactor_Coolant_Sp_1.get(1, new Object() {})
                    },
                    ItemList.Electric_Pump_LuV.get(1L, new Object() {})
                },
                new FluidStack[] {
                    new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000), new FluidStack(solderIndalloy, 720)
                },
                ItemList.Hatch_Dynamo_LuV.get(1),
                400,
                30720);

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Hatch_Dynamo_LuV.get(1, new Object() {}),
                144000,
                new Object[] {
                    ItemList.Hull_ZPM.get(1L, new Object() {}),
                    GT_OreDictUnificator.get(OrePrefixes.spring, Materials.Tetranaquadahdiindiumhexaplatiumosminid, 4L),
                    ItemList.Circuit_Chip_NPIC.get(2L, new Object() {}),
                    new Object[] {OrePrefixes.circuit.get(Materials.Ultimate), 2},
                    ItemList.ZPM_Coil.get(2L, new Object() {}),
                    new ItemStack[] {
                        ItemList.Reactor_Coolant_He_6.get(1, new Object() {}),
                        ItemList.Reactor_Coolant_NaK_6.get(1, new Object() {}),
                        ItemList.Reactor_Coolant_Sp_2.get(1, new Object() {})
                    },
                    new ItemStack[] {
                        ItemList.Reactor_Coolant_He_6.get(1, new Object() {}),
                        ItemList.Reactor_Coolant_NaK_6.get(1, new Object() {}),
                        ItemList.Reactor_Coolant_Sp_2.get(1, new Object() {})
                    },
                    ItemList.Electric_Pump_ZPM.get(1L, new Object() {})
                },
                new FluidStack[] {
                    new FluidStack(FluidRegistry.getFluid("ic2coolant"), 4000), new FluidStack(solderIndalloy, 1440)
                },
                ItemList.Hatch_Dynamo_ZPM.get(1),
                600,
                122880);

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Hatch_Dynamo_ZPM.get(1, new Object() {}),
                288000,
                new Object[] {
                    ItemList.Hull_UV.get(1L, new Object() {}),
                    GT_OreDictUnificator.get(OrePrefixes.spring, Materials.Longasssuperconductornameforuvwire, 4L),
                    ItemList.Circuit_Chip_PPIC.get(2L, new Object() {}),
                    new Object[] {OrePrefixes.circuit.get(Materials.Superconductor), 2},
                    ItemList.UV_Coil.get(2L, new Object() {}),
                    new ItemStack[] {
                        ItemList.Reactor_Coolant_He_6.get(1, new Object() {}),
                        ItemList.Reactor_Coolant_NaK_6.get(1, new Object() {}),
                        ItemList.Reactor_Coolant_Sp_2.get(1, new Object() {})
                    },
                    new ItemStack[] {
                        ItemList.Reactor_Coolant_He_6.get(1, new Object() {}),
                        ItemList.Reactor_Coolant_NaK_6.get(1, new Object() {}),
                        ItemList.Reactor_Coolant_Sp_2.get(1, new Object() {})
                    },
                    new ItemStack[] {
                        ItemList.Reactor_Coolant_He_6.get(1, new Object() {}),
                        ItemList.Reactor_Coolant_NaK_6.get(1, new Object() {}),
                        ItemList.Reactor_Coolant_Sp_2.get(1, new Object() {})
                    },
                    new ItemStack[] {
                        ItemList.Reactor_Coolant_He_6.get(1, new Object() {}),
                        ItemList.Reactor_Coolant_NaK_6.get(1, new Object() {}),
                        ItemList.Reactor_Coolant_Sp_2.get(1, new Object() {})
                    },
                    ItemList.Electric_Pump_UV.get(1L, new Object() {})
                },
                new FluidStack[] {
                    new FluidStack(FluidRegistry.getFluid("ic2coolant"), 8000), new FluidStack(solderIndalloy, 2880)
                },
                ItemList.Hatch_Dynamo_UV.get(1),
                800,
                500000);

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Energy_LapotronicOrb2.get(1),
                288000,
                new Object[] {
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Europium, 16L),
                    new Object[] {OrePrefixes.circuit.get(Materials.Ultimate), 1},
                    new Object[] {OrePrefixes.circuit.get(Materials.Ultimate), 1},
                    new Object[] {OrePrefixes.circuit.get(Materials.Ultimate), 1},
                    new Object[] {OrePrefixes.circuit.get(Materials.Ultimate), 1},
                    ItemList.Energy_LapotronicOrb2.get(8L),
                    ItemList.Field_Generator_LuV.get(2),
                    ItemList.Circuit_Wafer_SoC2.get(64),
                    ItemList.Circuit_Wafer_SoC2.get(64),
                    ItemList.Circuit_Parts_DiodeASMD.get(8),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Naquadah, 32)
                },
                new FluidStack[] {
                    new FluidStack(solderIndalloy, 2880), new FluidStack(FluidRegistry.getFluid("ic2coolant"), 16000)
                },
                ItemList.Energy_Module.get(1),
                2000,
                100000);

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Energy_Module.get(1),
                288000,
                new Object[] {
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Americium, 32L),
                    new Object[] {OrePrefixes.circuit.get(Materials.Superconductor), 1},
                    new Object[] {OrePrefixes.circuit.get(Materials.Superconductor), 1},
                    new Object[] {OrePrefixes.circuit.get(Materials.Superconductor), 1},
                    new Object[] {OrePrefixes.circuit.get(Materials.Superconductor), 1},
                    ItemList.Energy_Module.get(8L),
                    ItemList.Field_Generator_ZPM.get(2),
                    ItemList.Circuit_Wafer_HPIC.get(64),
                    ItemList.Circuit_Wafer_HPIC.get(64),
                    ItemList.Circuit_Parts_DiodeASMD.get(16),
                    GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.NaquadahAlloy, 32),
                },
                new FluidStack[] {
                    new FluidStack(solderIndalloy, 2880), new FluidStack(FluidRegistry.getFluid("ic2coolant"), 16000)
                },
                ItemList.Energy_Cluster.get(1),
                2000,
                200000);

        GT_Values.RA.addAssemblylineRecipe(
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 1),
                144000,
                new Object[] {
                    ItemList.Casing_Fusion_Coil.get(1),
                    new Object[] {OrePrefixes.circuit.get(Materials.Ultimate), 1},
                    new Object[] {OrePrefixes.circuit.get(Materials.Ultimate), 1},
                    new Object[] {OrePrefixes.circuit.get(Materials.Ultimate), 1},
                    new Object[] {OrePrefixes.circuit.get(Materials.Ultimate), 1},
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.NaquadahAlloy, 4L),
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Osmiridium, 4L),
                    ItemList.Field_Generator_LuV.get(2),
                    ItemList.Circuit_Wafer_UHPIC.get(32),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 32),
                },
                new FluidStack[] {
                    new FluidStack(solderIndalloy, 2880), Materials.VanadiumGallium.getMolten(1152L),
                },
                ItemList.FusionComputer_LuV.get(1),
                1000,
                30000);

        GT_Values.RA.addAssemblylineRecipe(
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Europium, 1),
                288000,
                new Object[] {
                    ItemList.Casing_Fusion_Coil.get(1),
                    new Object[] {OrePrefixes.circuit.get(Materials.Superconductor), 1},
                    new Object[] {OrePrefixes.circuit.get(Materials.Superconductor), 1},
                    new Object[] {OrePrefixes.circuit.get(Materials.Superconductor), 1},
                    new Object[] {OrePrefixes.circuit.get(Materials.Superconductor), 1},
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Europium, 4L),
                    ItemList.Field_Generator_ZPM.get(2),
                    ItemList.Circuit_Wafer_PPIC.get(48),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorZPM, 32),
                },
                new FluidStack[] {
                    new FluidStack(solderIndalloy, 2880), Materials.NiobiumTitanium.getMolten(1152L),
                },
                ItemList.FusionComputer_ZPMV.get(1),
                1000,
                60000);

        GT_Values.RA.addAssemblylineRecipe(
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Americium, 1),
                432000,
                new Object[] {
                    ItemList.Casing_Fusion_Coil.get(1),
                    new Object[] {OrePrefixes.circuit.get(Materials.Infinite), 1},
                    new Object[] {OrePrefixes.circuit.get(Materials.Infinite), 1},
                    new Object[] {OrePrefixes.circuit.get(Materials.Infinite), 1},
                    new Object[] {OrePrefixes.circuit.get(Materials.Infinite), 1},
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Americium, 4L),
                    ItemList.Field_Generator_UV.get(2),
                    ItemList.Circuit_Wafer_QPIC.get(64),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUV, 32),
                },
                new FluidStack[] {
                    new FluidStack(solderIndalloy, 2880), Materials.ElectrumFlux.getMolten(1152L),
                },
                ItemList.FusionComputer_UV.get(1),
                1000,
                90000);

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Machine_IV_OreWasher.get(1),
                432000,
                new Object[] {
                    ItemList.Hull_MAX.get(1L),
                    ItemList.Electric_Motor_UHV.get(32L),
                    ItemList.Electric_Piston_UHV.get(8L),
                    ItemList.Electric_Pump_UHV.get(16L),
                    ItemList.Conveyor_Module_UHV.get(8L),
                    ItemList.Robot_Arm_UHV.get(8L),
                    new Object[] {OrePrefixes.circuit.get(Materials.Bio), 4},
                    new ItemStack[] {
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Duranium, 32),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 32)
                    },
                    GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Polybenzimidazole, 64),
                    new ItemStack[] {
                        ItemList.Component_Grinder_Tungsten.get(4L), ItemList.Component_Grinder_Diamond.get(64L)
                    },
                    GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.StainlessSteel, 32),
                    GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Chrome, 16)
                },
                new FluidStack[] {new FluidStack(solderIndalloy, 2880), Materials.Naquadria.getMolten(1440)},
                ItemList.Ore_Processor.get(1),
                1200,
                900000);

        GT_NaniteChain.run();
        GT_PCBFactoryRecipes.load();

        if (GregTech_API.sThaumcraftCompat != null) {
            String tKey = "GT_WOOD_TO_CHARCOAL";
            GT_LanguageManager.addStringLocalization(
                    GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                    "You have discovered a way of making charcoal magically instead of using regular ovens for this purpose.<BR><BR>To create charcoal from wood you first need an air-free environment, some vacuus essentia is needed for that, then you need to incinerate the wood using ignis essentia and wait until all the water inside the wood is burned away.<BR><BR>This method however doesn't create creosote oil as byproduct.");

            GregTech_API.sThaumcraftCompat.addResearch(
                    tKey,
                    "Charcoal Transmutation",
                    "Turning wood into charcoal",
                    new String[] {"ALUMENTUM"},
                    "ALCHEMY",
                    GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 1L),
                    2,
                    0,
                    13,
                    5,
                    Arrays.asList(
                            new TC_Aspects.TC_AspectStack(TC_Aspects.ARBOR, 10L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 8L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 8L)),
                    null,
                    new Object[] {
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                tKey,
                                OrePrefixes.log.get(Materials.Wood),
                                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 1L),
                                Arrays.asList(
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 2L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 1L)))
                    });

            tKey = "GT_FILL_WATER_BUCKET";
            GT_LanguageManager.addStringLocalization(
                    GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                    "You have discovered a way of filling a bucket with aqua essentia in order to simply get water.");
            GregTech_API.sThaumcraftCompat.addResearch(
                    tKey,
                    "Water Transmutation",
                    "Filling buckets with water",
                    null,
                    "ALCHEMY",
                    GT_OreDictUnificator.get(OrePrefixes.bucket, Materials.Water, 1L),
                    2,
                    0,
                    16,
                    5,
                    Arrays.asList(
                            new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 4L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 4L)),
                    null,
                    new Object[] {
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                tKey,
                                GT_OreDictUnificator.get(OrePrefixes.bucket, Materials.Empty, 1L),
                                GT_OreDictUnificator.get(OrePrefixes.bucket, Materials.Water, 1L),
                                Collections.singletonList(new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 4L))),
                        GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                tKey,
                                GT_OreDictUnificator.get(OrePrefixes.bucketClay, Materials.Empty, 1L),
                                GT_OreDictUnificator.get(OrePrefixes.bucketClay, Materials.Water, 1L),
                                Collections.singletonList(new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 4L))),
                        GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                tKey,
                                GT_OreDictUnificator.get(OrePrefixes.capsule, Materials.Empty, 1L),
                                GT_OreDictUnificator.get(OrePrefixes.capsule, Materials.Water, 1L),
                                Collections.singletonList(new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 4L))),
                        GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                tKey,
                                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1L),
                                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1L),
                                Collections.singletonList(new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 4L)))
                    });

            tKey = "GT_TRANSZINC";
            GT_LanguageManager.addStringLocalization(
                    GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                    "You have discovered a way to multiply zinc by steeping zinc nuggets in metallum harvested from other metals.");
            GregTech_API.sThaumcraftCompat.addResearch(
                    tKey,
                    "Zinc Transmutation",
                    "Transformation of metals into zinc",
                    new String[] {"TRANSTIN"},
                    "ALCHEMY",
                    GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Zinc, 1L),
                    2,
                    1,
                    9,
                    13,
                    Arrays.asList(
                            new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.SANO, 3L)),
                    null,
                    new Object[] {
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                tKey,
                                OrePrefixes.nugget.get(Materials.Zinc),
                                GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Zinc, 3L),
                                Arrays.asList(
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.SANO, 1L)))
                    });

            tKey = "GT_TRANSANTIMONY";
            GT_LanguageManager.addStringLocalization(
                    GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                    "You have discovered a way to multiply antimony by steeping antimony nuggets in metallum harvested from other metals.");
            GregTech_API.sThaumcraftCompat.addResearch(
                    tKey,
                    "Antimony Transmutation",
                    "Transformation of metals into antimony",
                    new String[] {"GT_TRANSZINC", "TRANSLEAD"},
                    "ALCHEMY",
                    GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Antimony, 1L),
                    2,
                    1,
                    9,
                    14,
                    Arrays.asList(
                            new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 3L)),
                    null,
                    new Object[] {
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                tKey,
                                OrePrefixes.nugget.get(Materials.Antimony),
                                GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Antimony, 3L),
                                Arrays.asList(
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 1L)))
                    });

            tKey = "GT_TRANSNICKEL";
            GT_LanguageManager.addStringLocalization(
                    GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                    "You have discovered a way to multiply nickel by steeping nickel nuggets in metallum harvested from other metals.");
            GregTech_API.sThaumcraftCompat.addResearch(
                    tKey,
                    "Nickel Transmutation",
                    "Transformation of metals into nickel",
                    new String[] {"TRANSLEAD"},
                    "ALCHEMY",
                    GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Nickel, 1L),
                    2,
                    1,
                    9,
                    15,
                    Arrays.asList(
                            new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 3L)),
                    null,
                    new Object[] {
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                tKey,
                                OrePrefixes.nugget.get(Materials.Nickel),
                                GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Nickel, 3L),
                                Arrays.asList(
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 1L)))
                    });

            tKey = "GT_TRANSCOBALT";
            GT_LanguageManager.addStringLocalization(
                    GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                    "You have discovered a way to multiply cobalt by steeping cobalt nuggets in metallum harvested from other metals.");
            GregTech_API.sThaumcraftCompat.addResearch(
                    tKey,
                    "Cobalt Transmutation",
                    "Transformation of metals into cobalt",
                    new String[] {"GT_TRANSNICKEL"},
                    "ALCHEMY",
                    GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Cobalt, 1L),
                    2,
                    1,
                    9,
                    16,
                    Arrays.asList(
                            new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 3L)),
                    null,
                    new Object[] {
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                tKey,
                                OrePrefixes.nugget.get(Materials.Cobalt),
                                GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Cobalt, 3L),
                                Arrays.asList(
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 1L)))
                    });

            tKey = "GT_TRANSBISMUTH";
            GT_LanguageManager.addStringLocalization(
                    GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                    "You have discovered a way to multiply bismuth by steeping bismuth nuggets in metallum harvested from other metals.");
            GregTech_API.sThaumcraftCompat.addResearch(
                    tKey,
                    "Bismuth Transmutation",
                    "Transformation of metals into bismuth",
                    new String[] {"GT_TRANSCOBALT"},
                    "ALCHEMY",
                    GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Bismuth, 1L),
                    2,
                    1,
                    11,
                    17,
                    Arrays.asList(
                            new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 3L)),
                    null,
                    new Object[] {
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                tKey,
                                OrePrefixes.nugget.get(Materials.Bismuth),
                                GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Bismuth, 3L),
                                Arrays.asList(
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 1L)))
                    });

            tKey = "GT_IRON_TO_STEEL";
            GT_LanguageManager.addStringLocalization(
                    GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                    "You have discovered a way of making Iron harder by just re-ordering its components.<BR><BR>This Method can be used to create a Material called Steel, which is used in many non-Thaumaturgic applications.");
            GregTech_API.sThaumcraftCompat.addResearch(
                    tKey,
                    "Steel Transmutation",
                    "Transforming iron to steel",
                    new String[] {"TRANSIRON", "GT_WOOD_TO_CHARCOAL"},
                    "ALCHEMY",
                    GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Steel, 1L),
                    3,
                    0,
                    13,
                    8,
                    Arrays.asList(
                            new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 3L)),
                    null,
                    new Object[] {
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                tKey,
                                OrePrefixes.nugget.get(Materials.Iron),
                                GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Steel, 1L),
                                Collections.singletonList(new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 1L)))
                    });

            tKey = "GT_TRANSBRONZE";
            GT_LanguageManager.addStringLocalization(
                    GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                    "You have discovered a way of creating Alloys using the already known transmutations of Copper and Tin.<BR><BR>This Method can be used to create a Bronze directly without having to go through an alloying process.");
            GregTech_API.sThaumcraftCompat.addResearch(
                    tKey,
                    "Bronze Transmutation",
                    "Transformation of metals into bronze",
                    new String[] {"TRANSTIN", "TRANSCOPPER"},
                    "ALCHEMY",
                    GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Bronze, 1L),
                    2,
                    0,
                    13,
                    11,
                    Arrays.asList(
                            new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 3L)),
                    null,
                    new Object[] {
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                tKey,
                                OrePrefixes.nugget.get(Materials.Bronze),
                                GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Bronze, 3L),
                                Arrays.asList(
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 1L)))
                    });

            tKey = "GT_TRANSELECTRUM";
            GT_LanguageManager.addStringLocalization(
                    GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                    "Your discovery of Bronze Transmutation has lead you to the conclusion it works with other Alloys such as Electrum as well.");
            GregTech_API.sThaumcraftCompat.addResearch(
                    tKey,
                    "Electrum Transmutation",
                    "Transformation of metals into electrum",
                    new String[] {"GT_TRANSBRONZE", "TRANSGOLD", "TRANSSILVER"},
                    "ALCHEMY",
                    GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Electrum, 1L),
                    2,
                    1,
                    11,
                    11,
                    Arrays.asList(
                            new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.LUCRUM, 3L)),
                    null,
                    new Object[] {
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                tKey,
                                OrePrefixes.nugget.get(Materials.Electrum),
                                GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Electrum, 3L),
                                Arrays.asList(
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.LUCRUM, 1L)))
                    });

            tKey = "GT_TRANSBRASS";
            GT_LanguageManager.addStringLocalization(
                    GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                    "Your discovery of Bronze Transmutation has lead you to the conclusion it works with other Alloys such as Brass as well.");
            GregTech_API.sThaumcraftCompat.addResearch(
                    tKey,
                    "Brass Transmutation",
                    "Transformation of metals into brass",
                    new String[] {"GT_TRANSBRONZE", "GT_TRANSZINC"},
                    "ALCHEMY",
                    GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Brass, 1L),
                    2,
                    1,
                    11,
                    12,
                    Arrays.asList(
                            new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 3L)),
                    null,
                    new Object[] {
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                tKey,
                                OrePrefixes.nugget.get(Materials.Brass),
                                GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Brass, 3L),
                                Arrays.asList(
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 1L)))
                    });

            tKey = "GT_TRANSINVAR";
            GT_LanguageManager.addStringLocalization(
                    GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                    "Your discovery of Bronze Transmutation has lead you to the conclusion it works with other Alloys such as Invar as well.");
            GregTech_API.sThaumcraftCompat.addResearch(
                    tKey,
                    "Invar Transmutation",
                    "Transformation of metals into invar",
                    new String[] {"GT_TRANSBRONZE", "GT_TRANSNICKEL"},
                    "ALCHEMY",
                    GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Invar, 1L),
                    2,
                    1,
                    11,
                    15,
                    Arrays.asList(
                            new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.GELUM, 3L)),
                    null,
                    new Object[] {
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                tKey,
                                OrePrefixes.nugget.get(Materials.Invar),
                                GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Invar, 3L),
                                Arrays.asList(
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.GELUM, 1L)))
                    });

            tKey = "GT_TRANSCUPRONICKEL";
            GT_LanguageManager.addStringLocalization(
                    GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                    "Your discovery of Bronze Transmutation has lead you to the conclusion it works with other Alloys such as Cupronickel as well.");
            GregTech_API.sThaumcraftCompat.addResearch(
                    tKey,
                    "Cupronickel Transmutation",
                    "Transformation of metals into cupronickel",
                    new String[] {"GT_TRANSBRONZE", "GT_TRANSNICKEL"},
                    "ALCHEMY",
                    GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Cupronickel, 1L),
                    2,
                    1,
                    11,
                    16,
                    Arrays.asList(
                            new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 3L)),
                    null,
                    new Object[] {
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                tKey,
                                OrePrefixes.nugget.get(Materials.Cupronickel),
                                GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Cupronickel, 3L),
                                Arrays.asList(
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 1L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 1L)))
                    });

            tKey = "GT_TRANSBATTERYALLOY";
            GT_LanguageManager.addStringLocalization(
                    GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                    "Your discovery of Bronze Transmutation has lead you to the conclusion it works with other Alloys such as Battery Alloy as well.");
            GregTech_API.sThaumcraftCompat.addResearch(
                    tKey,
                    "Battery Alloy Transmutation",
                    "Transformation of metals into battery alloy",
                    new String[] {"GT_TRANSBRONZE", "GT_TRANSANTIMONY"},
                    "ALCHEMY",
                    GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.BatteryAlloy, 1L),
                    2,
                    1,
                    11,
                    13,
                    Arrays.asList(
                            new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 3L)),
                    null,
                    new Object[] {
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                tKey,
                                OrePrefixes.nugget.get(Materials.BatteryAlloy),
                                GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.BatteryAlloy, 3L),
                                Arrays.asList(
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 1L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 1L)))
                    });

            tKey = "GT_TRANSSOLDERINGALLOY";
            GT_LanguageManager.addStringLocalization(
                    GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                    "Your discovery of Bronze Transmutation has lead you to the conclusion it works with other Alloys such as Soldering Alloy as well.");
            GregTech_API.sThaumcraftCompat.addResearch(
                    tKey,
                    "Soldering Alloy Transmutation",
                    "Transformation of metals into soldering alloy",
                    new String[] {"GT_TRANSBRONZE", "GT_TRANSANTIMONY"},
                    "ALCHEMY",
                    GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.SolderingAlloy, 1L),
                    2,
                    1,
                    11,
                    14,
                    Arrays.asList(
                            new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 3L)),
                    null,
                    new Object[] {
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                tKey,
                                OrePrefixes.nugget.get(Materials.SolderingAlloy),
                                GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.SolderingAlloy, 3L),
                                Arrays.asList(
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 1L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 1L)))
                    });

            tKey = "GT_ADVANCEDMETALLURGY";
            GT_LanguageManager.addStringLocalization(
                    GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                    "Now that you have discovered all the basic metals, you can finally move on to the next Level of magic metallurgy and create more advanced metals");
            GregTech_API.sThaumcraftCompat.addResearch(
                    tKey,
                    "Advanced Metallurgic Transmutation",
                    "Mastering the basic metals",
                    new String[] {
                        "GT_TRANSBISMUTH",
                        "GT_IRON_TO_STEEL",
                        "GT_TRANSSOLDERINGALLOY",
                        "GT_TRANSBATTERYALLOY",
                        "GT_TRANSBRASS",
                        "GT_TRANSELECTRUM",
                        "GT_TRANSCUPRONICKEL",
                        "GT_TRANSINVAR"
                    },
                    "ALCHEMY",
                    GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, 1L),
                    3,
                    0,
                    16,
                    14,
                    Arrays.asList(
                            new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 50L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 20L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 20L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.PRAECANTATIO, 20L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.NEBRISUM, 20L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.MAGNETO, 20L)),
                    null,
                    new Object[] {GT_MachineRecipeLoader.aTextTCGTPage + tKey});

            tKey = "GT_TRANSALUMINIUM";
            GT_LanguageManager.addStringLocalization(
                    GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                    "You have discovered a way to multiply aluminium by steeping aluminium nuggets in metallum harvested from other metals.<BR><BR>This transmutation is slightly harder to achieve, because aluminium has special properties, which require more order to achieve the desired result.");
            GregTech_API.sThaumcraftCompat.addResearch(
                    tKey,
                    "Aluminium Transmutation",
                    "Transformation of metals into aluminium",
                    new String[] {"GT_ADVANCEDMETALLURGY"},
                    "ALCHEMY",
                    GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Aluminium, 1L),
                    4,
                    0,
                    19,
                    14,
                    Arrays.asList(
                            new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.VOLATUS, 3L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 3L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 3L)),
                    null,
                    new Object[] {
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                tKey,
                                OrePrefixes.nugget.get(Materials.Aluminium),
                                GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Aluminium, 3L),
                                Arrays.asList(
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.VOLATUS, 1L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 1L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.IGNIS, 1L)))
                    });

            if (Loader.isModLoaded("appliedenergistics2")) {
                tKey = "GT_TRANSSKYSTONE";
                GT_LanguageManager.addStringLocalization(
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        "You have discovered a way to convert obsidian to skystone.<BR><BR>Not sure why you'd want to do this, unless skystone is somehow unavailable in your world.");
                GregTech_API.sThaumcraftCompat.addResearch(
                        tKey,
                        "Skystone Transmutation",
                        "Transformation of obsidian into skystone",
                        new String[] {"GT_ADVANCEDMETALLURGY"},
                        "ALCHEMY",
                        getModItem("appliedenergistics2", "tile.BlockSkyStone", 1),
                        4,
                        0,
                        19,
                        15,
                        Arrays.asList(
                                new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.VOLATUS, 3L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.ALIENIS, 3L),
                                new TC_Aspects.TC_AspectStack(TC_Aspects.TERRA, 3L)),
                        null,
                        new Object[] {
                            GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                            GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                    tKey,
                                    new ItemStack(Blocks.obsidian),
                                    getModItem("appliedenergistics2", "tile.BlockSkyStone", 1),
                                    Arrays.asList(
                                            new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 2L),
                                            new TC_Aspects.TC_AspectStack(TC_Aspects.VOLATUS, 1L),
                                            new TC_Aspects.TC_AspectStack(TC_Aspects.TERRA, 1L),
                                            new TC_Aspects.TC_AspectStack(TC_Aspects.ALIENIS, 2L),
                                            new TC_Aspects.TC_AspectStack(TC_Aspects.TENEBRAE, 1L)))
                        });
            }

            tKey = "GT_TRANSMINERAL";
            GT_LanguageManager.addStringLocalization(
                    GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                    "You have discovered a way to convert basaltic mineral sand to granitic mineral sand and vice versa.<BR><BR>Handy for people living in the sky who can't access it normally, or if you really want one or the other.");
            GregTech_API.sThaumcraftCompat.addResearch(
                    tKey,
                    "Basaltic Mineral Transmutation",
                    "Transformation of mineral sands",
                    new String[] {"GT_ADVANCEDMETALLURGY"},
                    "ALCHEMY",
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.GraniticMineralSand, 1L),
                    4,
                    0,
                    19,
                    16,
                    Arrays.asList(
                            new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 5L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.VOLATUS, 3L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.MAGNETO, 3L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.TERRA, 3L)),
                    null,
                    new Object[] {
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                tKey,
                                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.BasalticMineralSand, 1L),
                                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.GraniticMineralSand, 1L),
                                Arrays.asList(
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 1L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 1L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.MAGNETO, 1L))),
                        GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                tKey,
                                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.GraniticMineralSand, 1L),
                                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.BasalticMineralSand, 1L),
                                Arrays.asList(
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 2L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 1L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 1L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.MAGNETO, 1L)))
                    });

            tKey = "GT_CRYSTALLISATION";
            GT_LanguageManager.addStringLocalization(
                    GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                    "Sometimes when processing your Crystal Shards they become a pile of Dust instead of the mostly required Shard.<BR><BR>You have finally found a way to reverse this Process by using Vitreus Essentia for recrystallising the Shards.");
            GregTech_API.sThaumcraftCompat.addResearch(
                    tKey,
                    "Shard Recrystallisation",
                    "Fixing your precious crystals",
                    new String[] {"ALCHEMICALMANUFACTURE"},
                    "ALCHEMY",
                    GT_OreDictUnificator.get(OrePrefixes.gem, Materials.InfusedOrder, 1L),
                    3,
                    0,
                    -11,
                    -3,
                    Arrays.asList(
                            new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 5L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 3L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 3L)),
                    null,
                    new Object[] {
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                tKey,
                                OrePrefixes.dust.get(Materials.Amber),
                                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Amber, 1L),
                                Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 4L))),
                        GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                tKey,
                                OrePrefixes.dust.get(Materials.InfusedOrder),
                                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.InfusedOrder, 1L),
                                Collections.singletonList(new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 4L))),
                        GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                tKey,
                                OrePrefixes.dust.get(Materials.InfusedEntropy),
                                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.InfusedEntropy, 1L),
                                Collections.singletonList(new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 4L))),
                        GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                tKey,
                                OrePrefixes.dust.get(Materials.InfusedAir),
                                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.InfusedAir, 1L),
                                Collections.singletonList(new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 4L))),
                        GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                tKey,
                                OrePrefixes.dust.get(Materials.InfusedEarth),
                                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.InfusedEarth, 1L),
                                Collections.singletonList(new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 4L))),
                        GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                tKey,
                                OrePrefixes.dust.get(Materials.InfusedFire),
                                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.InfusedFire, 1L),
                                Collections.singletonList(new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 4L))),
                        GregTech_API.sThaumcraftCompat.addCrucibleRecipe(
                                tKey,
                                OrePrefixes.dust.get(Materials.InfusedWater),
                                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.InfusedWater, 1L),
                                Collections.singletonList(new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 4L)))
                    });

            tKey = "GT_MAGICENERGY";
            GT_LanguageManager.addStringLocalization(
                    GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                    "While trying to find new ways to integrate magic into your industrial factories, you have discovered a way to convert magical energy into electrical power.");
            GregTech_API.sThaumcraftCompat.addResearch(
                    tKey,
                    "Magic Energy Conversion",
                    "Magic to Power",
                    new String[] {"ARCANEBORE"},
                    "ARTIFICE",
                    ItemList.MagicEnergyConverter_LV.get(1L),
                    3,
                    0,
                    -3,
                    10,
                    Arrays.asList(
                            new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 10L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 10L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 20L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 10L)),
                    null,
                    new Object[] {
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        GregTech_API.sThaumcraftCompat.addInfusionRecipe(
                                tKey,
                                ItemList.Hull_LV.get(1L),
                                new ItemStack[] {
                                    new ItemStack(Blocks.beacon),
                                    GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Good, 1L),
                                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Aluminium, 1L),
                                    ItemList.Sensor_MV.get(2L),
                                    GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Good, 1L),
                                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Thaumium, 1L),
                                    ItemList.Sensor_MV.get(2L)
                                },
                                ItemList.MagicEnergyConverter_LV.get(1L),
                                5,
                                Arrays.asList(
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 32L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 16L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 32L)))
                    });

            tKey = "GT_MAGICENERGY2";
            GT_LanguageManager.addStringLocalization(
                    GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                    "Attempts to increase the output of your Magic Energy generators have resulted in significant improvements.");
            GregTech_API.sThaumcraftCompat.addResearch(
                    tKey,
                    "Adept Magic Energy Conversion",
                    "Magic to Power",
                    new String[] {"GT_MAGICENERGY"},
                    "ARTIFICE",
                    ItemList.MagicEnergyConverter_MV.get(1L),
                    1,
                    1,
                    -4,
                    12,
                    Arrays.asList(
                            new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 10L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 10L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 20L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 10L)),
                    null,
                    new Object[] {
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        GregTech_API.sThaumcraftCompat.addInfusionRecipe(
                                tKey,
                                ItemList.Hull_MV.get(1L),
                                new ItemStack[] {
                                    new ItemStack(Blocks.beacon),
                                    GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 1L),
                                    GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Thaumium, 1L),
                                    ItemList.Sensor_HV.get(2L),
                                    GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 1L),
                                    GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.StainlessSteel, 1L),
                                    ItemList.Sensor_HV.get(2L)
                                },
                                ItemList.MagicEnergyConverter_MV.get(1L),
                                6,
                                Arrays.asList(
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 64L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 32L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 64L)))
                    });

            tKey = "GT_MAGICENERGY3";
            GT_LanguageManager.addStringLocalization(
                    GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                    "Attempts to further increase the output of your Magic Energy generators have resulted in great improvements.");
            GregTech_API.sThaumcraftCompat.addResearch(
                    tKey,
                    "Master Magic Energy Conversion",
                    "Magic to Power",
                    new String[] {"GT_MAGICENERGY2"},
                    "ARTIFICE",
                    ItemList.MagicEnergyConverter_HV.get(1L),
                    1,
                    1,
                    -4,
                    14,
                    Arrays.asList(
                            new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 20L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 20L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 40L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 20L)),
                    null,
                    new Object[] {
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        GregTech_API.sThaumcraftCompat.addInfusionRecipe(
                                tKey,
                                ItemList.Hull_HV.get(1L),
                                new ItemStack[] {
                                    new ItemStack(Blocks.beacon),
                                    GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Data, 1L),
                                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Thaumium, 1L),
                                    ItemList.Field_Generator_MV.get(1L),
                                    GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Data, 1L),
                                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Titanium, 1L),
                                    ItemList.Field_Generator_MV.get(1L)
                                },
                                ItemList.MagicEnergyConverter_HV.get(1L),
                                8,
                                Arrays.asList(
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 128L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 128L)))
                    });

            tKey = "GT_MAGICABSORB";
            GT_LanguageManager.addStringLocalization(
                    GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                    "Research into magical energy conversion methods has identified a way to convert surrounding energies into electrical power.");
            GregTech_API.sThaumcraftCompat.addResearch(
                    tKey,
                    "Magic Energy Absorption",
                    "Harvesting Magic",
                    new String[] {"GT_MAGICENERGY"},
                    "ARTIFICE",
                    ItemList.MagicEnergyAbsorber_LV.get(1L),
                    3,
                    0,
                    -2,
                    12,
                    Arrays.asList(
                            new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 10L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 10L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 20L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 10L)),
                    null,
                    new Object[] {
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        GregTech_API.sThaumcraftCompat.addInfusionRecipe(
                                tKey,
                                ItemList.Hull_LV.get(1L),
                                new ItemStack[] {
                                    ItemList.MagicEnergyConverter_LV.get(1L),
                                    GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Good, 1L),
                                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Thaumium, 1L),
                                    ItemList.Sensor_MV.get(2L)
                                },
                                ItemList.MagicEnergyAbsorber_LV.get(1L),
                                6,
                                Arrays.asList(
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 32L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 16L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 32L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 16L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 32L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.STRONTIO, 4L)))
                    });

            tKey = "GT_MAGICABSORB2";
            GT_LanguageManager.addStringLocalization(
                    GT_MachineRecipeLoader.aTextTCGTPage + tKey, "Moar output! Drain all the Magic!");
            GregTech_API.sThaumcraftCompat.addResearch(
                    tKey,
                    "Improved Magic Energy Absorption",
                    "Harvesting Magic",
                    new String[] {"GT_MAGICABSORB"},
                    "ARTIFICE",
                    ItemList.MagicEnergyAbsorber_EV.get(1L),
                    3,
                    1,
                    -2,
                    14,
                    Arrays.asList(
                            new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 10L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 10L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 20L),
                            new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 10L)),
                    null,
                    new Object[] {
                        GT_MachineRecipeLoader.aTextTCGTPage + tKey,
                        GregTech_API.sThaumcraftCompat.addInfusionRecipe(
                                tKey,
                                ItemList.Hull_MV.get(1L),
                                new ItemStack[] {
                                    ItemList.MagicEnergyConverter_MV.get(1L),
                                    GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 1L),
                                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Thaumium, 1L),
                                    ItemList.Sensor_HV.get(2L),
                                    GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 1L),
                                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Thaumium, 1L)
                                },
                                ItemList.MagicEnergyAbsorber_MV.get(1L),
                                6,
                                Arrays.asList(
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 64L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 32L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 64L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 32L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 64L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.STRONTIO, 8L))),
                        GregTech_API.sThaumcraftCompat.addInfusionRecipe(
                                tKey,
                                ItemList.Hull_HV.get(1L),
                                new ItemStack[] {
                                    ItemList.MagicEnergyConverter_MV.get(1L),
                                    GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Data, 1L),
                                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Void, 1),
                                    ItemList.Field_Generator_MV.get(1L),
                                    GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Data, 1L),
                                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Void, 1),
                                },
                                ItemList.MagicEnergyAbsorber_HV.get(1L),
                                8,
                                Arrays.asList(
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 128L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 128L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 64L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 128L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.STRONTIO, 16L))),
                        GregTech_API.sThaumcraftCompat.addInfusionRecipe(
                                tKey,
                                ItemList.Hull_EV.get(1L),
                                new ItemStack[] {
                                    ItemList.MagicEnergyConverter_HV.get(1L),
                                    GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Elite, 1L),
                                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Void, 1),
                                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 1),
                                    ItemList.Field_Generator_HV.get(1L),
                                    GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Elite, 1L),
                                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Void, 1),
                                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 1),
                                },
                                ItemList.MagicEnergyAbsorber_EV.get(1L),
                                10,
                                Arrays.asList(
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 256L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 128L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 256L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 128L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 256L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.STRONTIO, 64L)))
                    });
        }

    }

    /**
     * Adds recipes related to the processing of Charcoal Byproducts, Fermented Biomass
     * Adds recipes related to the production of Advanced Glue, Gunpowder, Polyvinyl Chloride
     * Adds replacement recipes for Epoxy Resin, Nitric Acid, Polyethylene, Polydimethylsiloxane (Silicone), Polytetrafluoroethylene, Rocket Fuel, Sulfuric Acid
     * Instrumental materials are not mentioned here.
     */
    private void addRecipesApril2017ChemistryUpdate() {


        // 2 0.5HCl(Diluted) = HCl + H2O
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.DilutedHydrochloricAcid.getFluid(2000),
                new FluidStack[] {Materials.Water.getFluid(1000), Materials.HydrochloricAcid.getFluid(1000)},
                GT_Values.NI,
                600,
                64);



        // (NaCl·H2O) = NaCl + H2O
        GT_Values.RA.addDistilleryRecipe(
                1,
                Materials.SaltWater.getFluid(1000),
                GT_ModHandler.getDistilledWater(1000),
                Materials.Salt.getDust(2),
                1600,
                30,
                false);

        GT_Values.RA.addUniversalDistillationRecipe(
                getFluidStack("potion.vinegar", 40),
                new FluidStack[] {Materials.AceticAcid.getFluid(5), Materials.Water.getFluid(35)},
                GT_Values.NI,
                20,
                64);


        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.CalciumAcetateSolution.getFluid(1000),
                new FluidStack[] {Materials.Acetone.getFluid(1000), Materials.CarbonDioxide.getGas(1000)},
                Materials.Quicklime.getDust(2),
                80,
                480);



        GT_Values.RA.addFermentingRecipe(
                Materials.Biomass.getFluid(100), Materials.FermentedBiomass.getFluid(100), 150, false);
        GT_Values.RA.addFermentingRecipe(
                new FluidStack(FluidRegistry.getFluid("ic2biomass"), 100),
                Materials.FermentedBiomass.getFluid(100),
                150,
                false);

        GT_Values.RA.addDistillationTowerRecipe(
                Materials.FermentedBiomass.getFluid(1000),
                new FluidStack[] {
                    Materials.AceticAcid.getFluid(25), Materials.Water.getFluid(375), Materials.Ethanol.getFluid(150),
                    Materials.Methanol.getFluid(150), Materials.Ammonia.getGas(100),
                            Materials.CarbonDioxide.getGas(400),
                    Materials.Methane.getGas(600)
                },
                ItemList.IC2_Fertilizer.get(1),
                75,
                180);
        GT_Values.RA.addDistilleryRecipe(
                1,
                Materials.FermentedBiomass.getFluid(1000),
                Materials.AceticAcid.getFluid(25),
                ItemList.IC2_Fertilizer.get(1),
                1500,
                8,
                false);
        GT_Values.RA.addDistilleryRecipe(
                2,
                Materials.FermentedBiomass.getFluid(1000),
                Materials.Water.getFluid(375),
                ItemList.IC2_Fertilizer.get(1),
                1500,
                8,
                false);
        GT_Values.RA.addDistilleryRecipe(
                3,
                Materials.FermentedBiomass.getFluid(1000),
                Materials.Ethanol.getFluid(150),
                ItemList.IC2_Fertilizer.get(1),
                1500,
                8,
                false);
        GT_Values.RA.addDistilleryRecipe(
                4,
                Materials.FermentedBiomass.getFluid(1000),
                Materials.Methanol.getFluid(150),
                ItemList.IC2_Fertilizer.get(1),
                1500,
                8,
                false);
        GT_Values.RA.addDistilleryRecipe(
                5,
                Materials.FermentedBiomass.getFluid(1000),
                Materials.Ammonia.getGas(100),
                ItemList.IC2_Fertilizer.get(1),
                1500,
                8,
                false);
        GT_Values.RA.addDistilleryRecipe(
                6,
                Materials.FermentedBiomass.getFluid(1000),
                Materials.CarbonDioxide.getGas(400),
                ItemList.IC2_Fertilizer.get(1),
                1500,
                8,
                false);
        GT_Values.RA.addDistilleryRecipe(
                7,
                Materials.FermentedBiomass.getFluid(1000),
                Materials.Methane.getGas(600),
                ItemList.IC2_Fertilizer.get(1),
                1500,
                8,
                false);

        GT_Values.RA.addDistilleryRecipe(
                17,
                Materials.FermentedBiomass.getFluid(1000),
                new FluidStack(FluidRegistry.getFluid("ic2biogas"), 1800),
                ItemList.IC2_Fertilizer.get(1),
                1600,
                8,
                false);
        GT_Values.RA.addDistilleryRecipe(
                1,
                Materials.Methane.getGas(1000),
                new FluidStack(FluidRegistry.getFluid("ic2biogas"), 3000),
                GT_Values.NI,
                160,
                8,
                false);

        GT_Values.RA.addUniversalDistillationRecipewithCircuit(
                Materials.CharcoalByproducts.getGas(1000),
                new ItemStack[] {GT_Utility.getIntegratedCircuit(1)},
                new FluidStack[] {
                    Materials.WoodTar.getFluid(250),
                    Materials.WoodVinegar.getFluid(400),
                    Materials.WoodGas.getGas(250),
                    Materials.Dimethylbenzene.getFluid(100)
                },
                Materials.Charcoal.getDustSmall(1),
                40,
                256);

        GT_Values.RA.addUniversalDistillationRecipewithCircuit(
                Materials.WoodGas.getGas(1000),
                new ItemStack[] {GT_Utility.getIntegratedCircuit(1)},
                new FluidStack[] {
                    Materials.CarbonDioxide.getGas(390),
                    Materials.Ethylene.getGas(120),
                    Materials.Methane.getGas(130),
                    Materials.CarbonMonoxide.getGas(240),
                    Materials.Hydrogen.getGas(120)
                },
                GT_Values.NI,
                40,
                256);
        GT_Values.RA.addUniversalDistillationRecipewithCircuit(
                Materials.WoodVinegar.getFluid(1000),
                new ItemStack[] {GT_Utility.getIntegratedCircuit(1)},
                new FluidStack[] {
                    Materials.AceticAcid.getFluid(100),
                    Materials.Water.getFluid(500),
                    Materials.Ethanol.getFluid(10),
                    Materials.Methanol.getFluid(300),
                    Materials.Acetone.getFluid(50),
                    Materials.MethylAcetate.getFluid(10)
                },
                GT_Values.NI,
                40,
                256);
        GT_Values.RA.addUniversalDistillationRecipewithCircuit(
                Materials.WoodTar.getFluid(1000),
                new ItemStack[] {GT_Utility.getIntegratedCircuit(1)},
                new FluidStack[] {
                    Materials.Creosote.getFluid(250),
                    Materials.Phenol.getFluid(100),
                    Materials.Benzene.getFluid(400),
                    Materials.Toluene.getFluid(100),
                    Materials.Dimethylbenzene.getFluid(150)
                },
                GT_Values.NI,
                40,
                256);

        GT_Values.RA.addDefaultPolymerizationRecipes(
                Materials.VinylAcetate.mFluid, Materials.VinylAcetate.getCells(1), Materials.PolyvinylAcetate.mFluid);

        GT_Values.RA.addDefaultPolymerizationRecipes(
                Materials.Ethylene.mGas, Materials.Ethylene.getCells(1), Materials.Plastic.mStandardMoltenFluid);

        GT_Values.RA.addDistilleryRecipe(
                2, Materials.HeavyFuel.getFluid(100), Materials.Benzene.getFluid(40), 160, 24, false);
        GT_Values.RA.addDistilleryRecipe(
                3, Materials.HeavyFuel.getFluid(100), Materials.Phenol.getFluid(25), 160, 24, false);

        GT_Values.RA.addDefaultPolymerizationRecipes(
                Materials.Tetrafluoroethylene.mGas,
                Materials.Tetrafluoroethylene.getCells(1),
                Materials.Polytetrafluoroethylene.mStandardMoltenFluid);

        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.DilutedSulfuricAcid.getFluid(3000),
                new FluidStack[] {Materials.SulfuricAcid.getFluid(2000), Materials.Water.getFluid(1000)},
                GT_Values.NI,
                600,
                120);

        GT_Values.RA.addDefaultPolymerizationRecipes(
                Materials.VinylChloride.mGas,
                Materials.VinylChloride.getCells(1),
                Materials.PolyvinylChloride.mStandardMoltenFluid);

        // C3H6O = C2H2O + CH4
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Acetone.getFluid(1000),
                new FluidStack[] {Materials.Ethenone.getGas(1000), Materials.Methane.getGas(1000)},
                GT_Values.NI,
                80,
                640);

        GT_Values.RA.addDefaultPolymerizationRecipes(
                Materials.Styrene.mFluid, Materials.Styrene.getCells(1), Materials.Polystyrene.mStandardMoltenFluid);



    }

    private void addRecipesMay2017OilRefining() {
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Gas.getGas(1000),
                new FluidStack[] {
                    Materials.Butane.getGas(60),
                    Materials.Propane.getGas(70),
                    Materials.Ethane.getGas(100),
                    Materials.Methane.getGas(750),
                    Materials.Helium.getGas(20)
                },
                GT_Values.NI,
                240,
                120);



        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Ethylene.getLightlyHydroCracked(1000),
                new FluidStack[] {Materials.Ethane.getGas(1000)},
                GT_Values.NI,
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Ethylene.getModeratelyHydroCracked(1000),
                new FluidStack[] {Materials.Methane.getGas(2000)},
                null,
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Ethylene.getSeverelyHydroCracked(1000),
                new FluidStack[] {Materials.Methane.getGas(2000), Materials.Hydrogen.getGas(2000)},
                GT_Values.NI,
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Ethylene.getLightlySteamCracked(1000),
                new FluidStack[] {Materials.Methane.getGas(1000)},
                Materials.Carbon.getDust(1),
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Ethylene.getModeratelySteamCracked(1000),
                new FluidStack[] {Materials.Methane.getGas(1000)},
                Materials.Carbon.getDust(1),
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Ethylene.getSeverelySteamCracked(1000),
                new FluidStack[] {Materials.Methane.getGas(1000)},
                Materials.Carbon.getDust(1),
                120,
                120);

        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Ethane.getLightlyHydroCracked(1000),
                new FluidStack[] {Materials.Methane.getGas(2000)},
                GT_Values.NI,
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Ethane.getModeratelyHydroCracked(1000),
                new FluidStack[] {Materials.Methane.getGas(2000), Materials.Hydrogen.getGas(2000)},
                GT_Values.NI,
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Ethane.getSeverelyHydroCracked(1000),
                new FluidStack[] {Materials.Methane.getGas(2000), Materials.Hydrogen.getGas(4000)},
                GT_Values.NI,
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Ethane.getLightlySteamCracked(1000),
                new FluidStack[] {Materials.Ethylene.getGas(250), Materials.Methane.getGas(1250)},
                Materials.Carbon.getDustSmall(1),
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Ethane.getModeratelySteamCracked(1000),
                new FluidStack[] {Materials.Ethylene.getGas(125), Materials.Methane.getGas(1375)},
                Materials.Carbon.getDustTiny(6),
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Ethane.getSeverelySteamCracked(1000),
                new FluidStack[] {Materials.Methane.getGas(1500)},
                Materials.Carbon.getDustSmall(2),
                120,
                120);

        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Propene.getLightlyHydroCracked(1000),
                new FluidStack[] {
                    Materials.Propane.getGas(500), Materials.Ethylene.getGas(500), Materials.Methane.getGas(500)
                },
                GT_Values.NI,
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Propene.getModeratelyHydroCracked(1000),
                new FluidStack[] {Materials.Ethane.getGas(1000), Materials.Methane.getGas(1000)},
                GT_Values.NI,
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Propene.getSeverelyHydroCracked(1000),
                new FluidStack[] {Materials.Methane.getGas(3000)},
                GT_Values.NI,
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Propene.getLightlySteamCracked(1000),
                new FluidStack[] {Materials.Ethylene.getGas(1000), Materials.Methane.getGas(500)},
                Materials.Carbon.getDustSmall(2),
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Propene.getModeratelySteamCracked(1000),
                new FluidStack[] {Materials.Ethylene.getGas(750), Materials.Methane.getGas(750)},
                Materials.Carbon.getDustSmall(3),
                180,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Propene.getSeverelySteamCracked(1000),
                new FluidStack[] {Materials.Methane.getGas(1500)},
                Materials.Carbon.getDustSmall(6),
                180,
                120);

        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Propane.getLightlyHydroCracked(1000),
                new FluidStack[] {Materials.Ethane.getGas(1000), Materials.Methane.getGas(1000)},
                GT_Values.NI,
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Propane.getModeratelyHydroCracked(1000),
                new FluidStack[] {Materials.Methane.getGas(3000)},
                GT_Values.NI,
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Propane.getSeverelyHydroCracked(1000),
                new FluidStack[] {Materials.Methane.getGas(3000), Materials.Hydrogen.getGas(2000)},
                GT_Values.NI,
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Propane.getLightlySteamCracked(1000),
                new FluidStack[] {Materials.Ethylene.getGas(750), Materials.Methane.getGas(1250)},
                Materials.Carbon.getDustTiny(2),
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Propane.getModeratelySteamCracked(1000),
                new FluidStack[] {Materials.Ethylene.getGas(500), Materials.Methane.getGas(1500)},
                Materials.Carbon.getDustSmall(1),
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Propane.getSeverelySteamCracked(1000),
                new FluidStack[] {Materials.Ethylene.getGas(250), Materials.Methane.getGas(1750)},
                Materials.Carbon.getDustTiny(4),
                120,
                120);

        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Butadiene.getLightlyHydroCracked(1000),
                new FluidStack[] {Materials.Butene.getGas(667), Materials.Ethylene.getGas(667)},
                GT_Values.NI,
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Butadiene.getModeratelyHydroCracked(1000),
                new FluidStack[] {
                    Materials.Butane.getGas(223),
                    Materials.Propene.getGas(223),
                    Materials.Ethane.getGas(400),
                    Materials.Ethylene.getGas(445),
                    Materials.Methane.getGas(223)
                },
                GT_Values.NI,
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Butadiene.getSeverelyHydroCracked(1000),
                new FluidStack[] {
                    Materials.Propane.getGas(260),
                    Materials.Ethane.getGas(926),
                    Materials.Ethylene.getGas(389),
                    Materials.Methane.getGas(2667)
                },
                GT_Values.NI,
                112,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Butadiene.getLightlySteamCracked(1000),
                new FluidStack[] {
                    Materials.Propene.getGas(750), Materials.Ethylene.getGas(188), Materials.Methane.getGas(188)
                },
                Materials.Carbon.getDustSmall(3),
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Butadiene.getModeratelySteamCracked(1000),
                new FluidStack[] {
                    Materials.Propene.getGas(125), Materials.Ethylene.getGas(1125), Materials.Methane.getGas(188)
                },
                Materials.Carbon.getDustSmall(3),
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Butadiene.getSeverelySteamCracked(1000),
                new FluidStack[] {
                    Materials.Propene.getGas(125), Materials.Ethylene.getGas(188), Materials.Methane.getGas(1125)
                },
                Materials.Carbon.getDust(1),
                120,
                120);

        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Butene.getLightlyHydroCracked(1000),
                new FluidStack[] {
                    Materials.Butane.getGas(334),
                    Materials.Propene.getGas(334),
                    Materials.Ethane.getGas(334),
                    Materials.Ethylene.getGas(334),
                    Materials.Methane.getGas(334)
                },
                GT_Values.NI,
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Butene.getModeratelyHydroCracked(1000),
                new FluidStack[] {
                    Materials.Propane.getGas(389),
                    Materials.Ethane.getGas(556),
                    Materials.Ethylene.getGas(334),
                    Materials.Methane.getGas(1056)
                },
                GT_Values.NI,
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Butene.getSeverelyHydroCracked(1000),
                new FluidStack[] {Materials.Ethane.getGas(1000), Materials.Methane.getGas(2000)},
                GT_Values.NI,
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Butene.getLightlySteamCracked(1000),
                new FluidStack[] {
                    Materials.Propene.getGas(750), Materials.Ethylene.getGas(500), Materials.Methane.getGas(250)
                },
                Materials.Carbon.getDustSmall(1),
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Butene.getModeratelySteamCracked(1000),
                new FluidStack[] {
                    Materials.Propene.getGas(200), Materials.Ethylene.getGas(1300), Materials.Methane.getGas(400)
                },
                Materials.Carbon.getDustSmall(1),
                192,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Butene.getSeverelySteamCracked(1000),
                new FluidStack[] {
                    Materials.Propene.getGas(125), Materials.Ethylene.getGas(313), Materials.Methane.getGas(1500)
                },
                Materials.Carbon.getDustSmall(6),
                120,
                120);

        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Butane.getLightlyHydroCracked(1000),
                new FluidStack[] {
                    Materials.Propane.getGas(667), Materials.Ethane.getGas(667), Materials.Methane.getGas(667)
                },
                GT_Values.NI,
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Butane.getModeratelyHydroCracked(1000),
                new FluidStack[] {Materials.Ethane.getGas(1000), Materials.Methane.getGas(2000)},
                GT_Values.NI,
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Butane.getSeverelyHydroCracked(1000),
                new FluidStack[] {Materials.Methane.getGas(1000)},
                GT_Values.NI,
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Butane.getLightlySteamCracked(1000),
                new FluidStack[] {
                    Materials.Propane.getGas(750),
                    Materials.Ethane.getGas(125),
                    Materials.Ethylene.getGas(125),
                    Materials.Methane.getGas(1063)
                },
                Materials.Carbon.getDustTiny(2),
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Butane.getModeratelySteamCracked(1000),
                new FluidStack[] {
                    Materials.Propane.getGas(125),
                    Materials.Ethane.getGas(750),
                    Materials.Ethylene.getGas(750),
                    Materials.Methane.getGas(438)
                },
                Materials.Carbon.getDustTiny(2),
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Butane.getSeverelySteamCracked(1000),
                new FluidStack[] {
                    Materials.Propane.getGas(125),
                    Materials.Ethane.getGas(125),
                    Materials.Ethylene.getGas(125),
                    Materials.Methane.getGas(2000)
                },
                Materials.Carbon.getDustTiny(11),
                120,
                120);

        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Gas.getLightlyHydroCracked(1000),
                new FluidStack[] {
                    Materials.Methane.getGas(1300), Materials.Hydrogen.getGas(1500), Materials.Helium.getGas(100)
                },
                GT_Values.NI,
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Gas.getModeratelyHydroCracked(1000),
                new FluidStack[] {
                    Materials.Methane.getGas(1400), Materials.Hydrogen.getGas(3000), Materials.Helium.getGas(150)
                },
                GT_Values.NI,
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Gas.getSeverelyHydroCracked(1000),
                new FluidStack[] {
                    Materials.Methane.getGas(1500), Materials.Hydrogen.getGas(4000), Materials.Helium.getGas(200)
                },
                GT_Values.NI,
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Gas.getLightlySteamCracked(1000),
                new FluidStack[] {
                    Materials.Propene.getGas(50),
                    Materials.Ethane.getGas(10),
                    Materials.Ethylene.getGas(100),
                    Materials.Methane.getGas(500),
                    Materials.Helium.getGas(50)
                },
                Materials.Carbon.getDustTiny(1),
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Gas.getModeratelySteamCracked(1000),
                new FluidStack[] {
                    Materials.Propene.getGas(10),
                    Materials.Ethane.getGas(50),
                    Materials.Ethylene.getGas(200),
                    Materials.Methane.getGas(600),
                    Materials.Helium.getGas(70)
                },
                Materials.Carbon.getDustTiny(1),
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Gas.getSeverelySteamCracked(1000),
                new FluidStack[] {
                    Materials.Propene.getGas(10),
                    Materials.Ethane.getGas(10),
                    Materials.Ethylene.getGas(300),
                    Materials.Methane.getGas(700),
                    Materials.Helium.getGas(100)
                },
                Materials.Carbon.getDustTiny(1),
                120,
                120);

        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Naphtha.getLightlyHydroCracked(1000),
                new FluidStack[] {
                    Materials.Butane.getGas(800),
                    Materials.Propane.getGas(300),
                    Materials.Ethane.getGas(250),
                    Materials.Methane.getGas(250)
                },
                GT_Values.NI,
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Naphtha.getModeratelyHydroCracked(1000),
                new FluidStack[] {
                    Materials.Butane.getGas(200),
                    Materials.Propane.getGas(1100),
                    Materials.Ethane.getGas(400),
                    Materials.Methane.getGas(400)
                },
                GT_Values.NI,
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Naphtha.getSeverelyHydroCracked(1000),
                new FluidStack[] {
                    Materials.Butane.getGas(125),
                    Materials.Propane.getGas(125),
                    Materials.Ethane.getGas(1500),
                    Materials.Methane.getGas(1500)
                },
                GT_Values.NI,
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Naphtha.getLightlySteamCracked(1000),
                new FluidStack[] {
                    Materials.HeavyFuel.getFluid(75),
                    Materials.LightFuel.getFluid(150),
                    Materials.Toluene.getFluid(40),
                    Materials.Benzene.getFluid(150),
                    Materials.Butene.getGas(80),
                    Materials.Butadiene.getGas(150),
                    Materials.Propane.getGas(15),
                    Materials.Propene.getGas(200),
                    Materials.Ethane.getGas(35),
                    Materials.Ethylene.getGas(200),
                    Materials.Methane.getGas(200)
                },
                Materials.Carbon.getDustTiny(1),
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Naphtha.getModeratelySteamCracked(1000),
                new FluidStack[] {
                    Materials.HeavyFuel.getFluid(50),
                    Materials.LightFuel.getFluid(100),
                    Materials.Toluene.getFluid(30),
                    Materials.Benzene.getFluid(125),
                    Materials.Butene.getGas(65),
                    Materials.Butadiene.getGas(100),
                    Materials.Propane.getGas(30),
                    Materials.Propene.getGas(400),
                    Materials.Ethane.getGas(50),
                    Materials.Ethylene.getGas(350),
                    Materials.Methane.getGas(350)
                },
                Materials.Carbon.getDustTiny(2),
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.Naphtha.getSeverelySteamCracked(1000),
                new FluidStack[] {
                    Materials.HeavyFuel.getFluid(25),
                    Materials.LightFuel.getFluid(50),
                    Materials.Toluene.getFluid(20),
                    Materials.Benzene.getFluid(100),
                    Materials.Butene.getGas(50),
                    Materials.Butadiene.getGas(50),
                    Materials.Propane.getGas(15),
                    Materials.Propene.getGas(300),
                    Materials.Ethane.getGas(65),
                    Materials.Ethylene.getGas(500),
                    Materials.Methane.getGas(500)
                },
                Materials.Carbon.getDustTiny(3),
                120,
                120);

        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.LightFuel.getLightlyHydroCracked(1000),
                new FluidStack[] {
                    Materials.Naphtha.getFluid(800),
                    Materials.Octane.getFluid(100),
                    Materials.Butane.getGas(150),
                    Materials.Propane.getGas(200),
                    Materials.Ethane.getGas(125),
                    Materials.Methane.getGas(125)
                },
                GT_Values.NI,
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.LightFuel.getModeratelyHydroCracked(1000),
                new FluidStack[] {
                    Materials.Naphtha.getFluid(500),
                    Materials.Octane.getFluid(50),
                    Materials.Butane.getGas(200),
                    Materials.Propane.getGas(1100),
                    Materials.Ethane.getGas(400),
                    Materials.Methane.getGas(400)
                },
                GT_Values.NI,
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.LightFuel.getSeverelyHydroCracked(1000),
                new FluidStack[] {
                    Materials.Naphtha.getFluid(200),
                    Materials.Octane.getFluid(20),
                    Materials.Butane.getGas(125),
                    Materials.Propane.getGas(125),
                    Materials.Ethane.getGas(1500),
                    Materials.Methane.getGas(1500)
                },
                GT_Values.NI,
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.LightFuel.getLightlySteamCracked(1000),
                new FluidStack[] {
                    Materials.HeavyFuel.getFluid(150),
                    Materials.Naphtha.getFluid(400),
                    Materials.Toluene.getFluid(40),
                    Materials.Benzene.getFluid(200),
                    Materials.Butene.getGas(75),
                    Materials.Butadiene.getGas(60),
                    Materials.Propane.getGas(20),
                    Materials.Propene.getGas(150),
                    Materials.Ethane.getGas(10),
                    Materials.Ethylene.getGas(50),
                    Materials.Methane.getGas(50)
                },
                Materials.Carbon.getDustTiny(1),
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.LightFuel.getModeratelySteamCracked(1000),
                new FluidStack[] {
                    Materials.HeavyFuel.getFluid(100),
                    Materials.Naphtha.getFluid(250),
                    Materials.Toluene.getFluid(50),
                    Materials.Benzene.getFluid(300),
                    Materials.Butene.getGas(90),
                    Materials.Butadiene.getGas(75),
                    Materials.Propane.getGas(35),
                    Materials.Propene.getGas(200),
                    Materials.Ethane.getGas(30),
                    Materials.Ethylene.getGas(150),
                    Materials.Methane.getGas(150)
                },
                Materials.Carbon.getDustTiny(2),
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.LightFuel.getSeverelySteamCracked(1000),
                new FluidStack[] {
                    Materials.HeavyFuel.getFluid(50),
                    Materials.Naphtha.getFluid(100),
                    Materials.Toluene.getFluid(30),
                    Materials.Benzene.getFluid(150),
                    Materials.Butene.getGas(65),
                    Materials.Butadiene.getGas(50),
                    Materials.Propane.getGas(50),
                    Materials.Propene.getGas(250),
                    Materials.Ethane.getGas(50),
                    Materials.Ethylene.getGas(250),
                    Materials.Methane.getGas(250)
                },
                Materials.Carbon.getDustTiny(3),
                120,
                120);

        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.HeavyFuel.getLightlyHydroCracked(1000),
                new FluidStack[] {
                    Materials.LightFuel.getFluid(600),
                    Materials.Naphtha.getFluid(100),
                    Materials.Butane.getGas(100),
                    Materials.Propane.getGas(100),
                    Materials.Ethane.getGas(75),
                    Materials.Methane.getGas(75)
                },
                GT_Values.NI,
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.HeavyFuel.getModeratelyHydroCracked(1000),
                new FluidStack[] {
                    Materials.LightFuel.getFluid(400),
                    Materials.Naphtha.getFluid(400),
                    Materials.Butane.getGas(150),
                    Materials.Propane.getGas(150),
                    Materials.Ethane.getGas(100),
                    Materials.Methane.getGas(100)
                },
                GT_Values.NI,
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.HeavyFuel.getSeverelyHydroCracked(1000),
                new FluidStack[] {
                    Materials.LightFuel.getFluid(200),
                    Materials.Naphtha.getFluid(250),
                    Materials.Butane.getGas(300),
                    Materials.Propane.getGas(300),
                    Materials.Ethane.getGas(175),
                    Materials.Methane.getGas(175)
                },
                GT_Values.NI,
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.HeavyFuel.getLightlySteamCracked(1000),
                new FluidStack[] {
                    Materials.LightFuel.getFluid(300),
                    Materials.Naphtha.getFluid(50),
                    Materials.Toluene.getFluid(25),
                    Materials.Benzene.getFluid(125),
                    Materials.Butene.getGas(25),
                    Materials.Butadiene.getGas(15),
                    Materials.Propane.getGas(3),
                    Materials.Propene.getGas(30),
                    Materials.Ethane.getGas(5),
                    Materials.Ethylene.getGas(50),
                    Materials.Methane.getGas(50)
                },
                Materials.Carbon.getDustTiny(1),
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.HeavyFuel.getModeratelySteamCracked(1000),
                new FluidStack[] {
                    Materials.LightFuel.getFluid(200),
                    Materials.Naphtha.getFluid(200),
                    Materials.Toluene.getFluid(40),
                    Materials.Benzene.getFluid(200),
                    Materials.Butene.getGas(40),
                    Materials.Butadiene.getGas(25),
                    Materials.Propane.getGas(5),
                    Materials.Propene.getGas(50),
                    Materials.Ethane.getGas(7),
                    Materials.Ethylene.getGas(75),
                    Materials.Methane.getGas(75)
                },
                Materials.Carbon.getDustTiny(2),
                120,
                120);
        GT_Values.RA.addUniversalDistillationRecipe(
                Materials.HeavyFuel.getSeverelySteamCracked(1000),
                new FluidStack[] {
                    Materials.LightFuel.getFluid(100),
                    Materials.Naphtha.getFluid(125),
                    Materials.Toluene.getFluid(80),
                    Materials.Benzene.getFluid(400),
                    Materials.Butene.getGas(80),
                    Materials.Butadiene.getGas(50),
                    Materials.Propane.getGas(10),
                    Materials.Propene.getGas(100),
                    Materials.Ethane.getGas(15),
                    Materials.Ethylene.getGas(150),
                    Materials.Methane.getGas(150)
                },
                Materials.Carbon.getDustTiny(3),
                120,
                120);








        // 9C5H12O = 4C6H14O + 5CH4O + 4C4H8
        GT_Values.RA.addDistillationTowerRecipe(
                Materials.MTBEMixture.getGas(900L),
                new FluidStack[] {
                    Materials.AntiKnock.getFluid(400L), Materials.Methanol.getFluid(500L), Materials.Butene.getGas(400L)
                },
                null,
                40,
                240);




    }

    public void addPotionRecipes(String aName, ItemStack aItem) {
        // normal
        GT_Values.RA.addBrewingRecipe(
                aItem, FluidRegistry.getFluid("potion.awkward"), FluidRegistry.getFluid("potion." + aName), false);
        // strong
        GT_Values.RA.addBrewingRecipe(
                aItem,
                FluidRegistry.getFluid("potion.thick"),
                FluidRegistry.getFluid("potion." + aName + ".strong"),
                false);
        // long
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L),
                FluidRegistry.getFluid("potion." + aName),
                FluidRegistry.getFluid("potion." + aName + ".long"),
                false);

    }

    /**
     * Adds recipes related to producing Polybenzimidazole.
     */
    private void addPolybenzimidazoleRecipes() {



        // Dimethylbenzene
        GT_Values.RA.addDistilleryRecipe(
                5, Materials.WoodTar.getFluid(200), Materials.Dimethylbenzene.getFluid(30), 100, 120, false);
        GT_Values.RA.addDistilleryRecipe(
                5, Materials.CharcoalByproducts.getGas(200), Materials.Dimethylbenzene.getFluid(20), 100, 120, false);

    }



    /**
     * Load all Railcraft recipes for GT Machines
     */
    private void loadRailcraftRecipes() {
        if (!Loader.isModLoaded(MOD_ID_RC)) return;





        GT_ModHandler.addPulverisationRecipe(
                getModItem(MOD_ID_RC, "cube.crushed.obsidian", 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Obsidian, 1L),
                GT_Values.NI,
                0,
                true);






    }
}
