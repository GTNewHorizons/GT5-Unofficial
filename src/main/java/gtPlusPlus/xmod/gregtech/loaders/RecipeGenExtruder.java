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

public class RecipeGenExtruder extends RecipeGenBase {

    public static final Set<RunnableWithInfo<Material>> mRecipeGenMap = new HashSet<>();

    static {
        MaterialGenerator.mRecipeMapsToGenerate.add(mRecipeGenMap);
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

        Logger.WARNING("Generating Extruder recipes for " + material.getDefaultLocalName());

        if (material.getIngot(1) != null && material.getBlock(1) != null) {
            // Ingot Recipe
            GTValues.RA.stdBuilder()
                .itemInputs(material.getBlock(1), shape_Ingot)
                .itemOutputs(material.getIngot(9))
                .duration((int) Math.max(material.getMass() * 2L * 1, 1))
                .eut(material.vVoltageMultiplier)
                .addTo(extruderRecipes);

            Logger.WARNING("Extruder Ingot Recipe: " + material.getDefaultLocalName() + " - Success");

            // Block Recipe
            GTValues.RA.stdBuilder()
                .itemInputs(material.getIngot(9), shape_Block)
                .itemOutputs(material.getBlock(1))
                .duration((int) Math.max(material.getMass() * 2L * 1, 1))
                .eut(material.vVoltageMultiplier)
                .addTo(extruderRecipes);

            Logger.WARNING("Extruder Block Recipe: " + material.getDefaultLocalName() + " - Success");
        }

        // Plate Recipe
        if (material.getIngot(1) != null && material.getPlate(1) != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(itemIngot, shape_Plate)
                .itemOutputs(itemPlate)
                .duration(10 * TICKS)
                .eut(material.vVoltageMultiplier)
                .addTo(extruderRecipes);

            Logger.WARNING("Extruder Plate Recipe: " + material.getDefaultLocalName() + " - Success");
        }

        // Ring Recipe
        if (material.getIngot(1) != null && material.getRing(1) != null) {
            if (!material.isRadioactive) {
                GTValues.RA.stdBuilder()
                    .itemInputs(itemIngot, shape_Ring)
                    .itemOutputs(material.getRing(4))
                    .duration((int) Math.max(material.getMass() * 2L * 1, 1))
                    .eut(material.vVoltageMultiplier)
                    .addTo(extruderRecipes);

                Logger.WARNING("Extruder Ring Recipe: " + material.getDefaultLocalName() + " - Success");
            }
        }

        // Gear Recipe
        if (material.getIngot(1) != null && material.getGear(1) != null && !material.isRadioactive) {
            GTValues.RA.stdBuilder()
                .itemInputs(material.getIngot(4), shape_Gear)
                .itemOutputs(itemGear)
                .duration((int) Math.max(material.getMass() * 5L, 1))
                .eut(material.vVoltageMultiplier)
                .addTo(extruderRecipes);

            Logger.WARNING("Extruder Gear Recipe: " + material.getDefaultLocalName() + " - Success");
        }

        // Rod Recipe
        if (material.getIngot(1) != null && material.getRod(1) != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(itemIngot, shape_Rod)
                .itemOutputs(material.getRod(2))
                .duration((int) Math.max(material.getMass() * 2L * 1, 1))
                .eut(material.vVoltageMultiplier)
                .addTo(extruderRecipes);

            Logger.WARNING("Extruder Rod Recipe: " + material.getDefaultLocalName() + " - Success");
        }

        // Bolt Recipe
        if (material.getIngot(1) != null && material.getBolt(1) != null && !material.isRadioactive) {
            GTValues.RA.stdBuilder()
                .itemInputs(itemIngot, shape_Bolt)
                .itemOutputs(material.getBolt(8))
                .duration((int) Math.max(material.getMass() * 2L, 1))
                .eut(material.vVoltageMultiplier)
                .addTo(extruderRecipes);

            Logger.WARNING("Extruder Bolt Recipe: " + material.getDefaultLocalName() + " - Success");
        }

        // Rotor Recipe
        // Shape_Extruder_Rotor
        if (material.getIngot(1) != null && material.getRotor(1) != null) {

            GTValues.RA.stdBuilder()
                .itemInputs(material.getIngot(5), ItemList.Shape_Extruder_Rotor.get(0))
                .itemOutputs(material.getRotor(1))
                .duration((int) Math.max(material.getMass() * 5L * 1, 1))
                .eut(material.vVoltageMultiplier)
                .addTo(extruderRecipes);

            Logger.WARNING("Extruder Rotor Recipe: " + material.getDefaultLocalName() + " - Success");
        }

    }
}
