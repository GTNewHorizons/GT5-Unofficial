package gregtech.loaders.oreprocessing;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Proxy;
import gregtech.loaders.postload.recipes.WiremillRecipes;

public class ProcessingFineWire implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingFineWire() {
        OrePrefixes.wireFine.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
            ItemStack aStack) {
        if (!aMaterial.contains(gregtech.api.enums.SubTag.NO_SMASHING)) {
            WiremillRecipes.registerWiremillRecipes(aMaterial, 100, 4);
        }
        if ((aMaterial.mUnificatable) && (aMaterial.mMaterialInto == aMaterial)
                && !aMaterial.contains(SubTag.NO_WORKING)) {
            GT_ModHandler.addCraftingRecipe(
                    GT_Utility.copyAmount(1L, aStack),
                    GT_Proxy.tBits,
                    new Object[] { "Xx", 'X', OrePrefixes.foil.get(aMaterial) });
        }
    }
}
