package gregtech.api.interfaces.metatileentity;

import java.util.HashSet;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public interface IMetaTileEntityCable extends IMetaTileEntityPipe {

    long transferElectricity(ForgeDirection side, long aVoltage, long aAmperage, HashSet<TileEntity> aAlreadyPassedSet);
}
