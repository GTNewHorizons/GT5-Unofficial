package gtPlusPlus.core.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import gregtech.api.items.GT_MetaGenerated_Tool;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.util.reflect.ReflectionUtils;

public class SlotToolBox extends SlotGtTool {
	
	private static final AutoMap<Class> mSupportedCustomTools = new AutoMap<Class>();
	
	static {
		//Look for Supported custom tool types
		Class temp;

		//IHL Pumps
		temp = ReflectionUtils.getClassByName("ihl.handpump.IHLHandPump");
		if (temp != null) {
			mSupportedCustomTools.put(temp);
			temp = null;
		}

		//IC2 Electrics
		temp = ReflectionUtils.getClassByName("ic2.api.item.IElectricItem");
		if (temp != null) {
			mSupportedCustomTools.put(temp);
			temp = null;
		}
		
		//IC2 Boxables
		temp = ReflectionUtils.getClassByName(" ic2.api.item.IBoxable");
		if (temp != null) {
			mSupportedCustomTools.put(temp);
			temp = null;
		}
		
		//Tinkers Tools
		temp = ReflectionUtils.getClassByName("tconstruct.library.tools.Weapon");
		if (temp != null) {
			mSupportedCustomTools.put(temp);
			temp = null;
		}
		//BattleGear Weapons
		temp = ReflectionUtils.getClassByName("mods.battlegear2.api.weapons.IBattlegearWeapon");
		if (temp != null) {
			mSupportedCustomTools.put(temp);
			temp = null;
		}
	
		
		//OpenMods	
		String[] OpenModsContent = new String[] {"openblocks.common.item.ItemDevNull", "openblocks.common.item.ItemHangGlider", "openblocks.common.item.ItemWrench", "openblocks.common.item.ItemSleepingBag"};
		for (String t : OpenModsContent) {
			temp = ReflectionUtils.getClassByName(t);
			if (temp != null) {
				mSupportedCustomTools.put(temp);
				temp = null;
			}
		}

		//GC Wrench
		temp = ReflectionUtils.getClassByName("micdoodle8.mods.galacticraft.core.items.ItemUniversalWrench");
		if (temp != null) {
			mSupportedCustomTools.put(temp);
			temp = null;
		}
		
		//EIO
		String[] EioContent = new String[] {"crazypants.enderio.api.tool.ITool", "crazypants.enderio.item.ItemMagnet", "crazypants.enderio.item.ItemConduitProbe"};
		for (String t : EioContent) {
			temp = ReflectionUtils.getClassByName(t);
			if (temp != null) {
				mSupportedCustomTools.put(temp);
				temp = null;
			}
		}
		
		//Forestry
		temp = ReflectionUtils.getClassByName("forestry.core.items.ItemForestryTool");
		if (temp != null) {
			mSupportedCustomTools.put(temp);
			temp = null;
		}
	}
	
	public SlotToolBox(final IInventory base, final int x, final int y, final int z) {
		super(base, x, y, z);
	}

	@Override
	public boolean isItemValid(final ItemStack itemstack) {
		return isItemValid_STATIC(itemstack);
	}
	
	public static boolean isItemValid_STATIC(final ItemStack itemstack) {
		if ((itemstack.getItem() instanceof GT_MetaGenerated_Tool) || (itemstack.getItem() instanceof ItemTool)) {
			Logger.WARNING(itemstack.getDisplayName() + " is a valid Tool.");
			return true;
		}
		for (Class C : mSupportedCustomTools) {
			if (C.isInstance(itemstack.getItem())) {
				return true;
			}
		}
		Logger.WARNING(itemstack.getDisplayName() + " is not a valid Tool.");
		return false;
	}

}
