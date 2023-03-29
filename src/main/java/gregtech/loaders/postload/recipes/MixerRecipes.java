package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.GT_Values.*;
import static gregtech.api.enums.ModIDs.*;
import static gregtech.api.util.GT_ModHandler.getModItem;
import static net.minecraftforge.fluids.FluidRegistry.getFluidStack;

import mods.railcraft.common.blocks.aesthetics.cube.EnumCube;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.*;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.loaders.postload.GT_MachineRecipeLoader;

public class MixerRecipes implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.EnderPearl, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Blaze, 1L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.getDust(Materials.EnderEye, OrePrefixes.dust.mMaterialAmount),
                (int) (100L * OrePrefixes.dust.mMaterialAmount / 3628800L),
                48);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gold, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silver, 1L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.getDust(Materials.Electrum, 2L * OrePrefixes.dust.mMaterialAmount),
                (int) (200L * OrePrefixes.dust.mMaterialAmount / 3628800L),
                8);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 2L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 1L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.getDust(Materials.Invar, 3L * OrePrefixes.dust.mMaterialAmount),
                (int) (300L * OrePrefixes.dust.mMaterialAmount / 3628800L),
                8);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 4L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Invar, 3L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Manganese, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chrome, 1L),
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.getDust(Materials.StainlessSteel, 9L * OrePrefixes.dust.mMaterialAmount),
                (int) (900L * OrePrefixes.dust.mMaterialAmount / 3628800L),
                120);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chrome, 1L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.getDust(Materials.Kanthal, 3L * OrePrefixes.dust.mMaterialAmount),
                (int) (300L * OrePrefixes.dust.mMaterialAmount / 3628800L),
                120);
        // GT_Values.RA.addMixerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 3L),
        // GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Barium, 2L), GT_OreDictUnificator.get(OrePrefixes.dust,
        // Materials.Yttrium, 1L), GT_Values.NI, GT_Values.NI, GT_Utility.getIntegratedCircuit(1), GT_Values.NF,
        // GT_Values.NF, GT_OreDictUnificator.getDust(Materials.YttriumBariumCuprate, 6L *
        // OrePrefixes.dust.mMaterialAmount), (int) (600L * OrePrefixes.dust.mMaterialAmount / 3628800L), 8);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 3L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Zinc, 1L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.getDust(Materials.Brass, 4L * OrePrefixes.dust.mMaterialAmount),
                (int) (400L * OrePrefixes.dust.mMaterialAmount / 3628800L),
                8);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 3L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Tin, 1L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.getDust(Materials.Bronze, 4L * OrePrefixes.dust.mMaterialAmount),
                (int) (400L * OrePrefixes.dust.mMaterialAmount / 3628800L),
                8);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 1L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(3),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.getDust(Materials.Cupronickel, 2L * OrePrefixes.dust.mMaterialAmount),
                (int) (200L * OrePrefixes.dust.mMaterialAmount / 3628800L),
                24);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silver, 4L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(4),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.getDust(Materials.SterlingSilver, 5L * OrePrefixes.dust.mMaterialAmount),
                (int) (500L * OrePrefixes.dust.mMaterialAmount / 3628800L),
                120);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 3L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Electrum, 2L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.getDust(Materials.BlackBronze, 5L * OrePrefixes.dust.mMaterialAmount),
                (int) (500L * OrePrefixes.dust.mMaterialAmount / 3628800L),
                8);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Bismuth, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Brass, 4L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.getDust(Materials.BismuthBronze, 5L * OrePrefixes.dust.mMaterialAmount),
                (int) (500L * OrePrefixes.dust.mMaterialAmount / 3628800L),
                8);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.BlackBronze, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 3L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.getDust(Materials.BlackSteel, 5L * OrePrefixes.dust.mMaterialAmount),
                (int) (500L * OrePrefixes.dust.mMaterialAmount / 3628800L),
                8);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SterlingSilver, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.BismuthBronze, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.BlackSteel, 4L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 2L),
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.getDust(Materials.RedSteel, 8L * OrePrefixes.dust.mMaterialAmount),
                (int) (800L * OrePrefixes.dust.mMaterialAmount / 3628800L),
                8);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RoseGold, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Brass, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.BlackSteel, 4L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 2L),
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.getDust(Materials.BlueSteel, 8L * OrePrefixes.dust.mMaterialAmount),
                (int) (800L * OrePrefixes.dust.mMaterialAmount / 3628800L),
                8);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 3L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gold, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silver, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 5L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 15L),
                GT_Utility.getIntegratedCircuit(14),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.getDust(Materials.BlackSteel, 25L * OrePrefixes.dust.mMaterialAmount),
                (int) (800L * OrePrefixes.dust.mMaterialAmount / 3628800L),
                480);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 4L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silver, 4L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Bismuth, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Zinc, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.BlackSteel, 20L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 10L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(15),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.getDust(Materials.RedSteel, 40L * OrePrefixes.dust.mMaterialAmount),
                (int) (1200L * OrePrefixes.dust.mMaterialAmount / 3628800L),
                480);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 19L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gold, 16L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Zinc, 5L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.BlackSteel, 64L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.BlackSteel, 16L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 40L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(16),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.getDust(Materials.BlueSteel, 64L * OrePrefixes.dust.mMaterialAmount),
                GT_OreDictUnificator.getDust(Materials.BlueSteel, 64L * OrePrefixes.dust.mMaterialAmount),
                GT_OreDictUnificator.getDust(Materials.BlueSteel, 32L * OrePrefixes.dust.mMaterialAmount),
                GT_Values.NI,
                (int) (3600L * OrePrefixes.dust.mMaterialAmount / 3628800L),
                480);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Cobalt, 5L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chrome, 2L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Molybdenum, 1L),
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(2),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.getDust(Materials.Ultimet, 9L * OrePrefixes.dust.mMaterialAmount),
                (int) (900L * OrePrefixes.dust.mMaterialAmount / 3628800L),
                500);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Brass, 7L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Cobalt, 1L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.getDust(Materials.CobaltBrass, 9L * OrePrefixes.dust.mMaterialAmount),
                (int) (900L * OrePrefixes.dust.mMaterialAmount / 3628800L),
                8);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Saltpeter, 2L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 3L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.getDust(Materials.Gunpowder, 6L * OrePrefixes.dust.mMaterialAmount),
                (int) (600L * OrePrefixes.dust.mMaterialAmount / 3628800L),
                8);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Saltpeter, 2L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Charcoal, 3L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.getDust(Materials.Gunpowder, 6L * OrePrefixes.dust.mMaterialAmount),
                (int) (600L * OrePrefixes.dust.mMaterialAmount / 3628800L),
                8);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Saltpeter, 2L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 3L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.getDust(Materials.Gunpowder, 6L * OrePrefixes.dust.mMaterialAmount),
                (int) (600L * OrePrefixes.dust.mMaterialAmount / 3628800L),
                8);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Indium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gallium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Phosphorus, 1L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.getDust(Materials.IndiumGalliumPhosphide, 3L * OrePrefixes.dust.mMaterialAmount),
                (int) (200L * OrePrefixes.dust.mMaterialAmount / 3628800L),
                8);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Brick, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Clay, 1L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.getDust(Materials.Fireclay, 2L * OrePrefixes.dust.mMaterialAmount),
                (int) (200L * OrePrefixes.dust.mMaterialAmount / 3628800L),
                8);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 4L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chrome, 1L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.getDust(Materials.Nichrome, 5L * OrePrefixes.dust.mMaterialAmount),
                (int) (500L * OrePrefixes.dust.mMaterialAmount / 3628800L),
                480);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Osmium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iridium, 3L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.getDust(Materials.Osmiridium, 4L * OrePrefixes.dust.mMaterialAmount),
                (int) (400L * OrePrefixes.dust.mMaterialAmount / 3628800L),
                2000);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Niobium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Titanium, 1L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.getDust(Materials.NiobiumTitanium, 2L * OrePrefixes.dust.mMaterialAmount),
                (int) (200L * OrePrefixes.dust.mMaterialAmount / 3628800L),
                2000);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Vanadium, 3L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gallium, 1L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.getDust(Materials.VanadiumGallium, 4L * OrePrefixes.dust.mMaterialAmount),
                (int) (400L * OrePrefixes.dust.mMaterialAmount / 3628800L),
                2000);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Tungsten, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 1L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.getDust(Materials.TungstenCarbide, 2L * OrePrefixes.dust.mMaterialAmount),
                (int) (200L * OrePrefixes.dust.mMaterialAmount / 3628800L),
                500);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Tungsten, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 1L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.getDust(Materials.TungstenSteel, 2L * OrePrefixes.dust.mMaterialAmount),
                (int) (50L * OrePrefixes.dust.mMaterialAmount / 3628800L),
                1920);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Titanium, 3L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Platinum, 3L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Vanadium, 1L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.getDust(Materials.TPV, 7L * OrePrefixes.dust.mMaterialAmount),
                (int) (175L * OrePrefixes.dust.mMaterialAmount / 3628800L),
                1920);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.TungstenSteel, 5L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chrome, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Molybdenum, 2L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Vanadium, 1L),
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.getDust(Materials.HSSG, 9L * OrePrefixes.dust.mMaterialAmount),
                (int) (600L * OrePrefixes.dust.mMaterialAmount / 3628800L),
                1920);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.HSSG, 6L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Cobalt, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Manganese, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 1L),
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.getDust(Materials.HSSE, 9L * OrePrefixes.dust.mMaterialAmount),
                (int) (700L * OrePrefixes.dust.mMaterialAmount / 3628800L),
                4096);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.HSSG, 6L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iridium, 2L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Osmium, 1L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(2),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.getDust(Materials.HSSS, 9L * OrePrefixes.dust.mMaterialAmount),
                (int) (800L * OrePrefixes.dust.mMaterialAmount / 3628800L),
                7680);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Zinc, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 4L),
                GT_Values.NI,
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.getDust(Materials.FerriteMixture, 6L * OrePrefixes.dust.mMaterialAmount),
                (int) (200L * OrePrefixes.dust.mMaterialAmount / 3628800L),
                8);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Boron, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glass, 7L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.getDust(Materials.BorosilicateGlass, 8L * OrePrefixes.dust.mMaterialAmount),
                (int) (200L * OrePrefixes.dust.mMaterialAmount / 3628800L),
                8);

        GT_Values.RA.addMixerRecipe(
                new ItemStack(Items.rotten_flesh, 1, 0),
                new ItemStack(Items.fermented_spider_eye, 1, 0),
                ItemList.IC2_Scrap.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.MeatRaw, 1L),
                getFluidStack("potion.purpledrink", 750),
                getFluidStack("sludge", 1000),
                ItemList.Food_Chum.get(4L),
                128,
                24);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wheat, 1L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                Materials.Water.getFluid(1000L),
                GT_Values.NF,
                ItemList.Food_Dough.get(2L),
                32,
                8);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chili, 1L),
                ItemList.Food_PotatoChips.get(1L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NF,
                GT_Values.NF,
                ItemList.Food_ChiliChips.get(1L),
                32,
                8);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Redstone, 5L),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Ruby, 4L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NF,
                GT_Values.NF,
                ItemList.IC2_Energium_Dust.get(1L),
                300,
                120);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 5L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ruby, 4L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NF,
                GT_Values.NF,
                ItemList.IC2_Energium_Dust.get(9L),
                600,
                120);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L),
                new ItemStack(Blocks.brown_mushroom, 1),
                new ItemStack(Items.spider_eye, 1),
                GT_Values.NI,
                GT_Values.NF,
                GT_Values.NF,
                new ItemStack(Items.fermented_spider_eye, 1),
                100,
                8);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Gold, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.LiveRoot, 1L),
                GT_Values.NI,
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.IronWood, 2L),
                100,
                8);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gold, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 9L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.LiveRoot, 9L),
                GT_Values.NI,
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.IronWood, 18L),
                900,
                8);
        GT_Values.RA.addMixerRecipe(
                getModItem(AppliedEnergistics2.modID, "item.ItemMultiMaterial", 1L, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L),
                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.NetherQuartz, 1L),
                GT_Values.NI,
                Materials.Water.getFluid(500L),
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Fluix, 2L),
                20,
                16);
        GT_Values.RA.addMixerRecipe(
                getModItem(AppliedEnergistics2.modID, "item.ItemMultiMaterial", 1L, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L),
                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.NetherQuartz, 1L),
                GT_Values.NI,
                GT_ModHandler.getDistilledWater(500L),
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Fluix, 2L),
                20,
                16);
        GT_Values.RA.addMixerRecipe(
                ItemList.IC2_Fertilizer.get(1L),
                new ItemStack(Blocks.dirt, 8, 32767),
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NI,
                Materials.Water.getFluid(1000L),
                GT_Values.NF,
                getModItem(Forestry.modID, "soil", 8L, 0),
                64,
                16);
        GT_Values.RA.addMixerRecipe(
                ItemList.FR_Fertilizer.get(1L),
                new ItemStack(Blocks.dirt, 8, 32767),
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NI,
                Materials.Water.getFluid(1000L),
                GT_Values.NF,
                getModItem(Forestry.modID, "soil", 8L, 0),
                64,
                16);
        GT_Values.RA.addMixerRecipe(
                ItemList.FR_Compost.get(1L),
                new ItemStack(Blocks.dirt, 8, 32767),
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NI,
                Materials.Water.getFluid(1000L),
                GT_Values.NF,
                getModItem(Forestry.modID, "soil", 8L, 0),
                64,
                16);
        GT_Values.RA.addMixerRecipe(
                ItemList.FR_Mulch.get(8L),
                new ItemStack(Blocks.dirt, 8, 32767),
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NI,
                Materials.Water.getFluid(1000L),
                GT_Values.NF,
                getModItem(Forestry.modID, "soil", 8L, 0),
                64,
                16);
        GT_Values.RA.addMixerRecipe(
                new ItemStack(Blocks.sand, 1, 32767),
                new ItemStack(Blocks.dirt, 1, 32767),
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NI,
                Materials.Water.getFluid(250L),
                GT_Values.NF,
                getModItem(Forestry.modID, "soil", 2L, 1),
                16,
                16);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.LightFuel, 5L),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.HeavyFuel, 1L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Fuel, 6L),
                16,
                120);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.LightFuel, 5L),
                Materials.Empty.getCells(1),
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(1),
                Materials.HeavyFuel.getFluid(1000L),
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Fuel, 6L),
                16,
                120);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.HeavyFuel, 1L),
                Materials.Empty.getCells(5),
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(1),
                Materials.LightFuel.getFluid(5000L),
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Fuel, 6L),
                16,
                120);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.LightFuel, 5L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(5),
                Materials.HeavyFuel.getFluid(1000L),
                Materials.Fuel.getFluid(6000L),
                Materials.Empty.getCells(5),
                16,
                120);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.HeavyFuel, 1L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(6),
                Materials.LightFuel.getFluid(5000L),
                Materials.Fuel.getFluid(6000L),
                Materials.Empty.getCells(1),
                16,
                120);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Water, 5L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1L),
                GT_Values.NI,
                GT_Values.NI,
                Materials.Lubricant.getFluid(20),
                new FluidStack(ItemList.sDrillingFluid, 5000),
                Materials.Empty.getCells(5),
                64,
                16);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lapis, 1L),
                GT_Utility.getIntegratedCircuit(4),
                GT_Values.NI,
                GT_Values.NI,
                Materials.Water.getFluid(125),
                getFluidStack("ic2coolant", 125),
                GT_Values.NI,
                256,
                48);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lapis, 1L),
                GT_Utility.getIntegratedCircuit(4),
                GT_Values.NI,
                GT_Values.NI,
                GT_ModHandler.getDistilledWater(1000),
                getFluidStack("ic2coolant", 1000),
                GT_Values.NI,
                256,
                48);

        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 4L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(1),
                Materials.AdvancedGlue.getFluid(200),
                null,
                ItemList.SFMixture.get(4),
                800,
                16);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lithium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 4L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(1),
                Materials.AdvancedGlue.getFluid(200),
                null,
                ItemList.SFMixture.get(8),
                800,
                16);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Caesium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 4L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(1),
                Materials.AdvancedGlue.getFluid(200),
                null,
                ItemList.SFMixture.get(12),
                800,
                16);

        GT_Values.RA.addMixerRecipe(
                ItemList.SFMixture.get(2),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.EnderEye, 1L),
                null,
                null,
                Materials.Mercury.getFluid(1000),
                null,
                ItemList.MSFMixture.get(4),
                300,
                64);
        GT_Values.RA.addMixerRecipe(
                ItemList.SFMixture.get(1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Blaze, 1L),
                null,
                null,
                Materials.Mercury.getFluid(500),
                null,
                ItemList.MSFMixture.get(1),
                300,
                64);

        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Lignite, 4),
                ItemList.MSFMixture.get(24),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Diamond, 1L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(1),
                Materials.NitroFuel.getFluid(4000),
                null,
                ItemList.Block_MSSFUEL.get(4),
                400,
                600);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Charcoal, 4),
                ItemList.MSFMixture.get(16),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Diamond, 1L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(1),
                Materials.NitroFuel.getFluid(3000),
                null,
                ItemList.Block_MSSFUEL.get(4),
                300,
                600);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Coal, 4),
                ItemList.MSFMixture.get(8),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Diamond, 1L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(1),
                Materials.NitroFuel.getFluid(2000),
                null,
                ItemList.Block_MSSFUEL.get(4),
                200,
                600);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Lignite, 4),
                ItemList.MSFMixture.get(24),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Diamond, 1L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(1),
                Materials.GasolinePremium.getFluid(1600),
                null,
                ItemList.Block_MSSFUEL.get(4),
                400,
                600);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Charcoal, 4),
                ItemList.MSFMixture.get(16),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Diamond, 1L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(1),
                Materials.GasolinePremium.getFluid(1200),
                null,
                ItemList.Block_MSSFUEL.get(4),
                300,
                600);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Coal, 4),
                ItemList.MSFMixture.get(8),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Diamond, 1L),
                GT_Values.NI,
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(1),
                Materials.GasolinePremium.getFluid(800),
                null,
                ItemList.Block_MSSFUEL.get(4),
                200,
                600);

        if (Thaumcraft.isModLoaded()) {
            GT_Values.RA.addMixerRecipe(
                    ItemList.SFMixture.get(20),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfusedAir, 1L),
                    GT_Values.NI,
                    GT_Values.NI,
                    GT_Values.NI,
                    GT_Utility.getIntegratedCircuit(1),
                    Materials.FierySteel.getFluid(50),
                    null,
                    ItemList.MSFMixture.get(20),
                    200,
                    64);
            GT_Values.RA.addMixerRecipe(
                    ItemList.SFMixture.get(20),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfusedEarth, 1L),
                    GT_Values.NI,
                    GT_Values.NI,
                    GT_Values.NI,
                    GT_Utility.getIntegratedCircuit(1),
                    Materials.FierySteel.getFluid(50),
                    null,
                    ItemList.MSFMixture.get(20),
                    200,
                    64);
            GT_Values.RA.addMixerRecipe(
                    ItemList.SFMixture.get(20),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfusedEntropy, 1L),
                    GT_Values.NI,
                    GT_Values.NI,
                    GT_Values.NI,
                    GT_Utility.getIntegratedCircuit(1),
                    Materials.FierySteel.getFluid(50),
                    null,
                    ItemList.MSFMixture.get(20),
                    200,
                    64);
            GT_Values.RA.addMixerRecipe(
                    ItemList.SFMixture.get(20),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfusedFire, 1L),
                    GT_Values.NI,
                    GT_Values.NI,
                    GT_Values.NI,
                    GT_Utility.getIntegratedCircuit(1),
                    Materials.FierySteel.getFluid(50),
                    null,
                    ItemList.MSFMixture.get(20),
                    200,
                    64);
            GT_Values.RA.addMixerRecipe(
                    ItemList.SFMixture.get(20),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfusedOrder, 1L),
                    GT_Values.NI,
                    GT_Values.NI,
                    GT_Values.NI,
                    GT_Utility.getIntegratedCircuit(1),
                    Materials.FierySteel.getFluid(50),
                    null,
                    ItemList.MSFMixture.get(20),
                    200,
                    64);
            GT_Values.RA.addMixerRecipe(
                    ItemList.SFMixture.get(20),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfusedWater, 1L),
                    GT_Values.NI,
                    GT_Values.NI,
                    GT_Values.NI,
                    GT_Utility.getIntegratedCircuit(1),
                    Materials.FierySteel.getFluid(50),
                    null,
                    ItemList.MSFMixture.get(20),
                    200,
                    64);

            FluidStack tFD = getFluidStack("fluiddeath", 30);
            if (tFD != null && tFD.getFluid() != null && tFD.amount > 0) {
                GT_Values.RA.addMixerRecipe(
                        ItemList.SFMixture.get(30),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfusedAir, 1L),
                        GT_Values.NI,
                        GT_Values.NI,
                        GT_Values.NI,
                        GT_Utility.getIntegratedCircuit(1),
                        tFD,
                        null,
                        ItemList.MSFMixture.get(30),
                        200,
                        64);
                GT_Values.RA.addMixerRecipe(
                        ItemList.SFMixture.get(30),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfusedEarth, 1L),
                        GT_Values.NI,
                        GT_Values.NI,
                        GT_Values.NI,
                        GT_Utility.getIntegratedCircuit(1),
                        tFD,
                        null,
                        ItemList.MSFMixture.get(30),
                        200,
                        64);
                GT_Values.RA.addMixerRecipe(
                        ItemList.SFMixture.get(30),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfusedEntropy, 1L),
                        GT_Values.NI,
                        GT_Values.NI,
                        GT_Values.NI,
                        GT_Utility.getIntegratedCircuit(1),
                        tFD,
                        null,
                        ItemList.MSFMixture.get(30),
                        200,
                        64);
                GT_Values.RA.addMixerRecipe(
                        ItemList.SFMixture.get(30),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfusedFire, 1L),
                        GT_Values.NI,
                        GT_Values.NI,
                        GT_Values.NI,
                        GT_Utility.getIntegratedCircuit(1),
                        tFD,
                        null,
                        ItemList.MSFMixture.get(30),
                        200,
                        64);
                GT_Values.RA.addMixerRecipe(
                        ItemList.SFMixture.get(30),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfusedOrder, 1L),
                        GT_Values.NI,
                        GT_Values.NI,
                        GT_Values.NI,
                        GT_Utility.getIntegratedCircuit(1),
                        tFD,
                        null,
                        ItemList.MSFMixture.get(30),
                        200,
                        64);
                GT_Values.RA.addMixerRecipe(
                        ItemList.SFMixture.get(30),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfusedWater, 1L),
                        GT_Values.NI,
                        GT_Values.NI,
                        GT_Values.NI,
                        GT_Utility.getIntegratedCircuit(1),
                        tFD,
                        null,
                        ItemList.MSFMixture.get(30),
                        200,
                        64);

                GT_Values.RA.addMixerRecipe(
                        GT_OreDictUnificator.get(OrePrefixes.block, Materials.Lignite, 1),
                        ItemList.MSFMixture.get(6),
                        getModItem("Thaumcraft", "ItemResource", 4),
                        GT_Values.NI,
                        GT_Values.NI,
                        GT_Utility.getIntegratedCircuit(1),
                        Materials.NitroFuel.getFluid(1000),
                        null,
                        ItemList.Block_MSSFUEL.get(1),
                        140,
                        600);
                GT_Values.RA.addMixerRecipe(
                        GT_OreDictUnificator.get(OrePrefixes.block, Materials.Charcoal, 1),
                        ItemList.MSFMixture.get(4),
                        getModItem("Thaumcraft", "ItemResource", 4),
                        GT_Values.NI,
                        GT_Values.NI,
                        GT_Utility.getIntegratedCircuit(1),
                        Materials.NitroFuel.getFluid(750),
                        null,
                        ItemList.Block_MSSFUEL.get(1),
                        120,
                        600);
                GT_Values.RA.addMixerRecipe(
                        GT_OreDictUnificator.get(OrePrefixes.block, Materials.Coal, 1),
                        ItemList.MSFMixture.get(2),
                        getModItem("Thaumcraft", "ItemResource", 4),
                        GT_Values.NI,
                        GT_Values.NI,
                        GT_Utility.getIntegratedCircuit(1),
                        Materials.NitroFuel.getFluid(500),
                        null,
                        ItemList.Block_MSSFUEL.get(1),
                        100,
                        600);
                GT_Values.RA.addMixerRecipe(
                        GT_OreDictUnificator.get(OrePrefixes.block, Materials.Lignite, 1),
                        ItemList.MSFMixture.get(6),
                        getModItem("Thaumcraft", "ItemResource", 4),
                        GT_Values.NI,
                        GT_Values.NI,
                        GT_Utility.getIntegratedCircuit(1),
                        Materials.GasolinePremium.getFluid(400),
                        null,
                        ItemList.Block_MSSFUEL.get(1),
                        140,
                        600);
                GT_Values.RA.addMixerRecipe(
                        GT_OreDictUnificator.get(OrePrefixes.block, Materials.Charcoal, 1),
                        ItemList.MSFMixture.get(4),
                        getModItem("Thaumcraft", "ItemResource", 4),
                        GT_Values.NI,
                        GT_Values.NI,
                        GT_Utility.getIntegratedCircuit(1),
                        Materials.GasolinePremium.getFluid(300),
                        null,
                        ItemList.Block_MSSFUEL.get(1),
                        120,
                        600);
                GT_Values.RA.addMixerRecipe(
                        GT_OreDictUnificator.get(OrePrefixes.block, Materials.Coal, 1),
                        ItemList.MSFMixture.get(2),
                        getModItem("Thaumcraft", "ItemResource", 4),
                        GT_Values.NI,
                        GT_Values.NI,
                        GT_Utility.getIntegratedCircuit(1),
                        Materials.GasolinePremium.getFluid(200),
                        null,
                        ItemList.Block_MSSFUEL.get(1),
                        100,
                        600);
            }
        }

        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Lignite, 1),
                ItemList.SFMixture.get(6),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(1),
                Materials.NitroFuel.getFluid(1000),
                null,
                ItemList.Block_SSFUEL.get(1),
                140,
                250);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Charcoal, 1),
                ItemList.SFMixture.get(4),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(1),
                Materials.NitroFuel.getFluid(750),
                null,
                ItemList.Block_SSFUEL.get(1),
                120,
                250);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Coal, 1),
                ItemList.SFMixture.get(2),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(1),
                Materials.NitroFuel.getFluid(500),
                null,
                ItemList.Block_SSFUEL.get(1),
                100,
                250);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Lignite, 1),
                ItemList.SFMixture.get(6),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(1),
                Materials.GasolinePremium.getFluid(400),
                null,
                ItemList.Block_SSFUEL.get(1),
                140,
                250);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Charcoal, 1),
                ItemList.SFMixture.get(4),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(1),
                Materials.GasolinePremium.getFluid(300),
                null,
                ItemList.Block_SSFUEL.get(1),
                120,
                250);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Coal, 1),
                ItemList.SFMixture.get(2),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(1),
                Materials.GasolinePremium.getFluid(200),
                null,
                ItemList.Block_SSFUEL.get(1),
                100,
                250);
        GT_Values.RA.addMixerRecipe(
                new ItemStack[] { Materials.NaquadahEnriched.getDust(8), Materials.Holmium.getDust(2),
                        GT_Utility.getIntegratedCircuit(4) },
                null,
                new ItemStack[] { Materials.EnrichedHolmium.getDust(10) },
                null,
                30 * 20,
                (int) GT_Values.VP[7]);

        // Catalysts for Plasma Forge.
        GT_Values.RA.addMixerRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(9) },
                new FluidStack[] { Materials.Helium.getPlasma(1000L), Materials.Iron.getPlasma(1000L),
                        Materials.Calcium.getPlasma(1000L), Materials.Niobium.getPlasma(1000L) },
                new ItemStack[] { GT_Values.NI },
                new FluidStack[] { Materials.DimensionallyTranscendentCrudeCatalyst.getFluid(1000L) },
                50_000,
                125_000);

        GT_Values.RA.addMixerRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(10) },
                new FluidStack[] { Materials.DimensionallyTranscendentCrudeCatalyst.getFluid(1000L),
                        Materials.Radon.getPlasma(1000L), Materials.Nickel.getPlasma(1000L),
                        Materials.Boron.getPlasma(1000L), Materials.Sulfur.getPlasma(1000L) },
                new ItemStack[] { GT_Values.NI },
                new FluidStack[] { Materials.DimensionallyTranscendentProsaicCatalyst.getFluid(1000L) },
                50_000,
                125_000 * 4);

        GT_Values.RA.addMixerRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(11) },
                new FluidStack[] { Materials.DimensionallyTranscendentProsaicCatalyst.getFluid(1000L),
                        Materials.Nitrogen.getPlasma(1000L), Materials.Zinc.getPlasma(1000L),
                        Materials.Silver.getPlasma(1000L), Materials.Titanium.getPlasma(1000L) },
                new ItemStack[] { GT_Values.NI },
                new FluidStack[] { Materials.DimensionallyTranscendentResplendentCatalyst.getFluid(1000L) },
                50_000,
                125_000 * 16);

        GT_Values.RA.addMixerRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(12) },
                new FluidStack[] { Materials.DimensionallyTranscendentResplendentCatalyst.getFluid(1000L),
                        Materials.Americium.getPlasma(1000L), Materials.Bismuth.getPlasma(1000L),
                        Materials.Oxygen.getPlasma(1000L), Materials.Tin.getPlasma(1000L) },
                new ItemStack[] { GT_Values.NI },
                new FluidStack[] { Materials.DimensionallyTranscendentExoticCatalyst.getFluid(1000L) },
                50_000,
                125_000 * 64);

        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.SulfuricAcid, 1),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(1),
                Materials.NitricAcid.getFluid(1000L),
                new FluidStack(ItemList.sNitrationMixture, 2000),
                ItemList.Cell_Empty.get(1),
                480,
                2);
        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.SulfuricAcid, 1),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.NitricAcid, 1),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.NitrationMixture, 2),
                480,
                2);

        GT_Values.RA.addMixerRecipe(
                new ItemStack(Blocks.dirt, 1, 32767),
                new ItemStack(Items.wheat, 4, 32767),
                GT_Utility.getIntegratedCircuit(2),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                Materials.Water.getFluid(100),
                null,
                getModItem("Forestry", "fertilizerBio", 1L, 0),
                200,
                16);
        GT_Values.RA.addMixerRecipe(
                new ItemStack(Blocks.dirt, 1, 2),
                new ItemStack(Items.wheat, 4, 32767),
                GT_Utility.getIntegratedCircuit(2),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                Materials.Water.getFluid(100),
                GT_Values.NF,
                getModItem("Forestry", "fertilizerBio", 1L, 0),
                200,
                16);
        GT_Values.RA.addMixerRecipe(
                new ItemStack(Blocks.dirt, 1, 32767),
                getModItem("BiomesOPlenty", "plants", 4, 6),
                GT_Utility.getIntegratedCircuit(2),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                Materials.Water.getFluid(100),
                GT_Values.NF,
                getModItem("Forestry", "fertilizerBio", 1L, 0),
                200,
                16);
        GT_Values.RA.addMixerRecipe(
                new ItemStack(Blocks.dirt, 1, 2),
                getModItem("BiomesOPlenty", "plants", 4, 6),
                GT_Utility.getIntegratedCircuit(2),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                Materials.Water.getFluid(100),
                GT_Values.NF,
                getModItem("Forestry", "fertilizerBio", 1L, 0),
                200,
                16);
        GT_Values.RA.addMixerRecipe(
                new ItemStack(Blocks.dirt, 1, 32767),
                getModItem("harvestcraft", "oatsItem", 4),
                GT_Utility.getIntegratedCircuit(2),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                Materials.Water.getFluid(100),
                GT_Values.NF,
                getModItem("Forestry", "fertilizerBio", 1L, 0),
                200,
                16);
        GT_Values.RA.addMixerRecipe(
                new ItemStack(Blocks.dirt, 1, 2),
                getModItem("harvestcraft", "oatsItem", 4),
                GT_Utility.getIntegratedCircuit(2),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                Materials.Water.getFluid(100),
                GT_Values.NF,
                getModItem("Forestry", "fertilizerBio", 1L, 0),
                200,
                16);
        GT_Values.RA.addMixerRecipe(
                new ItemStack(Blocks.dirt, 1, 32767),
                getModItem("harvestcraft", "ryeItem", 4),
                GT_Utility.getIntegratedCircuit(2),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                Materials.Water.getFluid(100),
                GT_Values.NF,
                getModItem("Forestry", "fertilizerBio", 1L, 0),
                200,
                16);
        GT_Values.RA.addMixerRecipe(
                new ItemStack(Blocks.dirt, 1, 2),
                getModItem("harvestcraft", "ryeItem", 4),
                GT_Utility.getIntegratedCircuit(2),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                Materials.Water.getFluid(100),
                GT_Values.NF,
                getModItem("Forestry", "fertilizerBio", 1L, 0),
                200,
                16);
        GT_Values.RA.addMixerRecipe(
                new ItemStack(Blocks.dirt, 1, 32767),
                getModItem("harvestcraft", "barleyItem", 4),
                GT_Utility.getIntegratedCircuit(2),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                Materials.Water.getFluid(100),
                GT_Values.NF,
                getModItem("Forestry", "fertilizerBio", 1L, 0),
                200,
                16);
        GT_Values.RA.addMixerRecipe(
                new ItemStack(Blocks.dirt, 1, 2),
                getModItem("harvestcraft", "barleyItem", 4, 6),
                GT_Utility.getIntegratedCircuit(2),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                Materials.Water.getFluid(100),
                GT_Values.NF,
                getModItem("Forestry", "fertilizerBio", 1L, 0),
                200,
                16);
        GT_Values.RA.addMixerRecipe(
                new ItemStack(Blocks.dirt, 1, 32767),
                getModItem("Natura", "barleyFood", 4),
                GT_Utility.getIntegratedCircuit(2),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                Materials.Water.getFluid(100),
                GT_Values.NF,
                getModItem("Forestry", "fertilizerBio", 1L, 0),
                200,
                16);
        GT_Values.RA.addMixerRecipe(
                new ItemStack(Blocks.dirt, 1, 2),
                getModItem("Natura", "barleyFood", 4),
                GT_Utility.getIntegratedCircuit(2),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                Materials.Water.getFluid(100),
                GT_Values.NF,
                getModItem("Forestry", "fertilizerBio", 1L, 0),
                200,
                16);
        GT_Values.RA.addMixerRecipe(
                new ItemStack(Blocks.dirt, 1, 32767),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 4L),
                GT_Utility.getIntegratedCircuit(3),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                Materials.Water.getFluid(100),
                GT_Values.NF,
                getModItem("Forestry", "fertilizerBio", 1L, 0),
                200,
                16);
        GT_Values.RA.addMixerRecipe(
                new ItemStack(Blocks.dirt, 1, 2),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 4L),
                GT_Utility.getIntegratedCircuit(3),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                Materials.Water.getFluid(100),
                GT_Values.NF,
                getModItem("Forestry", "fertilizerBio", 1L, 0),
                200,
                16);

        // radiation manufacturing
        GT_Values.RA.addMixerRecipe(
                GT_ModHandler.getIC2Item("fuelRod", 1),
                new ItemStack(Items.glowstone_dust, 9),
                NI,
                NI,
                Materials.Helium.getGas(250),
                NF,
                ItemList.GlowstoneCell.get(1),
                30,
                16);

        GT_Values.RA.addMixerRecipe(
                MaterialsOreAlum.SluiceSand.getDust(1),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                Materials.Water.getFluid(500),
                MaterialsOreAlum.SluiceJuice.getFluid(1000),
                GT_Values.NI,
                100,
                16);

        RA.addMixerRecipe(
                new ItemStack[] { ItemList.IC2_Spray_WeedEx.get(1) },
                new FluidStack[] { MaterialsKevlar.NaphthenicAcid.getFluid(10) },
                new ItemStack[] {},
                new FluidStack[] { Materials.WeedEX9000.getFluid(750) },
                100,
                100);

        // NaCl + H2O = (NaCl·H2O)
        GT_Values.RA.addMixerRecipe(
                Materials.Salt.getDust(2),
                GT_Utility.getIntegratedCircuit(3),
                GT_Values.NI,
                GT_Values.NI,
                Materials.Water.getFluid(1000),
                Materials.SaltWater.getFluid(1000),
                GT_Values.NI,
                100,
                8);

        // CaCO3 + 2 CH3COOH = Ca(CH3COO)2 + H2O + CO2
        GT_Values.RA.addMixerRecipe(
                Materials.Calcite.getDust(5),
                Materials.Empty.getCells(2),
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                Materials.AceticAcid.getFluid(2000),
                Materials.CalciumAcetateSolution.getFluid(1000),
                Materials.Water.getCells(1),
                Materials.CarbonDioxide.getCells(1),
                GT_Values.NI,
                GT_Values.NI,
                240,
                16);
        GT_Values.RA.addMixerRecipe(
                Materials.Calcite.getDust(5),
                Materials.Empty.getCells(2),
                GT_Utility.getIntegratedCircuit(11),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                Materials.AceticAcid.getFluid(2000),
                Materials.Water.getFluid(1000),
                Materials.CalciumAcetateSolution.getCells(1),
                Materials.CarbonDioxide.getCells(1),
                GT_Values.NI,
                GT_Values.NI,
                240,
                16);
        GT_Values.RA.addMixerRecipe(
                Materials.Calcite.getDust(5),
                Materials.Empty.getCells(2),
                GT_Utility.getIntegratedCircuit(21),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                Materials.AceticAcid.getFluid(2000),
                Materials.CarbonDioxide.getGas(1000),
                Materials.Water.getCells(1),
                Materials.CalciumAcetateSolution.getCells(1),
                GT_Values.NI,
                GT_Values.NI,
                240,
                16);
        GT_Values.RA.addMixerRecipe(
                Materials.Calcite.getDust(5),
                Materials.Empty.getCells(1),
                GT_Utility.getIntegratedCircuit(4),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                Materials.AceticAcid.getFluid(2000),
                Materials.CalciumAcetateSolution.getFluid(1000),
                Materials.CarbonDioxide.getCells(1),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                240,
                16);
        GT_Values.RA.addMixerRecipe(
                Materials.Calcite.getDust(5),
                Materials.Empty.getCells(1),
                GT_Utility.getIntegratedCircuit(14),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                Materials.AceticAcid.getFluid(2000),
                Materials.CarbonDioxide.getGas(1000),
                Materials.CalciumAcetateSolution.getCells(1),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                240,
                16);
        // Ca + 2 CH3COOH = Ca(CH3COO)2 + 2H
        GT_Values.RA.addMixerRecipe(
                Materials.Calcium.getDust(1),
                Materials.Empty.getCells(2),
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NI,
                Materials.AceticAcid.getFluid(2000),
                Materials.CalciumAcetateSolution.getFluid(1000),
                Materials.Hydrogen.getCells(2),
                80,
                16);
        GT_Values.RA.addMixerRecipe(
                Materials.Calcium.getDust(1),
                Materials.Empty.getCells(1),
                GT_Utility.getIntegratedCircuit(11),
                GT_Values.NI,
                Materials.AceticAcid.getFluid(2000),
                Materials.Hydrogen.getGas(2000),
                Materials.CalciumAcetateSolution.getCells(1),
                80,
                16);
        // CaO + 2 CH3COOH = Ca(CH3COO)2 + H2O
        GT_Values.RA.addMixerRecipe(
                Materials.Quicklime.getDust(2),
                Materials.Empty.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NI,
                Materials.AceticAcid.getFluid(2000),
                Materials.CalciumAcetateSolution.getFluid(1000),
                Materials.Water.getCells(1),
                80,
                16);
        GT_Values.RA.addMixerRecipe(
                Materials.Quicklime.getDust(2),
                Materials.Empty.getCells(1),
                GT_Utility.getIntegratedCircuit(11),
                GT_Values.NI,
                Materials.AceticAcid.getFluid(2000),
                Materials.Water.getFluid(1000),
                Materials.CalciumAcetateSolution.getCells(1),
                80,
                16);

        // 2CH3COOCH3 + 3CH3COCH3/(C4H6O2)n = 5Glue
        GT_Values.RA.addMixerRecipe(
                Materials.Acetone.getCells(3),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                Materials.PolyvinylAcetate.getFluid(2000),
                Materials.AdvancedGlue.getFluid(5000),
                Materials.Empty.getCells(3),
                100,
                8);
        GT_Values.RA.addMixerRecipe(
                Materials.PolyvinylAcetate.getCells(2),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                Materials.Acetone.getFluid(3000),
                Materials.AdvancedGlue.getFluid(5000),
                Materials.Empty.getCells(2),
                100,
                8);
        GT_Values.RA.addMixerRecipe(
                Materials.MethylAcetate.getCells(3),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                Materials.PolyvinylAcetate.getFluid(2000),
                Materials.AdvancedGlue.getFluid(5000),
                Materials.Empty.getCells(3),
                100,
                8);
        GT_Values.RA.addMixerRecipe(
                Materials.PolyvinylAcetate.getCells(2),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                Materials.MethylAcetate.getFluid(3000),
                Materials.AdvancedGlue.getFluid(5000),
                Materials.Empty.getCells(2),
                100,
                8);

        GT_Values.RA.addMixerRecipe(
                Materials.Sugar.getDust(4),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                Materials.SulfuricAcid.getFluid(1000),
                Materials.DilutedSulfuricAcid.getFluid(1000),
                Materials.Charcoal.getGems(1),
                1200,
                2);
        GT_Values.RA.addMixerRecipe(
                Materials.Wood.getDust(4),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                Materials.SulfuricAcid.getFluid(1000),
                Materials.DilutedSulfuricAcid.getFluid(1000),
                Materials.Charcoal.getGems(1),
                1200,
                2);

        GT_Values.RA.addMixerRecipe(
                Materials.Fuel.getCells(1),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                Materials.Tetranitromethane.getFluid(20),
                Materials.NitroFuel.getFluid(1000),
                Materials.Empty.getCells(1),
                20,
                480);
        GT_Values.RA.addMixerRecipe(
                Materials.BioDiesel.getCells(1),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                Materials.Tetranitromethane.getFluid(40),
                Materials.NitroFuel.getFluid(900),
                Materials.Empty.getCells(1),
                20,
                480);

        // CH4O + C4H8 = C5H12O
        GT_Values.RA.addMixerRecipe(
                Materials.Methanol.getCells(1),
                Materials.Butene.getCells(1),
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NI,
                GT_Values.NF,
                GT_Values.NF,
                Materials.MTBEMixture.getCells(1),
                Materials.Empty.getCells(1),
                GT_Values.NI,
                GT_Values.NI,
                20,
                480);
        GT_Values.RA.addMixerRecipe(
                Materials.Naphtha.getCells(16),
                Materials.Gas.getCells(2),
                Materials.Methanol.getCells(1),
                Materials.Acetone.getCells(1),
                GT_Values.NF,
                GT_Values.NF,
                Materials.GasolineRaw.getCells(20),
                100,
                480);

        GT_Values.RA.addMixerRecipe(
                Materials.GasolineRegular.getCells(20),
                Materials.Octane.getCells(2),
                Materials.NitrousOxide.getCells(6),
                Materials.Toluene.getCells(1),
                Materials.AntiKnock.getFluid(3000L),
                Materials.GasolinePremium.getFluid(32000L),
                Materials.Empty.getCells(29),
                50,
                1920);

        if (Railcraft.isModLoaded()) {
            GT_Values.RA.addMixerRecipe(
                    EnumCube.COKE_BLOCK.getItem(),
                    ItemList.SFMixture.get(2),
                    GT_Values.NI,
                    GT_Values.NI,
                    GT_Values.NI,
                    GT_Utility.getIntegratedCircuit(1),
                    Materials.NitroFuel.getFluid(300),
                    null,
                    ItemList.Block_SSFUEL.get(1),
                    100,
                    250);
            GT_Values.RA.addMixerRecipe(
                    EnumCube.COKE_BLOCK.getItem(),
                    ItemList.SFMixture.get(2),
                    GT_Values.NI,
                    GT_Values.NI,
                    GT_Values.NI,
                    GT_Utility.getIntegratedCircuit(1),
                    Materials.GasolinePremium.getFluid(120),
                    null,
                    ItemList.Block_SSFUEL.get(1),
                    100,
                    250);
        }

        if (Thaumcraft.isModLoaded() && Railcraft.isModLoaded()) {
            GT_Values.RA.addMixerRecipe(
                    EnumCube.COKE_BLOCK.getItem(),
                    ItemList.MSFMixture.get(2),
                    getModItem(MOD_ID_TC, "ItemResource", 4),
                    GT_Values.NI,
                    GT_Values.NI,
                    GT_Utility.getIntegratedCircuit(1),
                    Materials.NitroFuel.getFluid(300),
                    null,
                    ItemList.Block_MSSFUEL.get(1),
                    100,
                    600);
            GT_Values.RA.addMixerRecipe(
                    EnumCube.COKE_BLOCK.getItem(),
                    ItemList.MSFMixture.get(2),
                    getModItem(MOD_ID_TC, "ItemResource", 4),
                    GT_Values.NI,
                    GT_Values.NI,
                    GT_Utility.getIntegratedCircuit(1),
                    Materials.GasolinePremium.getFluid(120),
                    null,
                    ItemList.Block_MSSFUEL.get(1),
                    100,
                    600);
        }
    }

    public static void addMixerPotionRecipes(String aName, ItemStack aItem) {
        // splash
        if (!(FluidRegistry.getFluid("potion." + aName) == null
                || FluidRegistry.getFluid("potion." + aName + ".splash") == null))
            GT_Values.RA.addMixerRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gunpowder, 1L),
                    null,
                    null,
                    null,
                    new FluidStack(FluidRegistry.getFluid("potion." + aName), 750),
                    new FluidStack(FluidRegistry.getFluid("potion." + aName + ".splash"), 750),
                    null,
                    200,
                    24);
        // splash strong
        if (!(FluidRegistry.getFluid("potion." + aName + ".strong") == null
                || FluidRegistry.getFluid("potion." + aName + ".strong.splash") == null))
            GT_Values.RA.addMixerRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gunpowder, 1L),
                    null,
                    null,
                    null,
                    new FluidStack(FluidRegistry.getFluid("potion." + aName + ".strong"), 750),
                    new FluidStack(FluidRegistry.getFluid("potion." + aName + ".strong.splash"), 750),
                    null,
                    200,
                    24);
        // splash long
        if (!(FluidRegistry.getFluid("potion." + aName + ".long") == null
                || FluidRegistry.getFluid("potion." + aName + ".long.splash") == null))
            GT_Values.RA.addMixerRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gunpowder, 1L),
                    null,
                    null,
                    null,
                    new FluidStack(FluidRegistry.getFluid("potion." + aName + ".long"), 750),
                    new FluidStack(FluidRegistry.getFluid("potion." + aName + ".long.splash"), 750),
                    null,
                    200,
                    24);
    }
}
