package gregtech.api.multitileentity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.cricketcraft.chisel.api.IFacade;

import cpw.mods.fml.common.Optional;
import gregtech.api.metatileentity.CoverableTileEntity;
import gregtech.api.multitileentity.interfaces.IMultiTileEntity;
import gregtech.common.covers.CoverInfo;
import gregtech.common.render.GT_MultiTile_Renderer;

/*
 * MultiTileEntityBlock ported from GT6
 */
@Optional.Interface(iface = "com.cricketcraft.chisel.api.IFacade", modid = "ChiselAPI")
public class MultiTileEntityBlock extends BlockContainer implements IFacade {

    private String toolName;
    private MultiTileEntityRegistry registry;

    public MultiTileEntityBlock() {
        super(Material.anvil);
    }

    public MultiTileEntityRegistry getRegistry() {
        return registry;
    }

    public MultiTileEntityBlock setRegistry(final MultiTileEntityRegistry registry) {
        this.registry = registry;
        return this;
    }

    public MultiTileEntityBlock setTool(final String toolName) {
        this.toolName = toolName;
        return this;
    }

    @Override
    public int getRenderType() {
        return GT_MultiTile_Renderer.INSTANCE == null ? super.getRenderType()
            : GT_MultiTile_Renderer.INSTANCE.getRenderId();
    }

    @Override
    public void onNeighborBlockChange(final World worldIn, final int x, final int y, final int z, final Block neighbor) {
        super.onNeighborBlockChange(worldIn, x, y, z, neighbor);
        final TileEntity te = worldIn.getTileEntity(x, y, z);
        if (!(te instanceof final IMultiTileEntity mute)) {
            return;
        }

        mute.onNeighborBlockChange(neighbor);
    }

    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        return registry.getNewTileEntity(meta);
    }

    @Override
    public ItemStack getPickBlock(final MovingObjectPosition target, final World world, final int x, final int y, final int z) {
        final TileEntity te = world.getTileEntity(x,y,z);
        if (!(te instanceof final IMultiTileEntity mute)) return null;
        return registry.getItem(mute.getMetaId());
    }

    @Override
    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX,
        float subY, float subZ) {
        TileEntity te = worldIn.getTileEntity(x, y, z);
        if (!(te instanceof IMultiTileEntity mute)) return false;

        return mute.onBlockActivated(player, ForgeDirection.getOrientation(side), subX, subY, subZ);
    }

    @Override
    public String getHarvestTool(final int metadata) {
        return toolName;
    }

    @Override
    public void getSubBlocks(final Item itemIn, final CreativeTabs tab, final List<ItemStack> list) {
        for (final MultiTileEntityClassContainer container : registry.registrations) {
            list.add(new ItemStack(this, 0, container.getMetaId()));
        }
    }

    @Override
    public Block getFacade(final IBlockAccess aWorld, final int aX, final int aY, final int aZ, final int ordinalSide) {
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof final CoverableTileEntity tile) {
            final ForgeDirection side = ForgeDirection.getOrientation(ordinalSide);
            if (ordinalSide != -1) {
                final Block facadeBlock = tile.getCoverInfoAtSide(side)
                .getFacadeBlock();
                if (facadeBlock != null) return facadeBlock;
            } else {
                // we do not allow more than one type of facade per block, so no need to check every side
                // see comment in gregtech.common.covers.GT_Cover_FacadeBase.isCoverPlaceable
                for (final ForgeDirection tSide : ForgeDirection.VALID_DIRECTIONS) {
                    final Block facadeBlock = tile.getCoverInfoAtSide(tSide)
                    .getFacadeBlock();
                    if (facadeBlock != null) {
                        return facadeBlock;
                    }
                }
            }
        }
        return Blocks.air;
    }

    @Override
    public int getFacadeMetadata(final IBlockAccess aWorld, final int aX, final int aY, final int aZ, final int ordinalSide) {
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof final CoverableTileEntity tile) {
            final ForgeDirection side = ForgeDirection.getOrientation(ordinalSide);
            if (ordinalSide != -1) {
                final CoverInfo coverInfo = tile.getCoverInfoAtSide(side);
                final Block facadeBlock = coverInfo.getFacadeBlock();
                if (facadeBlock != null) return coverInfo.getFacadeMeta();
            } else {
                // we do not allow more than one type of facade per block, so no need to check every side
                // see comment in gregtech.common.covers.GT_Cover_FacadeBase.isCoverPlaceable
                for (final ForgeDirection tSide : ForgeDirection.VALID_DIRECTIONS) {
                    final CoverInfo coverInfo = tile.getCoverInfoAtSide(tSide);
                    final Block facadeBlock = coverInfo.getFacadeBlock();
                    if (facadeBlock != null) {
                        return coverInfo.getFacadeMeta();
                    }
                }
            }
        }
        return 0;
    }

    @Override
    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta) {
        final TileEntity te = worldIn.getTileEntity(x, y, z);
        if (te instanceof IMultiTileEntity mute) {
            mute.onBlockBroken();
        }
        super.breakBlock(worldIn, x, y, z, blockBroken, meta);
    }

    @Override
    public void onBlockAdded(World worldIn, int x, int y, int z) {
        super.onBlockAdded(worldIn, x, y, z);
        final TileEntity te = worldIn.getTileEntity(x,y,z);
        if (!(te instanceof final IMultiTileEntity mute)) return;
        mute.onBlockPlaced();
    }
}
