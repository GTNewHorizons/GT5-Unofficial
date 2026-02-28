package gregtech.api.gui.widgets;

import java.util.Locale;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.Rectangle;
import com.cleanroommc.modularui.drawable.text.TextRenderer;
import com.cleanroommc.modularui.network.NetworkUtils;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.SingleChildWidget;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.IConfigurationCircuitSupport;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.CommonMetaTileEntity;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTGuiTheme;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.api.util.GTUtility;
import gregtech.api.util.item.GhostCircuitItemStackHandler;
import gregtech.common.config.Client;
import gregtech.common.gui.modularui.widget.RotatedDrawable;
import gregtech.common.items.ItemIntegratedCircuit;
import gregtech.common.modularui2.widget.GhostCircuitSlotWidget;

/**
 * A class of commonly used widgets between all forms of MUI2.
 */
public class CommonWidgets {

    /**
     * Returns a muffle button with the provided syncHandler
     * For unsynced sync handlers. Use {@link #createMuffleButton(String)} with a
     * key for synced handlers
     *
     * @param syncValue - sync handler, can not be synced to manager or will crash
     * @return a muffle button with the attached sync value
     */
    public static ToggleButton createMuffleButton(BooleanSyncValue syncValue) {
        return new ToggleButton().value(syncValue)
            .tooltip(tooltip -> tooltip.add(IKey.lang("GT5U.machines.muffled")))
            .overlay(true, GTGuiTextures.OVERLAY_BUTTON_MUFFLE_ON)
            .overlay(false, GTGuiTextures.OVERLAY_BUTTON_MUFFLE_OFF)
            .size(12, 12)
            .excludeAreaInRecipeViewer(true);
    }

    /**
     * Returns a muffle toggle button with the provided syncKey
     *
     * @param syncKey - key of synced BooleanSyncValue
     * @return a muffle button with the attached sync value
     */
    public static ToggleButton createMuffleButton(String syncKey) {
        return new ToggleButton().syncHandler(syncKey)
            .tooltip(tooltip -> tooltip.add(IKey.lang("GT5U.machines.muffled")))
            .overlay(true, GTGuiTextures.OVERLAY_BUTTON_MUFFLE_ON)
            .overlay(false, GTGuiTextures.OVERLAY_BUTTON_MUFFLE_OFF)
            .size(12, 12)
            .excludeAreaInRecipeViewer(true);
    }

    /**
     * Returns a power toggle button with the syncValue
     *
     * @param syncValue             - value of unsynced Sync value
     * @param isPowerSwitchDisabled - is power switch disabled
     * @param baseMetaTileEntity    - base MTE of machine
     * @return synced power button
     */
    public static ToggleButton createPowerSwitchButton(BooleanSyncValue syncValue, boolean isPowerSwitchDisabled,
        IGregTechTileEntity baseMetaTileEntity) {
        return new ToggleButton().value(syncValue)
            .tooltip(tooltip -> tooltip.add(StatCollector.translateToLocal("GT5U.gui.button.power_switch")))
            .size(18, 18)
            .overlay(new DynamicDrawable(() -> {
                if (isPowerSwitchDisabled) return GTGuiTextures.OVERLAY_BUTTON_POWER_SWITCH_DISABLED;
                if (baseMetaTileEntity.isAllowedToWork()) return GTGuiTextures.OVERLAY_BUTTON_POWER_SWITCH_ON;
                return GTGuiTextures.OVERLAY_BUTTON_POWER_SWITCH_OFF;
            }));
    }

    /**
     * Returns a power toggle button with the syncKey
     *
     * @param syncKey               - key of synced BooleanSyncValue
     * @param isPowerSwitchDisabled - is power switch disabled
     * @param baseMetaTileEntity    - base MTE of machine
     * @return synced power button
     */
    public static ToggleButton createPowerSwitchButton(String syncKey, boolean isPowerSwitchDisabled,
        IGregTechTileEntity baseMetaTileEntity) {
        return new ToggleButton().syncHandler(syncKey)
            .tooltip(tooltip -> tooltip.add(StatCollector.translateToLocal("GT5U.gui.button.power_switch")))
            .size(18, 18)
            .overlay(new DynamicDrawable(() -> {
                if (isPowerSwitchDisabled) return GTGuiTextures.OVERLAY_BUTTON_POWER_SWITCH_DISABLED;
                if (baseMetaTileEntity.isAllowedToWork()) return GTGuiTextures.OVERLAY_BUTTON_POWER_SWITCH_ON;
                return GTGuiTextures.OVERLAY_BUTTON_POWER_SWITCH_OFF;
            }));
    }

    /**
     * Returns a small, 12x12 power toggle button with the syncKey
     *
     * @param syncValue          - unsynced BooleanSyncValue
     * @param baseMetaTileEntity - base MTE of machine
     * @return synced power button
     */
    public static ToggleButton createSmallPowerSwitchButton(BooleanSyncValue syncValue,
        IGregTechTileEntity baseMetaTileEntity) {
        return new ToggleButton().value(syncValue)
            .tooltip(tooltip -> tooltip.add(IKey.lang("GT5U.gui.button.power_switch")))
            .overlay(true, GTGuiTextures.OVERLAY_BUTTON_SMALL_POWER_SWITCH_ON)
            .overlay(false, GTGuiTextures.OVERLAY_BUTTON_SMALL_POWER_SWITCH_OFF)
            .size(12, 12)
            .excludeAreaInRecipeViewer(true);
    }

    /**
     * Returns a small, 12x12 power toggle button with the syncKey
     *
     * @param syncKey            - key of synced value
     * @param baseMetaTileEntity - base MTE of machine
     * @return synced power button
     */
    public static ToggleButton createSmallPowerSwitchButton(String syncKey, IGregTechTileEntity baseMetaTileEntity) {
        return new ToggleButton().syncHandler(syncKey)
            .tooltip(tooltip -> tooltip.add(IKey.lang("GT5U.gui.button.power_switch")))
            .overlay(true, GTGuiTextures.OVERLAY_BUTTON_SMALL_POWER_SWITCH_ON)
            .overlay(false, GTGuiTextures.OVERLAY_BUTTON_SMALL_POWER_SWITCH_OFF)
            .size(12, 12)
            .excludeAreaInRecipeViewer(true);
    }

    /**
     *
     * @return a button that when clicked, closes the panel its on
     */
    public static ButtonWidget<?> panelCloseButton() {
        return ButtonWidget.panelCloseButton();
    }

    /**
     * Returns a ghost circuit slot widget. baseMachine should be an instance of
     * IConfigurationCircuitSupport
     * Otherwise, returns an empty widget
     *
     * @param syncManager - manager
     * @param baseMachine - meta tile entity
     * @return ghost circuit slot widget
     */
    public static Widget<? extends Widget<?>> createCircuitSlot(PanelSyncManager syncManager,
        IMetaTileEntity baseMachine) {
        if (baseMachine instanceof IConfigurationCircuitSupport circuitEnabled && circuitEnabled.allowSelectCircuit()) {
            IntSyncValue selectedSyncHandler = new IntSyncValue(() -> {
                ItemStack selectedItem = baseMachine.getInventoryHandler()
                    .getStackInSlot(circuitEnabled.getCircuitSlot());
                if (selectedItem != null && selectedItem.getItem() instanceof ItemIntegratedCircuit) {
                    // selected index 0 == config 1
                    return selectedItem.getItemDamage() - 1;
                }
                return -1;
            }, index -> {
                if (index != -1) {
                    baseMachine.setInventorySlotContents(
                        circuitEnabled.getCircuitSlot(),
                        GTUtility.getAllIntegratedCircuits()
                            .get(index)
                            .copy());
                } else {
                    baseMachine.setInventorySlotContents(circuitEnabled.getCircuitSlot(), null);
                }
            });
            syncManager.syncValue("selector_screen_selected", selectedSyncHandler);
            return new GhostCircuitSlotWidget(baseMachine, syncManager)
                .slot(new ModularSlot(new GhostCircuitItemStackHandler(baseMachine), 0));
        }
        return IDrawable.EMPTY.asWidget()
            .size(18);
    }

    /**
     * Returns a title widget positioned on the top left above the panel. Client
     * only!
     *
     * @param mte        - The machine to make the title for
     * @param panelWidth - The width of the machine's main panel
     * @return machine title widget on the client, null otherwise
     */
    public static Widget<?> createMachineTitle(IMetaTileEntity mte, int panelWidth) {
        if (NetworkUtils.isClient()) {
            String title = mte.getLocalName();

            int borderRadius = 5;
            int maxWidth = panelWidth - borderRadius * 2;

            int titleWidth = TextRenderer.getFontRenderer()
                .getStringWidth(title);
            int widgetWidth = Math.min(maxWidth, titleWidth);

            int rows = (int) Math.ceil((double) titleWidth / maxWidth);
            int heightPerRow = TextRenderer.getFontRenderer().FONT_HEIGHT;
            int height = heightPerRow * rows;

            return new SingleChildWidget<>().coverChildren()
                .topRelAnchor(0, 1)
                .widgetTheme(GTWidgetThemes.BACKGROUND_TITLE)
                .child(
                    IKey.str(title)
                        .asWidget()
                        .size(widgetWidth, height)
                        .widgetTheme(GTWidgetThemes.TEXT_TITLE)
                        .marginLeft(5)
                        .marginRight(5)
                        .marginTop(5)
                        .marginBottom(1));
        }
        return null;
    }

    /**
     * Creates the machine color indicator tab shown on the right side of the panel.
     * <p>
     * The indicator is client-only and only appears when:
     * <ul>
     * <li>the client setting {@code Client.iface.showGuiColorIndicator} is enabled,
     * and</li>
     * <li>the machine has a custom colorization ({@code >= 0}).</li>
     * </ul>
     *
     * @param mte        machine being rendered
     * @param panelWidth width of the main panel, used as horizontal anchor
     * @return indicator widget, or {@code null} when the indicator should not be
     *         shown
     */
    public static Widget<?> createMachineColorIndicator(IMetaTileEntity mte, int panelWidth) {
        if (!NetworkUtils.isClient() || !Client.iface.showGuiColorIndicator || !hasCustomGuiColor(mte)) {
            return null;
        }
        final int frameSize = 16;
        final int colorSquareSize = 8;
        final int colorSquarePos = (frameSize - colorSquareSize) / 2;
        final int colorInnerSize = colorSquareSize - 2;
        final int borderColor = 0xFF000000 | mte.getTextColorOrDefault("text_gray", 0x404040);
        final String indicatorTooltip = StatCollector
            .translateToLocalFormatted("GT5U.tooltip.machine_color_indicator", getMachineColorName(mte));
        return new SingleChildWidget<>().size(frameSize)
            .widgetTheme(GTWidgetThemes.BACKGROUND_TITLE)
            .background(new RotatedDrawable(getTabTextureForTheme(mte)))
            .tooltip(tooltip -> tooltip.add(indicatorTooltip))
            .pos(panelWidth - 3, 82)
            .child(
                new SingleChildWidget<>().size(colorSquareSize, colorSquareSize)
                    .pos(colorSquarePos, colorSquarePos)
                    .tooltip(tooltip -> tooltip.add(indicatorTooltip))
                    .background(new DynamicDrawable(() -> new Rectangle().setColor(borderColor)))
                    .child(
                        new Widget<>().size(colorInnerSize, colorInnerSize)
                            .pos(1, 1)
                            .tooltip(tooltip -> tooltip.add(indicatorTooltip))
                            .background(
                                new DynamicDrawable(
                                    () -> new Rectangle().setColor(0xFF000000 | mte.getGUIColorization())))));
    }

    private static boolean hasCustomGuiColor(IMetaTileEntity mte) {
        return mte.getBaseMetaTileEntity() != null && mte.getBaseMetaTileEntity()
            .getColorization() >= 0;
    }

    private static String getMachineColorName(IMetaTileEntity mte) {
        if (mte.getBaseMetaTileEntity() == null) {
            return Dyes.GUI_METAL.getLocalizedDyeName();
        }
        return Dyes.getOrDefault(
            mte.getBaseMetaTileEntity()
                .getColorization(),
            Dyes.GUI_METAL)
            .getLocalizedDyeName();
    }

    private static IDrawable getTabTextureForTheme(IMetaTileEntity mte) {
        if (!(mte instanceof CommonMetaTileEntity commonMte)) {
            return GTGuiTextures.TAB_COLORED_NORMAL;
        }

        GTGuiTheme theme = commonMte.getEffectiveGuiTheme();
        String themeId = theme.getId();
        String suffix = themeId.contains(":") ? themeId.substring(themeId.indexOf(':') + 1) : themeId;
        suffix = suffix.toUpperCase(Locale.ROOT)
            .replaceAll("[^A-Z0-9]+", "_");
        String textureField = "TAB_COLORED_" + suffix;

        try {
            Object value = GTGuiTextures.class.getField(textureField)
                .get(null);
            if (value instanceof IDrawable drawable) {
                return drawable;
            }
        } catch (ReflectiveOperationException ignored) {}

        return GTGuiTextures.TAB_COLORED_NORMAL;
    }

}
