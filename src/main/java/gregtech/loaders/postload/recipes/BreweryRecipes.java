package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.ModIDs.Forestry;
import static gregtech.api.enums.ModIDs.IndustrialCraft2;
import static gregtech.api.util.GT_ModHandler.getModItem;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sBrewingRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;
import static net.minecraftforge.fluids.FluidRegistry.getFluidStack;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;

import gregtech.api.enums.*;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;

public class BreweryRecipes implements Runnable {

    @Override
    public void run() {
        ItemStack[] brewingItems = new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Talc, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Soapstone, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L) };

        for (ItemStack item : brewingItems) {
            // creosote to lubricant recipes
            GT_Values.RA.stdBuilder().itemInputs(item).noItemOutputs().fluidInputs(getFluidStack("creosote", 750))
                    .fluidOutputs(getFluidStack("lubricant", 750)).duration(6 * SECONDS + 8 * TICKS).eut(4)
                    .addTo(sBrewingRecipes);

            // seed oil to lubricant recipes
            GT_Values.RA.stdBuilder().itemInputs(item).noItemOutputs().fluidInputs(getFluidStack("seedoil", 750))
                    .fluidOutputs(getFluidStack("lubricant", 750)).duration(6 * SECONDS + 8 * TICKS).eut(4)
                    .addTo(sBrewingRecipes);

            // lubricant recipes
            {
                GT_Values.RA.stdBuilder().itemInputs(item).noItemOutputs().fluidInputs(getFluidStack("oil", 750))
                        .fluidOutputs(getFluidStack("lubricant", 750)).duration(6 * SECONDS + 8 * TICKS).eut(4)
                        .addTo(sBrewingRecipes);

                GT_Values.RA.stdBuilder().itemInputs(item).noItemOutputs()
                        .fluidInputs(getFluidStack("liquid_light_oil", 750))
                        .fluidOutputs(getFluidStack("lubricant", 500)).duration(6 * SECONDS + 8 * TICKS).eut(4)
                        .addTo(sBrewingRecipes);

                GT_Values.RA.stdBuilder().itemInputs(item).noItemOutputs()
                        .fluidInputs(getFluidStack("liquid_medium_oil", 750))
                        .fluidOutputs(getFluidStack("lubricant", 500)).duration(6 * SECONDS + 8 * TICKS).eut(4)
                        .addTo(sBrewingRecipes);

                GT_Values.RA.stdBuilder().itemInputs(item).noItemOutputs()
                        .fluidInputs(getFluidStack("liquid_heavy_oil", 500))
                        .fluidOutputs(getFluidStack("lubricant", 750)).duration(3 * SECONDS + 4 * TICKS).eut(4)
                        .addTo(sBrewingRecipes);
            }
        }

        // water based recipe input
        {
            String[] waterArray;

            /*
             * if IC2 isn't loaded, getDistilledWater returns the base minecraft water, so no need to do the recipe
             * loading twice.
             */
            if (IndustrialCraft2.isModLoaded()) {
                waterArray = new String[] { FluidRegistry.WATER.getUnlocalizedName(),
                        GT_ModHandler.getDistilledWater(1L).getFluid().getUnlocalizedName() };
            } else {
                waterArray = new String[] { FluidRegistry.WATER.getUnlocalizedName(), };
            }
            for (String fluid : waterArray) {
                GT_Values.RA.stdBuilder().itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Milk, 1L))
                        .noItemOutputs().fluidInputs(getFluidStack(fluid, 750)).fluidOutputs(getFluidStack("milk", 750))
                        .duration(6 * SECONDS + 8 * TICKS).eut(4).addTo(sBrewingRecipes);

                GT_Values.RA.stdBuilder().itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wheat, 1L))
                        .noItemOutputs().fluidInputs(getFluidStack(fluid, 750))
                        .fluidOutputs(getFluidStack("potion.wheatyjuice", 750)).duration(6 * SECONDS + 8 * TICKS).eut(4)
                        .addTo(sBrewingRecipes);

                GT_Values.RA.stdBuilder()
                        .itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Potassium, 1L)).noItemOutputs()
                        .fluidInputs(getFluidStack(fluid, 750)).fluidOutputs(getFluidStack("potion.mineralwater", 750))
                        .duration(6 * SECONDS + 8 * TICKS).eut(4).addTo(sBrewingRecipes);

                GT_Values.RA.stdBuilder().itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L))
                        .noItemOutputs().fluidInputs(getFluidStack(fluid, 750))
                        .fluidOutputs(getFluidStack("potion.mineralwater", 750)).duration(6 * SECONDS + 8 * TICKS)
                        .eut(4).addTo(sBrewingRecipes);

                GT_Values.RA.stdBuilder().itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 1L))
                        .noItemOutputs().fluidInputs(getFluidStack(fluid, 750))
                        .fluidOutputs(getFluidStack("potion.mineralwater", 750)).duration(6 * SECONDS + 8 * TICKS)
                        .eut(4).addTo(sBrewingRecipes);

                GT_Values.RA.stdBuilder()
                        .itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnesium, 1L)).noItemOutputs()
                        .fluidInputs(getFluidStack(fluid, 750)).fluidOutputs(getFluidStack("potion.mineralwater", 750))
                        .duration(6 * SECONDS + 8 * TICKS).eut(4).addTo(sBrewingRecipes);

                GT_Values.RA.stdBuilder()
                        .itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glowstone, 1L)).noItemOutputs()
                        .fluidInputs(getFluidStack(fluid, 750)).fluidOutputs(getFluidStack("potion.thick", 750))
                        .duration(6 * SECONDS + 8 * TICKS).eut(4).addTo(sBrewingRecipes);

                GT_Values.RA.stdBuilder().itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L))
                        .noItemOutputs().fluidInputs(getFluidStack(fluid, 750))
                        .fluidOutputs(getFluidStack("potion.mundane", 750)).duration(6 * SECONDS + 8 * TICKS).eut(4)
                        .addTo(sBrewingRecipes);

                GT_Values.RA.stdBuilder().itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L))
                        .noItemOutputs().fluidInputs(getFluidStack(fluid, 750))
                        .fluidOutputs(getFluidStack("potion.mundane", 750)).duration(6 * SECONDS + 8 * TICKS).eut(4)
                        .addTo(sBrewingRecipes);

                GT_Values.RA.stdBuilder().itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Blaze, 1L))
                        .noItemOutputs().fluidInputs(getFluidStack(fluid, 750))
                        .fluidOutputs(getFluidStack("potion.mundane", 750)).duration(6 * SECONDS + 8 * TICKS).eut(4)
                        .addTo(sBrewingRecipes);

                GT_Values.RA.stdBuilder().itemInputs(new ItemStack(Items.magma_cream, 1, 0)).noItemOutputs()
                        .fluidInputs(getFluidStack(fluid, 750)).fluidOutputs(getFluidStack("potion.mundane", 750))
                        .duration(6 * SECONDS + 8 * TICKS).eut(4).addTo(sBrewingRecipes);

                GT_Values.RA.stdBuilder().itemInputs(new ItemStack(Items.fermented_spider_eye, 1, 0)).noItemOutputs()
                        .fluidInputs(getFluidStack(fluid, 750)).fluidOutputs(getFluidStack("potion.mundane", 750))
                        .duration(6 * SECONDS + 8 * TICKS).eut(4).addTo(sBrewingRecipes);

                GT_Values.RA.stdBuilder().itemInputs(new ItemStack(Items.spider_eye, 1, 0)).noItemOutputs()
                        .fluidInputs(getFluidStack(fluid, 750)).fluidOutputs(getFluidStack("potion.mundane", 750))
                        .duration(6 * SECONDS + 8 * TICKS).eut(4).addTo(sBrewingRecipes);

                GT_Values.RA.stdBuilder().itemInputs(new ItemStack(Items.speckled_melon, 1, 0)).noItemOutputs()
                        .fluidInputs(getFluidStack(fluid, 750)).fluidOutputs(getFluidStack("potion.mundane", 750))
                        .duration(6 * SECONDS + 8 * TICKS).eut(4).addTo(sBrewingRecipes);

                GT_Values.RA.stdBuilder().itemInputs(new ItemStack(Items.ghast_tear, 1, 0)).noItemOutputs()
                        .fluidInputs(getFluidStack(fluid, 750)).fluidOutputs(getFluidStack("potion.mundane", 750))
                        .duration(6 * SECONDS + 8 * TICKS).eut(4).addTo(sBrewingRecipes);

                GT_Values.RA.stdBuilder().itemInputs(new ItemStack(Items.nether_wart, 1, 0)).noItemOutputs()
                        .fluidInputs(getFluidStack(fluid, 750)).fluidOutputs(getFluidStack("potion.awkward", 750))
                        .duration(6 * SECONDS + 8 * TICKS).eut(4).addTo(sBrewingRecipes);

                GT_Values.RA.stdBuilder().itemInputs(new ItemStack(Blocks.red_mushroom, 1, 0)).noItemOutputs()
                        .fluidInputs(getFluidStack(fluid, 750)).fluidOutputs(getFluidStack("potion.poison", 750))
                        .duration(6 * SECONDS + 8 * TICKS).eut(4).addTo(sBrewingRecipes);

                GT_Values.RA.stdBuilder().itemInputs(new ItemStack(Items.fish, 1, 3)).noItemOutputs()
                        .fluidInputs(getFluidStack(fluid, 750)).fluidOutputs(getFluidStack("potion.poison.strong", 750))
                        .duration(6 * SECONDS + 8 * TICKS).eut(4).hidden().addTo(sBrewingRecipes);

                GT_Values.RA.stdBuilder().itemInputs(ItemList.IC2_Grin_Powder.get(1L)).noItemOutputs()
                        .fluidInputs(getFluidStack(fluid, 750)).fluidOutputs(getFluidStack("potion.poison.strong", 750))
                        .duration(6 * SECONDS + 8 * TICKS).eut(4).addTo(sBrewingRecipes);

                GT_Values.RA.stdBuilder().itemInputs(new ItemStack(Items.reeds, 1, 0)).noItemOutputs()
                        .fluidInputs(getFluidStack(fluid, 750)).fluidOutputs(getFluidStack("potion.reedwater", 750))
                        .duration(6 * SECONDS + 8 * TICKS).eut(4).addTo(sBrewingRecipes);

                GT_Values.RA.stdBuilder().itemInputs(new ItemStack(Items.apple, 1, 0)).noItemOutputs()
                        .fluidInputs(getFluidStack(fluid, 750)).fluidOutputs(getFluidStack("potion.applejuice", 750))
                        .duration(6 * SECONDS + 8 * TICKS).eut(4).addTo(sBrewingRecipes);

                GT_Values.RA.stdBuilder().itemInputs(new ItemStack(Items.golden_apple, 1, 0)).noItemOutputs()
                        .fluidInputs(getFluidStack(fluid, 750))
                        .fluidOutputs(getFluidStack("potion.goldenapplejuice", 750)).duration(6 * SECONDS + 8 * TICKS)
                        .eut(4).hidden().addTo(sBrewingRecipes);

                GT_Values.RA.stdBuilder().itemInputs(new ItemStack(Items.golden_apple, 1, 1)).noItemOutputs()
                        .fluidInputs(getFluidStack(fluid, 750))
                        .fluidOutputs(getFluidStack("potion.idunsapplejuice", 750)).duration(6 * SECONDS + 8 * TICKS)
                        .eut(4).hidden().addTo(sBrewingRecipes);

                GT_Values.RA.stdBuilder().itemInputs(ItemList.IC2_Hops.get(1L)).noItemOutputs()
                        .fluidInputs(getFluidStack(fluid, 750)).fluidOutputs(getFluidStack("potion.hopsjuice", 750))
                        .duration(6 * SECONDS + 8 * TICKS).eut(4).addTo(sBrewingRecipes);

                GT_Values.RA.stdBuilder().itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coffee, 1L))
                        .noItemOutputs().fluidInputs(getFluidStack(fluid, 750))
                        .fluidOutputs(getFluidStack("potion.darkcoffee", 750)).duration(6 * SECONDS + 8 * TICKS).eut(4)
                        .addTo(sBrewingRecipes);

                GT_Values.RA.stdBuilder().itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chili, 1L))
                        .noItemOutputs().fluidInputs(getFluidStack(fluid, 750))
                        .fluidOutputs(getFluidStack("potion.chillysauce", 750)).duration(6 * SECONDS + 8 * TICKS).eut(4)
                        .addTo(sBrewingRecipes);

            }
        }

        // potion brewing 1
        {
            GT_Values.RA.stdBuilder().itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chili, 1L))
                    .noItemOutputs().fluidInputs(getFluidStack("potion.chillysauce", 750))
                    .fluidOutputs(getFluidStack("potion.hotsauce", 750)).duration(6 * SECONDS + 8 * TICKS).eut(4)
                    .addTo(sBrewingRecipes);

            GT_Values.RA.stdBuilder().itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chili, 1L))
                    .noItemOutputs().fluidInputs(getFluidStack("potion.hotsauce", 750))
                    .fluidOutputs(getFluidStack("potion.diabolosauce", 750)).duration(6 * SECONDS + 8 * TICKS).eut(4)
                    .hidden().addTo(sBrewingRecipes);

            GT_Values.RA.stdBuilder().itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chili, 1L))
                    .noItemOutputs().fluidInputs(getFluidStack("potion.diabolosauce", 750))
                    .fluidOutputs(getFluidStack("potion.diablosauce", 750)).duration(6 * SECONDS + 8 * TICKS).eut(4)
                    .hidden().addTo(sBrewingRecipes);

            GT_Values.RA.stdBuilder().itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coffee, 1L))
                    .noItemOutputs().fluidInputs(getFluidStack("milk", 750))
                    .fluidOutputs(getFluidStack("potion.coffee", 750)).duration(6 * SECONDS + 8 * TICKS).eut(4)
                    .addTo(sBrewingRecipes);

            GT_Values.RA.stdBuilder().itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Cocoa, 1L))
                    .noItemOutputs().fluidInputs(getFluidStack("milk", 750))
                    .fluidOutputs(getFluidStack("potion.darkchocolatemilk", 750)).duration(6 * SECONDS + 8 * TICKS)
                    .eut(4).addTo(sBrewingRecipes);

            GT_Values.RA.stdBuilder().itemInputs(ItemList.IC2_Hops.get(1L)).noItemOutputs()
                    .fluidInputs(getFluidStack("potion.wheatyjuice", 750))
                    .fluidOutputs(getFluidStack("potion.wheatyhopsjuice", 750)).duration(6 * SECONDS + 8 * TICKS).eut(4)
                    .addTo(sBrewingRecipes);

            GT_Values.RA.stdBuilder().itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wheat, 1L))
                    .noItemOutputs().fluidInputs(getFluidStack("potion.hopsjuice", 750))
                    .fluidOutputs(getFluidStack("potion.wheatyhopsjuice", 750)).duration(6 * SECONDS + 8 * TICKS).eut(4)
                    .addTo(sBrewingRecipes);

            GT_Values.RA.stdBuilder().itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L))
                    .noItemOutputs().fluidInputs(getFluidStack("potion.tea", 750))
                    .fluidOutputs(getFluidStack("potion.sweettea", 750)).duration(6 * SECONDS + 8 * TICKS).eut(4)
                    .hidden().addTo(sBrewingRecipes);

            GT_Values.RA.stdBuilder().itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L))
                    .noItemOutputs().fluidInputs(getFluidStack("potion.coffee", 750))
                    .fluidOutputs(getFluidStack("potion.cafeaulait", 750)).duration(6 * SECONDS + 8 * TICKS).eut(4)
                    .addTo(sBrewingRecipes);

            GT_Values.RA.stdBuilder().itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L))
                    .noItemOutputs().fluidInputs(getFluidStack("potion.cafeaulait", 750))
                    .fluidOutputs(getFluidStack("potion.laitaucafe", 750)).duration(6 * SECONDS + 8 * TICKS).eut(4)
                    .hidden().addTo(sBrewingRecipes);

            GT_Values.RA.stdBuilder().itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L))
                    .noItemOutputs().fluidInputs(getFluidStack("potion.lemonjuice", 750))
                    .fluidOutputs(getFluidStack("potion.lemonade", 750)).duration(6 * SECONDS + 8 * TICKS).eut(4)
                    .addTo(sBrewingRecipes);

            GT_Values.RA.stdBuilder().itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L))
                    .noItemOutputs().fluidInputs(getFluidStack("potion.darkcoffee", 750))
                    .fluidOutputs(getFluidStack("potion.darkcafeaulait", 750)).duration(6 * SECONDS + 8 * TICKS).eut(4)
                    .hidden().addTo(sBrewingRecipes);

            GT_Values.RA.stdBuilder().itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L))
                    .noItemOutputs().fluidInputs(getFluidStack("potion.darkchocolatemilk", 750))
                    .fluidOutputs(getFluidStack("potion.chocolatemilk", 750)).duration(6 * SECONDS + 8 * TICKS).eut(4)
                    .addTo(sBrewingRecipes);

            GT_Values.RA.stdBuilder().itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ice, 1L))
                    .noItemOutputs().fluidInputs(getFluidStack("potion.tea", 750))
                    .fluidOutputs(getFluidStack("potion.icetea", 750)).duration(6 * SECONDS + 8 * TICKS).eut(4)
                    .addTo(sBrewingRecipes);

            GT_Values.RA.stdBuilder().itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gunpowder, 1L))
                    .noItemOutputs().fluidInputs(getFluidStack("potion.lemonade", 750))
                    .fluidOutputs(getFluidStack("potion.cavejohnsonsgrenadejuice", 750))
                    .duration(6 * SECONDS + 8 * TICKS).eut(4).hidden().addTo(sBrewingRecipes);

            GT_Values.RA.stdBuilder().itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L))
                    .noItemOutputs().fluidInputs(getFluidStack("potion.mundane", 750))
                    .fluidOutputs(getFluidStack("potion.purpledrink", 750)).duration(6 * SECONDS + 8 * TICKS).eut(4)
                    .hidden().addTo(sBrewingRecipes);

            GT_Values.RA.stdBuilder().itemInputs(new ItemStack(Items.fermented_spider_eye, 1, 0)).noItemOutputs()
                    .fluidInputs(getFluidStack("potion.mundane", 750))
                    .fluidOutputs(getFluidStack("potion.weakness", 750)).duration(6 * SECONDS + 8 * TICKS).eut(4)
                    .addTo(sBrewingRecipes);

            GT_Values.RA.stdBuilder().itemInputs(new ItemStack(Items.fermented_spider_eye, 1, 0)).noItemOutputs()
                    .fluidInputs(getFluidStack("potion.thick", 750)).fluidOutputs(getFluidStack("potion.weakness", 750))
                    .duration(6 * SECONDS + 8 * TICKS).eut(4).addTo(sBrewingRecipes);
        }

        // biomass recipes
        {
            GT_Values.RA.stdBuilder().itemInputs(getModItem(Forestry.modID, "fertilizerBio", 4L, 0)).noItemOutputs()
                    .fluidInputs(getFluidStack(FluidRegistry.WATER.getUnlocalizedName(), 750))
                    .fluidOutputs(getFluidStack("biomass", 750)).duration(6 * SECONDS + 8 * TICKS).eut(4)
                    .addTo(sBrewingRecipes);

            GT_Values.RA.stdBuilder().itemInputs(getModItem(Forestry.modID, "mulch", 16L, 0)).noItemOutputs()
                    .fluidInputs(
                            getFluidStack(GT_ModHandler.getDistilledWater(750L).getFluid().getUnlocalizedName(), 750))
                    .fluidOutputs(getFluidStack("biomass", 750)).duration(6 * SECONDS + 8 * TICKS).eut(4)
                    .addTo(sBrewingRecipes);

            GT_Values.RA.stdBuilder().itemInputs(getModItem(Forestry.modID, "mulch", 8L, 0)).noItemOutputs()
                    .fluidInputs(getFluidStack("juice", 500)).fluidOutputs(getFluidStack("biomass", 750))
                    .duration(6 * SECONDS + 8 * TICKS).eut(4).addTo(sBrewingRecipes);
        }

        // ic2 biomass recipes
        {
            GT_Values.RA.stdBuilder().itemInputs(GT_ModHandler.getIC2Item("biochaff", 1)).noItemOutputs()
                    .fluidInputs(GT_ModHandler.getWater(1000L)).fluidOutputs(getFluidStack("ic2biomass", 1000))
                    .duration(8 * SECONDS + 10 * TICKS).eut(4).addTo(sBrewingRecipes);

            // Would add 2 different amount of water input if IC2 isn't loaded
            if (IndustrialCraft2.isModLoaded()) {
                GT_Values.RA.stdBuilder().itemInputs(GT_ModHandler.getIC2Item("biochaff", 1)).noItemOutputs()
                        .fluidInputs(GT_ModHandler.getDistilledWater(500L))
                        .fluidOutputs(getFluidStack("ic2biomass", 1000)).duration(10 * TICKS)
                        .eut((int) TierEU.RECIPE_LV).addTo(sBrewingRecipes);
            }
        }

        // potion brewing 2
        {
            this.addPotionRecipes("waterbreathing", new ItemStack(Items.fish, 1, 3));
            this.addPotionRecipes("fireresistance", new ItemStack(Items.magma_cream, 1, 0));
            this.addPotionRecipes("nightvision", new ItemStack(Items.golden_carrot, 1, 0));
            this.addPotionRecipes("weakness", new ItemStack(Items.fermented_spider_eye, 1, 0));
            this.addPotionRecipes("poison", new ItemStack(Items.spider_eye, 1, 0));
            this.addPotionRecipes("health", new ItemStack(Items.speckled_melon, 1, 0));
            this.addPotionRecipes("regen", new ItemStack(Items.ghast_tear, 1, 0));
            this.addPotionRecipes("speed", GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L));
            this.addPotionRecipes("strength", GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Blaze, 1L));
        }
    }

    public void addPotionRecipes(String aName, ItemStack aItem) {
        // normal
        GT_Values.RA.stdBuilder().itemInputs(aItem).noItemOutputs().fluidInputs(getFluidStack("potion.awkward", 750))
                .fluidOutputs(getFluidStack("potion." + aName, 750)).duration(6 * SECONDS + 8 * TICKS).eut(4)
                .addTo(sBrewingRecipes);

        // strong
        GT_Values.RA.stdBuilder().itemInputs(aItem).noItemOutputs().fluidInputs(getFluidStack("potion.thick", 750))
                .fluidOutputs(getFluidStack("potion." + aName + ".strong", 750)).duration(6 * SECONDS + 8 * TICKS)
                .eut(4).addTo(sBrewingRecipes);

        // long
        GT_Values.RA.stdBuilder().itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L))
                .noItemOutputs().fluidInputs(getFluidStack("potion." + aName, 750))
                .fluidOutputs(getFluidStack("potion." + aName + ".long", 750)).duration(6 * SECONDS + 8 * TICKS).eut(4)
                .addTo(sBrewingRecipes);

        MixerRecipes.addMixerPotionRecipes(aName);
    }
}
