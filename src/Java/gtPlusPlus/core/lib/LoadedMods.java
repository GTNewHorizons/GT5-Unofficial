package gtPlusPlus.core.lib;

import gtPlusPlus.core.lib.CORE.configSwitches;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechTextures;
import gtPlusPlus.xmod.gregtech.recipes.GregtechRecipeAdder;
import cpw.mods.fml.common.Loader;

public class LoadedMods {

	
	//Initialize Variables
	public static boolean Gregtech = false;
	public static boolean EnderIO = false;
	public static boolean Big_Reactors = false;
	public static boolean IndustrialCraft2 = false;
	public static boolean Simply_Jetpacks = false;
	public static boolean RFTools = false;
	public static boolean Thaumcraft = false;
	public static boolean Baubles = false;
	public static boolean Extra_Utils = false;
	public static boolean PneumaticCraft = false;
	public static boolean MorePlanets = false;
	public static boolean ForbiddenMagic = false;
	public static boolean CompactWindmills = false;
	public static boolean Railcraft = false;
	public static boolean ImmersiveEngineering = false;
	public static boolean Growthcraft = false;
	public static boolean CoFHCore = false;
	public static boolean Forestry = false;
	public static boolean MagicBees = false;
	public static boolean ExtraBees = false;
	public static boolean Psychedelicraft = false;
	public static boolean MiscUtils = true; //Dummy For MetaData Lookups in MT Wrapper
	public static boolean ThermalFoundation = false;
	public static boolean IHL = false;
	

	
	private static int totalMods;
	@SuppressWarnings("deprecation")
	public static void checkLoaded(){
		Utils.LOG_INFO("Looking for optional mod prereqs.");
		if (Loader.isModLoaded("gregtech") == true ){
			Gregtech = true;
			Utils.LOG_INFO("Components enabled for: Gregtech");
			if (Gregtech){
				try {
					CORE.sRecipeAdder = CORE.RA = new GregtechRecipeAdder();
					Utils.LOG_INFO("Created Gregtech recipe handler.");
					GregtechTextures.BlockIcons.VOID.name();
					GregtechTextures.ItemIcons.VOID.name();
					Utils.LOG_INFO("Created Gregtech texture handler.");
				} catch (NullPointerException e){
					Utils.LOG_INFO("Could NOT create a Gregtech recipe handler.");
				}
			}
			
			totalMods++;
		}
		if (Loader.isModLoaded("EnderIO") == true && !configSwitches.disableEnderIOIntegration){
			EnderIO = true;
			Utils.LOG_INFO("Components enabled for: EnderIO");
			totalMods++;
		}
		if (Loader.isModLoaded("BigReactors") == true){
			Big_Reactors = true;
			Utils.LOG_INFO("Components enabled for: Big Reactors");
			totalMods++;
		}
		if (Loader.isModLoaded("IC2") == true){
			IndustrialCraft2 = true;
			Utils.LOG_INFO("Components enabled for: IndustrialCraft2");
			totalMods++;
		}
		if (Loader.isModLoaded("simplyjetpacks") == true){
			Simply_Jetpacks = true;
			Utils.LOG_INFO("Components enabled for: Simply Jetpacks");
			totalMods++;
		}
		if (Loader.isModLoaded("rftools") == true){
			RFTools = true;
			Utils.LOG_INFO("Components enabled for: RFTools");
			totalMods++;
		}
		if (Loader.isModLoaded("Thaumcraft") == true){
			Thaumcraft = true;
			Utils.LOG_INFO("Components enabled for: Thaumcraft");
			totalMods++;
		}
		if (Loader.isModLoaded("ExtraUtilities") == true){
			Extra_Utils = true;
			Utils.LOG_INFO("Components enabled for: Extra_Utils");
			totalMods++;
		}
		if (Loader.isModLoaded("PneumaticCraft") == true){
			PneumaticCraft = true;
			Utils.LOG_INFO("Components enabled for: PneumaticCraft");
			totalMods++;
		}
		if (Loader.isModLoaded("MorePlanet") == true){
			MorePlanets  = true;
			Utils.LOG_INFO("Components enabled for: MorePlanets");
			totalMods++;
		}
		if (Loader.isModLoaded("ForbiddenMagic") == true){
			ForbiddenMagic  = true;
			Utils.LOG_INFO("Components enabled for: ForbiddenMagic");
			totalMods++;
		}
		if (Loader.isModLoaded("CompactWindmills") == true){
			CompactWindmills  = true;
			Utils.LOG_INFO("Components enabled for: CompactWindmills");
			totalMods++;
		}
		if (Loader.isModLoaded("Railcraft") == true){
			Railcraft  = true;
			Utils.LOG_INFO("Components enabled for: Railcraft");
			totalMods++;
		}
		if (Loader.isModLoaded("Growthcraft") == true){
			Utils.LOG_INFO("Growthcraft Version: "+getModVersion("Growthcraft"));
			if (getModVersion("Growthcraft").equals("1.7.10-2.3.1")){
				//Load Growthcraft Compat
				Growthcraft  = true;
				Utils.LOG_INFO("Components enabled for: Growthcraft");
				totalMods++;
			}
			else {
				Growthcraft = false;
				Utils.LOG_INFO("Growthcraft found, but the version was too new. I will update GC support eventually.");
			}
		}
		if (Loader.isModLoaded("CoFHCore") == true){
			CoFHCore  = true;
			Utils.LOG_INFO("Components enabled for: CoFHCore");
			totalMods++;
		}
		if (Loader.isModLoaded("Forestry") == true){
			Forestry  = true;
			Utils.LOG_INFO("Components enabled for: Forestry");
			totalMods++;
		}
		if (Loader.isModLoaded("MagicBees") == true){
			MagicBees  = true;
			Utils.LOG_INFO("Components enabled for: MagicBees");
			totalMods++;
		}
		if (Loader.isModLoaded("psychedelicraft") == true){
			Psychedelicraft  = true;
			Utils.LOG_INFO("Components enabled for: Psychedelicraft");
			totalMods++;
		}
		if (Loader.isModLoaded("ImmersiveEngineering") == true){
			ImmersiveEngineering  = true;
			Utils.LOG_INFO("Components enabled for: ImmersiveEngineering");
			totalMods++;
		}
		if (Loader.isModLoaded("ExtraBees") == true){
			ExtraBees  = true;
			Utils.LOG_INFO("Components enabled for: ExtraBees");
			totalMods++;
		}
		if (Loader.isModLoaded("ThermalFoundation") == false){
			ThermalFoundation  = false;
			Utils.LOG_INFO("Components enabled for: ThermalFoundation - This feature will disable itself if you add TF.");
			totalMods++;
		}
		else if (Loader.isModLoaded("ThermalFoundation") == true){
			ThermalFoundation  = true;
			Utils.LOG_INFO("Components disabled for: ThermalFoundation - This feature will enable itself if you remove TF.");
			//totalMods++;
		}
		if (Loader.isModLoaded("ihl") == true){
			IHL  = true;
			Utils.LOG_INFO("Components enabled for: IHL");
			totalMods++;
		} 
		if (Loader.isModLoaded("Baubles") == true){
			Baubles  = true;
			Utils.LOG_INFO("Components enabled for: Baubles");
			totalMods++;
		}
	
		Utils.LOG_INFO("Content found for "+totalMods+" mods");
		
	}
	
	public static String getModVersion(String modName){
		final String ver = cpw.mods.fml.common.FMLCommonHandler.instance().findContainerFor(modName).getVersion();
		return ver;
	}
	
}
