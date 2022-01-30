package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.extensions.ArrayExt;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class GT_MetaTileEntity_SuperBus_Input extends GT_MetaTileEntity_Hatch_InputBus {
	public GT_MetaTileEntity_SuperBus_Input(int aID, String aName, String aNameRegional, int aTier) {
		super(aID, aName, aNameRegional, aTier, getSlots(aTier));
	}

	public GT_MetaTileEntity_SuperBus_Input(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
		super(aName, aTier, getSlots(aTier), aDescription, aTextures);
	}

	/**
	 * Returns a factor of 16 based on tier.
	 *
	 * @param aTier The tier of this bus.
	 * @return (1 + aTier) * 16
	 */
	public static int getSlots(int aTier) {
		return (1 + aTier) * 16;
	}

	public ITexture[] getTexturesActive(ITexture aBaseTexture) {
		return new ITexture[]{aBaseTexture, new GT_RenderedTexture(BlockIcons.OVERLAY_PIPE_IN)};
	}

	public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
		return new ITexture[]{aBaseTexture, new GT_RenderedTexture(BlockIcons.OVERLAY_PIPE_IN)};
	}

	public boolean isSimpleMachine() {
		return true;
	}

	public boolean isFacingValid(byte aFacing) {
		return true;
	}

	public boolean isAccessAllowed(EntityPlayer aPlayer) {
		return true;
	}

	public boolean isValidSlot(int aIndex) {
		return true;
	}

	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT_MetaTileEntity_SuperBus_Input(this.mName, this.mTier, ArrayExt.of(this.mDescription), this.mTextures);
	}

	public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return null;
	}

	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return null;
	}

	public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return aSide == this.getBaseMetaTileEntity().getFrontFacing();
	}

	@Override
	public String[] getDescription() {
		String[] aDesc = new String[] {
				"Item Input for Multiblocks",
				"This bus has no GUI, but can have items extracted",
				""+getSlots(this.mTier)+" Slots",
				CORE.GT_Tooltip
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
			//Logger.INFO("Trying to display Super Input Bus contents.");
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
			PlayerUtils.messagePlayer(aPlayer, "This Super Bus (I) is Empty. Total Slots: "+getSlots(this.mTier));
			return;
		}

		PlayerUtils.messagePlayer(aPlayer, "This Super Bus (I) contains: ["+getRealInventory().length+"]");
		
		if (aNames.length <= 12) {
			for (String s : aNames) {
				if (s.startsWith(" ")) {
					s = s.substring(1);
				}			
				//Logger.INFO("Trying to display Super Input Bus contents. "+s);
				PlayerUtils.messagePlayer(aPlayer, s);
			}
		}
		else {
			
			String superString = "";
			
			for (String s : aNames) {
				if (s.startsWith(" ")) {
					s = s.substring(1);
				}			
				superString += (s+", ");
			}
			PlayerUtils.messagePlayer(aPlayer, superString);
		}
		
				
	}

}