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

package com.github.bartimaeusnek.crossmod.galacticraft.atmosphere;

import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import com.github.bartimaeusnek.bartworks.util.Pair;
import com.github.bartimaeusnek.crossmod.BartWorksCrossmod;
import com.google.common.collect.ArrayListMultimap;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ISubTagContainer;
import java.util.*;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.world.IAtmosphericGas;

@SuppressWarnings({"unused", "RedundantSuppression"})
public final class BWAtmosphereManager {

    private static final Map<Integer, Integer[]> COEFFICIENT_MAP = new HashMap<>();

    private BWAtmosphereManager() {
        BWAtmosphereManager.COEFFICIENT_MAP.put(1, new Integer[] {100});
        BWAtmosphereManager.COEFFICIENT_MAP.put(2, new Integer[] {70, 30});
        BWAtmosphereManager.COEFFICIENT_MAP.put(3, new Integer[] {60, 25, 15});
        BWAtmosphereManager.COEFFICIENT_MAP.put(4, new Integer[] {50, 25, 15, 10});
        BWAtmosphereManager.COEFFICIENT_MAP.put(5, new Integer[] {45, 25, 15, 10, 5});
        BWAtmosphereManager.COEFFICIENT_MAP.put(6, new Integer[] {45, 20, 15, 10, 5, 5});
        BWAtmosphereManager.COEFFICIENT_MAP.put(7, new Integer[] {40, 20, 15, 10, 5, 5, 5});
        BWAtmosphereManager.COEFFICIENT_MAP.put(8, new Integer[] {35, 20, 15, 10, 5, 5, 5, 5});
        BWAtmosphereManager.COEFFICIENT_MAP.put(9, new Integer[] {35, 15, 15, 10, 5, 5, 5, 5, 5});
    }

    public static final BWAtmosphereManager INSTANCE = new BWAtmosphereManager();

    private static final ArrayListMultimap<Integer, Pair<ISubTagContainer, Integer>> gasConcentration =
            ArrayListMultimap.create();

    public static List<Pair<ISubTagContainer, Integer>> getGasFromWorldID(int worldID) {
        return BWAtmosphereManager.gasConcentration.get(worldID);
    }

    public static void removeGasFromWorld(int worldID, ISubTagContainer gas) {
        for (Pair<ISubTagContainer, Integer> pair : BWAtmosphereManager.gasConcentration.get(worldID)) {
            if (pair.getKey().equals(gas)) {
                BWAtmosphereManager.gasConcentration.get(worldID).remove(pair);
                return;
            }
        }
    }

    public static void addGasToWorld(int worldID, ISubTagContainer gas, int amount) {
        Pair<ISubTagContainer, Integer> toadd = new Pair<>(gas, amount);
        BWAtmosphereManager.gasConcentration.put(worldID, toadd);
    }

    public static void addGasToWorld(int worldID, Pair<ISubTagContainer, Integer> toPut) {
        BWAtmosphereManager.gasConcentration.put(worldID, toPut);
    }

    @SafeVarargs
    public static void addGasToWorld(int worldID, Pair<ISubTagContainer, Integer>... toPut) {
        Arrays.stream(toPut).forEach(toadd -> BWAtmosphereManager.gasConcentration.put(worldID, toadd));
    }

    private static boolean addGCGasToWorld(int worldID, IAtmosphericGas gas, int aNumber, int aMaxNumber) {
        if (gas.equals(IAtmosphericGas.CO2)) {
            BWAtmosphereManager.addGasToWorld(
                    worldID,
                    Materials.CarbonDioxide,
                    BWAtmosphereManager.COEFFICIENT_MAP.get(aMaxNumber)[aNumber]);
            return true;
        }
        String name = gas.toString();
        name = name.charAt(0) + name.substring(1).toLowerCase(Locale.US);
        ISubTagContainer mat = Materials.get(name);
        if (mat == Materials._NULL) {
            mat = WerkstoffLoader.getWerkstoff(name);
        }
        if (mat == Werkstoff.default_null_Werkstoff) {
            return false;
        }
        BWAtmosphereManager.addGasToWorld(
                worldID, mat, BWAtmosphereManager.COEFFICIENT_MAP.get(aMaxNumber)[aNumber]);
        return true;
    }

    @SubscribeEvent
    public void gcAutoRegister(GalaxyRegistry.PlanetRegisterEvent event) {
        CelestialBody planet = GalaxyRegistry.getRegisteredPlanets().get(event.planetName);
        for (int i = 0; i < planet.atmosphere.size(); i++) {
            if (!BWAtmosphereManager.addGCGasToWorld(
                    planet.getDimensionID(), planet.atmosphere.get(i), i, planet.atmosphere.size()))
                BartWorksCrossmod.LOGGER.warn("Unidentified Fluid (" + planet.atmosphere.get(i)
                        + ") in the Atmosphere of: " + planet.getLocalizedName());
        }
    }
}
