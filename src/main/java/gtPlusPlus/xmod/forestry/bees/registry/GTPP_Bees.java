package gtPlusPlus.xmod.forestry.bees.registry;

import static gregtech.api.enums.GT_Values.MOD_ID_FR;

import java.util.HashMap;

import cpw.mods.fml.common.Loader;
import gregtech.GT_Mod;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.forestry.bees.handler.*;
import gtPlusPlus.xmod.forestry.bees.items.output.*;

public class GTPP_Bees {
	
	public final static byte FORESTRY = 0;
	public final static byte EXTRABEES = 1;
	public final static byte GENDUSTRY = 2;
	public final static byte MAGICBEES = 3;
    public final static byte GREGTECH = 4;

    public static GTPP_Propolis propolis;
    public static GTPP_Pollen pollen;
    public static GTPP_Drop drop;
    public static GTPP_Comb combs;
    
    
    public static HashMap<String, Material> sMaterialMappings = new HashMap<String, Material>();
	public static HashMap<Integer, GTPP_PropolisType> sPropolisMappings = new HashMap<Integer, GTPP_PropolisType>();
	public static HashMap<Integer, GTPP_PollenType> sPollenMappings = new HashMap<Integer, GTPP_PollenType>();
	public static HashMap<Integer, GTPP_DropType> sDropMappings = new HashMap<Integer, GTPP_DropType>();
	public static HashMap<Integer, GTPP_CombType> sCombMappings = new HashMap<Integer, GTPP_CombType>();

    public GTPP_Bees() {
        if (Loader.isModLoaded(MOD_ID_FR) && GT_Mod.gregtechproxy.mGTBees) {
        	
        	if (!ReflectionUtils.doesClassExist("gregtech.loaders.misc.GT_BeeDefinition")) {
        		CORE.crash("Missing gregtech.loaders.misc.GT_BeeDefinition.");
        	}
        	else {
        		Logger.BEES("Loading GT++ Bees!");
        	}

    		Logger.BEES("Creating required items.");
            propolis = new GTPP_Propolis();
            pollen = new GTPP_Pollen();
            drop = new GTPP_Drop();
            combs = new GTPP_Comb();

    		Logger.BEES("Loading types.");
            initTypes();

    		Logger.BEES("Adding recipes.");
            GTPP_Drop.initDropsRecipes();
            GTPP_Propolis.initPropolisRecipes();
            GTPP_Comb.initCombsRecipes();

    		Logger.BEES("Initialising bees.");
            GTPP_BeeDefinition.initBees();
            
    		Logger.BEES("Done!");
        }
    }
    
    private static void initTypes() {
    	ReflectionUtils.loadClass("gtPlusPlus.xmod.forestry.bees.registry.GTPP_BeeDefinition");
    	ReflectionUtils.loadClass("gtPlusPlus.xmod.forestry.bees.handler.GTPP_CombType");
    	ReflectionUtils.loadClass("gtPlusPlus.xmod.forestry.bees.handler.GTPP_DropType");
    	ReflectionUtils.loadClass("gtPlusPlus.xmod.forestry.bees.handler.GTPP_PollenType");
    	ReflectionUtils.loadClass("gtPlusPlus.xmod.forestry.bees.handler.GTPP_PropolisType");
    }
}
