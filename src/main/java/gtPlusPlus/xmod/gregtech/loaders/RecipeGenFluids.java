package gtPlusPlus.xmod.gregtech.loaders;

import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import java.util.HashSet;
import java.util.Set;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class RecipeGenFluids extends RecipeGenBase {

    public static final Set<RunnableWithInfo<Material>> mRecipeGenMap = new HashSet<>();

    static {
        MaterialGenerator.mRecipeMapsToGenerate.put(mRecipeGenMap);
    }

    public RecipeGenFluids(final Material M) {
        this(M, false);
    }

    public RecipeGenFluids(final Material M, final boolean dO) {
        this.toGenerate = M;
        this.disableOptional = dO;
        mRecipeGenMap.add(this);
    }

    @Override
    public void run() {
        generateRecipes(this.toGenerate, this.disableOptional);
    }

    private void generateRecipes(final Material material, final boolean dO) {

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
            if (ItemUtils.checkForInvalidItems(material.getIngot(1))) {
                GTValues.RA.stdBuilder()
                    .itemInputs(ItemList.Shape_Mold_Ingot.get(0))
                    .itemOutputs(material.getIngot(1))
                    .fluidInputs(material.getFluidStack(144))
                    .duration(1 * SECONDS + 12 * TICKS)
                    .eut(material.vVoltageMultiplier)
                    .addTo(fluidSolidifierRecipes);

                Logger.WARNING("144l fluid molder for 1 ingot Recipe: " + material.getLocalizedName() + " - Success");
            }

            // Plate
            if (ItemUtils.checkForInvalidItems(material.getPlate(1))) {
                GTValues.RA.stdBuilder()
                    .itemInputs(ItemList.Shape_Mold_Plate.get(0))
                    .itemOutputs(material.getPlate(1))
                    .fluidInputs(material.getFluidStack(144))
                    .duration(1 * SECONDS + 12 * TICKS)
                    .eut(material.vVoltageMultiplier)
                    .addTo(fluidSolidifierRecipes);

                Logger.WARNING("144l fluid molder for 1 plate Recipe: " + material.getLocalizedName() + " - Success");
            }

            // Nugget
            if (ItemUtils.checkForInvalidItems(material.getNugget(1))) {
                GTValues.RA.stdBuilder()
                    .itemInputs(ItemList.Shape_Mold_Nugget.get(0))
                    .itemOutputs(material.getNugget(1))
                    .fluidInputs(material.getFluidStack(16))
                    .duration(16 * TICKS)
                    .eut(material.vVoltageMultiplier)
                    .addTo(fluidSolidifierRecipes);

                Logger.WARNING("16l fluid molder for 1 nugget Recipe: " + material.getLocalizedName() + " - Success");
            }

            // Gears
            if (ItemUtils.checkForInvalidItems(material.getGear(1))) {
                GTValues.RA.stdBuilder()
                    .itemInputs(ItemList.Shape_Mold_Gear.get(0))
                    .itemOutputs(material.getGear(1))
                    .fluidInputs(material.getFluidStack(576))
                    .duration(6 * SECONDS + 8 * TICKS)
                    .eut(material.vVoltageMultiplier)
                    .addTo(fluidSolidifierRecipes);

                Logger.WARNING("576l fluid molder for 1 gear Recipe: " + material.getLocalizedName() + " - Success");

            }

            // Blocks
            if (ItemUtils.checkForInvalidItems(material.getBlock(1))) {
                GTValues.RA.stdBuilder()
                    .itemInputs(ItemList.Shape_Mold_Block.get(0))
                    .itemOutputs(material.getBlock(1))
                    .fluidInputs(material.getFluidStack(144 * 9))
                    .duration(14 * SECONDS + 8 * TICKS)
                    .eut(material.vVoltageMultiplier)
                    .addTo(fluidSolidifierRecipes);

                Logger.WARNING(
                    (144 * 9) + "l fluid molder from 1 block Recipe: " + material.getLocalizedName() + " - Success");
            }

            // Rod
            if (ItemUtils.checkForInvalidItems(material.getRod(1))) {
                GTValues.RA.stdBuilder()
                    .itemInputs(ItemList.Shape_Mold_Rod.get(0))
                    .itemOutputs(material.getRod(1))
                    .fluidInputs(material.getFluidStack(72))
                    .duration(7 * SECONDS + 10 * TICKS)
                    .eut(material.vVoltageMultiplier)
                    .addTo(fluidSolidifierRecipes);

                Logger.WARNING(
                    (144 * 9) + "l fluid molder from 1 rod Recipe: " + material.getLocalizedName() + " - Success");
            }

            // Rod Long
            if (ItemUtils.checkForInvalidItems(material.getLongRod(1))) {
                GTValues.RA.stdBuilder()
                    .itemInputs(ItemList.Shape_Mold_Rod_Long.get(0))
                    .itemOutputs(material.getLongRod(1))
                    .fluidInputs(material.getFluidStack(144))
                    .duration(15 * SECONDS)
                    .eut(material.vVoltageMultiplier)
                    .addTo(fluidSolidifierRecipes);

                Logger.WARNING(
                    (144 * 9) + "l fluid molder from 1 rod long Recipe: " + material.getLocalizedName() + " - Success");
            }

            // Bolt
            if (ItemUtils.checkForInvalidItems(material.getBolt(1))) {
                GTValues.RA.stdBuilder()
                    .itemInputs(ItemList.Shape_Mold_Bolt.get(0))
                    .itemOutputs(material.getBolt(1))
                    .fluidInputs(material.getFluidStack(18))
                    .duration(2 * SECONDS + 10 * TICKS)
                    .eut(material.vVoltageMultiplier)
                    .addTo(fluidSolidifierRecipes);

                Logger.WARNING(
                    (144 * 9) + "l fluid molder from 1 bolt Recipe: " + material.getLocalizedName() + " - Success");
            }

            // Screw
            if (ItemUtils.checkForInvalidItems(material.getScrew(1))) {
                GTValues.RA.stdBuilder()
                    .itemInputs(ItemList.Shape_Mold_Screw.get(0))
                    .itemOutputs(material.getScrew(1))
                    .fluidInputs(material.getFluidStack(18))
                    .duration(2 * SECONDS + 10 * TICKS)
                    .eut(material.vVoltageMultiplier)
                    .addTo(fluidSolidifierRecipes);

                Logger.WARNING(
                    (144 * 9) + "l fluid molder from 1 screw Recipe: " + material.getLocalizedName() + " - Success");

            }

            // Ring
            if (ItemUtils.checkForInvalidItems(material.getRing(1))) {
                GTValues.RA.stdBuilder()
                    .itemInputs(ItemList.Shape_Mold_Ring.get(0))
                    .itemOutputs(material.getRing(1))
                    .fluidInputs(material.getFluidStack(36))
                    .duration(5 * SECONDS)
                    .eut(material.vVoltageMultiplier)
                    .addTo(fluidSolidifierRecipes);

                Logger.WARNING(
                    (144 * 9) + "l fluid molder from 1 ring Recipe: " + material.getLocalizedName() + " - Success");

            }

            // Rotor
            if (ItemUtils.checkForInvalidItems(material.getRotor(1))) {
                GTValues.RA.stdBuilder()
                    .itemInputs(ItemList.Shape_Mold_Rotor.get(0))
                    .itemOutputs(material.getRotor(1))
                    .fluidInputs(material.getFluidStack(612))
                    .duration(5 * SECONDS)
                    .eut(material.vVoltageMultiplier)
                    .addTo(fluidSolidifierRecipes);

                Logger.WARNING(
                    (144 * 9) + "l fluid molder from 1 rotor Recipe: " + material.getLocalizedName() + " - Success");
            }
        }
    }
}
