package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.benderRecipes;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import net.minecraft.item.ItemStack;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class ProcessingFoil implements IOreRecipeRegistrator {

    public ProcessingFoil() {
        OrePrefixes.foil.add(this);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Materials material, String oreDictName, String modName,
        ItemStack stack) {
        registerBenderRecipe(material);
        registerCover(stack, material);
    }

    private void registerBenderRecipe(Materials material) {
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.copyAmount(1, GTOreDictUnificator.get(OrePrefixes.plate, material, 4L)),
                GTUtility.getIntegratedCircuit(1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.foil, material, 4L))
            .duration((int) Math.max(material.getMass(), 1L))
            .eut(calculateRecipeEU(material, 24))
            .addTo(benderRecipes);
    }

    private void registerCover(ItemStack stack, Materials material) {
        GregTechAPI
            .registerCover(stack, TextureFactory.of(material.mIconSet.mTextures[70], material.mRGBa, false), null);
    }
}
