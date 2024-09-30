package gregtech.common.covers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.gui.modularui2.CoverGuiData;
import gregtech.api.gui.modularui2.GTGuiTextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.CoverBehavior;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;
import gregtech.common.gui.modularui2.LinkedBoolValue;

public class CoverPlayerDetector extends CoverBehavior {

    private String placer = "";
    private int range = 8;

    public CoverPlayerDetector(ITexture coverTexture) {
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
        boolean playerDetected = false;

        if (aTileEntity instanceof IGregTechTileEntity) {
            if (aTileEntity.isUniversalEnergyStored(20)) {
                aTileEntity.decreaseStoredEnergyUnits(20, true);
                range = 32;
            } else {
                range = 8;
            }
            placer = ((IGregTechTileEntity) aTileEntity).getOwnerName();
        }
        for (Object tObject : aTileEntity.getWorld().playerEntities) {
            if ((tObject instanceof EntityPlayerMP tEntity)) {
                int dist = Math.max(
                    1,
                    (int) tEntity.getDistance(
                        aTileEntity.getXCoord() + 0.5D,
                        aTileEntity.getYCoord() + 0.5D,
                        aTileEntity.getZCoord() + 0.5D));
                if (dist < range) {
                    if (aCoverVariable == 0) {
                        playerDetected = true;
                        break;
                    }
                    if (tEntity.getDisplayName()
                        .equalsIgnoreCase(placer)) {
                        if (aCoverVariable == 1) {
                            playerDetected = true;
                            break;
                        }
                    } else if (aCoverVariable == 2) {
                        playerDetected = true;
                        break;
                    }
                }
            }
        }

        aTileEntity.setOutputRedstoneSignal(side, (byte) (playerDetected ? 15 : 0));
        return aCoverVariable;
    }

    @Override
    public int onCoverScrewdriverclick(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        EntityPlayer aPlayer, float aX, float aY, float aZ) {
        aCoverVariable = (aCoverVariable + (aPlayer.isSneaking() ? -1 : 1)) % 3;
        if (aCoverVariable < 0) {
            aCoverVariable = 2;
        }
        switch (aCoverVariable) {
            case 0 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("068.1", "Emit if any Player is close"));
            case 1 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("069.1", "Emit if other Player is close"));
            case 2 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("070", "Emit if you are close"));
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
        return 20;
    }

    // GUI stuff

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    protected String getGuiId() {
        return "cover.player_detector";
    }

    @Override
    public void addUIWidgets(CoverGuiData guiData, PanelSyncManager syncManager, Flow column) {
        EnumSyncValue<Mode> modeSyncValue = new EnumSyncValue<>(
            Mode.class,
            () -> getMode(guiData),
            value -> setMode(value, guiData));
        syncManager.syncValue("mode", modeSyncValue);

        column.child(
            new Grid().marginLeft(WIDGET_MARGIN)
                .coverChildren()
                .minElementMarginRight(WIDGET_MARGIN)
                .minElementMarginBottom(2)
                .minElementMarginTop(0)
                .minElementMarginLeft(0)
                .alignment(Alignment.CenterLeft)
                .row(
                    new ToggleButton().value(LinkedBoolValue.of(modeSyncValue, Mode.ANY_PLAYER))
                        .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                        .addTooltipLine(IKey.str(GTUtility.trans("068.1", "Emit if any Player is close")))
                        .size(16),
                    IKey.str(GTUtility.trans("319", "Any player"))
                        .asWidget())
                .row(
                    new ToggleButton().value(LinkedBoolValue.of(modeSyncValue, Mode.OTHER_PLAYERS))
                        .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                        .addTooltipLine(IKey.str(GTUtility.trans("069.1", "Emit if other Player is close")))
                        .size(16),
                    IKey.str(GTUtility.trans("320", "Other players"))
                        .asWidget())
                .row(
                    new ToggleButton().value(LinkedBoolValue.of(modeSyncValue, Mode.ONLY_OWNER))
                        .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                        .addTooltipLine(IKey.str(GTUtility.trans("070", "Emit if you are close")))
                        .size(16),
                    IKey.str(GTUtility.trans("321", "Only owner"))
                        .asWidget()));
    }

    private enum Mode {

        ANY_PLAYER,
        OTHER_PLAYERS,
        ONLY_OWNER;

        private static final Mode[] VALUES = values();
    }

    private Mode getMode(CoverGuiData guiData) {
        int coverVariable = convert(getCoverData(guiData));
        if (coverVariable >= 0 && coverVariable < Mode.VALUES.length) {
            return Mode.VALUES[coverVariable];
        }
        return Mode.ANY_PLAYER;
    }

    private void setMode(Mode mode, CoverGuiData guiData) {
        int coverVariable = convert(getCoverData(guiData));
        if (mode.ordinal() == coverVariable) return;

        guiData.setCoverData(new ISerializableObject.LegacyCoverData(mode.ordinal()));
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new PlayerDetectorUIFactory(buildContext).createWindow();
    }

    private class PlayerDetectorUIFactory extends UIFactory {

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;

        public PlayerDetectorUIFactory(CoverUIBuildContext buildContext) {
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
                        CoverPlayerDetector.this,
                        (index, coverData) -> index == convert(coverData),
                        (index, coverData) -> new ISerializableObject.LegacyCoverData(index))
                            .addToggleButton(
                                0,
                                CoverDataFollowerToggleButtonWidget.ofCheck(),
                                widget -> widget.addTooltip(GTUtility.trans("068.1", "Emit if any Player is close"))
                                    .setPos(spaceX * 0, spaceY * 0))
                            .addToggleButton(
                                1,
                                CoverDataFollowerToggleButtonWidget.ofCheck(),
                                widget -> widget.addTooltip(GTUtility.trans("069.1", "Emit if other Player is close"))
                                    .setPos(spaceX * 0, spaceY * 1))
                            .addToggleButton(
                                2,
                                CoverDataFollowerToggleButtonWidget.ofCheck(),
                                widget -> widget.addTooltip(GTUtility.trans("070", "Emit if you are close"))
                                    .setPos(spaceX * 0, spaceY * 2))
                            .setPos(startX, startY))
                .widget(
                    new TextWidget(GTUtility.trans("319", "Any player")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 1, 4 + startY + spaceY * 0))
                .widget(
                    new TextWidget(GTUtility.trans("320", "Other players")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 1, 4 + startY + spaceY * 1))
                .widget(
                    new TextWidget(GTUtility.trans("321", "Only owner")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 1, 4 + startY + spaceY * 2));
        }
    }
}
