package gregtech.common.gui.modularui.singleblock.base;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.GTValues.V;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.metatileentity.implementations.MTEBuffer;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.common.CommonButtons;
import gregtech.api.util.GTUtility;
import it.unimi.dsi.fastutil.booleans.BooleanObjectImmutablePair;
import it.unimi.dsi.fastutil.booleans.BooleanObjectPair;

public class MTEBufferBaseGui<T extends MTEBuffer> extends MTETieredMachineBlockBaseGui<T> {

    protected static final int EMIT_REDSTONE_BUTTON_INDEX = 2;

    public MTEBufferBaseGui(T machine) {
        super(machine);
    }

    @Override
    protected boolean supportsTopRightCornerFlow() {
        return false;
    }

    @Override
    protected Flow createBottomLeftCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        Flow corner = super.createBottomLeftCornerFlow(panel, syncManager).collapseDisabledChild();

        for (BooleanObjectPair<Supplier<IWidget>> elem : createButtonList(panel, syncManager))
            corner.childIf(elem.firstBoolean(), elem.second());

        return corner;
    }

    protected boolean supportsEmitEnergy() {
        return true;
    }

    protected boolean supportsSortStacks() {
        return true;
    }

    protected boolean supportsEmitRedstone() {
        return true;
    }

    protected boolean supportsInvertRedstone() {
        return true;
    }

    protected boolean supportsStocking() {
        return true;
    }

    /// Subclasses should add their own buttons to this list.
    protected List<BooleanObjectPair<Supplier<IWidget>>> createButtonList(ModularPanel panel,
        PanelSyncManager syncManager) {
        List<BooleanObjectPair<Supplier<IWidget>>> buttons = new ArrayList<>();

        // emit energy button
        buttons.add(
            new BooleanObjectImmutablePair<>(
                supportsEmitEnergy(),
                () -> CommonButtons.createToggleButtonDynamicTooltip(
                    new BooleanSyncValue(machine::isOutput, machine::setOutput).allowC2S(),
                    GTGuiTextures.OVERLAY_BUTTON_EMIT_ENERGY,
                    configureTooltip(
                        "GT5U.machines.emit_energy.tooltip",
                        EnumChatFormatting.GREEN + formatNumber(V[machine.mTier])
                            + " ("
                            + GTUtility.getColoredTierNameFromTier(machine.mTier)
                            + EnumChatFormatting.GREEN
                            + ")"
                            + EnumChatFormatting.GRAY,
                        machine.maxAmperesOut()))));

        // sorting mode button
        buttons.add(
            new BooleanObjectImmutablePair<>(
                supportsSortStacks(),
                () -> CommonButtons.createToggleButtonDynamicTooltip(
                    new BooleanSyncValue(machine::isSortStacks, machine::setSortStacks).allowC2S(),
                    GTGuiTextures.OVERLAY_BUTTON_SORTING_MODE,
                    configureTooltip("GT5U.machines.sorting_mode.tooltip"))));

        // emit redstone button
        // this button needs to be at index EMIT_REDSTONE_BUTTON_INDEX in the list
        buttons.add(
            new BooleanObjectImmutablePair<>(
                supportsEmitRedstone(),
                () -> CommonButtons.createToggleButtonDynamicTooltip(
                    new BooleanSyncValue(machine::isRedstoneIfFull, machine::setRedstoneIfFull).allowC2S(),
                    GTGuiTextures.OVERLAY_BUTTON_EMIT_REDSTONE,
                    configureDynamicTooltip(
                        "GT5U.machines.emit_redstone_if_full.tooltip",
                        () -> StatCollector.translateToLocal(machine.hasEmptySlots() ? "gui.yes" : "gui.no"),
                        machine::getRedstoneOutput))));

        // invert redstone button
        buttons.add(
            new BooleanObjectImmutablePair<>(
                supportsInvertRedstone(),
                () -> CommonButtons.createToggleButtonDynamicTooltip(
                    new BooleanSyncValue(machine::isInvert, machine::setInvert).allowC2S(),
                    GTGuiTextures.OVERLAY_BUTTON_REDSTONE_ON,
                    GTGuiTextures.OVERLAY_BUTTON_REDSTONE_OFF,
                    configureTooltip("GT5U.machines.invert_redstone.tooltip"))));

        // stocking mode button
        buttons.add(
            new BooleanObjectImmutablePair<>(
                supportsStocking(),
                () -> CommonButtons.createToggleButtonDynamicTooltip(
                    new BooleanSyncValue(machine::isStockingMode, machine::setStockingMode).allowC2S(),
                    GTGuiTextures.OVERLAY_BUTTON_STOCKING_MODE,
                    configureTooltip("GT5U.machines.buffer_stocking_mode.tooltip"))));

        return buttons;
    }
}
