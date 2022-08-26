package gregtech.api.multitileentity.multiblock.base;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.multitileentity.MultiTileEntityRegistry;
import gregtech.api.multitileentity.base.BaseMultiTileEntity;
import gregtech.api.multitileentity.base.BaseNontickableMultiTileEntity;
import gregtech.api.multitileentity.interfaces.IMultiBlockController;
import gregtech.api.multitileentity.interfaces.IMultiTileEntity.IMTE_BreakBlock;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_CoverBehaviorBase;
import gregtech.api.util.GT_Util;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.render.GT_MultiTexture;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.math.LongMath.log2;
import static gregtech.api.enums.GT_Values.ALL_VALID_SIDES;
import static gregtech.api.enums.GT_Values.B;
import static gregtech.api.enums.GT_Values.NBT;
import static gregtech.api.enums.GT_Values.SIDE_UNKNOWN;
import static gregtech.api.enums.Textures.BlockIcons.FLUID_IN_SIGN;
import static gregtech.api.enums.Textures.BlockIcons.FLUID_OUT_SIGN;
import static gregtech.api.enums.Textures.BlockIcons.ITEM_IN_SIGN;
import static gregtech.api.enums.Textures.BlockIcons.ITEM_OUT_SIGN;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ENERGY_IN_MULTI;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ENERGY_OUT_MULTI;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE_IN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE_OUT;
import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

public class MultiBlockPart extends BaseNontickableMultiTileEntity implements IMTE_BreakBlock {
    public static final int
        NOTHING         = 0,
        ENERGY_IN       = B[0],
        ENERGY_OUT      = B[1],
        FLUID_IN        = B[2],
        FLUID_OUT       = B[3],
        ITEM_IN         = B[4],
        ITEM_OUT        = B[5];

    protected final List<Integer> BASIC_MODES = new ArrayList<>(Arrays.asList(NOTHING, ENERGY_IN, ENERGY_OUT, FLUID_IN, FLUID_OUT, ITEM_IN, ITEM_OUT));

    protected ChunkCoordinates mTargetPos = null;
    protected IMultiBlockController mTarget = null;

    protected int mAllowedModes = NOTHING;  // BITMASK - Modes allowed for this part
    protected byte mMode = 0;               // Mode selected for this part

    /**
     * What Part Tier is this part?  All Basic Casings are Tier 1, and will allow:
     *  Energy, Item, Fluid input/output.  Some of the more advanced modes can be set to require a higher tier part.
     */
    public int getPartTier() {
        return 1;
    }

    public int getLockedInventory() {
        return -1;
    }

    public void setTarget(IMultiBlockController aTarget, int aAllowedModes) {
        mTarget = aTarget;
        mTargetPos = (mTarget == null ? null : mTarget.getCoords());
        mAllowedModes = aAllowedModes;
    }

    @Override
    protected void addDebugInfo(EntityPlayer aPlayer, int aLogLevel, ArrayList<String> tList) {
        final IMultiBlockController controller = getTarget(false);
        if(controller != null) {
            tList.add("Has controller");
        } else {
            tList.add("No Controller");
        }
        tList.add("Casing Mode: " + getModeName(mMode));
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);
        currenttip.add(String.format("Mode: %s", getModeName(mMode)));
    }

    public IMultiBlockController getTarget(boolean aCheckValidity) {
        if (mTargetPos == null) return null;
        if (mTarget == null || mTarget.isDead()) {
            if (worldObj.blockExists(mTargetPos.posX, mTargetPos.posY, mTargetPos.posZ)) {
                final TileEntity te = worldObj.getTileEntity(mTargetPos.posX, mTargetPos.posY, mTargetPos.posZ);
                if (te instanceof IMultiBlockController) {
                    mTarget = (IMultiBlockController)te;
                } else {
                    mTargetPos = null;
                }
            }
        }
        if(aCheckValidity) {
            return mTarget != null && mTarget.checkStructure(false) ? mTarget : null;
        }
        else
            return mTarget;
    }

    @Override
    public void readMultiTileNBT(NBTTagCompound aNBT) {
        if (aNBT.hasKey(NBT.ALLOWED_MODES)) mAllowedModes = aNBT.getInteger(NBT.ALLOWED_MODES);
        if (aNBT.hasKey(NBT.MODE)) mMode = aNBT.getByte(NBT.MODE);
        if (aNBT.hasKey(NBT.TARGET)) {
            mTargetPos = new ChunkCoordinates(aNBT.getInteger(NBT.TARGET_X), aNBT.getShort(NBT.TARGET_Y), aNBT.getInteger(NBT.TARGET_Z));
        }

    }

    @Override
    public void writeMultiTileNBT(NBTTagCompound aNBT) {
        if (mAllowedModes != NOTHING) aNBT.setInteger(NBT.ALLOWED_MODES, mAllowedModes);
        if (mMode != 0) aNBT.setInteger(NBT.MODE, mMode);
        if (mTargetPos != null) {
            aNBT.setBoolean(NBT.TARGET, true);
            aNBT.setInteger(NBT.TARGET_X, mTargetPos.posX);
            aNBT.setShort(NBT.TARGET_Y, (short)mTargetPos.posY);
            aNBT.setInteger(NBT.TARGET_Z, mTargetPos.posZ);
        }
    }

    /**
     * True if `aMode` is one of the allowed modes
     */
    public boolean hasMode(int aMode) {
        return (mAllowedModes & aMode) != 0;
    }

    /**
     * Returns true if the part has any of the modes provided, and that mode is the currently selected mode
     */
    public boolean modeSelected(int... aModes) {
        for(int aMode : aModes) {
            if (hasMode(aMode) && mMode == getModeOrdinal(aMode))
                return true;
        }
        return false;
    }

    @Override
    public boolean breakBlock() {
        final IMultiBlockController tTarget = getTarget(false);
        if (tTarget != null) tTarget.onStructureChange();
        return false;
    }

    @Override
    public void onBlockAdded() {
        for (byte tSide : ALL_VALID_SIDES) {
            final TileEntity te = getTileEntityAtSide(tSide);
            if (te instanceof MultiBlockPart) {
                final IMultiBlockController tController = ((MultiBlockPart)te).getTarget(false);
                if (tController != null) tController.onStructureChange();
            } else if (te instanceof IMultiBlockController) {
                ((IMultiBlockController)te).onStructureChange();
            }
        }
    }

    @Override
    public byte getTextureData() {
        return mMode;
    }

    @Override
    public void setTextureData(byte aData) {
        mMode = aData;
    }


    @Override
    public void loadTextureNBT(NBTTagCompound aNBT) {
        // Loading the registry
        final String textureName = aNBT.getString(NBT.TEXTURE);
        mTextures = new IIconContainer[] {
            new Textures.BlockIcons.CustomIcon("multitileentity/multiblockparts/"+textureName+"/bottom"),
            new Textures.BlockIcons.CustomIcon("multitileentity/multiblockparts/"+textureName+"/top"),
            new Textures.BlockIcons.CustomIcon("multitileentity/multiblockparts/"+textureName+"/side"),
            new Textures.BlockIcons.CustomIcon("multitileentity/multiblockparts/"+textureName+"/overlay/bottom"),
            new Textures.BlockIcons.CustomIcon("multitileentity/multiblockparts/"+textureName+"/overlay/top"),
            new Textures.BlockIcons.CustomIcon("multitileentity/multiblockparts/"+textureName+"/overlay/side")
        };
    }

    @Override
    public void copyTextures() {
        // Loading an instance
        final TileEntity tCanonicalTileEntity = MultiTileEntityRegistry.getCanonicalTileEntity(getMultiTileEntityRegistryID(), getMultiTileEntityID());
        if(tCanonicalTileEntity instanceof MultiBlockPart)
            mTextures = ((MultiBlockPart)tCanonicalTileEntity).mTextures;
    }


    @Override
    public ITexture[] getTexture(Block aBlock, byte aSide, boolean isActive, int aRenderPass) {
        // For normal parts - texture comes from BaseMTE; overlay based on current mode
        // TODO(MTE) - For Advanced parts they might come from somewhere else
        final ITexture baseTexture = TextureFactory.of(super.getTexture(aBlock, aSide, isActive, aRenderPass));
        if(mMode != 0 && aSide == mFacing) {
            if(mMode == getModeOrdinal(ITEM_IN))
                return new ITexture[]{ baseTexture, TextureFactory.of(OVERLAY_PIPE_IN), TextureFactory.of(ITEM_IN_SIGN) };
            if(mMode == getModeOrdinal(ITEM_OUT))
                return new ITexture[]{ baseTexture, TextureFactory.of(OVERLAY_PIPE_OUT), TextureFactory.of(ITEM_OUT_SIGN) };
            if(mMode == getModeOrdinal(FLUID_IN))
                return new ITexture[]{ baseTexture, TextureFactory.of(OVERLAY_PIPE_IN), TextureFactory.of(FLUID_IN_SIGN) };
            if(mMode == getModeOrdinal(FLUID_OUT))
                return new ITexture[]{ baseTexture, TextureFactory.of(OVERLAY_PIPE_OUT), TextureFactory.of(FLUID_OUT_SIGN) };
            if(mMode == getModeOrdinal(ENERGY_IN))
                return new ITexture[]{ baseTexture, TextureFactory.of(OVERLAY_ENERGY_IN_MULTI)};
            if(mMode == getModeOrdinal(ENERGY_OUT))
                return new ITexture[]{ baseTexture, TextureFactory.of(OVERLAY_ENERGY_OUT_MULTI)};

        }
        return new ITexture[]{ baseTexture };
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return false;
    }

    protected String getModeName(int aMode) {
        if(aMode == NOTHING)
            return "Nothing";
        if(aMode == getModeOrdinal(ITEM_IN))
            return "Item Input";
        if(aMode == getModeOrdinal(ITEM_OUT))
            return "Item Output";
        if(aMode == getModeOrdinal(FLUID_IN))
            return "Fluid Input";
        if(aMode == getModeOrdinal(FLUID_OUT))
            return "Fluid Output";
        if(aMode == getModeOrdinal(ENERGY_IN))
            return "Energy Input";
        if(aMode == getModeOrdinal(ENERGY_OUT))
            return "Energy Output";
        return "Unknown";
    }

    protected byte getModeOrdinal(int aMode) {
        // log2 returns the bit position of the only bit set, add 1 to account for 0 being NOTHING
        // NOTE: Must be a power of 2 (single bit)
        return (byte)(log2(aMode, RoundingMode.UNNECESSARY) + 1);
    }
    protected byte getNextAllowedMode(List<Integer> allowedModes) {
        if(mAllowedModes == NOTHING)
            return NOTHING;

        final int numModes = allowedModes.size();
        for(byte i = 1 ; i <= numModes ; i++) {
            final byte curMode = (byte)((mMode + i) % numModes);
            if(curMode == NOTHING || hasMode(1 << (curMode - 1)))
                return curMode;
        }
        // Nothing valid found
        return 0;
    }

    @Override
    public boolean onMalletRightClick(EntityPlayer aPlayer, ItemStack tCurrentItem, byte wrenchSide, float aX, float aY, float aZ) {
        if(mAllowedModes == NOTHING)
            return true;

        mMode = getNextAllowedMode(BASIC_MODES);
        GT_Utility.sendChatToPlayer(aPlayer, "Mode set to `" + getModeName(mMode) + "' (" + mMode + ")");
        sendClientData((EntityPlayerMP) aPlayer);
        return true;
    }

    @Override
    public void setLightValue(byte aLightValue) {

    }

    @Override
    public byte getComparatorValue(byte aSide) {
        return 0;
    }


    @Override public String getTileEntityName() {
        return "gt.multitileentity.multiblock.part";
    }

    /**
     * TODO: Make sure the energy/item/fluid hatch is facing that way! or has that mode enabled on that side
     * Check SIDE_UNKNOWN for or coverbehavior
     */

    /**
     * Fluid - Depending on the part type - proxy it to the multiblock controller, if we have one
     */
    @Override
    public int fill(ForgeDirection aDirection, FluidStack aFluidStack, boolean aDoFill) {
        if (!modeSelected(FLUID_IN)) return 0;
        final byte aSide = (byte)aDirection.ordinal();
        if(aDirection != ForgeDirection.UNKNOWN && (aSide != mFacing || !coverLetsFluidIn(aSide, aFluidStack == null ? null : aFluidStack.getFluid())))
            return 0;
        final IMultiBlockController controller = getTarget(true);
        return controller == null ? 0 : controller.fill(this, aDirection, aFluidStack, aDoFill);
    }

    @Override
    public FluidStack drain(ForgeDirection aDirection, FluidStack aFluidStack, boolean aDoDrain) {
        if (!modeSelected(FLUID_OUT)) return null;
        final byte aSide = (byte)aDirection.ordinal();
        if(aDirection != ForgeDirection.UNKNOWN && (aSide != mFacing || !coverLetsFluidOut(aSide, aFluidStack == null ? null : aFluidStack.getFluid())))
            return null;
        final IMultiBlockController controller = getTarget(true);
        return controller == null ? null : controller.drain(this, aDirection, aFluidStack, aDoDrain);
    }

    @Override
    public FluidStack drain(ForgeDirection aDirection, int aAmountToDrain, boolean aDoDrain) {
        if (!modeSelected(FLUID_OUT)) return null;
        final byte aSide = (byte)aDirection.ordinal();
        final IMultiBlockController controller = getTarget(true);
        if (controller == null) return null;
        final FluidStack aFluidStack = controller.getDrainableFluid(aSide);
        if(aDirection != ForgeDirection.UNKNOWN && (aSide != mFacing || !coverLetsFluidOut(aSide, aFluidStack == null ? null : aFluidStack.getFluid())))
            return null;
        return controller.drain(this, aDirection, aAmountToDrain, aDoDrain);
    }

    @Override
    public boolean canFill(ForgeDirection aDirection, Fluid aFluid) {
        if (!modeSelected(FLUID_IN)) return false;
        final byte aSide = (byte)aDirection.ordinal();
        if(aDirection != ForgeDirection.UNKNOWN && (aSide != mFacing || !coverLetsFluidIn(aSide, aFluid)))
            return false;
        final IMultiBlockController controller = getTarget(true);
        return controller != null && controller.canFill(this, aDirection, aFluid);
    }

    @Override
    public boolean canDrain(ForgeDirection aDirection, Fluid aFluid) {
        if (!modeSelected(FLUID_OUT)) return false;
        final byte aSide = (byte)aDirection.ordinal();
        if(aDirection != ForgeDirection.UNKNOWN && (aSide != mFacing || !coverLetsFluidOut(aSide, aFluid)))
            return false;
        final IMultiBlockController controller = getTarget(true);
        return controller != null && controller.canDrain(this, aDirection, aFluid);
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection aDirection) {
        final byte aSide = (byte) aDirection.ordinal();
        if (!modeSelected(FLUID_IN, FLUID_OUT) || (aSide != SIDE_UNKNOWN && aSide != mFacing)) return GT_Values.emptyFluidTankInfo;
        final IMultiBlockController controller = getTarget(true);
        if(controller == null) return GT_Values.emptyFluidTankInfo;

        final GT_CoverBehaviorBase<?> tCover = getCoverBehaviorAtSideNew(aSide);
        final int coverId = getCoverIDAtSide(aSide);
        final ISerializableObject complexCoverData = getComplexCoverDataAtSide(aSide);

        if((controller.isLiquidInput(aSide) && tCover.letsFluidIn(aSide, coverId, complexCoverData, null, controller)) ||
           (controller.isLiquidOutput(aSide) && tCover.letsFluidOut(aSide, coverId, complexCoverData, null, controller)))
            return controller.getTankInfo(this, aDirection);

        return GT_Values.emptyFluidTankInfo;
    }

    /**
     * Energy - Depending on the part type - proxy to the multiblock controller, if we have one
     */

    @Override
    public boolean isEnetInput() {
        return modeSelected(ENERGY_IN);
    }

    @Override
    public boolean isEnetOutput() {
        return modeSelected(ENERGY_OUT);
    }

    @Override
    public boolean isUniversalEnergyStored(long aEnergyAmount) {
        if (!modeSelected(ENERGY_OUT, ENERGY_IN)) return false;
        final IMultiBlockController controller = getTarget(true);
        return controller != null && controller.isUniversalEnergyStored(this, aEnergyAmount);

    }

    @Override
    public long getUniversalEnergyStored() {
        if (!modeSelected(ENERGY_OUT, ENERGY_IN)) return 0;
        final IMultiBlockController controller = getTarget(true);
        return controller != null ? controller.getUniversalEnergyStored(this) : 0;
    }

    @Override
    public long getUniversalEnergyCapacity() {
        if (!modeSelected(ENERGY_OUT, ENERGY_IN)) return 0;
        final IMultiBlockController controller = getTarget(true);
        return controller != null ? controller.getUniversalEnergyCapacity(this) : 0;
    }

    @Override
    public long getOutputAmperage() {
        if (!modeSelected(ENERGY_OUT)) return 0;
        final IMultiBlockController controller = getTarget(true);
        return controller != null ? controller.getOutputAmperage(this) : 0;
    }

    @Override
    public long getOutputVoltage() {
        if (!modeSelected(ENERGY_OUT)) return 0;
        final IMultiBlockController controller = getTarget(true);
        return controller != null ? controller.getOutputVoltage(this) : 0;
    }

    @Override
    public long getInputAmperage() {
        if (!modeSelected(ENERGY_IN)) return 0;
        final IMultiBlockController controller = getTarget(true);
        return (controller != null && hasMode(ENERGY_IN)) ? controller.getInputAmperage(this) : 0;
    }

    @Override
    public long getInputVoltage() {
        if (!modeSelected(ENERGY_IN)) return 0;
        final IMultiBlockController controller = getTarget(true);
        return (controller != null && hasMode(ENERGY_IN)) ? controller.getInputVoltage(this) : 0;
    }

    @Override
    public boolean decreaseStoredEnergyUnits(long aEnergy, boolean aIgnoreTooLittleEnergy) {
        if (!modeSelected(ENERGY_IN)) return false;
        final IMultiBlockController controller = getTarget(true);
        return controller != null && hasMode(ENERGY_OUT) && controller.decreaseStoredEnergyUnits(this, aEnergy, aIgnoreTooLittleEnergy);
    }

    @Override
    public boolean increaseStoredEnergyUnits(long aEnergy, boolean aIgnoreTooMuchEnergy) {
        if (!modeSelected(ENERGY_IN)) return false;
        final IMultiBlockController controller = getTarget(true);
        return controller != null && hasMode(ENERGY_IN) && controller.increaseStoredEnergyUnits(this, aEnergy, aIgnoreTooMuchEnergy);
    }

    @Override
    public boolean drainEnergyUnits(byte aSide, long aVoltage, long aAmperage) {
        if(!modeSelected(ENERGY_OUT) || (mFacing != SIDE_UNKNOWN && (mFacing != aSide || !coverLetsEnergyOut(aSide)))) return false;
        final IMultiBlockController controller = getTarget(true);
        return controller != null && controller.drainEnergyUnits(this, aSide, aVoltage, aAmperage);
    }


    @Override
    public long injectEnergyUnits(byte aSide, long aVoltage, long aAmperage) {
        if (!modeSelected(ENERGY_IN) || (mFacing != SIDE_UNKNOWN && (mFacing != aSide || !coverLetsEnergyIn(aSide)))) return 0;
        final IMultiBlockController controller = getTarget(true);
        return controller != null ? controller.injectEnergyUnits(this, aSide, aVoltage, aAmperage) : 0;
    }


    @Override
    public long getAverageElectricInput() {
        if (!modeSelected(ENERGY_IN)) return 0;
        final IMultiBlockController controller = getTarget(true);
        return controller != null? controller.getAverageElectricInput(this) : 0;
    }

    @Override
    public long getAverageElectricOutput() {
        if (!modeSelected(ENERGY_OUT)) return 0;
        final IMultiBlockController controller = getTarget(true);
        return controller != null ? controller.getAverageElectricOutput(this) : 0;
    }

    @Override
    public long getStoredEU() {
        if (!modeSelected(ENERGY_OUT, ENERGY_IN)) return 0;
        final IMultiBlockController controller = getTarget(true);
        return controller != null ? controller.getStoredEU(this) : 0;
    }

    @Override
    public long getEUCapacity() {
        if (!modeSelected(ENERGY_OUT, ENERGY_IN)) return 0;
        final IMultiBlockController controller = getTarget(true);
        return controller != null ? controller.getEUCapacity(this) : 0;
    }

    @Override
    public boolean inputEnergyFrom(byte aSide) {
        if (!modeSelected(ENERGY_IN) || (mFacing != SIDE_UNKNOWN && (mFacing != aSide || !coverLetsEnergyIn(aSide)))) return false;
        final IMultiBlockController controller = getTarget(true);
        return controller != null && controller.inputEnergyFrom(this, aSide);
    }
    @Override
    public boolean outputsEnergyTo(byte aSide) {
        if(!modeSelected(ENERGY_OUT) || (mFacing != SIDE_UNKNOWN && (mFacing != aSide || !coverLetsEnergyOut(aSide)))) return false;
        final IMultiBlockController controller = getTarget(true);
        return controller != null && controller.outputsEnergyTo(this, aSide);
    }

    // End Energy

    /**
     * Inventory - Depending on the part type - proxy to the multiblock controller, if we have one
     */

    @Override
    public boolean hasInventoryBeenModified() {
        final IMultiBlockController controller = getTarget(true);
        return (controller != null && controller.hasInventoryBeenModified(this));
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        final IMultiBlockController controller = getTarget(true);
        return (controller != null && controller.isValidSlot(this, aIndex));
    }

    @Override
    public boolean addStackToSlot(int aIndex, ItemStack aStack) {
        if (!modeSelected(ITEM_IN)) return false;
        final IMultiBlockController controller = getTarget(true);
        return (controller != null && controller.addStackToSlot(this, aIndex, aStack));
    }

    @Override
    public boolean addStackToSlot(int aIndex, ItemStack aStack, int aAmount) {
        if (!modeSelected(ITEM_IN)) return false;
        final IMultiBlockController controller = getTarget(true);
        return (controller != null && controller.addStackToSlot(this, aIndex, aStack, aAmount));
    }


    @Override
    public int[] getAccessibleSlotsFromSide(int aSide) {
        if (!modeSelected(ITEM_IN, ITEM_OUT) || (mFacing != SIDE_UNKNOWN && mFacing != aSide)) return GT_Values.emptyIntArray;
        final IMultiBlockController controller = getTarget(true);
        return controller != null ? controller.getAccessibleSlotsFromSide(this, (byte) aSide) : GT_Values.emptyIntArray;
    }

    @Override
    public boolean canInsertItem(int aSlot, ItemStack aStack, int aSide) {
        if (!modeSelected(ITEM_IN) || (mFacing != SIDE_UNKNOWN && (mFacing != aSide || !coverLetsItemsIn((byte)aSide, aSlot)))) return false;
        final IMultiBlockController controller = getTarget(true);
        return (controller != null && controller.canInsertItem(this, aSlot, aStack, (byte) aSide));
    }
    @Override
    public boolean canExtractItem(int aSlot, ItemStack aStack, int aSide) {
        if (!modeSelected(ITEM_OUT) || (mFacing != SIDE_UNKNOWN && (mFacing != aSide || !coverLetsItemsOut((byte)aSide, aSlot)))) return false;
        final IMultiBlockController controller = getTarget(true);
        return (controller != null && controller.canExtractItem(this, aSlot, aStack, (byte) aSide));
    }

    @Override
    public int getSizeInventory() {
        if (!modeSelected(ITEM_IN, ITEM_OUT)) return 0;
        final IMultiBlockController controller = getTarget(true);
        return controller != null ? controller.getSizeInventory(this) : 0;
    }

    @Override
    public ItemStack getStackInSlot(int aSlot) {
        if (!modeSelected(ITEM_IN, ITEM_OUT)) return null;
        final IMultiBlockController controller = getTarget(true);
        return controller != null ? controller.getStackInSlot(this, aSlot) : null;
    }

    @Override
    public ItemStack decrStackSize(int aSlot, int aDecrement) {
        if (!modeSelected(ITEM_OUT)) return null;
        final IMultiBlockController controller = getTarget(true);
        return controller != null ? controller.decrStackSize(this, aSlot, aDecrement) : null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int aSlot) {
        final IMultiBlockController controller = getTarget(true);
        return controller != null ? controller.getStackInSlotOnClosing(this, aSlot) : null;
    }

    @Override
    public void setInventorySlotContents(int aSlot, ItemStack aStack) {
        final IMultiBlockController controller = getTarget(true);
        if(controller != null) controller.setInventorySlotContents(this, aSlot, aStack);
    }

    @Override
    public String getInventoryName() {
        final IMultiBlockController controller = getTarget(true);
        if(controller != null)
            return controller.getInventoryName(this);
        return firstNonNull(getCustomName(), getTileEntityName());
    }

    @Override
    public int getInventoryStackLimit() {
        final IMultiBlockController controller = getTarget(true);
        return controller != null ? controller.getInventoryStackLimit(this) : 0;
    }


    @Override
    public boolean isItemValidForSlot(int aSlot, ItemStack aStack) {
        final IMultiBlockController controller = getTarget(true);
        return controller != null && controller.isItemValidForSlot(this, aSlot, aStack);
    }

    // End Inventory

}
