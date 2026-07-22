package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.benderRecipes;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.covers.CoverRegistry;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.material.MU;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class ProcessingFoil implements IOreRecipeRegistrator {

    public static ProcessingFoil INSTANCE;

    public ProcessingFoil() {
        INSTANCE = this;
        OrePrefixes.foil.add(this);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Materials material, String oreDictName, String modName,
        ItemStack stack) {
        registerOre(prefix, MU.material(material), oreDictName, modName, stack);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        Materials legacyMaterial = MU.materialOf(material);
        if (legacyMaterial == null) return;

        // Blacklist materials which are handled by Werkstoff loader
        if (legacyMaterial == Materials.Calcium || legacyMaterial == Materials.Magnesia) return;

        registerBenderRecipe(material, legacyMaterial);
        registerCover(stack, material, legacyMaterial);
    }

    private void registerBenderRecipe(Material material, Materials legacyMaterial) {
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, GTOreDictUnificator.get(OrePrefixes.plate, material, 4L)))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.foil, material, 4L))
            .duration((int) Math.max(legacyMaterial.getMass(), 1L))
            .eut(calculateRecipeEU(legacyMaterial, 24))
            .addTo(benderRecipes);
    }

    private void registerCover(ItemStack stack, Material material, Materials legacyMaterial) {
        CoverRegistry.registerDecorativeCover(
            stack,
            TextureFactory.of(legacyMaterial.mIconSet.mTextures[70], MU.rgba(material)));
    }
}
