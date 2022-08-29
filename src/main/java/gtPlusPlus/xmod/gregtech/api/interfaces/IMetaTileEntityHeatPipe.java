package gtPlusPlus.xmod.gregtech.api.interfaces;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import java.util.ArrayList;
import net.minecraft.tileentity.TileEntity;

public interface IMetaTileEntityHeatPipe extends IMetaTileEntity {

    long transferHeat(byte var1, long var2, long var4, ArrayList<TileEntity> var6);
}
