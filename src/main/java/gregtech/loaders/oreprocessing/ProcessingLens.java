package gregtech.loaders.oreprocessing;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraft.item.ItemStack;

public class ProcessingLens implements gregtech.api.interfaces.IOreRecipeRegistrator {
    public ProcessingLens() {
        OrePrefixes.lens.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName, ItemStack aStack) {
        switch (aMaterial.mName) {
            case "Diamond":
            case "Glass":
                GT_Values.RA.addLatheRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L), GT_OreDictUnificator.get(OrePrefixes.lens, aMaterial, 1L), GT_OreDictUnificator.get(OrePrefixes.dustSmall, aMaterial, 1L), 1200, 30);
                GT_Values.RA.addLatheRecipe(GT_OreDictUnificator.get(OrePrefixes.gemExquisite, aMaterial, 1L), GT_OreDictUnificator.get(OrePrefixes.lens, aMaterial, 3L), GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L), 2400, 16);
                break;
            default:
                GT_Values.RA.addLatheRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L), GT_OreDictUnificator.get(OrePrefixes.lens, aMaterial, 1L), GT_OreDictUnificator.get(OrePrefixes.dustSmall, aMaterial, 1L), 1200, 120);
                GT_Values.RA.addLatheRecipe(GT_OreDictUnificator.get(OrePrefixes.gemExquisite, aMaterial, 1L), GT_OreDictUnificator.get(OrePrefixes.lens, aMaterial, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, 2L), 2400, 30);
                GregTech_API.registerCover(aStack, TextureFactory.of(Textures.BlockIcons.MACHINE_CASINGS[2][0], TextureFactory.of(Textures.BlockIcons.OVERLAY_LENS, aMaterial.mRGBa, false)), new gregtech.common.covers.GT_Cover_Lens(aMaterial.mColor.mIndex));
        }
    }
}
