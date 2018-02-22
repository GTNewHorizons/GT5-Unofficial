package gtPlusPlus.xmod.gregtech.common.tileentities.storage;

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
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMetaTileEntity;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
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

	public GregtechMetaEnergyBuffer(final int aID, final String aName, final String aNameRegional, final int aTier, final String aDescription, final int aSlotCount) {
		super(aID, aName, aNameRegional, aTier, aSlotCount, aDescription);
	}

	public GregtechMetaEnergyBuffer(final String aName, final int aTier, final String aDescription, final ITexture[][][] aTextures, final int aSlotCount) {
		super(aName, aTier, aSlotCount, aDescription, aTextures);
	}

	@Override
	public String[] getDescription() {
		return new String[] {this.mDescription, "Accepts/Outputs 4Amp", CORE.GT_Tooltip};
	}

	/*
	 * MACHINE_STEEL_SIDE
	 */

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
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[this.mTier]};
	}


	public ITexture[] getBack(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Overlay_Machine_Dimensional_Orange)};
	}


	public ITexture[] getBottom(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Overlay_Machine_Dimensional_Orange)};
	}


	public ITexture[] getTop(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Overlay_Machine_Screen_Logo)};
	}


	public ITexture[] getSides(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Overlay_Machine_Dimensional_Orange)};
	}


	public ITexture[] getFrontActive(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[this.mTier]};
	}


	public ITexture[] getBackActive(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Overlay_Machine_Dimensional_Orange)};
	}


	public ITexture[] getBottomActive(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Overlay_Machine_Dimensional_Orange)};
	}


	public ITexture[] getTopActive(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Overlay_Machine_Screen_Logo)};
	}


	public ITexture[] getSidesActive(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Overlay_Machine_Dimensional_Orange)};
	}

	/*@Override
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
	}*/

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

	/*@Override
	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
		return mTextures[aSide == aFacing ? 1 : 0][aColorIndex+1];
	}*/

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaEnergyBuffer(this.mName, this.mTier, this.mDescription, this.mTextures, this.mInventory.length);
	}

	@Override public boolean isSimpleMachine()						{return false;}
	@Override public boolean isElectric()							{return true;}
	@Override public boolean isValidSlot(final int aIndex)				{return true;}
	@Override public boolean isFacingValid(final byte aFacing)			{return true;}
	@Override public boolean isEnetInput() 							{return true;}
	@Override public boolean isEnetOutput() 						{return true;}
	@Override public boolean isInputFacing(final byte aSide)				{return aSide!=this.getBaseMetaTileEntity().getFrontFacing();}
	@Override public boolean isOutputFacing(final byte aSide)				{return aSide==this.getBaseMetaTileEntity().getFrontFacing();}
	@Override public boolean isTeleporterCompatible()				{return false;}
	@Override public long getMinimumStoredEU()						{return V[this.mTier]*2;}
	@Override public long maxEUStore()								{return V[this.mTier]*250000;}

	@Override
	public long maxEUInput() {
		return V[this.mTier];
	}

	@Override
	public long maxEUOutput() {
		return V[this.mTier];
	}

	@Override
	public long maxAmperesIn() {
		return 4;
	}

	@Override
	public long maxAmperesOut() {
		return 4;
	}
	@Override public int rechargerSlotStartIndex()					{return 0;}
	@Override public int dechargerSlotStartIndex()					{return 0;}
	@Override public int rechargerSlotCount()						{return 0;}
	@Override public int dechargerSlotCount()						{return 0;}
	@Override public int getProgresstime()							{return (int)this.getBaseMetaTileEntity().getUniversalEnergyStored();}
	@Override public int maxProgresstime()							{return (int)this.getBaseMetaTileEntity().getUniversalEnergyCapacity();}
	@Override public boolean isAccessAllowed(final EntityPlayer aPlayer)	{return true;}

	@Override
	public void saveNBTData(final NBTTagCompound aNBT) {
		//
	}

	@Override
	public void loadNBTData(final NBTTagCompound aNBT) {
		//
	}

	@Override
	public boolean onRightclick(final IGregTechTileEntity aBaseMetaTileEntity, final EntityPlayer aPlayer) {
		Logger.WARNING("Right Click on MTE by Player");
		if (aBaseMetaTileEntity.isClientSide())
		{
			return true;
			//aBaseMetaTileEntity.openGUI(aPlayer);
		}

		Logger.WARNING("MTE is Client-side");
		this.showEnergy(aPlayer.getEntityWorld(), aPlayer);
		return true;
	}

	private void showEnergy(final World worldIn, final EntityPlayer playerIn){
		final long tempStorage = this.getBaseMetaTileEntity().getStoredEU();
		final double c = ((double) tempStorage / this.maxEUStore()) * 100;
		final double roundOff = Math.round(c * 100.00) / 100.00;
		PlayerUtils.messagePlayer(playerIn, "Energy: " + GT_Utility.formatNumbers(tempStorage) + " EU at "+V[this.mTier]+"v ("+roundOff+"%)");

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
	public Object getServerGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_Container_1by1(aPlayerInventory, aBaseMetaTileEntity);
	}

	@Override
	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_GUIContainer_1by1(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName());
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
		String cur = GT_Utility.formatNumbers(this.getBaseMetaTileEntity().getStoredEU());
		String max = GT_Utility.formatNumbers(this.getBaseMetaTileEntity().getEUCapacity());

		// Right-align current storage with maximum storage
		String fmt = String.format("%%%ds", max.length());
		cur = String.format(fmt, cur);

		return new String[] {
				this.getLocalName(),
				cur+" EU stored",
				max+" EU capacity"};
	}

	@Override
	public boolean isGivingInformation() {
		return true;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(final int p_94128_1_) {
		return null;
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

}