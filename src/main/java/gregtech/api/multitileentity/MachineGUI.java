package gregtech.api.multitileentity;

import com.badlogic.ashley.core.Entity;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.ItemSlotLongSH;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ScrollWidget;
import com.cleanroommc.modularui.widgets.FluidSlotLong;
import com.cleanroommc.modularui.widgets.ItemSlotLong;
import com.cleanroommc.modularui.widgets.PageButton;
import com.cleanroommc.modularui.widgets.PagedWidget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.PagedWidget.Controller;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.slot.ModularSlotLong;
import com.gtnewhorizons.mutecore.api.data.FluidOutputInventory;
import com.gtnewhorizons.mutecore.api.data.ItemInputInventory;
import com.gtnewhorizons.mutecore.api.data.ItemOutputInventory;
import com.gtnewhorizons.mutecore.api.gui.MuTEGUI;
import com.gtnewhorizons.mutecore.api.inventory.FluidComponentInventoryHandler;
import com.gtnewhorizons.mutecore.api.inventory.FluidInventory;
import com.gtnewhorizons.mutecore.api.inventory.ItemComponentInventoryHandler;
import com.gtnewhorizons.mutecore.api.inventory.ItemInventory;

public class MachineGUI implements MuTEGUI {

    protected Controller tabController = new Controller();

    @Override
    public ModularPanel createGUI(Entity entity, PanelSyncManager syncManager) {
        ModularPanel gui = new ModularPanel("machine_gui");
        gui.size(210, 180);
        Row tabRow = new Row();
        gui.child(tabRow.size(196, 32).align(Alignment.TopLeft).pos(7, -28));
        PagedWidget<?> pages = new PagedWidget<>();
        pages.size(210, 180);
        pages.controller(tabController);
        gui.child(pages);
        pages.addPage(new Column().child(new TextWidget("Some random text")));
        tabRow.child(new PageButton(0, tabController).overlay(new ItemDrawable()).tab(GuiTextures.TAB_TOP, 0));

        ItemInventory inv = entity.getComponent(ItemInputInventory.class);
        pages.addPage(createItemInventory(inv));
        tabRow.child(new PageButton(1, tabController).overlay(new ItemDrawable()).tab(GuiTextures.TAB_TOP, 0));
        ItemInventory intout = entity.getComponent(ItemOutputInventory.class);
        pages.addPage(createItemInventory(intout));
        tabRow.child(new PageButton(2, tabController).overlay(new ItemDrawable()).tab(GuiTextures.TAB_TOP, 0));
        FluidInventory fout = entity.getComponent(FluidOutputInventory.class);
        pages.addPage(createFluidInventory(fout));
        tabRow.child(new PageButton(3, tabController).overlay(new ItemDrawable()).tab(GuiTextures.TAB_TOP, 0));
        gui.bindPlayerInventory();
        return gui;
    }

    protected IWidget createItemInventory(ItemInventory inventory) {
        ItemComponentInventoryHandler inventoryHandler = new ItemComponentInventoryHandler(inventory);
        Column page = new Column();
        page.align(Alignment.TopCenter);
        final ScrollWidget<?> scrollable = new ScrollWidget<>();
        for (int row = 0; row * 4 < inventory.getSize() - 1; row++) {
            int columnsToMake = Math.min(inventory.getSize() - row * 4, 4);
            for (int column = 0; column < columnsToMake; column++) {
                scrollable.child(new ItemSlotLong().slot(inventoryHandler, row * 4 + column)
                    .pos(column * 18, row * 18).size(18,18));
            }
        }
        page.child(scrollable.size(18 * 4 + 4, 18 * 4).align(Alignment.Center));
        return page;
    }

    protected IWidget createFluidInventory(FluidInventory inventory) {
        FluidComponentInventoryHandler inventoryHandler = new FluidComponentInventoryHandler(inventory);
        Column page = new Column();
        page.align(Alignment.TopCenter);
        final ScrollWidget<?> scrollable = new ScrollWidget<>();
        for (int row = 0; row * 4 < inventory.getSize() - 1; row++) {
            int columnsToMake = Math.min(inventory.getSize() - row * 4, 4);
            for (int column = 0; column < columnsToMake; column++) {
                scrollable.child(new FluidSlotLong().syncHandler(inventoryHandler.getTank(row * 4 + column))
                    .pos(column * 18, row * 18).size(18,18));
            }
        }
        page.child(scrollable.size(18 * 4 + 4, 18 * 4).align(Alignment.Center));
        return page;
    }

}
