package gtPlusPlus.xmod.bop.blocks.rainforest;

import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.xmod.bop.blocks.base.LeavesBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class LeavesRainforestTree extends LeavesBase {

    public LeavesRainforestTree(){
    	super("Rainforest Oak", "rainforestoak", new ItemStack[]{ItemUtils.getSimpleStack(Items.apple)});
    	this.treeType = new String[] {"rainforest"};
    	this.leafType = new String[][] {{"rainforest"},{"rainforest_opaque"}};
    }

}