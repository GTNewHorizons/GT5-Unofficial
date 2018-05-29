package gtPlusPlus.xmod.gregtech.common.tileentities.storage.creative;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;

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
		if (aBaseMetaTileEntity.getWorld().isRemote) {
			return false;
		}
		
		if (!KeyboardUtils.isShiftKeyDown()) {
			if (this.mItemStack == null) {
				if (aPlayer.getHeldItem() != null) {
					this.mItemStack = aPlayer.getHeldItem().copy();
					this.mItemCount = Short.MAX_VALUE;
					aPlayer.setCurrentItemOrArmor(0, null);
					PlayerUtils.messagePlayer(aPlayer, "Now holding "+this.mItemStack.getDisplayName()+" x"+Short.MAX_VALUE+".");
					return true;
				}
			}
			else {
				if (aPlayer.getHeldItem() == null) {
					aPlayer.entityDropItem(mItemStack, 1);
					this.mItemStack = null;
					this.mItemCount = 0;
					PlayerUtils.messagePlayer(aPlayer, "Emptying.");
					return true;
				}
			}
		}
		
		PlayerUtils.messagePlayer(aPlayer, "Currently holding: "+(this.mItemStack != null ? this.mItemStack.getDisplayName() : "Nothing")+" x"+this.mItemCount);
		return true;
		//return super.onRightclick(aBaseMetaTileEntity, aPlayer);
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
		if (mItemStack != null) {
			setItemCount(0);
		}
		super.onPostTick(aBaseMetaTileEntity, aTimer);
	}

	@Override
	public void setItemCount(int aCount) {
		super.setItemCount(Short.MAX_VALUE);
	}

	@Override
	public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return true;
	}

	@Override
	public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return false;
	}
	
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT_MetaTileEntity_InfiniteItemHolder(this.mName, this.mTier, this.mDescription, this.mTextures);
	}





}
