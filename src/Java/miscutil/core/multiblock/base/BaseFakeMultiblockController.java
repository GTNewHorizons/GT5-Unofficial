package miscutil.core.multiblock.base;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import javax.annotation.Nonnull;

import miscutil.core.multiblock.base.interfaces.IBaseMultiblockControllerInternal;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import raisintoast.core.common.CoordTriplet;
import raisintoast.core.multiblock.IMultiblockPart;

public abstract class BaseFakeMultiblockController
implements IBaseMultiblockControllerInternal
{
	@Override
	public void attachBlock(IMultiblockPart part) {}

	@Override
	public void detachBlock(IMultiblockPart part, boolean chunkUnloading) {}

	@Override
	public void checkIfMachineIsWhole() {}

	public void assimilate(IBaseMultiblockControllerInternal other) {}

	public void _onAssimilated(IBaseMultiblockControllerInternal otherController) {}

	public void onAssimilated(IBaseMultiblockControllerInternal assimilator) {}

	@Override
	public void updateMultiblockEntity() {}

	@Override
	public CoordTriplet getReferenceCoord()
	{
		return null;
	}

	@Override
	public void recalculateMinMaxCoords() {}

	@Override
	public void formatDescriptionPacket(NBTTagCompound data) {}

	@Override
	public void decodeDescriptionPacket(NBTTagCompound data) {}

	@Override
	public World getWorld()
	{
		return null;
	}

	@Override
	public boolean isEmpty()
	{
		return true;
	}

	public boolean shouldConsume(IBaseMultiblockControllerInternal otherController)
	{
		return false;
	}

	@Override
	public String getPartsListString()
	{
		return "";
	}

	@Override
	public void auditParts() {}

	@Override
	@Nonnull
	public Set<IMultiblockPart> checkForDisconnections()
	{
		return Collections.emptySet();
	}

	@Override
	@Nonnull
	public Set<IMultiblockPart> detachAllBlocks()
	{
		return Collections.emptySet();
	}

	@Override
	public boolean isAssembled()
	{
		return false;
	}

	@Override
	public void reassemble() {}

	@Override
	public String getLastValidationError()
	{
		return null;
	}

	@Override
	@Nonnull
	public Collection<IMultiblockPart> getComponents()
	{
		return Collections.emptyList();
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {

	}

	public void writeToNBT(NBTTagCompound nbttagcompound) {

	}



}
