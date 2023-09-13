package gregtech.api.CustomStructureRendering.Base;

import static gregtech.api.CustomStructureRendering.Base.BaseRenderTESR.MODEL_NAME_NBT_TAG;

import java.util.ArrayList;

import gregtech.api.CustomStructureRendering.Trophies.BaseTrophyTileEntity;
import gregtech.api.CustomStructureRendering.Trophies.Trophies;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BaseRenderBlock extends Block {

    public BaseRenderBlock(String name) {
        super(Material.iron);
        this.setResistance(-1);
        this.setHardness(-1);
        this.setBlockName(name);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        blockIcon = iconRegister.registerIcon("gregtech:iconsets/TRANSPARENT");
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean canRenderInPass(int a) {
        return true;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase placer, ItemStack stack) {
        if (stack.hasTagCompound()) {
            // Get the NBT data from the ItemStack.
            NBTTagCompound nbt = stack.getTagCompound();

            // Transfer the NBT data to the TileEntity of the placed block.
            TileEntity tileEntity = world.getTileEntity(x, y, z);
            if (tileEntity instanceof BaseRenderTileEntity trophyTileEntity) {
                trophyTileEntity.modelName = nbt.getString(MODEL_NAME_NBT_TAG);
                trophyTileEntity.markDirty(); // Marks the TileEntity as needing to be saved
            }
        }
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune) {
        return new ArrayList<>();
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player)
    {
        final BaseTrophyTileEntity tileEntity = (BaseTrophyTileEntity) world.getTileEntity(x, y, z);
        final ItemStack itemToDrop = new ItemStack(Trophies.TrophyItem, 1, 0);

        final NBTTagCompound tag = new NBTTagCompound();
        tag.setString(MODEL_NAME_NBT_TAG, tileEntity.modelName);

        itemToDrop.stackTagCompound = tag;

        return itemToDrop;
    }

    @Deprecated
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
        // Although this is deprecated, waila depends on it to generate the ItemStack for its visualisation annoyingly.
        return getPickBlock(null, world, x, y, z, null);
    }


}
