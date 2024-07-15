package gregtech.api.multitileentity.multiblock.base;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.multitileentity.WeakTargetRef;
import gregtech.api.multitileentity.interfaces.IMultiBlockController;

public class WallShareablePart extends MultiBlockPart {

    protected List<WeakTargetRef<IMultiBlockController>> targets = new ArrayList<>();

    @Override
    public void setTarget(IMultiBlockController newController, int allowedModes) {
        if (targets.size() >= 1) {
            this.allowedModes = 0;
            setMode((byte) 0);
            controller.invalidate();
        } else {
            this.allowedModes = allowedModes;
            controller.setTarget(newController);
        }

        if (newController == null) {
            return;
        }

        targets.add(new WeakTargetRef<IMultiBlockController>(IMultiBlockController.class, true));
    }

    @Override
    public UUID getLockedInventory() {
        if (targets.size() > 1) {
            return null;
        }
        return super.getLockedInventory();
    }

    @Override
    public IMultiBlockController getTarget(boolean aCheckValidity) {
        if (targets.size() != 1) {
            return null;
        }

        controller.setTarget(
            targets.get(0)
                .get());
        return super.getTarget(aCheckValidity);
    }

    @Override
    public String getTileEntityName() {
        return "gt.multiTileEntity.casing.wallSharable";
    }

    @Override
    public boolean onBlockBroken() {
        for (final WeakTargetRef<IMultiBlockController> tar : targets) {
            IMultiBlockController target = getTarget(tar.getPosition(), false);
            if (target == null) {
                continue;
            }
            target.onStructureChange();
        }
        return false;
    }

    @Override
    public void onBlockAdded() {
        for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            final TileEntity te = getTileEntityAtSide(side);
            if (te instanceof MultiBlockPart part) {
                final IMultiBlockController tController = part.getTarget(false);
                if (tController != null) tController.onStructureChange();
            } else if (te instanceof IMultiBlockController tController) {
                tController.onStructureChange();
            }
        }
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
}
