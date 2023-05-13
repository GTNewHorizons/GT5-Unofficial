package gregtech.api.multitileentity.multiblock.base;

import static com.google.common.math.LongMath.log2;
import static gregtech.api.enums.GT_Values.B;
import static gregtech.api.enums.GT_Values.NBT;
import static gregtech.api.enums.Textures.BlockIcons.FLUID_IN_SIGN;
import static gregtech.api.enums.Textures.BlockIcons.FLUID_OUT_SIGN;
import static gregtech.api.enums.Textures.BlockIcons.ITEM_IN_SIGN;
import static gregtech.api.enums.Textures.BlockIcons.ITEM_OUT_SIGN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ENERGY_IN_MULTI;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ENERGY_OUT_MULTI;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE_IN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE_OUT;
import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

import java.math.RoundingMode;
import java.util.*;

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
import net.minecraftforge.fluids.IFluidTank;

import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.ModularWindow.Builder;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.*;

import gregtech.api.enums.GT_Values;
import gregtech.api.fluid.FluidTankGT;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.logic.PowerLogic;
import gregtech.api.logic.interfaces.PowerLogicHost;
import gregtech.api.multitileentity.base.NonTickableMultiTileEntity;
import gregtech.api.multitileentity.enums.MultiTileCasingPurpose;
import gregtech.api.multitileentity.interfaces.IMultiBlockController;
import gregtech.api.multitileentity.interfaces.IMultiBlockPart;
import gregtech.api.multitileentity.interfaces.IMultiTileEntity.IMTE_BreakBlock;
import gregtech.api.multitileentity.interfaces.IMultiTileEntity.IMTE_HasModes;
import gregtech.api.net.GT_Packet_MultiTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Utility;
import gregtech.common.covers.CoverInfo;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public abstract class MultiBlockPart extends NonTickableMultiTileEntity
    implements IMultiBlockPart, IMTE_BreakBlock, IMTE_HasModes, PowerLogicHost {

    public static final int NOTHING = 0, ENERGY_IN = B[0], ENERGY_OUT = B[1], FLUID_IN = B[2], FLUID_OUT = B[3],
        ITEM_IN = B[4], ITEM_OUT = B[5];

    protected final List<Integer> BASIC_MODES = new ArrayList<>(
        Arrays.asList(NOTHING, ENERGY_IN, ENERGY_OUT, FLUID_IN, FLUID_OUT, ITEM_IN, ITEM_OUT));

    protected Set<MultiTileCasingPurpose> registeredPurposes = new HashSet<>();

    protected ChunkCoordinates targetPosition = null;

    protected int allowedModes = NOTHING; // BITMASK - Modes allowed for this part
    protected byte mMode = 0; // Mode selected for this part

    protected String mLockedInventory = GT_Values.E;
    protected int mLockedInventoryIndex = 0;
    protected FluidTankGT configurationTank = new FluidTankGT();

    /**
     * What Part Tier is this part? All Basic Casings are Tier 1, and will allow: Energy, Item, Fluid input/output. Some
     * of the more advanced modes can be set to require a higher tier part.
     */
    public int getPartTier() {
        return 1;
    }

    public String getLockedInventory() {
        // TODO: Can this cause side-effects? Removed for now because it causes huge network traffic when using covers
        // issueClientUpdate();
        IMultiBlockController controller = getTarget(false);
        if (modeSelected(ITEM_IN) || modeSelected(ITEM_OUT)) {
            if (!getNameOfInventoryFromIndex(controller, mLockedInventoryIndex).equals(mLockedInventory)) {
                mLockedInventory = getNameOfInventoryFromIndex(controller, mLockedInventoryIndex);
                if (mLockedInventory.equals(Controller.ALL_INVENTORIES_NAME)) {
                    mLockedInventory = "";
                }
            }
        } else {
            if (!getNameOfTankArrayFromIndex(controller, mLockedInventoryIndex).equals(mLockedInventory)) {
                mLockedInventory = getNameOfTankArrayFromIndex(controller, mLockedInventoryIndex);
                if (mLockedInventory.equals(Controller.ALL_INVENTORIES_NAME)) {
                    mLockedInventory = "";
                }
            }
        }
        return mLockedInventory.equals("") ? null : mLockedInventory;
    }

    public void setTarget(IMultiBlockController newTarget, int aAllowedModes) {
        IMultiBlockController currentTarget = getTarget(false);
        if (currentTarget != null && currentTarget != newTarget) {
            for (MultiTileCasingPurpose purpose : registeredPurposes) {
                unregisterPurpose(purpose);
            }
        }
        targetPosition = (newTarget == null ? null : newTarget.getCoords());
        allowedModes = aAllowedModes;
        if (newTarget != null) {
            registerCovers(newTarget);
            registerPurposes();
        }
    }

    protected void registerPurpose(MultiTileCasingPurpose purpose) {
        IMultiBlockController target = getTarget(false);
        if (target != null) {
            target.registerCaseWithPurpose(purpose, this);
            registeredPurposes.add(purpose);
        }
    }

    protected void unregisterPurpose(MultiTileCasingPurpose purpose) {
        IMultiBlockController target = getTarget(false);
        if (target != null) {
            target.unregisterCaseWithPurpose(purpose, this);
        }
        registeredPurposes.remove(purpose);
    }

    @Override
    protected void addDebugInfo(EntityPlayer aPlayer, int aLogLevel, ArrayList<String> tList) {
        final IMultiBlockController controller = getTarget(false);
        if (controller != null) {
            tList.add("Has controller");
        } else {
            tList.add("No Controller");
        }
        tList.add("Casing Mode: " + getModeName(mMode));
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        currentTip.add(String.format("Mode: %s", getModeName(mMode)));
        if (modeSelected(FLUID_OUT)) {
            if (configurationTank != null && configurationTank.get() != null) {
                currentTip.add(
                    String.format(
                        "Locked to: %s",
                        configurationTank.get()
                            .getLocalizedName()));
            } else {
                currentTip.add("Locked to: Nothing");
            }
        }
    }

    public IMultiBlockController getTarget(boolean aCheckValidity) {
        if (targetPosition == null) {
            return null;
        }

        if (!worldObj.blockExists(targetPosition.posX, targetPosition.posY, targetPosition.posZ)) {
            return null;
        } 
        final TileEntity te = worldObj.getTileEntity(targetPosition.posX, targetPosition.posY, targetPosition.posZ);
        IMultiBlockController target = null;
        if (te instanceof IMultiBlockController targetFound) {
            target = targetFound;
        } else {
            targetPosition = null;
            return null;
        }
    
        if (aCheckValidity) {
            return target != null && target.checkStructure(false) ? target : null;
        }
        return target;
    }

    public void registerCovers(IMultiBlockController controller) {
        for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            final CoverInfo coverInfo = getCoverInfoAtSide(side);
            if (coverInfo.isValid() && coverInfo.getTickRate() > 0) {
                controller.registerCoveredPartOnSide(side, this);
            }
        }
    }

    protected void registerPurposes() {
        for (MultiTileCasingPurpose purpose : registeredPurposes) {
            registerPurpose(purpose);
        }
    }

    @Override
    public void setCoverItemAtSide(ForgeDirection side, ItemStack aCover) {
        super.setCoverItemAtSide(side, aCover);
        // TODO: Filter on tickable covers
        final IMultiBlockController tTarget = getTarget(true);
        if (tTarget != null) {
            final CoverInfo coverInfo = getCoverInfoAtSide(side);
            if (coverInfo.isValid() && coverInfo.getTickRate() > 0) {
                tTarget.registerCoveredPartOnSide(side, this);
            }
        }
    }

    public void unregisterCovers(IMultiBlockController controller) {
        for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            if (getCoverInfoAtSide(side).isValid()) {
                controller.unregisterCoveredPartOnSide(side, this);
            }
        }
    }

    @Override
    public boolean dropCover(ForgeDirection side, ForgeDirection droppedSide, boolean aForced) {
        final boolean res = super.dropCover(side, droppedSide, aForced);
        final IMultiBlockController tTarget = getTarget(true);
        if (tTarget != null) {
            tTarget.unregisterCoveredPartOnSide(side, this);
        }
        return res;
    }

    @Override
    public void readMultiTileNBT(NBTTagCompound aNBT) {
        if (aNBT.hasKey(NBT.ALLOWED_MODES)) allowedModes = aNBT.getInteger(NBT.ALLOWED_MODES);
        if (aNBT.hasKey(NBT.MODE)) setMode(aNBT.getByte(NBT.MODE));
        if (aNBT.hasKey(NBT.TARGET)) {
            targetPosition = new ChunkCoordinates(
                aNBT.getInteger(NBT.TARGET_X),
                aNBT.getShort(NBT.TARGET_Y),
                aNBT.getInteger(NBT.TARGET_Z));
        }
        if (aNBT.hasKey(NBT.LOCKED_INVENTORY)) {
            mLockedInventory = aNBT.getString(NBT.LOCKED_INVENTORY);
        }
        if (aNBT.hasKey(NBT.LOCKED_INVENTORY_INDEX)) {
            mLockedInventoryIndex = aNBT.getInteger(NBT.LOCKED_INVENTORY_INDEX);
        }
        if (aNBT.hasKey(NBT.LOCKED_FLUID)) {
            configurationTank.readFromNBT(aNBT, NBT.LOCKED_FLUID);
        }
        if (modeSelected(ITEM_OUT)) {
            registeredPurposes.add(MultiTileCasingPurpose.ItemOutput);
        }
        if (modeSelected(FLUID_OUT)) {
            registeredPurposes.add(MultiTileCasingPurpose.FluidOutput);
        }
    }

    @Override
    public void writeMultiTileNBT(NBTTagCompound aNBT) {
        if (allowedModes != NOTHING) aNBT.setInteger(NBT.ALLOWED_MODES, allowedModes);
        if (mMode != 0) aNBT.setInteger(NBT.MODE, mMode);
        if (targetPosition != null) {
            aNBT.setBoolean(NBT.TARGET, true);
            aNBT.setInteger(NBT.TARGET_X, targetPosition.posX);
            aNBT.setShort(NBT.TARGET_Y, (short) targetPosition.posY);
            aNBT.setInteger(NBT.TARGET_Z, targetPosition.posZ);
        }
        if (mLockedInventory != null) {
            aNBT.setString(NBT.LOCKED_INVENTORY, mLockedInventory);
        }
        if (mLockedInventoryIndex != 0) {
            aNBT.setInteger(NBT.LOCKED_INVENTORY_INDEX, mLockedInventoryIndex);
        }
        configurationTank.writeToNBT(aNBT, NBT.LOCKED_FLUID);
    }

    @Override
    public GT_Packet_MultiTileEntity getClientDataPacket() {
        final GT_Packet_MultiTileEntity packet = super.getClientDataPacket();
        packet.setModes(getMode(), getAllowedModes());
        if (getTargetPos() != null) {
            final ChunkCoordinates aTarget = getTargetPos();
            packet.setTargetPos(aTarget.posX, (short) aTarget.posY, aTarget.posZ);
        }
        packet.setInventoryIndex(getLockedInventoryIndex());
        return packet;
    }

    @Override
    public void setLockedInventoryIndex(int aIndex) {
        mLockedInventoryIndex = aIndex;
    }

    @Override
    public int getLockedInventoryIndex() {
        return mLockedInventoryIndex;
    }

    @Override
    public void setTargetPos(ChunkCoordinates aTargetPos) {
        targetPosition = aTargetPos;
        IMultiBlockController mTarget = getTarget(false);
        setTarget(mTarget, allowedModes);
    }

    @Override
    public ChunkCoordinates getTargetPos() {
        return targetPosition;
    }

    @Override
    public void setMode(byte aMode) {
        if (aMode == mMode) return;
        if (modeSelected(FLUID_OUT)) {
            unregisterPurpose(MultiTileCasingPurpose.FluidOutput);
        }
        if (modeSelected(ITEM_OUT)) {
            unregisterPurpose(MultiTileCasingPurpose.ItemOutput);
        }
        mMode = aMode;
        if (modeSelected(FLUID_OUT)) {
            registerPurpose(MultiTileCasingPurpose.FluidOutput);
        }
        if (modeSelected(ITEM_OUT)) {
            registerPurpose(MultiTileCasingPurpose.ItemOutput);
        }
    }

    @Override
    public byte getMode() {
        return mMode;
    }

    @Override
    public int getAllowedModes() {
        return allowedModes;
    }

    @Override
    public void setAllowedModes(int aAllowedModes) {
        allowedModes = aAllowedModes;
    }

    /**
     * True if `aMode` is one of the allowed modes
     */
    public boolean hasMode(int aMode) {
        // This is not sent to the client
        return (allowedModes & aMode) != 0;
    }

    /**
     * Returns true if the part has any of the modes provided, and that mode is the currently selected mode
     */
    public boolean modeSelected(int... aModes) {
        for (int aMode : aModes) {
            if (hasMode(aMode) && mMode == getModeOrdinal(aMode)) return true;
        }
        return false;
    }

    @Override
    public boolean breakBlock() {
        final IMultiBlockController tTarget = getTarget(false);
        if (tTarget != null) {
            unregisterCovers(tTarget);
            tTarget.onStructureChange();
        }
        return false;
    }

    @Override
    public void onBlockAdded() {
        for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            final TileEntity te = getTileEntityAtSide(side);
            if (te instanceof MultiBlockPart part) {
                final IMultiBlockController tController = part.getTarget(false);
                if (tController != null) tController.onStructureChange();
            } else if (te instanceof IMultiBlockController controller) {
                controller.onStructureChange();
            }
        }
    }

    @Override
    public ITexture getTexture(ForgeDirection side) {
        ITexture texture = super.getTexture(side);
        if (mMode != 0 && side == facing) {
            if (mMode == getModeOrdinal(ITEM_IN)) {
                return TextureFactory.of(
                    texture,
                    TextureFactory.of(OVERLAY_PIPE_IN),
                    TextureFactory.of(ITEM_IN_SIGN),
                    getCoverTexture(side));
            }
            if (mMode == getModeOrdinal(ITEM_OUT)) {
                return TextureFactory.of(
                    texture,
                    TextureFactory.of(OVERLAY_PIPE_OUT),
                    TextureFactory.of(ITEM_OUT_SIGN),
                    getCoverTexture(side));
            }
            if (mMode == getModeOrdinal(FLUID_IN)) {
                return TextureFactory.of(
                    texture,
                    TextureFactory.of(OVERLAY_PIPE_IN),
                    TextureFactory.of(FLUID_IN_SIGN),
                    getCoverTexture(side));
            }
            if (mMode == getModeOrdinal(FLUID_OUT)) {
                return TextureFactory.of(
                    texture,
                    TextureFactory.of(OVERLAY_PIPE_OUT),
                    TextureFactory.of(FLUID_OUT_SIGN),
                    getCoverTexture(side));
            }
            if (mMode == getModeOrdinal(ENERGY_IN)) {
                return TextureFactory.of(texture, TextureFactory.of(OVERLAY_ENERGY_IN_MULTI), getCoverTexture(side));
            }
            if (mMode == getModeOrdinal(ENERGY_OUT)) {
                return TextureFactory.of(texture, TextureFactory.of(OVERLAY_ENERGY_OUT_MULTI), getCoverTexture(side));
            }
        }

        return TextureFactory.of(texture, getCoverTexture(side));
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return false;
    }

    protected String getModeName(int aMode) {
        if (aMode == NOTHING) return "Nothing";
        if (aMode == getModeOrdinal(ITEM_IN)) return "Item Input";
        if (aMode == getModeOrdinal(ITEM_OUT)) return "Item Output";
        if (aMode == getModeOrdinal(FLUID_IN)) return "Fluid Input";
        if (aMode == getModeOrdinal(FLUID_OUT)) return "Fluid Output";
        if (aMode == getModeOrdinal(ENERGY_IN)) return "Energy Input";
        if (aMode == getModeOrdinal(ENERGY_OUT)) return "Energy Output";
        return "Unknown";
    }

    protected byte getModeOrdinal(int aMode) {
        // log2 returns the bit position of the only bit set, add 1 to account for 0 being NOTHING
        // NOTE: Must be a power of 2 (single bit)
        return (byte) (log2(aMode, RoundingMode.UNNECESSARY) + 1);
    }

    protected byte getNextAllowedMode(List<Integer> allowedModes) {
        if (this.allowedModes == NOTHING) return NOTHING;

        final int numModes = allowedModes.size();
        for (byte i = 1; i <= numModes; i++) {
            final byte curMode = (byte) ((mMode + i) % numModes);
            if (curMode == NOTHING || hasMode(1 << (curMode - 1))) return curMode;
        }
        // Nothing valid found
        return 0;
    }

    @Override
    public boolean onMalletRightClick(EntityPlayer aPlayer, ItemStack tCurrentItem, ForgeDirection wrenchSide, float aX,
        float aY, float aZ) {
        if (allowedModes == NOTHING) return true;
        if (mMode == NOTHING) {
            facing = wrenchSide;
        }
        setMode(getNextAllowedMode(BASIC_MODES));
        if (aPlayer.isSneaking()) {
            facing = wrenchSide;
        }
        GT_Utility.sendChatToPlayer(aPlayer, "Mode set to `" + getModeName(mMode) + "' (" + mMode + ")");
        sendClientData((EntityPlayerMP) aPlayer);
        return true;
    }

    @Override
    public void setLightValue(byte aLightValue) {}

    @Override
    public byte getComparatorValue(ForgeDirection side) {
        return 0;
    }

    @Override
    public String getTileEntityName() {
        return "gt.multitileentity.multiblock.part";
    }

    /**
     * TODO: Make sure the energy/item/fluid hatch is facing that way! or has that mode enabled on that side Check
     * SIDE_UNKNOWN for or coverbehavior
     */

    /**
     * Fluid - Depending on the part type - proxy it to the multiblock controller, if we have one
     */
    @Override
    public int fill(ForgeDirection aDirection, FluidStack aFluidStack, boolean aDoFill) {
        if (!modeSelected(FLUID_IN)) return 0;

        if (aFluidStack == null || isWrongFluid(aFluidStack.getFluid())) return 0;
        if (aDirection != ForgeDirection.UNKNOWN
            && (facing.compareTo(aDirection) != 0 || !coverLetsFluidIn(aDirection, aFluidStack.getFluid()))) return 0;
        final IMultiBlockController controller = getTarget(true);
        return controller == null ? 0 : controller.fill(this, aDirection, aFluidStack, aDoFill);
    }

    @Override
    public FluidStack drain(ForgeDirection aDirection, FluidStack aFluidStack, boolean aDoDrain) {
        if (!modeSelected(FLUID_OUT)) return null;
        if (aFluidStack == null || isWrongFluid(aFluidStack.getFluid())) return null;
        if (aDirection != ForgeDirection.UNKNOWN
            && (facing.compareTo(aDirection) != 0 || !coverLetsFluidOut(aDirection, aFluidStack.getFluid())))
            return null;
        final IMultiBlockController controller = getTarget(true);
        return controller == null ? null : controller.drain(this, aDirection, aFluidStack, aDoDrain);
    }

    @Override
    public FluidStack drain(ForgeDirection aDirection, int aAmountToDrain, boolean aDoDrain) {
        if (!modeSelected(FLUID_OUT)) return null;
        final IMultiBlockController controller = getTarget(true);
        if (controller == null) return null;
        FluidStack aFluidStack = null;
        if (getLockedFluid() != null) {
            aFluidStack = controller.getDrainableFluid(aDirection, getLockedFluid());
        } else {
            aFluidStack = controller.getDrainableFluid(aDirection);
        }
        if (aFluidStack == null || isWrongFluid(aFluidStack.getFluid())) return null;
        if (aDirection != ForgeDirection.UNKNOWN
            && (facing.compareTo(aDirection) != 0 || !coverLetsFluidOut(aDirection, aFluidStack.getFluid())))
            return null;
        return controller.drain(this, aDirection, aFluidStack, aDoDrain);
    }

    @Override
    public boolean canFill(ForgeDirection aDirection, Fluid aFluid) {
        if (!modeSelected(FLUID_IN)) return false;

        if (aDirection != ForgeDirection.UNKNOWN
            && (facing.compareTo(aDirection) != 0 || !coverLetsFluidIn(aDirection, aFluid))) return false;
        if (isWrongFluid(aFluid)) return false;
        final IMultiBlockController controller = getTarget(true);
        return controller != null && controller.canFill(this, aDirection, aFluid);
    }

    @Override
    public boolean canDrain(ForgeDirection aDirection, Fluid aFluid) {
        if (!modeSelected(FLUID_OUT)) return false;
        if (aDirection != ForgeDirection.UNKNOWN
            && (facing.compareTo(aDirection) != 0 || !coverLetsFluidOut(aDirection, aFluid))) return false;
        if (isWrongFluid(aFluid)) return false;
        final IMultiBlockController controller = getTarget(true);
        return controller != null && controller.canDrain(this, aDirection, aFluid);
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection aDirection) {
        if (!modeSelected(FLUID_IN, FLUID_OUT)
            || (aDirection != ForgeDirection.UNKNOWN && facing.compareTo(aDirection) != 0))
            return GT_Values.emptyFluidTankInfo;
        final IMultiBlockController controller = getTarget(true);
        if (controller == null) return GT_Values.emptyFluidTankInfo;

        final CoverInfo coverInfo = getCoverInfoAtSide(aDirection);

        if ((controller.isLiquidInput(aDirection) && coverInfo.letsFluidIn(null, controller))
            || (controller.isLiquidOutput(aDirection) && coverInfo.letsFluidOut(null, controller)))
            return controller.getTankInfo(this, aDirection);

        return GT_Values.emptyFluidTankInfo;
    }

    @Override
    public boolean shouldTick(long tickTimer) {
        return modeSelected(ITEM_OUT, FLUID_OUT);
    }

    // #region Energy - Depending on the part type - proxy to the multiblock controller, if we have one

    @Override
    public PowerLogic getPowerLogic(ForgeDirection side) {
        if (facing != side) {
            return null;
        }

        if (!modeSelected(ENERGY_IN, ENERGY_OUT)) {
            return null;
        }

        final IMultiBlockController controller = getTarget(true);
        if (controller == null) {
            return null;
        }
        return controller.getPowerLogic(this, side);
    }

    @Override
    public boolean isEnetInput() {
        return modeSelected(ENERGY_IN);
    }

    @Override
    public boolean isEnetOutput() {
        return modeSelected(ENERGY_OUT);
    }

    // #endregion

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
        if (!modeSelected(ITEM_IN, ITEM_OUT)) return false;
        final IMultiBlockController controller = getTarget(true);
        return (controller != null && controller.addStackToSlot(this, aIndex, aStack));
    }

    @Override
    public boolean addStackToSlot(int aIndex, ItemStack aStack, int aAmount) {
        if (!modeSelected(ITEM_IN, ITEM_OUT)) return false;
        final IMultiBlockController controller = getTarget(true);
        return (controller != null && controller.addStackToSlot(this, aIndex, aStack, aAmount));
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int ordinalSide) {
        final ForgeDirection side = ForgeDirection.getOrientation(ordinalSide);
        if (!modeSelected(ITEM_IN, ITEM_OUT) || (facing != ForgeDirection.UNKNOWN && facing.compareTo(side) != 0))
            return GT_Values.emptyIntArray;
        final IMultiBlockController controller = getTarget(true);
        return controller != null ? controller.getAccessibleSlotsFromSide(this, side) : GT_Values.emptyIntArray;
    }

    @Override
    public boolean canInsertItem(int aSlot, ItemStack aStack, int ordinalSide) {
        final ForgeDirection side = ForgeDirection.getOrientation(ordinalSide);
        if (!modeSelected(ITEM_IN, ITEM_OUT)
            || (facing != ForgeDirection.UNKNOWN && (facing.compareTo(side) != 0 || !coverLetsItemsIn(side, aSlot))))
            return false;
        final IMultiBlockController controller = getTarget(true);
        return (controller != null && controller.canInsertItem(this, aSlot, aStack, side));
    }

    @Override
    public boolean canExtractItem(int aSlot, ItemStack aStack, int ordinalSide) {
        final ForgeDirection side = ForgeDirection.getOrientation(ordinalSide);
        if (!modeSelected(ITEM_IN, ITEM_OUT)
            || (facing != ForgeDirection.UNKNOWN && (facing.compareTo(side) != 0 || !coverLetsItemsOut(side, aSlot))))
            return false;
        final IMultiBlockController controller = getTarget(true);
        return (controller != null && controller.canExtractItem(this, aSlot, aStack, side));
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
        if (!modeSelected(ITEM_IN, ITEM_OUT)) return null;
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
        if (controller != null) controller.setInventorySlotContents(this, aSlot, aStack);
    }

    @Override
    public String getInventoryName() {
        final IMultiBlockController controller = getTarget(true);
        if (controller != null) return controller.getInventoryName(this);
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

    // === Modular UI ===
    @Override
    public boolean useModularUI() {
        return true;
    }

    @Override
    public String getLocalName() {
        if (modeSelected(ITEM_IN)) return "Input Inventory";
        if (modeSelected(ITEM_OUT)) return "Output Inventory";
        if (modeSelected(FLUID_IN)) return "Fluid Input Hatch";
        if (modeSelected(FLUID_OUT)) return "Fluid Output Hatch";

        return "Unknown";
    }

    @Override
    public boolean hasGui(ForgeDirection side) {
        if (modeSelected(ENERGY_IN, ENERGY_OUT) && facing == side) {
            return false;
        }
        return getTarget(true) != null;
    }

    protected void addItemInventory(Builder builder, UIBuildContext buildContext) {
        final IMultiBlockController controller = getTarget(false);
        if (controller == null) {
            return;
        }
        final IItemHandlerModifiable inv = controller.getInventoryForGUI(this);
        final Scrollable scrollable = new Scrollable().setVerticalScroll();
        for (int rows = 0; rows * 4 < Math.min(inv.getSlots(), 128); rows++) {
            int columnsToMake = Math.min(Math.min(inv.getSlots(), 128) - rows * 4, 4);
            for (int column = 0; column < columnsToMake; column++) {
                scrollable.widget(
                    new SlotWidget(inv, rows * 4 + column).setPos(column * 18, rows * 18)
                        .setSize(18, 18));
            }
        }
        builder.widget(
            scrollable.setSize(18 * 4 + 4, 18 * 4)
                .setPos(52, 18));
        DropDownWidget dropDown = new DropDownWidget();
        dropDown.addDropDownItemsSimple(
            controller.getInventoryNames(this),
            (buttonWidget, index, label, setSelected) -> buttonWidget.setOnClick((clickData, widget) -> {
                if (getNameOfInventoryFromIndex(controller, index).equals(Controller.ALL_INVENTORIES_NAME)) {
                    mLockedInventory = GT_Values.E;
                    mLockedInventoryIndex = 0;
                } else {
                    mLockedInventory = getNameOfInventoryFromIndex(controller, index);
                    mLockedInventoryIndex = index;
                }
                setSelected.run();
            }),
            true);
        builder.widget(
            dropDown.setSelected(mLockedInventoryIndex)
                .setExpandedMaxHeight(60)
                .setDirection(DropDownWidget.Direction.DOWN)
                .setPos(53, 5)
                .setSize(70, 11));
    }

    protected String getNameOfInventoryFromIndex(final IMultiBlockController controller, int index) {
        final List<String> invNames = controller.getInventoryIDs(this);
        if (index > invNames.size()) {
            return invNames.get(0);
        }
        return invNames.get(index);
    }

    protected String getNameOfTankArrayFromIndex(final IMultiBlockController controller, int index) {
        final List<String> tankNames = controller.getTankArrayIDs(this);
        if (index > tankNames.size()) {
            return tankNames.get(0);
        }
        return tankNames.get(index);
    }

    protected boolean isWrongFluid(Fluid fluid) {
        if (fluid == null) {
            return true;
        }
        Fluid lockedFluid = getLockedFluid();
        if (lockedFluid != null) {
            return !fluid.equals(lockedFluid);
        }
        return false;
    }

    protected Fluid getLockedFluid() {
        if (configurationTank.get() != null && configurationTank.get()
            .getFluid() != null) {
            return configurationTank.get()
                .getFluid();
        }
        return null;
    }

    protected void addFluidInventory(Builder builder, UIBuildContext buildContext) {
        final IMultiBlockController controller = getTarget(false);
        if (controller == null) {
            return;
        }
        builder.widget(
            new DrawableWidget().setDrawable(GT_UITextures.PICTURE_SCREEN_BLACK)
                .setPos(7, 4)
                .setSize(85, 95));
        if (modeSelected(FLUID_OUT)) {
            builder.widget(
                new DrawableWidget().setDrawable(GT_UITextures.PICTURE_SCREEN_BLACK)
                    .setPos(getGUIWidth() - 77, 4)
                    .setSize(70, 40))
                .widget(
                    new TextWidget("Locked Fluid").setDefaultColor(COLOR_TEXT_WHITE.get())
                        .setPos(getGUIWidth() - 72, 8));
        }
        final IFluidTank[] tanks = controller.getFluidTanksForGUI(this);
        final Scrollable scrollable = new Scrollable().setVerticalScroll();
        for (int rows = 0; rows * 4 < tanks.length; rows++) {
            int columnsToMake = Math.min(tanks.length - rows * 4, 4);
            for (int column = 0; column < columnsToMake; column++) {
                FluidSlotWidget fluidSlot = new FluidSlotWidget(tanks[rows * 4 + column]);
                if (modeSelected(FLUID_OUT)) {
                    fluidSlot.setInteraction(true, false);
                }
                scrollable.widget(
                    fluidSlot.setPos(column * 18, rows * 18)
                        .setSize(18, 18));
            }
        }
        builder.widget(
            scrollable.setSize(18 * 4 + 4, 18 * 4)
                .setPos(12, 21));
        DropDownWidget dropDown = new DropDownWidget();
        dropDown.addDropDownItemsSimple(
            controller.getTankArrayNames(this),
            (buttonWidget, index, label, setSelected) -> buttonWidget.setOnClick((clickData, widget) -> {
                if (getNameOfTankArrayFromIndex(controller, index).equals(Controller.ALL_INVENTORIES_NAME)) {
                    mLockedInventory = GT_Values.E;
                    mLockedInventoryIndex = 0;
                } else {
                    mLockedInventory = getNameOfTankArrayFromIndex(controller, index);
                    mLockedInventoryIndex = index;
                }
                setSelected.run();
            }),
            true);
        builder.widget(
            dropDown.setSelected(mLockedInventoryIndex)
                .setExpandedMaxHeight(60)
                .setDirection(DropDownWidget.Direction.DOWN)
                .setPos(13, 8)
                .setSize(70, 11));
    }

    @Override
    public void addUIWidgets(Builder builder, UIBuildContext buildContext) {
        if (modeSelected(ITEM_IN, ITEM_OUT)) {
            addItemInventory(builder, buildContext);
            return;
        }
        if (modeSelected(FLUID_IN, FLUID_OUT)) {
            addFluidInventory(builder, buildContext);
            if (modeSelected(FLUID_OUT)) {
                builder.widget(
                    SlotGroup.ofFluidTanks(Collections.singletonList(configurationTank), 1)
                        .startFromSlot(0)
                        .endAtSlot(0)
                        .phantom(true)
                        .build()
                        .setPos(getGUIWidth() - 72, 20));
            }
            return;
        }
    }

    @Override
    public ModularWindow createWindow(UIBuildContext buildContext) {
        if (isServerSide()) {
            issueClientUpdate();
        }
        System.out.println("MultiBlockPart::createWindow");
        if (modeSelected(NOTHING, ENERGY_IN, ENERGY_OUT) || mMode == NOTHING) {
            IMultiBlockController controller = getTarget(false);
            if (controller == null) {
                return super.createWindow(buildContext);
            }
            return controller.createWindowGUI(buildContext);
        }
        return super.createWindow(buildContext);
    }

    @Override
    protected int getGUIHeight() {
        return super.getGUIHeight() + 20;
    }

    @Override
    public void addGregTechLogo(Builder builder) {
        if (modeSelected(ITEM_IN, ITEM_OUT)) {
            builder.widget(
                new DrawableWidget().setDrawable(getGUITextureSet().getGregTechLogo())
                    .setSize(17, 17)
                    .setPos(152, 74));
        } else if (modeSelected(FLUID_IN, FLUID_OUT)) {
            builder.widget(
                new DrawableWidget().setDrawable(getGUITextureSet().getGregTechLogo())
                    .setSize(17, 17)
                    .setPos(152, 82));
        } else {
            super.addGregTechLogo(builder);
        }
    }
}
