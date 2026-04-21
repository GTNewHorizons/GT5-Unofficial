package gregtech.common.blocks.rubbertree;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBlockRubberLogNatural extends ItemBlock {

    public ItemBlockRubberLogNatural(Block block) {
        super(block);
        setHasSubtypes(false);
        setMaxDamage(0);
        setCreativeTab(null);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z,
                             int side, float hitX, float hitY, float hitZ) {
        return false;
    }
}
