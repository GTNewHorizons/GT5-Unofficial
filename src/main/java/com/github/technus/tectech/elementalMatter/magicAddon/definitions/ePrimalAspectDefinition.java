package com.github.technus.tectech.elementalMatter.magicAddon.definitions;

import com.github.technus.tectech.elementalMatter.classes.cElementalPrimitive;

import static com.github.technus.tectech.elementalMatter.classes.cElementalDecay.noDecay;

/**
 * Created by Tec on 06.05.2017.
 */
public final class ePrimalAspectDefinition extends cElementalPrimitive {
    public static final ePrimalAspectDefinition
            magic_order = new ePrimalAspectDefinition("Order", "o`", 0, 0F, 0, -1, 30),
            magic_entropy_ = new ePrimalAspectDefinition("Entropy", "e`", 0, 0F, 0, -1, 31),
            magic_water = new ePrimalAspectDefinition("Water", "w`", 0, 0F, 0, -1, 32),
            magic_fire_ = new ePrimalAspectDefinition("Fire", "f`", 0, 0F, 0, -1, 33),
            magic_earth = new ePrimalAspectDefinition("Earth", "e`", 0, 0F, 0, -1, 34),
            magic_air_ = new ePrimalAspectDefinition("Air", "a`", 0, 0F, 0, -1, 35);

    private ePrimalAspectDefinition(String name, String symbol, int type, float mass, int charge, int color, int ID) {
        super(name, symbol, type, mass, charge, color, ID);
    }

    public static void run() {
        magic_order.init(magic_entropy_, -1F, -1, -1, noDecay);
        magic_entropy_.init(magic_order, -1F, -1, -1, noDecay);
        magic_water.init(magic_fire_, -1F, -1, -1, noDecay);
        magic_fire_.init(magic_water, -1F, -1, -1, noDecay);
        magic_earth.init(magic_air_, -1F, -1, -1, noDecay);
        magic_air_.init(magic_earth, -1F, -1, -1, noDecay);
    }

    @Override
    public String getName() {
        return "Primal: " + name;
    }

    //TODO aspect binding
}

