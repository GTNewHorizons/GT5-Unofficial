package gregtech.loaders.oreprocessing;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;

public class ProcessingBolt implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public static ProcessingBolt INSTANCE;

    public ProcessingBolt() {
        INSTANCE = this;
        OrePrefixes.bolt.add(this);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        if (MaterialUtils.processingMaterialTierEU(material) >= TierEU.IV) {
            return;
        }

        if (!MaterialUtils.unifiable(material)) {
            return;
        }

        if (MaterialUtils.hasFlag(material, GTMaterialFlag.NO_WORKING)) {
            return;
        }

        GTModHandler.addCraftingRecipe(
            GTUtility.copyAmount(2, stack),
            GTModHandler.RecipeBits.BITS_STD,
            new Object[] { "s ", " X", 'X', MaterialParts.craftIngredient(OrePrefixes.stick, material) });

    }
}
