package gtPlusPlus.xmod.gregtech.common.covers;

import static gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtCutomCovers.TEXTURE_ZTONES_AGON;
import static gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtCutomCovers.TEXTURE_ZTONES_BITT;
import static gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtCutomCovers.TEXTURE_ZTONES_ISZM;
import static gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtCutomCovers.TEXTURE_ZTONES_JELT;
import static gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtCutomCovers.TEXTURE_ZTONES_KORP;

import cpw.mods.fml.common.Loader;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock.CustomIcon;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtCutomCovers;
import gtPlusPlus.xmod.gregtech.common.items.MetaCustomCoverItem;

public class CoverManager {
	
	//ZTones
	public static MetaCustomCoverItem Cover_Agon;
	public static MetaCustomCoverItem Cover_Iszm;
	public static MetaCustomCoverItem Cover_Korp;
	public static MetaCustomCoverItem Cover_Jelt;
	public static MetaCustomCoverItem Cover_Bitt;
	

	public static void generateCustomCovers() {

		// init textures
		TexturesGtCutomCovers.init();
		
		if (Loader.isModLoaded("Ztones")) {
			String[] aZtoneCoverTextureNames = new String[] { "agon", "iszm", "korp", "jelt", "bitt" };
			MetaCustomCoverItem[] aZtoneCoverItems = new MetaCustomCoverItem[] { Cover_Agon, Cover_Iszm, Cover_Korp, Cover_Jelt, Cover_Bitt};
			CustomIcon[][] aArrays = new CustomIcon[][] { TEXTURE_ZTONES_AGON, TEXTURE_ZTONES_ISZM, TEXTURE_ZTONES_KORP, TEXTURE_ZTONES_JELT, TEXTURE_ZTONES_BITT };			
			for (int y=0;y<aZtoneCoverTextureNames.length;y++) {
				aZtoneCoverItems[y] = new MetaCustomCoverItem("Ztones", 16, aZtoneCoverTextureNames[y], aArrays[y]);
			}			
		}
		
	}
	
	
}
