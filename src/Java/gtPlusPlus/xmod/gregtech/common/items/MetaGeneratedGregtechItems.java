package gtPlusPlus.xmod.gregtech.common.items;

import static gtPlusPlus.core.util.Utils.getTcAspectStack;

import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;

import gregtech.api.GregTech_API;
import gregtech.api.enums.*;
import gregtech.api.interfaces.ITexture;
import gregtech.api.objects.*;
import gregtech.api.util.*;
import gregtech.common.covers.GT_Cover_Arm;
import gregtech.common.covers.GT_Cover_Conveyor;
import gregtech.common.covers.GT_Cover_Pump;
import gregtech.common.items.behaviors.Behaviour_DataOrb;
import gregtech.common.items.behaviors.Behaviour_DataStick;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.handler.OldCircuitHandler;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.data.StringUtils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
import gtPlusPlus.xmod.gregtech.api.items.Gregtech_MetaItem_X32;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.covers.GTPP_Cover_Overflow;
import gtPlusPlus.xmod.gregtech.common.covers.GTPP_Cover_Overflow2;
import gtPlusPlus.xmod.gregtech.common.covers.GTPP_Cover_ToggleVisual;

public class MetaGeneratedGregtechItems extends Gregtech_MetaItem_X32 {

	public final static MetaGeneratedGregtechItems INSTANCE;

	static {
		INSTANCE = new MetaGeneratedGregtechItems();
	}

	public MetaGeneratedGregtechItems() {
		super("MU-metaitem.01", new OrePrefixes[]{null});		
	}

	public void generateMetaItems() {
		int tLastID = 0;		

		registerCustomCircuits();
		OldCircuitHandler.addCircuitItems();

		if (!CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK){
			Logger.INFO("Gregtech 5.09 not found, using fallback components. (I like how I have to add compat to something I added first and had stolen.)");
			GregtechItemList.Electric_Pump_LuV.set(this.addItem(tLastID = 0, "Electric Pump (LuV)", "163920 L/sec (as Cover)", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 1L), getTcAspectStack(TC_Aspects.MACHINA, 1L), getTcAspectStack(TC_Aspects.ITER, 1L), getTcAspectStack(TC_Aspects.AQUA, 1L)}));
			GregtechItemList.Electric_Pump_ZPM.set(this.addItem(tLastID = 1, "Electric Pump (ZPM)", "655680 L/sec (as Cover)", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 2L), getTcAspectStack(TC_Aspects.MACHINA, 2L), getTcAspectStack(TC_Aspects.ITER, 2L), getTcAspectStack(TC_Aspects.AQUA, 2L)}));
			GregtechItemList.Electric_Pump_UV.set(this.addItem(tLastID = 2, "Electric Pump (UV)", "2622720 L/sec (as Cover)", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 4L), getTcAspectStack(TC_Aspects.MACHINA, 4L), getTcAspectStack(TC_Aspects.ITER, 4L), getTcAspectStack(TC_Aspects.AQUA, 4L)}));
			GregtechItemList.Electric_Pump_MAX.set(this.addItem(tLastID = 3, "Electric Pump (MAX)", "10490880 L/sec (as Cover)", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 8L), getTcAspectStack(TC_Aspects.MACHINA, 8L), getTcAspectStack(TC_Aspects.ITER, 8L), getTcAspectStack(TC_Aspects.AQUA, 8L)}));
			GregTech_API.registerCover(GregtechItemList.Electric_Pump_LuV.get(1L), new GT_MultiTexture(new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[5][0], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_PUMP)}), new GT_Cover_Pump(8196));
			GregTech_API.registerCover(GregtechItemList.Electric_Pump_ZPM.get(1L), new GT_MultiTexture(new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[6][0], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_PUMP)}), new GT_Cover_Pump(32768));
			GregTech_API.registerCover(GregtechItemList.Electric_Pump_UV.get(1L), new GT_MultiTexture(new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[7][0], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_PUMP)}), new GT_Cover_Pump(131072));
			GregTech_API.registerCover(GregtechItemList.Electric_Pump_MAX.get(1L), new GT_MultiTexture(new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[8][0], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_PUMP)}), new GT_Cover_Pump(524288));
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Electric_Pump_LuV.get(1L), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"SXO", "dPw", "OMW", Character.valueOf('M'), GregtechItemList.Electric_Motor_LuV, Character.valueOf('O'), OrePrefixes.ring.get(Materials.Rubber), Character.valueOf('X'), OrePrefixes.rotor.get(Materials.Tin), Character.valueOf('S'), OrePrefixes.screw.get(Materials.Tin), Character.valueOf('W'), OrePrefixes.cableGt01.get(Materials.Tin), Character.valueOf('P'), OrePrefixes.pipeMedium.get(Materials.Bronze)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Electric_Pump_ZPM.get(1L), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"SXO", "dPw", "OMW", Character.valueOf('M'), GregtechItemList.Electric_Motor_ZPM, Character.valueOf('O'), OrePrefixes.ring.get(Materials.Rubber), Character.valueOf('X'), OrePrefixes.rotor.get(Materials.Bronze), Character.valueOf('S'), OrePrefixes.screw.get(Materials.Bronze), Character.valueOf('W'), OrePrefixes.cableGt01.get(Materials.AnyCopper), Character.valueOf('P'), OrePrefixes.pipeMedium.get(Materials.Steel)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Electric_Pump_UV.get(1L), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"SXO", "dPw", "OMW", Character.valueOf('M'), GregtechItemList.Electric_Motor_UV, Character.valueOf('O'), OrePrefixes.ring.get(Materials.Rubber), Character.valueOf('X'), OrePrefixes.rotor.get(Materials.Steel), Character.valueOf('S'), OrePrefixes.screw.get(Materials.Steel), Character.valueOf('W'), OrePrefixes.cableGt01.get(Materials.Gold), Character.valueOf('P'), OrePrefixes.pipeMedium.get(Materials.StainlessSteel)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Electric_Pump_MAX.get(1L), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"SXO", "dPw", "OMW", Character.valueOf('M'), GregtechItemList.Electric_Motor_MAX, Character.valueOf('O'), OrePrefixes.ring.get(Materials.Rubber), Character.valueOf('X'), OrePrefixes.rotor.get(Materials.StainlessSteel), Character.valueOf('S'), OrePrefixes.screw.get(Materials.StainlessSteel), Character.valueOf('W'), OrePrefixes.cableGt01.get(Materials.Aluminium), Character.valueOf('P'), OrePrefixes.pipeMedium.get(Materials.Titanium)});
			tLastID = 4;
			GregtechItemList.Electric_Motor_LuV.set(this.addItem(tLastID = 4, "Electric Motor (LuV)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 1L), getTcAspectStack(TC_Aspects.MACHINA, 1L), getTcAspectStack(TC_Aspects.MOTUS, 1L)}));
			GregtechItemList.Electric_Motor_ZPM.set(this.addItem(tLastID = 5, "Electric Motor (ZPM)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 2L), getTcAspectStack(TC_Aspects.MACHINA, 2L), getTcAspectStack(TC_Aspects.MOTUS, 2L)}));
			GregtechItemList.Electric_Motor_UV.set(this.addItem(tLastID = 6, "Electric Motor (UV)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 4L), getTcAspectStack(TC_Aspects.MACHINA, 4L), getTcAspectStack(TC_Aspects.MOTUS, 4L)}));
			GregtechItemList.Electric_Motor_MAX.set(this.addItem(tLastID = 7, "Electric Motor (MAX)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 8L), getTcAspectStack(TC_Aspects.MACHINA, 8L), getTcAspectStack(TC_Aspects.MOTUS, 8L)}));
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Electric_Motor_LuV.get(1L), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"CWR", "WIW", "RWC", Character.valueOf('I'), OrePrefixes.stick.get(Materials.IronMagnetic), Character.valueOf('R'), OrePrefixes.stick.get(Materials.AnyIron), Character.valueOf('W'), OrePrefixes.wireGt01.get(Materials.AnyCopper), Character.valueOf('C'), OrePrefixes.cableGt01.get(Materials.Tin)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Electric_Motor_ZPM.get(1L), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"CWR", "WIW", "RWC", Character.valueOf('I'), OrePrefixes.stick.get(Materials.SteelMagnetic), Character.valueOf('R'), OrePrefixes.stick.get(Materials.Aluminium), Character.valueOf('W'), OrePrefixes.wireGt01.get(Materials.Electrum), Character.valueOf('C'), OrePrefixes.cableGt01.get(Materials.Silver)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Electric_Motor_UV.get(1L), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"CWR", "WIW", "RWC", Character.valueOf('I'), OrePrefixes.stick.get(Materials.SteelMagnetic), Character.valueOf('R'), OrePrefixes.stick.get(Materials.StainlessSteel), Character.valueOf('W'), OrePrefixes.wireGt02.get(Materials.Cupronickel), Character.valueOf('C'), OrePrefixes.cableGt01.get(Materials.Gold)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Electric_Motor_MAX.get(1L), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"CWR", "WIW", "RWC", Character.valueOf('I'), OrePrefixes.stick.get(Materials.NeodymiumMagnetic), Character.valueOf('R'), OrePrefixes.stick.get(Materials.Titanium), Character.valueOf('W'), OrePrefixes.wireGt02.get(Materials.TungstenSteel), Character.valueOf('C'), OrePrefixes.cableGt01.get(Materials.Nichrome)});

			tLastID = 8;
			GregtechItemList.Conveyor_Module_LuV.set(this.addItem(tLastID = 8, "Conveyor Module (LuV)", "1 Stack every 20 secs (as Cover)", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 1L), getTcAspectStack(TC_Aspects.MACHINA, 1L), getTcAspectStack(TC_Aspects.ITER, 1L)}));
			GregtechItemList.Conveyor_Module_ZPM.set(this.addItem(tLastID = 9, "Conveyor Module (ZPM)", "1 Stack every 5 secs (as Cover)", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 2L), getTcAspectStack(TC_Aspects.MACHINA, 2L), getTcAspectStack(TC_Aspects.ITER, 2L)}));
			GregtechItemList.Conveyor_Module_UV.set(this.addItem(tLastID = 10, "Conveyor Module (UV)", "1 Stack every 1 sec (as Cover)", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 4L), getTcAspectStack(TC_Aspects.MACHINA, 4L), getTcAspectStack(TC_Aspects.ITER, 4L)}));
			GregtechItemList.Conveyor_Module_MAX.set(this.addItem(tLastID = 11, "Conveyor Module (MAX)", "1 Stack every 1/5 sec (as Cover)", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 8L), getTcAspectStack(TC_Aspects.MACHINA, 8L), getTcAspectStack(TC_Aspects.ITER, 8L)}));
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Conveyor_Module_LuV.get(1L), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"RRR", "MCM", "RRR", Character.valueOf('M'), GregtechItemList.Electric_Motor_LuV, Character.valueOf('C'), OrePrefixes.cableGt01.get(Materials.Tin), Character.valueOf('R'), OrePrefixes.plate.get(Materials.Rubber)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Conveyor_Module_ZPM.get(1L), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"RRR", "MCM", "RRR", Character.valueOf('M'), GregtechItemList.Electric_Motor_ZPM, Character.valueOf('C'), OrePrefixes.cableGt01.get(Materials.AnyCopper), Character.valueOf('R'), OrePrefixes.plate.get(Materials.Rubber)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Conveyor_Module_UV.get(1L), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"RRR", "MCM", "RRR", Character.valueOf('M'), GregtechItemList.Electric_Motor_UV, Character.valueOf('C'), OrePrefixes.cableGt01.get(Materials.Gold), Character.valueOf('R'), OrePrefixes.plate.get(Materials.Rubber)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Conveyor_Module_MAX.get(1L), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"RRR", "MCM", "RRR", Character.valueOf('M'), GregtechItemList.Electric_Motor_MAX, Character.valueOf('C'), OrePrefixes.cableGt01.get(Materials.Aluminium), Character.valueOf('R'), OrePrefixes.plate.get(Materials.Rubber)});
			GregTech_API.registerCover(GregtechItemList.Conveyor_Module_LuV.get(1L), new GT_MultiTexture(new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[1][0], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_CONVEYOR)}), new GT_Cover_Conveyor(400));
			GregTech_API.registerCover(GregtechItemList.Conveyor_Module_ZPM.get(1L), new GT_MultiTexture(new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[2][0], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_CONVEYOR)}), new GT_Cover_Conveyor(100));
			GregTech_API.registerCover(GregtechItemList.Conveyor_Module_UV.get(1L), new GT_MultiTexture(new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[3][0], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_CONVEYOR)}), new GT_Cover_Conveyor(20));
			GregTech_API.registerCover(GregtechItemList.Conveyor_Module_MAX.get(1L), new GT_MultiTexture(new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[4][0], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_CONVEYOR)}), new GT_Cover_Conveyor(4));
			tLastID = 12;
			GregtechItemList.Electric_Piston_LuV.set(this.addItem(tLastID = 12, "Electric Piston (LuV)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 1L), getTcAspectStack(TC_Aspects.MACHINA, 2L), getTcAspectStack(TC_Aspects.MOTUS, 1L)}));
			GregtechItemList.Electric_Piston_ZPM.set(this.addItem(tLastID = 13, "Electric Piston (ZPM)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 2L), getTcAspectStack(TC_Aspects.MACHINA, 4L), getTcAspectStack(TC_Aspects.MOTUS, 2L)}));
			GregtechItemList.Electric_Piston_UV.set(this.addItem(tLastID = 14, "Electric Piston (UV)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 4L), getTcAspectStack(TC_Aspects.MACHINA, 8L), getTcAspectStack(TC_Aspects.MOTUS, 4L)}));
			GregtechItemList.Electric_Piston_MAX.set(this.addItem(tLastID = 15, "Electric Piston (MAX)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 8L), getTcAspectStack(TC_Aspects.MACHINA, 16L), getTcAspectStack(TC_Aspects.MOTUS, 8L)}));
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Electric_Piston_LuV.get(1L), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"PPP", "CSS", "CMG", Character.valueOf('P'), OrePrefixes.plate.get(Materials.Steel), Character.valueOf('S'), OrePrefixes.stick.get(Materials.Steel), Character.valueOf('G'), OrePrefixes.gearGtSmall.get(Materials.Steel), Character.valueOf('M'), GregtechItemList.Electric_Motor_LuV, Character.valueOf('C'), OrePrefixes.cableGt01.get(Materials.Tin)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Electric_Piston_ZPM.get(1L), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"PPP", "CSS", "CMG", Character.valueOf('P'), OrePrefixes.plate.get(Materials.Aluminium), Character.valueOf('S'), OrePrefixes.stick.get(Materials.Aluminium), Character.valueOf('G'), OrePrefixes.gearGtSmall.get(Materials.Aluminium), Character.valueOf('M'), GregtechItemList.Electric_Motor_ZPM, Character.valueOf('C'), OrePrefixes.cableGt01.get(Materials.AnyCopper)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Electric_Piston_UV.get(1L), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"PPP", "CSS", "CMG", Character.valueOf('P'), OrePrefixes.plate.get(Materials.StainlessSteel), Character.valueOf('S'), OrePrefixes.stick.get(Materials.StainlessSteel), Character.valueOf('G'), OrePrefixes.gearGtSmall.get(Materials.StainlessSteel), Character.valueOf('M'), GregtechItemList.Electric_Motor_UV, Character.valueOf('C'), OrePrefixes.cableGt01.get(Materials.Gold)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Electric_Piston_MAX.get(1L), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"PPP", "CSS", "CMG", Character.valueOf('P'), OrePrefixes.plate.get(Materials.Titanium), Character.valueOf('S'), OrePrefixes.stick.get(Materials.Titanium), Character.valueOf('G'), OrePrefixes.gearGtSmall.get(Materials.Titanium), Character.valueOf('M'), GregtechItemList.Electric_Motor_MAX, Character.valueOf('C'), OrePrefixes.cableGt01.get(Materials.Aluminium)});
			tLastID = 16;
			GregtechItemList.Robot_Arm_LuV.set(this.addItem(tLastID = 16, "Robot Arm (LuV)", "Inserts into specific Slots (as Cover)", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 1L), getTcAspectStack(TC_Aspects.MACHINA, 2L), getTcAspectStack(TC_Aspects.MOTUS, 1L), Utils.getTcAspectStack("COGNITIO", 1L)}));
			GregtechItemList.Robot_Arm_ZPM.set(this.addItem(tLastID = 17, "Robot Arm (ZPM)", "Inserts into specific Slots (as Cover)", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 2L), getTcAspectStack(TC_Aspects.MACHINA, 4L), getTcAspectStack(TC_Aspects.MOTUS, 2L), getTcAspectStack("COGNITIO", 2L)}));
			GregtechItemList.Robot_Arm_UV.set(this.addItem(tLastID = 18, "Robot Arm (UV)", "Inserts into specific Slots (as Cover)", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 4L), getTcAspectStack(TC_Aspects.MACHINA, 8L), getTcAspectStack(TC_Aspects.MOTUS, 4L), getTcAspectStack("COGNITIO", 4L)}));
			GregtechItemList.Robot_Arm_MAX.set(this.addItem(tLastID = 19, "Robot Arm (MAX)", "Inserts into specific Slots (as Cover)", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 8L), getTcAspectStack(TC_Aspects.MACHINA, 16L), getTcAspectStack(TC_Aspects.MOTUS, 8L), getTcAspectStack("COGNITIO", 8L)}));
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Robot_Arm_LuV.get(1L), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"CCC", "MSM", "PES", Character.valueOf('S'), OrePrefixes.stick.get(Materials.Steel), Character.valueOf('M'), GregtechItemList.Electric_Motor_LuV, Character.valueOf('P'), GregtechItemList.Electric_Piston_LuV, Character.valueOf('E'), OrePrefixes.circuit.get(Materials.Basic), Character.valueOf('C'), OrePrefixes.cableGt01.get(Materials.Tin)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Robot_Arm_ZPM.get(1L), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"CCC", "MSM", "PES", Character.valueOf('S'), OrePrefixes.stick.get(Materials.Aluminium), Character.valueOf('M'), GregtechItemList.Electric_Motor_ZPM, Character.valueOf('P'), GregtechItemList.Electric_Piston_ZPM, Character.valueOf('E'), OrePrefixes.circuit.get(Materials.Good), Character.valueOf('C'), OrePrefixes.cableGt01.get(Materials.AnyCopper)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Robot_Arm_UV.get(1L), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"CCC", "MSM", "PES", Character.valueOf('S'), OrePrefixes.stick.get(Materials.StainlessSteel), Character.valueOf('M'), GregtechItemList.Electric_Motor_UV, Character.valueOf('P'), GregtechItemList.Electric_Piston_UV, Character.valueOf('E'), OrePrefixes.circuit.get(Materials.Advanced), Character.valueOf('C'), OrePrefixes.cableGt01.get(Materials.Gold)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Robot_Arm_MAX.get(1L), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"CCC", "MSM", "PES", Character.valueOf('S'), OrePrefixes.stick.get(Materials.Titanium), Character.valueOf('M'), GregtechItemList.Electric_Motor_MAX, Character.valueOf('P'), GregtechItemList.Electric_Piston_MAX, Character.valueOf('E'), OrePrefixes.circuit.get(Materials.Elite), Character.valueOf('C'), OrePrefixes.cableGt01.get(Materials.Aluminium)});
			GregTech_API.registerCover(GregtechItemList.Robot_Arm_LuV.get(1L), new GT_MultiTexture(new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[1][0], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_ARM)}), new GT_Cover_Arm(400));
			GregTech_API.registerCover(GregtechItemList.Robot_Arm_ZPM.get(1L), new GT_MultiTexture(new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[2][0], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_ARM)}), new GT_Cover_Arm(100));
			GregTech_API.registerCover(GregtechItemList.Robot_Arm_UV.get(1L), new GT_MultiTexture(new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[3][0], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_ARM)}), new GT_Cover_Arm(20));
			GregTech_API.registerCover(GregtechItemList.Robot_Arm_MAX.get(1L), new GT_MultiTexture(new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[4][0], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_ARM)}), new GT_Cover_Arm(4));
			tLastID = 20;
			GregtechItemList.Field_Generator_LuV.set(this.addItem(tLastID = 20, "Field Generator (LuV)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 2L), getTcAspectStack(TC_Aspects.MACHINA, 1L), getTcAspectStack(TC_Aspects.TUTAMEN, 1L)}));
			GregtechItemList.Field_Generator_ZPM.set(this.addItem(tLastID = 21, "Field Generator (ZPM)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 4L), getTcAspectStack(TC_Aspects.MACHINA, 2L), getTcAspectStack(TC_Aspects.TUTAMEN, 2L)}));
			GregtechItemList.Field_Generator_UV.set(this.addItem(tLastID = 22, "Field Generator (UV)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 8L), getTcAspectStack(TC_Aspects.MACHINA, 4L), getTcAspectStack(TC_Aspects.TUTAMEN, 4L)}));
			GregtechItemList.Field_Generator_MAX.set(this.addItem(tLastID = 23, "Field Generator (MAX)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 16L), getTcAspectStack(TC_Aspects.MACHINA, 8L), getTcAspectStack(TC_Aspects.TUTAMEN, 8L)}));
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Field_Generator_LuV.get(1L), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"WCW", "CGC", "WCW", Character.valueOf('G'), OrePrefixes.gem.get(Materials.EnderPearl), Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Basic), Character.valueOf('W'), OrePrefixes.wireGt01.get(Materials.Osmium)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Field_Generator_ZPM.get(1L), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"WCW", "CGC", "WCW", Character.valueOf('G'), OrePrefixes.gem.get(Materials.EnderEye), Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Good), Character.valueOf('W'), OrePrefixes.wireGt02.get(Materials.Osmium)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Field_Generator_UV.get(1L), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"WCW", "CGC", "WCW", Character.valueOf('G'), OrePrefixes.gem.get(Materials.NetherStar), Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Advanced), Character.valueOf('W'), OrePrefixes.wireGt04.get(Materials.Osmium)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Field_Generator_MAX.get(1L), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"WCW", "CGC", "WCW", Character.valueOf('G'), OrePrefixes.gem.get(Materials.NetherStar), Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Elite), Character.valueOf('W'), OrePrefixes.wireGt08.get(Materials.Osmium)});
			tLastID = 24;
			GregtechItemList.Emitter_LuV.set(this.addItem(tLastID = 24, "Emitter (LuV)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 1L), getTcAspectStack(TC_Aspects.MACHINA, 1L), getTcAspectStack(TC_Aspects.LUX, 1L)}));
			GregtechItemList.Emitter_ZPM.set(this.addItem(tLastID = 25, "Emitter (ZPM)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 2L), getTcAspectStack(TC_Aspects.MACHINA, 2L), getTcAspectStack(TC_Aspects.LUX, 2L)}));
			GregtechItemList.Emitter_UV.set(this.addItem(tLastID = 26, "Emitter (UV)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 4L), getTcAspectStack(TC_Aspects.MACHINA, 4L), getTcAspectStack(TC_Aspects.LUX, 4L)}));
			GregtechItemList.Emitter_MAX.set(this.addItem(tLastID = 27, "Emitter (MAX)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 8L), getTcAspectStack(TC_Aspects.MACHINA, 8L), getTcAspectStack(TC_Aspects.LUX, 8L)}));
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Emitter_LuV.get(1L), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"SSC", "WQS", "CWS", Character.valueOf('Q'), OrePrefixes.gem.get(Materials.Quartzite), Character.valueOf('S'), OrePrefixes.stick.get(Materials.Brass), Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Basic), Character.valueOf('W'), OrePrefixes.cableGt01.get(Materials.Tin)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Emitter_ZPM.get(1L), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"SSC", "WQS", "CWS", Character.valueOf('Q'), OrePrefixes.gem.get(Materials.NetherQuartz), Character.valueOf('S'), OrePrefixes.stick.get(Materials.Electrum), Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Good), Character.valueOf('W'), OrePrefixes.cableGt01.get(Materials.AnyCopper)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Emitter_UV.get(1L), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"SSC", "WQS", "CWS", Character.valueOf('Q'), OrePrefixes.gem.get(Materials.Emerald), Character.valueOf('S'), OrePrefixes.stick.get(Materials.Chrome), Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Advanced), Character.valueOf('W'), OrePrefixes.cableGt01.get(Materials.Gold)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Emitter_MAX.get(1L), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"SSC", "WQS", "CWS", Character.valueOf('Q'), OrePrefixes.gem.get(Materials.EnderPearl), Character.valueOf('S'), OrePrefixes.stick.get(Materials.Platinum), Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Elite), Character.valueOf('W'), OrePrefixes.cableGt01.get(Materials.Aluminium)});
			tLastID = 28;
			GregtechItemList.Sensor_LuV.set(this.addItem(tLastID = 28, "Sensor (LuV)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 1L), getTcAspectStack(TC_Aspects.MACHINA, 1L), getTcAspectStack(TC_Aspects.SENSUS, 1L)}));
			GregtechItemList.Sensor_ZPM.set(this.addItem(tLastID = 29, "Sensor (ZPM)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 2L), getTcAspectStack(TC_Aspects.MACHINA, 2L), getTcAspectStack(TC_Aspects.SENSUS, 2L)}));
			GregtechItemList.Sensor_UV.set(this.addItem(tLastID = 30, "Sensor (UV)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 4L), getTcAspectStack(TC_Aspects.MACHINA, 4L), getTcAspectStack(TC_Aspects.SENSUS, 4L)}));
			GregtechItemList.Sensor_MAX.set(this.addItem(tLastID = 31, "Sensor (MAX)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 8L), getTcAspectStack(TC_Aspects.MACHINA, 8L), getTcAspectStack(TC_Aspects.SENSUS, 8L)}));
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Sensor_LuV.get(1L), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"P Q", "PS ", "CPP", Character.valueOf('Q'), OrePrefixes.gem.get(Materials.Quartzite), Character.valueOf('S'), OrePrefixes.stick.get(Materials.Brass), Character.valueOf('P'), OrePrefixes.plate.get(Materials.Steel), Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Basic)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Sensor_ZPM.get(1L), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"P Q", "PS ", "CPP", Character.valueOf('Q'), OrePrefixes.gem.get(Materials.NetherQuartz), Character.valueOf('S'), OrePrefixes.stick.get(Materials.Electrum), Character.valueOf('P'), OrePrefixes.plate.get(Materials.Aluminium), Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Good)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Sensor_UV.get(1L), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"P Q", "PS ", "CPP", Character.valueOf('Q'), OrePrefixes.gem.get(Materials.Emerald), Character.valueOf('S'), OrePrefixes.stick.get(Materials.Chrome), Character.valueOf('P'), OrePrefixes.plate.get(Materials.StainlessSteel), Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Advanced)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Sensor_MAX.get(1L), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"P Q", "PS ", "CPP", Character.valueOf('Q'), OrePrefixes.gem.get(Materials.EnderPearl), Character.valueOf('S'), OrePrefixes.stick.get(Materials.Platinum), Character.valueOf('P'), OrePrefixes.plate.get(Materials.Titanium), Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Elite)});

			//Handler for ULV Components
			registerComponents_ULV();		


		}

		else {
			//Simplify life.
			registerComponents_ULV();	
			registerComponents_MAX();
		}

		//Extruder Shape
		GregtechItemList.Shape_Extruder_WindmillShaft.set(this.addItem(tLastID = 40, "Extruder Shape (Shaft)", "Extruder Shape for making Windmill Shafts"));

		//GTNH Already adds this.
		if (!CORE.GTNH) {
			GregtechItemList.Shape_Extruder_SmallGear.set(this.addItem(221, "Extruder Shape (Small Gear)", "Extruder Shape for making small gears"));
		}

		//Batteries
		GregtechItemList.Battery_RE_EV_Sodium.set(this.addItem(tLastID = 50, "Quad Cell Sodium Battery", "Reusable", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 2L), getTcAspectStack(TC_Aspects.METALLUM, 2L), getTcAspectStack(TC_Aspects.POTENTIA, 2L)}));
		this.setElectricStats(32000 + tLastID, 3200000L, GT_Values.V[4], 4L, -3L, true);

		GregtechItemList.Battery_RE_EV_Cadmium.set(this.addItem(tLastID = 52, "Quad Cell Cadmium Battery", "Reusable", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 1L), getTcAspectStack(TC_Aspects.METALLUM, 1L), getTcAspectStack(TC_Aspects.POTENTIA, 1L)}));
		this.setElectricStats(32000 + tLastID, 4800000L, GT_Values.V[4], 4L, -3L, true);

		GregtechItemList.Battery_RE_EV_Lithium.set(this.addItem(tLastID = 54, "Quad Cell Lithium Battery", "Reusable", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 3L), getTcAspectStack(TC_Aspects.METALLUM, 3L), getTcAspectStack(TC_Aspects.POTENTIA, 3L)}));
		this.setElectricStats(32000 + tLastID, 6400000L, GT_Values.V[4], 4L, -3L, true);

		/**
		 * Power Gems
		 */

		GregtechItemList.Battery_Gem_1.set(this.addItem(tLastID = 66, "Proton Cell", "Reusable", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 8L), getTcAspectStack(TC_Aspects.METALLUM, 24L), getTcAspectStack(TC_Aspects.POTENTIA, 16L)}));
		this.setElectricStats(32000 + tLastID, GT_Values.V[6] * 20 * 300 / 4, GT_Values.V[6], 6L, -3L, false);
		GregtechItemList.Battery_Gem_2.set(this.addItem(tLastID = 68, "Electron Cell", "Reusable", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 16L), getTcAspectStack(TC_Aspects.METALLUM, 32L), getTcAspectStack(TC_Aspects.POTENTIA, 32L)}));
		this.setElectricStats(32000 + tLastID, GT_Values.V[7] * 20 * 300 / 4, GT_Values.V[7], 7L, -3L, false);
		GregtechItemList.Battery_Gem_3.set(this.addItem(tLastID = 70, "Quark Entanglement", "Reusable", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 32L), getTcAspectStack(TC_Aspects.METALLUM, 48L), getTcAspectStack(TC_Aspects.POTENTIA, 64L)}));
		this.setElectricStats(32000 + tLastID, GT_Values.V[8] * 20 * 300 / 4, GT_Values.V[8], 8L, -3L, false);
		//ItemUtils.addItemToOreDictionary(GregtechItemList.Battery_Gem_1.get(1), "batteryFutureBasic");
		//ItemUtils.addItemToOreDictionary(GregtechItemList.Battery_Gem_2.get(1), "batteryFutureGood");
		//ItemUtils.addItemToOreDictionary(GregtechItemList.Battery_Gem_3.get(1), "batteryFutureAdvanced");


		/*GregtechItemList.Battery_RE_EV_Sodium.set(addItem(tLastID = 50, "Quad Cell Acid Battery", "Reusable", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 1L), getTcAspectStack(TC_Aspects.METALLUM, 1L), getTcAspectStack(TC_Aspects.POTENTIA, 1L)}));
        setElectricStats(32000 + tLastID, 5000000L, GT_Values.V[2], 4L, -3L, true);

        GregtechItemList.Battery_RE_EV_Sodium.set(addItem(tLastID = 50, "Quad Cell Mercury Battery", "Reusable", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 1L), getTcAspectStack(TC_Aspects.METALLUM, 1L), getTcAspectStack(TC_Aspects.POTENTIA, 1L)}));
        setElectricStats(32000 + tLastID, 5000000L, GT_Values.V[2], 4L, -3L, true);*/

		//RTG Pellet
		GregtechItemList.Pellet_RTG_PU238.set(this.addItem(41, StringUtils.superscript("238")+"Pu Pellet", "", new Object[]{getTcAspectStack(TC_Aspects.RADIO, 4L), getTcAspectStack(TC_Aspects.POTENTIA, 2L), getTcAspectStack(TC_Aspects.METALLUM, 2L)}));
		GregtechItemList.Pellet_RTG_SR90.set(this.addItem(42, StringUtils.superscript("90")+"Sr Pellet", "", new Object[]{getTcAspectStack(TC_Aspects.RADIO, 4L), getTcAspectStack(TC_Aspects.POTENTIA, 2L), getTcAspectStack(TC_Aspects.METALLUM, 2L)}));
		GregtechItemList.Pellet_RTG_PO210.set(this.addItem(43, StringUtils.superscript("210")+"Po Pellet", "", new Object[]{getTcAspectStack(TC_Aspects.RADIO, 4L), getTcAspectStack(TC_Aspects.POTENTIA, 2L), getTcAspectStack(TC_Aspects.METALLUM, 2L)}));
		GregtechItemList.Pellet_RTG_AM241.set(this.addItem(44, StringUtils.superscript("241")+"Am Pellet", "", new Object[]{getTcAspectStack(TC_Aspects.RADIO, 4L), getTcAspectStack(TC_Aspects.POTENTIA, 2L), getTcAspectStack(TC_Aspects.METALLUM, 2L)}));
		Recipe_GT.Gregtech_Recipe_Map.sRTGFuels.addRecipe(
				true,
				new ItemStack[]{GregtechItemList.Pellet_RTG_PU238.get(1)},
				new ItemStack[]{},
				null,
				null,
				null,
				0,
				64,
				MathUtils.roundToClosestInt(87.7f));
		Recipe_GT.Gregtech_Recipe_Map.sRTGFuels.addRecipe(
				true,
				new ItemStack[]{GregtechItemList.Pellet_RTG_SR90.get(1)},
				new ItemStack[]{},
				null,
				null,
				null,
				0,
				32,
				MathUtils.roundToClosestInt(28.8f));
		Recipe_GT.Gregtech_Recipe_Map.sRTGFuels.addRecipe(
				true,
				new ItemStack[]{GregtechItemList.Pellet_RTG_PO210.get(1)},
				new ItemStack[]{},
				null,
				null,
				null,
				0,
				512,
				MathUtils.roundToClosestInt(1f));
		Recipe_GT.Gregtech_Recipe_Map.sRTGFuels.addRecipe(
				true,
				new ItemStack[]{GregtechItemList.Pellet_RTG_AM241.get(1)},
				new ItemStack[]{},
				null,
				null,
				null,
				0,
				16,
				MathUtils.roundToClosestInt(432/2));
		//Computer Cube
		GregtechItemList.Gregtech_Computer_Cube.set(this.addItem(tLastID = 55, "Gregtech Computer Cube", "Reusable", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 8L), getTcAspectStack(TC_Aspects.METALLUM, 8L), getTcAspectStack(TC_Aspects.POTENTIA, 8L)}));
		this.setElectricStats(32000 + tLastID, GT_Values.V[6]* 10 * 60 * 20, GT_Values.V[5], 5L, -3L, true);

		//FOOOOOOOOOOOOOOD
		GregtechItemList.Food_Baked_Raisin_Bread.set(this.addItem(tLastID = 60, "Raisin Bread", "Extra Raisins, Just for ImQ009", new Object[]{new GT_FoodStat(5, 0.5F, EnumAction.eat, null, false, true, false, new int[0]), getTcAspectStack(TC_Aspects.CORPUS, 1L), getTcAspectStack(TC_Aspects.FAMES, 1L), getTcAspectStack(TC_Aspects.IGNIS, 1L)}));

		if (!CORE.GTNH) {
			GregtechItemList.Fluid_Cell_144L.set(this.addItem(tLastID = 61, "144L Invar Fluid Cell", "Holds exactly one dust worth of liquid.", new Object[]{new ItemData(Materials.Invar, (OrePrefixes.plate.mMaterialAmount * 8L) + (4L * OrePrefixes.ring.mMaterialAmount), new MaterialStack[0]), getTcAspectStack(TC_Aspects.VACUOS, 2L), getTcAspectStack(TC_Aspects.AQUA, 1L)}));
			this.setFluidContainerStats(32000 + tLastID, 144L, 64L);

			GregtechItemList.Fluid_Cell_36L.set(this.addItem(tLastID = 62, "36L Brass Fluid Cell", "Holds exactly one small dust worth of liquid.", new Object[]{new ItemData(Materials.Brass, (OrePrefixes.plate.mMaterialAmount * 8L) + (4L * OrePrefixes.ring.mMaterialAmount), new MaterialStack[0]), getTcAspectStack(TC_Aspects.VACUOS, 2L), getTcAspectStack(TC_Aspects.AQUA, 1L)}));
			this.setFluidContainerStats(32000 + tLastID, 36L, 64L);

			GregtechItemList.Fluid_Cell_16L.set(this.addItem(tLastID = 63, "16L Bronze Fluid Cell", "Holds exactly one tiny dust / nugget worth of liquid.", new Object[]{new ItemData(Materials.Bronze, (OrePrefixes.plate.mMaterialAmount * 8L) + (4L * OrePrefixes.ring.mMaterialAmount), new MaterialStack[0]), getTcAspectStack(TC_Aspects.VACUOS, 2L), getTcAspectStack(TC_Aspects.AQUA, 1L)}));
			this.setFluidContainerStats(32000 + tLastID, 16L, 64L);

			GregtechItemList.Fluid_Cell_1L.set(this.addItem(tLastID = 64, "1L Wrought Iron Fluid Cell", "Holds exactly one litre worth of liquid.", new Object[]{new ItemData(Materials.WroughtIron, (OrePrefixes.plate.mMaterialAmount * 8L) + (4L * OrePrefixes.ring.mMaterialAmount), new MaterialStack[0]), getTcAspectStack(TC_Aspects.VACUOS, 2L), getTcAspectStack(TC_Aspects.AQUA, 1L)}));
			this.setFluidContainerStats(32000 + tLastID, 1L, 64L);			
		}



		GregtechItemList.Cover_Overflow_ULV.set(this.addItem(71, "Overflow Valve (ULV)", "Maximum void amount: 8000", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 1L), getTcAspectStack(TC_Aspects.MACHINA, 1L), getTcAspectStack(TC_Aspects.ITER, 1L), getTcAspectStack(TC_Aspects.AQUA, 1L)}));
		GregtechItemList.Cover_Overflow_LV.set(this.addItem(72, "Overflow Valve (LV)", "Maximum void amount: 64000", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 1L), getTcAspectStack(TC_Aspects.MACHINA, 1L), getTcAspectStack(TC_Aspects.ITER, 1L), getTcAspectStack(TC_Aspects.AQUA, 1L)}));
		GregtechItemList.Cover_Overflow_MV.set(this.addItem(73, "Overflow Valve (MV)", "Maximum void amount: 512000", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 1L), getTcAspectStack(TC_Aspects.MACHINA, 1L), getTcAspectStack(TC_Aspects.ITER, 1L), getTcAspectStack(TC_Aspects.AQUA, 1L)}));
		GregtechItemList.Cover_Overflow_HV.set(this.addItem(74, "Overflow Valve (HV)", "Maximum void amount: 4096000", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 1L), getTcAspectStack(TC_Aspects.MACHINA, 1L), getTcAspectStack(TC_Aspects.ITER, 1L), getTcAspectStack(TC_Aspects.AQUA, 1L)}));
		GregtechItemList.Cover_Overflow_EV.set(this.addItem(75, "Overflow Valve (EV)", "Maximum void amount: 32768000", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 1L), getTcAspectStack(TC_Aspects.MACHINA, 1L), getTcAspectStack(TC_Aspects.ITER, 1L), getTcAspectStack(TC_Aspects.AQUA, 1L)}));
		GregtechItemList.Cover_Overflow_IV.set(this.addItem(76, "Overflow Valve (IV)", "Maximum void amount: 262144000", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 1L), getTcAspectStack(TC_Aspects.MACHINA, 1L), getTcAspectStack(TC_Aspects.ITER, 1L), getTcAspectStack(TC_Aspects.AQUA, 1L)}));

		GregTech_API.registerCover(GregtechItemList.Cover_Overflow_ULV.get(1L), new GT_MultiTexture(new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[4][0], new GT_RenderedTexture(TexturesGtBlock.Overlay_Overflow_Valve)}), new GTPP_Cover_Overflow2(8));
		GregTech_API.registerCover(GregtechItemList.Cover_Overflow_LV.get(1L), new GT_MultiTexture(new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[4][0], new GT_RenderedTexture(TexturesGtBlock.Overlay_Overflow_Valve)}), new GTPP_Cover_Overflow2(64));
		GregTech_API.registerCover(GregtechItemList.Cover_Overflow_MV.get(1L), new GT_MultiTexture(new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[5][0], new GT_RenderedTexture(TexturesGtBlock.Overlay_Overflow_Valve)}), new GTPP_Cover_Overflow2(512));
		GregTech_API.registerCover(GregtechItemList.Cover_Overflow_HV.get(1L), new GT_MultiTexture(new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[5][0], new GT_RenderedTexture(TexturesGtBlock.Overlay_Overflow_Valve)}), new GTPP_Cover_Overflow2(4096));
		GregTech_API.registerCover(GregtechItemList.Cover_Overflow_EV.get(1L), new GT_MultiTexture(new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[8][0], new GT_RenderedTexture(TexturesGtBlock.Overlay_Overflow_Valve)}), new GTPP_Cover_Overflow2(32768));
		GregTech_API.registerCover(GregtechItemList.Cover_Overflow_IV.get(1L), new GT_MultiTexture(new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[8][0], new GT_RenderedTexture(TexturesGtBlock.Overlay_Overflow_Valve)}), new GTPP_Cover_Overflow2(262144));	

		//Fusion Reactor MK4 Singularity
		GregtechItemList.Compressed_Fusion_Reactor.set(this.addItem(100, "Hypervisor Matrix (Fusion)", "A memory unit containing an RI (Restricted Intelligence)"));

		
		//NanoTubes
		GregtechItemList.NanoTube_Base_Substrate.set(this.addItem(101, "Silicon Base Substrate", "Used in the production of Carbon Nanotubes"));
		GregtechItemList.NanoTube_Finished.set(this.addItem(102, "Carbon Nanotubes", "Multi-walled Zigzag nanotubes, possibly Carbon's final form"));
		GregtechItemList.Carbyne_Tube_Finished.set(this.addItem(103, "Linear Acetylenic Carbon (LAC/Carbyne)", "LAC chains grown inside Multi-walled Carbon Nanotubes, highly stable"));
		GregtechItemList.Carbyne_Sheet_Finished.set(this.addItem(104, "Carbyne Composite Panel", "Nanotubes which contain LAC, arranged side by side and compressed further"));
		GregtechItemList.Laser_Lens_Special.set(this.addItem(105, "Quantum Anomaly", "Probably should shoot it with lasers"));

		GregtechItemList.Battery_Casing_Gem_1.set(this.addItem(106, "Containment Unit I", "Used in crafting"));
		GregtechItemList.Battery_Casing_Gem_2.set(this.addItem(107, "Containment Unit II", "Used in crafting"));
		GregtechItemList.Battery_Casing_Gem_3.set(this.addItem(108, "Advanced Containment Unit", "Used in crafting"));
		GregtechItemList.Battery_Casing_Gem_4.set(this.addItem(109, "Exotic Containment Unit", "Used in crafting"));		

		GregtechItemList.Battery_Gem_4.set(this.addItem(tLastID = 110, "Graviton Anomaly", "Reusable", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 64L), getTcAspectStack(TC_Aspects.METALLUM, 64L), getTcAspectStack(TC_Aspects.POTENTIA, 64L)}));
		this.setElectricStats(32000 + tLastID, (64000000000L*16), GT_Values.V[9], 9L, -3L, false);
		
		
		
		/*
		 * Bombs
		 */
		GregtechItemList.Bomb_Cast.set(this.addItem(111, "Bomb Cast", "Used in the production of Bombs"));
		GregtechItemList.Bomb_Cast_Molten.set(this.addItem(112, "Bomb Cast (Hot)", "Consider cooling this off"));
		GregtechItemList.Bomb_Cast_Set.set(this.addItem(113, "Bomb Cast (Set)", "Break it open for the goodies inside!"));
		GregtechItemList.Bomb_Cast_Broken.set(this.addItem(114, "Bomb Cast (Broken)", "This is probably just junk"));
		GregtechItemList.Bomb_Cast_Mold.set(this.addItem(115, "Mold (Bomb Cast)", "Used in the production of Bombs"));
		
		/*
		 * High Tier 'Saws' for the tree Farm
		 */
		GregtechItemList.Farm_Processor_EV.set(this.addItem(tLastID = 120, "Farm Processor [EV]", "Reusable", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 8L), getTcAspectStack(TC_Aspects.METALLUM, 8L), getTcAspectStack(TC_Aspects.POTENTIA, 8L)}));
		this.setElectricStats(32000 + tLastID, GT_Values.V[5]* 10 * 60 * 20, GT_Values.V[4], 4L, -3L, false);
		GregtechItemList.Farm_Processor_IV.set(this.addItem(tLastID = 122, "Farm Processor [IV]", "Reusable", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 8L), getTcAspectStack(TC_Aspects.METALLUM, 8L), getTcAspectStack(TC_Aspects.POTENTIA, 8L)}));
		this.setElectricStats(32000 + tLastID, GT_Values.V[6]* 10 * 60 * 20, GT_Values.V[5], 5L, -3L, false);
		GregtechItemList.Farm_Processor_LuV.set(this.addItem(tLastID = 124, "Farm Processor [LuV]", "Reusable", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 8L), getTcAspectStack(TC_Aspects.METALLUM, 8L), getTcAspectStack(TC_Aspects.POTENTIA, 8L)}));
		this.setElectricStats(32000 + tLastID, GT_Values.V[7]* 10 * 60 * 20, GT_Values.V[6], 6L, -3L, false);
		GregtechItemList.Farm_Processor_ZPM.set(this.addItem(tLastID = 126, "Farm Processor [ZPM]", "Reusable", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 8L), getTcAspectStack(TC_Aspects.METALLUM, 8L), getTcAspectStack(TC_Aspects.POTENTIA, 8L)}));
		this.setElectricStats(32000 + tLastID, GT_Values.V[8]* 10 * 60 * 20, GT_Values.V[7], 7L, -3L, false);
		GregtechItemList.Farm_Processor_UV.set(this.addItem(tLastID = 128, "Farm Processor [UV]", "Reusable", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 8L), getTcAspectStack(TC_Aspects.METALLUM, 8L), getTcAspectStack(TC_Aspects.POTENTIA, 8L)}));
		this.setElectricStats(32000 + tLastID, GT_Values.V[9]* 10 * 60 * 20, GT_Values.V[8], 8L, -3L, false);
		
		String aTierName;
        int aFirstMachineCasingID = 130;
        GregtechItemList[] mMachineCasingCovers = new GregtechItemList[] {
                GregtechItemList.FakeMachineCasingPlate_ULV,
                GregtechItemList.FakeMachineCasingPlate_LV,
                GregtechItemList.FakeMachineCasingPlate_MV,
                GregtechItemList.FakeMachineCasingPlate_HV,
                GregtechItemList.FakeMachineCasingPlate_EV,
                GregtechItemList.FakeMachineCasingPlate_IV,
                GregtechItemList.FakeMachineCasingPlate_LuV,
                GregtechItemList.FakeMachineCasingPlate_ZPM,
                GregtechItemList.FakeMachineCasingPlate_UV,
                GregtechItemList.FakeMachineCasingPlate_MAX,
        };
        for (int i=0;i<10;i++) {
            if (i==10) {
                break;
            }
            else {              
                aTierName = GT_Values.VN[i];
                mMachineCasingCovers[i].set(this.addItem(aFirstMachineCasingID++, aTierName+" Machine Plate Cover", "Deprecated - Shapeless Craft to new version", new Object[]{}));
                GregTech_API.registerCover(mMachineCasingCovers[i].get(1L), new GT_MultiTexture(new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[i][0]}), new GTPP_Cover_ToggleVisual());
             }
        }
        GregtechItemList.Laser_Lens_WoodsGlass.set(this.addItem(140, "Wood's Glass Lens", "Allows UV & IF to pass through, blocks visible light spectrums"));

		
		
	}

	private boolean registerComponents_ULV(){
		GregtechItemList.Electric_Pump_ULV.set(this.addItem(32, "Electric Pump (ULV)", "160 L/sec (as Cover)", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 1L), getTcAspectStack(TC_Aspects.MACHINA, 1L), getTcAspectStack(TC_Aspects.ITER, 1L), getTcAspectStack(TC_Aspects.AQUA, 1L)}));
		GregtechItemList.Electric_Motor_ULV.set(this.addItem(33, "Electric Motor (ULV)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 1L), getTcAspectStack(TC_Aspects.MACHINA, 1L), getTcAspectStack(TC_Aspects.MOTUS, 1L)}));
		GregtechItemList.Conveyor_Module_ULV.set(this.addItem(34, "Conveyor Module (ULV)", "1 Stack every 80 secs (as Cover)", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 1L), getTcAspectStack(TC_Aspects.MACHINA, 1L), getTcAspectStack(TC_Aspects.ITER, 1L)}));
		GregtechItemList.Electric_Piston_ULV.set(this.addItem(35, "Electric Piston (ULV)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 1L), getTcAspectStack(TC_Aspects.MACHINA, 2L), getTcAspectStack(TC_Aspects.MOTUS, 1L)}));
		GregtechItemList.Robot_Arm_ULV.set(this.addItem(36, "Robot Arm (ULV)", "Inserts into specific Slots (as Cover)", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 1L), getTcAspectStack(TC_Aspects.MACHINA, 2L), getTcAspectStack(TC_Aspects.MOTUS, 1L), Utils.getTcAspectStack("COGNITIO", 1L)}));
		GregtechItemList.Field_Generator_ULV.set(this.addItem(37, "Field Generator (ULV)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 2L), getTcAspectStack(TC_Aspects.MACHINA, 1L), getTcAspectStack(TC_Aspects.TUTAMEN, 1L)}));
		GregtechItemList.Emitter_ULV.set(this.addItem(38, "Emitter (ULV)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 1L), getTcAspectStack(TC_Aspects.MACHINA, 1L), getTcAspectStack(TC_Aspects.LUX, 1L)}));
		GregtechItemList.Sensor_ULV.set(this.addItem(39, "Sensor (ULV)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 1L), getTcAspectStack(TC_Aspects.MACHINA, 1L), getTcAspectStack(TC_Aspects.SENSUS, 1L)}));

		GregTech_API.registerCover(GregtechItemList.Electric_Pump_ULV.get(1L), new GT_MultiTexture(new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[5][0], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_PUMP)}), new GT_Cover_Pump(8));
		GregTech_API.registerCover(GregtechItemList.Conveyor_Module_ULV.get(1L), new GT_MultiTexture(new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[1][0], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_CONVEYOR)}), new GT_Cover_Conveyor(1600));
		GregTech_API.registerCover(GregtechItemList.Robot_Arm_ULV.get(1L), new GT_MultiTexture(new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[1][0], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_ARM)}), new GT_Cover_Arm(1600));

		return true;
	}

	private boolean registerComponents_MAX(){
		GregtechItemList.Electric_Pump_MAX.set(this.addItem(3, "Electric Pump (MAX)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 8L), getTcAspectStack(TC_Aspects.MACHINA, 8L), getTcAspectStack(TC_Aspects.ITER, 8L), getTcAspectStack(TC_Aspects.AQUA, 8L)}));
		GregtechItemList.Electric_Motor_MAX.set(this.addItem(7, "Electric Motor (MAX)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 8L), getTcAspectStack(TC_Aspects.MACHINA, 8L), getTcAspectStack(TC_Aspects.MOTUS, 8L)}));
		GregtechItemList.Conveyor_Module_MAX.set(this.addItem(11, "Conveyor Module (MAX)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 8L), getTcAspectStack(TC_Aspects.MACHINA, 8L), getTcAspectStack(TC_Aspects.ITER, 8L)}));
		GregtechItemList.Electric_Piston_MAX.set(this.addItem(15, "Electric Piston (MAX)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 8L), getTcAspectStack(TC_Aspects.MACHINA, 16L), getTcAspectStack(TC_Aspects.MOTUS, 8L)}));
		GregtechItemList.Robot_Arm_MAX.set(this.addItem(19, "Robot Arm (MAX)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 8L), getTcAspectStack(TC_Aspects.MACHINA, 16L), getTcAspectStack(TC_Aspects.MOTUS, 8L), getTcAspectStack("COGNITIO", 8L)}));
		GregtechItemList.Field_Generator_MAX.set(this.addItem(23, "Field Generator (MAX)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 16L), getTcAspectStack(TC_Aspects.MACHINA, 8L), getTcAspectStack(TC_Aspects.TUTAMEN, 8L)}));
		GregtechItemList.Emitter_MAX.set(this.addItem(27, "Emitter (MAX)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 8L), getTcAspectStack(TC_Aspects.MACHINA, 8L), getTcAspectStack(TC_Aspects.LUX, 8L)}));
		GregtechItemList.Sensor_MAX.set(this.addItem(31, "Sensor (MAX)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 8L), getTcAspectStack(TC_Aspects.MACHINA, 8L), getTcAspectStack(TC_Aspects.SENSUS, 8L)}));

		GregTech_API.registerCover(GregtechItemList.Electric_Pump_MAX.get(1L), new GT_MultiTexture(new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[8][0], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_PUMP)}), new GT_Cover_Pump(524288));
		GregTech_API.registerCover(GregtechItemList.Conveyor_Module_MAX.get(1L), new GT_MultiTexture(new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[4][0], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_CONVEYOR)}), new GT_Cover_Conveyor(4));
		GregTech_API.registerCover(GregtechItemList.Robot_Arm_MAX.get(1L), new GT_MultiTexture(new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[4][0], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_ARM)}), new GT_Cover_Arm(4));


		return true;
	}

	public boolean registerOldCircuits(){
		//Enable Old Circuits
		Logger.INFO("[Old Feature - Circuits] Enabling Pre-5.09.28 Circuits and Data Storage.");

		GregtechItemList.Old_Circuit_Primitive.set(this.addItem(200, "NAND Chip", "A very simple Circuit", new Object[]{OrePrefixes.circuit.get(Materials.Primitive)}));
		GregtechItemList.Old_Circuit_Basic.set(this.addItem(201, "Basic Electronic Circuit", "A basic Circuit", new Object[]{OrePrefixes.circuit.get(Materials.Basic)}));
		GregtechItemList.Old_Circuit_Good.set(this.addItem(202, "Good Electronic Circuit", "A good Circuit", new Object[]{OrePrefixes.circuit.get(Materials.Good)}));
		GregtechItemList.Old_Circuit_Advanced.set(this.addItem(203, "Advanced Circuit", "An advanced Circuit", new Object[]{OrePrefixes.circuit.get(Materials.Advanced)}));
		GregtechItemList.Old_Circuit_Data.set(this.addItem(204, "Data Storage Circuit", "A Data Storage Chip", new Object[]{OrePrefixes.circuit.get(Materials.Data)}));
		GregtechItemList.Old_Circuit_Elite.set(this.addItem(205, "Data Control Circuit", "A Processor", new Object[]{OrePrefixes.circuit.get(Materials.Elite)}));
		GregtechItemList.Old_Circuit_Master.set(this.addItem(206, "Energy Flow Circuit", "A High Voltage Processor", new Object[]{OrePrefixes.circuit.get(Materials.Master)}));

		GregtechItemList.Old_Tool_DataOrb.set(this.addItem(207, "Data Orb [GT++]", "A High Capacity Data Storage", new Object[]{OrePrefixes.circuit.get(Materials.Ultimate), SubTag.NO_UNIFICATION, new Behaviour_DataOrb()}));
		GregtechItemList.Old_Circuit_Ultimate.set(GregtechItemList.Old_Tool_DataOrb.get(1L));
		GT_ModHandler.addShapelessCraftingRecipe(GregtechItemList.Old_Tool_DataOrb.get(1L), GT_ModHandler.RecipeBits.NOT_REMOVABLE, new Object[]{GregtechItemList.Old_Tool_DataOrb.get(1L)});

		GregtechItemList.Old_Tool_DataStick.set(this.addItem(208, "Data Stick [GT++]", "A Low Capacity Data Storage", new Object[]{OrePrefixes.circuit.get(Materials.Data), SubTag.NO_UNIFICATION, new Behaviour_DataStick()}));
		GT_ModHandler.addShapelessCraftingRecipe(GregtechItemList.Old_Tool_DataStick.get(1L), GT_ModHandler.RecipeBits.NOT_REMOVABLE, new Object[]{GregtechItemList.Old_Tool_DataStick.get(1L)});

		GregtechItemList.Old_Circuit_Board_Basic.set(this.addItem(210, "Basic Circuit Board", "A basic Board"));
		GregtechItemList.Old_Circuit_Board_Advanced.set(this.addItem(211, "Advanced Circuit Board", "An advanced Board"));
		GregtechItemList.Old_Circuit_Board_Elite.set(this.addItem(212, "Processor Board", "A Processor Board"));
		GregtechItemList.Old_Circuit_Parts_Crystal_Chip_Elite.set(this.addItem(213, "Engraved Crystal Chip", "Needed for Circuits"));
		GregtechItemList.Old_Circuit_Parts_Crystal_Chip_Master.set(this.addItem(214, "Engraved Lapotron Chip", "Needed for Circuits"));
		GregtechItemList.Old_Circuit_Parts_Advanced.set(this.addItem(215, "Advanced Circuit Parts", "Advanced Circuit Parts"));
		GregtechItemList.Old_Circuit_Parts_Wiring_Basic.set(this.addItem(216, "Etched Medium Voltage Wiring", "Part of Circuit Boards"));
		GregtechItemList.Old_Circuit_Parts_Wiring_Advanced.set(this.addItem(217, "Etched High Voltage Wiring", "Part of Circuit Boards"));
		GregtechItemList.Old_Circuit_Parts_Wiring_Elite.set(this.addItem(218, "Etched Extreme Voltage Wiring", "Part of Circuit Boards"));
		GregtechItemList.Old_Empty_Board_Basic.set(this.addItem(219, "Empty Circuit Board", "A Board Part"));
		GregtechItemList.Old_Empty_Board_Elite.set(this.addItem(220, "Empty Processor Board", "A Processor Board Part"));

		return true;
	}

	public boolean registerCustomCircuits() {
		if (CORE.ConfigSwitches.enableCustomCircuits){
			GregtechItemList.Circuit_IV.set(this.addItem(704, "Symbiotic Circuit (IV)", "A Symbiotic Data Processor", new Object[]{GregtechOrePrefixes.circuit.get(GT_Materials.Symbiotic)}));
			GregtechItemList.Circuit_LuV.set(this.addItem(705, "Neutronic Circuit (LuV)", "A Neutron Particle Processor", new Object[]{GregtechOrePrefixes.circuit.get(GT_Materials.Neutronic)}));
			GregtechItemList.Circuit_ZPM.set(this.addItem(706, "Quantum Circuit (ZPM)", "A Singlularity Processor", new Object[]{GregtechOrePrefixes.circuit.get(GT_Materials.Quantum)}));
			GregtechItemList.Circuit_Board_IV.set(this.addItem(710, "IV Circuit Board", "An IV Voltage Rated Circuit Board"));
			GregtechItemList.Circuit_Board_LuV.set(this.addItem(711, "LuV Circuit Board", "An LuV Voltage Rated Circuit Board"));
			GregtechItemList.Circuit_Board_ZPM.set(this.addItem(712, "ZPM Processor Board", "A ZPM Voltage Rated Processor Board"));
			GregtechItemList.Circuit_Parts_Crystal_Chip_IV.set(this.addItem(713, "(IV) Energized Crystal Chip", "Needed for Circuits"));
			GregtechItemList.Circuit_Parts_Crystal_Chip_LuV.set(this.addItem(714, "(LuV) Neutron based Microchip", "Needed for Circuits"));
			GregtechItemList.Circuit_Parts_Crystal_Chip_ZPM.set(this.addItem(715, "(ZPM) Quantum Chip", "Needed for Circuits"));
			GregtechItemList.Circuit_Parts_IV.set(this.addItem(716, "(IV) Energized Circuit Parts", "Circuit Parts"));
			GregtechItemList.Circuit_Parts_LuV.set(this.addItem(717, "(LuV) Neutron-based Circuit Parts", "Circuit Parts"));
			GregtechItemList.Circuit_Parts_ZPM.set(this.addItem(718, "(ZPM) Quantum Circuit Parts", "Circuit Parts"));
			GregtechItemList.Circuit_Parts_Wiring_IV.set(this.addItem(719, "Etched IV Voltage Wiring", "Part of Circuit Boards"));
			GregtechItemList.Circuit_Parts_Wiring_LuV.set(this.addItem(720, "Etched LuV Voltage Wiring", "Part of Circuit Boards"));
			GregtechItemList.Circuit_Parts_Wiring_ZPM.set(this.addItem(721, "Etched ZPM Voltage Wiring", "Part of Circuit Boards"));
			ItemUtils.addItemToOreDictionary(GregtechItemList.Circuit_IV.get(1), "circuitSuperconductor");
			ItemUtils.addItemToOreDictionary(GregtechItemList.Circuit_LuV.get(1), "circuitInfinite");
			return true;
		}
		return false;
	}
}
