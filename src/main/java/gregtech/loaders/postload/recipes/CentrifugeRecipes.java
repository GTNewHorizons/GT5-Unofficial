package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.AppliedEnergistics2;
import static gregtech.api.enums.Mods.ExtraUtilities;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.enums.Mods.ThaumicTinkerer;
import static gregtech.api.enums.Mods.TwilightForest;
import static gregtech.api.recipe.RecipeMaps.centrifugeNonCellRecipes;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.loaders.misc.GTBees.combs;
import static net.minecraftforge.fluids.FluidRegistry.getFluidStack;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.CellShapes;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.MaterialParts;
import gregtech.api.objects.OreDictItemStack;
import gregtech.api.recipe.metadata.CentrifugeRecipeKey;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

@SuppressWarnings({ "PointlessArithmeticExpression" })
public class CentrifugeRecipes implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(new OreDictItemStack("logWood", 1))
            .circuit(1)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 60))
            .duration(10 * SECONDS)
            .eut(20)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new OreDictItemStack("logRubber", 1))
            .circuit(2)
            .itemOutputs(
                ItemList.IC2_Resin.get(1L),
                ItemList.IC2_Plantball.get(1L),
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Wood, Shapes.dust, 1))
            .outputChances(5000, 3750, 2500, 2500)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 60))
            .duration(10 * SECONDS)
            .eut(20)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.InfusedGold, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Gold, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Gold, Shapes.dust, 1),
                getModItem(Thaumcraft.ID, "ItemResource", 2L, 14))
            .outputChances(10000, 10000, 9000)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Mercury, FluidShapes.fluidLiquid, 200))
            .duration(20 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Empty.get(1))
            .itemOutputs(MaterialParts.requireCell(Materials.Oxygen, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Air, FluidShapes.fluidGas, 10_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Nitrogen, FluidShapes.fluidGas, 3_900))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.PlatinumGroupSludge, Shapes.dust, 3))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.Gold, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.PlatinumMetallicPowder, Shapes.dust, 6),
                MaterialLibAPI.getStack(Materials.PalladiumMetallicPowder, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.IridiumMetalResidue, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.RarestMetalResidue, Shapes.dust, 2))
            .outputChances(10000, 10000, 10000, 9500, 9000, 8500)
            .duration(2 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Stone, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Copper, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Tin, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Nickel, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Antimony, Shapes.dust, 1))
            .outputChances(10000, 4000, 2000, 2000, 2000, 2000)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SluiceJuice, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(GTUtility.getWater(500))
            .duration(2 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .addTo(centrifugeRecipes);

        // food ->CH4

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.golden_apple, 1, 1))
            .circuit(1)
            .itemOutputs(new ItemStack(Items.gold_ingot, 64))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 4608))
            .duration(7 * MINUTES + 40 * SECONDS + 16 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.golden_apple, 1, 0))
            .circuit(1)
            .itemOutputs(new ItemStack(Items.gold_ingot, 7))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 576))
            .duration(7 * MINUTES + 40 * SECONDS + 16 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.golden_carrot, 1, 0))
            .circuit(1)
            .itemOutputs(new ItemStack(Items.gold_nugget, 6))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 576))
            .duration(7 * MINUTES + 40 * SECONDS + 16 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.speckled_melon, 1, 0))
            .circuit(1)
            .itemOutputs(new ItemStack(Items.gold_nugget, 6))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 576))
            .duration(7 * MINUTES + 40 * SECONDS + 16 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.mushroom_stew, 1, 0))
            .circuit(1)
            .itemOutputs(new ItemStack(Items.bowl, 1, 0))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 36))
            .duration(14 * SECONDS + 8 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.apple, 1, 0))
            .circuit(1)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 18))
            .duration(7 * SECONDS + 4 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.bread, 1, 0))
            .circuit(1)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 9))
            .duration(3 * SECONDS + 12 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.porkchop, 1, 0))
            .circuit(1)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 48))
            .duration(19 * SECONDS + 4 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.cooked_porkchop, 1, 0))
            .circuit(1)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 36))
            .duration(14 * SECONDS + 8 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.beef, 1, 0))
            .circuit(1)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 48))
            .duration(19 * SECONDS + 4 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.cooked_beef, 1, 0))
            .circuit(1)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 36))
            .duration(14 * SECONDS + 8 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.fish, 1, 32767))
            .circuit(1)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 48))
            .duration(19 * SECONDS + 4 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.cooked_fished, 1, 32767))
            .circuit(1)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 36))
            .duration(14 * SECONDS + 8 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.chicken, 1, 0))
            .circuit(1)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 48))
            .duration(19 * SECONDS + 4 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.cooked_chicken, 1, 0))
            .circuit(1)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 36))
            .duration(14 * SECONDS + 8 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.melon, 1, 0))
            .circuit(1)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 9))
            .duration(3 * SECONDS + 12 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.pumpkin, 1, 0))
            .circuit(1)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 36))
            .duration(14 * SECONDS + 8 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.rotten_flesh, 1, 0))
            .circuit(1)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 36))
            .duration(14 * SECONDS + 8 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.spider_eye, 1, 0))
            .circuit(1)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 18))
            .duration(7 * SECONDS + 4 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.carrot, 1, 0))
            .circuit(1)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 36))
            .duration(14 * SECONDS + 8 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Food_Raw_Potato.get(1))
            .circuit(1)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 36))
            .duration(14 * SECONDS + 8 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Food_Poisonous_Potato.get(1))
            .circuit(1)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 48))
            .duration(19 * SECONDS + 4 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Food_Baked_Potato.get(24))
            .circuit(1)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 576))
            .duration(3 * MINUTES + 50 * SECONDS + 8 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.cookie, 1, 0))
            .circuit(1)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 9))
            .duration(3 * SECONDS + 12 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.cake, 1, 0))
            .circuit(1)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 72))
            .duration(28 * SECONDS + 16 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.brown_mushroom_block, 1, 32767))
            .circuit(1)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 48))
            .duration(19 * SECONDS + 4 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.red_mushroom_block, 1, 32767))
            .circuit(1)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 48))
            .duration(19 * SECONDS + 4 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.brown_mushroom, 1, 32767))
            .circuit(1)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 18))
            .duration(7 * SECONDS + 4 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.red_mushroom, 1, 32767))
            .circuit(1)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 18))
            .duration(7 * SECONDS + 4 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.nether_wart, 1, 32767))
            .circuit(1)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 18))
            .duration(7 * SECONDS + 4 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(TwilightForest.ID, "item.meefRaw", 1L, 32767))
            .circuit(1)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 48))
            .duration(19 * SECONDS + 4 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(TwilightForest.ID, "item.meefSteak", 1L, 32767))
            .circuit(1)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 36))
            .duration(14 * SECONDS + 8 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(TwilightForest.ID, "item.venisonRaw", 1L, 32767))
            .circuit(1)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 48))
            .duration(19 * SECONDS + 4 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(TwilightForest.ID, "item.venisonCooked", 1L, 32767))
            .circuit(1)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 36))
            .duration(14 * SECONDS + 8 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.sand, 1, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Diamond, Shapes.dustTiny, 1),
                new ItemStack(Blocks.sand, 1))
            .outputChances(5000, 100, 5000)
            .duration(30 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.dirt, 1, 32767))
            .itemOutputs(
                ItemList.IC2_Plantball.get(1),
                MaterialLibAPI.getStack(Materials.Clay, Shapes.dustTiny, 1),
                new ItemStack(Blocks.sand, 1))
            .outputChances(1250, 5000, 5000)
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.grass, 1, 32767))
            .itemOutputs(
                ItemList.IC2_Plantball.get(1),
                MaterialLibAPI.getStack(Materials.Clay, Shapes.dustTiny, 1),
                new ItemStack(Blocks.sand, 1))
            .outputChances(2500, 5000, 5000)
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.mycelium, 1, 32767))
            .itemOutputs(
                new ItemStack(Blocks.brown_mushroom, 1),
                new ItemStack(Blocks.red_mushroom, 1),
                MaterialLibAPI.getStack(Materials.Clay, Shapes.dustTiny, 1),
                new ItemStack(Blocks.sand, 1))
            .outputChances(2500, 2500, 5000, 5000)
            .duration(32 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.IC2_Resin.get(1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.RawRubber, Shapes.dust, 3), ItemList.IC2_Plantball.get(1))
            .outputChances(10000, 1000)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Glue, FluidShapes.fluidLiquid, 100))
            .duration(15 * SECONDS)
            .eut(5)
            .addTo(centrifugeRecipes);

        if (!Mods.NuclearHorizons.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(Materials.Uranium, Shapes.dust, 1))
                .circuit(10)
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials.Uranium235, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Plutonium, Shapes.dust, 1))
                .outputChances(1000, 500)
                .duration(1 * MINUTES + 40 * SECONDS)
                .eut((int) TierEU.RECIPE_HV)
                .addTo(centrifugeRecipes);

            // Uranium Enrichment in Centrifuge by adding Fluorine (Uranium Hexafluoride)

            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(Materials.Uranium, Shapes.dust, 1))
                .itemOutputs(MaterialLibAPI.getStack(Materials.Uranium235, Shapes.dust, 1))
                .outputChances(10000)
                .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Fluorine, FluidShapes.fluidGas, 4_000))
                .duration(3 * MINUTES + 20 * SECONDS)
                .eut((int) TierEU.RECIPE_EV)
                .addTo(centrifugeRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(Materials.Plutonium, Shapes.dust, 1))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials.Plutonium241, Shapes.dustTiny, 1),
                    MaterialLibAPI.getStack(Materials.Uranium, Shapes.dustTiny, 1))
                .outputChances(2000, 3000)
                .duration(1 * MINUTES + 20 * SECONDS)
                .eut(320)
                .addTo(centrifugeRecipes);
        }

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dust, 9))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.EnrichedNaquadahOxideMixture, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.NaquadriaOxideMixture, Shapes.dust, 2))
            .outputChances(5000, 1000)
            .duration(24 * MINUTES)
            .eut(320)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.NaquadahEnriched, Shapes.dust, 4))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.NaquadriaOxideMixture, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.NaquadahOxideMixture, Shapes.dust, 2))
            .outputChances(2000, 3000)
            .duration(21 * MINUTES + 20 * SECONDS)
            .eut(640)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 160))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Deuterium, FluidShapes.fluidGas, 40))
            .duration(8 * SECONDS)
            .eut(20)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Deuterium, FluidShapes.fluidGas, 160))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Tritium, FluidShapes.fluidGas, 40))
            .duration(8 * SECONDS)
            .eut(80)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidGas, 80))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Helium3, FluidShapes.fluidGas, 5))
            .duration(8 * SECONDS)
            .eut(80)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Glowstone, Shapes.dust, 2))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Redstone, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Gold, Shapes.dust, 1))
            .duration(48 * SECONDS + 16 * TICKS)
            .eut(80)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Endstone, Shapes.dust, 36))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Tungstate, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.PlatinumMetallicPowder, Shapes.dust, 2),
                new ItemStack(Blocks.sand, 36))
            .outputChances(3750, 2500, 9000)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidGas, 4_320))
            .duration(9 * MINUTES + 36 * SECONDS)
            .eut(20)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Netherrack, Shapes.dust, 36))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Redstone, Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials.Coal, Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials.Gold, Shapes.dust, 1))
            .outputChances(5625, 9900, 5625, 2500)
            .duration(4 * MINUTES + 48 * SECONDS)
            .eut(20)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .circuit(10)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Magnesia, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Quicklime, Shapes.dust, 1),
                GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Gold, 4),
                MaterialLibAPI.getStack(Materials.Sapphire, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Tantalite, Shapes.dust, 1))
            .outputChances(5000, 1000, 1000, 250, 1250, 500)
            .fluidInputs(GTUtility.getLava(400))
            .duration(16 * SECONDS)
            .eut(80)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .circuit(20)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 5),
                MaterialLibAPI.getStack(Materials.Magnesia, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Quicklime, Shapes.dust, 1),
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Gold, 1),
                MaterialLibAPI.getStack(Materials.Sapphire, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.Tantalite, Shapes.dust, 1))
            .outputChances(9000, 9000, 9000, 1000, 3750, 4500)
            .fluidInputs(GTUtility.getLava(3_600))
            .duration(2 * MINUTES + 24 * SECONDS)
            .eut(80)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.soul_sand, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Saltpeter, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Coal, Shapes.dust, 1),
                new ItemStack(Blocks.sand, 1))
            .outputChances(1000, 700, 9000)
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Oil, FluidShapes.fluidLiquid, 200))
            .duration(10 * SECONDS)
            .eut(12)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .circuit(10)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Copper, Shapes.nugget, 1),
                MaterialLibAPI.getStack(Materials.Tin, Shapes.nugget, 1),
                MaterialLibAPI.getStack(Materials.Silver, Shapes.nugget, 1),
                MaterialLibAPI.getStack(Materials.Phosphorus, Shapes.dustSmall, 1),
                MaterialLibAPI.getStack(Materials.Scheelite, Shapes.dustSmall, 1),
                MaterialLibAPI.getStack(Materials.Bauxite, Shapes.dustSmall, 1))
            .outputChances(2000, 1000, 250, 50, 250, 500)
            .fluidInputs(getFluidStack("ic2pahoehoelava", 100))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_EV / 2)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .circuit(20)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Copper, Shapes.ingot, 1),
                MaterialLibAPI.getStack(Materials.Tin, Shapes.ingot, 1),
                MaterialLibAPI.getStack(Materials.Silver, Shapes.ingot, 1),
                MaterialLibAPI.getStack(Materials.Phosphorus, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Scheelite, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Bauxite, Shapes.dust, 1))
            .outputChances(8000, 4000, 1000, 450, 2250, 4500)
            .fluidInputs(getFluidStack("ic2pahoehoelava", 3_600))
            .duration(16 * SECONDS + 8 * TICKS)
            .eut(TierEU.RECIPE_IV / 2)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 45))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.BasalticMineralSand, Shapes.dustSmall, 1),
                MaterialLibAPI.getStack(Materials.Olivine, Shapes.dustSmall, 1),
                MaterialLibAPI.getStack(Materials.Obsidian, Shapes.dustSmall, 1),
                MaterialLibAPI.getStack(Materials.Basalt, Shapes.dustSmall, 1),
                MaterialLibAPI.getStack(Materials.Flint, Shapes.dustSmall, 1),
                MaterialLibAPI.getStack(Materials.RareEarth, Shapes.dustSmall, 1))
            .outputChances(2000, 2000, 2000, 2000, 2000, 2000)
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(20)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 36L, 45))
            .circuit(2)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.BasalticMineralSand, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.Olivine, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.Obsidian, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.Basalt, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.Flint, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.RareEarth, Shapes.dust, 2))
            .outputChances(9000, 9000, 9000, 9000, 9000, 9000)
            .duration(25 * SECONDS + 18 * TICKS)
            .eut(80)
            .addTo(centrifugeRecipes);

        // Ash centrifuge recipes

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, 36))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Quicklime, Shapes.dust, 18),
                MaterialLibAPI.getStack(Materials.Potash, Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials.Magnesia, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.PhosphorousPentoxide, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.SodaAsh, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.BandedIron, Shapes.dust, 4))
            .outputChances(6400, 6000, 4500, 10000, 10000, 10000)
            .duration(5 * MINUTES)
            .eut(TierEU.RECIPE_LV)
            .addTo(centrifugeRecipes);

        // Stone Dust and Metal Mixture centrifuge recipes

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Stone, Shapes.dust, 36))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Quartzite, Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials.PotassiumFeldspar, Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials.Marble, Shapes.dust, 8),
                MaterialLibAPI.getStack(Materials.Biotite, Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials.MetalMixture, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.Sodalite, Shapes.dust, 2))
            .outputChances(10000, 10000, 10000, 10000, 10000, 10000)
            .duration(7 * MINUTES + 12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.MetalMixture, Shapes.dust, 36))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.BandedIron, Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials.Bauxite, Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials.Pyrolusite, Shapes.dust, 8),
                MaterialLibAPI.getStack(Materials.Barite, Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials.Chromite, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.Ilmenite, Shapes.dust, 2))
            .outputChances(10000, 10000, 10000, 10000, 10000, 10000)
            .duration(10 * MINUTES + 56 * SECONDS + 5 * TICKS)
            .eut((int) TierEU.RECIPE_EV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Propane, FluidShapes.fluidGas, 320))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.LPG, FluidShapes.fluidLiquid, 290))
            .duration(20 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Butane, FluidShapes.fluidGas, 320))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.LPG, FluidShapes.fluidLiquid, 370))
            .duration(20 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.DarkAsh, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, 1))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.magma_cream, 1))
            .itemOutputs(new ItemStack(Items.blaze_powder, 1), new ItemStack(Items.slime_ball, 1))
            .duration(25 * SECONDS)
            .eut(5)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.gunpowder, 6))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Saltpeter, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Charcoal, Shapes.dust, 3))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(centrifugeRecipes);

        if (Thaumcraft.isModLoaded() && Forestry.isModLoaded()) {
            // air

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(combs, 1, 144))
                .circuit(1)
                .itemOutputs(MaterialLibAPI.getStack(Materials.InfusedAir, Shapes.gem, 1))
                .duration(51 * SECONDS + 4 * TICKS)
                .eut(12)
                .addTo(centrifugeRecipes);

            // fire

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(combs, 1, 146))
                .circuit(1)
                .itemOutputs(MaterialLibAPI.getStack(Materials.InfusedFire, Shapes.gem, 1))
                .duration(51 * SECONDS + 4 * TICKS)
                .eut(12)
                .addTo(centrifugeRecipes);

            // aqua

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(combs, 1, 147))
                .circuit(1)
                .itemOutputs(MaterialLibAPI.getStack(Materials.InfusedWater, Shapes.gem, 1))
                .duration(51 * SECONDS + 4 * TICKS)
                .eut(12)
                .addTo(centrifugeRecipes);

            // terra

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(combs, 1, 145))
                .circuit(1)
                .itemOutputs(MaterialLibAPI.getStack(Materials.InfusedEarth, Shapes.gem, 1))
                .duration(51 * SECONDS + 4 * TICKS)
                .eut(12)
                .addTo(centrifugeRecipes);

            // ordo

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(combs, 1, 148))
                .circuit(1)
                .itemOutputs(MaterialLibAPI.getStack(Materials.InfusedOrder, Shapes.gem, 1))
                .duration(51 * SECONDS + 4 * TICKS)
                .eut(12)
                .addTo(centrifugeRecipes);

            // perditio

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(combs, 1, 149))
                .circuit(1)
                .itemOutputs(MaterialLibAPI.getStack(Materials.InfusedEntropy, Shapes.gem, 1))
                .duration(51 * SECONDS + 4 * TICKS)
                .eut(12)
                .addTo(centrifugeRecipes);

            // Nethershard

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(combs, 1, 152))
                .circuit(1)
                .itemOutputs(getModItem(ThaumicTinkerer.ID, "kamiResource", 1L, 6))
                .duration(6 * SECONDS + 8 * TICKS)
                .eut(TierEU.RECIPE_HV)
                .addTo(centrifugeRecipes);

            // Endshard

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(combs, 1, 153))
                .circuit(1)
                .itemOutputs(getModItem(ThaumicTinkerer.ID, "kamiResource", 1L, 7))
                .duration(6 * SECONDS + 8 * TICKS)
                .eut(TierEU.RECIPE_HV)
                .addTo(centrifugeRecipes);
        }

        if (ExtraUtilities.isModLoaded() && Forestry.isModLoaded()) {
            // Caelestis red

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(combs, 1, 154))
                .circuit(1)
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
                .itemInputs(new ItemStack(combs, 1, 155))
                .circuit(1)
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
                .itemInputs(new ItemStack(combs, 1, 156))
                .circuit(1)
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

        // Endereye recycling

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.EnderEye, Shapes.dust, 1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Blaze, 1),
                MaterialLibAPI.getStack(Materials.EnderPearl, Shapes.dust, 1))
            .duration(32 * SECONDS + 4 * TICKS)
            .eut(10)
            .addTo(centrifugeRecipes);

        // Realgar recycling

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Realgar, Shapes.dust, 2))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Arsenic, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, 1))
            .duration(21 * SECONDS + 4 * TICKS)
            .eut(10)
            .addTo(centrifugeRecipes);

        // Cryotheum recycling

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Cryotheum, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Saltpeter, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Lapis, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Snow, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Blizz, Shapes.dust, 1))
            .duration(41 * SECONDS + 16 * TICKS)
            .eut(20)
            .addTo(centrifugeRecipes);

        // Pyrotheum recycling

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Pyrotheum, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Coal, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Redstone, Shapes.dust, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Blaze, 1),
                MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, 1))
            .duration(43 * SECONDS + 16 * TICKS)
            .eut(20)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Tesseract.get(1L), GregtechItemList.Laser_Lens_Special.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.SpaceTime, FluidShapes.fluidMolten, (int) (20 * INGOTS)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.spatialFluid, FluidShapes.fluidMolten, (int) (10 * INGOTS)),
                MaterialLibAPI.getFluidStack(Materials.temporalFluid, FluidShapes.fluidMolten, (int) (10 * INGOTS)))
            .metadata(CentrifugeRecipeKey.INSTANCE, true)
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UXV)
            .addTo(centrifugeNonCellRecipes);

        // From ProcessingSand
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.ore, Materials.Oilsands, 2), ItemList.Cell_Empty.get(1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Oil, CellShapes.cell, 1), new ItemStack(Blocks.sand, 1, 0))
            .duration(50 * SECONDS)
            .eut(5)
            .addTo(centrifugeRecipes);

        // Shijima and Churitsu recycling

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Shijima, Shapes.dust, 27))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.NetherStar, Shapes.dust, 8),
                MaterialLibAPI.getStack(Materials.Terbium, Shapes.dust, 7),
                MaterialLibAPI.getStack(Materials.Technetium, Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials.Unstable, Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials.FleroviumGT5U, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.InfinityCatalyst, Shapes.dust, 1))
            .duration(60 * SECONDS)
            .eut(122880)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Churitsu, Shapes.dust, 27))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.TinAlloy, Shapes.dust, 8),
                MaterialLibAPI.getStack(Materials.Ruridit, Shapes.dust, 7),
                MaterialLibAPI.getStack(Materials.TriniumNaquadahAlloy, Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials.AdamantiumAlloy, Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials.Californium, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.Quantum, Shapes.dust, 1))
            .duration(60 * SECONDS)
            .eut(122880)
            .addTo(centrifugeRecipes);
    }
}
