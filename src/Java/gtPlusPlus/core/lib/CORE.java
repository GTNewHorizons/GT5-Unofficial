package gtPlusPlus.core.lib;

import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.gregtech.recipehandlers.GregtechRecipe;
import gtPlusPlus.core.util.networking.NetworkUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
import gtPlusPlus.xmod.gregtech.api.interfaces.internal.IGregtech_RecipeAdder;
import gtPlusPlus.xmod.gregtech.common.Meta_GT_Proxy;
import gtPlusPlus.xmod.gregtech.common.tileentities.automation.GT_MetaTileEntity_TesseractGenerator;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.config.Configuration;

public class CORE {

	public static final String name = "GT++";	
	public static final String MODID = "miscutils";
	public static final String VERSION = "1.4.9-release";
	public static final String MASTER_VERSION = NetworkUtils.getContentFromURL("https://raw.githubusercontent.com/draknyte1/GTplusplus/master/Recommended.txt").toLowerCase();
	public static boolean isModUpToDate = Utils.isModUpToDate();
	public static boolean DEBUG = false;	
	public static final boolean LOAD_ALL_CONTENT = false;
	public static final int GREG_FIRST_ID = 760;
	public static Map PlayerCache;
	public static final String[] VOLTAGES = {"ULV","LV","MV","HV","EV","IV","LuV","ZPM","UV","MAX"};
	public static final boolean MAIN_GREGTECH_5U_EXPERIMENTAL_FORK = Meta_GT_Proxy.areWeUsingGregtech5uExperimental();
	public static IGregtech_RecipeAdder RA;
	@Deprecated
	public static IGregtech_RecipeAdder sRecipeAdder;
	public static GregtechRecipe GT_Recipe = new GregtechRecipe();
	
	public static Configuration Config;	
	public static final String GT_Tooltip = "Added by: " + EnumChatFormatting.DARK_GREEN+"Alkalus "+EnumChatFormatting.GRAY+"- "+EnumChatFormatting.RED+"[GT++]";
	public static final String GT_Tooltip_Radioactive = EnumChatFormatting.GRAY+"Warning: "+EnumChatFormatting.GREEN+"Radioactive! "+EnumChatFormatting.GOLD+" Avoid direct handling without hazmat protection.";
	public static final String noItem = "";
	
	/**
	 * A List containing all the Materials, which are somehow in use by GT and therefor receive a specific Set of Items.
	 */
	public static final GT_Materials[] sMU_GeneratedMaterials = new GT_Materials[1000];
	
	//Tesseract map
	public static final Map<Integer, GT_MetaTileEntity_TesseractGenerator> sTesseractGenerators = new HashMap<Integer, GT_MetaTileEntity_TesseractGenerator>();

	//GUIS
	public enum GUI_ENUM 
	{
		ENERGYBUFFER, TOOLBUILDER, NULL, NULL1, NULL2
	}

	/**
	 * File Paths and Resource Paths
	 */
	public static final String
	TEX_DIR = "textures/", 
	TEX_DIR_GUI = TEX_DIR + "gui/", 
	TEX_DIR_ITEM = TEX_DIR + "items/", 
	TEX_DIR_BLOCK = TEX_DIR + "blocks/",
	TEX_DIR_ENTITY = TEX_DIR + "entity/", 
	TEX_DIR_ASPECTS = TEX_DIR + "aspects/", 
	TEX_DIR_FLUIDS = TEX_DIR_BLOCK + "fluids/",
	RES_PATH = MODID + ":" + TEX_DIR, 
	RES_PATH_GUI = MODID + ":" + TEX_DIR_GUI, 
	RES_PATH_ITEM = MODID + ":" + TEX_DIR_ITEM, 
	RES_PATH_BLOCK = MODID + ":" + TEX_DIR_BLOCK, 
	RES_PATH_ENTITY = MODID + ":" + TEX_DIR_ENTITY,
	RES_PATH_ASPECTS = MODID + ":" + TEX_DIR_ASPECTS,
	RES_PATH_FLUIDS = MODID + ":" + TEX_DIR_FLUIDS;


	//public static final Materials2[] MiscGeneratedMaterials = new Materials2[1000];

	public static class configSwitches {

		//Debug
		public static boolean disableEnderIOIntegration = false;
		
		//Machine Related
		public static boolean enableAlternativeBatteryAlloy = false;
		public static boolean enableThaumcraftShardUnification = false;
		public static boolean disableIC2Recipes = false;	
		public static boolean enableAlternativeDivisionSigilRecipe = false;
		
		//Feature Related
		public static boolean enableCustomAlvearyBlocks = false;
		
		//Single Block Machines
		public static boolean enableMachine_SolarGenerators = false;
		public static boolean enableMachine_Dehydrators = true;
		public static boolean enableMachine_SteamConverter = true;
		public static boolean enableMachine_FluidTanks = true;
		public static boolean enableMachine_RocketEngines = true;
		public static boolean enableMachine_GeothermalEngines = true;
		public static boolean enableCustom_Pipes = true;
		public static boolean enableCustom_Cables = true;
		
		//Multiblocks
		public static boolean enabledMultiblock_AlloyBlastSmelter = true;
		public static boolean enabledMultiblock_IndustrialCentrifuge = true;
		public static boolean enabledMultiblock_IndustrialCokeOven = true;
		public static boolean enabledMultiblock_IndustrialElectrolyzer = true;
		public static boolean enabledMultiblock_IndustrialMacerationStack = true;
		public static boolean enabledMultiblock_IndustrialPlatePress = true;
		public static boolean enabledMultiblock_IndustrialWireMill = true;
		public static boolean enabledMultiblock_IronBlastFurnace = true;
		public static boolean enabledMultiblock_MatterFabricator = true;
		public static boolean enabledMultiblock_MultiTank = true;
		public static boolean enabledMultiblock_PowerSubstation = true;
		
	}
	
}
