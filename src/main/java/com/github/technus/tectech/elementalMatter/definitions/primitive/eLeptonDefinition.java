package com.github.technus.tectech.elementalMatter.definitions.primitive;

import com.github.technus.tectech.elementalMatter.core.cElementalDecay;
import com.github.technus.tectech.elementalMatter.core.containers.cElementalDefinitionStack;
import com.github.technus.tectech.elementalMatter.core.templates.cElementalPrimitive;

/**
 * Created by danie_000 on 22.10.2016.
 */
public final class eLeptonDefinition extends cElementalPrimitive {
    public static final eLeptonDefinition
            lepton_e = new eLeptonDefinition("Electron", "\u03b2-", 1, 0.511e6F, -3, -1, 15),
            lepton_m = new eLeptonDefinition("Muon", "\u03bc-", 2, 105.658e6F, -3, -1, 17),
            lepton_t = new eLeptonDefinition("Tauon", "\u03c4-", 3, 1776.83e6F, -3, -1, 19),
            lepton_e_ = new eLeptonDefinition("Positron", "\u03b2+", -1, 0.511e6F, 3, -1, 16),
            lepton_m_ = new eLeptonDefinition("Antimuon", "\u03bc+", -2, 105.658e6F, 3, -1, 18),
            lepton_t_ = new eLeptonDefinition("Antitauon", "\u03c4+", -3, 1776.83e6F, 3, -1, 20);

    public static final cElementalDefinitionStack lepton_e1 = new cElementalDefinitionStack(lepton_e, 1);
    public static final cElementalDefinitionStack lepton_e2 = new cElementalDefinitionStack(lepton_e, 2);
    public static final cElementalDefinitionStack lepton_e_1 = new cElementalDefinitionStack(lepton_e_, 1);
    public static final cElementalDefinitionStack lepton_e_2 = new cElementalDefinitionStack(lepton_e_, 2);

    private eLeptonDefinition(String name, String symbol, int type, float mass, int charge, int color, int ID) {
        super(name, symbol, type, mass, charge, color, ID);
        //this.itemThing=null;
        //this.fluidThing=null;
    }

    public static void run() {
        lepton_e.init(lepton_e_, STABLE_RAW_LIFE_TIME, 0, 1,
                eBosonDefinition.deadEnd,//makes photons and don't care
                new cElementalDecay(lepton_e, eBosonDefinition.boson_Y__));
        lepton_m.init(lepton_m_, 2.197019e-6F, 0, 1,
                new cElementalDecay(0.9F, lepton_e, eNeutrinoDefinition.lepton_Ve_, eNeutrinoDefinition.lepton_Vm),
                eBosonDefinition.deadEnd);//makes photons and don't care
        lepton_t.init(lepton_t_, 2.906e-13F, 1, 3,
                new cElementalDecay(0.8F, lepton_m, eNeutrinoDefinition.lepton_Vm_, eNeutrinoDefinition.lepton_Vt, eBosonDefinition.boson_Y__),
                new cElementalDecay(0.1F, lepton_e, eNeutrinoDefinition.lepton_Ve_, eNeutrinoDefinition.lepton_Vm),
                new cElementalDecay(0.05F, lepton_m, eNeutrinoDefinition.lepton_Vm_, eNeutrinoDefinition.lepton_Vt, eBosonDefinition.boson_H__),
                eBosonDefinition.deadEnd);//makes photons and don't care

        lepton_e_.init(lepton_e, STABLE_RAW_LIFE_TIME, 0, 1,
                eBosonDefinition.deadEnd,//makes photons and don't care
                new cElementalDecay(lepton_e_, eBosonDefinition.boson_Y__));
        lepton_m_.init(lepton_m, 2.197019e-6F, 0, 1,
                new cElementalDecay(0.9F, lepton_e_, eNeutrinoDefinition.lepton_Ve, eNeutrinoDefinition.lepton_Vm_),
                eBosonDefinition.deadEnd);//makes photons and don't care
        lepton_t_.init(lepton_t, 2.906e-13F, 1, 3,
                new cElementalDecay(0.8F, lepton_m_, eNeutrinoDefinition.lepton_Vm, eNeutrinoDefinition.lepton_Vt_, eBosonDefinition.boson_Y__),
                new cElementalDecay(0.1F, lepton_e_, eNeutrinoDefinition.lepton_Ve, eNeutrinoDefinition.lepton_Vm_),
                new cElementalDecay(0.05F, lepton_m_, eNeutrinoDefinition.lepton_Vm, eNeutrinoDefinition.lepton_Vt_, eBosonDefinition.boson_H__),
                eBosonDefinition.deadEnd);//makes photons and don't care
    }

    @Override
    public String getName() {
        return "Lepton: " + name;
    }

    @Override
    public boolean isTimeSpanHalfLife() {
        return true;
    }
}
