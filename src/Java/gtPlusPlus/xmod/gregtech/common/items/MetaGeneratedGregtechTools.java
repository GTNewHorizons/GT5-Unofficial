package gtPlusPlus.xmod.gregtech.common.items;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.TC_Aspects;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.interfaces.IItemBehaviour;
import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.util.GT_Log;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechToolDictNames;
import gtPlusPlus.xmod.gregtech.common.tools.TOOL_Gregtech_Choocher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public class MetaGeneratedGregtechTools extends GT_MetaGenerated_Tool {

	public static final short SKOOKUM_CHOOCHER = 7734;
	public static GT_MetaGenerated_Tool INSTANCE;

	public MetaGeneratedGregtechTools() {
		super("plusplus.metatool.01");
		INSTANCE = this;
		//Skookum Choocher
		GregTech_API.registerTool(
				this.addTool(
						SKOOKUM_CHOOCHER, "Skookum Choocher",
						"Can Really Chooch. Does a Skookum job at Hammering and Wrenching stuff.",
						new TOOL_Gregtech_Choocher(),
						new Object[]{GregtechToolDictNames.craftingToolSkookumChoocher,
								ToolDictNames.craftingToolHardHammer,
								ToolDictNames.craftingToolWrench,
								new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
								new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 2L),
								new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 2L)}),
				GregTech_API.sWrenchList);
	}	

}
