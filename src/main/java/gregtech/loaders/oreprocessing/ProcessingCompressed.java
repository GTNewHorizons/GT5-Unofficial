package gregtech.loaders.oreprocessing;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.covers.CoverRegistry;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.material.GTMaterialIcons;
import gregtech.api.material.MaterialUtils;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;

public class ProcessingCompressed implements IOreRecipeRegistrator {

    public ProcessingCompressed() {
        OrePrefixes.compressed.add(this);
    }

    /// Covers draw the single base texture; a material's icon layer stack does not apply to cover art.
    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        GTModHandler.removeRecipeByOutputDelayed(stack);
        CoverRegistry.registerDecorativeCover(
            stack,
            TextureFactory.builder()
                .addIcon(GTMaterialIcons.block("block2", material))
                .setRGBA(MaterialUtils.rgba(material))
                .untintOverrideIcon()
                .build());
    }
}
