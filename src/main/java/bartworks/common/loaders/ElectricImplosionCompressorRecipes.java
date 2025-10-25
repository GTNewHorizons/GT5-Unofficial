package bartworks.common.loaders;

import static bartworks.API.recipe.BartWorksRecipeMaps.electricImplosionCompressorRecipes;
import static goodgenerator.loader.Loaders.highDensityPlutonium;
import static goodgenerator.loader.Loaders.highDensityPlutoniumNugget;
import static goodgenerator.loader.Loaders.highDensityThorium;
import static goodgenerator.loader.Loaders.highDensityThoriumNugget;
import static goodgenerator.loader.Loaders.highDensityUranium;
import static goodgenerator.loader.Loaders.highDensityUraniumNugget;
import static gregtech.api.enums.GTValues.M;
import static gregtech.api.enums.Mods.Avaritia;
import static gregtech.api.enums.Mods.EternalSingularity;
import static gregtech.api.enums.Mods.OpenComputers;
import static gregtech.api.enums.Mods.SuperSolarPanels;
import static gregtech.api.enums.Mods.UniversalSingularities;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.HALF_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.STACKS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gtnhlanth.common.register.WerkstoffMaterialPool;

@SuppressWarnings({ "PointlessArithmeticExpression" })
public class ElectricImplosionCompressorRecipes implements Runnable {

    @Override
    public void run() {
        // Custom electric implosion compressor recipes. Cannot be overclocked.

        if (EternalSingularity.isModLoaded()) {

            if (UniversalSingularities.isModLoaded()) {
                // Raw Exposed Optical Chip
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Circuit_Silicon_Wafer7.get(1L),
                        // Fluxed Electrum Singularity.
                        getModItem(UniversalSingularities.ID, "universal.general.singularity", 1L, 20))
                    .itemOutputs(ItemList.Circuit_Chip_Optical.get(16L))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_UMV)
                    .addTo(electricImplosionCompressorRecipes);
            }
        }

        // Manual Add due to werkstoff material system disconnected from autogeneration
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.LanthanumHexaboride.get(OrePrefixes.dust, 4))
            .itemOutputs(
                WerkstoffMaterialPool.LanthanumHexaboride.get(OrePrefixes.gem, 3),
                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.AshDark, 12L))
            .duration(1 * TICKS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(electricImplosionCompressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(highDensityPlutoniumNugget, 5))
            .itemOutputs(new ItemStack(highDensityPlutonium, 1))
            .fluidInputs(Materials.Neutronium.getMolten(1 * HALF_INGOTS))
            .duration(1)
            .eut(TierEU.RECIPE_UEV)
            .addTo(electricImplosionCompressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(highDensityUraniumNugget, 5))
            .itemOutputs(new ItemStack(highDensityUranium, 1))
            .fluidInputs(Materials.Neutronium.getMolten(1 * HALF_INGOTS))
            .duration(1)
            .eut(TierEU.RECIPE_UEV)
            .addTo(electricImplosionCompressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(highDensityThoriumNugget, 5))
            .itemOutputs(new ItemStack(highDensityThorium, 1))
            .fluidInputs(Materials.Neutronium.getMolten(1 * HALF_INGOTS))
            .duration(1)
            .eut(TierEU.RECIPE_UEV)
            .addTo(electricImplosionCompressorRecipes);

        // Magneto material recipe for base fluid.
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.WhiteDwarfMatter, 1L),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Universium, 1L),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.BlackDwarfMatter, 1L))
            .fluidInputs(Materials.RawStarMatter.getFluid(1 * STACKS))
            .fluidOutputs(Materials.MHDCSM.getMolten(16 * INGOTS))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_UXV)
            .addTo(electricImplosionCompressorRecipes);

        // Infinity Catalyst
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.InfinityCatalyst, 64L))
            .itemOutputs(getModItem(Avaritia.ID, "Resource", 1L, 5))
            .duration(1)
            .eut(TierEU.RECIPE_UIV)
            .addTo(electricImplosionCompressorRecipes);

        // MHDCSM V2
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Eternity.getNanite(1), Materials.Universium.getNanite(1))
            .fluidInputs(Materials.RawStarMatter.getFluid(2 * STACKS))
            .fluidOutputs(Materials.MHDCSM.getMolten(32 * INGOTS))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_MAX)
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
        this.addWhiteDwarfMagnetoEICRecipe(OrePrefixes.block, 1, 1);
    }

    private void addWhiteDwarfMagnetoEICRecipe(final OrePrefixes part, final int multiplier,
        final int circuitMultiplier) {

        final int partFraction = (int) (part.getMaterialAmount() * INGOTS / M);

        GTValues.RA.stdBuilder()
            .itemInputs(
                new Object[] { OrePrefixes.circuit.get(Materials.UHV), circuitMultiplier },
                getModItem(SuperSolarPanels.ID, "solarsplitter", 1, 0),
                getModItem(OpenComputers.ID, "hologram2", circuitMultiplier, 0),
                GTOreDictUnificator.get(part, Materials.Eternity, multiplier))
            .itemOutputs(GTOreDictUnificator.get(part, Materials.MHDCSM, multiplier))
            .fluidInputs(Materials.MHDCSM.getMolten((long) partFraction * multiplier))
            .duration((int) (multiplier * (20 * partFraction / (float) INGOTS)))
            .eut(TierEU.RECIPE_UXV)
            .addTo(electricImplosionCompressorRecipes);
    }
}
