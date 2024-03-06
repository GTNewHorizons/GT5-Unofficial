package gregtech.api.net.data;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.IBlockAccess;

import gregtech.api.multitileentity.MultiTileEntityBlock;
import gregtech.api.multitileentity.MultiTileEntityRegistry;
import gregtech.api.multitileentity.enums.PartMode;
import gregtech.api.multitileentity.interfaces.IMultiTileEntity;
import gregtech.api.multitileentity.multiblock.base.MultiBlockPart;

public class MultiTileEntityProcess extends Process {

    @Nonnull
    private final IBlockAccess world;
    private int x, y, z;
    private int registryId;
    private int metaId;
    private int allowedModes;
    private int currentMode;
    private int controllerX, controllerY, controllerZ;

    public MultiTileEntityProcess(@Nonnull IBlockAccess world) {
        this.world = world;
    }

    @Override
    public void process() {
        Block block = world.getBlock(x, y, z);
        if (!(block instanceof MultiTileEntityBlock)) {
            return;
        }
        TileEntity te = world.getTileEntity(x, y, z);
        if (!(te instanceof IMultiTileEntity mte) || mte.getRegistryId() != registryId || mte.getMetaId() != metaId) {
            final MultiTileEntityRegistry registry = MultiTileEntityRegistry.getRegistry(registryId);
            if (registry == null) return;
        }

        final IMultiTileEntity mute = (IMultiTileEntity) te;

        if (mute instanceof MultiBlockPart part) {
            part.setTargetPos(new ChunkCoordinates(controllerX, controllerY, controllerZ));
            part.setAllowedModes(allowedModes);
            part.setMode(PartMode.values()[currentMode]);
        }
    }

    public void giveCoordinates(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void giveCasingData(int allowedModes, int currentMode, int controllerX, int controllerY, int controllerZ) {
        this.allowedModes = allowedModes;
        this.currentMode = currentMode;
        this.controllerX = controllerX;
        this.controllerY = controllerY;
        this.controllerZ = controllerZ;
    }
}
