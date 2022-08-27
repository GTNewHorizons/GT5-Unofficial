/*
 * Copyright (c) 2018-2020 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.crossmod.galacticraft.planets.ross128b;

import com.github.bartimaeusnek.crossmod.galacticraft.planets.AbstractWorldProviderSpace;
import com.github.bartimaeusnek.crossmod.galacticraft.solarsystems.Ross128SolarSystem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.IChunkProvider;

public class WorldProviderRoss128b extends AbstractWorldProviderSpace {

    @Override
    public boolean canRespawnHere() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public Vec3 getFogColor(float cy, float noidea) {
        float angle = MathHelper.cos(cy * (float) Math.PI * 2.0F) * 2.0F + 0.5F;

        if (angle < 0.0F) {
            angle = 0.0F;
        }

        if (angle > 1.0F) {
            angle = 1.0F;
        }

        float red = 200 / 255f;
        float green = 80 / 255f;
        float blue = 0.0F;
        red *= angle * 0.94F + 0.06F;
        green *= angle * 0.94F + 0.06F;
        return Vec3.createVectorHelper(red, green, blue);
    }

    @Override
    public Vector3 getFogColor() {
        // unused
        return null;
    }

    @Override
    public float getSunBrightness(float par1) {
        return super.getSunBrightness(par1) * 0.975f;
    }

    @Override
    public float calculateCelestialAngle(long par1, float par3) {
        return super.calculateCelestialAngle(par1, par3);
    }

    @Override
    public Vector3 getSkyColor() {
        float red = 200 / 255f;
        float green = 120 / 255f;
        float blue = 0.0F;
        return new Vector3(red, green, blue);
    }

    @Override
    public long getDayLength() {
        return (long) (24000 * 9.9f);
    }

    @Override
    public Class<? extends IChunkProvider> getChunkProviderClass() {
        return ChunkProviderRoss128b.class;
    }

    @Override
    public Class<? extends WorldChunkManager> getWorldChunkManagerClass() {
        return WorldChunkManager.class;
    }

    @Override
    public float getGravity() {
        return -0.0035F;
    }

    @Override
    public double getMeteorFrequency() {
        return 0D;
    }

    @Override
    public double getFuelUsageMultiplier() {
        return 1.35D;
    }

    @Override
    public boolean canSpaceshipTierPass(int tier) {
        return Ross128SolarSystem.Ross128b.getTierRequirement() <= tier;
    }

    @Override
    public float getFallDamageModifier() {
        return 1.35F;
    }

    @Override
    public float getSoundVolReductionAmount() {
        return 1F;
    }

    @Override
    public float getThermalLevelModifier() {
        return 0.01f;
    }

    @Override
    public float getWindLevel() {
        return 1.35f;
    }

    @Override
    public CelestialBody getCelestialBody() {
        return Ross128SolarSystem.Ross128b;
    }

    @Override
    public double getYCoordinateToTeleport() {
        return 500D;
    }

    @Override
    public double getSolarEnergyMultiplier() {
        return 1.38D;
    }

    @Override
    public boolean hasBreathableAtmosphere() {
        return true;
    }
}
