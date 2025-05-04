package gregtech.common.modularui2.widget;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;
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
 * color.
 */
public class ColorGridWidget extends Grid {

    public List<Byte> selected = new ArrayList<>();
    public int maxSelected = 16;
    public int buttonSize = 9;
    public int buttonPadding = 1;
    public int borderSize = 1;

    public ColorGridWidget() {
        super();
    }

    /**
     * @param value The initial selected button index (0-15) for the grid. Enter a number outside bounds for none.
     * @return this
     */
    public ColorGridWidget setInitialSelected(byte value) {
        if (value >= 0 && value < 16) {
            this.selected = ImmutableList.of(value);
        }
        return this;
    }

    /**
     *
     * @param value The initial selected buttons index (0-15) for the grid. Enter null for none.
     * @return
     */
    public ColorGridWidget setInitialSelected(List<Byte> value) {
        if (value == null) return this;
        this.selected = value;
        return this;
    }

    /**
     * @param value The desired maximum number of colors that can be selected at once
     * @return this
     */
    public ColorGridWidget setMaxSelected(int value) {
        this.maxSelected = value;
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
        return new ColorGridButton(index) {

            @Override
            public void onInit() {
                super.onInit();
                if (selected.contains((byte) index) && maxSelected >= selected.size()) {
                    this.setState(1, true);
                }
            }

            @Override
            public @NotNull Result onMousePressed(int mouseButton) {
                onToggled(index);
                return super.onMousePressed(mouseButton);
            }
        }.background(false, drawButton(Color.multiply(color, 0.5F, false)))
            .background(true, drawButton(color))
            .hoverOverlay(drawButton(Color.multiply(color, 0.7F, false)))
            .tooltip(tooltip -> tooltip.add(Dyes.VALUES[index].getLocalizedDyeName()))
            .size(buttonSize, buttonSize);
    }

    public IDrawable drawButtonBorder() {
        return (context, x, y, w, h, widgetTheme) -> {
            new Rectangle().setColor(Color.BLACK.main)
                .draw(context, new Area(x, y, buttonSize, buttonSize), widgetTheme);
        };
    }

    public IDrawable drawButton(int color) {
        int insideSize = buttonSize - (borderSize * 2);
        return IDrawable.of(drawButtonBorder(), (context, x, y, w, h, widgetTheme) -> {
            new Rectangle().setColor(color)
                .draw(context, new Area(x + borderSize, y + borderSize, insideSize, insideSize), widgetTheme);
        });
    }

    public void onToggled(int index) {
        List<IWidget> children = this.getChildren();

        if (!selected.contains((byte) index)) {
            selected.add((byte) index);
        }

        for (IWidget child : children) {
            if (!(child instanceof ColorGridButton button)) continue;
            if (!selected.contains((byte) button.index)) {
                button.setState(0, true);
            }
        }

        if (this.selected.size() > this.maxSelected) {
            IWidget targetChild = children.get(selected.get(0));
            if (!(targetChild instanceof ColorGridButton button)) return;
            // target locked; disable the child
            selected.remove(0);
            button.setState(0, true);
        }
    }

    /**
     * @return The name of the current selected color.
     */
    public String getName(int i) {
        int select = selected.get(i);
        return select >= 0 && select < 16 ? getDye(i).getLocalizedDyeName() : "None";
    }

    /**
     * @return The currently selected button's EnumChatFormatting
     */
    public EnumChatFormatting getColorFormatting(int i) {
        int select = selected.get(i);
        return select >= 0 && select < 16 ? Dyes.VALUES[select].formatting : null;
    }

    /**
     * @return The current selected button's Dyes
     */
    public Dyes getDye(int i) {
        int select = selected.get(i);
        return select >= 0 && select < 16 ? Dyes.VALUES[select] : null;
    }

    /**
     * @return The list of selected button's indexes
     */
    public List<Byte> getSelected() {
        return this.selected;
    }

    public static class ColorGridButton extends ToggleButton {

        int index;

        public ColorGridButton(int value) {
            super();
            this.index = value;
        }
    }

}
