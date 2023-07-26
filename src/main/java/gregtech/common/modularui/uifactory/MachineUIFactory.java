package gregtech.common.modularui.uifactory;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.viewport.GuiContext;
import com.cleanroommc.modularui.sync.GuiSyncHandler;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.TextWidget;

import gregtech.GT_Mod;
import gregtech.api.enums.Dyes;
import gregtech.api.gui.modularui.GTThemes;
import gregtech.api.gui.modularui.GUITextureSet;
import gregtech.api.gui.modularui.IHasCommonGUI;

/**
 * GUI template to share common code.
 */
public abstract class MachineUIFactory<T extends IHasCommonGUI> {

    protected final T tile;

    public MachineUIFactory(T tile) {
        this.tile = tile;
    }

    /**
     * Registers sync handlers. For your custom sync handlers, use {@link #buildSyncHandlerImpl}.
     */
    public final void buildSyncHandler(GuiSyncHandler guiSyncHandler, EntityPlayer player) {
        buildSyncHandlerImpl(guiSyncHandler, player);
    }

    /**
     * Adds default GUI widgets. For your custom widgets, use {@link #addWidgets}.
     */
    public final ModularPanel createUIPanel(GuiContext context, EntityPlayer player) {
        // buildContext.setValidator(() -> !this.isDead());
        ModularPanel panel = ModularPanel.defaultPanel(context, getGUIWidth(), getGUIHeight());
        // builder.setBackground(getGUITextureSet().getMainBackground());
        // builder.setGuiTint(getGUIColorization());
        context.useTheme(GTThemes.getThemeName(Dyes.getDyeFromIndex(tile.getColorization())));
        if (doesBindPlayerInventory()) {
            panel.bindPlayerInventory();
        }
        addWidgets(panel, context, player);
        addTitle(panel, tile.getLocalName());
        addCoverTabs(panel);
        // final IConfigurationCircuitSupport csc = getConfigurationCircuitSupport();
        // if (csc != null && csc.allowSelectCircuit()) {
        // addConfigurationCircuitSlot(builder);
        // } else {
        addGregTechLogo(panel);
        // }

        return panel;
    }

    // region overrides

    /**
     * Override this to define values to sync for your GUI. Widgets will go to {@link #addWidgets}.
     *
     * @param guiSyncHandler Manager to register sync handlers.
     * @param player         Player trying to open GUI.
     */
    public void buildSyncHandlerImpl(GuiSyncHandler guiSyncHandler, EntityPlayer player) {}

    /**
     * Override this to add widgets for your GUI. If the widget is synced, make sure to also override
     * {@link #buildSyncHandler} to register sync handlers.
     *
     * @param panel   Base panel to add widgets to.
     * @param context Context of GUI to open.
     * @param player  Player trying to open GUI.
     */
    protected void addWidgets(ModularPanel panel, GuiContext context, EntityPlayer player) {}

    protected int getGUIWidth() {
        return 176;
    }

    protected int getGUIHeight() {
        return 166;
    }

    protected boolean doesBindPlayerInventory() {
        return true;
    }

    protected void addGregTechLogo(ModularPanel panel) {
        panel.child(
            getGUITextureSet().getGregTechLogo()
                .asWidget()
                .size(17, 17)
                .pos(152, 63));
    }

    protected GUITextureSet getGUITextureSet() {
        return GUITextureSet.DEFAULT;
    }

    // endregion

    // region internals

    private void addTitle(ModularPanel panel, String title) {
        // todo
        if (GT_Mod.gregtechproxy.mTitleTabStyle == 2) {
            addTitleItemIconStyle(panel, title);
        } else {
            addTitleTextStyle(panel, title);
        }
    }

    private void addTitleTextStyle(ModularPanel panel, String title) {
        final int TAB_PADDING = 3;
        final int TITLE_PADDING = 2;
        final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        final List<String> titleLines = fontRenderer
            .listFormattedStringToWidth(title, getGUIWidth() - (TAB_PADDING + TITLE_PADDING) * 2);
        int titleWidth = titleLines.size() > 1 ? getGUIWidth() - (TAB_PADDING + TITLE_PADDING) * 2
            : fontRenderer.getStringWidth(title);
        // noinspection PointlessArithmeticExpression
        int titleHeight = titleLines.size() * fontRenderer.FONT_HEIGHT + (titleLines.size() - 1) * 1;

        final Widget<?> tab;
        final TextWidget text = new TextWidget(title).color(0x404040)
            .alignment(Alignment.CenterLeft)
            .width(titleWidth);
        if (GT_Mod.gregtechproxy.mTitleTabStyle == 1) {
            tab = getGUITextureSet().getTitleTabAngular()
                .asWidget()
                .pos(0, -(titleHeight + TAB_PADDING) + 1)
                .size(getGUIWidth(), titleHeight + TAB_PADDING * 2);
            text.pos(TAB_PADDING + TITLE_PADDING, -titleHeight + TAB_PADDING);
        } else {
            tab = getGUITextureSet().getTitleTabDark()
                .asWidget()
                .pos(0, -(titleHeight + TAB_PADDING * 2) + 1)
                .size(titleWidth + (TAB_PADDING + TITLE_PADDING) * 2, titleHeight + TAB_PADDING * 2 - 1);
            text.pos(TAB_PADDING + TITLE_PADDING, -titleHeight);
        }
        panel.child(tab)
            .child(text);
    }

    private void addTitleItemIconStyle(ModularPanel panel, String title) {
        ItemStack display = tile.getStackForm(1);
        if (display == null) return;
        panel.child(
            new ParentWidget<>().child(
                getGUITextureSet().getTitleTabNormal()
                    .asWidget()
                    .pos(0, 0)
                    .size(24, 24))
                .child(
                    new ItemDrawable(display).asWidget()
                        .pos(4, 4))
                .addTooltipLine(title)
                .tooltipShowUpTimer(TOOLTIP_DELAY)
                .pos(0, -24 + 3));
    }

    private void addCoverTabs(ModularPanel panel) {}

    // endregion
}
