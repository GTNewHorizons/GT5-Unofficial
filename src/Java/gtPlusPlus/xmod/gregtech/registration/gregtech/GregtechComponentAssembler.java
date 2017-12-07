package gtPlusPlus.xmod.gregtech.registration.gregtech;

import java.util.ArrayList;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine_GT_Recipe;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.Recipe_GT;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class GregtechComponentAssembler {

	public static void run(){
		GregtechItemList.Machine_LV_Component_Maker.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(985, "basicmachine.componentmaker.tier.01", "Basic Component Maker", 1, "Components, Unite!", Recipe_GT.Gregtech_Recipe_Map.sComponentAssemblerRecipes, 6, 1, 16000, 0, 1, "Assembler.png", "", false, false, 0, "ASSEMBLER", new Object[]{"ACA", "VMV", "WCW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'A', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4}).getStackForm(1L));
        GregtechItemList.Machine_MV_Component_Maker.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(986, "basicmachine.componentmaker.tier.02", "Advanced Component Maker", 2, "Components, Unite!", Recipe_GT.Gregtech_Recipe_Map.sComponentAssemblerRecipes, 6, 1, 16000, 0, 1, "Assembler.png", "", false, false, 0, "ASSEMBLER", new Object[]{"ACA", "VMV", "WCW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'A', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4}).getStackForm(1L));
        GregtechItemList.Machine_HV_Component_Maker.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(987, "basicmachine.componentmaker.tier.03", "Advanced Component Maker II", 3, "Components, Unite!", Recipe_GT.Gregtech_Recipe_Map.sComponentAssemblerRecipes, 6, 1, 16000, 0, 1, "Assembler.png", "", false, false, 0, "ASSEMBLER", new Object[]{"ACA", "VMV", "WCW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'A', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4}).getStackForm(1L));
        GregtechItemList.Machine_EV_Component_Maker.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(988, "basicmachine.componentmaker.tier.04", "Advanced Component Maker III", 4, "Components, Unite!", Recipe_GT.Gregtech_Recipe_Map.sComponentAssemblerRecipes, 6, 1, 16000, 0, 1, "Assembler.png", "", false, false, 0, "ASSEMBLER", new Object[]{"ACA", "VMV", "WCW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'A', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4}).getStackForm(1L));
        GregtechItemList.Machine_IV_Component_Maker.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(989, "basicmachine.componentmaker.tier.05", "Advanced Component Maker IV", 5, "Components, Unite!", Recipe_GT.Gregtech_Recipe_Map.sComponentAssemblerRecipes, 6, 1, 16000, 0, 1, "Assembler.png", "", false, false, 0, "ASSEMBLER", new Object[]{"ACA", "VMV", "WCW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'A', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4}).getStackForm(1L));

        addRecipeMotor(1, Materials.Copper, 1, Materials.Tin, 1, Materials.Iron, Materials.IronMagnetic);
        addRecipeMotor(2, Materials.Copper, 2, Materials.Copper, 1, Materials.Aluminium, Materials.SteelMagnetic);
        addRecipeMotor(3, Materials.Copper, 4, Materials.Gold, 1, Materials.StainlessSteel, Materials.SteelMagnetic);
        addRecipeMotor(4, Materials.Copper, 8, Materials.Aluminium, 1, Materials.Titanium, Materials.NeodymiumMagnetic);
        addRecipeMotor(5, Materials.Copper, 16, Materials.Tungsten, 1, Materials.TungstenSteel, Materials.NeodymiumMagnetic);

        addRecipePiston(1, Materials.Steel, Materials.Tin);
        addRecipePiston(2, Materials.Aluminium, Materials.Copper);
        addRecipePiston(3, Materials.StainlessSteel, Materials.Gold);
        addRecipePiston(4, Materials.Titanium, Materials.Aluminium);
        addRecipePiston(5, Materials.TungstenSteel, Materials.Tungsten);

        addRecipeConveyor(1, Materials.Tin);
        addRecipeConveyor(2, Materials.Copper);
        addRecipeConveyor(3, Materials.Gold);
        addRecipeConveyor(4, Materials.Aluminium);
        addRecipeConveyor(5, Materials.Tungsten);
        
        
	
	
	
	
	
	
	
	
	
	}
	
	private static boolean addRecipeMotor(int tier, Materials wire, int wirethickness, Materials cable, int cablethickness, Materials stick, Materials magstick){
		String mWT = ""+wirethickness;
		String mCT = ""+cablethickness;
		
		if (wirethickness < 10){
			mWT = "0"+wirethickness;
		}
		if (cablethickness < 10){
			mCT = "0"+cablethickness;
		}
		
		OrePrefixes prefixWire = OrePrefixes.getOrePrefix("wireGt"+mWT);
		OrePrefixes prefixCable = OrePrefixes.getOrePrefix("cableGt"+mCT);
		
		ItemStack wireStack = ItemUtils.getGregtechOreStack(prefixWire, wire, 4);
		ItemStack cableStack = ItemUtils.getGregtechOreStack(prefixCable, cable, 2);
		ItemStack rodStack = ItemUtils.getGregtechOreStack(OrePrefixes.rod, stick, 2);
		ItemStack magrodStack = ItemUtils.getGregtechOreStack(OrePrefixes.rod, magstick, 1);
		
		ItemStack Input[] = {wireStack, cableStack, rodStack, magrodStack};
		
		return CORE.RA.addComponentMakerRecipe(Input, GT_Values.NF, ItemList.valueOf("Electric_Motor_"+GT_Values.VN[tier]).get(1), 100, (int) GT_Values.V[tier]);
	}
	
	private static boolean addRecipePiston(int tier, Materials mat, Materials cable){
				
		OrePrefixes prefixCable = OrePrefixes.cableGt01;	
		ItemStack cableStack = ItemUtils.getGregtechOreStack(prefixCable, cable, 2);
		ItemStack rodStack = ItemUtils.getGregtechOreStack(OrePrefixes.rod, mat, 2);
		ItemStack plateStack = ItemUtils.getGregtechOreStack(OrePrefixes.plate, mat, 3);
		ItemStack gearStack = ItemUtils.getGregtechOreStack(OrePrefixes.gearGtSmall, mat, 1);
		ItemStack motorStack = ItemList.valueOf("Electric_Motor_"+GT_Values.VN[tier]).get(1);
		
		ItemStack Input[] = {plateStack, cableStack, rodStack, gearStack, motorStack};
		
		return CORE.RA.addComponentMakerRecipe(Input, GT_Values.NF, ItemList.valueOf("Electric_Piston_"+GT_Values.VN[tier]).get(1), 100, (int) GT_Values.V[tier]);
	}
	
	private static boolean addRecipeConveyor(int tier, Materials cable){		
		OrePrefixes prefixCable = OrePrefixes.cableGt01;	
		ItemStack cableStack = ItemUtils.getGregtechOreStack(prefixCable, cable, 1);
		ItemStack motorStack = ItemList.valueOf("Electric_Motor_"+GT_Values.VN[tier]).get(2);		
		boolean mAdd[];
		final ArrayList<ItemStack> oreDictList = OreDictionary.getOres("plateAnyRubber");
		if (!oreDictList.isEmpty()){
			int mcount = 0;
			mAdd = new boolean[oreDictList.size()];
			for (ItemStack mRubberType : oreDictList){
				final ItemStack returnValue = mRubberType.copy();
				returnValue.stackSize = 6;
				ItemStack Input[] = {cableStack, motorStack, returnValue};				
				mAdd[mcount++] = CORE.RA.addComponentMakerRecipe(Input, GT_Values.NF, ItemList.valueOf("Electric_Conveyor_"+GT_Values.VN[tier]).get(1), 100, (int) GT_Values.V[tier]);
			}
			int added = 0;
			for (boolean y : mAdd){
				if (y){
					added++;
				}
			}
			if (added >= (oreDictList.size()/2)){
				return true;
			}
			else {
				return false;
			}			
		}
		else {			
			ItemStack Input[] = {cableStack, motorStack, ItemUtils.getItemStackOfAmountFromOreDictNoBroken("plateRubber", 6)};
			return CORE.RA.addComponentMakerRecipe(Input, GT_Values.NF, ItemList.valueOf("Electric_Piston_"+GT_Values.VN[tier]).get(1), 100, (int) GT_Values.V[tier]);
		}		
		
	}
	
}
