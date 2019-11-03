package gregtech.api.interfaces.metatileentity;

import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;
import java.util.HashSet;

public interface IMetaTileEntityCable extends IMetaTileEntity {
    @Deprecated
    public long transferElectricity(byte aSide, long aVoltage, long aAmperage, ArrayList<TileEntity> aAlreadyPassedTileEntityList);

    public long transferElectricity(byte aSide, long aVoltage, long aAmperage, HashSet<TileEntity> aAlreadyPassedSet);
}