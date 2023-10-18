package gtPlusPlus.xmod.gregtech.loaders;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.ItemUtils;

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
                    case "Force":
                        GT_Values.RA.addFuel(GT_Utility.copyAmount(1L, aPlasmaCell), aContainerItem, 150_000, 4);
                    case "Runite":
                        GT_Values.RA.addFuel(GT_Utility.copyAmount(1L, aPlasmaCell), aContainerItem, 350_000, 4);
                    case "CelestialTungsten":
                        GT_Values.RA.addFuel(GT_Utility.copyAmount(1L, aPlasmaCell), aContainerItem, 720_000, 4);
                    default:
                        GT_Values.RA.addFuel(
                                GT_Utility.copyAmount(1L, aPlasmaCell),
                                aContainerItem,
                                (int) Math.max(1024L, 1024L * material.getMass()),
                                4);

                }
            }
            if (ItemUtils.checkForInvalidItems(new ItemStack[] { aCell, aPlasmaCell })) {
                GT_Values.RA.addVacuumFreezerRecipe(aPlasmaCell, aCell, (int) Math.max(material.getMass() * 2L, 1L));
            }
        }
    }
}
