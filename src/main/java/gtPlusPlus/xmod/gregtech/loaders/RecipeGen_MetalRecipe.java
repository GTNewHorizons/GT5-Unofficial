package gtPlusPlus.xmod.gregtech.loaders;

import java.util.HashSet;
import java.util.Set;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class RecipeGen_MetalRecipe extends RecipeGen_Base {

    public static final Set<RunnableWithInfo<Material>> mRecipeGenMap = new HashSet<>();

    static {
        MaterialGenerator.mRecipeMapsToGenerate.put(mRecipeGenMap);
    }

    public RecipeGen_MetalRecipe(final Material M) {
        this.toGenerate = M;
        mRecipeGenMap.add(this);
    }

    @Override
    public void run() {
        generateRecipes(this.toGenerate);
    }

    private void generateRecipes(final Material material) {

        Logger.WARNING("Generating Metal recipes for " + material.getLocalizedName());
        if (ItemUtils.checkForInvalidItems(material.getIngot(1))
                && ItemUtils.checkForInvalidItems(material.getBlock(1)))
            if (GT_ModHandler.addCompressionRecipe(material.getIngot(9), material.getBlock(1))) {
                Logger.WARNING("Compress Block Recipe: " + material.getLocalizedName() + " - Success");
            } else {
                Logger.WARNING("Compress Block Recipe: " + material.getLocalizedName() + " - Failed");
            }

        if (ItemUtils.checkForInvalidItems(material.getIngot(1)) && ItemUtils.checkForInvalidItems(material.getRod(1)))
            if (GT_Values.RA.addLatheRecipe(
                    material.getIngot(1),
                    material.getRod(1),
                    material.getSmallDust(2),
                    (int) Math.max(material.getMass() / 8L, 1L),
                    material.vVoltageMultiplier)) {
                        Logger.WARNING("Lathe Rod Recipe: " + material.getLocalizedName() + " - Success");
                    } else {
                        Logger.WARNING("Lathe Rod Recipe: " + material.getLocalizedName() + " - Failed");
                    }

        if (ItemUtils.checkForInvalidItems(material.getRod(1)) && ItemUtils.checkForInvalidItems(material.getBolt(1)))
            if (GT_Values.RA.addCutterRecipe(
                    material.getRod(1),
                    material.getBolt(4),
                    null,
                    (int) Math.max(material.getMass() * 2L, 1L),
                    material.vVoltageMultiplier)) {
                        Logger.WARNING("Cut Bolt Recipe: " + material.getLocalizedName() + " - Success");
                    } else {
                        Logger.WARNING("Cut Bolt Recipe: " + material.getLocalizedName() + " - Failed");
                    }

        if (ItemUtils.checkForInvalidItems(material.getIngot(1))
                && ItemUtils.checkForInvalidItems(material.getHotIngot(1)))
            if (CORE.RA.addVacuumFreezerRecipe(
                    material.getHotIngot(1),
                    material.getIngot(1),
                    (int) Math.max(material.getMass() * 3L, 1L),
                    material.vVoltageMultiplier)) {
                        Logger.WARNING("Cool Hot Ingot Recipe: " + material.getLocalizedName() + " - Success");
                    } else {
                        Logger.WARNING("Cool Hot Ingot Recipe: " + material.getLocalizedName() + " - Failed");
                    }

        if (ItemUtils.checkForInvalidItems(material.getRod(1))
                && ItemUtils.checkForInvalidItems(material.getLongRod(1))) {
            if (GT_Values.RA.addForgeHammerRecipe(
                    material.getRod(2),
                    material.getLongRod(1),
                    (int) Math.max(material.getMass(), 1L),
                    16)) {
                Logger.WARNING("Hammer Long Rod Recipe: " + material.getLocalizedName() + " - Success");
            } else {
                Logger.WARNING("Hammer Long Rod Recipe: " + material.getLocalizedName() + " - Failed");
            }

            GT_Values.RA.addCutterRecipe(
                    material.getLongRod(1),
                    material.getRod(2),
                    null,
                    (int) Math.max(material.getMass(), 1L),
                    4);
        }

        if (ItemUtils.checkForInvalidItems(material.getBolt(1)) && ItemUtils.checkForInvalidItems(material.getScrew(1)))
            if (GT_Values.RA.addLatheRecipe(
                    material.getBolt(1),
                    material.getScrew(1),
                    null,
                    (int) Math.max(material.getMass() / 8L, 1L),
                    4)) {
                        Logger.WARNING("Lathe Screw Recipe: " + material.getLocalizedName() + " - Success");
                    } else {
                        Logger.WARNING("Lathe Screw Recipe: " + material.getLocalizedName() + " - Failed");
                    }
    }
}
