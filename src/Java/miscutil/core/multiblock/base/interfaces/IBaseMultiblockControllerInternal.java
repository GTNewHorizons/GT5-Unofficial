package miscutil.core.multiblock.base.interfaces;

import java.util.Set;

import javax.annotation.Nonnull;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import raisintoast.core.common.CoordTriplet;
import raisintoast.core.multiblock.IMultiblockPart;

public abstract interface IBaseMultiblockControllerInternal
extends IBaseMultiblockController {
	
public abstract void attachBlock(IMultiblockPart paramIMultiblockComponent);

public abstract void detachBlock(IMultiblockPart paramIMultiblockComponent, boolean paramBoolean);

public abstract void checkIfMachineIsWhole();

public abstract void assimilate(IBaseMultiblockController paramIMultiblockControllerInternal);

public abstract void _onAssimilated(IBaseMultiblockController paramIMultiblockControllerInternal);

public abstract void onAssimilated(IBaseMultiblockController paramIMultiblockControllerInternal);

public abstract void updateMultiblockEntity();

public abstract CoordTriplet getReferenceCoord();

public abstract void recalculateMinMaxCoords();

public abstract void formatDescriptionPacket(NBTTagCompound paramNBTTagCompound);

public abstract void decodeDescriptionPacket(NBTTagCompound paramNBTTagCompound);

public abstract World getWorld();

public abstract boolean isEmpty();

public abstract boolean shouldConsume(IBaseMultiblockController paramIMultiblockControllerInternal);

public abstract String getPartsListString();

public abstract void auditParts();

@Nonnull
public abstract Set<IMultiblockPart> checkForDisconnections();

@Nonnull
public abstract Set<IMultiblockPart> detachAllBlocks();
}
