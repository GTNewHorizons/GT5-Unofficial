package gregtech.api.multitileentity;

import static gregtech.api.util.GT_Util.LAST_BROKEN_TILEENTITY;
import static gregtech.api.util.GT_Util.getTileEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import com.cricketcraft.chisel.api.IFacade;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IDebugableBlock;
import gregtech.api.interfaces.tileentity.IDebugableTileEntity;
import gregtech.api.metatileentity.BaseTileEntity;
import gregtech.api.metatileentity.CoverableTileEntity;
import gregtech.api.multitileentity.interfaces.IMultiTileEntity;
import gregtech.api.util.GT_Util;
import gregtech.api.util.WorldHelper;
import gregtech.common.covers.CoverInfo;
import gregtech.common.render.GT_MultiTile_Renderer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/*
 * MultiTileEntityBlock ported from GT6
 */
@Optional.Interface(iface = "com.cricketcraft.chisel.api.IFacade", modid = "ChiselAPI")
public class MultiTileEntityBlock extends BlockContainer implements IFacade {

    private final String toolName;
    private final MultiTileEntityRegistry registry;

    public MultiTileEntityBlock(@Nonnull final MultiTileEntityRegistry registry, @Nonnull final Material material, @Nonnull final String toolName, @Nonnull final String blockName) {
        super(material);
        this.toolName = toolName;
        this.registry = registry;
        setBlockName("gt.multitileentity.block."+blockName);
    }

    @Override
    public int getRenderType() {
        return GT_MultiTile_Renderer.INSTANCE == null ? super.getRenderType()
        : GT_MultiTile_Renderer.INSTANCE.getRenderId();
    }

    @Override
    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor) {
        super.onNeighborBlockChange(worldIn, x, y, z, neighbor);
        TileEntity te = worldIn.getTileEntity(x,y,z);
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
