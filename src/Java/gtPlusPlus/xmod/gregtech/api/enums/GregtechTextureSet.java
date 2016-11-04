package gtPlusPlus.xmod.gregtech.api.enums;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;

public class GregtechTextureSet {
	public static final GregtechTextureSet	SET_NONE	= new GregtechTextureSet("NONE"),
			SET_DULL = new GregtechTextureSet("DULL"), SET_RUBY = new GregtechTextureSet("RUBY"),
			SET_OPAL = new GregtechTextureSet("OPAL"), SET_LEAF = new GregtechTextureSet("LEAF"),
			SET_WOOD = new GregtechTextureSet("WOOD"), SET_SAND = new GregtechTextureSet("SAND"),
			SET_FINE = new GregtechTextureSet("FINE"), SET_FIERY = new GregtechTextureSet("FIERY"),
			SET_FLUID = new GregtechTextureSet("FLUID"), SET_ROUGH = new GregtechTextureSet("ROUGH"),
			SET_PAPER = new GregtechTextureSet("PAPER"), SET_GLASS = new GregtechTextureSet("GLASS"),
			SET_FLINT = new GregtechTextureSet("FLINT"), SET_LAPIS = new GregtechTextureSet("LAPIS"),
			SET_SHINY = new GregtechTextureSet("SHINY"), SET_SHARDS = new GregtechTextureSet("SHARDS"),
			SET_POWDER = new GregtechTextureSet("POWDER"), SET_QUARTZ = new GregtechTextureSet("QUARTZ"),
			SET_EMERALD = new GregtechTextureSet("EMERALD"), SET_DIAMOND = new GregtechTextureSet("DIAMOND"),
			SET_LIGNITE = new GregtechTextureSet("LIGNITE"), SET_MAGNETIC = new GregtechTextureSet("MAGNETIC"),
			SET_METALLIC = new GregtechTextureSet("METALLIC"), SET_NETHERSTAR = new GregtechTextureSet("NETHERSTAR"),
			SET_GEM_VERTICAL = new GregtechTextureSet("GEM_VERTICAL"),
			SET_GEM_HORIZONTAL = new GregtechTextureSet("GEM_HORIZONTAL");

	public final IIconContainer[]			mTextures	= new IIconContainer[128];
	public final String						mSetName;

	public GregtechTextureSet(final String aSetName) {
		this.mSetName = aSetName;
		this.mTextures[0] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/turbineBlade");
		this.mTextures[1] = new Textures.ItemIcons.CustomIcon(
				"materialicons/" + this.mSetName + "/toolHeadSkookumChoocher");
		this.mTextures[2] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[3] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[4] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[5] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[6] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[7] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[8] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[9] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[10] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[11] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[12] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[13] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[14] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[15] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[16] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[17] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[18] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[19] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[20] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[21] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[22] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[23] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[24] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[25] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[26] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[27] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[28] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[29] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[30] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[31] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[32] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[33] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[34] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[35] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[36] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[37] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[38] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[39] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[40] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[41] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[42] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[43] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[44] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[45] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[46] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[47] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[48] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[49] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[50] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[51] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[52] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[53] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[54] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[55] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[56] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[57] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[58] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[59] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[60] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[61] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[62] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[63] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[64] = new Textures.BlockIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[65] = new Textures.BlockIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[66] = new Textures.BlockIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[67] = new Textures.BlockIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[68] = new Textures.BlockIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[69] = new Textures.BlockIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[70] = new Textures.BlockIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[71] = new Textures.BlockIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[72] = new Textures.BlockIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[73] = new Textures.BlockIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[74] = new Textures.BlockIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[75] = new Textures.BlockIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[76] = new Textures.BlockIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[77] = new Textures.BlockIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[78] = new Textures.BlockIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[79] = new Textures.BlockIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[80] = new Textures.BlockIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[81] = new Textures.BlockIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[82] = new Textures.BlockIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[83] = new Textures.BlockIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[84] = new Textures.BlockIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[85] = new Textures.BlockIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[86] = new Textures.BlockIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[87] = new Textures.BlockIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[88] = new Textures.BlockIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[89] = new Textures.BlockIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[90] = new Textures.BlockIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[91] = new Textures.BlockIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[92] = new Textures.BlockIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[93] = new Textures.BlockIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[94] = new Textures.BlockIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[95] = new Textures.BlockIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[96] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[97] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[98] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[99] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[100] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[101] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[102] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[103] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[104] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[105] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[106] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[107] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[108] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[109] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[110] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[111] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[112] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[113] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[114] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[115] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[116] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[117] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[118] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[119] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[120] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[121] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[122] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[123] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[124] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[125] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[126] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
		this.mTextures[127] = new Textures.ItemIcons.CustomIcon("materialicons/" + this.mSetName + "/void");
	}
}