package com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive;

import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMGaugeBosonDefinition.deadEnd;
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
import static net.minecraft.util.StatCollector.translateToLocal;

import com.github.technus.tectech.mechanics.elementalMatter.core.decay.EMDecay;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.registry.EMDefinitionsRegistry;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.registry.EMType;

/**
 * Created by danie_000 on 22.10.2016.
 */
public class EMQuarkDefinition extends EMFermionDefinition {

    public static final EMQuarkDefinition quark_u = new EMQuarkDefinition(
            "tt.keyword.QuarkUp",
            "u",
            1,
            2.3e6D,
            2,
            3,
            "u"), quark_c = new EMQuarkDefinition("tt.keyword.QuarkCharm", "c", 2, 1.29e9D, 2, 9, "c"),
            quark_t = new EMQuarkDefinition("tt.keyword.QuarkTop", "t", 3, 172.44e9D, 2, 13, "t"),
            quark_d = new EMQuarkDefinition("tt.keyword.QuarkDown", "d", 1, 4.8e6D, -1, 5, "d"),
            quark_s = new EMQuarkDefinition("tt.keyword.QuarkStrange", "s", 2, 95e6D, -1, 7, "s"),
            quark_b = new EMQuarkDefinition("tt.keyword.QuarkBottom", "b", 3, 4.65e9D, -1, 11, "b"),
            quark_u_ = new EMQuarkDefinition("tt.keyword.QuarkAntiUp", "~u", -1, 2.3e6D, -2, 4, "~u"),
            quark_c_ = new EMQuarkDefinition("tt.keyword.QuarkAntiCharm", "~c", -2, 1.29e9D, -2, 10, "~c"),
            quark_t_ = new EMQuarkDefinition("tt.keyword.QuarkAntiTop", "~t", -3, 172.44e9D, -2, 14, "~t"),
            quark_d_ = new EMQuarkDefinition("tt.keyword.QuarkAntiDown", "~d", -1, 4.8e6D, 1, 6, "~d"),
            quark_s_ = new EMQuarkDefinition("tt.keyword.QuarkAntiStrange", "~s", -2, 95e6D, 1, 8, "~s"),
            quark_b_ = new EMQuarkDefinition("tt.keyword.QuarkAntiBottom", "~b", -3, 4.65e9D, 1, 12, "~b");

    protected EMQuarkDefinition(String name, String symbol, int type, double mass, int charge, int ID, String bind) {
        super(name, symbol, type, mass, charge, 0, ID, bind);
    }

    public static void run(EMDefinitionsRegistry registry) {
        registry.registerDefinitionClass(new EMType(EMQuarkDefinition.class, "tt.keyword.Quark"));
        quark_u.init(
                registry,
                quark_u_,
                STABLE_RAW_LIFE_TIME,
                3,
                -1,
                new EMDecay(1.23201e-5D, quark_b /* ,lepton_t_,lepton_Vt */),
                new EMDecay(0.050778116D, quark_s /* ,lepton_m_,lepton_Vm */),
                new EMDecay(0.9D, quark_d, lepton_e_, lepton_Ve),
                deadEnd); // makes photons and don't care
        quark_c.init(
                registry,
                quark_c_,
                0.5e-13D,
                1,
                -1,
                new EMDecay(0.00169744D, quark_b /* ,lepton_t_,lepton_Vt */),
                new EMDecay(0.05071504D, quark_d, lepton_m_, lepton_Vm),
                new EMDecay(0.9D, quark_s, lepton_e_, lepton_Ve),
                deadEnd); // makes photons and don't care
        quark_t.init(
                registry,
                quark_t_,
                2.5e-26D,
                0,
                -1,
                new EMDecay(7.51689e-5D, quark_d, lepton_t_, lepton_Vt),
                new EMDecay(0.00163216D, quark_s, lepton_m_, lepton_Vm),
                new EMDecay(0.9D, quark_b, lepton_e_, lepton_Ve),
                deadEnd); // makes photons and don't care

        quark_d.init(
                registry,
                quark_d_,
                STABLE_RAW_LIFE_TIME,
                3,
                -1,
                new EMDecay(7.51689e-5D, quark_t /* ,lepton_t,lepton_Vt_ */),
                new EMDecay(0.05071504D, quark_c /* ,lepton_m,lepton_Vm_ */),
                new EMDecay(0.9D, quark_u, lepton_e, lepton_Ve_),
                deadEnd); // makes photons and don't care
        quark_s.init(
                registry,
                quark_s_,
                0.6e-9D,
                1,
                -1,
                new EMDecay(0.00163216D, quark_t /* ,lepton_t,lepton_Vt_ */),
                new EMDecay(0.050778116D, quark_u, lepton_m, lepton_Vm_),
                new EMDecay(0.9D, quark_c, lepton_e, lepton_Ve_),
                deadEnd); // makes photons and don't care
        quark_b.init(
                registry,
                quark_b_,
                0.7e-13D,
                0,
                -1,
                new EMDecay(1.23201e-5D, quark_u, lepton_t, lepton_Vt_),
                new EMDecay(0.00169744D, quark_c, lepton_m, lepton_Vm_),
                new EMDecay(0.9D, quark_t, lepton_e, lepton_Ve_),
                deadEnd); // makes photons and don't care

        quark_u_.init(
                registry,
                quark_u,
                STABLE_RAW_LIFE_TIME,
                3,
                -1,
                new EMDecay(1.23201e-5D, quark_b_ /* ,lepton_t,lepton_Vt_ */),
                new EMDecay(0.050778116D, quark_s_ /* ,lepton_m,lepton_Vm_ */),
                new EMDecay(0.9D, quark_d_, lepton_e, lepton_Ve_),
                deadEnd); // makes photons and don't care
        quark_c_.init(
                registry,
                quark_c,
                0.5e-13D,
                1,
                -1,
                new EMDecay(0.00169744F, quark_b_ /* ,lepton_t,lepton_Vt_ */),
                new EMDecay(0.05071504F, quark_d_, lepton_m, lepton_Vm_),
                new EMDecay(0.9F, quark_s_, lepton_e, lepton_Ve_),
                deadEnd); // makes photons and don't care
        quark_t_.init(
                registry,
                quark_t,
                2.5e-26F,
                0,
                -1,
                new EMDecay(7.51689e-5F, quark_d_, lepton_t, lepton_Vt_),
                new EMDecay(0.00163216F, quark_s_, lepton_m, lepton_Vm_),
                new EMDecay(0.9F, quark_b_, lepton_e, lepton_Ve_),
                deadEnd); // makes photons and don't care

        quark_d_.init(
                registry,
                quark_d,
                STABLE_RAW_LIFE_TIME,
                3,
                -1,
                new EMDecay(7.51689e-5F, quark_t_ /* ,lepton_t_,lepton_Vt */),
                new EMDecay(0.05071504F, quark_c_ /* ,lepton_m_,lepton_Vm */),
                new EMDecay(0.9F, quark_u_, lepton_e_, lepton_Ve),
                deadEnd); // makes photons and don't care
        quark_s_.init(
                registry,
                quark_s,
                0.6e-9F,
                1,
                -1,
                new EMDecay(0.00163216F, quark_t_ /* ,lepton_t_,lepton_Vt */),
                new EMDecay(0.050778116F, quark_u_, lepton_m_, lepton_Vm),
                new EMDecay(0.9F, quark_c_, lepton_e_, lepton_Ve),
                deadEnd); // makes photons and don't care
        quark_b_.init(
                registry,
                quark_b,
                0.7e-13F,
                0,
                -1,
                new EMDecay(1.23201e-5F, quark_u_, lepton_t_, lepton_Vt),
                new EMDecay(0.00169744F, quark_c_, lepton_m_, lepton_Vm),
                new EMDecay(0.9F, quark_t_, lepton_e_, lepton_Ve),
                deadEnd); // makes photons and don't care
    }

    @Override
    public String getLocalizedTypeName() {
        return translateToLocal("tt.keyword.Quark");
    }

    @Override
    public boolean isTimeSpanHalfLife() {
        return true;
    }
}
