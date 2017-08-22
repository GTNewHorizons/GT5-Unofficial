package gtPlusPlus.core.material.gregtech;

import static gregtech.api.enums.Materials.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.Element;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TC_Aspects;
import gregtech.api.enums.TC_Aspects.TC_AspectStack;
import gregtech.api.enums.TextureSet;
import gregtech.api.interfaces.IMaterialHandler;
import gregtech.api.objects.MaterialStack;
import gtPlusPlus.core.util.Utils;

public class CustomGTMaterials {

	//public static Materials Fireclay = new MaterialBuilder(626, TextureSet.SET_ROUGH, "Fireclay").addDustItems().setRGB(173, 160, 155).setColor(Dyes.dyeBrown).setMaterialList(new MaterialStack(Brick, 1)).constructMaterial();
	
	/**int aMetaItemSubID, 
	 * TextureSet aIconSet, 
	 * float aToolSpeed,
	 * int aDurability, 
	 * int aToolQuality, 
	 * boolean aUnificatable, 
	 * String aName, String aDefaultLocalName, 
	 * String aConfigSection, 
	 * boolean aCustomOre, 
	 * String aCustomID) {
	
	**/
	
	public static List<Materials> Custom_GT_Materials = new ArrayList<Materials>();
	
	public static Materials Zirconium = materialBuilder_Element(1232, TextureSet.SET_METALLIC, 		6.0F, 256, 2, 1|2|8|32|64|128, 200, 200, 200, 0, "Zirconium", "Zirconium", 0, 0, 1811, 0, false, false, 3, 1, 1, Dyes.dyeLightGray, Element.Zr, Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 3)));
	public static Materials Geikielite = materialBuilder(1234, TextureSet.SET_SHINY, new int[]{1,2,3}, "Geikielite", Dyes.dyeBlack, Arrays.asList(new MaterialStack(Titanium, 1), new MaterialStack(Magnesium, 1), new MaterialStack(Oxygen, 3)));
	public static Materials Zirconolite = materialBuilder(1235, TextureSet.SET_METALLIC, new int[]{1,2,3}, "Zirconolite", Dyes.dyeBlack, Arrays.asList(new MaterialStack(Calcium, 1), new MaterialStack(Zirconium, 1), new MaterialStack(Titanium, 2), new MaterialStack(Oxygen, 7)));
	
	private final static boolean registerMaterial(Materials r){
		Custom_GT_Materials.add(r);
		Utils.LOG_INFO("Registered new Gregtech material - "+r.mName);
		return true;
	}	
	
	public final static Materials materialBuilder(int ID, TextureSet texture, int[] rgb, String materialName, Dyes dyeColour, List<MaterialStack> composition){
		Materials newMat = new Materials(
				ID,
				texture, 		
				1.0F,
				0,
				2,
				1 |8 ,
				rgb[0], rgb[1], rgb[2], 0,
				materialName, materialName, 
				0, 0, -1, 0, false, false, 3, 1, 1,
				dyeColour, 
				1, 
				composition
				);	
		registerMaterial(newMat);
		return newMat;
	}
	
	public final static Materials materialBuilder_Element(
			int ID,	TextureSet texture, 
			float a, int b, int c,
			int d, int r2, int g2, int b2, int a2,
			String materialName, String e,
			int f, int g, int h, int i,
			boolean j, boolean k,
			int l, int m, int n,
			Dyes dyeColour,	Element o,			
			List<TC_AspectStack> aspects){
		Materials newMat = new Materials(
				ID,	texture, 		
				a,	b,	c,
				d,	r2, g2,	b2, a2,
				materialName,	e, 
				f, g, h, i, j, k, l, m, n,
				dyeColour, 	o, 
				aspects
				);	
		registerMaterial(newMat);		
		return newMat;
	}


}
