package gregtech.loaders.oreprocessing;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.covers.CoverRegistry;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.material.GTMaterialIcons;
import gregtech.api.material.GTMaterialTextures;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTModHandler;

public class ProcessingCompressed implements PartRecipeGenerator {

    public static final ProcessingCompressed INSTANCE = new ProcessingCompressed();

    private ProcessingCompressed() {}

    @Override
    public void generate(OrePrefixes prefix, Material material, String oreDictName, ItemStack stack) {
        GTModHandler.removeRecipeByOutputDelayed(stack);
        CoverRegistry.registerDecorativeCover(
            stack,
            GTMaterialTextures.stored(GTMaterialIcons.block("block2", material), () -> MaterialUtils.rgba(material)));
    }
}
