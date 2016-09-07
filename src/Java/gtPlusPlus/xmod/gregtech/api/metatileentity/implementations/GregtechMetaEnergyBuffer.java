package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.GT_Values.V;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_Container_1by1;
import gregtech.api.gui.GT_GUIContainer_1by1;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMetaTileEntity;
import ic2.api.item.IElectricItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class GregtechMetaEnergyBuffer extends GregtechMetaTileEntity {

	/*
	 * public GregtechMetaEnergyBuffer() { super.this
	 * setCreativeTab(GregTech_API.TAB_GREGTECH); }
	 */

	public boolean mCharge = false, mDecharge = false;
	public int mBatteryCount = 1, mChargeableCount = 1;

	public GregtechMetaEnergyBuffer(int aID, String aName, String aNameRegional, int aTier, String aDescription, int aSlotCount) {
		super(aID, aName, aNameRegional, aTier, aSlotCount, aDescription);
	}

	public GregtechMetaEnergyBuffer(String aName, int aTier, String aDescription, ITexture[][][] aTextures, int aSlotCount) {
		super(aName, aTier, aSlotCount, aDescription, aTextures);
	}

	@Override
	public String[] getDescription() {
		return new String[] {mDescription, CORE.GT_Tooltip};
	}

	/*
	 * MACHINE_STEEL_SIDE
	 */
	@Override
	public ITexture[][][] getTextureSet(ITexture[] aTextures) {
		ITexture[][][] rTextures = new ITexture[2][17][];
		for (byte i = -1; i < 16; i++) {
			rTextures[0][i + 1] = new ITexture[] { new GT_RenderedTexture(
					Textures.BlockIcons.MACHINE_HEATPROOFCASING) };
			rTextures[1][i + 1] = new ITexture[] {
					new GT_RenderedTexture(
							Textures.BlockIcons.MACHINE_HEATPROOFCASING),
							mInventory.length > 4 ? Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[mTier]
									: Textures.BlockIcons.OVERLAYS_ENERGY_OUT[mTier] };
		}
		return rTextures;
	}

	/*
	 * @Override public ITexture[][][] getTextureSet(ITexture[] aTextures) {
	 * ITexture[][][] rTextures = new ITexture[5][17][]; for (byte i = -1; i <
	 * 16; i = (byte) (i + 1)) { ITexture[] tmp0 = { new GT_RenderedTexture(
	 * Textures.BlockIcons.MACHINE_STEEL_BOTTOM, Dyes.getModulation(i,
	 * Dyes._NULL.mRGBa)) }; rTextures[0][(i + 1)] = tmp0; ITexture[] tmp1 = {
	 * new GT_RenderedTexture( Textures.BlockIcons.MACHINE_STEEL_TOP) };
	 * rTextures[1][(i + 1)] = tmp1; ITexture[] tmp2 = { new GT_RenderedTexture(
	 * Textures.BlockIcons.MACHINE_STEEL_SIDE, Dyes.getModulation(i,
	 * Dyes._NULL.mRGBa)), new
	 * GT_RenderedTexture(Textures.BlockIcons.OVERLAY_PIPE) }; rTextures[2][(i +
	 * 1)] = tmp2; ITexture[] tmp4 = { new GT_RenderedTexture(
	 * Textures.BlockIcons.MACHINE_STEEL_SIDE, Dyes.getModulation(i,
	 * Dyes._NULL.mRGBa)), new
	 * GT_RenderedTexture(Textures.BlockIcons.BOILER_FRONT) }; rTextures[3][(i +
	 * 1)] = tmp4; ITexture[] tmp5 = { new GT_RenderedTexture(
	 * Textures.BlockIcons.MACHINE_STEEL_SIDE, Dyes.getModulation(i,
	 * Dyes._NULL.mRGBa)), new GT_RenderedTexture(
	 * Textures.BlockIcons.BOILER_FRONT_ACTIVE) }; rTextures[4][(i + 1)] = tmp5;
	 * } return rTextures; }
	 */

	@Override
	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
		return mTextures[aSide == aFacing ? 1 : 0][aColorIndex+1];
	}

	@Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GregtechMetaEnergyBuffer(mName, mTier, mDescription, mTextures, mInventory.length);
	}

	@Override public boolean isSimpleMachine()						{return false;}
	@Override public boolean isElectric()							{return true;}
	@Override public boolean isValidSlot(int aIndex)				{return true;}
	@Override public boolean isFacingValid(byte aFacing)			{return true;}
	@Override public boolean isEnetInput() 							{return true;}
	@Override public boolean isEnetOutput() 						{return true;}
	@Override public boolean isInputFacing(byte aSide)				{return aSide!=getBaseMetaTileEntity().getFrontFacing();}
	@Override public boolean isOutputFacing(byte aSide)				{return aSide==getBaseMetaTileEntity().getFrontFacing();}
	@Override public boolean isTeleporterCompatible()				{return false;}
	@Override public long getMinimumStoredEU()						{return V[mTier]*2;}
	@Override public long maxEUStore()								{return V[mTier]*250000;}

	@Override
	public long maxEUInput() {
		return V[mTier];
	}

	@Override
	public long maxEUOutput() {
		return V[mTier];
	}

	@Override
	public long maxAmperesIn() {
		return mChargeableCount * 4;
	}

	@Override
	public long maxAmperesOut() {
		return mChargeableCount * 4;
	}
	@Override public int rechargerSlotStartIndex()					{return 0;}
	@Override public int dechargerSlotStartIndex()					{return 0;}
	@Override public int rechargerSlotCount()						{return mCharge?mInventory.length:0;}
	@Override public int dechargerSlotCount()						{return mDecharge?mInventory.length:0;}
	@Override public int getProgresstime()							{return (int)getBaseMetaTileEntity().getUniversalEnergyStored();}
	@Override public int maxProgresstime()							{return (int)getBaseMetaTileEntity().getUniversalEnergyCapacity();}
	@Override public boolean isAccessAllowed(EntityPlayer aPlayer)	{return true;}

	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		//
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		//
	}

	@Override
	public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
		Utils.LOG_WARNING("Right Click on MTE by Player");
		if (aBaseMetaTileEntity.isClientSide()) return true;
		//aBaseMetaTileEntity.openGUI(aPlayer);

		Utils.LOG_WARNING("MTE is Client-side");
		showEnergy(aPlayer.getEntityWorld(), aPlayer);  
		return true;
	}

	private void showEnergy(World worldIn, EntityPlayer playerIn){
		long tempStorage = getStoredEnergy()[0];		
		final double c = ((double) tempStorage / maxEUStore()) * 100;
		final double roundOff = Math.round(c * 100.00) / 100.00;
		Utils.messagePlayer(playerIn, "Energy: " + tempStorage + " EU at "+V[mTier]+"v ("+roundOff+"%)");

	}
		//Utils.LOG_WARNING("Begin Show Energy");
		/*
		 * 
		//Utils.LOG_INFO("getProgresstime: "+tempStorage+"  maxProgresstime: "+maxEUStore()+"  C: "+c);
				Utils.LOG_INFO("getProgressTime: "+getProgresstime());
				Utils.LOG_INFO("maxProgressTime: "+maxProgresstime());
				Utils.LOG_INFO("getMinimumStoredEU: "+getMinimumStoredEU());
				Utils.LOG_INFO("maxEUStore: "+maxEUStore());*/
		/*final long d = (tempStorage * 100L) / maxEUStore();
		Utils.LOG_INFO("getProgresstime: "+tempStorage+"  maxProgresstime: "+maxEUStore()+"  D: "+d);
		final double roundOff2 = Math.round(d * 100.00) / 100.00;
		Utils.messagePlayer(playerIn, "Energy: " + tempStorage + " EU at "+V[mTier]+"v ("+roundOff2+"%)");
		Utils.LOG_WARNING("Making new instance of Guihandler");
		GuiHandler block = new GuiHandler();
		Utils.LOG_WARNING("Guihandler.toString(): "+block.toString());
		block.getClientGuiElement(1, playerIn, worldIn, (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);*/


	@Override
	public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_Container_1by1(aPlayerInventory, aBaseMetaTileEntity);
	}

	@Override
	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_GUIContainer_1by1(aPlayerInventory, aBaseMetaTileEntity, getLocalName());
	}

	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		
	}

	@Override
	public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return false;
	}

	@Override
	public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return false;
	}

	public long[] getStoredEnergy(){
		long tScale = getBaseMetaTileEntity().getEUCapacity();
		long tStored = getBaseMetaTileEntity().getStoredEU();
		if (mInventory != null) {
			for (ItemStack aStack : mInventory) {
				if (GT_ModHandler.isElectricItem(aStack)) {

					if (aStack.getItem() instanceof GT_MetaBase_Item) {
						Long[] stats = ((GT_MetaBase_Item) aStack.getItem())
								.getElectricStats(aStack);
						if (stats != null) {
							tScale = tScale + stats[0];
							tStored = tStored
									+ ((GT_MetaBase_Item) aStack.getItem())
									.getRealCharge(aStack);
						}
					} else if (aStack.getItem() instanceof IElectricItem) {
						tStored = tStored
								+ (long) ic2.api.item.ElectricItem.manager
								.getCharge(aStack);
						tScale = tScale
								+ (long) ((IElectricItem) aStack.getItem())
								.getMaxCharge(aStack);
					}
				}
			}

		}
		return new long[] { tStored, tScale };
	}

	private long count=0;
	private long mStored=0;
	private long mMax=0;

	@Override
	public String[] getInfoData() {
		count++;
		if(mMax==0||count%20==0){
			long[] tmp = getStoredEnergy();
			mStored=tmp[0];
			mMax=tmp[1];
		}

		return new String[] {
				getLocalName(),
				GT_Utility.formatNumbers(mStored)+" EU /",
				GT_Utility.formatNumbers(mMax)+" EU"};
	}

	@Override
	public boolean isGivingInformation() {
		return true;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
		return null;
	}

	@Override
	public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_, int p_102007_3_) {
		return false;
	}

	@Override
	public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, int p_102008_3_) {
		return false;
	}

	@Override
	public int getSizeInventory() {
		return 0;
	}

	@Override
	public ItemStack getStackInSlot(int p_70301_1_) {
		return null;
	}

	@Override
	public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
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
	public void markDirty() {
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		return false;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		return false;
	}

}