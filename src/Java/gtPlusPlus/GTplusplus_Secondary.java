package gtPlusPlus;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import cofh.mod.ChildMod;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.CustomProperty;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import gtPlusPlus.core.lib.CORE;

@ChildMod(parent = CORE.MODID, mod = @Mod(modid = "Gregtech++",
name = "GT++",
version = CORE.VERSION,
dependencies = "after:Miscutils;after:Gregtech",
customProperties = @CustomProperty(k = "cofhversion", v = "true")))
public class GTplusplus_Secondary {

	private static final String name = "Gregtech++";

	@EventHandler
	public void load(final FMLInitializationEvent e) {
		try {
			initMod();
		} catch (final Throwable $) {
			final ModContainer This = FMLCommonHandler.instance().findContainerFor(this);
			LogManager.getLogger(This.getModId()).log(Level.ERROR, "There was a problem loading " + This.getName(), $);
		}
	}

	private static void initMod() {

	}

	@EventHandler
	public static void postInit(final FMLPostInitializationEvent e) {
		
	}

}
