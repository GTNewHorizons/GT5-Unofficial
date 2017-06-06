package gtPlusPlus.core.util.recipe;

import java.util.*;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.core.handler.COMPAT_HANDLER;
import gtPlusPlus.core.handler.Recipes.LateRegistrationHandler;
import gtPlusPlus.core.handler.Recipes.RegistrationHandler;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.recipe.shapeless.ShapelessUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.*;

public class RecipeUtils {

	public static boolean recipeBuilder(final Object slot_1, final Object slot_2, final Object slot_3, final Object slot_4, final Object slot_5, final Object slot_6, final Object slot_7, final Object slot_8, final Object slot_9, final ItemStack resultItem){

		final ArrayList<Object> validSlots = new ArrayList<>();
		if (resultItem == null){
			Utils.LOG_INFO("Found a recipe with an invalid output, yet had a valid inputs. Skipping.");
			return false;
		}

		if ((slot_1 == null) && (slot_2 == null) && (slot_3 == null) &&
				(slot_4 == null) && (slot_5 == null) && (slot_6 == null) &&
				(slot_7 == null) && (slot_8 == null) && (slot_9 == null)){
			Utils.LOG_INFO("Found a recipe with 0 inputs, yet had a valid output.");
			Utils.LOG_INFO("Error found while adding a recipe for: "+resultItem.getDisplayName()+" | Please report this issue on Github.");
			return false;
		}

		//Utils.LOG_INFO("Trying to add a recipe for "+resultItem.toString());
		String a,b,c,d,e,f,g,h,i;
		if (slot_1 == null){ a = " ";} else { a = "1";validSlots.add('1');validSlots.add(slot_1);}
		Utils.LOG_WARNING(a);
		if (slot_2 == null){ b = " ";} else { b = "2";validSlots.add('2');validSlots.add(slot_2);}
		Utils.LOG_WARNING(b);
		if (slot_3 == null){ c = " ";} else { c = "3";validSlots.add('3');validSlots.add(slot_3);}
		Utils.LOG_WARNING(c);
		if (slot_4 == null){ d = " ";} else { d = "4";validSlots.add('4');validSlots.add(slot_4);}
		Utils.LOG_WARNING(d);
		if (slot_5 == null){ e = " ";} else { e = "5";validSlots.add('5');validSlots.add(slot_5);}
		Utils.LOG_WARNING(e);
		if (slot_6 == null){ f = " ";} else { f = "6";validSlots.add('6');validSlots.add(slot_6);}
		Utils.LOG_WARNING(f);
		if (slot_7 == null){ g = " ";} else { g = "7";validSlots.add('7');validSlots.add(slot_7);}
		Utils.LOG_WARNING(g);
		if (slot_8 == null){ h = " ";} else { h = "8";validSlots.add('8');validSlots.add(slot_8);}
		Utils.LOG_WARNING(h);
		if (slot_9 == null){ i = " ";} else { i = "9";validSlots.add('9');validSlots.add(slot_9);}
		Utils.LOG_WARNING(i);


		Utils.LOG_ERROR("_______");
		final String lineOne = a+b+c;
		Utils.LOG_ERROR("|"+a+"|"+b+"|"+c+"|");
		Utils.LOG_ERROR("_______");
		final String lineTwo = d+e+f;
		Utils.LOG_ERROR("|"+d+"|"+e+"|"+f+"|");
		Utils.LOG_ERROR("_______");
		final String lineThree = g+h+i;
		Utils.LOG_ERROR("|"+g+"|"+h+"|"+i+"|");
		Utils.LOG_ERROR("_______");

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
			Utils.LOG_WARNING("l:"+l);
			while (j <= l) {
				Utils.LOG_WARNING("j:"+j);
				if (j <= 2){
					Utils.LOG_WARNING("ArrayList Values: "+validSlots.get(j));
					Utils.LOG_WARNING("Adding 1.");
					j++;
				}
				else if (j == l){
					Utils.LOG_WARNING("Done iteration.");
					break;
				}
				else {
					Utils.LOG_WARNING("ArrayList Values: '"+validSlots.get(j)+"' "+validSlots.get(j+1));
					if (j < (l-2)){
						Utils.LOG_WARNING("Adding 2.");
						j=j+2;
					}
					else {
						Utils.LOG_WARNING("Done iteration.");
						break;
					}
				}
				if ((validSlots.get(j) instanceof String) || (validSlots.get(j) instanceof ItemStack)){
					//Utils.LOG_WARNING("Is Valid: "+validSlots.get(j));
				}
			}
		}

		try {
			GameRegistry.addRecipe(new ShapedOreRecipe(resultItem.copy(), validSlots.toArray()));
			//Utils.LOG_INFO("Success! Added a recipe for "+resultItem.getDisplayName());
			if (!COMPAT_HANDLER.areInitItemsLoaded){
				RegistrationHandler.recipesSuccess++;
			}
			else {
				LateRegistrationHandler.recipesSuccess++;
			}
			return true;
		}
		catch(NullPointerException | ClassCastException k){
			k.getMessage();
			k.getClass();
			k.printStackTrace();
			k.getLocalizedMessage();
			Utils.LOG_WARNING("@@@: Invalid Recipe detected for: "+resultItem.getUnlocalizedName());
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

		Utils.LOG_INFO("Trying to add a recipe for "+Output.toString());
		String a,b,c,d,e,f,g,h,i;
		if (slot_1 == null){ a = " ";} else { a = "1";validSlots.add('1');validSlots.add(slot_1);}
		Utils.LOG_WARNING(a);
		if (slot_2 == null){ b = " ";} else { b = "2";validSlots.add('2');validSlots.add(slot_2);}
		Utils.LOG_WARNING(b);
		if (slot_3 == null){ c = " ";} else { c = "3";validSlots.add('3');validSlots.add(slot_3);}
		Utils.LOG_WARNING(c);
		if (slot_4 == null){ d = " ";} else { d = "4";validSlots.add('4');validSlots.add(slot_4);}
		Utils.LOG_WARNING(d);
		if (slot_5 == null){ e = " ";} else { e = "5";validSlots.add('5');validSlots.add(slot_5);}
		Utils.LOG_WARNING(e);
		if (slot_6 == null){ f = " ";} else { f = "6";validSlots.add('6');validSlots.add(slot_6);}
		Utils.LOG_WARNING(f);
		if (slot_7 == null){ g = " ";} else { g = "7";validSlots.add('7');validSlots.add(slot_7);}
		Utils.LOG_WARNING(g);
		if (slot_8 == null){ h = " ";} else { h = "8";validSlots.add('8');validSlots.add(slot_8);}
		Utils.LOG_WARNING(h);
		if (slot_9 == null){ i = " ";} else { i = "9";validSlots.add('9');validSlots.add(slot_9);}
		Utils.LOG_WARNING(i);


		Utils.LOG_ERROR("_______");
		Utils.LOG_ERROR("|"+a+"|"+b+"|"+c+"|");
		Utils.LOG_ERROR("_______");
		Utils.LOG_ERROR("|"+d+"|"+e+"|"+f+"|");
		Utils.LOG_ERROR("_______");
		Utils.LOG_ERROR("|"+g+"|"+h+"|"+i+"|");
		Utils.LOG_ERROR("_______");

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
			Utils.LOG_INFO("Success! Added a recipe for "+Output.getDisplayName());
			RegistrationHandler.recipesSuccess++;
		}
		catch(final RuntimeException k){
			k.getMessage();
			k.getClass();
			k.printStackTrace();
			k.getLocalizedMessage();
			Utils.LOG_WARNING("@@@: Invalid Recipe detected for: "+Output.getUnlocalizedName());
			RegistrationHandler.recipesFailed++;
		}


		//GameRegistry.addShapelessRecipe(new ItemStack(output_ITEM, 1), new Object[] {slot_1, slot_2});
	}

	public static void recipeBuilder(final Object[] array, final ItemStack outPut) {
		Utils.LOG_SPECIFIC_WARNING("object Array - recipeBuilder", "Attempting to build a recipe using an object array as an input, splitting it, then running the normal recipeBuilder() method.", 396);
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
				Utils.LOG_INFO("Removing Recipe for "+r.getUnlocalizedName());
			}
			else {
				Utils.LOG_INFO("Removing Recipe for "+((ItemStack) x).getUnlocalizedName());
			}
			if (x instanceof ItemStack){
				final Item r = ((ItemStack) x).getItem();
				if (null != r){
					x = r;
				}
				else {
					Utils.LOG_INFO("Recipe removal failed - Tell Alkalus.");
					return false;
				}
			}
			if (RecipeUtils.attemptRecipeRemoval((Item) x)){
				Utils.LOG_INFO("Recipe removal successful");
				return true;
			}
			Utils.LOG_INFO("Recipe removal failed - Tell Alkalus.");
			return false;
		}
		return false;
	}

	private static boolean attemptRecipeRemoval(final Item I){
		Utils.LOG_WARNING("Create list of recipes.");
		final List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		final Iterator<IRecipe> items = recipes.iterator();
		Utils.LOG_WARNING("Begin list iteration.");
		while (items.hasNext()) {
			final ItemStack is = items.next().getRecipeOutput();
			if ((is != null) && (is.getItem() == I)){
				items.remove();
				Utils.LOG_INFO("Remove a recipe with "+I.getUnlocalizedName()+" as output.");
				continue;
			}
		}
		Utils.LOG_WARNING("All recipes should be gone?");
		if (!items.hasNext()){
			Utils.LOG_WARNING("We iterated once, let's try again to double check.");
			final Iterator<IRecipe> items2 = recipes.iterator();
			while (items2.hasNext()) {
				final ItemStack is = items2.next().getRecipeOutput();
				if ((is != null) && (is.getItem() == I)){
					items.remove();
					Utils.LOG_WARNING("REMOVING MISSED RECIPE - RECHECK CONSTRUCTORS");
					return true;
				}
			}
			Utils.LOG_WARNING("Should be all gone now after double checking, so return true.");
			return true;
		}
		Utils.LOG_INFO("Return false, because something went wrong.");
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
				
				if (addShapedGregtechRecipe(o, OutputItem)){
					return true;
				}
				else {		
					return false;
				}
			}

			public static boolean addShapedGregtechRecipe(final Object[] inputs, final ItemStack output){

				if (inputs.length != 9){
					Utils.LOG_INFO("Input array for "+output.getDisplayName()+" does not equal 9.");
					return false;
				}
				
				for (int x=0;x<9;x++){
					if (inputs[x] == null){
						inputs[x] = " ";
					}
					if (!(inputs[x] instanceof ItemStack) || !(inputs[x] instanceof String)){
						Utils.LOG_INFO("Invalid Item inserted into inputArray. Item:"+output.getDisplayName()+" has a bad recipe. Please report to Alkalus.");
					}
				}

				if (GT_ModHandler.addCraftingRecipe(output,
							GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE |
							GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED,
							new Object[]{"ABC", "DEF", "GHI",
								'A', inputs[0],
								'B', inputs[1],
								'C', inputs[2],
								'D', inputs[3],
								'E', inputs[4],
								'F', inputs[5],
								'G', inputs[6],
								'H', inputs[7],
								'I', inputs[8]})){
					Utils.LOG_INFO("Success! Added a recipe for "+output.getDisplayName());
					RegistrationHandler.recipesSuccess++;
					return true;
				}
				else {
					Utils.LOG_INFO("Failed to add recipe for "+output.getDisplayName()+". Please report to Alkalus.");
					return false;
				}
			}

	public static void addShapelessGregtechRecipe(final ItemStack OutputItem, final Object... inputItems){

		for(final Object whatever : inputItems){
			if (!(whatever instanceof ItemStack) && !(whatever instanceof String)){
				Utils.LOG_INFO("One Input item was not an ItemStack of an OreDict String.");
				return;
			}
		}

		GT_ModHandler.addShapelessCraftingRecipe(OutputItem,
				GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
				new Object[]{inputItems});
	}

	public static ItemStack getItemStackFromOreDict(final String oredictName){
		final ArrayList<ItemStack> oreDictList = OreDictionary.getOres(oredictName);
		return oreDictList.get(0);
	}

	public static boolean buildShapelessRecipe(final ItemStack output, final Object[] input){
		return ShapelessUtils.addShapelessRecipe(output, input);
	}

}
