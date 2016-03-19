package miscutil.core.lib;

import java.util.Map;

import miscutil.core.creativetabs.AddToCreativeTab;

public class CORE {

	public static final String name = "Misc. Utils";	
	public static final String MODID = "miscutils";
	public static final String VERSION = "0.9.6";
	public static final boolean DEBUG = true;	
	public static final boolean LOAD_ALL_CONTENT = false;
	public static final int GREG_FIRST_ID = 760;
	public static Map PlayerCache;
	
	
	public static final Class<AddToCreativeTab> TAB = AddToCreativeTab.class;
	
	//GUIS
	public enum GUI_ENUM 
	{
	    ENERGYBUFFER, TOOLBUILDER, NULL, NULL1, NULL2
	}
	
	//public static final Materials2[] MiscGeneratedMaterials = new Materials2[1000];
	
}
