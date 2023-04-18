package gregtech.api.interfaces.metatileentity;

import java.util.ArrayList;
import java.util.HashSet;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public interface IMetaTileEntityCable extends IMetaTileEntityPipe {

    @Deprecated
    long transferElectricity(ForgeDirection side, long aVoltage, long aAmperage,
        ArrayList<TileEntity> aAlreadyPassedTileEntityList);

    default long transferElectricity(ForgeDirection side, long aVoltage, long aAmperage,
        HashSet<TileEntity> aAlreadyPassedSet) {
        return transferElectricity(side, aVoltage, aAmperage, new ArrayList<>(aAlreadyPassedSet));
    }
}
