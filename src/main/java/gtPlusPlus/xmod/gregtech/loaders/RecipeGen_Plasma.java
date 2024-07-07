package gtPlusPlus.xmod.gregtech.loaders;

import java.util.HashSet;
import java.util.Set;

import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_RecipeConstants;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.ItemUtils;

import static gregtech.api.recipe.RecipeMaps.vacuumFreezerRecipes;
import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gregtech.api.util.GT_RecipeConstants.FUEL_TYPE;
import static gregtech.api.util.GT_RecipeConstants.FUEL_VALUE;

public class RecipeGen_Plasma extends RecipeGen_Base {

    public static final Set<RunnableWithInfo<Material>> mRecipeGenMap = new HashSet<>();

    static {
        MaterialGenerator.mRecipeMapsToGenerate.put(mRecipeGenMap);
    }

    public RecipeGen_Plasma(final Material M) {
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
            ItemStack aContainerItem = GT_Utility.getFluidForFilledItem(aPlasmaCell, true) == null
                ? GT_Utility.getContainerItem(aPlasmaCell, true)
                : CI.emptyCells(1);
            if (ItemUtils.checkForInvalidItems(new ItemStack[] { aPlasmaCell, aContainerItem })) {
                switch (material.getUnlocalizedName()) {
                    case "Runite":
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1L, aPlasmaCell))
                            .itemOutputs(aContainerItem)
                            .metadata(FUEL_VALUE, 350_000)
                            .metadata(FUEL_TYPE, GT_RecipeConstants.FuelType.PlasmaTurbine.ordinal())
                            .duration(0)
                            .eut(0)
                            .addTo(GT_RecipeConstants.Fuel);
                    case "CelestialTungsten":
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1L, aPlasmaCell))
                            .itemOutputs(aContainerItem)
                            .metadata(FUEL_VALUE, 720_000)
                            .metadata(FUEL_TYPE, GT_RecipeConstants.FuelType.PlasmaTurbine.ordinal())
                            .duration(0)
                            .eut(0)
                            .addTo(GT_RecipeConstants.Fuel);

                    default:
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_Utility.copyAmount(1L, aPlasmaCell))
                            .itemOutputs(aContainerItem)
                            .metadata(FUEL_VALUE, (int) Math.max(1024L, 1024L * material.getMass()))
                            .metadata(FUEL_TYPE, GT_RecipeConstants.FuelType.PlasmaTurbine.ordinal())
                            .duration(0)
                            .eut(0)
                            .addTo(GT_RecipeConstants.Fuel);
                }
            }
            if (ItemUtils.checkForInvalidItems(new ItemStack[] { aCell, aPlasmaCell })) {
                GT_Values.RA.stdBuilder()
                    .itemInputs(aPlasmaCell)
                    .itemOutputs(aCell)
                    .duration(Math.max(material.getMass() * 2L, 1L))
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumFreezerRecipes);
            }
        }
    }
}
