package gtPlusPlus.xmod.gregtech.api.interfaces;

import java.util.ArrayList;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import net.minecraft.tileentity.TileEntity;

public interface IMetaTileEntityHeatPipe extends IMetaTileEntity {
	
	
	long transferHeat(byte var1, long var2, long var4, ArrayList<TileEntity> var6);
	
	
}