package gregtech.common.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.items.GTGenericBlock;

/**
 * The legacy ores. Must still be registered so that postea can transform them into the new ore blocks.
 */
public abstract class BlockOresAbstractLegacy extends GTGenericBlock implements ITileEntityProvider {

    protected BlockOresAbstractLegacy(String aUnlocalizedName, int aOreMetaCount, boolean aHideFirstMeta,
        Material aMaterial) {
        super(ItemOresLegacy.class, aUnlocalizedName, aMaterial);
        this.isBlockContainer = true;
    }

    @Override
    public boolean hasTileEntity(int aMeta) {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World aWorld, int aMeta) {
        return createTileEntity(aWorld, aMeta);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess aIBlockAccess, int aX, int aY, int aZ, int ordinalSide) {
        return Blocks.stone.getIcon(0, 0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return Blocks.stone.getIcon(0, 0);
    }

    @Override
    public TileEntity createTileEntity(World aWorld, int aMeta) {
        return new TileEntityOres();
    }
}
