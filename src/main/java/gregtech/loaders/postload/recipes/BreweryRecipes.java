package gregtech.loaders.postload.recipes;

import static gregtech.api.util.GT_ModHandler.getModItem;
import static net.minecraftforge.fluids.FluidRegistry.getFluidStack;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import gregtech.api.enums.*;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.loaders.postload.GT_MachineRecipeLoader;

public class BreweryRecipes implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Talc, 1L),
                FluidRegistry.getFluid("creosote"),
                FluidRegistry.getFluid("lubricant"),
                false);
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Soapstone, 1L),
                FluidRegistry.getFluid("creosote"),
                FluidRegistry.getFluid("lubricant"),
                false);
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L),
                FluidRegistry.getFluid("creosote"),
                FluidRegistry.getFluid("lubricant"),
                false);
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Talc, 1L),
                FluidRegistry.getFluid("seedoil"),
                FluidRegistry.getFluid("lubricant"),
                false);
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Soapstone, 1L),
                FluidRegistry.getFluid("seedoil"),
                FluidRegistry.getFluid("lubricant"),
                false);
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L),
                FluidRegistry.getFluid("seedoil"),
                FluidRegistry.getFluid("lubricant"),
                false);
        for (Fluid tFluid : new Fluid[] { FluidRegistry.WATER, GT_ModHandler.getDistilledWater(1L).getFluid() }) {
            GT_Values.RA.addBrewingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Milk, 1L),
                    tFluid,
                    FluidRegistry.getFluid("milk"),
                    false);
            GT_Values.RA.addBrewingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wheat, 1L),
                    tFluid,
                    FluidRegistry.getFluid("potion.wheatyjuice"),
                    false);
            GT_Values.RA.addBrewingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Potassium, 1L),
                    tFluid,
                    FluidRegistry.getFluid("potion.mineralwater"),
                    false);
            GT_Values.RA.addBrewingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L),
                    tFluid,
                    FluidRegistry.getFluid("potion.mineralwater"),
                    false);
            GT_Values.RA.addBrewingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 1L),
                    tFluid,
                    FluidRegistry.getFluid("potion.mineralwater"),
                    false);
            GT_Values.RA.addBrewingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnesium, 1L),
                    tFluid,
                    FluidRegistry.getFluid("potion.mineralwater"),
                    false);
            GT_Values.RA.addBrewingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glowstone, 1L),
                    tFluid,
                    FluidRegistry.getFluid("potion.thick"),
                    false);
            GT_Values.RA.addBrewingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L),
                    tFluid,
                    FluidRegistry.getFluid("potion.mundane"),
                    false);
            GT_Values.RA.addBrewingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L),
                    tFluid,
                    FluidRegistry.getFluid("potion.mundane"),
                    false);
            GT_Values.RA.addBrewingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Blaze, 1L),
                    tFluid,
                    FluidRegistry.getFluid("potion.mundane"),
                    false);
            GT_Values.RA.addBrewingRecipe(
                    new ItemStack(Items.magma_cream, 1, 0),
                    tFluid,
                    FluidRegistry.getFluid("potion.mundane"),
                    false);
            GT_Values.RA.addBrewingRecipe(
                    new ItemStack(Items.fermented_spider_eye, 1, 0),
                    tFluid,
                    FluidRegistry.getFluid("potion.mundane"),
                    false);
            GT_Values.RA.addBrewingRecipe(
                    new ItemStack(Items.spider_eye, 1, 0),
                    tFluid,
                    FluidRegistry.getFluid("potion.mundane"),
                    false);
            GT_Values.RA.addBrewingRecipe(
                    new ItemStack(Items.speckled_melon, 1, 0),
                    tFluid,
                    FluidRegistry.getFluid("potion.mundane"),
                    false);
            GT_Values.RA.addBrewingRecipe(
                    new ItemStack(Items.ghast_tear, 1, 0),
                    tFluid,
                    FluidRegistry.getFluid("potion.mundane"),
                    false);
            GT_Values.RA.addBrewingRecipe(
                    new ItemStack(Items.nether_wart, 1, 0),
                    tFluid,
                    FluidRegistry.getFluid("potion.awkward"),
                    false);
            GT_Values.RA.addBrewingRecipe(
                    new ItemStack(Blocks.red_mushroom, 1, 0),
                    tFluid,
                    FluidRegistry.getFluid("potion.poison"),
                    false);
            GT_Values.RA.addBrewingRecipe(
                    new ItemStack(Items.fish, 1, 3),
                    tFluid,
                    FluidRegistry.getFluid("potion.poison.strong"),
                    true);
            GT_Values.RA.addBrewingRecipe(
                    ItemList.IC2_Grin_Powder.get(1L),
                    tFluid,
                    FluidRegistry.getFluid("potion.poison.strong"),
                    false);
            GT_Values.RA.addBrewingRecipe(
                    new ItemStack(Items.reeds, 1, 0),
                    tFluid,
                    FluidRegistry.getFluid("potion.reedwater"),
                    false);
            GT_Values.RA.addBrewingRecipe(
                    new ItemStack(Items.apple, 1, 0),
                    tFluid,
                    FluidRegistry.getFluid("potion.applejuice"),
                    false);
            GT_Values.RA.addBrewingRecipe(
                    new ItemStack(Items.golden_apple, 1, 0),
                    tFluid,
                    FluidRegistry.getFluid("potion.goldenapplejuice"),
                    true);
            GT_Values.RA.addBrewingRecipe(
                    new ItemStack(Items.golden_apple, 1, 1),
                    tFluid,
                    FluidRegistry.getFluid("potion.idunsapplejuice"),
                    true);
            GT_Values.RA.addBrewingRecipe(
                    ItemList.IC2_Hops.get(1L),
                    tFluid,
                    FluidRegistry.getFluid("potion.hopsjuice"),
                    false);
            GT_Values.RA.addBrewingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coffee, 1L),
                    tFluid,
                    FluidRegistry.getFluid("potion.darkcoffee"),
                    false);
            GT_Values.RA.addBrewingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chili, 1L),
                    tFluid,
                    FluidRegistry.getFluid("potion.chillysauce"),
                    false);
        }
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chili, 1L),
                FluidRegistry.getFluid("potion.chillysauce"),
                FluidRegistry.getFluid("potion.hotsauce"),
                false);
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chili, 1L),
                FluidRegistry.getFluid("potion.hotsauce"),
                FluidRegistry.getFluid("potion.diabolosauce"),
                true);
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Chili, 1L),
                FluidRegistry.getFluid("potion.diabolosauce"),
                FluidRegistry.getFluid("potion.diablosauce"),
                true);
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coffee, 1L),
                FluidRegistry.getFluid("milk"),
                FluidRegistry.getFluid("potion.coffee"),
                false);
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Cocoa, 1L),
                FluidRegistry.getFluid("milk"),
                FluidRegistry.getFluid("potion.darkchocolatemilk"),
                false);
        GT_Values.RA.addBrewingRecipe(
                ItemList.IC2_Hops.get(1L),
                FluidRegistry.getFluid("potion.wheatyjuice"),
                FluidRegistry.getFluid("potion.wheatyhopsjuice"),
                false);
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wheat, 1L),
                FluidRegistry.getFluid("potion.hopsjuice"),
                FluidRegistry.getFluid("potion.wheatyhopsjuice"),
                false);
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L),
                FluidRegistry.getFluid("potion.tea"),
                FluidRegistry.getFluid("potion.sweettea"),
                true);
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L),
                FluidRegistry.getFluid("potion.coffee"),
                FluidRegistry.getFluid("potion.cafeaulait"),
                false);
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L),
                FluidRegistry.getFluid("potion.cafeaulait"),
                FluidRegistry.getFluid("potion.laitaucafe"),
                true);
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L),
                FluidRegistry.getFluid("potion.lemonjuice"),
                FluidRegistry.getFluid("potion.lemonade"),
                false);
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L),
                FluidRegistry.getFluid("potion.darkcoffee"),
                FluidRegistry.getFluid("potion.darkcafeaulait"),
                true);
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L),
                FluidRegistry.getFluid("potion.darkchocolatemilk"),
                FluidRegistry.getFluid("potion.chocolatemilk"),
                false);
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ice, 1L),
                FluidRegistry.getFluid("potion.tea"),
                FluidRegistry.getFluid("potion.icetea"),
                false);
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gunpowder, 1L),
                FluidRegistry.getFluid("potion.lemonade"),
                FluidRegistry.getFluid("potion.cavejohnsonsgrenadejuice"),
                true);
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1L),
                FluidRegistry.getFluid("potion.mundane"),
                FluidRegistry.getFluid("potion.purpledrink"),
                true);
        GT_Values.RA.addBrewingRecipe(
                new ItemStack(Items.fermented_spider_eye, 1, 0),
                FluidRegistry.getFluid("potion.mundane"),
                FluidRegistry.getFluid("potion.weakness"),
                false);
        GT_Values.RA.addBrewingRecipe(
                new ItemStack(Items.fermented_spider_eye, 1, 0),
                FluidRegistry.getFluid("potion.thick"),
                FluidRegistry.getFluid("potion.weakness"),
                false);

        GT_Values.RA.addBrewingRecipe(
                getModItem(GT_MachineRecipeLoader.aTextForestry, "fertilizerBio", 4L, 0),
                FluidRegistry.WATER,
                FluidRegistry.getFluid("biomass"),
                false);
        GT_Values.RA.addBrewingRecipe(
                getModItem(GT_MachineRecipeLoader.aTextForestry, "mulch", 16L, 0),
                GT_ModHandler.getDistilledWater(750L).getFluid(),
                FluidRegistry.getFluid("biomass"),
                false);
        GT_Values.RA.addBrewingRecipeCustom(
                getModItem(GT_MachineRecipeLoader.aTextForestry, "mulch", 8L, 0),
                getFluidStack("juice", 500),
                getFluidStack("biomass", 750),
                128,
                4,
                false);

        GT_Values.RA.addBrewingRecipeCustom(
                GT_ModHandler.getIC2Item("biochaff", 1),
                GT_ModHandler.getWater(1000L),
                getFluidStack("ic2biomass", 1000),
                170,
                4,
                false);
        GT_Values.RA.addBrewingRecipeCustom(
                GT_ModHandler.getIC2Item("biochaff", 1),
                GT_ModHandler.getDistilledWater(500L),
                getFluidStack("ic2biomass", 1000),
                10,
                (int) TierEU.RECIPE_LV,
                false);

        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Talc, 1L),
                FluidRegistry.getFluid("oil"),
                FluidRegistry.getFluid("lubricant"),
                false);
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Soapstone, 1L),
                FluidRegistry.getFluid("oil"),
                FluidRegistry.getFluid("lubricant"),
                false);
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L),
                FluidRegistry.getFluid("oil"),
                FluidRegistry.getFluid("lubricant"),
                false);
        GT_Values.RA.addBrewingRecipeCustom(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Talc, 1L),
                getFluidStack("liquid_light_oil", 750),
                getFluidStack("lubricant", 500),
                128,
                4,
                false);

        GT_Values.RA.addBrewingRecipeCustom(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Soapstone, 1L),
                getFluidStack("liquid_light_oil", 750),
                getFluidStack("lubricant", 500),
                128,
                4,
                false);
        GT_Values.RA.addBrewingRecipeCustom(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L),
                getFluidStack("liquid_light_oil", 750),
                getFluidStack("lubricant", 500),
                128,
                4,
                false);
        GT_Values.RA.addBrewingRecipeCustom(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Talc, 1L),
                getFluidStack("liquid_medium_oil", 750),
                getFluidStack("lubricant", 750),
                128,
                4,
                false);

        GT_Values.RA.addBrewingRecipeCustom(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Soapstone, 1L),
                getFluidStack("liquid_medium_oil", 750),
                getFluidStack("lubricant", 750),
                128,
                4,
                false);
        GT_Values.RA.addBrewingRecipeCustom(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L),
                getFluidStack("liquid_medium_oil", 750),
                getFluidStack("lubricant", 750),
                128,
                4,
                false);
        GT_Values.RA.addBrewingRecipeCustom(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Talc, 1L),
                getFluidStack("liquid_heavy_oil", 500),
                getFluidStack("lubricant", 750),
                64,
                4,
                false);

        GT_Values.RA.addBrewingRecipeCustom(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Soapstone, 1L),
                getFluidStack("liquid_heavy_oil", 500),
                getFluidStack("lubricant", 750),
                64,
                4,
                false);
        GT_Values.RA.addBrewingRecipeCustom(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L),
                getFluidStack("liquid_heavy_oil", 500),
                getFluidStack("lubricant", 750),
                64,
                4,
                false);

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

    public void addPotionRecipes(String aName, ItemStack aItem) {
        // normal
        GT_Values.RA.addBrewingRecipe(
                aItem,
                FluidRegistry.getFluid("potion.awkward"),
                FluidRegistry.getFluid("potion." + aName),
                false);
        // strong
        GT_Values.RA.addBrewingRecipe(
                aItem,
                FluidRegistry.getFluid("potion.thick"),
                FluidRegistry.getFluid("potion." + aName + ".strong"),
                false);
        // long
        GT_Values.RA.addBrewingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L),
                FluidRegistry.getFluid("potion." + aName),
                FluidRegistry.getFluid("potion." + aName + ".long"),
                false);

        MixerRecipes.addMixerPotionRecipes(aName, aItem);
    }
}
