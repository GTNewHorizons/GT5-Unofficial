/*
 * Copyright (c) 2018-2019 bartimaeusnek
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


import com.github.bartimaeusnek.ASM.BWCoreTransformer;
import gregtech.api.enums.GT_Values;
import net.minecraftforge.common.config.Configuration;

import java.util.Arrays;

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

    public static long energyPerCell = 1000000L;

    public static boolean newStuff = true;
    public static boolean BioLab = true;
    public static boolean Ross128Enabled = true;

    public static boolean disableExtraGassesForEBF;
    public static boolean disableMagicalForest;
    public static boolean DEHPDirectSteam;
    public static boolean teslastaff;
    public static boolean classicMode;
    public static boolean experimentalThreadedLoader;
    public static boolean GTNH;
    public static boolean ezmode;

    //One-Side-Only
    public static boolean debugLog;
    public static boolean GTppLogDisabler;
    public static boolean tooltips = true;
    public static boolean[] enabledPatches;


    public ConfigHandler(Configuration C) {
        ConfigHandler.c = C;
        ConfigHandler.classicMode= ConfigHandler.c.get("System", "Enable Classic Mode", false, "Enables the classic Mode (all recipes in normal machines are doable in MV").getBoolean(false);
        ConfigHandler.creativeScannerID = ConfigHandler.c.get("System", "Creative Debug Scanner", 0, "ID for the Creative Debug Scanner Block").getInt(0);
        ConfigHandler.tooltips = ConfigHandler.c.get("System", "BartWorksToolTips", true, "If you wish to enable extra tooltips").getBoolean(true);
        ConfigHandler.IDOffset = ConfigHandler.c.get("System", "ID Offset", 12600, "ID Offset for this mod. This Mod uses " + ConfigHandler.IDU + " IDs. DO NOT CHANGE IF YOU DONT KNOW WHAT THIS IS").getInt(12600);
        ConfigHandler.ezmode = ConfigHandler.c.get("System", "Mode switch", false, "If GTNH is Loaded, this will enable easy recipes, if not, it will enable harder recipes.").getBoolean(false);
        ConfigHandler.teslastaff = ConfigHandler.c.get("System", "Enable Teslastaff", false, "Enables the Teslastaff, an Item used to destroy Electric Armors").getBoolean(false);
        ConfigHandler.newStuff = !ConfigHandler.c.get("System", "Disable non-original-GT-stuff", false, "This switch disables my new content, that is not part of the GT2 compat").getBoolean(false);
        ConfigHandler.BioLab = !ConfigHandler.c.get("System", "Disable BioLab", false, "This switch disables the BioLab, BioVat etc. If you use GT5.08 or equivalent, this needs to be turned off!").getBoolean(false);
        ConfigHandler.cutoffTier = ConfigHandler.c.get("System", "Tier to nerf circuits", 5, "This switch sets the lowest unnerfed Circuit Recipe Tier. -1 to disable it completely.").getInt(5);
        ConfigHandler.cutoffTier = (ConfigHandler.cutoffTier == -1 ? GT_Values.VN.length : ConfigHandler.cutoffTier);
        ConfigHandler.disableExtraGassesForEBF = ConfigHandler.c.get("System", "Disable Extra Gases for EBF", false, "This switch disables extra gas recipes for the EBF, i.e. Xenon instead of Nitrogen").getBoolean(false);

        ConfigHandler.mbWaterperSec = ConfigHandler.c.get("Singleblocks", "mL Water per Sec for the StirlingPump", 150).getInt(150);

        ConfigHandler.energyPerCell = ConfigHandler.c.get("Multiblocks", "energyPerLESUCell", 1000000, "This will set Up the Energy per LESU Cell", 1000000, Integer.MAX_VALUE).getInt(1000000);
        ConfigHandler.DEHPDirectSteam = ConfigHandler.c.get("Multiblocks", "DEHP Direct Steam Mode", false, "This switch enables the Direct Steam Mode of the DEHP. If enabled it will take in Waterand output steam. If disabled it will Input IC2Coolant and output hot coolant").getBoolean(false);
        ConfigHandler.megaMachinesMax = ConfigHandler.c.get("Multiblocks", "Mega Machines Maximum Recipes per Operation", 256, "This changes the Maximum Recipes per Operation to the specified Valure").getInt(256);
        ConfigHandler.bioVatMaxParallelBonus = ConfigHandler.c.get("Multiblocks","BioVat Maximum Bonus on Recipes", 1000,"This are the maximum parallel Operations the BioVat can do, when the output is half full.").getInt(1000);
        if (ConfigHandler.IDOffset == 0) {
            ConfigHandler.IDOffset = 12600;
            ConfigHandler.c.get("System", "ID Offset", 12600, "ID Offset for this mod. This Mod uses " + ConfigHandler.IDU + " IDs. DO NOT CHANGE IF YOU DONT KNOW WHAT THIS IS").set(12600);
        }
        ConfigHandler.GTppLogDisabler = ConfigHandler.c.get("System", "Disable GT++ Logging", false, "Enables or Disables GT++ Logging.").getBoolean(false);
        ConfigHandler.debugLog = ConfigHandler.c.get("System", "Enable Debug Log", false, "Enables or Disables the debug log.").getBoolean(false);
        ConfigHandler.experimentalThreadedLoader = ConfigHandler.c.get("System", "Enable Experimental Threaded Material Loader", false, "Enables or Disables the Experimental Threaded Material Loader.").getBoolean(false);

        for (int i = 0; i < BWCoreTransformer.CLASSESBEEINGTRANSFORMED.length; i++) {
            BWCoreTransformer.shouldTransform[i] = ConfigHandler.c.get("ASM fixes", BWCoreTransformer.DESCRIPTIONFORCONFIG[i] + " in class: " + BWCoreTransformer.CLASSESBEEINGTRANSFORMED[i], true).getBoolean(true);
        }
        ConfigHandler.enabledPatches = new boolean[BWCoreTransformer.shouldTransform.length];
        ConfigHandler.enabledPatches = Arrays.copyOf(BWCoreTransformer.shouldTransform,BWCoreTransformer.shouldTransform.length);
        ConfigHandler.ross128BID = ConfigHandler.c.get("CrossMod Interactions", "DimID - Ross128b", -64, "The Dim ID for Ross128b").getInt(-64);
        ConfigHandler.ross128BAID = ConfigHandler.c.get("CrossMod Interactions", "DimID - Ross128ba", -63, "The Dim ID for Ross128ba (Ross128b's Moon)").getInt(-63);
        ConfigHandler.ross128btier = ConfigHandler.c.get("CrossMod Interactions", "Rocket Tier - Ross128b", 3, "The Rocket Tier for Ross128b").getInt(3);
        ConfigHandler.ross128batier = ConfigHandler.c.get("CrossMod Interactions", "Rocket Tier - Ross128ba", 3, "The Rocket Tier for Ross128a").getInt(3);
        ConfigHandler.ross128bRuinChance = ConfigHandler.c.get("CrossMod Interactions", "Ruin Chance - Ross128b", 512, "Higher Values mean lesser Ruins.").getInt(512);
        ConfigHandler.Ross128Enabled = ConfigHandler.c.get("CrossMod Interactions", "Galacticraft - Activate Ross128 System", true, "If the Ross128 System should be activated, DO NOT CHANGE AFTER WORLD GENERATION").getBoolean(true);
        ConfigHandler.landerType = ConfigHandler.c.get("CrossMod Interactions", "LanderType", 3, "1 = Moon Lander, 2 = Landing Balloons, 3 = Asteroid Lander").getInt(3);
        ConfigHandler.disableMagicalForest = ConfigHandler.c.get("CrossMod Interactions", "Disable Magical Forest - Ross128b", false, "True disables the magical Forest Biome on Ross for more performance during World generation.").getBoolean(false);

        ConfigHandler.setUpComments();

        if (ConfigHandler.c.hasChanged())
            ConfigHandler.c.save();
    }

    private static void setUpComments() {
        ConfigHandler.c.addCustomCategoryComment("ASM fixes", "Disable ASM fixes here.");
        ConfigHandler.c.addCustomCategoryComment("Singleblocks", "Singleblock Options can be set here.");
        ConfigHandler.c.addCustomCategoryComment("Multiblocks", "Multliblock Options can be set here.");
        ConfigHandler.c.addCustomCategoryComment("System", "Different System Settings can be set here.");
        ConfigHandler.c.addCustomCategoryComment("CrossMod Interactions", "CrossMod Interaction Settings can be set here. For Underground Fluid settings change the Gregtech.cfg!");
    }

}
