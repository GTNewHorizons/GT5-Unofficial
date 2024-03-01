package gregtech.api.multitileentity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
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

    public MultiTileEntityBlock setRegistry(MultiTileEntityRegistry registry) {
        this.registry = registry;
        return this;
    }

    public MultiTileEntityBlock setTool(String toolName) {
        this.toolName = toolName;
        return this;
    }

    @Override
    public int getRenderType() {
        return GT_MultiTile_Renderer.INSTANCE == null ? super.getRenderType()
            : GT_MultiTile_Renderer.INSTANCE.getRenderId();
    }

    @Override
    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor) {
        super.onNeighborBlockChange(worldIn, x, y, z, neighbor);
        TileEntity te = worldIn.getTileEntity(x, y, z);
        if (!(te instanceof IMultiTileEntity mute)) {
            return;
        }
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return registry.getNewTileEntity(meta);
    }

    @Override
    public String getHarvestTool(int metadata) {
        return toolName;
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        for(MultiTileEntityClassContainer container : registry.registrations) {
            list.add(new ItemStack(container.block));
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
}
