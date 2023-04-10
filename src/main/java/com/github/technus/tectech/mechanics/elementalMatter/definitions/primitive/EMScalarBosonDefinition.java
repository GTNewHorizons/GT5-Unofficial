package com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive;

import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMGaugeBosonDefinition.boson_W;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMGaugeBosonDefinition.boson_W_;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMGaugeBosonDefinition.boson_Y__;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMGaugeBosonDefinition.boson_Z;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMGaugeBosonDefinition.boson_g__;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMGaugeBosonDefinition.deadEnd;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMLeptonDefinition.lepton_m;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMLeptonDefinition.lepton_m_;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMLeptonDefinition.lepton_t;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMLeptonDefinition.lepton_t_;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMQuarkDefinition.quark_b;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMQuarkDefinition.quark_b_;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMQuarkDefinition.quark_c;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMQuarkDefinition.quark_c_;
import static net.minecraft.util.StatCollector.translateToLocal;

import com.github.technus.tectech.mechanics.elementalMatter.core.decay.EMDecay;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.registry.EMDefinitionsRegistry;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.registry.EMType;
import com.github.technus.tectech.util.TT_Utility;

/**
 * Created by danie_000 on 22.10.2016.
 */
public class EMScalarBosonDefinition extends EMBosonDefinition {

    public static final EMScalarBosonDefinition boson_H__ = new EMScalarBosonDefinition(
            "tt.keyword.Higgs",
            "H0",
            125.09e9D,
            -2,
            32,
            "H0");

    private EMScalarBosonDefinition(String name, String symbol, double mass, int color, int ID, String bind) {
        super(name, TT_Utility.toSuperscript(symbol), 0, mass, 0, color, ID, bind);
    }

    public static void run(EMDefinitionsRegistry registry) {
        registry.registerDefinitionClass(new EMType(EMScalarBosonDefinition.class, "tt.keyword.ScalarBoson"));
        boson_H__.init(
                registry,
                boson_H__,
                1.56e-22D,
                8,
                8,
                new EMDecay(0.0002171, lepton_m, lepton_m_),
                new EMDecay(0.001541, boson_Z, boson_Y__),
                new EMDecay(0.02641, boson_Z, boson_Z),
                new EMDecay(0.02884, quark_c, quark_c_),
                new EMDecay(0.06256, lepton_t, lepton_t_),
                new EMDecay(0.0818, boson_g__, boson_g__),
                new EMDecay(0.2152, boson_W_, boson_W),
                new EMDecay(0.5809, quark_b, quark_b_),
                deadEnd);
    }

    @Override
    public String getLocalizedTypeName() {
        return translateToLocal("tt.keyword.ScalarBoson");
    }

    @Override
    public boolean isTimeSpanHalfLife() {
        return true;
    }
}
