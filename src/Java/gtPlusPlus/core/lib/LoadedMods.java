package gtPlusPlus.core.lib;

import cpw.mods.fml.common.Loader;
import gtPlusPlus.core.lib.CORE.configSwitches;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechTextures;
import gtPlusPlus.xmod.gregtech.recipes.GregtechRecipeAdder;

public class LoadedMods {

	// Initialize Variables
	public static boolean	Gregtech				= false;
	public static boolean	EnderIO					= false;
	public static boolean	Big_Reactors			= false;
	public static boolean	IndustrialCraft2		= false;
	public static boolean	Simply_Jetpacks			= false;
	public static boolean	RFTools					= false;
	public static boolean	Thaumcraft				= false;
	public static boolean	Baubles					= false;
	public static boolean	Extra_Utils				= false;
	public static boolean	PneumaticCraft			= false;
	public static boolean	MorePlanets				= false;
	public static boolean	ForbiddenMagic			= false;
	public static boolean	CompactWindmills		= false;
	public static boolean	Railcraft				= false;
	public static boolean	ImmersiveEngineering	= false;
	public static boolean	Growthcraft				= false;
	public static boolean	CoFHCore				= false;
	public static boolean	Forestry				= false;
	public static boolean	MagicBees				= false;
	public static boolean	ExtraBees				= false;
	public static boolean	Psychedelicraft			= false;
	public static boolean	MiscUtils				= true;		// Dummy For
																// MetaData
																// Lookups in MT
																// Wrapper
	public static boolean	ThermalFoundation		= false;
	public static boolean	IHL						= false;
	public static boolean	OpenComputers			= false;	// OpenComputers
	public static boolean	Computronics			= false;	// computronics

	private static int		totalMods;

	@SuppressWarnings("deprecation")
	public static void checkLoaded() {
		Utils.LOG_INFO("Looking for optional mod prereqs.");
		if (Loader.isModLoaded("gregtech") == true) {
			LoadedMods.Gregtech = true;
			Utils.LOG_INFO("Components enabled for: Gregtech");
			if (LoadedMods.Gregtech) {
				try {
					CORE.sRecipeAdder = CORE.RA = new GregtechRecipeAdder();
					Utils.LOG_INFO("Created Gregtech recipe handler.");
					GregtechTextures.BlockIcons.VOID.name();
					GregtechTextures.ItemIcons.VOID.name();
					Utils.LOG_INFO("Created Gregtech texture handler.");
				}
				catch (final NullPointerException e) {
					Utils.LOG_INFO("Could NOT create a Gregtech recipe handler.");
				}
			}

			LoadedMods.totalMods++;
		}
		if (Loader.isModLoaded("EnderIO") == true && !configSwitches.disableEnderIOIntegration) {
			LoadedMods.EnderIO = true;
			Utils.LOG_INFO("Components enabled for: EnderIO");
			LoadedMods.totalMods++;
		}
		if (Loader.isModLoaded("BigReactors") == true) {
			LoadedMods.Big_Reactors = true;
			Utils.LOG_INFO("Components enabled for: Big Reactors");
			LoadedMods.totalMods++;
		}
		if (Loader.isModLoaded("IC2") == true) {
			LoadedMods.IndustrialCraft2 = true;
			Utils.LOG_INFO("Components enabled for: IndustrialCraft2");
			LoadedMods.totalMods++;
		}
		if (Loader.isModLoaded("simplyjetpacks") == true) {
			LoadedMods.Simply_Jetpacks = true;
			Utils.LOG_INFO("Components enabled for: Simply Jetpacks");
			LoadedMods.totalMods++;
		}
		if (Loader.isModLoaded("rftools") == true) {
			LoadedMods.RFTools = true;
			Utils.LOG_INFO("Components enabled for: RFTools");
			LoadedMods.totalMods++;
		}
		if (Loader.isModLoaded("Thaumcraft") == true) {
			LoadedMods.Thaumcraft = true;
			Utils.LOG_INFO("Components enabled for: Thaumcraft");
			LoadedMods.totalMods++;
		}
		if (Loader.isModLoaded("ExtraUtilities") == true) {
			LoadedMods.Extra_Utils = true;
			Utils.LOG_INFO("Components enabled for: Extra_Utils");
			LoadedMods.totalMods++;
		}
		if (Loader.isModLoaded("PneumaticCraft") == true) {
			LoadedMods.PneumaticCraft = true;
			Utils.LOG_INFO("Components enabled for: PneumaticCraft");
			LoadedMods.totalMods++;
		}
		if (Loader.isModLoaded("MorePlanet") == true) {
			LoadedMods.MorePlanets = true;
			Utils.LOG_INFO("Components enabled for: MorePlanets");
			LoadedMods.totalMods++;
		}
		if (Loader.isModLoaded("ForbiddenMagic") == true) {
			LoadedMods.ForbiddenMagic = true;
			Utils.LOG_INFO("Components enabled for: ForbiddenMagic");
			LoadedMods.totalMods++;
		}
		if (Loader.isModLoaded("CompactWindmills") == true) {
			LoadedMods.CompactWindmills = true;
			Utils.LOG_INFO("Components enabled for: CompactWindmills");
			LoadedMods.totalMods++;
		}
		if (Loader.isModLoaded("Railcraft") == true) {
			LoadedMods.Railcraft = true;
			Utils.LOG_INFO("Components enabled for: Railcraft");
			LoadedMods.totalMods++;
		}
		if (Loader.isModLoaded("Growthcraft") == true) {
			Utils.LOG_INFO("Growthcraft Version: " + LoadedMods.getModVersion("Growthcraft"));
			if (LoadedMods.getModVersion("Growthcraft").equals("1.7.10-2.3.1")) {
				// Load Growthcraft Compat
				LoadedMods.Growthcraft = true;
				Utils.LOG_INFO("Components enabled for: Growthcraft");
				LoadedMods.totalMods++;
			}
			else {
				LoadedMods.Growthcraft = false;
				Utils.LOG_INFO("Growthcraft found, but the version was too new. I will update GC support eventually.");
			}
		}
		if (Loader.isModLoaded("CoFHCore") == true) {
			LoadedMods.CoFHCore = true;
			Utils.LOG_INFO("Components enabled for: CoFHCore");
			LoadedMods.totalMods++;
		}
		if (Loader.isModLoaded("Forestry") == true) {
			LoadedMods.Forestry = true;
			Utils.LOG_INFO("Components enabled for: Forestry");
			LoadedMods.totalMods++;
		}
		if (Loader.isModLoaded("MagicBees") == true) {
			LoadedMods.MagicBees = true;
			Utils.LOG_INFO("Components enabled for: MagicBees");
			LoadedMods.totalMods++;
		}
		if (Loader.isModLoaded("psychedelicraft") == true) {
			LoadedMods.Psychedelicraft = true;
			Utils.LOG_INFO("Components enabled for: Psychedelicraft");
			LoadedMods.totalMods++;
		}
		if (Loader.isModLoaded("ImmersiveEngineering") == true) {
			LoadedMods.ImmersiveEngineering = true;
			Utils.LOG_INFO("Components enabled for: ImmersiveEngineering");
			LoadedMods.totalMods++;
		}
		if (Loader.isModLoaded("ExtraBees") == true) {
			LoadedMods.ExtraBees = true;
			Utils.LOG_INFO("Components enabled for: ExtraBees");
			LoadedMods.totalMods++;
		}
		if (Loader.isModLoaded("ThermalFoundation") == false) {
			LoadedMods.ThermalFoundation = false;
			Utils.LOG_INFO(
					"Components enabled for: ThermalFoundation - This feature will disable itself if you add TF.");
			LoadedMods.totalMods++;
		}
		else if (Loader.isModLoaded("ThermalFoundation") == true) {
			LoadedMods.ThermalFoundation = true;
			Utils.LOG_INFO(
					"Components disabled for: ThermalFoundation - This feature will enable itself if you remove TF.");
			// totalMods++;
		}
		if (Loader.isModLoaded("ihl") == true) {
			LoadedMods.IHL = true;
			Utils.LOG_INFO("Components enabled for: IHL");
			LoadedMods.totalMods++;
		}
		if (Loader.isModLoaded("Baubles") == true) {
			LoadedMods.Baubles = true;
			Utils.LOG_INFO("Components enabled for: Baubles");
			LoadedMods.totalMods++;
		}
		if (Loader.isModLoaded("OpenComputers") == true) {
			LoadedMods.OpenComputers = true;
			Utils.LOG_INFO("Components enabled for: OpenComputers");
			LoadedMods.totalMods++;
		}
		if (Loader.isModLoaded("computronics") == true) {
			LoadedMods.Computronics = true;
			Utils.LOG_INFO("Components enabled for: Computronics");
			LoadedMods.totalMods++;
		}

		Utils.LOG_INFO("Content found for " + LoadedMods.totalMods + " mods");

	}

	public static String getModVersion(final String modName) {
		final String ver = cpw.mods.fml.common.FMLCommonHandler.instance().findContainerFor(modName).getVersion();
		return ver;
	}

}
