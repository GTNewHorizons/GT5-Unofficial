package gregtech.api.ModernMaterials.Blocks.DumbBase.NewDumb;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.ModernMaterials.Blocks.BlocksEnum;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import java.util.List;

public abstract class NewDumb extends Block {

    List<Integer> validIDs;

    private final int blockIDOffset;

    @Override
    public boolean hasTileEntity(int metadata) {
        return false;
    }

    @Override
    public boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z) {
        return false;
    }

    public NewDumb(int blockIDOffset, List<Integer> validIDs) {
        super(Material.rock);

        setHardness(1.5F);
        setResistance(10.0F);

        this.validIDs = validIDs;
        this.blockIDOffset = blockIDOffset;
    }

    public abstract BlocksEnum getBlockEnum();

    @Override
    public int getRenderType() {
        return getBlockEnum().getRenderId();
    }

    @Override
    public int getRenderBlockPass() {
        return 0;
    }


    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (int ID : validIDs) {
            list.add(new ItemStack(item, 1,  ID % 16));
        }
    }

    public int getMaterialID(int metadata) {
        return blockIDOffset * 16 + metadata;
    }

    @Override
    public int damageDropped(int meta)
    {
        return meta;
    }
}
