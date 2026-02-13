package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.AppliedEnergistics2;
import static gregtech.api.enums.Mods.BiomesOPlenty;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.Natura;
import static gregtech.api.enums.Mods.PamsHarvestCraft;
import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.mixerNonCellRecipes;
import static net.minecraftforge.fluids.FluidRegistry.getFluidStack;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import goodgenerator.items.GGMaterial;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.material.MaterialsAlloy;
import mods.railcraft.common.blocks.aesthetics.cube.EnumCube;

@SuppressWarnings({ "PointlessArithmeticExpression" })
public class MixerRecipes implements Runnable {

    @Override
    public void run() {

        registerSingleBlockAndMulti();

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.EnderPearl, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Blaze, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.EnderEye, OrePrefixes.dust.getMaterialAmount()))
            .duration(5 * SECONDS)
            .eut(48)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Gold, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Silver, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.Electrum, 2L * OrePrefixes.dust.getMaterialAmount()))
            .duration(10 * SECONDS)
            .eut(8)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 2),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.Invar, 3L * OrePrefixes.dust.getMaterialAmount()))
            .duration(15 * SECONDS)
            .eut(8)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Tin, 1))
            .circuit(5)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.TinAlloy, 2L * OrePrefixes.dust.getMaterialAmount()))
            .duration(5 * SECONDS)
            .eut(8)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 4),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Invar, 3),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Manganese, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Chrome, 1))
            .circuit(1)
            .itemOutputs(
                GTOreDictUnificator.getDust(Materials.StainlessSteel, 9L * OrePrefixes.dust.getMaterialAmount()))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Chrome, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.Kanthal, 3L * OrePrefixes.dust.getMaterialAmount()))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 3),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Zinc, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.Brass, 4L * OrePrefixes.dust.getMaterialAmount()))
            .duration(2 * SECONDS)
            .eut(8)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 3),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Tin, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.Bronze, 4L * OrePrefixes.dust.getMaterialAmount()))
            .duration(2 * SECONDS)
            .eut(8)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 1))
            .circuit(3)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.Cupronickel, 2L * OrePrefixes.dust.getMaterialAmount()))
            .duration(10 * SECONDS)
            .eut(24)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Silver, 4))
            .circuit(4)
            .itemOutputs(
                GTOreDictUnificator.getDust(Materials.SterlingSilver, 5L * OrePrefixes.dust.getMaterialAmount()))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 3),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Electrum, 2))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.BlackBronze, 5L * OrePrefixes.dust.getMaterialAmount()))
            .duration(25 * SECONDS)
            .eut(8)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Bismuth, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Brass, 4))
            .circuit(1)
            .itemOutputs(
                GTOreDictUnificator.getDust(Materials.BismuthBronze, 5L * OrePrefixes.dust.getMaterialAmount()))
            .duration(25 * SECONDS)
            .eut(8)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.BlackBronze, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 3))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.BlackSteel, 5L * OrePrefixes.dust.getMaterialAmount()))
            .duration(25 * SECONDS)
            .eut(8)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.SterlingSilver, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.BismuthBronze, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.BlackSteel, 4),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 2))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.RedSteel, 8L * OrePrefixes.dust.getMaterialAmount()))
            .duration(40 * SECONDS)
            .eut(8)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.RoseGold, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Brass, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.BlackSteel, 4),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 2))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.BlueSteel, 8L * OrePrefixes.dust.getMaterialAmount()))
            .duration(40 * SECONDS)
            .eut(8)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 3),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Gold, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Silver, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 5),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 15))
            .circuit(14)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.BlackSteel, 25L * OrePrefixes.dust.getMaterialAmount()))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 4),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Silver, 4),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Bismuth, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Zinc, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.BlackSteel, 20),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 10))
            .circuit(15)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.RedSteel, 40L * OrePrefixes.dust.getMaterialAmount()))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 19),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Gold, 16),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Zinc, 5),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.BlackSteel, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.BlackSteel, 16),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 40))
            .circuit(16)
            .itemOutputs(
                GTOreDictUnificator.getDust(Materials.BlueSteel, 64L * OrePrefixes.dust.getMaterialAmount()),
                GTOreDictUnificator.getDust(Materials.BlueSteel, 64L * OrePrefixes.dust.getMaterialAmount()),
                GTOreDictUnificator.getDust(Materials.BlueSteel, 32L * OrePrefixes.dust.getMaterialAmount()))
            .duration(3 * MINUTES)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Cobalt, 5),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Chrome, 2),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Molybdenum, 1))
            .circuit(2)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.Ultimet, 9L * OrePrefixes.dust.getMaterialAmount()))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Brass, 7),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Tin, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Cobalt, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.CobaltBrass, 9L * OrePrefixes.dust.getMaterialAmount()))
            .duration(45 * SECONDS)
            .eut(8)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Saltpeter, 2),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 3))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.Gunpowder, 6L * OrePrefixes.dust.getMaterialAmount()))
            .duration(30 * SECONDS)
            .eut(8)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Saltpeter, 2),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Charcoal, 3))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.Gunpowder, 6L * OrePrefixes.dust.getMaterialAmount()))
            .duration(30 * SECONDS)
            .eut(8)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Saltpeter, 2),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 3))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.Gunpowder, 6L * OrePrefixes.dust.getMaterialAmount()))
            .duration(30 * SECONDS)
            .eut(8)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Indium, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Gallium, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Phosphorus, 1))
            .circuit(1)
            .itemOutputs(
                GTOreDictUnificator
                    .getDust(Materials.IndiumGalliumPhosphide, 3L * OrePrefixes.dust.getMaterialAmount()))
            .duration(10 * SECONDS)
            .eut(8)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Brick, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Clay, 1))
            .itemOutputs(GTOreDictUnificator.getDust(Materials.Fireclay, 2L * OrePrefixes.dust.getMaterialAmount()))
            .duration(10 * SECONDS)
            .eut(8)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 4),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Chrome, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.Nichrome, 5L * OrePrefixes.dust.getMaterialAmount()))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Osmium, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Iridium, 3))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.Osmiridium, 4L * OrePrefixes.dust.getMaterialAmount()))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Niobium, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Titanium, 1))
            .circuit(1)
            .itemOutputs(
                GTOreDictUnificator.getDust(Materials.NiobiumTitanium, 2L * OrePrefixes.dust.getMaterialAmount()))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Vanadium, 3),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Gallium, 1))
            .circuit(1)
            .itemOutputs(
                GTOreDictUnificator.getDust(Materials.VanadiumGallium, 4L * OrePrefixes.dust.getMaterialAmount()))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Tungsten, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 1))
            .circuit(1)
            .itemOutputs(
                GTOreDictUnificator.getDust(Materials.TungstenCarbide, 2L * OrePrefixes.dust.getMaterialAmount()))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Tungsten, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 1))
            .circuit(1)
            .itemOutputs(
                GTOreDictUnificator.getDust(Materials.TungstenSteel, 2L * OrePrefixes.dust.getMaterialAmount()))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Titanium, 3),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Platinum, 3),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Vanadium, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.TPV, 7L * OrePrefixes.dust.getMaterialAmount()))
            .duration(8 * SECONDS + 15 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.TungstenSteel, 5),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Chrome, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Molybdenum, 2),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Vanadium, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.HSSG, 9L * OrePrefixes.dust.getMaterialAmount()))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.HSSG, 6),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Cobalt, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Manganese, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.HSSE, 9L * OrePrefixes.dust.getMaterialAmount()))
            .duration(35 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.HSSG, 6),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Iridium, 2),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Osmium, 1))
            .circuit(2)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.HSSS, 9L * OrePrefixes.dust.getMaterialAmount()))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Zinc, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 4))
            .circuit(3)
            .itemOutputs(
                GTOreDictUnificator.getDust(Materials.FerriteMixture, 6L * OrePrefixes.dust.getMaterialAmount()))
            .duration(10 * SECONDS)
            .eut(8)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Boron, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Glass, 7))
            .itemOutputs(
                GTOreDictUnificator.getDust(Materials.BorosilicateGlass, 8L * OrePrefixes.dust.getMaterialAmount()))
            .duration(10 * SECONDS)
            .eut(8)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 3),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 23),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 5),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Chrome, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 12),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 3),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 1))
            .circuit(20)
            .itemOutputs(MaterialsAlloy.EGLIN_STEEL.getDust(48))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Platinum, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Silver, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Tin, 2),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Thaumium, 2),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.EnderPearl, 2))
            .circuit(16)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Enderium, 8L))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Items.rotten_flesh, 1, 0),
                new ItemStack(Items.fermented_spider_eye, 1, 0),
                ItemList.IC2_Scrap.get(1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.MeatRaw, 1))
            .itemOutputs(ItemList.Food_Chum.get(4))
            .fluidInputs(getFluidStack("potion.purpledrink", 750))
            .fluidOutputs(getFluidStack("sludge", 1_000))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(24)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Wheat, 1))
            .itemOutputs(ItemList.Food_Dough.get(2))
            .fluidInputs(Materials.Water.getFluid(1_000))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(8)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Chili, 1), ItemList.Food_PotatoChips.get(1))
            .itemOutputs(ItemList.Food_ChiliChips.get(1))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(8)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Redstone, 5),
                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Ruby, 4))
            .itemOutputs(ItemList.IC2_Energium_Dust.get(1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 5),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Ruby, 4))
            .itemOutputs(ItemList.IC2_Energium_Dust.get(9))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1),
                new ItemStack(Blocks.brown_mushroom, 1),
                new ItemStack(Items.spider_eye, 1))
            .itemOutputs(new ItemStack(Items.fermented_spider_eye, 1))
            .duration(5 * SECONDS)
            .eut(8)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Gold, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.LiveRoot, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.IronWood, 2))
            .duration(5 * SECONDS)
            .eut(8)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Gold, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 9),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.LiveRoot, 9))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.IronWood, 18))
            .duration(45 * SECONDS)
            .eut(8)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1),
                GTOreDictUnificator.get(OrePrefixes.gem, Materials.NetherQuartz, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Fluix, 2))
            .fluidInputs(Materials.Water.getFluid(500))
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1),
                GTOreDictUnificator.get(OrePrefixes.gem, Materials.NetherQuartz, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Fluix, 2))
            .fluidInputs(GTModHandler.getDistilledWater(500))
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.LightFuel, 5),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.HeavyFuel, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Diesel, 6))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.LightFuel, 5), Materials.Empty.getCells(1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Diesel, 6))
            .fluidInputs(Materials.HeavyFuel.getFluid(1_000))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.HeavyFuel, 1), Materials.Empty.getCells(5))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Diesel, 6))
            .fluidInputs(Materials.LightFuel.getFluid(5_000))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.LightFuel, 5))
            .circuit(5)
            .itemOutputs(Materials.Empty.getCells(5))
            .fluidInputs(Materials.HeavyFuel.getFluid(1_000))
            .fluidOutputs(Materials.Diesel.getFluid(6_000))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.HeavyFuel, 1))
            .circuit(6)
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(Materials.LightFuel.getFluid(5_000))
            .fluidOutputs(Materials.Diesel.getFluid(6_000))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 5),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1))
            .itemOutputs(Materials.Empty.getCells(5))
            .fluidInputs(Materials.Lubricant.getFluid(20))
            .fluidOutputs(new FluidStack(ItemList.sDrillingFluid, 5_000))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Lapis, 1))
            .circuit(4)
            .fluidInputs(Materials.Water.getFluid(125))
            .fluidOutputs(GTModHandler.getIC2Coolant(125))
            .duration(12 * SECONDS + 16 * TICKS)
            .eut(48)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Lapis, 1))
            .circuit(4)
            .fluidInputs(GTModHandler.getDistilledWater(1_000))
            .fluidOutputs(GTModHandler.getIC2Coolant(1_000))
            .duration(12 * SECONDS + 16 * TICKS)
            .eut(48)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 4))
            .circuit(1)
            .itemOutputs(ItemList.SFMixture.get(4))
            .fluidInputs(Materials.GlueAdvanced.getFluid(200))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Lithium, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 4))
            .circuit(1)
            .itemOutputs(ItemList.SFMixture.get(8))
            .fluidInputs(Materials.GlueAdvanced.getFluid(200))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Caesium, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 4))
            .circuit(1)
            .itemOutputs(ItemList.SFMixture.get(12))
            .fluidInputs(Materials.GlueAdvanced.getFluid(200))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        // McGuffium239 is non-renewable and only obtainable though world gen.
        // It's a meme, don't think too deep about it.
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 4))
            .circuit(1)
            .itemOutputs(ItemList.SFMixture.get(64))
            .fluidInputs(Materials.McGuffium239.getFluid(12))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Lithium, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 4))
            .circuit(1)
            .itemOutputs(ItemList.SFMixture.get(64))
            .fluidInputs(Materials.McGuffium239.getFluid(8))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Caesium, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 4))
            .circuit(1)
            .itemOutputs(ItemList.SFMixture.get(64))
            .fluidInputs(Materials.McGuffium239.getFluid(4))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        // Molten Red Granite
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 1L))
            .fluidInputs(Materials.Lava.getFluid(125L))
            .fluidOutputs(Materials.GraniteRed.getMolten(1 * INGOTS))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);
        // Molten Black Granite
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 1L))
            .fluidInputs(Materials.Lava.getFluid(125L))
            .fluidOutputs(Materials.GraniteBlack.getMolten(1 * INGOTS))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.SFMixture.get(2), GTOreDictUnificator.get(OrePrefixes.dust, Materials.EnderEye, 1))
            .itemOutputs(ItemList.MSFMixture.get(4))
            .fluidInputs(Materials.Mercury.getFluid(1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.SFMixture.get(1), GTOreDictUnificator.get(OrePrefixes.dust, Materials.Blaze, 1))
            .itemOutputs(ItemList.MSFMixture.get(1))
            .fluidInputs(Materials.Mercury.getFluid(500))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Lignite, 4),
                ItemList.MSFMixture.get(24),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Diamond, 1))
            .circuit(1)
            .itemOutputs(ItemList.Block_MSSFUEL.get(4))
            .fluidInputs(Materials.NitroFuel.getFluid(4_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Charcoal, 4),
                ItemList.MSFMixture.get(16),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Diamond, 1))
            .circuit(1)
            .itemOutputs(ItemList.Block_MSSFUEL.get(4))
            .fluidInputs(Materials.NitroFuel.getFluid(3_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Coal, 4),
                ItemList.MSFMixture.get(8),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Diamond, 1))
            .circuit(1)
            .itemOutputs(ItemList.Block_MSSFUEL.get(4))
            .fluidInputs(Materials.NitroFuel.getFluid(2_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Lignite, 4),
                ItemList.MSFMixture.get(24),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Diamond, 1))
            .circuit(1)
            .itemOutputs(ItemList.Block_MSSFUEL.get(4))
            .fluidInputs(Materials.GasolinePremium.getFluid(1_600))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Charcoal, 4),
                ItemList.MSFMixture.get(16),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Diamond, 1))
            .circuit(1)
            .itemOutputs(ItemList.Block_MSSFUEL.get(4))
            .fluidInputs(Materials.GasolinePremium.getFluid(1_200))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Coal, 4),
                ItemList.MSFMixture.get(8),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Diamond, 1))
            .circuit(1)
            .itemOutputs(ItemList.Block_MSSFUEL.get(4))
            .fluidInputs(Materials.GasolinePremium.getFluid(800))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        if (Thaumcraft.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.SFMixture.get(20),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.InfusedAir, 1))
                .circuit(1)
                .itemOutputs(ItemList.MSFMixture.get(20))
                .fluidInputs(Materials.FierySteel.getFluid(50))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MV / 2)
                .addTo(mixerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.SFMixture.get(20),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.InfusedEarth, 1))
                .circuit(1)
                .itemOutputs(ItemList.MSFMixture.get(20))
                .fluidInputs(Materials.FierySteel.getFluid(50))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MV / 2)
                .addTo(mixerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.SFMixture.get(20),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.InfusedEntropy, 1))
                .circuit(1)
                .itemOutputs(ItemList.MSFMixture.get(20))
                .fluidInputs(Materials.FierySteel.getFluid(50))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MV / 2)
                .addTo(mixerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.SFMixture.get(20),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.InfusedFire, 1))
                .circuit(1)
                .itemOutputs(ItemList.MSFMixture.get(20))
                .fluidInputs(Materials.FierySteel.getFluid(50))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MV / 2)
                .addTo(mixerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.SFMixture.get(20),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.InfusedOrder, 1))
                .circuit(1)
                .itemOutputs(ItemList.MSFMixture.get(20))
                .fluidInputs(Materials.FierySteel.getFluid(50))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MV / 2)
                .addTo(mixerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.SFMixture.get(20),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.InfusedWater, 1))
                .circuit(1)
                .itemOutputs(ItemList.MSFMixture.get(20))
                .fluidInputs(Materials.FierySteel.getFluid(50))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MV / 2)
                .addTo(mixerRecipes);

            FluidStack tFD = getFluidStack("fluiddeath", 30);
            if (tFD != null && tFD.getFluid() != null && tFD.amount > 0) {
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        ItemList.SFMixture.get(30),
                        GTOreDictUnificator.get(OrePrefixes.dust, Materials.InfusedAir, 1))
                    .circuit(1)
                    .itemOutputs(ItemList.MSFMixture.get(30))
                    .fluidInputs(tFD)
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_MV / 2)
                    .addTo(mixerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(
                        ItemList.SFMixture.get(30),
                        GTOreDictUnificator.get(OrePrefixes.dust, Materials.InfusedEarth, 1))
                    .circuit(1)
                    .itemOutputs(ItemList.MSFMixture.get(30))
                    .fluidInputs(tFD)
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_MV / 2)
                    .addTo(mixerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(
                        ItemList.SFMixture.get(30),
                        GTOreDictUnificator.get(OrePrefixes.dust, Materials.InfusedEntropy, 1))
                    .circuit(1)
                    .itemOutputs(ItemList.MSFMixture.get(30))
                    .fluidInputs(tFD)
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_MV / 2)
                    .addTo(mixerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(
                        ItemList.SFMixture.get(30),
                        GTOreDictUnificator.get(OrePrefixes.dust, Materials.InfusedFire, 1))
                    .circuit(1)
                    .itemOutputs(ItemList.MSFMixture.get(30))
                    .fluidInputs(tFD)
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_MV / 2)
                    .addTo(mixerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(
                        ItemList.SFMixture.get(30),
                        GTOreDictUnificator.get(OrePrefixes.dust, Materials.InfusedOrder, 1))
                    .circuit(1)
                    .itemOutputs(ItemList.MSFMixture.get(30))
                    .fluidInputs(tFD)
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_MV / 2)
                    .addTo(mixerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(
                        ItemList.SFMixture.get(30),
                        GTOreDictUnificator.get(OrePrefixes.dust, Materials.InfusedWater, 1))
                    .circuit(1)
                    .itemOutputs(ItemList.MSFMixture.get(30))
                    .fluidInputs(tFD)
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_MV / 2)
                    .addTo(mixerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(
                        GTOreDictUnificator.get(OrePrefixes.block, Materials.Lignite, 1),
                        ItemList.MSFMixture.get(6),
                        getModItem(Thaumcraft.ID, "ItemResource", 4))
                    .circuit(1)
                    .itemOutputs(ItemList.Block_MSSFUEL.get(1))
                    .fluidInputs(Materials.NitroFuel.getFluid(1_000))
                    .duration(7 * SECONDS)
                    .eut(TierEU.RECIPE_HV)
                    .addTo(mixerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(
                        GTOreDictUnificator.get(OrePrefixes.block, Materials.Charcoal, 1),
                        ItemList.MSFMixture.get(4),
                        getModItem(Thaumcraft.ID, "ItemResource", 4))
                    .circuit(1)
                    .itemOutputs(ItemList.Block_MSSFUEL.get(1))
                    .fluidInputs(Materials.NitroFuel.getFluid(750))
                    .duration(6 * SECONDS)
                    .eut(TierEU.RECIPE_HV)
                    .addTo(mixerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(
                        GTOreDictUnificator.get(OrePrefixes.block, Materials.Coal, 1),
                        ItemList.MSFMixture.get(2),
                        getModItem(Thaumcraft.ID, "ItemResource", 4))
                    .circuit(1)
                    .itemOutputs(ItemList.Block_MSSFUEL.get(1))
                    .fluidInputs(Materials.NitroFuel.getFluid(500))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_HV)
                    .addTo(mixerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(
                        GTOreDictUnificator.get(OrePrefixes.block, Materials.Lignite, 1),
                        ItemList.MSFMixture.get(6),
                        getModItem(Thaumcraft.ID, "ItemResource", 4))
                    .circuit(1)
                    .itemOutputs(ItemList.Block_MSSFUEL.get(1))
                    .fluidInputs(Materials.GasolinePremium.getFluid(400))
                    .duration(7 * SECONDS)
                    .eut(TierEU.RECIPE_HV)
                    .addTo(mixerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(
                        GTOreDictUnificator.get(OrePrefixes.block, Materials.Charcoal, 1),
                        ItemList.MSFMixture.get(4),
                        getModItem(Thaumcraft.ID, "ItemResource", 4))
                    .circuit(1)
                    .itemOutputs(ItemList.Block_MSSFUEL.get(1))
                    .fluidInputs(Materials.GasolinePremium.getFluid(300))
                    .duration(6 * SECONDS)
                    .eut(TierEU.RECIPE_HV)
                    .addTo(mixerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(
                        GTOreDictUnificator.get(OrePrefixes.block, Materials.Coal, 1),
                        ItemList.MSFMixture.get(2),
                        getModItem(Thaumcraft.ID, "ItemResource", 4))
                    .circuit(1)
                    .itemOutputs(ItemList.Block_MSSFUEL.get(1))
                    .fluidInputs(Materials.GasolinePremium.getFluid(200))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_HV)
                    .addTo(mixerRecipes);
            }
        }

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.block, Materials.Lignite, 1), ItemList.SFMixture.get(6))
            .circuit(1)
            .itemOutputs(ItemList.Block_SSFUEL.get(1))
            .fluidInputs(Materials.NitroFuel.getFluid(1_000))
            .duration(7 * SECONDS)
            .eut(250)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.block, Materials.Charcoal, 1), ItemList.SFMixture.get(4))
            .circuit(1)
            .itemOutputs(ItemList.Block_SSFUEL.get(1))
            .fluidInputs(Materials.NitroFuel.getFluid(750))
            .duration(6 * SECONDS)
            .eut(250)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.block, Materials.Coal, 1), ItemList.SFMixture.get(2))
            .circuit(1)
            .itemOutputs(ItemList.Block_SSFUEL.get(1))
            .fluidInputs(Materials.NitroFuel.getFluid(500))
            .duration(5 * SECONDS)
            .eut(250)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.block, Materials.Lignite, 1), ItemList.SFMixture.get(6))
            .circuit(1)
            .itemOutputs(ItemList.Block_SSFUEL.get(1))
            .fluidInputs(Materials.GasolinePremium.getFluid(400))
            .duration(7 * SECONDS)
            .eut(250)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.block, Materials.Charcoal, 1), ItemList.SFMixture.get(4))
            .circuit(1)
            .itemOutputs(ItemList.Block_SSFUEL.get(1))
            .fluidInputs(Materials.GasolinePremium.getFluid(300))
            .duration(6 * SECONDS)
            .eut(250)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.block, Materials.Coal, 1), ItemList.SFMixture.get(2))
            .circuit(1)
            .itemOutputs(ItemList.Block_SSFUEL.get(1))
            .fluidInputs(Materials.GasolinePremium.getFluid(200))
            .duration(5 * SECONDS)
            .eut(250)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.SulfuricAcid, 1))
            .circuit(1)
            .itemOutputs(ItemList.Cell_Empty.get(1))
            .fluidInputs(Materials.NitricAcid.getFluid(1_000))
            .fluidOutputs(new FluidStack(ItemList.sNitrationMixture, 2_000))
            .duration(24 * SECONDS)
            .eut(2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.SulfuricAcid, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.NitricAcid, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.NitrationMixture, 2))
            .duration(24 * SECONDS)
            .eut(2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.dirt, 1, 32767), new ItemStack(Items.wheat, 4, 32767))
            .circuit(2)
            .itemOutputs(getModItem(Forestry.ID, "fertilizerBio", 1L, 0))
            .fluidInputs(Materials.Water.getFluid(100))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.dirt, 1, 32767), getModItem(BiomesOPlenty.ID, "plants", 4, 6))
            .circuit(2)
            .itemOutputs(getModItem(Forestry.ID, "fertilizerBio", 1L, 0))
            .fluidInputs(Materials.Water.getFluid(100))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.dirt, 1, 32767), getModItem(PamsHarvestCraft.ID, "oatsItem", 4))
            .circuit(2)
            .itemOutputs(getModItem(Forestry.ID, "fertilizerBio", 1L, 0))
            .fluidInputs(Materials.Water.getFluid(100))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.dirt, 1, 32767), getModItem(PamsHarvestCraft.ID, "ryeItem", 4))
            .circuit(2)
            .itemOutputs(getModItem(Forestry.ID, "fertilizerBio", 1L, 0))
            .fluidInputs(Materials.Water.getFluid(100))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.dirt, 1, 32767), getModItem(PamsHarvestCraft.ID, "barleyItem", 4))
            .circuit(2)
            .itemOutputs(getModItem(Forestry.ID, "fertilizerBio", 1L, 0))
            .fluidInputs(Materials.Water.getFluid(100))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.dirt, 1, 32767), getModItem(Natura.ID, "barleyFood", 4))
            .circuit(2)
            .itemOutputs(getModItem(Forestry.ID, "fertilizerBio", 1L, 0))
            .fluidInputs(Materials.Water.getFluid(100))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.dirt, 1, 32767),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 4))
            .circuit(3)
            .itemOutputs(getModItem(Forestry.ID, "fertilizerBio", 1L, 0))
            .fluidInputs(Materials.Water.getFluid(100))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        // radiation manufacturing

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.SluiceSand.getDust(1))
            .fluidInputs(Materials.Water.getFluid(500))
            .fluidOutputs(Materials.SluiceJuice.getFluid(1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        // NaCl + H2O = (NaCl·H2O)

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Salt.getDust(2))
            .circuit(3)
            .fluidInputs(Materials.Water.getFluid(1_000))
            .fluidOutputs(Materials.SaltWater.getFluid(1_000))
            .duration(5 * SECONDS)
            .eut(8)
            .addTo(mixerRecipes);

        // CaCO3 + 2 CH3COOH = Ca(CH3COO)2 + H2O + CO2

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Calcite.getDust(5), Materials.Empty.getCells(2))
            .circuit(1)
            .itemOutputs(Materials.Water.getCells(1), Materials.CarbonDioxide.getCells(1))
            .fluidInputs(Materials.AceticAcid.getFluid(2_000))
            .fluidOutputs(Materials.CalciumAcetateSolution.getFluid(1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Calcite.getDust(5), Materials.Empty.getCells(2))
            .circuit(11)
            .itemOutputs(Materials.CalciumAcetateSolution.getCells(1), Materials.CarbonDioxide.getCells(1))
            .fluidInputs(Materials.AceticAcid.getFluid(2_000))
            .fluidOutputs(Materials.Water.getFluid(1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Calcite.getDust(5), Materials.Empty.getCells(2))
            .circuit(21)
            .itemOutputs(Materials.Water.getCells(1), Materials.CalciumAcetateSolution.getCells(1))
            .fluidInputs(Materials.AceticAcid.getFluid(2_000))
            .fluidOutputs(Materials.CarbonDioxide.getGas(1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Calcite.getDust(5), Materials.Empty.getCells(1))
            .circuit(4)
            .itemOutputs(Materials.CarbonDioxide.getCells(1))
            .fluidInputs(Materials.AceticAcid.getFluid(2_000))
            .fluidOutputs(Materials.CalciumAcetateSolution.getFluid(1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Calcite.getDust(5), Materials.Empty.getCells(1))
            .circuit(14)
            .itemOutputs(Materials.CalciumAcetateSolution.getCells(1))
            .fluidInputs(Materials.AceticAcid.getFluid(2_000))
            .fluidOutputs(Materials.CarbonDioxide.getGas(1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        // Ca + 2 CH3COOH = Ca(CH3COO)2 + 2H

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Calcium.getDust(1), Materials.Empty.getCells(2))
            .circuit(1)
            .itemOutputs(Materials.Hydrogen.getCells(2))
            .fluidInputs(Materials.AceticAcid.getFluid(2_000))
            .fluidOutputs(Materials.CalciumAcetateSolution.getFluid(1_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Calcium.getDust(1), Materials.Empty.getCells(1))
            .circuit(11)
            .itemOutputs(Materials.CalciumAcetateSolution.getCells(1))
            .fluidInputs(Materials.AceticAcid.getFluid(2_000))
            .fluidOutputs(Materials.Hydrogen.getGas(2_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        // CaO + 2 CH3COOH = Ca(CH3COO)2 + H2O

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Quicklime.getDust(2), Materials.Empty.getCells(1))
            .circuit(1)
            .itemOutputs(Materials.Water.getCells(1))
            .fluidInputs(Materials.AceticAcid.getFluid(2_000))
            .fluidOutputs(Materials.CalciumAcetateSolution.getFluid(1_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Quicklime.getDust(2), Materials.Empty.getCells(1))
            .circuit(11)
            .itemOutputs(Materials.CalciumAcetateSolution.getCells(1))
            .fluidInputs(Materials.AceticAcid.getFluid(2_000))
            .fluidOutputs(Materials.Water.getFluid(1_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        // 2CH3COOCH3 + 3CH3COCH3/(C4H6O2)n = 5Glue

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Acetone.getCells(3))
            .itemOutputs(Materials.Empty.getCells(3))
            .fluidInputs(Materials.PolyvinylAcetate.getFluid(2_000))
            .fluidOutputs(Materials.GlueAdvanced.getFluid(5_000))
            .duration(5 * SECONDS)
            .eut(8)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.PolyvinylAcetate.getCells(2))
            .itemOutputs(Materials.Empty.getCells(2))
            .fluidInputs(Materials.Acetone.getFluid(3_000))
            .fluidOutputs(Materials.GlueAdvanced.getFluid(5_000))
            .duration(5 * SECONDS)
            .eut(8)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.MethylAcetate.getCells(3))
            .itemOutputs(Materials.Empty.getCells(3))
            .fluidInputs(Materials.PolyvinylAcetate.getFluid(2_000))
            .fluidOutputs(Materials.GlueAdvanced.getFluid(5_000))
            .duration(5 * SECONDS)
            .eut(8)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.PolyvinylAcetate.getCells(2))
            .itemOutputs(Materials.Empty.getCells(2))
            .fluidInputs(Materials.MethylAcetate.getFluid(3_000))
            .fluidOutputs(Materials.GlueAdvanced.getFluid(5_000))
            .duration(5 * SECONDS)
            .eut(8)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Sugar.getDust(4))
            .itemOutputs(Materials.Charcoal.getGems(1))
            .fluidInputs(Materials.SulfuricAcid.getFluid(1_000))
            .fluidOutputs(Materials.DilutedSulfuricAcid.getFluid(1_000))
            .duration(60 * SECONDS)
            .eut(2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Wood.getDust(4))
            .itemOutputs(Materials.Charcoal.getGems(1))
            .fluidInputs(Materials.SulfuricAcid.getFluid(1_000))
            .fluidOutputs(Materials.DilutedSulfuricAcid.getFluid(1_000))
            .duration(60 * SECONDS)
            .eut(2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Diesel.getCells(1))
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(Materials.Tetranitromethane.getFluid(20))
            .fluidOutputs(Materials.NitroFuel.getFluid(1_000))
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.BioDiesel.getCells(1))
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(Materials.Tetranitromethane.getFluid(40))
            .fluidOutputs(Materials.NitroFuel.getFluid(900))
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        // CH4O + C4H8 = C5H12O

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Methanol.getCells(1), Materials.Butene.getCells(1))
            .itemOutputs(Materials.MTBEMixture.getCells(1), Materials.Empty.getCells(1))
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Methanol.getCells(1), Materials.Butane.getCells(1))
            .itemOutputs(Materials.MTBEMixtureAlt.getCells(1), Materials.Empty.getCells(1))
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.Naphtha.getCells(16),
                Materials.Gas.getCells(2),
                Materials.Methanol.getCells(1),
                Materials.Acetone.getCells(1))
            .itemOutputs(Materials.GasolineRaw.getCells(20))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.GasolineRegular.getCells(20),
                Materials.Octane.getCells(2),
                Materials.NitrousOxide.getCells(6),
                Materials.Toluene.getCells(1))
            .itemOutputs(Materials.Empty.getCells(29))
            .fluidInputs(Materials.AntiKnock.getFluid(3_000))
            .fluidOutputs(Materials.GasolinePremium.getFluid(32_000))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerRecipes);

        if (Railcraft.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(EnumCube.COKE_BLOCK.getItem(), ItemList.SFMixture.get(2))
                .circuit(1)
                .itemOutputs(ItemList.Block_SSFUEL.get(1))
                .fluidInputs(Materials.NitroFuel.getFluid(300))
                .duration(5 * SECONDS)
                .eut(250)
                .addTo(mixerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    EnumCube.COKE_BLOCK.getItem(4),
                    ItemList.SFMixture.get(8),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Diamond, 1))
                .circuit(2)
                .itemOutputs(ItemList.Block_SSFUEL.get(4))
                .fluidInputs(Materials.GasolinePremium.getFluid(480))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(mixerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(EnumCube.COKE_BLOCK.getItem(), ItemList.SFMixture.get(2))
                .circuit(1)
                .itemOutputs(ItemList.Block_SSFUEL.get(1))
                .fluidInputs(Materials.GasolinePremium.getFluid(120))
                .duration(5 * SECONDS)
                .eut(250)
                .addTo(mixerRecipes);
        }

        if (Thaumcraft.isModLoaded() && Railcraft.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    EnumCube.COKE_BLOCK.getItem(),
                    ItemList.MSFMixture.get(2),
                    getModItem(Thaumcraft.ID, "ItemResource", 4))
                .circuit(1)
                .itemOutputs(ItemList.Block_MSSFUEL.get(1))
                .fluidInputs(Materials.NitroFuel.getFluid(300))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(mixerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    EnumCube.COKE_BLOCK.getItem(),
                    ItemList.MSFMixture.get(2),
                    getModItem(Thaumcraft.ID, "ItemResource", 4))
                .circuit(1)
                .itemOutputs(ItemList.Block_MSSFUEL.get(1))
                .fluidInputs(Materials.GasolinePremium.getFluid(120))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(mixerRecipes);
        }
    }

    public static void addMixerPotionRecipes(String aName) {

        boolean splash = !(FluidRegistry.getFluid("potion." + aName) == null
            || FluidRegistry.getFluid("potion." + aName + ".splash") == null);
        boolean splashStrong = !(FluidRegistry.getFluid("potion." + aName + ".strong") == null
            || FluidRegistry.getFluid("potion." + aName + ".strong.splash") == null);
        boolean splashLong = !(FluidRegistry.getFluid("potion." + aName + ".long") == null
            || FluidRegistry.getFluid("potion." + aName + ".long.splash") == null);

        if (splash) GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Gunpowder, 1))
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("potion." + aName), 750))
            .fluidOutputs(new FluidStack(FluidRegistry.getFluid("potion." + aName + ".splash"), 750))
            .duration(10 * SECONDS)
            .eut(24)
            .addTo(mixerRecipes);

        if (splashStrong) GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Gunpowder, 1))
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("potion." + aName + ".strong"), 750))
            .fluidOutputs(new FluidStack(FluidRegistry.getFluid("potion." + aName + ".strong.splash"), 750))
            .duration(10 * SECONDS)
            .eut(24)
            .addTo(mixerRecipes);

        if (splashLong) GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Gunpowder, 1))
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("potion." + aName + ".long"), 750))
            .fluidOutputs(new FluidStack(FluidRegistry.getFluid("potion." + aName + ".long.splash"), 750))
            .duration(10 * SECONDS)
            .eut(24)
            .addTo(mixerRecipes);
    }

    public void registerSingleBlockAndMulti() {
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.NaquadahEnriched.getDust(4), Materials.Holmium.getDust(1))
            .circuit(4)
            .itemOutputs(Materials.EnrichedHolmium.getDust(5))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(mixerRecipes);

        // Catalysts for Plasma Forge.
        {
            GTValues.RA.stdBuilder()
                .circuit(9)
                .fluidInputs(
                    Materials.Helium.getPlasma(1_000),
                    Materials.Iron.getPlasma(1_000),
                    Materials.Calcium.getPlasma(1_000),
                    Materials.Niobium.getPlasma(1_000))
                .fluidOutputs(Materials.DTCC.getFluid(1_000))
                .duration(41 * MINUTES + 40 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .addTo(mixerNonCellRecipes);

            GTValues.RA.stdBuilder()
                .circuit(10)
                .fluidInputs(
                    Materials.DTCC.getFluid(1_000),
                    Materials.Radon.getPlasma(1_000),
                    Materials.Nickel.getPlasma(1_000),
                    Materials.Boron.getPlasma(1_000),
                    Materials.Sulfur.getPlasma(1_000))
                .fluidOutputs(Materials.DTPC.getFluid(1_000))
                .duration(41 * MINUTES + 40 * SECONDS)
                .eut(TierEU.RECIPE_UV)
                .addTo(mixerNonCellRecipes);

            GTValues.RA.stdBuilder()
                .circuit(11)
                .fluidInputs(
                    Materials.DTPC.getFluid(1_000),
                    Materials.Nitrogen.getPlasma(1_000),
                    Materials.Zinc.getPlasma(1_000),
                    Materials.Silver.getPlasma(1_000),
                    Materials.Titanium.getPlasma(1_000))
                .fluidOutputs(Materials.DTRC.getFluid(1_000))
                .duration(41 * MINUTES + 40 * SECONDS)
                .eut(TierEU.RECIPE_UHV)
                .addTo(mixerNonCellRecipes);

            GTValues.RA.stdBuilder()
                .circuit(12)
                .fluidInputs(
                    Materials.DTRC.getFluid(1_000),
                    Materials.Americium.getPlasma(1_000),
                    Materials.Bismuth.getPlasma(1_000),
                    Materials.Oxygen.getPlasma(1_000),
                    Materials.Tin.getPlasma(1_000))
                .fluidOutputs(Materials.DTEC.getFluid(1_000))
                .duration(41 * MINUTES + 40 * SECONDS)
                .eut(TierEU.RECIPE_UEV)
                .addTo(mixerNonCellRecipes);

            GTValues.RA.stdBuilder()
                .circuit(13)
                .fluidInputs(
                    Materials.DTEC.getFluid(1_000),
                    Materials.Lead.getPlasma(1_000),
                    Materials.Thorium.getPlasma(1_000),
                    Materials.Naquadria.getPlasma(100L),
                    Materials.RawStarMatter.getFluid(25L))
                .fluidOutputs(Materials.DTSC.getFluid(1_000))
                .duration(41 * MINUTES + 40 * SECONDS)
                .eut(TierEU.RECIPE_UIV)
                .addTo(mixerNonCellRecipes);
        }

        if (Mods.CropsNH.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Mods.CropsNH.ID, "weedEx", 1))
                .fluidInputs(Materials.NaphthenicAcid.getFluid(10))
                .fluidOutputs(Materials.WeedEX9000.getFluid(750))
                .duration(5 * SECONDS)
                .eut(100)
                .addTo(mixerRecipes);
        }

        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.Tritanium.getDust(11),
                GGMaterial.orundum.get(OrePrefixes.dust, 8),
                Materials.Rubidium.getDust(11),
                Materials.FierySteel.getDust(7),
                Materials.Firestone.getDust(13),
                GGMaterial.atomicSeparationCatalyst.get(OrePrefixes.dust, 13))
            .circuit(6)
            .itemOutputs(Materials.Mellion.getDust(63))
            .fluidInputs(Materials.DTR.getFluid(5_000))
            .duration(15 * SECONDS)
            .eut((int) TierEU.RECIPE_UMV)
            .addTo(mixerRecipes);
    }
}
