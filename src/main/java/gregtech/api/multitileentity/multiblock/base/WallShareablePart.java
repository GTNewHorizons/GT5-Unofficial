package gregtech.api.multitileentity.multiblock.base;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;

import gregtech.api.multitileentity.enums.PartMode;
import gregtech.api.multitileentity.interfaces.IMultiBlockController;

public class WallShareablePart extends MultiBlockPart {

    protected List<ChunkCoordinates> targetPositions = new ArrayList<>();

    @Override
    public void setTarget(IMultiBlockController aTarget, int aAllowedModes) {
        if (targetPositions.size() >= 1) {
            allowedModes = 0;
            setMode(PartMode.NOTHING);
            targetPosition = null;
        } else {
            allowedModes = aAllowedModes;
        }

        if (aTarget == null) {
            return;
        }

        targetPositions.add(aTarget.getCoords());
    }

    @Override
    public UUID getLockedInventory() {
        if (targetPositions.size() > 1) {
            return null;
        }
        return super.getLockedInventory();
    }

    @Override
    public IMultiBlockController getTarget(boolean aCheckValidity) {
        if (targetPositions.size() != 1) {
            return null;
        }

        targetPosition = targetPositions.get(0);
        return super.getTarget(aCheckValidity);
    }

    @Override
    public String getTileEntityName() {
        return "gt.multiTileEntity.casing.wallSharable";
    }

    public IMultiBlockController getTarget(ChunkCoordinates coordinates, boolean aCheckValidity) {
        IMultiBlockController target = null;
        if (coordinates == null) return null;
        if (worldObj.blockExists(coordinates.posX, coordinates.posY, coordinates.posZ)) {
            final TileEntity te = worldObj.getTileEntity(coordinates.posX, coordinates.posY, coordinates.posZ);
            if (te instanceof IMultiBlockController) {
                target = (IMultiBlockController) te;
            }
        }
        if (aCheckValidity) {
            return target != null && target.checkStructure(false) ? target : null;
        }
        return target;
    }

    @Override
    public boolean shouldOpen() {
        return !(targetPositions.size() > 1);
    }
}
