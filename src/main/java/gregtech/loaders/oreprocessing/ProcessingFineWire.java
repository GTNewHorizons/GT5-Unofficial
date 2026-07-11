package gregtech.loaders.oreprocessing;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipeRegistrator;
import gregtech.api.util.GTUtility;

public class ProcessingFineWire implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public static ProcessingFineWire INSTANCE;

    public ProcessingFineWire() {
        INSTANCE = this;
        OrePrefixes.wireFine.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        if (!MU.hasFlag(aMaterial, GTMaterialFlag.NO_SMASHING)) {
            GTRecipeRegistrator.registerWiremillRecipes(aMaterial, 100, 4);
        }
        if ((aMaterial.mUnifiable) && (aMaterial.mMaterialInto == aMaterial)
            && !MU.hasFlag(aMaterial, GTMaterialFlag.NO_WORKING)) {
            GTModHandler.addCraftingRecipe(
                GTUtility.copyAmount(1, aStack),
                GTModHandler.RecipeBits.BITS_STD,
                new Object[] { "Xx", 'X', OrePrefixes.foil.get(aMaterial) });
        }
    }
}
