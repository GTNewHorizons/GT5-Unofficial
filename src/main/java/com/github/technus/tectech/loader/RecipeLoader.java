package com.github.technus.tectech.loader;

import com.github.technus.tectech.compatibility.dreamcraft.DreamCraftRecipeLoader;
import com.github.technus.tectech.elementalMatter.definitions.complex.atom.dAtomDefinition;
import com.github.technus.tectech.elementalMatter.definitions.complex.hadron.dHadronDefinition;
import com.github.technus.tectech.thing.CustomItemList;
import com.github.technus.tectech.thing.casing.TT_Container_Casings;
import com.github.technus.tectech.thing.item.ConstructableTriggerItem;
import com.github.technus.tectech.thing.item.EuMeterGT;
import cpw.mods.fml.common.Loader;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;

import static gregtech.api.enums.GT_Values.RA;

/**
 * Created by danie_000 on 16.11.2016.
 */
public class RecipeLoader implements Runnable {
    public static Materials getOrDefault(String name,Materials def){
        Materials mat=Materials.get(name);
        return mat == Materials._NULL || mat == null ? def : mat;
    }

    @Override
    public void run() {
        dAtomDefinition.setTransformation();
        dHadronDefinition.setTransformations();

        // ===================================================================================================
        // Recipes init - common goes here rest goes into methods below
        // ===================================================================================================

        for(int i=0;i<=15;i++) {
            RA.addAssemblerRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(i), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Cobalt, 1)}, Materials.Aluminium.getMolten(864), new ItemStack(TT_Container_Casings.sHintCasingsTT, 1, i), 32, 120);
        }

        //BLUEprint
        GT_ModHandler.addShapelessCraftingRecipe(new ItemStack(ConstructableTriggerItem.INSTANCE, 1),
                GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                new Object[]{Dyes.dyeBlue, OrePrefixes.plate.get(Materials.Paper), Dyes.dyeBlue, Dyes.dyeWhite});

        //GT EU reader
        GT_ModHandler.addCraftingRecipe(new ItemStack(EuMeterGT.INSTANCE,1),
                GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                new Object[]{"PGW", "SCW", "BRN",
                        'P', OrePrefixes.plateDouble.get(Materials.Steel),
                        'G', OrePrefixes.plate.get(Materials.Glass),
                        'W', OrePrefixes.cableGt01.get(Materials.Copper),
                        'S', OrePrefixes.stick.get(Materials.Brass),
                        'C', ItemList.Casing_Coil_Cupronickel.get(1),
                        'B', Dyes.dyeBlue,
                        'R', Dyes.dyeRed,
                        'N', Dyes.dyeBlack,});

        //Owner detector
        GT_ModHandler.addCraftingRecipe(CustomItemList.Machine_OwnerDetector.get(1),
                GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                new Object[]{"PPP","GEG","PPP",
                        'P', OrePrefixes.plate.get(Materials.IronMagnetic),
                        'G', OrePrefixes.plate.get(Materials.Glass),
                        'E', OrePrefixes.gem.get(Materials.EnderPearl)});

        //Data reader
        GT_ModHandler.addCraftingRecipe(CustomItemList.Machine_DataReader.get(1),
                GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                new Object[]{"BdB","GES","PwP",
                        'B', OrePrefixes.screw.get(Materials.Iridium),
                        'P', OrePrefixes.plate.get(Materials.Iridium),
                        'G', ItemList.Cover_Screen,
                        'S', ItemList.Sensor_IV,
                        'E', ItemList.Hull_IV});

        if (Loader.isModLoaded("dreamcraft")) {
            new DreamCraftRecipeLoader().run();//init recipes for GTNH version
        } else {
            new BloodyRecipeLoader().run();//init recipes for NON-GTNH version
        }
    }
}
