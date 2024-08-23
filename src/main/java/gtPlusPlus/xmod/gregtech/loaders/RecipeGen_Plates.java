package gtPlusPlus.xmod.gregtech.loaders;

import static gregtech.api.recipe.RecipeMaps.alloySmelterRecipes;
import static gregtech.api.recipe.RecipeMaps.benderRecipes;
import static gregtech.api.recipe.RecipeMaps.cutterRecipes;
import static gregtech.api.recipe.RecipeMaps.hammerRecipes;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.item.ItemStack;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class RecipeGen_Plates extends RecipeGen_Base {

    public static final Set<RunnableWithInfo<Material>> mRecipeGenMap = new HashSet<>();

    static {
        MaterialGenerator.mRecipeMapsToGenerate.put(mRecipeGenMap);
    }

    public RecipeGen_Plates(final Material M) {
        this.toGenerate = M;
        mRecipeGenMap.add(this);
    }

    @Override
    public void run() {
        generateRecipes(this.toGenerate);
    }

    private void generateRecipes(final Material material) {

        final ItemStack ingotStackOne = material.getIngot(1);
        final ItemStack ingotStackTwo = material.getIngot(2);
        final ItemStack ingotStackThree = material.getIngot(3);
        final ItemStack ingotStackNine = material.getIngot(9);
        final ItemStack shape_Mold = ItemList.Shape_Mold_Plate.get(0);
        final ItemStack plate_Single = material.getPlate(1);
        final ItemStack plate_SingleTwo = material.getPlate(2);
        final ItemStack plate_SingleNine = material.getPlate(9);
        final ItemStack plate_Double = material.getPlateDouble(1);
        final ItemStack plate_Dense = material.getPlateDense(1);
        final ItemStack foil_SingleFour = material.getFoil(4);
        final ItemStack block = material.getBlock(1);

        Logger.WARNING("Generating Plate recipes for " + material.getLocalizedName());

        // Forge Hammer
        if (ItemUtils.checkForInvalidItems(ingotStackTwo) && ItemUtils.checkForInvalidItems(plate_Single)) {
            GT_Values.RA.stdBuilder()
                .itemInputs(ingotStackThree)
                .itemOutputs(plate_SingleTwo)
                .duration(Math.max(material.getMass(), 1L))
                .eut(material.vVoltageMultiplier)
                .addTo(hammerRecipes);

            Logger.WARNING("Forge Hammer Recipe: " + material.getLocalizedName() + " - Success");
        }

        // Bender
        if (ItemUtils.checkForInvalidItems(ingotStackOne) && ItemUtils.checkForInvalidItems(plate_Single)) {
            GT_Values.RA.stdBuilder()
                .itemInputs(ingotStackOne, GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(plate_Single)
                .duration(Math.max(material.getMass() * 1L, 1L))
                .eut(material.vVoltageMultiplier)
                .addTo(benderRecipes);

            Logger.WARNING("Bender Recipe: " + material.getLocalizedName() + " - Success");
        }

        if (ItemUtils.checkForInvalidItems(ingotStackOne) && ItemUtils.checkForInvalidItems(foil_SingleFour)) {
            GT_Values.RA.stdBuilder()
                .itemInputs(ingotStackOne, GT_Utility.getIntegratedCircuit(10))
                .itemOutputs(foil_SingleFour)
                .duration(Math.max(material.getMass() * 2L, 1L))
                .eut(material.vVoltageMultiplier)
                .addTo(benderRecipes);

            Logger.WARNING("Bender Recipe: " + material.getLocalizedName() + " - Success");
        }

        // Alloy Smelter
        if (ItemUtils.checkForInvalidItems(ingotStackTwo) && ItemUtils.checkForInvalidItems(plate_Single)) {
            GT_Values.RA.stdBuilder()
                .itemInputs(ingotStackTwo, shape_Mold)
                .itemOutputs(plate_Single)
                .duration(Math.max(material.getMass() * 2L, 1L))
                .eut(material.vVoltageMultiplier)
                .addTo(alloySmelterRecipes);

            Logger.WARNING("Alloy Smelter Recipe: " + material.getLocalizedName() + " - Success");
        }
        // Cutting Machine
        if (ItemUtils.checkForInvalidItems(block) && ItemUtils.checkForInvalidItems(plate_Single)) {
            GT_Values.RA.stdBuilder()
                .itemInputs(block)
                .itemOutputs(plate_SingleNine)
                .duration(Math.max(material.getMass() * 10L, 1L))
                .eut(material.vVoltageMultiplier)
                .addTo(cutterRecipes);

            Logger.WARNING("Cutting Machine Recipe: " + material.getLocalizedName() + " - Success");
        }

        // Making Double Plates
        if (ItemUtils.checkForInvalidItems(ingotStackTwo) && ItemUtils.checkForInvalidItems(plate_Double)) {
            GT_Values.RA.stdBuilder()
                .itemInputs(ingotStackTwo, GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(plate_Double)
                .duration(Math.max(material.getMass() * 2L, 1L))
                .eut(material.vVoltageMultiplier)
                .addTo(benderRecipes);

            Logger.WARNING("Bender Recipe: " + material.getLocalizedName() + " - Success");
        }

        if (ItemUtils.checkForInvalidItems(plate_SingleTwo) && ItemUtils.checkForInvalidItems(plate_Double)) {
            GT_Values.RA.stdBuilder()
                .itemInputs(plate_SingleTwo, GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(plate_Double)
                .duration(Math.max(material.getMass() * 2L, 1L))
                .eut(material.vVoltageMultiplier)
                .addTo(benderRecipes);
            Logger.WARNING("Bender Recipe: " + material.getLocalizedName() + " - Success");
        }

        // Bender
        if (ItemUtils.checkForInvalidItems(material.getPlate(1))
            && ItemUtils.checkForInvalidItems(material.getFoil(1))) {
            GT_Values.RA.stdBuilder()
                .itemInputs(material.getPlate(1), GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(material.getFoil(4))
                .duration(Math.max(material.getMass(), 1L))
                .eut(material.vVoltageMultiplier)
                .addTo(benderRecipes);

            GregTech_API.registerCover(
                material.getFoil(1),
                new GT_RenderedTexture(material.getTextureSet().mTextures[70], material.getRGBA(), false),
                null);
            Logger.WARNING("Bender Foil Recipe: " + material.getLocalizedName() + " - Success");
        }

        // Making Dense Plates
        if (ItemUtils.checkForInvalidItems(ingotStackNine) && ItemUtils.checkForInvalidItems(plate_Dense)) {
            GT_Values.RA.stdBuilder()
                .itemInputs(ingotStackNine, GT_Utility.getIntegratedCircuit(9))
                .itemOutputs(plate_Dense)
                .duration(Math.max(material.getMass() * 2L, 1L))
                .eut(material.vVoltageMultiplier)
                .addTo(benderRecipes);

            Logger.WARNING("Bender Recipe: " + material.getLocalizedName() + " - Success");
        }

        if (ItemUtils.checkForInvalidItems(plate_SingleNine) && ItemUtils.checkForInvalidItems(plate_Dense)) {
            GT_Values.RA.stdBuilder()
                .itemInputs(plate_SingleNine, GT_Utility.getIntegratedCircuit(9))
                .itemOutputs(plate_Dense)
                .duration(Math.max(material.getMass() * 2L, 1L))
                .eut(material.vVoltageMultiplier)
                .addTo(benderRecipes);

            Logger.WARNING("Bender Recipe: " + material.getLocalizedName() + " - Success");
        }

    }
}
