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
                MaterialLibAPI.getStack(Materials2Materials.EnderPearl, Materials2Shapes.dust, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Blaze, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.EnderEye, Materials2Shapes.dust, 1))
            .duration(5 * SECONDS)
            .eut(48)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Gold, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Silver, Materials2Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Electrum, Materials2Shapes.dust, 2))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials2Materials.Nickel, Materials2Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Invar, Materials2Shapes.dust, 3))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Tin, Materials2Shapes.dust, 1))
            .circuit(5)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.TinAlloy, Materials2Shapes.dust, 2))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials2Materials.Invar, Materials2Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials2Materials.Manganese, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Chrome, Materials2Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.StainlessSteel, Materials2Shapes.dust, 9))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Aluminium, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Chrome, Materials2Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Kanthal, Materials2Shapes.dust, 3))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Copper, Materials2Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials2Materials.Zinc, Materials2Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Brass, Materials2Shapes.dust, 4))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Copper, Materials2Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials2Materials.Tin, Materials2Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Bronze, Materials2Shapes.dust, 4))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Copper, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Nickel, Materials2Shapes.dust, 1))
            .circuit(3)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Cupronickel, Materials2Shapes.dust, 2))
            .duration(10 * SECONDS)
            .eut(24)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Copper, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Silver, Materials2Shapes.dust, 4))
            .circuit(4)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.SterlingSilver, Materials2Shapes.dust, 5))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Copper, Materials2Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials2Materials.Electrum, Materials2Shapes.dust, 2))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.BlackBronze, Materials2Shapes.dust, 5))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Bismuth, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Brass, Materials2Shapes.dust, 4))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.BismuthBronze, Materials2Shapes.dust, 5))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.BlackBronze, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Nickel, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.dust, 3))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.BlackSteel, Materials2Shapes.dust, 5))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.SterlingSilver, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.BismuthBronze, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.BlackSteel, Materials2Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.dust, 2))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.RedSteel, Materials2Shapes.dust, 8))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.RoseGold, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Brass, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.BlackSteel, Materials2Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.dust, 2))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.BlueSteel, Materials2Shapes.dust, 8))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Copper, Materials2Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials2Materials.Gold, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Silver, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Nickel, Materials2Shapes.dust, 5),
                MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.dust, 15))
            .circuit(14)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.BlackSteel, Materials2Shapes.dust, 25))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Copper, Materials2Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials2Materials.Silver, Materials2Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials2Materials.Bismuth, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Zinc, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.BlackSteel, Materials2Shapes.dust, 20),
                MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.dust, 10))
            .circuit(15)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.RedSteel, Materials2Shapes.dust, 40))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Copper, Materials2Shapes.dust, 19),
                MaterialLibAPI.getStack(Materials2Materials.Gold, Materials2Shapes.dust, 16),
                MaterialLibAPI.getStack(Materials2Materials.Zinc, Materials2Shapes.dust, 5),
                MaterialLibAPI.getStack(Materials2Materials.BlackSteel, Materials2Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials2Materials.BlackSteel, Materials2Shapes.dust, 16),
                MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.dust, 40))
            .circuit(16)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.BlueSteel, Materials2Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials2Materials.BlueSteel, Materials2Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials2Materials.BlueSteel, Materials2Shapes.dust, 32))
            .duration(3 * MINUTES)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Cobalt, Materials2Shapes.dust, 5),
                MaterialLibAPI.getStack(Materials2Materials.Chrome, Materials2Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials2Materials.Nickel, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Molybdenum, Materials2Shapes.dust, 1))
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Ultimet, Materials2Shapes.dust, 9))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Brass, Materials2Shapes.dust, 7),
                MaterialLibAPI.getStack(Materials2Materials.Tin, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Cobalt, Materials2Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.CobaltBrass, Materials2Shapes.dust, 9))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Saltpeter, Materials2Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Coal, Materials2Shapes.dust, 3))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.Gunpowder, 6L * OrePrefixes.dust.getMaterialAmount()))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Saltpeter, Materials2Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Charcoal, Materials2Shapes.dust, 3))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.Gunpowder, 6L * OrePrefixes.dust.getMaterialAmount()))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Saltpeter, Materials2Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dust, 3))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.getDust(Materials.Gunpowder, 6L * OrePrefixes.dust.getMaterialAmount()))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Indium, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Gallium, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Phosphorus, Materials2Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.IndiumGalliumPhosphide, Materials2Shapes.dust, 3))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Brick, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Clay, Materials2Shapes.dust, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Fireclay, Materials2Shapes.dust, 2))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Nickel, Materials2Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials2Materials.Chrome, Materials2Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Nichrome, Materials2Shapes.dust, 5))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Osmium, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Iridium, Materials2Shapes.dust, 3))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Osmiridium, Materials2Shapes.dust, 4))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Niobium, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Titanium, Materials2Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.NiobiumTitanium, Materials2Shapes.dust, 2))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Vanadium, Materials2Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials2Materials.Gallium, Materials2Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.VanadiumGallium, Materials2Shapes.dust, 4))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Tungsten, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.TungstenCarbide, Materials2Shapes.dust, 2))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Tungsten, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.TungstenSteel, Materials2Shapes.dust, 2))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Titanium, Materials2Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials2Materials.Platinum, Materials2Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials2Materials.Vanadium, Materials2Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.TPVAlloy, Materials2Shapes.dust, 7))
            .duration(8 * SECONDS + 15 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.TungstenSteel, Materials2Shapes.dust, 5),
                MaterialLibAPI.getStack(Materials2Materials.Chrome, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Molybdenum, Materials2Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials2Materials.Vanadium, Materials2Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.HSSG, Materials2Shapes.dust, 9))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.HSSG, Materials2Shapes.dust, 6),
                MaterialLibAPI.getStack(Materials2Materials.Cobalt, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Manganese, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Silicon, Materials2Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.HSSE, Materials2Shapes.dust, 9))
            .duration(35 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.HSSG, Materials2Shapes.dust, 6),
                MaterialLibAPI.getStack(Materials2Materials.Iridium, Materials2Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials2Materials.Osmium, Materials2Shapes.dust, 1))
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.HSSS, Materials2Shapes.dust, 9))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Nickel, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Zinc, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.dust, 4))
            .circuit(3)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.FerriteMixture, Materials2Shapes.dust, 6))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Boron, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Glass, Materials2Shapes.dust, 7))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.BorosilicateGlass, Materials2Shapes.dust, 8))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.dust, 23),
                MaterialLibAPI.getStack(Materials2Materials.Nickel, Materials2Shapes.dust, 5),
                MaterialLibAPI.getStack(Materials2Materials.Chrome, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Silicon, Materials2Shapes.dust, 12),
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials2Materials.Aluminium, Materials2Shapes.dust, 1))
            .circuit(20)
            .itemOutputs(MaterialsAlloy.EGLIN_STEEL.getDust(48))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Platinum, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Silver, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Tin, Materials2Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials2Materials.Thaumium, Materials2Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials2Materials.EnderPearl, Materials2Shapes.dust, 2))
            .circuit(16)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Enderium, Materials2Shapes.dust, 8))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Items.rotten_flesh, 1, 0),
                new ItemStack(Items.fermented_spider_eye, 1, 0),
                ItemList.IC2_Scrap.get(1),
                MaterialLibAPI.getStack(Materials2Materials.MeatRaw, Materials2Shapes.dust, 1))
            .itemOutputs(ItemList.Food_Chum.get(4))
            .fluidInputs(getFluidStack("potion.purpledrink", 750))
            .fluidOutputs(getFluidStack("sludge", 1_000))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(24)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Wheat, Materials2Shapes.dust, 1))
            .itemOutputs(ItemList.Food_Dough.get(2))
            .fluidInputs(Materials.Water.getFluid(1_000))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Chili, Materials2Shapes.dust, 1),
                ItemList.Food_PotatoChips.get(1))
            .itemOutputs(ItemList.Food_ChiliChips.get(1))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Redstone, Materials2Shapes.dustTiny, 5),
                MaterialLibAPI.getStack(Materials2Materials.Ruby, Materials2Shapes.dustTiny, 4))
            .itemOutputs(ItemList.IC2_Energium_Dust.get(1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Redstone, Materials2Shapes.dust, 5),
                MaterialLibAPI.getStack(Materials2Materials.Ruby, Materials2Shapes.dust, 4))
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
                MaterialLibAPI.getStack(Materials2Materials.Gold, Materials2Shapes.dustTiny, 1),
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.LiveRoot, Materials2Shapes.dust, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.IronWood, Materials2Shapes.dust, 2))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Gold, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials2Materials.LiveRoot, Materials2Shapes.dust, 9))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.IronWood, Materials2Shapes.dust, 18))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 1),
                MaterialLibAPI.getStack(Materials2Materials.Redstone, Materials2Shapes.dust, 1),
                GTOreDictUnificator.get(OrePrefixes.gem, Materials.NetherQuartz, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Fluix, 2))
            .fluidInputs(Materials.Water.getFluid(500))
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 1),
                MaterialLibAPI.getStack(Materials2Materials.Redstone, Materials2Shapes.dust, 1),
                GTOreDictUnificator.get(OrePrefixes.gem, Materials.NetherQuartz, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Fluix, 2))
            .fluidInputs(GTModHandler.getDistilledWater(500))
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.LightFuel, Materials2CellShapes.cell, 5),
                MaterialLibAPI.getStack(Materials2Materials.HeavyFuel, Materials2CellShapes.cell, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Fuel, Materials2CellShapes.cell, 6))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.LightFuel, Materials2CellShapes.cell, 5),
                Materials.Empty.getCells(1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Fuel, Materials2CellShapes.cell, 6))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.HeavyFuel, Materials2FluidShapes.fluidLiquid, 1_000))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.HeavyFuel, Materials2CellShapes.cell, 1),
                Materials.Empty.getCells(5))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Fuel, Materials2CellShapes.cell, 6))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.LightFuel, Materials2FluidShapes.fluidLiquid, 5_000))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.LightFuel, Materials2CellShapes.cell, 5))
            .circuit(5)
            .itemOutputs(Materials.Empty.getCells(5))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.HeavyFuel, Materials2FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Fuel, Materials2FluidShapes.fluidLiquid, 6_000))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.HeavyFuel, Materials2CellShapes.cell, 1))
            .circuit(6)
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.LightFuel, Materials2FluidShapes.fluidLiquid, 5_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Fuel, Materials2FluidShapes.fluidLiquid, 6_000))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 5),
                MaterialLibAPI.getStack(Materials2Materials.Stone, Materials2Shapes.dust, 1))
            .itemOutputs(Materials.Empty.getCells(5))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, 20))
            .fluidOutputs(new FluidStack(ItemList.sDrillingFluid, 5_000))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Lapis, Materials2Shapes.dust, 1))
            .circuit(4)
            .fluidInputs(Materials.Water.getFluid(125))
            .fluidOutputs(GTModHandler.getIC2Coolant(125))
            .duration(12 * SECONDS + 16 * TICKS)
            .eut(48)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Lapis, Materials2Shapes.dust, 1))
            .circuit(4)
            .fluidInputs(GTModHandler.getDistilledWater(1_000))
            .fluidOutputs(GTModHandler.getIC2Coolant(1_000))
            .duration(12 * SECONDS + 16 * TICKS)
            .eut(48)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Sodium, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Wood, Materials2Shapes.dust, 4))
            .circuit(1)
            .itemOutputs(ItemList.SFMixture.get(4))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.AdvancedGlue, Materials2FluidShapes.fluidLiquid, 200))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Lithium, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Wood, Materials2Shapes.dust, 4))
            .circuit(1)
            .itemOutputs(ItemList.SFMixture.get(8))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.AdvancedGlue, Materials2FluidShapes.fluidLiquid, 200))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Caesium, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Wood, Materials2Shapes.dust, 4))
            .circuit(1)
            .itemOutputs(ItemList.SFMixture.get(12))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.AdvancedGlue, Materials2FluidShapes.fluidLiquid, 200))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        // McGuffium239 is non-renewable and only obtainable though world gen.
        // It's a meme, don't think too deep about it.
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Sodium, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Wood, Materials2Shapes.dust, 4))
            .circuit(1)
            .itemOutputs(ItemList.SFMixture.get(64))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.McGuffium239, Materials2FluidShapes.fluidLiquid, 12))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Lithium, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Wood, Materials2Shapes.dust, 4))
            .circuit(1)
            .itemOutputs(ItemList.SFMixture.get(64))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.McGuffium239, Materials2FluidShapes.fluidLiquid, 8))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Caesium, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Wood, Materials2Shapes.dust, 4))
            .circuit(1)
            .itemOutputs(ItemList.SFMixture.get(64))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.McGuffium239, Materials2FluidShapes.fluidLiquid, 4))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        // Molten Red Granite
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Redstone, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.dust, 1))
            .fluidInputs(Materials.Lava.getFluid(125L))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.GraniteRed, Materials2FluidShapes.fluidMolten, 1 * INGOTS))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);
        // Molten Black Granite
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Stone, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dust, 1))
            .fluidInputs(Materials.Lava.getFluid(125L))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.GraniteBlack, Materials2FluidShapes.fluidMolten, 1 * INGOTS))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.SFMixture.get(2),
                MaterialLibAPI.getStack(Materials2Materials.EnderEye, Materials2Shapes.dust, 1))
            .itemOutputs(ItemList.MSFMixture.get(4))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Mercury, Materials2FluidShapes.fluidLiquid, 1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.SFMixture.get(1), GTOreDictUnificator.get(OrePrefixes.dust, Materials.Blaze, 1))
            .itemOutputs(ItemList.MSFMixture.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Mercury, Materials2FluidShapes.fluidLiquid, 500))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Lignite, 4),
                ItemList.MSFMixture.get(24),
                MaterialLibAPI.getStack(Materials2Materials.Diamond, Materials2Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(ItemList.Block_MSSFUEL.get(4))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.NitroFuel, Materials2FluidShapes.fluidLiquid, 4_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Charcoal, 4),
                ItemList.MSFMixture.get(16),
                MaterialLibAPI.getStack(Materials2Materials.Diamond, Materials2Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(ItemList.Block_MSSFUEL.get(4))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.NitroFuel, Materials2FluidShapes.fluidLiquid, 3_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Coal, 4),
                ItemList.MSFMixture.get(8),
                MaterialLibAPI.getStack(Materials2Materials.Diamond, Materials2Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(ItemList.Block_MSSFUEL.get(4))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.NitroFuel, Materials2FluidShapes.fluidLiquid, 2_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Lignite, 4),
                ItemList.MSFMixture.get(24),
                MaterialLibAPI.getStack(Materials2Materials.Diamond, Materials2Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(ItemList.Block_MSSFUEL.get(4))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HighOctaneGasoline, Materials2FluidShapes.fluidLiquid, 1_600))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Charcoal, 4),
                ItemList.MSFMixture.get(16),
                MaterialLibAPI.getStack(Materials2Materials.Diamond, Materials2Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(ItemList.Block_MSSFUEL.get(4))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HighOctaneGasoline, Materials2FluidShapes.fluidLiquid, 1_200))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Coal, 4),
                ItemList.MSFMixture.get(8),
                MaterialLibAPI.getStack(Materials2Materials.Diamond, Materials2Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(ItemList.Block_MSSFUEL.get(4))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HighOctaneGasoline, Materials2FluidShapes.fluidLiquid, 800))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        if (Thaumcraft.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.SFMixture.get(20),
                    MaterialLibAPI.getStack(Materials2Materials.InfusedAir, Materials2Shapes.dust, 1))
                .circuit(1)
                .itemOutputs(ItemList.MSFMixture.get(20))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(Materials2Materials.FierySteel, Materials2FluidShapes.fluidLiquid, 50))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MV / 2)
                .addTo(mixerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.SFMixture.get(20),
                    MaterialLibAPI.getStack(Materials2Materials.InfusedEarth, Materials2Shapes.dust, 1))
                .circuit(1)
                .itemOutputs(ItemList.MSFMixture.get(20))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(Materials2Materials.FierySteel, Materials2FluidShapes.fluidLiquid, 50))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MV / 2)
                .addTo(mixerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.SFMixture.get(20),
                    MaterialLibAPI.getStack(Materials2Materials.InfusedEntropy, Materials2Shapes.dust, 1))
                .circuit(1)
                .itemOutputs(ItemList.MSFMixture.get(20))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(Materials2Materials.FierySteel, Materials2FluidShapes.fluidLiquid, 50))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MV / 2)
                .addTo(mixerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.SFMixture.get(20),
                    MaterialLibAPI.getStack(Materials2Materials.InfusedFire, Materials2Shapes.dust, 1))
                .circuit(1)
                .itemOutputs(ItemList.MSFMixture.get(20))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(Materials2Materials.FierySteel, Materials2FluidShapes.fluidLiquid, 50))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MV / 2)
                .addTo(mixerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.SFMixture.get(20),
                    MaterialLibAPI.getStack(Materials2Materials.InfusedOrder, Materials2Shapes.dust, 1))
                .circuit(1)
                .itemOutputs(ItemList.MSFMixture.get(20))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(Materials2Materials.FierySteel, Materials2FluidShapes.fluidLiquid, 50))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MV / 2)
                .addTo(mixerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.SFMixture.get(20),
                    MaterialLibAPI.getStack(Materials2Materials.InfusedWater, Materials2Shapes.dust, 1))
                .circuit(1)
                .itemOutputs(ItemList.MSFMixture.get(20))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(Materials2Materials.FierySteel, Materials2FluidShapes.fluidLiquid, 50))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MV / 2)
                .addTo(mixerRecipes);

            FluidStack tFD = getFluidStack("fluiddeath", 30);
            if (tFD != null && tFD.getFluid() != null && tFD.amount > 0) {
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        ItemList.SFMixture.get(30),
                        MaterialLibAPI.getStack(Materials2Materials.InfusedAir, Materials2Shapes.dust, 1))
                    .circuit(1)
                    .itemOutputs(ItemList.MSFMixture.get(30))
                    .fluidInputs(tFD)
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_MV / 2)
                    .addTo(mixerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(
                        ItemList.SFMixture.get(30),
                        MaterialLibAPI.getStack(Materials2Materials.InfusedEarth, Materials2Shapes.dust, 1))
                    .circuit(1)
                    .itemOutputs(ItemList.MSFMixture.get(30))
                    .fluidInputs(tFD)
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_MV / 2)
                    .addTo(mixerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(
                        ItemList.SFMixture.get(30),
                        MaterialLibAPI.getStack(Materials2Materials.InfusedEntropy, Materials2Shapes.dust, 1))
                    .circuit(1)
                    .itemOutputs(ItemList.MSFMixture.get(30))
                    .fluidInputs(tFD)
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_MV / 2)
                    .addTo(mixerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(
                        ItemList.SFMixture.get(30),
                        MaterialLibAPI.getStack(Materials2Materials.InfusedFire, Materials2Shapes.dust, 1))
                    .circuit(1)
                    .itemOutputs(ItemList.MSFMixture.get(30))
                    .fluidInputs(tFD)
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_MV / 2)
                    .addTo(mixerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(
                        ItemList.SFMixture.get(30),
                        MaterialLibAPI.getStack(Materials2Materials.InfusedOrder, Materials2Shapes.dust, 1))
                    .circuit(1)
                    .itemOutputs(ItemList.MSFMixture.get(30))
                    .fluidInputs(tFD)
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_MV / 2)
                    .addTo(mixerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(
                        ItemList.SFMixture.get(30),
                        MaterialLibAPI.getStack(Materials2Materials.InfusedWater, Materials2Shapes.dust, 1))
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
                        MaterialLibAPI
                            .getFluidStack(Materials2Materials.NitroFuel, Materials2FluidShapes.fluidLiquid, 1_000))
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
                            .getFluidStack(Materials2Materials.NitroFuel, Materials2FluidShapes.fluidLiquid, 750))
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
                            .getFluidStack(Materials2Materials.NitroFuel, Materials2FluidShapes.fluidLiquid, 500))
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
                    .fluidInputs(
                        MaterialLibAPI.getFluidStack(
                            Materials2Materials.HighOctaneGasoline,
                            Materials2FluidShapes.fluidLiquid,
                            400))
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
                        MaterialLibAPI.getFluidStack(
                            Materials2Materials.HighOctaneGasoline,
                            Materials2FluidShapes.fluidLiquid,
                            300))
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
                        MaterialLibAPI.getFluidStack(
                            Materials2Materials.HighOctaneGasoline,
                            Materials2FluidShapes.fluidLiquid,
                            200))
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
                MaterialLibAPI.getFluidStack(Materials2Materials.NitroFuel, Materials2FluidShapes.fluidLiquid, 1_000))
            .duration(7 * SECONDS)
            .eut(250)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.block, Materials.Charcoal, 1), ItemList.SFMixture.get(4))
            .circuit(1)
            .itemOutputs(ItemList.Block_SSFUEL.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.NitroFuel, Materials2FluidShapes.fluidLiquid, 750))
            .duration(6 * SECONDS)
            .eut(250)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.block, Materials.Coal, 1), ItemList.SFMixture.get(2))
            .circuit(1)
            .itemOutputs(ItemList.Block_SSFUEL.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.NitroFuel, Materials2FluidShapes.fluidLiquid, 500))
            .duration(5 * SECONDS)
            .eut(250)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.block, Materials.Lignite, 1), ItemList.SFMixture.get(6))
            .circuit(1)
            .itemOutputs(ItemList.Block_SSFUEL.get(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HighOctaneGasoline, Materials2FluidShapes.fluidLiquid, 400))
            .duration(7 * SECONDS)
            .eut(250)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.block, Materials.Charcoal, 1), ItemList.SFMixture.get(4))
            .circuit(1)
            .itemOutputs(ItemList.Block_SSFUEL.get(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HighOctaneGasoline, Materials2FluidShapes.fluidLiquid, 300))
            .duration(6 * SECONDS)
            .eut(250)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.block, Materials.Coal, 1), ItemList.SFMixture.get(2))
            .circuit(1)
            .itemOutputs(ItemList.Block_SSFUEL.get(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HighOctaneGasoline, Materials2FluidShapes.fluidLiquid, 200))
            .duration(5 * SECONDS)
            .eut(250)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.SulfuricAcid, Materials2CellShapes.cell, 1))
            .circuit(1)
            .itemOutputs(ItemList.Cell_Empty.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.NitricAcid, Materials2FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(new FluidStack(ItemList.sNitrationMixture, 2_000))
            .duration(24 * SECONDS)
            .eut(2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.SulfuricAcid, Materials2CellShapes.cell, 1),
                MaterialLibAPI.getStack(Materials2Materials.NitricAcid, Materials2CellShapes.cell, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.NitrationMixture, Materials2CellShapes.cell, 2))
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
                MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, 4))
            .circuit(3)
            .itemOutputs(getModItem(Forestry.ID, "fertilizerBio", 1L, 0))
            .fluidInputs(Materials.Water.getFluid(100))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        // radiation manufacturing

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.SluiceSand, Materials2Shapes.dust, 1))
            .fluidInputs(Materials.Water.getFluid(500))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.SluiceJuice, Materials2FluidShapes.fluidLiquid, 1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        // NaCl + H2O = (NaCl·H2O)

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Salt, Materials2Shapes.dust, 2))
            .circuit(3)
            .fluidInputs(Materials.Water.getFluid(1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.SaltWater, Materials2FluidShapes.fluidLiquid, 1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        // CaCO3 + 2 CH3COOH = Ca(CH3COO)2 + H2O + CO2

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Calcite, Materials2Shapes.dust, 5),
                Materials.Empty.getCells(2))
            .circuit(1)
            .itemOutputs(
                Materials.Water.getCells(1),
                MaterialLibAPI.getStack(Materials2Materials.CarbonDioxide, Materials2CellShapes.cell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.AceticAcid, Materials2FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.CalciumAcetateSolution,
                    Materials2FluidShapes.fluidLiquid,
                    1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Calcite, Materials2Shapes.dust, 5),
                Materials.Empty.getCells(2))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.CalciumAcetateSolution, Materials2CellShapes.cell, 1),
                MaterialLibAPI.getStack(Materials2Materials.CarbonDioxide, Materials2CellShapes.cell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.AceticAcid, Materials2FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(Materials.Water.getFluid(1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Calcite, Materials2Shapes.dust, 5),
                Materials.Empty.getCells(2))
            .circuit(21)
            .itemOutputs(
                Materials.Water.getCells(1),
                MaterialLibAPI.getStack(Materials2Materials.CalciumAcetateSolution, Materials2CellShapes.cell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.AceticAcid, Materials2FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.fluidGas, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Calcite, Materials2Shapes.dust, 5),
                Materials.Empty.getCells(1))
            .circuit(4)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.CarbonDioxide, Materials2CellShapes.cell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.AceticAcid, Materials2FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.CalciumAcetateSolution,
                    Materials2FluidShapes.fluidLiquid,
                    1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Calcite, Materials2Shapes.dust, 5),
                Materials.Empty.getCells(1))
            .circuit(14)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.CalciumAcetateSolution, Materials2CellShapes.cell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.AceticAcid, Materials2FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.fluidGas, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        // Ca + 2 CH3COOH = Ca(CH3COO)2 + 2H

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Calcium, Materials2Shapes.dust, 1),
                Materials.Empty.getCells(2))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.cell, 2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.AceticAcid, Materials2FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.CalciumAcetateSolution,
                    Materials2FluidShapes.fluidLiquid,
                    1_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Calcium, Materials2Shapes.dust, 1),
                Materials.Empty.getCells(1))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.CalciumAcetateSolution, Materials2CellShapes.cell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.AceticAcid, Materials2FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.fluidGas, 2_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        // CaO + 2 CH3COOH = Ca(CH3COO)2 + H2O

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Quicklime, Materials2Shapes.dust, 2),
                Materials.Empty.getCells(1))
            .circuit(1)
            .itemOutputs(Materials.Water.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.AceticAcid, Materials2FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.CalciumAcetateSolution,
                    Materials2FluidShapes.fluidLiquid,
                    1_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Quicklime, Materials2Shapes.dust, 2),
                Materials.Empty.getCells(1))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.CalciumAcetateSolution, Materials2CellShapes.cell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.AceticAcid, Materials2FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(Materials.Water.getFluid(1_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        // 2CH3COOCH3 + 3CH3COCH3/(C4H6O2)n = 5Glue

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Acetone, Materials2CellShapes.cell, 3))
            .itemOutputs(Materials.Empty.getCells(3))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.PolyvinylAcetate, Materials2FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.AdvancedGlue, Materials2FluidShapes.fluidLiquid, 5_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.PolyvinylAcetate, Materials2CellShapes.cell, 2))
            .itemOutputs(Materials.Empty.getCells(2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Acetone, Materials2FluidShapes.fluidLiquid, 3_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.AdvancedGlue, Materials2FluidShapes.fluidLiquid, 5_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.MethylAcetate, Materials2CellShapes.cell, 3))
            .itemOutputs(Materials.Empty.getCells(3))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.PolyvinylAcetate, Materials2FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.AdvancedGlue, Materials2FluidShapes.fluidLiquid, 5_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.PolyvinylAcetate, Materials2CellShapes.cell, 2))
            .itemOutputs(Materials.Empty.getCells(2))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.MethylAcetate, Materials2FluidShapes.fluidLiquid, 3_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.AdvancedGlue, Materials2FluidShapes.fluidLiquid, 5_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Sugar.getDust(4))
            .itemOutputs(Materials.Charcoal.getGems(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.DilutedSulfuricAcid, Materials2FluidShapes.fluidLiquid, 1_000))
            .duration(60 * SECONDS)
            .eut(2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Wood, Materials2Shapes.dust, 4))
            .itemOutputs(Materials.Charcoal.getGems(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.DilutedSulfuricAcid, Materials2FluidShapes.fluidLiquid, 1_000))
            .duration(60 * SECONDS)
            .eut(2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Fuel, Materials2CellShapes.cell, 1))
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Tetranitromethane, Materials2FluidShapes.fluidLiquid, 20))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.NitroFuel, Materials2FluidShapes.fluidLiquid, 1_000))
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.BioDiesel, Materials2CellShapes.cell, 1))
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Tetranitromethane, Materials2FluidShapes.fluidLiquid, 40))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.NitroFuel, Materials2FluidShapes.fluidLiquid, 900))
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        // CH4O + C4H8 = C5H12O

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Methanol, Materials2CellShapes.cell, 1),
                MaterialLibAPI.getStack(Materials2Materials.Butene, Materials2CellShapes.cell, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.MTBEReactionMixtureButene, Materials2CellShapes.cell, 1),
                Materials.Empty.getCells(1))
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Methanol, Materials2CellShapes.cell, 1),
                MaterialLibAPI.getStack(Materials2Materials.Butane, Materials2CellShapes.cell, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.MTBEReactionMixtureButane, Materials2CellShapes.cell, 1),
                Materials.Empty.getCells(1))
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Naphtha, Materials2CellShapes.cell, 16),
                MaterialLibAPI.getStack(Materials2Materials.Gas, Materials2CellShapes.cell, 2),
                MaterialLibAPI.getStack(Materials2Materials.Methanol, Materials2CellShapes.cell, 1),
                MaterialLibAPI.getStack(Materials2Materials.Acetone, Materials2CellShapes.cell, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.RawGasoline, Materials2CellShapes.cell, 20))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Gasoline, Materials2CellShapes.cell, 20),
                MaterialLibAPI.getStack(Materials2Materials.Octane, Materials2CellShapes.cell, 2),
                MaterialLibAPI.getStack(Materials2Materials.NitrousOxide, Materials2CellShapes.cell, 6),
                MaterialLibAPI.getStack(Materials2Materials.Toluene, Materials2CellShapes.cell, 1))
            .itemOutputs(Materials.Empty.getCells(29))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.EthylTertButylEther, Materials2FluidShapes.fluidLiquid, 3_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HighOctaneGasoline, Materials2FluidShapes.fluidLiquid, 32_000))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerRecipes);

        if (Railcraft.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(EnumCube.COKE_BLOCK.getItem(), ItemList.SFMixture.get(2))
                .circuit(1)
                .itemOutputs(ItemList.Block_SSFUEL.get(1))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(Materials2Materials.NitroFuel, Materials2FluidShapes.fluidLiquid, 300))
                .duration(5 * SECONDS)
                .eut(250)
                .addTo(mixerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    EnumCube.COKE_BLOCK.getItem(4),
                    ItemList.SFMixture.get(8),
                    MaterialLibAPI.getStack(Materials2Materials.Diamond, Materials2Shapes.dust, 1))
                .circuit(2)
                .itemOutputs(ItemList.Block_SSFUEL.get(4))
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.HighOctaneGasoline, Materials2FluidShapes.fluidLiquid, 480))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(mixerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(EnumCube.COKE_BLOCK.getItem(), ItemList.SFMixture.get(2))
                .circuit(1)
                .itemOutputs(ItemList.Block_SSFUEL.get(1))
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.HighOctaneGasoline, Materials2FluidShapes.fluidLiquid, 120))
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
                    MaterialLibAPI.getFluidStack(Materials2Materials.NitroFuel, Materials2FluidShapes.fluidLiquid, 300))
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
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.HighOctaneGasoline, Materials2FluidShapes.fluidLiquid, 120))
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
                MaterialLibAPI.getStack(Materials2Materials.Cocoa, Materials2Shapes.dust, 1))
            .itemOutputs(ItemList.Food_Dough_Chocolate.get(2L))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                new OreDictItemStack("foodDough", 1),
                MaterialLibAPI.getStack(Materials2Materials.Chocolate, Materials2Shapes.dust, 1))
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
                MaterialLibAPI.getStack(Materials2Materials.NaquadahEnriched, Materials2Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials2Materials.Holmium, Materials2Shapes.dust, 1))
            .circuit(4)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.EnrichedHolmium, Materials2Shapes.dust, 5))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.NetherStar, Materials2Shapes.dust, 8),
                MaterialLibAPI.getStack(Materials2Materials.Terbium, Materials2Shapes.dust, 7),
                MaterialsElements.getInstance().TECHNETIUM.getDust(4),
                MaterialLibAPI.getStack(Materials2Materials.Unstable, Materials2Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials2Materials.FleroviumGT5U, Materials2Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials2Materials.InfinityCatalyst, Materials2Shapes.dust, 1))
            .circuit(6)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Shijima, Materials2Shapes.dust, 27))
            .duration(15 * SECONDS)
            .eut((int) TierEU.RECIPE_ZPM)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.TinAlloy, Materials2Shapes.dust, 8),
                WerkstoffLoader.Ruridit.get(OrePrefixes.dust, 7),
                MaterialsAlloy.TRINIUM_NAQUADAH.getDust(4),
                GGMaterial.adamantiumAlloy.get(OrePrefixes.dust, 4),
                WerkstoffLoader.Californium.get(OrePrefixes.dust, 3),
                MaterialsAlloy.QUANTUM.getDust(1))
            .circuit(6)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Churitsu, Materials2Shapes.dust, 27))
            .duration(15 * SECONDS)
            .eut((int) TierEU.RECIPE_ZPM)
            .addTo(mixerRecipes);

        // Catalysts for Plasma Forge.
        {
            GTValues.RA.stdBuilder()
                .circuit(9)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(Materials2Materials.Helium, Materials2FluidShapes.fluidPlasma, 1_000),
                    MaterialLibAPI.getFluidStack(Materials2Materials.Iron, Materials2FluidShapes.fluidPlasma, 1_000),
                    MaterialLibAPI.getFluidStack(Materials2Materials.Calcium, Materials2FluidShapes.fluidPlasma, 1_000),
                    MaterialLibAPI.getFluidStack(Materials2Materials.Niobium, Materials2FluidShapes.fluidPlasma, 1_000))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.DimensionallyTranscendentCrudeCatalyst,
                        Materials2FluidShapes.fluidLiquid,
                        1_000))
                .duration(41 * MINUTES + 40 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .addTo(mixerNonCellRecipes);

            GTValues.RA.stdBuilder()
                .circuit(10)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.DimensionallyTranscendentCrudeCatalyst,
                        Materials2FluidShapes.fluidLiquid,
                        1_000),
                    MaterialLibAPI.getFluidStack(Materials2Materials.Radon, Materials2FluidShapes.fluidPlasma, 1_000),
                    MaterialLibAPI.getFluidStack(Materials2Materials.Nickel, Materials2FluidShapes.fluidPlasma, 1_000),
                    MaterialLibAPI.getFluidStack(Materials2Materials.Boron, Materials2FluidShapes.fluidPlasma, 1_000),
                    MaterialLibAPI.getFluidStack(Materials2Materials.Sulfur, Materials2FluidShapes.fluidPlasma, 1_000))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.DimensionallyTranscendentProsaicCatalyst,
                        Materials2FluidShapes.fluidLiquid,
                        1_000))
                .duration(41 * MINUTES + 40 * SECONDS)
                .eut(TierEU.RECIPE_UV)
                .addTo(mixerNonCellRecipes);

            GTValues.RA.stdBuilder()
                .circuit(11)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.DimensionallyTranscendentProsaicCatalyst,
                        Materials2FluidShapes.fluidLiquid,
                        1_000),
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Nitrogen, Materials2FluidShapes.fluidPlasma, 1_000),
                    MaterialLibAPI.getFluidStack(Materials2Materials.Zinc, Materials2FluidShapes.fluidPlasma, 1_000),
                    MaterialLibAPI.getFluidStack(Materials2Materials.Silver, Materials2FluidShapes.fluidPlasma, 1_000),
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Titanium, Materials2FluidShapes.fluidPlasma, 1_000))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.DimensionallyTranscendentResplendentCatalyst,
                        Materials2FluidShapes.fluidLiquid,
                        1_000))
                .duration(41 * MINUTES + 40 * SECONDS)
                .eut(TierEU.RECIPE_UHV)
                .addTo(mixerNonCellRecipes);

            GTValues.RA.stdBuilder()
                .circuit(12)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.DimensionallyTranscendentResplendentCatalyst,
                        Materials2FluidShapes.fluidLiquid,
                        1_000),
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Americium, Materials2FluidShapes.fluidPlasma, 1_000),
                    MaterialLibAPI.getFluidStack(Materials2Materials.Bismuth, Materials2FluidShapes.fluidPlasma, 1_000),
                    MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.fluidPlasma, 1_000),
                    MaterialLibAPI.getFluidStack(Materials2Materials.Tin, Materials2FluidShapes.fluidPlasma, 1_000))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.DimensionallyTranscendentExoticCatalyst,
                        Materials2FluidShapes.fluidLiquid,
                        1_000))
                .duration(41 * MINUTES + 40 * SECONDS)
                .eut(TierEU.RECIPE_UEV)
                .addTo(mixerNonCellRecipes);

            GTValues.RA.stdBuilder()
                .circuit(13)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.DimensionallyTranscendentExoticCatalyst,
                        Materials2FluidShapes.fluidLiquid,
                        1_000),
                    MaterialLibAPI.getFluidStack(Materials2Materials.Lead, Materials2FluidShapes.fluidPlasma, 1_000),
                    MaterialLibAPI.getFluidStack(Materials2Materials.Thorium, Materials2FluidShapes.fluidPlasma, 1_000),
                    MaterialLibAPI.getFluidStack(Materials2Materials.Naquadria, Materials2FluidShapes.fluidPlasma, 100),
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.RawStarMatter, Materials2FluidShapes.fluidLiquid, 25))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.DimensionallyTranscendentStellarCatalyst,
                        Materials2FluidShapes.fluidLiquid,
                        1_000))
                .duration(41 * MINUTES + 40 * SECONDS)
                .eut(TierEU.RECIPE_UIV)
                .addTo(mixerNonCellRecipes);
        }

        if (Mods.CropsNH.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Mods.CropsNH.ID, "weedEX", 1))
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.NaphthenicAcid, Materials2FluidShapes.fluidLiquid, 10))
                .fluidOutputs(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.WeedEX9000, Materials2FluidShapes.fluidLiquid, 750))
                .duration(5 * SECONDS)
                .eut(100)
                .addTo(mixerRecipes);
        }

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Tritanium, Materials2Shapes.dust, 11),
                GGMaterial.orundum.get(OrePrefixes.dust, 8),
                MaterialLibAPI.getStack(Materials2Materials.Rubidium, Materials2Shapes.dust, 11),
                MaterialLibAPI.getStack(Materials2Materials.FierySteel, Materials2Shapes.dust, 7),
                MaterialLibAPI.getStack(Materials2Materials.Firestone, Materials2Shapes.dust, 13),
                GGMaterial.atomicSeparationCatalyst.get(OrePrefixes.dust, 13))
            .circuit(6)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Mellion, Materials2Shapes.dust, 63))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DimensionallyTranscendentResidue,
                    Materials2FluidShapes.fluidLiquid,
                    5_000))
            .duration(15 * SECONDS)
            .eut((int) TierEU.RECIPE_UMV)
            .addTo(mixerRecipes);
    }
}
