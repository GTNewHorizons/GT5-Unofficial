package gregtech.common.covers;

import java.text.FieldPosition;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;

import org.jetbrains.annotations.NotNull;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.NumberFormatMUI;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerNumericWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;
import gregtech.common.gui.modularui.widget.ItemWatcherSlotWidget;
import gregtech.common.tileentities.machines.MTEHatchOutputBusME;
import gregtech.common.tileentities.storage.MTEDigitalChestBase;
import io.netty.buffer.ByteBuf;

public class CoverItemMeter extends CoverBehaviorBase {

    // Legacy data format
    private static final int SLOT_MASK = 0x3FFFFFFF; // 0 = all, 1 = 0 ...
    private static final int CONVERTED_BIT = 0x80000000;
    private static final int INVERT_BIT = 0x40000000;

    private boolean inverted;
    /** The special value {@code -1} means all slots. */
    private int slot;
    /** The special value {@code 0} means threshold check is disabled. */
    private int threshold;

    public CoverItemMeter(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
    }

    public int getSlot() {
        return this.slot;
    }

    public CoverItemMeter setSlot(int slot) {
        this.slot = slot;
        return this;
    }

    public boolean isInverted() {
        return this.inverted;
    }

    public CoverItemMeter setInverted(boolean inverted) {
        this.inverted = inverted;
        return this;
    }

    public int getThreshold() {
        return this.threshold;
    }

    public CoverItemMeter setThresdhold(int threshold) {
        this.threshold = threshold;
        return this;
    }

    @Override
    protected void initializeData() {
        inverted = false;
        slot = -1;
        threshold = 0;
    }

    @Override
    protected void loadFromNbt(NBTBase nbt) {
        NBTTagCompound tag = (NBTTagCompound) nbt;
        inverted = tag.getBoolean("invert");
        slot = tag.getInteger("slot");
        threshold = tag.getInteger("threshold");
    }

    @Override
    protected void readFromPacket(ByteArrayDataInput byteData) {
        inverted = byteData.readBoolean();
        slot = byteData.readInt();
        threshold = byteData.readInt();
    }

    @Override
    protected @NotNull NBTBase saveDataToNbt() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("invert", inverted);
        tag.setInteger("slot", slot);
        tag.setInteger("threshold", threshold);
        return tag;
    }

    @Override
    protected void writeDataToByteBuf(ByteBuf byteBuf) {
        byteBuf.writeBoolean(inverted);
        byteBuf.writeInt(slot);
        byteBuf.writeInt(threshold);
    }

    public static byte computeSignalBasedOnItems(ICoverable tileEntity, boolean inverted, int threshold, int slot,
        int ordinalSide) {
        long max = 0;
        long used = 0;
        final IMetaTileEntity mte = ((IGregTechTileEntity) tileEntity).getMetaTileEntity();
        if (mte instanceof MTEDigitalChestBase dc) {
            max = dc.getMaxItemCount();
            used = dc.getProgresstime();
        } else if (mte instanceof MTEHatchOutputBusME) {
            if (((MTEHatchOutputBusME) mte).canAcceptItem()) {
                max = 64;
                used = 0;
            }
        } else {
            final int[] slots = slot >= 0 ? new int[] { slot } : tileEntity.getAccessibleSlotsFromSide(ordinalSide);

            for (int i : slots) {
                if (i >= 0 && i < tileEntity.getSizeInventory()) {
                    max += 64;
                    final ItemStack stack = tileEntity.getStackInSlot(i);
                    if (stack != null) used += ((long) stack.stackSize << 6) / stack.getMaxStackSize();
                }
            }
        }

        return GTUtility.convertRatioToRedstone(used, max, threshold, inverted);
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable != null) {
            byte signal = computeSignalBasedOnItems(coverable, inverted, threshold, slot, coverSide.ordinal());
            coverable.setOutputRedstoneSignal(coverSide, signal);
        }
    }

    @Override
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null) {
            return;
        }
        if (aPlayer.isSneaking()) {
            if (inverted) {
                inverted = false;
                GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("055", "Normal"));
            } else {
                inverted = true;
                GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("054", "Inverted"));
            }
        } else {
            slot++;
            if (slot > coverable.getSizeInventory()) slot = -1;

            if (slot == -1)
                GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("053", "Slot: ") + GTUtility.trans("ALL", "All"));
            else GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("053", "Slot: ") + slot);
        }
    }

    @Override
    public boolean letsEnergyIn() {
        return true;
    }

    @Override
    public boolean letsEnergyOut() {
        return true;
    }

    @Override
    public boolean letsFluidIn(Fluid aFluid) {
        return true;
    }

    @Override
    public boolean letsFluidOut(Fluid aFluid) {
        return true;
    }

    @Override
    public boolean letsItemsIn(int aSlot) {
        return true;
    }

    @Override
    public boolean letsItemsOut(int aSlot) {
        return true;
    }

    @Override
    public boolean manipulatesSidedRedstoneOutput() {
        return true;
    }

    @Override
    public int getMinimumTickRate() {
        return 1;
    }

    @Override
    public int getDefaultTickRate() {
        return 5;
    }

    // GUI stuff

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new ItemMeterUIFactory(buildContext).createWindow();
    }

    private static class ItemMeterUIFactory extends UIFactory<CoverItemMeter> {

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;

        /**
         * Display the text "All" instead of a number when the slot is set to -1.
         */
        protected static final NumberFormatMUI numberFormatAll = new NumberFormatMUI() {

            @Override
            public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
                if (number < 0) {
                    return toAppendTo.append(GTUtility.trans("ALL", "All"));
                } else {
                    return super.format(number, toAppendTo, pos);
                }
            }
        };

        private int maxSlot;
        private int maxThreshold;

        public ItemMeterUIFactory(CoverUIBuildContext buildContext) {
            super(buildContext);
        }

        @Override
        protected CoverItemMeter adaptCover(Cover cover) {
            if (cover instanceof CoverItemMeter adapterCover) {
                return adapterCover;
            }
            return null;
        }

        @Override
        protected void addUIWidgets(ModularWindow.Builder builder) {
            final String INVERTED = GTUtility.trans("INVERTED", "Inverted");
            final String NORMAL = GTUtility.trans("NORMAL", "Normal");

            setMaxSlot();
            setMaxThreshold();

            builder
                .widget(
                    new CoverDataControllerWidget<>(this::getCover, getUIBuildContext())
                        .addFollower(
                            CoverDataFollowerToggleButtonWidget.ofRedstone(),
                            CoverItemMeter::isInverted,
                            CoverItemMeter::setInverted,
                            widget -> widget.addTooltip(0, NORMAL)
                                .addTooltip(1, INVERTED)
                                .setPos(0, 0))
                        .addFollower(
                            new CoverDataFollowerNumericWidget<>(),
                            coverData -> (double) coverData.getThreshold(),
                            (coverData, state) -> coverData.setThresdhold(state.intValue()),
                            widget -> widget.setBounds(0, maxThreshold)
                                .setScrollValues(1, 64, 1000)
                                .setFocusOnGuiOpen(true)
                                .setPos(0, 2 + spaceY)
                                .setSize(spaceX * 4 + 5, 12))
                        .addFollower(
                            new CoverDataFollowerNumericWidget<>(),
                            coverData -> (double) coverData.getSlot(),
                            (coverData, state) -> coverData.setSlot(state.intValue()),
                            widget -> widget.setBounds(-1, maxSlot)
                                .setDefaultValue(-1)
                                .setScrollValues(1, 100, 10)
                                .setNumberFormat(numberFormatAll)
                                .setPos(0, 2 + spaceY * 2)
                                .setSize(spaceX * 3 + 1, 12))
                        .setPos(startX, startY))
                .widget(
                    new ItemWatcherSlotWidget().setGetter(this::getTargetItem)
                        .setPos(startX + spaceX * 3 + 8, startY + spaceY * 2))
                .widget(
                    new TextWidget().setStringSupplier(getCoverString(c -> c.isInverted() ? INVERTED : NORMAL))
                        .setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX, 4 + startY))
                .widget(
                    new TextWidget(GTUtility.trans("254", "Detect slot #")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 4 + 9, 4 + startY + spaceY * 2))
                .widget(
                    new TextWidget(GTUtility.trans("221", "Item threshold")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 4 + 9, 4 + startY + spaceY));
        }

        private void setMaxSlot() {
            final ICoverable tile = getUIBuildContext().getTile();
            if (!tile.isDead() && tile instanceof IGregTechTileEntity gtTile
                && !(gtTile.getMetaTileEntity() instanceof MTEDigitalChestBase)) {
                maxSlot = Math.min(tile.getSizeInventory() - 1, SLOT_MASK - 1);
            } else {
                maxSlot = -1;
            }
        }

        private void setMaxThreshold() {
            final ICoverable tile = getUIBuildContext().getTile();
            if (!tile.isDead() && tile instanceof IGregTechTileEntity gtTile
                && gtTile.getMetaTileEntity() instanceof MTEDigitalChestBase) {
                maxThreshold = gtTile.getMaxItemCount();
            } else {
                maxThreshold = maxSlot > 0 ? maxSlot * 64 : Integer.MAX_VALUE;
            }
        }

        private ItemStack getTargetItem() {
            CoverItemMeter cover = getCover();
            if (cover == null || cover.getSlot() < 0) {
                return null;
            }
            ICoverable tile = getUIBuildContext().getTile();
            if (tile instanceof TileEntity && !tile.isDead()) {
                if (tile.getSizeInventory() >= cover.getSlot()) {
                    return tile.getStackInSlot(cover.getSlot());
                }
            }
            return null;
        }
    }
}
