package gregtech.common.gui.modularui.singleblock.base;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.GTValues.V;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.api.widget.Interactable;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.metatileentity.implementations.MTEBuffer;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTTooltipDataCache;
import gregtech.api.util.GTUtility;
import xyz.wagyourtail.jvmdg.util.Pair;

public class MTEBufferBaseGui<T extends MTEBuffer> extends MTETieredMachineBlockBaseGui<T> {

    public static final int TOOLTIP_DELAY = 5;

    public MTEBufferBaseGui(T machine) {
        super(machine);
    }

    @Override
    protected boolean supportsPowerSwitch() {
        return false;
    }

    @Override
    protected boolean supportsMuffler() {
        return false;
    }

    @Override
    protected Flow createLeftCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        Flow corner = super.createLeftCornerFlow(panel, syncManager).collapseDisabledChild();

        for (Pair<Boolean, Supplier<IWidget>> elem : createButtonList(panel, syncManager))
            corner.childIf(elem.getFirst(), elem.getSecond());

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

    protected ToggleButton createButton(BooleanSyncValue syncValue, IDrawable overlay,
        Consumer<RichTooltip> tooltipBuilder) {
        return new ToggleButton().value(syncValue)
            .marginBottom(3)
            .overlay(overlay)
            .tooltipDynamic(tooltipBuilder)
            .tooltipAutoUpdate(true)
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    protected Consumer<RichTooltip> configureTooltip(String key, Object... args) {
        GTTooltipDataCache.TooltipData data = machine.mTooltipCache.getData(key, args);

        return t -> t.addStringLines(Interactable.hasShiftDown() ? data.shiftText : data.text)
            .titleMargin(2);
    }

    /// Subclasses should add their own buttons to this list.
    protected List<Pair<Boolean, Supplier<IWidget>>> createButtonList(ModularPanel panel,
        PanelSyncManager syncManager) {
        List<Pair<Boolean, Supplier<IWidget>>> buttons = new ArrayList<>();

        // emit energy button
        buttons.add(
            new Pair<>(
                supportsEmitEnergy(),
                () -> createButton(
                    new BooleanSyncValue(machine::isOutput, machine::setOutput),
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
            new Pair<>(
                supportsSortStacks(),
                () -> createButton(
                    new BooleanSyncValue(machine::isSortStacks, machine::setSortStacks),
                    GTGuiTextures.OVERLAY_BUTTON_SORTING_MODE,
                    configureTooltip("GT5U.machines.sorting_mode.tooltip"))));

        // emit redstone button
        buttons.add(
            new Pair<>(
                supportsEmitRedstone(),
                () -> createButton(
                    new BooleanSyncValue(machine::isRedstoneIfFull, machine::setRedstoneIfFull),
                    GTGuiTextures.OVERLAY_BUTTON_EMIT_REDSTONE,
                    configureTooltip(
                        "GT5U.machines.emit_redstone_if_full.tooltip",
                        GTUtility.translate(machine.hasEmptySlots() ? "gui.yes" : "gui.no"),
                        machine.getRedstoneOutput()))));

        // invert redstone button
        buttons.add(
            new Pair<>(
                supportsInvertRedstone(),
                () -> createButton(
                    new BooleanSyncValue(machine::isInvert, machine::setInvert),
                    GTGuiTextures.OVERLAY_BUTTON_INVERT_REDSTONE,
                    configureTooltip("GT5U.machines.invert_redstone.tooltip"))));

        // stocking mode button
        buttons.add(
            new Pair<>(
                supportsStocking(),
                () -> createButton(
                    new BooleanSyncValue(machine::isStockingMode, machine::setStockingMode),
                    GTGuiTextures.OVERLAY_BUTTON_STOCKING_MODE,
                    configureTooltip("GT5U.machines.buffer_stocking_mode.tooltip"))));

        return buttons;
    }

    protected Widget<?> createArrow(BiFunction<Integer, Boolean, UITexture> arrowSupplier, int width, int height,
        boolean fromRight) {
        return arrowSupplier.apply(width, fromRight)
            .asWidget()
            .size(width, height);
    }
}
