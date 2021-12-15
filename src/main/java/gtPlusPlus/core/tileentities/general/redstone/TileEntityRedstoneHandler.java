package gtPlusPlus.core.tileentities.general.redstone;

import cpw.mods.fml.common.registry.GameRegistry;
import gtPlusPlus.api.interfaces.IToolable;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.minecraft.BlockPos;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.EntityUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;

public abstract class TileEntityRedstoneHandler extends TileEntity implements IToolable {

	private final int mTileType;
	private BlockPos mTilePos;
	private boolean mRequiresUpdate = false;
	private Long mStartTime;
	private Byte mRedstoneLevel;
	
	public boolean mLightMode = false;
	public float mLightValue = 0;	
	
	/**
	 * Sets the Redstone Handler Type. 
	 * @param aTileType - A type of the handler designated by an int. 0 = receiver, 1 = emitter, 2 = both, anything else = nothing.
	 */
	public TileEntityRedstoneHandler(int aTileType) {
		mTileType = aTileType;
		registerTileEntity();
	}
	
	private void registerTileEntity() {
		if (!EntityUtils.isTileEntityRegistered(getTileEntityClass(), getTileEntityNameForRegistration())) {
			GameRegistry.registerTileEntity(getTileEntityClass(), getTileEntityNameForRegistration());
		}
	}
	
	protected abstract Class<? extends TileEntity> getTileEntityClass();
	
	protected abstract String getTileEntityNameForRegistration();
	
	public Block getBlock() {
		return mTilePos != null ? mTilePos.getBlockAtPos() : Blocks.redstone_block;
	}

	public final boolean isLight() {
		return mLightMode;
	}	
	
	public final float getLightBrightness() {
		if (!isLight()) {
			return 0;
		}
		else {
			return mLightValue;
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound aNBT) {
		mStartTime = aNBT.getLong("mStartTime");
		mInvName = aNBT.getString("mInvName");
		mLightValue = aNBT.getFloat("mLightValue");
		mLightMode = aNBT.getBoolean("mLightMode");
		mRedstoneLevel = aNBT.getByte("mRedstoneLevel");
		super.readFromNBT(aNBT);
	}

	@Override
	public void writeToNBT(NBTTagCompound aNBT) {
		aNBT.setInteger("mTileType", mTileType);
		aNBT.setLong("mStartTime", mStartTime);
		aNBT.setString("mInvName", mInvName);
		aNBT.setFloat("mLightValue", getLightBrightness());
		aNBT.setBoolean("mLightMode", isLight());
		aNBT.setByte("mRedstoneLevel", mRedstoneLevel);
		super.writeToNBT(aNBT);
	}
	

	private boolean mHasUpdatedRecently = false;
	
	private final boolean init() {
		if (mTilePos == null) {
			try {
				mTilePos = new BlockPos(this);
			} catch (Throwable t) {
				return false;
			}
		}
		if (mStartTime == null) {
			try {
				mStartTime = System.currentTimeMillis();
			} catch (Throwable t) {
				return false;
			}
		}
		return true;
	}
	private Long mLastUpdate;
	private String mInvName = "";
	
	@Override
	public void updateEntity() {		
		//Handle init
		if (!init()) {
			return;
		}		
		if (mRequiresUpdate || mLastUpdate == null) {
			mRequiresUpdate = false;
			mHasUpdatedRecently = true;
			mLastUpdate = System.currentTimeMillis();
			if (mTilePos.world.getBlockLightValue(xCoord, yCoord, zCoord) != getLightBrightness()/0.0625f) {
				mTilePos.getBlockAtPos().setLightLevel(getLightBrightness()/0.0625f);
				mTilePos.world.setLightValue(EnumSkyBlock.Block, xCoord, yCoord, zCoord, (int) (getLightBrightness()/0.0625f));	
				mTilePos.world.updateLightByType(EnumSkyBlock.Block, xCoord, yCoord, zCoord);
				Logger.INFO("Updating Light");
			}	
			mTilePos.world.scheduleBlockUpdate(xCoord, yCoord, zCoord, mTilePos.getBlockAtPos(), 1);		
			markDirty();
		}		
		if (Utils.getMillisSince(mLastUpdate, System.currentTimeMillis()) >= 5000) {
			if (mHasUpdatedRecently) {
				mHasUpdatedRecently = false;
				this.markForUpdate();
			}
		}		
		
		if (Utils.getMillisSince(mStartTime, System.currentTimeMillis()) % 50 == 0) {
			
		}
		
		
		
		super.updateEntity();
	}
	
	public final void markForUpdate() {
		mRequiresUpdate = true;
	}
	
	public final boolean hasUpdatedRecently() {
		return mHasUpdatedRecently;
	}

	@Override
	public int getBlockMetadata() {
		return super.getBlockMetadata();
	}

	@Override
	public void markDirty() {
		super.markDirty();
	}

	@Override
	public boolean canUpdate() {
		return true;
	}
	
	public void setRedstoneState(boolean aRedstoneActive) {
		
	}
	
	public void setCurrentTextureArray(IIcon[] aTextures) {
		
	}
	
    /**
     * Used to see if one of the blocks next to you or your block is getting power from a neighboring block. Used by
     * items like TNT or Doors so they don't have redstone going straight into them.  Args: x, y, z
     */
	public boolean isGettingIndirectlyPowered() {
		if (mTilePos == null) {
			return false;
		}
		return mTilePos.world.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
	}
	
	public int getStrongestIndirectPower() {
		if (mTilePos == null) {
			return 0;
		}
		return mTilePos.world.getStrongestIndirectPower(xCoord, yCoord, zCoord);		
	}
	
    /**
     * Gets the power level from a certain block face.  Args: x, y, z, direction
     */
	public int getIndirectPowerForSide(int aSide) {
		if (mTilePos == null || aSide <0 || aSide > 5) {
			return 0;
		}
		return mTilePos.world.getIndirectPowerLevelTo(xCoord, yCoord, zCoord, aSide);		
	}
	
	/**
     * Returns the highest redstone signal strength powering the given block. Args: X, Y, Z.
     */
	public int getBlockPowerInput() {
		if (mTilePos == null) {
			return 0;
		}
		return mTilePos.world.getBlockPowerInput(xCoord, yCoord, zCoord);		
	}	
	
    /**
     * Determine if this block can make a redstone connection on the side provided,
     * Useful to control which sides are inputs and outputs for redstone wires.
     *
     * Side:
     *  -1: UP
     *   0: NORTH
     *   1: EAST
     *   2: SOUTH
     *   3: WEST
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @param side The side that is trying to make the connection
     * @return True to make the connection
     */
	public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {
		if (mTilePos == null) {
			return false;
		}
		return canAcceptRedstoneSignal() || canSupplyRedstoneSignal();
	}

    /**
     * Called to determine whether to allow the a block to handle its own indirect power rather than using the default rules.
     * @param world The world
     * @param x The x position of this block instance
     * @param y The y position of this block instance
     * @param z The z position of this block instance
     * @param side The INPUT side of the block to be powered - ie the opposite of this block's output side
     * @return Whether Block#isProvidingWeakPower should be called when determining indirect power
     */
	public boolean shouldCheckWeakPower(IBlockAccess world, int x, int y, int z, int side) {
		if (mTilePos == null) {
			return false;
		}
        return getBlock().isNormalCube();
	}

    /**
     * If this block should be notified of weak changes.
     * Weak changes are changes 1 block away through a solid block.
     * Similar to comparators.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y position
     * @param z Z position
     * @param side The side to check
     * @return true To be notified of changes
     */
	public boolean getWeakChanges(IBlockAccess world, int x, int y, int z) {
		if (mTilePos == null) {
			return false;
		}
		return false;
	}
	
	
	/**
	 * Override this to change the level of redstone output.
	 * @return
	 */
	public int getRedstoneLevel() {
		if (mTilePos == null || mRedstoneLevel == null) {
			return 0;
		}
		else {
			if (canSupplyRedstoneSignal()) {				
				if (this.hasUpdatedRecently()) {
					int aInputPower = getInputPowerLevel();
					mRedstoneLevel = (byte) ((aInputPower >= 0 && aInputPower <= 127) ? aInputPower : 0);					
				}
				return mRedstoneLevel;				
			}
		}		
		return 0;
	}
	
	
	public boolean providesWeakPower() {
		return isProvidingPower();
	}
	
	public boolean providesStrongPower() {
		return isProvidingPower();
	}


	/**
	 * Returns the amount of week power this block is providing to a side.
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param side
	 * @return
	 */
	public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side) {		
		if (!providesWeakPower()) {
			return 0;
		}
		return getOutputPowerLevel();
	}
	/**
	 * Returns the amount of strong power this block is providing to a side.
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param side
	 * @return
	 */
	public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side) {
		if (!providesStrongPower()) {
			return 0;
		}
		return getOutputPowerLevel();
	}
	
	
	
	
	
	
	
	/*
	 * Alk's Simplified Redstone Handling functions (Fuck redstone)
	 */
	
	/**
	 * 
	 * @return - Does this Block supply redstone signal at all?
	 */
	public final boolean isPowered() {
		return canAcceptRedstoneSignal() && getInputPowerLevel() > 0;
	}
	
	/**
	 * 
	 * @return - Can this Block provide redstone signal at all?
	 */
	public final boolean isProvidingPower() {
		return canSupplyRedstoneSignal() && getOutputPowerLevel() > 0;
	}
	
	/**
	 * 
	 * @return - (0-15) Redstone Output signal level
	 */
	public final int getOutputPowerLevel() {
		return getRedstoneLevel();
	}
	
	/**
	 * 
	 * @return (0-15) Redstone Input Signal level
	 */
	public final int getInputPowerLevel() {
		return getBlockPowerInput();
	}
	
	/**
	 * 
	 * @return - Does this Tile Entity support outputting redstone?
	 */
	public final boolean canSupplyRedstoneSignal() {
		return mTileType == 1 || mTileType == 2;
	}
	
	/**
	 * 
	 * @return - Does this Tile Entity support inputting redstone?
	 */
	public final boolean canAcceptRedstoneSignal() {
		return mTileType == 0 || mTileType == 2;
	}


	@Override
	public boolean isScrewdriverable() {
		return false;
	}


	@Override
	public boolean onScrewdriverLMB() {
		return false;
	}


	@Override
	public boolean onScrewdriverRMB() {
		return false;
	}


	@Override
	public boolean isWrenchable() {
		return false;
	}


	@Override
	public boolean onWrenchLMB() {
		return false;
	}


	@Override
	public boolean onWrenchRMB() {
		return false;
	}


	@Override
	public boolean isMalletable() {
		return false;
	}


	@Override
	public boolean onMalletLMB() {
		return false;
	}


	@Override
	public boolean onMalletRMB() {
		return false;
	}


	public void setCustomName(String displayName) {
		this.mInvName = displayName;		
	}
	
	public String getCustomName() {
		return this.mInvName;
	}

	public String getInventoryName() {
		return this.hasCustomInventoryName() ? this.mInvName : "container.redstone.generic";
	}

	public boolean hasCustomInventoryName() {
		return (this.mInvName != null) && !this.mInvName.equals("");
	}
	
	
	
	
	
	
}
