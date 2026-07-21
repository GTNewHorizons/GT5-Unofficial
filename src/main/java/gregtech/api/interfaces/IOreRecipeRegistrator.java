package gregtech.api.interfaces;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;

public interface IOreRecipeRegistrator {

    /**
     * Contains a Code Fragment, used in the OrePrefix to register Recipes. Better than using a switch/case, like I did
     * before.
     *
     * @param prefix   always != null
     * @param material always != null, and can be == _NULL if the Prefix is Self Referencing or not Material based!
     * @param stack    always != null
     */
    void registerOre(OrePrefixes prefix, Materials material, String oreDictName, String modName, ItemStack stack);

    /// Bridges the [IOreMaterial]-typed ore-processing pipeline to the [Materials]-typed registrator. A material
    /// that is not a legacy [Materials] carries no ore-processing recipes, so it is a no-op.
    default void registerOre(OrePrefixes prefix, IOreMaterial material, String oreDictName, String modName,
        ItemStack stack) {
        if (material instanceof Materials legacyMaterial)
            registerOre(prefix, legacyMaterial, oreDictName, modName, stack);
    }
}
