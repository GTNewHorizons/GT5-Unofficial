package gregtech.common.modularui2.widget;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.EnumChatFormatting;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.Rectangle;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.widget.sizer.Area;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Grid;

import gregtech.api.enums.Dyes;

/**
 * A Grid containing 16 ToggleButtons arranged in a 4x4 pattern. Each button represents a different minecraft dye
 * color and only one button can be selected at a time.
 */
public class ColorGridWidget extends Grid {

    public int selected = -1;
    public int buttonSize = 9;
    public int buttonPadding = 1;
    public int borderSize = 1;

    public ColorGridWidget() {
        super();
    }

    public ColorGridWidget(int select) {
        this();
        this.selected = select;
    }

    public ColorGridWidget(int buttonSize, int buttonPadding, int borderSize) {
        this();
        this.buttonSize = buttonSize;
        this.buttonPadding = buttonPadding;
        this.borderSize = borderSize;
    }

    /**
     * @param value The initial selected button index (0-15) for the grid. Enter a negative number for none.
     * @return this
     */
    public ColorGridWidget setInitialSelected(int value) {
        this.selected = value;
        return this;
    }

    /**
     * @param value The total width/height desired for every button in the grid
     * @return this
     */
    public ColorGridWidget setButtonSize(int value) {
        this.buttonSize = value;
        return this;
    }

    /**
     * @param value The desired space between every button in the grid
     * @return this
     */
    public ColorGridWidget setButtonPadding(int value) {
        this.buttonPadding = value;
        return this;
    }

    /**
     * @param value The desired thickness of the border for every button in the grid
     * @return this
     */
    public ColorGridWidget setBorderSize(int value) {
        this.borderSize = value;
        return this;
    }

    /**
     * Creates the ToggleButtons and the Grid
     *
     * @return this
     */
    public ColorGridWidget build() {
        int size = (buttonSize * 4) + (buttonPadding * 4);
        this.matrix(createButtonMatrix())
            .size(size, size)
            .minColWidth(buttonSize + buttonPadding)
            .minRowHeight(buttonSize + buttonPadding);
        return this;
    }

    public List<List<IWidget>> createButtonMatrix() {
        List<List<IWidget>> widgets = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            List<IWidget> row = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                Dyes dye = Dyes.VALUES[(i * 4) + j];
                short[] colors = dye.getRGBA();
                row.add(createToggleButton(Color.argb(colors[0], colors[1], colors[2], 255), ((i * 4) + j)));
            }
            widgets.add(row);
        }
        return widgets;
    }

    public ToggleButton createToggleButton(int color, int index) {
        return new ToggleButton() {

            @Override
            public void onInit() {
                super.onInit();
                if (index == selected) {
                    this.setState(1, true);
                }
            }

            @Override
            public @NotNull Result onMousePressed(int mouseButton) {
                onToggled(index);
                return super.onMousePressed(mouseButton);
            }
        }.background(false, drawButton(color, false, false))
            .background(true, drawButton(color, true, false))
            .hoverOverlay(drawButton(color, false, true))
            .tooltip(tooltip -> tooltip.add(Dyes.VALUES[index].getLocalizedDyeName()))
            .size(buttonSize, buttonSize);
    }

    public IDrawable drawButtonBorder() {
        return (context, x, y, w, h, widgetTheme) -> {
            new Rectangle().setColor(Color.BLACK.main)
                .draw(context, new Area(x, y, buttonSize, buttonSize), widgetTheme);
        };
    }

    public IDrawable drawButton(int c, boolean selected, boolean overlay) {
        int insideSize = buttonSize - (borderSize * 2);
        int color;
        if (selected) {
            color = c;
        } else if (overlay) {
            color = Color.multiply(c, 0.7F, false);
        } else {
            color = Color.multiply(c, 0.5F, false);
        }
        return IDrawable.of(drawButtonBorder(), (context, x, y, w, h, widgetTheme) -> {
            new Rectangle().setColor(color)
                .draw(context, new Area(x + borderSize, y + borderSize, insideSize, insideSize), widgetTheme);
        });
    }

    public void onToggled(int except) {
        this.selected = except;
        for (IWidget child : this.getChildren()) {
            if (!(child instanceof ToggleButton button)) continue;
            button.setState(0, true);
        }
    }

    /**
     * @return The name of the current selected color.
     */
    public String getName() {
        return selected >= 0 && selected < 16 ? getDye().getLocalizedDyeName() : "None";
    }

    /**
     * @return The currently selected button's EnumChatFormatting
     */
    public EnumChatFormatting getColorFormatting() {
        return selected >= 0 && selected < 16 ? Dyes.VALUES[selected].formatting : null;
    }

    /**
     * @return The current selected button's Dyes
     */
    public Dyes getDye() {
        return selected >= 0 && selected < 16 ? Dyes.VALUES[selected] : null;
    }
}
