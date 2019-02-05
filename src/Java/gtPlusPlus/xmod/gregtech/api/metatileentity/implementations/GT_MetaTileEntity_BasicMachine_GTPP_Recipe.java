package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import gregtech.api.interfaces.ITexture;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine_GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;

public class GT_MetaTileEntity_BasicMachine_GTPP_Recipe extends GT_MetaTileEntity_BasicMachine_GT_Recipe {
	
	public GT_MetaTileEntity_BasicMachine_GTPP_Recipe(int aID, String aName, String aNameRegional, int aTier,
			String aDescription, GT_Recipe_Map aRecipes, int aInputSlots, int aOutputSlots, int aTankCapacity,
			int aGUIParameterA, int aGUIParameterB, String aGUIName, String aSound, boolean aSharedTank,
			boolean aRequiresFluidForFiltering, int aSpecialEffect, String aOverlays, Object[] aRecipe) {
		super(aID, aName, aNameRegional, aTier, aDescription, aRecipes, aInputSlots, aOutputSlots, aTankCapacity,
				aGUIParameterA, aGUIParameterB, aGUIName, aSound, aSharedTank, aRequiresFluidForFiltering, aSpecialEffect,
				aOverlays, aRecipe);
	}

	public GT_MetaTileEntity_BasicMachine_GTPP_Recipe(String aName, int aTier, String aDescription,
			GT_Recipe_Map aRecipes, int aInputSlots, int aOutputSlots, int aTankCapacity, int aAmperage,
			int aGUIParameterA, int aGUIParameterB, ITexture[][][] aTextures, String aGUIName, String aNEIName,
			String aSound, boolean aSharedTank, boolean aRequiresFluidForFiltering, int aSpecialEffect) {
		super(aName, aTier, aDescription, aRecipes, aInputSlots, aOutputSlots, aTankCapacity, aAmperage, aGUIParameterA,
				aGUIParameterB, aTextures, aGUIName, aNEIName, aSound, aSharedTank, aRequiresFluidForFiltering, aSpecialEffect);
	}
	
	public GT_MetaTileEntity_BasicMachine_GTPP_Recipe(String aName, int aTier, String[] aDescription,
			GT_Recipe_Map aRecipes, int aInputSlots, int aOutputSlots, int aTankCapacity, int aAmperage,
			int aGUIParameterA, int aGUIParameterB, ITexture[][][] aTextures, String aGUIName, String aNEIName,
			String aSound, boolean aSharedTank, boolean aRequiresFluidForFiltering, int aSpecialEffect) {
		super(aName, aTier, aDescription, aRecipes, aInputSlots, aOutputSlots, aTankCapacity, aAmperage, aGUIParameterA,
				aGUIParameterB, aTextures, aGUIName, aNEIName, aSound, aSharedTank, aRequiresFluidForFiltering, aSpecialEffect);
	}

	

	

}
