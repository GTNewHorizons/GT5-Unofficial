package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.benderRecipes;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.covers.CoverRegistry;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.material.GTMaterialIcons;
import gregtech.api.material.MaterialUtils;
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
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        registerBenderRecipe(material);
        registerCover(stack, material);
    }

    private void registerBenderRecipe(Material material) {
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, GTOreDictUnificator.get(OrePrefixes.plate, material, 4L)))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.foil, material, 4L))
            .duration((int) Math.max(MaterialUtils.mass(material), 1L))
            .eut(calculateRecipeEU(material, 24))
            .addTo(benderRecipes);
    }

    private void registerCover(ItemStack stack, Material material) {
        CoverRegistry.registerDecorativeCover(
            stack,
            TextureFactory.builder()
                .addIcon(GTMaterialIcons.block("foil", material))
                .setRGBA(MaterialUtils.rgba(material))
                .untintOverrideIcon()
                .build());
    }
}
