package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.machines;

import static gregtech.api.enums.GT_Values.V;

import java.util.UUID;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.core.util.player.PlayerCache;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public abstract class GregtechMetaSafeBlockBase extends GT_MetaTileEntity_TieredMachineBlock {
	public boolean bOutput = false, bRedstoneIfFull = false, bInvert = false, bUnbreakable = false;
	public int mSuccess = 0, mTargetStackSize = 0;
	public UUID ownerUUID;
	//UnbreakableBlockManager Xasda = new UnbreakableBlockManager();
	private boolean value_last = false, value_current = false;

	public GregtechMetaSafeBlockBase(final int aID, final String aName, final String aNameRegional, final int aTier, final int aInvSlotCount, final String aDescription) {
		super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription);
	}

	public GregtechMetaSafeBlockBase(final String aName, final int aTier, final int aInvSlotCount, final String aDescription, final ITexture[][][] aTextures) {
		super(aName, aTier, aInvSlotCount, aDescription, aTextures);
	}

	@Override
	public ITexture[][][] getTextureSet(final ITexture[] aTextures) {
		final ITexture[][][] rTextures = new ITexture[6][17][];
		final ITexture tIcon = this.getOverlayIcon(), tOut = new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_QCHEST), tUp = new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_VENT);
		for (byte i = -1; i < 16; i++) {
			rTextures[0][i + 1] = new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1], tUp, tIcon}; //Back
			rTextures[1][i + 1] = new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1], tIcon}; // Right, Strangely The top side as well when facing East?
			rTextures[2][i + 1] = new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1], tIcon}; // Top  And Bottom, When Facing South (What the hell?)
			rTextures[3][i + 1] = new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1], tIcon}; // Left, Top if facing West and Bottom if facing east?
			rTextures[4][i + 1] = new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1], tIcon}; // Top and Bottom when Facing North..
			rTextures[5][i + 1] = new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][i + 1], tOut}; // Front
		}
		return rTextures;

	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return this.mTextures[5][aColorIndex + 1];
		}
		if (GT_Utility.getOppositeSide(aSide) == aFacing) {
			return this.mTextures[0][aColorIndex + 1];
		}
		switch (aFacing) {
		case 0:
			return this.mTextures[4][aColorIndex + 1];
		case 1:
			return this.mTextures[2][aColorIndex + 1];
		case 2:
			switch (aSide) {
			case 0:
				return this.mTextures[2][aColorIndex + 1];
			case 1:
				return this.mTextures[2][aColorIndex + 1];
			case 4:
				return this.mTextures[1][aColorIndex + 1];
			case 5:
				return this.mTextures[3][aColorIndex + 1];
			}
		case 3:
			switch (aSide) {
			case 0:
				return this.mTextures[4][aColorIndex + 1];
			case 1:
				return this.mTextures[4][aColorIndex + 1];
			case 4:
				return this.mTextures[3][aColorIndex + 1];
			case 5:
				return this.mTextures[1][aColorIndex + 1];
			}
		case 4:
			switch (aSide) {
			case 0:
				return this.mTextures[3][aColorIndex + 1];
			case 1:
				return this.mTextures[1][aColorIndex + 1];
			case 2:
				return this.mTextures[3][aColorIndex + 1];
			case 3:
				return this.mTextures[1][aColorIndex + 1];
			}
		case 5:
			switch (aSide) {
			case 0:
				return this.mTextures[1][aColorIndex + 1];
			case 1:
				return this.mTextures[3][aColorIndex + 1];
			case 2:
				return this.mTextures[1][aColorIndex + 1];
			case 3:
				return this.mTextures[3][aColorIndex + 1];
			}
		}
		return this.mTextures[5][aColorIndex + 1];
	}

	@Override
	public boolean isSimpleMachine() {
		return false;
	}

	@Override
	public boolean isValidSlot(final int aIndex) {
		return aIndex < (this.mInventory.length - 1);
	}

	@Override
	public boolean isFacingValid(final byte aFacing) {
		return true;
	}

	@Override
	public boolean isEnetInput() {
		return true;
	}

	@Override
	public boolean isEnetOutput() {
		return true;
	}

	@Override
	public boolean isInputFacing(final byte aSide) {
		return !this.isOutputFacing(aSide);
	}

	@Override
	public boolean isOutputFacing(final byte aSide) {
		return this.getBaseMetaTileEntity().getBackFacing() == aSide;
	}

	@Override
	public boolean isTeleporterCompatible() {
		return false;
	}

	@Override
	public long getMinimumStoredEU() {
		return 512;
	}

	@Override
	public long maxEUStore() {
		return 512 + (V[this.mTier] * 50);
	}

	@Override
	public long maxEUInput() {
		return V[this.mTier];
	}

	@Override
	public long maxEUOutput() {
		return this.bOutput ? V[this.mTier] : 0;
	}

	@Override
	public long maxAmperesIn() {
		return 1;
	}

	@Override
	public long maxAmperesOut() {
		return 1;
	}

	@Override
	public boolean isAccessAllowed(final EntityPlayer aPlayer) {
		return true;
	}

	public abstract ITexture getOverlayIcon();

	@Override
	public boolean onRightclick(final IGregTechTileEntity aBaseMetaTileEntity, final EntityPlayer aPlayer) {

		if (aBaseMetaTileEntity.isClientSide()) {
			//Utils.LOG_WARNING("Clicky Clicky.");
			return true;

		}
		if (aPlayer != null) {
			final UUID tempUUID = aPlayer.getUniqueID();
			/*if (!aPlayer.worldObj.isRemote){
			//PlayerCache.appendParamChanges(aPlayer.getDisplayName(), aPlayer.getUniqueID().toString());
			}*/
			//Utils.LOG_INFO("test");
			if (this.ownerUUID == null){
				Logger.INFO("No owner yet for this block.");
			}
			else {
				//Utils.LOG_INFO("test");
				Logger.INFO("Current Owner: "+PlayerCache.lookupPlayerByUUID(this.ownerUUID)+" - UUID: "+this.ownerUUID);
			}
			Logger.WARNING("Is ownerUUID Null");
			if (this.ownerUUID == null){
				Logger.WARNING("OwnerUUID is Null, let's set it.");
				Logger.WARNING("Accessing Players UUID is: "+tempUUID);
				this.ownerUUID = tempUUID;
				//Utils.messagePlayer(aPlayer, "Owner of this safe, now set. Try accessing it again.");
				Logger.WARNING("Block Owner is now set to: "+this.ownerUUID);
			}
			Logger.WARNING("No, it is not.");
			Logger.WARNING("Checking ownerUUID.");
			if (this.ownerUUID != null){
				Logger.WARNING("ownerUUID != Null, if accessor == owner.");
				Logger.WARNING("Accessing is: "+PlayerCache.lookupPlayerByUUID(tempUUID));
				if (this.ownerUUID.equals(tempUUID)){
					Logger.WARNING("Owner's UUID: "+this.ownerUUID);
					aBaseMetaTileEntity.openGUI(aPlayer);
					//Utils.LOG_WARNING("GUI should now be open for you sir.");
				}
				else {
					PlayerUtils.messagePlayer(aPlayer, "Access Denied, This does not belong to you.");
					PlayerUtils.messagePlayer(aPlayer, "it is owned by: "+PlayerCache.lookupPlayerByUUID(this.ownerUUID));
					Logger.WARNING("Expecting Player : "+PlayerCache.lookupPlayerByUUID(this.ownerUUID));
					Logger.ERROR("Access Denied.");
					return true;
				}

			}

			/*else {
				Utils.LOG_ERROR("This is NOT good. Tell Draknyte1 your safe broke.");
			}*/
			/*Utils.LOG_WARNING("Clicky Clicky.");
			Utils.messagePlayer(aPlayer, "Owner of this safe, now set.");
			aBaseMetaTileEntity.openGUI(aPlayer);	*/

		}
		return true;
	}

	@Override
	public void saveNBTData(final NBTTagCompound aNBT) {
		aNBT.setBoolean("bUnbreakable", this.bUnbreakable);
		aNBT.setBoolean("bOutput", this.bOutput);
		aNBT.setBoolean("bRedstoneIfFull", this.bRedstoneIfFull);
		aNBT.setInteger("mTargetStackSize", this.mTargetStackSize);
		if (this.ownerUUID != null) {
			aNBT.setString("ownerUUID", this.ownerUUID.toString());
		}
	}

	@Override
	public void loadNBTData(final NBTTagCompound aNBT) {
		this.bUnbreakable = aNBT.getBoolean("bUnbreakable");
		this.bOutput = aNBT.getBoolean("bOutput");
		this.bRedstoneIfFull = aNBT.getBoolean("bRedstoneIfFull");
		this.mTargetStackSize = aNBT.getInteger("mTargetStackSize");
		if (aNBT.hasKey("ownerUUID")) {
			this.ownerUUID = UUID.fromString(aNBT.getString("ownerUUID"));
		}
	}

	@Override
	public void setItemNBT(final NBTTagCompound aNBT) {
		super.setItemNBT(aNBT);
		if (this.mTargetStackSize > 0) {
			aNBT.setInteger("mTargetStackSize", this.mTargetStackSize);
		}
	}

	@Override
	public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTimer) {
		/*if (aBaseMetaTileEntity.isAllowedToWork() && aBaseMetaTileEntity.isServerSide() && aBaseMetaTileEntity.isUniversalEnergyStored(getMinimumStoredEU()) && (aBaseMetaTileEntity.hasWorkJustBeenEnabled() || aBaseMetaTileEntity.hasInventoryBeenModified() || aTimer % 200 == 0 || mSuccess > 0)) {
		 */
		if (aBaseMetaTileEntity.isServerSide()  && (aBaseMetaTileEntity.hasWorkJustBeenEnabled() || aBaseMetaTileEntity.hasInventoryBeenModified() || ((aTimer % 200) == 0) || (this.mSuccess > 0))) {
			this.value_last = this.value_current;
			this.value_current = this.bUnbreakable;
			if (this.value_last != this.value_current){
				Logger.WARNING("VALUE CHANGE - Ticking for a moment.");
				if (this.bUnbreakable == true){
					//Xasda.setmTileEntity((BaseMetaTileEntity) aBaseMetaTileEntity);
					//Utils.LOG_ERROR("Safe is Indestructible.");
					this.getBaseMetaTileEntity().getBlock(this.getBaseMetaTileEntity().getXCoord(), this.getBaseMetaTileEntity().getYCoord(), this.getBaseMetaTileEntity().getZCoord()).setResistance(Float.MAX_VALUE);
					this.getBaseMetaTileEntity().getBlock(this.getBaseMetaTileEntity().getXCoord(), this.getBaseMetaTileEntity().getYCoord(), this.getBaseMetaTileEntity().getZCoord()).setBlockUnbreakable();
				}
				else {
					//Xasda.setmTileEntity((BaseMetaTileEntity) aBaseMetaTileEntity);
					//Utils.LOG_ERROR("Safe is not Indestructible.");
					this.getBaseMetaTileEntity().getBlock(this.getBaseMetaTileEntity().getXCoord(), this.getBaseMetaTileEntity().getYCoord(), this.getBaseMetaTileEntity().getZCoord()).setResistance(1F);
					this.getBaseMetaTileEntity().getBlock(this.getBaseMetaTileEntity().getXCoord(), this.getBaseMetaTileEntity().getYCoord(), this.getBaseMetaTileEntity().getZCoord()).setHardness(2);

				}
			}
			else {

			}

		}
	}

	@Override
	public boolean allowPullStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex, final byte aSide, final ItemStack aStack) {
		return false;
	}

	@Override
	public boolean allowPutStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex, final byte aSide, final ItemStack aStack) {
		return aSide != aBaseMetaTileEntity.getBackFacing();
	}
}