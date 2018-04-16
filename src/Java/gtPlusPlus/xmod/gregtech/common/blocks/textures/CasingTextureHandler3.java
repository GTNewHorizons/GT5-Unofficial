package gtPlusPlus.xmod.gregtech.common.blocks.textures;

import net.minecraft.util.IIcon;

public class CasingTextureHandler3 {

	public static  IIcon getIcon(final int aSide, final int aMeta) { //Texture ID's. case 0 == ID[57]
		if ((aMeta >= 0) && (aMeta < 16)) {
			switch (aMeta) {
			case 0:
				//Aquatic Casing
				return TexturesGtBlock.TEXTURE_METAL_PANEL_B.getIcon();
			case 1:
				//Inconel Reinforced Casing
				return TexturesGtBlock.TEXTURE_METAL_PANEL_D.getIcon();
			case 2:
				//Multi-Use Casing
				return TexturesGtBlock.TEXTURE_METAL_PANEL_C.getIcon();
			case 3:
				//Trinium Plated Mining Platform Casing
				return TexturesGtBlock.Casing_Staballoy_Firebox.getIcon();
			case 4:
				//Vanadium Redox IV
				return TexturesGtBlock.Casing_Redox_2.getIcon();
			case 5:
				//Vanadium Redox LuV
				return TexturesGtBlock.Casing_Redox_3.getIcon();
			case 6:
				//Vanadium Redox ZPM
				return TexturesGtBlock.Casing_Redox_4.getIcon();
			case 7:
				//Vanadium Redox UV
				return TexturesGtBlock.Casing_Redox_5.getIcon();
			case 8:
				//Vanadium Redox MAX
				return TexturesGtBlock.Casing_Redox_6.getIcon();
			case 9:
				//Amazon Warehouse Casing
				return TexturesGtBlock.TEXTURE_CASING_AMAZON.getIcon();
			case 10:
				return TexturesGtBlock._PlaceHolder.getIcon();
			case 11:
				return TexturesGtBlock._PlaceHolder.getIcon();
			case 12:
				return TexturesGtBlock._PlaceHolder.getIcon();
			case 13:
				if (aSide <2) {
					return TexturesGtBlock._PlaceHolder.getIcon();					
				}
				else {
					return TexturesGtBlock._PlaceHolder.getIcon();					
				}
			case 14:
				return TexturesGtBlock._PlaceHolder.getIcon();
			case 15:
				return TexturesGtBlock._PlaceHolder.getIcon();

			default:
				return TexturesGtBlock._PlaceHolder.getIcon();

			}
		}
		return TexturesGtBlock._PlaceHolder.getIcon();
	}

}