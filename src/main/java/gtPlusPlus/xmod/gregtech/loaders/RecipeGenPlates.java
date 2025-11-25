package gtPlusPlus.xmod.gregtech.loaders;

import static gregtech.api.recipe.RecipeMaps.alloySmelterRecipes;
import static gregtech.api.recipe.RecipeMaps.benderRecipes;
import static gregtech.api.recipe.RecipeMaps.cutterRecipes;
import static gregtech.api.recipe.RecipeMaps.hammerRecipes;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.item.ItemStack;

import gregtech.api.covers.CoverRegistry;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.render.TextureFactory;
import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;

public class RecipeGenPlates extends RecipeGenBase {

    public static final Set<RunnableWithInfo<Material>> mRecipeGenMap = new HashSet<>();

    static {
        MaterialGenerator.mRecipeMapsToGenerate.add(mRecipeGenMap);
    }

    public RecipeGenPlates(final Material M) {
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
        if (ingotStackTwo != null && plate_Single != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(ingotStackThree)
                .itemOutputs(plate_SingleTwo)
                .duration(Math.max(material.getMass(), 1L))
                .eut(material.vVoltageMultiplier)
                .addTo(hammerRecipes);

            Logger.WARNING("Forge Hammer Recipe: " + material.getLocalizedName() + " - Success");
        }

        // Bender
        if (ingotStackOne != null && plate_Single != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(ingotStackOne)
                .circuit(1)
                .itemOutputs(plate_Single)
                .duration(Math.max(material.getMass() * 1L, 1L))
                .eut(material.vVoltageMultiplier)
                .addTo(benderRecipes);

            Logger.WARNING("Bender Recipe: " + material.getLocalizedName() + " - Success");
        }

        if (ingotStackOne != null && foil_SingleFour != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(ingotStackOne)
                .circuit(10)
                .itemOutputs(foil_SingleFour)
                .duration(Math.max(material.getMass() * 2L, 1L))
                .eut(material.vVoltageMultiplier)
                .addTo(benderRecipes);

            Logger.WARNING("Bender Recipe: " + material.getLocalizedName() + " - Success");
        }

        // Alloy Smelter
        if (ingotStackTwo != null && plate_Single != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(ingotStackTwo, shape_Mold)
                .itemOutputs(plate_Single)
                .duration(Math.max(material.getMass() * 2L, 1L))
                .eut(material.vVoltageMultiplier)
                .addTo(alloySmelterRecipes);

            Logger.WARNING("Alloy Smelter Recipe: " + material.getLocalizedName() + " - Success");
        }
        // Cutting Machine
        if (block != null && plate_Single != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(block)
                .itemOutputs(plate_SingleNine)
                .duration(Math.max(material.getMass() * 10L, 1L))
                .eut(material.vVoltageMultiplier)
                .addTo(cutterRecipes);

            Logger.WARNING("Cutting Machine Recipe: " + material.getLocalizedName() + " - Success");
        }

        // Making Double Plates
        if (ingotStackTwo != null && plate_Double != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(ingotStackTwo)
                .circuit(2)
                .itemOutputs(plate_Double)
                .duration(Math.max(material.getMass() * 2L, 1L))
                .eut(material.vVoltageMultiplier)
                .addTo(benderRecipes);

            Logger.WARNING("Bender Recipe: " + material.getLocalizedName() + " - Success");
        }

        if (plate_SingleTwo != null && plate_Double != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(plate_SingleTwo)
                .circuit(2)
                .itemOutputs(plate_Double)
                .duration(Math.max(material.getMass() * 2L, 1L))
                .eut(material.vVoltageMultiplier)
                .addTo(benderRecipes);
            Logger.WARNING("Bender Recipe: " + material.getLocalizedName() + " - Success");
        }

        // Bender
        if (material.getPlate(1) != null && material.getFoil(1) != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(material.getPlate(1))
                .circuit(1)
                .itemOutputs(material.getFoil(4))
                .duration(Math.max(material.getMass(), 1L))
                .eut(material.vVoltageMultiplier)
                .addTo(benderRecipes);

            CoverRegistry.registerDecorativeCover(
                material.getFoil(1),
                TextureFactory.of(material.getTextureSet().mTextures[70], material.getRGBA(), false));
            Logger.WARNING("Bender Foil Recipe: " + material.getLocalizedName() + " - Success");
        }

        // Making Dense Plates
        if (ingotStackNine != null && plate_Dense != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(ingotStackNine)
                .circuit(9)
                .itemOutputs(plate_Dense)
                .duration(Math.max(material.getMass() * 2L, 1L))
                .eut(material.vVoltageMultiplier)
                .addTo(benderRecipes);

            Logger.WARNING("Bender Recipe: " + material.getLocalizedName() + " - Success");
        }

        if (plate_SingleNine != null && plate_Dense != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(plate_SingleNine)
                .circuit(9)
                .itemOutputs(plate_Dense)
                .duration(Math.max(material.getMass() * 2L, 1L))
                .eut(material.vVoltageMultiplier)
                .addTo(benderRecipes);

            Logger.WARNING("Bender Recipe: " + material.getLocalizedName() + " - Success");
        }

    }
}
