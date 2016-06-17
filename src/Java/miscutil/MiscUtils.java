package miscutil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import miscutil.core.commands.CommandMath;
import miscutil.core.common.CommonProxy;
import miscutil.core.lib.CORE;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;

@Mod(modid=CORE.MODID, name="Misc. Utils", version=CORE.VERSION, dependencies="required-after:Forge; after:IC2; after:gregtech; after:Railcraft; after:CompactWindmills; after:ForbiddenMagic; after:MorePlanet; after:PneumaticCraft; after:ExtraUtilities; after:Thaumcraft; after:rftools; after:simplyjetpacks; after:BigReactors; after:EnderIO;")
public class MiscUtils
implements ActionListener
{ 

	@Mod.Instance(CORE.MODID)
	public static MiscUtils instance;

	@SidedProxy(clientSide="miscutil.core.proxy.ClientProxy", serverSide="miscutil.core.proxy.ServerProxy")
	public static CommonProxy proxy;


	//Pre-Init
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		proxy.registerTileEntities();
		proxy.registerRenderThings();
		proxy.preInit(event);
	}

	//Init
	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.init(event);		
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
		proxy.registerNetworkStuff();
	}

	//Post-Init
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
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

	}

}
