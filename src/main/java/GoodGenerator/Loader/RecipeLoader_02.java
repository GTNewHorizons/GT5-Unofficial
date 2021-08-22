package GoodGenerator.Loader;

import GoodGenerator.util.ItemRefer;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;

public class RecipeLoader_02 {

    public static void RecipeLoad(){

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[]{
                        GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.StainlessSteel,1),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.BlueAlloy,1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorHV,32),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Beryllium,32),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Elite,1)
                },
                null,
                ItemRefer.Speeding_Pipe.get(1),
                300,
                1920
        );

    }
}
