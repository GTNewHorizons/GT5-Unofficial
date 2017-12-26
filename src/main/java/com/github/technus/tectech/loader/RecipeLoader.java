package com.github.technus.tectech.loader;

import com.github.technus.tectech.compatibility.dreamcraft.DreamCraftRecipeLoader;
import com.github.technus.tectech.elementalMatter.definitions.complex.atom.dAtomDefinition;
import com.github.technus.tectech.elementalMatter.definitions.complex.hadron.dHadronDefinition;
import com.github.technus.tectech.thing.casing.TT_Container_Casings;
import cpw.mods.fml.common.Loader;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;

import static gregtech.api.enums.GT_Values.RA;

/**
 * Created by danie_000 on 16.11.2016.
 */
public class RecipeLoader implements Runnable {
    @Override
    public void run() {
        dAtomDefinition.setTransformation();
        dHadronDefinition.setTransformations();

        // ===================================================================================================
        // Recipes init - common goes here rest goes into methods below
        // ===================================================================================================

        for(int i=0;i<=15;i++)
            RA.addAssemblerRecipe(new ItemStack[]{
                    GT_Utility.getIntegratedCircuit(i),
                    GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Cobalt,1)},
                    Materials.Aluminium.getMolten(864),
                    new ItemStack(TT_Container_Casings.sHintCasingsTT, 1,i),32,120);

        if (Loader.isModLoaded("dreamcraft")) new DreamCraftRecipeLoader().run();//init recipes for GTNH version
        else new BloodyRecipeLoader().run();//init recipes for NON-GTNH version


    }
}
