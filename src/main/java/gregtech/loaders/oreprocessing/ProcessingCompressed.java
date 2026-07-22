package gregtech.loaders.oreprocessing;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.covers.CoverRegistry;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.material.MU;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;

public class ProcessingCompressed implements IOreRecipeRegistrator {

    public ProcessingCompressed() {
        OrePrefixes.compressed.add(this);
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

        GTModHandler.removeRecipeByOutputDelayed(stack);
        CoverRegistry
            .registerDecorativeCover(stack, TextureFactory.of(MU.iconSet(material).mTextures[72], MU.rgba(material)));
    }
}
