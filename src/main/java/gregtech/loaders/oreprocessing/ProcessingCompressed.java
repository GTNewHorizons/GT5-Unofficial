package gregtech.loaders.oreprocessing;

import net.minecraft.item.ItemStack;

import gregtech.api.GregTechAPI;
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
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        GTModHandler.removeRecipeByOutputDelayed(aStack);
        GregTechAPI
            .registerCover(aStack, TextureFactory.of(aMaterial.mIconSet.mTextures[72], aMaterial.mRGBa, false), null);
    }
}
