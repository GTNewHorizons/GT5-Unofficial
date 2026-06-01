package tectech.thing.gui;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.util.GTUtility.getColoredTierNameFromTier;
import static gregtech.api.util.GTUtility.getTier;

import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.ByteSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.enums.GTValues;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.singleblock.base.MTETieredMachineBlockBaseGui;
import tectech.thing.metaTileEntity.single.MTEBuckConverter;

public class MTEBuckConverterGui extends MTETieredMachineBlockBaseGui<MTEBuckConverter> {

    private final int MAX_TIER = machine.mTier;
    private final int MAX_VOLTAGE = (int) machine.maxEUInput();

    public MTEBuckConverterGui(MTEBuckConverter machine) {
        super(machine);
    }

    private TextFieldWidget createNumberTextField() {
        return new TextFieldWidget().setTextAlignment(Alignment.CenterRight)
            .setFormatAsInteger(true)
            .height(14);
    }

    private boolean onVoltageModifierButtonPressed(int mouseButton, IntSyncValue amperageSyncer,
        ByteSyncValue voltageTierSyncer, IntSyncValue voltageSyncer, BooleanSyncValue isUsingTiersSyncer) {

        if (isUsingTiersSyncer.getBoolValue()) {
            byte changedTier = voltageTierSyncer.getByteValue();
            switch (mouseButton) {
                case 0 -> changedTier = (byte) Math.min(changedTier + 1, MAX_TIER);
                case 1 -> changedTier = (byte) Math.max(0, changedTier - 1);
                case 2 -> isUsingTiersSyncer.setBoolValue(!isUsingTiersSyncer.getBoolValue());
            }
            voltageTierSyncer.setByteValue(changedTier);
        } else {
            int changedVoltage = voltageSyncer.getIntValue();
            switch (mouseButton) {
                case 0 -> changedVoltage = (int) GTUtility.clamp((long) changedVoltage * 2, 1, MAX_VOLTAGE);
                case 1 -> changedVoltage = Math.max(1, changedVoltage / 2);
                case 2 -> isUsingTiersSyncer.setBoolValue(!isUsingTiersSyncer.getBoolValue());
            }
            voltageSyncer.setIntValue(changedVoltage);
        }

        // refresh amperage since it might be too high for current voltage
        amperageSyncer.setIntValue(
            GTUtility.clamp(
                amperageSyncer.getIntValue(),
                1,
                getMaxAmperageAtCurrentVoltage(voltageSyncer, voltageTierSyncer, isUsingTiersSyncer)));

        return true;
    }

    private void createVoltageModifierButtonTooltip(RichTooltip t, BooleanSyncValue isUsingTiersSyncer) {
        t.addLine(
            IKey.dynamic(
                () -> EnumChatFormatting.AQUA + "Left"
                    + EnumChatFormatting.RESET
                    + "/"
                    + EnumChatFormatting.RED
                    + "Right"
                    + EnumChatFormatting.RESET
                    + " Click to "
                    + EnumChatFormatting.AQUA
                    + (isUsingTiersSyncer.getBoolValue() ? "Increment" : "Double")
                    + EnumChatFormatting.RESET
                    + "/"
                    + EnumChatFormatting.RED
                    + (isUsingTiersSyncer.getBoolValue() ? "Decrement" : "Halve")
                    + EnumChatFormatting.RESET
                    + (isUsingTiersSyncer.getBoolValue() ? " Tier" : " Voltage")));
        t.addLine(
            IKey.dynamic(
                () -> EnumChatFormatting.GREEN + "Middle"
                    + EnumChatFormatting.RESET
                    + " Click to Switch to "
                    + (isUsingTiersSyncer.getBoolValue() ? "Voltage" : "Tier")));
        t.addLine(
            IKey.dynamic(
                () -> EnumChatFormatting.GRAY + ""
                    + EnumChatFormatting.ITALIC
                    + (isUsingTiersSyncer.getBoolValue() ? ("Tier: 0 - " + MAX_TIER)
                        : ("Voltage: 1 - " + formatNumber(MAX_VOLTAGE)))));
    }

    private void createAmperageModifierButtonTooltip(RichTooltip t, IntSyncValue voltageSyncer,
        ByteSyncValue voltageTierSyncer, BooleanSyncValue isUsingTiersSyncer) {
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
            IKey.dynamic(
                () -> EnumChatFormatting.GRAY + ""
                    + EnumChatFormatting.ITALIC
                    + "Amperage: 1 - "
                    + formatNumber(
                        getMaxAmperageAtCurrentVoltage(voltageSyncer, voltageTierSyncer, isUsingTiersSyncer))));
    }

    private boolean onAmperageModifierButtonPressed(int mouseButton, IntSyncValue amperageSyncer,
        IntSyncValue voltageSyncer, ByteSyncValue voltageTierSyncer, BooleanSyncValue isUsingTiersSyncer) {

        int changedAmperage = amperageSyncer.getIntValue();
        switch (mouseButton) {
            case 0 -> changedAmperage = GTUtility.clamp(
                changedAmperage * 2,
                1,
                getMaxAmperageAtCurrentVoltage(voltageSyncer, voltageTierSyncer, isUsingTiersSyncer));
            case 1 -> changedAmperage = Math.max(1, changedAmperage / 2);
        }
        amperageSyncer.setIntValue(changedAmperage);
        return true;
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        ByteSyncValue voltageTierSyncer = new ByteSyncValue(machine::getVoltageTier, machine::setVoltageTier)
            .allowC2S();
        IntSyncValue voltageSyncer = new IntSyncValue(machine::getVoltage, machine::setVoltage).allowC2S();
        IntSyncValue amperageSyncer = new IntSyncValue(machine::getAmperage, machine::setAmperage).allowC2S();
        BooleanSyncValue isUsingTiersSyncer = new BooleanSyncValue(machine::isUsingTiers, machine::setUsingTiers)
            .allowC2S();

        syncManager.syncValue("isUsingTiers", isUsingTiersSyncer);

        Flow numberInputColumn = Flow.column()
            .coverChildren()
            .childPadding(4)
            .crossAxisAlignment(Alignment.CrossAxis.START);

        Flow voltageRow = Flow.row()
            .width(8 * SLOT_SIZE)
            .coverChildrenHeight()
            .mainAxisAlignment(Alignment.MainAxis.SPACE_BETWEEN);

        Flow voltageTextRow = Flow.row()
            .coverChildren()
            .childPadding(2)
            .collapseDisabledChild();

        // add a number input field to determine voltage tier
        voltageTextRow.child(
            createNumberTextField().width(20)
                .setMaxLength(2)
                .numbersInt(0, MAX_TIER)
                .value(voltageTierSyncer)
                .defaultNumber(0)
                .setEnabledIf(t -> isUsingTiersSyncer.getBoolValue()));

        // add the changing tier description widget
        voltageTextRow.child(
            IKey.dynamic(
                () -> GTUtility.translate("GT5U.gui.text.voltagetier") + " ("
                    + getColoredTierNameFromTier(voltageTierSyncer.getByteValue())
                    + ")")
                .asWidget()
                .setEnabledIf(t -> isUsingTiersSyncer.getBoolValue()));

        // add a number input field to determine voltage
        voltageTextRow.child(
            createNumberTextField().width(75)
                .setMaxLength((int) Math.ceil(Math.log10(MAX_VOLTAGE)))
                .numbersInt(1, MAX_VOLTAGE)
                .value(voltageSyncer)
                .defaultNumber(1)
                .tooltip(t -> t.addLine(IKey.dynamic(() -> {
                    byte tier = getTier(voltageSyncer.getIntValue());
                    return GTValues.TIER_COLORS[tier] + GTValues.VN[tier] + EnumChatFormatting.RESET;
                })))
                .tooltipShowUpTimer(5)
                .setEnabledIf(t -> !isUsingTiersSyncer.getBoolValue()));

        // text widget for voltage, is static
        voltageTextRow.child(
            IKey.lang("GT5U.gui.text.voltage")
                .asWidget()
                .setEnabledIf(t -> !isUsingTiersSyncer.getBoolValue()));

        voltageRow.child(voltageTextRow);

        // add a button to increment/decrement voltage tier up to MAX_TIER, or double/halve voltage up to MAX_VOLTAGE
        voltageRow.child(
            new ButtonWidget<>().overlay(GuiTextures.GRAPH)
                .onMousePressed(
                    mouseButton -> this.onVoltageModifierButtonPressed(
                        mouseButton,
                        amperageSyncer,
                        voltageTierSyncer,
                        voltageSyncer,
                        isUsingTiersSyncer))
                .tooltip(t -> createVoltageModifierButtonTooltip(t, isUsingTiersSyncer)));

        Flow amperageRow = Flow.row()
            .width(8 * SLOT_SIZE)
            .coverChildrenHeight()
            .mainAxisAlignment(Alignment.MainAxis.SPACE_BETWEEN);

        Flow amperageTextRow = Flow.row()
            .coverChildren()
            .childPadding(2);

        // number field for amperage
        amperageTextRow.child(
            createNumberTextField().width(65)
                .setMaxLength(
                    (int) Math.ceil(
                        Math.log10(
                            getMaxAmperageAtCurrentVoltage(voltageSyncer, voltageTierSyncer, isUsingTiersSyncer))))
                .numbersInt(1, getMaxAmperageAtCurrentVoltage(voltageSyncer, voltageTierSyncer, isUsingTiersSyncer))
                .value(amperageSyncer)
                .defaultNumber(2));

        // text widget for amperage, is static
        amperageTextRow.child(
            IKey.lang("GT5U.gui.text.amperage")
                .asWidget());

        amperageRow.child(amperageTextRow);

        // button to double / halve amperage, up to MAX_AMPERAGE
        amperageRow.child(
            new ButtonWidget<>().overlay(GuiTextures.MAZE)
                .onMousePressed(
                    mouseButton -> onAmperageModifierButtonPressed(
                        mouseButton,
                        amperageSyncer,
                        voltageSyncer,
                        voltageTierSyncer,
                        isUsingTiersSyncer))
                .tooltip(
                    t -> createAmperageModifierButtonTooltip(t, voltageSyncer, voltageTierSyncer, isUsingTiersSyncer)));

        numberInputColumn.child(voltageRow);
        numberInputColumn.child(amperageRow);
        return super.createContentSection(panel, syncManager).child(numberInputColumn);
    }

    private int getMaxAmperageAtCurrentVoltage(IntSyncValue voltageSyncer, ByteSyncValue voltageTierSyncer,
        BooleanSyncValue isUsingTiersSyncer) {
        return Math.min(
            (int) GTUtility.getAmperageForTier(
                MAX_VOLTAGE,
                isUsingTiersSyncer.getBoolValue() ? voltageTierSyncer.getByteValue()
                    : getTier(voltageSyncer.getValue()))
                * 2,
            64);
    }
}
