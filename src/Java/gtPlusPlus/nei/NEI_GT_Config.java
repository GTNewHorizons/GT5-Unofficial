package gtPlusPlus.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import gregtech.api.util.CustomRecipeMap;
import gregtech.api.util.Recipe_GT.Gregtech_Recipe_Map;
import gtPlusPlus.api.objects.data.AutoMap;

public class NEI_GT_Config
implements IConfigureNEI {
	public static boolean sIsAdded = true;
	
	private static final AutoMap<String> mUniqueRecipeMapHandling = new AutoMap<String>();

	@Override
	public synchronized void loadConfig() {
		sIsAdded = false;

		mUniqueRecipeMapHandling.add(Gregtech_Recipe_Map.sChemicalPlantRecipes.mUnlocalizedName);
		mUniqueRecipeMapHandling.add(Gregtech_Recipe_Map.sOreMillRecipes.mUnlocalizedName);
		mUniqueRecipeMapHandling.add(Gregtech_Recipe_Map.sFlotationCellRecipes.mUnlocalizedName);
		
		for (final CustomRecipeMap tMap : gregtech.api.util.CustomRecipeMap.sMappings) {
			if (tMap.mNEIAllowed) {
				new GT_NEI_DefaultHandler(tMap);
			}
		}
		for (final Gregtech_Recipe_Map tMap : gregtech.api.util.Recipe_GT.Gregtech_Recipe_Map.sMappings) {
			if (tMap.mNEIAllowed) {				
				if (!mUniqueRecipeMapHandling.contains(tMap.mUnlocalizedName)) {
					new GT_NEI_MultiBlockHandler(tMap);					
				}				
			}
		}
		new GT_NEI_FluidReactor();
		new GT_NEI_MillingMachine();
		new GT_NEI_FlotationCell();
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
