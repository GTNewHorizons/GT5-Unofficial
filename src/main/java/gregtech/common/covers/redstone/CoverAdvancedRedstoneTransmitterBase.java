package gregtech.common.covers.redstone;

import java.util.Objects;
import java.util.UUID;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;
import io.netty.buffer.ByteBuf;

public abstract class CoverAdvancedRedstoneTransmitterBase<T extends CoverAdvancedRedstoneTransmitterBase.TransmitterData>
    extends CoverAdvancedWirelessRedstoneBase<T> {

    public CoverAdvancedRedstoneTransmitterBase(CoverContext context, Class<T> typeToken, ITexture coverTexture) {
        super(context, typeToken, coverTexture);
    }

    private void unregisterSignal() {
        ICoverable coverable = coveredTile.get();
        if (coverable == null) return;
        final long hash = hashCoverCoords(coverable, coverSide);
        removeSignalAt(coverData.uuid, coverData.frequency, hash);
    }

    @Override
    public void onCoverRemoval() {
        unregisterSignal();
    }

    @Override
    public void onBaseTEDestroyed() {
        unregisterSignal();
    }

    @Override
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        coverData.invert = !coverData.invert;
        GTUtility.sendChatToPlayer(
            aPlayer,
            coverData.invert ? GTUtility.trans("054", "Inverted") : GTUtility.trans("055", "Normal"));
    }

    @Override
    public void preDataChanged(int newCoverId, ISerializableObject newCoverVariable) {
        if (newCoverVariable instanceof TransmitterData newTransmitterData
            && (coverData.frequency != newTransmitterData.frequency
                || !Objects.equals(coverData.uuid, newTransmitterData.uuid))) {
            unregisterSignal();
        }
    }

    public static class TransmitterData extends CoverAdvancedWirelessRedstoneBase.WirelessData {

        protected boolean invert;

        public TransmitterData(int frequency, UUID uuid, boolean invert) {
            super(frequency, uuid);
            this.invert = invert;
        }

        public TransmitterData() {
            this(0, null, false);
        }

        public boolean isInvert() {
            return invert;
        }

        @Nonnull
        @Override
        public ISerializableObject copy() {
            return new TransmitterData(frequency, uuid, invert);
        }

        @Nonnull
        @Override
        public NBTBase saveDataToNBT() {
            NBTTagCompound tag = (NBTTagCompound) super.saveDataToNBT();
            tag.setBoolean("invert", invert);

            return tag;
        }

        @Override
        public void writeToByteBuf(ByteBuf aBuf) {
            super.writeToByteBuf(aBuf);
            aBuf.writeBoolean(invert);
        }

        @Override
        public void loadDataFromNBT(NBTBase aNBT) {
            super.loadDataFromNBT(aNBT);

            NBTTagCompound tag = (NBTTagCompound) aNBT;
            invert = tag.getBoolean("invert");
        }

        @Override
        public void readFromPacket(ByteArrayDataInput aBuf) {
            super.readFromPacket(aBuf);
            invert = aBuf.readBoolean();
        }
    }

    // GUI stuff

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new AdvancedRedstoneTransmitterBaseUIFactory(buildContext).createWindow();
    }

    protected class AdvancedRedstoneTransmitterBaseUIFactory extends AdvancedWirelessRedstoneBaseUIFactory {

        public AdvancedRedstoneTransmitterBaseUIFactory(CoverUIBuildContext buildContext) {
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
        protected boolean isShiftPrivateLeft() {
            return true;
        }

        @Override
        protected void addUIWidgets(ModularWindow.Builder builder) {
            super.addUIWidgets(builder);
            builder.widget(TextWidget.dynamicString(() -> {
                T coverData = getCoverData();
                if (coverData != null) {
                    return getCoverData().invert ? GTUtility.trans("INVERTED", "Inverted")
                        : GTUtility.trans("NORMAL", "Normal");
                } else {
                    return "";
                }
            })
                .setSynced(false)
                .setDefaultColor(COLOR_TEXT_GRAY.get())
                .setPos(startX + spaceX * 10, 4 + startY + spaceY * getButtonRow()));
        }

        @Override
        protected void addUIForDataController(CoverDataControllerWidget<T> controller) {
            super.addUIForDataController(controller);
            controller.addFollower(
                CoverDataFollowerToggleButtonWidget.ofRedstone(),
                coverData -> coverData.invert,
                (coverData, state) -> {
                    coverData.invert = state;
                    return coverData;
                },
                widget -> widget.addTooltip(0, GTUtility.trans("NORMAL", "Normal"))
                    .addTooltip(1, GTUtility.trans("INVERTED", "Inverted"))
                    .setPos(spaceX * 9, spaceY * getButtonRow()));
        }
    }
}
