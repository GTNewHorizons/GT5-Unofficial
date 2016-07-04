package miscutil.core.multiblock.base;

import miscutil.core.interfaces.IGuiManager;
import miscutil.core.multiblock.base.interfaces.IBaseMultiblockController;
import miscutil.core.multiblock.base.interfaces.IBaseMultiblockLogic;
import miscutil.core.xmod.forestry.core.interfaces.IFilterSlotDelegate;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.ChunkCoordinates;
import raisintoast.core.multiblock.IMultiblockPart;

public abstract class BaseMultiblockTE<T extends IBaseMultiblockLogic> extends IMultiblockPart implements IFilterSlotDelegate, IGuiManager
{
	private final T multiblockLogic;

	public BaseMultiblockTE(T multiblockLogic)
	{
		this.multiblockLogic = multiblockLogic;
	}

	@Override
	public ChunkCoordinates getCoordinates()
	{
		return new ChunkCoordinates(this.xCoord, this.yCoord, this.zCoord);
	}

	public T getMultiblockLogic()
	{
		return this.multiblockLogic;
	}

	public abstract void onMachineAssembled(IBaseMultiblockController paramIMultiblockController, ChunkCoordinates paramChunkCoordinates1, ChunkCoordinates paramChunkCoordinates2);

	@Override
	public abstract void onMachineBroken();

	@Override
	public void readFromNBT(NBTTagCompound data)
	{
		super.readFromNBT(data);
		this.multiblockLogic.readFromNBT(data);
	}

	@Override
	public void writeToNBT(NBTTagCompound data)
	{
		super.writeToNBT(data);
		this.multiblockLogic.writeToNBT(data);
	}

	@Override
	public boolean canUpdate()
	{
		return false;
	}

	@Override
	public final void invalidate()
	{
		super.invalidate();
		this.multiblockLogic.invalidate(this.worldObj, this);
	}

	@Override
	public final void onChunkUnload()
	{
		super.onChunkUnload();
		this.multiblockLogic.onChunkUnload(this.worldObj, this);
	}

	@Override
	public final void validate()
	{
		super.validate();
		this.multiblockLogic.validate(this.worldObj, this);
	}

	@Override
	public final Packet getDescriptionPacket()
	{
		NBTTagCompound packetData = new NBTTagCompound();
		this.multiblockLogic.encodeDescriptionPacket(packetData);
		encodeDescriptionPacket(packetData);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, packetData);
	}

	@Override
	public final void onDataPacket(NetworkManager network, S35PacketUpdateTileEntity packet)
	{
		NBTTagCompound nbtData = packet.func_148857_g();
		this.multiblockLogic.decodeDescriptionPacket(nbtData);
		decodeDescriptionPacket(nbtData);
	}

	protected void encodeDescriptionPacket(NBTTagCompound packetData) {}

	protected void decodeDescriptionPacket(NBTTagCompound packetData) {}
}
