package gregtech.common.covers.redstone;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;
import com.gtnewhorizons.modularui.api.math.MathExpression;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.BaseTextFieldWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.TextFieldWidget;
import gregtech.api.gui.widgets.GT_GuiFakeItemButton;
import gregtech.api.gui.widgets.GT_GuiIcon;
import gregtech.api.gui.widgets.GT_GuiIntegerTextBox;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.covers.GT_Cover_ItemMeter;
import gregtech.common.gui.modularui.CoverDataControllerWidget;
import gregtech.common.gui.modularui.CoverDataFollower_TextFieldWidget;
import gregtech.common.gui.modularui.ItemWatcherSlotWidget;
import gregtech.common.tileentities.storage.GT_MetaTileEntity_DigitalChestBase;
import io.netty.buffer.ByteBuf;
import java.util.Collections;
import java.util.UUID;
import javax.annotation.Nonnull;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GT_Cover_WirelessItemDetector
        extends GT_Cover_AdvancedRedstoneTransmitterBase<GT_Cover_WirelessItemDetector.ItemTransmitterData> {

    public GT_Cover_WirelessItemDetector(ITexture coverTexture) {
        super(ItemTransmitterData.class, coverTexture);
    }

    @Override
    public ItemTransmitterData createDataObject() {
        return new ItemTransmitterData();
    }

    @Override
    public ItemTransmitterData createDataObject(int aLegacyData) {
        return createDataObject();
    }

    @Override
    public ItemTransmitterData doCoverThingsImpl(
            byte aSide,
            byte aInputRedstone,
            int aCoverID,
            ItemTransmitterData aCoverVariable,
            ICoverable aTileEntity,
            long aTimer) {
        byte signal = GT_Cover_ItemMeter.computeSignalBasedOnItems(
                aTileEntity, aCoverVariable.invert, aCoverVariable.threshold, aCoverVariable.slot, aSide);
        long hash = hashCoverCoords(aTileEntity, aSide);
        setSignalAt(aCoverVariable.getUuid(), aCoverVariable.getFrequency(), hash, signal);

        return aCoverVariable;
    }

    @Override
    public boolean letsRedstoneGoOutImpl(
            byte aSide, int aCoverID, ItemTransmitterData aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean manipulatesSidedRedstoneOutputImpl(
            byte aSide, int aCoverID, ItemTransmitterData aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    public static class ItemTransmitterData extends GT_Cover_AdvancedRedstoneTransmitterBase.TransmitterData {
        /**
         * The special value {@code -1} means all slots.
         */
        private int slot;
        /**
         * The special value {@code 0} means threshold check is disabled.
         */
        private int threshold;

        public ItemTransmitterData(int frequency, UUID uuid, boolean invert, int threshold, int slot) {
            super(frequency, uuid, invert);
            this.threshold = threshold;
            this.slot = slot;
        }

        public ItemTransmitterData() {
            super();
            this.threshold = 0;
            this.slot = -1;
        }

        @Nonnull
        @Override
        public ISerializableObject copy() {
            return new ItemTransmitterData(frequency, uuid, invert, threshold, slot);
        }

        @Nonnull
        @Override
        public NBTBase saveDataToNBT() {
            NBTTagCompound tag = (NBTTagCompound) super.saveDataToNBT();
            tag.setInteger("threshold", threshold);
            tag.setInteger("slot", slot);

            return tag;
        }

        @Override
        public void writeToByteBuf(ByteBuf aBuf) {
            super.writeToByteBuf(aBuf);
            aBuf.writeInt(threshold);
            aBuf.writeInt(slot);
        }

        @Override
        public void loadDataFromNBT(NBTBase aNBT) {
            super.loadDataFromNBT(aNBT);

            NBTTagCompound tag = (NBTTagCompound) aNBT;
            threshold = tag.getInteger("threshold");
            slot = tag.getInteger("slot");
        }

        @Nonnull
        @Override
        public ISerializableObject readFromPacket(ByteArrayDataInput aBuf, EntityPlayerMP aPlayer) {
            super.readFromPacket(aBuf, aPlayer);
            threshold = aBuf.readInt();
            slot = aBuf.readInt();

            return this;
        }
    }

    // GUI stuff

    private static final String ALL_TEXT = "All";

    private int maxSlot;
    private final ItemStackHandler targetSlotHandler = new ItemStackHandler(1);

    @Override
    protected int getFrequencyRow() {
        return 0;
    }

    @Override
    protected int getButtonRow() {
        return 1;
    }

    @Override
    protected void addUIWidgets(ModularWindow.Builder builder) {
        maxSlot = getMaxSlot();
        super.addUIWidgets(builder);
        builder.widget(new ItemWatcherSlotWidget()
                        .setGetter(this::getTargetItem)
                        .setPos(startX + spaceX * 4 - 1, startY + spaceY * 3))
                .widget(new TextWidget(GT_Utility.trans("221", "Item threshold"))
                        .setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 5, 4 + startY + spaceY * 2))
                .widget(new TextWidget(GT_Utility.trans("254.0", "Detect Slot"))
                        .setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 5, 4 + startY + spaceY * 3));
    }

    @Override
    protected void addUIForDataController(CoverDataControllerWidget<ItemTransmitterData> controller) {
        super.addUIForDataController(controller);
        controller
                .addFollower(
                        new CoverDataFollower_TextFieldWidget<>(),
                        coverData -> String.valueOf(coverData.threshold),
                        (coverData, state) -> {
                            coverData.threshold = (int) MathExpression.parseMathExpression(state);
                            return coverData;
                        },
                        widget -> widget.setOnScrollNumbers(1, 10, 64)
                                .setNumbers(() -> 0, this::getMaxItemCount)
                                .setPos(1, 2 + spaceY * 2)
                                .setSize(spaceX * 5 - 4, 12))
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
                                        return TextFieldWidget.format.format(Math.min(valSlot, maxSlot));
                                    } else {
                                        return ALL_TEXT;
                                    }
                                })
                                .setPattern(BaseTextFieldWidget.NATURAL_NUMS)
                                .setPos(1, 2 + spaceY * 3)
                                .setSize(spaceX * 4 - 8, 12));
    }

    private int getMaxSlot() {
        final ICoverable tile = getUIContext().getTile();
        if (tile instanceof TileEntity
                && !tile.isDead()
                && tile instanceof IGregTechTileEntity
                && !(((IGregTechTileEntity) tile).getMetaTileEntity() instanceof GT_MetaTileEntity_DigitalChestBase)) {
            return tile.getSizeInventory() - 1;
        } else {
            return -1;
        }
    }

    private int getMaxItemCount() {
        return maxSlot > 0 ? maxSlot * 64 : Integer.MAX_VALUE;
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
        final ICoverable tile = getUIContext().getTile();
        final ItemTransmitterData coverVariable = getCoverData();
        if (coverVariable != null
                && coverVariable.slot >= 0
                && tile instanceof TileEntity
                && !tile.isDead()
                && tile.getSizeInventory() >= coverVariable.slot) {
            return tile.getStackInSlot(coverVariable.slot);
        } else {
            return null;
        }
    }

    @Override
    public Object getClientGUIImpl(
            byte aSide,
            int aCoverID,
            ItemTransmitterData aCoverVariable,
            ICoverable aTileEntity,
            EntityPlayer aPlayer,
            World aWorld) {
        return new ItemTransmitterGUI(aSide, aCoverID, aCoverVariable, aTileEntity);
    }

    private class ItemTransmitterGUI extends TransmitterGUI<ItemTransmitterData> {
        private final GT_GuiIntegerTextBox thresholdBox;
        private final GT_GuiIntegerTextBox slotBox;

        private final GT_GuiFakeItemButton fakeItemSlot;

        private final int maxSlot;

        public ItemTransmitterGUI(
                byte aSide, int aCoverID, ItemTransmitterData aCoverVariable, ICoverable aTileEntity) {
            super(aSide, aCoverID, aCoverVariable, aTileEntity, 0, 1);

            if (tile instanceof TileEntity
                    && !super.tile.isDead()
                    && tile instanceof IGregTechTileEntity
                    && !(((IGregTechTileEntity) tile).getMetaTileEntity()
                            instanceof GT_MetaTileEntity_DigitalChestBase)) {
                maxSlot = tile.getSizeInventory() - 1;
            } else {
                maxSlot = -1;
            }

            thresholdBox = new GT_GuiShortTextBox(
                    this, 1, 1 + startX, 2 + startY + spaceY * 2, spaceX * 5 - 4, 12, 0, getMaxCount());
            slotBox = new GT_GuiShortTextBox(
                    this,
                    2,
                    1 + startX,
                    2 + startY + spaceY * 3,
                    spaceX * 4 - 8,
                    12,
                    -1,
                    maxSlot,
                    Collections.singletonMap("-1", "All"));
            fakeItemSlot =
                    new GT_GuiFakeItemButton(this, startX + spaceX * 4 - 1, startY + spaceY * 3, GT_GuiIcon.SLOT_GRAY);

            slotBox.setEnabled(maxSlot >= 0);
            fakeItemSlot.setMimicSlot(true);
        }

        @Override
        public void drawExtras(int mouseX, int mouseY, float parTicks) {
            super.drawExtras(mouseX, mouseY, parTicks);
            this.getFontRenderer()
                    .drawString(
                            GT_Utility.trans("221", "Item threshold"),
                            startX + spaceX * 5,
                            4 + startY + spaceY * 2,
                            textColor);
            this.getFontRenderer()
                    .drawString(
                            GT_Utility.trans("254.0", "Detect Slot"),
                            startX + spaceX * 5,
                            4 + startY + spaceY * 3,
                            textColor);
        }

        @Override
        public void onMouseWheel(int x, int y, int delta) {
            super.onMouseWheel(x, y, delta);
            if (thresholdBox.isFocused()) {
                genericMouseWheel(thresholdBox, delta, 0, getMaxCount(), 1, 10, 64);
            } else if (slotBox.isFocused()) {
                genericMouseWheel(slotBox, delta, -1, maxSlot, 1, 5, 50);
            }
        }

        @Override
        public void applyTextBox(GT_GuiIntegerTextBox box) {
            if (box == thresholdBox) {
                coverVariable.threshold = parseTextBox(thresholdBox, 0, getMaxCount());
            } else if (box == slotBox) {
                coverVariable.slot = parseTextBox(slotBox, -1, maxSlot);
            }

            super.applyTextBox(box);
        }

        @Override
        public void resetTextBox(GT_GuiIntegerTextBox box) {
            super.resetTextBox(box);
            if (box == thresholdBox) {
                thresholdBox.setText(Integer.toString(coverVariable.threshold));
            } else if (box == slotBox) {
                slotBox.setText(Integer.toString(coverVariable.slot));
            }
        }

        @Override
        protected void update() {
            super.update();
            resetTextBox(thresholdBox);
            resetTextBox(slotBox);

            if (coverVariable.slot >= 0
                    && tile instanceof TileEntity
                    && !tile.isDead()
                    && tile.getSizeInventory() >= coverVariable.slot) {
                ItemStack itemStack = tile.getStackInSlot(coverVariable.slot);
                fakeItemSlot.setItem(itemStack);
            } else {
                fakeItemSlot.setItem(null);
            }
        }

        private int getMaxCount() {
            return maxSlot > 0 ? maxSlot * 64 : Integer.MAX_VALUE;
        }
    }
}
