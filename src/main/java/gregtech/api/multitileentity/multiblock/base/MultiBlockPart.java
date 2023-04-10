package gregtech.api.multitileentity.multiblock.base;

import static com.google.common.math.LongMath.log2;
import static gregtech.api.enums.GT_Values.ALL_VALID_SIDES;
import static gregtech.api.enums.GT_Values.B;
import static gregtech.api.enums.GT_Values.NBT;
import static gregtech.api.enums.GT_Values.SIDE_UNKNOWN;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import net.minecraftforge.fluids.IFluidTank;

import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.ModularWindow.Builder;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.DropDownWidget;
import com.gtnewhorizons.modularui.common.widget.FluidSlotWidget;
import com.gtnewhorizons.modularui.common.widget.Scrollable;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.logic.PowerLogic;
import gregtech.api.logic.interfaces.PowerLogicHost;
import gregtech.api.multitileentity.MultiTileEntityRegistry;
import gregtech.api.multitileentity.base.NonTickableMultiTileEntity;
import gregtech.api.multitileentity.interfaces.IMultiBlockController;
import gregtech.api.multitileentity.interfaces.IMultiBlockPart;
import gregtech.api.multitileentity.interfaces.IMultiTileEntity.IMTE_BreakBlock;
import gregtech.api.multitileentity.interfaces.IMultiTileEntity.IMTE_HasModes;
import gregtech.api.net.GT_Packet_MultiTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Utility;
import gregtech.common.covers.CoverInfo;

public abstract class MultiBlockPart extends NonTickableMultiTileEntity
    implements IMultiBlockPart, IMTE_BreakBlock, IMTE_HasModes, PowerLogicHost {

    public static final int NOTHING = 0, ENERGY_IN = B[0], ENERGY_OUT = B[1], FLUID_IN = B[2], FLUID_OUT = B[3],
        ITEM_IN = B[4], ITEM_OUT = B[5];

    protected final List<Integer> BASIC_MODES = new ArrayList<>(
        Arrays.asList(NOTHING, ENERGY_IN, ENERGY_OUT, FLUID_IN, FLUID_OUT, ITEM_IN, ITEM_OUT));

    protected ChunkCoordinates mTargetPos = null;
    protected IMultiBlockController target = null;

    protected int mAllowedModes = NOTHING; // BITMASK - Modes allowed for this part
    protected byte mMode = 0; // Mode selected for this part

    protected String mLockedInventory = GT_Values.E;
    protected int mLockedInventoryIndex = 0;

    /**
     * What Part Tier is this part? All Basic Casings are Tier 1, and will allow: Energy, Item, Fluid input/output. Some
     * of the more advanced modes can be set to require a higher tier part.
     */
    public int getPartTier() {
        return 1;
    }

    public String getLockedInventory() {
        issueClientUpdate();
        IMultiBlockController controller = getTarget(false);
        if (!getNameOfInventoryFromIndex(controller, mLockedInventoryIndex).equals(mLockedInventory)) {
            mLockedInventory = getNameOfInventoryFromIndex(controller, mLockedInventoryIndex);
            if (mLockedInventory.equals("all")) {
                mLockedInventory = "";
            }
        }
        return mLockedInventory.equals("") ? null : mLockedInventory;
    }

    public void setTarget(IMultiBlockController aTarget, int aAllowedModes) {
        target = aTarget;
        mTargetPos = (target == null ? null : target.getCoords());
        mAllowedModes = aAllowedModes;
        if (target != null) registerCovers(target);
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
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);
        currenttip.add(String.format("Mode: %s", getModeName(mMode)));
    }

    public IMultiBlockController getTarget(boolean aCheckValidity) {
        if (mTargetPos == null) return null;
        if (target == null || target.isDead()) {
            if (worldObj.blockExists(mTargetPos.posX, mTargetPos.posY, mTargetPos.posZ)) {
                final TileEntity te = worldObj.getTileEntity(mTargetPos.posX, mTargetPos.posY, mTargetPos.posZ);
                if (te instanceof IMultiBlockController) {
                    target = (IMultiBlockController) te;
                    // Register our covers with the controller
                    registerCovers(target);
                } else {
                    mTargetPos = null;
                }
            }
        }
        if (aCheckValidity) {
            return target != null && target.checkStructure(false) ? target : null;
        }
        return target;
    }

    public void registerCovers(IMultiBlockController controller) {
        for (byte i : ALL_VALID_SIDES) {
            final CoverInfo coverInfo = getCoverInfoAtSide(i);
            if (coverInfo.isValid() && coverInfo.getTickRate() > 0) {
                controller.registerCoveredPartOnSide(i, this);
            }
        }
    }

    @Override
    public void setCoverItemAtSide(byte aSide, ItemStack aCover) {
        super.setCoverItemAtSide(aSide, aCover);
        // TODO: Filter on tickable covers
        final IMultiBlockController tTarget = getTarget(true);
        if (tTarget != null) {
            final CoverInfo coverInfo = getCoverInfoAtSide(aSide);
            if (coverInfo.isValid() && coverInfo.getTickRate() > 0) {
                tTarget.registerCoveredPartOnSide(aSide, this);
            }
        }
    }

    public void unregisterCovers(IMultiBlockController controller) {
        for (byte i : ALL_VALID_SIDES) {
            if (getCoverInfoAtSide(i).isValid()) {
                controller.unregisterCoveredPartOnSide(i, this);
            }
        }
    }

    @Override
    public boolean dropCover(byte aSide, byte aDroppedSide, boolean aForced) {
        final boolean res = super.dropCover(aSide, aDroppedSide, aForced);
        final IMultiBlockController tTarget = getTarget(true);
        if (tTarget != null) {
            tTarget.unregisterCoveredPartOnSide(aSide, this);
        }
        return res;
    }

    @Override
    public void readMultiTileNBT(NBTTagCompound aNBT) {
        if (aNBT.hasKey(NBT.ALLOWED_MODES)) mAllowedModes = aNBT.getInteger(NBT.ALLOWED_MODES);
        if (aNBT.hasKey(NBT.MODE)) mMode = aNBT.getByte(NBT.MODE);
        if (aNBT.hasKey(NBT.TARGET)) {
            mTargetPos = new ChunkCoordinates(
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
    }

    @Override
    public void writeMultiTileNBT(NBTTagCompound aNBT) {
        if (mAllowedModes != NOTHING) aNBT.setInteger(NBT.ALLOWED_MODES, mAllowedModes);
        if (mMode != 0) aNBT.setInteger(NBT.MODE, mMode);
        if (mTargetPos != null) {
            aNBT.setBoolean(NBT.TARGET, true);
            aNBT.setInteger(NBT.TARGET_X, mTargetPos.posX);
            aNBT.setShort(NBT.TARGET_Y, (short) mTargetPos.posY);
            aNBT.setInteger(NBT.TARGET_Z, mTargetPos.posZ);
        }
        if (mLockedInventory != null) {
            aNBT.setString(NBT.LOCKED_INVENTORY, mLockedInventory);
        }
        if (mLockedInventoryIndex != 0) {
            aNBT.setInteger(NBT.LOCKED_INVENTORY_INDEX, mLockedInventoryIndex);
        }
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
        mTargetPos = aTargetPos;
        IMultiBlockController mTarget = getTarget(false);
        setTarget(mTarget, mAllowedModes);
    }

    @Override
    public ChunkCoordinates getTargetPos() {
        return mTargetPos;
    }

    @Override
    public void setMode(byte aMode) {
        mMode = aMode;
    }

    @Override
    public byte getMode() {
        return mMode;
    }

    @Override
    public int getAllowedModes() {
        return mAllowedModes;
    }

    @Override
    public void setAllowedModes(int aAllowedModes) {
        mAllowedModes = aAllowedModes;
    }

    /**
     * True if `aMode` is one of the allowed modes
     */
    public boolean hasMode(int aMode) {
        // This is not sent to the client
        return (mAllowedModes & aMode) != 0;
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
        for (byte tSide : ALL_VALID_SIDES) {
            final TileEntity te = getTileEntityAtSide(tSide);
            if (te instanceof MultiBlockPart) {
                final IMultiBlockController tController = ((MultiBlockPart) te).getTarget(false);
                if (tController != null) tController.onStructureChange();
            } else if (te instanceof IMultiBlockController) {
                ((IMultiBlockController) te).onStructureChange();
            }
        }
    }

    @Override
    public void loadTextureNBT(NBTTagCompound aNBT) {
        // Loading the registry
        final String textureName = aNBT.getString(NBT.TEXTURE);
        textures = new IIconContainer[] {
            new Textures.BlockIcons.CustomIcon("multitileentity/multiblockparts/" + textureName + "/bottom"),
            new Textures.BlockIcons.CustomIcon("multitileentity/multiblockparts/" + textureName + "/top"),
            new Textures.BlockIcons.CustomIcon("multitileentity/multiblockparts/" + textureName + "/side"),
            new Textures.BlockIcons.CustomIcon("multitileentity/multiblockparts/" + textureName + "/overlay/bottom"),
            new Textures.BlockIcons.CustomIcon("multitileentity/multiblockparts/" + textureName + "/overlay/top"),
            new Textures.BlockIcons.CustomIcon("multitileentity/multiblockparts/" + textureName + "/overlay/side") };
    }

    @Override
    public void copyTextures() {
        // Loading an instance
        final TileEntity tCanonicalTileEntity = MultiTileEntityRegistry
            .getCanonicalTileEntity(getMultiTileEntityRegistryID(), getMultiTileEntityID());
        if (tCanonicalTileEntity instanceof MultiBlockPart) textures = ((MultiBlockPart) tCanonicalTileEntity).textures;
    }

    @Override
    public ITexture[] getTexture(Block aBlock, byte aSide, boolean isActive, int aRenderPass) {
        // For normal parts - texture comes from BaseMTE; overlay based on current mode
        // TODO(MTE) - For Advanced parts they might come from somewhere else
        final ITexture baseTexture = TextureFactory.of(super.getTexture(aBlock, aSide, isActive, aRenderPass));
        if (mMode != 0 && aSide == facing) {
            if (mMode == getModeOrdinal(ITEM_IN)) return new ITexture[] { baseTexture,
                TextureFactory.of(OVERLAY_PIPE_IN), TextureFactory.of(ITEM_IN_SIGN) };
            if (mMode == getModeOrdinal(ITEM_OUT)) return new ITexture[] { baseTexture,
                TextureFactory.of(OVERLAY_PIPE_OUT), TextureFactory.of(ITEM_OUT_SIGN) };
            if (mMode == getModeOrdinal(FLUID_IN)) return new ITexture[] { baseTexture,
                TextureFactory.of(OVERLAY_PIPE_IN), TextureFactory.of(FLUID_IN_SIGN) };
            if (mMode == getModeOrdinal(FLUID_OUT)) return new ITexture[] { baseTexture,
                TextureFactory.of(OVERLAY_PIPE_OUT), TextureFactory.of(FLUID_OUT_SIGN) };
            if (mMode == getModeOrdinal(ENERGY_IN))
                return new ITexture[] { baseTexture, TextureFactory.of(OVERLAY_ENERGY_IN_MULTI) };
            if (mMode == getModeOrdinal(ENERGY_OUT))
                return new ITexture[] { baseTexture, TextureFactory.of(OVERLAY_ENERGY_OUT_MULTI) };
        }
        return new ITexture[] { baseTexture };
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
        if (mAllowedModes == NOTHING) return NOTHING;

        final int numModes = allowedModes.size();
        for (byte i = 1; i <= numModes; i++) {
            final byte curMode = (byte) ((mMode + i) % numModes);
            if (curMode == NOTHING || hasMode(1 << (curMode - 1))) return curMode;
        }
        // Nothing valid found
        return 0;
    }

    @Override
    public boolean onMalletRightClick(EntityPlayer aPlayer, ItemStack tCurrentItem, byte wrenchSide, float aX, float aY,
        float aZ) {
        if (mAllowedModes == NOTHING) return true;

        mMode = getNextAllowedMode(BASIC_MODES);
        GT_Utility.sendChatToPlayer(aPlayer, "Mode set to `" + getModeName(mMode) + "' (" + mMode + ")");
        sendClientData((EntityPlayerMP) aPlayer);
        return true;
    }

    @Override
    public void setLightValue(byte aLightValue) {}

    @Override
    public byte getComparatorValue(byte aSide) {
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
        final byte aSide = (byte) aDirection.ordinal();
        if (aDirection != ForgeDirection.UNKNOWN
            && (aSide != facing || !coverLetsFluidIn(aSide, aFluidStack == null ? null : aFluidStack.getFluid())))
            return 0;
        final IMultiBlockController controller = getTarget(true);
        return controller == null ? 0 : controller.fill(this, aDirection, aFluidStack, aDoFill);
    }

    @Override
    public FluidStack drain(ForgeDirection aDirection, FluidStack aFluidStack, boolean aDoDrain) {
        if (!modeSelected(FLUID_OUT)) return null;
        final byte aSide = (byte) aDirection.ordinal();
        if (aDirection != ForgeDirection.UNKNOWN
            && (aSide != facing || !coverLetsFluidOut(aSide, aFluidStack == null ? null : aFluidStack.getFluid())))
            return null;
        final IMultiBlockController controller = getTarget(true);
        return controller == null ? null : controller.drain(this, aDirection, aFluidStack, aDoDrain);
    }

    @Override
    public FluidStack drain(ForgeDirection aDirection, int aAmountToDrain, boolean aDoDrain) {
        if (!modeSelected(FLUID_OUT)) return null;
        final byte aSide = (byte) aDirection.ordinal();
        final IMultiBlockController controller = getTarget(true);
        if (controller == null) return null;
        final FluidStack aFluidStack = controller.getDrainableFluid(aSide);
        if (aDirection != ForgeDirection.UNKNOWN
            && (aSide != facing || !coverLetsFluidOut(aSide, aFluidStack == null ? null : aFluidStack.getFluid())))
            return null;
        return controller.drain(this, aDirection, aAmountToDrain, aDoDrain);
    }

    @Override
    public boolean canFill(ForgeDirection aDirection, Fluid aFluid) {
        if (!modeSelected(FLUID_IN)) return false;
        final byte aSide = (byte) aDirection.ordinal();
        if (aDirection != ForgeDirection.UNKNOWN && (aSide != facing || !coverLetsFluidIn(aSide, aFluid))) return false;
        final IMultiBlockController controller = getTarget(true);
        return controller != null && controller.canFill(this, aDirection, aFluid);
    }

    @Override
    public boolean canDrain(ForgeDirection aDirection, Fluid aFluid) {
        if (!modeSelected(FLUID_OUT)) return false;
        final byte aSide = (byte) aDirection.ordinal();
        if (aDirection != ForgeDirection.UNKNOWN && (aSide != facing || !coverLetsFluidOut(aSide, aFluid)))
            return false;
        final IMultiBlockController controller = getTarget(true);
        return controller != null && controller.canDrain(this, aDirection, aFluid);
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection aDirection) {
        final byte aSide = (byte) aDirection.ordinal();
        if (!modeSelected(FLUID_IN, FLUID_OUT) || (aSide != SIDE_UNKNOWN && aSide != facing))
            return GT_Values.emptyFluidTankInfo;
        final IMultiBlockController controller = getTarget(true);
        if (controller == null) return GT_Values.emptyFluidTankInfo;

        final CoverInfo coverInfo = getCoverInfoAtSide(aSide);

        if ((controller.isLiquidInput(aSide) && coverInfo.letsFluidIn(null, controller))
            || (controller.isLiquidOutput(aSide) && coverInfo.letsFluidOut(null, controller)))
            return controller.getTankInfo(this, aDirection);

        return GT_Values.emptyFluidTankInfo;
    }

    // #region Energy - Depending on the part type - proxy to the multiblock controller, if we have one

    @Override
    public PowerLogic getPowerLogic(byte side) {
        final IMultiBlockController controller = getTarget(true);
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
    public int[] getAccessibleSlotsFromSide(int aSide) {
        if (!modeSelected(ITEM_IN, ITEM_OUT) || (facing != SIDE_UNKNOWN && facing != aSide))
            return GT_Values.emptyIntArray;
        final IMultiBlockController controller = getTarget(true);
        return controller != null ? controller.getAccessibleSlotsFromSide(this, (byte) aSide) : GT_Values.emptyIntArray;
    }

    @Override
    public boolean canInsertItem(int aSlot, ItemStack aStack, int aSide) {
        if (!modeSelected(ITEM_IN, ITEM_OUT)
            || (facing != SIDE_UNKNOWN && (facing != aSide || !coverLetsItemsIn((byte) aSide, aSlot)))) return false;
        final IMultiBlockController controller = getTarget(true);
        return (controller != null && controller.canInsertItem(this, aSlot, aStack, (byte) aSide));
    }

    @Override
    public boolean canExtractItem(int aSlot, ItemStack aStack, int aSide) {
        if (!modeSelected(ITEM_IN, ITEM_OUT)
            || (facing != SIDE_UNKNOWN && (facing != aSide || !coverLetsItemsOut((byte) aSide, aSlot)))) return false;
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
    public boolean hasGui(byte aSide) {
        // UIs only for specific mode(s)
        if (modeSelected(ITEM_IN, ITEM_OUT, FLUID_IN, FLUID_OUT)) return true;

        return false;
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
                if (getNameOfInventoryFromIndex(controller, index).equals("all")) {
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

    protected void addFluidInventory(Builder builder, UIBuildContext buildContext) {
        final IMultiBlockController controller = getTarget(false);
        if (controller == null) {
            return;
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
                .setPos(52, 7));
    }

    @Override
    public void addUIWidgets(Builder builder, UIBuildContext buildContext) {
        if (modeSelected(ITEM_IN, ITEM_OUT)) {
            addItemInventory(builder, buildContext);
        }
        if (modeSelected(FLUID_IN, FLUID_OUT)) {
            addFluidInventory(builder, buildContext);
        }
    }

    @Override
    public ModularWindow createWindow(UIBuildContext buildContext) {
        if (isServerSide()) {
            issueClientUpdate();
        }
        System.out.println("MultiBlockPart::createWindow");
        return super.createWindow(buildContext);
    }

    @Override
    protected int getGUIHeight() {
        if (modeSelected(ITEM_IN, ITEM_OUT)) {
            return super.getGUIHeight() + 11;
        }
        return super.getGUIHeight();
    }

    @Override
    public void addGregTechLogo(Builder builder) {
        if (modeSelected(ITEM_IN, ITEM_OUT)) {
            builder.widget(
                new DrawableWidget().setDrawable(getGUITextureSet().getGregTechLogo())
                    .setSize(17, 17)
                    .setPos(152, 74));
        } else {
            super.addGregTechLogo(builder);
        }
    }
}
