package gregtech.common.covers.redstone;

import java.util.Arrays;
import java.util.UUID;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.GT_CoverUIBuildContext;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollower_ToggleButtonWidget;
import io.netty.buffer.ByteBuf;

public abstract class GT_Cover_AdvancedRedstoneReceiverBase
    extends GT_Cover_AdvancedWirelessRedstoneBase<GT_Cover_AdvancedRedstoneReceiverBase.ReceiverData> {

    public GT_Cover_AdvancedRedstoneReceiverBase(ITexture coverTexture) {
        super(ReceiverData.class, coverTexture);
    }

    @Override
    public ReceiverData createDataObject() {
        return new ReceiverData();
    }

    @Override
    public ReceiverData createDataObject(int aLegacyData) {
        return createDataObject();
    }

    // GUI stuff

    @Override
    public ModularWindow createWindow(GT_CoverUIBuildContext buildContext) {
        return new AdvancedRedstoneReceiverBaseUIFactory(buildContext).createWindow();
    }

    private class AdvancedRedstoneReceiverBaseUIFactory extends AdvancedWirelessRedstoneBaseUIFactory {

        public AdvancedRedstoneReceiverBaseUIFactory(GT_CoverUIBuildContext buildContext) {
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
            return false;
        }

        @Override
        protected void addUIWidgets(ModularWindow.Builder builder) {
            super.addUIWidgets(builder);
            builder.widget(
                new TextWidget(GT_Utility.trans("335", "Gate Mode")).setDefaultColor(COLOR_TEXT_GRAY.get())
                    .setPos(startX + spaceX * 5, 4 + startY + spaceY * 2));
        }

        @SuppressWarnings("PointlessArithmeticExpression")
        @Override
        protected void addUIForDataController(CoverDataControllerWidget<ReceiverData> controller) {
            super.addUIForDataController(controller);
            controller.addFollower(
                CoverDataFollower_ToggleButtonWidget.ofDisableable(),
                coverData -> coverData.mode == GateMode.AND,
                (coverData, state) -> {
                    coverData.mode = GateMode.AND;
                    return coverData;
                },
                widget -> widget.setStaticTexture(GT_UITextures.OVERLAY_BUTTON_GATE_AND)
                    .addTooltip(GT_Utility.trans("331", "AND Gate"))
                    .setPos(spaceX * 0, spaceY * 2))
                .addFollower(
                    CoverDataFollower_ToggleButtonWidget.ofDisableable(),
                    coverData -> coverData.mode == GateMode.NAND,
                    (coverData, state) -> {
                        coverData.mode = GateMode.NAND;
                        return coverData;
                    },
                    widget -> widget.setStaticTexture(GT_UITextures.OVERLAY_BUTTON_GATE_NAND)
                        .addTooltip(GT_Utility.trans("332", "NAND Gate"))
                        .setPos(spaceX * 1, spaceY * 2))
                .addFollower(
                    CoverDataFollower_ToggleButtonWidget.ofDisableable(),
                    coverData -> coverData.mode == GateMode.OR,
                    (coverData, state) -> {
                        coverData.mode = GateMode.OR;
                        return coverData;
                    },
                    widget -> widget.setStaticTexture(GT_UITextures.OVERLAY_BUTTON_GATE_OR)
                        .addTooltip(GT_Utility.trans("333", "OR Gate"))
                        .setPos(spaceX * 2, spaceY * 2))
                .addFollower(
                    CoverDataFollower_ToggleButtonWidget.ofDisableable(),
                    coverData -> coverData.mode == GateMode.NOR,
                    (coverData, state) -> {
                        coverData.mode = GateMode.NOR;
                        return coverData;
                    },
                    widget -> widget.setStaticTexture(GT_UITextures.OVERLAY_BUTTON_GATE_NOR)
                        .addTooltip(GT_Utility.trans("334", "NOR Gate"))
                        .setPos(spaceX * 3, spaceY * 2))
                .addFollower(
                    CoverDataFollower_ToggleButtonWidget.ofDisableable(),
                    coverData -> coverData.mode == GateMode.SINGLE_SOURCE,
                    (coverData, state) -> {
                        coverData.mode = GateMode.SINGLE_SOURCE;
                        return coverData;
                    },
                    widget -> widget.setStaticTexture(GT_UITextures.OVERLAY_BUTTON_ANALOG)
                        .addTooltips(
                            Arrays.asList(
                                "ANALOG Mode",
                                "Only use this mode with ONE transmitter in total,",
                                "no logic involved"))
                        .setPos(spaceX * 4, spaceY * 2));
        }
    }

    public enum GateMode {
        AND,
        NAND,
        OR,
        NOR,
        SINGLE_SOURCE
    }

    public static class ReceiverData extends GT_Cover_AdvancedWirelessRedstoneBase.WirelessData {

        private GateMode mode;

        public ReceiverData(int frequency, UUID uuid, GateMode mode) {
            super(frequency, uuid);
            this.mode = mode;
        }

        public ReceiverData() {
            this(0, null, GateMode.AND);
        }

        public GateMode getGateMode() {
            return mode;
        }

        @Nonnull
        @Override
        public ISerializableObject copy() {
            return new ReceiverData(frequency, uuid, mode);
        }

        @Nonnull
        @Override
        public NBTBase saveDataToNBT() {
            NBTTagCompound tag = (NBTTagCompound) super.saveDataToNBT();
            tag.setByte("mode", (byte) mode.ordinal());

            return tag;
        }

        @Override
        public void writeToByteBuf(ByteBuf aBuf) {
            super.writeToByteBuf(aBuf);
            aBuf.writeByte(mode.ordinal());
        }

        @Override
        public void loadDataFromNBT(NBTBase aNBT) {
            super.loadDataFromNBT(aNBT);

            NBTTagCompound tag = (NBTTagCompound) aNBT;
            mode = GateMode.values()[tag.getByte("mode")];
        }

        @Nonnull
        @Override
        public ISerializableObject readFromPacket(ByteArrayDataInput aBuf, EntityPlayerMP aPlayer) {
            super.readFromPacket(aBuf, aPlayer);
            mode = GateMode.values()[aBuf.readByte()];

            return this;
        }
    }
}
