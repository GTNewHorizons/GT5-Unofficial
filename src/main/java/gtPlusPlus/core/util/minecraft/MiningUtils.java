package gtPlusPlus.core.util.minecraft;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import gregtech.common.GT_Worldgen_GT_Ore_Layer;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.util.reflect.ReflectionUtils;

public class MiningUtils {

    public static boolean getBlockType(final Block block, final World world, final int[] xyz, final int miningLevel) {
        final String LIQUID = "liquid";
        final String BLOCK = "block";
        final String ORE = "ore";
        final String AIR = "air";
        String blockClass;

        if (world.isRemote) {
            return false;
        }

        if (block == Blocks.end_stone) {
            return true;
        }
        if (block == Blocks.stone) {
            return true;
        }
        if (block == Blocks.sandstone) {
            return true;
        }
        if (block == Blocks.netherrack) {
            return true;
        }
        if (block == Blocks.nether_brick) {
            return true;
        }
        if (block == Blocks.nether_brick_stairs) {
            return true;
        }
        if (block == Blocks.nether_brick_fence) {
            return true;
        }
        if (block == Blocks.glowstone) {
            return true;
        }

        try {
            blockClass = block.getClass().toString().toLowerCase();
            Logger.WARNING(blockClass);
            if (blockClass.toLowerCase().contains(LIQUID)) {
                Logger.WARNING(block + " is a Liquid.");
                return false;
            } else if (blockClass.toLowerCase().contains(ORE)) {
                Logger.WARNING(block + " is an Ore.");
                return true;
            } else if (block.getHarvestLevel(world.getBlockMetadata(xyz[0], xyz[1], xyz[2])) >= miningLevel) {
                Logger.WARNING(block + " is minable.");
                return true;
            } else if (blockClass.toLowerCase().contains(AIR)) {
                Logger.WARNING(block + " is Air.");
                return false;
            } else if (blockClass.toLowerCase().contains(BLOCK)) {
                Logger.WARNING(block + " is a block of some kind.");
                return false;
            } else {
                Logger.WARNING(block + " is mystery.");
                return false;
            }
        } catch (final NullPointerException e) {
            return false;
        }
    }

    public static int mMoonID = -99;
    public static int mMarsID = -99;
    public static int mCometsID = -99;

    public static void iterateAllOreTypes() {
        HashMap<String, Integer> M = new HashMap<>();
        String aTextWorldGen;
        if (MiningUtils.findAndMapOreTypesFromGT()) {
            int mapKey = 0;
            for (AutoMap<GT_Worldgen_GT_Ore_Layer> g : MiningUtils.mOreMaps) {
                for (GT_Worldgen_GT_Ore_Layer h : g) {

                    try {
                        aTextWorldGen = (String) ReflectionUtils
                                .getField(GT_Worldgen_GT_Ore_Layer.class, "aTextWorldgen").get(h);
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        aTextWorldGen = h.mWorldGenName;
                    }

                    M.put(aTextWorldGen + h.mWorldGenName, mapKey);
                    Logger.INFO("Found Vein type: " + aTextWorldGen + h.mWorldGenName + " in map with key: " + mapKey);
                }
                mapKey++;
            }
        }
    }

    public static AutoMap<GT_Worldgen_GT_Ore_Layer>[] mOreMaps = new AutoMap[7];
    private static final AutoMap<GT_Worldgen_GT_Ore_Layer> Ores_Overworld = new AutoMap<>();
    private static final AutoMap<GT_Worldgen_GT_Ore_Layer> Ores_Nether = new AutoMap<>();
    private static final AutoMap<GT_Worldgen_GT_Ore_Layer> Ores_End = new AutoMap<>();
    private static final AutoMap<GT_Worldgen_GT_Ore_Layer> Ores_Moon = new AutoMap<>();
    private static final AutoMap<GT_Worldgen_GT_Ore_Layer> Ores_Mars = new AutoMap<>();
    private static final AutoMap<GT_Worldgen_GT_Ore_Layer> Ores_Comets = new AutoMap<>();
    private static final AutoMap<GT_Worldgen_GT_Ore_Layer> Ores_Misc = new AutoMap<>();

    public static boolean findAndMapOreTypesFromGT() {
        // Gets Moon ID

        boolean aEndAsteroids;
        try {
            if (ReflectionUtils.getClass("micdoodle8.mods.galacticraft.core.util.ConfigManagerCore") != null
                    && mMoonID == -99) {
                mMoonID = ReflectionUtils.getField(
                        ReflectionUtils.getClass("micdoodle8.mods.galacticraft.core.util.ConfigManagerCore"),
                        "idDimensionMoon").getInt(null);
            }
        } catch (IllegalArgumentException | IllegalAccessException ignored) {}

        // Gets Mars ID
        try {
            if (ReflectionUtils.getClass("micdoodle8.mods.galacticraft.planets.mars.ConfigManagerMars") != null
                    && mMarsID == -99) {
                mMarsID = ReflectionUtils.getField(
                        ReflectionUtils.getClass("micdoodle8.mods.galacticraft.planets.mars.ConfigManagerMars"),
                        "dimensionIDMars").getInt(null);
            }
        } catch (IllegalArgumentException | IllegalAccessException ignored) {}

        // Get Comets ID
        try {
            if (ReflectionUtils.getClass("micdoodle8.mods.galacticraft.planets.asteroids.ConfigManagerAsteroids")
                    != null && mCometsID == -99) {
                mCometsID = ReflectionUtils.getField(
                        ReflectionUtils
                                .getClass("micdoodle8.mods.galacticraft.planets.asteroids.ConfigManagerAsteroids"),
                        "dimensionIDAsteroids").getInt(null);
            }
        } catch (IllegalArgumentException | IllegalAccessException ignored) {}

        // Clear Cache
        Ores_Overworld.clear();
        Ores_Nether.clear();
        Ores_End.clear();
        Ores_Misc.clear();

        for (GT_Worldgen_GT_Ore_Layer x : GT_Worldgen_GT_Ore_Layer.sList) {
            if (x.mEnabled) {

                try {
                    aEndAsteroids = ReflectionUtils.getField(GT_Worldgen_GT_Ore_Layer.class, "mEndAsteroid")
                            .getBoolean(x);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    aEndAsteroids = false;
                }

                if (x.mOverworld) {
                    Ores_Overworld.put(x);
                }
                if (x.mNether) {
                    Ores_Nether.put(x);
                }
                if (x.mEnd || aEndAsteroids) {
                    Ores_End.put(x);
                }
                if (x.mOverworld || x.mNether || (x.mEnd || aEndAsteroids)) {
                    continue;
                }
                /*
                 * if (x.mMoon) { Ores_Moon.put(x); continue; } if (x.mMars) { Ores_Mars.put(x); continue; } if
                 * (x.mAsteroid) { Ores_Comets.put(x); continue; }
                 */
                Ores_Misc.put(x);
                continue;
            } else {
                Ores_Comets.put(x);
            }
        }

        mOreMaps[0] = Ores_Overworld;
        mOreMaps[1] = Ores_Nether;
        mOreMaps[2] = Ores_End;
        mOreMaps[3] = Ores_Moon;
        mOreMaps[4] = Ores_Mars;
        mOreMaps[5] = Ores_Comets;
        mOreMaps[6] = Ores_Misc;
        return true;
    }
}
