package gtPlusPlus.xmod.gregtech.loaders;

import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import java.util.HashSet;
import java.util.Set;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;

public class RecipeGenFluids extends RecipeGenBase {

    public static final Set<Runnable> mRecipeGenMap = new HashSet<>();

    static {
        MaterialGenerator.mRecipeMapsToGenerate.add(mRecipeGenMap);
    }

    public RecipeGenFluids(final Material M) {
        this.toGenerate = M;
        this.disableOptional = false;
        mRecipeGenMap.add(this);
    }

    @Override
    public void run() {
        generateRecipes(this.toGenerate);
    }

    private void generateRecipes(final Material material) {

        if (material == null) {
            return;
        }

        // Melting Shapes to fluid
        if (material.getFluidStack(1) != null && !material.getFluidStack(1)
            .getUnlocalizedName()
            .toLowerCase()
            .contains("plasma")) {

            // Making Shapes from fluid

            // Ingot
            if (material.getIngot(1) != null) {
                GTValues.RA.stdBuilder()
                    .itemInputs(ItemList.Shape_Mold_Ingot.get(0))
                    .itemOutputs(material.getIngot(1))
                    .fluidInputs(material.getFluidStack(144))
                    .duration(1 * SECONDS + 12 * TICKS)
                    .eut(material.vVoltageMultiplier)
                    .addTo(fluidSolidifierRecipes);
            }

            // Plate
            if (material.getPlate(1) != null) {
                GTValues.RA.stdBuilder()
                    .itemInputs(ItemList.Shape_Mold_Plate.get(0))
                    .itemOutputs(material.getPlate(1))
                    .fluidInputs(material.getFluidStack(144))
                    .duration(1 * SECONDS + 12 * TICKS)
                    .eut(material.vVoltageMultiplier)
                    .addTo(fluidSolidifierRecipes);
            }

            // Nugget
            if (material.getNugget(1) != null) {
                GTValues.RA.stdBuilder()
                    .itemInputs(ItemList.Shape_Mold_Nugget.get(0))
                    .itemOutputs(material.getNugget(1))
                    .fluidInputs(material.getFluidStack(16))
                    .duration(16 * TICKS)
                    .eut(material.vVoltageMultiplier)
                    .addTo(fluidSolidifierRecipes);
            }

            // Gears
            if (material.getGear(1) != null) {
                GTValues.RA.stdBuilder()
                    .itemInputs(ItemList.Shape_Mold_Gear.get(0))
                    .itemOutputs(material.getGear(1))
                    .fluidInputs(material.getFluidStack(576))
                    .duration(6 * SECONDS + 8 * TICKS)
                    .eut(material.vVoltageMultiplier)
                    .addTo(fluidSolidifierRecipes);
            }

            // Blocks
            if (material.getBlock(1) != null) {
                GTValues.RA.stdBuilder()
                    .itemInputs(ItemList.Shape_Mold_Block.get(0))
                    .itemOutputs(material.getBlock(1))
                    .fluidInputs(material.getFluidStack(144 * 9))
                    .duration(14 * SECONDS + 8 * TICKS)
                    .eut(material.vVoltageMultiplier)
                    .addTo(fluidSolidifierRecipes);
            }

            // Rod
            if (material.getRod(1) != null) {
                GTValues.RA.stdBuilder()
                    .itemInputs(ItemList.Shape_Mold_Rod.get(0))
                    .itemOutputs(material.getRod(1))
                    .fluidInputs(material.getFluidStack(72))
                    .duration(7 * SECONDS + 10 * TICKS)
                    .eut(material.vVoltageMultiplier)
                    .addTo(fluidSolidifierRecipes);
            }

            // Rod Long
            if (material.getLongRod(1) != null) {
                GTValues.RA.stdBuilder()
                    .itemInputs(ItemList.Shape_Mold_Rod_Long.get(0))
                    .itemOutputs(material.getLongRod(1))
                    .fluidInputs(material.getFluidStack(144))
                    .duration(15 * SECONDS)
                    .eut(material.vVoltageMultiplier)
                    .addTo(fluidSolidifierRecipes);
            }

            // Bolt
            if (material.getBolt(1) != null) {
                GTValues.RA.stdBuilder()
                    .itemInputs(ItemList.Shape_Mold_Bolt.get(0))
                    .itemOutputs(material.getBolt(1))
                    .fluidInputs(material.getFluidStack(18))
                    .duration(2 * SECONDS + 10 * TICKS)
                    .eut(material.vVoltageMultiplier)
                    .addTo(fluidSolidifierRecipes);
            }

            // Screw
            if (material.getScrew(1) != null) {
                GTValues.RA.stdBuilder()
                    .itemInputs(ItemList.Shape_Mold_Screw.get(0))
                    .itemOutputs(material.getScrew(1))
                    .fluidInputs(material.getFluidStack(18))
                    .duration(2 * SECONDS + 10 * TICKS)
                    .eut(material.vVoltageMultiplier)
                    .addTo(fluidSolidifierRecipes);
            }

            // Ring
            if (material.getRing(1) != null) {
                GTValues.RA.stdBuilder()
                    .itemInputs(ItemList.Shape_Mold_Ring.get(0))
                    .itemOutputs(material.getRing(1))
                    .fluidInputs(material.getFluidStack(36))
                    .duration(5 * SECONDS)
                    .eut(material.vVoltageMultiplier)
                    .addTo(fluidSolidifierRecipes);
            }

            // Rotor
            if (material.getRotor(1) != null) {
                GTValues.RA.stdBuilder()
                    .itemInputs(ItemList.Shape_Mold_Rotor.get(0))
                    .itemOutputs(material.getRotor(1))
                    .fluidInputs(material.getFluidStack(612))
                    .duration(5 * SECONDS)
                    .eut(material.vVoltageMultiplier)
                    .addTo(fluidSolidifierRecipes);
            }
        }
    }
}
