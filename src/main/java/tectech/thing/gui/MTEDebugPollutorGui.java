package tectech.thing.gui;

import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.common.gui.modularui.singleblock.base.MTETieredMachineBlockBaseGui;
import gregtech.common.modularui2.sync.LinkedBoolValue;
import gregtech.common.modularui2.widget.SelectButton;
import tectech.thing.metaTileEntity.single.MTEDebugPollutor;

public class MTEDebugPollutorGui extends MTETieredMachineBlockBaseGui<MTEDebugPollutor> {

    public MTEDebugPollutorGui(MTEDebugPollutor machine) {
        super(machine);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        IntSyncValue pollutionSyncer = new IntSyncValue(machine::getPollution, machine::setPollution);
        BooleanSyncValue isPollutingSyncer = new BooleanSyncValue(machine::isPolluting, machine::setPolluting);
        syncManager.syncValue("isPolluting", isPollutingSyncer); // needed for the label to function

        Flow mainColumn = Flow.column()
            .sizeRel(1)
            .paddingTop(4)
            .paddingLeft(4)
            .childPadding(4)
            .crossAxisAlignment(Alignment.CrossAxis.START);

        Flow pollutionRow = Flow.row()
            .height(18)
            .childPadding(2)
            .coverChildrenWidth();

        // number input field for pollution
        pollutionRow.child(
            new TextFieldWidget().setTextAlignment(Alignment.CenterRight)
                .size(80, 14)
                .setMaxLength(10)
                .value(pollutionSyncer)
                .setFormatAsInteger(true)
                .setNumbers(0, Integer.MAX_VALUE)
                .setDefaultNumber(0));

        // text widget for pollution
        pollutionRow.child(
            IKey.lang("tt.gui.text.debug_pollutor.pollution")
                .asWidget()
                .height(18));

        Flow statusRow = Flow.row()
            .coverChildren()
            .childPadding(2);

        statusRow.child(getModeSetterButton(true, isPollutingSyncer));
        statusRow.child(getModeSetterButton(false, isPollutingSyncer));

        // text widget for producing/consuming
        statusRow.child(
            IKey.dynamic(
                () -> translateToLocalFormatted("tt.gui.text.debug_pollutor.status", getStatus(isPollutingSyncer)))
                .asWidget()
                .height(18));

        mainColumn.child(pollutionRow);
        mainColumn.child(statusRow);
        return super.createContentSection(panel, syncManager).child(mainColumn);
    }

    private ToggleButton getModeSetterButton(boolean isPolluteButton, BooleanSyncValue isPollutingSyncer) {
        return new SelectButton().size(18)
            .value(LinkedBoolValue.of(isPollutingSyncer, isPolluteButton))
            .overlay(isPolluteButton ? GuiTextures.MAXIMIZE : GuiTextures.MINIMIZE)
            .tooltip(
                t -> t.addLine(
                    translateToLocalFormatted(
                        "tt.gui.text.debug.set_mode",
                        translateToLocal(getMode(isPolluteButton)))));
    }

    private String getStatus(BooleanSyncValue isPollutingSyncer) {
        return !baseMetaTileEntity.isAllowedToWork() ? translateToLocal("tt.gui.text.debug.status.disabled")
            : !baseMetaTileEntity.isActive() ? translateToLocal("tt.gui.text.debug.status.inactive")
                : getMode(isPollutingSyncer.getBoolValue());
    }

    private String getMode(boolean isPolluting) {
        return isPolluting ? (EnumChatFormatting.RED + translateToLocal("tt.gui.text.debug_pollutor.status.produce"))
            : (EnumChatFormatting.GREEN + translateToLocal("tt.gui.text.debug_pollutor.status.consume"));
    }
}
