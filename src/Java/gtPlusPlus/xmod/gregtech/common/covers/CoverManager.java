package gtPlusPlus.xmod.gregtech.common.covers;

import static gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtCutomCovers.TEXTURE_ZTONES_AGON;
import static gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtCutomCovers.TEXTURE_ZTONES_BITT;
import static gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtCutomCovers.TEXTURE_ZTONES_ISZM;
import static gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtCutomCovers.TEXTURE_ZTONES_JELT;
import static gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtCutomCovers.TEXTURE_ZTONES_KORP;

import cpw.mods.fml.common.Loader;
import gtPlusPlus.core.lib.VanillaColours;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock.CustomIcon;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtCutomCovers;
import gtPlusPlus.xmod.gregtech.common.items.MetaCustomCoverItem;
import gtPlusPlus.xmod.gregtech.common.items.covers.MetaItemCoverCasings;

public class CoverManager {

	// ZTones
	public static MetaCustomCoverItem Cover_Agon;
	public static MetaCustomCoverItem Cover_Iszm;
	public static MetaCustomCoverItem Cover_Korp;
	public static MetaCustomCoverItem Cover_Jelt;
	public static MetaCustomCoverItem Cover_Bitt;

	// GT
	public static MetaItemCoverCasings Cover_Gt_Machine_Casing;

	public static void generateCustomCovers() {

		// init textures
		TexturesGtCutomCovers.init();

		// GT Machine Casings
		Cover_Gt_Machine_Casing = new MetaItemCoverCasings();

		if (Loader.isModLoaded("Ztones")) {
			String[] aZtoneCoverTextureNames = new String[] { "agon", "iszm", "korp", "jelt", "bitt" };
			MetaCustomCoverItem[] aZtoneCoverItems = new MetaCustomCoverItem[] { Cover_Agon, Cover_Iszm, Cover_Korp,
					Cover_Jelt, Cover_Bitt };
			CustomIcon[][] aArrays = new CustomIcon[][] { TEXTURE_ZTONES_AGON, TEXTURE_ZTONES_ISZM, TEXTURE_ZTONES_KORP,
					TEXTURE_ZTONES_JELT, TEXTURE_ZTONES_BITT };
			short[][][] aRGB = new short[][][] { ZTONES.RGB_AGON, ZTONES.RGB_ISZM, ZTONES.RGB_KORP, ZTONES.RGB_JELT,
					ZTONES.RGB_BITT };
			for (int y = 0; y < aZtoneCoverTextureNames.length; y++) {
				aZtoneCoverItems[y] = new MetaCustomCoverItem("Ztones", 16, aZtoneCoverTextureNames[y], aArrays[y],
						aRGB[y]);
			}
		}

	}

	final static class ZTONES {

		private static final short[][] RGB_AGON = new short[][] { VanillaColours.DYE_WHITE.getAsShort(),
				VanillaColours.DYE_YELLOW.getAsShort(), VanillaColours.DYE_LIME.getAsShort(),
				VanillaColours.DYE_GREEN.getAsShort(), VanillaColours.DYE_CYAN.getAsShort(),
				VanillaColours.DYE_LIGHT_BLUE.getAsShort(), VanillaColours.DYE_DARK_BLUE.getAsShort(),
				VanillaColours.DYE_DARK_PURPLE.getAsShort(), VanillaColours.DYE_LIGHT_PURPLE.getAsShort(),
				VanillaColours.DYE_PINK.getAsShort(), VanillaColours.DYE_RED.getAsShort(),
				VanillaColours.DYE_ORANGE.getAsShort(), VanillaColours.DYE_BROWN.getAsShort(),
				VanillaColours.DYE_BLACK.getAsShort(), VanillaColours.DYE_DARK_GRAY.getAsShort(),
				VanillaColours.DYE_LIGHT_GRAY.getAsShort(), };
		private static final short[][] RGB_ISZM = new short[][] { VanillaColours.DYE_LIGHT_GRAY.getAsShort(),
				VanillaColours.DYE_WHITE.getAsShort(), VanillaColours.DYE_DARK_GRAY.getAsShort(),
				VanillaColours.DYE_DARK_BLUE.getAsShort(), VanillaColours.DYE_YELLOW.getAsShort(),
				VanillaColours.DYE_DARK_BLUE.getAsShort(), VanillaColours.DYE_RED.getAsShort(),
				VanillaColours.DYE_ORANGE.getAsShort(), VanillaColours.DYE_CYAN.getAsShort(),
				VanillaColours.DYE_YELLOW.getAsShort(), VanillaColours.DYE_RED.getAsShort(),
				VanillaColours.DYE_CYAN.getAsShort(), VanillaColours.DYE_GREEN.getAsShort(),
				VanillaColours.DYE_ORANGE.getAsShort(), VanillaColours.DYE_LIGHT_BLUE.getAsShort(),
				VanillaColours.DYE_DARK_PURPLE.getAsShort(),
		};
		private static final short[][] RGB_KORP = new short[][] { new short[] { 125, 125, 125 },
				VanillaColours.DYE_DARK_GRAY.getAsShort(), VanillaColours.DYE_DARK_GRAY.getAsShort(),
				VanillaColours.DYE_DARK_GRAY.getAsShort(), VanillaColours.DYE_DARK_GRAY.getAsShort(),
				VanillaColours.DYE_DARK_GRAY.getAsShort(), VanillaColours.DYE_DARK_GRAY.getAsShort(),
				VanillaColours.DYE_DARK_GRAY.getAsShort(), VanillaColours.DYE_DARK_GRAY.getAsShort(),
				VanillaColours.DYE_DARK_GRAY.getAsShort(), VanillaColours.DYE_DARK_GRAY.getAsShort(),
				VanillaColours.DYE_DARK_GRAY.getAsShort(), new short[] { 22, 156, 156 }, new short[] { 22, 156, 156 },
				VanillaColours.DYE_DARK_GRAY.getAsShort(), VanillaColours.DYE_DARK_GRAY.getAsShort(), };
		private static final short[][] RGB_JELT = new short[][] { VanillaColours.DYE_ORANGE.getAsShort(),
				VanillaColours.DYE_ORANGE.getAsShort(), VanillaColours.DYE_ORANGE.getAsShort(),
				VanillaColours.DYE_ORANGE.getAsShort(), VanillaColours.DYE_ORANGE.getAsShort(),
				VanillaColours.DYE_ORANGE.getAsShort(), VanillaColours.DYE_ORANGE.getAsShort(),
				VanillaColours.DYE_ORANGE.getAsShort(), VanillaColours.DYE_ORANGE.getAsShort(),
				VanillaColours.DYE_ORANGE.getAsShort(), VanillaColours.DYE_ORANGE.getAsShort(),
				VanillaColours.DYE_ORANGE.getAsShort(), VanillaColours.DYE_ORANGE.getAsShort(),
				VanillaColours.DYE_ORANGE.getAsShort(), VanillaColours.DYE_ORANGE.getAsShort(),
				VanillaColours.DYE_ORANGE.getAsShort(), };
		private static final short[][] RGB_BITT = new short[][] { VanillaColours.DYE_BLACK.getAsShort(),
				VanillaColours.DYE_WHITE.getAsShort(), VanillaColours.DYE_YELLOW.getAsShort(),
				VanillaColours.DYE_LIME.getAsShort(), VanillaColours.DYE_GREEN.getAsShort(),
				VanillaColours.DYE_CYAN.getAsShort(), VanillaColours.DYE_LIGHT_BLUE.getAsShort(),
				VanillaColours.DYE_LIGHT_BLUE.getAsShort(), VanillaColours.DYE_DARK_BLUE.getAsShort(),
				VanillaColours.DYE_DARK_PURPLE.getAsShort(), VanillaColours.DYE_LIGHT_PURPLE.getAsShort(),
				VanillaColours.DYE_PINK.getAsShort(), VanillaColours.DYE_RED.getAsShort(),
				VanillaColours.DYE_RED.getAsShort(), VanillaColours.DYE_ORANGE.getAsShort(),
				VanillaColours.DYE_BROWN.getAsShort(), };

	}

}
