package gtPlusPlus.core.util.minecraft;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.Element;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TextureSet;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.data.TypeCounter;
import gtPlusPlus.core.client.CustomTextureSet.TextureSets;
import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.item.base.BaseItemComponent.ComponentTypes;
import gtPlusPlus.core.item.base.plates.BaseItemPlateHeavy;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialStack;
import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.data.EnumUtils;
import gtPlusPlus.core.util.data.StringUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.item.Item;
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

	private static Map<String, Material> mGeneratedMaterialMap = new HashMap();

	public static Material generateMaterialFromGtENUM(final Materials material){
		return generateMaterialFromGtENUM(material, null, null);
	}

	public static Material generateMaterialFromGtENUM(final Materials material, TextureSet aCustomTextures){
		return generateMaterialFromGtENUM(material, null, aCustomTextures);
	}

	public static Material generateMaterialFromGtENUM(final Materials material, short[] customRGB){
		return generateMaterialFromGtENUM(material, customRGB, null);
	}

	public static Material generateMaterialFromGtENUM(final Materials material, short[] customRGB, TextureSet aCustomTextures){
		String aMaterialKey = getMaterialName(material).toLowerCase();
		if (mGeneratedMaterialMap.containsKey(aMaterialKey)) {
			return mGeneratedMaterialMap.get(aMaterialKey);
		}

		try {
			@SuppressWarnings("deprecation")
			String name = material.name();
			final short[] rgba = (customRGB == null ? material.mRGBa : customRGB);
			final int melting = material.mMeltingPoint;
			final int boiling = material.mBlastFurnaceTemp;
			final long protons = material.getProtons();
			final long neutrons = material.getNeutrons();
			final boolean blastFurnace = material.mBlastFurnaceRequired;
			Integer radioactivity = 0;
			if (material.isRadioactive()){
				ItemStack aDustStack = ItemUtils.getOrePrefixStack(OrePrefixes.dust, material, 1);
				radioactivity = aDustStack != null ? GT_Utility.getRadioactivityLevel(aDustStack) : 0;
				if (radioactivity == 0) {
					long aProtons = material.getProtons();
					radioactivity = (int) Math.min(Math.max((aProtons / 30), 1), 9);				
				}
			}
			Logger.MATERIALS("[Debug] Calculated Radiation level to be "+radioactivity.intValue()+".");
			TextureSet iconSet = null;
			if (aCustomTextures == null) {
				if (material.isRadioactive()) {
					iconSet = TextureSets.NUCLEAR.get();
				}
				else {
					iconSet = material.mIconSet;
				}
			}
			else {
				iconSet = aCustomTextures;
			}
			if (iconSet == null || iconSet.mSetName.toLowerCase().contains("fluid")) {
				iconSet = TextureSet.SET_METALLIC;
			}
			Logger.MATERIALS("[Debug] Calculated Texture Set to be "+iconSet.mSetName+".");


			final int durability = material.mDurability;
			boolean mGenerateCell = false;
			boolean mGenerateFluid = true;
			MaterialState materialState;
			String chemicalFormula = StringUtils.subscript(Utils.sanitizeString(material.mChemicalFormula));
			final Element element = material.mElement;


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
				Material M = new Material(name, materialState,iconSet, durability, rgba, melting, boiling, protons, neutrons, blastFurnace, chemicalFormula, radioactivity.intValue(), mGenerateCell, mGenerateFluid);
				mGeneratedMaterialMap.put(aMaterialKey, M);
				return M;
			}
			else {
				Logger.DEBUG_MATERIALS("Failed to generate GT++ material instance for "+material.name() +" | Valid RGB? "+(hasValidRGBA(rgba)));
			}
		}
		catch (Throwable t) {	
			Logger.DEBUG_MATERIALS("Failed to generate GT++ material instance for "+material.name());		
			t.printStackTrace();
		}
		return null;

	}

	public static Material generateQuickMaterial(final String materialName, final MaterialState defaultState, final short[] colour, final int sRadioactivity) {
		String aMaterialKey = materialName.toLowerCase();
		if (mGeneratedMaterialMap.containsKey(aMaterialKey)) {
			return mGeneratedMaterialMap.get(aMaterialKey);
		}

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
		mGeneratedMaterialMap.put(aMaterialKey, temp);
		return temp;
	}

	public static boolean hasValidRGBA(final short[] rgba){
		if (rgba == null || rgba.length < 3 || rgba.length > 4){
			return false;
		}
		return true;
	}

	public static int getTierOfMaterial(final int aMeltingPoint){
		
		return aMeltingPoint < 1000 ? 0 : (Math.round(aMeltingPoint/1000));
		
		
		/*if ((aMeltingPoint >= 0) && (aMeltingPoint <= 1000)){
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
		}*/
	}

	public static int getVoltageForTier(int aTier) {
		//aTier += 1; - Probably some logic to this, idk.
		if (aTier <= 0) {
			return 16;
		} else if (aTier == 1) {
			return 30;
		} else if (aTier == 2) {
			return 120;
		} else if (aTier == 3) {
			return 480;
		} else if (aTier == 4) {
			return 1920;
		} else if (aTier == 5) {
			return 7680;
		} else if (aTier == 6) {
			return 30720;
		} else if (aTier == 7) {
			return 122880;
		} else if (aTier == 8) {
			return 491520;
		} else if (aTier == 9) {
			return 1966080;
		} else if (aTier == 10) {
			return 7864320;
		} else if (aTier == 11) {
			return 31457280;
		} else if (aTier == 12) {
			return 125829120;
		} else if (aTier == 13) {
			return 503316480;
		} else if (aTier == 14) {
			return 2013265920;
		} else {
			return Integer.MAX_VALUE;
		}
		
		/*else {
			int newTier = aTier - 1;
			return (int) ((4*(Math.pow(4, newTier)))*7.5);
		}*/
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
		catch (IllegalArgumentException | IllegalAccessException e) {
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
	


	public static Materials getMaterial(String aMaterialName, String aFallbackMaterialName) {
		Materials g = getMaterial(aMaterialName);
		if (g == null) {
			g = getMaterial(aFallbackMaterialName);
		}
		if (g == null) {
			Logger.INFO("Failed finding material '"+aMaterialName+"' & fallback '"+aFallbackMaterialName+"', returning _NULL.");
			g = Materials._NULL;
		}
		return g;		
	}

	public static Materials getMaterial(String aMaterialName) {
		Materials m = gtPlusPlus.xmod.gregtech.common.StaticFields59.getMaterial(aMaterialName);
		if (m == null) {
			m = getMaterialByName(aMaterialName);
		}
		if (m == null) {
			Logger.INFO("Failed finding material '"+aMaterialName+"', returning _NULL.");
			m = Materials._NULL;
		}
		return m;
	}

	public static AutoMap<Material> getCompoundMaterialsRecursively(Material aMat){		
		return getCompoundMaterialsRecursively_Speiger(aMat);		
		/*
		AutoMap<Material> aDataSet = new AutoMap<Material>();	
		final int HARD_LIMIT = 1000;
		int mLoopCounter = 0;
		if (aMat.getComposites().size() > 0) {
			try {
				List<Material> xList = Lists.newLinkedList();
				for (MaterialStack kj : aMat.getComposites()) {
					xList.add(kj.getStackMaterial());
				}
				if (xList.isEmpty()) {
					aDataSet.put(aMat);
					return aDataSet;
				}
				ListIterator<Material> listIterator = xList.listIterator();			
				while(listIterator.hasNext()){				
					Material e = listIterator.next();
					listIterator.remove();
					if (mLoopCounter > HARD_LIMIT) {
						break;
					}

					if (e.getComposites().isEmpty()) {
						aDataSet.put(e);
					}
					else {
						for (MaterialStack x : e.getComposites()) {
							listIterator.add(x.getStackMaterial());		
						}				
					}
					mLoopCounter++;


				}}
			catch (Throwable t) {
				aDataSet.put(aMat);
				t.printStackTrace();
			}
		}
		if (aDataSet.isEmpty()) {
			aDataSet.put(aMat);
			return aDataSet;
		}
		return aDataSet;	
	*/}

	public static AutoMap<Material> getCompoundMaterialsRecursively_Speiger(Material toSearch) {
		AutoMap<Material> resultList = new AutoMap<Material>();
		if (toSearch.getComposites().isEmpty()) {
			resultList.put(toSearch);
			return resultList;
		}
		final int HARD_LIMIT = 1000;
		
		// Could be a Deque but i dont use the interface
		// enough to use it as default.
		LinkedList<Material> toCheck = new LinkedList<Material>(); 		
		
		toCheck.add(toSearch);
		int processed = 0;
		while (toCheck.size() > 0 && processed < HARD_LIMIT) {
			Material current = toCheck.remove();
			if (current.getComposites().isEmpty()) {
				resultList.put(current);
			} else {
				for (MaterialStack entry : current.getComposites()) {
					toCheck.add(entry.getStackMaterial());
				}
			}
			processed++;
		}
		return resultList;
	}
	
	public static void generateComponentAndAssignToAMaterial(ComponentTypes aType, Material aMaterial) {
		generateComponentAndAssignToAMaterial(aType, aMaterial, true);
	}
	
	public static void generateComponentAndAssignToAMaterial(ComponentTypes aType, Material aMaterial, boolean generateRecipes) {		
		Item aGC;
		if (aType == ComponentTypes.PLATEHEAVY) {
			aGC = new BaseItemPlateHeavy(aMaterial);
		}
		else {
			aGC = new BaseItemComponent(aMaterial, aType);			
		}
		if (aGC != null) {
			String aFormattedLangName = aType.getName();
			
			if (!aFormattedLangName.startsWith(" ")) {
				if (aFormattedLangName.contains("@")) {
					String[] aSplit = aFormattedLangName.split("@");
					aFormattedLangName = aSplit[0] + " " + aMaterial.getLocalizedName() + " " + aSplit[1];
				}
			}
			
			if (aFormattedLangName.equals(aType.getName())) {
				aFormattedLangName = aMaterial.getLocalizedName() + aFormattedLangName;
				
			}
			
			
			
			Logger.MATERIALS("[Lang] "+aGC.getUnlocalizedName()+".name="+aFormattedLangName);
			aMaterial.registerComponentForMaterial(aType, ItemUtils.getSimpleStack(aGC));			
		}				
	}
	
	
	
	
	
	
	
	
	public static void generateSpecialDustAndAssignToAMaterial(Material aMaterial) {
		generateSpecialDustAndAssignToAMaterial(aMaterial, true);
	}

	public static void generateSpecialDustAndAssignToAMaterial(Material aMaterial, boolean generateMixerRecipes) {
		Item[] aDusts = ItemUtils.generateSpecialUseDusts(aMaterial, false, Utils.invertBoolean(generateMixerRecipes));
		if (aDusts != null && aDusts.length > 0) {
			aMaterial.registerComponentForMaterial(OrePrefixes.dust, ItemUtils.getSimpleStack(aDusts[0]));
			aMaterial.registerComponentForMaterial(OrePrefixes.dustSmall, ItemUtils.getSimpleStack(aDusts[1]));
			aMaterial.registerComponentForMaterial(OrePrefixes.dustTiny, ItemUtils.getSimpleStack(aDusts[2]));
		}
		
	}


}