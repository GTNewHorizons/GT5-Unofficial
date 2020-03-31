package gtPlusPlus.preloader.asm.helpers;

import java.util.List;

import gregtech.api.enums.Materials;
import gregtech.api.items.GT_MetaGenerated_Tool;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MethodHelper_GT {

	public static final void getSubItems(GT_MetaGenerated_Tool aTool, Item var1, CreativeTabs aCreativeTab, List aList) {
		for (int i = 0; i < 32766; i += 2) {
			if (aTool.getToolStats(new ItemStack(aTool, 1, i)) != null) {
				ItemStack tStack = new ItemStack(aTool, 1, i);
				aTool.isItemStackUsable(tStack);
				aList.add(tStack);
				aList.add(aTool.getToolWithStats(i,1,Materials.TungstenSteel,Materials.TungstenSteel,null));
				aList.add(aTool.getToolWithStats(i,1,Materials.Neutronium,Materials.Neutronium,null));
			}
		}
	}
	
}
