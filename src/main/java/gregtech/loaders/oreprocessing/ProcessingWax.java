package gregtech.loaders.oreprocessing;

import static gregtech.api.util.GT_RecipeConstants.FUEL_TYPE;
import static gregtech.api.util.GT_RecipeConstants.FUEL_VALUE;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_RecipeConstants;
import gregtech.api.util.GT_Utility;

public class ProcessingWax implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingWax() {
        OrePrefixes.wax.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        if (aOreDictName.equals("waxMagical")) {
            GT_Values.RA.stdBuilder()
                .itemInputs(GT_Utility.copyAmount(1, aStack))
                .metadata(FUEL_VALUE, 6)
                .metadata(FUEL_TYPE, 5)
                .addTo(GT_RecipeConstants.Fuel);
        }
    }
}
