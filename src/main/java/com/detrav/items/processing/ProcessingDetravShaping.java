package com.detrav.items.processing;

import com.detrav.enums.DetravItemList;
import com.detrav.enums.DetravSimpleItems;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;

/**
 * Created by wital_000 on 20.03.2016.
 */
public class ProcessingDetravShaping implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingDetravShaping() {
        OrePrefixes.ingot.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName, ItemStack aStack) {
        if ((GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L) != null) && (!aMaterial.contains(SubTag.NO_SMELTING))) {
            int tAmount = (int) (aPrefix.mMaterialAmount / 3628800L);
            if ((tAmount > 0) && (tAmount <= 64) && (aPrefix.mMaterialAmount % 3628800L == 0L)) {
                int tVoltageMultiplier = aMaterial.mBlastFurnaceTemp >= 2800 ? 64 : 16;

                if (aMaterial.contains(SubTag.NO_SMASHING)) {
                    tVoltageMultiplier /= 4;
                } else if (aPrefix.name().startsWith(OrePrefixes.dust.name())) {
                    return;
                }
                GT_Values.RA.addExtruderRecipe(GT_Utility.copyAmount(2L, new Object[]{aStack}), DetravItemList.Shape_Extruder_ProPick.get(0L, new Object[0]), GT_OreDictUnificator.get(DetravSimpleItems.toolHeadProPick.get(aMaterial), tAmount), (int) Math.max(aMaterial.getMass() * 2L * tAmount, tAmount), 8 * tVoltageMultiplier);
            }
        }
    }
}