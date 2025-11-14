package gregtech.common.gui.modularui.hatch;

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
            .height(14)
            .marginRight(2);
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
        t.addLine(EnumChatFormatting.GRAY + "" + EnumChatFormatting.ITALIC + "Max Amperage of 536,870,912");
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
        IntSyncValue voltageTierSyncer = new IntSyncValue(hatch::getVoltageTier, hatch::setVoltageTier);
        IntSyncValue amperageSyncer = new IntSyncValue(hatch::getAmperage, hatch::setAmperage);
        IntSyncValue intervalSyncer = new IntSyncValue(hatch::getRefillInterval, hatch::setRefillInterval);
        Flow numberInputColumn = Flow.column();
        numberInputColumn.sizeRel(1)
            .paddingTop(4)
            .paddingLeft(4)
            .crossAxisAlignment(Alignment.CrossAxis.START);

        Flow voltageRow = Flow.row()
            .height(18)
            .coverChildrenWidth()
            .marginBottom(4);

        // add a number input field to determine voltage tier
        voltageRow.child(
            createNumberTextField().width(40)
                .setNumbers(0, 15)
                .value(voltageTierSyncer)
                .setDefaultNumber(0));

        // add the changing tier description widget
        voltageRow.child(IKey.dynamic(() -> {
            int clampedTier = GTUtility.clamp(voltageTierSyncer.getIntValue(), 0, TIER_COLORS.length - 1);
            String color = GTValues.TIER_COLORS[clampedTier];
            return GTUtility.translate(
                "GT5U.gui.text.voltagetier") + " (" + color + GTValues.VN[clampedTier] + EnumChatFormatting.RESET + ")";
        })
            .asWidget()
            .width(80)
            .height(14)
            .marginRight(2));

        // add a button to increment / decrement voltage tier
        voltageRow.child(
            new ButtonWidget<>().overlay(GuiTextures.GRAPH)
                .size(18)
                .onMousePressed(mouseButton -> this.onVoltageModifierButtonPressed(mouseButton, voltageTierSyncer))
                .tooltip(this::createVoltageModifierButtonTooltip));

        Flow amperageRow = Flow.row()
            .height(18)
            .marginBottom(16)
            .coverChildrenWidth();

        // number field for amperage
        amperageRow.child(
            createNumberTextField().width(60)
                .setNumbers(1, MAX_AMPERAGE)
                .value(amperageSyncer)
                .setDefaultNumber(2));

        // text widget for Amperage, is static. width is larger for nice spacing
        amperageRow.child(
            new TextWidget<>(IKey.lang("GT5U.gui.text.amperage")).width(60)
                .height(18)
                .marginRight(2));

        // button to double / halve amperage, up to 536,870,912
        amperageRow.child(
            new ButtonWidget<>().overlay(GuiTextures.MAZE)
                .size(18)
                .onMousePressed(mouseButton -> onAmperageModifierButtonPressed(mouseButton, amperageSyncer))
                .tooltip(this::createAmperageModifierButtonTooltip));

        // row to allow setting of 'refill interval'
        Flow intervalRow = Flow.row()
            .height(18)
            .alignX(0)
            .coverChildrenWidth();

        intervalRow.child(
            createNumberTextField().width(40)
                .setDefaultNumber(600)
                .setNumbers(MIN_TICKS_PER_REFILL, MAX_TICKS_PER_REFILL)
                .value(intervalSyncer));

        intervalRow.child(
            IKey.lang("GT5U.gui.text.ticks_between_refill")
                .asWidget()
                .width(100)
                .height(18)
                .scale(0.9f));

        numberInputColumn.child(voltageRow);
        numberInputColumn.child(amperageRow);
        numberInputColumn.child(intervalRow);
        return super.createContentSection(panel, syncManager).child(numberInputColumn);
    }
}
