package miscutil.core.util.recipe;

import gregtech.api.util.GT_ModHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import miscutil.core.handler.COMPAT_HANDLER;
import miscutil.core.handler.registration.LateRegistrationHandler;
import miscutil.core.handler.registration.RegistrationHandler;
import miscutil.core.lib.CORE;
import miscutil.core.util.Utils;
import miscutil.core.util.item.UtilsItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;

public class UtilsRecipe {

	public static void recipeBuilder(Object slot_1, Object slot_2, Object slot_3, Object slot_4, Object slot_5, Object slot_6, Object slot_7, Object slot_8, Object slot_9, ItemStack resultItem){	

		ArrayList<Object> validSlots = new ArrayList<Object>();

		Utils.LOG_INFO("Trying to add a recipe for "+resultItem.toString());
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
		String lineOne = a+b+c;
		Utils.LOG_ERROR("|"+a+"|"+b+"|"+c+"|");
		Utils.LOG_ERROR("_______");
		String lineTwo = d+e+f;
		Utils.LOG_ERROR("|"+d+"|"+e+"|"+f+"|");
		Utils.LOG_ERROR("_______");
		String lineThree = g+h+i;
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
			int l = validSlots.size();
			Utils.LOG_WARNING("l:"+l);
			while (j <= l) {
				Utils.LOG_WARNING("j:"+j);
				if (j <= 2){
					Utils.LOG_WARNING("ArrayList Values: "+validSlots.get(j));
					Utils.LOG_WARNING("Adding 1.");
					j++;
				}
				else if (j >= 3){
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
				else if (j == l){
					Utils.LOG_WARNING("Done iteration.");
					break;
				}
				if (validSlots.get(j) instanceof String || validSlots.get(j) instanceof ItemStack){
					//Utils.LOG_WARNING("Is Valid: "+validSlots.get(j));
				}
			}	
		}

		try {
			GameRegistry.addRecipe(new ShapedOreRecipe(resultItem.copy(), (Object[]) validSlots.toArray()));		
			Utils.LOG_INFO("Success! Added a recipe for "+resultItem.toString());
			if (!COMPAT_HANDLER.areInitItemsLoaded){
				RegistrationHandler.recipesSuccess++;
			}
			else {
				LateRegistrationHandler.recipesSuccess++;
			}
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
		}
	}

	public static void shapelessBuilder(ItemStack Output, Object slot_1, Object slot_2, Object slot_3, Object slot_4, Object slot_5, Object slot_6, Object slot_7, Object slot_8, Object slot_9){
		//Item output_ITEM = Output.getItem();

		ArrayList<Object> validSlots = new ArrayList<Object>();

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
			GameRegistry.addRecipe(new ShapelessOreRecipe(Output, (Object[]) validSlots.toArray()));
			//GameRegistry.addShapelessRecipe(new ItemStack(output_ITEM, 1), new Object[] {slot_1, slot_2});
			Utils.LOG_INFO("Success! Added a recipe for "+Output.toString());
			RegistrationHandler.recipesSuccess++;		
		}
		catch(RuntimeException k){
			k.getMessage();
			k.getClass();
			k.printStackTrace();
			k.getLocalizedMessage();
			Utils.LOG_WARNING("@@@: Invalid Recipe detected for: "+Output.getUnlocalizedName());
			RegistrationHandler.recipesFailed++;
		}


		//GameRegistry.addShapelessRecipe(new ItemStack(output_ITEM, 1), new Object[] {slot_1, slot_2});
	}

	public static void recipeBuilder(Object[] array, ItemStack outPut) {
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
			Item R = UtilsItems.getItem((String) x);
			if (R != null){
				x = R;
			}
			else {
				return false;
			}
		}
		if (x instanceof Item || x instanceof ItemStack){
			if (x instanceof Item){
				ItemStack r = new ItemStack((Item) x);
				Utils.LOG_INFO("Removing Recipe for "+r.getUnlocalizedName());
			}
			else {
				Utils.LOG_INFO("Removing Recipe for "+((ItemStack) x).getUnlocalizedName());
			}
			if (x instanceof ItemStack){
				Item r = ((ItemStack) x).getItem();		
				if (null != r){
					x = r;
				}
				else {
					Utils.LOG_INFO("Recipe removal failed - Tell Alkalus.");
					return false;
				}
			}			
			if (UtilsRecipe.attemptRecipeRemoval((Item) x)){
				Utils.LOG_INFO("Recipe removal successful");
				return true;
			}
			Utils.LOG_INFO("Recipe removal failed - Tell Alkalus.");
			return false;			
		}
		return false;
	}

	private static boolean attemptRecipeRemoval(Item I){
		Utils.LOG_WARNING("Create list of recipes.");
		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		Iterator<IRecipe> items = recipes.iterator();
		Utils.LOG_WARNING("Begin list iteration.");
		while (items.hasNext()) {
			ItemStack is = items.next().getRecipeOutput();
			if (is != null && is.getItem() == I){
				items.remove();
				Utils.LOG_INFO("Remove a recipe with "+I.getUnlocalizedName()+" as output.");
				continue;
			}
		}
		Utils.LOG_WARNING("All recipes should be gone?");
		if (!items.hasNext()){
			Utils.LOG_WARNING("We iterated once, let's try again to double check.");
			Iterator<IRecipe> items2 = recipes.iterator();
			while (items2.hasNext()) {
				ItemStack is = items2.next().getRecipeOutput();
				if (is != null && is.getItem() == I){
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






	public static void addShapedGregtechRecipe( 
			Object InputItem1, Object InputItem2, Object InputItem3,
			Object InputItem4, Object InputItem5, Object InputItem6,
			Object InputItem7, Object InputItem8, Object InputItem9,
			ItemStack OutputItem){

		if ((!(InputItem1 instanceof ItemStack) && !(InputItem1 instanceof String)) || 
				(!(InputItem2 instanceof ItemStack) && !(InputItem2 instanceof String)) || 
				(!(InputItem3 instanceof ItemStack) && !(InputItem3 instanceof String)) || 
				(!(InputItem4 instanceof ItemStack) && !(InputItem4 instanceof String)) || 
				(!(InputItem5 instanceof ItemStack) && !(InputItem5 instanceof String)) || 
				(!(InputItem6 instanceof ItemStack) && !(InputItem6 instanceof String)) || 
				(!(InputItem7 instanceof ItemStack) && !(InputItem7 instanceof String)) || 
				(!(InputItem8 instanceof ItemStack) && !(InputItem8 instanceof String)) || 
				(!(InputItem9 instanceof ItemStack) && !(InputItem9 instanceof String))){
			Utils.LOG_INFO("One Input item was not an ItemStack of an OreDict String.");
			return;
		}

		/*if  (InputItem1 instanceof String) {
			String temp = (String) InputItem1;
			InputItem1 = getItemStackFromOreDict(temp);
		}
		if	(InputItem2 instanceof String) {
			String temp = (String) InputItem2;
			InputItem2 = getItemStackFromOreDict(temp);
		}
		if	(InputItem3 instanceof String) {
			String temp = (String) InputItem3;
			InputItem3 = getItemStackFromOreDict(temp);
		}
		if	(InputItem4 instanceof String) {
			String temp = (String) InputItem4;
			InputItem4 = getItemStackFromOreDict(temp);
		}
		if	(InputItem5 instanceof String) {
			String temp = (String) InputItem5;
			InputItem5 = getItemStackFromOreDict(temp);
		}
		if	(InputItem6 instanceof String) {
			String temp = (String) InputItem6;
			InputItem6 = getItemStackFromOreDict(temp);
		}
		if	(InputItem7 instanceof String) {
			String temp = (String) InputItem7;
			InputItem7 = getItemStackFromOreDict(temp);
		}
		if	(InputItem8 instanceof String) { 
			String temp = (String) InputItem8;
			InputItem8 = getItemStackFromOreDict(temp);
		}
		if	(InputItem9 instanceof String) {
			String temp = (String) InputItem9;
			InputItem9 = getItemStackFromOreDict(temp);
		}*/

		GT_ModHandler.addCraftingRecipe(OutputItem, 
				GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | 
				GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED,
				new Object[]{"ABC", "DEF", "GHI", 
				'A', InputItem1,  
				'B', InputItem2,  
				'C', InputItem3,  
				'D', InputItem4,  
				'E', InputItem5,  
				'F', InputItem6,  
				'G', InputItem7,  
				'H', InputItem8,  
				'I', InputItem9});		
	}

	public static ItemStack getItemStackFromOreDict(String oredictName){
		ArrayList<ItemStack> oreDictList = OreDictionary.getOres(oredictName);
		return oreDictList.get(0);
	}

}
