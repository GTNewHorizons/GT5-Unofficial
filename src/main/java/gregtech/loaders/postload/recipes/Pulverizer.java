package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.AppliedEnergistics2;
import static gregtech.api.enums.Mods.BiomesOPlenty;
import static gregtech.api.enums.Mods.ProjectRedExploration;
import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.util.GTModHandler.getIC2Item;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.recipe.RecipeCategories;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.material.MaterialsElements;
import gtnhlanth.common.register.WerkstoffMaterialPool;

@SuppressWarnings({ "PointlessArithmeticExpression" })
public class Pulverizer implements Runnable {

    @Override
    public void run() {
        // recycling Long Distance Pipes
        {
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Long_Distance_Pipeline_Fluid.get(1))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 19))
                .duration(15 * SECONDS)
                .eut(4)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Long_Distance_Pipeline_Item.get(1))
                .itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Tin, 12),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 7))
                .duration(15 * SECONDS)
                .eut(4)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Long_Distance_Pipeline_Fluid_Pipe.get(1))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Steel, 2))
                .duration(10 * TICKS)
                .eut(4)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Long_Distance_Pipeline_Item_Pipe.get(1))
                .itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Tin, 1),
                    GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Steel, 1))
                .duration(10 * TICKS)
                .eut(4)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);
        }

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.quartz_block, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.NetherQuartz, 4))
            .duration(19 * SECONDS + 12 * TICKS)
            .eut(4)
            .recipeCategory(RecipeCategories.maceratorRecycling)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.quartz_block, 1, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.NetherQuartz, 4))
            .duration(19 * SECONDS + 12 * TICKS)
            .eut(4)
            .recipeCategory(RecipeCategories.maceratorRecycling)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.quartz_block, 1, 2))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.NetherQuartz, 4))
            .duration(19 * SECONDS + 12 * TICKS)
            .eut(4)
            .recipeCategory(RecipeCategories.maceratorRecycling)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.block, Materials.Ichorium, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Ichorium, 2))
            .duration(44 * SECONDS + 2 * TICKS)
            .eut(4)
            .recipeCategory(RecipeCategories.maceratorRecycling)
            .addTo(maceratorRecipes);

        // marble dust, stone dust

        for (ItemStack marble : OreDictionary.getOres("blockMarble")) {
            GTValues.RA.stdBuilder()
                .itemInputs(marble)
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Marble, 1))
                .duration(8 * SECONDS)
                .eut(4)
                .addTo(maceratorRecipes);
        }

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Thaumcraft.ID, "ItemResource", 1, 18))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Gold, 1))
            .duration(1 * SECONDS + 1 * TICKS)
            .eut(4)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.reeds, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(2)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_Cupronickel.get(1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Cupronickel, 8),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Tin, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.QuartzSand, 2))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(80)
            .recipeCategory(RecipeCategories.maceratorRecycling)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_Kanthal.get(1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Kanthal, 8),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Cupronickel, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.QuartzSand, 3))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(80)
            .recipeCategory(RecipeCategories.maceratorRecycling)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_Nichrome.get(1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Nichrome, 8),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Kanthal, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.QuartzSand, 4))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(80)
            .recipeCategory(RecipeCategories.maceratorRecycling)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_TungstenSteel.get(1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.TPV, 8),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Nichrome, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.QuartzSand, 5))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(80)
            .recipeCategory(RecipeCategories.maceratorRecycling)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_HSSG.get(1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.HSSG, 8),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.TPV, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.QuartzSand, 6))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(80)
            .recipeCategory(RecipeCategories.maceratorRecycling)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_HSSS.get(1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.HSSS, 8),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.HSSG, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.QuartzSand, 7))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(80)
            .recipeCategory(RecipeCategories.maceratorRecycling)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_Naquadah.get(1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 8),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.HSSS, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.QuartzSand, 8))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(80)
            .recipeCategory(RecipeCategories.maceratorRecycling)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_NaquadahAlloy.get(1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.NaquadahAlloy, 8),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.QuartzSand, 9))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(80)
            .recipeCategory(RecipeCategories.maceratorRecycling)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_Trinium.get(1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Trinium, 8),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.NaquadahAlloy, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.PrismaticNaquadah, 16),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Netherite, 1))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(80)
            .recipeCategory(RecipeCategories.maceratorRecycling)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_ElectrumFlux.get(1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.ElectrumFlux, 8),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Trinium, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.PrismaticNaquadah, 24),
                GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Netherite, 6))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(80)
            .recipeCategory(RecipeCategories.maceratorRecycling)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_AwakenedDraconium.get(1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.DraconiumAwakened, 8),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.ElectrumFlux, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.PrismaticNaquadah, 32),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Netherite, 2))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(80)
            .recipeCategory(RecipeCategories.maceratorRecycling)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_Infinity.get(1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Infinity, 9L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.DraconiumAwakened, 4L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.PrismaticNaquadah, 48),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Netherite, 3))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .recipeCategory(RecipeCategories.maceratorRecycling)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_Hypogen.get(1))
            .itemOutputs(
                MaterialsElements.STANDALONE.HYPOGEN.getDust(9),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Infinity, 4L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.PrismaticNaquadah, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Netherite, 4))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .recipeCategory(RecipeCategories.maceratorRecycling)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_Eternal.get(1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.SpaceTime, 9L),
                MaterialsElements.STANDALONE.HYPOGEN.getDust(4),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.PrismaticNaquadah, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Netherite, 8))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .recipeCategory(RecipeCategories.maceratorRecycling)
            .addTo(maceratorRecipes);

        if (Railcraft.isModLoaded()) {
            // recycling RC Tanks
            // Iron

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 0))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 2))
                .duration(15 * SECONDS)
                .eut(2)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 1))
                .itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 1),
                    GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3))
                .duration(15 * SECONDS)
                .eut(2)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 2))
                .itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Bronze, 12),
                    GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Iron, 3))
                .duration(15 * SECONDS)
                .eut(2)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            // Steel

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 13))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 2))
                .duration(15 * SECONDS)
                .eut(2)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 14))
                .itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 1),
                    GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3))
                .duration(15 * SECONDS)
                .eut(2)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 15))
                .itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 12),
                    GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Steel, 3))
                .duration(15 * SECONDS)
                .eut(2)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            // Aluminium

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 0))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 2))
                .duration(22 * SECONDS + 10 * TICKS)
                .eut(8)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 1))
                .itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 1),
                    GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3))
                .duration(22 * SECONDS + 10 * TICKS)
                .eut(8)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 2))
                .itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Polyethylene, 12),
                    GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Aluminium, 3))
                .duration(22 * SECONDS + 10 * TICKS)
                .eut(8)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            // Stainless Steel

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 3))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.StainlessSteel, 2))
                .duration(30 * SECONDS)
                .eut(16)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 4))
                .itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.StainlessSteel, 1),
                    GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3))
                .duration(30 * SECONDS)
                .eut(16)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 5))
                .itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.StainlessSteel, 12),
                    GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.StainlessSteel, 3))
                .duration(30 * SECONDS)
                .eut(16)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            // Titanium

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 6))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Titanium, 2))
                .duration(30 * SECONDS)
                .eut(30)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 7))
                .itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Titanium, 1),
                    GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3))
                .duration(30 * SECONDS)
                .eut(30)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 8))
                .itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Titanium, 12),
                    GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Titanium, 3))
                .duration(30 * SECONDS)
                .eut(30)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            // Tungesten Steel

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 9))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.TungstenSteel, 2))
                .duration(30 * SECONDS)
                .eut(30)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 10))
                .itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.TungstenSteel, 1),
                    GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3))
                .duration(30 * SECONDS)
                .eut(30)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 11))
                .itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.TungstenSteel, 12),
                    GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.TungstenSteel, 3))
                .duration(30 * SECONDS)
                .eut(30)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            // Palladium

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 12))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Palladium, 2))
                .duration(37 * SECONDS + 10 * TICKS)
                .eut(64)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 13))
                .itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Palladium, 1),
                    GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3))
                .duration(37 * SECONDS + 10 * TICKS)
                .eut(64)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 14))
                .itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.NiobiumTitanium, 12),
                    GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Chrome, 3))
                .duration(37 * SECONDS + 10 * TICKS)
                .eut(64)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            // Iridium

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 0))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Iridium, 2))
                .duration(45 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 1))
                .itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Iridium, 1),
                    GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3))
                .duration(45 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 2))
                .itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Enderium, 12),
                    GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Iridium, 3))
                .duration(45 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            // Osmium

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 3))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Osmium, 2))
                .duration(52 * SECONDS + 10 * TICKS)
                .eut(256)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 4))
                .itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Osmium, 1),
                    GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3))
                .duration(52 * SECONDS + 10 * TICKS)
                .eut(256)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 5))
                .itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Naquadah, 12),
                    GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Osmium, 3))
                .duration(52 * SECONDS + 10 * TICKS)
                .eut(256)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            // Neutronium

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 6))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Neutronium, 2))
                .duration(60 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 7))
                .itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Neutronium, 1),
                    GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3))
                .duration(60 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 8))
                .itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Neutronium, 12),
                    GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Neutronium, 3))
                .duration(60 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);
        }

        if (AppliedEnergistics2.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(AppliedEnergistics2.ID, "tile.BlockSkyStone", 1L, 32767))
                .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 45))
                .duration(20 * SECONDS)
                .eut(2)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(AppliedEnergistics2.ID, "tile.BlockSkyChest", 1L, 32767))
                .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 8L, 45))
                .duration(20 * SECONDS)
                .eut(2)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);
        }

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.blaze_rod, 1))
            .itemOutputs(new ItemStack(Items.blaze_powder, 3), new ItemStack(Items.blaze_powder, 1))
            .outputChances(10000, 5000)
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.rod, Materials.Blizz, 1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Blizz, 3),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Blizz, 1))
            .outputChances(10000, 5000)
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.web, 1, 0))
            .itemOutputs(new ItemStack(Items.string, 1), new ItemStack(Items.string, 1))
            .outputChances(10000, 5000)
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.red_mushroom, 1, 32767))
            .itemOutputs(ItemList.IC2_Grin_Powder.get(1L))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.poisonous_potato, 1))
            .itemOutputs(ItemList.IC2_Grin_Powder.get(1L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.spider_eye, 1))
            .itemOutputs(ItemList.IC2_Grin_Powder.get(1L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.bone, 1))
            .itemOutputs(new ItemStack(Items.dye, 4, 15))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.IC2_EnergyCrystal.get(1))
            .itemOutputs(ItemList.IC2_Energium_Dust.get(9L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getIC2Item("biochaff", 1))
            .itemOutputs(new ItemStack(Blocks.dirt, 1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.quartz_stairs, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.NetherQuartz, 6))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .recipeCategory(RecipeCategories.maceratorRecycling)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.item_frame, 1, 32767))
            .itemOutputs(
                new ItemStack(Items.leather, 1),
                GTOreDictUnificator.getDust(Materials.Wood, OrePrefixes.stick.getMaterialAmount() * 4L))
            .outputChances(10000, 9500)
            .duration(20 * SECONDS)
            .eut(2)
            .recipeCategory(RecipeCategories.maceratorRecycling)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.bow, 1, 0))
            .itemOutputs(
                new ItemStack(Items.string, 3),
                GTOreDictUnificator.getDust(Materials.Wood, OrePrefixes.stick.getMaterialAmount() * 3))
            .outputChances(10000, 9500)
            .duration(20 * SECONDS)
            .eut(2)
            .recipeCategory(RecipeCategories.maceratorRecycling)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Brick.getIngots(1))
            .itemOutputs(Materials.Brick.getDustSmall(1))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.brick_stairs, 1, 0))
            .itemOutputs(Materials.Brick.getDustSmall(6))
            .duration(20 * SECONDS)
            .eut(2)
            .recipeCategory(RecipeCategories.maceratorRecycling)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Machine_Bricked_BlastFurnace.get(1))
            .itemOutputs(Materials.Brick.getDust(8), Materials.Iron.getDust(1))
            .duration(20 * SECONDS)
            .eut(2)
            .recipeCategory(RecipeCategories.maceratorRecycling)
            .addTo(maceratorRecipes);

        if (BiomesOPlenty.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(BiomesOPlenty.ID, "gemOre", 1, 5))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Olivine, 9))
                .duration(Materials.Olivine.getMass() * 9 * TICKS)
                .eut(4)
                .addTo(maceratorRecipes);
        }

        if (ProjectRedExploration.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(ProjectRedExploration.ID, "projectred.exploration.stone", 1, 7))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Olivine, 9))
                .duration(Materials.Olivine.getMass() * 9 * TICKS)
                .eut(4)
                .addTo(maceratorRecipes);
        }

        // LUAG gems
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemChipped, 1))
            .itemOutputs(WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.dustSmall, 1))
            .duration(25 * TICKS)
            .eut(4)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemFlawed, 1))
            .itemOutputs(WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.dustSmall, 2))
            .duration(50 * TICKS)
            .eut(4)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gem, 1))
            .itemOutputs(WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.dust, 1))
            .duration(5 * SECONDS)
            .eut(4)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemFlawless, 1))
            .itemOutputs(WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.dust, 2))
            .duration(10 * SECONDS)
            .eut(4)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 1))
            .itemOutputs(WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.dust, 4))
            .duration(20 * SECONDS)
            .eut(4)
            .addTo(maceratorRecipes);
    }
}
