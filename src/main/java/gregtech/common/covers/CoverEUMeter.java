package gregtech.common.covers;

import java.util.function.Function;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicBatteryBuffer;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerCycleButtonWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerNumericWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;
import io.netty.buffer.ByteBuf;

public class CoverEUMeter extends CoverBehaviorBase<CoverEUMeter.EUMeterData> {

    public CoverEUMeter(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
    }

    public EnergyType getType() {
        return this.coverData.type;
    }

    public CoverEUMeter setType(EnergyType type) {
        this.coverData.type = type;
        return this;
    }

    public boolean isInverted() {
        return this.coverData.inverted;
    }

    public CoverEUMeter setInverted(boolean inverted) {
        this.coverData.inverted = inverted;
        return this;
    }

    public long getThreshold() {
        return this.coverData.threshold;
    }

    public CoverEUMeter setThresdhold(long threshold) {
        this.coverData.threshold = threshold;
        return this;
    }

    @Override
    protected EUMeterData initializeData() {
        return new CoverEUMeter.EUMeterData();
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        {
            ICoverable coverable = coveredTile.get();
            if (coverable == null) {
                return;
            }
            final long stored = coverData.type.getTileEntityStoredEnergy(coverable);
            final long capacity = coverData.type.getTileEntityEnergyCapacity(coverable);

            byte redstoneSignal;

            if (stored == 0L) {
                // nothing
                redstoneSignal = 0;
            } else if (stored >= capacity) {
                // full
                redstoneSignal = 15;
            } else {
                // 1-14 range
                redstoneSignal = (byte) (1 + (14 * stored) / capacity);
            }

            if (coverData.inverted) {
                redstoneSignal = (byte) (15 - redstoneSignal);
            }

            if (coverData.threshold > 0) {
                if (coverData.inverted && stored >= coverData.threshold) {
                    redstoneSignal = 0;
                } else if (!coverData.inverted && stored < coverData.threshold) {
                    redstoneSignal = 0;
                }
            }

            coverable.setOutputRedstoneSignal(coverSide, redstoneSignal);
        }
    }

    @Override
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        int num = (coverData.getNum() + (aPlayer.isSneaking() ? -1 : 1) + EnergyType.values().length * 2)
            % (EnergyType.values().length * 2);
        switch (num) {
            case 0 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("031", "Normal Universal Storage"));
            case 1 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("032", "Inverted Universal Storage"));
            case 2 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("033", "Normal Electricity Storage"));
            case 3 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("034", "Inverted Electricity Storage"));
            case 4 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("035", "Normal Steam Storage"));
            case 5 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("036", "Inverted Steam Storage"));
            case 6 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("037", "Normal Average Electric Input"));
            case 7 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("038", "Inverted Average Electric Input"));
            case 8 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("039", "Normal Average Electric Output"));
            case 9 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("040", "Inverted Average Electric Output"));
            case 10 -> GTUtility
                .sendChatToPlayer(aPlayer, GTUtility.trans("041", "Normal Electricity Storage(Including Batteries)"));
            case 11 -> GTUtility
                .sendChatToPlayer(aPlayer, GTUtility.trans("042", "Inverted Electricity Storage(Including Batteries)"));
        }
        coverData.setNum(num);
    }

    // region Static Result Methods
    @Override
    public boolean isRedstoneSensitive(long aTimer) {
        return false;
    }

    @Override
    public boolean letsEnergyIn() {
        return true;
    }

    @Override
    public boolean letsEnergyOut() {
        return true;
    }

    @Override
    public boolean letsFluidIn(Fluid aFluid) {
        return true;
    }

    @Override
    public boolean letsFluidOut(Fluid aFluid) {
        return true;
    }

    @Override
    public boolean letsItemsIn(int aSlot) {
        return true;
    }

    @Override
    public boolean letsItemsOut(int aSlot) {
        return true;
    }

    @Override
    public boolean manipulatesSidedRedstoneOutput() {
        return true;
    }

    @Override
    public int getMinimumTickRate() {
        return 20;
    }
    // endregion

    // region GUI Stuff
    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new EUMeterUIFactory(buildContext).createWindow();
    }

    private class EUMeterUIFactory extends UIFactory {

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;

        public EUMeterUIFactory(CoverUIBuildContext buildContext) {
            super(buildContext);
        }

        @Override
        protected CoverEUMeter adaptCover(Cover cover) {
            if (cover instanceof CoverEUMeter adapterCover) {
                return adapterCover;
            }
            return null;
        }

        @SuppressWarnings("PointlessArithmeticExpression")
        @Override
        protected void addUIWidgets(ModularWindow.Builder builder) {
            final String INVERTED = GTUtility.trans("INVERTED", "Inverted");
            final String NORMAL = GTUtility.trans("NORMAL", "Normal");

            final CoverDataFollowerNumericWidget<CoverEUMeter> numericWidget = new CoverDataFollowerNumericWidget<>();

            builder
                .widget(
                    new CoverDataControllerWidget<>(this::adaptCover, getUIBuildContext())
                        .addFollower(
                            new CoverDataFollowerCycleButtonWidget<>(),
                            coverData -> coverData.getType()
                                .ordinal(),
                            (coverData, state) -> coverData.setType(EnergyType.getEnergyType(state)),
                            widget -> widget.setLength(EnergyType.values().length)
                                .addTooltip(
                                    state -> EnergyType.getEnergyType(state)
                                        .getTooltip())
                                .setStaticTexture(GTUITextures.OVERLAY_BUTTON_CYCLIC)
                                .setPos(spaceX * 0, spaceY * 0))
                        .addFollower(
                            CoverDataFollowerToggleButtonWidget.ofRedstone(),
                            CoverEUMeter::isInverted,
                            CoverEUMeter::setInverted,
                            widget -> widget.addTooltip(0, NORMAL)
                                .addTooltip(1, INVERTED)
                                .setPos(spaceX * 0, spaceY * 1))
                        .addFollower(
                            numericWidget,
                            coverData -> (double) coverData.getThreshold(),
                            (coverData, state) -> coverData.setThresdhold(state.longValue()),
                            widget -> widget.setScrollValues(1000, 100, 100000)
                                .setFocusOnGuiOpen(true)
                                .setPos(spaceX * 0, spaceY * 2 + 2)
                                .setSize(spaceX * 8, 12))
                        .setPos(startX, startY))
                .widget(
                    new TextWidget()
                        .setStringSupplier(() -> getCoverData() != null ? getCoverData().type.getTitle() : "")
                        .setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX, 4 + startY))
                .widget(
                    new TextWidget()
                        .setStringSupplier(
                            () -> getCoverData() != null ? getCoverData().inverted ? INVERTED : NORMAL : "")
                        .setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX, 4 + startY + spaceY))
                .widget(
                    new TextWidget(GTUtility.trans("222.1", "Energy threshold")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX, startY + spaceY * 3 + 4))

                .widget(
                    new FakeSyncWidget.LongSyncer(
                        () -> getCoverData() != null
                            ? getCoverData().type.getTileEntityEnergyCapacity(getUIBuildContext().getTile())
                            : Long.MAX_VALUE,
                        value -> numericWidget.setMaxValue(value)));
        }
    }

    // endregion

    public static class EUMeterData implements ISerializableObject {

        private EnergyType type;
        private boolean inverted;
        /**
         * The special value {@code 0} means threshold check is disabled.
         */
        private long threshold;

        public EUMeterData() {
            type = EnergyType.UNIVERSAL_STORAGE;
            inverted = false;
            threshold = 0;
        }

        public EUMeterData(EnergyType type, boolean inverted, long threshold) {
            this.type = type;
            this.inverted = inverted;
            this.threshold = threshold;
        }

        public int getNum() {
            return type.ordinal() * 2 + (inverted ? 1 : 0);
        }

        public void setNum(int num) {
            type = EnergyType.getEnergyType(num / 2);
            inverted = num % 2 == 1;
        }

        @Nonnull
        @Override
        public ISerializableObject copy() {
            return new EUMeterData(type, inverted, threshold);
        }

        @Nonnull
        @Override
        public NBTBase saveDataToNBT() {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("typeOrdinal", type.ordinal());
            tag.setBoolean("inverted", inverted);
            tag.setLong("threshold", threshold);
            return tag;
        }

        @Override
        public void writeToByteBuf(ByteBuf aBuf) {
            aBuf.writeInt(type.ordinal());
            aBuf.writeBoolean(inverted);
            aBuf.writeLong(threshold);
        }

        @Override
        public void loadDataFromNBT(NBTBase aNBT) {
            NBTTagCompound tag = (NBTTagCompound) aNBT;
            int typeOrdinal = tag.getInteger("typeOrdinal");
            type = EnergyType.getEnergyType(typeOrdinal);
            inverted = tag.getBoolean("inverted");
            threshold = tag.getLong("threshold");
        }

        @Override
        public void readFromPacket(ByteArrayDataInput aBuf) {
            int typeOrdinal = aBuf.readInt();
            type = EnergyType.getEnergyType(typeOrdinal);
            inverted = aBuf.readBoolean();
            threshold = aBuf.readLong();
        }
    }

    public enum EnergyType {

        UNIVERSAL_STORAGE(GTUtility.trans("301", "Universal"), GTUtility.trans("256", "Universal Storage"),
            ICoverable::getUniversalEnergyStored, ICoverable::getUniversalEnergyCapacity),
        ELECTRICITY_STORAGE(GTUtility.trans("302", "Int. EU"), GTUtility.trans("257", "Electricity Storage"),
            ICoverable::getStoredEU, ICoverable::getEUCapacity),
        STEAM_STORAGE(GTUtility.trans("303", "Steam"), GTUtility.trans("258", "Steam Storage"),
            ICoverable::getStoredSteam, ICoverable::getSteamCapacity),
        AVERAGE_ELECTRIC_INPUT(GTUtility.trans("304", "Avg. Input"), GTUtility.trans("259", "Average Electric Input"),
            ICoverable::getAverageElectricInput, (te) -> te.getInputVoltage() * te.getInputAmperage()),
        AVERAGE_ELECTRIC_OUTPUT(GTUtility.trans("305", "Avg. Output"),
            GTUtility.trans("260", "Average Electric Output"), ICoverable::getAverageElectricOutput,
            (te) -> te.getOutputVoltage() * te.getOutputAmperage()),
        ELECTRICITY_STORAGE_INCLUDING_BATTERIES(GTUtility.trans("306", "EU stored"),
            GTUtility.trans("261", "Electricity Storage(Including Batteries)"), (te) -> {
                if (te instanceof IGregTechTileEntity) {
                    IMetaTileEntity mte = ((IGregTechTileEntity) te).getMetaTileEntity();
                    if (mte instanceof MTEBasicBatteryBuffer buffer) {
                        return buffer.getStoredEnergy()[0];
                    }
                }
                return te.getStoredEU();
            }, (te) -> {
                if (te instanceof IGregTechTileEntity) {
                    IMetaTileEntity mte = ((IGregTechTileEntity) te).getMetaTileEntity();
                    if (mte instanceof MTEBasicBatteryBuffer buffer) {
                        return buffer.getStoredEnergy()[1];
                    }
                }
                return te.getEUCapacity();
            });

        private final String title;
        private final String tooltip;
        private final Function<ICoverable, Long> getTileEntityStoredEnergyFunc;
        private final Function<ICoverable, Long> getTileEntityEnergyCapacityFunc;

        EnergyType(String title, String tooltip, Function<ICoverable, Long> getTileEntityStoredEnergyFunc,
            Function<ICoverable, Long> getTileEntityEnergyCapacityFunc) {
            this.title = title;
            this.tooltip = tooltip;
            this.getTileEntityStoredEnergyFunc = getTileEntityStoredEnergyFunc;
            this.getTileEntityEnergyCapacityFunc = getTileEntityEnergyCapacityFunc;
        }

        public String getTitle() {
            return title;
        }

        public String getTooltip() {
            return tooltip;
        }

        public long getTileEntityStoredEnergy(ICoverable coverable) {
            return getTileEntityStoredEnergyFunc.apply(coverable);
        }

        public long getTileEntityEnergyCapacity(ICoverable coverable) {
            return getTileEntityEnergyCapacityFunc.apply(coverable);
        }

        public EnergyType getNext() {
            return values()[(ordinal() + 1) % values().length];
        }

        public static EnergyType getEnergyType(int ordinal) {
            if (ordinal < 0 || values().length <= ordinal) {
                ordinal = 0;
            }
            return values()[ordinal];
        }
    }
}
