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

package bwcrossmod.galacticraft.atmosphere;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ArrayListMultimap;

import bartworks.system.material.Werkstoff;
import bartworks.system.material.WerkstoffLoader;
import bwcrossmod.BartWorksCrossmod;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ISubTagContainer;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.world.IAtmosphericGas;

public final class BWAtmosphereManager {

    private static final int[][] COEFFICIENT_ARRAY = new int[9][];

    private BWAtmosphereManager() {
        COEFFICIENT_ARRAY[0] = new int[] { 100 };
        COEFFICIENT_ARRAY[1] = new int[] { 70, 30 };
        COEFFICIENT_ARRAY[2] = new int[] { 60, 25, 15 };
        COEFFICIENT_ARRAY[3] = new int[] { 50, 25, 15, 10 };
        COEFFICIENT_ARRAY[4] = new int[] { 45, 25, 15, 10, 5 };
        COEFFICIENT_ARRAY[5] = new int[] { 45, 20, 15, 10, 5, 5 };
        COEFFICIENT_ARRAY[6] = new int[] { 40, 20, 15, 10, 5, 5, 5 };
        COEFFICIENT_ARRAY[7] = new int[] { 35, 20, 15, 10, 5, 5, 5, 5 };
        COEFFICIENT_ARRAY[8] = new int[] { 35, 15, 15, 10, 5, 5, 5, 5, 5 };
    }

    public static final BWAtmosphereManager INSTANCE = new BWAtmosphereManager();

    private static final ArrayListMultimap<Integer, Pair<ISubTagContainer, Integer>> gasConcentration = ArrayListMultimap
        .create();

    public static List<Pair<ISubTagContainer, Integer>> getGasFromWorldID(int worldID) {
        return BWAtmosphereManager.gasConcentration.get(worldID);
    }

    public static void removeGasFromWorld(int worldID, ISubTagContainer gas) {
        for (Pair<ISubTagContainer, Integer> pair : BWAtmosphereManager.gasConcentration.get(worldID)) {
            if (pair.getKey()
                .equals(gas)) {
                BWAtmosphereManager.gasConcentration.get(worldID)
                    .remove(pair);
                return;
            }
        }
    }

    public static void addGasToWorld(int worldID, ISubTagContainer gas, int amount) {
        Pair<ISubTagContainer, Integer> toadd = Pair.of(gas, amount);
        BWAtmosphereManager.gasConcentration.put(worldID, toadd);
    }

    public static void addGasToWorld(int worldID, Pair<ISubTagContainer, Integer> toPut) {
        BWAtmosphereManager.gasConcentration.put(worldID, toPut);
    }

    @SafeVarargs
    public static void addGasToWorld(int worldID, Pair<ISubTagContainer, Integer>... toPut) {
        Arrays.stream(toPut)
            .forEach(toadd -> BWAtmosphereManager.gasConcentration.put(worldID, toadd));
    }

    private static boolean addGCGasToWorld(int worldID, IAtmosphericGas gas, int aNumber, int aMaxNumber) {
        if (IAtmosphericGas.CO2.equals(gas)) {
            BWAtmosphereManager.addGasToWorld(
                worldID,
                Materials.CarbonDioxide,
                BWAtmosphereManager.COEFFICIENT_ARRAY[aMaxNumber - 1][aNumber]);
            return true;
        }
        String name = gas.toString();
        name = name.charAt(0) + name.substring(1)
            .toLowerCase(Locale.US);
        ISubTagContainer mat = Materials.get(name);
        if (mat == Materials._NULL) {
            mat = WerkstoffLoader.getWerkstoff(name);
        }
        if (mat == Werkstoff.default_null_Werkstoff) {
            return false;
        }
        BWAtmosphereManager.addGasToWorld(worldID, mat, BWAtmosphereManager.COEFFICIENT_ARRAY[aMaxNumber - 1][aNumber]);
        return true;
    }

    @SubscribeEvent
    public void gcAutoRegister(GalaxyRegistry.PlanetRegisterEvent event) {
        CelestialBody planet = GalaxyRegistry.getRegisteredPlanets()
            .get(event.planetName);
        for (int i = 0; i < planet.atmosphere.size(); i++) {
            if (!BWAtmosphereManager
                .addGCGasToWorld(planet.getDimensionID(), planet.atmosphere.get(i), i, planet.atmosphere.size()))
                BartWorksCrossmod.LOGGER.warn(
                    "Unidentified Fluid (" + planet.atmosphere.get(i)
                        + ") in the Atmosphere of: "
                        + planet.getLocalizedName());
        }
    }
}
