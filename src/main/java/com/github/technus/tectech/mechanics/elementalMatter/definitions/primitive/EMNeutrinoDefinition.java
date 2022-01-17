package com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive;

import com.github.technus.tectech.mechanics.elementalMatter.core.decay.EMDecay;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.EMDefinitionStack;
import com.github.technus.tectech.mechanics.elementalMatter.core.templates.EMPrimitive;

import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMBosonDefinition.*;

/**
 * Created by danie_000 on 22.10.2016.
 */
public final class EMNeutrinoDefinition extends EMPrimitive {
    public static final EMNeutrinoDefinition
            lepton_Ve = new EMNeutrinoDefinition("Electron neutrino", "\u03bd\u03b2", 1, 2e0D, 21),
            lepton_Vm = new EMNeutrinoDefinition("Muon neutrino", "\u03bd\u03bc", 2, 0.15e6D, 23),
            lepton_Vt = new EMNeutrinoDefinition("Tauon neutrino", "\u03bd\u03c4", 3, 15e6D, 25),
            lepton_Ve_ = new EMNeutrinoDefinition("Positron neutrino", "~\u03bd\u03b2", -1, 2e0D, 22),
            lepton_Vm_ = new EMNeutrinoDefinition("Antimuon neutrino", "~\u03bd\u03bc", -2, 0.15e6D, 24),
            lepton_Vt_ = new EMNeutrinoDefinition("Antitauon neutrino", "~\u03bd\u03c4", -3, 15e6D, 26);

    public static final EMDefinitionStack lepton_Ve1  = new EMDefinitionStack(lepton_Ve, 1);
    public static final EMDefinitionStack lepton_Ve2  = new EMDefinitionStack(lepton_Ve, 2);
    public static final EMDefinitionStack lepton_Ve_1 = new EMDefinitionStack(lepton_Ve_, 1);
    public static final EMDefinitionStack lepton_Ve_2 = new EMDefinitionStack(lepton_Ve_, 2);

    private EMNeutrinoDefinition(String name, String symbol, int type, double mass, int ID) {
        super(name, symbol, type, mass, 0, -1, ID);
    }

    public static void run() {
        lepton_Ve.init(lepton_Ve_, 1D, -1, -1,
                EMDecay.NO_PRODUCT);
        lepton_Vm.init(lepton_Vm_, 1D, 1, 0,
                new EMDecay(0.825D, nothing),
                deadEndHalf);
        lepton_Vt.init(lepton_Vt_, 1, 1, 0,
                new EMDecay(0.75F, nothing),
                deadEnd);

        lepton_Ve_.init(lepton_Ve, 1, -1, -1,
                EMDecay.NO_PRODUCT);
        lepton_Vm_.init(lepton_Vm, 1, 1, 0,
                new EMDecay(0.825F, nothing),
                deadEndHalf);
        lepton_Vt_.init(lepton_Vt, 1, 1, 0,
                new EMDecay(0.75F, nothing),
                deadEnd);
    }

    @Override
    public String getLocalizedName() {
        return "Lepton: " + getName();
    }

    @Override
    public boolean isTimeSpanHalfLife() {
        return true;
    }
}
