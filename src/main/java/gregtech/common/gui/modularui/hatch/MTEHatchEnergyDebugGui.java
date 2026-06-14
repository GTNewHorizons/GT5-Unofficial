package gregtech.common.gui.modularui.hatch;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.GTValues.TIER_COLORS;

import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.enums.GTValues;
import gregtech.api.metatileentity.implementations.MTEHatchEnergyDebug;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;

public class MTEHatchEnergyDebugGui extends MTEHatchBaseGui<MTEHatchEnergyDebug> {

    public MTEHatchEnergyDebugGui(MTEHatchEnergyDebug base) {
        super(base);
    }

    private TextFieldWidget createNumberTextField() {
        return new TextFieldWidget().setTextAlignment(Alignment.CenterRight)
            .setFormatAsInteger(true)
            .height(14);
    }

    private boolean onVoltageModifierButtonPressed(int mouseButton, IntSyncValue voltageTierSyncer) {

        int changedTier = voltageTierSyncer.getIntValue();
        switch (mouseButton) {
            case 0 -> changedTier = Math.min(changedTier + 1, 15);
            case 1 -> changedTier = Math.max(0, changedTier - 1);
        }
        voltageTierSyncer.setValue(changedTier);
        return true;
    }

    private void createVoltageModifierButtonTooltip(RichTooltip t) {
        t.addLine(
            EnumChatFormatting.AQUA + "Left"
                + EnumChatFormatting.RESET
                + "/"
                + EnumChatFormatting.RED
                + "Right"
                + EnumChatFormatting.RESET
                + " Click to "
                + EnumChatFormatting.AQUA
                + "Increment"
                + EnumChatFormatting.RESET
                + "/"
                + EnumChatFormatting.RED
                + "Decrement"
                + EnumChatFormatting.RESET
                + " Tier");
    }

    private void createAmperageModifierButtonTooltip(RichTooltip t) {
        t.addLine(
            EnumChatFormatting.AQUA + "Left"
                + EnumChatFormatting.RESET
                + "/"
                + EnumChatFormatting.RED
                + "Right"
                + EnumChatFormatting.RESET
                + " Click to "
                + EnumChatFormatting.AQUA
                + "Double"
                + EnumChatFormatting.RESET
                + "/"
                + EnumChatFormatting.RED
                + "Halve"
                + EnumChatFormatting.RESET
                + " Amperage");
        t.addLine(
            EnumChatFormatting.GRAY + "" + EnumChatFormatting.ITALIC + "Max Amperage of " + formatNumber(MAX_AMPERAGE));
    }

    final int MAX_AMPERAGE = 536870912;
    final int MIN_TICKS_PER_REFILL = 20;
    final int MAX_TICKS_PER_REFILL = 1200;

    private boolean onAmperageModifierButtonPressed(int mouseButton, IntSyncValue amperageSyncer) {

        int changedAmperage = amperageSyncer.getIntValue();
        switch (mouseButton) {
            case 0 -> changedAmperage = GTUtility.clamp(changedAmperage * 2, 1, MAX_AMPERAGE);
            case 1 -> changedAmperage = Math.max(1, changedAmperage / 2);
        }
        amperageSyncer.setValue(changedAmperage);
        return true;
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        IntSyncValue voltageTierSyncer = new IntSyncValue(machine::getVoltageTier, machine::setVoltageTier).allowC2S();
        IntSyncValue amperageSyncer = new IntSyncValue(machine::getAmperage, machine::setAmperage).allowC2S();

        Flow numberInputColumn = Flow.column()
            .coverChildren()
            .childPadding(4)
            .crossAxisAlignment(Alignment.CrossAxis.START);

        Flow voltageRow = Flow.row()
            .width(17 * SLOT_SIZE / 2)
            .coverChildrenHeight()
            .mainAxisAlignment(Alignment.MainAxis.SPACE_BETWEEN);

        Flow voltageTextRow = Flow.row()
            .coverChildren()
            .childPadding(2)
            .collapseDisabledChild();

        // add a number input field to determine voltage tier
        voltageTextRow.child(
            createNumberTextField().width(25)
                .setMaxLength(2)
                .numbersInt(0, 15)
                .value(voltageTierSyncer)
                .defaultNumber(0));

        // add the changing tier description widget
        voltageTextRow.child(IKey.dynamic(() -> {
            int clampedTier = GTUtility.clamp(voltageTierSyncer.getIntValue(), 0, TIER_COLORS.length - 1);
            String color = GTValues.TIER_COLORS[clampedTier];
            return GTUtility.translate(
                "GT5U.gui.text.voltagetier") + " (" + color + GTValues.VN[clampedTier] + EnumChatFormatting.RESET + ")";
        })
            .asWidget());

        voltageRow.child(voltageTextRow);

        // add a button to increment / decrement voltage tier
        voltageRow.child(
            new ButtonWidget<>().overlay(GuiTextures.GRAPH)
                .onMousePressed(mouseButton -> this.onVoltageModifierButtonPressed(mouseButton, voltageTierSyncer))
                .tooltip(this::createVoltageModifierButtonTooltip));

        Flow amperageRow = Flow.row()
            .width(17 * SLOT_SIZE / 2)
            .coverChildrenHeight()
            .mainAxisAlignment(Alignment.MainAxis.SPACE_BETWEEN);

        Flow amperageTextRow = Flow.row()
            .coverChildren()
            .childPadding(2);

        // number field for amperage
        amperageTextRow.child(
            createNumberTextField().width(70)
                .setMaxLength((int) Math.ceil(Math.log10(MAX_AMPERAGE)))
                .numbersInt(1, MAX_AMPERAGE)
                .value(amperageSyncer)
                .defaultNumber(2));

        // text widget for Amperage, is static. width is larger for nice spacing
        amperageTextRow.child(new TextWidget<>(IKey.lang("GT5U.gui.text.amperage")));

        amperageRow.child(amperageTextRow);

        // button to double / halve amperage, up to MAX_AMPERAGE
        amperageRow.child(
            new ButtonWidget<>().overlay(GuiTextures.MAZE)
                .onMousePressed(mouseButton -> onAmperageModifierButtonPressed(mouseButton, amperageSyncer))
                .tooltip(this::createAmperageModifierButtonTooltip));

        numberInputColumn.child(voltageRow);
        numberInputColumn.child(amperageRow);

        return super.createContentSection(panel, syncManager).child(numberInputColumn);
    }

    @Override
    protected Flow createBottomLeftCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        IntSyncValue intervalSyncer = new IntSyncValue(machine::getRefillInterval, machine::setRefillInterval)
            .allowC2S();

        // row to allow setting of 'refill interval'
        Flow intervalRow = Flow.row()
            .coverChildren();

        intervalRow.child(
            createNumberTextField().width(40)
                .defaultNumber(600)
                .numbersInt(MIN_TICKS_PER_REFILL, MAX_TICKS_PER_REFILL)
                .value(intervalSyncer));

        intervalRow.child(
            IKey.lang("GT5U.gui.text.ticks_between_refill")
                .asWidget()
                .scale(0.9f));

        return super.createBottomLeftCornerFlow(panel, syncManager).child(intervalRow);
    }
}
