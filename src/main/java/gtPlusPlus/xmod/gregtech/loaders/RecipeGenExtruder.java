package gtPlusPlus.xmod.gregtech.loaders;

import static gregtech.api.recipe.RecipeMaps.extruderRecipes;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class RecipeGenExtruder extends RecipeGenBase {

    public static final Set<RunnableWithInfo<Material>> mRecipeGenMap = new HashSet<>();

    static {
        MaterialGenerator.mRecipeMapsToGenerate.put(mRecipeGenMap);
    }

    public RecipeGenExtruder(final Material M) {
        this.toGenerate = M;
        mRecipeGenMap.add(this);
    }

    @Override
    public void run() {
        generateRecipes(this.toGenerate);
    }

    private void generateRecipes(final Material material) {

        final ItemStack itemIngot = material.getIngot(1);
        final ItemStack itemPlate = material.getPlate(1);
        final ItemStack itemGear = material.getGear(1);

        final ItemStack shape_Plate = ItemList.Shape_Extruder_Plate.get(0);
        final ItemStack shape_Ring = ItemList.Shape_Extruder_Ring.get(0);
        final ItemStack shape_Gear = ItemList.Shape_Extruder_Gear.get(0);
        final ItemStack shape_Rod = ItemList.Shape_Extruder_Rod.get(0);
        final ItemStack shape_Bolt = ItemList.Shape_Extruder_Bolt.get(0);
        final ItemStack shape_Block = ItemList.Shape_Extruder_Block.get(0);
        final ItemStack shape_Ingot = ItemList.Shape_Extruder_Ingot.get(0);

        Logger.WARNING("Generating Extruder recipes for " + material.getLocalizedName());

        if (ItemUtils.checkForInvalidItems(material.getIngot(1))
            && ItemUtils.checkForInvalidItems(material.getBlock(1))) {
            // Ingot Recipe
            GTValues.RA.stdBuilder()
                .itemInputs(material.getBlock(1), shape_Ingot)
                .itemOutputs(material.getIngot(9))
                .duration((int) Math.max(material.getMass() * 2L * 1, 1))
                .eut(material.vVoltageMultiplier)
                .addTo(extruderRecipes);

            Logger.WARNING("Extruder Ingot Recipe: " + material.getLocalizedName() + " - Success");

            // Block Recipe
            GTValues.RA.stdBuilder()
                .itemInputs(material.getIngot(9), shape_Block)
                .itemOutputs(material.getBlock(1))
                .duration((int) Math.max(material.getMass() * 2L * 1, 1))
                .eut(material.vVoltageMultiplier)
                .addTo(extruderRecipes);

            Logger.WARNING("Extruder Block Recipe: " + material.getLocalizedName() + " - Success");
        }

        // Plate Recipe
        if (ItemUtils.checkForInvalidItems(material.getIngot(1))
            && ItemUtils.checkForInvalidItems(material.getPlate(1))) {
            GTValues.RA.stdBuilder()
                .itemInputs(itemIngot, shape_Plate)
                .itemOutputs(itemPlate)
                .duration(10 * TICKS)
                .eut(material.vVoltageMultiplier)
                .addTo(extruderRecipes);

            Logger.WARNING("Extruder Plate Recipe: " + material.getLocalizedName() + " - Success");
        }

        // Ring Recipe
        if (ItemUtils.checkForInvalidItems(material.getIngot(1))
            && ItemUtils.checkForInvalidItems(material.getRing(1))) {
            if (!material.isRadioactive) {
                GTValues.RA.stdBuilder()
                    .itemInputs(itemIngot, shape_Ring)
                    .itemOutputs(material.getRing(4))
                    .duration((int) Math.max(material.getMass() * 2L * 1, 1))
                    .eut(material.vVoltageMultiplier)
                    .addTo(extruderRecipes);

                Logger.WARNING("Extruder Ring Recipe: " + material.getLocalizedName() + " - Success");
            }
        }

        // Gear Recipe
        if (ItemUtils.checkForInvalidItems(material.getIngot(1)) && ItemUtils.checkForInvalidItems(material.getGear(1)))
            if (!material.isRadioactive) {
                GTValues.RA.stdBuilder()
                    .itemInputs(material.getIngot(4), shape_Gear)
                    .itemOutputs(itemGear)
                    .duration((int) Math.max(material.getMass() * 5L, 1))
                    .eut(material.vVoltageMultiplier)
                    .addTo(extruderRecipes);

                Logger.WARNING("Extruder Gear Recipe: " + material.getLocalizedName() + " - Success");
            }

        // Rod Recipe
        if (ItemUtils.checkForInvalidItems(material.getIngot(1))
            && ItemUtils.checkForInvalidItems(material.getRod(1))) {
            GTValues.RA.stdBuilder()
                .itemInputs(itemIngot, shape_Rod)
                .itemOutputs(material.getRod(2))
                .duration((int) Math.max(material.getMass() * 2L * 1, 1))
                .eut(material.vVoltageMultiplier)
                .addTo(extruderRecipes);

            Logger.WARNING("Extruder Rod Recipe: " + material.getLocalizedName() + " - Success");
        }

        // Bolt Recipe
        if (ItemUtils.checkForInvalidItems(material.getIngot(1)) && ItemUtils.checkForInvalidItems(material.getBolt(1)))
            if (!material.isRadioactive) {
                GTValues.RA.stdBuilder()
                    .itemInputs(itemIngot, shape_Bolt)
                    .itemOutputs(material.getBolt(8))
                    .duration((int) Math.max(material.getMass() * 2L * 1, 1))
                    .eut(material.vVoltageMultiplier)
                    .addTo(extruderRecipes);

                Logger.WARNING("Extruder Bolt Recipe: " + material.getLocalizedName() + " - Success");
            }

        // Rotor Recipe
        // Shape_Extruder_Rotor
        if (ItemUtils.checkForInvalidItems(material.getIngot(1))
            && ItemUtils.checkForInvalidItems(material.getRotor(1))) {

            GTValues.RA.stdBuilder()
                .itemInputs(material.getIngot(5), ItemList.Shape_Extruder_Rotor.get(0))
                .itemOutputs(material.getRotor(1))
                .duration((int) Math.max(material.getMass() * 5L * 1, 1))
                .eut(material.vVoltageMultiplier)
                .addTo(extruderRecipes);

            Logger.WARNING("Extruder Rotor Recipe: " + material.getLocalizedName() + " - Success");
        }

    }
}
