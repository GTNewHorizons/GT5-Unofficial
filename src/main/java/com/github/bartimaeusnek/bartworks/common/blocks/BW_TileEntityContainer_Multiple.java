/*
 * Copyright (c) 2019 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.common.blocks;

import com.github.bartimaeusnek.bartworks.API.ITileWithGUI;
import com.github.bartimaeusnek.bartworks.MainMod;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.tile.IWrenchable;
import ic2.core.IC2;
import ic2.core.IHasGui;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public class BW_TileEntityContainer_Multiple extends BlockContainer {

    final protected String[] textureNames;
    final protected String name;
    final Class<? extends TileEntity>[] tileEntityArray;
    @SideOnly(Side.CLIENT)
    protected IIcon[] texture;

    public BW_TileEntityContainer_Multiple(Material p_i45386_1_, Class<? extends TileEntity>[] tileEntity, String blockName, String[] textureNames, CreativeTabs tabs) {
        super(p_i45386_1_);
        this.setHardness(15.0F);
        this.setResistance(30.0F);
        tileEntityArray = tileEntity;
        name = blockName;
        this.textureNames = textureNames;
        this.setCreativeTab(tabs);
        this.setBlockName(blockName);
        this.setBlockTextureName(MainMod.MOD_ID + ":" + blockName);
    }

    @Override
    public boolean onBlockActivated(World worldObj, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
        if (worldObj.isRemote) {
            return true;
        }
        if (!player.isSneaking()) {
            final TileEntity tile = worldObj.getTileEntity(x, y, z);
            if (tile instanceof IHasGui) {
                return worldObj.isRemote || IC2.platform.launchGui(player, (IHasGui) tile);
            } else if (tile instanceof ITileWithGUI) {
                return worldObj.isRemote || ((ITileWithGUI) tile).openGUI(tile, player);
            }
        }

        return false;
    }

    public void onBlockPlacedBy(final World world, final int x, final int y, final int z, final EntityLivingBase entity, final ItemStack itemStack) {
        final TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof IWrenchable && itemStack != null) {
            final IWrenchable tile2 = (IWrenchable) tile;
            int meta = itemStack.getItemDamage();
            world.setBlockMetadataWithNotify(x, y, z, meta, 2);
            if (entity != null) {
                final int face = MathHelper.floor_double(entity.rotationYaw * 4.0f / 360.0f + 0.5) & 0x3;
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
    public int damageDropped(final int meta) {
        return meta;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(final Item item, final CreativeTabs tab, final List list) {
        for (int i = 0; i < textureNames.length; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return meta < texture.length ? texture[meta] : texture[0];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        texture = new IIcon[textureNames.length];
        for (int i = 0; i < textureNames.length; i++) {
            texture[i] = par1IconRegister.registerIcon(textureNames[i]);
        }
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
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        try {
            return this.tileEntityArray[p_149915_2_].newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
