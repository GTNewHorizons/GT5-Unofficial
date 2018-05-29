package gtPlusPlus.xmod.gregtech.common.tileentities.storage.creative;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.core.util.sys.KeyboardUtils;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.GT_MetaTileEntity_TieredChest;

public class GT_MetaTileEntity_InfiniteItemHolder extends GT_MetaTileEntity_TieredChest {

	public GT_MetaTileEntity_InfiniteItemHolder(int aID, String aName, String aNameRegional, int aTier) {
		super(aID, aName, aNameRegional, aTier);
	}

	public GT_MetaTileEntity_InfiniteItemHolder(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
		super(aName, aTier, aDescription, aTextures);
	}

	@Override
	public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
		
		if (aPlayer.worldObj.isRemote) {
			return false;
		}
		
		if (!KeyboardUtils.isShiftKeyDown()) {
			if (this.mItemStack == null) {
				if (aPlayer.getHeldItem() != null) {
					this.mItemStack = aPlayer.getHeldItem().copy();
					this.mItemCount = Short.MAX_VALUE;
				}
			}
			else {
				if (aPlayer.getHeldItem() == null) {
					this.mItemStack = null;
					this.mItemCount = 0;
				}
			}
		}
		else {
			PlayerUtils.messagePlayer(aPlayer, "Currently holding: "+(this.mItemStack != null ? this.mItemStack.getDisplayName() : "Nothing")+" x"+this.mItemCount);
		}
		return super.onRightclick(aBaseMetaTileEntity, aPlayer);
	}

	@Override
	public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return null;
	}

	@Override
	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return null;
	}

	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
		super.onPostTick(aBaseMetaTileEntity, aTimer);
	}

	@Override
	public void setItemCount(int aCount) {
		super.setItemCount(Short.MAX_VALUE);
	}

	@Override
	public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		// TODO Auto-generated method stub
		return super.allowPullStack(aBaseMetaTileEntity, aIndex, aSide, aStack);
	}

	@Override
	public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		// TODO Auto-generated method stub
		return super.allowPutStack(aBaseMetaTileEntity, aIndex, aSide, aStack);
	}





}
