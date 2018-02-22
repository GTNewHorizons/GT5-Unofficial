package gtPlusPlus.core.handler;

import java.util.HashMap;
import java.util.Map;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.RecipeUtils;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class BookHandler {

	public static int mBookKeeperCount = 0;

	public static Map<Integer, BookTemplate> mBookMap = new HashMap();
	
	public static BookTemplate book_ThermalBoiler;
	public static BookTemplate book_TestNovel;
	public static BookTemplate book_ModularBauble;
	
	public static void run(){		

		Logger.INFO("Writing books.");

		//Thermal Boiler
		book_ThermalBoiler = writeBookTemplate(
				"Manual_Thermal_Boiler", "Thermal Boiler Manual", "Alkalus", 
				new String[] {
						"This Book explains how to set up and run your Thermal Boiler. We are not responsible for any Damage done by this Book itself nor its content.", 
						"First you need to craft the following things for a Thermal Boiler to Function: The Main Boiler Block, 20 Thermal Containment Casings, two Input Hatches, two Output Hatches, a bunch of different Tools and a Maintenance Hatch.",
						"To begin the building, lay out the first 3x3 layer of Machine Casings on the ground (with a Hatch in the Middle), then place the Boiler Block facing outward in the middle of one of the 3m wide Sides.", 
						"Now grab 3 other Hatches and place them on the remaining three 3m wide Sides also facing outwards. And now the four corners of the Machine need also a Machine Casing. There should only be a Hole left in the middle of the Cube.",
						"So, now place a 3x3 of Machine Casings ontop, at the 3rd Layer with the last Hatch in the middle facing outwards as well.", 
						"When accessing the Boiler Block, it should now stop telling you, that the structure is incomplete (bottom Line of that Screen). Now go with a bunch of different Tools (Metal Hammer, Rubber Hammer, Screwdriver, Wrench, Soldering Iron and Crowbar)", 
						"to the Maintenance Hatch and access it. After that you grab the 6 Tools and rightclick the Slot with each of them in your Hand in the Maintenance GUI. Note that you need Soldering Tin/Lead in your Inventory to use the Soldering Iron.", 
						"The Main Block should now tell you that you need to use the Rubber Hammer on it to (re)activate the Machine. The Rubber Hammer can enable and disable Machines. The Machine disables itself after something important broke.", 
						"If you want to use Lava with this Device, then you should add a Lava Filter to extract additional Resources from the Lava. If the Filter breaks, the Machine won't explode like a Turbine would. If you use molten Salt, then you won't need a Filter.", 
						"You will get Obsidian when processing Lava, however if a Filter is used, you will get sometimes an Ingot instead of a Block of Obsidian. When using molten Salt, you will get the Salt back.", 
						"So, now for the Maintenance. After a few Hours of running nonstop, your Boiler will get small Problems, which don't prevent it from running, these Problems just decrease Efficiency. Every Problem listed on the Screen does -10% Efficiency.", 
						"To fix these Problems, just go to the Maintenance Hatch and click with the problem corresponding Tool on the Slot to repair. If all six possible runtime Problems happen, the Machine will auto-shutdown no matter what. No Explosion, it's just stopping.", 
						"The Thermal Boiler will produce 800 Liters of Steam per tick for about 5 or 6 Liters of Water per tick at reaching 100% Efficiency. In case of Lava it consumes 1666 Liters every Second.", 
						"A Thermal Boiler is worth about 33 small Thermal Generators, and as the Boilers get much less Efficient, when not having enough Fuel, you should consider making a large Nether Pump for Lava, or a good Nuclear Reactor for molten Salt.", 
						"Input and Output Slots are fully optional, you can place multiple ones of them or even none on the Machine. A Machine without Input couldn't process any Recipes, while a Machine without Output just voids all outputted Items and Liquids.", 
				"It might be useful to use the Screwdriver on the Output Hatches to determine what is outputted where." });

		

		//Test Novel
		book_TestNovel = writeBookTemplate(
				"TestBook", "A Book about the Gardens", "Alkalus", 
				new String[] {"There was once a sad and lonely oak tree."});

		//Test Novel
		book_ModularBauble = writeBookTemplate(
				"Manual_Modular_Bauble", "How to: Modular Baubles", "Alkalus", 
				new String[] {
						"Concept: This idea came from wanting flexibility.",
						"First step, Build a Modularity table to begin customisation of your Bauble."
						+ " After this has been constructed, you can now combine the upgrades listed within this book to improve the baubles level /100.",
						"Defence:"
						+ " Can be upgraded by combining metal plates with the bauble."
						+ " | +1 | Aluminium Plate"
						+ " | +2 | Stainless Steel Plate"
						+ " | +3 | Tungsten Plate"
						+ " | +4 | TungstenSteel Plate"
						+ " | +5 | Naquadah Plate",
						"There was once a sad and lonely oak tree.",
						"There was once a sad and lonely oak tree.",
						"There was once a sad and lonely oak tree."});
	}

	
	

	public static ItemStack ItemBookWritten_ThermalBoiler;
	public static ItemStack ItemBookWritten_ModularBaubles;
	public static ItemStack ItemBookWritten_Test;
	
	public static void runLater(){
		ItemBookWritten_ThermalBoiler = ItemUtils.simpleMetaStack(ModItems.itemCustomBook, 0, 1);
		ItemBookWritten_ModularBaubles = ItemUtils.simpleMetaStack(ModItems.itemCustomBook, 1, 1);
		ItemBookWritten_Test = ItemUtils.simpleMetaStack(ModItems.itemCustomBook, 2, 1);
		RecipeUtils.addShapelessGregtechRecipe(new ItemStack[]{ItemUtils.getSimpleStack(Items.writable_book), ItemUtils.getSimpleStack(Items.lava_bucket)}, ItemBookWritten_ThermalBoiler);
	}
	
	private static BookTemplate writeBookTemplate(String aMapping, String aTitle, String aAuthor, String[] aPages){
		mBookKeeperCount++;
		BookTemplate mTemp = new BookTemplate(mBookKeeperCount, aMapping, aTitle, aAuthor, aPages);
		mBookMap.put(mBookKeeperCount-1, mTemp);
		return mTemp;
	}
	
	public static class BookTemplate {
		public final int mMeta;
		public final String mMapping;
		public final String mTitle;
		public final String mAuthor;
		public final String[] mPages;
		
		BookTemplate(int aMeta, String aMapping, String aTitle, String aAuthor, String[] aPages){
			this.mMeta = aMeta;
			this.mMapping = aMapping;
			this.mTitle = aTitle;
			this.mAuthor = aAuthor;
			this.mPages = aPages;
		}
	}
}


