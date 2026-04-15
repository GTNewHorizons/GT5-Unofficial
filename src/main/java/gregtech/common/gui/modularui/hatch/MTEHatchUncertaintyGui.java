package gregtech.common.gui.modularui.hatch;

import static it.unimi.dsi.fastutil.shorts.ShortArrays.swap;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.awt.*;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.ByteSyncValue;
import com.cleanroommc.modularui.value.sync.GenericSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.adapter.ShortArrayAdapter;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.gui.modularui.multiblock.godforge.ForgeOfGodsGuiUtil;
import tectech.thing.metaTileEntity.hatch.MTEHatchUncertainty;

public class MTEHatchUncertaintyGui extends MTEHatchBaseGui<MTEHatchUncertainty> {

    public MTEHatchUncertaintyGui(MTEHatchUncertainty hatch) {
        super(hatch);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        Flow mainRow = Flow.row()
            .coverChildren()
            .paddingLeft(3);

        mainRow.child(createButtonColumn(syncManager, 0));
        mainRow.child(createButtonColumn(syncManager, 1));
        mainRow.child(createScreen(syncManager));
        mainRow.child(createButtonColumn(syncManager, 2));
        mainRow.child(createButtonColumn(syncManager, 3));

        return super.createContentSection(panel, syncManager).child(mainRow);
    }

    private ParentWidget<?> createScreen(PanelSyncManager syncManager) {
        ByteSyncValue statusSyncer = syncManager.findSyncHandler("status", ByteSyncValue.class);

        ParentWidget<?> screen = new ParentWidget<>().size(90, 72)
            .background(GTGuiTextures.TT_PICTURE_SCREEN_BLUE)
            .padding(3);

        // status label
        screen.child(
            IKey.dynamic(
                () -> translateToLocal(
                    statusSyncer.getValue() == 0 ? "tt.gui.text.hatch.uncertainty.status.ok"
                        : "tt.gui.text.hatch.uncertainty.status.ng"))
                .asWidget()
                .color(Color.WHITE.main)
                .topRel(0)
                .leftRel(0));

        // matrix widget
        screen.child(createMatrix(syncManager));

        // tt logo
        screen.child(
            createLogo().bottomRel(0)
                .rightRel(0));

        return screen;
    }

    private ParentWidget<?> createMatrix(PanelSyncManager syncManager) {
        @SuppressWarnings("unchecked")
        GenericSyncValue<short[]> matrixSyncer = syncManager.findSyncHandler("matrix", GenericSyncValue.class);
        ByteSyncValue statusSyncer = syncManager.findSyncHandler("status", ByteSyncValue.class);
        ByteSyncValue modeSyncer = syncManager.findSyncHandler("mode", ByteSyncValue.class);
        ByteSyncValue selectionSyncer = syncManager.findSyncHandler("selection", ByteSyncValue.class);

        ParentWidget<?> matrixWidget = new ParentWidget<>().size(46)
            .leftRel(0)
            .bottomRel(0);

        // background image
        matrixWidget.background(GTGuiTextures.TT_PICTURE_UNCERTAINTY_MONITOR);

        // blue indicators with yellow selection box
        matrixWidget
            .child(
                new Grid()
                    .gridOfWidthHeight(
                        4,
                        4,
                        (x, y, index) -> new DynamicDrawable(
                            () -> selectionSyncer.getValue() == index ? GTGuiTextures.TT_PICTURE_UNCERTAINTY_SELECTED
                                : IDrawable.EMPTY).asWidget()
                                    .size(10)
                                    .overlay(
                                        new DynamicDrawable(
                                            () -> GTGuiTextures.TT_PICTURE_UNCERTAINTY_INDICATOR
                                                .withColorOverride(
                                                    Color.argb(1f, 1f, 1f, matrixSyncer.getValue()[index] / 1000f))
                                                .asIcon()
                                                .size(8))))
                    .center()
                    .minElementMargin(1)
                    .coverChildren());

        // red/green status lights
        matrixWidget.child(new Grid().gridOfWidthHeight(3, 3, (x, y, index) -> new DynamicDrawable(() -> {
            UITexture valid = GTGuiTextures.TT_PICTURE_UNCERTAINTY_VALID[index];
            UITexture invalid = GTGuiTextures.TT_PICTURE_UNCERTAINTY_INVALID[index];
            byte status = statusSyncer.getValue();

            switch (modeSyncer.getValue()) {
                case 1: // ooo oxo ooo
                    if (index == 4) return status == 0 ? valid : invalid;
                    break;
                case 2: // ooo xox ooo
                    if (index == 3) return (status & 1) == 0 ? valid : invalid;
                    if (index == 5) return (status & 2) == 0 ? valid : invalid;
                    break;
                case 3: // oxo xox oxo
                    if (index == 1) return (status & 1) == 0 ? valid : invalid;
                    if (index == 3) return (status & 2) == 0 ? valid : invalid;
                    if (index == 5) return (status & 4) == 0 ? valid : invalid;
                    if (index == 7) return (status & 8) == 0 ? valid : invalid;
                    break;
                case 4: // xox ooo xox
                    if (index == 0) return (status & 1) == 0 ? valid : invalid;
                    if (index == 2) return (status & 2) == 0 ? valid : invalid;
                    if (index == 6) return (status & 4) == 0 ? valid : invalid;
                    if (index == 8) return (status & 8) == 0 ? valid : invalid;
                    break;
                case 5: // xox oxo xox
                    if (index == 0) return (status & 1) == 0 ? valid : invalid;
                    if (index == 2) return (status & 2) == 0 ? valid : invalid;
                    if (index == 4) return (status & 4) == 0 ? valid : invalid;
                    if (index == 6) return (status & 8) == 0 ? valid : invalid;
                    if (index == 8) return (status & 16) == 0 ? valid : invalid;
                    break;
            }
            return IDrawable.EMPTY;
        }).asWidget()
            .size(4))
            .center()
            .minElementMargin(4)
            .coverChildren());

        return matrixWidget;
    }

    private Flow createButtonColumn(PanelSyncManager syncManager, int offset) {
        @SuppressWarnings("unchecked")
        GenericSyncValue<short[]> matrixSyncer = syncManager.findSyncHandler("matrix", GenericSyncValue.class);
        ByteSyncValue selectionSyncer = syncManager.findSyncHandler("selection", ByteSyncValue.class);

        Flow buttonColumn = Flow.col()
            .coverChildren();

        for (int i = 0; i < 4; i++) {
            int index = offset + i * 4;

            buttonColumn.child(
                new ButtonWidget<>().size(18)
                    .onMousePressed($ -> {
                        short selection = selectionSyncer.getValue();

                        if (selection == -1) {
                            selectionSyncer.setIntValue(index);
                        } else {
                            short[] temp = matrixSyncer.getValue();

                            swap(temp, selection, index);

                            matrixSyncer.setValue(temp);
                            selectionSyncer.setIntValue(-1);
                        }
                        return true;
                    })
                    .clickSound(ForgeOfGodsGuiUtil.getButtonSound())
                    .backgroundOverlay(GTGuiTextures.TT_OVERLAY_BUTTON_UNCERTAINTY[index]));

        }

        return buttonColumn;
    }

    @Override
    public void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        GenericSyncValue<short[]> matrixSyncer = GenericSyncValue.<short[]>notNullBuilder()
            .adapter(new ShortArrayAdapter())
            .getter(hatch::getMatrix)
            .setter(hatch::setMatrix)
            .build();
        matrixSyncer.setChangeListener(hatch::compute);
        syncManager.syncValue("matrix", matrixSyncer);

        syncManager.syncValue("selection", new ByteSyncValue(hatch::getSelection, hatch::setSelection));
        syncManager.syncValue("mode", new ByteSyncValue(hatch::getMode, hatch::setMode));
        syncManager.syncValue("status", new ByteSyncValue(hatch::getStatus, hatch::setStatus));
    }

    @Override
    protected boolean supportsRightCornerFlow() {
        return false;
    }

    @Override
    protected IDrawable.DrawableWidget createLogo() {
        return new IDrawable.DrawableWidget(GTGuiTextures.TT_PICTURE_TECTECH_LOGO_DARK).size(18);
    }
}
