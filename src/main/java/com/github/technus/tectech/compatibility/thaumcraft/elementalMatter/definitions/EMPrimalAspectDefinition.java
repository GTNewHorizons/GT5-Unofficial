package com.github.technus.tectech.compatibility.thaumcraft.elementalMatter.definitions;

import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.EMPrimitiveTemplate;

import static com.github.technus.tectech.mechanics.elementalMatter.core.decay.EMDecay.NO_DECAY;
import static net.minecraft.util.StatCollector.translateToLocal;

/**
 * Created by Tec on 06.05.2017.
 */
public final class EMPrimalAspectDefinition extends EMPrimitiveTemplate {
    public static final EMPrimalAspectDefinition
            magic_air = new EMPrimalAspectDefinition(translateToLocal("tt.keyword.Air"), "a`", 1e1D, 35),
            magic_earth = new EMPrimalAspectDefinition(translateToLocal("tt.keyword.Earth"), "e`", 1e9D, 34),
            magic_fire = new EMPrimalAspectDefinition(translateToLocal("tt.keyword.Fire"), "f`", 1e3D, 33),
            magic_water = new EMPrimalAspectDefinition(translateToLocal("tt.keyword.Water"), "w`", 1e7D, 32),
            magic_order = new EMPrimalAspectDefinition(translateToLocal("tt.keyword.Order"), "o`", 1e5D, 30),
            magic_entropy = new EMPrimalAspectDefinition(translateToLocal("tt.keyword.Entropy"), "e`", 1e5D, 31);

    private EMPrimalAspectDefinition(String name, String symbol, double mass, int ID) {
        super(name, symbol, 0, mass, 0, -1, ID);
    }

    public static void run() {
        magic_air.init(null, -1F, -1, -1, NO_DECAY);
        magic_earth.init(null, -1F, -1, -1, NO_DECAY);
        magic_fire.init(null, -1F, -1, -1, NO_DECAY);
        magic_water.init(null, -1F, -1, -1, NO_DECAY);
        magic_order.init(null, -1F, -1, -1, NO_DECAY);
        magic_entropy.init(null, -1F, -1, -1, NO_DECAY);
    }

    @Override
    public String getLocalizedName() {
        return translateToLocal("tt.keyword.Primal") + ": " + getName();
    }

    @Override
    public boolean isTimeSpanHalfLife() {
        return false;
    }
}