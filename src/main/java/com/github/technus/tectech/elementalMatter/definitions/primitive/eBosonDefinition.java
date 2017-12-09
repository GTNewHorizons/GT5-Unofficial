package com.github.technus.tectech.elementalMatter.definitions.primitive;

import com.github.technus.tectech.elementalMatter.core.cElementalDecay;
import com.github.technus.tectech.elementalMatter.core.containers.cElementalDefinitionStack;
import com.github.technus.tectech.elementalMatter.core.templates.cElementalPrimitive;

/**
 * Created by danie_000 on 22.10.2016.
 */
public final class eBosonDefinition extends cElementalPrimitive {
    public static final eBosonDefinition
            boson_Y__ = new eBosonDefinition("Photon", "\u03b3", 0, 1e-18F, 0, -1, 27),
            boson_H__ = new eBosonDefinition("Higgs", "\u0397", 0, 126.09e9F, 0, -2, 28);
    //deadEnd
    public static final cElementalDecay deadEnd = new cElementalDecay(boson_Y__, boson_Y__);
    public static final cElementalDecay deadEndHalf = new cElementalDecay(boson_Y__);

    private eBosonDefinition(String name, String symbol, int type, float mass, int charge, int color, int ID) {
        super(name, symbol, type, mass, charge, color, ID);
    }

    public static void run() {
        boson_Y__.init(null, -1F, 0, 0,
                deadEndHalf);
        boson_H__.init(null, 1.56e-22F, 0, 0,
                new cElementalDecay(0.96F, new cElementalDefinitionStack(boson_Y__, 4)),
                new cElementalDecay(0.02F, eLeptonDefinition.lepton_t, eLeptonDefinition.lepton_t_),
                new cElementalDecay(0.01F, eQuarkDefinition.quark_b, eQuarkDefinition.quark_b_),
                deadEnd);
    }

    @Override
    public String getName() {
        return "Boson: " + name;
    }

    @Override
    public boolean isTimeSpanHalfLife() {
        return false;
    }
}
