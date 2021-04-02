package gregtech.api.metatileentity.implementations;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import static gregtech.api.enums.GT_Values.V;

public abstract class GT_MetaTileEntity_Buffer extends GT_MetaTileEntity_TieredMachineBlock {
    private static final int OUTPUT_INDEX = 0;
    private static final int ARROW_RIGHT_INDEX = 1;
    private static final int ARROW_DOWN_INDEX = 2;
    private static final int ARROW_LEFT_INDEX = 3;
    private static final int ARROW_UP_INDEX = 4;
    private static final int FRONT_INDEX = 5;

    public boolean bOutput = false, bRedstoneIfFull = false, bInvert = false, bStockingMode = false, bSortStacks = false;
    public int mSuccess = 0, mTargetStackSize = 0;

    public GT_MetaTileEntity_Buffer(int aID, String aName, String aNameRegional, int aTier, int aInvSlotCount, String aDescription) {
        super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription);
    }

    public GT_MetaTileEntity_Buffer(int aID, String aName, String aNameRegional, int aTier, int aInvSlotCount, String[] aDescription) {
                super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription);
    }

    public GT_MetaTileEntity_Buffer(String aName, int aTier, int aInvSlotCount, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public GT_MetaTileEntity_Buffer(String aName, int aTier, int aInvSlotCount, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[ForgeDirection.VALID_DIRECTIONS.length][17][];
        ITexture tIcon = getOverlayIcon();
        ITexture tOut = new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_PIPE_OUT);
        ITexture tUp = new GT_RenderedTexture(Textures.BlockIcons.ARROW_UP);
        ITexture tDown = new GT_RenderedTexture(Textures.BlockIcons.ARROW_DOWN);
        ITexture tLeft = new GT_RenderedTexture(Textures.BlockIcons.ARROW_LEFT);
        ITexture tRight = new GT_RenderedTexture(Textures.BlockIcons.ARROW_RIGHT);
        for (int i = 0; i < rTextures[0].length; i++) {
            rTextures[OUTPUT_INDEX][i] = new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][i], tOut};
            rTextures[ARROW_RIGHT_INDEX][i] = new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][i], tRight, tIcon};
            rTextures[ARROW_DOWN_INDEX][i] = new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][i], tDown, tIcon};
            rTextures[ARROW_LEFT_INDEX][i] = new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][i], tLeft, tIcon};
            rTextures[ARROW_UP_INDEX][i] = new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][i], tUp, tIcon};
            rTextures[FRONT_INDEX][i] = new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][i], tIcon};
        }
        return rTextures;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        int colorIndex = aColorIndex + 1;
        ForgeDirection side = ForgeDirection.VALID_DIRECTIONS[aSide];
        ForgeDirection facing = ForgeDirection.VALID_DIRECTIONS[aFacing];
        if (side == facing) return mTextures[FRONT_INDEX][colorIndex];
        if (ForgeDirection.OPPOSITES[aSide] == aFacing) return mTextures[OUTPUT_INDEX][colorIndex];
        switch (facing) {
            case DOWN:
                return mTextures[ARROW_UP_INDEX][colorIndex]; // ARROW_UP
            case UP:
                return mTextures[ARROW_DOWN_INDEX][colorIndex]; // ARROW_DOWN
            case NORTH:
                switch (side) {
                    case DOWN:
                    case UP:
                        return mTextures[ARROW_DOWN_INDEX][colorIndex]; // ARROW_DOWN
                    case WEST:
                        return mTextures[ARROW_RIGHT_INDEX][colorIndex]; // ARROW_RIGHT
                    case EAST:
                        return mTextures[ARROW_LEFT_INDEX][colorIndex]; // ARROW_LEFT
                    default:
                }
                break;
            case SOUTH:
                switch (side) {
                    case DOWN:
                    case UP:
                        return mTextures[ARROW_UP_INDEX][colorIndex]; // ARROW_UP
                    case WEST:
                        return mTextures[ARROW_LEFT_INDEX][colorIndex]; // ARROW_LEFT
                    case EAST:
                        return mTextures[ARROW_RIGHT_INDEX][colorIndex]; // ARROW_RIGHT
                    default:
                }
                break;
            case WEST:
                switch (side) {
                    case UP:
                    case SOUTH:
                        return mTextures[ARROW_RIGHT_INDEX][colorIndex]; // ARROW_RIGHT
                    case DOWN:
                    case NORTH:
                        return mTextures[ARROW_LEFT_INDEX][colorIndex]; // ARROW_LEFT
                    default:
                }
                break;
            case EAST:
                switch (side) {
                    case UP:
                    case SOUTH:
                        return mTextures[ARROW_LEFT_INDEX][colorIndex]; // ARROW_LEFT
                    case DOWN:
                    case NORTH:
                        return mTextures[ARROW_RIGHT_INDEX][colorIndex]; // ARROW_RIGHT
                    default:
                }
                break;
            default:
        }
        return mTextures[FRONT_INDEX][colorIndex];
    }

    @Override
    public boolean isSimpleMachine() {
        return false;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return aIndex < mInventory.length - 1;
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
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
    public boolean isTeleporterCompatible() {
        return false;
    }

    @Override
    public long getMinimumStoredEU() {
        return 512L;
    }

    @Override
    public long maxEUStore() {
        return 512L + V[mTier] * 50L;
    }

    @Override
    public long maxEUInput() {
        return V[mTier];
    }

    @Override
    public long maxEUOutput() {
        return bOutput ? V[mTier] : 0L;
    }

    @Override
    public long maxAmperesIn() {
        return 2;
    }

    @Override
    public long maxAmperesOut() {
        return 2;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    public abstract ITexture getOverlayIcon();

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) return true;
        aBaseMetaTileEntity.openGUI(aPlayer);
        return true;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setBoolean("bInvert", bInvert);
        aNBT.setBoolean("bOutput", bOutput);
        aNBT.setBoolean("bRedstoneIfFull", bRedstoneIfFull);
        aNBT.setBoolean("bStockingMode", bStockingMode);
        aNBT.setInteger("mTargetStackSize", mTargetStackSize);
        aNBT.setBoolean("bSortStacks", bSortStacks);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        bInvert = aNBT.getBoolean("bInvert");
        bOutput = aNBT.getBoolean("bOutput");
        bRedstoneIfFull = aNBT.getBoolean("bRedstoneIfFull");
        bSortStacks = aNBT.getBoolean("bSortStacks");
        if (aNBT.hasKey("bStockingMode")) { // Adding new key to existing NBT, need to protect if it is not there.
            bStockingMode = aNBT.getBoolean("bStockingMode");
        }
        mTargetStackSize = aNBT.getInteger("mTargetStackSize");
    }

    @Override
    public void setItemNBT(NBTTagCompound aNBT) {
        super.setItemNBT(aNBT);
        if (mTargetStackSize > 0) aNBT.setInteger("mTargetStackSize", mTargetStackSize);
    }

    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (aSide == getBaseMetaTileEntity().getBackFacing()) {
        	
            mTargetStackSize = (byte) ((mTargetStackSize + (aPlayer.isSneaking()? -1 : 1)) % 65);
            if(mTargetStackSize <0){mTargetStackSize = 64;}
            if (mTargetStackSize == 0) {
                GT_Utility.sendChatToPlayer(aPlayer, trans("098","Do not regulate Item Stack Size"));
            } else {
                GT_Utility.sendChatToPlayer(aPlayer, trans("099","Regulate Item Stack Size to: ") + mTargetStackSize);
            }
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        if (aBaseMetaTileEntity.isAllowedToWork() && aBaseMetaTileEntity.isServerSide() && (aBaseMetaTileEntity.hasWorkJustBeenEnabled() || aBaseMetaTileEntity.hasInventoryBeenModified() || aTimer % 200 == 0 || mSuccess > 0)) {
            mSuccess--;
            updateSlots();
            moveItems(aBaseMetaTileEntity, aTimer);
            for(byte b = 0;b<6;b++)
            	aBaseMetaTileEntity.setInternalOutputRedstoneSignal(b,bInvert ? (byte)15 : (byte)0);
            if (bRedstoneIfFull) {
                for(byte b = 0;b<6;b++)
                    aBaseMetaTileEntity.setInternalOutputRedstoneSignal(b,bInvert ? (byte)0 : (byte)15);
                for (int i = 0; i < mInventory.length; i++)
                    if (isValidSlot(i)) {
                        if (mInventory[i] == null) {
                            for(byte b = 0;b<6;b++)
                                aBaseMetaTileEntity.setInternalOutputRedstoneSignal(b,bInvert ? (byte)15 : (byte)0);
                            break;
                        }
                    }
            }
        }
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
    	for(byte b = 0;b<6;b++)
            aBaseMetaTileEntity.setInternalOutputRedstoneSignal(b,(byte)0);
    }

    protected void moveItems(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        int tCost;
        if( bStockingMode )
            tCost = GT_Utility.moveMultipleItemStacks(aBaseMetaTileEntity, aBaseMetaTileEntity.getTileEntityAtSide(aBaseMetaTileEntity.getBackFacing()), aBaseMetaTileEntity.getBackFacing(), aBaseMetaTileEntity.getFrontFacing(), null, false, mTargetStackSize == 0 ? 64 : (byte) mTargetStackSize, mTargetStackSize == 0 ? 1 : (byte) mTargetStackSize, (byte) 64, (byte) 1,1);
        else
            tCost = GT_Utility.moveMultipleItemStacks(aBaseMetaTileEntity, aBaseMetaTileEntity.getTileEntityAtSide(aBaseMetaTileEntity.getBackFacing()), aBaseMetaTileEntity.getBackFacing(), aBaseMetaTileEntity.getFrontFacing(), null, false, (byte) 64, (byte) 1, mTargetStackSize == 0 ? 64 : (byte) mTargetStackSize, mTargetStackSize == 0 ? 1 : (byte) mTargetStackSize,1);

        if (tCost > 0 || aBaseMetaTileEntity.hasInventoryBeenModified()) {
            mSuccess = 50;
        }
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return true;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return aSide != aBaseMetaTileEntity.getBackFacing();
    }
    
    @Override
    public boolean allowGeneralRedstoneOutput(){
    	return true;
    }

    public void updateSlots() {
        for (int i = 0; i < mInventory.length; i++)
            if (mInventory[i] != null && mInventory[i].stackSize <= 0) mInventory[i] = null;
        fillStacksIntoFirstSlots();
    }

    protected void fillStacksIntoFirstSlots() {
        if (bSortStacks) {
            for (int i = 0; i < mInventory.length; i++)
                for (int j = i + 1; j < mInventory.length; j++)
                    if (mInventory[j] != null && (mInventory[i] == null || GT_Utility.areStacksEqual(mInventory[i], mInventory[j])))
                        GT_Utility.moveStackFromSlotAToSlotB(getBaseMetaTileEntity(), getBaseMetaTileEntity(), j, i, (byte) 64, (byte) 1, (byte) 64, (byte) 1);
        }
    }

    @Override
    public boolean onSolderingToolRightClick(byte aSide, byte aWrenchingSide, EntityPlayer aPlayer, float aX, float aY, float aZ)  {
        if (aPlayer.isSneaking()) {
            // I was so proud of all this but I literally just copied code from OutputBus
            bSortStacks = !bSortStacks;
            GT_Utility.sendChatToPlayer(aPlayer, trans("200", "Sort mode: " + (bSortStacks ? "Disabled" : "Enabled")));
            return true;
        }
        return super.onSolderingToolRightClick(aSide,aWrenchingSide,aPlayer,aX,aY,aZ);
    }

}
