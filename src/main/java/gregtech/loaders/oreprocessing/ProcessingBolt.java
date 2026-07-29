package gregtech.loaders.oreprocessing;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MU;
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
        Integer processingTierEU = material.getProperty(GTMaterialProperties.PROCESSING_MATERIAL_TIER_EU);
        if ((processingTierEU == null ? 0 : processingTierEU) >= TierEU.IV) {
            return;
        }

        if (Boolean.FALSE.equals(material.getProperty(GTMaterialProperties.UNIFIABLE))) {
            return;
        }

        if (MaterialUtils.hasFlag(material, GTMaterialFlag.NO_WORKING)) {
            return;
        }

        GTModHandler.addCraftingRecipe(
            GTUtility.copyAmount(2, stack),
            GTModHandler.RecipeBits.BITS_STD,
            new Object[] { "s ", " X", 'X', MU.craftIngredient(OrePrefixes.stick, material) });

    }
}
