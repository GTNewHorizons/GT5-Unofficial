package com.github.bartimaeusnek.bartworks.common.blocks;

import com.github.bartimaeusnek.bartworks.API.SideReference;
import com.github.bartimaeusnek.bartworks.client.renderer.RendererGlasBlock;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public class BW_GlasBlocks2 extends BW_Blocks {
    @SideOnly(Side.CLIENT)
    private IIcon[] connectedTexture;

    private final boolean connectedTex;
    private boolean fake;
    private short[][] color = new short[this.textureNames.length][3];

    public BW_GlasBlocks2(String name, String[] texture, CreativeTabs tabs) {
        super(name, texture, tabs, Material.glass);
        this.connectedTex = false;
    }

    public BW_GlasBlocks2(
            String name, String[] texture, short[][] color, CreativeTabs tabs, boolean connectedTex, boolean fake) {
        super(name, texture, tabs, Material.glass);
        this.connectedTex = connectedTex;
        this.color = color;
        this.fake = fake;
    }

    public short[] getColor(int meta) {
        return meta < this.texture.length ? this.color[meta] : this.color[0];
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess worldClient, int xCoord, int yCoord, int zCoord, int aSide) {
        if (worldClient.getBlock(xCoord, yCoord, zCoord) instanceof BW_GlasBlocks2) return false;
        return super.shouldSideBeRendered(worldClient, xCoord, yCoord, zCoord, aSide);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return meta < this.texture.length ? this.texture[meta] : this.texture[0];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        if (!this.connectedTex) {
            this.texture = new IIcon[this.textureNames.length];
            for (int i = 0; i < this.textureNames.length; i++) {
                this.texture[i] = par1IconRegister.registerIcon(this.textureNames[i]);
            }
            return;
        }
        this.texture = new IIcon[this.textureNames.length];
        this.connectedTexture = new IIcon[16];
        for (int i = 0; i < this.textureNames.length; i++) {
            this.texture[i] = par1IconRegister.registerIcon(this.textureNames[i]);
            String[] splitname = this.textureNames[0].split(":");
            for (int j = 0; j < 16; j++) {
                this.connectedTexture[j] = par1IconRegister.registerIcon(splitname[0] + ":connectedTex/"
                        + "BoronSilicateGlassBlock" + '/' + "BoronSilicateGlassBlock" + '_' + j);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess worldClient, int xCoord, int yCoord, int zCoord, int aSide) {
        if (!this.connectedTex) return super.getIcon(worldClient, xCoord, yCoord, zCoord, aSide);

        ForgeDirection dir = ForgeDirection.getOrientation(aSide);
        byte sides = 0;
        switch (dir) {
            case UP:
            case DOWN: {
                if (worldClient.getBlock(xCoord, yCoord, zCoord - 1) instanceof BW_GlasBlocks2)
                    sides = (byte) (sides | 0b0001);
                if (worldClient.getBlock(xCoord, yCoord, zCoord + 1) instanceof BW_GlasBlocks2)
                    sides = (byte) (sides | 0b0010);
                if (worldClient.getBlock(xCoord - 1, yCoord, zCoord) instanceof BW_GlasBlocks2)
                    sides = (byte) (sides | 0b0100);
                if (worldClient.getBlock(xCoord + 1, yCoord, zCoord) instanceof BW_GlasBlocks2)
                    sides = (byte) (sides | 0b1000);
                break;
            }
            case EAST: {
                if (worldClient.getBlock(xCoord, yCoord + 1, zCoord) instanceof BW_GlasBlocks2)
                    sides = (byte) (sides | 0b0001);
                if (worldClient.getBlock(xCoord, yCoord - 1, zCoord) instanceof BW_GlasBlocks2)
                    sides = (byte) (sides | 0b0010);
                if (worldClient.getBlock(xCoord, yCoord, zCoord + 1) instanceof BW_GlasBlocks2)
                    sides = (byte) (sides | 0b0100);
                if (worldClient.getBlock(xCoord, yCoord, zCoord - 1) instanceof BW_GlasBlocks2)
                    sides = (byte) (sides | 0b1000);
                break;
            }
            case WEST: {
                if (worldClient.getBlock(xCoord, yCoord + 1, zCoord) instanceof BW_GlasBlocks2)
                    sides = (byte) (sides | 0b0001);
                if (worldClient.getBlock(xCoord, yCoord - 1, zCoord) instanceof BW_GlasBlocks2)
                    sides = (byte) (sides | 0b0010);
                if (worldClient.getBlock(xCoord, yCoord, zCoord - 1) instanceof BW_GlasBlocks2)
                    sides = (byte) (sides | 0b0100);
                if (worldClient.getBlock(xCoord, yCoord, zCoord + 1) instanceof BW_GlasBlocks2)
                    sides = (byte) (sides | 0b1000);
                break;
            }
            case NORTH: {
                if (worldClient.getBlock(xCoord, yCoord + 1, zCoord) instanceof BW_GlasBlocks2)
                    sides = (byte) (sides | 0b0001);
                if (worldClient.getBlock(xCoord, yCoord - 1, zCoord) instanceof BW_GlasBlocks2)
                    sides = (byte) (sides | 0b0010);
                if (worldClient.getBlock(xCoord + 1, yCoord, zCoord) instanceof BW_GlasBlocks2)
                    sides = (byte) (sides | 0b0100);
                if (worldClient.getBlock(xCoord - 1, yCoord, zCoord) instanceof BW_GlasBlocks2)
                    sides = (byte) (sides | 0b1000);
                break;
            }
            case SOUTH: {
                if (worldClient.getBlock(xCoord, yCoord + 1, zCoord) instanceof BW_GlasBlocks2)
                    sides = (byte) (sides | 0b0001);
                if (worldClient.getBlock(xCoord, yCoord - 1, zCoord) instanceof BW_GlasBlocks2)
                    sides = (byte) (sides | 0b0010);
                if (worldClient.getBlock(xCoord - 1, yCoord, zCoord) instanceof BW_GlasBlocks2)
                    sides = (byte) (sides | 0b0100);
                if (worldClient.getBlock(xCoord + 1, yCoord, zCoord) instanceof BW_GlasBlocks2)
                    sides = (byte) (sides | 0b1000);
                break;
            }
            case UNKNOWN:
            default: {
                break;
            }
        }
        return this.connectedTexture[sides];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass() {
        return 1;
    }

    @Override
    public int getRenderType() {
        if (!this.fake && SideReference.Side.Client) return RendererGlasBlock.RID;
        else return 0;
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
