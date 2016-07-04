package miscutil.core.multiblock.abstracts;

import java.util.Collection;

import miscutil.core.multiblock.abstracts.interfaces.IAbstractControllerInternal;
import miscutil.core.multiblock.base.interfaces.IBaseMultiblockController;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import raisintoast.core.common.CoordTriplet;
import raisintoast.core.multiblock.IMultiblockPart;
import raisintoast.core.multiblock.MultiblockControllerBase;
import raisintoast.core.multiblock.rectangular.RectangularMultiblockControllerBase;


public class AbstractMultiblockController extends RectangularMultiblockControllerBase implements IAbstractControllerInternal {

	protected AbstractMultiblockController(World world) {
		super(world);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void decodeDescriptionPacket(NBTTagCompound arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void formatDescriptionPacket(NBTTagCompound arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected int getMaximumXSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getMaximumYSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getMaximumZSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getMinimumNumberOfBlocksForAssembledMachine() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void onAssimilate(MultiblockControllerBase arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onAssimilated(MultiblockControllerBase arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAttachedPartWithMultiblockData(IMultiblockPart arg0,
			NBTTagCompound arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onBlockAdded(IMultiblockPart arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onBlockRemoved(IMultiblockPart arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onMachineAssembled() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onMachineDisassembled() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onMachinePaused() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onMachineRestored() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void readFromNBT(NBTTagCompound arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void updateClient() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean updateServer() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void writeToNBT(NBTTagCompound arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reassemble() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getLastValidationError() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<IMultiblockPart> getComponents() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void assimilate(
			IBaseMultiblockController paramIMultiblockControllerInternal) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void _onAssimilated(
			IBaseMultiblockController paramIMultiblockControllerInternal) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAssimilated(
			IBaseMultiblockController paramIMultiblockControllerInternal) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CoordTriplet getReferenceCoord() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public World getWorld() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean shouldConsume(
			IBaseMultiblockController paramIMultiblockControllerInternal) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getPartsListString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void auditParts() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getHealthScaled(int paramInt) {
		// TODO Auto-generated method stub
		return 0;
	}

}
