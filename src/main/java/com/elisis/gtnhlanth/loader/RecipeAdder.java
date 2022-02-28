package com.elisis.gtnhlanth.loader;

import java.util.Collection;
import java.util.HashSet;

import gregtech.api.util.GT_Recipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

public class RecipeAdder {
	
	

	public static final RecipeAdder instance = new RecipeAdder();
	
	public final DigestMap DigesterRecipes = new DigestMap(
			new HashSet<>(100),
			"gtnhlanth.recipe.digester",
			StatCollector.translateToLocal("tile.recipe.digester"),
			null,
			"gtnhlanth:textures/gui/Digester",
			1, 1, 1, 1, 1,
			StatCollector.translateToLocal("value.digester") + ": ", //Heat Capacity
			1,
			"K",
			false,
			true	
		);
	
	public final DissolutionTankMap DissolutionTankRecipes = new DissolutionTankMap(
			new HashSet<>(100),
			"gtnhlanth.recipe.disstank",
			StatCollector.translateToLocal("tile.recipe.disstank"),
			null,
			"gtnhlanth:textures/gui/Disstank",
			1, 1, 1, 1, 1,
			StatCollector.translateToLocal("value.disstank") + ": ",
			1,
			":1",
			false,
			true			
		);
	
	
	public class DigestMap extends GT_Recipe.GT_Recipe_Map {

		public DigestMap(Collection<GT_Recipe> aRecipeList, 
					String aUnlocalizedName, String aLocalName, 
					String aNEIName, String aNEIGUIPath, 
					int aUsualInputCount, int aUsualOutputCount, 
					int aMinimalInputItems, int aMinimalInputFluids, 
					int aAmperage, String aNEISpecialValuePre, 
					int aNEISpecialValueMultiplier, String aNEISpecialValuePost, 
					boolean aShowVoltageAmperageInNEI, boolean aNEIAllowed) {
						super(aRecipeList, aUnlocalizedName, aLocalName, aNEIName, aNEIGUIPath, aUsualInputCount, aUsualOutputCount,
								aMinimalInputItems, aMinimalInputFluids, aAmperage, aNEISpecialValuePre, aNEISpecialValueMultiplier,
									aNEISpecialValuePost, aShowVoltageAmperageInNEI, aNEIAllowed);
			
					}
		
		public void addDigesterRecipe(FluidStack[] fluidInputs, ItemStack[] itemInputs, FluidStack fluidOutput, ItemStack[] itemOutputs, int EUt, int ticks, int heat){
            super.addRecipe(false, itemInputs, itemOutputs, null, fluidInputs, new FluidStack[]{fluidOutput}, ticks, EUt, heat);
        }
		

	}
	
	public class DissolutionTankMap extends GT_Recipe.GT_Recipe_Map {
		
		public DissolutionTankMap(Collection<GT_Recipe> aRecipeList,
				String aUnlocalizedName, String aLocalName, 
				String aNEIName, String aNEIGUIPath, 
				int aUsualInputCount, int aUsualOutputCount, 
				int aMinimalInputItems, int aMinimalInputFluids, 
				int aAmperage, String aNEISpecialValuePre, 
				int aNEISpecialValueMultiplier, String aNEISpecialValuePost, 
				boolean aShowVoltageAmperageInNEI, boolean aNEIAllowed) {
					super(aRecipeList, aUnlocalizedName, aLocalName, aNEIName, aNEIGUIPath, aUsualInputCount, aUsualOutputCount,
							aMinimalInputItems, aMinimalInputFluids, aAmperage, aNEISpecialValuePre, aNEISpecialValueMultiplier,
								aNEISpecialValuePost, aShowVoltageAmperageInNEI, aNEIAllowed);
		
				}
		/** Higher part input fluid first, always **/
		public void addDissolutionTankRecipe(FluidStack[] fluidInputs, ItemStack[] itemInputs, FluidStack fluidOutput, ItemStack[] itemOutputs, int EUt, int ticks, int ratio) {
			super.addRecipe(false, itemInputs, itemOutputs, null, fluidInputs, new FluidStack[] {fluidOutput}, ticks, EUt, ratio);
		}
		
	}

}
