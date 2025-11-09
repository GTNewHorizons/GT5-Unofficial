package gregtech.api.util;

import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.gtnhlib.blockpos.BlockPos;
import com.gtnewhorizon.gtnhlib.item.ItemTransfer;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IHasWorldObjectAndCoords;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;

public class GTItemTransfer extends ItemTransfer {

    public void push(IHasWorldObjectAndCoords self, ForgeDirection side) {
        push(self, side, self.getTileEntityAtSide(side));
    }

    public void pull(IHasWorldObjectAndCoords self, ForgeDirection side) {
        pull(self, side, self.getTileEntityAtSide(side));
    }

    public void outOfMachine(IGregTechTileEntity igte, ForgeDirection side) {
        outOfMachine(igte.getMetaTileEntity(), side);
    }

    public void outOfMachine(IMetaTileEntity imte, ForgeDirection side) {
        source(imte, side);
        sink(
            imte.getBaseMetaTileEntity()
                .getTileEntityAtSide(side),
            side.getOpposite());
    }

    public void dropItems(IHasWorldObjectAndCoords tile, ForgeDirection output) {
        dropItems(
            tile.getWorld(),
            new BlockPos(
                tile.getXCoord() + output.offsetX,
                tile.getYCoord() + output.offsetY,
                tile.getZCoord() + output.offsetZ));
    }

    public void dropItems(MTEBasicMachine machine) {
        dropItems(
            machine.getBaseMetaTileEntity(),
            machine.getBaseMetaTileEntity()
                .getFrontFacing());
    }
}
