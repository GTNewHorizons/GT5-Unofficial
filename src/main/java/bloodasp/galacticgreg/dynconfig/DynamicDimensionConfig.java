package bloodasp.galacticgreg.dynconfig;

import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.common.ChestGenHooks;

import bloodasp.galacticgreg.GalacticGreg;
import bloodasp.galacticgreg.api.Enums.DimensionType;
import bloodasp.galacticgreg.api.ModContainer;
import bloodasp.galacticgreg.api.ModDimensionDef;
import bloodasp.galacticgreg.api.enums.properties.Asteroids;
import bloodasp.galacticgreg.registry.GalacticGregRegistry;

public class DynamicDimensionConfig {

    public static class AsteroidConfig {

        public int MinSize;
        public int MaxSize;
        public int Probability;
        public int OreChance;
        public int OrePrimaryOffset;
        public int SpecialBlockChance;
        public int SmallOreChance;
        public boolean ObeyHeightLimits;
        public boolean HiddenOres;
        public int LootChestChance;
        public int LootChestTable;
        public int NumLootItems;
        public boolean RandomizeNumLootItems;
    }

    private static Map<String, AsteroidConfig> _mDynamicAsteroidMap = new HashMap<>();

    public static AsteroidConfig getAsteroidConfig(ModDimensionDef pDimDef) {
        return _mDynamicAsteroidMap.getOrDefault(pDimDef.getDimIdentifier(), null);
    }

    public static boolean InitDynamicConfig() {
        try {
            for (ModContainer mc : GalacticGregRegistry.getModContainers()) {
                if (!mc.getEnabled()) continue;

                for (ModDimensionDef mdd : mc.getDimensionList()) {
                    DimensionType dt = mdd.getDimensionType();
                    if (dt != DimensionType.Asteroid) {
                        continue;
                    }
                    String tDimIdentifier = mdd.getDimIdentifier();
                    if (_mDynamicAsteroidMap.containsKey(tDimIdentifier)) {
                        GalacticGreg.Logger.warn(
                            "Found 2 Dimensions with the same Identifier! This should never happen, and you should report this to me. Identifier in question: %s",
                            tDimIdentifier);
                        continue;
                    }

                    Asteroids AsteroidProperties = null;
                    for (Asteroids asteroidsConfig : Asteroids.values()) {
                        if (!asteroidsConfig.modContainers.modContainer.getModName()
                            .equals(mc.getModName())) {
                            continue;
                        }
                        if (!asteroidsConfig.dimensionDef.modDimensionDef.getDimensionName()
                            .equals(mdd.getDimensionName())) {
                            continue;
                        }
                        AsteroidProperties = asteroidsConfig;
                        break;
                    }
                    if (AsteroidProperties == null) {
                        GalacticGreg.Logger.error(
                            "Something went wrong! no properties are existing for Asteroid dim: "
                                + mdd.getDimensionName()
                                + " from mod container "
                                + mc.getModName());
                        continue;
                    }

                    AsteroidConfig aConf = new AsteroidConfig();

                    aConf.MinSize = AsteroidProperties.asteroidPropertyBuilder.sizeMin;
                    aConf.MaxSize = AsteroidProperties.asteroidPropertyBuilder.sizeMax;
                    aConf.Probability = AsteroidProperties.asteroidPropertyBuilder.probability;
                    aConf.SpecialBlockChance = AsteroidProperties.asteroidPropertyBuilder.specialBlockChance;

                    aConf.OreChance = AsteroidProperties.asteroidPropertyBuilder.oreSpawn.baseOreChance;
                    aConf.OrePrimaryOffset = AsteroidProperties.asteroidPropertyBuilder.oreSpawn.primaryToRareOreOffset;
                    aConf.SmallOreChance = AsteroidProperties.asteroidPropertyBuilder.oreSpawn.smallOreChance;
                    aConf.ObeyHeightLimits = AsteroidProperties.asteroidPropertyBuilder.oreSpawn.obeyHeighLimits;
                    aConf.HiddenOres = AsteroidProperties.asteroidPropertyBuilder.oreSpawn.oresOnlyInsideAsteroids;

                    if (GalacticGreg.GalacticConfig.LootChestsEnabled) {
                        aConf.LootChestChance = AsteroidProperties.asteroidPropertyBuilder.loot.lootChestChance;
                        aConf.LootChestTable = AsteroidProperties.asteroidPropertyBuilder.loot.lootChestTable;
                        aConf.NumLootItems = AsteroidProperties.asteroidPropertyBuilder.loot.lootChestItemCount;
                        aConf.RandomizeNumLootItems = AsteroidProperties.asteroidPropertyBuilder.loot.randomizeLootItemCount;
                    } else {
                        aConf.LootChestChance = 0;
                        aConf.LootChestTable = 1;
                        aConf.NumLootItems = 0;
                        aConf.RandomizeNumLootItems = false;
                    }

                    if (aConf.MaxSize > 50) GalacticGreg.Logger.warn(
                        "Asteroid-MaxSize for dimID [%s] is larger than 50. This might cause memory-problems, as the maximum asteroid size will be larger than 50*50*50 blocks",
                        tDimIdentifier);
                    _mDynamicAsteroidMap.put(tDimIdentifier, aConf);

                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Convert numbers to actual loot-table entries
     *
     * @param pACfg
     * @return
     */
    public static String getLootChestTable(AsteroidConfig pACfg) {
        String tLootTable = ChestGenHooks.MINESHAFT_CORRIDOR;

        switch (pACfg.LootChestTable) {
            case 2:
                tLootTable = ChestGenHooks.PYRAMID_DESERT_CHEST;
                break;
            case 3:
                tLootTable = ChestGenHooks.PYRAMID_JUNGLE_CHEST;
                break;
            case 4:
                tLootTable = ChestGenHooks.PYRAMID_JUNGLE_DISPENSER;
                break;
            case 5:
                tLootTable = ChestGenHooks.STRONGHOLD_CORRIDOR;
                break;
            case 6:
                tLootTable = ChestGenHooks.STRONGHOLD_LIBRARY;
                break;
            case 7:
                tLootTable = ChestGenHooks.STRONGHOLD_CROSSING;
                break;
            case 8:
                tLootTable = ChestGenHooks.VILLAGE_BLACKSMITH;
                break;
            case 9:
                tLootTable = ChestGenHooks.BONUS_CHEST;
                break;
            case 10:
                tLootTable = ChestGenHooks.DUNGEON_CHEST;
                break;
        }

        return tLootTable;
    }
}
