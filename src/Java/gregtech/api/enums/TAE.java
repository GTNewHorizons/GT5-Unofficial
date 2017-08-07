package gregtech.api.enums;

import gregtech.api.interfaces.ITexture;
import gregtech.api.objects.GT_CopiedBlockTexture;
import gregtech.api.objects.GT_RenderedTexture;
import gtPlusPlus.core.util.Utils;

public class TAE {

	//TAE stands for Texture Array Expansion.
	
	public static int gtTexturesArrayStartOrigin;
	public static int gtPPLastUsedIndex = 96;
	public static boolean hasArrayBeenExpanded = false;

	public static boolean hookGtTextures() {
		ITexture[] textureArrayDump = Textures.BlockIcons.CASING_BLOCKS;
		ITexture[] newTextureArray = new ITexture[1024];
		Utils.LOG_INFO("|======| Texture Array Start Length: "+textureArrayDump.length+" |======|");
		for (int r=0;r<textureArrayDump.length;r++){
			if (textureArrayDump[r] == null){
				Utils.LOG_WARNING("Texture slot "+r+" is empty.");
			}
		}
		gtTexturesArrayStartOrigin = textureArrayDump.length;
		System.arraycopy(textureArrayDump, 0, newTextureArray, 0, textureArrayDump.length);
		Textures.BlockIcons.CASING_BLOCKS  = newTextureArray;
		if (Textures.BlockIcons.CASING_BLOCKS.length == 1024){
			hasArrayBeenExpanded = true;
		}
		else {
			hasArrayBeenExpanded = false;
		}
		return hasArrayBeenExpanded;		
	}

	public static boolean registerTextures(GT_RenderedTexture textureToRegister) {
		Textures.BlockIcons.CASING_BLOCKS[gtPPLastUsedIndex++] = textureToRegister;
		//Just so I know registration is done.
		return true;
	}

	public static boolean registerTextures(GT_CopiedBlockTexture gt_CopiedBlockTexture) {
		Textures.BlockIcons.CASING_BLOCKS[gtPPLastUsedIndex++] = gt_CopiedBlockTexture;
		//Just so I know registration is done.
		return true;
	}
	
	public static ITexture getTexture(int index){
		if (!hasArrayBeenExpanded){
			return null;
		}
		else {
			return Textures.BlockIcons.CASING_BLOCKS[(96+index)];
		}
	}
	
	public static int GTPP_INDEX(int ID){
		return (96+ID);
	}
}
