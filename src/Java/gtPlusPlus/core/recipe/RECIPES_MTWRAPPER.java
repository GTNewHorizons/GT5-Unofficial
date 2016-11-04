package gtPlusPlus.core.recipe;

import java.util.ArrayList;

import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.recipe.RecipeUtils;
import gtPlusPlus.core.util.wrapper.var;
import net.minecraft.item.ItemStack;

public class RECIPES_MTWRAPPER {

	public static int MT_RECIPES_LOADED = 0;
	public static int MT_RECIPES_FAILED = 0;

	static var chestWood = new var("minecraft:chest>");
	static var slabWood = new var("ore:slabWood>");
	static var gemNetherQuartz = new var("ore:gemNetherQuartz>");
	static var glass = new var("ore:blockGlass>");
	static var sensorDaylight = new var("minecraft:daylight_detector>");
	static var blazeRod = new var("minecraft:blaze_rod>");
	static var saw = new var("ore:craftingToolSaw>");
	static var logWood = new var("ore:logWood>");
	static var button = new var("minecraft:stone_button>");
	static var stoneBlock = new var("minecraft:stone>");

	public static void run(){
		/*addShaped(button.getStack(2), 
				null, stoneBlock, null,
				null, stoneBlock, null,
				null, null, null);*/
		/*addShaped(stoneStick.getStack(1), 
				stoneBlock, null, null,
				stoneBlock, null, null,
				null, null, null);*/
		addShaped(chestWood.getStack(2), 
				logWood, logWood, logWood,
				logWood, null, logWood,
				logWood, logWood, logWood);
		addShaped(chestWood.getStack(4), 
				logWood, logWood, logWood,
				logWood, saw, logWood,
				logWood, logWood, logWood);
		//Recipe Fixes
		//remove(sensorDaylight);
		addShaped(sensorDaylight.getStack(1), 
				glass, glass, glass,
				gemNetherQuartz, gemNetherQuartz, gemNetherQuartz,
				slabWood, slabWood, slabWood);
		/*addShaped(ironBars .getStack( 8), 
				null, "<ore:craftingToolWrench>", null,
				"<ore:stickAnyIron>", "<ore:stickAnyIron>", "<ore:stickAnyIron>",
				"<ore:stickAnyIron>", "<ore:stickAnyIron>", "<ore:stickAnyIron>");*/
	}


	public static void addShaped(Object item_Output,
			Object item_1,	Object item_2,	Object item_3,
			Object item_4,	Object item_5,	Object item_6,
			Object item_7,	Object item_8,	Object item_9){


		/*
		 * 
		 * var item_1,	var item_2,	var item_3,
			var item_4,	var item_5,	var item_6,
			var item_7,	var item_8,	var item_9
		 * 
		 * 
		 */

		ItemStack outputItem = ItemUtils.getCorrectStacktype(item_Output, 1);

		ArrayList<Object> validSlots = new ArrayList<Object>();
		String a,b,c,d,e,f,g,h,i;
		if (item_1 == null){ a = " ";} else { a = "1";validSlots.add('1');validSlots.add(item_1);}
		if (item_2 == null){ b = " ";} else { b = "2";validSlots.add('2');validSlots.add(item_2);}
		if (item_3 == null){ c = " ";} else { c = "3";validSlots.add('3');validSlots.add(item_3);}
		if (item_4 == null){ d = " ";} else { d = "4";validSlots.add('4');validSlots.add(item_4);}
		if (item_5 == null){ e = " ";} else { e = "5";validSlots.add('5');validSlots.add(item_5);}
		if (item_6 == null){ f = " ";} else { f = "6";validSlots.add('6');validSlots.add(item_6);}
		if (item_7 == null){ g = " ";} else { g = "7";validSlots.add('7');validSlots.add(item_7);}
		if (item_8 == null){ h = " ";} else { h = "8";validSlots.add('8');validSlots.add(item_8);}
		if (item_9 == null){ i = " ";} else { i = "9";validSlots.add('9');validSlots.add(item_9);}

		String lineOne = a+b+c;
		String lineTwo = d+e+f;
		String lineThree = g+h+i;
		validSlots.add(0, lineOne);
		validSlots.add(1, lineTwo);
		validSlots.add(2, lineThree);

		try {
			RecipeUtils.recipeBuilder((Object[]) validSlots.toArray(), outputItem.copy());
			MT_RECIPES_LOADED++;		
		}
		catch(NullPointerException | ClassCastException k){
			k.getMessage();
			k.getClass();
			k.printStackTrace();
			k.getLocalizedMessage();
			Utils.LOG_WARNING("@@@: Invalid Recipe detected for: "+((var) item_Output).getsanitizedName());
			MT_RECIPES_FAILED++;
		}		
	}

	public static void addShapeless(){

	}



}

