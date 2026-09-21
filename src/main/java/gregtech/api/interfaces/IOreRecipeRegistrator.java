package gregtech.api.interfaces;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.OrePrefixes;

public interface IOreRecipeRegistrator {

    /**
     * Contains a Code Fragment, used in the OrePrefix to register Recipes. Better than using a switch/case, like I did
     * before.
     *
     * @param prefix   always != null
     * @param material always != null, and can be == Materials.NULL if the Prefix is Self Referencing or not
     *                 Material based!
     * @param stack    always != null
     */
    void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName, ItemStack stack);
}
