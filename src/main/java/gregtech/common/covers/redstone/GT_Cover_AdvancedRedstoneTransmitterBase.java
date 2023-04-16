package gregtech.common.covers.redstone;

import java.util.Objects;
import java.util.UUID;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.GT_CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollower_ToggleButtonWidget;
import io.netty.buffer.ByteBuf;

public abstract class GT_Cover_AdvancedRedstoneTransmitterBase<T extends GT_Cover_AdvancedRedstoneTransmitterBase.TransmitterData>
    extends GT_Cover_AdvancedWirelessRedstoneBase<T> {

    public GT_Cover_AdvancedRedstoneTransmitterBase(Class<T> typeToken, ITexture coverTexture) {
        super(typeToken, coverTexture);
    }

    private static void unregisterSignal(ForgeDirection aSide, TransmitterData aCoverVariable, ICoverable aTileEntity) {
        long hash = hashCoverCoords(aTileEntity, aSide);
        removeSignalAt(aCoverVariable.uuid, aCoverVariable.frequency, hash);
    }

    @Override
    public boolean onCoverRemovalImpl(ForgeDirection aSide, int aCoverID, TransmitterData aCoverVariable,
        ICoverable aTileEntity, boolean aForced) {
        unregisterSignal(aSide, aCoverVariable, aTileEntity);
        return true;
    }

    @Override
    protected void onBaseTEDestroyedImpl(ForgeDirection aSide, int aCoverID, TransmitterData aCoverVariable,
        ICoverable aTileEntity) {
        unregisterSignal(aSide, aCoverVariable, aTileEntity);
    }

    @Override
    protected T onCoverScrewdriverClickImpl(ForgeDirection aSide, int aCoverID, T aCoverVariable,
        ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        aCoverVariable.invert = !aCoverVariable.invert;
        GT_Utility.sendChatToPlayer(
            aPlayer,
            aCoverVariable.invert ? GT_Utility.trans("054", "Inverted") : GT_Utility.trans("055", "Normal"));

        return aCoverVariable;
    }

    @Override
    protected void preDataChangedImpl(ForgeDirection aSide, int aCoverID, int aNewCoverId, T aCoverVariable,
        T aNewCoverVariable, ICoverable aTileEntity) {
        if (aCoverVariable.frequency != aNewCoverVariable.frequency
            || !Objects.equals(aCoverVariable.uuid, aNewCoverVariable.uuid)) {
            unregisterSignal(aSide, aCoverVariable, aTileEntity);
        }
    }

    public static class TransmitterData extends GT_Cover_AdvancedWirelessRedstoneBase.WirelessData {

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

        @Nonnull
        @Override
        public ISerializableObject readFromPacket(ByteArrayDataInput aBuf, EntityPlayerMP aPlayer) {
            super.readFromPacket(aBuf, aPlayer);
            invert = aBuf.readBoolean();

            return this;
        }
    }

    // GUI stuff

    @Override
    public ModularWindow createWindow(GT_CoverUIBuildContext buildContext) {
        return new AdvancedRedstoneTransmitterBaseUIFactory(buildContext).createWindow();
    }

    protected class AdvancedRedstoneTransmitterBaseUIFactory extends AdvancedWirelessRedstoneBaseUIFactory {

        public AdvancedRedstoneTransmitterBaseUIFactory(GT_CoverUIBuildContext buildContext) {
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
                    return getCoverData().invert ? GT_Utility.trans("INVERTED", "Inverted")
                        : GT_Utility.trans("NORMAL", "Normal");
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
                CoverDataFollower_ToggleButtonWidget.ofRedstone(),
                coverData -> coverData.invert,
                (coverData, state) -> {
                    coverData.invert = state;
                    return coverData;
                },
                widget -> widget.addTooltip(0, GT_Utility.trans("NORMAL", "Normal"))
                    .addTooltip(1, GT_Utility.trans("INVERTED", "Inverted"))
                    .setPos(spaceX * 9, spaceY * getButtonRow()));
        }
    }
}
