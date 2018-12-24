package gregtech.loaders.preload;

import codechicken.nei.api.API;
import cpw.mods.fml.common.Loader;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.*;
import gregtech.api.interfaces.ITexture;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.common.tileentities.automation.*;
import gregtech.common.tileentities.boilers.GT_MetaTileEntity_Boiler_Bronze;
import gregtech.common.tileentities.boilers.GT_MetaTileEntity_Boiler_Lava;
import gregtech.common.tileentities.boilers.GT_MetaTileEntity_Boiler_Solar;
import gregtech.common.tileentities.boilers.GT_MetaTileEntity_Boiler_Steel;
import gregtech.common.tileentities.generators.*;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_BasicHull_Bronze;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_BasicHull_BronzeBricks;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_BasicHull_Steel;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_BasicHull_SteelBricks;
import gregtech.common.tileentities.machines.basic.*;
import gregtech.common.tileentities.machines.multi.*;
import gregtech.common.tileentities.machines.steam.*;
import gregtech.common.tileentities.storage.GT_MetaTileEntity_Locker;
import gregtech.common.tileentities.storage.GT_MetaTileEntity_QuantumChest;
import gregtech.common.tileentities.storage.GT_MetaTileEntity_QuantumTank;
import gregtech.common.tileentities.storage.GT_MetaTileEntity_SuperChest;
import gregtech.common.tileentities.storage.GT_MetaTileEntity_SuperTank;
import ic2.core.Ic2Items;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.github.technus.tectech.thing.CustomItemList;
import com.github.technus.tectech.thing.metaTileEntity.single.GT_MetaTileEntity_WetTransformer;

//import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
//import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMetaTransformerHiAmp;

public class GT_Loader_MetaTileEntities implements Runnable {
    private static final String aTextWire1 = "wire."; private static final String aTextCable1 = "cable."; private static final String aTextWire2 = " Wire"; private static final String aTextCable2 = " Cable";
    private final static String aTextPlate = "PPP"; private final static String aTextPlateWrench = "PwP"; private final static String aTextPlateMotor = "PMP"; private final static String aTextCableHull = "CMC";
    private final static String aTextWireHull = "WMW"; private final static String aTextWireChest = "WTW"; private final static String aTextWireCoil = "WCW"; private final static String aTextMotorWire = "EWE";
    private final static String aTextWirePump = "WPW";
    private final static boolean aBoolConst_0 = false;
	private final static Boolean isNEILoaded = Loader.isModLoaded("NotEnoughItems");
	
    private static void run1() {
    	long bits = GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED;
    	long bitsd = GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED;
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_ULV.get(1L, new Object[0]), bits, new Object[]{aTextPlate, aTextPlateWrench, aTextPlate, 'P', OrePrefixes.plate.get(Materials.WroughtIron)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_LV.get(1L, new Object[0]), bits, new Object[]{aTextPlate, aTextPlateWrench, aTextPlate, 'P', OrePrefixes.plate.get(Materials.Steel)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_MV.get(1L, new Object[0]), bits, new Object[]{aTextPlate, aTextPlateWrench, aTextPlate, 'P', OrePrefixes.plate.get(Materials.Aluminium)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_HV.get(1L, new Object[0]), bits, new Object[]{aTextPlate, aTextPlateWrench, aTextPlate, 'P', OrePrefixes.plate.get(Materials.StainlessSteel)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_EV.get(1L, new Object[0]), bits, new Object[]{aTextPlate, aTextPlateWrench, aTextPlate, 'P', OrePrefixes.plate.get(Materials.Titanium)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_IV.get(1L, new Object[0]), bits, new Object[]{aTextPlate, aTextPlateWrench, aTextPlate, 'P', OrePrefixes.plate.get(Materials.TungstenSteel)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_LuV.get(1L, new Object[0]), bits, new Object[]{aTextPlate, aTextPlateWrench, aTextPlate, 'P', OrePrefixes.plate.get(Materials.Chrome)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_ZPM.get(1L, new Object[0]), bits, new Object[]{aTextPlate, aTextPlateWrench, aTextPlate, 'P', OrePrefixes.plate.get(Materials.Iridium)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_UV.get(1L, new Object[0]), bits, new Object[]{aTextPlate, aTextPlateWrench, aTextPlate, 'P', OrePrefixes.plate.get(Materials.Osmium)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_MAX.get(1L, new Object[0]), bits, new Object[]{aTextPlate, aTextPlateWrench, aTextPlate, 'P', OrePrefixes.plate.get(Materials.Tritanium)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_UEV.get(1L, new Object[0]), bits, new Object[]{aTextPlate, aTextPlateWrench, aTextPlate, 'P', OrePrefixes.plate.get(Materials.Neutronium)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_BronzePlatedBricks.get(2L, new Object[0]), bits, new Object[]{"PhP", "PBP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Bronze), 'B', new ItemStack(Blocks.brick_block, 1)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_SolidSteel.get(2L, new Object[0]), bits, new Object[]{"PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Steel), 'F', OrePrefixes.frameGt.get(Materials.Steel)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_StableTitanium.get(2L, new Object[0]), bits, new Object[]{"PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Titanium), 'F', OrePrefixes.frameGt.get(Materials.Titanium)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_HeatProof.get(2L, new Object[0]), bits, new Object[]{"PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Invar), 'F', OrePrefixes.frameGt.get(Materials.Invar)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_FrostProof.get(2L, new Object[0]), bits, new Object[]{"PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Aluminium), 'F', OrePrefixes.frameGt.get(Materials.Aluminium)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_CleanStainlessSteel.get(2L, new Object[0]), bits, new Object[]{"PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.StainlessSteel), 'F', OrePrefixes.frameGt.get(Materials.StainlessSteel)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_RobustTungstenSteel.get(2L, new Object[0]), bits, new Object[]{"PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.TungstenSteel), 'F', OrePrefixes.frameGt.get(Materials.TungstenSteel)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_RobustNaquadahAlloy.get(2L, new Object[0]), bits, new Object[]{"PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.NaquadahAlloy), 'F', OrePrefixes.frameGt.get(Materials.NaquadahAlloy)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_MiningOsmiridium.get(2L, new Object[0]), bits, new Object[]{"PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Osmiridium), 'F', OrePrefixes.frameGt.get(Materials.Osmiridium)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_Turbine.get(2L, new Object[0]), bits, new Object[]{"PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Magnalium), 'F', OrePrefixes.frameGt.get(Materials.BlueSteel)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_Turbine1.get(2L, new Object[0]), bits, new Object[]{"PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.StainlessSteel), 'F', ItemList.Casing_Turbine});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_Turbine2.get(2L, new Object[0]), bits, new Object[]{"PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Titanium), 'F', ItemList.Casing_Turbine});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_Turbine3.get(2L, new Object[0]), bits, new Object[]{"PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.TungstenSteel), 'F', ItemList.Casing_Turbine});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_Pipe_Bronze.get(2L, new Object[0]), bits, new Object[]{"PIP", "IFI", "PIP", 'P', OrePrefixes.plate.get(Materials.Bronze), 'F', OrePrefixes.frameGt.get(Materials.Bronze), 'I', OrePrefixes.pipeMedium.get(Materials.Bronze)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_Pipe_Steel.get(2L, new Object[0]), bits, new Object[]{"PIP", "IFI", "PIP", 'P', OrePrefixes.plate.get(Materials.Steel), 'F', OrePrefixes.frameGt.get(Materials.Steel), 'I', OrePrefixes.pipeMedium.get(Materials.Steel)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_Pipe_Titanium.get(2L, new Object[0]), bits, new Object[]{"PIP", "IFI", "PIP", 'P', OrePrefixes.plate.get(Materials.Titanium), 'F', OrePrefixes.frameGt.get(Materials.Titanium), 'I', OrePrefixes.pipeMedium.get(Materials.Titanium)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_Pipe_TungstenSteel.get(2L, new Object[0]), bits, new Object[]{"PIP", "IFI", "PIP", 'P', OrePrefixes.plate.get(Materials.TungstenSteel), 'F', OrePrefixes.frameGt.get(Materials.TungstenSteel), 'I', OrePrefixes.pipeMedium.get(Materials.TungstenSteel)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_Pipe_Polytetrafluoroethylene.get(2L, new Object[0]), bits, new Object[]{"PIP", "IFI", "PIP", 'P', OrePrefixes.plate.get(Materials.Polytetrafluoroethylene), 'F', OrePrefixes.frameGt.get(Materials.Polytetrafluoroethylene), 'I', OrePrefixes.pipeMedium.get(Materials.Polytetrafluoroethylene)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_Gearbox_Bronze.get(2L, new Object[0]), bits, new Object[]{"PhP", "GFG", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Bronze), 'F', OrePrefixes.frameGt.get(Materials.Bronze), 'G', OrePrefixes.gearGt.get(Materials.Bronze)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_Gearbox_Steel.get(2L, new Object[0]), bits, new Object[]{"PhP", "GFG", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Steel), 'F', OrePrefixes.frameGt.get(Materials.Steel), 'G', OrePrefixes.gearGt.get(Materials.Steel)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_Gearbox_Titanium.get(2L, new Object[0]), bits, new Object[]{"PhP", "GFG", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Steel), 'F', OrePrefixes.frameGt.get(Materials.Titanium), 'G', OrePrefixes.gearGt.get(Materials.Titanium)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_Gearbox_TungstenSteel.get(2L, new Object[0]), bits, new Object[]{"PhP", "GFG", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Steel), 'F', OrePrefixes.frameGt.get(Materials.TungstenSteel), 'G', ItemList.Robot_Arm_IV});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_Gearbox_TungstenSteel2.get(2L, new Object[0]), bits, new Object[]{"PhP", "GFG", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Steel), 'F', OrePrefixes.frameGt.get(Materials.TungstenSteel), 'G', OrePrefixes.gearGt.get(Materials.TungstenSteel)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_Grate.get(2L, new Object[0]), bits, new Object[]{"PVP", "PFP", aTextPlateMotor, 'P', new ItemStack(Blocks.iron_bars,1), 'F', OrePrefixes.frameGt.get(Materials.Steel), 'M', ItemList.Electric_Motor_MV, 'V', OrePrefixes.rotor.get(Materials.Steel)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_Vent.get(2L, new Object[0]), bits, new Object[]{"PPP", "SSS", "MFV", 'P', new ItemStack(Blocks.iron_bars,1), 'F', OrePrefixes.frameGt.get(Materials.Steel), 'M', ItemList.Electric_Motor_MV, 'V', OrePrefixes.rotor.get(Materials.Steel),'S',ItemList.Component_Filter});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_Assembler.get(2L, new Object[0]), bits, new Object[]{"PVP", "PFP", aTextPlateMotor, 'P', OrePrefixes.circuit.get(Materials.Data), 'F', OrePrefixes.frameGt.get(Materials.TungstenSteel), 'M', ItemList.Electric_Motor_IV, 'V', OrePrefixes.circuit.get(Materials.Elite)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_Firebox_Bronze.get(2L, new Object[0]), bits, new Object[]{"PSP", "SFS", "PSP", 'P', OrePrefixes.plate.get(Materials.Bronze), 'F', OrePrefixes.frameGt.get(Materials.Bronze), 'S', OrePrefixes.stick.get(Materials.Bronze)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_Firebox_Steel.get(2L, new Object[0]), bits, new Object[]{"PSP", "SFS", "PSP", 'P', OrePrefixes.plate.get(Materials.Steel), 'F', OrePrefixes.frameGt.get(Materials.Steel), 'S', OrePrefixes.stick.get(Materials.Steel)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_Firebox_Titanium.get(2L, new Object[0]), bits, new Object[]{"PSP", "SFS", "PSP", 'P', OrePrefixes.plate.get(Materials.Titanium), 'F', OrePrefixes.frameGt.get(Materials.Titanium), 'S', OrePrefixes.stick.get(Materials.Titanium)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_Firebox_TungstenSteel.get(2L, new Object[0]), bits, new Object[]{"PSP", "SFS", "PSP", 'P', OrePrefixes.plate.get(Materials.TungstenSteel), 'F', OrePrefixes.frameGt.get(Materials.TungstenSteel), 'S', OrePrefixes.stick.get(Materials.TungstenSteel)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_Stripes_A.get(1L, new Object[0]), bits, new Object[]{"Y  ", " M ", "  B", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B', Dyes.dyeBlack});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_Stripes_B.get(1L, new Object[0]), bits, new Object[]{"  Y", " M ", "B  ", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B', Dyes.dyeBlack});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_RadioactiveHazard.get(1L, new Object[0]), bits, new Object[]{" YB", " M ", "   ", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B', Dyes.dyeBlack});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_BioHazard.get(1L, new Object[0]), bits, new Object[]{" Y ", " MB", "   ", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B', Dyes.dyeBlack});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_ExplosionHazard.get(1L, new Object[0]), bits, new Object[]{" Y ", " M ", "  B", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B', Dyes.dyeBlack});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_FireHazard.get(1L, new Object[0]), bits, new Object[]{" Y ", " M ", " B ", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B', Dyes.dyeBlack});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_AcidHazard.get(1L, new Object[0]), bits, new Object[]{" Y ", " M ", "B  ", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B', Dyes.dyeBlack});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_MagicHazard.get(1L, new Object[0]), bits, new Object[]{" Y ", "BM ", "   ", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B', Dyes.dyeBlack});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_FrostHazard.get(1L, new Object[0]), bits, new Object[]{"BY ", " M ", "   ", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B', Dyes.dyeBlack});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_NoiseHazard.get(1L, new Object[0]), bits, new Object[]{"   ", " M ", "BY ", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B', Dyes.dyeBlack});

        GT_ModHandler.addShapelessCraftingRecipe(ItemList.Casing_SolidSteel.get(1L, new Object[0]), bits, new Object[]{ItemList.Casing_Stripes_A});
        GT_ModHandler.addShapelessCraftingRecipe(ItemList.Casing_SolidSteel.get(1L, new Object[0]), bits, new Object[]{ItemList.Casing_Stripes_B});
        GT_ModHandler.addShapelessCraftingRecipe(ItemList.Casing_SolidSteel.get(1L, new Object[0]), bits, new Object[]{ItemList.Casing_RadioactiveHazard});
        GT_ModHandler.addShapelessCraftingRecipe(ItemList.Casing_SolidSteel.get(1L, new Object[0]), bits, new Object[]{ItemList.Casing_BioHazard});
        GT_ModHandler.addShapelessCraftingRecipe(ItemList.Casing_SolidSteel.get(1L, new Object[0]), bits, new Object[]{ItemList.Casing_ExplosionHazard});
        GT_ModHandler.addShapelessCraftingRecipe(ItemList.Casing_SolidSteel.get(1L, new Object[0]), bits, new Object[]{ItemList.Casing_FireHazard});
        GT_ModHandler.addShapelessCraftingRecipe(ItemList.Casing_SolidSteel.get(1L, new Object[0]), bits, new Object[]{ItemList.Casing_AcidHazard});
        GT_ModHandler.addShapelessCraftingRecipe(ItemList.Casing_SolidSteel.get(1L, new Object[0]), bits, new Object[]{ItemList.Casing_MagicHazard});
        GT_ModHandler.addShapelessCraftingRecipe(ItemList.Casing_SolidSteel.get(1L, new Object[0]), bits, new Object[]{ItemList.Casing_FrostHazard});
        GT_ModHandler.addShapelessCraftingRecipe(ItemList.Casing_SolidSteel.get(1L, new Object[0]), bits, new Object[]{ItemList.Casing_NoiseHazard});

        GT_ModHandler.addShapelessCraftingRecipe(ItemList.Casing_Coil_Cupronickel.get(1L, new Object[0]), bits, new Object[] {ItemList.Casing_Coil_Cupronickel_Deprecated});
        GT_ModHandler.addShapelessCraftingRecipe(ItemList.Casing_Coil_Kanthal.get(1L, new Object[0]), bits, new Object[] {ItemList.Casing_Coil_Kanthal_Deprecated});
        GT_ModHandler.addShapelessCraftingRecipe(ItemList.Casing_Coil_Nichrome.get(1L, new Object[0]), bits, new Object[] {ItemList.Casing_Coil_Nichrome_Deprecated});

       	GT_ModHandler.addCraftingRecipe(ItemList.Casing_Firebricks.get(1L, new Object[0]), bits, new Object[] {"BB", "BB", 'B', ItemList.Firebrick.get(1, new Object[0])});
        
       	GT_ModHandler.addCraftingRecipe(ItemList.Casing_Processor.get(1L), bits, new Object[]{"PSP", "SMS", "PSP", 'P', OrePrefixes.itemCasing.get(Materials.StainlessSteel), 'S', OrePrefixes.circuit.get(Materials.Elite), 'M', ItemList.Casing_SolidSteel});
       	GT_ModHandler.addCraftingRecipe(ItemList.Casing_Pump.get(1L), bits, new Object[]{"PSP", "RMR", "PSP", 'P', OrePrefixes.itemCasing.get(Materials.StainlessSteel), 'S', OrePrefixes.circuit.get(Materials.Advanced), 'M', ItemList.Casing_SolidSteel, 'R', ItemList.Electric_Pump_HV});
       	GT_ModHandler.addCraftingRecipe(ItemList.Casing_Motor.get(1L), bits, new Object[]{"PSP", "RMR", "PSP", 'P', OrePrefixes.itemCasing.get(Materials.StainlessSteel), 'S', OrePrefixes.circuit.get(Materials.Advanced), 'M', ItemList.Casing_SolidSteel, 'R', ItemList.Electric_Motor_HV});
        
        ItemList.Hull_Bronze.set(new GT_MetaTileEntity_BasicHull_Bronze(1, "hull.bronze", "Bronze Hull", 0, "For your first Steam Machines").getStackForm(1L));
        ItemList.Hull_Bronze_Bricks.set(new GT_MetaTileEntity_BasicHull_BronzeBricks(2, "hull.bronze_bricked", "Bricked Bronze Hull", 0, "For your first Steam Machines").getStackForm(1L));
        ItemList.Hull_Steel.set(new GT_MetaTileEntity_BasicHull_Steel(3, "hull.steel", "Steel Hull", 0, "For improved Steam Machines").getStackForm(1L));
        ItemList.Hull_Steel_Bricks.set(new GT_MetaTileEntity_BasicHull_SteelBricks(4, "hull.steel_bricked", "Bricked Steel Hull", 0, "For improved Steam Machines").getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(ItemList.Hull_Bronze.get(1L, new Object[0]), bits, new Object[]{aTextPlate, "PhP", aTextPlate, 'P', OrePrefixes.plate.get(Materials.Bronze)});
        GT_ModHandler.addCraftingRecipe(ItemList.Hull_Bronze_Bricks.get(1L, new Object[0]), bits, new Object[]{aTextPlate, "PhP", "BBB", 'P', OrePrefixes.plate.get(Materials.Bronze), 'B', new ItemStack(Blocks.brick_block, 1)});
        GT_ModHandler.addCraftingRecipe(ItemList.Hull_Steel.get(1L, new Object[0]), bits, new Object[]{aTextPlate, "PhP", aTextPlate, 'P', OrePrefixes.plate.get(Materials.Steel)});
        GT_ModHandler.addCraftingRecipe(ItemList.Hull_Steel_Bricks.get(1L, new Object[0]), bits, new Object[]{aTextPlate, "PhP", "BBB", 'P', OrePrefixes.plate.get(Materials.Steel), 'B', new ItemStack(Blocks.brick_block, 1)});

        ItemList.Hull_ULV.set(new GT_MetaTileEntity_BasicHull(10, "hull.tier.00", "ULV Machine Hull", 0, EnumChatFormatting.RESET + "You just need " + EnumChatFormatting.DARK_PURPLE + "I" + EnumChatFormatting.LIGHT_PURPLE + "m" + EnumChatFormatting.DARK_RED + "a" + EnumChatFormatting.RED + "g" + EnumChatFormatting.YELLOW + "i" + EnumChatFormatting.GREEN + "n" + EnumChatFormatting.AQUA + "a" + EnumChatFormatting.DARK_AQUA + "t" + EnumChatFormatting.BLUE + "i" + EnumChatFormatting.DARK_BLUE + "o" + EnumChatFormatting.DARK_PURPLE + "n" + EnumChatFormatting.RESET + " to use this.", new ITexture[0]).getStackForm(1L));
        ItemList.Hull_LV.set(new GT_MetaTileEntity_BasicHull(11, "hull.tier.01", "LV Machine Hull", 1, EnumChatFormatting.RESET + "You just need " + EnumChatFormatting.DARK_PURPLE + "I" + EnumChatFormatting.LIGHT_PURPLE + "m" + EnumChatFormatting.DARK_RED + "a" + EnumChatFormatting.RED + "g" + EnumChatFormatting.YELLOW + "i" + EnumChatFormatting.GREEN + "n" + EnumChatFormatting.AQUA + "a" + EnumChatFormatting.DARK_AQUA + "t" + EnumChatFormatting.BLUE + "i" + EnumChatFormatting.DARK_BLUE + "o" + EnumChatFormatting.DARK_PURPLE + "n" + EnumChatFormatting.RESET + " to use this.", new ITexture[0]).getStackForm(1L));
        ItemList.Hull_MV.set(new GT_MetaTileEntity_BasicHull(12, "hull.tier.02", "MV Machine Hull", 2, EnumChatFormatting.RESET + "You just need " + EnumChatFormatting.DARK_PURPLE + "I" + EnumChatFormatting.LIGHT_PURPLE + "m" + EnumChatFormatting.DARK_RED + "a" + EnumChatFormatting.RED + "g" + EnumChatFormatting.YELLOW + "i" + EnumChatFormatting.GREEN + "n" + EnumChatFormatting.AQUA + "a" + EnumChatFormatting.DARK_AQUA + "t" + EnumChatFormatting.BLUE + "i" + EnumChatFormatting.DARK_BLUE + "o" + EnumChatFormatting.DARK_PURPLE + "n" + EnumChatFormatting.RESET + " to use this.", new ITexture[0]).getStackForm(1L));
        ItemList.Hull_HV.set(new GT_MetaTileEntity_BasicHull(13, "hull.tier.03", "HV Machine Hull", 3, EnumChatFormatting.RESET + "You just need " + EnumChatFormatting.DARK_PURPLE + "I" + EnumChatFormatting.LIGHT_PURPLE + "m" + EnumChatFormatting.DARK_RED + "a" + EnumChatFormatting.RED + "g" + EnumChatFormatting.YELLOW + "i" + EnumChatFormatting.GREEN + "n" + EnumChatFormatting.AQUA + "a" + EnumChatFormatting.DARK_AQUA + "t" + EnumChatFormatting.BLUE + "i" + EnumChatFormatting.DARK_BLUE + "o" + EnumChatFormatting.DARK_PURPLE + "n" + EnumChatFormatting.RESET + " to use this.", new ITexture[0]).getStackForm(1L));
        ItemList.Hull_EV.set(new GT_MetaTileEntity_BasicHull(14, "hull.tier.04", "EV Machine Hull", 4, EnumChatFormatting.RESET + "You just need " + EnumChatFormatting.DARK_PURPLE + "I" + EnumChatFormatting.LIGHT_PURPLE + "m" + EnumChatFormatting.DARK_RED + "a" + EnumChatFormatting.RED + "g" + EnumChatFormatting.YELLOW + "i" + EnumChatFormatting.GREEN + "n" + EnumChatFormatting.AQUA + "a" + EnumChatFormatting.DARK_AQUA + "t" + EnumChatFormatting.BLUE + "i" + EnumChatFormatting.DARK_BLUE + "o" + EnumChatFormatting.DARK_PURPLE + "n" + EnumChatFormatting.RESET + " to use this.", new ITexture[0]).getStackForm(1L));
        ItemList.Hull_IV.set(new GT_MetaTileEntity_BasicHull(15, "hull.tier.05", "IV Machine Hull", 5, EnumChatFormatting.RESET + "You just need " + EnumChatFormatting.DARK_PURPLE + "I" + EnumChatFormatting.LIGHT_PURPLE + "m" + EnumChatFormatting.DARK_RED + "a" + EnumChatFormatting.RED + "g" + EnumChatFormatting.YELLOW + "i" + EnumChatFormatting.GREEN + "n" + EnumChatFormatting.AQUA + "a" + EnumChatFormatting.DARK_AQUA + "t" + EnumChatFormatting.BLUE + "i" + EnumChatFormatting.DARK_BLUE + "o" + EnumChatFormatting.DARK_PURPLE + "n" + EnumChatFormatting.RESET + " to use this.", new ITexture[0]).getStackForm(1L));
        ItemList.Hull_LuV.set(new GT_MetaTileEntity_BasicHull(16, "hull.tier.06", "LuV Machine Hull", 6, EnumChatFormatting.RESET + "You just need " + EnumChatFormatting.DARK_PURPLE + "I" + EnumChatFormatting.LIGHT_PURPLE + "m" + EnumChatFormatting.DARK_RED + "a" + EnumChatFormatting.RED + "g" + EnumChatFormatting.YELLOW + "i" + EnumChatFormatting.GREEN + "n" + EnumChatFormatting.AQUA + "a" + EnumChatFormatting.DARK_AQUA + "t" + EnumChatFormatting.BLUE + "i" + EnumChatFormatting.DARK_BLUE + "o" + EnumChatFormatting.DARK_PURPLE + "n" + EnumChatFormatting.RESET + " to use this.", new ITexture[0]).getStackForm(1L));
        ItemList.Hull_ZPM.set(new GT_MetaTileEntity_BasicHull(17, "hull.tier.07", "ZPM Machine Hull", 7, EnumChatFormatting.RESET + "You just need " + EnumChatFormatting.DARK_PURPLE + "I" + EnumChatFormatting.LIGHT_PURPLE + "m" + EnumChatFormatting.DARK_RED + "a" + EnumChatFormatting.RED + "g" + EnumChatFormatting.YELLOW + "i" + EnumChatFormatting.GREEN + "n" + EnumChatFormatting.AQUA + "a" + EnumChatFormatting.DARK_AQUA + "t" + EnumChatFormatting.BLUE + "i" + EnumChatFormatting.DARK_BLUE + "o" + EnumChatFormatting.DARK_PURPLE + "n" + EnumChatFormatting.RESET + " to use this.", new ITexture[0]).getStackForm(1L));
        ItemList.Hull_UV.set(new GT_MetaTileEntity_BasicHull(18, "hull.tier.08", "UV Machine Hull", 8, EnumChatFormatting.RESET + "You just need " + EnumChatFormatting.DARK_PURPLE + "I" + EnumChatFormatting.LIGHT_PURPLE + "m" + EnumChatFormatting.DARK_RED + "a" + EnumChatFormatting.RED + "g" + EnumChatFormatting.YELLOW + "i" + EnumChatFormatting.GREEN + "n" + EnumChatFormatting.AQUA + "a" + EnumChatFormatting.DARK_AQUA + "t" + EnumChatFormatting.BLUE + "i" + EnumChatFormatting.DARK_BLUE + "o" + EnumChatFormatting.DARK_PURPLE + "n" + EnumChatFormatting.RESET + " to use this.", new ITexture[0]).getStackForm(1L));
        ItemList.Hull_MAX.set(new GT_MetaTileEntity_BasicHull(19, "hull.tier.09", "UHV Machine Hull", 9, EnumChatFormatting.RESET + "You just need " + EnumChatFormatting.DARK_PURPLE + "I" + EnumChatFormatting.LIGHT_PURPLE + "m" + EnumChatFormatting.DARK_RED + "a" + EnumChatFormatting.RED + "g" + EnumChatFormatting.YELLOW + "i" + EnumChatFormatting.GREEN + "n" + EnumChatFormatting.AQUA + "a" + EnumChatFormatting.DARK_AQUA + "t" + EnumChatFormatting.BLUE + "i" + EnumChatFormatting.DARK_BLUE + "o" + EnumChatFormatting.DARK_PURPLE + "n" + EnumChatFormatting.RESET + " to use this.", new ITexture[0]).getStackForm(1L));
        ItemList.Hull_UEV.set(new GT_MetaTileEntity_BasicHull(11230, "hull.tier.10", "UEV Machine Hull", 10, EnumChatFormatting.RESET + "You just need " + EnumChatFormatting.DARK_PURPLE + "I" + EnumChatFormatting.LIGHT_PURPLE + "m" + EnumChatFormatting.DARK_RED + "a" + EnumChatFormatting.RED + "g" + EnumChatFormatting.YELLOW + "i" + EnumChatFormatting.GREEN + "n" + EnumChatFormatting.AQUA + "a" + EnumChatFormatting.DARK_AQUA + "t" + EnumChatFormatting.BLUE + "i" + EnumChatFormatting.DARK_BLUE + "o" + EnumChatFormatting.DARK_PURPLE + "n" + EnumChatFormatting.RESET + " to use this.", new ITexture[0]).getStackForm(1L));
        ItemList.Hull_UIV.set(new GT_MetaTileEntity_BasicHull(11231, "hull.tier.11", "UIV Machine Hull", 11, EnumChatFormatting.RESET + "You just need " + EnumChatFormatting.DARK_PURPLE + "I" + EnumChatFormatting.LIGHT_PURPLE + "m" + EnumChatFormatting.DARK_RED + "a" + EnumChatFormatting.RED + "g" + EnumChatFormatting.YELLOW + "i" + EnumChatFormatting.GREEN + "n" + EnumChatFormatting.AQUA + "a" + EnumChatFormatting.DARK_AQUA + "t" + EnumChatFormatting.BLUE + "i" + EnumChatFormatting.DARK_BLUE + "o" + EnumChatFormatting.DARK_PURPLE + "n" + EnumChatFormatting.RESET + " to use this.", new ITexture[0]).getStackForm(1L));
        ItemList.Hull_UMV.set(new GT_MetaTileEntity_BasicHull(11232, "hull.tier.12", "UMV Machine Hull", 12, EnumChatFormatting.RESET + "You just need " + EnumChatFormatting.DARK_PURPLE + "I" + EnumChatFormatting.LIGHT_PURPLE + "m" + EnumChatFormatting.DARK_RED + "a" + EnumChatFormatting.RED + "g" + EnumChatFormatting.YELLOW + "i" + EnumChatFormatting.GREEN + "n" + EnumChatFormatting.AQUA + "a" + EnumChatFormatting.DARK_AQUA + "t" + EnumChatFormatting.BLUE + "i" + EnumChatFormatting.DARK_BLUE + "o" + EnumChatFormatting.DARK_PURPLE + "n" + EnumChatFormatting.RESET + " to use this.", new ITexture[0]).getStackForm(1L));
        ItemList.Hull_UXV.set(new GT_MetaTileEntity_BasicHull(11233, "hull.tier.13", "UXV Machine Hull", 13, EnumChatFormatting.RESET + "You just need " + EnumChatFormatting.DARK_PURPLE + "I" + EnumChatFormatting.LIGHT_PURPLE + "m" + EnumChatFormatting.DARK_RED + "a" + EnumChatFormatting.RED + "g" + EnumChatFormatting.YELLOW + "i" + EnumChatFormatting.GREEN + "n" + EnumChatFormatting.AQUA + "a" + EnumChatFormatting.DARK_AQUA + "t" + EnumChatFormatting.BLUE + "i" + EnumChatFormatting.DARK_BLUE + "o" + EnumChatFormatting.DARK_PURPLE + "n" + EnumChatFormatting.RESET + " to use this.", new ITexture[0]).getStackForm(1L));
        ItemList.Hull_OPV.set(new GT_MetaTileEntity_BasicHull(11234, "hull.tier.14", "OPV Machine Hull", 14, EnumChatFormatting.RESET + "You just need " + EnumChatFormatting.DARK_PURPLE + "I" + EnumChatFormatting.LIGHT_PURPLE + "m" + EnumChatFormatting.DARK_RED + "a" + EnumChatFormatting.RED + "g" + EnumChatFormatting.YELLOW + "i" + EnumChatFormatting.GREEN + "n" + EnumChatFormatting.AQUA + "a" + EnumChatFormatting.DARK_AQUA + "t" + EnumChatFormatting.BLUE + "i" + EnumChatFormatting.DARK_BLUE + "o" + EnumChatFormatting.DARK_PURPLE + "n" + EnumChatFormatting.RESET + " to use this.", new ITexture[0]).getStackForm(1L));
        ItemList.Hull_MAXV.set(new GT_MetaTileEntity_BasicHull(11235, "hull.tier.15", "MAXV Machine Hull", 15, EnumChatFormatting.RESET + "You just need " + EnumChatFormatting.DARK_PURPLE + "I" + EnumChatFormatting.LIGHT_PURPLE + "m" + EnumChatFormatting.DARK_RED + "a" + EnumChatFormatting.RED + "g" + EnumChatFormatting.YELLOW + "i" + EnumChatFormatting.GREEN + "n" + EnumChatFormatting.AQUA + "a" + EnumChatFormatting.DARK_AQUA + "t" + EnumChatFormatting.BLUE + "i" + EnumChatFormatting.DARK_BLUE + "o" + EnumChatFormatting.DARK_PURPLE + "n" + EnumChatFormatting.RESET + " to use this.", new ITexture[0]).getStackForm(1L));
        
        GT_ModHandler.addCraftingRecipe(ItemList.Hull_ULV.get(1L, new Object[0]), GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{aTextCableHull, 'M', ItemList.Casing_ULV, 'C', OrePrefixes.cableGt01.get(Materials.Lead), 'H', OrePrefixes.plate.get(Materials.WroughtIron), 'P', OrePrefixes.plate.get(Materials.Wood)});
        GT_ModHandler.addCraftingRecipe(ItemList.Hull_ULV.get(1L, new Object[0]), GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{aTextCableHull, 'M', ItemList.Casing_ULV, 'C', OrePrefixes.cableGt01.get(Materials.Lead), 'H', OrePrefixes.plate.get(Materials.WroughtIron), 'P', OrePrefixes.plate.get(Materials.Plastic)});
        GT_ModHandler.addCraftingRecipe(ItemList.Hull_LV.get(1L, new Object[0]),  GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{aTextCableHull, 'M', ItemList.Casing_LV, 'C', OrePrefixes.cableGt01.get(Materials.Tin), 'H', OrePrefixes.plate.get(Materials.Steel), 'P', OrePrefixes.plate.get(Materials.WroughtIron)});
        GT_ModHandler.addCraftingRecipe(ItemList.Hull_LV.get(1L, new Object[0]),  GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{aTextCableHull, 'M', ItemList.Casing_LV, 'C', OrePrefixes.cableGt01.get(Materials.Tin), 'H', OrePrefixes.plate.get(Materials.Steel), 'P', OrePrefixes.plate.get(Materials.Plastic)});
        GT_ModHandler.addCraftingRecipe(ItemList.Hull_MV.get(1L, new Object[0]),  GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{aTextCableHull, 'M', ItemList.Casing_MV, 'C', OrePrefixes.cableGt01.get(Materials.Copper), 'H', OrePrefixes.plate.get(Materials.Aluminium), 'P', OrePrefixes.plate.get(Materials.WroughtIron)});
        GT_ModHandler.addCraftingRecipe(ItemList.Hull_MV.get(1L, new Object[0]),  GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{aTextCableHull, 'M', ItemList.Casing_MV, 'C', OrePrefixes.cableGt01.get(Materials.Copper), 'H', OrePrefixes.plate.get(Materials.Aluminium), 'P', OrePrefixes.plate.get(Materials.Plastic)});
        GT_ModHandler.addCraftingRecipe(ItemList.Hull_HV.get(1L, new Object[0]),  GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{aTextCableHull, 'M', ItemList.Casing_HV, 'C', OrePrefixes.cableGt01.get(Materials.Gold), 'H', OrePrefixes.plate.get(Materials.StainlessSteel), 'P', OrePrefixes.plate.get(Materials.Plastic)});
        GT_ModHandler.addCraftingRecipe(ItemList.Hull_EV.get(1L, new Object[0]),  GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{aTextCableHull, 'M', ItemList.Casing_EV, 'C', OrePrefixes.cableGt01.get(Materials.Aluminium), 'H', OrePrefixes.plate.get(Materials.Titanium), 'P', OrePrefixes.plate.get(Materials.Plastic)});
        GT_ModHandler.addCraftingRecipe(ItemList.Hull_IV.get(1L, new Object[0]),  GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{aTextCableHull, 'M', ItemList.Casing_IV, 'C', OrePrefixes.cableGt01.get(Materials.Tungsten), 'H', OrePrefixes.plate.get(Materials.TungstenSteel), 'P', OrePrefixes.plate.get(Materials.Polytetrafluoroethylene)});
        GT_ModHandler.addCraftingRecipe(ItemList.Hull_LuV.get(1L, new Object[0]), GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{aTextCableHull, 'M', ItemList.Casing_LuV, 'C', OrePrefixes.cableGt01.get(Materials.VanadiumGallium), 'H', OrePrefixes.plate.get(Materials.Chrome), 'P', OrePrefixes.plate.get(Materials.Polytetrafluoroethylene)});
        GT_ModHandler.addCraftingRecipe(ItemList.Hull_ZPM.get(1L, new Object[0]), GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{aTextCableHull, 'M', ItemList.Casing_ZPM, 'C', OrePrefixes.cableGt01.get(Materials.Naquadah), 'H', OrePrefixes.plate.get(Materials.Iridium), 'P', OrePrefixes.plate.get(Materials.Polybenzimidazole)});
        GT_ModHandler.addCraftingRecipe(ItemList.Hull_UV.get(1L, new Object[0]),  GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{aTextCableHull, 'M', ItemList.Casing_UV, 'C', OrePrefixes.cableGt04.get(Materials.NaquadahAlloy), 'H', OrePrefixes.plate.get(Materials.Osmium), 'P', OrePrefixes.plate.get(Materials.Polybenzimidazole)});
        GT_ModHandler.addCraftingRecipe(ItemList.Hull_MAX.get(1L, new Object[0]), GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{aTextCableHull, 'M', ItemList.Casing_MAX, 'C', OrePrefixes.wireGt04.get(Materials.Europium), 'H', OrePrefixes.plate.get(Materials.Tritanium), 'P', OrePrefixes.plateDouble.get(Materials.Polybenzimidazole)});
        GT_ModHandler.addCraftingRecipe(ItemList.Hull_UEV.get(1L, new Object[0]), GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{aTextCableHull, 'M', ItemList.Casing_UEV, 'C', OrePrefixes.wireGt04.get(Materials.Diamericiumtitanium), 'H', OrePrefixes.plate.get(Materials.Neutronium), 'P', OrePrefixes.plateDouble.get(Materials.Polybenzimidazole)});
        
        GT_ModHandler.removeRecipeByOutput(ItemList.Hull_ULV.get(1L, new Object[0]));
        GT_ModHandler.removeRecipeByOutput(ItemList.Hull_LV.get(1L, new Object[0]));
        GT_ModHandler.removeRecipeByOutput(ItemList.Hull_MV.get(1L, new Object[0]));
        GT_ModHandler.removeRecipeByOutput(ItemList.Hull_HV.get(1L, new Object[0]));
        GT_ModHandler.removeRecipeByOutput(ItemList.Hull_EV.get(1L, new Object[0]));
        GT_ModHandler.removeRecipeByOutput(ItemList.Hull_IV.get(1L, new Object[0]));
        GT_ModHandler.removeRecipeByOutput(ItemList.Hull_LuV.get(1L, new Object[0]));
        GT_ModHandler.removeRecipeByOutput(ItemList.Hull_ZPM.get(1L, new Object[0]));
        GT_ModHandler.removeRecipeByOutput(ItemList.Hull_UV.get(1L, new Object[0]));
        GT_ModHandler.removeRecipeByOutput(ItemList.Hull_MAX.get(1L, new Object[0]));
        GT_ModHandler.removeRecipeByOutput(ItemList.Hull_UEV.get(1L, new Object[0]));
        
        if (GT_Mod.gregtechproxy.mHardMachineCasings) {
            GT_ModHandler.addCraftingRecipe(ItemList.Hull_ULV.get(1L, new Object[0]), GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"PHP", aTextCableHull, 'M', ItemList.Casing_ULV, 'C', OrePrefixes.cableGt01.get(Materials.Lead), 'H', OrePrefixes.plate.get(Materials.WroughtIron), 'P', OrePrefixes.plate.get(Materials.Wood)});
            GT_ModHandler.addCraftingRecipe(ItemList.Hull_LV.get(1L, new Object[0]), GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"PHP", aTextCableHull, 'M', ItemList.Casing_LV, 'C', OrePrefixes.cableGt01.get(Materials.Tin), 'H', OrePrefixes.plate.get(Materials.Steel), 'P', OrePrefixes.plate.get(Materials.WroughtIron)});
            GT_ModHandler.addCraftingRecipe(ItemList.Hull_MV.get(1L, new Object[0]), GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"PHP", aTextCableHull, 'M', ItemList.Casing_MV, 'C', OrePrefixes.cableGt01.get(Materials.Copper), 'H', OrePrefixes.plate.get(Materials.Aluminium), 'P', OrePrefixes.plate.get(Materials.WroughtIron)});
            GT_ModHandler.addCraftingRecipe(ItemList.Hull_HV.get(1L, new Object[0]), GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"PHP", aTextCableHull, 'M', ItemList.Casing_HV, 'C', OrePrefixes.cableGt01.get(Materials.Gold), 'H', OrePrefixes.plate.get(Materials.StainlessSteel), 'P', OrePrefixes.plate.get(Materials.Plastic)});
            GT_ModHandler.addCraftingRecipe(ItemList.Hull_EV.get(1L, new Object[0]), GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"PHP", aTextCableHull, 'M', ItemList.Casing_EV, 'C', OrePrefixes.cableGt01.get(Materials.Aluminium), 'H', OrePrefixes.plate.get(Materials.Titanium), 'P', OrePrefixes.plate.get(Materials.Plastic)});
            GT_ModHandler.addCraftingRecipe(ItemList.Hull_IV.get(1L, new Object[0]), GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"PHP", aTextCableHull, 'M', ItemList.Casing_IV, 'C', OrePrefixes.cableGt01.get(Materials.Tungsten), 'H', OrePrefixes.plate.get(Materials.TungstenSteel), 'P', OrePrefixes.plate.get(Materials.Polytetrafluoroethylene)});
            GT_ModHandler.addCraftingRecipe(ItemList.Hull_LuV.get(1L, new Object[0]), GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"PHP", aTextCableHull, 'M', ItemList.Casing_LuV, 'C', OrePrefixes.cableGt01.get(Materials.VanadiumGallium), 'H', OrePrefixes.plate.get(Materials.Chrome), 'P', OrePrefixes.plate.get(Materials.Polytetrafluoroethylene)});
            GT_ModHandler.addCraftingRecipe(ItemList.Hull_ZPM.get(1L, new Object[0]), GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"PHP", aTextCableHull, 'M', ItemList.Casing_ZPM, 'C', OrePrefixes.cableGt01.get(Materials.Naquadah), 'H', OrePrefixes.plate.get(Materials.Iridium), 'P', OrePrefixes.plate.get(Materials.Polybenzimidazole)});
            GT_ModHandler.addCraftingRecipe(ItemList.Hull_UV.get(1L, new Object[0]), GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"PHP", aTextCableHull, 'M', ItemList.Casing_UV, 'C', OrePrefixes.cableGt04.get(Materials.NaquadahAlloy), 'H', OrePrefixes.plate.get(Materials.Osmium), 'P', OrePrefixes.plate.get(Materials.Polybenzimidazole)});
            GT_ModHandler.addCraftingRecipe(ItemList.Hull_MAX.get(1L, new Object[0]), GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"PHP", aTextCableHull, 'M', ItemList.Casing_MAX, 'C', OrePrefixes.wireGt04.get(Materials.Europium), 'H', OrePrefixes.plate.get(Materials.Tritanium), 'P', OrePrefixes.plateDouble.get(Materials.Polybenzimidazole)});
            GT_ModHandler.addCraftingRecipe(ItemList.Hull_UEV.get(1L, new Object[0]), GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"PHP", aTextCableHull, 'M', ItemList.Casing_UEV, 'C', OrePrefixes.wireGt04.get(Materials.Diamericiumtitanium), 'H', OrePrefixes.plate.get(Materials.Neutronium), 'P', OrePrefixes.plateDouble.get(Materials.Polybenzimidazole)});
            
        } else {
            GT_ModHandler.addCraftingRecipe(ItemList.Hull_ULV.get(1L, new Object[0]), GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{aTextCableHull, 'M', ItemList.Casing_ULV, 'C', OrePrefixes.cableGt01.get(Materials.Lead)});
            GT_ModHandler.addCraftingRecipe(ItemList.Hull_LV.get(1L, new Object[0]), GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{aTextCableHull, 'M', ItemList.Casing_LV, 'C', OrePrefixes.cableGt01.get(Materials.Tin)});
            GT_ModHandler.addCraftingRecipe(ItemList.Hull_MV.get(1L, new Object[0]), GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{aTextCableHull, 'M', ItemList.Casing_MV, 'C', OrePrefixes.cableGt01.get(Materials.Copper)});
            GT_ModHandler.addCraftingRecipe(ItemList.Hull_HV.get(1L, new Object[0]), GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{aTextCableHull, 'M', ItemList.Casing_HV, 'C', OrePrefixes.cableGt01.get(Materials.Gold)});
            GT_ModHandler.addCraftingRecipe(ItemList.Hull_EV.get(1L, new Object[0]), GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{aTextCableHull, 'M', ItemList.Casing_EV, 'C', OrePrefixes.cableGt01.get(Materials.Aluminium)});
            GT_ModHandler.addCraftingRecipe(ItemList.Hull_IV.get(1L, new Object[0]), GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{aTextCableHull, 'M', ItemList.Casing_IV, 'C', OrePrefixes.cableGt01.get(Materials.Tungsten)});
            GT_ModHandler.addCraftingRecipe(ItemList.Hull_LuV.get(1L, new Object[0]), GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{aTextCableHull, 'M', ItemList.Casing_LuV, 'C', OrePrefixes.cableGt01.get(Materials.VanadiumGallium)});
            GT_ModHandler.addCraftingRecipe(ItemList.Hull_ZPM.get(1L, new Object[0]), GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{aTextCableHull, 'M', ItemList.Casing_ZPM, 'C', OrePrefixes.cableGt01.get(Materials.Naquadah)});
            GT_ModHandler.addCraftingRecipe(ItemList.Hull_UV.get(1L, new Object[0]), GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{aTextCableHull, 'M', ItemList.Casing_UV, 'C', OrePrefixes.cableGt04.get(Materials.NaquadahAlloy)});
            GT_ModHandler.addCraftingRecipe(ItemList.Hull_MAX.get(1L, new Object[0]), GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{aTextCableHull, 'M', ItemList.Casing_MAX, 'C', OrePrefixes.wireGt04.get(Materials.Europium)});
            GT_ModHandler.addCraftingRecipe(ItemList.Hull_UEV.get(1L, new Object[0]), GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{aTextCableHull, 'M', ItemList.Casing_UEV, 'C', OrePrefixes.wireGt04.get(Materials.Diamericiumtitanium)});
            
        }
        ItemList.Transformer_LV_ULV.set(new GT_MetaTileEntity_Transformer(20, "transformer.tier.00", "Ultra Low Voltage Transformer", 0, "LV -> ULV (Use Soft Hammer to invert)").getStackForm(1L));
        ItemList.Transformer_MV_LV.set(new GT_MetaTileEntity_Transformer(21, "transformer.tier.01", "Low Voltage Transformer", 1, "MV -> LV (Use Soft Hammer to invert)").getStackForm(1L));
        ItemList.Transformer_HV_MV.set(new GT_MetaTileEntity_Transformer(22, "transformer.tier.02", "Medium Voltage Transformer", 2, "HV -> MV (Use Soft Hammer to invert)").getStackForm(1L));
        ItemList.Transformer_EV_HV.set(new GT_MetaTileEntity_Transformer(23, "transformer.tier.03", "High Voltage Transformer", 3, "EV -> HV (Use Soft Hammer to invert)").getStackForm(1L));
        ItemList.Transformer_IV_EV.set(new GT_MetaTileEntity_Transformer(24, "transformer.tier.04", "Extreme Transformer", 4, "IV -> EV (Use Soft Hammer to invert)").getStackForm(1L));
        ItemList.Transformer_LuV_IV.set(new GT_MetaTileEntity_Transformer(25, "transformer.tier.05", "Insane Transformer", 5, "LuV -> IV (Use Soft Hammer to invert)").getStackForm(1L));
        ItemList.Transformer_ZPM_LuV.set(new GT_MetaTileEntity_Transformer(26, "transformer.tier.06", "Ludicrous Transformer", 6, "ZPM -> LuV (Use Soft Hammer to invert)").getStackForm(1L));
        ItemList.Transformer_UV_ZPM.set(new GT_MetaTileEntity_Transformer(27, "transformer.tier.07", "ZPM Voltage Transformer", 7, "UV -> ZPM (Use Soft Hammer to invert)").getStackForm(1L));
        ItemList.Transformer_MAX_UV.set(new GT_MetaTileEntity_Transformer(28, "transformer.tier.08", "Ultimate Transformer", 8, "UHV -> UV (Use Soft Hammer to invert)").getStackForm(1L));
        ItemList.Transformer_UEV_UHV.set(new GT_MetaTileEntity_Transformer(11220, "transformer.tier.09", "Highly Ultimate Transformer", 9,"UEV -> UHV (Use Soft Mallet to invert)").getStackForm(1L));
		ItemList.Transformer_UIV_UEV.set(new GT_MetaTileEntity_Transformer(11221, "transformer.tier.10", "Extremely Ultimate Transformer",10,"UIV -> UEV (Use Soft Mallet to invert)").getStackForm(1L));
		ItemList.Transformer_UMV_UIV.set(new GT_MetaTileEntity_Transformer(11222, "transformer.tier.11", "Insanely Ultimate Transformer",11,"UMV -> UIV (Use Soft Mallet to invert)").getStackForm(1L));
		ItemList.Transformer_UXV_UMV.set(new GT_MetaTileEntity_Transformer(11223, "transformer.tier.12", "Mega Ultimate Transformer",12,"UXV -> UMV (Use Soft Mallet to invert)").getStackForm(1L));
		ItemList.Transformer_OPV_UXV.set(new GT_MetaTileEntity_Transformer(11224, "transformer.tier.13", "Extended Mega Ultimate Transformer",13,"OPV -> UXV (Use Soft Mallet to invert)").getStackForm(1L));
		ItemList.Transformer_MAXV_OPV.set(new GT_MetaTileEntity_Transformer(11225, "transformer.tier.14", "Overpowered Transformer",14,"MAX -> OPV (Use Soft Mallet to invert)").getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(ItemList.Transformer_LV_ULV.get(1L, new Object[0]), bitsd, new Object[]{" BB", "CM ", " BB", 'M', ItemList.Hull_ULV, 'C', OrePrefixes.cableGt01.get(Materials.Tin), 'B', OrePrefixes.cableGt01.get(Materials.Lead)});
        GT_ModHandler.addCraftingRecipe(ItemList.Transformer_MV_LV.get(1L, new Object[0]), bitsd, new Object[]{" BB", "CM ", " BB", 'M', ItemList.Hull_LV, 'C', OrePrefixes.cableGt01.get(Materials.Copper), 'B', OrePrefixes.cableGt01.get(Materials.Tin)});
        GT_ModHandler.addCraftingRecipe(ItemList.Transformer_HV_MV.get(1L, new Object[0]), bitsd, new Object[]{"KBB", "CM ", "KBB", 'M', ItemList.Hull_MV, 'C', OrePrefixes.cableGt01.get(Materials.Gold), 'B', OrePrefixes.cableGt01.get(Materials.Copper),'K',ItemList.Circuit_Parts_Coil});
        GT_ModHandler.addCraftingRecipe(ItemList.Transformer_EV_HV.get(1L, new Object[0]), bitsd, new Object[]{"KBB", "CM ", "KBB", 'M', ItemList.Hull_HV, 'C', OrePrefixes.cableGt01.get(Materials.Aluminium), 'B', OrePrefixes.cableGt01.get(Materials.Gold),'K',ItemList.Circuit_Chip_ULPIC});
        GT_ModHandler.addCraftingRecipe(ItemList.Transformer_IV_EV.get(1L, new Object[0]), bitsd, new Object[]{"KBB", "CM ", "KBB", 'M', ItemList.Hull_EV, 'C', OrePrefixes.cableGt01.get(Materials.Tungsten), 'B', OrePrefixes.cableGt01.get(Materials.Aluminium),'K',ItemList.Circuit_Chip_LPIC});
        GT_ModHandler.addCraftingRecipe(ItemList.Transformer_LuV_IV.get(1L, new Object[0]), bitsd, new Object[]{"KBB", "CM ", "KBB", 'M', ItemList.Hull_IV, 'C', OrePrefixes.cableGt01.get(Materials.VanadiumGallium), 'B', OrePrefixes.cableGt01.get(Materials.Tungsten),'K',ItemList.Circuit_Chip_PIC});
        GT_ModHandler.addCraftingRecipe(ItemList.Transformer_ZPM_LuV.get(1L, new Object[0]), bitsd, new Object[]{"KBB", "CM ", "KBB", 'M', ItemList.Hull_LuV, 'C', OrePrefixes.cableGt01.get(Materials.Naquadah), 'B', OrePrefixes.cableGt01.get(Materials.VanadiumGallium),'K',ItemList.Circuit_Chip_HPIC});
        GT_ModHandler.addCraftingRecipe(ItemList.Transformer_UV_ZPM.get(1L, new Object[0]), bitsd, new Object[]{"KBB", "CM ", "KBB", 'M', ItemList.Hull_ZPM, 'C', OrePrefixes.cableGt04.get(Materials.NaquadahAlloy), 'B', OrePrefixes.cableGt02.get(Materials.Naquadah),'K',ItemList.Circuit_Chip_UHPIC});
        GT_ModHandler.addCraftingRecipe(ItemList.Transformer_MAX_UV.get(1L, new Object[0]), bitsd, new Object[]{"KBB", "CM ", "KBB", 'M', ItemList.Hull_UV, 'C', OrePrefixes.wireGt04.get(Materials.Europium), 'B', OrePrefixes.cableGt04.get(Materials.NaquadahAlloy),'K',ItemList.Circuit_Chip_NPIC});
        GT_ModHandler.addCraftingRecipe(ItemList.Transformer_UEV_UHV.get(1L, new Object[0]), bitsd, new Object[]{"KBB", "CM ", "KBB", 'M', ItemList.Hull_MAX, 'C', OrePrefixes.wireGt04.get(Materials.Diamericiumtitanium), 'B', OrePrefixes.wireGt04.get(Materials.Europium),'K',ItemList.Circuit_Chip_PPIC});
        GT_ModHandler.addCraftingRecipe(ItemList.Transformer_UIV_UEV.get(1L, new Object[0]), bitsd, new Object[]{"KBB", "CM ", "KBB", 'M', ItemList.Hull_UEV, 'C', OrePrefixes.wireGt04.get(Materials.Neutronium), 'B', OrePrefixes.wireGt04.get(Materials.Diamericiumtitanium),'K',ItemList.Circuit_Chip_QPIC});
        GT_ModHandler.addCraftingRecipe(ItemList.Transformer_UMV_UIV.get(1L, new Object[0]), bitsd, new Object[]{"KBB", "CM ", "KBB", 'M', ItemList.Hull_UIV, 'C', OrePrefixes.wireGt08.get(Materials.Quantium), 'B', OrePrefixes.wireGt08.get(Materials.Neutronium),'K',ItemList.Circuit_Chip_QPIC});
        
        ItemList.Hatch_Dynamo_ULV.set(new GT_MetaTileEntity_Hatch_Dynamo(30, "hatch.dynamo.tier.00", "ULV Dynamo Hatch", 0).getStackForm(1L));
        ItemList.Hatch_Dynamo_LV.set(new GT_MetaTileEntity_Hatch_Dynamo(31, "hatch.dynamo.tier.01", "LV Dynamo Hatch", 1).getStackForm(1L));
        ItemList.Hatch_Dynamo_MV.set(new GT_MetaTileEntity_Hatch_Dynamo(32, "hatch.dynamo.tier.02", "MV Dynamo Hatch", 2).getStackForm(1L));
        ItemList.Hatch_Dynamo_HV.set(new GT_MetaTileEntity_Hatch_Dynamo(33, "hatch.dynamo.tier.03", "HV Dynamo Hatch", 3).getStackForm(1L));
        ItemList.Hatch_Dynamo_EV.set(new GT_MetaTileEntity_Hatch_Dynamo(34, "hatch.dynamo.tier.04", "EV Dynamo Hatch", 4).getStackForm(1L));
        ItemList.Hatch_Dynamo_IV.set(new GT_MetaTileEntity_Hatch_Dynamo(35, "hatch.dynamo.tier.05", "IV Dynamo Hatch", 5).getStackForm(1L));
        ItemList.Hatch_Dynamo_LuV.set(new GT_MetaTileEntity_Hatch_Dynamo(36, "hatch.dynamo.tier.06", "LuV Dynamo Hatch", 6).getStackForm(1L));
        ItemList.Hatch_Dynamo_ZPM.set(new GT_MetaTileEntity_Hatch_Dynamo(37, "hatch.dynamo.tier.07", "ZPM Dynamo Hatch", 7).getStackForm(1L));
        ItemList.Hatch_Dynamo_UV.set(new GT_MetaTileEntity_Hatch_Dynamo(38, "hatch.dynamo.tier.08", "UV Dynamo Hatch", 8).getStackForm(1L));
        ItemList.Hatch_Dynamo_MAX.set(new GT_MetaTileEntity_Hatch_Dynamo(39, "hatch.dynamo.tier.09", "UHV Dynamo Hatch", 9).getStackForm(1L));
        ItemList.Hatch_Dynamo_UEV.set(new GT_MetaTileEntity_Hatch_Dynamo(11290, "hatch.dynamo.tier.10", "UEV Dynamo Hatch",10).getStackForm(1L));
        ItemList.Hatch_Dynamo_UIV.set(new GT_MetaTileEntity_Hatch_Dynamo(11291, "hatch.dynamo.tier.11", "UIV Dynamo Hatch",11).getStackForm(1L));
		ItemList.Hatch_Dynamo_UMV.set(new GT_MetaTileEntity_Hatch_Dynamo(11292, "hatch.dynamo.tier.12", "UMV Dynamo Hatch",12).getStackForm(1L));
		ItemList.Hatch_Dynamo_UXV.set(new GT_MetaTileEntity_Hatch_Dynamo(11293, "hatch.dynamo.tier.13", "UXV Dynamo Hatch",13).getStackForm(1L));
		ItemList.Hatch_Dynamo_OPV.set(new GT_MetaTileEntity_Hatch_Dynamo(11294, "hatch.dynamo.tier.14", "OPV Dynamo Hatch",14).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(ItemList.Hatch_Dynamo_ULV.get(1L, new Object[0]), bitsd, new Object[]{"XOL", "CMP", "XOL", 'M', ItemList.Hull_ULV, 'C', OrePrefixes.spring.get(Materials.Lead), 'X', OrePrefixes.circuit.get(Materials.Primitive), 'O', ItemList.ULV_Coil, 'L', OrePrefixes.cell.get(Materials.Lubricant), 'P', OrePrefixes.rotor.get(Materials.Lead)});
        GT_ModHandler.addCraftingRecipe(ItemList.Hatch_Dynamo_LV.get(1L, new Object[0]), bitsd, new Object[]{"XOL", "CMP", "XOL", 'M', ItemList.Hull_LV, 'C', OrePrefixes.spring.get(Materials.Tin), 'X', OrePrefixes.circuit.get(Materials.Basic), 'O', ItemList.LV_Coil, 'L', OrePrefixes.cell.get(Materials.Lubricant), 'P', ItemList.Electric_Pump_LV});
        GT_ModHandler.addCraftingRecipe(ItemList.Hatch_Dynamo_MV.get(1L, new Object[0]), bitsd, new Object[]{"XOL", "CMP", "XOL", 'M', ItemList.Hull_MV, 'C', OrePrefixes.spring.get(Materials.Copper), 'X', OrePrefixes.circuit.get(Materials.Good), 'O', ItemList.MV_Coil, 'L', OrePrefixes.cell.get(Materials.Lubricant), 'P', ItemList.Electric_Pump_MV});

        ItemList.Hatch_Energy_ULV.set(new GT_MetaTileEntity_Hatch_Energy(40, "hatch.energy.tier.00", "ULV Energy Hatch", 0).getStackForm(1L));
        ItemList.Hatch_Energy_LV.set(new GT_MetaTileEntity_Hatch_Energy(41, "hatch.energy.tier.01", "LV Energy Hatch", 1).getStackForm(1L));
        ItemList.Hatch_Energy_MV.set(new GT_MetaTileEntity_Hatch_Energy(42, "hatch.energy.tier.02", "MV Energy Hatch", 2).getStackForm(1L));
        ItemList.Hatch_Energy_HV.set(new GT_MetaTileEntity_Hatch_Energy(43, "hatch.energy.tier.03", "HV Energy Hatch", 3).getStackForm(1L));
        ItemList.Hatch_Energy_EV.set(new GT_MetaTileEntity_Hatch_Energy(44, "hatch.energy.tier.04", "EV Energy Hatch", 4).getStackForm(1L));
        ItemList.Hatch_Energy_IV.set(new GT_MetaTileEntity_Hatch_Energy(45, "hatch.energy.tier.05", "IV Energy Hatch", 5).getStackForm(1L));
        ItemList.Hatch_Energy_LuV.set(new GT_MetaTileEntity_Hatch_Energy(46, "hatch.energy.tier.06", "LuV Energy Hatch", 6).getStackForm(1L));
        ItemList.Hatch_Energy_ZPM.set(new GT_MetaTileEntity_Hatch_Energy(47, "hatch.energy.tier.07", "ZPM Energy Hatch", 7).getStackForm(1L));
        ItemList.Hatch_Energy_UV.set(new GT_MetaTileEntity_Hatch_Energy(48, "hatch.energy.tier.08", "UV Energy Hatch", 8).getStackForm(1L));
        ItemList.Hatch_Energy_MAX.set(new GT_MetaTileEntity_Hatch_Energy(49, "hatch.energy.tier.09", "UHV Energy Hatch", 9).getStackForm(1L));
        ItemList.Hatch_Energy_UEV.set(new GT_MetaTileEntity_Hatch_Energy(11300, "hatch.energy.tier.10", "UEV Energy Hatch",10).getStackForm(1L));
        ItemList.Hatch_Energy_UIV.set(new GT_MetaTileEntity_Hatch_Energy(11301, "hatch.energy.tier.11", "UIV Energy Hatch",11).getStackForm(1L));
		ItemList.Hatch_Energy_UMV.set(new GT_MetaTileEntity_Hatch_Energy(11302, "hatch.energy.tier.12", "UMV Energy Hatch",12).getStackForm(1L));
		ItemList.Hatch_Energy_UXV.set(new GT_MetaTileEntity_Hatch_Energy(11303, "hatch.energy.tier.13", "UXV Energy Hatch",13).getStackForm(1L));
		ItemList.Hatch_Energy_OPV.set(new GT_MetaTileEntity_Hatch_Energy(11304, "hatch.energy.tier.14", "OPV Energy Hatch",14).getStackForm(1L));
		
		GT_ModHandler.addCraftingRecipe(ItemList.Hatch_Energy_ULV.get(1L, new Object[0]), bitsd, new Object[]{"XOL", "CMP", "XOL", 'M', ItemList.Hull_ULV, 'C', OrePrefixes.cableGt01.get(Materials.Lead), 'X', OrePrefixes.circuit.get(Materials.Primitive), 'O', ItemList.ULV_Coil, 'L', OrePrefixes.cell.get(Materials.Lubricant), 'P', OrePrefixes.rotor.get(Materials.Lead)});
		GT_ModHandler.addCraftingRecipe(ItemList.Hatch_Energy_LV.get(1L, new Object[0]), bitsd, new Object[]{"XOL", "CMP", "XOL", 'M', ItemList.Hull_LV, 'C', OrePrefixes.cableGt01.get(Materials.Tin), 'X', OrePrefixes.circuit.get(Materials.Basic), 'O', ItemList.LV_Coil, 'L', OrePrefixes.cell.get(Materials.Lubricant), 'P', ItemList.Electric_Pump_LV});
		GT_ModHandler.addCraftingRecipe(ItemList.Hatch_Energy_MV.get(1L, new Object[0]), bitsd, new Object[]{"XOL", "CMP", "XOL", 'M', ItemList.Hull_MV, 'C', OrePrefixes.cableGt01.get(Materials.Copper), 'X', OrePrefixes.circuit.get(Materials.Good), 'O', ItemList.MV_Coil, 'L', OrePrefixes.cell.get(Materials.Lubricant), 'P', ItemList.Electric_Pump_MV});

        ItemList.Hatch_Input_ULV.set(new GT_MetaTileEntity_Hatch_Input(50, "hatch.input.tier.00", "Input Hatch (ULV)", 0).getStackForm(1L));
        ItemList.Hatch_Input_LV.set(new GT_MetaTileEntity_Hatch_Input(51, "hatch.input.tier.01", "Input Hatch (LV)", 1).getStackForm(1L));
        ItemList.Hatch_Input_MV.set(new GT_MetaTileEntity_Hatch_Input(52, "hatch.input.tier.02", "Input Hatch (MV)", 2).getStackForm(1L));
        ItemList.Hatch_Input_HV.set(new GT_MetaTileEntity_Hatch_Input(53, "hatch.input.tier.03", "Input Hatch (HV)", 3).getStackForm(1L));
        ItemList.Hatch_Input_EV.set(new GT_MetaTileEntity_Hatch_Input(54, "hatch.input.tier.04", "Input Hatch (EV)", 4).getStackForm(1L));
        ItemList.Hatch_Input_IV.set(new GT_MetaTileEntity_Hatch_Input(55, "hatch.input.tier.05", "Input Hatch (IV)", 5).getStackForm(1L));
        ItemList.Hatch_Input_LuV.set(new GT_MetaTileEntity_Hatch_Input(56, "hatch.input.tier.06", "Input Hatch (LuV)", 6).getStackForm(1L));
        ItemList.Hatch_Input_ZPM.set(new GT_MetaTileEntity_Hatch_Input(57, "hatch.input.tier.07", "Input Hatch (ZPM)", 7).getStackForm(1L));
        ItemList.Hatch_Input_UV.set(new GT_MetaTileEntity_Hatch_Input(58, "hatch.input.tier.08", "Input Hatch (UV)", 8).getStackForm(1L));
        ItemList.Hatch_Input_MAX.set(new GT_MetaTileEntity_Hatch_Input(59, "hatch.input.tier.09", "Input Hatch (UHV)", 9).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(ItemList.Hatch_Input_ULV.get(1L, new Object[0]), bitsd, new Object[]{"CX ", "MI ", 'M', ItemList.Hull_ULV, 'C', OrePrefixes.plate.get(Materials.Rubber), 'I', OrePrefixes.pipeMedium.get(Materials.Copper), 'X', ItemList.Cell_Empty});
		GT_ModHandler.addCraftingRecipe(ItemList.Hatch_Input_LV.get(1L, new Object[0]), bitsd, new Object[]{"CX ", "MI ", 'M', ItemList.Hull_LV, 'C', ItemList.Electric_Pump_LV,  'I', OrePrefixes.pipeMedium.get(Materials.Bronze), 'X', ItemList.Large_Fluid_Cell_Steel});
		GT_ModHandler.addCraftingRecipe(ItemList.Hatch_Input_MV.get(1L, new Object[0]), bitsd, new Object[]{"CX ", "MI ", 'M', ItemList.Hull_MV, 'C', ItemList.Electric_Pump_MV,  'I', OrePrefixes.pipeMedium.get(Materials.Steel),'X', ItemList.Large_Fluid_Cell_Aluminium});
		
        ItemList.Hatch_Output_ULV.set(new GT_MetaTileEntity_Hatch_Output(60, "hatch.output.tier.00", "Output Hatch (ULV)", 0).getStackForm(1L));
        ItemList.Hatch_Output_LV.set(new GT_MetaTileEntity_Hatch_Output(61, "hatch.output.tier.01", "Output Hatch (LV)", 1).getStackForm(1L));
        ItemList.Hatch_Output_MV.set(new GT_MetaTileEntity_Hatch_Output(62, "hatch.output.tier.02", "Output Hatch (MV)", 2).getStackForm(1L));
        ItemList.Hatch_Output_HV.set(new GT_MetaTileEntity_Hatch_Output(63, "hatch.output.tier.03", "Output Hatch (HV)", 3).getStackForm(1L));
        ItemList.Hatch_Output_EV.set(new GT_MetaTileEntity_Hatch_Output(64, "hatch.output.tier.04", "Output Hatch (EV)", 4).getStackForm(1L));
        ItemList.Hatch_Output_IV.set(new GT_MetaTileEntity_Hatch_Output(65, "hatch.output.tier.05", "Output Hatch (IV)", 5).getStackForm(1L));
        ItemList.Hatch_Output_LuV.set(new GT_MetaTileEntity_Hatch_Output(66, "hatch.output.tier.06", "Output Hatch (LuV)", 6).getStackForm(1L));
        ItemList.Hatch_Output_ZPM.set(new GT_MetaTileEntity_Hatch_Output(67, "hatch.output.tier.07", "Output Hatch (ZPM)", 7).getStackForm(1L));
        ItemList.Hatch_Output_UV.set(new GT_MetaTileEntity_Hatch_Output(68, "hatch.output.tier.08", "Output Hatch (UV)", 8).getStackForm(1L));
        ItemList.Hatch_Output_MAX.set(new GT_MetaTileEntity_Hatch_Output(69, "hatch.output.tier.09", "Output Hatch (UHV)", 9).getStackForm(1L));
        
        GT_ModHandler.addCraftingRecipe(ItemList.Hatch_Output_ULV.get(1L, new Object[0]), bitsd, new Object[]{" XC", "IM ", 'M', ItemList.Hull_ULV, 'C', OrePrefixes.plate.get(Materials.Rubber), 'I', OrePrefixes.pipeMedium.get(Materials.Copper), 'X', ItemList.Cell_Empty});
        GT_ModHandler.addCraftingRecipe(ItemList.Hatch_Output_LV.get(1L, new Object[0]), bitsd, new Object[]{" XC", "IM ", 'M', ItemList.Hull_LV, 'C', ItemList.Electric_Pump_LV,  'I', OrePrefixes.pipeMedium.get(Materials.Bronze), 'X', ItemList.Large_Fluid_Cell_Steel});
        GT_ModHandler.addCraftingRecipe(ItemList.Hatch_Output_MV.get(1L, new Object[0]), bitsd, new Object[]{" XC", "IM ", 'M', ItemList.Hull_MV, 'C', ItemList.Electric_Pump_MV,  'I', OrePrefixes.pipeMedium.get(Materials.Steel), 'X', ItemList.Large_Fluid_Cell_Aluminium});
        
        ItemList.Quantum_Tank_LV.set(new GT_MetaTileEntity_QuantumTank(120, "quantum.tank.tier.06", "Quantum Tank I", 6).getStackForm(1L));
        ItemList.Quantum_Tank_MV.set(new GT_MetaTileEntity_QuantumTank(121, "quantum.tank.tier.07", "Quantum Tank II", 7).getStackForm(1L));
        ItemList.Quantum_Tank_HV.set(new GT_MetaTileEntity_QuantumTank(122, "quantum.tank.tier.08", "Quantum Tank III", 8).getStackForm(1L));
        ItemList.Quantum_Tank_EV.set(new GT_MetaTileEntity_QuantumTank(123, "quantum.tank.tier.09", "Quantum Tank IV", 9).getStackForm(1L));
        ItemList.Quantum_Tank_IV.set(new GT_MetaTileEntity_QuantumTank(124, "quantum.tank.tier.10", "Quantum Tank V", 10).getStackForm(1L));

        ItemList.Quantum_Chest_LV.set(new GT_MetaTileEntity_QuantumChest(125, "quantum.chest.tier.06", "Quantum Chest I", 6).getStackForm(1L));
        ItemList.Quantum_Chest_MV.set(new GT_MetaTileEntity_QuantumChest(126, "quantum.chest.tier.07", "Quantum Chest II", 7).getStackForm(1L));
        ItemList.Quantum_Chest_HV.set(new GT_MetaTileEntity_QuantumChest(127, "quantum.chest.tier.08", "Quantum Chest III", 8).getStackForm(1L));
        ItemList.Quantum_Chest_EV.set(new GT_MetaTileEntity_QuantumChest(128, "quantum.chest.tier.09", "Quantum Chest IV", 9).getStackForm(1L));
        ItemList.Quantum_Chest_IV.set(new GT_MetaTileEntity_QuantumChest(129, "quantum.chest.tier.10", "Quantum Chest V", 10).getStackForm(1L));
        
        ItemList.Super_Tank_ULV.set(new GT_MetaTileEntity_SuperTank(139, "super.tank.tier.00", "Super Tank (Steam Age)", 0).getStackForm(1L));
        ItemList.Super_Tank_LV.set(new GT_MetaTileEntity_SuperTank(140, "super.tank.tier.01", "Super Tank I", 1).getStackForm(1L));
        ItemList.Super_Tank_MV.set(new GT_MetaTileEntity_SuperTank(141, "super.tank.tier.02", "Super Tank II", 2).getStackForm(1L));
        ItemList.Super_Tank_HV.set(new GT_MetaTileEntity_SuperTank(142, "super.tank.tier.03", "Super Tank III", 3).getStackForm(1L));
        ItemList.Super_Tank_EV.set(new GT_MetaTileEntity_SuperTank(143, "super.tank.tier.04", "Super Tank IV", 4).getStackForm(1L));
        ItemList.Super_Tank_IV.set(new GT_MetaTileEntity_SuperTank(144, "super.tank.tier.05", "Super Tank V", 5).getStackForm(1L));

        ItemList.Super_Chest_LV.set(new GT_MetaTileEntity_SuperChest(145, "super.chest.tier.01", "Super Chest I", 1).getStackForm(1L));
        ItemList.Super_Chest_MV.set(new GT_MetaTileEntity_SuperChest(146, "super.chest.tier.02", "Super Chest II", 2).getStackForm(1L));
        ItemList.Super_Chest_HV.set(new GT_MetaTileEntity_SuperChest(147, "super.chest.tier.03", "Super Chest III", 3).getStackForm(1L));
        ItemList.Super_Chest_EV.set(new GT_MetaTileEntity_SuperChest(148, "super.chest.tier.04", "Super Chest IV", 4).getStackForm(1L));
        ItemList.Super_Chest_IV.set(new GT_MetaTileEntity_SuperChest(149, "super.chest.tier.05", "Super Chest V", 5).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(ItemList.Super_Tank_ULV.get(1L), GT_ModHandler.RecipeBits.DISMANTLEABLE| GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED,  new Object[]{"DGD", "PMP", "DUD", 'U', ItemList.Electric_Pump_LV, 'M', ItemList.Casing_Tank_0, 'G', OrePrefixes.pipeMedium.get(Materials.Bronze), 'D', OrePrefixes.circuit.get(Materials.Primitive), 'P', OrePrefixes.plate.get(Materials.Steel)});
        GT_ModHandler.addCraftingRecipe(ItemList.Super_Tank_LV.get(1L), GT_ModHandler.RecipeBits.DISMANTLEABLE| GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED,  new Object[]{"DGD", "PMP", "DUD", 'U', ItemList.Electric_Pump_MV, 'M', ItemList.Casing_Tank_1, 'G', OrePrefixes.pipeLarge.get(Materials.Bronze), 'D', OrePrefixes.circuit.get(Materials.Basic), 'P', OrePrefixes.plate.get(Materials.Aluminium)});
        GT_ModHandler.addCraftingRecipe(ItemList.Super_Tank_MV.get(1L), GT_ModHandler.RecipeBits.DISMANTLEABLE| GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED,  new Object[]{"DGD", "PMP", "DUD", 'U', ItemList.Electric_Pump_HV, 'M', ItemList.Casing_Tank_2, 'G', OrePrefixes.pipeLarge.get(Materials.Steel), 'D', OrePrefixes.circuit.get(Materials.Good), 'P', OrePrefixes.plate.get(Materials.StainlessSteel)});
        GT_ModHandler.addCraftingRecipe(ItemList.Super_Tank_HV.get(1L), GT_ModHandler.RecipeBits.DISMANTLEABLE| GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED,  new Object[]{"DGD", "PMP", "DUD", 'U', ItemList.Electric_Pump_HV, 'M', ItemList.Casing_Tank_3, 'G', ItemList.Field_Generator_LV, 'D', OrePrefixes.circuit.get(Materials.Advanced), 'P', OrePrefixes.plate.get(Materials.StainlessSteel)});
        GT_ModHandler.addCraftingRecipe(ItemList.Super_Tank_EV.get(1L), GT_ModHandler.RecipeBits.DISMANTLEABLE| GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED,  new Object[]{"DGD", "PMP", "DUD", 'U', ItemList.Electric_Pump_EV, 'M', ItemList.Casing_Tank_4, 'G', ItemList.Field_Generator_MV, 'D', OrePrefixes.circuit.get(Materials.Data),'P', OrePrefixes.plate.get(Materials.Titanium)});
        GT_ModHandler.addCraftingRecipe(ItemList.Super_Tank_IV.get(1L), GT_ModHandler.RecipeBits.DISMANTLEABLE| GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED,  new Object[]{"DGD", "PMP", "DUD", 'U', ItemList.Electric_Pump_EV, 'M', ItemList.Casing_Tank_5, 'G', ItemList.Field_Generator_HV, 'D', OrePrefixes.circuit.get(Materials.Elite), 'P', OrePrefixes.plate.get(Materials.Titanium)});

        GT_ModHandler.addCraftingRecipe(ItemList.Quantum_Tank_LV.get(1L), GT_ModHandler.RecipeBits.DISMANTLEABLE| GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED,  new Object[]{"DGD", "PMP", "DUD", 'U', ItemList.Electric_Pump_IV, 'M', ItemList.Casing_Tank_6, 'G', ItemList.Field_Generator_EV, 'D', OrePrefixes.circuit.get(Materials.Master), 'P', OrePrefixes.plate.get(Materials.TungstenSteel)});
        GT_ModHandler.addCraftingRecipe(ItemList.Quantum_Tank_MV.get(1L), GT_ModHandler.RecipeBits.DISMANTLEABLE| GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED,  new Object[]{"DGD", "PMP", "DUD", 'U', ItemList.Electric_Pump_IV, 'M', ItemList.Casing_Tank_7, 'G', ItemList.Field_Generator_IV, 'D', OrePrefixes.circuit.get(Materials.Ultimate), 'P', OrePrefixes.plate.get(Materials.HSSG)});
        GT_ModHandler.addCraftingRecipe(ItemList.Quantum_Tank_HV.get(1L), GT_ModHandler.RecipeBits.DISMANTLEABLE| GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED,  new Object[]{"DGD", "PMP", "DUD", 'U', ItemList.Electric_Pump_LuV, 'M', ItemList.Casing_Tank_8, 'G', ItemList.Field_Generator_LuV, 'D', OrePrefixes.circuit.get(Materials.Superconductor), 'P', OrePrefixes.plate.get(Materials.HSSS)});
        GT_ModHandler.addCraftingRecipe(ItemList.Quantum_Tank_EV.get(1L), GT_ModHandler.RecipeBits.DISMANTLEABLE| GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED,  new Object[]{"DGD", "PMP", "DUD", 'U', ItemList.Electric_Pump_ZPM, 'M', ItemList.Casing_Tank_9, 'G', ItemList.Field_Generator_ZPM, 'D', OrePrefixes.circuit.get(Materials.Infinite), 'P', OrePrefixes.plate.get(Materials.Europium)});
        GT_ModHandler.addCraftingRecipe(ItemList.Quantum_Tank_IV.get(1L), GT_ModHandler.RecipeBits.DISMANTLEABLE| GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED,  new Object[]{"DGD", "PMP", "DUD", 'U', ItemList.Electric_Pump_UV, 'M', ItemList.Casing_Tank_10, 'G', ItemList.Field_Generator_UV, 'D', OrePrefixes.circuit.get(Materials.Bio), 'P', OrePrefixes.plate.get(Materials.Americium)});

        GT_ModHandler.addCraftingRecipe(ItemList.Super_Chest_LV.get(1L), bitsd,  new Object[]{"DMD", "PCP", "DGD", 'C', ItemList.Hull_LV, 'M', ItemList.Conveyor_Module_MV, 'G', OrePrefixes.pipeLarge.get(Materials.Brass), 'D', OrePrefixes.circuit.get(Materials.Basic),     'P', OrePrefixes.plateDense.get(Materials.Iron)});
        GT_ModHandler.addCraftingRecipe(ItemList.Super_Chest_MV.get(1L), bitsd,  new Object[]{"DMD", "PCP", "DGD", 'C', ItemList.Hull_MV, 'M', ItemList.Conveyor_Module_HV, 'G', OrePrefixes.pipeLarge.get(Materials.Aluminium), 'D', OrePrefixes.circuit.get(Materials.Good),    'P', OrePrefixes.plateDense.get(Materials.Steel)});
        GT_ModHandler.addCraftingRecipe(ItemList.Super_Chest_HV.get(1L), bitsd,  new Object[]{"DMD", "PCP", "DGD", 'C', ItemList.Hull_HV, 'M', ItemList.Conveyor_Module_HV, 'G', ItemList.Field_Generator_LV, 'D', OrePrefixes.circuit.get(Materials.Advanced), 'P', OrePrefixes.plateQuintuple.get(Materials.Aluminium)});
        GT_ModHandler.addCraftingRecipe(ItemList.Super_Chest_EV.get(1L), bitsd,  new Object[]{"DMD", "PCP", "DGD", 'C', ItemList.Hull_EV, 'M', ItemList.Conveyor_Module_EV, 'G', ItemList.Field_Generator_MV, 'D', OrePrefixes.circuit.get(Materials.Data), 'P', OrePrefixes.plateQuintuple.get(Materials.StainlessSteel)});
        GT_ModHandler.addCraftingRecipe(ItemList.Super_Chest_IV.get(1L), bitsd,  new Object[]{"DMD", "PCP", "DGD", 'C', ItemList.Hull_IV, 'M', ItemList.Conveyor_Module_EV, 'G', ItemList.Field_Generator_HV, 'D', OrePrefixes.circuit.get(Materials.Elite), 'P', OrePrefixes.plateQuadruple.get(Materials.Titanium)});

        GT_ModHandler.addCraftingRecipe(ItemList.Quantum_Chest_LV.get(1L), bitsd, new Object[]{"DMD", "PCP", "DGD", 'C', ItemList.Hull_LuV, 'M', ItemList.Conveyor_Module_IV, 'G', ItemList.Field_Generator_EV, 'D', OrePrefixes.circuit.get(Materials.Master),     'P', OrePrefixes.plateQuadruple.get(Materials.TungstenSteel)});
        GT_ModHandler.addCraftingRecipe(ItemList.Quantum_Chest_MV.get(1L), bitsd, new Object[]{"DMD", "PCP", "DGD", 'C', ItemList.Hull_ZPM, 'M', ItemList.Conveyor_Module_IV, 'G', ItemList.Field_Generator_IV, 'D', OrePrefixes.circuit.get(Materials.Ultimate),    'P', OrePrefixes.plateTriple.get(Materials.HSSG)});
        GT_ModHandler.addCraftingRecipe(ItemList.Quantum_Chest_HV.get(1L), bitsd, new Object[]{"DMD", "PCP", "DGD", 'C', ItemList.Hull_UV, 'M', ItemList.Conveyor_Module_LuV, 'G', ItemList.Field_Generator_LuV, 'D', OrePrefixes.circuit.get(Materials.Superconductor),   'P', OrePrefixes.plateTriple.get(Materials.HSSS)});
        GT_ModHandler.addCraftingRecipe(ItemList.Quantum_Chest_EV.get(1L), bitsd, new Object[]{"DMD", "PCP", "DGD", 'C', ItemList.Hull_MAX, 'M', ItemList.Conveyor_Module_ZPM, 'G', ItemList.Field_Generator_ZPM, 'D', OrePrefixes.circuit.get(Materials.Infinite), 'P', OrePrefixes.plateDouble.get(Materials.Europium)});
        GT_ModHandler.addCraftingRecipe(ItemList.Quantum_Chest_IV.get(1L), bitsd, new Object[]{"DMD", "PCP", "DGD", 'C', ItemList.Hull_UEV, 'M', ItemList.Conveyor_Module_UV, 'G', ItemList.Field_Generator_UV, 'D', OrePrefixes.circuit.get(Materials.Bio), 'P', OrePrefixes.plate.get(Materials.Americium)});

        GT_ModHandler.addCraftingRecipe(ItemList.Casing_Tank_0.get(1L), bitsd, new Object[]{"PPP", "PIP", "PPP", 'P', OrePrefixes.plate.get(Materials.WroughtIron), 'I', OrePrefixes.pipeLarge.get(Materials.Bronze)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_Tank_1.get(1L), bitsd, new Object[]{"PPP", "PIP", "PPP", 'P', OrePrefixes.plate.get(Materials.Steel), 'I', OrePrefixes.pipeLarge.get(Materials.Steel)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_Tank_2.get(1L), bitsd, new Object[]{"PPP", "PIP", "PPP", 'P', OrePrefixes.plate.get(Materials.Aluminium), 'I', OrePrefixes.pipeLarge.get(Materials.Plastic)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_Tank_3.get(1L), bitsd, new Object[]{"PPP", "PIP", "PPP", 'P', OrePrefixes.plate.get(Materials.StainlessSteel), 'I', OrePrefixes.pipeLarge.get(Materials.Polytetrafluoroethylene)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_Tank_4.get(1L), bitsd, new Object[]{"PPP", "PIP", "PPP", 'P', OrePrefixes.plate.get(Materials.Titanium), 'I', OrePrefixes.pipeLarge.get(Materials.StainlessSteel)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_Tank_5.get(1L), bitsd, new Object[]{"PPP", "PIP", "PPP", 'P', OrePrefixes.plate.get(Materials.TungstenSteel), 'I', OrePrefixes.pipeLarge.get(Materials.Titanium)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_Tank_6.get(1L), bitsd, new Object[]{"PPP", "PIP", "PPP", 'P', OrePrefixes.plate.get(Materials.Chrome), 'I', OrePrefixes.pipeLarge.get(Materials.TungstenSteel)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_Tank_7.get(1L), bitsd, new Object[]{"PPP", "PIP", "PPP", 'P', OrePrefixes.plate.get(Materials.Iridium), 'I', OrePrefixes.pipeLarge.get(Materials.NiobiumTitanium)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_Tank_8.get(1L), bitsd, new Object[]{"PPP", "PIP", "PPP", 'P', OrePrefixes.plate.get(Materials.Osmium), 'I', OrePrefixes.pipeLarge.get(Materials.Enderium)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_Tank_9.get(1L), bitsd, new Object[]{"PPP", "PIP", "PPP", 'P', OrePrefixes.plate.get(Materials.Tritanium), 'I', OrePrefixes.pipeLarge.get(Materials.Naquadah)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_Tank_10.get(1L), bitsd, new Object[]{"PPP", "PIP", "PPP", 'P', OrePrefixes.plate.get(Materials.Neutronium), 'I', OrePrefixes.pipeLarge.get(Materials.NetherStar)});

        ItemList.Hatch_Input_Bus_ULV.set(new GT_MetaTileEntity_Hatch_InputBus(70, "hatch.input_bus.tier.00", "Input Bus (ULV)", 0).getStackForm(1L));
        ItemList.Hatch_Input_Bus_LV.set(new GT_MetaTileEntity_Hatch_InputBus(71, "hatch.input_bus.tier.01", "Input Bus (LV)", 1).getStackForm(1L));
        ItemList.Hatch_Input_Bus_MV.set(new GT_MetaTileEntity_Hatch_InputBus(72, "hatch.input_bus.tier.02", "Input Bus (MV)", 2).getStackForm(1L));
        ItemList.Hatch_Input_Bus_HV.set(new GT_MetaTileEntity_Hatch_InputBus(73, "hatch.input_bus.tier.03", "Input Bus (HV)", 3).getStackForm(1L));
        ItemList.Hatch_Input_Bus_EV.set(new GT_MetaTileEntity_Hatch_InputBus(74, "hatch.input_bus.tier.04", "Input Bus (EV)", 4).getStackForm(1L));
        ItemList.Hatch_Input_Bus_IV.set(new GT_MetaTileEntity_Hatch_InputBus(75, "hatch.input_bus.tier.05", "Input Bus (IV)", 5).getStackForm(1L));
        ItemList.Hatch_Input_Bus_LuV.set(new GT_MetaTileEntity_Hatch_InputBus(76, "hatch.input_bus.tier.06", "Input Bus (LuV)", 6).getStackForm(1L));
        ItemList.Hatch_Input_Bus_ZPM.set(new GT_MetaTileEntity_Hatch_InputBus(77, "hatch.input_bus.tier.07", "Input Bus (ZPM)", 7).getStackForm(1L));
        ItemList.Hatch_Input_Bus_UV.set(new GT_MetaTileEntity_Hatch_InputBus(78, "hatch.input_bus.tier.08", "Input Bus (UV)", 8).getStackForm(1L));
        ItemList.Hatch_Input_Bus_MAX.set(new GT_MetaTileEntity_Hatch_InputBus(79, "hatch.input_bus.tier.09", "Input Bus (UHV)", 9).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(ItemList.Hatch_Input_Bus_ULV.get(1L, new Object[0]), bitsd, new Object[]{"CX ", "MI ", 'M', ItemList.Hull_ULV, 'C', OrePrefixes.plate.get(Materials.Lead), 'I', OrePrefixes.pipeMedium.get(Materials.Tin), 'X', new ItemStack(Blocks.chest, 1, 0)});
        GT_ModHandler.addCraftingRecipe(ItemList.Hatch_Input_Bus_LV.get(1L, new Object[0]), bitsd, new Object[]{"CX ", "MI ", 'M', ItemList.Hull_LV, 'C', ItemList.Conveyor_Module_LV,  'I', OrePrefixes.pipeMedium.get(Materials.Nickel), 'X', new ItemStack(Blocks.chest, 1, 0)});
        GT_ModHandler.addCraftingRecipe(ItemList.Hatch_Input_Bus_MV.get(1L, new Object[0]), bitsd, new Object[]{"CX ", "MI ", 'M', ItemList.Hull_MV, 'C', ItemList.Conveyor_Module_MV,  'I', OrePrefixes.pipeMedium.get(Materials.Cobalt), 'X', new ItemStack(Blocks.chest, 1, 0)});	
        
        ItemList.Hatch_Output_Bus_ULV.set(new GT_MetaTileEntity_Hatch_OutputBus(80, "hatch.output_bus.tier.00", "Output Bus (ULV)", 0).getStackForm(1L));
        ItemList.Hatch_Output_Bus_LV.set(new GT_MetaTileEntity_Hatch_OutputBus(81, "hatch.output_bus.tier.01", "Output Bus (LV)", 1).getStackForm(1L));
        ItemList.Hatch_Output_Bus_MV.set(new GT_MetaTileEntity_Hatch_OutputBus(82, "hatch.output_bus.tier.02", "Output Bus (MV)", 2).getStackForm(1L));
        ItemList.Hatch_Output_Bus_HV.set(new GT_MetaTileEntity_Hatch_OutputBus(83, "hatch.output_bus.tier.03", "Output Bus (HV)", 3).getStackForm(1L));
        ItemList.Hatch_Output_Bus_EV.set(new GT_MetaTileEntity_Hatch_OutputBus(84, "hatch.output_bus.tier.04", "Output Bus (EV)", 4).getStackForm(1L));
        ItemList.Hatch_Output_Bus_IV.set(new GT_MetaTileEntity_Hatch_OutputBus(85, "hatch.output_bus.tier.05", "Output Bus (IV)", 5).getStackForm(1L));
        ItemList.Hatch_Output_Bus_LuV.set(new GT_MetaTileEntity_Hatch_OutputBus(86, "hatch.output_bus.tier.06", "Output Bus (LuV)", 6).getStackForm(1L));
        ItemList.Hatch_Output_Bus_ZPM.set(new GT_MetaTileEntity_Hatch_OutputBus(87, "hatch.output_bus.tier.07", "Output Bus (ZPM)", 7).getStackForm(1L));
        ItemList.Hatch_Output_Bus_UV.set(new GT_MetaTileEntity_Hatch_OutputBus(88, "hatch.output_bus.tier.08", "Output Bus (UV)", 8).getStackForm(1L));
        ItemList.Hatch_Output_Bus_MAX.set(new GT_MetaTileEntity_Hatch_OutputBus(89, "hatch.output_bus.tier.09", "Output Bus (UHV)", 9).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(ItemList.Hatch_Output_Bus_ULV.get(1L, new Object[0]), bitsd, new Object[]{" XC", "IM ", 'M', ItemList.Hull_ULV, 'C', OrePrefixes.plate.get(Materials.Lead), 'I', OrePrefixes.pipeMedium.get(Materials.Tin), 'X', new ItemStack(Blocks.chest, 1, 0)});
        GT_ModHandler.addCraftingRecipe(ItemList.Hatch_Output_Bus_LV.get(1L, new Object[0]), bitsd, new Object[]{" XC", "IM ", 'M', ItemList.Hull_LV, 'C', ItemList.Conveyor_Module_LV,  'I', OrePrefixes.pipeMedium.get(Materials.Nickel), 'X', new ItemStack(Blocks.chest, 1, 0)});
        GT_ModHandler.addCraftingRecipe(ItemList.Hatch_Output_Bus_MV.get(1L, new Object[0]), bitsd, new Object[]{" XC", "IM ", 'M', ItemList.Hull_MV, 'C', ItemList.Conveyor_Module_MV,  'I', OrePrefixes.pipeMedium.get(Materials.Cobalt), 'X', new ItemStack(Blocks.chest, 1, 0)});
        
        ItemList.Hatch_Maintenance.set(new GT_MetaTileEntity_Hatch_Maintenance(90, "hatch.maintenance", "Maintenance Hatch", 1).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(ItemList.Hatch_Maintenance.get(1L, new Object[0]), bitsd, new Object[]{"dwx", "hMc", "fsr", 'M', ItemList.Hull_LV});
        
        ItemList.Hatch_AutoMaintenance.set(new GT_MetaTileEntity_Hatch_Maintenance(111, "hatch.maintenance.auto", "Auto Maintenance Hatch", 5, true).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(ItemList.Hatch_AutoMaintenance.get(1L, new Object[0]), bitsd, new Object[]{"CHC", "AMA", "CHC", 'M', ItemList.Hull_IV,'H',ItemList.Hatch_Maintenance,'A',ItemList.Robot_Arm_IV,'C',OrePrefixes.circuit.get(Materials.Master)});

	    ItemList.Hatch_DataAccess_EV.set(new GT_MetaTileEntity_Hatch_DataAccess(131, "hatch.dataaccess", "Data Access Hatch", 4).getStackForm(1L));
	    ItemList.Hatch_DataAccess_LuV.set(new GT_MetaTileEntity_Hatch_DataAccess(132, "hatch.dataaccess.adv", "Advanced Data Access Hatch", 6).getStackForm(1L));
	    ItemList.Hatch_DataAccess_UV.set(new GT_MetaTileEntity_Hatch_DataAccess(12100, "hatch.dataaccess.auto", "Automatable Data Access Hatch", 8).getStackForm(1L));
	    
        GT_ModHandler.addCraftingRecipe(ItemList.Hatch_DataAccess_EV.get(1L, new Object[0]), bitsd, new Object[]{"COC", "OMO", "COC", 'M', ItemList.Hull_EV, 'O', ItemList.Tool_DataStick , 'C' ,OrePrefixes.circuit.get(Materials.Elite)});
        GT_ModHandler.addCraftingRecipe(ItemList.Hatch_DataAccess_LuV.get(1L, new Object[0]), bitsd, new Object[]{"COC", "OMO", "COC", 'M', ItemList.Hull_LuV, 'O', ItemList.Tool_DataOrb , 'C' ,OrePrefixes.circuit.get(Materials.Ultimate)});
        GT_ModHandler.addCraftingRecipe(ItemList.Hatch_DataAccess_UV.get(1L, new Object[0]), bitsd, new Object[]{"CRC", "OMO", "CRC", 'M', ItemList.Hull_UV, 'O', ItemList.Tool_DataOrb , 'C' ,OrePrefixes.circuit.get(Materials.Infinite), 'R', ItemList.Robot_Arm_UV});
        ItemList.Hatch_Muffler_LV.set(new GT_MetaTileEntity_Hatch_Muffler(91, "hatch.muffler.tier.01", "Muffler Hatch (LV)", 1).getStackForm(1L));
        ItemList.Hatch_Muffler_MV.set(new GT_MetaTileEntity_Hatch_Muffler(92, "hatch.muffler.tier.02", "Muffler Hatch (MV)", 2).getStackForm(1L));
        ItemList.Hatch_Muffler_HV.set(new GT_MetaTileEntity_Hatch_Muffler(93, "hatch.muffler.tier.03", "Muffler Hatch (HV)", 3).getStackForm(1L));
        ItemList.Hatch_Muffler_EV.set(new GT_MetaTileEntity_Hatch_Muffler(94, "hatch.muffler.tier.04", "Muffler Hatch (EV)", 4).getStackForm(1L));
        ItemList.Hatch_Muffler_IV.set(new GT_MetaTileEntity_Hatch_Muffler(95, "hatch.muffler.tier.05", "Muffler Hatch (IV)", 5).getStackForm(1L));
        ItemList.Hatch_Muffler_LuV.set(new GT_MetaTileEntity_Hatch_Muffler(96, "hatch.muffler.tier.06", "Muffler Hatch (LuV)", 6).getStackForm(1L));
        ItemList.Hatch_Muffler_ZPM.set(new GT_MetaTileEntity_Hatch_Muffler(97, "hatch.muffler.tier.07", "Muffler Hatch (ZPM)", 7).getStackForm(1L));
        ItemList.Hatch_Muffler_UV.set(new GT_MetaTileEntity_Hatch_Muffler(98, "hatch.muffler.tier.08", "Muffler Hatch (UV)", 8).getStackForm(1L));
        ItemList.Hatch_Muffler_MAX.set(new GT_MetaTileEntity_Hatch_Muffler(99, "hatch.muffler.tier.09", "Muffler Hatch (UHV)", 9).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(ItemList.Hatch_Muffler_LV.get(1L, new Object[0]), bitsd, new Object[]{"MX ", "PR ", 'M', ItemList.Hull_LV, 'P', OrePrefixes.pipeMedium.get(Materials.Bronze), 'R', OrePrefixes.rotor.get(Materials.Bronze), 'X', ItemList.Electric_Motor_LV});
        GT_ModHandler.addCraftingRecipe(ItemList.Hatch_Muffler_MV.get(1L, new Object[0]), bitsd, new Object[]{"MX ", "PR ", 'M', ItemList.Hull_LV, 'P', OrePrefixes.pipeMedium.get(Materials.Steel), 'R', OrePrefixes.rotor.get(Materials.Steel), 'X', ItemList.Electric_Motor_MV});
        
        ItemList.Machine_Bronze_Boiler.set(new GT_MetaTileEntity_Boiler_Bronze(100, "boiler.bronze", "Small Coal Boiler").getStackForm(1L));
        ItemList.Machine_Steel_Boiler.set(new GT_MetaTileEntity_Boiler_Steel(101, "boiler.steel", "High Pressure Coal Boiler").getStackForm(1L));
        ItemList.Machine_Steel_Boiler_Lava.set(new GT_MetaTileEntity_Boiler_Lava(102, "boiler.lava", "High Pressure Lava Boiler").getStackForm(1L));
        ItemList.Machine_Bronze_Boiler_Solar.set(new GT_MetaTileEntity_Boiler_Solar(105, "boiler.solar", "Simple Solar Boiler").getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(ItemList.Machine_Bronze_Boiler.get(1L, new Object[0]), bits, new Object[]{aTextPlate, "PwP", "BFB", 'F', OreDictNames.craftingIronFurnace, 'P', OrePrefixes.plate.get(Materials.Bronze), 'B', new ItemStack(Blocks.brick_block, 1)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_Steel_Boiler.get(1L, new Object[0]), bits, new Object[]{aTextPlate, "PwP", "BFB", 'F', OreDictNames.craftingIronFurnace, 'P', OrePrefixes.plate.get(Materials.Steel), 'B', new ItemStack(Blocks.brick_block, 1)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_Steel_Boiler_Lava.get(1L, new Object[0]), bits, new Object[]{aTextPlate, "GGG", aTextPlateMotor, 'M', ItemList.Hull_Steel_Bricks, 'P', OrePrefixes.plate.get(Materials.Steel), 'G', new ItemStack(Blocks.glass, 1)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_Bronze_Boiler_Solar.get(1L, new Object[0]), bits, new Object[]{"GGG", "SSS", aTextPlateMotor, 'M', ItemList.Hull_Bronze_Bricks, 'P', OrePrefixes.pipeSmall.get(Materials.Bronze), 'S', OrePrefixes.plateDouble.get(Materials.Silver), 'G', new ItemStack(Blocks.glass, 1)});

        ItemList.Machine_Bronze_BlastFurnace.set(new GT_MetaTileEntity_BronzeBlastFurnace(108, "bronzemachine.blastfurnace", "Bronze Plated Blast Furnace").getStackForm(1L));
        if (!Loader.isModLoaded("terrafirmacraft")) {
            GT_ModHandler.addCraftingRecipe(ItemList.Machine_Bronze_BlastFurnace.get(1L, new Object[0]), bits, new Object[]{"PFP", "FwF", "PFP", 'P', OrePrefixes.plate.get(Materials.Bronze), 'F', OreDictNames.craftingFurnace});
        }

        ItemList.Machine_Bricked_BlastFurnace.set(new GT_MetaTileEntity_BrickedBlastFurnace(130, "multimachine.brickedblastfurnace", "Bricked Blast Furnace").getStackForm(1L));
        if (GT_Mod.gregtechproxy.mBrickedBlastFurnace) {
        	GT_ModHandler.addCraftingRecipe(ItemList.Machine_Bricked_BlastFurnace.get(1L, new Object[0]), GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"PFP", "FwF", "PFP", 'P', ItemList.Casing_Firebricks, 'F', OreDictNames.craftingIronFurnace});
        }
        
        ItemList.Machine_Bronze_Furnace.set(new GT_MetaTileEntity_Furnace_Bronze(103, "bronzemachine.furnace", "Steam Furnace").getStackForm(1L));
        ItemList.Machine_Steel_Furnace.set(new GT_MetaTileEntity_Furnace_Steel(104, "steelmachine.furnace", "High Pressure Furnace").getStackForm(1L));
        ItemList.Machine_Bronze_Macerator.set(new GT_MetaTileEntity_Macerator_Bronze(106, "bronzemachine.macerator", "Steam Macerator").getStackForm(1L));
        ItemList.Machine_Steel_Macerator.set(new GT_MetaTileEntity_Macerator_Steel(107, "steelmachine.macerator", "High Pressure Macerator").getStackForm(1L));
        ItemList.Machine_Bronze_Extractor.set(new GT_MetaTileEntity_Extractor_Bronze(109, "bronzemachine.extractor", "Steam Extractor").getStackForm(1L));
        ItemList.Machine_Steel_Extractor.set(new GT_MetaTileEntity_Extractor_Steel(110, "steelmachine.extractor", "High Pressure Extractor").getStackForm(1L));
        ItemList.Machine_Bronze_Hammer.set(new GT_MetaTileEntity_ForgeHammer_Bronze(112, "bronzemachine.hammer", "Steam Forge Hammer").getStackForm(1L));
        ItemList.Machine_Steel_Hammer.set(new GT_MetaTileEntity_ForgeHammer_Steel(113, "steelmachine.hammer", "High Pressure Forge Hammer").getStackForm(1L));
        ItemList.Machine_Bronze_Compressor.set(new GT_MetaTileEntity_Compressor_Bronze(115, "bronzemachine.compressor", "Steam Compressor").getStackForm(1L));
        ItemList.Machine_Steel_Compressor.set(new GT_MetaTileEntity_Compressor_Steel(116, "steelmachine.compressor", "High Pressure Compressor").getStackForm(1L));
        ItemList.Machine_Bronze_AlloySmelter.set(new GT_MetaTileEntity_AlloySmelter_Bronze(118, "bronzemachine.alloysmelter", "Steam Alloy Smelter").getStackForm(1L));
        ItemList.Machine_Steel_AlloySmelter.set(new GT_MetaTileEntity_AlloySmelter_Steel(119, "steelmachine.alloysmelter", "High Pressure Alloy Smelter").getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(ItemList.Machine_Bronze_Furnace.get(1L, new Object[0]), bits, new Object[]{"XXX", "XMX", "XFX", 'M', ItemList.Hull_Bronze_Bricks, 'X', OrePrefixes.pipeSmall.get(Materials.Bronze), 'F', OreDictNames.craftingFurnace});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_Steel_Furnace.get(1L, new Object[0]), bits, new Object[]{"XSX", "PMP", "XXX", 'M', ItemList.Machine_Bronze_Furnace, 'X', OrePrefixes.pipeSmall.get(Materials.WroughtIron), 'P', OrePrefixes.plate.get(Materials.WroughtIron), 'S', OrePrefixes.plate.get(Materials.Steel)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_Bronze_Macerator.get(1L, new Object[0]), bits, new Object[]{"DXD", "XMX", "PXP", 'M', ItemList.Hull_Bronze, 'X', OrePrefixes.pipeSmall.get(Materials.Bronze), 'P', OreDictNames.craftingPiston, 'D', OrePrefixes.gem.get(Materials.Diamond)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_Steel_Macerator.get(1L, new Object[0]), bits, new Object[]{"PSP", "XMX", "PPP", 'M', ItemList.Machine_Bronze_Macerator, 'X', OrePrefixes.pipeSmall.get(Materials.WroughtIron), 'P', OrePrefixes.plate.get(Materials.WroughtIron), 'S', OrePrefixes.plate.get(Materials.Steel)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_Bronze_Extractor.get(1L, new Object[0]), bits, new Object[]{"XXX", "PMG", "XXX", 'M', ItemList.Hull_Bronze, 'X', OrePrefixes.pipeSmall.get(Materials.Bronze), 'P', OreDictNames.craftingPiston, 'G', new ItemStack(Blocks.glass, 1)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_Steel_Extractor.get(1L, new Object[0]), bits, new Object[]{"XSX", "PMP", "XXX", 'M', ItemList.Machine_Bronze_Extractor, 'X', OrePrefixes.pipeSmall.get(Materials.WroughtIron), 'P', OrePrefixes.plate.get(Materials.WroughtIron), 'S', OrePrefixes.plate.get(Materials.Steel)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_Bronze_Hammer.get(1L, new Object[0]), bits, new Object[]{"XPX", "XMX", "XAX", 'M', ItemList.Hull_Bronze, 'X', OrePrefixes.pipeSmall.get(Materials.Bronze), 'P', OreDictNames.craftingPiston, 'A', OreDictNames.craftingAnvil});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_Steel_Hammer.get(1L, new Object[0]), bits, new Object[]{"PSP", "XMX", "PPP", 'M', ItemList.Machine_Bronze_Hammer, 'X', OrePrefixes.pipeSmall.get(Materials.WroughtIron), 'P', OrePrefixes.plate.get(Materials.WroughtIron), 'S', OrePrefixes.plate.get(Materials.Steel)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_Bronze_Compressor.get(1L, new Object[0]), bits, new Object[]{"XXX", aTextPlateMotor, "XXX", 'M', ItemList.Hull_Bronze, 'X', OrePrefixes.pipeSmall.get(Materials.Bronze), 'P', OreDictNames.craftingPiston});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_Steel_Compressor.get(1L, new Object[0]), bits, new Object[]{"XSX", "PMP", "XXX", 'M', ItemList.Machine_Bronze_Compressor, 'X', OrePrefixes.pipeSmall.get(Materials.WroughtIron), 'P', OrePrefixes.plate.get(Materials.WroughtIron), 'S', OrePrefixes.plate.get(Materials.Steel)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_Bronze_AlloySmelter.get(1L, new Object[0]), bits, new Object[]{"XXX", "FMF", "XXX", 'M', ItemList.Hull_Bronze_Bricks, 'X', OrePrefixes.pipeSmall.get(Materials.Bronze), 'F', OreDictNames.craftingFurnace});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_Steel_AlloySmelter.get(1L, new Object[0]), bits, new Object[]{"PSP", "PMP", "PXP", 'M', ItemList.Machine_Bronze_AlloySmelter, 'X', OrePrefixes.pipeSmall.get(Materials.WroughtIron), 'P', OrePrefixes.plate.get(Materials.WroughtIron), 'S', OrePrefixes.plate.get(Materials.Steel)});


        ItemList.Locker_ULV.set(new GT_MetaTileEntity_Locker(150, "locker.tier.00", "Ultra Low Voltage Locker", 0).getStackForm(1L));
        ItemList.Locker_LV.set(new GT_MetaTileEntity_Locker(151, "locker.tier.01", "Low Voltage Locker", 1).getStackForm(1L));
        ItemList.Locker_MV.set(new GT_MetaTileEntity_Locker(152, "locker.tier.02", "Medium Voltage Locker", 2).getStackForm(1L));
        ItemList.Locker_HV.set(new GT_MetaTileEntity_Locker(153, "locker.tier.03", "High Voltage Locker", 3).getStackForm(1L));
        ItemList.Locker_EV.set(new GT_MetaTileEntity_Locker(154, "locker.tier.04", "Extreme Voltage Locker", 4).getStackForm(1L));
        ItemList.Locker_IV.set(new GT_MetaTileEntity_Locker(155, "locker.tier.05", "Insane Voltage Locker", 5).getStackForm(1L));
        ItemList.Locker_LuV.set(new GT_MetaTileEntity_Locker(156, "locker.tier.06", "Ludicrous Voltage Locker", 6).getStackForm(1L));
        ItemList.Locker_ZPM.set(new GT_MetaTileEntity_Locker(157, "locker.tier.07", "ZPM Voltage Locker", 7).getStackForm(1L));
        ItemList.Locker_UV.set(new GT_MetaTileEntity_Locker(158, "locker.tier.08", "Ultimate Voltage Locker", 8).getStackForm(1L));
        ItemList.Locker_MAX.set(new GT_MetaTileEntity_Locker(159, "locker.tier.09", "MAX Voltage Locker", 9).getStackForm(1L));

        ItemList.Battery_Buffer_1by1_ULV.set(new GT_MetaTileEntity_BasicBatteryBuffer(160, "batterybuffer.01.tier.00", "Ultra Low Voltage Battery Buffer", 0, "", 1).getStackForm(1L));
        ItemList.Battery_Buffer_1by1_LV.set(new GT_MetaTileEntity_BasicBatteryBuffer(161, "batterybuffer.01.tier.01", "Low Voltage Battery Buffer", 1, "", 1).getStackForm(1L));
        ItemList.Battery_Buffer_1by1_MV.set(new GT_MetaTileEntity_BasicBatteryBuffer(162, "batterybuffer.01.tier.02", "Medium Voltage Battery Buffer", 2, "", 1).getStackForm(1L));
        ItemList.Battery_Buffer_1by1_HV.set(new GT_MetaTileEntity_BasicBatteryBuffer(163, "batterybuffer.01.tier.03", "High Voltage Battery Buffer", 3, "", 1).getStackForm(1L));
        ItemList.Battery_Buffer_1by1_EV.set(new GT_MetaTileEntity_BasicBatteryBuffer(164, "batterybuffer.01.tier.04", "Extreme Voltage Battery Buffer", 4, "", 1).getStackForm(1L));
        ItemList.Battery_Buffer_1by1_IV.set(new GT_MetaTileEntity_BasicBatteryBuffer(165, "batterybuffer.01.tier.05", "Insane Voltage Battery Buffer", 5, "", 1).getStackForm(1L));
        ItemList.Battery_Buffer_1by1_LuV.set(new GT_MetaTileEntity_BasicBatteryBuffer(166, "batterybuffer.01.tier.06", "Ludicrous Voltage Battery Buffer", 6, "", 1).getStackForm(1L));
        ItemList.Battery_Buffer_1by1_ZPM.set(new GT_MetaTileEntity_BasicBatteryBuffer(167, "batterybuffer.01.tier.07", "ZPM Voltage Battery Buffer", 7, "", 1).getStackForm(1L));
        ItemList.Battery_Buffer_1by1_UV.set(new GT_MetaTileEntity_BasicBatteryBuffer(168, "batterybuffer.01.tier.08", "Ultimate Voltage Battery Buffer", 8, "", 1).getStackForm(1L));
        ItemList.Battery_Buffer_1by1_MAX.set(new GT_MetaTileEntity_BasicBatteryBuffer(169, "batterybuffer.01.tier.09", "Ultimate High Voltage Battery Buffer", 9, "", 1).getStackForm(1L));
        ItemList.Battery_Buffer_1by1_UEV.set(new GT_MetaTileEntity_BasicBatteryBuffer(11270, "batterybuffer.01.tier.10", "Extremely Ultimate Battery Buffer",10,"", 1).getStackForm(1L));
        ItemList.Battery_Buffer_1by1_UIV.set(new GT_MetaTileEntity_BasicBatteryBuffer(11271, "batterybuffer.01.tier.11", "Insanely Ultimate Battery Buffer",11,"", 1).getStackForm(1L));
		ItemList.Battery_Buffer_1by1_UMV.set(new GT_MetaTileEntity_BasicBatteryBuffer(11272, "batterybuffer.01.tier.12", "Mega Ultimate Battery Buffer",12,"", 1).getStackForm(1L));
		ItemList.Battery_Buffer_1by1_UXV.set(new GT_MetaTileEntity_BasicBatteryBuffer(11273, "batterybuffer.01.tier.13", "Extended Mega Ultimate Battery Buffer",13,"", 1).getStackForm(1L));
	    ItemList.Battery_Buffer_1by1_OPV.set(new GT_MetaTileEntity_BasicBatteryBuffer(11274, "batterybuffer.01.tier.14", "Overpowered Battery Buffer",14,"", 1).getStackForm(1L));
		ItemList.Battery_Buffer_1by1_MAXV.set(new GT_MetaTileEntity_BasicBatteryBuffer(11275, "batterybuffer.01.tier.15", "Maximum Battery Buffer",15,"", 1).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_1by1_ULV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_ULV, 'W', OrePrefixes.wireGt01.get(Materials.Lead), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_1by1_LV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_LV, 'W', OrePrefixes.wireGt01.get(Materials.Tin), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_1by1_MV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_MV, 'W', OrePrefixes.wireGt01.get(Materials.Copper), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_1by1_HV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_HV, 'W', OrePrefixes.wireGt01.get(Materials.Gold), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_1by1_EV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_EV, 'W', OrePrefixes.wireGt01.get(Materials.Aluminium), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_1by1_IV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_IV, 'W', OrePrefixes.wireGt01.get(Materials.Tungsten), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_1by1_LuV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_LuV, 'W', OrePrefixes.wireGt01.get(Materials.VanadiumGallium), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_1by1_ZPM.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_ZPM, 'W', OrePrefixes.wireGt01.get(Materials.Naquadah), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_1by1_UV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_UV, 'W', OrePrefixes.wireGt01.get(Materials.ElectrumFlux), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_1by1_MAX.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_MAX, 'W', OrePrefixes.wireGt01.get(Materials.Superconductor), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_1by1_UEV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_UEV, 'W', OrePrefixes.wireGt01.get(Materials.SuperconductorUEV), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_1by1_UIV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_UIV, 'W', OrePrefixes.wireGt01.get(Materials.Neutronium), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_1by1_UMV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_UMV, 'W', OrePrefixes.wireGt01.get(Materials.Quantium), 'T', OreDictNames.craftingChest});
        
        ItemList.Battery_Buffer_2by2_ULV.set(new GT_MetaTileEntity_BasicBatteryBuffer(170, "batterybuffer.04.tier.00", "Ultra Low Voltage Battery Buffer", 0, "", 4).getStackForm(1L));
        ItemList.Battery_Buffer_2by2_LV.set(new GT_MetaTileEntity_BasicBatteryBuffer(171, "batterybuffer.04.tier.01", "Low Voltage Battery Buffer", 1, "", 4).getStackForm(1L));
        ItemList.Battery_Buffer_2by2_MV.set(new GT_MetaTileEntity_BasicBatteryBuffer(172, "batterybuffer.04.tier.02", "Medium Voltage Battery Buffer", 2, "", 4).getStackForm(1L));
        ItemList.Battery_Buffer_2by2_HV.set(new GT_MetaTileEntity_BasicBatteryBuffer(173, "batterybuffer.04.tier.03", "High Voltage Battery Buffer", 3, "", 4).getStackForm(1L));
        ItemList.Battery_Buffer_2by2_EV.set(new GT_MetaTileEntity_BasicBatteryBuffer(174, "batterybuffer.04.tier.04", "Extreme Voltage Battery Buffer", 4, "", 4).getStackForm(1L));
        ItemList.Battery_Buffer_2by2_IV.set(new GT_MetaTileEntity_BasicBatteryBuffer(175, "batterybuffer.04.tier.05", "Insane Voltage Battery Buffer", 5, "", 4).getStackForm(1L));
        ItemList.Battery_Buffer_2by2_LuV.set(new GT_MetaTileEntity_BasicBatteryBuffer(176, "batterybuffer.04.tier.06", "Ludicrous Voltage Battery Buffer", 6, "", 4).getStackForm(1L));
        ItemList.Battery_Buffer_2by2_ZPM.set(new GT_MetaTileEntity_BasicBatteryBuffer(177, "batterybuffer.04.tier.07", "ZPM Voltage Battery Buffer", 7, "", 4).getStackForm(1L));
        ItemList.Battery_Buffer_2by2_UV.set(new GT_MetaTileEntity_BasicBatteryBuffer(178, "batterybuffer.04.tier.08", "Ultimate Voltage Battery Buffer", 8, "", 4).getStackForm(1L));
        ItemList.Battery_Buffer_2by2_MAX.set(new GT_MetaTileEntity_BasicBatteryBuffer(179, "batterybuffer.04.tier.09", "Ultimate High Voltage Battery Buffer", 9, "", 4).getStackForm(1L));
        ItemList.Battery_Buffer_2by2_UEV.set(new GT_MetaTileEntity_BasicBatteryBuffer(11260, "batterybuffer.04.tier.10", "Extremely Ultimate Battery Buffer",10,"", 4).getStackForm(1L));
        ItemList.Battery_Buffer_2by2_UIV.set(new GT_MetaTileEntity_BasicBatteryBuffer(11261, "batterybuffer.04.tier.11", "Insanely Ultimate Battery Buffer",11,"", 4).getStackForm(1L));
		ItemList.Battery_Buffer_2by2_UMV.set(new GT_MetaTileEntity_BasicBatteryBuffer(11262, "batterybuffer.04.tier.12", "Mega Ultimate Battery Buffer",12,"", 4).getStackForm(1L));
		ItemList.Battery_Buffer_2by2_UXV.set(new GT_MetaTileEntity_BasicBatteryBuffer(11263, "batterybuffer.04.tier.13", "Extended Mega Ultimate Battery Buffer",13,"", 4).getStackForm(1L));
		ItemList.Battery_Buffer_2by2_OPV.set(new GT_MetaTileEntity_BasicBatteryBuffer(11264, "batterybuffer.04.tier.14", "Overpowered Battery Buffer",14,"", 4).getStackForm(1L));
		ItemList.Battery_Buffer_2by2_MAXV.set(new GT_MetaTileEntity_BasicBatteryBuffer(11265, "batterybuffer.04.tier.15", "Maximum Battery Buffer",15,"", 4).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_2by2_ULV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_ULV, 'W', OrePrefixes.wireGt04.get(Materials.Lead), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_2by2_LV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_LV, 'W', OrePrefixes.wireGt04.get(Materials.Tin), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_2by2_MV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_MV, 'W', OrePrefixes.wireGt04.get(Materials.Copper), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_2by2_HV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_HV, 'W', OrePrefixes.wireGt04.get(Materials.Gold), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_2by2_EV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_EV, 'W', OrePrefixes.wireGt04.get(Materials.Aluminium), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_2by2_IV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_IV, 'W', OrePrefixes.wireGt04.get(Materials.Tungsten), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_2by2_LuV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_LuV, 'W', OrePrefixes.wireGt04.get(Materials.VanadiumGallium), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_2by2_ZPM.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_ZPM, 'W', OrePrefixes.wireGt04.get(Materials.Naquadah), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_2by2_UV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_UV, 'W', OrePrefixes.wireGt04.get(Materials.ElectrumFlux), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_2by2_MAX.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_MAX, 'W', OrePrefixes.wireGt04.get(Materials.Superconductor), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_2by2_UEV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_UEV, 'W', OrePrefixes.wireGt04.get(Materials.SuperconductorUEV), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_2by2_UIV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_UIV, 'W', OrePrefixes.wireGt04.get(Materials.Neutronium), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_2by2_UMV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_UMV, 'W', OrePrefixes.wireGt04.get(Materials.Quantium), 'T', OreDictNames.craftingChest});
        
        ItemList.Battery_Buffer_3by3_ULV.set(new GT_MetaTileEntity_BasicBatteryBuffer(180, "batterybuffer.09.tier.00", "Ultra Low Voltage Battery Buffer", 0, "", 9).getStackForm(1L));
        ItemList.Battery_Buffer_3by3_LV.set(new GT_MetaTileEntity_BasicBatteryBuffer(181, "batterybuffer.09.tier.01", "Low Voltage Battery Buffer", 1, "", 9).getStackForm(1L));
        ItemList.Battery_Buffer_3by3_MV.set(new GT_MetaTileEntity_BasicBatteryBuffer(182, "batterybuffer.09.tier.02", "Medium Voltage Battery Buffer", 2, "", 9).getStackForm(1L));
        ItemList.Battery_Buffer_3by3_HV.set(new GT_MetaTileEntity_BasicBatteryBuffer(183, "batterybuffer.09.tier.03", "High Voltage Battery Buffer", 3, "", 9).getStackForm(1L));
        ItemList.Battery_Buffer_3by3_EV.set(new GT_MetaTileEntity_BasicBatteryBuffer(184, "batterybuffer.09.tier.04", "Extreme Voltage Battery Buffer", 4, "", 9).getStackForm(1L));
        ItemList.Battery_Buffer_3by3_IV.set(new GT_MetaTileEntity_BasicBatteryBuffer(185, "batterybuffer.09.tier.05", "Insane Voltage Battery Buffer", 5, "", 9).getStackForm(1L));
        ItemList.Battery_Buffer_3by3_LuV.set(new GT_MetaTileEntity_BasicBatteryBuffer(186, "batterybuffer.09.tier.06", "Ludicrous Voltage Battery Buffer", 6, "", 9).getStackForm(1L));
        ItemList.Battery_Buffer_3by3_ZPM.set(new GT_MetaTileEntity_BasicBatteryBuffer(187, "batterybuffer.09.tier.07", "ZPM Voltage Battery Buffer", 7, "", 9).getStackForm(1L));
        ItemList.Battery_Buffer_3by3_UV.set(new GT_MetaTileEntity_BasicBatteryBuffer(188, "batterybuffer.09.tier.08", "Ultimate Voltage Battery Buffer", 8, "", 9).getStackForm(1L));
        ItemList.Battery_Buffer_3by3_MAX.set(new GT_MetaTileEntity_BasicBatteryBuffer(189, "batterybuffer.09.tier.09", "Ultimate High Voltage Battery Buffer", 9, "", 9).getStackForm(1L));
        ItemList.Battery_Buffer_3by3_UEV.set(new GT_MetaTileEntity_BasicBatteryBuffer(11250, "batterybuffer.09.tier.10", "Extremely Ultimate Battery Buffer",10,"", 9).getStackForm(1L));
        ItemList.Battery_Buffer_3by3_UIV.set(new GT_MetaTileEntity_BasicBatteryBuffer(11251, "batterybuffer.09.tier.11", "Insanely Ultimate Battery Buffer",11,"", 9).getStackForm(1L));
		ItemList.Battery_Buffer_3by3_UMV.set(new GT_MetaTileEntity_BasicBatteryBuffer(11252, "batterybuffer.09.tier.12", "Mega Ultimate Battery Buffer",12,"", 9).getStackForm(1L));
		ItemList.Battery_Buffer_3by3_UXV.set(new GT_MetaTileEntity_BasicBatteryBuffer(11253, "batterybuffer.09.tier.13", "Extended Mega Ultimate Battery Buffer",13,"", 9).getStackForm(1L));
		ItemList.Battery_Buffer_3by3_OPV.set(new GT_MetaTileEntity_BasicBatteryBuffer(11254, "batterybuffer.09.tier.14", "Overpowered Battery Buffer",14,"", 9).getStackForm(1L));
		ItemList.Battery_Buffer_3by3_MAXV.set(new GT_MetaTileEntity_BasicBatteryBuffer(11255, "batterybuffer.09.tier.15", "Maximum Battery Buffer",15,"", 9).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_3by3_ULV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_ULV, 'W', OrePrefixes.wireGt08.get(Materials.Lead), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_3by3_LV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_LV, 'W', OrePrefixes.wireGt08.get(Materials.Tin), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_3by3_MV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_MV, 'W', OrePrefixes.wireGt08.get(Materials.Copper), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_3by3_HV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_HV, 'W', OrePrefixes.wireGt08.get(Materials.Gold), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_3by3_EV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_EV, 'W', OrePrefixes.wireGt08.get(Materials.Aluminium), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_3by3_IV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_IV, 'W', OrePrefixes.wireGt08.get(Materials.Tungsten), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_3by3_LuV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_LuV, 'W', OrePrefixes.wireGt08.get(Materials.VanadiumGallium), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_3by3_ZPM.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_ZPM, 'W', OrePrefixes.wireGt08.get(Materials.Naquadah), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_3by3_UV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_UV, 'W', OrePrefixes.wireGt08.get(Materials.ElectrumFlux), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_3by3_MAX.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_MAX, 'W', OrePrefixes.wireGt08.get(Materials.Superconductor), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_3by3_UEV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_UEV, 'W', OrePrefixes.wireGt08.get(Materials.SuperconductorUEV), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_3by3_UIV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_UIV, 'W', OrePrefixes.wireGt08.get(Materials.Neutronium), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_3by3_UMV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_UMV, 'W', OrePrefixes.wireGt08.get(Materials.Quantium), 'T', OreDictNames.craftingChest});
        
        ItemList.Battery_Buffer_4by4_ULV.set(new GT_MetaTileEntity_BasicBatteryBuffer(190, "batterybuffer.16.tier.00", "Ultra Low Voltage Battery Buffer", 0, "", 16).getStackForm(1L));
        ItemList.Battery_Buffer_4by4_LV.set(new GT_MetaTileEntity_BasicBatteryBuffer(191, "batterybuffer.16.tier.01", "Low Voltage Battery Buffer", 1, "", 16).getStackForm(1L));
        ItemList.Battery_Buffer_4by4_MV.set(new GT_MetaTileEntity_BasicBatteryBuffer(192, "batterybuffer.16.tier.02", "Medium Voltage Battery Buffer", 2, "", 16).getStackForm(1L));
        ItemList.Battery_Buffer_4by4_HV.set(new GT_MetaTileEntity_BasicBatteryBuffer(193, "batterybuffer.16.tier.03", "High Voltage Battery Buffer", 3, "", 16).getStackForm(1L));
        ItemList.Battery_Buffer_4by4_EV.set(new GT_MetaTileEntity_BasicBatteryBuffer(194, "batterybuffer.16.tier.04", "Extreme Voltage Battery Buffer", 4, "", 16).getStackForm(1L));
        ItemList.Battery_Buffer_4by4_IV.set(new GT_MetaTileEntity_BasicBatteryBuffer(195, "batterybuffer.16.tier.05", "Insane Voltage Battery Buffer", 5, "", 16).getStackForm(1L));
        ItemList.Battery_Buffer_4by4_LuV.set(new GT_MetaTileEntity_BasicBatteryBuffer(196, "batterybuffer.16.tier.06", "Ludicrous Voltage Battery Buffer", 6, "", 16).getStackForm(1L));
        ItemList.Battery_Buffer_4by4_ZPM.set(new GT_MetaTileEntity_BasicBatteryBuffer(197, "batterybuffer.16.tier.07", "ZPM Voltage Battery Buffer", 7, "", 16).getStackForm(1L));
        ItemList.Battery_Buffer_4by4_UV.set(new GT_MetaTileEntity_BasicBatteryBuffer(198, "batterybuffer.16.tier.08", "Ultimate Voltage Battery Buffer", 8, "", 16).getStackForm(1L));
        ItemList.Battery_Buffer_4by4_MAX.set(new GT_MetaTileEntity_BasicBatteryBuffer(199, "batterybuffer.16.tier.09", "Ultimate High Voltage Battery Buffer", 9, "", 16).getStackForm(1L));
        ItemList.Battery_Buffer_4by4_UEV.set(new GT_MetaTileEntity_BasicBatteryBuffer(11240, "batterybuffer.16.tier.10", "Extremely Ultimate Battery Buffer",10,"", 16).getStackForm(1L));
        ItemList.Battery_Buffer_4by4_UIV.set(new GT_MetaTileEntity_BasicBatteryBuffer(11241, "batterybuffer.16.tier.11", "Insanely Ultimate Battery Buffer",11,"", 16).getStackForm(1L));
		ItemList.Battery_Buffer_4by4_UMV.set(new GT_MetaTileEntity_BasicBatteryBuffer(11242, "batterybuffer.16.tier.12", "Mega Ultimate Battery Buffer",12,"", 16).getStackForm(1L));
		ItemList.Battery_Buffer_4by4_UXV.set(new GT_MetaTileEntity_BasicBatteryBuffer(11243, "batterybuffer.16.tier.13", "Extended Mega Ultimate Battery Buffer",13,"", 16).getStackForm(1L));
		ItemList.Battery_Buffer_4by4_OPV.set(new GT_MetaTileEntity_BasicBatteryBuffer(11244, "batterybuffer.16.tier.14", "Overpowered Battery Buffer",14,"", 16).getStackForm(1L));
        ItemList.Battery_Buffer_4by4_MAXV.set(new GT_MetaTileEntity_BasicBatteryBuffer(11245, "batterybuffer.16.tier.15", "Maximum Battery Buffer",15,"", 16).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_4by4_ULV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_ULV, 'W', OrePrefixes.wireGt16.get(Materials.Lead), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_4by4_LV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_LV, 'W', OrePrefixes.wireGt16.get(Materials.Tin), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_4by4_MV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_MV, 'W', OrePrefixes.wireGt16.get(Materials.Copper), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_4by4_HV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_HV, 'W', OrePrefixes.wireGt16.get(Materials.Gold), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_4by4_EV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_EV, 'W', OrePrefixes.wireGt16.get(Materials.Aluminium), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_4by4_IV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_IV, 'W', OrePrefixes.wireGt16.get(Materials.Tungsten), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_4by4_LuV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_LuV, 'W', OrePrefixes.wireGt16.get(Materials.VanadiumGallium), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_4by4_ZPM.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_ZPM, 'W', OrePrefixes.wireGt16.get(Materials.Naquadah), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_4by4_UV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_UV, 'W', OrePrefixes.wireGt16.get(Materials.ElectrumFlux), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_4by4_MAX.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_MAX, 'W', OrePrefixes.wireGt16.get(Materials.Superconductor), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_4by4_UEV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_UEV, 'W', OrePrefixes.wireGt16.get(Materials.SuperconductorUEV), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_4by4_UIV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_UIV, 'W', OrePrefixes.wireGt16.get(Materials.Neutronium), 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Buffer_4by4_UMV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, 'M', ItemList.Hull_UMV, 'W', OrePrefixes.wireGt16.get(Materials.Quantium), 'T', OreDictNames.craftingChest});
        
        ItemList.Battery_Charger_4by4_ULV.set(new GT_MetaTileEntity_Charger(690, "batterycharger.16.tier.00", "Ultra Low Voltage Battery Charger", 0, "", 4).getStackForm(1L));
        ItemList.Battery_Charger_4by4_LV.set(new GT_MetaTileEntity_Charger(691, "batterycharger.16.tier.01", "Low Voltage Battery Charger", 1, "", 4).getStackForm(1L));
        ItemList.Battery_Charger_4by4_MV.set(new GT_MetaTileEntity_Charger(692, "batterycharger.16.tier.02", "Medium Voltage Battery Charger", 2, "", 4).getStackForm(1L));
        ItemList.Battery_Charger_4by4_HV.set(new GT_MetaTileEntity_Charger(693, "batterycharger.16.tier.03", "High Voltage Battery Charger", 3, "", 4).getStackForm(1L));
        ItemList.Battery_Charger_4by4_EV.set(new GT_MetaTileEntity_Charger(694, "batterycharger.16.tier.04", "Extreme Voltage Battery Charger", 4, "", 4).getStackForm(1L));
        ItemList.Battery_Charger_4by4_IV.set(new GT_MetaTileEntity_Charger(695, "batterycharger.16.tier.05", "Insane Voltage Battery Charger", 5, "", 4).getStackForm(1L));
        ItemList.Battery_Charger_4by4_LuV.set(new GT_MetaTileEntity_Charger(696, "batterycharger.16.tier.06", "Ludicrous Voltage Battery Charger", 6, "", 4).getStackForm(1L));
        ItemList.Battery_Charger_4by4_ZPM.set(new GT_MetaTileEntity_Charger(697, "batterycharger.16.tier.07", "ZPM Voltage Battery Charger", 7, "", 4).getStackForm(1L));
        ItemList.Battery_Charger_4by4_UV.set(new GT_MetaTileEntity_Charger(698, "batterycharger.16.tier.08", "Ultimate Voltage Battery Charger", 8, "", 4).getStackForm(1L));
        ItemList.Battery_Charger_4by4_MAX.set(new GT_MetaTileEntity_Charger(699, "batterycharger.16.tier.09", "Ultimate High Voltage Battery Charger", 9, "", 4).getStackForm(1L));
        ItemList.Battery_Charger_4by4_UEV.set(new GT_MetaTileEntity_Charger(11280, "batterycharger.16.tier.10", "Extremely Ultimate Battery Charger",10,"", 4).getStackForm(1L));
        ItemList.Battery_Charger_4by4_UIV.set(new GT_MetaTileEntity_Charger(11281, "batterycharger.16.tier.11", "Insanely Ultimate Battery Charger",11,"", 4).getStackForm(1L));
		ItemList.Battery_Charger_4by4_UMV.set(new GT_MetaTileEntity_Charger(11282, "batterycharger.16.tier.12", "Mega Ultimate Battery Charger",12,"", 4).getStackForm(1L));
		ItemList.Battery_Charger_4by4_UXV.set(new GT_MetaTileEntity_Charger(11283, "batterycharger.16.tier.13", "Extended Mega Ultimate Battery Charger",13,"", 4).getStackForm(1L));
		ItemList.Battery_Charger_4by4_OPV.set(new GT_MetaTileEntity_Charger(11284, "batterycharger.16.tier.14", "Overpowered Battery Charger",14,"", 4).getStackForm(1L));
		ItemList.Battery_Charger_4by4_MAXV.set(new GT_MetaTileEntity_Charger(11285, "batterycharger.16.tier.15", "Maximum Battery Charger",15,"", 4).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Charger_4by4_ULV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_ULV, 'W', OrePrefixes.wireGt16.get(Materials.Lead), 'T', OreDictNames.craftingChest, 'B', ItemList.Battery_RE_ULV_Tantalum, 'C', OrePrefixes.circuit.get(Materials.Primitive)});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Charger_4by4_LV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_LV, 'W', OrePrefixes.wireGt16.get(Materials.Tin), 'T', OreDictNames.craftingChest, 'B', ItemList.Battery_RE_LV_Lithium, 'C', OrePrefixes.circuit.get(Materials.Basic)});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Charger_4by4_MV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_MV, 'W', OrePrefixes.wireGt16.get(Materials.Copper), 'T', OreDictNames.craftingChest, 'B', ItemList.Battery_RE_MV_Lithium, 'C', OrePrefixes.circuit.get(Materials.Good)});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Charger_4by4_HV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_HV, 'W', OrePrefixes.wireGt16.get(Materials.Gold), 'T', OreDictNames.craftingChest, 'B', ItemList.Battery_RE_HV_Lithium, 'C', OrePrefixes.circuit.get(Materials.Advanced)});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Charger_4by4_EV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_EV, 'W', OrePrefixes.wireGt16.get(Materials.Aluminium), 'T', OreDictNames.craftingChest, 'B', OrePrefixes.battery.get(Materials.Master), 'C', OrePrefixes.circuit.get(Materials.Data)});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Charger_4by4_IV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_IV, 'W', OrePrefixes.wireGt16.get(Materials.Tungsten), 'T', OreDictNames.craftingChest, 'B', ItemList.Energy_LapotronicOrb, 'C', OrePrefixes.circuit.get(Materials.Elite)});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Charger_4by4_LuV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_LuV, 'W', OrePrefixes.wireGt16.get(Materials.VanadiumGallium), 'T', OreDictNames.craftingChest, 'B', ItemList.Energy_LapotronicOrb2, 'C', OrePrefixes.circuit.get(Materials.Master)});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Charger_4by4_ZPM.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_ZPM, 'W', OrePrefixes.wireGt16.get(Materials.Naquadah), 'T', OreDictNames.craftingChest, 'B', ItemList.Energy_Module, 'C', OrePrefixes.circuit.get(Materials.Ultimate)});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Charger_4by4_UV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_UV, 'W', OrePrefixes.wireGt16.get(Materials.ElectrumFlux), 'T', OreDictNames.craftingChest, 'B', ItemList.Energy_Cluster, 'C', OrePrefixes.circuit.get(Materials.Superconductor)});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Charger_4by4_MAX.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_MAX, 'W', OrePrefixes.wireGt16.get(Materials.Superconductor), 'T', OreDictNames.craftingChest, 'B', ItemList.ZPM2, 'C', OrePrefixes.circuit.get(Materials.Infinite)});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Charger_4by4_UEV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_UEV, 'W', OrePrefixes.wireGt16.get(Materials.SuperconductorUEV), 'T', OreDictNames.craftingChest, 'B', ItemList.ZPM2, 'C', OrePrefixes.circuit.get(Materials.Bio)});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Charger_4by4_UIV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_UIV, 'W', OrePrefixes.wireGt16.get(Materials.Neutronium), 'T', OreDictNames.craftingChest, 'B', ItemList.ZPM3, 'C', OrePrefixes.circuit.get(Materials.Bio)});
        GT_ModHandler.addCraftingRecipe(ItemList.Battery_Charger_4by4_UMV.get(1L, new Object[0]), bitsd, new Object[]{aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_UMV, 'W', OrePrefixes.wireGt16.get(Materials.Quantium), 'T', OreDictNames.craftingChest, 'B', ItemList.ZPM3, 'C', OrePrefixes.circuit.get(Materials.Bio)});
        
        GT_ModHandler.addCraftingRecipe(ItemList.Locker_ULV.get(1L, new Object[0]), bitsd, new Object[]{"T", "M", 'M', ItemList.Battery_Buffer_2by2_ULV, 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Locker_LV.get(1L, new Object[0]), bitsd, new Object[]{"T", "M", 'M', ItemList.Battery_Buffer_2by2_LV, 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Locker_MV.get(1L, new Object[0]), bitsd, new Object[]{"T", "M", 'M', ItemList.Battery_Buffer_2by2_MV, 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Locker_HV.get(1L, new Object[0]), bitsd, new Object[]{"T", "M", 'M', ItemList.Battery_Buffer_2by2_HV, 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Locker_EV.get(1L, new Object[0]), bitsd, new Object[]{"T", "M", 'M', ItemList.Battery_Buffer_2by2_EV, 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Locker_IV.get(1L, new Object[0]), bitsd, new Object[]{"T", "M", 'M', ItemList.Battery_Buffer_2by2_IV, 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Locker_LuV.get(1L, new Object[0]), bitsd, new Object[]{"T", "M", 'M', ItemList.Battery_Buffer_2by2_LuV, 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Locker_ZPM.get(1L, new Object[0]), bitsd, new Object[]{"T", "M", 'M', ItemList.Battery_Buffer_2by2_ZPM, 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Locker_UV.get(1L, new Object[0]), bitsd, new Object[]{"T", "M", 'M', ItemList.Battery_Buffer_2by2_UV, 'T', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Locker_MAX.get(1L, new Object[0]), bitsd, new Object[]{"T", "M", 'M', ItemList.Battery_Buffer_2by2_MAX, 'T', OreDictNames.craftingChest});
    }

    private static void run2() {
    	long bits = GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED;
    	long bitsd = GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED;
        ItemList.Machine_LV_AlloySmelter.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(201, "basicmachine.alloysmelter.tier.01", "Basic Alloy Smelter", 1, "HighTech combination Smelter", GT_Recipe.GT_Recipe_Map.sAlloySmelterRecipes, 2, 1, 0, 0, 1, "AlloySmelter.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(208)), aBoolConst_0, aBoolConst_0, 0, "ALLOY_SMELTER", new Object[]{"ECE", aTextCableHull, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE}).getStackForm(1L));
        ItemList.Machine_MV_AlloySmelter.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(202, "basicmachine.alloysmelter.tier.02", "Advanced Alloy Smelter", 2, "HighTech combination Smelter", GT_Recipe.GT_Recipe_Map.sAlloySmelterRecipes, 2, 1, 0, 0, 1, "AlloySmelter.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(208)), aBoolConst_0, aBoolConst_0, 0, "ALLOY_SMELTER", new Object[]{"ECE", aTextCableHull, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE}).getStackForm(1L));
        ItemList.Machine_HV_AlloySmelter.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(203, "basicmachine.alloysmelter.tier.03", "Advanced Alloy Smelter II", 3, "HighTech combination Smelter", GT_Recipe.GT_Recipe_Map.sAlloySmelterRecipes, 2, 1, 0, 0, 1, "AlloySmelter.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(208)), aBoolConst_0, aBoolConst_0, 0, "ALLOY_SMELTER", new Object[]{"ECE", aTextCableHull, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE}).getStackForm(1L));
        ItemList.Machine_EV_AlloySmelter.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(204, "basicmachine.alloysmelter.tier.04", "Advanced Alloy Smelter III", 4, "HighTech combination Smelter", GT_Recipe.GT_Recipe_Map.sAlloySmelterRecipes, 2, 1, 0, 0, 1, "AlloySmelter.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(208)), aBoolConst_0, aBoolConst_0, 0, "ALLOY_SMELTER", new Object[]{"ECE", aTextCableHull, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE}).getStackForm(1L));
        ItemList.Machine_IV_AlloySmelter.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(205, "basicmachine.alloysmelter.tier.05", "Advanced Alloy Smelter IV", 5, "HighTech combination Smelter", GT_Recipe.GT_Recipe_Map.sAlloySmelterRecipes, 2, 1, 0, 0, 1, "AlloySmelter.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(208)), aBoolConst_0, aBoolConst_0, 0, "ALLOY_SMELTER", new Object[]{"ECE", aTextCableHull, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE}).getStackForm(1L));
        ItemList.Machine_LuV_AlloySmelter.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(206, "basicmachine.alloysmelter.tier.06", "Advanced Alloy Smelter V", 6, "HighTech combination Smelter", GT_Recipe.GT_Recipe_Map.sAlloySmelterRecipes, 2, 1, 0, 0, 1, "AlloySmelter.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(208)), aBoolConst_0, aBoolConst_0, 0, "ALLOY_SMELTER", new Object[]{"ECE", aTextCableHull, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE}).getStackForm(1L));
        ItemList.Machine_ZPM_AlloySmelter.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(207, "basicmachine.alloysmelter.tier.07", "Advanced Alloy Smelter VI", 7, "HighTech combination Smelter", GT_Recipe.GT_Recipe_Map.sAlloySmelterRecipes, 2, 1, 0, 0, 1, "AlloySmelter.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(208)), aBoolConst_0, aBoolConst_0, 0, "ALLOY_SMELTER", new Object[]{"ECE", aTextCableHull, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE}).getStackForm(1L));
        ItemList.Machine_UV_AlloySmelter.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(208, "basicmachine.alloysmelter.tier.08", "Advanced Alloy Smelter VII", 8, "HighTech combination Smelter", GT_Recipe.GT_Recipe_Map.sAlloySmelterRecipes, 2, 1, 0, 0, 1, "AlloySmelter.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(208)), aBoolConst_0, aBoolConst_0, 0, "ALLOY_SMELTER", new Object[]{"ECE", aTextCableHull, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE}).getStackForm(1L));

        ItemList.Machine_LV_Assembler.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(211, "basicmachine.assembler.tier.01", "Basic Assembling Machine", 1, "Avengers, Assemble!", GT_Recipe.GT_Recipe_Map.sAssemblerRecipes, 6, 1, 16000, 0, 1, "Assembler.png", "", aBoolConst_0, aBoolConst_0, 0, "ASSEMBLER", new Object[]{"ACA", "VMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_MV_Assembler.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(212, "basicmachine.assembler.tier.02", "Advanced Assembling Machine", 2, "Avengers, Assemble!", GT_Recipe.GT_Recipe_Map.sAssemblerRecipes, 9, 1, 24000, 0, 1, "Assembler2.png", "", aBoolConst_0, aBoolConst_0, 0, "ASSEMBLER", new Object[]{"ACA", "VMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_HV_Assembler.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(213, "basicmachine.assembler.tier.03", "Advanced Assembling Machine II", 3, "Avengers, Assemble!", GT_Recipe.GT_Recipe_Map.sAssemblerRecipes, 9, 1, 32000, 0, 1, "Assembler2.png", "", aBoolConst_0, aBoolConst_0, 0, "ASSEMBLER", new Object[]{"ACA", "VMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_EV_Assembler.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(214, "basicmachine.assembler.tier.04", "Advanced Assembling Machine III", 4, "Avengers, Assemble!", GT_Recipe.GT_Recipe_Map.sAssemblerRecipes, 9, 1, 48000, 0, 1, "Assembler2.png", "", aBoolConst_0, aBoolConst_0, 0, "ASSEMBLER", new Object[]{"ACA", "VMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_IV_Assembler.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(215, "basicmachine.assembler.tier.05", "Advanced Assembling Machine IV", 5, "Avengers, Assemble!", GT_Recipe.GT_Recipe_Map.sAssemblerRecipes, 9, 1, 64000, 0, 1, "Assembler2.png", "", aBoolConst_0, aBoolConst_0, 0, "ASSEMBLER", new Object[]{"ACA", "VMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_LuV_Assembler.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(216, "basicmachine.assembler.tier.06", "Advanced Assembling Machine V", 6, "Avengers, Assemble!", GT_Recipe.GT_Recipe_Map.sAssemblerRecipes, 9, 1, 64000, 0, 1, "Assembler2.png", "", aBoolConst_0, aBoolConst_0, 0, "ASSEMBLER", new Object[]{"ACA", "VMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_ZPM_Assembler.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(217, "basicmachine.assembler.tier.07", "Advanced Assembling Machine VI", 7, "Avengers, Assemble!", GT_Recipe.GT_Recipe_Map.sAssemblerRecipes, 9, 1, 64000, 0, 1, "Assembler2.png", "", aBoolConst_0, aBoolConst_0, 0, "ASSEMBLER", new Object[]{"ACA", "VMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_UV_Assembler.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(218, "basicmachine.assembler.tier.08", "Advanced Assembling Machine VII", 8, "Avengers, Assemble!", GT_Recipe.GT_Recipe_Map.sAssemblerRecipes, 9, 1, 64000, 0, 1, "Assembler2.png", "", aBoolConst_0, aBoolConst_0, 0, "ASSEMBLER", new Object[]{"ACA", "VMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));

        ItemList.Machine_LV_Bender.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(221, "basicmachine.bender.tier.01", "Basic Bending Machine", 1, "Boo, he's bad! We want BENDER!!!", GT_Recipe.GT_Recipe_Map.sBenderRecipes, 2, 1, 0, 0, 1, "Bender.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(203)), aBoolConst_0, aBoolConst_0, 0, "BENDER", new Object[]{aTextPlateWrench, aTextCableHull, aTextMotorWire, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_MV_Bender.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(222, "basicmachine.bender.tier.02", "Advanced Bending Machine", 2, "Boo, he's bad! We want BENDER!!!", GT_Recipe.GT_Recipe_Map.sBenderRecipes, 2, 1, 0, 0, 1, "Bender.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(203)), aBoolConst_0, aBoolConst_0, 0, "BENDER", new Object[]{aTextPlateWrench, aTextCableHull, aTextMotorWire, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_HV_Bender.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(223, "basicmachine.bender.tier.03", "Advanced Bending Machine II", 3, "Boo, he's bad! We want BENDER!!!", GT_Recipe.GT_Recipe_Map.sBenderRecipes, 2, 1, 0, 0, 1, "Bender.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(203)), aBoolConst_0, aBoolConst_0, 0, "BENDER", new Object[]{aTextPlateWrench, aTextCableHull, aTextMotorWire, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_EV_Bender.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(224, "basicmachine.bender.tier.04", "Advanced Bending Machine III", 4, "Boo, he's bad! We want BENDER!!!", GT_Recipe.GT_Recipe_Map.sBenderRecipes, 2, 1, 0, 0, 1, "Bender.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(203)), aBoolConst_0, aBoolConst_0, 0, "BENDER", new Object[]{aTextPlateWrench, aTextCableHull, aTextMotorWire, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_IV_Bender.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(225, "basicmachine.bender.tier.05", "Advanced Bending Machine IV", 5, "Boo, he's bad! We want BENDER!!!", GT_Recipe.GT_Recipe_Map.sBenderRecipes, 2, 1, 0, 0, 1, "Bender.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(203)), aBoolConst_0, aBoolConst_0, 0, "BENDER", new Object[]{aTextPlateWrench, aTextCableHull, aTextMotorWire, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_LuV_Bender.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(226, "basicmachine.bender.tier.06", "Advanced Bending Machine V", 6, "Boo, he's bad! We want BENDER!!!", GT_Recipe.GT_Recipe_Map.sBenderRecipes, 2, 1, 0, 0, 1, "Bender.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(203)), aBoolConst_0, aBoolConst_0, 0, "BENDER", new Object[]{aTextPlateWrench, aTextCableHull, aTextMotorWire, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_ZPM_Bender.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(227, "basicmachine.bender.tier.07", "Advanced Bending Machine VI", 7, "Boo, he's bad! We want BENDER!!!", GT_Recipe.GT_Recipe_Map.sBenderRecipes, 2, 1, 0, 0, 1, "Bender.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(203)), aBoolConst_0, aBoolConst_0, 0, "BENDER", new Object[]{aTextPlateWrench, aTextCableHull, aTextMotorWire, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_UV_Bender.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(228, "basicmachine.bender.tier.08", "Advanced Bending Machine VII", 8, "Boo, he's bad! We want BENDER!!!", GT_Recipe.GT_Recipe_Map.sBenderRecipes, 2, 1, 0, 0, 1, "Bender.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(203)), aBoolConst_0, aBoolConst_0, 0, "BENDER", new Object[]{aTextPlateWrench, aTextCableHull, aTextMotorWire, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));

        ItemList.Machine_LV_Canner.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(231, "basicmachine.canner.tier.01", "Basic Canning Machine", 1, "Unmobile Food Canning Machine GTA4", GT_Recipe.GT_Recipe_Map.sCannerRecipes, 2, 2, 0, 0, 1, "Canner.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "CANNER", new Object[]{aTextWirePump, aTextCableHull, "GGG", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_MV_Canner.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(232, "basicmachine.canner.tier.02", "Advanced Canning Machine", 2, "Unmobile Food Canning Machine GTA4", GT_Recipe.GT_Recipe_Map.sCannerRecipes, 2, 2, 0, 0, 1, "Canner.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "CANNER", new Object[]{aTextWirePump, aTextCableHull, "GGG", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_HV_Canner.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(233, "basicmachine.canner.tier.03", "Advanced Canning Machine II", 3, "Unmobile Food Canning Machine GTA4", GT_Recipe.GT_Recipe_Map.sCannerRecipes, 2, 2, 0, 0, 1, "Canner.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "CANNER", new Object[]{aTextWirePump, aTextCableHull, "GGG", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_EV_Canner.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(234, "basicmachine.canner.tier.04", "Advanced Canning Machine III", 4, "Unmobile Food Canning Machine GTA4", GT_Recipe.GT_Recipe_Map.sCannerRecipes, 2, 2, 0, 0, 1, "Canner.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "CANNER", new Object[]{aTextWirePump, aTextCableHull, "GGG", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_IV_Canner.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(235, "basicmachine.canner.tier.05", "Advanced Canning Machine IV", 5, "Unmobile Food Canning Machine GTA4", GT_Recipe.GT_Recipe_Map.sCannerRecipes, 2, 2, 0, 0, 1, "Canner.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "CANNER", new Object[]{aTextWirePump, aTextCableHull, "GGG", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_LuV_Canner.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(236, "basicmachine.canner.tier.06", "Advanced Canning Machine V", 6, "Unmobile Food Canning Machine GTA4", GT_Recipe.GT_Recipe_Map.sCannerRecipes, 2, 2, 0, 0, 1, "Canner.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "CANNER", new Object[]{aTextWirePump, aTextCableHull, "GGG", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_ZPM_Canner.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(237, "basicmachine.canner.tier.07", "Advanced Canning Machine VI", 7, "Unmobile Food Canning Machine GTA4", GT_Recipe.GT_Recipe_Map.sCannerRecipes, 2, 2, 0, 0, 1, "Canner.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "CANNER", new Object[]{aTextWirePump, aTextCableHull, "GGG", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_UV_Canner.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(238, "basicmachine.canner.tier.08", "Advanced Canning Machine VII", 8, "Unmobile Food Canning Machine GTA4", GT_Recipe.GT_Recipe_Map.sCannerRecipes, 2, 2, 0, 0, 1, "Canner.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "CANNER", new Object[]{aTextWirePump, aTextCableHull, "GGG", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));

        ItemList.Machine_LV_Compressor.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(241, "basicmachine.compressor.tier.01", "Basic Compressor", 1, "Compress-O-Matic C77", GT_Recipe.GT_Recipe_Map.sCompressorRecipes, 1, 1, 0, 0, 1, "Compressor.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(203)), aBoolConst_0, aBoolConst_0, 0, "COMPRESSOR", new Object[]{" C ", aTextPlateMotor, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_MV_Compressor.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(242, "basicmachine.compressor.tier.02", "Advanced Compressor", 2, "Compress-O-Matic C77", GT_Recipe.GT_Recipe_Map.sCompressorRecipes, 1, 1, 0, 0, 1, "Compressor.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(203)), aBoolConst_0, aBoolConst_0, 0, "COMPRESSOR", new Object[]{" C ", aTextPlateMotor, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_HV_Compressor.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(243, "basicmachine.compressor.tier.03", "Advanced Compressor II", 3, "Compress-O-Matic C77", GT_Recipe.GT_Recipe_Map.sCompressorRecipes, 1, 1, 0, 0, 1, "Compressor.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(203)), aBoolConst_0, aBoolConst_0, 0, "COMPRESSOR", new Object[]{" C ", aTextPlateMotor, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_EV_Compressor.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(244, "basicmachine.compressor.tier.04", "Advanced Compressor III", 4, "Compress-O-Matic C77", GT_Recipe.GT_Recipe_Map.sCompressorRecipes, 1, 1, 0, 0, 1, "Compressor.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(203)), aBoolConst_0, aBoolConst_0, 0, "COMPRESSOR", new Object[]{" C ", aTextPlateMotor, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_IV_Compressor.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(245, "basicmachine.compressor.tier.05", "Singularity Compressor", 5, "Compress-O-Matic C77", GT_Recipe.GT_Recipe_Map.sCompressorRecipes, 1, 1, 0, 0, 1, "Compressor.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(203)), aBoolConst_0, aBoolConst_0, 0, "COMPRESSOR", new Object[]{" C ", aTextPlateMotor, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_LuV_Compressor.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(246, "basicmachine.compressor.tier.06", "Singularity Compressor", 6, "Compress-O-Matic C77", GT_Recipe.GT_Recipe_Map.sCompressorRecipes, 1, 1, 0, 0, 1, "Compressor.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(203)), aBoolConst_0, aBoolConst_0, 0, "COMPRESSOR", new Object[]{" C ", aTextPlateMotor, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_ZPM_Compressor.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(247, "basicmachine.compressor.tier.07", "Singularity Compressor", 7, "Compress-O-Matic C77", GT_Recipe.GT_Recipe_Map.sCompressorRecipes, 1, 1, 0, 0, 1, "Compressor.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(203)), aBoolConst_0, aBoolConst_0, 0, "COMPRESSOR", new Object[]{" C ", aTextPlateMotor, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_UV_Compressor.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(248, "basicmachine.compressor.tier.08", "Singularity Compressor", 8, "Compress-O-Matic C77", GT_Recipe.GT_Recipe_Map.sCompressorRecipes, 1, 1, 0, 0, 1, "Compressor.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(203)), aBoolConst_0, aBoolConst_0, 0, "COMPRESSOR", new Object[]{" C ", aTextPlateMotor, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));

        ItemList.Machine_LV_Cutter.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(251, "basicmachine.cutter.tier.01", "Basic Cutting Machine", 1, "Slice'N Dice", GT_Recipe.GT_Recipe_Map.sCutterRecipes, 1, 2, 1000, 0, 1, "Cutter.png", "", aBoolConst_0, aBoolConst_0, 0, "CUTTER", new Object[]{"WCG", "VMB", "CWE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS, 'B', OreDictNames.craftingDiamondBlade}).getStackForm(1L));
        ItemList.Machine_MV_Cutter.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(252, "basicmachine.cutter.tier.02", "Advanced Cutting Machine", 2, "Slice'N Dice", GT_Recipe.GT_Recipe_Map.sCutterRecipes, 1, 2, 1000, 0, 1, "Cutter.png", "", aBoolConst_0, aBoolConst_0, 0, "CUTTER", new Object[]{"WCG", "VMB", "CWE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS, 'B', OreDictNames.craftingDiamondBlade}).getStackForm(1L));
        ItemList.Machine_HV_Cutter.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(253, "basicmachine.cutter.tier.03", "Advanced Cutting Machine II", 3, "Slice'N Dice", GT_Recipe.GT_Recipe_Map.sCutterRecipes, 1, 2, 1000, 0, 1, "Cutter.png", "", aBoolConst_0, aBoolConst_0, 0, "CUTTER", new Object[]{"WCG", "VMB", "CWE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS, 'B', OreDictNames.craftingDiamondBlade}).getStackForm(1L));
        ItemList.Machine_EV_Cutter.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(254, "basicmachine.cutter.tier.04", "Advanced Cutting Machine III", 4, "Slice'N Dice", GT_Recipe.GT_Recipe_Map.sCutterRecipes, 1, 2, 1000, 0, 1, "Cutter.png", "", aBoolConst_0, aBoolConst_0, 0, "CUTTER", new Object[]{"WCG", "VMB", "CWE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS, 'B', OreDictNames.craftingDiamondBlade}).getStackForm(1L));
        ItemList.Machine_IV_Cutter.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(255, "basicmachine.cutter.tier.05", "Advanced Cutting Machine IV", 5, "Slice'N Dice", GT_Recipe.GT_Recipe_Map.sCutterRecipes, 1, 2, 1000, 0, 1, "Cutter.png", "", aBoolConst_0, aBoolConst_0, 0, "CUTTER", new Object[]{"WCG", "VMB", "CWE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS, 'B', OreDictNames.craftingDiamondBlade}).getStackForm(1L));
        ItemList.Machine_LuV_Cutter.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(256, "basicmachine.cutter.tier.06", "Advanced Cutting Machine V", 6, "Slice'N Dice", GT_Recipe.GT_Recipe_Map.sCutterRecipes, 1, 2, 1000, 0, 1, "Cutter.png", "", aBoolConst_0, aBoolConst_0, 0, "CUTTER", new Object[]{"WCG", "VMB", "CWE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS, 'B', OreDictNames.craftingDiamondBlade}).getStackForm(1L));
        ItemList.Machine_ZPM_Cutter.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(257, "basicmachine.cutter.tier.07", "Advanced Cutting Machine VI", 7, "Slice'N Dice", GT_Recipe.GT_Recipe_Map.sCutterRecipes, 1, 2, 1000, 0, 1, "Cutter.png", "", aBoolConst_0, aBoolConst_0, 0, "CUTTER", new Object[]{"WCG", "VMB", "CWE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS, 'B', OreDictNames.craftingDiamondBlade}).getStackForm(1L));
        ItemList.Machine_UV_Cutter.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(258, "basicmachine.cutter.tier.08", "Advanced Cutting Machine VII", 8, "Slice'N Dice", GT_Recipe.GT_Recipe_Map.sCutterRecipes, 1, 2, 1000, 0, 1, "Cutter.png", "", aBoolConst_0, aBoolConst_0, 0, "CUTTER", new Object[]{"WCG", "VMB", "CWE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS, 'B', OreDictNames.craftingDiamondBlade}).getStackForm(1L));

        ItemList.Machine_LV_E_Furnace.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(261, "basicmachine.e_furnace.tier.01", "Basic Electric Furnace", 1, "Not like using a Commodore 64", GT_Recipe.GT_Recipe_Map.sFurnaceRecipes, 1, 1, 0, 0, 1, "E_Furnace.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(207)), aBoolConst_0, aBoolConst_0, 0, "ELECTRIC_FURNACE", new Object[]{"ECE", aTextCableHull, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING}).getStackForm(1L));
        ItemList.Machine_MV_E_Furnace.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(262, "basicmachine.e_furnace.tier.02", "Advanced Electric Furnace", 2, "Not like using a Commodore 64", GT_Recipe.GT_Recipe_Map.sFurnaceRecipes, 1, 1, 0, 0, 1, "E_Furnace.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(207)), aBoolConst_0, aBoolConst_0, 0, "ELECTRIC_FURNACE", new Object[]{"ECE", aTextCableHull, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING}).getStackForm(1L));
        ItemList.Machine_HV_E_Furnace.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(263, "basicmachine.e_furnace.tier.03", "Advanced Electric Furnace II", 3, "Not like using a Commodore 64", GT_Recipe.GT_Recipe_Map.sFurnaceRecipes, 1, 1, 0, 0, 1, "E_Furnace.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(207)), aBoolConst_0, aBoolConst_0, 0, "ELECTRIC_FURNACE", new Object[]{"ECE", aTextCableHull, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING}).getStackForm(1L));
        ItemList.Machine_EV_E_Furnace.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(264, "basicmachine.e_furnace.tier.04", "Advanced Electric Furnace III", 4, "Not like using a Commodore 64", GT_Recipe.GT_Recipe_Map.sFurnaceRecipes, 1, 1, 0, 0, 1, "E_Furnace.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(207)), aBoolConst_0, aBoolConst_0, 0, "ELECTRIC_FURNACE", new Object[]{"ECE", aTextCableHull, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING}).getStackForm(1L));
        ItemList.Machine_IV_E_Furnace.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(265, "basicmachine.e_furnace.tier.05", "Electron Exitement Processor", 5, "Not like using a Commodore 64", GT_Recipe.GT_Recipe_Map.sFurnaceRecipes, 1, 1, 0, 0, 1, "E_Furnace.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(207)), aBoolConst_0, aBoolConst_0, 0, "ELECTRIC_FURNACE", new Object[]{"ECE", aTextCableHull, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING}).getStackForm(1L));
        ItemList.Machine_LuV_E_Furnace.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(266, "basicmachine.e_furnace.tier.06", "Electron Exitement Processor", 6, "Not like using a Commodore 64", GT_Recipe.GT_Recipe_Map.sFurnaceRecipes, 1, 1, 0, 0, 1, "E_Furnace.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(207)), aBoolConst_0, aBoolConst_0, 0, "ELECTRIC_FURNACE", new Object[]{"ECE", aTextCableHull, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING}).getStackForm(1L));
        ItemList.Machine_ZPM_E_Furnace.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(267, "basicmachine.e_furnace.tier.07", "Electron Exitement Processor", 7, "Not like using a Commodore 64", GT_Recipe.GT_Recipe_Map.sFurnaceRecipes, 1, 1, 0, 0, 1, "E_Furnace.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(207)), aBoolConst_0, aBoolConst_0, 0, "ELECTRIC_FURNACE", new Object[]{"ECE", aTextCableHull, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING}).getStackForm(1L));
        ItemList.Machine_UV_E_Furnace.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(268, "basicmachine.e_furnace.tier.08", "Electron Exitement Processor", 8, "Not like using a Commodore 64", GT_Recipe.GT_Recipe_Map.sFurnaceRecipes, 1, 1, 0, 0, 1, "E_Furnace.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(207)), aBoolConst_0, aBoolConst_0, 0, "ELECTRIC_FURNACE", new Object[]{"ECE", aTextCableHull, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING}).getStackForm(1L));

        ItemList.Machine_LV_Extractor.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(271, "basicmachine.extractor.tier.01", "Basic Extractor", 1, "Dejuicer-Device of Doom - D123", GT_Recipe.GT_Recipe_Map.sExtractorRecipes, 1, 1, 0, 0, 1, "Extractor.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "EXTRACTOR", new Object[]{"GCG", "EMP", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_MV_Extractor.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(272, "basicmachine.extractor.tier.02", "Advanced Extractor", 2, "Dejuicer-Device of Doom - D123", GT_Recipe.GT_Recipe_Map.sExtractorRecipes, 1, 1, 0, 0, 1, "Extractor.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "EXTRACTOR", new Object[]{"GCG", "EMP", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_HV_Extractor.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(273, "basicmachine.extractor.tier.03", "Advanced Extractor II", 3, "Dejuicer-Device of Doom - D123", GT_Recipe.GT_Recipe_Map.sExtractorRecipes, 1, 1, 0, 0, 1, "Extractor.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "EXTRACTOR", new Object[]{"GCG", "EMP", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_EV_Extractor.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(274, "basicmachine.extractor.tier.04", "Advanced Extractor III", 4, "Dejuicer-Device of Doom - D123", GT_Recipe.GT_Recipe_Map.sExtractorRecipes, 1, 1, 0, 0, 1, "Extractor.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "EXTRACTOR", new Object[]{"GCG", "EMP", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_IV_Extractor.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(275, "basicmachine.extractor.tier.05", "Vacuum Extractor", 5, "Dejuicer-Device of Doom - D123", GT_Recipe.GT_Recipe_Map.sExtractorRecipes, 1, 1, 0, 0, 1, "Extractor.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "EXTRACTOR", new Object[]{"GCG", "EMP", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_LuV_Extractor.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(276, "basicmachine.extractor.tier.06", "Vacuum Extractor", 6, "Dejuicer-Device of Doom - D123", GT_Recipe.GT_Recipe_Map.sExtractorRecipes, 1, 1, 0, 0, 1, "Extractor.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "EXTRACTOR", new Object[]{"GCG", "EMP", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_ZPM_Extractor.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(277, "basicmachine.extractor.tier.07", "Vacuum Extractor", 7, "Dejuicer-Device of Doom - D123", GT_Recipe.GT_Recipe_Map.sExtractorRecipes, 1, 1, 0, 0, 1, "Extractor.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "EXTRACTOR", new Object[]{"GCG", "EMP", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_UV_Extractor.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(278, "basicmachine.extractor.tier.08", "Vacuum Extractor", 8, "Dejuicer-Device of Doom - D123", GT_Recipe.GT_Recipe_Map.sExtractorRecipes, 1, 1, 0, 0, 1, "Extractor.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "EXTRACTOR", new Object[]{"GCG", "EMP", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));

        ItemList.Machine_LV_Extruder.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(281, "basicmachine.extruder.tier.01", "Basic Extruder", 1, "Universal Machine for Metal Working", GT_Recipe.GT_Recipe_Map.sExtruderRecipes, 2, 1, 0, 0, 1, "Extruder.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(208)), aBoolConst_0, aBoolConst_0, 0, "EXTRUDER", new Object[]{"CCE", "XMP", "CCE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'X', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PIPE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE}).getStackForm(1L));
        ItemList.Machine_MV_Extruder.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(282, "basicmachine.extruder.tier.02", "Advanced Extruder", 2, "Universal Machine for Metal Working", GT_Recipe.GT_Recipe_Map.sExtruderRecipes, 2, 1, 0, 0, 1, "Extruder.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(208)), aBoolConst_0, aBoolConst_0, 0, "EXTRUDER", new Object[]{"CCE", "XMP", "CCE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'X', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PIPE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE}).getStackForm(1L));
        ItemList.Machine_HV_Extruder.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(283, "basicmachine.extruder.tier.03", "Advanced Extruder II", 3, "Universal Machine for Metal Working", GT_Recipe.GT_Recipe_Map.sExtruderRecipes, 2, 1, 0, 0, 1, "Extruder.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(208)), aBoolConst_0, aBoolConst_0, 0, "EXTRUDER", new Object[]{"CCE", "XMP", "CCE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'X', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PIPE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE}).getStackForm(1L));
        ItemList.Machine_EV_Extruder.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(284, "basicmachine.extruder.tier.04", "Advanced Extruder III", 4, "Universal Machine for Metal Working", GT_Recipe.GT_Recipe_Map.sExtruderRecipes, 2, 1, 0, 0, 1, "Extruder.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(208)), aBoolConst_0, aBoolConst_0, 0, "EXTRUDER", new Object[]{"CCE", "XMP", "CCE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'X', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PIPE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE}).getStackForm(1L));
        ItemList.Machine_IV_Extruder.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(285, "basicmachine.extruder.tier.05", "Advanced Extruder IV", 5, "Universal Machine for Metal Working", GT_Recipe.GT_Recipe_Map.sExtruderRecipes, 2, 1, 0, 0, 1, "Extruder.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(208)), aBoolConst_0, aBoolConst_0, 0, "EXTRUDER", new Object[]{"CCE", "XMP", "CCE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'X', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PIPE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE}).getStackForm(1L));
        ItemList.Machine_LuV_Extruder.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(286, "basicmachine.extruder.tier.06", "Advanced Extruder V", 6, "Universal Machine for Metal Working", GT_Recipe.GT_Recipe_Map.sExtruderRecipes, 2, 1, 0, 0, 1, "Extruder.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(208)), aBoolConst_0, aBoolConst_0, 0, "EXTRUDER", new Object[]{"CCE", "XMP", "CCE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'X', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PIPE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE}).getStackForm(1L));
        ItemList.Machine_ZPM_Extruder.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(287, "basicmachine.extruder.tier.07", "Advanced Extruder VI", 7, "Universal Machine for Metal Working", GT_Recipe.GT_Recipe_Map.sExtruderRecipes, 2, 1, 0, 0, 1, "Extruder.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(208)), aBoolConst_0, aBoolConst_0, 0, "EXTRUDER", new Object[]{"CCE", "XMP", "CCE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'X', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PIPE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE}).getStackForm(1L));
        ItemList.Machine_UV_Extruder.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(288, "basicmachine.extruder.tier.08", "Advanced Extruder VII", 8, "Universal Machine for Metal Working", GT_Recipe.GT_Recipe_Map.sExtruderRecipes, 2, 1, 0, 0, 1, "Extruder.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(208)), aBoolConst_0, aBoolConst_0, 0, "EXTRUDER", new Object[]{"CCE", "XMP", "CCE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'X', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PIPE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE}).getStackForm(1L));

        ItemList.Machine_LV_Lathe.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(291, "basicmachine.lathe.tier.01", "Basic Lathe", 1, "Produces Rods more efficiently", GT_Recipe.GT_Recipe_Map.sLatheRecipes, 1, 2, 0, 0, 1, "Lathe.png", "", aBoolConst_0, aBoolConst_0, 0, "LATHE", new Object[]{aTextWireCoil, "EMD", "CWP", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'D', OrePrefixes.gem.get(Materials.Diamond)}).getStackForm(1L));
        ItemList.Machine_MV_Lathe.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(292, "basicmachine.lathe.tier.02", "Advanced Lathe", 2, "Produces Rods more efficiently", GT_Recipe.GT_Recipe_Map.sLatheRecipes, 1, 2, 0, 0, 1, "Lathe.png", "", aBoolConst_0, aBoolConst_0, 0, "LATHE", new Object[]{aTextWireCoil, "EMD", "CWP", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'D', OreDictNames.craftingIndustrialDiamond}).getStackForm(1L));
        ItemList.Machine_HV_Lathe.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(293, "basicmachine.lathe.tier.03", "Advanced Lathe II", 3, "Produces Rods more efficiently", GT_Recipe.GT_Recipe_Map.sLatheRecipes, 1, 2, 0, 0, 1, "Lathe.png", "", aBoolConst_0, aBoolConst_0, 0, "LATHE", new Object[]{aTextWireCoil, "EMD", "CWP", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'D', OreDictNames.craftingIndustrialDiamond}).getStackForm(1L));
        ItemList.Machine_EV_Lathe.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(294, "basicmachine.lathe.tier.04", "Advanced Lathe III", 4, "Produces Rods more efficiently", GT_Recipe.GT_Recipe_Map.sLatheRecipes, 1, 2, 0, 0, 1, "Lathe.png", "", aBoolConst_0, aBoolConst_0, 0, "LATHE", new Object[]{aTextWireCoil, "EMD", "CWP", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'D', OreDictNames.craftingIndustrialDiamond}).getStackForm(1L));
        ItemList.Machine_IV_Lathe.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(295, "basicmachine.lathe.tier.05", "Advanced Lathe IV", 5, "Produces Rods more efficiently", GT_Recipe.GT_Recipe_Map.sLatheRecipes, 1, 2, 0, 0, 1, "Lathe.png", "", aBoolConst_0, aBoolConst_0, 0, "LATHE", new Object[]{aTextWireCoil, "EMD", "CWP", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'D', OreDictNames.craftingIndustrialDiamond}).getStackForm(1L));
        ItemList.Machine_LuV_Lathe.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(296, "basicmachine.lathe.tier.06", "Advanced Lathe V", 6, "Produces Rods more efficiently", GT_Recipe.GT_Recipe_Map.sLatheRecipes, 1, 2, 0, 0, 1, "Lathe.png", "", aBoolConst_0, aBoolConst_0, 0, "LATHE", new Object[]{aTextWireCoil, "EMD", "CWP", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'D', OreDictNames.craftingIndustrialDiamond}).getStackForm(1L));
        ItemList.Machine_ZPM_Lathe.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(297, "basicmachine.lathe.tier.07", "Advanced Lathe VI", 7, "Produces Rods more efficiently", GT_Recipe.GT_Recipe_Map.sLatheRecipes, 1, 2, 0, 0, 1, "Lathe.png", "", aBoolConst_0, aBoolConst_0, 0, "LATHE", new Object[]{aTextWireCoil, "EMD", "CWP", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'D', OreDictNames.craftingIndustrialDiamond}).getStackForm(1L));
        ItemList.Machine_UV_Lathe.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(298, "basicmachine.lathe.tier.08", "Advanced Lathe VII", 8, "Produces Rods more efficiently", GT_Recipe.GT_Recipe_Map.sLatheRecipes, 1, 2, 0, 0, 1, "Lathe.png", "", aBoolConst_0, aBoolConst_0, 0, "LATHE", new Object[]{aTextWireCoil, "EMD", "CWP", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'D', OreDictNames.craftingIndustrialDiamond}).getStackForm(1L));

        ItemList.Machine_LV_Macerator.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(301, "basicmachine.macerator.tier.01", "Basic Macerator", 1, "Schreddering your Ores", GT_Recipe.GT_Recipe_Map.sMaceratorRecipes, 1, 1, 0, 0, 1, "Macerator1.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(201)), aBoolConst_0, aBoolConst_0, 1, "MACERATOR", new Object[]{"PEG", "WWM", "CCW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OrePrefixes.gem.get(Materials.Diamond)}).getStackForm(1L));
        ItemList.Machine_MV_Macerator.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(302, "basicmachine.macerator.tier.02", "Advanced Macerator", 2, "Schreddering your Ores", GT_Recipe.GT_Recipe_Map.sMaceratorRecipes, 1, 1, 0, 0, 1, "Macerator1.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(201)), aBoolConst_0, aBoolConst_0, 1, "MACERATOR", new Object[]{"PEG", "WWM", "CCW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OreDictNames.craftingIndustrialDiamond}).getStackForm(1L));
        ItemList.Machine_HV_Macerator.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(303, "basicmachine.macerator.tier.03", "Universal Macerator", 3, "Schreddering your Ores with Byproducts", GT_Recipe.GT_Recipe_Map.sMaceratorRecipes, 1, 2, 0, 0, 1, "Macerator2.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(201)), aBoolConst_0, aBoolConst_0, 1, "PULVERIZER", new Object[]{"PEG", "WWM", "CCW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OreDictNames.craftingGrinder}).getStackForm(1L));
        ItemList.Machine_EV_Macerator.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(304, "basicmachine.macerator.tier.04", "Universal Pulverizer", 4, "Schreddering your Ores with Byproducts", GT_Recipe.GT_Recipe_Map.sMaceratorRecipes, 1, 3, 0, 0, 1, "Macerator3.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(201)), aBoolConst_0, aBoolConst_0, 1, "PULVERIZER", new Object[]{"PEG", "WWM", "CCW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OreDictNames.craftingGrinder}).getStackForm(1L));
        ItemList.Machine_IV_Macerator.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(305, "basicmachine.macerator.tier.05", "Blend-O-Matic 9001", 5, "Schreddering your Ores with Byproducts", GT_Recipe.GT_Recipe_Map.sMaceratorRecipes, 1, 4, 0, 0, 1, "Macerator4.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(201)), aBoolConst_0, aBoolConst_0, 1, "PULVERIZER", new Object[]{"PEG", "WWM", "CCW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OreDictNames.craftingGrinder}).getStackForm(1L));
        ItemList.Machine_LuV_Macerator.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(306, "basicmachine.macerator.tier.06", "Blend-O-Matic 9001", 6, "Schreddering your Ores with Byproducts", GT_Recipe.GT_Recipe_Map.sMaceratorRecipes, 1, 4, 0, 0, 1, "Macerator4.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(201)), aBoolConst_0, aBoolConst_0, 1, "PULVERIZER", new Object[]{"PEG", "WWM", "CCW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OreDictNames.craftingGrinder}).getStackForm(1L));
        ItemList.Machine_ZPM_Macerator.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(307, "basicmachine.macerator.tier.07", "Blend-O-Matic 9001", 7, "Schreddering your Ores with Byproducts", GT_Recipe.GT_Recipe_Map.sMaceratorRecipes, 1, 4, 0, 0, 1, "Macerator4.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(201)), aBoolConst_0, aBoolConst_0, 1, "PULVERIZER", new Object[]{"PEG", "WWM", "CCW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OreDictNames.craftingGrinder}).getStackForm(1L));
        ItemList.Machine_UV_Macerator.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(308, "basicmachine.macerator.tier.08", "Blend-O-Matic 9001", 8, "Schreddering your Ores with Byproducts", GT_Recipe.GT_Recipe_Map.sMaceratorRecipes, 1, 4, 0, 0, 1, "Macerator4.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(201)), aBoolConst_0, aBoolConst_0, 1, "PULVERIZER", new Object[]{"PEG", "WWM", "CCW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OreDictNames.craftingGrinder}).getStackForm(1L));

        ItemList.Machine_LV_Microwave.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(311, "basicmachine.microwave.tier.01", "Basic Microwave", 1, "Did you really read the instruction Manual?", GT_Recipe.GT_Recipe_Map.sMicrowaveRecipes, 1, 1, 0, 0, 1, "E_Furnace.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(207)), aBoolConst_0, aBoolConst_0, 0, "MICROWAVE", new Object[]{"LWC", "LMR", "LEC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'L', OrePrefixes.plate.get(Materials.Lead)}).getStackForm(1L));
        ItemList.Machine_MV_Microwave.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(312, "basicmachine.microwave.tier.02", "Advanced Microwave", 2, "Did you really read the instruction Manual?", GT_Recipe.GT_Recipe_Map.sMicrowaveRecipes, 1, 1, 0, 0, 1, "E_Furnace.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(207)), aBoolConst_0, aBoolConst_0, 0, "MICROWAVE", new Object[]{"LWC", "LMR", "LEC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'L', OrePrefixes.plate.get(Materials.Lead)}).getStackForm(1L));
        ItemList.Machine_HV_Microwave.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(313, "basicmachine.microwave.tier.03", "Advanced Microwave II", 3, "Did you really read the instruction Manual?", GT_Recipe.GT_Recipe_Map.sMicrowaveRecipes, 1, 1, 0, 0, 1, "E_Furnace.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(207)), aBoolConst_0, aBoolConst_0, 0, "MICROWAVE", new Object[]{"LWC", "LMR", "LEC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'L', OrePrefixes.plate.get(Materials.Lead)}).getStackForm(1L));
        ItemList.Machine_EV_Microwave.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(314, "basicmachine.microwave.tier.04", "Advanced Microwave III", 4, "Did you really read the instruction Manual?", GT_Recipe.GT_Recipe_Map.sMicrowaveRecipes, 1, 1, 0, 0, 1, "E_Furnace.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(207)), aBoolConst_0, aBoolConst_0, 0, "MICROWAVE", new Object[]{"LWC", "LMR", "LEC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'L', OrePrefixes.plate.get(Materials.Lead)}).getStackForm(1L));
        ItemList.Machine_IV_Microwave.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(315, "basicmachine.microwave.tier.05", "Advanced Microwave IV", 5, "Did you really read the instruction Manual?", GT_Recipe.GT_Recipe_Map.sMicrowaveRecipes, 1, 1, 0, 0, 1, "E_Furnace.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(207)), aBoolConst_0, aBoolConst_0, 0, "MICROWAVE", new Object[]{"LWC", "LMR", "LEC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'L', OrePrefixes.plate.get(Materials.Lead)}).getStackForm(1L));
        ItemList.Machine_LuV_Microwave.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(316, "basicmachine.microwave.tier.06", "Advanced Microwave V", 6, "Did you really read the instruction Manual?", GT_Recipe.GT_Recipe_Map.sMicrowaveRecipes, 1, 1, 0, 0, 1, "E_Furnace.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(207)), aBoolConst_0, aBoolConst_0, 0, "MICROWAVE", new Object[]{"LWC", "LMR", "LEC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'L', OrePrefixes.plate.get(Materials.Lead)}).getStackForm(1L));
        ItemList.Machine_ZPM_Microwave.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(317, "basicmachine.microwave.tier.07", "Advanced Microwave VI", 7, "Did you really read the instruction Manual?", GT_Recipe.GT_Recipe_Map.sMicrowaveRecipes, 1, 1, 0, 0, 1, "E_Furnace.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(207)), aBoolConst_0, aBoolConst_0, 0, "MICROWAVE", new Object[]{"LWC", "LMR", "LEC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'L', OrePrefixes.plate.get(Materials.Lead)}).getStackForm(1L));
        ItemList.Machine_UV_Microwave.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(318, "basicmachine.microwave.tier.08", "Advanced Microwave VII", 8, "Did you really read the instruction Manual?", GT_Recipe.GT_Recipe_Map.sMicrowaveRecipes, 1, 1, 0, 0, 1, "E_Furnace.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(207)), aBoolConst_0, aBoolConst_0, 0, "MICROWAVE", new Object[]{"LWC", "LMR", "LEC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'L', OrePrefixes.plate.get(Materials.Lead)}).getStackForm(1L));

        ItemList.Machine_LV_Printer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(321, "basicmachine.printer.tier.01", "Basic Printer", 1, "It can copy Books and paint Stuff", GT_Recipe.GT_Recipe_Map.sPrinterRecipes, 1, 1, 16000, 0, 1, "Printer.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(203)), aBoolConst_0, aBoolConst_0, 1, "PRINTER", new Object[]{aTextMotorWire, aTextCableHull, "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_MV_Printer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(322, "basicmachine.printer.tier.02", "Advanced Printer", 2, "It can copy Books and paint Stuff", GT_Recipe.GT_Recipe_Map.sPrinterRecipes, 1, 1, 16000, 0, 1, "Printer.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(203)), aBoolConst_0, aBoolConst_0, 1, "PRINTER", new Object[]{aTextMotorWire, aTextCableHull, "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_HV_Printer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(323, "basicmachine.printer.tier.03", "Advanced Printer II", 3, "It can copy Books and paint Stuff", GT_Recipe.GT_Recipe_Map.sPrinterRecipes, 1, 1, 16000, 0, 1, "Printer.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(203)), aBoolConst_0, aBoolConst_0, 1, "PRINTER", new Object[]{aTextMotorWire, aTextCableHull, "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_EV_Printer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(324, "basicmachine.printer.tier.04", "Advanced Printer III", 4, "It can copy Books and paint Stuff", GT_Recipe.GT_Recipe_Map.sPrinterRecipes, 1, 1, 16000, 0, 1, "Printer.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(203)), aBoolConst_0, aBoolConst_0, 1, "PRINTER", new Object[]{aTextMotorWire, aTextCableHull, "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_IV_Printer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(325, "basicmachine.printer.tier.05", "Advanced Printer IV", 5, "It can copy Books and paint Stuff", GT_Recipe.GT_Recipe_Map.sPrinterRecipes, 1, 1, 16000, 0, 1, "Printer.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(203)), aBoolConst_0, aBoolConst_0, 1, "PRINTER", new Object[]{aTextMotorWire, aTextCableHull, "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_LuV_Printer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(326, "basicmachine.printer.tier.06", "Advanced Printer V", 6, "It can copy Books and paint Stuff", GT_Recipe.GT_Recipe_Map.sPrinterRecipes, 1, 1, 16000, 0, 1, "Printer.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(203)), aBoolConst_0, aBoolConst_0, 1, "PRINTER", new Object[]{aTextMotorWire, aTextCableHull, "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_ZPM_Printer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(327, "basicmachine.printer.tier.07", "Advanced Printer VI", 7, "It can copy Books and paint Stuff", GT_Recipe.GT_Recipe_Map.sPrinterRecipes, 1, 1, 16000, 0, 1, "Printer.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(203)), aBoolConst_0, aBoolConst_0, 1, "PRINTER", new Object[]{aTextMotorWire, aTextCableHull, "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_UV_Printer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(328, "basicmachine.printer.tier.08", "Advanced Printer VII", 8, "It can copy Books and paint Stuff", GT_Recipe.GT_Recipe_Map.sPrinterRecipes, 1, 1, 16000, 0, 1, "Printer.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(203)), aBoolConst_0, aBoolConst_0, 1, "PRINTER", new Object[]{aTextMotorWire, aTextCableHull, "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));

        ItemList.Machine_LV_Recycler.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(331, "basicmachine.recycler.tier.01", "Basic Recycler", 1, "Compress, burn, obliterate and filter EVERYTHING", GT_Recipe.GT_Recipe_Map.sRecyclerRecipes, 1, 1, 0, 0, 1, "Recycler.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(204)), aBoolConst_0, aBoolConst_0, 0, "RECYCLER", new Object[]{"GCG", aTextPlateMotor, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OrePrefixes.dust.get(Materials.Glowstone)}).getStackForm(1L));
        ItemList.Machine_MV_Recycler.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(332, "basicmachine.recycler.tier.02", "Advanced Recycler", 2, "Compress, burn, obliterate and filter EVERYTHING", GT_Recipe.GT_Recipe_Map.sRecyclerRecipes, 1, 1, 0, 0, 1, "Recycler.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(204)), aBoolConst_0, aBoolConst_0, 0, "RECYCLER", new Object[]{"GCG", aTextPlateMotor, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OrePrefixes.dust.get(Materials.Glowstone)}).getStackForm(1L));
        ItemList.Machine_HV_Recycler.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(333, "basicmachine.recycler.tier.03", "Advanced Recycler II", 3, "Compress, burn, obliterate and filter EVERYTHING", GT_Recipe.GT_Recipe_Map.sRecyclerRecipes, 1, 1, 0, 0, 1, "Recycler.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(204)), aBoolConst_0, aBoolConst_0, 0, "RECYCLER", new Object[]{"GCG", aTextPlateMotor, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OrePrefixes.dust.get(Materials.Glowstone)}).getStackForm(1L));
        ItemList.Machine_EV_Recycler.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(334, "basicmachine.recycler.tier.04", "Advanced Recycler III", 4, "Compress, burn, obliterate and filter EVERYTHING", GT_Recipe.GT_Recipe_Map.sRecyclerRecipes, 1, 1, 0, 0, 1, "Recycler.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(204)), aBoolConst_0, aBoolConst_0, 0, "RECYCLER", new Object[]{"GCG", aTextPlateMotor, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OrePrefixes.dust.get(Materials.Glowstone)}).getStackForm(1L));
        ItemList.Machine_IV_Recycler.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(335, "basicmachine.recycler.tier.05", "The Oblitterator", 5, "Compress, burn, obliterate and filter EVERYTHING", GT_Recipe.GT_Recipe_Map.sRecyclerRecipes, 1, 1, 0, 0, 1, "Recycler.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(204)), aBoolConst_0, aBoolConst_0, 0, "RECYCLER", new Object[]{"GCG", aTextPlateMotor, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OrePrefixes.dust.get(Materials.Glowstone)}).getStackForm(1L));
        ItemList.Machine_LuV_Recycler.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(336, "basicmachine.recycler.tier.06", "The Oblitterator", 6, "Compress, burn, obliterate and filter EVERYTHING", GT_Recipe.GT_Recipe_Map.sRecyclerRecipes, 1, 1, 0, 0, 1, "Recycler.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(204)), aBoolConst_0, aBoolConst_0, 0, "RECYCLER", new Object[]{"GCG", aTextPlateMotor, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OrePrefixes.dust.get(Materials.NetherStar)}).getStackForm(1L));
        ItemList.Machine_ZPM_Recycler.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(337, "basicmachine.recycler.tier.07", "The Oblitterator", 7, "Compress, burn, obliterate and filter EVERYTHING", GT_Recipe.GT_Recipe_Map.sRecyclerRecipes, 1, 1, 0, 0, 1, "Recycler.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(204)), aBoolConst_0, aBoolConst_0, 0, "RECYCLER", new Object[]{"GCG", aTextPlateMotor, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OrePrefixes.dust.get(Materials.NetherStar)}).getStackForm(1L));
        ItemList.Machine_UV_Recycler.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(338, "basicmachine.recycler.tier.08", "The Oblitterator", 8, "Compress, burn, obliterate and filter EVERYTHING", GT_Recipe.GT_Recipe_Map.sRecyclerRecipes, 1, 1, 0, 0, 1, "Recycler.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(204)), aBoolConst_0, aBoolConst_0, 0, "RECYCLER", new Object[]{"GCG", aTextPlateMotor, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OrePrefixes.dust.get(Materials.NetherStar)}).getStackForm(1L));

        ItemList.Machine_LV_Scanner.set(new GT_MetaTileEntity_Scanner(341, "basicmachine.scanner.tier.01", "Basic Scanner", 1).getStackForm(1L));
        ItemList.Machine_MV_Scanner.set(new GT_MetaTileEntity_Scanner(342, "basicmachine.scanner.tier.02", "Advanced Scanner", 2).getStackForm(1L));
        ItemList.Machine_HV_Scanner.set(new GT_MetaTileEntity_Scanner(343, "basicmachine.scanner.tier.03", "Advanced Scanner II", 3).getStackForm(1L));
        ItemList.Machine_EV_Scanner.set(new GT_MetaTileEntity_Scanner(344, "basicmachine.scanner.tier.04", "Advanced Scanner III", 4).getStackForm(1L));
        ItemList.Machine_IV_Scanner.set(new GT_MetaTileEntity_Scanner(345, "basicmachine.scanner.tier.05", "Advanced Scanner IV", 5).getStackForm(1L));
        ItemList.Machine_LuV_Scanner.set(new GT_MetaTileEntity_Scanner(346, "basicmachine.scanner.tier.06", "Advanced Scanner V", 6).getStackForm(1L));
        ItemList.Machine_ZPM_Scanner.set(new GT_MetaTileEntity_Scanner(347, "basicmachine.scanner.tier.07", "Advanced Scanner VI", 7).getStackForm(1L));
        ItemList.Machine_UV_Scanner.set(new GT_MetaTileEntity_Scanner(348, "basicmachine.scanner.tier.08", "Advanced Scanner VII", 8).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(ItemList.Machine_LV_Scanner.get(1L, new Object[0]), bitsd, new Object[]{"CTC", aTextWireHull, "CRC", 'M', ItemList.Hull_LV, 'T', ItemList.Emitter_LV, 'R', ItemList.Sensor_LV, 'C', OrePrefixes.circuit.get(Materials.Good), 'W', OrePrefixes.cableGt01.get(Materials.Tin)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_MV_Scanner.get(1L, new Object[0]), bitsd, new Object[]{"CTC", aTextWireHull, "CRC", 'M', ItemList.Hull_MV, 'T', ItemList.Emitter_MV, 'R', ItemList.Sensor_MV, 'C', OrePrefixes.circuit.get(Materials.Advanced), 'W', OrePrefixes.cableGt01.get(Materials.Copper)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_HV_Scanner.get(1L, new Object[0]), bitsd, new Object[]{"CTC", aTextWireHull, "CRC", 'M', ItemList.Hull_HV, 'T', ItemList.Emitter_HV, 'R', ItemList.Sensor_HV, 'C', OrePrefixes.circuit.get(Materials.Data), 'W', OrePrefixes.cableGt01.get(Materials.Gold)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_EV_Scanner.get(1L, new Object[0]), bitsd, new Object[]{"CTC", aTextWireHull, "CRC", 'M', ItemList.Hull_EV, 'T', ItemList.Emitter_EV, 'R', ItemList.Sensor_EV, 'C', OrePrefixes.circuit.get(Materials.Elite), 'W', OrePrefixes.cableGt01.get(Materials.Aluminium)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_IV_Scanner.get(1L, new Object[0]), bitsd, new Object[]{"CTC", aTextWireHull, "CRC", 'M', ItemList.Hull_IV, 'T', ItemList.Emitter_IV, 'R', ItemList.Sensor_IV, 'C', OrePrefixes.circuit.get(Materials.Master), 'W', OrePrefixes.cableGt01.get(Materials.Tungsten)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_LuV_Scanner.get(1L, new Object[0]), bitsd, new Object[]{"CTC", aTextWireHull, "CRC", 'M', ItemList.Hull_LuV, 'T', ItemList.Emitter_LuV, 'R', ItemList.Sensor_LuV, 'C', OrePrefixes.circuit.get(Materials.Ultimate), 'W', OrePrefixes.cableGt01.get(Materials.VanadiumGallium)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_ZPM_Scanner.get(1L, new Object[0]), bitsd, new Object[]{"CTC", aTextWireHull, "CRC", 'M', ItemList.Hull_ZPM, 'T', ItemList.Emitter_ZPM, 'R', ItemList.Sensor_ZPM, 'C', OrePrefixes.circuit.get(Materials.Superconductor), 'W', OrePrefixes.cableGt01.get(Materials.Naquadah)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_UV_Scanner.get(1L, new Object[0]), bitsd, new Object[]{"CTC", aTextWireHull, "CRC", 'M', ItemList.Hull_UV, 'T', ItemList.Emitter_UV, 'R', ItemList.Sensor_UV, 'C', OrePrefixes.circuit.get(Materials.Infinite), 'W', OrePrefixes.cableGt01.get(Materials.NaquadahAlloy)});

        ItemList.Machine_LV_Wiremill.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(351, "basicmachine.wiremill.tier.01", "Basic Wiremill", 1, "Produces Wires more efficiently", GT_Recipe.GT_Recipe_Map.sWiremillRecipes, 1, 1, 0, 0, 1, "Wiremill.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(204)), aBoolConst_0, aBoolConst_0, 0, "WIREMILL", new Object[]{aTextMotorWire, aTextCableHull, aTextMotorWire, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_MV_Wiremill.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(352, "basicmachine.wiremill.tier.02", "Advanced Wiremill", 2, "Produces Wires more efficiently", GT_Recipe.GT_Recipe_Map.sWiremillRecipes, 1, 1, 0, 0, 1, "Wiremill.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(204)), aBoolConst_0, aBoolConst_0, 0, "WIREMILL", new Object[]{aTextMotorWire, aTextCableHull, aTextMotorWire, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_HV_Wiremill.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(353, "basicmachine.wiremill.tier.03", "Advanced Wiremill II", 3, "Produces Wires more efficiently", GT_Recipe.GT_Recipe_Map.sWiremillRecipes, 1, 1, 0, 0, 1, "Wiremill.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(204)), aBoolConst_0, aBoolConst_0, 0, "WIREMILL", new Object[]{aTextMotorWire, aTextCableHull, aTextMotorWire, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_EV_Wiremill.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(354, "basicmachine.wiremill.tier.04", "Advanced Wiremill III", 4, "Produces Wires more efficiently", GT_Recipe.GT_Recipe_Map.sWiremillRecipes, 1, 1, 0, 0, 1, "Wiremill.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(204)), aBoolConst_0, aBoolConst_0, 0, "WIREMILL", new Object[]{aTextMotorWire, aTextCableHull, aTextMotorWire, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_IV_Wiremill.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(355, "basicmachine.wiremill.tier.05", "Advanced Wiremill IV", 5, "Produces Wires more efficiently", GT_Recipe.GT_Recipe_Map.sWiremillRecipes, 1, 1, 0, 0, 1, "Wiremill.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(204)), aBoolConst_0, aBoolConst_0, 0, "WIREMILL", new Object[]{aTextMotorWire, aTextCableHull, aTextMotorWire, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_LuV_Wiremill.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(356, "basicmachine.wiremill.tier.06", "Advanced Wiremill V", 6, "Produces Wires more efficiently", GT_Recipe.GT_Recipe_Map.sWiremillRecipes, 1, 1, 0, 0, 1, "Wiremill.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(204)), aBoolConst_0, aBoolConst_0, 0, "WIREMILL", new Object[]{aTextMotorWire, aTextCableHull, aTextMotorWire, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_ZPM_Wiremill.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(357, "basicmachine.wiremill.tier.07", "Advanced Wiremill VI", 7, "Produces Wires more efficiently", GT_Recipe.GT_Recipe_Map.sWiremillRecipes, 1, 1, 0, 0, 1, "Wiremill.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(204)), aBoolConst_0, aBoolConst_0, 0, "WIREMILL", new Object[]{aTextMotorWire, aTextCableHull, aTextMotorWire, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_UV_Wiremill.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(358, "basicmachine.wiremill.tier.08", "Advanced Wiremill VII", 8, "Produces Wires more efficiently", GT_Recipe.GT_Recipe_Map.sWiremillRecipes, 1, 1, 0, 0, 1, "Wiremill.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(204)), aBoolConst_0, aBoolConst_0, 0, "WIREMILL", new Object[]{aTextMotorWire, aTextCableHull, aTextMotorWire, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));

        ItemList.Machine_LV_Centrifuge.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(361, "basicmachine.centrifuge.tier.01", "Basic Centrifuge", 1, "Separating Molecules", GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes, 2, 6, 64000, 0, 1, "Centrifuge.png", "", aBoolConst_0, aBoolConst_0, 0, "CENTRIFUGE", new Object[]{"CEC", aTextWireHull, "CEC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_MV_Centrifuge.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(362, "basicmachine.centrifuge.tier.02", "Advanced Centrifuge", 2, "Separating Molecules", GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes, 2, 6, 64000, 0, 1, "Centrifuge.png", "", aBoolConst_0, aBoolConst_0, 0, "CENTRIFUGE", new Object[]{"CEC", aTextWireHull, "CEC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_HV_Centrifuge.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(363, "basicmachine.centrifuge.tier.03", "Turbo Centrifuge", 3, "Separating Molecules", GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes, 2, 6, 64000, 0, 1, "Centrifuge.png", "", aBoolConst_0, aBoolConst_0, 0, "CENTRIFUGE", new Object[]{"CEC", aTextWireHull, "CEC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_EV_Centrifuge.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(364, "basicmachine.centrifuge.tier.04", "Molecular Separator", 4, "Separating Molecules", GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes, 2, 6, 64000, 0, 1, "Centrifuge.png", "", aBoolConst_0, aBoolConst_0, 0, "CENTRIFUGE", new Object[]{"CEC", aTextWireHull, "CEC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_IV_Centrifuge.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(365, "basicmachine.centrifuge.tier.05", "Molecular Cyclone", 5, "Separating Molecules", GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes, 2, 6, 64000, 0, 1, "Centrifuge.png", "", aBoolConst_0, aBoolConst_0, 0, "CENTRIFUGE", new Object[]{"CEC", aTextWireHull, "CEC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_LuV_Centrifuge.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(366, "basicmachine.centrifuge.tier.06", "Molecular Cyclone", 6, "Separating Molecules", GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes, 2, 6, 64000, 0, 1, "Centrifuge.png", "", aBoolConst_0, aBoolConst_0, 0, "CENTRIFUGE", new Object[]{"CEC", aTextWireHull, "CEC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_ZPM_Centrifuge.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(367, "basicmachine.centrifuge.tier.07", "Molecular Cyclone", 7, "Separating Molecules", GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes, 2, 6, 64000, 0, 1, "Centrifuge.png", "", aBoolConst_0, aBoolConst_0, 0, "CENTRIFUGE", new Object[]{"CEC", aTextWireHull, "CEC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_UV_Centrifuge.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(368, "basicmachine.centrifuge.tier.08", "Molecular Cyclone", 8, "Separating Molecules", GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes, 2, 6, 64000, 0, 1, "Centrifuge.png", "", aBoolConst_0, aBoolConst_0, 0, "CENTRIFUGE", new Object[]{"CEC", aTextWireHull, "CEC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));

        ItemList.Machine_LV_Electrolyzer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(371, "basicmachine.electrolyzer.tier.01", "Basic Electrolyzer", 1, "Electrolyzing Molecules", GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes, 2, 6, 64000, 0, 1, "Electrolyzer.png", "", aBoolConst_0, aBoolConst_0, 0, "ELECTROLYZER", new Object[]{"IGI", "IMI", "CWC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I', OrePrefixes.wireGt01.get(Materials.Gold), 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_MV_Electrolyzer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(372, "basicmachine.electrolyzer.tier.02", "Advanced Electrolyzer", 2, "Electrolyzing Molecules", GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes, 2, 6, 64000, 0, 1, "Electrolyzer.png", "", aBoolConst_0, aBoolConst_0, 0, "ELECTROLYZER", new Object[]{"IGI", "IMI", "CWC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I', OrePrefixes.wireGt01.get(Materials.Silver), 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_HV_Electrolyzer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(373, "basicmachine.electrolyzer.tier.03", "Advanced Electrolyzer II", 3, "Electrolyzing Molecules", GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes, 2, 6, 64000, 0, 1, "Electrolyzer.png", "", aBoolConst_0, aBoolConst_0, 0, "ELECTROLYZER", new Object[]{"IGI", "IMI", "CWC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I', OrePrefixes.wireGt01.get(Materials.Electrum), 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_EV_Electrolyzer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(374, "basicmachine.electrolyzer.tier.04", "Advanced Electrolyzer III", 4, "Electrolyzing Molecules", GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes, 2, 6, 64000, 0, 1, "Electrolyzer.png", "", aBoolConst_0, aBoolConst_0, 0, "ELECTROLYZER", new Object[]{"IGI", "IMI", "CWC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I', OrePrefixes.wireGt01.get(Materials.Platinum), 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_IV_Electrolyzer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(375, "basicmachine.electrolyzer.tier.05", "Molecular Disintegrator E-4908", 5, "Electrolyzing Molecules", GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes, 2, 6, 64000, 0, 1, "Electrolyzer.png", "", aBoolConst_0, aBoolConst_0, 0, "ELECTROLYZER", new Object[]{"IGI", "IMI", "CWC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I', OrePrefixes.wireGt01.get(Materials.Osmium), 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_LuV_Electrolyzer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(376, "basicmachine.electrolyzer.tier.06", "Molecular Disintegrator E-4908", 6, "Electrolyzing Molecules", GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes, 2, 6, 64000, 0, 1, "Electrolyzer.png", "", aBoolConst_0, aBoolConst_0, 0, "ELECTROLYZER", new Object[]{"IGI", "IMI", "CWC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I', OrePrefixes.wireGt02.get(Materials.Osmium), 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_ZPM_Electrolyzer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(377, "basicmachine.electrolyzer.tier.07", "Molecular Disintegrator E-4908", 7, "Electrolyzing Molecules", GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes, 2, 6, 64000, 0, 1, "Electrolyzer.png", "", aBoolConst_0, aBoolConst_0, 0, "ELECTROLYZER", new Object[]{"IGI", "IMI", "CWC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I', OrePrefixes.wireGt02.get(Materials.NiobiumTitanium), 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_UV_Electrolyzer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(378, "basicmachine.electrolyzer.tier.08", "Molecular Disintegrator E-4908", 8, "Electrolyzing Molecules", GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes, 2, 6, 64000, 0, 1, "Electrolyzer.png", "", aBoolConst_0, aBoolConst_0, 0, "ELECTROLYZER", new Object[]{"IGI", "IMI", "CWC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I', OrePrefixes.wireGt04.get(Materials.VanadiumGallium), 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));

        ItemList.Machine_LV_ThermalCentrifuge.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(381, "basicmachine.thermalcentrifuge.tier.01", "Basic Thermal Centrifuge", 1, "Separating Ores more precisely", GT_Recipe.GT_Recipe_Map.sThermalCentrifugeRecipes, 1, 3, 0, 0, 1, "ThermalCentrifuge.png", "", aBoolConst_0, aBoolConst_0, 0, "THERMAL_CENTRIFUGE", new Object[]{"CEC", "OMO", "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE}).getStackForm(1L));
        ItemList.Machine_MV_ThermalCentrifuge.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(382, "basicmachine.thermalcentrifuge.tier.02", "Advanced Thermal Centrifuge", 2, "Separating Ores more precisely", GT_Recipe.GT_Recipe_Map.sThermalCentrifugeRecipes, 1, 3, 0, 0, 1, "ThermalCentrifuge.png", "", aBoolConst_0, aBoolConst_0, 0, "THERMAL_CENTRIFUGE", new Object[]{"CEC", "OMO", "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE}).getStackForm(1L));
        ItemList.Machine_HV_ThermalCentrifuge.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(383, "basicmachine.thermalcentrifuge.tier.03", "Advanced Thermal Centrifuge II", 3, "Separating Ores more precisely", GT_Recipe.GT_Recipe_Map.sThermalCentrifugeRecipes, 1, 3, 0, 0, 1, "ThermalCentrifuge.png", "", aBoolConst_0, aBoolConst_0, 0, "THERMAL_CENTRIFUGE", new Object[]{"CEC", "OMO", "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE}).getStackForm(1L));
        ItemList.Machine_EV_ThermalCentrifuge.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(384, "basicmachine.thermalcentrifuge.tier.04", "Advanced Thermal Centrifuge III", 4, "Separating Ores more precisely", GT_Recipe.GT_Recipe_Map.sThermalCentrifugeRecipes, 1, 3, 0, 0, 1, "ThermalCentrifuge.png", "", aBoolConst_0, aBoolConst_0, 0, "THERMAL_CENTRIFUGE", new Object[]{"CEC", "OMO", "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE}).getStackForm(1L));
        ItemList.Machine_IV_ThermalCentrifuge.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(385, "basicmachine.thermalcentrifuge.tier.05", "Blaze Sweatshop T-6350", 5, "Separating Ores more precisely", GT_Recipe.GT_Recipe_Map.sThermalCentrifugeRecipes, 1, 3, 0, 0, 1, "ThermalCentrifuge.png", "", aBoolConst_0, aBoolConst_0, 0, "THERMAL_CENTRIFUGE", new Object[]{"CEC", "OMO", "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE}).getStackForm(1L));
        ItemList.Machine_LuV_ThermalCentrifuge.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(386, "basicmachine.thermalcentrifuge.tier.06", "Blaze Sweatshop T-6350", 6, "Separating Ores more precisely", GT_Recipe.GT_Recipe_Map.sThermalCentrifugeRecipes, 1, 3, 0, 0, 1, "ThermalCentrifuge.png", "", aBoolConst_0, aBoolConst_0, 0, "THERMAL_CENTRIFUGE", new Object[]{"CEC", "OMO", "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE}).getStackForm(1L));
        ItemList.Machine_ZPM_ThermalCentrifuge.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(387, "basicmachine.thermalcentrifuge.tier.07", "Blaze Sweatshop T-6350", 7, "Separating Ores more precisely", GT_Recipe.GT_Recipe_Map.sThermalCentrifugeRecipes, 1, 3, 0, 0, 1, "ThermalCentrifuge.png", "", aBoolConst_0, aBoolConst_0, 0, "THERMAL_CENTRIFUGE", new Object[]{"CEC", "OMO", "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE}).getStackForm(1L));
        ItemList.Machine_UV_ThermalCentrifuge.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(388, "basicmachine.thermalcentrifuge.tier.08", "Blaze Sweatshop T-6350", 8, "Separating Ores more precisely", GT_Recipe.GT_Recipe_Map.sThermalCentrifugeRecipes, 1, 3, 0, 0, 1, "ThermalCentrifuge.png", "", aBoolConst_0, aBoolConst_0, 0, "THERMAL_CENTRIFUGE", new Object[]{"CEC", "OMO", "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE}).getStackForm(1L));

        ItemList.Machine_LV_OreWasher.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(391, "basicmachine.orewasher.tier.01", "Basic Ore Washing Plant", 1, "Getting more Byproducts from your Ores", GT_Recipe.GT_Recipe_Map.sOreWasherRecipes, 1, 3, 16000, 0, 1, "OreWasher.png", "", aBoolConst_0, aBoolConst_0, 0, "ORE_WASHER", new Object[]{"RGR", "CEC", aTextWireHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_MV_OreWasher.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(392, "basicmachine.orewasher.tier.02", "Advanced Ore Washing Plant", 2, "Getting more Byproducts from your Ores", GT_Recipe.GT_Recipe_Map.sOreWasherRecipes, 1, 3, 16000, 0, 1, "OreWasher.png", "", aBoolConst_0, aBoolConst_0, 0, "ORE_WASHER", new Object[]{"RGR", "CEC", aTextWireHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_HV_OreWasher.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(393, "basicmachine.orewasher.tier.03", "Advanced Ore Washing Plant II", 3, "Getting more Byproducts from your Ores", GT_Recipe.GT_Recipe_Map.sOreWasherRecipes, 1, 3, 16000, 0, 1, "OreWasher.png", "", aBoolConst_0, aBoolConst_0, 0, "ORE_WASHER", new Object[]{"RGR", "CEC", aTextWireHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_EV_OreWasher.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(394, "basicmachine.orewasher.tier.04", "Advanced Ore Washing Plant III", 4, "Getting more Byproducts from your Ores", GT_Recipe.GT_Recipe_Map.sOreWasherRecipes, 1, 3, 16000, 0, 1, "OreWasher.png", "", aBoolConst_0, aBoolConst_0, 0, "ORE_WASHER", new Object[]{"RGR", "CEC", aTextWireHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_IV_OreWasher.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(395, "basicmachine.orewasher.tier.05", "Repurposed Laundry-Washer I-360", 5, "Getting more Byproducts from your Ores", GT_Recipe.GT_Recipe_Map.sOreWasherRecipes, 1, 3, 16000, 0, 1, "OreWasher.png", "", aBoolConst_0, aBoolConst_0, 0, "ORE_WASHER", new Object[]{"RGR", "CEC", aTextWireHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_LuV_OreWasher.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(396, "basicmachine.orewasher.tier.06", "Repurposed Laundry-Washer I-360", 6, "Getting more Byproducts from your Ores", GT_Recipe.GT_Recipe_Map.sOreWasherRecipes, 1, 3, 16000, 0, 1, "OreWasher.png", "", aBoolConst_0, aBoolConst_0, 0, "ORE_WASHER", new Object[]{"RGR", "CEC", aTextWireHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_ZPM_OreWasher.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(397, "basicmachine.orewasher.tier.07", "Repurposed Laundry-Washer I-360", 7, "Getting more Byproducts from your Ores", GT_Recipe.GT_Recipe_Map.sOreWasherRecipes, 1, 3, 16000, 0, 1, "OreWasher.png", "", aBoolConst_0, aBoolConst_0, 0, "ORE_WASHER", new Object[]{"RGR", "CEC", aTextWireHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_UV_OreWasher.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(398, "basicmachine.orewasher.tier.08", "Repurposed Laundry-Washer I-360", 8, "Getting more Byproducts from your Ores", GT_Recipe.GT_Recipe_Map.sOreWasherRecipes, 1, 3, 16000, 0, 1, "OreWasher.png", "", aBoolConst_0, aBoolConst_0, 0, "ORE_WASHER", new Object[]{"RGR", "CEC", aTextWireHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));

        ItemList.Machine_LV_Boxinator.set(new GT_MetaTileEntity_Boxinator(401, "basicmachine.boxinator.tier.01", "Basic Packager", 1).getStackForm(1L));
        ItemList.Machine_MV_Boxinator.set(new GT_MetaTileEntity_Boxinator(402, "basicmachine.boxinator.tier.02", "Advanced Packager", 2).getStackForm(1L));
        ItemList.Machine_HV_Boxinator.set(new GT_MetaTileEntity_Boxinator(403, "basicmachine.boxinator.tier.03", "Advanced Packager II", 3).getStackForm(1L));
        ItemList.Machine_EV_Boxinator.set(new GT_MetaTileEntity_Boxinator(404, "basicmachine.boxinator.tier.04", "Advanced Packager III", 4).getStackForm(1L));
        ItemList.Machine_IV_Boxinator.set(new GT_MetaTileEntity_Boxinator(405, "basicmachine.boxinator.tier.05", "Boxinator", 5).getStackForm(1L));
        ItemList.Machine_LuV_Boxinator.set(new GT_MetaTileEntity_Boxinator(406, "basicmachine.boxinator.tier.06", "Boxinator", 6).getStackForm(1L));
        ItemList.Machine_ZPM_Boxinator.set(new GT_MetaTileEntity_Boxinator(407, "basicmachine.boxinator.tier.07", "Boxinator", 7).getStackForm(1L));
        ItemList.Machine_UV_Boxinator.set(new GT_MetaTileEntity_Boxinator(408, "basicmachine.boxinator.tier.08", "Boxinator", 8).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(ItemList.Machine_LV_Boxinator.get(1L, new Object[0]), bitsd, new Object[]{"BCB", "RMV", aTextWireCoil, 'M', ItemList.Hull_LV, 'R', ItemList.Robot_Arm_LV, 'V', ItemList.Conveyor_Module_LV, 'C', OrePrefixes.circuit.get(Materials.Basic), 'W', OrePrefixes.cableGt01.get(Materials.Tin), 'B', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_MV_Boxinator.get(1L, new Object[0]), bitsd, new Object[]{"BCB", "RMV", aTextWireCoil, 'M', ItemList.Hull_MV, 'R', ItemList.Robot_Arm_MV, 'V', ItemList.Conveyor_Module_MV, 'C', OrePrefixes.circuit.get(Materials.Good), 'W', OrePrefixes.cableGt01.get(Materials.Copper), 'B', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_HV_Boxinator.get(1L, new Object[0]), bitsd, new Object[]{"BCB", "RMV", aTextWireCoil, 'M', ItemList.Hull_HV, 'R', ItemList.Robot_Arm_HV, 'V', ItemList.Conveyor_Module_HV, 'C', OrePrefixes.circuit.get(Materials.Advanced), 'W', OrePrefixes.cableGt01.get(Materials.Gold), 'B', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_EV_Boxinator.get(1L, new Object[0]), bitsd, new Object[]{"BCB", "RMV", aTextWireCoil, 'M', ItemList.Hull_EV, 'R', ItemList.Robot_Arm_EV, 'V', ItemList.Conveyor_Module_EV, 'C', OrePrefixes.circuit.get(Materials.Data), 'W', OrePrefixes.cableGt01.get(Materials.Aluminium), 'B', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_IV_Boxinator.get(1L, new Object[0]), bitsd, new Object[]{"BCB", "RMV", aTextWireCoil, 'M', ItemList.Hull_IV, 'R', ItemList.Robot_Arm_IV, 'V', ItemList.Conveyor_Module_IV, 'C', OrePrefixes.circuit.get(Materials.Elite), 'W', OrePrefixes.cableGt01.get(Materials.Tungsten), 'B', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_LuV_Boxinator.get(1L, new Object[0]), bitsd, new Object[]{"BCB", "RMV", aTextWireCoil, 'M', ItemList.Hull_LuV, 'R', ItemList.Robot_Arm_LuV, 'V', ItemList.Conveyor_Module_LuV, 'C', OrePrefixes.circuit.get(Materials.Master), 'W', OrePrefixes.cableGt01.get(Materials.VanadiumGallium), 'B', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_ZPM_Boxinator.get(1L, new Object[0]), bitsd, new Object[]{"BCB", "RMV", aTextWireCoil, 'M', ItemList.Hull_ZPM, 'R', ItemList.Robot_Arm_ZPM, 'V', ItemList.Conveyor_Module_ZPM, 'C', OrePrefixes.circuit.get(Materials.Ultimate), 'W', OrePrefixes.cableGt01.get(Materials.Naquadah), 'B', OreDictNames.craftingChest});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_UV_Boxinator.get(1L, new Object[0]), bitsd, new Object[]{"BCB", "RMV", aTextWireCoil, 'M', ItemList.Hull_UV, 'R', ItemList.Robot_Arm_UV, 'V', ItemList.Conveyor_Module_UV, 'C', OrePrefixes.circuit.get(Materials.Superconductor), 'W', OrePrefixes.cableGt01.get(Materials.NaquadahAlloy), 'B', OreDictNames.craftingChest});

        ItemList.Machine_LV_Unboxinator.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(411, "basicmachine.unboxinator.tier.01", "Basic Unpackager", 1, "Grabs things out of Boxes", GT_Recipe.GT_Recipe_Map.sUnboxinatorRecipes, 1, 2, 0, 0, 1, "Unpackager.png", "", aBoolConst_0, aBoolConst_0, 0, "UNBOXINATOR", new Object[]{"BCB", "VMR", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'B', OreDictNames.craftingChest}).getStackForm(1L));
        ItemList.Machine_MV_Unboxinator.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(412, "basicmachine.unboxinator.tier.02", "Advanced Unpackager", 2, "Grabs things out of Boxes", GT_Recipe.GT_Recipe_Map.sUnboxinatorRecipes, 1, 2, 0, 0, 1, "Unpackager.png", "", aBoolConst_0, aBoolConst_0, 0, "UNBOXINATOR", new Object[]{"BCB", "VMR", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'B', OreDictNames.craftingChest}).getStackForm(1L));
        ItemList.Machine_HV_Unboxinator.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(413, "basicmachine.unboxinator.tier.03", "Advanced Unpackager II", 3, "Grabs things out of Boxes", GT_Recipe.GT_Recipe_Map.sUnboxinatorRecipes, 1, 2, 0, 0, 1, "Unpackager.png", "", aBoolConst_0, aBoolConst_0, 0, "UNBOXINATOR", new Object[]{"BCB", "VMR", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'B', OreDictNames.craftingChest}).getStackForm(1L));
        ItemList.Machine_EV_Unboxinator.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(414, "basicmachine.unboxinator.tier.04", "Advanced Unpackager III", 4, "Grabs things out of Boxes", GT_Recipe.GT_Recipe_Map.sUnboxinatorRecipes, 1, 2, 0, 0, 1, "Unpackager.png", "", aBoolConst_0, aBoolConst_0, 0, "UNBOXINATOR", new Object[]{"BCB", "VMR", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'B', OreDictNames.craftingChest}).getStackForm(1L));
        ItemList.Machine_IV_Unboxinator.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(415, "basicmachine.unboxinator.tier.05", "Unboxinator", 5, "Grabs things out of Boxes", GT_Recipe.GT_Recipe_Map.sUnboxinatorRecipes, 1, 2, 0, 0, 1, "Unpackager.png", "", aBoolConst_0, aBoolConst_0, 0, "UNBOXINATOR", new Object[]{"BCB", "VMR", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'B', OreDictNames.craftingChest}).getStackForm(1L));
        ItemList.Machine_LuV_Unboxinator.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(416, "basicmachine.unboxinator.tier.06", "Unboxinator", 6, "Grabs things out of Boxes", GT_Recipe.GT_Recipe_Map.sUnboxinatorRecipes, 1, 2, 0, 0, 1, "Unpackager.png", "", aBoolConst_0, aBoolConst_0, 0, "UNBOXINATOR", new Object[]{"BCB", "VMR", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'B', OreDictNames.craftingChest}).getStackForm(1L));
        ItemList.Machine_ZPM_Unboxinator.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(417, "basicmachine.unboxinator.tier.07", "Unboxinator", 7, "Grabs things out of Boxes", GT_Recipe.GT_Recipe_Map.sUnboxinatorRecipes, 1, 2, 0, 0, 1, "Unpackager.png", "", aBoolConst_0, aBoolConst_0, 0, "UNBOXINATOR", new Object[]{"BCB", "VMR", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'B', OreDictNames.craftingChest}).getStackForm(1L));
        ItemList.Machine_UV_Unboxinator.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(418, "basicmachine.unboxinator.tier.08", "Unboxinator", 8, "Grabs things out of Boxes", GT_Recipe.GT_Recipe_Map.sUnboxinatorRecipes, 1, 2, 0, 0, 1, "Unpackager.png", "", aBoolConst_0, aBoolConst_0, 0, "UNBOXINATOR", new Object[]{"BCB", "VMR", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'B', OreDictNames.craftingChest}).getStackForm(1L));

        ItemList.Machine_LV_ChemicalReactor.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(421, "basicmachine.chemicalreactor.tier.01", "Basic Chemical Reactor", 1, "Letting Chemicals react with each other", GT_Recipe.GT_Recipe_Map.sChemicalRecipes, 2, 2, 16000, 0, 1, "ChemicalReactor.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "CHEMICAL_REACTOR", new Object[]{"GRG", "WEW", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_MV_ChemicalReactor.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(422, "basicmachine.chemicalreactor.tier.02", "Advanced Chemical Reactor", 2, "Letting Chemicals react with each other", GT_Recipe.GT_Recipe_Map.sChemicalRecipes, 2, 2, 16000, 0, 1, "ChemicalReactor.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "CHEMICAL_REACTOR", new Object[]{"GRG", "WEW", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_HV_ChemicalReactor.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(423, "basicmachine.chemicalreactor.tier.03", "Advanced Chemical Reactor II", 3, "Letting Chemicals react with each other", GT_Recipe.GT_Recipe_Map.sChemicalRecipes, 2, 2, 16000, 0, 1, "ChemicalReactor.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "CHEMICAL_REACTOR", new Object[]{"GRG", "WEW", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OrePrefixes.pipeMedium.get(Materials.Plastic)}).getStackForm(1L));
        ItemList.Machine_EV_ChemicalReactor.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(424, "basicmachine.chemicalreactor.tier.04", "Advanced Chemical Reactor III", 4, "Letting Chemicals react with each other", GT_Recipe.GT_Recipe_Map.sChemicalRecipes, 2, 2, 16000, 0, 1, "ChemicalReactor.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "CHEMICAL_REACTOR", new Object[]{"GRG", "WEW", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OrePrefixes.pipeLarge.get(Materials.Plastic)}).getStackForm(1L));
        ItemList.Machine_IV_ChemicalReactor.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(425, "basicmachine.chemicalreactor.tier.05", "Advanced Chemical Reactor IV", 5, "Letting Chemicals react with each other", GT_Recipe.GT_Recipe_Map.sChemicalRecipes, 2, 2, 16000, 0, 1, "ChemicalReactor.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "CHEMICAL_REACTOR", new Object[]{"GRG", "WEW", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OrePrefixes.pipeHuge.get(Materials.Plastic)}).getStackForm(1L));
        ItemList.Machine_LuV_ChemicalReactor.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(426, "basicmachine.chemicalreactor.tier.06", "Advanced Chemical Reactor V", 6, "Letting Chemicals react with each other", GT_Recipe.GT_Recipe_Map.sChemicalRecipes, 2, 2, 16000, 0, 1, "ChemicalReactor.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "CHEMICAL_REACTOR", new Object[]{"GRG", "WEW", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OrePrefixes.pipeHuge.get(Materials.Plastic)}).getStackForm(1L));
        ItemList.Machine_ZPM_ChemicalReactor.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(427, "basicmachine.chemicalreactor.tier.07", "Advanced Chemical Reactor VI", 7, "Letting Chemicals react with each other", GT_Recipe.GT_Recipe_Map.sChemicalRecipes, 2, 2, 16000, 0, 1, "ChemicalReactor.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "CHEMICAL_REACTOR", new Object[]{"GRG", "WEW", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OrePrefixes.pipeHuge.get(Materials.Plastic)}).getStackForm(1L));
        ItemList.Machine_UV_ChemicalReactor.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(428, "basicmachine.chemicalreactor.tier.08", "Advanced Chemical Reactor VII", 8, "Letting Chemicals react with each other", GT_Recipe.GT_Recipe_Map.sChemicalRecipes, 2, 2, 16000, 0, 1, "ChemicalReactor.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "CHEMICAL_REACTOR", new Object[]{"GRG", "WEW", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OrePrefixes.pipeHuge.get(Materials.Plastic)}).getStackForm(1L));

        ItemList.Machine_LV_FluidCanner.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(431, "basicmachine.fluidcanner.tier.01", "Basic Fluid Canner", 1, "Puts Fluids into and out of Containers", GT_Recipe.GT_Recipe_Map.sFluidCannerRecipes, 1, 1, 16000, 0, 1, "FluidCanner.png", "", true, aBoolConst_0, 0, "FLUID_CANNER", new Object[]{"GCG", "GMG", "WPW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_MV_FluidCanner.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(432, "basicmachine.fluidcanner.tier.02", "Advanced Fluid Canner", 2, "Puts Fluids into and out of Containers", GT_Recipe.GT_Recipe_Map.sFluidCannerRecipes, 1, 1, 32000, 0, 1, "FluidCanner.png", "", true, aBoolConst_0, 0, "FLUID_CANNER", new Object[]{"GCG", "GMG", "WPW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_HV_FluidCanner.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(433, "basicmachine.fluidcanner.tier.03", "Quick Fluid Canner", 3, "Puts Fluids into and out of Containers", GT_Recipe.GT_Recipe_Map.sFluidCannerRecipes, 1, 1, 48000, 0, 1, "FluidCanner.png", "", true, aBoolConst_0, 0, "FLUID_CANNER", new Object[]{"GCG", "GMG", "WPW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_EV_FluidCanner.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(434, "basicmachine.fluidcanner.tier.04", "Turbo Fluid Canner", 4, "Puts Fluids into and out of Containers", GT_Recipe.GT_Recipe_Map.sFluidCannerRecipes, 1, 1, 64000, 0, 1, "FluidCanner.png", "", true, aBoolConst_0, 0, "FLUID_CANNER", new Object[]{"GCG", "GMG", "WPW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_IV_FluidCanner.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(435, "basicmachine.fluidcanner.tier.05", "Instant Fluid Canner", 5, "Puts Fluids into and out of Containers", GT_Recipe.GT_Recipe_Map.sFluidCannerRecipes, 1, 1, 80000, 0, 1, "FluidCanner.png", "", true, aBoolConst_0, 0, "FLUID_CANNER", new Object[]{"GCG", "GMG", "WPW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_LuV_FluidCanner.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(436, "basicmachine.fluidcanner.tier.06", "Instant Fluid Canner", 6, "Puts Fluids into and out of Containers", GT_Recipe.GT_Recipe_Map.sFluidCannerRecipes, 1, 1, 80000, 0, 1, "FluidCanner.png", "", true, aBoolConst_0, 0, "FLUID_CANNER", new Object[]{"GCG", "GMG", "WPW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_ZPM_FluidCanner.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(437, "basicmachine.fluidcanner.tier.07", "Instant Fluid Canner", 7, "Puts Fluids into and out of Containers", GT_Recipe.GT_Recipe_Map.sFluidCannerRecipes, 1, 1, 80000, 0, 1, "FluidCanner.png", "", true, aBoolConst_0, 0, "FLUID_CANNER", new Object[]{"GCG", "GMG", "WPW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_UV_FluidCanner.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(438, "basicmachine.fluidcanner.tier.08", "Instant Fluid Canner", 8, "Puts Fluids into and out of Containers", GT_Recipe.GT_Recipe_Map.sFluidCannerRecipes, 1, 1, 80000, 0, 1, "FluidCanner.png", "", true, aBoolConst_0, 0, "FLUID_CANNER", new Object[]{"GCG", "GMG", "WPW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));

        ItemList.Machine_LV_RockBreaker.set(new GT_MetaTileEntity_RockBreaker(441, "basicmachine.rockbreaker.tier.01", "Basic Rock Breaker", 1).getStackForm(1L));
        ItemList.Machine_MV_RockBreaker.set(new GT_MetaTileEntity_RockBreaker(442, "basicmachine.rockbreaker.tier.02", "Advanced Rock Breaker", 2).getStackForm(1L));
        ItemList.Machine_HV_RockBreaker.set(new GT_MetaTileEntity_RockBreaker(443, "basicmachine.rockbreaker.tier.03", "Advanced Rock Breaker II", 3).getStackForm(1L));
        ItemList.Machine_EV_RockBreaker.set(new GT_MetaTileEntity_RockBreaker(444, "basicmachine.rockbreaker.tier.04", "Advanced Rock Breaker III", 4).getStackForm(1L));
        ItemList.Machine_IV_RockBreaker.set(new GT_MetaTileEntity_RockBreaker(445, "basicmachine.rockbreaker.tier.05", "Cryogenic Magma Solidifier R-8200", 5).getStackForm(1L));
        ItemList.Machine_LuV_RockBreaker.set(new GT_MetaTileEntity_RockBreaker(446, "basicmachine.rockbreaker.tier.06", "Cryogenic Magma Solidifier R-8200", 6).getStackForm(1L));
        ItemList.Machine_ZPM_RockBreaker.set(new GT_MetaTileEntity_RockBreaker(447, "basicmachine.rockbreaker.tier.07", "Cryogenic Magma Solidifier R-8200", 7).getStackForm(1L));
        ItemList.Machine_UV_RockBreaker.set(new GT_MetaTileEntity_RockBreaker(448, "basicmachine.rockbreaker.tier.08", "Cryogenic Magma Solidifier R-8200", 8).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(ItemList.Machine_LV_RockBreaker.get(1L, new Object[0]), bitsd, new Object[]{"PED", aTextWireHull, "GGG", 'M', ItemList.Hull_LV, 'D', OreDictNames.craftingGrinder, 'E', ItemList.Electric_Motor_LV, 'P', ItemList.Electric_Piston_LV, 'C', OrePrefixes.circuit.get(Materials.Basic), 'W', OrePrefixes.cableGt01.get(Materials.Tin), 'G', new ItemStack(Blocks.glass, 1)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_MV_RockBreaker.get(1L, new Object[0]), bitsd, new Object[]{"PED", aTextWireHull, "GGG", 'M', ItemList.Hull_MV, 'D', OreDictNames.craftingGrinder, 'E', ItemList.Electric_Motor_MV, 'P', ItemList.Electric_Piston_MV, 'C', OrePrefixes.circuit.get(Materials.Good), 'W', OrePrefixes.cableGt01.get(Materials.Copper), 'G', new ItemStack(Blocks.glass, 1)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_HV_RockBreaker.get(1L, new Object[0]), bitsd, new Object[]{"PED", aTextWireHull, "GGG", 'M', ItemList.Hull_HV, 'D', OreDictNames.craftingGrinder, 'E', ItemList.Electric_Motor_HV, 'P', ItemList.Electric_Piston_HV, 'C', OrePrefixes.circuit.get(Materials.Advanced), 'W', OrePrefixes.cableGt01.get(Materials.Gold), 'G', new ItemStack(Blocks.glass, 1)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_EV_RockBreaker.get(1L, new Object[0]), bitsd, new Object[]{"PED", aTextWireHull, "GGG", 'M', ItemList.Hull_EV, 'D', OreDictNames.craftingGrinder, 'E', ItemList.Electric_Motor_EV, 'P', ItemList.Electric_Piston_EV, 'C', OrePrefixes.circuit.get(Materials.Data), 'W', OrePrefixes.cableGt01.get(Materials.Aluminium), 'G', new ItemStack(Blocks.glass, 1)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_IV_RockBreaker.get(1L, new Object[0]), bitsd, new Object[]{"PED", aTextWireHull, "GGG", 'M', ItemList.Hull_IV, 'D', OreDictNames.craftingGrinder, 'E', ItemList.Electric_Motor_IV, 'P', ItemList.Electric_Piston_IV, 'C', OrePrefixes.circuit.get(Materials.Elite), 'W', OrePrefixes.cableGt01.get(Materials.Tungsten), 'G', new ItemStack(Blocks.glass, 1)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_LuV_RockBreaker.get(1L, new Object[0]), bitsd, new Object[]{"PED", aTextWireHull, "GGG", 'M', ItemList.Hull_LuV, 'D', OreDictNames.craftingGrinder, 'E', ItemList.Electric_Motor_LuV, 'P', ItemList.Electric_Piston_LuV, 'C', OrePrefixes.circuit.get(Materials.Master), 'W', OrePrefixes.cableGt01.get(Materials.VanadiumGallium), 'G', Ic2Items.reinforcedGlass});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_ZPM_RockBreaker.get(1L, new Object[0]), bitsd, new Object[]{"PED", aTextWireHull, "GGG", 'M', ItemList.Hull_ZPM, 'D', OreDictNames.craftingGrinder, 'E', ItemList.Electric_Motor_ZPM, 'P', ItemList.Electric_Piston_ZPM, 'C', OrePrefixes.circuit.get(Materials.Ultimate), 'W', OrePrefixes.cableGt01.get(Materials.Naquadah), 'G', Ic2Items.reinforcedGlass});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_UV_RockBreaker.get(1L, new Object[0]), bitsd, new Object[]{"PED", aTextWireHull, "GGG", 'M', ItemList.Hull_UV, 'D', OreDictNames.craftingGrinder, 'E', ItemList.Electric_Motor_UV, 'P', ItemList.Electric_Piston_UV, 'C', OrePrefixes.circuit.get(Materials.Superconductor), 'W', OrePrefixes.cableGt01.get(Materials.NaquadahAlloy), 'G', Ic2Items.reinforcedGlass});

        ItemList.Machine_LV_Disassembler.set(new GT_MetaTileEntity_Disassembler(451, "basicmachine.disassembler.tier.01", "Basic Disassembler", 1).getStackForm(1L));
        ItemList.Machine_MV_Disassembler.set(new GT_MetaTileEntity_Disassembler(452, "basicmachine.disassembler.tier.02", "Advanced Disassembler", 2).getStackForm(1L));
        ItemList.Machine_HV_Disassembler.set(new GT_MetaTileEntity_Disassembler(453, "basicmachine.disassembler.tier.03", "Advanced Disassembler II", 3).getStackForm(1L));
        ItemList.Machine_EV_Disassembler.set(new GT_MetaTileEntity_Disassembler(454, "basicmachine.disassembler.tier.04", "Advanced Disassembler III", 4).getStackForm(1L));
        ItemList.Machine_IV_Disassembler.set(new GT_MetaTileEntity_Disassembler(455, "basicmachine.disassembler.tier.05", "Advanced Disassembler IV", 5).getStackForm(1L));
        ItemList.Machine_LuV_Disassembler.set(new GT_MetaTileEntity_Disassembler(456, "basicmachine.disassembler.tier.06", "Advanced Disassembler V", 6).getStackForm(1L));
        ItemList.Machine_ZPM_Disassembler.set(new GT_MetaTileEntity_Disassembler(457, "basicmachine.disassembler.tier.07", "Advanced Disassembler VI", 7).getStackForm(1L));
        ItemList.Machine_UV_Disassembler.set(new GT_MetaTileEntity_Disassembler(458, "basicmachine.disassembler.tier.08", "Advanced Disassembler VII", 8).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(ItemList.Machine_LV_Disassembler.get(1L, new Object[0]), bitsd, new Object[]{"ACA", aTextWireHull, "ACA", 'M', ItemList.Hull_LV, 'A', ItemList.Robot_Arm_LV, 'C', OrePrefixes.circuit.get(Materials.Basic), 'W', OrePrefixes.cableGt01.get(Materials.Tin)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_MV_Disassembler.get(1L, new Object[0]), bitsd, new Object[]{"ACA", aTextWireHull, "ACA", 'M', ItemList.Hull_MV, 'A', ItemList.Robot_Arm_MV, 'C', OrePrefixes.circuit.get(Materials.Good), 'W', OrePrefixes.cableGt01.get(Materials.Copper)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_HV_Disassembler.get(1L, new Object[0]), bitsd, new Object[]{"ACA", aTextWireHull, "ACA", 'M', ItemList.Hull_HV, 'A', ItemList.Robot_Arm_HV, 'C', OrePrefixes.circuit.get(Materials.Advanced), 'W', OrePrefixes.cableGt01.get(Materials.Gold)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_EV_Disassembler.get(1L, new Object[0]), bitsd, new Object[]{"ACA", aTextWireHull, "ACA", 'M', ItemList.Hull_EV, 'A', ItemList.Robot_Arm_EV, 'C', OrePrefixes.circuit.get(Materials.Data), 'W', OrePrefixes.cableGt01.get(Materials.Aluminium)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_IV_Disassembler.get(1L, new Object[0]), bitsd, new Object[]{"ACA", aTextWireHull, "ACA", 'M', ItemList.Hull_IV, 'A', ItemList.Robot_Arm_IV, 'C', OrePrefixes.circuit.get(Materials.Elite), 'W', OrePrefixes.cableGt01.get(Materials.Tungsten)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_LuV_Disassembler.get(1L, new Object[0]), bitsd, new Object[]{"ACA", aTextWireHull, "ACA", 'M', ItemList.Hull_LuV, 'A', ItemList.Robot_Arm_LuV, 'C', OrePrefixes.circuit.get(Materials.Master), 'W', OrePrefixes.cableGt01.get(Materials.VanadiumGallium)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_ZPM_Disassembler.get(1L, new Object[0]), bitsd, new Object[]{"ACA", aTextWireHull, "ACA", 'M', ItemList.Hull_ZPM, 'A', ItemList.Robot_Arm_ZPM, 'C', OrePrefixes.circuit.get(Materials.Ultimate), 'W', OrePrefixes.cableGt01.get(Materials.Naquadah)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_UV_Disassembler.get(1L, new Object[0]), bitsd, new Object[]{"ACA", aTextWireHull, "ACA", 'M', ItemList.Hull_UV, 'A', ItemList.Robot_Arm_UV, 'C', OrePrefixes.circuit.get(Materials.Superconductor), 'W', OrePrefixes.cableGt01.get(Materials.NaquadahAlloy)});

        ItemList.Machine_LV_Massfab.set(new GT_MetaTileEntity_Massfabricator(461, "basicmachine.massfab.tier.01", "Basic Mass Fabricator", 1).getStackForm(1L));
        ItemList.Machine_MV_Massfab.set(new GT_MetaTileEntity_Massfabricator(462, "basicmachine.massfab.tier.02", "Advanced Mass Fabricator", 2).getStackForm(1L));
        ItemList.Machine_HV_Massfab.set(new GT_MetaTileEntity_Massfabricator(463, "basicmachine.massfab.tier.03", "Advanced Mass Fabricator II", 3).getStackForm(1L));
        ItemList.Machine_EV_Massfab.set(new GT_MetaTileEntity_Massfabricator(464, "basicmachine.massfab.tier.04", "Advanced Mass Fabricator III", 4).getStackForm(1L));
        ItemList.Machine_IV_Massfab.set(new GT_MetaTileEntity_Massfabricator(465, "basicmachine.massfab.tier.05", "Advanced Mass Fabricator IV", 5).getStackForm(1L));
        ItemList.Machine_LuV_Massfab.set(new GT_MetaTileEntity_Massfabricator(466, "basicmachine.massfab.tier.06", "Advanced Mass Fabricator V", 6).getStackForm(1L));
        ItemList.Machine_ZPM_Massfab.set(new GT_MetaTileEntity_Massfabricator(467, "basicmachine.massfab.tier.07", "Advanced Mass Fabricator VI", 7).getStackForm(1L));
        ItemList.Machine_UV_Massfab.set(new GT_MetaTileEntity_Massfabricator(468, "basicmachine.massfab.tier.08", "Advanced Mass Fabricator VII", 8).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(ItemList.Machine_LV_Massfab.get(1L, new Object[0]), bitsd, new Object[]{"CFC", aTextWireHull, "CFC", 'M', ItemList.Hull_LV, 'F', ItemList.Field_Generator_LV, 'C', OrePrefixes.circuit.get(Materials.Good), 'W', OrePrefixes.cableGt04.get(Materials.Tin)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_MV_Massfab.get(1L, new Object[0]), bitsd, new Object[]{"CFC", aTextWireHull, "CFC", 'M', ItemList.Hull_MV, 'F', ItemList.Field_Generator_MV, 'C', OrePrefixes.circuit.get(Materials.Advanced), 'W', OrePrefixes.cableGt04.get(Materials.Copper)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_HV_Massfab.get(1L, new Object[0]), bitsd, new Object[]{"CFC", aTextWireHull, "CFC", 'M', ItemList.Hull_HV, 'F', ItemList.Field_Generator_HV, 'C', OrePrefixes.circuit.get(Materials.Data), 'W', OrePrefixes.cableGt04.get(Materials.Gold)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_EV_Massfab.get(1L, new Object[0]), bitsd, new Object[]{"CFC", aTextWireHull, "CFC", 'M', ItemList.Hull_EV, 'F', ItemList.Field_Generator_EV, 'C', OrePrefixes.circuit.get(Materials.Elite), 'W', OrePrefixes.cableGt04.get(Materials.Aluminium)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_IV_Massfab.get(1L, new Object[0]), bitsd, new Object[]{"CFC", aTextWireHull, "CFC", 'M', ItemList.Hull_IV, 'F', ItemList.Field_Generator_IV, 'C', OrePrefixes.circuit.get(Materials.Master), 'W', OrePrefixes.cableGt04.get(Materials.Tungsten)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_LuV_Massfab.get(1L, new Object[0]), bitsd, new Object[]{"CFC", aTextWireHull, "CFC", 'M', ItemList.Hull_LuV, 'F', ItemList.Field_Generator_LuV, 'C', OrePrefixes.circuit.get(Materials.Ultimate), 'W', OrePrefixes.cableGt04.get(Materials.VanadiumGallium)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_ZPM_Massfab.get(1L, new Object[0]), bitsd, new Object[]{"CFC", aTextWireHull, "CFC", 'M', ItemList.Hull_ZPM, 'F', ItemList.Field_Generator_ZPM, 'C', OrePrefixes.circuit.get(Materials.Superconductor), 'W', OrePrefixes.cableGt04.get(Materials.Naquadah)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_UV_Massfab.get(1L, new Object[0]), bitsd, new Object[]{"CFC", aTextWireHull, "CFC", 'M', ItemList.Hull_UV, 'F', ItemList.Field_Generator_UV, 'C', OrePrefixes.circuit.get(Materials.Infinite), 'W', OrePrefixes.cableGt04.get(Materials.NaquadahAlloy)});

        ItemList.Machine_LV_Amplifab.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(471, "basicmachine.amplifab.tier.01", "Basic Amplifabricator", 1, "Extracting UU Amplifier", GT_Recipe.GT_Recipe_Map.sAmplifiers, 1, 1, 1000, 0, 1, "Amplifabricator.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "AMPLIFAB", new Object[]{aTextWirePump, aTextPlateMotor, "CPC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4}).getStackForm(1L));
        ItemList.Machine_MV_Amplifab.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(472, "basicmachine.amplifab.tier.02", "Advanced Amplifabricator", 2, "Extracting UU Amplifier", GT_Recipe.GT_Recipe_Map.sAmplifiers, 1, 1, 1000, 0, 1, "Amplifabricator.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "AMPLIFAB", new Object[]{aTextWirePump, aTextPlateMotor, "CPC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4}).getStackForm(1L));
        ItemList.Machine_HV_Amplifab.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(473, "basicmachine.amplifab.tier.03", "Advanced Amplifabricator II", 3, "Extracting UU Amplifier", GT_Recipe.GT_Recipe_Map.sAmplifiers, 1, 1, 1000, 0, 1, "Amplifabricator.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "AMPLIFAB", new Object[]{aTextWirePump, aTextPlateMotor, "CPC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4}).getStackForm(1L));
        ItemList.Machine_EV_Amplifab.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(474, "basicmachine.amplifab.tier.04", "Advanced Amplifabricator III", 4, "Extracting UU Amplifier", GT_Recipe.GT_Recipe_Map.sAmplifiers, 1, 1, 1000, 0, 1, "Amplifabricator.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "AMPLIFAB", new Object[]{aTextWirePump, aTextPlateMotor, "CPC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4}).getStackForm(1L));
        ItemList.Machine_IV_Amplifab.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(475, "basicmachine.amplifab.tier.05", "Advanced Amplifabricator IV", 5, "Extracting UU Amplifier", GT_Recipe.GT_Recipe_Map.sAmplifiers, 1, 1, 1000, 0, 1, "Amplifabricator.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "AMPLIFAB", new Object[]{aTextWirePump, aTextPlateMotor, "CPC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4}).getStackForm(1L));
        ItemList.Machine_LuV_Amplifab.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(476, "basicmachine.amplifab.tier.06", "Advanced Amplifabricator IV", 6, "Extracting UU Amplifier", GT_Recipe.GT_Recipe_Map.sAmplifiers, 1, 1, 1000, 0, 1, "Amplifabricator.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "AMPLIFAB", new Object[]{aTextWirePump, aTextPlateMotor, "CPC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4}).getStackForm(1L));
        ItemList.Machine_ZPM_Amplifab.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(477, "basicmachine.amplifab.tier.07", "Advanced Amplifabricator IV", 7, "Extracting UU Amplifier", GT_Recipe.GT_Recipe_Map.sAmplifiers, 1, 1, 1000, 0, 1, "Amplifabricator.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "AMPLIFAB", new Object[]{aTextWirePump, aTextPlateMotor, "CPC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4}).getStackForm(1L));
        ItemList.Machine_UV_Amplifab.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(478, "basicmachine.amplifab.tier.08", "Advanced Amplifabricator IV", 8, "Extracting UU Amplifier", GT_Recipe.GT_Recipe_Map.sAmplifiers, 1, 1, 1000, 0, 1, "Amplifabricator.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "AMPLIFAB", new Object[]{aTextWirePump, aTextPlateMotor, "CPC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4}).getStackForm(1L));

        ItemList.Machine_LV_Replicator.set(new GT_MetaTileEntity_Replicator(481, "basicmachine.replicator.tier.01", "Basic Replicator", 1).getStackForm(1L));
        ItemList.Machine_MV_Replicator.set(new GT_MetaTileEntity_Replicator(482, "basicmachine.replicator.tier.02", "Advanced Replicator", 2).getStackForm(1L));
        ItemList.Machine_HV_Replicator.set(new GT_MetaTileEntity_Replicator(483, "basicmachine.replicator.tier.03", "Advanced Replicator II", 3).getStackForm(1L));
        ItemList.Machine_EV_Replicator.set(new GT_MetaTileEntity_Replicator(484, "basicmachine.replicator.tier.04", "Advanced Replicator III", 4).getStackForm(1L));
        ItemList.Machine_IV_Replicator.set(new GT_MetaTileEntity_Replicator(485, "basicmachine.replicator.tier.05", "Advanced Replicator IV", 5).getStackForm(1L));
        ItemList.Machine_LuV_Replicator.set(new GT_MetaTileEntity_Replicator(486, "basicmachine.replicator.tier.06", "Advanced Replicator V", 6).getStackForm(1L));
        ItemList.Machine_ZPM_Replicator.set(new GT_MetaTileEntity_Replicator(487, "basicmachine.replicator.tier.07", "Advanced Replicator VI", 7).getStackForm(1L));
        ItemList.Machine_UV_Replicator.set(new GT_MetaTileEntity_Replicator(488, "basicmachine.replicator.tier.08", "Advanced Replicator VII", 8).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(ItemList.Machine_LV_Replicator.get(1L, new Object[0]), bitsd, new Object[]{"EFE", aTextCableHull, aTextMotorWire, 'M', ItemList.Hull_LV, 'F', ItemList.Field_Generator_LV, 'E', ItemList.Emitter_LV, 'C', OrePrefixes.circuit.get(Materials.Good), 'W', OrePrefixes.cableGt04.get(Materials.Tin)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_MV_Replicator.get(1L, new Object[0]), bitsd, new Object[]{"EFE", aTextCableHull, aTextMotorWire, 'M', ItemList.Hull_MV, 'F', ItemList.Field_Generator_MV, 'E', ItemList.Emitter_MV, 'C', OrePrefixes.circuit.get(Materials.Advanced), 'W', OrePrefixes.cableGt04.get(Materials.Copper)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_HV_Replicator.get(1L, new Object[0]), bitsd, new Object[]{"EFE", aTextCableHull, aTextMotorWire, 'M', ItemList.Hull_HV, 'F', ItemList.Field_Generator_HV, 'E', ItemList.Emitter_HV, 'C', OrePrefixes.circuit.get(Materials.Data), 'W', OrePrefixes.cableGt04.get(Materials.Gold)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_EV_Replicator.get(1L, new Object[0]), bitsd, new Object[]{"EFE", aTextCableHull, aTextMotorWire, 'M', ItemList.Hull_EV, 'F', ItemList.Field_Generator_EV, 'E', ItemList.Emitter_EV, 'C', OrePrefixes.circuit.get(Materials.Elite), 'W', OrePrefixes.cableGt04.get(Materials.Aluminium)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_IV_Replicator.get(1L, new Object[0]), bitsd, new Object[]{"EFE", aTextCableHull, aTextMotorWire, 'M', ItemList.Hull_IV, 'F', ItemList.Field_Generator_IV, 'E', ItemList.Emitter_IV, 'C', OrePrefixes.circuit.get(Materials.Master), 'W', OrePrefixes.cableGt04.get(Materials.Tungsten)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_LuV_Replicator.get(1L, new Object[0]), bitsd, new Object[]{"EFE", aTextCableHull, aTextMotorWire, 'M', ItemList.Hull_LuV, 'F', ItemList.Field_Generator_LuV, 'E', ItemList.Emitter_LuV, 'C', OrePrefixes.circuit.get(Materials.Ultimate), 'W', OrePrefixes.cableGt04.get(Materials.VanadiumGallium)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_ZPM_Replicator.get(1L, new Object[0]), bitsd, new Object[]{"EFE", aTextCableHull, aTextMotorWire, 'M', ItemList.Hull_ZPM, 'F', ItemList.Field_Generator_ZPM, 'E', ItemList.Emitter_ZPM, 'C', OrePrefixes.circuit.get(Materials.Superconductor), 'W', OrePrefixes.cableGt04.get(Materials.Naquadah)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_UV_Replicator.get(1L, new Object[0]), bitsd, new Object[]{"EFE", aTextCableHull, aTextMotorWire, 'M', ItemList.Hull_UV, 'F', ItemList.Field_Generator_UV, 'E', ItemList.Emitter_UV, 'C', OrePrefixes.circuit.get(Materials.Infinite), 'W', OrePrefixes.cableGt04.get(Materials.NaquadahAlloy)});

        ItemList.Machine_LV_Brewery.set(new GT_MetaTileEntity_PotionBrewer(491, "basicmachine.brewery.tier.01", "Basic Brewery", 1).getStackForm(1L));
        ItemList.Machine_MV_Brewery.set(new GT_MetaTileEntity_PotionBrewer(492, "basicmachine.brewery.tier.02", "Advanced Brewery", 2).getStackForm(1L));
        ItemList.Machine_HV_Brewery.set(new GT_MetaTileEntity_PotionBrewer(493, "basicmachine.brewery.tier.03", "Advanced Brewery II", 3).getStackForm(1L));
        ItemList.Machine_EV_Brewery.set(new GT_MetaTileEntity_PotionBrewer(494, "basicmachine.brewery.tier.04", "Advanced Brewery III", 4).getStackForm(1L));
        ItemList.Machine_IV_Brewery.set(new GT_MetaTileEntity_PotionBrewer(495, "basicmachine.brewery.tier.05", "Advanced Brewery IV", 5).getStackForm(1L));
        ItemList.Machine_LuV_Brewery.set(new GT_MetaTileEntity_PotionBrewer(496, "basicmachine.brewery.tier.06", "Advanced Brewery V", 6).getStackForm(1L));
        ItemList.Machine_ZPM_Brewery.set(new GT_MetaTileEntity_PotionBrewer(497, "basicmachine.brewery.tier.07", "Advanced Brewery VI", 7).getStackForm(1L));
        ItemList.Machine_UV_Brewery.set(new GT_MetaTileEntity_PotionBrewer(498, "basicmachine.brewery.tier.08", "Advanced Brewery VII", 8).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(ItemList.Machine_LV_Brewery.get(1L, new Object[0]), bitsd, new Object[]{"GPG", aTextWireHull, "CBC", 'M', ItemList.Hull_LV, 'P', ItemList.Electric_Pump_LV, 'B', OrePrefixes.spring.get(Materials.Copper), 'C', OrePrefixes.circuit.get(Materials.Basic), 'W', OrePrefixes.cableGt01.get(Materials.Tin), 'G', new ItemStack(Blocks.glass, 1)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_MV_Brewery.get(1L, new Object[0]), bitsd, new Object[]{"GPG", aTextWireHull, "CBC", 'M', ItemList.Hull_MV, 'P', ItemList.Electric_Pump_MV, 'B', OrePrefixes.spring.get(Materials.Cupronickel), 'C', OrePrefixes.circuit.get(Materials.Good), 'W', OrePrefixes.cableGt01.get(Materials.Copper), 'G', new ItemStack(Blocks.glass, 1)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_HV_Brewery.get(1L, new Object[0]), bitsd, new Object[]{"GPG", aTextWireHull, "CBC", 'M', ItemList.Hull_HV, 'P', ItemList.Electric_Pump_HV, 'B', OrePrefixes.spring.get(Materials.Kanthal), 'C', OrePrefixes.circuit.get(Materials.Advanced), 'W', OrePrefixes.cableGt01.get(Materials.Gold), 'G', new ItemStack(Blocks.glass, 1)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_EV_Brewery.get(1L, new Object[0]), bitsd, new Object[]{"GPG", aTextWireHull, "CBC", 'M', ItemList.Hull_EV, 'P', ItemList.Electric_Pump_EV, 'B', OrePrefixes.spring.get(Materials.Nichrome), 'C', OrePrefixes.circuit.get(Materials.Data), 'W', OrePrefixes.cableGt01.get(Materials.Aluminium), 'G', new ItemStack(Blocks.glass, 1)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_IV_Brewery.get(1L, new Object[0]), bitsd, new Object[]{"GPG", aTextWireHull, "CBC", 'M', ItemList.Hull_IV, 'P', ItemList.Electric_Pump_IV, 'B', OrePrefixes.spring.get(Materials.TungstenSteel), 'C', OrePrefixes.circuit.get(Materials.Elite), 'W', OrePrefixes.cableGt01.get(Materials.Tungsten), 'G', new ItemStack(Blocks.glass, 1)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_LuV_Brewery.get(1L, new Object[0]), bitsd, new Object[]{"GPG", aTextWireHull, "CBC", 'M', ItemList.Hull_LuV, 'P', ItemList.Electric_Pump_LuV, 'B', OrePrefixes.spring.get(Materials.HSSG), 'C', OrePrefixes.circuit.get(Materials.Master), 'W', OrePrefixes.cableGt01.get(Materials.VanadiumGallium), 'G', Ic2Items.reinforcedGlass});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_ZPM_Brewery.get(1L, new Object[0]), bitsd, new Object[]{"GPG", aTextWireHull, "CBC", 'M', ItemList.Hull_ZPM, 'P', ItemList.Electric_Pump_ZPM, 'B', OrePrefixes.spring.get(Materials.Naquadah), 'C', OrePrefixes.circuit.get(Materials.Ultimate), 'W', OrePrefixes.cableGt01.get(Materials.Naquadah), 'G', Ic2Items.reinforcedGlass});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_UV_Brewery.get(1L, new Object[0]), bitsd, new Object[]{"GPG", aTextWireHull, "CBC", 'M', ItemList.Hull_UV, 'P', ItemList.Electric_Pump_UV, 'B', OrePrefixes.spring.get(Materials.NaquadahAlloy), 'C', OrePrefixes.circuit.get(Materials.Superconductor), 'W', OrePrefixes.cableGt01.get(Materials.NaquadahAlloy), 'G', Ic2Items.reinforcedGlass});
        
        ItemList.Machine_LV_Fermenter.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(501, "basicmachine.fermenter.tier.01", "Basic Fermenter", 1, "Fermenting Fluids", GT_Recipe.GT_Recipe_Map.sFermentingRecipes, 1, 1, 1000, 0, 1, "Fermenter.png", "", aBoolConst_0, aBoolConst_0, 0, "FERMENTER", new Object[]{aTextWirePump, "GMG", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_MV_Fermenter.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(502, "basicmachine.fermenter.tier.02", "Advanced Fermenter", 2, "Fermenting Fluids", GT_Recipe.GT_Recipe_Map.sFermentingRecipes, 1, 1, 1000, 0, 1, "Fermenter.png", "", aBoolConst_0, aBoolConst_0, 0, "FERMENTER", new Object[]{aTextWirePump, "GMG", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_HV_Fermenter.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(503, "basicmachine.fermenter.tier.03", "Advanced Fermenter II", 3, "Fermenting Fluids", GT_Recipe.GT_Recipe_Map.sFermentingRecipes, 1, 1, 1000, 0, 1, "Fermenter.png", "", aBoolConst_0, aBoolConst_0, 0, "FERMENTER", new Object[]{aTextWirePump, "GMG", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_EV_Fermenter.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(504, "basicmachine.fermenter.tier.04", "Advanced Fermenter III", 4, "Fermenting Fluids", GT_Recipe.GT_Recipe_Map.sFermentingRecipes, 1, 1, 1000, 0, 1, "Fermenter.png", "", aBoolConst_0, aBoolConst_0, 0, "FERMENTER", new Object[]{aTextWirePump, "GMG", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_IV_Fermenter.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(505, "basicmachine.fermenter.tier.05", "Advanced Fermenter IV", 5, "Fermenting Fluids", GT_Recipe.GT_Recipe_Map.sFermentingRecipes, 1, 1, 1000, 0, 1, "Fermenter.png", "", aBoolConst_0, aBoolConst_0, 0, "FERMENTER", new Object[]{aTextWirePump, "GMG", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_LuV_Fermenter.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(506, "basicmachine.fermenter.tier.06", "Advanced Fermenter V", 6, "Fermenting Fluids", GT_Recipe.GT_Recipe_Map.sFermentingRecipes, 1, 1, 1000, 0, 1, "Fermenter.png", "", aBoolConst_0, aBoolConst_0, 0, "FERMENTER", new Object[]{aTextWirePump, "GMG", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_ZPM_Fermenter.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(507, "basicmachine.fermenter.tier.07", "Advanced Fermenter VI", 7, "Fermenting Fluids", GT_Recipe.GT_Recipe_Map.sFermentingRecipes, 1, 1, 1000, 0, 1, "Fermenter.png", "", aBoolConst_0, aBoolConst_0, 0, "FERMENTER", new Object[]{aTextWirePump, "GMG", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_UV_Fermenter.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(508, "basicmachine.fermenter.tier.08", "Advanced Fermenter VII", 8, "Fermenting Fluids", GT_Recipe.GT_Recipe_Map.sFermentingRecipes, 1, 1, 1000, 0, 1, "Fermenter.png", "", aBoolConst_0, aBoolConst_0, 0, "FERMENTER", new Object[]{aTextWirePump, "GMG", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));

        ItemList.Machine_LV_FluidExtractor.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(511, "basicmachine.fluidextractor.tier.01", "Basic Fluid Extractor", 1, "Extracting Fluids from Items", GT_Recipe.GT_Recipe_Map.sFluidExtractionRecipes, 1, 1, 16000, 0, 1, "FluidExtractor.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "FLUID_EXTRACTOR", new Object[]{"GCG", "PME", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_MV_FluidExtractor.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(512, "basicmachine.fluidextractor.tier.02", "Advanced Fluid Extractor", 2, "Extracting Fluids from Items", GT_Recipe.GT_Recipe_Map.sFluidExtractionRecipes, 1, 1, 16000, 0, 1, "FluidExtractor.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "FLUID_EXTRACTOR", new Object[]{"GCG", "PME", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_HV_FluidExtractor.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(513, "basicmachine.fluidextractor.tier.03", "Advanced Fluid Extractor II", 3, "Extracting Fluids from Items", GT_Recipe.GT_Recipe_Map.sFluidExtractionRecipes, 1, 1, 16000, 0, 1, "FluidExtractor.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "FLUID_EXTRACTOR", new Object[]{"GCG", "PME", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_EV_FluidExtractor.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(514, "basicmachine.fluidextractor.tier.04", "Advanced Fluid Extractor III", 4, "Extracting Fluids from Items", GT_Recipe.GT_Recipe_Map.sFluidExtractionRecipes, 1, 1, 16000, 0, 1, "FluidExtractor.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "FLUID_EXTRACTOR", new Object[]{"GCG", "PME", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_IV_FluidExtractor.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(515, "basicmachine.fluidextractor.tier.05", "Advanced Fluid Extractor IV", 5, "Extracting Fluids from Items", GT_Recipe.GT_Recipe_Map.sFluidExtractionRecipes, 1, 1, 16000, 0, 1, "FluidExtractor.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "FLUID_EXTRACTOR", new Object[]{"GCG", "PME", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_LuV_FluidExtractor.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(516, "basicmachine.fluidextractor.tier.06", "Advanced Fluid Extractor V", 6, "Extracting Fluids from Items", GT_Recipe.GT_Recipe_Map.sFluidExtractionRecipes, 1, 1, 16000, 0, 1, "FluidExtractor.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "FLUID_EXTRACTOR", new Object[]{"GCG", "PME", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_ZPM_FluidExtractor.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(517, "basicmachine.fluidextractor.tier.07", "Advanced Fluid Extractor VI", 7, "Extracting Fluids from Items", GT_Recipe.GT_Recipe_Map.sFluidExtractionRecipes, 1, 1, 16000, 0, 1, "FluidExtractor.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "FLUID_EXTRACTOR", new Object[]{"GCG", "PME", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_UV_FluidExtractor.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(518, "basicmachine.fluidextractor.tier.08", "Advanced Fluid Extractor VII", 8, "Extracting Fluids from Items", GT_Recipe.GT_Recipe_Map.sFluidExtractionRecipes, 1, 1, 16000, 0, 1, "FluidExtractor.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "FLUID_EXTRACTOR", new Object[]{"GCG", "PME", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));

        ItemList.Machine_LV_FluidSolidifier.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(521, "basicmachine.fluidsolidifier.tier.01", "Basic Fluid Solidifier", 1, "Cools Fluids down to form Solids", GT_Recipe.GT_Recipe_Map.sFluidSolidficationRecipes, 1, 1, 16000, 0, 1, "FluidSolidifier.png", "", aBoolConst_0, aBoolConst_0, 0, "FLUID_SOLIDIFIER", new Object[]{"PGP", aTextWireHull, "CBC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS, 'B', OreDictNames.craftingChest}).getStackForm(1L));
        ItemList.Machine_MV_FluidSolidifier.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(522, "basicmachine.fluidsolidifier.tier.02", "Advanced Fluid Solidifier", 2, "Cools Fluids down to form Solids", GT_Recipe.GT_Recipe_Map.sFluidSolidficationRecipes, 1, 1, 16000, 0, 1, "FluidSolidifier.png", "", aBoolConst_0, aBoolConst_0, 0, "FLUID_SOLIDIFIER", new Object[]{"PGP", aTextWireHull, "CBC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS, 'B', OreDictNames.craftingChest}).getStackForm(1L));
        ItemList.Machine_HV_FluidSolidifier.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(523, "basicmachine.fluidsolidifier.tier.03", "Advanced Fluid Solidifier II", 3, "Cools Fluids down to form Solids", GT_Recipe.GT_Recipe_Map.sFluidSolidficationRecipes, 1, 1, 16000, 0, 1, "FluidSolidifier.png", "", aBoolConst_0, aBoolConst_0, 0, "FLUID_SOLIDIFIER", new Object[]{"PGP", aTextWireHull, "CBC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS, 'B', OreDictNames.craftingChest}).getStackForm(1L));
        ItemList.Machine_EV_FluidSolidifier.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(524, "basicmachine.fluidsolidifier.tier.04", "Advanced Fluid Solidifier III", 4, "Cools Fluids down to form Solids", GT_Recipe.GT_Recipe_Map.sFluidSolidficationRecipes, 1, 1, 16000, 0, 1, "FluidSolidifier.png", "", aBoolConst_0, aBoolConst_0, 0, "FLUID_SOLIDIFIER", new Object[]{"PGP", aTextWireHull, "CBC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS, 'B', OreDictNames.craftingChest}).getStackForm(1L));
        ItemList.Machine_IV_FluidSolidifier.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(525, "basicmachine.fluidsolidifier.tier.05", "Advanced Fluid Solidifier IV", 5, "Cools Fluids down to form Solids", GT_Recipe.GT_Recipe_Map.sFluidSolidficationRecipes, 1, 1, 16000, 0, 1, "FluidSolidifier.png", "", aBoolConst_0, aBoolConst_0, 0, "FLUID_SOLIDIFIER", new Object[]{"PGP", aTextWireHull, "CBC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS, 'B', OreDictNames.craftingChest}).getStackForm(1L));
        ItemList.Machine_LuV_FluidSolidifier.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(526, "basicmachine.fluidsolidifier.tier.06", "Advanced Fluid Solidifier V", 6, "Cools Fluids down to form Solids", GT_Recipe.GT_Recipe_Map.sFluidSolidficationRecipes, 1, 1, 16000, 0, 1, "FluidSolidifier.png", "", aBoolConst_0, aBoolConst_0, 0, "FLUID_SOLIDIFIER", new Object[]{"PGP", aTextWireHull, "CBC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS, 'B', OreDictNames.craftingChest}).getStackForm(1L));
        ItemList.Machine_ZPM_FluidSolidifier.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(527, "basicmachine.fluidsolidifier.tier.07", "Advanced Fluid Solidifier VI", 7, "Cools Fluids down to form Solids", GT_Recipe.GT_Recipe_Map.sFluidSolidficationRecipes, 1, 1, 16000, 0, 1, "FluidSolidifier.png", "", aBoolConst_0, aBoolConst_0, 0, "FLUID_SOLIDIFIER", new Object[]{"PGP", aTextWireHull, "CBC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS, 'B', OreDictNames.craftingChest}).getStackForm(1L));
        ItemList.Machine_UV_FluidSolidifier.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(528, "basicmachine.fluidsolidifier.tier.08", "Advanced Fluid Solidifier VII", 8, "Cools Fluids down to form Solids", GT_Recipe.GT_Recipe_Map.sFluidSolidficationRecipes, 1, 1, 16000, 0, 1, "FluidSolidifier.png", "", aBoolConst_0, aBoolConst_0, 0, "FLUID_SOLIDIFIER", new Object[]{"PGP", aTextWireHull, "CBC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS, 'B', OreDictNames.craftingChest}).getStackForm(1L));

        ItemList.Machine_LV_Distillery.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(531, "basicmachine.distillery.tier.01", "Basic Distillery", 1, "Extracting the most relevant Parts of Fluids", GT_Recipe.GT_Recipe_Map.sDistilleryRecipes, 1, 1, 1000, 0, 1, "Distillery.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "DISTILLERY", new Object[]{"GBG", aTextCableHull, aTextWirePump, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'B', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_DISTILLATION, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_MV_Distillery.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(532, "basicmachine.distillery.tier.02", "Advanced Distillery", 2, "Extracting the most relevant Parts of Fluids", GT_Recipe.GT_Recipe_Map.sDistilleryRecipes, 1, 1, 1000, 0, 1, "Distillery.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "DISTILLERY", new Object[]{"GBG", aTextCableHull, aTextWirePump, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'B', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_DISTILLATION, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_HV_Distillery.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(533, "basicmachine.distillery.tier.03", "Advanced Distillery II", 3, "Extracting the most relevant Parts of Fluids", GT_Recipe.GT_Recipe_Map.sDistilleryRecipes, 1, 1, 1000, 0, 1, "Distillery.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "DISTILLERY", new Object[]{"GBG", aTextCableHull, aTextWirePump, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'B', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_DISTILLATION, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_EV_Distillery.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(534, "basicmachine.distillery.tier.04", "Advanced Distillery III", 4, "Extracting the most relevant Parts of Fluids", GT_Recipe.GT_Recipe_Map.sDistilleryRecipes, 1, 1, 1000, 0, 1, "Distillery.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "DISTILLERY", new Object[]{"GBG", aTextCableHull, aTextWirePump, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'B', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_DISTILLATION, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_IV_Distillery.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(535, "basicmachine.distillery.tier.05", "Advanced Distillery IV", 5, "Extracting the most relevant Parts of Fluids", GT_Recipe.GT_Recipe_Map.sDistilleryRecipes, 1, 1, 1000, 0, 1, "Distillery.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "DISTILLERY", new Object[]{"GBG", aTextCableHull, aTextWirePump, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'B', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_DISTILLATION, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_LuV_Distillery.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(536, "basicmachine.distillery.tier.06", "Advanced Distillery V", 6, "Extracting the most relevant Parts of Fluids", GT_Recipe.GT_Recipe_Map.sDistilleryRecipes, 1, 1, 1000, 0, 1, "Distillery.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "DISTILLERY", new Object[]{"GBG", aTextCableHull, aTextWirePump, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'B', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_DISTILLATION, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_ZPM_Distillery.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(537, "basicmachine.distillery.tier.07", "Advanced Distillery VI", 7, "Extracting the most relevant Parts of Fluids", GT_Recipe.GT_Recipe_Map.sDistilleryRecipes, 1, 1, 1000, 0, 1, "Distillery.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "DISTILLERY", new Object[]{"GBG", aTextCableHull, aTextWirePump, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'B', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_DISTILLATION, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_UV_Distillery.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(538, "basicmachine.distillery.tier.08", "Advanced Distillery VII", 8, "Extracting the most relevant Parts of Fluids", GT_Recipe.GT_Recipe_Map.sDistilleryRecipes, 1, 1, 1000, 0, 1, "Distillery.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(200)), aBoolConst_0, aBoolConst_0, 0, "DISTILLERY", new Object[]{"GBG", aTextCableHull, aTextWirePump, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'B', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_DISTILLATION, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));

        ItemList.Machine_LV_ChemicalBath.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(541, "basicmachine.chemicalbath.tier.01", "Basic Chemical Bath", 1, "Bathing Ores in Chemicals to separate them", GT_Recipe.GT_Recipe_Map.sChemicalBathRecipes, 1, 3, 8000, 0, 1, "ChemicalBath.png", "", aBoolConst_0, true, 0, "CHEMICAL_BATH", new Object[]{"VGW", "PGV", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_MV_ChemicalBath.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(542, "basicmachine.chemicalbath.tier.02", "Advanced Chemical Bath", 2, "Bathing Ores in Chemicals to separate them", GT_Recipe.GT_Recipe_Map.sChemicalBathRecipes, 1, 3, 8000, 0, 1, "ChemicalBath.png", "", aBoolConst_0, true, 0, "CHEMICAL_BATH", new Object[]{"VGW", "PGV", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_HV_ChemicalBath.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(543, "basicmachine.chemicalbath.tier.03", "Advanced Chemical Bath II", 3, "Bathing Ores in Chemicals to separate them", GT_Recipe.GT_Recipe_Map.sChemicalBathRecipes, 1, 3, 8000, 0, 1, "ChemicalBath.png", "", aBoolConst_0, true, 0, "CHEMICAL_BATH", new Object[]{"VGW", "PGV", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_EV_ChemicalBath.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(544, "basicmachine.chemicalbath.tier.04", "Advanced Chemical Bath III", 4, "Bathing Ores in Chemicals to separate them", GT_Recipe.GT_Recipe_Map.sChemicalBathRecipes, 1, 3, 8000, 0, 1, "ChemicalBath.png", "", aBoolConst_0, true, 0, "CHEMICAL_BATH", new Object[]{"VGW", "PGV", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_IV_ChemicalBath.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(545, "basicmachine.chemicalbath.tier.05", "Advanced Chemical Bath IV", 5, "Bathing Ores in Chemicals to separate them", GT_Recipe.GT_Recipe_Map.sChemicalBathRecipes, 1, 3, 8000, 0, 1, "ChemicalBath.png", "", aBoolConst_0, true, 0, "CHEMICAL_BATH", new Object[]{"VGW", "PGV", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_LuV_ChemicalBath.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(546, "basicmachine.chemicalbath.tier.06", "Advanced Chemical Bath V", 6, "Bathing Ores in Chemicals to separate them", GT_Recipe.GT_Recipe_Map.sChemicalBathRecipes, 1, 3, 8000, 0, 1, "ChemicalBath.png", "", aBoolConst_0, true, 0, "CHEMICAL_BATH", new Object[]{"VGW", "PGV", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_ZPM_ChemicalBath.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(547, "basicmachine.chemicalbath.tier.07", "Advanced Chemical Bath VI", 7, "Bathing Ores in Chemicals to separate them", GT_Recipe.GT_Recipe_Map.sChemicalBathRecipes, 1, 3, 8000, 0, 1, "ChemicalBath.png", "", aBoolConst_0, true, 0, "CHEMICAL_BATH", new Object[]{"VGW", "PGV", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_UV_ChemicalBath.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(548, "basicmachine.chemicalbath.tier.08", "Advanced Chemical Bath VII", 8, "Bathing Ores in Chemicals to separate them", GT_Recipe.GT_Recipe_Map.sChemicalBathRecipes, 1, 3, 8000, 0, 1, "ChemicalBath.png", "", aBoolConst_0, true, 0, "CHEMICAL_BATH", new Object[]{"VGW", "PGV", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));

        ItemList.Machine_LV_Polarizer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(551, "basicmachine.polarizer.tier.01", "Basic Polarizer", 1, "Bipolarising your Magnets", GT_Recipe.GT_Recipe_Map.sPolarizerRecipes, 1, 1, 0, 0, 1, "Polarizer.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(212)), aBoolConst_0, aBoolConst_0, 0, "POLARIZER", new Object[]{"ZSZ", aTextWireHull, "ZSZ", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_MV_Polarizer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(552, "basicmachine.polarizer.tier.02", "Advanced Polarizer", 2, "Bipolarising your Magnets", GT_Recipe.GT_Recipe_Map.sPolarizerRecipes, 1, 1, 0, 0, 1, "Polarizer.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(212)), aBoolConst_0, aBoolConst_0, 0, "POLARIZER", new Object[]{"ZSZ", aTextWireHull, "ZSZ", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_HV_Polarizer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(553, "basicmachine.polarizer.tier.03", "Advanced Polarizer II", 3, "Bipolarising your Magnets", GT_Recipe.GT_Recipe_Map.sPolarizerRecipes, 1, 1, 0, 0, 1, "Polarizer.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(212)), aBoolConst_0, aBoolConst_0, 0, "POLARIZER", new Object[]{"ZSZ", aTextWireHull, "ZSZ", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_EV_Polarizer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(554, "basicmachine.polarizer.tier.04", "Advanced Polarizer III", 4, "Bipolarising your Magnets", GT_Recipe.GT_Recipe_Map.sPolarizerRecipes, 1, 1, 0, 0, 1, "Polarizer.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(212)), aBoolConst_0, aBoolConst_0, 0, "POLARIZER", new Object[]{"ZSZ", aTextWireHull, "ZSZ", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_IV_Polarizer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(555, "basicmachine.polarizer.tier.05", "Advanced Polarizer IV", 5, "Bipolarising your Magnets", GT_Recipe.GT_Recipe_Map.sPolarizerRecipes, 1, 1, 0, 0, 1, "Polarizer.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(212)), aBoolConst_0, aBoolConst_0, 0, "POLARIZER", new Object[]{"ZSZ", aTextWireHull, "ZSZ", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_LuV_Polarizer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(556, "basicmachine.polarizer.tier.06", "Advanced Polarizer V", 6, "Bipolarising your Magnets", GT_Recipe.GT_Recipe_Map.sPolarizerRecipes, 1, 1, 0, 0, 1, "Polarizer.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(212)), aBoolConst_0, aBoolConst_0, 0, "POLARIZER", new Object[]{"ZSZ", aTextWireHull, "ZSZ", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_ZPM_Polarizer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(557, "basicmachine.polarizer.tier.07", "Advanced Polarizer VI", 7, "Bipolarising your Magnets", GT_Recipe.GT_Recipe_Map.sPolarizerRecipes, 1, 1, 0, 0, 1, "Polarizer.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(212)), aBoolConst_0, aBoolConst_0, 0, "POLARIZER", new Object[]{"ZSZ", aTextWireHull, "ZSZ", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_UV_Polarizer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(558, "basicmachine.polarizer.tier.08", "Advanced Polarizer VII", 8, "Bipolarising your Magnets", GT_Recipe.GT_Recipe_Map.sPolarizerRecipes, 1, 1, 0, 0, 1, "Polarizer.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(212)), aBoolConst_0, aBoolConst_0, 0, "POLARIZER", new Object[]{"ZSZ", aTextWireHull, "ZSZ", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));

        ItemList.Machine_LV_ElectromagneticSeparator.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(561, "basicmachine.electromagneticseparator.tier.01", "Basic Electromagnetic Separator", 1, "Separating the magnetic Ores from the rest", GT_Recipe.GT_Recipe_Map.sElectroMagneticSeparatorRecipes, 1, 3, 0, 0, 1, "ElectromagneticSeparator.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(212)), aBoolConst_0, aBoolConst_0, 0, "ELECTROMAGNETIC_SEPARATOR", new Object[]{"VWZ", "WMS", "CWZ", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_MV_ElectromagneticSeparator.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(562, "basicmachine.electromagneticseparator.tier.02", "Advanced Electromagnetic Separator", 2, "Separating the magnetic Ores from the rest", GT_Recipe.GT_Recipe_Map.sElectroMagneticSeparatorRecipes, 1, 3, 0, 0, 1, "ElectromagneticSeparator.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(212)), aBoolConst_0, aBoolConst_0, 0, "ELECTROMAGNETIC_SEPARATOR", new Object[]{"VWZ", "WMS", "CWZ", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_HV_ElectromagneticSeparator.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(563, "basicmachine.electromagneticseparator.tier.03", "Advanced Electromagnetic Separator II", 3, "Separating the magnetic Ores from the rest", GT_Recipe.GT_Recipe_Map.sElectroMagneticSeparatorRecipes, 1, 3, 0, 0, 1, "ElectromagneticSeparator.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(212)), aBoolConst_0, aBoolConst_0, 0, "ELECTROMAGNETIC_SEPARATOR", new Object[]{"VWZ", "WMS", "CWZ", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_EV_ElectromagneticSeparator.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(564, "basicmachine.electromagneticseparator.tier.04", "Advanced Electromagnetic Separator III", 4, "Separating the magnetic Ores from the rest", GT_Recipe.GT_Recipe_Map.sElectroMagneticSeparatorRecipes, 1, 3, 0, 0, 1, "ElectromagneticSeparator.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(212)), aBoolConst_0, aBoolConst_0, 0, "ELECTROMAGNETIC_SEPARATOR", new Object[]{"VWZ", "WMS", "CWZ", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_IV_ElectromagneticSeparator.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(565, "basicmachine.electromagneticseparator.tier.05", "Advanced Electromagnetic Separator IV", 5, "Separating the magnetic Ores from the rest", GT_Recipe.GT_Recipe_Map.sElectroMagneticSeparatorRecipes, 1, 3, 0, 0, 1, "ElectromagneticSeparator.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(212)), aBoolConst_0, aBoolConst_0, 0, "ELECTROMAGNETIC_SEPARATOR", new Object[]{"VWZ", "WMS", "CWZ", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_LuV_ElectromagneticSeparator.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(566, "basicmachine.electromagneticseparator.tier.06", "Advanced Electromagnetic Separator V", 6, "Separating the magnetic Ores from the rest", GT_Recipe.GT_Recipe_Map.sElectroMagneticSeparatorRecipes, 1, 3, 0, 0, 1, "ElectromagneticSeparator.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(212)), aBoolConst_0, aBoolConst_0, 0, "ELECTROMAGNETIC_SEPARATOR", new Object[]{"VWZ", "WMS", "CWZ", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_ZPM_ElectromagneticSeparator.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(567, "basicmachine.electromagneticseparator.tier.07", "Advanced Electromagnetic Separator VI", 7, "Separating the magnetic Ores from the rest", GT_Recipe.GT_Recipe_Map.sElectroMagneticSeparatorRecipes, 1, 3, 0, 0, 1, "ElectromagneticSeparator.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(212)), aBoolConst_0, aBoolConst_0, 0, "ELECTROMAGNETIC_SEPARATOR", new Object[]{"VWZ", "WMS", "CWZ", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_UV_ElectromagneticSeparator.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(568, "basicmachine.electromagneticseparator.tier.08", "Advanced Electromagnetic Separator VII", 8, "Separating the magnetic Ores from the rest", GT_Recipe.GT_Recipe_Map.sElectroMagneticSeparatorRecipes, 1, 3, 0, 0, 1, "ElectromagneticSeparator.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(212)), aBoolConst_0, aBoolConst_0, 0, "ELECTROMAGNETIC_SEPARATOR", new Object[]{"VWZ", "WMS", "CWZ", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));

        ItemList.Machine_LV_Autoclave.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(571, "basicmachine.autoclave.tier.01", "Basic Autoclave", 1, "Crystallizing your Dusts", GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes, 1, 1, 8000, 0, 1, "Autoclave.png", "", aBoolConst_0, aBoolConst_0, 0, "AUTOCLAVE", new Object[]{"IGI", "IMI", "CPC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_MV_Autoclave.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(572, "basicmachine.autoclave.tier.02", "Advanced Autoclave", 2, "Crystallizing your Dusts", GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes, 1, 1, 8000, 0, 1, "Autoclave.png", "", aBoolConst_0, aBoolConst_0, 0, "AUTOCLAVE", new Object[]{"IGI", "IMI", "CPC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_HV_Autoclave.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(573, "basicmachine.autoclave.tier.03", "Advanced Autoclave II", 3, "Crystallizing your Dusts", GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes, 1, 1, 8000, 0, 1, "Autoclave.png", "", aBoolConst_0, aBoolConst_0, 0, "AUTOCLAVE", new Object[]{"IGI", "IMI", "CPC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_EV_Autoclave.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(574, "basicmachine.autoclave.tier.04", "Advanced Autoclave III", 4, "Crystallizing your Dusts", GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes, 1, 1, 8000, 0, 1, "Autoclave.png", "", aBoolConst_0, aBoolConst_0, 0, "AUTOCLAVE", new Object[]{"IGI", "IMI", "CPC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_IV_Autoclave.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(575, "basicmachine.autoclave.tier.05", "Advanced Autoclave IV", 5, "Crystallizing your Dusts", GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes, 1, 1, 8000, 0, 1, "Autoclave.png", "", aBoolConst_0, aBoolConst_0, 0, "AUTOCLAVE", new Object[]{"IGI", "IMI", "CPC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_LuV_Autoclave.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(576, "basicmachine.autoclave.tier.06", "Advanced Autoclave V", 6, "Crystallizing your Dusts", GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes, 1, 1, 8000, 0, 1, "Autoclave.png", "", aBoolConst_0, aBoolConst_0, 0, "AUTOCLAVE", new Object[]{"IGI", "IMI", "CPC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_ZPM_Autoclave.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(577, "basicmachine.autoclave.tier.07", "Advanced Autoclave VI", 7, "Crystallizing your Dusts", GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes, 1, 1, 8000, 0, 1, "Autoclave.png", "", aBoolConst_0, aBoolConst_0, 0, "AUTOCLAVE", new Object[]{"IGI", "IMI", "CPC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_UV_Autoclave.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(578, "basicmachine.autoclave.tier.08", "Advanced Autoclave VII", 8, "Crystallizing your Dusts", GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes, 1, 1, 8000, 0, 1, "Autoclave.png", "", aBoolConst_0, aBoolConst_0, 0, "AUTOCLAVE", new Object[]{"IGI", "IMI", "CPC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));

        ItemList.Machine_LV_Mixer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(581, "basicmachine.mixer.tier.01", "Basic Mixer", 1, "Will it Blend?", GT_Recipe.GT_Recipe_Map.sMixerRecipes, 6, 1, 16000, 0, 1, "Mixer.png", "", aBoolConst_0, aBoolConst_0, 0, "MIXER", new Object[]{"GRG", "GEG", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_MV_Mixer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(582, "basicmachine.mixer.tier.02", "Advanced Mixer", 2, "Will it Blend?", GT_Recipe.GT_Recipe_Map.sMixerRecipes, 6, 1, 24000, 0, 1, "Mixer.png", "", aBoolConst_0, aBoolConst_0, 0, "MIXER", new Object[]{"GRG", "GEG", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_HV_Mixer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(583, "basicmachine.mixer.tier.03", "Advanced Mixer II", 3, "Will it Blend?", GT_Recipe.GT_Recipe_Map.sMixerRecipes, 6, 1, 32000, 0, 1, "Mixer.png", "", aBoolConst_0, aBoolConst_0, 0, "MIXER", new Object[]{"GRG", "GEG", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_EV_Mixer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(584, "basicmachine.mixer.tier.04", "Advanced Mixer III", 4, "Will it Blend?", GT_Recipe.GT_Recipe_Map.sMixerRecipes, 6, 1, 48000, 0, 1, "Mixer.png", "", aBoolConst_0, aBoolConst_0, 0, "MIXER", new Object[]{"GRG", "GEG", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_IV_Mixer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(585, "basicmachine.mixer.tier.05", "Advanced Mixer IV", 5, "Will it Blend?", GT_Recipe.GT_Recipe_Map.sMixerRecipes, 6, 1, 64000, 0, 1, "Mixer.png", "", aBoolConst_0, aBoolConst_0, 0, "MIXER", new Object[]{"GRG", "GEG", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_LuV_Mixer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(586, "basicmachine.mixer.tier.06", "Advanced Mixer V", 6, "Will it Blend?", GT_Recipe.GT_Recipe_Map.sMixerRecipes, 6, 1, 64000, 0, 1, "Mixer.png", "", aBoolConst_0, aBoolConst_0, 0, "MIXER", new Object[]{"GRG", "GEG", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_ZPM_Mixer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(587, "basicmachine.mixer.tier.07", "Advanced Mixer VI", 7, "Will it Blend?", GT_Recipe.GT_Recipe_Map.sMixerRecipes, 6, 1, 64000, 0, 1, "Mixer.png", "", aBoolConst_0, aBoolConst_0, 0, "MIXER", new Object[]{"GRG", "GEG", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_UV_Mixer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(588, "basicmachine.mixer.tier.08", "Advanced Mixer VII", 8, "Will it Blend?", GT_Recipe.GT_Recipe_Map.sMixerRecipes, 6, 1, 64000, 0, 1, "Mixer.png", "", aBoolConst_0, aBoolConst_0, 0, "MIXER", new Object[]{"GRG", "GEG", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));

        ItemList.Machine_LV_LaserEngraver.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(591, "basicmachine.laserengraver.tier.01", "Basic Precision Laser Engraver", 1, "Don't look directly at the Laser", GT_Recipe.GT_Recipe_Map.sLaserEngraverRecipes, 2, 1, 0, 0, 1, "LaserEngraver.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(212)), aBoolConst_0, aBoolConst_0, 0, "LASER_ENGRAVER", new Object[]{"PEP", aTextCableHull, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_MV_LaserEngraver.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(592, "basicmachine.laserengraver.tier.02", "Advanced Precision Laser Engraver", 2, "Don't look directly at the Laser", GT_Recipe.GT_Recipe_Map.sLaserEngraverRecipes, 2, 1, 0, 0, 1, "LaserEngraver.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(212)), aBoolConst_0, aBoolConst_0, 0, "LASER_ENGRAVER", new Object[]{"PEP", aTextCableHull, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_HV_LaserEngraver.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(593, "basicmachine.laserengraver.tier.03", "Advanced Precision Laser Engraver II", 3, "Don't look directly at the Laser", GT_Recipe.GT_Recipe_Map.sLaserEngraverRecipes, 2, 1, 0, 0, 1, "LaserEngraver.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(212)), aBoolConst_0, aBoolConst_0, 0, "LASER_ENGRAVER", new Object[]{"PEP", aTextCableHull, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_EV_LaserEngraver.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(594, "basicmachine.laserengraver.tier.04", "Advanced Precision Laser Engraver III", 4, "Don't look directly at the Laser", GT_Recipe.GT_Recipe_Map.sLaserEngraverRecipes, 2, 1, 0, 0, 1, "LaserEngraver.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(212)), aBoolConst_0, aBoolConst_0, 0, "LASER_ENGRAVER", new Object[]{"PEP", aTextCableHull, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_IV_LaserEngraver.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(595, "basicmachine.laserengraver.tier.05", "Advanced Precision Laser Engraver IV", 5, "Don't look directly at the Laser", GT_Recipe.GT_Recipe_Map.sLaserEngraverRecipes, 2, 1, 0, 0, 1, "LaserEngraver.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(212)), aBoolConst_0, aBoolConst_0, 0, "LASER_ENGRAVER", new Object[]{"PEP", aTextCableHull, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_LuV_LaserEngraver.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(596, "basicmachine.laserengraver.tier.06", "Advanced Precision Laser Engraver V", 6, "Don't look directly at the Laser", GT_Recipe.GT_Recipe_Map.sLaserEngraverRecipes, 2, 1, 0, 0, 1, "LaserEngraver.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(212)), aBoolConst_0, aBoolConst_0, 0, "LASER_ENGRAVER", new Object[]{"PEP", aTextCableHull, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_ZPM_LaserEngraver.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(597, "basicmachine.laserengraver.tier.07", "Advanced Precision Laser Engraver VI", 7, "Don't look directly at the Laser", GT_Recipe.GT_Recipe_Map.sLaserEngraverRecipes, 2, 1, 0, 0, 1, "LaserEngraver.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(212)), aBoolConst_0, aBoolConst_0, 0, "LASER_ENGRAVER", new Object[]{"PEP", aTextCableHull, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_UV_LaserEngraver.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(598, "basicmachine.laserengraver.tier.08", "Advanced Precision Laser Engraver VII", 8, "Don't look directly at the Laser", GT_Recipe.GT_Recipe_Map.sLaserEngraverRecipes, 2, 1, 0, 0, 1, "LaserEngraver.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(212)), aBoolConst_0, aBoolConst_0, 0, "LASER_ENGRAVER", new Object[]{"PEP", aTextCableHull, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));

        ItemList.Machine_LV_Press.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(601, "basicmachine.press.tier.01", "Basic Forming Press", 1, "Imprinting Images into things", GT_Recipe.GT_Recipe_Map.sPressRecipes, 2, 1, 0, 0, 1, "Press.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(203)), aBoolConst_0, aBoolConst_0, 0, "PRESS", new Object[]{aTextWirePump, aTextCableHull, aTextWirePump, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_MV_Press.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(602, "basicmachine.press.tier.02", "Advanced Forming Press", 2, "Imprinting Images into things", GT_Recipe.GT_Recipe_Map.sPressRecipes, 2, 1, 0, 0, 1, "Press.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(203)), aBoolConst_0, aBoolConst_0, 0, "PRESS", new Object[]{aTextWirePump, aTextCableHull, aTextWirePump, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_HV_Press.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(603, "basicmachine.press.tier.03", "Advanced Forming Press II", 3, "Imprinting Images into things", GT_Recipe.GT_Recipe_Map.sPressRecipes, 2, 1, 0, 0, 1, "Press.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(203)), aBoolConst_0, aBoolConst_0, 0, "PRESS", new Object[]{aTextWirePump, aTextCableHull, aTextWirePump, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_EV_Press.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(604, "basicmachine.press.tier.04", "Advanced Forming Press III", 4, "Imprinting Images into things", GT_Recipe.GT_Recipe_Map.sPressRecipes, 2, 1, 0, 0, 1, "Press.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(203)), aBoolConst_0, aBoolConst_0, 0, "PRESS", new Object[]{aTextWirePump, aTextCableHull, aTextWirePump, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_IV_Press.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(605, "basicmachine.press.tier.05", "Advanced Forming Press IV", 5, "Imprinting Images into things", GT_Recipe.GT_Recipe_Map.sPressRecipes, 2, 1, 0, 0, 1, "Press.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(203)), aBoolConst_0, aBoolConst_0, 0, "PRESS", new Object[]{aTextWirePump, aTextCableHull, aTextWirePump, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_LuV_Press.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(606, "basicmachine.press.tier.06", "Advanced Forming Press V", 6, "Imprinting Images into things", GT_Recipe.GT_Recipe_Map.sPressRecipes, 2, 1, 0, 0, 1, "Press.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(203)), aBoolConst_0, aBoolConst_0, 0, "PRESS", new Object[]{aTextWirePump, aTextCableHull, aTextWirePump, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_ZPM_Press.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(607, "basicmachine.press.tier.07", "Advanced Forming Press VI", 7, "Imprinting Images into things", GT_Recipe.GT_Recipe_Map.sPressRecipes, 2, 1, 0, 0, 1, "Press.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(203)), aBoolConst_0, aBoolConst_0, 0, "PRESS", new Object[]{aTextWirePump, aTextCableHull, aTextWirePump, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_UV_Press.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(608, "basicmachine.press.tier.08", "Advanced Forming Press VII", 8, "Imprinting Images into things", GT_Recipe.GT_Recipe_Map.sPressRecipes, 2, 1, 0, 0, 1, "Press.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(203)), aBoolConst_0, aBoolConst_0, 0, "PRESS", new Object[]{aTextWirePump, aTextCableHull, aTextWirePump, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));

        ItemList.Machine_LV_Hammer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(611, "basicmachine.hammer.tier.01", "Basic Forge Hammer", 1, "Stop, Hammertime!", GT_Recipe.GT_Recipe_Map.sHammerRecipes, 1, 1, 0, 6, 3, "Hammer.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(1)), aBoolConst_0, aBoolConst_0, 0, "HAMMER", new Object[]{aTextWirePump, aTextCableHull, "WAW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'A', OreDictNames.craftingAnvil}).getStackForm(1L));
        ItemList.Machine_MV_Hammer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(612, "basicmachine.hammer.tier.02", "Advanced Forge Hammer", 2, "Stop, Hammertime!", GT_Recipe.GT_Recipe_Map.sHammerRecipes, 1, 1, 0, 6, 3, "Hammer.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(1)), aBoolConst_0, aBoolConst_0, 0, "HAMMER", new Object[]{aTextWirePump, aTextCableHull, "WAW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'A', OreDictNames.craftingAnvil}).getStackForm(1L));
        ItemList.Machine_HV_Hammer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(613, "basicmachine.hammer.tier.03", "Advanced Forge Hammer II", 3, "Stop, Hammertime!", GT_Recipe.GT_Recipe_Map.sHammerRecipes, 1, 1, 0, 6, 3, "Hammer.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(1)), aBoolConst_0, aBoolConst_0, 0, "HAMMER", new Object[]{aTextWirePump, aTextCableHull, "WAW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'A', OreDictNames.craftingAnvil}).getStackForm(1L));
        ItemList.Machine_EV_Hammer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(614, "basicmachine.hammer.tier.04", "Advanced Forge Hammer III", 4, "Stop, Hammertime!", GT_Recipe.GT_Recipe_Map.sHammerRecipes, 1, 1, 0, 6, 3, "Hammer.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(1)), aBoolConst_0, aBoolConst_0, 0, "HAMMER", new Object[]{aTextWirePump, aTextCableHull, "WAW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'A', OreDictNames.craftingAnvil}).getStackForm(1L));
        ItemList.Machine_IV_Hammer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(615, "basicmachine.hammer.tier.05", "Advanced Forge Hammer IV", 5, "Stop, Hammertime!", GT_Recipe.GT_Recipe_Map.sHammerRecipes, 1, 1, 0, 6, 3, "Hammer.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(1)), aBoolConst_0, aBoolConst_0, 0, "HAMMER", new Object[]{aTextWirePump, aTextCableHull, "WAW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'A', OreDictNames.craftingAnvil}).getStackForm(1L));
        ItemList.Machine_LuV_Hammer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(616, "basicmachine.hammer.tier.06", "Advanced Forge Hammer V", 6, "Stop, Hammertime!", GT_Recipe.GT_Recipe_Map.sHammerRecipes, 1, 1, 0, 6, 3, "Hammer.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(1)), aBoolConst_0, aBoolConst_0, 0, "HAMMER", new Object[]{aTextWirePump, aTextCableHull, "WAW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'A', OreDictNames.craftingAnvil}).getStackForm(1L));
        ItemList.Machine_ZPM_Hammer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(617, "basicmachine.hammer.tier.07", "Advanced Forge Hammer VI", 7, "Stop, Hammertime!", GT_Recipe.GT_Recipe_Map.sHammerRecipes, 1, 1, 0, 6, 3, "Hammer.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(1)), aBoolConst_0, aBoolConst_0, 0, "HAMMER", new Object[]{aTextWirePump, aTextCableHull, "WAW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'A', OreDictNames.craftingAnvil}).getStackForm(1L));
        ItemList.Machine_UV_Hammer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(618, "basicmachine.hammer.tier.08", "Advanced Forge Hammer VII", 8, "Stop, Hammertime!", GT_Recipe.GT_Recipe_Map.sHammerRecipes, 1, 1, 0, 6, 3, "Hammer.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(1)), aBoolConst_0, aBoolConst_0, 0, "HAMMER", new Object[]{aTextWirePump, aTextCableHull, "WAW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'A', OreDictNames.craftingAnvil}).getStackForm(1L));

        ItemList.Machine_LV_FluidHeater.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(621, "basicmachine.fluidheater.tier.01", "Basic Fluid Heater", 1, "Heating up your Fluids", GT_Recipe.GT_Recipe_Map.sFluidHeaterRecipes, 1, 0, 8000, 0, 1, "FluidHeater.png", "", aBoolConst_0, aBoolConst_0, 0, "FLUID_HEATER", new Object[]{"OGO", aTextPlateMotor, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'O', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_MV_FluidHeater.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(622, "basicmachine.fluidheater.tier.02", "Advanced Fluid Heater", 2, "Heating up your Fluids", GT_Recipe.GT_Recipe_Map.sFluidHeaterRecipes, 1, 0, 8000, 0, 1, "FluidHeater.png", "", aBoolConst_0, aBoolConst_0, 0, "FLUID_HEATER", new Object[]{"OGO", aTextPlateMotor, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'O', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_HV_FluidHeater.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(623, "basicmachine.fluidheater.tier.03", "Advanced Fluid Heater II", 3, "Heating up your Fluids", GT_Recipe.GT_Recipe_Map.sFluidHeaterRecipes, 1, 0, 8000, 0, 1, "FluidHeater.png", "", aBoolConst_0, aBoolConst_0, 0, "FLUID_HEATER", new Object[]{"OGO", aTextPlateMotor, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'O', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_EV_FluidHeater.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(624, "basicmachine.fluidheater.tier.04", "Advanced Fluid Heater III", 4, "Heating up your Fluids", GT_Recipe.GT_Recipe_Map.sFluidHeaterRecipes, 1, 0, 8000, 0, 1, "FluidHeater.png", "", aBoolConst_0, aBoolConst_0, 0, "FLUID_HEATER", new Object[]{"OGO", aTextPlateMotor, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'O', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_IV_FluidHeater.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(625, "basicmachine.fluidheater.tier.05", "Advanced Fluid Heater IV", 5, "Heating up your Fluids", GT_Recipe.GT_Recipe_Map.sFluidHeaterRecipes, 1, 0, 8000, 0, 1, "FluidHeater.png", "", aBoolConst_0, aBoolConst_0, 0, "FLUID_HEATER", new Object[]{"OGO", aTextPlateMotor, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'O', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_LuV_FluidHeater.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(626, "basicmachine.fluidheater.tier.06", "Advanced Fluid Heater V", 6, "Heating up your Fluids", GT_Recipe.GT_Recipe_Map.sFluidHeaterRecipes, 1, 0, 8000, 0, 1, "FluidHeater.png", "", aBoolConst_0, aBoolConst_0, 0, "FLUID_HEATER", new Object[]{"OGO", aTextPlateMotor, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'O', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_ZPM_FluidHeater.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(627, "basicmachine.fluidheater.tier.07", "Advanced Fluid Heater VI", 7, "Heating up your Fluids", GT_Recipe.GT_Recipe_Map.sFluidHeaterRecipes, 1, 0, 8000, 0, 1, "FluidHeater.png", "", aBoolConst_0, aBoolConst_0, 0, "FLUID_HEATER", new Object[]{"OGO", aTextPlateMotor, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'O', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));
        ItemList.Machine_UV_FluidHeater.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(628, "basicmachine.fluidheater.tier.08", "Advanced Fluid Heater VII", 8, "Heating up your Fluids", GT_Recipe.GT_Recipe_Map.sFluidHeaterRecipes, 1, 0, 8000, 0, 1, "FluidHeater.png", "", aBoolConst_0, aBoolConst_0, 0, "FLUID_HEATER", new Object[]{"OGO", aTextPlateMotor, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'O', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));

        ItemList.Machine_LV_Slicer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(631, "basicmachine.slicer.tier.01", "Basic Slicing Machine", 1, "Slice of Life", GT_Recipe.GT_Recipe_Map.sSlicerRecipes, 2, 1, 0, 0, 1, "Slicer.png", "", aBoolConst_0, aBoolConst_0, 0, "SLICER", new Object[]{aTextWireCoil, "PMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_MV_Slicer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(632, "basicmachine.slicer.tier.02", "Advanced Slicing Machine", 2, "Slice of Life", GT_Recipe.GT_Recipe_Map.sSlicerRecipes, 2, 1, 0, 0, 1, "Slicer.png", "", aBoolConst_0, aBoolConst_0, 0, "SLICER", new Object[]{aTextWireCoil, "PMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_HV_Slicer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(633, "basicmachine.slicer.tier.03", "Advanced Slicing Machine II", 3, "Slice of Life", GT_Recipe.GT_Recipe_Map.sSlicerRecipes, 2, 1, 0, 0, 1, "Slicer.png", "", aBoolConst_0, aBoolConst_0, 0, "SLICER", new Object[]{aTextWireCoil, "PMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_EV_Slicer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(634, "basicmachine.slicer.tier.04", "Advanced Slicing Machine III", 4, "Slice of Life", GT_Recipe.GT_Recipe_Map.sSlicerRecipes, 2, 1, 0, 0, 1, "Slicer.png", "", aBoolConst_0, aBoolConst_0, 0, "SLICER", new Object[]{aTextWireCoil, "PMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_IV_Slicer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(635, "basicmachine.slicer.tier.05", "Advanced Slicing Machine IV", 5, "Slice of Life", GT_Recipe.GT_Recipe_Map.sSlicerRecipes, 2, 1, 0, 0, 1, "Slicer.png", "", aBoolConst_0, aBoolConst_0, 0, "SLICER", new Object[]{aTextWireCoil, "PMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_LuV_Slicer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(636, "basicmachine.slicer.tier.06", "Advanced Slicing Machine V", 6, "Slice of Life", GT_Recipe.GT_Recipe_Map.sSlicerRecipes, 2, 1, 0, 0, 1, "Slicer.png", "", aBoolConst_0, aBoolConst_0, 0, "SLICER", new Object[]{aTextWireCoil, "PMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_ZPM_Slicer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(637, "basicmachine.slicer.tier.07", "Advanced Slicing Machine VI", 7, "Slice of Life", GT_Recipe.GT_Recipe_Map.sSlicerRecipes, 2, 1, 0, 0, 1, "Slicer.png", "", aBoolConst_0, aBoolConst_0, 0, "SLICER", new Object[]{aTextWireCoil, "PMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_UV_Slicer.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(638, "basicmachine.slicer.tier.08", "Advanced Slicing Machine VII", 8, "Slice of Life", GT_Recipe.GT_Recipe_Map.sSlicerRecipes, 2, 1, 0, 0, 1, "Slicer.png", "", aBoolConst_0, aBoolConst_0, 0, "SLICER", new Object[]{aTextWireCoil, "PMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));

        ItemList.Machine_LV_Sifter.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(641, "basicmachine.sifter.tier.01", "Basic Sifting Machine", 1, "Stay calm and keep sifting", GT_Recipe.GT_Recipe_Map.sSifterRecipes, 1, 9, 0, 2, 5, "Sifter.png", "", aBoolConst_0, aBoolConst_0, 0, "SIFTER", new Object[]{"WFW", aTextPlateMotor, "CFC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'F', OreDictNames.craftingFilter, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_MV_Sifter.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(642, "basicmachine.sifter.tier.02", "Advanced Sifting Machine", 2, "Stay calm and keep sifting", GT_Recipe.GT_Recipe_Map.sSifterRecipes, 1, 9, 0, 2, 5, "Sifter.png", "", aBoolConst_0, aBoolConst_0, 0, "SIFTER", new Object[]{"WFW", aTextPlateMotor, "CFC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'F', OreDictNames.craftingFilter, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_HV_Sifter.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(643, "basicmachine.sifter.tier.03", "Advanced Sifting Machine II", 3, "Stay calm and keep sifting", GT_Recipe.GT_Recipe_Map.sSifterRecipes, 1, 9, 0, 2, 5, "Sifter.png", "", aBoolConst_0, aBoolConst_0, 0, "SIFTER", new Object[]{"WFW", aTextPlateMotor, "CFC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'F', OreDictNames.craftingFilter, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_EV_Sifter.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(644, "basicmachine.sifter.tier.04", "Advanced Sifting Machine III", 4, "Stay calm and keep sifting", GT_Recipe.GT_Recipe_Map.sSifterRecipes, 1, 9, 0, 2, 5, "Sifter.png", "", aBoolConst_0, aBoolConst_0, 0, "SIFTER", new Object[]{"WFW", aTextPlateMotor, "CFC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'F', OreDictNames.craftingFilter, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_IV_Sifter.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(645, "basicmachine.sifter.tier.05", "Advanced Sifting Machine IV", 5, "Stay calm and keep sifting", GT_Recipe.GT_Recipe_Map.sSifterRecipes, 1, 9, 0, 2, 5, "Sifter.png", "", aBoolConst_0, aBoolConst_0, 0, "SIFTER", new Object[]{"WFW", aTextPlateMotor, "CFC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'F', OreDictNames.craftingFilter, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_LuV_Sifter.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(646, "basicmachine.sifter.tier.06", "Advanced Sifting Machine V", 6, "Stay calm and keep sifting", GT_Recipe.GT_Recipe_Map.sSifterRecipes, 1, 9, 0, 2, 5, "Sifter.png", "", aBoolConst_0, aBoolConst_0, 0, "SIFTER", new Object[]{"WFW", aTextPlateMotor, "CFC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'F', OreDictNames.craftingFilter, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_ZPM_Sifter.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(647, "basicmachine.sifter.tier.07", "Advanced Sifting Machine VI", 7, "Stay calm and keep sifting", GT_Recipe.GT_Recipe_Map.sSifterRecipes, 1, 9, 0, 2, 5, "Sifter.png", "", aBoolConst_0, aBoolConst_0, 0, "SIFTER", new Object[]{"WFW", aTextPlateMotor, "CFC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'F', OreDictNames.craftingFilter, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));
        ItemList.Machine_UV_Sifter.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(648, "basicmachine.sifter.tier.08", "Advanced Sifting Machine VII", 8, "Stay calm and keep sifting", GT_Recipe.GT_Recipe_Map.sSifterRecipes, 1, 9, 0, 2, 5, "Sifter.png", "", aBoolConst_0, aBoolConst_0, 0, "SIFTER", new Object[]{"WFW", aTextPlateMotor, "CFC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'F', OreDictNames.craftingFilter, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));

        ItemList.Machine_LV_ArcFurnace.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(651, "basicmachine.arcfurnace.tier.01", "Basic Arc Furnace", 1, "", GT_Recipe.GT_Recipe_Map.sArcFurnaceRecipes, 1, 4, 16000, 0, 1, "ArcFurnace.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(202)), aBoolConst_0, aBoolConst_0, 0, "ARC_FURNACE", new Object[]{"WGW", aTextCableHull, aTextPlate, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'G', OrePrefixes.cell.get(Materials.Graphite)}).getStackForm(1L));
        ItemList.Machine_MV_ArcFurnace.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(652, "basicmachine.arcfurnace.tier.02", "Advanced Arc Furnace", 2, "", GT_Recipe.GT_Recipe_Map.sArcFurnaceRecipes, 1, 4, 24000, 0, 1, "ArcFurnace.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(202)), aBoolConst_0, aBoolConst_0, 0, "ARC_FURNACE", new Object[]{"WGW", aTextCableHull, aTextPlate, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'G', OrePrefixes.cell.get(Materials.Graphite)}).getStackForm(1L));
        ItemList.Machine_HV_ArcFurnace.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(653, "basicmachine.arcfurnace.tier.03", "Advanced Arc Furnace II", 3, "", GT_Recipe.GT_Recipe_Map.sArcFurnaceRecipes, 1, 4, 32000, 0, 1, "ArcFurnace.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(202)), aBoolConst_0, aBoolConst_0, 0, "ARC_FURNACE", new Object[]{"WGW", aTextCableHull, aTextPlate, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'G', OrePrefixes.cell.get(Materials.Graphite)}).getStackForm(1L));
        ItemList.Machine_EV_ArcFurnace.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(654, "basicmachine.arcfurnace.tier.04", "Advanced Arc Furnace III", 4, "", GT_Recipe.GT_Recipe_Map.sArcFurnaceRecipes, 1, 4, 48000, 0, 1, "ArcFurnace.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(202)), aBoolConst_0, aBoolConst_0, 0, "ARC_FURNACE", new Object[]{"WGW", aTextCableHull, aTextPlate, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'G', OrePrefixes.cell.get(Materials.Graphite)}).getStackForm(1L));
        ItemList.Machine_IV_ArcFurnace.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(655, "basicmachine.arcfurnace.tier.05", "Advanced Arc Furnace IV", 5, "", GT_Recipe.GT_Recipe_Map.sArcFurnaceRecipes, 1, 4, 64000, 0, 1, "ArcFurnace.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(202)), aBoolConst_0, aBoolConst_0, 0, "ARC_FURNACE", new Object[]{"WGW", aTextCableHull, aTextPlate, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'G', OrePrefixes.cell.get(Materials.Graphite)}).getStackForm(1L));
        ItemList.Machine_LuV_ArcFurnace.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(656, "basicmachine.arcfurnace.tier.06", "Advanced Arc Furnace V", 6, "", GT_Recipe.GT_Recipe_Map.sArcFurnaceRecipes, 1, 4, 64000, 0, 1, "ArcFurnace.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(202)), aBoolConst_0, aBoolConst_0, 0, "ARC_FURNACE", new Object[]{"WGW", aTextCableHull, aTextPlate, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'G', OrePrefixes.cell.get(Materials.Graphite)}).getStackForm(1L));
        ItemList.Machine_ZPM_ArcFurnace.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(657, "basicmachine.arcfurnace.tier.07", "Advanced Arc Furnace VI", 7, "", GT_Recipe.GT_Recipe_Map.sArcFurnaceRecipes, 1, 4, 64000, 0, 1, "ArcFurnace.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(202)), aBoolConst_0, aBoolConst_0, 0, "ARC_FURNACE", new Object[]{"WGW", aTextCableHull, aTextPlate, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'G', OrePrefixes.cell.get(Materials.Graphite)}).getStackForm(1L));
        ItemList.Machine_UV_ArcFurnace.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(658, "basicmachine.arcfurnace.tier.08", "Advanced Arc Furnace VII", 8, "", GT_Recipe.GT_Recipe_Map.sArcFurnaceRecipes, 1, 4, 64000, 0, 1, "ArcFurnace.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(202)), aBoolConst_0, aBoolConst_0, 0, "ARC_FURNACE", new Object[]{"WGW", aTextCableHull, aTextPlate, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'G', OrePrefixes.cell.get(Materials.Graphite)}).getStackForm(1L));

        ItemList.Machine_LV_PlasmaArcFurnace.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(661, "basicmachine.plasmaarcfurnace.tier.01", "Basic Plasma Arc Furnace", 1, "", GT_Recipe.GT_Recipe_Map.sPlasmaArcFurnaceRecipes, 1, 4, 1000, 0, 1, "PlasmaArcFurnace.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(202)), aBoolConst_0, aBoolConst_0, 0, "PLASMA_ARC_FURNACE", new Object[]{"WGW", aTextCableHull, "TPT", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'T', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'G', OrePrefixes.cell.get(Materials.Graphite)}).getStackForm(1L));
        ItemList.Machine_MV_PlasmaArcFurnace.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(662, "basicmachine.plasmaarcfurnace.tier.02", "Advanced Plasma Arc Furnace", 2, "", GT_Recipe.GT_Recipe_Map.sPlasmaArcFurnaceRecipes, 1, 4, 2000, 0, 1, "PlasmaArcFurnace.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(202)), aBoolConst_0, aBoolConst_0, 0, "PLASMA_ARC_FURNACE", new Object[]{"WGW", aTextCableHull, "TPT", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'T', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'G', OrePrefixes.cell.get(Materials.Graphite)}).getStackForm(1L));
        ItemList.Machine_HV_PlasmaArcFurnace.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(663, "basicmachine.plasmaarcfurnace.tier.03", "Advanced Plasma Arc Furnace II", 3, "", GT_Recipe.GT_Recipe_Map.sPlasmaArcFurnaceRecipes, 1, 4, 4000, 0, 1, "PlasmaArcFurnace.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(202)), aBoolConst_0, aBoolConst_0, 0, "PLASMA_ARC_FURNACE", new Object[]{"WGW", aTextCableHull, "TPT", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'T', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'G', OrePrefixes.cell.get(Materials.Graphite)}).getStackForm(1L));
        ItemList.Machine_EV_PlasmaArcFurnace.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(664, "basicmachine.plasmaarcfurnace.tier.04", "Advanced Plasma Arc Furnace III", 4, "", GT_Recipe.GT_Recipe_Map.sPlasmaArcFurnaceRecipes, 1, 4, 8000, 0, 1, "PlasmaArcFurnace.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(202)), aBoolConst_0, aBoolConst_0, 0, "PLASMA_ARC_FURNACE", new Object[]{"WGW", aTextCableHull, "TPT", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'T', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'G', OrePrefixes.cell.get(Materials.Graphite)}).getStackForm(1L));
        ItemList.Machine_IV_PlasmaArcFurnace.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(665, "basicmachine.plasmaarcfurnace.tier.05", "Advanced Plasma Arc Furnace IV", 5, "", GT_Recipe.GT_Recipe_Map.sPlasmaArcFurnaceRecipes, 1, 4, 16000, 0, 1, "PlasmaArcFurnace.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(202)), aBoolConst_0, aBoolConst_0, 0, "PLASMA_ARC_FURNACE", new Object[]{"WGW", aTextCableHull, "TPT", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'T', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'G', OrePrefixes.cell.get(Materials.Graphite)}).getStackForm(1L));
        ItemList.Machine_LuV_PlasmaArcFurnace.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(666, "basicmachine.plasmaarcfurnace.tier.06", "Advanced Plasma Arc Furnace V", 6, "", GT_Recipe.GT_Recipe_Map.sPlasmaArcFurnaceRecipes, 1, 4, 16000, 0, 1, "PlasmaArcFurnace.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(202)), aBoolConst_0, aBoolConst_0, 0, "PLASMA_ARC_FURNACE", new Object[]{"WGW", aTextCableHull, "TPT", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'T', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'G', OrePrefixes.cell.get(Materials.Graphite)}).getStackForm(1L));
        ItemList.Machine_ZPM_PlasmaArcFurnace.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(667, "basicmachine.plasmaarcfurnace.tier.07", "Advanced Plasma Arc Furnace VI", 7, "", GT_Recipe.GT_Recipe_Map.sPlasmaArcFurnaceRecipes, 1, 4, 24000, 0, 1, "PlasmaArcFurnace.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(202)), aBoolConst_0, aBoolConst_0, 0, "PLASMA_ARC_FURNACE", new Object[]{"WGW", aTextCableHull, "TPT", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'T', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'G', OrePrefixes.cell.get(Materials.Graphite)}).getStackForm(1L));
        ItemList.Machine_UV_PlasmaArcFurnace.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(668, "basicmachine.plasmaarcfurnace.tier.08", "Advanced Plasma Arc Furnace VII", 8, "", GT_Recipe.GT_Recipe_Map.sPlasmaArcFurnaceRecipes, 1, 4, 32000, 0, 1, "PlasmaArcFurnace.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(202)), aBoolConst_0, aBoolConst_0, 0, "PLASMA_ARC_FURNACE", new Object[]{"WGW", aTextCableHull, "TPT", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'T', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'G', OrePrefixes.cell.get(Materials.Graphite)}).getStackForm(1L));

        ItemList.Machine_LV_Oven.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(671, "basicmachine.e_oven.tier.01", "Basic Electric Oven", 1, "Just a Furnace with a different Design", GT_Recipe.GT_Recipe_Map.sFurnaceRecipes, 1, 1, 0, 0, 1, "E_Oven.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(207)), aBoolConst_0, aBoolConst_0, 0, "ELECTRIC_OVEN", new Object[]{"CEC", aTextCableHull, "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING}).getStackForm(1L));
        ItemList.Machine_MV_Oven.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(672, "basicmachine.e_oven.tier.02", "Advanced Electric Oven", 2, "Just a Furnace with a different Design", GT_Recipe.GT_Recipe_Map.sFurnaceRecipes, 1, 1, 0, 0, 1, "E_Oven.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(207)), aBoolConst_0, aBoolConst_0, 0, "ELECTRIC_OVEN", new Object[]{"CEC", aTextCableHull, "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING}).getStackForm(1L));
        ItemList.Machine_HV_Oven.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(673, "basicmachine.e_oven.tier.03", "Advanced Electric Oven II", 3, "Just a Furnace with a different Design", GT_Recipe.GT_Recipe_Map.sFurnaceRecipes, 1, 1, 0, 0, 1, "E_Oven.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(207)), aBoolConst_0, aBoolConst_0, 0, "ELECTRIC_OVEN", new Object[]{"CEC", aTextCableHull, "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING}).getStackForm(1L));
        ItemList.Machine_EV_Oven.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(674, "basicmachine.e_oven.tier.04", "Advanced Electric Oven III", 4, "Just a Furnace with a different Design", GT_Recipe.GT_Recipe_Map.sFurnaceRecipes, 1, 1, 0, 0, 1, "E_Oven.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(207)), aBoolConst_0, aBoolConst_0, 0, "ELECTRIC_OVEN", new Object[]{"CEC", aTextCableHull, "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING}).getStackForm(1L));
        ItemList.Machine_IV_Oven.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(675, "basicmachine.e_oven.tier.05", "Advanced Electric Oven IV", 5, "Just a Furnace with a different Design", GT_Recipe.GT_Recipe_Map.sFurnaceRecipes, 1, 1, 0, 0, 1, "E_Oven.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(207)), aBoolConst_0, aBoolConst_0, 0, "ELECTRIC_OVEN", new Object[]{"CEC", aTextCableHull, "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING}).getStackForm(1L));
        ItemList.Machine_LuV_Oven.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(676, "basicmachine.e_oven.tier.06", "Advanced Electric Oven V", 6, "Just a Furnace with a different Design", GT_Recipe.GT_Recipe_Map.sFurnaceRecipes, 1, 1, 0, 0, 1, "E_Oven.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(207)), aBoolConst_0, aBoolConst_0, 0, "ELECTRIC_OVEN", new Object[]{"CEC", aTextCableHull, "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING}).getStackForm(1L));
        ItemList.Machine_ZPM_Oven.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(677, "basicmachine.e_oven.tier.07", "Advanced Electric Oven VI", 7, "Just a Furnace with a different Design", GT_Recipe.GT_Recipe_Map.sFurnaceRecipes, 1, 1, 0, 0, 1, "E_Oven.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(207)), aBoolConst_0, aBoolConst_0, 0, "ELECTRIC_OVEN", new Object[]{"CEC", aTextCableHull, "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING}).getStackForm(1L));
        ItemList.Machine_UV_Oven.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(678, "basicmachine.e_oven.tier.08", "Advanced Electric Oven VII", 8, "Just a Furnace with a different Design", GT_Recipe.GT_Recipe_Map.sFurnaceRecipes, 1, 1, 0, 0, 1, "E_Oven.png", (String) GregTech_API.sSoundList.get(Integer.valueOf(207)), aBoolConst_0, aBoolConst_0, 0, "ELECTRIC_OVEN", new Object[]{"CEC", aTextCableHull, "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING}).getStackForm(1L));

        ItemList.Machine_LV_Miner.set(new GT_MetaTileEntity_Miner(679, "basicmachine.miner.tier.01", "Basic Miner", 1).getStackForm(1L));
        ItemList.Machine_MV_Miner.set(new GT_MetaTileEntity_Miner(680, "basicmachine.miner.tier.02", "Good Miner", 2).getStackForm(1L));
        ItemList.Machine_HV_Miner.set(new GT_MetaTileEntity_Miner(681, "basicmachine.miner.tier.03", "Advanced Miner", 3).getStackForm(1L));
        ItemList.Machine_EV_Miner.set(new GT_MetaTileEntity_Miner(12101, "basicmachine.miner.tier.04", "Extreme Miner", 4).getStackForm(1L));
        ItemList.Machine_IV_Miner.set(new GT_MetaTileEntity_Miner(12102, "basicmachine.miner.tier.05", "Insane Miner", 5).getStackForm(1L));
        

        GT_ModHandler.addCraftingRecipe(ItemList.Machine_LV_Miner.get(1L), bitsd, new Object[]{"EEE", aTextWireHull, "CSC", 'M', ItemList.Hull_LV, 'E', ItemList.Electric_Motor_LV, 'C', OrePrefixes.circuit.get(Materials.Basic), 'W', OrePrefixes.cableGt01.get(Materials.Tin), 'S', ItemList.Sensor_LV});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_MV_Miner.get(1L), bitsd, new Object[]{"PEP", aTextWireHull, "CSC", 'M', ItemList.Hull_MV, 'E', ItemList.Electric_Motor_MV, 'P', ItemList.Electric_Piston_MV, 'C', OrePrefixes.circuit.get(Materials.Good), 'W', OrePrefixes.cableGt02.get(Materials.Copper), 'S', ItemList.Sensor_MV});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_HV_Miner.get(1L), bitsd, new Object[]{"RPR", aTextWireHull, "CSC", 'M', ItemList.Hull_HV, 'E', ItemList.Electric_Motor_HV, 'P', ItemList.Electric_Piston_HV, 'R', ItemList.Robot_Arm_HV, 'C', OrePrefixes.circuit.get(Materials.Advanced), 'W', OrePrefixes.cableGt04.get(Materials.Gold), 'S', ItemList.Sensor_HV});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_EV_Miner.get(1L), bitsd, new Object[]{"RPR", aTextWireHull, "CSC", 'M', ItemList.Hull_EV, 'P', ItemList.Electric_Piston_EV, 'R', ItemList.Robot_Arm_EV, 'C', OrePrefixes.circuit.get(Materials.Data), 'W', OrePrefixes.cableGt08.get(Materials.Aluminium), 'S', ItemList.Sensor_EV});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_IV_Miner.get(1L), bitsd, new Object[]{"RRR", aTextWireHull, "CSC", 'M', ItemList.Hull_IV, 'R', ItemList.Robot_Arm_IV, 'C', OrePrefixes.circuit.get(Materials.Elite), 'W', OrePrefixes.cableGt12.get(Materials.Platinum), 'S', ItemList.Sensor_IV});
        
    }

    private static void run3() {
    	long bits = GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED;
    	long bitsd = GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED;
        ItemList.Machine_Multi_BlastFurnace.set(new GT_MetaTileEntity_ElectricBlastFurnace(1000, "multimachine.blastfurnace", "Electric Blast Furnace").getStackForm(1L));
        ItemList.Machine_Multi_ImplosionCompressor.set(new GT_MetaTileEntity_ImplosionCompressor(1001, "multimachine.implosioncompressor", "Implosion Compressor").getStackForm(1L));
        ItemList.Machine_Multi_VacuumFreezer.set(new GT_MetaTileEntity_VacuumFreezer(1002, "multimachine.vacuumfreezer", "Vacuum Freezer").getStackForm(1L));
        ItemList.Machine_Multi_Furnace.set(new GT_MetaTileEntity_MultiFurnace(1003, "multimachine.multifurnace", "Multi Smelter").getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(ItemList.Machine_Multi_BlastFurnace.get(1L, new Object[0]), bitsd, new Object[]{"FFF", aTextCableHull, aTextWireCoil, 'M', ItemList.Casing_HeatProof, 'F', ItemList.Machine_LV_Oven, 'C', OrePrefixes.circuit.get(Materials.Basic), 'W', OrePrefixes.cableGt01.get(Materials.Tin)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_Multi_VacuumFreezer.get(1L, new Object[0]), bitsd, new Object[]{aTextPlate, aTextCableHull, aTextWireCoil, 'M', ItemList.Casing_FrostProof, 'P', ItemList.Electric_Pump_HV, 'C', OrePrefixes.circuit.get(Materials.Data), 'W', OrePrefixes.cableGt01.get(Materials.Gold)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_Multi_ImplosionCompressor.get(1L, new Object[0]), bitsd, new Object[]{"OOO", aTextCableHull, aTextWireCoil, 'M', ItemList.Casing_SolidSteel, 'O', Ic2Items.reinforcedStone, 'C', OrePrefixes.circuit.get(Materials.Advanced), 'W', OrePrefixes.cableGt01.get(Materials.Gold)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_Multi_Furnace.get(1L, new Object[0]), bitsd, new Object[]{"FFF", aTextCableHull, aTextWireCoil, 'M', ItemList.Casing_HeatProof, 'F', ItemList.Machine_LV_Oven, 'C', OrePrefixes.circuit.get(Materials.Advanced), 'W', OrePrefixes.cableGt01.get(Materials.AnnealedCopper)});

        ItemList.Machine_Multi_LargeBoiler_Bronze.set(new GT_MetaTileEntity_LargeBoiler_Bronze(1020, "multimachine.boiler.bronze", "Large Bronze Boiler").getStackForm(1L));
        ItemList.Machine_Multi_LargeBoiler_Steel.set(new GT_MetaTileEntity_LargeBoiler_Steel(1021, "multimachine.boiler.steel", "Large Steel Boiler").getStackForm(1L));
        ItemList.Machine_Multi_LargeBoiler_Titanium.set(new GT_MetaTileEntity_LargeBoiler_Titanium(1022, "multimachine.boiler.titanium", "Large Titanium Boiler").getStackForm(1L));
        ItemList.Machine_Multi_LargeBoiler_TungstenSteel.set(new GT_MetaTileEntity_LargeBoiler_TungstenSteel(1023, "multimachine.boiler.tungstensteel", "Large Tungstensteel Boiler").getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(ItemList.Machine_Multi_LargeBoiler_Bronze.get(1L, new Object[0]), bitsd, new Object[]{aTextWireCoil, aTextCableHull, aTextWireCoil, 'M', ItemList.Casing_Firebox_Bronze, 'C', OrePrefixes.circuit.get(Materials.Basic), 'W', OrePrefixes.cableGt01.get(Materials.Tin)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_Multi_LargeBoiler_Steel.get(1L, new Object[0]), bitsd, new Object[]{aTextWireCoil, aTextCableHull, aTextWireCoil, 'M', ItemList.Casing_Firebox_Steel, 'C', OrePrefixes.circuit.get(Materials.Advanced), 'W', OrePrefixes.cableGt01.get(Materials.Copper)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_Multi_LargeBoiler_Titanium.get(1L, new Object[0]), bitsd, new Object[]{aTextWireCoil, aTextCableHull, aTextWireCoil, 'M', ItemList.Casing_Firebox_Titanium, 'C', OrePrefixes.circuit.get(Materials.Data), 'W', OrePrefixes.cableGt01.get(Materials.Gold)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_Multi_LargeBoiler_TungstenSteel.get(1L, new Object[0]), bitsd, new Object[]{aTextWireCoil, aTextCableHull, aTextWireCoil, 'M', ItemList.Casing_Firebox_TungstenSteel, 'C', OrePrefixes.circuit.get(Materials.Elite), 'W', OrePrefixes.cableGt01.get(Materials.Aluminium)});


        ItemList.Generator_Diesel_LV.set(new GT_MetaTileEntity_DieselGenerator(1110, "basicgenerator.diesel.tier.01", "Basic Combustion Generator", 1).getStackForm(1L));
        ItemList.Generator_Diesel_MV.set(new GT_MetaTileEntity_DieselGenerator(1111, "basicgenerator.diesel.tier.02", "Advanced Combustion Generator", 2).getStackForm(1L));
        ItemList.Generator_Diesel_HV.set(new GT_MetaTileEntity_DieselGenerator(1112, "basicgenerator.diesel.tier.03", "Turbo Combustion Generator", 3).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(ItemList.Generator_Diesel_LV.get(1L, new Object[0]), bitsd, new Object[]{"PCP", "EME", "GWG", 'M', ItemList.Hull_LV, 'P', ItemList.Electric_Piston_LV, 'E', ItemList.Electric_Motor_LV, 'C', OrePrefixes.circuit.get(Materials.Basic), 'W', OrePrefixes.cableGt01.get(Materials.Tin), 'G', OrePrefixes.gearGt.get(Materials.Steel)});
        GT_ModHandler.addCraftingRecipe(ItemList.Generator_Diesel_MV.get(1L, new Object[0]), bitsd, new Object[]{"PCP", "EME", "GWG", 'M', ItemList.Hull_MV, 'P', ItemList.Electric_Piston_MV, 'E', ItemList.Electric_Motor_MV, 'C', OrePrefixes.circuit.get(Materials.Good), 'W', OrePrefixes.cableGt01.get(Materials.Copper), 'G', OrePrefixes.gearGt.get(Materials.Aluminium)});
        GT_ModHandler.addCraftingRecipe(ItemList.Generator_Diesel_HV.get(1L, new Object[0]), bitsd, new Object[]{"PCP", "EME", "GWG", 'M', ItemList.Hull_HV, 'P', ItemList.Electric_Piston_HV, 'E', ItemList.Electric_Motor_HV, 'C', OrePrefixes.circuit.get(Materials.Advanced), 'W', OrePrefixes.cableGt01.get(Materials.Gold), 'G', OrePrefixes.gearGt.get(Materials.StainlessSteel)});

        ItemList.Generator_Gas_Turbine_LV.set(new GT_MetaTileEntity_GasTurbine(1115, "basicgenerator.gasturbine.tier.01", "Basic Gas Turbine", 1).getStackForm(1L));
        ItemList.Generator_Gas_Turbine_MV.set(new GT_MetaTileEntity_GasTurbine(1116, "basicgenerator.gasturbine.tier.02", "Advanced Gas Turbine", 2).getStackForm(1L));
        ItemList.Generator_Gas_Turbine_HV.set(new GT_MetaTileEntity_GasTurbine(1117, "basicgenerator.gasturbine.tier.03", "Turbo Gas Turbine", 3).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(ItemList.Generator_Gas_Turbine_LV.get(1L, new Object[0]), bitsd, new Object[]{"CRC", "RMR", aTextMotorWire, 'M', ItemList.Hull_LV, 'E', ItemList.Electric_Motor_LV, 'R', OrePrefixes.rotor.get(Materials.Tin), 'C', OrePrefixes.circuit.get(Materials.Basic), 'W', OrePrefixes.cableGt01.get(Materials.Tin)});
        GT_ModHandler.addCraftingRecipe(ItemList.Generator_Gas_Turbine_MV.get(1L, new Object[0]), bitsd, new Object[]{"CRC", "RMR", aTextMotorWire, 'M', ItemList.Hull_MV, 'E', ItemList.Electric_Motor_MV, 'R', OrePrefixes.rotor.get(Materials.Bronze), 'C', OrePrefixes.circuit.get(Materials.Good), 'W', OrePrefixes.cableGt01.get(Materials.Copper)});
        GT_ModHandler.addCraftingRecipe(ItemList.Generator_Gas_Turbine_HV.get(1L, new Object[0]), bitsd, new Object[]{"CRC", "RMR", aTextMotorWire, 'M', ItemList.Hull_HV, 'E', ItemList.Electric_Motor_HV, 'R', OrePrefixes.rotor.get(Materials.Steel), 'C', OrePrefixes.circuit.get(Materials.Advanced), 'W', OrePrefixes.cableGt01.get(Materials.Gold)});

        ItemList.Generator_Steam_Turbine_LV.set(new GT_MetaTileEntity_SteamTurbine(1120, "basicgenerator.steamturbine.tier.01", "Basic Steam Turbine", 1).getStackForm(1L));
        ItemList.Generator_Steam_Turbine_MV.set(new GT_MetaTileEntity_SteamTurbine(1121, "basicgenerator.steamturbine.tier.02", "Advanced Steam Turbine", 2).getStackForm(1L));
        ItemList.Generator_Steam_Turbine_HV.set(new GT_MetaTileEntity_SteamTurbine(1122, "basicgenerator.steamturbine.tier.03", "Turbo Steam Turbine", 3).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(ItemList.Generator_Steam_Turbine_LV.get(1L, new Object[0]), bitsd, new Object[]{"PCP", "RMR", aTextMotorWire, 'M', ItemList.Hull_LV, 'E', ItemList.Electric_Motor_LV, 'R', OrePrefixes.rotor.get(Materials.Tin), 'C', OrePrefixes.circuit.get(Materials.Basic), 'W', OrePrefixes.cableGt01.get(Materials.Tin), 'P', OrePrefixes.pipeMedium.get(Materials.Bronze)});
        GT_ModHandler.addCraftingRecipe(ItemList.Generator_Steam_Turbine_MV.get(1L, new Object[0]), bitsd, new Object[]{"PCP", "RMR", aTextMotorWire, 'M', ItemList.Hull_MV, 'E', ItemList.Electric_Motor_MV, 'R', OrePrefixes.rotor.get(Materials.Bronze), 'C', OrePrefixes.circuit.get(Materials.Good), 'W', OrePrefixes.cableGt01.get(Materials.Copper), 'P', OrePrefixes.pipeMedium.get(Materials.Steel)});
        GT_ModHandler.addCraftingRecipe(ItemList.Generator_Steam_Turbine_HV.get(1L, new Object[0]), bitsd, new Object[]{"PCP", "RMR", aTextMotorWire, 'M', ItemList.Hull_HV, 'E', ItemList.Electric_Motor_HV, 'R', OrePrefixes.rotor.get(Materials.Steel), 'C', OrePrefixes.circuit.get(Materials.Advanced), 'W', OrePrefixes.cableGt01.get(Materials.Gold), 'P', OrePrefixes.pipeMedium.get(Materials.StainlessSteel)});

        ItemList.Generator_Naquadah_Mark_I.set(new GT_MetaTileEntity_SolidNaquadahReactor(1190, "basicgenerator.naquadah.tier.04", "Naquadah Reactor Mark I", 4).getStackForm(1L));
        ItemList.Generator_Naquadah_Mark_II.set(new GT_MetaTileEntity_SolidNaquadahReactor2(1191, "basicgenerator.naquadah.tier.05", "Naquadah Reactor Mark II", 5).getStackForm(1L));
        ItemList.Generator_Naquadah_Mark_III.set(new GT_MetaTileEntity_FluidNaquadahReactor(1192, "basicgenerator.naquadah.tier.06", "Naquadah Reactor Mark III", 6).getStackForm(1L));
        ItemList.Generator_Naquadah_Mark_IV.set(new GT_MetaTileEntity_SolidNaquadahReactor3(1188, "basicgenerator.naquadah.tier.07", "Naquadah Reactor Mark IV", 7).getStackForm(1L));
        ItemList.Generator_Naquadah_Mark_V.set(new GT_MetaTileEntity_SolidNaquadahReactor4(1189, "basicgenerator.naquadah.tier.08", "Naquadah Reactor Mark V", 8).getStackForm(1L));
        ItemList.Generator_Naquadah_Mark_VI.set(new GT_MetaTileEntity_SolidNaquadahReactor5(12110, "basicgenerator.naquadah.tier.09", "Naquadah Reactor Mark VI", 9).getStackForm(1L));
        ItemList.Generator_Naquadah_Mark_VII.set(new GT_MetaTileEntity_SolidNaquadahReactor6(12111, "basicgenerator.naquadah.tier.10", "Naquadah Reactor Mark VII", 10).getStackForm(1L));
        ItemList.Generator_Naquadah_Mark_VIII.set(new GT_MetaTileEntity_SolidNaquadahReactor7(12112, "basicgenerator.naquadah.tier.11", "Naquadah Reactor Mark VIII", 11).getStackForm(1L));
        
        GT_ModHandler.addCraftingRecipe(ItemList.Generator_Naquadah_Mark_I.get(1L), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"UCU", "FMF", "WCW", 'M', ItemList.Hull_EV, 'F', ItemList.Field_Generator_EV, 'C', OrePrefixes.circuit.get(Materials.Elite), 'W', OrePrefixes.cableGt04.get(Materials.Aluminium), 'U', OrePrefixes.stick.get(Materials.Uranium235)});
        GT_ModHandler.addCraftingRecipe(ItemList.Generator_Naquadah_Mark_II.get(1L), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"PCP", "FMF", "WCW", 'M', ItemList.Hull_IV, 'F', ItemList.Field_Generator_IV, 'C', OrePrefixes.circuit.get(Materials.Master), 'W', OrePrefixes.cableGt04.get(Materials.Tungsten), 'P', OrePrefixes.stick.get(Materials.Plutonium241)});
        GT_ModHandler.addCraftingRecipe(ItemList.Generator_Naquadah_Mark_III.get(1L), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"NCN", "FMF", "WCW", 'M', ItemList.Hull_LuV, 'F', ItemList.Field_Generator_LuV, 'C', OrePrefixes.circuit.get(Materials.Ultimate), 'W', OrePrefixes.cableGt04.get(Materials.HSSG), 'N', OrePrefixes.stick.get(Materials.Europium)});
        GT_ModHandler.addCraftingRecipe(ItemList.Generator_Naquadah_Mark_IV.get(1L), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"NCN", "FMF", "WCW", 'M', ItemList.Hull_ZPM, 'F', ItemList.Field_Generator_ZPM, 'C', OrePrefixes.circuit.get(Materials.Superconductor), 'W', OrePrefixes.cableGt04.get(Materials.Naquadah), 'N', OrePrefixes.stick.get(Materials.Duranium)});
        GT_ModHandler.addCraftingRecipe(ItemList.Generator_Naquadah_Mark_V.get(1L), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"NCN", "FMF", "WCW", 'M', ItemList.Hull_UV, 'F', ItemList.Field_Generator_UV, 'C', OrePrefixes.circuit.get(Materials.Infinite), 'W', OrePrefixes.cableGt04.get(Materials.ElectrumFlux), 'N', OrePrefixes.stick.get(Materials.Tritanium)});
        GT_ModHandler.addCraftingRecipe(ItemList.Generator_Naquadah_Mark_VI.get(1L), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"NCN", "FMF", "WCW", 'M', ItemList.Hull_MAX, 'F', ItemList.Field_Generator_UHV, 'C', OrePrefixes.circuit.get(Materials.Bio), 'W', OrePrefixes.wireGt04.get(Materials.Europium), 'N', OrePrefixes.stick.get(Materials.Infuscolium)});
        GT_ModHandler.addCraftingRecipe(ItemList.Generator_Naquadah_Mark_VII.get(1L), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"NCN", "FMF", "WCW", 'M', ItemList.Hull_UEV, 'F', ItemList.Field_Generator_UEV, 'C', OrePrefixes.circuit.get(Materials.Bio), 'W', OrePrefixes.wireGt04.get(Materials.Diamericiumtitanium), 'N', OrePrefixes.stick.get(Materials.Neutronium)});
        GT_ModHandler.addCraftingRecipe(ItemList.Generator_Naquadah_Mark_VIII.get(1L), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"NCN", "FMF", "WCW", 'M', ItemList.Hull_UIV, 'F', ItemList.Field_Generator_UEV, 'C', OrePrefixes.circuit.get(Materials.Bio), 'W', OrePrefixes.wireGt04.get(Materials.Neutronium), 'N', OrePrefixes.stickLong.get(Materials.Neutronium)});
        
        ItemList.MagicEnergyConverter_LV.set(new GT_MetaTileEntity_MagicEnergyConverter(1123, "basicgenerator.magicenergyconverter.tier.01", "Novice Magic Energy Converter", 1).getStackForm(1L));
        ItemList.MagicEnergyConverter_MV.set(new GT_MetaTileEntity_MagicEnergyConverter(1124, "basicgenerator.magicenergyconverter.tier.02", "Adept Magic Energy Converter", 2).getStackForm(1L));
        ItemList.MagicEnergyConverter_HV.set(new GT_MetaTileEntity_MagicEnergyConverter(1125, "basicgenerator.magicenergyconverter.tier.03", "Master Magic Energy Converter", 3).getStackForm(1L));

        ItemList.MagicEnergyAbsorber_LV.set(new GT_MetaTileEntity_MagicalEnergyAbsorber(1127, "basicgenerator.magicenergyabsorber.tier.01", "Novice Magic Energy Absorber", 1).getStackForm(1L));
        ItemList.MagicEnergyAbsorber_MV.set(new GT_MetaTileEntity_MagicalEnergyAbsorber(1128, "basicgenerator.magicenergyabsorber.tier.02", "Adept Magic Energy Absorber", 2).getStackForm(1L));
        ItemList.MagicEnergyAbsorber_HV.set(new GT_MetaTileEntity_MagicalEnergyAbsorber(1129, "basicgenerator.magicenergyabsorber.tier.03", "Master Magic Energy Absorber", 3).getStackForm(1L));
        ItemList.MagicEnergyAbsorber_EV.set(new GT_MetaTileEntity_MagicalEnergyAbsorber(1130, "basicgenerator.magicenergyabsorber.tier.04", "Grandmaster Magic Energy Absorber", 4).getStackForm(1L));
        if (!Loader.isModLoaded("Thaumcraft")) {
            GT_ModHandler.addCraftingRecipe(ItemList.MagicEnergyConverter_LV.get(1L, new Object[0]), bitsd, new Object[]{"CTC", "FMF", "CBC", 'M', ItemList.Hull_LV, 'B', new ItemStack(Blocks.beacon), 'C', OrePrefixes.circuit.get(Materials.Advanced), 'T', ItemList.Field_Generator_LV, 'F', OrePrefixes.plate.get(Materials.Platinum)});
            GT_ModHandler.addCraftingRecipe(ItemList.MagicEnergyConverter_MV.get(1L, new Object[0]), bitsd, new Object[]{"CTC", "FMF", "CBC", 'M', ItemList.Hull_MV, 'B', new ItemStack(Blocks.beacon), 'C', OrePrefixes.circuit.get(Materials.Data), 'T', ItemList.Field_Generator_MV, 'F', OrePrefixes.plate.get(Materials.Iridium)});
            GT_ModHandler.addCraftingRecipe(ItemList.MagicEnergyConverter_HV.get(1L, new Object[0]), bitsd, new Object[]{"CTC", "FMF", "CBC", 'M', ItemList.Hull_HV, 'B', new ItemStack(Blocks.beacon), 'C', OrePrefixes.circuit.get(Materials.Elite), 'T', ItemList.Field_Generator_HV, 'F', OrePrefixes.plate.get(Materials.Neutronium)});

            GT_ModHandler.addCraftingRecipe(ItemList.MagicEnergyAbsorber_LV.get(1L, new Object[0]), bitsd, new Object[]{"CTC", "FMF", "CBC", 'M', ItemList.Hull_LV, 'B', ItemList.MagicEnergyConverter_LV.get(1L, new Object[0]), 'C', OrePrefixes.circuit.get(Materials.Advanced), 'T', ItemList.Field_Generator_LV, 'F', OrePrefixes.plate.get(Materials.Platinum)});
            GT_ModHandler.addCraftingRecipe(ItemList.MagicEnergyAbsorber_MV.get(1L, new Object[0]), bitsd, new Object[]{"CTC", "FMF", "CBC", 'M', ItemList.Hull_MV, 'B', ItemList.MagicEnergyConverter_MV.get(1L, new Object[0]), 'C', OrePrefixes.circuit.get(Materials.Data), 'T', ItemList.Field_Generator_MV, 'F', OrePrefixes.plate.get(Materials.Iridium)});
            GT_ModHandler.addCraftingRecipe(ItemList.MagicEnergyAbsorber_HV.get(1L, new Object[0]), bitsd, new Object[]{"CTC", "FMF", "CBC", 'M', ItemList.Hull_HV, 'B', ItemList.MagicEnergyConverter_MV.get(1L, new Object[0]), 'C', OrePrefixes.circuit.get(Materials.Elite), 'T', ItemList.Field_Generator_HV, 'F', OrePrefixes.plate.get(Materials.Europium)});
            GT_ModHandler.addCraftingRecipe(ItemList.MagicEnergyAbsorber_EV.get(1L, new Object[0]), bitsd, new Object[]{"CTC", "FMF", "CBC", 'M', ItemList.Hull_HV, 'B', ItemList.MagicEnergyConverter_HV.get(1L, new Object[0]), 'C', OrePrefixes.circuit.get(Materials.Master), 'T', ItemList.Field_Generator_EV, 'F', OrePrefixes.plate.get(Materials.Neutronium)});
        }
        ItemList.FusionComputer_LuV.set(new GT_MetaTileEntity_FusionComputer1(1193, "fusioncomputer.tier.06", "Fusion Control Computer Mark I").getStackForm(1L));
        ItemList.FusionComputer_ZPMV.set(new GT_MetaTileEntity_FusionComputer2(1194, "fusioncomputer.tier.07", "Fusion Control Computer Mark II").getStackForm(1L));
        ItemList.FusionComputer_UV.set(new GT_MetaTileEntity_FusionComputer3(1195, "fusioncomputer.tier.08", "Fusion Control Computer Mark III").getStackForm(1L));
        ItemList.FusionComputer_UHV.set(new GT_MetaTileEntity_FusionComputer4(12107, "fusioncomputer.tier.09", "Fusion Control Computer Mark IV").getStackForm(1L));
        ItemList.FusionComputer_UEV.set(new GT_MetaTileEntity_FusionComputer5(12108, "fusioncomputer.tier.10", "Fusion Control Computer Mark V").getStackForm(1L));
        
//        GT_ModHandler.addCraftingRecipe(ItemList.Casing_Fusion_Coil.get(1L, new Object[0]), bitsd, new Object[]{"CTC", "FMF", "CTC", 'M', ItemList.Casing_Coil_Superconductor, 'C', OrePrefixes.circuit.get(Materials.Master), 'F', ItemList.Field_Generator_MV, 'T', ItemList.Neutron_Reflector});

//        GT_ModHandler.addCraftingRecipe(ItemList.FusionComputer_LuV.get(1L, new Object[0]), bitsd, new Object[]{"CTC", "FMF", "CBC", 'M', ItemList.Casing_Fusion_Coil, 'B', OrePrefixes.plate.get(Materials.NetherStar), 'C', OrePrefixes.circuit.get(Materials.Master), 'F', ItemList.Field_Generator_IV, 'T', OrePrefixes.plate.get(Materials.Plutonium241)});
//        GT_ModHandler.addCraftingRecipe(ItemList.FusionComputer_ZPMV.get(1L, new Object[0]), bitsd, new Object[]{"CBC", "FMF", "CBC", 'M', ItemList.FusionComputer_LuV, 'C', OrePrefixes.circuit.get(Materials.Master), 'F', ItemList.Field_Generator_IV, 'B', OrePrefixes.plate.get(Materials.Europium)});
//        GT_ModHandler.addCraftingRecipe(ItemList.FusionComputer_UV.get(1L, new Object[0]), bitsd, new Object[]{"CBC", "FMF", "CBC", 'M', ItemList.FusionComputer_ZPMV, 'C', OrePrefixes.circuit.get(Materials.Master), 'F', ItemList.Field_Generator_IV, 'B', OrePrefixes.plate.get(Materials.Americium)});

        ItemList.Generator_Plasma_IV.set(new GT_MetaTileEntity_PlasmaGenerator(1196, "basicgenerator.plasmagenerator.tier.05", "Plasma Generator Mark I", 5).getStackForm(1L));
        ItemList.Generator_Plasma_LuV.set(new GT_MetaTileEntity_PlasmaGenerator(1197, "basicgenerator.plasmagenerator.tier.06", "Plasma Generator Mark II", 6).getStackForm(1L));
        ItemList.Generator_Plasma_ZPMV.set(new GT_MetaTileEntity_PlasmaGenerator(1198, "basicgenerator.plasmagenerator.tier.07", "Plasma Generator Mark III", 7).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(ItemList.Generator_Plasma_IV.get(1L, new Object[0]), bitsd, new Object[]{"UCU", "FMF", aTextWireCoil, 'M', ItemList.Hull_IV, 'F', ItemList.Field_Generator_EV, 'C', OrePrefixes.circuit.get(Materials.Elite), 'W', OrePrefixes.wireGt04.get(Materials.SuperconductorIV), 'U', OrePrefixes.stick.get(Materials.Plutonium241)});
        GT_ModHandler.addCraftingRecipe(ItemList.Generator_Plasma_LuV.get(1L, new Object[0]), bitsd, new Object[]{"UCU", "FMF", aTextWireCoil, 'M', ItemList.Hull_LuV, 'F', ItemList.Field_Generator_IV, 'C', OrePrefixes.circuit.get(Materials.Master), 'W', OrePrefixes.wireGt04.get(Materials.SuperconductorLuV), 'U', OrePrefixes.stick.get(Materials.Europium)});
        GT_ModHandler.addCraftingRecipe(ItemList.Generator_Plasma_ZPMV.get(1L, new Object[0]), bitsd, new Object[]{"UCU", "FMF", aTextWireCoil, 'M', ItemList.Hull_ZPM, 'F', ItemList.Field_Generator_LuV, 'C', OrePrefixes.circuit.get(Materials.Ultimate), 'W', OrePrefixes.wireGt04.get(Materials.SuperconductorZPM), 'U', OrePrefixes.stick.get(Materials.Americium)});

        ItemList.Processing_Array_T1.set(new GT_MetaTileEntity_ProcessingArray(1199, "multimachine.processingarray.tier.04", "Processing Array Mk 1").getStackForm(1L));
        GT_ModHandler.addCraftingRecipe(ItemList.Processing_Array_T1.get(1L, new Object[0]), bitsd, new Object[]{"CTC", "FMF", "CBC", 'M', ItemList.Hull_EV, 'B', OrePrefixes.pipeLarge.get(Materials.StainlessSteel), 'C', OrePrefixes.circuit.get(Materials.Elite), 'F', ItemList.Robot_Arm_EV, 'T', ItemList.Energy_LapotronicOrb});
        ItemList.Processing_Array_T2.set(new GT_MetaTileEntity_ProcessingArray2(12073, "multimachine.processingarray.tier.06", "Processing Array Mk 2").getStackForm(1L));
        GT_ModHandler.addCraftingRecipe(ItemList.Processing_Array_T2.get(1L, new Object[0]), bitsd, new Object[]{"CTC", "FMF", "CBC", 'M', ItemList.Processing_Array_T1, 'B', OrePrefixes.pipeLarge.get(Materials.TungstenSteel), 'C', OrePrefixes.circuit.get(Materials.Ultimate), 'F', ItemList.Robot_Arm_LuV, 'T', ItemList.Energy_LapotronicOrb2});
        ItemList.Processing_Array_T3.set(new GT_MetaTileEntity_ProcessingArray3(12074, "multimachine.processingarray.tier.08", "Processing Array Mk 3").getStackForm(1L));
        GT_ModHandler.addCraftingRecipe(ItemList.Processing_Array_T3.get(1L, new Object[0]), bitsd, new Object[]{"CTC", "FMF", "CBC", 'M', ItemList.Processing_Array_T2, 'B', OrePrefixes.pipeLarge.get(Materials.NiobiumTitanium), 'C', OrePrefixes.circuit.get(Materials.Infinite), 'F', ItemList.Robot_Arm_UV, 'T', ItemList.Energy_Module});
        
        
        ItemList.Distillation_Tower.set(new GT_MetaTileEntity_DistillationTower(1126, "multimachine.distillationtower", "Distillation Tower").getStackForm(1L));
        GT_ModHandler.addCraftingRecipe(ItemList.Distillation_Tower.get(1L, new Object[0]), bitsd, new Object[]{"CBC", "FMF", "CBC", 'M', ItemList.Hull_EV, 'B', OrePrefixes.pipeLarge.get(Materials.StainlessSteel), 'C', OrePrefixes.circuit.get(Materials.Data), 'F', ItemList.Electric_Pump_EV});

        ItemList.LargeSteamTurbine.set(new GT_MetaTileEntity_LargeTurbine_Steam(1131, "multimachine.largeturbine", "Large Steam Turbine").getStackForm(1L));
        ItemList.LargeGasTurbine.set(new GT_MetaTileEntity_LargeTurbine_Gas(1151, "multimachine.largegasturbine", "Large Gas Turbine").getStackForm(1L));
        ItemList.LargeHPSteamTurbine.set(new GT_MetaTileEntity_LargeTurbine_HPSteam(1152, "multimachine.largehpturbine", "Large HP Steam Turbine").getStackForm(1L));
        ItemList.LargePlasmaTurbine.set(new GT_MetaTileEntity_LargeTurbine_Plasma(1153, "multimachine.largeplasmaturbine", "Large Plasma Generator").getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(ItemList.LargeSteamTurbine.get(1L, new Object[0]), bitsd, new Object[]{"CPC", aTextPlateMotor, "BPB", 'M', ItemList.Hull_HV, 'B', OrePrefixes.pipeLarge.get(Materials.Steel), 'C', OrePrefixes.circuit.get(Materials.Advanced), 'P', OrePrefixes.gearGt.get(Materials.Steel)});
        GT_ModHandler.addCraftingRecipe(ItemList.LargeGasTurbine.get(1L, new Object[0]), bitsd, new Object[]{"CPC", aTextPlateMotor, "BPB", 'M', ItemList.Hull_EV, 'B', OrePrefixes.pipeLarge.get(Materials.StainlessSteel), 'C', OrePrefixes.circuit.get(Materials.Data), 'P', OrePrefixes.gearGt.get(Materials.StainlessSteel)});
        GT_ModHandler.addCraftingRecipe(ItemList.LargeHPSteamTurbine.get(1L), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"CPC", "PMP", "BPB", 'M', ItemList.Hull_IV, 'B', OrePrefixes.pipeLarge.get(Materials.TungstenSteel), 'C', OrePrefixes.circuit.get(Materials.Master), 'P', OrePrefixes.gearGt.get(Materials.TungstenSteel)});
        GT_ModHandler.addCraftingRecipe(ItemList.LargePlasmaTurbine.get(1L), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"CPC", "PMP", "BPB", 'M', ItemList.Hull_UV, 'B', OrePrefixes.pipeHuge.get(Materials.Naquadah), 'C',  OrePrefixes.circuit.get(Materials.Ultimate), 'P', OrePrefixes.gearGt.get(Materials.NaquadahAlloy)});


        ItemList.Pump_LV.set(new GT_MetaTileEntity_Pump(1140, "basicmachine.pump.tier.01", "Basic Pump", 1).getStackForm(1L));
        ItemList.Pump_MV.set(new GT_MetaTileEntity_Pump(1141, "basicmachine.pump.tier.02", "Advanced Pump", 2).getStackForm(1L));
        ItemList.Pump_HV.set(new GT_MetaTileEntity_Pump(1142, "basicmachine.pump.tier.03", "Advanced Pump II", 3).getStackForm(1L));
        ItemList.Pump_EV.set(new GT_MetaTileEntity_Pump(1143, "basicmachine.pump.tier.04", "Advanced Pump III", 4).getStackForm(1L));
        ItemList.Pump_IV.set(new GT_MetaTileEntity_Pump(1144, "basicmachine.pump.tier.05", "Advanced Pump IV", 5).getStackForm(1L));
        ItemList.Pump_LuV.set(new GT_MetaTileEntity_Pump(1132, "basicmachine.pump.tier.06", "Advanced Pump V", 6).getStackForm(1L));
        ItemList.Pump_ZPM.set(new GT_MetaTileEntity_Pump(1133, "basicmachine.pump.tier.07", "Advanced Pump VI", 7).getStackForm(1L));
        ItemList.Pump_UV.set(new GT_MetaTileEntity_Pump(1134, "basicmachine.pump.tier.08", "Advanced Pump VII", 8).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(ItemList.Pump_LV.get(1L, new Object[0]), bitsd, new Object[]{"CPC", aTextPlateMotor, "BPB", 'M', ItemList.Hull_LV, 'B', OrePrefixes.pipeLarge.get(Materials.Bronze), 'C', OrePrefixes.circuit.get(Materials.Basic), 'P', ItemList.Electric_Pump_LV});
        GT_ModHandler.addCraftingRecipe(ItemList.Pump_MV.get(1L, new Object[0]), bitsd, new Object[]{"CPC", aTextPlateMotor, "BPB", 'M', ItemList.Hull_MV, 'B', OrePrefixes.pipeLarge.get(Materials.Steel), 'C', OrePrefixes.circuit.get(Materials.Good), 'P', ItemList.Electric_Pump_MV});
        GT_ModHandler.addCraftingRecipe(ItemList.Pump_HV.get(1L, new Object[0]), bitsd, new Object[]{"CPC", aTextPlateMotor, "BPB", 'M', ItemList.Hull_HV, 'B', OrePrefixes.pipeLarge.get(Materials.StainlessSteel), 'C', OrePrefixes.circuit.get(Materials.Advanced), 'P', ItemList.Electric_Pump_HV});
        GT_ModHandler.addCraftingRecipe(ItemList.Pump_EV.get(1L, new Object[0]), bitsd, new Object[]{"CPC", aTextPlateMotor, "BPB", 'M', ItemList.Hull_EV, 'B', OrePrefixes.pipeLarge.get(Materials.Titanium), 'C', OrePrefixes.circuit.get(Materials.Data), 'P', ItemList.Electric_Pump_EV});
        GT_ModHandler.addCraftingRecipe(ItemList.Pump_IV.get(1L, new Object[0]), bitsd, new Object[]{"CPC", aTextPlateMotor, "BPB", 'M', ItemList.Hull_IV, 'B', OrePrefixes.pipeLarge.get(Materials.TungstenSteel), 'C', OrePrefixes.circuit.get(Materials.Elite), 'P', ItemList.Electric_Pump_IV});
        GT_ModHandler.addCraftingRecipe(ItemList.Pump_LuV.get(1L, new Object[0]), bitsd, new Object[]{"CPC", aTextPlateMotor, "BPB", 'M', ItemList.Hull_LuV, 'B', OrePrefixes.pipeLarge.get(Materials.NiobiumTitanium), 'C', OrePrefixes.circuit.get(Materials.Master), 'P', ItemList.Electric_Pump_LuV});
        GT_ModHandler.addCraftingRecipe(ItemList.Pump_ZPM.get(1L, new Object[0]), bitsd, new Object[]{"CPC", aTextPlateMotor, "BPB", 'M', ItemList.Hull_ZPM, 'B', OrePrefixes.pipeLarge.get(Materials.Enderium), 'C', OrePrefixes.circuit.get(Materials.Ultimate), 'P', ItemList.Electric_Pump_ZPM});
        GT_ModHandler.addCraftingRecipe(ItemList.Pump_UV.get(1L, new Object[0]), bitsd, new Object[]{"CPC", aTextPlateMotor, "BPB", 'M', ItemList.Hull_UV, 'B', OrePrefixes.pipeLarge.get(Materials.Naquadah), 'C', OrePrefixes.circuit.get(Materials.Superconductor), 'P', ItemList.Electric_Pump_UV});

        ItemList.Machine_LV_WaterGenerator.set(new GT_MetaTileEntity_WaterGenerator(682, "basicmachine.e_watergenerator.tier.01", "Basic Electric Water Condenser", 1, null).getStackForm(1L));
        ItemList.Machine_MV_WaterGenerator.set(new GT_MetaTileEntity_WaterGenerator(683, "basicmachine.e_watergenerator.tier.02", "Advanced Electric Water Condenser", 2, null).getStackForm(1L));
        ItemList.Machine_HV_WaterGenerator.set(new GT_MetaTileEntity_WaterGenerator(684, "basicmachine.e_watergenerator.tier.03", "Advanced Electric Water Condenser II", 3, null).getStackForm(1L));
        ItemList.Machine_EV_WaterGenerator.set(new GT_MetaTileEntity_WaterGenerator(685, "basicmachine.e_watergenerator.tier.04", "Advanced Electric Water Condenser III", 4, null).getStackForm(1L));
        ItemList.Machine_IV_WaterGenerator.set(new GT_MetaTileEntity_WaterGenerator(686, "basicmachine.e_watergenerator.tier.05", "Advanced Electric Water Condenser IV", 5, null).getStackForm(1L));
        ItemList.Machine_LuV_WaterGenerator.set(new GT_MetaTileEntity_WaterGenerator(687, "basicmachine.e_watergenerator.tier.06", "Advanced Electric Water Condenser V", 6, null).getStackForm(1L));
        ItemList.Machine_ZPM_WaterGenerator.set(new GT_MetaTileEntity_WaterGenerator(688, "basicmachine.e_watergenerator.tier.07", "Advanced Electric Water Condenser VI", 7, null).getStackForm(1L));
        ItemList.Machine_UV_WaterGenerator.set(new GT_MetaTileEntity_WaterGenerator(689, "basicmachine.e_watergenerator.tier.08", "Advanced Electric Water Condenser VII", 8, null).getStackForm(1L));
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_LV_WaterGenerator.get(1L, new Object[0]), bitsd, new Object[]{"CCC", "PMP", "WEW", 'M', ItemList.Hull_LV, 'E', OrePrefixes.circuit.get(Materials.Basic), 'P', ItemList.Pump_LV, 'C', new ItemStack(Blocks.glass, 1), 'W', OrePrefixes.plate.get(Materials.Steel)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_MV_WaterGenerator.get(1L, new Object[0]), bitsd, new Object[]{"CCC", "PMP", "WEW", 'M', ItemList.Hull_MV, 'E', OrePrefixes.circuit.get(Materials.Good), 'P', ItemList.Pump_MV, 'C', new ItemStack(Blocks.glass, 1), 'W', OrePrefixes.plate.get(Materials.Aluminium)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_HV_WaterGenerator.get(1L, new Object[0]), bitsd, new Object[]{"CCC", "PMP", "WEW", 'M', ItemList.Hull_HV, 'E', OrePrefixes.circuit.get(Materials.Advanced), 'P', ItemList.Pump_HV, 'C', new ItemStack(Blocks.glass, 1), 'W', OrePrefixes.plate.get(Materials.StainlessSteel)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_EV_WaterGenerator.get(1L, new Object[0]), bitsd, new Object[]{"CCC", "PMP", "WEW", 'M', ItemList.Hull_EV, 'E', OrePrefixes.circuit.get(Materials.Data), 'P', ItemList.Pump_EV, 'C', new ItemStack(Blocks.glass, 1), 'W', OrePrefixes.plate.get(Materials.VanadiumSteel)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_IV_WaterGenerator.get(1L, new Object[0]), bitsd, new Object[]{"CCC", "PMP", "WEW", 'M', ItemList.Hull_IV, 'E', OrePrefixes.circuit.get(Materials.Elite), 'P', ItemList.Pump_IV, 'C', Ic2Items.reinforcedGlass, 'W', OrePrefixes.plate.get(Materials.TungstenCarbide)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_LuV_WaterGenerator.get(1L, new Object[0]), bitsd, new Object[]{"CCC", "PMP", "WEW", 'M', ItemList.Hull_LuV, 'E', OrePrefixes.circuit.get(Materials.Master), 'P', ItemList.Pump_LuV, 'C', Ic2Items.reinforcedGlass, 'W', OrePrefixes.plate.get(Materials.HSSS)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_ZPM_WaterGenerator.get(1L, new Object[0]), bitsd, new Object[]{"CCC", "PMP", "WEW", 'M', ItemList.Hull_ZPM, 'E', OrePrefixes.circuit.get(Materials.Ultimate), 'P', ItemList.Pump_ZPM, 'C', Ic2Items.reinforcedGlass, 'W', OrePrefixes.plate.get(Materials.HSSE)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_UV_WaterGenerator.get(1L, new Object[0]), bitsd, new Object[]{"CCC", "PMP", "WEW", 'M', ItemList.Hull_UV, 'E', OrePrefixes.circuit.get(Materials.Superconductor), 'P', ItemList.Pump_UV, 'C', Ic2Items.reinforcedGlass, 'W', OrePrefixes.plate.get(Materials.Tritanium)});

        ItemList.Teleporter.set(new GT_MetaTileEntity_Teleporter(1145, "basicmachine.teleporter", "Teleporter", 9).getStackForm(1L));
        GT_ModHandler.addCraftingRecipe(ItemList.Teleporter.get(1L, new Object[0]), bitsd, new Object[]{"CPC", aTextPlateMotor, "BCB", 'M', ItemList.Hull_MAX, 'B', ItemList.Tool_DataOrb, 'C', OrePrefixes.circuit.get(Materials.Infinite), 'P', ItemList.Field_Generator_UHV});

        ItemList.MobRep_LV.set(new GT_MetaTileEntity_MonsterRepellent(1146, "basicmachine.mobrep.tier.01", "Basic Monster Repellator", 1).getStackForm(1L));
        ItemList.MobRep_MV.set(new GT_MetaTileEntity_MonsterRepellent(1147, "basicmachine.mobrep.tier.02", "Advanced Monster Repellator", 2).getStackForm(1L));
        ItemList.MobRep_HV.set(new GT_MetaTileEntity_MonsterRepellent(1148, "basicmachine.mobrep.tier.03", "Advanced Monster Repellator II", 3).getStackForm(1L));
        ItemList.MobRep_EV.set(new GT_MetaTileEntity_MonsterRepellent(1149, "basicmachine.mobrep.tier.04", "Advanced Monster Repellator III", 4).getStackForm(1L));
        ItemList.MobRep_IV.set(new GT_MetaTileEntity_MonsterRepellent(1150, "basicmachine.mobrep.tier.05", "Advanced Monster Repellator IV", 5).getStackForm(1L));
        ItemList.MobRep_LuV.set(new GT_MetaTileEntity_MonsterRepellent(1135, "basicmachine.mobrep.tier.06", "Advanced Monster Repellator V", 6).getStackForm(1L));
        ItemList.MobRep_ZPM.set(new GT_MetaTileEntity_MonsterRepellent(1136, "basicmachine.mobrep.tier.07", "Advanced Monster Repellator VI", 7).getStackForm(1L));
        ItemList.MobRep_UV.set(new GT_MetaTileEntity_MonsterRepellent(1137, "basicmachine.mobrep.tier.08", "Advanced Monster Repellator VII", 8).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(ItemList.MobRep_LV.get(1L, new Object[0]), bitsd, new Object[]{"EEE", " M ", "CCC", 'M', ItemList.Hull_LV, 'E', ItemList.Emitter_LV.get(1L, new Object[0]), 'C', OrePrefixes.circuit.get(Materials.Basic)});
        GT_ModHandler.addCraftingRecipe(ItemList.MobRep_MV.get(1L, new Object[0]), bitsd, new Object[]{"EEE", " M ", "CCC", 'M', ItemList.Hull_MV, 'E', ItemList.Emitter_MV.get(1L, new Object[0]), 'C', OrePrefixes.circuit.get(Materials.Good)});
        GT_ModHandler.addCraftingRecipe(ItemList.MobRep_HV.get(1L, new Object[0]), bitsd, new Object[]{"EEE", " M ", "CCC", 'M', ItemList.Hull_HV, 'E', ItemList.Emitter_HV.get(1L, new Object[0]), 'C', OrePrefixes.circuit.get(Materials.Advanced)});
        GT_ModHandler.addCraftingRecipe(ItemList.MobRep_EV.get(1L, new Object[0]), bitsd, new Object[]{"EEE", " M ", "CCC", 'M', ItemList.Hull_EV, 'E', ItemList.Emitter_EV.get(1L, new Object[0]), 'C', OrePrefixes.circuit.get(Materials.Data)});
        GT_ModHandler.addCraftingRecipe(ItemList.MobRep_IV.get(1L, new Object[0]), bitsd, new Object[]{"EEE", " M ", "CCC", 'M', ItemList.Hull_IV, 'E', ItemList.Emitter_IV.get(1L, new Object[0]), 'C', OrePrefixes.circuit.get(Materials.Elite)});
        GT_ModHandler.addCraftingRecipe(ItemList.MobRep_LuV.get(1L, new Object[0]), bitsd, new Object[]{"EEE", " M ", "CCC", 'M', ItemList.Hull_LuV, 'E', ItemList.Emitter_LuV.get(1L, new Object[0]), 'C', OrePrefixes.circuit.get(Materials.Master)});
        GT_ModHandler.addCraftingRecipe(ItemList.MobRep_ZPM.get(1L, new Object[0]), bitsd, new Object[]{"EEE", " M ", "CCC", 'M', ItemList.Hull_ZPM, 'E', ItemList.Emitter_ZPM.get(1L, new Object[0]), 'C', OrePrefixes.circuit.get(Materials.Ultimate)});
        GT_ModHandler.addCraftingRecipe(ItemList.MobRep_UV.get(1L, new Object[0]), bitsd, new Object[]{"EEE", " M ", "CCC", 'M', ItemList.Hull_UV, 'E', ItemList.Emitter_UV.get(1L, new Object[0]), 'C', OrePrefixes.circuit.get(Materials.Superconductor)});

        ItemList.Machine_Multi_HeatExchanger.set(new GT_MetaTileEntity_HeatExchanger(1154, "multimachine.heatexchanger", "Large Heat Exchanger").getStackForm(1L));
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_Multi_HeatExchanger.get(1L, new Object[0]), bitsd, new Object[]{aTextWireCoil, aTextCableHull, aTextWireCoil, 'M', ItemList.Casing_Pipe_Titanium, 'C', OrePrefixes.pipeMedium.get(Materials.Titanium), 'W', ItemList.Electric_Pump_EV});


        ItemList.Charcoal_Pile.set(new GT_MetaTileEntity_Charcoal_Pit(1155, "multimachine.charcoalpile", "Charcoal Pile Igniter").getStackForm(1));
        GT_ModHandler.addCraftingRecipe(ItemList.Charcoal_Pile.get(1L, new Object[0]), bitsd, new Object[]{"EME", "CCC", 'M', ItemList.Hull_Bronze_Bricks, 'E', OrePrefixes.nugget.get(Materials.WroughtIron), 'C', new ItemStack(Items.flint, 1)});

        ItemList.Seismic_Prospector_LV.set(new GT_MetaTileEntity_SeismicProspector(1156, "basicmachine.seismicprospector.01", "Seismic Prospector LV", 1).getStackForm(1));
        ItemList.Seismic_Prospector_MV.set(new GT_MetaTileEntity_SeismicProspector(2100, "basicmachine.seismicprospector.02", "Seismic Prospector MV", 2).getStackForm(1));
        ItemList.Seismic_Prospector_HV.set(new GT_MetaTileEntity_SeismicProspector(2101, "basicmachine.seismicprospector.03", "Seismic Prospector HV", 3).getStackForm(1));
        
        ItemList.Seismic_Prospector_Adv_LV.set(new GT_MetaTileEntity_AdvSeismicProspector(2102, "basicmachine.seismicprospector.07", "Advanced Seismic Prospector LV", 1, 5*16/2, 6).getStackForm(1));
        ItemList.Seismic_Prospector_Adv_MV.set(new GT_MetaTileEntity_AdvSeismicProspector(2103, "basicmachine.seismicprospector.06", "Advanced Seismic Prospector MV", 2, 7*16/2, 6).getStackForm(1));
        ItemList.Seismic_Prospector_Adv_HV.set(new GT_MetaTileEntity_AdvSeismicProspector(2104, "basicmachine.seismicprospector.05", "Advanced Seismic Prospector HV", 3, 9*16/2, 6).getStackForm(1));
        ItemList.Seismic_Prospector_Adv_EV.set(new GT_MetaTileEntity_AdvSeismicProspector(1173, "basicmachine.seismicprospector.04", "Advanced Seismic Prospector EV", 4, 128, 6).getStackForm(1));
        
        GT_ModHandler.addShapelessCraftingRecipe(ItemList.Seismic_Prospector_Adv_LV.get(1L, new Object[0]), bits, new Object[]{ItemList.Seismic_Prospector_LV});
        GT_ModHandler.addShapelessCraftingRecipe(ItemList.Seismic_Prospector_Adv_MV.get(1L, new Object[0]), bits, new Object[]{ItemList.Seismic_Prospector_MV});
        GT_ModHandler.addShapelessCraftingRecipe(ItemList.Seismic_Prospector_Adv_HV.get(1L, new Object[0]), bits, new Object[]{ItemList.Seismic_Prospector_HV});
        GT_ModHandler.addCraftingRecipe(ItemList.Seismic_Prospector_Adv_LV.get(1L, new Object[0]), bitsd, new Object[]{"WWW", "EME", "CXC", 'M', ItemList.Hull_LV, 'W', OrePrefixes.plateDouble.get(Materials.Steel), 'E', OrePrefixes.circuit.get(Materials.Basic), 'C', ItemList.Sensor_LV, 'X', OrePrefixes.cableGt02.get(Materials.Tin)});
        GT_ModHandler.addCraftingRecipe(ItemList.Seismic_Prospector_Adv_MV.get(1L, new Object[0]), bitsd, new Object[]{"WWW", "EME", "CXC", 'M', ItemList.Hull_MV, 'W', OrePrefixes.plateDouble.get(Materials.BlackSteel), 'E', OrePrefixes.circuit.get(Materials.Good), 'C', ItemList.Sensor_MV, 'X', OrePrefixes.cableGt02.get(Materials.Copper)});
        GT_ModHandler.addCraftingRecipe(ItemList.Seismic_Prospector_Adv_HV.get(1L, new Object[0]), bitsd, new Object[]{"WWW", "EME", "CXC", 'M', ItemList.Hull_HV, 'W', OrePrefixes.plateDouble.get(Materials.StainlessSteel), 'E', OrePrefixes.circuit.get(Materials.Advanced), 'C', ItemList.Sensor_HV, 'X', OrePrefixes.cableGt04.get(Materials.Gold)});
        GT_ModHandler.addCraftingRecipe(ItemList.Seismic_Prospector_Adv_EV.get(1L, new Object[0]), bitsd, new Object[] { "WWW", "EME", "CXC", 'M', ItemList.Hull_EV, 'W', OrePrefixes.plateDouble.get(Materials.VanadiumSteel),'E', OrePrefixes.circuit.get(Materials.Data), 'C', ItemList.Sensor_EV, 'X', OrePrefixes.cableGt04.get(Materials.Aluminium) });
        
		//Solar Panels 1-128 EU
        GT_ModHandler.addCraftingRecipe(ItemList.Cover_SolarPanel.get(1L), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"SGS", "CPC", "TRT", 'C', OrePrefixes.circuit.get(Materials.Basic), 'G', GT_ModHandler.getIC2Item("reinforcedGlass", 1L), 'P', OrePrefixes.plateAlloy.get(Materials.Carbon), 'S', ItemList.Circuit_Silicon_Wafer, 'T', OrePrefixes.wireGt01.get(Materials.RedAlloy), 'R', OrePrefixes.plateDouble.get(Materials.Steel)});
        GT_ModHandler.addCraftingRecipe(ItemList.Cover_SolarPanel_8V.get(1L), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"SGS", "CRC", "PAP", 'C', OrePrefixes.circuit.get(Materials.Good), 'G', ItemList.Cover_SolarPanel.get(1L), 'P', OrePrefixes.wireGt01.get(Materials.Tin), 'S', ItemList.Circuit_Silicon_Wafer, 'R', OrePrefixes.plate.get(Materials.GalliumArsenide), 'A', OrePrefixes.plateDouble.get(Materials.Aluminium)});
        GT_ModHandler.addCraftingRecipe(ItemList.Cover_SolarPanel_LV.get(1L), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"SGS", "CRC","PAP", 'C', OrePrefixes.circuit.get(Materials.Advanced), 'G', ItemList.Cover_SolarPanel_8V.get(1L), 'P', OrePrefixes.wireGt01.get(Materials.Cobalt), 'S', ItemList.Circuit_Silicon_Wafer2,'R', OrePrefixes.plateDouble.get(Materials.GalliumArsenide), 'A', OrePrefixes.plateDouble.get(Materials.StainlessSteel)});
        GT_ModHandler.addCraftingRecipe(ItemList.Cover_SolarPanel_MV.get(1L), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"SGS", "CRC", "PAP", 'C', OrePrefixes.circuit.get(Materials.Data), 'G', ItemList.Cover_SolarPanel_LV.get(1L), 'P', OrePrefixes.wireGt01.get(Materials.SuperconductorMV), 'S', ItemList.Circuit_Silicon_Wafer2, 'R', OrePrefixes.plateTriple.get(Materials.GalliumArsenide), 'A', OrePrefixes.plateDouble.get(Materials.Titanium)});

        ItemList.OilDrill1.set(new GT_MetaTileEntity_OilDrill1(1157, "multimachine.oildrill1", "Oil Drilling Rig").getStackForm(1));
        ItemList.OilDrill2.set(new GT_MetaTileEntity_OilDrill2(133, "multimachine.oildrill2", "Oil Drilling Rig II").getStackForm(1));
        ItemList.OilDrill3.set(new GT_MetaTileEntity_OilDrill3(134, "multimachine.oildrill3", "Oil Drilling Rig III").getStackForm(1));
        GT_ModHandler.addCraftingRecipe(ItemList.OilDrill1.get(1L, new Object[0]), bitsd, new Object[]{"WWW", "EME", "CCC", 'M', ItemList.Hull_MV, 'W', OrePrefixes.frameGt.get(Materials.Steel), 'E', OrePrefixes.circuit.get(Materials.Good), 'C', ItemList.Electric_Motor_MV});
        GT_ModHandler.addCraftingRecipe(ItemList.OilDrill2.get(1L, new Object[0]), bitsd, new Object[]{"WWW", "EME", "CCC", 'M', ItemList.OilDrill1, 'W', OrePrefixes.frameGt.get(Materials.Titanium), 'E', OrePrefixes.circuit.get(Materials.Advanced), 'C', ItemList.Electric_Motor_HV});
        GT_ModHandler.addCraftingRecipe(ItemList.OilDrill3.get(1L, new Object[0]), bitsd, new Object[]{"WWW", "EME", "CCC", 'M', ItemList.OilDrill2, 'W', OrePrefixes.frameGt.get(Materials.TungstenSteel), 'E', OrePrefixes.circuit.get(Materials.Data), 'C', ItemList.Electric_Motor_EV});
        
        ItemList.ConcreteBackfiller1.set(new GT_MetaTileEntity_ConcreteBackfiller1(135, "multimachine.concretebackfiller1", "Concrete Backfiller").getStackForm(1));
        ItemList.ConcreteBackfiller2.set(new GT_MetaTileEntity_ConcreteBackfiller2(136, "multimachine.concretebackfiller3", "Advanced Concrete Backfiller").getStackForm(1));
        GT_ModHandler.addCraftingRecipe(ItemList.ConcreteBackfiller1.get(1L, new Object[0]), bitsd, new Object[]{"WPW", "EME", "CQC", 'M', ItemList.Hull_MV, 'W', OrePrefixes.frameGt.get(Materials.Steel), 'E', OrePrefixes.circuit.get(Materials.Good), 'C', ItemList.Electric_Motor_MV, 'P', OrePrefixes.pipeLarge.get(Materials.Steel), 'Q', ItemList.Electric_Pump_MV});
        GT_ModHandler.addCraftingRecipe(ItemList.ConcreteBackfiller2.get(1L, new Object[0]), bitsd, new Object[]{"WPW", "EME", "CQC", 'M', ItemList.ConcreteBackfiller1, 'W', OrePrefixes.frameGt.get(Materials.Titanium), 'E', OrePrefixes.circuit.get(Materials.Data), 'C', ItemList.Electric_Motor_EV, 'P', OrePrefixes.pipeLarge.get(Materials.Steel), 'Q', ItemList.Electric_Pump_EV});

        ItemList.OreDrill1.set(new GT_MetaTileEntity_OreDrillingPlant1(1158, "multimachine.oredrill1", "Ore Drilling Plant").getStackForm(1));
        GT_ModHandler.addCraftingRecipe(ItemList.OreDrill1.get(1L), bitsd, new Object[]{"WWW", "EME", "CCC", 'M', ItemList.Hull_EV, 'W', OrePrefixes.frameGt.get(Materials.Titanium), 'E', OrePrefixes.circuit.get(Materials.Data), 'C', ItemList.Electric_Motor_EV});

        ItemList.OreDrill2.set(new GT_MetaTileEntity_OreDrillingPlant2(1177, "multimachine.oredrill2", "Ore Drilling Plant II").getStackForm(1));
        GT_ModHandler.addCraftingRecipe(ItemList.OreDrill2.get(1L), bitsd, new Object[]{"WWW", "EME", "CCC", 'M', ItemList.OreDrill1, 'W', OrePrefixes.frameGt.get(Materials.TungstenSteel), 'E', OrePrefixes.circuit.get(Materials.Elite), 'C', ItemList.Electric_Motor_IV});

        ItemList.OreDrill3.set(new GT_MetaTileEntity_OreDrillingPlant3(1178, "multimachine.oredrill3", "Ore Drilling Plant III").getStackForm(1));
        GT_ModHandler.addCraftingRecipe(ItemList.OreDrill3.get(1L), bitsd, new Object[]{"WWW", "EME", "CCC", 'M', ItemList.OreDrill2, 'W', OrePrefixes.frameGt.get(Materials.Osmiridium), 'E', OrePrefixes.circuit.get(Materials.Master), 'C', ItemList.Electric_Motor_LuV});

        ItemList.OreDrill4.set(new GT_MetaTileEntity_OreDrillingPlant4(1179, "multimachine.oredrill4", "Ore Drilling Plant IV").getStackForm(1));
        GT_ModHandler.addCraftingRecipe(ItemList.OreDrill4.get(1L), bitsd, new Object[]{"WWW", "EME", "CCC", 'M', ItemList.OreDrill3, 'W', OrePrefixes.frameGt.get(Materials.Tritanium), 'E', OrePrefixes.circuit.get(Materials.Ultimate), 'C', ItemList.Electric_Motor_ZPM});

        ItemList.PyrolyseOven.set(new GT_MetaTileEntity_PyrolyseOven(1159, "multimachine.pyro", "Pyrolyse Oven").getStackForm(1));
        GT_ModHandler.addCraftingRecipe(ItemList.PyrolyseOven.get(1L, new Object[0]), bitsd, new Object[]{"WEP", "EME", "WCP", 'M', ItemList.Hull_MV, 'W', ItemList.Electric_Piston_MV, 'P', OrePrefixes.wireGt04.get(Materials.Cupronickel), 'E', OrePrefixes.circuit.get(Materials.Good), 'C', ItemList.Electric_Pump_MV});


        ItemList.OilCracker.set(new GT_MetaTileEntity_OilCracker(1160, "multimachine.cracker", "Oil Cracking Unit").getStackForm(1));
        GT_ModHandler.addCraftingRecipe(ItemList.OilCracker.get(1L, new Object[0]), bitsd, new Object[]{aTextWireCoil, "EME", aTextWireCoil, 'M', ItemList.Hull_HV, 'W', ItemList.Casing_Coil_Cupronickel, 'E', OrePrefixes.circuit.get(Materials.Advanced), 'C', ItemList.Electric_Pump_HV});

        ItemList.MicroTransmitter_HV.set(new GT_MetaTileEntity_MicrowaveEnergyTransmitter(1161, "basicmachine.microtransmitter.03", "HV Microwave Energy Transmitter", 3).getStackForm(1L));
        ItemList.MicroTransmitter_EV.set(new GT_MetaTileEntity_MicrowaveEnergyTransmitter(1162, "basicmachine.microtransmitter.04", "EV Microwave Energy Transmitter", 4).getStackForm(1L));
        ItemList.MicroTransmitter_IV.set(new GT_MetaTileEntity_MicrowaveEnergyTransmitter(1163, "basicmachine.microtransmitter.05", "IV Microwave Energy Transmitter", 5).getStackForm(1L));
        ItemList.MicroTransmitter_LUV.set(new GT_MetaTileEntity_MicrowaveEnergyTransmitter(1164, "basicmachine.microtransmitter.06", "LuV Microwave Energy Transmitter", 6).getStackForm(1L));
        ItemList.MicroTransmitter_ZPM.set(new GT_MetaTileEntity_MicrowaveEnergyTransmitter(1165, "basicmachine.microtransmitter.07", "ZPM Microwave Energy Transmitter", 7).getStackForm(1L));
        ItemList.MicroTransmitter_UV.set(new GT_MetaTileEntity_MicrowaveEnergyTransmitter(1166, "basicmachine.microtransmitter.08", "UV Microwave Energy Transmitter", 8).getStackForm(1L));
        GT_ModHandler.addCraftingRecipe(ItemList.MicroTransmitter_HV.get(1L, new Object[0]), bitsd, new Object[]{"CPC", aTextCableHull, "GBG", 'M', ItemList.Hull_HV, 'B',  ItemList.Battery_RE_HV_Lithium, 'C', ItemList.Emitter_HV, 'G', OrePrefixes.circuit.get(Materials.Advanced), 'P', ItemList.Field_Generator_HV});
        GT_ModHandler.addCraftingRecipe(ItemList.MicroTransmitter_EV.get(1L, new Object[0]), bitsd, new Object[]{"CPC", aTextCableHull, "GBG", 'M', ItemList.Hull_EV, 'B', OrePrefixes.battery.get(Materials.Master), 'C', ItemList.Emitter_EV, 'G', OrePrefixes.circuit.get(Materials.Data), 'P', ItemList.Field_Generator_EV});
        GT_ModHandler.addCraftingRecipe(ItemList.MicroTransmitter_IV.get(1L, new Object[0]), bitsd, new Object[]{"CPC", aTextCableHull, "GBG", 'M', ItemList.Hull_IV, 'B', ItemList.Energy_LapotronicOrb, 'C', ItemList.Emitter_IV, 'G', OrePrefixes.circuit.get(Materials.Elite), 'P', ItemList.Field_Generator_IV});GT_ModHandler.addCraftingRecipe(ItemList.MicroTransmitter_LUV.get(1L, new Object[0]), bitsd, new Object[]{"CPC", aTextCableHull, "GBG", 'M', ItemList.Hull_LuV, 'B', ItemList.Energy_LapotronicOrb2, 'C', ItemList.Emitter_IV, 'G', OrePrefixes.circuit.get(Materials.Master), 'P', ItemList.Field_Generator_IV});
        GT_ModHandler.addCraftingRecipe(ItemList.MicroTransmitter_LUV.get(1L, new Object[0]), bitsd, new Object[]{"CPC", aTextCableHull, "GBG", 'M', ItemList.Hull_LuV, 'B', ItemList.Energy_LapotronicOrb2, 'C', ItemList.Emitter_LuV, 'G', OrePrefixes.circuit.get(Materials.Master), 'P', ItemList.Field_Generator_LuV});
        GT_ModHandler.addCraftingRecipe(ItemList.MicroTransmitter_ZPM.get(1L, new Object[0]), bitsd, new Object[]{"CPC", aTextCableHull, "GBG", 'M', ItemList.Hull_ZPM, 'B', ItemList.Energy_Module, 'C', ItemList.Emitter_ZPM, 'G', OrePrefixes.circuit.get(Materials.Ultimate), 'P', ItemList.Field_Generator_ZPM});
        GT_ModHandler.addCraftingRecipe(ItemList.MicroTransmitter_UV.get(1L, new Object[0]), bitsd, new Object[]{"CPC", aTextCableHull, "GBG", 'M', ItemList.Hull_UV, 'B', ItemList.Energy_Cluster, 'C', ItemList.Emitter_UV, 'G', OrePrefixes.circuit.get(Materials.Superconductor), 'P', ItemList.Field_Generator_UV});
        
        
        //ItemList.CuringOven.set(new GT_MetaTileEntity_CuringOven(1166, "basicmachine.curingoven", "Curing Oven", 1).getStackForm(1));
        //GT_ModHandler.addCraftingRecipe(ItemList.CuringOven.get(1L, new Object[0]), bitsd, new Object[]{"CWC", aTextCableHull, aTextMotorWire, 'M', ItemList.Hull_LV, 'E', OrePrefixes.circuit.get(Materials.Basic), 'W', GT_OreDictUnificator.get(OrePrefixes.cable, Materials.Tin, 1), 'C', GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Cupronickel, 1)});

        ItemList.Machine_Multi_Assemblyline.set(new GT_MetaTileEntity_AssemblyLine(1170, "multimachine.assemblyline", "Assembly Line").getStackForm(1L));
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_Multi_Assemblyline.get(1L, new Object[0]), bitsd, new Object[]{aTextWireCoil, "EME", aTextWireCoil, 'M', ItemList.Hull_IV, 'W', ItemList.Casing_Assembler, 'E', OrePrefixes.circuit.get(Materials.Elite), 'C', ItemList.Robot_Arm_IV});

        ItemList.Machine_Multi_DieselEngine.set(new GT_MetaTileEntity_DieselEngine(1171, "multimachine.dieselengine", "Combustion Engine Mk1").getStackForm(1L));
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_Multi_DieselEngine.get(1L, new Object[0]), bitsd, new Object[]{"PCP", "EME", "GWG", 'M', ItemList.Hull_EV, 'P', ItemList.Electric_Piston_EV, 'E', ItemList.Electric_Motor_EV, 'C', OrePrefixes.circuit.get(Materials.Elite), 'W', OrePrefixes.cableGt01.get(Materials.TungstenSteel), 'G', OrePrefixes.gearGt.get(Materials.Titanium)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_EngineIntake.get(2L, new Object[0]), bitsd, new Object[]{"PhP", "RFR", aTextPlateWrench, 'R', OrePrefixes.pipeMedium.get(Materials.Titanium), 'F', ItemList.Casing_StableTitanium, 'P', OrePrefixes.rotor.get(Materials.Titanium)});
        
        ItemList.Machine_Multi_DieselEngine2.set(new GT_MetaTileEntity_DieselEngine2(12230, "multimachine.dieselengine2", "Combustion Engine Mk2").getStackForm(1L));
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_Multi_DieselEngine2.get(1L, new Object[0]), bitsd, new Object[]{"PCP", "EME", "GWG", 'M', ItemList.Hull_IV, 'P', ItemList.Electric_Piston_IV, 'E', ItemList.Electric_Motor_IV, 'C', OrePrefixes.circuit.get(Materials.Master), 'W', OrePrefixes.cableGt01.get(Materials.VanadiumGallium), 'G', OrePrefixes.gearGt.get(Materials.TungstenSteel)});
        
        ItemList.Machine_Multi_PlasmaEngine.set(new GT_MetaTileEntity_PlasmaEngine(12072, "multimachine.plasmaengine", "Plasma Engine").getStackForm(1L));
        //GT_ModHandler.addCraftingRecipe(ItemList.Machine_Multi_PlasmaEngine.get(1L, new Object[0]), bitsd, new Object[]{"PCP", "FMF", "WCW", 'M', ItemList.Hull_MAX, 'F', ItemList.Field_Generator_UHV, 'C', OrePrefixes.circuit.get(Materials.Infinite), 'W', OrePrefixes.wireGt16.get(Materials.Superconductor), 'P', OrePrefixes.pipeMedium.get(Materials.Superconductor)});
        
		ItemList.Machine_Multi_Cleanroom.set(new GT_MetaTileEntity_Cleanroom(1172, "multimachine.cleanroom", "Cleanroom Controller").getStackForm(1));
		//If Cleanroom is enabled, add a recipe, else hide from NEI.
		if (GT_Mod.gregtechproxy.mEnableCleanroom){
			GT_ModHandler.addCraftingRecipe(ItemList.Machine_Multi_Cleanroom.get(1L, new Object[0]), bitsd, new Object[]{"FFF", "RHR", "MCM", 'H', ItemList.Hull_HV, 'F', ItemList.Component_Filter, 'R', OrePrefixes.rotor.get(Materials.StainlessSteel), 'M', ItemList.Electric_Motor_HV, 'C', OrePrefixes.circuit.get(Materials.Advanced)});
		}
		else {
			if (isNEILoaded){
				API.hideItem(ItemList.Machine_Multi_Cleanroom.get(1L, new Object[0]));
			}
		}
		
        ItemList.Machine_LV_CircuitAssembler.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe( 1180, "basicmachine.circuitassembler.tier.01", "Basic Circuit Assembling Machine", 1, "Avengers, Assemble!", GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes, 6, 1, 16000, 0, 1, "CircuitAssembler.png", "", aBoolConst_0, aBoolConst_0, 0, "CIRCUITASSEMBLER", new Object[]{"ACE", "VMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER}).getStackForm(1L));
        ItemList.Machine_MV_CircuitAssembler.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe( 1181, "basicmachine.circuitassembler.tier.02", "Advanced Circuit Assembling Machine", 2, "Avengers, Assemble!", GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes, 6, 1, 16000, 0, 1, "CircuitAssembler.png", "", aBoolConst_0, aBoolConst_0, 0, "CIRCUITASSEMBLER", new Object[]{"ACE", "VMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER}).getStackForm(1L));
        ItemList.Machine_HV_CircuitAssembler.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe( 1182, "basicmachine.circuitassembler.tier.03", "Advanced Circuit Assembling Machine II", 3, "Avengers, Assemble!", GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes, 6, 1, 16000, 0, 1, "CircuitAssembler.png", "", aBoolConst_0, aBoolConst_0, 0, "CIRCUITASSEMBLER", new Object[]{"ACE", "VMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER}).getStackForm(1L));
        ItemList.Machine_EV_CircuitAssembler.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe( 1183, "basicmachine.circuitassembler.tier.04", "Advanced Circuit Assembling Machine III", 4, "Avengers, Assemble!", GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes, 6, 1, 16000, 0, 1, "CircuitAssembler.png", "", aBoolConst_0, aBoolConst_0, 0, "CIRCUITASSEMBLER", new Object[]{"ACE", "VMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER}).getStackForm(1L));
        ItemList.Machine_IV_CircuitAssembler.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe( 1184, "basicmachine.circuitassembler.tier.05", "Advanced Circuit Assembling Machine IV", 5, "Avengers, Assemble!", GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes, 6, 1, 16000, 0, 1, "CircuitAssembler.png", "", aBoolConst_0, aBoolConst_0, 0, "CIRCUITASSEMBLER", new Object[]{"ACE", "VMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER}).getStackForm(1L));
        ItemList.Machine_LuV_CircuitAssembler.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(1185, "basicmachine.circuitassembler.tier.06", "Advanced Circuit Assembling Machine V", 6, "Avengers, Assemble!", GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes, 6, 1, 16000, 0, 1, "CircuitAssembler.png", "", aBoolConst_0, aBoolConst_0, 0, "CIRCUITASSEMBLER", new Object[]{"ACE", "VMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER}).getStackForm(1L));
        ItemList.Machine_ZPM_CircuitAssembler.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(1186, "basicmachine.circuitassembler.tier.07", "Advanced Circuit Assembling Machine VI", 7, "Avengers, Assemble!", GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes, 6, 1, 16000, 0, 1, "CircuitAssembler.png", "", aBoolConst_0, aBoolConst_0, 0, "CIRCUITASSEMBLER", new Object[]{"ACE", "VMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER}).getStackForm(1L));
        ItemList.Machine_UV_CircuitAssembler.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe( 1187, "basicmachine.circuitassembler.tier.08", "Advanced Circuit Assembling Machine VII", 8, "Avengers, Assemble!", GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes, 6, 1, 16000, 0, 1, "CircuitAssembler.png", "", aBoolConst_0, aBoolConst_0, 0, "CIRCUITASSEMBLER", new Object[]{"ACE", "VMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER}).getStackForm(1L));
          
        ItemList.Machine_HV_LightningRod.set(new GT_MetaTileEntity_LightningRod(1174, "basicgenerator.lightningrod.03", "Lightning Rod", 3).getStackForm(1));
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_HV_LightningRod.get(1L, new Object[0]), bitsd, new Object[]{"LTL", "TMT", "LTL", 'M', ItemList.Hull_LuV, 'L', ItemList.Energy_LapotronicOrb, 'T', ItemList.Transformer_ZPM_LuV});
        ItemList.Machine_EV_LightningRod.set(new GT_MetaTileEntity_LightningRod(1175, "basicgenerator.lightningrod.04", "Lightning Rod II", 4).getStackForm(1));
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_EV_LightningRod.get(1L, new Object[0]), bitsd, new Object[]{"LTL", "TMT", "LTL", 'M', ItemList.Hull_ZPM, 'L', ItemList.Energy_LapotronicOrb2, 'T', ItemList.Transformer_UV_ZPM});
        ItemList.Machine_IV_LightningRod.set(new GT_MetaTileEntity_LightningRod(1176, "basicgenerator.lightningrod.05", "Lightning Rod III", 5).getStackForm(1));
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_IV_LightningRod.get(1L, new Object[0]), bitsd, new Object[]{"LTL", "TMT", "LTL", 'M', ItemList.Hull_UV, 'L', ItemList.Energy_Module, 'T', ItemList.Transformer_MAX_UV});
        ItemList.Machine_LuV_LightningRod.set(new GT_MetaTileEntity_LightningRod(12070, "basicgenerator.lightningrod.06", "Lightning Rod IV", 6).getStackForm(1));
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_LuV_LightningRod.get(1L, new Object[0]), bitsd, new Object[]{"LTL", "TMT", "LTL", 'M', ItemList.Hull_MAX, 'L', ItemList.Energy_Cluster, 'T', ItemList.Transformer_UEV_UHV});
        ItemList.Machine_ZPM_LightningRod.set(new GT_MetaTileEntity_LightningRod(12103, "basicgenerator.lightningrod.07", "Lightning Rod V", 7).getStackForm(1));
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_ZPM_LightningRod.get(1L, new Object[0]), bitsd, new Object[]{"LTL", "TMT", "LTL", 'M', ItemList.Hull_UEV, 'L', ItemList.ZPM2, 'T', ItemList.Transformer_UIV_UEV});
        ItemList.Machine_UV_LightningRod.set(new GT_MetaTileEntity_LightningRod(12104, "basicgenerator.lightningrod.08", "Lightning Rod VI", 8).getStackForm(1));
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_UV_LightningRod.get(1L, new Object[0]), bitsd, new Object[]{"LTL", "TMT", "LTL", 'M', ItemList.Hull_UIV, 'L', ItemList.ZPM3, 'T', ItemList.Transformer_UMV_UIV});
        
        ItemList.Machine_Multi_AirFilter.set(new GT_MetaTileEntity_AirFilter(12105, "multimachine.airfilter", "Electric Air Filter").getStackForm(1L));
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_AirFilter_Vent.get(2L), bits, new Object[]{"ThT", "TFT", "TwT", 'T', OrePrefixes.stickLong.get(Materials.Titanium), 'F', OrePrefixes.frameGt.get(Materials.Steel)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_AirFilter.get(1L), bits, new Object[]{"SSS", "RVR", "MSM", 'V', ItemList.Casing_AirFilter_Vent, 'S', OrePrefixes.stickLong.get(Materials.Titanium), 'R', OrePrefixes.rotor.get(Materials.Titanium), 'M', ItemList.Electric_Motor_EV});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_Multi_AirFilter.get(1L), bits, new Object[]{"RPR", "MBM", "CGC", 'B', ItemList.Hull_EV, 'R', OrePrefixes.rotor.get(Materials.Titanium), 'P',  ItemList.Electric_Pump_EV, 'M', ItemList.Electric_Motor_EV, 'C', OrePrefixes.cableGt01.get(Materials.Titanium), 'G', ItemList.Casing_Turbine2});

        //Antimatter
        ItemList.Antimatter_Reactor.set(new GT_MetaTileEntity_AntimatterReactor(12071, "basicgenerator.antimatter.tier.09", "Antimatter Reactor", 9).getStackForm(1L));
        
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_RadiationProof.get(2L, new Object[0]), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"PhP", "PFP", "PwP", 'P', OrePrefixes.plate.get(Materials.TungstenCarbide), 'F', OrePrefixes.frameGt.get(Materials.TungstenCarbide)});
        GT_ModHandler.addCraftingRecipe(ItemList.Block_Magnetic_Coil.get(1L, new Object[0]), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"PPP", "WWW", "PPP", 'W', OrePrefixes.wireGt04.get(Materials.SuperconductorZPM), 'P', OrePrefixes.plate.get(Materials.NeodymiumMagnetic)});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_Core_Chamber.get(1L, new Object[0]), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"FPE", "ACT", "FPS", 'F', ItemList.Field_Generator_ZPM, 'P', ItemList.Electric_Pump_ZPM, 'E', ItemList.Emitter_ZPM, 'A', OrePrefixes.pipeMedium.get(Materials.Superconductor), 'C', ItemList.Hull_ZPM, 'T', OrePrefixes.circuit.get(Materials.Master), 'S', ItemList.Sensor_ZPM});
        GT_ModHandler.addCraftingRecipe(ItemList.Casing_Internix_Chamber.get(1L, new Object[0]), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"PRP", "WCW", "PRP", 'P', OrePrefixes.plateDouble.get(Materials.HSSS), 'R', ItemList.Neutron_Reflector, 'W', ItemList.Electric_Pump_LuV, 'C', ItemList.Casing_Pipe_TungstenSteel});
         
        ItemList.Machine_Multi_LargeChemicalReactor.set(new GT_MetaTileEntity_LargeChemicalReactor(1169, "multimachine.chemicalreactor", "Large Chemical Reactor").getStackForm(1));
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_Multi_LargeChemicalReactor.get(1L, new Object[0]), bitsd, new Object[]{"CRC", "PMP", "CBC", 
        		'C', OrePrefixes.circuit.get(Materials.Advanced), 
        		'R', OrePrefixes.rotor.get(Materials.StainlessSteel), 
        		'P', OrePrefixes.pipeLarge.get(Materials.Polytetrafluoroethylene), 
        		'M', ItemList.Electric_Motor_HV,
        		'B', ItemList.Hull_HV});
        
        //Nuclear reactor (12106, 12107, 12108, 12109)
        //ItemList.Nuclear_Reactor1.set(new GT_MetaTileEntity_NuclearReactor1(12106, "multimachine.nuclear1", "Nuclear Reactor Mk 1").getStackForm(1L));
        
        ItemList.Machine_LV_AirGenerator.set(new GT_MetaTileEntity_AirGenerator(12120, "basicmachine.e_airgenerator.tier.01", "Basic Electric Air Condenser", 1, null).getStackForm(1L));
        ItemList.Machine_MV_AirGenerator.set(new GT_MetaTileEntity_AirGenerator(12121, "basicmachine.e_airgenerator.tier.02", "Advanced Electric Air Condenser", 2, null).getStackForm(1L));
        ItemList.Machine_HV_AirGenerator.set(new GT_MetaTileEntity_AirGenerator(12122, "basicmachine.e_airgenerator.tier.03", "Advanced Electric Air Condenser II", 3, null).getStackForm(1L));
        ItemList.Machine_EV_AirGenerator.set(new GT_MetaTileEntity_AirGenerator(12123, "basicmachine.e_airgenerator.tier.04", "Advanced Electric Air Condenser III", 4, null).getStackForm(1L));
        ItemList.Machine_IV_AirGenerator.set(new GT_MetaTileEntity_AirGenerator(12124, "basicmachine.e_airgenerator.tier.05", "Advanced Electric Air Condenser IV", 5, null).getStackForm(1L));
        ItemList.Machine_LuV_AirGenerator.set(new GT_MetaTileEntity_AirGenerator(12125, "basicmachine.e_airgenerator.tier.06", "Advanced Electric Air Condenser V", 6, null).getStackForm(1L));
        ItemList.Machine_ZPM_AirGenerator.set(new GT_MetaTileEntity_AirGenerator(12126, "basicmachine.e_airgenerator.tier.07", "Advanced Electric Air Condenser VI", 7, null).getStackForm(1L));
        ItemList.Machine_UV_AirGenerator.set(new GT_MetaTileEntity_AirGenerator(12127, "basicmachine.e_airgenerator.tier.08", "Advanced Electric Air Condenser VII", 8, null).getStackForm(1L));
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_LV_AirGenerator.get(1L, new Object[0]), bitsd, new Object[]{"EPE", "CMC", "EPE", 'M', ItemList.Hull_LV, 'E', OrePrefixes.circuit.get(Materials.Basic), 'P', ItemList.Electric_Pump_LV, 'C', new ItemStack(Blocks.glass, 1)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_MV_AirGenerator.get(1L, new Object[0]), bitsd, new Object[]{"EPE", "CMC", "EPE", 'M', ItemList.Hull_MV, 'E', OrePrefixes.circuit.get(Materials.Good), 'P', ItemList.Electric_Pump_MV, 'C', new ItemStack(Blocks.glass, 1)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_HV_AirGenerator.get(1L, new Object[0]), bitsd, new Object[]{"EPE", "CMC", "EPE", 'M', ItemList.Hull_HV, 'E', OrePrefixes.circuit.get(Materials.Advanced), 'P', ItemList.Electric_Pump_HV, 'C', new ItemStack(Blocks.glass, 1)});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_EV_AirGenerator.get(1L, new Object[0]), bitsd, new Object[]{"EPE", "CMC", "EPE", 'M', ItemList.Hull_EV, 'E', OrePrefixes.circuit.get(Materials.Data), 'P', ItemList.Electric_Pump_EV, 'C', Ic2Items.reinforcedGlass});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_IV_AirGenerator.get(1L, new Object[0]), bitsd, new Object[]{"EPE", "CMC", "EPE", 'M', ItemList.Hull_IV, 'E', OrePrefixes.circuit.get(Materials.Elite), 'P', ItemList.Electric_Pump_IV, 'C', Ic2Items.reinforcedGlass});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_LuV_AirGenerator.get(1L, new Object[0]), bitsd, new Object[]{"EPE", "CMC", "EPE", 'M', ItemList.Hull_LuV, 'E', OrePrefixes.circuit.get(Materials.Master), 'P', ItemList.Electric_Pump_LuV, 'C', Ic2Items.reinforcedGlass});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_ZPM_AirGenerator.get(1L, new Object[0]), bitsd, new Object[]{"EPE", "CMC", "EPE", 'M', ItemList.Hull_ZPM, 'E', OrePrefixes.circuit.get(Materials.Ultimate), 'P', ItemList.Electric_Pump_ZPM, 'C', Ic2Items.reinforcedGlass});
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_UV_AirGenerator.get(1L, new Object[0]), bitsd, new Object[]{"EPE", "CMC", "EPE", 'M', ItemList.Hull_UV, 'E', OrePrefixes.circuit.get(Materials.Superconductor), 'P', ItemList.Electric_Pump_UV, 'C', Ic2Items.reinforcedGlass});

        ItemList.Machine_DigitalTransformer_EV.set(new GT_MetaTileEntity_Digital_Transformer(12183, "basicmachine.d_transformer.tier.04", "Extreme Digital Transformer", 4,"EV -> (ULV - MAX) (Use Soft Mallet to invert)").getStackForm(1L));
        ItemList.Machine_DigitalTransformer_IV.set(new GT_MetaTileEntity_Digital_Transformer(12184, "basicmachine.d_transformer.tier.05", "Insane Digital Transformer", 5,"IV -> (ULV - MAX) (Use Soft Mallet to invert)").getStackForm(1L));
        ItemList.Machine_DigitalTransformer_LuV.set(new GT_MetaTileEntity_Digital_Transformer(12185, "basicmachine.d_transformer.tier.06", "Ludicrous Digital Transformer", 6,"LuV -> (ULV - MAX) (Use Soft Mallet to invert)").getStackForm(1L));
        ItemList.Machine_DigitalTransformer_ZPM.set(new GT_MetaTileEntity_Digital_Transformer(12186, "basicmachine.d_transformer.tier.07", "ZPM Digital Transformer", 7,"ZPM -> (ULV - MAX) (Use Soft Mallet to invert)").getStackForm(1L));
        ItemList.Machine_DigitalTransformer_UV.set(new GT_MetaTileEntity_Digital_Transformer(12187, "basicmachine.d_transformer.tier.08", "Ultimate Digital Transformer", 8,"UV -> (ULV - MAX) (Use Soft Mallet to invert)").getStackForm(1L));
        ItemList.Machine_DigitalTransformer_UHV.set(new GT_MetaTileEntity_Digital_Transformer(12188, "basicmachine.d_transformer.tier.09", "Ultimate High Digital Transformer", 9,"UHV -> (ULV - MAX) (Use Soft Mallet to invert)").getStackForm(1L));
        ItemList.Machine_DigitalTransformer_UEV.set(new GT_MetaTileEntity_Digital_Transformer(12189, "basicmachine.d_transformer.tier.10", "Ultra Extreme Digital Transformer", 10,"UEV -> (ULV - MAX) (Use Soft Mallet to invert)").getStackForm(1L));
        
        //Industrial Pulverizer
        ItemList.Machine_IndustrialPulverizer.set(new GT_MetaTileEntity_IndustrialPulverizer(12191, "multimachine.industrialpulverizer", "Industrial Pulverizer").getStackForm(1));
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_IndustrialPulverizer.get(1L, new Object[0]), bitsd, new Object[]{"POP", "MHM", "CWC", 'H', ItemList.Hull_MV, 'C', OrePrefixes.circuit.get(Materials.Good), 'P', ItemList.Electric_Piston_MV, 'M', ItemList.Electric_Motor_MV, 'W', OrePrefixes.cableGt01.get(Materials.Copper), 'O', ItemList.Component_Grinder_Diamond});
        
        //Flotation Unit
        ItemList.Machine_FlotationUnit.set(new GT_MetaTileEntity_FlotationUnit(12192, "multimachine.flotationunit", "Flotation Unit").getStackForm(1));
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_FlotationUnit.get(1L, new Object[0]), bitsd, new Object[]{"SOS", "RHR", "MCM", 'H', ItemList.Hull_EV, 'C', OrePrefixes.circuit.get(Materials.Data), 'S', ItemList.Electric_Pump_EV, 'M', ItemList.Electric_Motor_EV, 'R', OrePrefixes.rotor.get(Materials.Titanium), 'O', OrePrefixes.pipeMedium.get(Materials.Titanium)});
        
        //Multiblock Centrifuge
        ItemList.Machine_MultiblockCentrifuge.set(new GT_MetaTileEntity_MultiblockCentrifuge(12193, "multimachine.multicentrifuge", "Multiblock Centrifuge").getStackForm(1));
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_MultiblockCentrifuge.get(1L, new Object[0]), bitsd, new Object[]{"MCM", "CHC", "MCM", 'H', ItemList.Hull_EV, 'C', OrePrefixes.circuit.get(Materials.Data), 'M', ItemList.Electric_Motor_EV});
        
        //Multiblock Electrolyzer
        ItemList.Machine_MultiblockElectrolyzer.set(new GT_MetaTileEntity_MultiblockElectrolyzer(12194, "multimachine.multielectrolyzer", "Multiblock Electrolyzer").getStackForm(1));
        GT_ModHandler.addCraftingRecipe(ItemList.Machine_MultiblockElectrolyzer.get(1L, new Object[0]), bitsd, new Object[]{"WCW", "PHP", "WCW", 'H', ItemList.Hull_IV, 'C', OrePrefixes.circuit.get(Materials.Elite), 'W', OrePrefixes.wireGt02.get(Materials.Platinum), 'P', OrePrefixes.plateDouble.get(Materials.Titanium)});
        
        //Tesseract
        //ItemList.Machine_MultiblockTesseract.set(new GT_MetaTileEntity_Tesseract(12195, "multimachine.tesseract.tier.09", "Tesseract", 9).getStackForm(1L));
        
        
   }

    private static void run4() {
    	long bits = GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED;
    	long bitsd = GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED;
        for (int i = 0; i < GregTech_API.sGeneratedMaterials.length; i++) {
            if (((GregTech_API.sGeneratedMaterials[i] != null) && ((GregTech_API.sGeneratedMaterials[i].mTypes & 0x2) != 0)) || (GregTech_API.sGeneratedMaterials[i] == Materials.Wood)) {
                new GT_MetaPipeEntity_Frame(4096 + i, "GT_Frame_" + GregTech_API.sGeneratedMaterials[i], (GT_LanguageManager.i18nPlaceholder ? "%material" : GregTech_API.sGeneratedMaterials[i].mDefaultLocalName) + " Frame Box", GregTech_API.sGeneratedMaterials[i]);
            }
        }
        boolean bEC = !GT_Mod.gregtechproxy.mHardcoreCables;

        makeWires(Materials.RedAlloy, 2000, 0L, 1L, 1L, gregtech.api.enums.GT_Values.V[0], true, aBoolConst_0);

        makeWires(Materials.Cobalt, 1200, bEC ? 2L : 2L, bEC ? 4L : 4L, 2L, gregtech.api.enums.GT_Values.V[1], true, aBoolConst_0);
        makeWires(Materials.Lead, 1220, bEC ? 2L : 2L, bEC ? 4L : 4L, 2L, gregtech.api.enums.GT_Values.V[1], true, aBoolConst_0);
        makeWires(Materials.Tin, 1240, bEC ? 1L : 1L, bEC ? 2L : 2L, 1L, gregtech.api.enums.GT_Values.V[1], true, aBoolConst_0);
        makeWires(Materials.Zinc, 1260, bEC ? 1L : 1L, bEC ? 2L : 2L, 1L, gregtech.api.enums.GT_Values.V[1], true, aBoolConst_0);
        makeWires(Materials.SolderingAlloy, 1280, bEC ? 1L : 1L, bEC ? 2L : 2L, 1L, gregtech.api.enums.GT_Values.V[1], true, aBoolConst_0);

        makeWires(Materials.Iron, 1300, bEC ? 3L : 4L, bEC ? 6L : 8L, 2L, gregtech.api.enums.GT_Values.V[2], true, aBoolConst_0);
        makeWires(Materials.Nickel, 1320, bEC ? 3L : 5L, bEC ? 6L : 10L, 3L, gregtech.api.enums.GT_Values.V[2], true, aBoolConst_0);
        makeWires(Materials.Cupronickel, 1340, bEC ? 3L : 4L, bEC ? 6L : 8L, 2L, gregtech.api.enums.GT_Values.V[2], true, aBoolConst_0);
        makeWires(Materials.Copper, 1360, bEC ? 2L : 3L, bEC ? 4L : 6L, 1L, gregtech.api.enums.GT_Values.V[2], true, aBoolConst_0);
        makeWires(Materials.AnnealedCopper, 1380, bEC ? 1L : 2L, bEC ? 2L : 4L, 1L, gregtech.api.enums.GT_Values.V[2], true, aBoolConst_0);
        makeWires(Materials.RedstoneAlloy, 11430, 1L, 1L, 2L, gregtech.api.enums.GT_Values.V[2], true, aBoolConst_0);

        makeWires(Materials.Kanthal, 1400, bEC ? 3L : 8L, bEC ? 6L : 16L, 4L, gregtech.api.enums.GT_Values.V[3], true, aBoolConst_0);
        makeWires(Materials.Gold, 1420, bEC ? 2L : 6L, bEC ? 4L : 12L, 3L, gregtech.api.enums.GT_Values.V[3], true, aBoolConst_0);
        makeWires(Materials.Electrum, 1440, bEC ? 2L : 5L, bEC ? 4L : 10L, 2L, gregtech.api.enums.GT_Values.V[3], true, aBoolConst_0);
        makeWires(Materials.Silver, 1460, bEC ? 1L : 4L, bEC ? 2L : 8L, 1L, gregtech.api.enums.GT_Values.V[3], true, aBoolConst_0);
        makeWires(Materials.BlueAlloy, 1480, bEC ? 1L : 4L, bEC ? 2L : 8L, 2L, gregtech.api.enums.GT_Values.V[3], true, aBoolConst_0);
        makeWires(Materials.EnergeticAlloy, 11450, 1L, 1L, 3L, gregtech.api.enums.GT_Values.V[3], true, aBoolConst_0);

        makeWires(Materials.Nichrome, 1500, bEC ? 4L : 32L, bEC ? 8L : 64L, 3L, gregtech.api.enums.GT_Values.V[4], true, aBoolConst_0);
        makeWires(Materials.Steel, 1520, bEC ? 2L : 16L, bEC ? 4L : 32L, 2L, gregtech.api.enums.GT_Values.V[4], true, aBoolConst_0);
        makeWires(Materials.BlackSteel, 1540, bEC ? 2L : 14L, bEC ? 4L : 28L, 3L, gregtech.api.enums.GT_Values.V[4], true, aBoolConst_0);
        makeWires(Materials.Titanium, 1560, bEC ? 2L : 12L, bEC ? 4L : 24L, 4L, gregtech.api.enums.GT_Values.V[4], true, aBoolConst_0);
        makeWires(Materials.Aluminium, 1580, bEC ? 1L : 8L, bEC ? 2L : 16L, 1L, gregtech.api.enums.GT_Values.V[4], true, aBoolConst_0);

        makeWires(Materials.Graphene, 1600, bEC ? 1L : 16L, bEC ? 2L : 32L, 1L, gregtech.api.enums.GT_Values.V[5], aBoolConst_0, true);
        makeWires(Materials.Osmium, 1620, bEC ? 2L : 32L, bEC ? 4L : 64L, 4L, gregtech.api.enums.GT_Values.V[5], true, aBoolConst_0);
        makeWires(Materials.Platinum, 1640, bEC ? 1L : 16L, bEC ? 2L : 32L, 2L, gregtech.api.enums.GT_Values.V[5], true, aBoolConst_0);
        makeWires(Materials.TungstenSteel, 1660, bEC ? 2L : 14L, bEC ? 4L : 28L, 3L, gregtech.api.enums.GT_Values.V[5], true, aBoolConst_0);
        makeWires(Materials.Tungsten, 1680, bEC ? 2L : 12L, bEC ? 4L : 24L, 2L, gregtech.api.enums.GT_Values.V[5], true, aBoolConst_0);
        makeWires(Materials.VibrantAlloy, 11470, 1L, 1L, 4L, gregtech.api.enums.GT_Values.V[5], true, aBoolConst_0);

        makeWires(Materials.HSSG, 1700, bEC ? 2L : 128L, bEC ? 4L : 256L, 4L, gregtech.api.enums.GT_Values.V[6], true, aBoolConst_0);
        makeWires(Materials.NiobiumTitanium, 1720, bEC ? 2L : 128L, bEC ? 4L : 256L, 4L, gregtech.api.enums.GT_Values.V[6], true, aBoolConst_0);
        makeWires(Materials.VanadiumGallium, 1740, bEC ? 2L : 128L, bEC ? 4L : 256L, 4L, gregtech.api.enums.GT_Values.V[6], true, aBoolConst_0);
        makeWires(Materials.YttriumBariumCuprate, 1760, bEC ? 4L : 256L, bEC ? 8L : 512L, 4L, gregtech.api.enums.GT_Values.V[6], true, aBoolConst_0);

        makeWires(Materials.Naquadah, 1780, bEC ? 2L : 64L, bEC ? 4L : 128L, 2L, gregtech.api.enums.GT_Values.V[7], true, aBoolConst_0);

        makeWires(Materials.NaquadahAlloy, 1800, bEC ? 4L : 64L, bEC ? 8L : 128L, 2L, gregtech.api.enums.GT_Values.V[8], true, aBoolConst_0);
        makeWires(Materials.Duranium, 1820, bEC ? 8L : 64L, bEC ? 16L : 128L, 1L, gregtech.api.enums.GT_Values.V[8], true, aBoolConst_0);
        makeWires(Materials.ElectrumFlux, 1900, bEC ? 2L : 16L, bEC ? 4L : 64L, 3L, gregtech.api.enums.GT_Values.V[8], true, aBoolConst_0);

        makeWires(Materials.Superconductor, 2020, 0L, 0L, 16L, gregtech.api.enums.GT_Values.V[9], aBoolConst_0, true);
        makeWires(Materials.Europium, 11310, 1L, 32L, 2L, GT_Values.V[9], aBoolConst_0, true);
        
        makeWires(Materials.Diamericiumtitanium, 11330, 1L, 32L, 8L, GT_Values.V[10], aBoolConst_0, true);
        
        makeWires(Materials.Neutronium, 11350, 1L, 16L, 4L, GT_Values.V[11], aBoolConst_0, true);
        
        makeWires(Materials.Quantium, 11370, 1L, 16L, 2L, GT_Values.V[12], aBoolConst_0, true);
        
        makeWires(Materials.Pentacadmiummagnesiumhexaoxid, 2200, bEC ? 1L : 128L, bEC ? 2L : 256L, 1L, gregtech.api.enums.GT_Values.V[2],true, aBoolConst_0);
        makeWires(Materials.Titaniumonabariumdecacoppereikosaoxid, 2220, bEC ? 1L : 128L, bEC ? 2L : 256L, 1L, gregtech.api.enums.GT_Values.V[3], true, aBoolConst_0);
        makeWires(Materials.Uraniumtriplatinid, 2240, bEC ? 1L : 128L, bEC ? 2L : 256L, 2L, gregtech.api.enums.GT_Values.V[4], true, aBoolConst_0);
        makeWires(Materials.Vanadiumtriindinid, 2260,  bEC ? 1L : 128L, bEC ? 2L : 256L, 2L, gregtech.api.enums.GT_Values.V[5], true, aBoolConst_0);
        makeWires(Materials.Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid, 2280, 2L, 2L, 4L, gregtech.api.enums.GT_Values.V[6], true, aBoolConst_0);
        makeWires(Materials.Tetranaquadahdiindiumhexaplatiumosminid, 2300, 2L, 2L, 4L, gregtech.api.enums.GT_Values.V[7], true, aBoolConst_0);
        makeWires(Materials.SuperconductorMV, 2320, 0L, 0L, 2L, gregtech.api.enums.GT_Values.V[2], aBoolConst_0, true);
        makeWires(Materials.SuperconductorHV, 2340, 0L, 0L, 2L, gregtech.api.enums.GT_Values.V[3], aBoolConst_0, true);
        makeWires(Materials.SuperconductorEV, 2360, 0L, 0L, 4L, gregtech.api.enums.GT_Values.V[4], aBoolConst_0, true);
        makeWires(Materials.SuperconductorIV, 2380, 0L, 0L, 4L, gregtech.api.enums.GT_Values.V[5], aBoolConst_0, true);
        makeWires(Materials.SuperconductorLuV, 2400, 0L, 0L, 8L, gregtech.api.enums.GT_Values.V[6], aBoolConst_0, true);
        makeWires(Materials.SuperconductorZPM, 2420, 0L, 0L, 8L, gregtech.api.enums.GT_Values.V[7], aBoolConst_0, true);
        makeWires(Materials.SuperconductorUV, 2440, 0L, 0L, 12L, gregtech.api.enums.GT_Values.V[8], aBoolConst_0, true);
        makeWires(Materials.SuperconductorUEV, 2460, 0L, 0L, 16L, gregtech.api.enums.GT_Values.V[10], aBoolConst_0, true);
        makeWires(Materials.Longasssuperconductornameforuvwire, 2480, 2L, 2L, 4L, gregtech.api.enums.GT_Values.V[8], true, aBoolConst_0);
        makeWires(Materials.Longasssuperconductornameforuhvwire, 2500, 2L, 2L, 4L, gregtech.api.enums.GT_Values.V[9], true, aBoolConst_0);
        makeWires(Materials.Longasssuperconductornameforuevwire, 2520, 2L, 2L, 4L, gregtech.api.enums.GT_Values.V[10], true, aBoolConst_0);
        
        if (Loader.isModLoaded("DraconicEvolution")){
        makeWires(Materials.Draconium, 11390, bEC ? 4L : 16L, bEC ? 32L : 64L, 8L, GT_Values.V[10], true, false);
        makeWires(Materials.DraconiumAwakened, 11410, bEC ? 4L : 16L, bEC ? 16L : 32L, 1L, GT_Values.V[14], false, false);
        }
        
        if (!GT_Mod.gregtechproxy.mDisableIC2Cables) {
            GT_ModHandler.addCraftingRecipe(GT_ModHandler.getIC2Item("copperCableItem", 2L), GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"xP", 'P', OrePrefixes.plate.get(Materials.Copper)});
            GT_ModHandler.addCraftingRecipe(GT_ModHandler.getIC2Item("goldCableItem", 4L), GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"xP", 'P', OrePrefixes.plate.get(Materials.Gold)});
            GT_ModHandler.addCraftingRecipe(GT_ModHandler.getIC2Item("ironCableItem", 3L), GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"xP", 'P', OrePrefixes.plate.get(Materials.Iron)});
            GT_ModHandler.addCraftingRecipe(GT_ModHandler.getIC2Item("tinCableItem", 3L), GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"xP", 'P', OrePrefixes.plate.get(Materials.Tin)});
        }


        GT_OreDictUnificator.registerOre(OrePrefixes.pipeSmall.get(Materials.Wood), new GT_MetaPipeEntity_Fluid(5101, "GT_Pipe_Wood_Small", "Small Wooden Fluid Pipe", 0.375F, Materials.Wood, 10, 350, aBoolConst_0).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeMedium.get(Materials.Wood), new GT_MetaPipeEntity_Fluid(5102, "GT_Pipe_Wood", "Wooden Fluid Pipe", 0.5F, Materials.Wood, 30, 350, aBoolConst_0).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeLarge.get(Materials.Wood), new GT_MetaPipeEntity_Fluid(5103, "GT_Pipe_Wood_Large", "Large Wooden Fluid Pipe", 0.75F, Materials.Wood, 60, 350, aBoolConst_0).getStackForm(1L));

        /*GT_OreDictUnificator.registerOre(OrePrefixes.pipeTiny.get(Materials.Copper), new GT_MetaPipeEntity_Fluid(5110, "GT_Pipe_Copper_Tiny", "Tiny Copper Fluid Pipe", 0.25F, Materials.Copper, 10, 1000, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeSmall.get(Materials.Copper), new GT_MetaPipeEntity_Fluid(5111, "GT_Pipe_Copper_Small", "Small Copper Fluid Pipe", 0.375F, Materials.Copper, 20, 1000, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeMedium.get(Materials.Copper), new GT_MetaPipeEntity_Fluid(5112, "GT_Pipe_Copper", "Copper Fluid Pipe", 0.5F, Materials.Copper, 60, 1000, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeLarge.get(Materials.Copper), new GT_MetaPipeEntity_Fluid(5113, "GT_Pipe_Copper_Large", "Large Copper Fluid Pipe", 0.75F, Materials.Copper, 120, 1000, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeHuge.get(Materials.Copper), new GT_MetaPipeEntity_Fluid(5114, "GT_Pipe_Copper_Huge", "Huge Copper Fluid Pipe", 0.875F, Materials.Copper, 240, 1000, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeTiny.get(Materials.Bronze), new GT_MetaPipeEntity_Fluid(5120, "GT_Pipe_Bronze_Tiny", "Tiny Bronze Fluid Pipe", 0.25F, Materials.Bronze, 20, 2000, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeSmall.get(Materials.Bronze), new GT_MetaPipeEntity_Fluid(5121, "GT_Pipe_Bronze_Small", "Small Bronze Fluid Pipe", 0.375F, Materials.Bronze, 40, 2000, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeMedium.get(Materials.Bronze), new GT_MetaPipeEntity_Fluid(5122, "GT_Pipe_Bronze", "Bronze Fluid Pipe", 0.5F, Materials.Bronze, 120, 2000, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeLarge.get(Materials.Bronze), new GT_MetaPipeEntity_Fluid(5123, "GT_Pipe_Bronze_Large", "Large Bronze Fluid Pipe", 0.75F, Materials.Bronze, 240, 2000, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeHuge.get(Materials.Bronze), new GT_MetaPipeEntity_Fluid(5124, "GT_Pipe_Bronze_Huge", "Huge Bronze Fluid Pipe", 0.875F, Materials.Bronze, 480, 2000, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeTiny.get(Materials.Steel), new GT_MetaPipeEntity_Fluid(5130, "GT_Pipe_Steel_Tiny", "Tiny Steel Fluid Pipe", 0.25F, Materials.Steel, 40, 2500, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeSmall.get(Materials.Steel), new GT_MetaPipeEntity_Fluid(5131, "GT_Pipe_Steel_Small", "Small Steel Fluid Pipe", 0.375F, Materials.Steel, 80, 2500, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeMedium.get(Materials.Steel), new GT_MetaPipeEntity_Fluid(5132, "GT_Pipe_Steel", "Steel Fluid Pipe", 0.5F, Materials.Steel, 240, 2500, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeLarge.get(Materials.Steel), new GT_MetaPipeEntity_Fluid(5133, "GT_Pipe_Steel_Large", "Large Steel Fluid Pipe", 0.75F, Materials.Steel, 480, 2500, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeHuge.get(Materials.Steel), new GT_MetaPipeEntity_Fluid(5134, "GT_Pipe_Steel_Huge", "Huge Steel Fluid Pipe", 0.875F, Materials.Steel, 960, 2500, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeTiny.get(Materials.StainlessSteel), new GT_MetaPipeEntity_Fluid(5140, "GT_Pipe_StainlessSteel_Tiny", "Tiny Stainless Steel Fluid Pipe", 0.25F, Materials.StainlessSteel, 60, 3000, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeSmall.get(Materials.StainlessSteel), new GT_MetaPipeEntity_Fluid(5141, "GT_Pipe_StainlessSteel_Small", "Small Stainless Steel Fluid Pipe", 0.375F, Materials.StainlessSteel, 120, 3000, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeMedium.get(Materials.StainlessSteel), new GT_MetaPipeEntity_Fluid(5142, "GT_Pipe_StainlessSteel", "Stainless Steel Fluid Pipe", 0.5F, Materials.StainlessSteel, 360, 3000, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeLarge.get(Materials.StainlessSteel), new GT_MetaPipeEntity_Fluid(5143, "GT_Pipe_StainlessSteel_Large", "Large Stainless Steel Fluid Pipe", 0.75F, Materials.StainlessSteel, 720, 3000, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeHuge.get(Materials.StainlessSteel), new GT_MetaPipeEntity_Fluid(5144, "GT_Pipe_StainlessSteel_Huge", "Huge Stainless Steel Fluid Pipe", 0.875F, Materials.StainlessSteel, 1440, 3000, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeTiny.get(Materials.Titanium), new GT_MetaPipeEntity_Fluid(5150, "GT_Pipe_Titanium_Tiny", "Tiny Titanium Fluid Pipe", 0.25F, Materials.Titanium, 80, 5000, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeSmall.get(Materials.Titanium), new GT_MetaPipeEntity_Fluid(5151, "GT_Pipe_Titanium_Small", "Small Titanium Fluid Pipe", 0.375F, Materials.Titanium, 160, 5000, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeMedium.get(Materials.Titanium), new GT_MetaPipeEntity_Fluid(5152, "GT_Pipe_Titanium", "Titanium Fluid Pipe", 0.5F, Materials.Titanium, 480, 5000, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeLarge.get(Materials.Titanium), new GT_MetaPipeEntity_Fluid(5153, "GT_Pipe_Titanium_Large", "Large Titanium Fluid Pipe", 0.75F, Materials.Titanium, 960, 5000, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeHuge.get(Materials.Titanium), new GT_MetaPipeEntity_Fluid(5154, "GT_Pipe_Titanium_Huge", "Huge Titanium Fluid Pipe", 0.875F, Materials.Titanium, 1920, 5000, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeTiny.get(Materials.TungstenSteel), new GT_MetaPipeEntity_Fluid(5160, "GT_Pipe_TungstenSteel_Tiny", "Tiny Tungstensteel Fluid Pipe", 0.25F, Materials.TungstenSteel, 100, 7500, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeSmall.get(Materials.TungstenSteel), new GT_MetaPipeEntity_Fluid(5161, "GT_Pipe_TungstenSteel_Small", "Small Tungstensteel Fluid Pipe", 0.375F, Materials.TungstenSteel, 200, 7500, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeMedium.get(Materials.TungstenSteel), new GT_MetaPipeEntity_Fluid(5162, "GT_Pipe_TungstenSteel", "Tungstensteel Fluid Pipe", 0.5F, Materials.TungstenSteel, 600, 7500, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeLarge.get(Materials.TungstenSteel), new GT_MetaPipeEntity_Fluid(5163, "GT_Pipe_TungstenSteel_Large", "Large Tungstensteel Fluid Pipe", 0.75F, Materials.TungstenSteel, 1200, 7500, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeHuge.get(Materials.TungstenSteel), new GT_MetaPipeEntity_Fluid(5164, "GT_Pipe_TungstenSteel_Huge", "Huge Tungstensteel Fluid Pipe", 0.875F, Materials.TungstenSteel, 2400, 7500, true).getStackForm(1L));*/
        generateFluidPipes(Materials.Copper, Materials.Copper.mName, 5110, 60, 1000, true);
        generateFluidPipes(Materials.Bronze, Materials.Bronze.mName, 5120, 120, 2000, true);
        generateFluidPipes(Materials.Steel, Materials.Steel.mName, 5130, 240, 2500, true);
        generateFluidPipes(Materials.StainlessSteel, Materials.StainlessSteel.mName, 5140, 360, 3000, true);
        generateFluidPipes(Materials.Titanium, Materials.Titanium.mName, 5150, 480, 5000, true);
        generateFluidPipes(Materials.TungstenSteel, Materials.TungstenSteel.mName, 5160, 600, 7500, true);
        generateFluidPipes(Materials.NiobiumTitanium, Materials.NiobiumTitanium.mName, 5300, 900, 2900, true);
        generateFluidPipes(Materials.Enderium, Materials.Enderium.mName, 5310, 1800, 15000, true);
        generateFluidPipes(Materials.Naquadah, Materials.Naquadah.mName, 5320, 9000, 19000, true);
        generateFluidPipes(Materials.Neutronium, Materials.Neutronium.mName, 5330, 16800, 1000000, true);
        generateFluidPipes(Materials.WroughtIron, Materials.WroughtIron.mName, 5340, 180, 2250, true);
        generateFluidPipes(Materials.NetherStar, Materials.NetherStar.mName, 5350, 19200, 1000000, true);
        generateFluidPipes(Materials.Duranium, Materials.Duranium.mName, 5360, 12400, 1000000, true);
        generateFluidPipes(Materials.Polybenzimidazole, Materials.Polybenzimidazole.mName, "PBI", 5370, 600, 1000, true);
        generateFluidPipes(Materials.Infuscolium, Materials.Infuscolium.mName, 5380, 14600, 1000000, true);
        
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeSmall.get(Materials.Ultimate), new GT_MetaPipeEntity_Fluid(5165, "GT_Pipe_HighPressure_Small", "Small High Pressure Fluid Pipe", 0.375F, Materials.Redstone, 4800, 1500, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeMedium.get(Materials.Ultimate), new GT_MetaPipeEntity_Fluid(5166, "GT_Pipe_HighPressure", "High Pressure Fluid Pipe", 0.5F, Materials.Redstone, 7200, 1500, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeLarge.get(Materials.Ultimate), new GT_MetaPipeEntity_Fluid(5167, "GT_Pipe_HighPressure_Large", "Large High Pressure Fluid Pipe", 0.75F, Materials.Redstone, 9600, 1500, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeMedium.get(Materials.Superconductor), new GT_MetaPipeEntity_Fluid(5168, "GT_Pipe_PlasmaContain", "Plasma Containment Pipe", 0.5F, Materials.Glowstone, 240, 100000, true).getStackForm(1L));
        /*GT_OreDictUnificator.registerOre(OrePrefixes.pipeTiny.get(Materials.Plastic), new GT_MetaPipeEntity_Fluid(5170, "GT_Pipe_Plastic_Tiny", "Tiny Plastic Fluid Pipe", 0.25F, Materials.Plastic, 60, 350, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeSmall.get(Materials.Plastic), new GT_MetaPipeEntity_Fluid(5171, "GT_Pipe_Plastic_Small", "Small Plastic Fluid Pipe", 0.375F, Materials.Plastic, 120, 350, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeMedium.get(Materials.Plastic), new GT_MetaPipeEntity_Fluid(5172, "GT_Pipe_Plastic", "Plastic Fluid Pipe", 0.5F, Materials.Plastic, 360, 350, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeLarge.get(Materials.Plastic), new GT_MetaPipeEntity_Fluid(5173, "GT_Pipe_Plastic_Large", "Large Plastic Fluid Pipe", 0.75F, Materials.Plastic, 720, 350, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeHuge.get(Materials.Plastic), new GT_MetaPipeEntity_Fluid(5174, "GT_Pipe_Plastic_Huge", "Huge Plastic Fluid Pipe", 0.875F, Materials.Plastic, 1440, 350, true).getStackForm(1L));*/
        generateFluidPipes(Materials.Plastic, Materials.Plastic.mName, "Plastic", 5170, 360, 350, true);
        generateFluidPipes(Materials.Polytetrafluoroethylene, Materials.Polytetrafluoroethylene.mName, "PTFE", 5175, 480, 600, true);
        generateFluidMultiPipes(Materials.Copper, Materials.Copper.mName, "Copper", 5200, 60, 1000, true);
        generateFluidMultiPipes(Materials.Bronze, Materials.Bronze.mName, "Bronze", 5205, 120, 2000, true);
        generateFluidMultiPipes(Materials.Steel, Materials.Steel.mName, "Steel", 5210, 240, 2500, true);
        generateFluidMultiPipes(Materials.StainlessSteel, Materials.StainlessSteel.mName, "StainlessSteel", 5215, 360, 3000, true);
        generateFluidMultiPipes(Materials.Titanium, Materials.Titanium.mName, "Titanium", 5220, 480, 5000, true);
        generateFluidMultiPipes(Materials.TungstenSteel, Materials.TungstenSteel.mName, "Tungstensteel", 5225, 600, 7500, true);
        generateFluidMultiPipes(Materials.NiobiumTitanium, Materials.NiobiumTitanium.mName, "Niobium-Titanium", 5400, 900, 2900, true);
        generateFluidMultiPipes(Materials.Enderium, Materials.Enderium.mName, "Enderium", 5405, 1800, 15000, true);
        generateFluidMultiPipes(Materials.Naquadah, Materials.Naquadah.mName, "Naquadah", 5410, 9000, 19000, true);
        generateFluidMultiPipes(Materials.Neutronium, Materials.Neutronium.mName, "Neutronium", 5415, 16800, 1000000, true);
        generateFluidMultiPipes(Materials.WroughtIron, Materials.WroughtIron.mName, "Wrought Iron", 5420, 180, 2250, true);
        generateFluidMultiPipes(Materials.NetherStar, Materials.NetherStar.mName, "Nether Star", 5425, 19200, 1000000, true);
        generateFluidMultiPipes(Materials.Duranium, Materials.Duranium.mName, "Duranium", 5430, 12400, 1000000, true);
        generateFluidMultiPipes(Materials.Polybenzimidazole, Materials.Polybenzimidazole.mName, "PBI", 5435, 600, 1000, true);
        
        generateFluidMultiPipes(Materials.Plastic, Materials.Plastic.mName, "Plastic", 5230, 360, 350, true);
        generateFluidMultiPipes(Materials.Polytetrafluoroethylene, Materials.Polytetrafluoroethylene.mName, "PTFE", 5235, 480, 600, true);

        GT_ModHandler.addCraftingRecipe(GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Superconductor, 1L), bitsd, new Object[]{"WSW", aTextCableHull, "WSW", 'M', OrePrefixes.pipeSmall.get(Materials.Titanium), 'C', OrePrefixes.plate.get(Materials.NeodymiumMagnetic), 'W', OrePrefixes.plate.get(Materials.Plastic), 'S', OrePrefixes.wireGt02.get(Materials.SuperconductorIV)});


        /*GT_OreDictUnificator.registerOre(OrePrefixes.pipeMedium.get(Materials.Brass), new GT_MetaPipeEntity_Item(5602, "GT_Pipe_Brass", "Brass Item Pipe", 0.5F, Materials.Brass, 1, 32768, aBoolConst_0).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeLarge.get(Materials.Brass), new GT_MetaPipeEntity_Item(5603, "GT_Pipe_Brass_Large", "Large Brass Item Pipe", 0.75F, Materials.Brass, 2, 16384, aBoolConst_0).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeHuge.get(Materials.Brass), new GT_MetaPipeEntity_Item(5604, "GT_Pipe_Brass_Huge", "Huge Brass Item Pipe", 0.875F, Materials.Brass, 4, 8192, aBoolConst_0).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeRestrictiveMedium.get(Materials.Brass), new GT_MetaPipeEntity_Item(5607, "GT_Pipe_Restrictive_Brass", "Restrictive Brass Item Pipe", 0.5F, Materials.Brass, 1, 3276800, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeRestrictiveLarge.get(Materials.Brass), new GT_MetaPipeEntity_Item(5608, "GT_Pipe_Restrictive_Brass_Large", "Large Restrictive Brass Item Pipe", 0.75F, Materials.Brass, 2, 1638400, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeRestrictiveHuge.get(Materials.Brass), new GT_MetaPipeEntity_Item(5609, "GT_Pipe_Restrictive_Brass_Huge", "Huge Restrictive Brass Item Pipe", 0.875F, Materials.Brass, 4, 819200, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeMedium.get(Materials.Electrum), new GT_MetaPipeEntity_Item(5612, "GT_Pipe_Electrum", "Electrum Item Pipe", 0.5F, Materials.Electrum, 2, 16384, aBoolConst_0).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeLarge.get(Materials.Electrum), new GT_MetaPipeEntity_Item(5613, "GT_Pipe_Electrum_Large", "Large Electrum Item Pipe", 0.75F, Materials.Electrum, 4, 8192, aBoolConst_0).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeHuge.get(Materials.Electrum), new GT_MetaPipeEntity_Item(5614, "GT_Pipe_Electrum_Huge", "Huge Electrum Item Pipe", 0.875F, Materials.Electrum, 8, 4096, aBoolConst_0).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeRestrictiveMedium.get(Materials.Electrum), new GT_MetaPipeEntity_Item(5617, "GT_Pipe_Restrictive_Electrum", "Restrictive Electrum Item Pipe", 0.5F, Materials.Electrum, 2, 1638400, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeRestrictiveLarge.get(Materials.Electrum), new GT_MetaPipeEntity_Item(5618, "GT_Pipe_Restrictive_Electrum_Large", "Large Restrictive Electrum Item Pipe", 0.75F, Materials.Electrum, 4, 819200, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeRestrictiveHuge.get(Materials.Electrum), new GT_MetaPipeEntity_Item(5619, "GT_Pipe_Restrictive_Electrum_Huge", "Huge Restrictive Electrum Item Pipe", 0.875F, Materials.Electrum, 8, 409600, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeMedium.get(Materials.Platinum), new GT_MetaPipeEntity_Item(5622, "GT_Pipe_Platinum", "Platinum Item Pipe", 0.5F, Materials.Platinum, 4, 8192, aBoolConst_0).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeLarge.get(Materials.Platinum), new GT_MetaPipeEntity_Item(5623, "GT_Pipe_Platinum_Large", "Large Platinum Item Pipe", 0.75F, Materials.Platinum, 8, 4096, aBoolConst_0).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeHuge.get(Materials.Platinum), new GT_MetaPipeEntity_Item(5624, "GT_Pipe_Platinum_Huge", "Huge Platinum Item Pipe", 0.875F, Materials.Platinum, 16, 2048, aBoolConst_0).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeRestrictiveMedium.get(Materials.Platinum), new GT_MetaPipeEntity_Item(5627, "GT_Pipe_Restrictive_Platinum", "Restrictive Platinum Item Pipe", 0.5F, Materials.Platinum, 4, 819200, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeRestrictiveLarge.get(Materials.Platinum), new GT_MetaPipeEntity_Item(5628, "GT_Pipe_Restrictive_Platinum_Large", "Large Restrictive Platinum Item Pipe", 0.75F, Materials.Platinum, 8, 409600, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeRestrictiveHuge.get(Materials.Platinum), new GT_MetaPipeEntity_Item(5629, "GT_Pipe_Restrictive_Platinum_Huge", "Huge Restrictive Platinum Item Pipe", 0.875F, Materials.Platinum, 16, 204800, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeMedium.get(Materials.Osmium), new GT_MetaPipeEntity_Item(5632, "GT_Pipe_Osmium", "Osmium Item Pipe", 0.5F, Materials.Osmium, 8, 4096, aBoolConst_0).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeLarge.get(Materials.Osmium), new GT_MetaPipeEntity_Item(5633, "GT_Pipe_Osmium_Large", "Large Osmium Item Pipe", 0.75F, Materials.Osmium, 16, 2048, aBoolConst_0).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeHuge.get(Materials.Osmium), new GT_MetaPipeEntity_Item(5634, "GT_Pipe_Osmium_Huge", "Huge Osmium Item Pipe", 0.875F, Materials.Osmium, 32, 1024, aBoolConst_0).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeRestrictiveMedium.get(Materials.Osmium), new GT_MetaPipeEntity_Item(5637, "GT_Pipe_Restrictive_Osmium", "Restrictive Osmium Item Pipe", 0.5F, Materials.Osmium, 8, 409600, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeRestrictiveLarge.get(Materials.Osmium), new GT_MetaPipeEntity_Item(5638, "GT_Pipe_Restrictive_Osmium_Large", "Large Restrictive Osmium Item Pipe", 0.75F, Materials.Osmium, 16, 204800, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeRestrictiveHuge.get(Materials.Osmium), new GT_MetaPipeEntity_Item(5639, "GT_Pipe_Restrictive_Osmium_Huge", "Huge Restrictive Osmium Item Pipe", 0.875F, Materials.Osmium, 32, 102400, true).getStackForm(1L));*/
        generateItemPipes(Materials.Brass, Materials.Brass.mName, 5602, 1);
        generateItemPipes(Materials.Electrum, Materials.Electrum.mName, 5612, 2);
        generateItemPipes(Materials.Platinum, Materials.Platinum.mName, 5622, 4);
        generateItemPipes(Materials.Osmium, Materials.Osmium.mName, 5632, 8);
        generateItemPipes(Materials.PolyvinylChloride, Materials.PolyvinylChloride.mName, "PVC", 5640, 4);
        //generateItemPipes(Materials.WroughtIron, Materials.WroughtIron.mName, 5646, 1);
        generateItemPipes(Materials.Nickel, Materials.Nickel.mName, 5652, 1);
        generateItemPipes(Materials.Cobalt, Materials.Cobalt.mName, 5658, 2);
        generateItemPipes(Materials.Aluminium, Materials.Aluminium.mName, 5664, 2);
        generateItemPipes(Materials.ElectrumFlux, Materials.ElectrumFlux.mName, 5670, 16);
        generateItemPipes(Materials.Diamericiumtitanium, Materials.Diamericiumtitanium.mName, 5676, 64);
        generateItemPipes(Materials.Quantium, Materials.Quantium.mName, 5682, 128);
        generateItemPipes(Materials.Tin, Materials.Tin.mName, 5688, 1);
        
        ItemList.Automation_ChestBuffer_ULV.set(new GT_MetaTileEntity_ChestBuffer(9230, "automation.chestbuffer.tier.00", "Ultra Low Voltage Chest Buffer", 0).getStackForm(1L));
        ItemList.Automation_ChestBuffer_LV.set(new GT_MetaTileEntity_ChestBuffer(9231, "automation.chestbuffer.tier.01", "Low Voltage Chest Buffer", 1).getStackForm(1L));
        ItemList.Automation_ChestBuffer_MV.set(new GT_MetaTileEntity_ChestBuffer(9232, "automation.chestbuffer.tier.02", "Medium Voltage Chest Buffer", 2).getStackForm(1L));
        ItemList.Automation_ChestBuffer_HV.set(new GT_MetaTileEntity_ChestBuffer(9233, "automation.chestbuffer.tier.03", "High Voltage Chest Buffer", 3).getStackForm(1L));
        ItemList.Automation_ChestBuffer_EV.set(new GT_MetaTileEntity_ChestBuffer(9234, "automation.chestbuffer.tier.04", "Extreme Voltage Chest Buffer", 4).getStackForm(1L));
        ItemList.Automation_ChestBuffer_IV.set(new GT_MetaTileEntity_ChestBuffer(9235, "automation.chestbuffer.tier.05", "Insane Voltage Chest Buffer", 5).getStackForm(1L));
        ItemList.Automation_ChestBuffer_LuV.set(new GT_MetaTileEntity_ChestBuffer(9236, "automation.chestbuffer.tier.06", "Ludicrous Voltage Chest Buffer", 6).getStackForm(1L));
        ItemList.Automation_ChestBuffer_ZPM.set(new GT_MetaTileEntity_ChestBuffer(9237, "automation.chestbuffer.tier.07", "ZPM Voltage Chest Buffer", 7).getStackForm(1L));
        ItemList.Automation_ChestBuffer_UV.set(new GT_MetaTileEntity_ChestBuffer(9238, "automation.chestbuffer.tier.08", "Ultimate Voltage Chest Buffer", 8).getStackForm(1L));
        ItemList.Automation_ChestBuffer_MAX.set(new GT_MetaTileEntity_ChestBuffer(9239, "automation.chestbuffer.tier.09", "MAX Voltage Chest Buffer", 9).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(ItemList.Automation_ChestBuffer_ULV.get(1L, new Object[0]), bitsd, new Object[]{"CMV", " X ", 'M', ItemList.Hull_ULV, 'V', ItemList.Conveyor_Module_LV, 'C', OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Basic)});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_ChestBuffer_LV.get(1L, new Object[0]), bitsd, new Object[]{"CMV", " X ", 'M', ItemList.Hull_LV, 'V', ItemList.Conveyor_Module_LV, 'C', OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Basic)});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_ChestBuffer_MV.get(1L, new Object[0]), bitsd, new Object[]{"CMV", " X ", 'M', ItemList.Hull_MV, 'V', ItemList.Conveyor_Module_MV, 'C', OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Basic)});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_ChestBuffer_HV.get(1L, new Object[0]), bitsd, new Object[]{"CMV", " X ", 'M', ItemList.Hull_HV, 'V', ItemList.Conveyor_Module_HV, 'C', OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Basic)});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_ChestBuffer_EV.get(1L, new Object[0]), bitsd, new Object[]{"CMV", " X ", 'M', ItemList.Hull_EV, 'V', ItemList.Conveyor_Module_EV, 'C', OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Basic)});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_ChestBuffer_IV.get(1L, new Object[0]), bitsd, new Object[]{"CMV", " X ", 'M', ItemList.Hull_IV, 'V', ItemList.Conveyor_Module_IV, 'C', OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Basic)});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_ChestBuffer_LuV.get(1L, new Object[0]), bitsd, new Object[]{"CMV", " X ", 'M', ItemList.Hull_LuV, 'V', ItemList.Conveyor_Module_LuV, 'C', OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Basic)});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_ChestBuffer_ZPM.get(1L, new Object[0]), bitsd, new Object[]{"CMV", " X ", 'M', ItemList.Hull_ZPM, 'V', ItemList.Conveyor_Module_ZPM, 'C', OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Basic)});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_ChestBuffer_UV.get(1L, new Object[0]), bitsd, new Object[]{"CMV", " X ", 'M', ItemList.Hull_UV, 'V', ItemList.Conveyor_Module_UV, 'C', OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Basic)});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_ChestBuffer_MAX.get(1L, new Object[0]), bitsd, new Object[]{"CMV", " X ", 'M', ItemList.Hull_MAX, 'V', ItemList.Conveyor_Module_UV, 'C', OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Basic)});

        ItemList.Automation_Filter_ULV.set(new GT_MetaTileEntity_Filter(9240, "automation.filter.tier.00", "Ultra Low Voltage Item Filter", 0).getStackForm(1L));
        ItemList.Automation_Filter_LV.set(new GT_MetaTileEntity_Filter(9241, "automation.filter.tier.01", "Low Voltage Item Filter", 1).getStackForm(1L));
        ItemList.Automation_Filter_MV.set(new GT_MetaTileEntity_Filter(9242, "automation.filter.tier.02", "Medium Voltage Item Filter", 2).getStackForm(1L));
        ItemList.Automation_Filter_HV.set(new GT_MetaTileEntity_Filter(9243, "automation.filter.tier.03", "High Voltage Item Filter", 3).getStackForm(1L));
        ItemList.Automation_Filter_EV.set(new GT_MetaTileEntity_Filter(9244, "automation.filter.tier.04", "Extreme Voltage Item Filter", 4).getStackForm(1L));
        ItemList.Automation_Filter_IV.set(new GT_MetaTileEntity_Filter(9245, "automation.filter.tier.05", "Insane Voltage Item Filter", 5).getStackForm(1L));
        ItemList.Automation_Filter_LuV.set(new GT_MetaTileEntity_Filter(9246, "automation.filter.tier.06", "Ludicrous Voltage Item Filter", 6).getStackForm(1L));
        ItemList.Automation_Filter_ZPM.set(new GT_MetaTileEntity_Filter(9247, "automation.filter.tier.07", "ZPM Voltage Item Filter", 7).getStackForm(1L));
        ItemList.Automation_Filter_UV.set(new GT_MetaTileEntity_Filter(9248, "automation.filter.tier.08", "Ultimate Voltage Item Filter", 8).getStackForm(1L));
        ItemList.Automation_Filter_MAX.set(new GT_MetaTileEntity_Filter(9249, "automation.filter.tier.09", "MAX Voltage Item Filter", 9).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(ItemList.Automation_Filter_ULV.get(1L, new Object[0]), bitsd, new Object[]{" F ", "CMV", " X ", 'M', ItemList.Hull_ULV, 'V', ItemList.Conveyor_Module_LV, 'C', OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X', OrePrefixes.circuit.get(Materials.Basic)});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_Filter_LV.get(1L, new Object[0]), bitsd, new Object[]{" F ", "CMV", " X ", 'M', ItemList.Hull_LV, 'V', ItemList.Conveyor_Module_LV, 'C', OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X', OrePrefixes.circuit.get(Materials.Basic)});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_Filter_MV.get(1L, new Object[0]), bitsd, new Object[]{" F ", "CMV", " X ", 'M', ItemList.Hull_MV, 'V', ItemList.Conveyor_Module_MV, 'C', OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X', OrePrefixes.circuit.get(Materials.Basic)});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_Filter_HV.get(1L, new Object[0]), bitsd, new Object[]{" F ", "CMV", " X ", 'M', ItemList.Hull_HV, 'V', ItemList.Conveyor_Module_HV, 'C', OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X', OrePrefixes.circuit.get(Materials.Basic)});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_Filter_EV.get(1L, new Object[0]), bitsd, new Object[]{" F ", "CMV", " X ", 'M', ItemList.Hull_EV, 'V', ItemList.Conveyor_Module_EV, 'C', OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X', OrePrefixes.circuit.get(Materials.Basic)});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_Filter_IV.get(1L, new Object[0]), bitsd, new Object[]{" F ", "CMV", " X ", 'M', ItemList.Hull_IV, 'V', ItemList.Conveyor_Module_IV, 'C', OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X', OrePrefixes.circuit.get(Materials.Basic)});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_Filter_LuV.get(1L, new Object[0]), bitsd, new Object[]{" F ", "CMV", " X ", 'M', ItemList.Hull_LuV, 'V', ItemList.Conveyor_Module_LuV, 'C', OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X', OrePrefixes.circuit.get(Materials.Basic)});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_Filter_ZPM.get(1L, new Object[0]), bitsd, new Object[]{" F ", "CMV", " X ", 'M', ItemList.Hull_ZPM, 'V', ItemList.Conveyor_Module_ZPM, 'C', OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X', OrePrefixes.circuit.get(Materials.Basic)});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_Filter_UV.get(1L, new Object[0]), bitsd, new Object[]{" F ", "CMV", " X ", 'M', ItemList.Hull_UV, 'V', ItemList.Conveyor_Module_UV, 'C', OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X', OrePrefixes.circuit.get(Materials.Basic)});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_Filter_MAX.get(1L, new Object[0]), bitsd, new Object[]{" F ", "CMV", " X ", 'M', ItemList.Hull_MAX, 'V', ItemList.Conveyor_Module_UV, 'C', OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X', OrePrefixes.circuit.get(Materials.Basic)});

        ItemList.Automation_TypeFilter_ULV.set(new GT_MetaTileEntity_TypeFilter(9250, "automation.typefilter.tier.00", "Ultra Low Voltage Type Filter", 0).getStackForm(1L));
        ItemList.Automation_TypeFilter_LV.set(new GT_MetaTileEntity_TypeFilter(9251, "automation.typefilter.tier.01", "Low Voltage Type Filter", 1).getStackForm(1L));
        ItemList.Automation_TypeFilter_MV.set(new GT_MetaTileEntity_TypeFilter(9252, "automation.typefilter.tier.02", "Medium Voltage Type Filter", 2).getStackForm(1L));
        ItemList.Automation_TypeFilter_HV.set(new GT_MetaTileEntity_TypeFilter(9253, "automation.typefilter.tier.03", "High Voltage Type Filter", 3).getStackForm(1L));
        ItemList.Automation_TypeFilter_EV.set(new GT_MetaTileEntity_TypeFilter(9254, "automation.typefilter.tier.04", "Extreme Voltage Type Filter", 4).getStackForm(1L));
        ItemList.Automation_TypeFilter_IV.set(new GT_MetaTileEntity_TypeFilter(9255, "automation.typefilter.tier.05", "Insane Voltage Type Filter", 5).getStackForm(1L));
        ItemList.Automation_TypeFilter_LuV.set(new GT_MetaTileEntity_TypeFilter(9256, "automation.typefilter.tier.06", "Ludicrous Voltage Type Filter", 6).getStackForm(1L));
        ItemList.Automation_TypeFilter_ZPM.set(new GT_MetaTileEntity_TypeFilter(9257, "automation.typefilter.tier.07", "ZPM Voltage Type Filter", 7).getStackForm(1L));
        ItemList.Automation_TypeFilter_UV.set(new GT_MetaTileEntity_TypeFilter(9258, "automation.typefilter.tier.08", "Ultimate Voltage Type Filter", 8).getStackForm(1L));
        ItemList.Automation_TypeFilter_MAX.set(new GT_MetaTileEntity_TypeFilter(9259, "automation.typefilter.tier.09", "MAX Voltage Type Filter", 9).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(ItemList.Automation_TypeFilter_ULV.get(1L, new Object[0]), bitsd, new Object[]{" F ", "CMV", " X ", 'M', ItemList.Hull_ULV, 'V', ItemList.Conveyor_Module_LV, 'C', OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X', OrePrefixes.circuit.get(Materials.Advanced)});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_TypeFilter_LV.get(1L, new Object[0]), bitsd, new Object[]{" F ", "CMV", " X ", 'M', ItemList.Hull_LV, 'V', ItemList.Conveyor_Module_LV, 'C', OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X', OrePrefixes.circuit.get(Materials.Advanced)});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_TypeFilter_MV.get(1L, new Object[0]), bitsd, new Object[]{" F ", "CMV", " X ", 'M', ItemList.Hull_MV, 'V', ItemList.Conveyor_Module_MV, 'C', OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X', OrePrefixes.circuit.get(Materials.Advanced)});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_TypeFilter_HV.get(1L, new Object[0]), bitsd, new Object[]{" F ", "CMV", " X ", 'M', ItemList.Hull_HV, 'V', ItemList.Conveyor_Module_HV, 'C', OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X', OrePrefixes.circuit.get(Materials.Advanced)});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_TypeFilter_EV.get(1L, new Object[0]), bitsd, new Object[]{" F ", "CMV", " X ", 'M', ItemList.Hull_EV, 'V', ItemList.Conveyor_Module_EV, 'C', OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X', OrePrefixes.circuit.get(Materials.Advanced)});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_TypeFilter_IV.get(1L, new Object[0]), bitsd, new Object[]{" F ", "CMV", " X ", 'M', ItemList.Hull_IV, 'V', ItemList.Conveyor_Module_IV, 'C', OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X', OrePrefixes.circuit.get(Materials.Advanced)});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_TypeFilter_LuV.get(1L, new Object[0]), bitsd, new Object[]{" F ", "CMV", " X ", 'M', ItemList.Hull_LuV, 'V', ItemList.Conveyor_Module_LuV, 'C', OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X', OrePrefixes.circuit.get(Materials.Advanced)});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_TypeFilter_ZPM.get(1L, new Object[0]), bitsd, new Object[]{" F ", "CMV", " X ", 'M', ItemList.Hull_ZPM, 'V', ItemList.Conveyor_Module_ZPM, 'C', OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X', OrePrefixes.circuit.get(Materials.Advanced)});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_TypeFilter_UV.get(1L, new Object[0]), bitsd, new Object[]{" F ", "CMV", " X ", 'M', ItemList.Hull_UV, 'V', ItemList.Conveyor_Module_UV, 'C', OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X', OrePrefixes.circuit.get(Materials.Advanced)});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_TypeFilter_MAX.get(1L, new Object[0]), bitsd, new Object[]{" F ", "CMV", " X ", 'M', ItemList.Hull_MAX, 'V', ItemList.Conveyor_Module_UV, 'C', OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X', OrePrefixes.circuit.get(Materials.Advanced)});

        ItemList.Automation_Regulator_ULV.set(new GT_MetaTileEntity_Regulator(9270, "automation.regulator.tier.00", "Ultra Low Voltage Regulator", 0).getStackForm(1L));
        ItemList.Automation_Regulator_LV.set(new GT_MetaTileEntity_Regulator(9271, "automation.regulator.tier.01", "Low Voltage Regulator", 1).getStackForm(1L));
        ItemList.Automation_Regulator_MV.set(new GT_MetaTileEntity_Regulator(9272, "automation.regulator.tier.02", "Medium Voltage Regulator", 2).getStackForm(1L));
        ItemList.Automation_Regulator_HV.set(new GT_MetaTileEntity_Regulator(9273, "automation.regulator.tier.03", "High Voltage Regulator", 3).getStackForm(1L));
        ItemList.Automation_Regulator_EV.set(new GT_MetaTileEntity_Regulator(9274, "automation.regulator.tier.04", "Extreme Voltage Regulator", 4).getStackForm(1L));
        ItemList.Automation_Regulator_IV.set(new GT_MetaTileEntity_Regulator(9275, "automation.regulator.tier.05", "Insane Voltage Regulator", 5).getStackForm(1L));
        ItemList.Automation_Regulator_LuV.set(new GT_MetaTileEntity_Regulator(9276, "automation.regulator.tier.06", "Ludicrous Voltage Regulator", 6).getStackForm(1L));
        ItemList.Automation_Regulator_ZPM.set(new GT_MetaTileEntity_Regulator(9277, "automation.regulator.tier.07", "ZPM Voltage Regulator", 7).getStackForm(1L));
        ItemList.Automation_Regulator_UV.set(new GT_MetaTileEntity_Regulator(9278, "automation.regulator.tier.08", "Ultimate Voltage Regulator", 8).getStackForm(1L));
        ItemList.Automation_Regulator_MAX.set(new GT_MetaTileEntity_Regulator(9279, "automation.regulator.tier.09", "MAX Voltage Regulator", 9).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(ItemList.Automation_Regulator_ULV.get(1L, new Object[0]), bitsd, new Object[]{"XFX", "VMV", "XCX", 'M', ItemList.Hull_ULV, 'V', ItemList.Robot_Arm_LV, 'C', OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X', OrePrefixes.circuit.get(Materials.Advanced)});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_Regulator_LV.get(1L, new Object[0]), bitsd, new Object[]{"XFX", "VMV", "XCX", 'M', ItemList.Hull_LV, 'V', ItemList.Robot_Arm_LV, 'C', OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X', OrePrefixes.circuit.get(Materials.Advanced)});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_Regulator_MV.get(1L, new Object[0]), bitsd, new Object[]{"XFX", "VMV", "XCX", 'M', ItemList.Hull_MV, 'V', ItemList.Robot_Arm_MV, 'C', OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X', OrePrefixes.circuit.get(Materials.Advanced)});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_Regulator_HV.get(1L, new Object[0]), bitsd, new Object[]{"XFX", "VMV", "XCX", 'M', ItemList.Hull_HV, 'V', ItemList.Robot_Arm_HV, 'C', OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X', OrePrefixes.circuit.get(Materials.Advanced)});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_Regulator_EV.get(1L, new Object[0]), bitsd, new Object[]{"XFX", "VMV", "XCX", 'M', ItemList.Hull_EV, 'V', ItemList.Robot_Arm_EV, 'C', OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X', OrePrefixes.circuit.get(Materials.Advanced)});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_Regulator_IV.get(1L, new Object[0]), bitsd, new Object[]{"XFX", "VMV", "XCX", 'M', ItemList.Hull_IV, 'V', ItemList.Robot_Arm_IV, 'C', OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X', OrePrefixes.circuit.get(Materials.Advanced)});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_Regulator_LuV.get(1L, new Object[0]), bitsd, new Object[]{"XFX", "VMV", "XCX", 'M', ItemList.Hull_LuV, 'V', ItemList.Robot_Arm_LuV, 'C', OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X', OrePrefixes.circuit.get(Materials.Advanced)});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_Regulator_ZPM.get(1L, new Object[0]), bitsd, new Object[]{"XFX", "VMV", "XCX", 'M', ItemList.Hull_ZPM, 'V', ItemList.Robot_Arm_ZPM, 'C', OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X', OrePrefixes.circuit.get(Materials.Advanced)});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_Regulator_UV.get(1L, new Object[0]), bitsd, new Object[]{"XFX", "VMV", "XCX", 'M', ItemList.Hull_UV, 'V', ItemList.Robot_Arm_UV, 'C', OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X', OrePrefixes.circuit.get(Materials.Advanced)});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_Regulator_MAX.get(1L, new Object[0]), bitsd, new Object[]{"XFX", "VMV", "XCX", 'M', ItemList.Hull_MAX, 'V', ItemList.Robot_Arm_UV, 'C', OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X', OrePrefixes.circuit.get(Materials.Advanced)});

        ItemList.Automation_SuperBuffer_ULV.set(new GT_MetaTileEntity_SuperBuffer(9300, "automation.superbuffer.tier.00", "Ultra Low Voltage Super Buffer", 0).getStackForm(1L));
        ItemList.Automation_SuperBuffer_LV.set(new GT_MetaTileEntity_SuperBuffer(9301, "automation.superbuffer.tier.01", "Low Voltage Super Buffer", 1).getStackForm(1L));
        ItemList.Automation_SuperBuffer_MV.set(new GT_MetaTileEntity_SuperBuffer(9302, "automation.superbuffer.tier.02", "Medium Voltage Super Buffer", 2).getStackForm(1L));
        ItemList.Automation_SuperBuffer_HV.set(new GT_MetaTileEntity_SuperBuffer(9303, "automation.superbuffer.tier.03", "High Voltage Super Buffer", 3).getStackForm(1L));
        ItemList.Automation_SuperBuffer_EV.set(new GT_MetaTileEntity_SuperBuffer(9304, "automation.superbuffer.tier.04", "Extreme Voltage Super Buffer", 4).getStackForm(1L));
        ItemList.Automation_SuperBuffer_IV.set(new GT_MetaTileEntity_SuperBuffer(9305, "automation.superbuffer.tier.05", "Insane Voltage Super Buffer", 5).getStackForm(1L));
        ItemList.Automation_SuperBuffer_LuV.set(new GT_MetaTileEntity_SuperBuffer(9306, "automation.superbuffer.tier.06", "Ludicrous Voltage Super Buffer", 6).getStackForm(1L));
        ItemList.Automation_SuperBuffer_ZPM.set(new GT_MetaTileEntity_SuperBuffer(9307, "automation.superbuffer.tier.07", "ZPM Voltage Super Buffer", 7).getStackForm(1L));
        ItemList.Automation_SuperBuffer_UV.set(new GT_MetaTileEntity_SuperBuffer(9308, "automation.superbuffer.tier.08", "Ultimate Voltage Super Buffer", 8).getStackForm(1L));
        ItemList.Automation_SuperBuffer_MAX.set(new GT_MetaTileEntity_SuperBuffer(9309, "automation.superbuffer.tier.09", "MAX Voltage Super Buffer", 9).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(ItemList.Automation_SuperBuffer_ULV.get(1L, new Object[0]), bitsd, new Object[]{"DMV", 'M', ItemList.Hull_ULV, 'V', ItemList.Conveyor_Module_LV, 'D', ItemList.Tool_DataOrb});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_SuperBuffer_LV.get(1L, new Object[0]), bitsd, new Object[]{"DMV", 'M', ItemList.Hull_LV, 'V', ItemList.Conveyor_Module_LV, 'D', ItemList.Tool_DataOrb});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_SuperBuffer_MV.get(1L, new Object[0]), bitsd, new Object[]{"DMV", 'M', ItemList.Hull_MV, 'V', ItemList.Conveyor_Module_MV, 'D', ItemList.Tool_DataOrb});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_SuperBuffer_HV.get(1L, new Object[0]), bitsd, new Object[]{"DMV", 'M', ItemList.Hull_HV, 'V', ItemList.Conveyor_Module_HV, 'D', ItemList.Tool_DataOrb});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_SuperBuffer_EV.get(1L, new Object[0]), bitsd, new Object[]{"DMV", 'M', ItemList.Hull_EV, 'V', ItemList.Conveyor_Module_EV, 'D', ItemList.Tool_DataOrb});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_SuperBuffer_IV.get(1L, new Object[0]), bitsd, new Object[]{"DMV", 'M', ItemList.Hull_IV, 'V', ItemList.Conveyor_Module_IV, 'D', ItemList.Tool_DataOrb});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_SuperBuffer_LuV.get(1L, new Object[0]), bitsd, new Object[]{"DMV", 'M', ItemList.Hull_LuV, 'V', ItemList.Conveyor_Module_LuV, 'D', ItemList.Tool_DataOrb});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_SuperBuffer_ZPM.get(1L, new Object[0]), bitsd, new Object[]{"DMV", 'M', ItemList.Hull_ZPM, 'V', ItemList.Conveyor_Module_ZPM, 'D', ItemList.Tool_DataOrb});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_SuperBuffer_UV.get(1L, new Object[0]), bitsd, new Object[]{"DMV", 'M', ItemList.Hull_UV, 'V', ItemList.Conveyor_Module_UV, 'D', ItemList.Tool_DataOrb});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_SuperBuffer_MAX.get(1L, new Object[0]), bitsd, new Object[]{"DMV", 'M', ItemList.Hull_MAX, 'V', ItemList.Conveyor_Module_UV, 'D', ItemList.Tool_DataOrb});

        GT_ModHandler.addCraftingRecipe(ItemList.Automation_SuperBuffer_ULV.get(1L, new Object[0]), bitsd, new Object[]{"DMV", "DDD", 'M', ItemList.Hull_ULV, 'V', ItemList.Conveyor_Module_LV, 'D', ItemList.Tool_DataStick});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_SuperBuffer_LV.get(1L, new Object[0]), bitsd, new Object[]{"DMV", "DDD", 'M', ItemList.Hull_LV, 'V', ItemList.Conveyor_Module_LV, 'D', ItemList.Tool_DataStick});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_SuperBuffer_MV.get(1L, new Object[0]), bitsd, new Object[]{"DMV", "DDD", 'M', ItemList.Hull_MV, 'V', ItemList.Conveyor_Module_MV, 'D', ItemList.Tool_DataStick});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_SuperBuffer_HV.get(1L, new Object[0]), bitsd, new Object[]{"DMV", "DDD", 'M', ItemList.Hull_HV, 'V', ItemList.Conveyor_Module_HV, 'D', ItemList.Tool_DataStick});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_SuperBuffer_EV.get(1L, new Object[0]), bitsd, new Object[]{"DMV", "DDD", 'M', ItemList.Hull_EV, 'V', ItemList.Conveyor_Module_EV, 'D', ItemList.Tool_DataStick});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_SuperBuffer_IV.get(1L, new Object[0]), bitsd, new Object[]{"DMV", "DDD", 'M', ItemList.Hull_IV, 'V', ItemList.Conveyor_Module_IV, 'D', ItemList.Tool_DataStick});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_SuperBuffer_LuV.get(1L, new Object[0]), bitsd, new Object[]{"DMV", "DDD", 'M', ItemList.Hull_LuV, 'V', ItemList.Conveyor_Module_LuV, 'D', ItemList.Tool_DataStick});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_SuperBuffer_ZPM.get(1L, new Object[0]), bitsd, new Object[]{"DMV", "DDD", 'M', ItemList.Hull_ZPM, 'V', ItemList.Conveyor_Module_ZPM, 'D', ItemList.Tool_DataStick});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_SuperBuffer_UV.get(1L, new Object[0]), bitsd, new Object[]{"DMV", "DDD", 'M', ItemList.Hull_UV, 'V', ItemList.Conveyor_Module_UV, 'D', ItemList.Tool_DataStick});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_SuperBuffer_MAX.get(1L, new Object[0]), bitsd, new Object[]{"DMV", "DDD", 'M', ItemList.Hull_MAX, 'V', ItemList.Conveyor_Module_UV, 'D', ItemList.Tool_DataStick});

        ItemList.Automation_ItemDistributor_ULV.set(new GT_MetaTileEntity_ItemDistributor(9320, "automation.itemdistributor.tier.00", "Ultra Low Voltage Item Distributor", 0).getStackForm(1L));
        ItemList.Automation_ItemDistributor_LV.set(new GT_MetaTileEntity_ItemDistributor(9321,  "automation.itemdistributor.tier.01", "Low Voltage Item Distributor", 1).getStackForm(1L));
        ItemList.Automation_ItemDistributor_MV.set(new GT_MetaTileEntity_ItemDistributor(9322,  "automation.itemdistributor.tier.02", "Medium Voltage Item Distributor", 2).getStackForm(1L));
        ItemList.Automation_ItemDistributor_HV.set(new GT_MetaTileEntity_ItemDistributor(9323,  "automation.itemdistributor.tier.03", "High Voltage Item Distributor", 3).getStackForm(1L));
        ItemList.Automation_ItemDistributor_EV.set(new GT_MetaTileEntity_ItemDistributor(9324,  "automation.itemdistributor.tier.04", "Extreme Voltage Item Distributor", 4).getStackForm(1L));
        ItemList.Automation_ItemDistributor_IV.set(new GT_MetaTileEntity_ItemDistributor(9325,  "automation.itemdistributor.tier.05", "Insane Voltage Item Distributor", 5).getStackForm(1L));
        ItemList.Automation_ItemDistributor_LuV.set(new GT_MetaTileEntity_ItemDistributor(9326, "automation.itemdistributor.tier.06", "Ludicrous Voltage Item Distributor", 6).getStackForm(1L));
        ItemList.Automation_ItemDistributor_ZPM.set(new GT_MetaTileEntity_ItemDistributor(9327, "automation.itemdistributor.tier.07", "ZPM Voltage Item Distributor", 7).getStackForm(1L));
        ItemList.Automation_ItemDistributor_UV.set(new GT_MetaTileEntity_ItemDistributor(9328,  "automation.itemdistributor.tier.08", "Ultimate Voltage Item Distributor", 8).getStackForm(1L));
        ItemList.Automation_ItemDistributor_MAX.set(new GT_MetaTileEntity_ItemDistributor(9329, "automation.itemdistributor.tier.09", "MAX Voltage Item Distributor", 9).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(ItemList.Automation_ItemDistributor_ULV.get(1L, new Object[0]), bitsd, new Object[]{"XCX", "VMV", " V ", 'M', ItemList.Hull_ULV, 'V', ItemList.Conveyor_Module_LV, 'C', OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Good)});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_ItemDistributor_LV.get(1L,  new Object[0]), bitsd, new Object[]{"XCX", "VMV", " V ", 'M', ItemList.Hull_LV,  'V', ItemList.Conveyor_Module_LV, 'C', OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Good)});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_ItemDistributor_MV.get(1L,  new Object[0]), bitsd, new Object[]{"XCX", "VMV", " V ", 'M', ItemList.Hull_MV,  'V', ItemList.Conveyor_Module_MV, 'C', OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Good)});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_ItemDistributor_HV.get(1L,  new Object[0]), bitsd, new Object[]{"XCX", "VMV", " V ", 'M', ItemList.Hull_HV,  'V', ItemList.Conveyor_Module_HV, 'C', OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Good)});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_ItemDistributor_EV.get(1L,  new Object[0]), bitsd, new Object[]{"XCX", "VMV", " V ", 'M', ItemList.Hull_EV,  'V', ItemList.Conveyor_Module_EV, 'C', OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Good)});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_ItemDistributor_IV.get(1L,  new Object[0]), bitsd, new Object[]{"XCX", "VMV", " V ", 'M', ItemList.Hull_IV,  'V', ItemList.Conveyor_Module_IV, 'C', OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Good)});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_ItemDistributor_LuV.get(1L, new Object[0]), bitsd, new Object[]{"XCX", "VMV", " V ", 'M', ItemList.Hull_LuV, 'V', ItemList.Conveyor_Module_LuV, 'C', OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Good)});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_ItemDistributor_ZPM.get(1L, new Object[0]), bitsd, new Object[]{"XCX", "VMV", " V ", 'M', ItemList.Hull_ZPM, 'V', ItemList.Conveyor_Module_ZPM, 'C', OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Good)});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_ItemDistributor_UV.get(1L,  new Object[0]), bitsd, new Object[]{"XCX", "VMV", " V ", 'M', ItemList.Hull_UV,  'V', ItemList.Conveyor_Module_UV, 'C', OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Good)});
        GT_ModHandler.addCraftingRecipe(ItemList.Automation_ItemDistributor_MAX.get(1L, new Object[0]), bitsd, new Object[]{"XCX", "VMV", " V ", 'M', ItemList.Hull_MAX, 'V', ItemList.Conveyor_Module_UV, 'C', OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Good)});
        
      //PlasmaGenerator
        ItemList.Generator_Plasma_UV.set(new GT_MetaTileEntity_PlasmaGenerator(
				10752, "basicgenerator.plasmagenerator.tier.08", "Plasma Generator Mark IV",8).getStackForm(1L));

		ItemList.Generator_Plasma_UHV.set(new GT_MetaTileEntity_PlasmaGenerator(
				10753, "basicgenerator.plasmagenerator.tier.09", "Ultimate Pocket Sun", 9).getStackForm(1L));

		ItemList.Generator_Plasma_UEV.set(new GT_MetaTileEntity_PlasmaGenerator(
				10754, "basicgenerator.plasmagenerator.tier.10", "Legendary Pocket Sun", 10).getStackForm(1L));

		ItemList.Generator_Plasma_UIV.set(new GT_MetaTileEntity_PlasmaGenerator(
				10755, "basicgenerator.plasmagenerator.tier.11", "Legendary Pocket Sun II",11).getStackForm(1L));

		ItemList.Generator_Plasma_UMV.set(new GT_MetaTileEntity_PlasmaGenerator(
				10756, "basicgenerator.plasmagenerator.tier.12", "Legendary Pocket Sun III",12).getStackForm(1L));

		ItemList.Generator_Plasma_UXV.set(new GT_MetaTileEntity_PlasmaGenerator(
				10757, "basicgenerator.plasmagenerator.tier.13", "Legendary Pocket Sun IV",13).getStackForm(1L));

		GT_ModHandler.addCraftingRecipe(ItemList.Generator_Plasma_UV.get(1L),
				bitsd,
				new Object[] { "UCU", "FMF", "WCW",
						'M', ItemList.Hull_UV,
						'F', ItemList.Field_Generator_ZPM,
						'C', OrePrefixes.circuit.get(Materials.Superconductor),
						'W', OrePrefixes.wireGt04.get(Materials.ElectrumFlux),
						'U', OrePrefixes.stick.get(Materials.Tritanium) });
		
		GT_ModHandler.addCraftingRecipe(ItemList.Generator_Plasma_UHV.get(1L),
				bitsd,
				new Object[] { "UCU", "FMF", "WCW",
						'M', ItemList.Hull_MAX,
						'F', ItemList.Field_Generator_UV,
						'C', OrePrefixes.circuit.get(Materials.Infinite),
						'W', OrePrefixes.wireGt04.get(Materials.Superconductor),
						'U', OrePrefixes.stickLong.get(Materials.Tritanium) });
		
		GT_ModHandler.addCraftingRecipe(ItemList.Generator_Plasma_UEV.get(1L),
				bitsd,
				new Object[] { "UCU", "FMF", "WCW",
						'M', ItemList.Hull_UEV,
						'F', ItemList.Field_Generator_UHV,
						'C', OrePrefixes.circuit.get(Materials.Bio),
						'W', OrePrefixes.wireGt04.get(Materials.SuperconductorUEV),
						'U', OrePrefixes.stickLong.get(Materials.Neutronium) });
		
		GT_ModHandler.addCraftingRecipe(ItemList.Generator_Plasma_UIV.get(1L),
				bitsd,
				new Object[] { "UCU", "FMF", "WCW",
						'M', ItemList.Hull_UIV,
						'F', ItemList.Field_Generator_UEV,
						'C', OrePrefixes.circuit.get(Materials.Bio),
						'W', OrePrefixes.wireGt04.get(Materials.Neutronium),
						'U', OrePrefixes.block.get(Materials.Neutronium) });
		
      //Alloy Smelter
    	ItemList.AlloySmelterUHV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10763, "basicmachine.alloysmelter.tier.09", "Epic Alloy Integrator", 9, "HighTech combination Smelter",
				GT_Recipe.GT_Recipe_Map.sAlloySmelterRecipes, 2, 1, 0, 0, 1, "AlloySmelter.png",
				GregTech_API.sSoundList.get(208), false, false, 0, "ALLOY_SMELTER",new Object[]{"ECE", aTextCableHull, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE}).getStackForm(1L));

		ItemList.AlloySmelterUEV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10764, "basicmachine.alloysmelter.tier.10", "Epic Alloy Integrator II",10, "HighTech combination Smelter",
				GT_Recipe.GT_Recipe_Map.sAlloySmelterRecipes, 2, 1, 0, 0, 1, "AlloySmelter.png",
				GregTech_API.sSoundList.get(208), false, false, 0, "ALLOY_SMELTER",new Object[]{"ECE", aTextCableHull, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE}).getStackForm(1L));

		ItemList.AlloySmelterUIV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10765, "basicmachine.alloysmelter.tier.11", "Epic Alloy Integrator III",11, "HighTech combination Smelter",
				GT_Recipe.GT_Recipe_Map.sAlloySmelterRecipes, 2, 1, 0, 0, 1, "AlloySmelter.png",
				GregTech_API.sSoundList.get(208), false, false, 0, "ALLOY_SMELTER",null).getStackForm(1L));

		ItemList.AlloySmelterUMV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10767, "basicmachine.alloysmelter.tier.12", "Epic Alloy Integrator IV",12, "HighTech combination Smelter",
				GT_Recipe.GT_Recipe_Map.sAlloySmelterRecipes, 2, 1, 0, 0, 1, "AlloySmelter.png",
				GregTech_API.sSoundList.get(208), false, false, 0, "ALLOY_SMELTER",null).getStackForm(1L));
		
		//Amplifabricator
		ItemList.AmplifabricatorUHV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10773, "basicmachine.amplifab.tier.09", "Epic Amplicreator", 9, "Extracting UU Amplifier",
				GT_Recipe.GT_Recipe_Map.sAmplifiers, 1, 1, 1000, 0, 1, "Amplifabricator.png",
				GregTech_API.sSoundList.get(200), false, false, 0, "AMPLIFAB",new Object[]{aTextWirePump, aTextPlateMotor, "CPC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4}).getStackForm(1L));

		ItemList.AmplifabricatorUEV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10774, "basicmachine.amplifab.tier.10", "Epic Amplicreator II",10, "Extracting UU Amplifier",
				GT_Recipe.GT_Recipe_Map.sAmplifiers, 1, 1, 1000, 0, 1, "Amplifabricator.png",
				GregTech_API.sSoundList.get(200), false, false, 0, "AMPLIFAB",new Object[]{aTextWirePump, aTextPlateMotor, "CPC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4}).getStackForm(1L));

		ItemList.AmplifabricatorUIV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10775, "basicmachine.amplifab.tier.11", "Epic Amplicreator III",11, "Extracting UU Amplifier",
				GT_Recipe.GT_Recipe_Map.sAmplifiers, 1, 1, 1000, 0, 1, "Amplifabricator.png",
				GregTech_API.sSoundList.get(200), false, false, 0, "AMPLIFAB",null).getStackForm(1L));

		ItemList.AmplifabricatorUMV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10776, "basicmachine.amplifab.tier.12", "Epic Amplicreator IV",12, "Extracting UU Amplifier",
				GT_Recipe.GT_Recipe_Map.sAmplifiers, 1, 1, 1000, 0, 1, "Amplifabricator.png",
				GregTech_API.sSoundList.get(200), false, false, 0, "AMPLIFAB",null).getStackForm(1L));
		
		//AssemblingMachine
		ItemList.AssemblingMachineUHV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10783, "basicmachine.assembler.tier.09", "Epic Assembly Constructor", 9, "Avengers, Assemble!",
				GT_Recipe.GT_Recipe_Map.sAssemblerRecipes, 9, 1, 72000, 0, 1, "Assembler2.png",
				"", false, false, 0, "ASSEMBLER",new Object[]{"ACA", "VMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));

		ItemList.AssemblingMachineUEV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10784, "basicmachine.assembler.tier.10", "Epic Assembly Constructor II",10, "Avengers, Assemble!",
				GT_Recipe.GT_Recipe_Map.sAssemblerRecipes, 9, 1, 96000, 0, 1, "Assembler2.png",
				"", false, false, 0, "ASSEMBLER",new Object[]{"ACA", "VMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));

		ItemList.AssemblingMachineUIV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10785, "basicmachine.assembler.tier.11", "Epic Assembly Constructor III",11, "Avengers, Assemble!",
				GT_Recipe.GT_Recipe_Map.sAssemblerRecipes, 9, 1, 128000, 0, 1, "Assembler2.png",
				"", false, false, 0, "ASSEMBLER",null).getStackForm(1L));

		ItemList.AssemblingMachineUMV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10786, "basicmachine.assembler.tier.12", "Epic Assembly Constructor IV",12, "Avengers, Assemble!",
				GT_Recipe.GT_Recipe_Map.sAssemblerRecipes, 9, 1, 128000, 0, 1, "Assembler2.png",
				"", false, false, 0, "ASSEMBLER",null).getStackForm(1L));
		
		//Autoclave
		ItemList.AutoclaveUHV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10793, "basicmachine.autoclave.tier.09", "Epic Pressure Cooker", 9, "Crystallizing your Dusts",
				GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes, 1, 1, 8000, 0, 1, "Autoclave.png",
				"", false, false, 0, "AUTOCLAVE",new Object[]{"IGI", "IMI", "CPC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));

		ItemList.AutoclaveUEV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10794, "basicmachine.autoclave.tier.10", "Epic Pressure Cooker II",10, "Crystallizing your Dusts",
				GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes, 1, 1, 8000, 0, 1, "Autoclave.png",
				"", false, false, 0, "AUTOCLAVE",new Object[]{"IGI", "IMI", "CPC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));

		ItemList.AutoclaveUIV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10795, "basicmachine.autoclave.tier.11", "Epic Pressure Cooker III",11, "Crystallizing your Dusts",
				GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes, 1, 1, 8000, 0, 1, "Autoclave.png",
				"", false, false, 0, "AUTOCLAVE",null).getStackForm(1L));

		ItemList.AutoclaveUMV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10796, "basicmachine.autoclave.tier.12", "Epic Pressure Cooker IV",12, "Crystallizing your Dusts",
				GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes, 1, 1, 8000, 0, 1, "Autoclave.png",
				"", false, false, 0, "AUTOCLAVE",null).getStackForm(1L));
		
		
		//Bender
		ItemList.BendingMachineUHV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10803, "basicmachine.bender.tier.09", "Epic Bending Unit", 9, "Boo, he's bad! We want BENDER!!!",
				GT_Recipe.GT_Recipe_Map.sBenderRecipes, 2, 1, 0, 0, 1, "Bender.png",
				GregTech_API.sSoundList.get(203), false, false, 0, "BENDER",new Object[]{aTextPlateWrench, aTextCableHull, aTextMotorWire, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));

		ItemList.BendingMachineUEV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10804, "basicmachine.bender.tier.10", "Epic Bending Unit II",10, "Boo, he's bad! We want BENDER!!!",
				GT_Recipe.GT_Recipe_Map.sBenderRecipes, 2, 1, 0, 0, 1, "Bender.png",
				GregTech_API.sSoundList.get(203), false, false, 0, "BENDER",new Object[]{aTextPlateWrench, aTextCableHull, aTextMotorWire, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));

		ItemList.BendingMachineUIV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10805, "basicmachine.bender.tier.11", "Epic Bending Unit III",11, "Boo, he's bad! We want BENDER!!!",
				GT_Recipe.GT_Recipe_Map.sBenderRecipes, 2, 1, 0, 0, 1, "Bender.png",
				GregTech_API.sSoundList.get(203), false, false, 0, "BENDER",null).getStackForm(1L));

		ItemList.BendingMachineUMV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10806, "basicmachine.bender.tier.12", "Epic Bending Unit IV",12, "Boo, he's bad! We want BENDER!!!",
				GT_Recipe.GT_Recipe_Map.sBenderRecipes, 2, 1, 0, 0, 1, "Bender.png",
				GregTech_API.sSoundList.get(203), false, false, 0, "BENDER",null).getStackForm(1L));
		
		
		//Compressor
		ItemList.CompressorUHV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10813, "basicmachine.compressor.tier.09", "Epic Matter Constrictor", 9, "Compress-O-Matic C77",
				GT_Recipe.GT_Recipe_Map.sCompressorRecipes, 1, 1, 0, 0, 1, "Compressor.png",
				GregTech_API.sSoundList.get(203), false, false, 0, "COMPRESSOR",new Object[]{" C ", aTextPlateMotor, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));

		ItemList.CompressorUEV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10814, "basicmachine.compressor.tier.10", "Epic Matter Constrictor II",10, "Compress-O-Matic C77",
				GT_Recipe.GT_Recipe_Map.sCompressorRecipes, 1, 1, 0, 0, 1, "Compressor.png",
				GregTech_API.sSoundList.get(203), false, false, 0, "COMPRESSOR",new Object[]{" C ", aTextPlateMotor, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));

		ItemList.CompressorUIV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10815, "basicmachine.compressor.tier.11", "Epic Matter Constrictor III",11, "Compress-O-Matic C77",
				GT_Recipe.GT_Recipe_Map.sCompressorRecipes, 1, 1, 0, 0, 1, "Compressor.png",
				GregTech_API.sSoundList.get(203), false, false, 0, "COMPRESSOR",null).getStackForm(1L));

		ItemList.CompressorUMV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10816, "basicmachine.compressor.tier.12", "Epic Matter Constrictor IV",12, "Compress-O-Matic C77",
				GT_Recipe.GT_Recipe_Map.sCompressorRecipes, 1, 1, 0, 0, 1, "Compressor.png",
				GregTech_API.sSoundList.get(203), false, false, 0, "COMPRESSOR",null).getStackForm(1L));
		
		
		//CuttingMaching
		ItemList.CuttingMachineUHV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10823, "basicmachine.cutter.tier.09", "Epic Object Divider", 9, "Slice'N Dice",
				GT_Recipe.GT_Recipe_Map.sCutterRecipes, 1, 2, 1000, 0, 1, "Cutter.png",
				"", false, false, 0, "CUTTER",new Object[]{"WCG", "VMB", "CWE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS, 'B', OreDictNames.craftingDiamondBlade}).getStackForm(1L));

		ItemList.CuttingMachineUEV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10824, "basicmachine.cutter.tier.10", "Epic Object Divider II",10, "Slice'N Dice",
				GT_Recipe.GT_Recipe_Map.sCutterRecipes, 1, 2, 1000, 0, 1, "Cutter.png",
				"", false, false, 0, "CUTTER",new Object[]{"WCG", "VMB", "CWE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS, 'B', OreDictNames.craftingDiamondBlade}).getStackForm(1L));

		ItemList.CuttingMachineUIV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10825, "basicmachine.cutter.tier.11", "Epic Object Divider III",11, "Slice'N Dice",
				GT_Recipe.GT_Recipe_Map.sCutterRecipes, 1, 2, 1000, 0, 1, "Cutter.png",
				"", false, false, 0, "CUTTER",null).getStackForm(1L));

		ItemList.CuttingMachineUMV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10826, "basicmachine.cutter.tier.12", "Epic Object Divider IV",12, "Slice'N Dice",
				GT_Recipe.GT_Recipe_Map.sCutterRecipes, 1, 2, 1000, 0, 1, "Cutter.png",
				"", false, false, 0, "CUTTER",null).getStackForm(1L));
		
		
		//Distillery
		ItemList.DistilleryUHV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10833, "basicmachine.distillery.tier.09", "Epic Fraction Splitter", 9, "Extracting the most relevant Parts of Fluids",
				GT_Recipe.GT_Recipe_Map.sDistilleryRecipes, 1, 1, 1000, 0, 1, "Distillery.png",
				GregTech_API.sSoundList.get(200), false, false, 0, "DISTILLERY",new Object[]{"GBG", aTextCableHull, aTextWirePump, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'B', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_DISTILLATION, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));

		ItemList.DistilleryUEV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10834, "basicmachine.distillery.tier.10", "Epic Fraction Splitter II",10, "Extracting the most relevant Parts of Fluids",
				GT_Recipe.GT_Recipe_Map.sDistilleryRecipes, 1, 1, 1000, 0, 1, "Distillery.png",
				GregTech_API.sSoundList.get(200), false, false, 0, "DISTILLERY",new Object[]{"GBG", aTextCableHull, aTextWirePump, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'B', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_DISTILLATION, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));

		ItemList.DistilleryUIV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10835, "basicmachine.distillery.tier.11", "Epic Fraction Splitter III",11, "Extracting the most relevant Parts of Fluids",
				GT_Recipe.GT_Recipe_Map.sDistilleryRecipes, 1, 1, 1000, 0, 1, "Distillery.png",
				GregTech_API.sSoundList.get(200), false, false, 0, "DISTILLERY",null).getStackForm(1L));

		ItemList.DistilleryUMV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10836, "basicmachine.distillery.tier.12", "Epic Fraction Splitter IV",12, "Extracting the most relevant Parts of Fluids",
				GT_Recipe.GT_Recipe_Map.sDistilleryRecipes, 1, 1, 1000, 0, 1, "Distillery.png",
				GregTech_API.sSoundList.get(200), false, false, 0, "DISTILLERY",null).getStackForm(1L));
		
		
		//EFurnace
		ItemList.ElectricFurnaceUHV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10843, "basicmachine.e_furnace.tier.09", "Epic Atom Stimulator", 9, "Not like using a Commodore 64",
				GT_Recipe.GT_Recipe_Map.sFurnaceRecipes, 1, 1, 0, 0, 1, "E_Furnace.png",
				GregTech_API.sSoundList.get(207), false, false, 0, "ELECTRIC_FURNACE",new Object[]{"ECE", aTextCableHull, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING}).getStackForm(1L));

		ItemList.ElectricFurnaceUEV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10844, "basicmachine.e_furnace.tier.10", "Epic Atom Stimulator II",10, "Not like using a Commodore 64",
				GT_Recipe.GT_Recipe_Map.sFurnaceRecipes, 1, 1, 0, 0, 1, "E_Furnace.png",
				GregTech_API.sSoundList.get(207), false, false, 0, "ELECTRIC_FURNACE",new Object[]{"ECE", aTextCableHull, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING}).getStackForm(1L));

		ItemList.ElectricFurnaceUIV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10845, "basicmachine.e_furnace.tier.11", "Epic Atom Stimulator III",11, "Not like using a Commodore 64",
				GT_Recipe.GT_Recipe_Map.sFurnaceRecipes, 1, 1, 0, 0, 1, "E_Furnace.png",
				GregTech_API.sSoundList.get(207), false, false, 0, "ELECTRIC_FURNACE",null).getStackForm(1L));

		ItemList.ElectricFurnaceUMV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10846, "basicmachine.e_furnace.tier.12", "Epic Atom Stimulator IV",12, "Not like using a Commodore 64",
				GT_Recipe.GT_Recipe_Map.sFurnaceRecipes, 1, 1, 0, 0, 1, "E_Furnace.png",
				GregTech_API.sSoundList.get(207), false, false, 0, "ELECTRIC_FURNACE",null).getStackForm(1L));
		
		
		//Electrolyzer
		ItemList.ElectrolyzerUHV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10853, "basicmachine.electrolyzer.tier.09", "Epic Ionizer", 9, "Electrolyzing Molecules",
				GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes, 2, 6, 64000, 0, 1, "Electrolyzer.png",
				"", false, false, 0, "ELECTROLYZER",new Object[]{"IGI", "IMI", "CWC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I', OrePrefixes.wireGt01.get(Materials.Osmium), 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));

		ItemList.ElectrolyzerUEV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10854, "basicmachine.electrolyzer.tier.10", "Epic Ionizer II",10, "Electrolyzing Molecules",
				GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes, 2, 6, 64000, 0, 1, "Electrolyzer.png",
				"", false, false, 0, "ELECTROLYZER",new Object[]{"IGI", "IMI", "CWC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I', OrePrefixes.wireGt01.get(Materials.Osmium), 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));

		ItemList.ElectrolyzerUIV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10855, "basicmachine.electrolyzer.tier.11", "Epic Ionizer III",11, "Electrolyzing Molecules",
				GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes, 2, 6, 64000, 0, 1, "Electrolyzer.png",
				"", false, false, 0, "ELECTROLYZER",null).getStackForm(1L));

		ItemList.ElectrolyzerUMV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10856, "basicmachine.electrolyzer.tier.12", "Epic Ionizer IV",12, "Electrolyzing Molecules",
				GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes, 2, 6, 64000, 0, 1, "Electrolyzer.png",
				"", false, false, 0, "ELECTROLYZER",null).getStackForm(1L));
		
		
		//ElectromagneticSeparator
		ItemList.ElectromagneticSeparatorUHV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10863, "basicmachine.electromagneticseparator.tier.09", "Epic Magnetar Separator", 9, "Separating the magnetic Ores from the rest",
				GT_Recipe.GT_Recipe_Map.sElectroMagneticSeparatorRecipes, 1, 3, 0, 0, 1, "ElectromagneticSeparator.png",
				GregTech_API.sSoundList.get(212), false, false, 0, "ELECTROMAGNETIC_SEPARATOR",new Object[]{"VWZ", "WMS", "CWZ", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));

		ItemList.ElectromagneticSeparatorUEV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10864, "basicmachine.electromagneticseparator.tier.10", "Epic Magnetar Separator II",10, "Separating the magnetic Ores from the rest",
				GT_Recipe.GT_Recipe_Map.sElectroMagneticSeparatorRecipes, 1, 3, 0, 0, 1, "ElectromagneticSeparator.png",
				GregTech_API.sSoundList.get(212), false, false, 0, "ELECTROMAGNETIC_SEPARATOR",new Object[]{"VWZ", "WMS", "CWZ", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));

		ItemList.ElectromagneticSeparatorUIV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10865, "basicmachine.electromagneticseparator.tier.11", "Epic Magnetar Separator III",11, "Separating the magnetic Ores from the rest",
				GT_Recipe.GT_Recipe_Map.sElectroMagneticSeparatorRecipes, 1, 3, 0, 0, 1, "ElectromagneticSeparator.png",
				GregTech_API.sSoundList.get(212), false, false, 0, "ELECTROMAGNETIC_SEPARATOR",null).getStackForm(1L));

		ItemList.ElectromagneticSeparatorUMV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10866, "basicmachine.electromagneticseparator.tier.12", "Epic Magnetar Separator IV",12, "Separating the magnetic Ores from the rest",
				GT_Recipe.GT_Recipe_Map.sElectroMagneticSeparatorRecipes, 1, 3, 0, 0, 1, "ElectromagneticSeparator.png",
				GregTech_API.sSoundList.get(212), false, false, 0, "ELECTROMAGNETIC_SEPARATOR",null).getStackForm(1L));
		
		
		//Extractor
		ItemList.ExtractorUHV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10873, "basicmachine.extractor.tier.09", "Epic Extractinator", 9, "Dejuicer-Device of Doom - D123",
				GT_Recipe.GT_Recipe_Map.sExtractorRecipes, 1, 1, 0, 0, 1, "Extractor.png",
				GregTech_API.sSoundList.get(200), false, false, 0, "EXTRACTOR",new Object[]{"GCG", "EMP", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));

		ItemList.ExtractorUEV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10874, "basicmachine.extractor.tier.10", "Epic Extractinator II",10, "Dejuicer-Device of Doom - D123",
				GT_Recipe.GT_Recipe_Map.sExtractorRecipes, 1, 1, 0, 0, 1, "Extractor.png",
				GregTech_API.sSoundList.get(200), false, false, 0, "EXTRACTOR",new Object[]{"GCG", "EMP", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));

		ItemList.ExtractorUIV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10875, "basicmachine.extractor.tier.11", "Epic Extractinator III",11, "Dejuicer-Device of Doom - D123",
				GT_Recipe.GT_Recipe_Map.sExtractorRecipes, 1, 1, 0, 0, 1, "Extractor.png",
				GregTech_API.sSoundList.get(200), false, false, 0, "EXTRACTOR",null).getStackForm(1L));

		ItemList.ExtractorUMV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10876, "basicmachine.extractor.tier.12", "Epic Extractinator IV",12, "Dejuicer-Device of Doom - D123",
				GT_Recipe.GT_Recipe_Map.sExtractorRecipes, 1, 1, 0, 0, 1, "Extractor.png",
				GregTech_API.sSoundList.get(200), false, false, 0, "EXTRACTOR",null).getStackForm(1L));
		
		
		//Extruder
		ItemList.ExtruderUHV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10883, "basicmachine.extruder.tier.09", "Epic Shape Driver", 9, "Universal Machine for Metal Working",
				GT_Recipe.GT_Recipe_Map.sExtruderRecipes, 2, 1, 0, 0, 1, "Extruder.png",
				GregTech_API.sSoundList.get(208), false, false, 0, "EXTRUDER",new Object[]{"CCE", "XMP", "CCE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'X', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PIPE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE}).getStackForm(1L));

		ItemList.ExtruderUEV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10884, "basicmachine.extruder.tier.10", "Epic Shape Driver II",10, "Universal Machine for Metal Working",
				GT_Recipe.GT_Recipe_Map.sExtruderRecipes, 2, 1, 0, 0, 1, "Extruder.png",
				GregTech_API.sSoundList.get(208), false, false, 0, "EXTRUDER",new Object[]{"CCE", "XMP", "CCE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'X', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PIPE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE}).getStackForm(1L));

		ItemList.ExtruderUIV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10885, "basicmachine.extruder.tier.11", "Epic Shape Driver III",11, "Universal Machine for Metal Working",
				GT_Recipe.GT_Recipe_Map.sExtruderRecipes, 2, 1, 0, 0, 1, "Extruder.png",
				GregTech_API.sSoundList.get(208), false, false, 0, "EXTRUDER",null).getStackForm(1L));

		ItemList.ExtruderUMV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10886, "basicmachine.extruder.tier.12", "Epic Shape Driver IV",12, "Universal Machine for Metal Working",
				GT_Recipe.GT_Recipe_Map.sExtruderRecipes, 2, 1, 0, 0, 1, "Extruder.png",
				GregTech_API.sSoundList.get(208), false, false, 0, "EXTRUDER",null).getStackForm(1L));
		
		
		//FluidSolidifier
		ItemList.FluidSolidifierUHV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10893, "basicmachine.fluidsolidifier.tier.09", "Epic Fluid Petrificator", 9, "Cools Fluids down to form Solids",
				GT_Recipe.GT_Recipe_Map.sFluidSolidficationRecipes, 1, 1, 16000, 0, 1, "FluidSolidifier.png",
				"", false, false, 0, "FLUID_SOLIDIFIER",new Object[]{"PGP", aTextWireHull, "CBC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS, 'B', OreDictNames.craftingChest}).getStackForm(1L));

		ItemList.FluidSolidifierUEV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10894, "basicmachine.fluidsolidifier.tier.10", "Epic Fluid Petrificator II",10, "Cools Fluids down to form Solids",
				GT_Recipe.GT_Recipe_Map.sFluidSolidficationRecipes, 1, 1, 16000, 0, 1, "FluidSolidifier.png",
				"", false, false, 0, "FLUID_SOLIDIFIER",new Object[]{"PGP", aTextWireHull, "CBC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS, 'B', OreDictNames.craftingChest}).getStackForm(1L));

		ItemList.FluidSolidifierUIV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10895, "basicmachine.fluidsolidifier.tier.11", "Epic Fluid Petrificator III",11, "Cools Fluids down to form Solids",
				GT_Recipe.GT_Recipe_Map.sFluidSolidficationRecipes, 1, 1, 16000, 0, 1, "FluidSolidifier.png",
				"", false, false, 0, "FLUID_SOLIDIFIER",null).getStackForm(1L));

		ItemList.FluidSolidifierUMV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10896, "basicmachine.fluidsolidifier.tier.12", "Epic Fluid Petrificator IV",12, "Cools Fluids down to form Solids",
				GT_Recipe.GT_Recipe_Map.sFluidSolidficationRecipes, 1, 1, 16000, 0, 1, "FluidSolidifier.png",
				"", false, false, 0, "FLUID_SOLIDIFIER",null).getStackForm(1L));
		
		
		//FormingPress
		ItemList.FormingPressUHV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10903, "basicmachine.press.tier.09", "Epic Surface Shifter", 9, "Imprinting Images into things",
				GT_Recipe.GT_Recipe_Map.sPressRecipes, 2, 1, 0, 0, 1, "Press.png",
				GregTech_API.sSoundList.get(203), false, false, 0, "PRESS",new Object[]{aTextWirePump, aTextCableHull, aTextWirePump, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));

		ItemList.FormingPressUEV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10904, "basicmachine.press.tier.10", "Epic Surface Shifter II",10, "Imprinting Images into things",
				GT_Recipe.GT_Recipe_Map.sPressRecipes, 2, 1, 0, 0, 1, "Press.png",
				GregTech_API.sSoundList.get(203), false, false, 0, "PRESS",new Object[]{aTextWirePump, aTextCableHull, aTextWirePump, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));

		ItemList.FormingPressUIV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10905, "basicmachine.press.tier.11", "Epic Surface Shifter III",11, "Imprinting Images into things",
				GT_Recipe.GT_Recipe_Map.sPressRecipes, 2, 1, 0, 0, 1, "Press.png",
				GregTech_API.sSoundList.get(203), false, false, 0, "PRESS",null).getStackForm(1L));

		ItemList.FormingPressUMV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10906, "basicmachine.press.tier.12", "Epic Surface Shifter IV",12, "Imprinting Images into things",
				GT_Recipe.GT_Recipe_Map.sPressRecipes, 2, 1, 0, 0, 1, "Press.png",
				GregTech_API.sSoundList.get(203), false, false, 0, "PRESS",null).getStackForm(1L));
		
		
		//ForgeHammer
		ItemList.ForgeHammerUHV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10913, "basicmachine.hammer.tier.09", "Epic Impact Modulator", 9, "Stop, Hammertime!",
				GT_Recipe.GT_Recipe_Map.sHammerRecipes, 1, 1, 0, 6, 3, "Hammer.png",
				GregTech_API.sSoundList.get(1), false, false, 0, "HAMMER",new Object[]{aTextWirePump, aTextCableHull, "WAW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'A', OreDictNames.craftingAnvil}).getStackForm(1L));

		ItemList.ForgeHammerUEV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10914, "basicmachine.hammer.tier.10", "Epic Impact Modulator II",10, "Stop, Hammertime!",
				GT_Recipe.GT_Recipe_Map.sHammerRecipes, 1, 1, 0, 6, 3, "Hammer.png",
				GregTech_API.sSoundList.get(1), false, false, 0, "HAMMER",new Object[]{aTextWirePump, aTextCableHull, "WAW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'A', OreDictNames.craftingAnvil}).getStackForm(1L));

		ItemList.ForgeHammerUIV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10915, "basicmachine.hammer.tier.11", "Epic Impact Modulator III",11, "Stop, Hammertime!",
				GT_Recipe.GT_Recipe_Map.sHammerRecipes, 1, 1, 0, 6, 3, "Hammer.png",
				GregTech_API.sSoundList.get(1), false, false, 0, "HAMMER",null).getStackForm(1L));

		ItemList.ForgeHammerUMV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10916, "basicmachine.hammer.tier.12", "Epic Impact Modulator IV",12, "Stop, Hammertime!",
				GT_Recipe.GT_Recipe_Map.sHammerRecipes, 1, 1, 0, 6, 3, "Hammer.png",
				GregTech_API.sSoundList.get(1), false, false, 0, "HAMMER",null).getStackForm(1L));
		
		
		//Lathe
		ItemList.LatheUHV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10923, "basicmachine.lathe.tier.09", "Epic Turn-O-Matic", 9, "Produces Rods more efficiently",
				GT_Recipe.GT_Recipe_Map.sLatheRecipes, 1, 2, 0, 0, 1, "Lathe.png", "", false, false, 0, "LATHE",new Object[]{aTextWireCoil, "EMD", "CWP", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'D', OreDictNames.craftingIndustrialDiamond}).getStackForm(1L));

		ItemList.LatheUEV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10924, "basicmachine.lathe.tier.10", "Epic Turn-O-Matic II",10, "Produces Rods more efficiently",
				GT_Recipe.GT_Recipe_Map.sLatheRecipes, 1, 2, 0, 0, 1, "Lathe.png", "", false, false, 0, "LATHE",new Object[]{aTextWireCoil, "EMD", "CWP", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'D', OreDictNames.craftingIndustrialDiamond}).getStackForm(1L));

		ItemList.LatheUIV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10925, "basicmachine.lathe.tier.11", "Epic Turn-O-Matic III",11, "Produces Rods more efficiently",
				GT_Recipe.GT_Recipe_Map.sLatheRecipes, 1, 2, 0, 0, 1, "Lathe.png", "", false, false, 0, "LATHE",null).getStackForm(1L));

		ItemList.LatheUMV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10926, "basicmachine.lathe.tier.12", "Epic Turn-O-Matic IV",12, "Produces Rods more efficiently",
				GT_Recipe.GT_Recipe_Map.sLatheRecipes, 1, 2, 0, 0, 1, "Lathe.png", "", false, false, 0, "LATHE",null).getStackForm(1L));
		
		
		//PrecisionLaserEngraver
		ItemList.PrecisionLaserEngraverUHV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10933, "basicmachine.laserengraver.tier.09", "Epic Exact Photon Cannon", 9, "With the Power of 2.42 GW",
				GT_Recipe.GT_Recipe_Map.sLaserEngraverRecipes, 2, 1, 0, 0, 1, "LaserEngraver.png",
				GregTech_API.sSoundList.get(212), false, false, 0, "LASER_ENGRAVER",new Object[]{"PEP", aTextCableHull, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));

		ItemList.PrecisionLaserEngraverUEV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10934, "basicmachine.laserengraver.tier.10", "Epic Exact Photon Cannon II",10, "With the Power of 4.84 GW",
				GT_Recipe.GT_Recipe_Map.sLaserEngraverRecipes, 2, 1, 0, 0, 1, "LaserEngraver.png",
				GregTech_API.sSoundList.get(212), false, false, 0, "LASER_ENGRAVER",new Object[]{"PEP", aTextCableHull, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));

		ItemList.PrecisionLaserEngraverUIV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10935, "basicmachine.laserengraver.tier.11", "Epic Exact Photon Cannon III",11, "With the Power of 9.68 GW",
				GT_Recipe.GT_Recipe_Map.sLaserEngraverRecipes, 2, 1, 0, 0, 1, "LaserEngraver.png",
				GregTech_API.sSoundList.get(212), false, false, 0, "LASER_ENGRAVER",null).getStackForm(1L));

		ItemList.PrecisionLaserEngraverUMV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10936, "basicmachine.laserengraver.tier.12", "Epic Exact Photon Cannon IV",12, "With the Power of 19.36 GW",
				GT_Recipe.GT_Recipe_Map.sLaserEngraverRecipes, 2, 1, 0, 0, 1, "LaserEngraver.png",
				GregTech_API.sSoundList.get(212), false, false, 0, "LASER_ENGRAVER",null).getStackForm(1L));
		
		
		//Macerator
		ItemList.MaceratorUHV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10943, "basicmachine.macerator.tier.09", "Epic Shape Eliminator", 9, "Schreddering your Ores with Byproducts",
				GT_Recipe.GT_Recipe_Map.sMaceratorRecipes, 1, 4, 0, 0, 1, "Macerator4.png",
				GregTech_API.sSoundList.get(201), false, false, 1, "PULVERIZER",new Object[]{"PEG", "WWM", "CCW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OreDictNames.craftingGrinder}).getStackForm(1L));

		ItemList.MaceratorUEV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10944, "basicmachine.macerator.tier.10", "Epic Shape Eliminator II",10, "Schreddering your Ores with Byproducts",
				GT_Recipe.GT_Recipe_Map.sMaceratorRecipes, 1, 4, 0, 0, 1, "Macerator4.png",
				GregTech_API.sSoundList.get(201), false, false, 1, "PULVERIZER",new Object[]{"PEG", "WWM", "CCW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OreDictNames.craftingGrinder}).getStackForm(1L));

		ItemList.MaceratorUIV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10945, "basicmachine.macerator.tier.11", "Epic Shape Eliminator III",11, "Schreddering your Ores with Byproducts",
				GT_Recipe.GT_Recipe_Map.sMaceratorRecipes, 1, 4, 0, 0, 1, "Macerator4.png",
				GregTech_API.sSoundList.get(201), false, false, 1, "PULVERIZER",null).getStackForm(1L));

		ItemList.MaceratorUMV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10946, "basicmachine.macerator.tier.12", "Epic Shape Eliminator IV",12, "Schreddering your Ores with Byproducts",
				GT_Recipe.GT_Recipe_Map.sMaceratorRecipes, 1, 4, 0, 0, 1, "Macerator4.png",
				GregTech_API.sSoundList.get(201), false, false, 1, "PULVERIZER",null).getStackForm(1L));
		
		
		//MassFabricator
		ItemList.MassFabricatorUHV.set(new GT_MetaTileEntity_Massfabricator(
				10953, "basicmachine.massfab.tier.09", "Epic Existence Initiator", 9).getStackForm(1L));
		ItemList.MassFabricatorUEV.set(new GT_MetaTileEntity_Massfabricator(
				10954, "basicmachine.massfab.tier.10", "Epic Existence Initiator II",10).getStackForm(1L));
		ItemList.MassFabricatorUIV.set(new GT_MetaTileEntity_Massfabricator(
				10955, "basicmachine.massfab.tier.11", "Epic Existence Initiator III",11).getStackForm(1L));
		ItemList.MassFabricatorUMV.set(new GT_MetaTileEntity_Massfabricator(
				10956, "basicmachine.massfab.tier.12", "Epic Existence Initiator IV",12).getStackForm(1L));
				
		GT_ModHandler.addCraftingRecipe(ItemList.MassFabricatorUHV.get(1L, new Object[0]), bitsd, new Object[]{"CFC", aTextWireHull, "CFC", 'M', ItemList.Hull_MAX, 'F', ItemList.Field_Generator_UHV, 'C', OrePrefixes.circuit.get(Materials.Bio), 'W', OrePrefixes.wireGt04.get(Materials.Superconductor)});
		GT_ModHandler.addCraftingRecipe(ItemList.MassFabricatorUEV.get(1L, new Object[0]), bitsd, new Object[]{"CFC", aTextWireHull, "CFC", 'M', ItemList.Hull_UEV, 'F', ItemList.Field_Generator_UEV, 'C', OrePrefixes.circuit.get(Materials.Bio), 'W', OrePrefixes.wireGt08.get(Materials.Superconductor)});
		
		
		
		//Microwave
		ItemList.MicrowaveUHV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10963, "basicmachine.microwave.tier.09", "Epic UFO Engine", 9, "Did you really remember the instruction Manual?",
				GT_Recipe.GT_Recipe_Map.sMicrowaveRecipes, 1, 1, 0, 0, 1, "E_Furnace.png",
				GregTech_API.sSoundList.get(207), false, false, 0, "MICROWAVE", new Object[]{"LWC", "LMR", "LEC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'L', OrePrefixes.plate.get(Materials.Lead)}).getStackForm(1L));

		ItemList.MicrowaveUEV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10964, "basicmachine.microwave.tier.10", "Epic UFO Engine II",10, "Did you really remember the instruction Manual?",
				GT_Recipe.GT_Recipe_Map.sMicrowaveRecipes, 1, 1, 0, 0, 1, "E_Furnace.png",
				GregTech_API.sSoundList.get(207), false, false, 0, "MICROWAVE", new Object[]{"LWC", "LMR", "LEC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'L', OrePrefixes.plate.get(Materials.Lead)}).getStackForm(1L));

		ItemList.MicrowaveUIV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10965, "basicmachine.microwave.tier.11", "Epic UFO Engine III",11, "Did you really remember the instruction Manual?",
				GT_Recipe.GT_Recipe_Map.sMicrowaveRecipes, 1, 1, 0, 0, 1, "E_Furnace.png",
				GregTech_API.sSoundList.get(207), false, false, 0, "MICROWAVE", null).getStackForm(1L));

		ItemList.MicrowaveUMV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10966, "basicmachine.microwave.tier.12", "Epic UFO Engine IV",12, "Did you really remember the instruction Manual?",
				GT_Recipe.GT_Recipe_Map.sMicrowaveRecipes, 1, 1, 0, 0, 1, "E_Furnace.png",
				GregTech_API.sSoundList.get(207), false, false, 0, "MICROWAVE", null).getStackForm(1L));
		
		
		//OreWasher
		ItemList.OreWashingPlantUHV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10973, "basicmachine.orewasher.tier.09", "Epic Ore Washing Machine", 9, "Getting more Byproducts from your Ores",
				GT_Recipe.GT_Recipe_Map.sOreWasherRecipes, 1, 3, 16000, 0, 1, "OreWasher.png",
				"", false, false, 0, "ORE_WASHER",new Object[]{"RGR", "CEC", aTextWireHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));

		ItemList.OreWashingPlantUEV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10974, "basicmachine.orewasher.tier.10", "Epic Ore Washing Machine II",10, "Getting more Byproducts from your Ores",
				GT_Recipe.GT_Recipe_Map.sOreWasherRecipes, 1, 3, 16000, 0, 1, "OreWasher.png",
				"", false, false, 0, "ORE_WASHER",new Object[]{"RGR", "CEC", aTextWireHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));

		ItemList.OreWashingPlantUIV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10975, "basicmachine.orewasher.tier.11", "Epic Ore Washing Machine III",11, "Getting more Byproducts from your Ores",
				GT_Recipe.GT_Recipe_Map.sOreWasherRecipes, 1, 3, 16000, 0, 1, "OreWasher.png",
				"", false, false, 0, "ORE_WASHER",null).getStackForm(1L));

		ItemList.OreWashingPlantUMV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10976, "basicmachine.orewasher.tier.12", "Epic Ore Washing Machine IV",12, "Getting more Byproducts from your Ores",
				GT_Recipe.GT_Recipe_Map.sOreWasherRecipes, 1, 3, 16000, 0, 1, "OreWasher.png",
				"", false, false, 0, "ORE_WASHER",null).getStackForm(1L));
		
		
		//Polarizer
		ItemList.PolarizerUHV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10983, "basicmachine.polarizer.tier.09", "Epic Magnetism Inducer", 9, "Bipolarising your Magnets",
				GT_Recipe.GT_Recipe_Map.sPolarizerRecipes, 1, 1, 0, 0, 1, "Polarizer.png",
				GregTech_API.sSoundList.get(212), false, false, 0, "POLARIZER",new Object[]{"ZSZ", aTextWireHull, "ZSZ", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));

		ItemList.PolarizerUEV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10984, "basicmachine.polarizer.tier.10", "Epic Magnetism Inducer II",10, "Bipolarising your Magnets",
				GT_Recipe.GT_Recipe_Map.sPolarizerRecipes, 1, 1, 0, 0, 1, "Polarizer.png",
				GregTech_API.sSoundList.get(212), false, false, 0, "POLARIZER",new Object[]{"ZSZ", aTextWireHull, "ZSZ", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));

		ItemList.PolarizerUIV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10985, "basicmachine.polarizer.tier.11", "Epic Magnetism Inducer III",11, "Bipolarising your Magnets",
				GT_Recipe.GT_Recipe_Map.sPolarizerRecipes, 1, 1, 0, 0, 1, "Polarizer.png",
				GregTech_API.sSoundList.get(212), false, false, 0, "POLARIZER",null).getStackForm(1L));

		ItemList.PolarizerUMV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10986, "basicmachine.polarizer.tier.12", "Epic Magnetism Inducer IV",12, "Bipolarising your Magnets",
				GT_Recipe.GT_Recipe_Map.sPolarizerRecipes, 1, 1, 0, 0, 1, "Polarizer.png",
				GregTech_API.sSoundList.get(212), false, false, 0, "POLARIZER",null).getStackForm(1L));
		
		
		//Recycler
		ItemList.RecyclerUHV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10993, "basicmachine.recycler.tier.09", "Epic Scrap-O-Matic", 9, "Compress, burn, obliterate and filter EVERYTHING",
				GT_Recipe.GT_Recipe_Map.sRecyclerRecipes, 1, 1, 0, 0, 1, "Recycler.png",
				GregTech_API.sSoundList.get(204), false, false, 0, "RECYCLER",new Object[]{"GCG", aTextPlateMotor, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OrePrefixes.dust.get(Materials.NetherStar)}).getStackForm(1L));

		ItemList.RecyclerUEV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10994, "basicmachine.recycler.tier.10", "Epic Scrap-O-Matic II",10, "Compress, burn, obliterate and filter EVERYTHING",
				GT_Recipe.GT_Recipe_Map.sRecyclerRecipes, 1, 1, 0, 0, 1, "Recycler.png",
				GregTech_API.sSoundList.get(204), false, false, 0, "RECYCLER",new Object[]{"GCG", aTextPlateMotor, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OrePrefixes.dust.get(Materials.NetherStar)}).getStackForm(1L));

		ItemList.RecyclerUIV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10995, "basicmachine.recycler.tier.11", "Epic Scrap-O-Matic III",11, "Compress, burn, obliterate and filter EVERYTHING",
				GT_Recipe.GT_Recipe_Map.sRecyclerRecipes, 1, 1, 0, 0, 1, "Recycler.png",
				GregTech_API.sSoundList.get(204), false, false, 0, "RECYCLER",null).getStackForm(1L));

		ItemList.RecyclerUMV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				10996, "basicmachine.recycler.tier.12", "Epic Scrap-O-Matic IV",12, "Compress, burn, obliterate and filter EVERYTHING",
				GT_Recipe.GT_Recipe_Map.sRecyclerRecipes, 1, 1, 0, 0, 1, "Recycler.png",
				GregTech_API.sSoundList.get(204), false, false, 0, "RECYCLER",null).getStackForm(1L));
		
		
		//Replicator
		ItemList.ReplicatorUHV.set(new GT_MetaTileEntity_Replicator(
				11003, "basicmachine.replicator.tier.09", "Epic Elemental Composer", 9).getStackForm(1L));
		ItemList.ReplicatorUEV.set(new GT_MetaTileEntity_Replicator(
				11004, "basicmachine.replicator.tier.10", "Epic Elemental Composer II",10).getStackForm(1L));
		ItemList.ReplicatorUIV.set(new GT_MetaTileEntity_Replicator(
				11005, "basicmachine.replicator.tier.11", "Epic Elemental Composer III",11).getStackForm(1L));
		ItemList.ReplicatorUMV.set(new GT_MetaTileEntity_Replicator(
				11006, "basicmachine.replicator.tier.12", "Epic Elemental Composer IV",12).getStackForm(1L));
		
		GT_ModHandler.addCraftingRecipe(ItemList.ReplicatorUHV.get(1L, new Object[0]), bitsd, new Object[]{"EFE", aTextCableHull, aTextMotorWire, 'M', ItemList.Hull_MAX, 'F', ItemList.Field_Generator_UHV, 'E', ItemList.Emitter_UHV, 'C', OrePrefixes.circuit.get(Materials.Bio), 'W', OrePrefixes.wireGt04.get(Materials.Superconductor)});
		GT_ModHandler.addCraftingRecipe(ItemList.ReplicatorUEV.get(1L, new Object[0]), bitsd, new Object[]{"EFE", aTextCableHull, aTextMotorWire, 'M', ItemList.Hull_UEV, 'F', ItemList.Field_Generator_UEV, 'E', ItemList.Emitter_UEV, 'C', OrePrefixes.circuit.get(Materials.Bio), 'W', OrePrefixes.wireGt08.get(Materials.Superconductor)});
		
		
		
		//Scanner
		ItemList.ScannerUHV.set(new GT_MetaTileEntity_Scanner(
				11013, "basicmachine.scanner.tier.09", "Epic Electron Microscope", 9).getStackForm(1L));
		ItemList.ScannerUEV.set(new GT_MetaTileEntity_Scanner(
				11014, "basicmachine.scanner.tier.10", "Epic Electron Microscope II",10).getStackForm(1L));
		ItemList.ScannerUIV.set(new GT_MetaTileEntity_Scanner(
				11015, "basicmachine.scanner.tier.11", "Epic Electron Microscope III",11).getStackForm(1L));
		ItemList.ScannerUMV.set(new GT_MetaTileEntity_Scanner(
				11016, "basicmachine.scanner.tier.12", "Epic Electron Microscope IV",12).getStackForm(1L));
		
		GT_ModHandler.addCraftingRecipe(ItemList.ScannerUHV.get(1L, new Object[0]), bitsd, new Object[]{"CTC", aTextWireHull, "CRC", 'M', ItemList.Hull_MAX, 'T', ItemList.Emitter_UHV, 'R', ItemList.Sensor_UHV, 'C', OrePrefixes.circuit.get(Materials.Bio), 'W', OrePrefixes.wireGt04.get(Materials.Europium)});
		GT_ModHandler.addCraftingRecipe(ItemList.ScannerUEV.get(1L, new Object[0]), bitsd, new Object[]{"CTC", aTextWireHull, "CRC", 'M', ItemList.Hull_UEV, 'T', ItemList.Emitter_UEV, 'R', ItemList.Sensor_UEV, 'C', OrePrefixes.circuit.get(Materials.Bio), 'W', OrePrefixes.wireGt04.get(Materials.Diamericiumtitanium)});

		
		//Sifter
		ItemList.SiftingMachineUHV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11023, "basicmachine.sifter.tier.09", "Epic Pulsation Filter", 9, "Stay calm and keep sifting",
				GT_Recipe.GT_Recipe_Map.sSifterRecipes, 1, 9, 0, 2, 5, "Sifter.png", "", false, false, 0, "SIFTER",new Object[]{"WFW", aTextPlateMotor, "CFC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'F', OreDictNames.craftingFilter, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));

		ItemList.SiftingMachineUEV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11024, "basicmachine.sifter.tier.10", "Epic Pulsation Filter II",10, "Stay calm and keep sifting",
				GT_Recipe.GT_Recipe_Map.sSifterRecipes, 1, 9, 0, 2, 5, "Sifter.png", "", false, false, 0, "SIFTER",new Object[]{"WFW", aTextPlateMotor, "CFC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'F', OreDictNames.craftingFilter, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));

		ItemList.SiftingMachineUIV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11025, "basicmachine.sifter.tier.11", "Epic Pulsation Filter III",11, "Stay calm and keep sifting",
				GT_Recipe.GT_Recipe_Map.sSifterRecipes, 1, 9, 0, 2, 5, "Sifter.png", "", false, false, 0, "SIFTER",null).getStackForm(1L));

		ItemList.SiftingMachineUMV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11026, "basicmachine.sifter.tier.12", "Epic Pulsation Filter IV",12, "Stay calm and keep sifting",
				GT_Recipe.GT_Recipe_Map.sSifterRecipes, 1, 9, 0, 2, 5, "Sifter.png", "", false, false, 0, "SIFTER",null).getStackForm(1L));
		
		
		//SlicingMachine
		ItemList.SlicingMachineUHV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11033, "basicmachine.slicer.tier.09", "Epic Quantum Slicer", 9, "Be careful when slicing bread!",
				GT_Recipe.GT_Recipe_Map.sSlicerRecipes, 2, 1, 0, 0, 1, "Slicer.png", "", false, false, 0, "SLICER",new Object[]{aTextWireCoil, "PMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));

		ItemList.SlicingMachineUEV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11034, "basicmachine.slicer.tier.10", "Epic Quantum Slicer II",10, "Be careful when slicing bread!",
				GT_Recipe.GT_Recipe_Map.sSlicerRecipes, 2, 1, 0, 0, 1, "Slicer.png", "", false, false, 0, "SLICER",new Object[]{aTextWireCoil, "PMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));

		ItemList.SlicingMachineUIV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11035, "basicmachine.slicer.tier.11", "Epic Quantum Slicer III",11, "Be careful when slicing bread!",
				GT_Recipe.GT_Recipe_Map.sSlicerRecipes, 2, 1, 0, 0, 1, "Slicer.png", "", false, false, 0, "SLICER",null).getStackForm(1L));

		ItemList.SlicingMachineUMV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11036, "basicmachine.slicer.tier.12", "Epic Quantum Slicer IV",12, "Be careful when slicing bread!",
				GT_Recipe.GT_Recipe_Map.sSlicerRecipes, 2, 1, 0, 0, 1, "Slicer.png", "", false, false, 0, "SLICER",null).getStackForm(1L));
		
		
		//ThermalCentrifuge
		ItemList.ThermalCentrifugeUHV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11043, "basicmachine.thermalcentrifuge.tier.09", "Epic Fire Cyclone", 9, "Separating Ores more precisely",
				GT_Recipe.GT_Recipe_Map.sThermalCentrifugeRecipes, 1, 3, 0, 0, 1, "ThermalCentrifuge.png",
				"", false, false, 0, "THERMAL_CENTRIFUGE",new Object[]{"CEC", "OMO", "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE}).getStackForm(1L));

		ItemList.ThermalCentrifugeUEV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11044, "basicmachine.thermalcentrifuge.tier.10", "Epic Fire Cyclone II",10, "Separating Ores more precisely",
				GT_Recipe.GT_Recipe_Map.sThermalCentrifugeRecipes, 1, 3, 0, 0, 1, "ThermalCentrifuge.png",
				"", false, false, 0, "THERMAL_CENTRIFUGE",new Object[]{"CEC", "OMO", "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE}).getStackForm(1L));

		ItemList.ThermalCentrifugeUIV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11045, "basicmachine.thermalcentrifuge.tier.11", "Epic Fire Cyclone III",11, "Separating Ores more precisely",
				GT_Recipe.GT_Recipe_Map.sThermalCentrifugeRecipes, 1, 3, 0, 0, 1, "ThermalCentrifuge.png",
				"", false, false, 0, "THERMAL_CENTRIFUGE",null).getStackForm(1L));

		ItemList.ThermalCentrifugeUMV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11046, "basicmachine.thermalcentrifuge.tier.12", "Epic Fire Cyclone IV",12, "Separating Ores more precisely",
				GT_Recipe.GT_Recipe_Map.sThermalCentrifugeRecipes, 1, 3, 0, 0, 1, "ThermalCentrifuge.png",
				"", false, false, 0, "THERMAL_CENTRIFUGE",null).getStackForm(1L));
		
		
		//Wiremill
		ItemList.WiremillUHV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11053, "basicmachine.wiremill.tier.09", "Epic Wire Transfigurator", 9, "Produces Wires more efficiently",
				GT_Recipe.GT_Recipe_Map.sWiremillRecipes, 1, 1, 0, 0, 1, "Wiremill.png",
				GregTech_API.sSoundList.get(204), false, false, 0, "WIREMILL",new Object[]{aTextMotorWire, aTextCableHull, aTextMotorWire, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));

		ItemList.WiremillUEV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11054, "basicmachine.wiremill.tier.10", "Epic Wire Transfigurator II",10, "Produces Wires more efficiently",
				GT_Recipe.GT_Recipe_Map.sWiremillRecipes, 1, 1, 0, 0, 1, "Wiremill.png",
				GregTech_API.sSoundList.get(204), false, false, 0, "WIREMILL",new Object[]{aTextMotorWire, aTextCableHull, aTextMotorWire, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));

		ItemList.WiremillUIV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11055, "basicmachine.wiremill.tier.11", "Epic Wire Transfigurator III",11, "Produces Wires more efficiently",
				GT_Recipe.GT_Recipe_Map.sWiremillRecipes, 1, 1, 0, 0, 1, "Wiremill.png",
				GregTech_API.sSoundList.get(204), false, false, 0, "WIREMILL",null).getStackForm(1L));

		ItemList.WiremillUMV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11056, "basicmachine.wiremill.tier.12", "Epic Wire Transfigurator IV",12, "Produces Wires more efficiently",
				GT_Recipe.GT_Recipe_Map.sWiremillRecipes, 1, 1, 0, 0, 1, "Wiremill.png",
				GregTech_API.sSoundList.get(204), false, false, 0, "WIREMILL",null).getStackForm(1L));
		
		
		//ArcFurnace
		ItemList.ArcFurnaceUHV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11073, "basicmachine.arcfurnace.tier.09", "Epic Short Circuit Heater", 9, "",
				GT_Recipe.GT_Recipe_Map.sArcFurnaceRecipes, 1, 1, 128000, 0, 1, "ArcFurnace.png",
				GregTech_API.sSoundList.get(202), false, false, 0, "ARC_FURNACE",new Object[]{"WGW", aTextCableHull, aTextPlate, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'G', OrePrefixes.cell.get(Materials.Graphite)}).getStackForm(1L));

		ItemList.ArcFurnaceUEV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11074, "basicmachine.arcfurnace.tier.10", "Epic Short Circuit Heater II",10, "",
				GT_Recipe.GT_Recipe_Map.sArcFurnaceRecipes, 1, 1, 128000, 0, 1, "ArcFurnace.png",
				GregTech_API.sSoundList.get(202), false, false, 0, "ARC_FURNACE",new Object[]{"WGW", aTextCableHull, aTextPlate, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'G', OrePrefixes.cell.get(Materials.Graphite)}).getStackForm(1L));

		ItemList.ArcFurnaceUIV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11075, "basicmachine.arcfurnace.tier.11", "Epic Short Circuit Heater III",11, "",
				GT_Recipe.GT_Recipe_Map.sArcFurnaceRecipes, 1, 1, 128000, 0, 1, "ArcFurnace.png",
				GregTech_API.sSoundList.get(202), false, false, 0, "ARC_FURNACE",null).getStackForm(1L));

		ItemList.ArcFurnaceUMV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11076, "basicmachine.arcfurnace.tier.12", "Epic Short Circuit Heater IV",12, "",
				GT_Recipe.GT_Recipe_Map.sArcFurnaceRecipes, 1, 1, 128000, 0, 1, "ArcFurnace.png",
				GregTech_API.sSoundList.get(202), false, false, 0, "ARC_FURNACE",null).getStackForm(1L));
		
		
		//Centrifuge
		ItemList.CentrifugeUHV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11083, "basicmachine.centrifuge.tier.09", "Epic Molecular Tornado", 9, "Separating Molecules",
				GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes, 2, 6, 128000, 0, 1, "Centrifuge.png",
				"", false, false, 0, "CENTRIFUGE",new Object[]{"CEC", aTextWireHull, "CEC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));

		ItemList.CentrifugeUEV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11084, "basicmachine.centrifuge.tier.10", "Epic Molecular Tornado II",10, "Separating Molecules",
				GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes, 2, 6, 256000, 0, 1, "Centrifuge.png",
				"", false, false, 0, "CENTRIFUGE",new Object[]{"CEC", aTextWireHull, "CEC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE}).getStackForm(1L));

		ItemList.CentrifugeUIV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11085, "basicmachine.centrifuge.tier.11", "Epic Molecular Tornado III",11, "Separating Molecules",
				GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes, 2, 6, 512000, 0, 1, "Centrifuge.png",
				"", false, false, 0, "CENTRIFUGE",null).getStackForm(1L));

		ItemList.CentrifugeUMV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11086, "basicmachine.centrifuge.tier.12", "Epic Molecular Tornado IV",12, "Separating Molecules",
				GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes, 2, 6, 1024000, 0, 1, "Centrifuge.png",
				"", false, false, 0, "CENTRIFUGE",null).getStackForm(1L));
		
		
		//PlasmaArcFurnace
		ItemList.PlasmaArcFurnaceUHV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11093, "basicmachine.plasmaarcfurnace.tier.09", "Epic Plasma Discharge Heater", 9, "",
				GT_Recipe.GT_Recipe_Map.sPlasmaArcFurnaceRecipes, 1, 4, 128000, 0, 1, "PlasmaArcFurnace.png",
				GregTech_API.sSoundList.get(202), false, false, 0, "PLASMA_ARC_FURNACE",new Object[]{"WGW", aTextCableHull, "TPT", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'T', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'G', OrePrefixes.cell.get(Materials.Graphite)}).getStackForm(1L));


		ItemList.PlasmaArcFurnaceUEV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11094, "basicmachine.plasmaarcfurnace.tier.10", "Epic Plasma Discharge Heater II",10, "",
				GT_Recipe.GT_Recipe_Map.sPlasmaArcFurnaceRecipes, 1, 4, 128000, 0, 1, "PlasmaArcFurnace.png",
				GregTech_API.sSoundList.get(202), false, false, 0, "PLASMA_ARC_FURNACE",new Object[]{"WGW", aTextCableHull, "TPT", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'T', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'G', OrePrefixes.cell.get(Materials.Graphite)}).getStackForm(1L));


		ItemList.PlasmaArcFurnaceUIV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11095, "basicmachine.plasmaarcfurnace.tier.11", "Epic Plasma Discharge Heater III",11, "",
				GT_Recipe.GT_Recipe_Map.sPlasmaArcFurnaceRecipes, 1, 4, 128000, 0, 1, "PlasmaArcFurnace.png",
				GregTech_API.sSoundList.get(202), false, false, 0, "PLASMA_ARC_FURNACE",null).getStackForm(1L));


		ItemList.PlasmaArcFurnaceUMV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11096, "basicmachine.plasmaarcfurnace.tier.12", "Epic Plasma Discharge Heater IV",12, "",
				GT_Recipe.GT_Recipe_Map.sPlasmaArcFurnaceRecipes, 1, 4, 128000, 0, 1, "PlasmaArcFurnace.png",
				GregTech_API.sSoundList.get(202), false, false, 0, "PLASMA_ARC_FURNACE",null).getStackForm(1L));
		
		
		//Brewer
		ItemList.BreweryUHV.set(new GT_MetaTileEntity_PotionBrewer(
				11123,"basicmachine.brewery.tier.09", "Epic Brew Rusher", 9).getStackForm(1L));
		ItemList.BreweryUEV.set(new GT_MetaTileEntity_PotionBrewer(
				11124,"basicmachine.brewery.tier.10", "Epic Brew Rusher II",10).getStackForm(1L));
		ItemList.BreweryUIV.set(new GT_MetaTileEntity_PotionBrewer(
				11125,"basicmachine.brewery.tier.11", "Epic Brew Rusher III",11).getStackForm(1L));
		ItemList.BreweryUMV.set(new GT_MetaTileEntity_PotionBrewer(
				11126,"basicmachine.brewery.tier.12", "Epic Brew Rusher IV",12).getStackForm(1L));
		ItemList.BreweryUXV.set(new GT_MetaTileEntity_PotionBrewer(
				11127,"basicmachine.brewery.tier.13", "Epic Brew Rusher V",13).getStackForm(1L));
		ItemList.BreweryOPV.set(new GT_MetaTileEntity_PotionBrewer(
				11128,"basicmachine.brewery.tier.14", "Epic Brew Rusher VI",14).getStackForm(1L));
		
		GT_ModHandler.addCraftingRecipe(ItemList.BreweryUHV.get(1L, new Object[0]), bitsd, new Object[]{"GPG", aTextWireHull, "CBC", 'M', ItemList.Hull_MAX, 'P', ItemList.Electric_Pump_UHV, 'B', OrePrefixes.spring.get(Materials.Europium), 'C', OrePrefixes.circuit.get(Materials.Infinite), 'W', OrePrefixes.wireGt04.get(Materials.Europium), 'G', Ic2Items.reinforcedGlass});
		GT_ModHandler.addCraftingRecipe(ItemList.BreweryUEV.get(1L, new Object[0]), bitsd, new Object[]{"GPG", aTextWireHull, "CBC", 'M', ItemList.Hull_UEV, 'P', ItemList.Electric_Pump_UEV, 'B', OrePrefixes.spring.get(Materials.Tritanium), 'C', OrePrefixes.circuit.get(Materials.Bio), 'W', OrePrefixes.wireGt04.get(Materials.Diamericiumtitanium), 'G', Ic2Items.reinforcedGlass});
        
		
		//CanningMachine
		ItemList.CanningMachineUHV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11133, "basicmachine.canner.tier.09", "Epic Can Operator", 9,
				"Unmobile Food Canning Machine GTA4", GT_Recipe.GT_Recipe_Map.sCannerRecipes,
				2, 2, 0, 0, 1, "Canner.png", GregTech_API.sSoundList.get(200),
				false, false, 0, "CANNER", new Object[]{aTextWirePump, aTextCableHull, "GGG", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));

		ItemList.CanningMachineUEV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11134, "basicmachine.canner.tier.10", "Epic Can Operator II",10,
				"Unmobile Food Canning Machine GTA4", GT_Recipe.GT_Recipe_Map.sCannerRecipes,
				2, 2, 0, 0, 1, "Canner.png", GregTech_API.sSoundList.get(200),
				false, false, 0, "CANNER", new Object[]{aTextWirePump, aTextCableHull, "GGG", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));

		ItemList.CanningMachineUIV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11135, "basicmachine.canner.tier.11", "Epic Can Operator III",11,
				"Unmobile Food Canning Machine GTA4", GT_Recipe.GT_Recipe_Map.sCannerRecipes,
				2, 2, 0, 0, 1, "Canner.png", GregTech_API.sSoundList.get(200),
				false, false, 0, "CANNER", null).getStackForm(1L));

		ItemList.CanningMachineUMV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11136, "basicmachine.canner.tier.12", "Epic Can Operator IV",12,
				"Unmobile Food Canning Machine GTA4", GT_Recipe.GT_Recipe_Map.sCannerRecipes,
				2, 2, 0, 0, 1, "Canner.png", GregTech_API.sSoundList.get(200),
				false, false, 0, "CANNER", null).getStackForm(1L));
		
		ItemList.CanningMachineUXV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11137, "basicmachine.canner.tier.13", "Epic Can Operator V",13,
				"Unmobile Food Canning Machine GTA4", GT_Recipe.GT_Recipe_Map.sCannerRecipes,
				2, 2, 0, 0, 1, "Canner.png", GregTech_API.sSoundList.get(200),
				false, false, 0, "CANNER", null).getStackForm(1L));
		
		ItemList.CanningMachineOPV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11138, "basicmachine.canner.tier.14", "Epic Can Operator VI",14,
				"Unmobile Food Canning Machine GTA4", GT_Recipe.GT_Recipe_Map.sCannerRecipes,
				2, 2, 0, 0, 1, "Canner.png", GregTech_API.sSoundList.get(200),
				false, false, 0, "CANNER", null).getStackForm(1L));
		
		
		//ChemicalBath
		ItemList.ChemicalBathUHV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11143, "basicmachine.chemicalbath.tier.09", "Epic Chemical Dunktron", 9,
				"Bathing Ores in Chemicals to separate them", GT_Recipe.GT_Recipe_Map.sChemicalBathRecipes,
				1, 3, 8000, 0, 1, "ChemicalBath.png", "",
				false, true, 0, "CHEMICAL_BATH", new Object[]{"VGW", "PGV", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));

		ItemList.ChemicalBathUEV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11144, "basicmachine.chemicalbath.tier.10", "Epic Chemical Dunktron II",10,
				"Bathing Ores in Chemicals to separate them", GT_Recipe.GT_Recipe_Map.sChemicalBathRecipes,
				1, 3, 8000, 0, 1, "ChemicalBath.png", "",
				false, true, 0, "CHEMICAL_BATH", new Object[]{"VGW", "PGV", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));

		ItemList.ChemicalBathUIV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11145, "basicmachine.chemicalbath.tier.11", "Epic Chemical Dunktron III",11,
				"Bathing Ores in Chemicals to separate them", GT_Recipe.GT_Recipe_Map.sChemicalBathRecipes,
				1, 3, 8000, 0, 1, "ChemicalBath.png", "",
				false, true, 0, "CHEMICAL_BATH", null).getStackForm(1L));

		ItemList.ChemicalBathUMV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11146, "basicmachine.chemicalbath.tier.12", "Epic Chemical Dunktron IV",12,
				"Bathing Ores in Chemicals to separate them", GT_Recipe.GT_Recipe_Map.sChemicalBathRecipes,
				1, 3, 8000, 0, 1, "ChemicalBath.png", "",
				false, true, 0, "CHEMICAL_BATH", null).getStackForm(1L));
		
		ItemList.ChemicalBathUXV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11147, "basicmachine.chemicalbath.tier.13", "Epic Chemical Dunktron V",13,
				"Bathing Ores in Chemicals to separate them", GT_Recipe.GT_Recipe_Map.sChemicalBathRecipes,
				1, 3, 8000, 0, 1, "ChemicalBath.png", "",
				false, true, 0, "CHEMICAL_BATH", null).getStackForm(1L));
		
		ItemList.ChemicalBathOPV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11148, "basicmachine.chemicalbath.tier.14", "Epic Chemical Dunktron VI",14,
				"Bathing Ores in Chemicals to separate them", GT_Recipe.GT_Recipe_Map.sChemicalBathRecipes,
				1, 3, 8000, 0, 1, "ChemicalBath.png", "",
				false, true, 0, "CHEMICAL_BATH", null).getStackForm(1L));
		
		
		//ChemicalReactor
		ItemList.ChemicalReactorUHV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11153, "basicmachine.chemicalreactor.tier.09", "Epic Chemical Performer", 9,
				"Letting Chemicals react with each other", GT_Recipe.GT_Recipe_Map.sChemicalRecipes,
				2, 2, 16000, 0, 1, "ChemicalReactor.png", GregTech_API.sSoundList.get(200),
				false, false, 0, "CHEMICAL_REACTOR", new Object[]{"GRG", "WEW", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OrePrefixes.pipeHuge.get(Materials.Plastic)}).getStackForm(1L));

		ItemList.ChemicalReactorUEV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11154, "basicmachine.chemicalreactor.tier.10", "Epic Chemical Performer II",10,
				"Letting Chemicals react with each other", GT_Recipe.GT_Recipe_Map.sChemicalRecipes,
				2, 2, 16000, 0, 1, "ChemicalReactor.png", GregTech_API.sSoundList.get(200),
				false, false, 0, "CHEMICAL_REACTOR", new Object[]{"GRG", "WEW", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OrePrefixes.pipeHuge.get(Materials.Plastic)}).getStackForm(1L));

		ItemList.ChemicalReactorUIV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11155, "basicmachine.chemicalreactor.tier.11", "Epic Chemical Performer III",11,
				"Letting Chemicals react with each other", GT_Recipe.GT_Recipe_Map.sChemicalRecipes,
				2, 2, 16000, 0, 1, "ChemicalReactor.png", GregTech_API.sSoundList.get(200),
				false, false, 0, "CHEMICAL_REACTOR", null).getStackForm(1L));

		ItemList.ChemicalReactorUMV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11156, "basicmachine.chemicalreactor.tier.12", "Epic Chemical Performer IV",12,
				"Letting Chemicals react with each other", GT_Recipe.GT_Recipe_Map.sChemicalRecipes,
				2, 2, 16000, 0, 1, "ChemicalReactor.png", GregTech_API.sSoundList.get(200),
				false, false, 0, "CHEMICAL_REACTOR", null).getStackForm(1L));
		
		ItemList.ChemicalReactorUXV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11157, "basicmachine.chemicalreactor.tier.13", "Epic Chemical Performer V",13,
				"Letting Chemicals react with each other", GT_Recipe.GT_Recipe_Map.sChemicalRecipes,
				2, 2, 16000, 0, 1, "ChemicalReactor.png", GregTech_API.sSoundList.get(200),
				false, false, 0, "CHEMICAL_REACTOR", null).getStackForm(1L));
		
		ItemList.ChemicalReactorOPV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11158, "basicmachine.chemicalreactor.tier.14", "Epic Chemical Performer VI",14,
				"Letting Chemicals react with each other", GT_Recipe.GT_Recipe_Map.sChemicalRecipes,
				2, 2, 16000, 0, 1, "ChemicalReactor.png", GregTech_API.sSoundList.get(200),
				false, false, 0, "CHEMICAL_REACTOR", null).getStackForm(1L));
		
		
		//Disassembler
		ItemList.DisassemblerUHV.set(new GT_MetaTileEntity_Disassembler(
				11163, "basicmachine.disassembler.tier.09", "Epic Deconstructor", 9).getStackForm(1L));
		ItemList.DisassemblerUEV.set(new GT_MetaTileEntity_Disassembler(
				11164, "basicmachine.disassembler.tier.10", "Epic Deconstructor II",10).getStackForm(1L));
		ItemList.DisassemblerUIV.set(new GT_MetaTileEntity_Disassembler(
				11165, "basicmachine.disassembler.tier.11", "Epic Deconstructor III",11).getStackForm(1L));
		ItemList.DisassemblerUMV.set(new GT_MetaTileEntity_Disassembler(
				11166, "basicmachine.disassembler.tier.12", "Epic Deconstructor IV",12).getStackForm(1L));
		ItemList.DisassemblerUXV.set(new GT_MetaTileEntity_Disassembler(
				11167, "basicmachine.disassembler.tier.13", "Epic Deconstructor V",13).getStackForm(1L));
		ItemList.DisassemblerOPV.set(new GT_MetaTileEntity_Disassembler(
				11168, "basicmachine.disassembler.tier.14", "Epic Deconstructor VI",14).getStackForm(1L));
		
		GT_ModHandler.addCraftingRecipe(ItemList.DisassemblerUHV.get(1L, new Object[0]), bitsd, new Object[]{"ACA", aTextWireHull, "ACA", 'M', ItemList.Hull_MAX, 'A', ItemList.Robot_Arm_UHV, 'C', OrePrefixes.circuit.get(Materials.Infinite), 'W', OrePrefixes.wireGt04.get(Materials.Europium)});
		GT_ModHandler.addCraftingRecipe(ItemList.DisassemblerUEV.get(1L, new Object[0]), bitsd, new Object[]{"ACA", aTextWireHull, "ACA", 'M', ItemList.Hull_UEV, 'A', ItemList.Robot_Arm_UEV, 'C', OrePrefixes.circuit.get(Materials.Bio), 'W', OrePrefixes.wireGt04.get(Materials.Diamericiumtitanium)});

		
		
		//Fermenter
		ItemList.FermenterUHV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11173, "basicmachine.fermenter.tier.09", "Epic Fermentation Hastener", 9,
				"Fermenting Fluids", GT_Recipe.GT_Recipe_Map.sFermentingRecipes,
				1, 1, 2000, 0, 1, "Fermenter.png", "",
				false, false, 0, "FERMENTER", new Object[]{aTextWirePump, "GMG", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));

		ItemList.FermenterUEV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11174, "basicmachine.fermenter.tier.10", "Epic Fermentation Hastener II",10,
				"Fermenting Fluids", GT_Recipe.GT_Recipe_Map.sFermentingRecipes,
				1, 1, 2000, 0, 1, "Fermenter.png", "",
				false, false, 0, "FERMENTER", new Object[]{aTextWirePump, "GMG", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));

		ItemList.FermenterUIV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11175, "basicmachine.fermenter.tier.11", "Epic Fermentation Hastener III",11,
				"Fermenting Fluids", GT_Recipe.GT_Recipe_Map.sFermentingRecipes,
				1, 1, 4000, 0, 1, "Fermenter.png", "",
				false, false, 0, "FERMENTER", null).getStackForm(1L));

		ItemList.FermenterUMV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11176, "basicmachine.fermenter.tier.12", "Epic Fermentation Hastener IV",12,
				"Fermenting Fluids", GT_Recipe.GT_Recipe_Map.sFermentingRecipes,
				1, 1, 4000, 0, 1, "Fermenter.png", "",
				false, false, 0, "FERMENTER", null).getStackForm(1L));
		
		ItemList.FermenterUXV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11177, "basicmachine.fermenter.tier.13", "Epic Fermentation Hastener V",13,
				"Fermenting Fluids", GT_Recipe.GT_Recipe_Map.sFermentingRecipes,
				1, 1, 8000, 0, 1, "Fermenter.png", "",
				false, false, 0, "FERMENTER", null).getStackForm(1L));
		
		ItemList.FermenterOPV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11178, "basicmachine.fermenter.tier.14", "Epic Fermentation Hastener VI",14,
				"Fermenting Fluids", GT_Recipe.GT_Recipe_Map.sFermentingRecipes,
				1, 1, 8000, 0, 1, "Fermenter.png", "",
				false, false, 0, "FERMENTER", null).getStackForm(1L));
		
		
		//FluidCanner
		ItemList.FluidCannerUHV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11183, "basicmachine.fluidcanner.tier.09", "Epic Liquid Can Actuator", 9,
				"Puts Fluids into and out of Containers", GT_Recipe.GT_Recipe_Map.sFluidCannerRecipes,
				1, 1, 144000, 0, 1, "FluidCanner.png", "",
				true, false, 0, "FLUID_CANNER", new Object[]{"GCG", "GMG", "WPW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));

		ItemList.FluidCannerUEV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11184, "basicmachine.fluidcanner.tier.10", "Epic Liquid Can Actuator II",10,
				"Puts Fluids into and out of Containers", GT_Recipe.GT_Recipe_Map.sFluidCannerRecipes,
				1, 1, 160000, 0, 1, "FluidCanner.png", "",
				true, false, 0, "FLUID_CANNER", new Object[]{"GCG", "GMG", "WPW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));

		ItemList.FluidCannerUIV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11185, "basicmachine.fluidcanner.tier.11", "Epic Liquid Can Actuator III",11,
				"Puts Fluids into and out of Containers", GT_Recipe.GT_Recipe_Map.sFluidCannerRecipes,
				1, 1, 176000, 0, 1, "FluidCanner.png", "",
				true, false, 0, "FLUID_CANNER", null).getStackForm(1L));

		ItemList.FluidCannerUMV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11186, "basicmachine.fluidcanner.tier.12", "Epic Liquid Can Actuator IV",12,
				"Puts Fluids into and out of Containers", GT_Recipe.GT_Recipe_Map.sFluidCannerRecipes,
				1, 1, 192000, 0, 1, "FluidCanner.png", "",
				true, false, 0, "FLUID_CANNER", null).getStackForm(1L));
		
		ItemList.FluidCannerUXV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11187, "basicmachine.fluidcanner.tier.13", "Epic Liquid Can Actuator V",13,
				"Puts Fluids into and out of Containers", GT_Recipe.GT_Recipe_Map.sFluidCannerRecipes,
				1, 1, 192000, 0, 1, "FluidCanner.png", "",
				true, false, 0, "FLUID_CANNER", null).getStackForm(1L));
		
		ItemList.FluidCannerOPV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11188, "basicmachine.fluidcanner.tier.14", "Epic Liquid Can Actuator VI",14,
				"Puts Fluids into and out of Containers", GT_Recipe.GT_Recipe_Map.sFluidCannerRecipes,
				1, 1, 192000, 0, 1, "FluidCanner.png", "",
				true, false, 0, "FLUID_CANNER", null).getStackForm(1L));
		
		
		//FluidExtractor
		ItemList.FluidExtractorUHV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11193, "basicmachine.fluidextractor.tier.09", "Epic Liquefying Sucker", 9,
				"Extracting Fluids from Items", GT_Recipe.GT_Recipe_Map.sFluidExtractionRecipes,
				1, 1, 16000, 0, 1, "FluidExtractor.png", GregTech_API.sSoundList.get(200),
				false, false, 0, "FLUID_EXTRACTOR", new Object[]{"GCG", "PME", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));

		ItemList.FluidExtractorUEV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11194, "basicmachine.fluidextractor.tier.10", "Epic Liquefying Sucker II",10,
				"Extracting Fluids from Items", GT_Recipe.GT_Recipe_Map.sFluidExtractionRecipes,
				1, 1, 16000, 0, 1, "FluidExtractor.png", GregTech_API.sSoundList.get(200),
				false, false, 0, "FLUID_EXTRACTOR", new Object[]{"GCG", "PME", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));

		ItemList.FluidExtractorUIV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11195, "basicmachine.fluidextractor.tier.11", "Epic Liquefying Sucker III",11,
				"Extracting Fluids from Items", GT_Recipe.GT_Recipe_Map.sFluidExtractionRecipes,
				1, 1, 16000, 0, 1, "FluidExtractor.png", GregTech_API.sSoundList.get(200),
				false, false, 0, "FLUID_EXTRACTOR", null).getStackForm(1L));

		ItemList.FluidExtractorUMV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11196, "basicmachine.fluidextractor.tier.12", "Epic Liquefying Sucker IV",12,
				"Extracting Fluids from Items", GT_Recipe.GT_Recipe_Map.sFluidExtractionRecipes,
				1, 1, 16000, 0, 1, "FluidExtractor.png", GregTech_API.sSoundList.get(200),
				false, false, 0, "FLUID_EXTRACTOR", null).getStackForm(1L));
		
		ItemList.FluidExtractorUXV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11197, "basicmachine.fluidextractor.tier.13", "Epic Liquefying Sucker V",13,
				"Extracting Fluids from Items", GT_Recipe.GT_Recipe_Map.sFluidExtractionRecipes,
				1, 1, 16000, 0, 1, "FluidExtractor.png", GregTech_API.sSoundList.get(200),
				false, false, 0, "FLUID_EXTRACTOR", null).getStackForm(1L));
		
		ItemList.FluidExtractorOPV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11198, "basicmachine.fluidextractor.tier.14", "Epic Liquefying Sucker VI",14,
				"Extracting Fluids from Items", GT_Recipe.GT_Recipe_Map.sFluidExtractionRecipes,
				1, 1, 16000, 0, 1, "FluidExtractor.png", GregTech_API.sSoundList.get(200),
				false, false, 0, "FLUID_EXTRACTOR", null).getStackForm(1L));
		
		
		//FluidHeater
		ItemList.FluidHeaterUHV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11203, "basicmachine.fluidheater.tier.09", "Epic Heat Infuser", 9,
				"Heating up your Fluids", GT_Recipe.GT_Recipe_Map.sFluidHeaterRecipes,
				1, 0, 8000, 0, 1, "FluidHeater.png", "",
				false, false, 0, "FLUID_HEATER", new Object[]{"OGO", aTextPlateMotor, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'O', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));

		ItemList.FluidHeaterUEV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11204, "basicmachine.fluidheater.tier.10", "Epic Heat Infuser II",10,
				"Heating up your Fluids", GT_Recipe.GT_Recipe_Map.sFluidHeaterRecipes,
				1, 0, 8000, 0, 1, "FluidHeater.png", "",
				false, false, 0, "FLUID_HEATER", new Object[]{"OGO", aTextPlateMotor, aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'O', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));

		ItemList.FluidHeaterUIV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11205, "basicmachine.fluidheater.tier.11", "Epic Heat Infuser III",11,
				"Heating up your Fluids", GT_Recipe.GT_Recipe_Map.sFluidHeaterRecipes,
				1, 0, 8000, 0, 1, "FluidHeater.png", "",
				false, false, 0, "FLUID_HEATER", null).getStackForm(1L));

		ItemList.FluidHeaterUMV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11206, "basicmachine.fluidheater.tier.12", "Epic Heat Infuser IV",12,
				"Heating up your Fluids", GT_Recipe.GT_Recipe_Map.sFluidHeaterRecipes,
				1, 0, 8000, 0, 1, "FluidHeater.png", "",
				false, false, 0, "FLUID_HEATER", null).getStackForm(1L));
		
		ItemList.FluidHeaterUXV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11207, "basicmachine.fluidheater.tier.13", "Epic Heat Infuser V",13,
				"Heating up your Fluids", GT_Recipe.GT_Recipe_Map.sFluidHeaterRecipes,
				1, 0, 8000, 0, 1, "FluidHeater.png", "",
				false, false, 0, "FLUID_HEATER", null).getStackForm(1L));
		
		ItemList.FluidHeaterOPV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11208, "basicmachine.fluidheater.tier.14", "Epic Heat Infuser VI",14,
				"Heating up your Fluids", GT_Recipe.GT_Recipe_Map.sFluidHeaterRecipes,
				1, 0, 8000, 0, 1, "FluidHeater.png", "",
				false, false, 0, "FLUID_HEATER", null).getStackForm(1L));
		
		
		//Mixer
		ItemList.MixerUHV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11213, "basicmachine.mixer.tier.09", "Epic Matter Organizer", 9,
				"Will it Blend?", GT_Recipe.GT_Recipe_Map.sMixerRecipes,
				6, 1, 72000, 0, 1, "Mixer.png", "",
				false, false, 0, "MIXER", new Object[]{"GRG", "GEG", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));

		ItemList.MixerUEV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11214, "basicmachine.mixer.tier.10", "Epic Matter Organizer II",10,
				"Will it Blend?", GT_Recipe.GT_Recipe_Map.sMixerRecipes,
				6, 1, 96000, 0, 1, "Mixer.png", "",
				false, false, 0, "MIXER", new Object[]{"GRG", "GEG", aTextCableHull, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'G', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS}).getStackForm(1L));

		ItemList.MixerUIV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11215, "basicmachine.mixer.tier.11", "Epic Matter Organizer III",11,
				"Will it Blend?", GT_Recipe.GT_Recipe_Map.sMixerRecipes,
				6, 1, 128000, 0, 1, "Mixer.png", "",
				false, false, 0, "MIXER", null).getStackForm(1L));

		ItemList.MixerUMV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11216, "basicmachine.mixer.tier.12", "Epic Matter Organizer IV",12,
				"Will it Blend?", GT_Recipe.GT_Recipe_Map.sMixerRecipes,
				6, 1, 128000, 0, 1, "Mixer.png", "",
				false, false, 0, "MIXER", null).getStackForm(1L));
		
		ItemList.MixerUXV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11217, "basicmachine.mixer.tier.13", "Epic Matter Organizer V",13,
				"Will it Blend?", GT_Recipe.GT_Recipe_Map.sMixerRecipes,
				6, 1, 128000, 0, 1, "Mixer.png", "",
				false, false, 0, "MIXER", null).getStackForm(1L));
		
		ItemList.MixerOPV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				11218, "basicmachine.mixer.tier.14", "Epic Matter Organizer VI",14,
				"Will it Blend?", GT_Recipe.GT_Recipe_Map.sMixerRecipes,
				6, 1, 128000, 0, 1, "Mixer.png", "",
				false, false, 0, "MIXER", null).getStackForm(1L));
		
		//Circuit Assembler
				ItemList.Machine_UHV_CircuitAssembler.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
						12080, "basicmachine.circuitassembler.tier.09", "Epic Circuit Assembling Machine", 9,
						"Avengers, Assemble!", GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes,
						6, 1, 16000, 0, 1, "CircuitAssembler.png", "",
						false, false, 0, "CIRCUITASSEMBLER", new Object[]{"ACE", "VMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER}).getStackForm(1L));
				
				ItemList.Machine_UEV_CircuitAssembler.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
						12081, "basicmachine.circuitassembler.tier.10", "Epic Circuit Assembling Machine II", 10,
						"Avengers, Assemble!", GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes,
						6, 1, 16000, 0, 1, "CircuitAssembler.png", "",
						false, false, 0, "CIRCUITASSEMBLER", new Object[]{"ACE", "VMV", aTextWireCoil, 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'E', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER}).getStackForm(1L));
				
				ItemList.Machine_UIV_CircuitAssembler.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
						12082, "basicmachine.circuitassembler.tier.11", "Epic Circuit Assembling Machine III", 11,
						"Avengers, Assemble!", GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes,
						6, 1, 16000, 0, 1, "CircuitAssembler.png", "",
						false, false, 0, "CIRCUITASSEMBLER", null).getStackForm(1L));
				
				ItemList.Machine_UMV_CircuitAssembler.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
						12083, "basicmachine.circuitassembler.tier.12", "Epic Circuit Assembling Machine IV", 12,
						"Avengers, Assemble!", GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes,
						6, 1, 16000, 0, 1, "CircuitAssembler.png", "",
						false, false, 0, "CIRCUITASSEMBLER", null).getStackForm(1L));
				
				ItemList.Machine_UXV_CircuitAssembler.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
						12084, "basicmachine.circuitassembler.tier.13", "Epic Circuit Assembling Machine V", 13,
						"Avengers, Assemble!", GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes,
						6, 1, 16000, 0, 1, "CircuitAssembler.png", "",
						false, false, 0, "CIRCUITASSEMBLER", null).getStackForm(1L));
				
				ItemList.Machine_OPV_CircuitAssembler.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
						12085, "basicmachine.circuitassembler.tier.14", "Epic Circuit Assembling Machine VI", 14,
						"Avengers, Assemble!", GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes,
						6, 1, 16000, 0, 1, "CircuitAssembler.png", "",
						false, false, 0, "CIRCUITASSEMBLER", null).getStackForm(1L));
				
				//DarkMatter Reactor
				ItemList.Generator_DarkMatter_Mark_I.set(new GT_MetaTileEntity_DarkMatterGenerator(12190, "basicgenerator.darkmatter.tier.09", "Dark Matter Reactor Mark I", 9).getStackForm(1L));
				
		// ===================================================================================================
		// Wet Transformers
		// ===================================================================================================
                    
					ItemList.Transformer_HA_LV_ULV.set(new GT_MetaTileEntity_TransformerHiAmp(
							12135, "transformer.ha.tier.00", "Ultra Low Voltage Hi-Amp Transformer", 0,
							"LV -> ULV (Use Soft Mallet to invert)").getStackForm(1L));
					ItemList.Transformer_HA_MV_LV.set(new GT_MetaTileEntity_TransformerHiAmp(
							12136, "transformer.ha.tier.01", "Low Voltage Hi-Amp Transformer", 1,
							"MV -> LV (Use Soft Mallet to invert)").getStackForm(1L));
					ItemList.Transformer_HA_HV_MV.set(new GT_MetaTileEntity_TransformerHiAmp(
							12137, "transformer.ha.tier.02", "Medium Voltage Hi-Amp Transformer", 2,
							"HV -> MV (Use Soft Mallet to invert)").getStackForm(1L));
					ItemList.Transformer_HA_EV_HV.set(new GT_MetaTileEntity_TransformerHiAmp(
							12138, "transformer.ha.tier.03", "High Voltage Hi-Amp Transformer", 3,
							"EV -> HV (Use Soft Mallet to invert)").getStackForm(1L));
					ItemList.Transformer_HA_IV_EV.set(new GT_MetaTileEntity_TransformerHiAmp(
							12139, "transformer.ha.tier.04", "Extreme Hi-Amp Transformer", 4,
							"IV -> EV (Use Soft Mallet to invert)").getStackForm(1L));
					ItemList.Transformer_HA_LuV_IV.set(new GT_MetaTileEntity_TransformerHiAmp(
							12140, "transformer.ha.tier.05", "Insane Hi-Amp Transformer", 5,
							"LuV -> IV (Use Soft Mallet to invert)").getStackForm(1L));
					ItemList.Transformer_HA_ZPM_LuV.set(new GT_MetaTileEntity_TransformerHiAmp(
							12141, "transformer.ha.tier.06", "Ludicrous Hi-Amp Transformer", 6,
							"ZPM -> LuV (Use Soft Mallet to invert)").getStackForm(1L));
					ItemList.Transformer_HA_UV_ZPM.set(new GT_MetaTileEntity_TransformerHiAmp(
							12142, "transformer.ha.tier.07", "ZPM Voltage Hi-Amp Transformer", 7,
							"UV -> ZPM (Use Soft Mallet to invert)").getStackForm(1L));
					ItemList.Transformer_HA_UHV_UV.set(new GT_MetaTileEntity_TransformerHiAmp(
							12143, "transformer.ha.tier.08", "Ultimate Hi-Amp Transformer", 8,
							"UHV -> UV (Use Soft Mallet to invert)").getStackForm(1L));
					ItemList.Transformer_HA_UEV_UHV.set(new GT_MetaTileEntity_TransformerHiAmp(
							12144, "transformer.ha.tier.09", "Highly Ultimate Hi-Amp Transformer", 9,
							"UEV -> UHV (Use Soft Mallet to invert)").getStackForm(1L));
					ItemList.Transformer_HA_UIV_UEV.set(new GT_MetaTileEntity_TransformerHiAmp(
							12145, "transformer.ha.tier.10", "Extremely Ultimate Hi-Amp Transformer",10,
							"UIV -> UEV (Use Soft Mallet to invert)").getStackForm(1L));
					ItemList.Transformer_HA_UMV_UIV.set(new GT_MetaTileEntity_TransformerHiAmp(
							12146, "transformer.ha.tier.11", "Insanely Ultimate Hi-Amp Transformer",11,
							"UMV -> UIV (Use Soft Mallet to invert)").getStackForm(1L));
					ItemList.Transformer_HA_UXV_UMV.set(new GT_MetaTileEntity_TransformerHiAmp(
							12147, "transformer.ha.tier.12", "Mega Ultimate Hi-Amp Transformer",12,
							"UXV -> UMV (Use Soft Mallet to invert)").getStackForm(1L));
					ItemList.Transformer_HA_OPV_UXV.set(new GT_MetaTileEntity_TransformerHiAmp(
							12148, "transformer.ha.tier.13", "Extended Mega Ultimate Hi-Amp Transformer",13,
							"OPV -> UXV (Use Soft Mallet to invert)").getStackForm(1L));
					ItemList.Transformer_HA_MAXV_OPV.set(new GT_MetaTileEntity_TransformerHiAmp(
							12149, "transformer.ha.tier.14", "Overpowered Hi-Amp Transformer",14,
							"MAX -> OPV (Use Soft Mallet to invert)").getStackForm(1L));
					GT_ModHandler.addCraftingRecipe(ItemList.Transformer_HA_LV_ULV.get(1L, new Object[0]), bitsd,
							new Object[] { " BB", "CM ", " BB", Character.valueOf('M'), ItemList.Transformer_LV_ULV, Character.valueOf('C'),
									OrePrefixes.wireGt16.get(Materials.Tin), Character.valueOf('B'),
									OrePrefixes.wireGt16.get(Materials.Lead) });
					GT_ModHandler.addCraftingRecipe(ItemList.Transformer_HA_MV_LV.get(1L, new Object[0]), bitsd,
							new Object[] { " BB", "CM ", " BB", Character.valueOf('M'), ItemList.Transformer_MV_LV, Character.valueOf('C'),
									OrePrefixes.wireGt16.get(Materials.Copper), Character.valueOf('B'),
									OrePrefixes.wireGt16.get(Materials.Tin) });
					GT_ModHandler.addCraftingRecipe(ItemList.Transformer_HA_HV_MV.get(1L, new Object[0]), bitsd,
							new Object[] { " BB", "CM ", " BB", Character.valueOf('M'), ItemList.Transformer_HV_MV, Character.valueOf('C'),
									OrePrefixes.wireGt16.get(Materials.Gold), Character.valueOf('B'),
									OrePrefixes.wireGt16.get(Materials.Copper) });
					GT_ModHandler.addCraftingRecipe(ItemList.Transformer_HA_EV_HV.get(1L, new Object[0]), bitsd,
							new Object[] { "KBB", "CM ", "KBB", Character.valueOf('M'), ItemList.Transformer_EV_HV, Character.valueOf('C'),
									OrePrefixes.wireGt16.get(Materials.Aluminium), Character.valueOf('B'),
									OrePrefixes.wireGt16.get(Materials.Gold), Character.valueOf('K'),
									ItemList.Casing_Coil_Cupronickel });
					GT_ModHandler.addCraftingRecipe(ItemList.Transformer_HA_IV_EV.get(1L, new Object[0]), bitsd,
							new Object[] { "KBB", "CM ", "KBB", Character.valueOf('M'), ItemList.Transformer_IV_EV, Character.valueOf('C'),
									OrePrefixes.wireGt16.get(Materials.Tungsten), Character.valueOf('B'),
									OrePrefixes.wireGt16.get(Materials.Aluminium), Character.valueOf('K'),
									ItemList.Casing_Coil_Kanthal });
					GT_ModHandler.addCraftingRecipe(ItemList.Transformer_HA_LuV_IV.get(1L, new Object[0]), bitsd,
							new Object[] { "KBB", "CM ", "KBB", Character.valueOf('M'), ItemList.Transformer_LuV_IV, Character.valueOf('C'),
									OrePrefixes.wireGt16.get(Materials.VanadiumGallium), Character.valueOf('B'),
									OrePrefixes.wireGt16.get(Materials.Tungsten), Character.valueOf('K'),
									ItemList.Casing_Coil_Nichrome });
					GT_ModHandler.addCraftingRecipe(ItemList.Transformer_HA_ZPM_LuV.get(1L, new Object[0]), bitsd,
							new Object[] { "KBB", "CM ", "KBB", Character.valueOf('M'), ItemList.Transformer_ZPM_LuV, Character.valueOf('C'),
									OrePrefixes.wireGt16.get(Materials.Naquadah), Character.valueOf('B'),
									OrePrefixes.wireGt16.get(Materials.VanadiumGallium), Character.valueOf('K'),
									ItemList.Casing_Coil_TungstenSteel });
					GT_ModHandler.addCraftingRecipe(ItemList.Transformer_HA_UV_ZPM.get(1L, new Object[0]), bitsd,
							new Object[] { "KBB", "CM ", "KBB", Character.valueOf('M'), ItemList.Transformer_UV_ZPM, Character.valueOf('C'),
									OrePrefixes.wireGt04.get(Materials.NaquadahAlloy), Character.valueOf('B'),
									OrePrefixes.wireGt16.get(Materials.Naquadah), Character.valueOf('K'),
									ItemList.Casing_Coil_Naquadah });
					GT_ModHandler.addCraftingRecipe(ItemList.Transformer_HA_UHV_UV.get(1L, new Object[0]), bitsd,
							new Object[] { "KBB", "CME", "KBB", Character.valueOf('M'), ItemList.Transformer_MAX_UV, Character.valueOf('C'),
									OrePrefixes.wireGt01.get(Materials.Superconductor), Character.valueOf('B'),
									OrePrefixes.wireGt04.get(Materials.NaquadahAlloy), Character.valueOf('K'),
									ItemList.Casing_Coil_NaquadahAlloy, Character.valueOf('E'),
									ItemList.Circuit_HighEnergyFlow});
					GT_ModHandler.addCraftingRecipe(ItemList.Transformer_HA_UEV_UHV.get(1L, new Object[0]), bitsd,
							new Object[] { "KBB", "CME", "KBB", Character.valueOf('M'), ItemList.Transformer_UEV_UHV, Character.valueOf('C'),
									OrePrefixes.wireGt04.get(Materials.SuperconductorUEV), Character.valueOf('B'),
									OrePrefixes.wireGt01.get(Materials.Superconductor), Character.valueOf('K'),
									ItemList.Casing_Coil_Superconductor, Character.valueOf('E'),
									ItemList.Circuit_HighEnergyFlow});
					GT_ModHandler.addCraftingRecipe(ItemList.Transformer_HA_UIV_UEV.get(1L, new Object[0]), bitsd,
							new Object[] { "KBB", "CME", "KBB", Character.valueOf('M'), ItemList.Transformer_UIV_UEV, Character.valueOf('C'),
									OrePrefixes.wireGt08.get(Materials.Neutronium), Character.valueOf('B'),
									OrePrefixes.wireGt01.get(Materials.SuperconductorUEV), Character.valueOf('K'),
									ItemList.Casing_Fusion_Coil, Character.valueOf('E'),
									ItemList.Circuit_HighEnergyFlow});
					GT_ModHandler.addCraftingRecipe(ItemList.Transformer_HA_UMV_UIV.get(1L, new Object[0]), bitsd,
							new Object[] { "KBB", "CME", "KBB", Character.valueOf('M'), ItemList.Transformer_UMV_UIV, Character.valueOf('C'),
									OrePrefixes.wireGt08.get(Materials.Quantium), Character.valueOf('B'),
									OrePrefixes.wireGt04.get(Materials.Neutronium), Character.valueOf('K'),
									ItemList.Casing_Fusion_Coil, Character.valueOf('E'),
									ItemList.Circuit_HighEnergyFlow});	
				
				ItemList.WetTransformer_LV_ULV.set(new GT_MetaTileEntity_Wet_Transformer(
						12150, "wettransformer.tier.00", "Ultra Low Voltage Power Transformer", 0,
						"LV -> ULV (Use Soft Mallet to invert)").getStackForm(1L));

				ItemList.WetTransformer_MV_LV.set(new GT_MetaTileEntity_Wet_Transformer(
						12151, "wetransformer.tier.01", "Low Voltage Power Transformer", 1,
						"MV -> LV (Use Soft Mallet to invert)").getStackForm(1L));

				ItemList.WetTransformer_HV_MV.set(new GT_MetaTileEntity_Wet_Transformer(
						12152, "wettransformer.tier.02", "Medium Voltage Power Transformer", 2,
						"HV -> MV (Use Soft Mallet to invert)").getStackForm(1L));

				ItemList.WetTransformer_EV_HV.set(new GT_MetaTileEntity_Wet_Transformer(
						12153, "wettransformer.tier.03", "High Voltage Power Transformer", 3,
						"EV -> HV (Use Soft Mallet to invert)").getStackForm(1L));

				ItemList.WetTransformer_IV_EV.set(new GT_MetaTileEntity_Wet_Transformer(
						12154, "wettransformer.tier.04", "Extreme Power Transformer", 4,
						"IV -> EV (Use Soft Mallet to invert)").getStackForm(1L));

				ItemList.WetTransformer_LuV_IV.set(new GT_MetaTileEntity_Wet_Transformer(
						12155, "wettransformer.tier.05", "Insane Power Transformer", 5,
						"LuV -> IV (Use Soft Mallet to invert)").getStackForm(1L));

				ItemList.WetTransformer_ZPM_LuV.set(new GT_MetaTileEntity_Wet_Transformer(
						12156, "wettransformer.tier.06", "Ludicrous Power Transformer", 6,
						"ZPM -> LuV (Use Soft Mallet to invert)").getStackForm(1L));

				ItemList.WetTransformer_UV_ZPM.set(new GT_MetaTileEntity_Wet_Transformer(
						12157, "wettransformer.tier.07", "ZPM Voltage Power Transformer", 7,
						"UV -> ZPM (Use Soft Mallet to invert)").getStackForm(1L));

				ItemList.WetTransformer_UHV_UV.set(new GT_MetaTileEntity_Wet_Transformer(
						12158, "wettransformer.tier.08", "Ultimate Power Transformer", 8,
						"UHV -> UV (Use Soft Mallet to invert)").getStackForm(1L));

				ItemList.WetTransformer_UEV_UHV.set(new GT_MetaTileEntity_Wet_Transformer(
						12159, "wettransformer.tier.09", "Highly Ultimate Power Transformer", 9,
						"UEV -> UHV (Use Soft Mallet to invert)").getStackForm(1L));

				ItemList.WetTransformer_UIV_UEV.set(new GT_MetaTileEntity_Wet_Transformer(
						12160, "wettransformer.tier.10", "Extremely Ultimate Power Transformer",10,
						"UIV -> UEV (Use Soft Mallet to invert)").getStackForm(1L));

				ItemList.WetTransformer_UMV_UIV.set(new GT_MetaTileEntity_Wet_Transformer(
						12161, "wettransformer.tier.11", "Insanely Ultimate Power Transformer",11,
						"UMV -> UIV (Use Soft Mallet to invert)").getStackForm(1L));

				ItemList.WetTransformer_UXV_UMV.set(new GT_MetaTileEntity_Wet_Transformer(
						12162, "wettransformer.tier.12", "Mega Ultimate Power Transformer",12,
						"UXV -> UMV (Use Soft Mallet to invert)").getStackForm(1L));

				ItemList.WetTransformer_OPV_UXV.set(new GT_MetaTileEntity_Wet_Transformer(
						12163, "wettransformer.tier.13", "Extended Mega Ultimate Power Transformer",13,
						"OPV -> UXV (Use Soft Mallet to invert)").getStackForm(1L));

				ItemList.WetTransformer_MAXV_OPV.set(new GT_MetaTileEntity_Wet_Transformer(
						12164, "wettransformer.tier.14", "Overpowered Power Transformer",14,
						"MAX -> OPV (Use Soft Mallet to invert)").getStackForm(1L));
				
						GT_ModHandler.addCraftingRecipe(ItemList.WetTransformer_LV_ULV.get(1L),
								bitsd,
								new Object[]{"XOC", "STA", "POC",
										'A', OrePrefixes.springSmall.get(Materials.Lead),
										'C', OrePrefixes.wireGt16.get(Materials.Lead),
										'S', OrePrefixes.spring.get(Materials.Tin),
										'X', OrePrefixes.wireGt08.get(Materials.Tin),
										'O', OrePrefixes.cell.get(Materials.Lubricant),
										'P', ItemList.Electric_Pump_LV,
										'T', ItemList.Transformer_HA_LV_ULV});

						GT_ModHandler.addCraftingRecipe(ItemList.WetTransformer_MV_LV.get(1L),
								bitsd,
								new Object[]{"XOC", "STA", "POC",
										'A', OrePrefixes.springSmall.get(Materials.Tin),
										'C', OrePrefixes.wireGt16.get(Materials.Tin),
										'S', OrePrefixes.spring.get(Materials.Copper),
										'X', OrePrefixes.wireGt08.get(Materials.Copper),
										'O', OrePrefixes.cell.get(Materials.Lubricant),
										'P', ItemList.Electric_Pump_LV,
										'T', ItemList.Transformer_HA_MV_LV});

						GT_ModHandler.addCraftingRecipe(ItemList.WetTransformer_HV_MV.get(1L),
								bitsd,
								new Object[]{"XOC", "STA", "POC",
										'A', OrePrefixes.springSmall.get(Materials.Copper),
										'C', OrePrefixes.wireGt16.get(Materials.Copper),
										'S', OrePrefixes.spring.get(Materials.Gold),
										'X', OrePrefixes.wireGt08.get(Materials.Gold),
										'O', OrePrefixes.cell.get(Materials.Lubricant),
										'P', ItemList.Electric_Pump_MV,
										'T', ItemList.Transformer_HA_HV_MV});

						GT_ModHandler.addCraftingRecipe(ItemList.WetTransformer_EV_HV.get(1L),
								bitsd,
								new Object[]{"XOC", "STA", "POC",
										'A', OrePrefixes.springSmall.get(Materials.Gold),
										'C', OrePrefixes.wireGt16.get(Materials.Gold),
										'S', OrePrefixes.spring.get(Materials.Aluminium),
										'X', OrePrefixes.wireGt08.get(Materials.Aluminium),
										'O', OrePrefixes.cell.get(Materials.Lubricant),
										'P', ItemList.Electric_Pump_MV,
										'T', ItemList.Transformer_HA_EV_HV});

						GT_ModHandler.addCraftingRecipe(ItemList.WetTransformer_IV_EV.get(1L),
								bitsd,
								new Object[]{"XOC", "STA", "POC",
										'A', OrePrefixes.springSmall.get(Materials.Aluminium),
										'C', OrePrefixes.wireGt16.get(Materials.Aluminium),
										'S', OrePrefixes.spring.get(Materials.Tungsten),
										'X', OrePrefixes.wireGt08.get(Materials.Tungsten),
										'O', OrePrefixes.cell.get(Materials.Lubricant),
										'P', ItemList.Electric_Pump_HV,
										'T', ItemList.Transformer_HA_IV_EV});

						GT_ModHandler.addCraftingRecipe(ItemList.WetTransformer_LuV_IV.get(1L),
								bitsd,
								new Object[]{"XOC", "STA", "POC",
										'A', OrePrefixes.springSmall.get(Materials.Tungsten),
										'C', OrePrefixes.wireGt16.get(Materials.Tungsten),
										'S', OrePrefixes.spring.get(Materials.VanadiumGallium),
										'X', OrePrefixes.wireGt08.get(Materials.VanadiumGallium),
										'O', OrePrefixes.cell.get(Materials.Lubricant),
										'P', ItemList.Electric_Pump_HV,
										'T', ItemList.Transformer_HA_LuV_IV});

						GT_ModHandler.addCraftingRecipe(ItemList.WetTransformer_ZPM_LuV.get(1L),
								bitsd,
								new Object[]{"XOC", "STA", "POC",
										'A', OrePrefixes.springSmall.get(Materials.VanadiumGallium),
										'C', OrePrefixes.wireGt16.get(Materials.VanadiumGallium),
										'S', OrePrefixes.spring.get(Materials.Naquadah),
										'X', OrePrefixes.wireGt08.get(Materials.Naquadah),
										'O', OrePrefixes.cell.get(Materials.Lubricant),
										'P', ItemList.Electric_Pump_EV,
										'T', ItemList.Transformer_HA_ZPM_LuV});

						GT_ModHandler.addCraftingRecipe(ItemList.WetTransformer_UV_ZPM.get(1L),
								bitsd,
								new Object[]{"XOC", "STA", "POC",
										'A', OrePrefixes.springSmall.get(Materials.Naquadah),
										'C', OrePrefixes.wireGt16.get(Materials.Naquadah),
										'S', OrePrefixes.spring.get(Materials.NaquadahAlloy),
										'X', OrePrefixes.wireGt08.get(Materials.NaquadahAlloy),
										'O', OrePrefixes.cell.get(Materials.Lubricant),
										'P', ItemList.Electric_Pump_EV,
										'T', ItemList.Transformer_HA_UV_ZPM});

						GT_ModHandler.addCraftingRecipe(ItemList.WetTransformer_UHV_UV.get(1L),
								bitsd,
								new Object[]{"XOC", "STA", "POC",
										'A', OrePrefixes.springSmall.get(Materials.NaquadahAlloy),
										'C', OrePrefixes.wireGt16.get(Materials.NaquadahAlloy),
										'S', OrePrefixes.spring.get(Materials.Europium),
										'X', OrePrefixes.wireGt08.get(Materials.Europium),
										'O', OrePrefixes.cell.get(Materials.Lubricant),
										'P', ItemList.Electric_Pump_IV,
										'T', ItemList.Transformer_HA_UHV_UV});

						GT_ModHandler.addCraftingRecipe(ItemList.WetTransformer_UEV_UHV.get(1L),
								bitsd,
								new Object[]{"XOC", "STA", "POC",
										'A', OrePrefixes.springSmall.get(Materials.Europium),
										'C', OrePrefixes.wireGt16.get(Materials.Europium),
										'S', OrePrefixes.spring.get(Materials.Diamericiumtitanium),
										'X', OrePrefixes.wireGt08.get(Materials.Diamericiumtitanium),
										'O', ItemList.Reactor_Coolant_He_1,
										'P', ItemList.Electric_Pump_IV,
										'T', ItemList.Transformer_HA_UEV_UHV});

						GT_ModHandler.addCraftingRecipe(ItemList.WetTransformer_UIV_UEV.get(1L),
								bitsd,
								new Object[]{"XOC", "STA", "POC",
										'A', OrePrefixes.springSmall.get(Materials.Diamericiumtitanium),
										'C', OrePrefixes.wireGt16.get(Materials.Diamericiumtitanium),
										'S', OrePrefixes.spring.get(Materials.Neutronium),
										'X', OrePrefixes.wireGt08.get(Materials.Neutronium),
										'O', ItemList.Reactor_Coolant_He_3,
										'P', ItemList.Electric_Pump_LuV,
										'T', ItemList.Transformer_HA_UIV_UEV});

						GT_ModHandler.addCraftingRecipe(ItemList.WetTransformer_UMV_UIV.get(1L),
								bitsd,
								new Object[]{"XOC", "STA", "POC",
										'A', OrePrefixes.springSmall.get(Materials.Neutronium),
										'C', OrePrefixes.wireGt16.get(Materials.Neutronium),
										'S', OrePrefixes.spring.get(Materials.Quantium),
										'X', OrePrefixes.wireGt08.get(Materials.Quantium),
										'O', ItemList.Reactor_Coolant_He_6,
										'P', ItemList.Electric_Pump_LuV,
										'T', ItemList.Transformer_HA_UMV_UIV});
					
		// ===================================================================================================
		// Turbo Chargers
		// ===================================================================================================
						ItemList.Battery_TurboCharger_4by4_ULV.set(new GT_MetaTileEntity_TurboCharger(
								12170, "batteryturbocharger.16.tier.00", "Ultra Low Voltage Turbo Charger",0,"64A in /16A out, 120A/item, Disable to force Charge", 4).getStackForm(1L));

						ItemList.Battery_TurboCharger_4by4_LV.set(new GT_MetaTileEntity_TurboCharger(
								12171, "batteryturbocharger.16.tier.01", "Low Voltage Turbo Charger",1,"64A in /16A out, 120A/item, Disable to force Charge", 4).getStackForm(1L));

						ItemList.Battery_TurboCharger_4by4_MV.set(new GT_MetaTileEntity_TurboCharger(
								12172, "batteryturbocharger.16.tier.02", "Medium Voltage Turbo Charger",2,"64A in /16A out, 120A/item, Disable to force Charge", 4).getStackForm(1L));

						ItemList.Battery_TurboCharger_4by4_HV.set(new GT_MetaTileEntity_TurboCharger(
								12173, "batteryturbocharger.16.tier.03", "High Voltage Turbo Charger",3,"64A in /16A out, 120A/item, Disable to force Charge", 4).getStackForm(1L));

						ItemList.Battery_TurboCharger_4by4_EV.set(new GT_MetaTileEntity_TurboCharger(
								12174, "batteryturbocharger.16.tier.04", "Extreme Voltage Turbo Charger",4,"64A in /16A out, 120A/item, Disable to force Charge", 4).getStackForm(1L));

						ItemList.Battery_TurboCharger_4by4_IV.set(new GT_MetaTileEntity_TurboCharger(
								12175, "batteryturbocharger.16.tier.05", "Insane Voltage Turbo Charger",5,"64A in /16A out, 120A/item, Disable to force Charge", 4).getStackForm(1L));

						ItemList.Battery_TurboCharger_4by4_LuV.set(new GT_MetaTileEntity_TurboCharger(
								12176, "batteryturbocharger.16.tier.06", "Ludicrous Voltage Turbo Charger",6,"64A in /16A out, 120A/item, Disable to force Charge", 4).getStackForm(1L));

						ItemList.Battery_TurboCharger_4by4_ZPM.set(new GT_MetaTileEntity_TurboCharger(
								12177, "batteryturbocharger.16.tier.07", "ZPM Voltage Turbo Charger",7,"64A in /16A out, 120A/item, Disable to force Charge", 4).getStackForm(1L));

						ItemList.Battery_TurboCharger_4by4_UV.set(new GT_MetaTileEntity_TurboCharger(
								12178, "batteryturbocharger.16.tier.08", "Ultimate Voltage Turbo Charger",8,"64A in /16A out, 120A/item, Disable to force Charge", 4).getStackForm(1L));

						ItemList.Battery_TurboCharger_4by4_UHV.set(new GT_MetaTileEntity_TurboCharger(
								12179, "batteryturbocharger.16.tier.09", "Highly Ultimate Voltage Turbo Charger",9,"64A in /16A out, 120A/item, Disable to force Charge", 4).getStackForm(1L));

						ItemList.Battery_TurboCharger_4by4_UEV.set(new GT_MetaTileEntity_TurboCharger(
								12180, "batteryturbocharger.16.tier.10", "Extremely Ultimate Turbo Charger",10,"64A in /16A out", 4).getStackForm(1L));

						ItemList.Battery_TurboCharger_4by4_UIV.set(new GT_MetaTileEntity_TurboCharger(
								12181, "batteryturbocharger.16.tier.11", "Insanely Ultimate Turbo Charger",11,"64A in /16A out", 4).getStackForm(1L));

						ItemList.Battery_TurboCharger_4by4_UMV.set(new GT_MetaTileEntity_TurboCharger(
								12182, "batteryturbocharger.16.tier.12", "Mega Ultimate Turbo Charger",12,"64A in /16A out", 4).getStackForm(1L));
						
							GT_ModHandler.addCraftingRecipe(ItemList.Battery_TurboCharger_4by4_ULV.get(1L),
									bitsd,
									new Object[]{"BTB", "CMC", "BXB",
											'T', ItemList.WetTransformer_LV_ULV,
											'M', ItemList.Battery_Charger_4by4_ULV,
											'B', ItemList.Battery_RE_ULV_Tantalum,
											'C', OrePrefixes.wireGt16.get(Materials.Lead),
											'X', OrePrefixes.circuit.get(Materials.Primitive)});
									GT_ModHandler.addCraftingRecipe(ItemList.Battery_TurboCharger_4by4_LV.get(1L),
									bitsd,
									new Object[]{"BTB", "CMC", "BXB",
											'T', ItemList.WetTransformer_MV_LV,
											'M', ItemList.Battery_Charger_4by4_LV,
											'B', ItemList.Battery_RE_LV_Lithium,
											'C', OrePrefixes.wireGt16.get(Materials.Tin),
											'X', OrePrefixes.circuit.get(Materials.Basic)});
									GT_ModHandler.addCraftingRecipe(ItemList.Battery_TurboCharger_4by4_MV.get(1L),
									bitsd,
									new Object[]{"BTB", "CMC", "BXB",
											'T', ItemList.WetTransformer_HV_MV,
											'M', ItemList.Battery_Charger_4by4_MV,
											'B', ItemList.Battery_RE_MV_Lithium,
											'C', OrePrefixes.wireGt16.get(Materials.Copper),
											'X', OrePrefixes.circuit.get(Materials.Good)});
									GT_ModHandler.addCraftingRecipe(ItemList.Battery_TurboCharger_4by4_HV.get(1L),
									bitsd,
									new Object[]{"BTB", "CMC", "BXB",
											'T', ItemList.WetTransformer_EV_HV,
											'M', ItemList.Battery_Charger_4by4_HV,
											'B', ItemList.Battery_RE_HV_Lithium,
											'C', OrePrefixes.wireGt16.get(Materials.Gold),
											'X', OrePrefixes.circuit.get(Materials.Advanced)});
									GT_ModHandler.addCraftingRecipe(ItemList.Battery_TurboCharger_4by4_EV.get(1L),
									bitsd,
									new Object[]{"BTB", "CMC", "BXB",
											'T', ItemList.WetTransformer_IV_EV,
											'M', ItemList.Battery_Charger_4by4_EV,
											'B', ItemList.BatteryHull_EV_Full,
											'C', OrePrefixes.wireGt16.get(Materials.Aluminium),
											'X', OrePrefixes.circuit.get(Materials.Data)});
									GT_ModHandler.addCraftingRecipe(ItemList.Battery_TurboCharger_4by4_IV.get(1L),
									bitsd,
									new Object[]{"BTB", "CMC", "BXB",
											'T', ItemList.WetTransformer_LuV_IV,
											'M', ItemList.Battery_Charger_4by4_IV,
											'B', ItemList.Energy_LapotronicOrb,
											'C', OrePrefixes.wireGt16.get(Materials.Tungsten),
											'X', OrePrefixes.circuit.get(Materials.Elite)});
									GT_ModHandler.addCraftingRecipe(ItemList.Battery_TurboCharger_4by4_IV.get(1L),
											bitsd,
											new Object[]{"BTB", "CMC", "BXB",
										    'T', ItemList.WetTransformer_LuV_IV,
											'M', ItemList.Battery_Charger_4by4_IV,
											'B', ItemList.BatteryHull_IV_Full,
											'C', OrePrefixes.wireGt16.get(Materials.Tungsten),
											'X', OrePrefixes.circuit.get(Materials.Elite)});
									GT_ModHandler.addCraftingRecipe(ItemList.Battery_TurboCharger_4by4_LuV.get(1L),
									bitsd,
									new Object[]{"BTB", "CMC", "BXB",
											'T', ItemList.WetTransformer_ZPM_LuV,
											'M', ItemList.Battery_Charger_4by4_LuV,
											'B', ItemList.Energy_LapotronicOrb2,
											'C', OrePrefixes.wireGt16.get(Materials.VanadiumGallium),
											'X', OrePrefixes.circuit.get(Materials.Master)});
									GT_ModHandler.addCraftingRecipe(ItemList.Battery_TurboCharger_4by4_LuV.get(1L),
											bitsd,
											new Object[]{"BTB", "CMC", "BXB",
													'T', ItemList.WetTransformer_ZPM_LuV,
													'M', ItemList.Battery_Charger_4by4_LuV,
													'B', ItemList.BatteryHull_LuV_Full,
													'C', OrePrefixes.wireGt16.get(Materials.VanadiumGallium),
													'X', OrePrefixes.circuit.get(Materials.Master)});
									GT_ModHandler.addCraftingRecipe(ItemList.Battery_TurboCharger_4by4_ZPM.get(1L),
									bitsd,
									new Object[]{"BTB", "CMC", "BXB",
											'T', ItemList.WetTransformer_UV_ZPM,
											'M', ItemList.Battery_Charger_4by4_ZPM,
											'B', ItemList.Energy_Module,
											'C', OrePrefixes.wireGt16.get(Materials.Naquadah),
											'X', OrePrefixes.circuit.get(Materials.Ultimate)});
									GT_ModHandler.addCraftingRecipe(ItemList.Battery_TurboCharger_4by4_ZPM.get(1L),
											bitsd,
											new Object[]{"BTB", "CMC", "BXB",
													'T', ItemList.WetTransformer_UV_ZPM,
													'M', ItemList.Battery_Charger_4by4_ZPM,
													'B', ItemList.BatteryHull_ZPM_Full,
													'C', OrePrefixes.wireGt16.get(Materials.Naquadah),
													'X', OrePrefixes.circuit.get(Materials.Ultimate)});
									GT_ModHandler.addCraftingRecipe(ItemList.Battery_TurboCharger_4by4_UV.get(1L),
									bitsd,
									new Object[]{"BTB", "CMC", "BXB",
											'T', ItemList.WetTransformer_UHV_UV,
											'M', ItemList.Battery_Charger_4by4_UV,
											'B', ItemList.Energy_Cluster,
											'C', OrePrefixes.wireGt16.get(Materials.NaquadahAlloy),
											'X', OrePrefixes.circuit.get(Materials.Superconductor)});
									GT_ModHandler.addCraftingRecipe(ItemList.Battery_TurboCharger_4by4_UV.get(1L),
											bitsd,
											new Object[]{"BTB", "CMC", "BXB",
													'T', ItemList.WetTransformer_UHV_UV,
													'M', ItemList.Battery_Charger_4by4_UV,
													'B', ItemList.BatteryHull_UV_Full,
													'C', OrePrefixes.wireGt16.get(Materials.NaquadahAlloy),
													'X', OrePrefixes.circuit.get(Materials.Superconductor)});
									GT_ModHandler.addCraftingRecipe(ItemList.Battery_TurboCharger_4by4_UHV.get(1L),
									bitsd,
									new Object[]{"BTB", "CMC", "BXB",
											'T', ItemList.WetTransformer_UEV_UHV,
											'M', ItemList.Battery_Charger_4by4_MAX,
											'B', ItemList.ZPM2,
											'C', OrePrefixes.wireGt16.get(Materials.Europium),
											'X', OrePrefixes.circuit.get(Materials.Infinite)});
									GT_ModHandler.addCraftingRecipe(ItemList.Battery_TurboCharger_4by4_UHV.get(1L),
											bitsd,
											new Object[]{"BTB", "CMC", "BXB",
													'T', ItemList.WetTransformer_UEV_UHV,
													'M', ItemList.Battery_Charger_4by4_MAX,
													'B', ItemList.BatteryHull_UHV_Full,
													'C', OrePrefixes.wireGt16.get(Materials.Europium),
													'X', OrePrefixes.circuit.get(Materials.Infinite)});
									GT_ModHandler.addCraftingRecipe(ItemList.Battery_TurboCharger_4by4_UEV.get(1L),
											bitsd,
											new Object[]{"BTB", "CMC", "BXB",
													'T', ItemList.WetTransformer_UIV_UEV,
													'M', ItemList.Battery_Charger_4by4_UEV,
													'B', ItemList.ZPM2,
													'C', OrePrefixes.wireGt16.get(Materials.Diamericiumtitanium),
													'X', OrePrefixes.circuit.get(Materials.Bio)});
									GT_ModHandler.addCraftingRecipe(ItemList.Battery_TurboCharger_4by4_UEV.get(1L),
													bitsd,
													new Object[]{"BTB", "CMC", "BXB",
															'T', ItemList.WetTransformer_UIV_UEV,
															'M', ItemList.Battery_Charger_4by4_UEV,
															'B', ItemList.BatteryHull_UEV_Full,
															'C', OrePrefixes.wireGt16.get(Materials.Diamericiumtitanium),
															'X', OrePrefixes.circuit.get(Materials.Bio)});
									GT_ModHandler.addCraftingRecipe(ItemList.Battery_TurboCharger_4by4_UIV.get(1L),
											bitsd,
											new Object[]{"BTB", "CMC", "BXB",
													'T', ItemList.WetTransformer_UMV_UIV,
													'M', ItemList.Battery_Charger_4by4_UIV,
													'B', ItemList.ZPM3,
													'C', OrePrefixes.wireGt16.get(Materials.Neutronium),
													'X', OrePrefixes.circuit.get(Materials.Bio)});
									GT_ModHandler.addCraftingRecipe(ItemList.Battery_TurboCharger_4by4_UIV.get(1L),
													bitsd,
													new Object[]{"BTB", "CMC", "BXB",
															'T', ItemList.WetTransformer_UMV_UIV,
															'M', ItemList.Battery_Charger_4by4_UIV,
															'B', ItemList.BatteryHull_UIV_Full,
															'C', OrePrefixes.wireGt16.get(Materials.Neutronium),
															'X', OrePrefixes.circuit.get(Materials.Bio)});
									
							//Ultra Transformer
									ItemList.Transformer_Ultra_HV_MV.set(new GT_MetaTileEntity_Ultra_Transformer(
											12200, "ultratransformer.tier.02", "Medium Voltage Ultra Transformer", 2,
											"HV -> MV (Use Soft Mallet to invert)").getStackForm(1L));
									ItemList.Transformer_Ultra_EV_HV.set(new GT_MetaTileEntity_Ultra_Transformer(
											12201, "ultratransformer.tier.03", "High Voltage Ultra Transformer", 3,
											"EV -> HV (Use Soft Mallet to invert)").getStackForm(1L));
									ItemList.Transformer_Ultra_IV_EV.set(new GT_MetaTileEntity_Ultra_Transformer(
											12202, "ultratransformer.tier.04", "Extreme Voltage Ultra Transformer", 4,
											"IV -> EV (Use Soft Mallet to invert)").getStackForm(1L));
									ItemList.Transformer_Ultra_LuV_IV.set(new GT_MetaTileEntity_Ultra_Transformer(
											12203, "ultratransformer.tier.05", "Insane Voltage Ultra Transformer", 5,
											"LuV -> IV (Use Soft Mallet to invert)").getStackForm(1L));
									ItemList.Transformer_Ultra_ZPM_LuV.set(new GT_MetaTileEntity_Ultra_Transformer(
											12204, "ultratransformer.tier.06", "Ludicrous Voltage Ultra Transformer", 6,
											"ZPM -> LuV (Use Soft Mallet to invert)").getStackForm(1L));
									ItemList.Transformer_Ultra_UV_ZPM.set(new GT_MetaTileEntity_Ultra_Transformer(
											12205, "ultratransformer.tier.07", "ZPM Voltage Ultra Transformer", 7,
											"UV -> ZPM (Use Soft Mallet to invert)").getStackForm(1L));
									ItemList.Transformer_Ultra_UHV_UV.set(new GT_MetaTileEntity_Ultra_Transformer(
											12206, "ultratransformer.tier.08", "Ultimate Voltage Ultra Transformer", 8,
											"UHV -> UV (Use Soft Mallet to invert)").getStackForm(1L));
									ItemList.Transformer_Ultra_UEV_UHV.set(new GT_MetaTileEntity_Ultra_Transformer(
											12207, "ultratransformer.tier.09", "Highly Ultimate Voltage Ultra Transformer", 9,
											"UEV -> UHV (Use Soft Mallet to invert)").getStackForm(1L));
									ItemList.Transformer_Ultra_UIV_UEV.set(new GT_MetaTileEntity_Ultra_Transformer(
											12208, "ultratransformer.tier.10", "Extreme Ultimate Voltage Ultra Transformer", 10,
											"UIV -> UEV (Use Soft Mallet to invert)").getStackForm(1L));
									ItemList.Transformer_Ultra_UMV_UIV.set(new GT_MetaTileEntity_Ultra_Transformer(
											12209, "ultratransformer.tier.11", "Insanely Ultimate Voltage Ultra Transformer", 11,
											"UMV -> UIV (Use Soft Mallet to invert)").getStackForm(1L));
									ItemList.Transformer_Ultra_UXV_UMV.set(new GT_MetaTileEntity_Ultra_Transformer(
											12210, "ultratransformer.tier.12", "Mega Ultimate Voltage Ultra Transformer", 12,
											"UXV -> UMV (Use Soft Mallet to invert)").getStackForm(1L));
									ItemList.Transformer_Ultra_OPV_UXV.set(new GT_MetaTileEntity_Ultra_Transformer(
											12211, "ultratransformer.tier.13", "Extended Mega Ultimate Voltage Ultra Transformer", 13,
											"OPV -> UXV (Use Soft Mallet to invert)").getStackForm(1L));
									ItemList.Transformer_Ultra_MAXV_OPV.set(new GT_MetaTileEntity_Ultra_Transformer(
											12212, "ultratransformer.tier.14", "Overpowered Voltage Ultra Transformer", 14,
											"MAX -> OPV (Use Soft Mallet to invert)").getStackForm(1L));
									
									GT_ModHandler.addCraftingRecipe(ItemList.Transformer_Ultra_HV_MV.get(1L),
											bitsd,
											new Object[]{"oCo", "oTo", "MOR",
													'o', OrePrefixes.wireGt04.get(Materials.SuperconductorMV),
													'O', OrePrefixes.wireGt16.get(Materials.SuperconductorHV),
													'C', OrePrefixes.circuit.get(Materials.Good),
													'R', OrePrefixes.rotor.get(Materials.Aluminium),
													'M', ItemList.Electric_Motor_MV,
													'T', ItemList.WetTransformer_HV_MV});
									GT_ModHandler.addCraftingRecipe(ItemList.Transformer_Ultra_EV_HV.get(1L),
											bitsd,
											new Object[]{"oCo", "oTo", "MOR",
													'o', OrePrefixes.wireGt04.get(Materials.SuperconductorHV),
													'O', OrePrefixes.wireGt16.get(Materials.SuperconductorEV),
													'C', OrePrefixes.circuit.get(Materials.Advanced),
													'R', OrePrefixes.rotor.get(Materials.StainlessSteel),
													'M', ItemList.Electric_Motor_HV,
													'T', ItemList.WetTransformer_EV_HV});
									GT_ModHandler.addCraftingRecipe(ItemList.Transformer_Ultra_IV_EV.get(1L),
											bitsd,
											new Object[]{"oCo", "oTo", "MOR",
													'o', OrePrefixes.wireGt04.get(Materials.SuperconductorEV),
													'O', OrePrefixes.wireGt16.get(Materials.SuperconductorIV),
													'C', OrePrefixes.circuit.get(Materials.Data),
													'R', OrePrefixes.rotor.get(Materials.Titanium),
													'M', ItemList.Electric_Motor_EV,
													'T', ItemList.WetTransformer_IV_EV});
									GT_ModHandler.addCraftingRecipe(ItemList.Transformer_Ultra_LuV_IV.get(1L),
											bitsd,
											new Object[]{"oCo", "oTo", "MOR",
													'o', OrePrefixes.wireGt04.get(Materials.SuperconductorIV),
													'O', OrePrefixes.wireGt16.get(Materials.SuperconductorLuV),
													'C', OrePrefixes.circuit.get(Materials.Elite),
													'R', OrePrefixes.rotor.get(Materials.TungstenSteel),
													'M', ItemList.Electric_Motor_IV,
													'T', ItemList.WetTransformer_LuV_IV});
									GT_ModHandler.addCraftingRecipe(ItemList.Transformer_Ultra_ZPM_LuV.get(1L),
											bitsd,
											new Object[]{"oCo", "oTo", "MOR",
													'o', OrePrefixes.wireGt04.get(Materials.SuperconductorLuV),
													'O', OrePrefixes.wireGt16.get(Materials.SuperconductorZPM),
													'C', OrePrefixes.circuit.get(Materials.Master),
													'R', OrePrefixes.rotor.get(Materials.Chrome),
													'M', ItemList.Electric_Motor_LuV,
													'T', ItemList.WetTransformer_ZPM_LuV});
									GT_ModHandler.addCraftingRecipe(ItemList.Transformer_Ultra_UV_ZPM.get(1L),
											bitsd,
											new Object[]{"oCo", "oTo", "MOR",
													'o', OrePrefixes.wireGt04.get(Materials.SuperconductorZPM),
													'O', OrePrefixes.wireGt16.get(Materials.SuperconductorUV),
													'C', OrePrefixes.circuit.get(Materials.Ultimate),
													'R', OrePrefixes.rotor.get(Materials.Iridium),
													'M', ItemList.Electric_Motor_ZPM,
													'T', ItemList.WetTransformer_UV_ZPM});
									GT_ModHandler.addCraftingRecipe(ItemList.Transformer_Ultra_UHV_UV.get(1L),
											bitsd,
											new Object[]{"oCo", "oTo", "MOR",
													'o', OrePrefixes.wireGt04.get(Materials.SuperconductorUV),
													'O', OrePrefixes.wireGt16.get(Materials.Superconductor),
													'C', OrePrefixes.circuit.get(Materials.Superconductor),
													'R', OrePrefixes.rotor.get(Materials.Osmium),
													'M', ItemList.Electric_Motor_UV,
													'T', ItemList.WetTransformer_UHV_UV});
									GT_ModHandler.addCraftingRecipe(ItemList.Transformer_Ultra_UEV_UHV.get(1L),
											bitsd,
											new Object[]{"oCo", "oTo", "MOR",
													'o', OrePrefixes.wireGt04.get(Materials.Superconductor),
													'O', OrePrefixes.wireGt16.get(Materials.SuperconductorUEV),
													'C', OrePrefixes.circuit.get(Materials.Infinite),
													'R', OrePrefixes.rotor.get(Materials.NaquadahAlloy),
													'M', ItemList.Electric_Motor_UHV,
													'T', ItemList.WetTransformer_UEV_UHV});
									GT_ModHandler.addCraftingRecipe(ItemList.Transformer_Ultra_UIV_UEV.get(1L),
											bitsd,
											new Object[]{"oCo", "oTo", "MOR",
													'o', OrePrefixes.wireGt04.get(Materials.SuperconductorUEV),
													'O', OrePrefixes.wireGt16.get(Materials.Neutronium),
													'C', OrePrefixes.circuit.get(Materials.Bio),
													'R', OrePrefixes.rotor.get(Materials.Neutronium),
													'M', ItemList.Electric_Motor_UEV,
													'T', ItemList.WetTransformer_UIV_UEV});
		
									// ===================================================================================================
									// World Accelerator Lv-UV
									// ===================================================================================================
									if(Loader.isModLoaded("miscutils")) {
									}else {
									ItemList.AcceleratorLV.set(new GT_MetaTileEntity_WorldAccelerator(
											12220, "basicmachine.accelerator.tier.01", "Basic World Accelerator", 1).getStackForm(1L));
									ItemList.AcceleratorMV.set(new GT_MetaTileEntity_WorldAccelerator(
											12221, "basicmachine.accelerator.tier.02", "Advanced World Accelerator", 2).getStackForm(1L));
									ItemList.AcceleratorHV.set(new GT_MetaTileEntity_WorldAccelerator(
											12222, "basicmachine.accelerator.tier.03", "Advanced World Accelerator II", 3).getStackForm(1L));
									ItemList.AcceleratorEV.set(new GT_MetaTileEntity_WorldAccelerator(
											12223, "basicmachine.accelerator.tier.04", "Advanced World Accelerator III", 4).getStackForm(1L));
									ItemList.AcceleratorIV.set(new GT_MetaTileEntity_WorldAccelerator(
											12224, "basicmachine.accelerator.tier.05", "Advanced World Accelerator IV", 5).getStackForm(1L));
									ItemList.AcceleratorLuV.set(new GT_MetaTileEntity_WorldAccelerator(
											12225, "basicmachine.accelerator.tier.06", "Elite World Accelerator", 6).getStackForm(1L));
									ItemList.AcceleratorZPM.set(new GT_MetaTileEntity_WorldAccelerator(
											12226, "basicmachine.accelerator.tier.07", "Elite World Accelerator II", 7).getStackForm(1L));
									ItemList.AcceleratorUV.set(new GT_MetaTileEntity_WorldAccelerator(
											12227, "basicmachine.accelerator.tier.08", "Ultimate Time Anomaly", 8).getStackForm(1L));
									//Not adding either...

									GT_ModHandler.addCraftingRecipe(ItemList.AcceleratorLV.get(1L),
											bitsd,
											new Object[]{"RMR", "PBC", "IMI",
													'R', ItemList.Robot_Arm_LV,
													'M', ItemList.Electric_Motor_LV,
													'P', ItemList.Electric_Pump_LV,
													'B', ItemList.Hull_LV,
													'C', ItemList.Conveyor_Module_LV,
													'I', ItemList.Electric_Piston_LV});

									GT_ModHandler.addCraftingRecipe(ItemList.AcceleratorMV.get(1L),
											bitsd,
											new Object[]{"RMR", "PBC", "IMI",
													'R', ItemList.Robot_Arm_MV,
													'M', ItemList.Electric_Motor_MV,
													'P', ItemList.Electric_Pump_MV,
													'B', ItemList.Hull_MV,
													'C', ItemList.Conveyor_Module_MV,
													'I', ItemList.Electric_Piston_MV});

									GT_ModHandler.addCraftingRecipe(ItemList.AcceleratorHV.get(1L),
											bitsd,
											new Object[]{"RMR", "PBC", "IMI",
													'R', ItemList.Robot_Arm_HV,
													'M', ItemList.Electric_Motor_HV,
													'P', ItemList.Electric_Pump_HV,
													'B', ItemList.Hull_HV,
													'C', ItemList.Conveyor_Module_HV,
													'I', ItemList.Electric_Piston_HV});

									GT_ModHandler.addCraftingRecipe(ItemList.AcceleratorEV.get(1L),
											bitsd,
											new Object[]{"RMR", "PBC", "IMI",
													'R', ItemList.Robot_Arm_EV,
													'M', ItemList.Electric_Motor_EV,
													'P', ItemList.Electric_Pump_EV,
													'B', ItemList.Hull_EV,
													'C', ItemList.Conveyor_Module_EV,
													'I', ItemList.Electric_Piston_EV});

									GT_ModHandler.addCraftingRecipe(ItemList.AcceleratorIV.get(1L),
											bitsd,
											new Object[]{"RMR", "PBC", "IMI",
													'R', ItemList.Robot_Arm_IV,
													'M', ItemList.Electric_Motor_IV,
													'P', ItemList.Electric_Pump_IV,
													'B', ItemList.Hull_IV,
													'C', ItemList.Conveyor_Module_IV,
													'I', ItemList.Electric_Piston_IV});

									GT_ModHandler.addCraftingRecipe(ItemList.AcceleratorLuV.get(1L),
											bitsd,
											new Object[]{"RMR", "PBC", "IMI",
													'R', ItemList.Robot_Arm_LuV,
													'M', ItemList.Electric_Motor_LuV,
													'P', ItemList.Electric_Pump_LuV,
													'B', ItemList.Hull_LuV,
													'C', ItemList.Conveyor_Module_LuV,
													'I', ItemList.Electric_Piston_LuV});

									GT_ModHandler.addCraftingRecipe(ItemList.AcceleratorZPM.get(1L),
											bitsd,
											new Object[]{"RMR", "PBC", "IMI",
													'R', ItemList.Robot_Arm_ZPM,
													'M', ItemList.Electric_Motor_ZPM,
													'P', ItemList.Electric_Pump_ZPM,
													'B', ItemList.Hull_ZPM,
													'C', ItemList.Conveyor_Module_ZPM,
													'I', ItemList.Electric_Piston_ZPM});

									GT_ModHandler.addCraftingRecipe(ItemList.AcceleratorUV.get(1L),
											bitsd,
											new Object[]{"RMR", "PBC", "IMI",
													'R', ItemList.Robot_Arm_UV,
													'M', ItemList.Electric_Motor_UV,
													'P', ItemList.Electric_Pump_UV,
													'B', ItemList.Hull_UV,
													'C', ItemList.Conveyor_Module_UV,
													'I', ItemList.Electric_Piston_UV});
									}
    }

    private static void makeWires(Materials aMaterial, int aStartID, long aLossInsulated, long aLoss, long aAmperage, long aVoltage, boolean aInsulatable, boolean aAutoInsulated) {
    	String name = GT_LanguageManager.i18nPlaceholder ? "%material" : aMaterial.mDefaultLocalName;
        GT_OreDictUnificator.registerOre(OrePrefixes.wireGt01, aMaterial, new GT_MetaPipeEntity_Cable(aStartID + 0, aTextWire1 + aMaterial.mName.toLowerCase() + ".01", "1x " + name + aTextWire2, 0.125F, aMaterial, aLoss, 1L * aAmperage, aVoltage, aBoolConst_0, !aAutoInsulated).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.wireGt02, aMaterial, new GT_MetaPipeEntity_Cable(aStartID + 1, aTextWire1 + aMaterial.mName.toLowerCase() + ".02", "2x " + name + aTextWire2, 0.25F, aMaterial, aLoss, 2L * aAmperage, aVoltage, aBoolConst_0, !aAutoInsulated).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.wireGt04, aMaterial, new GT_MetaPipeEntity_Cable(aStartID + 2, aTextWire1 + aMaterial.mName.toLowerCase() + ".04", "4x " + name + aTextWire2, 0.375F, aMaterial, aLoss, 4L * aAmperage, aVoltage, aBoolConst_0, !aAutoInsulated).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.wireGt08, aMaterial, new GT_MetaPipeEntity_Cable(aStartID + 3, aTextWire1 + aMaterial.mName.toLowerCase() + ".08", "8x " + name + aTextWire2, 0.5F, aMaterial, aLoss, 8L * aAmperage, aVoltage, aBoolConst_0, !aAutoInsulated).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.wireGt12, aMaterial, new GT_MetaPipeEntity_Cable(aStartID + 4, aTextWire1 + aMaterial.mName.toLowerCase() + ".12", "12x " + name + aTextWire2, 0.625F, aMaterial, aLoss, 12L * aAmperage, aVoltage, aBoolConst_0, !aAutoInsulated).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.wireGt16, aMaterial, new GT_MetaPipeEntity_Cable(aStartID + 5, aTextWire1 + aMaterial.mName.toLowerCase() + ".16", "16x " + name + aTextWire2, 0.75F, aMaterial, aLoss, 16L * aAmperage, aVoltage, aBoolConst_0, !aAutoInsulated).getStackForm(1L));
        if (aInsulatable) {
            GT_OreDictUnificator.registerOre(OrePrefixes.cableGt01, aMaterial, new GT_MetaPipeEntity_Cable(aStartID + 6, aTextCable1 + aMaterial.mName.toLowerCase() + ".01", "1x " + name + aTextCable2, 0.25F, aMaterial, aLossInsulated, 1L * aAmperage, aVoltage, true, aBoolConst_0).getStackForm(1L));
            GT_OreDictUnificator.registerOre(OrePrefixes.cableGt02, aMaterial, new GT_MetaPipeEntity_Cable(aStartID + 7, aTextCable1 + aMaterial.mName.toLowerCase() + ".02", "2x " + name + aTextCable2, 0.375F, aMaterial, aLossInsulated, 2L * aAmperage, aVoltage, true, aBoolConst_0).getStackForm(1L));
            GT_OreDictUnificator.registerOre(OrePrefixes.cableGt04, aMaterial, new GT_MetaPipeEntity_Cable(aStartID + 8, aTextCable1 + aMaterial.mName.toLowerCase() + ".04", "4x " + name + aTextCable2, 0.5F, aMaterial, aLossInsulated, 4L * aAmperage, aVoltage, true, aBoolConst_0).getStackForm(1L));
            GT_OreDictUnificator.registerOre(OrePrefixes.cableGt08, aMaterial, new GT_MetaPipeEntity_Cable(aStartID + 9, aTextCable1 + aMaterial.mName.toLowerCase() + ".08", "8x " + name + aTextCable2, 0.625F, aMaterial, aLossInsulated, 8L * aAmperage, aVoltage, true, aBoolConst_0).getStackForm(1L));
            GT_OreDictUnificator.registerOre(OrePrefixes.cableGt12, aMaterial, new GT_MetaPipeEntity_Cable(aStartID + 10, aTextCable1 + aMaterial.mName.toLowerCase() + ".12", "12x " + name + aTextCable2, 0.75F, aMaterial, aLossInsulated, 12L * aAmperage, aVoltage, true, aBoolConst_0).getStackForm(1L));
            GT_OreDictUnificator.registerOre(OrePrefixes.cableGt16, aMaterial, new GT_MetaPipeEntity_Cable(aStartID + 11, aTextCable1 + aMaterial.mName.toLowerCase() + ".16", "16x " + name + aTextCable2, 0.875F, aMaterial, aLossInsulated, 16L * aAmperage, aVoltage, true, aBoolConst_0).getStackForm(1L));
        }
    }

    public void run() {
        GT_Log.out.println("GT_Mod: Registering MetaTileEntities.");
        run1();
        run2();
        run3();
        run4();
    }

    private static void generateItemPipes(Materials aMaterial, String name, int startID, int baseInvSlots){
    	generateItemPipes(aMaterial, name, GT_LanguageManager.i18nPlaceholder ? "%material" : aMaterial.mDefaultLocalName, startID, baseInvSlots);
    }

    private static void generateItemPipes(Materials aMaterial, String name, String displayName, int startID, int baseInvSlots){
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeMedium.get(aMaterial), 			new GT_MetaPipeEntity_Item(startID, 	"GT_Pipe_" + name, 						displayName + " Item Pipe", 						0.50F, aMaterial, baseInvSlots, 	32768   / baseInvSlots, aBoolConst_0).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeLarge.get(aMaterial), 				new GT_MetaPipeEntity_Item(startID + 1, "GT_Pipe_" + name + "_Large", 			"Large " + displayName + " Item Pipe", 				0.75F, aMaterial, baseInvSlots * 2, 16384   / baseInvSlots, aBoolConst_0).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeHuge.get(aMaterial), 				new GT_MetaPipeEntity_Item(startID + 2, "GT_Pipe_" + name + "_Huge", 			"Huge " + displayName +" Item Pipe", 				1.00F, aMaterial, baseInvSlots * 4, 8192    / baseInvSlots, aBoolConst_0).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeRestrictiveMedium.get(aMaterial), 	new GT_MetaPipeEntity_Item(startID + 3, "GT_Pipe_Restrictive_" + name, 			"Restrictive " + displayName + " Item Pipe", 		0.50F, aMaterial, baseInvSlots, 	3276800 / baseInvSlots, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeRestrictiveLarge.get(aMaterial), 	new GT_MetaPipeEntity_Item(startID + 4, "GT_Pipe_Restrictive_" + name + "_Large","Large Restrictive " + displayName + " Item Pipe", 	0.75F, aMaterial, baseInvSlots * 2, 1638400 / baseInvSlots, true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeRestrictiveHuge.get(aMaterial), 	new GT_MetaPipeEntity_Item(startID + 5, "GT_Pipe_Restrictive_" + name + "_Huge", "Huge Restrictive " + displayName + " Item Pipe", 	0.875F, aMaterial, baseInvSlots * 4, 819200  / baseInvSlots, true).getStackForm(1L));

    }

    private static void generateFluidPipes(Materials aMaterial, String name, int startID, int baseCapacity, int heatCapacity, boolean gasProof){
    	generateFluidPipes(aMaterial, name, GT_LanguageManager.i18nPlaceholder ? "%material" : aMaterial.mDefaultLocalName, startID, baseCapacity, heatCapacity, gasProof);
    }

    private static void generateFluidPipes(Materials aMaterial, String name, String displayName, int startID, int baseCapacity, int heatCapacity, boolean gasProof){
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeTiny.get(aMaterial), 	new GT_MetaPipeEntity_Fluid(startID, 		"GT_Pipe_" + name + "_Tiny", 	"Tiny " + displayName + " Fluid Pipe", 	0.25F, 	aMaterial, baseCapacity / 6, 	heatCapacity, gasProof).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeSmall.get(aMaterial), 	new GT_MetaPipeEntity_Fluid(startID + 1, 	"GT_Pipe_" + name + "_Small", 	"Small " + displayName + " Fluid Pipe", 0.375F, aMaterial, baseCapacity / 3, 	heatCapacity, gasProof).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeMedium.get(aMaterial), new GT_MetaPipeEntity_Fluid(startID + 2, 	"GT_Pipe_" + name, 				displayName + " Fluid Pipe", 			0.5F, 	aMaterial, baseCapacity, 		heatCapacity, gasProof).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeLarge.get(aMaterial), 	new GT_MetaPipeEntity_Fluid(startID + 3, 	"GT_Pipe_" + name + "_Large", 	"Large " + displayName + " Fluid Pipe", 0.75F, 	aMaterial, baseCapacity * 2, 	heatCapacity, gasProof).getStackForm(1L));
        if (aMaterial != Materials.WroughtIron) {
            GT_OreDictUnificator.registerOre(OrePrefixes.pipeHuge.get(aMaterial), new GT_MetaPipeEntity_Fluid(startID + 4, "GT_Pipe_" + name + "_Huge", "Huge " + displayName + " Fluid Pipe", 0.875F, aMaterial, baseCapacity * 4, heatCapacity, gasProof).getStackForm(1L));
        }
    }

    private static void generateFluidMultiPipes(Materials aMaterial, String name, String displayName, int startID, int baseCapacity, int heatCapacity, boolean gasProof){
    	GT_OreDictUnificator.registerOre(OrePrefixes.pipeQuadruple.get(aMaterial), 	new GT_MetaPipeEntity_Fluid(startID, 		"GT_Pipe_" + name + "_Quadruple", 	"Quadruple " + displayName + " Fluid Pipe", 	1.0F, 	aMaterial, baseCapacity, 	heatCapacity, gasProof, 4).getStackForm(1L));
    	GT_OreDictUnificator.registerOre(OrePrefixes.pipeNonuple.get(aMaterial), 	new GT_MetaPipeEntity_Fluid(startID + 1,	"GT_Pipe_" + name + "_Nonuple", 	"Nonuple "	 + displayName + " Fluid Pipe", 	1.0F, 	aMaterial, baseCapacity / 3, 	heatCapacity, gasProof, 9).getStackForm(1L));
    }
}
