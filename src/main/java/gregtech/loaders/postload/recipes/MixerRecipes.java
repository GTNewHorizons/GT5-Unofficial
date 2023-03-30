package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.GT_Values.*;
import static gregtech.api.enums.ModIDs.*;
import static gregtech.api.util.GT_ModHandler.getModItem;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sMixerRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sMultiblockMixerRecipes;
import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MixerRecipes implements Runnable {

    @Override
    public void run() {

        registerSingleBlockAndMulti();

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.EnderPearl, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Blaze, 1),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                GT_OreDictUnificator.getDust(Materials.EnderEye, OrePrefixes.dust.mMaterialAmount)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(5 * SECONDS)
            .eut(48)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gold, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silver, 1),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                GT_OreDictUnificator.getDust(Materials.Electrum, 2L * OrePrefixes.dust.mMaterialAmount)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(10 * SECONDS)
            .eut(8)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 2),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 1),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                GT_OreDictUnificator.getDust(Materials.Invar, 3L * OrePrefixes.dust.mMaterialAmount)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(15 * SECONDS)
            .eut(8)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 4),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Invar, 3),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Manganese, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chrome, 1),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                GT_OreDictUnificator.getDust(Materials.StainlessSteel, 9L * OrePrefixes.dust.mMaterialAmount)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chrome, 1),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                GT_OreDictUnificator.getDust(Materials.Kanthal, 3L * OrePrefixes.dust.mMaterialAmount)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 3),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Zinc, 1),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                GT_OreDictUnificator.getDust(Materials.Brass, 4L * OrePrefixes.dust.mMaterialAmount)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(2 * SECONDS)
            .eut(8)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 3),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Tin, 1),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                GT_OreDictUnificator.getDust(Materials.Bronze, 4L * OrePrefixes.dust.mMaterialAmount)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(2 * SECONDS)
            .eut(8)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 1),
                GT_Utility.getIntegratedCircuit(3)
            )
            .itemOutputs(
                GT_OreDictUnificator.getDust(Materials.Cupronickel, 2L * OrePrefixes.dust.mMaterialAmount)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(10 * SECONDS)
            .eut(24)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silver, 4),
                GT_Utility.getIntegratedCircuit(4)
            )
            .itemOutputs(
                GT_OreDictUnificator.getDust(Materials.SterlingSilver, 5L * OrePrefixes.dust.mMaterialAmount)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 3),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Electrum, 2),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                GT_OreDictUnificator.getDust(Materials.BlackBronze, 5L * OrePrefixes.dust.mMaterialAmount)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(25 * SECONDS)
            .eut(8)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Bismuth, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Brass, 4),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                GT_OreDictUnificator.getDust(Materials.BismuthBronze, 5L * OrePrefixes.dust.mMaterialAmount)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(25 * SECONDS)
            .eut(8)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.BlackBronze, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 3),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                GT_OreDictUnificator.getDust(Materials.BlackSteel, 5L * OrePrefixes.dust.mMaterialAmount)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(25 * SECONDS)
            .eut(8)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SterlingSilver, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.BismuthBronze, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.BlackSteel, 4),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 2),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                GT_OreDictUnificator.getDust(Materials.RedSteel, 8L * OrePrefixes.dust.mMaterialAmount)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(40 * SECONDS)
            .eut(8)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RoseGold, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Brass, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.BlackSteel, 4),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 2),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                GT_OreDictUnificator.getDust(Materials.BlueSteel, 8L * OrePrefixes.dust.mMaterialAmount)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(40 * SECONDS)
            .eut(8)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 3),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gold, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silver, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 5),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 15),
                GT_Utility.getIntegratedCircuit(14)
            )
            .itemOutputs(
                GT_OreDictUnificator.getDust(Materials.BlackSteel, 25L * OrePrefixes.dust.mMaterialAmount)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 4),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silver, 4),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Bismuth, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Zinc, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.BlackSteel, 20),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 10),
                GT_Utility.getIntegratedCircuit(15)
            )
            .itemOutputs(
                GT_OreDictUnificator.getDust(Materials.RedSteel, 40L * OrePrefixes.dust.mMaterialAmount)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 19),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gold, 16),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Zinc, 5),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.BlackSteel, 64),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.BlackSteel, 16),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 40),
                GT_Utility.getIntegratedCircuit(16)
            )
            .itemOutputs(
                GT_OreDictUnificator.getDust(Materials.BlueSteel, 64L * OrePrefixes.dust.mMaterialAmount),
                GT_OreDictUnificator.getDust(Materials.BlueSteel, 64L * OrePrefixes.dust.mMaterialAmount),
                GT_OreDictUnificator.getDust(Materials.BlueSteel, 32L * OrePrefixes.dust.mMaterialAmount)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(3 * MINUTES)
            .eut(TierEU.RECIPE_HV)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Cobalt, 5),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chrome, 2),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Molybdenum, 1),
                GT_Utility.getIntegratedCircuit(2)
            )
            .itemOutputs(
                GT_OreDictUnificator.getDust(Materials.Ultimet, 9L * OrePrefixes.dust.mMaterialAmount)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(45 * SECONDS)
            .eut(500)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Brass, 7),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Cobalt, 1),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                GT_OreDictUnificator.getDust(Materials.CobaltBrass, 9L * OrePrefixes.dust.mMaterialAmount)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(45 * SECONDS)
            .eut(8)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Saltpeter, 2),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 3),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                GT_OreDictUnificator.getDust(Materials.Gunpowder, 6L * OrePrefixes.dust.mMaterialAmount)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(30 * SECONDS)
            .eut(8)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Saltpeter, 2),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Charcoal, 3),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                GT_OreDictUnificator.getDust(Materials.Gunpowder, 6L * OrePrefixes.dust.mMaterialAmount)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(30 * SECONDS)
            .eut(8)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Saltpeter, 2),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 3),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                GT_OreDictUnificator.getDust(Materials.Gunpowder, 6L * OrePrefixes.dust.mMaterialAmount)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(30 * SECONDS)
            .eut(8)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Indium, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gallium, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Phosphorus, 1),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                GT_OreDictUnificator.getDust(Materials.IndiumGalliumPhosphide, 3L * OrePrefixes.dust.mMaterialAmount)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(10 * SECONDS)
            .eut(8)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Brick, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Clay, 1)
            )
            .itemOutputs(
                GT_OreDictUnificator.getDust(Materials.Fireclay, 2L * OrePrefixes.dust.mMaterialAmount)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(10 * SECONDS)
            .eut(8)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 4),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chrome, 1),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                GT_OreDictUnificator.getDust(Materials.Nichrome, 5L * OrePrefixes.dust.mMaterialAmount)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Osmium, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iridium, 3),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                GT_OreDictUnificator.getDust(Materials.Osmiridium, 4L * OrePrefixes.dust.mMaterialAmount)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(2 * SECONDS)
            .eut(2000)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Niobium, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Titanium, 1),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                GT_OreDictUnificator.getDust(Materials.NiobiumTitanium, 2L * OrePrefixes.dust.mMaterialAmount)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(10 * SECONDS)
            .eut(2000)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Vanadium, 3),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gallium, 1),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                GT_OreDictUnificator.getDust(Materials.VanadiumGallium, 4L * OrePrefixes.dust.mMaterialAmount)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(2 * SECONDS)
            .eut(2000)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Tungsten, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 1),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                GT_OreDictUnificator.getDust(Materials.TungstenCarbide, 2L * OrePrefixes.dust.mMaterialAmount)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(10 * SECONDS)
            .eut(500)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Tungsten, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 1),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                GT_OreDictUnificator.getDust(Materials.TungstenSteel, 2L * OrePrefixes.dust.mMaterialAmount)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Titanium, 3),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Platinum, 3),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Vanadium, 1),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                GT_OreDictUnificator.getDust(Materials.TPV, 7L * OrePrefixes.dust.mMaterialAmount)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(8 * SECONDS + 15 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.TungstenSteel, 5),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chrome, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Molybdenum, 2),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Vanadium, 1),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                GT_OreDictUnificator.getDust(Materials.HSSG, 9L * OrePrefixes.dust.mMaterialAmount)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.HSSG, 6),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Cobalt, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Manganese, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 1),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                GT_OreDictUnificator.getDust(Materials.HSSE, 9L * OrePrefixes.dust.mMaterialAmount)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(35 * SECONDS)
            .eut(4096)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.HSSG, 6),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iridium, 2),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Osmium, 1),
                GT_Utility.getIntegratedCircuit(2)
            )
            .itemOutputs(
                GT_OreDictUnificator.getDust(Materials.HSSS, 9L * OrePrefixes.dust.mMaterialAmount)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Zinc, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 4)
            )
            .itemOutputs(
                GT_OreDictUnificator.getDust(Materials.FerriteMixture, 6L * OrePrefixes.dust.mMaterialAmount)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(10 * SECONDS)
            .eut(8)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Boron, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glass, 7)
            )
            .itemOutputs(
                GT_OreDictUnificator.getDust(Materials.BorosilicateGlass, 8L * OrePrefixes.dust.mMaterialAmount)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(10 * SECONDS)
            .eut(8)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Items.rotten_flesh, 1, 0),
                new ItemStack(Items.fermented_spider_eye, 1, 0),
                ItemList.IC2_Scrap.get(1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.MeatRaw, 1)
            )
            .itemOutputs(
                ItemList.Food_Chum.get(4)
            )
            .fluidInputs(
                getFluidStack("potion.purpledrink", 750)
            )
            .fluidOutputs(
                getFluidStack("sludge", 1000)
            )
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(24)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wheat, 1)
            )
            .itemOutputs(
                ItemList.Food_Dough.get(2)
            )
            .fluidInputs(
                Materials.Water.getFluid(1000)
            )
            .noFluidOutputs()
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(8)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chili, 1),
                ItemList.Food_PotatoChips.get(1)
            )
            .itemOutputs(
                ItemList.Food_ChiliChips.get(1)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(8)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Redstone, 5),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Ruby, 4)
            )
            .itemOutputs(
                ItemList.IC2_Energium_Dust.get(1)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 5),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ruby, 4)
            )
            .itemOutputs(
                ItemList.IC2_Energium_Dust.get(9)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1),
                new ItemStack(Blocks.brown_mushroom, 1),
                new ItemStack(Items.spider_eye, 1)
            )
            .itemOutputs(
                new ItemStack(Items.fermented_spider_eye, 1)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(5 * SECONDS)
            .eut(8)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Gold, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.LiveRoot, 1)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.IronWood, 2)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(5 * SECONDS)
            .eut(8)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gold, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 9),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.LiveRoot, 9)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.IronWood, 18)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(45 * SECONDS)
            .eut(8)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(AppliedEnergistics2.modID, "item.ItemMultiMaterial", 1L, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1),
                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.NetherQuartz, 1)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Fluix, 2)
            )
            .fluidInputs(
                Materials.Water.getFluid(500)
            )
            .noFluidOutputs()
            .duration(20 * TICKS)
            .eut(16)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(AppliedEnergistics2.modID, "item.ItemMultiMaterial", 1L, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1),
                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.NetherQuartz, 1)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Fluix, 2)
            )
            .fluidInputs(
                GT_ModHandler.getDistilledWater(500)
            )
            .noFluidOutputs()
            .duration(20 * TICKS)
            .eut(16)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.IC2_Fertilizer.get(1),
                new ItemStack(Blocks.dirt, 8, 32767),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                getModItem(Forestry.modID, "soil", 8L, 0)
            )
            .fluidInputs(
                Materials.Water.getFluid(1000)
            )
            .noFluidOutputs()
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(16)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.FR_Fertilizer.get(1),
                new ItemStack(Blocks.dirt, 8, 32767),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                getModItem(Forestry.modID, "soil", 8L, 0)
            )
            .fluidInputs(
                Materials.Water.getFluid(1000)
            )
            .noFluidOutputs()
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(16)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.FR_Compost.get(1),
                new ItemStack(Blocks.dirt, 8, 32767),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                getModItem(Forestry.modID, "soil", 8L, 0)
            )
            .fluidInputs(
                Materials.Water.getFluid(1000)
            )
            .noFluidOutputs()
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(16)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.FR_Mulch.get(8),
                new ItemStack(Blocks.dirt, 8, 32767),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                getModItem(Forestry.modID, "soil", 8L, 0)
            )
            .fluidInputs(
                Materials.Water.getFluid(1000)
            )
            .noFluidOutputs()
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(16)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.sand, 1, 32767),
                new ItemStack(Blocks.dirt, 1, 32767),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                getModItem(Forestry.modID, "soil", 2L, 1)
            )
            .fluidInputs(
                Materials.Water.getFluid(250)
            )
            .noFluidOutputs()
            .duration(16 * TICKS)
            .eut(16)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.LightFuel, 5),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.HeavyFuel, 1)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Fuel, 6)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.LightFuel, 5),
                Materials.Empty.getCells(1),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Fuel, 6)
            )
            .fluidInputs(
                Materials.HeavyFuel.getFluid(1000)
            )
            .noFluidOutputs()
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.HeavyFuel, 1),
                Materials.Empty.getCells(5),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Fuel, 6)
            )
            .fluidInputs(
                Materials.LightFuel.getFluid(5000)
            )
            .noFluidOutputs()
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.LightFuel, 5),
                GT_Utility.getIntegratedCircuit(5)
            )
            .itemOutputs(
                Materials.Empty.getCells(5)
            )
            .fluidInputs(
                Materials.HeavyFuel.getFluid(1000)
            )
            .fluidOutputs(
                Materials.Fuel.getFluid(6000)
            )
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.HeavyFuel, 1),
                GT_Utility.getIntegratedCircuit(6)
            )
            .itemOutputs(
                Materials.Empty.getCells(1)
            )
            .fluidInputs(
                Materials.LightFuel.getFluid(5000)
            )
            .fluidOutputs(
                Materials.Fuel.getFluid(6000)
            )
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Water, 5),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1)
            )
            .itemOutputs(
                Materials.Empty.getCells(5)
            )
            .fluidInputs(
                Materials.Lubricant.getFluid(20)
            )
            .fluidOutputs(
                new FluidStack(ItemList.sDrillingFluid, 5000)
            )
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(16)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lapis, 1),
                GT_Utility.getIntegratedCircuit(4)
            )
            .noItemOutputs()
            .fluidInputs(
                Materials.Water.getFluid(125)
            )
            .fluidOutputs(
                getFluidStack("ic2coolant", 125)
            )
            .duration(12 * SECONDS + 16 * TICKS)
            .eut(48)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lapis, 1),
                GT_Utility.getIntegratedCircuit(4)
            )
            .noItemOutputs()
            .fluidInputs(
                GT_ModHandler.getDistilledWater(1000)
            )
            .fluidOutputs(
                getFluidStack("ic2coolant", 1000)
            )
            .duration(12 * SECONDS + 16 * TICKS)
            .eut(48)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 4),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                ItemList.SFMixture.get(4)
            )
            .fluidInputs(
                Materials.AdvancedGlue.getFluid(200)
            )
            .noFluidOutputs()
            .duration(40 * SECONDS)
            .eut(16)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lithium, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 4),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                ItemList.SFMixture.get(8)
            )
            .fluidInputs(
                Materials.AdvancedGlue.getFluid(200)
            )
            .noFluidOutputs()
            .duration(40 * SECONDS)
            .eut(16)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Caesium, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 4),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                ItemList.SFMixture.get(12)
            )
            .fluidInputs(
                Materials.AdvancedGlue.getFluid(200)
            )
            .noFluidOutputs()
            .duration(40 * SECONDS)
            .eut(16)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.SFMixture.get(2),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.EnderEye, 1)
            )
            .itemOutputs(
                ItemList.MSFMixture.get(4)
            )
            .fluidInputs(
                Materials.Mercury.getFluid(1000)
            )
            .noFluidOutputs()
            .duration(15 * SECONDS)
            .eut(64)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.SFMixture.get(1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Blaze, 1)
            )
            .itemOutputs(
                ItemList.MSFMixture.get(1)
            )
            .fluidInputs(
                Materials.Mercury.getFluid(500)
            )
            .noFluidOutputs()
            .duration(15 * SECONDS)
            .eut(64)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Lignite, 4),
                ItemList.MSFMixture.get(24),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Diamond, 1),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                ItemList.Block_MSSFUEL.get(4)
            )
            .fluidInputs(
                Materials.NitroFuel.getFluid(4000)
            )
            .noFluidOutputs()
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Charcoal, 4),
                ItemList.MSFMixture.get(16),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Diamond, 1),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                ItemList.Block_MSSFUEL.get(4)
            )
            .fluidInputs(
                Materials.NitroFuel.getFluid(3000)
            )
            .noFluidOutputs()
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Coal, 4),
                ItemList.MSFMixture.get(8),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Diamond, 1),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                ItemList.Block_MSSFUEL.get(4)
            )
            .fluidInputs(
                Materials.NitroFuel.getFluid(2000)
            )
            .noFluidOutputs()
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Lignite, 4),
                ItemList.MSFMixture.get(24),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Diamond, 1),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                ItemList.Block_MSSFUEL.get(4)
            )
            .fluidInputs(
                Materials.GasolinePremium.getFluid(1600)
            )
            .noFluidOutputs()
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Charcoal, 4),
                ItemList.MSFMixture.get(16),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Diamond, 1),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                ItemList.Block_MSSFUEL.get(4)
            )
            .fluidInputs(
                Materials.GasolinePremium.getFluid(1200)
            )
            .noFluidOutputs()
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Coal, 4),
                ItemList.MSFMixture.get(8),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Diamond, 1),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                ItemList.Block_MSSFUEL.get(4)
            )
            .fluidInputs(
                Materials.GasolinePremium.getFluid(800)
            )
            .noFluidOutputs()
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sMixerRecipes);

        if (Thaumcraft.isModLoaded()) {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.SFMixture.get(20),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfusedAir, 1),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.MSFMixture.get(20)
                )
                .fluidInputs(
                    Materials.FierySteel.getFluid(50)
                )
                .noFluidOutputs()
                .duration(10 * SECONDS)
                .eut(64)
                .addTo(sMixerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.SFMixture.get(20),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfusedEarth, 1),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.MSFMixture.get(20)
                )
                .fluidInputs(
                    Materials.FierySteel.getFluid(50)
                )
                .noFluidOutputs()
                .duration(10 * SECONDS)
                .eut(64)
                .addTo(sMixerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.SFMixture.get(20),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfusedEntropy, 1),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.MSFMixture.get(20)
                )
                .fluidInputs(
                    Materials.FierySteel.getFluid(50)
                )
                .noFluidOutputs()
                .duration(10 * SECONDS)
                .eut(64)
                .addTo(sMixerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.SFMixture.get(20),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfusedFire, 1),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.MSFMixture.get(20)
                )
                .fluidInputs(
                    Materials.FierySteel.getFluid(50)
                )
                .noFluidOutputs()
                .duration(10 * SECONDS)
                .eut(64)
                .addTo(sMixerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.SFMixture.get(20),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfusedOrder, 1),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.MSFMixture.get(20)
                )
                .fluidInputs(
                    Materials.FierySteel.getFluid(50)
                )
                .noFluidOutputs()
                .duration(10 * SECONDS)
                .eut(64)
                .addTo(sMixerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.SFMixture.get(20),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfusedWater, 1),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.MSFMixture.get(20)
                )
                .fluidInputs(
                    Materials.FierySteel.getFluid(50)
                )
                .noFluidOutputs()
                .duration(10 * SECONDS)
                .eut(64)
                .addTo(sMixerRecipes);

            FluidStack tFD = getFluidStack("fluiddeath", 30);
            if (tFD != null && tFD.getFluid() != null && tFD.amount > 0) {
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.SFMixture.get(30),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfusedAir, 1),
                        GT_Utility.getIntegratedCircuit(1)
                    )
                    .itemOutputs(
                        ItemList.MSFMixture.get(30)
                    )
                    .fluidInputs(
                        tFD
                    )
                    .noFluidOutputs()
                    .duration(10 * SECONDS)
                    .eut(64)
                    .addTo(sMixerRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.SFMixture.get(30),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfusedEarth, 1),
                        GT_Utility.getIntegratedCircuit(1)
                    )
                    .itemOutputs(
                        ItemList.MSFMixture.get(30)
                    )
                    .fluidInputs(
                        tFD
                    )
                    .noFluidOutputs()
                    .duration(10 * SECONDS)
                    .eut(64)
                    .addTo(sMixerRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.SFMixture.get(30),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfusedEntropy, 1),
                        GT_Utility.getIntegratedCircuit(1)
                    )
                    .itemOutputs(
                        ItemList.MSFMixture.get(30)
                    )
                    .fluidInputs(
                        tFD
                    )
                    .noFluidOutputs()
                    .duration(10 * SECONDS)
                    .eut(64)
                    .addTo(sMixerRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.SFMixture.get(30),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfusedFire, 1),
                        GT_Utility.getIntegratedCircuit(1)
                    )
                    .itemOutputs(
                        ItemList.MSFMixture.get(30)
                    )
                    .fluidInputs(
                        tFD
                    )
                    .noFluidOutputs()
                    .duration(10 * SECONDS)
                    .eut(64)
                    .addTo(sMixerRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.SFMixture.get(30),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfusedOrder, 1),
                        GT_Utility.getIntegratedCircuit(1)
                    )
                    .itemOutputs(
                        ItemList.MSFMixture.get(30)
                    )
                    .fluidInputs(
                        tFD
                    )
                    .noFluidOutputs()
                    .duration(10 * SECONDS)
                    .eut(64)
                    .addTo(sMixerRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.SFMixture.get(30),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfusedWater, 1),
                        GT_Utility.getIntegratedCircuit(1)
                    )
                    .itemOutputs(
                        ItemList.MSFMixture.get(30)
                    )
                    .fluidInputs(
                        tFD
                    )
                    .noFluidOutputs()
                    .duration(10 * SECONDS)
                    .eut(64)
                    .addTo(sMixerRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.block, Materials.Lignite, 1),
                        ItemList.MSFMixture.get(6),
                        getModItem(Thaumcraft.modID, "ItemResource", 4),
                        GT_Utility.getIntegratedCircuit(1)
                    )
                    .itemOutputs(
                        ItemList.Block_MSSFUEL.get(1)
                    )
                    .fluidInputs(
                        Materials.NitroFuel.getFluid(1000)
                    )
                    .noFluidOutputs()
                    .duration(7 * SECONDS)
                    .eut(TierEU.RECIPE_HV)
                    .addTo(sMixerRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.block, Materials.Charcoal, 1),
                        ItemList.MSFMixture.get(4),
                        getModItem(Thaumcraft.modID, "ItemResource", 4),
                        GT_Utility.getIntegratedCircuit(1)
                    )
                    .itemOutputs(
                        ItemList.Block_MSSFUEL.get(1)
                    )
                    .fluidInputs(
                        Materials.NitroFuel.getFluid(750)
                    )
                    .noFluidOutputs()
                    .duration(6 * SECONDS)
                    .eut(TierEU.RECIPE_HV)
                    .addTo(sMixerRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.block, Materials.Coal, 1),
                        ItemList.MSFMixture.get(2),
                        getModItem(Thaumcraft.modID, "ItemResource", 4),
                        GT_Utility.getIntegratedCircuit(1)
                    )
                    .itemOutputs(
                        ItemList.Block_MSSFUEL.get(1)
                    )
                    .fluidInputs(
                        Materials.NitroFuel.getFluid(500)
                    )
                    .noFluidOutputs()
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_HV)
                    .addTo(sMixerRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.block, Materials.Lignite, 1),
                        ItemList.MSFMixture.get(6),
                        getModItem(Thaumcraft.modID, "ItemResource", 4),
                        GT_Utility.getIntegratedCircuit(1)
                    )
                    .itemOutputs(
                        ItemList.Block_MSSFUEL.get(1)
                    )
                    .fluidInputs(
                        Materials.GasolinePremium.getFluid(400)
                    )
                    .noFluidOutputs()
                    .duration(7 * SECONDS)
                    .eut(TierEU.RECIPE_HV)
                    .addTo(sMixerRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.block, Materials.Charcoal, 1),
                        ItemList.MSFMixture.get(4),
                        getModItem(Thaumcraft.modID, "ItemResource", 4),
                        GT_Utility.getIntegratedCircuit(1)
                    )
                    .itemOutputs(
                        ItemList.Block_MSSFUEL.get(1)
                    )
                    .fluidInputs(
                        Materials.GasolinePremium.getFluid(300)
                    )
                    .noFluidOutputs()
                    .duration(6 * SECONDS)
                    .eut(TierEU.RECIPE_HV)
                    .addTo(sMixerRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.block, Materials.Coal, 1),
                        ItemList.MSFMixture.get(2),
                        getModItem(Thaumcraft.modID, "ItemResource", 4),
                        GT_Utility.getIntegratedCircuit(1)
                    )
                    .itemOutputs(
                        ItemList.Block_MSSFUEL.get(1)
                    )
                    .fluidInputs(
                        Materials.GasolinePremium.getFluid(200)
                    )
                    .noFluidOutputs()
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_HV)
                    .addTo(sMixerRecipes);
            }
        }

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Lignite, 1),
                ItemList.SFMixture.get(6),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                ItemList.Block_SSFUEL.get(1)
            )
            .fluidInputs(
                Materials.NitroFuel.getFluid(1000)
            )
            .noFluidOutputs()
            .duration(7 * SECONDS)
            .eut(250)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Charcoal, 1),
                ItemList.SFMixture.get(4),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                ItemList.Block_SSFUEL.get(1)
            )
            .fluidInputs(
                Materials.NitroFuel.getFluid(750)
            )
            .noFluidOutputs()
            .duration(6 * SECONDS)
            .eut(250)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Coal, 1),
                ItemList.SFMixture.get(2),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                ItemList.Block_SSFUEL.get(1)
            )
            .fluidInputs(
                Materials.NitroFuel.getFluid(500)
            )
            .noFluidOutputs()
            .duration(5 * SECONDS)
            .eut(250)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Lignite, 1),
                ItemList.SFMixture.get(6),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                ItemList.Block_SSFUEL.get(1)
            )
            .fluidInputs(
                Materials.GasolinePremium.getFluid(400)
            )
            .noFluidOutputs()
            .duration(7 * SECONDS)
            .eut(250)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Charcoal, 1),
                ItemList.SFMixture.get(4),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                ItemList.Block_SSFUEL.get(1)
            )
            .fluidInputs(
                Materials.GasolinePremium.getFluid(300)
            )
            .noFluidOutputs()
            .duration(6 * SECONDS)
            .eut(250)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Coal, 1),
                ItemList.SFMixture.get(2),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                ItemList.Block_SSFUEL.get(1)
            )
            .fluidInputs(
                Materials.GasolinePremium.getFluid(200)
            )
            .noFluidOutputs()
            .duration(5 * SECONDS)
            .eut(250)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.SulfuricAcid, 1),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                ItemList.Cell_Empty.get(1)
            )
            .fluidInputs(
                Materials.NitricAcid.getFluid(1000)
            )
            .fluidOutputs(
                new FluidStack(ItemList.sNitrationMixture, 2000)
            )
            .duration(24 * SECONDS)
            .eut(2)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.SulfuricAcid, 1),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.NitricAcid, 1),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.NitrationMixture, 2)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(24 * SECONDS)
            .eut(2)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.dirt, 1, 32767),
                new ItemStack(Items.wheat, 4, 32767),
                GT_Utility.getIntegratedCircuit(2)
            )
            .itemOutputs(
                getModItem(Forestry.modID, "fertilizerBio", 1L, 0)
            )
            .fluidInputs(
                Materials.Water.getFluid(100)
            )
            .noFluidOutputs()
            .duration(10 * SECONDS)
            .eut(16)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.dirt, 1, 2),
                new ItemStack(Items.wheat, 4, 32767),
                GT_Utility.getIntegratedCircuit(2)
            )
            .itemOutputs(
                getModItem(Forestry.modID, "fertilizerBio", 1L, 0)
            )
            .fluidInputs(
                Materials.Water.getFluid(100)
            )
            .noFluidOutputs()
            .duration(10 * SECONDS)
            .eut(16)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.dirt, 1, 32767),
                getModItem(BiomesOPlanty.modID, "plants", 4, 6),
                GT_Utility.getIntegratedCircuit(2)
            )
            .itemOutputs(
                getModItem(Forestry.modID, "fertilizerBio", 1L, 0)
            )
            .fluidInputs(
                Materials.Water.getFluid(100)
            )
            .noFluidOutputs()
            .duration(10 * SECONDS)
            .eut(16)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.dirt, 1, 2),
                getModItem(BiomesOPlanty.modID, "plants", 4, 6),
                GT_Utility.getIntegratedCircuit(2)
            )
            .itemOutputs(
                getModItem(Forestry.modID, "fertilizerBio", 1L, 0)
            )
            .fluidInputs(
                Materials.Water.getFluid(100)
            )
            .noFluidOutputs()
            .duration(10 * SECONDS)
            .eut(16)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.dirt, 1, 32767),
                getModItem(PamsHarvestCraft.modID, "oatsItem", 4),
                GT_Utility.getIntegratedCircuit(2)
            )
            .itemOutputs(
                getModItem(Forestry.modID, "fertilizerBio", 1L, 0)
            )
            .fluidInputs(
                Materials.Water.getFluid(100)
            )
            .noFluidOutputs()
            .duration(10 * SECONDS)
            .eut(16)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.dirt, 1, 2),
                getModItem(PamsHarvestCraft.modID, "oatsItem", 4),
                GT_Utility.getIntegratedCircuit(2)
            )
            .itemOutputs(
                getModItem(Forestry.modID, "fertilizerBio", 1L, 0)
            )
            .fluidInputs(
                Materials.Water.getFluid(100)
            )
            .noFluidOutputs()
            .duration(10 * SECONDS)
            .eut(16)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.dirt, 1, 32767),
                getModItem(PamsHarvestCraft.modID, "ryeItem", 4),
                GT_Utility.getIntegratedCircuit(2)
            )
            .itemOutputs(
                getModItem(Forestry.modID, "fertilizerBio", 1L, 0)
            )
            .fluidInputs(
                Materials.Water.getFluid(100)
            )
            .noFluidOutputs()
            .duration(10 * SECONDS)
            .eut(16)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.dirt, 1, 2),
                getModItem(PamsHarvestCraft.modID, "ryeItem", 4),
                GT_Utility.getIntegratedCircuit(2)
            )
            .itemOutputs(
                getModItem(Forestry.modID, "fertilizerBio", 1L, 0)
            )
            .fluidInputs(
                Materials.Water.getFluid(100)
            )
            .noFluidOutputs()
            .duration(10 * SECONDS)
            .eut(16)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.dirt, 1, 32767),
                getModItem(PamsHarvestCraft.modID, "barleyItem", 4),
                GT_Utility.getIntegratedCircuit(2)
            )
            .itemOutputs(
                getModItem(Forestry.modID, "fertilizerBio", 1L, 0)
            )
            .fluidInputs(
                Materials.Water.getFluid(100)
            )
            .noFluidOutputs()
            .duration(10 * SECONDS)
            .eut(16)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.dirt, 1, 2),
                getModItem(PamsHarvestCraft.modID, "barleyItem", 4, 6),
                GT_Utility.getIntegratedCircuit(2)
            )
            .itemOutputs(
                getModItem(Forestry.modID, "fertilizerBio", 1L, 0)
            )
            .fluidInputs(
                Materials.Water.getFluid(100)
            )
            .noFluidOutputs()
            .duration(10 * SECONDS)
            .eut(16)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.dirt, 1, 32767),
                getModItem(Natura.modID, "barleyFood", 4),
                GT_Utility.getIntegratedCircuit(2)
            )
            .itemOutputs(
                getModItem(Forestry.modID, "fertilizerBio", 1L, 0)
            )
            .fluidInputs(
                Materials.Water.getFluid(100)
            )
            .noFluidOutputs()
            .duration(10 * SECONDS)
            .eut(16)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.dirt, 1, 2),
                getModItem(Natura.modID, "barleyFood", 4),
                GT_Utility.getIntegratedCircuit(2)
            )
            .itemOutputs(
                getModItem(Forestry.modID, "fertilizerBio", 1L, 0)
            )
            .fluidInputs(
                Materials.Water.getFluid(100)
            )
            .noFluidOutputs()
            .duration(10 * SECONDS)
            .eut(16)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.dirt, 1, 32767),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 4),
                GT_Utility.getIntegratedCircuit(3)
            )
            .itemOutputs(
                getModItem(Forestry.modID, "fertilizerBio", 1L, 0)
            )
            .fluidInputs(
                Materials.Water.getFluid(100)
            )
            .noFluidOutputs()
            .duration(10 * SECONDS)
            .eut(16)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.dirt, 1, 2),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 4),
                GT_Utility.getIntegratedCircuit(3)
            )
            .itemOutputs(
                getModItem(Forestry.modID, "fertilizerBio", 1L, 0)
            )
            .fluidInputs(
                Materials.Water.getFluid(100)
            )
            .noFluidOutputs()
            .duration(10 * SECONDS)
            .eut(16)
            .addTo(sMixerRecipes);

        // radiation manufacturing

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_ModHandler.getIC2Item("fuelRod", 1),
                new ItemStack(Items.glowstone_dust, 9),
                NI,
                NI
            )
            .itemOutputs(
                ItemList.GlowstoneCell.get(1)
            )
            .fluidInputs(
                Materials.Helium.getGas(250)
            )
            .fluidOutputs(
                NF
            )
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                MaterialsOreAlum.SluiceSand.getDust(1)
            )
            .noItemOutputs()
            .fluidInputs(
                Materials.Water.getFluid(500)
            )
            .fluidOutputs(
                MaterialsOreAlum.SluiceJuice.getFluid(1000)
            )
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(sMixerRecipes);

        // NaCl + H2O = (NaCl·H2O)

        GT_Values.RA.stdBuilder()
            .itemInputs(
                Materials.Salt.getDust(2),
                GT_Utility.getIntegratedCircuit(3)
            )
            .noItemOutputs()
            .fluidInputs(
                Materials.Water.getFluid(1000)
            )
            .fluidOutputs(
                Materials.SaltWater.getFluid(1000)
            )
            .duration(5 * SECONDS)
            .eut(8)
            .addTo(sMixerRecipes);

        // CaCO3 + 2 CH3COOH = Ca(CH3COO)2 + H2O + CO2

        GT_Values.RA.stdBuilder()
            .itemInputs(
                Materials.Calcite.getDust(5),
                Materials.Empty.getCells(2),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                Materials.Water.getCells(1),
                Materials.CarbonDioxide.getCells(1)
            )
            .fluidInputs(
                Materials.AceticAcid.getFluid(2000)
            )
            .fluidOutputs(
                Materials.CalciumAcetateSolution.getFluid(1000)
            )
            .duration(12 * SECONDS)
            .eut(16)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                Materials.Calcite.getDust(5),
                Materials.Empty.getCells(2),
                GT_Utility.getIntegratedCircuit(11)
            )
            .itemOutputs(
                Materials.CalciumAcetateSolution.getCells(1),
                Materials.CarbonDioxide.getCells(1)
            )
            .fluidInputs(
                Materials.AceticAcid.getFluid(2000)
            )
            .fluidOutputs(
                Materials.Water.getFluid(1000)
            )
            .duration(12 * SECONDS)
            .eut(16)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                Materials.Calcite.getDust(5),
                Materials.Empty.getCells(2),
                GT_Utility.getIntegratedCircuit(21)
            )
            .itemOutputs(
                Materials.Water.getCells(1),
                Materials.CalciumAcetateSolution.getCells(1)
            )
            .fluidInputs(
                Materials.AceticAcid.getFluid(2000)
            )
            .fluidOutputs(
                Materials.CarbonDioxide.getGas(1000)
            )
            .duration(12 * SECONDS)
            .eut(16)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                Materials.Calcite.getDust(5),
                Materials.Empty.getCells(1),
                GT_Utility.getIntegratedCircuit(4)
            )
            .itemOutputs(
                Materials.CarbonDioxide.getCells(1)
            )
            .fluidInputs(
                Materials.AceticAcid.getFluid(2000)
            )
            .fluidOutputs(
                Materials.CalciumAcetateSolution.getFluid(1000)
            )
            .duration(12 * SECONDS)
            .eut(16)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                Materials.Calcite.getDust(5),
                Materials.Empty.getCells(1),
                GT_Utility.getIntegratedCircuit(14)
            )
            .itemOutputs(
                Materials.CalciumAcetateSolution.getCells(1)
            )
            .fluidInputs(
                Materials.AceticAcid.getFluid(2000)
            )
            .fluidOutputs(
                Materials.CarbonDioxide.getGas(1000)
            )
            .duration(12 * SECONDS)
            .eut(16)
            .addTo(sMixerRecipes);

        // Ca + 2 CH3COOH = Ca(CH3COO)2 + 2H

        GT_Values.RA.stdBuilder()
            .itemInputs(
                Materials.Calcium.getDust(1),
                Materials.Empty.getCells(2),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                Materials.Hydrogen.getCells(2)
            )
            .fluidInputs(
                Materials.AceticAcid.getFluid(2000)
            )
            .fluidOutputs(
                Materials.CalciumAcetateSolution.getFluid(1000)
            )
            .duration(4 * SECONDS)
            .eut(16)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                Materials.Calcium.getDust(1),
                Materials.Empty.getCells(1),
                GT_Utility.getIntegratedCircuit(11)
            )
            .itemOutputs(
                Materials.CalciumAcetateSolution.getCells(1)
            )
            .fluidInputs(
                Materials.AceticAcid.getFluid(2000)
            )
            .fluidOutputs(
                Materials.Hydrogen.getGas(2000)
            )
            .duration(4 * SECONDS)
            .eut(16)
            .addTo(sMixerRecipes);

        // CaO + 2 CH3COOH = Ca(CH3COO)2 + H2O

        GT_Values.RA.stdBuilder()
            .itemInputs(
                Materials.Quicklime.getDust(2),
                Materials.Empty.getCells(1),
                GT_Utility.getIntegratedCircuit(1)
            )
            .itemOutputs(
                Materials.Water.getCells(1)
            )
            .fluidInputs(
                Materials.AceticAcid.getFluid(2000)
            )
            .fluidOutputs(
                Materials.CalciumAcetateSolution.getFluid(1000)
            )
            .duration(4 * SECONDS)
            .eut(16)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                Materials.Quicklime.getDust(2),
                Materials.Empty.getCells(1),
                GT_Utility.getIntegratedCircuit(11)
            )
            .itemOutputs(
                Materials.CalciumAcetateSolution.getCells(1)
            )
            .fluidInputs(
                Materials.AceticAcid.getFluid(2000)
            )
            .fluidOutputs(
                Materials.Water.getFluid(1000)
            )
            .duration(4 * SECONDS)
            .eut(16)
            .addTo(sMixerRecipes);

        // 2CH3COOCH3 + 3CH3COCH3/(C4H6O2)n = 5Glue

        GT_Values.RA.stdBuilder()
            .itemInputs(
                Materials.Acetone.getCells(3)
            )
            .itemOutputs(
                Materials.Empty.getCells(3)
            )
            .fluidInputs(
                Materials.PolyvinylAcetate.getFluid(2000)
            )
            .fluidOutputs(
                Materials.AdvancedGlue.getFluid(5000)
            )
            .duration(5 * SECONDS)
            .eut(8)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                Materials.PolyvinylAcetate.getCells(2)
            )
            .itemOutputs(
                Materials.Empty.getCells(2)
            )
            .fluidInputs(
                Materials.Acetone.getFluid(3000)
            )
            .fluidOutputs(
                Materials.AdvancedGlue.getFluid(5000)
            )
            .duration(5 * SECONDS)
            .eut(8)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                Materials.MethylAcetate.getCells(3)
            )
            .itemOutputs(
                Materials.Empty.getCells(3)
            )
            .fluidInputs(
                Materials.PolyvinylAcetate.getFluid(2000)
            )
            .fluidOutputs(
                Materials.AdvancedGlue.getFluid(5000)
            )
            .duration(5 * SECONDS)
            .eut(8)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                Materials.PolyvinylAcetate.getCells(2)
            )
            .itemOutputs(
                Materials.Empty.getCells(2)
            )
            .fluidInputs(
                Materials.MethylAcetate.getFluid(3000)
            )
            .fluidOutputs(
                Materials.AdvancedGlue.getFluid(5000)
            )
            .duration(5 * SECONDS)
            .eut(8)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                Materials.Sugar.getDust(4)
            )
            .itemOutputs(
                Materials.Charcoal.getGems(1)
            )
            .fluidInputs(
                Materials.SulfuricAcid.getFluid(1000)
            )
            .fluidOutputs(
                Materials.DilutedSulfuricAcid.getFluid(1000)
            )
            .duration(60 * SECONDS)
            .eut(2)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                Materials.Wood.getDust(4)
            )
            .itemOutputs(
                Materials.Charcoal.getGems(1)
            )
            .fluidInputs(
                Materials.SulfuricAcid.getFluid(1000)
            )
            .fluidOutputs(
                Materials.DilutedSulfuricAcid.getFluid(1000)
            )
            .duration(60 * SECONDS)
            .eut(2)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                Materials.Fuel.getCells(1)
            )
            .itemOutputs(
                Materials.Empty.getCells(1)
            )
            .fluidInputs(
                Materials.Tetranitromethane.getFluid(20)
            )
            .fluidOutputs(
                Materials.NitroFuel.getFluid(1000)
            )
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                Materials.BioDiesel.getCells(1)
            )
            .itemOutputs(
                Materials.Empty.getCells(1)
            )
            .fluidInputs(
                Materials.Tetranitromethane.getFluid(40)
            )
            .fluidOutputs(
                Materials.NitroFuel.getFluid(900)
            )
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sMixerRecipes);

        // CH4O + C4H8 = C5H12O

        GT_Values.RA.stdBuilder()
            .itemInputs(
                Materials.Methanol.getCells(1),
                Materials.Butene.getCells(1)
            )
            .itemOutputs(
                Materials.MTBEMixture.getCells(1),
                Materials.Empty.getCells(1)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                Materials.Naphtha.getCells(16),
                Materials.Gas.getCells(2),
                Materials.Methanol.getCells(1),
                Materials.Acetone.getCells(1)
            )
            .itemOutputs(
                Materials.GasolineRaw.getCells(20)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sMixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                Materials.GasolineRegular.getCells(20),
                Materials.Octane.getCells(2),
                Materials.NitrousOxide.getCells(6),
                Materials.Toluene.getCells(1)
            )
            .itemOutputs(
                Materials.Empty.getCells(29)
            )
            .fluidInputs(
                Materials.AntiKnock.getFluid(3000)
            )
            .fluidOutputs(
                Materials.GasolinePremium.getFluid(32000)
            )
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(sMixerRecipes);

        if (Railcraft.isModLoaded()) {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    EnumCube.COKE_BLOCK.getItem(),
                    ItemList.SFMixture.get(2),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Block_SSFUEL.get(1)
                )
                .fluidInputs(
                    Materials.NitroFuel.getFluid(300)
                )
                .noFluidOutputs()
                .duration(5 * SECONDS)
                .eut(250)
                .addTo(sMixerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    EnumCube.COKE_BLOCK.getItem(),
                    ItemList.SFMixture.get(2),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Block_SSFUEL.get(1)
                )
                .fluidInputs(
                    Materials.GasolinePremium.getFluid(120)
                )
                .noFluidOutputs()
                .duration(5 * SECONDS)
                .eut(250)
                .addTo(sMixerRecipes);
        }

        if (Thaumcraft.isModLoaded() && Railcraft.isModLoaded()) {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    EnumCube.COKE_BLOCK.getItem(),
                    ItemList.MSFMixture.get(2),
                    getModItem(Thaumcraft.modID, "ItemResource", 4),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Block_MSSFUEL.get(1)
                )
                .fluidInputs(
                    Materials.NitroFuel.getFluid(300)
                )
                .noFluidOutputs()
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(sMixerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    EnumCube.COKE_BLOCK.getItem(),
                    ItemList.MSFMixture.get(2),
                    getModItem(Thaumcraft.modID, "ItemResource", 4),
                    GT_Utility.getIntegratedCircuit(1)
                )
                .itemOutputs(
                    ItemList.Block_MSSFUEL.get(1)
                )
                .fluidInputs(
                    Materials.GasolinePremium.getFluid(120)
                )
                .noFluidOutputs()
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(sMixerRecipes);
        }
    }

    public static void addMixerPotionRecipes(String aName) {

        boolean splash = !(FluidRegistry.getFluid("potion." + aName) == null || FluidRegistry.getFluid("potion." + aName + ".splash") == null);
        boolean splashStrong = !(FluidRegistry.getFluid("potion." + aName + ".strong") == null || FluidRegistry.getFluid("potion." + aName + ".strong.splash") == null);
        boolean splashLong = !(FluidRegistry.getFluid("potion." + aName + ".long") == null || FluidRegistry.getFluid("potion." + aName + ".long.splash") == null);

        if (splash)
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gunpowder, 1)
                )
                .noItemOutputs()
                .fluidInputs(
                    new FluidStack(FluidRegistry.getFluid("potion." + aName), 750)
                )
                .fluidOutputs(
                    new FluidStack(FluidRegistry.getFluid("potion." + aName + ".splash"), 750)
                )
                .duration(10 * SECONDS)
                .eut(24)
                .addTo(sMixerRecipes);

        if (splashStrong)
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gunpowder, 1)
                )
                .noItemOutputs()
                .fluidInputs(
                    new FluidStack(FluidRegistry.getFluid("potion." + aName + ".strong"), 750)
                )
                .fluidOutputs(
                    new FluidStack(FluidRegistry.getFluid("potion." + aName + ".strong.splash"), 750)
                )
                .duration(10 * SECONDS)
                .eut(24)
                .addTo(sMixerRecipes);

        if (splashLong)
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gunpowder, 1)
                )
                .noItemOutputs()
                .fluidInputs(
                    new FluidStack(FluidRegistry.getFluid("potion." + aName + ".long"), 750)
                )
                .fluidOutputs(
                    new FluidStack(FluidRegistry.getFluid("potion." + aName + ".long.splash"), 750)
                )
                .duration(10 * SECONDS)
                .eut(24)
                .addTo(sMixerRecipes);
    }

    public void addMixerRecipe(ItemStack[] itemInputArray, FluidStack[] fluidInputArray, ItemStack[] itemOutputArray,
                                  FluidStack[] fluidOutputArray, int duration, long EUTick) {

        boolean itemInputNull = itemInputArray == null;
        boolean itemOutputNull = itemOutputArray == null;
        boolean fluidInputNull = fluidInputArray == null;
        boolean fluidOutputNull = fluidOutputArray == null;

        // todo: Maybe doing some null checks?

        // incorrectly shaped recipe
        if ((itemInputNull && fluidInputNull) || (itemOutputNull && fluidOutputNull)) {
            return ;
        }

        /* single block recipe creation */

        GT_Values.RA.stdBuilder()
            .itemInputs(itemInputArray)
            .itemOutputs(itemOutputArray)
            .fluidInputs(fluidInputArray)
            .fluidOutputs(fluidOutputArray)
            .duration(duration)
            .eut(EUTick)
            .addTo(sMixerRecipes);

        // converting what can be converted to fluids for multiblock recipe

        // converting the inputs
        List<ItemStack> itemInputList = itemInputNull ? new ArrayList<>(1) : new ArrayList<>(Arrays.asList(itemInputArray));
        List<FluidStack> fluidInputList = fluidInputNull ? new ArrayList<>(1) : new ArrayList<>(Arrays.asList(fluidInputArray));

        for (int i = 0; i < itemInputList.size(); i++) {
            if (itemInputList.get(i) != null) {
                if (GT_Utility.getFluidForFilledItem(itemInputList.get(i), true) != null
                    || GT_Utility.isCellEmpty(itemInputList.get(i))) {
                    fluidInputList.add(GT_Utility.convertCellToFluid(itemInputList.get(i)));
                    itemInputList.set(i, null);
                }
            }
        }

        // converting the outputs
        List<ItemStack> itemOutputList = itemOutputNull ? new ArrayList<>(1) : new ArrayList<>(Arrays.asList(itemOutputArray));
        List<FluidStack> fluidOutputList = fluidOutputNull ? new ArrayList<>(1) : new ArrayList<>(Arrays.asList(fluidOutputArray));

        for (int i = 0; i < itemOutputList.size(); i++) {
            if (itemOutputList.get(i) != null) {
                if (GT_Utility.getFluidForFilledItem(itemOutputList.get(i), true) != null
                    || GT_Utility.isCellEmpty(itemOutputList.get(i))) {
                    fluidInputList.add(GT_Utility.convertCellToFluid(itemOutputList.get(i)));
                    itemOutputList.set(i, null);
                }
            }
        }

        /* Multiblock recipe creation */
        GT_Values.RA.stdBuilder()
            .itemInputs(itemInputList.toArray(new ItemStack[0]))
            .itemOutputs(itemOutputList.toArray(new ItemStack[0]))
            .fluidInputs(fluidInputList.toArray(new FluidStack[0]))
            .fluidOutputs(fluidOutputList.toArray(new FluidStack[0]))
            .duration(duration)
            .eut(EUTick)
            .addTo(sMultiblockMixerRecipes);
    }

    public void registerSingleBlockAndMulti(){
        addMixerRecipe(
            new ItemStack[] {
                Materials.NaquadahEnriched.getDust(8), Materials.Holmium.getDust(2),
                GT_Utility.getIntegratedCircuit(4)
            },
            null,
            new ItemStack[] {
                Materials.EnrichedHolmium.getDust(10)
            },
            null,
            30 * SECONDS,
            TierEU.RECIPE_ZPM);

        // Catalysts for Plasma Forge.
        addMixerRecipe(
            new ItemStack[] {
                GT_Utility.getIntegratedCircuit(9)
            },
            new FluidStack[] {
                Materials.Helium.getPlasma(1000L),
                Materials.Iron.getPlasma(1000L),
                Materials.Calcium.getPlasma(1000L),
                Materials.Niobium.getPlasma(1000L)
            },
            null,
            new FluidStack[] {
                Materials.DimensionallyTranscendentCrudeCatalyst.getFluid(1000L)
            },
            41*MINUTES+40*SECONDS,
            TierEU.RECIPE_ZPM);

        addMixerRecipe(
            new ItemStack[] {
                GT_Utility.getIntegratedCircuit(10)
            },
            new FluidStack[] {
                Materials.DimensionallyTranscendentCrudeCatalyst.getFluid(1000L),
                Materials.Radon.getPlasma(1000L),
                Materials.Nickel.getPlasma(1000L),
                Materials.Boron.getPlasma(1000L),
                Materials.Sulfur.getPlasma(1000L)
            },
            null,
            new FluidStack[] {
                Materials.DimensionallyTranscendentProsaicCatalyst.getFluid(1000L)
            },
            41*MINUTES+40*SECONDS,
            TierEU.RECIPE_UV);

        addMixerRecipe(
            new ItemStack[] {
                GT_Utility.getIntegratedCircuit(11)
            },
            new FluidStack[] {
                Materials.DimensionallyTranscendentProsaicCatalyst.getFluid(1000L),
                Materials.Nitrogen.getPlasma(1000L),
                Materials.Zinc.getPlasma(1000L),
                Materials.Silver.getPlasma(1000L),
                Materials.Titanium.getPlasma(1000L)
            },
            null,
            new FluidStack[] {
                Materials.DimensionallyTranscendentResplendentCatalyst.getFluid(1000L)
            },
            41*MINUTES+40*SECONDS,
            TierEU.RECIPE_UHV);

        addMixerRecipe(
            new ItemStack[] {
                GT_Utility.getIntegratedCircuit(12)
            },
            new FluidStack[] {
                Materials.DimensionallyTranscendentResplendentCatalyst.getFluid(1000L),
                Materials.Americium.getPlasma(1000L),
                Materials.Bismuth.getPlasma(1000L),
                Materials.Oxygen.getPlasma(1000L),
                Materials.Tin.getPlasma(1000L)
            },
            null,
            new FluidStack[] {
                Materials.DimensionallyTranscendentExoticCatalyst.getFluid(1000L)
            },
            41*MINUTES+40*SECONDS,
            TierEU.RECIPE_UEV);

        addMixerRecipe(
            new ItemStack[] {
                ItemList.IC2_Spray_WeedEx.get(1)
            },
            new FluidStack[] {
                MaterialsKevlar.NaphthenicAcid.getFluid(10)
            },
            null,
            new FluidStack[] {
                Materials.WeedEX9000.getFluid(750)
            },
            5*SECONDS,
            100);
    }
}
