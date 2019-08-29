package com.github.technus.tectech.compatibility.thaumcraft.elementalMatter.definitions;

import com.github.technus.tectech.mechanics.elementalMatter.core.templates.cElementalPrimitive;

import static com.github.technus.tectech.mechanics.elementalMatter.core.cElementalDecay.noDecay;
import static net.minecraft.util.StatCollector.translateToLocal;

/**
 * Created by Tec on 06.05.2017.
 */
public final class ePrimalAspectDefinition extends cElementalPrimitive implements iElementalAspect {
    public static final ePrimalAspectDefinition
            magic_air = new ePrimalAspectDefinition(translateToLocal("tt.keyword.Air"), "a`", 1e1F, 35),
            magic_earth = new ePrimalAspectDefinition(translateToLocal("tt.keyword.Earth"), "e`", 1e9F, 34),
            magic_fire = new ePrimalAspectDefinition(translateToLocal("tt.keyword.Fire"), "f`", 1e3F, 33),
            magic_water = new ePrimalAspectDefinition(translateToLocal("tt.keyword.Water"), "w`", 1e7F, 32),
            magic_order = new ePrimalAspectDefinition(translateToLocal("tt.keyword.Order"), "o`", 1e5F, 30),
            magic_entropy = new ePrimalAspectDefinition(translateToLocal("tt.keyword.Entropy"), "e`", 1e5F, 31);

    private ePrimalAspectDefinition(String name, String symbol, float mass, int ID) {
        super(name, symbol, 0, mass, 0, -1, ID);
    }

    public static void run() {
        magic_air.init(null, -1F, -1, -1, noDecay);
        magic_earth.init(null, -1F, -1, -1, noDecay);
        magic_fire.init(null, -1F, -1, -1, noDecay);
        magic_water.init(null, -1F, -1, -1, noDecay);
        magic_order.init(null, -1F, -1, -1, noDecay);
        magic_entropy.init(null, -1F, -1, -1, noDecay);
    }

    @Override
    public String getName() {
        return translateToLocal("tt.keyword.Primal") + ": " + name;
    }

    @Override
    public Object materializeIntoAspect() {
        return AspectDefinitionCompat.aspectDefinitionCompat.getAspect(this);
    }

    @Override
    public boolean isTimeSpanHalfLife() {
        return false;
    }
}