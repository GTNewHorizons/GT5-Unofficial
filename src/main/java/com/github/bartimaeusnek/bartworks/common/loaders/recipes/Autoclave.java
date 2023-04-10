package com.github.bartimaeusnek.bartworks.common.loaders.recipes;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.github.bartimaeusnek.bartworks.common.loaders.BioItemList;
import com.github.bartimaeusnek.bartworks.util.BW_Util;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;

public class Autoclave implements Runnable {

    @Override
    public void run() {
        Materials[] sterilizers = { Materials.Ammonia, Materials.Chlorine, Materials.Ethanol, Materials.Methanol };
        for (Materials used : sterilizers) {
            GT_Values.RA.addAutoclaveRecipe(
                    ItemList.Circuit_Parts_PetriDish.get(1L),
                    used.getGas(10L) != null ? used.getGas(8L) : used.getFluid(16L),
                    BioItemList.getPetriDish(null),
                    10000,
                    100,
                    BW_Util.getMachineVoltageFromTier(1));

            GT_Values.RA.addAutoclaveRecipe(
                    new ItemStack(Items.glass_bottle),
                    used.getGas(10L) != null ? used.getGas(8L) : used.getFluid(16L),
                    BioItemList.getDNASampleFlask(null),
                    10000,
                    100,
                    BW_Util.getMachineVoltageFromTier(1));
        }
    }
}
