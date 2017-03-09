package com.github.technus.tectech;

import com.github.technus.tectech.auxiliary.Reference;
import com.github.technus.tectech.auxiliary.TecTechConfig;
import com.github.technus.tectech.blocks.QuantumGlass;
import com.github.technus.tectech.proxy.CommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import eu.usrv.yamcore.auxiliary.IngameErrorLog;
import eu.usrv.yamcore.auxiliary.LogHelper;

import java.util.Random;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, dependencies = "required-after:Forge@[10.13.4.1614,);"
		+ "required-after:YAMCore@[0.5.73,);")
public class TecTech {

	@SidedProxy(clientSide = Reference.CLIENTSIDE, serverSide = Reference.SERVERSIDE)
	public static CommonProxy proxy;

	@Instance(Reference.MODID)
	public static TecTech instance;

	//public static CreativeTabsManager TabManager = null;
	public static IngameErrorLog Module_AdminErrorLogs = null;
	public static GT_CustomLoader GTCustomLoader = null;
	public static TecTechConfig ModConfig;
	public static Random Rnd = null;
	public static LogHelper Logger = new LogHelper(Reference.MODID);

	public static void AddLoginError(String pMessage) {
		if (Module_AdminErrorLogs != null)
			Module_AdminErrorLogs.AddErrorLogOnAdminJoin(pMessage);
	}

	@EventHandler
	public void PreLoad(FMLPreInitializationEvent PreEvent) {
		Logger.setDebugOutput(true);

		Rnd = new Random(System.currentTimeMillis());

		ModConfig = new TecTechConfig(PreEvent.getModConfigurationDirectory(), Reference.COLLECTIONNAME,
				Reference.MODID);
		if (!ModConfig.LoadConfig())
			Logger.error(
					String.format("%s could not load its config file. Things are going to be weird!", Reference.MODID));

		if (ModConfig.ModAdminErrorLogs_Enabled) {
			Logger.debug("Module_AdminErrorLogs is enabled");
			Module_AdminErrorLogs = new IngameErrorLog();
		}
	}

	@EventHandler
	public void load(FMLInitializationEvent event) {
		GameRegistry.registerBlock(QuantumGlass.INSTANCE,QuantumGlass.INSTANCE.getUnlocalizedName());
		proxy.registerRenderInfo();
	}

	@EventHandler
	public void PostLoad(FMLPostInitializationEvent PostEvent) {
		GTCustomLoader = new GT_CustomLoader();
		GTCustomLoader.run();
	}

	/**
	 * Do some stuff once the server starts
	 * 
	 * @param pEvent
	 */
	@EventHandler
	public void serverLoad(FMLServerStartingEvent pEvent) {
	}

}
