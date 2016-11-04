package pers.gwyog.gtneioreplugin;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.relauncher.Side;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraft.item.ItemStack;
import pers.gwyog.gtneioreplugin.event.ClientTickHandler;

@Mod(modid = GTNEIOrePlugin.MODID, name = GTNEIOrePlugin.NAME, version = GTNEIOrePlugin.VERSION, dependencies = "required-after:gregtech;required-after:NotEnoughItems")
public class GTNEIOrePlugin {
	public static final String MODID = "gtneioreplugin";
	public static final String NAME = "GT NEI Ore Plugin";
	public static final String VERSION = "1.0.0";
	
    @Mod.Instance(MODID)
    public static GTNEIOrePlugin instance;
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
    	if (event.getSide() == Side.CLIENT)
    		FMLCommonHandler.instance().bus().register(new ClientTickHandler());
    }

}
