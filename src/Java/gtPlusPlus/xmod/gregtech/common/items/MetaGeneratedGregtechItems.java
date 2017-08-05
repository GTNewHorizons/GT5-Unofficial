package gtPlusPlus.xmod.gregtech.common.items;

import static gtPlusPlus.core.util.Utils.getTcAspectStack;

import codechicken.nei.api.API;
import cpw.mods.fml.common.Loader;
import gregtech.api.GregTech_API;
import gregtech.api.enums.*;
import gregtech.api.interfaces.ITexture;
import gregtech.api.objects.*;
import gregtech.api.util.*;
import gregtech.common.covers.*;
import gregtech.common.items.behaviors.Behaviour_DataOrb;
import gregtech.common.items.behaviors.Behaviour_DataStick;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.recipe.RECIPES_Old_Circuits;
import gtPlusPlus.core.util.StringUtils;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
import gtPlusPlus.xmod.gregtech.api.items.Gregtech_MetaItem_X32;
import net.minecraft.item.EnumAction;

public class MetaGeneratedGregtechItems extends Gregtech_MetaItem_X32 {
	public MetaGeneratedGregtechItems INSTANCE;

	public MetaGeneratedGregtechItems() {
		super("MU-metaitem.01", new OrePrefixes[]{null});
		this.INSTANCE = this;
		int tLastID = 0;

		if (!CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK){
			Utils.LOG_INFO("Gregtech 5.09 not found, using fallback components. (I like how I have to add compat to something I added first and had stolen.)");
			GregtechItemList.Electric_Pump_LuV.set(this.addItem(tLastID = 0, "Electric Pump (LuV)", "163920 L/sec (as Cover)", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 1L), getTcAspectStack(TC_Aspects.MACHINA, 1L), getTcAspectStack(TC_Aspects.ITER, 1L), getTcAspectStack(TC_Aspects.AQUA, 1L)}));
			GregtechItemList.Electric_Pump_ZPM.set(this.addItem(tLastID = 1, "Electric Pump (ZPM)", "655680 L/sec (as Cover)", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 2L), getTcAspectStack(TC_Aspects.MACHINA, 2L), getTcAspectStack(TC_Aspects.ITER, 2L), getTcAspectStack(TC_Aspects.AQUA, 2L)}));
			GregtechItemList.Electric_Pump_UV.set(this.addItem(tLastID = 2, "Electric Pump (UV)", "2622720 L/sec (as Cover)", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 4L), getTcAspectStack(TC_Aspects.MACHINA, 4L), getTcAspectStack(TC_Aspects.ITER, 4L), getTcAspectStack(TC_Aspects.AQUA, 4L)}));
			GregtechItemList.Electric_Pump_MAX.set(this.addItem(tLastID = 3, "Electric Pump (MAX)", "10490880 L/sec (as Cover)", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 8L), getTcAspectStack(TC_Aspects.MACHINA, 8L), getTcAspectStack(TC_Aspects.ITER, 8L), getTcAspectStack(TC_Aspects.AQUA, 8L)}));
			GregTech_API.registerCover(GregtechItemList.Electric_Pump_LuV.get(1L, new Object[0]), new GT_MultiTexture(new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[5][0], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_PUMP)}), new GT_Cover_Pump(8196));
			GregTech_API.registerCover(GregtechItemList.Electric_Pump_ZPM.get(1L, new Object[0]), new GT_MultiTexture(new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[6][0], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_PUMP)}), new GT_Cover_Pump(32768));
			GregTech_API.registerCover(GregtechItemList.Electric_Pump_UV.get(1L, new Object[0]), new GT_MultiTexture(new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[7][0], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_PUMP)}), new GT_Cover_Pump(131072));
			GregTech_API.registerCover(GregtechItemList.Electric_Pump_MAX.get(1L, new Object[0]), new GT_MultiTexture(new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[8][0], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_PUMP)}), new GT_Cover_Pump(524288));
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Electric_Pump_LuV.get(1L, new Object[0]), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"SXO", "dPw", "OMW", Character.valueOf('M'), GregtechItemList.Electric_Motor_LuV, Character.valueOf('O'), OrePrefixes.ring.get(Materials.Rubber), Character.valueOf('X'), OrePrefixes.rotor.get(Materials.Tin), Character.valueOf('S'), OrePrefixes.screw.get(Materials.Tin), Character.valueOf('W'), OrePrefixes.cableGt01.get(Materials.Tin), Character.valueOf('P'), OrePrefixes.pipeMedium.get(Materials.Bronze)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Electric_Pump_ZPM.get(1L, new Object[0]), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"SXO", "dPw", "OMW", Character.valueOf('M'), GregtechItemList.Electric_Motor_ZPM, Character.valueOf('O'), OrePrefixes.ring.get(Materials.Rubber), Character.valueOf('X'), OrePrefixes.rotor.get(Materials.Bronze), Character.valueOf('S'), OrePrefixes.screw.get(Materials.Bronze), Character.valueOf('W'), OrePrefixes.cableGt01.get(Materials.AnyCopper), Character.valueOf('P'), OrePrefixes.pipeMedium.get(Materials.Steel)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Electric_Pump_UV.get(1L, new Object[0]), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"SXO", "dPw", "OMW", Character.valueOf('M'), GregtechItemList.Electric_Motor_UV, Character.valueOf('O'), OrePrefixes.ring.get(Materials.Rubber), Character.valueOf('X'), OrePrefixes.rotor.get(Materials.Steel), Character.valueOf('S'), OrePrefixes.screw.get(Materials.Steel), Character.valueOf('W'), OrePrefixes.cableGt01.get(Materials.Gold), Character.valueOf('P'), OrePrefixes.pipeMedium.get(Materials.StainlessSteel)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Electric_Pump_MAX.get(1L, new Object[0]), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"SXO", "dPw", "OMW", Character.valueOf('M'), GregtechItemList.Electric_Motor_MAX, Character.valueOf('O'), OrePrefixes.ring.get(Materials.Rubber), Character.valueOf('X'), OrePrefixes.rotor.get(Materials.StainlessSteel), Character.valueOf('S'), OrePrefixes.screw.get(Materials.StainlessSteel), Character.valueOf('W'), OrePrefixes.cableGt01.get(Materials.Aluminium), Character.valueOf('P'), OrePrefixes.pipeMedium.get(Materials.Titanium)});
			tLastID = 4;
			GregtechItemList.Electric_Motor_LuV.set(this.addItem(tLastID = 4, "Electric Motor (LuV)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 1L), getTcAspectStack(TC_Aspects.MACHINA, 1L), getTcAspectStack(TC_Aspects.MOTUS, 1L)}));
			GregtechItemList.Electric_Motor_ZPM.set(this.addItem(tLastID = 5, "Electric Motor (ZPM)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 2L), getTcAspectStack(TC_Aspects.MACHINA, 2L), getTcAspectStack(TC_Aspects.MOTUS, 2L)}));
			GregtechItemList.Electric_Motor_UV.set(this.addItem(tLastID = 6, "Electric Motor (UV)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 4L), getTcAspectStack(TC_Aspects.MACHINA, 4L), getTcAspectStack(TC_Aspects.MOTUS, 4L)}));
			GregtechItemList.Electric_Motor_MAX.set(this.addItem(tLastID = 7, "Electric Motor (MAX)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 8L), getTcAspectStack(TC_Aspects.MACHINA, 8L), getTcAspectStack(TC_Aspects.MOTUS, 8L)}));
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Electric_Motor_LuV.get(1L, new Object[0]), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"CWR", "WIW", "RWC", Character.valueOf('I'), OrePrefixes.stick.get(Materials.IronMagnetic), Character.valueOf('R'), OrePrefixes.stick.get(Materials.AnyIron), Character.valueOf('W'), OrePrefixes.wireGt01.get(Materials.AnyCopper), Character.valueOf('C'), OrePrefixes.cableGt01.get(Materials.Tin)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Electric_Motor_ZPM.get(1L, new Object[0]), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"CWR", "WIW", "RWC", Character.valueOf('I'), OrePrefixes.stick.get(Materials.SteelMagnetic), Character.valueOf('R'), OrePrefixes.stick.get(Materials.Aluminium), Character.valueOf('W'), OrePrefixes.wireGt01.get(Materials.Electrum), Character.valueOf('C'), OrePrefixes.cableGt01.get(Materials.Silver)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Electric_Motor_UV.get(1L, new Object[0]), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"CWR", "WIW", "RWC", Character.valueOf('I'), OrePrefixes.stick.get(Materials.SteelMagnetic), Character.valueOf('R'), OrePrefixes.stick.get(Materials.StainlessSteel), Character.valueOf('W'), OrePrefixes.wireGt02.get(Materials.Cupronickel), Character.valueOf('C'), OrePrefixes.cableGt01.get(Materials.Gold)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Electric_Motor_MAX.get(1L, new Object[0]), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"CWR", "WIW", "RWC", Character.valueOf('I'), OrePrefixes.stick.get(Materials.NeodymiumMagnetic), Character.valueOf('R'), OrePrefixes.stick.get(Materials.Titanium), Character.valueOf('W'), OrePrefixes.wireGt02.get(Materials.TungstenSteel), Character.valueOf('C'), OrePrefixes.cableGt01.get(Materials.Nichrome)});

			tLastID = 8;
			GregtechItemList.Conveyor_Module_LuV.set(this.addItem(tLastID = 8, "Conveyor Module (LuV)", "1 Stack every 20 secs (as Cover)", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 1L), getTcAspectStack(TC_Aspects.MACHINA, 1L), getTcAspectStack(TC_Aspects.ITER, 1L)}));
			GregtechItemList.Conveyor_Module_ZPM.set(this.addItem(tLastID = 9, "Conveyor Module (ZPM)", "1 Stack every 5 secs (as Cover)", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 2L), getTcAspectStack(TC_Aspects.MACHINA, 2L), getTcAspectStack(TC_Aspects.ITER, 2L)}));
			GregtechItemList.Conveyor_Module_UV.set(this.addItem(tLastID = 10, "Conveyor Module (UV)", "1 Stack every 1 sec (as Cover)", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 4L), getTcAspectStack(TC_Aspects.MACHINA, 4L), getTcAspectStack(TC_Aspects.ITER, 4L)}));
			GregtechItemList.Conveyor_Module_MAX.set(this.addItem(tLastID = 11, "Conveyor Module (MAX)", "1 Stack every 1/5 sec (as Cover)", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 8L), getTcAspectStack(TC_Aspects.MACHINA, 8L), getTcAspectStack(TC_Aspects.ITER, 8L)}));
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Conveyor_Module_LuV.get(1L, new Object[0]), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"RRR", "MCM", "RRR", Character.valueOf('M'), GregtechItemList.Electric_Motor_LuV, Character.valueOf('C'), OrePrefixes.cableGt01.get(Materials.Tin), Character.valueOf('R'), OrePrefixes.plate.get(Materials.Rubber)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Conveyor_Module_ZPM.get(1L, new Object[0]), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"RRR", "MCM", "RRR", Character.valueOf('M'), GregtechItemList.Electric_Motor_ZPM, Character.valueOf('C'), OrePrefixes.cableGt01.get(Materials.AnyCopper), Character.valueOf('R'), OrePrefixes.plate.get(Materials.Rubber)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Conveyor_Module_UV.get(1L, new Object[0]), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"RRR", "MCM", "RRR", Character.valueOf('M'), GregtechItemList.Electric_Motor_UV, Character.valueOf('C'), OrePrefixes.cableGt01.get(Materials.Gold), Character.valueOf('R'), OrePrefixes.plate.get(Materials.Rubber)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Conveyor_Module_MAX.get(1L, new Object[0]), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"RRR", "MCM", "RRR", Character.valueOf('M'), GregtechItemList.Electric_Motor_MAX, Character.valueOf('C'), OrePrefixes.cableGt01.get(Materials.Aluminium), Character.valueOf('R'), OrePrefixes.plate.get(Materials.Rubber)});
			GregTech_API.registerCover(GregtechItemList.Conveyor_Module_LuV.get(1L, new Object[0]), new GT_MultiTexture(new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[1][0], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_CONVEYOR)}), new GT_Cover_Conveyor(400));
			GregTech_API.registerCover(GregtechItemList.Conveyor_Module_ZPM.get(1L, new Object[0]), new GT_MultiTexture(new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[2][0], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_CONVEYOR)}), new GT_Cover_Conveyor(100));
			GregTech_API.registerCover(GregtechItemList.Conveyor_Module_UV.get(1L, new Object[0]), new GT_MultiTexture(new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[3][0], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_CONVEYOR)}), new GT_Cover_Conveyor(20));
			GregTech_API.registerCover(GregtechItemList.Conveyor_Module_MAX.get(1L, new Object[0]), new GT_MultiTexture(new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[4][0], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_CONVEYOR)}), new GT_Cover_Conveyor(4));
			tLastID = 12;
			GregtechItemList.Electric_Piston_LuV.set(this.addItem(tLastID = 12, "Electric Piston (LuV)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 1L), getTcAspectStack(TC_Aspects.MACHINA, 2L), getTcAspectStack(TC_Aspects.MOTUS, 1L)}));
			GregtechItemList.Electric_Piston_ZPM.set(this.addItem(tLastID = 13, "Electric Piston (ZPM)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 2L), getTcAspectStack(TC_Aspects.MACHINA, 4L), getTcAspectStack(TC_Aspects.MOTUS, 2L)}));
			GregtechItemList.Electric_Piston_UV.set(this.addItem(tLastID = 14, "Electric Piston (UV)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 4L), getTcAspectStack(TC_Aspects.MACHINA, 8L), getTcAspectStack(TC_Aspects.MOTUS, 4L)}));
			GregtechItemList.Electric_Piston_MAX.set(this.addItem(tLastID = 15, "Electric Piston (MAX)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 8L), getTcAspectStack(TC_Aspects.MACHINA, 16L), getTcAspectStack(TC_Aspects.MOTUS, 8L)}));
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Electric_Piston_LuV.get(1L, new Object[0]), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"PPP", "CSS", "CMG", Character.valueOf('P'), OrePrefixes.plate.get(Materials.Steel), Character.valueOf('S'), OrePrefixes.stick.get(Materials.Steel), Character.valueOf('G'), OrePrefixes.gearGtSmall.get(Materials.Steel), Character.valueOf('M'), GregtechItemList.Electric_Motor_LuV, Character.valueOf('C'), OrePrefixes.cableGt01.get(Materials.Tin)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Electric_Piston_ZPM.get(1L, new Object[0]), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"PPP", "CSS", "CMG", Character.valueOf('P'), OrePrefixes.plate.get(Materials.Aluminium), Character.valueOf('S'), OrePrefixes.stick.get(Materials.Aluminium), Character.valueOf('G'), OrePrefixes.gearGtSmall.get(Materials.Aluminium), Character.valueOf('M'), GregtechItemList.Electric_Motor_ZPM, Character.valueOf('C'), OrePrefixes.cableGt01.get(Materials.AnyCopper)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Electric_Piston_UV.get(1L, new Object[0]), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"PPP", "CSS", "CMG", Character.valueOf('P'), OrePrefixes.plate.get(Materials.StainlessSteel), Character.valueOf('S'), OrePrefixes.stick.get(Materials.StainlessSteel), Character.valueOf('G'), OrePrefixes.gearGtSmall.get(Materials.StainlessSteel), Character.valueOf('M'), GregtechItemList.Electric_Motor_UV, Character.valueOf('C'), OrePrefixes.cableGt01.get(Materials.Gold)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Electric_Piston_MAX.get(1L, new Object[0]), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"PPP", "CSS", "CMG", Character.valueOf('P'), OrePrefixes.plate.get(Materials.Titanium), Character.valueOf('S'), OrePrefixes.stick.get(Materials.Titanium), Character.valueOf('G'), OrePrefixes.gearGtSmall.get(Materials.Titanium), Character.valueOf('M'), GregtechItemList.Electric_Motor_MAX, Character.valueOf('C'), OrePrefixes.cableGt01.get(Materials.Aluminium)});
			tLastID = 16;
			GregtechItemList.Robot_Arm_LuV.set(this.addItem(tLastID = 16, "Robot Arm (LuV)", "Inserts into specific Slots (as Cover)", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 1L), getTcAspectStack(TC_Aspects.MACHINA, 2L), getTcAspectStack(TC_Aspects.MOTUS, 1L), Utils.getTcAspectStack("COGNITIO", 1L)}));
			GregtechItemList.Robot_Arm_ZPM.set(this.addItem(tLastID = 17, "Robot Arm (ZPM)", "Inserts into specific Slots (as Cover)", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 2L), getTcAspectStack(TC_Aspects.MACHINA, 4L), getTcAspectStack(TC_Aspects.MOTUS, 2L), getTcAspectStack("COGNITIO", 2L)}));
			GregtechItemList.Robot_Arm_UV.set(this.addItem(tLastID = 18, "Robot Arm (UV)", "Inserts into specific Slots (as Cover)", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 4L), getTcAspectStack(TC_Aspects.MACHINA, 8L), getTcAspectStack(TC_Aspects.MOTUS, 4L), getTcAspectStack("COGNITIO", 4L)}));
			GregtechItemList.Robot_Arm_MAX.set(this.addItem(tLastID = 19, "Robot Arm (MAX)", "Inserts into specific Slots (as Cover)", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 8L), getTcAspectStack(TC_Aspects.MACHINA, 16L), getTcAspectStack(TC_Aspects.MOTUS, 8L), getTcAspectStack("COGNITIO", 8L)}));
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Robot_Arm_LuV.get(1L, new Object[0]), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"CCC", "MSM", "PES", Character.valueOf('S'), OrePrefixes.stick.get(Materials.Steel), Character.valueOf('M'), GregtechItemList.Electric_Motor_LuV, Character.valueOf('P'), GregtechItemList.Electric_Piston_LuV, Character.valueOf('E'), OrePrefixes.circuit.get(Materials.Basic), Character.valueOf('C'), OrePrefixes.cableGt01.get(Materials.Tin)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Robot_Arm_ZPM.get(1L, new Object[0]), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"CCC", "MSM", "PES", Character.valueOf('S'), OrePrefixes.stick.get(Materials.Aluminium), Character.valueOf('M'), GregtechItemList.Electric_Motor_ZPM, Character.valueOf('P'), GregtechItemList.Electric_Piston_ZPM, Character.valueOf('E'), OrePrefixes.circuit.get(Materials.Good), Character.valueOf('C'), OrePrefixes.cableGt01.get(Materials.AnyCopper)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Robot_Arm_UV.get(1L, new Object[0]), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"CCC", "MSM", "PES", Character.valueOf('S'), OrePrefixes.stick.get(Materials.StainlessSteel), Character.valueOf('M'), GregtechItemList.Electric_Motor_UV, Character.valueOf('P'), GregtechItemList.Electric_Piston_UV, Character.valueOf('E'), OrePrefixes.circuit.get(Materials.Advanced), Character.valueOf('C'), OrePrefixes.cableGt01.get(Materials.Gold)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Robot_Arm_MAX.get(1L, new Object[0]), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"CCC", "MSM", "PES", Character.valueOf('S'), OrePrefixes.stick.get(Materials.Titanium), Character.valueOf('M'), GregtechItemList.Electric_Motor_MAX, Character.valueOf('P'), GregtechItemList.Electric_Piston_MAX, Character.valueOf('E'), OrePrefixes.circuit.get(Materials.Elite), Character.valueOf('C'), OrePrefixes.cableGt01.get(Materials.Aluminium)});
			GregTech_API.registerCover(GregtechItemList.Robot_Arm_LuV.get(1L, new Object[0]), new GT_MultiTexture(new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[1][0], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_ARM)}), new GT_Cover_Arm(400));
			GregTech_API.registerCover(GregtechItemList.Robot_Arm_ZPM.get(1L, new Object[0]), new GT_MultiTexture(new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[2][0], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_ARM)}), new GT_Cover_Arm(100));
			GregTech_API.registerCover(GregtechItemList.Robot_Arm_UV.get(1L, new Object[0]), new GT_MultiTexture(new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[3][0], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_ARM)}), new GT_Cover_Arm(20));
			GregTech_API.registerCover(GregtechItemList.Robot_Arm_MAX.get(1L, new Object[0]), new GT_MultiTexture(new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[4][0], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_ARM)}), new GT_Cover_Arm(4));
			tLastID = 20;
			GregtechItemList.Field_Generator_LuV.set(this.addItem(tLastID = 20, "Field Generator (LuV)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 2L), getTcAspectStack(TC_Aspects.MACHINA, 1L), getTcAspectStack(TC_Aspects.TUTAMEN, 1L)}));
			GregtechItemList.Field_Generator_ZPM.set(this.addItem(tLastID = 21, "Field Generator (ZPM)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 4L), getTcAspectStack(TC_Aspects.MACHINA, 2L), getTcAspectStack(TC_Aspects.TUTAMEN, 2L)}));
			GregtechItemList.Field_Generator_UV.set(this.addItem(tLastID = 22, "Field Generator (UV)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 8L), getTcAspectStack(TC_Aspects.MACHINA, 4L), getTcAspectStack(TC_Aspects.TUTAMEN, 4L)}));
			GregtechItemList.Field_Generator_MAX.set(this.addItem(tLastID = 23, "Field Generator (MAX)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 16L), getTcAspectStack(TC_Aspects.MACHINA, 8L), getTcAspectStack(TC_Aspects.TUTAMEN, 8L)}));
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Field_Generator_LuV.get(1L, new Object[0]), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"WCW", "CGC", "WCW", Character.valueOf('G'), OrePrefixes.gem.get(Materials.EnderPearl), Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Basic), Character.valueOf('W'), OrePrefixes.wireGt01.get(Materials.Osmium)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Field_Generator_ZPM.get(1L, new Object[0]), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"WCW", "CGC", "WCW", Character.valueOf('G'), OrePrefixes.gem.get(Materials.EnderEye), Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Good), Character.valueOf('W'), OrePrefixes.wireGt02.get(Materials.Osmium)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Field_Generator_UV.get(1L, new Object[0]), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"WCW", "CGC", "WCW", Character.valueOf('G'), OrePrefixes.gem.get(Materials.NetherStar), Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Advanced), Character.valueOf('W'), OrePrefixes.wireGt04.get(Materials.Osmium)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Field_Generator_MAX.get(1L, new Object[0]), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"WCW", "CGC", "WCW", Character.valueOf('G'), OrePrefixes.gem.get(Materials.NetherStar), Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Elite), Character.valueOf('W'), OrePrefixes.wireGt08.get(Materials.Osmium)});
			tLastID = 24;
			GregtechItemList.Emitter_LuV.set(this.addItem(tLastID = 24, "Emitter (LuV)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 1L), getTcAspectStack(TC_Aspects.MACHINA, 1L), getTcAspectStack(TC_Aspects.LUX, 1L)}));
			GregtechItemList.Emitter_ZPM.set(this.addItem(tLastID = 25, "Emitter (ZPM)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 2L), getTcAspectStack(TC_Aspects.MACHINA, 2L), getTcAspectStack(TC_Aspects.LUX, 2L)}));
			GregtechItemList.Emitter_UV.set(this.addItem(tLastID = 26, "Emitter (UV)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 4L), getTcAspectStack(TC_Aspects.MACHINA, 4L), getTcAspectStack(TC_Aspects.LUX, 4L)}));
			GregtechItemList.Emitter_MAX.set(this.addItem(tLastID = 27, "Emitter (MAX)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 8L), getTcAspectStack(TC_Aspects.MACHINA, 8L), getTcAspectStack(TC_Aspects.LUX, 8L)}));
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Emitter_LuV.get(1L, new Object[0]), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"SSC", "WQS", "CWS", Character.valueOf('Q'), OrePrefixes.gem.get(Materials.Quartzite), Character.valueOf('S'), OrePrefixes.stick.get(Materials.Brass), Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Basic), Character.valueOf('W'), OrePrefixes.cableGt01.get(Materials.Tin)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Emitter_ZPM.get(1L, new Object[0]), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"SSC", "WQS", "CWS", Character.valueOf('Q'), OrePrefixes.gem.get(Materials.NetherQuartz), Character.valueOf('S'), OrePrefixes.stick.get(Materials.Electrum), Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Good), Character.valueOf('W'), OrePrefixes.cableGt01.get(Materials.AnyCopper)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Emitter_UV.get(1L, new Object[0]), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"SSC", "WQS", "CWS", Character.valueOf('Q'), OrePrefixes.gem.get(Materials.Emerald), Character.valueOf('S'), OrePrefixes.stick.get(Materials.Chrome), Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Advanced), Character.valueOf('W'), OrePrefixes.cableGt01.get(Materials.Gold)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Emitter_MAX.get(1L, new Object[0]), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"SSC", "WQS", "CWS", Character.valueOf('Q'), OrePrefixes.gem.get(Materials.EnderPearl), Character.valueOf('S'), OrePrefixes.stick.get(Materials.Platinum), Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Elite), Character.valueOf('W'), OrePrefixes.cableGt01.get(Materials.Aluminium)});
			tLastID = 28;
			GregtechItemList.Sensor_LuV.set(this.addItem(tLastID = 28, "Sensor (LuV)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 1L), getTcAspectStack(TC_Aspects.MACHINA, 1L), getTcAspectStack(TC_Aspects.SENSUS, 1L)}));
			GregtechItemList.Sensor_ZPM.set(this.addItem(tLastID = 29, "Sensor (ZPM)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 2L), getTcAspectStack(TC_Aspects.MACHINA, 2L), getTcAspectStack(TC_Aspects.SENSUS, 2L)}));
			GregtechItemList.Sensor_UV.set(this.addItem(tLastID = 30, "Sensor (UV)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 4L), getTcAspectStack(TC_Aspects.MACHINA, 4L), getTcAspectStack(TC_Aspects.SENSUS, 4L)}));
			GregtechItemList.Sensor_MAX.set(this.addItem(tLastID = 31, "Sensor (MAX)", "", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 8L), getTcAspectStack(TC_Aspects.MACHINA, 8L), getTcAspectStack(TC_Aspects.SENSUS, 8L)}));
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Sensor_LuV.get(1L, new Object[0]), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"P Q", "PS ", "CPP", Character.valueOf('Q'), OrePrefixes.gem.get(Materials.Quartzite), Character.valueOf('S'), OrePrefixes.stick.get(Materials.Brass), Character.valueOf('P'), OrePrefixes.plate.get(Materials.Steel), Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Basic)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Sensor_ZPM.get(1L, new Object[0]), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"P Q", "PS ", "CPP", Character.valueOf('Q'), OrePrefixes.gem.get(Materials.NetherQuartz), Character.valueOf('S'), OrePrefixes.stick.get(Materials.Electrum), Character.valueOf('P'), OrePrefixes.plate.get(Materials.Aluminium), Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Good)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Sensor_UV.get(1L, new Object[0]), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"P Q", "PS ", "CPP", Character.valueOf('Q'), OrePrefixes.gem.get(Materials.Emerald), Character.valueOf('S'), OrePrefixes.stick.get(Materials.Chrome), Character.valueOf('P'), OrePrefixes.plate.get(Materials.StainlessSteel), Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Advanced)});
			//GT_ModHandler.addCraftingRecipe(GregtechItemList.Sensor_MAX.get(1L, new Object[0]), //GT_ModHandler.RecipeBits.DISMANTLEABLE | //GT_ModHandler.RecipeBits.NOT_REMOVABLE | //GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"P Q", "PS ", "CPP", Character.valueOf('Q'), OrePrefixes.gem.get(Materials.EnderPearl), Character.valueOf('S'), OrePrefixes.stick.get(Materials.Platinum), Character.valueOf('P'), OrePrefixes.plate.get(Materials.Titanium), Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Elite)});

			//Handler for ULV Components
			registerComponents_ULV();		


		}

		else {
			//Simplify life.
			registerComponents_ULV();	
			registerComponents_MAX();
		}

		if (!CORE.GTNH){
			GregtechItemList.Circuit_IV.set(this.addItem(tLastID = 704, "Symbiotic Circuit (IV)", "A Symbiotic Data Processor", new Object[]{GregtechOrePrefixes.circuit.get(GT_Materials.Symbiotic)}));
			GregtechItemList.Circuit_LuV.set(this.addItem(tLastID = 705, "Neutronic Circuit (LuV)", "A Neutron Particle Processor", new Object[]{GregtechOrePrefixes.circuit.get(GT_Materials.Neutronic)}));
			GregtechItemList.Circuit_ZPM.set(this.addItem(tLastID = 706, "Quantum Circuit (ZPM)", "A Singlularity Processor", new Object[]{GregtechOrePrefixes.circuit.get(GT_Materials.Quantum)}));
			GregtechItemList.Circuit_Board_IV.set(this.addItem(tLastID = 710, "IV Circuit Board", "An IV Voltage Rated Circuit Board", new Object[0]));
			GregtechItemList.Circuit_Board_LuV.set(this.addItem(tLastID = 711, "LuV Circuit Board", "An LuV Voltage Rated Circuit Board", new Object[0]));
			GregtechItemList.Circuit_Board_ZPM.set(this.addItem(tLastID = 712, "ZPM Processor Board", "A ZPM Voltage Rated Processor Board", new Object[0]));
			GregtechItemList.Circuit_Parts_Crystal_Chip_IV.set(this.addItem(tLastID = 713, "(IV) Energized Crystal Chip", "Needed for Circuits", new Object[0]));
			GregtechItemList.Circuit_Parts_Crystal_Chip_LuV.set(this.addItem(tLastID = 714, "(LuV) Neutron based Microchip", "Needed for Circuits", new Object[0]));
			GregtechItemList.Circuit_Parts_Crystal_Chip_ZPM.set(this.addItem(tLastID = 715, "(ZPM) Quantum Chip", "Needed for Circuits", new Object[0]));
			GregtechItemList.Circuit_Parts_IV.set(this.addItem(tLastID = 716, "(IV) Energized Circuit Parts", "Circuit Parts", new Object[0]));
			GregtechItemList.Circuit_Parts_LuV.set(this.addItem(tLastID = 717, "(LuV) Neutron-based Circuit Parts", "Circuit Parts", new Object[0]));
			GregtechItemList.Circuit_Parts_ZPM.set(this.addItem(tLastID = 718, "(ZPM) Quantum Circuit Parts", "Circuit Parts", new Object[0]));
			GregtechItemList.Circuit_Parts_Wiring_IV.set(this.addItem(tLastID = 719, "Etched IV Voltage Wiring", "Part of Circuit Boards", new Object[0]));
			GregtechItemList.Circuit_Parts_Wiring_LuV.set(this.addItem(tLastID = 720, "Etched LuV Voltage Wiring", "Part of Circuit Boards", new Object[0]));
			GregtechItemList.Circuit_Parts_Wiring_ZPM.set(this.addItem(tLastID = 721, "Etched ZPM Voltage Wiring", "Part of Circuit Boards", new Object[0]));

			if (CORE.configSwitches.enableCustomCircuits){
				ItemUtils.addItemToOreDictionary(GregtechItemList.Circuit_IV.get(1), "circuitSuperconductor");
				ItemUtils.addItemToOreDictionary(GregtechItemList.Circuit_LuV.get(1), "circuitInfinite");
			}
		}

		//Extruder Shape
		GregtechItemList.Shape_Extruder_WindmillShaft.set(this.addItem(tLastID = 40, "Extruder Shape (Shaft)", "Extruder Shape for making Windmill Shafts", new Object[0]));


		//Batteries
		GregtechItemList.Battery_RE_EV_Sodium.set(this.addItem(tLastID = 50, "Quad Cell Sodium Battery", "Reusable", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 2L), getTcAspectStack(TC_Aspects.METALLUM, 2L), getTcAspectStack(TC_Aspects.POTENTIA, 2L)}));
		this.setElectricStats(32000 + tLastID, 3200000L, GT_Values.V[4], 4L, -3L, true);

		GregtechItemList.Battery_RE_EV_Cadmium.set(this.addItem(tLastID = 52, "Quad Cell Cadmium Battery", "Reusable", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 1L), getTcAspectStack(TC_Aspects.METALLUM, 1L), getTcAspectStack(TC_Aspects.POTENTIA, 1L)}));
		this.setElectricStats(32000 + tLastID, 4800000L, GT_Values.V[4], 4L, -3L, true);

		GregtechItemList.Battery_RE_EV_Lithium.set(this.addItem(tLastID = 54, "Quad Cell Lithium Battery", "Reusable", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 3L), getTcAspectStack(TC_Aspects.METALLUM, 3L), getTcAspectStack(TC_Aspects.POTENTIA, 3L)}));
		this.setElectricStats(32000 + tLastID, 6400000L, GT_Values.V[4], 4L, -3L, true);

		/*GregtechItemList.Battery_RE_EV_Sodium.set(addItem(tLastID = 50, "Quad Cell Acid Battery", "Reusable", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 1L), getTcAspectStack(TC_Aspects.METALLUM, 1L), getTcAspectStack(TC_Aspects.POTENTIA, 1L)}));
        setElectricStats(32000 + tLastID, 5000000L, GT_Values.V[2], 4L, -3L, true);

        GregtechItemList.Battery_RE_EV_Sodium.set(addItem(tLastID = 50, "Quad Cell Mercury Battery", "Reusable", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 1L), getTcAspectStack(TC_Aspects.METALLUM, 1L), getTcAspectStack(TC_Aspects.POTENTIA, 1L)}));
        setElectricStats(32000 + tLastID, 5000000L, GT_Values.V[2], 4L, -3L, true);*/

		//RTG Pellet
		GregtechItemList.Pellet_RTG_PU238.set(this.addItem(41, StringUtils.superscript("238")+"Pu Pellet", "", new Object[]{getTcAspectStack(TC_Aspects.RADIO, 4L), getTcAspectStack(TC_Aspects.POTENTIA, 2L), getTcAspectStack(TC_Aspects.METALLUM, 2L)}));
		GregtechItemList.Pellet_RTG_SR90.set(this.addItem(42, StringUtils.superscript("90")+"Sr Pellet", "", new Object[]{getTcAspectStack(TC_Aspects.RADIO, 4L), getTcAspectStack(TC_Aspects.POTENTIA, 2L), getTcAspectStack(TC_Aspects.METALLUM, 2L)}));
		GregtechItemList.Pellet_RTG_PO210.set(this.addItem(43, StringUtils.superscript("210")+"Po Pellet", "", new Object[]{getTcAspectStack(TC_Aspects.RADIO, 4L), getTcAspectStack(TC_Aspects.POTENTIA, 2L), getTcAspectStack(TC_Aspects.METALLUM, 2L)}));
		GregtechItemList.Pellet_RTG_AM241.set(this.addItem(44, StringUtils.superscript("241")+"Am Pellet", "", new Object[]{getTcAspectStack(TC_Aspects.RADIO, 4L), getTcAspectStack(TC_Aspects.POTENTIA, 2L), getTcAspectStack(TC_Aspects.METALLUM, 2L)}));

		//Computer Cube
		GregtechItemList.Gregtech_Computer_Cube.set(this.addItem(tLastID = 55, "Gregtech Computer Cube", "Reusable", new Object[]{getTcAspectStack(TC_Aspects.ELECTRUM, 8L), getTcAspectStack(TC_Aspects.METALLUM, 8L), getTcAspectStack(TC_Aspects.POTENTIA, 8L)}));
		this.setElectricStats(32000 + tLastID, Integer.MAX_VALUE, GT_Values.V[5], 5L, -3L, true);
		
		//FOOOOOOOOOOOOOOD
		GregtechItemList.Food_Baked_Raisin_Bread.set(this.addItem(tLastID = 60, "Raisin Bread", "Extra Raisins, Just for ImQ009", new Object[]{new GT_FoodStat(5, 0.5F, EnumAction.eat, null, false, true, false, new int[0]), getTcAspectStack(TC_Aspects.CORPUS, 1L), getTcAspectStack(TC_Aspects.FAMES, 1L), getTcAspectStack(TC_Aspects.IGNIS, 1L)}));

		//Old Circuits
		if (CORE.configSwitches.enableOldGTcircuits && CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK){
			registerOldCircuits();
		}

		if (!CORE.GTNH) {
			GregtechItemList.Fluid_Cell_144L.set(this.addItem(tLastID = 61, "144L Invar Fluid Cell", "Holds exactly one dust worth of liquid.", new Object[]{new ItemData(Materials.Invar, (OrePrefixes.plate.mMaterialAmount * 8L) + (4L * OrePrefixes.ring.mMaterialAmount), new MaterialStack[0]), getTcAspectStack(TC_Aspects.VACUOS, 2L), getTcAspectStack(TC_Aspects.AQUA, 1L)}));
			this.setFluidContainerStats(32000 + tLastID, 144L, 64L);

			GregtechItemList.Fluid_Cell_36L.set(this.addItem(tLastID = 62, "36L Brass Fluid Cell", "Holds exactly one small dust worth of liquid.", new Object[]{new ItemData(Materials.Brass, (OrePrefixes.plate.mMaterialAmount * 8L) + (4L * OrePrefixes.ring.mMaterialAmount), new MaterialStack[0]), getTcAspectStack(TC_Aspects.VACUOS, 2L), getTcAspectStack(TC_Aspects.AQUA, 1L)}));
			this.setFluidContainerStats(32000 + tLastID, 36L, 64L);

			GregtechItemList.Fluid_Cell_16L.set(this.addItem(tLastID = 63, "16L Bronze Fluid Cell", "Holds exactly one tiny dust / nugget worth of liquid.", new Object[]{new ItemData(Materials.Bronze, (OrePrefixes.plate.mMaterialAmount * 8L) + (4L * OrePrefixes.ring.mMaterialAmount), new MaterialStack[0]), getTcAspectStack(TC_Aspects.VACUOS, 2L), getTcAspectStack(TC_Aspects.AQUA, 1L)}));
			this.setFluidContainerStats(32000 + tLastID, 16L, 64L);

			GregtechItemList.Fluid_Cell_1L.set(this.addItem(tLastID = 64, "1L Wrought Iron Fluid Cell", "Holds exactly one litre worth of liquid.", new Object[]{new ItemData(Materials.WroughtIron, (OrePrefixes.plate.mMaterialAmount * 8L) + (4L * OrePrefixes.ring.mMaterialAmount), new MaterialStack[0]), getTcAspectStack(TC_Aspects.VACUOS, 2L), getTcAspectStack(TC_Aspects.AQUA, 1L)}));
			this.setFluidContainerStats(32000 + tLastID, 1L, 64L);

			if (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK) {

				GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.WroughtIron, 1L), GT_OreDictUnificator.get(OrePrefixes.ring, Materials.WroughtIron, 2L), GregtechItemList.Fluid_Cell_1L.get(1L, new Object[0]), 50, 32);
				GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Bronze, 1L), GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Bronze, 2L), GregtechItemList.Fluid_Cell_16L.get(1L, new Object[0]), 50, 32);
				GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Brass, 1L), GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Brass, 2L), GregtechItemList.Fluid_Cell_36L.get(1L, new Object[0]), 75, 32);
				GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Invar, 1L), GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Invar, 2L), GregtechItemList.Fluid_Cell_144L.get(1L, new Object[0]), 75, 32);

			} else {
				GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 8L), GT_OreDictUnificator.get(OrePrefixes.ring, Materials.WroughtIron, 4L), GregtechItemList.Fluid_Cell_1L.get(1L, new Object[0]), 50, 32);
				GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Bronze, 8L), GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Bronze, 4L), GregtechItemList.Fluid_Cell_16L.get(1L, new Object[0]), 50, 32);
				GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Brass, 8L), GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Brass, 4L), GregtechItemList.Fluid_Cell_36L.get(1L, new Object[0]), 75, 32);
				GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Invar, 8L), GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Invar, 4L), GregtechItemList.Fluid_Cell_144L.get(1L, new Object[0]), 75, 32);
			}
		}
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

		GregTech_API.registerCover(GregtechItemList.Electric_Pump_ULV.get(1L, new Object[0]), new GT_MultiTexture(new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[5][0], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_PUMP)}), new GT_Cover_Pump(8));
		GregTech_API.registerCover(GregtechItemList.Conveyor_Module_ULV.get(1L, new Object[0]), new GT_MultiTexture(new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[1][0], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_CONVEYOR)}), new GT_Cover_Conveyor(1600));
		GregTech_API.registerCover(GregtechItemList.Robot_Arm_ULV.get(1L, new Object[0]), new GT_MultiTexture(new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[1][0], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_ARM)}), new GT_Cover_Arm(1600));

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

		GregTech_API.registerCover(GregtechItemList.Electric_Pump_MAX.get(1L, new Object[0]), new GT_MultiTexture(new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[8][0], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_PUMP)}), new GT_Cover_Pump(524288));
		GregTech_API.registerCover(GregtechItemList.Conveyor_Module_MAX.get(1L, new Object[0]), new GT_MultiTexture(new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[4][0], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_CONVEYOR)}), new GT_Cover_Conveyor(4));
		GregTech_API.registerCover(GregtechItemList.Robot_Arm_MAX.get(1L, new Object[0]), new GT_MultiTexture(new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[4][0], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_ARM)}), new GT_Cover_Arm(4));


		return true;
	}

	private boolean registerOldCircuits(){
		//Enable Old Circuits
		Utils.LOG_INFO("[Old Feature - Circuits] Enabling Pre-5.09.28 Circuits and Data Storage.");

		GregtechItemList.Old_Circuit_Primitive.set(this.addItem(200, "NAND Chip", "A very simple Circuit", new Object[]{OrePrefixes.circuit.get(Materials.Primitive)}));
		GregtechItemList.Old_Circuit_Basic.set(this.addItem(201, "Basic Electronic Circuit", "A basic Circuit", new Object[]{OrePrefixes.circuit.get(Materials.Basic)}));
		GregtechItemList.Old_Circuit_Good.set(this.addItem(202, "Good Electronic Circuit", "A good Circuit", new Object[]{OrePrefixes.circuit.get(Materials.Good)}));
		GregtechItemList.Old_Circuit_Advanced.set(this.addItem(203, "Advanced Circuit", "An advanced Circuit", new Object[]{OrePrefixes.circuit.get(Materials.Advanced)}));
		GregtechItemList.Old_Circuit_Data.set(this.addItem(204, "Data Storage Circuit", "A Data Storage Chip", new Object[]{OrePrefixes.circuit.get(Materials.Data)}));
		GregtechItemList.Old_Circuit_Elite.set(this.addItem(205, "Data Control Circuit", "A Processor", new Object[]{OrePrefixes.circuit.get(Materials.Elite)}));
		GregtechItemList.Old_Circuit_Master.set(this.addItem(206, "Energy Flow Circuit", "A High Voltage Processor", new Object[]{OrePrefixes.circuit.get(Materials.Master)}));

		GregtechItemList.Old_Tool_DataOrb.set(this.addItem(207, "Data Orb [GT++]", "A High Capacity Data Storage", new Object[]{OrePrefixes.circuit.get(Materials.Ultimate), SubTag.NO_UNIFICATION, new Behaviour_DataOrb()}));
		GregtechItemList.Old_Circuit_Ultimate.set(GregtechItemList.Old_Tool_DataOrb.get(1L, new Object[0]));
		GT_ModHandler.addShapelessCraftingRecipe(GregtechItemList.Old_Tool_DataOrb.get(1L, new Object[0]), GT_ModHandler.RecipeBits.NOT_REMOVABLE, new Object[]{GregtechItemList.Old_Tool_DataOrb.get(1L, new Object[0])});

		GregtechItemList.Old_Tool_DataStick.set(this.addItem(208, "Data Stick [GT++]", "A Low Capacity Data Storage", new Object[]{OrePrefixes.circuit.get(Materials.Data), SubTag.NO_UNIFICATION, new Behaviour_DataStick()}));
		GT_ModHandler.addShapelessCraftingRecipe(GregtechItemList.Old_Tool_DataStick.get(1L, new Object[0]), GT_ModHandler.RecipeBits.NOT_REMOVABLE, new Object[]{GregtechItemList.Old_Tool_DataStick.get(1L, new Object[0])});

		GregtechItemList.Old_Circuit_Board_Basic.set(this.addItem(210, "Basic Circuit Board", "A basic Board", new Object[0]));
		GregtechItemList.Old_Circuit_Board_Advanced.set(this.addItem(211, "Advanced Circuit Board", "An advanced Board", new Object[0]));
		GregtechItemList.Old_Circuit_Board_Elite.set(this.addItem(212, "Processor Board", "A Processor Board", new Object[0]));
		GregtechItemList.Old_Circuit_Parts_Crystal_Chip_Elite.set(this.addItem(213, "Engraved Crystal Chip", "Needed for Circuits", new Object[0]));
		GregtechItemList.Old_Circuit_Parts_Crystal_Chip_Master.set(this.addItem(214, "Engraved Lapotron Chip", "Needed for Circuits", new Object[0]));
		GregtechItemList.Old_Circuit_Parts_Advanced.set(this.addItem(215, "Advanced Circuit Parts", "Advanced Circuit Parts", new Object[0]));
		GregtechItemList.Old_Circuit_Parts_Wiring_Basic.set(this.addItem(216, "Etched Medium Voltage Wiring", "Part of Circuit Boards", new Object[0]));
		GregtechItemList.Old_Circuit_Parts_Wiring_Advanced.set(this.addItem(217, "Etched High Voltage Wiring", "Part of Circuit Boards", new Object[0]));
		GregtechItemList.Old_Circuit_Parts_Wiring_Elite.set(this.addItem(218, "Etched Extreme Voltage Wiring", "Part of Circuit Boards", new Object[0]));
		GregtechItemList.Old_Empty_Board_Basic.set(this.addItem(219, "Empty Circuit Board", "A Board Part", new Object[0]));
		GregtechItemList.Old_Empty_Board_Elite.set(this.addItem(220, "Empty Processor Board", "A Processor Board Part", new Object[0]));
		
		return true;
	}
}
