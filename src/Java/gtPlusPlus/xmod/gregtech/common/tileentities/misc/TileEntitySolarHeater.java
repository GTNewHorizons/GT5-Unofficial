package gtPlusPlus.xmod.gregtech.common.tileentities.misc;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.minecraft.BlockPos;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.GregtechMetaTileEntity_SolarTower;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntitySolarHeater extends GT_MetaTileEntity_TieredMachineBlock {

	public String mSolarTower;
	public boolean mHasTower = false;
	
	private Integer mTX, mTY, mTZ;
	private Byte mRequiredFacing;

	public TileEntitySolarHeater(final int aID, final String aName, final String aNameRegional, final int aTier,
			final String aDescription, final int aSlotCount) {
		super(aID, aName, aNameRegional, aTier, aSlotCount, aDescription);
	}

	public TileEntitySolarHeater(final String aName, final int aTier, final String aDescription,
			final ITexture[][][] aTextures, final int aSlotCount) {
		super(aName, aTier, aSlotCount, aDescription, aTextures);
	}

	@Override
	public String[] getDescription() {
		return new String[] { 
				this.mDescription, 
				"Point me at a Solar Tower",
				CORE.GT_Tooltip };
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
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing,
			final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		return this.mTextures[(aActive ? 5 : 0) + (aSide == aFacing ? 0
				: aSide == GT_Utility.getOppositeSide(aFacing) ? 1 : aSide == 0 ? 2 : aSide == 1 ? 3 : 4)][aColorIndex
				                                                                                           + 1];
	}

	public ITexture[] getFront(final byte aColor) {
		return new ITexture[] { new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Simple_Top),
				new GT_RenderedTexture(Textures.BlockIcons.SOLARPANEL_IV) };
	}

	public ITexture[] getBack(final byte aColor) {
		return new ITexture[] { new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Simple_Top) };
	}

	public ITexture[] getBottom(final byte aColor) {
		return new ITexture[] { new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Simple_Bottom) };
	}

	public ITexture[] getTop(final byte aColor) {
		return new ITexture[] { new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Simple_Top),
				new GT_RenderedTexture(Textures.BlockIcons.SOLARPANEL_LuV) };
	}

	public ITexture[] getSides(final byte aColor) {
		return new ITexture[] { new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Simple_Top) };
	}

	public ITexture[] getFrontActive(final byte aColor) {
		return new ITexture[] { new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Simple_Top),
				new GT_RenderedTexture(Textures.BlockIcons.SOLARPANEL_IV) };
	}

	public ITexture[] getBackActive(final byte aColor) {
		return new ITexture[] { new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Simple_Top) };
	}

	public ITexture[] getBottomActive(final byte aColor) {
		return new ITexture[] { new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Simple_Bottom) };
	}

	public ITexture[] getTopActive(final byte aColor) {
		return new ITexture[] { new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Simple_Top),
				new GT_RenderedTexture(Textures.BlockIcons.SOLARPANEL_LuV) };
	}

	public ITexture[] getSidesActive(final byte aColor) {
		return new ITexture[] { new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Simple_Top) };
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new TileEntitySolarHeater(this.mName, this.mTier, this.mDescription, this.mTextures, 0);
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
	public String[] getInfoData() {
		return new String[] { 
				this.getLocalName(), 
				"Testificate" 
		};
	}

	@Override
	public boolean isGivingInformation() {
		return true;
	}

	@Override
	public boolean isUseableByPlayer(final EntityPlayer p_70300_1_) {
		return false;
	}

	public boolean allowCoverOnSide(final byte aSide, final int aCoverID) {
		return false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(final int p_94128_1_) {
		return new int[] {};
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
	public long maxEUStore() {
		return 0;
	}

	@Override
	public int getCapacity() {
		return 0;
	}

	@Override
	public long maxEUInput() {
		return 0;
	}

	@Override
	public long maxEUOutput() {
		return 0;
	}

	@Override
	public long maxAmperesIn() {
		return 0;
	}

	@Override
	public long maxAmperesOut() {
		return 0;
	}

	@Override
	public boolean isElectric() {
		return false;
	}

	@Override
	public boolean isValidSlot(final int aIndex) {
		return false;
	}

	@Override
	public boolean isFacingValid(final byte aFacing) {
		return aFacing >= 2;
	}

	@Override
	public boolean isEnetInput() {
		return false;
	}

	@Override
	public boolean isEnetOutput() {
		return false;
	}

	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		aNBT.setBoolean("mHasTower", mHasTower);
		if (mHasTower && mTX != null && mTY != null && mTZ != null) {
			aNBT.setInteger("mTX", mTX);
			aNBT.setInteger("mTY", mTY);
			aNBT.setInteger("mTZ", mTZ);
		}
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		this.mHasTower = aNBT.getBoolean("mHasTower");
		if (this.mHasTower) {
			if (aNBT.hasKey("mTX"))
				this.mTX = aNBT.getInteger("mTX");
			if (aNBT.hasKey("mTY"))
				this.mTY = aNBT.getInteger("mTY");
			if (aNBT.hasKey("mTZ"))
				this.mTZ = aNBT.getInteger("mTZ");
		}
	}

	@Override
	public long getInputTier() {
		return 0;
	}

	@Override
	public long getOutputTier() {
		return 0;
	}

	@Override
	public boolean allowCoverOnSide(byte aSide, GT_ItemStack aStack) {
		return false;
	}	

	@Override
	public void onExplosion() {

	}
	
	public GregtechMetaTileEntity_SolarTower getSolarTower() {
		if (this.mHasTower && this.mSolarTower != null && this.mSolarTower.length() > 0) {
		BlockPos p = BlockPos.generateBlockPos(mSolarTower);
		if (p != null) {
			IGregTechTileEntity tTileEntity = getBaseMetaTileEntity().getIGregTechTileEntity(p.xPos, p.yPos, p.zPos);
			if (tTileEntity != null && tTileEntity instanceof GregtechMetaTileEntity_SolarTower) {
				return (GregtechMetaTileEntity_SolarTower) tTileEntity;
			}
		}
		}
		return null;
	}
	

	public boolean setSolarTower(BlockPos aTowerPos) {
		if (!this.mHasTower && setSolarTowerInternal(aTowerPos.getUniqueIdentifier())) {			
			IGregTechTileEntity tTileEntity = getBaseMetaTileEntity().getIGregTechTileEntity(aTowerPos.xPos, aTowerPos.yPos, aTowerPos.zPos);
			if (tTileEntity != null && tTileEntity instanceof GregtechMetaTileEntity_SolarTower) {
				this.mTX = tTileEntity.getXCoord();
				this.mTY = (int) tTileEntity.getYCoord();
				this.mTZ = tTileEntity.getZCoord();
				this.mHasTower = true;
				return true;
			}			
		}
		return false;
	}	
	
	private boolean clearSolarTower() {
			if (mHasTower || mTX != null || mTY != null || mTZ != null || mRequiredFacing != null || this.mSolarTower != null) {
				this.mTX = null;
				this.mTY = null;
				this.mTZ = null;
				this.mRequiredFacing = null;
				this.mSolarTower = null;
				this.mHasTower = false;
				return true;
			}
			return false;			
	}
	
	private final boolean setSolarTowerInternal(String aTowerPos) {
		this.mSolarTower = aTowerPos;
		return this.mSolarTower != null && this.mSolarTower.length() > 0;
	}

	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		super.onPostTick(aBaseMetaTileEntity, aTick);		
			
	}

	@Override
	public void onRemoval() {
		super.onRemoval();
	}

	@Override
	public boolean isOutputFacing(byte aSide) {
		return aSide > 1;
	}

	@Override
	public boolean isInputFacing(byte aSide) {
		return false;
	}

	@Override
	public long getMinimumStoredEU() {
		return 0;
	}

	@Override
	public void onFacingChange() {
		super.onFacingChange();
	}

	@Override
	public void doExplosion(long aExplosionPower) {

	}
}
