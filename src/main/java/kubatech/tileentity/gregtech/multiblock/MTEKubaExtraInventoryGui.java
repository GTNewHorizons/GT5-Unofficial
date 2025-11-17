package kubatech.tileentity.gregtech.multiblock;

import net.minecraft.item.ItemStack;

import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.PageButton;
import com.cleanroommc.modularui.widgets.PagedWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.MetaTileEntityIDs;
import kubatech.api.implementations.KubaTechGTMultiBlockBase;

// for Mapiary and EIG
public class MTEKubaExtraInventoryGui<T extends KubaTechGTMultiBlockBase<T>> extends MTEKubaGui<T> {

    public MTEKubaExtraInventoryGui(T multiblock) {
        super(multiblock);
    }

    @Override
    protected Flow createTerminalRow(ModularPanel panel, PanelSyncManager syncManager) {
        PagedWidget.Controller tabController = new PagedWidget.Controller();

        PagedWidget<?> pagedWidget = new PagedWidget<>().controller(tabController);
        pagedWidget
            // preventative comment so spotless doesnt move child call up
            .addPage(super.createTerminalRow(panel, syncManager))
            .addPage(createExtraInventory(panel, syncManager))
            .size(getTerminalRowWidth(), getTerminalRowHeight());

        Flow pageButtons = new Column().coverChildren()
            .child(
                new PageButton(0, tabController).tab(GuiTextures.TAB_RIGHT, -1)
                    .overlay(new ItemDrawable(multiblock.getMachineCraftingIcon()).asIcon()))
            .child(
                new PageButton(1, tabController).tab(GuiTextures.TAB_RIGHT, 0)
                    .overlay(
                        new ItemDrawable(
                            new ItemStack(GregTechAPI.sBlockMachines, 1, MetaTileEntityIDs.QUANTUM_CHEST_LV.ID))
                                .asIcon()))
            .excludeAreaInRecipeViewer(true);

        return new Row().size(getTerminalRowWidth(), getTerminalRowHeight())
            .child(pagedWidget)
            .child(pageButtons);
    }

    protected IWidget createExtraInventory(ModularPanel panel, PanelSyncManager syncManager) {
        return new Row().size(getTerminalRowWidth(), getTerminalRowHeight());
    }
}
