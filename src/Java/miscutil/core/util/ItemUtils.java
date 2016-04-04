package miscutil.core.util;

import java.util.ArrayList;

import miscutil.core.handler.registration.RegistrationHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;

public class ItemUtils {

	public static ItemStack getItemStackOfItem(Boolean modToCheck, String mod_itemname_meta){
		if (modToCheck){
			try{
				Item em = null;

				Item em1 = Utils.getItem(mod_itemname_meta);
				Utils.LOG_WARNING("Found: "+em1.toString());
				if (!em1.equals(null)){
					em = em1;
				}			
				else {
					em = null;
					return null;
				}			
				if (!em.equals(null)){
					ItemStack returnStack = new ItemStack(em,1,16);				
					return returnStack;
				}
				Utils.LOG_WARNING(mod_itemname_meta+" not found.");
				return null;
			} catch (NullPointerException e) {
				Utils.LOG_ERROR(mod_itemname_meta+" not found. [NULL]");
				return null;
			}
		}
		return null;
	}

	public static void recipeBuilder(Object slot_1, Object slot_2, Object slot_3, Object slot_4, Object slot_5, Object slot_6, Object slot_7, Object slot_8, Object slot_9, ItemStack resultItem){	

		ArrayList<Object> validSlots = new ArrayList<Object>();

		//, String lineFirst, String lineSecond, String lineThird
		Utils.LOG_INFO("Trying to add a recipe for "+resultItem.toString());
		String a,b,c,d,e,f,g,h,i;		
		//ItemStack empty = new ItemStack(Blocks.air);
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
		Boolean AadvancedLog = true;
		if (AadvancedLog){
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
			/*Utils.LOG_WARNING("validSlots to array: "+validSlots.toArray());
			Object[] validSlotsArray = (Object[]) validSlots.toArray();

			for(int j = 0; j < validSlotsArray.length; j++)
			{
				Utils.LOG_ERROR(""+validSlotsArray[j]);
			}*/
			
			GameRegistry.addRecipe(new ShapedOreRecipe(resultItem.copy(), (Object[]) validSlots.toArray()));		
			Utils.LOG_INFO("Success! Added a recipe for "+resultItem.toString());
			RegistrationHandler.recipesSuccess++;
			/*try {
			try {
				try {
				//Code
				}
					catch (NullPointerException | ClassCastException r){
						Utils.LOG_WARNING("@@@: Invalid Recipe detected for: "+resultItem.getUnlocalizedName());
						RegistrationHandler.recipesFailed++;
						r.printStackTrace();
						//System.exit(1);
					}
				}
				catch (NullPointerException o){

					Utils.LOG_WARNING("@@@: Invalid Recipe detected for: "+resultItem.getUnlocalizedName());
					o.printStackTrace();
					RegistrationHandler.recipesFailed++;
					//System.exit(1);
				}
			}
			catch (ClassCastException r){
				Utils.LOG_WARNING("@@@: Casting to ObjectArray Failed :(");
			}*/
		}
		catch(NullPointerException | ClassCastException k){
			k.getMessage();
			k.getClass();
			k.printStackTrace();
			k.getLocalizedMessage();
			Utils.LOG_WARNING("@@@: Invalid Recipe detected for: "+resultItem.getUnlocalizedName());
			RegistrationHandler.recipesFailed++;
			//System.exit(1);
		}
	}

}
