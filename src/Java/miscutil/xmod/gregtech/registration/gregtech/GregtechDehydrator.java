package miscutil.xmod.gregtech.registration.gregtech;

import gregtech.api.enums.OreDictNames;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine_GT_Recipe;
import gregtech.api.util.GT_Recipe;
import miscutil.core.lib.CORE.configSwitches;
import miscutil.core.lib.LoadedMods;
import miscutil.core.util.Utils;
import miscutil.xmod.gregtech.api.enums.GregtechItemList;
import miscutil.xmod.gregtech.common.tileentities.generators.GregtechMetaTileEntitySolarGenerator;

public class GregtechDehydrator
{
	public static void run()
	{
		if (LoadedMods.Gregtech && configSwitches.enableSolarGenerators){
			Utils.LOG_INFO("Gregtech5u Content | Registering Chemical Dehydrators.");
			run1();
		}
		
	}

	private static void run1()
	{
		
		GregtechItemList.GT_Dehydrator_EV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(813, "advancedmachine.dehydrator.tier.01", "Advanced Chemical Dehydrator I", 4, "Stay calm and keep sifting",
        		GT_Recipe.GT_Recipe_Map.sSifterRecipes, 1, 9, 0, 2, 5, "Sifter.png", "", false, false, 0, "SIFTER",
        		new Object[]{"WFW", "PMP", "CFC", Character.valueOf('M'), GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL,
        		Character.valueOf('P'), GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER,
        		Character.valueOf('F'), OreDictNames.craftingFilter,
        		Character.valueOf('C'), GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC,
        		Character.valueOf('W'), GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
		
        //GregtechItemList.GT_Dehydrator_EV.set(new GregtechMetaTileEntitySolarGenerator(813, "dehydrator.tier.01", "Extreme Voltage Chemical Dehydrator", 4).getStackForm(1L));
        GregtechItemList.GT_Dehydrator_IV.set(new GregtechMetaTileEntitySolarGenerator(814, "dehydrator.tier.02", "Insane Voltage Chemical Dehydrator", 5).getStackForm(1L));
        GregtechItemList.GT_Dehydrator_LuV.set(new GregtechMetaTileEntitySolarGenerator(815, "dehydrator.tier.03", "Ludicrous Voltage Chemical Dehydrator", 6).getStackForm(1L));
        GregtechItemList.GT_Dehydrator_ZPM.set(new GregtechMetaTileEntitySolarGenerator(816, "dehydrator.tier.04", "ZPM Voltage Chemical Dehydrator", 7).getStackForm(1L));
		
        
        
        
	}
}
