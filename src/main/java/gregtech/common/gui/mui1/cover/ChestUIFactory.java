package gregtech.common.gui.mui1.cover;

import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.SlotGroup;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.common.covers.Cover;
import gregtech.common.covers.CoverChest;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;

public class ChestUIFactory extends CoverUIFactory<CoverChest> {

    private static final int spaceX = 18;
    private static final int spaceY = 18;

    public ChestUIFactory(CoverUIBuildContext buildContext) {
        super(buildContext);
    }

    @Override
    protected int getGUIHeight() {
        CoverChest cover = getCover();
        int slotCount = 0;
        if (cover != null) {
            slotCount = cover.getSlotCount();
        }
        int height = slotCount / 3 * spaceY + 8;
        if (!getUIBuildContext().isAnotherWindow()) {
            // player inv is 4 row
            return height + 4 * spaceY + 14;
        }
        return height;
    }

    @Override
    protected void addTitleToUI(ModularWindow.Builder builder) {}

    @Override
    protected int getGUIWidth() {
        if (getUIBuildContext().isAnotherWindow()) {
            return spaceX * 3 + 20;
        } else {
            return spaceX * 9 + 20;
        }
    }

    @Override
    protected CoverChest adaptCover(Cover cover) {
        if (cover instanceof CoverChest adapterCover) {
            return adapterCover;
        }
        return null;
    }

    @Override
    protected void addUIWidgets(ModularWindow.Builder builder) {
        CoverDataControllerWidget<CoverChest> w = new CoverDataControllerWidget<>(this::getCover, getUIBuildContext());
        CoverChest cover = getCover();
        IItemHandlerModifiable h;
        if (cover == null) {
            // ???
            return;
        }
        h = cover.getItems();
        SlotGroup slotGroup = SlotGroup.ofItemHandler(h, 3)
            .build();
        if (getUIBuildContext().isAnotherWindow()) {
            slotGroup.setPos(4, 4);
        } else {
            slotGroup.setPos(getGUIWidth() / 2 - spaceX * 3 / 2, 6);
        }
        w.addChild(slotGroup);
        builder.widget(w);

        builder.setPos(
            (size, mainWindow) -> Alignment.Center.getAlignedPos(size, new Size(getGUIWidth(), getGUIHeight()))
                .subtract(getUIBuildContext().isAnotherWindow() ? getGUIWidth() + 80 : 0, 0));
    }

    @Override
    protected boolean doesBindPlayerInventory() {
        return true;
    }
}
