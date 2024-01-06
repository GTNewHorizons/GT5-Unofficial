package gregtech.api.multitileentity.multiblock.base;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.multitileentity.interfaces.IMultiBlockController;

public class WallShareablePart extends MultiBlockPart {

    protected List<ChunkCoordinates> targetPositions = new ArrayList<>();
    protected List<IMultiBlockController> targets = new ArrayList<>();

    @Override
    public void setTarget(IMultiBlockController aTarget, int aAllowedModes) {
        if (targets.size() > 1 || targetPositions.size() > 1) {
            mAllowedModes = 0;
            mMode = 0;
        } else {
            mAllowedModes = aAllowedModes;
        }

        if (aTarget == null) {
            return;
        }

        targets.add(aTarget);
        targetPositions.add(aTarget.getCoords());
    }

    @Override
    public String getLockedInventory() {
        issueClientUpdate();
        if (targets.size() > 1 || targetPositions.size() > 1) {
            return null;
        }

        IMultiBlockController controller = getTarget(false);
        if (!getNameOfInventoryFromIndex(controller, mLockedInventoryIndex).equals(mLockedInventory)) {
            mLockedInventory = getNameOfInventoryFromIndex(controller, mLockedInventoryIndex);
            if (mLockedInventory.equals("all")) {
                mLockedInventory = "";
            }
        }
        return mLockedInventory.equals("") ? null : mLockedInventory;
    }

    @Override
    public IMultiBlockController getTarget(boolean aCheckValidity) {
        if (targets.size() > 1 || targetPositions.size() > 1 || targets.size() <= 0 || targetPositions.size() <= 0) {
            return null;
        }

        target = targets.get(0);
        mTargetPos = targetPositions.get(0);
        return super.getTarget(aCheckValidity);
    }

    @Override
    public String getTileEntityName() {
        return "gt.multiTileEntity.casing.wallSharable";
    }

    @Override
    public boolean breakBlock() {
        for (final ChunkCoordinates coordinates : targetPositions) {
            IMultiBlockController target = getTarget(coordinates, false);
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
            } else if (te instanceof IMultiBlockController controller) {
                controller.onStructureChange();
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
