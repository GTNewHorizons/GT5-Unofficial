package gregtech.common.covers.redstone;

import java.util.Objects;
import java.util.UUID;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

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

    public CoverAdvancedRedstoneTransmitterBase(Class<T> typeToken, ITexture coverTexture) {
        super(typeToken, coverTexture);
    }

    private static void unregisterSignal(ForgeDirection side, TransmitterData aCoverVariable, ICoverable aTileEntity) {
        final long hash = hashCoverCoords(aTileEntity, side);
        removeSignalAt(aCoverVariable.uuid, aCoverVariable.frequency, hash);
    }

    @Override
    public boolean onCoverRemovalImpl(ForgeDirection side, int aCoverID, TransmitterData aCoverVariable,
        ICoverable aTileEntity, boolean aForced) {
        unregisterSignal(side, aCoverVariable, aTileEntity);
        return true;
    }

    @Override
    protected void onBaseTEDestroyedImpl(ForgeDirection side, int aCoverID, TransmitterData aCoverVariable,
        ICoverable aTileEntity) {
        unregisterSignal(side, aCoverVariable, aTileEntity);
    }

    @Override
    protected T onCoverScrewdriverClickImpl(ForgeDirection side, int aCoverID, T aCoverVariable, ICoverable aTileEntity,
        EntityPlayer aPlayer, float aX, float aY, float aZ) {
        aCoverVariable.invert = !aCoverVariable.invert;
        GTUtility.sendChatToPlayer(
            aPlayer,
            aCoverVariable.invert ? GTUtility.trans("054", "Inverted") : GTUtility.trans("055", "Normal"));

        return aCoverVariable;
    }

    @Override
    protected void preDataChangedImpl(ForgeDirection side, int aCoverID, int aNewCoverId, T aCoverVariable,
        T aNewCoverVariable, ICoverable aTileEntity) {
        if (aCoverVariable.frequency != aNewCoverVariable.frequency
            || !Objects.equals(aCoverVariable.uuid, aNewCoverVariable.uuid)) {
            unregisterSignal(side, aCoverVariable, aTileEntity);
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
