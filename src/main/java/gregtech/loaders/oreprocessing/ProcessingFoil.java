package gregtech.loaders.oreprocessing;

import static gregtech.api.util.GT_Utility.calculateRecipeEU;

import net.minecraft.item.ItemStack;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

public class ProcessingFoil implements IOreRecipeRegistrator {

    public ProcessingFoil() {
        OrePrefixes.foil.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        GT_Values.RA.addBenderRecipe(
            GT_Utility.copyAmount(1L, GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial, 4L)),
            GT_OreDictUnificator.get(OrePrefixes.foil, aMaterial, 4L),
            (int) Math.max(aMaterial.getMass(), 1L),
            calculateRecipeEU(aMaterial, 24));
        GregTech_API
            .registerCover(aStack, TextureFactory.of(aMaterial.mIconSet.mTextures[70], aMaterial.mRGBa, false), null);
    }
}
