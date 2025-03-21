package gregtech.common.covers.redstone;

import java.util.Arrays;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import org.jetbrains.annotations.NotNull;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.util.GTUtility;
import gregtech.common.covers.Cover;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;
import io.netty.buffer.ByteBuf;

public abstract class CoverAdvancedRedstoneReceiverBase extends CoverAdvancedWirelessRedstoneBase {

    private GateMode mode;

    public CoverAdvancedRedstoneReceiverBase(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
    }

    public GateMode getGateMode() {
        return mode;
    }

    public CoverAdvancedRedstoneReceiverBase setMode(GateMode mode) {
        this.mode = mode;
        return this;
    }

    @Override
    protected void initializeData() {
        super.initializeData();
        this.mode = GateMode.AND;
    }

    @Override
    protected void loadFromNbt(NBTBase nbt) {
        super.loadFromNbt(nbt);

        NBTTagCompound tag = (NBTTagCompound) nbt;
        mode = GateMode.values()[tag.getByte("mode")];
    }

    @Override
    protected void readFromPacket(ByteArrayDataInput byteData) {
        super.readFromPacket(byteData);
        mode = GateMode.values()[byteData.readByte()];
    }

    @Override
    protected @NotNull NBTBase saveDataToNbt() {
        NBTTagCompound tag = (NBTTagCompound) super.saveDataToNbt();
        tag.setByte("mode", (byte) mode.ordinal());

        return tag;
    }

    @Override
    protected void writeDataToByteBuf(ByteBuf byteBuf) {
        super.writeToByteBuf(byteBuf);
        byteBuf.writeByte(mode.ordinal());
    }

    // GUI stuff

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new AdvancedRedstoneReceiverBaseUIFactory(buildContext).createWindow();
    }

    private static class AdvancedRedstoneReceiverBaseUIFactory
        extends AdvancedWirelessRedstoneBaseUIFactory<CoverAdvancedRedstoneReceiverBase> {

        public AdvancedRedstoneReceiverBaseUIFactory(CoverUIBuildContext buildContext) {
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
        protected CoverAdvancedRedstoneReceiverBase adaptCover(Cover cover) {
            if (cover instanceof CoverAdvancedRedstoneReceiverBase adaptedCover) {
                return adaptedCover;
            }
            return null;
        }

        @Override
        protected @NotNull CoverDataControllerWidget<CoverAdvancedRedstoneReceiverBase> getDataController() {
            return new CoverDataControllerWidget<>(this::getCover, getUIBuildContext());
        }

        @Override
        protected void addUIWidgets(ModularWindow.Builder builder) {
            super.addUIWidgets(builder);
            builder.widget(
                new TextWidget(GTUtility.trans("335", "Gate Mode")).setDefaultColor(COLOR_TEXT_GRAY.get())
                    .setPos(startX + spaceX * 5, 4 + startY + spaceY * 2));
        }

        @SuppressWarnings("PointlessArithmeticExpression")
        @Override
        protected void addUIForDataController(CoverDataControllerWidget<CoverAdvancedRedstoneReceiverBase> controller) {
            super.addUIForDataController(controller);
            controller
                .addFollower(
                    CoverDataFollowerToggleButtonWidget.ofDisableable(),
                    coverData -> coverData.getGateMode() == GateMode.AND,
                    (coverData, state) -> coverData.setMode(GateMode.AND),
                    widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_GATE_AND)
                        .addTooltip(GTUtility.trans("331", "AND Gate"))
                        .setPos(spaceX * 0, spaceY * 2))
                .addFollower(
                    CoverDataFollowerToggleButtonWidget.ofDisableable(),
                    coverData -> coverData.getGateMode() == GateMode.NAND,
                    (coverData, state) -> coverData.setMode(GateMode.NAND),
                    widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_GATE_NAND)
                        .addTooltip(GTUtility.trans("332", "NAND Gate"))
                        .setPos(spaceX * 1, spaceY * 2))
                .addFollower(
                    CoverDataFollowerToggleButtonWidget.ofDisableable(),
                    coverData -> coverData.getGateMode() == GateMode.OR,
                    (coverData, state) -> coverData.setMode(GateMode.OR),
                    widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_GATE_OR)
                        .addTooltip(GTUtility.trans("333", "OR Gate"))
                        .setPos(spaceX * 2, spaceY * 2))
                .addFollower(
                    CoverDataFollowerToggleButtonWidget.ofDisableable(),
                    coverData -> coverData.getGateMode() == GateMode.NOR,
                    (coverData, state) -> coverData.setMode(GateMode.NOR),
                    widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_GATE_NOR)
                        .addTooltip(GTUtility.trans("334", "NOR Gate"))
                        .setPos(spaceX * 3, spaceY * 2))
                .addFollower(
                    CoverDataFollowerToggleButtonWidget.ofDisableable(),
                    coverData -> coverData.getGateMode() == GateMode.SINGLE_SOURCE,
                    (coverData, state) -> coverData.setMode(GateMode.SINGLE_SOURCE),
                    widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_ANALOG)
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
}
