package com.github.bartimaeusnek.bartworks.common.loaders.recipes;

import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sFluidSolidficationRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.TierEU;

public class FluidSolidifier implements Runnable {

    @Override
    public void run() {

        GT_Values.RA.stdBuilder().itemInputs(new ItemStack(Blocks.lapis_block))
                .itemOutputs(new ItemStack(ItemRegistry.BW_BLOCKS[0], 1, 0))
                .fluidInputs(Materials.Iron.getMolten(1296L)).duration(5 * SECONDS).eut(TierEU.RECIPE_HV)
                .addTo(sFluidSolidficationRecipes);

        GT_Values.RA.stdBuilder().itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0))
                .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 1))
                .fluidInputs(Materials.Titanium.getMolten(1152)).duration(40 * SECONDS).eut(TierEU.RECIPE_HV)
                .addTo(sFluidSolidficationRecipes);

        GT_Values.RA.stdBuilder().itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0))
                .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 2))
                .fluidInputs(Materials.TungstenSteel.getMolten(1152)).duration(40 * SECONDS).eut(TierEU.RECIPE_EV)
                .addTo(sFluidSolidficationRecipes);

        GT_Values.RA.stdBuilder().itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0))
                .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 3))
                .fluidInputs(WerkstoffLoader.LuVTierMaterial.getMolten(1152)).duration(40 * SECONDS)
                .eut(TierEU.RECIPE_IV).addTo(sFluidSolidficationRecipes);

        GT_Values.RA.stdBuilder().itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0))
                .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 4))
                .fluidInputs(Materials.Iridium.getMolten(1152)).duration(40 * SECONDS).eut(TierEU.RECIPE_LuV)
                .addTo(sFluidSolidficationRecipes);

        GT_Values.RA.stdBuilder().itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0))
                .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 5))
                .fluidInputs(Materials.Osmium.getMolten(1152)).duration(40 * SECONDS).eut(TierEU.RECIPE_ZPM)
                .addTo(sFluidSolidficationRecipes);

        GT_Values.RA.stdBuilder().itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0))
                .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 13))
                .fluidInputs(Materials.Neutronium.getMolten(1152)).duration(40 * SECONDS).eut(TierEU.RECIPE_UV)
                .addTo(sFluidSolidficationRecipes);

        GT_Values.RA.stdBuilder().itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0))
                .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 14))
                .fluidInputs(Materials.CosmicNeutronium.getMolten(1152)).duration(40 * SECONDS).eut(TierEU.RECIPE_UHV)
                .addTo(sFluidSolidficationRecipes);

        GT_Values.RA.stdBuilder().itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0))
                .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 15))
                .fluidInputs(Materials.Infinity.getMolten(1152)).duration(40 * SECONDS).eut(TierEU.RECIPE_UEV)
                .addTo(sFluidSolidficationRecipes);

        GT_Values.RA.stdBuilder().itemInputs(new ItemStack(ItemRegistry.bw_glasses[0], 1, 0))
                .itemOutputs(new ItemStack(ItemRegistry.bw_glasses[1], 1, 0))
                .fluidInputs(MaterialsUEVplus.TranscendentMetal.getMolten(1152)).duration(40 * SECONDS)
                .eut(TierEU.RECIPE_UIV).addTo(sFluidSolidficationRecipes);

    }
}
