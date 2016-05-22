package miscutil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import miscutil.core.commands.CommandMath;
import miscutil.core.common.CommonProxy;
import miscutil.core.common.compat.COMPAT_HANDLER;
import miscutil.core.creative.AddToCreativeTab;
import miscutil.core.handler.events.PickaxeBlockBreakEventHandler;
import miscutil.core.lib.CORE;
import miscutil.core.lib.LoadedMods;
import miscutil.core.util.PlayerCache;
import miscutil.core.util.Utils;
import miscutil.core.util.debug.DEBUG_ScreenOverlay;
import miscutil.core.util.uptime.Uptime;
import miscutil.gregtech.common.GregtechRecipeAdder;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

@Mod(modid=CORE.MODID, name="Misc. Utils", version=CORE.VERSION, dependencies="required-after:gregtech;")
public class MiscUtils
implements ActionListener
{ 
	
	@Mod.Instance(CORE.MODID)
	public static MiscUtils instance;
	public static Uptime Uptime = new Uptime();

	@SidedProxy(clientSide="miscutil.core.proxy.ClientProxy", serverSide="miscutil.core.proxy.ServerProxy")
	public static CommonProxy proxy;


	//Pre-Init
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		LoadedMods.checkLoaded();
		Utils.LOG_INFO("Doing some house cleaning.");
		
		if (LoadedMods.Gregtech){
			try {
				CORE.sRecipeAdder = CORE.RA = new GregtechRecipeAdder();
			} catch (NullPointerException e){
				
			}
		}
		
		AddToCreativeTab.initialiseTabs();
		proxy.registerTileEntities();
		proxy.registerRenderThings();
		proxy.preInit(event);
		Uptime.preInit(); //Integration of Uptime.
		//FMLInterModComms.sendMessage("Waila", "register", "miscutil.core.waila.WailaCompat.load");
	}

	//Init
	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.init(event);
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new PickaxeBlockBreakEventHandler());
		if (CORE.DEBUG){
			MinecraftForge.EVENT_BUS.register(new DEBUG_ScreenOverlay());	
		}
		FMLCommonHandler.instance().bus().register(this);
		proxy.registerNetworkStuff();
		Uptime.init(); //Integration of Uptime.
	}

	//Post-Init
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		Utils.LOG_INFO("Cleaning up, doing postInit.");
		COMPAT_HANDLER.ServerStartedEvent();
		PlayerCache.initCache();
		proxy.postInit(event);
		
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new CommandMath());
		Uptime.serverStarting(); //Integration of Uptime.

	}

	@Mod.EventHandler
	public void serverStopping(FMLServerStoppingEvent event)
	{
		Uptime.serverStopping(); //Integration of Uptime.

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		Uptime.actionPerformed(arg0); //Integration of Uptime.

	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
	{
		Uptime.onPlayerLogin(event);
	}

	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event)
	{
		Uptime.onPlayerLogout(event);
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onPlayerDeath(LivingDeathEvent lde)
	{
		Uptime.onPlayerDeath(lde);
	}

	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event)
	{
		Uptime.onPlayerRespawn(event);
	}

}
