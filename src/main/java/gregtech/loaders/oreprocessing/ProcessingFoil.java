package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.benderRecipes;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.covers.CoverRegistry;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.material.GTMaterialIcons;
import gregtech.api.material.GTMaterialTextures;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTUtility;

public class ProcessingFoil implements IOreRecipeRegistrator {

    public static ProcessingFoil INSTANCE;

    public ProcessingFoil() {
        INSTANCE = this;
    }

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        // ArtificialMicaLine bends Fluorophlogopite plates on the same circuit into Mica Insulator Foil
        if (material != Materials.Fluorophlogopite) registerBenderRecipe(material);
        registerCover(stack, material);
    }

    private void registerBenderRecipe(Material material) {
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, MaterialLibAPI.findStack(material, Shapes.plate, 4)))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.findStack(material, Shapes.foil, 4))
            .duration((int) Math.max(MaterialUtils.mass(material), 1L))
            .eut(calculateRecipeEU(material, 24))
            .addTo(benderRecipes);
    }

    private void registerCover(ItemStack stack, Material material) {
        CoverRegistry.registerDecorativeCover(
            stack,
            GTMaterialTextures.stored(GTMaterialIcons.block("foil", material), () -> MaterialUtils.rgba(material)));
    }
}
