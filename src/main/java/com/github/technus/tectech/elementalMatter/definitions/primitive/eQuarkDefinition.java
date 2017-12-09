package com.github.technus.tectech.elementalMatter.definitions.primitive;

import com.github.technus.tectech.elementalMatter.core.cElementalDecay;
import com.github.technus.tectech.elementalMatter.core.templates.cElementalPrimitive;

/**
 * Created by danie_000 on 22.10.2016.
 */
public final class eQuarkDefinition extends cElementalPrimitive {
    public static final eQuarkDefinition
            quark_u = new eQuarkDefinition("Up", "u", 1, 2.3e6F, 2, 0, 3),
            quark_c = new eQuarkDefinition("Charm", "c", 2, 1.29e9F, 2, 0, 9),
            quark_t = new eQuarkDefinition("Top", "t", 3, 172.44e9F, 2, 0, 13),
            quark_d = new eQuarkDefinition("Down", "d", 1, 4.8e6F, -1, 0, 5),
            quark_s = new eQuarkDefinition("Strange", "s", 2, 95e6F, -1, 0, 7),
            quark_b = new eQuarkDefinition("Bottom", "b", 3, 4.65e9F, -1, 0, 11),
            quark_u_ = new eQuarkDefinition("AntiUp", "~u", -1, 2.3e6F, -2, 0, 4),
            quark_c_ = new eQuarkDefinition("AntiCharm", "~c", -2, 1.29e9F, -2, 0, 10),
            quark_t_ = new eQuarkDefinition("AntiTop", "~t", -3, 172.44e9F, -2, 0, 14),
            quark_d_ = new eQuarkDefinition("AntiDown", "~d", -1, 4.8e6F, 1, 0, 6),
            quark_s_ = new eQuarkDefinition("AntiStrange", "~s", -2, 95e6F, 1, 0, 8),
            quark_b_ = new eQuarkDefinition("AntiBottom", "~b", -3, 4.65e9F, 1, 0, 12);

    private eQuarkDefinition(String name, String symbol, int type, float mass, int charge, int color, int ID) {
        super(name, symbol, type, mass, charge, color, ID);
    }

    public static void run() {
        quark_u.init(quark_u_, 1e35F, 3, -1,
                new cElementalDecay(0.9F, quark_d, eLeptonDefinition.lepton_e_, eNeutrinoDefinition.lepton_Ve),
                new cElementalDecay(0.050778116F, quark_s/*,lepton_m_,lepton_Vm*/),
                new cElementalDecay(1.23201e-5F, quark_b/*,lepton_t_,lepton_Vt*/),
                eBosonDefinition.deadEnd);//makes photons and don't care
        quark_c.init(quark_c_, 0.5e-13F, 1, -1,
                new cElementalDecay(0.9F, quark_s, eLeptonDefinition.lepton_e_, eNeutrinoDefinition.lepton_Ve),
                new cElementalDecay(0.05071504F, quark_d, eLeptonDefinition.lepton_m_, eNeutrinoDefinition.lepton_Vm),
                new cElementalDecay(0.00169744F, quark_b/*,lepton_t_,lepton_Vt*/),
                eBosonDefinition.deadEnd);//makes photons and don't care
        quark_t.init(quark_t_, 2.5e-26F, 2, -1,
                new cElementalDecay(0.9F, quark_b, eLeptonDefinition.lepton_e_, eNeutrinoDefinition.lepton_Ve),
                new cElementalDecay(0.00163216F, quark_s, eLeptonDefinition.lepton_m_, eNeutrinoDefinition.lepton_Vm),
                new cElementalDecay(7.51689e-5F, quark_d, eLeptonDefinition.lepton_t_, eNeutrinoDefinition.lepton_Vt),
                eBosonDefinition.deadEnd);//makes photons and don't care

        quark_d.init(quark_d_, 1e35F, 3, -1,
                new cElementalDecay(0.9F, quark_u, eLeptonDefinition.lepton_e, eNeutrinoDefinition.lepton_Ve_),
                new cElementalDecay(0.05071504F, quark_c/*,lepton_m,lepton_Vm_*/),
                new cElementalDecay(7.51689e-5F, quark_t/*,lepton_t,lepton_Vt_*/),
                eBosonDefinition.deadEnd);//makes photons and don't care
        quark_s.init(quark_s_, 0.6e-9F, 1, -1,
                new cElementalDecay(0.9F, quark_c, eLeptonDefinition.lepton_e, eNeutrinoDefinition.lepton_Ve_),
                new cElementalDecay(0.050778116F, quark_u, eLeptonDefinition.lepton_m, eNeutrinoDefinition.lepton_Vm_),
                new cElementalDecay(0.00163216F, quark_t/*,lepton_t,lepton_Vt_*/),
                eBosonDefinition.deadEnd);//makes photons and don't care
        quark_b.init(quark_b_, 0.7e-13F, 2, -1,
                new cElementalDecay(0.9F, quark_t, eLeptonDefinition.lepton_e, eNeutrinoDefinition.lepton_Ve_),
                new cElementalDecay(0.00169744F, quark_c, eLeptonDefinition.lepton_m, eNeutrinoDefinition.lepton_Vm_),
                new cElementalDecay(1.23201e-5F, quark_u, eLeptonDefinition.lepton_t, eNeutrinoDefinition.lepton_Vt_),
                eBosonDefinition.deadEnd);//makes photons and don't care

        quark_u_.init(quark_u, 88, 3, -1,
                new cElementalDecay(0.9F, quark_d_, eLeptonDefinition.lepton_e, eNeutrinoDefinition.lepton_Ve_),
                new cElementalDecay(0.050778116F, quark_s_/*,lepton_m,lepton_Vm_*/),
                new cElementalDecay(1.23201e-5F, quark_b_/*,lepton_t,lepton_Vt_*/),
                eBosonDefinition.deadEnd);//makes photons and don't care
        quark_c_.init(quark_c, 0.5e-13F, 1, -1,
                new cElementalDecay(0.9F, quark_s_, eLeptonDefinition.lepton_e, eNeutrinoDefinition.lepton_Ve_),
                new cElementalDecay(0.05071504F, quark_d_, eLeptonDefinition.lepton_m, eNeutrinoDefinition.lepton_Vm_),
                new cElementalDecay(0.00169744F, quark_b_/*,lepton_t,lepton_Vt_*/),
                eBosonDefinition.deadEnd);//makes photons and don't care
        quark_t_.init(quark_t, 2.5e-26F, 2, -1,
                new cElementalDecay(0.9F, quark_b_, eLeptonDefinition.lepton_e, eNeutrinoDefinition.lepton_Ve_),
                new cElementalDecay(0.00163216F, quark_s_, eLeptonDefinition.lepton_m, eNeutrinoDefinition.lepton_Vm_),
                new cElementalDecay(7.51689e-5F, quark_d_, eLeptonDefinition.lepton_t, eNeutrinoDefinition.lepton_Vt_),
                eBosonDefinition.deadEnd);//makes photons and don't care

        quark_d_.init(quark_d, 44F, 3, -1,
                new cElementalDecay(0.9F, quark_u_, eLeptonDefinition.lepton_e_, eNeutrinoDefinition.lepton_Ve),
                new cElementalDecay(0.05071504F, quark_c_/*,lepton_m_,lepton_Vm*/),
                new cElementalDecay(7.51689e-5F, quark_t_/*,lepton_t_,lepton_Vt*/),
                eBosonDefinition.deadEnd);//makes photons and don't care
        quark_s_.init(quark_s, 0.6e-9F, 1, -1,
                new cElementalDecay(0.9F, quark_c_, eLeptonDefinition.lepton_e_, eNeutrinoDefinition.lepton_Ve),
                new cElementalDecay(0.050778116F, quark_u_, eLeptonDefinition.lepton_m_, eNeutrinoDefinition.lepton_Vm),
                new cElementalDecay(0.00163216F, quark_t_/*,lepton_t_,lepton_Vt*/),
                eBosonDefinition.deadEnd);//makes photons and don't care
        quark_b_.init(quark_b, 0.7e-13F, 2, -1,
                new cElementalDecay(0.9F, quark_t_, eLeptonDefinition.lepton_e_, eNeutrinoDefinition.lepton_Ve),
                new cElementalDecay(0.00169744F, quark_c_, eLeptonDefinition.lepton_m_, eNeutrinoDefinition.lepton_Vm),
                new cElementalDecay(1.23201e-5F, quark_u_, eLeptonDefinition.lepton_t_, eNeutrinoDefinition.lepton_Vt),
                eBosonDefinition.deadEnd);//makes photons and don't care
    }

    @Override
    public String getName() {
        return "Quark: " + name;
    }

    @Override
    public boolean isTimeSpanHalfLife() {
        return true;
    }
}
