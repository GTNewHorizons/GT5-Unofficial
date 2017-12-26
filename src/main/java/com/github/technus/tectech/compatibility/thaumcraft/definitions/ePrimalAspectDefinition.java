package com.github.technus.tectech.compatibility.thaumcraft.definitions;

import com.github.technus.tectech.elementalMatter.core.templates.cElementalPrimitive;

import static com.github.technus.tectech.compatibility.thaumcraft.definitions.AspectDefinitionCompat.aspectDefinitionCompat;
import static com.github.technus.tectech.elementalMatter.core.cElementalDecay.noDecay;

/**
 * Created by Tec on 06.05.2017.
 */
public final class ePrimalAspectDefinition extends cElementalPrimitive implements iElementalAspect {
    public static final ePrimalAspectDefinition
            magic_air = new ePrimalAspectDefinition("Air", "a`", 0, 1e1F, 0, -1, 35),
            magic_earth = new ePrimalAspectDefinition("Earth", "e`", 0, 1e9F, 0, -1, 34),
            magic_fire = new ePrimalAspectDefinition("Fire", "f`", 0, 1e3F, 0, -1, 33),
            magic_water = new ePrimalAspectDefinition("Water", "w`", 0, 1e7F, 0, -1, 32),
            magic_order = new ePrimalAspectDefinition("Order", "o`", 0, 1e5F, 0, -1, 30),
            magic_entropy = new ePrimalAspectDefinition("Entropy", "e`", 0, 1e5F, 0, -1, 31);

    private ePrimalAspectDefinition(String name, String symbol, int type, float mass, int charge, int color, int ID) {
        super(name, symbol, type, mass, charge, color, ID);
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
        return "Primal: " + name;
    }

    @Override
    public Object materializeIntoAspect() {
        return aspectDefinitionCompat.getAspect(this);
    }

    @Override
    public boolean isTimeSpanHalfLife() {
        return false;
    }
}

