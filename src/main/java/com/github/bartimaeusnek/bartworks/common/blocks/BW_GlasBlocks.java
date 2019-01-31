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

import com.github.bartimaeusnek.bartworks.client.renderer.RendererGlasBlock;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public class BW_GlasBlocks extends BW_Blocks {

    @SideOnly(Side.CLIENT)
    IIcon[] connectedTexture;

    boolean connectedTex, fake;
    short[][] color = new short[textureNames.length][3];

    public BW_GlasBlocks(String name, String[] texture, CreativeTabs tabs) {
        super(name, texture, tabs, Material.glass);
        connectedTex = false;
    }


    public BW_GlasBlocks(String name, String[] texture, short[][] color, CreativeTabs tabs, boolean connectedTex, boolean fake) {
        super(name, texture, tabs, Material.glass);
        this.connectedTex = connectedTex;
        this.color = color;
        this.fake = fake;
    }

    public short[] getColor(int meta) {
        return meta < texture.length ? color[meta] : color[0];
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess worldClient, int xCoord, int yCoord, int zCoord, int aSide) {
        if (worldClient.getBlock(xCoord, yCoord, zCoord) instanceof BW_GlasBlocks)
            return false;
        return super.shouldSideBeRendered(worldClient, xCoord, yCoord, zCoord, aSide);
    }


    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return meta < texture.length ? texture[meta] : texture[0];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        if (!connectedTex) {
            texture = new IIcon[textureNames.length];
            for (int i = 0; i < textureNames.length; i++) {
                texture[i] = par1IconRegister.registerIcon(textureNames[i]);
            }
            return;
        }
        texture = new IIcon[textureNames.length];
        connectedTexture = new IIcon[16];
        for (int i = 0; i < textureNames.length; i++) {
            texture[i] = par1IconRegister.registerIcon(textureNames[i]);
            for (int j = 0; j < 16; j++) {
                connectedTexture[j] = par1IconRegister.registerIcon(textureNames[0] + "_" + j);
            }
        }
    }


    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess worldClient, int xCoord, int yCoord, int zCoord, int aSide) {
        if (!connectedTex)
            return super.getIcon(worldClient, xCoord, yCoord, zCoord, aSide);

        ForgeDirection dir = ForgeDirection.getOrientation(aSide);
        byte sides = 0;
        switch (dir) {
            case UP:
            case DOWN: {
                if (worldClient.getBlock(xCoord, yCoord, zCoord - 1) instanceof BW_GlasBlocks)
                    sides = (byte) (sides | 0b0001);
                if (worldClient.getBlock(xCoord, yCoord, zCoord + 1) instanceof BW_GlasBlocks)
                    sides = (byte) (sides | 0b0010);
                if (worldClient.getBlock(xCoord - 1, yCoord, zCoord) instanceof BW_GlasBlocks)
                    sides = (byte) (sides | 0b0100);
                if (worldClient.getBlock(xCoord + 1, yCoord, zCoord) instanceof BW_GlasBlocks)
                    sides = (byte) (sides | 0b1000);
                break;
            }
            case EAST: {
                if (worldClient.getBlock(xCoord, yCoord + 1, zCoord) instanceof BW_GlasBlocks)
                    sides = (byte) (sides | 0b0001);
                if (worldClient.getBlock(xCoord, yCoord - 1, zCoord) instanceof BW_GlasBlocks)
                    sides = (byte) (sides | 0b0010);
                if (worldClient.getBlock(xCoord, yCoord, zCoord + 1) instanceof BW_GlasBlocks)
                    sides = (byte) (sides | 0b0100);
                if (worldClient.getBlock(xCoord, yCoord, zCoord - 1) instanceof BW_GlasBlocks)
                    sides = (byte) (sides | 0b1000);
                break;
            }
            case WEST: {
                if (worldClient.getBlock(xCoord, yCoord + 1, zCoord) instanceof BW_GlasBlocks)
                    sides = (byte) (sides | 0b0001);
                if (worldClient.getBlock(xCoord, yCoord - 1, zCoord) instanceof BW_GlasBlocks)
                    sides = (byte) (sides | 0b0010);
                if (worldClient.getBlock(xCoord, yCoord, zCoord - 1) instanceof BW_GlasBlocks)
                    sides = (byte) (sides | 0b0100);
                if (worldClient.getBlock(xCoord, yCoord, zCoord + 1) instanceof BW_GlasBlocks)
                    sides = (byte) (sides | 0b1000);
                break;

            }
            case NORTH: {
                if (worldClient.getBlock(xCoord, yCoord + 1, zCoord) instanceof BW_GlasBlocks)
                    sides = (byte) (sides | 0b0001);
                if (worldClient.getBlock(xCoord, yCoord - 1, zCoord) instanceof BW_GlasBlocks)
                    sides = (byte) (sides | 0b0010);
                if (worldClient.getBlock(xCoord + 1, yCoord, zCoord) instanceof BW_GlasBlocks)
                    sides = (byte) (sides | 0b0100);
                if (worldClient.getBlock(xCoord - 1, yCoord, zCoord) instanceof BW_GlasBlocks)
                    sides = (byte) (sides | 0b1000);
                break;
            }
            case SOUTH: {
                if (worldClient.getBlock(xCoord, yCoord + 1, zCoord) instanceof BW_GlasBlocks)
                    sides = (byte) (sides | 0b0001);
                if (worldClient.getBlock(xCoord, yCoord - 1, zCoord) instanceof BW_GlasBlocks)
                    sides = (byte) (sides | 0b0010);
                if (worldClient.getBlock(xCoord - 1, yCoord, zCoord) instanceof BW_GlasBlocks)
                    sides = (byte) (sides | 0b0100);
                if (worldClient.getBlock(xCoord + 1, yCoord, zCoord) instanceof BW_GlasBlocks)
                    sides = (byte) (sides | 0b1000);
                break;
            }
            case UNKNOWN:
            default: {
                break;
            }
        }
        return connectedTexture[sides];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass() {
        return 1;
    }

    @Override
    public int getRenderType() {
        if (!fake && FMLCommonHandler.instance().getSide().isClient())
            return RendererGlasBlock.RID;
        else
            return 0;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    protected boolean canSilkHarvest() {
        return false;
    }
}
