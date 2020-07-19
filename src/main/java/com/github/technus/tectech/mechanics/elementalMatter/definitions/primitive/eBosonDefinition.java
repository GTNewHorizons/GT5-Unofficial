package com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive;

import com.github.technus.tectech.mechanics.elementalMatter.core.cElementalDecay;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.cElementalDefinitionStack;
import com.github.technus.tectech.mechanics.elementalMatter.core.templates.cElementalPrimitive;

import static com.github.technus.tectech.mechanics.elementalMatter.core.cElementalDecay.*;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.eLeptonDefinition.*;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.eQuarkDefinition.*;

/**
 * Created by danie_000 on 22.10.2016.
 */
public final class eBosonDefinition extends cElementalPrimitive {
    public static final eBosonDefinition
            boson_Y__ = new eBosonDefinition("Photon", "\u03b3", 1e-18D, -1, 27),
            boson_H__ = new eBosonDefinition("Higgs", "\u0397", 126.09e9D, -2, 28);
    //deadEnd
    public static final cElementalDecay deadEnd = new cElementalDecay(boson_Y__, boson_Y__);
    public static final cElementalDecay deadEndHalf = new cElementalDecay(boson_Y__);
    public static final cElementalDefinitionStack boson_Y__1=new cElementalDefinitionStack(boson_Y__,1);

    private eBosonDefinition(String name, String symbol, double mass, int color, int ID) {
        super(name, symbol, 0, mass, 0, color, ID);
    }

    public static void run() {
        boson_Y__.init(null, NO_DECAY_RAW_LIFE_TIME, -1, -1, noDecay);
        boson_H__.init(null, 1.56e-22D, 2, 2,
                new cElementalDecay(0.01D, quark_b, quark_b_),
                new cElementalDecay(0.02D, lepton_t, lepton_t_),
                new cElementalDecay(0.96D, new cElementalDefinitionStack(boson_Y__, 4)),
                deadEnd);
    }

    @Override
    public String getName() {
        return "Boson: " + name;
    }

    @Override
    public boolean isTimeSpanHalfLife() {
        return this==boson_H__;
    }
}
