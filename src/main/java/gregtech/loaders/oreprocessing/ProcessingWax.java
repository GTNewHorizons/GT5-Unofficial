package gregtech.loaders.oreprocessing;

import static gregtech.api.util.GTRecipeConstants.FUEL_TYPE;
import static gregtech.api.util.GTRecipeConstants.FUEL_VALUE;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTUtility;

public class ProcessingWax implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingWax() {
        OrePrefixes.wax.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        if (aOreDictName.equals("waxMagical")) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(1, aStack))
                .metadata(FUEL_VALUE, 6)
                .metadata(FUEL_TYPE, 5)
                .addTo(GTRecipeConstants.Fuel);
        }
    }
}
