package gtPlusPlus.xmod.gregtech.common.tileentities.redstone;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.api.objects.GT_ItemStack;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public abstract class GT_MetaTileEntity_RedstoneBase extends GT_MetaTileEntity_TieredMachineBlock {

	protected int mOpenerCount;

	public GT_MetaTileEntity_RedstoneBase(int aID, String aName, String aNameRegional, int aTier, int aInvSlotCount, String aDescription, ITexture... aTextures) {
		super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription, aTextures);
	}

	public GT_MetaTileEntity_RedstoneBase(int aID, String aName, String aNameRegional, int aTier, int aInvSlotCount, String[] aDescription, ITexture... aTextures) {
		super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription, aTextures);
	}

	public GT_MetaTileEntity_RedstoneBase(String aName, int aTier, int aInvSlotCount, String aDescription, ITexture[][][] aTextures) {
		super(aName, aTier, aInvSlotCount, aDescription, aTextures);
	}

	public GT_MetaTileEntity_RedstoneBase(String aName, int aTier, int aInvSlotCount, String[] aDescription, ITexture[][][] aTextures) {
		super(aName, aTier, aInvSlotCount, aDescription, aTextures);
	}

	@Override
	public final boolean isSimpleMachine() {
		return false;
	}

	@Override
	public boolean allowCoverOnSide(byte aSide, GT_ItemStack aStack) {
		return aSide != getBaseMetaTileEntity().getFrontFacing();
	}

	@Override
	public final boolean isValidSlot(int aIndex) {
		return false;
	}

	@Override
	public final boolean isFacingValid(byte aFacing) {
		return true;
	}

	@Override
	public final boolean isAccessAllowed(EntityPlayer aPlayer) {
		return true;
	}

	@Override
	public abstract void saveNBTData(NBTTagCompound aNBT);

	@Override
	public abstract void loadNBTData(NBTTagCompound aNBT);

	@Override
	public boolean onRightclick(final IGregTechTileEntity aBaseMetaTileEntity, final EntityPlayer aPlayer) {
		return false;
	}

	@Override
	public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, gregtech.api.interfaces.tileentity.IGregTechTileEntity aBaseMetaTileEntity) {
		return null;
	}

	@Override
	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, gregtech.api.interfaces.tileentity.IGregTechTileEntity aBaseMetaTileEntity) {
		return null;
	}

	@Override
	public final void onOpenGUI() {
		super.onOpenGUI();
		mOpenerCount++;
	}

	@Override
	public final void onCloseGUI() {
		super.onCloseGUI();
		mOpenerCount--;
	}
	
	public final boolean hasRedstoneSignal() {
		if (getBaseMetaTileEntity().getStrongestRedstone() > 0) {
			return true;
		}
		for (byte i=0;i<6;i++) {
			if (getBaseMetaTileEntity().getOutputRedstoneSignal(i) > 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return false;
	}

	@Override
	public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return false;
	}

	@Override
	public String[] getDescription() {
		return new String[]{
				this.mDescription, 
				CORE.GT_Tooltip
		};
	}

}
