package gtPlusPlus.core.util.minecraft;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

import gregtech.api.enums.Materials;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;
import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.handler.COMPAT_HANDLER;
import gtPlusPlus.core.handler.Recipes.LateRegistrationHandler;
import gtPlusPlus.core.handler.Recipes.RegistrationHandler;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.recipe.common.CI;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class RecipeUtils {

	public static boolean recipeBuilder(final Object slot_1, final Object slot_2, final Object slot_3, final Object slot_4, final Object slot_5, final Object slot_6, final Object slot_7, final Object slot_8, final Object slot_9, final ItemStack resultItem){

		final ArrayList<Object> validSlots = new ArrayList<>();
		if (resultItem == null){
			Logger.WARNING("Found a recipe with an invalid output, yet had a valid inputs. Skipping.");
			return false;
		}

		if ((slot_1 == null) && (slot_2 == null) && (slot_3 == null) &&
				(slot_4 == null) && (slot_5 == null) && (slot_6 == null) &&
				(slot_7 == null) && (slot_8 == null) && (slot_9 == null)){
			Logger.WARNING("Found a recipe with 0 inputs, yet had a valid output.");
			Logger.WARNING("Error found while adding a recipe for: "+resultItem.getDisplayName()+" | Please report this issue on Github.");
			RegistrationHandler.recipesFailed++;
			return false;
		}

		//Utils.LOG_WARNING("Trying to add a recipe for "+resultItem.toString());
		String a,b,c,d,e,f,g,h,i;
		if (slot_1 == null){ a = " ";} else { a = "1";validSlots.add('1');validSlots.add(slot_1);}
		Logger.WARNING(a);
		if (slot_2 == null){ b = " ";} else { b = "2";validSlots.add('2');validSlots.add(slot_2);}
		Logger.WARNING(b);
		if (slot_3 == null){ c = " ";} else { c = "3";validSlots.add('3');validSlots.add(slot_3);}
		Logger.WARNING(c);
		if (slot_4 == null){ d = " ";} else { d = "4";validSlots.add('4');validSlots.add(slot_4);}
		Logger.WARNING(d);
		if (slot_5 == null){ e = " ";} else { e = "5";validSlots.add('5');validSlots.add(slot_5);}
		Logger.WARNING(e);
		if (slot_6 == null){ f = " ";} else { f = "6";validSlots.add('6');validSlots.add(slot_6);}
		Logger.WARNING(f);
		if (slot_7 == null){ g = " ";} else { g = "7";validSlots.add('7');validSlots.add(slot_7);}
		Logger.WARNING(g);
		if (slot_8 == null){ h = " ";} else { h = "8";validSlots.add('8');validSlots.add(slot_8);}
		Logger.WARNING(h);
		if (slot_9 == null){ i = " ";} else { i = "9";validSlots.add('9');validSlots.add(slot_9);}
		Logger.WARNING(i);


		Logger.ERROR("_______");
		final String lineOne = a+b+c;
		Logger.ERROR("|"+a+"|"+b+"|"+c+"|");
		Logger.ERROR("_______");
		final String lineTwo = d+e+f;
		Logger.ERROR("|"+d+"|"+e+"|"+f+"|");
		Logger.ERROR("_______");
		final String lineThree = g+h+i;
		Logger.ERROR("|"+g+"|"+h+"|"+i+"|");
		Logger.ERROR("_______");

		validSlots.add(0, lineOne);
		validSlots.add(1, lineTwo);
		validSlots.add(2, lineThree);
		boolean advancedLog = false;
		if (CORE.DEBUG){
			advancedLog = true;
		}
		if (advancedLog){
			int j = 0;
			final int l = validSlots.size();
			Logger.WARNING("l:"+l);
			while (j <= l) {
				Logger.WARNING("j:"+j);
				if (j <= 2){
					Logger.WARNING("ArrayList Values: "+validSlots.get(j));
					Logger.WARNING("Adding 1.");
					j++;
				}
				else if (j == l){
					Logger.WARNING("Done iteration.");
					break;
				}
				else {
					Logger.WARNING("ArrayList Values: '"+validSlots.get(j)+"' "+validSlots.get(j+1));
					if (j < (l-2)){
						Logger.WARNING("Adding 2.");
						j=j+2;
					}
					else {
						Logger.WARNING("Done iteration.");
						break;
					}
				}
				if ((validSlots.get(j) instanceof String) || (validSlots.get(j) instanceof ItemStack)){
					//Utils.LOG_WARNING("Is Valid: "+validSlots.get(j));
				}
			}
		}

		try {
			int size = COMPAT_HANDLER.mRecipesToGenerate.size();
			COMPAT_HANDLER.mRecipesToGenerate.put(new InternalRecipeObject(validSlots.toArray(), resultItem, false));

			//Utils.LOG_WARNING("Success! Added a recipe for "+resultItem.getDisplayName());
			if (COMPAT_HANDLER.mRecipesToGenerate.size() > size) {
				if (!COMPAT_HANDLER.areInitItemsLoaded){
					RegistrationHandler.recipesSuccess++;
				}
				else {
					LateRegistrationHandler.recipesSuccess++;
				}
				return true;
			}
			return false;
		}
		catch(RuntimeException k){
			//k.getMessage();
			//k.getClass();
			//k.printStackTrace();
			//k.getLocalizedMessage();
			Logger.INFO("@@@: Invalid Recipe detected for: "+resultItem.getUnlocalizedName());
			if (!COMPAT_HANDLER.areInitItemsLoaded){
				RegistrationHandler.recipesFailed++;
			}
			else {
				LateRegistrationHandler.recipesFailed++;
			}
			return false;
		}
	}

	public static void shapelessBuilder(final ItemStack Output, final Object slot_1, final Object slot_2, final Object slot_3, final Object slot_4, final Object slot_5, final Object slot_6, final Object slot_7, final Object slot_8, final Object slot_9){
		//Item output_ITEM = Output.getItem();

		final ArrayList<Object> validSlots = new ArrayList<>();

		Logger.WARNING("Trying to add a recipe for "+Output.toString());
		String a,b,c,d,e,f,g,h,i;
		if (slot_1 == null){ a = " ";} else { a = "1";validSlots.add('1');validSlots.add(slot_1);}
		Logger.WARNING(a);
		if (slot_2 == null){ b = " ";} else { b = "2";validSlots.add('2');validSlots.add(slot_2);}
		Logger.WARNING(b);
		if (slot_3 == null){ c = " ";} else { c = "3";validSlots.add('3');validSlots.add(slot_3);}
		Logger.WARNING(c);
		if (slot_4 == null){ d = " ";} else { d = "4";validSlots.add('4');validSlots.add(slot_4);}
		Logger.WARNING(d);
		if (slot_5 == null){ e = " ";} else { e = "5";validSlots.add('5');validSlots.add(slot_5);}
		Logger.WARNING(e);
		if (slot_6 == null){ f = " ";} else { f = "6";validSlots.add('6');validSlots.add(slot_6);}
		Logger.WARNING(f);
		if (slot_7 == null){ g = " ";} else { g = "7";validSlots.add('7');validSlots.add(slot_7);}
		Logger.WARNING(g);
		if (slot_8 == null){ h = " ";} else { h = "8";validSlots.add('8');validSlots.add(slot_8);}
		Logger.WARNING(h);
		if (slot_9 == null){ i = " ";} else { i = "9";validSlots.add('9');validSlots.add(slot_9);}
		Logger.WARNING(i);


		Logger.ERROR("_______");
		Logger.ERROR("|"+a+"|"+b+"|"+c+"|");
		Logger.ERROR("_______");
		Logger.ERROR("|"+d+"|"+e+"|"+f+"|");
		Logger.ERROR("_______");
		Logger.ERROR("|"+g+"|"+h+"|"+i+"|");
		Logger.ERROR("_______");

		validSlots.add(0, a);
		validSlots.add(1, b);
		validSlots.add(2, c);
		validSlots.add(3, d);
		validSlots.add(4, e);
		validSlots.add(5, f);
		validSlots.add(6, g);
		validSlots.add(7, h);
		validSlots.add(8, i);

		try {
			//GameRegistry.addRecipe(new ShapelessOreRecipe(Output, outputAmount), (Object[]) validSlots.toArray());
			GameRegistry.addRecipe(new ShapelessOreRecipe(Output, validSlots.toArray()));
			//GameRegistry.addShapelessRecipe(new ItemStack(output_ITEM, 1), new Object[] {slot_1, slot_2});
			Logger.WARNING("Success! Added a recipe for "+Output.getDisplayName());
			RegistrationHandler.recipesSuccess++;
		}
		catch(final RuntimeException k){
			k.getMessage();
			k.getClass();
			k.printStackTrace();
			k.getLocalizedMessage();
			Logger.WARNING("@@@: Invalid Recipe detected for: "+Output.getUnlocalizedName());
			RegistrationHandler.recipesFailed++;
		}


		//GameRegistry.addShapelessRecipe(new ItemStack(output_ITEM, 1), new Object[] {slot_1, slot_2});
	}

	public static void recipeBuilder(final Object[] array, final ItemStack outPut) {
		Logger.SPECIFIC_WARNING("object Array - recipeBuilder", "Attempting to build a recipe using an object array as an input, splitting it, then running the normal recipeBuilder() method.", 396);
		Object a=null;
		Object b=null;
		Object c=null;
		Object d=null;
		Object e=null;
		Object f=null;
		Object g=null;
		Object h=null;
		Object i=null;
		for(int z =0; z <= array.length; z++){
			array[z].toString();
			switch(z)
			{
			case 0:
				a = array[z];
				break;
			case 1:
				b = array[z];
				break;
			case 2:
				c = array[z];
				break;
			case 3:
				d = array[z];
				break;
			case 4:
				e = array[z];
				break;
			case 5:
				f = array[z];
				break;
			case 6:
				g = array[z];
				break;
			case 7:
				h = array[z];
				break;
			case 8:
				i = array[z];
				break;
			default:
				break;
			}
			recipeBuilder(a, b, c, d, e, f, g, h, i, outPut);
		}
	}

	public static boolean removeCraftingRecipe(Object x){
		if (null == x){return false;}
		if (x instanceof String){
			final Item R = ItemUtils.getItem((String) x);
			if (R != null){
				x = R;
			}
			else {
				return false;
			}
		}
		if ((x instanceof Item) || (x instanceof ItemStack)){
			if (x instanceof Item){
				final ItemStack r = new ItemStack((Item) x);
				Logger.WARNING("Removing Recipe for "+r.getUnlocalizedName());
			}
			else {
				Logger.WARNING("Removing Recipe for "+((ItemStack) x).getUnlocalizedName());
			}
			if (x instanceof ItemStack){
				final Item r = ((ItemStack) x).getItem();
				if (null != r){
					x = r;
				}
				else {
					Logger.WARNING("Recipe removal failed - Tell Alkalus.");
					return false;
				}
			}
			if (RecipeUtils.attemptRecipeRemoval((Item) x)){
				Logger.WARNING("Recipe removal successful");
				return true;
			}
			Logger.WARNING("Recipe removal failed - Tell Alkalus.");
			return false;
		}
		return false;
	}

	private static boolean attemptRecipeRemoval(final Item I){
		Logger.WARNING("Create list of recipes.");
		final List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		final Iterator<IRecipe> items = recipes.iterator();
		Logger.WARNING("Begin list iteration.");
		while (items.hasNext()) {
			final ItemStack is = items.next().getRecipeOutput();
			if ((is != null) && (is.getItem() == I)){
				items.remove();
				Logger.WARNING("Remove a recipe with "+I.getUnlocalizedName()+" as output.");
				continue;
			}
		}
		Logger.WARNING("All recipes should be gone?");
		if (!items.hasNext()){
			Logger.WARNING("We iterated once, let's try again to double check.");
			final Iterator<IRecipe> items2 = recipes.iterator();
			while (items2.hasNext()) {
				final ItemStack is = items2.next().getRecipeOutput();
				if ((is != null) && (is.getItem() == I)){
					items.remove();
					Logger.WARNING("REMOVING MISSED RECIPE - RECHECK CONSTRUCTORS");
					return true;
				}
			}
			Logger.WARNING("Should be all gone now after double checking, so return true.");
			return true;
		}
		Logger.WARNING("Return false, because something went wrong.");
		return false;
	}



	public static boolean addShapedGregtechRecipeForTypes(
			final Object InputItem1, final Object InputItem2, final Object InputItem3,
			final Object InputItem4, final Object InputItem5, final Object InputItem6,
			final Object InputItem7, final Object InputItem8, final Object InputItem9,
			final ItemStack OutputItem){

		int using = 0, recipeSlotCurrent = 0;
		boolean[] hasMultiStack = new boolean[9];
		boolean inUse[] = {false, false, false};		
		ItemStack array[][] = new ItemStack[3][9];

		Object[] inputs = {
				InputItem1, InputItem2, InputItem3,
				InputItem4, InputItem5, InputItem6, 
				InputItem7, InputItem8, InputItem9};

		for (Object o : inputs){
			if (o.getClass().isArray()){
				if (inUse[using] == false){
					inUse[using] = true;
					array[using] = (ItemStack[]) o;
					hasMultiStack[recipeSlotCurrent] = true;
					using++;					
				}
			}
			else {
				hasMultiStack[recipeSlotCurrent] = false;
			}
			recipeSlotCurrent++;
		}

		int using2 = 0;
		for (boolean t : inUse){

			if (t){
				if (array[using2] != null){
					//addShapedGregtechRecipe
				}
			}
			using2++;
		}


		return false;
	}



	public static boolean addShapedGregtechRecipe(
			final Object InputItem1, final Object InputItem2, final Object InputItem3,
			final Object InputItem4, final Object InputItem5, final Object InputItem6,
			final Object InputItem7, final Object InputItem8, final Object InputItem9,
			final ItemStack OutputItem){		

		Object[] o = {
				InputItem1, InputItem2, InputItem3,
				InputItem4, InputItem5, InputItem6,
				InputItem7, InputItem8, InputItem9
		};

		int size = COMPAT_HANDLER.mGtRecipesToGenerate.size();
		COMPAT_HANDLER.mGtRecipesToGenerate.put(new InternalRecipeObject(o, OutputItem, true));

		if (COMPAT_HANDLER.mGtRecipesToGenerate.size() > size) {
			if (!COMPAT_HANDLER.areInitItemsLoaded){
				RegistrationHandler.recipesSuccess++;
			}
			else {
				LateRegistrationHandler.recipesSuccess++;
			}
			return true;
		}
		return false;	

	}

	public static boolean addShapedGregtechRecipe(final Object[] inputs, ItemStack output){

		if (inputs.length != 9){
			Logger.WARNING("Input array for "+output.getDisplayName()+" does not equal 9. "+inputs.length+" is the actual size.");

			RegistrationHandler.recipesFailed++;
			return false;
		}

		for (int x=0;x<9;x++){
			if (inputs[x] == null){
				inputs[x] = " ";
				Logger.WARNING("Input slot "+x+" changed from NULL to a blank space.");
			}
			else if (!(inputs[x] instanceof ItemStack) && !(inputs[x] instanceof String)){
				if (output != null){
					Logger.WARNING("Invalid Item inserted into inputArray. Item:"+output.getDisplayName()+" has a bad recipe. Please report to Alkalus.");

					RegistrationHandler.recipesFailed++;
					return false;
				}
				else {
					Logger.WARNING("Output is Null for a recipe. Report to Alkalus.");
					output = ItemUtils.getItemStackOfAmountFromOreDict("sadibasdkjnad", 1);
				}
			}
		}


		int size = COMPAT_HANDLER.mGtRecipesToGenerate.size();
		COMPAT_HANDLER.mGtRecipesToGenerate.put(new InternalRecipeObject(inputs, output, true));

		if (COMPAT_HANDLER.mGtRecipesToGenerate.size() > size) {
			if (!COMPAT_HANDLER.areInitItemsLoaded){
				RegistrationHandler.recipesSuccess++;
			}
			else {
				LateRegistrationHandler.recipesSuccess++;
			}
			return true;
		}
		return false;		
	}

	public static boolean addShapelessGregtechRecipe(final Object[] inputItems, final ItemStack OutputItem){
		//Catch Invalid Recipes
		if (inputItems.length > 9 || inputItems.length < 1){
			if (OutputItem != null){
				Logger.WARNING("Invalid input array for shapeless recipe, which should output "+OutputItem.getDisplayName());
			}
			return false;
		}
		//let gregtech handle shapeless recipes.
		if (GT_ModHandler.addShapelessCraftingRecipe(OutputItem, inputItems)){
			return true;
		}
		return false;
	}

	public static ItemStack getItemStackFromOreDict(final String oredictName){
		final ArrayList<ItemStack> oreDictList = OreDictionary.getOres(oredictName);
		return oreDictList.get(0);
	}

	public static boolean buildShapelessRecipe(final ItemStack output, final Object[] input){
		return ShapelessUtils.addShapelessRecipe(output, input);
	}

	public static boolean generateMortarRecipe(ItemStack aStack, ItemStack aOutput) {
		return RecipeUtils.addShapedGregtechRecipe(
				aStack, null, null,
				CI.craftingToolMortar, null, null,
				null, null, null,
				aOutput);
	}

	public static boolean doesGregtechRecipeHaveEqualCells(GT_Recipe x) {
		if (x.mInputs.length == 0 && x.mOutputs.length == 0) {
			return true;
		}

		final int tInputAmount = GT_ModHandler.getCapsuleCellContainerCountMultipliedWithStackSize(x.mInputs);
		final int tOutputAmount = GT_ModHandler.getCapsuleCellContainerCountMultipliedWithStackSize(x.mOutputs);

		if (tInputAmount < tOutputAmount) {
			if (!Materials.Tin.contains(x.mInputs)) {
				return false;
			}
			else {
				return true;
			}
		} 
		else if (tInputAmount > tOutputAmount && !Materials.Tin.contains(x.mOutputs)) {
			return false;
		}	
		else {
			return true;				
		}
	}

	public static String[] getRecipeInfo(GT_Recipe m) {
		if (m == null) {
			return new String[] {};
		}
		AutoMap<String> result = new AutoMap<String>();
		result.put(m.toString());
		result.put("Input "+ItemUtils.getArrayStackNames(m.mInputs));
		result.put("Output "+ItemUtils.getArrayStackNames(m.mOutputs));
		result.put("Input "+ItemUtils.getArrayStackNames(m.mFluidInputs));
		result.put("Output "+ItemUtils.getArrayStackNames(m.mFluidOutputs));
		result.put("Can be buffered? "+m.mCanBeBuffered);
		result.put("Duration: "+m.mDuration);
		result.put("EU/t: "+m.mEUt);
		result.put("Is Hidden? "+m.mHidden);
		result.put("Is Enabled? "+m.mEnabled);
		result.put("Special Value: "+m.mSpecialValue);
		result.put("=====================================");		
		String s[] = new String[result.size()];
		for (int i=0;i<result.size();i++) {
			s[i] = result.get(i);
		}		
		return s;
	}


	public static class InternalRecipeObject implements RunnableWithInfo<String> {
		final ItemStack mOutput;
		final ShapedOreRecipe mRecipe;
		public final boolean isValid;

		public InternalRecipeObject(Object[] aInputs, ItemStack aOutput, boolean gtRecipe) {

			mOutput = aOutput != null ? aOutput.copy() : null;			
			String line1 = "", line2 = "", line3 = "";
			String h = "abcdefghi";
			char blank = ' ';
			int counter = 0;
			char s[] = new char[9];
			ItemStack[] vInputs = new ItemStack[9];
			Object[] tInputs = aInputs.clone();
			
			if (tInputs.length >= 8 && aOutput != null) {
				
				for (Object o : tInputs) {
					if (o instanceof String || o instanceof ItemStack) {
						s[counter] = h.charAt(counter);
					}
					else if (o == null) {
						s[counter] = blank;
					}				
					counter++;
				}			

				for (int y=0;y<9;y++){			
					if (tInputs.length > y) {
						if (tInputs[y] instanceof String) {
							vInputs[y] = ItemUtils.getItemStackOfAmountFromOreDict((String) tInputs[y], 1);
						}
						else if (tInputs[y] instanceof ItemStack) {
							vInputs[y] = (ItemStack) tInputs[y];
						}	
						else {
							vInputs[y] = null;
							Logger.INFO("[Recipe] Invalid Item in shapped recipe outputting "+mOutput != null ? mOutput.getDisplayName() : "BAD OUTPUT ITEM" + " | Type: "+tInputs[y] != null ? tInputs[y].getClass().getName() : "Unable to determine class of invalid object.");
						}
					}
					else {
						vInputs[y] = null;
					}
				}	
			}

			for (int i=0;i<3;i++) {
				line1 = line1 + s[i];
			}
			for (int i=3;i<6;i++) {
				line2 = line2 + s[i];				
			}
			for (int i=6;i<9;i++) {
				line3 = line3 + s[i];				
			}

			//line1 = StringUtils.join(s, null, 0, 2);
			//line2 = StringUtils.join(s, null, 3, 5);
			//line3 = StringUtils.join(s, null, 6, 8);
			

			Object[] mVarags = new Object[18];
			int mSlotCount = 0;
			
			for (int i=0;i<9;i++) {
				if (s[i] != ' ') {
					mVarags[mSlotCount] = String.valueOf(s[i]);
				}
				if (vInputs[i] != null) {
					mVarags[mSlotCount+1] = vInputs[i];
				}
				mSlotCount += 2;
			}
			
			int nullCount = 0;
			
			for (Object m : mVarags){
				if (m == null) {
					nullCount++;
				}
			}
			Object[] mVarags2 = new Object[mVarags.length-nullCount];
			for (int i=0;i<(mVarags.length-nullCount);i++) {
				mVarags2[i] = mVarags[i];
			}			

			ShapedOreRecipe d = new ShapedOreRecipe(
					aOutput,
					line1,
					line2,
					line3,
					mVarags2);


			if (mOutput == null || counter < 8 || d == null || (line1 == null || line2 == null || line3 == null) || !ItemUtils.checkForInvalidItems(d.getRecipeOutput())) {
				isValid = false;
			}
			else {
				isValid = true;				
			}

			mRecipe = d != null ? d : null;
		}

		@Override
		public void run() {
			if (this.isValid) {
				GameRegistry.addRecipe(mRecipe);
			}
			else {
				Logger.INFO("[Recipe] Invalid shapped recipe outputting "+mOutput != null ? mOutput.getDisplayName() : "Bad Output Item");
			}

			/*ItemStack[] vInputs = new ItemStack[9];
			if (mOutput != null) {
				for (int y=0;y<9;y++){			
					if (mInputs.length > y) {
						if (mInputs[y] instanceof String) {
							vInputs[y] = ItemUtils.getItemStackOfAmountFromOreDict((String) mInputs[y], 1);
						}
						else if (mInputs[y] instanceof ItemStack) {
							vInputs[y] = (ItemStack) mInputs[y];
						}	
						else {
							Logger.INFO("[Recipe] Invalid Item in shapped recipe outputting "+mOutput.getDisplayName() + " | Type: "+mInputs[y].getClass().getName());
						}
					}
					else {
						vInputs[y] = null;
					}
				}		

				GameRegistry.addRecipe(new ShapedOreRecipe(mOutput.copy(), vInputs));
			}*/



			/*if (!gtType) {

			}
			else {
				if (GT_ModHandler.addCraftingRecipe(mOutput,
						GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE |
						GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED,
						new Object[]{"ABC", "DEF", "GHI",
								'A', mInputs[0],
								'B', mInputs[1],
								'C', mInputs[2],
								'D', mInputs[3],
								'E', mInputs[4],
								'F', mInputs[5],
								'G', mInputs[6],
								'H', mInputs[7],
								'I', mInputs[8]})){
					Logger.WARNING("Success! Added a recipe for "+mOutput.getDisplayName());
					RegistrationHandler.recipesSuccess++;
				}
				else if (recipeBuilder(
						mInputs[0], mInputs[0], mInputs[0],
						mInputs[0], mInputs[0], mInputs[0],
						mInputs[0], mInputs[0], mInputs[0], 
						mOutput)){
					Logger.WARNING("Success! Added a recipe for "+mOutput.getDisplayName());
					RegistrationHandler.recipesSuccess++;
				}
				else {
					if (mOutput != null){
						Logger.WARNING("Adding recipe for "+mOutput.getDisplayName()+" failed. Error 62.");
					}
					RegistrationHandler.recipesFailed++;
				}
			}*/
		}

		@Override
		public String getInfoData() {
			if (mOutput != null && mOutput instanceof ItemStack) {
				return ((ItemStack) mOutput).getDisplayName();
			}
			return "";
		}

	}



}
