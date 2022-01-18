package com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive;

import com.github.technus.tectech.mechanics.elementalMatter.core.decay.EMDecay;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.EMDefinitionStack;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.EMPrimitiveTemplate;

import static com.github.technus.tectech.mechanics.elementalMatter.core.decay.EMDecay.*;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMLeptonDefinition.*;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMQuarkDefinition.*;
import static net.minecraft.util.StatCollector.translateToLocal;

/**
 * Created by danie_000 on 22.10.2016.
 */
public final class EMBosonDefinition extends EMPrimitiveTemplate {
    public static final EMBosonDefinition
            boson_Y__ = new EMBosonDefinition("Photon", "\u03b3", 1e-18D, -1, 27),
            boson_H__ = new EMBosonDefinition("Higgs", "\u0397", 126.09e9D, -2, 28);
    //deadEnd
    public static final EMDecay                   deadEnd     = new EMDecay(boson_Y__, boson_Y__);
    public static final EMDecay           deadEndHalf = new EMDecay(boson_Y__);
    public static final EMDefinitionStack boson_Y__1  =new EMDefinitionStack(boson_Y__,1);

    private EMBosonDefinition(String name, String symbol, double mass, int color, int ID) {
        super(name, symbol, 0, mass, 0, color, ID);
    }

    public static void run() {
        boson_Y__.init(null, NO_DECAY_RAW_LIFE_TIME, -1, -1, NO_DECAY);
        boson_H__.init(null, 1.56e-22D, 2, 2,
                new EMDecay(0.01D, quark_b, quark_b_),
                new EMDecay(0.02D, lepton_t, lepton_t_),
                new EMDecay(0.96D, new EMDefinitionStack(boson_Y__, 4)),
                deadEnd);
    }

    @Override
    public String getLocalizedName() {
        return translateToLocal("tt.keyword.Boson")+": " + getName();
    }

    @Override
    public boolean isTimeSpanHalfLife() {
        return this==boson_H__;
    }
}
