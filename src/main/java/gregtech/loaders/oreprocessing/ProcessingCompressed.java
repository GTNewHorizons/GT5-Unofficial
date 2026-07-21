package gregtech.loaders.oreprocessing;

import net.minecraft.item.ItemStack;

import gregtech.api.covers.CoverRegistry;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;

public class ProcessingCompressed implements IOreRecipeRegistrator {

    public ProcessingCompressed() {
        OrePrefixes.compressed.add(this);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Materials material, String oreDictName, String modName,
        ItemStack stack) {
        GTModHandler.removeRecipeByOutputDelayed(stack);
        CoverRegistry
            .registerDecorativeCover(stack, TextureFactory.of(material.mIconSet.mTextures[72], material.mRGBa));
    }
}
