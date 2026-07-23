package gregtech.loaders.oreprocessing;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.GTMaterialProperties;
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
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        if (!MU.hasFlag(material, GTMaterialFlag.NO_SMASHING)) {
            GTRecipeRegistrator.registerWiremillRecipes(material, 100, 4);
        }
        if (!Boolean.FALSE.equals(material.getProperty(GTMaterialProperties.UNIFIABLE))
            && !MU.hasFlag(material, GTMaterialFlag.NO_WORKING)) {
            GTModHandler.addCraftingRecipe(
                GTUtility.copyAmount(1, stack),
                GTModHandler.RecipeBits.BITS_STD,
                new Object[] { "Xx", 'X', MU.craftIngredient(OrePrefixes.foil, material) });
        }
    }
}
