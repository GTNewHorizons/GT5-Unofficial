package gregtech.common.gui.modularui.multiblock;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.MTENanoForge;

public class MTENanoForgeGui extends MTEMultiBlockBaseGui<MTENanoForge> {

    public MTENanoForgeGui(MTENanoForge multiblock) {
        super(multiblock);
    }

    @Override
    protected Flow createButtonColumn(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createButtonColumn(panel, syncManager).child(createInfoButton(syncManager));
    }

    protected IWidget createInfoButton(PanelSyncManager syncManager) {
        IPanelHandler infoPanel = syncManager
            .syncedPanel("nanoForgeInfoPanel", true, (p_syncManager, syncHandler) -> openInfoPanel());
        return new ButtonWidget<>().size(16)
            .marginBottom(2)
            .overlay(GTGuiTextures.INFORMATION_BUBBLE)
            .background(IDrawable.EMPTY)
            .disableHoverBackground()
            .onMousePressed(d -> {
                if (!infoPanel.isPanelOpen()) {
                    infoPanel.openPanel();
                } else {
                    infoPanel.closePanel();
                }
                return true;
            })
            .tooltip(t -> t.addLine(GTUtility.translate("GT5U.machines.nano_forge.t4_info_tooltip")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    private static final int WIDTH = 250;
    private static final int HEIGHT = 250;
    private static final int PADDING_SIDE = 4;
    private static final int ADJUSTED_WIDTH = WIDTH - 2 * PADDING_SIDE;
    private static final int ADJUSTED_HEIGHT = HEIGHT - 2 * PADDING_SIDE;

    private ModularPanel openInfoPanel() {

        ModularPanel returnPanel = new ModularPanel("nanoForgeInfoPanel").size(WIDTH, HEIGHT)
            .padding(PADDING_SIDE, 0, PADDING_SIDE, 0)
            .background(GTGuiTextures.TT_BACKGROUND_TEXT_FIELD)
            .disableHoverBackground();
        ListWidget<IWidget, ?> textList = new ListWidget<>().size(ADJUSTED_WIDTH, ADJUSTED_HEIGHT);
        textList.child(createHeader());
        textList.child(createTextEntry(EnumChatFormatting.GOLD, "GT5U.machines.nano_forge.t4_info_text.1"));
        textList.child(createTextEntry(EnumChatFormatting.GOLD, "GT5U.machines.nano_forge.t4_info_text.2"));
        textList.child(createTextEntry(EnumChatFormatting.GREEN, "GT5U.machines.nano_forge.t4_info_text.3"));
        textList.child(createTextEntry(EnumChatFormatting.GOLD, "GT5U.machines.nano_forge.t4_info_text.4"));
        textList.child(createTextEntry(EnumChatFormatting.GOLD, "GT5U.machines.nano_forge.t4_info_text.5"));
        textList.child(createTextEntry(EnumChatFormatting.GREEN, "GT5U.machines.nano_forge.t4_info_text.6"));
        textList.child(createTextEntry(EnumChatFormatting.GOLD, "GT5U.machines.nano_forge.t4_info_text.7"));
        returnPanel.child(textList);

        return returnPanel;
    }

    private TextWidget<?> createTextEntry(EnumChatFormatting textColor, String langKey) {
        return IKey.str(textColor + GTUtility.translate(langKey))
            .alignment(Alignment.CenterLeft)
            .asWidget()
            .width(ADJUSTED_WIDTH - 10)
            .marginBottom(8);
    }

    private TextWidget<?> createHeader() {
        return IKey
            .str(
                EnumChatFormatting.GOLD + ""
                    + EnumChatFormatting.BOLD
                    + GTUtility.translate("GT5U.machines.nano_forge.t4_info_header"))
            .asWidget()
            .alignX(Alignment.CENTER)
            .marginBottom(8);
    }

}
