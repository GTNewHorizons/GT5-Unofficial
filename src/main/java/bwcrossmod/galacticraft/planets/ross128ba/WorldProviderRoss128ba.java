/*
 * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package bwcrossmod.galacticraft.planets.ross128ba;

import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.IChunkProvider;

import bartworks.util.MathUtils;
import bwcrossmod.galacticraft.planets.AbstractWorldProviderSpace;
import bwcrossmod.galacticraft.solarsystems.Ross128SolarSystem;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.world.gen.WorldChunkManagerMoon;

public class WorldProviderRoss128ba extends AbstractWorldProviderSpace {

    @Override
    public Vector3 getFogColor() {
        return new Vector3(0, 0, 0);
    }

    @Override
    public Vector3 getSkyColor() {
        return new Vector3(0, 0, 0);
    }

    @Override
    public long getDayLength() {
        return MathUtils.floorLong(24000f * 9.9f / 100f);
    }

    @Override
    public boolean hasSunset() {
        return false;
    }

    @Override
    public Class<? extends IChunkProvider> getChunkProviderClass() {
        return ChunkProviderRoss128ba.class;
    }

    @Override
    public Class<? extends WorldChunkManager> getWorldChunkManagerClass() {
        return WorldChunkManagerMoon.class;
    }

    @Override
    public double getYCoordinateToTeleport() {
        return 500;
    }

    @Override
    public float getGravity() {
        return 0.060f;
    }

    @Override
    public double getMeteorFrequency() {
        return 9D;
    }

    @Override
    public double getFuelUsageMultiplier() {
        return 0.7D;
    }

    @Override
    public boolean canSpaceshipTierPass(int i) {
        return i >= Ross128SolarSystem.Ross128ba.getTierRequirement();
    }

    @Override
    public float getFallDamageModifier() {
        return 0.2f;
    }

    @Override
    public float getSoundVolReductionAmount() {
        return 20f;
    }

    @Override
    public float getThermalLevelModifier() {
        return 0;
    }

    @Override
    public float getWindLevel() {
        return 0;
    }

    @Override
    public CelestialBody getCelestialBody() {
        return Ross128SolarSystem.Ross128ba;
    }

    @Override
    public double getSolarEnergyMultiplier() {
        return 1.9D;
    }
}
