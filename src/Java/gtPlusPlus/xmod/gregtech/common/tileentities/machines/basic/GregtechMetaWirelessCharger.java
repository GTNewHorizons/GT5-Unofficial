package gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;

import gtPlusPlus.api.objects.minecraft.BlockPos;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.EntityUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMetaTileEntity;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.helpers.ChargingHelper;

public class GregtechMetaWirelessCharger extends GregtechMetaTileEntity {

	private boolean mHasBeenMapped = false;
	private int mCurrentDimension = 0;
	public int mMode = 0;

	public GregtechMetaWirelessCharger(final int aID, final String aName, final String aNameRegional, final int aTier, final String aDescription, final int aSlotCount) {
		super(aID, aName, aNameRegional, aTier, aSlotCount, aDescription);
	}

	public GregtechMetaWirelessCharger(final String aName, final int aTier, final String aDescription, final ITexture[][][] aTextures, final int aSlotCount) {
		super(aName, aTier, aSlotCount, aDescription, aTextures);
	}

	@Override
	public String[] getDescription() {
		return new String[] {this.mDescription,
				"3 Modes, Long-Range, Local and Mixed.", 
				"Long-Range: Can supply 2A of power to a single player upto "+(GT_Values.V[this.mTier]*4)+"m away.",
				"Local: Can supply several Amps to each player within "+this.mTier*20+"m.",
				"Mixed: Provides both 2A of long range and 1A per player locally.",
				"Mixed mode is more conservative of power and as a result only",
				"Gets half the distances each singular mode gets.",
				CORE.GT_Tooltip};
	}

	public int getTier(){
		return this.mTier;
	}

	public int getMode(){
		return this.mMode;
	}
	
	public int getDimensionID(){
		return this.mCurrentDimension;
	}

	public Map<UUID, EntityPlayer> getLocalMap(){
		return this.mLocalChargingMap;
	}

	public Map<EntityPlayer, UUID> getLongRangeMap(){
		return this.mWirelessChargingMap;
	}

	@Override
	public ITexture[][][] getTextureSet(final ITexture[] aTextures) {
		final ITexture[][][] rTextures = new ITexture[10][17][];
		for (byte i = -1; i < 16; i++) {
			rTextures[0][i + 1] = this.getFront(i);
			rTextures[1][i + 1] = this.getBack(i);
			rTextures[2][i + 1] = this.getBottom(i);
			rTextures[3][i + 1] = this.getTop(i);
			rTextures[4][i + 1] = this.getSides(i);
			rTextures[5][i + 1] = this.getFrontActive(i);
			rTextures[6][i + 1] = this.getBackActive(i);
			rTextures[7][i + 1] = this.getBottomActive(i);
			rTextures[8][i + 1] = this.getTopActive(i);
			rTextures[9][i + 1] = this.getSidesActive(i);
		}
		return rTextures;
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		return this.mTextures[(aActive ? 5 : 0) + (aSide == aFacing ? 0 : aSide == GT_Utility.getOppositeSide(aFacing) ? 1 : aSide == 0 ? 2 : aSide == 1 ? 3 : 4)][aColorIndex + 1];
	}


	public ITexture[] getFront(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Screen_2)};
	}


	public ITexture[] getBack(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Simple_Bottom)};
	}


	public ITexture[] getBottom(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Simple_Bottom)};
	}


	public ITexture[] getTop(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Simple_Bottom)};
	}


	public ITexture[] getSides(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Simple_Bottom)};
	}


	public ITexture[] getFrontActive(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Screen_2)};
	}


	public ITexture[] getBackActive(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Simple_Bottom)};
	}


	public ITexture[] getBottomActive(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Simple_Bottom)};
	}


	public ITexture[] getTopActive(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Simple_Bottom)};
	}


	public ITexture[] getSidesActive(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Simple_Bottom)};
	}

	@Override
	public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
		mWirelessChargingMap.clear();
		mLocalChargingMap.clear();

		if (!this.getBaseMetaTileEntity().getWorld().playerEntities.isEmpty()){
			for (Object mTempPlayer : this.getBaseMetaTileEntity().getWorld().playerEntities){
				if (mTempPlayer instanceof EntityPlayer || mTempPlayer instanceof EntityPlayerMP){
					EntityPlayer mTemp = (EntityPlayer) mTempPlayer;
					ChargingHelper.removeValidPlayer(mTemp, this);
				}
			}
		}

		if (this.mMode >= 2){
			this.mMode = 0;
		}
		else {
			this.mMode++;
		}
		if (this.mMode == 0){
			PlayerUtils.messagePlayer(aPlayer, "Now in Long-Range Charge Mode.");				
		}
		else if (this.mMode == 1){
			PlayerUtils.messagePlayer(aPlayer, "Now in Local Charge Mode.");				
		}
		else {
			PlayerUtils.messagePlayer(aPlayer, "Now in Mixed Charge Mode.");				
		}
		super.onScrewdriverRightClick(aSide, aPlayer, aX, aY, aZ);
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaWirelessCharger(this.mName, this.mTier, this.mDescription, this.mTextures, this.mInventory.length);
	}

	@Override public boolean isSimpleMachine()						{return false;}
	@Override public boolean isElectric()							{return true;}
	@Override public boolean isValidSlot(final int aIndex)				{return true;}
	@Override public boolean isFacingValid(final byte aFacing)			{return true;}
	@Override public boolean isEnetInput() 							{return true;}
	@Override public boolean isEnetOutput() 						{return false;}
	@Override public boolean isInputFacing(final byte aSide)				{return aSide!=this.getBaseMetaTileEntity().getFrontFacing();}
	@Override public boolean isOutputFacing(final byte aSide)				{return aSide==this.getBaseMetaTileEntity().getFrontFacing();}
	@Override public boolean isTeleporterCompatible()				{return false;}
	@Override public long getMinimumStoredEU()						{return 0;}
	@Override public long maxEUStore()								{return GT_Values.V[this.mTier]*128;}

	@Override
	public int getCapacity() {
		return (int) (GT_Values.V[this.mTier]*32);
	}

	@Override
	public long maxEUInput() {
		return GT_Values.V[this.mTier];
	}

	@Override
	public long maxEUOutput() {
		return 0;
	}

	@Override
	public long maxAmperesIn() {
		if (this.mMode == 0){
			return 2;
		}
		else if (this.mMode == 1){
			return this.mLocalChargingMap.size()*8;
		}
		else {
			return ((this.mLocalChargingMap.size()*4)+this.mWirelessChargingMap.size());
		}
	}

	@Override
	public long maxAmperesOut() {
		return 0;
	}
	@Override public int rechargerSlotStartIndex()					{return 0;}
	@Override public int dechargerSlotStartIndex()					{return 0;}
	@Override public int rechargerSlotCount()						{return 0;}
	@Override public int dechargerSlotCount()						{return 0;}
	@Override public int getProgresstime()							{return (int)this.getBaseMetaTileEntity().getUniversalEnergyStored();}
	@Override public int maxProgresstime()							{return (int)this.getBaseMetaTileEntity().getUniversalEnergyCapacity();}
	@Override public boolean isAccessAllowed(final EntityPlayer aPlayer)	{return true;}

	@Override
	public boolean onRightclick(final IGregTechTileEntity aBaseMetaTileEntity, final EntityPlayer aPlayer) {
		if (aBaseMetaTileEntity.isClientSide())
		{
			return true;
		}
		return true;
	}

	@Override
	public boolean allowPullStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex, final byte aSide, final ItemStack aStack) {
		return false;
	}

	@Override
	public boolean allowPutStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex, final byte aSide, final ItemStack aStack) {
		return false;
	}

	@Override
	public String[] getInfoData() {
		return new String[] {
				this.getLocalName()};
	}

	@Override
	public boolean isGivingInformation() {
		return true;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(final int p_94128_1_) {
		return new int[] {};
	}

	@Override
	public boolean canInsertItem(final int p_102007_1_, final ItemStack p_102007_2_, final int p_102007_3_) {
		return false;
	}

	@Override
	public boolean canExtractItem(final int p_102008_1_, final ItemStack p_102008_2_, final int p_102008_3_) {
		return false;
	}

	@Override
	public int getSizeInventory() {
		return 0;
	}

	@Override
	public ItemStack getStackInSlot(final int p_70301_1_) {
		return null;
	}

	@Override
	public ItemStack decrStackSize(final int p_70298_1_, final int p_70298_2_) {
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(final int p_70304_1_) {
		return null;
	}

	@Override
	public void setInventorySlotContents(final int p_70299_1_, final ItemStack p_70299_2_) {
	}

	@Override
	public String getInventoryName() {
		return null;
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 0;
	}

	@Override
	public boolean isUseableByPlayer(final EntityPlayer p_70300_1_) {
		return false;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	@Override
	public boolean isItemValidForSlot(final int p_94041_1_, final ItemStack p_94041_2_) {
		return false;
	}

	@Override
	public boolean isOverclockerUpgradable() {
		return false;
	}

	@Override
	public boolean isTransformerUpgradable() {
		return false;
	}

	@Override
	public void saveNBTData(final NBTTagCompound aNBT) {
		aNBT.setInteger("mMode", this.mMode);
		aNBT.setInteger("mCurrentDimension", this.mCurrentDimension);
	}

	@Override
	public void loadNBTData(final NBTTagCompound aNBT) {
		this.mMode = aNBT.getInteger("mMode");
		this.mCurrentDimension = aNBT.getInteger("mCurrentDimension");
	}

	@Override
	public void onFirstTick(final IGregTechTileEntity aBaseMetaTileEntity) {
		if (this.getBaseMetaTileEntity().isServerSide()) {

		}
	}


	private Map<EntityPlayer, UUID> mWirelessChargingMap = new HashMap<EntityPlayer, UUID>();
	private Map<UUID, EntityPlayer> mLocalChargingMap = new HashMap<UUID, EntityPlayer>();

	@Override
	public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
		super.onPostTick(aBaseMetaTileEntity, aTick);
		if (this.getBaseMetaTileEntity().isServerSide()) {

			if (this.mCurrentDimension != aBaseMetaTileEntity.getWorld().provider.dimensionId){
				this.mCurrentDimension = aBaseMetaTileEntity.getWorld().provider.dimensionId;
			}

			if (!mHasBeenMapped && ChargingHelper.addEntry(getTileEntityPosition(), this)){
				mHasBeenMapped = true;
			}

			if (aTick % 20 == 0 && mHasBeenMapped){
				if (!aBaseMetaTileEntity.getWorld().playerEntities.isEmpty()){
					for (Object mTempPlayer : aBaseMetaTileEntity.getWorld().playerEntities){
						if (mTempPlayer instanceof EntityPlayer || mTempPlayer instanceof EntityPlayerMP){
							EntityPlayer mTemp = (EntityPlayer) mTempPlayer;

							if (this.mMode == 1 || this.mMode == 2){
								int tempRange = (this.mMode == 1 ? this.mTier*20 : this.mTier*10);
								if (getDistanceBetweenTwoPositions(getTileEntityPosition(), getPositionOfEntity(mTemp)) < tempRange){
									if (!mLocalChargingMap.containsKey(mTemp.getPersistentID())){
										mLocalChargingMap.put(mTemp.getPersistentID(), mTemp);
										ChargingHelper.addValidPlayer(mTemp, this);
										PlayerUtils.messagePlayer(mTemp, "You have entered charging range. ["+tempRange+"m - Local].");
									}
								}
								else {
									if (mLocalChargingMap.containsKey(mTemp.getPersistentID())){
										if (mLocalChargingMap.remove(mTemp.getPersistentID()) != null){
											PlayerUtils.messagePlayer(mTemp, "You have left charging range. ["+tempRange+"m - Local].");
											ChargingHelper.removeValidPlayer(mTemp, this);	
										}
									}
								}
							}
							if (this.mMode == 0 || this.mMode == 2){
								int tempRange = (int) (this.mMode == 0 ? 4*GT_Values.V[this.mTier] : 2*GT_Values.V[this.mTier]);
								if (getDistanceBetweenTwoPositions(getTileEntityPosition(), getPositionOfEntity(mTemp)) <= tempRange){
									if (!mWirelessChargingMap.containsKey(mTemp)){
										if (mTemp.getDisplayName().equalsIgnoreCase(this.getBaseMetaTileEntity().getOwnerName())) {
											mWirelessChargingMap.put(mTemp, mTemp.getPersistentID());
											ChargingHelper.addValidPlayer(mTemp, this);
											PlayerUtils.messagePlayer(mTemp, "You have entered charging range. ["+tempRange+"m - Long-Range].");											
										}
									}
								}
								else {
									if (mWirelessChargingMap.containsKey(mTemp)){
										if (mWirelessChargingMap.remove(mTemp) != null){
											PlayerUtils.messagePlayer(mTemp, "You have left charging range. ["+tempRange+"m - Long Range].");
											ChargingHelper.removeValidPlayer(mTemp, this);	
										}
									}
								}
								if (mWirelessChargingMap.containsKey(mTemp) && !mTemp.getDisplayName().equalsIgnoreCase(this.getBaseMetaTileEntity().getOwnerName())){
									if (mWirelessChargingMap.remove(mTemp) != null){
										ChargingHelper.removeValidPlayer(mTemp, this);	
									}
								}
							}
							/*if (this.mMode == 0 || this.mMode == 2){
								int tempRange = (int) (this.mMode == 0 ? 4*GT_Values.V[this.mTier] : 2*GT_Values.V[this.mTier]);
								if (getDistanceBetweenTwoPositions(getTileEntityPosition(), getPositionOfEntity(mTemp)) < tempRange){
									if (!mWirelessChargingMap.containsKey(mTemp)){
										mWirelessChargingMap.put(mTemp, mTemp.getPersistentID());
										PlayerUtils.messagePlayer(mTemp, "You have entered charging range. ["+tempRange+"m].");
										ChargingHelper.addValidPlayer(mTemp, this);
									}
								}
								else {
									if (mWirelessChargingMap.containsKey(mTemp)){
										if (mWirelessChargingMap.remove(mTemp) != null){
											PlayerUtils.messagePlayer(mTemp, "You have left charging range. ["+tempRange+"m].");
											ChargingHelper.removeValidPlayer(mTemp, this);		
										}
									}
								}
							} */

						}
					}
				}
			}


		}		
	}

	public BlockPos getTileEntityPosition(){
		return new BlockPos(this.getBaseMetaTileEntity().getXCoord(), this.getBaseMetaTileEntity().getYCoord(), this.getBaseMetaTileEntity().getZCoord());
	}

	public BlockPos getPositionOfEntity(Entity mEntity){
		if (mEntity == null){
			return null;
		}
		return EntityUtils.findBlockPosUnderEntity(mEntity);
	}

	public double getDistanceBetweenTwoPositions(BlockPos objectA, BlockPos objectB){
		if (objectA == null || objectB == null){
			return 0f;
		}
		int[] objectArray1 = new int[]{objectA.xPos, objectA.yPos, objectA.zPos};
		int[] objectArray2 = new int[]{objectB.xPos, objectB.yPos, objectB.zPos};
		
		final double distance = Math.sqrt(
				(objectArray2[0]-objectArray1[0])*(objectArray2[0]-objectArray1[0])
				+(objectArray2[1]-objectArray1[1])*(objectArray2[1]-objectArray1[1])
				+(objectArray2[2]-objectArray1[2])*(objectArray2[2]-objectArray1[2]));
		return distance;
	}

	@Override
	public void onRemoval() {

		ChargingHelper.removeEntry(getTileEntityPosition(), this);

		mWirelessChargingMap.clear();
		mLocalChargingMap.clear();
		if (!this.getBaseMetaTileEntity().getWorld().playerEntities.isEmpty()){
			for (Object mTempPlayer : this.getBaseMetaTileEntity().getWorld().playerEntities){
				if (mTempPlayer instanceof EntityPlayer || mTempPlayer instanceof EntityPlayerMP){
					EntityPlayer mTemp = (EntityPlayer) mTempPlayer;
					ChargingHelper.removeValidPlayer(mTemp, this);
				}
			}
		}


		super.onRemoval();
	}

	@Override
	public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity,
			EntityPlayer aPlayer, byte aSide, float aX, float aY, float aZ) {

		int tempRange;

		if (this.mMode == 0 || this.mMode == 2){
			tempRange = (int) (this.mMode == 0 ? 4*GT_Values.V[this.mTier] : 2*GT_Values.V[this.mTier]);			
		}
		else {
			tempRange = this.mMode == 1 ? this.mTier*20 : this.mTier*10;			
		}

		if (this.mMode == 2){
			PlayerUtils.messagePlayer(aPlayer, "Mixed Mode | Local: "+this.mTier*10+"m | Long: "+tempRange+"m");	
		}
		else if (this.mMode == 1){
			PlayerUtils.messagePlayer(aPlayer, "Local Mode: "+this.mTier*20+"m");	

		}
		else {
			PlayerUtils.messagePlayer(aPlayer, "Long-range Mode: "+tempRange+"m");	

		}

		return super.onRightclick(aBaseMetaTileEntity, aPlayer, aSide, aX, aY, aZ);
	}

	@Override
	public void onServerStart() {
		mWirelessChargingMap.clear();
		mLocalChargingMap.clear();
		if (!mHasBeenMapped && ChargingHelper.addEntry(getTileEntityPosition(), this)){
			mHasBeenMapped = true;
		}
		super.onServerStart();
	}

	@Override
	public void onExplosion() {
		ChargingHelper.removeEntry(getTileEntityPosition(), this);
		super.onExplosion();
	}

	@Override
	public void doExplosion(long aExplosionPower) {
		ChargingHelper.removeEntry(getTileEntityPosition(), this);
		super.doExplosion(aExplosionPower);
	}

}