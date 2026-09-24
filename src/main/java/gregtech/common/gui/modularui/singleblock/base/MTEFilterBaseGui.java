package gregtech.common.gui.modularui.singleblock.base;

import java.util.List;
import java.util.function.Supplier;

import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.metatileentity.implementations.MTEFilterBase;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.common.CommonButtons;
import it.unimi.dsi.fastutil.booleans.BooleanObjectImmutablePair;
import it.unimi.dsi.fastutil.booleans.BooleanObjectPair;

public class MTEFilterBaseGui<T extends MTEFilterBase> extends MTEBufferBaseGui<T> {

    public MTEFilterBaseGui(T machine) {
        super(machine);
    }

    protected boolean supportsInvertFilter() {
        return true;
    }

    @Override
    protected List<BooleanObjectPair<Supplier<IWidget>>> createButtonList(ModularPanel panel,
        PanelSyncManager syncManager) {
        List<BooleanObjectPair<Supplier<IWidget>>> buttons = super.createButtonList(panel, syncManager);

        // change emit redstone button
        buttons.set(
            EMIT_REDSTONE_BUTTON_INDEX,
            new BooleanObjectImmutablePair<>(
                supportsEmitRedstone(),
                () -> CommonButtons.createToggleButtonDynamicTooltip(
                    new BooleanSyncValue(machine::isRedstoneIfFull, machine::setRedstoneIfFull).allowC2S(),
                    GTGuiTextures.OVERLAY_BUTTON_EMIT_REDSTONE,
                    configureDynamicTooltip(
                        "GT5U.machines.emit_redstone_gradually.tooltip",
                        machine::getEmptySlots,
                        machine::getRedstoneOutput))));

        // invert filter button
        buttons.add(
            new BooleanObjectImmutablePair<>(
                supportsInvertFilter(),
                () -> CommonButtons.createToggleButtonDynamicTooltip(
                    new BooleanSyncValue(machine::isInvertFilter, machine::setInvertFilter).allowC2S(),
                    GTGuiTextures.OVERLAY_BUTTON_INVERT_FILTER,
                    configureTooltip("GT5U.machines.invert_filter.tooltip"))));

        return buttons;
    }
}
