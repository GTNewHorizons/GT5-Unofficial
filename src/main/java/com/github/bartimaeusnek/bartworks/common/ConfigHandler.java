package com.github.bartimaeusnek.bartworks.common;


import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.common.tileentities.GT_TileEntity_LESU;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import gregtech.api.enums.GT_Values;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigHandler {
    public static int IDOffset = 12600;
    public static final int IDU=1+ GT_Values.VN.length;
    public static boolean ezmode = false;
    public static boolean teslastaff = false;
    public final Configuration c;
    public ConfigHandler(FMLPreInitializationEvent e){
        c = new Configuration(new File(e.getModConfigurationDirectory().toString()+"/"+MainMod.modID+".cfg"));

        IDOffset=c.get("System","ID Offset",12600,"ID Offset for this mod. This Mod uses "+IDU+" IDs. DO NOT CHANGE IF YOU DONT KNOW WHAT THIS IS").getInt(12600);
        GT_TileEntity_LESU.energyPerCell=c.get("Multiblocks","energyPerLESUCell",1000000,"This will set Up the Energy per LESU Cell",1000000,Integer.MAX_VALUE).getInt(1000000);
        ezmode=c.get("System","Mode switch",false,"If GTNH is Loaded, this will enable easy recipes, if not, it will enable harder recipes.").getBoolean(false);
        MainMod.GTNH = ezmode ? !MainMod.GTNH : MainMod.GTNH;
        teslastaff=c.get("System","Enable Teslastaff",false,"Enables the Teslastaff, an Item used to destroy Electric Armors").getBoolean(false);

        if (c.hasChanged())
            c.save();
    }

}
