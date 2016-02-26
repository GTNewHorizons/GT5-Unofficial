package miscutil.enderio.conduit;

import gregtech.api.interfaces.tileentity.IEnergyConnected;
import gregtech.api.metatileentity.BaseTileEntity;
import mekanism.api.gas.GasStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class ConduitGTHandler extends BaseTileEntity implements IEnergyConnected{

	@Override
	public byte getColorization() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public byte setColorization(byte arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getTimer() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isInvalidTileEntity() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLightValue(byte arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeToNBT(NBTTagCompound arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long injectEnergyUnits(byte arg0, long arg1, long arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean inputEnergyFrom(byte arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean outputsEnergyTo(byte arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public int receiveGas(ForgeDirection dir, GasStack offer) {
		// TODO Auto-generated method stub
		return 0;
	}

}
