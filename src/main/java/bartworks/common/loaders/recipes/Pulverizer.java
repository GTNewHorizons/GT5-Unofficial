package bartworks.common.loaders.recipes;

import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.common.loaders.ItemRegistry;
import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;

public class Pulverizer implements Runnable {

    @Override
    public void run() {

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.BorosilicateGlass, Materials2Shapes.dust, (int) (9)),
                MaterialLibAPI.getStack(Materials2Materials.Titanium, Materials2Shapes.dust, (int) (8)))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 2))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.BorosilicateGlass, Materials2Shapes.dust, (int) (9)),
                MaterialLibAPI.getStack(Materials2Materials.TungstenSteel, Materials2Shapes.dust, (int) (8)))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 3))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.BorosilicateGlass, Materials2Shapes.dust, (int) (9)),
                WerkstoffLoader.RhodiumPlatedPalladium.get(OrePrefixes.dust, 8))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 4))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.BorosilicateGlass, Materials2Shapes.dust, (int) (9)),
                MaterialLibAPI.getStack(Materials2Materials.Iridium, Materials2Shapes.dust, (int) (8)))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 5))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.BorosilicateGlass, Materials2Shapes.dust, (int) (9)),
                MaterialLibAPI.getStack(Materials2Materials.Osmium, Materials2Shapes.dust, (int) (8)))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 6))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.BorosilicateGlass, Materials2Shapes.dust, (int) (9)),
                MaterialLibAPI.getStack(Materials2Materials.Neutronium, Materials2Shapes.dust, (int) (8)))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 7))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.BorosilicateGlass, Materials2Shapes.dust, (int) (9)),
                MaterialLibAPI.getStack(Materials2Materials.Infinity, Materials2Shapes.dust, (int) (8)))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 8))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.BorosilicateGlass, Materials2Shapes.dust, (int) (9)),
                MaterialLibAPI.getStack(Materials2Materials.TranscendentMetal, Materials2Shapes.dust, (int) (8)))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .addTo(maceratorRecipes);

        // Hexanite glass cannot be macerated

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 10))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.BorosilicateGlass, Materials2Shapes.dust, (int) (9)),
                MaterialLibAPI.getStack(Materials2Materials.Universium, Materials2Shapes.dust, (int) (8)))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_UXV)
            .addTo(maceratorRecipes);

    }
}
