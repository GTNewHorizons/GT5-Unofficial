package gregtech.loaders.oreprocessing;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;

public class ProcessingBolt implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public static ProcessingBolt INSTANCE;

    public ProcessingBolt() {
        INSTANCE = this;
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

        if (MU.hasFlag(aMaterial, GTMaterialFlag.NO_WORKING)) {
            return;
        }

        GTModHandler.addCraftingRecipe(
            GTUtility.copyAmount(2, aStack),
            GTModHandler.RecipeBits.BITS_STD,
            new Object[] { "s ", " X", 'X', OrePrefixes.stick.get(aMaterial) });

    }
}
