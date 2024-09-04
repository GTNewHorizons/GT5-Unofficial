package gtPlusPlus.everglades.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.api.interfaces.ITileTooltip;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.minecraft.BlockPos;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.everglades.dimension.DimensionEverglades;
import gtPlusPlus.everglades.world.TeleporterDimensionMod;

public class BlockEvergladesPortal extends BlockBreakable implements ITileTooltip {

    IIcon gor = null, dol = null, st1 = null, st2 = null, st3 = null, st4 = null;

    public BlockEvergladesPortal() {
        super("portal", Material.portal, false);
        this.setTickRandomly(true);
        this.setHardness(-1.0F);
        this.setLightLevel(0.75F);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setBlockName("blockDarkWorldPortal");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int ordinalSide, int meta) {

        if (ordinalSide == 0) return gor;
        else if (ordinalSide == 1) return dol;
        else if (ordinalSide == 2) return st1;
        else if (ordinalSide == 3) return st2;
        else if (ordinalSide == 4) return st4;
        else if (ordinalSide == 5) return st3;
        else return gor;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister reg) {
        this.gor = reg.registerIcon("portal");
        this.dol = reg.registerIcon("portal");
        this.st1 = reg.registerIcon("portal");
        this.st2 = reg.registerIcon("portal");
        this.st3 = reg.registerIcon("portal");
        this.st4 = reg.registerIcon("portal");
    }

    /**
     * Ticks the block if it's been scheduled
     */
    @Override
    public void updateTick(World par1World, int x, int y, int z, Random par5Random) {
        super.updateTick(par1World, x, y, z, par5Random);

        int blockCount = 0;
        BlockPos portal = new BlockPos(x, y, z, par1World.provider.dimensionId);

        for (BlockPos side : portal.getSurroundingBlocks()) {
            Block b = side.getBlockAtPos();
            if (b == DimensionEverglades.blockPortalFrame || b == DimensionEverglades.portalBlock) {
                blockCount++;
            }
        }
        if (blockCount < 4) {
            par1World.setBlockToAir(x, y, z);
        }
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
        return null;
    }

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
        float f;
        float f1;
        if (par1IBlockAccess.getBlock(par2 - 1, par3, par4) != this
            && par1IBlockAccess.getBlock(par2 + 1, par3, par4) != this) {
            f = 0.125F;
            f1 = 0.5F;
            this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f1, 0.5F + f, 1.0F, 0.5F + f1);
        } else {
            f = 0.5F;
            f1 = 0.125F;
            this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f1, 0.5F + f, 1.0F, 0.5F + f1);
        }
    }

    /**
     * Is this block (a) opaque and (B) a full 1m cube? This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    /**
     * Checks to see if this location is valid to create a portal and will return True if it does. Args: world, x, y, z
     */
    public boolean tryToCreatePortal(World par1World, int par2, int par3, int par4) {
        byte b0 = 0;
        byte b1 = 0;
        if (par1World.getBlock(par2 - 1, par3, par4) == DimensionEverglades.blockPortalFrame
            || par1World.getBlock(par2 + 1, par3, par4) == DimensionEverglades.blockPortalFrame) {
            b0 = 1;
        }
        if (par1World.getBlock(par2, par3, par4 - 1) == DimensionEverglades.blockPortalFrame
            || par1World.getBlock(par2, par3, par4 + 1) == DimensionEverglades.blockPortalFrame) {
            b1 = 1;
        }
        if (b0 == b1) {
            return false;
        } else {
            if (par1World.getBlock(par2 - b0, par3, par4 - b1) == Blocks.air) {
                par2 -= b0;
                par4 -= b1;
            }
            int l;
            int i1;
            for (l = -1; l <= 2; ++l) {
                for (i1 = -1; i1 <= 3; ++i1) {
                    boolean flag = l == -1 || l == 2 || i1 == -1 || i1 == 3;
                    if (l != -1 && l != 2 || i1 != -1 && i1 != 3) {
                        Block j1 = par1World.getBlock(par2 + b0 * l, par3 + i1, par4 + b1 * l);
                        if (flag) {
                            if (j1 != DimensionEverglades.blockPortalFrame) {
                                return false;
                            }
                        }
                        /*
                         * else if (j1 != 0 && j1 != Main.TutorialFire.blockID) { return false; }
                         */
                    }
                }
            }
            for (l = 0; l < 2; ++l) {
                for (i1 = 0; i1 < 3; ++i1) {
                    par1World.setBlock(par2 + b0 * l, par3 + i1, par4 + b1 * l, this, 0, 2);
                }
            }
            return true;
        }
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(BlockPos pos) {
        int x = pos.xPos, y = pos.yPos, z = pos.zPos;
        Logger.INFO("Trigger");
        int blockCount = 0;
        BlockPos portal = pos;
        World par1World = portal.world;
        for (BlockPos side : portal.getSurroundingBlocks()) {
            Block b = side.getBlockAtPos();
            if (b == DimensionEverglades.blockPortalFrame || b == DimensionEverglades.portalBlock) {
                blockCount++;
            }
        }
        if (blockCount < 4) {
            par1World.setBlockToAir(x, y, z);
            par1World.scheduleBlockUpdate(x, y, z, portal.getBlockAtPos(), 0);
        }

        byte b0 = 0;
        byte b1 = 1;
        if (par1World.getBlock(x - 1, y, z) == this || par1World.getBlock(x + 1, y, z) == this) {
            b0 = 1;
            b1 = 0;
        }
        int i1;
        for (i1 = y; par1World.getBlock(x, i1 - 1, z) == this; --i1) {
            ;
        }
        if (par1World.getBlock(x, i1 - 1, z) != DimensionEverglades.blockPortalFrame) {
            par1World.setBlockToAir(x, y, z);
        } else {
            int j1;
            for (j1 = 1; j1 < 4 && par1World.getBlock(x, i1 + j1, z) == this; ++j1) {
                ;
            }
            if (j1 == 3 && par1World.getBlock(x, i1 + j1, z) == DimensionEverglades.blockPortalFrame) {
                boolean flag = par1World.getBlock(x - 1, y, z) == this || par1World.getBlock(x + 1, y, z) == this;
                boolean flag1 = par1World.getBlock(x, y, z - 1) == this || par1World.getBlock(x, y, z + 1) == this;
                if (flag && flag1) {
                    par1World.setBlockToAir(x, y, z);
                } else {
                    if ((par1World.getBlock(x + b0, y, z + b1) != DimensionEverglades.blockPortalFrame
                        || par1World.getBlock(x - b0, y, z - b1) != this)
                        && (par1World.getBlock(x - b0, y, z - b1) != DimensionEverglades.blockPortalFrame
                            || par1World.getBlock(x + b0, y, z + b1) != this)) {
                        par1World.setBlockToAir(x, y, z);
                    }
                }
            } else {
                par1World.setBlockToAir(x, y, z);
            }
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        onNeighborBlockChange(new BlockPos(x, y, z, world.provider.dimensionId));
        super.onNeighborBlockChange(world, x, y, z, block);
    }

    /*
     * @Override public void onNeighborChange(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ)
     * { onNeighborBlockChange(new BlockPos(x, y, z, world.)); super.onNeighborChange(world, x, y, z, tileX, tileY,
     * tileZ); }
     */

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
     * coordinates. Args: blockAccess, x, y, z, side
     */
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
        if (par1IBlockAccess.getBlock(par2, par3, par4) == this) {
            return false;
        } else {
            boolean flag = par1IBlockAccess.getBlock(par2 - 1, par3, par4) == this
                && par1IBlockAccess.getBlock(par2 - 2, par3, par4) != this;
            boolean flag1 = par1IBlockAccess.getBlock(par2 + 1, par3, par4) == this
                && par1IBlockAccess.getBlock(par2 + 2, par3, par4) != this;
            boolean flag2 = par1IBlockAccess.getBlock(par2, par3, par4 - 1) == this
                && par1IBlockAccess.getBlock(par2, par3, par4 - 2) != this;
            boolean flag3 = par1IBlockAccess.getBlock(par2, par3, par4 + 1) == this
                && par1IBlockAccess.getBlock(par2, par3, par4 + 2) != this;
            boolean flag4 = flag || flag1;
            boolean flag5 = flag2 || flag3;
            return flag4 && par5 == 4 ? true
                : (flag4 && par5 == 5 ? true : (flag5 && par5 == 2 ? true : flag5 && par5 == 3));
        }
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    @Override
    public int quantityDropped(Random par1Random) {
        return 0;
    }

    /**
     * Triggered whenever an entity collides with this block (enters into the block). Args: world, x, y, z, entity
     */
    @Override
    public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity) {
        if ((par5Entity.ridingEntity == null) && (par5Entity.riddenByEntity == null)
            && ((par5Entity instanceof EntityPlayerMP thePlayer))) {
            if (thePlayer.timeUntilPortal > 0) {
                thePlayer.timeUntilPortal = 100;
            } else if (thePlayer.dimension != DimensionEverglades.DIMID) {
                thePlayer.timeUntilPortal = 100;
                thePlayer.mcServer.getConfigurationManager()
                    .transferPlayerToDimension(
                        thePlayer,
                        DimensionEverglades.DIMID,
                        new TeleporterDimensionMod(
                            thePlayer.mcServer.worldServerForDimension(DimensionEverglades.DIMID)));
            } else {
                thePlayer.timeUntilPortal = 100;
                thePlayer.mcServer.getConfigurationManager()
                    .transferPlayerToDimension(
                        thePlayer,
                        0,
                        new TeleporterDimensionMod(thePlayer.mcServer.worldServerForDimension(0)));
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * Returns which pass should this block be rendered on. 0 for solids and 1 for alpha
     */
    public int getRenderBlockPass() {
        return 1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random) {
        if (GTPPCore.RANDOM.nextInt(100) == 0) {
            par1World.playSound(
                par2 + 0.5D,
                par3 + 0.5D,
                par4 + 0.5D,
                "portal.portal",
                0.5F,
                GTPPCore.RANDOM.nextFloat() * 0.4F + 0.8F,
                false);
        }
        for (int l = 0; l < 4; ++l) {
            double d0 = par2 + GTPPCore.RANDOM.nextFloat();
            double d1 = par3 + GTPPCore.RANDOM.nextFloat();
            double d2 = par4 + GTPPCore.RANDOM.nextFloat();
            double d3 = 0.0D;
            double d4 = 0.0D;
            double d5 = 0.0D;
            int i1 = GTPPCore.RANDOM.nextInt(2) * 2 - 1;
            d3 = (GTPPCore.RANDOM.nextFloat() - 0.5D) * 0.5D;
            d4 = (GTPPCore.RANDOM.nextFloat() - 0.5D) * 0.5D;
            d5 = (GTPPCore.RANDOM.nextFloat() - 0.5D) * 0.5D;
            if (par1World.getBlock(par2 - 1, par3, par4) != this && par1World.getBlock(par2 + 1, par3, par4) != this) {
                d0 = par2 + 0.5D + 0.25D * i1;
                d3 = GTPPCore.RANDOM.nextFloat() * 2.0F * i1;
            } else {
                d2 = par4 + 0.5D + 0.25D * i1;
                d5 = GTPPCore.RANDOM.nextFloat() * 2.0F * i1;
            }
            par1World.spawnParticle("reddust", d0 + 0.1D, d1, d2, d3, d4, d5);
            par1World.spawnParticle("smoke", d0, d1 + 0.1D, d2, 0, 0, 0);

            Random R = new Random();

            if (R.nextInt(10) == 0) {
                par1World.spawnParticle("largesmoke", d0, d1, d2, 0, 0 + 0.2D, 0);
            } else if (R.nextInt(5) == 1) {
                par1World.spawnParticle("flame", d0, d1, d2, 0, 0 + 0.1D, 0);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    /**
     * only called by clickMiddleMouseButton , and passed to inventory.setCurrentItem (along with isCreative)
     */
    public int idPicked(World par1World, int par2, int par3, int par4) {
        return 0;
    }

    @Override
    public int colorMultiplier(final IBlockAccess par1IBlockAccess, final int par2, final int par3, final int par4) {
        return Utils.rgbtoHexValue(0, 255, 0);
    }

    @Override
    public int getRenderColor(final int aMeta) {
        return Utils.rgbtoHexValue(0, 255, 0);
    }

    @Override
    public String getLocalizedName() {
        return EnumChatFormatting.OBFUSCATED + super.getLocalizedName();
    }

    @Override
    public int getTooltipID() {
        return 1;
    }
}
