package gregtech.common.covers;

import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.math.MathExpression;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.GT_CoverUIBuildContext;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicBatteryBuffer;
import gregtech.api.util.GT_CoverBehaviorBase;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollower_CycleButtonWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollower_TextFieldWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollower_ToggleButtonWidget;
import io.netty.buffer.ByteBuf;

public class GT_Cover_EUMeter extends GT_CoverBehaviorBase<GT_Cover_EUMeter.EUMeterData> {

    /**
     * @deprecated use {@link #GT_Cover_EUMeter(ITexture coverTexture)} instead
     */
    @Deprecated
    public GT_Cover_EUMeter() {
        this(null);
    }

    public GT_Cover_EUMeter(ITexture coverTexture) {
        super(EUMeterData.class, coverTexture);
    }

    @Override
    public EUMeterData createDataObject(int aLegacyData) {
        return new EUMeterData(aLegacyData, 0);
    }

    @Override
    public EUMeterData createDataObject() {
        return new EUMeterData();
    }

    @Override
    protected EUMeterData doCoverThingsImpl(ForgeDirection aSide, byte aInputRedstone, int aCoverID,
        EUMeterData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        final long stored = aCoverVariable.type.getTileEntityStoredEnergy(aTileEntity);
        final long capacity = aCoverVariable.type.getTileEntityEnergyCapacity(aTileEntity);

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

        if (aCoverVariable.inverted) {
            redstoneSignal = (byte) (15 - redstoneSignal);
        }

        if (aCoverVariable.threshold > 0) {
            if (aCoverVariable.inverted && stored >= aCoverVariable.threshold) {
                redstoneSignal = 0;
            } else if (!aCoverVariable.inverted && stored < aCoverVariable.threshold) {
                redstoneSignal = 0;
            }
        }

        aTileEntity.setOutputRedstoneSignal(aSide, redstoneSignal);
        return aCoverVariable;
    }

    @Override
    protected EUMeterData onCoverScrewdriverClickImpl(ForgeDirection aSide, int aCoverID, EUMeterData aCoverVariable,
        ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        int num = (aCoverVariable.getNum() + (aPlayer.isSneaking() ? -1 : 1) + EnergyType.values().length * 2)
            % (EnergyType.values().length * 2);
        switch (num) {
            case 0 -> GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("031", "Normal Universal Storage"));
            case 1 -> GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("032", "Inverted Universal Storage"));
            case 2 -> GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("033", "Normal Electricity Storage"));
            case 3 -> GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("034", "Inverted Electricity Storage"));
            case 4 -> GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("035", "Normal Steam Storage"));
            case 5 -> GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("036", "Inverted Steam Storage"));
            case 6 -> GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("037", "Normal Average Electric Input"));
            case 7 -> GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("038", "Inverted Average Electric Input"));
            case 8 -> GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("039", "Normal Average Electric Output"));
            case 9 -> GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("040", "Inverted Average Electric Output"));
            case 10 -> GT_Utility
                .sendChatToPlayer(aPlayer, GT_Utility.trans("041", "Normal Electricity Storage(Including Batteries)"));
            case 11 -> GT_Utility.sendChatToPlayer(
                aPlayer,
                GT_Utility.trans("042", "Inverted Electricity Storage(Including Batteries)"));
        }
        aCoverVariable.setNum(num);
        return aCoverVariable;
    }

    // region Static Result Methods
    @Override
    protected boolean isRedstoneSensitiveImpl(ForgeDirection aSide, int aCoverID, EUMeterData aCoverVariable,
        ICoverable aTileEntity, long aTimer) {
        return false;
    }

    @Override
    protected boolean letsEnergyInImpl(ForgeDirection aSide, int aCoverID, EUMeterData aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsEnergyOutImpl(ForgeDirection aSide, int aCoverID, EUMeterData aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsFluidInImpl(ForgeDirection aSide, int aCoverID, EUMeterData aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsFluidOutImpl(ForgeDirection aSide, int aCoverID, EUMeterData aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsItemsInImpl(ForgeDirection aSide, int aCoverID, EUMeterData aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsItemsOutImpl(ForgeDirection aSide, int aCoverID, EUMeterData aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean manipulatesSidedRedstoneOutputImpl(ForgeDirection aSide, int aCoverID, EUMeterData aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected int getTickRateImpl(ForgeDirection aSide, int aCoverID, EUMeterData aCoverVariable,
        ICoverable aTileEntity) {
        return 20;
    }
    // endregion

    // region GUI Stuff
    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public boolean useModularUI() {
        return true;
    }

    @Override
    public ModularWindow createWindow(GT_CoverUIBuildContext buildContext) {
        return new EUMeterUIFactory(buildContext).createWindow();
    }

    private class EUMeterUIFactory extends UIFactory {

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;

        public EUMeterUIFactory(GT_CoverUIBuildContext buildContext) {
            super(buildContext);
        }

        @SuppressWarnings("PointlessArithmeticExpression")
        @Override
        protected void addUIWidgets(ModularWindow.Builder builder) {
            final String INVERTED = GT_Utility.trans("INVERTED", "Inverted");
            final String NORMAL = GT_Utility.trans("NORMAL", "Normal");

            builder.widget(
                new CoverDataControllerWidget<>(this::getCoverData, this::setCoverData, GT_Cover_EUMeter.this)
                    .addFollower(
                        new CoverDataFollower_CycleButtonWidget<>(),
                        coverData -> coverData.type.ordinal(),
                        (coverData, state) -> {
                            coverData.type = EnergyType.getEnergyType(state);
                            return coverData;
                        },
                        widget -> widget.setLength(EnergyType.values().length)
                            .addTooltip(
                                state -> EnergyType.getEnergyType(state)
                                    .getTooltip())
                            .setStaticTexture(GT_UITextures.OVERLAY_BUTTON_CYCLIC)
                            .setPos(spaceX * 0, spaceY * 0))
                    .addFollower(
                        CoverDataFollower_ToggleButtonWidget.ofRedstone(),
                        coverData -> coverData.inverted,
                        (coverData, state) -> {
                            coverData.inverted = state;
                            return coverData;
                        },
                        widget -> widget.addTooltip(0, NORMAL)
                            .addTooltip(1, INVERTED)
                            .setPos(spaceX * 0, spaceY * 1))
                    .addFollower(
                        new CoverDataFollower_TextFieldWidget<>(),
                        coverData -> String.valueOf(coverData.threshold),
                        (coverData, state) -> {
                            coverData.threshold = (long) MathExpression.parseMathExpression(state);
                            return coverData;
                        },
                        widget -> widget.setOnScrollNumbersLong(1000, 100, 100000)
                            .setNumbersLong(() -> 0L, () -> Long.MAX_VALUE)
                            .setFocusOnGuiOpen(true)
                            .setPos(spaceX * 0, spaceY * 2 + 2)
                            .setSize(spaceX * 8, 12))
                    .setPos(startX, startY))
                .widget(
                    TextWidget.dynamicString(() -> getCoverData() != null ? getCoverData().type.getTitle() : "")
                        .setSynced(false)
                        .setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX, 4 + startY))
                .widget(
                    TextWidget
                        .dynamicString(() -> getCoverData() != null ? getCoverData().inverted ? INVERTED : NORMAL : "")
                        .setSynced(false)
                        .setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX, 4 + startY + spaceY))
                .widget(
                    new TextWidget(GT_Utility.trans("222.1", "Energy threshold")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX, startY + spaceY * 3 + 4));
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

        public EUMeterData(int num, long threshold) {
            this();
            this.setNum(num);
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

        @Nonnull
        @Override
        public ISerializableObject readFromPacket(ByteArrayDataInput aBuf, @Nullable EntityPlayerMP aPlayer) {
            int typeOrdinal = aBuf.readInt();
            type = EnergyType.getEnergyType(typeOrdinal);
            inverted = aBuf.readBoolean();
            threshold = aBuf.readLong();
            return this;
        }
    }

    private enum EnergyType {

        UNIVERSAL_STORAGE(GT_Utility.trans("301", "Universal"), GT_Utility.trans("256", "Universal Storage"),
            ICoverable::getUniversalEnergyStored, ICoverable::getUniversalEnergyCapacity),
        ELECTRICITY_STORAGE(GT_Utility.trans("302", "Int. EU"), GT_Utility.trans("257", "Electricity Storage"),
            ICoverable::getStoredEU, ICoverable::getEUCapacity),
        STEAM_STORAGE(GT_Utility.trans("303", "Steam"), GT_Utility.trans("258", "Steam Storage"),
            ICoverable::getStoredSteam, ICoverable::getSteamCapacity),
        AVERAGE_ELECTRIC_INPUT(GT_Utility.trans("304", "Avg. Input"), GT_Utility.trans("259", "Average Electric Input"),
            ICoverable::getAverageElectricInput, (te) -> te.getInputVoltage() * te.getInputAmperage()),
        AVERAGE_ELECTRIC_OUTPUT(GT_Utility.trans("305", "Avg. Output"),
            GT_Utility.trans("260", "Average Electric Output"), ICoverable::getAverageElectricOutput,
            (te) -> te.getOutputVoltage() * te.getOutputAmperage()),
        ELECTRICITY_STORAGE_INCLUDING_BATTERIES(GT_Utility.trans("306", "EU stored"),
            GT_Utility.trans("261", "Electricity Storage(Including Batteries)"), (te) -> {
                if (te instanceof IGregTechTileEntity) {
                    IMetaTileEntity mte = ((IGregTechTileEntity) te).getMetaTileEntity();
                    if (mte instanceof GT_MetaTileEntity_BasicBatteryBuffer buffer) {
                        return buffer.getStoredEnergy()[0];
                    }
                }
                return te.getStoredEU();
            }, (te) -> {
                if (te instanceof IGregTechTileEntity) {
                    IMetaTileEntity mte = ((IGregTechTileEntity) te).getMetaTileEntity();
                    if (mte instanceof GT_MetaTileEntity_BasicBatteryBuffer buffer) {
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

        public long getTileEntityStoredEnergy(ICoverable aTileEntity) {
            return getTileEntityStoredEnergyFunc.apply(aTileEntity);
        }

        public long getTileEntityEnergyCapacity(ICoverable aTileEntity) {
            return getTileEntityEnergyCapacityFunc.apply(aTileEntity);
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
