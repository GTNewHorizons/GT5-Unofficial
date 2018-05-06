package com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive;

import com.github.technus.tectech.mechanics.elementalMatter.core.cElementalDecay;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.cElementalDefinitionStack;
import com.github.technus.tectech.mechanics.elementalMatter.core.templates.cElementalPrimitive;

/**
 * Created by danie_000 on 22.10.2016.
 */
public final class eNeutrinoDefinition extends cElementalPrimitive {
    public static final eNeutrinoDefinition
            lepton_Ve = new eNeutrinoDefinition("Electron neutrino", "\u03bd\u03b2", 1, 2e0F, 0, -1, 21),
            lepton_Vm = new eNeutrinoDefinition("Muon neutrino", "\u03bd\u03bc", 2, 0.15e6F, 0, -1, 23),
            lepton_Vt = new eNeutrinoDefinition("Tauon neutrino", "\u03bd\u03c4", 3, 15e6F, 0, -1, 25),
            lepton_Ve_ = new eNeutrinoDefinition("Positron neutrino", "~\u03bd\u03b2", -1, 2e0F, 0, -1, 22),
            lepton_Vm_ = new eNeutrinoDefinition("Antimuon neutrino", "~\u03bd\u03bc", -2, 0.15e6F, 0, -1, 24),
            lepton_Vt_ = new eNeutrinoDefinition("Antitauon neutrino", "~\u03bd\u03c4", -3, 15e6F, 0, -1, 26);

    public static final cElementalDefinitionStack lepton_Ve1 = new cElementalDefinitionStack(lepton_Ve, 1);
    public static final cElementalDefinitionStack lepton_Ve2 = new cElementalDefinitionStack(lepton_Ve, 2);
    public static final cElementalDefinitionStack lepton_Ve_1 = new cElementalDefinitionStack(lepton_Ve_, 1);
    public static final cElementalDefinitionStack lepton_Ve_2 = new cElementalDefinitionStack(lepton_Ve_, 2);

    private eNeutrinoDefinition(String name, String symbol, int type, float mass, int charge, int color, int ID) {
        super(name, symbol, type, mass, charge, color, ID);
    }

    public static void run() {
        lepton_Ve.init(lepton_Ve_, 1F, -1, -1,
                cElementalDecay.noProduct);
        lepton_Vm.init(lepton_Vm_, 1F, 1, 0,
                new cElementalDecay(0.825F, nothing),
                eBosonDefinition.deadEndHalf);
        lepton_Vt.init(lepton_Vt_, 1F, 1, 0,
                new cElementalDecay(0.75F, nothing),
                eBosonDefinition.deadEnd);

        lepton_Ve_.init(lepton_Ve, 1F, -1, -1,
                cElementalDecay.noProduct);
        lepton_Vm_.init(lepton_Vm, 1F, 1, 0,
                new cElementalDecay(0.825F, nothing),
                eBosonDefinition.deadEndHalf);
        lepton_Vt_.init(lepton_Vt, 1F, 1, 0,
                new cElementalDecay(0.75F, nothing),
                eBosonDefinition.deadEnd);
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
