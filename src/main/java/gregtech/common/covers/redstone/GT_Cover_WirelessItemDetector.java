package gregtech.common.covers.redstone;

import java.util.UUID;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;
import com.gtnewhorizons.modularui.api.math.MathExpression;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.BaseTextFieldWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.TextFieldWidget;

import gregtech.api.gui.modularui.GT_CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.covers.GT_Cover_ItemMeter;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollower_TextFieldWidget;
import gregtech.common.gui.modularui.widget.ItemWatcherSlotWidget;
import gregtech.common.tileentities.storage.GT_MetaTileEntity_DigitalChestBase;
import io.netty.buffer.ByteBuf;

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
    public ItemTransmitterData doCoverThingsImpl(ForgeDirection side, byte aInputRedstone, int aCoverID,
        ItemTransmitterData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        byte signal = GT_Cover_ItemMeter.computeSignalBasedOnItems(
            aTileEntity,
            aCoverVariable.invert,
            aCoverVariable.threshold,
            aCoverVariable.slot,
            side.ordinal());
        final long hash = hashCoverCoords(aTileEntity, side);
        setSignalAt(aCoverVariable.getUuid(), aCoverVariable.getFrequency(), hash, signal);

        return aCoverVariable;
    }

    @Override
    public boolean letsRedstoneGoOutImpl(ForgeDirection side, int aCoverID, ItemTransmitterData aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean manipulatesSidedRedstoneOutputImpl(ForgeDirection side, int aCoverID,
        ItemTransmitterData aCoverVariable, ICoverable aTileEntity) {
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

    @Override
    public ModularWindow createWindow(GT_CoverUIBuildContext buildContext) {
        return new WirelessItemDetectorUIFactory(buildContext).createWindow();
    }

    private class WirelessItemDetectorUIFactory extends AdvancedRedstoneTransmitterBaseUIFactory {

        private static final String ALL_TEXT = "All";

        private int maxSlot;
        private final ItemStackHandler targetSlotHandler = new ItemStackHandler(1);

        public WirelessItemDetectorUIFactory(GT_CoverUIBuildContext buildContext) {
            super(buildContext);
        }

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
            builder.widget(
                new ItemWatcherSlotWidget().setGetter(this::getTargetItem)
                    .setPos(startX + spaceX * 4 - 1, startY + spaceY * 3))
                .widget(
                    new TextWidget(GT_Utility.trans("221", "Item threshold")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 5, 4 + startY + spaceY * 2))
                .widget(
                    new TextWidget(GT_Utility.trans("254.0", "Detect Slot")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 5, 4 + startY + spaceY * 3));
        }

        @Override
        protected void addUIForDataController(CoverDataControllerWidget<ItemTransmitterData> controller) {
            super.addUIForDataController(controller);
            controller.addFollower(
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
            final ICoverable tile = getUIBuildContext().getTile();
            if (tile instanceof TileEntity && !tile.isDead()
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
            final ICoverable tile = getUIBuildContext().getTile();
            final ItemTransmitterData coverVariable = getCoverData();
            if (coverVariable != null && coverVariable.slot >= 0
                && tile instanceof TileEntity
                && !tile.isDead()
                && tile.getSizeInventory() >= coverVariable.slot) {
                return tile.getStackInSlot(coverVariable.slot);
            } else {
                return null;
            }
        }
    }
}
