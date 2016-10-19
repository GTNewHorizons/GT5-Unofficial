package gtPlusPlus.xmod.gregtech.common.blocks.textures;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.IIconContainer;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public class TexturesGtBlocks {

	/*
	 * Handles Custom Textures.
	 */
	
	public static class CustomIcon implements IIconContainer, Runnable {
		protected IIcon mIcon;
		protected String mIconName;

		public CustomIcon(String aIconName) {
			mIconName = aIconName;
			Utils.LOG_WARNING("Constructing a Custom Texture. " + mIconName);
			GregTech_API.sGTBlockIconload.add(this);
		}

		@Override
		public IIcon getIcon() {
			return mIcon;
		}

		@Override
		public IIcon getOverlayIcon() {
			return null;
		}

		@Override
		public void run() {
			mIcon = GregTech_API.sBlockIcons.registerIcon(CORE.MODID + ":"  + mIconName);
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



	//Machine Casings
	private static final CustomIcon Internal_Casing_Machine_Simple = new CustomIcon("TileEntities/machine_top");	
	public static final CustomIcon Casing_Machine_Simple = Internal_Casing_Machine_Simple;
	
	private static final CustomIcon Internal_Casing_Machine_Dimensional = new CustomIcon("TileEntities/adv_machine_dimensional");	
	public static final CustomIcon Casing_Machine_Dimensional = Internal_Casing_Machine_Dimensional;
	private static final CustomIcon Internal_Casing_Machine_Dimensional_Adv = new CustomIcon("TileEntities/high_adv_machine_dimensional");	
	public static final CustomIcon Casing_Machine_Dimensional_Adv = Internal_Casing_Machine_Dimensional_Adv;
	
	private static final CustomIcon Internal_Casing_Machine_Sound = new CustomIcon("TileEntities/audio_out");	
	public static final CustomIcon Casing_Machine_Sound = Internal_Casing_Machine_Sound;
	private static final CustomIcon Internal_Casing_Machine_Sound_Active = new CustomIcon("TileEntities/audio_out_active");	
	public static final CustomIcon Casing_Machine_Sound_Active = Internal_Casing_Machine_Sound_Active;
	
	private static final CustomIcon Internal_Casing_Machine_Redstone_Off = new CustomIcon("TileEntities/cover_redstone_conductor");	
	public static final CustomIcon Casing_Machine_Redstone_Off = Internal_Casing_Machine_Redstone_Off;
	private static final CustomIcon Internal_Casing_Machine_Redstone_On = new CustomIcon("TileEntities/cover_redstone_emitter");	
	public static final CustomIcon Casing_Machine_Redstone_On = Internal_Casing_Machine_Redstone_On;
	
	private static final CustomIcon Internal_Casing_Machine_Vent = new CustomIcon("TileEntities/machine_top_vent_rotating");	
	public static final CustomIcon Casing_Machine_Vent = Internal_Casing_Machine_Vent;
	private static final CustomIcon Internal_Casing_Machine_Vent_Fast = new CustomIcon("TileEntities/machine_top_vent_rotating_fast");	
	public static final CustomIcon Casing_Machine_Vent_Fast = Internal_Casing_Machine_Vent_Fast;
	private static final CustomIcon Internal_Casing_Machine_Vent_Adv = new CustomIcon("TileEntities/adv_machine_vent_rotating");	
	public static final CustomIcon Casing_Machine_Vent_Adv = Internal_Casing_Machine_Vent_Adv;
	
	//Computer Screens
	private static final CustomIcon Internal_Casing_Machine_Screen_1 = new CustomIcon("TileEntities/adv_machine_screen_random1");	
	public static final CustomIcon Casing_Machine_Screen_1 = Internal_Casing_Machine_Screen_1;
	private static final CustomIcon Internal_Casing_Machine_Screen_2 = new CustomIcon("TileEntities/adv_machine_screen_random2");	
	public static final CustomIcon Casing_Machine_Screen_2 = Internal_Casing_Machine_Screen_2;
	private static final CustomIcon Internal_Casing_Machine_Screen_3 = new CustomIcon("TileEntities/adv_machine_screen_random3");	
	public static final CustomIcon Casing_Machine_Screen_3 = Internal_Casing_Machine_Screen_3;
	private static final CustomIcon Internal_Casing_Machine_Screen_Frequency = new CustomIcon("TileEntities/adv_machine_screen_frequency");	
	public static final CustomIcon Casing_Machine_Screen_Frequency = Internal_Casing_Machine_Screen_Frequency;
	
	//Overlays
	private static final CustomIcon Internal_Overlay_Crafting_Bronze = new CustomIcon("TileEntities/bronze_top_crafting");	
	public static final CustomIcon Overlay_Crafting_Bronze = Internal_Overlay_Crafting_Bronze;
	private static final CustomIcon Internal_Overlay_Crafting_Steel = new CustomIcon("TileEntities/cover_crafting");	
	public static final CustomIcon Overlay_Crafting_Steel = Internal_Overlay_Crafting_Steel;


}
