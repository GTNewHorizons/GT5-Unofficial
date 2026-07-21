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
    public void registerOre(OrePrefixes prefix, Materials material, String oreDictName, String modName,
        ItemStack stack) {
        if (material.getProcessingMaterialTierEU() >= TierEU.IV) {
            return;
        }

        if (!material.mUnifiable) {
            return;
        }

        if ((material.mMaterialInto != material)) {
            return;
        }

        if (MU.hasFlag(material, GTMaterialFlag.NO_WORKING)) {
            return;
        }

        GTModHandler.addCraftingRecipe(
            GTUtility.copyAmount(2, stack),
            GTModHandler.RecipeBits.BITS_STD,
            new Object[] { "s ", " X", 'X', OrePrefixes.stick.ingredient(material) });

    }
}
