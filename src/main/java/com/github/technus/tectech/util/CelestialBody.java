package com.github.technus.tectech.util;

import net.minecraft.block.Block;

import java.util.ArrayList;

public class CelestialBody {

    CelestialBody(Block mainBody, ArrayList<CelestialBody> orbitingBodies) {
        this.mainBody = mainBody;
        this.orbitingBodies = orbitingBodies;
    }

    public Block mainBody;

    public ArrayList<CelestialBody> orbitingBodies;
}
