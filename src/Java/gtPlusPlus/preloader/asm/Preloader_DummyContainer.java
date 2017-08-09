package gtPlusPlus.preloader.asm;

import java.io.File;
import java.util.Arrays;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.*;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.preloader.CORE_Preloader;
import net.minecraftforge.common.config.Configuration;

public class Preloader_DummyContainer extends DummyModContainer {

	public Preloader_DummyContainer() {

		super(new ModMetadata());
		ModMetadata meta = getMetadata();
		meta.modId = CORE_Preloader.MODID;
		meta.name = CORE_Preloader.NAME;
		meta.version = CORE_Preloader.VERSION;
		meta.credits = "Roll Credits ...";
		meta.authorList = Arrays.asList("Alkalus");
		meta.description = "";
		meta.url = "";
		meta.updateUrl = "";
		meta.screenshots = new String[0];
		meta.logoFile = "";
		meta.dependencies = CORE_Preloader.DEPENDENCIES;

	}
	
	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		bus.register(this);
		return true;
	}

	@Subscribe
	public void modConstruction(FMLConstructionEvent evt){

	}

	@Subscribe
	public void init(FMLInitializationEvent evt) {

	}
	
	@EventHandler
	public void load(final FMLInitializationEvent e) {
		Utils.LOG_INFO("Begin resource allocation for " + CORE_Preloader.MODID + " V" + CORE_Preloader.VERSION);
	}
	
	@Subscribe
	public void preInit(FMLPreInitializationEvent event) {
		Utils.LOG_INFO("Loading " + CORE_Preloader.MODID + " V" + CORE_Preloader.VERSION);
		// Handle GT++ Config
		handleConfigFile(event);
	}

	@Subscribe
	public void postInit(FMLPostInitializationEvent evt) {
		Utils.LOG_INFO("Finished loading GT++ Pre-Loader.");
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