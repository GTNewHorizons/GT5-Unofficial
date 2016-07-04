package miscutil.core.multiblock.base;

import miscutil.core.multiblock.base.interfaces.IBaseMultiblockComponent;
import miscutil.core.multiblock.base.interfaces.IBaseMultiblockControllerInternal;
import miscutil.core.multiblock.base.interfaces.IBaseMultiblockLogic;
import miscutil.core.util.Utils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import raisintoast.core.multiblock.IMultiblockPart;
import raisintoast.core.multiblock.MultiblockRegistry;

public abstract class BaseMultiblockLogic<T extends IBaseMultiblockControllerInternal>
implements IBaseMultiblockLogic
{
	private final Class<T> controllerClass;
	private boolean visited;
	private boolean saveMultiblockData;
	private NBTTagCompound cachedMultiblockData;
	protected T controller;

	protected BaseMultiblockLogic(Class<T> controllerClass)
	{
		this.controllerClass = controllerClass;
		this.controller = null;
		this.visited = false;
		this.saveMultiblockData = false;
		this.cachedMultiblockData = null;
	}

	public void setController(T controller)
	{
		if (controller == null) {
			this.controller = null;
		} else if (this.controllerClass.isAssignableFrom(controller.getClass())) {
			this.controller = (this.controllerClass.cast(controller));
		}
	}

	public Class<T> getControllerClass()
	{
		return this.controllerClass;
	}

	@Override
	public abstract T getController();

	public abstract T createNewController(World paramWorld);

	@Override
	public void validate(World world, IMultiblockPart part)
	{
		MultiblockRegistry.onPartAdded(world, part);
	}

	@Override
	public final void invalidate(World world, IMultiblockPart part)
	{
		detachSelf(world, part, false);
	}

	@Override
	public final void onChunkUnload(World world, IMultiblockPart part)
	{
		detachSelf(world, part, true);
	}

	protected void detachSelf(World world, IMultiblockPart part, boolean chunkUnloading)
	{
		if (this.controller != null)
		{
			this.controller.detachBlock(part, chunkUnloading);


			this.controller = null;
		}
		MultiblockRegistry.onPartRemovedFromWorld(world, part);
	}

	@Override
	public void readFromNBT(NBTTagCompound data)
	{
		if (data.hasKey("multiblockData")) {
			this.cachedMultiblockData = data.getCompoundTag("multiblockData");
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound data)
	{
		if ((isMultiblockSaveDelegate()) && (isConnected()))
		{
			NBTTagCompound multiblockData = new NBTTagCompound();
			((IBaseMultiblockLogic) this.controller).writeToNBT(multiblockData);
			data.setTag("multiblockData", multiblockData);
		}
	}

	public final void assertDetached(IBaseMultiblockComponent part)
	{
		if (this.controller != null)
		{
			ChunkCoordinates coords = part.getCoordinates();
			Utils.LOG_INFO("[assert] Part @ (%d, %d, %d) should be detached already, but detected that it was not. This is not a fatal error, and will be repaired, but is unusual. " + String.valueOf(new Object[] { Integer.valueOf(coords.posX), Integer.valueOf(coords.posY), Integer.valueOf(coords.posZ) }));
			this.controller = null;
		}
	}

	@Override
	public final boolean isConnected()
	{
		return this.controller != null;
	}

	public final void becomeMultiblockSaveDelegate()
	{
		this.saveMultiblockData = true;
	}

	public final void forfeitMultiblockSaveDelegate()
	{
		this.saveMultiblockData = false;
	}

	public final boolean isMultiblockSaveDelegate()
	{
		return this.saveMultiblockData;
	}

	public final void setUnvisited()
	{
		this.visited = false;
	}

	public final void setVisited()
	{
		this.visited = true;
	}

	public final boolean isVisited()
	{
		return this.visited;
	}

	public final boolean hasMultiblockSaveData()
	{
		return this.cachedMultiblockData != null;
	}

	public final NBTTagCompound getMultiblockSaveData()
	{
		return this.cachedMultiblockData;
	}

	public final void onMultiblockDataAssimilated()
	{
		this.cachedMultiblockData = null;
	}

	@Override
	public void encodeDescriptionPacket(NBTTagCompound packetData)
	{
		if ((isMultiblockSaveDelegate()) && (isConnected()))
		{
			NBTTagCompound tag = new NBTTagCompound();
			this.controller.formatDescriptionPacket(tag);
			packetData.setTag("multiblockData", tag);
		}
	}

	@Override
	public void decodeDescriptionPacket(NBTTagCompound packetData)
	{
		if (packetData.hasKey("multiblockData"))
		{
			NBTTagCompound tag = packetData.getCompoundTag("multiblockData");
			if (isConnected()) {
				this.controller.decodeDescriptionPacket(tag);
			} else {
				this.cachedMultiblockData = tag;
			}
		}
	}
}
