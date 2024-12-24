package gregtech.api.multitileentity;

import com.badlogic.ashley.core.Entity;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.PageButton;
import com.cleanroommc.modularui.widgets.PagedWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.gtnewhorizons.mutecore.api.data.FluidOutputInventory;
import com.gtnewhorizons.mutecore.api.data.ItemInputInventory;
import com.gtnewhorizons.mutecore.api.data.ItemOutputInventory;
import com.gtnewhorizons.mutecore.api.inventory.FluidInventory;
import com.gtnewhorizons.mutecore.api.inventory.ItemInventory;

public class MultiblockGUI extends MachineGUI {

    @Override
    public ModularPanel createGUI(Entity entity, PanelSyncManager syncManager) {
        ModularPanel gui = new ModularPanel("multiblock_gui");
        gui.size(210, 180);
        Row tabRow = new Row();
        gui.child(
            tabRow.size(196, 32)
                .align(Alignment.TopLeft)
                .pos(7, -28));
        PagedWidget<?> pages = new PagedWidget<>();
        pages.size(210, 180);
        pages.controller(tabController);
        gui.child(pages);
        pages.addPage(new Column().child(new TextWidget("Some random text")));
        tabRow.child(
            new PageButton(0, tabController).overlay(new ItemDrawable())
                .tab(GuiTextures.TAB_TOP, 0));

        ItemInventory inv = entity.getComponent(ItemInputInventory.class);
        pages.addPage(createItemInventory(inv));
        tabRow.child(
            new PageButton(1, tabController).overlay(new ItemDrawable())
                .tab(GuiTextures.TAB_TOP, 0));
        ItemInventory intout = entity.getComponent(ItemOutputInventory.class);
        pages.addPage(createItemInventory(intout));
        tabRow.child(
            new PageButton(2, tabController).overlay(new ItemDrawable())
                .tab(GuiTextures.TAB_TOP, 0));
        FluidInventory fout = entity.getComponent(FluidOutputInventory.class);
        pages.addPage(createFluidInventory(fout));
        tabRow.child(
            new PageButton(3, tabController).overlay(new ItemDrawable())
                .tab(GuiTextures.TAB_TOP, 0));
        gui.bindPlayerInventory();
        return gui;
    }

    protected IWidget createComplexItemInventory() {
        return null;
    }

    protected IWidget createComplexFluidInventory() {
        return null;
    }
}
