package com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive;

import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMGaugeBosonDefinition.boson_Y__;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMGaugeBosonDefinition.deadEnd;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMNeutrinoDefinition.lepton_Ve;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMNeutrinoDefinition.lepton_Ve_;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMNeutrinoDefinition.lepton_Vm;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMNeutrinoDefinition.lepton_Vm_;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMNeutrinoDefinition.lepton_Vt;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMNeutrinoDefinition.lepton_Vt_;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMScalarBosonDefinition.boson_H__;
import static net.minecraft.util.StatCollector.translateToLocal;

import com.github.technus.tectech.mechanics.elementalMatter.core.decay.EMDecay;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.registry.EMDefinitionsRegistry;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.registry.EMType;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.EMDefinitionStack;
import com.github.technus.tectech.util.TT_Utility;

/**
 * Created by danie_000 on 22.10.2016.
 */
public class EMLeptonDefinition extends EMFermionDefinition {

    public static final EMLeptonDefinition lepton_e = new EMLeptonDefinition(
            "tt.keyword.Electron",
            "\u03b2-",
            1,
            0.511e6D,
            -3,
            15,
            "e-"), lepton_m = new EMLeptonDefinition("tt.keyword.Muon", "\u03bc-", 2, 105.658e6D, -3, 17, "m-"),
            lepton_t = new EMLeptonDefinition("tt.keyword.Tauon", "\u03c4-", 3, 1776.83e6D, -3, 19, "t-"),
            lepton_e_ = new EMLeptonDefinition("tt.keyword.Positron", "\u03b2+", -1, 0.511e6D, 3, 16, "e+"),
            lepton_m_ = new EMLeptonDefinition("tt.keyword.Antimuon", "\u03bc+", -2, 105.658e6D, 3, 18, "m+"),
            lepton_t_ = new EMLeptonDefinition("tt.keyword.Antitauon", "\u03c4+", -3, 1776.83e6D, 3, 20, "t+");

    public static final EMDefinitionStack lepton_e1 = new EMDefinitionStack(lepton_e, 1);
    public static final EMDefinitionStack lepton_e2 = new EMDefinitionStack(lepton_e, 2);
    public static final EMDefinitionStack lepton_e_1 = new EMDefinitionStack(lepton_e_, 1);
    public static final EMDefinitionStack lepton_e_2 = new EMDefinitionStack(lepton_e_, 2);

    protected EMLeptonDefinition(String name, String symbol, int type, double mass, int charge, int ID, String bind) {
        super(name, TT_Utility.toSuperscript(symbol), type, mass, charge, -1, ID, bind);
        // this.itemThing=null;
        // this.fluidThing=null;
    }

    public static void run(EMDefinitionsRegistry registry) {
        registry.registerDefinitionClass(new EMType(EMLeptonDefinition.class, "tt.keyword.Lepton"));
        lepton_e.init(registry, lepton_e_, STABLE_RAW_LIFE_TIME, 0, 1, deadEnd, new EMDecay(lepton_e, boson_Y__));
        lepton_m.init(
                registry,
                lepton_m_,
                2.197019e-6D,
                0,
                1,
                new EMDecay(0.9D, lepton_e, lepton_Ve_, lepton_Vm),
                deadEnd); // makes photons and don't care
        lepton_t.init(
                registry,
                lepton_t_,
                2.903e-13D,
                1,
                3,
                new EMDecay(0.05F, lepton_m, lepton_Vm_, lepton_Vt, boson_H__),
                new EMDecay(0.1D, lepton_e, lepton_Ve_, lepton_Vm),
                new EMDecay(0.8D, lepton_m, lepton_Vm_, lepton_Vt, boson_Y__),
                deadEnd); // makes photons and don't care

        lepton_e_.init(registry, lepton_e, STABLE_RAW_LIFE_TIME, 0, 1, deadEnd, new EMDecay(lepton_e, boson_Y__));
        lepton_m_.init(
                registry,
                lepton_m,
                2.197019e-6F,
                0,
                1,
                new EMDecay(0.9F, lepton_e_, lepton_Ve, lepton_Vm_),
                deadEnd); // makes photons and don't care
        lepton_t_.init(
                registry,
                lepton_t,
                2.903e-13F,
                1,
                3,
                new EMDecay(0.05F, lepton_m_, lepton_Vm, lepton_Vt_, boson_H__),
                new EMDecay(0.1F, lepton_e_, lepton_Ve, lepton_Vm_),
                new EMDecay(0.8F, lepton_m_, lepton_Vm, lepton_Vt_, boson_Y__),
                deadEnd); // makes photons and don't care
    }

    @Override
    public String getLocalizedName() {
        return translateToLocal("tt.keyword.Lepton") + ": " + getShortLocalizedName();
    }

    @Override
    public boolean isTimeSpanHalfLife() {
        return true;
    }
}
