package gregtech.loaders.materialrecipes;

import static gregtech.api.recipe.RecipeMaps.crackingRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static net.minecraftforge.fluids.FluidRegistry.getFluidStack;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.CellShapes;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTUtility;
import gregtech.common.items.MetaGeneratedItem98;

/// Hydrocracking and steamcracking recipes, generated for every material carrying the lightly cracked fluid of the
/// family.
public final class CrackingRecipes {

    private CrackingRecipes() {}

    /// Registers the shape consumers. Runs from `GTMod`'s `MaterialRegistrationEvent` handler.
    public static void register() {
        MaterialLibAPI.registerShapeConsumer(
            "gregtech",
            FluidShapes.fluidHydroCracked1,
            (shape, material) -> addHydroCrackingRecipes(material));
        MaterialLibAPI.registerShapeConsumer(
            "gregtech",
            FluidShapes.fluidSteamCracked1,
            (shape, material) -> addSteamCrackingRecipes(material));
    }

    /// Adds the hydrocracking recipes turning a material's liquid, or else its gas, into its three cracked fluids.
    private static void addHydroCrackingRecipes(Material material) {
        final Fluid uncrackedFluid;
        if (MaterialUtils.fluidOf(material) != null) {
            uncrackedFluid = MaterialUtils.fluidOf(material);
        } else if (MaterialUtils.gasOf(material) != null) {
            uncrackedFluid = MaterialUtils.gasOf(material);
        } else return;
        for (int i = 0; i < 3; i++) {
            Fluid crackedFluid = MaterialUtils.crackedFluid(material, MaterialUtils.CrackType.HYDRO, i);
            int hydrogenAmount = 2 * i + 2;
            GTValues.RA.stdBuilder()
                .circuit(i + 1)
                .fluidInputs(
                    new FluidStack(uncrackedFluid, 1000),
                    MaterialLibAPI
                        .getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, (int) (hydrogenAmount * 800)))
                .fluidOutputs(new FluidStack(crackedFluid, 1000))
                .duration((1 + i) * SECONDS)
                .eut(TierEU.RECIPE_HV / 2)
                .addTo(crackingRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(Materials.Hydrogen, CellShapes.cellGas, (int) (hydrogenAmount)))
                .circuit(i + 1)
                .itemOutputs(GTUtility.getEmptyCell(hydrogenAmount))
                .fluidInputs(new FluidStack(uncrackedFluid, 1000))
                .fluidOutputs(new FluidStack(crackedFluid, 800))
                .duration((8 + 4 * i) * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(RecipeMaps.chemicalReactorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(MaterialParts.requireCell(material, 1))
                .circuit(i + 1)
                .itemOutputs(GTUtility.getEmptyCell(1L))
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, (int) (hydrogenAmount * 1000)))
                .fluidOutputs(new FluidStack(crackedFluid, 800))
                .duration((8 + 4 * i) * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(RecipeMaps.chemicalReactorRecipes);

            GTValues.RA.stdBuilder()
                .circuit(i + 1)
                .fluidInputs(
                    new FluidStack(uncrackedFluid, 1000),
                    MaterialLibAPI
                        .getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, (int) (hydrogenAmount * 1000)))
                .fluidOutputs(new FluidStack(crackedFluid, 800))
                .duration((4 + 2 * i) * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(RecipeMaps.multiblockChemicalReactorRecipes);
        }
    }

    /// Adds the steamcracking recipes turning a material's liquid, or else its gas, into its three cracked fluids.
    private static void addSteamCrackingRecipes(Material material) {
        final Fluid uncrackedFluid;
        if (MaterialUtils.fluidOf(material) != null) {
            uncrackedFluid = MaterialUtils.fluidOf(material);
        } else if (MaterialUtils.gasOf(material) != null) {
            uncrackedFluid = MaterialUtils.gasOf(material);
        } else return;
        for (int i = 0; i < 3; i++) {
            Fluid crackedFluid = MaterialUtils.crackedFluid(material, MaterialUtils.CrackType.STEAM, i);
            GTValues.RA.stdBuilder()
                .circuit(i + 1)
                .fluidInputs(
                    new FluidStack(uncrackedFluid, 1_000),
                    MaterialLibAPI.getFluidStack(Materials.Steam, FluidShapes.fluidGas, 1_000))
                .fluidOutputs(new FluidStack(crackedFluid, 1_200))
                .duration((1 + i) * SECONDS)
                .eut(TierEU.RECIPE_HV / 2)
                .addTo(crackingRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(MetaGeneratedItem98.FluidCell.STEAM.get(1))
                .circuit(i + 1)
                .itemOutputs(GTUtility.getEmptyCell(1L))
                .fluidInputs(new FluidStack(uncrackedFluid, 1_000))
                .fluidOutputs(new FluidStack(crackedFluid, 800))
                .duration((8 + 4 * i) * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(RecipeMaps.chemicalReactorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(MaterialParts.requireCell(material, 1))
                .circuit(i + 1)
                .itemOutputs(GTUtility.getEmptyCell(1L))
                .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Steam, FluidShapes.fluidGas, 1_000))
                .fluidOutputs(new FluidStack(crackedFluid, 800))
                .duration((8 + 4 * i) * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(RecipeMaps.chemicalReactorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(MaterialParts.requireCell(material, 1))
                .circuit(i + 1)
                .itemOutputs(GTUtility.getEmptyCell(1L))
                .fluidInputs(getFluidStack("ic2steam", 1000))
                .fluidOutputs(new FluidStack(crackedFluid, 800))
                .duration((8 + 4 * i) * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(RecipeMaps.chemicalReactorRecipes);

            GTValues.RA.stdBuilder()
                .circuit(i + 1)
                .fluidInputs(
                    new FluidStack(uncrackedFluid, 1_000),
                    MaterialLibAPI.getFluidStack(Materials.Steam, FluidShapes.fluidGas, 1_000))
                .fluidOutputs(new FluidStack(crackedFluid, 800))
                .duration((4 + 2 * i) * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(RecipeMaps.multiblockChemicalReactorRecipes);
        }
    }
}
