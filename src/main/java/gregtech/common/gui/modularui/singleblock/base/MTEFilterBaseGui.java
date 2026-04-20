package gregtech.common.gui.modularui.singleblock.base;

import java.util.List;
import java.util.function.Supplier;

import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.metatileentity.implementations.MTEFilterBase;
import gregtech.api.modularui2.GTGuiTextures;
import xyz.wagyourtail.jvmdg.util.Pair;

public class MTEFilterBaseGui<T extends MTEFilterBase> extends MTEBufferBaseGui<T> {

    public MTEFilterBaseGui(T machine) {
        super(machine);
    }

    protected boolean supportsInvertFilter() {
        return true;
    }

    @Override
    protected List<Pair<Boolean, Supplier<IWidget>>> createButtonList(ModularPanel panel,
        PanelSyncManager syncManager) {
        List<Pair<Boolean, Supplier<IWidget>>> buttons = super.createButtonList(panel, syncManager);

        // change emit redstone button
        buttons.set(
            2,
            new Pair<>(
                supportsEmitRedstone(),
                () -> createButton(
                    new BooleanSyncValue(machine::isRedstoneIfFull, machine::setRedstoneIfFull),
                    GTGuiTextures.OVERLAY_BUTTON_EMIT_REDSTONE,
                    configureTooltip(
                        "GT5U.machines.emit_redstone_gradually.tooltip",
                        machine.getEmptySlots(),
                        machine.getRedstoneOutput()))));

        // invert filter button
        buttons.add(
            new Pair<>(
                supportsInvertFilter(),
                () -> createButton(
                    new BooleanSyncValue(machine::isInvertFilter, machine::setInvertFilter),
                    GTGuiTextures.OVERLAY_BUTTON_INVERT_FILTER,
                    configureTooltip("GT5U.machines.invert_filter.tooltip"))));

        return buttons;
    }
}
