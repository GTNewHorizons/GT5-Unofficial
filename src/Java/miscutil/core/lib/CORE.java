package miscutil.core.lib;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import miscutil.core.creative.AddToCreativeTab;
import miscutil.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
import miscutil.xmod.gregtech.api.interfaces.internal.IGregtech_RecipeAdder;
import miscutil.xmod.gregtech.common.Meta_GT_Proxy;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CORE {

	public static final String name = "GT++";	
	public static final String MODID = "miscutils";
	public static final String VERSION = "1.4.3.13.1-release";
	public static boolean DEBUG = false;	
	public static final boolean LOAD_ALL_CONTENT = false;
	public static final int GREG_FIRST_ID = 760;
	public static Map PlayerCache;
	public static final String[] VOLTAGES = {"ULV","LV","MV","HV","EV","IV","LuV","ZPM","UV","MAX"};
	public static final boolean MAIN_GREGTECH_5U_EXPERIMENTAL_FORK = Meta_GT_Proxy.areWeUsingGregtech5uExperimental();
	public static IGregtech_RecipeAdder RA;
	@Deprecated
	public static IGregtech_RecipeAdder sRecipeAdder;

	@SideOnly(Side.CLIENT)
	public static IIconRegister GT_BlockIcons, GT_ItemIcons;	
	public static List<Runnable> GT_BlockIconload = new ArrayList<Runnable>();
	public static final Class<AddToCreativeTab> TAB = AddToCreativeTab.class;
	public static Configuration Config;	
	public static final String GT_Tooltip = "Added by: " + EnumChatFormatting.DARK_GREEN+"Alkalus "+EnumChatFormatting.GRAY+"- "+EnumChatFormatting.RED+"[GT++]";
	public static final String noItem = "";
	
	/**
	 * A List containing all the Materials, which are somehow in use by GT and therefor receive a specific Set of Items.
	 */
	public static final GT_Materials[] sMU_GeneratedMaterials = new GT_Materials[1000];

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
		public static boolean disableStaballoyBlastFurnaceRecipe = false;
		public static boolean disableCentrifugeFormation = false;
		
		//Machine Related
		public static boolean enableSolarGenerators = false;
		public static boolean enableAlternativeBatteryAlloy = false;
		public static boolean enableThaumcraftShardUnification = false;
		public static boolean disableIC2Recipes = false;	
		public static boolean enableAlternativeDivisionSigilRecipe = false;
		
		//Feature Related
		public static boolean enableCustomAlvearyBlocks = false;
		
	}
	
}
