package gtPlusPlus.core.handler;

import java.util.HashMap;
import java.util.Map;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.RecipeUtils;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class BookHandler {

	public static int mBookKeeperCount = 0;

	public static Map<Integer, BookTemplate> mBookMap = new HashMap<Integer, BookTemplate>();

	public static BookTemplate book_ThermalBoiler;
	public static BookTemplate book_MultiPowerStation;
	public static BookTemplate book_ModularBauble;
	public static BookTemplate book_MultiMachineManual;
	public static BookTemplate book_NuclearManual;

	public static void run(){		

		Logger.INFO("Writing books.");

		//Thermal Boiler
		book_ThermalBoiler = writeBookTemplate(
				"Manual_Thermal_Boiler", "Thermal Boiler Manual", "GregoriusT", 
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
		book_MultiPowerStation = writeBookTemplate(
				"Manual_Multi_PowerStation",
				"Power Storage & You [Version 0.64]",
				"Alkalus", 
				new String[] {
								//Page 1
								"So, when it comes to power storage you relaly have three seperate options:\n"+
								" \n"+
								"Battery Buffers,\n"+
								"Energy Buffers,\n"+
								"The Power Sub-Station\n",
								//Page 2
								"Battery Buffer\n" + 
										" \n" +
								"Is rather portable. Allowing you to throw set one up and insert batteries where ever you may need."+"\n"+
								"They output 1A for each battery stored inside, upto a maximum of 16A.",
								//Page 3
								"Energy Buffer\n" + 
										" \n" +
								"Is a more optimal choice for storage in your base. Once placed down, they cannot be moved without losing all stored power."+"\n"+
								"Energy Buffers can output 4A from the output side, however accept 16A as input.",
								//Page 4
								"The Power Sub-Station"+
										" \n"+
								"Is used for storing Insane amounts of power later game."+"\n"+
								"Consumes 2% of the average voltage of all energy type hatches every tick."+"\n",
								//Page 5
								"Allows Insertation/Removal of power from the rear face of the controller, swap with a screwdriver."+"\n"+
								"Variable Height Structure, between 4-16Y. Inserted Redox Cells dictate max energy tier of structure."+"\n",
								//Page 6
								"Redox Cells cannot be placed into the Top or Bottom layer and only take up 3xhx3 internally."+"\n"+
								"Different Tier cells CANNOT be mixed together."+"\n",
								//Page 7
								"All Hatches Must be HV at a Minimum, this minimum tier is in place to stop people abusing ULV/LV hatches to lower the avg/t."+"\n"+
								"Currently the GUI will NOT display anything at all until the structure forms, this is a known bug."+"\n",
								//Page 8
								"Valid Hatches:\n"+
								"Energy Hatch,\n"+
								"Dynamo Hatch,\n"+
								"Charging Bus,\n"+
								"Discharging Bus,\n"+
								"Dynamo Buffer,\n"+
								"Multi-Amp Dynamo Hatch.\n\n\n"+
								"Structure MUST contain at least one energy input and one energy output hatch."
				});

		//Test Novel
		book_ModularBauble = writeBookTemplate(
				"Manual_Modular_Bauble", "How to: Modular Baubles", "Alkalus", 
				new String[] {
						"Concept: This idea came from wanting flexibility. \n" +
						"First step, Build a Modularity table to begin customisation of your Bauble. \n"
								+ " After this has been constructed, you can now combine the upgrades listed within this book to improve the baubles level/100.",
								"Defence:\n"
										+ "Can be upgraded by combining metal plates with the bauble. \n"
										+ " | +1 | Aluminium \n"
										+ " | +2 | Stainless Steel \n"
										+ " | +3 | Tungsten \n"
										+ " | +4 | Tungsten Steel \n"
										+ " | +5 | Naquadah \n",
										"There was once a sad and lonely oak tree. \n",
										"There was once a sad and lonely oak tree. \n",
				"There was once a sad and lonely oak tree. \n"});

		//Test Novel
		//20/21/22
		book_MultiMachineManual = writeBookTemplate(
				"Manual_Multi_Machine", "Multi Machine Manual", "Alkalus", 
				new String[] {
						"This Multiblock, depending upon the mode used, can function as a variety of different machines. The idea behind this, was that most of these machines are rather niche compared to any others, as such, not used often.",
						"To build, you need to construct a hollow 3x3x3 structure made from Multi-Use casings, With a minimum of 6. Any Casing position can be substituted out with an Input Hatch/Bus, an Output Hatch/Bus, Muffler, Maint. Hatch or Energy Injector Hatch.",
						"The Mode can be set by using a Screwdriver on the controller block. Each mode allows the use of Numbered Circuits, to allow a different machine 'type' for each input bus.",
						"[Metal Work] Mode A - Allows the multiblock to function as a Compressor, a Lathe or an Electro-Magnet. To allow a hatch to run in Compressor mode, insert a No. 20 circuit. For Lathe, use No. 21 and for Electro-Magnet use No. 22.",
						"[Fluid Work] Mode B - Allows the multiblock to function as a Fermenter, a Fluid Extractor or an Extractor. To allow a hatch to run in Fermenter mode, insert a No. 20 circuit. For Fluid Extractor, use No. 21 and for Extractor use No. 22.",
						"[Misc. Work] Mode C - Allows the multiblock to function as a Laser Engraver, a Replicator or an Autoclave. To allow a hatch to run in Laser Engraver mode, insert a No. 20 circuit. For Autoclave, use No. 21 and for Replicator use No. 22. REPLICATOR DISABLED.",
				});
		
		book_NuclearManual = writeBookTemplate(
				"Manual_NuclearStuff_1", "Nuclear Chemistry [FFPP]", "Alkalus", 
				new String[] {
						//Page 1
						"Fission Fuel Processing Plant\n"
						+ "Size: 3x9x3 [LxHxW]\n"
						+ "Controller: Center, Bottom\n"
						+ "4x Input Hatch\n"
						+ "2x Output Hatch\n"
						+ "1x Output Bus\n"
						+ "1x ZPM+ Muffler\n"
						+ "1x Maintenance Hatch\n"
						+ "1x Energy Hatch\n",
						//Page 2
						"[1] 7x Hastelloy-X or I/O\n"
						+ "[2] 5x Incoloy-DS Fluid Containment\n"
						+ "[3] 4x Zeron-100 Shielding\n"
						+ "[4] 17x Hastelloy-N Sealant Case"
						+ "\n"
						+ "Multiblock Construction\n"
						+ "Convention is [LxHxW]\n"
						+ "\n",
						//Page 3
						"Layer 1/2:\n"
						+ "[1][1][1]\n"
						+ "[1][1][1]\n"
						+ "[1][1][1]\n"
						+ "\n"
						+ "Layer 3/5/6\n"
						+ "[ ][4][ ]\n"
						+ "[4][2][4]\n"
						+ "[ ][4][ ]\n"
						+ "\n",
						//Page 4
						"Layer 4\n"
						+ "[ ][3][ ]\n"
						+ "[3][2][3]\n"
						+ "[ ][3][ ]\n"
						+ "\n"
						+ "Layer 7/8/9\n"
						+ "[ ][ ][ ]\n"
						+ "[ ][3][ ]\n"
						+ "[ ][ ][ ]\n"
						,
						//Page 5
						"Fission Fuel\n"+
						"Processing Plant"+
						"----------------------\n"+
						"This structure is used to produce the Molten Salts required to run a Liquid Fluorine Thorium Reactor [LFTR]."
						
				});
	}




	public static ItemStack ItemBookWritten_ThermalBoiler;
	public static ItemStack ItemBookWritten_NuclearManual;
	public static ItemStack ItemBookWritten_ModularBaubles;
	public static ItemStack ItemBookWritten_MultiPowerStorage;
	public static ItemStack ItemBookWritten_MultiMachineManual;

	public static void runLater(){
		ItemBookWritten_ThermalBoiler = ItemUtils.simpleMetaStack(ModItems.itemCustomBook, 0, 1);
		ItemBookWritten_MultiPowerStorage = ItemUtils.simpleMetaStack(ModItems.itemCustomBook, 1, 1);
		ItemBookWritten_ModularBaubles = ItemUtils.simpleMetaStack(ModItems.itemCustomBook, 2, 1);
		ItemBookWritten_MultiMachineManual = ItemUtils.simpleMetaStack(ModItems.itemCustomBook, 3, 1);
		ItemBookWritten_NuclearManual = ItemUtils.simpleMetaStack(ModItems.itemCustomBook, 4, 1);

		//Multiblock Manuals
		RecipeUtils.addShapelessGregtechRecipe(new ItemStack[]{ItemUtils.getSimpleStack(Items.writable_book), ItemUtils.getSimpleStack(Items.lava_bucket)}, ItemBookWritten_ThermalBoiler);
		RecipeUtils.addShapelessGregtechRecipe(new ItemStack[]{ItemUtils.getSimpleStack(Items.writable_book), ItemUtils.getItemStackOfAmountFromOreDict(CI.craftingToolWrench, 1)}, ItemBookWritten_MultiMachineManual);
		RecipeUtils.addShapelessGregtechRecipe(new ItemStack[]{ItemUtils.getSimpleStack(Items.writable_book), ItemUtils.getItemStackOfAmountFromOreDict("wireGt01Tin", 1)}, ItemBookWritten_MultiPowerStorage);
		RecipeUtils.addShapelessGregtechRecipe(new ItemStack[]{ItemUtils.getSimpleStack(Items.writable_book), ItemUtils.getItemStackOfAmountFromOreDict("dustUranium", 1)}, ItemBookWritten_NuclearManual);
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


