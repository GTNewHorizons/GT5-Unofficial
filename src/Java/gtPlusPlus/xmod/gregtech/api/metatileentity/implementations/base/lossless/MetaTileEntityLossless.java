package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.lossless;

import static gregtech.api.enums.GT_Values.GT;
import static gregtech.api.enums.GT_Values.V;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.util.*;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * Extend this Class to add a new MetaMachine
 * Call the Constructor with the desired ID at the load-phase (not preload and also not postload!)
 * Implement the newMetaEntity-Method to return a new ready instance of your MetaTileEntity
 * <p/>
 * Call the Constructor like the following example inside the Load Phase, to register it.
 * "new GT_MetaTileEntity_E_Furnace(54, "GT_E_Furnace", "Automatic E-Furnace");"
 */
public abstract class MetaTileEntityLossless implements IMetaTileEntity {
	/**
	 * Only assigned for the MetaTileEntity in the List! Also only used to get the localized Name for the ItemStack and for getInvName.
	 */
	public final String mName;
	/**
	 * The Inventory of the MetaTileEntity. Amount of Slots can be larger than 256. HAYO!
	 */
	public final ItemStack[] mInventory;
	public boolean doTickProfilingInThisTick = true;
	/**
	 * accessibility to this Field is no longer given, see below
	 */
	private IGregTechTileEntity mBaseMetaTileEntity;

	/**
	 * This registers your Machine at the List.
	 * Use only ID's larger than 2048, because i reserved these ones.
	 * See also the List in the API, as it has a Description containing all the reservations.
	 *
	 * @param aID the ID
	 * @example for Constructor overload.
	 * <p/>
	 * public GT_MetaTileEntity_EBench(int aID, String mName, String mNameRegional) {
	 * super(aID, mName, mNameRegional);
	 * }
	 */
	public MetaTileEntityLossless(final int aID, final String aBasicName, final String aRegionalName, final int aInvSlotCount) {
		if (GregTech_API.sPostloadStarted || !GregTech_API.sPreloadStarted) {
			throw new IllegalAccessError("This Constructor has to be called in the load Phase");
		}
		if (GregTech_API.METATILEENTITIES[aID] == null) {
			GregTech_API.METATILEENTITIES[aID] = this;
		} else {
			throw new IllegalArgumentException("MetaMachine-Slot Nr. " + aID + " is already occupied!");
		}
		this.mName = aBasicName.replaceAll(" ", "_").toLowerCase();
		this.setBaseMetaTileEntity(GregTech_API.constructBaseMetaTileEntity());
		this.getBaseMetaTileEntity().setMetaTileID((short) aID);
		GT_LanguageManager.addStringLocalization("gt.blockmachines." + this.mName + ".name", aRegionalName);
		this.mInventory = new ItemStack[aInvSlotCount];

		if (GT.isClientSide()) {
			final ItemStack tStack = new ItemStack(GregTech_API.sBlockMachines, 1, aID);
			tStack.getItem().addInformation(tStack, null, new ArrayList<String>(), true);
		}
	}

	/**
	 * This is the normal Constructor.
	 */
	public MetaTileEntityLossless(final String aName, final int aInvSlotCount) {
		this.mInventory = new ItemStack[aInvSlotCount];
		this.mName = aName;
	}

	@Override
	public IGregTechTileEntity getBaseMetaTileEntity() {
		return this.mBaseMetaTileEntity;
	}

	@Override
	public void setBaseMetaTileEntity(final IGregTechTileEntity aBaseMetaTileEntity) {
		if ((this.mBaseMetaTileEntity != null) && (aBaseMetaTileEntity == null)) {
			this.mBaseMetaTileEntity.getMetaTileEntity().inValidate();
			this.mBaseMetaTileEntity.setMetaTileEntity(null);
		}
		this.mBaseMetaTileEntity = aBaseMetaTileEntity;
		if (this.mBaseMetaTileEntity != null) {
			this.mBaseMetaTileEntity.setMetaTileEntity(this);
		}
	}

	@Override
	public ItemStack getStackForm(final long aAmount) {
		return new ItemStack(GregTech_API.sBlockMachines, (int) aAmount, this.getBaseMetaTileEntity().getMetaTileID());
	}

	public String getLocalName() {
		return GT_LanguageManager.getTranslation("gt.blockmachines." + this.mName + ".name");
	}

	@Override
	public void onServerStart() {/*Do nothing*/}

	@Override
	public void onWorldSave(final File aSaveDirectory) {/*Do nothing*/}

	@Override
	public void onWorldLoad(final File aSaveDirectory) {/*Do nothing*/}

	@Override
	public void onConfigLoad(final GT_Config aConfig) {/*Do nothing*/}

	@Override
	public void setItemNBT(final NBTTagCompound aNBT) {/*Do nothing*/}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(final IIconRegister aBlockIconRegister) {/*Do nothing*/}

	@Override
	public boolean allowCoverOnSide(final byte aSide, final GT_ItemStack aStack) {
		return true;
	}

	@Override
	public void onScrewdriverRightClick(final byte aSide, final EntityPlayer aPlayer, final float aX, final float aY, final float aZ) {/*Do nothing*/}

	@Override
	public boolean onWrenchRightClick(final byte aSide, final byte aWrenchingSide, final EntityPlayer aPlayer, final float aX, final float aY, final float aZ) {
		if (this.getBaseMetaTileEntity().isValidFacing(aWrenchingSide)) {
			this.getBaseMetaTileEntity().setFrontFacing(aWrenchingSide);
			return true;
		}
		return false;
	}

	@Override
	public void onExplosion() {/*Do nothing*/}

	@Override
	public void onFirstTick(final IGregTechTileEntity aBaseMetaTileEntity) {/*Do nothing*/}

	@Override
	public void onPreTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {/*Do nothing*/}

	@Override
	public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {/*Do nothing*/}

	@Override
	public void inValidate() {/*Do nothing*/}

	@Override
	public void onRemoval() {/*Do nothing*/}

	@Override
	public void initDefaultModes(final NBTTagCompound aNBT) {/*Do nothing*/}

	/**
	 * When a GUI is opened
	 */
	public void onOpenGUI() {/*Do nothing*/}

	/**
	 * When a GUI is closed
	 */
	public void onCloseGUI() {/*Do nothing*/}

	/**
	 * a Player rightclicks the Machine
	 * Sneaky rightclicks are not getting passed to this!
	 */
	public boolean onRightclick(final IGregTechTileEntity aBaseMetaTileEntity, final EntityPlayer aPlayer) {
		return false;
	}

	@Override
	public boolean onRightclick(final IGregTechTileEntity aBaseMetaTileEntity, final EntityPlayer aPlayer, final byte aSide, final float aX, final float aY, final float aZ) {
		return this.onRightclick(aBaseMetaTileEntity, aPlayer);
	}

	@Override
	public void onLeftclick(final IGregTechTileEntity aBaseMetaTileEntity, final EntityPlayer aPlayer) {/*Do nothing*/}

	@Override
	public void onValueUpdate(final byte aValue) {/*Do nothing*/}

	@Override
	public byte getUpdateData() {
		return 0;
	}

	@Override
	public void doSound(final byte aIndex, final double aX, final double aY, final double aZ) {/*Do nothing*/}

	@Override
	public void startSoundLoop(final byte aIndex, final double aX, final double aY, final double aZ) {/*Do nothing*/}

	@Override
	public void stopSoundLoop(final byte aValue, final double aX, final double aY, final double aZ) {/*Do nothing*/}

	@Override
	public final void sendSound(final byte aIndex) {
		if (!this.getBaseMetaTileEntity().hasMufflerUpgrade()) {
			this.getBaseMetaTileEntity().sendBlockEvent((byte) 4, aIndex);
		}
	}

	@Override
	public final void sendLoopStart(final byte aIndex) {
		if (!this.getBaseMetaTileEntity().hasMufflerUpgrade()) {
			this.getBaseMetaTileEntity().sendBlockEvent((byte) 5, aIndex);
		}
	}

	@Override
	public final void sendLoopEnd(final byte aIndex) {
		if (!this.getBaseMetaTileEntity().hasMufflerUpgrade()) {
			this.getBaseMetaTileEntity().sendBlockEvent((byte) 6, aIndex);
		}
	}

	/**
	 * @return true if this Device emits Energy at all
	 */
	public boolean isElectric() {
		return true;
	}

	/**
	 * @return true if this Device emits Energy at all
	 */
	public boolean isPneumatic() {
		return false;
	}

	/**
	 * @return true if this Device emits Energy at all
	 */
	public boolean isSteampowered() {
		return false;
	}

	/**
	 * @return true if this Device emits Energy at all
	 */
	public boolean isEnetOutput() {
		return false;
	}

	/**
	 * @return true if this Device consumes Energy at all
	 */
	public boolean isEnetInput() {
		return false;
	}

	/**
	 * @return the amount of EU, which can be stored in this Device. Default is 0 EU.
	 */
	public long maxEUStore() {
		return 0;
	}

	/**
	 * @return the amount of EU/t, which can be accepted by this Device before it explodes.
	 */
	public long maxEUInput() {
		return 0;
	}

	/**
	 * @return the amount of EU/t, which can be outputted by this Device.
	 */
	public long maxEUOutput() {
		return 0;
	}

	/**
	 * @return the amount of E-net Impulses of the maxEUOutput size, which can be outputted by this Device.
	 * Default is 1 Pulse, this shouldn't be set to smaller Values than 1, as it won't output anything in that Case!
	 */
	public long maxAmperesOut() {
		return 1;
	}

	/**
	 * How many Amperes this Block can suck at max. Surpassing this value won't blow it up.
	 */
	public long maxAmperesIn() {
		return 1;
	}

	/**
	 * @return true if that Side is an Output.
	 */
	public boolean isOutputFacing(final byte aSide) {
		return false;
	}

	/**
	 * @return true if that Side is an Input.
	 */
	public boolean isInputFacing(final byte aSide) {
		return false;
	}

	/**
	 * @return true if Transformer Upgrades increase Packet Amount.
	 */
	public boolean isTransformingLowEnergy() {
		return true;
	}

	@Override
	public boolean isFacingValid(final byte aFacing) {
		return false;
	}

	@Override
	public boolean isAccessAllowed(final EntityPlayer aPlayer) {
		return false;
	}

	@Override
	public boolean isValidSlot(final int aIndex) {
		return true;
	}

	@Override
	public boolean setStackToZeroInsteadOfNull(final int aIndex) {
		return false;
	}

	/**
	 * This is used to get the internal Energy. I use this for the IDSU.
	 */
	public long getEUVar() {
		return ((GregtechBaseMetaTileEntityLossless) this.mBaseMetaTileEntity).mStoredEnergy;
	}

	/**
	 * This is used to set the internal Energy to the given Parameter. I use this for the IDSU.
	 */
	public void setEUVar(final long aEnergy) {
		((GregtechBaseMetaTileEntityLossless) this.mBaseMetaTileEntity).mStoredEnergy = aEnergy;
	}

	/**
	 * This is used to get the internal Steam Energy.
	 */
	public long getSteamVar() {
		return ((GregtechBaseMetaTileEntityLossless) this.mBaseMetaTileEntity).mStoredSteam;
	}

	/**
	 * This is used to set the internal Steam Energy to the given Parameter.
	 */
	public void setSteamVar(final long aSteam) {
		((GregtechBaseMetaTileEntityLossless) this.mBaseMetaTileEntity).mStoredSteam = aSteam;
	}

	/**
	 * @return the amount of Steam, which can be stored in this Device. Default is 0 EU.
	 */
	public long maxSteamStore() {
		return 0;
	}

	/**
	 * @return the amount of EU, which this Device stores before starting to emit Energy.
	 * useful if you don't want to emit stored Energy until a certain Level is reached.
	 */
	public long getMinimumStoredEU() {
		return 512;
	}

	/**
	 * Determines the Tier of the Machine, used for de-charging Tools.
	 */
	public long getInputTier() {
		return GT_Utility.getTier(this.getBaseMetaTileEntity().getInputVoltage());
	}

	/**
	 * Determines the Tier of the Machine, used for charging Tools.
	 */
	public long getOutputTier() {
		return GT_Utility.getTier(this.getBaseMetaTileEntity().getOutputVoltage());
	}

	/**
	 * gets the first RechargerSlot
	 */
	public int rechargerSlotStartIndex() {
		return 0;
	}

	/**
	 * gets the amount of RechargerSlots
	 */
	public int rechargerSlotCount() {
		return 0;
	}

	/**
	 * gets the first DechargerSlot
	 */
	public int dechargerSlotStartIndex() {
		return 0;
	}

	/**
	 * gets the amount of DechargerSlots
	 */
	public int dechargerSlotCount() {
		return 0;
	}

	/**
	 * gets if this is protected from other Players per default or not
	 */
	public boolean ownerControl() {
		return false;
	}

	@Override
	public ArrayList<String> getSpecialDebugInfo(final IGregTechTileEntity aBaseMetaTileEntity, final EntityPlayer aPlayer, final int aLogLevel, final ArrayList<String> aList) {
		return aList;
	}

	@Override
	public boolean isLiquidInput(final byte aSide) {
		return true;
	}

	@Override
	public boolean isLiquidOutput(final byte aSide) {
		return true;
	}

	/**
	 * gets the contained Liquid
	 */
	@Override
	public FluidStack getFluid() {
		return null;
	}

	/**
	 * tries to fill this Tank
	 */
	@Override
	public int fill(final FluidStack resource, final boolean doFill) {
		return 0;
	}

	/**
	 * tries to empty this Tank
	 */
	@Override
	public FluidStack drain(final int maxDrain, final boolean doDrain) {
		return null;
	}

	/**
	 * Tank pressure
	 */
	public int getTankPressure() {
		return 0;
	}

	/**
	 * Liquid Capacity
	 */
	@Override
	public int getCapacity() {
		return 0;
	}

	@Override
	public void onMachineBlockUpdate() {/*Do nothing*/}

	@Override
	public void receiveClientEvent(final byte aEventID, final byte aValue) {/*Do nothing*/}

	@Override
	public boolean isSimpleMachine() {
		return false;
	}

	/**
	 * If this accepts up to 4 Overclockers
	 */
	public boolean isOverclockerUpgradable() {
		return false;
	}

	/**
	 * If this accepts Transformer Upgrades
	 */
	public boolean isTransformerUpgradable() {
		return false;
	}

	/**
	 * Progress this machine has already made
	 */
	public int getProgresstime() {
		return 0;
	}

	/**
	 * Progress this Machine has to do to produce something
	 */
	public int maxProgresstime() {
		return 0;
	}

	/**
	 * Increases the Progress, returns the overflown Progress.
	 */
	public int increaseProgress(final int aProgress) {
		return 0;
	}

	/**
	 * If this TileEntity makes use of Sided Redstone behaviors.
	 * Determines only, if the Output Redstone Array is getting filled with 0 for true, or 15 for false.
	 */
	public boolean hasSidedRedstoneOutputBehavior() {
		return false;
	}

	/**
	 * When the Facing gets changed.
	 */
	public void onFacingChange() {/*Do nothing*/}

	/**
	 * if the IC2 Teleporter can drain from this.
	 */
	public boolean isTeleporterCompatible() {
		return this.isEnetOutput() && (this.getBaseMetaTileEntity().getOutputVoltage() >= 128) && (this.getBaseMetaTileEntity().getUniversalEnergyCapacity() >= 500000);
	}

	/**
	 * Gets the Output for the comparator on the given Side
	 */
	@Override
	public byte getComparatorValue(final byte aSide) {
		return 0;
	}

	@Override
	public boolean acceptsRotationalEnergy(final byte aSide) {
		return false;
	}

	@Override
	public boolean injectRotationalEnergy(final byte aSide, final long aSpeed, final long aEnergy) {
		return false;
	}

	@Override
	public String getSpecialVoltageToolTip() {
		return null;
	}

	@Override
	public boolean isGivingInformation() {
		return false;
	}

	@Override
	public String[] getInfoData() {
		return new String[]{};
	}

	public boolean isDigitalChest() {
		return false;
	}

	public ItemStack[] getStoredItemData() {
		return null;
	}

	public void setItemCount(final int aCount) {/*Do nothing*/}

	public int getMaxItemCount() {
		return 0;
	}

	@Override
	public int getSizeInventory() {
		return this.mInventory.length;
	}

	@Override
	public ItemStack getStackInSlot(final int aIndex) {
		if ((aIndex >= 0) && (aIndex < this.mInventory.length)) {
			return this.mInventory[aIndex];
		}
		return null;
	}

	@Override
	public void setInventorySlotContents(final int aIndex, final ItemStack aStack) {
		if ((aIndex >= 0) && (aIndex < this.mInventory.length)) {
			this.mInventory[aIndex] = aStack;
		}
	}

	@Override
	public String getInventoryName() {
		if (GregTech_API.METATILEENTITIES[this.getBaseMetaTileEntity().getMetaTileID()] != null) {
			return GregTech_API.METATILEENTITIES[this.getBaseMetaTileEntity().getMetaTileID()].getMetaName();
		}
		return "";
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isItemValidForSlot(final int aIndex, final ItemStack aStack) {
		return this.getBaseMetaTileEntity().isValidSlot(aIndex);
	}

	@Override
	public ItemStack decrStackSize(final int aIndex, final int aAmount) {
		final ItemStack tStack = this.getStackInSlot(aIndex);
		ItemStack rStack = GT_Utility.copy(tStack);
		if (tStack != null) {
			if (tStack.stackSize <= aAmount) {
				if (this.setStackToZeroInsteadOfNull(aIndex)) {
					tStack.stackSize = 0;
				} else {
					this.setInventorySlotContents(aIndex, null);
				}
			} else {
				rStack = tStack.splitStack(aAmount);
				if ((tStack.stackSize == 0) && !this.setStackToZeroInsteadOfNull(aIndex)) {
					this.setInventorySlotContents(aIndex, null);
				}
			}
		}
		return rStack;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(final int aSide) {
		final ArrayList<Integer> tList = new ArrayList<>();
		final IGregTechTileEntity tTileEntity = this.getBaseMetaTileEntity();
		final boolean tSkip = tTileEntity.getCoverBehaviorAtSide((byte) aSide).letsItemsIn((byte) aSide, tTileEntity.getCoverIDAtSide((byte) aSide), tTileEntity.getCoverDataAtSide((byte) aSide), -2, tTileEntity) || tTileEntity.getCoverBehaviorAtSide((byte) aSide).letsItemsOut((byte) aSide, tTileEntity.getCoverIDAtSide((byte) aSide), tTileEntity.getCoverDataAtSide((byte) aSide), -2, tTileEntity);
		for (int i = 0; i < this.getSizeInventory(); i++) {
			if (this.isValidSlot(i) && (tSkip || tTileEntity.getCoverBehaviorAtSide((byte) aSide).letsItemsOut((byte) aSide, tTileEntity.getCoverIDAtSide((byte) aSide), tTileEntity.getCoverDataAtSide((byte) aSide), i, tTileEntity) || tTileEntity.getCoverBehaviorAtSide((byte) aSide).letsItemsIn((byte) aSide, tTileEntity.getCoverIDAtSide((byte) aSide), tTileEntity.getCoverDataAtSide((byte) aSide), i, tTileEntity))) {
				tList.add(i);
			}
		}
		final int[] rArray = new int[tList.size()];
		for (int i = 0; i < rArray.length; i++) {
			rArray[i] = tList.get(i);
		}
		return rArray;
	}

	@Override
	public boolean canInsertItem(final int aIndex, final ItemStack aStack, final int aSide) {
		return this.isValidSlot(aIndex) && (aStack != null) && (aIndex < this.mInventory.length) && ((this.mInventory[aIndex] == null) || GT_Utility.areStacksEqual(aStack, this.mInventory[aIndex])) && this.allowPutStack(this.getBaseMetaTileEntity(), aIndex, (byte) aSide, aStack);
	}

	@Override
	public boolean canExtractItem(final int aIndex, final ItemStack aStack, final int aSide) {
		return this.isValidSlot(aIndex) && (aStack != null) && (aIndex < this.mInventory.length) && this.allowPullStack(this.getBaseMetaTileEntity(), aIndex, (byte) aSide, aStack);
	}

	@Override
	public boolean canFill(final ForgeDirection aSide, final Fluid aFluid) {
		return this.fill(aSide, new FluidStack(aFluid, 1), false) == 1;
	}

	@Override
	public boolean canDrain(final ForgeDirection aSide, final Fluid aFluid) {
		return this.drain(aSide, new FluidStack(aFluid, 1), false) != null;
	}

	@Override
	public FluidTankInfo[] getTankInfo(final ForgeDirection aSide) {
		if ((this.getCapacity() <= 0) && !this.getBaseMetaTileEntity().hasSteamEngineUpgrade()) {
			return new FluidTankInfo[]{};
		}
		return new FluidTankInfo[]{this.getInfo()};
	}

	public int fill_default(final ForgeDirection aSide, final FluidStack aFluid, final boolean doFill) {
		return this.fill(aFluid, doFill);
	}

	@Override
	public int fill(final ForgeDirection aSide, final FluidStack aFluid, final boolean doFill) {
		if (this.getBaseMetaTileEntity().hasSteamEngineUpgrade() && GT_ModHandler.isSteam(aFluid) && (aFluid.amount > 1)) {
			final int tSteam = (int) Math.min(Integer.MAX_VALUE, Math.min(aFluid.amount / 2, this.getBaseMetaTileEntity().getSteamCapacity() - this.getBaseMetaTileEntity().getStoredSteam()));
			if (tSteam > 0) {
				if (doFill) {
					this.getBaseMetaTileEntity().increaseStoredSteam(tSteam, true);
				}
				return tSteam * 2;
			}
		} else {
			return this.fill_default(aSide, aFluid, doFill);
		}
		return 0;
	}

	@Override
	public FluidStack drain(final ForgeDirection aSide, final FluidStack aFluid, final boolean doDrain) {
		if ((this.getFluid() != null) && (aFluid != null) && this.getFluid().isFluidEqual(aFluid)) {
			return this.drain(aFluid.amount, doDrain);
		}
		return null;
	}

	@Override
	public FluidStack drain(final ForgeDirection aSide, final int maxDrain, final boolean doDrain) {
		return this.drain(maxDrain, doDrain);
	}

	@Override
	public int getFluidAmount() {
		return 0;
	}

	@Override
	public FluidTankInfo getInfo() {
		return new FluidTankInfo(this);
	}

	@Override
	public String getMetaName() {
		return this.mName;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(final int i) {
		return null;
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public boolean doTickProfilingMessageDuringThisTick() {
		return this.doTickProfilingInThisTick;
	}

	@Override
	public void markDirty() {
		//
	}

	@Override
	public boolean isUseableByPlayer(final EntityPlayer entityplayer) {
		return false;
	}

	@Override
	public void openInventory() {
		//
	}

	@Override
	public void closeInventory() {
		//
	}

	@Override
	public Object getServerGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return null;
	}

	@Override
	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return null;
	}

	@Override
	public boolean connectsToItemPipe(final byte aSide) {
		return false;
	}

	@Override
	public float getExplosionResistance(final byte aSide) {
		return 10.0F;
	}

	@Override
	public ItemStack[] getRealInventory() {
		return this.mInventory;
	}

	@Override
	public void onColorChangeServer(final byte aColor) {
		//
	}

	@Override
	public void onColorChangeClient(final byte aColor) {
		//
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean renderInInventory(final Block aBlock, final int aMeta, final RenderBlocks aRenderer) {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean renderInWorld(final IBlockAccess aWorld, final int aX, final int aY, final int aZ, final Block aBlock, final RenderBlocks aRenderer) {
		return false;
	}

	@Override
	public void doExplosion(final long aExplosionPower) {
		final float tStrength = aExplosionPower < V[0] ? 1.0F : aExplosionPower < V[1] ? 2.0F : aExplosionPower < V[2] ? 3.0F : aExplosionPower < V[3] ? 4.0F : aExplosionPower < V[4] ? 5.0F : aExplosionPower < (V[4] * 2) ? 6.0F : aExplosionPower < V[5] ? 7.0F : aExplosionPower < V[6] ? 8.0F : aExplosionPower < V[7] ? 9.0F : 10.0F;
		final int tX = this.getBaseMetaTileEntity().getXCoord(), tY = this.getBaseMetaTileEntity().getYCoord(), tZ = this.getBaseMetaTileEntity().getZCoord();
		final World tWorld = this.getBaseMetaTileEntity().getWorld();
		GT_Utility.sendSoundToPlayers(tWorld, GregTech_API.sSoundList.get(209), 1.0F, -1, tX, tY, tZ);
		tWorld.setBlock(tX, tY, tZ, Blocks.air);
		if (GregTech_API.sMachineExplosions) {
			tWorld.createExplosion(null, tX + 0.5, tY + 0.5, tZ + 0.5, tStrength, true);
		}
	}

	@Override
	public int getLightOpacity() {
		return ((GregtechBaseMetaTileEntityLossless) this.getBaseMetaTileEntity()).getLightValue() > 0 ? 0 : 255;
	}

	@Override
	public void addCollisionBoxesToList(final World aWorld, final int aX, final int aY, final int aZ, final AxisAlignedBB inputAABB, final List<AxisAlignedBB> outputAABB, final Entity collider) {
		final AxisAlignedBB axisalignedbb1 = this.getCollisionBoundingBoxFromPool(aWorld, aX, aY, aZ);
		if ((axisalignedbb1 != null) && inputAABB.intersectsWith(axisalignedbb1)) {
			outputAABB.add(axisalignedbb1);
		}
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(final World aWorld, final int aX, final int aY, final int aZ) {
		return AxisAlignedBB.getBoundingBox(aX, aY, aZ, aX + 1, aY + 1, aZ + 1);
	}

	@Override
	public void onEntityCollidedWithBlock(final World aWorld, final int aX, final int aY, final int aZ, final Entity collider) {
		//
	}

	@Override
	public void onCreated(final ItemStack aStack, final World aWorld, final EntityPlayer aPlayer) {
		//
	}
}