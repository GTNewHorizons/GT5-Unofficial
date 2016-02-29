package miscutil.enderio.conduit;

import static gregtech.api.enums.GT_Values.V;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import ic2.api.item.IElectricItem;
import miscutil.core.handler.GuiHandler;
import miscutil.core.util.Utils;
import miscutil.gregtech.metatileentity.implementations.base.GregtechMetaTileEntity;
import miscutil.gregtech.util.IMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * 
 * This is the main construct for my Basic Machines such as the Automatic Extractor
 * Extend this class to make a simple Machine
 */
public class MetaConduitBase extends GregtechMetaTileEntity {

	/*
	 * public GregtechMetaEnergyBuffer() { super.this
	 * setCreativeTab(GregTech_API.TAB_GREGTECH); }
	 */

	public int mAmpage = 1;
	public boolean mCharge = false, mDecharge = false;
	public int mBatteryCount = 1, mChargeableCount = 1;

	public MetaConduitBase(int aID, String aName, String aNameRegional, int aTier, String aDescription, int aSlotCount) {
		super(aID, aName, aNameRegional, aTier, aSlotCount, aDescription);
	}

	public MetaConduitBase(String aName, int aTier, String aDescription, ITexture[][][] aTextures, int aSlotCount) {
		super(aName, aTier, aSlotCount, aDescription, aTextures);
	}

	@Override
	public String[] getDescription() {
		return new String[] {mDescription, mInventory.length + " Slots"};
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

	@Override
	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
		return mTextures[aSide == aFacing ? 1 : 0][aColorIndex+1];
	}

	@Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new MetaConduitBase(mName, mTier, mDescription, mTextures, mInventory.length);
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
	@Override public long getMinimumStoredEU()						{return V[mTier]*4*mInventory.length;}
	@Override public long maxEUStore()								{return V[mTier]*250;}

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
		return mChargeableCount * mAmpage;
	}

	@Override
	public long maxAmperesOut() {
		return mChargeableCount * mAmpage;
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
		Utils.LOG_WARNING("Begin Show Energy");
		final double c = ((double) getProgresstime() / maxProgresstime()) * 100;
		Utils.LOG_WARNING(""+c);
		final double roundOff = Math.round(c * 100.0) / 100.0;
		IMessage.messageThePlayer("Energy: " + getProgresstime() + " EU at "+V[mTier]+"v ("+roundOff+"%)");
		Utils.LOG_WARNING("Making new instance of Guihandler");
		GuiHandler block = new GuiHandler();
		Utils.LOG_WARNING("Guihandler.toString(): "+block.toString());
		block.getClientGuiElement(1, playerIn, worldIn, (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);
		
	}

	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		if (aBaseMetaTileEntity.isServerSide()) {
			mCharge = aBaseMetaTileEntity.getStoredEU() / 2 > aBaseMetaTileEntity
					.getEUCapacity() / 3;
			mDecharge = aBaseMetaTileEntity.getStoredEU()     < aBaseMetaTileEntity.getEUCapacity() / 3;
			mBatteryCount = 1;
			mChargeableCount = 1;
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
				"Internal Storage:",
				GT_Utility.formatNumbers(mStored)+" EU /",
				GT_Utility.formatNumbers(mMax)+" EU"};
	}
}