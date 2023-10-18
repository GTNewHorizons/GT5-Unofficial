package gtPlusPlus.xmod.gregtech.loaders;

import java.util.HashSet;
import java.util.Set;

import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class RecipeGen_ShapedCrafting extends RecipeGen_Base {

    public static final Set<RunnableWithInfo<Material>> mRecipeGenMap = new HashSet<>();

    static {
        MaterialGenerator.mRecipeMapsToGenerate.put(mRecipeGenMap);
    }

    public RecipeGen_ShapedCrafting(final Material M) {
        this.toGenerate = M;
        mRecipeGenMap.add(this);
    }

    @Override
    public void run() {
        generateRecipes(this.toGenerate);
    }

    private void generateRecipes(final Material material) {
        Logger.WARNING("Generating Shaped Crafting recipes for " + material.getLocalizedName()); // TODO

        // Single Plate Shaped/Shapeless
        if (ItemUtils.checkForInvalidItems(material.getPlate(1))
                && ItemUtils.checkForInvalidItems(material.getIngot(1)))
            if (material.getPlate(1) != null && material.getIngot(1) != null) GT_ModHandler.addCraftingRecipe(
                    material.getPlate(1),
                    gregtech.api.util.GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                            | gregtech.api.util.GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "h", "B", "I", Character.valueOf('I'), material.getIngot(1), Character.valueOf('B'),
                            material.getIngot(1) });

        if (ItemUtils.checkForInvalidItems(material.getPlate(1))
                && ItemUtils.checkForInvalidItems(material.getIngot(1)))
            GT_ModHandler.addShapelessCraftingRecipe(
                    material.getPlate(1),
                    new Object[] { gregtech.api.enums.ToolDictNames.craftingToolForgeHammer, material.getIngot(1),
                            material.getIngot(1) });

        // Double Plate Shaped/Shapeless
        if (ItemUtils.checkForInvalidItems(material.getPlateDouble(1))
                && ItemUtils.checkForInvalidItems(material.getPlate(1)))
            GT_ModHandler.addCraftingRecipe(
                    material.getPlateDouble(1),
                    gregtech.api.util.GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                            | gregtech.api.util.GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "I", "B", "h", Character.valueOf('I'), material.getPlate(1), Character.valueOf('B'),
                            material.getPlate(1) });

        if (ItemUtils.checkForInvalidItems(material.getPlateDouble(1))
                && ItemUtils.checkForInvalidItems(material.getPlate(1)))
            GT_ModHandler.addShapelessCraftingRecipe(
                    material.getPlateDouble(1),
                    new Object[] { gregtech.api.enums.ToolDictNames.craftingToolForgeHammer, material.getPlate(1),
                            material.getPlate(1) });

        // Ring Recipe
        if (!material.isRadioactive && ItemUtils.checkForInvalidItems(material.getRing(1))
                && ItemUtils.checkForInvalidItems(material.getRod(1))) {
            if (GT_ModHandler.addCraftingRecipe(
                    material.getRing(1),
                    GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "h ", "fR", 'R', material.getRod(1) })) {
                Logger.WARNING("GT:NH Ring Recipe: " + material.getLocalizedName() + " - Success");
            } else {
                Logger.WARNING("GT:NH Ring Recipe: " + material.getLocalizedName() + " - Failed");
            }
        }

        // Framebox Recipe
        if (!material.isRadioactive && ItemUtils.checkForInvalidItems(material.getFrameBox(1))
                && ItemUtils.checkForInvalidItems(material.getRod(1))) {
            if (GT_ModHandler.addCraftingRecipe(
                    material.getFrameBox(2),
                    gregtech.api.util.GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                            | gregtech.api.util.GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "RRR", "RwR", "RRR", 'R', material.getRod(1) })) {
                Logger.WARNING("Framebox Recipe: " + material.getLocalizedName() + " - Success");
            } else {
                Logger.WARNING("Framebox Recipe: " + material.getLocalizedName() + " - Failed");
            }
        }

        // Shaped Recipe - Bolts
        if (!material.isRadioactive && ItemUtils.checkForInvalidItems(material.getBolt(1))
                && ItemUtils.checkForInvalidItems(material.getRod(1))) {
            if (GT_ModHandler.addCraftingRecipe(
                    material.getBolt(2),
                    gregtech.api.util.GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                            | gregtech.api.util.GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "s ", " R", 'R', material.getRod(1) })) {
                Logger.WARNING("Bolt Recipe: " + material.getLocalizedName() + " - Success");
            } else {
                Logger.WARNING("Bolt Recipe: " + material.getLocalizedName() + " - Failed");
            }
        }

        // Shaped Recipe - Fine Wire
        if (!material.isRadioactive && ItemUtils.checkForInvalidItems(material.getFoil(1))
                && ItemUtils.checkForInvalidItems(material.getFineWire(1))) {
            if (GT_ModHandler.addCraftingRecipe(
                    material.getFineWire(1),
                    gregtech.api.util.GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                            | gregtech.api.util.GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "Fx", 'F', material.getFoil(1) })) {
                Logger.WARNING("Fine Wire Recipe: " + material.getLocalizedName() + " - Success");
            } else {
                Logger.WARNING("Fine Wire Recipe: " + material.getLocalizedName() + " - Failed");
            }
        }

        // Shaped Recipe - Foil
        if (ItemUtils.checkForInvalidItems(material.getFoil(1))
                && ItemUtils.checkForInvalidItems(material.getPlate(1))) {
            if (GT_ModHandler.addCraftingRecipe(
                    material.getFoil(2),
                    gregtech.api.util.GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                            | gregtech.api.util.GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "hP", 'P', material.getPlate(1) })) {
                Logger.WARNING("Foil Recipe: " + material.getLocalizedName() + " - Success");
            } else {
                Logger.WARNING("Foil Recipe: " + material.getLocalizedName() + " - Failed");
            }
        }

        // Shaped Recipe - Ingot to Rod
        if (ItemUtils.checkForInvalidItems(material.getRod(1)) && ItemUtils.checkForInvalidItems(material.getIngot(1)))
            if (GT_ModHandler.addCraftingRecipe(
                    material.getRod(1),
                    gregtech.api.util.GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                            | gregtech.api.util.GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "f ", " I", 'I', material.getIngot(1) })) {
                        Logger.WARNING("Rod Recipe: " + material.getLocalizedName() + " - Success");
                    } else {
                        Logger.WARNING("Rod Recipe: " + material.getLocalizedName() + " - Failed");
                    }

        // Shaped Recipe - Long Rod to two smalls
        if (ItemUtils.checkForInvalidItems(material.getRod(1))
                && ItemUtils.checkForInvalidItems(material.getLongRod(1)))
            if (GT_ModHandler.addCraftingRecipe(
                    material.getRod(2),
                    gregtech.api.util.GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                            | gregtech.api.util.GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "s", "L", 'L', material.getLongRod(1) })) {
                        Logger.WARNING("Rod Recipe: " + material.getLocalizedName() + " - Success");
                    } else {
                        Logger.WARNING("Rod Recipe: " + material.getLocalizedName() + " - Failed");
                    }

        // Two small to long rod
        if (ItemUtils.checkForInvalidItems(material.getLongRod(1))
                && ItemUtils.checkForInvalidItems(material.getRod(1)))
            if (GT_ModHandler.addCraftingRecipe(
                    material.getLongRod(1),
                    gregtech.api.util.GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                            | gregtech.api.util.GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "RhR", 'R', material.getRod(1) })) {
                        Logger.WARNING("Long Rod Recipe: " + material.getLocalizedName() + " - Success");
                    } else {
                        Logger.WARNING("Long Rod Recipe: " + material.getLocalizedName() + " - Failed");
                    }

        // Rotor Recipe
        if (!material.isRadioactive && ItemUtils.checkForInvalidItems(material.getRotor(1))
                && ItemUtils.checkForInvalidItems(material.getRing(1))
                && !material.isRadioactive
                && ItemUtils.checkForInvalidItems(material.getPlate(1))
                && ItemUtils.checkForInvalidItems(material.getScrew(1))) {
            if (GT_ModHandler.addCraftingRecipe(
                    material.getRotor(1),
                    gregtech.api.util.GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                            | gregtech.api.util.GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PhP", "SRf", "PdP", 'P', material.getPlate(1), 'S', material.getScrew(1), 'R',
                            material.getRing(1), })) {
                Logger.WARNING("Rotor Recipe: " + material.getLocalizedName() + " - Success");
            } else {
                Logger.WARNING("Rotor Recipe: " + material.getLocalizedName() + " - Failed");
            }
        }

        // Gear Recipe
        if (!material.isRadioactive && ItemUtils.checkForInvalidItems(material.getGear(1))
                && ItemUtils.checkForInvalidItems(material.getPlate(1))
                && ItemUtils.checkForInvalidItems(material.getRod(1))) {
            if (GT_ModHandler.addCraftingRecipe(
                    material.getGear(1),
                    gregtech.api.util.GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                            | gregtech.api.util.GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "RPR", "PwP", "RPR", 'P', material.getPlate(1), 'R', material.getRod(1), })) {
                Logger.WARNING("Gear Recipe: " + material.getLocalizedName() + " - Success");
            } else {
                Logger.WARNING("Gear Recipe: " + material.getLocalizedName() + " - Failed");
            }
        }

        // Screws
        if (!material.isRadioactive && ItemUtils.checkForInvalidItems(material.getScrew(1))
                && ItemUtils.checkForInvalidItems(material.getBolt(1))) {
            if (GT_ModHandler.addCraftingRecipe(
                    material.getScrew(1),
                    gregtech.api.util.GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS
                            | gregtech.api.util.GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "fB", "B ", 'B', material.getBolt(1), })) {
                Logger.WARNING("Screw Recipe: " + material.getLocalizedName() + " - Success");
            } else {
                Logger.WARNING("Screw Recipe: " + material.getLocalizedName() + " - Failed");
            }
        }
    }
}
