package com.github.technus.tectech.thing.metaTileEntity.multi.em_machine;

import com.github.technus.tectech.mechanics.elementalMatter.core.cElementalInstanceStackMap;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.MultiblockControl;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.Parameters;

/**
 * Created by danie_000 on 24.12.2017.
 */
public class Behaviour_Recycler implements GT_MetaTileEntity_EM_machine.Behaviour {
    final int tier;
    public Behaviour_Recycler(int tier){
        this.tier=tier;
    }

    @Override
    public void parametersInstantiation(GT_MetaTileEntity_EM_machine te, Parameters parameters) {

    }

    @Override
    public boolean checkParametersInAndSetStatuses(GT_MetaTileEntity_EM_machine te, Parameters parameters) {
        return false;
    }

    @Override
    public MultiblockControl<cElementalInstanceStackMap[]> process(cElementalInstanceStackMap[] inputs, GT_MetaTileEntity_EM_machine te, Parameters parameters) {
        return null;
    }
}
