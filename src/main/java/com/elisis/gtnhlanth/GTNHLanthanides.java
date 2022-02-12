package com.elisis.gtnhlanth;

import java.util.Arrays;
import java.util.logging.Logger;

import com.elisis.gtnhlanth.common.CommonProxy;
import com.elisis.gtnhlanth.common.register.BotWerkstoffMaterialPool;
import com.elisis.gtnhlanth.common.register.ItemList;
import com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool;
import com.elisis.gtnhlanth.loader.BotRecipes;
import com.elisis.gtnhlanth.loader.RecipeLoader;
import com.github.bartimaeusnek.bartworks.API.WerkstoffAdderRegistry;
import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import gregtech.api.util.GT_Log;

@Mod(modid = Tags.MODID, version = Tags.VERSION, name = Tags.MODNAME, 
    dependencies = "required-after:IC2; " + "required-after:gregtech; "
            + "required-after:bartworks; " 
            + "required-after:GoodGenerator; "
            + "before:miscutils; "
            + "required-after:dreamcraft; "
        )
public class GTNHLanthanides {
    
    public static Logger LOG = Logger.getLogger("GTNH:Lanthanides");
    
    @Mod.Instance
    public static GTNHLanthanides instance;
    
    @SidedProxy(clientSide = "com.elisis.gtnhlanth.client.ClientProxy",serverSide = "com.elisis.gtnhlanth.common.CommonProxy")
    public static CommonProxy proxy;
    
    @EventHandler
    public static void preInit(FMLPreInitializationEvent e) {
        WerkstoffAdderRegistry.addWerkstoffAdder(new WerkstoffMaterialPool());
        WerkstoffAdderRegistry.addWerkstoffAdder(new BotWerkstoffMaterialPool());
        ItemList.register();
        proxy.preInit(e);
    }
    
    @EventHandler
    public static void init(FMLInitializationEvent e) {
        proxy.init(e);
        WerkstoffMaterialPool.runInit();
    }
    
    @EventHandler
    public static void postInit(FMLPostInitializationEvent e) {
        RecipeLoader.loadGeneral();
        RecipeLoader.loadLanthanideRecipes();
        RecipeLoader.addRandomChemCrafting();
        BotRecipes.addGTRecipe();
        BotRecipes.addFuels();
        //RecipeLoader.loadZylonRecipes();
        proxy.postInit(e);
        //GT_Log.out.print(FluidRegistry.getFluid("Sodium Tungstate").getName());
        
        GT_Log.out.print(Arrays.toString(Werkstoff.werkstoffNameHashMap.keySet().toArray()));
        GT_Log.out.print(Arrays.toString(Werkstoff.werkstoffHashMap.keySet().toArray()));
        
        
        
        
    }
    
    @EventHandler
    public static void onModLoadingComplete(FMLLoadCompleteEvent e) {
    	GT_Log.out.print("AAAAAAAAAAAAAA");
    	//
    	GT_Log.out.print("We are done loading");
    	BotRecipes.removeRecipes();
    	
    	
    	
    }
    
    // This is horrifying and I'm sorry
    @EventHandler
    public static void onServerAboutToStart(FMLServerAboutToStartEvent e) {
    	//RecipeLoader.removeCeriumSources();
    }
    
    @EventHandler
    public static void onServerStart(FMLServerStartedEvent e) {
    	RecipeLoader.removeCeriumSources();
    }
    

}
