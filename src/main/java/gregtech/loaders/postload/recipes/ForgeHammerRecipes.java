package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.BiomesOPlenty;
import static gregtech.api.enums.Mods.HardcoreEnderExpansion;
import static gregtech.api.enums.Mods.ProjectRedExploration;
import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.util.GTOreDictUnificator;

public class ForgeHammerRecipes implements Runnable {

    @Override
    public void run() {

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.stonebrick, 1, 0))
            .itemOutputs(new ItemStack(Blocks.stonebrick, 1, 2))
            .duration(10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.stone, 1, 0))
            .itemOutputs(new ItemStack(Blocks.cobblestone, 1, 0))
            .duration(10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.cobblestone, 1, 0))
            .itemOutputs(new ItemStack(Blocks.gravel, 1, 0))
            .duration(10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.gravel, 1, 0))
            .itemOutputs(new ItemStack(Blocks.sand, 1, 0))
            .duration(10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.sandstone, 1, 32767))
            .itemOutputs(new ItemStack(Blocks.sand, 1, 0))
            .duration(10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.ice, 1, 0))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Ice, Materials2Shapes.dust, (int) (1)))
            .duration(10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.packed_ice, 1, 0))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Ice, Materials2Shapes.dust, (int) (2)))
            .duration(10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.brick_block, 1, 0))
            .itemOutputs(new ItemStack(Items.brick, 3, 0))
            .duration(10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.nether_brick, 1, 0))
            .itemOutputs(new ItemStack(Items.netherbrick, 3, 0))
            .duration(10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.stained_glass, 1, 32767))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Glass, Materials2Shapes.dust, (int) (1)))
            .duration(10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.glass, 1, 32767))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Glass, Materials2Shapes.dust, (int) (1)))
            .duration(10 * TICKS)
            .eut(10)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.stained_glass_pane, 1, 32767))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Glass, Materials2Shapes.dustTiny, (int) (3)))
            .duration(10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.glass_pane, 1, 32767))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Glass, Materials2Shapes.dustTiny, (int) (3)))
            .duration(10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Brick, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Brick, Materials2Shapes.dustSmall, 1))
            .duration(10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Firebrick.get(1))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Fireclay, Materials2Shapes.dust, 1))
            .duration(10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Firebricks.get(1))
            .itemOutputs(ItemList.Firebrick.get(3))
            .duration(10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(hammerRecipes);

        if (HardcoreEnderExpansion.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(HardcoreEnderExpansion.ID, "endium_ore", 1))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Endium, 1))
                .duration(16)
                .eut(10)
                .addTo(hammerRecipes);
        }

        if (BiomesOPlenty.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(BiomesOPlenty.ID, "gemOre", 1, 5))
                .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Olivine, Materials2Shapes.gem, (int) (9)))
                .duration(5 * SECONDS)
                .eut(24)
                .addTo(hammerRecipes);
        }

        if (ProjectRedExploration.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(ProjectRedExploration.ID, "projectred.exploration.stone", 1, 7))
                .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Olivine, Materials2Shapes.gem, (int) (9)))
                .duration(5 * SECONDS)
                .eut(24)
                .addTo(hammerRecipes);
        }
    }
}
