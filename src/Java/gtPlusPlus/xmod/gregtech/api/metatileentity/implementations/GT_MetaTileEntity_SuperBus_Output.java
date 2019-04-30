package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBus;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class GT_MetaTileEntity_SuperBus_Output extends GT_MetaTileEntity_Hatch_OutputBus {
	public GT_Recipe_Map mRecipeMap = null;

	public GT_MetaTileEntity_SuperBus_Output(int aID, String aName, String aNameRegional, int aTier) {
		super(aID, aName, aNameRegional, aTier);
	}

	public GT_MetaTileEntity_SuperBus_Output(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
		super(aName, aTier, aDescription, aTextures);
	}

	public GT_MetaTileEntity_SuperBus_Output(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
		super(aName, aTier, aDescription[0], aTextures);
	}

	/**
	 * Returns a factor of 16 based on tier.
	 * @param aTier The tier of this bus.
	 * @return (1+ aTier) * 16
	 */
	public static int getSlots(int aTier) {
		return (1+ aTier) * 16;
	}

	public boolean isValidSlot(int aIndex) {
		return true;
	}

	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT_MetaTileEntity_SuperBus_Output(this.mName, this.mTier, this.mDescription, this.mTextures);
	}

	public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return null;
	}

	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return null;
	}

	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
		if (aBaseMetaTileEntity.isServerSide() && aBaseMetaTileEntity.hasInventoryBeenModified()) {
			this.fillStacksIntoFirstSlots();
		}

	}

	public void updateSlots() {
		for (int i = 0; i < this.mInventory.length; ++i) {
			if (this.mInventory[i] != null && this.mInventory[i].stackSize <= 0) {
				this.mInventory[i] = null;
			}
		}
		this.fillStacksIntoFirstSlots();
	}

	protected void fillStacksIntoFirstSlots() {
		for (int i = 0; i < this.mInventory.length; ++i) {
			for (int j = i + 1; j < this.mInventory.length; ++j) {
				if (this.mInventory[j] != null && (this.mInventory[i] == null
						|| GT_Utility.areStacksEqual(this.mInventory[i], this.mInventory[j]))) {
					GT_Utility.moveStackFromSlotAToSlotB((IInventory) this.getBaseMetaTileEntity(), (IInventory) this.getBaseMetaTileEntity(), j, i, (byte) 64, (byte) 1, (byte) 64, (byte) 1);
				}
			}
		}
	}

	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		super.saveNBTData(aNBT);
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		super.loadNBTData(aNBT);
	}

	@Override
	public String[] getDescription() {
		String[] aDesc = new String[] {
				"Item Output for Multiblocks",
				"This bus has no GUI",
				""+getSlots(this.mTier)+" Slots",
		};
		return aDesc;
	}

	@Override
	public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, byte aSide, float aX,
			float aY, float aZ) {
		return super.onRightclick(aBaseMetaTileEntity, aPlayer, aSide, aX, aY, aZ);
	}

	public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
		if (aBaseMetaTileEntity.isClientSide()) {
			return true;
		} else {
			displayBusContents(aPlayer);
			return true;
		}
	}
	
	public void displayBusContents(EntityPlayer aPlayer) {
		String STRIP = "Item Array: ";
		String aNameString = ItemUtils.getArrayStackNames(getRealInventory());
		aNameString = aNameString.replace(STRIP, "");

		String[] aNames;
		if (aNameString.length() < 1) {
			aNames = null;
		}
		else {
			aNames = aNameString.split(",");
		}		
		
		if (aNames == null || aNames.length <= 0) {
			PlayerUtils.messagePlayer(aPlayer, "This Super Bus (O) is Empty. Total Slots: "+getSlots(this.mTier));
			return;
		}

		PlayerUtils.messagePlayer(aPlayer, "This Super Bus (O) contains:");
		for (String s : aNames) {
			if (s.startsWith(" ")) {
				s = s.substring(1);
			}			
			//Logger.INFO("Trying to display Super Output Bus contents. "+s);
			PlayerUtils.messagePlayer(aPlayer, s);
		}		
	}

	@Override
	public int getMaxItemCount() {
		// TODO Auto-generated method stub
		return super.getMaxItemCount();
	}

	@Override
	public int getSizeInventory() {
		// TODO Auto-generated method stub
		return super.getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(int aIndex) {
		// TODO Auto-generated method stub
		return super.getStackInSlot(aIndex);
	}

	@Override
	public boolean canInsertItem(int aIndex, ItemStack aStack, int aSide) {
		// TODO Auto-generated method stub
		return super.canInsertItem(aIndex, aStack, aSide);
	}

	@Override
	public boolean canExtractItem(int aIndex, ItemStack aStack, int aSide) {
		// TODO Auto-generated method stub
		return super.canExtractItem(aIndex, aStack, aSide);
	}

	@Override
	public ItemStack[] getRealInventory() {
		// TODO Auto-generated method stub
		return super.getRealInventory();
	}
}