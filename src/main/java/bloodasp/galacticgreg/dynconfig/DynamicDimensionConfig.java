package bloodasp.galacticgreg.dynconfig;

import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.common.ChestGenHooks;

import bloodasp.galacticgreg.GalacticGreg;
import bloodasp.galacticgreg.api.Enums.DimensionType;
import bloodasp.galacticgreg.api.ModContainer;
import bloodasp.galacticgreg.api.ModDimensionDef;
import bloodasp.galacticgreg.registry.GalacticGregRegistry;
import gregtech.api.GregTech_API;

/**
 * This dynamic config is different to the OreMix one. This is used/bound to the ModDimensionDef, and the
 * OreMixWorldConfig is bound to the veins. Don't get confused!
 *
 */
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
        public int OreGenMaxY;
        public int FloatingAsteroidMinY;
        public boolean HiddenOres;
        public int LootChestChance;
        public int LootChestTable;
        public int NumLootItems;
        public boolean RandomizeNumLootItems;
    }

    private static Map<String, AsteroidConfig> _mDynamicAsteroidMap = new HashMap<>();

    private static String getConfigKeyName(ModContainer pMC, ModDimensionDef pMDD) {
        return String.format("galacticgreg.asteroids.%s.%s", pMC.getModName(), pMDD.getDimensionName());
    }

    private static String getConfigKeyName(ModContainer pMC, ModDimensionDef pMDD, String pSubCat) {
        return String.format("%s.%s", getConfigKeyName(pMC, pMDD), pSubCat);
    }

    public static AsteroidConfig getAsteroidConfig(ModDimensionDef pDimDef) {
        return _mDynamicAsteroidMap.getOrDefault(pDimDef.getDimIdentifier(), null);
    }

    public static boolean InitDynamicConfig() {
        try {
            for (ModContainer mc : GalacticGregRegistry.getModContainers()) {
                if (!mc.getEnabled()) continue;

                for (ModDimensionDef mdd : mc.getDimensionList()) {
                    DimensionType dt = mdd.getDimensionType();
                    if (dt == DimensionType.Asteroid || dt == DimensionType.AsteroidAndPlanet) {
                        String tDimIdentifier = mdd.getDimIdentifier();
                        if (_mDynamicAsteroidMap.containsKey(tDimIdentifier)) GalacticGreg.Logger.warn(
                            "Found 2 Dimensions with the same Identifier! This should never happen, and you should report this to me. Identifier in question: %s",
                            tDimIdentifier);
                        else {
                            AsteroidConfig aConf = new AsteroidConfig();
                            aConf.MinSize = GregTech_API.sWorldgenFile.get(getConfigKeyName(mc, mdd), "SizeMin", 5);
                            aConf.MaxSize = GregTech_API.sWorldgenFile.get(getConfigKeyName(mc, mdd), "SizeMax", 15);
                            aConf.Probability = GregTech_API.sWorldgenFile
                                .get(getConfigKeyName(mc, mdd), "Probability", 200);
                            aConf.SpecialBlockChance = GregTech_API.sWorldgenFile
                                .get(getConfigKeyName(mc, mdd), "SpecialBlockChance", 5);

                            aConf.OreChance = GregTech_API.sWorldgenFile
                                .get(getConfigKeyName(mc, mdd, "orespawn"), "BaseOreChance", 5);
                            aConf.OrePrimaryOffset = GregTech_API.sWorldgenFile
                                .get(getConfigKeyName(mc, mdd, "orespawn"), "PrimaryToRareOreOffset", 5);
                            aConf.SmallOreChance = GregTech_API.sWorldgenFile
                                .get(getConfigKeyName(mc, mdd, "orespawn"), "SmallOreChance", 10);
                            aConf.ObeyHeightLimits = GregTech_API.sWorldgenFile
                                .get(getConfigKeyName(mc, mdd, "orespawn"), "ObeyHeightLimits", false);
                            aConf.HiddenOres = GregTech_API.sWorldgenFile
                                .get(getConfigKeyName(mc, mdd, "orespawn"), "OresOnlyInsideAsteroids", false);

                            if (GalacticGreg.GalacticConfig.LootChestsEnabled) {
                                aConf.LootChestChance = GregTech_API.sWorldgenFile
                                    .get(getConfigKeyName(mc, mdd, "loot"), "LootChestChance", 1);
                                aConf.LootChestTable = GregTech_API.sWorldgenFile
                                    .get(getConfigKeyName(mc, mdd, "loot"), "LootChestTable", 3);
                                aConf.NumLootItems = GregTech_API.sWorldgenFile
                                    .get(getConfigKeyName(mc, mdd, "loot"), "LootChestItemCount", 10);
                                aConf.RandomizeNumLootItems = GregTech_API.sWorldgenFile
                                    .get(getConfigKeyName(mc, mdd, "loot"), "RandomizeLootItemCount", true);
                            } else {
                                aConf.LootChestChance = 0;
                                aConf.LootChestTable = 1;
                                aConf.NumLootItems = 0;
                                aConf.RandomizeNumLootItems = false;
                            }

                            if (dt == DimensionType.AsteroidAndPlanet) {
                                int tDefaultMaxY = mdd.getPreConfiguratedGroundOreMaxY();
                                int tDefaultMinY = mdd.getPreConfiguratedFloatingAsteroidMinY();
                                aConf.OreGenMaxY = GregTech_API.sWorldgenFile
                                    .get(getConfigKeyName(mc, mdd, "floating"), "OreGenMaxY", tDefaultMaxY);
                                aConf.FloatingAsteroidMinY = GregTech_API.sWorldgenFile
                                    .get(getConfigKeyName(mc, mdd, "floating"), "FloatingAsteroidMinY", tDefaultMinY);
                            }

                            if (aConf.MaxSize > 50) GalacticGreg.Logger.warn(
                                "Asteroid-MaxSize for dimID [%s] is larger than 50. This might cause memory-problems, as the maximum asteroid size will be larger than 50*50*50 blocks",
                                tDimIdentifier);
                            _mDynamicAsteroidMap.put(tDimIdentifier, aConf);
                        }
                    }
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
