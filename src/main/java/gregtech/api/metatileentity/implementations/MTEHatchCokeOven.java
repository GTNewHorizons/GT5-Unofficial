package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.Textures.BlockIcons.FLUID_OUT_SIGN;
import static gregtech.api.enums.Textures.BlockIcons.ITEM_IN_SIGN;
import static gregtech.api.enums.Textures.BlockIcons.ITEM_OUT_SIGN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE_IN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE_OUT;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.item.IItemHandlerModifiable;
import com.cleanroommc.modularui.utils.item.InvWrapper;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.gtnewhorizons.modularui.common.fluid.FluidStackTank;

import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.HarvestTool;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.hatch.MTEHatchCokeOvenGui;
import io.netty.buffer.ByteBuf;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTEHatchCokeOven extends MTEHatch {

    private enum Mode {

        Input((byte) 0),
        OutputItem((byte) 1),
        OutputFluid((byte) 2);

        private final byte index;

        Mode(byte index) {
            this.index = index;
        }

        private Mode next(boolean reverse) {
            Mode[] modes = values();
            return modes[(index + (reverse ? modes.length - 1 : 1)) % modes.length];
        }

        private static Mode fromIndex(byte index) {
            return switch (index) {
                case 0 -> Input;
                case 1 -> OutputItem;
                case 2 -> OutputFluid;
                default -> Input;
            };
        }

        private void saveNBTData(NBTTagCompound NBT) {
            NBT.setByte("inputMode", index);
        }

        private static Mode loadNBTData(NBTTagCompound NBT) {
            final byte index = NBT.getByte("inputMode");
            return fromIndex(index);
        }

        private void writeToStream(ByteBuf buffer) {
            buffer.writeByte(this.index);
        }

        private static Mode readFromStream(ByteBuf buffer) {
            final byte index = buffer.readByte();
            return fromIndex(index);
        }

        @Override
        public @NotNull String toString() {
            return StatCollector.translateToLocal(this.getUnlocalizedString());
        }

        public @NotNull String getLocalizedName() {
            return switch (this) {
                case Input -> StatCollector.translateToLocal("GT5U.waila.coke_oven_hatch.mode.input");
                case OutputItem -> StatCollector.translateToLocal("GT5U.waila.coke_oven_hatch.mode.output_item");
                case OutputFluid -> StatCollector.translateToLocal("GT5U.waila.coke_oven_hatch.mode.output_fluid");
            };
        }

        public @NotNull String getUnlocalizedString() {
            return switch (this) {
                case Input -> "GT5U.machines.coke_oven_hatch.mode.input";
                case OutputItem -> "GT5U.machines.coke_oven_hatch.mode.output";
                case OutputFluid -> "GT5U.machines.coke_oven_hatch.mode.output_fluid";
            };
        }
    }

    private static final int FLUID_OUTPUT_LITERS_PER_SECOND = 1_000;

    public static final int ITEM_SLOTS = 1;
    public static final int FLUID_CAPACITY = 8_000;

    private Mode mode = Mode.Input;

    private final IItemHandlerModifiable bufferedInventory = new InvWrapper(this) {

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return mode == Mode.OutputItem && isValidSlot(slot) && amount > 0
                ? super.extractItem(slot, amount, simulate)
                : null;
        }
    };

    private final FluidStackTank drainOnlyOutputTank = new FluidStackTank(
        this::getDrainableStack,
        this::setDrainableStack,
        this::getCapacity) {

        @Override
        public int fill(FluidStack resource, boolean doFill) {
            return 0;
        }

        @Override
        public FluidStack drain(int maxDrain, boolean doDrain) {
            return MTEHatchCokeOven.this.drain(maxDrain, doDrain);
        }

        @Override
        public int getCanFillAmount() {
            return 0;
        }
    };

    public MTEHatchCokeOven(int ID, String name, String nameRegional) {
        super(
            ID,
            name,
            nameRegional,
            0,
            ITEM_SLOTS,
            new String[] { "Hatch for automating the Coke Oven",
                EnumChatFormatting.STRIKETHROUGH + "------------------------------",
                EnumChatFormatting.GREEN + "Modes" + EnumChatFormatting.GRAY + ": Input, Output, Output (Fluids)",
                "Cycle mode with a Screwdriver", });
    }

    public MTEHatchCokeOven(String name, int tier, String[] description, ITexture[][][] textures) {
        super(name, tier, ITEM_SLOTS, description, textures);
    }

    @Override
    public MTEHatchCokeOven newMetaEntity(IGregTechTileEntity tileEntity) {
        return new MTEHatchCokeOven(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public byte getTileEntityBaseType() {
        return HarvestTool.PickaxeLevel0.toTileEntityBaseType();
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity baseMetaTileEntity, EntityPlayer player) {
        openGui(player);
        return true;
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEHatchCokeOvenGui(this).build(guiData, syncManager, uiSettings);
    }

    @Override
    public void saveNBTData(NBTTagCompound NBT) {
        super.saveNBTData(NBT);
        mode.saveNBTData(NBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound NBT) {
        super.loadNBTData(NBT);
        mode = Mode.loadNBTData(NBT);
    }

    @Override
    public void writeToStream(ByteBuf buffer) {
        super.writeToStream(buffer);
        mode.writeToStream(buffer);
    }

    @Override
    public void readFromStream(ByteBuf buffer) {
        super.readFromStream(buffer);
        mode = Mode.readFromStream(buffer);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);
        currenttip.add(StatCollector.translateToLocalFormatted("GT5U.waila.mode", mode.getLocalizedName()));
    }

    @Override
    public void onPostTick(IGregTechTileEntity baseMetaTileEntity, long tick) {
        if (baseMetaTileEntity.isClientSide() || tick % 20 != 0) return;
        if (mode != Mode.OutputFluid || !baseMetaTileEntity.isAllowedToWork()) return;
        final ForgeDirection sideFront = baseMetaTileEntity.getFrontFacing();
        final IFluidHandler target = baseMetaTileEntity.getITankContainerAtSide(sideFront);
        if (target != null) {
            GTUtility.moveFluid(baseMetaTileEntity, target, sideFront, FLUID_OUTPUT_LITERS_PER_SECOND, null);
        }
    }

    public boolean isInput() {
        return mode == Mode.Input && isValid();
    }

    public boolean isFluidOutput() {
        return mode == Mode.OutputFluid;
    }

    /** Returns the number stored without modifying {@code stack}. */
    public int storeItemOutput(ItemStack stack) {
        if (mode != Mode.OutputItem || !isValid() || stack == null || stack.stackSize <= 0) return 0;
        int remaining = stack.stackSize;
        for (int slot = 0; slot < ITEM_SLOTS && remaining > 0; slot++) {
            ItemStack stored = mInventory[slot];
            if (stored != null && !GTUtility.areStacksEqual(stored, stack)) continue;
            int accepted = Math.min(
                remaining,
                Math.min(getInventoryStackLimit(), stack.getMaxStackSize()) - (stored == null ? 0 : stored.stackSize));
            if (accepted <= 0) continue;
            ItemStack updated = stack.copy();
            updated.stackSize = (stored == null ? 0 : stored.stackSize) + accepted;
            setInventorySlotContents(slot, updated);
            remaining -= accepted;
        }
        return stack.stackSize - remaining;
    }

    public int storeFluidOutput(FluidStack stack) {
        if (mode != Mode.OutputFluid || !isValid() || stack == null || stack.amount <= 0) return 0;
        if (mFluid != null && !mFluid.isFluidEqual(stack)) return 0;
        int accepted = Math.min(stack.amount, FLUID_CAPACITY - (mFluid == null ? 0 : mFluid.amount));
        if (accepted <= 0) return 0;
        if (mFluid == null) {
            mFluid = stack.copy();
            mFluid.amount = accepted;
        } else {
            mFluid.amount += accepted;
        }
        markDirty();
        return accepted;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public int getCapacity() {
        return FLUID_CAPACITY;
    }

    @Override
    public FluidStackTank getFluidTank() {
        return drainOnlyOutputTank;
    }

    @Override
    public boolean isLiquidInput(ForgeDirection side) {
        return false;
    }

    @Override
    public boolean isLiquidOutput(ForgeDirection side) {
        return mode == Mode.OutputFluid && getBaseMetaTileEntity() != null
            && side == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean canTankBeEmptied() {
        return mode == Mode.OutputFluid;
    }

    @Override
    public FluidStack setDrainableStack(FluidStack fluid) {
        mFluid = fluid;
        markDirty();
        return fluid;
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        return maxDrain > 0 ? super.drain(maxDrain, doDrain) : null;
    }

    @Override
    public FluidStack drain(ForgeDirection side, int maxDrain, boolean doDrain) {
        return side == ForgeDirection.UNKNOWN || isLiquidOutput(side) ? drain(maxDrain, doDrain) : null;
    }

    @Override
    public FluidStack drain(ForgeDirection side, FluidStack fluid, int amount, boolean doDrain) {
        FluidStack stored = getFluid();
        return stored != null && stored.isFluidEqual(fluid) ? drain(side, amount, doDrain) : null;
    }

    @Override
    public boolean canDrain(ForgeDirection side, Fluid fluid) {
        FluidStack stored = getFluid();
        return canTankBeEmptied() && (side == ForgeDirection.UNKNOWN || isLiquidOutput(side))
            && stored != null
            && stored.amount > 0
            && (fluid == null || stored.getFluid() == fluid);
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection side) {
        return canTankBeEmptied() && (side == ForgeDirection.UNKNOWN || isLiquidOutput(side))
            ? new FluidTankInfo[] { getInfo() }
            : GTValues.emptyFluidTankInfo;
    }

    @Override
    public IItemHandlerModifiable getInventoryHandler() {
        return bufferedInventory;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return mode == Mode.Input && isValidSlot(index) && stack != null;
    }

    @Override
    public boolean isValidSlot(int index) {
        return index >= 0 && index < ITEM_SLOTS;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int ordinalSide) {
        IGregTechTileEntity base = getBaseMetaTileEntity();
        return mode != Mode.OutputFluid && base != null
            && ordinalSide == base.getFrontFacing()
                .ordinal() ? super.getAccessibleSlotsFromSide(ordinalSide) : GTValues.emptyIntArray;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity base, int index, ForgeDirection side, ItemStack stack) {
        return mode == Mode.Input && isValidSlot(index) && base != null && side == base.getFrontFacing();
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity base, int index, ForgeDirection side, ItemStack stack) {
        return mode == Mode.OutputItem && isValidSlot(index) && base != null && side == base.getFrontFacing();
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer player, float x, float y, float z,
        ItemStack tool) {
        final IGregTechTileEntity baseMetaTileEntity = getBaseMetaTileEntity();
        if (baseMetaTileEntity == null) return;
        if (baseMetaTileEntity.isClientSide()) return;
        if (!baseMetaTileEntity.getCoverAtSide(side)
            .isGUIClickable()) return;
        ForgeDirection front = baseMetaTileEntity.getFrontFacing();
        for (int slot = 0; slot < mInventory.length; slot++) {
            ItemStack stack = mInventory[slot];
            if (stack == null) continue;
            GTUtility.dropItemToBlockPos(
                baseMetaTileEntity.getWorld(),
                baseMetaTileEntity.getXCoord() + front.offsetX,
                baseMetaTileEntity.getYCoord() + front.offsetY,
                baseMetaTileEntity.getZCoord() + front.offsetZ,
                stack);
            setInventorySlotContents(slot, null);
        }
        mFluid = null;
        mode = mode.next(player.isSneaking());
        markDirty();
        clearInventoryTextureCache();
        baseMetaTileEntity.issueTileUpdate();
        GTUtility.sendChatTrans(player, mode.getUnlocalizedString());
    }

    @Override
    public ITexture getCasingTexture() {
        ITexture casing = super.getCasingTexture();
        return casing != null ? casing
            : Textures.BlockIcons
                .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings12, 0));
    }

    @Override
    public ITexture[] getTexturesActive(ITexture baseTexture) {
        ITexture pipe = TextureFactory.of(mode == Mode.Input ? OVERLAY_PIPE_IN : OVERLAY_PIPE_OUT);
        if (!GTMod.proxy.mRenderIndicatorsOnHatch) return new ITexture[] { baseTexture, pipe };
        return new ITexture[] { baseTexture, pipe, TextureFactory.of(switch (mode) {
                case Input -> ITEM_IN_SIGN;
                case OutputItem -> ITEM_OUT_SIGN;
                case OutputFluid -> FLUID_OUT_SIGN;
            }) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture baseTexture) {
        return getTexturesActive(baseTexture);
    }
}
