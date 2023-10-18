package gtPlusPlus.xmod.gregtech.loaders;

import java.util.HashSet;
import java.util.Set;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class RecipeGen_Fluids extends RecipeGen_Base {

    public static final Set<RunnableWithInfo<Material>> mRecipeGenMap = new HashSet<>();

    static {
        MaterialGenerator.mRecipeMapsToGenerate.put(mRecipeGenMap);
    }

    public RecipeGen_Fluids(final Material M) {
        this(M, false);
    }

    public RecipeGen_Fluids(final Material M, final boolean dO) {
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
        if (material.getFluidStack(1) != null
                && !material.getFluidStack(1).getUnlocalizedName().toLowerCase().contains("plasma")) {

            // Making Shapes from fluid

            // Ingot
            if (ItemUtils.checkForInvalidItems(material.getIngot(1))) if (GT_Values.RA.addFluidSolidifierRecipe(
                    ItemList.Shape_Mold_Ingot.get(0), // Item Shape
                    material.getFluidStack(144), // Fluid Input
                    material.getIngot(1), // output
                    32, // Duration
                    material.vVoltageMultiplier // Eu Tick
            )) {
                Logger.WARNING("144l fluid molder for 1 ingot Recipe: " + material.getLocalizedName() + " - Success");
            } else {
                Logger.WARNING("144l fluid molder for 1 ingot Recipe: " + material.getLocalizedName() + " - Failed");
            }

            // Plate
            if (ItemUtils.checkForInvalidItems(material.getPlate(1))) if (GT_Values.RA.addFluidSolidifierRecipe(
                    ItemList.Shape_Mold_Plate.get(0), // Item Shape
                    material.getFluidStack(144), // Fluid Input
                    material.getPlate(1), // output
                    32, // Duration
                    material.vVoltageMultiplier // Eu Tick
            )) {
                Logger.WARNING("144l fluid molder for 1 plate Recipe: " + material.getLocalizedName() + " - Success");
            } else {
                Logger.WARNING("144l fluid molder for 1 plate Recipe: " + material.getLocalizedName() + " - Failed");
            }

            // Nugget
            if (ItemUtils.checkForInvalidItems(material.getNugget(1))) if (GT_Values.RA.addFluidSolidifierRecipe(
                    ItemList.Shape_Mold_Nugget.get(0), // Item Shape
                    material.getFluidStack(16), // Fluid Input
                    material.getNugget(1), // output
                    16, // Duration
                    material.vVoltageMultiplier // Eu Tick
            )) {
                Logger.WARNING("16l fluid molder for 1 nugget Recipe: " + material.getLocalizedName() + " - Success");
            } else {
                Logger.WARNING("16l fluid molder for 1 nugget Recipe: " + material.getLocalizedName() + " - Failed");
            }

            // Gears
            if (ItemUtils.checkForInvalidItems(material.getGear(1))) if (GT_Values.RA.addFluidSolidifierRecipe(
                    ItemList.Shape_Mold_Gear.get(0), // Item Shape
                    material.getFluidStack(576), // Fluid Input
                    material.getGear(1), // output
                    128, // Duration
                    material.vVoltageMultiplier // Eu Tick
            )) {
                Logger.WARNING("576l fluid molder for 1 gear Recipe: " + material.getLocalizedName() + " - Success");
            } else {
                Logger.WARNING("576l fluid molder for 1 gear Recipe: " + material.getLocalizedName() + " - Failed");
            }

            // Blocks
            if (ItemUtils.checkForInvalidItems(material.getBlock(1))) if (GT_Values.RA.addFluidSolidifierRecipe(
                    ItemList.Shape_Mold_Block.get(0), // Item Shape
                    material.getFluidStack(144 * 9), // Fluid Input
                    material.getBlock(1), // output
                    288, // Duration
                    material.vVoltageMultiplier // Eu Tick
            )) {
                Logger.WARNING(
                        (144 * 9) + "l fluid molder from 1 block Recipe: "
                                + material.getLocalizedName()
                                + " - Success");
            } else {
                Logger.WARNING(
                        (144 * 9) + "l fluid molder from 1 block Recipe: " + material.getLocalizedName() + " - Failed");
            }

            // GTNH

            // Rod
            if (ItemUtils.checkForInvalidItems(material.getRod(1))) if (GT_Values.RA.addFluidSolidifierRecipe(
                    ItemList.Shape_Mold_Rod.get(0), // Item Shape
                    material.getFluidStack(72), // Fluid Input
                    material.getRod(1), // output
                    150, // Duration
                    material.vVoltageMultiplier // Eu Tick
            )) {
                Logger.WARNING(
                        (144 * 9) + "l fluid molder from 1 rod Recipe: " + material.getLocalizedName() + " - Success");
            } else {
                Logger.WARNING(
                        (144 * 9) + "l fluid molder from 1 rod Recipe: " + material.getLocalizedName() + " - Failed");
            }

            // Rod Long
            if (ItemUtils.checkForInvalidItems(material.getLongRod(1))) if (GT_Values.RA.addFluidSolidifierRecipe(
                    ItemList.Shape_Mold_Rod_Long.get(0), // Item
                    // Shape
                    material.getFluidStack(144), // Fluid Input
                    material.getLongRod(1), // output
                    300, // Duration
                    material.vVoltageMultiplier // Eu Tick
            )) {
                Logger.WARNING(
                        (144 * 9) + "l fluid molder from 1 rod long Recipe: "
                                + material.getLocalizedName()
                                + " - Success");
            } else {
                Logger.WARNING(
                        (144 * 9) + "l fluid molder from 1 rod long Recipe: "
                                + material.getLocalizedName()
                                + " - Failed");
            }

            // Bolt
            if (ItemUtils.checkForInvalidItems(material.getBolt(1))) if (GT_Values.RA.addFluidSolidifierRecipe(
                    ItemList.Shape_Mold_Bolt.get(0), // Item Shape
                    material.getFluidStack(18), // Fluid Input
                    material.getBolt(1), // output
                    50, // Duration
                    material.vVoltageMultiplier // Eu Tick
            )) {
                Logger.WARNING(
                        (144 * 9) + "l fluid molder from 1 bolt Recipe: " + material.getLocalizedName() + " - Success");
            } else {
                Logger.WARNING(
                        (144 * 9) + "l fluid molder from 1 bolt Recipe: " + material.getLocalizedName() + " - Failed");
            }

            // Screw
            if (ItemUtils.checkForInvalidItems(material.getScrew(1))) if (GT_Values.RA.addFluidSolidifierRecipe(
                    ItemList.Shape_Mold_Screw.get(0), // Item Shape
                    material.getFluidStack(18), // Fluid Input
                    material.getScrew(1), // output
                    50, // Duration
                    material.vVoltageMultiplier // Eu Tick
            )) {
                Logger.WARNING(
                        (144 * 9) + "l fluid molder from 1 screw Recipe: "
                                + material.getLocalizedName()
                                + " - Success");
            } else {
                Logger.WARNING(
                        (144 * 9) + "l fluid molder from 1 screw Recipe: " + material.getLocalizedName() + " - Failed");
            }

            // Ring
            if (ItemUtils.checkForInvalidItems(material.getRing(1))) if (GT_Values.RA.addFluidSolidifierRecipe(
                    ItemList.Shape_Mold_Ring.get(0), // Item Shape
                    material.getFluidStack(36), // Fluid Input
                    material.getRing(1), // output
                    100, // Duration
                    material.vVoltageMultiplier // Eu Tick
            )) {
                Logger.WARNING(
                        (144 * 9) + "l fluid molder from 1 ring Recipe: " + material.getLocalizedName() + " - Success");
            } else {
                Logger.WARNING(
                        (144 * 9) + "l fluid molder from 1 ring Recipe: " + material.getLocalizedName() + " - Failed");
            }

            // Rotor
            if (ItemUtils.checkForInvalidItems(material.getRotor(1))) if (GT_Values.RA.addFluidSolidifierRecipe(
                    ItemList.Shape_Mold_Rotor.get(0), // Item Shape
                    material.getFluidStack(612), // Fluid Input
                    material.getRotor(1), // output
                    100, // Duration
                    material.vVoltageMultiplier // Eu Tick
            )) {
                Logger.WARNING(
                        (144 * 9) + "l fluid molder from 1 rotor Recipe: "
                                + material.getLocalizedName()
                                + " - Success");
            } else {
                Logger.WARNING(
                        (144 * 9) + "l fluid molder from 1 rotor Recipe: " + material.getLocalizedName() + " - Failed");
            }
        }
    }
}
