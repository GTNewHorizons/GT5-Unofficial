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

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.MaterialUtils;
import gregtech.api.objects.OreDictItemStack;
import gregtech.api.recipe.RecipeCategories;
import gregtech.api.util.GTOreDictUnificator;

@SuppressWarnings({ "PointlessArithmeticExpression" })
public class Pulverizer implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(new OreDictItemStack("logWood", 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Wood, Shapes.dust, 6),
                MaterialLibAPI.getStack(Materials.Wood, Shapes.dust, 1))
            .outputChances(10000, 8000)
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getIC2Item("iridiumOre", 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.IridiumMetalResidue, Shapes.dust, 1))
            .duration(16 * TICKS)
            .eut(4)
            .recipeCategory(RecipeCategories.maceratorRecycling)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.chainmail_boots, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Steel, 1))
            .duration(MaterialUtils.mass(Materials.Steel) * TICKS)
            .eut(4)
            .recipeCategory(RecipeCategories.maceratorRecycling)
            .addTo(maceratorRecipes);

        // recycling Long Distance Pipes
        {
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Long_Distance_Pipeline_Fluid.get(1))
                .itemOutputs(MaterialLibAPI.getStack(Materials.Steel, Shapes.dust, 19))
                .duration(15 * SECONDS)
                .eut(4)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Long_Distance_Pipeline_Item.get(1))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials.Tin, Shapes.dust, 12),
                    MaterialLibAPI.getStack(Materials.Steel, Shapes.dust, 7))
                .duration(15 * SECONDS)
                .eut(4)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Long_Distance_Pipeline_Fluid_Pipe.get(1))
                .itemOutputs(MaterialLibAPI.getStack(Materials.Steel, Shapes.dustTiny, 2))
                .duration(10 * TICKS)
                .eut(4)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Long_Distance_Pipeline_Item_Pipe.get(1))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials.Tin, Shapes.dustTiny, 1),
                    MaterialLibAPI.getStack(Materials.Steel, Shapes.dustTiny, 1))
                .duration(10 * TICKS)
                .eut(4)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);
        }

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.quartz_block, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.NetherQuartz, Shapes.dust, 4))
            .duration(19 * SECONDS + 12 * TICKS)
            .eut(4)
            .recipeCategory(RecipeCategories.maceratorRecycling)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.quartz_block, 1, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.NetherQuartz, Shapes.dust, 4))
            .duration(19 * SECONDS + 12 * TICKS)
            .eut(4)
            .recipeCategory(RecipeCategories.maceratorRecycling)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.quartz_block, 1, 2))
            .itemOutputs(MaterialLibAPI.getStack(Materials.NetherQuartz, Shapes.dust, 4))
            .duration(19 * SECONDS + 12 * TICKS)
            .eut(4)
            .recipeCategory(RecipeCategories.maceratorRecycling)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.block, Materials.Ichorium, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Ichorium, Shapes.dust, 2))
            .duration(44 * SECONDS + 2 * TICKS)
            .eut(4)
            .recipeCategory(RecipeCategories.maceratorRecycling)
            .addTo(maceratorRecipes);

        // marble dust, stone dust

        GTValues.RA.stdBuilder()
            .itemInputs(new OreDictItemStack("blockMarble", 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Marble, Shapes.dust, 1))
            .duration(8 * SECONDS)
            .eut(4)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Thaumcraft.ID, "ItemResource", 1, 18))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Gold, Shapes.dustTiny, 1))
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
            .itemInputs(new OreDictItemStack("cropChilipepper", 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Chili, Shapes.dust, 1))
            .duration(2 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_Cupronickel.get(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Cupronickel, Shapes.dust, 8),
                MaterialLibAPI.getStack(Materials.Tin, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.QuartzSand, Shapes.dust, 2))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(80)
            .recipeCategory(RecipeCategories.maceratorRecycling)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_Kanthal.get(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Kanthal, Shapes.dust, 8),
                MaterialLibAPI.getStack(Materials.Cupronickel, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.QuartzSand, Shapes.dust, 3))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(80)
            .recipeCategory(RecipeCategories.maceratorRecycling)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_Nichrome.get(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Nichrome, Shapes.dust, 8),
                MaterialLibAPI.getStack(Materials.Kanthal, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.QuartzSand, Shapes.dust, 4))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(80)
            .recipeCategory(RecipeCategories.maceratorRecycling)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_TungstenSteel.get(1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.TPVAlloy, 8),
                MaterialLibAPI.getStack(Materials.Nichrome, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.QuartzSand, Shapes.dust, 5))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(80)
            .recipeCategory(RecipeCategories.maceratorRecycling)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_HSSG.get(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.HSSG, Shapes.dust, 8),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.TPVAlloy, 1),
                MaterialLibAPI.getStack(Materials.QuartzSand, Shapes.dust, 6))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(80)
            .recipeCategory(RecipeCategories.maceratorRecycling)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_HSSS.get(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.HSSS, Shapes.dust, 8),
                MaterialLibAPI.getStack(Materials.HSSG, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.QuartzSand, Shapes.dust, 7))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(80)
            .recipeCategory(RecipeCategories.maceratorRecycling)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_Naquadah.get(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dust, 8),
                MaterialLibAPI.getStack(Materials.HSSS, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.QuartzSand, Shapes.dust, 8))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(80)
            .recipeCategory(RecipeCategories.maceratorRecycling)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_NaquadahAlloy.get(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.NaquadahAlloy, Shapes.dust, 8),
                MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.QuartzSand, Shapes.dust, 9))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(80)
            .recipeCategory(RecipeCategories.maceratorRecycling)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_Trinium.get(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Trinium, Shapes.dust, 8),
                MaterialLibAPI.getStack(Materials.NaquadahAlloy, Shapes.dust, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.prismaticnaquadah, 16),
                MaterialLibAPI.getStack(Materials.Netherite, Shapes.dust, 1))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(80)
            .recipeCategory(RecipeCategories.maceratorRecycling)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_ElectrumFlux.get(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.ElectrumFlux, Shapes.dust, 8),
                MaterialLibAPI.getStack(Materials.Trinium, Shapes.dust, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.prismaticnaquadah, 24),
                MaterialLibAPI.getStack(Materials.Netherite, Shapes.dustSmall, 6))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(80)
            .recipeCategory(RecipeCategories.maceratorRecycling)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_AwakenedDraconium.get(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.DraconiumAwakened, Shapes.dust, 8),
                MaterialLibAPI.getStack(Materials.ElectrumFlux, Shapes.dust, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.prismaticnaquadah, 32),
                MaterialLibAPI.getStack(Materials.Netherite, Shapes.dust, 2))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(80)
            .recipeCategory(RecipeCategories.maceratorRecycling)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_Infinity.get(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Infinity, Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials.DraconiumAwakened, Shapes.dust, 4),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.prismaticnaquadah, 48),
                MaterialLibAPI.getStack(Materials.Netherite, Shapes.dust, 3))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .recipeCategory(RecipeCategories.maceratorRecycling)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_Hypogen.get(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Hypogen, Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials.Infinity, Shapes.dust, 4),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.prismaticnaquadah, 64),
                MaterialLibAPI.getStack(Materials.Netherite, Shapes.dust, 4))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .recipeCategory(RecipeCategories.maceratorRecycling)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_Eternal.get(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.SpaceTime, Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials.Hypogen, Shapes.dust, 4),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.prismaticnaquadah, 64),
                MaterialLibAPI.getStack(Materials.Netherite, Shapes.dust, 8))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .recipeCategory(RecipeCategories.maceratorRecycling)
            .addTo(maceratorRecipes);

        if (Railcraft.isModLoaded()) {
            // recycling RC Tanks
            // Iron

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 0))
                .itemOutputs(MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, 2))
                .duration(15 * SECONDS)
                .eut(2)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 1))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Glass, Shapes.dustTiny, 3))
                .duration(15 * SECONDS)
                .eut(2)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 2))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials.Bronze, Shapes.dust, 12),
                    MaterialLibAPI.getStack(Materials.Iron, Shapes.dustSmall, 3))
                .duration(15 * SECONDS)
                .eut(2)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            // Steel

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 13))
                .itemOutputs(MaterialLibAPI.getStack(Materials.Steel, Shapes.dust, 2))
                .duration(15 * SECONDS)
                .eut(2)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 14))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials.Steel, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Glass, Shapes.dustTiny, 3))
                .duration(15 * SECONDS)
                .eut(2)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 15))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials.Steel, Shapes.dust, 12),
                    MaterialLibAPI.getStack(Materials.Steel, Shapes.dustSmall, 3))
                .duration(15 * SECONDS)
                .eut(2)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            // Aluminium

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 0))
                .itemOutputs(MaterialLibAPI.getStack(Materials.Aluminium, Shapes.dust, 2))
                .duration(22 * SECONDS + 10 * TICKS)
                .eut(TierEU.RECIPE_ULV)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 1))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials.Aluminium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Glass, Shapes.dustTiny, 3))
                .duration(22 * SECONDS + 10 * TICKS)
                .eut(TierEU.RECIPE_ULV)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 2))
                .itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Plastic, 12),
                    MaterialLibAPI.getStack(Materials.Aluminium, Shapes.dustSmall, 3))
                .duration(22 * SECONDS + 10 * TICKS)
                .eut(TierEU.RECIPE_ULV)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            // Stainless Steel

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 3))
                .itemOutputs(MaterialLibAPI.getStack(Materials.StainlessSteel, Shapes.dust, 2))
                .duration(30 * SECONDS)
                .eut(TierEU.RECIPE_LV / 2)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 4))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials.StainlessSteel, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Glass, Shapes.dustTiny, 3))
                .duration(30 * SECONDS)
                .eut(TierEU.RECIPE_LV / 2)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 5))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials.StainlessSteel, Shapes.dust, 12),
                    MaterialLibAPI.getStack(Materials.StainlessSteel, Shapes.dustSmall, 3))
                .duration(30 * SECONDS)
                .eut(TierEU.RECIPE_LV / 2)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            // Titanium

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 6))
                .itemOutputs(MaterialLibAPI.getStack(Materials.Titanium, Shapes.dust, 2))
                .duration(30 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 7))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials.Titanium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Glass, Shapes.dustTiny, 3))
                .duration(30 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 8))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials.Titanium, Shapes.dust, 12),
                    MaterialLibAPI.getStack(Materials.Titanium, Shapes.dustSmall, 3))
                .duration(30 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            // Tungesten Steel

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 9))
                .itemOutputs(MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.dust, 2))
                .duration(30 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 10))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Glass, Shapes.dustTiny, 3))
                .duration(30 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 11))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.dust, 12),
                    MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.dustSmall, 3))
                .duration(30 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            // Palladium

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 12))
                .itemOutputs(MaterialLibAPI.getStack(Materials.Palladium, Shapes.dust, 2))
                .duration(37 * SECONDS + 10 * TICKS)
                .eut(TierEU.RECIPE_MV / 2)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 13))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials.Palladium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Glass, Shapes.dustTiny, 3))
                .duration(37 * SECONDS + 10 * TICKS)
                .eut(TierEU.RECIPE_MV / 2)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 14))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials.NiobiumTitanium, Shapes.dust, 12),
                    MaterialLibAPI.getStack(Materials.Chrome, Shapes.dustSmall, 3))
                .duration(37 * SECONDS + 10 * TICKS)
                .eut(TierEU.RECIPE_MV / 2)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            // Iridium

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 0))
                .itemOutputs(MaterialLibAPI.getStack(Materials.Iridium, Shapes.dust, 2))
                .duration(45 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 1))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials.Iridium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Glass, Shapes.dustTiny, 3))
                .duration(45 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 2))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials.Enderium, Shapes.dust, 12),
                    MaterialLibAPI.getStack(Materials.Iridium, Shapes.dustSmall, 3))
                .duration(45 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            // Osmium

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 3))
                .itemOutputs(MaterialLibAPI.getStack(Materials.Osmium, Shapes.dust, 2))
                .duration(52 * SECONDS + 10 * TICKS)
                .eut(TierEU.RECIPE_HV / 2)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 4))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials.Osmium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Glass, Shapes.dustTiny, 3))
                .duration(52 * SECONDS + 10 * TICKS)
                .eut(TierEU.RECIPE_HV / 2)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 5))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dust, 12),
                    MaterialLibAPI.getStack(Materials.Osmium, Shapes.dustSmall, 3))
                .duration(52 * SECONDS + 10 * TICKS)
                .eut(TierEU.RECIPE_HV / 2)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            // Neutronium

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 6))
                .itemOutputs(MaterialLibAPI.getStack(Materials.Neutronium, Shapes.dust, 2))
                .duration(60 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 7))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials.Neutronium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Glass, Shapes.dustTiny, 3))
                .duration(60 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .recipeCategory(RecipeCategories.maceratorRecycling)
                .addTo(maceratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 8))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials.Neutronium, Shapes.dust, 12),
                    MaterialLibAPI.getStack(Materials.Neutronium, Shapes.dustSmall, 3))
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
                MaterialLibAPI.getStack(Materials.Blizz, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.Blizz, Shapes.dust, 1))
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
            .itemOutputs(MaterialLibAPI.getStack(Materials.NetherQuartz, Shapes.dust, 6))
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
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Brick, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Brick, Shapes.dustSmall, 1))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.brick_stairs, 1, 0))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Brick, Shapes.dustSmall, 6))
            .duration(20 * SECONDS)
            .eut(2)
            .recipeCategory(RecipeCategories.maceratorRecycling)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Machine_Bricked_BlastFurnace.get(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Brick, Shapes.dust, 8),
                MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, 1))
            .duration(20 * SECONDS)
            .eut(2)
            .recipeCategory(RecipeCategories.maceratorRecycling)
            .addTo(maceratorRecipes);

        if (BiomesOPlenty.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(BiomesOPlenty.ID, "gemOre", 1, 5))
                .itemOutputs(MaterialLibAPI.getStack(Materials.Olivine, Shapes.dust, 9))
                .duration(MaterialUtils.mass(Materials.Olivine) * 9 * TICKS)
                .eut(4)
                .addTo(maceratorRecipes);
        }

        if (ProjectRedExploration.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(ProjectRedExploration.ID, "projectred.exploration.stone", 1, 7))
                .itemOutputs(MaterialLibAPI.getStack(Materials.Olivine, Shapes.dust, 9))
                .duration(MaterialUtils.mass(Materials.Olivine) * 9 * TICKS)
                .eut(4)
                .addTo(maceratorRecipes);
        }

        // From ProcessingSaplings
        GTValues.RA.stdBuilder()
            .itemInputs(new OreDictItemStack("treeSapling", 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Wood, Shapes.dustSmall, 2))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        // From ProcessingStone (macerator recipes)
        GTValues.RA.stdBuilder()
            .itemInputs(new OreDictItemStack("stoneSand", 1))
            .itemOutputs(new ItemStack(Blocks.sand, 1, 0))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new OreDictItemStack("stoneEndstone", 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Endstone, Shapes.dustImpure, 1),
                MaterialLibAPI.getStack(Materials.Tungstate, Shapes.dustTiny, 1))
            .outputChances(10000, 500)
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new OreDictItemStack("stoneNetherrack", 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Netherrack, Shapes.dustImpure, 1),
                GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Gold, 1L))
            .outputChances(10000, 500)
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new OreDictItemStack("stoneConcrete", 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Concrete, Shapes.dust, 1))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new OreDictItemStack("stoneAndesite", 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Obsidian, Shapes.dust, 1))
            .outputChances(10000, 2000)
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        // Basalt falls through to the Quartzite macerator case in the original ProcessingStone
        GTValues.RA.stdBuilder()
            .itemInputs(new OreDictItemStack("stoneBasalt", 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Basalt, Shapes.dustImpure, 1),
                MaterialLibAPI.getStack(Materials.Basalt, Shapes.dust, 1))
            .outputChances(10000, 1000)
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new OreDictItemStack("stoneGraniteBlack", 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.GraniteBlack, Shapes.dustImpure, 1),
                MaterialLibAPI.getStack(Materials.Thorium, Shapes.dust, 1))
            .outputChances(10000, 100)
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new OreDictItemStack("stoneGraniteRed", 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.GraniteRed, Shapes.dustImpure, 1),
                MaterialLibAPI.getStack(Materials.Uranium, Shapes.dustSmall, 1))
            .outputChances(10000, 100)
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        // LUAG gems
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.CeriumdopedLutetiumAluminiumGarnetCeLuAG, Shapes.gemChipped, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.CeriumdopedLutetiumAluminiumGarnetCeLuAG, Shapes.dustSmall, 1))
            .duration(25 * TICKS)
            .eut(4)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.CeriumdopedLutetiumAluminiumGarnetCeLuAG, Shapes.gemFlawed, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.CeriumdopedLutetiumAluminiumGarnetCeLuAG, Shapes.dustSmall, 2))
            .duration(50 * TICKS)
            .eut(4)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.CeriumdopedLutetiumAluminiumGarnetCeLuAG, Shapes.gem, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.CeriumdopedLutetiumAluminiumGarnetCeLuAG, Shapes.dust, 1))
            .duration(5 * SECONDS)
            .eut(4)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.CeriumdopedLutetiumAluminiumGarnetCeLuAG, Shapes.gemFlawless, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.CeriumdopedLutetiumAluminiumGarnetCeLuAG, Shapes.dust, 2))
            .duration(10 * SECONDS)
            .eut(4)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.CeriumdopedLutetiumAluminiumGarnetCeLuAG, Shapes.gemExquisite, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.CeriumdopedLutetiumAluminiumGarnetCeLuAG, Shapes.dust, 4))
            .duration(20 * SECONDS)
            .eut(4)
            .addTo(maceratorRecipes);
    }
}
