package gtPlusPlus.preloader.asm;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.versioning.ArtifactVersion;

import gtPlusPlus.api.objects.Logger;
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
		meta.dependencies = (List<ArtifactVersion>) CORE_Preloader.DEPENDENCIES;

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
		Logger.INFO("[GT++ ASM] Begin resource allocation for " + CORE_Preloader.MODID + " V" + CORE_Preloader.VERSION);
	}
	
	@Subscribe
	public void preInit(FMLPreInitializationEvent event) {
		Logger.INFO("[GT++ ASM] Loading " + CORE_Preloader.MODID + " V" + CORE_Preloader.VERSION);
		// Handle GT++ Config
		handleConfigFile(event);
	}

	@Subscribe
	public void postInit(FMLPostInitializationEvent evt) {
		Logger.INFO("[GT++ ASM] Finished loading GT++ Pre-Loader.");
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