package gregtech.loaders.oreprocessing;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;

public class ProcessingBolt implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingBolt() {
        OrePrefixes.bolt.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        if (aMaterial.getProcessingMaterialTierEU() >= TierEU.IV) {
            return;
        }

        if (!aMaterial.mUnifiable) {
            return;
        }

        if ((aMaterial.mMaterialInto != aMaterial)) {
            return;
        }

        if (aMaterial.contains(SubTag.NO_WORKING)) {
            return;
        }

        GTModHandler.addCraftingRecipe(
            GTUtility.copyAmount(2, aStack),
            GTModHandler.RecipeBits.BITS_STD,
            new Object[] { "s ", " X", 'X', OrePrefixes.stick.get(aMaterial) });

    }
}
