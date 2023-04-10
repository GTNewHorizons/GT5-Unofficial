package gregtech.loaders.oreprocessing;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_Utility;

public class ProcessingWax implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingWax() {
        OrePrefixes.wax.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        if (aOreDictName.equals("waxMagical")) GT_Values.RA.addFuel(GT_Utility.copyAmount(1L, aStack), null, 6, 5);
    }
}
