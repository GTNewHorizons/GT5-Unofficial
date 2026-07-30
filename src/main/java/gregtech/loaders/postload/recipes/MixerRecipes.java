package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.AppliedEnergistics2;
import static gregtech.api.enums.Mods.BiomesOPlenty;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.IndustrialCraft2;
import static gregtech.api.enums.Mods.Natura;
import static gregtech.api.enums.Mods.PamsHarvestCraft;
import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.recipe.RecipeMaps.mixerNonCellRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static net.minecraftforge.fluids.FluidRegistry.getFluidStack;

import java.util.Locale;

import gregtech.api.enums.materials2.Materials;
import gregtech.api.enums.materials2.Shapes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2CellShapes;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.objects.OreDictItemStack;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.loaders.materials.RecognitionMaterials;
import mods.railcraft.common.blocks.aesthetics.cube.EnumCube;

@SuppressWarnings({ "PointlessArithmeticExpression" })
public class MixerRecipes implements Runnable {

    @Override
    public void run() {

        registerSingleBlockAndMulti();

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.EnderPearl, Shapes.dust, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Blaze, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.EnderEye, Shapes.dust, 1))
            .duration(5 * SECONDS)
            .eut(48)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Gold, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Silver, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Electrum, Shapes.dust, 2))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.Nickel, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Invar, Shapes.dust, 3))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Tin, Shapes.dust, 1))
            .circuit(5)
            .itemOutputs(MaterialLibAPI.getStack(Materials.TinAlloy, Shapes.dust, 2))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials.Invar, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.Manganese, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Chrome, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.StainlessSteel, Shapes.dust, 9))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Aluminium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Chrome, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Kanthal, Shapes.dust, 3))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Copper, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.Zinc, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Brass, Shapes.dust, 4))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Copper, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.Tin, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Bronze, Shapes.dust, 4))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Copper, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Nickel, Shapes.dust, 1))
            .circuit(3)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Cupronickel, Shapes.dust, 2))
            .duration(10 * SECONDS)
            .eut(24)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Copper, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Silver, Shapes.dust, 4))
            .circuit(4)
            .itemOutputs(MaterialLibAPI.getStack(Materials.SterlingSilver, Shapes.dust, 5))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Copper, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.Electrum, Shapes.dust, 2))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.BlackBronze, Shapes.dust, 5))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Bismuth, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Brass, Shapes.dust, 4))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.BismuthBronze, Shapes.dust, 5))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.BlackBronze, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Nickel, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Steel, Shapes.dust, 3))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.BlackSteel, Shapes.dust, 5))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SterlingSilver, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.BismuthBronze, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.BlackSteel, Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials.Steel, Shapes.dust, 2))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.RedSteel, Shapes.dust, 8))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.RoseGold, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Brass, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.BlackSteel, Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials.Steel, Shapes.dust, 2))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.BlueSteel, Shapes.dust, 8))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Copper, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.Gold, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Silver, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Nickel, Shapes.dust, 5),
                MaterialLibAPI.getStack(Materials.Steel, Shapes.dust, 15))
            .circuit(14)
            .itemOutputs(MaterialLibAPI.getStack(Materials.BlackSteel, Shapes.dust, 25))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Copper, Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials.Silver, Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials.Bismuth, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Zinc, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.BlackSteel, Shapes.dust, 20),
                MaterialLibAPI.getStack(Materials.Steel, Shapes.dust, 10))
            .circuit(15)
            .itemOutputs(MaterialLibAPI.getStack(Materials.RedSteel, Shapes.dust, 40))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Copper, Shapes.dust, 19),
                MaterialLibAPI.getStack(Materials.Gold, Shapes.dust, 16),
                MaterialLibAPI.getStack(Materials.Zinc, Shapes.dust, 5),
                MaterialLibAPI.getStack(Materials.BlackSteel, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.BlackSteel, Shapes.dust, 16),
                MaterialLibAPI.getStack(Materials.Steel, Shapes.dust, 40))
            .circuit(16)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.BlueSteel, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.BlueSteel, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.BlueSteel, Shapes.dust, 32))
            .duration(3 * MINUTES)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Cobalt, Shapes.dust, 5),
                MaterialLibAPI.getStack(Materials.Chrome, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.Nickel, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Molybdenum, Shapes.dust, 1))
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Ultimet, Shapes.dust, 9))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Brass, Shapes.dust, 7),
                MaterialLibAPI.getStack(Materials.Tin, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Cobalt, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.CobaltBrass, Shapes.dust, 9))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Saltpeter, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Coal, Shapes.dust, 3))
            .circuit(1)
            .itemOutputs(
                GTOreDictUnificator.getDust(Materials.Gunpowder, 6L * OrePrefixes.dust.getMaterialAmount()))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Saltpeter, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Charcoal, Shapes.dust, 3))
            .circuit(1)
            .itemOutputs(
                GTOreDictUnificator.getDust(Materials.Gunpowder, 6L * OrePrefixes.dust.getMaterialAmount()))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Saltpeter, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, 3))
            .circuit(1)
            .itemOutputs(
                GTOreDictUnificator.getDust(Materials.Gunpowder, 6L * OrePrefixes.dust.getMaterialAmount()))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Indium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Gallium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Phosphorus, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.IndiumGalliumPhosphide, Shapes.dust, 3))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Brick, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Clay, Shapes.dust, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Fireclay, Shapes.dust, 2))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Nickel, Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials.Chrome, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Nichrome, Shapes.dust, 5))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Osmium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Iridium, Shapes.dust, 3))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Osmiridium, Shapes.dust, 4))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Niobium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Titanium, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.NiobiumTitanium, Shapes.dust, 2))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Vanadium, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.Gallium, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.VanadiumGallium, Shapes.dust, 4))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Tungsten, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.TungstenCarbide, Shapes.dust, 2))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Tungsten, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Steel, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.dust, 2))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Titanium, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.Platinum, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.Vanadium, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.TPVAlloy, Shapes.dust, 7))
            .duration(8 * SECONDS + 15 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.dust, 5),
                MaterialLibAPI.getStack(Materials.Chrome, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Molybdenum, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.Vanadium, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.HSSG, Shapes.dust, 9))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.HSSG, Shapes.dust, 6),
                MaterialLibAPI.getStack(Materials.Cobalt, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Manganese, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Silicon, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.HSSE, Shapes.dust, 9))
            .duration(35 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.HSSG, Shapes.dust, 6),
                MaterialLibAPI.getStack(Materials.Iridium, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.Osmium, Shapes.dust, 1))
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials.HSSS, Shapes.dust, 9))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Nickel, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Zinc, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, 4))
            .circuit(3)
            .itemOutputs(MaterialLibAPI.getStack(Materials.FerriteMixture, Shapes.dust, 6))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Boron, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Glass, Shapes.dust, 7))
            .itemOutputs(MaterialLibAPI.getStack(Materials.BorosilicateGlass, Shapes.dust, 8))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, 23),
                MaterialLibAPI.getStack(Materials.Nickel, Shapes.dust, 5),
                MaterialLibAPI.getStack(Materials.Chrome, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Silicon, Shapes.dust, 12),
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.Aluminium, Shapes.dust, 1))
            .circuit(20)
            .itemOutputs(MaterialLibAPI.getStack(Materials.EglinSteel, Shapes.dust, 48))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Platinum, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Silver, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Tin, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.Thaumium, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.EnderPearl, Shapes.dust, 2))
            .circuit(16)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Enderium, Shapes.dust, 8))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Items.rotten_flesh, 1, 0),
                new ItemStack(Items.fermented_spider_eye, 1, 0),
                ItemList.IC2_Scrap.get(1),
                MaterialLibAPI.getStack(Materials.MeatRaw, Shapes.dust, 1))
            .itemOutputs(ItemList.Food_Chum.get(4))
            .fluidInputs(getFluidStack("potion.purpledrink", 750))
            .fluidOutputs(getFluidStack("sludge", 1_000))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(24)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Wheat, Shapes.dust, 1))
            .itemOutputs(ItemList.Food_Dough.get(2))
            .fluidInputs(GTUtility.getWater(1_000))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Chili, Shapes.dust, 1),
                ItemList.Food_PotatoChips.get(1))
            .itemOutputs(ItemList.Food_ChiliChips.get(1))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Redstone, Shapes.dustTiny, 5),
                MaterialLibAPI.getStack(Materials.Ruby, Shapes.dustTiny, 4))
            .itemOutputs(ItemList.IC2_Energium_Dust.get(1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Redstone, Shapes.dust, 5),
                MaterialLibAPI.getStack(Materials.Ruby, Shapes.dust, 4))
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
                MaterialLibAPI.getStack(Materials.Gold, Shapes.dustTiny, 1),
                MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.LiveRoot, Shapes.dust, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.IronWood, Shapes.dust, 2))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Gold, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials.LiveRoot, Shapes.dust, 9))
            .itemOutputs(MaterialLibAPI.getStack(Materials.IronWood, Shapes.dust, 18))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 1),
                MaterialLibAPI.getStack(Materials.Redstone, Shapes.dust, 1),
                GTOreDictUnificator.get(OrePrefixes.gem, Materials.NetherQuartz, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, RecognitionMaterials.Fluix, 2))
            .fluidInputs(GTUtility.getWater(500))
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 1),
                MaterialLibAPI.getStack(Materials.Redstone, Shapes.dust, 1),
                GTOreDictUnificator.get(OrePrefixes.gem, Materials.NetherQuartz, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, RecognitionMaterials.Fluix, 2))
            .fluidInputs(GTModHandler.getDistilledWater(500))
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.LightFuel, Materials2CellShapes.cell, 5),
                MaterialLibAPI.getStack(Materials.HeavyFuel, Materials2CellShapes.cell, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Fuel, Materials2CellShapes.cell, 6))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.LightFuel, Materials2CellShapes.cell, 5),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Fuel, Materials2CellShapes.cell, 6))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.HeavyFuel, Materials2FluidShapes.fluidLiquid, 1_000))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.HeavyFuel, Materials2CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 5))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Fuel, Materials2CellShapes.cell, 6))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.LightFuel, Materials2FluidShapes.fluidLiquid, 5_000))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.LightFuel, Materials2CellShapes.cell, 5))
            .circuit(5)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 5))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.HeavyFuel, Materials2FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Fuel, Materials2FluidShapes.fluidLiquid, 6_000))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.HeavyFuel, Materials2CellShapes.cell, 1))
            .circuit(6)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.LightFuel, Materials2FluidShapes.fluidLiquid, 5_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Fuel, Materials2FluidShapes.fluidLiquid, 6_000))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 5),
                MaterialLibAPI.getStack(Materials.Stone, Shapes.dust, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 5))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Lubricant, Materials2FluidShapes.fluidLiquid, 20))
            .fluidOutputs(new FluidStack(ItemList.sDrillingFluid, 5_000))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Lapis, Shapes.dust, 1))
            .circuit(4)
            .fluidInputs(GTUtility.getWater(125))
            .fluidOutputs(GTModHandler.getIC2Coolant(125))
            .duration(12 * SECONDS + 16 * TICKS)
            .eut(48)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Lapis, Shapes.dust, 1))
            .circuit(4)
            .fluidInputs(GTModHandler.getDistilledWater(1_000))
            .fluidOutputs(GTModHandler.getIC2Coolant(1_000))
            .duration(12 * SECONDS + 16 * TICKS)
            .eut(48)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Sodium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Wood, Shapes.dust, 4))
            .circuit(1)
            .itemOutputs(ItemList.SFMixture.get(4))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.AdvancedGlue, Materials2FluidShapes.fluidLiquid, 200))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Lithium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Wood, Shapes.dust, 4))
            .circuit(1)
            .itemOutputs(ItemList.SFMixture.get(8))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.AdvancedGlue, Materials2FluidShapes.fluidLiquid, 200))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Caesium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Wood, Shapes.dust, 4))
            .circuit(1)
            .itemOutputs(ItemList.SFMixture.get(12))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.AdvancedGlue, Materials2FluidShapes.fluidLiquid, 200))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        // McGuffium239 is non-renewable and only obtainable though world gen.
        // It's a meme, don't think too deep about it.
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Sodium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Wood, Shapes.dust, 4))
            .circuit(1)
            .itemOutputs(ItemList.SFMixture.get(64))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.McGuffium239, Materials2FluidShapes.fluidLiquid, 12))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Lithium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Wood, Shapes.dust, 4))
            .circuit(1)
            .itemOutputs(ItemList.SFMixture.get(64))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.McGuffium239, Materials2FluidShapes.fluidLiquid, 8))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Caesium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Wood, Shapes.dust, 4))
            .circuit(1)
            .itemOutputs(ItemList.SFMixture.get(64))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.McGuffium239, Materials2FluidShapes.fluidLiquid, 4))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        // Molten Red Granite
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Redstone, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 1))
            .fluidInputs(GTUtility.getLava(125L))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.GraniteRed, Materials2FluidShapes.fluidMolten, 1 * INGOTS))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);
        // Molten Black Granite
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Stone, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, 1))
            .fluidInputs(GTUtility.getLava(125L))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.GraniteBlack, Materials2FluidShapes.fluidMolten, 1 * INGOTS))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.SFMixture.get(2),
                MaterialLibAPI.getStack(Materials.EnderEye, Shapes.dust, 1))
            .itemOutputs(ItemList.MSFMixture.get(4))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Mercury, Materials2FluidShapes.fluidLiquid, 1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.SFMixture.get(1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Blaze, 1))
            .itemOutputs(ItemList.MSFMixture.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Mercury, Materials2FluidShapes.fluidLiquid, 500))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Lignite, 4),
                ItemList.MSFMixture.get(24),
                MaterialLibAPI.getStack(Materials.Diamond, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(ItemList.Block_MSSFUEL.get(4))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.NitroFuel, Materials2FluidShapes.fluidLiquid, 4_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Charcoal, 4),
                ItemList.MSFMixture.get(16),
                MaterialLibAPI.getStack(Materials.Diamond, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(ItemList.Block_MSSFUEL.get(4))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.NitroFuel, Materials2FluidShapes.fluidLiquid, 3_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Coal, 4),
                ItemList.MSFMixture.get(8),
                MaterialLibAPI.getStack(Materials.Diamond, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(ItemList.Block_MSSFUEL.get(4))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.NitroFuel, Materials2FluidShapes.fluidLiquid, 2_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Lignite, 4),
                ItemList.MSFMixture.get(24),
                MaterialLibAPI.getStack(Materials.Diamond, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(ItemList.Block_MSSFUEL.get(4))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.HighOctaneGasoline, Materials2FluidShapes.fluidLiquid, 1_600))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Charcoal, 4),
                ItemList.MSFMixture.get(16),
                MaterialLibAPI.getStack(Materials.Diamond, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(ItemList.Block_MSSFUEL.get(4))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.HighOctaneGasoline, Materials2FluidShapes.fluidLiquid, 1_200))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Coal, 4),
                ItemList.MSFMixture.get(8),
                MaterialLibAPI.getStack(Materials.Diamond, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(ItemList.Block_MSSFUEL.get(4))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.HighOctaneGasoline, Materials2FluidShapes.fluidLiquid, 800))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        if (Thaumcraft.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.SFMixture.get(20),
                    MaterialLibAPI.getStack(Materials.InfusedAir, Shapes.dust, 1))
                .circuit(1)
                .itemOutputs(ItemList.MSFMixture.get(20))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(Materials.FierySteel, Materials2FluidShapes.fluidLiquid, 50))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MV / 2)
                .addTo(mixerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.SFMixture.get(20),
                    MaterialLibAPI.getStack(Materials.InfusedEarth, Shapes.dust, 1))
                .circuit(1)
                .itemOutputs(ItemList.MSFMixture.get(20))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(Materials.FierySteel, Materials2FluidShapes.fluidLiquid, 50))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MV / 2)
                .addTo(mixerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.SFMixture.get(20),
                    MaterialLibAPI.getStack(Materials.InfusedEntropy, Shapes.dust, 1))
                .circuit(1)
                .itemOutputs(ItemList.MSFMixture.get(20))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(Materials.FierySteel, Materials2FluidShapes.fluidLiquid, 50))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MV / 2)
                .addTo(mixerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.SFMixture.get(20),
                    MaterialLibAPI.getStack(Materials.InfusedFire, Shapes.dust, 1))
                .circuit(1)
                .itemOutputs(ItemList.MSFMixture.get(20))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(Materials.FierySteel, Materials2FluidShapes.fluidLiquid, 50))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MV / 2)
                .addTo(mixerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.SFMixture.get(20),
                    MaterialLibAPI.getStack(Materials.InfusedOrder, Shapes.dust, 1))
                .circuit(1)
                .itemOutputs(ItemList.MSFMixture.get(20))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(Materials.FierySteel, Materials2FluidShapes.fluidLiquid, 50))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MV / 2)
                .addTo(mixerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.SFMixture.get(20),
                    MaterialLibAPI.getStack(Materials.InfusedWater, Shapes.dust, 1))
                .circuit(1)
                .itemOutputs(ItemList.MSFMixture.get(20))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(Materials.FierySteel, Materials2FluidShapes.fluidLiquid, 50))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MV / 2)
                .addTo(mixerRecipes);

            FluidStack tFD = getFluidStack("fluiddeath", 30);
            if (tFD != null && tFD.getFluid() != null && tFD.amount > 0) {
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        ItemList.SFMixture.get(30),
                        MaterialLibAPI.getStack(Materials.InfusedAir, Shapes.dust, 1))
                    .circuit(1)
                    .itemOutputs(ItemList.MSFMixture.get(30))
                    .fluidInputs(tFD)
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_MV / 2)
                    .addTo(mixerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(
                        ItemList.SFMixture.get(30),
                        MaterialLibAPI.getStack(Materials.InfusedEarth, Shapes.dust, 1))
                    .circuit(1)
                    .itemOutputs(ItemList.MSFMixture.get(30))
                    .fluidInputs(tFD)
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_MV / 2)
                    .addTo(mixerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(
                        ItemList.SFMixture.get(30),
                        MaterialLibAPI.getStack(Materials.InfusedEntropy, Shapes.dust, 1))
                    .circuit(1)
                    .itemOutputs(ItemList.MSFMixture.get(30))
                    .fluidInputs(tFD)
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_MV / 2)
                    .addTo(mixerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(
                        ItemList.SFMixture.get(30),
                        MaterialLibAPI.getStack(Materials.InfusedFire, Shapes.dust, 1))
                    .circuit(1)
                    .itemOutputs(ItemList.MSFMixture.get(30))
                    .fluidInputs(tFD)
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_MV / 2)
                    .addTo(mixerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(
                        ItemList.SFMixture.get(30),
                        MaterialLibAPI.getStack(Materials.InfusedOrder, Shapes.dust, 1))
                    .circuit(1)
                    .itemOutputs(ItemList.MSFMixture.get(30))
                    .fluidInputs(tFD)
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_MV / 2)
                    .addTo(mixerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(
                        ItemList.SFMixture.get(30),
                        MaterialLibAPI.getStack(Materials.InfusedWater, Shapes.dust, 1))
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
                            .getFluidStack(Materials.NitroFuel, Materials2FluidShapes.fluidLiquid, 1_000))
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
                            .getFluidStack(Materials.NitroFuel, Materials2FluidShapes.fluidLiquid, 750))
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
                            .getFluidStack(Materials.NitroFuel, Materials2FluidShapes.fluidLiquid, 500))
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
                            Materials.HighOctaneGasoline,
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
                            Materials.HighOctaneGasoline,
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
                            Materials.HighOctaneGasoline,
                            Materials2FluidShapes.fluidLiquid,
                            200))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_HV)
                    .addTo(mixerRecipes);
            }
        }

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Lignite, 1),
                ItemList.SFMixture.get(6))
            .circuit(1)
            .itemOutputs(ItemList.Block_SSFUEL.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.NitroFuel, Materials2FluidShapes.fluidLiquid, 1_000))
            .duration(7 * SECONDS)
            .eut(250)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Charcoal, 1),
                ItemList.SFMixture.get(4))
            .circuit(1)
            .itemOutputs(ItemList.Block_SSFUEL.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.NitroFuel, Materials2FluidShapes.fluidLiquid, 750))
            .duration(6 * SECONDS)
            .eut(250)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Coal, 1),
                ItemList.SFMixture.get(2))
            .circuit(1)
            .itemOutputs(ItemList.Block_SSFUEL.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.NitroFuel, Materials2FluidShapes.fluidLiquid, 500))
            .duration(5 * SECONDS)
            .eut(250)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Lignite, 1),
                ItemList.SFMixture.get(6))
            .circuit(1)
            .itemOutputs(ItemList.Block_SSFUEL.get(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.HighOctaneGasoline, Materials2FluidShapes.fluidLiquid, 400))
            .duration(7 * SECONDS)
            .eut(250)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Charcoal, 1),
                ItemList.SFMixture.get(4))
            .circuit(1)
            .itemOutputs(ItemList.Block_SSFUEL.get(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.HighOctaneGasoline, Materials2FluidShapes.fluidLiquid, 300))
            .duration(6 * SECONDS)
            .eut(250)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Coal, 1),
                ItemList.SFMixture.get(2))
            .circuit(1)
            .itemOutputs(ItemList.Block_SSFUEL.get(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.HighOctaneGasoline, Materials2FluidShapes.fluidLiquid, 200))
            .duration(5 * SECONDS)
            .eut(250)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SulfuricAcid, Materials2CellShapes.cell, 1))
            .circuit(1)
            .itemOutputs(ItemList.Cell_Empty.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.NitricAcid, Materials2FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(new FluidStack(ItemList.sNitrationMixture, 2_000))
            .duration(24 * SECONDS)
            .eut(2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SulfuricAcid, Materials2CellShapes.cell, 1),
                MaterialLibAPI.getStack(Materials.NitricAcid, Materials2CellShapes.cell, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.NitrationMixture, Materials2CellShapes.cell, 2))
            .duration(24 * SECONDS)
            .eut(2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.dirt, 1, 32767), new ItemStack(Items.wheat, 4, 32767))
            .circuit(2)
            .itemOutputs(getModItem(Forestry.ID, "fertilizerBio", 1L, 0))
            .fluidInputs(GTUtility.getWater(100))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.dirt, 1, 32767), getModItem(BiomesOPlenty.ID, "plants", 4, 6))
            .circuit(2)
            .itemOutputs(getModItem(Forestry.ID, "fertilizerBio", 1L, 0))
            .fluidInputs(GTUtility.getWater(100))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.dirt, 1, 32767), getModItem(PamsHarvestCraft.ID, "oatsItem", 4))
            .circuit(2)
            .itemOutputs(getModItem(Forestry.ID, "fertilizerBio", 1L, 0))
            .fluidInputs(GTUtility.getWater(100))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.dirt, 1, 32767), getModItem(PamsHarvestCraft.ID, "ryeItem", 4))
            .circuit(2)
            .itemOutputs(getModItem(Forestry.ID, "fertilizerBio", 1L, 0))
            .fluidInputs(GTUtility.getWater(100))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.dirt, 1, 32767), getModItem(PamsHarvestCraft.ID, "barleyItem", 4))
            .circuit(2)
            .itemOutputs(getModItem(Forestry.ID, "fertilizerBio", 1L, 0))
            .fluidInputs(GTUtility.getWater(100))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.dirt, 1, 32767), getModItem(Natura.ID, "barleyFood", 4))
            .circuit(2)
            .itemOutputs(getModItem(Forestry.ID, "fertilizerBio", 1L, 0))
            .fluidInputs(GTUtility.getWater(100))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.dirt, 1, 32767),
                MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, 4))
            .circuit(3)
            .itemOutputs(getModItem(Forestry.ID, "fertilizerBio", 1L, 0))
            .fluidInputs(GTUtility.getWater(100))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        // radiation manufacturing

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SluiceSand, Shapes.dust, 1))
            .fluidInputs(GTUtility.getWater(500))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.SluiceJuice, Materials2FluidShapes.fluidLiquid, 1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        // NaCl + H2O = (NaCl·H2O)

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Salt, Shapes.dust, 2))
            .circuit(3)
            .fluidInputs(GTUtility.getWater(1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.SaltWater, Materials2FluidShapes.fluidLiquid, 1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        // CaCO3 + 2 CH3COOH = Ca(CH3COO)2 + H2O + CO2

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Calcite, Shapes.dust, 5),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .circuit(1)
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1),
                MaterialLibAPI.getStack(Materials.CarbonDioxide, Materials2CellShapes.cell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.AceticAcid, Materials2FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.CalciumAcetateSolution,
                    Materials2FluidShapes.fluidLiquid,
                    1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Calcite, Shapes.dust, 5),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.CalciumAcetateSolution, Materials2CellShapes.cell, 1),
                MaterialLibAPI.getStack(Materials.CarbonDioxide, Materials2CellShapes.cell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.AceticAcid, Materials2FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(GTUtility.getWater(1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Calcite, Shapes.dust, 5),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .circuit(21)
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1),
                MaterialLibAPI.getStack(Materials.CalciumAcetateSolution, Materials2CellShapes.cell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.AceticAcid, Materials2FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.CarbonDioxide, Materials2FluidShapes.fluidGas, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Calcite, Shapes.dust, 5),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .circuit(4)
            .itemOutputs(MaterialLibAPI.getStack(Materials.CarbonDioxide, Materials2CellShapes.cell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.AceticAcid, Materials2FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.CalciumAcetateSolution,
                    Materials2FluidShapes.fluidLiquid,
                    1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Calcite, Shapes.dust, 5),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .circuit(14)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.CalciumAcetateSolution, Materials2CellShapes.cell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.AceticAcid, Materials2FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.CarbonDioxide, Materials2FluidShapes.fluidGas, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        // Ca + 2 CH3COOH = Ca(CH3COO)2 + 2H

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Calcium, Shapes.dust, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Hydrogen, Materials2CellShapes.cell, 2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.AceticAcid, Materials2FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.CalciumAcetateSolution,
                    Materials2FluidShapes.fluidLiquid,
                    1_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Calcium, Shapes.dust, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.CalciumAcetateSolution, Materials2CellShapes.cell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.AceticAcid, Materials2FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, Materials2FluidShapes.fluidGas, 2_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        // CaO + 2 CH3COOH = Ca(CH3COO)2 + H2O

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Quicklime, Shapes.dust, 2),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.AceticAcid, Materials2FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.CalciumAcetateSolution,
                    Materials2FluidShapes.fluidLiquid,
                    1_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Quicklime, Shapes.dust, 2),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.CalciumAcetateSolution, Materials2CellShapes.cell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.AceticAcid, Materials2FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(GTUtility.getWater(1_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(mixerRecipes);

        // 2CH3COOCH3 + 3CH3COCH3/(C4H6O2)n = 5Glue

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Acetone, Materials2CellShapes.cell, 3))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 3))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.PolyvinylAcetate, Materials2FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.AdvancedGlue, Materials2FluidShapes.fluidLiquid, 5_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.PolyvinylAcetate, Materials2CellShapes.cell, 2))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Acetone, Materials2FluidShapes.fluidLiquid, 3_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.AdvancedGlue, Materials2FluidShapes.fluidLiquid, 5_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.MethylAcetate, Materials2CellShapes.cell, 3))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 3))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.PolyvinylAcetate, Materials2FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.AdvancedGlue, Materials2FluidShapes.fluidLiquid, 5_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.PolyvinylAcetate, Materials2CellShapes.cell, 2))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.MethylAcetate, Materials2FluidShapes.fluidLiquid, 3_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.AdvancedGlue, Materials2FluidShapes.fluidLiquid, 5_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 4))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.SulfuricAcid, Materials2FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.DilutedSulfuricAcid, Materials2FluidShapes.fluidLiquid, 1_000))
            .duration(60 * SECONDS)
            .eut(2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Wood, Shapes.dust, 4))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.SulfuricAcid, Materials2FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.DilutedSulfuricAcid, Materials2FluidShapes.fluidLiquid, 1_000))
            .duration(60 * SECONDS)
            .eut(2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Fuel, Materials2CellShapes.cell, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Tetranitromethane, Materials2FluidShapes.fluidLiquid, 20))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.NitroFuel, Materials2FluidShapes.fluidLiquid, 1_000))
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.BioDiesel, Materials2CellShapes.cell, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Tetranitromethane, Materials2FluidShapes.fluidLiquid, 40))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.NitroFuel, Materials2FluidShapes.fluidLiquid, 900))
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        // CH4O + C4H8 = C5H12O

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Methanol, Materials2CellShapes.cell, 1),
                MaterialLibAPI.getStack(Materials.Butene, Materials2CellShapes.cell, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.MTBEReactionMixtureButene, Materials2CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Methanol, Materials2CellShapes.cell, 1),
                MaterialLibAPI.getStack(Materials.Butane, Materials2CellShapes.cell, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.MTBEReactionMixtureButane, Materials2CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Naphtha, Materials2CellShapes.cell, 16),
                MaterialLibAPI.getStack(Materials.Gas, Materials2CellShapes.cell, 2),
                MaterialLibAPI.getStack(Materials.Methanol, Materials2CellShapes.cell, 1),
                MaterialLibAPI.getStack(Materials.Acetone, Materials2CellShapes.cell, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.RawGasoline, Materials2CellShapes.cell, 20))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Gasoline, Materials2CellShapes.cell, 20),
                MaterialLibAPI.getStack(Materials.Octane, Materials2CellShapes.cell, 2),
                MaterialLibAPI.getStack(Materials.NitrousOxide, Materials2CellShapes.cell, 6),
                MaterialLibAPI.getStack(Materials.Toluene, Materials2CellShapes.cell, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 29))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.EthylTertButylEther, Materials2FluidShapes.fluidLiquid, 3_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.HighOctaneGasoline, Materials2FluidShapes.fluidLiquid, 32_000))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerRecipes);

        if (Railcraft.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(EnumCube.COKE_BLOCK.getItem(), ItemList.SFMixture.get(2))
                .circuit(1)
                .itemOutputs(ItemList.Block_SSFUEL.get(1))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(Materials.NitroFuel, Materials2FluidShapes.fluidLiquid, 300))
                .duration(5 * SECONDS)
                .eut(250)
                .addTo(mixerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    EnumCube.COKE_BLOCK.getItem(4),
                    ItemList.SFMixture.get(8),
                    MaterialLibAPI.getStack(Materials.Diamond, Shapes.dust, 1))
                .circuit(2)
                .itemOutputs(ItemList.Block_SSFUEL.get(4))
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials.HighOctaneGasoline, Materials2FluidShapes.fluidLiquid, 480))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(mixerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(EnumCube.COKE_BLOCK.getItem(), ItemList.SFMixture.get(2))
                .circuit(1)
                .itemOutputs(ItemList.Block_SSFUEL.get(1))
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials.HighOctaneGasoline, Materials2FluidShapes.fluidLiquid, 120))
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
                    MaterialLibAPI.getFluidStack(Materials.NitroFuel, Materials2FluidShapes.fluidLiquid, 300))
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
                        .getFluidStack(Materials.HighOctaneGasoline, Materials2FluidShapes.fluidLiquid, 120))
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
                MaterialLibAPI.getStack(Materials.Cocoa, Shapes.dust, 1))
            .itemOutputs(ItemList.Food_Dough_Chocolate.get(2L))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                new OreDictItemStack("foodDough", 1),
                MaterialLibAPI.getStack(Materials.Chocolate, Shapes.dust, 1))
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
                MaterialLibAPI.getStack(Materials.NaquadahEnriched, Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials.Holmium, Shapes.dust, 1))
            .circuit(4)
            .itemOutputs(MaterialLibAPI.getStack(Materials.EnrichedHolmium, Shapes.dust, 5))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.NetherStar, Shapes.dust, 8),
                MaterialLibAPI.getStack(Materials.Terbium, Shapes.dust, 7),
                MaterialLibAPI.getStack(Materials.Technetium, Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials.Unstable, Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials.FleroviumGT5U, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.InfinityCatalyst, Shapes.dust, 1))
            .circuit(6)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Shijima, Shapes.dust, 27))
            .duration(15 * SECONDS)
            .eut((int) TierEU.RECIPE_ZPM)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.TinAlloy, Shapes.dust, 8),
                MaterialLibAPI.getStack(Materials.Ruridit, Shapes.dust, 7),
                MaterialLibAPI.getStack(Materials.TriniumNaquadahAlloy, Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials.AdamantiumAlloy, Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials.Californium, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.Quantum, Shapes.dust, 1))
            .circuit(6)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Churitsu, Shapes.dust, 27))
            .duration(15 * SECONDS)
            .eut((int) TierEU.RECIPE_ZPM)
            .addTo(mixerRecipes);

        // Catalysts for Plasma Forge.
        {
            GTValues.RA.stdBuilder()
                .circuit(9)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(Materials.Helium, Materials2FluidShapes.fluidPlasma, 1_000),
                    MaterialLibAPI.getFluidStack(Materials.Iron, Materials2FluidShapes.fluidPlasma, 1_000),
                    MaterialLibAPI.getFluidStack(Materials.Calcium, Materials2FluidShapes.fluidPlasma, 1_000),
                    MaterialLibAPI.getFluidStack(Materials.Niobium, Materials2FluidShapes.fluidPlasma, 1_000))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.DimensionallyTranscendentCrudeCatalyst,
                        Materials2FluidShapes.fluidLiquid,
                        1_000))
                .duration(41 * MINUTES + 40 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .addTo(mixerNonCellRecipes);

            GTValues.RA.stdBuilder()
                .circuit(10)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.DimensionallyTranscendentCrudeCatalyst,
                        Materials2FluidShapes.fluidLiquid,
                        1_000),
                    MaterialLibAPI.getFluidStack(Materials.Radon, Materials2FluidShapes.fluidPlasma, 1_000),
                    MaterialLibAPI.getFluidStack(Materials.Nickel, Materials2FluidShapes.fluidPlasma, 1_000),
                    MaterialLibAPI.getFluidStack(Materials.Boron, Materials2FluidShapes.fluidPlasma, 1_000),
                    MaterialLibAPI.getFluidStack(Materials.Sulfur, Materials2FluidShapes.fluidPlasma, 1_000))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.DimensionallyTranscendentProsaicCatalyst,
                        Materials2FluidShapes.fluidLiquid,
                        1_000))
                .duration(41 * MINUTES + 40 * SECONDS)
                .eut(TierEU.RECIPE_UV)
                .addTo(mixerNonCellRecipes);

            GTValues.RA.stdBuilder()
                .circuit(11)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.DimensionallyTranscendentProsaicCatalyst,
                        Materials2FluidShapes.fluidLiquid,
                        1_000),
                    MaterialLibAPI
                        .getFluidStack(Materials.Nitrogen, Materials2FluidShapes.fluidPlasma, 1_000),
                    MaterialLibAPI.getFluidStack(Materials.Zinc, Materials2FluidShapes.fluidPlasma, 1_000),
                    MaterialLibAPI.getFluidStack(Materials.Silver, Materials2FluidShapes.fluidPlasma, 1_000),
                    MaterialLibAPI
                        .getFluidStack(Materials.Titanium, Materials2FluidShapes.fluidPlasma, 1_000))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.DimensionallyTranscendentResplendentCatalyst,
                        Materials2FluidShapes.fluidLiquid,
                        1_000))
                .duration(41 * MINUTES + 40 * SECONDS)
                .eut(TierEU.RECIPE_UHV)
                .addTo(mixerNonCellRecipes);

            GTValues.RA.stdBuilder()
                .circuit(12)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.DimensionallyTranscendentResplendentCatalyst,
                        Materials2FluidShapes.fluidLiquid,
                        1_000),
                    MaterialLibAPI
                        .getFluidStack(Materials.Americium, Materials2FluidShapes.fluidPlasma, 1_000),
                    MaterialLibAPI.getFluidStack(Materials.Bismuth, Materials2FluidShapes.fluidPlasma, 1_000),
                    MaterialLibAPI.getFluidStack(Materials.Oxygen, Materials2FluidShapes.fluidPlasma, 1_000),
                    MaterialLibAPI.getFluidStack(Materials.Tin, Materials2FluidShapes.fluidPlasma, 1_000))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.DimensionallyTranscendentExoticCatalyst,
                        Materials2FluidShapes.fluidLiquid,
                        1_000))
                .duration(41 * MINUTES + 40 * SECONDS)
                .eut(TierEU.RECIPE_UEV)
                .addTo(mixerNonCellRecipes);

            GTValues.RA.stdBuilder()
                .circuit(13)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.DimensionallyTranscendentExoticCatalyst,
                        Materials2FluidShapes.fluidLiquid,
                        1_000),
                    MaterialLibAPI.getFluidStack(Materials.Lead, Materials2FluidShapes.fluidPlasma, 1_000),
                    MaterialLibAPI.getFluidStack(Materials.Thorium, Materials2FluidShapes.fluidPlasma, 1_000),
                    MaterialLibAPI.getFluidStack(Materials.Naquadria, Materials2FluidShapes.fluidPlasma, 100),
                    MaterialLibAPI
                        .getFluidStack(Materials.RawStarMatter, Materials2FluidShapes.fluidLiquid, 25))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.DimensionallyTranscendentStellarCatalyst,
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
                        .getFluidStack(Materials.NaphthenicAcid, Materials2FluidShapes.fluidLiquid, 10))
                .fluidOutputs(
                    MaterialLibAPI
                        .getFluidStack(Materials.WeedEX9000, Materials2FluidShapes.fluidLiquid, 750))
                .duration(5 * SECONDS)
                .eut(100)
                .addTo(mixerRecipes);
        }

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Tritanium, Shapes.dust, 11),
                MaterialLibAPI.getStack(Materials.Orundum, Shapes.dust, 8),
                MaterialLibAPI.getStack(Materials.Rubidium, Shapes.dust, 11),
                MaterialLibAPI.getStack(Materials.FierySteel, Shapes.dust, 7),
                MaterialLibAPI.getStack(Materials.Firestone, Shapes.dust, 13),
                MaterialLibAPI.getStack(Materials.AtomicSeparationCatalyst, Shapes.dust, 13))
            .circuit(6)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Mellion, Shapes.dust, 63))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.DimensionallyTranscendentResidue,
                    Materials2FluidShapes.fluidLiquid,
                    5_000))
            .duration(15 * SECONDS)
            .eut((int) TierEU.RECIPE_UMV)
            .addTo(mixerRecipes);
    }
}
