package gregtech.common.covers;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.math.MathExpression;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.BaseTextFieldWidget;

import gregtech.api.gui.modularui.GT_CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_CoverBehaviorBase;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollower_TextFieldWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollower_ToggleButtonWidget;
import gregtech.common.gui.modularui.widget.ItemWatcherSlotWidget;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_OutputBus_ME;
import gregtech.common.tileentities.storage.GT_MetaTileEntity_DigitalChestBase;
import io.netty.buffer.ByteBuf;

public class GT_Cover_ItemMeter extends GT_CoverBehaviorBase<GT_Cover_ItemMeter.ItemMeterData> {

    // Legacy data format
    private static final int SLOT_MASK = 0x3FFFFFFF; // 0 = all, 1 = 0 ...
    private static final int CONVERTED_BIT = 0x80000000;
    private static final int INVERT_BIT = 0x40000000;

    /**
     * @deprecated use {@link #GT_Cover_ItemMeter(ITexture coverTexture)} instead
     */
    @Deprecated
    public GT_Cover_ItemMeter() {
        this(null);
    }

    public GT_Cover_ItemMeter(ITexture coverTexture) {
        super(ItemMeterData.class, coverTexture);
    }

    @Override
    public ItemMeterData createDataObject(int aLegacyData) {
        // Convert from ver. 5.09.33.50
        if ((CONVERTED_BIT & aLegacyData) == 0) if (aLegacyData == 0) aLegacyData = CONVERTED_BIT;
        else if (aLegacyData == 1) aLegacyData = CONVERTED_BIT | INVERT_BIT;
        else if (aLegacyData > 1) aLegacyData = CONVERTED_BIT | Math.min((aLegacyData - 2), SLOT_MASK);

        boolean invert = (aLegacyData & INVERT_BIT) == INVERT_BIT;
        int slot = (aLegacyData & SLOT_MASK) - 1;

        return new ItemMeterData(invert, slot, 0);
    }

    @Override
    public ItemMeterData createDataObject() {
        return new ItemMeterData();
    }

    @Override
    protected boolean isRedstoneSensitiveImpl(ForgeDirection side, int aCoverID, ItemMeterData aCoverVariable,
        ICoverable aTileEntity, long aTimer) {
        return false;
    }

    public static byte computeSignalBasedOnItems(ICoverable tileEntity, boolean inverted, int threshold, int slot,
        int ordinalSide) {
        long max = 0;
        long used = 0;
        final IMetaTileEntity mte = ((IGregTechTileEntity) tileEntity).getMetaTileEntity();
        if (mte instanceof GT_MetaTileEntity_DigitalChestBase dc) {
            max = dc.getMaxItemCount();
            used = dc.getProgresstime();
        } else if (mte instanceof GT_MetaTileEntity_Hatch_OutputBus_ME) {
            if (((GT_MetaTileEntity_Hatch_OutputBus_ME) mte).isLastOutputFailed()) {
                max = 64;
                used = 64;
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

        return GT_Utility.convertRatioToRedstone(used, max, threshold, inverted);
    }

    @Override
    protected ItemMeterData doCoverThingsImpl(ForgeDirection side, byte aInputRedstone, int aCoverID,
        ItemMeterData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        byte signal = computeSignalBasedOnItems(
            aTileEntity,
            aCoverVariable.inverted,
            aCoverVariable.threshold,
            aCoverVariable.slot,
            side.ordinal());
        aTileEntity.setOutputRedstoneSignal(side, signal);

        return aCoverVariable;
    }

    @Override
    protected ItemMeterData onCoverScrewdriverClickImpl(ForgeDirection side, int aCoverID, ItemMeterData aCoverVariable,
        ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (aPlayer.isSneaking()) {
            if (aCoverVariable.inverted) {
                aCoverVariable.inverted = false;
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("055", "Normal"));
            } else {
                aCoverVariable.inverted = true;
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("054", "Inverted"));
            }
        } else {
            aCoverVariable.slot++;
            if (aCoverVariable.slot > aTileEntity.getSizeInventory()) aCoverVariable.slot = -1;

            if (aCoverVariable.slot == -1) GT_Utility
                .sendChatToPlayer(aPlayer, GT_Utility.trans("053", "Slot: ") + GT_Utility.trans("ALL", "All"));
            else GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("053", "Slot: ") + aCoverVariable.slot);
        }

        return aCoverVariable;
    }

    @Override
    protected boolean letsEnergyInImpl(ForgeDirection side, int aCoverID, ItemMeterData aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsEnergyOutImpl(ForgeDirection side, int aCoverID, ItemMeterData aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsFluidInImpl(ForgeDirection side, int aCoverID, ItemMeterData aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsFluidOutImpl(ForgeDirection side, int aCoverID, ItemMeterData aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsItemsInImpl(ForgeDirection side, int aCoverID, ItemMeterData aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsItemsOutImpl(ForgeDirection side, int aCoverID, ItemMeterData aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean manipulatesSidedRedstoneOutputImpl(ForgeDirection side, int aCoverID,
        ItemMeterData aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected int getTickRateImpl(ForgeDirection side, int aCoverID, ItemMeterData aCoverVariable,
        ICoverable aTileEntity) {
        return 5;
    }

    // GUI stuff

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public boolean useModularUI() {
        return true;
    }

    @Override
    public ModularWindow createWindow(GT_CoverUIBuildContext buildContext) {
        return new ItemMeterUIFactory(buildContext).createWindow();
    }

    private class ItemMeterUIFactory extends UIFactory {

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;
        private static final String ALL_TEXT = "All";

        private int maxSlot;
        private int maxThreshold;

        public ItemMeterUIFactory(GT_CoverUIBuildContext buildContext) {
            super(buildContext);
        }

        @Override
        protected void addUIWidgets(ModularWindow.Builder builder) {
            final String INVERTED = GT_Utility.trans("INVERTED", "Inverted");
            final String NORMAL = GT_Utility.trans("NORMAL", "Normal");

            setMaxSlot();
            setMaxThreshold();

            builder.widget(
                new CoverDataControllerWidget<>(this::getCoverData, this::setCoverData, GT_Cover_ItemMeter.this)
                    .addFollower(
                        CoverDataFollower_ToggleButtonWidget.ofRedstone(),
                        coverData -> coverData.inverted,
                        (coverData, state) -> {
                            coverData.inverted = state;
                            return coverData;
                        },
                        widget -> widget.addTooltip(0, NORMAL)
                            .addTooltip(1, INVERTED)
                            .setPos(0, 0))
                    .addFollower(
                        new CoverDataFollower_TextFieldWidget<>(),
                        coverData -> getSlotTextFieldContent(coverData.slot),
                        (coverData, state) -> {
                            coverData.slot = getIntFromText(state);
                            return coverData;
                        },
                        widget -> widget.setOnScrollText()
                            .setValidator(val -> {
                                final int valSlot = getIntFromText(val);
                                if (valSlot > -1) {
                                    return widget.getDecimalFormatter()
                                        .format(Math.min(valSlot, maxSlot));
                                } else {
                                    return ALL_TEXT;
                                }
                            })
                            .setPattern(BaseTextFieldWidget.NATURAL_NUMS)
                            .setFocusOnGuiOpen(true)
                            .setPos(0, spaceY + 2)
                            .setSize(spaceX * 2 + 5, 12))
                    .addFollower(
                        new CoverDataFollower_TextFieldWidget<>(),
                        coverData -> String.valueOf(coverData.threshold),
                        (coverData, state) -> {
                            coverData.threshold = (int) MathExpression.parseMathExpression(state);
                            return coverData;
                        },
                        widget -> widget.setOnScrollNumbers(1, 10, 64)
                            .setNumbers(0, maxThreshold)
                            .setPos(0, spaceY * 2 + 2)
                            .setSize(spaceX * 4 + 5, 12))
                    .setPos(startX, startY))
                .widget(
                    new ItemWatcherSlotWidget().setGetter(this::getTargetItem)
                        .setPos(startX + spaceX * 8 - 4, startY + spaceY))
                .widget(
                    TextWidget
                        .dynamicString(() -> getCoverData() != null ? getCoverData().inverted ? INVERTED : NORMAL : "")
                        .setSynced(false)
                        .setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 3, 4 + startY))
                .widget(
                    new TextWidget(GT_Utility.trans("254", "Detect slot#")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 3, 4 + startY + spaceY))
                .widget(
                    new TextWidget(GT_Utility.trans("221", "Item threshold")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 5 - 10, startY + spaceY * 2 + 4));
        }

        private void setMaxSlot() {
            final ICoverable tile = getUIBuildContext().getTile();
            if (!tile.isDead() && tile instanceof IGregTechTileEntity gtTile
                && !(gtTile.getMetaTileEntity() instanceof GT_MetaTileEntity_DigitalChestBase)) {
                maxSlot = Math.min(tile.getSizeInventory() - 1, SLOT_MASK - 1);
            } else {
                maxSlot = -1;
            }
        }

        private void setMaxThreshold() {
            final ICoverable tile = getUIBuildContext().getTile();
            if (!tile.isDead() && tile instanceof IGregTechTileEntity gtTile
                && gtTile.getMetaTileEntity() instanceof GT_MetaTileEntity_DigitalChestBase) {
                maxThreshold = gtTile.getMaxItemCount();
            } else {
                maxThreshold = maxSlot > 0 ? maxSlot * 64 : Integer.MAX_VALUE;
            }
        }

        private int getIntFromText(String text) {
            try {
                return (int) MathExpression.parseMathExpression(text, -1);
            } catch (Exception e) {
                return -1;
            }
        }

        private String getSlotTextFieldContent(int val) {
            return val < 0 ? ALL_TEXT : String.valueOf(val);
        }

        private ItemStack getTargetItem() {
            ItemMeterData coverVariable = getCoverData();
            if (coverVariable == null || coverVariable.slot < 0) {
                return null;
            }
            ICoverable tile = getUIBuildContext().getTile();
            if (tile instanceof TileEntity && !tile.isDead()) {
                if (tile.getSizeInventory() >= coverVariable.slot) {
                    return tile.getStackInSlot(coverVariable.slot);
                }
            }
            return null;
        }
    }

    public static class ItemMeterData implements ISerializableObject {

        private boolean inverted;
        /** The special value {@code -1} means all slots. */
        private int slot;
        /** The special value {@code 0} means threshold check is disabled. */
        private int threshold;

        public ItemMeterData() {
            inverted = false;
            slot = -1;
            threshold = 0;
        }

        public ItemMeterData(boolean inverted, int slot, int threshold) {
            this.inverted = inverted;
            this.slot = slot;
            this.threshold = threshold;
        }

        @Nonnull
        @Override
        public ISerializableObject copy() {
            return new ItemMeterData(inverted, slot, threshold);
        }

        @Nonnull
        @Override
        public NBTBase saveDataToNBT() {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setBoolean("invert", inverted);
            tag.setInteger("slot", slot);
            tag.setInteger("threshold", threshold);
            return tag;
        }

        @Override
        public void writeToByteBuf(ByteBuf aBuf) {
            aBuf.writeBoolean(inverted);
            aBuf.writeInt(slot);
            aBuf.writeInt(threshold);
        }

        @Override
        public void loadDataFromNBT(NBTBase aNBT) {
            NBTTagCompound tag = (NBTTagCompound) aNBT;
            inverted = tag.getBoolean("invert");
            slot = tag.getInteger("slot");
            threshold = tag.getInteger("threshold");
        }

        @Nonnull
        @Override
        public ISerializableObject readFromPacket(ByteArrayDataInput aBuf, EntityPlayerMP aPlayer) {
            inverted = aBuf.readBoolean();
            slot = aBuf.readInt();
            threshold = aBuf.readInt();
            return this;
        }
    }
}
