package gtPlusPlus.xmod.gregtech.registration.gregtech;

import java.util.ArrayList;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine_GT_Recipe;
import gregtech.api.util.Recipe_GT;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class GregtechComponentAssembler {

	public static void run() {
		GregtechItemList.Machine_LV_Component_Maker.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(985,
				"basicmachine.componentmaker.tier.01", "Basic Component Maker", 1, "Components, Unite!",
				Recipe_GT.Gregtech_Recipe_Map.sComponentAssemblerRecipes, 6, 1, 16000, 0, 1, "Assembler.png", "", false,
				false, 0, "ASSEMBLER",
				new Object[] { "ACA", "VMV", "WCW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V',
						GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'A',
						GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
						GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
						GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4 }).getStackForm(1L));
		GregtechItemList.Machine_MV_Component_Maker.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(986,
				"basicmachine.componentmaker.tier.02", "Advanced Component Maker", 2, "Components, Unite!",
				Recipe_GT.Gregtech_Recipe_Map.sComponentAssemblerRecipes, 6, 1, 16000, 0, 1, "Assembler.png", "", false,
				false, 0, "ASSEMBLER",
				new Object[] { "ACA", "VMV", "WCW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V',
						GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'A',
						GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
						GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
						GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4 }).getStackForm(1L));
		GregtechItemList.Machine_HV_Component_Maker.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(987,
				"basicmachine.componentmaker.tier.03", "Advanced Component Maker II", 3, "Components, Unite!",
				Recipe_GT.Gregtech_Recipe_Map.sComponentAssemblerRecipes, 6, 1, 16000, 0, 1, "Assembler.png", "", false,
				false, 0, "ASSEMBLER",
				new Object[] { "ACA", "VMV", "WCW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V',
						GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'A',
						GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
						GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
						GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4 }).getStackForm(1L));
		GregtechItemList.Machine_EV_Component_Maker.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(988,
				"basicmachine.componentmaker.tier.04", "Advanced Component Maker III", 4, "Components, Unite!",
				Recipe_GT.Gregtech_Recipe_Map.sComponentAssemblerRecipes, 6, 1, 16000, 0, 1, "Assembler.png", "", false,
				false, 0, "ASSEMBLER",
				new Object[] { "ACA", "VMV", "WCW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V',
						GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'A',
						GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
						GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
						GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4 }).getStackForm(1L));
		GregtechItemList.Machine_IV_Component_Maker.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(989,
				"basicmachine.componentmaker.tier.05", "Advanced Component Maker IV", 5, "Components, Unite!",
				Recipe_GT.Gregtech_Recipe_Map.sComponentAssemblerRecipes, 6, 1, 16000, 0, 1, "Assembler.png", "", false,
				false, 0, "ASSEMBLER",
				new Object[] { "ACA", "VMV", "WCW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V',
						GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'A',
						GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
						GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
						GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4 }).getStackForm(1L));

		// Motors
		//addRecipeMotor(0, Materials.Lead, 1, Materials.RedAlloy, 1, Materials.WroughtIron, Materials.IronMagnetic);
		addRecipeMotor(1, Materials.Copper, 1, Materials.Tin, 1, Materials.Iron, Materials.IronMagnetic);
		addRecipeMotor(2, Materials.Copper, 2, Materials.Copper, 1, Materials.Aluminium, Materials.SteelMagnetic);
		addRecipeMotor(3, Materials.Copper, 4, Materials.Gold, 1, Materials.StainlessSteel, Materials.SteelMagnetic);
		addRecipeMotor(4, Materials.Copper, 8, Materials.Aluminium, 1, Materials.Titanium, Materials.NeodymiumMagnetic);
		addRecipeMotor(5, Materials.Copper, 16, Materials.Tungsten, 1, Materials.TungstenSteel,
				Materials.NeodymiumMagnetic);

		// Pistons
		//addRecipePiston(0, Materials.Lead, Materials.Lead);
		addRecipePiston(1, Materials.Steel, Materials.Tin);
		addRecipePiston(2, Materials.Aluminium, Materials.Copper);
		addRecipePiston(3, Materials.StainlessSteel, Materials.Gold);
		addRecipePiston(4, Materials.Titanium, Materials.Aluminium);
		addRecipePiston(5, Materials.TungstenSteel, Materials.Tungsten);

		// Conveyors
		//addRecipeConveyor(0, Materials.Lead);
		addRecipeConveyor(1, Materials.Tin);
		addRecipeConveyor(2, Materials.Copper);
		addRecipeConveyor(3, Materials.Gold);
		addRecipeConveyor(4, Materials.Aluminium);
		addRecipeConveyor(5, Materials.Tungsten);

		// Pumps
		//addRecipePump(0, Materials.Lead, Materials.Lead, Materials.Lead);
		addRecipePump(1, Materials.Tin, Materials.Copper, Materials.Tin);
		addRecipePump(2, Materials.Bronze, Materials.Steel, Materials.Copper);
		addRecipePump(3, Materials.Steel, Materials.StainlessSteel, Materials.Gold);
		addRecipePump(4, Materials.StainlessSteel, Materials.Titanium, Materials.Aluminium);
		addRecipePump(5, Materials.TungstenSteel, Materials.TungstenSteel, Materials.Tungsten);

		// Robot Arms
		//addRecipeRobotArm(0, Materials.Lead, Materials.Lead);
		addRecipeRobotArm(1, Materials.Steel, Materials.Tin);
		addRecipeRobotArm(2, Materials.Aluminium, Materials.Copper);
		addRecipeRobotArm(3, Materials.StainlessSteel, Materials.Gold);
		addRecipeRobotArm(4, Materials.Titanium, Materials.Aluminium);
		addRecipeRobotArm(5, Materials.TungstenSteel, Materials.Tungsten);

		// Field Generators
		//addRecipeFieldGenerator(0, Materials.Lead);
		addRecipeFieldGenerator(1);
		addRecipeFieldGenerator(2);
		addRecipeFieldGenerator(3);
		addRecipeFieldGenerator(4);
		addRecipeFieldGenerator(5);

		// Emitters
		//addRecipeEmitter(0, Materials.Lead, Materials.IronMagnetic);
		addRecipeEmitter(1, Materials.Tin, Materials.Brass, Materials.Quartzite);
		addRecipeEmitter(2, Materials.Copper, Materials.Electrum, Materials.NetherQuartz);;
		addRecipeEmitter(3, Materials.Gold, Materials.Chrome, Materials.Emerald);
		addRecipeEmitter(4, Materials.Aluminium, Materials.Platinum, Materials.EnderPearl);
		addRecipeEmitter(5, Materials.Tungsten, Materials.Osmium, Materials.EnderEye);

		// Sensors
		//addRecipeSensor(0, Materials.WroughtIron, Materials.IronMagnetic, Materials.Apatite);
		addRecipeSensor(1, Materials.Steel, Materials.Brass, Materials.Quartzite);
		addRecipeSensor(2, Materials.Aluminium, Materials.Electrum, Materials.NetherQuartz);
		addRecipeSensor(3, Materials.StainlessSteel, Materials.Chrome, Materials.Emerald);
		addRecipeSensor(4, Materials.Titanium, Materials.Platinum, Materials.EnderPearl);
		addRecipeSensor(5, Materials.TungstenSteel, Materials.Osmium, Materials.EnderEye);

	}

	private static boolean addRecipeMotor(int tier, Materials wire, int wirethickness, Materials cable,
			int cablethickness, Materials stick, Materials magstick) {
		try {
			String mWT = "" + wirethickness;
			String mCT = "" + cablethickness;

			if (wirethickness < 10) {
				mWT = "0" + wirethickness;
			}
			if (cablethickness < 10) {
				mCT = "0" + cablethickness;
			}

			OrePrefixes prefixWire = OrePrefixes.getOrePrefix("wireGt" + mWT);
			OrePrefixes prefixCable = OrePrefixes.getOrePrefix("cableGt" + mCT);

			ItemStack wireStack = ItemUtils.getGregtechOreStack(prefixWire, wire, 4);
			ItemStack cableStack = ItemUtils.getGregtechOreStack(prefixCable, cable, 2);
			ItemStack rodStack = ItemUtils.getGregtechOreStack(OrePrefixes.stick, stick, 2);
			ItemStack magrodStack = ItemUtils.getGregtechOreStack(OrePrefixes.stick, magstick, 1);

			ItemStack Input[] = { wireStack, cableStack, rodStack, magrodStack };

			return CORE.RA.addComponentMakerRecipe(Input, GT_Values.NF,
					ItemList.valueOf("Electric_Motor_" + GT_Values.VN[tier]).get(1), (tier * 40),
					((int) GT_Values.V[tier] / 16) * 15);
		}
		catch (Throwable t) {
			return false;
		}
	}

	private static boolean addRecipePiston(int tier, Materials mat, Materials cable) {
		try {

			OrePrefixes prefixCable = OrePrefixes.cableGt01;
			ItemStack cableStack = ItemUtils.getGregtechOreStack(prefixCable, cable, 2);
			ItemStack rodStack = ItemUtils.getGregtechOreStack(OrePrefixes.stick, mat, 2);
			ItemStack plateStack = ItemUtils.getGregtechOreStack(OrePrefixes.plate, mat, 3);
			ItemStack gearStack = ItemUtils.getGregtechOreStack(OrePrefixes.gearGtSmall, mat, 1);
			ItemStack motorStack = ItemList.valueOf("Electric_Motor_" + GT_Values.VN[tier]).get(1);

			ItemStack Input[] = { plateStack, cableStack, rodStack, gearStack, motorStack };

			return CORE.RA.addComponentMakerRecipe(Input, GT_Values.NF,
					ItemList.valueOf("Electric_Piston_" + GT_Values.VN[tier]).get(1), (tier * 40),
					((int) GT_Values.V[tier] / 16) * 15);
		}
		catch (Throwable t) {
			return false;
		}
	}

	private static boolean addRecipeConveyor(int tier, Materials cable) {
		try {
			OrePrefixes prefixCable = OrePrefixes.cableGt01;
			ItemStack cableStack = ItemUtils.getGregtechOreStack(prefixCable, cable, 1);
			ItemStack motorStack = ItemList.valueOf("Electric_Motor_" + GT_Values.VN[tier]).get(2);
			boolean mAdd[];
			final ArrayList<ItemStack> oreDictList = OreDictionary.getOres("plateAnyRubber");
			if (!oreDictList.isEmpty()) {
				int mcount = 0;
				mAdd = new boolean[oreDictList.size()];
				for (ItemStack mRubberType : oreDictList) {
					final ItemStack returnValue = mRubberType.copy();
					returnValue.stackSize = 6;
					ItemStack Input[] = { cableStack, motorStack, returnValue };
					mAdd[mcount++] = CORE.RA.addComponentMakerRecipe(Input, GT_Values.NF,
							ItemList.valueOf("Conveyor_Module_" + GT_Values.VN[tier]).get(1), (tier * 40),
							((int) GT_Values.V[tier] / 16) * 15);
				}
				int added = 0;
				for (boolean y : mAdd) {
					if (y) {
						added++;
					}
				}
				if (added >= (oreDictList.size() / 2)) {
					return true;
				}
				else {
					return false;
				}
			}
			else {
				ItemStack Input[] = { cableStack, motorStack,
						ItemUtils.getItemStackOfAmountFromOreDictNoBroken("plateRubber", 6) };
				return CORE.RA.addComponentMakerRecipe(Input, GT_Values.NF,
						ItemList.valueOf("Conveyor_Module_" + GT_Values.VN[tier]).get(1), (tier * 40),
						((int) GT_Values.V[tier] / 16) * 15);
			}

		}
		catch (Throwable t) {
			return false;
		}
	}

	private static boolean addRecipePump(int tier, Materials main, Materials pipe, Materials cable) {
		try {
			ItemStack cableStack = ItemUtils.getGregtechOreStack(OrePrefixes.cableGt01, main, 1);
			ItemStack screwStack = ItemUtils.getGregtechOreStack(OrePrefixes.screw, main, 1);
			ItemStack rotorStack = ItemUtils.getGregtechOreStack(OrePrefixes.rotor, main, 1);
			ItemStack pipeStack = ItemUtils.getGregtechOreStack(OrePrefixes.pipeMedium, pipe, 1);
			ItemStack motorStack = ItemList.valueOf("Electric_Motor_" + GT_Values.VN[tier]).get(1);
			boolean mAdd[];
			final ArrayList<ItemStack> oreDictList = OreDictionary.getOres("ringAnyRubber");
			if (!oreDictList.isEmpty()) {
				int mcount = 0;
				mAdd = new boolean[oreDictList.size()];
				for (ItemStack mRubberType : oreDictList) {
					final ItemStack returnValue = mRubberType.copy();
					returnValue.stackSize = 2;
					ItemStack Input[] = { cableStack, screwStack, rotorStack, pipeStack, motorStack, returnValue };
					mAdd[mcount++] = CORE.RA.addComponentMakerRecipe(Input, GT_Values.NF,
							ItemList.valueOf("Electric_Pump_" + GT_Values.VN[tier]).get(1), (tier * 40),
							((int) GT_Values.V[tier] / 16) * 15);
				}
				int added = 0;
				for (boolean y : mAdd) {
					if (y) {
						added++;
					}
				}
				if (added >= (oreDictList.size() / 2)) {
					return true;
				}
				else {
					return false;
				}
			}
			else {
				ItemStack Input[] = { cableStack, screwStack, rotorStack, pipeStack, motorStack,
						ItemUtils.getItemStackOfAmountFromOreDictNoBroken("ringRubber", 2) };
				return CORE.RA.addComponentMakerRecipe(Input, GT_Values.NF,
						ItemList.valueOf("Electric_Pump_" + GT_Values.VN[tier]).get(1), (tier * 40),
						((int) GT_Values.V[tier] / 16) * 15);
			}

		}
		catch (Throwable t) {
			return false;
		}
	}

	private static boolean addRecipeRobotArm(int tier, Materials stick, Materials cable) {
		try {
			ItemStack cableStack = ItemUtils.getGregtechOreStack(OrePrefixes.cableGt01, cable, 3);
			ItemStack rodStack = ItemUtils.getGregtechOreStack(OrePrefixes.stick, stick, 2);
			ItemStack motorStack = ItemList.valueOf("Electric_Motor_" + GT_Values.VN[tier]).get(2);
			ItemStack pistonStack = ItemList.valueOf("Electric_Piston_" + GT_Values.VN[tier]).get(1);
			ItemStack circuitStack;
			if (CI.getTieredCircuit(tier) instanceof String) {
				circuitStack = ItemUtils.getItemStackOfAmountFromOreDict((String) CI.getTieredCircuit(tier), 1);
			}
			else {
				circuitStack = (ItemStack) CI.getTieredCircuit(tier);
			}
			ItemStack Input[] = { circuitStack, cableStack, rodStack, pistonStack, motorStack };
			return CORE.RA.addComponentMakerRecipe(Input, GT_Values.NF,
					ItemList.valueOf("Robot_Arm_" + GT_Values.VN[tier]).get(1), (tier * 40),
					((int) GT_Values.V[tier] / 16) * 15);
		}
		catch (Throwable t) {
			return false;
		}
	}

	private static boolean addRecipeFieldGenerator(int tier) {
		try {
			ItemStack gem;
			String mWT;
			if (tier == 1) mWT = "01";
			else if (tier == 2) mWT = "02";
			else if (tier == 3) mWT = "04";
			else if (tier == 4) mWT = "08";
			else if (tier == 5) mWT = "16";
			else mWT = "01";
			
			if (tier == 1) gem = ItemUtils.getSimpleStack(Items.ender_pearl);
			else if (tier == 2) gem = ItemUtils.getSimpleStack(Items.ender_eye);
			else if (tier == 3) gem = ItemList.QuantumEye.get(1);
			else if (tier == 4) gem = ItemUtils.getSimpleStack(Items.nether_star);
			else if (tier == 5) gem = ItemList.QuantumStar.get(1);
			else gem = ItemUtils.getSimpleStack(Items.ender_pearl);
			

			OrePrefixes prefixWire = OrePrefixes.getOrePrefix("wireGt" + mWT);

			ItemStack wireStack = ItemUtils.getGregtechOreStack(prefixWire, Materials.Osmium, 4);

			ItemStack circuitStack;
			if (CI.getTieredCircuit(tier) instanceof String) {
				circuitStack = ItemUtils.getItemStackOfAmountFromOreDict((String) CI.getTieredCircuit(tier), 4);
			}
			else {
				circuitStack = (ItemStack) CI.getTieredCircuit(tier);
			}
			ItemStack Input[] = { circuitStack, wireStack, gem};
			return CORE.RA.addComponentMakerRecipe(Input, GT_Values.NF,
					ItemList.valueOf("Field_Generator_" + GT_Values.VN[tier]).get(1), (tier * 40),
					((int) GT_Values.V[tier] / 16) * 15);
		}
		catch (Throwable t) {
			return false;
		}
	}

	private static boolean addRecipeEmitter(int tier, Materials cable, Materials stick, Materials gem) {
		try {
			ItemStack cableStack = ItemUtils.getGregtechOreStack(OrePrefixes.cableGt01, cable, 2);
			ItemStack gemstack = ItemUtils.getGregtechOreStack(OrePrefixes.gem, gem, 1);
			ItemStack magrodStack = ItemUtils.getGregtechOreStack(OrePrefixes.stick, stick, 4);
			ItemStack circuitStack;
			if (CI.getTieredCircuit(tier) instanceof String) {
				circuitStack = ItemUtils.getItemStackOfAmountFromOreDict((String) CI.getTieredCircuit(tier), 2);
			}
			else {
				circuitStack = (ItemStack) CI.getTieredCircuit(tier);
			}
			ItemStack Input[] = { circuitStack, cableStack, gemstack, magrodStack};
			return CORE.RA.addComponentMakerRecipe(Input, GT_Values.NF,
					ItemList.valueOf("Emitter_" + GT_Values.VN[tier]).get(1), (tier * 40),
					((int) GT_Values.V[tier] / 16) * 15);
		}
		catch (Throwable t) {
			return false;
		}
	}

	private static boolean addRecipeSensor(int tier, Materials plate, Materials rod, Materials gem) {
		try {
			ItemStack cableStack = ItemUtils.getGregtechOreStack(OrePrefixes.plate, plate, 4);
			ItemStack magrodStack = ItemUtils.getGregtechOreStack(OrePrefixes.stick, rod, 1);
			ItemStack gemStack = ItemUtils.getGregtechOreStack(OrePrefixes.gem, gem, 1);

			if (tier == 0) {
				if (CI.getTieredCircuit(tier+1) instanceof String) {
					gemStack = ItemUtils.getItemStackOfAmountFromOreDict((String) CI.getTieredCircuit(tier+1), 1);
				}
				else {
					gemStack = (ItemStack) CI.getTieredCircuit(tier+1);
				}
			}

			ItemStack circuitStack2;
			if (CI.getTieredCircuit(tier) instanceof String) {
				circuitStack2 = ItemUtils.getItemStackOfAmountFromOreDict((String) CI.getTieredCircuit(tier), 1);
			}
			else {
				circuitStack2 = (ItemStack) CI.getTieredCircuit(tier);
			}
			ItemStack Input[] = { gemStack, cableStack, circuitStack2, magrodStack };
			return CORE.RA.addComponentMakerRecipe(Input, GT_Values.NF,
					ItemList.valueOf("Sensor_" + GT_Values.VN[tier]).get(1), (tier * 40),
					((int) GT_Values.V[tier] / 16) * 15);
		}
		catch (Throwable t) {
			return false;
		}
	}

}
