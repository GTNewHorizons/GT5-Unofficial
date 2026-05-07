package tectech.thing.gui;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.util.GTUtility.getColoredTierNameFromTier;
import static gregtech.api.util.GTUtility.getTier;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

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
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.enums.GTValues;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.singleblock.base.MTETieredMachineBlockBaseGui;
import gregtech.common.modularui2.sync.LinkedBoolValue;
import gregtech.common.modularui2.widget.SelectButton;
import tectech.thing.metaTileEntity.single.MTEDebugPowerGenerator;

public class MTEDebugPowerGeneratorGui extends MTETieredMachineBlockBaseGui<MTEDebugPowerGenerator> {

    private final byte MAX_TIER = 14;
    private final int MAX_VOLTAGE = (int) GTValues.V[MAX_TIER];
    private final int MAX_AMPERAGE = 536870912;

    public MTEDebugPowerGeneratorGui(MTEDebugPowerGenerator machine) {
        super(machine);
    }

    private TextFieldWidget createNumberTextField() {
        return new TextFieldWidget().setTextAlignment(Alignment.CenterRight)
            .setFormatAsInteger(true)
            .height(14);
    }

    private boolean onVoltageModifierButtonPressed(int mouseButton, ByteSyncValue voltageTierSyncer,
        IntSyncValue voltageSyncer, BooleanSyncValue isUsingTierSyncer) {

        if (isUsingTierSyncer.getBoolValue()) {
            byte changedTier = voltageTierSyncer.getByteValue();
            switch (mouseButton) {
                case 0 -> changedTier = (byte) Math.min(changedTier + 1, MAX_TIER);
                case 1 -> changedTier = (byte) Math.max(0, changedTier - 1);
                case 2 -> isUsingTierSyncer.setBoolValue(!isUsingTierSyncer.getBoolValue());
            }
            voltageTierSyncer.setByteValue(changedTier);
        } else {
            int changedVoltage = voltageSyncer.getIntValue();
            switch (mouseButton) {
                case 0 -> changedVoltage = (int) GTUtility.clamp((long) changedVoltage * 2, 1, MAX_VOLTAGE);
                case 1 -> changedVoltage = Math.max(1, changedVoltage / 2);
                case 2 -> isUsingTierSyncer.setBoolValue(!isUsingTierSyncer.getBoolValue());
            }
            voltageSyncer.setIntValue(changedVoltage);
        }

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
            EnumChatFormatting.GRAY + "" + EnumChatFormatting.ITALIC + "Amperage: 1 - " + formatNumber(MAX_AMPERAGE));
    }

    private boolean onAmperageModifierButtonPressed(int mouseButton, IntSyncValue amperageSyncer) {

        int changedAmperage = amperageSyncer.getIntValue();
        switch (mouseButton) {
            case 0 -> changedAmperage = (int) GTUtility.clamp((long) changedAmperage * 2, 1, MAX_AMPERAGE);
            case 1 -> changedAmperage = Math.max(1, changedAmperage / 2);
        }
        amperageSyncer.setIntValue(changedAmperage);
        return true;
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        ByteSyncValue voltageTierSyncer = new ByteSyncValue(machine::getVoltageTier, machine::setVoltageTier);
        IntSyncValue voltageSyncer = new IntSyncValue(machine::getVoltage, machine::setVoltage);
        IntSyncValue amperageSyncer = new IntSyncValue(machine::getAmperage, machine::setAmperage);
        BooleanSyncValue isUsingTiersSyncer = new BooleanSyncValue(machine::isUsingTiers, machine::setUsingTiers);
        BooleanSyncValue isProducingSyncer = new BooleanSyncValue(machine::isProducing, (machine::setProducing));
        BooleanSyncValue isLaserSyncer = new BooleanSyncValue(machine::isLASER, machine::setLASER);

        syncManager.syncValue("isUsingTiers", isUsingTiersSyncer);
        syncManager.syncValue("isProducing", isProducingSyncer);

        Flow numberInputColumn = Flow.column();
        numberInputColumn.sizeRel(1)
            .paddingTop(4)
            .paddingLeft(4)
            .childPadding(4)
            .crossAxisAlignment(Alignment.CrossAxis.START);

        ParentWidget<?> voltageRow = new ParentWidget<>().size(152, 18);

        Flow voltageTextRow = Flow.row()
            .coverChildren()
            .verticalCenter()
            .childPadding(2)
            .collapseDisabledChild();

        // add a number input field to determine voltage tier
        voltageTextRow.child(
            createNumberTextField().width(20)
                .setMaxLength(2)
                .setNumbers(0, MAX_TIER)
                .value(voltageTierSyncer)
                .setDefaultNumber(0)
                .setEnabledIf(t -> isUsingTiersSyncer.getBoolValue()));

        // add the changing tier description widget
        voltageTextRow.child(
            IKey.dynamic(
                () -> GTUtility.translate("GT5U.gui.text.voltagetier") + " ("
                    + getColoredTierNameFromTier(voltageTierSyncer.getByteValue())
                    + ")")
                .asWidget()
                .height(14)
                .setEnabledIf(t -> isUsingTiersSyncer.getBoolValue()));

        // add a number input field to determine voltage
        voltageTextRow.child(
            createNumberTextField().width(75)
                .setMaxLength((int) Math.ceil(Math.log10(MAX_VOLTAGE)))
                .setNumbers(1, MAX_VOLTAGE)
                .value(voltageSyncer)
                .setDefaultNumber(1)
                .tooltip(t -> t.addLine(IKey.dynamic(() -> {
                    byte tier = getTier(voltageSyncer.getIntValue());
                    return GTValues.TIER_COLORS[tier] + GTValues.VN[tier] + EnumChatFormatting.RESET;
                })))
                .tooltipShowUpTimer(5)
                .setEnabledIf(t -> !isUsingTiersSyncer.getBoolValue()));

        // text widget for voltage, is static
        voltageTextRow.child(
            IKey.str("Voltage")
                .asWidget()
                .height(14)
                .setEnabledIf(t -> !isUsingTiersSyncer.getBoolValue()));

        voltageRow.child(voltageTextRow);

        // add a button to increment/decrement voltage tier up to MAX_TIER, or double/halve voltage up to MAX_VOLTAGE
        voltageRow.child(
            new ButtonWidget<>().overlay(GuiTextures.GRAPH)
                .size(18)
                .rightRel(0)
                .onMousePressed(
                    mouseButton -> this.onVoltageModifierButtonPressed(
                        mouseButton,
                        voltageTierSyncer,
                        voltageSyncer,
                        isUsingTiersSyncer))
                .tooltip(t -> createVoltageModifierButtonTooltip(t, isUsingTiersSyncer)));

        ParentWidget<?> amperageRow = new ParentWidget<>().size(152, 18);

        Flow amperageTextRow = Flow.row()
            .coverChildren()
            .verticalCenter()
            .childPadding(2);

        // number field for amperage
        amperageTextRow.child(
            createNumberTextField().width(65)
                .setMaxLength((int) Math.ceil(Math.log10(MAX_AMPERAGE)))
                .setNumbers(1, MAX_AMPERAGE)
                .value(amperageSyncer)
                .setDefaultNumber(2));

        // text widget for amperage, is static
        amperageTextRow.child(new TextWidget<>(IKey.lang("GT5U.gui.text.amperage")).height(18));

        amperageRow.child(amperageTextRow);

        // button to double / halve amperage, up to MAX_AMPERAGE
        amperageRow.child(
            new ButtonWidget<>().overlay(GuiTextures.MAZE)
                .size(18)
                .rightRel(0)
                .onMousePressed(mouseButton -> onAmperageModifierButtonPressed(mouseButton, amperageSyncer))
                .tooltip(this::createAmperageModifierButtonTooltip));

        Flow statusRow = Flow.row()
            .coverChildren()
            .childPadding(2);

        statusRow.child(getModeSetterButton(true, isProducingSyncer));
        statusRow.child(getModeSetterButton(false, isProducingSyncer));

        // button to toggle laser mode
        statusRow.child(
            new ToggleButton().value(isLaserSyncer)
                .overlay(GuiTextures.MAIN_HANDLE)
                .tooltip(t -> t.addLine(translateToLocal("tt.gui.text.debug_power.toggle_laser"))));

        numberInputColumn.child(voltageRow);
        numberInputColumn.child(amperageRow);
        numberInputColumn.child(statusRow);
        return super.createContentSection(panel, syncManager).child(numberInputColumn);
    }

    private ToggleButton getModeSetterButton(boolean isProduceButton, BooleanSyncValue isProducingSyncer) {
        return new SelectButton().size(18)
            .value(LinkedBoolValue.of(isProducingSyncer, isProduceButton))
            .overlay(isProduceButton ? GuiTextures.MAXIMIZE : GuiTextures.MINIMIZE)
            .tooltip(
                t -> t.addLine(
                    translateToLocalFormatted(
                        "tt.gui.text.debug.set_mode",
                        translateToLocal(
                            "tt.gui.text.debug_power.status." + (isProduceButton ? "produce" : "consume")))));
    }

    @Override
    protected boolean supportsMuffler() {
        return false;
    }
}
