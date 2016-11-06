package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.creative;

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
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GregtechMetaEnergyBuffer;
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


	public GregtechMetaCreativeEnergyBuffer(String aName, int aTier,
			String aDescription, ITexture[][][] aTextures, int aSlotCount) {
		super(aName, aTier, aDescription, aTextures, aSlotCount);
		// TODO Auto-generated constructor stub
	}

	public GregtechMetaCreativeEnergyBuffer(int aID, String aName,
			String aNameRegional, int aTier, String aDescription, int aSlotCount) {
		super(aID, aName, aNameRegional, aTier, aDescription, aSlotCount);
	}

	public boolean mCharge = false, mDecharge = false;
	public int mBatteryCount = 1, mChargeableCount = 1;

	@Override
	public String[] getDescription() {
		return new String[] {mDescription, "Added by: "	+ EnumChatFormatting.DARK_GREEN+"Alkalus"};
	}

	/*
	 * MACHINE_STEEL_SIDE
	 */
	@Override
	public ITexture[][][] getTextureSet(ITexture[] aTextures) {
		ITexture[][][] rTextures = new ITexture[2][17][];
		for (byte i = -1; i < 16; i++) {
			rTextures[0][i + 1] = new ITexture[] { new GT_RenderedTexture(
					Textures.BlockIcons.MACHINE_CASING_MAGIC_FRONT) };
			rTextures[1][i + 1] = new ITexture[] {
					new GT_RenderedTexture(
							Textures.BlockIcons.MACHINE_CASING_MAGIC_FRONT),
							mInventory.length > 4 ? Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[mTier]
									: Textures.BlockIcons.OVERLAYS_ENERGY_OUT[mTier] };
		}
		return rTextures;
	}

	@Override
	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity,
			byte aSide, byte aFacing, byte aColorIndex, boolean aActive,
			boolean aRedstone) {
		return mTextures[aSide == aFacing ? 1 : 0][aColorIndex + 1];
	}

	@Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GregtechMetaCreativeEnergyBuffer(mName, mTier, mDescription,
				mTextures, mInventory.length);
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

	@Override
	public long getMinimumStoredEU() {
		return 1;
	}

	@Override
	public long maxEUStore() {
		return Long.MAX_VALUE;
	}

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
		return mChargeableCount * 16;
	}

	@Override
	public long maxAmperesOut() {
		return mChargeableCount * 16;
	}
	@Override public int rechargerSlotStartIndex()					{return 0;}
	@Override public int dechargerSlotStartIndex()					{return 0;}
	@Override public int rechargerSlotCount()						{return mCharge?mInventory.length:0;}
	@Override public int dechargerSlotCount()						{return mDecharge?mInventory.length:0;}
	@Override public int getProgresstime()							{return Integer.MAX_VALUE;}
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
	public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory,
			IGregTechTileEntity aBaseMetaTileEntity) {
		switch (mInventory.length) {
		case  1: return new GT_Container_1by1(aPlayerInventory, aBaseMetaTileEntity);
		case  4: return new GT_Container_2by2(aPlayerInventory, aBaseMetaTileEntity);
		case  9: return new GT_Container_3by3(aPlayerInventory, aBaseMetaTileEntity);
		case 16: return new GT_Container_4by4(aPlayerInventory, aBaseMetaTileEntity);
		}
		return new GT_Container_1by1(aPlayerInventory, aBaseMetaTileEntity);
	}

	@Override
	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory,
			IGregTechTileEntity aBaseMetaTileEntity) {
		switch (mInventory.length) {
		case  1: return new GT_GUIContainer_1by1(aPlayerInventory, aBaseMetaTileEntity, getLocalName());
		case  4: return new GT_GUIContainer_2by2(aPlayerInventory, aBaseMetaTileEntity, getLocalName());
		case  9: return new GT_GUIContainer_3by3(aPlayerInventory, aBaseMetaTileEntity, getLocalName());
		case 16: return new GT_GUIContainer_4by4(aPlayerInventory, aBaseMetaTileEntity, getLocalName());
		}
		return new GT_GUIContainer_1by1(aPlayerInventory, aBaseMetaTileEntity, getLocalName());
	}

	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		this.getBaseMetaTileEntity().increaseStoredEnergyUnits(Integer.MAX_VALUE, true);
		if (aBaseMetaTileEntity.isServerSide()) {
			mCharge = aBaseMetaTileEntity.getStoredEU() / 2 > aBaseMetaTileEntity
					.getEUCapacity() / 3;
			mDecharge = aBaseMetaTileEntity.getStoredEU()     < aBaseMetaTileEntity.getEUCapacity() / 3;
			mBatteryCount = 1;
			mChargeableCount = 1;
			this.getBaseMetaTileEntity().increaseStoredEnergyUnits(mMax, true);
			for (ItemStack tStack : mInventory) if (GT_ModHandler.isElectricItem(tStack, mTier)) {
				if (GT_ModHandler.isChargerItem(tStack)) mBatteryCount++;
				mChargeableCount++;
			}
		}
	}

	@Override
	public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		if(GT_ModHandler.isElectricItem(aStack)&&aStack.getUnlocalizedName().startsWith("gt.metaitem.01.")){
			String name = aStack.getUnlocalizedName();
			if(name.equals("gt.metaitem.01.32510")||
					name.equals("gt.metaitem.01.32511")||
					name.equals("gt.metaitem.01.32520")||
					name.equals("gt.metaitem.01.32521")||
					name.equals("gt.metaitem.01.32530")||
					name.equals("gt.metaitem.01.32531")){
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		if(!GT_Utility.isStackValid(aStack)){
			return false;
		}
		if(GT_ModHandler.isElectricItem(aStack, this.mTier)){
			return true;
		}
		return false;
	}

	@Override
	public long[] getStoredEnergy(){
		long tScale = getBaseMetaTileEntity().getEUCapacity();
		long tStored = getBaseMetaTileEntity().getStoredEU();
		this.setEUVar(Long.MAX_VALUE);
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
				"THIS IS A CREATIVE ITEM - FOR TESTING",
				GT_Utility.formatNumbers(mStored)+" EU /",
				GT_Utility.formatNumbers(mMax)+" EU"};
	}

	@Override
	public boolean isGivingInformation() {
		return true;
	}
}