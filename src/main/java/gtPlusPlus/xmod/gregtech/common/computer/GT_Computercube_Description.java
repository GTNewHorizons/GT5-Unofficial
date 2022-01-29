package gtPlusPlus.xmod.gregtech.common.computer;

import java.util.ArrayList;

import gregtech.api.enums.ItemList;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import net.minecraft.item.ItemStack;

public class GT_Computercube_Description {
  public static ArrayList<GT_Computercube_Description> sDescriptions = new ArrayList<GT_Computercube_Description>();
  
  public String[] mDescription;
  
  public ItemStack[] mStacks;
  
  public GT_Computercube_Description(String[] aDescription, ItemStack[] aStacks) {
    this.mDescription = aDescription;
    this.mStacks = aStacks;
    sDescriptions.add(this);
  }
  
  public static void addStandardDescriptions() {
		Logger.INFO("Adding Default Description Set of the Computer Cube");
    new GT_Computercube_Description(new String[] { 
          "Lightning Rod", "Also known as the Bane of", "Alblaka. The Lightning Rod", "enables you to gain Energy", "from Lightning! To set it up", "you just need the Block", "itself, 4 HV-Transformers", "and a crapton of Ironfences,", "which you then place on top", "of it. After that you have to", 
          "wait for a Thunderstorm and", "when you are lucky you get", "2.5 MFSU of Energy out of", "it. If a Rod is high enough", "then Rain is also enough to", "get stroke, but with less", "probability ofcourse." }, new ItemStack[] { 
          GT_ModHandler.getIC2Item("ironFence", 1), GT_ModHandler.getIC2Item("ironFence", 1), GT_ModHandler.getIC2Item("ironFence", 1), GT_ModHandler.getIC2Item("ironFence", 1), ItemList.Machine_IV_LightningRod.get(1), null, null, null, null, null, 
          null, null, null, null });
    new GT_Computercube_Description(new String[] { 
          "Quantum Chest", "You want to store tons of", "Materials into your Chests", "but you hate the Item limit", "of them? Not anymore! The", "Quantum Chest is able to", "store an INFINITE* amount", "of one single Item type per", "Chest.", "This Chest stores your Items", 
          "like Data and ever has a", "Stack of the Item ready for", "extraction. It is compatible", "with any Item that doesnt", "have a NBT-Tag. You ask what", "NBT is? I know it, thats enough.", "Up to 2147483391*" }, new ItemStack[] { 
          ItemList.Quantum_Chest_IV.get(1), ItemList.Quantum_Chest_EV.get(1), ItemList.Quantum_Chest_HV.get(1), ItemList.Quantum_Chest_MV.get(1), ItemList.Quantum_Chest_LV.get(1), null, 
          null, null, null, null, null, null, null, null });
    new GT_Computercube_Description(new String[] { 
            "Quantum Tank", "You want to store tons of", "Fluids into your Tanks", "but you hate the capacity", "of them? Not anymore! The", "Quantum Tank is able to", "store an INFINITE* amount", "of one single Fluid type per", "Tank.", "This Tank stores your Fluids", 
            "like Data and ever has a", "Stack of the Fluid ready for", "extraction. It is compatible", "with any Fluid.           ", "You ask what", "NBT is? I know it, thats enough.", "* = 2147483391" }, new ItemStack[] { 
            ItemList.Quantum_Tank_IV.get(1), ItemList.Quantum_Tank_EV.get(1), ItemList.Quantum_Tank_HV.get(1), ItemList.Quantum_Tank_MV.get(1), ItemList.Quantum_Tank_LV.get(1), null, 
            null, null, null, null, null, null, null, null });
    new GT_Computercube_Description(new String[] { 
          "Computer Cube", "The Device you are", "currently using. This Computer", "is running the G.L.A.D.-OS,", "which is containing many", "usefull Apps:", "- Reactor Planner", "- Seedbag Scanner", "- Recipelists for GT-Devices", "- ", 
          "- ", "- ", "- ", "- ", "And the Description List you", "are currently reading.", "~This Device has private Access~" }, new ItemStack[] { 
          null, null, null, null, GregtechItemList.Gregtech_Computer_Cube_Machine.get(1), null, null, null, null, null, 
          null, null, null, null });
		/*    new GT_Computercube_Description(new String[] { 
		  "UUM-Assembler", "It's like an automatic", "Crafting Table just for UUM", "It can store 20 UUM-Recipes", "and produces those on demand", "It costs 512EU per used piece", "of Universal-Usable-Matter(TM).", "The integrated Quantum Chest", "allows it to store all your", "UUM inside it.", 
		  "Top and Bottom are for Input,", "while the Output is on the", "Sides. The Output is designed,", "to work with RP-Managers, so", "build it into your recursive", "Autocraftingsystem.", "" }, new ItemStack[] { 
		  null, null, null, GT_ModHandler.getIC2Item("matter", 1), new ItemStack(GregTech_API.sBlockList[1], 1, 5), null, null, null, null, null, 
		  null, null, null, null });
		    new GT_Computercube_Description(new String[] { 
		  "Sonictron", "You like Music? Then the", "Sonictron 9001 is your best", "choice! You can compose Alarms,", "Doorbell Sounds or boring", "Elevator Music, with the 64 Slots", "inside it. Just leftclick them", "to switch the Sound, rightclick", "them to switch the modulation", "and shiftclick to remove it.", 
		  "Then apply Redstone to play", "With the mobile Version you can", "play sounds everywhere, after", "you copied them from a normal", "Sonictron via rightclicking", "Sneakrightclicking pastes", "Emits Redstone when finished." }, new ItemStack[] { 
		  null, null, null, GregTech_API.getGregTechItem(32, 1, 0), new ItemStack(GregTech_API.sBlockList[1], 1, 6), null, null, null, null, null, 
		  null, null, null, null });
		    new GT_Computercube_Description(new String[] { 
		  "L.E.S.U.", "The unlaggiest Multiblock ever!", "One Controllerblock, and as many", "'stupid' Storageblocks as you want.", "To use it, place one Controller", "and then place the LESU-Storages", "adjacent to it or other placed", "LESU-Storages. The Tier (max EU/t)", "of it depends on the amount of", "adjacent Storages. The", 
		  "Storageblocks are NOT TileEntities,", "what means that they cause as much", "Lag as a random Dirtblock. And the", "Controller Block only checks ONCE", "for the Storages, so no", "Blockiterationlag, AT. ALL. Anyone,", "who says that they lag gets murdered!" }, new ItemStack[] { 
		  null, null, null, new ItemStack(GregTech_API.sBlockList[0], 1, 6), new ItemStack(GregTech_API.sBlockList[1], 1, 7), null, null, null, null, null, 
		  null, null, null, null });
		    new GT_Computercube_Description(new String[] { 
		  "I.D.S.U.", "The Interdimensional Storage Unit", "is a Device, which is like a", "wireless, crossdimensional and", "enderchestlike EU-Storage Block", "", "Every Player has one Network of", "these. The ID is determined by", "the Hashcode of the Name from the", "first Player, who opens it's GUI", 
		  "", "It stores up to 1 Billion EU", "and emits EV. But you need at", "least two of them for Energy", "Transfer", "", "" }, new ItemStack[] { 
		  null, null, null, null, new ItemStack(GregTech_API.sBlockList[1], 1, 8), null, null, null, null, null, 
		  null, null, null, null });
		    new GT_Computercube_Description(new String[] { 
		  "A.E.S.U.", "The Adjustable Energy Storage Unit", "is like 10 MFSU and has an", "adjustable Output between 0 and", "2048EU/t. You could use it as a", "Transformer. It is Tier-IV, so", "it's basically needed to charge", "Energy Orbs and Lapotron Packs", "", "Not much else to say about it.", 
		  "", "", "", "", "", "", "" }, new ItemStack[] { 
		  null, null, null, null, new ItemStack(GregTech_API.sBlockList[1], 1, 9), null, null, null, null, null, 
		  null, null, null, null });
		    new GT_Computercube_Description(new String[] { 
		  "Charge-O-Mat", "An automatable Charging Bench", "It puts (de-)charged Tools into", "the right Outputslots, which are", "accessible on the Sides of it.", "", "The Energy Orb inside stores enough", "to charge your QSuit almost instantly", "", "This is a Tier-V Charging Station", 
		  "even when the Max-IN/OUT is only", "2048EU/t. It also charges your Armor", "when you are standing close to it.", "", "If you apply Redstone, then it", "decharges instead.", "" }, new ItemStack[] { 
		  null, null, null, null, new ItemStack(GregTech_API.sBlockList[1], 1, 10), null, null, null, null, null, 
		  null, null, null, null });
		    new GT_Computercube_Description(new String[] { 
		  "Centrifuge", "This is a Machine to seperate", "Isotopes.", "", "It has a maximum Consumption Rate", "of 5EU/t, and its Maxinput is", "32EU/t. The time it needs depends", "on the Recipe you use.", "", "It needs Tin Cells for some Recipes,", 
		  "which you put in the Top Left Slot", "", "Top = Input", "Bottom = Tin Cells", "Side = Output", "", "You can pipe Lava into this Device" }, new ItemStack[] { 
		  null, null, null, null, new ItemStack(GregTech_API.sBlockList[1], 1, 11), null, null, null, null, null, 
		  null, null, null, null });
		    new GT_Computercube_Description(new String[] { 
		  "Electrolyzer", "This is a Machine to seperate", "Molecules and electrolyze", "Watercells.", "", "It has a maximum Consumption Rate", "of 128EU/t, and its Maxinput is", "128EU/t. The time it needs depends", "on the Recipe you use.", "", 
		  "It needs Tin Cells for some Recipes,", "which you put in the Bottom Left Slot", "", "Top = Input", "Bottom = Tin Cells", "Side = Output", "" }, new ItemStack[] { 
		  null, null, null, null, new ItemStack(GregTech_API.sBlockList[1], 1, 25), null, null, null, null, null, 
		  null, null, null, null });
		    new GT_Computercube_Description(new String[] { 
		  "Grinder", "This Machines purpose is to", "macerate and grind Ores.", "It can ONLY grind Ores, don't", "try regular Macerator Recipes.", "It has a fixed Consumption Rate", "of 128EU/t, and its Maxinput is", "128EU/t. The time it needs is", "5 seconds per Ore Block", "It needs Water for most Recipes,", 
		  "which you put in the Bottom Left Slot", "Top = Input", "Bottom = Water", "Side = Output", "Its a lagfree Multiblock Structure,", "so you need a special Machine Casing", "for this Device. (see GUI)" }, new ItemStack[] { 
		  null, new ItemStack(Block.field_71943_B, 1), new ItemStack(GregTech_API.sBlockList[0], 1, 14), new ItemStack(GregTech_API.sBlockList[0], 1, 13), new ItemStack(GregTech_API.sBlockList[1], 1, 28), null, null, null, null, null, 
		  null, null, null, null });*/
    new GT_Computercube_Description(new String[] { 
          "Electric Blast Furnace", "You may know the Blast Furnace", "of Railcraft. This one works", "similar, as it can also produce", "Steel out of Iron and Coal.", "", "Its heat Capacity depends on the", "used Machine Casings for building", "it. The better they are, the more", "Heat it can achieve.", 
          "", "Top = Input 1", "Bottom = Input 2", "Side = Output", "Its a lagfree Multiblock Structure,", "so you need a special Machine Casing", "for this Device. (see GUI)" }, new ItemStack[] { 
          null, null, null, ItemList.Casing_HeatProof.get(1), ItemList.Machine_Multi_BlastFurnace.get(1), ItemList.Casing_Coil_Cupronickel.get(1), ItemList.Casing_Coil_Kanthal.get(1), ItemList.Casing_Coil_Nichrome.get(1), ItemList.Casing_Coil_TungstenSteel.get(1), ItemList.Casing_Coil_HSSG.get(1), 
          ItemList.Casing_Coil_HSSS.get(1), ItemList.Casing_Coil_Naquadah.get(1), ItemList.Casing_Coil_NaquadahAlloy.get(1), null });
		/*    new GT_Computercube_Description(new String[] { 
		  "Sawmill", "This Device turns your Logs", "into more Planks, than a normal", "Steve can produce with his Hands.", "", "Its byproduct, Wood Pulp, can be", "compressed into special Planks,", "which are burning like Charcoal.", "", "It needs Water for most Recipes,", 
		  "which you put in the Bottom Left Slot", "Top = Input", "Water Sides = Water", "Saw Side = Output", "Its a lagfree Multiblock Structure,", "so you need a special Machine Casing", "for this Device. (see GUI)" }, new ItemStack[] { 
		  null, null, GT_MetaItem_Material.instance.getStack(15, 1), GT_MetaItem_Dust.instance.getStack(15, 1), new ItemStack(GregTech_API.sBlockList[1], 1, 32), null, null, null, null, null, 
		  null, null, null, null });*/
    new GT_Computercube_Description(new String[] { 
          "Implosion Compressor", "You need to turn Dusts back", "into Gems? Or do you just want", "to make Iridium Plates?", "With a bit ITNT you can achieve", "that in this Device!", "", "We strongly recommend to use", "Flint Dust instead of Flints", "for making the ITNT.", 
          "", "Top = Input", "Explosion Sides = Output", "ITNT Side = ITNT Input", "Its a lagfree Multiblock Structure,", "so you need a special Machine Casing", "for this Device. (see GUI)" }, new ItemStack[] { 
          null, null, null, GT_ModHandler.getIC2Item("industrialTnt", 1, new ItemStack(net.minecraft.init.Blocks.tnt, 1)), ItemList.Machine_Multi_ImplosionCompressor.get(1), null, null, null, null, null, 
          null, null, null, null });
		/*    new GT_Computercube_Description(new String[] { 
		  "Superconductor", "Expensive, but superconducting", "nearly infinite EU/p and it has", "no Cableloss!", "Do not confuse this with the", "Superconductor Item!", "", "Supercondensator", "This is a special kind of Transformer", "It allows you to convert anything down", 
		  "to 8192 EU/t, what is like a normal HVT.", "But if you apply Redstone to it then it", "outputs friggin 1000000EU/t!!!", "", "You also need it for the Fusion Reactor.", "Some Machines will require that high", "Voltage in a short period of time." }, new ItemStack[] { 
		  null, null, GregTech_API.getGregTechItem(3, 1, 2), new ItemStack(GregTech_API.sBlockList[1], 1, 12), new ItemStack(GregTech_API.sBlockList[1], 1, 15), null, null, null, null, null, 
		  null, null, null, null });
		    new GT_Computercube_Description(new String[] { 
		  "Player Detector", "This nice little Device is able", "to detect Players in a Range of", "16-Spherical Meters and a", "EU-Consumption of 2.5EU/t.", "", "It can be switched to 3 Diffrent", "Modes, to detect YOURSELF, OTHERS", "and ALL Players by Rightclicking it.", "", 
		  "It doesnt detect regular Mobs.", "", "", "", "", "", "~This Device has private Access~" }, new ItemStack[] { 
		  null, null, null, null, new ItemStack(GregTech_API.sBlockList[1], 1, 13), null, null, null, null, null, 
		  null, null, null, null });*/
    new GT_Computercube_Description(new String[] { 
          "Matter Fabricator", "The Matter Fabricator is nothing", "else than a Mass Fabricator, which", "can ONLY run on Scrap and other", "Amplifiers.", "", "With the Default Config it is 100", "times more expensive than normal.", "Of course you can set the Config", "to 166666, to get your normal", 
          "Massfabricationrate back, or you", "could make Mass Fabrication even", "cheaper, if you really want to", "make Mass Fabrication that easy", "", "", "" }, new ItemStack[] { 
          null, null, null, GT_Utility.getFluidDisplayStack(FluidUtils.getUUM(1), false), ItemList.Machine_LV_Massfab.get(1), null, null, null, null, null, 
          null, null, null, null });
    new GT_Computercube_Description(new String[] { 
          "Electric Autocrafting Tables", "These are Crafting Tables for the", "common need of autocrafting in", "Factories. One Craft needs 5000EU to", "be performed, so you have actually to", "lay Wires to it. This Table is", "unique as its also able, to give you", "the used Capsulecellcontainers, made", "by Industrial Corp, back.", "You may use that behaviour to", 
          "craft anything releated to chemics,", "like the 2xKNO3-Recipe for Saltpeter.", "The 5 Modes are the following:", "1. Craft Recipe, 2. All 5 Modes", "3. Craft all as single Items", "4. 2x2-Grid and 5. a 3x3-Grid.", "It accepts only 32EU/p as Input." }, new ItemStack[] { 
          null, null, null, null, GregtechItemList.GT4_Electric_Auto_Workbench_LV.get(1), null, null, null, null, null, 
          null, null, null, null });
		/* new GT_Computercube_Description(new String[] { 
		  "Automation with GregTech", "Translocators and Buffers are the", "newest Way to automate your Machines.", "Screw Buildcraft, these EU-wasting", "Devices are much more awesome.", "They output 32EU/t to their directed", "IN- and OUT-puts, making them usefull", "for things, like saving wires.", "Translocators are taking Stuff from", "the Block at their green Inputfacing", 
		  "and putting it into the Block at the", "red Outputfacing. Buffers do the same,", "but the grab Items from their own", "Inventory, what makes them usefull", "as Pipe-replacement.", "Buffers also have Redstone Intelligence,", "which you can configure in their GUI." }, new ItemStack[] { 
		  null, null, new ItemStack(GregTech_API.sBlockList[1], 1, 19), new ItemStack(GregTech_API.sBlockList[1], 1, 18), new ItemStack(GregTech_API.sBlockList[1], 1, 17), null, null, null, null, null, 
		  null, null, null, null });
		 new GT_Computercube_Description(new String[] { 
		  "Silver Ore", "It's rarity is similar to Gold", "Silver can be used, to make", "Circuits cheaper, or you can use", "it for Redpowerstuff.", "", "", "", "", "", 
		  "", "", "", "", "", "", "" }, new ItemStack[] { 
		  null, null, GT_OreDictUnificator.get("dustSilver", 1), GregTech_API.getGregTechItem(0, 1, 17), new ItemStack(GregTech_API.sBlockList[2], 1, 1), null, null, null, null, null, 
		  null, null, null, null });
		 new GT_Computercube_Description(new String[] { 
		  "Sapphires and Rubys", "These spawn exactly like Emeralds.", "But Rubies are found in Deserts,", "while Sapphires can be found in", "Oceans.", "", "They currently make only a cheaper", "Recipe for Energycrystals and", "Lapotroncrystals, but they are", "Redpower Compatible.", 
		  "", "They also sometimes drop random", "other Gems, like Garnet for Ruby", "or green Sapphire for Sapphire", "in addition.", "", "" }, new ItemStack[] { 
		  null, GregTech_API.getGregTechItem(0, 1, 32), new ItemStack(GregTech_API.sBlockList[2], 1, 3), GregTech_API.getGregTechItem(0, 1, 33), new ItemStack(GregTech_API.sBlockList[2], 1, 4), null, null, null, null, null, 
		  null, null, null, null });
		 new GT_Computercube_Description(new String[] { 
		  "Bauxite Ore", "The Stuff out of which you can", "produce Aluminium and also", "Titanium.", "You find this Ore in Plains and", "Forests.", "", "If you think Aluminium is useless", "then note, that mobs NEVER spawn", "ontop of an Aluminium Block", 
		  "(Same applies also for Silver-,", "Gem- and Iridium Blocks)", "Production Chain:", "macerating Bauxite Ore", "electrolyzing 24 Bauxite Dust", "smelting Aluminium Dust in a", "Blast Furnace" }, new ItemStack[] { 
		  new ItemStack(GregTech_API.sBlockList[0], 1, 7), GregTech_API.getGregTechItem(0, 1, 18), GregTech_API.getGregTechItem(1, 1, 18), GregTech_API.getGregTechItem(1, 1, 17), new ItemStack(GregTech_API.sBlockList[2], 1, 5), null, null, null, null, null, 
		  null, null, null, null });
		 new GT_Computercube_Description(new String[] { 
		  "Titanium", "Produced by centrifuging Bauxitedust", "as a byproduct, this Material can make", "anything much more resistant against", "damage, like Explosions.", "Blocks made of Titaniumingots have a", "large Blastresistance", "", "It can also be used to craft tons of", "mixed Metal Ingots", 
		  "", "", "", "", "", "", "" }, new ItemStack[] { 
		  new ItemStack(GregTech_API.sBlockList[0], 1, 8), GregTech_API.getGregTechItem(0, 1, 19), GregTech_API.getGregTechItem(1, 1, 19), GregTech_API.getGregTechItem(1, 1, 17), new ItemStack(GregTech_API.sBlockList[2], 1, 5), null, null, null, null, null, 
		  null, null, null, null });
		 new GT_Computercube_Description(new String[] { 
		  "Iridium Ore", "You can find it only when you", "stripmine very large Areas with", "Quarries and such. There is only", "one in every 5th-10th Chunk.", "It's even more rare in Oceans!", "", "Some people disable the UUM-Recipe", "for Iridium, for making getting it", "an Achievement.", 
		  "", "However Iridium Ore contains traces", "of Platinum, so it's best to use the", "Industrial Grinder for this Ore.", "", "", "" }, new ItemStack[] { 
		  null, null, GT_OreDictUnificator.get("plateAlloyIridium", 1), GT_ModHandler.getIC2Item("iridiumOre", 1), new ItemStack(GregTech_API.sBlockList[2], 1, 2), null, null, null, null, null, 
		  null, null, null, null });
		 new GT_Computercube_Description(new String[] { 
		  "Helium Coolant Cell", "These are just cheaper, than the", "Water based Coolant Cells, and can", "also hold six times more Heat.", "", "Helium Cells can also be used for", "making Luminators and Mininglasers", "", "", "", 
		  "", "", "", "", "", "", "" }, new ItemStack[] { 
		  GregTech_API.getGregTechItem(2, 1, 6), GregTech_API.getGregTechItem(2, 1, 3), GregTech_API.getGregTechItem(34, 1, 0), GregTech_API.getGregTechItem(35, 1, 0), GregTech_API.getGregTechItem(36, 1, 0), null, null, null, null, null, 
		  null, null, null, null });
		 new GT_Computercube_Description(new String[] { 
		  "Destructopack", "Open its GUI via rightclick and", "dump all the useless Stuff from", "your Inventory into it, instead of", "littering Items into the World.", "", "", "", "", "", 
		  "", "", "", "", "", "", "" }, new ItemStack[] { 
		  null, null, null, null, GregTech_API.getGregTechItem(33, 1, 0), null, null, null, null, null, 
		  null, null, null, null });*/
    new GT_Computercube_Description(new String[] { 
          "Data Orbs", "They store Data.", "", "Rightclick on a Computer Cube, to", "extract a Reactorplan", "", "Sneak-Rightclick on it, to insert", "a Reactorplan", "", "Works also with Sonictrons", 
          "", "", "", "", "", "", "" }, new ItemStack[] { 
          null, null, null, null, ItemList.Tool_DataOrb.get(1), null, null, null, null, null, 
          null, null, null, null });
    new GT_Computercube_Description(new String[] { 
          "Energy Orbs", "100 Million EU in one Orb!", "", "This is a Tier-IV-Energystorage", "So a MFSU is not enough for it!", "", "Use it to create a Lapotron Pack,", "which is like an ultimate Lap Pack!", "", "", 
          "", "", "", "", "", "", "" }, new ItemStack[] { 
          null, null, null, null, ItemList.Energy_LapotronicOrb.get(1), null, null, null, null, null, 
          null, null, null, null });
    new GT_Computercube_Description(new String[] { 
          "Iridium Neutron Reflector", "It's used for Fusion Reactor Coils,", "and works like a normal one", "inside a Reactor, but it's also", "INDESTRUCTIBLE*.", "", "", "", "", "", 
          "", "", "", "", "", "", "* = for weardown" }, new ItemStack[] { 
          null, null, null, null, ItemList.Neutron_Reflector.get(1), null, null, null, null, null, 
          null, null, null, null });
		/*new GT_Computercube_Description(new String[] { 
		  "Rock Cutter", "You want to get whole Blocks, but", "your Drill is not enchantable?", "The Rock Cutter has an awesome", "SilkTouch-III-Function!", "", "It works like a Drill, but you", "get the whole Block instead of", "'macerated' Ores!", "", 
		  "Put those Blocks into a Macerator", "and double your Diamond Income!", "", "Or better. Use the Industrial", "Grinder to get even more", "Resources!", "" }, new ItemStack[] { 
		  null, null, null, null, GregTech_API.getGregTechItem(46, 1, 0), null, null, null, null, null, 
		  null, null, null, null });
		new GT_Computercube_Description(new String[] { 
		  "Tesla Staff", "This completly untested PvP-Weapon", "destroys electric Armor in one hit", "", "The Energy Orb inside it must be", "fully charged to let this work.", "", "We are not responsible for any", "Electrocution Damage to yourself,", "while using it.", 
		  "", "We also dont even know, if this", "Weapon has any effect AT ALL.", "", "", "", "" }, new ItemStack[] { 
		  null, null, null, null, GregTech_API.getGregTechItem(47, 1, 0), null, null, null, null, null, 
		  null, null, null, null });*/
  }
}
