package gregtech.loaders.postload;

import static gregtech.api.enums.GT_Values.*;
import static gregtech.api.util.GT_ModHandler.getModItem;

import codechicken.nei.api.API;
import cpw.mods.fml.common.Loader;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.*;
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
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
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
    public static final Boolean isForestryloaded =
            Loader.isModLoaded(GT_MachineRecipeLoader.aTextForestry); // TODO OW YEAH NEW PLANK GEN CODE!!!

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

        RA.addThermalCentrifugeRecipe(
                ItemList.SunnariumCell.get(1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sunnarium, 1L),
                new ItemStack(Items.glowstone_dust, 2),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 1L),
                500,
                48);

        GT_ModHandler.removeRecipeByOutput(ItemList.IC2_Fertilizer.get(1L));

        if (!GregTech_API.mIC2Classic) {
            GT_Values.RA.addFuel(GT_ModHandler.getIC2Item("biogasCell", 1L), null, 40, 1);
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

        GT_BauxiteRefineChain.run();
    }

    public void run2() {

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

        this.addKevlarLineRecipes();

        loadRailcraftRecipes();
    }

    private void addKevlarLineRecipes() {
        // Kevlar Line

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
