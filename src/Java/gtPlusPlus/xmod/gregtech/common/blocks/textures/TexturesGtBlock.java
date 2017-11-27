package gtPlusPlus.xmod.gregtech.common.blocks.textures;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.IIconContainer;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public class TexturesGtBlock {


	//public static ITexture[] CASING_BLOCKS_GTPP = new ITexture[256];

	/*
	 * Handles Custom Textures.
	 */

	public static class CustomIcon implements IIconContainer, Runnable {
		protected IIcon mIcon;
		protected String mIconName;

		public CustomIcon(final String aIconName) {
			this.mIconName = aIconName;
			Utils.LOG_WARNING("Constructing a Custom Texture. " + this.mIconName);
			GregTech_API.sGTBlockIconload.add(this);
		}

		@Override
		public IIcon getIcon() {
			return this.mIcon;
		}

		@Override
		public IIcon getOverlayIcon() {
			return null;
		}

		@Override
		public void run() {
			this.mIcon = GregTech_API.sBlockIcons.registerIcon(CORE.MODID + ":"  + this.mIconName);
			Utils.LOG_WARNING("FIND ME _ Processing texture: "+this.getTextureFile().getResourcePath());
		}

		@Override
		public ResourceLocation getTextureFile() {
			return TextureMap.locationBlocksTexture;
		}
	}


	/*
	 * Add Some Custom Textures below.
	 * I am not sure whether or not I need to declare them as such, but better to be safe than sorry.
	 * Right?
	 */


	//PlaceHolder Texture
	private static final CustomIcon Internal_PlaceHolder = new CustomIcon("TileEntities/_PlaceHolder");
	public static final CustomIcon _PlaceHolder = Internal_PlaceHolder;


	//Controllers
	private static final CustomIcon Internal_Casing_Fusion_Simple_Front = new CustomIcon("TileEntities/MACHINE_CASING_FUSION_FRONT");
	public static final CustomIcon Casing_Fusion_Simple_Front = Internal_Casing_Fusion_Simple_Front;
	private static final CustomIcon Internal_Casing_Fusion_Simple_Front_Active = new CustomIcon("TileEntities/MACHINE_CASING_FUSION_FRONT_ACTIVE");
	public static final CustomIcon Casing_Fusion_Simple_Front_Active = Internal_Casing_Fusion_Simple_Front_Active;
	
	//Machine Casings
	//Simple
	private static final CustomIcon Internal_Casing_Machine_Simple_Top = new CustomIcon("TileEntities/machine_top");
	public static final CustomIcon Casing_Machine_Simple_Top = Internal_Casing_Machine_Simple_Top;
	private static final CustomIcon Internal_Casing_Machine_Simple_Bottom = new CustomIcon("TileEntities/machine_bottom");
	public static final CustomIcon Casing_Machine_Simple_Bottom = Internal_Casing_Machine_Simple_Bottom;
	//Advanced and Ultra
	private static final CustomIcon Internal_Casing_Machine_Advanced = new CustomIcon("TileEntities/high_adv_machine");
	public static final CustomIcon Casing_Machine_Advanced = Internal_Casing_Machine_Advanced;
	private static final CustomIcon Internal_Casing_Machine_Ultra = new CustomIcon("TileEntities/adv_machine_lesu");
	public static final CustomIcon Casing_Machine_Ultra = Internal_Casing_Machine_Ultra;
	//Dimensional - Non Overlay
	private static final CustomIcon Internal_Casing_Machine_Dimensional = new CustomIcon("TileEntities/adv_machine_dimensional");
	public static final CustomIcon Casing_Machine_Dimensional = Internal_Casing_Machine_Dimensional;
	private static final CustomIcon Internal_Casing_Machine_Dimensional_Adv = new CustomIcon("TileEntities/high_adv_machine_dimensional");
	public static final CustomIcon Casing_Machine_Dimensional_Adv = Internal_Casing_Machine_Dimensional_Adv;

	//Material Casings
	private static final CustomIcon Internal_Casing_Tantalloy61 = new CustomIcon("TileEntities/MACHINE_CASING_STABLE_TANTALLOY61");
	public static final CustomIcon Casing_Material_Tantalloy61 = Internal_Casing_Tantalloy61;
	private static final CustomIcon Internal_Casing_MaragingSteel = new CustomIcon("TileEntities/MACHINE_CASING_STABLE_MARAGINGSTEEL");
	public static final CustomIcon Casing_Material_MaragingSteel = Internal_Casing_MaragingSteel;
	private static final CustomIcon Internal_Casing_Stellite = new CustomIcon("TileEntities/MACHINE_CASING_STABLE_STELLITE");
	public static final CustomIcon Casing_Material_Stellite = Internal_Casing_Stellite;
	private static final CustomIcon Internal_Casing_Talonite = new CustomIcon("TileEntities/MACHINE_CASING_STABLE_TALONITE");
	public static final CustomIcon Casing_Material_Talonite = Internal_Casing_Talonite;
	private static final CustomIcon Internal_Casing_Tumbaga = new CustomIcon("TileEntities/MACHINE_CASING_STABLE_TUMBAGA");
	public static final CustomIcon Casing_Material_Tumbaga = Internal_Casing_Tumbaga;
	private static final CustomIcon Internal_Casing_Zeron100 = new CustomIcon("TileEntities/MACHINE_CASING_STABLE_ZERON100");
	public static final CustomIcon Casing_Material_Zeron100 = Internal_Casing_Zeron100;
	private static final CustomIcon Internal_Casing_Potin = new CustomIcon("TileEntities/MACHINE_CASING_STABLE_POTIN");
	public static final CustomIcon Casing_Material_Potin = Internal_Casing_Potin;

	private static final CustomIcon Internal_Casing_Grisium = new CustomIcon("TileEntities/MACHINE_CASING_STABLE_GRISIUM");
	public static final CustomIcon Casing_Material_Grisium = Internal_Casing_Grisium;
	private static final CustomIcon Internal_Casing_RedSteel = new CustomIcon("TileEntities/MACHINE_CASING_STABLE_RED_STEEL");
	public static final CustomIcon Casing_Material_RedSteel = Internal_Casing_RedSteel;
	private static final CustomIcon Internal_Casing_Incoloy020 = new CustomIcon("TileEntities/MACHINE_CASING_STABLE_INCOLOY_020");
	public static final CustomIcon Casing_Material_Incoloy020 = Internal_Casing_Incoloy020;
	private static final CustomIcon Internal_Casing_IncoloyDS = new CustomIcon("TileEntities/MACHINE_CASING_STABLE_INCOLOY_DS");
	public static final CustomIcon Casing_Material_IncoloyDS = Internal_Casing_IncoloyDS;
	private static final CustomIcon Internal_Casing_IncoloyMA956 = new CustomIcon("TileEntities/MACHINE_CASING_STABLE_INCOLOY_MA956");
	public static final CustomIcon Casing_Material_IncoloyMA956 = Internal_Casing_IncoloyMA956;
	private static final CustomIcon Internal_Casing_ZirconiumCarbide = new CustomIcon("TileEntities/MACHINE_CASING_STABLE_ZIRCONIUM_CARBIDE");
	public static final CustomIcon Casing_Material_ZirconiumCarbide = Internal_Casing_ZirconiumCarbide;


	private static final CustomIcon Internal_Casing_HastelloyX = new CustomIcon("TileEntities/MACHINE_CASING_STABLE_HASTELLOY_X");
	public static final CustomIcon Casing_Material_HastelloyX = Internal_Casing_HastelloyX;
	private static final CustomIcon Internal_Casing_HastelloyN = new CustomIcon("TileEntities/MACHINE_CASING_STABLE_HASTELLOY_N");
	public static final CustomIcon Casing_Material_HastelloyN = Internal_Casing_HastelloyN;
	private static final CustomIcon Internal_Casing_Fluid_IncoloyDS = new CustomIcon("TileEntities/MACHINE_CASING_FLUID_INCOLOY_DS");
	public static final CustomIcon Casing_Material_Fluid_IncoloyDS = Internal_Casing_Fluid_IncoloyDS;

	//Material Machine/Firebox Casings
	private static final CustomIcon Internal_Casing_Staballoy_Firebox = new CustomIcon("TileEntities/MACHINE_CASING_FIREBOX_STABALLOY");
	public static final CustomIcon Casing_Staballoy_Firebox = Internal_Casing_Staballoy_Firebox;

	//Misc Casings
	private static final CustomIcon Internal_Casing_Machine_Redstone_Off = new CustomIcon("TileEntities/cover_redstone_conductor");
	public static final CustomIcon Casing_Machine_Redstone_Off = Internal_Casing_Machine_Redstone_Off;
	private static final CustomIcon Internal_Casing_Machine_Redstone_On = new CustomIcon("TileEntities/cover_redstone_emitter");
	public static final CustomIcon Casing_Machine_Redstone_On = Internal_Casing_Machine_Redstone_On;

	//Centrifuge Casing
	private static final CustomIcon Internal_Casing_Centrifuge = new CustomIcon("TileEntities/MACHINE_CASING_CENTRIFUGE");
	public static final CustomIcon Casing_Material_Centrifuge = Internal_Casing_Centrifuge;

	//MACHINE_CASING_FARM_MANAGER_STRUCTURAL
	//Farm Manager Casings
	private static final CustomIcon Internal_Casing_Machine_Farm_Manager = new CustomIcon("TileEntities/MACHINE_CASING_FARM_MANAGER_STRUCTURAL");
	public static final CustomIcon Casing_Machine_Farm_Manager = Internal_Casing_Machine_Farm_Manager;
	//Acacia_Log
	private static final CustomIcon Internal_Casing_Machine_Acacia_Log = new CustomIcon("TileEntities/log_acacia_top");
	public static final CustomIcon Casing_Machine_Acacia_Log = Internal_Casing_Machine_Acacia_Log;
	//Podzol Top
	private static final CustomIcon Internal_Casing_Machine_Podzol = new CustomIcon("TileEntities/dirt_podzol_top");
	public static final CustomIcon Casing_Machine_Podzol = Internal_Casing_Machine_Podzol;

	//Structural Blocks
	private static final CustomIcon Internal_Casing_Machine_Metal_Grate_A = new CustomIcon("chrono/MetalGrate");
	public static final CustomIcon Casing_Machine_Metal_Grate_A = Internal_Casing_Machine_Metal_Grate_A;
	private static final CustomIcon Internal_Casing_Machine_Metal_Grate_A_Solid = new CustomIcon("chrono/MetalGrateA_Solid");
	public static final CustomIcon Casing_Machine_Metal_Grate_A_Solid = Internal_Casing_Machine_Metal_Grate_A_Solid;
	private static final CustomIcon Internal_Casing_Machine_Metal_Grate_B = new CustomIcon("chrono/MetalGrate2");
	public static final CustomIcon Casing_Machine_Metal_Grate_B = Internal_Casing_Machine_Metal_Grate_B;
	private static final CustomIcon Internal_Casing_Machine_Metal_Panel_A = new CustomIcon("chrono/MetalPanel");
	public static final CustomIcon Casing_Machine_Metal_Panel_A = Internal_Casing_Machine_Metal_Panel_A;
	private static final CustomIcon Internal_Casing_Machine_Metal_Sheet_A = new CustomIcon("chrono/MetalSheet");
	public static final CustomIcon Casing_Machine_Metal_Sheet_A = Internal_Casing_Machine_Metal_Sheet_A;
	private static final CustomIcon Internal_Casing_Machine_Metal_Sheet_B = new CustomIcon("chrono/MetalSheet2");
	public static final CustomIcon Casing_Machine_Metal_Sheet_B = Internal_Casing_Machine_Metal_Sheet_B;
	private static final CustomIcon Internal_Overlay_Machine_Cyber_A = new CustomIcon("chrono/CyberPanel");
	public static final CustomIcon Overlay_Machine_Cyber_A = Internal_Overlay_Machine_Cyber_A;
	private static final CustomIcon Internal_Overlay_Machine_Cyber_B = new CustomIcon("chrono/CyberPanel2");
	public static final CustomIcon Overlay_Machine_Cyber_B = Internal_Overlay_Machine_Cyber_B;

	//Overlays
	//Fan Textures
	private static final CustomIcon Internal_Overlay_Machine_Vent = new CustomIcon("TileEntities/machine_top_vent_rotating");
	public static final CustomIcon Overlay_Machine_Vent = Internal_Overlay_Machine_Vent;
	private static final CustomIcon Internal_Overlay_Machine_Vent_Fast = new CustomIcon("TileEntities/machine_top_vent_rotating_fast");
	public static final CustomIcon Overlay_Machine_Vent_Fast = Internal_Overlay_Machine_Vent_Fast;
	private static final CustomIcon Internal_Overlay_Machine_Vent_Adv = new CustomIcon("TileEntities/adv_machine_vent_rotating");
	public static final CustomIcon Overlay_Machine_Vent_Adv = Internal_Overlay_Machine_Vent_Adv;
	//Speaker Texture
	private static final CustomIcon Internal_Overlay_Machine_Sound = new CustomIcon("TileEntities/audio_out");
	public static final CustomIcon Overlay_Machine_Sound = Internal_Overlay_Machine_Sound;
	private static final CustomIcon Internal_Overlay_Machine_Sound_Active = new CustomIcon("TileEntities/audio_out_active");
	public static final CustomIcon Overlay_Machine_Sound_Active = Internal_Overlay_Machine_Sound_Active;
	//Diesel Engines
	private static final CustomIcon Internal_Overlay_Machine_Diesel_Vertical = new CustomIcon("TileEntities/machine_top_dieselmotor");
	public static final CustomIcon Overlay_Machine_Diesel_Vertical = Internal_Overlay_Machine_Diesel_Vertical;
	private static final CustomIcon Internal_Overlay_Machine_Diesel_Horizontal = new CustomIcon("TileEntities/machine_top_dieselmotor2");
	public static final CustomIcon Overlay_Machine_Diesel_Horizontal = Internal_Overlay_Machine_Diesel_Horizontal;
	private static final CustomIcon Internal_Overlay_Machine_Diesel_Vertical_Active = new CustomIcon("TileEntities/machine_top_dieselmotor_active");
	public static final CustomIcon Overlay_Machine_Diesel_Vertical_Active = Internal_Overlay_Machine_Diesel_Vertical_Active;
	private static final CustomIcon Internal_Overlay_Machine_Diesel_Horizontal_Active = new CustomIcon("TileEntities/machine_top_dieselmotor2_active");
	public static final CustomIcon Overlay_Machine_Diesel_Horizontal_Active = Internal_Overlay_Machine_Diesel_Horizontal_Active;
	//Computer Screens
	private static final CustomIcon Internal_Casing_Machine_Screen_1 = new CustomIcon("TileEntities/adv_machine_screen_random1");
	public static final CustomIcon Casing_Machine_Screen_1 = Internal_Casing_Machine_Screen_1;
	private static final CustomIcon Internal_Casing_Machine_Screen_2 = new CustomIcon("TileEntities/adv_machine_screen_random2");
	public static final CustomIcon Casing_Machine_Screen_2 = Internal_Casing_Machine_Screen_2;
	private static final CustomIcon Internal_Casing_Machine_Screen_3 = new CustomIcon("TileEntities/adv_machine_screen_random3");
	public static final CustomIcon Casing_Machine_Screen_3 = Internal_Casing_Machine_Screen_3;
	private static final CustomIcon Internal_Casing_Machine_Screen_Frequency = new CustomIcon("TileEntities/adv_machine_screen_frequency");
	public static final CustomIcon Casing_Machine_Screen_Frequency = Internal_Casing_Machine_Screen_Frequency;
	private static final CustomIcon Internal_Overlay_Machine_Screen_Logo = new CustomIcon("TileEntities/adv_machine_screen_logo");
	public static final CustomIcon Overlay_Machine_Screen_Logo = Internal_Overlay_Machine_Screen_Logo;
	private static final CustomIcon Internal_Overlay_Machine_Cyber_Interface = new CustomIcon("chrono/Overlay_Cyber");
	public static final CustomIcon Overlay_Machine_Cyber_Interface = Internal_Overlay_Machine_Cyber_Interface;
	//Crafting Overlays
	private static final CustomIcon Internal_Overlay_Crafting_Bronze = new CustomIcon("TileEntities/bronze_top_crafting");
	public static final CustomIcon Overlay_Crafting_Bronze = Internal_Overlay_Crafting_Bronze;
	private static final CustomIcon Internal_Overlay_Crafting_Steel = new CustomIcon("TileEntities/cover_crafting");
	public static final CustomIcon Overlay_Crafting_Steel = Internal_Overlay_Crafting_Steel;
	
	//Hatch Overlays
	//Charger Texture
	private static final CustomIcon Internal_Overlay_Hatch_Charger = new CustomIcon("TileEntities/cover_charger");
	public static final CustomIcon Overlay_Hatch_Charger = Internal_Overlay_Hatch_Charger;
	//Discharger Texture
	private static final CustomIcon Internal_Overlay_Hatch_Discharger = new CustomIcon("TileEntities/cover_discharge");
	public static final CustomIcon Overlay_Hatch_Discharger = Internal_Overlay_Hatch_Discharger;
	
	//Dimensional
	private static final CustomIcon Internal_Overlay_Machine_Dimensional_Blue = new CustomIcon("TileEntities/adv_machine_dimensional_cover_blue");
	public static final CustomIcon Overlay_Machine_Dimensional_Blue = Internal_Overlay_Machine_Dimensional_Blue;
	private static final CustomIcon Internal_Overlay_Machine_Dimensional_Orange = new CustomIcon("TileEntities/adv_machine_dimensional_cover_orange");
	public static final CustomIcon Overlay_Machine_Dimensional_Orange = Internal_Overlay_Machine_Dimensional_Orange;
	//Icons
	private static final CustomIcon Internal_Overlay_MatterFab = new CustomIcon("TileEntities/adv_machine_matterfab");
	public static final CustomIcon Overlay_MatterFab = Internal_Overlay_MatterFab;
	private static final CustomIcon Internal_Overlay_MatterFab_Active = new CustomIcon("TileEntities/adv_machine_matterfab_active");
	public static final CustomIcon Overlay_MatterFab_Active = Internal_Overlay_MatterFab_Active;
	private static final CustomIcon Internal_Overlay_Oil = new CustomIcon("TileEntities/adv_machine_oil");
	public static final CustomIcon Overlay_Oil = Internal_Overlay_Oil;
	private static final CustomIcon Internal_Overlay_UU_Matter = new CustomIcon("TileEntities/adv_machine_uum");
	public static final CustomIcon Overlay_UU_Matter = Internal_Overlay_UU_Matter;

}
