package gregtech.api.net.data;

import static gregtech.api.util.GT_Util.setTileEntity;

import javax.annotation.Nonnull;

import gregtech.api.multitileentity.MultiTileEntityBlock;
import gregtech.api.multitileentity.MultiTileEntityRegistry;
import gregtech.api.multitileentity.interfaces.IMultiTileEntity;
import gregtech.api.util.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class MultiTileEntityProcess extends Process {

    @Nonnull
    private final IBlockAccess world;
    private ChunkCoordinates coords;
    private int registryId;
    private int metaId;
    private byte redstone;
    private byte color;
    private byte commonData;

    public MultiTileEntityProcess(@Nonnull IBlockAccess world) {
        this.world = world;
    }

    @Override
    public void process() {
        if (coords == null) return;
        if (!(world instanceof World ww)) return;
        Block block = world.getBlock(coords.posX, coords.posY, coords.posZ);
        if (!(block instanceof MultiTileEntityBlock)) {
            return;
        }
        TileEntity te = WorldHelper.getTileEntityAtSide(ForgeDirection.UNKNOWN, world, coords);
        if (!(te instanceof IMultiTileEntity mte) || mte.getRegistryId() != registryId || mte.getMetaId() != metaId) {
            final MultiTileEntityRegistry registry = MultiTileEntityRegistry.getRegistry(registryId);
            if (registry == null) return;
            te = registry.getNewTileEntity(ww, coords.posX, coords.posY, coords.posZ, metaId);
            if (!(te instanceof IMultiTileEntity)) return;
            setTileEntity(ww, coords.posX, coords.posY, coords.posZ, te, false);
        }

        final IMultiTileEntity mute = (IMultiTileEntity) te;

    }

    public void giveCoordinates(@Nonnull ChunkCoordinates coords) {
        this.coords = coords;
    }

    public void giveMultiTileEntityData(int registryId, int metaId) {
        this.registryId = registryId;
        this.metaId = metaId;
    }

    public void giveCommonData(byte redstone, byte color, byte commonData) {
        this.redstone = redstone;
        this.color = color;
        this.commonData = commonData;
    }
}
