package gregtech.common.covers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.gui.modularui2.CoverGuiData;
import gregtech.api.gui.modularui2.GTGuiTextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.covers.IControlsWorkCover;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.util.CoverBehavior;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;
import gregtech.common.gui.modularui2.LinkedBoolValue;

public class CoverControlsWork extends CoverBehavior implements IControlsWorkCover {

    public CoverControlsWork(ITexture coverTexture) {
        super(coverTexture);
    }

    @Override
    public int doCoverThings(ForgeDirection side, byte aInputRedstone, int aCoverID, int aCoverVariable,
        ICoverable aTileEntity, long aTimer) {
        if (!makeSureOnlyOne(side, aTileEntity)) return 0;
        if (aTileEntity instanceof IMachineProgress machine) {
            if (aCoverVariable < 2) {
                if ((aInputRedstone > 0) == (aCoverVariable == 0)) {
                    if (!machine.isAllowedToWork()) machine.enableWorking();
                } else if (machine.isAllowedToWork()) machine.disableWorking();
                machine.setWorkDataValue(aInputRedstone);
            } else if (aCoverVariable == 2) {
                machine.disableWorking();
            } else {
                if (machine.wasShutdown() && machine.getLastShutDownReason()
                    .wasCritical()) {
                    machine.disableWorking();
                    if (!mPlayerNotified) {
                        EntityPlayer player = lastPlayer == null ? null : lastPlayer.get();
                        if (player != null) {
                            lastPlayer = null;
                            mPlayerNotified = true;
                            GTUtility.sendChatToPlayer(
                                player,
                                aTileEntity.getInventoryName() + "at "
                                    + String.format(
                                        "(%d,%d,%d)",
                                        aTileEntity.getXCoord(),
                                        aTileEntity.getYCoord(),
                                        aTileEntity.getZCoord())
                                    + " shut down.");
                        }
                    }
                    return 2;
                } else {
                    return 3 + doCoverThings(side, aInputRedstone, aCoverID, aCoverVariable - 3, aTileEntity, aTimer);
                }
            }
        }
        return aCoverVariable;
    }

    @Override
    protected boolean isRedstoneSensitiveImpl(ForgeDirection side, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        return aCoverVariable.get() != 2; // always off, so no redstone needed either
    }

    /**
     * Make sure there is only one GT_Cover_ControlsWork on the aTileEntity TODO this is a migration thing. Remove this
     * after 2.3.0 is released.
     *
     * @return true if the cover is the first (side) one
     **/
    private boolean makeSureOnlyOne(ForgeDirection side, ICoverable aTileEntity) {
        return IControlsWorkCover.makeSureOnlyOne(side, aTileEntity);
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
    public boolean onCoverRemoval(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        boolean aForced) {
        if ((aTileEntity instanceof IMachineProgress)) {
            ((IMachineProgress) aTileEntity).enableWorking();
            ((IMachineProgress) aTileEntity).setWorkDataValue((byte) 0);
        }
        return true;
    }

    @Override
    public int onCoverScrewdriverclick(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        EntityPlayer aPlayer, float aX, float aY, float aZ) {
        aCoverVariable = (aCoverVariable + (aPlayer.isSneaking() ? -1 : 1)) % 5;
        if (aCoverVariable < 0) {
            aCoverVariable = 2;
        }
        if (aCoverVariable == 0) {
            GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("003", "Enable with Signal"));
        }
        if (aCoverVariable == 1) {
            GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("004", "Disable with Signal"));
        }
        if (aCoverVariable == 2) {
            GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("005", "Disabled"));
        }
        if (aCoverVariable == 3) {
            GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("505", "Enable with Signal (Safe)"));
        }
        if (aCoverVariable == 4) {
            GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("506", "Disable with Signal (Safe)"));
        }
        // TODO: Set lastPlayer
        return aCoverVariable;
    }

    @Override
    public int getTickRate(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return 1;
    }

    @Override
    public boolean isCoverPlaceable(ForgeDirection side, ItemStack aStack, ICoverable aTileEntity) {
        if (!super.isCoverPlaceable(side, aStack, aTileEntity)) return false;
        for (final ForgeDirection tSide : ForgeDirection.VALID_DIRECTIONS) {
            if (aTileEntity.getCoverBehaviorAtSideNew(tSide) instanceof IControlsWorkCover) {
                return false;
            }
        }
        return true;
    }

    // GUI stuff

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    protected String getGuiId() {
        return "cover.machine_controller";
    }

    @Override
    public void addUIWidgets(CoverGuiData guiData, PanelSyncManager syncManager, Flow column) {
        EnumSyncValue<ConditionMode> conditionModeSyncValue = new EnumSyncValue<>(
            ConditionMode.class,
            () -> getConditionMode(guiData),
            mode -> setConditionMode(mode, guiData));
        syncManager.syncValue("condition_mode", conditionModeSyncValue);
        BooleanSyncValue safeModeSyncValue = new BooleanSyncValue(
            () -> getSafeMode(guiData),
            safeMode -> setSafeMode(safeMode, guiData));

        column.child(
            Flow.column()
                .crossAxisAlignment(Alignment.CrossAxis.START)
                .marginLeft(WIDGET_MARGIN)
                .childPadding(2)
                .child(
                    Flow.row()
                        .coverChildren()
                        .childPadding(WIDGET_MARGIN)
                        .child(
                            new ToggleButton()
                                .value(LinkedBoolValue.of(conditionModeSyncValue, ConditionMode.ENABLE_WITH_REDSTONE))
                                .overlay(GTGuiTextures.OVERLAY_BUTTON_REDSTONE_ON)
                                .size(16))
                        .child(
                            IKey.str(GTUtility.trans("243", "Enable with Redstone"))
                                .asWidget()))
                .child(
                    Flow.row()
                        .coverChildren()
                        .childPadding(WIDGET_MARGIN)
                        .child(
                            new ToggleButton()
                                .value(LinkedBoolValue.of(conditionModeSyncValue, ConditionMode.DISABLE_WITH_REDSTONE))
                                .overlay(GTGuiTextures.OVERLAY_BUTTON_REDSTONE_OFF)
                                .size(16))
                        .child(
                            IKey.str(GTUtility.trans("244", "Disable with Redstone"))
                                .asWidget()))
                .child(
                    Flow.row()
                        .coverChildren()
                        .childPadding(WIDGET_MARGIN)
                        .child(
                            new ToggleButton().value(LinkedBoolValue.of(conditionModeSyncValue, ConditionMode.DISABLE))
                                .overlay(GTGuiTextures.OVERLAY_BUTTON_CROSS)
                                .size(16))
                        .child(
                            IKey.str(GTUtility.trans("245", "Disable machine"))
                                .asWidget()))
                .child(
                    Flow.row()
                        .coverChildren()
                        .childPadding(WIDGET_MARGIN)
                        .child(
                            new ToggleButton().value(safeModeSyncValue)
                                .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                                .overlay(false, GTGuiTextures.OVERLAY_BUTTON_CROSS)
                                .size(16))
                        .child(
                            IKey.str(GTUtility.trans("507", "Safe Mode"))
                                .asWidget())));
    }

    private enum ConditionMode {
        ENABLE_WITH_REDSTONE,
        DISABLE_WITH_REDSTONE,
        DISABLE
    }

    private ConditionMode getConditionMode(CoverGuiData guiData) {
        int coverVariable = convert(getCoverData(guiData));
        if (coverVariable % 3 == 0) {
            return ConditionMode.ENABLE_WITH_REDSTONE;
        } else if (coverVariable % 3 == 1) {
            return ConditionMode.DISABLE_WITH_REDSTONE;
        }
        return ConditionMode.DISABLE;
    }

    private void setConditionMode(ConditionMode mode, CoverGuiData guiData) {
        int coverVariable = convert(getCoverData(guiData));
        boolean safeMode = coverVariable > 2;
        int newCoverVariable = switch (mode) {
            case ENABLE_WITH_REDSTONE -> safeMode ? 3 : 0;
            case DISABLE_WITH_REDSTONE -> safeMode ? 4 : 1;
            case DISABLE -> safeMode ? 5 : 2;
        };
        if (coverVariable != newCoverVariable) {
            guiData.setCoverData(new ISerializableObject.LegacyCoverData(newCoverVariable));
        }
    }

    private boolean getSafeMode(CoverGuiData guiData) {
        int coverVariable = convert(getCoverData(guiData));
        return coverVariable > 2;
    }

    private void setSafeMode(boolean safeMode, CoverGuiData guiData) {
        int coverVariable = convert(getCoverData(guiData));
        int newCoverVariable;
        if (safeMode && coverVariable < 3) {
            newCoverVariable = coverVariable + 3;
        } else if (!safeMode && coverVariable > 2) {
            newCoverVariable = coverVariable - 3;
        } else {
            newCoverVariable = coverVariable;
        }
        if (coverVariable != newCoverVariable) {
            guiData.setCoverData(new ISerializableObject.LegacyCoverData(newCoverVariable));
        }
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new ControlsWorkUIFactory(buildContext).createWindow();
    }

    private class ControlsWorkUIFactory extends UIFactory {

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;

        public ControlsWorkUIFactory(CoverUIBuildContext buildContext) {
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
                        CoverControlsWork.this,
                        (id, coverData) -> !getClickable(id, convert(coverData)),
                        (id, coverData) -> new ISerializableObject.LegacyCoverData(
                            getNewCoverVariable(id, convert(coverData))))
                                .addToggleButton(
                                    0,
                                    CoverDataFollowerToggleButtonWidget.ofDisableable(),
                                    widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_REDSTONE_ON)
                                        .setPos(spaceX * 0, spaceY * 0))
                                .addToggleButton(
                                    1,
                                    CoverDataFollowerToggleButtonWidget.ofDisableable(),
                                    widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_REDSTONE_OFF)
                                        .setPos(spaceX * 0, spaceY * 1))
                                .addToggleButton(
                                    2,
                                    CoverDataFollowerToggleButtonWidget.ofDisableable(),
                                    widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_CROSS)
                                        .setPos(spaceX * 0, spaceY * 2))
                                .setPos(startX, startY))
                .widget(
                    new CoverDataControllerWidget<>(this::getCoverData, this::setCoverData, CoverControlsWork.this)
                        .addFollower(
                            CoverDataFollowerToggleButtonWidget.ofCheckAndCross(),
                            coverData -> convert(coverData) > 2,
                            (coverData, state) -> new ISerializableObject.LegacyCoverData(
                                adjustCoverVariable(state, convert(coverData))),
                            widget -> widget.setPos(spaceX * 0, spaceY * 3))
                        .setPos(startX, startY))
                .widget(
                    new TextWidget(GTUtility.trans("243", "Enable with Redstone"))
                        .setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(3 + startX + spaceX * 1, 4 + startY + spaceY * 0))
                .widget(
                    new TextWidget(GTUtility.trans("244", "Disable with Redstone"))
                        .setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(3 + startX + spaceX * 1, 4 + startY + spaceY * 1))
                .widget(
                    new TextWidget(GTUtility.trans("245", "Disable machine")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(3 + startX + spaceX * 1, 4 + startY + spaceY * 2))
                .widget(
                    new TextWidget(GTUtility.trans("507", "Safe Mode")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(3 + startX + spaceX * 1, 4 + startY + spaceY * 3));
        }

        private int getNewCoverVariable(int id, int coverVariable) {
            if (coverVariable > 2) {
                return id + 3;
            } else {
                return id;
            }
        }

        private boolean getClickable(int id, int coverVariable) {
            return ((id != coverVariable && id != coverVariable - 3) || id == 3);
        }

        private int adjustCoverVariable(boolean safeMode, int coverVariable) {
            if (safeMode && coverVariable <= 2) {
                coverVariable += 3;
            }
            if (!safeMode && coverVariable > 2) {
                coverVariable -= 3;
            }
            return coverVariable;
        }
    }
}
