package gregtech.common.gui.modularui.hatch;

import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.modularui2.GTGuiTextures;
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
            new Column().coverChildren()
                .align(Alignment.CENTER)
                .child(
                    IKey.str("Channel")
                        .asWidget())
                .child(
                    new Row().paddingBottom(4)
                        .coverChildren()
                        .child(
                            new ButtonWidget<>().size(14)
                                .overlay(GuiTextures.REMOVE)
                                .onMousePressed((a) -> {
                                    channel.setValue((channel.getValue() - 1) % hatch.MAX_CHANNEL);
                                    return true;
                                })

                        )
                        .child(
                            new TextFieldWidget().setFormatAsInteger(true)
                                .setDefaultNumber(0)
                                .height(14)
                                .setTextAlignment(Alignment.CENTER)
                                .setNumbers(0, hatch.MAX_CHANNEL)
                                .syncHandler("channel"))
                        .child(
                            new ButtonWidget<>().size(14)
                                .overlay(GuiTextures.ADD)
                                .onMousePressed((a) -> {
                                    channel.setValue((channel.getValue() + 1) % hatch.MAX_CHANNEL);
                                    return true;
                                })))

                .child(
                    IKey.str("Current Redstone Level")
                        .asWidget())

                .child(
                    IKey.dynamic(() -> EnumChatFormatting.RED + redstone.getStringValue())
                        .alignment(Alignment.CENTER)
                        .asWidget()
                        .paddingTop(3))

        );
    }

    @Override
    protected IDrawable.DrawableWidget createLogo() {
        return super.createLogo();
    }

    @Override
    protected UITexture getLogoTexture() {
        return GTGuiTextures.PICTURE_NANOCHIP_LOGO;
    }

    @Override
    public void registerSyncValues(PanelSyncManager syncManager) {
        IntSyncValue redstone = new IntSyncValue(hatch::getRedstoneInput);
        IntSyncValue channel = new IntSyncValue(hatch::getChannel, hatch::setChannel);
        syncManager.syncValue("redstone", redstone);
        syncManager.syncValue("channel", channel);
        super.registerSyncValues(syncManager);
    }
}
