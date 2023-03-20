package gregtech.api.ModernMaterials.Blocks.DumbBase;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;

public class DumbBlock extends BlockContainer {

    public static final String NAME = "frame_box_ji2ojmd";

    protected DumbBlock() {
        super(Material.rock);
        setBlockName(NAME);
        setCreativeTab(CreativeTabs.tabBlock);
        setHardness(1.5F);
        setResistance(10.0F);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new DumbTileEntity();
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    public static void register() {
        Block block = new DumbBlock();
        GameRegistry.registerBlock(block, DumbItemBlock.class, NAME);
        GameRegistry.registerTileEntity(DumbTileEntity.class, NAME);
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < 160; i++) { // Replace 3 with the number of sub-blocks you have
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack itemStack) {
        final TileEntity tTileEntity = world.getTileEntity(x, y, z);
        final DumbTileEntity tileEntityFrameBox = (DumbTileEntity) tTileEntity;

        tileEntityFrameBox.setMaterialID(itemStack.getItemDamage());
        tileEntityFrameBox.markDirty();
        world.markBlockForUpdate(x, y, z);
    }

    @Override
    public boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {

        // Get the material ID.
        DumbTileEntity tileEntityFrameBox = (DumbTileEntity) world.getTileEntity(x, y, z);
        final int materialID = tileEntityFrameBox.getMaterialID();

        // Destroy the block.
        world.setBlock(x, y, z, Blocks.air);

        // Create a new ItemStack with the saved materialID. This bypasses
        // the 4 bit storage limit in the blocks damage value.
        ArrayList<ItemStack> itemList = new ArrayList<>();
        itemList.add(new ItemStack(this, 1, materialID));

        return itemList;
    }

    @Override
    public boolean removedByPlayer(World aWorld, EntityPlayer aPlayer, int x, int y, int z, boolean aWillHarvest) {
        // This delays deletion of the block until after getDrops
        return aWillHarvest || super.removedByPlayer(aWorld, aPlayer, x, y, z, false);
    }

}
