package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.BuildCraftTransport;
import static gregtech.api.enums.Mods.EtFuturumRequiem;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.TinkerConstruct;
import static gregtech.api.recipe.RecipeMaps.cutterRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;

@SuppressWarnings({ "PointlessArithmeticExpression" })
public class CuttingRecipes implements Runnable {

    @Override
    public void run() {

        recipeWithClassicFluids(
            new ItemStack[] { ItemList.Food_Dough_Chocolate.get(1), ItemList.Shape_Slicer_Flat.get(0) },
            new ItemStack[] { ItemList.Food_Raw_Cookie.get(4) },
            6 * SECONDS + 8 * TICKS,
            4,
            false);

        recipeWithClassicFluids(
            new ItemStack[] { ItemList.Food_Baked_Bun.get(1), ItemList.Shape_Slicer_Flat.get(0) },
            new ItemStack[] { ItemList.Food_Sliced_Bun.get(2) },
            6 * SECONDS + 8 * TICKS,
            4,
            false);

        recipeWithClassicFluids(
            new ItemStack[] { ItemList.Food_Baked_Bread.get(1), ItemList.Shape_Slicer_Flat.get(0) },
            new ItemStack[] { ItemList.Food_Sliced_Bread.get(2) },
            6 * SECONDS + 8 * TICKS,
            4,
            false);

        recipeWithClassicFluids(
            new ItemStack[] { ItemList.Food_Baked_Baguette.get(1), ItemList.Shape_Slicer_Flat.get(0) },
            new ItemStack[] { ItemList.Food_Sliced_Baguette.get(2) },
            6 * SECONDS + 8 * TICKS,
            4,
            false);

        // silicon wafer recipes
        {
            recipeWithClassicFluids(
                new ItemStack[] { ItemList.Circuit_Silicon_Ingot.get(1) },
                new ItemStack[] { ItemList.Circuit_Silicon_Wafer.get(16),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.SiliconSG, 4) },
                20 * SECONDS,
                TierEU.RECIPE_LV,
                false);

            recipeWithClassicFluids(
                new ItemStack[] { ItemList.Circuit_Silicon_Ingot2.get(1) },
                new ItemStack[] { ItemList.Circuit_Silicon_Wafer2.get(32),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.SiliconSG, 8) },
                40 * SECONDS,
                TierEU.RECIPE_MV,
                true);

            recipeWithClassicFluids(
                new ItemStack[] { ItemList.Circuit_Silicon_Ingot3.get(1) },
                new ItemStack[] { ItemList.Circuit_Silicon_Wafer3.get(64),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.SiliconSG, 16) },
                1 * MINUTES + 20 * SECONDS,
                TierEU.RECIPE_HV,
                true);
            // Naq wafer also gets recipes using purified water
            recipeWithPurifiedWater(
                new ItemStack[] { ItemList.Circuit_Silicon_Ingot3.get(1) },
                new ItemStack[] { ItemList.Circuit_Silicon_Wafer3.get(128),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.SiliconSG, 16) },
                Materials.Grade1PurifiedWater,
                Materials.Grade2PurifiedWater,
                (int) ((1 * MINUTES + 20 * SECONDS) * 0.75),
                (1 * MINUTES + 20 * SECONDS) / 2,
                TierEU.RECIPE_HV);

            recipeWithPurifiedWater(
                new ItemStack[] { ItemList.Circuit_Silicon_Ingot4.get(1) },
                new ItemStack[] { ItemList.Circuit_Silicon_Wafer4.get(64), ItemList.Circuit_Silicon_Wafer4.get(32),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.SiliconSG, 32) },
                Materials.Grade3PurifiedWater,
                Materials.Grade4PurifiedWater,
                2 * MINUTES,
                1 * MINUTES,
                TierEU.RECIPE_EV);

            recipeWithPurifiedWater(
                new ItemStack[] { ItemList.Circuit_Silicon_Ingot5.get(1) },
                new ItemStack[] { ItemList.Circuit_Silicon_Wafer5.get(64), ItemList.Circuit_Silicon_Wafer5.get(64),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.SiliconSG, 64) },
                Materials.Grade5PurifiedWater,
                Materials.Grade6PurifiedWater,
                2 * MINUTES + 40 * SECONDS,
                (2 * MINUTES + 40 * SECONDS) / 2,
                TierEU.RECIPE_IV);

        }

        // glass pane recipes
        {
            // stained-glass -> glass pane recipes
            for (int i = 0; i < 16; i++) {
                recipeWithClassicFluids(
                    new ItemStack[] { new ItemStack(Blocks.stained_glass, 3, i) },
                    new ItemStack[] { new ItemStack(Blocks.stained_glass_pane, 8, i) },
                    2 * SECONDS + 10 * TICKS,
                    8,
                    false);

            }

            recipeWithClassicFluids(
                new ItemStack[] { new ItemStack(Blocks.glass, 3, 0) },
                new ItemStack[] { new ItemStack(Blocks.glass_pane, 8, 0) },
                2 * SECONDS + 10 * TICKS,
                8,
                false);

            if (TinkerConstruct.isModLoaded()) {
                recipeWithClassicFluids(
                    new ItemStack[] { getModItem(TinkerConstruct.ID, "GlassBlock", 3L, 0) },
                    new ItemStack[] { getModItem(TinkerConstruct.ID, "GlassPane", 8L, 0) },
                    2 * SECONDS + 10 * TICKS,
                    8,
                    false);
            }
        }

        // stone slab recipes
        {
            if (EtFuturumRequiem.isModLoaded()) {
                recipeWithClassicFluids(
                    new ItemStack[] { getModItem(EtFuturumRequiem.ID, "smooth_stone", 1L, 0) },
                    new ItemStack[] { new ItemStack(Blocks.stone_slab, 2, 0) },
                    1 * SECONDS + 5 * TICKS,
                    8,
                    false);
            }

            recipeWithClassicFluids(
                new ItemStack[] { new ItemStack(Blocks.sandstone, 1, 0) },
                new ItemStack[] { new ItemStack(Blocks.stone_slab, 2, 1) },
                1 * SECONDS + 5 * TICKS,
                8,
                false);

            recipeWithClassicFluids(
                new ItemStack[] { new ItemStack(Blocks.cobblestone, 1, 0) },
                new ItemStack[] { new ItemStack(Blocks.stone_slab, 2, 3) },
                1 * SECONDS + 5 * TICKS,
                8,
                false);

            recipeWithClassicFluids(
                new ItemStack[] { new ItemStack(Blocks.brick_block, 1, 0) },
                new ItemStack[] { new ItemStack(Blocks.stone_slab, 2, 4) },
                1 * SECONDS + 5 * TICKS,
                8,
                false);

            recipeWithClassicFluids(
                new ItemStack[] { new ItemStack(Blocks.stonebrick, 1, 0) },
                new ItemStack[] { new ItemStack(Blocks.stone_slab, 2, 5) },
                1 * SECONDS + 5 * TICKS,
                8,
                false);

            recipeWithClassicFluids(
                new ItemStack[] { new ItemStack(Blocks.nether_brick, 1, 0) },
                new ItemStack[] { new ItemStack(Blocks.stone_slab, 2, 6) },
                1 * SECONDS + 5 * TICKS,
                8,
                false);

            recipeWithClassicFluids(
                new ItemStack[] { new ItemStack(Blocks.quartz_block, 1, 32767) },
                new ItemStack[] { new ItemStack(Blocks.stone_slab, 2, 7) },
                1 * SECONDS + 5 * TICKS,
                8,
                false);
        }

        recipeWithClassicFluids(
            new ItemStack[] { new ItemStack(Blocks.glowstone, 1, 0) },
            new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.plate, Materials.Glowstone, 4) },
            5 * SECONDS,
            16,
            false);

        for (byte i = 0; i < 16; i++) {
            recipeWithClassicFluids(
                new ItemStack[] { new ItemStack(Blocks.wool, 1, i) },
                new ItemStack[] { new ItemStack(Blocks.carpet, 2, i) },
                2 * SECONDS + 10 * TICKS,
                8,
                false);
        }

        // vanilla planks recipes
        {
            ItemStack[] plankArray = new ItemStack[] { ItemList.Plank_Oak.get(2L), ItemList.Plank_Spruce.get(2L),
                ItemList.Plank_Birch.get(2L), ItemList.Plank_Jungle.get(2L), ItemList.Plank_Acacia.get(2L),
                ItemList.Plank_DarkOak.get(2L) };
            for (int i = 0; i < 6; i++) {
                recipeWithClassicFluids(
                    new ItemStack[] { new ItemStack(Blocks.wooden_slab, 1, i) },
                    new ItemStack[] { plankArray[i] },
                    2 * SECONDS + 10 * TICKS,
                    8,
                    false);
            }
        }

        if (EtFuturumRequiem.isModLoaded()) {
            recipeWithClassicFluids(
                new ItemStack[] { getModItem(EtFuturumRequiem.ID, "wood_slab", 1L, 3) },
                new ItemStack[] { ItemList.Plank_Cherry_EFR.get(2) },
                2 * SECONDS + 10 * TICKS,
                8,
                false);
        }

        if (Forestry.isModLoaded()) {
            ItemStack[] coverIDs = { ItemList.Plank_Larch.get(2L), ItemList.Plank_Teak.get(2L),
                ItemList.Plank_Acacia_Green.get(2L), ItemList.Plank_Lime.get(2L), ItemList.Plank_Chestnut.get(2L),
                ItemList.Plank_Wenge.get(2L), ItemList.Plank_Baobab.get(2L), ItemList.Plank_Sequoia.get(2L),
                ItemList.Plank_Kapok.get(2L), ItemList.Plank_Ebony.get(2L), ItemList.Plank_Mahagony.get(2L),
                ItemList.Plank_Balsa.get(2L), ItemList.Plank_Willow.get(2L), ItemList.Plank_Walnut.get(2L),
                ItemList.Plank_Greenheart.get(2L), ItemList.Plank_Cherry.get(2L), ItemList.Plank_Mahoe.get(2L),
                ItemList.Plank_Poplar.get(2L), ItemList.Plank_Palm.get(2L), ItemList.Plank_Papaya.get(2L),
                ItemList.Plank_Pine.get(2L), ItemList.Plank_Plum.get(2L), ItemList.Plank_Maple.get(2L),
                ItemList.Plank_Citrus.get(2L) };
            for (int i = 0; i < coverIDs.length; i++) {
                ItemStack slabWood = getModItem(Forestry.ID, "slabs", 1, i);
                ItemStack slabWoodFireproof = getModItem(Forestry.ID, "slabsFireproof", 1, i);

                recipeWithClassicFluids(
                    new ItemStack[] { slabWood },
                    new ItemStack[] { coverIDs[i] },
                    2 * SECONDS,
                    8,
                    false);

                recipeWithClassicFluids(
                    new ItemStack[] { slabWoodFireproof },
                    new ItemStack[] { coverIDs[i] },
                    2 * SECONDS,
                    8,
                    false);

            }
        }

        if (BuildCraftTransport.isModLoaded()) {
            recipeWithClassicFluids(
                new ItemStack[] {
                    getModItem(BuildCraftTransport.ID, "item.buildcraftPipe.pipestructurecobblestone", 1L, 0) },
                new ItemStack[] { getModItem(BuildCraftTransport.ID, "pipePlug", 8L, 0) },
                1 * SECONDS + 12 * TICKS,
                16,
                false);

        }

    }

    public void recipeWithPurifiedWater(ItemStack[] inputs, ItemStack[] outputs, Materials lowTierWater,
        Materials highTierWater, int duration, int boostedDuration, long eut) {
        GTValues.RA.stdBuilder()
            .itemInputs(inputs)
            .itemOutputs(outputs)
            .fluidInputs(lowTierWater.getFluid(100L))
            .duration(duration)
            .eut(eut)
            .addTo(cutterRecipes);
        // Bonus for using higher tier water
        GTValues.RA.stdBuilder()
            .itemInputs(inputs)
            .itemOutputs(outputs)
            .fluidInputs(highTierWater.getFluid(100L))
            .duration(boostedDuration)
            .eut(eut)
            .addTo(cutterRecipes);
    }

    public void recipeWithClassicFluids(ItemStack[] inputs, ItemStack[] outputs, int duration, long eut,
        boolean cleanroomRequired) {
        if (cleanroomRequired) {
            GTValues.RA.stdBuilder()
                .itemInputs(inputs)
                .itemOutputs(outputs)
                .fluidInputs(Materials.Water.getFluid(Math.max(4, Math.min(1000, duration * eut / 320))))
                .duration(2 * duration)
                .eut(eut)
                .requiresCleanRoom()
                .addTo(cutterRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(inputs)
                .itemOutputs(outputs)
                .fluidInputs(GTModHandler.getDistilledWater(Math.max(3, Math.min(750, duration * eut / 426))))
                .duration(2 * duration)
                .eut(eut)
                .requiresCleanRoom()
                .addTo(cutterRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(inputs)
                .itemOutputs(outputs)
                .fluidInputs(Materials.Lubricant.getFluid(Math.max(1, Math.min(250, duration * eut / 1280))))
                .duration(duration)
                .eut(eut)
                .requiresCleanRoom()
                .addTo(cutterRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(inputs)
                .itemOutputs(outputs)
                .fluidInputs(
                    Materials.DimensionallyShiftedSuperfluid.getFluid(Math.max(1, Math.min(10, duration * eut / 4000))))
                .duration((int) (duration / 2.5))
                .eut(eut)
                .requiresCleanRoom()
                .addTo(cutterRecipes);
        } else {
            GTValues.RA.stdBuilder()
                .itemInputs(inputs)
                .itemOutputs(outputs)
                .fluidInputs(Materials.Water.getFluid(Math.max(4, Math.min(1000, duration * eut / 320))))
                .duration(2 * duration)
                .eut(eut)
                .addTo(cutterRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(inputs)
                .itemOutputs(outputs)
                .fluidInputs(GTModHandler.getDistilledWater(Math.max(3, Math.min(750, duration * eut / 426))))
                .duration(2 * duration)
                .eut(eut)
                .addTo(cutterRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(inputs)
                .itemOutputs(outputs)
                .fluidInputs(Materials.Lubricant.getFluid(Math.max(1, Math.min(250, duration * eut / 1280))))
                .duration(duration)
                .eut(eut)
                .addTo(cutterRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(inputs)
                .itemOutputs(outputs)
                .fluidInputs(
                    Materials.DimensionallyShiftedSuperfluid.getFluid(Math.max(1, Math.min(10, duration * eut / 4000))))
                .duration((int) (duration / 2.5))
                .eut(eut)
                .addTo(cutterRecipes);
        }
    }
}
