package gtPlusPlus.core.util.minecraft;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.reflect.FieldUtils;

import gregtech.api.enums.*;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.data.EnumUtils;
import gtPlusPlus.core.util.data.StringUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class MaterialUtils {

	public static short firstID = 791;

	@SuppressWarnings({ "rawtypes", "unused" })
	private static Class[][] commonTypes =
{{Materials.class, int.class, TextureSet.class, float.class, int.class,
	int.class, int.class, int.class, int.class, int.class, int.class,
	String.class, int.class, int.class, int.class, int.class, boolean.class,
	boolean.class, int.class, int.class, int.class, Dyes.class, int.class,
	List.class , List.class}};

	public static List<?> oreDictValuesForEntry(final String oredictName){
		List<?> oredictItemNames;
		if(OreDictionary.doesOreNameExist(oredictName)){
			final List<ItemStack> oredictItems = OreDictionary.getOres(oredictName);
			oredictItemNames = oredictItems;
			return oredictItemNames;
		}
		return null;
	}

	public static Material generateMaterialFromGtENUM(final Materials material){
		@SuppressWarnings("deprecation")
		String name = material.name();
		final short[] rgba = material.mRGBa;
		final int melting = material.mMeltingPoint;
		final int boiling = material.mBlastFurnaceTemp;
		final long protons = material.getProtons();
		final long neutrons = material.getNeutrons();
		final boolean blastFurnace = material.mBlastFurnaceRequired;
		final TextureSet iconSet = material.mIconSet;
		final int durability = material.mDurability;
		boolean mGenerateCell = false;
		MaterialState materialState;
		String chemicalFormula = StringUtils.subscript(Utils.sanitizeString(material.mChemicalFormula));
		final Element element = material.mElement;
		int radioactivity = 0;
		if (material.isRadioactive()){
			radioactivity = 1;
		}
		
		//Weird Blacklist of Bad Chemical Strings
		if (material.mElement == Element.Pb || material.mElement == Element.Na || material.mElement == Element.Ar){
			chemicalFormula = StringUtils.subscript(Utils.sanitizeString(material.mElement.name()));
		}
		
		//Determine default state
		Logger.MATERIALS("[Debug] Setting State of GT generated material. "+material.mDefaultLocalName);
		if (material.getMolten(1) != null || material.getSolid(1) != null){
			materialState = MaterialState.SOLID;
			Logger.MATERIALS("[Debug] Molten or Solid was not null.");
			if (material.getMolten(1) == null && material.getSolid(1) != null){
				Logger.MATERIALS("[Debug] Molten is Null, Solid is not. Enabling cell generation.");
				mGenerateCell = true;
			}
			else if (material.getMolten(1) != null && material.getSolid(1) == null){
				Logger.MATERIALS("[Debug] Molten is not Null, Solid is null. Not enabling cell generation.");
				//mGenerateCell = true;
			}
			Logger.MATERIALS("[Debug] State set as solid.");
		}
		else if (material.getFluid(1) != null){
			Logger.MATERIALS("[Debug] State set as liquid.");
			materialState = MaterialState.LIQUID;
		}
		else if (material.getGas(1) != null){
			Logger.MATERIALS("[Debug] State set as gas.");
			materialState = MaterialState.GAS;
		}/*
		else if (material.getPlasma(1) != null){
			Logger.MATERIALS("[Debug] State set as plasma.");
			materialState = MaterialState.PLASMA;
		}*/
		else {
			Logger.MATERIALS("[Debug] State set as solid.");
			materialState = MaterialState.SOLID;
		}


		if (name.toLowerCase().contains("infused")){
			final String tempname = name.substring(7, name.length());
			name = "Infused " + tempname;
		}
		if (hasValidRGBA(rgba) || (element == Element.H) || ((material == Materials.InfusedAir) || (material == Materials.InfusedFire) || (material == Materials.InfusedEarth) || (material == Materials.InfusedWater))){
			//ModItems.itemBaseDecidust = UtilsItems.generateDecidust(material);
			//ModItems.itemBaseCentidust = UtilsItems.generateCentidust(material);
			return new Material(name, materialState,iconSet, durability, rgba, melting, boiling, protons, neutrons, blastFurnace, chemicalFormula, radioactivity, mGenerateCell);
		}
		else {
			Logger.DEBUG_MATERIALS("Failed to generate GT++ material instance for "+material.name() +" | Valid RGB? "+(hasValidRGBA(rgba)));
		}
		return null;

	}

	public static Material generateQuickMaterial(final String materialName, final MaterialState defaultState, final short[] colour, final int sRadioactivity) {
		final Material temp = new Material(
				materialName,
				defaultState,
				0, //Durability
				colour,
				1000, //melting
				3000, //boiling
				50, //Protons
				50, //Neutrons
				false,
				"",
				sRadioactivity);
		return temp;
	}

	public static boolean hasValidRGBA(final short[] rgba){		
		if (rgba == null || rgba.length < 3 || rgba.length > 4){
			return false;
		}		
		return true;
	}

	public static int getTierOfMaterial(final int M){
		if ((M >= 0) && (M <= 1000)){
			return 1;
		}
		else if((M >= 1001) && (M <= 2000)){
			return 2;
		}
		else if((M >= 2001) && (M <= 3000)){
			return 3;
		}
		else if((M >= 3001) && (M <= 4000)){
			return 4;
		}
		else if((M >= 4001) && (M <= 5000)){
			return 5;
		}
		else if((M >= 5001) && (M <= 6000)){
			return 6;
		}
		else if((M >= 6001) && (M <= 7000)){
			return 7;
		}
		else if((M >= 7001) && (M <= 8000)){
			return 8;
		}
		else if((M >= 8001) && (M <= 9000)){
			return 9;
		}
		else if((M >= 9001) && (M <= 9999)){
			return 10;
		}
		else {
			return 0;
		}
	}

	public static Materials getMaterialByName(String materialName) {
		
		if (!CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK) {
			return (Materials) EnumUtils.getValue(gregtech.api.enums.Materials.class, materialName, false);			
		}
		else {
			for (Materials m : Materials.values()) {
				if (MaterialUtils.getMaterialName(m).toLowerCase().equals(materialName.toLowerCase())) {
					return m;
				}
			}
			return null;
		}
	}
	
	@SuppressWarnings("deprecation")
	public static String getMaterialName(Materials mat){
		String mName;
		try {
			mName = (String) FieldUtils.getDeclaredField(Materials.class, "mName", true).get(mat);
		}
		catch (IllegalArgumentException | IllegalAccessException e) {
			mName = mat.name();
		}
		if (mName == null || mName.equals("") || mName == ""){
			mName = mat.name();
		}
		return mName;
	}
	
	public static TextureSet getMostCommonTextureSet(List<Material> list) {
		Optional<TextureSet> r = list.stream().map(Material::getTextureSet).collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey);
		TextureSet o =  (r != null && r.isPresent() && r.get() != null) ? r.get() : null;
		return o;
		}
	
	
	}


