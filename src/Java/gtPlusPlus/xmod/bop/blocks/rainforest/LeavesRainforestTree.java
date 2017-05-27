package gtPlusPlus.xmod.bop.blocks.rainforest;

import java.util.Random;

import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.xmod.bop.blocks.BOP_Block_Registrator;
import gtPlusPlus.xmod.bop.blocks.base.LeavesBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class LeavesRainforestTree extends LeavesBase {

	public LeavesRainforestTree(){
		super("Rainforest Oak", "rainforestoak", new ItemStack[]{ItemUtils.getSimpleStack(Items.apple)});
		this.treeType = new String[] {"rainforest"};
		this.leafType = new String[][] {{"rainforest"},{"rainforest_opaque"}};
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_){
		return Item.getItemFromBlock(BOP_Block_Registrator.sapling_Rainforest);
	}

}