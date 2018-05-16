package gtPlusPlus.xmod.gregtech.common.tileentities.storage.shelving;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.api.gui.CONTAINER_SuperChest;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_SuperChest;

public class GT4Entity_Shelf_Large extends GT4Entity_Shelf {

	private final int mSize;
	public int mItemCount;
	public ItemStack mItemStack;


	public GT4Entity_Shelf_Large(final int aID, final String aName, final String aNameRegional, final String aDescription, final int aSize) {
		super(aID, aName, aNameRegional, aDescription);
		this.mSize = aSize;
		this.mItemCount = 0;
		this.mItemStack = null;
	}

	public GT4Entity_Shelf_Large(String mName, String mDescriptionArray, final int aSize, ITexture[][][] mTextures) {
		super(mName, mDescriptionArray, mTextures);
		this.mSize = aSize;
		this.mItemCount = 0;
		this.mItemStack = null;
	}

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT4Entity_Shelf_Large(this.mName, this.mDescription, mSize, this.mTextures);
	}

	/*@Override
	public int getInvSize() {
		return (mSize > 0 && mSize < 255 ? mSize : 255);
	}*/

	@Override
	public boolean isGivingInformation() {
		return true;
	}

	@Override
	public boolean isDigitalChest() {
		return true;
	}

	@Override
	public boolean isValidSlot(final int aIndex) {
		return true;
	}

	@Override
	public boolean onRightclick(final IGregTechTileEntity aBaseMetaTileEntity, final EntityPlayer aPlayer) {
		//Single Block Behaviour
		//return super.onRightclick(aTile, aPlayer);		
		if (aBaseMetaTileEntity.isClientSide()) {
			return true;
		}	
		
		String itemName = (this.mItemStack != null ? this.mItemStack.getDisplayName() : "Nothing.");
		String itemAmount = (this.mItemCount > 0 ? ""+this.mItemCount : "bad");
		String itemMessage = "This container currently holds "+(itemAmount.equalsIgnoreCase("bad") ? "nothing." : itemName+" x"+itemAmount+".");
		PlayerUtils.messagePlayer(aPlayer, itemMessage);
	
		aBaseMetaTileEntity.openGUI(aPlayer);
		return true;
	}

	public Object getServerGUI(final int aID, final InventoryPlayer aPlayerInventory,
			final IGregTechTileEntity aBaseMetaTileEntity) {
		return new CONTAINER_SuperChest(aPlayerInventory, aBaseMetaTileEntity);
	}

	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory,
			final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_SuperChest(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName());
	}

	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		aNBT.setInteger("mItemCount", this.mItemCount);
		if (this.mItemStack != null) {
			aNBT.setTag("mItemStack", (NBTBase) this.mItemStack.writeToNBT(new NBTTagCompound()));
		}
		super.saveNBTData(aNBT);

	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		if (aNBT.hasKey("mItemCount")) {
			this.mItemCount = aNBT.getInteger("mItemCount");
		}
		if (aNBT.hasKey("mItemStack")) {
			this.mItemStack = ItemStack.loadItemStackFromNBT((NBTTagCompound) aNBT.getTag("mItemStack"));
		}
		super.loadNBTData(aNBT);
	}

	@Override
	public void onOpenGUI() {
		super.onOpenGUI();
	}

	public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTimer) {		
		if (this.getBaseMetaTileEntity().isServerSide() && this.getBaseMetaTileEntity().isAllowedToWork()) {

			if (this.mInventory[0] != null) {
				this.mType = (byte) this.mIndex;		
			}
			else {
				this.mType = 0;	
			}

			if (this.getItemCount() <= 0) {
				this.mItemStack = null;
				this.mItemCount = 0;
			}
			if (this.mItemStack == null && this.mInventory[0] != null) {
				this.mItemStack = this.mInventory[0].copy();
			}
			if (this.mInventory[0] != null && this.mItemCount < this.getMaxItemCount()
					&& GT_Utility.areStacksEqual(this.mInventory[0], this.mItemStack)) {
				this.mItemCount += this.mInventory[0].stackSize;
				if (this.mItemCount > this.getMaxItemCount()) {
					this.mInventory[0].stackSize = this.mItemCount - this.getMaxItemCount();
					this.mItemCount = this.getMaxItemCount();
				} else {
					this.mInventory[0] = null;
				}
			}
			if (this.mInventory[1] == null && this.mItemStack != null) {
				this.mInventory[1] = this.mItemStack.copy();
				this.mInventory[1].stackSize = Math.min(this.mItemStack.getMaxStackSize(), this.mItemCount);
				this.mItemCount -= this.mInventory[1].stackSize;
			} else if (this.mItemCount > 0 && GT_Utility.areStacksEqual(this.mInventory[1], this.mItemStack)
					&& this.mInventory[1].getMaxStackSize() > this.mInventory[1].stackSize) {
				final int tmp = Math.min(this.mItemCount,
						this.mInventory[1].getMaxStackSize() - this.mInventory[1].stackSize);
				final ItemStack itemStack = this.mInventory[1];
				itemStack.stackSize += tmp;
				this.mItemCount -= tmp;
			}
			if (this.mItemStack != null) {
				this.mInventory[2] = this.mItemStack.copy();
				this.mInventory[2].stackSize = Math.min(this.mItemStack.getMaxStackSize(), this.mItemCount);
			} else {
				this.mInventory[2] = null;
			}
		}
	}

	private int getItemCount() {
		return this.mItemCount;
	}

	public void setItemCount(final int aCount) {
		this.mItemCount = aCount;
	}

	public int getProgresstime() {
		return this.mItemCount + ((this.mInventory[0] == null) ? 0 : this.mInventory[0].stackSize)
				+ ((this.mInventory[1] == null) ? 0 : this.mInventory[1].stackSize);
	}

	public int maxProgresstime() {
		return this.getMaxItemCount();
	}

	public int getMaxItemCount() {
		return this.mSize;
	}

	public boolean allowPullStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex, final byte aSide,
			final ItemStack aStack) {
		return aIndex == 1;
	}

	public boolean allowPutStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex, final byte aSide,
			final ItemStack aStack) {
		return aIndex == 0 && ((this.mInventory[0] == null && this.mItemStack == null) || GT_Utility.areStacksEqual(this.mInventory[0], aStack) || (this.mItemStack != null && GT_Utility.areStacksEqual(this.mItemStack, aStack)));
	}

	public String[] getInfoData() {
		if (this.mItemStack == null) {
			return new String[]{
					this.getLocalName(), "No Items Stored", "Free Space: "+Integer.toString(this.getMaxItemCount())};
		}
		return new String[]{
				this.getLocalName(),
				"Storing: "+this.mItemStack.getDisplayName()+" x"+Integer.toString(this.mItemCount),
				"Space Remaining: "+Integer.toString(this.getMaxItemCount()-this.getItemCount())+"/"+Integer.toString(this.getMaxItemCount())};
	}


}
