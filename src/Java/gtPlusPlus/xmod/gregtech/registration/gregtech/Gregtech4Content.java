package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.recipe.RecipeUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.automation.GT_MetaTileEntity_TesseractGenerator;
import gtPlusPlus.xmod.gregtech.common.tileentities.automation.GT_MetaTileEntity_TesseractTerminal;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.GT4Entity_AutoCrafter;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.GT4Entity_ThermalBoiler;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.shelving.*;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class Gregtech4Content {

	// ID Range 828, 829, 833 - 850

	public static void run() {
		if (LoadedMods.Gregtech) {
			workbenches();
			thermalBoiler();
			multiCrafter();
			tesseracts();
			shelves();
		}
	}

	private static void workbenches() {
		// Gregtech 4 Workbenches
		//Utils.LOG_INFO("Gregtech 4 Content | Registering Workbenches.");
		// Free //GregtechItemList.GT4_Workbench_Bronze.set(new
		// GT_MetaTileEntity_BronzeCraftingTable(828, "workbench.bronze",
		// "Bronze Workbench", 0).getStackForm(1L));
	}

	private static void tesseracts() {
		// Gregtech 4 Tesseracts
		Utils.LOG_INFO("Gregtech 4 Content | Registering Tesseracts.");
		GregtechItemList.GT4_Tesseract_Generator
				.set(new GT_MetaTileEntity_TesseractGenerator(833, "tesseract.generator", "Tesseract Generator", 4)
						.getStackForm(1L));
		GregtechItemList.GT4_Tesseract_Terminal
				.set(new GT_MetaTileEntity_TesseractTerminal(834, "tesseract.terminal", "Tesseract Terminal", 4)
						.getStackForm(1L));
	}

	private static void shelves() {
		// Gregtech 4 Shelves
		Utils.LOG_INFO("Gregtech 4 Content | Registering Shelves.");
		GregtechItemList.GT4_Shelf
				.set(new GT4Entity_Shelf(870, "gtplusplus.shelf.wooden", "Wood encased Shelf", "Stores Books & Things")
						.getStackForm(1L));
		GregtechItemList.GT4_Shelf_Iron.set(
				new GT4Entity_Shelf_Iron(871, "gtplusplus.shelf.iron", "Metal encased Shelf", "Stores Books & Things")
						.getStackForm(1L));
		GregtechItemList.GT4_Shelf_FileCabinet.set(new GT4Entity_Shelf_FileCabinet(872, "gtplusplus.shelf.filecabinet",
				"File Cabinet", "Stores Books & Things").getStackForm(1L));
		GregtechItemList.GT4_Shelf_Desk.set(
				new GT4Entity_Shelf_Desk(873, "gtplusplus.shelf.desk", "Metal encased Desk", "Stores Books & Things")
						.getStackForm(1L));
		GregtechItemList.GT4_Shelf_Compartment.set(new GT4Entity_Shelf_Compartment(874, "gtplusplus.shelf.compartment",
				"Compartment", "Stores Books & Things").getStackForm(1L));
	}

	private static void thermalBoiler() {
		// Gregtech 4 Thermal Boiler
		if (CORE.configSwitches.enableMultiblock_ThermalBoiler){
		Utils.LOG_INFO("Gregtech 4 Content | Registering Thermal Boiler.");
		GregtechItemList.GT4_Thermal_Boiler
				.set(new GT4Entity_ThermalBoiler(875, "gtplusplus.thermal.boiler", "Thermal Boiler").getStackForm(1L));
		
		
		
		//Thermal Boiler Manual
	    ItemStack manual_Boiler = GT_Utility.getWrittenBook(
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
	    				"Input and Output Slots are fully optional, you can place multiple ones of them or even none on the Machine. A Machine without Input couldn't process any Recipes, while a Machine without Output just voids all outputted Items and Liquids.", "It might be useful to use the Screwdriver on the Output Hatches to determine what is outputted where." });
		
	    RecipeUtils.addShapelessGregtechRecipe(new ItemStack[]{ItemUtils.getSimpleStack(Items.writable_book), ItemUtils.getSimpleStack(Items.lava_bucket)}, manual_Boiler);
		}
		
	}

	private static void multiCrafter() {
		// Gregtech 4 Multiblock Auto-Crafter
		Utils.LOG_INFO("Gregtech 4 Content | Registering Multiblock Crafter.");
		GregtechItemList.GT4_Multi_Crafter.set(
				new GT4Entity_AutoCrafter(876, "gtplusplus.autocrafter.multi", "Large Scale Auto-Asesembler v1.01").getStackForm(1L));
	}
}
