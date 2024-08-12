package com.github.bartimaeusnek.bartworks.common.loaders;

import static com.github.bartimaeusnek.bartworks.API.recipe.BartWorksRecipeMaps.electricImplosionCompressorRecipes;
import static goodgenerator.loader.Loaders.highDensityPlutonium;
import static goodgenerator.loader.Loaders.highDensityPlutoniumNugget;
import static goodgenerator.loader.Loaders.highDensityThorium;
import static goodgenerator.loader.Loaders.highDensityThoriumNugget;
import static goodgenerator.loader.Loaders.highDensityUranium;
import static goodgenerator.loader.Loaders.highDensityUraniumNugget;
import static gregtech.api.enums.GT_Values.M;
import static gregtech.api.enums.Mods.Avaritia;
import static gregtech.api.enums.Mods.EternalSingularity;
import static gregtech.api.enums.Mods.OpenComputers;
import static gregtech.api.enums.Mods.SuperSolarPanels;
import static gregtech.api.enums.Mods.UniversalSingularities;
import static gregtech.api.util.GT_ModHandler.getModItem;
import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_OreDictUnificator;

public class ElectricImplosionCompressorRecipes implements Runnable {

    @Override
    public void run() {
        // Custom electric implosion compressor recipes. Cannot be overclocked.

        if (EternalSingularity.isModLoaded()) {

            GT_Values.RA.stdBuilder()
                .itemOutputs(getModItem(EternalSingularity.ID, "eternal_singularity", 1L))
                .fluidInputs(MaterialsUEVplus.SpaceTime.getMolten(72L))
                .duration(1 * MINUTES + 40 * SECONDS)
                .eut(TierEU.RECIPE_UMV)
                .noOptimize()
                .addTo(electricImplosionCompressorRecipes);

            if (UniversalSingularities.isModLoaded())
                // Raw Exposed Optical Chip
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Circuit_Silicon_Wafer7.get(1L),
                        // Fluxed Electrum Singularity.
                        getModItem(UniversalSingularities.ID, "universal.general.singularity", 1L, 20))
                    .itemOutputs(ItemList.Circuit_Chip_Optical.get(16L))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_UMV)
                    .noOptimize()
                    .addTo(electricImplosionCompressorRecipes);
        }

        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(highDensityPlutoniumNugget, 5))
            .itemOutputs(new ItemStack(highDensityPlutonium, 1))
            .fluidInputs(Materials.Neutronium.getMolten(72L))
            .duration(1)
            .eut(TierEU.RECIPE_UEV)
            .noOptimize()
            .addTo(electricImplosionCompressorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(highDensityUraniumNugget, 5))
            .itemOutputs(new ItemStack(highDensityUranium, 1))
            .fluidInputs(Materials.Neutronium.getMolten(72L))
            .duration(1)
            .eut(TierEU.RECIPE_UEV)
            .noOptimize()
            .addTo(electricImplosionCompressorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(highDensityThoriumNugget, 5))
            .itemOutputs(new ItemStack(highDensityThorium, 1))
            .fluidInputs(Materials.Neutronium.getMolten(72L))
            .duration(1)
            .eut(TierEU.RECIPE_UEV)
            .noOptimize()
            .addTo(electricImplosionCompressorRecipes);

        // Magneto material recipe for base fluid.
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.nanite, MaterialsUEVplus.WhiteDwarfMatter, 1L),
                GT_OreDictUnificator.get(OrePrefixes.nanite, MaterialsUEVplus.Universium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.nanite, MaterialsUEVplus.BlackDwarfMatter, 1L))
            .fluidInputs(MaterialsUEVplus.RawStarMatter.getFluid(64 * 144L))
            .fluidOutputs(MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter.getMolten(16 * 144L))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_UXV)
            .noOptimize()
            .addTo(electricImplosionCompressorRecipes);

        // Infinity Catalyst
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfinityCatalyst, 64L))
            .itemOutputs(getModItem(Avaritia.ID, "Resource", 1L, 5))
            .duration(1)
            .eut(TierEU.RECIPE_UIV)
            .noOptimize()
            .addTo(electricImplosionCompressorRecipes);

        if (UniversalSingularities.isModLoaded()) {
            // Fluxed Singularity
            GT_Values.RA.stdBuilder()
                .itemInputs(GT_OreDictUnificator.get(OrePrefixes.block, Materials.ElectrumFlux, 16L))
                .itemOutputs(getModItem(UniversalSingularities.ID, "universal.general.singularity", 1L, 20))
                .duration(1)
                .eut(TierEU.RECIPE_UIV)
                .noOptimize()
                .addTo(electricImplosionCompressorRecipes);

            // Iron Singularity
            GT_Values.RA.stdBuilder()
                .itemOutputs(getModItem(Avaritia.ID, "Singularity", 1L, 0))
                .fluidInputs(Materials.Iron.getMolten(7296 * 9 * 144L))
                .duration(1)
                .eut(TierEU.RECIPE_UIV)
                .noOptimize()
                .addTo(electricImplosionCompressorRecipes);
        }
        // MHDCSM V2
        GT_Values.RA.stdBuilder()
            .itemInputs(MaterialsUEVplus.Eternity.getNanite(1), MaterialsUEVplus.Universium.getNanite(1))
            .fluidInputs(MaterialsUEVplus.RawStarMatter.getFluid(128 * 144L))
            .fluidOutputs(MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter.getMolten(32 * 144L))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_MAX)
            .noOptimize()
            .addTo(electricImplosionCompressorRecipes);

        this.addMagnetohydrodynamicallyConstrainedStarMatterPartRecipes();
    }

    private void addMagnetohydrodynamicallyConstrainedStarMatterPartRecipes() {

        this.addWhiteDwarfMagnetoEICRecipe(OrePrefixes.frameGt, 1, 1);
        this.addWhiteDwarfMagnetoEICRecipe(OrePrefixes.nugget, 9, 1);
        this.addWhiteDwarfMagnetoEICRecipe(OrePrefixes.ingot, 1, 1);
        this.addWhiteDwarfMagnetoEICRecipe(OrePrefixes.plate, 1, 1);
        this.addWhiteDwarfMagnetoEICRecipe(OrePrefixes.plateDense, 1, 3);
        this.addWhiteDwarfMagnetoEICRecipe(OrePrefixes.stick, 2, 1);
        this.addWhiteDwarfMagnetoEICRecipe(OrePrefixes.round, 8, 1);
        this.addWhiteDwarfMagnetoEICRecipe(OrePrefixes.bolt, 8, 1);
        this.addWhiteDwarfMagnetoEICRecipe(OrePrefixes.screw, 8, 1);
        this.addWhiteDwarfMagnetoEICRecipe(OrePrefixes.ring, 4, 1);
        this.addWhiteDwarfMagnetoEICRecipe(OrePrefixes.foil, 8, 1);
        this.addWhiteDwarfMagnetoEICRecipe(OrePrefixes.itemCasing, 2, 1);
        this.addWhiteDwarfMagnetoEICRecipe(OrePrefixes.gearGtSmall, 1, 1);
        this.addWhiteDwarfMagnetoEICRecipe(OrePrefixes.rotor, 1, 2);
        this.addWhiteDwarfMagnetoEICRecipe(OrePrefixes.stickLong, 1, 1);
        this.addWhiteDwarfMagnetoEICRecipe(OrePrefixes.springSmall, 2, 1);
        this.addWhiteDwarfMagnetoEICRecipe(OrePrefixes.spring, 1, 1);
        this.addWhiteDwarfMagnetoEICRecipe(OrePrefixes.gearGt, 1, 2);
        this.addWhiteDwarfMagnetoEICRecipe(OrePrefixes.wireFine, 8, 1);
    }

    private void addWhiteDwarfMagnetoEICRecipe(final OrePrefixes part, final int multiplier,
        final int circuitMultiplier) {

        final int partFraction = (int) (144 * part.mMaterialAmount / M);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                new Object[] { OrePrefixes.circuit.get(Materials.UHV), circuitMultiplier },
                getModItem(SuperSolarPanels.ID, "solarsplitter", 1, 0),
                getModItem(OpenComputers.ID, "hologram2", circuitMultiplier, 0),
                GT_OreDictUnificator.get(part, MaterialsUEVplus.Eternity, multiplier))
            .itemOutputs(
                GT_OreDictUnificator
                    .get(part, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, multiplier))
            .fluidInputs(
                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter
                    .getMolten((long) partFraction * multiplier))
            .duration((int) (multiplier * (20 * partFraction / 144.0)))
            .eut(TierEU.RECIPE_UXV)
            .noOptimize()
            .addTo(electricImplosionCompressorRecipes);
    }
}
