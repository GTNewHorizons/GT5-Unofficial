package gtPlusPlus.xmod.gregtech.common.blocks.textures;

import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock.CustomIcon;

public class TexturesGtCutomCovers {

	public static final CustomIcon[] TEXTURE_ZTONES_AGON = new CustomIcon[16];
	public static final CustomIcon[] TEXTURE_ZTONES_ISZM = new CustomIcon[16];
	public static final CustomIcon[] TEXTURE_ZTONES_KORP = new CustomIcon[16];
	public static final CustomIcon[] TEXTURE_ZTONES_JELT = new CustomIcon[16];
	public static final CustomIcon[] TEXTURE_ZTONES_BITT = new CustomIcon[16];
	
	public static void init() {
		generateZTones();
	}

	private static void generateZTones() {
		// ZTONES
		String[] aZtoneCoverTextureNames = new String[] { "agon", "iszm", "korp", "jelt", "bitt" };
		int aArrayIndex = 0;
		CustomIcon[][] aArrays = new CustomIcon[][] { TEXTURE_ZTONES_AGON, TEXTURE_ZTONES_ISZM, TEXTURE_ZTONES_KORP,
				TEXTURE_ZTONES_JELT, TEXTURE_ZTONES_BITT };

		for (CustomIcon[] t : aArrays) {
			for (int s = 0; s < 16; s++) {
				t[s] = new CustomIcon("Ztones", "sets/" + aZtoneCoverTextureNames[aArrayIndex] + "/"+aZtoneCoverTextureNames[aArrayIndex]+"_ (" + s + ")");
			}
			aArrayIndex++;
		}
	}

}
