package gregtech.api.modularui2;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import net.minecraftforge.common.MinecraftForge;

import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.api.ITheme;
import com.cleanroommc.modularui.api.IThemeApi;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.theme.ReloadThemeEvent;
import com.cleanroommc.modularui.utils.JsonBuilder;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

/**
 * Theme object gets applied to the entire panel. If you want to customize the appearance of specific widget, see
 * {@link GTWidgetThemes}.
 */
public class GTGuiTheme {

    private static final List<GTGuiTheme> THEMES = new ArrayList<>();

    private final String themeId;

    private final List<Consumer<JsonBuilder>> elementBuilder;
    private final JsonBuilder jsonBuilder;

    private UITexture logo;

    private GTGuiTheme(String themeId) {
        this.themeId = themeId;
        this.jsonBuilder = new JsonBuilder();
        this.elementBuilder = new ArrayList<>();
        THEMES.add(this);
    }

    public String getId() {
        return themeId;
    }

    public ITheme getMuiTheme() {
        return IThemeApi.get()
            .getTheme(themeId);
    }

    public @Nullable UITexture getLogo() {
        return logo;
    }

    private void register() {
        buildJson();
        IThemeApi.get()
            .registerTheme(themeId, jsonBuilder);
    }

    private void buildJson() {
        elementBuilder.forEach(c -> c.accept(jsonBuilder));
    }

    public static void registerThemes() {
        MinecraftForge.EVENT_BUS.register(GTGuiTheme.class);
        GTGuiThemes.init();
        THEMES.forEach(GTGuiTheme::register);
    }

    @SubscribeEvent
    public static void onReloadThemes(ReloadThemeEvent.Pre event) {
        THEMES.forEach(GTGuiTheme::buildJson);
    }

    public static Builder builder(String themeId) {
        return new Builder(themeId);
    }

    public static class Builder {

        private final GTGuiTheme theme;

        private Builder(String themeId) {
            theme = new GTGuiTheme(themeId);
        }

        /**
         * Set a parent theme for this theme, which unset values will inherit from.
         * If not set, it will use the default theme as the parent (VANILLA).
         */
        public Builder parent(String parentId) {
            theme.elementBuilder.add(b -> b.add("parent", parentId));
            return this;
        }

        /**
         * Set a background fallback for when specific widgets do not set their own.
         */
        public Builder globalBackground(String backgroundId) {
            theme.elementBuilder.add(b -> b.add("background", backgroundId));
            return this;
        }

        /**
         * Set a tooltip hover background fallback for when specific widgets do not set their own.
         */
        public Builder globalHoverBackground(String hoverBackgroundId) {
            theme.elementBuilder.add(b -> b.add("hoverBackground", hoverBackgroundId));
            return this;
        }

        /**
         * Set the window open/close animation speed. Overrides global cfg.
         *
         * @param rate the rate in frames to play the open/close animation over, or 0 for no animation
         */
        public Builder openCloseAnimation(int rate) {
            theme.elementBuilder.add(b -> b.add("openCloseAnimation", rate));
            return this;
        }

        /**
         * Set whether progress bars should animate smoothly. Overrides global cfg.
         */
        public Builder smoothProgressBar(boolean smoothBar) {
            theme.elementBuilder.add(b -> b.add("smoothProgressBar", smoothBar));
            return this;
        }

        /**
         * Set the tooltip pos for this theme. Overrides global cfg.
         */
        public Builder tooltipPos(RichTooltip.Pos tooltipPos) {
            theme.elementBuilder.add(b -> b.add("tooltipPos", tooltipPos.name()));
            return this;
        }

        /**
         * Set a global UI coloration for this theme.
         */
        public Builder color(int color) {
            theme.elementBuilder.add(b -> b.add("color", color));
            return this;
        }

        /**
         * Set a global UI text coloration for this theme.
         */
        public Builder textColor(int textColor) {
            theme.elementBuilder.add(b -> b.add("textColor", textColor));
            return this;
        }

        /**
         * Enable text shadow for the global UI text for this theme.
         */
        public Builder textShadow() {
            theme.elementBuilder.add(b -> b.add("textShadow", true));
            return this;
        }

        /**
         * Set a custom panel (background texture) for UIs with this theme.
         */
        public Builder panel(String panelId) {
            theme.elementBuilder.add(
                b -> b.add(
                    "panel",
                    new JsonBuilder().add(
                        "background",
                        new JsonBuilder().add("type", "texture")
                            .add("id", panelId))));
            return this;
        }

        /**
         * Set a custom button texture for UIs with this theme.
         */
        public Builder button(String buttonId) {
            return button(buttonId, buttonId, 0xFFFFFFFF, false);
        }

        /**
         * Set a custom button texture for UIs with this theme.
         *
         * @param buttonId The ID of the button texture
         * @param hoverId  The ID of the button texture while hovering over the button with your mouse
         */
        public Builder button(String buttonId, String hoverId) {
            return button(buttonId, hoverId, 0xFFFFFFFF, false);
        }

        /**
         * Set a custom button texture for UIs with this theme.
         *
         * @param buttonId   The ID of the button texture
         * @param hoverId    The ID of the button texture while hovering over the button with your mouse
         * @param textColor  The color of text overlaid on this button
         * @param textShadow If text overlaid on this button should have a text shadow
         */
        public Builder button(String buttonId, String hoverId, int textColor, boolean textShadow) {
            theme.elementBuilder.add(
                b -> b.add(
                    "button",
                    new JsonBuilder().add(
                        "background",
                        new JsonBuilder().add("type", "texture")
                            .add("id", buttonId))
                        .add(
                            "hoverBackground",
                            new JsonBuilder().add("type", "texture")
                                .add("id", hoverId))
                        .add("textColor", textColor)
                        .add("textShadow", textShadow)));
            return this;
        }

        /**
         * Set a custom item slot texture for UIs with this theme.
         */
        public Builder itemSlot(String itemSlotId) {
            return itemSlot(itemSlotId, 0x60FFFFFF);
        }

        /**
         * Set a custom item slot texture for UIs with this theme.
         *
         * @param itemSlotId The ID of the item slot texture
         * @param hoverColor The color of the tooltip hover box for this widget
         */
        public Builder itemSlot(String itemSlotId, int hoverColor) {
            theme.elementBuilder.add(
                b -> b.add(
                    "itemSlot",
                    new JsonBuilder().add(
                        "background",
                        new JsonBuilder().add("type", "texture")
                            .add("id", itemSlotId))
                        .add("slotHoverColor", hoverColor)));
            return this;
        }

        /**
         * Set a custom fluid slot texture for UIs with this theme.
         */
        public Builder fluidSlot(String fluidSlotId) {
            return fluidSlot(fluidSlotId, 0x60FFFFFF);
        }

        /**
         * Set a custom fluid slot texture for UIs with this theme.
         *
         * @param fluidSlotId The ID of the fluid slot texture
         * @param hoverColor  The color of the tooltip hover box for this widget
         */
        public Builder fluidSlot(String fluidSlotId, int hoverColor) {
            theme.elementBuilder.add(
                b -> b.add(
                    "fluidSlot",
                    new JsonBuilder().add(
                        "background",
                        new JsonBuilder().add("type", "texture")
                            .add("id", fluidSlotId))
                        .add("slotHoverColor", hoverColor)));
            return this;
        }

        /**
         * Set the text color for text fields in UIs with this theme.
         */
        public Builder textField(int textColor) {
            return textField(textColor, 0xFF2F72A8);
        }

        /**
         * Set the text color for text fields in UIs with this theme.
         *
         * @param textColor   Text color
         * @param markedColor Color of the highlight on selected text
         */
        public Builder textField(int textColor, int markedColor) {
            theme.elementBuilder.add(
                b -> b.add(
                    "textField",
                    new JsonBuilder().add("textColor", textColor)
                        .add("markedColor", markedColor)));
            return this;
        }

        /**
         * Set the theme options for a ToggleButton widget.
         *
         * @param backgroundId              The main background for the unpressed button
         * @param hoverBackgroundId         The on-hover background for the unpressed button
         * @param selectedBackgroundId      The main background for the pressed button
         * @param selectedHoverBackgroundId The on-hover background for the pressed button
         * @param selectedColor             The color to apply to the pressed button
         */
        public Builder toggleButton(String backgroundId, String hoverBackgroundId, String selectedBackgroundId,
            String selectedHoverBackgroundId, int selectedColor) {
            return toggleButton(
                backgroundId,
                hoverBackgroundId,
                selectedBackgroundId,
                selectedHoverBackgroundId,
                selectedColor,
                0xFFBBBBBB,
                false);
        }

        /**
         * Set the theme options for a ToggleButton widget.
         *
         * @param backgroundId              The main background for the unpressed button
         * @param hoverBackgroundId         The on-hover background for the unpressed button
         * @param selectedBackgroundId      The main background for the pressed button
         * @param selectedHoverBackgroundId The on-hover background for the pressed button
         * @param selectedColor             The color to apply to the pressed button
         * @param textColor                 The color for text overlaid on this button
         * @param textShadow                Whether to apply text shadow to text overlaid on this button
         */
        public Builder toggleButton(String backgroundId, String hoverBackgroundId, String selectedBackgroundId,
            String selectedHoverBackgroundId, int selectedColor, int textColor, boolean textShadow) {
            theme.elementBuilder.add(
                b -> b.add(
                    "toggleButton",
                    new JsonBuilder().add(
                        "background",
                        new JsonBuilder().add("type", "texture")
                            .add("id", backgroundId))
                        .add(
                            "hoverBackground",
                            new JsonBuilder().add("type", "texture")
                                .add("id", hoverBackgroundId))
                        .add(
                            "selectedBackground",
                            new JsonBuilder().add("type", "texture")
                                .add("id", selectedBackgroundId))
                        .add(
                            "selectedHoverBackground",
                            new JsonBuilder().add("type", "texture")
                                .add("id", selectedHoverBackgroundId))
                        .add("selectedColor", selectedColor)
                        .add("textColor", textColor)
                        .add("textShadow", textShadow)));
            return this;
        }

        /**
         * Simple toggle button configuration for when you want a button with no texture changes on hover.
         *
         * @param backgroundId         The unselected background texture
         * @param selectedBackgroundId The selected background texture
         * @param selectedColor        The background color when the button is selected
         */
        public Builder simpleToggleButton(String backgroundId, String selectedBackgroundId, int selectedColor) {
            return simpleToggleButton(backgroundId, selectedBackgroundId, selectedColor, 0xFFBBBBBB, false);
        }

        /**
         * Simple toggle button configuration for when you want a button with no texture changes on hover.
         *
         * @param backgroundId         The unselected background texture
         * @param selectedBackgroundId The selected background texture
         * @param selectedColor        The background color when the button is selected
         * @param textColor            The color for text overlaid on this button
         * @param textShadow           Whether to apply text shadow to text overlaid on this button
         */
        public Builder simpleToggleButton(String backgroundId, String selectedBackgroundId, int selectedColor,
            int textColor, boolean textShadow) {
            return toggleButton(
                backgroundId,
                backgroundId,
                selectedBackgroundId,
                selectedBackgroundId,
                selectedColor,
                textColor,
                textShadow);
        }

        /**
         * Applies text color for specific widget theme.
         *
         * @param textType WidgetTheme ID in {@link GTWidgetThemes}
         * @param color    Text color
         */
        public Builder customTextColor(String textType, int color) {
            theme.elementBuilder.add(b -> b.add(textType, new JsonBuilder().add("textColor", color)));
            return this;
        }

        /**
         * Set a logo for this theme.
         */
        public Builder logo(UITexture logo) {
            theme.logo = logo;
            return this;
        }

        public GTGuiTheme build() {
            return theme;
        }
    }
}
