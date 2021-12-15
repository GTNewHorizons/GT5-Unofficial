package gtPlusPlus.core.lib;

import gtPlusPlus.core.util.Utils;

public enum VanillaColours {

	BONE_MEAL(249, 255, 254), INK_BLACK(29, 29, 33), COCOA_BEANS(131, 84, 50), LAPIS_LAZULI(60, 68, 170),
	DYE_WHITE(249, 255, 254), DYE_BLACK(29, 29, 33), DYE_RED(176, 46, 38), DYE_GREEN(94, 124, 22),
	DYE_CYAN(22, 156, 156), DYE_PINK(243, 139, 170), DYE_LIME(128, 199, 31), DYE_YELLOW(254, 216, 61),
	DYE_ORANGE(249, 128, 29), DYE_BROWN(131, 84, 50), DYE_LIGHT_BLUE(58, 179, 218), DYE_LIGHT_PURPLE(199, 78, 189),
	DYE_LIGHT_GRAY(157, 157, 151), DYE_DARK_BLUE(60, 68, 170), DYE_DARK_PURPLE(137, 50, 184), DYE_DARK_GRAY(71, 79, 82);

	private final int r, g, b;

	private VanillaColours(int aR, int aG, int aB) {
		r = aR;
		g = aG;
		b = aB;
	}

	public short[] getAsShort() {
		return new short[] { (short) r, (short) g, (short) b };
	}
	
	public int getAsInt() {
		return Utils.rgbtoHexValue(r, g, b);
	}
}
