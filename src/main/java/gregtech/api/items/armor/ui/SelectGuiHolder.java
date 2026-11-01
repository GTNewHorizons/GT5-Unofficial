package gregtech.api.items.armor.ui;

import static gregtech.api.enums.Mods.GregTech;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.factory.GuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.ModularScreen;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

class SelectGuiHolder implements IGuiHolder<GuiData> {

    @Override
    public ModularScreen createScreen(final GuiData data, final ModularPanel mainPanel) {
        return new ModularScreen(GregTech.ID, mainPanel);
    }

    public static final SelectGuiHolder INSTANCE = new SelectGuiHolder();

    @Override
    public ModularPanel buildUI(final GuiData data, final PanelSyncManager syncManager, final UISettings settings) {
        return ArmorRadialUIFactory.createMainPanel(new ArmorRadialMenuSession(data, syncManager, settings));
    }

}
