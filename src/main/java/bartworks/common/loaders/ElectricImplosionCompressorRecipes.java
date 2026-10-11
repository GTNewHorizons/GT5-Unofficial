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

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.Circuits;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.BlockShapes;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.enums.materials.TEBlockShapes;
import gregtech.api.material.GTShapeProperties;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTUtility;

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

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(highDensityPlutoniumNugget, 5))
            .itemOutputs(new ItemStack(highDensityPlutonium, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Neutronium, FluidShapes.fluidMolten, HALF_INGOTS))
            .duration(1)
            .eut(TierEU.RECIPE_UEV)
            .addTo(electricImplosionCompressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(highDensityUraniumNugget, 5))
            .itemOutputs(new ItemStack(highDensityUranium, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Neutronium, FluidShapes.fluidMolten, HALF_INGOTS))
            .duration(1)
            .eut(TierEU.RECIPE_UEV)
            .addTo(electricImplosionCompressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(highDensityThoriumNugget, 5))
            .itemOutputs(new ItemStack(highDensityThorium, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Neutronium, FluidShapes.fluidMolten, HALF_INGOTS))
            .duration(1)
            .eut(TierEU.RECIPE_UEV)
            .addTo(electricImplosionCompressorRecipes);

        // Magneto material recipe for base fluid.
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.WhiteDwarfMatter, Shapes.nanite, 1),
                MaterialLibAPI.getStack(Materials.Universium, Shapes.nanite, 1),
                MaterialLibAPI.getStack(Materials.BlackDwarfMatter, Shapes.nanite, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.RawStarMatter, FluidShapes.fluidLiquid, STACKS))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.MagnetohydrodynamicallyConstrainedStarMatter,
                    FluidShapes.fluidMolten,
                    16 * INGOTS))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_UXV)
            .addTo(electricImplosionCompressorRecipes);

        // Infinity Catalyst
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.InfinityCatalyst, Shapes.dust, 64))
            .itemOutputs(getModItem(Avaritia.ID, "Resource", 1L, 5))
            .duration(1)
            .eut(TierEU.RECIPE_UIV)
            .addTo(electricImplosionCompressorRecipes);

        // MHDCSM V2
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Eternity, Shapes.nanite, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.RawStarMatter, FluidShapes.fluidLiquid, 4 * STACKS))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.MagnetohydrodynamicallyConstrainedStarMatter,
                    FluidShapes.fluidMolten,
                    72 * INGOTS))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_MAX)
            .addTo(electricImplosionCompressorRecipes);

        // Flawed Amalgatite
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.copyAmount(0, MaterialLibAPI.getStack(Materials.Magmatter, Shapes.nanite, 1)),
                MaterialLibAPI.getStack(Materials.Amalgatite, Shapes.gemChipped, 3),
                MaterialLibAPI.getStack(Materials.FoolsRuby, Shapes.gemFlawless, 64),
                MaterialLibAPI.getStack(Materials.Amethyst, Shapes.gemFlawless, 64),
                MaterialLibAPI.getStack(Materials.Fluorspar, Shapes.gemFlawless, 64),
                MaterialLibAPI.getStack(Materials.Tanzanite, Shapes.gemFlawless, 64))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.PhononCrystalSolution, FluidShapes.fluidLiquid, 500))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Amalgatite, Shapes.gemFlawed, 1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MAX)
            .addTo(electricImplosionCompressorRecipes);

        this.addMagnetohydrodynamicallyConstrainedStarMatterPartRecipes();
    }

    private void addMagnetohydrodynamicallyConstrainedStarMatterPartRecipes() {
        if (!SuperSolarPanels.isModLoaded() || !OpenComputers.isModLoaded()) return;

        this.addWhiteDwarfMagnetoEICRecipe(TEBlockShapes.frameGt, 1, 1);
        this.addWhiteDwarfMagnetoEICRecipe(Shapes.nugget, 9, 1);
        this.addWhiteDwarfMagnetoEICRecipe(Shapes.ingot, 1, 1);
        this.addWhiteDwarfMagnetoEICRecipe(Shapes.plate, 1, 1);
        this.addWhiteDwarfMagnetoEICRecipe(Shapes.plateDense, 1, 3);
        this.addWhiteDwarfMagnetoEICRecipe(Shapes.stick, 2, 1);
        this.addWhiteDwarfMagnetoEICRecipe(Shapes.round, 8, 1);
        this.addWhiteDwarfMagnetoEICRecipe(Shapes.bolt, 8, 1);
        this.addWhiteDwarfMagnetoEICRecipe(Shapes.screw, 8, 1);
        this.addWhiteDwarfMagnetoEICRecipe(Shapes.ring, 4, 1);
        this.addWhiteDwarfMagnetoEICRecipe(Shapes.foil, 8, 1);
        this.addWhiteDwarfMagnetoEICRecipe(Shapes.itemCasing, 2, 1);
        this.addWhiteDwarfMagnetoEICRecipe(Shapes.gearGtSmall, 1, 1);
        this.addWhiteDwarfMagnetoEICRecipe(Shapes.rotor, 1, 2);
        this.addWhiteDwarfMagnetoEICRecipe(Shapes.stickLong, 1, 1);
        this.addWhiteDwarfMagnetoEICRecipe(Shapes.springSmall, 2, 1);
        this.addWhiteDwarfMagnetoEICRecipe(Shapes.spring, 1, 1);
        this.addWhiteDwarfMagnetoEICRecipe(Shapes.gearGt, 1, 2);
        this.addWhiteDwarfMagnetoEICRecipe(Shapes.wireFine, 8, 1);
        this.addWhiteDwarfMagnetoEICRecipe(BlockShapes.block, 1, 1);
    }

    private void addWhiteDwarfMagnetoEICRecipe(final Shape part, final int multiplier, final int circuitMultiplier) {

        final int partFraction = (int) (part.getProperty(GTShapeProperties.MATERIAL_AMOUNT) * INGOTS / M);

        GTValues.RA.stdBuilder()
            .itemInputs(
                new Object[] { Circuits.UHV.getIngredient(), circuitMultiplier },
                getModItem(SuperSolarPanels.ID, "solarsplitter", 1, 0),
                getModItem(OpenComputers.ID, "hologram2", circuitMultiplier, 0),
                MaterialLibAPI.getStack(Materials.Eternity, part, multiplier))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.MagnetohydrodynamicallyConstrainedStarMatter, part, multiplier))
            .fluidInputs(
                MaterialUtils
                    .molten(Materials.MagnetohydrodynamicallyConstrainedStarMatter, (long) partFraction * multiplier))
            .duration((int) (multiplier * (20 * partFraction / (float) INGOTS)))
            .eut(TierEU.RECIPE_UXV)
            .addTo(electricImplosionCompressorRecipes);
    }
}
