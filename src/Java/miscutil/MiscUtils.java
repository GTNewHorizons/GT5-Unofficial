package miscutil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import miscutil.core.commands.CommandMath;
import miscutil.core.common.CommonProxy;
import miscutil.core.creativetabs.AddToCreativeTab;
import miscutil.core.lib.LoadedMods;
import miscutil.core.lib.Strings;
import miscutil.core.util.Utils;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;

@Mod(modid=Strings.MODID, name="Misc. Utils", version=Strings.VERSION, dependencies="required-after:gregtech")
public class MiscUtils
implements ActionListener
{ 
	
	@Mod.Instance(Strings.MODID)
	public static MiscUtils instance;

	@SidedProxy(clientSide="miscutil.core.proxy.ClientProxy", serverSide="miscutil.core.proxy.ServerProxy")
	public static CommonProxy proxy;


	//Pre-Init
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		LoadedMods.checkLoaded();
		Utils.LOG_INFO("Doing some house cleaning.");
		AddToCreativeTab.initialiseTabs();
		//CraftingManager.mainRegistry();
		proxy.registerTileEntities();
		proxy.registerRenderThings();
		proxy.preInit(event);
		FMLInterModComms.sendMessage("Waila", "register", "miscutil.core.waila.WailaCompat.load");
	}

	//Init
	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.init(event);
		proxy.registerOreDict();
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
		proxy.registerNetworkStuff();
	}

	//Post-Init
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		Utils.LOG_INFO("Tidying things up.");
		proxy.postInit(event);
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new CommandMath());

	}

	@Mod.EventHandler
	public void serverStopping(FMLServerStoppingEvent event)
	{


	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

}
