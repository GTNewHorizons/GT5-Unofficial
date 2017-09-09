package gtPlusPlus.xmod.gregtech.common.tileentities.generators.creative;

import static gregtech.api.enums.GT_Values.V;

import gregtech.api.enums.Textures;
import gregtech.api.gui.*;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.GregtechMetaEnergyBuffer;
import ic2.api.item.IElectricItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 *
 * This is the main construct for my Basic Machines such as the Automatic Extractor
 * Extend this class to make a simple Machine
 */
public class GregtechMetaCreativeEnergyBuffer extends GregtechMetaEnergyBuffer {


	public GregtechMetaCreativeEnergyBuffer(final String aName, final int aTier,
			final String aDescription, final ITexture[][][] aTextures, final int aSlotCount) {
		super(aName, aTier, aDescription, aTextures, aSlotCount);
		// TODO Auto-generated constructor stub
	}

	public GregtechMetaCreativeEnergyBuffer(final int aID, final String aName,
			final String aNameRegional, final int aTier, final String aDescription, final int aSlotCount) {
		super(aID, aName, aNameRegional, aTier, aDescription, aSlotCount);
	}

	@Override
	public String[] getDescription() {
		return new String[] {this.mDescription, "Added by: "	+ EnumChatFormatting.DARK_GREEN+"Alkalus"};
	}

	/*
	 * MACHINE_STEEL_SIDE
	 */
	@Override
	public ITexture[][][] getTextureSet(final ITexture[] aTextures) {
		final ITexture[][][] rTextures = new ITexture[2][17][];
		for (byte i = -1; i < 16; i++) {
			rTextures[0][i + 1] = new ITexture[] { new GT_RenderedTexture(
					Textures.BlockIcons.MACHINE_CASING_MAGIC_FRONT) };
			rTextures[1][i + 1] = new ITexture[] {
					new GT_RenderedTexture(
							Textures.BlockIcons.MACHINE_CASING_MAGIC_FRONT),
					this.mInventory.length > 4 ? Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[this.mTier]
							: Textures.BlockIcons.OVERLAYS_ENERGY_OUT[this.mTier] };
		}
		return rTextures;
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity,
			final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive,
			final boolean aRedstone) {
		return this.mTextures[aSide == aFacing ? 1 : 0][aColorIndex + 1];
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaCreativeEnergyBuffer(this.mName, this.mTier, this.mDescription,
				this.mTextures, this.mInventory.length);
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

	@Override
	public long getMinimumStoredEU() {
		return 0;
	}

	@Override
	public long maxEUStore() {
		return Long.MAX_VALUE;
	}

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
		return this.mChargeableCount * 16;
	}

	@Override
	public long maxAmperesOut() {
		return this.mChargeableCount * 16;
	}
	@Override public int rechargerSlotStartIndex()					{return 0;}
	@Override public int dechargerSlotStartIndex()					{return 0;}
	@Override public int rechargerSlotCount()						{return this.mCharge?this.mInventory.length:0;}
	@Override public int dechargerSlotCount()						{return this.mDecharge?this.mInventory.length:0;}
	@Override public int getProgresstime()							{return Integer.MAX_VALUE;}
	@Override public int maxProgresstime()							{return (int)this.getBaseMetaTileEntity().getUniversalEnergyCapacity();}
	@Override public boolean isAccessAllowed(final EntityPlayer aPlayer)	{return true;}

	@Override
	public Object getServerGUI(final int aID, final InventoryPlayer aPlayerInventory,
			final IGregTechTileEntity aBaseMetaTileEntity) {
		return null;
	}

	@Override
	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory,
			final IGregTechTileEntity aBaseMetaTileEntity) {
		return null;
	}

	@Override
	public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
		this.getBaseMetaTileEntity().increaseStoredEnergyUnits(Integer.MAX_VALUE, true);
		if (aBaseMetaTileEntity.isServerSide()) {
			aBaseMetaTileEntity.increaseStoredEnergyUnits(Integer.MAX_VALUE, true);
		}
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
	public long[] getStoredEnergy(){
		long tScale = this.getBaseMetaTileEntity().getEUCapacity();
		long tStored = this.getBaseMetaTileEntity().getStoredEU();
		//this.setEUVar(Long.MAX_VALUE);
		return new long[] { tStored, tScale };
	}

	private long count=0;
	private long mStored=0;
	private long mMax=0;

	@Override
	public String[] getInfoData() {
		this.count++;
		if((this.mMax==0)||((this.count%20)==0)){
			final long[] tmp = this.getStoredEnergy();
			this.mStored=tmp[0];
			this.mMax=tmp[1];
		}

		return new String[] {
				this.getLocalName(),
				"THIS IS A CREATIVE ITEM - FOR TESTING",
				GT_Utility.formatNumbers(this.mStored)+" EU /",
				GT_Utility.formatNumbers(this.mMax)+" EU"};
	}

	@Override
	public boolean isGivingInformation() {
		return true;
	}
}