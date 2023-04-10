package com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive;

import static com.github.technus.tectech.mechanics.elementalMatter.core.decay.EMDecay.NO_DECAY;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMLeptonDefinition.lepton_e;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMLeptonDefinition.lepton_e_;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMLeptonDefinition.lepton_m;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMLeptonDefinition.lepton_m_;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMLeptonDefinition.lepton_t;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMLeptonDefinition.lepton_t_;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMNeutrinoDefinition.lepton_Ve;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMNeutrinoDefinition.lepton_Ve_;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMNeutrinoDefinition.lepton_Vm;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMNeutrinoDefinition.lepton_Vm_;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMNeutrinoDefinition.lepton_Vt;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMNeutrinoDefinition.lepton_Vt_;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMQuarkDefinition.quark_b;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMQuarkDefinition.quark_b_;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMQuarkDefinition.quark_c;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMQuarkDefinition.quark_c_;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMQuarkDefinition.quark_d;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMQuarkDefinition.quark_d_;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMQuarkDefinition.quark_s;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMQuarkDefinition.quark_s_;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMQuarkDefinition.quark_u;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMQuarkDefinition.quark_u_;
import static net.minecraft.util.StatCollector.translateToLocal;

import com.github.technus.tectech.mechanics.elementalMatter.core.decay.EMDecay;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.registry.EMDefinitionsRegistry;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.registry.EMType;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.EMDefinitionStack;
import com.github.technus.tectech.util.TT_Utility;

/**
 * Created by danie_000 on 22.10.2016.
 */
public class EMGaugeBosonDefinition extends EMBosonDefinition {

    public static final EMGaugeBosonDefinition boson_g__ = new EMGaugeBosonDefinition(
            "tt.keyword.Gluon",
            "g",
            0,
            0,
            8,
            27,
            "g"), boson_Y__ = new EMGaugeBosonDefinition("tt.keyword.Photon", "\u03b3", 1e-18D, 0, -1, 28, "Y"),
            boson_Z = new EMGaugeBosonDefinition("tt.keyword.Weak0", "Z0", 91.1876e9, 0, -1, 29, "Z0"),
            boson_W_ = new EMGaugeBosonDefinition("tt.keyword.WeakPlus", "W+", 80.379e9, 3, -1, 30, "W+"),
            boson_W = new EMGaugeBosonDefinition("tt.keyword.WeakMinus", "W-", 80.379e9, -3, -1, 31, "W-");
    // deadEnd
    public static final EMDecay deadEnd = new EMDecay(boson_Y__, boson_Y__);
    public static final EMDefinitionStack boson_Y__1 = new EMDefinitionStack(boson_Y__, 1);
    public static final EMDecay deadEndHalf = new EMDecay(boson_Y__1);

    protected EMGaugeBosonDefinition(String name, String symbol, double mass, int charge, int color, int ID,
            String bind) {
        super(name, TT_Utility.toSuperscript(symbol), 0, mass, charge, color, ID, bind);
    }

    public static void run(EMDefinitionsRegistry registry) {
        registry.registerDefinitionClass(new EMType(EMGaugeBosonDefinition.class, "tt.keyword.GaugeBoson"));
        boson_g__.init(registry, boson_g__, 3e-50, 0, 0, deadEndHalf);
        boson_Y__.init(registry, boson_Y__, NO_DECAY_RAW_LIFE_TIME, -1, -1, NO_DECAY);
        boson_Z.init(
                registry,
                boson_Z,
                3e-25,
                11,
                11,
                new EMDecay(0.03363, lepton_e, lepton_e_),
                new EMDecay(0.03366, lepton_m, lepton_m_),
                new EMDecay(0.03367, lepton_t, lepton_t_),
                new EMDecay(0.068333, lepton_Ve, lepton_Ve_),
                new EMDecay(0.068333, lepton_Vm, lepton_Vm_),
                new EMDecay(0.068333, lepton_Vt, lepton_Vt_),
                new EMDecay(0.118, quark_u, quark_u_),
                new EMDecay(0.118, quark_c, quark_c_),
                new EMDecay(0.152, quark_d, quark_d_),
                new EMDecay(0.152, quark_s, quark_s_),
                new EMDecay(0.152, quark_b, quark_b_),
                deadEnd);
        boson_W.init(
                registry,
                boson_W_,
                3e-25,
                9,
                9,
                new EMDecay(0.108, lepton_e, lepton_Ve_),
                new EMDecay(0.108, lepton_m, lepton_Vm_),
                new EMDecay(0.108, lepton_t, lepton_Vt_),
                new EMDecay(0.112666, quark_u_, quark_d),
                new EMDecay(0.112666, quark_u_, quark_s),
                new EMDecay(0.112666, quark_u_, quark_b),
                new EMDecay(0.112666, quark_c_, quark_d),
                new EMDecay(0.112666, quark_c_, quark_s),
                new EMDecay(0.112666, quark_c_, quark_b),
                deadEnd);
        boson_W_.init(
                registry,
                boson_W,
                3e-25,
                9,
                9,
                new EMDecay(0.108, lepton_e_, lepton_Ve),
                new EMDecay(0.108, lepton_m_, lepton_Vm),
                new EMDecay(0.108, lepton_t_, lepton_Vt),
                new EMDecay(0.112666, quark_u, quark_d_),
                new EMDecay(0.112666, quark_u, quark_s_),
                new EMDecay(0.112666, quark_u, quark_b_),
                new EMDecay(0.112666, quark_c, quark_d_),
                new EMDecay(0.112666, quark_c, quark_s_),
                new EMDecay(0.112666, quark_c, quark_b_),
                deadEnd);
    }

    @Override
    public String getLocalizedTypeName() {
        return translateToLocal("tt.keyword.GaugeBoson");
    }

    @Override
    public boolean isTimeSpanHalfLife() {
        return false;
    }
}
