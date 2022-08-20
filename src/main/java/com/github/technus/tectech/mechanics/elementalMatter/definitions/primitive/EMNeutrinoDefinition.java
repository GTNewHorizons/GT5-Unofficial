package com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive;

import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMGaugeBosonDefinition.deadEnd;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMGaugeBosonDefinition.deadEndHalf;
import static net.minecraft.util.StatCollector.translateToLocal;

import com.github.technus.tectech.mechanics.elementalMatter.core.decay.EMDecay;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.registry.EMDefinitionsRegistry;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.registry.EMType;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.EMDefinitionStack;

/**
 * Created by danie_000 on 22.10.2016.
 */
public class EMNeutrinoDefinition extends EMLeptonDefinition {
    public static final EMNeutrinoDefinition
            lepton_Ve = new EMNeutrinoDefinition("tt.keyword.ElectronNeutrino", "\u03bd\u03b2", 1, 2e0D, 21, "Ve"),
            lepton_Vm = new EMNeutrinoDefinition("tt.keyword.MuonNeutrino", "\u03bd\u03bc", 2, 0.15e6D, 23, "Vm"),
            lepton_Vt = new EMNeutrinoDefinition("tt.keyword.TauonNeutrino", "\u03bd\u03c4", 3, 15e6D, 25, "Vt"),
            lepton_Ve_ = new EMNeutrinoDefinition("tt.keyword.PositronNeutrino", "~\u03bd\u03b2", -1, 2e0D, 22, "~Ve"),
            lepton_Vm_ =
                    new EMNeutrinoDefinition("tt.keyword.AntimuonNeutrino", "~\u03bd\u03bc", -2, 0.15e6D, 24, "~Vm"),
            lepton_Vt_ =
                    new EMNeutrinoDefinition("tt.keyword.AntitauonNeutrino", "~\u03bd\u03c4", -3, 15e6D, 26, "~Vt");

    public static final EMDefinitionStack lepton_Ve1 = new EMDefinitionStack(lepton_Ve, 1);
    public static final EMDefinitionStack lepton_Ve2 = new EMDefinitionStack(lepton_Ve, 2);
    public static final EMDefinitionStack lepton_Ve_1 = new EMDefinitionStack(lepton_Ve_, 1);
    public static final EMDefinitionStack lepton_Ve_2 = new EMDefinitionStack(lepton_Ve_, 2);

    protected EMNeutrinoDefinition(String name, String symbol, int type, double mass, int ID, String bind) {
        super(name, symbol, type, mass, 0, ID, bind);
    }

    public static void run(EMDefinitionsRegistry registry) {
        registry.registerDefinitionClass(new EMType(EMNeutrinoDefinition.class, "tt.keyword.Neutrino"));
        lepton_Ve.init(registry, lepton_Ve_, 1D, -1, -1, EMDecay.NO_PRODUCT);
        lepton_Vm.init(registry, lepton_Vm_, 1D, 1, 0, new EMDecay(0.825D, nothing), deadEndHalf);
        lepton_Vt.init(registry, lepton_Vt_, 1, 1, 0, new EMDecay(0.75F, nothing), deadEnd);

        lepton_Ve_.init(registry, lepton_Ve, 1, -1, -1, EMDecay.NO_PRODUCT);
        lepton_Vm_.init(registry, lepton_Vm, 1, 1, 0, new EMDecay(0.825F, nothing), deadEndHalf);
        lepton_Vt_.init(registry, lepton_Vt, 1, 1, 0, new EMDecay(0.75F, nothing), deadEnd);
    }

    @Override
    public String getLocalizedTypeName() {
        return translateToLocal("tt.keyword.Neutrino");
    }

    @Override
    public boolean isTimeSpanHalfLife() {
        return true;
    }
}
