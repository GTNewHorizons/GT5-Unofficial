package gtPlusPlus.xmod.gregtech.common.blocks.textures;

import gregtech.api.enums.Textures;
import net.minecraft.util.IIcon;

public class CasingTextureHandler3 {

	public static  IIcon getIcon(final int aSide, final int aMeta) { //Texture ID's. case 0 == ID[57]
		if ((aMeta >= 0) && (aMeta < 16)) {
			switch (aMeta) {
			//Centrifuge
			case 0:		
				return TexturesGtBlock.TEXTURE_METAL_PANEL_B.getIcon();								
				//Coke Oven Frame
			case 1:
				return TexturesGtBlock.TEXTURE_METAL_PANEL_D.getIcon();
				//Coke Oven Casing Tier 1
			case 2:
				return TexturesGtBlock._PlaceHolder.getIcon();
				//Coke Oven Casing Tier 2
			case 3:
				return TexturesGtBlock._PlaceHolder.getIcon();
				//Material Press Casings
			case 4:
				return TexturesGtBlock._PlaceHolder.getIcon();				
				//Sifter Structural
			case 5:
				return TexturesGtBlock._PlaceHolder.getIcon();
				//Sifter Sieve
			case 6:
				return TexturesGtBlock._PlaceHolder.getIcon();				
				//Vanadium Radox Battery
			case 7:
				return TexturesGtBlock._PlaceHolder.getIcon();
				//Power Sub-Station Casing
			case 8:
				return TexturesGtBlock._PlaceHolder.getIcon();
				//Cyclotron Coil
			case 9:
				return TexturesGtBlock._PlaceHolder.getIcon();
				//Cyclotron External Casing
			case 10:
				return TexturesGtBlock._PlaceHolder.getIcon();
				//Multitank Exterior Casing
			case 11:
				return TexturesGtBlock._PlaceHolder.getIcon();
				//Reactor Casing I
			case 12:
				return TexturesGtBlock._PlaceHolder.getIcon();
				//Reactor Casing II
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
				return TexturesGtBlock._PlaceHolder.getIcon(); //Tree Farmer Textures

			default:
				return TexturesGtBlock._PlaceHolder.getIcon();

			}
		}
		return TexturesGtBlock._PlaceHolder.getIcon();
	}

}