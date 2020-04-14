package gtPlusPlus.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import gregtech.api.util.CustomRecipeMap;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.GTPP_Recipe;
import gregtech.api.util.GTPP_Recipe.GTPP_Recipe_Map;
import gregtech.nei.GT_NEI_DefaultHandler;
import gtPlusPlus.api.objects.data.AutoMap;

public class NEI_GT_Config
implements IConfigureNEI {
	public static boolean sIsAdded = true;
	
	private static final AutoMap<String> mUniqueRecipeMapHandling = new AutoMap<String>();

	@Override
	public synchronized void loadConfig() {
		sIsAdded = false;

		mUniqueRecipeMapHandling.add(GTPP_Recipe_Map.sChemicalPlantRecipes.mUnlocalizedName);
		mUniqueRecipeMapHandling.add(GTPP_Recipe_Map.sOreMillRecipes.mUnlocalizedName);
		mUniqueRecipeMapHandling.add(GTPP_Recipe_Map.sFlotationCellRecipes.mUnlocalizedName);
		mUniqueRecipeMapHandling.add(GTPP_Recipe_Map.sVacuumFurnaceRecipes.mUnlocalizedName);
		
		// Custom Recipe Maps
		for (final CustomRecipeMap tMap : CustomRecipeMap.sMappings) {
			if (tMap.mNEIAllowed) {
				new GTPP_NEI_CustomMapHandler(tMap);
			}
		}
		
		// Custom Recipe maps
		for (final GTPP_Recipe_Map tMap : GTPP_Recipe.GTPP_Recipe_Map.sMappings) {
			if (tMap.mNEIAllowed) {				
				if (!mUniqueRecipeMapHandling.contains(tMap.mUnlocalizedName)) {
					new GTPP_NEI_DefaultHandler(tMap);					
				}				
			}
		}
		
		// Standard GT Recipe Maps
		for (final GT_Recipe_Map tMap : GTPP_Recipe.GTPP_Recipe_Map_Internal.sMappingsEx) {
			if (tMap.mNEIAllowed) {				
				if (!mUniqueRecipeMapHandling.contains(tMap.mUnlocalizedName)) {
					new GT_NEI_DefaultHandler(tMap);					
				}				
			}
		}
		new GT_NEI_FluidReactor();
		new GT_NEI_MillingMachine();
		new GT_NEI_FlotationCell();
		new GT_NEI_VacFurnace();
		sIsAdded = true;
		API.registerRecipeHandler(new DecayableRecipeHandler());
		API.registerUsageHandler(new DecayableRecipeHandler());
	}

	@Override
	public String getName() {
		return "GT++ NEI Plugin";
	}

	@Override
	public String getVersion() {
		return "(1.01)";
	}
}
