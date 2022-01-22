package gtPlusPlus.xmod.gregtech.common.tileentities.redstone;

import java.util.*;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.interfaces.IRedstoneCircuitBlock;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.*;
import gtPlusPlus.xmod.gregtech.api.gui.computer.GT_Container_RedstoneCircuitBlock;
import gtPlusPlus.xmod.gregtech.api.gui.computer.GT_GUIContainer_RedstoneCircuitBlock;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class GT_MetaTileEntity_RedstoneCircuitBlock extends GT_MetaTileEntity_RedstoneBase implements IRedstoneCircuitBlock {

	public int mGate = 0, mGateData[] = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
	public boolean bOutput = true;

	public GT_MetaTileEntity_RedstoneCircuitBlock(int aID) {
		super(aID, "redstone.circuit", "Redstone Circuit Block", 1, 5, "Computes Redstone");
	}

	public GT_MetaTileEntity_RedstoneCircuitBlock(final String aName, String aDescription, final ITexture[][][] aTextures) {
		super(aName, 1, 5, aDescription, aTextures);
	}

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT_MetaTileEntity_RedstoneCircuitBlock(this.mName, mDescription, this.mTextures);
	}

	@Override
	public Object getServerGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_Container_RedstoneCircuitBlock(aPlayerInventory, aBaseMetaTileEntity);
	}

	@Override
	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_GUIContainer_RedstoneCircuitBlock(aPlayerInventory, aBaseMetaTileEntity);
	}

	@Override
	public boolean hasSidedRedstoneOutputBehavior() {
		return true;
	}

	@Override
	public boolean isEnetInput() {
		return true;
	}

	@Override
	public boolean isEnetOutput() {
		return true;
	}

	@Override
	public boolean isInputFacing(byte aSide) {
		return !this.isOutputFacing(aSide);
	}

	@Override
	public boolean isElectric() {
		return true;
	}

	@Override
	public boolean isPneumatic() {
		return false;
	}

	@Override
	public boolean isSteampowered() {
		return false;
	}

	@Override
	public boolean isOutputFacing(byte aSide) {
		return aSide == this.getOutputFacing();
	}

	@Override
	public long getMinimumStoredEU() {
		return 512;
	}

	@Override
	public long maxEUInput() {
		return GT_Values.V[1];
	}

	@Override
	public long maxEUOutput() {
		return bOutput ? GT_Values.V[1] : 0;
	}

	@Override
	public long maxAmperesIn() {
		return 2;
	}

	@Override
	public long maxAmperesOut() {
		return 1;
	}

	@Override
	public int getSizeInventory() {
		return 5;
	}

	@Override
	public long maxEUStore() {
		return GT_Values.V[3] * 1024;
	}

	@Override
	public boolean onRightclick(final IGregTechTileEntity aBaseMetaTileEntity, final EntityPlayer aPlayer) {
		if (aBaseMetaTileEntity.isClientSide()) {
			return true;
		}
		aBaseMetaTileEntity.openGUI(aPlayer, 147);
		return true;
	}

	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		aNBT.setInteger("mGate", mGate);
		aNBT.setIntArray("mGateData", mGateData);
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		mGate = aNBT.getInteger("mGate");
		mGateData = aNBT.getIntArray("mGateData");
		if (mGateData.length != 8)
			mGateData = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
	}

	public void switchOutput() {
		bOutput = !bOutput;
	}

	public void switchGateForward(boolean aShift) {
		try {
			Set<Integer> tKeys = GregTech_API.sCircuitryBehaviors.keySet();
			ArrayList<Integer> tList = new ArrayList<Integer>();
			tList.addAll(tKeys);
			if (tList.size() <= 0)
				return;
			Collections.sort(tList);
			if (!GregTech_API.sCircuitryBehaviors.containsKey(mGate))
				mGate = tList.get(0);
			int tIndex = Collections.binarySearch(tList, mGate);
			tIndex += aShift ? 16 : 1;
			while (tIndex >= tList.size())
				tIndex -= tList.size();
			mGate = tList.get(tIndex);
			switchGate();
		}
		catch (Throwable e) {
			GT_Log.err.print(e);
		}
	}

	public void switchGateBackward(boolean aShift) {
		try {
			Set<Integer> tKeys = GregTech_API.sCircuitryBehaviors.keySet();
			ArrayList<Integer> tList = new ArrayList<Integer>();
			tList.addAll(tKeys);
			if (tList.size() <= 0)
				return;
			Collections.sort(tList);
			if (!GregTech_API.sCircuitryBehaviors.containsKey(mGate))
				mGate = tList.get(0);
			int tIndex = Collections.binarySearch(tList, mGate);
			tIndex -= aShift ? 16 : 1;
			while (tIndex < 0)
				tIndex += tList.size();
			mGate = tList.get(tIndex);
			switchGate();
		}
		catch (Throwable e) {
			GT_Log.err.print(e);
		}
	}

	@Override
	public void onFacingChange() {
		resetRedstone();
	}

	private void resetRedstone() {
		getBaseMetaTileEntity().setInternalOutputRedstoneSignal((byte) 0, (byte) 0);
		getBaseMetaTileEntity().setInternalOutputRedstoneSignal((byte) 1, (byte) 0);
		getBaseMetaTileEntity().setInternalOutputRedstoneSignal((byte) 2, (byte) 0);
		getBaseMetaTileEntity().setInternalOutputRedstoneSignal((byte) 3, (byte) 0);
		getBaseMetaTileEntity().setInternalOutputRedstoneSignal((byte) 4, (byte) 0);
		getBaseMetaTileEntity().setInternalOutputRedstoneSignal((byte) 5, (byte) 0);
	}

	public void changeGateData(int aIndex, int aValue) {
		mGateData[aIndex] += aValue;
		validateGateData();
	}

	public void stackGateData(int aIndex, ItemStack aStack) {
		mGateData[aIndex] = GT_Utility.stackToInt(aStack);
		validateGateData();
	}

	private void switchGate() {
		resetRedstone();
		for (int i = 0; i < mGateData.length; i++)
			mGateData[i] = 0;
		GT_CircuitryBehavior tBehaviour = GregTech_API.sCircuitryBehaviors.get(mGate);
		if (tBehaviour != null)
			try {
				tBehaviour.initParameters(mGateData, this);
			}
			catch (Throwable e) {
				GT_Log.err.print(e);
			}
		validateGateData();
	}

	private void validateGateData() {
		GT_CircuitryBehavior tBehaviour = GregTech_API.sCircuitryBehaviors.get(mGate);
		if (tBehaviour != null)
			try {
				tBehaviour.validateParameters(mGateData, this);
			}
			catch (Throwable e) {
				GT_Log.err.print(e);
			}
	}

	@Override
	public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
		super.onFirstTick(aBaseMetaTileEntity);
		getBaseMetaTileEntity().setGenericRedstoneOutput(true);
		validateGateData();
	}

	@Override
	public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		super.onPreTick(aBaseMetaTileEntity, aTick);
		getBaseMetaTileEntity().setGenericRedstoneOutput(true);
		if (getBaseMetaTileEntity().isAllowedToWork() && getBaseMetaTileEntity().isServerSide()) {
			mInventory[0] = mInventory[1] = mInventory[2] = mInventory[3] = mInventory[4] = null;
			if (getBaseMetaTileEntity().getUniversalEnergyStored() >= getMinimumStoredEU()) {
				if (getBaseMetaTileEntity().isActive()) {
					GT_CircuitryBehavior tBehaviour = GregTech_API.sCircuitryBehaviors.get(mGate);
					if (tBehaviour != null) {
						try {
							tBehaviour.onTick(mGateData, this);
							if (tBehaviour.displayItemStack(mGateData, this, 0))
								mInventory[1] = getCoverByID(mGateData[0]);
							if (tBehaviour.displayItemStack(mGateData, this, 1))
								mInventory[2] = getCoverByID(mGateData[1]);
							if (tBehaviour.displayItemStack(mGateData, this, 2))
								mInventory[3] = getCoverByID(mGateData[2]);
							if (tBehaviour.displayItemStack(mGateData, this, 3))
								mInventory[4] = getCoverByID(mGateData[3]);
						}
						catch (Throwable e) {
							GT_Log.err.print(e);
						}
					}
				}
				getBaseMetaTileEntity().setErrorDisplayID(0);
			}
			else {
				getBaseMetaTileEntity().setErrorDisplayID(1);
			}
		}
	}
	
	@Override
	public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
		super.onPostTick(aBaseMetaTileEntity, aTick);		
		//Only Calc server-side
		if (!this.getBaseMetaTileEntity().isServerSide()) {
			return;
		}		
		//Emit Redstone		
		for (byte i=0;i<6;i++) {
			byte aRedstone = getBaseMetaTileEntity().getOutputRedstoneSignal(i);
			this.getBaseMetaTileEntity().setInternalOutputRedstoneSignal(i, aRedstone);
		}
		
	}

	@Override
	public final boolean hasRedstoneSignal() {
		for (byte i=0;i<6;i++) {
			if (getBaseMetaTileEntity().getOutputRedstoneSignal(i) > 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean allowGeneralRedstoneOutput() {
		return true;
	}

	/** The Item List for Covers */
	public static final Map<Integer, ItemStack> sCoversItems = new HashMap<Integer, ItemStack>();

	private static void initCovers() {
		for (GT_ItemStack aKey : GregTech_API.sCovers.keySet()) {
			ItemStack aStack = aKey.toStack().copy();
			if (aStack != null) {
				sCoversItems.put(GT_Utility.stackToInt(aStack), aStack);
			}
		}
	}

	public static ItemStack getCoverByID(int aStack) {
		if (sCoversItems.isEmpty()) {
			initCovers();
		}
		return sCoversItems.get(Integer.valueOf(aStack));
	}

	@Override
	public byte getOutputFacing() {
		return getBaseMetaTileEntity().getBackFacing();
	}

	@Override
	public boolean setRedstone(byte aStrength, byte aSide) {
		if (getOutputRedstone(aSide) != aStrength) {
			if (getBaseMetaTileEntity().decreaseStoredEnergyUnits(1, false)) {
				getBaseMetaTileEntity().setInternalOutputRedstoneSignal(aSide, aStrength);
				getBaseMetaTileEntity().setErrorDisplayID(0);
				return true;
			}
			else {
				getBaseMetaTileEntity().setErrorDisplayID(1);
				return false;
			}
		}
		return false;
	}

	/*	@Override
		public int getTextureIndex(byte aSide, byte aFacing, boolean aActive, boolean aRedstone) {
			if (aSide == getOutputFacing()) {
				if (aSide == 0)
					return aRedstone ? 56 : 54;
				if (aSide == 1)
					return aRedstone ? 53 : 52;
				return aRedstone ? 94 : 93;
			}
			if (aSide == 0)
				return aRedstone ? 60 : 59;
			if (aSide == 1)
				return aRedstone ? 58 : 57;
			return aRedstone ? 62 : 61;
		}*/

	@Override
	public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return false;
	}

	@Override
	public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return false;
	}

	@Override
	public byte getOutputRedstone(byte aSide) {
		return getBaseMetaTileEntity().getOutputRedstoneSignal(aSide);
	}

	@Override
	public byte getInputRedstone(byte aSide) {
		return getBaseMetaTileEntity().getInternalInputRedstoneSignal(aSide);
	}

	@Override
	public Block getBlockAtSide(byte aSide) {
		return getBaseMetaTileEntity().getBlockAtSide(aSide);
	}

	@Override
	public byte getMetaIDAtSide(byte aSide) {
		return getBaseMetaTileEntity().getMetaIDAtSide(aSide);
	}

	@Override
	public TileEntity getTileEntityAtSide(byte aSide) {
		return getBaseMetaTileEntity().getTileEntityAtSide(aSide);
	}

	@Override
	public int getRandom(int aRange) {
		return getBaseMetaTileEntity().getRandomNumber(aRange);
	}

	@Override
	public GT_CoverBehavior getCover(byte aSide) {
		return getBaseMetaTileEntity().getCoverBehaviorAtSide(aSide);
	}

	@Override
	public int getCoverID(byte aSide) {
		return getBaseMetaTileEntity().getCoverIDAtSide(aSide);
	}

	@Override
	public int getCoverVariable(byte aSide) {
		return getBaseMetaTileEntity().getCoverDataAtSide(aSide);
	}

	@Override
	public ICoverable getOwnTileEntity() {
		return getBaseMetaTileEntity();
	}

	@Override
	public ITexture[][][] getTextureSet(final ITexture[] aTextures) {
		final ITexture[][][] rTextures = new ITexture[10][17][];
		for (byte i = -1; i < 16; i++) {
			rTextures[0][i + 1] = this.getSides(i);
			rTextures[1][i + 1] = this.getBack(i);
			rTextures[2][i + 1] = this.getBottom(i);
			rTextures[3][i + 1] = this.getTop(i);
			rTextures[4][i + 1] = this.getSides(i);
			rTextures[5][i + 1] = this.getSidesActive(i);
			rTextures[6][i + 1] = this.getBackActive(i);
			rTextures[7][i + 1] = this.getBottomActive(i);
			rTextures[8][i + 1] = this.getTopActive(i);
			rTextures[9][i + 1] = this.getSidesActive(i);
		}
		return rTextures;
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		return this.mTextures[(aActive || hasRedstoneSignal() ? 5 : 0) + (aSide == aFacing ? 0 : aSide == GT_Utility.getOppositeSide(aFacing) ? 1 : aSide == 0 ? 2 : aSide == 1 ? 3 : 4)][aColorIndex
				+ 1];
	}

	private GT_RenderedTexture getBase() {
		return new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Simple_Top);
	}

	public ITexture[] getTop(final byte aColor) {
		return new ITexture[]{getBase(), new GT_RenderedTexture(TexturesGtBlock.Casing_Redstone_Top_Off)};
	}

	public ITexture[] getTopActive(final byte aColor) {
		return new ITexture[]{getBase(), new GT_RenderedTexture(TexturesGtBlock.Casing_Redstone_Top_On)};
	}
	
	public ITexture[] getBack(final byte aColor) {
		return new ITexture[] {getBase(), new GT_RenderedTexture(TexturesGtBlock.Casing_Redstone_Side_Off), new GT_RenderedTexture(TexturesGtBlock.Casing_InventoryManagaer_Red)};
	}
	
	public ITexture[] getBackActive(final byte aColor) {
		return new ITexture[] {getBase(), new GT_RenderedTexture(TexturesGtBlock.Casing_Redstone_Side_On), new GT_RenderedTexture(TexturesGtBlock.Casing_InventoryManagaer_Red_Redstone)};
	}

	public ITexture[] getBottom(final byte aColor) {
		return new ITexture[]{getBase(), new GT_RenderedTexture(TexturesGtBlock.Casing_Redstone_Bottom_Off)};
	}

	public ITexture[] getBottomActive(final byte aColor) {
		return new ITexture[]{getBase(), new GT_RenderedTexture(TexturesGtBlock.Casing_Redstone_Bottom_On)};
	}

	public ITexture[] getSides(final byte aColor) {
		return new ITexture[]{getBase(), new GT_RenderedTexture(TexturesGtBlock.Casing_Redstone_Side_Off)};
	}

	public ITexture[] getSidesActive(final byte aColor) {
		return new ITexture[]{getBase(), new GT_RenderedTexture(TexturesGtBlock.Casing_Redstone_Side_On)};
	}

}
