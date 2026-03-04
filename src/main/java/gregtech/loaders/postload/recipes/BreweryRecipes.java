package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.IndustrialCraft2;
import static gregtech.api.recipe.RecipeMaps.brewingRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static net.minecraftforge.fluids.FluidRegistry.getFluidStack;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;

public class BreweryRecipes implements Runnable {

    @Override
    public void run() {
        ItemStack[] brewingItems = new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.dust, Materials.Talc, 1L),
            GTOreDictUnificator.get(OrePrefixes.dust, Materials.Soapstone, 1L),
            GTOreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L) };

        for (ItemStack item : brewingItems) {
            // creosote to lubricant recipes
            GTValues.RA.stdBuilder()
                .itemInputs(item)
                .fluidInputs(Materials.Creosote.getFluid(750))
                .fluidOutputs(Materials.Lubricant.getFluid(750))
                .duration(6 * SECONDS + 8 * TICKS)
                .eut(4)
                .addTo(brewingRecipes);

            // seed oil to lubricant recipes
            GTValues.RA.stdBuilder()
                .itemInputs(item)
                .fluidInputs(Materials.SeedOil.getFluid(750))
                .fluidOutputs(Materials.Lubricant.getFluid(750))
                .duration(6 * SECONDS + 8 * TICKS)
                .eut(4)
                .addTo(brewingRecipes);

            // lubricant recipes
            {
                GTValues.RA.stdBuilder()
                    .itemInputs(item)
                    .fluidInputs(Materials.Oil.getFluid(750))
                    .fluidOutputs(Materials.Lubricant.getFluid(750))
                    .duration(6 * SECONDS + 8 * TICKS)
                    .eut(4)
                    .addTo(brewingRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(item)
                    .fluidInputs(Materials.OilLight.getFluid(750))
                    .fluidOutputs(Materials.Lubricant.getFluid(500))
                    .duration(6 * SECONDS + 8 * TICKS)
                    .eut(4)
                    .addTo(brewingRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(item)
                    .fluidInputs(Materials.OilMedium.getFluid(750))
                    .fluidOutputs(Materials.Lubricant.getFluid(500))
                    .duration(6 * SECONDS + 8 * TICKS)
                    .eut(4)
                    .addTo(brewingRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(item)
                    .fluidInputs(Materials.OilHeavy.getFluid(500))
                    .fluidOutputs(Materials.Lubricant.getFluid(750))
                    .duration(3 * SECONDS + 4 * TICKS)
                    .eut(4)
                    .addTo(brewingRecipes);
            }
        }

        // water based recipe input
        {
            Fluid[] waterArray;

            /*
             * if IC2 isn't loaded, getDistilledWater returns the base minecraft water, so no need to do the recipe
             * loading twice.
             */
            if (IndustrialCraft2.isModLoaded()) {
                waterArray = new Fluid[] { FluidRegistry.WATER, GTModHandler.getDistilledWater(1L)
                    .getFluid() };

                GTValues.RA.stdBuilder()
                    .itemInputs(new ItemStack(Blocks.red_mushroom, 1, 0))
                    .fluidInputs(new FluidStack(GTModHandler.getDistilledWater(1), 750))
                    .fluidOutputs(getFluidStack("potion.poison", 750))
                    .duration(6 * SECONDS + 8 * TICKS)
                    .eut(4)
                    .addTo(brewingRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(new ItemStack(Items.reeds, 1, 0))
                    .fluidInputs(new FluidStack(GTModHandler.getDistilledWater(1), 750))
                    .fluidOutputs(getFluidStack("potion.reedwater", 750))
                    .duration(6 * SECONDS + 8 * TICKS)
                    .eut(4)
                    .addTo(brewingRecipes);

            } else {
                waterArray = new Fluid[] { FluidRegistry.WATER };
            }
            for (Fluid tFluid : waterArray) {
                GTValues.RA.stdBuilder()
                    .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Milk, 1L))
                    .fluidInputs(new FluidStack(tFluid, 750))
                    .fluidOutputs(Materials.Milk.getFluid(750))
                    .duration(6 * SECONDS + 8 * TICKS)
                    .eut(4)
                    .addTo(brewingRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Wheat, 1L))
                    .fluidInputs(new FluidStack(tFluid, 750))
                    .fluidOutputs(getFluidStack("potion.wheatyjuice", 750))
                    .duration(6 * SECONDS + 8 * TICKS)
                    .eut(4)
                    .addTo(brewingRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Potassium, 1L))
                    .fluidInputs(new FluidStack(tFluid, 750))
                    .fluidOutputs(getFluidStack("potion.mineralwater", 750))
                    .duration(6 * SECONDS + 8 * TICKS)
                    .eut(4)
                    .addTo(brewingRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L))
                    .fluidInputs(new FluidStack(tFluid, 750))
                    .fluidOutputs(getFluidStack("potion.mineralwater", 750))
                    .duration(6 * SECONDS + 8 * TICKS)
                    .eut(4)
                    .addTo(brewingRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 1L))
                    .fluidInputs(new FluidStack(tFluid, 750))
                    .fluidOutputs(getFluidStack("potion.mineralwater", 750))
                    .duration(6 * SECONDS + 8 * TICKS)
                    .eut(4)
                    .addTo(brewingRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Magnesium, 1L))
                    .fluidInputs(new FluidStack(tFluid, 750))
                    .fluidOutputs(getFluidStack("potion.mineralwater", 750))
                    .duration(6 * SECONDS + 8 * TICKS)
                    .eut(4)
                    .addTo(brewingRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Glowstone, 1L))
                    .fluidInputs(new FluidStack(tFluid, 750))
                    .fluidOutputs(getFluidStack("potion.thick", 750))
                    .duration(6 * SECONDS + 8 * TICKS)
                    .eut(4)
                    .addTo(brewingRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L))
                    .fluidInputs(new FluidStack(tFluid, 750))
                    .fluidOutputs(getFluidStack("potion.mundane", 750))
                    .duration(6 * SECONDS + 8 * TICKS)
                    .eut(4)
                    .addTo(brewingRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L))
                    .fluidInputs(new FluidStack(tFluid, 750))
                    .fluidOutputs(getFluidStack("potion.mundane", 750))
                    .duration(6 * SECONDS + 8 * TICKS)
                    .eut(4)
                    .addTo(brewingRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Blaze, 1L))
                    .fluidInputs(new FluidStack(tFluid, 750))
                    .fluidOutputs(getFluidStack("potion.mundane", 750))
                    .duration(6 * SECONDS + 8 * TICKS)
                    .eut(4)
                    .addTo(brewingRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(new ItemStack(Items.magma_cream, 1, 0))
                    .fluidInputs(new FluidStack(tFluid, 750))
                    .fluidOutputs(getFluidStack("potion.mundane", 750))
                    .duration(6 * SECONDS + 8 * TICKS)
                    .eut(4)
                    .addTo(brewingRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(new ItemStack(Items.fermented_spider_eye, 1, 0))
                    .fluidInputs(new FluidStack(tFluid, 750))
                    .fluidOutputs(getFluidStack("potion.mundane", 750))
                    .duration(6 * SECONDS + 8 * TICKS)
                    .eut(4)
                    .addTo(brewingRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(new ItemStack(Items.spider_eye, 1, 0))
                    .fluidInputs(new FluidStack(tFluid, 750))
                    .fluidOutputs(getFluidStack("potion.mundane", 750))
                    .duration(6 * SECONDS + 8 * TICKS)
                    .eut(4)
                    .addTo(brewingRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(new ItemStack(Items.speckled_melon, 1, 0))
                    .fluidInputs(new FluidStack(tFluid, 750))
                    .fluidOutputs(getFluidStack("potion.mundane", 750))
                    .duration(6 * SECONDS + 8 * TICKS)
                    .eut(4)
                    .addTo(brewingRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(new ItemStack(Items.ghast_tear, 1, 0))
                    .fluidInputs(new FluidStack(tFluid, 750))
                    .fluidOutputs(getFluidStack("potion.mundane", 750))
                    .duration(6 * SECONDS + 8 * TICKS)
                    .eut(4)
                    .addTo(brewingRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(new ItemStack(Items.nether_wart, 1, 0))
                    .fluidInputs(new FluidStack(tFluid, 750))
                    .fluidOutputs(getFluidStack("potion.awkward", 750))
                    .duration(6 * SECONDS + 8 * TICKS)
                    .eut(4)
                    .addTo(brewingRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(new ItemStack(Items.fish, 1, 3))
                    .fluidInputs(new FluidStack(tFluid, 750))
                    .fluidOutputs(getFluidStack("potion.poison.strong", 750))
                    .duration(6 * SECONDS + 8 * TICKS)
                    .eut(4)
                    .addTo(brewingRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(new ItemStack(Items.apple, 1, 0))
                    .fluidInputs(new FluidStack(tFluid, 750))
                    .fluidOutputs(getFluidStack("potion.applejuice", 750))
                    .duration(6 * SECONDS + 8 * TICKS)
                    .eut(4)
                    .addTo(brewingRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(new ItemStack(Items.golden_apple, 1, 0))
                    .fluidInputs(new FluidStack(tFluid, 750))
                    .fluidOutputs(getFluidStack("potion.goldenapplejuice", 750))
                    .duration(6 * SECONDS + 8 * TICKS)
                    .eut(4)
                    .addTo(brewingRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(new ItemStack(Items.golden_apple, 1, 1))
                    .fluidInputs(new FluidStack(tFluid, 750))
                    .fluidOutputs(getFluidStack("potion.idunsapplejuice", 750))
                    .duration(6 * SECONDS + 8 * TICKS)
                    .eut(4)
                    .addTo(brewingRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Coffee, 1L))
                    .fluidInputs(new FluidStack(tFluid, 750))
                    .fluidOutputs(getFluidStack("potion.coffee", 750))
                    .duration(6 * SECONDS + 8 * TICKS)
                    .eut(4)
                    .addTo(brewingRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Chili, 1L))
                    .fluidInputs(new FluidStack(tFluid, 750))
                    .fluidOutputs(getFluidStack("potion.chillysauce", 750))
                    .duration(6 * SECONDS + 8 * TICKS)
                    .eut(4)
                    .addTo(brewingRecipes);

            }
        }

        // potion brewing 1
        {
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Chili, 1L))
                .fluidInputs(getFluidStack("potion.chillysauce", 750))
                .fluidOutputs(getFluidStack("potion.hotsauce", 750))
                .duration(6 * SECONDS + 8 * TICKS)
                .eut(4)
                .addTo(brewingRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Chili, 1L))
                .fluidInputs(getFluidStack("potion.hotsauce", 750))
                .fluidOutputs(getFluidStack("potion.diabolosauce", 750))
                .duration(6 * SECONDS + 8 * TICKS)
                .eut(4)
                .addTo(brewingRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Chili, 1L))
                .fluidInputs(getFluidStack("potion.diabolosauce", 750))
                .fluidOutputs(getFluidStack("potion.diablosauce", 750))
                .duration(6 * SECONDS + 8 * TICKS)
                .eut(4)
                .addTo(brewingRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Coffee, 1L))
                .fluidInputs(Materials.Milk.getFluid(750))
                .fluidOutputs(getFluidStack("potion.latte", 750))
                .duration(6 * SECONDS + 8 * TICKS)
                .eut(4)
                .addTo(brewingRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Cocoa, 1L))
                .fluidInputs(Materials.Milk.getFluid(750))
                .fluidOutputs(getFluidStack("potion.darkchocolatemilk", 750))
                .duration(6 * SECONDS + 8 * TICKS)
                .eut(4)
                .addTo(brewingRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Wheat, 1L))
                .fluidInputs(getFluidStack("potion.hopsjuice", 750))
                .fluidOutputs(getFluidStack("potion.wheatyhopsjuice", 750))
                .duration(6 * SECONDS + 8 * TICKS)
                .eut(4)
                .addTo(brewingRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L))
                .fluidInputs(getFluidStack("potion.tea", 750))
                .fluidOutputs(getFluidStack("potion.sweettea", 750))
                .duration(6 * SECONDS + 8 * TICKS)
                .eut(4)
                .addTo(brewingRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L))
                .fluidInputs(getFluidStack("potion.latte", 750))
                .fluidOutputs(getFluidStack("potion.sweetlatte", 750))
                .duration(6 * SECONDS + 8 * TICKS)
                .eut(4)
                .addTo(brewingRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L))
                .fluidInputs(getFluidStack("potion.sweetlatte", 750))
                .fluidOutputs(getFluidStack("potion.sweetjesuslatte", 750))
                .duration(6 * SECONDS + 8 * TICKS)
                .eut(4)
                .addTo(brewingRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L))
                .fluidInputs(getFluidStack("potion.lemonjuice", 750))
                .fluidOutputs(getFluidStack("potion.lemonade", 750))
                .duration(6 * SECONDS + 8 * TICKS)
                .eut(4)
                .addTo(brewingRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L))
                .fluidInputs(getFluidStack("potion.coffee", 750))
                .fluidOutputs(getFluidStack("potion.sweetcoffee", 750))
                .duration(6 * SECONDS + 8 * TICKS)
                .eut(4)
                .addTo(brewingRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L))
                .fluidInputs(getFluidStack("potion.darkchocolatemilk", 750))
                .fluidOutputs(getFluidStack("potion.chocolatemilk", 750))
                .duration(6 * SECONDS + 8 * TICKS)
                .eut(4)
                .addTo(brewingRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Ice, 1L))
                .fluidInputs(getFluidStack("potion.tea", 750))
                .fluidOutputs(getFluidStack("potion.icetea", 750))
                .duration(6 * SECONDS + 8 * TICKS)
                .eut(4)
                .addTo(brewingRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Gunpowder, 1L))
                .fluidInputs(getFluidStack("potion.lemonade", 750))
                .fluidOutputs(getFluidStack("potion.cavejohnsonsgrenadejuice", 750))
                .duration(6 * SECONDS + 8 * TICKS)
                .eut(4)
                .addTo(brewingRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L))
                .fluidInputs(getFluidStack("potion.mundane", 750))
                .fluidOutputs(getFluidStack("potion.purpledrink", 750))
                .duration(6 * SECONDS + 8 * TICKS)
                .eut(4)
                .addTo(brewingRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(Items.fermented_spider_eye, 1, 0))
                .fluidInputs(getFluidStack("potion.mundane", 750))
                .fluidOutputs(getFluidStack("potion.weakness", 750))
                .duration(6 * SECONDS + 8 * TICKS)
                .eut(4)
                .addTo(brewingRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(Items.fermented_spider_eye, 1, 0))
                .fluidInputs(getFluidStack("potion.thick", 750))
                .fluidOutputs(getFluidStack("potion.weakness", 750))
                .duration(6 * SECONDS + 8 * TICKS)
                .eut(4)
                .addTo(brewingRecipes);
        }

        // biomass recipes
        {
            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Forestry.ID, "fertilizerBio", 4L, 0))
                .fluidInputs(Materials.Water.getFluid(750))
                .fluidOutputs(Materials.Biomass.getFluid(750))
                .duration(6 * SECONDS + 8 * TICKS)
                .eut(4)
                .addTo(brewingRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Forestry.ID, "mulch", 16L, 0))
                .fluidInputs(GTModHandler.getDistilledWater(750L))
                .fluidOutputs(Materials.Biomass.getFluid(750))
                .duration(6 * SECONDS + 8 * TICKS)
                .eut(4)
                .addTo(brewingRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Forestry.ID, "mulch", 8L, 0))
                .fluidInputs(getFluidStack("juice", 500))
                .fluidOutputs(Materials.Biomass.getFluid(750))
                .duration(6 * SECONDS + 8 * TICKS)
                .eut(4)
                .addTo(brewingRecipes);
        }

        // ic2 biomass recipes
        {
            GTValues.RA.stdBuilder()
                .itemInputs(GTModHandler.getIC2Item("biochaff", 1))
                .fluidInputs(Materials.Water.getFluid(1_000))
                .fluidOutputs(getFluidStack("ic2biomass", 1_000))
                .duration(8 * SECONDS + 10 * TICKS)
                .eut(4)
                .addTo(brewingRecipes);

            // Would add 2 different amount of water input if IC2 isn't loaded
            if (IndustrialCraft2.isModLoaded()) {
                GTValues.RA.stdBuilder()
                    .itemInputs(GTModHandler.getIC2Item("biochaff", 1))
                    .fluidInputs(GTModHandler.getDistilledWater(500L))
                    .fluidOutputs(getFluidStack("ic2biomass", 1_000))
                    .duration(10 * TICKS)
                    .eut((int) TierEU.RECIPE_LV)
                    .addTo(brewingRecipes);
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
            this.addPotionRecipes("speed", GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L));
            this.addPotionRecipes("strength", GTOreDictUnificator.get(OrePrefixes.dust, Materials.Blaze, 1L));
        }
    }

    public void addPotionRecipes(String aName, ItemStack aItem) {
        // normal
        GTValues.RA.stdBuilder()
            .itemInputs(aItem)
            .fluidInputs(getFluidStack("potion.awkward", 750))
            .fluidOutputs(getFluidStack("potion." + aName, 750))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(4)
            .addTo(brewingRecipes);

        // strong
        if (aName.equals("regen") || aName.equals("speed")
            || aName.equals("health")
            || aName.equals("strength")
            || aName.equals("poison")) {
            GTValues.RA.stdBuilder()
                .itemInputs(aItem)
                .fluidInputs(getFluidStack("potion.thick", 750))
                .fluidOutputs(getFluidStack("potion." + aName + ".strong", 750))
                .duration(6 * SECONDS + 8 * TICKS)
                .eut(4)
                .addTo(brewingRecipes);
        }

        // long
        if (!aName.equals("health")) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L))
                .fluidInputs(getFluidStack("potion." + aName, 750))
                .fluidOutputs(getFluidStack("potion." + aName + ".long", 750))
                .duration(6 * SECONDS + 8 * TICKS)
                .eut(4)
                .addTo(brewingRecipes);
        }

        MixerRecipes.addMixerPotionRecipes(aName);
    }
}
