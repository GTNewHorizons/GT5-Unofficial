package gregtech.common.covers.redstone;

import java.util.Objects;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import org.jetbrains.annotations.NotNull;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GTUtility;
import gregtech.common.covers.Cover;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;
import io.netty.buffer.ByteBuf;

public abstract class CoverAdvancedRedstoneTransmitterBase extends CoverAdvancedWirelessRedstoneBase {

    protected boolean invert;

    public CoverAdvancedRedstoneTransmitterBase(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
    }

    public boolean isInverted() {
        return this.invert;
    }

    public CoverAdvancedRedstoneTransmitterBase setInverted(boolean inverted) {
        this.invert = inverted;
        return this;
    }

    @Override
    protected void initializeData() {
        super.initializeData();
        this.invert = false;
    }

    @Override
    protected void loadFromNbt(NBTBase nbt) {
        super.loadFromNbt(nbt);

        NBTTagCompound tag = (NBTTagCompound) nbt;
        invert = tag.getBoolean("invert");
    }

    @Override
    protected void readFromPacket(ByteArrayDataInput byteData) {
        super.readFromPacket(byteData);
        invert = byteData.readBoolean();
    }

    @Override
    protected @NotNull NBTBase saveDataToNbt() {
        NBTTagCompound tag = (NBTTagCompound) super.saveDataToNbt();
        tag.setBoolean("invert", invert);

        return tag;
    }

    @Override
    protected void writeDataToByteBuf(ByteBuf byteBuf) {
        super.writeToByteBuf(byteBuf);
        byteBuf.writeBoolean(invert);
    }

    private void unregisterSignal() {
        ICoverable coverable = coveredTile.get();
        if (coverable == null) return;
        final long hash = hashCoverCoords(coverable, coverSide);
        removeSignalAt(uuid, frequency, hash);
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
        invert = !invert;
        GTUtility
            .sendChatToPlayer(aPlayer, invert ? GTUtility.trans("054", "Inverted") : GTUtility.trans("055", "Normal"));
    }

    @Override
    public void preDataChanged(Cover newCover) {
        if (newCover instanceof CoverAdvancedRedstoneTransmitterBase newTransmitterCover
            && (frequency != newTransmitterCover.frequency || !Objects.equals(uuid, newTransmitterCover.uuid))) {
            unregisterSignal();
        }
    }

    // GUI stuff

    protected static abstract class AdvancedRedstoneTransmitterBaseUIFactory<C extends CoverAdvancedRedstoneTransmitterBase>
        extends AdvancedWirelessRedstoneBaseUIFactory<C> {

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
            builder.widget(
                TextWidget
                    .dynamicString(
                        getCoverString(
                            c -> c.isInverted() ? GTUtility.trans("INVERTED", "Inverted")
                                : GTUtility.trans("NORMAL", "Normal")))
                    .setSynced(false)
                    .setDefaultColor(COLOR_TEXT_GRAY.get())
                    .setPos(startX + spaceX * 10, 4 + startY + spaceY * getButtonRow()));
        }

        @Override
        protected void addUIForDataController(CoverDataControllerWidget<C> controller) {
            super.addUIForDataController(controller);
            controller.addFollower(
                CoverDataFollowerToggleButtonWidget.ofRedstone(),
                CoverAdvancedRedstoneTransmitterBase::isInverted,
                (coverData, state) -> {
                    coverData.setInverted(state);
                    return coverData;
                },
                widget -> widget.addTooltip(0, GTUtility.trans("NORMAL", "Normal"))
                    .addTooltip(1, GTUtility.trans("INVERTED", "Inverted"))
                    .setPos(spaceX * 9, spaceY * getButtonRow()));
        }
    }
}
