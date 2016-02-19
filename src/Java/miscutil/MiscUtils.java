package miscutil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import miscutil.core.commands.CommandMath;
import miscutil.core.common.CommonProxy;
import miscutil.core.creativetabs.AddToCreativeTab;
import miscutil.core.handler.CraftingManager;
import miscutil.core.lib.Strings;
import miscutil.core.util.Utils;
import miscutil.gregtech.enums.MaterialsNew;
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

@Mod(modid=Strings.MODID, name="Misc. Utils", version=Strings.VERSION, dependencies="required-after:gregtech")
public class MiscUtils
implements ActionListener
{ 

	//Vars
	//EnumBuster EB = new EnumBuster(gregtech.api.enums.Materials, null);
	
	@Mod.Instance(Strings.MODID)
	public static MiscUtils instance;

	@SidedProxy(clientSide="miscutil.core.proxy.ClientProxy", serverSide="miscutil.core.proxy.ServerProxy")
	public static CommonProxy proxy;


	//Pre-Init
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		try {
			MaterialsNew.getGregMaterials();
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//java.lang.reflect.Array.get(Materials, index)
		Utils.LOG_INFO("Doing some house cleaning.");
		AddToCreativeTab.initialiseTabs();
		//TMEntity.mainRegistry();
		CraftingManager.mainRegistry();
		//TMWorld.mainRegistry();
		//TMHooks.mainRegistry();
		proxy.registerTileEntities();
		proxy.registerRenderThings();
		proxy.preInit(event);



	}

	//Init
	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		/*		Utils.LOG_INFO("Double checking floating point precision.");
		try {
			Thread.sleep(100);
			Benchmark GammeRayBurst = new Benchmark();
			GammeRayBurst.math();
		} catch (InterruptedException | ParseException | NumberFormatException | UnknownFormatConversionException | MissingFormatArgumentException e) {
			if (Strings.DEBUG){
				e.printStackTrace();
				Utils.LOG_INFO("Math went wrong somewhere.");
			}
			;
		}*/
		proxy.init(event);
		/*if (Strings.DEBUG){
			Benchmark GammeRayBurst = new Benchmark();
			String Insight = GammeRayBurst.superhash("This is Absolution");
			FMLLog.info(Insight);
			Utils.LOG_INFO("Math is ok.");
		}*/

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
		
		//while (Strings.DEBUG){
		//Thread.setDefaultUncaughtExceptionHandler(null);
		//}

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
