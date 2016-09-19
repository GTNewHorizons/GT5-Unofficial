package gtPlusPlus.core.util.materials;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TC_Aspects.TC_AspectStack;
import gregtech.api.enums.TextureSet;
import gregtech.api.objects.MaterialStack;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.oredict.OreDictionary;

public class MaterialUtils {
	
	public static short firstID = 791;

	private static Class[][] commonTypes =
		{{Materials.class, int.class, TextureSet.class, float.class, int.class,
			int.class, int.class, int.class, int.class, int.class, int.class,
			String.class, int.class, int.class, int.class, int.class, boolean.class,
			boolean.class, int.class, int.class, int.class, Dyes.class, int.class,
			List.class , List.class}};
	
	public static Materials addGtMaterial(String enumNameForMaterial, TextureSet aIconSet, float aToolSpeed, int aToolDurability, int aToolQuality, int aTypes, int aR, int aG, int aB, int aA, String aLocalName, int aFuelType, int aFuelPower, int aMeltingPoint, int aBlastFurnaceTemp, boolean aBlastFurnaceRequired, boolean aTransparent, int aOreValue, int aDensityMultiplier, int aDensityDivider, Dyes aColor, int aExtraData, List<MaterialStack> aMaterialList, List<TC_AspectStack> aAspects)
    {
		Utils.LOG_INFO("Attempting to add GT material: "+enumNameForMaterial);
		return EnumHelper.addEnum(Materials.class, enumNameForMaterial, commonTypes, firstID++, aIconSet, aToolSpeed, aToolDurability, aToolQuality, aTypes, aR, aG, aB, aA, aLocalName,
				aFuelType, aFuelPower, aMeltingPoint, aBlastFurnaceTemp, aBlastFurnaceRequired, aTransparent, aOreValue, aDensityMultiplier, aDensityDivider,
				aColor, aExtraData, aMaterialList, aAspects);
		}
	
	public static List<?> oreDictValuesForEntry(String oredictName){
		List<?> oredictItemNames;
		if(OreDictionary.doesOreNameExist(oredictName)){
			ArrayList<ItemStack> oredictItems = OreDictionary.getOres(oredictName);
			oredictItemNames = Utils.convertArrayListToList(oredictItems);
			return oredictItemNames;
		}		
		return null;
	}
	
	public static Material generateMaterialFromGtENUM(Materials material){
		String name = material.name();
		short[] rgba = material.mRGBa;
		int melting = material.mMeltingPoint;
		int boiling = material.mBlastFurnaceTemp;
		long protons = material.getProtons();
		long neutrons = material.getNeutrons();
		boolean blastFurnace = material.mBlastFurnaceRequired;		
		return new Material(name, rgba, melting, boiling, protons, neutrons, blastFurnace, null);
	}
	

	/*
	 * That's shown, many times, in the EnumHelper code, all the add functions just wrap the addEnum function.
	You need the target enum class, and 2 arrays, 1 holding the types for the constructor arguments, 
	and the other holding the constructor argument values (the things being passed to the constructor)

	The 'decompiled' Boolean should be set to true if the class you're adding to has been decompiled/recompiled again. 
	(it adds a 2nd enum arguments that need to be accounted for, but isn't actually useful to you) 

	 *
	 *new Class[{int.class, TextureSet.class, float.class, int.class,
	int.class, int.class, int.class, int.class, int.class, int.class,
	String.class, int.class, int.class, int.class, int.class, boolean.class,
	boolean.class, int.class, int.class, int.class, Dyes.class, int.class,
					List.class , List.class}],
	 */

	

	/*public static Materials GenerateGtMaterialForSingleUse(MaterialInfo s){

		Materials yourName = EnumHelper.addEnum(

				Materials.class, s.name(),



				new Object[0]

				);
		try
		{



			Class<? extends ItemCell> clz = item.getClass();
			Method methode = clz.getDeclaredMethod("addCell", int.class, InternalName.class, Block[].class);
			methode.setAccessible(true);
			ItemStack temp = (ItemStack) methode.invoke(item, cellID++, yourName, new Block[0]);






			return Materials.Abyssal;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}*/

}
