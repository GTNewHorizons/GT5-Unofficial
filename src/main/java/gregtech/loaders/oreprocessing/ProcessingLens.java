package gregtech.loaders.oreprocessing;

import net.minecraft.item.ItemStack;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_OreDictUnificator;

public class ProcessingLens implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingLens() {
        OrePrefixes.lens.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
            ItemStack aStack) {
        switch (aMaterial.mName) {
            case "Diamond":
            case "Glass":
                GT_Values.RA.addLatheRecipe(
                        GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.lens, aMaterial, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dustSmall, aMaterial, 1L),
                        1200,
                        30);
                GT_Values.RA.addLatheRecipe(
                        GT_OreDictUnificator.get(OrePrefixes.gemExquisite, aMaterial, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.lens, aMaterial, 3L),
                        GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L),
                        2400,
                        16);
                break;
            case "ChromaticGlass":
                GT_Values.RA.addCompressorRecipe(
                        GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, 16L),
                        GT_OreDictUnificator.get(OrePrefixes.lens, aMaterial, 1L),
                        600 * 20,
                        1966080);
                break;
            default:
                GT_Values.RA.addLatheRecipe(
                        GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.lens, aMaterial, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dustSmall, aMaterial, 1L),
                        1200,
                        120);
                GT_Values.RA.addLatheRecipe(
                        GT_OreDictUnificator.get(OrePrefixes.gemExquisite, aMaterial, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.lens, aMaterial, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, 2L),
                        2400,
                        30);
                final ITexture lensCoverTexture = TextureFactory.of(
                        Textures.BlockIcons.OVERLAY_LENS,
                        aMaterial.mRGBa,
                        false);
                GregTech_API.registerCover(
                        aStack,
                        TextureFactory.of(Textures.BlockIcons.MACHINE_CASINGS[2][0], lensCoverTexture),
                        new gregtech.common.covers.GT_Cover_Lens(aMaterial.mColor.mIndex, lensCoverTexture));
        }
    }
}
