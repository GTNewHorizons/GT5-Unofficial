package gregtech.loaders.oreprocessing;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipeRegistrator;
import gregtech.api.util.GTUtility;
import gregtech.common.GTProxy;

public class ProcessingFineWire implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingFineWire() {
        OrePrefixes.wireFine.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        if (!aMaterial.contains(gregtech.api.enums.SubTag.NO_SMASHING)) {
            GTRecipeRegistrator.registerWiremillRecipes(aMaterial, 100, 4);
        }
        if ((aMaterial.mUnificatable) && (aMaterial.mMaterialInto == aMaterial)
            && !aMaterial.contains(SubTag.NO_WORKING)) {
            GTModHandler.addCraftingRecipe(
                GTUtility.copyAmount(1, aStack),
                GTProxy.tBits,
                new Object[] { "Xx", 'X', OrePrefixes.foil.get(aMaterial) });
        }
    }
}
