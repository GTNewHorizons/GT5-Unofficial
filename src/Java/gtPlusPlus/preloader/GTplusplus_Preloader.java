package gtPlusPlus.preloader;

import static gtPlusPlus.preloader.CORE_Preloader.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.*;
import gtPlusPlus.core.util.Utils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

@Mod(modid = MODID, name = NAME, version = VERSION, dependencies = "required-before:gregtech;")
public class GTplusplus_Preloader implements ActionListener {

	@EventHandler
	public void load(final FMLInitializationEvent e) {
		Utils.LOG_INFO("Begin resource allocation for " + MODID + " V" + VERSION);
	}

	// Pre-Init
	@Mod.EventHandler
	public void preInit(final FMLPreInitializationEvent event) {
		Utils.LOG_INFO("Loading " + MODID + " V" + VERSION);
		// Handle GT++ Config
		handleConfigFile(event);
		if (CORE_Preloader.enableOldGTcircuits) {
			MinecraftForge.EVENT_BUS.register(new Preloader_GT_OreDict());
		}
	}

	@EventHandler
	public static void postInit(final FMLPostInitializationEvent e) {
		Utils.LOG_INFO("Finished loading GT++ Pre-Loader.");
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
	}

	public static void handleConfigFile(final FMLPreInitializationEvent event) {
		final Configuration config = new Configuration(
				new File(event.getModConfigurationDirectory(), "GTplusplus/GTplusplus.cfg"));
		config.load();

		// Circuits
		CORE_Preloader.enableOldGTcircuits = config.getBoolean("enableOldGTcircuits", "gregtech", false,
				"Restores circuits and their recipes from Pre-5.09.28 times.");
	}

}
