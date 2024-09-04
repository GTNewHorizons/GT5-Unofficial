package gregtech.common.covers.redstone;

import java.text.FieldPosition;
import java.util.UUID;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.NumberFormatMUI;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.covers.CoverItemMeter;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerNumericWidget;
import gregtech.common.gui.modularui.widget.ItemWatcherSlotWidget;
import gregtech.common.tileentities.storage.MTEDigitalChestBase;
import io.netty.buffer.ByteBuf;

public class CoverWirelessItemDetector
    extends CoverAdvancedRedstoneTransmitterBase<CoverWirelessItemDetector.ItemTransmitterData> {

    public CoverWirelessItemDetector(ITexture coverTexture) {
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
        byte signal = CoverItemMeter.computeSignalBasedOnItems(
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

    public static class ItemTransmitterData extends CoverAdvancedRedstoneTransmitterBase.TransmitterData {

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
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new WirelessItemDetectorUIFactory(buildContext).createWindow();
    }

    private class WirelessItemDetectorUIFactory extends AdvancedRedstoneTransmitterBaseUIFactory {

        private int maxSlot;
        private int maxThreshold;
        /**
         * Display the text "All" instead of a number when the slot is set to -1.
         */
        private static final NumberFormatMUI numberFormatAll = new NumberFormatMUI() {

            @Override
            public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
                if (number < 0) {
                    return toAppendTo.append(GTUtility.trans("ALL", "All"));
                } else {
                    return super.format(number, toAppendTo, pos);
                }
            }
        };

        public WirelessItemDetectorUIFactory(CoverUIBuildContext buildContext) {
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
            setMaxSlot();
            setMaxThreshold();
            super.addUIWidgets(builder);
            builder.widget(
                new ItemWatcherSlotWidget().setGetter(this::getTargetItem)
                    .setPos(startX + spaceX * 4 - 1, startY + spaceY * 3))
                .widget(
                    new TextWidget(GTUtility.trans("221", "Item threshold")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 5, 4 + startY + spaceY * 2))
                .widget(
                    new TextWidget(GTUtility.trans("254", "Detect Slot #")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 5, 4 + startY + spaceY * 3));
        }

        @Override
        protected void addUIForDataController(CoverDataControllerWidget<ItemTransmitterData> controller) {
            super.addUIForDataController(controller);
            controller.addFollower(
                new CoverDataFollowerNumericWidget<>(),
                coverData -> (double) coverData.threshold,
                (coverData, state) -> {
                    coverData.threshold = state.intValue();
                    return coverData;
                },
                widget -> widget.setBounds(0, maxThreshold)
                    .setScrollValues(1, 64, 1000)
                    .setFocusOnGuiOpen(true)
                    .setPos(1, 2 + spaceY * 2)
                    .setSize(spaceX * 5 - 4, 12))
                .addFollower(
                    new CoverDataFollowerNumericWidget<>(),
                    coverData -> (double) coverData.slot,
                    (coverData, state) -> {
                        coverData.slot = state.intValue();
                        return coverData;
                    },
                    widget -> widget.setBounds(-1, maxSlot)
                        .setDefaultValue(-1)
                        .setScrollValues(1, 100, 10)
                        .setNumberFormat(numberFormatAll)
                        .setPos(1, 2 + spaceY * 3)
                        .setSize(spaceX * 4 - 8, 12));
        }

        private void setMaxSlot() {
            final ICoverable tile = getUIBuildContext().getTile();
            if (!tile.isDead() && tile instanceof IGregTechTileEntity gtTile
                && !(gtTile.getMetaTileEntity() instanceof MTEDigitalChestBase)) {
                maxSlot = tile.getSizeInventory() - 1;
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
