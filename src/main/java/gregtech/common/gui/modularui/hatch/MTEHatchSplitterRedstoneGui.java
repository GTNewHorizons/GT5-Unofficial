package gregtech.common.gui.modularui.hatch;

import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.tileentities.machines.multi.nanochip.hatches.MTEHatchSplitterRedstone;

public class MTEHatchSplitterRedstoneGui extends MTEHatchBaseGui<MTEHatchSplitterRedstone> {

    public MTEHatchSplitterRedstoneGui(MTEHatchSplitterRedstone hatch) {
        super(hatch);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        IntSyncValue redstone = syncManager.findSyncHandler("redstone", IntSyncValue.class);
        IntSyncValue channel = syncManager.findSyncHandler("channel", IntSyncValue.class);

        return super.createContentSection(panel, syncManager).child(
            Flow.column()
                .coverChildren()
                .center()
                .child(
                    IKey.lang("GT5U.gui.text.nac.splitter.channel")
                        .asWidget())
                .child(
                    Flow.row()
                        .paddingBottom(4)
                        .coverChildren()
                        .child(
                            new ButtonWidget<>().size(14)
                                .overlay(GuiTextures.REMOVE)
                                .onMousePressed((a) -> {
                                    channel.setValue((channel.getValue() - 1) % machine.MAX_CHANNEL);
                                    return true;
                                }))
                        .child(
                            new TextFieldWidget().formatAsInteger(true)
                                .defaultNumber(0)
                                .height(14)
                                .setTextAlignment(Alignment.CENTER)
                                .numbersInt(0, machine.MAX_CHANNEL)
                                .syncHandler("channel"))
                        .child(
                            new ButtonWidget<>().size(14)
                                .overlay(GuiTextures.ADD)
                                .onMousePressed((a) -> {
                                    channel.setValue((channel.getValue() + 1) % machine.MAX_CHANNEL);
                                    return true;
                                })))
                .child(
                    IKey.lang("GT5U.gui.text.nac.splitter.redstone_level")
                        .asWidget())
                .child(
                    IKey.dynamic(() -> EnumChatFormatting.RED + redstone.getStringValue())
                        .alignment(Alignment.CENTER)
                        .asWidget()
                        .widgetTheme(GTWidgetThemes.DISPLAY_TEXT_WHITE)
                        .paddingTop(3)));
    }

    @Override
    public void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        syncManager.syncValue("redstone", new IntSyncValue(machine::getRedstoneInput));
        syncManager.syncValue("channel", new IntSyncValue(machine::getChannel, machine::setChannel).allowC2S());
    }

    @Override
    protected boolean supportsBottomRowOverlap() {
        return true;
    }
}
