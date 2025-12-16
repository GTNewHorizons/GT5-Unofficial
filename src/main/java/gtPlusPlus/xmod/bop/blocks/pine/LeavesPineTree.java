package gtPlusPlus.xmod.bop.blocks.pine;

import java.util.Random;

import net.minecraft.item.Item;
import net.minecraft.world.World;

import gregtech.api.enums.GTValues;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.bop.blocks.BOPBlockRegistrator;
import gtPlusPlus.xmod.bop.blocks.base.LeavesBase;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class LeavesPineTree extends LeavesBase {

    public LeavesPineTree() {
        super("Pine", "pine", GTValues.emptyItemStackArray);
        this.treeType = new String[] { "pine" };
        this.leafType = new String[][] { { "pine" }, { "pine_opaque" } };
    }

    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
        return Item.getItemFromBlock(BOPBlockRegistrator.sapling_Pine);
    }

    @Override // Drops when Leaf is broken
    protected void func_150124_c(World world, int x, int y, int z, int meta, int randomChance) {
        Logger.INFO("Dropping Bonus Drops");
        if (MathUtils.randInt(0, 10) >= 9) {
            this.dropBlockAsItem(world, x, y, z, GregtechItemList.Pinecone.get(MathUtils.randInt(1, 4)));
        }
    }
}
