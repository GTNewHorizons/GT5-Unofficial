package gtPlusPlus.xmod.gregtech.common.blocks.textures;

import gregtech.api.enums.Textures;
import gtPlusPlus.xmod.gregtech.common.blocks.GregtechMetaCasingBlocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class CasingTextureHandler {

	//private static final TexturesGregtech59 gregtech59 = new TexturesGregtech59();
	//private static final TexturesGregtech58 gregtech58 = new TexturesGregtech58();
	private static final TexturesCentrifugeMultiblock gregtechX = new TexturesCentrifugeMultiblock();

	public static  IIcon getIcon(int aSide, int aMeta) { //Texture ID's. case 0 == ID[57]
		if ((aMeta >= 0) && (aMeta < 16)) {
			switch (aMeta) {
			//Centrifuge 
			case 0: 
				return TexturesGtBlock.Casing_Material_MaragingSteel.getIcon();
				//Coke Oven Frame
			case 1:
				return TexturesGtBlock.Casing_Material_Tantalloy61.getIcon();
				//Coke Oven Casing Tier 1
			case 2: 
				return Textures.BlockIcons.MACHINE_CASING_FIREBOX_BRONZE.getIcon();
				//Coke Oven Casing Tier 2
			case 3:
				return Textures.BlockIcons.MACHINE_CASING_FIREBOX_STEEL.getIcon();
				//Material Press Casings
			case 4:
				return Textures.BlockIcons.MACHINE_CASING_STABLE_TITANIUM.getIcon();
				//Electrolyzer Casings
			case 5:
				return TexturesGtBlock.Casing_Material_Potin.getIcon();
				//Broken Blue Fusion Casings
			case 6:
				return Textures.BlockIcons.MACHINE_CASING_FUSION.getIcon();
				//Maceration Stack Casings
			case 7:
				return TexturesGtBlock.Casing_Material_Tumbaga.getIcon();
				//Broken Pink Fusion Casings
			case 8:
				return Textures.BlockIcons.MACHINE_CASING_FUSION_2.getIcon();
				//Matter Fabricator Casings
			case 9:
				return TexturesGtBlock.Casing_Machine_Dimensional_Adv.getIcon();
				//Iron Blast Fuance Textures
			case 10:
				return TexturesGtBlock.Casing_Machine_Simple_Top.getIcon();		
				//Multitank Exterior Casing
			case 11:
				return TexturesGtBlock.Casing_Material_Grisium.getIcon();	
				//Reactor Casing I
			case 12:
				return TexturesGtBlock.Casing_Material_Stellite.getIcon();	
				//Reactor Casing II
			case 13:
				return TexturesGtBlock.Casing_Material_Zeron100.getIcon();	
			case 14:
				return TexturesGtBlock.Casing_Staballoy_Firebox.getIcon();
			case 15:
				return TexturesGtBlock.Casing_Material_ZirconiumCarbide.getIcon();		

			default:
				return Textures.BlockIcons.MACHINE_CASING_RADIOACTIVEHAZARD.getIcon();

			}
		}
		return Textures.BlockIcons.MACHINE_CASING_GEARBOX_TUNGSTENSTEEL.getIcon();
	}


	public static IIcon handleCasingsGT(IBlockAccess aWorld, int xCoord, int yCoord, int zCoord, int aSide, GregtechMetaCasingBlocks thisBlock) {
		/*if (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK){
			return gregtech59.handleCasingsGT59(aWorld, xCoord, yCoord, zCoord, aSide, thisBlock);
		}
		return gregtech58.handleCasingsGT58(aWorld, xCoord, yCoord, zCoord, aSide, thisBlock);*/
		return gregtechX.handleCasingsGT(aWorld, xCoord, yCoord, zCoord, aSide, thisBlock);
	}
}