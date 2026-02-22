package bartworks.common.loaders.recipes;

import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import bartworks.common.loaders.ItemRegistry;
import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;

public class FluidSolidifier implements Runnable {

    @Override
    public void run() {

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.lapis_block))
            .itemOutputs(new ItemStack(ItemRegistry.BW_BLOCKS[0], 1, 0))
            .fluidInputs(Materials.Iron.getMolten(9 * INGOTS))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0))
            .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 1))
            .fluidInputs(Materials.Titanium.getMolten(8 * INGOTS))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0))
            .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 2))
            .fluidInputs(Materials.TungstenSteel.getMolten(8 * INGOTS))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0))
            .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 3))
            .fluidInputs(WerkstoffLoader.LuVTierMaterial.getMolten(8 * INGOTS))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0))
            .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 4))
            .fluidInputs(Materials.Iridium.getMolten(8 * INGOTS))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0))
            .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 5))
            .fluidInputs(Materials.Osmium.getMolten(8 * INGOTS))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0))
            .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 13))
            .fluidInputs(Materials.Neutronium.getMolten(8 * INGOTS))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0))
            .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 14))
            .fluidInputs(Materials.CosmicNeutronium.getMolten(8 * INGOTS))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0))
            .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 15))
            .fluidInputs(Materials.Infinity.getMolten(8 * INGOTS))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(fluidSolidifierRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0))
            .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[1], 1, 0))
            .fluidInputs(Materials.TranscendentMetal.getMolten(8 * INGOTS))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .addTo(fluidSolidifierRecipes);

    }
}
