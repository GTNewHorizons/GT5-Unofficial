package gregtech.loaders.oreprocessing;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Proxy;

public class ProcessingBolt implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingBolt() {
        OrePrefixes.bolt.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        if (aMaterial.getProcessingMaterialTierEU() >= TierEU.IV){
            return;
        }

        if (!aMaterial.mUnificatable){
            return;
        }

        if ((aMaterial.mMaterialInto != aMaterial)){
            return;
        }

        if (aMaterial.contains(SubTag.NO_WORKING)){
            return;
        }

        GT_ModHandler.addCraftingRecipe(
            GT_Utility.copyAmount(2L, aStack),
            GT_Proxy.tBits,
            new Object[] { "s ", " X", 'X', OrePrefixes.stick.get(aMaterial) });

    }
}
