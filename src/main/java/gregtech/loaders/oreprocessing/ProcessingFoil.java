package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.benderRecipes;
import static gregtech.api.util.GT_Utility.calculateRecipeEU;

import net.minecraft.item.ItemStack;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

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
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.copyAmount(1, GT_OreDictUnificator.get(OrePrefixes.plate, material, 4L)),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.foil, material, 4L))
            .duration((int) Math.max(material.getMass(), 1L))
            .eut(calculateRecipeEU(material, 24))
            .addTo(benderRecipes);
    }

    private void registerCover(ItemStack stack, Materials material) {
        GregTech_API
            .registerCover(stack, TextureFactory.of(material.mIconSet.mTextures[70], material.mRGBa, false), null);
    }
}
