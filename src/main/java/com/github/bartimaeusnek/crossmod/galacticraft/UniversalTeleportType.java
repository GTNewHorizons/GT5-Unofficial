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

package com.github.bartimaeusnek.crossmod.galacticraft;

import com.github.bartimaeusnek.bartworks.API.LoaderReference;
import java.util.Random;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.ITeleportType;
import micdoodle8.mods.galacticraft.core.entities.EntityLander;
import micdoodle8.mods.galacticraft.core.entities.EntityLanderBase;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class UniversalTeleportType implements ITeleportType {

    @Override
    public boolean useParachute() {
        return false;
    }

    @Override
    public Vector3 getPlayerSpawnLocation(WorldServer world, EntityPlayerMP player) {
        return this.getEntitySpawnLocation(world, player);
    }

    @Override
    public Vector3 getEntitySpawnLocation(WorldServer world, Entity entity) {
        if (entity instanceof EntityPlayerMP) {
            GCPlayerStats stats = GCPlayerStats.get((EntityPlayerMP) entity);
            return new Vector3(stats.coordsTeleportedFromX, 500D, stats.coordsTeleportedFromZ);
        }
        return new Vector3(entity.posX, 500D, entity.posZ);
    }

    @Override
    public Vector3 getParaChestSpawnLocation(WorldServer world, EntityPlayerMP player, Random rand) {
        return null;
    }

    @Override
    public void onSpaceDimensionChanged(World newWorld, EntityPlayerMP player, boolean ridingAutoRocket) {
        if (ridingAutoRocket) return;
        if ((player != null) && (GCPlayerStats.get(player).teleportCooldown <= 0)) {
            if (player.capabilities.isFlying) {
                player.capabilities.isFlying = false;
            }

            EntityLanderBase elb;
            if (LoaderReference.GalacticraftMars) elb = PlanetsHelperClass.getLanderType(player);
            else elb = new EntityLander(player);

            if (!newWorld.isRemote) {
                newWorld.spawnEntityInWorld(elb);
            }
            GCPlayerStats.get(player).teleportCooldown = 10;
        }
    }

    @Override
    public void setupAdventureSpawn(EntityPlayerMP player) {}
}
