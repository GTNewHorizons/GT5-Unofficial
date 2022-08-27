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

package com.github.bartimaeusnek.bartworks.common.configs;

import com.github.bartimaeusnek.bartworks.API.API_ConfigValues;
import com.github.bartimaeusnek.bartworks.ASM.BWCoreTransformer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {

    private static final int IDU = 10 * 8 + 5;
    public static Configuration c;

    public static int IDOffset = 12600;
    public static int megaMachinesMax = 256;
    public static int mbWaterperSec = 150;
    public static int ross128BID = -64;
    public static int ross128BAID = -63;
    public static int ross128btier = 3;
    public static int ross128batier = 3;
    public static int landerType = 3;
    public static int ross128bRuinChance = 512;
    public static int creativeScannerID;
    public static int bioVatMaxParallelBonus = 1000;
    public static int cutoffTier = 5;
    public static int[][][] metasForTiers;

    public static long energyPerCell = 1000000L;

    public static boolean newStuff = true;
    public static boolean BioLab = true;
    public static boolean Ross128Enabled = true;

    public static boolean disableExtraGassesForEBF;
    public static boolean disableMagicalForest;
    public static boolean DEHPDirectSteam;
    public static boolean teslastaff;
    public static boolean classicMode;
    public static boolean hardmode;
    public static boolean ezmode;

    public static boolean GTppLogDisabler;
    public static boolean tooltips = true;
    public static boolean sharedItemStackTooltip = true;
    public static boolean[] enabledPatches;

    public static byte maxTierRoss;

    public static boolean disableBoltedBlocksCasing = false;
    public static boolean disableReboltedBlocksCasing = false;

    public static int pollutionHeatedWaterPumpSecond = 5;
    public static int basePollutionMBFSecond = 400;

    public static Set<String> voidMinerBlacklist = Collections.unmodifiableSet(new HashSet<>());

    public static boolean disablePistonInEIC = false;

    private static final int[][] METAFORTIERS_ENERGY = {
        {100, 101, 102, 105},
        {1110, 1115, 1120, 1127},
        {1111, 12726, 1116, 1121, 1128},
        {1112, 12727, 1117, 1122, 1129},
        {12728, 1190, 1130, 12685},
        {1191, 1174, 695, 12686},
    };
    private static final int[][] METAFORTIERS_BUFFER = {
        {5133, 5123},
        {161, 171, 181, 191},
        {162, 172, 182, 192},
        {163, 173, 183, 193},
        {164, 174, 184, 194},
        {165, 175, 185, 195},
    };
    private static final int[][] METAFORTIERS_CABLE = {
        {5133, 5123},
        {1210, 1230, 1250, 1270, 1290},
        {1310, 1330, 1350, 1370, 1390},
        {1410, 1430, 1450, 1470, 1490},
        {1510, 1530, 1550, 1570, 1590},
        {1650, 1670, 1690},
    };
    private static final int[][] METAFORTIERS_MACHINE = {
        {103, 104, 106, 107, 109, 110, 112, 113, 115, 116, 118, 119},
        {
            201, 211, 221, 231, 241, 251, 261, 271, 281, 291, 301, 311, 321, 331, 341, 351, 361, 371, 381, 391, 401,
            411, 421, 431, 441, 451, 461, 471, 481, 491, 501, 511, 521, 531, 541, 551, 561, 571, 581, 591, 601, 611,
            621, 631, 641, 651, 661, 671
        },
        {
            202, 212, 222, 232, 242, 252, 262, 272, 282, 292, 302, 312, 322, 332, 342, 352, 362, 372, 382, 392, 402,
            412, 422, 432, 442, 452, 462, 472, 482, 492, 502, 512, 522, 532, 542, 552, 562, 572, 582, 592, 602, 612,
            622, 632, 642, 652, 662, 672
        },
        {
            203, 213, 223, 233, 243, 253, 263, 273, 283, 293, 303, 313, 323, 333, 343, 353, 363, 373, 383, 393, 403,
            413, 423, 433, 443, 453, 463, 473, 483, 493, 503, 513, 523, 533, 543, 553, 563, 573, 583, 593, 603, 613,
            623, 633, 643, 653, 663, 673
        },
        {
            204, 214, 224, 234, 244, 254, 264, 274, 284, 294, 304, 314, 324, 334, 344, 354, 364, 374, 384, 394, 404,
            414, 424, 434, 444, 454, 464, 474, 484, 494, 504, 514, 524, 534, 544, 554, 564, 574, 584, 594, 604, 614,
            624, 634, 644, 654, 664, 674
        },
        {
            205, 215, 225, 235, 245, 255, 265, 275, 285, 295, 305, 315, 325, 335, 345, 355, 365, 375, 385, 395, 405,
            415, 425, 435, 445, 455, 465, 475, 485, 495, 505, 515, 525, 535, 545, 555, 565, 575, 585, 595, 605, 615,
            625, 635, 645, 655, 665, 675
        },
    };
    private static int[][][] defaultMetasForTiers = {
        METAFORTIERS_ENERGY, METAFORTIERS_BUFFER, METAFORTIERS_CABLE, METAFORTIERS_MACHINE
    };
    private static final String[] VOLTAGE_NAMES = new String[] {
        "High Pressure Steam",
        "Low Voltage",
        "Medium Voltage",
        "High Voltage",
        "Extreme Voltage",
        "Insane Voltage",
        "Ludicrous Voltage",
        "ZPM Voltage",
        "Ultimate Voltage",
        "Ultimate High Voltage",
        "Ultimate Extreme Voltage",
        "Ultimate Insane Voltage",
        "Ultimate Mega Voltage",
        "Ultimate Extended Mega Voltage",
        "Overpowered Voltage",
        "Maximum Voltage"
    };
    private static final String[] names = {"Generators", "Buffers", "Cables", "Machines"};

    public ConfigHandler(Configuration C) {
        ConfigHandler.c = C;
        ConfigHandler.classicMode = ConfigHandler.c
                .get(
                        "System",
                        "Enable Classic Mode",
                        false,
                        "Enables the classic Mode (all recipes in normal machines are doable in MV")
                .getBoolean(false);
        ConfigHandler.creativeScannerID = ConfigHandler.c
                .get("System", "Creative Debug Scanner", 0, "ID for the Creative Debug Scanner Block")
                .getInt(0);

        ConfigHandler.tooltips = ConfigHandler.c
                .get("System", "BartWorksToolTips", true, "If you wish to enable extra tooltips")
                .getBoolean(true);
        ConfigHandler.sharedItemStackTooltip = ConfigHandler.c
                .get(
                        "System",
                        "BartWorksSharedItemStackToolTips",
                        true,
                        "If you wish to enable \"Shared Item Stack\" tooltips")
                .getBoolean(true);

        ConfigHandler.IDOffset = ConfigHandler.c
                .get(
                        "System",
                        "ID Offset",
                        12600,
                        "ID Offset for this mod. This Mod uses " + ConfigHandler.IDU
                                + " IDs. DO NOT CHANGE IF YOU DONT KNOW WHAT THIS IS")
                .getInt(12600);
        ConfigHandler.ezmode = ConfigHandler.c
                .get(
                        "System",
                        "Mode switch",
                        false,
                        "If GTNH is Loaded, this will enable easy recipes, if not, it will enable harder recipes.")
                .getBoolean(false);
        ConfigHandler.teslastaff = ConfigHandler.c
                .get(
                        "System",
                        "Enable Teslastaff",
                        false,
                        "Enables the Teslastaff, an Item used to destroy Electric Armors")
                .getBoolean(false);
        ConfigHandler.newStuff = !ConfigHandler.c
                .get(
                        "System",
                        "Disable non-original-GT-stuff",
                        false,
                        "This switch disables my new content, that is not part of the GT2 compat")
                .getBoolean(false);
        ConfigHandler.BioLab = !ConfigHandler.c
                .get(
                        "System",
                        "Disable BioLab",
                        false,
                        "This switch disables the BioLab, BioVat etc. If you use GT5.08 or equivalent, this needs to be turned off!")
                .getBoolean(false);
        ConfigHandler.cutoffTier = ConfigHandler.c
                .get(
                        "System",
                        "Tier to nerf circuits",
                        5,
                        "This switch sets the lowest unnerfed Circuit Recipe Tier. -1 to disable it completely.",
                        -1,
                        VOLTAGE_NAMES.length)
                .getInt(5);
        ConfigHandler.cutoffTier = (ConfigHandler.cutoffTier == -1 ? VOLTAGE_NAMES.length : ConfigHandler.cutoffTier);
        ConfigHandler.disableExtraGassesForEBF = ConfigHandler.c
                .get(
                        "System",
                        "Disable Extra Gases for EBF",
                        false,
                        "This switch disables extra gas recipes for the EBF, i.e. Xenon instead of Nitrogen")
                .getBoolean(false);
        ConfigHandler.disableBoltedBlocksCasing = ConfigHandler.c
                .get("System", "Disable Bolted Casings", false, "This switch disable the generation of bolted casings")
                .getBoolean(false);
        ConfigHandler.disableReboltedBlocksCasing = ConfigHandler.c
                .get(
                        "System",
                        "Disable Rebolted Casings",
                        false,
                        "This switch disable the generation of rebolted casings")
                .getBoolean(false);

        ConfigHandler.mbWaterperSec = ConfigHandler.c
                .get("Singleblocks", "mL Water per Sec for the StirlingPump", 150)
                .getInt(150);

        ConfigHandler.energyPerCell = ConfigHandler.c
                .get(
                        "Multiblocks",
                        "energyPerLESUCell",
                        1000000,
                        "This will set Up the Energy per LESU Cell",
                        1000000,
                        Integer.MAX_VALUE)
                .getInt(1000000);
        ConfigHandler.DEHPDirectSteam = ConfigHandler.c
                .get(
                        "Multiblocks",
                        "DEHP Direct Steam Mode",
                        false,
                        "This switch enables the Direct Steam Mode of the DEHP. If enabled it will take in Waterand output steam. If disabled it will Input IC2Coolant and output hot coolant")
                .getBoolean(false);
        ConfigHandler.megaMachinesMax = ConfigHandler.c
                .get(
                        "Multiblocks",
                        "Mega Machines Maximum Recipes per Operation",
                        256,
                        "This changes the Maximum Recipes per Operation to the specified Valure")
                .getInt(256);
        ConfigHandler.bioVatMaxParallelBonus = ConfigHandler.c
                .get(
                        "Multiblocks",
                        "BioVat Maximum Bonus on Recipes",
                        1000,
                        "This are the maximum parallel Operations the BioVat can do, when the output is half full.")
                .getInt(1000);
        ConfigHandler.voidMinerBlacklist = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(ConfigHandler.c
                .get(
                        "Multiblocks",
                        "Void Miner Blacklist",
                        new String[0],
                        "This is a blacklist for the Void Miner, blacklisted ores will not enter the drop prize pool. Please fill in the Unique Identifier of Ore and connect Damage with a colon, For example: gregtech:gt.blockores:32")
                .getStringList())));
        ConfigHandler.disablePistonInEIC = ConfigHandler.c
                .get(
                        "Multiblocks",
                        "Disable Electric Implosion Compressor piston",
                        false,
                        "This switch completely disables piston animation in Electric Implosion Compressor multiblock")
                .getBoolean(false);

        ConfigHandler.pollutionHeatedWaterPumpSecond = ConfigHandler.c
                .get(
                        "Pollution",
                        "Pollution produced per second by the water pump",
                        ConfigHandler.pollutionHeatedWaterPumpSecond,
                        "How much should the Simple Stirling Water Pump produce pollution per second")
                .getInt(ConfigHandler.pollutionHeatedWaterPumpSecond);
        ConfigHandler.basePollutionMBFSecond = ConfigHandler.c
                .get(
                        "Pollution",
                        "Pollution produced per tick by the MBF per ingot",
                        ConfigHandler.basePollutionMBFSecond,
                        "How much should the MBF produce pollution per tick per ingot. Then it'll be multiplied by the amount of ingots done in parallel")
                .getInt(ConfigHandler.basePollutionMBFSecond);

        if (ConfigHandler.IDOffset == 0) {
            ConfigHandler.IDOffset = 12600;
            ConfigHandler.c
                    .get(
                            "System",
                            "ID Offset",
                            12600,
                            "ID Offset for this mod. This Mod uses " + ConfigHandler.IDU
                                    + " IDs. DO NOT CHANGE IF YOU DONT KNOW WHAT THIS IS")
                    .set(12600);
        }

        ConfigHandler.GTppLogDisabler = ConfigHandler.c
                .get("System", "Disable GT++ Logging", false, "Enables or Disables GT++ Logging.")
                .getBoolean(false);
        API_ConfigValues.debugLog = ConfigHandler.c
                .get("System", "Enable Debug Log", false, "Enables or Disables the debug log.")
                .getBoolean(false);

        for (int i = 0; i < BWCoreTransformer.CLASSESBEEINGTRANSFORMED.length; i++)
            BWCoreTransformer.shouldTransform[i] = ConfigHandler.c
                    .get(
                            "ASM fixes",
                            BWCoreTransformer.DESCRIPTIONFORCONFIG[i] + " in class: "
                                    + BWCoreTransformer.CLASSESBEEINGTRANSFORMED[i],
                            true)
                    .getBoolean(true);

        ConfigHandler.enabledPatches = new boolean[BWCoreTransformer.shouldTransform.length];
        ConfigHandler.enabledPatches =
                Arrays.copyOf(BWCoreTransformer.shouldTransform, BWCoreTransformer.shouldTransform.length);
        ConfigHandler.ross128BID = ConfigHandler.c
                .get("CrossMod Interactions", "DimID - Ross128b", -64, "The Dim ID for Ross128b")
                .getInt(-64);
        ConfigHandler.ross128BAID = ConfigHandler.c
                .get("CrossMod Interactions", "DimID - Ross128ba", -63, "The Dim ID for Ross128ba (Ross128b's Moon)")
                .getInt(-63);
        ConfigHandler.ross128btier = ConfigHandler.c
                .get("CrossMod Interactions", "Rocket Tier - Ross128b", 3, "The Rocket Tier for Ross128b")
                .getInt(3);
        ConfigHandler.ross128batier = ConfigHandler.c
                .get("CrossMod Interactions", "Rocket Tier - Ross128ba", 3, "The Rocket Tier for Ross128a")
                .getInt(3);
        ConfigHandler.ross128bRuinChance = ConfigHandler.c
                .get("CrossMod Interactions", "Ruin Chance - Ross128b", 512, "Higher Values mean lesser Ruins.")
                .getInt(512);
        ConfigHandler.Ross128Enabled = ConfigHandler.c
                .get(
                        "CrossMod Interactions",
                        "Galacticraft - Activate Ross128 System",
                        true,
                        "If the Ross128 System should be activated, DO NOT CHANGE AFTER WORLD GENERATION")
                .getBoolean(true);
        ConfigHandler.landerType = ConfigHandler.c
                .get(
                        "CrossMod Interactions",
                        "LanderType",
                        3,
                        "1 = Moon Lander, 2 = Landing Balloons, 3 = Asteroid Lander")
                .getInt(3);
        ConfigHandler.disableMagicalForest = ConfigHandler.c
                .get(
                        "CrossMod Interactions",
                        "Disable Magical Forest - Ross128b",
                        false,
                        "True disables the magical Forest Biome on Ross for more performance during World generation.")
                .getBoolean(false);

        ConfigHandler.maxTierRoss = (byte) ConfigHandler.c
                .get("Ross Ruin Metas", "A_Ruin Machine Tiers", 6, "", 0, VOLTAGE_NAMES.length)
                .getInt(6);
        ConfigHandler.metasForTiers = new int[4][maxTierRoss][];

        for (int i = 0; i < 4; i++) {
            if (maxTierRoss > ConfigHandler.defaultMetasForTiers[i].length)
                ConfigHandler.defaultMetasForTiers[i] = new int[maxTierRoss][0];
            for (int j = 0; j < maxTierRoss; j++)
                ConfigHandler.metasForTiers[i][j] = ConfigHandler.c
                        .get(
                                "Ross Ruin Metas",
                                j + "_Ruin " + names[i] + " Tier " + VOLTAGE_NAMES[j],
                                ConfigHandler.defaultMetasForTiers[i][j])
                        .getIntList();
        }

        ConfigHandler.setUpComments();

        if (ConfigHandler.c.hasChanged()) ConfigHandler.c.save();
    }

    private static void setUpComments() {
        ConfigHandler.c.addCustomCategoryComment("ASM fixes", "Disable ASM fixes here.");
        ConfigHandler.c.addCustomCategoryComment("Singleblocks", "Singleblock Options can be set here.");
        ConfigHandler.c.addCustomCategoryComment("Multiblocks", "Multiblock Options can be set here.");
        ConfigHandler.c.addCustomCategoryComment("System", "Different System Settings can be set here.");
        ConfigHandler.c.addCustomCategoryComment(
                "CrossMod Interactions",
                "CrossMod Interaction Settings can be set here. For Underground Fluid settings change the Gregtech.cfg!");
        ConfigHandler.c.addCustomCategoryComment("Ross Ruin Metas", "Ruin Metas and Tiers can be set here.");
    }
}
