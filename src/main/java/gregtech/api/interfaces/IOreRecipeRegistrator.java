package gregtech.api.interfaces;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.material.MU;

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

    /// Bridges the MaterialLib [Material]-typed ore-processing pipeline to the [Materials]-typed registrator,
    /// resolving `material`'s legacy counterpart through [MU#materialOf]. A material with no legacy counterpart
    /// carries no ore-processing recipes through this entry, so it is a no-op -- mirrors
    /// [#registerOre(OrePrefixes, IOreMaterial, String, String, ItemStack)]'s `instanceof Materials` gate.
    default void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        Materials legacyMaterial = MU.materialOf(material);
        if (legacyMaterial != null) registerOre(prefix, legacyMaterial, oreDictName, modName, stack);
    }
}
