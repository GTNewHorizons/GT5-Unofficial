package com.github.technus.tectech.thing.metaTileEntity.multi.em_machine;

import static com.github.technus.tectech.util.CommonValues.V;

import com.github.technus.tectech.mechanics.elementalMatter.core.maps.EMInstanceStackMap;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.MultiblockControl;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.Parameters;

/**
 * Created by danie_000 on 24.12.2017.
 */
public class Behaviour_Recycler implements GT_MetaTileEntity_EM_machine.IBehaviour {
    private final int tier;
    private final double coeff;

    public Behaviour_Recycler(int tier) {
        this.tier = tier;
        coeff = (1D / Math.pow(2D, tier - 4));
    }

    @Override
    public void parametersInstantiation(GT_MetaTileEntity_EM_machine te, Parameters parameters) {}

    @Override
    public boolean checkParametersInAndSetStatuses(GT_MetaTileEntity_EM_machine te, Parameters parameters) {
        return true;
    }

    @Override
    public MultiblockControl<EMInstanceStackMap[]> process(
            EMInstanceStackMap[] inputs, GT_MetaTileEntity_EM_machine te, Parameters parameters) {
        double mass = 0;
        for (EMInstanceStackMap input : inputs) {
            if (input != null) {
                mass += input.getMass();
            }
        }
        return new MultiblockControl<>(null, (int) V[tier], 4, 0, 10000, 20, 0, mass * coeff);
    }
}
