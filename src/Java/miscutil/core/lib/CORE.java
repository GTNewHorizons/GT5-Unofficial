package miscutil.core.lib;

import java.util.Map;

import miscutil.core.creative.AddToCreativeTab;

public class CORE {

	public static final String name = "Misc. Utils";	
	public static final String MODID = "miscutils";
	public static final String VERSION = "1.0.1";
	public static final boolean DEBUG = false;	
	public static final boolean LOAD_ALL_CONTENT = false;
	public static final int GREG_FIRST_ID = 760;
	public static Map PlayerCache;
	public static final String[] VOLTAGES = {"ULV","LV","MV","HV","EV","IV","LuV","ZPM","UV","MAX"};
	
	
	
	public static final Class<AddToCreativeTab> TAB = AddToCreativeTab.class;
	
	//GUIS
	public enum GUI_ENUM 
	{
	    ENERGYBUFFER, TOOLBUILDER, NULL, NULL1, NULL2
	}
	
	//public static final Materials2[] MiscGeneratedMaterials = new Materials2[1000];
	
}
