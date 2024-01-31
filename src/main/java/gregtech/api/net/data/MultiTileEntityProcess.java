package gregtech.api.net.data;

import javax.annotation.Nonnull;

import gregtech.api.multitileentity.MultiTileEntityBlock;
import gregtech.api.multitileentity.interfaces.IMultiTileEntity;
import net.minecraft.block.Block;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.IBlockAccess;

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
        Block block = world.getBlock(coords.posX, coords.posY, coords.posZ);
        if (!(block instanceof MultiTileEntityBlock muteBlock)) {
            return;
        }
        IMultiTileEntity mute = muteBlock.receiveMultiTileEntityData(world, coords.posX, coords.posY, coords.posZ, registryId, metaId);
        if (mute == null) return;
        mute.setColorization(color);
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
