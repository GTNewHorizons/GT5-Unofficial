package gregtech.api.ec.component.gui;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.OrderedSectionBuilder;

public final class MultiblockGuiBuilder {

    private final OrderedSectionBuilder<IGuiProviderComponent.GuiWidgetFactory> terminalTexts = new OrderedSectionBuilder<>();
    private final OrderedSectionBuilder<IGuiProviderComponent.GuiWidgetFactory> leftGap = new OrderedSectionBuilder<>();
    private final OrderedSectionBuilder<IGuiProviderComponent.GuiWidgetFactory> rightGap = new OrderedSectionBuilder<>();
    private final OrderedSectionBuilder<IGuiProviderComponent.GuiWidgetFactory> rightCorner = new OrderedSectionBuilder<>();
    private final OrderedSectionBuilder<IGuiProviderComponent.GuiWidgetFactory> leftCorner = new OrderedSectionBuilder<>();
    private final OrderedSectionBuilder<IGuiProviderComponent.GuiWidgetFactory> powerPanel = new OrderedSectionBuilder<>();

    public @NotNull OrderedSectionBuilder<IGuiProviderComponent.GuiWidgetFactory> terminalTexts() {
        return terminalTexts;
    }

    public MultiblockGuiBuilder addTerminalTexts(@NotNull IGuiProviderComponent.TerminalSection section,
        @NotNull IGuiProviderComponent.GuiWidgetFactory factory) {
        terminalTexts.add(section, factory);
        return this;
    }

    public @NotNull OrderedSectionBuilder<IGuiProviderComponent.GuiWidgetFactory> leftGap() {
        return leftGap;
    }

    public MultiblockGuiBuilder addLeftGap(@NotNull IGuiProviderComponent.LeftGapSection section,
        @NotNull IGuiProviderComponent.GuiWidgetFactory factory) {
        leftGap.add(section, factory);
        return this;
    }

    public @NotNull OrderedSectionBuilder<IGuiProviderComponent.GuiWidgetFactory> rightGap() {
        return rightGap;
    }

    public MultiblockGuiBuilder addRightGap(@NotNull IGuiProviderComponent.RightGapSection section,
        @NotNull IGuiProviderComponent.GuiWidgetFactory factory) {
        rightGap.add(section, factory);
        return this;
    }

    public @NotNull OrderedSectionBuilder<IGuiProviderComponent.GuiWidgetFactory> rightCorner() {
        return rightCorner;
    }

    public MultiblockGuiBuilder addRightCorner(@NotNull IGuiProviderComponent.RightCornerSection section,
        @NotNull IGuiProviderComponent.GuiWidgetFactory factory) {
        rightCorner.add(section, factory);
        return this;
    }

    public @NotNull OrderedSectionBuilder<IGuiProviderComponent.GuiWidgetFactory> leftCorner() {
        return leftCorner;
    }

    public MultiblockGuiBuilder addLeftCorner(@NotNull IGuiProviderComponent.LeftCornerSection section,
        @NotNull IGuiProviderComponent.GuiWidgetFactory factory) {
        leftCorner.add(section, factory);
        return this;
    }

    public @NotNull OrderedSectionBuilder<IGuiProviderComponent.GuiWidgetFactory> powerPanel() {
        return powerPanel;
    }

    public MultiblockGuiBuilder addPowerPanel(@NotNull IGuiProviderComponent.PowerPanelSection section,
        @NotNull IGuiProviderComponent.GuiWidgetFactory factory) {
        powerPanel.add(section, factory);
        return this;
    }
}
