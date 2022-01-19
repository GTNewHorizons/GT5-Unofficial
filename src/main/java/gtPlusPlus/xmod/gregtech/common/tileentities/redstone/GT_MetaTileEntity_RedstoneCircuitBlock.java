package gtPlusPlus.xmod.gregtech.common.tileentities.redstone;

import java.util.*;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.IRedstoneCircuitBlock;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.util.*;
import gtPlusPlus.xmod.gregtech.api.gui.computer.GT_Container_RedstoneCircuitBlock;
import gtPlusPlus.xmod.gregtech.api.gui.computer.GT_GUIContainer_RedstoneCircuitBlock;
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
		super(aID, "redstone.circuit", "Redstone Circuit Block", 0, 0, "Computes Redstone");
	}

	public GT_MetaTileEntity_RedstoneCircuitBlock(final String aName, String aDescription, final ITexture[][][] aTextures) {
		super(aName, 0, 0, aDescription, aTextures);
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
		return !isOutputFacing(aSide);
	}
	
	@Override
	public boolean isOutputFacing(byte aSide) {
		return getBaseMetaTileEntity().getBackFacing() == aSide;
	}
	
	@Override
	public long getMinimumStoredEU() {
		return 500;
	}
	
	@Override
	public long maxEUInput() {
		return 32;
	}
	
	@Override
	public long maxEUOutput() {
		return bOutput ? 32 : 0;
	}
	
	@Override
	public int getSizeInventory() {
		return 5;
	}
	
	@Override
	public long maxEUStore() {
		return 1000;
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
			if (getBaseMetaTileEntity().getUniversalEnergyStored() > 400) {
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
	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITexture[][][] getTextureSet(ITexture[] aTextures) {
		// TODO Auto-generated method stub
		return null;
	}
}
