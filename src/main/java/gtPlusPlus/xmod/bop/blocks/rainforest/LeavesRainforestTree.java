package gtPlusPlus.xmod.bop.blocks.rainforest;

import java.util.Random;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.bop.blocks.BOPBlockRegistrator;
import gtPlusPlus.xmod.bop.blocks.base.LeavesBase;

public class LeavesRainforestTree extends LeavesBase {

    public LeavesRainforestTree() {
        super("Rainforest Oak", "rainforestoak", new ItemStack[] { ItemUtils.getSimpleStack(Items.apple) });
        this.treeType = new String[] { "rainforest" };
        this.leafType = new String[][] { { "rainforest" }, { "rainforest_opaque" } };
    }

    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
        return Item.getItemFromBlock(BOPBlockRegistrator.sapling_Rainforest);
    }
}
