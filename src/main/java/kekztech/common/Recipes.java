package kekztech.common;

import static gregtech.api.enums.Mods.*;
import static gregtech.api.util.GTModHandler.getModItem;
import static thaumcraft.api.ThaumcraftApi.addArcaneCraftingRecipe;

import java.util.HashMap;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;
import kekztech.KekzCore;
import kekztech.common.recipeLoaders.AlloySmelter;
import kekztech.common.recipeLoaders.Assembler;
import kekztech.common.recipeLoaders.AssemblyLine;
import kekztech.common.recipeLoaders.ChemicalReactor;
import kekztech.common.recipeLoaders.Crafting;
import kekztech.common.recipeLoaders.FormingPress;
import kekztech.common.recipeLoaders.Mixer;
import kekztech.common.recipeLoaders.ResearchableAssemblyLine;
import kekztech.common.recipeLoaders.Unpackager;
import thaumcraft.api.ItemApi;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.crafting.ShapedArcaneRecipe;

public class Recipes {

    public static final HashMap<String, InfusionRecipe> infusionRecipes = new HashMap<>();
    public static final HashMap<String, ShapedArcaneRecipe> shapedArcaneRecipes = new HashMap<>();

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

        if (Mods.Thaumcraft.isModLoaded()) {
            registerRecipes_Jars();
        }

        KekzCore.LOGGER.info("Finished registering recipes");
    }

    private static void registerRecipes_Jars() {

        // Thaumium Reinforced Jar
        shapedArcaneRecipes.put(
            "THAUMIUMREINFORCEDJAR",
            addArcaneCraftingRecipe(
                "THAUMIUMREINFORCEDJAR",
                new ItemStack(Blocks.jarThaumiumReinforced, 1, 0),
                new AspectList().add(Aspect.WATER, 30),
                "MTM",
                "TJT",
                "GGG",
                "M",
                getModItem(Botania.ID, "manaGlass", 1, 0),
                "T",
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Thaumium, 1L),
                "J",
                getModItem(Thaumcraft.ID, "blockJar", 1, 0),
                "G",
                getModItem(Minecraft.ID, "glass", 1, 0)));
        // Thaumium Reinforced Void Jar
        final ItemStack[] recipe_voidjarupgrade = {
            GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Obsidian, 1),
            GTOreDictUnificator.get(OrePrefixes.dust, Materials.Blaze, 1),
            GTOreDictUnificator.get(OrePrefixes.plate, Materials.EnderEye, 1), ItemApi.getItem("itemNugget", 5) };
        final AspectList aspects_voidjarupgrade = new AspectList().add(Aspect.VOID, 14)
            .add(Aspect.MAGIC, 14)
            .add(Aspect.ENTROPY, 14)
            .add(Aspect.WATER, 14);
        infusionRecipes.put(
            "THAUMIUMREINFORCEDVOIDJAR",
            ThaumcraftApi.addInfusionCraftingRecipe(
                "THAUMIUMREINFORCEDJAR",
                new ItemStack(Blocks.jarThaumiumReinforced, 1, 3),
                2,
                aspects_voidjarupgrade,
                new ItemStack(Blocks.jarThaumiumReinforced, 1, 0),
                recipe_voidjarupgrade));

        final ItemStack[] recipe_jarichor = { getModItem(ThaumicTinkerer.ID, "kamiResource", 1, 0),
            GTOreDictUnificator.get(OrePrefixes.plate, Materials.Diamond, 1),
            new ItemStack(net.minecraft.init.Blocks.glass_pane),
            GTOreDictUnificator.get(OrePrefixes.foil, Materials.Osmiridium, 1),
            GTOreDictUnificator.get(OrePrefixes.plate, Materials.Diamond, 1),
            new ItemStack(net.minecraft.init.Blocks.glass_pane),
            GTOreDictUnificator.get(OrePrefixes.gemExquisite, Materials.Diamond, 1),
            GTOreDictUnificator.get(OrePrefixes.plate, Materials.Diamond, 1),
            new ItemStack(net.minecraft.init.Blocks.glass_pane),
            GTOreDictUnificator.get(OrePrefixes.foil, Materials.Osmiridium, 1),
            GTOreDictUnificator.get(OrePrefixes.plate, Materials.Diamond, 1),
            new ItemStack(net.minecraft.init.Blocks.glass_pane), };
        final AspectList aspects_jarichor = new AspectList().add(Aspect.ARMOR, 256)
            .add(Aspect.ELDRITCH, 128)
            .add(Aspect.ORDER, 128)
            .add(Aspect.WATER, 128)
            .add(Aspect.GREED, 64)
            .add(Aspect.VOID, 64)
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
