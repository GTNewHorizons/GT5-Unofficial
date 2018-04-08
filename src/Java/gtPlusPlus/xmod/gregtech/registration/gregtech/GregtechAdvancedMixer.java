package gtPlusPlus.xmod.gregtech.registration.gregtech;

import java.util.Collection;

import gregtech.api.enums.ItemList;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine_GT_Recipe;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.Recipe_GT;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class GregtechAdvancedMixer {

	private static int mID = 851;

	public static void run(){
		generateMixerRecipeMap();
		run1();
	}

	private static boolean generateMixerRecipeMap(){
		if (Recipe_GT.Gregtech_Recipe_Map.sAdvancedMixerRecipes.mRecipeList.size() == 0){
			GT_Recipe_Map oldMap = GT_Recipe.GT_Recipe_Map.sMixerRecipes;
			GT_Recipe_Map map = Recipe_GT.Gregtech_Recipe_Map.sAdvancedMixerRecipes;
			final Collection<GT_Recipe> x = oldMap.mRecipeList;
			//Logger.INFO("Generating " + map.mUnlocalizedName + " Recipes.");
			for (final GT_Recipe g : x) {
				Recipe_GT.Gregtech_Recipe_Map.sAdvancedMixerRecipes.add(g);
			}

			if (Recipe_GT.Gregtech_Recipe_Map.sAdvancedMixerRecipes.mRecipeList.size() > 0){
				return true;
			}
			else {
				Logger.INFO("Failed to generate "+ map.mUnlocalizedName +" recipes.");
				return false;
			}

		}		
		return true;
	}

	private static void run1(){
		GregtechItemList.Machine_Advanced_LV_Mixer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(mID++, "advancedmachine.mixer.tier.01",
				"Basic Combiner", 1, "Indeed, It does blend!", Recipe_GT.Gregtech_Recipe_Map.sAdvancedMixerRecipes, 4, 4, 32000, 0, 1,
				"MixerAdvanced.png", "", false, false, 0, "MIXER",
				new Object[] { "GRG", "GEG", "CMC", Character.valueOf('M'),
						ItemList.Machine_LV_Mixer, Character.valueOf('E'),
						GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, Character.valueOf('R'),
						GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, Character.valueOf('C'),
						GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, Character.valueOf('G'),
						GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE }).getStackForm(1L));
		GregtechItemList.Machine_Advanced_MV_Mixer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(mID++, "advancedmachine.mixer.tier.02",
				"Advanced Combiner I", 2, "Indeed, It does blend!", Recipe_GT.Gregtech_Recipe_Map.sAdvancedMixerRecipes, 4, 4, 32000, 0, 1,
				"MixerAdvanced.png", "", false, false, 0, "MIXER",
				new Object[] { "GRG", "GEG", "CMC", Character.valueOf('M'),
						ItemList.Machine_MV_Mixer, Character.valueOf('E'),
						GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, Character.valueOf('R'),
						GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, Character.valueOf('C'),
						GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, Character.valueOf('G'),
						GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE }).getStackForm(1L));
		GregtechItemList.Machine_Advanced_HV_Mixer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(mID++, "advancedmachine.mixer.tier.03",
				"Advanced Combiner II", 3, "Indeed, It does blend!", Recipe_GT.Gregtech_Recipe_Map.sAdvancedMixerRecipes, 4, 4, 32000, 0, 1,
				"MixerAdvanced.png", "", false, false, 0, "MIXER",
				new Object[] { "GRG", "GEG", "CMC", Character.valueOf('M'),
						ItemList.Machine_HV_Mixer, Character.valueOf('E'),
						GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, Character.valueOf('R'),
						GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, Character.valueOf('C'),
						GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, Character.valueOf('G'),
						GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE }).getStackForm(1L));
		GregtechItemList.Machine_Advanced_EV_Mixer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(mID++, "advancedmachine.mixer.tier.04",
				"Super Combiner I", 4, "Indeed, It does blend!", Recipe_GT.Gregtech_Recipe_Map.sAdvancedMixerRecipes, 4, 4, 32000, 0, 1,
				"MixerAdvanced.png", "", false, false, 0, "MIXER",
				new Object[] { "GRG", "GEG", "CMC", Character.valueOf('M'),
						ItemList.Machine_EV_Mixer, Character.valueOf('E'),
						GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, Character.valueOf('R'),
						GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, Character.valueOf('C'),
						GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, Character.valueOf('G'),
						GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE }).getStackForm(1L));
		GregtechItemList.Machine_Advanced_IV_Mixer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(mID++, "advancedmachine.mixer.tier.05",
				"Super Combiner II", 5, "Indeed, It does blend!", Recipe_GT.Gregtech_Recipe_Map.sAdvancedMixerRecipes, 4, 4, 32000, 0, 1,
				"MixerAdvanced.png", "", false, false, 0, "MIXER",
				new Object[] { "GRG", "GEG", "CMC", Character.valueOf('M'),
						ItemList.Machine_IV_Mixer, Character.valueOf('E'),
						GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, Character.valueOf('R'),
						GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, Character.valueOf('C'),
						GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, Character.valueOf('G'),
						GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE }).getStackForm(1L));

		if (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK){
			try {
				GregtechItemList.Machine_Advanced_LuV_Mixer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(mID++, "advancedmachine.mixer.tier.06",
						"Mega Combiner I", 6, "Indeed, It does blend!", Recipe_GT.Gregtech_Recipe_Map.sAdvancedMixerRecipes, 4, 4, 32000, 0, 1,
						"MixerAdvanced.png", "", false, false, 0, "MIXER",
						new Object[] { "GRG", "GEG", "CMC", Character.valueOf('M'),
								//ItemList.Machine_LuV_Mixer, Character.valueOf('E'),
								ItemList.valueOf("Machine_LuV_Mixer"), Character.valueOf('E'),
								GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, Character.valueOf('R'),
								GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, Character.valueOf('C'),
								GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, Character.valueOf('G'),
								GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE }).getStackForm(1L));
				GregtechItemList.Machine_Advanced_ZPM_Mixer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(mID++, "advancedmachine.mixer.tier.07",
						"Mega Combiner II", 7, "Indeed, It does blend!", Recipe_GT.Gregtech_Recipe_Map.sAdvancedMixerRecipes, 4, 4, 32000, 0, 1,
						"MixerAdvanced.png", "", false, false, 0, "MIXER",
						new Object[] { "GRG", "GEG", "CMC", Character.valueOf('M'),
								//ItemList.Machine_ZPM_Mixer, Character.valueOf('E'),
								ItemList.valueOf("Machine_ZPM_Mixer"), Character.valueOf('E'),
								GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, Character.valueOf('R'),
								GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, Character.valueOf('C'),
								GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, Character.valueOf('G'),
								GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE }).getStackForm(1L));
				GregtechItemList.Machine_Advanced_UV_Mixer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(mID++, "advancedmachine.mixer.tier.08",
						"Ultra Combiner I", 8, "Indeed, It does blend!", Recipe_GT.Gregtech_Recipe_Map.sAdvancedMixerRecipes, 4, 4, 32000, 0, 1,
						"MixerAdvanced.png", "", false, false, 0, "MIXER",
						new Object[] { "GRG", "GEG", "CMC", Character.valueOf('M'),
								//ItemList.Machine_UV_Mixer, Character.valueOf('E'),
								ItemList.valueOf("Machine_UV_Mixer"), Character.valueOf('E'),
								GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, Character.valueOf('R'),
								GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, Character.valueOf('C'),
								GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, Character.valueOf('G'),
								GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE }).getStackForm(1L));
			}
			catch (Throwable t){}
		}

	}
}
