package gtPlusPlus.core.util.minecraft;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.reflect.FieldUtils;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.*;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.TypeCounter;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.data.EnumUtils;
import gtPlusPlus.core.util.data.StringUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
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
		return generateMaterialFromGtENUM(material, null);
	}

	public static Material generateMaterialFromGtENUM(final Materials material, short[] customRGB){
		@SuppressWarnings("deprecation")
		String name = material.name();
		final short[] rgba = (customRGB == null ? material.mRGBa : customRGB);
		final int melting = material.mMeltingPoint;
		final int boiling = material.mBlastFurnaceTemp;
		final long protons = material.getProtons();
		final long neutrons = material.getNeutrons();
		final boolean blastFurnace = material.mBlastFurnaceRequired;
		final TextureSet iconSet = material.mIconSet;
		final int durability = material.mDurability;
		boolean mGenerateCell = false;
		boolean mGenerateFluid = true;
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
			Logger.MATERIALS("[Debug] State set as solid. This material has no alternative states, so for safety we wont generate anything.");
			materialState = MaterialState.SOLID;
			mGenerateFluid = false;
		}


		if (name.toLowerCase().contains("infused")){
			final String tempname = name.substring(7, name.length());
			name = "Infused " + tempname;
		}
		if (hasValidRGBA(rgba) || (element == Element.H) || ((material == Materials.InfusedAir) || (material == Materials.InfusedFire) || (material == Materials.InfusedEarth) || (material == Materials.InfusedWater))){
			//ModItems.itemBaseDecidust = UtilsItems.generateDecidust(material);
			//ModItems.itemBaseCentidust = UtilsItems.generateCentidust(material);
			return new Material(name, materialState,iconSet, durability, rgba, melting, boiling, protons, neutrons, blastFurnace, chemicalFormula, radioactivity, mGenerateCell, mGenerateFluid);
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

	public static int getTierOfMaterial(final int aMeltingPoint){
		if ((aMeltingPoint >= 0) && (aMeltingPoint <= 1000)){
			return 1;
		}
		else if((aMeltingPoint >= 1001) && (aMeltingPoint <= 2000)){
			return 2;
		}
		else if((aMeltingPoint >= 2001) && (aMeltingPoint <= 3000)){
			return 3;
		}
		else if((aMeltingPoint >= 3001) && (aMeltingPoint <= 4000)){
			return 4;
		}
		else if((aMeltingPoint >= 4001) && (aMeltingPoint <= 5000)){
			return 5;
		}
		else if((aMeltingPoint >= 5001) && (aMeltingPoint <= 6000)){
			return 6;
		}
		else if((aMeltingPoint >= 6001) && (aMeltingPoint <= 7000)){
			return 7;
		}
		else if((aMeltingPoint >= 7001) && (aMeltingPoint <= 8000)){
			return 8;
		}
		else if((aMeltingPoint >= 8001) && (aMeltingPoint <= 9000)){
			return 9;
		}
		else if((aMeltingPoint >= 9001) && (aMeltingPoint <= 9999)){
			return 10;
		}
		else {
			return 0;
		}
	}

	public static int getVoltageForTier(final int aTier) {
		if (aTier == 0) {
			return 16;
		}
		if (aTier == 1) {
			return 30;
		}
		if (aTier == 2) {
			return 120;
		}
		if (aTier == 3) {
			return 480;
		}
		if (aTier == 4) {
			return 1600;
		}
		if (aTier == 5) {
			return 6400;
		}
		if (aTier == 6) {
			return 25000;
		}
		if (aTier == 7) {
			return 100000;
		}
		if (aTier == 8) {
			return 400000;
		}
		if (aTier == 9) {
			return 1600000;
		}


		return 120;
	}

	private static Materials getMaterialByName(String materialName) {

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
		
		String mName = null;

		try {
			mName = (String) ReflectionUtils.getField(Materials.class, "mDefaultLocalName").get(mat);
			if (mName == null) {
				mName = (String) ReflectionUtils.getField(Materials.class, "mName").get(mat);
			}
		}
		catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException e) {
		}


		if (mName == null || mName.equals("")){
			mName = mat.name();
		}
		return mName;
	}

	public static TextureSet getMostCommonTextureSet(List<Material> list) {		
		TypeCounter<TextureSet> aCounter = new TypeCounter<TextureSet>(TextureSet.class);
		for (Material m : list) {
			TextureSet t = m.getTextureSet();
			if (t == null) {
				t = Materials.Gold.mIconSet;
			}
			if (t != null) {
				aCounter.add(t, t.mSetName);
			}			
		}
		return aCounter.getResults();
		/*Optional<TextureSet> r = list.stream().map(Material::getTextureSet).collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey);
		TextureSet o =  (r != null && r.isPresent() && r.get() != null) ? r.get() : null;
		return o;*/
	}

	public static Materials getMaterial(String aMaterialName) {
		Materials m = gtPlusPlus.xmod.gregtech.common.StaticFields59.getMaterial(aMaterialName);
		if (m == null) {
			m = getMaterialByName(aMaterialName);
		}
		return m;
	}


}