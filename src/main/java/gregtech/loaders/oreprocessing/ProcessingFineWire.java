package gregtech.loaders.oreprocessing;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipeRegistrator;
import gregtech.api.util.GTUtility;

public class ProcessingFineWire implements PartRecipeGenerator {

    public static final ProcessingFineWire INSTANCE = new ProcessingFineWire();

    private ProcessingFineWire() {}

    @Override
    public void generate(OrePrefixes prefix, Material material, Shape shape, ItemStack stack) {
        if (!MaterialUtils.hasFlag(material, GTMaterialFlag.NO_SMASHING)) {
            GTRecipeRegistrator.registerWiremillRecipes(material, 100, 4);
        }
        if (MaterialUtils.unifiable(material) && !MaterialUtils.hasFlag(material, GTMaterialFlag.NO_WORKING)) {
            GTModHandler.addCraftingRecipe(
                GTUtility.copyAmount(1, stack),
                GTModHandler.RecipeBits.BITS_STD,
                new Object[] { "Xx", 'X', OrePrefixes.foil.ingredient(material) });
        }
    }
}
