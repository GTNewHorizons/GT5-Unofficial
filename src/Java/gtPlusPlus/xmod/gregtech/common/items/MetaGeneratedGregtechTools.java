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
import gtPlusPlus.xmod.gregtech.common.tools.TOOL_Gregtech_Pump;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public class MetaGeneratedGregtechTools extends GT_MetaGenerated_Tool {

	public static final short SKOOKUM_CHOOCHER = 7734;
	public static final short HAND_PUMP = 7736;
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

		//Pump
		this.addTool(
				HAND_PUMP,
				"Electric Pump",
				"Holds Fluids from GT Machines.",
				new TOOL_Gregtech_Pump(),
				new Object[]{GregtechToolDictNames.craftingToolHandPump,
						new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
						new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 2L),
						new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 2L)});
		this.setFluidContainerStats(HAND_PUMP, 16000L, 16L);
	}

	

	public final ConcurrentHashMap<Short, Long[]> mFluidContainerStats = new ConcurrentHashMap<Short, Long[]>();
	
	public final MetaGeneratedGregtechTools setFluidContainerStats(int aMetaValue, long aCapacity, long aStacksize) {
		if (aMetaValue >= 0) {
			if (aCapacity > 0L) {
				this.mFluidContainerStats.put(Short.valueOf((short) aMetaValue),
						new Long[]{Long.valueOf(aCapacity), Long.valueOf(Math.max(1L, aStacksize))});
			}
			return this;
		} else {
			return this;
		}
	}

	@Override
	public final Long[] getFluidContainerStats(ItemStack aStack) {
		return (Long[]) this.mFluidContainerStats.get(Short.valueOf((short) aStack.getItemDamage()));
	}
	

	@SuppressWarnings("unchecked")
	private ConcurrentHashMap<Short, ArrayList<IItemBehaviour<GT_MetaBase_Item>>> getItemBehaviours() {
		try {			
			ConcurrentHashMap<Short, ArrayList<IItemBehaviour<GT_MetaBase_Item>>> aItemBehaviors;
			aItemBehaviors = (ConcurrentHashMap<Short, ArrayList<IItemBehaviour<GT_MetaBase_Item>>>) ReflectionUtils.getField(getClass(), "mItemBehaviors").get(this);
			return aItemBehaviors != null ? aItemBehaviors : null;
		} catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
			return null;
		}
	}

	@Override
	public ItemStack onItemRightClick(ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
		Logger.INFO("Item Right Click");
		this.use(aStack, 0.0D, aPlayer);
		this.isItemStackUsable(aStack);
		ArrayList<IItemBehaviour<GT_MetaBase_Item>> tList = (ArrayList<IItemBehaviour<GT_MetaBase_Item>>) this.getItemBehaviours().get(Short.valueOf((short) this.getDamage(aStack)));
		try {
			IItemBehaviour<GT_MetaBase_Item> tBehavior;
			if (tList != null) {				
				for (IItemBehaviour<GT_MetaBase_Item> g : tList) {
					if (g != null) {
						if (g instanceof TOOL_Gregtech_Pump) {
							TOOL_Gregtech_Pump t = (TOOL_Gregtech_Pump) g;
							if (t != null) {
								Logger.INFO("Right clicked with Pump tool.");
								aStack = g.onItemRightClick(this, aStack, aWorld, aPlayer);
								FluidStack f = t.getBehaviour().getStoredFluid();
								if (f != null) {
									Logger.INFO("Found Fluid '"+f.getLocalizedName()+"', trying to set fluid contents of tool.");
									this.setFluidContent(aStack, f);
								}
							}
						}
						else {
							aStack = g.onItemRightClick(this, aStack, aWorld, aPlayer);
						}
					}
				}
			}
		} catch (Throwable arg6) {
			if (GT_Values.D1) {
				arg6.printStackTrace(GT_Log.err);
			}
		}
		return aStack;
	}
	

}
