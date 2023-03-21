package gregtech.api.ModernMaterials.Blocks.DumbBase;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.ModernMaterials.Blocks.BlocksEnum;
import gregtech.api.ModernMaterials.Blocks.FrameBox.FrameBoxBlock;
import gregtech.api.ModernMaterials.ModernMaterial;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class DumbBlock extends BlockContainer {

    public abstract BlocksEnum getBlockEnum();

    protected DumbBlock() {
        super(Material.rock);
        setBlockName(getBlockEnum().name());
        setHardness(1.5F);
        setResistance(10.0F);
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    public void registerBlock(Class<? extends DumbTileEntity> dumbTileEntity, Class<? extends DumbItemBlock> dumbItemBlock) {
        GameRegistry.registerBlock(new FrameBoxBlock(), dumbItemBlock, getBlockEnum().name());
        GameRegistry.registerTileEntity(dumbTileEntity, getBlockEnum().name());
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        for (ModernMaterial modernMaterial : getBlockEnum().associatedMaterials) {
            list.add(new ItemStack(item, 1, modernMaterial.getMaterialID()));
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
