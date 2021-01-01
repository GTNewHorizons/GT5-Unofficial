package com.github.bartimaeusnek.crossmod.tectech.tileentites.multi.GT_Replacement;

import com.github.bartimaeusnek.crossmod.tectech.helper.IHasCoils;
import gregtech.api.enums.HeatingCoilLevel;

public abstract class TT_Abstract_GT_Replacement_Coils extends TT_Abstract_GT_Replacement implements IHasCoils {
    protected TT_Abstract_GT_Replacement_Coils(int newId, String aName, String aNameRegional) {
        super(newId, aName, aNameRegional);
    }

    protected TT_Abstract_GT_Replacement_Coils(String aName) {
        super(aName);
    }
    protected HeatingCoilLevel heatingCoilLevel = HeatingCoilLevel.None;

    @Override
    public void setCoilHeat(HeatingCoilLevel coilMeta) {
        heatingCoilLevel = coilMeta;
    }

    @Override
    public HeatingCoilLevel getCoilHeat() {
        return heatingCoilLevel;
    }
}
