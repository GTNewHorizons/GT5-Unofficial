/*
 * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package bartworks.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidContainerItem;

import com.gtnewhorizons.modularui.api.UIInfos;
import com.gtnewhorizons.modularui.api.screen.ITileWithModularUI;

import bartworks.API.ITileAddsInformation;
import bartworks.API.ITileDropsContent;
import bartworks.API.ITileHasDifferentTextureSides;
import bartworks.MainMod;
import bartworks.common.tileentities.classic.TileEntityHeatedWaterPump;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.tile.IWrenchable;
import ic2.core.IC2;
import ic2.core.IHasGui;

public class BWTileEntityContainer extends BlockContainer implements ITileAddsInformation {

    protected Class<? extends TileEntity> tileEntity;

    public BWTileEntityContainer(Material p_i45386_1_, Class<? extends TileEntity> tileEntity, String blockName) {
        super(p_i45386_1_);
        this.tileEntity = tileEntity;
        this.setHardness(15.0F);
        this.setResistance(30.0F);
        this.setCreativeTab(MainMod.BWT);
        this.setBlockName(blockName);
        this.setBlockTextureName(MainMod.MOD_ID + ":" + blockName);
    }

    @Override
    public boolean onBlockActivated(World worldObj, int x, int y, int z, EntityPlayer player, int side, float subX,
        float subY, float subZ) {
        if (worldObj.isRemote) {
            return false;
        }
        TileEntity tile = worldObj.getTileEntity(x, y, z);
        if (tile instanceof TileEntityHeatedWaterPump) {
            if (player.getHeldItem() != null && (player.getHeldItem()
                .getItem()
                .equals(Items.bucket)
                || player.getHeldItem()
                    .getItem() instanceof IFluidContainerItem)
                && ((TileEntityHeatedWaterPump) tile).drain(1000, false) != null)
                if (player.getHeldItem()
                    .getItem()
                    .equals(Items.bucket) && ((TileEntityHeatedWaterPump) tile).drain(1000, false).amount == 1000) {
                        ((TileEntityHeatedWaterPump) tile).drain(1000, true);
                        player.getHeldItem().stackSize--;
                        if (player.getHeldItem().stackSize <= 0)
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                        player.inventory.addItemStackToInventory(new ItemStack(Items.water_bucket));
                        return true;
                    }
        }
        if (!player.isSneaking()) {
            if (tile instanceof IHasGui) {
                return worldObj.isRemote || IC2.platform.launchGui(player, (IHasGui) tile);
            }
            if (tile instanceof ITileWithModularUI) {
                if (!worldObj.isRemote) {
                    UIInfos.TILE_MODULAR_UI.open(player, worldObj, x, y, z);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof IWrenchable tile2 && itemStack != null) {
            int meta = itemStack.getItemDamage();
            world.setBlockMetadataWithNotify(x, y, z, meta, 2);
            if (entity != null) {
                int face = MathHelper.floor_double(entity.rotationYaw * 4.0f / 360.0f + 0.5) & 0x3;
                switch (face) {
                    case 0:
                        tile2.setFacing((short) 2);
                        break;
                    case 1:
                        tile2.setFacing((short) 5);
                        break;
                    case 2:
                        tile2.setFacing((short) 3);
                        break;
                    case 3:
                        tile2.setFacing((short) 4);
                        break;
                }
            }
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        TileEntity t = world.getTileEntity(x, y, z);
        if (t instanceof ITileDropsContent) {
            int[] dropSlots = ((ITileDropsContent) t).getDropSlots();
            for (int dropSlot : dropSlots) {
                if (((ITileDropsContent) t).getStackInSlot(dropSlot) != null) world.spawnEntityInWorld(
                    new EntityItem(world, x, y, z, ((TileEntityHeatedWaterPump) t).getStackInSlot(dropSlot)));
            }
        }
        super.breakBlock(world, x, y, z, block, meta);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (!ITileHasDifferentTextureSides.class.isAssignableFrom(this.tileEntity)) return super.getIcon(side, meta);
        try {
            return ((ITileHasDifferentTextureSides) this.tileEntity.getConstructor()
                .newInstance()).getTextureForSide(side, meta);
        } catch (Exception e) {
            e.printStackTrace();
            return super.getIcon(side, meta);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        if (ITileHasDifferentTextureSides.class.isAssignableFrom(this.tileEntity)) {
            try {
                ((ITileHasDifferentTextureSides) this.tileEntity.getConstructor()
                    .newInstance()).registerBlockIcons(par1IconRegister);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else super.registerBlockIcons(par1IconRegister);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        try {
            return this.tileEntity.getConstructor()
                .newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity) {
        return false;
    }

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    public String[] getInfoData() {
        if (ITileAddsInformation.class.isAssignableFrom(this.tileEntity)) {
            try {
                return ((ITileAddsInformation) this.tileEntity.getConstructor()
                    .newInstance()).getInfoData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new String[0];
    }
}
