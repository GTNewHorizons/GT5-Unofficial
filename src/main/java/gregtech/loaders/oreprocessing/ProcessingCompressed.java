package gregtech.loaders.oreprocessing;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.covers.CoverRegistry;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.material.GTMaterialIcons;
import gregtech.api.material.GTMaterialTextures;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTModHandler;

public class ProcessingCompressed implements IOreRecipeRegistrator {

    public ProcessingCompressed() {
        OrePrefixes.compressed.add(this);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        GTModHandler.removeRecipeByOutputDelayed(stack);
        CoverRegistry.registerDecorativeCover(
            stack,
            GTMaterialTextures.of(GTMaterialIcons.block("block2", material), MaterialUtils.rgba(material)));
    }
}
