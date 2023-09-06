package gregtech.api.ModernMaterials.Blocks.DumbBase;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.ModernMaterials.Blocks.BlocksEnum;
import gregtech.api.ModernMaterials.Blocks.FrameBox.FrameBoxTileEntity;
import gregtech.api.ModernMaterials.ModernMaterial;
import gregtech.api.ModernMaterials.ModernMaterialUtilities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
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
        GameRegistry.registerBlock(this, dumbItemBlock, getBlockEnum().name());
        GameRegistry.registerTileEntity(dumbTileEntity, getBlockEnum().name());
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {

        getBlockEnum().setAssociatedItem(item);

        for (ModernMaterial modernMaterial : getBlockEnum().getAssociatedMaterials()) {
            list.add(new ItemStack(item, 1, modernMaterial.getMaterialID()));
        }
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack itemStack) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        final DumbTileEntity dumbTileEntity = (DumbTileEntity) tileEntity;

        dumbTileEntity.setMaterialID(itemStack.getItemDamage());
        dumbTileEntity.markDirty();
        world.markBlockForUpdate(x, y, z);
    }

    @Override
    public boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {

        // Create a new ItemStack with the saved materialID. This bypasses
        // the 4 bit storage limit in the blocks damage value.
        ArrayList<ItemStack> itemList = new ArrayList<>();
        itemList.add(getDroppedItemStack(world, x, y, z));

        // Destroy the block.
        world.setBlock(x, y, z, Blocks.air);

        return itemList;
    }

    @Override
    public boolean removedByPlayer(World aWorld, EntityPlayer aPlayer, int x, int y, int z, boolean aWillHarvest) {
        // This delays deletion of the block until after getDrops
        return aWillHarvest || super.removedByPlayer(aWorld, aPlayer, x, y, z, false);
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
        // Return the actual item when pick block is used in creative.
        return getDroppedItemStack(world, x, y, z);
    }

    public ItemStack getDroppedItemStack(World world, int x, int y, int z) {
        return new ItemStack(this, 1, getMaterialID(world, x, y, z));
    }

    public int getMaterialID(World world, int x, int y, int z) {
        DumbTileEntity dumbTileEntity = (DumbTileEntity) world.getTileEntity(x, y, z);
        return dumbTileEntity.getMaterialID();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int colorMultiplier(IBlockAccess worldIn, int x, int y, int z)
    {
        final DumbTileEntity dumbTileEntity = (DumbTileEntity) worldIn.getTileEntity(x, y, z);
        final int materialID = dumbTileEntity.getMaterialID();
        final ModernMaterial material = ModernMaterialUtilities.materialIDToMaterial.get(materialID);

        if (material == null) return new Color(100, 100, 0, 255).getRGB();
        return material.getColor().getRGB();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getUnlocalizedName() {
        return super.getUnlocalizedName();
    }

    @Override
    public String getLocalizedName() {
        return super.getLocalizedName();
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new FrameBoxTileEntity();
    }
}
