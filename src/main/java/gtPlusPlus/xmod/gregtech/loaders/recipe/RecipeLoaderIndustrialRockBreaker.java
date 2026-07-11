package gtPlusPlus.xmod.gregtech.loaders.recipe;

import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.multiblockRockBreakerRecipes;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;

public class RecipeLoaderIndustrialRockBreaker {

    public static void run() {
        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(new ItemStack(Blocks.cobblestone, 1))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockRockBreakerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .itemOutputs(new ItemStack(Blocks.stone, 1))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockRockBreakerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Redstone, Materials2Shapes.shapeDust, (int) (1)))
            .circuit(3)
            .itemOutputs(new ItemStack(Blocks.obsidian, 1))
            .duration(6 * SECONDS + 8 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockRockBreakerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Glowstone, Materials2Shapes.shapeDust, (int) (1)))
            .circuit(6)
            .itemOutputs(new ItemStack(Blocks.netherrack, 1))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockRockBreakerRecipes);

        if (Mods.EtFuturumRequiem.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTModHandler.getModItem(Mods.EtFuturumRequiem.ID, "blue_ice", 0, 0),
                    new ItemStack(Blocks.soul_sand, 0))
                .circuit(4)
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.stone, Materials.Basalt, 1L))
                .duration(16 * TICKS)
                .eut(TierEU.RECIPE_LV)
                .addTo(multiblockRockBreakerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTModHandler.getModItem(Mods.EtFuturumRequiem.ID, "magma", 0, 0),
                    new ItemStack(Blocks.soul_sand, 0))
                .circuit(5)
                .itemOutputs(GTModHandler.getModItem(Mods.EtFuturumRequiem.ID, "cobbled_deepslate", 1, 0))
                .duration(16 * TICKS)
                .eut(TierEU.RECIPE_LV)
                .addTo(multiblockRockBreakerRecipes);
        }

        if (Mods.ThaumicBases.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTModHandler.getModItem(Mods.ThaumicBases.ID, "genLeaves", 1, 2))
                .inputChances(1000)
                .circuit(6)
                .itemOutputs(
                    new ItemStack(Blocks.netherrack, 1),
                    new ItemStack(Blocks.soul_sand, 1),
                    new ItemStack(Blocks.quartz_ore, 1))
                .outputChances(6000, 3000, 1000)
                .duration(SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(multiblockRockBreakerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTModHandler.getModItem(Mods.ThaumicBases.ID, "genLeaves", 1, 3))
                .inputChances(1000)
                .circuit(7)
                .itemOutputs(new ItemStack(Blocks.end_stone, 1), new ItemStack(Blocks.obsidian, 1))
                .outputChances(9000, 1000)
                .duration(SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(multiblockRockBreakerRecipes);
        }
    }
}
