package common;

import java.util.HashMap;

import common.recipeLoaders.AlloySmelter;
import common.recipeLoaders.Assembler;
import common.recipeLoaders.AssemblyLine;
import common.recipeLoaders.ChemicalReactor;
import common.recipeLoaders.Crafting;
import common.recipeLoaders.FormingPress;
import common.recipeLoaders.Mixer;
import common.recipeLoaders.ResearchableAssemblyLine;
import common.recipeLoaders.Unpackager;
import kekztech.KekzCore;

import net.minecraft.item.ItemStack;

import thaumcraft.api.ItemApi;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.InfusionRecipe;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.*;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;

public class Recipes {

    public static final HashMap<String, InfusionRecipe> infusionRecipes = new HashMap<>();

    public static void postInit() {
        KekzCore.LOGGER.info("Registering recipes...");

        new AlloySmelter().run();
        new Assembler().run();
        new AssemblyLine().run();
        new ChemicalReactor().run();
        new Crafting().run();
        new FormingPress().run();
        new Mixer().run();
        new ResearchableAssemblyLine().run();
        new Unpackager().run();

        registerRecipes_Jars();

        KekzCore.LOGGER.info("Finished registering recipes");
    }

    private static void registerRecipes_Jars() {

        // Thaumium Reinforced Jar
        final ItemStack[] recipe_jarthaumiumreinforced = {
                GameRegistry.makeItemStack("Thaumcraft:ItemResource", 15, 1, null),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Thaumium, 1),
                new ItemStack(net.minecraft.init.Blocks.glass_pane),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Thaumium, 1),
                new ItemStack(net.minecraft.init.Blocks.glass_pane),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Titanium, 1),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Thaumium, 1),
                new ItemStack(net.minecraft.init.Blocks.glass_pane),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Thaumium, 1),
                new ItemStack(net.minecraft.init.Blocks.glass_pane), };
        final AspectList aspects_jarthaumiumreinforced = new AspectList().add(Aspect.ARMOR, 64).add(Aspect.ORDER, 32)
                .add(Aspect.WATER, 32).add(Aspect.GREED, 16).add(Aspect.VOID, 16).add(Aspect.AIR, 8);
        infusionRecipes.put(
                "THAUMIUMREINFORCEDJAR",
                ThaumcraftApi.addInfusionCraftingRecipe(
                        "THAUMIUMREINFORCEDJAR",
                        new ItemStack(Blocks.jarThaumiumReinforced, 1, 0),
                        5,
                        aspects_jarthaumiumreinforced,
                        ItemApi.getBlock("blockJar", 0),
                        recipe_jarthaumiumreinforced));
        // Thaumium Reinforced Void Jar
        final ItemStack[] recipe_voidjarupgrade = {
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Obsidian, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Blaze, 1),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.EnderEye, 1), ItemApi.getItem("itemNugget", 5) };
        final AspectList aspects_voidjarupgrade = new AspectList().add(Aspect.VOID, 14).add(Aspect.MAGIC, 14)
                .add(Aspect.ENTROPY, 14).add(Aspect.WATER, 14);
        infusionRecipes.put(
                "THAUMIUMREINFORCEDVOIDJAR",
                ThaumcraftApi.addInfusionCraftingRecipe(
                        "THAUMIUMREINFORCEDJAR",
                        new ItemStack(Blocks.jarThaumiumReinforced, 1, 3),
                        2,
                        aspects_voidjarupgrade,
                        new ItemStack(Blocks.jarThaumiumReinforced, 1, 0),
                        recipe_voidjarupgrade));

        final ItemStack[] recipe_jarichor = { GT_ModHandler.getModItem("ThaumicTinkerer", "kamiResource", 1, 0),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Diamond, 1),
                new ItemStack(net.minecraft.init.Blocks.glass_pane),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Osmiridium, 1),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Diamond, 1),
                new ItemStack(net.minecraft.init.Blocks.glass_pane),
                GT_OreDictUnificator.get(OrePrefixes.gemExquisite, Materials.Diamond, 1),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Diamond, 1),
                new ItemStack(net.minecraft.init.Blocks.glass_pane),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Osmiridium, 1),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Diamond, 1),
                new ItemStack(net.minecraft.init.Blocks.glass_pane), };
        final AspectList aspects_jarichor = new AspectList().add(Aspect.ARMOR, 256).add(Aspect.ELDRITCH, 128)
                .add(Aspect.ORDER, 128).add(Aspect.WATER, 128).add(Aspect.GREED, 64).add(Aspect.VOID, 64)
                .add(Aspect.AIR, 32);
        infusionRecipes.put(
                "ICHORJAR",
                ThaumcraftApi.addInfusionCraftingRecipe(
                        "ICHORJAR",
                        new ItemStack(Blocks.jarIchor, 1, 0),
                        15,
                        aspects_jarichor,
                        ItemApi.getBlock("blockJar", 0),
                        recipe_jarichor));
        // Ichor Void Jar
        infusionRecipes.put(
                "ICHORVOIDJAR",
                ThaumcraftApi.addInfusionCraftingRecipe(
                        "ICHORJAR",
                        new ItemStack(Blocks.jarIchor, 1, 3),
                        5,
                        aspects_voidjarupgrade,
                        new ItemStack(Blocks.jarIchor, 1, 0),
                        recipe_voidjarupgrade));
    }

}
