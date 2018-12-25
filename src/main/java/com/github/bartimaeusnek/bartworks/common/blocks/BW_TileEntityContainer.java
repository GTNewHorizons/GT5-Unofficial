package com.github.bartimaeusnek.bartworks.common.blocks;

import com.github.bartimaeusnek.bartworks.MainMod;
import ic2.api.tile.IWrenchable;
import ic2.core.IC2;
import ic2.core.IHasGui;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BW_TileEntityContainer extends BlockContainer {

    Class<? extends TileEntity> tileEntity = null;

    public BW_TileEntityContainer(Material p_i45386_1_,Class<? extends TileEntity> tileEntity, String blockName) {
        super(p_i45386_1_);
        this.tileEntity=tileEntity;
        this.setCreativeTab(MainMod.BWT);
        this.setBlockName(blockName);
        this.setBlockTextureName(MainMod.modID+":"+blockName);
    }


    @Override
    public boolean onBlockActivated(World worldObj, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
        if (worldObj.isRemote) {
            return true;
        }
        if (!player.isSneaking()) {
            final TileEntity tile = worldObj.getTileEntity(x, y, z);
            if (tile instanceof IHasGui) {
                return worldObj.isRemote || IC2.platform.launchGui(player, (IHasGui)tile);
            }
        }

        return false;
    }


    public void onBlockPlacedBy(final World world, final int x, final int y, final int z, final EntityLivingBase entity, final ItemStack itemStack) {
        final TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof IWrenchable && itemStack != null) {
            final IWrenchable tile2 = (IWrenchable)tile;
            int meta = itemStack.getItemDamage();
            world.setBlockMetadataWithNotify(x, y, z, meta, 2);
            if (entity != null) {
                final int face = MathHelper.floor_double(entity.rotationYaw * 4.0f / 360.0f + 0.5) & 0x3;
                switch (face) {
                    case 0:
                        tile2.setFacing((short)2);
                        break;
                    case 1:
                        tile2.setFacing((short)5);
                        break;
                    case 2:
                        tile2.setFacing((short)3);
                        break;
                    case 3:
                        tile2.setFacing((short)4);
                        break;
                }
            }
        }
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        try {
            return this.tileEntity.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
