package gregtech.common.covers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.network.NetworkUtils;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BinaryEnumSyncValue;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.WidgetTree;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.gui.modularui2.CoverGuiData;
import gregtech.api.gui.modularui2.GTGuiTextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.util.CoverBehavior;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;
import gregtech.common.gui.modularui2.EnumRowBuilder;

public class CoverDoesWork extends CoverBehavior {

    private static final int FLAG_INVERTED = 0x1;
    private static final int FLAG_PROGRESS = 0x2;
    private static final int FLAG_ENABLED = 0x4;

    public CoverDoesWork(ITexture coverTexture) {
        super(coverTexture);
    }

    @Override
    public boolean isRedstoneSensitive(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        long aTimer) {
        return false;
    }

    @Override
    public int doCoverThings(ForgeDirection side, byte aInputRedstone, int aCoverID, int aCoverVariable,
        ICoverable aTileEntity, long aTimer) {
        if ((aTileEntity instanceof IMachineProgress mProgress)) {
            boolean inverted = isFlagSet(aCoverVariable, FLAG_INVERTED);
            int signal = 0;

            if (isFlagSet(aCoverVariable, FLAG_ENABLED)) {
                signal = inverted == mProgress.isAllowedToWork() ? 0 : 15;
            } else if (isFlagSet(aCoverVariable, FLAG_PROGRESS)) {
                signal = inverted == (mProgress.getMaxProgress() == 0) ? 0 : 15;
            } else {
                int tScale = mProgress.getMaxProgress() / 15;

                if (tScale > 0 && mProgress.hasThingsToDo()) {
                    signal = inverted ? (15 - mProgress.getProgress() / tScale) : (mProgress.getProgress() / tScale);
                } else {
                    signal = inverted ? 15 : 0;
                }
            }

            aTileEntity.setOutputRedstoneSignal(side, (byte) signal);
        } else {
            aTileEntity.setOutputRedstoneSignal(side, (byte) 0);
        }
        return aCoverVariable;
    }

    @Override
    public int onCoverScrewdriverclick(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        EntityPlayer aPlayer, float aX, float aY, float aZ) {
        aCoverVariable = (aCoverVariable + (aPlayer.isSneaking() ? -1 : 1)) % 6;
        if (aCoverVariable < 0) {
            aCoverVariable = 5;
        }
        switch (aCoverVariable) {
            case 0 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("018", "Normal"));
            // Progress scaled
            case 1 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("019", "Inverted"));
            // ^ inverted
            case 2 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("020", "Ready to work"));
            // Not Running
            case 3 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("021", "Not ready to work"));
            // Running
            case 4 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("028", "Machine Enabled"));
            // Enabled
            case 5 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("029", "Machine Disabled"));
            // Disabled
        }
        return aCoverVariable;
    }

    @Override
    public boolean letsEnergyIn(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsEnergyOut(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsFluidIn(ForgeDirection side, int aCoverID, int aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsFluidOut(ForgeDirection side, int aCoverID, int aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsItemsIn(ForgeDirection side, int aCoverID, int aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsItemsOut(ForgeDirection side, int aCoverID, int aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean manipulatesSidedRedstoneOutput(ForgeDirection side, int aCoverID, int aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public int getTickRate(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return 5;
    }

    // GUI stuff

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    protected String getGuiId() {
        return "cover.activity_detector";
    }

    @Override
    public void addUIWidgets(CoverGuiData guiData, PanelSyncManager syncManager, Flow column) {
        EnumSyncValue<DetectionMode> detectionModeSyncValue = new EnumSyncValue<>(
            DetectionMode.class,
            () -> getDetectionMode(guiData),
            mode -> setDetectionMode(mode, guiData, column));
        syncManager.syncValue("detection_mode", detectionModeSyncValue);
        BinaryEnumSyncValue<RedstoneMode> redstoneModeSyncValue = new BinaryEnumSyncValue<>(
            RedstoneMode.class,
            () -> getRedstoneMode(guiData),
            mode -> setRedstoneMode(mode, guiData, column));

        column.child(
            new Grid().marginLeft(WIDGET_MARGIN)
                .coverChildren()
                .minElementMarginRight(WIDGET_MARGIN)
                .minElementMarginBottom(2)
                .minElementMarginTop(0)
                .minElementMarginLeft(0)
                .alignment(Alignment.CenterLeft)
                .row(
                    new EnumRowBuilder<>(DetectionMode.class).value(detectionModeSyncValue)
                        .overlay(
                            GTGuiTextures.OVERLAY_BUTTON_PROGRESS,
                            GTGuiTextures.OVERLAY_BUTTON_CHECKMARK,
                            GTGuiTextures.OVERLAY_BUTTON_POWER_SWITCH_ON)
                        .build(),
                    IKey.dynamic(() -> {
                        DetectionMode mode = detectionModeSyncValue.getValue();
                        if (mode == DetectionMode.MACHINE_ENABLED) {
                            return GTUtility.trans("271", "Machine enabled");
                        } else if (mode == DetectionMode.MACHINE_IDLE) {
                            return GTUtility.trans("242", "Machine idle");
                        } else {
                            return GTUtility.trans("241", "Recipe progress");
                        }
                    })
                        .asWidget())
                .row(
                    new ToggleButton().value(redstoneModeSyncValue)
                        .overlay(true, GTGuiTextures.OVERLAY_BUTTON_REDSTONE_ON)
                        .overlay(false, GTGuiTextures.OVERLAY_BUTTON_REDSTONE_OFF)
                        .size(16),
                    IKey.dynamic(
                        () -> redstoneModeSyncValue.getValue() == RedstoneMode.INVERTED
                            ? GTUtility.trans("INVERTED", "Inverted")
                            : GTUtility.trans("NORMAL", "Normal"))
                        .asWidget()));
    }

    private enum DetectionMode {
        RECIPE_PROGRESS,
        MACHINE_IDLE,
        MACHINE_ENABLED
    }

    private DetectionMode getDetectionMode(CoverGuiData guiData) {
        int coverVariable = convert(getCoverData(guiData));
        if (isFlagSet(coverVariable, FLAG_PROGRESS)) {
            return DetectionMode.MACHINE_IDLE;
        } else if (isFlagSet(coverVariable, FLAG_ENABLED)) {
            return DetectionMode.MACHINE_ENABLED;
        } else {
            return DetectionMode.RECIPE_PROGRESS;
        }
    }

    private void setDetectionMode(DetectionMode mode, CoverGuiData guiData, Flow column) {
        int coverVariable = convert(getCoverData(guiData));
        final int newCoverVariable = switch (mode) {
            case RECIPE_PROGRESS -> (coverVariable & ~FLAG_ENABLED) & ~FLAG_PROGRESS;
            case MACHINE_IDLE -> (coverVariable & ~FLAG_ENABLED) | FLAG_PROGRESS;
            case MACHINE_ENABLED -> (coverVariable & ~FLAG_PROGRESS) | FLAG_ENABLED;
        };
        if (coverVariable != newCoverVariable) {
            guiData.setCoverData(new ISerializableObject.LegacyCoverData(newCoverVariable));
            if (NetworkUtils.isClient()) {
                WidgetTree.resize(column);
            }
        }
    }

    private enum RedstoneMode {
        NORMAL,
        INVERTED
    }

    private RedstoneMode getRedstoneMode(CoverGuiData guiData) {
        int coverVariable = convert(getCoverData(guiData));
        return isFlagSet(coverVariable, FLAG_INVERTED) ? RedstoneMode.INVERTED : RedstoneMode.NORMAL;
    }

    private void setRedstoneMode(RedstoneMode mode, CoverGuiData guiData, Flow column) {
        int coverVariable = convert(getCoverData(guiData));
        int newCoverVariable = switch (mode) {
            case NORMAL -> coverVariable & ~FLAG_INVERTED;
            case INVERTED -> coverVariable | FLAG_INVERTED;
        };
        if (coverVariable != newCoverVariable) {
            guiData.setCoverData(new ISerializableObject.LegacyCoverData(newCoverVariable));
            if (NetworkUtils.isClient()) {
                WidgetTree.resize(column);
            }
        }
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new DoesWorkUIFactory(buildContext).createWindow();
    }

    private static boolean isFlagSet(int coverVariable, int flag) {
        return (coverVariable & flag) == flag;
    }

    private class DoesWorkUIFactory extends UIFactory {

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;

        public DoesWorkUIFactory(CoverUIBuildContext buildContext) {
            super(buildContext);
        }

        @SuppressWarnings("PointlessArithmeticExpression")
        @Override
        protected void addUIWidgets(ModularWindow.Builder builder) {
            builder
                .widget(
                    new CoverDataControllerWidget.CoverDataIndexedControllerWidget_ToggleButtons<>(
                        this::getCoverData,
                        this::setCoverData,
                        CoverDoesWork.this,
                        (id, coverData) -> isEnabled(id, convert(coverData)),
                        (id, coverData) -> new ISerializableObject.LegacyCoverData(
                            getNewCoverVariable(id, convert(coverData))))
                                .addToggleButton(
                                    0,
                                    CoverDataFollowerToggleButtonWidget.ofDisableable(),
                                    widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_PROGRESS)
                                        .setPos(spaceX * 0, spaceY * 0))
                                .addToggleButton(
                                    1,
                                    CoverDataFollowerToggleButtonWidget.ofDisableable(),
                                    widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_CHECKMARK)
                                        .setPos(spaceX * 1, spaceY * 0))
                                .addToggleButton(
                                    2,
                                    CoverDataFollowerToggleButtonWidget.ofDisableable(),
                                    widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_POWER_SWITCH_ON)
                                        .setPos(spaceX * 2, spaceY * 0))
                                .addToggleButton(
                                    3,
                                    CoverDataFollowerToggleButtonWidget.ofRedstone(),
                                    widget -> widget.setPos(spaceX * 0, spaceY * 1))
                                .setPos(startX, startY))
                .widget(TextWidget.dynamicString(() -> {
                    int coverVariable = convert(getCoverData());

                    if (isFlagSet(coverVariable, FLAG_ENABLED)) {
                        return GTUtility.trans("271", "Machine enabled");
                    } else if (isFlagSet(coverVariable, FLAG_PROGRESS)) {
                        return GTUtility.trans("242", "Machine idle");
                    } else {
                        return GTUtility.trans("241", "Recipe progress");
                    }

                })
                    .setSynced(false)
                    .setDefaultColor(COLOR_TEXT_GRAY.get())
                    .setPos(startX + spaceX * 3, 4 + startY + spaceY * 0))
                .widget(
                    TextWidget
                        .dynamicString(
                            () -> isFlagSet(convert(getCoverData()), FLAG_INVERTED)
                                ? GTUtility.trans("INVERTED", "Inverted")
                                : GTUtility.trans("NORMAL", "Normal"))
                        .setSynced(false)
                        .setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 3, 4 + startY + spaceY * 1));
        }

        private int getNewCoverVariable(int id, int coverVariable) {
            switch (id) {
                case 0 -> {
                    return (coverVariable & ~FLAG_ENABLED) & ~FLAG_PROGRESS;
                }
                case 1 -> {
                    return (coverVariable & ~FLAG_ENABLED) | FLAG_PROGRESS;
                }
                case 2 -> {
                    return (coverVariable & ~FLAG_PROGRESS) | FLAG_ENABLED;
                }
                case 3 -> {
                    if (isFlagSet(coverVariable, FLAG_INVERTED)) {
                        return coverVariable & ~FLAG_INVERTED;
                    } else {
                        return coverVariable | FLAG_INVERTED;
                    }
                }
            }
            return coverVariable;
        }

        private boolean isEnabled(int id, int coverVariable) {
            return switch (id) {
                case 0 -> !isFlagSet(coverVariable, FLAG_PROGRESS) && !isFlagSet(coverVariable, FLAG_ENABLED);
                case 1 -> isFlagSet(coverVariable, FLAG_PROGRESS);
                case 2 -> isFlagSet(coverVariable, FLAG_ENABLED);
                case 3 -> isFlagSet(coverVariable, FLAG_INVERTED);
                default -> true;
            };
        }
    }
}
