package gregtech.loaders.oreprocessing;

import net.minecraft.item.ItemStack;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_ModHandler;

public class ProcessingCompressed implements IOreRecipeRegistrator {

    public ProcessingCompressed() {
        OrePrefixes.compressed.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        GT_ModHandler.removeRecipeByOutputDelayed(aStack);
        GregTech_API
            .registerCover(aStack, TextureFactory.of(aMaterial.mIconSet.mTextures[72], aMaterial.mRGBa, false), null);
        // GT_RecipeRegistrator.registerUsagesForMaterials(null, false, GT_Utility.copyAmount(1L, aStack));
    }
}
