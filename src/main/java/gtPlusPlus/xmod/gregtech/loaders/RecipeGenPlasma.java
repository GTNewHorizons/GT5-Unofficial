package gtPlusPlus.xmod.gregtech.loaders;

import static gregtech.api.recipe.RecipeMaps.vacuumFreezerRecipes;
import static gregtech.api.util.GTRecipeConstants.FUEL_TYPE;
import static gregtech.api.util.GTRecipeConstants.FUEL_VALUE;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTUtility;
import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class RecipeGenPlasma extends RecipeGenBase {

    public static final Set<RunnableWithInfo<Material>> mRecipeGenMap = new HashSet<>();

    static {
        MaterialGenerator.mRecipeMapsToGenerate.put(mRecipeGenMap);
    }

    public RecipeGenPlasma(final Material M) {
        this.toGenerate = M;
        mRecipeGenMap.add(this);
    }

    @Override
    public void run() {
        generateRecipes(this.toGenerate);
    }

    private void generateRecipes(final Material material) {
        if (material.getPlasma() != null) {
            // Cool Plasma
            ItemStack aPlasmaCell = material.getPlasmaCell(1);
            ItemStack aCell = material.getCell(1);
            ItemStack aContainerItem = GTUtility.getFluidForFilledItem(aPlasmaCell, true) == null
                ? GTUtility.getContainerItem(aPlasmaCell, true)
                : CI.emptyCells(1);
            if (ItemUtils.checkForInvalidItems(new ItemStack[] { aPlasmaCell, aContainerItem })) {
                switch (material.getUnlocalizedName()) {
                    case "Runite" -> GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1L, aPlasmaCell))
                        .itemOutputs(aContainerItem)
                        .metadata(FUEL_VALUE, 350_000)
                        .metadata(FUEL_TYPE, GTRecipeConstants.FuelType.PlasmaTurbine.ordinal())
                        .duration(0)
                        .eut(0)
                        .addTo(GTRecipeConstants.Fuel);
                    case "CelestialTungsten" -> GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1L, aPlasmaCell))
                        .itemOutputs(aContainerItem)
                        .metadata(FUEL_VALUE, 720_000)
                        .metadata(FUEL_TYPE, GTRecipeConstants.FuelType.PlasmaTurbine.ordinal())
                        .duration(0)
                        .eut(0)
                        .addTo(GTRecipeConstants.Fuel);
                    default -> GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1L, aPlasmaCell))
                        .itemOutputs(aContainerItem)
                        .metadata(FUEL_VALUE, (int) Math.max(1024L, 1024L * material.getMass()))
                        .metadata(FUEL_TYPE, GTRecipeConstants.FuelType.PlasmaTurbine.ordinal())
                        .duration(0)
                        .eut(0)
                        .addTo(GTRecipeConstants.Fuel);
                }
            }
            if (ItemUtils.checkForInvalidItems(new ItemStack[] { aCell, aPlasmaCell })) {
                GTValues.RA.stdBuilder()
                    .itemInputs(aPlasmaCell)
                    .itemOutputs(aCell)
                    .duration(Math.max(material.getMass() * 2L, 1L))
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumFreezerRecipes);
            }
        }
    }
}
