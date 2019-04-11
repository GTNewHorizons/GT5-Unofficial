/*
 * Copyright (c) 2019 bartimaeusnek
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


import com.github.bartimaeusnek.bartworks.MainMod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import gregtech.api.enums.GT_Values;
import net.minecraftforge.common.config.Configuration;

import javax.annotation.Nonnull;
import java.io.File;

public class ConfigHandler {
    private static final int IDU = GT_Values.VN.length * 8 + 1;
    public static int IDOffset = 12600;
    public static boolean teslastaff = false;
    public static long energyPerCell = 1000000L;
    public static boolean newStuff = true;
    public static boolean BioLab = true;
    public static Configuration c;
    public static boolean DEHPDirectSteam = false;
    public static int megaMachinesMax = 256;
    public static int mbWaterperSec = 150;
    private static boolean ezmode = false;
    public static boolean debugLog = false;

    public ConfigHandler(@Nonnull FMLPreInitializationEvent e) {
        c = new Configuration(new File(e.getModConfigurationDirectory().toString() + "/" + MainMod.MOD_ID + ".cfg"));

        IDOffset = c.get("System", "ID Offset", 12600, "ID Offset for this mod. This Mod uses " + IDU + " IDs. DO NOT CHANGE IF YOU DONT KNOW WHAT THIS IS").getInt(12600);
        energyPerCell = c.get("Multiblocks", "energyPerLESUCell", 1000000, "This will set Up the Energy per LESU Cell", 1000000, Integer.MAX_VALUE).getInt(1000000);
        ezmode = c.get("System", "Mode switch", false, "If GTNH is Loaded, this will enable easy recipes, if not, it will enable harder recipes.").getBoolean(false);
        MainMod.GTNH = ezmode ? !MainMod.GTNH : MainMod.GTNH;
        teslastaff = c.get("System", "Enable Teslastaff", false, "Enables the Teslastaff, an Item used to destroy Electric Armors").getBoolean(false);
        newStuff = !c.get("System", "Disable non-original-GT-stuff", false, "This switch disables my new content, that is not part of the GT2 compat").getBoolean(false);
        BioLab = !c.get("System", "Disable BioLab", false, "This switch disables the BioLab, BioVat etc. If you use GT5.08 or equivalent, this needs to be turned off!").getBoolean(false);
        DEHPDirectSteam = c.get("Multiblocks", "DEHP Direct Steam Mode", false, "This switch enables the Direct Steam Mode of the DEHP. If enabled it will take in Waterand output steam. If disabled it will Input IC2Coolant and output hot coolant").getBoolean(false);
        megaMachinesMax = c.get("Multiblocks", "Mega Machines Maximum Recipes per Operation", 256, "This changes the Maximum Recipes per Operation to the specified Valure").getInt(256);
        mbWaterperSec = c.get("Singleblocks", "mL Water per Sec for the StirlingPump", 150).getInt(150);
        if (ConfigHandler.IDOffset == 0) {
            ConfigHandler.IDOffset = 12600;
            c.get("System", "ID Offset", 12600, "ID Offset for this mod. This Mod uses " + IDU + " IDs. DO NOT CHANGE IF YOU DONT KNOW WHAT THIS IS").set(12600);
        }
        debugLog=c.get("System","Enable Debug Log",false,"Enables or Disables the debug log.").getBoolean(false);
        if (c.hasChanged())
            c.save();

    }

    public static void setUpComments(){
        c.addCustomCategoryComment("ASM fixes","Disable ASM fixes here.");
        c.addCustomCategoryComment("Multiblocks","Multliblock Options can be set here.");
        c.addCustomCategoryComment("Singleblocks","Singleblock Options can be set here.");
        c.addCustomCategoryComment("System","Different System Settings can be set here.");
        c.addCustomCategoryComment("CrossMod Interactions","CrossMod Interaction Settings can be set here. For Underground Fluid settings change the Gregtech.cfg!");
        c.save();
    }

}
