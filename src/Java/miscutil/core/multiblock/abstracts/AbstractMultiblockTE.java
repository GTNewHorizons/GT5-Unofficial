package miscutil.core.multiblock.abstracts;


import miscutil.core.handler.GuiHandler;
import miscutil.core.multiblock.abstracts.interfaces.IAbstractMultiblockLogic;
import miscutil.core.multiblock.base.BaseMultiblockTE;
import miscutil.core.xmod.forestry.core.inventory.FakeInventoryAdapter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.world.World;

import com.mojang.authlib.GameProfile;

public abstract class AbstractMultiblockTE<T extends IAbstractMultiblockLogic>
extends BaseMultiblockTE<T>  implements ISidedInventory {

	private GameProfile owner;

	public AbstractMultiblockTE(T multiblockLogic)
	{
		super(multiblockLogic);
	}

	public void openGui(EntityPlayer player)
	{
		GuiHandler.openGui(player, this);
	}

	@Override
	public void readFromNBT(NBTTagCompound data)
	{
		super.readFromNBT(data);
		if (data.hasKey("owner")) {
			this.owner = NBTUtil.func_152459_a(data.getCompoundTag("owner"));
		}
		getInternalInventory().readFromNBT(data);
	}

	@Override
	public void writeToNBT(NBTTagCompound data)
	{
		super.writeToNBT(data);
		if (this.owner != null)
		{
			NBTTagCompound nbt = new NBTTagCompound();
			NBTUtil.func_152460_a(nbt, this.owner);
			data.setTag("owner", nbt);
		}
		getInternalInventory().writeToNBT(data);
	}

	public FakeInventoryAdapter getInternalInventory()
	{
		return FakeInventoryAdapter.instance();
	}

	public boolean allowsAutomation()
	{
		return false;
	}

	@Override
	public final int getSizeInventory()
	{
		return getInternalInventory().getSizeInventory();
	}

	@Override
	public final ItemStack getStackInSlot(int slotIndex)
	{
		return getInternalInventory().getStackInSlot(slotIndex);
	}

	@Override
	public final ItemStack decrStackSize(int slotIndex, int amount)
	{
		return getInternalInventory().decrStackSize(slotIndex, amount);
	}

	@Override
	public final ItemStack getStackInSlotOnClosing(int slotIndex)
	{
		return getInternalInventory().getStackInSlotOnClosing(slotIndex);
	}

	@Override
	public final void setInventorySlotContents(int slotIndex, ItemStack itemstack)
	{
		getInternalInventory().setInventorySlotContents(slotIndex, itemstack);
	}

	@Override
	public final int getInventoryStackLimit()
	{
		return getInternalInventory().getInventoryStackLimit();
	}

	@Override
	public final void openInventory()
	{
		getInternalInventory().openInventory();
	}

	@Override
	public final void closeInventory()
	{
		getInternalInventory().closeInventory();
	}

	@Override
	public final String getInventoryName()
	{
		return getInternalInventory().getInventoryName();
	}

	@Override
	public final boolean isUseableByPlayer(EntityPlayer player)
	{
		return getInternalInventory().isUseableByPlayer(player);
	}

	@Override
	public final boolean hasCustomInventoryName()
	{
		return getInternalInventory().hasCustomInventoryName();
	}

	@Override
	public final boolean isItemValidForSlot(int slotIndex, ItemStack itemStack)
	{
		return getInternalInventory().isItemValidForSlot(slotIndex, itemStack);
	}

	@Override
	public final int[] getAccessibleSlotsFromSide(int side)
	{
		if (allowsAutomation()) {
			return getAccessibleSlotsFromSide(side);
		}
		return FakeInventoryAdapter.SLOTS_NONE;
	}

	@Override
	public final boolean canInsertItem(int slotIndex, ItemStack itemStack, int side)
	{
		if (allowsAutomation()) {
			return canInsertItem(slotIndex, itemStack, side);
		}
		return false;
	}

	@Override
	public final boolean canExtractItem(int slotIndex, ItemStack itemStack, int side)
	{
		if (allowsAutomation()) {
			return getInternalInventory().canExtractItem(slotIndex, itemStack, side);
		}
		return false;
	}

	@Override
	public final boolean canSlotAccept(int slotIndex, ItemStack itemStack)
	{
		return getInternalInventory().canSlotAccept(slotIndex, itemStack);
	}

	@Override
	public final boolean isLocked(int slotIndex)
	{
		return getInternalInventory().isLocked(slotIndex);
	}

	@Override
	public final World getWorld()
	{
		return this.worldObj;
	}

	public final GameProfile getOwner()
	{
		return this.owner;
	}

	public final void setOwner(GameProfile owner)
	{
		this.owner = owner;
	}



}
