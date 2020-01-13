package gtPlusPlus.core.lib;

import static gtPlusPlus.core.lib.CORE.*;

import java.util.HashMap;

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
	public static boolean IndustrialCraft2Classic = false;
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
	public static boolean OpenBlocks = false;
	public static boolean Computronics = false; //computronics
	public static boolean DreamCraft = false; //GT: New Horizons
	public static boolean BeyondRealityCore = false; //Beyond Reality - Probably Classic
	public static boolean BiomesOPlenty = false;
	public static boolean PamsHarvestcraft = false;
	public static boolean GalacticraftCore = false;
	public static boolean Mekanism = false;
	public static boolean RedTech = false; //RedMage's Mod
	public static boolean TecTech = false; //Technus' Mod
	public static boolean KekzTech = false; //KekzDealers' Mod
	public static boolean TiCon = false;
	public static boolean StevesCarts = false;
	public static boolean Witchery = false;
	public static boolean Waila = false;
	public static boolean CropsPlusPlus = false; //Barts Crop Mod
	public static boolean Reliquary = false;



	private static int totalMods;
	@SuppressWarnings("deprecation")
	public static void checkLoaded(){
		Logger.INFO("Looking for optional mod prereqs.");
		if (isModLoaded("gregtech") ){
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
		if (isModLoaded("dreamcraft")){
			DreamCraft = true;
			GTNH = true;
			Logger.INFO("Components enabled for: DreamCraft");
			Logger.INFO("Components enabled for: GT: New Horizons");
			totalMods++;
		}
		if (isModLoaded("beyondrealitycore")){
			BeyondRealityCore = true;
			BRC = true;
			Logger.INFO("Components enabled for: Beyond Reality");
			totalMods++;
		}
		if (isModLoaded("PlayerAPI")){
			PlayerAPI = true;
			Logger.INFO("Components enabled for: PlayerAPI");
			totalMods++;
		}
		if (isModLoaded("berriespp")) {
			CropsPlusPlus = true;
			Logger.INFO("Components enabled for: Crops++");
			totalMods++;
		}
		if (isModLoaded("xreliquary")) {
			Reliquary = true;
			Logger.INFO("Components enabled for: Reliquary");
			totalMods++;
		}
		if (isModLoaded("TConstruct")){
			TiCon = true;
			Logger.INFO("Components enabled for: Tinkers Construct");
			totalMods++;
		}
		if (isModLoaded("BuildCraft")){
			BuildCraft = true;
			Logger.INFO("Components enabled for: BuildCraft");
			totalMods++;
		}
		if ((isModLoaded("EnderIO")) && !ConfigSwitches.disableEnderIOIntegration){
			EnderIO = true;
			Logger.INFO("Components enabled for: EnderIO");
			totalMods++;
		}
		if (isModLoaded("BigReactors")){
			Big_Reactors = true;
			Logger.INFO("Components enabled for: Big Reactors");
			totalMods++;
		}
		if (isModLoaded("IC2")){
			IndustrialCraft2 = true;
			Logger.INFO("Components enabled for: IndustrialCraft2");
			totalMods++;
		}
		if (isModLoaded("IC2-Classic-Spmod")){
			IndustrialCraft2Classic = true;
			Logger.INFO("Components enabled for: IndustrialCraft2-Classic");
			totalMods++;
		}
		if (isModLoaded("simplyjetpacks")){
			Simply_Jetpacks = true;
			Logger.INFO("Components enabled for: Simply Jetpacks");
			totalMods++;
		}
		if (isModLoaded("rftools")){
			RFTools = true;
			Logger.INFO("Components enabled for: RFTools");
			totalMods++;
		}
		if (isModLoaded("StevesCarts")){
			StevesCarts = true;
			Logger.INFO("Components enabled for: StevesCarts");
			totalMods++;
		}
		if (isModLoaded("OpenBlocks")){
			OpenBlocks = true;
			Logger.INFO("Components enabled for: OpenBlocks");
			totalMods++;
		}
		if (isModLoaded("Thaumcraft")){
			Thaumcraft = true;
			Logger.INFO("Components enabled for: Thaumcraft");
			totalMods++;
		}
		if (isModLoaded("BiomesOPlenty")){
			BiomesOPlenty = true;
			Logger.INFO("Components enabled for: BiomesOPlenty");
			totalMods++;
		}
		if (isModLoaded("ExtraUtilities")){
			Extra_Utils = true;
			Logger.INFO("Components enabled for: Extra_Utils");
			totalMods++;
		}
		if (isModLoaded("harvestcraft")){
			PamsHarvestcraft = true;
			Logger.INFO("Components enabled for: PamsHarvestcraft");
			totalMods++;
		}
		if (isModLoaded("PneumaticCraft")){
			PneumaticCraft = true;
			Logger.INFO("Components enabled for: PneumaticCraft");
			totalMods++;
		}
		if (isModLoaded("MorePlanet")){
			MorePlanets  = true;
			Logger.INFO("Components enabled for: MorePlanets");
			totalMods++;
		}
		if (isModLoaded("ForbiddenMagic")){
			ForbiddenMagic  = true;
			Logger.INFO("Components enabled for: ForbiddenMagic");
			totalMods++;
		}
		if (isModLoaded("CompactWindmills")){
			CompactWindmills  = true;
			Logger.INFO("Components enabled for: CompactWindmills");
			totalMods++;
		}
		if (isModLoaded("Railcraft")){
			Railcraft  = true;
			Logger.INFO("Components enabled for: Railcraft");
			totalMods++;
		}
		if (isModLoaded("Waila")){
			Waila  = true;
			Logger.INFO("Components enabled for: WAILA");
			totalMods++;
		}
		if (isModLoaded("Mekanism")){
			Mekanism  = true;
			Logger.INFO("Components enabled for: Mekanism - This feature is not configurable and balances Mekanism to suit GT.");
			totalMods++;
		}
		if (isModLoaded("Growthcraft")){
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
		if (isModLoaded("CoFHCore")){
			CoFHCore  = true;
			Logger.INFO("Components enabled for: CoFHCore");
			totalMods++;
		}
		if (isModLoaded("Forestry")){
			Forestry  = true;
			Logger.INFO("Components enabled for: Forestry");
			totalMods++;
		}
		if (isModLoaded("MagicBees")){
			MagicBees  = true;
			Logger.INFO("Components enabled for: MagicBees");
			totalMods++;
		}
		if (isModLoaded("psychedelicraft")){
			Psychedelicraft  = true;
			Logger.INFO("Components enabled for: Psychedelicraft");
			totalMods++;
		}
		if (isModLoaded("ImmersiveEngineering")){
			ImmersiveEngineering  = true;
			Logger.INFO("Components enabled for: ImmersiveEngineering");
			totalMods++;
		}
		if (isModLoaded("ExtraBees")){
			ExtraBees  = true;
			Logger.INFO("Components enabled for: ExtraBees");
			totalMods++;
		}
		if (isModLoaded("ThermalFoundation") == false){
			ThermalFoundation  = false;
			Logger.INFO("Components enabled for: ThermalFoundation - This feature will disable itself if you add TF.");
			totalMods++;
		}
		else if (isModLoaded("ThermalFoundation")){
			ThermalFoundation  = true;
			Logger.INFO("Components disabled for: ThermalFoundation - This feature will enable itself if you remove TF.");
			//totalMods++;
		}
		if (isModLoaded("ihl")){
			IHL  = true;
			Logger.INFO("Components enabled for: IHL");
			totalMods++;
		}
		if (isModLoaded("Baubles")){
			Baubles  = true;
			Logger.INFO("Components enabled for: Baubles");
			totalMods++;
		}
		if (isModLoaded("GalacticraftCore")){
			GalacticraftCore  = true;
			Logger.INFO("Components enabled for: Galacticraft Core");
			totalMods++;
		}
		if (isModLoaded("OpenComputers")){
			OpenComputers  = true;
			Logger.INFO("Components enabled for: OpenComputers");
			totalMods++;
		}
		if (isModLoaded("computronics")){
			Computronics  = true;
			Logger.INFO("Components disabled for: Computronics - This feature will enable itself if you remove Computronics.");
			totalMods++;
		}
		else {
			Logger.INFO("Components enabled for: Computronics - This feature will disable itself if you add Computronics.");
		}
		if (isModLoaded("GTRedtech")){
			RedTech  = true;
			Logger.INFO("Components enabled for: GTRedtech");
			totalMods++;
		}
		if (isModLoaded("tectech")){
			TecTech  = true;
			Logger.INFO("Components enabled for: TecTech");
			totalMods++;
		}
		if (isModLoaded("kekztech")){
			KekzTech  = true;
			Logger.INFO("Components enabled for: KekzTech");
			totalMods++;
		}	
		if (isModLoaded("witchery")){
			Witchery  = true;
			Logger.INFO("Components enabled for: Witchery");
			totalMods++;
		}		

		Logger.INFO("Content found for "+totalMods+" mods");

	}

	public static String getModVersion(final String modName){
		final String ver = cpw.mods.fml.common.FMLCommonHandler.instance().findContainerFor(modName).getVersion();
		return ver;
	}
	
	private static final HashMap<String, Boolean> mLoadedModCache = new HashMap<String, Boolean>();

	public static boolean isModLoaded(String aModName) {
		Boolean aResult = mLoadedModCache.get(aModName);
		if (aResult == null) {
			boolean aTemp = Loader.isModLoaded(aModName);
			mLoadedModCache.put(aModName, aTemp);
			aResult = aTemp;
		}
		return aResult;
	}

}
