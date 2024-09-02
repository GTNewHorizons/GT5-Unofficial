package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.AppliedEnergistics2;
import static gregtech.api.enums.Mods.ExtraUtilities;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.enums.Mods.ThaumicTinkerer;
import static gregtech.api.enums.Mods.TwilightForest;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.loaders.misc.GTBees.combs;
import static net.minecraftforge.fluids.FluidRegistry.getFluidStack;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsOreAlum;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class CentrifugeRecipes implements Runnable {

    @Override
    public void run() {

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.InfusedGold, 1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Gold, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Gold, 1),
                getModItem(Thaumcraft.ID, "ItemResource", 2L, 14))
            .outputChances(10000, 10000, 9000)
            .fluidInputs(Materials.Mercury.getFluid(200))
            .duration(20 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Empty.get(1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 1))
            .fluidInputs(Materials.Air.getGas(10000))
            .fluidOutputs(Materials.Nitrogen.getGas(3900))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(8)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.PlatinumGroupSludge, 9))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 9),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Gold, 9),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Platinum, 9),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Palladium, 3),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Iridium, 3),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Osmium, 3))
            .outputChances(10000, 10000, 10000, 9500, 9000, 8500)
            .duration(6 * MINUTES + 45 * SECONDS)
            .eut(30)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(
                Materials.Stone.getDust(1),
                Materials.Iron.getDust(1),
                Materials.Copper.getDust(1),
                Materials.Tin.getDust(1),
                Materials.Nickel.getDust(1),
                Materials.Antimony.getDust(1))
            .outputChances(10000, 4000, 2000, 2000, 2000, 2000)
            .fluidInputs(MaterialsOreAlum.SluiceJuice.getFluid(1000))
            .fluidOutputs(Materials.Water.getFluid(500))
            .duration(2 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .addTo(centrifugeRecipes);

        // food ->CH4

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.golden_apple, 1, 1), GTUtility.getIntegratedCircuit(1))
            .itemOutputs(new ItemStack(Items.gold_ingot, 64))
            .fluidOutputs(Materials.Methane.getGas(4608))
            .duration(7 * MINUTES + 40 * SECONDS + 16 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.golden_apple, 1, 0), GTUtility.getIntegratedCircuit(1))
            .itemOutputs(new ItemStack(Items.gold_ingot, 7))
            .fluidOutputs(Materials.Methane.getGas(576))
            .duration(7 * MINUTES + 40 * SECONDS + 16 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.golden_carrot, 1, 0), GTUtility.getIntegratedCircuit(1))
            .itemOutputs(new ItemStack(Items.gold_nugget, 6))
            .fluidOutputs(Materials.Methane.getGas(576))
            .duration(7 * MINUTES + 40 * SECONDS + 16 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.speckled_melon, 1, 0), GTUtility.getIntegratedCircuit(1))
            .itemOutputs(new ItemStack(Items.gold_nugget, 6))
            .fluidOutputs(Materials.Methane.getGas(576))
            .duration(7 * MINUTES + 40 * SECONDS + 16 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.mushroom_stew, 16, 0), GTUtility.getIntegratedCircuit(1))
            .itemOutputs(new ItemStack(Items.bowl, 16, 0))
            .fluidOutputs(Materials.Methane.getGas(576))
            .duration(3 * MINUTES + 50 * SECONDS + 8 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.apple, 32, 0), GTUtility.getIntegratedCircuit(1))
            .fluidOutputs(Materials.Methane.getGas(576))
            .duration(3 * MINUTES + 50 * SECONDS + 8 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.bread, 64, 0), GTUtility.getIntegratedCircuit(1))
            .fluidOutputs(Materials.Methane.getGas(576))
            .duration(3 * MINUTES + 50 * SECONDS + 8 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.porkchop, 12, 0), GTUtility.getIntegratedCircuit(1))
            .fluidOutputs(Materials.Methane.getGas(576))
            .duration(3 * MINUTES + 50 * SECONDS + 8 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.cooked_porkchop, 16, 0), GTUtility.getIntegratedCircuit(1))
            .fluidOutputs(Materials.Methane.getGas(576))
            .duration(3 * MINUTES + 50 * SECONDS + 8 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.beef, 12, 0), GTUtility.getIntegratedCircuit(1))
            .fluidOutputs(Materials.Methane.getGas(576))
            .duration(3 * MINUTES + 50 * SECONDS + 8 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.cooked_beef, 16, 0), GTUtility.getIntegratedCircuit(1))
            .fluidOutputs(Materials.Methane.getGas(576))
            .duration(3 * MINUTES + 50 * SECONDS + 8 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.fish, 12, 32767), GTUtility.getIntegratedCircuit(1))
            .fluidOutputs(Materials.Methane.getGas(576))
            .duration(3 * MINUTES + 50 * SECONDS + 8 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.cooked_fished, 16, 32767), GTUtility.getIntegratedCircuit(1))
            .fluidOutputs(Materials.Methane.getGas(576))
            .duration(3 * MINUTES + 50 * SECONDS + 8 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.chicken, 12, 0), GTUtility.getIntegratedCircuit(1))
            .fluidOutputs(Materials.Methane.getGas(576))
            .duration(3 * MINUTES + 50 * SECONDS + 8 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.cooked_chicken, 16, 0), GTUtility.getIntegratedCircuit(1))
            .fluidOutputs(Materials.Methane.getGas(576))
            .duration(3 * MINUTES + 50 * SECONDS + 8 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.melon, 64, 0), GTUtility.getIntegratedCircuit(1))
            .fluidOutputs(Materials.Methane.getGas(576))
            .duration(3 * MINUTES + 50 * SECONDS + 8 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.pumpkin, 16, 0), GTUtility.getIntegratedCircuit(1))
            .fluidOutputs(Materials.Methane.getGas(576))
            .duration(3 * MINUTES + 50 * SECONDS + 8 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.rotten_flesh, 16, 0), GTUtility.getIntegratedCircuit(1))
            .fluidOutputs(Materials.Methane.getGas(576))
            .duration(3 * MINUTES + 50 * SECONDS + 8 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.spider_eye, 32, 0), GTUtility.getIntegratedCircuit(1))
            .fluidOutputs(Materials.Methane.getGas(576))
            .duration(3 * MINUTES + 50 * SECONDS + 8 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.carrot, 16, 0), GTUtility.getIntegratedCircuit(1))
            .fluidOutputs(Materials.Methane.getGas(576))
            .duration(3 * MINUTES + 50 * SECONDS + 8 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Food_Raw_Potato.get(16), GTUtility.getIntegratedCircuit(1))
            .fluidOutputs(Materials.Methane.getGas(576))
            .duration(3 * MINUTES + 50 * SECONDS + 8 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Food_Poisonous_Potato.get(12), GTUtility.getIntegratedCircuit(1))
            .fluidOutputs(Materials.Methane.getGas(576))
            .duration(3 * MINUTES + 50 * SECONDS + 8 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Food_Baked_Potato.get(24), GTUtility.getIntegratedCircuit(1))
            .fluidOutputs(Materials.Methane.getGas(576))
            .duration(3 * MINUTES + 50 * SECONDS + 8 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.cookie, 64, 0), GTUtility.getIntegratedCircuit(1))
            .fluidOutputs(Materials.Methane.getGas(576))
            .duration(3 * MINUTES + 50 * SECONDS + 8 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.cake, 8, 0), GTUtility.getIntegratedCircuit(1))
            .fluidOutputs(Materials.Methane.getGas(576))
            .duration(3 * MINUTES + 50 * SECONDS + 8 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.brown_mushroom_block, 12, 32767), GTUtility.getIntegratedCircuit(1))
            .fluidOutputs(Materials.Methane.getGas(576))
            .duration(3 * MINUTES + 50 * SECONDS + 8 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.red_mushroom_block, 12, 32767), GTUtility.getIntegratedCircuit(1))
            .fluidOutputs(Materials.Methane.getGas(576))
            .duration(3 * MINUTES + 50 * SECONDS + 8 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.brown_mushroom, 32, 32767), GTUtility.getIntegratedCircuit(1))
            .fluidOutputs(Materials.Methane.getGas(576))
            .duration(3 * MINUTES + 50 * SECONDS + 8 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.red_mushroom, 32, 32767), GTUtility.getIntegratedCircuit(1))
            .fluidOutputs(Materials.Methane.getGas(576))
            .duration(3 * MINUTES + 50 * SECONDS + 8 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.nether_wart, 32, 32767), GTUtility.getIntegratedCircuit(1))
            .fluidOutputs(Materials.Methane.getGas(576))
            .duration(3 * MINUTES + 50 * SECONDS + 8 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getIC2Item("terraWart", 16), GTUtility.getIntegratedCircuit(1))
            .fluidOutputs(Materials.Methane.getGas(576))
            .duration(3 * MINUTES + 50 * SECONDS + 8 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(TwilightForest.ID, "item.meefRaw", 12L, 32767), GTUtility.getIntegratedCircuit(1))
            .fluidOutputs(Materials.Methane.getGas(576))
            .duration(3 * MINUTES + 50 * SECONDS + 8 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(TwilightForest.ID, "item.meefSteak", 16L, 32767), GTUtility.getIntegratedCircuit(1))
            .fluidOutputs(Materials.Methane.getGas(576))
            .duration(3 * MINUTES + 50 * SECONDS + 8 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(TwilightForest.ID, "item.venisonRaw", 12L, 32767), GTUtility.getIntegratedCircuit(1))
            .fluidOutputs(Materials.Methane.getGas(576))
            .duration(3 * MINUTES + 50 * SECONDS + 8 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                getModItem(TwilightForest.ID, "item.venisonCooked", 16L, 32767),
                GTUtility.getIntegratedCircuit(1))
            .fluidOutputs(Materials.Methane.getGas(576))
            .duration(3 * MINUTES + 50 * SECONDS + 8 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.log, Materials.Wood, 1), GTUtility.getIntegratedCircuit(1))
            .fluidOutputs(Materials.Methane.getGas(60))
            .duration(10 * SECONDS)
            .eut(20)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.sand, 1, 1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 1),
                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Diamond, 1),
                new ItemStack(Blocks.sand, 1))
            .outputChances(5000, 100, 5000)
            .duration(30 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.dirt, 1, 32767))
            .itemOutputs(
                ItemList.IC2_Plantball.get(1),
                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Clay, 1),
                new ItemStack(Blocks.sand, 1))
            .outputChances(1250, 5000, 5000)
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(30)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.grass, 1, 32767))
            .itemOutputs(
                ItemList.IC2_Plantball.get(1),
                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Clay, 1),
                new ItemStack(Blocks.sand, 1))
            .outputChances(2500, 5000, 5000)
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(30)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.mycelium, 1, 32767))
            .itemOutputs(
                new ItemStack(Blocks.brown_mushroom, 1),
                new ItemStack(Blocks.red_mushroom, 1),
                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Clay, 1),
                new ItemStack(Blocks.sand, 1))
            .outputChances(2500, 2500, 5000, 5000)
            .duration(32 * SECONDS + 10 * TICKS)
            .eut(30)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.IC2_Resin.get(1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.RawRubber, 3),
                ItemList.IC2_Plantball.get(1))
            .outputChances(10000, 1000)
            .fluidOutputs(Materials.Glue.getFluid(100))
            .duration(15 * SECONDS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Uranium, 1),
                GTUtility.getIntegratedCircuit(10))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Uranium235, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Plutonium, 1))
            .outputChances(1000, 500)
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut((int) TierEU.RECIPE_HV)
            .addTo(centrifugeRecipes);

        // Uranium Enrichment in Centrifuge by adding Fluorine (Uranium Hexafluoride)

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Uranium, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Uranium235, 1))
            .outputChances(10000)
            .fluidInputs(Materials.Fluorine.getGas(4000))
            .duration(3 * MINUTES + 20 * SECONDS)
            .eut((int) TierEU.RECIPE_EV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Plutonium, 1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Plutonium241, 1),
                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Uranium, 1))
            .outputChances(2000, 3000)
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(320)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 9))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.NaquadahEnriched, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Naquadria, 1))
            .outputChances(5000, 1000)
            .duration(24 * MINUTES)
            .eut(320)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.NaquadahEnriched, 4))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Naquadria, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 1))
            .outputChances(2000, 3000)
            .duration(21 * MINUTES + 20 * SECONDS)
            .eut(640)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Hydrogen.getGas(160))
            .fluidOutputs(Materials.Deuterium.getGas(40))
            .duration(8 * SECONDS)
            .eut(20)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Deuterium.getGas(160))
            .fluidOutputs(Materials.Tritium.getGas(40))
            .duration(8 * SECONDS)
            .eut(80)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Helium.getGas(80))
            .fluidOutputs(Materials.Helium_3.getGas(5))
            .duration(8 * SECONDS)
            .eut(80)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Glowstone, 2))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Gold, 1))
            .duration(48 * SECONDS + 16 * TICKS)
            .eut(80)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Endstone, 36))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Tungstate, 3),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Platinum, 1),
                new ItemStack(Blocks.sand, 36))
            .outputChances(3750, 2500, 9000)
            .fluidOutputs(Materials.Helium.getGas(4320))
            .duration(9 * MINUTES + 36 * SECONDS)
            .eut(20)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Netherrack, 36))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 4),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 9),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 4),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Gold, 1))
            .outputChances(5625, 9900, 5625, 2500)
            .duration(4 * MINUTES + 48 * SECONDS)
            .eut(20)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(10))
            .itemOutputs(
                Materials.SiliconDioxide.getDust(1),
                Materials.Magnesia.getDust(1),
                Materials.Quicklime.getDust(1),
                Materials.Gold.getNuggets(4),
                Materials.Sapphire.getDust(1),
                Materials.Tantalite.getDust(1))
            .outputChances(5000, 1000, 1000, 250, 1250, 500)
            .fluidInputs(Materials.Lava.getFluid(400))
            .duration(16 * SECONDS)
            .eut(80)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(20))
            .itemOutputs(
                Materials.SiliconDioxide.getDust(5),
                Materials.Magnesia.getDust(1),
                Materials.Quicklime.getDust(1),
                Materials.Gold.getIngots(1),
                Materials.Sapphire.getDust(3),
                Materials.Tantalite.getDust(1))
            .outputChances(9000, 9000, 9000, 1000, 3750, 4500)
            .fluidInputs(Materials.Lava.getFluid(3600))
            .duration(2 * MINUTES + 24 * SECONDS)
            .eut(80)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.soul_sand, 1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Saltpeter, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 1),
                new ItemStack(Blocks.sand, 1))
            .outputChances(1000, 700, 9000)
            .fluidOutputs(Materials.Oil.getFluid(200))
            .duration(10 * SECONDS)
            .eut(12)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(10))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Copper, 1),
                GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Tin, 1),
                GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Silver, 1),
                GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Phosphorus, 1),
                GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Scheelite, 1),
                GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Bauxite, 1))
            .outputChances(2000, 1000, 250, 50, 250, 500)
            .fluidInputs(getFluidStack("ic2pahoehoelava", 100))
            .duration(2 * SECONDS)
            .eut(1024)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(20))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Copper, 1),
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Tin, 1),
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Silver, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Phosphorus, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Scheelite, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Bauxite, 1))
            .outputChances(8000, 4000, 1000, 450, 2250, 4500)
            .fluidInputs(getFluidStack("ic2pahoehoelava", 3600))
            .duration(16 * SECONDS + 8 * TICKS)
            .eut(4096)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 45),
                GTUtility.getIntegratedCircuit(1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.BasalticMineralSand, 1),
                GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Olivine, 1),
                GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Obsidian, 1),
                GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Basalt, 1),
                GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Flint, 1),
                GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.RareEarth, 1))
            .outputChances(2000, 2000, 2000, 2000, 2000, 2000)
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(20)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 36L, 45),
                GTUtility.getIntegratedCircuit(2))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.BasalticMineralSand, 2),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Olivine, 2),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Obsidian, 2),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Basalt, 2),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Flint, 2),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.RareEarth, 2))
            .outputChances(9000, 9000, 9000, 9000, 9000, 9000)
            .duration(25 * SECONDS + 18 * TICKS)
            .eut(80)
            .addTo(centrifugeRecipes);

        // Ash centrifuge recipes

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Ash.getDust(36))
            .itemOutputs(
                Materials.Quicklime.getDust(18),
                Materials.Potash.getDust(9),
                Materials.Magnesia.getDust(1),
                Materials.PhosphorousPentoxide.getDust(2),
                Materials.SodaAsh.getDust(1),
                Materials.BandedIron.getDust(4))
            .outputChances(6400, 6000, 4500, 10000, 10000, 10000)
            .duration(5 * MINUTES)
            .eut(30)
            .addTo(centrifugeRecipes);

        // Stone Dust and Metal Mixture centrifuge recipes

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Stone.getDust(36))
            .itemOutputs(
                Materials.Quartzite.getDust(9),
                Materials.PotassiumFeldspar.getDust(9),
                Materials.Marble.getDust(8),
                Materials.Biotite.getDust(4),
                Materials.MetalMixture.getDust(3),
                Materials.Sodalite.getDust(2))
            .outputChances(10000, 10000, 10000, 10000, 10000, 10000)
            .duration(7 * MINUTES + 12 * SECONDS)
            .eut(30)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.MetalMixture.getDust(36))
            .itemOutputs(
                Materials.BandedIron.getDust(9),
                Materials.Bauxite.getDust(9),
                Materials.Pyrolusite.getDust(8),
                Materials.Barite.getDust(4),
                Materials.Chromite.getDust(3),
                Materials.Ilmenite.getDust(2))
            .outputChances(10000, 10000, 10000, 10000, 10000, 10000)
            .duration(10 * MINUTES + 56 * SECONDS + 5 * TICKS)
            .eut((int) TierEU.RECIPE_EV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Propane.getGas(320))
            .fluidOutputs(Materials.LPG.getFluid(290))
            .duration(20 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Butane.getGas(320))
            .fluidOutputs(Materials.LPG.getFluid(370))
            .duration(20 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 8),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.ElectrumFlux, 8))
            .outputChances(10000, 10000)
            .fluidInputs(Materials.EnrichedNaquadria.getFluid(9216))
            .fluidOutputs(Materials.FluidNaquadahFuel.getFluid(4806))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.DarkAsh, 1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 1))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.magma_cream, 1))
            .itemOutputs(new ItemStack(Items.blaze_powder, 1), new ItemStack(Items.slime_ball, 1))
            .duration(25 * SECONDS)
            .eut(5)
            .addTo(centrifugeRecipes);

        if (Thaumcraft.isModLoaded()) {
            // air

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(combs, 1, 144), GTUtility.getIntegratedCircuit(1))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.InfusedAir, 1))
                .duration(51 * SECONDS + 4 * TICKS)
                .eut(12)
                .addTo(centrifugeRecipes);

            // fire

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(combs, 1, 146), GTUtility.getIntegratedCircuit(1))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.InfusedFire, 1))
                .duration(51 * SECONDS + 4 * TICKS)
                .eut(12)
                .addTo(centrifugeRecipes);

            // aqua

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(combs, 1, 147), GTUtility.getIntegratedCircuit(1))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.InfusedWater, 1))
                .duration(51 * SECONDS + 4 * TICKS)
                .eut(12)
                .addTo(centrifugeRecipes);

            // terra

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(combs, 1, 145), GTUtility.getIntegratedCircuit(1))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.InfusedEarth, 1))
                .duration(51 * SECONDS + 4 * TICKS)
                .eut(12)
                .addTo(centrifugeRecipes);

            // ordo

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(combs, 1, 148), GTUtility.getIntegratedCircuit(1))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.InfusedOrder, 1))
                .duration(51 * SECONDS + 4 * TICKS)
                .eut(12)
                .addTo(centrifugeRecipes);

            // perditio

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(combs, 1, 149), GTUtility.getIntegratedCircuit(1))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.InfusedEntropy, 1))
                .duration(51 * SECONDS + 4 * TICKS)
                .eut(12)
                .addTo(centrifugeRecipes);

            // Nethershard

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(combs, 1, 152), GTUtility.getIntegratedCircuit(1))
                .itemOutputs(getModItem(ThaumicTinkerer.ID, "kamiResource", 1L, 6))
                .duration(6 * SECONDS + 8 * TICKS)
                .eut(TierEU.RECIPE_HV)
                .addTo(centrifugeRecipes);

            // Endshard

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(combs, 1, 153), GTUtility.getIntegratedCircuit(1))
                .itemOutputs(getModItem(ThaumicTinkerer.ID, "kamiResource", 1L, 7))
                .duration(6 * SECONDS + 8 * TICKS)
                .eut(TierEU.RECIPE_HV)
                .addTo(centrifugeRecipes);
        }

        if (ExtraUtilities.isModLoaded()) {
            // Caelestis red

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(combs, 1, 154), GTUtility.getIntegratedCircuit(1))
                .itemOutputs(
                    getModItem(ExtraUtilities.ID, "greenscreen", 1L, 2),
                    getModItem(ExtraUtilities.ID, "greenscreen", 1L, 10),
                    getModItem(ExtraUtilities.ID, "greenscreen", 1L, 14),
                    getModItem(ExtraUtilities.ID, "greenscreen", 1L, 1),
                    getModItem(ExtraUtilities.ID, "greenscreen", 1L, 12),
                    getModItem(ExtraUtilities.ID, "greenscreen", 1L, 6))
                .duration(25 * SECONDS + 12 * TICKS)
                .eut(12)
                .addTo(centrifugeRecipes);

            // Caelestis green

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(combs, 1, 155), GTUtility.getIntegratedCircuit(1))
                .itemOutputs(
                    getModItem(ExtraUtilities.ID, "greenscreen", 1L, 13),
                    getModItem(ExtraUtilities.ID, "greenscreen", 1L, 5),
                    getModItem(ExtraUtilities.ID, "greenscreen", 1L, 4),
                    getModItem(ExtraUtilities.ID, "greenscreen", 1L, 8),
                    getModItem(ExtraUtilities.ID, "greenscreen", 1L, 0))
                .duration(25 * SECONDS + 12 * TICKS)
                .eut(12)
                .addTo(centrifugeRecipes);

            // Caelestis blue

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(combs, 1, 156), GTUtility.getIntegratedCircuit(1))
                .itemOutputs(
                    getModItem(ExtraUtilities.ID, "greenscreen", 1L, 3),
                    getModItem(ExtraUtilities.ID, "greenscreen", 1L, 9),
                    getModItem(ExtraUtilities.ID, "greenscreen", 1L, 11),
                    getModItem(ExtraUtilities.ID, "greenscreen", 1L, 7),
                    getModItem(ExtraUtilities.ID, "greenscreen", 1L, 15))
                .duration(25 * SECONDS + 12 * TICKS)
                .eut(12)
                .addTo(centrifugeRecipes);
        }
    }
}
