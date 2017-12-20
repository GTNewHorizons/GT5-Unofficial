package gtPlusPlus.core.lib;

import static gtPlusPlus.core.lib.CORE.BRC;
import static gtPlusPlus.core.lib.CORE.GTNH;

import cpw.mods.fml.common.Loader;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE.ConfigSwitches;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechTextures;
import gtPlusPlus.xmod.gregtech.recipes.GregtechRecipeAdder;

public class LoadedMods {


	//Initialize Variables
	public static boolean Gregtech = false;
	public static boolean PlayerAPI = false;
	public static boolean BuildCraft = false;
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
	public static boolean OpenComputers = false; //OpenComputers
	public static boolean Computronics = false; //computronics
	public static boolean DreamCraft = false; //GT: New Horizons
	public static boolean BeyondRealityCore = false; //Beyond Reality - Probably Classic
	public static boolean BiomesOPlenty = false;
	public static boolean PamsHarvestcraft = false;
	public static boolean GalacticraftCore = false;
	public static boolean Mekanism = false;
	public static boolean RedTech = false; //RedMage's Mod
	public static boolean TecTech = false; //Technus' Mod



	private static int totalMods;
	@SuppressWarnings("deprecation")
	public static void checkLoaded(){
		Logger.INFO("Looking for optional mod prereqs.");
		if (Loader.isModLoaded("gregtech") == true ){
			Gregtech = true;
			Logger.INFO("Components enabled for: Gregtech");
			if (Gregtech){
				try {
					CORE.sRecipeAdder = CORE.RA = new GregtechRecipeAdder();
					Logger.INFO("Created Gregtech recipe handler.");
					GregtechTextures.BlockIcons.VOID.name();
					GregtechTextures.ItemIcons.VOID.name();
					Logger.INFO("Created Gregtech texture handler.");
				} catch (final NullPointerException e){
					Logger.INFO("Could NOT create a Gregtech recipe handler.");
				}
			}

			totalMods++;
		}
		if (Loader.isModLoaded("dreamcraft") == true){
			DreamCraft = true;
			GTNH = true;
			Logger.INFO("Components enabled for: DreamCraft");
			Logger.INFO("Components enabled for: GT: New Horizons");
			totalMods++;
		}
		if (Loader.isModLoaded("beyondrealitycore") == true){
			BeyondRealityCore = true;
			BRC = true;
			Logger.INFO("Components enabled for: Beyond Reality");
			totalMods++;
		}
		if (Loader.isModLoaded("PlayerAPI") == true){
			PlayerAPI = true;
			Logger.INFO("Components enabled for: PlayerAPI");
			totalMods++;
		}
		if (Loader.isModLoaded("BuildCraft") == true){
			BuildCraft = true;
			Logger.INFO("Components enabled for: BuildCraft");
			totalMods++;
		}
		if ((Loader.isModLoaded("EnderIO") == true) && !ConfigSwitches.disableEnderIOIntegration){
			EnderIO = true;
			Logger.INFO("Components enabled for: EnderIO");
			totalMods++;
		}
		if (Loader.isModLoaded("BigReactors") == true){
			Big_Reactors = true;
			Logger.INFO("Components enabled for: Big Reactors");
			totalMods++;
		}
		if (Loader.isModLoaded("IC2") == true){
			IndustrialCraft2 = true;
			Logger.INFO("Components enabled for: IndustrialCraft2");
			totalMods++;
		}
		if (Loader.isModLoaded("simplyjetpacks") == true){
			Simply_Jetpacks = true;
			Logger.INFO("Components enabled for: Simply Jetpacks");
			totalMods++;
		}
		if (Loader.isModLoaded("rftools") == true){
			RFTools = true;
			Logger.INFO("Components enabled for: RFTools");
			totalMods++;
		}
		if (Loader.isModLoaded("Thaumcraft") == true){
			Thaumcraft = true;
			Logger.INFO("Components enabled for: Thaumcraft");
			totalMods++;
		}
		if (Loader.isModLoaded("BiomesOPlenty") == true){
			BiomesOPlenty = true;
			Logger.INFO("Components enabled for: BiomesOPlenty");
			totalMods++;
		}
		if (Loader.isModLoaded("ExtraUtilities") == true){
			Extra_Utils = true;
			Logger.INFO("Components enabled for: Extra_Utils");
			totalMods++;
		}
		if (Loader.isModLoaded("harvestcraft") == true){
			PamsHarvestcraft = true;
			Logger.INFO("Components enabled for: PamsHarvestcraft");
			totalMods++;
		}
		if (Loader.isModLoaded("PneumaticCraft") == true){
			PneumaticCraft = true;
			Logger.INFO("Components enabled for: PneumaticCraft");
			totalMods++;
		}
		if (Loader.isModLoaded("MorePlanet") == true){
			MorePlanets  = true;
			Logger.INFO("Components enabled for: MorePlanets");
			totalMods++;
		}
		if (Loader.isModLoaded("ForbiddenMagic") == true){
			ForbiddenMagic  = true;
			Logger.INFO("Components enabled for: ForbiddenMagic");
			totalMods++;
		}
		if (Loader.isModLoaded("CompactWindmills") == true){
			CompactWindmills  = true;
			Logger.INFO("Components enabled for: CompactWindmills");
			totalMods++;
		}
		if (Loader.isModLoaded("Railcraft") == true){
			Railcraft  = true;
			Logger.INFO("Components enabled for: Railcraft");
			totalMods++;
		}
		if (Loader.isModLoaded("Mekanism") == true){
			Mekanism  = true;
			Logger.INFO("Components enabled for: Mekanism - This feature is not configurable and balances Mekanism to suit GT.");
			totalMods++;
		}
		if (Loader.isModLoaded("Growthcraft") == true){
			Logger.INFO("Growthcraft Version: "+getModVersion("Growthcraft"));
			if (getModVersion("Growthcraft").equals("1.7.10-2.3.1")){
				//Load Growthcraft Compat
				Growthcraft  = true;
				Logger.INFO("Components enabled for: Growthcraft");
				totalMods++;
			}
			else {
				Growthcraft = false;
				Logger.INFO("Growthcraft found, but the version was too new. I will update GC support eventually.");
			}
		}
		if (Loader.isModLoaded("CoFHCore") == true){
			CoFHCore  = true;
			Logger.INFO("Components enabled for: CoFHCore");
			totalMods++;
		}
		if (Loader.isModLoaded("Forestry") == true){
			Forestry  = true;
			Logger.INFO("Components enabled for: Forestry");
			totalMods++;
		}
		if (Loader.isModLoaded("MagicBees") == true){
			MagicBees  = true;
			Logger.INFO("Components enabled for: MagicBees");
			totalMods++;
		}
		if (Loader.isModLoaded("psychedelicraft") == true){
			Psychedelicraft  = true;
			Logger.INFO("Components enabled for: Psychedelicraft");
			totalMods++;
		}
		if (Loader.isModLoaded("ImmersiveEngineering") == true){
			ImmersiveEngineering  = true;
			Logger.INFO("Components enabled for: ImmersiveEngineering");
			totalMods++;
		}
		if (Loader.isModLoaded("ExtraBees") == true){
			ExtraBees  = true;
			Logger.INFO("Components enabled for: ExtraBees");
			totalMods++;
		}
		if (Loader.isModLoaded("ThermalFoundation") == false){
			ThermalFoundation  = false;
			Logger.INFO("Components enabled for: ThermalFoundation - This feature will disable itself if you add TF.");
			totalMods++;
		}
		else if (Loader.isModLoaded("ThermalFoundation") == true){
			ThermalFoundation  = true;
			Logger.INFO("Components disabled for: ThermalFoundation - This feature will enable itself if you remove TF.");
			//totalMods++;
		}
		if (Loader.isModLoaded("ihl") == true){
			IHL  = true;
			Logger.INFO("Components enabled for: IHL");
			totalMods++;
		}
		if (Loader.isModLoaded("Baubles") == true){
			Baubles  = true;
			Logger.INFO("Components enabled for: Baubles");
			totalMods++;
		}
		if (Loader.isModLoaded("GalacticraftCore") == true){
			GalacticraftCore  = true;
			Logger.INFO("Components enabled for: Galacticraft Core");
			totalMods++;
		}
		if (Loader.isModLoaded("OpenComputers") == true){
			OpenComputers  = true;
			Logger.INFO("Components enabled for: OpenComputers");
			totalMods++;
		}
		if (Loader.isModLoaded("computronics") == true){
			Computronics  = true;
			Logger.INFO("Components disabled for: Computronics - This feature will enable itself if you remove Computronics.");
			totalMods++;
		}
		else {
			Logger.INFO("Components enabled for: Computronics - This feature will disable itself if you add Computronics.");
		}
		if (Loader.isModLoaded("GTRedtech") == true){
			RedTech  = true;
			Logger.INFO("Components enabled for: GTRedtech");
			totalMods++;
		}
		if (Loader.isModLoaded("tectech") == true){
			TecTech  = true;
			Logger.INFO("Components enabled for: TecTech");
			totalMods++;
		}		

		Logger.INFO("Content found for "+totalMods+" mods");

	}

	public static String getModVersion(final String modName){
		final String ver = cpw.mods.fml.common.FMLCommonHandler.instance().findContainerFor(modName).getVersion();
		return ver;
	}

}
