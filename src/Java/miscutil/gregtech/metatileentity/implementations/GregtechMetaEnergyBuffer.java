package miscutil.gregtech.metatileentity.implementations;

import static gregtech.api.enums.GT_Values.V;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_Container_1by1;
import gregtech.api.gui.GT_Container_2by2;
import gregtech.api.gui.GT_Container_3by3;
import gregtech.api.gui.GT_Container_4by4;
import gregtech.api.gui.GT_GUIContainer_1by1;
import gregtech.api.gui.GT_GUIContainer_2by2;
import gregtech.api.gui.GT_GUIContainer_3by3;
import gregtech.api.gui.GT_GUIContainer_4by4;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import ic2.api.item.IElectricItem;

import java.util.List;

import miscutil.core.handler.GuiHandler;
import miscutil.core.util.Utils;
import miscutil.core.waila.IWailaInfoProvider;
import miscutil.gregtech.metatileentity.implementations.base.GregtechMetaTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import crazypants.enderio.gui.TooltipAddera;
import crazypants.util.Lang;
import crazypants.util.Util;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * 
 * This is the main construct for my Basic Machines such as the Automatic Extractor
 * Extend this class to make a simple Machine
 */
public class GregtechMetaEnergyBuffer extends GregtechMetaTileEntity implements IWailaInfoProvider{

	/*
	 * public GregtechMetaEnergyBuffer() { super.this
	 * setCreativeTab(GregTech_API.TAB_GREGTECH); }
	 */

	public boolean mCharge = false, mDecharge = false;
	public int mBatteryCount = 1, mChargeableCount = 1;

	public GregtechMetaEnergyBuffer(int aID, String aName, String aNameRegional, int aTier, String aDescription, int aSlotCount) {
		super(aID, aName, aNameRegional, aTier, aSlotCount, aDescription);
		//setCreativeTab(AddToCreativeTab.tabMachines);
	}

	public GregtechMetaEnergyBuffer(String aName, int aTier, String aDescription, ITexture[][][] aTextures, int aSlotCount) {
		super(aName, aTier, aSlotCount, aDescription, aTextures);
	}

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
	@Override public long getMinimumStoredEU()						{return V[mTier]*16*mInventory.length;}
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
		Utils.LOG_WARNING("Begin Show Energy");
		final double c = ((double) getProgresstime() / maxProgresstime()) * 100;
		Utils.LOG_WARNING(""+c);
		final double roundOff = Math.round(c * 100.0) / 100.0;
		Utils.messagePlayer(playerIn, "Energy: " + getProgresstime() + " EU at "+V[mTier]+"v ("+roundOff+"%)");
		Utils.LOG_WARNING("Making new instance of Guihandler");
		GuiHandler block = new GuiHandler();
		Utils.LOG_WARNING("Guihandler.toString(): "+block.toString());
		block.getClientGuiElement(1, playerIn, worldIn, (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);
		
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
				"Stored Items:",
				GT_Utility.formatNumbers(mStored)+" EU /",
				GT_Utility.formatNumbers(mMax)+" EU"};
	}

	@Override
	public boolean isGivingInformation() {
		return true;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_,
			int p_102007_3_) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_,
			int p_102008_3_) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getSizeInventory() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ItemStack getStackInSlot(int p_70301_1_) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getInventoryName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasCustomInventoryName() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void markDirty() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void openInventory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeInventory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void getWailaInfo(List<String> tooltip, EntityPlayer player, World world, int x, int y, int z) {
	        String format = Util.TAB + Util.ALIGNRIGHT + EnumChatFormatting.WHITE;
	        if(TooltipAddera.showAdvancedTooltips()) {
	          tooltip.add(String.format("%s : %s%s%sRF/t ", Lang.localize("capbank.maxIO"), format, fmt.format(this.maxEUStore()), Util.TAB + Util.ALIGNRIGHT));
	          tooltip
	              .add(String.format("%s : %s%s%sRF/t ", Lang.localize("capbank.maxIn"), format, fmt.format(this.maxEUInput()), Util.TAB + Util.ALIGNRIGHT));
	          tooltip
	              .add(String.format("%s : %s%s%sRF/t ", Lang.localize("capbank.maxOut"), format, fmt.format(this.maxEUOutput()), Util.TAB + Util.ALIGNRIGHT));

	          tooltip.add("");
	        }

	        long stored = this.getProgresstime();
	        long max = this.maxEUStore();
	        tooltip.add(String.format("%s%s%s / %s%s%s RF", EnumChatFormatting.WHITE, fmt.format(stored), EnumChatFormatting.RESET, EnumChatFormatting.WHITE,
	            fmt.format(max),
	            EnumChatFormatting.RESET));

	        //int change = Math.round(nw.getAverageChangePerTick());
	        String color = EnumChatFormatting.WHITE.toString();
	       /* if(change > 0) {
	          color = EnumChatFormatting.GREEN.toString() + "+";
	        } else if(change < 0) {
	          color = EnumChatFormatting.RED.toString();
	        }*/
	        tooltip
	            .add(String.format("%s%s%sRF/t ", color, fmt.format("null"), " " + EnumChatFormatting.RESET.toString()));

	      }

	@Override
	public int getDefaultDisplayMask(World paramWorld, int paramInt1, int paramInt2, int paramInt3) {
		return IWailaInfoProvider.BIT_DETAILED;
	}
}