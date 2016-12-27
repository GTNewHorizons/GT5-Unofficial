package gtPlusPlus.xmod.gregtech.common.blocks.textures;

import net.minecraft.util.IIcon;

public class CasingTextureHandler2 {

	public static  IIcon getIcon(int aSide, int aMeta) { //Texture ID's. case 0 == ID[57]
		if ((aMeta >= 0) && (aMeta < 16)) {
			switch (aMeta) {
			//Centrifuge 
			case 0: 
				return TexturesGtBlock.Casing_Machine_Dimensional_Adv.getIcon();
				//Coke Oven Frame
			case 1:
				return TexturesGtBlock.Casing_Material_HastelloyX.getIcon();
				//Coke Oven Casing Tier 1
			case 2: 
				return TexturesGtBlock.Casing_Material_HastelloyN.getIcon();
				//Coke Oven Casing Tier 2
			case 3: 
				return TexturesGtBlock.Casing_Material_Fluid_IncoloyDS.getIcon();
				//Material Press Casings
			case 4: 
				return TexturesGtBlock._PlaceHolder.getIcon();
				//Electrolyzer Casings
			case 5: 
				return TexturesGtBlock._PlaceHolder.getIcon();
				//Broken Blue Fusion Casings
			case 6: 
				return TexturesGtBlock._PlaceHolder.getIcon();
				//Maceration Stack Casings
			case 7: 
				return TexturesGtBlock._PlaceHolder.getIcon();
				//Broken Pink Fusion Casings
			case 8: 
				return TexturesGtBlock._PlaceHolder.getIcon();
				//Matter Fabricator Casings
			case 9: 
				return TexturesGtBlock._PlaceHolder.getIcon();
				//Iron Blast Fuance Textures
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
				return TexturesGtBlock._PlaceHolder.getIcon();
			case 14: 
				return TexturesGtBlock._PlaceHolder.getIcon();
			case 15: 
				return TexturesGtBlock._PlaceHolder.getIcon();		

			default: 
				return TexturesGtBlock.Overlay_UU_Matter.getIcon();

			}
		}	
		return TexturesGtBlock._PlaceHolder.getIcon();
	}

}