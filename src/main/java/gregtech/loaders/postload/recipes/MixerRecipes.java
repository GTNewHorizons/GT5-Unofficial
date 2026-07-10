package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.AppliedEnergistics2;
import static gregtech.api.enums.Mods.BiomesOPlenty;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.IndustrialCraft2;
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

import java.util.Locale;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.system.material.WerkstoffLoader;
import goodgenerator.items.GGMaterial;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2CellShapes;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.objects.OreDictItemStack;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import mods.railcraft.common.blocks.aesthetics.cube.EnumCube;

@SuppressWarnings({ "PointlessArithmeticExpression" })
public class MixerRecipes implements Runnable {

    @Override
    public void run() {

        registerSingleBlockAndMulti();

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.EnderPearl, Materials2Shapes.shapeDust, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Blaze, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.EnderEye, OrePrefixes.dust.getMaterialAmount()))
            .duration(5 * SECONDS)
            .eut(48)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Gold, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Silver, Materials2Shapes.shapeDust, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.Electrum, 2L * OrePrefixes.dust.getMaterialAmount()))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.shapeDust, 2),
                MaterialLibAPI.getStack(Materials2Materials.Nickel, Materials2Shapes.shapeDust, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.Invar, 3L * OrePrefixes.dust.getMaterialAmount()))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Tin, Materials2Shapes.shapeDust, 1))
            .circuit(5)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.TinAlloy, 2L * OrePrefixes.dust.getMaterialAmount()))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.shapeDust, 4),
                MaterialLibAPI.getStack(Materials2Materials.Invar, Materials2Shapes.shapeDust, 3),
                MaterialLibAPI.getStack(Materials2Materials.Manganese, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Chrome, Materials2Shapes.shapeDust, 1))
            .circuit(1)
            .itemOutputs(
                GTOreDictUnificator.getDust(Materials.StainlessSteel, 9L * OrePrefixes.dust.getMaterialAmount()))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Aluminium, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Chrome, Materials2Shapes.shapeDust, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.Kanthal, 3L * OrePrefixes.dust.getMaterialAmount()))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Copper, Materials2Shapes.shapeDust, 3),
                MaterialLibAPI.getStack(Materials2Materials.Zinc, Materials2Shapes.shapeDust, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.Brass, 4L * OrePrefixes.dust.getMaterialAmount()))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Copper, Materials2Shapes.shapeDust, 3),
                MaterialLibAPI.getStack(Materials2Materials.Tin, Materials2Shapes.shapeDust, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.Bronze, 4L * OrePrefixes.dust.getMaterialAmount()))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Copper, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Nickel, Materials2Shapes.shapeDust, 1))
            .circuit(3)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.Cupronickel, 2L * OrePrefixes.dust.getMaterialAmount()))
            .duration(10 * SECONDS)
            .eut(24)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Copper, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Silver, Materials2Shapes.shapeDust, 4))
            .circuit(4)
            .itemOutputs(
                GTOreDictUnificator.getDust(Materials.SterlingSilver, 5L * OrePrefixes.dust.getMaterialAmount()))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Copper, Materials2Shapes.shapeDust, 3),
                MaterialLibAPI.getStack(Materials2Materials.Electrum, Materials2Shapes.shapeDust, 2))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.BlackBronze, 5L * OrePrefixes.dust.getMaterialAmount()))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Bismuth, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Brass, Materials2Shapes.shapeDust, 4))
            .circuit(1)
            .itemOutputs(
                GTOreDictUnificator.getDust(Materials.BismuthBronze, 5L * OrePrefixes.dust.getMaterialAmount()))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.BlackBronze, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Nickel, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.shapeDust, 3))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.BlackSteel, 5L * OrePrefixes.dust.getMaterialAmount()))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.SterlingSilver, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.BismuthBronze, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.BlackSteel, Materials2Shapes.shapeDust, 4),
                MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.shapeDust, 2))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.RedSteel, 8L * OrePrefixes.dust.getMaterialAmount()))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.RoseGold, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Brass, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.BlackSteel, Materials2Shapes.shapeDust, 4),
                MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.shapeDust, 2))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.BlueSteel, 8L * OrePrefixes.dust.getMaterialAmount()))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Copper, Materials2Shapes.shapeDust, 3),
                MaterialLibAPI.getStack(Materials2Materials.Gold, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Silver, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Nickel, Materials2Shapes.shapeDust, 5),
                MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.shapeDust, 15))
            .circuit(14)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.BlackSteel, 25L * OrePrefixes.dust.getMaterialAmount()))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Copper, Materials2Shapes.shapeDust, 4),
                MaterialLibAPI.getStack(Materials2Materials.Silver, Materials2Shapes.shapeDust, 4),
                MaterialLibAPI.getStack(Materials2Materials.Bismuth, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Zinc, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.BlackSteel, Materials2Shapes.shapeDust, 20),
                MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.shapeDust, 10))
            .circuit(15)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.RedSteel, 40L * OrePrefixes.dust.getMaterialAmount()))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Copper, Materials2Shapes.shapeDust, 19),
                MaterialLibAPI.getStack(Materials2Materials.Gold, Materials2Shapes.shapeDust, 16),
                MaterialLibAPI.getStack(Materials2Materials.Zinc, Materials2Shapes.shapeDust, 5),
                MaterialLibAPI.getStack(Materials2Materials.BlackSteel, Materials2Shapes.shapeDust, 64),
                MaterialLibAPI.getStack(Materials2Materials.BlackSteel, Materials2Shapes.shapeDust, 16),
                MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.shapeDust, 40))
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
                MaterialLibAPI.getStack(Materials2Materials.Cobalt, Materials2Shapes.shapeDust, 5),
                MaterialLibAPI.getStack(Materials2Materials.Chrome, Materials2Shapes.shapeDust, 2),
                MaterialLibAPI.getStack(Materials2Materials.Nickel, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Molybdenum, Materials2Shapes.shapeDust, 1))
            .circuit(2)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.Ultimet, 9L * OrePrefixes.dust.getMaterialAmount()))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Brass, Materials2Shapes.shapeDust, 7),
                MaterialLibAPI.getStack(Materials2Materials.Tin, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Cobalt, Materials2Shapes.shapeDust, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.CobaltBrass, 9L * OrePrefixes.dust.getMaterialAmount()))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Saltpeter, Materials2Shapes.shapeDust, 2),
                MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Coal, Materials2Shapes.shapeDust, 3))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.Gunpowder, 6L * OrePrefixes.dust.getMaterialAmount()))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Saltpeter, Materials2Shapes.shapeDust, 2),
                MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Charcoal, Materials2Shapes.shapeDust, 3))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.Gunpowder, 6L * OrePrefixes.dust.getMaterialAmount()))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Saltpeter, Materials2Shapes.shapeDust, 2),
                MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.shapeDust, 3))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.Gunpowder, 6L * OrePrefixes.dust.getMaterialAmount()))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Indium, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Gallium, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Phosphorus, Materials2Shapes.shapeDust, 1))
            .circuit(1)
            .itemOutputs(
                GTOreDictUnificator
                    .getDust(Materials.IndiumGalliumPhosphide, 3L * OrePrefixes.dust.getMaterialAmount()))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Brick, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Clay, Materials2Shapes.shapeDust, 1))
            .itemOutputs(GTOreDictUnificator.getDust(Materials.Fireclay, 2L * OrePrefixes.dust.getMaterialAmount()))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Nickel, Materials2Shapes.shapeDust, 4),
                MaterialLibAPI.getStack(Materials2Materials.Chrome, Materials2Shapes.shapeDust, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.Nichrome, 5L * OrePrefixes.dust.getMaterialAmount()))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Osmium, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Iridium, Materials2Shapes.shapeDust, 3))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.Osmiridium, 4L * OrePrefixes.dust.getMaterialAmount()))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Niobium, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Titanium, Materials2Shapes.shapeDust, 1))
            .circuit(1)
            .itemOutputs(
                GTOreDictUnificator.getDust(Materials.NiobiumTitanium, 2L * OrePrefixes.dust.getMaterialAmount()))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Vanadium, Materials2Shapes.shapeDust, 3),
                MaterialLibAPI.getStack(Materials2Materials.Gallium, Materials2Shapes.shapeDust, 1))
            .circuit(1)
            .itemOutputs(
                GTOreDictUnificator.getDust(Materials.VanadiumGallium, 4L * OrePrefixes.dust.getMaterialAmount()))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Tungsten, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.shapeDust, 1))
            .circuit(1)
            .itemOutputs(
                GTOreDictUnificator.getDust(Materials.TungstenCarbide, 2L * OrePrefixes.dust.getMaterialAmount()))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Tungsten, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.shapeDust, 1))
            .circuit(1)
            .itemOutputs(
                GTOreDictUnificator.getDust(Materials.TungstenSteel, 2L * OrePrefixes.dust.getMaterialAmount()))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Titanium, Materials2Shapes.shapeDust, 3),
                MaterialLibAPI.getStack(Materials2Materials.Platinum, Materials2Shapes.shapeDust, 3),
                MaterialLibAPI.getStack(Materials2Materials.Vanadium, Materials2Shapes.shapeDust, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.TPV, 7L * OrePrefixes.dust.getMaterialAmount()))
            .duration(8 * SECONDS + 15 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.TungstenSteel, Materials2Shapes.shapeDust, 5),
                MaterialLibAPI.getStack(Materials2Materials.Chrome, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Molybdenum, Materials2Shapes.shapeDust, 2),
                MaterialLibAPI.getStack(Materials2Materials.Vanadium, Materials2Shapes.shapeDust, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.HSSG, 9L * OrePrefixes.dust.getMaterialAmount()))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.HSSG, Materials2Shapes.shapeDust, 6),
                MaterialLibAPI.getStack(Materials2Materials.Cobalt, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Manganese, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Silicon, Materials2Shapes.shapeDust, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.HSSE, 9L * OrePrefixes.dust.getMaterialAmount()))
            .duration(35 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.HSSG, Materials2Shapes.shapeDust, 6),
                MaterialLibAPI.getStack(Materials2Materials.Iridium, Materials2Shapes.shapeDust, 2),
                MaterialLibAPI.getStack(Materials2Materials.Osmium, Materials2Shapes.shapeDust, 1))
            .circuit(2)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.HSSS, 9L * OrePrefixes.dust.getMaterialAmount()))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Nickel, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Zinc, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.shapeDust, 4))
            .circuit(3)
            .itemOutputs(
                GTOreDictUnificator.getDust(Materials.FerriteMixture, 6L * OrePrefixes.dust.getMaterialAmount()))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Boron, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Glass, Materials2Shapes.shapeDust, 7))
            .itemOutputs(
                GTOreDictUnificator.getDust(Materials.BorosilicateGlass, 8L * OrePrefixes.dust.getMaterialAmount()))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.shapeDust, 3),
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.shapeDust, 23),
                MaterialLibAPI.getStack(Materials2Materials.Nickel, Materials2Shapes.shapeDust, 5),
                MaterialLibAPI.getStack(Materials2Materials.Chrome, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Silicon, Materials2Shapes.shapeDust, 12),
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.shapeDust, 3),
                MaterialLibAPI.getStack(Materials2Materials.Aluminium, Materials2Shapes.shapeDust, 1))
            .circuit(20)
            .itemOutputs(MaterialsAlloy.EGLIN_STEEL.getDust(48))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Platinum, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Silver, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Tin, Materials2Shapes.shapeDust, 2),
                MaterialLibAPI.getStack(Materials2Materials.Thaumium, Materials2Shapes.shapeDust, 2),
                MaterialLibAPI.getStack(Materials2Materials.EnderPearl, Materials2Shapes.shapeDust, 2))
            .circuit(16)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Enderium, Materials2Shapes.shapeDust, 8))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Items.rotten_flesh, 1, 0),
                new ItemStack(Items.fermented_spider_eye, 1, 0),
                ItemList.IC2_Scrap.get(1),
                MaterialLibAPI.getStack(Materials2Materials.MeatRaw, Materials2Shapes.shapeDust, 1))
            .itemOutputs(ItemList.Food_Chum.get(4))
            .fluidInputs(getFluidStack("potion.purpledrink", 750))
            .fluidOutputs(getFluidStack("sludge", 1_000))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(24)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Wheat, Materials2Shapes.shapeDust, 1))
            .itemOutputs(ItemList.Food_Dough.get(2))
            .fluidInputs(Materials.Water.getFluid(1_000))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Chili, Materials2Shapes.shapeDust, 1),
                ItemList.Food_PotatoChips.get(1))
            .itemOutputs(ItemList.Food_ChiliChips.get(1))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Redstone, Materials2Shapes.shapeDustTiny, 5),
                MaterialLibAPI.getStack(Materials2Materials.Ruby, Materials2Shapes.shapeDustTiny, 4))
            .itemOutputs(ItemList.IC2_Energium_Dust.get(1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Redstone, Materials2Shapes.shapeDust, 5),
                MaterialLibAPI.getStack(Materials2Materials.Ruby, Materials2Shapes.shapeDust, 4))
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
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Gold, Materials2Shapes.shapeDustTiny, 1),
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.LiveRoot, Materials2Shapes.shapeDust, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.IronWood, Materials2Shapes.shapeDust, 2))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Gold, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.shapeDust, 9),
                MaterialLibAPI.getStack(Materials2Materials.LiveRoot, Materials2Shapes.shapeDust, 9))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.IronWood, Materials2Shapes.shapeDust, 18))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 1),
                MaterialLibAPI.getStack(Materials2Materials.Redstone, Materials2Shapes.shapeDust, 1),
                GTOreDictUnificator.get(OrePrefixes.gem, Materials.NetherQuartz, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Fluix, 2))
            .fluidInputs(Materials.Water.getFluid(500))
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 1),
                MaterialLibAPI.getStack(Materials2Materials.Redstone, Materials2Shapes.shapeDust, 1),
                GTOreDictUnificator.get(OrePrefixes.gem, Materials.NetherQuartz, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Fluix, 2))
            .fluidInputs(GTModHandler.getDistilledWater(500))
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.LightFuel, Materials2CellShapes.shapeCell, 5),
                MaterialLibAPI.getStack(Materials2Materials.HeavyFuel, Materials2CellShapes.shapeCell, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Diesel, 6))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.LightFuel, Materials2CellShapes.shapeCell, 5),
                Materials.Empty.getCells(1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Diesel, 6))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HeavyFuel, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.HeavyFuel, Materials2CellShapes.shapeCell, 1),
                Materials.Empty.getCells(5))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Diesel, 6))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.LightFuel, Materials2FluidShapes.shapeFluidLiquid, 5_000))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.LightFuel, Materials2CellShapes.shapeCell, 5))
            .circuit(5)
            .itemOutputs(Materials.Empty.getCells(5))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HeavyFuel, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(Materials.Diesel.getFluid(6_000))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.HeavyFuel, Materials2CellShapes.shapeCell, 1))
            .circuit(6)
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.LightFuel, Materials2FluidShapes.shapeFluidLiquid, 5_000))
            .fluidOutputs(Materials.Diesel.getFluid(6_000))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 5),
                MaterialLibAPI.getStack(Materials2Materials.Stone, Materials2Shapes.shapeDust, 1))
            .itemOutputs(Materials.Empty.getCells(5))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.shapeFluidLiquid, 20))
            .fluidOutputs(new FluidStack(ItemList.sDrillingFluid, 5_000))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Lapis, Materials2Shapes.shapeDust, 1))
            .circuit(4)
            .fluidInputs(Materials.Water.getFluid(125))
            .fluidOutputs(GTModHandler.getIC2Coolant(125))
            .duration(12 * SECONDS + 16 * TICKS)
            .eut(48)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Lapis, Materials2Shapes.shapeDust, 1))
            .circuit(4)
            .fluidInputs(GTModHandler.getDistilledWater(1_000))
            .fluidOutputs(GTModHandler.getIC2Coolant(1_000))
            .duration(12 * SECONDS + 16 * TICKS)
            .eut(48)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Sodium, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Wood, Materials2Shapes.shapeDust, 4))
            .circuit(1)
            .itemOutputs(ItemList.SFMixture.get(4))
            .fluidInputs(Materials.GlueAdvanced.getFluid(200))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Lithium, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Wood, Materials2Shapes.shapeDust, 4))
            .circuit(1)
            .itemOutputs(ItemList.SFMixture.get(8))
            .fluidInputs(Materials.GlueAdvanced.getFluid(200))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Caesium, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Wood, Materials2Shapes.shapeDust, 4))
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
                MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Sodium, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Wood, Materials2Shapes.shapeDust, 4))
            .circuit(1)
            .itemOutputs(ItemList.SFMixture.get(64))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.McGuffium239, Materials2FluidShapes.shapeFluidLiquid, 12))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Lithium, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Wood, Materials2Shapes.shapeDust, 4))
            .circuit(1)
            .itemOutputs(ItemList.SFMixture.get(64))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.McGuffium239, Materials2FluidShapes.shapeFluidLiquid, 8))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Caesium, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Wood, Materials2Shapes.shapeDust, 4))
            .circuit(1)
            .itemOutputs(ItemList.SFMixture.get(64))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.McGuffium239, Materials2FluidShapes.shapeFluidLiquid, 4))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        // Molten Red Granite
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Redstone, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.shapeDust, 1))
            .fluidInputs(Materials.Lava.getFluid(125L))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.GraniteRed, Materials2FluidShapes.shapeFluidMolten, 1 * INGOTS))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);
        // Molten Black Granite
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Stone, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.shapeDust, 1))
            .fluidInputs(Materials.Lava.getFluid(125L))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.GraniteBlack,
                    Materials2FluidShapes.shapeFluidMolten,
                    1 * INGOTS))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.SFMixture.get(2),
                MaterialLibAPI.getStack(Materials2Materials.EnderEye, Materials2Shapes.shapeDust, 1))
            .itemOutputs(ItemList.MSFMixture.get(4))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Mercury, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.SFMixture.get(1), GTOreDictUnificator.get(OrePrefixes.dust, Materials.Blaze, 1))
            .itemOutputs(ItemList.MSFMixture.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Mercury, Materials2FluidShapes.shapeFluidLiquid, 500))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Lignite, 4),
                ItemList.MSFMixture.get(24),
                MaterialLibAPI.getStack(Materials2Materials.Diamond, Materials2Shapes.shapeDust, 1))
            .circuit(1)
            .itemOutputs(ItemList.Block_MSSFUEL.get(4))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitroFuel, Materials2FluidShapes.shapeFluidLiquid, 4_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Charcoal, 4),
                ItemList.MSFMixture.get(16),
                MaterialLibAPI.getStack(Materials2Materials.Diamond, Materials2Shapes.shapeDust, 1))
            .circuit(1)
            .itemOutputs(ItemList.Block_MSSFUEL.get(4))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitroFuel, Materials2FluidShapes.shapeFluidLiquid, 3_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Coal, 4),
                ItemList.MSFMixture.get(8),
                MaterialLibAPI.getStack(Materials2Materials.Diamond, Materials2Shapes.shapeDust, 1))
            .circuit(1)
            .itemOutputs(ItemList.Block_MSSFUEL.get(4))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitroFuel, Materials2FluidShapes.shapeFluidLiquid, 2_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Lignite, 4),
                ItemList.MSFMixture.get(24),
                MaterialLibAPI.getStack(Materials2Materials.Diamond, Materials2Shapes.shapeDust, 1))
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
                MaterialLibAPI.getStack(Materials2Materials.Diamond, Materials2Shapes.shapeDust, 1))
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
                MaterialLibAPI.getStack(Materials2Materials.Diamond, Materials2Shapes.shapeDust, 1))
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
                    MaterialLibAPI.getStack(Materials2Materials.InfusedAir, Materials2Shapes.shapeDust, 1))
                .circuit(1)
                .itemOutputs(ItemList.MSFMixture.get(20))
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.FierySteel, Materials2FluidShapes.shapeFluidLiquid, 50))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MV / 2)
                .addTo(mixerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.SFMixture.get(20),
                    MaterialLibAPI.getStack(Materials2Materials.InfusedEarth, Materials2Shapes.shapeDust, 1))
                .circuit(1)
                .itemOutputs(ItemList.MSFMixture.get(20))
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.FierySteel, Materials2FluidShapes.shapeFluidLiquid, 50))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MV / 2)
                .addTo(mixerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.SFMixture.get(20),
                    MaterialLibAPI.getStack(Materials2Materials.InfusedEntropy, Materials2Shapes.shapeDust, 1))
                .circuit(1)
                .itemOutputs(ItemList.MSFMixture.get(20))
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.FierySteel, Materials2FluidShapes.shapeFluidLiquid, 50))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MV / 2)
                .addTo(mixerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.SFMixture.get(20),
                    MaterialLibAPI.getStack(Materials2Materials.InfusedFire, Materials2Shapes.shapeDust, 1))
                .circuit(1)
                .itemOutputs(ItemList.MSFMixture.get(20))
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.FierySteel, Materials2FluidShapes.shapeFluidLiquid, 50))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MV / 2)
                .addTo(mixerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.SFMixture.get(20),
                    MaterialLibAPI.getStack(Materials2Materials.InfusedOrder, Materials2Shapes.shapeDust, 1))
                .circuit(1)
                .itemOutputs(ItemList.MSFMixture.get(20))
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.FierySteel, Materials2FluidShapes.shapeFluidLiquid, 50))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MV / 2)
                .addTo(mixerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.SFMixture.get(20),
                    MaterialLibAPI.getStack(Materials2Materials.InfusedWater, Materials2Shapes.shapeDust, 1))
                .circuit(1)
                .itemOutputs(ItemList.MSFMixture.get(20))
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.FierySteel, Materials2FluidShapes.shapeFluidLiquid, 50))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MV / 2)
                .addTo(mixerRecipes);

            FluidStack tFD = getFluidStack("fluiddeath", 30);
            if (tFD != null && tFD.getFluid() != null && tFD.amount > 0) {
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        ItemList.SFMixture.get(30),
                        MaterialLibAPI.getStack(Materials2Materials.InfusedAir, Materials2Shapes.shapeDust, 1))
                    .circuit(1)
                    .itemOutputs(ItemList.MSFMixture.get(30))
                    .fluidInputs(tFD)
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_MV / 2)
                    .addTo(mixerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(
                        ItemList.SFMixture.get(30),
                        MaterialLibAPI.getStack(Materials2Materials.InfusedEarth, Materials2Shapes.shapeDust, 1))
                    .circuit(1)
                    .itemOutputs(ItemList.MSFMixture.get(30))
                    .fluidInputs(tFD)
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_MV / 2)
                    .addTo(mixerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(
                        ItemList.SFMixture.get(30),
                        MaterialLibAPI.getStack(Materials2Materials.InfusedEntropy, Materials2Shapes.shapeDust, 1))
                    .circuit(1)
                    .itemOutputs(ItemList.MSFMixture.get(30))
                    .fluidInputs(tFD)
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_MV / 2)
                    .addTo(mixerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(
                        ItemList.SFMixture.get(30),
                        MaterialLibAPI.getStack(Materials2Materials.InfusedFire, Materials2Shapes.shapeDust, 1))
                    .circuit(1)
                    .itemOutputs(ItemList.MSFMixture.get(30))
                    .fluidInputs(tFD)
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_MV / 2)
                    .addTo(mixerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(
                        ItemList.SFMixture.get(30),
                        MaterialLibAPI.getStack(Materials2Materials.InfusedOrder, Materials2Shapes.shapeDust, 1))
                    .circuit(1)
                    .itemOutputs(ItemList.MSFMixture.get(30))
                    .fluidInputs(tFD)
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_MV / 2)
                    .addTo(mixerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(
                        ItemList.SFMixture.get(30),
                        MaterialLibAPI.getStack(Materials2Materials.InfusedWater, Materials2Shapes.shapeDust, 1))
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
                    .fluidInputs(
                        MaterialLibAPI.getFluidStack(
                            Materials2Materials.NitroFuel,
                            Materials2FluidShapes.shapeFluidLiquid,
                            1_000))
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
                    .fluidInputs(
                        MaterialLibAPI
                            .getFluidStack(Materials2Materials.NitroFuel, Materials2FluidShapes.shapeFluidLiquid, 750))
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
                    .fluidInputs(
                        MaterialLibAPI
                            .getFluidStack(Materials2Materials.NitroFuel, Materials2FluidShapes.shapeFluidLiquid, 500))
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
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitroFuel, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(7 * SECONDS)
            .eut(250)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.block, Materials.Charcoal, 1), ItemList.SFMixture.get(4))
            .circuit(1)
            .itemOutputs(ItemList.Block_SSFUEL.get(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitroFuel, Materials2FluidShapes.shapeFluidLiquid, 750))
            .duration(6 * SECONDS)
            .eut(250)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.block, Materials.Coal, 1), ItemList.SFMixture.get(2))
            .circuit(1)
            .itemOutputs(ItemList.Block_SSFUEL.get(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitroFuel, Materials2FluidShapes.shapeFluidLiquid, 500))
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
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.SulfuricAcid, Materials2CellShapes.shapeCell, 1))
            .circuit(1)
            .itemOutputs(ItemList.Cell_Empty.get(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(new FluidStack(ItemList.sNitrationMixture, 2_000))
            .duration(24 * SECONDS)
            .eut(2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.SulfuricAcid, Materials2CellShapes.shapeCell, 1),
                MaterialLibAPI.getStack(Materials2Materials.NitricAcid, Materials2CellShapes.shapeCell, 1))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.NitrationMixture, Materials2CellShapes.shapeCell, 2))
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
                MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.shapeDust, 4))
            .circuit(3)
            .itemOutputs(getModItem(Forestry.ID, "fertilizerBio", 1L, 0))
            .fluidInputs(Materials.Water.getFluid(100))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        // radiation manufacturing

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.SluiceSand, Materials2Shapes.shapeDust, 1))
            .fluidInputs(Materials.Water.getFluid(500))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SluiceJuice, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        // NaCl + H2O = (NaCl·H2O)

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Salt, Materials2Shapes.shapeDust, 2))
            .circuit(3)
            .fluidInputs(Materials.Water.getFluid(1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SaltWater, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        // CaCO3 + 2 CH3COOH = Ca(CH3COO)2 + H2O + CO2

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Calcite, Materials2Shapes.shapeDust, 5),
                Materials.Empty.getCells(2))
            .circuit(1)
            .itemOutputs(
                Materials.Water.getCells(1),
                MaterialLibAPI.getStack(Materials2Materials.CarbonDioxide, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.AceticAcid, Materials2FluidShapes.shapeFluidLiquid, 2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.CalciumAcetateSolution,
                    Materials2FluidShapes.shapeFluidLiquid,
                    1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Calcite, Materials2Shapes.shapeDust, 5),
                Materials.Empty.getCells(2))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.CalciumAcetateSolution, Materials2CellShapes.shapeCell, 1),
                MaterialLibAPI.getStack(Materials2Materials.CarbonDioxide, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.AceticAcid, Materials2FluidShapes.shapeFluidLiquid, 2_000))
            .fluidOutputs(Materials.Water.getFluid(1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Calcite, Materials2Shapes.shapeDust, 5),
                Materials.Empty.getCells(2))
            .circuit(21)
            .itemOutputs(
                Materials.Water.getCells(1),
                MaterialLibAPI.getStack(Materials2Materials.CalciumAcetateSolution, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.AceticAcid, Materials2FluidShapes.shapeFluidLiquid, 2_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Calcite, Materials2Shapes.shapeDust, 5),
                Materials.Empty.getCells(1))
            .circuit(4)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.CarbonDioxide, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.AceticAcid, Materials2FluidShapes.shapeFluidLiquid, 2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.CalciumAcetateSolution,
                    Materials2FluidShapes.shapeFluidLiquid,
                    1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Calcite, Materials2Shapes.shapeDust, 5),
                Materials.Empty.getCells(1))
            .circuit(14)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.CalciumAcetateSolution, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.AceticAcid, Materials2FluidShapes.shapeFluidLiquid, 2_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        // Ca + 2 CH3COOH = Ca(CH3COO)2 + 2H

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Calcium, Materials2Shapes.shapeDust, 1),
                Materials.Empty.getCells(2))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.shapeCell, 2))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.AceticAcid, Materials2FluidShapes.shapeFluidLiquid, 2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.CalciumAcetateSolution,
                    Materials2FluidShapes.shapeFluidLiquid,
                    1_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Calcium, Materials2Shapes.shapeDust, 1),
                Materials.Empty.getCells(1))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.CalciumAcetateSolution, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.AceticAcid, Materials2FluidShapes.shapeFluidLiquid, 2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 2_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        // CaO + 2 CH3COOH = Ca(CH3COO)2 + H2O

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Quicklime, Materials2Shapes.shapeDust, 2),
                Materials.Empty.getCells(1))
            .circuit(1)
            .itemOutputs(Materials.Water.getCells(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.AceticAcid, Materials2FluidShapes.shapeFluidLiquid, 2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.CalciumAcetateSolution,
                    Materials2FluidShapes.shapeFluidLiquid,
                    1_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Quicklime, Materials2Shapes.shapeDust, 2),
                Materials.Empty.getCells(1))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.CalciumAcetateSolution, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.AceticAcid, Materials2FluidShapes.shapeFluidLiquid, 2_000))
            .fluidOutputs(Materials.Water.getFluid(1_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        // 2CH3COOCH3 + 3CH3COCH3/(C4H6O2)n = 5Glue

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Acetone, Materials2CellShapes.shapeCell, 3))
            .itemOutputs(Materials.Empty.getCells(3))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.PolyvinylAcetate, Materials2FluidShapes.shapeFluidLiquid, 2_000))
            .fluidOutputs(Materials.GlueAdvanced.getFluid(5_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.PolyvinylAcetate, Materials2CellShapes.shapeCell, 2))
            .itemOutputs(Materials.Empty.getCells(2))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Acetone, Materials2FluidShapes.shapeFluidLiquid, 3_000))
            .fluidOutputs(Materials.GlueAdvanced.getFluid(5_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.MethylAcetate, Materials2CellShapes.shapeCell, 3))
            .itemOutputs(Materials.Empty.getCells(3))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.PolyvinylAcetate, Materials2FluidShapes.shapeFluidLiquid, 2_000))
            .fluidOutputs(Materials.GlueAdvanced.getFluid(5_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.PolyvinylAcetate, Materials2CellShapes.shapeCell, 2))
            .itemOutputs(Materials.Empty.getCells(2))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.MethylAcetate, Materials2FluidShapes.shapeFluidLiquid, 3_000))
            .fluidOutputs(Materials.GlueAdvanced.getFluid(5_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Sugar.getDust(4))
            .itemOutputs(Materials.Charcoal.getGems(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DilutedSulfuricAcid,
                    Materials2FluidShapes.shapeFluidLiquid,
                    1_000))
            .duration(60 * SECONDS)
            .eut(2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Wood, Materials2Shapes.shapeDust, 4))
            .itemOutputs(Materials.Charcoal.getGems(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DilutedSulfuricAcid,
                    Materials2FluidShapes.shapeFluidLiquid,
                    1_000))
            .duration(60 * SECONDS)
            .eut(2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Diesel.getCells(1))
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Tetranitromethane, Materials2FluidShapes.shapeFluidLiquid, 20))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitroFuel, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.BioDiesel, Materials2CellShapes.shapeCell, 1))
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Tetranitromethane, Materials2FluidShapes.shapeFluidLiquid, 40))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitroFuel, Materials2FluidShapes.shapeFluidLiquid, 900))
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        // CH4O + C4H8 = C5H12O

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Methanol, Materials2CellShapes.shapeCell, 1),
                MaterialLibAPI.getStack(Materials2Materials.Butene, Materials2CellShapes.shapeCell, 1))
            .itemOutputs(Materials.MTBEMixture.getCells(1), Materials.Empty.getCells(1))
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Methanol, Materials2CellShapes.shapeCell, 1),
                MaterialLibAPI.getStack(Materials2Materials.Butane, Materials2CellShapes.shapeCell, 1))
            .itemOutputs(Materials.MTBEMixtureAlt.getCells(1), Materials.Empty.getCells(1))
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Naphtha, Materials2CellShapes.shapeCell, 16),
                MaterialLibAPI.getStack(Materials2Materials.Gas, Materials2CellShapes.shapeCell, 2),
                MaterialLibAPI.getStack(Materials2Materials.Methanol, Materials2CellShapes.shapeCell, 1),
                MaterialLibAPI.getStack(Materials2Materials.Acetone, Materials2CellShapes.shapeCell, 1))
            .itemOutputs(Materials.GasolineRaw.getCells(20))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.GasolineRegular.getCells(20),
                MaterialLibAPI.getStack(Materials2Materials.Octane, Materials2CellShapes.shapeCell, 2),
                MaterialLibAPI.getStack(Materials2Materials.NitrousOxide, Materials2CellShapes.shapeCell, 6),
                MaterialLibAPI.getStack(Materials2Materials.Toluene, Materials2CellShapes.shapeCell, 1))
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
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.NitroFuel, Materials2FluidShapes.shapeFluidLiquid, 300))
                .duration(5 * SECONDS)
                .eut(250)
                .addTo(mixerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    EnumCube.COKE_BLOCK.getItem(4),
                    ItemList.SFMixture.get(8),
                    MaterialLibAPI.getStack(Materials2Materials.Diamond, Materials2Shapes.shapeDust, 1))
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
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.NitroFuel, Materials2FluidShapes.shapeFluidLiquid, 300))
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

        // From ProcessingFood - foodDough mixer
        GTValues.RA.stdBuilder()
            .itemInputs(
                new OreDictItemStack("foodDough", 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L))
            .itemOutputs(ItemList.Food_Dough_Sugar.get(2L))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                new OreDictItemStack("foodDough", 1),
                MaterialLibAPI.getStack(Materials2Materials.Cocoa, Materials2Shapes.shapeDust, 1))
            .itemOutputs(ItemList.Food_Dough_Chocolate.get(2L))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                new OreDictItemStack("foodDough", 1),
                MaterialLibAPI.getStack(Materials2Materials.Chocolate, Materials2Shapes.shapeDust, 1))
            .itemOutputs(ItemList.Food_Dough_Chocolate.get(2L))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        // From ProcessingCrop - cropTea mixer
        GTValues.RA.stdBuilder()
            .itemInputs(new OreDictItemStack("cropTea", 1))
            .fluidInputs(new FluidStack(FluidRegistry.WATER, 750))
            .fluidOutputs(new FluidStack(FluidRegistry.getFluid("potion.tea"), 750))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(4)
            .addTo(mixerRecipes);

        if (IndustrialCraft2.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(new OreDictItemStack("cropTea", 1))
                .fluidInputs(GTModHandler.getDistilledWater(750))
                .fluidOutputs(new FluidStack(FluidRegistry.getFluid("potion.tea"), 750))
                .duration(6 * SECONDS + 8 * TICKS)
                .eut(4)
                .addTo(mixerRecipes);
        }

        // From ProcessingCrop - cropGrape mixer
        GTValues.RA.stdBuilder()
            .itemInputs(new OreDictItemStack("cropGrape", 1))
            .fluidInputs(new FluidStack(FluidRegistry.WATER, 750))
            .fluidOutputs(new FluidStack(FluidRegistry.getFluid("potion.grapejuice"), 750))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(4)
            .addTo(mixerRecipes);

        if (IndustrialCraft2.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(new OreDictItemStack("cropGrape", 1))
                .fluidInputs(GTModHandler.getDistilledWater(750))
                .fluidOutputs(new FluidStack(FluidRegistry.getFluid("potion.grapejuice"), 750))
                .duration(6 * SECONDS + 8 * TICKS)
                .eut(4)
                .addTo(mixerRecipes);
        }

        // From ProcessingCrop - cropPotato mixer
        GTValues.RA.stdBuilder()
            .itemInputs(new OreDictItemStack("cropPotato", 1))
            .fluidInputs(new FluidStack(FluidRegistry.WATER, 750))
            .fluidOutputs(new FluidStack(FluidRegistry.getFluid("potion.potatojuice"), 750))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(4)
            .addTo(mixerRecipes);

        if (IndustrialCraft2.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(new OreDictItemStack("cropPotato", 1))
                .fluidInputs(GTModHandler.getDistilledWater(750))
                .fluidOutputs(new FluidStack(FluidRegistry.getFluid("potion.potatojuice"), 750))
                .duration(6 * SECONDS + 8 * TICKS)
                .eut(4)
                .addTo(mixerRecipes);
        }

        // From ProcessingCrop - cropLemon mixer
        GTValues.RA.stdBuilder()
            .itemInputs(new OreDictItemStack("cropLemon", 1))
            .fluidInputs(new FluidStack(FluidRegistry.WATER, 750))
            .fluidOutputs(new FluidStack(FluidRegistry.getFluid("potion.lemonjuice"), 750))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(4)
            .addTo(mixerRecipes);

        if (IndustrialCraft2.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(new OreDictItemStack("cropLemon", 1))
                .fluidInputs(GTModHandler.getDistilledWater(750))
                .fluidOutputs(new FluidStack(FluidRegistry.getFluid("potion.lemonjuice"), 750))
                .duration(6 * SECONDS + 8 * TICKS)
                .eut(4)
                .addTo(mixerRecipes);
        }

        // From ProcessingDye - dye mixer
        for (Dyes dye : Dyes.VALUES) {
            String fluidName = "dye.watermixed." + dye.name()
                .toLowerCase(Locale.ENGLISH);
            GTValues.RA.stdBuilder()
                .itemInputs(new OreDictItemStack(dye.name(), 1))
                .circuit(1)
                .fluidInputs(GTModHandler.getDistilledWater(2 * INGOTS))
                .fluidOutputs(FluidRegistry.getFluidStack(fluidName, 288))
                .duration(16 * TICKS)
                .eut(4)
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
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.NaquadahEnriched, Materials2Shapes.shapeDust, 4),
                MaterialLibAPI.getStack(Materials2Materials.Holmium, Materials2Shapes.shapeDust, 1))
            .circuit(4)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.EnrichedHolmium, Materials2Shapes.shapeDust, 5))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.NetherStar, Materials2Shapes.shapeDust, 8),
                MaterialLibAPI.getStack(Materials2Materials.Terbium, Materials2Shapes.shapeDust, 7),
                MaterialsElements.getInstance().TECHNETIUM.getDust(4),
                MaterialLibAPI.getStack(Materials2Materials.Unstable, Materials2Shapes.shapeDust, 4),
                Materials.Flerovium.getDust(3),
                MaterialLibAPI.getStack(Materials2Materials.InfinityCatalyst, Materials2Shapes.shapeDust, 1))
            .circuit(6)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Shijima, Materials2Shapes.shapeDust, 27))
            .duration(15 * SECONDS)
            .eut((int) TierEU.RECIPE_ZPM)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.TinAlloy, Materials2Shapes.shapeDust, 8),
                WerkstoffLoader.Ruridit.get(OrePrefixes.dust, 7),
                MaterialsAlloy.TRINIUM_NAQUADAH.getDust(4),
                GGMaterial.adamantiumAlloy.get(OrePrefixes.dust, 4),
                WerkstoffLoader.Californium.get(OrePrefixes.dust, 3),
                MaterialsAlloy.QUANTUM.getDust(1))
            .circuit(6)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Churitsu, Materials2Shapes.shapeDust, 27))
            .duration(15 * SECONDS)
            .eut((int) TierEU.RECIPE_ZPM)
            .addTo(mixerRecipes);

        // Catalysts for Plasma Forge.
        {
            GTValues.RA.stdBuilder()
                .circuit(9)
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Helium, Materials2FluidShapes.shapeFluidPlasma, 1_000),
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Iron, Materials2FluidShapes.shapeFluidPlasma, 1_000),
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Calcium, Materials2FluidShapes.shapeFluidPlasma, 1_000),
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Niobium, Materials2FluidShapes.shapeFluidPlasma, 1_000))
                .fluidOutputs(Materials.DTCC.getFluid(1_000))
                .duration(41 * MINUTES + 40 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .addTo(mixerNonCellRecipes);

            GTValues.RA.stdBuilder()
                .circuit(10)
                .fluidInputs(
                    Materials.DTCC.getFluid(1_000),
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Radon, Materials2FluidShapes.shapeFluidPlasma, 1_000),
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Nickel, Materials2FluidShapes.shapeFluidPlasma, 1_000),
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Boron, Materials2FluidShapes.shapeFluidPlasma, 1_000),
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Sulfur, Materials2FluidShapes.shapeFluidPlasma, 1_000))
                .fluidOutputs(Materials.DTPC.getFluid(1_000))
                .duration(41 * MINUTES + 40 * SECONDS)
                .eut(TierEU.RECIPE_UV)
                .addTo(mixerNonCellRecipes);

            GTValues.RA.stdBuilder()
                .circuit(11)
                .fluidInputs(
                    Materials.DTPC.getFluid(1_000),
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Nitrogen, Materials2FluidShapes.shapeFluidPlasma, 1_000),
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Zinc, Materials2FluidShapes.shapeFluidPlasma, 1_000),
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Silver, Materials2FluidShapes.shapeFluidPlasma, 1_000),
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Titanium, Materials2FluidShapes.shapeFluidPlasma, 1_000))
                .fluidOutputs(Materials.DTRC.getFluid(1_000))
                .duration(41 * MINUTES + 40 * SECONDS)
                .eut(TierEU.RECIPE_UHV)
                .addTo(mixerNonCellRecipes);

            GTValues.RA.stdBuilder()
                .circuit(12)
                .fluidInputs(
                    Materials.DTRC.getFluid(1_000),
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Americium, Materials2FluidShapes.shapeFluidPlasma, 1_000),
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Bismuth, Materials2FluidShapes.shapeFluidPlasma, 1_000),
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidPlasma, 1_000),
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Tin, Materials2FluidShapes.shapeFluidPlasma, 1_000))
                .fluidOutputs(Materials.DTEC.getFluid(1_000))
                .duration(41 * MINUTES + 40 * SECONDS)
                .eut(TierEU.RECIPE_UEV)
                .addTo(mixerNonCellRecipes);

            GTValues.RA.stdBuilder()
                .circuit(13)
                .fluidInputs(
                    Materials.DTEC.getFluid(1_000),
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Lead, Materials2FluidShapes.shapeFluidPlasma, 1_000),
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Thorium, Materials2FluidShapes.shapeFluidPlasma, 1_000),
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Naquadria, Materials2FluidShapes.shapeFluidPlasma, 100),
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.RawStarMatter, Materials2FluidShapes.shapeFluidLiquid, 25))
                .fluidOutputs(Materials.DTSC.getFluid(1_000))
                .duration(41 * MINUTES + 40 * SECONDS)
                .eut(TierEU.RECIPE_UIV)
                .addTo(mixerNonCellRecipes);
        }

        if (Mods.CropsNH.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Mods.CropsNH.ID, "weedEX", 1))
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.NaphthenicAcid, Materials2FluidShapes.shapeFluidLiquid, 10))
                .fluidOutputs(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.WeedEX9000, Materials2FluidShapes.shapeFluidLiquid, 750))
                .duration(5 * SECONDS)
                .eut(100)
                .addTo(mixerRecipes);
        }

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Tritanium, Materials2Shapes.shapeDust, 11),
                GGMaterial.orundum.get(OrePrefixes.dust, 8),
                MaterialLibAPI.getStack(Materials2Materials.Rubidium, Materials2Shapes.shapeDust, 11),
                MaterialLibAPI.getStack(Materials2Materials.FierySteel, Materials2Shapes.shapeDust, 7),
                MaterialLibAPI.getStack(Materials2Materials.Firestone, Materials2Shapes.shapeDust, 13),
                GGMaterial.atomicSeparationCatalyst.get(OrePrefixes.dust, 13))
            .circuit(6)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Mellion, Materials2Shapes.shapeDust, 63))
            .fluidInputs(Materials.DTR.getFluid(5_000))
            .duration(15 * SECONDS)
            .eut((int) TierEU.RECIPE_UMV)
            .addTo(mixerRecipes);
    }
}
