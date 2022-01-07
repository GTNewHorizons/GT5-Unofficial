package gtPlusPlus.xmod.gregtech.common.tileentities.storage.shelving;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import gregtech.api.enums.*;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicHull_NonElectric;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;
import gregtech.common.items.GT_MetaGenerated_Tool_01;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.core.util.sys.KeyboardUtils;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock.CustomIcon;

public class GT4Entity_Shelf extends GT_MetaTileEntity_BasicHull_NonElectric {
	public byte mType = 0;
	public String mOldDesc = "";
	public boolean mLocked = false;
	protected byte mIndex = (byte) MathUtils.randInt(1, 3);
	public static GT_RenderedTexture texBottom =  new GT_RenderedTexture(new CustomIcon("TileEntities/gt4/machine_bottom"));
	public static GT_RenderedTexture texTop =  new GT_RenderedTexture(new CustomIcon("TileEntities/gt4/machine_top"));
	public static GT_RenderedTexture texSide =  new GT_RenderedTexture(new CustomIcon("TileEntities/gt4/machine_side"));
	public static GT_RenderedTexture texSideCabinet =  new GT_RenderedTexture(new CustomIcon("TileEntities/gt4/machine_side_cabinet"));

	public GT4Entity_Shelf(final int aID, final String aName, final String aNameRegional, final String aDescription) {
		super(aID, aName, aNameRegional, 0, aDescription);
		mOldDesc = aDescription;
	}

	public GT4Entity_Shelf(final String aName, final String aDescription, final ITexture[][][] aTextures) {
		super(aName, 0, aDescription, aTextures);
		mOldDesc = aDescription;
	}

	@Override
	public boolean isSimpleMachine() {
		return true;
	}

	public int getInvSize() {
		return 1;
	}

	@Override
	public boolean isFacingValid(byte aFacing) {
		return aFacing > 1;
	}

	@Override
	public boolean isAccessAllowed(EntityPlayer aPlayer) {		
		if (this.getBaseMetaTileEntity().getOwnerName().equalsIgnoreCase(aPlayer.getDisplayName())) {
			return true;
		}
		PlayerUtils.messagePlayer(aPlayer, "This container belongs to "+this.getBaseMetaTileEntity().getOwnerName()+".");
		return false;
	}

	@Override
	public boolean ownerControl() {
		return false;
	}

	@Override
	public boolean isEnetOutput() {
		return false;
	}

	@Override
	public boolean isEnetInput() {
		return false;
	}

	@Override
	public boolean isOutputFacing(final byte aSide) {
		return aSide != this.getBaseMetaTileEntity().getFrontFacing();
	}

	@Override
	public boolean isInputFacing(byte aSide) {
		return false;
	}

	@Override
	public boolean onRightclick(IGregTechTileEntity aTile, EntityPlayer aPlayer) {	
		if (aTile.isClientSide()) {
			return true;
		}
		ItemStack tStack = aPlayer.inventory.getStackInSlot(aPlayer.inventory.currentItem);
		if (tStack == null) {
			if (KeyboardUtils.isCtrlKeyDown()) {
				if (this.mInventory[0] != null) {
					PlayerUtils.messagePlayer(aPlayer, "Contains "+this.mInventory[0].getDisplayName()+" x"+this.mInventory[0].stackSize+".");
				}
				return false;
			}
			else {
				if ((this.mInventory[0] != null) && (this.mInventory[0].stackSize > 0)) {
					if (!this.mLocked) {
						PlayerUtils.messagePlayer(aPlayer, "Removed "+this.mInventory[0].getDisplayName()+" x"+this.mInventory[0].stackSize+".");
						aPlayer.inventory.setInventorySlotContents(aPlayer.inventory.currentItem, this.mInventory[0]);
						getBaseMetaTileEntity().setInventorySlotContents(0, null);
						this.mType = 0;
						return true;
					}
					else {
						PlayerUtils.messagePlayer(aPlayer, "This container is locked. It belongs to "+aTile.getOwnerName()+".");
						return false;
					}
				}
				return false;
			}			
		}
		else if (tStack != null && this.mInventory[0] == null) {

			if (KeyboardUtils.isCtrlKeyDown() || GT_MetaGenerated_Tool_01.class.isInstance(tStack.getItem())) {
				return false;
			}			
			PlayerUtils.messagePlayer(aPlayer, "Stored "+tStack.getDisplayName()+" x"+tStack.stackSize+".");
			aPlayer.inventory.setInventorySlotContents(aPlayer.inventory.currentItem, null);
			getBaseMetaTileEntity().setInventorySlotContents(0, tStack);
			this.mType = (byte) MathUtils.randInt(1, 3);
			return true;
		}
		else {
			if (this.mInventory[0] == null) {
				this.mType = 0;	
			}
			return super.onRightclick(aTile, aPlayer);
		}
	}

	@Override
	public void onLeftclick(IGregTechTileEntity aTile,EntityPlayer aPlayer) {
		if ((this.mInventory[0] != null) && (this.mInventory[0].stackSize > 0)) {
			ItemStack tOutput = GT_Utility.copy(new Object[] { this.mInventory[0] });
			if (!aPlayer.isSneaking()) {
				tOutput.stackSize = 1;
			}
			getBaseMetaTileEntity().decrStackSize(0, tOutput.stackSize);
			EntityItem tEntity = new EntityItem(getBaseMetaTileEntity().getWorld(),
					getBaseMetaTileEntity().getOffsetX(getBaseMetaTileEntity().getFrontFacing(), 1) + 0.5D,
					getBaseMetaTileEntity().getOffsetY(getBaseMetaTileEntity().getFrontFacing(), 1) + 0.5D,
					getBaseMetaTileEntity().getOffsetZ(getBaseMetaTileEntity().getFrontFacing(), 1) + 0.5D, tOutput);
			tEntity.motionX = 0.0D;
			tEntity.motionY = 0.0D;
			tEntity.motionZ = 0.0D;
			getBaseMetaTileEntity().getWorld().spawnEntityInWorld(tEntity);
			if (this.mInventory[0] == null) {
				this.mType = 0;
			}
		}
		super.onLeftclick(aTile, aPlayer);
	}

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT4Entity_Shelf(this.mName, this.mDescription, this.mTextures);
	}

	@Override
	public void saveNBTData(NBTTagCompound aNBT) {//mLocked
		aNBT.setInteger("mType", this.mType);
		aNBT.setBoolean("mLocked", this.mLocked);
		aNBT.setByte("mIndex", this.mIndex);
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		if (aNBT.hasKey("mIndex")) {
			this.mType = ((byte) aNBT.getInteger("mType"));
		}
		if (aNBT.hasKey("mIndex")) {
			this.mLocked = (aNBT.getBoolean("mLocked"));
		}
		if (aNBT.hasKey("mIndex")) {
			this.mIndex = aNBT.getByte("mIndex");
		}
	}

	@Override
	public void onValueUpdate(byte aValue) {
		this.mType = aValue;
	}

	@Override
	public byte getUpdateData() {
		return this.mType;
	}

	@Override
	public String[] getDescription() {
		return new String[] { 
				mOldDesc,
				"Decorative Item Storage", 
				"Right click to store/remove something",
				"Ctrl + Rmb to check contents",
				"Ctrl + Rmb with a screwdriver to lock",
				};
	}

	@Override
	public byte getTileEntityBaseType() {
		return 0;
	}

	@Override
	public boolean allowCoverOnSide(byte aSide, GT_ItemStack aStack) {
		//return aSide != getBaseMetaTileEntity().getFrontFacing();
		return false;
	}

	@Override
	public boolean allowPullStack(IGregTechTileEntity p0, int p1, byte p2, ItemStack p3) {
		return true;
	}

	@Override
	public boolean allowPutStack(IGregTechTileEntity p0, int p1, byte p2, ItemStack p3) {
		return false;
	}

	@Override
	public ITexture[][][] getTextureSet(ITexture[] aTextures) {
		ITexture[][][] rTextures = new ITexture[7][17][];
		for (byte i = -1; i < 16; i = (byte) (i + 1)) {
			ITexture[] tmp0 = {this.getBottom((byte) 0)[0]};
			rTextures[0][(i + 1)] = tmp0;
			ITexture[] tmp1 = { this.getTop((byte) 0)[0]};
			rTextures[1][(i + 1)] = tmp1;
			ITexture[] tmp2 = { this.getSides((byte) 0)[0]};
			rTextures[2][(i + 1)] = tmp2;
			ITexture[] tmp4 = {this.getSides((byte) 0)[0]};
			rTextures[3][(i + 1)] = tmp4;
			ITexture[] tmp5 = {this.getSides((byte) 0)[0], getFront((byte) 0)[0]};
			rTextures[4][(i + 1)] = tmp5;
			ITexture[] tmp6 = {this.getSides((byte) 0)[0], getFront((byte) 1)[0]};
			rTextures[5][(i + 1)] = tmp6;
			ITexture[] tmp7 = {this.getSides((byte) 0)[0], getFront((byte) 2)[0]};
			rTextures[6][(i + 1)] = tmp7;
		}
		return rTextures;
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity,
			final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive,
			final boolean aRedstone) {

		ITexture[] tmp = this.mTextures[(aSide >= 2) ? ((aSide != aFacing) ? 2 : ((byte) this.mType == 0 ? 4 : this.mType == 1 || this.mType == 2 ? 5 : this.mType == 3 ? 6 : 0)) : aSide][aColorIndex + 1];
		if (aSide != aFacing && tmp.length == 2) {
			tmp = new ITexture[]{tmp[0]};
		}
		return tmp;
	}

	private static GT_RenderedTexture x1 = new GT_RenderedTexture(TexturesGtBlock.OVERLAY_WOODEN_SHELF_FRONT);
	private static GT_RenderedTexture x2 = new GT_RenderedTexture(TexturesGtBlock.OVERLAY_WOODEN_SHELF_PAPER_FRONT);
	private static GT_RenderedTexture x3 = new GT_RenderedTexture(TexturesGtBlock.OVERLAY_WOODEN_SHELF_CANS_FRONT);

	public ITexture[] getFrontNormal() {	
		return new ITexture[]{x1};
	}
	public ITexture[] getFrontActive() {	
		return new ITexture[]{x2};
	}
	public ITexture[] getFrontAlternative() {	
		return new ITexture[]{x3};
	}

	public ITexture[] getBack(final byte aColor) {
		return new ITexture[]{ new GT_RenderedTexture(TexturesGtBlock.VanillaIcon_OakPlanks)};
	}

	public ITexture[] getBottom(final byte aColor) {
		return new ITexture[]{ new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Acacia_Log)};
	}

	public ITexture[] getTop(final byte aColor) {
		return new ITexture[]{ new GT_RenderedTexture(TexturesGtBlock.VanillaIcon_OakPlanks)};
	}

	public ITexture[] getSides(final byte aColor) {
		return new ITexture[]{ new GT_RenderedTexture(TexturesGtBlock.VanillaIcon_OakPlanks)};
	}

	public ITexture[] getFrontActive(final byte aColor) {
		return this.getFrontNormal();
	}

	public ITexture[] getBackActive(final byte aColor) {
		return this.getBack(aColor);
	}

	public ITexture[] getBottomActive(final byte aColor) {
		return this.getBottom(aColor);
	}

	public ITexture[] getTopActive(final byte aColor) {
		return this.getTop(aColor);
	}

	public ITexture[] getSidesActive(final byte aColor) {
		return this.getSides(aColor);
	}

	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		super.onPostTick(aBaseMetaTileEntity, aTick);
	}

	public ITexture[] getFront(byte aColour) {
		if (aColour == 1) {
			return getFrontActive();
		}
		else if (aColour == 2) {
			return getFrontAlternative();
		}
		return getFrontNormal();
	}

	@Override
	public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
		if (KeyboardUtils.isCtrlKeyDown()) {			
			if (!aPlayer.getDisplayName().equalsIgnoreCase(this.getBaseMetaTileEntity().getOwnerName())) {
				PlayerUtils.messagePlayer(aPlayer, "Container is not yours to lock.");
			}
			else {
				this.mLocked = Utils.invertBoolean(this.mLocked);
				if (this.mLocked) {
					PlayerUtils.messagePlayer(aPlayer, "Container is now locked.");
				}
				else {
					PlayerUtils.messagePlayer(aPlayer, "Container is now unlocked.");
				}
			}			
		}
		else {
			super.onScrewdriverRightClick(aSide, aPlayer, aX, aY, aZ);
		}
	}

	@Override
	public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {	
		super.onPreTick(aBaseMetaTileEntity, aTick);
	}

}
