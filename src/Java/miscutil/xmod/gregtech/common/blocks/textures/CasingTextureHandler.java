package miscutil.xmod.gregtech.common.blocks.textures;

import gregtech.api.enums.Textures;
import miscutil.core.lib.CORE;
import miscutil.xmod.gregtech.common.blocks.GregtechMetaCasingBlocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class CasingTextureHandler {
	
	private static final TexturesGregtech59 gregtech59 = new TexturesGregtech59();
	private static final TexturesGregtech58 gregtech58 = new TexturesGregtech58();
	
	public static  IIcon getIcon(int aSide, int aMeta) { //Texture ID's. case 0 == ID[57]
		if ((aMeta >= 0) && (aMeta < 16)) {
			switch (aMeta) {
			//Centrifuge 
			case 0: 
				return Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL.getIcon();
				//Coke Oven Frame
			case 1:
				return Textures.BlockIcons.MACHINE_CASING_RADIATIONPROOF.getIcon();
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
				return Textures.BlockIcons.MACHINE_CASING_FUSION_2.getIcon();
				//Broken Blue Fusion Casings
			case 6:
				return Textures.BlockIcons.MACHINE_CASING_FUSION.getIcon();
				//Maceration Stack Casings
			case 7:
				return Textures.BlockIcons.MACHINE_LuV_BOTTOM.getIcon();
				//Broken Pink Fusion Casings
			case 8:
				return Textures.BlockIcons.MACHINE_CASING_FUSION_2.getIcon();
				//Matter Fabricator Casings
			case 9:
				return Textures.BlockIcons.MACHINE_CASING_DRAGONEGG.getIcon();
				//Iron Blast Fuance Textures
			case 10:
				return Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL.getIcon();			

			default:
				return Textures.BlockIcons.MACHINE_CASING_RADIOACTIVEHAZARD.getIcon();

			}
		}
		return Textures.BlockIcons.MACHINE_CASING_GEARBOX_TUNGSTENSTEEL.getIcon();
	}
	
	
	public static IIcon handleCasingsGT(IBlockAccess aWorld, int xCoord, int yCoord, int zCoord, int aSide, GregtechMetaCasingBlocks thisBlock) {
		if (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK){
			return gregtech59.handleCasingsGT59(aWorld, xCoord, yCoord, zCoord, aSide, thisBlock);
		}
		return gregtech58.handleCasingsGT58(aWorld, xCoord, yCoord, zCoord, aSide, thisBlock);
	}
}