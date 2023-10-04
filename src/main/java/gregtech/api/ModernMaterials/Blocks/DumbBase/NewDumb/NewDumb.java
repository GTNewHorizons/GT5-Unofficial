package gregtech.api.ModernMaterials.Blocks.DumbBase.NewDumb;

import gregtech.api.ModernMaterials.Blocks.BlocksEnum;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;

import java.util.List;

public class NewDumb extends Block {

    List<Integer> validIDs;
    private final BlocksEnum blockEnum;

    private final int blockIDOffset;

    @Override
    public boolean hasTileEntity(int metadata) {
        return false;
    }

    @Override
    public boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z) {
        return false;
    }

    public NewDumb(BlocksEnum blockEnum, int blockIDOffset, List<Integer> validIDs) {
        super(Material.rock);
        setHardness(1.5F);
        setResistance(10.0F);

        this.validIDs = validIDs;
        this.blockIDOffset = blockIDOffset;
        this.blockEnum = blockEnum;

    }

    public BlocksEnum getBlockEnum() {
        return blockEnum;
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (int ID : validIDs) {
            list.add(new ItemStack(item, 1,  ID));
        }
    }
}
